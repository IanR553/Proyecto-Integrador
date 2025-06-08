package controller;

import application.Main;
import data.DBConnectionFactory;
import data.ReservaDAO;
import data.ReservaEquipoDAO;
import data.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ReservaEquipo;

import java.sql.Connection;
import java.util.ArrayList;

public class ReservaEController {

	@FXML private TableView<ReservaEquipo> tableReservas;
	@FXML private TableColumn<ReservaEquipo, String> colTipo;
	@FXML private TableColumn<ReservaEquipo, String> colEstado;
	@FXML private TableColumn<ReservaEquipo, String> colTipoEquipo;
	@FXML private TableColumn<ReservaEquipo, String> colMarca;
	@FXML private TableColumn<ReservaEquipo, String> colSoftware;
	
	@FXML private Button btnequipo;
	@FXML private Button btnCerrarSesion;

    private Connection connection = DBConnectionFactory.getConnectionByRole(UserSession.getInstance().getRole()).getConnection();
    private ReservaEquipoDAO reservaEquipoDAO = new ReservaEquipoDAO(connection);
    private ReservaDAO reservaDAO = new ReservaDAO(connection);

    @FXML
    public void initialize() {
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTipoEquipo.setCellValueFactory(new PropertyValueFactory<>("tipoEquipo"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colSoftware.setCellValueFactory(new PropertyValueFactory<>("software"));

        cargarReservas();
        
        btnequipo.setOnAction(e -> Main.loadView("/view/Equipo.fxml"));
        btnCerrarSesion.setOnAction(e -> {
            UserSession.getInstance().destroy();
            Main.loadView("/view/Login.fxml");
        });
    }

    private void cargarReservas() {
        long cedula = Long.parseLong(UserSession.getInstance().getUsername());
        ArrayList<ReservaEquipo> reservas = reservaEquipoDAO.obtenerReservasConEquiposPorUsuario(cedula);
        ObservableList<ReservaEquipo> lista = FXCollections.observableArrayList(reservas);
        tableReservas.setItems(lista);
    }
    
    public void eliminarReserva() {
    	ReservaEquipo seleccionado = tableReservas.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Main.showAlert("Debe seleccionar un equipo para actualizar.", "Sin selección", Alert.AlertType.WARNING);
            return;
        }
        
        reservaEquipoDAO.delete(seleccionado.getIdReserva(), seleccionado.getIdEquipo());
        reservaDAO.delete(seleccionado.getIdReserva());
    	cargarReservas();
    }
}


