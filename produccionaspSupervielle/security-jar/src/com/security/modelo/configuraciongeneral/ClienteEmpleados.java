/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/05/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.security.modelo.administracion.ClienteAsp;


/**
 * @author Luis Manzanelli
 *
 */
@Entity(name="clientesEmpleados")
public class ClienteEmpleados implements Comparable<ClienteEmpleados>{
	private Long id;
	private String celular;
	private String codigo;
	private String fax;
	private boolean habilitado;
	private String interno;
	private ClienteEmp clienteEmp;
	private String observaciones;
	private Direccion direccionDefecto;
	
	

	@Override
	public int compareTo(ClienteEmpleados o) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getCelular() {
		return celular;
	}



	public void setCelular(String celular) {
		this.celular = celular;
	}



	public String getCodigo() {
		return codigo;
	}



	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}



	public String getFax() {
		return fax;
	}



	public void setFax(String fax) {
		this.fax = fax;
	}



	public boolean isHabilitado() {
		return habilitado;
	}



	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}



	public String getInterno() {
		return interno;
	}



	public void setInterno(String interno) {
		this.interno = interno;
	}


	@ManyToOne
	public ClienteEmp getClienteEmp() {
		return clienteEmp;
	}



	public void setClienteEmp(ClienteEmp clienteEmp) {
		this.clienteEmp = clienteEmp;
	}



	public String getObservaciones() {
		return observaciones;
	}



	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	@ManyToOne
	public Direccion getDireccionDefecto() {
		return direccionDefecto;
	}



	public void setDireccionDefecto(Direccion direccionDefecto) {
		this.direccionDefecto = direccionDefecto;
	}

}
