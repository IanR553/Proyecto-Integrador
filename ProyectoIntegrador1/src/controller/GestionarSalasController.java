package controller;

import data.DBConnection;
import data.SalaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Sala;
import application.Main;

import java.sql.Connection;

public class GestionarSalasController {

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCapacidad;
    @FXML private CheckBox chkEstado;
    @FXML private TextField txtUbicacion;
    @FXML private TextField txtSoftware;

    @FXML private TableView<Sala> tableSalas;
    @FXML private TableColumn<Sala, String> colId;
    @FXML private TableColumn<Sala, String> colNombre;
    @FXML private TableColumn<Sala, String> colEstado;
    @FXML private TableColumn<Sala, String> colCapacidad;
    @FXML private TableColumn<Sala, String> colUbicacion;
    @FXML private TableColumn<Sala, String> colSoftware;

    private final Connection connection = DBConnection.getInstance().getConnection();
    private final SalaDAO salaDAO = new SalaDAO(connection);
    private ObservableList<Sala> listaSalas;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().isEstado() ? "Disponible" : "No disponible"));
        colCapacidad.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getCapacidad())));
        colUbicacion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUbicacion()));
        colSoftware.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSoftware()));

        cargarSalas();

        tableSalas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) mostrarDatosSala(newSelection);
        });
    }

    private void cargarSalas() {
        listaSalas = FXCollections.observableArrayList(salaDAO.fetch());
        tableSalas.setItems(listaSalas);
    }

    private void mostrarDatosSala(Sala sala) {
        txtId.setText(sala.getId());
        txtNombre.setText(sala.getNombre());
        chkEstado.setSelected(sala.isEstado());
        txtCapacidad.setText(String.valueOf(sala.getCapacidad()));
        txtUbicacion.setText(sala.getUbicacion());
        txtSoftware.setText(sala.getSoftware());
    }

    @FXML
    private void actionCrearSala() {
        if (!validarCampos()) return;

        Sala sala = new Sala(
                txtId.getText().trim(),
                txtNombre.getText().trim(),
                Integer.parseInt(txtCapacidad.getText().trim()),
                chkEstado.isSelected(),
                txtUbicacion.getText().trim(),
                txtSoftware.getText().trim()
        );

        salaDAO.save(sala);
        cargarSalas();
        limpiarCampos();
    }

    @FXML
    private void actionActualizarSala() {
        if (!validarCampos()) return;

        Sala sala = new Sala(
                txtId.getText().trim(),
                txtNombre.getText().trim(),
                Integer.parseInt(txtCapacidad.getText().trim()),
                chkEstado.isSelected(),
                txtUbicacion.getText().trim(),
                txtSoftware.getText().trim()
        );

        salaDAO.update(sala);
        cargarSalas();
        limpiarCampos();
    }

    @FXML
    private void actionEliminarSala() {
        Sala salaSeleccionada = tableSalas.getSelectionModel().getSelectedItem();

        if (salaSeleccionada == null) {
            Main.showAlert("Debe seleccionar una sala en la tabla para eliminar.", "Sin selección", Alert.AlertType.WARNING);
            return;
        }

        salaDAO.delete(salaSeleccionada.getId());
        cargarSalas();
        limpiarCampos();
    }

    @FXML
    private void actionVolverMenu() {
        Main.loadView("/view/Admin.fxml");
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtCapacidad.clear();
        chkEstado.setSelected(false);
        txtUbicacion.clear();
        txtSoftware.clear();
        tableSalas.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {
        if (txtId.getText().trim().isEmpty() ||
            txtNombre.getText().trim().isEmpty() ||
            txtCapacidad.getText().trim().isEmpty() ||
            txtUbicacion.getText().trim().isEmpty() ||
            txtSoftware.getText().trim().isEmpty()) {

            Main.showAlert("Por favor, complete todos los campos sin espacios en blanco.",
                           "Campos obligatorios",
                           Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}

