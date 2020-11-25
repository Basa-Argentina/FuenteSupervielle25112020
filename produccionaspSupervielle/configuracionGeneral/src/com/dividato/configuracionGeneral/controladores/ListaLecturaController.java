/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/28/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.dividato.configuracionGeneral.validadores.LecturaBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.hibernate.LecturaDetalleServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de lecturas.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Victor Kenis (29/08/2011)
 * 
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarLectura.html",
				"/mostrarLectura.html",
				"/eliminarLectura.html",
				"/filtrarLectura.html"
			}
		)
public class ListaLecturaController {
	private LecturaService lecturaService;
	private SerieService serieService;
	private LecturaBusquedaValidator validator;
	
	@Autowired
	public void setLecturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	
	@Autowired
	public void setValidator(LecturaBusquedaValidator validator) {
		this.validator = validator;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}

	@RequestMapping(value="/iniciarLectura.html", method = RequestMethod.GET)
	public String iniciarLectura(
			HttpSession session, 
			Map<String,Object> atributos){
			session.removeAttribute("lecturaBusqueda");
		return "redirect:mostrarLectura.html";
	}
	
	@RequestMapping(value="/mostrarLectura.html", method = RequestMethod.GET)
	public String mostrarLectura(
			@RequestParam(value="val",required=false) String valSerie,
			HttpSession session, 
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<Lectura> lecturas = null;	
		Lectura lectura = (Lectura) session.getAttribute("lecturaBusqueda");
		if(lectura != null)
			lecturas = lecturaService.listarLecturaFiltradasPorSQL(lectura, obtenerClienteAspUser());
			//lecturas = lecturaService.listarLecturaFiltradas(lectura, obtenerClienteAspUser());
		else
			lecturas = lecturaService.listarLecturaFiltradasPorSQL(null, obtenerClienteAspUser());
			//lecturas = lecturaService.listarLecturaFiltradas(null, obtenerClienteAspUser());
		
		atributos.put("lecturas", lecturas);
		User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String codigoEmpresa = ((PersonaFisica)usuario.getPersona()).getEmpresaDefecto().getCodigo();
		atributos.put("codigoEmpresa", codigoEmpresa);
		session.setAttribute("lecturaBusqueda", lectura);
		
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		definirPopupSerie(atributos, valSerie, null, "I", null, null, null);
		
		return "consultaLectura";
	}
	
	@RequestMapping(value="/filtrarLectura.html", method = RequestMethod.POST)
	public String filtrarLectura(
			@ModelAttribute("lecturaBusqueda") Lectura lectura, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		
		this.validator.validate(lectura, result);
		if(!result.hasErrors()){
			session.setAttribute("lecturaBusqueda", lectura);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarLectura(null,session, atributos);
	}
	
	@RequestMapping(
			value="/eliminarLectura.html",
			method = RequestMethod.GET
		)
	public String eliminarLectura(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos,
			@RequestParam(value="accion",required=false) String accion) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la lectura para eliminar luego
		Lectura lectura = lecturaService.obtenerPorId(Long.valueOf(id));
		
		if(lectura!=null){
			LecturaDetalleService service = new LecturaDetalleServiceImp(HibernateControl.getInstance());
			List<LecturaDetalle> detalles = service.listarLecturaDetallePorLectura(lectura, obtenerClienteAspUser());
		
			//Eliminamos la lectura
			commit = lecturaService.eliminarLectura(lectura,detalles);
			ScreenMessage mensaje;
			//Controlamos su eliminacion.
			if(commit){
				mensaje = new ScreenMessageImp("formularioLectura.notificacion.lecturaEliminada", null);
				hayAvisos = true;
			}else{
				mensaje = new ScreenMessageImp("error.deleteDataBase", null);
				hayAvisosNeg = true;
			}
			avisos.add(mensaje);
			
			atributos.put("hayAvisosNeg", hayAvisosNeg);
			atributos.put("hayAvisos", hayAvisos);
			atributos.put("avisos", avisos);
		}
		
		return mostrarLectura(null, session, atributos);
	}
	
	private void definirPopupSerie(Map<String,Object> atributos, String val, String accion, String tipoSerie, String condIvaClientes, String id, Empresa emp){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> seriesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioLectura.datosLectura.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioLectura.datosLectura.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		seriesPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		seriesPopupMap.put("coleccionPopup", serieService.listarSeriePopup(val, tipoSerie, condIvaClientes, obtenerClienteAspUser(), emp));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		seriesPopupMap.put("referenciaHtml", "codigoSerie");
		//url que se debe consumir con ajax
		seriesPopupMap.put("urlRequest", "mostrarLectura.html");
		//se vuelve a setear el texto utilizado para el filtrado
		seriesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		seriesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("seriesPopupMap", seriesPopupMap);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
		
	}
}
