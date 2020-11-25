/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/07/2011
 */
package com.dividato.configuracionGeneral.validadores.jerarquias;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.jerarquias.interfaz.TipoJerarquiaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.jerarquias.TipoJerarquia;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class TipoJerarquiaValidator implements Validator{
	private TipoJerarquiaService service;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return TipoJerarquia.class.isAssignableFrom(type);
	}
	
	@Autowired
	public void setService(TipoJerarquiaService service) {
		this.service = service;
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"codigo","descripcion"});
	}

	@Override
	public void validate(Object obj, Errors errors) {
		TipoJerarquia tipo = (TipoJerarquia) obj;
		
		//valido que el codigo ingresado sea unico
		List<TipoJerarquia> tipos = service.listarTipoJerarquia(tipo.getCodigo(), tipo.getDescripcion(), obtenerClienteAspUser());
		if(tipos != null && !tipos.isEmpty())
			if(!("MODIFICACION").equals(tipo.getAccion()) || !tipos.get(0).getId().equals(tipo.getId()))
				errors.rejectValue("codigo", "error.tipoJerarquia.errorCodigo");			
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}

}
