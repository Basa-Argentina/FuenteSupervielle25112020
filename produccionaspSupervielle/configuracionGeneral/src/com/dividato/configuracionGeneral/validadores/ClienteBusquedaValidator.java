/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.validadores;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.configuraciongeneral.ClienteEmp;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class ClienteBusquedaValidator implements Validator{

	@Override
	public boolean supports(Class type) {
		return ClienteEmp.class.isAssignableFrom(type);
	}
	
	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {}

	@Override
	public void validate(Object command, Errors errors) {
		// nada que validar		
	}

}
