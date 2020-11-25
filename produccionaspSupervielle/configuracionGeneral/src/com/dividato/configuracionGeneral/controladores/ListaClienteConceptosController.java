/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.ClienteConceptoBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteConceptoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteConcepto;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de clienteConceptos.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarClienteConcepto.html",
				"/mostrarClienteConcepto.html",
				"/eliminarClienteConcepto.html",
				"/filtrarClienteConcepto.html"
			}
		)
public class ListaClienteConceptosController {
	private ClienteConceptoService clienteConceptoService;
	private ClienteConceptoBusquedaValidator validator;
	private ClienteEmpService clienteEmpService;
	private ListaPreciosService listaPreciosService;
	private ConceptoFacturableService conceptoFacturableService;
	
	@Autowired
	public void setClienteConceptoService(ClienteConceptoService clienteConceptoService) {
		this.clienteConceptoService = clienteConceptoService;
	}

	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setListaPreciosService(ListaPreciosService listaPreciosService) {
		this.listaPreciosService = listaPreciosService;
	}
	
	@Autowired
	public void setConceptoFacturableService(ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
	}
	
	
	@Autowired
	public void setValidator(ClienteConceptoBusquedaValidator validator) {
		this.validator = validator;
	}

	@RequestMapping(value="/iniciarClienteConcepto.html", method = RequestMethod.GET)
	public String iniciarClienteConcepto(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("clienteConceptoBusqueda");
		return "redirect:mostrarClienteConcepto.html";
	}
	
	@RequestMapping(value="/mostrarClienteConcepto.html", method = RequestMethod.GET)
	public String mostrarClienteConcepto(
			@RequestParam(value="val", required=false) String valCliente,
			@RequestParam(value="val", required=false) String valListaPrecio,
			@RequestParam(value="val", required=false) String valConcepto,
			HttpSession session, 
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<ClienteConcepto> clienteConceptos = null;	
		ClienteConcepto clienteConcepto = (ClienteConcepto) session.getAttribute("clienteConceptoBusqueda");
		if(clienteConcepto != null)
			clienteConceptos = clienteConceptoService.listarClienteConceptoesFiltradasPorCliente(clienteConcepto, obtenerClienteAspUser());
		else
			clienteConceptos = clienteConceptoService.listarClienteConceptoesFiltradasPorCliente(null, obtenerClienteAspUser());
		

		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		//Preparo el popup de clientes	
		definirPopupClientes(atributos, valCliente);
		//Preparo el popup de Listas de Precios
		definirPopupListaPrecios(atributos, valListaPrecio);
		//Preparo el popup de Conceptos
		definirPopupConceptos(atributos, valConcepto);
		
		atributos.put("clienteConceptos", clienteConceptos);
		return "consultaClienteConcepto";
	}
	
	@RequestMapping(value="/filtrarClienteConcepto.html", method = RequestMethod.POST)
	public String filtrarClienteConcepto(
			@ModelAttribute("clienteConceptoBusqueda") ClienteConcepto clienteConcepto, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(clienteConcepto, result);
		if(!result.hasErrors()){
			session.setAttribute("clienteConceptoBusqueda", clienteConcepto);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarClienteConcepto(null, null, null,session, atributos);
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar ClienteConcepto.
	 * 
	 * @param idConcepto el id de ClienteConcepto a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarClienteConcepto.html",
			method = RequestMethod.GET
		)
	public String eliminarClienteConcepto(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la clienteConcepto para eliminar luego
		ClienteConcepto clienteConcepto = clienteConceptoService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la clienteConcepto
		commit = clienteConceptoService.eliminarClienteConcepto(clienteConcepto);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.clienteConcepto.clienteConceptoEliminado", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarClienteConcepto(null,null,null,session, atributos);
	}
	
	
	/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void definirPopupClientes(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clientesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("razonSocialONombreYApellido","formularioClienteConcepto.cliente",false));
		campos.add(new CampoDisplayTag("empresa.razonSocial.razonSocial","formularioClienteConcepto.cliente.empresa",false));
		campos.add(new CampoDisplayTag("nombreYApellido","formularioClienteConcepto.cliente.apellido",true));
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.cliente.apellido",true));
		clientesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		clientesPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup(val, null, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup2", "nombreYApellido");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clientesPopupMap.put("referenciaHtml", "clienteCodigo"); 		
		//url que se debe consumir con ajax
		clientesPopupMap.put("urlRequest", "mostrarClienteConcepto.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clientesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		clientesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("clientesPopupMap", clientesPopupMap);
	}
	
	private void definirPopupListaPrecios(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> listaPreciosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.listaPrecio.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioClienteConcepto.listaPrecio.descripcion",false));
		campos.add(new CampoDisplayTag("valor","formularioClienteConcepto.listaPrecio.valor",false));
		listaPreciosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		listaPreciosPopupMap.put("coleccionPopup", listaPreciosService.listarListasPreciosPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		listaPreciosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		listaPreciosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		listaPreciosPopupMap.put("referenciaHtml", "listaPrecioCodigo"); 		
		//url que se debe consumir con ajax
		listaPreciosPopupMap.put("urlRequest", "mostrarClienteConcepto.html");
		//se vuelve a setear el texto utilizado para el filtrado
		listaPreciosPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		listaPreciosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("listaPreciosPopupMap", listaPreciosPopupMap);
	}
	
	private void definirPopupConceptos(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> conceptosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.concepto.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioClienteConcepto.concepto.descripcion",false));
		campos.add(new CampoDisplayTag("costo","formularioClienteConcepto.concepto.costo",false));
		campos.add(new CampoDisplayTag("precioBase","formularioClienteConcepto.concepto.precioBase",false));
		conceptosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		conceptosPopupMap.put("coleccionPopup", conceptoFacturableService.listarConceptosFacturablesPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		conceptosPopupMap.put("referenciaHtml", "conceptoCodigo"); 		
		//url que se debe consumir con ajax
		conceptosPopupMap.put("urlRequest", "mostrarClienteConcepto.html");
		//se vuelve a setear el texto utilizado para el filtrado
		conceptosPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		conceptosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("conceptosPopupMap", conceptosPopupMap);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
