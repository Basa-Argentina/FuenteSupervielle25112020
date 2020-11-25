/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.SerieBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de series.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega 
 * @modificado Victor Kenis (15/08/2011)
 * 
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarSerie.html",
				"/mostrarSerie.html",
				"/eliminarSerie.html",
				"/filtrarSerie.html",
				"/siguienteCodigo.html"
			}
		)
public class ListaSeriesController {
	private SerieService serieService;
	private EmpresaService empresaService;
	private SerieBusquedaValidator validator;
	
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}

	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setValidator(SerieBusquedaValidator validator) {
		this.validator = validator;
	}

	@RequestMapping(value="/iniciarSerie.html", method = RequestMethod.GET)
	public String iniciarSerie(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("serieBusqueda");
		return "redirect:mostrarSerie.html";
	}
	
	@RequestMapping(value="/mostrarSerie.html", method = RequestMethod.GET)
	public String mostrarSerie(HttpSession session, Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<Serie> series = null;	
		Serie serie = (Serie) session.getAttribute("serieBusqueda");
		if(serie != null)
			series = serieService.listarSerieFiltradas(serie, obtenerClienteAspUser());
		else
			series = serieService.listarSerieFiltradas(null, obtenerClienteAspUser());
		
		atributos.put("series", series);
		return "consultaSerie";
	}
	
	@RequestMapping(value="/filtrarSerie.html", method = RequestMethod.POST)
	public String filtrarSerie(
			@ModelAttribute("serieBusqueda") Serie serie, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(serie, result);
		if(!result.hasErrors()){
			session.setAttribute("serieBusqueda", serie);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarSerie(session, atributos);
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Serie.
	 * 
	 * @param idDireccion el id de Empresa a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarSerie.html",
			method = RequestMethod.GET
		)
	public String eliminarSerie(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la serie para eliminar luego
		ScreenMessage mensaje;
		Serie serie = serieService.obtenerPorId(Long.valueOf(id));
		Empresa empresa = empresaService.obtenerPorId(serie.getEmpresa().getId());
		if(empresa.getSerie1().getId().equals(serie.getId()) || empresa.getSerie2().getId().equals(serie.getId()))
		{
			mensaje = new ScreenMessageImp("formularioSerie.errorEliminarSerie1PorDefecto", null);
			hayAvisosNeg = true;
			avisos.add(mensaje);
			atributos.put("hayAvisosNeg", hayAvisosNeg);
			atributos.put("hayAvisos", hayAvisos);
			atributos.put("avisos", avisos);
			return mostrarSerie(session, atributos);
		}
		//Eliminamos la serie
		commit = serieService.eliminarSerie(serie);
		
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.serie.serieEliminada", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteSerie", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarSerie(session, atributos);
	}
	
	@RequestMapping(
			value="/siguienteCodigo.html",
			method = RequestMethod.GET
		)
	public void siguienteCodigo(HttpSession session,
			Map<String,Object> atributos,
			HttpServletResponse response,
			HttpServletRequest request) {
		try {
				List<Serie> series = serieService.listarSerieFiltradas(null, obtenerClienteAspUser());
				if(!series.isEmpty()){
					Serie s = (Serie)series.get(series.size()-1);
					Long codigo = Long.valueOf(s.getCodigo())+1;
					response.getWriter().write(String.valueOf(codigo));
				}else{
					response.getWriter().write("");
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
