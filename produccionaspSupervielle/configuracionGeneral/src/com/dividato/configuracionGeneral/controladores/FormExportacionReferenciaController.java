/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dividato.configuracionGeneral.objectForms.DatosParaExportacionForm;
import com.dividato.configuracionGeneral.utils.ExportadorExcelReferencias;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.servicios.MailManager;

/**
 * Controlador que permite la exportación de referencias a excel
 *
 * @author Emiliano
 */

@Controller
@RequestMapping(
		value={
				"/exportarReferencia.html",
				"/generarExcelReferencias.html"
		}
)
public class FormExportacionReferenciaController {
	private static Logger logger=Logger.getLogger(FormExportacionReferenciaController.class);
	
	private ClasificacionDocumentalService clasificacionDocumentalService;
	private TipoElementoService tipoElementoService;
	private EmpleadoService empleadoService;
	private ReferenciaService referenciaService;
	private MailManager mailManager;
	
	
	@Autowired
	public void setServices(ClasificacionDocumentalService clasificacionDocumentalService,
			TipoElementoService tipoElementoService,
			EmpleadoService empleadoService,
			ReferenciaService referenciaService){
		this.clasificacionDocumentalService=clasificacionDocumentalService;
		this.tipoElementoService = tipoElementoService;
		this.empleadoService = empleadoService;
		this.referenciaService = referenciaService;
	}
	@Autowired
	public void setMailManager(MailManager mailManager){
		this.mailManager=mailManager;
	}
	
	@RequestMapping(value="/exportarReferencia.html")
	public String exportarReferencia(HttpSession session, Map<String,Object> atributos,
			@ModelAttribute("datosParaExportacion") DatosParaExportacionForm datosParaExportacion,
			HttpServletRequest request) {
		List<TipoElemento> tipoElementos = null;		
		TipoElemento tipoElemento = (TipoElemento) session.getAttribute("tipoElementoBusqueda");
		tipoElementos =(List<TipoElemento>) tipoElementoService.listarTipoElementoFiltrados(tipoElemento, obtenerClienteAspUser());		
		atributos.put("elementosDisponibles", tipoElementos);
	
		if(datosParaExportacion==null){
			datosParaExportacion=new DatosParaExportacionForm();
			atributos.put("datosParaExportacion", datosParaExportacion);
		}
		if(datosParaExportacion.getIdClienteAsp()==null){
			datosParaExportacion.setIdClienteAsp(obtenerClienteAspUser().getId());
			datosParaExportacion.setCodigoEmpresa(obtenerCodigoEmpresaUser());
			datosParaExportacion.setCodigoSucursal(obtenerCodigoSucursalUser());
		}
		return "formularioExportacionReferencia";
	}
	
