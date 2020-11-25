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

import com.security.accesoDatos.configuraciongeneral.hibernate.ImpuestoServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.ImpuestoService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Impuesto;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
public class ImpuestosServlet extends HttpServlet{
	private static final long serialVersionUID = 7014807021831556429L;
	private static Logger logger = Logger.getLogger(ImpuestosServlet.class);
	
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
		Impuesto impuesto = null;
		if(!"".equals(codigo))
			impuesto = getObject(codigo, Long.valueOf(clienteIdStr));	
		if(impuesto != null){
			respuesta = impuesto.getDescripcion();
		}else{
			respuesta = "";
		}		
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar las provincias", e);
			e.printStackTrace();
		}		
	}
	
	private Impuesto getObject(String codigo, Long clienteId){
		ImpuestoService service = new ImpuestoServiceImp(HibernateControl.getInstance());		
		Impuesto impuesto = service.obtenerPorCodigo(codigo, getObject(clienteId));
		if(impuesto != null)
			return impuesto;
		else
			return null;
	}
	
	private ClienteAsp getObject(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
