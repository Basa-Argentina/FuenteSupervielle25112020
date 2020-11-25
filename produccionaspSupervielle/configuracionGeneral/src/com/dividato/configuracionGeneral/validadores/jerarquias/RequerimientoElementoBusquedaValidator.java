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

import com.dividato.configuracionGeneral.objectForms.RequerimientoElementoBusquedaForm;

/**
 * @author Gabriel Mainero
 *
 */
@Component
public class RequerimientoElementoBusquedaValidator implements Validator{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class type) {
		return RequerimientoElementoBusquedaForm.class.isAssignableFrom(type);
	}
	
	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fecha1",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fecha2",
				new CustomDateEditor(formatoFechaFormularios, true));
		binder.registerCustomEditor(Date.class, "fechaEntre",
				new CustomDateEditor(formatoFechaFormularios, true));	
		binder.registerCustomEditor(Date.class, "fechaInicio",
				new CustomDateEditor(formatoFechaFormularios, true));	
		binder.registerCustomEditor(Date.class, "fechaFin",
				new CustomDateEditor(formatoFechaFormularios, true));	
	}

	@Override
	public void validate(Object command, Errors errors) {
//		RequerimientoElementoBusquedaForm requerimiento = (RequerimientoElementoBusquedaForm) command;
//		if(requerimiento.getFecha1() != null && requerimiento.getFecha2() != null)
//			if(requerimiento.getFecha1().after(requerimiento.getFecha2()))
//				errors.rejectValue("fecha1", "formularioRequerimientoElemento.errorRangoFecha");
//		if (requerimiento.getNumero1() != null
//				&& requerimiento.getNumero2() != null) {
//			if (requerimiento.getNumero1().longValue() > requerimiento.getNumero2()
//					.longValue()) {
//				errors.rejectValue("numero1",
//						"formularioRequerimientoElemento.errorNumero1");
//
//			}
//		}
	}

}
