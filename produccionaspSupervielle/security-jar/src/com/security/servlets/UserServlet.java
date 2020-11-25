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

import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.hibernate.UserServiceImp;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.seguridad.User;

/**
 * @author Victor Kenis
 *
 */
public class UserServlet extends HttpServlet{
	private static final long serialVersionUID = -2135973356955496716L;
	private static Logger logger = Logger.getLogger(UserServlet.class);

	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigo = request.getParameter("codigo");
		
		String respuesta = "";
		User lista = null;
		if(!"".equals(codigo))
			lista = getObject(Long.valueOf(codigo));	
		if(lista != null){
			respuesta = lista.getPersona().toString();
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
	
	private User getObject(Long userId){
		UserService service = new UserServiceImp(HibernateControl.getInstance());
		User user = service.obtenerPorIdNoPersonal(userId);
		if(user != null)
			return user;
		else
			return null;
	}
	
}
