/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.general;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.utils.HashCodeUtil;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="personas_fisicas")
public class PersonaFisica extends Persona implements Serializable{
	private static final long serialVersionUID = -3824777048095315931L;
	private String nombre;
	private String apellido;
	private Empresa empresaDefecto;
	private Sucursal sucursalDefecto;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Empresa getEmpresaDefecto() {
		return empresaDefecto;
	}
	public void setEmpresaDefecto(Empresa empresaDefecto) {
		this.empresaDefecto = empresaDefecto;
	}
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public Sucursal getSucursalDefecto() {
		return sucursalDefecto;
	}
	public void setSucursalDefecto(Sucursal sucursalDefecto) {
		this.sucursalDefecto = sucursalDefecto;
	}
	
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Persona persona = (Persona) o;
		if (getId() != null ? !getId().equals(persona.getId()) : persona.getId() != null) 
			return false;
		if (this.getNumeroDoc() != null ? !this.getNumeroDoc().equals(persona.getNumeroDoc()) :	persona.getNumeroDoc() != null) 
			return false;
		if (this.getTipoDoc() != null ? !this.getTipoDoc().equals(persona.getTipoDoc()) : persona.getTipoDoc() != null) 
			return false;
		if (getNombre() != null ? !getNombre().equals(persona.getNombre()) : persona.getNombre() != null) 
			return false;
		if (getApellido() != null ? !getApellido().equals(persona.getApellido()) : persona.getApellido() != null) 
			return false;
		return true;
	}	
	
	public int hashCode(){
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, this.getId());
		result = HashCodeUtil.hash(result, this.getNumeroDoc());
		result = HashCodeUtil.hash(result, this.getTipoDoc());
		result = HashCodeUtil.hash(result, this.nombre);
		result = HashCodeUtil.hash(result, this.apellido);
		return result;
	}
}
