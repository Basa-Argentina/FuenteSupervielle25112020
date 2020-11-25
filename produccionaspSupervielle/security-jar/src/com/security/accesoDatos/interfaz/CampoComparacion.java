package com.security.accesoDatos.interfaz;

public class CampoComparacion{
	private String campo;
	private Object valor;
	public CampoComparacion(String campo,Object valor){
		this.campo=campo;
		this.valor=valor;
	}
	public String getCampo() {
		return campo;
	}
	public Object getValor() {
		return valor;
	}
	
}