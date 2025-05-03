package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.ReservaEquipo;

public class ReservaEquipoDAO {

    private Connection connection;

    public ReservaEquipoDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(ReservaEquipo entity) {
        String query = "INSERT INTO reserva_equipo (id_reserva, id_equipo) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, entity.getIdReserva());
            pstmt.setInt(2, entity.getIdEquipo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ReservaEquipo> fetch() {
        ArrayList<ReservaEquipo> lista = new ArrayList<>();
        String query = "SELECT id_reserva, id_equipo FROM reserva_equipo";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int idReserva = rs.getInt("id_reserva");
                int idEquipo = rs.getInt("id_equipo");

                ReservaEquipo reservaEquipo = new ReservaEquipo(idReserva, idEquipo);
                lista.add(reservaEquipo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void update(ReservaEquipo entity) {
        String query = "UPDATE reserva_equipo SET id_equipo = ? WHERE id_reserva = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, entity.getIdEquipo());
            pstmt.setInt(2, entity.getIdReserva());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int idReserva, int idEquipo) {
        String query = "DELETE FROM reserva_equipo WHERE id_reserva = ? AND id_equipo = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idReserva);
            pstmt.setInt(2, idEquipo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(int idReserva, int idEquipo) {
        String sql = "SELECT id_reserva, id_equipo FROM reserva_equipo WHERE id_reserva = ? AND id_equipo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idReserva);
            stmt.setInt(2, idEquipo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}

