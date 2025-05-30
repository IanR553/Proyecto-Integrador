package controller;

import java.sql.Connection;

import application.Main;
import data.DBConnectionFactory;
import data.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Rol;
import model.UserSession;

public class loginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<Rol> comBoxRolU;

    @FXML
    private Button btnLogin;

    private Connection connection;
    private UsuarioDAO usuarioDAO;

    public UserSession userSession;

    @FXML
    void initialize() {
    	comBoxRolU.getItems().addAll(
                new Rol("R1", "Manager"),
                new Rol("R2", "Profesor"),
                new Rol("R3", "Administrativo"),
                new Rol("R4", "Estudiante")
            );
        // Mostrar solo el nombre del rol en el ComboBox
    	comBoxRolU.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Rol item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
    	comBoxRolU.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Rol item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
    }

    @FXML
    private void iniciarSesion() {
        if (!validarCampos()) return;

        Rol rolSeleccionado = comBoxRolU.getSelectionModel().getSelectedItem();
        String usuarioTexto = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();

        Long cedula;
        try {
            cedula = Long.parseLong(usuarioTexto);
        } catch (NumberFormatException e) {
            Main.showAlert("Error", "El número de cédula debe ser un valor numérico válido.", Alert.AlertType.ERROR);
            return;
        }

        try {
            connection = DBConnectionFactory.getConnectionByRole(rolSeleccionado.getNombre()).getConnection();
            usuarioDAO = new UsuarioDAO(connection);

            if (usuarioDAO.authenticate(cedula, password, rolSeleccionado.getId())) {
                userSession = UserSession.getInstance(usuarioTexto, rolSeleccionado.getNombre());

                switch (rolSeleccionado.getNombre()) {
                    case "Manager":
                        Main.loadView("/view/Admin.fxml");
                        break;
                    case "Profesor":
                    case "Administrativo":
                        Main.loadView("/view/reservaU.fxml");
                        break;
                    case "Estudiante":
                        Main.loadView("/view/ReservaE.fxml");
                        break;
                }
            } else {
                Main.showAlert("Usuario inválido", "Digite un usuario válido", Alert.AlertType.WARNING);
            }

        } catch (Exception e) {
            Main.showAlert("Error de conexión", "Ocurrió un error al intentar conectar con la base de datos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        if (comBoxRolU.getValue() == null ||
            txtUsuario.getText().trim().isEmpty() ||
            txtPassword.getText().trim().isEmpty()) {

            Main.showAlert(
                "Campos obligatorios",
                "Por favor, complete los campos de usuario, contraseña y rol sin espacios en blanco.",
                Alert.AlertType.WARNING
            );
            return false;
        }
        return true;
    }

    @FXML
    private void clearFields() {
        txtUsuario.clear();
        txtPassword.clear();
        comBoxRolU.setValue(null);
    }
}


