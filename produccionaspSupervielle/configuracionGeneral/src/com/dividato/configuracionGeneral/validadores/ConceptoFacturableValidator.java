/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/06/2011
 */
package com.dividato.configuracionGeneral.validadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class ConceptoFacturableValidator implements Validator{
	private ConceptoFacturableService conceptoFacturableService;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return ConceptoFacturable.class.isAssignableFrom(type);
	}
	
	@Autowired	
	public void setConceptoFacturableService(
			ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
	}



	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"codigo","descripcion","costo","precioBase","impuestoCodigo"});
	}

	@Override
	public void validate(Object obj, Errors errors) {	
		// TODO Auto-generated method stub		
	}
	
	public void validateRegMod(Object obj, Errors errors) {	
		ConceptoFacturable c = (ConceptoFacturable) obj;
		//valido que no se repita el codigo de concepto facturable
		List<ConceptoFacturable> conceptos = conceptoFacturableService.listarPorFiltro(null, c.getCodigo(), null, null, null, null, c.getClienteAsp());
		if(conceptos != null && !conceptos.isEmpty()){
			if(!("MODIFICACION").equals(c.getAccion()) || !conceptos.get(0).getId().equals(c.getId()))
				errors.rejectValue("codigo", "error.conceptoFacturable.errorCodigo");			
		}
			
	}

}
