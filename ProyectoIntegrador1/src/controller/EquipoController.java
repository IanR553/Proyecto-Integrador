package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class EquipoController {

    @FXML
    private Button btnBuscarSalas;

    @FXML
    private Button btnReservar;

    @FXML
    private ComboBox<?> cbxEquipos;

    @FXML
    private ComboBox<?> cbxSoftware;

    @FXML
    private TableColumn<?, ?> colCapacidadSala;

    @FXML
    private TableColumn<?, ?> colIdSala;

    @FXML
    private TableColumn<?, ?> colNombreSala;

    @FXML
    private TableColumn<?, ?> colUbicacionSala;

    @FXML
    private TableView<?> tableSalasDisponibles;

    @FXML
    private TextField txtHoraFin;

    @FXML
    private TextField txtHoraInicio;

}
