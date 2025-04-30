package model;

public class Sala {
	
	private int id;
	private String nombre;
	private int capacidad;
	private boolean estado;
	private String ubicacion;
	private String software;
	private int idEquipo;
	
	public Sala(int id, String nombre, int capacidad, boolean estado, String ubicacion, String software, int idEquipo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.capacidad = capacidad;
		this.estado = estado;
		this.ubicacion = ubicacion;
		this.software = software;
		this.idEquipo = idEquipo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getSoftware() {
		return software;
	}

	public void setSoftware(String software) {
		this.software = software;
	}

	public int getIdEquipo() {
		return idEquipo;
	}

	public void setIdEquipo(int idEquipo) {
		this.idEquipo = idEquipo;
	}
	
	
	
}
