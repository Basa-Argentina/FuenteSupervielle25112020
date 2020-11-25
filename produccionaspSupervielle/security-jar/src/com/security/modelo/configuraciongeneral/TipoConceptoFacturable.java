/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/06/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity
@Table(name="tipoconceptofacturable")
public class TipoConceptoFacturable implements Comparable<TipoConceptoFacturable>{
	private Long id;
	private String descripcion;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Override
	public int compareTo(TipoConceptoFacturable o) {
		int cmp = this.id.compareTo(o.getId());
		if(cmp != 0) return cmp;
		
		cmp = this.descripcion.compareTo(o.getDescripcion());
		return cmp;
	}
	
	
}
