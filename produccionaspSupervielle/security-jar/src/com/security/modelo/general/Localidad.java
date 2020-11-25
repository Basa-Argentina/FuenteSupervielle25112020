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
@Entity(name="localidades")
public class Localidad implements Comparable<Localidad>{
	private Long id;
	private String nombre;
	private String codigoPostal;
	private String codigoArea;
	private Provincia provincia;
	private Set<Barrio> barrios;
	
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
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getCodigoArea() {
		return codigoArea;
	}
	public void setCodigoArea(String codigoArea) {
		this.codigoArea = codigoArea;
	}
	@ManyToOne
	public Provincia getProvincia() {
		return provincia;
	}
	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "localidad", fetch=FetchType.LAZY)
	public Set<Barrio> getBarrios() {
		return barrios;
	}
	public void setBarrios(Set<Barrio> barrios) {
		this.barrios = barrios;
	}
	@Override
	public int compareTo(Localidad o) {
		int comp;
		if (this.provincia != null) {
			comp = this.provincia.compareTo(o.getProvincia());
			if (comp != 0)
				return comp;
		}
		return this.nombre.compareTo(o.getNombre());
	}	
}
