package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class MantenimientoController {

    @FXML
    private Button btnGuardar;

    @FXML
    private ComboBox<?> cbxTipo;

    @FXML
    private DatePicker dpFechaFinal;

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtMotivo;

}
