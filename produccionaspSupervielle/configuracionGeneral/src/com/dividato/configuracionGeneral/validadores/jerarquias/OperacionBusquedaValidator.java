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

import com.security.modelo.jerarquias.Operacion;

/**
 * @author Gabriel Mainero
 *
 */
@Component
public class OperacionBusquedaValidator implements Validator{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class type) {
		return Operacion.class.isAssignableFrom(type);
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
		Operacion operacion = (Operacion) command;
		if(operacion.getFechaDesde() != null && operacion.getFechaHasta() != null)
			if(operacion.getFechaDesde().after(operacion.getFechaHasta()))
				errors.rejectValue("fechaDesde", "formularioOperacion.errorRangoFecha");
		if(operacion.getFechaEntregaDesde() != null && operacion.getFechaEntregaHasta() != null)
			if(operacion.getFechaEntregaDesde().after(operacion.getFechaEntregaHasta()))
				errors.rejectValue("fechaEntregaDesde", "formularioOperacion.errorRangoFecha");
		
		if(operacion.getIdDesde()!= null && operacion.getIdHasta()!=null){
			if(operacion.getIdDesde().longValue() > operacion.getIdHasta().longValue())
				errors.rejectValue("idDesde", "formularioOperacion.errorRangoCódigo");
		}
	}

}
