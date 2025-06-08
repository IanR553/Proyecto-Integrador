package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import model.ReservaEquipo;
import oracle.jdbc.OracleTypes;

public class ReservaEquipoDAO {

    private Connection connection;

    public ReservaEquipoDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean save(ReservaEquipo reservaEquipo) {
        String call = "{ ? = call PI1SIDS.insertar_reserva_equipo(?, ?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, reservaEquipo.getIdReserva());
            cstmt.setString(3, reservaEquipo.getIdEquipo());

            cstmt.execute();
            int resultado = cstmt.getInt(1);

            return resultado == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public ArrayList<ReservaEquipo> obtenerReservasConEquiposPorUsuario(long cedulaUsuario) {
        ArrayList<ReservaEquipo> lista = new ArrayList<>();
        String call = "{ ? = call PI1SIDS.reservas_equipos_usuario(?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.setLong(2, cedulaUsuario);
            cstmt.execute();

            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
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
        String call = "{ call PI1SIDS.eliminar_reserva_equipo(?, ?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.setString(1, idReserva);
            cstmt.setString(2, idEquipo);
            cstmt.execute();
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

    public boolean existePorIdReserva(String idReserva) {
        String call = "{ ? = call PI1SIDS.existe_reserva_equipo(?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, idReserva);

            cstmt.execute();
            int resultado = cstmt.getInt(1);
            return resultado > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

