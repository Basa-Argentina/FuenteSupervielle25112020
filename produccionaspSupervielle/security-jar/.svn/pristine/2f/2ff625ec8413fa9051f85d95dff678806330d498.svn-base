/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/06/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;

/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="agrupadorFacturacion")
public class AgrupadorFacturacion {
	private Long id;
	private String codigo;
	private String descripcion;
	private String tipoAgrupador; //E-Empleados, D-Direcciones (Lugares de entrega)
	private String  observacion;
	private Boolean habilitado;
	private ClienteAsp clienteAsp;
	private ClienteEmp clienteEmp;
	private transient String clienteCodigo;
	private transient String accion;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(length=4)
	public String getCodigo() {
		return codigo.toUpperCase();
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo.toUpperCase();
	}
	@Column(length=30)
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Column(length=1)
	public String getTipoAgrupador() {
		return tipoAgrupador;
	}

	public void setTipoAgrupador(String tipoAgrupador) {
		this.tipoAgrupador = tipoAgrupador;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public Boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}

	@ManyToOne(cascade={CascadeType.MERGE}, fetch=FetchType.LAZY)
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE}, fetch=FetchType.LAZY)
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}

	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}

	@Transient
	public String getAccion() {
		return accion;
	}
	@Transient
	public void setAccion(String accion) {
		this.accion = accion;
	}
	@Transient
	public String getClienteCodigo() {
		return clienteCodigo;
	}
	@Transient
	public void setClienteCodigo(String clienteCodigo) {
		this.clienteCodigo = clienteCodigo;
	}
	
	
	
}
