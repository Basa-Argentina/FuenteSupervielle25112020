/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 10/05/2011
 */
package com.security.modelo.configuraciongeneral;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.general.PersonaJuridica;
import com.security.modelo.general.TipoDocumento;
import com.security.recursos.Configuracion;


/**
 * @author Gonzalo Noriega
 * @modificado Víctor Kenis (12/08/2011)
 *
 */
@Entity
@Table(name="empresas")
public class Empresa implements Comparable<Empresa>{
	private Long id;
	private String codigo;
	private PersonaJuridica razonSocial;
	private Boolean principal;
	private String descripcion;
	private TipoDocumento tipoDoc;
	private String numeroDoc;
	private AfipCondIva afipCondIva;  
	private Direccion direccion;
	private String telefono;
	private String mail;
	private ClienteAsp cliente;
	private Set<Sucursal> sucursales;
	private Serie serie1;
	private Serie serie2;
	private String ingresosBrutos;
	private String numeroEstablecimiento;
	private String sedeTimbrado;
	private Date fechaInicioActividad;
	private transient String accion;
	private transient Long idTipoDocSel;
	private transient Long idBarrio;
	private transient Long idDireccion;
	private transient Long idAfipCondIva;
	private transient Long idRazonSocial;
	private transient String codigoSerie1;
	private transient String codigoSerie2;
	
	
	public Empresa() {
		super();
		this.razonSocial = new PersonaJuridica();
		this.direccion = new Direccion();
		this.principal = false;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(columnDefinition = "VARCHAR(20)")
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public TipoDocumento getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(TipoDocumento tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	
	@Column(columnDefinition = "VARCHAR(15)")
	public String getNumeroDoc() {
		return numeroDoc;
	}
	public void setNumeroDoc(String numeroDoc) {
		this.numeroDoc = numeroDoc;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteAsp getCliente() {
		return cliente;
	}

	public void setCliente(ClienteAsp cliente) {
		this.cliente = cliente;
	}

	@Transient
	public String getNombreRazonSocial(){
		return razonSocial.getRazonSocial();
	}
	
	@Column(columnDefinition = "VARCHAR(4)")
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Boolean getPrincipal() {
		return principal;
	}
	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public AfipCondIva getAfipCondIva() {
		return afipCondIva;
	}
	public void setAfipCondIva(AfipCondIva afipCondIva) {
		this.afipCondIva = afipCondIva;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public Direccion getDireccion() {
		return direccion;
	}
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	public void setRazonSocial(PersonaJuridica razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public PersonaJuridica getRazonSocial() {
		return razonSocial;
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="empresa", fetch=FetchType.LAZY)
	public Set<Sucursal> getSucursales() {
		return sucursales;
	}
	public void setSucursales(Set<Sucursal> sucursales) {
		this.sucursales = sucursales;
	}
	@Override
	public int compareTo(Empresa o) {
		int cmp = this.id.compareTo(o.getId());
		if(cmp != 0) return cmp;
			
		cmp = this.descripcion.compareTo(o.getDescripcion());
		return cmp;
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
	public Long getIdTipoDocSel() {
		return idTipoDocSel;
	}
	@Transient
	public void setIdTipoDocSel(Long idTipoDocSel) {
		this.idTipoDocSel = idTipoDocSel;
	}
	@Transient
	public Long getIdBarrio() {
		return idBarrio;
	}
	@Transient
	public void setIdBarrio(Long idBarrio) {
		this.idBarrio = idBarrio;
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
	public Long getIdAfipCondIva() {
		return idAfipCondIva;
	}
	@Transient
	public void setIdAfipCondIva(Long idAfipCondIva) {
		this.idAfipCondIva = idAfipCondIva;
	}
	@Transient
	public Long getIdRazonSocial() {
		return idRazonSocial;
	}
	@Transient
	public void setIdRazonSocial(Long idRazonSocial) {
		this.idRazonSocial = idRazonSocial;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public Serie getSerie1() {
		return serie1;
	}

	public void setSerie1(Serie serie1) {
		this.serie1 = serie1;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public Serie getSerie2() {
		return serie2;
	}
	
	public void setSerie2(Serie serie2) {
		this.serie2 = serie2;
	}
	
	public String getIngresosBrutos() {
		return ingresosBrutos;
	}
	
	public void setIngresosBrutos(String ingresosBrutos) {
		this.ingresosBrutos = ingresosBrutos;
	}
	
	public String getNumeroEstablecimiento() {
		return numeroEstablecimiento;
	}

	public void setNumeroEstablecimiento(String numeroEstablecimiento) {
		this.numeroEstablecimiento = numeroEstablecimiento;
	}
	public String getSedeTimbrado() {
		return sedeTimbrado;
	}

	public void setSedeTimbrado(String sedeTimbrado) {
		this.sedeTimbrado = sedeTimbrado;
	}

	public Date getFechaInicioActividad() {
		return fechaInicioActividad;
	}

	public void setFechaInicioActividad(Date fechaInicioActividad) {
		this.fechaInicioActividad = fechaInicioActividad;
	}

	@Transient
	public String getFechaInicioActividadStr(){
		if(fechaInicioActividad == null)
			return "";
		return Configuracion.formatoFechaFormularios.format(fechaInicioActividad);
		
	}
	@Transient
	public String getCodigoSerie1() {
		return codigoSerie1;
	}
	@Transient
	public void setCodigoSerie1(String codigoSerie1) {
		this.codigoSerie1 = codigoSerie1;
	}
	@Transient
	public String getCodigoSerie2() {
		return codigoSerie2;
	}
	@Transient
	public void setCodigoSerie2(String codigoSerie2) {
		this.codigoSerie2 = codigoSerie2;
	}
	
	
}
