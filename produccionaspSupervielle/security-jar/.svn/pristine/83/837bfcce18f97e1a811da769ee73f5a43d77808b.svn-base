/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 07/07/2011
 */
package com.security.modelo.jerarquias;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity
@Table(name="tipos_jerarquia")
public class TipoJerarquia implements Comparable<TipoJerarquia>{
	private Long id;
	private String codigo;
	private String descripcion;
	private String observacion;
	private ClienteAsp clienteAsp;
	private Set<Jerarquia> jerarquias;
	private Date fechaRegistro;
	private Date fechaActualizacion;
	private String modifico;
	private transient String accion;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
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
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	@OneToMany(
			mappedBy="tipo", 
			fetch=FetchType.LAZY, 
			cascade=CascadeType.ALL)
	public Set<Jerarquia> getJerarquias() {
		return jerarquias;
	}
	public void setJerarquias(Set<Jerarquia> jerarquias) {
		this.jerarquias = jerarquias;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	@Transient
	public String getFechaRegistroStr() {
		if(fechaRegistro==null)
			return "";
		return formatoFechaFormularios.format(fechaRegistro);
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}
	@Transient
	public String getFechaActualizacionStr() {
		if(fechaActualizacion==null)
			return "";
		return formatoFechaFormularios.format(fechaActualizacion);
	}
	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}	
	public String getModifico() {
		return modifico;
	}
	public void setModifico(String modifico) {
		this.modifico = modifico;
	}
	@Transient
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	@Override
	public int compareTo(TipoJerarquia o) {		
				
		int cmp = getCodigo().compareTo(o.getCodigo());
		if(cmp != 0) return cmp;
		
		cmp = getDescripcion().compareTo(o.getDescripcion());
		if(cmp != 0) return cmp;
		
		cmp = getObservacion().compareTo(o.getObservacion());
		if(cmp != 0) return cmp;
		
		cmp = getId().compareTo(o.getId());
		if(cmp != 0) return cmp;
		return 0;
	}
	
}
