package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import oracle.jdbc.OracleTypes;

import model.Equipo;

public class EquipoDAO implements CRUD_operaciones<Equipo, String> {
	private Connection connection;

	public EquipoDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void save(Equipo equipo) {
	    String call = "{ CALL PI1SIDS.insertar_equipo(?, ?, ?, ?) }";

	    try (CallableStatement cstmt = connection.prepareCall(call)) {
	        cstmt.setString(1, equipo.getTipo());
	        cstmt.setInt(2, equipo.isEstado()? 1 : 0);
	        cstmt.setString(3, equipo.getMarca());
	        cstmt.setString(4, equipo.getSoftware());

	        cstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	@Override
	public ArrayList<Equipo> fetch() {
	    ArrayList<Equipo> equipos = new ArrayList<>();
	    String call = "{ ? = call PI1SIDS.obtener_todos_equipos() }";

	    try (CallableStatement cstmt = connection.prepareCall(call)) {
	        cstmt.registerOutParameter(1, OracleTypes.CURSOR);
	        cstmt.execute();

	        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
	            while (rs.next()) {
	                String id = rs.getString("id");
	                String tipo = rs.getString("tipo");
	                boolean estado = rs.getBoolean("estado");
	                String marca = rs.getString("marca");
	                String software = rs.getString("software");

	                Equipo equipo = new Equipo(id, tipo, estado, marca, software);
	                equipos.add(equipo);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return equipos;
	}


	@Override
	public void update(Equipo equipo) {
	    String call = "{ CALL PI1SIDS.actualizar_equipo(?, ?, ?, ?, ?) }";

	    try (CallableStatement cstmt = connection.prepareCall(call)) {
	        cstmt.setString(1, equipo.getId());
	        cstmt.setString(2, equipo.getTipo());
	        cstmt.setBoolean(3, equipo.isEstado());
	        cstmt.setString(4, equipo.getMarca());
	        cstmt.setString(5, equipo.getSoftware());

	        cstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	@Override
	public void delete(String id) {
	    String call = "{ CALL PI1SIDS.eliminar_equipo(?) }";

	    try (CallableStatement cstmt = connection.prepareCall(call)) {
	        cstmt.setString(1, id);
	        cstmt.executeUpdate();
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
	    String call = "{ ? = call PI1SIDS.obtener_softwaresEquipo() }";

	    try (CallableStatement cstmt = connection.prepareCall(call)) {
	        cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	        cstmt.execute();

	        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
	            while (rs.next()) {
	                String softwares = rs.getString("software");
	                if (softwares != null && !softwares.trim().isEmpty()) {
	                    String[] separados = softwares.split(",");
	                    for (String sw : separados) {
	                        String limpio = sw.trim().toUpperCase();
	                        if (!limpio.isEmpty()) {
	                            softwareUnicos.add(limpio);
	                        }
	                    }
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    ArrayList<String> listaOrdenada = new ArrayList<>(softwareUnicos);
	    Collections.sort(listaOrdenada);
	    return listaOrdenada;
	}



	public ArrayList<Equipo> fetchDisponiblesPorHorario(String tipo, String software, String idHorario) {
	    ArrayList<Equipo> lista = new ArrayList<>();
	    String call = "{ ? = call PI1SIDS.obtener_equipos_disponibles(?, ?, ?) }";

	    try (CallableStatement cstmt = connection.prepareCall(call)) {
	        cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
	        cstmt.setString(2, tipo);
	        cstmt.setString(3, software);
	        cstmt.setString(4, idHorario);

	        cstmt.execute();

	        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
	            while (rs.next()) {
	                String id = rs.getString("id");
	                String tipoEquipo = rs.getString("tipo");
	                boolean estado = rs.getInt("estado") == 1;
	                String marca = rs.getString("marca");
	                String softwareEquipo = rs.getString("software");

	                Equipo e = new Equipo(id, tipoEquipo, estado, marca, softwareEquipo);
	                lista.add(e);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return lista;
	}


}
