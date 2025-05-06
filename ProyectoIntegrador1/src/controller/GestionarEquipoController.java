package controller;

import data.DBConnection;
import data.EquipoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Equipo;
import application.Main;

import java.sql.Connection;

public class GestionarEquipoController {

    @FXML private TextField txtId;
    @FXML private TextField txtTipo;
    @FXML private CheckBox chkEstado;
    @FXML private TextField txtMarca;
    @FXML private TextField txtSoftware;

    @FXML private TableView<Equipo> tableEquipos;
    @FXML private TableColumn<Equipo, String> colId;
    @FXML private TableColumn<Equipo, String> colTipo;
    @FXML private TableColumn<Equipo, String> colEstado;
    @FXML private TableColumn<Equipo, String> colMarca;
    @FXML private TableColumn<Equipo, String> colSoftware;

    private final Connection connection = DBConnection.getInstance().getConnection();
    private final EquipoDAO equipoDAO = new EquipoDAO(connection);
    private ObservableList<Equipo> listaEquipos;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        colEstado.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().isEstado() ? "Disponible" : "No disponible"));
        colMarca.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMarca()));
        colSoftware.setCellValueFactory(data -> {
            String software = data.getValue().getSoftware();
            return new javafx.beans.property.SimpleStringProperty(software != null ? software : "Ninguno");
        });

        cargarEquipos();

        tableEquipos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) mostrarDatosEquipo(newSelection);
        });
    }

    private void cargarEquipos() {
        listaEquipos = FXCollections.observableArrayList(equipoDAO.fetch());
        tableEquipos.setItems(listaEquipos);
    }

    private void mostrarDatosEquipo(Equipo equipo) {
        txtId.setText(equipo.getId());
        txtTipo.setText(equipo.getTipo());
        chkEstado.setSelected(equipo.isEstado());
        txtMarca.setText(equipo.getMarca());
        txtSoftware.setText(equipo.getSoftware() != null ? equipo.getSoftware() : "");
    }

    @FXML
    private void actionCrearEquipo() {
        if (!validarCampos()) return;

        Equipo equipo = new Equipo(
                txtId.getText().trim(),
                txtTipo.getText().trim(),
                chkEstado.isSelected(),
                txtMarca.getText().trim(),
                txtSoftware.getText().trim().isEmpty() ? null : txtSoftware.getText().trim()
        );

        equipoDAO.save(equipo);
        cargarEquipos();
        limpiarCampos();
    }

    @FXML
    private void actionActualizarEquipo() {
        if (!validarCampos()) return;

        Equipo equipo = new Equipo(
                txtId.getText().trim(),
                txtTipo.getText().trim(),
                chkEstado.isSelected(),
                txtMarca.getText().trim(),
                txtSoftware.getText().trim().isEmpty() ? null : txtSoftware.getText().trim()
        );

        equipoDAO.update(equipo);
        cargarEquipos();
        limpiarCampos();
    }

    @FXML
    private void actionEliminarEquipo() {
        Equipo equipoSeleccionado = tableEquipos.getSelectionModel().getSelectedItem();

        if (equipoSeleccionado == null) {
            Main.showAlert("Debe seleccionar un equipo en la tabla para eliminar.", "Sin selección", Alert.AlertType.WARNING);
            return;
        }

        equipoDAO.delete(equipoSeleccionado.getId());
        cargarEquipos();
        limpiarCampos();
    }


    @FXML
    private void actionVolverMenu() {
        Main.loadView("/view/Admin.fxml");
    }

    private void limpiarCampos() {
        txtId.clear();
        txtTipo.clear();
        chkEstado.setSelected(false);
        txtMarca.clear();
        txtSoftware.clear();
        tableEquipos.getSelectionModel().clearSelection();
    }

    private boolean validarCampos() {
        if (txtId.getText().trim().isEmpty() ||
            txtTipo.getText().trim().isEmpty() ||
            txtMarca.getText().trim().isEmpty()) {

            Main.showAlert("Por favor, complete los campos ID, Tipo y Marca sin espacios en blanco.",
                           "Campos obligatorios",
                           Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }
}
