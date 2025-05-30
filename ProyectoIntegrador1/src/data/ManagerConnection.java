package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ManagerConnection implements DBConnection{
	
	private static ManagerConnection instance; //Singleton
    private Connection connection;
	private final String username="admin_PI25";
	private final String password="admin_PI25";
	private final String host = "localhost";
	private final String port = "1521";
	private final String service = "XE";

    private ManagerConnection() {
        try {
            connection = DriverManager.getConnection(getConnectionString(), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error connecting to the database.");
        }
    }

    public static ManagerConnection getInstance() {
        if (instance == null) instance = new ManagerConnection();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
    
	public String getConnectionString() {
		return String.format("jdbc:oracle:thin:@%s:%s:%s", this.host, this.port, this.service);
	}
}
