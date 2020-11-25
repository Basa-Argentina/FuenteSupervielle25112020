package com.dividato.security.controladores;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.security.validadores.AppLogBusquedaValidator;
import com.security.accesoDatos.interfaz.AppLogService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.AppLog;
import com.security.modelo.seguridad.User;
/**
 * 
 * @author Gabriel Mainero
 * @modificado Ezequiel Beccaria, agregado el metodo "eliminarInsModAppLogFiltrados()"
 *
 */
@Controller
@RequestMapping (
		value={
				"/iniciarInsModAppLog.html",
				"/mostrarInsModAppLog.html",
				"/filtrarInsModAppLog.html",
				"/consultaInsModAppLogDetalle.html",
				"/eliminarInsModAppLog.html"
		}
	)
public class ListaAppLogController {
	private AppLogService appLogService;
	private AppLogBusquedaValidator validator;
	private List<AppLog> insModAppLogs;
	
	@Autowired
	public void setAppLogService(AppLogService appLogService){
		this.appLogService=appLogService;
	}
	@Autowired
	public void setValidator(AppLogBusquedaValidator validator){
		this.validator=validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder){
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(
			value="/iniciarInsModAppLog.html",
			method = RequestMethod.GET
		)
	public String iniciar(HttpSession session,
			Map<String,Object> atributos){
		session.removeAttribute("insModAppLogBusqueda");
		return "redirect:mostrarInsModAppLog.html";
	}
	
	@RequestMapping (
			value="/mostrarInsModAppLog.html",
			method=RequestMethod.GET
		)
	public String mostrar(Map<String,Object> atributos,HttpSession session){
		AppLog errorFilter=(AppLog) session.getAttribute("insModAppLogBusqueda");
		if(errorFilter==null){
			errorFilter=new AppLog();
			errorFilter.setFechaHora(new Date());
			session.setAttribute("insModAppLogBusqueda",errorFilter);
		}		
		insModAppLogs = appLogService.listarTodosAppLogFiltrados(
				errorFilter.getNivel(), 
				errorFilter.getFechaHora(), 
				errorFilter.getApp(), 
				obtenerClienteAspUser());
		atributos.put("insModAppLogs", insModAppLogs);
		
		String habilitarEliminar = "NO";
		if(insModAppLogs != null && !insModAppLogs.isEmpty())
			habilitarEliminar = "SI";
		atributos.put("habilitarEliminar", habilitarEliminar); // parametro utilizado para habilitar o deshabilitar el boton eliminar
		
		return "consultaInsModAppLog";
	}
	
	@RequestMapping (
			value="/filtrarInsModAppLog.html",
			method=RequestMethod.POST
		)
	public String filtrar(
			@ModelAttribute("insModAppLogBusqueda")AppLog insModAppLogBusqueda,
			BindingResult result,
			HttpSession session
			){
		session.setAttribute("insModAppLogBusqueda", insModAppLogBusqueda);
		return "redirect:mostrarInsModAppLog.html";
	}
	
	@RequestMapping (
			value="/consultaInsModAppLogDetalle.html",
			method=RequestMethod.GET
		)
	public String detalleError(
				@RequestParam("id") Long id,
				Map<String, Object> atributos
			){
		atributos.put("insModLog", appLogService.obtenerPorId(id));
		return "consultaInsModAppLogDetalle";
	}
	@RequestMapping (
			value="/eliminarInsModAppLog.html",
			method=RequestMethod.GET
		)
	public String eliminar(){
		if(insModAppLogs != null && !insModAppLogs.isEmpty())
			appLogService.eliminarInsModAppLog(insModAppLogs);
		return "redirect:mostrarInsModAppLog.html";
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
