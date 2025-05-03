package model;

public class EquipoMantenimiento {
	
	private int idMantenimiento;
    private int idEquipo;
    
	public EquipoMantenimiento(int idMantenimiento, int idEquipo) {
		super();
		this.idMantenimiento = idMantenimiento;
		this.idEquipo = idEquipo;
	}
	public int getIdMantenimiento() {
		return idMantenimiento;
	}
	public void setIdMantenimiento(int idMantenimiento) {
		this.idMantenimiento = idMantenimiento;
	}
	public int getIdEquipo() {
		return idEquipo;
	}
	public void setIdEquipo(int idEquipo) {
		this.idEquipo = idEquipo;
	}
    
    
}
