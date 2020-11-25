package com.dividato.security.validadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.interfaz.CampoComparacion;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.seguridad.User;
import com.security.recursos.ValidacionEMail;
/**
 * 
 * @author Gabriel Mainero
 *
 */
@Component
public class UserValidator implements Validator {
	
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
		binder.setRequiredFields(new String[] {"username","persona.nombre","persona.apellido","persona.mail"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		User user = (User) command;
		if(user.getAccion().equals("NUEVO")){
			List<User> listaSalida = userService.listarTodosFiltradoPorLista(new CampoComparacion("username",user.getUsername()));
			if(listaSalida.size()>0){	
				errors.rejectValue("username", "error.user.errorClavePrimaria");
			}		
		}
		if(user.getPassword() != null && !"".equals(user.getPassword())){
			if(user.getPassword()==null || user.getPassword().equals(""))
				errors.rejectValue("password", "error.user.errorPassword");
			
			if(!user.getPassword().equals(user.getConfirmarContrasenia()))
				errors.rejectValue("confirmarContrasenia", "error.user.errorDistintasContrasenias");
		}
		if(user.getPersona().getMail()!=null && !"".equals(user.getPersona().getMail())){
			if(!ValidacionEMail.validar(user.getPersona().getMail())){
				errors.rejectValue("persona.mail", "error.user.errorFormatoEMail");
				return;
			}	
			User user2 = userService.obtenerPorEMail(user.getPersona().getMail());
			if(user2!=null && !user.getId().equals(user2.getId()))
				errors.rejectValue("persona.mail", "error.user.errorEMail");
		}
		if(user.getAssignedRoles()==null || "".equals(user.getAssignedRoles()))
			errors.rejectValue("assignedRoles", "error.user.errorAsignacion");
	}		
}