	@RequestMapping(
			value="/generarExcelReferencias.html",
			method = RequestMethod.POST
		)
	public String exportarExcel(Map<String,Object> atributos,
			HttpServletRequest request,
			HttpSession session,
			@ModelAttribute("datosParaExportacion") DatosParaExportacionForm datosParaExportacion,
			BindingResult result,
			HttpServletResponse response){
		
		List<ClasificacionDocumental> clasificaciones = new ArrayList<ClasificacionDocumental>();
		String mailEnviarTo = null;
		String mailEnviarCopia = null;
		if(datosParaExportacion.getFiltrarPor().equals("ClasificacionDocumental")){
			if(datosParaExportacion.getCodigoClasificacionDocumental()!=null && !datosParaExportacion.getCodigoClasificacionDocumental().equals("")){
				ClasificacionDocumental clasificacion = clasificacionDocumentalService.getClasificacionByCodigoCargarHijos(datosParaExportacion.getCodigoClasificacionDocumental(), datosParaExportacion.getCodigoCliente(), obtenerClienteAspUser(), null);
				if(clasificacion!=null)
					clasificaciones.add(clasificacion);
			}else{
				result.rejectValue("codigoClasificacionDocumental", "required");
				atributos.put("errores", true);
				atributos.put("result", result);
				return exportarReferencia(session, atributos, datosParaExportacion, request);
			}
		}else{
			if(datosParaExportacion.getCodigoPersonal()!=null && !datosParaExportacion.getCodigoPersonal().equals("")){
				Empleado empleado = empleadoService.getByCodigo(datosParaExportacion.getCodigoPersonal(), datosParaExportacion.getCodigoCliente(), obtenerClienteAspUser());
				if(empleado!=null){
					clasificaciones.addAll(clasificacionDocumentalService.getByPersonalAsignado(empleado));
					if(datosParaExportacion.getEnviarMail()!=null){
						mailEnviarTo = empleado.getPersona().getMail();
	
						if(datosParaExportacion.getEnviarConCopia()!=null){
							mailEnviarCopia = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPersona().getMail();
						}
					}
				}
			}else{
				result.rejectValue("codigoPersonal", "required");
				atributos.put("errores", true);
				atributos.put("result", result);
				return exportarReferencia(session, atributos, datosParaExportacion, request);
			}
		}
		if(clasificaciones.size()==0){
			result.reject("formularioExportacionReferencia.noClasificacionesEncontradas");
			atributos.put("errores", true);
			atributos.put("result", result);
		}
		if(datosParaExportacion.getElementosSeleccionadosDer()==null){
			result.rejectValue("elementosSeleccionadosDer", "required");
			atributos.put("errores", true);
			atributos.put("result", result);
		}
		if(result.hasErrors()){
			return exportarReferencia(session, atributos, datosParaExportacion, request);
		}
		List<Long> tiposElemento = new ArrayList<Long>();
		for(String idTipoElementoStr:datosParaExportacion.getElementosSeleccionadosDer())
			tiposElemento.add(new Long(idTipoElementoStr));
		
		try {
			
			InputStream inp = request.getClass().getClassLoader().getResourceAsStream("exportacion_referencias_base.xls");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(baos)); 
			ZipEntry entry = new ZipEntry("exportacion_referencias.xls");
		    zipOut.putNextEntry(entry);
		    //zipOut.setMethod(ZipOutputStream.DEFLATED);
		    ExportadorExcelReferencias.getNewInstance(referenciaService).createWorkbook(zipOut, inp,clasificaciones,tiposElemento);
		    zipOut.closeEntry();
		    zipOut.close();
		    baos.close();
		    
		    byte zip[]=baos.toByteArray();
			
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment; filename=referencias.zip");
			OutputStream out = response.getOutputStream();
			out.write(zip);
			
			if(mailEnviarTo!=null){ 
				enviarMail(mailEnviarTo,mailEnviarCopia,"referencias.zip", new ByteArrayInputStream(zip));
			}
			//pruebas eliminar
			//enviarMail("emimaldo@gmail.com","referencias.zip", new ByteArrayInputStream(zip));
			
			
		} catch (Exception e) {
			logger.fatal(e,e);
		}
		return null;
	}

	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private String obtenerCodigoEmpresaUser(){
		User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if(user.getPersona()!=null && user.getPersona() instanceof PersonaFisica){
			return ((PersonaFisica)user.getPersona()).getEmpresaDefecto().getCodigo();
		}
		return null;
	}
	private String obtenerCodigoSucursalUser(){
		User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if(user.getPersona()!=null && user.getPersona() instanceof PersonaFisica){
			return ((PersonaFisica)user.getPersona()).getSucursalDefecto().getCodigo();
		}
		return null;
	}
	//TODO: Informar cuando se excede el tamaño maximo de filas en el excel
	private void enviarMail(String mailTo,String mailCopia, String nombreArchivo,ByteArrayInputStream inputStream){
		try {
			mailManager.enviarConAdjunto(mailTo, mailCopia, "Referencia exportada:", "Archivo de referencia exportada",nombreArchivo,inputStream,((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPersona().getApellido());
		} catch (MessagingException e) {
			logger.error("error al enviar mail",e);
		} catch (IllegalStateException e){
			logger.error("error al enviar mail",e);
		}
	}
}
