/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/06/2011
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
import com.security.accesoDatos.configuraciongeneral.hibernate.SucursalServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
public class DepositosPorCodigoSucursalServlet extends HttpServlet{
	private static final long serialVersionUID = 7014807021831556429L;
	private static Logger logger = Logger.getLogger(DepositosPorCodigoSucursalServlet.class);
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		String codigo = request.getParameter("codigo");
		String clienteIdStr = request.getParameter("clienteId");
		String sucursalIdStr = request.getParameter("sucursalId");
		
		if(clienteIdStr == null && clienteIdStr.length()==0){
			clienteIdStr=obtenerClienteAsp().getId().toString();
		}
		
		
		String respuesta = "";
		Deposito deposito = null;
		if(!"".equals(codigo))
			deposito = getObject(codigo, Long.valueOf(clienteIdStr), sucursalIdStr);	
		if(deposito != null){
			respuesta = deposito.getDescripcion();
		}else{
			respuesta = "";
		}		
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo recuperar el deposito", e);
			e.printStackTrace();
		}		
	}
	
	private Deposito getObject(String codigo, Long clienteId, String codigoSucursal){
		Deposito deposito = new Deposito();
		DepositoService service = new DepositoServiceImp(HibernateControl.getInstance());
		deposito = service.getByCodigoYSucursal(codigo, codigoSucursal,getClienteAsp(clienteId));		
		return deposito;
	}
	
	private ClienteAsp getClienteAsp(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}

}
