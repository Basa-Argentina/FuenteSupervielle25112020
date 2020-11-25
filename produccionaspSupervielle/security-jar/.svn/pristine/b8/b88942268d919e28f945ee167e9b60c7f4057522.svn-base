/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.administracion;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.modelo.general.Persona;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.general.PersonaJuridica;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity
@Table(name="clientesAsp")
public class ClienteAsp implements Serializable, Comparable<ClienteAsp>{
	private static final long serialVersionUID = -5551434511385773169L;
	private Long id;
	private Persona persona;
	private Persona contacto;
	private String nombreAbreviado;
	private Boolean habilitado;
	private String observaciones;
	private transient String accion;
	private transient User user; //usuario temporal para el contacto del cliente
	private transient Long idTipoDocSel;
	
	public ClienteAsp(){
		user = new User();
		persona = new PersonaJuridica();
		contacto = new PersonaFisica();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@OneToOne(cascade={CascadeType.ALL})
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	@OneToOne(cascade={CascadeType.ALL})
	public Persona getContacto() {
		return contacto;
	}
	public void setContacto(Persona contacto) {
		this.contacto = contacto;
	}
	@Transient
	public String getClienteStr(){
		return persona.toString();
	}
	@Transient
	public String getContactoStr(){
		return contacto.toString();
	}
	public String getNombreAbreviado() {
		return nombreAbreviado;
	}
	public void setNombreAbreviado(String nombreAbreviado) {
		this.nombreAbreviado = nombreAbreviado;
	}
	public Boolean getHabilitado() {
		return habilitado;
	}
	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}	
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
	public User getUser() {
		return user;
	}
	@Transient
	public void setUser(User user) {
		this.user = user;
	}
	@Transient
	public Long getIdTipoDocSel() {
		return idTipoDocSel;
	}
	@Transient
	public void setIdTipoDocSel(Long idTipoDocSel) {
		this.idTipoDocSel = idTipoDocSel;
	}
	@Override
	public int compareTo(ClienteAsp o) {
		int cmp = getId().compareTo(o.getId());
		return cmp;
		//TODO completar compareTo()
	}	
}
