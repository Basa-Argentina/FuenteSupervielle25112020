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

import com.security.accesoDatos.configuraciongeneral.hibernate.AfipTipoComprobanteServiceImp;
import com.security.accesoDatos.configuraciongeneral.hibernate.EmpresaServiceImp;
import com.security.accesoDatos.configuraciongeneral.hibernate.SerieServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.ParseNumberUtils;
/**
 * parametros<br>
 * codigo = serie.codigo<br>
 * codigoEmpresa = serie.empresa.codigo<br>
 * codigoSucursal = serie.sucursal.codigo<br>
 * codigoCliente = se utiliza para obtener la condicion de iva del cliente (clienteEmp.afipCondIva.abreviatura) y compararla con serie.condIvaClientes<br>
 * idAfipTipoComprobante = serie.afipTipoComprobante.id<br>
 * <br>
 * @return serie.descripcion;*;serie.prefijo;*;serie.ultNroImpreso
 */
public class SerieServletParaFactura2 extends HttpServlet{
	private static final long serialVersionUID = -2135973356955496716L;
	private static Logger logger = Logger.getLogger(SerieServletParaFactura2.class);
	
/**
 * parametros<br>
 * codigo = serie.codigo<br>
 * habilitado = serie.habilitado <br>
 * codigoEmpresa = serie.empresa.codigo<br>
 * codigoSucursal = serie.sucursal.codigo<br>
 * codigoCliente = se utiliza para obtener la condicion de iva del cliente (clienteEmp.afipCondIva.abreviatura) y compararla con serie.condIvaClientes<br>
 * idAfipTipoComprobante = serie.afipTipoComprobante.id<br>
 * <br>
 * @return serie.descripcion;*;serie.prefijo;*;serie.ultNroImpreso
 */
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigo = request.getParameter("codigo");
		String codigoEmpresa = request.getParameter("codigoEmpresa");
	
		String idAfipTipoComprobanteStr = request.getParameter("idAfipTipoComprobante");
		String habilitadoStr = request.getParameter("habilitado");
		ClienteAsp clienteAsp = obtenerClienteAsp();
		
		Empresa empresa = obtenerEmpresa();
		if(codigoEmpresa != null && codigoEmpresa.length()>0){
			empresa = serviceObtenerEmpresa(codigoEmpresa, clienteAsp);
		}
		
		Long idAfipTipoComprobante = null;
		try{
			idAfipTipoComprobante = Long.valueOf(idAfipTipoComprobanteStr);
		}catch (NumberFormatException e){
			idAfipTipoComprobante = null;
		}
		AfipTipoComprobante afipTipoComprobante = null; 
		if(idAfipTipoComprobante!=null && idAfipTipoComprobante>0){
			afipTipoComprobante = serviceObtenerAfipTipoComprobante(idAfipTipoComprobante);
		}
		
		Serie serieBusqueda = new Serie();
		serieBusqueda.setCodigo(codigo);		
		serieBusqueda.setEmpresa(empresa);
		serieBusqueda.setAfipTipoComprobante(afipTipoComprobante);
		if(habilitadoStr != null && habilitadoStr.length()>0){
			Boolean habilitado = true;
			try{
				habilitado = Boolean.valueOf(habilitadoStr);
			}catch (Exception e){
				habilitado = true;
			}
			serieBusqueda.setHabilitado(habilitado);
		}else{
			serieBusqueda.setHabilitado(true);
		}
		
		String respuesta = "";
		Serie lista = null;
		if(!"".equals(codigo))
			lista = getObject(serieBusqueda, clienteAsp);	
		if(lista != null){
			Long ultNro = ParseNumberUtils.parseLongCodigo(lista.getUltNroImpreso()) + 1L;			
			respuesta = lista.getDescripcion()+ ";*;" + lista.getPrefijo() + ";*;" + ParseNumberUtils.parseStringCodigo(ultNro, 8);
		}else{
			respuesta = "";
		}		
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar las series", e);
			e.printStackTrace();
		}		
	}
	
	private Serie getObject(Serie serieBusqueda, ClienteAsp clienteAsp){
		SerieService service = new SerieServiceImp(HibernateControl.getInstance());
		Serie tipo = service.obtenerSerieFiltradaServlet(serieBusqueda, clienteAsp);
		if(tipo != null)
			return tipo;
		else
			return null;
	}
	
	private Empresa serviceObtenerEmpresa(String codigo, ClienteAsp clienteAsp){
		EmpresaService service = new EmpresaServiceImp(HibernateControl.getInstance());
		Empresa tipo = service.getByCodigo(codigo, clienteAsp);
		if(tipo != null)
			return tipo;
		else
			return null;
	}
	
	private AfipTipoComprobante serviceObtenerAfipTipoComprobante(Long idAfipTipoComprobante){
		AfipTipoComprobanteService service = new AfipTipoComprobanteServiceImp(HibernateControl.getInstance());
		AfipTipoComprobante tipo = service.obtenerPorId(idAfipTipoComprobante);
		if(tipo != null)
			return tipo;
		else
			return null;
	}
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	
	private Empresa obtenerEmpresa(){
		User usuario = obtenerUser();
		return ((PersonaFisica)usuario.getPersona()).getEmpresaDefecto();
	}
}
