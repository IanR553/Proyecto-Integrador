package model;

public class Reserva {
	
	private int id;
	private boolean estado;
	private String tipo;
	private int cedUsuario;
	private int idHorario;
	
	public Reserva(int id, boolean estado, String tipo, int cedUsuario, int idHorario) {
		super();
		this.id = id;
		this.estado = estado;
		this.tipo = tipo;
		this.cedUsuario = cedUsuario;
		this.idHorario = idHorario;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getCedUsuario() {
		return cedUsuario;
	}
	public void setCedUsuario(int cedUsuario) {
		this.cedUsuario = cedUsuario;
	}
	public int getIdHorario() {
		return idHorario;
	}
	public void setIdHorario(int idHorario) {
		this.idHorario = idHorario;
	}
	
	
}
