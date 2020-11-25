package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class ClienteDireccionValidator implements Validator {	
	private ClienteDireccionService clienteDireccionService;
	
	@Autowired
	public void setClienteDireccionService(ClienteDireccionService clienteDireccionService) {
		this.clienteDireccionService = clienteDireccionService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return ClienteDireccion.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"clienteCodigo", "codigo", "descripcion", "direccion.calle","direccion.numero"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		ClienteDireccion clienteDireccion = (ClienteDireccion) command;
		if(clienteDireccion.getAccion().equals("NUEVO")){
			ClienteDireccion exists = clienteDireccionService.verificarExistente(clienteDireccion, obtenerClienteAspUser());
			if(exists != null)
				errors.rejectValue("codigo", "formularioClienteDireccion.errorClavePrimaria");	
			if(clienteDireccion.getDireccion().getCalle() == null || clienteDireccion.getDireccion().getCalle().equals("")){	
				errors.rejectValue("direccion.calle", "formularioClienteDireccion.errorCalle");
			}
			if(clienteDireccion.getDireccion().getNumero() == null || clienteDireccion.getDireccion().getNumero().equals("")){	
				errors.rejectValue("direccion.numero", "formularioClienteDireccion.errorNumero");
			}
		}
		if(clienteDireccion.getIdBarrio() == null || clienteDireccion.getIdBarrio() == 0){	
			errors.rejectValue("idBarrio", "required");
		}
		if(clienteDireccion.getObservacion() != null && !clienteDireccion.getObservacion().equalsIgnoreCase("") && clienteDireccion.getObservacion().length()>500){
			errors.rejectValue("direccion.observaciones", "formularioClienteDireccion.errorObservaciones");
		}
		
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}