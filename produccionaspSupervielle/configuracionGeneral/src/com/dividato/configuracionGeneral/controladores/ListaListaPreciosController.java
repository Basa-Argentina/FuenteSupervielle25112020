/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
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

import com.dividato.configuracionGeneral.validadores.ListaPreciosValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
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
				"/iniciarListaPrecios.html",
				"/mostrarListaPrecios.html",
				"/eliminarListaPrecios.html",
				"/eliminarAsociacionListaPrecios.html",
				"/filtrarListaPrecios.html"
			}
		)
public class ListaListaPreciosController {
	private ListaPreciosService listaPreciosService;
	private ListaPreciosValidator validator;
	
	
	@Autowired
	public void setListaPreciosService(ListaPreciosService listaPreciosService) {
		this.listaPreciosService = listaPreciosService;
	}
	@Autowired
	public void setValidator(ListaPreciosValidator validator) {
		this.validator = validator;
	}
	

	@RequestMapping(value="/iniciarListaPrecios.html", method = RequestMethod.GET)
	public String iniciar(HttpSession session, Map<String,Object> atributos){		
		atributos.remove("ListaPreciosBusqueda");
		return "redirect:mostrarListaPrecios.html";
	}
	
	@RequestMapping(value="/mostrarListaPrecios.html", method = RequestMethod.GET)
	public String mostrar(
			@RequestParam(value="id",required=false) Long id,
			HttpSession session, Map<String,Object> atributos){
		//buscamos en la base de datos los conceptos filtrados (o no) y lo cargamos a request.
		List<ListaPrecios> listas = null;	
		ListaPrecios busqueda = (ListaPrecios) atributos.get("listaPreciosBusqueda");
		if(busqueda != null)
			listas = listaPreciosService.listarListasPrecios(
					busqueda.getCodigo(), busqueda.getDescripcion(), 
					busqueda.getTipoVariacion(), obtenerClienteAspUser());
		else
			listas = listaPreciosService.listarListasPrecios(null, null, null, obtenerClienteAspUser());
		atributos.put("listas", listas);
		//busco la lista seleccionada
		if(id != null){
			ListaPrecios listaSel = listaPreciosService.getByID(id);
			if(listaSel != null)
				atributos.put("listaSel", listaSel);
		}
		//hacemos la redireccion a la ventana
		return "consultaListaPrecios";
	}
	
	@RequestMapping(value="/filtrarListaPrecios.html", method = RequestMethod.POST)
	public String filtrar(
			@ModelAttribute("impuestoBusqueda") ListaPrecios busqueda, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(busqueda, result);		
		if(!result.hasErrors()){
			atributos.put("listaPreciosBusqueda", busqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrar(null, session, atributos);
	}
	
	@RequestMapping(value="/eliminarListaPrecios.html", method = RequestMethod.GET)
	public String eliminar(
			HttpSession session,
			@RequestParam("id") Long id,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//obtenemos la lista
		ListaPrecios lista = listaPreciosService.obtenerPorId(id);
		//eliminamos
		commit =listaPreciosService.delete(lista);
		
		ScreenMessage mensaje;
		if(commit){
			mensaje = new ScreenMessageImp("notif.listaPrecios.eliminado", null);
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
	
	@RequestMapping(value="/eliminarAsociacionListaPrecios.html", method = RequestMethod.GET)
	public String eliminarAsociacion(
			HttpSession session,
			@RequestParam("listaId") Long listaId, 
			@RequestParam("detalleId") Long detalleId, 
			Map<String,Object> atributos) {
		
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//obtenemos la lista
		ListaPrecios lista = listaPreciosService.obtenerPorId(listaId);
		//obtenemos el detalle
		ListaPreciosDetalle detalle = listaPreciosService.obtenerListaPreciosDetallePorId(detalleId);
		//eliminamos el datalle de la lista
		lista.getDetalle().remove(detalle);
		//actualizamos la lista de precios
		commit =listaPreciosService.delete(detalle);
		
		ScreenMessage mensaje;
		if(commit){
			mensaje = new ScreenMessageImp("notif.agregarConcepto.eliminar", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.commitDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrar(listaId, session,atributos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
