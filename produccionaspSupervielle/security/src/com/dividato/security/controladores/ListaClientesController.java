/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.security.controladores;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dividato.security.validadores.ClienteBusquedaValidator;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Clientes.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Ezequiel Beccaria *
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
public class ListaClientesController {
	private ClienteAspService clienteService;
	private ClienteBusquedaValidator validator;
	
	@Autowired
	public void setClienteService(ClienteAspService clienteService) {
		this.clienteService = clienteService;
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
	public String mostrarCliente(HttpSession session, Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<ClienteAsp> clientes = null;	
		ClienteAsp cliente = (ClienteAsp) session.getAttribute("clienteBusqueda");
		if(cliente != null)
			clientes = clienteService.getByPersona(cliente.getPersona().getRazonSocial(), cliente.getContacto().getNombre());
		else
			clientes = clienteService.getByPersona(null, null);
		atributos.put("clientes", clientes);
		return "consultaCliente";
	}
	
	@RequestMapping(value="/filtrarCliente.html", method = RequestMethod.POST)
	public String filtrarCliente(
			@ModelAttribute("clienteBusqueda") ClienteAsp cliente, 
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
		return mostrarCliente(session, atributos);
	}
}
