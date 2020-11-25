/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/05/2011
 */
package com.security.modelo.configuraciongeneral;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.security.utils.HashCodeUtil;

/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="afip_tipocomprobante")
public class AfipTipoComprobante {
	private Long id;
	private String codigo;
	private String descripcion;
	private String letra;
	private String tipo; //(FF = Factura, RR = Remito, DI = Documento Interno)
	private transient List<String> codigos;
	
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
	
	@Column(length=1)
	public String getLetra() {
		return letra;
	}
	public void setLetra(String letra) {
		this.letra = letra;
	}
	@Column(length=2)
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	@Transient
	public List<String> getCodigos() {
		return codigos;
	}
	@Transient
	public void setCodigos(List<String> codigos) {
		this.codigos = codigos;
	}
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AfipTipoComprobante tipo = (AfipTipoComprobante) o;		
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
