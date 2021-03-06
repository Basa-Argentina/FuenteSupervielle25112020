package com.dividato.configuracionGeneral.utils;

import java.util.List;

public class TablaPaginada<T> {
	int tamaņoPagina;
	int numeroPagina;
	int totalPaginas;
	List<T> registros;
	
	public TablaPaginada() {
		super();
	}
	
	public TablaPaginada(int tamaņoPagina, int numeroPagina, int totalPaginas,
			List<T> registros) {
		super();
		this.tamaņoPagina = tamaņoPagina;
		this.numeroPagina = numeroPagina;
		this.totalPaginas = totalPaginas;
		this.registros = registros;
	}


	public int getTamaņoPagina() {
		return tamaņoPagina;
	}
	public void setTamaņoPagina(int tamaņoPagina) {
		this.tamaņoPagina = tamaņoPagina;
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
