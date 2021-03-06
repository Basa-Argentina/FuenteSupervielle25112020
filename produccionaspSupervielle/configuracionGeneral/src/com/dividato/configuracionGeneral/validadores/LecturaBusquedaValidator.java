/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/08/2011
 */
package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.configuraciongeneral.Lectura;

/**
 * @author Victor Kenis
 *
 */
@Component
public class LecturaBusquedaValidator implements Validator{

	@Override
	public boolean supports(Class type) {
		return Lectura.class.isAssignableFrom(type);
	}
	
	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fechaDesde",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fechaHasta",
				new CustomDateEditor(formatoFechaFormularios, true));
	}

	@Override
	public void validate(Object command, Errors errors) {
		Lectura lectura = (Lectura) command;
		if (lectura.getCodigoDesde() != null
				&& lectura.getCodigoHasta() != null) {
			if (lectura.getCodigoDesde().longValue() > lectura.getCodigoHasta()
					.longValue()) {
				errors.rejectValue("codigoDesde",
						"formularioLectura.errorCodigoDesde");

			}
		}
	}

}
