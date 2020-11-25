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

import com.security.accesoDatos.configuraciongeneral.hibernate.ClasificacionDocumentalServiceImp;
import com.security.accesoDatos.configuraciongeneral.hibernate.ClienteEmpServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
public class ClasificacionDocumentalUltimoCodigoServlet extends HttpServlet{
	private static final long serialVersionUID = 7014807021831556429L;
	private static Logger logger = Logger.getLogger(ClasificacionDocumentalUltimoCodigoServlet.class);
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String clienteEmpIdStr = request.getParameter("clienteEmpId");
		String clienteAspIdStr = request.getParameter("clienteAspId");
		
		ClienteAsp clienteAsp = null;
		ClienteEmp clienteEmp = null;
		
		if(clienteAspIdStr == null || clienteAspIdStr.length()==0){
			clienteAsp = obtenerClienteAspUser();
		}else{
			clienteAsp = getObject(Long.valueOf(clienteAspIdStr));
		}
		String respuesta = "";
		
		if(clienteEmpIdStr != null && clienteEmpIdStr.length()>0){
			clienteEmp = getObject(Long.valueOf(clienteEmpIdStr), clienteAsp);
		}
		
		if(clienteAsp != null && clienteEmp != null){
			respuesta = getRespuesta(clienteEmp, clienteAsp).toString();
		}else{
			respuesta = "1";
		}
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar las provincias", e);
			e.printStackTrace();
		}
		
	}
	
	private ClienteEmp getObject(Long clienteEmpId, ClienteAsp clienteAsp){
		ClienteEmp cliente = null;		
		ClienteEmpService service = new ClienteEmpServiceImp(HibernateControl.getInstance());
		cliente = service.getById(clienteEmpId, clienteAsp);		
		return cliente;
	}
	
	private ClienteAsp getObject(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private Integer getRespuesta(ClienteEmp clienteEmp, ClienteAsp clienteAsp){
		ClasificacionDocumentalService service = new ClasificacionDocumentalServiceImp(HibernateControl.getInstance());
		return service.getProximoCodigoByClienteEmp(clienteEmp, clienteAsp);
	}
}
