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
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.security.modelo.general.Barrio;

/**
 * @author Gonzalo Noriega
 * 
 */
@Entity(name = "direcciones")
public class Direccion{
	private Long id;
	private Barrio barrio;
	private String calle;
	private String numero;
	private String edificio;
	private String piso;
	private String dpto;
	private Float latitud; // utilizado para la geo-localizacion
	private Float longitud; // utilizado para la geo-localizacion
	private String observaciones;
	private transient String accion;
	private transient Long idBarrio;

	public Direccion() {
		super();
		this.barrio = new Barrio();
		//this.clienteEmp = new ClienteEmp();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(columnDefinition = "VARCHAR(30)")
	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	@Column(columnDefinition = "VARCHAR(6)")
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Column(columnDefinition = "VARCHAR(4)")
	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	@Column(columnDefinition = "VARCHAR(4)")
	public String getDpto() {
		return dpto;
	}

	public void setDpto(String dpto) {
		this.dpto = dpto;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})	
	public Barrio getBarrio() {
		return barrio;
	}

	public void setBarrio(Barrio barrio) {
		this.barrio = barrio;
	}

	@Column(columnDefinition = "VARCHAR(30)")
	public String getEdificio() {
		return edificio;
	}

	public void setEdificio(String edificio) {
		this.edificio = edificio;
	}

	public Float getLatitud() {
		return latitud;
	}

	public void setLatitud(Float latitud) {
		this.latitud = latitud;
	}

	public Float getLongitud() {
		return longitud;
	}

	public void setLongitud(Float longitud) {
		this.longitud = longitud;
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
	public Long getIdBarrio() {
		return idBarrio;
	}

	@Transient
	public void setIdBarrio(Long idBarrio) {
		this.idBarrio = idBarrio;
	}
}
