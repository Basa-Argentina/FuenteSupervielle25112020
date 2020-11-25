package com.dividato.configuracionGeneral.validadores;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Transporte;
/**
 * 
 * @author Gonzalo Noriega
 *
 */
@Component
public class PosicionLibreBusquedaValidator implements Validator {
	
	/**
	 * Indica cual es la clase que este validador puede validar.
	 */
	@Override
	public boolean supports(Class type) {
		return Posicion.class.isAssignableFrom(type);
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"posDesdeVert","posDesdeHor","posHastaVert","posHastaHor","codigoDesdeEstante","codigoHastaEstante"});
	}
	/**
	 * Valida los valores de las propiedades del objeto.
	 */
	@Override
	public void validate(Object command, Errors errors) {
		Posicion posicion = (Posicion) command;
		if(posicion.getCodigoDesdeEstante()!= null && posicion.getCodigoDesdeEstante()!=""
			&& posicion.getCodigoHastaEstante()!= null && posicion.getCodigoHastaEstante()!=""){
			if(Long.valueOf(posicion.getCodigoDesdeEstante())>Long.valueOf(posicion.getCodigoHastaEstante())){
				errors.rejectValue("codigoDesdeEstante", "formularioPosicionLibre.errorCodigoDesde");
			}
		}
		if(posicion.getCodigoDesdeModulo()!= null && posicion.getCodigoDesdeModulo()!= ""
				&& posicion.getCodigoHastaModulo()!= null && posicion.getCodigoHastaModulo()!= ""){
			if(Long.valueOf(posicion.getCodigoDesdeModulo())>Long.valueOf(posicion.getCodigoHastaModulo())){
				errors.rejectValue("codigoDesdeModulo", "formularioPosicionLibre.errorCodigoModuloDesde");
			}
		}
		
		if(posicion.getCodigoDesde()!= null && posicion.getCodigoDesde()!= ""
			&& posicion.getCodigoHasta()!= null && posicion.getCodigoHasta()!= ""){
		if(Long.valueOf(posicion.getCodigoDesde())>Long.valueOf(posicion.getCodigoHasta())){
			errors.rejectValue("codigoDesde", "formularioPosicionLibre.errorCodigoDesde");
		}
	}
	}	
	
}