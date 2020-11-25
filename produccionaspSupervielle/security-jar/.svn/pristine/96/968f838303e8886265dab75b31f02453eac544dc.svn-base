/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.modelo.configuraciongeneral;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import com.security.modelo.administracion.ClienteAsp;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="impuestos")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Impuesto implements Comparable<Impuesto>{
	private Long id;
	private String codigo;
	private String descripcion;
	private String tipo;
	private BigDecimal alicuota;
	private ClienteAsp cliente;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(length=3)
	public String getCodigo() {
		return codigo.toUpperCase();
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo.toUpperCase();
	}
	@Column(length=30)
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public BigDecimal getAlicuota() {
		return alicuota;
	}
	public void setAlicuota(BigDecimal alicuota) {
		this.alicuota = alicuota;
	}
	@ManyToOne(cascade={CascadeType.MERGE}, fetch=FetchType.LAZY)
	public ClienteAsp getCliente() {
		return cliente;
	}
	public void setCliente(ClienteAsp cliente) {
		this.cliente = cliente;
	}

	public abstract BigDecimal calcular(BigDecimal monto);
}
