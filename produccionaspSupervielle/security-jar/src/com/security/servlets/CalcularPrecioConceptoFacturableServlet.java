/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 28/06/2011
 */
package com.security.servlets;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.context.SecurityContextHolder;

import com.security.accesoDatos.configuraciongeneral.hibernate.ConceptoFacturableServiceImp;
import com.security.accesoDatos.configuraciongeneral.hibernate.TipoVariacionServiceImp;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.TipoVariacion;
import com.security.modelo.seguridad.User;
import com.security.recursos.RecursosNumeros;

/**
 * @author Ezequiel Beccaria
 *
 */
public class CalcularPrecioConceptoFacturableServlet extends HttpServlet{
	private static Logger logger = Logger.getLogger(CalcularPrecioConceptoFacturableServlet.class);
	private static final long serialVersionUID = -8573634511961736755L;
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigoConcepto = request.getParameter("conceptoCodigo");
		String variacionId = request.getParameter("variacionId");
		String valor = request.getParameter("valor");
		String clienteIdStr = request.getParameter("clienteId");
		
		if(clienteIdStr == null || clienteIdStr.length()==0){
			clienteIdStr=obtenerClienteAspUser().getId().toString();
		}
		
		String respuesta = "";
		BigDecimal rta = null;
		if(codigoConcepto != null && !"".equals(codigoConcepto) && variacionId != null && !"".equals(variacionId) &&
				valor != null && !"".equals(valor) && clienteIdStr != null && !"".equals(clienteIdStr))
			rta = calcularPrecio(codigoConcepto, Long.valueOf(variacionId), new BigDecimal(valor), Long.valueOf(clienteIdStr));
		if(rta != null){
			respuesta = RecursosNumeros.formatoMoneda(rta.doubleValue());
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
	
	private BigDecimal calcularPrecio(String codigoConcepto, Long variacionId, BigDecimal valor, Long clienteId){
		ConceptoFacturable concepto = new ConceptoFacturableServiceImp(HibernateControl.getInstance())
				.obtenerConceptoFacturablePorCodigo(codigoConcepto, getObject(clienteId));
		TipoVariacion variacion = new TipoVariacionServiceImp(HibernateControl.getInstance())
				.obtenerPorId(variacionId);
		return variacion.calcularMonto(concepto.getPrecioBase(), valor);
	}
	
	private ClienteAsp getObject(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}

}
