/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 23/06/2011
 */
package com.security.modelo.configuraciongeneral;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity
@DiscriminatorValue("TipoVariacionValorFijo")
public class TipoVariacionValorFijo extends TipoVariacion{

	/**
	 * Calcula el valor de un Concepto en base al valor pasado. Devuelve un monto
	 * que es la suma del precio base mas el valor pasado por parametros
	 * (return = precioBase + valor)
	 */
	@Override
	public BigDecimal calcularMonto(BigDecimal precioBase, BigDecimal valor) {
		return precioBase.add(valor);
	}

	@Override
	public int compareTo(TipoVariacion o) {
		int cmp = this.getId().compareTo(o.getId());
		if(cmp != 0) return cmp;
		
		cmp = this.getDescripcion().compareTo(o.getDescripcion());
		return cmp;
	}
	
}
