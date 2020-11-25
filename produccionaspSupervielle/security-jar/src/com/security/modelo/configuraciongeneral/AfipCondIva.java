/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/05/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.security.utils.HashCodeUtil;

/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="afip_condiva")
public class AfipCondIva {
	private Long id;
	private String codigo;
	private String descripcion;
	private String abreviatura;
	
	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	
	public String getAbreviatura() {
		return abreviatura;
	}
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AfipCondIva tipo = (AfipCondIva) o;		
		if (getId() != null ? !getId().equals(tipo.getId()) : tipo.getId() != null) 
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
		result = HashCodeUtil.hash(result, this.codigo);
		result = HashCodeUtil.hash(result, this.descripcion);
		return result;
	}
	
}
