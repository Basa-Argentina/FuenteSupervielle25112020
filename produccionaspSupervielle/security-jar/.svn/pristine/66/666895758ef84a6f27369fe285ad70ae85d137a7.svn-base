/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 19/01/2011
 */
package com.security.modelo.general;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="paises")
public class Pais implements Comparable<Pais>{
	private Long id;
	private String nombre;
	private Set<Provincia> provincias;
	
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
	@OneToMany(cascade=CascadeType.ALL, mappedBy="pais", fetch=FetchType.LAZY)
	public Set<Provincia> getProvincias() {
		return provincias;
	}
	public void setProvincias(Set<Provincia> provincias) {
		this.provincias = provincias;
	}
	@Override
	public int compareTo(Pais o) {
		int cmp = this.nombre.compareTo(o.getNombre());
		if(cmp != 0) return cmp;
			
		cmp = this.id.compareTo(o.getId());
		return cmp;
	}
	
}
