/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.general;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.security.modelo.configuraciongeneral.Direccion;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="personas")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Persona{
	private Long id;
	private Direccion direccion;	
	private String observaciones;
	private String telefono;
	private String mail;
	private TipoDocumento tipoDoc;
	private String numeroDoc;
		
	public Persona() {
		super();
		direccion = new Direccion();
	}
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne
	@Cascade({
		org.hibernate.annotations.CascadeType.SAVE_UPDATE,
		org.hibernate.annotations.CascadeType.DELETE_ORPHAN
		})
	public Direccion getDireccion() {
		return direccion;
	}
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
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
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public TipoDocumento getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(TipoDocumento tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public String getNumeroDoc() {
		return numeroDoc;
	}
	public void setNumeroDoc(String numeroDoc) {
		this.numeroDoc = numeroDoc;
	}
	public String toString(){
		if(this instanceof PersonaFisica){
			PersonaFisica pf = (PersonaFisica) this;
			return pf.getApellido()+", "+pf.getNombre();
		}else{
			PersonaJuridica pj = (PersonaJuridica) this;
			return pj.getRazonSocial();
		}
	}
	@Transient
	public String getApellido(){
		if(this instanceof PersonaFisica){
			PersonaFisica pf = (PersonaFisica) this;
			return pf.getApellido();
		}else
			return "";
	}
	@Transient
	public String getNombre(){
		if(this instanceof PersonaFisica){
			PersonaFisica pf = (PersonaFisica) this;
			return pf.getNombre();
		}else
			return "";
	}
	@Transient
	public String getRazonSocial(){
		if(this instanceof PersonaJuridica){
			PersonaJuridica pj = (PersonaJuridica) this;
			return pj.getRazonSocial();
		}else
			return "";
	}	
}
