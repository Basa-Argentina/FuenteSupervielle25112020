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

import com.dividato.configuracionGeneral.validadores.SucursalBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de sucursals.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarSucursal.html",
				"/mostrarSucursal.html",
				"/eliminarSucursal.html",
				"/filtrarSucursal.html"
			}
		)
public class ListaSucursalController {
	private SucursalService sucursalService;
	private SucursalBusquedaValidator validator;
	private EmpresaService empresaService;
	
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@Autowired
	public void setValidator(SucursalBusquedaValidator validator) {
		this.validator = validator;
	}

	@RequestMapping(value="/iniciarSucursal.html", method = RequestMethod.GET)
	public String iniciarSucursal(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("sucursalBusqueda");
		return "redirect:mostrarSucursal.html";
	}
	
	@RequestMapping(value="/mostrarSucursal.html", method = RequestMethod.GET)
	public String mostrarSucursal(HttpSession session, Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<Sucursal> sucursales = null;	
		Sucursal sucursal = (Sucursal) session.getAttribute("sucursalBusqueda");
		if(sucursal != null)
			sucursales = sucursalService.listarSucursalFiltradas(sucursal, obtenerClienteAspUser());
		else
			sucursales = sucursalService.listarSucursalFiltradas(null, obtenerClienteAspUser());
		
		//traemos todas las empresas
		List<Empresa> empresas = empresaService.listarEmpresaFiltradas(null, obtenerClienteAspUser());
		atributos.put("empresas", empresas);
		
		atributos.put("sucursales", sucursales);
		return "consultaSucursal";
	}
	
	@RequestMapping(value="/filtrarSucursal.html", method = RequestMethod.POST)
	public String filtrarSucursal(
			@ModelAttribute("sucursalBusqueda") Sucursal sucursal, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(sucursal, result);
		if(!result.hasErrors()){
			session.setAttribute("sucursalBusqueda", sucursal);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarSucursal(session, atributos);
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Sucursal.
	 * 
	 * @param idDireccion el id de Sucursal a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarSucursal.html",
			method = RequestMethod.GET
		)
	public String eliminarSucursal(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la sucursal para eliminar luego
		Sucursal sucursal = sucursalService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la sucursal
		commit = sucursalService.eliminarSucursal(sucursal);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.sucursal.sucursalEliminada", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarSucursal(session, atributos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
