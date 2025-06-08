package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import oracle.jdbc.OracleTypes;

import model.Usuario;

public class UsuarioDAO implements CRUD_operaciones<Usuario, Long> {

	private Connection connection;

	public UsuarioDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void save(Usuario usuario) {
	    String call = "{call PI1SIDS.insertar_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

	    try (CallableStatement stmt = connection.prepareCall(call)) {
	        stmt.setLong(1, usuario.getCedula());
	        stmt.setString(2, usuario.getPrimerNombre());
	        stmt.setString(3, usuario.getSegundoNombre());
	        stmt.setString(4, usuario.getPrimerApellido());
	        stmt.setString(5, usuario.getSegundoApellido());
	        stmt.setString(6, usuario.getCorreoElectronico());
	        stmt.setLong(7, usuario.getCelular());
	        stmt.setString(8, usuario.getidRol());
	        stmt.setString(9, usuario.getContraseña());

	        stmt.execute();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	@Override
	public ArrayList<Usuario> fetch() {
		ArrayList<Usuario> usuarios = new ArrayList<>();
		String sql = "{ ? = call PI1SIDS.obtener_usuarios() }";

		try (CallableStatement cstmt = connection.prepareCall(sql)) {
			cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.execute();

			try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
				while (rs.next()) {
					Long cedula = rs.getLong("cedula");
					String primerNombre = rs.getString("primerNombre");
					String segundoNombre = rs.getString("segundoNombre");
					String primerApellido = rs.getString("primerApellido");
					String segundoApellido = rs.getString("segundoApellido");
					String correoElectronico = rs.getString("correoElectronico");
					Long celular = rs.getLong("celular");
					String idRol = rs.getString("idRol");
					String contraseña = rs.getString("contrasena");

					Usuario usuario = new Usuario(cedula, primerNombre, segundoNombre, primerApellido, segundoApellido,
							correoElectronico, celular, idRol, contraseña);
					usuarios.add(usuario);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return usuarios;
	}

	@Override
	public void update(Usuario usuario) {
	    String call = "{call PI1SIDS.actualizar_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

	    try (CallableStatement cstmt = connection.prepareCall(call)) {
	        cstmt.setLong(1, usuario.getCedula());
	        cstmt.setString(2, usuario.getPrimerNombre());
	        cstmt.setString(3, usuario.getSegundoNombre());
	        cstmt.setString(4, usuario.getPrimerApellido());
	        cstmt.setString(5, usuario.getSegundoApellido());
	        cstmt.setString(6, usuario.getCorreoElectronico());
	        cstmt.setLong(7, usuario.getCelular());
	        cstmt.setString(8, usuario.getidRol());
	        cstmt.setString(9, usuario.getContraseña());

	        cstmt.execute();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	@Override
	public void delete(Long cedula) {
	    String call = "{call PI1SIDS.eliminar_usuario(?)}";

	    try (CallableStatement cstmt = connection.prepareCall(call)) {
	        cstmt.setLong(1, cedula);
	        cstmt.execute();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	@Override
	public boolean authenticate(Long cedula) {
		boolean existe = false;
		String sql = "{ ? = call PI1SIDS.autenticar_cedula(?) }";

		try (CallableStatement cstmt = connection.prepareCall(sql)) {
			cstmt.registerOutParameter(1, java.sql.Types.NUMERIC);
			cstmt.setLong(2, cedula);
			cstmt.execute();
			int resultado = cstmt.getInt(1);
			existe = (resultado == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existe;
	}

	public boolean authenticate(Long cedula, String contrasena, String rol) {
		String sql = "{ ? = call PI1SIDS.autenticar_usuario(?, ?, ?) }";

		try (CallableStatement stmt = connection.prepareCall(sql)) {
			// Registramos el parámetro de retorno
			stmt.registerOutParameter(1, java.sql.Types.INTEGER);

			// Seteamos los parámetros de entrada
			stmt.setLong(2, cedula);
			stmt.setString(3, contrasena);
			stmt.setString(4, rol);

			// Ejecutamos la función
			stmt.execute();

			// Obtenemos el resultado
			int resultado = stmt.getInt(1);
			return resultado == 1;
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
