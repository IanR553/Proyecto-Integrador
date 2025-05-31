package controller;

import data.DBConnectionFactory;
import data.EquipoDAO;
import data.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.Equipo;
import application.Main;

import java.sql.Connection;
import java.util.ArrayList;


public class ReservaEController {

    @FXML private Button btnequipo;
    @FXML private Button btnCerrarSesion;
    @FXML private TableView<Equipo> tableEquipos;
    @FXML private TableColumn<Equipo, String> colIdEquipo;
    @FXML private TableColumn<Equipo, String> colNombreEquipo;

    private Connection connection;
    private EquipoDAO equipoDAO;
    private ObservableList<Equipo> listaEquiposReservados;

    @FXML
    public void initialize() {
        // Inicializar conexión y DAO
        connection = DBConnectionFactory.getConnectionByRole(UserSession.getInstance().getRole()).getConnection();
        equipoDAO = new EquipoDAO(connection);

        // Configurar columnas de la tabla
        colIdEquipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        colNombreEquipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));

        // Cargar equipos reservados por el estudiante
        cargarEquiposReservados();

        // Acción botón para ir a reservar equipo
        btnequipo.setOnAction(e -> Main.loadView("/view/Equipo.fxml"));

        // Acción cerrar sesión
        btnCerrarSesion.setOnAction(e -> {
            UserSession.getInstance().destroy();
            Main.loadView("/view/Login.fxml");
        });
    }

    private void cargarEquiposReservados() {
        long cedulaEstudiante = Long.parseLong(UserSession.getInstance().getUsername());
        ArrayList<Equipo> equipos = equipoDAO.fetch();
        listaEquiposReservados = FXCollections.observableArrayList(equipos);
        tableEquipos.setItems(listaEquiposReservados);
    }
}

