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

import com.security.accesoDatos.configuraciongeneral.hibernate.ClienteEmpServiceImp;
import com.security.accesoDatos.configuraciongeneral.hibernate.RemitoServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.seguridad.User;

/**
 * @author Gabriel Mainero
 *
 */
public class RemitoServlet extends HttpServlet{
	private static final long serialVersionUID = 7014807021831556429L;
	private static Logger logger = Logger.getLogger(RemitoServlet.class);
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		Long codigo;
		try {
			codigo = Long.parseLong(request.getParameter("codigo"));
		} catch (NumberFormatException e1) {
			codigo = null;
		}
		String clienteEmpIdStr = request.getParameter("clienteId");
		String clienteAspIdStr = request.getParameter("clienteAspId");
		String codigoEmpresa = request.getParameter("codigoEmpresa");
		
		
		ClienteAsp clienteAsp = null;
		ClienteEmp clienteEmp = null;
		
		if(clienteAspIdStr == null || clienteAspIdStr.length()==0){
			clienteAsp = obtenerClienteAspUser();
		}else{
			clienteAsp = getObject(Long.valueOf(clienteAspIdStr));
		}
		String respuesta = "";
		
		if(clienteEmpIdStr != null && clienteEmpIdStr.length()>0){
			clienteEmp = getObject(clienteEmpIdStr, codigoEmpresa, clienteAsp);
		}
		
		Remito remito = getRespuesta(codigo, clienteEmp, clienteAsp);
		
		if(clienteAsp != null && clienteEmp != null && remito != null){
			respuesta = remito.getLetraYNumeroComprobante();
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
	
	private ClienteEmp getObject(String clienteEmpId, String codigoEmpresa, ClienteAsp clienteAsp){
		ClienteEmp cliente = null;		
		ClienteEmpService service = new ClienteEmpServiceImp(HibernateControl.getInstance());
		cliente = service.getByCodigo(clienteEmpId, codigoEmpresa, clienteAsp);
		return cliente;
	}
	
	private ClienteAsp getObject(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private Remito getRespuesta(Long codigo, ClienteEmp clienteEmp, ClienteAsp clienteAsp){
		RemitoService service = new RemitoServiceImp(HibernateControl.getInstance());
		return service.getByCodigo(codigo, clienteEmp, clienteAsp);
	}
}
