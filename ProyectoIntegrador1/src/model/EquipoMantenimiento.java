package model;

public class EquipoMantenimiento {
	
	private String idMantenimiento;
    private String idEquipo;
    
    
	public EquipoMantenimiento(String idMantenimiento, String idEquipo) {
		this.idMantenimiento = idMantenimiento;
		this.idEquipo = idEquipo;
	}
	
	public String getIdMantenimiento() {
		return idMantenimiento;
	}
	
	public void setIdMantenimiento(String idMantenimiento) {
		this.idMantenimiento = idMantenimiento;
	}
	
	public String getIdEquipo() {
		return idEquipo;
	}
	
	public void setIdEquipo(String idEquipo) {
		this.idEquipo = idEquipo;
	}
    
    
}
