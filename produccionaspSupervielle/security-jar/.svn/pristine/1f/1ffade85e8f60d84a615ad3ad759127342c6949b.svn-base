/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.configuraciongeneral;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;


/**
 * @author Gonzalo Noriega
 * @modificado Victor Kenis (11/08/2011)
 *
 */
@Entity
@Table(name="series")
public class Serie{
	private Long id;
	private String codigo;
	private String descripcion;
	private boolean habilitado;
	private String tipoSerie; //Valores Posibles (F = Factura, R = Remito, I = Documento Interno)
	private Empresa empresa;
	private Sucursal sucursal;
	private AfipTipoComprobante afipTipoComprobante;
	private String prefijo;
	private String ultNroImpreso; //Nro Automatico, con posibilidad de modificacion.
	private String condIvaClientes; //Abreviaturas de AfipCondIva split for '|'
	private ClienteAsp cliente;
	private transient String accion;
	private transient Long idEmpresa;
	private transient Long idSucursal;
	private transient Long idAfipTipoComprobante;
	private transient String codigoAfipTipoComprobante;
	private transient String codigoEmpresa;
	private transient String codigoSucursal;
	private transient Date fechaParaCai;

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(columnDefinition = "VARCHAR(3)")
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
	
	@Column(columnDefinition = "VARCHAR(1)")
	public String getTipoSerie() {
		return tipoSerie;
	}

	public void setTipoSerie(String tipoSerie) {
		this.tipoSerie = tipoSerie;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Empresa getEmpresa() {
		return empresa;
	}	

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}

	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public AfipTipoComprobante getAfipTipoComprobante() {
		return afipTipoComprobante;
	}

	public void setAfipTipoComprobante(AfipTipoComprobante afipTipoComprobante) {
		this.afipTipoComprobante = afipTipoComprobante;
	}
	@Column(columnDefinition = "VARCHAR(4)")
	public String getPrefijo() {
		return prefijo;
	}

	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}
	@Column(columnDefinition = "VARCHAR(8)")
	public String getUltNroImpreso() {
		return ultNroImpreso;
	}

	public void setUltNroImpreso(String ultNroImpreso) {
		this.ultNroImpreso = ultNroImpreso;
	}

	public String getCondIvaClientes() {
		return condIvaClientes;
	}

	public void setCondIvaClientes(String condIvaClientes) {
		this.condIvaClientes = condIvaClientes;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteAsp getCliente() {
		return cliente;
	}
	
	public void setCliente(ClienteAsp cliente) {
		this.cliente = cliente;
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
	public Long getIdEmpresa() {
		return idEmpresa;
	}
	
	@Transient
	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	@Transient
	public Long getIdSucursal() {
		return idSucursal;
	}
	@Transient
	public void setIdSucursal(Long idSucursal) {
		this.idSucursal = idSucursal;
	}
	@Transient
	public Long getIdAfipTipoComprobante() {
		return idAfipTipoComprobante;
	}
	@Transient
	public void setIdAfipTipoComprobante(Long idAfipTipoComprobante) {
		this.idAfipTipoComprobante = idAfipTipoComprobante;
	}
	@Transient
	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}
	@Transient
	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}
	@Transient
	public String getCodigoSucursal() {
		return codigoSucursal;
	}
	@Transient
	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}
	@Transient
	public String getCodigoAfipTipoComprobante() {
		return codigoAfipTipoComprobante;
	}
	@Transient
	public void setCodigoAfipTipoComprobante(String codigoAfipTipoComprobante) {
		this.codigoAfipTipoComprobante = codigoAfipTipoComprobante;
	}
	@Transient
	public Date getFechaParaCai() {
		return fechaParaCai;
	}
	@Transient
	public void setFechaParaCai(Date fechaParaCai) {
		this.fechaParaCai = fechaParaCai;
	}
	
}
