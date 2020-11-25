/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 23/06/2011
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

import com.security.accesoDatos.configuraciongeneral.hibernate.AfipTipoComprobanteServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.seguridad.User;

/**
 * @author Gonzalo Noriega
 *
 */
public class TipoComprobantePorTipoSerieServlet extends HttpServlet{
	private static final long serialVersionUID = 7100679955581101875L;
	private static Logger logger = Logger.getLogger(TipoComprobantePorTipoSerieServlet.class);
	
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {	
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String tipo = request.getParameter("val");
	
		String respuesta = "";
		
		for(AfipTipoComprobante p:getAfipTipoComprobantes(tipo)){
			if("".equals(respuesta))
				respuesta = p.getDescripcion()+"-"+p.getId();
			else
				respuesta = respuesta+"|"+p.getDescripcion()+"-"+p.getId();
		}
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar las provincias", e);
			e.printStackTrace();
		}		
	}
	
	private List<AfipTipoComprobante> getAfipTipoComprobantes(String tipo){
		AfipTipoComprobante afip = new AfipTipoComprobante();
		afip.setTipo(tipo);
		AfipTipoComprobanteService service = new AfipTipoComprobanteServiceImp(HibernateControl.getInstance());
		List<AfipTipoComprobante> tipos = service.listarTipoComprobanteFiltrados(afip);
		return tipos;
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
