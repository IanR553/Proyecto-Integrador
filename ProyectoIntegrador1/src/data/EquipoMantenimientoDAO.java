package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.EquipoMantenimiento;

public class EquipoMantenimientoDAO {

    private Connection connection;

    public EquipoMantenimientoDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(EquipoMantenimiento em) {
        String query = "INSERT INTO equipo_mantenimiento (id_mantenimiento, id_equipo) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, em.getIdMantenimiento());
            pstmt.setInt(2, em.getIdEquipo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<EquipoMantenimiento> fetch() {
        ArrayList<EquipoMantenimiento> lista = new ArrayList<>();
        String query = "SELECT id_mantenimiento, id_equipo FROM equipo_mantenimiento";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int idMantenimiento = rs.getInt("id_mantenimiento");
                int idEquipo = rs.getInt("id_equipo");

                lista.add(new EquipoMantenimiento(idMantenimiento, idEquipo));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void update(EquipoMantenimiento em) {
        String query = "UPDATE equipo_mantenimiento SET id_equipo = ? WHERE id_mantenimiento = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, em.getIdEquipo());
            pstmt.setInt(2, em.getIdMantenimiento());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int idMantenimiento, int idEquipo) {
        String query = "DELETE FROM equipo_mantenimiento WHERE id_mantenimiento = ? AND id_equipo = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idMantenimiento);
            pstmt.setInt(2, idEquipo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(int idMantenimiento, int idEquipo) {
        String query = "SELECT id_mantenimiento, id_equipo FROM equipo_mantenimiento WHERE id_mantenimiento = ? AND id_equipo = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idMantenimiento);
            pstmt.setInt(2, idEquipo);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}


