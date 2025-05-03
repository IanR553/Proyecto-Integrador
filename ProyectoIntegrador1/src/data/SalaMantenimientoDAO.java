package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.SalaMantenimiento;

public class SalaMantenimientoDAO {

    private Connection connection;

    public SalaMantenimientoDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(SalaMantenimiento sm) {
        String query = "INSERT INTO sala_mantenimiento (id_sala, id_mantenimiento) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, sm.getIdSala());
            pstmt.setInt(2, sm.getIdMantenimiento());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<SalaMantenimiento> fetch() {
        ArrayList<SalaMantenimiento> lista = new ArrayList<>();
        String query = "SELECT id_sala, id_mantenimiento FROM sala_mantenimiento";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int idSala = rs.getInt("id_sala");
                int idMantenimiento = rs.getInt("id_mantenimiento");
                lista.add(new SalaMantenimiento(idSala, idMantenimiento));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void update(SalaMantenimiento sm) {
        String query = "UPDATE sala_mantenimiento SET id_mantenimiento = ? WHERE id_sala = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, sm.getIdMantenimiento());
            pstmt.setInt(2, sm.getIdSala());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int idSala, int idMantenimiento) {
        String query = "DELETE FROM sala_mantenimiento WHERE id_sala = ? AND id_mantenimiento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idSala);
            pstmt.setInt(2, idMantenimiento);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(int idSala, int idMantenimiento) {
        String sql = "SELECT id_sala,id_mantenimiento FROM sala_mantenimiento WHERE id_sala = ? AND id_mantenimiento = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idSala);
            stmt.setInt(2, idMantenimiento);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}


