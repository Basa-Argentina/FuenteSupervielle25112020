/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.configuraciongeneral;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Gonzalo Noriega
 *
 */
@Entity
@Table(name="depositos")
public class Deposito{
	private Long id;
	private Sucursal sucursal;
	private Direccion direccion;
	private String codigo; 
	private Boolean depositoPropio;
	private String descripcion;
	private String observacion;
	private Float subTotal;
	private Float subDisponible;
	private Set<Seccion> secciones;
	private transient String accion;
	private transient Long idBarrio;
	private transient Long idDireccion;	
	private transient Long idSucursal;
	private transient String codigoDeposito;
	private transient String codigoSucursal;
	private transient String codigoEmpresa;
	
	public Deposito(){
		super();
		this.direccion = new Direccion();
		this.sucursal = new Sucursal();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})	
	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	@Column(columnDefinition = "VARCHAR(2)")
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Boolean getDepositoPropio() {
		return depositoPropio;
	}

	public void setDepositoPropio(Boolean depositoPropio) {
		this.depositoPropio = depositoPropio;
	}
	
	@Column(columnDefinition = "VARCHAR(100)")
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Float getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Float subTotal) {
		this.subTotal = subTotal;
	}

	public Float getSubDisponible() {
		return subDisponible;
	}

	public void setSubDisponible(Float subDisponible) {
		this.subDisponible = subDisponible;
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="deposito", fetch=FetchType.LAZY)
	public Set<Seccion> getSecciones() {
		return secciones;
	}

	public void setSecciones(Set<Seccion> secciones) {
		this.secciones = secciones;
	}

	@Transient
	public Long getIdBarrio() {
		return idBarrio;
	}
	@Transient
	public void setIdBarrio(Long idBarrio) {
		this.idBarrio = idBarrio;
	}
	@Transient
	public Long getIdSucursal() {
		return idSucursal;
	}
	@Transient
	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
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
	public Long getIdDireccion() {
		return idDireccion;
	}
	
	@Transient
	public void setIdDireccion(Long idDireccion) {
		this.idDireccion = idDireccion;
	}
	
	@Transient
	public String getCodigoDeposito() {
		return codigoDeposito;
	}

	@Transient
	public void setCodigoDeposito(String codigoDeposito) {
		this.codigoDeposito = codigoDeposito;
	}

	@Transient
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	@Transient
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}
	@Transient
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}
	@Transient
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	
}
