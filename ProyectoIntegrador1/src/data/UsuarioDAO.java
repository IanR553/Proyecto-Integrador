package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Usuario;

public class UsuarioDAO implements CRUD_operaciones<Usuario, Long> {

    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Usuario usuario) {
        String query = "INSERT INTO Usuario (cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, correoElectronico, celular, idRol) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, usuario.getCedula()); 
            pstmt.setString(2, usuario.getPrimerNombre());
            pstmt.setString(3, usuario.getSegundoNombre());
            pstmt.setString(4, usuario.getPrimerApellido());
            pstmt.setString(5, usuario.getSegundoApellido());
            pstmt.setString(6, usuario.getCorreoElectronico());
            pstmt.setInt(7, usuario.getCelular());
            pstmt.setString(8, usuario.getidRol()); 

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Usuario> fetch() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT u.cedula, u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, " +
                       "u.correoElectronico, u.celular, u.idRol " +
                       "FROM Usuario u";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Long cedula = rs.getLong("cedula"); 
                String primerNombre = rs.getString("primerNombre");
                String segundoNombre = rs.getString("segundoNombre");
                String primerApellido = rs.getString("primerApellido");
                String segundoApellido = rs.getString("segundoApellido");
                String correoElectronico = rs.getString("correoElectronico");
                int celular = rs.getInt("celular");
                String idRol = rs.getString("idRol"); 

                Usuario usuario = new Usuario(cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, correoElectronico, celular, idRol);
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    @Override
    public void update(Usuario usuario) {
        String query = "UPDATE Usuario SET primerNombre=?, segundoNombre=?, primerApellido=?, segundoApellido=?, correoElectronico=?, celular=?, idRol=? WHERE cedula=?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, usuario.getPrimerNombre());
            pstmt.setString(2, usuario.getSegundoNombre());
            pstmt.setString(3, usuario.getPrimerApellido());
            pstmt.setString(4, usuario.getSegundoApellido());
            pstmt.setString(5, usuario.getCorreoElectronico());
            pstmt.setInt(6, usuario.getCelular());
            pstmt.setString(7, usuario.getidRol()); 
            pstmt.setLong(8, usuario.getCedula()); 

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long cedula) {
        String query = "DELETE FROM Usuario WHERE cedula=?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, cedula); 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean authenticate(Long cedula) {
        String query = "SELECT cedula FROM Usuario WHERE cedula=?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setLong(1, cedula); 
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getLong("cedula") == cedula; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}


