/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/07/2011
 */
package com.dividato.configuracionGeneral.validadores.jerarquias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.jerarquias.interfaz.JerarquiaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.jerarquias.Jerarquia;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class JerarquiaValidator implements Validator{
	private JerarquiaService service;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return Jerarquia.class.isAssignableFrom(type);
	}
	
	@Autowired
	public void setService(JerarquiaService service) {
		this.service = service;
	}
	
	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {
				"descripcion","valoracion",
				"horizontalDesde","horizontalHasta",
				"verticalDesde","verticalHasta"});
	}

	@Override
	public void validate(Object obj, Errors errors) {
		boolean bandera = true;
		Jerarquia jerarquia = (Jerarquia) obj;
		//valido que la descripcion de la jerarquia se diferente a de las demas jerarquias del tipo
		if(service.seRepiteDescripcion(jerarquia, jerarquia.getTipo(), obtenerClienteAspUser()))
			errors.rejectValue("descripcion", "error.jerarquia.descripcion");
		//valido que los valores Desde sean menores o iguales que los valores Hasta y mayores a cero
		if(jerarquia.getVerticalDesde() < 1 || jerarquia.getVerticalDesde() >= jerarquia.getVerticalHasta()){
			errors.rejectValue("verticalDesde", "error.jerarquia.rangoVerticalIncorrecto");
			bandera = false;
		}	
		if(jerarquia.getHorizontalDesde() < 1 || jerarquia.getHorizontalDesde() >= jerarquia.getHorizontalHasta()){	
			errors.rejectValue("horizontalDesde", "error.jerarquia.rangoHastaIncorrecto");
			bandera = false;
		}	
		//Valido que no este repetida la valoracion en un mismo tipo de jerarquia y que no sea menor a 0
		if(jerarquia.getValoracion() < 0)
			errors.rejectValue("valoracion", "error.jerarquia.valoracionMenorCero");
		else if(service.seRepiteValoracion(jerarquia, jerarquia.getTipo(), obtenerClienteAspUser()))
			errors.rejectValue("valoracion", "error.jerarquia.valoracion");
		if(bandera){	
			//valido que no se superponga las posiciones dentro del mismo tipo de jerarquia
			if(service.seSuperPonenJerarquias(jerarquia, jerarquia.getTipo(), obtenerClienteAspUser()))
				errors.rejectValue("verticalDesde", "error.jerarquia.superposicion");
		}	
	}	

	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
