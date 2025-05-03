package data;

import javafx.scene.control.Alert;
import model.Rol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RolDAO implements CRUD_operaciones<Rol, Integer> {
    private Connection connection;

    public RolDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Rol rol) {
        String sql = "INSERT INTO Rol (id, nombre) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, rol.getId());
            pstmt.setString(2, rol.getNombre());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Rol> fetch() {
        ArrayList<Rol> roles = new ArrayList<>();
        String sql = "SELECT * FROM Rol";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");

                Rol rol = new Rol(id, nombre);
                roles.add(rol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roles;
    }

    @Override
    public void update(Rol rol) {
        String sql = "UPDATE Rol SET nombre = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, rol.getNombre());
            pstmt.setInt(2, rol.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Rol WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(Integer id) {
        String sql = "SELECT id FROM Rol WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id") == id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void showAlert(String mensaje, String header, Alert.AlertType tipoAlerta) {
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle("Mensaje del sistema");
        alert.setHeaderText(header);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}


