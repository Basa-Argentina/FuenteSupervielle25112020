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

import com.security.accesoDatos.configuraciongeneral.hibernate.TransporteServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Transporte;
import com.security.modelo.seguridad.User;

/**
 * @author Victor Kenis
 *
 */
public class TransportesServlet extends HttpServlet{
	private static final long serialVersionUID = -2135973356955496716L;
	private static Logger logger = Logger.getLogger(TransportesServlet.class);

	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		Integer codigo = null;
		String codigoStr = request.getParameter("codigo");
		String codigoEmpresa = request.getParameter("codigoEmpresa");
		String habilitadoStr = request.getParameter("habilitado");
		Boolean habilitado = null;
		String clienteIdStr = request.getParameter("clienteId");
		
		if(clienteIdStr == null || clienteIdStr.length()==0){
			clienteIdStr=obtenerClienteAspUser().getId().toString();
		}
		
		if(codigoStr!=null && !"".equals(codigoStr))
		{
			codigo = Integer.valueOf(codigoStr);
		}
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
		
		String respuesta = "";
		Transporte lista = null;
		if(codigo != null)
			lista = getObject(codigo, codigoEmpresa, Long.valueOf(clienteIdStr),habilitado);	
		if(lista != null){
			respuesta = lista.getDescripcion();
		}else{
			respuesta = "";
		}		
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar los transportes", e);
			e.printStackTrace();
		}		
	}
	
	private Transporte getObject(Integer codigo, String codigoEmpresa, Long clienteId, Boolean habilitado){
		TransporteService service = new TransporteServiceImp(HibernateControl.getInstance());
		Transporte transporte = service.obtenerPorCodigo(codigo, codigoEmpresa, habilitado,getObject(clienteId));
		if(transporte != null)
			return transporte;
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
