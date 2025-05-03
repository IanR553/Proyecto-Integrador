package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;

import model.Horario;

public class HorarioDAO implements CRUD_operaciones<Horario, Integer> {

    private Connection connection;

    public HorarioDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Horario horario) {
        String sql = "INSERT INTO Horario (id, semana, dia, horaInicio, horaFin) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, horario.getId());
            pstmt.setInt(2, horario.getSemana());
            pstmt.setString(3, horario.getDia());
            pstmt.setTime(4, Time.valueOf(horario.getHoraInicio()));
            pstmt.setTime(5, Time.valueOf(horario.getHoraFin()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Horario> fetch() {
        ArrayList<Horario> horarios = new ArrayList<>();
        String sql = "SELECT * FROM Horario";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int semana = rs.getInt("semana");
                String dia = rs.getString("dia");
                LocalTime horaInicio = rs.getTime("horaInicio").toLocalTime();
                LocalTime horaFin = rs.getTime("horaFin").toLocalTime();

                Horario horario = new Horario(id, semana, dia, horaInicio, horaFin, new ArrayList<>());
                horarios.add(horario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return horarios;
    }

    @Override
    public void update(Horario horario) {
        String sql = "UPDATE Horario SET semana=?, dia=?, horaInicio=?, horaFin=? WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, horario.getSemana());
            pstmt.setString(2, horario.getDia());
            pstmt.setTime(3, Time.valueOf(horario.getHoraInicio()));
            pstmt.setTime(4, Time.valueOf(horario.getHoraFin()));
            pstmt.setInt(5, horario.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Horario WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(Integer id) {
        String sql = "SELECT id FROM Horario WHERE id=?";

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

