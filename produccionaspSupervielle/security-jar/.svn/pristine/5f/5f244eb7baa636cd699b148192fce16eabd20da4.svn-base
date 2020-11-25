/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/08/2011
 */
package com.security.modelo.configuraciongeneral;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;
import java.util.Set;

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

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.User;

/**
 * @author Victor Kenis
 *
 */
@Entity(name="lecturas")
public class Lectura implements Comparable<Lectura>{
	private Long id;
	private Empresa empresa;
	private Sucursal sucursal;
	private ClienteAsp clienteAsp;
	private User usuario;
	private Long codigo;
	private String descripcion;
	private String observacion;
	private Date fecha;
	private Set<LecturaDetalle> detalles;
	private Long elementos;
	private Boolean utilizada;
	private transient String accion;
	private transient String codigoEmpresa;
	private transient String codigoSucursal;
	private transient String codigoSerie;
	private transient String codigoUsuario;
	private transient Date fechaDesde;
	private transient Date fechaHasta;
	private transient Long codigoDesde;
	private transient Long codigoHasta;
	private transient String codigoLecturaStr;
	private transient String codigoCliente;
	private transient String codigoTipoElemento;
	private transient String codigoElementoDesde;
	private transient String codigoElementoHasta;
	
	public Lectura(){
		super();
		this.utilizada = false;
		this.elementos = (long) 0;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	
	@Transient
	public String getCodigoStr() {
		String codigo = String.valueOf(getCodigo());
		if (codigo == null)
			return "";
		int cant = codigo.length();
		int largo = 8 - cant;
		for (int i = 0; i < largo; i++) {
			codigo = "0" + codigo;
		}
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	@Column(length=30)
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Column(length=255)
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	

	@OneToMany(	mappedBy="lectura", 
				fetch=FetchType.LAZY, 
				cascade=CascadeType.ALL,
				orphanRemoval = true)
	public Set<LecturaDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(Set<LecturaDetalle> detalles) {
		this.detalles = detalles;
	}
	
	public Long getElementos() {
		return elementos;
	}

	public void setElementos(Long elementos) {
		this.elementos = elementos;
	}
	
	public Boolean getUtilizada() {
		return utilizada;
	}

	public void setUtilizada(Boolean ultilizada) {
		this.utilizada = ultilizada;
	}

	@Transient
	public String getAccion() {
		return accion;
	}
	@Transient
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	@Override
	public int compareTo(Lectura o) {
		int cmp = this.id.compareTo(o.getId());
		if(cmp != 0) return cmp;
		return cmp;
	}
	
	public Date getFecha() {
		return fecha;
	}
	@Transient
	public String getFechaStr() {
		if(fecha==null)
			return "";
		return formatoFechaFormularios.format(fecha);
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}

	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
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
	public String getCodigoSerie() {
		return codigoSerie;
	}
	@Transient
	public void setCodigoSerie(String codigoSerie) {
		this.codigoSerie = codigoSerie;
	}
	@Transient
	public String getCodigoUsuario() {
		return codigoUsuario;
	}
	@Transient
	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	@Transient
	public Date getFechaDesde() {
		return fechaDesde;
	}
	
	@Transient
	public String getFechaDesdeStr() {
		if(fechaDesde==null)
			return "";
		return formatoFechaFormularios.format(fechaDesde);
	}
	
	@Transient
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	@Transient
	public Date getFechaHasta() {
		return fechaHasta;
	}
	
	@Transient
	public String getFechaHastaStr() {
		if(fechaHasta==null)
			return "";
		return formatoFechaFormularios.format(fechaHasta);
	}
	
	@Transient
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	@Transient
	public Long getCodigoDesde() {
		return codigoDesde;
	}
	@Transient
	public void setCodigoDesde(Long codigoDesde) {
		this.codigoDesde = codigoDesde;
	}
	@Transient
	public Long getCodigoHasta() {
		return codigoHasta;
	}
	@Transient
	public void setCodigoHasta(Long codigoHasta) {
		this.codigoHasta = codigoHasta;
	}
	@Transient
	public String getCodigoLecturaStr() {
		return codigoLecturaStr;
	}
	@Transient
	public void setCodigoLecturaStr(String codigoLecturaStr) {
		this.codigoLecturaStr = codigoLecturaStr;
	}
	@Transient
	public String getCodigoCliente() {
		return codigoCliente;
	}
	@Transient
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	@Transient
	public String getCodigoTipoElemento() {
		return codigoTipoElemento;
	}
	@Transient
	public void setCodigoTipoElemento(String codigoTipoElemento) {
		this.codigoTipoElemento = codigoTipoElemento;
	}
	
	
	@Transient
	public String getCodigoElementoDesde() {
		return codigoElementoDesde;
	}
	@Transient
	public void setCodigoElementoDesde(String codigoElementoDesde) {
		this.codigoElementoDesde = codigoElementoDesde;
	}
	@Transient
	public String getCodigoElementoHasta() {
		return codigoElementoHasta;
	}
	@Transient
	public void setCodigoElementoHasta(String codigoElementoHasta) {
		this.codigoElementoHasta = codigoElementoHasta;
	}
}