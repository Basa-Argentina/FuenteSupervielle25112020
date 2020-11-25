/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/06/2011
 */
package com.security.modelo.configuraciongeneral;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.security.modelo.administracion.ClienteAsp;
import com.security.recursos.Configuracion;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity
@Table(name="conceptoFacturable")
public class ConceptoFacturable implements Comparable<ConceptoFacturable>{
	private Long id;
	private String codigo;
	private String descripcion;
	private TipoConceptoFacturable tipo;
	private Boolean habilitado;
	private Boolean generaStock;
	private BigDecimal costo;
	private BigDecimal precioBase;
	private List<Impuesto> impuestos;
	private String tipoCalculo;
	private Date fechaRegistro;
	private Date fechaActualizacion;
	private ClienteAsp clienteAsp;
	private transient String accion;
	private transient String impuestoCodigo;
	private transient Long tipoId;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(columnDefinition = "VARCHAR(6)")
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
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE},fetch=FetchType.LAZY)
	public TipoConceptoFacturable getTipo() {
		return tipo;
	}
	public void setTipo(TipoConceptoFacturable tipo) {
		this.tipo = tipo;
	}
	public Boolean getHabilitado() {
		return habilitado;
	}
	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}
	public Boolean getGeneraStock() {
		return generaStock;
	}
	public void setGeneraStock(Boolean generaStock) {
		this.generaStock = generaStock;
	}
	public BigDecimal getCosto() {
		return costo;
	}
	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}
	public BigDecimal getPrecioBase() {
		return precioBase;
	}
	public void setPrecioBase(BigDecimal precioBase) {
		this.precioBase = precioBase;
	}
	@ManyToMany(
	        targetEntity=Impuesto.class,
	        cascade={CascadeType.PERSIST, CascadeType.MERGE},
	        fetch=FetchType.EAGER
	    )
	@JoinTable(
        name="x_conceptofacturable_impuesto",
        joinColumns=@JoinColumn(name="conceptofacturable_id"),
        inverseJoinColumns=@JoinColumn(name="impuesto_id")
    )
	public List<Impuesto> getImpuestos() {
		if(impuestos == null)
			impuestos = new ArrayList<Impuesto>();
		return impuestos;
	}
	public void setImpuestos(List<Impuesto> impuestos) {
		this.impuestos = impuestos;
	}
	public String getTipoCalculo() {
		return tipoCalculo;
	}
	public void setTipoCalculo(String tipoCalculo) {
		this.tipoCalculo = tipoCalculo;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	@Transient
	public String getFechaRegistroStr() {
		if(fechaRegistro==null)
			return "";
		return Configuracion.formatoFechaHoraFormularios.format(fechaRegistro);
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
		return Configuracion.formatoFechaHoraFormularios.format(fechaActualizacion);
	}
	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
	@ManyToOne(cascade={CascadeType.MERGE})
	public ClienteAsp getClienteAsp() {
		return clienteAsp;
	}
	public void setClienteAsp(ClienteAsp clienteAsp) {
		this.clienteAsp = clienteAsp;
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
	public String getImpuestoCodigo() {
		return impuestoCodigo;
	}
	@Transient
	public void setImpuestoCodigo(String impuestoCodigo) {
		this.impuestoCodigo = impuestoCodigo;
	}
	@Transient
	public Impuesto getImpuesto(){
		if(impuestos != null && !impuestos.isEmpty())
			return impuestos.get(0);
		else
			return null;
	}
	@Transient
	public Long getTipoId() {
		return tipoId;
	}
	@Transient
	public void setTipoId(Long tipoId) {
		this.tipoId = tipoId;
	}
	@Override
	public int compareTo(ConceptoFacturable o) {		
		
		int cmp = this.codigo.compareTo(o.getCodigo());
		if(cmp != 0) return cmp;
		
		cmp = this.descripcion.compareTo(o.getDescripcion());
		if(cmp != 0) return cmp;
		
		cmp = this.tipo.compareTo(o.getTipo());
		if(cmp != 0) return cmp;
		
		cmp = this.habilitado.compareTo(o.getHabilitado());
		if(cmp != 0) return cmp;
		
		cmp = this.generaStock.compareTo(o.getGeneraStock());
		if(cmp != 0) return cmp;
		
		cmp = this.costo.compareTo(o.getCosto());
		if(cmp != 0) return cmp;
		
		cmp = this.precioBase.compareTo(o.getPrecioBase());
		if(cmp != 0) return cmp;
		
		cmp = this.tipoCalculo.compareTo(o.getTipoCalculo());
		if(cmp != 0) return cmp;
		
		cmp = this.fechaRegistro.compareTo(o.getFechaRegistro());
		if(cmp != 0) return cmp;
		
		cmp = this.fechaActualizacion.compareTo(o.getFechaActualizacion());
		if(cmp != 0) return cmp;
		
		cmp = this.clienteAsp.compareTo(o.getClienteAsp());
		if(cmp != 0) return cmp;
		
		cmp = this.id.compareTo(o.getId());		
		return cmp;
	}
	
}
