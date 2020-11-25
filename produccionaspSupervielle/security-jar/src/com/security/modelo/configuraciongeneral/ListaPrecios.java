/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
 */
package com.security.modelo.configuraciongeneral;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;

/**
 * @author Ezequiel Beccaria
 * @modificado Victor Kenis (15/08/2011)
 *
 */

@Entity
@Table(name="lista_precios")
public class ListaPrecios implements Serializable, Comparable<ListaPrecios>{
	private static final long serialVersionUID = 7153126444683352083L;
	
	private Long id;
	private Set<ClienteEmp> clientesEmp;
	private String codigo;
	private String descripcion;
	private TipoVariacion tipoVariacion;
	private BigDecimal valor;
	private Boolean listaFija;
	private Boolean habilitada;
	private Boolean usaVencimiento;
	private ClienteAsp clienteAsp;
	private Date fechaRegistro;
	private Date fechaActualizacion;
	private Date fechaVencimiento;
	private String modifico;
	private Set<ListaPreciosDetalle> detalle;
	private transient String accion;
	private transient Long tipoVariacionId;
	private transient String opcion;
	
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToMany(
			cascade={CascadeType.PERSIST, CascadeType.MERGE},
			mappedBy="listasPrecio",
			fetch=FetchType.LAZY, 
			targetEntity=ClienteEmp.class
	)
	public Set<ClienteEmp> getClientesEmp() {
		return clientesEmp;
	}
	public void setClientesEmp(Set<ClienteEmp> clienteEmp) {
		this.clientesEmp = clienteEmp;
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
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	@ManyToOne(cascade={CascadeType.MERGE}, fetch=FetchType.EAGER)
	public TipoVariacion getTipoVariacion() {
		return tipoVariacion;
	}
	public void setTipoVariacion(TipoVariacion tipoVariacion) {
		this.tipoVariacion = tipoVariacion;
	}
	public Boolean getListaFija() {
		return listaFija;
	}
	public void setListaFija(Boolean listaFija) {
		this.listaFija = listaFija;
	}	
	public Boolean getHabilitada() {
		return habilitada;
	}
	public void setHabilitada(Boolean habilitada) {
		this.habilitada = habilitada;
	}
	@ManyToOne(cascade={CascadeType.MERGE}, fetch=FetchType.LAZY)
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
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
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	@Transient
	public String getFechaVencimientoStr() {
		if(fechaVencimiento==null)
			return "";
		return formatoFechaFormularios.format(fechaVencimiento);
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	@OneToMany(
		mappedBy="listaPrecios", 
		fetch=FetchType.EAGER, 
		cascade=CascadeType.ALL)
	public Set<ListaPreciosDetalle> getDetalle() {
		return detalle;
	}
	public void setDetalle(Set<ListaPreciosDetalle> detalle) {
		this.detalle = detalle;
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
	@Transient
	public void setAccion(String accion) {
		this.accion = accion;
	}
	@Transient
	public Long getTipoVariacionId() {
		return tipoVariacionId;
	}
	@Transient
	public void setTipoVariacionId(Long tipoVariacionId) {
		this.tipoVariacionId = tipoVariacionId;
	}	
	@Transient
	public String getOpcion() {
		return opcion;
	}
	@Transient
	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}
	@Override
	public int compareTo(ListaPrecios o) {
		int cmp = getId().compareTo(o.getId());
		if(cmp != 0) return cmp;
				
		cmp = getCodigo().compareTo(o.getCodigo());
		if(cmp != 0) return cmp;
		return 0;
	}
	
	public Boolean getUsaVencimiento() {
		return usaVencimiento;
	}
	
	public void setUsaVencimiento(Boolean usaVencimiento) {
		this.usaVencimiento = usaVencimiento;
	}
		
}
