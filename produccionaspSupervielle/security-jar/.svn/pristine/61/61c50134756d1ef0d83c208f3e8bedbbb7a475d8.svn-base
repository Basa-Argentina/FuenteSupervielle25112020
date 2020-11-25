/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/06/2011
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
import javax.persistence.Transient;


/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="grupos")
public class Grupo{
	private Long id;
	private Seccion seccion;
	private String codigo;
	private String descripcion;
	private Integer verticales; 
	private Integer horizontales;
	private Integer modulosVert; //Debe ser un numero que sea divendo del campo Verticales.
	private Integer modulosHor; //Debe ser un numero que sea divendo del campo Horizontales.
	private Set<Estante> estantes;
	private transient String accion; 
	private transient Long idSeccion;
	private transient String codigoDeposito;
	private transient String codigoSucursal;
	private transient String codigoEmpresa;
	private transient String codigoSeccion;
	
	public Grupo() {
		super();
		this.seccion = new Seccion();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(columnDefinition = "VARCHAR(2)")
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	@Column(columnDefinition = "VARCHAR(100)")
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Seccion getSeccion() {
		return seccion;
	}

	public void setSeccion(Seccion seccion) {
		this.seccion = seccion;
	}
	
	public Integer getVerticales() {
		return verticales;
	}

	public void setVerticales(Integer verticales) {
		this.verticales = verticales;
	}

	public Integer getHorizontales() {
		return horizontales;
	}

	public void setHorizontales(Integer horizontales) {
		this.horizontales = horizontales;
	}

	public Integer getModulosVert() {
		return modulosVert;
	}

	public void setModulosVert(Integer modulosVert) {
		this.modulosVert = modulosVert;
	}

	public Integer getModulosHor() {
		return modulosHor;
	}

	public void setModulosHor(Integer modulosHor) {
		this.modulosHor = modulosHor;
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="grupo", fetch=FetchType.LAZY)
	public Set<Estante> getEstantes() {
		return estantes;
	}

	public void setEstantes(Set<Estante> estantes) {
		this.estantes = estantes;
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
	public Long getIdSeccion() {
		return idSeccion;
	}
	@Transient
	public void setIdSeccion(Long idSeccion) {
		this.idSeccion = idSeccion;
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
	@Transient
	public String getCodigoSeccion() {
		return codigoSeccion;
	}
	@Transient
	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}
	
}
