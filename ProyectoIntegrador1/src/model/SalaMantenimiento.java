package model;

public class SalaMantenimiento {
	
	private String idMantenimiento;
    private String idSala;
    
    
	public SalaMantenimiento(String idMantenimiento, String idSala) {
		this.idMantenimiento = idMantenimiento;
		this.idSala = idSala;
	}
	
	public String getIdMantenimiento() {
		return idMantenimiento;
	}
	
	public void setIdMantenimiento(String idMantenimiento) {
		this.idMantenimiento = idMantenimiento;
	}
	
	public String getIdSala() {
		return idSala;
	}
	
	public void setIdSala(String idSala) {
		this.idSala = idSala;
	}
 
    
}
