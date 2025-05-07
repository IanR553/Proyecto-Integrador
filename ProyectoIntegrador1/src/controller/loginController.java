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
import model.Usuario;

public class loginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    private Connection connection = DBConnection.getInstance().getConnection();
    private UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

    @FXML
    private void iniciarSesion() {
        String cedulaStr = txtUsuario.getText();
        String password = txtPassword.getText();

        if (cedulaStr.isEmpty() || password.isEmpty()) {
        	Main.showAlert("Debe llenar todos los campos.", null, Alert.AlertType.WARNING);
            return;
        }

        try {
            Long cedula = Long.parseLong(cedulaStr);

            Usuario usuario = usuarioDAO.obtenerUsuarioPorCredenciales(cedula, password);

            if (usuario != null) {
                String idRol = usuario.getidRol();
                switch (idRol) {
                    case "R1":
                        Main.loadView("/view/Admin.fxml");
                        break;
                    case "R2":
                    case "R3":
                        Main.loadView("/view/reservaU.fxml");
                        break;
                    case "R4":
                        Main.loadView("/view/ReservaE.fxml");
                        break;
                    default:
                    	Main.showAlert("Rol no reconocido. Contacte al administrador.", null, Alert.AlertType.ERROR);
                        return;
                }
                Main.showAlert("Inicio de sesión exitoso.", null, Alert.AlertType.INFORMATION);
                clearFields();
            } else {
            	Main.showAlert("Credenciales incorrectas.", null, Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
        	Main.showAlert("La cédula debe ser un número válido.", null, Alert.AlertType.WARNING);
        } catch (Exception e) {
            Main.showAlert("Error al intentar iniciar sesión: " + e.getMessage(), null, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clearFields() {
        txtUsuario.clear();
        txtPassword.clear();
    }
}

