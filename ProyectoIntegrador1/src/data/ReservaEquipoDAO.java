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

    public boolean save(ReservaEquipo reservaEquipo) {
        String sql = "INSERT INTO PI1SIDS.ReservaEquipo (idReserva, idEquipo) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, reservaEquipo.getIdReserva());
            pstmt.setString(2, reservaEquipo.getIdEquipo());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public ArrayList<ReservaEquipo> fetch() {
        ArrayList<ReservaEquipo> lista = new ArrayList<>();
        String query = "SELECT id_reserva, id_equipo FROM PI1SIDS.reserva_equipo";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String idReserva = rs.getString("id_reserva");
                String idEquipo = rs.getString("id_equipo");

                ReservaEquipo reservaEquipo = new ReservaEquipo(idReserva, idEquipo);
                lista.add(reservaEquipo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void update(ReservaEquipo entity) {
        String query = "UPDATE PI1SIDS.reserva_equipo SET id_equipo = ? WHERE id_reserva = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, entity.getIdEquipo());
            pstmt.setString(2, entity.getIdReserva());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String idReserva, String idEquipo) {
        String query = "DELETE FROM PI1SIDS.reserva_equipo WHERE id_reserva = ? AND id_equipo = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, idReserva);
            pstmt.setString(2, idEquipo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String idReserva, String idEquipo) {
        String sql = "SELECT id_reserva, id_equipo FROM PI1SIDS.reserva_equipo WHERE id_reserva = ? AND id_equipo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idReserva);
            stmt.setString(2, idEquipo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}

