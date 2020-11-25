/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/06/2011
 */
package com.security.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.context.SecurityContextHolder;

import com.security.accesoDatos.configuraciongeneral.hibernate.AfipTipoComprobanteServiceImp;
import com.security.accesoDatos.configuraciongeneral.hibernate.ClienteEmpServiceImp;
import com.security.accesoDatos.configuraciongeneral.hibernate.EmpresaServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;

/**
 * @author Ezequiel Beccaria
 *
 */
public class ClientesCodigoYCodigoCondicionAfipServlet extends HttpServlet{
	private static final long serialVersionUID = 7014807021831556429L;
	private static Logger logger = Logger.getLogger(ClientesCodigoYCodigoCondicionAfipServlet.class);
	
	/**
	 * codigo = codigo de ClienteEmp
	 * codigoEmpresa = codigo Empresa
	 * modo = F , A // F = devuelve solo los tipo de comprobantes correspondientes a facturas; A = Todos
	 * clienteId = id cliente ASP
	 * @return clienteEmp.nomORazonSocial;*;clienteEmp.afipCondIva.codigo;*;..."option afipTiposComprobante option"...
	 */
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml"); 
		response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
		response.setHeader("Cache-Control", "no-cache");
		
		String codigo = request.getParameter("codigo");
		String clienteIdStr = request.getParameter("clienteId");
		String codigoEmpresa = request.getParameter("codigoEmpresa");
		String habilitadoStr = request.getParameter("habilitado");
		String modo = request.getParameter("modo");
		if(clienteIdStr == null || clienteIdStr.length()==0){
			clienteIdStr=obtenerClienteAsp().getId().toString();
		}
		Boolean habilitado=null;
		if(habilitadoStr != null && habilitadoStr.length() > 0)
		{
			try{
				habilitado = Boolean.valueOf(habilitadoStr);
			}catch(Exception e){
				habilitado=null;
			}
		}
		StringBuilder respuestaBuilder= new StringBuilder("");
		String respuesta = "";
		ClienteEmp cliente = null;
		if(!"".equals(codigo))
			cliente = getObject(codigo, codigoEmpresa, Long.valueOf(clienteIdStr),habilitado);	
		if(cliente != null){
			respuestaBuilder.append(cliente.getRazonSocialONombreYApellido()).append(";*;")
				.append(cliente.getAfipCondIva().getCodigo()).append(";*;")
				.append("<option value=\"0\" >Seleccionar</option>\n");
				AfipTipoComprobante afipTipoComprobante = new AfipTipoComprobante();
				//se cargan los codigos para filtrar los tipos de comprobantes que aplican a factura
				if("F".equals(modo)){
					ArrayList<String> codigos = new ArrayList<String>();
					codigos.add("001");
					codigos.add("002");
					codigos.add("003");
					codigos.add("006");
					codigos.add("007");
					codigos.add("008");
					afipTipoComprobante.setCodigos(codigos);
				}
				List<AfipTipoComprobante> listaAfipTipoComprobantes = obtenerAfipTiposComprobantes(afipTipoComprobante);
				//genero la respuesta
				for ( AfipTipoComprobante tipo : listaAfipTipoComprobantes){
					respuestaBuilder.append("<option value =\"").append(tipo.getId()).append("\" ");
					
					EmpresaService service = new EmpresaServiceImp(HibernateControl.getInstance());
					Empresa empresa = service.getByCodigoConCondAfip(codigoEmpresa, obtenerClienteAsp());
					if(empresa != null && empresa.getAfipCondIva()!=null && "1".equals(empresa.getAfipCondIva().getCodigo()) && "1".equals(cliente.getAfipCondIva().getCodigo()) &&
							("006".equals(tipo.getCodigo()) || "007".equals(tipo.getCodigo()) || "008".equals(tipo.getCodigo()))){
						respuestaBuilder.append(" disabled=\"disabled\" ");						
					}else if((empresa != null && empresa.getAfipCondIva()!=null && !"1".equals(empresa.getAfipCondIva().getCodigo()) || !"1".equals(cliente.getAfipCondIva().getCodigo())) &&
							("001".equals(tipo.getCodigo()) || "002".equals(tipo.getCodigo()) || "003".equals(tipo.getCodigo()) )){
						respuestaBuilder.append(" disabled=\"disabled\" ");
					}else{
						//TODO solo si la empresa no tiene condicion de iva 
						//respuestaBuilder.append(" disabled=\"disabled\"");
					}
					respuestaBuilder.append(">")
						.append(tipo.getDescripcion())
						.append("</option>");
				}
				respuesta = respuestaBuilder.toString();
		}else{
			respuesta = ";*;;*;<option value=\"0\" >Seleccionar</option>";
		}		
		try {
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo listar las provincias", e);
			e.printStackTrace();
		}
	}
	
	private ClienteEmp getObject(String codigoClienteEmp,String codigoEmpresa, Long clienteId, Boolean habilitado){
		ClienteEmp clienteEmp = new ClienteEmp();
		clienteEmp.setCodigo(codigoClienteEmp);
		clienteEmp.setCodigoEmpresa(codigoEmpresa);
		clienteEmp.setHabilitado(habilitado);
		ClienteEmpService service = new ClienteEmpServiceImp(HibernateControl.getInstance());
		clienteEmp = service.getByCodigoFactura(clienteEmp, getObject(clienteId));
		return clienteEmp;
	}
	
	private ClienteAsp getObject(Long id){
		ClienteAspService service = new ClienteAspServiceImp(HibernateControl.getInstance());
		return service.obtenerPorId(id);
	}
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	private Empresa obtenerEmpresaDefault(){
		return ((PersonaFisica)obtenerClienteAsp().getContacto()).getEmpresaDefecto();
	}
	private Sucursal obtenerSucursalDefault(){
		return ((PersonaFisica)obtenerClienteAsp().getContacto()).getSucursalDefecto();
	}
	
	private List<AfipTipoComprobante> obtenerAfipTiposComprobantes(AfipTipoComprobante afipTipoComprobante){
		AfipTipoComprobanteService afipTipoComprobanteService = new AfipTipoComprobanteServiceImp(HibernateControl.getInstance());
		
		return afipTipoComprobanteService.listarTipoComprobanteFiltrados(afipTipoComprobante);
	}
	
}
