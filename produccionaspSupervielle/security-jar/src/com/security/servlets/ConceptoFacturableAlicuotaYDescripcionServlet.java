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

import com.security.accesoDatos.configuraciongeneral.hibernate.ConceptoFacturableServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Impuesto;
import com.security.modelo.seguridad.User;

/**
 * respuesta = descripcion + impuesto.descripcion + ";*;" + impuesto.tipo + ";*;" + impuesto.alicuota;
 *
 */
public class ConceptoFacturableAlicuotaYDescripcionServlet extends HttpServlet{
	private static final long serialVersionUID = -2135973356955496716L;
	private static Logger logger = Logger.getLogger(ConceptoFacturableAlicuotaYDescripcionServlet.class);
	public static String SPLIT_CHARACTER = ";*;";
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigo = request.getParameter("codigo");
		String mode = request.getParameter("mode");
		String tipo = request.getParameter("tipo");
		Boolean habilitado = Boolean.valueOf(request.getParameter("habilitado"));
		
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
		if(codigo != null && !"undefined".equals(codigo) && !"".equals(codigo.trim())){
			ConceptoFacturable p = getObject(codigo,Long.valueOf(clienteIdStr),Integer.valueOf(mode),Integer.valueOf(tipo), habilitado);
			if(p != null){
				Impuesto imp = p.getImpuesto();
				if(imp != null){
					respuesta = p.getDescripcion() + SPLIT_CHARACTER + imp.getDescripcion() + SPLIT_CHARACTER + imp.getTipo() + SPLIT_CHARACTER + imp.getAlicuota();
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
	
	private ConceptoFacturable getObject(String codigo, Long clienteId, int mode,int tipo, Boolean habilitado ){
		ConceptoFacturableService service = new ConceptoFacturableServiceImp(HibernateControl.getInstance());
		List<ConceptoFacturable> l = service.listarConceptosFacturablesPopup(codigo, getObject(clienteId), mode, tipo, habilitado);
		if(!l.isEmpty())
			return l.get(0);
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
