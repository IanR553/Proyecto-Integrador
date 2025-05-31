package controller;

import application.Main;
import data.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class AdminController {

    @FXML
    private Button btnGestionSala;

    @FXML
    private Button btnGestionEquipo;

    @FXML
    private Button btnGestionarRes;

    @FXML
    private Button btnGestionUsuario;

    @FXML
    private Button btnGestionRoles;

    @FXML
    private Button btnGestionarMant;

    @FXML
    private Button btnCerrarSesion;


    @FXML
    private void actionGestionarSalas() {
        Main.loadView("/view/GestionarSalas.fxml");
    }

    @FXML
    private void actionGestionEquipo() {
        Main.loadView("/view/GestionarEquipo.fxml");
    }

    @FXML
    private void actionGestionarReservas() {
    	Main.loadView("/view/GestionarR.fxml");
    }

    @FXML
    private void actionGestionUsuarios() {
        Main.loadView("/view/GestionUsuarios.fxml");
    }

    @FXML
    private void actionGestionarMant() {
    	Main.loadView("/view/GestionarMantenimiento.fxml");
    }

    @FXML
    private void cerrarSesion() {
        Main.showAlert("Sesión cerrada correctamente.", "Cerrar sesión", Alert.AlertType.INFORMATION);
        UserSession.getInstance().destroy();
        Main.loadView("/view/Login.fxml"); 
        
    }

}
