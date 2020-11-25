/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/06/2011
 */
package com.dividato.configuracionGeneral.controladores.jerarquias;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.jerarquias.TipoRequerimientoBusquedaValidator;
import com.security.accesoDatos.jerarquias.interfaz.TipoRequerimientoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.jerarquias.TipoRequerimiento;
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
				"/iniciarTipoRequerimiento.html",
				"/mostrarTipoRequerimiento.html",
				"/eliminarTipoRequerimiento.html",
				"/filtrarTipoRequerimiento.html"
			}
		)
public class ListaTipoRequerimientoController {
	private TipoRequerimientoService tipoRequerimientoService;
	private TipoRequerimientoBusquedaValidator validator;

	@Autowired
	public void setTipoRequerimientoService(TipoRequerimientoService tipoRequerimientoService) {
		this.tipoRequerimientoService = tipoRequerimientoService;
	}

	@Autowired
	public void setValidator(TipoRequerimientoBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}

	@RequestMapping(value="/iniciarTipoRequerimiento.html", method = RequestMethod.GET)
	public String iniciar(HttpSession session, Map<String,Object> atributos){		
		atributos.remove("tipoRequerimientoBusqueda");
		return "redirect:mostrarTipoRequerimiento.html";
	}
	
	@RequestMapping(value="/mostrarTipoRequerimiento.html", method = RequestMethod.GET)
	public String mostrar(HttpSession session, Map<String,Object> atributos){
		//buscamos en la base de datos los conceptos filtrados (o no) y lo cargamos a request.
		List<TipoRequerimiento> tipos = null;	
		TipoRequerimiento busqueda = (TipoRequerimiento) atributos.get("tipoRequerimientoBusqueda");
		if(busqueda != null)
			tipos = tipoRequerimientoService.listarTipoRequerimiento(
					busqueda.getCodigo(), busqueda.getDescripcion(), busqueda.getPlazo(), obtenerClienteAspUser());			
		else
			tipos = tipoRequerimientoService.listarTipoRequerimiento(null, null, null, obtenerClienteAspUser());
		atributos.put("tipos", tipos);		
		//hacemos la redireccion a la ventana
		return "consultaTipoRequerimiento";
	}
	
	@RequestMapping(value="/filtrarTipoRequerimiento.html", method = RequestMethod.POST)
	public String filtrar(
			@ModelAttribute("tipoRequerimientoBusqueda") TipoRequerimiento busqueda, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(busqueda, result);		
		if(!result.hasErrors()){
			atributos.put("tipoRequerimientoBusqueda", busqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrar(session, atributos);
	}
	
	@RequestMapping(value="/eliminarTipoRequerimiento.html", method = RequestMethod.GET)
	public String eliminar(
			HttpSession session,
			@RequestParam("id") Long id,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//obtenemos el impuesto
		TipoRequerimiento tipo = tipoRequerimientoService.obtenerPorId(id);
		
		//verificamos si esta asociado a algun Requerimiento
		List<Object> req = new ArrayList<Object>(); //TODO Listas objectos asociados y verificar que no posea dependencias
		if(!req.isEmpty()){
			avisos.add(new ScreenMessageImp("error.tipoRequerimiento.requerimientoAsociado", null));
			hayAvisosNeg = true;
		}else{
			//eliminamos user
			commit = tipoRequerimientoService.delete(tipo);
			
			ScreenMessage mensaje;
			if(commit){
				mensaje = new ScreenMessageImp("notif.tipoRequerimiento.eliminado", null);
				hayAvisos = true;
			}else{
				mensaje = new ScreenMessageImp("error.commitDataBase", null);
				hayAvisosNeg = true;
			}
			avisos.add(mensaje);
		}	
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrar(session,atributos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
