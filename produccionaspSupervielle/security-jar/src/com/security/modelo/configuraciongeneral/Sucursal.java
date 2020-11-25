/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * @author Gonzalo Noriega
 *
 */
@Entity
@Table(name="sucursales")
public class Sucursal{
	private Long id;
	private Empresa empresa;
	private Direccion direccion;
	private String codigo;
	private String descripcion;
	private Boolean principal;	
	private String telefono;
	private String mail;
	private transient String accion;
	private transient Long idEmpresa;
	private transient Long idDireccion;
	private transient Long idBarrio;
	
	public Sucursal() {
		super();
		this.empresa= new Empresa();
		this.direccion = new Direccion();
		this.principal = false;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(columnDefinition = "VARCHAR(20)")
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	@Column(columnDefinition = "VARCHAR(4)")
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Boolean getPrincipal() {
		return principal;
	}
	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Direccion getDireccion() {
		return direccion;
	}
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	
	@Transient
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
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
	public Long getIdBarrio() {
		return idBarrio;
	}
	@Transient
	public void setIdBarrio(Long idBarrio) {
		this.idBarrio = idBarrio;
	}
	
}
