/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 28/06/2011
 */
package com.security.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.context.SecurityContextHolder;



import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.accesoDatos.jerarquias.hibernate.TipoOperacionServiceImp;
import com.security.accesoDatos.jerarquias.interfaz.TipoOperacionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.jerarquias.TipoOperacion;
import com.security.modelo.seguridad.User;

/**
 * @author 
 *
 */
public class TipoOperacionesServlet extends HttpServlet{
	private static final long serialVersionUID = -2135973356955496716L;
	private static Logger logger = Logger.getLogger(TipoOperacionesServlet.class);

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
		if(!"".equals(codigo)){
			for(String c:codigo.split(",")){
				TipoOperacion tipoOp = getObject(c, Long.valueOf(clienteIdStr));
				if(tipoOp!=null){
					respuesta+= tipoOp.getDescripcion()+", ";
				}
			}
		}		
			
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar los conceptos facturables", e);
			e.printStackTrace();
		}		
	}
	
	private TipoOperacion getObject(String codigo, Long clienteId){
		TipoOperacionService service = new TipoOperacionServiceImp(HibernateControl.getInstance());
		TipoOperacion tipo = service.obtenerTipoOperacionPorCodigo(codigo, getObject(clienteId));
		if(tipo != null)
			return tipo;
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
