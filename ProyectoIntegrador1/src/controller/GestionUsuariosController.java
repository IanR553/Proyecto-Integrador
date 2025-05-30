package controller;

import application.Main;
import data.DBConnectionFactory;
import data.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Rol;
import model.UserSession;
import model.Usuario;

import java.sql.Connection;

public class GestionUsuariosController {

    @FXML private TableView<Usuario> tableUsuarios;
    @FXML private TableColumn<Usuario, Long> colCedula;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellido;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colRol;

    @FXML private TextField txtCedula;
    @FXML private TextField txtPrimerNombre;
    @FXML private TextField txtSegundoNombre;
    @FXML private TextField txtPrimerApellido;
    @FXML private TextField txtSegundoApellido;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtCelular;
    @FXML private ComboBox<Rol> comBoxRol;
    @FXML private PasswordField txtContraseña;

    @FXML private Button btnCrear;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnVolverMenu;

    private Connection connection = DBConnectionFactory.getConnectionByRole(UserSession.getInstance().getRole()).getConnection();
    private UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
    private ObservableList<Usuario> listaUsuarios;

    @FXML
    private void initialize() {
    	
        comBoxRol.getItems().addAll(
                new Rol("R1", "Manager"),
                new Rol("R2", "Profesor"),
                new Rol("R3", "Administrativo"),
                new Rol("R4", "Estudiante")
            );

            // Mostrar solo el nombre del rol en el ComboBox
            comBoxRol.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Rol item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNombre());
                }
            });
            comBoxRol.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Rol item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNombre());
                }
            });


        colCedula.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getCedula()).asObject());
        colNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrimerNombre()));
        colApellido.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrimerApellido()));
        colCorreo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCorreoElectronico()));
        colRol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getidRol()));

        cargarUsuarios();

        tableUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtCedula.setText(String.valueOf(newSelection.getCedula()));
                txtPrimerNombre.setText(newSelection.getPrimerNombre());
                txtSegundoNombre.setText(newSelection.getSegundoNombre());
                txtPrimerApellido.setText(newSelection.getPrimerApellido());
                txtSegundoApellido.setText(newSelection.getSegundoApellido());
                txtCorreo.setText(newSelection.getCorreoElectronico());
                txtCelular.setText(String.valueOf(newSelection.getCelular()));
                txtContraseña.setText(newSelection.getContraseña());
                txtCedula.setDisable(true);
                
                for (Rol rol : comBoxRol.getItems()) {
                    if (rol.getId().equals(newSelection.getidRol())) {
                        comBoxRol.setValue(rol);
                        break;
                    }
                }

            }
        });
    }

    private void cargarUsuarios() {
        listaUsuarios = FXCollections.observableArrayList(usuarioDAO.fetch());
        tableUsuarios.setItems(listaUsuarios);
    }

    @FXML
    private void actionCrearUsuario() {
        String cedulaStr = txtCedula.getText().trim();
        String primerNombre = txtPrimerNombre.getText().trim();
        String segundoNombre = txtSegundoNombre.getText().trim().isEmpty() ? null : txtSegundoNombre.getText().trim();
        String primerApellido = txtPrimerApellido.getText().trim();
        String segundoApellido = txtSegundoApellido.getText().trim().isEmpty() ? null : txtSegundoApellido.getText().trim();
        String correo = txtCorreo.getText().trim();
        String celularStr = txtCelular.getText().trim();
        String contraseña = txtContraseña.getText().trim();
        Rol rolSeleccionado = comBoxRol.getValue();
        
        // Validación de campos vacíos
        if (cedulaStr.isEmpty() || primerNombre.isEmpty() ||
            primerApellido.isEmpty() ||
            correo.isEmpty() || celularStr.isEmpty() || rolSeleccionado == null) {
            Main.showAlert("Todos los campos deben estar llenos.", "Validación", Alert.AlertType.WARNING);
            return;
        }

        // Validar que la cédula sea numérica
        if (!cedulaStr.matches("\\d+")) {
            Main.showAlert("La cédula debe contener solo números.", "Validación", Alert.AlertType.ERROR);
            return;
        }

        Long cedula = Long.parseLong(cedulaStr);

        // Validar correo institucional
        if (!correo.endsWith("@udi.edu.co")) {
            Main.showAlert("El correo electrónico debe terminar en @udi.edu.co.", "Validación", Alert.AlertType.ERROR);
            return;
        }

        // Validar que el celular tenga exactamente 10 dígitos
        if (!celularStr.matches("\\d{10}")) {
            Main.showAlert("El número de celular debe tener exactamente 10 dígitos.", "Validación", Alert.AlertType.ERROR);
            return;
        }

        Long celular = Long.parseLong(celularStr);

        // Verificar que no exista la cédula
        if (usuarioDAO.authenticate(cedula)) {
            Main.showAlert("Ya existe un usuario con esa cédula.", "Validación", Alert.AlertType.ERROR);
            return;
        }
        if (contraseña.isEmpty()) {
            Main.showAlert("La contraseña no puede estar vacía.", "Validación", Alert.AlertType.WARNING);
            return;
        }

        Usuario nuevo = new Usuario(cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, correo, celular, rolSeleccionado.getId(), contraseña);
        usuarioDAO.save(nuevo);
        cargarUsuarios();
        limpiarCampos();
        Main.showAlert("Usuario creado exitosamente.", "Gestión de Usuarios", Alert.AlertType.INFORMATION);
    }


    @FXML
    private void actionActualizarUsuario() {
        String cedulaStr = txtCedula.getText().trim();
        String primerNombre = txtPrimerNombre.getText() != null ? txtPrimerNombre.getText().trim() : "";
        String segundoNombre = txtSegundoNombre.getText() != null && !txtSegundoNombre.getText().trim().isEmpty() ? txtSegundoNombre.getText().trim() : null;
        String primerApellido = txtPrimerApellido.getText() != null ? txtPrimerApellido.getText().trim() : "";
        String segundoApellido = txtSegundoApellido.getText() != null && !txtSegundoApellido.getText().trim().isEmpty() ? txtSegundoApellido.getText().trim() : null;
        String correo = txtCorreo.getText() != null ? txtCorreo.getText().trim() : "";
        String celularStr = txtCelular.getText() != null ? txtCelular.getText().trim() : "";
        String contraseña = txtContraseña.getText() != null ? txtContraseña.getText().trim() : "";
        Rol rolSeleccionado = comBoxRol.getValue();
        
        // Validación de campos vacíos
        if (cedulaStr.isEmpty() || primerNombre.isEmpty() ||
            primerApellido.isEmpty() || correo.isEmpty() || celularStr.isEmpty() || rolSeleccionado == null) {
            Main.showAlert("Todos los campos deben estar llenos para actualizar el usuario.", "Validación", Alert.AlertType.WARNING);
            return;
        }

        // Validar que la cédula sea numérica
        if (!cedulaStr.matches("\\d+")) {
            Main.showAlert("La cédula debe contener solo números.", "Validación", Alert.AlertType.ERROR);
            return;
        }

        Long cedula = Long.parseLong(cedulaStr);

        // Validar correo institucional
        if (!correo.endsWith("@udi.edu.co")) {
            Main.showAlert("El correo electrónico debe terminar en @udi.edu.co.", "Validación", Alert.AlertType.ERROR);
            return;
        }

        // Validar que el celular tenga exactamente 10 dígitos
        if (!celularStr.matches("\\d{10}")) {
            Main.showAlert("El número de celular debe tener exactamente 10 dígitos.", "Validación", Alert.AlertType.ERROR);
            return;
        }

        Long celular = Long.parseLong(celularStr);

        if (contraseña.isEmpty()) {
            Main.showAlert("La contraseña no puede estar vacía.", "Validación", Alert.AlertType.WARNING);
            return;
        }

        Usuario actualizado = new Usuario(cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, correo, celular, rolSeleccionado.getId(), contraseña);
        usuarioDAO.update(actualizado);
        cargarUsuarios();
        limpiarCampos();
        Main.showAlert("Usuario actualizado exitosamente.", "Gestión de Usuarios", Alert.AlertType.INFORMATION);
    }



    @FXML
    private void actionEliminarUsuario() {
        Usuario seleccionado = tableUsuarios.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            Main.showAlert("Debe seleccionar un usuario para eliminar.", "Gestión de Usuarios", Alert.AlertType.WARNING);
            return;
        }

        if ("R1".equalsIgnoreCase(seleccionado.getidRol())) {
            Main.showAlert("No se puede eliminar un usuario con rol Administrador (R1).", "Acción no permitida", Alert.AlertType.ERROR);
            return;
        }

        usuarioDAO.delete(seleccionado.getCedula());
        cargarUsuarios();
        limpiarCampos();
        Main.showAlert("Usuario eliminado exitosamente.", "Gestión de Usuarios", Alert.AlertType.INFORMATION);
    }


    @FXML
    private void actionVolverMenu() {
        Main.loadView("/view/Admin.fxml");
    }

    private void limpiarCampos() {
        txtCedula.clear();
        txtPrimerNombre.clear();
        txtSegundoNombre.clear();
        txtPrimerApellido.clear();
        txtSegundoApellido.clear();
        txtCorreo.clear();
        txtCelular.clear();
        comBoxRol.setValue(null);
        txtContraseña.clear();
        txtCedula.setDisable(false);
        tableUsuarios.getSelectionModel().clearSelection();
    }
}
