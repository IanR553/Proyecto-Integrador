package model;

public class SalaMantenimiento {
	
	private int idMantenimiento;
    private int idSala;
    
	public SalaMantenimiento(int idMantenimiento, int idSala) {
		super();
		this.idMantenimiento = idMantenimiento;
		this.idSala = idSala;
	}

	public int getIdMantenimiento() {
		return idMantenimiento;
	}

	public void setIdMantenimiento(int idMantenimiento) {
		this.idMantenimiento = idMantenimiento;
	}

	public int getIdSala() {
		return idSala;
	}

	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}
    
    
}
