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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author Ezequiel Beccaria
 * 
 */
@Entity(name = "provincias")
public class Provincia implements Comparable<Provincia> {
	private Long id;
	private String nombre;
	private Set<Localidad> localidades;
	private Pais pais;

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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "provincia", fetch=FetchType.LAZY)
	public Set<Localidad> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(Set<Localidad> localidades) {
		this.localidades = localidades;
	}

	@ManyToOne
	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	@Override
	public int compareTo(Provincia prov) {
		int comp;
		if (this.pais != null) {
			comp = this.pais.compareTo(prov.getPais());
			if (comp != 0)
				return comp;
		}
		return this.nombre.compareTo(prov.getNombre());
	}


}
