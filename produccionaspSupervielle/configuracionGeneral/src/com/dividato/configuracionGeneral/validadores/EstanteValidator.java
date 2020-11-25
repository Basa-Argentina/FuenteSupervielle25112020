package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.EstanteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Estante;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class EstanteValidator implements Validator {	
	private EstanteService estanteService;
	
	@Autowired
	public void setEstanteService(EstanteService estanteService) {
		this.estanteService = estanteService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Estante.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {
				"codigo"});
	}
	
	
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Estante estante = (Estante)command;
		if(estante.getAccion().equals("NUEVO")){
			Estante exists = estanteService.verificarEstante(estante, obtenerClienteAspUser());
			if(exists != null)
				errors.rejectValue("codigo", "formularioEstante.errorCodigo");			
			
		}
		try{
			Integer.valueOf(estante.getCodigo());
		}catch (NumberFormatException e) {
			errors.rejectValue("codigo", "formularioEstante.errorFormat");
			estante.setCodigo("0");
		}
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}