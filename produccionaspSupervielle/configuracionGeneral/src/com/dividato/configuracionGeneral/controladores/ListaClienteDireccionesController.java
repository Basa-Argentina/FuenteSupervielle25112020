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

import com.dividato.configuracionGeneral.validadores.ClienteDireccionBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.Localidad;
import com.security.modelo.general.Pais;
import com.security.modelo.general.Provincia;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de clienteDireccions.
 * Esta anotaci�n le indica a SPRING que esta clase es de tipo controlador.
 * A continuaci�n est� la anotaci�n @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarClienteDireccion.html",
				"/mostrarClienteDireccion.html",
				"/eliminarClienteDireccion.html",
				"/filtrarClienteDireccion.html"
			}
		)
public class ListaClienteDireccionesController {
	private ClienteDireccionService clienteDireccionService;
	private ClienteDireccionBusquedaValidator validator;
	private ClienteEmpService clienteEmpService;
	private List<Pais> paises;
	private ProvinciaService provinciaService;
	private LocalidadService localidadService;
	private PaisService paisService;
	private BarrioService barrioService;
	
	@Autowired
	public void setClienteDireccionService(ClienteDireccionService clienteDireccionService) {
		this.clienteDireccionService = clienteDireccionService;
	}
	
	@Autowired
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	
	@Autowired
	public void setBarrioService(BarrioService barrioService) {
		this.barrioService = barrioService;
	}
	
