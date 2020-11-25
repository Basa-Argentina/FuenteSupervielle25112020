/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.context.SecurityContextHolder;

import com.security.accesoDatos.configuraciongeneral.hibernate.DepositoServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Seccion;
import com.security.modelo.seguridad.User;

/**
 * @author Gonzalo Noriega
 *
 */
public class SeccionPorDepositoServlet extends HttpServlet{
	private static final long serialVersionUID = 7100679955581101875L;
	private static Logger logger = Logger.getLogger(SeccionPorDepositoServlet.class);
	
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {	
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String id = request.getParameter("val");
	
		String respuesta = "";
		Deposito deposito = getDesposito(Long.valueOf(id));
		if(deposito != null){
			for(Seccion p:deposito.getSecciones()){
				if("".equals(respuesta)){
					respuesta = "Seleccione una Sección"+"-"+0;
					respuesta = respuesta+"|"+p.getDescripcion()+"-"+p.getId();
				}
				else
					respuesta = respuesta+"|"+p.getDescripcion()+"-"+p.getId();
			}
		}
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar las provincias", e);
			e.printStackTrace();
		}		
	}
	
	private Deposito getDesposito(Long id){
		DepositoService service = new DepositoServiceImp(HibernateControl.getInstance());
		Deposito deposito = service.obtenerPorId(id);
		return deposito;
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
