package controller;

import java.sql.Connection;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import application.Main;
import data.DBConnectionFactory;
import data.EquipoDAO;
import data.HorarioDAO;
import data.ReservaDAO;
import data.ReservaEquipoDAO;
import data.ReservaSalaDAO;
import data.SalaDAO;
import data.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Equipo;
import model.Reserva;
import model.ReservaEquipo;
import model.ReservaSala;
import model.Sala;

public class SalaController {

    @FXML private ComboBox<String> comBoxSoftwareSala;
    @FXML private ComboBox<String> comBoxEquipo;
    @FXML private ComboBox<String> comBoxSoftwareEquipo;
    @FXML private ComboBox<Integer> comBoxSemana;
    
    @FXML private TableView<Sala> tableSalasDisponibles;
    @FXML private TableColumn<Sala, String> colIdSala;
    @FXML private TableColumn<Sala, String> colNombreSala;
    @FXML private TableColumn<Sala, String> colSoftwareSala;
    @FXML private TableColumn<Sala, Integer> colCapacidadSala;
    
    @FXML private TableView<Equipo> tableEquiposDisponibles;
    @FXML private TableColumn<Equipo, String> colIdEquipo;
    @FXML private TableColumn<Equipo, String> colTipoEquipo;
    @FXML private TableColumn<Equipo, String> colMarcaEquipo;
    @FXML private TableColumn<Equipo, String> colSoftwareEquipo;
    
    @FXML private DatePicker dpFecha;
    
    @FXML private TextField txtCapacidad;
    @FXML private TextField txtHoraFin;
    @FXML private TextField txtHoraInicio;

    private Connection connection;
    private EquipoDAO equipoDAO;
    private SalaDAO salaDAO;
    private HorarioDAO horarioDAO;
    private ReservaDAO reservaDAO;
    private ReservaEquipoDAO reservaEquipoDAO;
    private ReservaSalaDAO reservaSalaDAO;
    
    private ObservableList<Equipo> listaEquiposDisponibles;
    private ObservableList<Sala> listaSalasDisponibles;
    
