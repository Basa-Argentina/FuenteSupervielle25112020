/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/08/2011
 */
package com.dividato.configuracionGeneral.validadores.jerarquias;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.jerarquias.OperacionElemento;

/**
 * @author Gabriel Mainero
 *
 */
@Component
public class OperacionReferenciaBusquedaValidator implements Validator{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class type) {
		return OperacionElemento.class.isAssignableFrom(type);
	}
	
	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		
	}

	@Override
	public void validate(Object command, Errors errors) {
		//OperacionReferencia operacion = (OperacionReferencia) command;
		
	}

}
