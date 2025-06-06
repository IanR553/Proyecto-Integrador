package model;

public class ReservaSala {

    private String idReserva;
    private String tipo;
    private String estado;
    
    private String idSala;
    private String nombreSala;
    private int capacidadSala;
    private String ubicacionSala;
    private String softwareSala;
    
    private String idEquipo;
    private String tipoEquipo;
    private String marcaEquipo;
    private String softwareEquipo;
    
	public ReservaSala(String idReserva, String tipo, String estado, String idSala, String nombreSala,
			int capacidadSala, String ubicacionSala, String softwareSala, String idEquipo, String tipoEquipo,
			String marcaEquipo, String softwareEquipo) {
		super();
		this.idReserva = idReserva;
		this.tipo = tipo;
		this.estado = estado;
		this.idSala = idSala;
		this.nombreSala = nombreSala;
		this.capacidadSala = capacidadSala;
		this.ubicacionSala = ubicacionSala;
		this.softwareSala = softwareSala;
		this.idEquipo = idEquipo;
		this.tipoEquipo = tipoEquipo;
		this.marcaEquipo = marcaEquipo;
		this.softwareEquipo = softwareEquipo;
	}

	public ReservaSala(String idReserva, String tipo, String estado, String idSala, String nombreSala,
			int capacidadSala, String ubicacionSala, String softwareSala) {
		super();
		this.idReserva = idReserva;
		this.tipo = tipo;
		this.estado = estado;
		this.idSala = idSala;
		this.nombreSala = nombreSala;
		this.capacidadSala = capacidadSala;
		this.ubicacionSala = ubicacionSala;
		this.softwareSala = softwareSala;
	}
	
	public ReservaSala(String idReserva, String idSala, String idEquipo) {
	    this.idReserva = idReserva;
	    this.idSala = idSala;
	    this.idEquipo = idEquipo;
	}


	public String getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(String idReserva) {
		this.idReserva = idReserva;
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

	public String getIdSala() {
		return idSala;
	}

	public void setIdSala(String idSala) {
		this.idSala = idSala;
	}

	public String getNombreSala() {
		return nombreSala;
	}

	public void setNombreSala(String nombreSala) {
		this.nombreSala = nombreSala;
	}

	public int getCapacidadSala() {
		return capacidadSala;
	}

	public void setCapacidadSala(int capacidadSala) {
		this.capacidadSala = capacidadSala;
	}

	public String getUbicacionSala() {
		return ubicacionSala;
	}

	public void setUbicacionSala(String ubicacionSala) {
		this.ubicacionSala = ubicacionSala;
	}

	public String getSoftwareSala() {
		return softwareSala;
	}

	public void setSoftwareSala(String softwareSala) {
		this.softwareSala = softwareSala;
	}

	public String getIdEquipo() {
		return idEquipo;
	}

	public void setIdEquipo(String idEquipo) {
		this.idEquipo = idEquipo;
	}

	public String getTipoEquipo() {
		return tipoEquipo;
	}

	public void setTipoEquipo(String tipoEquipo) {
		this.tipoEquipo = tipoEquipo;
	}

	public String getMarcaEquipo() {
		return marcaEquipo;
	}

	public void setMarcaEquipo(String marcaEquipo) {
		this.marcaEquipo = marcaEquipo;
	}

	public String getSoftwareEquipo() {
		return softwareEquipo;
	}

	public void setSoftwareEquipo(String softwareEquipo) {
		this.softwareEquipo = softwareEquipo;
	}


}
