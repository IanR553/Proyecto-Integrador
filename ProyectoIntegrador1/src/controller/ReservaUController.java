package controller;

import data.DBConnectionFactory;
import data.ReservaDAO;
import data.ReservaEquipoDAO;
import data.ReservaSalaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ReservaEquipo;
import model.ReservaSala;
import data.UserSession;
import application.Main;

import java.sql.Connection;
import java.util.ArrayList;

public class ReservaUController {

    @FXML private TableView<ReservaSala> tableSalas;
	@FXML private TableColumn<ReservaSala, String> colTipoS;
	@FXML private TableColumn<ReservaSala, String> colEstadoSala;
	@FXML private TableColumn<ReservaSala, String> colNombreSala;
	@FXML private TableColumn<ReservaSala, String> colSoftwareSala;
	@FXML private TableColumn<ReservaSala, String> colUbicacionSala;
	@FXML private TableColumn<ReservaSala, Integer> colCapacidadSala;

    @FXML private TableView<ReservaEquipo> tableEquipos;
	@FXML private TableColumn<ReservaEquipo, String> colTipoE;
	@FXML private TableColumn<ReservaEquipo, String> colEstadoEquipo;
	@FXML private TableColumn<ReservaEquipo, String> colTipoEquipo;
	@FXML private TableColumn<ReservaEquipo, String> colMarcaEquipo;
	@FXML private TableColumn<ReservaEquipo, String> colSoftwareEquipo;


    private Connection connection = DBConnectionFactory.getConnectionByRole(UserSession.getInstance().getRole()).getConnection();
    private ReservaEquipoDAO reservaEquipoDAO = new ReservaEquipoDAO(connection);
    private ReservaSalaDAO reservaSalaDAO = new ReservaSalaDAO(connection);
    private ReservaDAO reservaDAO = new ReservaDAO(connection);
    
    @FXML
    public void initialize() {
    	
    	colTipoS.setCellValueFactory(new PropertyValueFactory<>("tipo"));
    	colEstadoSala.setCellValueFactory(new PropertyValueFactory<>("estado"));
    	colNombreSala.setCellValueFactory(new PropertyValueFactory<>("nombreSala"));
    	colSoftwareSala.setCellValueFactory(new PropertyValueFactory<>("softwareSala"));
    	colUbicacionSala.setCellValueFactory(new PropertyValueFactory<>("ubicacionSala"));
    	colCapacidadSala.setCellValueFactory(new PropertyValueFactory<>("capacidadSala"));
    	
    	colTipoE.setCellValueFactory(new PropertyValueFactory<>("tipo"));
    	colEstadoEquipo.setCellValueFactory(new PropertyValueFactory<>("estado"));
    	colTipoEquipo.setCellValueFactory(new PropertyValueFactory<>("tipoEquipo"));
    	colMarcaEquipo.setCellValueFactory(new PropertyValueFactory<>("marca"));
    	colSoftwareEquipo.setCellValueFactory(new PropertyValueFactory<>("software"));
    	
        cargarReservas();
    }

    private void cargarReservas() {
        long cedula = Long.parseLong(UserSession.getInstance().getUsername());
        ArrayList<ReservaEquipo> reservasEquipo = reservaEquipoDAO.obtenerReservasConEquiposPorUsuario(cedula);
        ArrayList<ReservaSala> reservasSala = reservaSalaDAO.obtenerReservasSalasPorUsuario(cedula);
        ObservableList<ReservaEquipo> listaEquipos = FXCollections.observableArrayList(reservasEquipo);
        ObservableList<ReservaSala> listaSalas = FXCollections.observableArrayList(reservasSala);
        tableSalas.setItems(listaSalas);
        tableEquipos.setItems(listaEquipos);
    }

    @FXML
    private void actionReservarSala() {
        Main.loadView("/view/sala.fxml");
    }

    @FXML
    private void actionReservarEquipo() {
        Main.loadView("/view/Equipo.fxml");
    }

    @FXML
    private void actionEliminarReserva() {
        ReservaSala seleccionadoSala = tableSalas.getSelectionModel().getSelectedItem();
        ReservaEquipo seleccionadoEquipo = tableEquipos.getSelectionModel().getSelectedItem();

        if (seleccionadoSala == null && seleccionadoEquipo == null) {
            Main.showAlert("Debe seleccionar una reserva de sala o de equipo para eliminar.", "Sin selección", Alert.AlertType.WARNING);
            return;
        }

        boolean reservaEliminada = false;

        if (seleccionadoSala != null) {
            reservaSalaDAO.delete(seleccionadoSala.getIdReserva(), seleccionadoSala.getIdSala(), seleccionadoSala.getIdEquipo());
            System.out.println("Eliminando reserva de sala: idReserva=" + seleccionadoSala.getIdReserva());
            // Verifica si no hay otra reserva de equipo asociada a esta reserva
            if (!reservaEquipoDAO.existePorIdReserva(seleccionadoSala.getIdReserva())) {
                reservaDAO.delete(seleccionadoSala.getIdReserva());
                reservaEliminada = true;
            }
        }

        if (seleccionadoEquipo != null) {
            reservaEquipoDAO.delete(seleccionadoEquipo.getIdReserva(), seleccionadoEquipo.getIdEquipo());
            System.out.println("Eliminando reserva de equipo: idReserva=" + seleccionadoEquipo.getIdReserva());
            if (!reservaSalaDAO.existePorIdReserva(seleccionadoEquipo.getIdReserva()) && !reservaEliminada) {
                reservaDAO.delete(seleccionadoEquipo.getIdReserva());
            }
        }

        cargarReservas();
    }

    
    @FXML
    private void actionModificarReserva() {
        cargarReservas(); 
    }

    @FXML
    private void actionCerrarSesion() {
        UserSession.getInstance().destroy();
        Main.loadView("/view/Login.fxml");
    }
}

