package model;

import java.time.LocalTime;
import java.util.ArrayList;

public class Horario {
	
	private int id;
	private int semana;
	private String dia;
	private LocalTime horaInicio;
	private LocalTime horaFin;
	private ArrayList<RequerimientoSalas> tablaRequerimientos;
	
	public Horario(int id, int semana, String dia, LocalTime horaInicio, LocalTime horaFin,
			ArrayList<RequerimientoSalas> tablaRequerimientos) {
		super();
		this.id = id;
		this.semana = semana;
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
		this.tablaRequerimientos = tablaRequerimientos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSemana() {
		return semana;
	}

	public void setSemana(int semana) {
		this.semana = semana;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
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

	public ArrayList<RequerimientoSalas> getTablaRequerimientos() {
		return tablaRequerimientos;
	}

	public void setTablaRequerimientos(ArrayList<RequerimientoSalas> tablaRequerimientos) {
		this.tablaRequerimientos = tablaRequerimientos;
	}

	
}
