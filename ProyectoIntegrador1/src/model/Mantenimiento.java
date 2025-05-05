package model;

import java.time.LocalDate;

public class Mantenimiento {
	
	private String id;
	private String motivo;
	private String tipo;  //"preventivo", "correctivo"
	private LocalDate fechaInicio;
	private LocalDate fechaFinalPropuesta;
	private long cedUsuario;
	
	public Mantenimiento(String id, String motivo, String tipo, LocalDate fechaInicio, LocalDate fechaFinalPropuesta,
			long cedUsuario) {
		super();
		this.id = id;
		this.motivo = motivo;
		this.tipo = tipo;
		this.fechaInicio = fechaInicio;
		this.fechaFinalPropuesta = fechaFinalPropuesta;
		this.cedUsuario = cedUsuario;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public LocalDate getFechaFinalPropuesta() {
		return fechaFinalPropuesta;
	}
	public void setFechaFinalPropuesta(LocalDate fechaFinalPropuesta) {
		this.fechaFinalPropuesta = fechaFinalPropuesta;
	}
	public long getCedUsuario() {
		return cedUsuario;
	}
	public void setCedUsuario(long cedUsuario) {
		this.cedUsuario = cedUsuario;
	}


}
