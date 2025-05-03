package model;

public class Equipo extends Entidad {

    private String tipo;
    private String marca;

    public Equipo(int id, String tipo, boolean estado, String marca, String software) {
        super(id, estado, software); 
        this.tipo = tipo;
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
}

