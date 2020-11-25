package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteConceptoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteConcepto;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class ClienteConceptoValidator implements Validator {	
	private ClienteConceptoService clienteConceptoService;
	
	@Autowired
	public void setClienteConceptoService(ClienteConceptoService clienteConceptoService) {
		this.clienteConceptoService = clienteConceptoService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return ClienteConcepto.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"clienteCodigo", "listaPrecioCodigo", "conceptoCodigo"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		ClienteConcepto clienteConcepto = (ClienteConcepto) command;
		if(clienteConcepto.getAccion().equals("NUEVO")){
			ClienteConcepto exists = clienteConceptoService.verificarExistente(clienteConcepto, obtenerClienteAspUser());
			if(exists != null)
				errors.rejectValue("conceptoCodigo", "formularioClienteConcepto.errorClavePrimaria");
		}
		
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}