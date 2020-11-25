/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 28/06/2011
 */
package com.security.modelo.configuraciongeneral;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;


/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="clientesConceptos")
public class ClienteConcepto implements Comparable<ClienteConcepto>{
	// Se agrega para aquellos conceptos que
	//pertenecen al cliente empresa y a una lista de precios
	// (Conceptos Facturables para Clientes).
	private Long id;
	private ClienteEmp cliente;
	private ListaPrecios listaPrecios;	
	private ConceptoFacturable concepto;
	private Boolean habilitado;
	private ClienteAsp clienteAsp;
	private transient String accion;
	private transient String clienteCodigo;
	private transient String listaPrecioCodigo;
	private transient String conceptoCodigo;
	private transient Long idClienteEmp;
	private transient Long idListaPrecios;
	private transient String idConcepto;;
	
	public ClienteConcepto() {
		super();
		this.cliente = new ClienteEmp();
		this.listaPrecios = new ListaPrecios();
		this.concepto = new ConceptoFacturable();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ClienteEmp getCliente() {
		return cliente;
	}

	public void setCliente(ClienteEmp cliente) {
		this.cliente = cliente;
	}
	
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ListaPrecios getListaPrecios() {
		return listaPrecios;
	}

	public void setListaPrecios(ListaPrecios listaPrecios) {
		this.listaPrecios = listaPrecios;
	}

	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public ConceptoFacturable getConcepto() {
		return concepto;
	}

	public void setConcepto(ConceptoFacturable concepto) {
		this.concepto = concepto;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE}, fetch=FetchType.LAZY)
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
	}	

	public Boolean getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}

	@Override
	public int compareTo(ClienteConcepto o) {
		int cmp = this.id.compareTo(o.getId());
		if(cmp != 0) return cmp;
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
	public Long getIdClienteEmp() {
		return idClienteEmp;
	}
	@Transient
	public void setIdClienteEmp(Long idClienteEmp) {
		this.idClienteEmp = idClienteEmp;
	}
	@Transient
	public Long getIdListaPrecios() {
		return idListaPrecios;
	}
	@Transient
	public void setIdListaPrecios(Long idListaPrecios) {
		this.idListaPrecios = idListaPrecios;
	}
	@Transient
	public String getIdConcepto() {
		return idConcepto;
	}
	@Transient
	public void setIdConcepto(String idConcepto) {
		this.idConcepto = idConcepto;
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
	public String getListaPrecioCodigo() {
		return listaPrecioCodigo;
	}
	@Transient
	public void setListaPrecioCodigo(String listaPrecioCodigo) {
		this.listaPrecioCodigo = listaPrecioCodigo;
	}
	@Transient
	public String getConceptoCodigo() {
		return conceptoCodigo;
	}
	@Transient
	public void setConceptoCodigo(String conceptoCodigo) {
		this.conceptoCodigo = conceptoCodigo;
	}
	
}
