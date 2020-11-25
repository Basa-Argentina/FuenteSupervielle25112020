package com.dividato.security.validadores;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.interfaz.AuthorityService;
import com.security.accesoDatos.interfaz.CampoComparacion;
import com.security.modelo.seguridad.Authority;
/**
 * 
 * @author Gabriel Mainero
 *
 */
@Component
public class AuthorityValidator implements Validator {
	
	private AuthorityService authorityService;
	
	@Autowired
	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Authority.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"authority"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		Authority authority = (Authority) command;
		List<Authority> listaSalida = authorityService.listarTodosFiltradoPorLista(new CampoComparacion("authority",authority.getAuthority()));
		if(listaSalida.size()>0){	
			errors.rejectValue("authority", "formularioAuthority.errorClavePrimaria");
		}
	}	
			
}