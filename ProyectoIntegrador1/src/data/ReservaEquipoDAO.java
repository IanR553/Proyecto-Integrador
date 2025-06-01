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
        String sql = "INSERT INTO PI1SIDS.reserva_equipo (id_reserva, id_equipo) VALUES (?, ?)";

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

    public ArrayList<ReservaEquipo> obtenerReservasConEquiposPorUsuario(long cedulaUsuario) {
        ArrayList<ReservaEquipo> lista = new ArrayList<>();

        String sql = """
            SELECT r.id AS id_reserva, r.tipo, r.estado, 
                   e.id AS id_equipo, e.tipo AS tipo_equipo, e.marca, e.software
            FROM PI1SIDS.reserva r
            JOIN PI1SIDS.reserva_equipo re ON r.id = re.id_reserva
            JOIN PI1SIDS.equipo e ON re.id_equipo = e.id
            WHERE r.cedusuario = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ReservaEquipo reservaEquipo = new ReservaEquipo(
                    rs.getString("id_reserva"),
                    rs.getString("tipo"),
                    rs.getString("estado"),
                    rs.getString("id_equipo"),
                    rs.getString("tipo_equipo"),
                    rs.getString("marca"),
                    rs.getString("software")
                );
                lista.add(reservaEquipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
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

