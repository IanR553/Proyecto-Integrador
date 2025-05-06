package controller;

import data.DBConnection;
import data.RolDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Rol;

import java.sql.Connection;

import application.Main;

public class GestionRolesController {

    @FXML
    private TableView<Rol> tableRoles;

    @FXML
    private TableColumn<Rol, String> colIdRol;

    @FXML
    private TableColumn<Rol, String> colNombreRol;

    @FXML
    private TextField txtIdRol;

    @FXML
    private TextField txtNombreRol;

    @FXML
    private Button btnCrearRol;

    @FXML
    private Button btnActualizarRol;

    @FXML
    private Button btnEliminarRol;

    @FXML
    private Button btnvolverMenu;

    private final Connection connection = DBConnection.getInstance().getConnection();
    private final RolDAO rolDAO = new RolDAO(connection);
    private ObservableList<Rol> listaRoles;

    @FXML
    private void initialize() {
        colIdRol.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombreRol.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        cargarRoles();

        tableRoles.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtIdRol.setText(newSelection.getId());
                txtNombreRol.setText(newSelection.getNombre());
                txtIdRol.setDisable(true); 
            }
        });
    }

    private void cargarRoles() {
        listaRoles = FXCollections.observableArrayList(rolDAO.fetch());
        tableRoles.setItems(listaRoles);
    }

    @FXML
    private void actionCrearRol() {
        String id = txtIdRol.getText().trim();
        String nombre = txtNombreRol.getText().trim();

        if (id.isEmpty() || nombre.isEmpty()) {
            Main.showAlert("Debe completar ambos campos para crear un rol.", "Gestión de Roles", Alert.AlertType.WARNING);
            return;
        }

        if (rolDAO.authenticate(id)) {
            Main.showAlert("Ya existe un rol con ese ID.", "Gestión de Roles", Alert.AlertType.ERROR);
            return;
        }

        Rol nuevoRol = new Rol(id, nombre);
        rolDAO.save(nuevoRol);
        cargarRoles();
        limpiarCampos();
        Main.showAlert("Rol creado exitosamente.", "Gestión de Roles", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void actionActualizarRol() {
        String id = txtIdRol.getText().trim();
        String nombre = txtNombreRol.getText().trim();

        if (id.isEmpty() || nombre.isEmpty()) {
            Main.showAlert("Debe seleccionar un rol y modificar el nombre para actualizar.", "Gestión de Roles", Alert.AlertType.WARNING);
            return;
        }

        if ("R1".equals(id)) {
            Main.showAlert("El rol 'R1' no se puede modificar porque es un rol protegido.", "Gestión de Roles", Alert.AlertType.ERROR);
            return;
        }

        Rol rolActualizado = new Rol(id, nombre);
        rolDAO.update(rolActualizado);
        cargarRoles();
        limpiarCampos();
        Main.showAlert("Rol actualizado exitosamente.", "Gestión de Roles", Alert.AlertType.INFORMATION);
    }


    @FXML
    private void actionEliminarRol() {
        Rol rolSeleccionado = tableRoles.getSelectionModel().getSelectedItem();

        if (rolSeleccionado == null) {
            Main.showAlert("Debe seleccionar un rol para eliminar.", "Gestión de Roles", Alert.AlertType.WARNING);
            return;
        }

        if ("R1".equals(rolSeleccionado.getId())) {
            Main.showAlert("El rol 'R1' no se puede eliminar porque es un rol protegido.", "Gestión de Roles", Alert.AlertType.ERROR);
            return;
        }

        rolDAO.delete(rolSeleccionado.getId());
        cargarRoles();
        limpiarCampos();
        Main.showAlert("Rol eliminado exitosamente.", "Gestión de Roles", Alert.AlertType.INFORMATION);
    }


    @FXML
    private void actionVolverMenu() {
        Main.loadView("/view/Admin.fxml");
    }

    private void limpiarCampos() {
        txtIdRol.clear();
        txtNombreRol.clear();
        txtIdRol.setDisable(false);
        tableRoles.getSelectionModel().clearSelection();
    }
}


