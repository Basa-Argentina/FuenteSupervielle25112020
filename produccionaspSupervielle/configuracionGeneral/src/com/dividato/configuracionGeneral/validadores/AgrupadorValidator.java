package com.dividato.configuracionGeneral.validadores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.AgrupadorService;
import com.security.modelo.configuraciongeneral.AgrupadorFacturacion;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class AgrupadorValidator implements Validator {	
	private AgrupadorService agrupadorService;
	
	@Autowired
	public void setAgrupadorService(AgrupadorService agrupadorService) {
		this.agrupadorService = agrupadorService;
	}
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return AgrupadorFacturacion.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"clienteCodigo",
				"codigo","descripcion"
		});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		//aquí irían validaciones sobre los valores del objeto.
		//de los campos requeridos y las conversiones se encarga el WebDataBinder 
		AgrupadorFacturacion agrupador = (AgrupadorFacturacion)command;
		if(agrupador.getAccion().equals("NUEVO")){
			String codigo = agrupador.getCodigo();
			agrupador.setCodigo("");
			AgrupadorFacturacion clienteAndTipoAgrupador = agrupadorService.verificarExistente(agrupador);
			agrupador.setCodigo(codigo);
			AgrupadorFacturacion exists = agrupadorService.verificarExistente(agrupador);
			if(clienteAndTipoAgrupador != null){
				errors.rejectValue("tipoAgrupador", "formularioAgrupador.errorCliente");
			}
			if(exists != null)
				errors.rejectValue("codigo", "formularioAgrupador.errorCodigo");			
			
		}
		if(agrupador.getTipoAgrupador() == null || "".equals(agrupador.getTipoAgrupador())){
			errors.rejectValue("tipoAgrupador", "required");
		}
	}
	
	
}