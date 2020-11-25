/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
 */
package com.security.modelo.configuraciongeneral;

import java.math.BigDecimal;

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
@Entity(name="conceptos_precios")
public class ListaPreciosDetalle implements Comparable<ListaPreciosDetalle>{
	private Long id;
	private ConceptoFacturable conceptoFacturable;
	private BigDecimal valor;
	private TipoVariacion tipoVariacion;
	private ListaPrecios listaPrecios;
	private transient String accion;
	private transient String conceptoCodigo;
	private transient Long variacionId;
	private transient String listaPreciosCodigo;
	private transient String urlDestino;
		
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST})
	public ConceptoFacturable getConceptoFacturable() {
		return conceptoFacturable;
	}
	public void setConceptoFacturable(ConceptoFacturable conceptoFacturable) {
		this.conceptoFacturable = conceptoFacturable;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST})
	public TipoVariacion getTipoVariacion() {
		return tipoVariacion;
	}
	public void setTipoVariacion(TipoVariacion tipoVariacion) {
		this.tipoVariacion = tipoVariacion;
	}
	@ManyToOne    
	public ListaPrecios getListaPrecios() {
		return listaPrecios;
	}
	public void setListaPrecios(ListaPrecios listaPrecios) {
		this.listaPrecios = listaPrecios;
	}
	@Transient
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}	
	@Transient
	public String getConceptoCodigo() {
		return conceptoCodigo;
	}
	public void setConceptoCodigo(String conceptoCodigo) {
		this.conceptoCodigo = conceptoCodigo;
	}
	@Transient
	public Long getVariacionId() {
		return variacionId;
	}
	public void setVariacionId(Long variacionId) {
		this.variacionId = variacionId;
	}
	@Transient
	public String getListaPreciosCodigo() {
		return listaPreciosCodigo;
	}
	public void setListaPreciosCodigo(String listaPreciosCodigo) {
		this.listaPreciosCodigo = listaPreciosCodigo;
	}
	@Transient
	public String getUrlDestino() {
		return urlDestino;
	}
	public void setUrlDestino(String urlDestino) {
		this.urlDestino = urlDestino;
	}	
	@Transient
	public BigDecimal getCalcularMonto(){
		if(tipoVariacion != null && conceptoFacturable != null && valor != null)
			return this.tipoVariacion.calcularMonto(conceptoFacturable.getPrecioBase(), valor);
		return null;
	}
	@Override
	public int compareTo(ListaPreciosDetalle o) {
		if(o == null)
			return -1;
	
		int cmp = conceptoFacturable.compareTo(o.getConceptoFacturable());
		if(cmp != 0) return cmp;
		
		cmp = tipoVariacion.compareTo(o.getTipoVariacion());
		if(cmp != 0) return cmp;
		
		cmp = valor.compareTo(o.getValor());
		if(cmp != 0) return cmp;
		
		cmp = getId().compareTo(o.getId());
		if(cmp != 0) return cmp;
		
		cmp = listaPrecios.compareTo(o.getListaPrecios());
		return cmp;		
	}
}
