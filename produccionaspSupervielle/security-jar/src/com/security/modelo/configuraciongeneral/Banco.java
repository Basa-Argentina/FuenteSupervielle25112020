/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.configuraciongeneral;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.text.ParseException;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;


/**
 * @author X
 *
 */
@Entity(name="banco")
public class Banco{
	private Long id;
	private String nombreBanco;
	private Boolean habilitado;
	private transient Long idBanco;
	
	public Banco() {
		super();
	
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreBanco() {
		return nombreBanco;
	}

	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}

	public Boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}
	@Transient
	public Long getIdBanco() {
		return idBanco;
	}
	@Transient
	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}
	
	
}