    @FXML
    public void initialize() {
        connection = DBConnectionFactory.getConnectionByRole(UserSession.getInstance().getRole()).getConnection();
        equipoDAO = new EquipoDAO(connection);
        salaDAO = new SalaDAO(connection);
        horarioDAO = new HorarioDAO(connection);
        reservaDAO = new ReservaDAO(connection);
        reservaEquipoDAO = new ReservaEquipoDAO(connection);
        reservaSalaDAO = new ReservaSalaDAO(connection);
        
        comBoxEquipo.getItems().addAll("PORTATIL", "CAMARA", "MICROFONO", "PROYECTOR", "TELEVISOR", "CABLE HDMI", "NINGUNO");
        ArrayList<String> softwaresBDEquipo = equipoDAO.obtenerSoftwaresUnicos();
        softwaresBDEquipo.add("Ninguno");
        comBoxSoftwareEquipo.getItems().addAll(softwaresBDEquipo);
        comBoxEquipo.setOnAction(e -> {
            String tipoEquipoSeleccionado = comBoxEquipo.getValue();
            boolean esNinguno = "NINGUNO".equalsIgnoreCase(tipoEquipoSeleccionado);
            tableEquiposDisponibles.setDisable(esNinguno);
        });


        ArrayList<String> softwaresBDSala = salaDAO.obtenerSoftwaresUnicos();
        softwaresBDSala.add("Ninguno");
        comBoxSoftwareSala.getItems().addAll(softwaresBDSala);
        
        comBoxSemana.getItems().addAll(1, 2);
        
        colIdEquipo.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipoEquipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colMarcaEquipo.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colSoftwareEquipo.setCellValueFactory(new PropertyValueFactory<>("software"));
        
        colIdSala.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreSala.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSoftwareSala.setCellValueFactory(new PropertyValueFactory<>("software"));
        colCapacidadSala.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        
        dpFecha.setValue(LocalDate.now());
        // Configuración del DatePicker para deshabilitar sábados, domingos, días antes de hoy, y días después del viernes
        dpFecha.setDayCellFactory((Callback<DatePicker, DateCell>) new Callback<DatePicker, DateCell>() {
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);

                        // Obtén la fecha de hoy
                        LocalDate today = LocalDate.now();

                        // Calcula el inicio (lunes) y el fin (viernes) de la semana actual
                        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
                        LocalDate endOfWeek = today.with(DayOfWeek.FRIDAY);

                        // Deshabilita sábados y domingos
                        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            setDisable(true);
                        }

                        // Deshabilita los días después de la semana actual (viernes)
                        if (date.isAfter(endOfWeek)) {
                            setDisable(true);
                        }

                        // Deshabilita los días anteriores a hoy
                        if (date.isBefore(today)) {
                            setDisable(true);
                        }
                    }
                };
            }
        });
        
    }
    
	public void actionBuscarSalas() {

		String horaInicioStr = txtHoraInicio.getText().trim();
		String horaFinStr = txtHoraFin.getText().trim();
		LocalDate fechaSeleccionada = dpFecha.getValue();
		Integer semana = comBoxSemana.getValue();
		String tipoEquipo = comBoxEquipo.getValue();
		String softSala = comBoxSoftwareSala.getValue();
		String softEqui = comBoxSoftwareEquipo.getValue();
		String capacidad = txtCapacidad.getText().trim();
		
        if (tipoEquipo == null || semana == null || horaInicioStr.isEmpty() || horaFinStr.isEmpty() || softSala == null || softEqui == null || capacidad == null) {
            Main.showAlert("Debe llenar todos los datos para continuar.", "Datos incompletos", Alert.AlertType.WARNING);
            return;
        }
        
		int capacidadInt = Integer.parseInt(capacidad);
		if (capacidadInt <= 0) {
			Main.showAlert("La capacidad debe ser mayor que 0.", "Capacidad inválida", Alert.AlertType.WARNING);
		}

		LocalTime horaInicio;
		LocalTime horaFin;
		
		try {
			horaInicio = LocalTime.parse(horaInicioStr);
			horaFin = LocalTime.parse(horaFinStr);
		} catch (Exception e) {
			Main.showAlert("Formato de hora incorrecto. Use HH:mm (ej. 14:00).", "Error de formato",
					Alert.AlertType.ERROR);
			return;
		}

		// Obtener nombre del día en español
		String diaSemana = switch (fechaSeleccionada.getDayOfWeek()) {
		case MONDAY -> "lunes";
		case TUESDAY -> "martes";
		case WEDNESDAY -> "miercoles";
		case THURSDAY -> "jueves";
		case FRIDAY -> "viernes";
		case SATURDAY -> "sabado";
		case SUNDAY -> "domingo";
		};

		// Buscar idHorario
		String idHorario = horarioDAO.traerIdHorario(semana, diaSemana, horaInicio, horaFin);
        System.out.println(idHorario);
		
        if (idHorario == null) {
            Main.showAlert("No se encontró un horario con los datos proporcionados.", "Horario no encontrado", Alert.AlertType.WARNING);
            return;
        }
        	
        ArrayList<Sala> salasDisponibles = salaDAO.fetchDisponiblesPorHorario(softSala, capacidadInt, idHorario);
        listaSalasDisponibles = FXCollections.observableArrayList(salasDisponibles);
        tableSalasDisponibles.setItems(listaSalasDisponibles);
        
        ArrayList<Equipo> equiposDisponibles = equipoDAO.fetchDisponiblesPorHorario(tipoEquipo, softEqui, idHorario);
        listaEquiposDisponibles = FXCollections.observableArrayList(equiposDisponibles);
        tableEquiposDisponibles.setItems(listaEquiposDisponibles);

	}
    
	public void actionReservar() {
	    String horaInicioStr = txtHoraInicio.getText().trim();
	    String horaFinStr = txtHoraFin.getText().trim();
	    LocalDate fechaSeleccionada = dpFecha.getValue();
	    Integer semana = comBoxSemana.getValue();
	    String tipoEquipo = comBoxEquipo.getValue();

	    Sala salaSeleccionada = tableSalasDisponibles.getSelectionModel().getSelectedItem();
	    Equipo equipoSeleccionado = tableEquiposDisponibles.getSelectionModel().getSelectedItem();

	    ReservaFacade reservaFacade = new ReservaFacade(horarioDAO, reservaDAO, reservaSalaDAO, reservaEquipoDAO);
	    boolean exito = reservaFacade.realizarReserva(horaInicioStr, horaFinStr, fechaSeleccionada, semana,
	                                                  tipoEquipo, salaSeleccionada, equipoSeleccionado);

	    if (exito) {
	        actionBuscarSalas();
	    }
	}


    
    public void actionVolverMenu() {
    	Main.loadView("/view/reservaU.fxml");
    }
}

