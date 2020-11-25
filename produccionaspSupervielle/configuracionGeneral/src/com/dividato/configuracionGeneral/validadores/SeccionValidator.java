package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.SeccionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Seccion;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class SeccionValidator implements Validator {
	
	private SeccionService seccionService;
	
	@Autowired
	public void setSeccionService(SeccionService seccionService) {
		this.seccionService = seccionService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Seccion.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"codigo","descripcion","codigoDeposito"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Seccion seccion = (Seccion) command;
		if(seccion.getAccion().equals("NUEVO")){	
			Seccion exists= seccionService.verificarSeccion(seccion, obtenerClienteAspUser());
			if(exists != null)
				errors.rejectValue("codigo", "formularioSeccion.errorCodigo");					
			
		}
		try{
			Integer.valueOf(seccion.getCodigo());
		}catch (NumberFormatException e) {
			errors.rejectValue("codigo", "formularioSeccion.errorFormat");
			seccion.setCodigo("0");
		}
		
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}