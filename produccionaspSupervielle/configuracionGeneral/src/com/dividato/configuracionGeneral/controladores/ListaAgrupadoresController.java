/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
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

import com.dividato.configuracionGeneral.validadores.AgrupadorBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.AgrupadorService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AgrupadorFacturacion;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de agrupadores.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarAgrupador.html",
				"/mostrarAgrupador.html",
				"/eliminarAgrupador.html",
				"/filtrarAgrupador.html"
			}
		)
public class ListaAgrupadoresController {
	private AgrupadorService agrupadorService;
	private AgrupadorBusquedaValidator validator;
	
	@Autowired
	public void setAgrupadorService(AgrupadorService agrupadorService) {
		this.agrupadorService = agrupadorService;
	}

	@Autowired
	public void setValidator(AgrupadorBusquedaValidator validator) {
		this.validator = validator;
	}

	@RequestMapping(value="/iniciarAgrupador.html", method = RequestMethod.GET)
	public String iniciarAgrupador(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("agrupadorBusqueda");
		return "redirect:mostrarAgrupador.html";
	}
	
	@RequestMapping(value="/mostrarAgrupador.html", method = RequestMethod.GET)
	public String mostrarAgrupador(HttpSession session, Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<AgrupadorFacturacion> agrupadores = null;	
		AgrupadorFacturacion agrupador = (AgrupadorFacturacion) session.getAttribute("agrupadorBusqueda");
		if(agrupador != null)
			agrupadores = agrupadorService.listarAgrupadorFacturacionFiltradas(agrupador, obtenerClienteAspUser());
		else
			agrupadores = agrupadorService.listarAgrupadorFacturacionFiltradas(null, obtenerClienteAspUser());
		
		atributos.put("agrupadores", agrupadores);
		return "consultaAgrupador";
	}
	
	@RequestMapping(value="/filtrarAgrupador.html", method = RequestMethod.POST)
	public String filtrarAgrupador(
			@ModelAttribute("agrupadorBusqueda") AgrupadorFacturacion agrupador, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(agrupador, result);
		if(!result.hasErrors()){
			session.setAttribute("agrupadorBusqueda", agrupador);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarAgrupador(session, atributos);
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Agrupador.
	 * 
	 * @param idAgrupador el id de Agrupador a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarAgrupador.html",
			method = RequestMethod.GET
		)
	public String eliminarAgrupador(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos){
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la agrupador para eliminar luego
		AgrupadorFacturacion agrupador = agrupadorService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la agrupador
		commit = agrupadorService.eliminarAgrupadorFacturacion(agrupador);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.agrupador.agrupadorEliminado", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarAgrupador(session, atributos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
