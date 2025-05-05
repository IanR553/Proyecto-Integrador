package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import model.SalaMantenimiento;

public class SalaMantenimientoDAO implements CRUD_operaciones_Relacion <SalaMantenimiento,String,String>{

    private Connection connection;

    public SalaMantenimientoDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(SalaMantenimiento sm) {
        String query = "INSERT INTO sala_mantenimiento (id_sala, id_mantenimiento) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, sm.getIdSala());
            pstmt.setString(2, sm.getIdMantenimiento());
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
                String idSala = rs.getString("id_sala");
                String idMantenimiento = rs.getString("id_mantenimiento");
                lista.add(new SalaMantenimiento(idMantenimiento, idSala));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void update(SalaMantenimiento sm) {
        String query = "UPDATE sala_mantenimiento SET id_mantenimiento = ? WHERE id_sala = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, sm.getIdMantenimiento());
            pstmt.setString(2, sm.getIdSala());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String idSala, String idMantenimiento) {
        String query = "DELETE FROM sala_mantenimiento WHERE id_sala = ? AND id_mantenimiento = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, idSala);
            pstmt.setString(2, idMantenimiento);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String idSala, String idMantenimiento) {
        String sql = "SELECT id_sala, id_mantenimiento FROM sala_mantenimiento WHERE id_sala = ? AND id_mantenimiento = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idSala);
            stmt.setString(2, idMantenimiento);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}



