/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 07/07/2011
 */
package com.security.modelo.jerarquias;

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
@Entity(name="jerarquia")
public class Jerarquia {
	private Long id;
	private String descripcion;
	private Integer valoracion;
	private String observacion;
	private TipoJerarquia tipo;
	private Integer horizontalDesde;
	private Integer horizontalHasta;
	private Integer verticalDesde;
	private Integer verticalHasta;
	private transient String accion;
	private transient Long tipoId;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getValoracion() {
		return valoracion;
	}
	public void setValoracion(Integer valoracion) {
		this.valoracion = valoracion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	@ManyToOne
	public TipoJerarquia getTipo() {
		return tipo;
	}
	public void setTipo(TipoJerarquia tipo) {
		this.tipo = tipo;
	}
	public Integer getHorizontalDesde() {
		return horizontalDesde;
	}
	public void setHorizontalDesde(Integer horizontalDesde) {
		this.horizontalDesde = horizontalDesde;
	}
	public Integer getHorizontalHasta() {
		return horizontalHasta;
	}
	public void setHorizontalHasta(Integer horizontalHasta) {
		this.horizontalHasta = horizontalHasta;
	}
	public Integer getVerticalDesde() {
		return verticalDesde;
	}
	public void setVerticalDesde(Integer verticalDesde) {
		this.verticalDesde = verticalDesde;
	}
	public Integer getVerticalHasta() {
		return verticalHasta;
	}
	public void setVerticalHasta(Integer verticalHasta) {
		this.verticalHasta = verticalHasta;
	}
	@Transient
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	@Transient
	public Long getTipoId() {
		return tipoId;
	}
	public void setTipoId(Long tipoId) {
		this.tipoId = tipoId;
	}
	@Transient
	public String getCoordenadasDesde(){
		return "("+String.valueOf(this.verticalDesde)+","+String.valueOf(this.horizontalDesde)+")";
	}
	@Transient
	public String getCoordenadasHasta(){
		return "("+String.valueOf(this.verticalHasta)+","+String.valueOf(this.horizontalHasta)+")";
	}
}
