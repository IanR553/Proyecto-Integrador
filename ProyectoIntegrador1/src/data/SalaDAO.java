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

import model.Equipo;
import model.Sala;

public class SalaDAO implements CRUD_operaciones<Sala, String> { 
    private Connection connection;

    public SalaDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Sala sala) {
        String call = "{ call PI1SIDS.insertar_sala(?, ?, ?, ?, ?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.setString(1, sala.getNombre());
            cstmt.setInt(2, sala.getCapacidad());
            cstmt.setInt(3, sala.isEstado() ? 1 : 0);
            cstmt.setString(4, sala.getUbicacion());
            cstmt.setString(5, sala.getSoftware());

            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public ArrayList<Sala> fetch() {
        ArrayList<Sala> salas = new ArrayList<>();
        String call = "{ ? = call PI1SIDS.obtener_todas_salas() }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cstmt.execute();

            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String nombre = rs.getString("nombre");
                    int capacidad = rs.getInt("capacidad");
                    boolean estado = rs.getInt("estado") == 1;
                    String ubicacion = rs.getString("ubicacion");
                    String software = rs.getString("software");

                    Sala sala = new Sala(id, nombre, capacidad, estado, ubicacion, software);
                    salas.add(sala);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salas;
    }


    @Override
    public void update(Sala sala) {
        String call = "{ call PI1SIDS.actualizar_sala(?, ?, ?, ?, ?, ?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.setString(1, sala.getId());
            cstmt.setString(2, sala.getNombre());
            cstmt.setInt(3, sala.getCapacidad());
            cstmt.setInt(4, sala.isEstado() ? 1 : 0);
            cstmt.setString(5, sala.getUbicacion());
            cstmt.setString(6, sala.getSoftware());

            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(String id) {
        String call = "{ call PI1SIDS.eliminar_sala(?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.setString(1, id);
            cstmt.executeUpdate();
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
        ArrayList<String> listaOrdenada = new ArrayList<>();
        Set<String> softwareUnicos = new HashSet<>();
        String call = "{ ? = call PI1SIDS.obtener_softwares_sala_unicos }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cstmt.execute();

            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    String software = rs.getString("software");
                    if (software != null && !software.trim().isEmpty()) {
                        softwareUnicos.add(software.trim().toUpperCase());
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        listaOrdenada.addAll(softwareUnicos);
        Collections.sort(listaOrdenada);
        return listaOrdenada;
    }


    public ArrayList<Sala> fetchDisponiblesPorHorario(String softSala, int capacidadInt, String idHorario) {
        ArrayList<Sala> lista = new ArrayList<>();
        String call = "{ ? = call PI1SIDS.fetch_salas_disponiblesHor(?, ?, ?) }";

        try (CallableStatement cstmt = connection.prepareCall(call)) {
            cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cstmt.setString(2, softSala);
            cstmt.setInt(3, capacidadInt);
            cstmt.setString(4, idHorario);

            cstmt.execute();
            try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
                while (rs.next()) {
                    String id = rs.getString("id");
                    String nombre = rs.getString("nombre");
                    int capacidad = rs.getInt("capacidad");
                    boolean estado = rs.getInt("estado") == 1;
                    String ubicacion = rs.getString("ubicacion");
                    String softwareSala = rs.getString("software");

                    Sala sala = new Sala(id, nombre, capacidad, estado, ubicacion, softwareSala);
                    lista.add(sala);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}



