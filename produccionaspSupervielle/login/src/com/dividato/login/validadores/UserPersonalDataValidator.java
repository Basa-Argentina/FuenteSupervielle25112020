package com.dividato.login.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.general.Persona;
import com.security.modelo.seguridad.User;
import com.security.recursos.ValidacionEMail;
/**
 * 
 * @author Gabriel Mainero
 * @modified Ezequiel Beccaria
 *
 */
@Component
public class UserPersonalDataValidator implements Validator {
	
	private UserService userService;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return User.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"persona.nombre","persona.apellido","persona.mail"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Persona persona = (Persona) command;
		if(persona.getMail()!=null && !"".equals(persona.getMail())){
			if(!ValidacionEMail.validar(persona.getMail())){
				errors.rejectValue("persona.mail", "error.errorFormatoEMail");
				return;
			}	
			User user2 = userService.obtenerPorEMail(persona.getMail());
			if(user2!=null && !persona.getId().equals(user2.getPersona().getId()))
				errors.rejectValue("persona.mail", "error.errorEMail");
		}
	}		
}