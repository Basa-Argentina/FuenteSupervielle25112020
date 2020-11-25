/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.dividato.configuracionGeneral.validadores.ListaPreciosValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoVariacionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.configuraciongeneral.TipoVariacion;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
import com.security.utils.ScreenOption;
import com.security.utils.ScreenOptionImp;

/**
 * @author Ezequiel Beccaria
 * @modificado Victor Kenis (15/08/2011)
 *
 */

@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioListaPrecios.html",
				"/guardarActualizarListaPrecios.html"
			}
		)
public class FormListaPreciosController {
	private ListaPreciosService service;
	private TipoVariacionService tipoVariacionService;
	private ListaPreciosValidator validator;
	private ListaListaPreciosController listaListaPreciosController;
		
	@Autowired
	public void setService(ListaPreciosService service) {
		this.service = service;
	}	
	@Autowired	
	public void setTipoVariacionService(TipoVariacionService tipoVariacionService) {
		this.tipoVariacionService = tipoVariacionService;
	}
	@Autowired
	public void setListaListaPreciosController(
			ListaListaPreciosController listaListaPreciosController) {
		this.listaListaPreciosController = listaListaPreciosController;
	}
	@Autowired
	public void setValidator(ListaPreciosValidator validator) {
		this.validator = validator;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}

	@RequestMapping(
			value="/precargaFormularioListaPrecios.html",
			method = RequestMethod.GET
		)
	public String precarga(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			ListaPrecios lista;
			lista = service.obtenerPorId(Long.valueOf(id));			
			atributos.put("listaFormulario", lista);
			atributos.put("listaVacia", (lista.getDetalle() == null || lista.getDetalle().isEmpty())? true : false);			
		}else{
			atributos.put("listaVacia", true);
		}
		//Seteo la accion actual
		atributos.put("accion", accion);	
		//Busco los impuesto registrados en la BD
		List<TipoVariacion> tiposVariacion = tipoVariacionService.listarVariaciones(null);
		atributos.put("tiposVariacion", tiposVariacion); //Seteo los impuestos en el responce
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		//creo el popup de opciones para la modificacion
		atributos.put("opcionesTitulo", "formularioListaPrecios.opcion.titulo");
		List<ScreenOption> opciones = new ArrayList<ScreenOption>();
		opciones.add(new ScreenOptionImp("1","formularioListaPrecios.opcion.o1", true));
		opciones.add(new ScreenOptionImp("2","formularioListaPrecios.opcion.o2", false));
		opciones.add(new ScreenOptionImp("3","formularioListaPrecios.opcion.o3", false));
		atributos.put("opciones", opciones);
		//Se realiza el redirect
		return "formularioListaPrecios";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto User con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return la vista que debe mostrarse
	 */
	@RequestMapping(
			value="/guardarActualizarListaPrecios.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("listaFormulario") final ListaPrecios listaFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			//seteo la accion
			listaFormulario.setAccion(accion);
			//obtengo el cliente
			listaFormulario.setClienteAsp(obtenerClienteAspUser());
			//obtengo el tipo de variacion asignado
			listaFormulario.setTipoVariacion(tipoVariacionService.obtenerPorId(listaFormulario.getTipoVariacionId()));			
			//valido datos
			validator.validateRegMod(listaFormulario, result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		ListaPrecios lista;
		Long lista_id = null; 
		if(!result.hasErrors()){	
			
			if(accion.equals("NUEVO")){
				lista = new ListaPrecios();
				setData(lista, listaFormulario);
				lista.setFechaRegistro(new Date());	
				lista.setFechaActualizacion(lista.getFechaRegistro()); //fecha de actualizacion
				//se setea el que realizo la modificacion
				lista.setModifico(obtenerUser().getPersona().getNombre()+" "+obtenerUser().getPersona().getApellido());
				//Se guarda el cliente en la BD
				commit = service.save(lista);
			}else{
				lista = service.obtenerPorId(listaFormulario.getId());
				actualizarDetalles(listaFormulario.getOpcion(), lista.getDetalle(), listaFormulario, lista);
				setData(lista, listaFormulario);				
				lista.setFechaActualizacion(new Date()); //fecha de actualizacion
				//se setea el que realizo la modificacion
				lista.setModifico(obtenerUser().getPersona().getNombre()+" "+obtenerUser().getPersona().getApellido());
				//Se Actualiza el cliente en la BD
				commit = service.update(lista);
			}			
			lista_id = lista.getId();
			if(commit == null || !commit)
				listaFormulario.setId(lista.getId());
		}
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		//Ver errores
		if(commit != null && !commit){
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("listaFormulario", listaFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precarga(accion, String.valueOf(listaFormulario.getId()), atributos);
		}
		if(result.hasErrors()){
			atributos.put("impuestoFormulario", listaFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", false);
			atributos.remove("avisos");	
			return precarga(accion, String.valueOf(listaFormulario.getId()), atributos);
		}else{
			//Genero las notificaciones			
			if("NUEVO".equals(accion)){	
				ScreenMessage notificacion = new ScreenMessageImp("notif.listaPrecios.registrado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}else{
				ScreenMessage notificacion = new ScreenMessageImp("notif.listaPrecios.modificado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaListaPreciosController.mostrar(lista_id, session, atributos);
	}
	
	private void actualizarDetalles(String opcion, Set<ListaPreciosDetalle> detalles, ListaPrecios lista, ListaPrecios listaVieja){
		if(!"3".equals(opcion)){
			for(ListaPreciosDetalle d:detalles){
				if("1".equals(opcion)){
					d.setTipoVariacion(lista.getTipoVariacion());
					d.setValor(lista.getValor());
				}else if("2".equals(opcion)){
					if(d.getTipoVariacion().getId() == listaVieja.getTipoVariacion().getId() &&
							d.getValor().equals(listaVieja.getValor())){
						d.setTipoVariacion(lista.getTipoVariacion());
						d.setValor(lista.getValor());
					}						
				}				
			}
		}
	}
	 
	private void setData(ListaPrecios obj, ListaPrecios data){
		if(data != null){			
			obj.setClienteAsp(data.getClienteAsp());
			if(data.getHabilitada()!= null)
				obj.setHabilitada(data.getHabilitada());
			else
				obj.setHabilitada(false);
			obj.setCodigo(data.getCodigo());
			obj.setDescripcion(data.getDescripcion());
			obj.setTipoVariacion(data.getTipoVariacion());
			obj.setValor(data.getValor());
			if(data.getUsaVencimiento() != null)
			{	
				obj.setUsaVencimiento(data.getUsaVencimiento());
				obj.setFechaVencimiento(data.getFechaVencimiento());
			}
			else
			{
				obj.setUsaVencimiento(false);
				obj.setFechaVencimiento(null);	
			}
			if(data.getListaFija() != null)
				obj.setListaFija(data.getListaFija());
			else
				obj.setListaFija(false);		
		}
	}	
	
	private ClienteAsp obtenerClienteAspUser(){
		return obtenerUser().getCliente();
	}
	
	private User obtenerUser(){		
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
