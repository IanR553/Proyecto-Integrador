package controller;

import data.DBConnectionFactory;
import data.EquipoDAO;
import data.HorarioDAO;
import data.ReservaDAO;
import data.ReservaEquipoDAO;
import data.UserSession;
import model.Reserva;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Equipo;
import model.ReservaEquipo;
import application.Main;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EquipoController {

    @FXML private ComboBox<String> cbxEquipos;
    @FXML private ComboBox<String> cbxSoftware;
    @FXML private ComboBox<Integer> comBoxSemana;
    @FXML private DatePicker datePicker;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFin;
    @FXML private Button btnBuscarSalas;
    @FXML private Button btnReservar;

    @FXML private TableView<Equipo> tableSalasDisponibles;
    @FXML private TableColumn<Equipo, String> colIdSala;
    @FXML private TableColumn<Equipo, String> colNombreSala;
    @FXML private TableColumn<Equipo, String> colUbicacionSala;
    @FXML private TableColumn<Equipo, Integer> colCapacidadSala;

    private Connection connection;
    private EquipoDAO equipoDAO;
    private HorarioDAO horarioDAO;
    private ReservaDAO reservaDAO;
    private ReservaEquipoDAO reservaEquipoDAO;
    
    private ObservableList<Equipo> listaEquiposDisponibles;

    @FXML
    public void initialize() {
        connection = DBConnectionFactory.getConnectionByRole(UserSession.getInstance().getRole()).getConnection();
        equipoDAO = new EquipoDAO(connection);
        horarioDAO = new HorarioDAO(connection);
        reservaDAO = new ReservaDAO(connection);
        reservaEquipoDAO = new ReservaEquipoDAO(connection);

        cbxEquipos.getItems().addAll("Portatil", "Camara", "Microfono", "Proyector", "Televisor", "Cable HDMI");
        cbxSoftware.getItems().addAll("Office", "AutoCAD", "Eclipse", "Visual Studio", "Ninguno");
        comBoxSemana.getItems().addAll(1, 2);

        colIdSala.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreSala.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colUbicacionSala.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colCapacidadSala.setCellValueFactory(new PropertyValueFactory<>("estadoTexto"));

        btnBuscarSalas.setOnAction(e -> buscarEquiposDisponibles());
        btnReservar.setOnAction(e -> reservarEquipo());

        tableSalasDisponibles.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            btnReservar.setDisable(newSel == null);
        });

        // Solo permitir seleccionar la fecha actual
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || !date.equals(LocalDate.now()));
            }
        });
        datePicker.setValue(LocalDate.now());
    }

    private void buscarEquiposDisponibles() {
        String tipo = cbxEquipos.getValue();
        String software = cbxSoftware.getValue();
        String horaInicioStr = txtHoraInicio.getText().trim();
        String horaFinStr = txtHoraFin.getText().trim();
        LocalDate fechaSeleccionada = datePicker.getValue();
        Integer semana = comBoxSemana.getValue();

        if (tipo == null || semana == null || horaInicioStr.isEmpty() || horaFinStr.isEmpty()) {
            Main.showAlert("Debe seleccionar el tipo de equipo, semana y completar las horas.", "Datos incompletos", Alert.AlertType.WARNING);
            return;
        }

        // Buscar equipos disponibles
        ArrayList<Equipo> disponibles = equipoDAO.fetchDisponibles(tipo, software);
        listaEquiposDisponibles = FXCollections.observableArrayList(disponibles);
        tableSalasDisponibles.setItems(listaEquiposDisponibles);
    }

    private void reservarEquipo() {
    	
    	String horaInicioStr = txtHoraInicio.getText().trim();
        String horaFinStr = txtHoraFin.getText().trim();
    	LocalDate fechaSeleccionada = datePicker.getValue();
    	Integer semana = comBoxSemana.getValue();
    	LocalTime horaInicio;
        LocalTime horaFin;

        try {
            horaInicio = LocalTime.parse(horaInicioStr);
            horaFin = LocalTime.parse(horaFinStr);
        } catch (Exception e) {
            Main.showAlert("Formato de hora incorrecto. Use HH:mm (ej. 14:30).", "Error de formato", Alert.AlertType.ERROR);
            return;
        }

        // Obtener nombre del día en español
        String diaSemana = fechaSeleccionada.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));

        // Buscar idHorario
        String idHorario = horarioDAO.traerIdHorario(semana, diaSemana, horaInicio, horaFin);
        
     // Validar si se encontró horario
        if (idHorario == null) {
            Main.showAlert("No se encontró un horario con los datos proporcionados.", "Horario no encontrado", Alert.AlertType.WARNING);
            return;
        }
        
        //cedula del usuario
        long cedula = Long.parseLong(UserSession.getInstance().getUsername());
        
        Equipo seleccionado = tableSalasDisponibles.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            Main.showAlert("Debe seleccionar un equipo para reservar.", "Sin selección", Alert.AlertType.WARNING);
            return;
        }
        
        Reserva reservaEqui = new Reserva("Pendiente", "Equipo", cedula, idHorario);
        reservaDAO.save(reservaEqui);
        equipoDAO.updateNoDisponible(seleccionado.getId());
        
        String idReserva = reservaDAO.traerIdPorCedulaYHorario(reservaEqui.getCedUsuario(), reservaEqui.getIdHorario());
        
        ReservaEquipo equipoReservado = new ReservaEquipo(idReserva,seleccionado.getId());
        boolean exito = reservaEquipoDAO.save(equipoReservado);


        if (exito) {
            Main.showAlert("Equipo reservado exitosamente.", "Éxito", Alert.AlertType.INFORMATION);
            buscarEquiposDisponibles();
        } else {
            Main.showAlert("No se pudo completar la reserva. Verifique disponibilidad o conflicto de horarios.", "Error", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void regresarMenu() {
    	Main.loadView("/view/ReservaE.fxml");
    }
}



