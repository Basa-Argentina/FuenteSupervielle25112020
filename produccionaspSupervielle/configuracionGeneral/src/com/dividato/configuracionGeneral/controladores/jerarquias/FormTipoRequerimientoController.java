/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/06/2011
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

import com.dividato.configuracionGeneral.validadores.jerarquias.TipoRequerimientoValidator;
import com.security.accesoDatos.jerarquias.interfaz.TipoRequerimientoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.jerarquias.TipoRequerimiento;
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
				"/precargaFormularioTipoRequerimiento.html",
				"/guardarActualizarTipoRequerimiento.html"
			}
		)
public class FormTipoRequerimientoController {
	private TipoRequerimientoService service;
	private TipoRequerimientoValidator validator;	
	private ListaTipoRequerimientoController listaTipoRequerimientosController;
	
	/**
	 * Setea el servicio de User.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase UserImp implementa User y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param personaService
	 */

	@Autowired
	public void setService(TipoRequerimientoService impuestoService) {
		this.service = impuestoService;
	}
	@Autowired
	public void setListaTipoRequerimientosController(
			ListaTipoRequerimientoController listaTipoRequerimientosController) {
		this.listaTipoRequerimientosController = listaTipoRequerimientosController;
	}
	@Autowired
	public void setValidator(TipoRequerimientoValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}

	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioUser.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de User, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioUser" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioTipoRequerimiento.html",
			method = RequestMethod.GET
		)
	public String precarga(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			TipoRequerimiento tipo;
			tipo = service.obtenerPorId(Long.valueOf(id));			
			atributos.put("tipoFormulario", tipo);			
		}	
		//Seteo la accion actual
		atributos.put("accion", accion);		
		//Se realiza el redirect
		return "formularioTipoRequerimiento";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos User.
	 * 
	 * @param User user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto User con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioUser" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarTipoRequerimiento.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("tipoFormulario") final TipoRequerimiento tipoFormulario,
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
		TipoRequerimiento tipo;
		
		if(!result.hasErrors()){	
			
			if(accion.equals("NUEVO")){
				tipo = new TipoRequerimiento();
				setData(tipo, tipoFormulario);
				tipo.setFechaRegistro(new Date());	
				tipo.setFechaActualizacion(tipo.getFechaRegistro()); //fecha de actualizacion
				//se setea el que realizo la modificacion
				tipo.setModifico(obtenerUser().getPersona().getNombre()+" "+obtenerUser().getPersona().getApellido());
				//Se guarda el cliente en la BD
				commit = service.save(tipo);
			}else{
				tipo = service.obtenerPorId(tipoFormulario.getId());
				setData(tipo, tipoFormulario);
				tipo.setFechaActualizacion(new Date()); //fecha de actualizacion
				//se setea el que realizo la modificacion
				tipo.setModifico(obtenerUser().getPersona().getNombre()+" "+obtenerUser().getPersona().getApellido());
				//Se Actualiza el cliente en la BD
				commit = service.update(tipo);
			}
			if(commit == null || !commit)
				tipoFormulario.setId(tipo.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("impuestoFormulario", tipoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precarga(accion, String.valueOf(tipoFormulario.getId()), atributos);			
		}
		
		if(result.hasErrors()){
			atributos.put("impuestoFormulario", tipoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			return precarga(accion, String.valueOf(tipoFormulario.getId()), atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			if("NUEVO".equals(accion)){	
				ScreenMessage notificacion = new ScreenMessageImp("notif.tipoRequerimiento.registrado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}else{
				ScreenMessage notificacion = new ScreenMessageImp("notif.tipoRequerimiento.modificado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaTipoRequerimientosController.mostrar(session, atributos);
	}
	
	private void setData(TipoRequerimiento obj, TipoRequerimiento data){
		if(data != null){			
			obj.setClienteAsp(data.getClienteAsp());
			obj.setCodigo(data.getCodigo());
			obj.setDescripcion(data.getDescripcion());
			obj.setPlazo(data.getPlazo());	
			obj.setCargaPorCantidad(data.getCargaPorCantidad());
			obj.setRetiro(data.getRetiro());
			obj.setBuscarConRef(data.getBuscarConRef());
			obj.setBuscarSinRef(data.getBuscarSinRef());
			obj.setOcultaGrilla(data.getOcultaGrilla());
		}
	}	
	
	private ClienteAsp obtenerClienteAspUser(){
		return obtenerUser().getCliente();
	}
	
	private User obtenerUser(){		
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
