package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class DepositoValidator implements Validator {
	
	private DepositoService depositoService;
	
	@Autowired
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Deposito.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"codigo","descripcion","codigoSucursal","direccion.calle","direccion.numero"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Deposito deposito = (Deposito) command;
		if(deposito.getAccion().equals("NUEVO")){	
			Deposito exists= depositoService.verificarExistente(deposito,obtenerClienteAspUser());
			if(exists != null)
				errors.rejectValue("codigo", "formularioDeposito.errorCodigo");					
			
		}
		try{
			Integer.valueOf(deposito.getCodigo());
		}catch (NumberFormatException e) {
			errors.rejectValue("codigo", "formularioDeposito.errorFormat");
			deposito.setCodigo("0");
		}
		if(deposito.getIdBarrio() == null || deposito.getIdBarrio() == 0){	
			errors.rejectValue("idBarrio", "required");
		}
		
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}