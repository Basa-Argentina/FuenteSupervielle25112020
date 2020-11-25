/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.objectForms.ImpuestoBusquedaForm;
import com.dividato.configuracionGeneral.validadores.ImpuestoFormValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ImpuestoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Impuesto;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * @author Ezequiel Beccaria
 *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarImpuesto.html",
				"/mostrarImpuesto.html",
				"/eliminarImpuesto.html",
				"/filtrarImpuesto.html"
			}
		)
public class ListaImpuestosController {
	private ImpuestoService impuestoService;
	private ImpuestoFormValidator validator;
	
	@Autowired
	public void setImpuestoService(ImpuestoService impuestoService) {
		this.impuestoService = impuestoService;
	}
	@Autowired
	public void setValidator(ImpuestoFormValidator validator) {
		this.validator = validator;
	}

	@RequestMapping(value="/iniciarImpuesto.html", method = RequestMethod.GET)
	public String iniciar(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("ImpuestoBusqueda");
		return "redirect:mostrarImpuesto.html";
	}
	
	@RequestMapping(value="/mostrarImpuesto.html", method = RequestMethod.GET)
	public String mostrar(HttpSession session, Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<Impuesto> impuestos = null;	
		ImpuestoBusquedaForm impuestoBusqueda = (ImpuestoBusquedaForm) atributos.get("impuestoBusqueda");
		if(impuestoBusqueda != null)
			impuestos = impuestoService.listarImpuestos(impuestoBusqueda.getCodigo(), impuestoBusqueda.getDescripcion(), obtenerClienteAspUser()); 
		else
			impuestos = impuestoService.listarImpuestos(null, null, obtenerClienteAspUser());
		atributos.put("impuestos", impuestos);
		return "consultaImpuesto";
	}
	
	@RequestMapping(value="/filtrarImpuesto.html", method = RequestMethod.POST)
	public String filtrar(
			@ModelAttribute("impuestoBusqueda") ImpuestoBusquedaForm impuestoBusqueda, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(impuestoBusqueda, result);
		if(!result.hasErrors()){
			session.setAttribute("impuestoBusqueda", impuestoBusqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrar(session, atributos);
	}
	
	@RequestMapping(value="/eliminarImpuesto.html", method = RequestMethod.GET)
	public String eliminar(
			HttpSession session,
			@RequestParam("id") Long id,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//obtenemos el impuesto
		Impuesto impuesto = impuestoService.obtenerPorId(id);
		//eliminamos user
		commit = impuestoService.delete(impuesto);
		
		ScreenMessage mensaje;
		if(commit){
			mensaje = new ScreenMessageImp("notif.impuesto.eliminado", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.commitDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrar(session,atributos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
