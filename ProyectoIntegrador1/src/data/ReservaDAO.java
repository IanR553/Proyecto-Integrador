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
        String sql = "INSERT INTO Reserva (id, estado, tipo, cedUsuario, idHorario) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, reserva.getId());
            pstmt.setString(2, reserva.getEstado());
            pstmt.setString(3, reserva.getTipo());
            pstmt.setLong(4, reserva.getCedUsuario());
            pstmt.setString(5, reserva.getIdHorario());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Reserva> fetch() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        String query = "SELECT id, estado, tipo, cedUsuario, idHorario FROM Reserva";

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
        String sql = "UPDATE Reserva SET estado = ?, tipo = ?, cedUsuario = ?, idHorario = ? WHERE id = ?";

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
        String sql = "DELETE FROM Reserva WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(String id) {
        String sql = "SELECT id FROM Reserva WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}


