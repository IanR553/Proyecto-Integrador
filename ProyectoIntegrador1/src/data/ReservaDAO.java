package data;

import model.Horario;
import model.RequerimientoSalas;
import model.Reserva;
import model.Rol;
import model.Usuario;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class ReservaDAO implements CRUD_operaciones<Reserva, Integer> {
    private Connection connection;

    public ReservaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Reserva reserva) {
        String sql = "INSERT INTO Reserva (id, estado, tipo, cedUsuario, idHorario) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reserva.getId());
            pstmt.setString(2, reserva.getEstado());
            pstmt.setString(3, reserva.getTipo());
            pstmt.setInt(4, reserva.getUsuario().getCedula());
            pstmt.setInt(5, reserva.getHorario().getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // 
        }
    }

    @Override
    public ArrayList<Reserva> fetch() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        String query = "SELECT r.id, r.estado, r.tipo, r.cedUsuario, r.idHorario, " +
                       "h.id as horario_id, h.semana, h.dia, h.horaInicio, h.horaFin, " +
                       "u.idRol as rol_id, u.cedula, u.primerNombre, u.segundoNombre, " +
                       "u.primerApellido, u.segundoApellido, u.correoElectronico, u.celular " +
                       "FROM Reserva r " +
                       "JOIN Horario h ON r.idHorario = h.id " +
                       "JOIN Usuario u ON r.cedUsuario = u.cedula";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
 
                int horarioId = rs.getInt("horario_id");
                int semana = rs.getInt("semana");
                String dia = rs.getString("dia");
                LocalTime horaInicio = rs.getTime("horaInicio").toLocalTime();
                LocalTime horaFin = rs.getTime("horaFin").toLocalTime();
                ArrayList<RequerimientoSalas> requerimientos = new ArrayList<>(); // Lista de requerimientos vacia por ahora

                Horario horario = new Horario(horarioId, semana, dia, horaInicio, horaFin, requerimientos);

                // Crear el objeto Rol
                int rolId = rs.getInt("rol_id");
                String rolNombre = rs.getString("rol_nombre");
                Rol rol = new Rol(rolId, rolNombre);

                // Crear el objeto Usuario
                int cedula = rs.getInt("cedula");
                String primerNombre = rs.getString("primerNombre");
                String segundoNombre = rs.getString("segundoNombre");
                String primerApellido = rs.getString("primerApellido");
                String segundoApellido = rs.getString("segundoApellido");
                String correoElectronico = rs.getString("correoElectronico");
                int celular = rs.getInt("celular");

                Usuario usuario = new Usuario(cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, correoElectronico, celular, rol);

                // Crear el objeto Reserva
                int idReserva = rs.getInt("id");
                String estado = rs.getString("estado");
                String tipo = rs.getString("tipo");
                Reserva reserva = new Reserva(idReserva, estado, tipo, usuario, horario);
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
            pstmt.setInt(3, reserva.getUsuario().getCedula());
            pstmt.setInt(4, reserva.getHorario().getId());
            pstmt.setInt(5, reserva.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Reserva WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }

    @Override
    public boolean authenticate(Integer id) {
        String sql = "SELECT id FROM Reserva WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id") == id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
