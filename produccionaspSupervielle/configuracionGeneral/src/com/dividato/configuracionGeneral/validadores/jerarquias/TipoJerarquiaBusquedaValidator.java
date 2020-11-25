/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/07/2011
 */
package com.dividato.configuracionGeneral.validadores.jerarquias;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.security.modelo.jerarquias.TipoJerarquia;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class TipoJerarquiaBusquedaValidator implements Validator{

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return TipoJerarquia.class.isAssignableFrom(type);
	}
	
	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub
		
	}

}
