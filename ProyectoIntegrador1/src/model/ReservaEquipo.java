package model;

public class ReservaEquipo {
	
    private String idReserva;
    private String idEquipo;
    
    
	public ReservaEquipo(String idReserva, String idEquipo) {
		this.idReserva = idReserva;
		this.idEquipo = idEquipo;
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