	@Autowired
	public void setProvinciaService(ProvinciaService provinciaService) {
		this.provinciaService = provinciaService;
	}
	@Autowired
	public void setLocalidadService(LocalidadService localidadService) {
		this.localidadService = localidadService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setValidator(ClienteDireccionBusquedaValidator validator) {
		this.validator = validator;
	}

	@RequestMapping(value="/iniciarClienteDireccion.html", method = RequestMethod.GET)
	public String iniciarClienteDireccion(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("clienteDireccionBusqueda");
		return "redirect:mostrarClienteDireccion.html";
	}
	
	@RequestMapping(value="/mostrarClienteDireccion.html", method = RequestMethod.GET)
	public String mostrarClienteDireccion(
			@RequestParam(value="val", required=false) String val,
			@RequestParam(value="clienteCodigo",required=false) Long ubicacionId, 
			HttpSession session, 
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<ClienteDireccion> clienteDirecciones = null;	
		ClienteDireccion clienteDireccion = (ClienteDireccion) session.getAttribute("clienteDireccionBusqueda");
		if(clienteDireccion != null){
			if(clienteDireccion.getIdPaisAux()!=null && clienteDireccion.getIdPaisAux()>0){
				Pais pais = paisService.obtenerPorId(clienteDireccion.getIdPaisAux());
				clienteDireccion.setPaisAux(pais);
				atributos.put("paisDefecto", pais);
			}
			else{
				Pais paisDefecto = paisService.getPaisPorNombre(Constants.PAIS_DEFECTO);
				if(paisDefecto!=null)
					atributos.put("paisDefecto", paisDefecto);
			}
			if(clienteDireccion.getIdProvinciaAux()!=null && clienteDireccion.getIdProvinciaAux()>0){
				Provincia provincia = provinciaService.obtenerPorId(clienteDireccion.getIdProvinciaAux());
				clienteDireccion.setProvinciaAux(provincia);
			}
			else{
				clienteDireccion.setProvinciaAux(null);
			}
			if(clienteDireccion.getIdLocalidadAux()!=null && clienteDireccion.getIdLocalidadAux()>0){
				Localidad localidad = localidadService.obtenerPorId(clienteDireccion.getIdLocalidadAux());
				clienteDireccion.setLocalidadAux(localidad);
			}
			else{
				clienteDireccion.setLocalidadAux(null);
			}
			if(clienteDireccion.getIdBarrio()!=null && clienteDireccion.getIdBarrio()>0){
				Barrio barrio = barrioService.obtenerPorId(clienteDireccion.getIdBarrio());
				clienteDireccion.setBarrioAux(barrio);
			}
			else{
				clienteDireccion.setBarrioAux(null);
			}
			if(clienteDireccion.getClienteCodigo()!=null && !"".equals(clienteDireccion.getClienteCodigo())){
				ClienteEmp clienteEmp = clienteEmpService.getByCodigo(clienteDireccion.getClienteCodigo());
				clienteDireccion.setCliente(clienteEmp);
			}
			else{
				clienteDireccion.setCliente(new ClienteEmp());
			}
			clienteDirecciones = clienteDireccionService.listarClienteDireccionesFiltradasPorCliente(clienteDireccion, obtenerClienteAspUser());
		}
		else{
			Pais paisDefecto = paisService.getPaisPorNombre(Constants.PAIS_DEFECTO);
			if(paisDefecto!=null)
				atributos.put("paisDefecto", paisDefecto);
			clienteDirecciones = clienteDireccionService.listarClienteDireccionesFiltradasPorCliente(null, obtenerClienteAspUser());
		}

		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		
		
		//Preparo el popup de impuestos	
		definirPopupClientes(atributos, val);
		
		//obtengo los paises registrados en el sistema
		definirPopupPais(atributos, val);
		//si se selecciono un pais, obtengo las provincias del mismo
		definirPopupProvincia(atributos, val, ubicacionId);
		//si se selecciono un provincia, obtengo las localidades de la misma
		definirPopupLocalidad(atributos, val, ubicacionId);
		//si se selecciono una localidad, obtengo los barrios
		definirPopupBarrio(atributos, val, ubicacionId);
		session.setAttribute("clienteDireccionBusqueda", clienteDireccion);
		atributos.put("clienteDirecciones", clienteDirecciones);
		return "consultaClienteDireccion";
	}
	
	@RequestMapping(value="/filtrarClienteDireccion.html", method = RequestMethod.POST)
	public String filtrarClienteDireccion(
			@ModelAttribute("clienteDireccionBusqueda") ClienteDireccion clienteDireccion, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(clienteDireccion, result);
		if(!result.hasErrors()){
			session.setAttribute("clienteDireccionBusqueda", clienteDireccion);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarClienteDireccion(null,null,session, atributos);
	}
	
	/**
	 * Observar la anotaci�n @RequestMapping de SPRING.
	 * Todos los par�metros son inyectados por SPRING cuando ejecuta el m�todo.
	 * 
	 * Se encarga de eliminar ClienteDireccion.
	 * 
	 * @param idDireccion el id de ClienteDireccion a eliminar.
	 * (Observar la anotaci�n @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el m�todo de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarClienteDireccion.html",
			method = RequestMethod.GET
		)
	public String eliminarClienteDireccion(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la clienteDireccion para eliminar luego
		ClienteDireccion clienteDireccion = clienteDireccionService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la clienteDireccion
		commit = clienteDireccionService.eliminarClienteDireccion(clienteDireccion);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.clienteDireccion.clienteDireccionEliminada", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarClienteDireccion(null,null,session, atributos);
	}
	
	
	/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void definirPopupClientes(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clientesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("empresa.razonSocial","formularioClienteDireccion.cliente.razonSocialEmpresa",false));
		
		campos.add(new CampoDisplayTag("razonSocialONombreYApellido","formularioClienteDireccion.cliente.nombreRazonSocial",false));
		
		campos.add(new CampoDisplayTag("nombreYApellido","formularioClienteDireccion.cliente.apellido",true));
		campos.add(new CampoDisplayTag("codigo","formularioClienteDireccion.cliente.apellido",true));
		clientesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colecci�n dentro del servicio
		clientesPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup(val, null, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup2", "nombreYApellido");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clientesPopupMap.put("referenciaHtml", "clienteCodigo"); 		
		//url que se debe consumir con ajax
		clientesPopupMap.put("urlRequest", "mostrarClienteDireccion.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clientesPopupMap.put("textoBusqueda", val);		
		//codigo de la localizaci�n para el titulo del popup
		clientesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("clientesPopupMap", clientesPopupMap);
	}
	
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private void definirPopupPais(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.pais",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colecci�n dentro del servicio		
		map.put("coleccionPopup", paisService.listarPaisesPopup(val));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "pais"); 		
		//url que se debe consumir con ajax
		map.put("urlRequest", "mostrarClienteDireccion.html");	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localizaci�n para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("paisPopupMap", map);
	}

	private void definirPopupProvincia(Map<String,Object> atributos, String val, Long paisId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioCliente.datosCliente.direccion.provincia",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.provincia",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colecci�n dentro del servicio		
		map.put("coleccionPopup", (paisId != null)? provinciaService.listarProvinciasPopup(paisId, val): new ArrayList<Provincia>());
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "provincia"); 		
		//url que se debe consumir con ajax		
		map.put("urlRequest", "mostrarClienteDireccion.html");
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//se setea el id del pais seleccionado.
		map.put("filterPopUp", paisId);
		//codigo de la localizaci�n para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("provinciaPopupMap", map);
	}
	
	private void definirPopupLocalidad(Map<String,Object> atributos, String val, Long provinciaId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioCliente.datosCliente.direccion.localidad",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.localidad",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colecci�n dentro del servicio		
		map.put("coleccionPopup", (provinciaId != null)? localidadService.listarLocalidadesPopup(provinciaId, val): new ArrayList<Provincia>());
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "localidad"); 		
		//url que se debe consumir con ajax		
		map.put("urlRequest", "mostrarClienteDireccion.html");
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//se setea el id del pais seleccionado.
		map.put("filterPopUp", provinciaId);
		//codigo de la localizaci�n para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("localidadPopupMap", map);
	}
	
	private void definirPopupBarrio(Map<String,Object> atributos, String val, Long localidadId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioCliente.datosCliente.direccion.barrio",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.barrio",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colecci�n dentro del servicio		
		map.put("coleccionPopup", (localidadId != null)? barrioService.listarBarriosPopup(localidadId, val) : new ArrayList<Provincia>());
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "barrio"); 		
		//url que se debe consumir con ajax		
		map.put("urlRequest", "mostrarClienteDireccion.html");
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//se setea el id del pais seleccionado.
		map.put("filterPopUp", localidadId);
		//codigo de la localizaci�n para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("barrioPopupMap", map);
	}
}
