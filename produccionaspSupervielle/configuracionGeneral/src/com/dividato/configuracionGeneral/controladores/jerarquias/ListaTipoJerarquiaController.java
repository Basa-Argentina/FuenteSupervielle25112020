/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.jerarquias.TipoJerarquiaBusquedaValidator;
import com.security.accesoDatos.jerarquias.interfaz.JerarquiaService;
import com.security.accesoDatos.jerarquias.interfaz.TipoJerarquiaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.jerarquias.Jerarquia;
import com.security.modelo.jerarquias.TipoJerarquia;
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
				"/iniciarTipoJerarquia.html",
				"/mostrarTipoJerarquia.html",
				"/eliminarTipoJerarquia.html",
				"/eliminarJerarquia.html",
				"/filtrarTipoJerarquia.html"
			}
		)
public class ListaTipoJerarquiaController {
	private TipoJerarquiaService tipoJerarquiaService;
	private JerarquiaService jerarquiaService;
	private TipoJerarquiaBusquedaValidator validator;
	
	
	@Autowired
	public void setTipoJerarquiaService(TipoJerarquiaService TipoJerarquiaService) {
		this.tipoJerarquiaService = TipoJerarquiaService;
	}
	
	@Autowired
	public void setJerarquiaService(JerarquiaService jerarquiaService) {
		this.jerarquiaService = jerarquiaService;
	}

	@Autowired
	public void setValidator(TipoJerarquiaBusquedaValidator validator) {
		this.validator = validator;
	}
	

	@RequestMapping(value="/iniciarTipoJerarquia.html", method = RequestMethod.GET)
	public String iniciar(HttpSession session, Map<String,Object> atributos){		
		atributos.remove("tipoJerarquiaBusqueda");
		return "redirect:mostrarTipoJerarquia.html";
	}
	
	@RequestMapping(value="/mostrarTipoJerarquia.html", method = RequestMethod.GET)
	public String mostrar(
			@RequestParam(value="id",required=false) Long id,
			HttpSession session, Map<String,Object> atributos){
		//buscamos en la base de datos los conceptos filtrados (o no) y lo cargamos a request.
		List<TipoJerarquia> tipos = null;	
		TipoJerarquia busqueda = (TipoJerarquia) atributos.get("tipoJerarquiaBusqueda");
			atributos.put("hdn_idTipoJerarquia", id);
		if(busqueda != null)
			tipos = tipoJerarquiaService.listarTipoJerarquia(busqueda.getCodigo(), busqueda.getDescripcion(), obtenerClienteAspUser());
		else
			tipos = tipoJerarquiaService.listarTipoJerarquia(null, null, obtenerClienteAspUser());
		atributos.put("tipos", tipos);
		//busco la lista seleccionada
		if(id != null){
			TipoJerarquia tipoSel = tipoJerarquiaService.getByID(id);
			if(tipoSel != null)
				atributos.put("tipoSel", tipoSel);
		}
		//hacemos la redireccion a la ventana
		return "consultaTipoJerarquia";
	}
	
	@RequestMapping(value="/filtrarTipoJerarquia.html", method = RequestMethod.POST)
	public String filtrar(
			@ModelAttribute("tipoJerarquiaBusqueda") TipoJerarquia busqueda, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(busqueda, result);		
		if(!result.hasErrors()){
			atributos.put("tipoJerarquiaBusqueda", busqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrar(null, session, atributos);
	}
	
	@RequestMapping(value="/eliminarTipoJerarquia.html", method = RequestMethod.GET)
	public String eliminar(
			HttpSession session,
			@RequestParam("id") Long id,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//obtenemos la lista
		TipoJerarquia lista = tipoJerarquiaService.obtenerPorId(id);
		//eliminamos
		commit =tipoJerarquiaService.delete(lista);
		
		ScreenMessage mensaje;
		if(commit){
			mensaje = new ScreenMessageImp("notif.tipoJerarquia.eliminado", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.commitDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrar(null, session,atributos);
	}
	
	@RequestMapping(value="/eliminarJerarquia.html", method = RequestMethod.GET)
	public String eliminarJerarquia(
			HttpSession session,
			@RequestParam("jerarquiaId") Long id,
			Map<String,Object> atributos) {
		
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//obtenemos la lista
		Jerarquia jeraquia = jerarquiaService.obtenerPorId(id);
		//eliminamos
		commit =jerarquiaService.delete(jeraquia);
		
		ScreenMessage mensaje;
		if(commit){
			mensaje = new ScreenMessageImp("notif.jerarquia.eliminada", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.commitDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrar(null, session,atributos);
	}	
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
