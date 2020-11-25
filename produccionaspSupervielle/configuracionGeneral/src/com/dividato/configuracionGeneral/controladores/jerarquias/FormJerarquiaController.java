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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.jerarquias.JerarquiaValidator;
import com.security.accesoDatos.jerarquias.interfaz.JerarquiaService;
import com.security.accesoDatos.jerarquias.interfaz.TipoJerarquiaService;
import com.security.modelo.jerarquias.Jerarquia;
import com.security.modelo.jerarquias.TipoJerarquia;
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
				"/precargaFormularioJerarquia.html",
				"/guardarActualizarJerarquia.html"
			}
		)
public class FormJerarquiaController {
	private JerarquiaService service;
	private TipoJerarquiaService tipoJerarquiaService;
	private JerarquiaValidator validator;
	private ListaTipoJerarquiaController listaTipoJerarquiaController;
		
	@Autowired
	public void setService(JerarquiaService service) {
		this.service = service;
	}	
	
	@Autowired
	public void setTipoJerarquiaService(TipoJerarquiaService tipoJerarquiaService) {
		this.tipoJerarquiaService = tipoJerarquiaService;
	}

	@Autowired
	public void setListaJerarquiaController(ListaTipoJerarquiaController listaTipoJerarquiaController) {
		this.listaTipoJerarquiaController = listaTipoJerarquiaController;
	}
	@Autowired
	public void setValidator(JerarquiaValidator validator) {
		this.validator = validator;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}

	@RequestMapping(
			value="/precargaFormularioJerarquia.html",
			method = RequestMethod.GET
		)
	public String precarga(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="idTipoSel",required=false) Long tipoId,
			@RequestParam(value="id",required=false) Long id,
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		Jerarquia jerarquia = (Jerarquia) atributos.get("jerarquia");	
		if(jerarquia == null)
			if(!accion.equals("NUEVO")){
				jerarquia = service.obtenerPorId(id);			
			}else{
				jerarquia = new Jerarquia();
				if(tipoId != null){
					TipoJerarquia tipo = tipoJerarquiaService.obtenerPorId(tipoId);
					jerarquia.setTipo(tipo);		
				}			
			}		
		//agrego el detalle a los atributos
		atributos.put("jerarquia", jerarquia);			
		//Seteo la accion actual
		atributos.put("accion", accion);		
		//Se realiza el redirect
		return "formularioJerarquia";
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
			value="/guardarActualizarJerarquia.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("jerarquia") final Jerarquia jerarquia,
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
			jerarquia.setAccion(accion);			
			//seteo el tipo de jerarquia
			TipoJerarquia tipo = tipoJerarquiaService.obtenerPorId(jerarquia.getTipoId());
			jerarquia.setTipo(tipo);		
			//valido datos
			validator.validate(jerarquia, result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Jerarquia jerarquiaGuardar;
		Long tipo_id = null;
		if(!result.hasErrors()){			
			if(accion.equals("NUEVO")){
				jerarquiaGuardar = new Jerarquia();
				setData(jerarquiaGuardar, jerarquia);				
				//Se guarda el cliente en la BD
				commit = service.save(jerarquia);
			}else{
				jerarquiaGuardar = service.obtenerPorId(jerarquia.getId());
				setData(jerarquiaGuardar, jerarquia);				
				//Se Actualiza el cliente en la BD
				commit = service.update(jerarquia);
			}			
			tipo_id = jerarquia.getTipo().getId(); //seteo el id del tipo
		}
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		//Ver errores
		if(commit != null && !commit){
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("jerarquia", jerarquia);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precarga(
					accion,
					jerarquia.getTipoId(),
					jerarquia.getId(),					
					atributos);
		}
		if(result.hasErrors()){
			atributos.put("jerarquia", jerarquia);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", false);
			atributos.remove("avisos");	
			return precarga(
					accion,
					jerarquia.getTipoId(),
					jerarquia.getId(),					
					atributos);
		}else{
			//Genero las notificaciones			
			if("NUEVO".equals(accion)){	
				ScreenMessage notificacion = new ScreenMessageImp("notif.jerarquia.registrada", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}else{
				ScreenMessage notificacion = new ScreenMessageImp("notif.jerarquia.modificada", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaTipoJerarquiaController.mostrar(tipo_id, session, atributos);
		
	}
	 
	private void setData(Jerarquia obj, Jerarquia data){
		if(data != null){			
			obj.setDescripcion(data.getDescripcion());
			obj.setValoracion(data.getValoracion());
			obj.setObservacion(data.getObservacion());
			obj.setVerticalDesde(data.getVerticalDesde());
			obj.setVerticalHasta(data.getVerticalHasta());
			obj.setHorizontalDesde(data.getHorizontalDesde());
			obj.setHorizontalHasta(data.getHorizontalHasta());
		}
	}	
}
