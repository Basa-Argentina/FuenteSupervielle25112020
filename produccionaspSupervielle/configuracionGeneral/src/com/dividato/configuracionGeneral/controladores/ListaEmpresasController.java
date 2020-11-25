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

import com.dividato.configuracionGeneral.validadores.EmpresaBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de empresas.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarEmpresa.html",
				"/mostrarEmpresa.html",
				"/eliminarEmpresa.html",
				"/filtrarEmpresa.html"
			}
		)
public class ListaEmpresasController {
	private EmpresaService empresaService;
	private EmpresaBusquedaValidator validator;
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@Autowired
	public void setValidator(EmpresaBusquedaValidator validator) {
		this.validator = validator;
	}

	@RequestMapping(value="/iniciarEmpresa.html", method = RequestMethod.GET)
	public String iniciarEmpresa(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("empresaBusqueda");
		return "redirect:mostrarEmpresa.html";
	}
	
	@RequestMapping(value="/mostrarEmpresa.html", method = RequestMethod.GET)
	public String mostrarEmpresa(HttpSession session, Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<Empresa> empresas = null;	
		Empresa empresa = (Empresa) session.getAttribute("empresaBusqueda");
		if(empresa != null)
			empresas = empresaService.listarEmpresaFiltradas(empresa, obtenerClienteAspUser());
		else
			empresas = empresaService.listarEmpresaFiltradas(null, obtenerClienteAspUser());
		
		atributos.put("empresas", empresas);
		return "consultaEmpresa";
	}
	
	@RequestMapping(value="/filtrarEmpresa.html", method = RequestMethod.POST)
	public String filtrarEmpresa(
			@ModelAttribute("empresaBusqueda") Empresa empresa, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(empresa, result);
		if(!result.hasErrors()){
			session.setAttribute("empresaBusqueda", empresa);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarEmpresa(session, atributos);
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Empresa.
	 * 
	 * @param idDireccion el id de Empresa a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarEmpresa.html",
			method = RequestMethod.GET
		)
	public String eliminarEmpresa(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la empresa para eliminar luego
		Empresa empresa = empresaService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la empresa
		commit = empresaService.eliminarEmpresa(empresa);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.empresa.empresaEliminada", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarEmpresa(session, atributos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
