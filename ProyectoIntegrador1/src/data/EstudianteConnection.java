package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EstudianteConnection implements DBConnection{
	
	private static EstudianteConnection instance; //Singleton
    private Connection connection;
	private final String username="estudiante_PI25";
	private final String password="estudiante_PI25";
	private final String host = "192.168.254.215";
	private final String port = "1521";
	private final String service = "orcl";

    private EstudianteConnection() {
        try {
            connection = DriverManager.getConnection(getConnectionString(), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database.");
        }
    }

    public static EstudianteConnection getInstance() {
        if (instance == null) instance = new EstudianteConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
    
	public String getConnectionString() {
		return String.format("jdbc:oracle:thin:@%s:%s:%s", this.host, this.port, this.service);
	}
}
