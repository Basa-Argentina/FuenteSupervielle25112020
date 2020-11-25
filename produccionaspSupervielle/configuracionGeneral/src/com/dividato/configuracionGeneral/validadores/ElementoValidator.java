package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class ElementoValidator implements Validator {	
	private ElementoService elementoService;
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Elemento.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"codigo", /*"codigoCliente",*/ "codigoTipoElemento"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		Elemento elemento = (Elemento) command;
		elemento.setClienteAsp(obtenerClienteAspUser());
		if(elemento.getAccion().equals("NUEVO")){
			
			Elemento exists = elementoService.verificarExistente(elemento);
			if(exists != null){
				errors.rejectValue("codigo", "formularioElemento.errorClavePrimaria");
			}
			
			if(elemento.getCantidad() != null && elemento.getCantidad() >= 1)
			{
				String codigo = elemento.getCodigo().substring(2);
				int codigoElemento = Integer.valueOf(codigo);
				int cantidad = elemento.getCantidad();
				int hasta = codigoElemento + cantidad;
				String codigoFinal = String.valueOf(hasta);
				int cantNumeros = codigoFinal.length();
				int faltan = 10 - cantNumeros;
				for(int f = 0; f<faltan ; f++)
				{
					codigoFinal= "0" + codigoFinal;
				}
				codigoFinal = elemento.getTipoElemento().getPrefijoCodigo() + codigoFinal;
				Elemento existe = elementoService.verificarExistenteeEnRango(elemento, elemento.getCodigo(), codigoFinal);
				if(existe != null){
					errors.rejectValue("cantidad", "formularioElemento.errorCodigoRango");
				}
			}
		}
		
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}