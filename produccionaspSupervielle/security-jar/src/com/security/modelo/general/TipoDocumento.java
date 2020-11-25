/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/05/2011
 */
package com.security.modelo.general;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.security.utils.HashCodeUtil;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="tipos_documento")
public class TipoDocumento {
	private Long id;
	private String nombre;
	private String codigo;
	private String descripcion;
	
	@Id
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
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TipoDocumento tipo = (TipoDocumento) o;		
		if (getId() != null ? !getId().equals(tipo.getId()) : tipo.getId() != null) 
			return false;
		if (getNombre() != null ? !getNombre().equals(tipo.getNombre()) :	tipo.getNombre() != null) 
			return false;
		if (getCodigo() != null ? !getCodigo().equals(tipo.getCodigo()) : tipo.getCodigo() != null) 
			return false;
		if (getDescripcion() != null ? !getDescripcion().equals(tipo.getDescripcion()) : tipo.getDescripcion() != null) 
			return false;
		return true;
	}	
	
	public int hashCode(){
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, this.id);
		result = HashCodeUtil.hash(result, this.nombre);
		result = HashCodeUtil.hash(result, this.codigo);
		result = HashCodeUtil.hash(result, this.descripcion);
		return result;
	}
	
}
