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
            pstmt.setInt(1, rs.getIdReserva());
            pstmt.setInt(2, rs.getIdSala());
            pstmt.setInt(3, rs.getIdEquipo());
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
                int idReserva = rs.getInt("id_reserva");
                int idSala = rs.getInt("id_sala");
                int idEquipo = rs.getInt("id_equipo");

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
            pstmt.setInt(1, rs.getIdSala());
            pstmt.setInt(2, rs.getIdEquipo());
            pstmt.setInt(3, rs.getIdReserva());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int idReserva, int idSala, int idEquipo) {
        String query = "DELETE FROM reserva_sala WHERE id_reserva = ? AND id_sala = ? AND id_equipo = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, idReserva);
            pstmt.setInt(2, idSala);
            pstmt.setInt(3, idEquipo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(int idReserva, int idSala, int idEquipo) {
        String sql = "SELECT id_reserva, id_sala, id_equipo FROM reserva_sala WHERE id_reserva = ? AND id_sala = ? AND id_equipo = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idReserva);
            stmt.setInt(2, idSala);
            stmt.setInt(3, idEquipo);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

