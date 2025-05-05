package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.ReservaSala;

public class ReservaSalaDAO {

    private Connection connection;

    public ReservaSalaDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(ReservaSala rs) {
        String query = "INSERT INTO reserva_sala (id_reserva, id_sala, id_equipo) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, rs.getIdReserva());
            pstmt.setString(2, rs.getIdSala());
            pstmt.setString(3, rs.getIdEquipo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ReservaSala> fetch() {
        ArrayList<ReservaSala> lista = new ArrayList<>();
        String query = "SELECT id_reserva, id_sala, id_equipo FROM reserva_sala";

        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String idReserva = rs.getString("id_reserva");
                String idSala = rs.getString("id_sala");
                String idEquipo = rs.getString("id_equipo");

                ReservaSala reservaSala = new ReservaSala(idReserva, idSala, idEquipo);
                lista.add(reservaSala);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void update(ReservaSala rs) {
        String query = "UPDATE reserva_sala SET id_sala = ?, id_equipo = ? WHERE id_reserva = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, rs.getIdSala());
            pstmt.setString(2, rs.getIdEquipo());
            pstmt.setString(3, rs.getIdReserva());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String idReserva, String idSala, String idEquipo) {
        String query = "DELETE FROM reserva_sala WHERE id_reserva = ? AND id_sala = ? AND id_equipo = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, idReserva);
            pstmt.setString(2, idSala);
            pstmt.setString(3, idEquipo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String idReserva, String idSala, String idEquipo) {
        String sql = "SELECT id_reserva, id_sala, id_equipo FROM reserva_sala WHERE id_reserva = ? AND id_sala = ? AND id_equipo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, idReserva);
            stmt.setString(2, idSala);
            stmt.setString(3, idEquipo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

