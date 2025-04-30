package model;

public class Equipo {
	
	private int id;
	private String tipo;
	private boolean estado;
	private String marca;
	private String software;
	
	public Equipo(int id, String tipo, boolean estado, String marca, String software) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.estado = estado;
		this.marca = marca;
		this.software = software;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getSoftware() {
		return software;
	}

	public void setSoftware(String software) {
		this.software = software;
	}
	
	
}
