/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/06/2011
 */
package com.security.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.context.SecurityContextHolder;

import com.security.accesoDatos.configuraciongeneral.hibernate.ElementoServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.seguridad.User;

/**
 * @author Gonzalo Noriega
 *
 */
public class ElementosServlet extends HttpServlet{
	private static final long serialVersionUID = 2918192502249275039L;
	private static Logger logger = Logger.getLogger(ElementosServlet.class);
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigo = request.getParameter("codigo");
		String clienteIdStr = request.getParameter("clienteId");
		String codigoDepositoActual = request.getParameter("codigoDepositoActual");
		String codigoClienteEmp = request.getParameter("codigoClienteEmp");
		String codigoTipoElemento = request.getParameter("codigoTipoElemento");
		
		if(clienteIdStr == null || clienteIdStr.length()==0){
			clienteIdStr=obtenerClienteAspUser().getId().toString();
		}
		Elemento elementoBusqueda = new Elemento();
		elementoBusqueda.setCodigo(codigo);
		elementoBusqueda.setCodigoDeposito(codigoDepositoActual);		
		elementoBusqueda.setCodigoTipoElemento(codigoTipoElemento);
		elementoBusqueda.setCodigoCliente(codigoClienteEmp);
		
		String respuesta = "";
		Elemento elemento = null;
		if(!"".equals(codigo))
			elemento = getObject(elementoBusqueda, Long.valueOf(clienteIdStr));	
		if(elemento != null){
			respuesta = elemento.getTipoElemento().getDescripcion();
		}else{
			respuesta = "";
		}		
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo recuperar el elemento", e);
			e.printStackTrace();
		}		
	}
	
	private Elemento getObject(Elemento elementoBusqueda, Long clienteId){
		Elemento elemento = null;
		ElementoService service = new ElementoServiceImp(HibernateControl.getInstance());
		elemento = service.busquedaServlet(elementoBusqueda, getObject(clienteId));		
		return elemento;
	}
	
	private ClienteAsp getObject(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
