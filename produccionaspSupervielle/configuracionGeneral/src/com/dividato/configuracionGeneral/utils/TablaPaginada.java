package com.dividato.configuracionGeneral.utils;

import java.util.List;

public class TablaPaginada<T> {
	int tamañoPagina;
	int numeroPagina;
	int totalPaginas;
	List<T> registros;
	
	public TablaPaginada() {
		super();
	}
	
	public TablaPaginada(int tamañoPagina, int numeroPagina, int totalPaginas,
			List<T> registros) {
		super();
		this.tamañoPagina = tamañoPagina;
		this.numeroPagina = numeroPagina;
		this.totalPaginas = totalPaginas;
		this.registros = registros;
	}


	public int getTamañoPagina() {
		return tamañoPagina;
	}
	public void setTamañoPagina(int tamañoPagina) {
		this.tamañoPagina = tamañoPagina;
	}
	public int getNumeroPagina() {
		return numeroPagina;
	}
	public void setNumeroPagina(int numeroPagina) {
		this.numeroPagina = numeroPagina;
	}
	public int getTotalPaginas() {
		return totalPaginas;
	}
	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}
	public List<T> getRegistros() {
		return registros;
	}
	public void setRegistros(List<T> registros) {
		this.registros = registros;
	}
	
}
