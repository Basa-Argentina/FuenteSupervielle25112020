/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.general;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.security.utils.HashCodeUtil;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="personas_juridicas")
public class PersonaJuridica extends Persona implements Serializable{
	private static final long serialVersionUID = 2004019294584606152L;
	private String razonSocial;

	public String getRazonSocial() {
		return razonSocial;
	}

	@Transient
	public String getRazonSocialStr() {
		if(razonSocial==null)
			return "";
		return razonSocial;
	}
	
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Persona persona = (Persona) o;
		if (getId() != null ? !getId().equals(persona.getId()) : persona.getId() != null) 
			return false;
		if (getNumeroDoc() != null ? !getNumeroDoc().equals(persona.getNumeroDoc()) : persona.getNumeroDoc() != null) 
			return false;
		if (getTipoDoc() != null ? !getTipoDoc().equals(persona.getTipoDoc()) : persona.getTipoDoc() != null) 
			return false;
		if (getRazonSocial() != null ? !getRazonSocial().equals(persona.getRazonSocial()) : persona.getRazonSocial() != null) 
			return false;		
		return true;
	}	
	
	public int hashCode(){
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, this.getId());
		result = HashCodeUtil.hash(result, this.getNumeroDoc());
		result = HashCodeUtil.hash(result, this.getTipoDoc());
		result = HashCodeUtil.hash(result, this.razonSocial);
		return result;
	}
}
