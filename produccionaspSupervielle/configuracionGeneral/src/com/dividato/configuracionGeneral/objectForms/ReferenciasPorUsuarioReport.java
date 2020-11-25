/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.dividato.configuracionGeneral.objectForms;

import java.math.BigDecimal;


/**
 * @author Victor Kenis
 *
 */
public class ReferenciasPorUsuarioReport{
	private Integer cantidad;
	private String accion;	
	private BigDecimal codigo;
	private String descripcion;
	

	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	public BigDecimal getCodigo() {
		return codigo;
	}
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
