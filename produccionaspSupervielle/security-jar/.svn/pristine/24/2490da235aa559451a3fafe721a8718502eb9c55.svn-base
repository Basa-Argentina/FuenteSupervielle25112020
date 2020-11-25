/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/06/2011
 */
package com.security.modelo.configuraciongeneral;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="grupoFacturacion")
public class GrupoFacturacion implements Comparable<GrupoFacturacion>{
	private Long id;
	private AgrupadorFacturacion agrupador;
	private String codigo;
	private String descripcion;
	private String  observacion;
	private Set<GrupoFactDetalle> detalles;
	private transient String accion;
	private transient String codigoAgrupador;
	
	public GrupoFacturacion(){
		super();
		this.agrupador = new AgrupadorFacturacion();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public AgrupadorFacturacion getAgrupador() {
		return agrupador;
	}

	public void setAgrupador(AgrupadorFacturacion agrupador) {
		this.agrupador = agrupador;
	}
	
	@Column(length=4)
	public String getCodigo() {
		if(this.codigo != null){
			return codigo.toUpperCase();
		}
		return null;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo.toUpperCase();
	}
	@Column(length=30)
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
	
	@OneToMany(
			mappedBy="grupoFacturacion", 
			fetch=FetchType.LAZY,
			cascade=CascadeType.ALL,
			orphanRemoval = true)
	public Set<GrupoFactDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(Set<GrupoFactDetalle> detalles) {
		this.detalles = detalles;
	}

	@Transient
	public String getCodigoAgrupador() {
		return codigoAgrupador;
	}
	@Transient
	public void setCodigoAgrupador(String codigoAgrupador) {
		this.codigoAgrupador = codigoAgrupador;
	}
	
	@Transient
	public String getAccion() {
		return accion;
	}
	@Transient
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	@Transient
	public Set<Empleado> getEmpleados() {
		Set<Empleado> empleados = new TreeSet<Empleado>();
		for(GrupoFactDetalle detalle : detalles){
			empleados.add(detalle.getEmpleado());
		}		
		return empleados;
	}
	
	@Transient
	public Set<ClienteDireccion> getDirecciones() {
		Set<ClienteDireccion> clienteDirecciones = new TreeSet<ClienteDireccion>();
		for(GrupoFactDetalle detalle : detalles){
			clienteDirecciones.add(detalle.getDireccionEntrega());
		}		
		return clienteDirecciones;
	}
	
	@Override
	public int compareTo(GrupoFacturacion o) {
		int cmp = this.id.compareTo(o.getId());
		if(cmp != 0) return cmp;
		return cmp;
	}
}
