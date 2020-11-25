/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/09/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * @author Victor Kenis
 *
 */
@Entity(name="remitosDetalle")
public class RemitoDetalle implements Comparable<RemitoDetalle>{
	private Long id;
	private Remito remito;
	private Elemento elemento;
	private Long orden;
	private transient String codigoElemento;
	private transient String referencia;

		
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
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Remito getRemito() {
		return remito;
	}

	public void setRemito(Remito remito) {
		this.remito = remito;
	}

	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Elemento getElemento() {
		return elemento;
	}

	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}
	
	@Override
	public int compareTo(RemitoDetalle o) {
		if(o == null)
			return -1;
		
		int cmp = getId().compareTo(o.getId());
		if(cmp != 0) return cmp;
		
		cmp = getOrden().compareTo(o.getOrden());
		if(cmp != 0) return cmp;
		
		cmp = remito.compareTo(o.getRemito());
		if(cmp != 0) return cmp;
		
		cmp = elemento.getCodigo().compareTo(o.elemento.getCodigo());
		if(cmp != 0) return cmp;
		
		return cmp;		
	}
	
	@Transient
	public String getCodigoElemento() {
		return codigoElemento;
	}
	@Transient
	public void setCodigoElemento(String codigoElemento) {
		this.codigoElemento = codigoElemento;
	}
	@Transient
	public String getReferencia() {
		return referencia;
	}
	@Transient
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
}
