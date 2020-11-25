/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.modelo.configuraciongeneral;

import com.security.modelo.administracion.ClienteAsp;

/**
 * @author Gonzalo Noriega
 *
 */
public class StockResumen {
	private ConceptoFacturable concepto;
	private Integer cantidad;	
	private Deposito deposito;
	private ClienteAsp cliente;
	private transient String accion;
	
	public StockResumen(){
		super();
	}
	
	public ConceptoFacturable getConcepto() {
		return concepto;
	}

	public void setConcepto(ConceptoFacturable concepto) {
		this.concepto = concepto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Deposito getDeposito() {
		return deposito;
	}

	
	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}
	
	
	public ClienteAsp getCliente() {
		return cliente;
	}

	public void setCliente(ClienteAsp cliente) {
		this.cliente = cliente;
	}

	public String getAccion() {
		return accion;
	}
	
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
}
