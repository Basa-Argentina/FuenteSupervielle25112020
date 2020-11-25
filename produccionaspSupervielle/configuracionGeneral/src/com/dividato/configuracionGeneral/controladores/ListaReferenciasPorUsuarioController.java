/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.controladores.FormLoteReferenciaController.ReferenciaReporte;
import com.dividato.configuracionGeneral.objectForms.ImpuestoBusquedaForm;
import com.dividato.configuracionGeneral.objectForms.ReferenciasPorUsuarioReport;
import com.dividato.configuracionGeneral.validadores.ImpuestoFormValidator;
import com.dividato.configuracionGeneral.validadores.ReferenciaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ImpuestoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaHistoricoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Impuesto;
import com.security.modelo.configuraciongeneral.LoteReferencia;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.ReferenciaHistorico;
import com.security.modelo.general.Persona;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * @author Victor Kenis
 *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarReferenciasPorUsuario.html",
				"/mostrarReferenciasPorUsuario.html",
				"/filtrarReferenciasPorUsuario.html",
				"/imprimirReferenciasPorUsuario.html",
				"/imprimirReferenciasPorUsuarioXls.html",
				"/imprimirReferenciasPorUsuarioDetalle.html"
			}
		)
public class ListaReferenciasPorUsuarioController {
	private ReferenciaHistoricoService referenciaHistoricoService;
	private ReferenciaService referenciaService;
	private UserService userService;
	
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setReferenciaHistoricoService(ReferenciaHistoricoService referenciaHistoricoService) {
		this.referenciaHistoricoService = referenciaHistoricoService;
	}

	@RequestMapping(value="/iniciarReferenciasPorUsuario.html", method = RequestMethod.GET)
	public String iniciar(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("referenciasPorUsuarioBusqueda");
		return "redirect:mostrarReferenciasPorUsuario.html";
	}
	
	@RequestMapping(value="/mostrarReferenciasPorUsuario.html", method = RequestMethod.GET)
	public String mostrar(HttpSession session, Map<String,Object> atributos){
				
		ReferenciaHistorico historicoBusqueda = new ReferenciaHistorico();
		String codigoEmpresa = "0001";
		int size = 20;
		
		atributos.put("size", size);
		
		atributos.put("historicoBusqueda", historicoBusqueda);
		atributos.put("codigoEmpesa", codigoEmpresa);
		return "consultaReferenciasPorUsuario";
	}
	
//	@RequestMapping(value="/filtrarReferenciasPorUsuario.html", method = RequestMethod.POST)
//	public String filtrar(
//			@ModelAttribute("historicoBusqueda") ReferenciaHistorico referenciaHistorico, 
//			BindingResult result,
//			HttpSession session,
//			Map<String,Object> atributos,HttpServletResponse response){
//		
//		User usuario = null;
//		if(referenciaHistorico!=null && referenciaHistorico.getCodigoUsuario()!=null)
//			usuario = userService.obtenerPorId(referenciaHistorico.getCodigoUsuario());
//		if(usuario==null)
//			result.rejectValue("", "");
//		
//		if(!result.hasErrors()){
//			session.setAttribute("referenciaHistorico", referenciaHistorico);
//			atributos.put("errores", false);
//			atributos.remove("result");
//		}else{
//			atributos.put("errores", true);
//			atributos.put("result", result);
//			return mostrar(session, atributos);
//		}	
//		return imprimirReferenciasPorUsuario(session,atributos,response);
//	}
	
