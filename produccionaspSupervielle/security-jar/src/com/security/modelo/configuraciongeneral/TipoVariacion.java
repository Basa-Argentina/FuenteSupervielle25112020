/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
 */
package com.security.modelo.configuraciongeneral;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="tipos_variacion")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="tipo",
    discriminatorType=DiscriminatorType.STRING
)
public abstract class TipoVariacion implements Comparable<TipoVariacion>{
	private Long id;
	private String descripcion;
	
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
	/**
	 * Metodo que retorna el monto real de un valor en base precio base y un valor.
	 * Este metodo es implementado de diferentes estrategias segun la subclase intanciada.
	 * @param precioBase
	 * @param valor
	 * @return
	 */
	public abstract BigDecimal calcularMonto(BigDecimal precioBase, BigDecimal valor);
	
}
