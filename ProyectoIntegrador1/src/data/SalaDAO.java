package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import model.Sala;

public class SalaDAO implements CRUD_operaciones<Sala, Integer> {
    private Connection connection;

    public SalaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Sala sala) {
        String sql = "INSERT INTO Sala (id, nombre, capacidad, estado, ubicacion, software) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, sala.getId());
            pstmt.setString(2, sala.getNombre());
            pstmt.setInt(3, sala.getCapacidad());
            pstmt.setBoolean(4, sala.isEstado());
            pstmt.setString(5, sala.getUbicacion());
            pstmt.setString(6, sala.getSoftware());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
            	showAlert("Sala insertado exitosamente.", "Registro exitoso", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Sala> fetch() {
        ArrayList<Sala> salas = new ArrayList<>();
        String sql = "SELECT * FROM Sala";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                int capacidad = rs.getInt("capacidad");
                boolean estado = rs.getBoolean("estado");
                String ubicacion = rs.getString("ubicacion");
                String software = rs.getString("software");

                Sala sala = new Sala(id, nombre, capacidad, estado, ubicacion, software);
                salas.add(sala);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salas;
    }

    @Override
    public void update(Sala sala) {
        String sql = "UPDATE Sala SET nombre=?, capacidad=?, estado=?, ubicacion=?, software=? WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, sala.getNombre());
            pstmt.setInt(2, sala.getCapacidad());
            pstmt.setBoolean(3, sala.isEstado());
            pstmt.setString(4, sala.getUbicacion());
            pstmt.setString(5, sala.getSoftware());
            pstmt.setInt(6, sala.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Sala WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(Integer id) {
        String sql = "SELECT id FROM Sala WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
			return rs.getInt("id")==id;
			}
		} catch (SQLException e) {
		e.printStackTrace();}
		
		return false;
	}
    
    public void showAlert(String mensaje,String header, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle("Alerta");
        alert.setHeaderText(header);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}


