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
import com.security.accesoDatos.hibernate.ProvinciaServiceImp;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.general.Provincia;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
public class ProvinciasPorPaisServlet extends HttpServlet{
	private static final long serialVersionUID = 7100679955581101875L;
	private static Logger logger = Logger.getLogger(ProvinciasPorPaisServlet.class);
	
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
				Provincia p = getProvincia(Long.valueOf(id), nombre);
				if(p != null)
					respuesta = p.getNombre()+"-"+p.getId();
			}else{
				for(Provincia p:getProvincias(Long.valueOf(id))){
					if("".equals(respuesta))
						respuesta = p.getNombre()+"-"+p.getId();
					else
						respuesta = respuesta+"|"+p.getNombre()+"-"+p.getId();
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
	
	private List<Provincia> getProvincias(Long id){
		ProvinciaService service = new ProvinciaServiceImp(HibernateControl.getInstance());
		return service.listarProvinciasPorPaisId(id);
	}
	
	private Provincia getProvincia(Long id, String nombre){
		ProvinciaService service = new ProvinciaServiceImp(HibernateControl.getInstance());
		List<Provincia> l = service.listarProvinciasPopup(id, nombre);
		if(!l.isEmpty())
			return l.get(0);
		else
			return null;
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
