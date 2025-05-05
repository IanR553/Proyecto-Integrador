package model;

public class Reserva {

    private String id;
    private String estado; // "cancelada", "aprobada", "pendiente"
    private String tipo;
    private long cedUsuario;
    private String idHorario;
    
    
	public Reserva(String id, String estado, String tipo, long cedUsuario, String idHorario) {
		super();
		this.id = id;
		this.estado = estado;
		this.tipo = tipo;
		this.cedUsuario = cedUsuario;
		this.idHorario = idHorario;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public long getCedUsuario() {
		return cedUsuario;
	}


	public void setCedUsuario(long cedUsuario) {
		this.cedUsuario = cedUsuario;
	}


	public String getIdHorario() {
		return idHorario;
	}


	public void setIdHorario(String idHorario) {
		this.idHorario = idHorario;
	}


}

