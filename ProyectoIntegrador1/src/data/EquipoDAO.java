package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import model.Equipo;

public class EquipoDAO implements CRUD_operaciones<Equipo, String> {
	private Connection connection;

	public EquipoDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void save(Equipo equipo) {
		String query = "INSERT INTO PI1SIDS.Equipo (tipo, estado, marca, software) VALUES (?, ?, ?, ?)";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, equipo.getTipo());
			pstmt.setBoolean(2, equipo.isEstado());
			pstmt.setString(3, equipo.getMarca());
			pstmt.setString(4, equipo.getSoftware());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Equipo> fetch() {
		ArrayList<Equipo> equipos = new ArrayList<>();
		String query = "SELECT * FROM PI1SIDS.Equipo";

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				String id = rs.getString("id");
				String tipo = rs.getString("tipo");
				boolean estado = rs.getBoolean("estado");
				String marca = rs.getString("marca");
				String software = rs.getString("software");

				Equipo equipo = new Equipo(id, tipo, estado, marca, software);
				equipos.add(equipo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return equipos;
	}

	@Override
	public void update(Equipo equipo) {
		String sql = "UPDATE PI1SIDS.Equipo SET tipo=?, estado=?, marca=?, software=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, equipo.getTipo());
			pstmt.setBoolean(2, equipo.isEstado());
			pstmt.setString(3, equipo.getMarca());
			pstmt.setString(4, equipo.getSoftware());
			pstmt.setString(5, equipo.getId());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String id) {
		String sql = "DELETE FROM PI1SIDS.Equipo WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean authenticate(String id) {
		String sql = "SELECT id FROM PI1SIDS.Equipo WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getString("id").equals(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public ArrayList<Equipo> fetchDisponibles(String tipo, String software) {
	    ArrayList<Equipo> equipos = new ArrayList<>();

	    String query;
	    boolean sinSoftware = software.equalsIgnoreCase("Ninguno");

	    if (sinSoftware) {
	        // Trae todos los equipos disponibles del tipo, sin importar software
	        query = "SELECT * FROM PI1SIDS.Equipo " +
	                "WHERE estado = 1 " +
	                "AND LOWER(TRIM(tipo)) = ?";
	    } else {
	        // Filtrar por tipo y que el software contenga el valor dado
	        query = "SELECT * FROM PI1SIDS.Equipo " +
	                "WHERE estado = 1 " +
	                "AND LOWER(TRIM(tipo)) = ? " +
	                "AND LOWER(software) LIKE ?";
	    }

	    try (PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, tipo.trim().toLowerCase());

	        if (!sinSoftware) {
	            stmt.setString(2, "%" + software.trim().toLowerCase() + "%");
	        }

	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            String id = rs.getString("id");
	            String tipoResult = rs.getString("tipo");
	            boolean estado = rs.getBoolean("estado");
	            String marca = rs.getString("marca");
	            String softwareResult = rs.getString("software");

	            Equipo equipo = new Equipo(id, tipoResult, estado, marca, softwareResult);
	            equipos.add(equipo);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return equipos;
	}




	public void updateNoDisponible(String id) {
		String sql = "UPDATE PI1SIDS.Equipo SET estado = 0 WHERE id = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> obtenerSoftwaresUnicos() {
	    Set<String> softwareUnicos = new HashSet<>();
	    String query = "SELECT software FROM PI1SIDS.Equipo WHERE software IS NOT NULL";

	    try (PreparedStatement stmt = connection.prepareStatement(query);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            String softwares = rs.getString("software");
	            if (softwares != null && !softwares.trim().isEmpty()) {
	                String[] separados = softwares.split(",");
	                for (String sw : separados) {
	                    String limpio = sw.trim().toUpperCase(); // Convertir a mayúsculas
	                    if (!limpio.isEmpty()) {
	                        softwareUnicos.add(limpio);
	                    }
	                }
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    ArrayList<String> listaOrdenada = new ArrayList<>(softwareUnicos);
	    Collections.sort(listaOrdenada); // Orden alfabético en mayúsculas
	    return listaOrdenada;
	}


	public ArrayList<Equipo> fetchDisponiblesPorHorario(String tipo, String software, String idHorario) {
	    ArrayList<Equipo> lista = new ArrayList<>();
	    try {
	        PreparedStatement stmt;
	        String query;

	        if ("Ninguno".equalsIgnoreCase(software)) {
	            // Cuando es "Ninguno", mostrar TODOS los equipos disponibles sin importar el software
	            query = """
	                SELECT * FROM PI1SIDS.EQUIPO e
	                WHERE e.estado = 1
	                AND TRIM(UPPER(e.tipo)) = ?
	                AND e.id NOT IN (
	                    SELECT re.id_equipo
	                    FROM PI1SIDS.RESERVA_EQUIPO re
	                    JOIN PI1SIDS.RESERVA r ON re.id_reserva = r.id
	                    WHERE UPPER(r.idhorario) = ?
	                )
	                """;
	            stmt = connection.prepareStatement(query);
	            stmt.setString(1, tipo.toUpperCase().trim());
	            stmt.setString(2, idHorario.toUpperCase().trim());
	        } else {
	            // Cuando hay un software específico, filtrar por ese software
	            query = """
	                SELECT * FROM PI1SIDS.EQUIPO e
	                WHERE e.estado = 1
	                AND TRIM(UPPER(e.tipo)) = ?
	                AND TRIM(UPPER(e.software)) LIKE ?
	                AND e.id NOT IN (
	                    SELECT re.id_equipo
	                    FROM PI1SIDS.RESERVA_EQUIPO re
	                    JOIN PI1SIDS.RESERVA r ON re.id_reserva = r.id
	                    WHERE UPPER(r.idhorario) = ?
	                )
	                """;
	            stmt = connection.prepareStatement(query);
	            stmt.setString(1, tipo.toUpperCase().trim());
	            stmt.setString(2, "%" + software.toUpperCase().trim() + "%");
	            stmt.setString(3, idHorario.toUpperCase().trim());
	        }

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            String id = rs.getString("id");
	            String tipoEquipo = rs.getString("tipo");
	            boolean estado = rs.getInt("estado") == 1;
	            String marca = rs.getString("marca");
	            String softwareEquipo = rs.getString("software");

	            Equipo e = new Equipo(id, tipoEquipo, estado, marca, softwareEquipo);
	            lista.add(e);
	        }
	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return lista;
	}

}
