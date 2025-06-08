package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import model.ReservaEquipo;
import model.ReservaSala;
import oracle.jdbc.OracleTypes;

public class ReservaSalaDAO {

    private Connection connection;

    public ReservaSalaDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean save(ReservaSala rs) {
        String call = "{ ? = call PI1SIDS.save_reserva_sala(?, ?, ?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, rs.getIdReserva());
            cstmt.setString(3, rs.getIdSala());
            cstmt.setString(4, rs.getIdEquipo());

            cstmt.execute();
            int resultado = cstmt.getInt(1);
            return resultado == 1;
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
        String call = "{ call PI1SIDS.delete_reserva_sala(?, ?, ?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.setString(1, idReserva);
            cstmt.setString(2, idSala);

            if (idEquipo == null || idEquipo.trim().isEmpty()) {
                cstmt.setNull(3, Types.VARCHAR);
            } else {
                cstmt.setString(3, idEquipo);
            }

            cstmt.execute();
            System.out.println("Reserva sala eliminada correctamente.");

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
        String call = "{ ? = call PI1SIDS.get_reservas_salas_por_usuario(?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.registerOutParameter(1, OracleTypes.CURSOR);
            cstmt.setLong(2, cedulaUsuario);
            cstmt.execute();

            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
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
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean existePorIdReserva(String idReserva) {
        String call = "{ ? = call PI1SIDS.existe_reserva_sala(?) }";

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

