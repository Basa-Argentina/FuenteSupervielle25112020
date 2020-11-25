/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 28/06/2011
 */
package com.security.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.context.SecurityContextHolder;

import com.security.accesoDatos.configuraciongeneral.hibernate.EmpleadoServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.seguridad.User;

/**
 * @author Victor Kenis
 *
 */
public class EmpleadosReturnCodigoDireccionServlet extends HttpServlet{
	private static final long serialVersionUID = -2135973356955496716L;
	private static Logger logger = Logger.getLogger(EmpleadosReturnCodigoDireccionServlet.class);

	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigo = request.getParameter("codigo");
		String clienteIdStr = request.getParameter("clienteId");
		String clienteEmpIdStr = request.getParameter("clienteEmpId");
		String habilitadoStr = request.getParameter("habilitado");
		Boolean habilitado = null;
		if(habilitadoStr != null && habilitadoStr.length() > 0)
		{
			if("true".equals(habilitadoStr))
			{
				habilitado = true;
			}
			if("false".equals(habilitadoStr))
			{
				habilitado = false;
			}
		}
		if(clienteIdStr == null || clienteIdStr.length()==0){
			clienteIdStr=obtenerClienteAspUser().getId().toString();
		}
		String respuesta = "";
		Empleado lista = null;
		if(!"".equals(codigo))
			lista = getObject(codigo, clienteEmpIdStr, Long.valueOf(clienteIdStr),habilitado);	
		if(lista != null){
			if(lista.getDireccionDefecto() != null)
			{
			respuesta = lista.getNombreYApellido()+"-"+lista.getDireccionDefecto().getCodigo();
			}
			else
			{respuesta = lista.getNombreYApellido()+"-"+" ";}
		}else{
			respuesta = "";
		}		
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar los empleados", e);
			e.printStackTrace();
		}		
	}
	
	private Empleado getObject(String codigo, String clienteEmpIdStr, Long clienteId,Boolean habilitado){
		EmpleadoService service = new EmpleadoServiceImp(HibernateControl.getInstance());
		Empleado empleado = service.getByCodigo(codigo,clienteEmpIdStr,getObject(clienteId),habilitado);
		if(empleado != null)
			return empleado;
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
