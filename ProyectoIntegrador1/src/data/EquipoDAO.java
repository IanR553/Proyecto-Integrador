package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Equipo;

public class EquipoDAO implements CRUD_operaciones<Equipo, String> {
    private Connection connection;

    public EquipoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Equipo equipo) {
        String query = "INSERT INTO Equipo (id, tipo, estado, marca, software) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, equipo.getId());
            pstmt.setString(2, equipo.getTipo());
            pstmt.setBoolean(3, equipo.isEstado());
            pstmt.setString(4, equipo.getMarca());
            pstmt.setString(5, equipo.getSoftware());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Equipo> fetch() {
        ArrayList<Equipo> equipos = new ArrayList<>();
        String query = "SELECT * FROM Equipo";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String tipo = rs.getString("tipo");
                boolean estado = rs.getBoolean("estado");
                String marca = rs.getString("marca");
                String software = rs.getString("software");

                Equipo equipo = new Equipo(id, tipo, estado, marca, software);
                equipos.add(equipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return equipos;
    }

    @Override
    public void update(Equipo equipo) {
        String sql = "UPDATE Equipo SET tipo=?, estado=?, marca=?, software=? WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, equipo.getTipo());
            pstmt.setBoolean(2, equipo.isEstado());
            pstmt.setString(3, equipo.getMarca());
            pstmt.setString(4, equipo.getSoftware());
            pstmt.setString(5, equipo.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM Equipo WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(String id) {
        String sql = "SELECT id FROM Equipo WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("id").equals(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}



