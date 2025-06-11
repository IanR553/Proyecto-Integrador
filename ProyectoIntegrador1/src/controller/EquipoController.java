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
import java.util.ArrayList;


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
    private ReservaFacade reservaFacade;

    
    private ObservableList<Equipo> listaEquiposDisponibles;

    @FXML
    public void initialize() {
        connection = DBConnectionFactory.getConnectionByRole(UserSession.getInstance().getRole()).getConnection();
        equipoDAO = new EquipoDAO(connection);
        horarioDAO = new HorarioDAO(connection);
        reservaDAO = new ReservaDAO(connection);
        reservaEquipoDAO = new ReservaEquipoDAO(connection);

        cbxEquipos.getItems().addAll("PORTATIL", "CAMARA", "MICROFONO", "PROYECTOR", "TELEVISOR", "CABLE HDMI");
        ArrayList<String> softwaresBD = equipoDAO.obtenerSoftwaresUnicos();
        softwaresBD.add("Ninguno"); // 
        cbxSoftware.getItems().addAll(softwaresBD);

        comBoxSemana.getItems().addAll(1, 2);

        colIdSala.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreSala.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colUbicacionSala.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colCapacidadSala.setCellValueFactory(new PropertyValueFactory<>("software"));


        btnBuscarSalas.setOnAction(e -> buscarEquiposDisponibles());
        btnReservar.setOnAction(e -> reservarEquipo());

        tableSalasDisponibles.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            btnReservar.setDisable(newSel == null);
        });
        
        datePicker.setValue(LocalDate.now());
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

        LocalTime horaInicio;
        LocalTime horaFin;
        try {
            horaInicio = LocalTime.parse(horaInicioStr);
            horaFin = LocalTime.parse(horaFinStr);
        } catch (Exception e) {
            Main.showAlert("Formato de hora incorrecto. Use HH:mm (ej. 14:00).", "Error de formato", Alert.AlertType.ERROR);
            return;
        }

        // Obtener nombre del día en español
        String diaSemana = switch ( fechaSeleccionada.getDayOfWeek()) {
	        case MONDAY -> "lunes";
	        case TUESDAY -> "martes";
	        case WEDNESDAY -> "miercoles"; 
	        case THURSDAY -> "jueves";
	        case FRIDAY -> "viernes";
	        case SATURDAY -> "sabado";
	        case SUNDAY -> "domingo";
        };
        
        System.out.println(semana + ", " + diaSemana + ", " + horaInicio + ", " + horaFin);
        String idHorario = horarioDAO.traerIdHorario(semana, diaSemana, horaInicio, horaFin);
        System.out.println(idHorario);
        if (idHorario == null) {
            Main.showAlert("No se encontró un horario con los datos proporcionados.", "Horario no encontrado", Alert.AlertType.WARNING);
            return;
        }

        ArrayList<Equipo> disponibles = equipoDAO.fetchDisponiblesPorHorario(tipo, software, idHorario);
        listaEquiposDisponibles = FXCollections.observableArrayList(disponibles);
        tableSalasDisponibles.setItems(listaEquiposDisponibles);

        System.out.println("Buscando equipos tipo: " + tipo + ", software: " + software);
        System.out.println("Equipos disponibles: " + disponibles.size());
    }


    @FXML
    private void reservarEquipo() {
        String horaInicioStr = txtHoraInicio.getText().trim();
        String horaFinStr = txtHoraFin.getText().trim();
        LocalDate fechaSeleccionada = datePicker.getValue();
        Integer semana = comBoxSemana.getValue();
        Equipo seleccionado = tableSalasDisponibles.getSelectionModel().getSelectedItem();

        reservaFacade = new ReservaFacade(horarioDAO, reservaDAO, null, reservaEquipoDAO);

        boolean exito = reservaFacade.realizarReservaEquipo(
            horaInicioStr,
            horaFinStr,
            fechaSeleccionada,
            semana,
            seleccionado
        );

        if (exito) {
            buscarEquiposDisponibles(); 
        }
    }

    
    @FXML
    private void regresarMenu() {
        String rol = UserSession.getInstance().getRole();

        if (rol.equals("Estudiante")) {
            Main.loadView("/view/ReservaE.fxml");
        } else if (rol.equals("Profesor") || rol.equals("Administrativo")) {
            Main.loadView("/view/ReservaU.fxml");
        } else {
            Main.loadView("/view/GestionReservas.fxml");
        }
    }

}



