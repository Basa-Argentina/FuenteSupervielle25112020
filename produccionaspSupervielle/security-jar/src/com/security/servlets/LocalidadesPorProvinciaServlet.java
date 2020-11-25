/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 24/05/2011
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

import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.hibernate.LocalidadServiceImp;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.general.Localidad;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
public class LocalidadesPorProvinciaServlet extends HttpServlet{
	private static final long serialVersionUID = 219776640635261725L;
	private static Logger logger = Logger.getLogger(LocalidadesPorProvinciaServlet.class);
	
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {	
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String id = request.getParameter("val");
		String nombre = request.getParameter("nom");
		
		String respuesta = "";
		if(id != null && !"undefined".equals(id) && !"".equals(id)){
			if(nombre != null && !"undefined".equals(nombre)){
				Localidad l = getObject(Long.valueOf(id), nombre);
				if(l != null)
					respuesta = l.getNombre()+"-"+l.getId();
			}else{
				for(Localidad l:getObject(Long.valueOf(id))){
					if("".equals(respuesta))
						respuesta = l.getNombre()+"-"+l.getId();
					else
						respuesta = respuesta+"|"+l.getNombre()+"-"+l.getId();
				}
			}	
		}
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar las provincias", e);
			e.printStackTrace();
		}		
	}
	
	private List<Localidad> getObject(Long id){
		LocalidadService service = new LocalidadServiceImp(HibernateControl.getInstance());
		return service.listarLocalidadesPorProcinciaId(id);
	}
	
	private Localidad getObject(Long id, String nombre){
		LocalidadService service = new LocalidadServiceImp(HibernateControl.getInstance());
		List<Localidad> l = service.listarLocalidadesPopup(id, nombre);		
		if(!l.isEmpty())
			return l.get(0);
		else
			return null;
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
