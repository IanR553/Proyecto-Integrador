package model;

import java.time.LocalTime;

public class RequerimientoSalas {
    private String dia;
    private int idSala;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean estado;
    
	public RequerimientoSalas(String dia, int idSala, LocalTime horaInicio, LocalTime horaFin, boolean estado) {
		super();
		this.dia = dia;
		this.idSala = idSala;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.estado = estado;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	public int getIdSala() {
		return idSala;
	}

	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}

	public LocalTime getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(LocalTime horaInicio) {
		this.horaInicio = horaInicio;
	}

	public LocalTime getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(LocalTime horaFin) {
		this.horaFin = horaFin;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}


}