	@SuppressWarnings("unused")
	@RequestMapping(
			value="/imprimirReferenciasPorUsuario.html",
			method = RequestMethod.POST
		)
	public String imprimirReferenciasPorUsuario(
			@ModelAttribute("historicoBusqueda") ReferenciaHistorico referenciaHistorico, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,HttpServletResponse response) {
		try{
			
			User usuario = null;
			if(referenciaHistorico!=null && referenciaHistorico.getCodigoUsuario()!=null)
				usuario = userService.obtenerPorId(referenciaHistorico.getCodigoUsuario());
			
			
				//result.rejectValue("usuario", "required");
			
			if(result.hasErrors()){
				atributos.put("errores", true);
				atributos.put("result", result);
				return mostrar(session, atributos);
			}
			
			int cantCreadas = 0, cantModificadas = 0;
			
			FormLoteReferenciaController forRefController = new FormLoteReferenciaController();

	  		List<Object> sumas = referenciaHistoricoService.traerSumasReferenciasPorSQL(usuario, referenciaHistorico.getFechaHoraDesdeStr(), referenciaHistorico.getFechaHoraHastaStr(),obtenerClienteAspUser().getId());
	  		
	 		List<ReferenciasPorUsuarioReport> listado = new ArrayList<ReferenciasPorUsuarioReport>();
	 		if(sumas!=null && sumas.size()>0){
	 			for(Object ob:sumas){
	 				Object[] lista = (Object[]) ob;
	 				String accion = "";
	 				if      ( ((String) lista[1]).equals("MS004REF") ) accion = "Creadas";
	 				else if ( ((String) lista[1]).equals("MS006REF") ) accion = "Modificadas";
	 				else if ( ((String) lista[1]).equals("MS008REF") ) accion = "Rearchivos";
	 				ReferenciasPorUsuarioReport ref = new ReferenciasPorUsuarioReport();
	 				ref.setCantidad((Integer)lista[0]);
	 				ref.setAccion(accion);
	 				ref.setCodigo((BigDecimal)lista[2]);
	 				ref.setDescripcion((String)lista[3]);	 				
	 				listado.add(ref);
	 			}
	 		}
	 		
	 		if(listado!=null && listado.size()>0){
	 			for(ReferenciasPorUsuarioReport ref : listado){
	 				
	 					if(ref.getAccion().equalsIgnoreCase("Creadas")){
							cantCreadas = cantCreadas + ref.getCantidad();
						}
						else if(ref.getAccion().equalsIgnoreCase("Modificadas")){
							cantModificadas = cantModificadas + ref.getCantidad();
						}
						else if(ref.getAccion().equalsIgnoreCase("Rearchivos")){
							cantCreadas = cantCreadas + ref.getCantidad();
						}
	 				
	 			}
	 		}

			List<ReferenciaReporte> referencias = new ArrayList<FormLoteReferenciaController.ReferenciaReporte>();
			FormLoteReferenciaController.ReferenciaReporte referenciaReporte = forRefController.new ReferenciaReporte();
			referencias.add(referenciaReporte);

	
			JasperReport jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+"/informeReferenciasPorUsuario.jrxml");
			Map<String,Object> parametros=new HashMap<String,Object>();	
			
			String nombres = "";
			
			if(usuario!=null)
				parametros.put("usuario", usuario.getPersona().toString());
			else
			{
				List<Object> listaNombre = referenciaHistoricoService.traerUsuariosCargaReferenciasPorSQL(referenciaHistorico.getFechaHoraDesdeStr(), referenciaHistorico.getFechaHoraHastaStr(),obtenerClienteAspUser().getId());
				if(listaNombre!= null && listaNombre.size()>0){
					for(Object o : listaNombre){
						Object[] nombreApellido = (Object[]) o;
						nombres += nombreApellido[0] + " "+ nombreApellido[1] + "- ";
					}
				}
				parametros.put("usuario", nombres.toLowerCase());
			}
				
			parametros.put("fechaDesde", referenciaHistorico.getFechaDesdeStr());
			parametros.put("fechaHasta", referenciaHistorico.getFechaHastaStr());
			parametros.put("cantidadRefCreadas", cantCreadas);
			parametros.put("cantidadRefModificadas", cantModificadas);
			parametros.put("listado", listado);
			
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=referencias_por_usuario.pdf");
			
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listado);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,dataSource);
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
			response.getOutputStream().close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return mostrar(session, atributos);
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(
			value="/imprimirReferenciasPorUsuarioXls.html",
			method = RequestMethod.POST
		)
	public String imprimirReferenciasPorUsuarioXls(
			@ModelAttribute("historicoBusqueda") ReferenciaHistorico referenciaHistorico, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,HttpServletResponse response) {
		try{
			
			User usuario = null;
			if(referenciaHistorico!=null && referenciaHistorico.getCodigoUsuario()!=null)
				usuario = userService.obtenerPorId(referenciaHistorico.getCodigoUsuario());
			
			
				//result.rejectValue("usuario", "required");
			
			if(result.hasErrors()){
				atributos.put("errores", true);
				atributos.put("result", result);
				return mostrar(session, atributos);
			}
			
			int cantCreadas = 0, cantModificadas = 0;
			
			FormLoteReferenciaController forRefController = new FormLoteReferenciaController();

	  		List<Object> sumas = referenciaHistoricoService.traerSumasReferenciasPorSQL(usuario, referenciaHistorico.getFechaHoraDesdeStr(), referenciaHistorico.getFechaHoraHastaStr(),obtenerClienteAspUser().getId());
	  		
	 		List<ReferenciasPorUsuarioReport> listado = new ArrayList<ReferenciasPorUsuarioReport>();
	 		if(sumas!=null && sumas.size()>0){
	 			for(Object ob:sumas){
	 				Object[] lista = (Object[]) ob;
	 				String accion = "";
	 				if      ( ((String) lista[1]).equals("MS004REF") ) accion = "Creadas";
	 				else if ( ((String) lista[1]).equals("MS006REF") ) accion = "Modificadas";
	 				else if ( ((String) lista[1]).equals("MS008REF") ) accion = "Creadas Rearchivo";
	 				ReferenciasPorUsuarioReport ref = new ReferenciasPorUsuarioReport();
	 				ref.setCantidad((Integer)lista[0]);
	 				ref.setAccion(accion);
	 				ref.setCodigo((BigDecimal)lista[2]);
	 				ref.setDescripcion((String)lista[3]);	 				
	 				listado.add(ref);
	 			}
	 		}
	 		
	 		if(listado!=null && listado.size()>0){
	 			for(ReferenciasPorUsuarioReport ref : listado){
	 				
	 					if(ref.getAccion().equalsIgnoreCase("Creadas")){
							cantCreadas = cantCreadas + ref.getCantidad();
						}
						else if(ref.getAccion().equalsIgnoreCase("Modificadas")){
							cantModificadas = cantModificadas + ref.getCantidad();
						}
						else if(ref.getAccion().equalsIgnoreCase("Creadas Rearchivo")){
							cantCreadas = cantCreadas + ref.getCantidad();
						}
	 				
	 			}
	 		}

			List<ReferenciaReporte> referencias = new ArrayList<FormLoteReferenciaController.ReferenciaReporte>();
			FormLoteReferenciaController.ReferenciaReporte referenciaReporte = forRefController.new ReferenciaReporte();
			referencias.add(referenciaReporte);

		
			String nombres = "";
			List<Object> listaNombre = null;
			
			if(usuario!=null)
				atributos.put("usuario", usuario.getPersona().toString());
			else
			{
				listaNombre = referenciaHistoricoService.traerUsuariosCargaReferenciasPorSQL(referenciaHistorico.getFechaHoraDesdeStr(), referenciaHistorico.getFechaHoraHastaStr(),obtenerClienteAspUser().getId());
				if(listaNombre!= null && listaNombre.size()>0){
					for(Object o : listaNombre){
						Object[] nombreApellido = (Object[]) o;
						nombres += nombreApellido[0] + " "+ nombreApellido[1] + "- ";
					}
				}
				atributos.put("usuario", nombres.toLowerCase());
			}
				
			atributos.put("fechaDesde", referenciaHistorico.getFechaDesdeStr());
			atributos.put("fechaHasta", referenciaHistorico.getFechaHastaStr());
			atributos.put("cantidadRefCreadas", cantCreadas);
			atributos.put("cantidadRefModificadas", cantModificadas);
			atributos.put("listado", listado);
			atributos.put("listaNombre", listaNombre);
			
			
	
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return 	"imprimirReferenciasPorUsuarioXls";
	}
	
	
	@RequestMapping(
			value="/imprimirReferenciasPorUsuarioDetalle.html",
			method = RequestMethod.POST
		)
	public String imprimirReferenciasPorUsuarioDetalle(
			@ModelAttribute("historicoBusqueda") ReferenciaHistorico referenciaHistorico, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,HttpServletResponse response) {
		try{
			
			User usuario = null;
			if(referenciaHistorico!=null && referenciaHistorico.getCodigoUsuarioDia()!=null)
				usuario = userService.obtenerPorId(referenciaHistorico.getCodigoUsuarioDia());
			if(usuario==null)
				result.rejectValue("usuario", "required");
			
			if(result.hasErrors()){
				atributos.put("errores", true);
				atributos.put("result", result);
				return mostrar(session, atributos);
			}
			
			int cantCreadas = 0, cantModificadas = 0;
			
			FormLoteReferenciaController forRefController = new FormLoteReferenciaController();
			referenciaHistorico.setFechaDesdeStr(referenciaHistorico.getFechaHoraStrCorta());
			referenciaHistorico.setFechaHastaStr(referenciaHistorico.getFechaHoraStrCorta());
			List<Long> idReferencias = referenciaHistoricoService.obtenerIdsReferenciasPorUsuario(usuario, referenciaHistorico.getFechaDesde(),referenciaHistorico.getFechaHasta());
	 		String cadenaIds = "";
	 		if(idReferencias!=null && idReferencias.size()>0){
	 			for(int i =0;i<idReferencias.size();i++){
	 				if(i==0)
	 					cadenaIds = idReferencias.get(0).toString();
	 				else{
	 					if(idReferencias.get(i)!=null)
	 						cadenaIds = cadenaIds+","+idReferencias.get(i).toString();
	 				}
	 			}
	 		}

	 		List<ReferenciaHistorico> referenciasHistoricas = referenciaHistoricoService.traerReferenciasHistoricasPorSQL(usuario, referenciaHistorico.getFechaHoraDesdeStr(), referenciaHistorico.getFechaHoraHastaStr());
	 		if(referenciasHistoricas!=null && referenciasHistoricas.size()>0){
	 			for(ReferenciaHistorico refHisto : referenciasHistoricas){
	 				if(refHisto.getAccion().equalsIgnoreCase("MS004REF")){
						cantCreadas++;
					}
					else if(refHisto.getAccion().equalsIgnoreCase("MS006REF")){
						cantModificadas++;
					}
					else if(refHisto.getAccion().equalsIgnoreCase("MS008REF")){
						cantCreadas++;
					}
	 			}
	 		}
			List<ReferenciaReporte> referencias = new ArrayList<FormLoteReferenciaController.ReferenciaReporte>();
			List<Object> listaReferencias = referenciaService.traerObjectsPorIdsPorSQL(cadenaIds);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			if(listaReferencias!=null && listaReferencias.size()>0){
				
				for(Object ob:listaReferencias){
					Object[] lista = (Object[]) ob;				
					FormLoteReferenciaController.ReferenciaReporte referenciaReporte = forRefController.new ReferenciaReporte();
					if(lista!=null){
					
						if(lista[17]!=null)
							referenciaReporte.setClasificacionDocumental((String)lista[17]);
						if(lista[18]!=null)
							referenciaReporte.setElemento((String)lista[18]);
						if(lista[2]!=null){
							Date fecha1 = (Date) lista[2]; 
							referenciaReporte.setFecha1(sdf.format(fecha1));
						}
						if(lista[3]!=null){
							Date fecha2 = (Date) lista[3];
							referenciaReporte.setFecha2(sdf.format(fecha2));
						}
						if(lista[4]!=null)
							referenciaReporte.setIndiceIndividual((String)lista[4].toString());
						if(lista[5]!=null)
							referenciaReporte.setNumero1((String)lista[5].toString());
						if(lista[6]!=null)
							referenciaReporte.setNumero2((String)lista[6].toString());
						if(lista[7]!=null)
							referenciaReporte.setTexto1((String)lista[7]);
						if(lista[8]!=null)
							referenciaReporte.setTexto2((String)lista[8]);
						if(lista[19]!=null)
							referenciaReporte.setContenedor((String)lista[19]);
						if(lista[20]!=null){
							BigDecimal codigoLote = new BigDecimal(lista[20].toString());
							referenciaReporte.setCodigoLote(codigoLote.longValue());
						}		
						referencias.add(referenciaReporte);
					}
				}
	
				JasperReport jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+"/informeReferenciasPorUsuarioDetalle.jrxml");
				Map<String,Object> parametros=new HashMap<String,Object>();	
				
				parametros.put("usuario", usuario.getPersona().toString());
				parametros.put("fechaDesde", referenciaHistorico.getFechaDesdeStr());
				parametros.put("fechaHasta", referenciaHistorico.getFechaHastaStr());
				parametros.put("cantidadRefCreadas", cantCreadas);
				parametros.put("cantidadRefModificadas", cantModificadas);
				
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=referencias_por_usuario.pdf");
				
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(referencias);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,dataSource);
				JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
				response.getOutputStream().close();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return mostrar(session, atributos);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
