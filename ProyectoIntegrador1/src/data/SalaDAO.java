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
import model.Sala;

public class SalaDAO implements CRUD_operaciones<Sala, String> { 
    private Connection connection;

    public SalaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Sala sala) {
        String sql = "INSERT INTO PI1SIDS.Sala (nombre, capacidad, estado, ubicacion, software) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, sala.getNombre());
            pstmt.setInt(2, sala.getCapacidad());
            pstmt.setBoolean(3, sala.isEstado());
            pstmt.setString(4, sala.getUbicacion());
            pstmt.setString(5, sala.getSoftware());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public ArrayList<Sala> fetch() {
        ArrayList<Sala> salas = new ArrayList<>();
        String sql = "SELECT * FROM PI1SIDS.Sala";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id"); 
                String nombre = rs.getString("nombre");
                int capacidad = rs.getInt("capacidad");
                boolean estado = rs.getBoolean("estado");
                String ubicacion = rs.getString("ubicacion");
                String software = rs.getString("software");

                Sala sala = new Sala(id, nombre, capacidad, estado, ubicacion, software); 
                salas.add(sala);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salas;
    }

    @Override
    public void update(Sala sala) {
        String sql = "UPDATE PI1SIDS.Sala SET nombre=?, capacidad=?, estado=?, ubicacion=?, software=? WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, sala.getNombre());
            pstmt.setInt(2, sala.getCapacidad());
            pstmt.setBoolean(3, sala.isEstado());
            pstmt.setString(4, sala.getUbicacion());
            pstmt.setString(5, sala.getSoftware());
            pstmt.setString(6, sala.getId()); 

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM PI1SIDS.Sala WHERE id=?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, id);  
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(String id) {
        String sql = "SELECT id FROM PI1SIDS.Sala WHERE id=?";

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
    
	public ArrayList<String> obtenerSoftwaresUnicos() {
	    Set<String> softwareUnicos = new HashSet<>();
	    String query = "SELECT software FROM PI1SIDS.Sala WHERE software IS NOT NULL";

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

	public ArrayList<Sala> fetchDisponiblesPorHorario(String softSala, int capacidadInt, String idHorario) {
	    ArrayList<Sala> lista = new ArrayList<>();
	    try {
	        PreparedStatement stmt;
	        String query;

	        if ("Ninguno".equalsIgnoreCase(softSala)) {
	            // Cuando es "Ninguno", mostrar TODOS los equipos disponibles sin importar el software
	            query = """
	                SELECT * FROM PI1SIDS.SALA s
	                WHERE s.estado = 1
	                AND s.capacidad >= ?
	                AND s.id NOT IN (
	                    SELECT rs.id_sala
	                    FROM PI1SIDS.RESERVA_SALA rs
	                    JOIN PI1SIDS.RESERVA r ON rs.id_reserva = r.id
	                    WHERE UPPER(r.idhorario) = ?
	                )
	                """;
	            stmt = connection.prepareStatement(query);
	            stmt.setInt(1, capacidadInt);
	            stmt.setString(2, idHorario.toUpperCase().trim());
	        } else {
	            // Cuando hay un software específico, filtrar por ese software
	            query = """
	                SELECT * FROM PI1SIDS.SALA s
	                WHERE s.estado = 1
	                AND s.capacidad >= ?
	                AND TRIM(UPPER(s.software)) LIKE ?
	                AND s.id NOT IN (
	                    SELECT rs.id_sala
	                    FROM PI1SIDS.RESERVA_SALA rs
	                    JOIN PI1SIDS.RESERVA r ON rs.id_reserva = r.id
	                    WHERE UPPER(r.idhorario) = ?
	                )
	                """;
	            stmt = connection.prepareStatement(query);
	            stmt.setInt(1, capacidadInt);
	            stmt.setString(2, "%" + softSala.toUpperCase().trim() + "%");
	            stmt.setString(3, idHorario.toUpperCase().trim());
	        }

	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            String id = rs.getString("id");
	            String nombre = rs.getString("nombre");
	            int capacidad = rs.getInt("capacidad");
	            boolean estado = rs.getInt("estado") == 1;
	            String ubicacion = rs.getString("ubicacion");
	            String softwareSala = rs.getString("software");

	            Sala e = new Sala(id, nombre, capacidad, estado, ubicacion, softwareSala);
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



