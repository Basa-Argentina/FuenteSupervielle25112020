package com.dividato.configuracionGeneral.validadores;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.configuraciongeneral.ElementoHistorico;
/**
 * 
 * @author Victor Kenis
 *
 */
@Component
public class ElementoHistoricoBusquedaValidator implements Validator {	
		
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return ElementoHistorico.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
//		binder.registerCustomEditor(Date.class, "fechaDesde",
//				new CustomDateEditor(formatoFechaFormularios, true));
//		binder.registerCustomEditor(Date.class, "fechaHasta",
//				new CustomDateEditor(formatoFechaFormularios, true));
	}
	
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
				
	}

}