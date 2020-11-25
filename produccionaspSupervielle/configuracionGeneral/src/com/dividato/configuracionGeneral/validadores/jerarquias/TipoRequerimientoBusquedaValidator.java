package com.dividato.configuracionGeneral.validadores.jerarquias;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.modelo.jerarquias.TipoRequerimiento;

@Component
public class TipoRequerimientoBusquedaValidator implements Validator{

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return TipoRequerimiento.class.isAssignableFrom(type);
	}
	
	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {});
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// TODO Auto-generated method stub		
	}

}
