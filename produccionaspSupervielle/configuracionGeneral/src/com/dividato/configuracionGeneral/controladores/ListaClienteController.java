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

import com.dividato.configuracionGeneral.validadores.ClienteBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de clientes.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarCliente.html",
				"/mostrarCliente.html",
				"/eliminarCliente.html",
				"/filtrarCliente.html"
			}
		)
public class ListaClienteController {
	private ClienteEmpService clienteEmpService;
	private ClienteBusquedaValidator validator;
	private EmpresaService empresaService;
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@Autowired
	public void setValidator(ClienteBusquedaValidator validator) {
		this.validator = validator;
	}

	@RequestMapping(value="/iniciarCliente.html", method = RequestMethod.GET)
	public String iniciarCliente(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("clienteBusqueda");
		return "redirect:mostrarCliente.html";
	}
	
	@RequestMapping(value="/mostrarCliente.html", method = RequestMethod.GET)
	public String mostrarCliente(HttpSession session, 
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valEmpresa){
		//buscamos en la base de datos y lo subimos a request.
		List<ClienteEmp> clientes = null;	
		ClienteEmp cliente = (ClienteEmp) session.getAttribute("clienteBusqueda");
		Empleado empleado = (Empleado) session.getAttribute("empleadoSession");
		if(empleado!=null){
			if(cliente == null)
				cliente = new ClienteEmp();
			cliente.setCodigo(empleado.getClienteEmp().getCodigo());
		}
		clientes = clienteEmpService.listarClienteFiltradas(cliente, obtenerClienteAspUser());
		
		if(cliente != null && cliente.getCodigoEmpresa()!=null && !"".equals( cliente.getCodigoEmpresa())){
			Empresa empresa = empresaService.getByCodigo(cliente.getCodigoEmpresa(), obtenerClienteAspUser());
			cliente.setEmpresa(empresa);
		}
		session.setAttribute("clienteBusqueda", cliente);
		atributos.put("clientes", clientes);
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		definirPopupEmpresa(atributos, valEmpresa);
		return "consultaCliente";
	}
	
	@RequestMapping(value="/filtrarCliente.html", method = RequestMethod.POST)
	public String filtrarCliente(
			@ModelAttribute("clienteBusqueda") ClienteEmp cliente, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(cliente, result);
		if(!result.hasErrors()){
			session.setAttribute("clienteBusqueda", cliente);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarCliente(session, atributos, null);
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Cliente.
	 * 
	 * @param idDireccion el id de Cliente a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarCliente.html",
			method = RequestMethod.GET
		)
	public String eliminarCliente(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la cliente para eliminar luego
		ClienteEmp cliente = clienteEmpService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la cliente
		commit = clienteEmpService.eliminarCliente(cliente);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.cliente.clienteEliminado", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarCliente(session, atributos, null);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private void definirPopupEmpresa(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> empresasPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioEmpresa.datosEmpresa.codigo",false));
		campos.add(new CampoDisplayTag("razonSocial.razonSocial","formularioEmpresa.datosEmpresa.razonSocial",false));
		campos.add(new CampoDisplayTag("descripcion","formularioEmpresa.datosEmpresa.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		empresasPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		empresasPopupMap.put("coleccionPopup", empresaService.listarEmpresaPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		empresasPopupMap.put("referenciaHtml", "codigoEmpresa");
		//url que se debe consumir con ajax
		empresasPopupMap.put("urlRequest", "mostrarStock.html");
		//se vuelve a setear el texto utilizado para el filtrado
		empresasPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		empresasPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("empresasPopupMap", empresasPopupMap);
	}
	
	
}
