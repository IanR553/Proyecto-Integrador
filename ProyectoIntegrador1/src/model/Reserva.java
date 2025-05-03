package model;

public class Reserva {

    private int id;
    private String estado; // "cancelada", "aprobada", "pendiente"
    private String tipo;
    private Usuario usuario;
    private Horario horario;

    public Reserva(int id, String estado, String tipo, Usuario usuario, Horario horario) {
        this.id = id;
        this.estado = estado;
        this.tipo = tipo;
        this.usuario = usuario;
        this.horario = horario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }
}

