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

import com.security.accesoDatos.configuraciongeneral.hibernate.TipoElementoServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.seguridad.User;

/**
 * @author Victor Kenis
 *
 */
public class TipoElementoDescripcionServlet extends HttpServlet{
	private static final long serialVersionUID = -2135973356955496716L;
	private static Logger logger = Logger.getLogger(TipoElementoDescripcionServlet.class);

	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigo = request.getParameter("codigo");
		String mode = request.getParameter("mode");
		String tipo = request.getParameter("tipo");
		
		if(mode == null || "undefined".equals(mode) || "".equals(mode))
		{
			mode = "0";
		}
		
		if(tipo == null || "undefined".equals(tipo) || "".equals(tipo))
		{
			tipo = "0";
		}
		
		String clienteIdStr = request.getParameter("clienteId");
		if(clienteIdStr == null || clienteIdStr.length()==0){
			clienteIdStr=obtenerClienteAspUser().getId().toString();
		}
		String respuesta = "";
		
		String[] codigos;
		
		if(codigo != null && !"undefined".equals(codigo) && !"".equals(codigo.trim())){
			
			codigos = codigo.split(",");
			if(codigos.length<=1){
				TipoElemento p = getObject(codigo, Long.valueOf(clienteIdStr));
				if(p != null){
					respuesta = p.getDescripcion();
				}else{
					respuesta = "";
				}
			}else{
				for(String cod:codigos){
					
					TipoElemento p = getObject(cod, Long.valueOf(clienteIdStr));
					if(p != null){
						respuesta += p.getDescripcion() + " - ";
					}else{
						respuesta += "";
					}
				}
					
				
			}
			
			
		}
		try {
			response.getWriter().write(respuesta);		
		} catch (IOException e) {
			logger.error("No se pudo listar los tipos de elementos", e);
			e.printStackTrace();
		}		
	}
	
	private TipoElemento getObject(String codigo, Long clienteId){
		TipoElementoService service = new TipoElementoServiceImp(HibernateControl.getInstance());
		TipoElemento l = service.getByCodigo(codigo, getObject(clienteId));
			return l;
	
	}	
	
	private ClienteAsp getObject(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
