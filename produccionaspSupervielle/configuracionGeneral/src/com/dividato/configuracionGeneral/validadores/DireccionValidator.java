package com.dividato.configuracionGeneral.validadores;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.configuraciongeneral.Direccion;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class DireccionValidator implements Validator {
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Direccion.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"calle","numero"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Direccion direccion = (Direccion) command;
		if(direccion.getAccion().equals("NUEVO")){
			if(direccion.getCalle() == null || direccion.getCalle().equals("")){	
				errors.rejectValue("calle", "formularioDireccion.errorCalle");
			}
			if(direccion.getNumero() == null || direccion.getNumero().equals("")){	
				errors.rejectValue("numero", "formularioDireccion.errorNumero");
			}
		}
		
	}		
}