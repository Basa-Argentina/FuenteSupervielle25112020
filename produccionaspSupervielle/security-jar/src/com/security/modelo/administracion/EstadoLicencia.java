/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 26/05/2011
 */
package com.security.modelo.administracion;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="estados_licencia")
public class EstadoLicencia implements Comparable<EstadoLicencia>{
	private Long id;
	private String nombre;
	private String descripcion;
	private Boolean asignable;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Boolean getAsignable() {
		return asignable;
	}
	public void setAsignable(Boolean asignable) {
		this.asignable = asignable;
	}
	@Override
	public int compareTo(EstadoLicencia o) {
		int cmp = this.asignable.compareTo(o.getAsignable());
		if(cmp != 0) return cmp;
		
		cmp = this.nombre.compareTo(o.getNombre());
		if(cmp != 0) return cmp;
		
		cmp = this.id.compareTo(o.getId());
		return cmp;
	}	
}
