package data;

import model.Reserva;

import java.sql.*;
import java.util.ArrayList;

public class ReservaDAO implements CRUD_operaciones<Reserva, String> {
    private Connection connection;

    public ReservaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Reserva reserva) {
        String call = "{ call PI1SIDS.insertar_reserva(?, ?, ?, ?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.setString(1, reserva.getEstado());
            cstmt.setString(2, reserva.getTipo());
            cstmt.setLong(3, reserva.getCedUsuario());
            cstmt.setString(4, reserva.getIdHorario());

            cstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public ArrayList<Reserva> fetch() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        String query = "SELECT id, estado, tipo, cedUsuario, idHorario FROM PI1SIDS.Reserva";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String estado = rs.getString("estado");
                String tipo = rs.getString("tipo");
                long cedUsuario = rs.getLong("cedUsuario");
                String idHorario = rs.getString("idHorario");

                Reserva reserva = new Reserva(id, estado, tipo, cedUsuario, idHorario);
                reservas.add(reserva);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservas;
    }

    @Override
    public void update(Reserva reserva) {
        String sql = "UPDATE PI1SIDS.Reserva SET estado = ?, tipo = ?, cedUsuario = ?, idHorario = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, reserva.getEstado());
            pstmt.setString(2, reserva.getTipo());
            pstmt.setLong(3, reserva.getCedUsuario());
            pstmt.setString(4, reserva.getIdHorario());
            pstmt.setString(5, reserva.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String call = "{ call PI1SIDS.eliminar_reserva(?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.setString(1, id);
            cstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean authenticate(String id) {
        String sql = "SELECT id FROM PI1SIDS.Reserva WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    

    public String traerIdPorCedulaYHorario(long cedula, String idHorario, String tipo) {
        String idReserva = null;
        String call = "{ ? = call PI1SIDS.traer_id_reserva(?, ?, ?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.registerOutParameter(1, Types.VARCHAR);
            cstmt.setLong(2, cedula);
            cstmt.setString(3, idHorario);
            cstmt.setString(4, tipo);

            cstmt.execute();
            idReserva = cstmt.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idReserva;
    }
    
    public void delete_por_admin(String id) {
        String call = "{ call PI1SIDS.eliminar_reserva_modAdmin(?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.setString(1, id);
            cstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


