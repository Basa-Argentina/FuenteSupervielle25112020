/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.dividato.configuracionGeneral.validadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.dividato.configuracionGeneral.objectForms.ImpuestoBusquedaForm;
import com.security.accesoDatos.configuraciongeneral.interfaz.ImpuestoService;
import com.security.modelo.configuraciongeneral.Impuesto;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class ImpuestoFormValidator implements Validator{
	private ImpuestoService impuestoService;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {		
		return ImpuestoBusquedaForm.class.isAssignableFrom(type);
	}
	
	@Autowired
	public void setImpuestoService(ImpuestoService impuestoService) {
		this.impuestoService = impuestoService;
	}


	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"codigo", "descripcion", "alicuota"});
	}

	@Override
	public void validate(Object obj, Errors errors) {		
		ImpuestoBusquedaForm imp = (ImpuestoBusquedaForm) obj;
		if(imp.getAccion() != null){	 
			List<Impuesto> impuestos = impuestoService.listarImpuestos(imp.getCodigo(), null, imp.getCliente());
			if(!impuestos.isEmpty() && (!("MODIFICACION").equals(imp.getAccion()) || !impuestos.get(0).getId().equals(imp.getId())))
				errors.rejectValue("codigo", "error.errorCodigo");	
			if(imp.getAlicuota().doubleValue()<0 || imp.getAlicuota().doubleValue()>100)
				errors.rejectValue("alicuota", "error.errorAlicuota");		
		}
	}

}
