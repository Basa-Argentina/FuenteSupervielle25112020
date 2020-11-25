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

import com.security.accesoDatos.configuraciongeneral.hibernate.SucursalServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.seguridad.User;

/**
 * @author Victor Kenis
 *
 */
public class SucursalesPorCodigoEmpresaServlet extends HttpServlet{
	private static final long serialVersionUID = 7014807021831556429L;
	private static Logger logger = Logger.getLogger(SucursalesPorCodigoEmpresaServlet.class);
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigo = request.getParameter("codigo");
		String clienteIdStr = request.getParameter("clienteId");
		String codigoEmpresa = request.getParameter("codigoEmpresa");
		if(clienteIdStr == null || clienteIdStr.length()==0){
			clienteIdStr=obtenerClienteAspUser().getId().toString();
		}
		String respuesta = "";
		Sucursal sucursal = null;
		if(!"".equals(codigo))
			sucursal = getObject(codigo, codigoEmpresa,Long.valueOf(clienteIdStr));	
		if(sucursal != null){
			respuesta = sucursal.getDescripcion();
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
	
	private Sucursal getObject(String codigo, String codigoEmpresa,Long clienteId){
		Sucursal sucursal  = new Sucursal();
		SucursalService service = new SucursalServiceImp(HibernateControl.getInstance());
		sucursal = service.getByCodigo(codigo, codigoEmpresa,getObject(clienteId));		
		return sucursal;
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private ClienteAsp getObject(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
}
