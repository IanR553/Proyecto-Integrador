package model;

public class Entidad {
    private String id;
    private boolean estado;
    private String software;

    public Entidad(String id, boolean estado, String software) {
        this.id = id;
        this.estado = estado;
        this.software = software;
    }


    public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}


	public boolean isEstado() {
		return estado;
	}


	public void setEstado(boolean estado) {
		this.estado = estado;
	}


	public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }
}

