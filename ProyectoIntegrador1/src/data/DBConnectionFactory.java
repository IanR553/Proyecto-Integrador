package data;

public class DBConnectionFactory {

	public static DBConnection getConnectionByRole(String role) {

		switch (role.toLowerCase()) {

		case "manager":

			return ManagerConnection.getInstance();

		case "profesor":

			return ProfesorConnection.getInstance();

		case "administrativo":

			return AdministrativoConnection.getInstance();
			
		case "estudiante":

			return EstudianteConnection.getInstance();

		default:

			throw new IllegalArgumentException("Rol no válido: " + role);
		}
	}

}