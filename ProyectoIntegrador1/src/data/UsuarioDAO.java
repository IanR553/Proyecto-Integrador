package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Usuario;

public class UsuarioDAO implements CRUD_operaciones<Usuario, Long>{

	private Connection connection;

	public UsuarioDAO(Connection connection) {
		this.connection = connection;
	}

    @Override
	public void save(Usuario usuario) {
		String query = "INSERT INTO PI1SIDS.Usuario (cedula, primerNombre, segundoNombre, primerApellido, segundoApellido, correoElectronico, celular, idRol, contraseña) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, usuario.getCedula());
			pstmt.setString(2, usuario.getPrimerNombre());
			pstmt.setString(3, usuario.getSegundoNombre());
			pstmt.setString(4, usuario.getPrimerApellido());
			pstmt.setString(5, usuario.getSegundoApellido());
			pstmt.setString(6, usuario.getCorreoElectronico());
			pstmt.setLong(7, usuario.getCelular());
			pstmt.setString(8, usuario.getidRol());
			pstmt.setString(9, usuario.getContraseña());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    @Override
	public ArrayList<Usuario> fetch() {
		ArrayList<Usuario> usuarios = new ArrayList<>();
		String query = "SELECT u.cedula, u.primerNombre, u.segundoNombre, u.primerApellido, u.segundoApellido, "
				+ "u.correoElectronico, u.celular, u.idRol, u.contraseña " + "FROM PI1SIDS.Usuario u";

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				Long cedula = rs.getLong("cedula");
				String primerNombre = rs.getString("primerNombre");
				String segundoNombre = rs.getString("segundoNombre");
				String primerApellido = rs.getString("primerApellido");
				String segundoApellido = rs.getString("segundoApellido");
				String correoElectronico = rs.getString("correoElectronico");
				Long celular = rs.getLong("celular");
				String idRol = rs.getString("idRol");
				String contraseña = rs.getString("contraseña");

				Usuario usuario = new Usuario(cedula, primerNombre, segundoNombre, primerApellido, segundoApellido,
						correoElectronico, celular, idRol, contraseña);
				usuarios.add(usuario);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return usuarios;
	}

    @Override
	public void update(Usuario usuario) {
		String query = "UPDATE PI1SIDS.Usuario SET primerNombre=?, segundoNombre=?, primerApellido=?, segundoApellido=?, correoElectronico=?, celular=?, idRol=?, contraseña=? WHERE cedula=?";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, usuario.getPrimerNombre());
			pstmt.setString(2, usuario.getSegundoNombre());
			pstmt.setString(3, usuario.getPrimerApellido());
			pstmt.setString(4, usuario.getSegundoApellido());
			pstmt.setString(5, usuario.getCorreoElectronico());
			pstmt.setLong(6, usuario.getCelular());
			pstmt.setString(7, usuario.getidRol());
			pstmt.setString(8, usuario.getContraseña());
			pstmt.setLong(9, usuario.getCedula());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    @Override
	public void delete(Long cedula) {
		String query = "DELETE FROM PI1SIDS.Usuario WHERE cedula=?";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, cedula);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    @Override
    public boolean authenticate(Long cedula) {
        String query = "SELECT cedula FROM PI1SIDS.Usuario WHERE cedula=?";

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
    
	public boolean authenticate(Long cedula, String contraseña, String rol) {

		String sql = "SELECT * FROM PI1SIDS.Usuario WHERE cedula=? AND contraseña=? AND idrol=?";

		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setLong(1, cedula);
			stmt.setString(2, contraseña);
			stmt.setString(3, rol);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong("cedula") == cedula && rs.getString("contraseña").equals(contraseña)
						&& rs.getString("idrol").equals(rol);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean authenticateLogin(Long cedula, String password) {
		String query = "SELECT * FROM PI1SIDS.Usuario WHERE cedula = ? AND contraseña = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, cedula);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public Usuario obtenerUsuarioPorCredenciales(Long cedula, String password) {
		String query = "SELECT * FROM PI1SIDS.Usuario WHERE cedula = ? AND contraseña = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setLong(1, cedula);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String primerNombre = rs.getString("primerNombre");
				String segundoNombre = rs.getString("segundoNombre");
				String primerApellido = rs.getString("primerApellido");
				String segundoApellido = rs.getString("segundoApellido");
				String correoElectronico = rs.getString("correoElectronico");
				Long celular = rs.getLong("celular");
				String idRol = rs.getString("idRol");
				String contraseña = rs.getString("contraseña");

				return new Usuario(cedula, primerNombre, segundoNombre, primerApellido, segundoApellido,
						correoElectronico, celular, idRol, contraseña);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
