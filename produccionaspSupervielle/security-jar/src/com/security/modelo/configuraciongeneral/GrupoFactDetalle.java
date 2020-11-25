/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/06/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="grupoFactDetalle")
public class GrupoFactDetalle implements Comparable<GrupoFactDetalle>{
	private Long id;
	private GrupoFacturacion grupoFacturacion;
	private ClienteDireccion direccionEntrega;
	private Empleado empleado;
	private transient Long idEmpleado;
	private transient Long idDireccion;
	private transient String codigoGrupoFac;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public GrupoFacturacion getGrupoFacturacion() {
		return grupoFacturacion;
	}

	public void setGrupoFacturacion(GrupoFacturacion grupoFacturacion) {
		this.grupoFacturacion = grupoFacturacion;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteDireccion getDireccionEntrega() {
		return direccionEntrega;
	}

	public void setDireccionEntrega(ClienteDireccion direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
	@Transient
	public Long getIdEmpleado() {
		return idEmpleado;
	}
	@Transient
	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}
	@Transient
	public Long getIdDireccion() {
		return idDireccion;
	}
	@Transient
	public void setIdDireccion(Long idDireccion) {
		this.idDireccion = idDireccion;
	}
	@Transient
	public String getCodigoGrupoFac() {
		return codigoGrupoFac;
	}
	@Transient
	public void setCodigoGrupoFac(String codigoGrupoFac) {
		this.codigoGrupoFac = codigoGrupoFac;
	}
	
	@Override
	public int compareTo(GrupoFactDetalle o) {
		if(o == null)
			return -1;
		
		int cmp = getId().compareTo(o.getId());
		if(cmp != 0) return cmp;
		
		cmp = grupoFacturacion.compareTo(o.getGrupoFacturacion());
		if(cmp != 0) return cmp;
		
		cmp = direccionEntrega.compareTo(o.getDireccionEntrega());
		if(cmp != 0) return cmp;
		
		cmp = empleado.compareTo(o.getEmpleado());
		if(cmp != 0) return cmp;
		
		return cmp;		
	}
	
}
