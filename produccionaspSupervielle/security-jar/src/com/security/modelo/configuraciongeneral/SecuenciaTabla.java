/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/05/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.security.modelo.administracion.ClienteAsp;

/**
 * @author Victor Kenis
 *
 */
@Entity(name="secuenciasTablas")
public class SecuenciaTabla {
	
	private Long id;
	private Long nroSecuencia;
	private String nombreTabla;
	private ClienteAsp clienteAsp;
	
	@Id@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNroSecuencia() {
		return nroSecuencia;
	}
	public void setNroSecuencia(Long nroSecuencia) {
		this.nroSecuencia = nroSecuencia;
	}
	public String getNombreTabla() {
		return nombreTabla;
	}
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	
	
	
}
