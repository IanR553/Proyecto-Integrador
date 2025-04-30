package model;

public class Usuario {

	private int cedula;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private String correoElectronico;
	private int celular;
	private int idRol;
	
	
	public Usuario(int cedula, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
			String correoElectronico, int celular, int idRol) {
		super();
		this.cedula = cedula;
		this.primerNombre = primerNombre;
		this.segundoNombre = segundoNombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.correoElectronico = correoElectronico;
		this.celular = celular;
		this.idRol = idRol;
	}
	public int getCedula() {
		return cedula;
	}
	public void setCedula(int cedula) {
		this.cedula = cedula;
	}
	public String getPrimerNombre() {
		return primerNombre;
	}
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	public String getSegundoNombre() {
		return segundoNombre;
	}
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	public String getCorreoElectronico() {
		return correoElectronico;
	}
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}
	public int getCelular() {
		return celular;
	}
	public void setCelular(int celular) {
		this.celular = celular;
	}
	public int getidRol() {
		return idRol;
	}
	public void setRol(int idRol) {
		this.idRol = idRol;
	}
	
	
}
