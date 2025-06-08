package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalTime;
import java.util.ArrayList;
import oracle.jdbc.OracleTypes;

import model.Horario;

public class HorarioDAO implements CRUD_operaciones<Horario, String> {

	private Connection connection;

	public HorarioDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void save(Horario horario) {
		String sql = "INSERT INTO PI1SIDS.Horario (id, semana, dia, horaInicio, horaFin) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, horario.getId());
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
		String sql = "SELECT * FROM PI1SIDS.Horario";

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				String id = rs.getString("id");
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
		String sql = "UPDATE PI1SIDS.Horario SET semana=?, dia=?, horaInicio=?, horaFin=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, horario.getSemana());
			pstmt.setString(2, horario.getDia());
			pstmt.setTime(3, Time.valueOf(horario.getHoraInicio()));
			pstmt.setTime(4, Time.valueOf(horario.getHoraFin()));
			pstmt.setString(5, horario.getId());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String id) {
		String sql = "DELETE FROM PI1SIDS.Horario WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean authenticate(String id) {
		String sql = "SELECT id FROM PI1SIDS.Horario WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();

			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public String traerIdHorario(int semana, String dia, LocalTime horaInicio, LocalTime horaFin) {
	    String idHorario = null;
	    String call = "{ ? = call PI1SIDS.obtener_id_horario(?, ?, ?, ?) }";

	    try (CallableStatement cstmt = connection.prepareCall(call)) {
	        cstmt.registerOutParameter(1, Types.VARCHAR);
	        cstmt.setInt(2, semana);
	        cstmt.setString(3, dia);

	        Time sqlHoraInicio = Time.valueOf(horaInicio);
	        Time sqlHoraFin = Time.valueOf(horaFin);

	        cstmt.setTime(4, sqlHoraInicio);
	        cstmt.setTime(5, sqlHoraFin);

	        cstmt.execute();
	        idHorario = cstmt.getString(1);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return idHorario;
	}


}
