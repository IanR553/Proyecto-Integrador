package model;

public class ReservaSala {
	
    private int idReserva;
    private int idSala;
    private int idEquipo;
    
	public ReservaSala(int idReserva, int idSala, int idEquipo) {
		super();
		this.idReserva = idReserva;
		this.idSala = idSala;
		this.idEquipo = idEquipo;
	}

	public int getIdReserva() {
		return idReserva;
	}

	public void setIdReserva(int idReserva) {
		this.idReserva = idReserva;
	}

	public int getIdSala() {
		return idSala;
	}

	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}

	public int getIdEquipo() {
		return idEquipo;
	}

	public void setIdEquipo(int idEquipo) {
		this.idEquipo = idEquipo;
	}
    
}
