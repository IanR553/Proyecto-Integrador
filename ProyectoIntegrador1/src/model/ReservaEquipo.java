package model;

public class ReservaEquipo {
	
    private String idReserva;
    private String tipo;
    private String estado;
    private String idEquipo;
    private String tipoEquipo;
    private String marca;
    private String software;
    
	public ReservaEquipo(String idReserva, String idEquipo) {
		this.idReserva = idReserva;
		this.idEquipo = idEquipo;
	}
	
	public ReservaEquipo(String idReserva, String tipo, String estado, String idEquipo, String tipoEquipo, String marca,
			String software) {
		this.idReserva = idReserva;
		this.tipo = tipo;
		this.estado = estado;
		this.idEquipo = idEquipo;
		this.tipoEquipo = tipoEquipo;
		this.marca = marca;
		this.software = software;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTipoEquipo() {
		return tipoEquipo;
	}

	public void setTipoEquipo(String tipoEquipo) {
		this.tipoEquipo = tipoEquipo;
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
	
	public String getIdReserva() {
		return idReserva;
	}
	
	public void setIdReserva(String idReserva) {
		this.idReserva = idReserva;
	}
	
	public String getIdEquipo() {
		return idEquipo;
	}
	
	public void setIdEquipo(String idEquipo) {
		this.idEquipo = idEquipo;
	}

}
