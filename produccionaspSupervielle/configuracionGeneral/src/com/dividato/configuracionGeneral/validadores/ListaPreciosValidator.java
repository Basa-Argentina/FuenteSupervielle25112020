/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
 */
package com.dividato.configuracionGeneral.validadores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 * @modificado Victor Kenis (16/08/2011)
 *
 */
@Component
public class ListaPreciosValidator implements Validator {
	private ListaPreciosService service;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return ListaPrecios.class.isAssignableFrom(type);
	}

	@Autowired
	public void setService(ListaPreciosService service) {
		this.service = service;
	}

	/**
	 * Inicializa el WebDataBinder con los campos requeridos y las conversiones de tipos de datos.
	 * @param binder
	 */
	public void initDataBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, "fechaVencimiento", new CustomDateEditor(formatoFechaFormularios,true));
		binder.setRequiredFields(new String[] {"codigo", "descripcion","tipoVariacionId","valor"});
	}

	@Override
	public void validate(Object object, Errors arg1) {
//		ListaPrecios lista = (ListaPrecios) object;
		
	}
	
	public void validateRegMod(Object object, Errors errors) {
		ListaPrecios lista = (ListaPrecios) object;
		if(lista != null)
		{
			//valido que el codigo ingresado sea unico
			List<ListaPrecios> listas = service.listarListasPrecios(lista.getCodigo(), null, null, obtenerClienteAspUser());
			if(listas != null && !listas.isEmpty())
			{
				if(!("MODIFICACION").equals(lista.getAccion()) || !listas.get(0).getId().equals(lista.getId()))
				{
					errors.rejectValue("codigo", "error.listaPrecios.errorCodigo");
				}
			}
			//valido que la fecha de vencimiento sea mayor al dia de la fecha para los nuevos registros
			if(!("MODIFICACION").equals(lista.getAccion())){
				if(lista.getFechaVencimiento() != null)
				{
					Date fechaHoy = new Date();
					if(lista.getFechaVencimiento().before(fechaHoy))
					{
						errors.rejectValue("fechaVencimiento", "error.listaPrecios.errorFechaVencimiento");
					}
				}
			}
		}		
	}	
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
}
