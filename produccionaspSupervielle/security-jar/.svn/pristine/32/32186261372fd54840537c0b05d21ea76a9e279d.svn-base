/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/05/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;

import com.security.modelo.general.Barrio;
import com.security.modelo.general.Localidad;
import com.security.modelo.general.Pais;
import com.security.modelo.general.Provincia;


/**
 * @author Gonzalo Noriega
 *
 */
@Entity
@Table(name="clientesDirecciones")
public class ClienteDireccion implements Comparable<ClienteDireccion>{
	// Se agrega para aquellas direcciones que son
	// independientes y pertenecen al cliente empresa
	// (Lugares de entrega).
	private Long id;
	private ClienteEmp cliente;
	private Direccion direccion;	
	private String codigo;
	private String descripcion;
	private String observacion;
	private Barrio barrioAux;
	private Localidad localidadAux;
	private Provincia provinciaAux;
	private Pais paisAux;
	private transient String accion;
	private transient Long idClienteEmp;
	private transient Long idBarrio;
	private transient Long idPaisAux;
	private transient Long idProvinciaAux;
	private transient Long idLocalidadAux;
	private transient Long idDireccion;
	private transient String clienteCodigo;
	
	public ClienteDireccion() {
		super();
		this.direccion = new Direccion();
		this.cliente = new ClienteEmp();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE},fetch=FetchType.LAZY)
	public ClienteEmp getCliente() {
		return cliente;
	}

	public void setCliente(ClienteEmp cliente) {
		this.cliente = cliente;
	}
	
	@Column(length = 3)
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	@OneToOne
	@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	public Direccion getDireccion() {
		return direccion;
	}
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	
	@Column(length = 100)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(length = 255)
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Override
	public int compareTo(ClienteDireccion o) {
		int cmp = this.id.compareTo(o.getId());
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
	public Long getIdDireccion() {
		return idDireccion;
	}
	@Transient
	public void setIdDireccion(Long idDireccion) {
		this.idDireccion = idDireccion;
	}
	@Transient
	public Long getIdClienteEmp() {
		return idClienteEmp;
	}
	@Transient
	public void setIdClienteEmp(Long idClienteEmp) {
		this.idClienteEmp = idClienteEmp;
	}
	@Transient
	public String getClienteCodigo() {
		return clienteCodigo;
	}
	@Transient
	public void setClienteCodigo(String clienteCodigo) {
		this.clienteCodigo = clienteCodigo;
	}
	
	@Transient
	public String getCodigoDescripcion() {
		if(codigo != null && descripcion !=null){
			return codigo + " - "+ descripcion;
		}
		return "";
	}
	@ManyToOne
	public Barrio getBarrioAux() {
		return barrioAux;
	}

	public void setBarrioAux(Barrio barrioAux) {
		this.barrioAux = barrioAux;
	}
	@ManyToOne
	public Localidad getLocalidadAux() {
		return localidadAux;
	}

	public void setLocalidadAux(Localidad localidadAux) {
		this.localidadAux = localidadAux;
	}
	@ManyToOne
	public Provincia getProvinciaAux() {
		return provinciaAux;
	}

	public void setProvinciaAux(Provincia provinciaAux) {
		this.provinciaAux = provinciaAux;
	}
	@ManyToOne
	public Pais getPaisAux() {
		return paisAux;
	}
	
	public void setPaisAux(Pais paisAux) {
		this.paisAux = paisAux;
	}
	@Transient
	public Long getIdPaisAux() {
		return idPaisAux;
	}
	@Transient
	public void setIdPaisAux(Long idPaisAux) {
		this.idPaisAux = idPaisAux;
	}
	@Transient
	public Long getIdProvinciaAux() {
		return idProvinciaAux;
	}
	@Transient
	public void setIdProvinciaAux(Long idProvinciaAux) {
		this.idProvinciaAux = idProvinciaAux;
	}
	@Transient
	public Long getIdLocalidadAux() {
		return idLocalidadAux;
	}
	@Transient
	public void setIdLocalidadAux(Long idLocalidadAux) {
		this.idLocalidadAux = idLocalidadAux;
	}
	@Transient
	public String getCalleNumeroPisoDpto(){
		if(direccion==null)
			return "";
		return direccion.getCalle()+" "+direccion.getNumero()+" "+direccion.getPiso()+" "+direccion.getDpto();
	}
	
	
}
