/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/05/2011
 */
package com.security.modelo.configuraciongeneral;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.modelo.seguridad.User;


/**
 * @author Gonzalo Noriega
 *
 */
@Entity
@Table(name="clienteEmpleados")
public class Empleado  extends User{
	private static final long serialVersionUID = -6867445469676355954L;
	
	private ClienteEmp clienteEmp;
	private String codigo;
	private String interno;
	private String celular;
	private String fax;
	private Boolean habilitado;
	private String observaciones;
	private Set<ClasificacionDocumental> clasificacionesDocumentales;
	private ClienteDireccion direccionDefecto;
	private transient String codigoDireccion;
	private transient String accion;
	private transient Long idBarrio;
	private transient String clienteCodigo;
	private transient Long idTipoDoc;
	private transient String codigoCliente;
	
	
	public Empleado() {
		super();
		this.clienteEmp = new ClienteEmp();
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}

	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}
	
	@Column(length=6)
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Boolean getHabilitado() {
		return habilitado;
	}
	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}
	@Column(length=5)
	public String getInterno() {
		return interno;
	}

	public void setInterno(String interno) {
		this.interno = interno;
	}

	@Column(length=20)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}
	
	public int compareTo(Empleado o) {
		int cmp = this.getId().compareTo(o.getId());
		if(cmp != 0) return cmp;
			
		cmp = this.codigo.compareTo(o.getCodigo());
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
	public Long getIdBarrio() {
		return idBarrio;
	}
	@Transient
	public void setIdBarrio(Long idBarrio) {
		this.idBarrio = idBarrio;
	}
	
	@Transient
	public Long getIdTipoDoc() {
		return idTipoDoc;
	}
	@Transient
	public void setIdTipoDoc(Long idTipoDoc) {
		this.idTipoDoc = idTipoDoc;
	}
	
	@Transient
	public String getNombreYApellido(){
		if(getPersona().getNombre() != null && getPersona().getApellido() != null)
			return getPersona().getNombre() + ", "+getPersona().getApellido();
		return "";
	}
	
	@Transient
	public String getApellidoYNombre(){
		if(getPersona().getNombre() != null && getPersona().getApellido() != null)
			return  getPersona().getApellido()+ ", "+getPersona().getNombre();
		return "";
	}
	@Column(length=500)
	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteDireccion getDireccionDefecto() {
		return direccionDefecto;
	}

	public void setDireccionDefecto(ClienteDireccion direccionDefecto) {
		this.direccionDefecto = direccionDefecto;
	}
	@ManyToMany(
			cascade={CascadeType.PERSIST, CascadeType.MERGE},
			mappedBy="empleadosClienteEmp",
			fetch=FetchType.LAZY, 
			targetEntity=ClasificacionDocumental.class
	)
	public Set<ClasificacionDocumental> getClasificacionesDocumentales() {
		return clasificacionesDocumentales;
	}
	public void setClasificacionesDocumentales(
			Set<ClasificacionDocumental> clasificacionesDocumentales) {
		this.clasificacionesDocumentales = clasificacionesDocumentales;
	}
	
	@Transient
	public String getCodigoDireccion() {
		return codigoDireccion;
	}
	@Transient
	public void setCodigoDireccion(String codigoDireccion) {
		this.codigoDireccion = codigoDireccion;
	}

	@Transient
	public String getClienteCodigo() {
		return clienteCodigo;
	}
	
	@Transient
	public void setClienteCodigo(String clienteCodigo) {
		this.clienteCodigo = clienteCodigo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().intValue());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof Empleado)) {
			return false;
		}
		Empleado other = (Empleado) obj;
		if (this.getId() == null) {
			return false;
		} else if (!this.getId().equals(other.getId())) {
			return false;
		}
		return true;
	}
	@Transient
	public String getCodigoCliente() {
		return codigoCliente;
	}
	@Transient
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	
}
