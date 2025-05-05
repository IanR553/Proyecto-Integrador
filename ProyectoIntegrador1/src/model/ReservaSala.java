package model;

public class ReservaSala {

    private String idReserva;
    private String idSala;
    private String idEquipo;

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

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }
}
