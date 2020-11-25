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

import com.security.accesoDatos.configuraciongeneral.hibernate.ClienteDireccionServiceImp;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;

import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;

import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
public class DireccionesEntregaServlet extends HttpServlet{
	private static final long serialVersionUID = 7014807021831556429L;
	private static Logger logger = Logger.getLogger(DireccionesEntregaServlet.class);
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigo = request.getParameter("codigo");
		String clienteIdStr = request.getParameter("clienteId");
		if(clienteIdStr == null || clienteIdStr.length()==0){
			clienteIdStr=obtenerClienteAspUser().getId().toString();
		}
		String respuesta = "";
		ClienteDireccion direccion = null;
		if(!"".equals(codigo))
			direccion = getObject(codigo, Long.valueOf(clienteIdStr));	
		if(direccion != null){
			respuesta = direccion.getDireccion().getCalle() + ", \n" + direccion.getDireccion().getNumero() + ", Bº " +
			direccion.getDireccion().getBarrio().getNombre() + ", <br>" + direccion.getDireccion().getBarrio().getLocalidad().getNombre() +
			", " + direccion.getDireccion().getBarrio().getLocalidad().getProvincia().getNombre();
		}else{
			respuesta = "";
		}		
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo recuperar el empresa", e);
			e.printStackTrace();
		}		
	}
	
	private ClienteDireccion getObject(String codigo, Long clienteId){
		ClienteDireccion direccion  = new ClienteDireccion();
		ClienteDireccionService service = new ClienteDireccionServiceImp(HibernateControl.getInstance());
		direccion = service.getByCodigo(codigo, getObject(clienteId));		
		return direccion;
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private ClienteAsp getObject(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
}
