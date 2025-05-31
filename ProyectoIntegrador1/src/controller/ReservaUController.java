package controller;

import data.DBConnectionFactory;
import data.ReservaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Reserva;
import data.UserSession;
import application.Main;

import java.sql.Connection;

public class ReservaUController {

    @FXML private TableView<Reserva> tableSalas;
    @FXML private TableColumn<Reserva, String> colIdSala;
    @FXML private TableColumn<Reserva, String> colNombreSala;

    @FXML private TableView<Reserva> tableEquipos;
    @FXML private TableColumn<Reserva, String> colIdEquipo;
    @FXML private TableColumn<Reserva, String> colNombreEquipo;

    @FXML private Button btnReservaSala;
    @FXML private Button btnReservaEquipo;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    @FXML private Button btnCerrarSesion;

    private Connection connection;
    private ReservaDAO reservaDAO;
    private ObservableList<Reserva> reservasSalas;
    private ObservableList<Reserva> reservasEquipos;

    @FXML
    public void initialize() {
        String rol = UserSession.getInstance().getRole();
        connection = DBConnectionFactory.getConnectionByRole(rol).getConnection();
        reservaDAO = new ReservaDAO(connection);

        configurarColumnas();
        cargarReservas();
    }

    private void configurarColumnas() {
        colIdSala.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        //colNombreSala.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));

        colIdEquipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        //colNombreEquipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
    }

    private void cargarReservas() {
        String username = UserSession.getInstance().getUsername();

       // reservasSalas = FXCollections.observableArrayList(reservaDAO.obtenerReservasPorUsuario(username, "sala"));
       // reservasEquipos = FXCollections.observableArrayList(reservaDAO.obtenerReservasPorUsuario(username, "equipo"));

        tableSalas.setItems(reservasSalas);
        tableEquipos.setItems(reservasEquipos);
    }

    @FXML
    private void handleReservarSala() {
        Main.loadView("/view/sala.fxml");
    }

    @FXML
    private void handleReservarEquipo() {
        Main.loadView("/view/Equipo.fxml");
    }

    @FXML
    private void handleEliminar() {
        cargarReservas(); 
    }
    
    @FXML
    private void handleModificar() {
        cargarReservas(); 
    }

    @FXML
    private void handleCerrarSesion() {
        UserSession.getInstance().destroy();
        Main.loadView("/view/Login.fxml");
    }
}

