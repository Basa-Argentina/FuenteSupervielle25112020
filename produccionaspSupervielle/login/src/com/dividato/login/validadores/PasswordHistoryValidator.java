package com.dividato.login.validadores;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.seguridad.PasswordHistory;
/**
 * 
 * @author Gabriel Mainero
 *
 */
@Component
public class PasswordHistoryValidator implements Validator {
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return PasswordHistory.class.isAssignableFrom(type);
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
	public void validate(Object command, Errors errors) {
		PasswordHistory passwordHistory = (PasswordHistory) command;		
		if(!passwordHistory.getNuevaContrasenia().equals(passwordHistory.getConfirmarContrasenia())){
			errors.rejectValue("confirmarContrasenia", "formularioPasswordHistory.errorDistintasContrasenias");
		}
	}
	
			
}