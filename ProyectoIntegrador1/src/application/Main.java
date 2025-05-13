package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	private static BorderPane rootLayout;
	@Override
	public void start(Stage primaryStage) {
	try {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		rootLayout = loader.load();
		
		Scene scene = new Scene(rootLayout);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/udi.png")));

		
		primaryStage.setScene(scene);
		
		primaryStage.setTitle("Software Reservas UDI");
		
		primaryStage.show();
		
		} catch (Exception e) {
			
		e.printStackTrace();}}
	
	public static void loadView(String fxmlFile) {

		try {

		FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));

		rootLayout.setCenter(loader.load());

		} catch (Exception e) {

		e.printStackTrace();

		}

		}
	//Hola IAN
	
    public static void showAlert(String mensaje, String header, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle("Alerta");
        alert.setHeaderText(header);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
	public static void main(String[] args) {
		launch(args);
	}
}

