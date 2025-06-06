package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.ReservaEquipo;
import model.ReservaSala;

public class ReservaSalaDAO {

    private Connection connection;

    public ReservaSalaDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean save(ReservaSala rs) {
        String query = "INSERT INTO PI1SIDS.reserva_sala (id_reserva, id_sala, id_equipo) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, rs.getIdReserva());
            pstmt.setString(2, rs.getIdSala());
            pstmt.setString(3, rs.getIdEquipo());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<ReservaSala> fetch() {
        ArrayList<ReservaSala> lista = new ArrayList<>();
        String query = "SELECT id_reserva, id_sala, id_equipo FROM PI1SIDS.reserva_sala";

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
        String query = "UPDATE PI1SIDS.reserva_sala SET id_sala = ?, id_equipo = ? WHERE id_reserva = ?";

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
        String query;
        boolean equipoEsNull = (idEquipo == null || idEquipo.trim().isEmpty());

        if (equipoEsNull) {
            query = "DELETE FROM PI1SIDS.reserva_sala WHERE id_reserva = ? AND id_sala = ? AND id_equipo IS NULL";
        } else {
            query = "DELETE FROM PI1SIDS.reserva_sala WHERE id_reserva = ? AND id_sala = ? AND id_equipo = ?";
        }

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, idReserva);
            pstmt.setString(2, idSala);

            if (!equipoEsNull) {
                pstmt.setString(3, idEquipo);
            }

            int filas = pstmt.executeUpdate();
            System.out.println("Filas eliminadas: " + filas);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public boolean authenticate(String idReserva, String idSala, String idEquipo) {
        String sql = "SELECT PI1SIDS.id_reserva, id_sala, id_equipo FROM reserva_sala WHERE id_reserva = ? AND id_sala = ? AND id_equipo = ?";

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

    public ArrayList<ReservaSala> obtenerReservasSalasPorUsuario(long cedulaUsuario) {
        ArrayList<ReservaSala> lista = new ArrayList<>();

        String sql = """
        	    SELECT r.id AS id_reserva, r.tipo, r.estado,
        	           s.id AS id_sala, s.nombre, s.capacidad, s.ubicacion, s.software,
        	           rs.id_equipo
        	    FROM PI1SIDS.reserva r
        	    JOIN PI1SIDS.reserva_sala rs ON r.id = rs.id_reserva
        	    JOIN PI1SIDS.sala s ON rs.id_sala = s.id
        	    WHERE r.cedusuario = ?
        	""";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, cedulaUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ReservaSala reservaSala = new ReservaSala(
                    rs.getString("id_reserva"),
                    rs.getString("tipo"),
                    rs.getString("estado"),
                    rs.getString("id_sala"),
                    rs.getString("nombre"),
                    rs.getInt("capacidad"),
                    rs.getString("ubicacion"),
                    rs.getString("software")
                );
                reservaSala.setIdEquipo(rs.getString("id_equipo"));
                lista.add(reservaSala);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}

