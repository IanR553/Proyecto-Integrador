package model;

import java.time.LocalTime;

public class RequerimientoSalas {
	
	private String id;
    private String idHorario;      
    private String dia;
    private int semana;
    private String idSala;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean estado;


    public RequerimientoSalas(String id, String idHorario, String dia, int semana, String idSala, LocalTime horaInicio,
			LocalTime horaFin, boolean estado) {
		super();
		this.id = id;
		this.idHorario = idHorario;
		this.dia = dia;
		this.semana = semana;
		this.idSala = idSala;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.estado = estado;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getIdHorario() {
		return idHorario;
	}


	public void setIdHorario(String idHorario) {
		this.idHorario = idHorario;
	}


	public String getDia() {
		return dia;
	}


	public void setDia(String dia) {
		this.dia = dia;
	}


	public int getSemana() {
		return semana;
	}


	public void setSemana(int semana) {
		this.semana = semana;
	}


	public String getIdSala() {
		return idSala;
	}


	public void setIdSala(String idSala) {
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

