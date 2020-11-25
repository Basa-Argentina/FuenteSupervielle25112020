/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/05/2011
 */
package com.security.modelo.configuraciongeneral;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.security.recursos.Configuracion;
import com.security.utils.HashCodeUtil;

/**
 * @author Gonzalo Noriega
 *
 */
@Entity(name="cai")
public class Cai {
	private Long id;
	private Serie serie;
	private Long numero;
	private Date fechaVencimiento;
	private transient String accion;
	private transient Long idSerie;
	
	public Cai(){
		super();
		this.serie = new Serie();
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Serie getSerie() {
		return serie;
	}
	public void setSerie(Serie serie) {
		this.serie = serie;
	}
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
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
	public Long getIdSerie() {
		return idSerie;
	}
	@Transient
	public void setIdSerie(Long idSerie) {
		this.idSerie = idSerie;
	}
	
	@Transient
	public String getFechaVencimientoStr(){
		if(fechaVencimiento==null)
			return "";
		return Configuracion.formatoFechaHoraFormularios.format(fechaVencimiento);
	}
	
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cai tipo = (Cai) o;		
		if (getId() != null ? !getId().equals(tipo.getId()) : tipo.getId() != null)		 
			return false;
		return true;
	}	
	
	public int hashCode(){
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, this.id);
		return result;
	}
	
}
