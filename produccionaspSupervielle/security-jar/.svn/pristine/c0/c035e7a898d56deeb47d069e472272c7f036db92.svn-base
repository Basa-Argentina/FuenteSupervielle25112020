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
@DiscriminatorValue("TipoVariacionPorcentual")
public class TipoVariacionPorcentual extends TipoVariacion{

	/**
	 * Calcula el valor de un Concepto de forma porcentual. Devuelve un monto
	 * que es un porcentaje del precio base.
	 * (return = precioBase * (1 + valor/100))
	 */
	@Override
	public BigDecimal calcularMonto(BigDecimal precioBase, BigDecimal valor) {
		 return precioBase.multiply((valor.divide(new BigDecimal(100)).add(new BigDecimal(1))));
	}

	@Override
	public int compareTo(TipoVariacion o) {
		int cmp = this.getId().compareTo(o.getId());
		if(cmp != 0) return cmp;
		
		cmp = this.getDescripcion().compareTo(o.getDescripcion());
		return cmp;
	}

}
