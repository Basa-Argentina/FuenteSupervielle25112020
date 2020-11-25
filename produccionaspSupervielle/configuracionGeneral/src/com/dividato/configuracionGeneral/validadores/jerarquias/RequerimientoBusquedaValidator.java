/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/08/2011
 */
package com.dividato.configuracionGeneral.validadores.jerarquias;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.jerarquias.Requerimiento;

/**
 * @author Gabriel Mainero
 *
 */
@Component
public class RequerimientoBusquedaValidator implements Validator{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class type) {
		return Requerimiento.class.isAssignableFrom(type);
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
		binder.registerCustomEditor(Date.class, "fechaEntregaDesde",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fechaEntregaHasta",
				new CustomDateEditor(formatoFechaFormularios, true));
		
	}

	@Override
	public void validate(Object command, Errors errors) {
		Requerimiento requerimiento = (Requerimiento) command;
		if(requerimiento.getFechaDesde() != null && requerimiento.getFechaHasta() != null)
			if(requerimiento.getFechaDesde().after(requerimiento.getFechaHasta()))
				errors.rejectValue("fechaDesde", "formularioRequerimiento.errorRangoFecha");
		if(requerimiento.getFechaEntregaDesde() != null && requerimiento.getFechaEntregaHasta() != null)
			if(requerimiento.getFechaEntregaDesde().after(requerimiento.getFechaEntregaHasta()))
				errors.rejectValue("fechaEntregaDesde", "formularioRequerimiento.errorRangoFecha");
		if (requerimiento.getSerieDesde() != null
				&& requerimiento.getSerieHasta() != null) {
			if (requerimiento.getSerieDesde().longValue() > requerimiento.getSerieHasta()
					.longValue()) {
				errors.rejectValue("serieDesde",
						"formularioRequerimiento.errorSerieDesde");

			}
		}
	}

}
