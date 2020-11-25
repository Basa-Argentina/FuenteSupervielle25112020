/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.dividato.configuracionGeneral.objectForms;

import java.math.BigDecimal;

import com.security.modelo.configuraciongeneral.Impuesto;

/**
 * @author Ezequiel Beccaria
 *
 */
public class ImpuestoBusquedaForm extends Impuesto{
	private String accion;	
	
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	@Override
	public BigDecimal calcular(BigDecimal monto) {
		return null;
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
