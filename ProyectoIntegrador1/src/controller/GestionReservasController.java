package controller;

import application.Main;
import data.DBConnectionFactory;
import data.ReservaDAO;
import data.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Reserva;

import java.sql.Connection;

public class GestionReservasController {

    @FXML
    private TableView<Reserva> tableReservas;
    @FXML
    private TableColumn<Reserva, String> colId;
    @FXML
    private TableColumn<Reserva, String> colEstado;
    @FXML
    private TableColumn<Reserva, String> colTipo;
    @FXML
    private TableColumn<Reserva, Long> colCedUsuario;
    @FXML
    private TableColumn<Reserva, String> colIdHorario;

    private Connection connection = DBConnectionFactory
            .getConnectionByRole(UserSession.getInstance().getRole())
            .getConnection();
    private ReservaDAO reservaDAO = new ReservaDAO(connection);

    @FXML
    public void initialize() {
    	
		ObservableList<Reserva> allReservas = FXCollections.observableArrayList();
		for (Reserva reserva : reservaDAO.fetch()) {
			allReservas.add(reserva);
		}
		
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));        
		colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
		colEstado.setCellFactory(TextFieldTableCell.forTableColumn());
		colEstado.setOnEditCommit(event -> {
			Reserva reserva = event.getRowValue();
			reserva.setEstado(event.getNewValue());
			reservaDAO.update(reserva);
		});
		colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		colCedUsuario.setCellValueFactory(new PropertyValueFactory<>("cedUsuario"));
		colIdHorario.setCellValueFactory(new PropertyValueFactory<>("idHorario"));

		tableReservas.setItems(allReservas);
		tableReservas.setEditable(true);
    }

    @FXML
    private void actionEliminarReserva() {
        Reserva seleccionada = tableReservas.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            Main.showAlert("Debe seleccionar una reserva en la tabla para eliminar.", "Sin selección", Alert.AlertType.WARNING);
            return;
        }

        reservaDAO.delete_por_admin(seleccionada.getId());
        initialize();
    }

    @FXML
    private void actionReservarEquipo() {
        Main.loadView("/view/Equipo.fxml");
    }

    @FXML
    private void actionReservarSala() {
        Main.loadView("/view/Sala.fxml");
    }

    @FXML
    private void actionVolverMenu() {
        Main.loadView("/view/Admin.fxml");
    }
}
