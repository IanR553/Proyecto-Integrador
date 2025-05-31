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
        String query = "INSERT INTO PI1SIDS.Equipo (tipo, estado, marca, software) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, equipo.getTipo());
            pstmt.setBoolean(2, equipo.isEstado());
            pstmt.setString(3, equipo.getMarca());
            pstmt.setString(4, equipo.getSoftware());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Equipo> fetch() {
        ArrayList<Equipo> equipos = new ArrayList<>();
        String query = "SELECT * FROM PI1SIDS.Equipo";

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
        String sql = "UPDATE PI1SIDS.Equipo SET tipo=?, estado=?, marca=?, software=? WHERE id=?";

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
        String sql = "DELETE FROM PI1SIDS.Equipo WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(String id) {
        String sql = "SELECT id FROM PI1SIDS.Equipo WHERE id=?";

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
    

    public ArrayList<Equipo> fetchDisponibles(String tipo, String software) {
        ArrayList<Equipo> equipos = new ArrayList<>();
        String query = "SELECT * FROM PI1SIDS.Equipo WHERE estado = 1 AND tipo LIKE ? AND software LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + tipo + "%");
            stmt.setString(2, "%" + software + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String tipoResult = rs.getString("tipo");
                boolean estado = rs.getBoolean("estado");
                String marca = rs.getString("marca");
                String softwareResult = rs.getString("software");

                Equipo equipo = new Equipo(id, tipoResult, estado, marca, softwareResult);
                equipos.add(equipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return equipos;
    }

    public void updateNoDisponible(String id) {
        String sql = "UPDATE PI1SIDS.Equipo SET estado = 0 WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



