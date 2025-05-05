package controller;

import java.sql.Connection;

import application.Main;
import data.DBConnection;
import data.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    private final Connection connection = DBConnection.getInstance().getConnection();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO(connection);


    @FXML
    private void iniciarSesion() {
        String cedulaStr = txtUsuario.getText();
        String password = txtPassword.getText();

        if (cedulaStr.isEmpty() || password.isEmpty()) {
            showAlert("Debe llenar todos los campos.", null, Alert.AlertType.WARNING);
            return;
        }

        try {
            Long cedula = Long.parseLong(cedulaStr);

            boolean autenticado = usuarioDAO.authenticateLogin(cedula, password);

            if (autenticado) {
                showAlert("Inicio de sesión exitoso.", null, Alert.AlertType.INFORMATION);
                clearFields();
                Main.loadView("/view/Admin.fxml");
            } else {
                showAlert("Credenciales incorrectas.", null, Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            showAlert("La cédula debe ser un número válido.", null, Alert.AlertType.WARNING);
        } catch (Exception e) {
            showAlert("Error al intentar iniciar sesión: " + e.getMessage(), null, Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String mensaje, String header, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle("Alerta");
        alert.setHeaderText(header);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void clearFields() {
        txtUsuario.clear();
        txtPassword.clear();
    }
}
