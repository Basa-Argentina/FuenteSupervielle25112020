package com.dividato.security.validadores;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.seguridad.Parameter;
import com.security.recursos.ValidacionEMail;
/**
 * 
 * @author Gabriel Mainero
 *
 */
@Component
public class ParameterValidator implements Validator {
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Parameter.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"passwordExpirationDays","failedLoginCounter","minutesSanctionLogin","passwordWarningDays",
				"hostSMTP","portSMTP","userSMTP","eMailUserSMTP","passwordSMTP"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		Parameter parameter = (Parameter) command;
		if(parameter.getPasswordWarningDays() > parameter.getPasswordExpirationDays())
			errors.rejectValue("passwordWarningDays", "formularioParameterInsMod.errorPasswordWarningDays");
		if(!ValidacionEMail.validar(parameter.geteMailUserSMTP())){
			errors.rejectValue("eMailUserSMTP", "formularioParameterInsMod.errorFormatoEMail");
			return;
		}	
		
	}	
			
}