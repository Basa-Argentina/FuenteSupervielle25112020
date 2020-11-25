/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
 */
package com.dividato.configuracionGeneral.controladores.jerarquias;

import java.util.ArrayList;
import java.util.Date;
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

import com.dividato.configuracionGeneral.controladores.jerarquias.ListaTipoJerarquiaController;
import com.dividato.configuracionGeneral.validadores.jerarquias.TipoJerarquiaValidator;
import com.security.accesoDatos.jerarquias.interfaz.TipoJerarquiaService;
import com.security.modelo.administracion.ClienteAsp;
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
				"/precargaFormularioTipoJerarquia.html",
				"/guardarActualizarTipoJerarquia.html"
			}
		)
public class FormTipoJerarquiaController {
	private TipoJerarquiaService service;
	private TipoJerarquiaValidator validator;
	private ListaTipoJerarquiaController listaTipoJerarquiaController;
		
	@Autowired
	public void setService(TipoJerarquiaService service) {
		this.service = service;
	}	
	@Autowired
	public void setListaTipoJerarquiaController(
			ListaTipoJerarquiaController listaTipoJerarquiaController) {
		this.listaTipoJerarquiaController = listaTipoJerarquiaController;
	}
	@Autowired
	public void setValidator(TipoJerarquiaValidator validator) {
		this.validator = validator;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}

	@RequestMapping(
			value="/precargaFormularioTipoJerarquia.html",
			method = RequestMethod.GET
		)
	public String precarga(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,
			Map<String,Object> atributos,
			HttpSession session) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			TipoJerarquia tipo;
			tipo = service.obtenerPorId(Long.valueOf(id));			
			atributos.put("tipoFormulario", tipo);
		}		
		//Seteo la accion actual
		atributos.put("accion", accion);	
		
		//Se realiza el redirect
		return "formularioTipoJerarquia";
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
			value="/guardarActualizarTipoJerarquia.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("tipoFormulario") final TipoJerarquia tipoFormulario,
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
			tipoFormulario.setAccion(accion);
			//obtengo el cliente
			tipoFormulario.setClienteAsp(obtenerClienteAspUser());
			//valido datos
			validator.validate(tipoFormulario, result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		TipoJerarquia lista;
		Long lista_id = null; 
		if(!result.hasErrors()){	
			
			if(accion.equals("NUEVO")){
				lista = new TipoJerarquia();
				setData(lista, tipoFormulario);
				lista.setFechaRegistro(new Date());	
				lista.setFechaActualizacion(lista.getFechaRegistro()); //fecha de actualizacion
				//se setea el que realizo la modificacion
				lista.setModifico(obtenerUser().getPersona().getNombre()+" "+obtenerUser().getPersona().getApellido()); 
				//Se guarda el cliente en la BD
				commit = service.save(lista);
			}else{
				lista = service.obtenerPorId(tipoFormulario.getId());				
				setData(lista, tipoFormulario);				
				lista.setFechaActualizacion(new Date()); //fecha de actualizacion
				//se setea el que realizo la modificacion
				lista.setModifico(obtenerUser().getPersona().getNombre()+" "+obtenerUser().getPersona().getApellido());
				//Se Actualiza el cliente en la BD
				commit = service.update(lista);
			}			
			lista_id = lista.getId();
			if(commit == null || !commit)
				tipoFormulario.setId(lista.getId());
		}
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		//Ver errores
		if(commit != null && !commit){
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("listaFormulario", tipoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precarga(accion, String.valueOf(tipoFormulario.getId()), atributos, session);
		}
		if(result.hasErrors()){
			atributos.put("impuestoFormulario", tipoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", false);
			atributos.remove("avisos");	
			return precarga(accion, String.valueOf(tipoFormulario.getId()), atributos, session);
		}else{
			//Genero las notificaciones			
			if("NUEVO".equals(accion)){	
				ScreenMessage notificacion = new ScreenMessageImp("notif.tipoJerarquia.registrado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}else{
				ScreenMessage notificacion = new ScreenMessageImp("notif.tipoJerarquia.modificado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaTipoJerarquiaController.mostrar(lista_id, session, atributos);
	}
	 
	private void setData(TipoJerarquia obj, TipoJerarquia data){
		if(data != null){			
			obj.setClienteAsp(data.getClienteAsp());
			obj.setCodigo(data.getCodigo());
			obj.setDescripcion(data.getDescripcion());
			obj.setObservacion(data.getObservacion());		
		}
	}	
	
	private ClienteAsp obtenerClienteAspUser(){
		return obtenerUser().getCliente();
	}
	
	private User obtenerUser(){		
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
