package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class loginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private void initialize() {
        btnLogin.setOnAction(event -> iniciarSesion());
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();
        
        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Error", "Debe llenar todos los campos.", Alert.AlertType.ERROR);
            return;
        }
        
        if (autenticarUsuario(usuario, password)) {
            mostrarAlerta("Éxito", "Inicio de sesión exitoso.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "Credenciales incorrectas.", Alert.AlertType.ERROR);
        }
    }

    private boolean autenticarUsuario(String usuario, String password) {
        // Simulación de autenticación, reemplazar con lógica real
        return "admin".equals(usuario) && "1234".equals(password);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
