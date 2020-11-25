/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.security.controladores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.security.validadores.LicenciaValidator;
import com.security.accesoDatos.interfaz.EstadoLicenciaService;
import com.security.accesoDatos.interfaz.LicenciaService;
import com.security.modelo.administracion.EstadoLicencia;
import com.security.modelo.administracion.Licencia;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Clientes.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Ezequiel Beccaria *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarLicencia.html",
				"/mostrarLicencia.html",
				"/eliminarLicencia.html",
				"/filtrarLicencia.html"
			}
		)
public class ListaLicenciaController {
	private LicenciaService licenciaService;
	private LicenciaValidator validator;
	private EstadoLicenciaService estadoLicenciaService;
			
	@Autowired
	public void setLicenciaService(LicenciaService licenciaService) {
		this.licenciaService = licenciaService;
	}
	@Autowired
	public void setEstadoLicenciaService(EstadoLicenciaService estadoLicenciaService) {
		this.estadoLicenciaService = estadoLicenciaService;
	}
	@Autowired
	public void setValidator(LicenciaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(value="/iniciarLicencia.html", method = RequestMethod.GET)
	public String iniciar(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("licenciaBusqueda");
		return "redirect:mostrarLicencia.html";
	}
	
	@RequestMapping(value="/mostrarLicencia.html", method = RequestMethod.GET)
	public String mostrar(HttpSession session, Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<Licencia> licencias = null;	
		Licencia licenciaBusqueda = (Licencia) atributos.get("licenciaBusqueda");
		if(licenciaBusqueda == null){
			licenciaBusqueda = new Licencia();
		}
		licencias = licenciaService.obtenerLicenciaPorFiltroLicencia(licenciaBusqueda);
		atributos.put("licencias", licencias);
		//obtengo los estados
		List<EstadoLicencia> estados = estadoLicenciaService.listarTodos();
		Collections.sort(estados); //ordeno la coleccion a traves del compareTo
		atributos.put("estados", estados);
//		atributos.put("licenciaBusqueda", licenciaBusqueda);
		return "consultaLicencia";
	}
	
	@RequestMapping(value="/filtrarLicencia.html", method = RequestMethod.POST)
	public String filtrar(
			@ModelAttribute("licenciaBusqueda") Licencia licencia, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(licencia, result);
		if(!result.hasErrors()){
			session.setAttribute("licenciaBusqueda", licencia);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrar(session, atributos);
	}
	
	@RequestMapping(value="/eliminarLicencia.html", method = RequestMethod.GET)
	public String eliminar(
			@RequestParam("id") Long id,
			HttpSession session,
			Map<String,Object> atributos){
		//obtengo la licencia a eliminar
		Licencia lic = licenciaService.obtenerPorId(Long.valueOf(id));
		Boolean commit = licenciaService.eliminarLicencia(lic);
		BindingResult result = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		if(commit != null && !commit){
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");	
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
		}
		if(result != null && result.hasErrors()){
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", false);
			atributos.remove("avisos");
		}else{			
			ScreenMessage mensaje = new ScreenMessageImp("notif.licencia.eliminada", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");	
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return mostrar(session, atributos);
	}
}
