package com.dividato.configuracionGeneral.validadores;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.Posicion;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class ImpresionEtiquetaModuloBusquedaValidator implements Validator {
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Posicion.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		
		Modulo modulo = (Modulo) command;
		
		if (modulo.getCodigoDesdeEstante() != null && !"".equals(modulo.getCodigoDesdeEstante().trim())
				&& modulo.getCodigoHastaEstante() != null && !"".equals(modulo.getCodigoHastaEstante().trim())) {
			if (Long.valueOf(modulo.getCodigoDesdeEstante()) > Long.valueOf(modulo.getCodigoHastaEstante())) {
				errors.rejectValue("codigoDesdeEstante","formularioImpresionEtiquetaModulo.error.codigoEstanteriaMayor");

			}
		}
	}	
	
}