/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.modelo.configuraciongeneral;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;

/**
 * @author Ezequiel Beccaria
 *
 */
@Entity(name="ImpuestosIva")
public class ImpuestoIva extends Impuesto implements Serializable{
	private static final long serialVersionUID = 568492019526235781L;

	@Override
	public BigDecimal calcular(BigDecimal monto) {
		// es igual -> monto*(1+Alicuota/100)
		return monto.multiply((getAlicuota().divide(new BigDecimal(100)).add(new BigDecimal(1))));
	}

	@Override
	public int compareTo(Impuesto o) {
		int cmp = this.getId().compareTo(o.getId());
		if(cmp != 0) return 0;
		
		cmp = this.getCodigo().compareTo(o.getCodigo());
		if(cmp != 0) return 0;
		
		cmp = this.getDescripcion().compareTo(o.getDescripcion());
		if(cmp != 0) return 0;
		
		cmp = this.getTipo().compareTo(o.getTipo());
		if(cmp != 0) return 0;
		
		cmp = this.getAlicuota().compareTo(o.getAlicuota());
		if(cmp != 0) return 0;
		
		cmp = this.getCliente().compareTo(o.getCliente());
		return 0;
	}
}
