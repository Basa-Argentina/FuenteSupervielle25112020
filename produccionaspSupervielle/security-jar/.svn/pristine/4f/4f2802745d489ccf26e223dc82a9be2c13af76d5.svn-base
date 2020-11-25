/*
+ * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.security.modelo.administracion;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="licencias")
public class Licencia implements Comparable<Licencia>{
	private Long id;
	private ClienteAsp cliente;
	private Date fechaDesde;
	private Date fechaHasta;
	private EstadoLicencia estado;
	private transient String accion;
	private transient Long idCliente;
	private transient Long estadoId;

	
	public Licencia() {
		super();
	}
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public ClienteAsp getCliente() {
		return cliente;
	}
	public void setCliente(ClienteAsp cliente) {
		this.cliente = cliente;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
	public EstadoLicencia getEstado() {
		return estado;
	}
	public void setEstado(EstadoLicencia estado) {
		this.estado = estado;
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
	public Long getIdCliente() {
		return idCliente;
	}
	@Transient
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	@Transient
	public String getFechaDesdeStr(){
		if(fechaDesde==null)
			return "";
		return formatoFechaFormularios.format(fechaDesde);
	}
	@Transient
	public String getFechaHastaStr(){
		if(fechaHasta==null)
			return "";
		return formatoFechaFormularios.format(fechaHasta);
	}
	@Transient
	public Long getEstadoId() {
		return estadoId;
	}
	public void setEstadoId(Long estadoId) {
		this.estadoId = estadoId;
	}
	@Override
	public int compareTo(Licencia o) {
		int cmp = this.getCliente().getClienteStr().compareTo(o.getCliente().getClienteStr());
		if(cmp != 0) return cmp;
		cmp = this.getFechaDesde().compareTo(o.getFechaDesde());
		if(cmp != 0) return cmp;
		cmp = this.getFechaHasta().compareTo(o.getFechaHasta());
		if(cmp != 0) return cmp;
		cmp = this.getEstado().compareTo(o.getEstado());
		return cmp;
	}
}
