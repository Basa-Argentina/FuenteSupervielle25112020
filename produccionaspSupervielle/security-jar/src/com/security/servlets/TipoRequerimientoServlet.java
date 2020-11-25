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

import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.accesoDatos.jerarquias.hibernate.TipoRequerimientoServiceImp;
import com.security.accesoDatos.jerarquias.interfaz.TipoRequerimientoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.jerarquias.TipoRequerimiento;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
public class TipoRequerimientoServlet extends HttpServlet{
	private static final long serialVersionUID = -2135973356955496716L;
	private static Logger logger = Logger.getLogger(TipoRequerimientoServlet.class);

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
		TipoRequerimiento lista = null;
		if(!"".equals(codigo))
			lista = getObject(codigo, Long.valueOf(clienteIdStr));	
		if(lista != null){
			respuesta = lista.getDescripcion();
		}else{
			respuesta = "";
		}		
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar los conceptos facturables", e);
			e.printStackTrace();
		}		
	}
	
	private TipoRequerimiento getObject(String codigo, Long clienteId){
		TipoRequerimientoService service = new TipoRequerimientoServiceImp(HibernateControl.getInstance());
		TipoRequerimiento tipo = service.obtenerPorCodigo(codigo, getObject(clienteId));
		if(tipo != null)
		{
			Empleado usuario = null;
			try{
				usuario = (Empleado)obtenerUser();
			}catch(ClassCastException e){
				
			}
			
			if(usuario!=null)
			{
				if(usuario.getClienteEmp().getId().longValue() == 20042){
					if(usuario.getId().longValue() != 60592 && usuario.getId().longValue() != 60593)
					{
						if(tipo.getId().longValue()!= 1 && tipo.getId().longValue()!= 9 && tipo.getId().longValue()!= 11)
						{
							tipo = null;
						}
					}
				}
			}
			return tipo;
		}
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
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
