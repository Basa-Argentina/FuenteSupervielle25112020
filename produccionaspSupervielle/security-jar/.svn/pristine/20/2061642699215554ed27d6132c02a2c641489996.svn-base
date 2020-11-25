/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 23/05/2011
 */
package com.security.modelo.general;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="Barrios")
public class Barrio implements Comparable<Barrio>{
	private Long id;
	private String nombre;
	private Localidad localidad;
	
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
	@ManyToOne
	public Localidad getLocalidad() {
		return localidad;
	}
	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}
	@Override
	public int compareTo(Barrio o) {
		int comp;
		if (this.localidad != null) {
			comp = this.localidad.compareTo(o.getLocalidad());
			if (comp != 0)
				return comp;
		}
		return this.nombre.compareTo(o.getNombre());
	}
	
}
