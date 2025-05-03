package model;

public class ReservaEquipo {
	
    private int idReserva;
    private int idEquipo;


    public ReservaEquipo(int idReserva, int idEquipo) {
        this.idReserva = idReserva;
        this.idEquipo = idEquipo;
    }


    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

}
