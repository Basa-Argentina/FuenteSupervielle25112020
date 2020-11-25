package com.dividato.configuracionGeneral.validadores.jerarquias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.jerarquias.interfaz.TipoRequerimientoService;
import com.security.modelo.jerarquias.TipoRequerimiento;

@Component
public class TipoRequerimientoValidator implements Validator{
	private TipoRequerimientoService service;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return TipoRequerimiento.class.isAssignableFrom(type);
	}
	
	@Autowired	
	public void setService(TipoRequerimientoService service) {
		this.service = service;
	}
	
	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"codigo","descripcion","plazo"});
	}

	@Override
	public void validate(Object obj, Errors errors) {	
		TipoRequerimiento tipo = (TipoRequerimiento) obj;
		//valido que no se repita el codigo
		if(service.seRepiteCodigoTipoRequerimiento(tipo, tipo.getClienteAsp()))
			errors.rejectValue("codigo", "error.tipoRequerimiento.codigoDuplicado");
	}

}
