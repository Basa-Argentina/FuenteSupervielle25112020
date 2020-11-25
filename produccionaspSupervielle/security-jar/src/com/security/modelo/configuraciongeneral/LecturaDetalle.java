/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/08/2011
 */
package com.security.modelo.configuraciongeneral;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * @author Victor Kenis
 *
 */
@Entity(name="lecturaDetalle")
public class LecturaDetalle implements Comparable<LecturaDetalle>{
	private Long id;
	private Long orden;
	private Lectura lectura;
	private Elemento elemento;
	private String codigoBarras;
	private String observacion;
	private transient String codigoLectura;
	private transient String codigoElemento;

	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getOrden() {
		return orden;
	}

	public void setOrden(Long orden) {
		this.orden = orden;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Lectura getLectura() {
		return lectura;
	}

	public void setLectura(Lectura lectura) {
		this.lectura = lectura;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Elemento getElemento() {
		return elemento;
	}

	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}
	
	@Column(length=13)
	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	@Override
	public int compareTo(LecturaDetalle o) {
		if(o == null)
			return -1;
		
		int cmp = getId().compareTo(o.getId());
		if(cmp != 0) return cmp;
		
		cmp = getOrden().compareTo(o.getOrden());
		if(cmp != 0) return cmp;
		
		cmp = lectura.compareTo(o.getLectura());
		if(cmp != 0) return cmp;
		
		cmp = elemento.getCodigo().compareTo(o.elemento.getCodigo());
		if(cmp != 0) return cmp;
		
		return cmp;		
	}
	
	
	@Transient
	public String getCodigoLectura() {
		return codigoLectura;
	}
	@Transient
	public void setCodigoLectura(String codigoLectura) {
		this.codigoLectura = codigoLectura;
	}
	@Transient
	public String getCodigoElemento() {
		return codigoElemento;
	}
	@Transient
	public void setCodigoElemento(String codigoElemento) {
		this.codigoElemento = codigoElemento;
	}
	
	
}
