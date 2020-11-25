package com.security.modelo.configuraciongeneral;

/**
 * 
 * @author Gabriel Mainero
 * 
 */
public class Mes {
	private Integer mes;
	private boolean seleccionado;
	
	public Mes(Integer mes, boolean seleccionado){
		this.mes = mes;
		this.seleccionado = seleccionado;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}
}
