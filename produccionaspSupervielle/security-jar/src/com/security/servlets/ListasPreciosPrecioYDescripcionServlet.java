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

import com.security.accesoDatos.configuraciongeneral.hibernate.ClienteEmpServiceImp;
import com.security.accesoDatos.configuraciongeneral.hibernate.ListaPreciosServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.seguridad.User;


public class ListasPreciosPrecioYDescripcionServlet extends HttpServlet{
	private static final long serialVersionUID = 3992770880977886067L;
	private static Logger logger = Logger.getLogger(ListasPreciosPrecioYDescripcionServlet.class);
	
	/**
	 * 	respuesta = detalleSeleccionado.getCalcularMonto()+ ";*;" + lista.getDescripcion();
	 *
	 */
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigo = request.getParameter("codigo");
		String clienteIdStr = request.getParameter("clienteId");
		String codigoConceptoFacturable = request.getParameter("codigoConceptoFacturable");
		Boolean habilitado = Boolean.valueOf(request.getParameter("habilitado"));
		String codigoClienteEmp = request.getParameter("codigoClienteEmp");
		String codigoEmpresa = request.getParameter("codigoEmpresa");
		ClienteEmp clienteEmp = null;
		
		if(clienteIdStr == null || clienteIdStr.length()==0){
			clienteIdStr=obtenerClienteAspUser().getId().toString();
		}
		
		if(codigoClienteEmp != null && codigoClienteEmp.length()>0){
			clienteEmp = getClienteEmp(codigoClienteEmp, codigoEmpresa, obtenerClienteAspUser());
		}
		
		String respuesta = "";
		ListaPrecios lista = null;
		if(!"".equals(codigo)){
			lista = getObject(codigo, Long.valueOf(clienteIdStr), codigoConceptoFacturable, clienteEmp,  habilitado);	
		}
		
		if(lista != null){
			ListaPreciosDetalle detalleSeleccionado = null;
			for(ListaPreciosDetalle detalle:lista.getDetalle()){
				if(detalle.getConceptoFacturable().getCodigo().equals(codigoConceptoFacturable)){
					detalleSeleccionado = detalle;
					break;
				}
			}
			if(detalleSeleccionado!=null){
				respuesta = detalleSeleccionado.getCalcularMonto()+ ";*;" + lista.getDescripcion() +" / "+ lista.getValor() +"%";
			}
		}else{
			respuesta = "";
		}
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar las listas de precio", e);
			e.printStackTrace();
		}		
	}
	
	private ListaPrecios getObject(String codigo, Long clienteId, String codigoConceptoFacturable, ClienteEmp clienteEmp, Boolean habilitado){
		ListaPreciosService service = new ListaPreciosServiceImp(HibernateControl.getInstance());		
		ListaPrecios lista = service.obtenerListaPreciosPorCodigo(codigo, getObject(clienteId), codigoConceptoFacturable,clienteEmp, habilitado);
		if(lista != null)
			return lista;
		else
			return null;
	}
	
	private ClienteEmp getClienteEmp(String codigoClienteEmp, String codigoEmpresa, ClienteAsp clienteAsp){
		ClienteEmpService service = new ClienteEmpServiceImp(HibernateControl.getInstance());
		return service.getByCodigo(codigoClienteEmp, codigoEmpresa, clienteAsp, true);
	}
	
	private ClienteAsp getObject(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
