/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 04/07/2011
 */
package com.dividato.configuracionGeneral.validadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class ListaPreciosDetalleValidator implements Validator{
	private ListaPreciosService service;
	private ConceptoFacturableService conceptoFacturableService;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class type) {
		return ListaPreciosDetalle.class.isAssignableFrom(type);
	}
	
	@Autowired
	public void setService(ListaPreciosService service) {
		this.service = service;
	}
	
	@Autowired
	public void setConceptoFacturableService(
			ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
	}
	
	public void initDataBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[] {"conceptoCodigo", "listaPreciosCodigo","variacionId","valor"});
	}

	@Override
	public void validate(Object object, Errors errors) {
		ListaPreciosDetalle detalle = (ListaPreciosDetalle) object;
		if(detalle != null){
			//valido que exista el codigo de concepto facturable
			ConceptoFacturable c = conceptoFacturableService.obtenerConceptoFacturablePorCodigo(
					detalle.getConceptoCodigo(), obtenerClienteAspUser());
			if(c == null)
				errors.rejectValue("conceptoCodigo", "error.agregarConcepto.codigoConcepto");
			//valido que exista el codigo de lista de precios
			ListaPrecios l = service.obtenerListaPreciosPorCodigo(
					detalle.getListaPreciosCodigo(), obtenerClienteAspUser());
			if(l == null)
				errors.rejectValue("listaPreciosCodigo", "error.agregarConcepto.codigoLista");
			//valido que no se haya seleccionado tipo de varicion "Valor Fijo" y que se haya ingresado un valor negativo
//			BigDecimal valor = detalle.getCalcularMonto();
//			if(valor.compareTo(new BigDecimal(0)) <= 0)
//				errors.rejectValue("valor", "error.agregarConcepto.valorMenorIgualCero");			
			//valido que no exista un detalle para la lista con el mismo concepto facturable
			List<ListaPreciosDetalle> detalles = service.listarDetallesPorListaPreciosConceptoFacturable(
					detalle.getListaPrecios(), detalle.getConceptoFacturable());
			if(detalles != null && !detalles.isEmpty()){
				if(!("MODIFICACION").equals(detalle.getAccion()) || !detalles.get(0).getId().equals(detalle.getId()))
					errors.rejectValue("conceptoCodigo", "error.agregarConcepto.conceptoAsociado");
			}
		}	
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}

}
