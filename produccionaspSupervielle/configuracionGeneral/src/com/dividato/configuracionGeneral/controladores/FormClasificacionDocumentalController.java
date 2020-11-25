/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.ClasificacionDocumentalValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LeeCodigoBarraService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.LeeCodigoBarra;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la administración
 * de la ClasificacionDocumental.
 *
 * @author Federico Muñoz
 */

@Controller
@RequestMapping(
		value=
			{	
				"/mostrarClasificacionDocumental.html",
				"/guardarClasificacionDocumental.html",
				"/exportarClasificacionDocPdf.html"
			}
		)
public class FormClasificacionDocumentalController {
	private static Logger logger=Logger.getLogger(FormClasificacionDocumentalController.class);
	
	private ClasificacionDocumentalService clasificacionDocumentalService;
	private ClienteEmpService clienteEmpService;
	private EmpleadoService empleadoService;
	private ReferenciaService referenciaService;
	private ClasificacionDocumentalValidator validator;
	private LeeCodigoBarraService leeCodigoBarraService;
	
	@Autowired
	public void setClasificacionDocumentalService(
			ClasificacionDocumentalService clasificacionDocumentalService) {
		this.clasificacionDocumentalService = clasificacionDocumentalService;
	}
	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
	@Autowired
	public void setServices(ClasificacionDocumentalService clasificacionDocumentalService,ClienteEmpService clienteEmpService,
			LeeCodigoBarraService leeCodigoBarraService) {
		this.clasificacionDocumentalService = clasificacionDocumentalService;
		this.clienteEmpService = clienteEmpService;
		this.leeCodigoBarraService = leeCodigoBarraService; 
	}
	
	@Autowired
	public void setValidator(ClasificacionDocumentalValidator validator) {
		this.validator = validator;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(value="/mostrarClasificacionDocumental.html")
	public String mostrarClasificacionDocumental(HttpSession session,
			Map<String,Object> atributos, 
			@RequestParam(value="cArbolScrollX", required=false) String arbolScrollX,
			@RequestParam(value="cArbolScrollY", required=false) String arbolScrollY,
			@RequestParam(value="id_cliente_emp") Long idClienteEmp,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id_seleccionado",required=false) Long idSeleccionado){
		ClienteEmp clienteEmp = clienteEmpService.obtenerPorId(idClienteEmp);
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		
		if(arbolScrollX == null || arbolScrollX.length() == 0){
			arbolScrollX = "0";
		}
		if(arbolScrollY == null || arbolScrollY.length() == 0){
			arbolScrollY = "0";
		}
		atributos.put("arbolScrollX", arbolScrollX);
		atributos.put("arbolScrollY", arbolScrollY);
		
		//buscamos en la base de datos y lo subimos a request.
		List<ClasificacionDocumental> clasificacionesDocumentales = 
			clasificacionDocumentalService.getNodosRaizPorCliente(clienteEmp);
		List<Empleado> empleadosDisponibles = null;
		List<Empleado> empleadosSeleccionados = null;
		//(+) evaluamos si hubo accion sobre el arbol
		ClasificacionDocumental clasificacionSeleccionada = null;
		if(accion!=null && accion.equals("crearCarpeta")){
			clasificacionSeleccionada = new ClasificacionDocumental();
			clasificacionSeleccionada.setNodo(Constantes.CLASIFICACION_DOCUMENTAL_NODO_CARPETA);
			clasificacionSeleccionada.setNombre("nuevo nodo raiz");
			clasificacionSeleccionada.setCodigo(obtenerUltimoCodigo(clienteEmp));
			clasificacionesDocumentales.add(clasificacionSeleccionada);
		}else{
			if(idSeleccionado!=null && idSeleccionado.longValue()!=0){
				ClasificacionDocumental encontrado = null;
				for(ClasificacionDocumental nodo : clasificacionesDocumentales){
					encontrado = buscarClasificacion(nodo, idSeleccionado);
					if(encontrado !=null)
						break;
				}
				if(encontrado!=null){
					if (accion.equals("consultar") || accion.equals("modificar") ){
						clasificacionSeleccionada = encontrado;
					}else if(accion.equals("personal") && Constantes.CLASIFICACION_DOCUMENTAL_NODO_CARPETA.equals(encontrado.getNodo())){
						clasificacionSeleccionada = encontrado;
						//se obtienen los empleados disponibles
						List<ClasificacionDocumental> carpetasPadre = clasificacionSeleccionada.getListaCarpetasPadre();
						HashSet<ClasificacionDocumental> aux = new HashSet<ClasificacionDocumental>(carpetasPadre);
						aux.add(clasificacionSeleccionada);
						Set<Empleado> empleadosNoDisponibles = obtenerEmpleadosNoDisponibles(clasificacionSeleccionada, carpetasPadre, clienteEmp, clienteAsp);
						empleadosDisponibles = empleadoService.listarEmpleadosDisponiblesParaCarpeta(empleadosNoDisponibles, clienteEmp, clienteAsp);
						empleadosSeleccionados = empleadoService.listarEmpleadosAsignadosCarpeta(clasificacionSeleccionada, clienteEmp, clienteAsp);
					}else if(accion.equals("eliminar")){
					
						try{
							//TODO: validar que no existan documentos relacionados
							if(referenciaService.verificarNodoYNodosHijosSinReferencias(encontrado, clienteAsp)){
								if(	clasificacionDocumentalService.eliminarNodoYNodosHijos(encontrado, clienteAsp)){
									clasificacionesDocumentales = clasificacionDocumentalService.getNodosRaizPorCliente(clienteEmp);
									generateAvisoExito("formularioClasificacionDocumental.notificacion.eliminado", atributos);
								}else{
									List<String> codigoErrores = new ArrayList<String>();
									codigoErrores.add("formularioClasificacionDocumental.notificacion.noeliminado");
									generateErrors(codigoErrores, atributos);
								}
							}else{
								List<String> codigoErrores = new ArrayList<String>();
								codigoErrores.add("formularioClasificacionDocumental.notificacion.noeliminadoReferenciasRelacionadas");
								generateErrors(codigoErrores, atributos);								
							}
						}catch(Exception e){
							logger.error("error al intentar eliminar",e);
							List<String> codigoErrores = new ArrayList<String>();
							codigoErrores.add("formularioClasificacionDocumental.notificacion.noeliminado");
							generateErrors(codigoErrores, atributos);
						}
						
					}else{ //nuevo nodo
						clasificacionSeleccionada = new ClasificacionDocumental();
						clasificacionSeleccionada.setPadre(encontrado);
						clasificacionSeleccionada.setCodigo(obtenerUltimoCodigo(clienteEmp));
						encontrado.getNodosHijos().add(clasificacionSeleccionada);
						if(accion.equals("crearSubCarpeta")){
							clasificacionSeleccionada.setNodo(Constantes.CLASIFICACION_DOCUMENTAL_NODO_CARPETA);
							clasificacionSeleccionada.setNombre("nueva carpeta");
						}else{
							clasificacionSeleccionada.setNodo(Constantes.CLASIFICACION_DOCUMENTAL_NODO_INDICE);
							clasificacionSeleccionada.setNombre("Nuevo índice");
						}
					}
				}
			}
		}
		if(empleadosDisponibles == null){
			empleadosDisponibles = new ArrayList<Empleado>();
		}
		atributos.put("empleadosDisponibles", empleadosDisponibles);
		if(empleadosSeleccionados == null){
			empleadosSeleccionados = new ArrayList<Empleado>();
		}
		atributos.put("empleadosSeleccionados", empleadosSeleccionados);
		if(accion!=null && !accion.equals("consultar") && !accion.equals("personal"))
			accion="modificar";
		atributos.put("clienteAsp", clienteAsp);
		atributos.put("accion", accion);
		atributos.put("clasificacionSeleccionada", clasificacionSeleccionada);
		if(clasificacionesDocumentales !=null && clasificacionesDocumentales.size()>0){
			session.setAttribute("clasificacionSeleccionada", clasificacionesDocumentales.get(0));
		}
		//(-) evaluamos si hubo accion sobre el arbol
		
		atributos.put("clasificacionesDocumentales", clasificacionesDocumentales);
		List<LeeCodigoBarra> leeCodigoBarraList = leeCodigoBarraService.listarTodos();
		atributos.put("leeCodigoBarraList", leeCodigoBarraList);
		atributos.put("cliente", clienteEmp);
		
		return "consultaClasificacionDocumental";
	}
	
	/**
	 * busqueda recursiva de la clasificacion documental que tenga el id recibido por parámetros.
	 * @param clasificacion base
	 * @param idSeleccionado
	 * @return
	 */
	private ClasificacionDocumental buscarClasificacion(ClasificacionDocumental clasificacion,Long idSeleccionado){
		if(clasificacion.getId().equals(idSeleccionado)){
			return clasificacion;
		}
		for(ClasificacionDocumental hijo : clasificacion.getNodosHijos()){
			ClasificacionDocumental encontrado = buscarClasificacion(hijo, idSeleccionado);
			if(encontrado !=null)
				return encontrado;
		}
		return null;
	}
	
	@RequestMapping(value="/guardarClasificacionDocumental.html",method= RequestMethod.POST)
	public String guardarClasificacionDocumental(
			Map<String, Object> atributos,
			@RequestParam(value="gArbolScrollX", required=false) String arbolScrollX,
			@RequestParam(value="gArbolScrollY", required=false) String arbolScrollY,
			@RequestParam("id_cliente_emp") Long idClienteEmp,
			@RequestParam("id_padre") Long idPadre,
			@RequestParam("accion") String accion,
			@RequestParam("empleadosSeleccionados") String empleadosSeleccionadosIdsStr,
			@ModelAttribute("clasificacionDocumentoFormulario") ClasificacionDocumental clasificacionDocumentalRecibida,
			BindingResult result){
		
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		ClienteEmp clienteEmp = clienteEmpService.obtenerPorId(idClienteEmp);
		clasificacionDocumentalRecibida.setClienteAsp(clienteAsp);
		clasificacionDocumentalRecibida.setClienteEmp(clienteEmp);
		
		boolean personalActualizado = false;
		
		if(arbolScrollX == null || arbolScrollX.length() == 0){
			arbolScrollX = "0";
		}
		if(arbolScrollY == null || arbolScrollY.length() == 0){
			arbolScrollY = "0";
		}
		atributos.put("arbolScrollX", arbolScrollX);
		atributos.put("arbolScrollY", arbolScrollY);
		
		
		validator.validate(clasificacionDocumentalRecibida,result);
		if(result.hasErrors()){

			List<ClasificacionDocumental> clasificacionesDocumentales = 
				clasificacionDocumentalService.getNodosRaizPorCliente(clienteEmp);
			
			if(idPadre!=null && idPadre.longValue()!=0){
				ClasificacionDocumental padre = null;
				for(ClasificacionDocumental nodo : clasificacionesDocumentales){
					padre = buscarClasificacion(nodo, idPadre);
					if(padre !=null)
						break;
				}
				if(padre!=null){
					clasificacionDocumentalRecibida.setPadre(padre);
					if(clasificacionDocumentalRecibida.getId()==null )
						padre.getNodosHijos().add(clasificacionDocumentalRecibida);
				}
			}
			
			atributos.put("clasificacionesDocumentales", clasificacionesDocumentales);
			atributos.put("cliente", clienteEmp);
			atributos.put("clienteAsp", obtenerClienteAspUser());
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");	
			
			atributos.put("accion", accion);
			atributos.put("clasificacionSeleccionada", clasificacionDocumentalRecibida);
			
			return "consultaClasificacionDocumental";
		}		
		if(clasificacionDocumentalRecibida.getId()!=null && clasificacionDocumentalRecibida.getId().longValue()!=0){
			ClasificacionDocumental clasificacionDocumental = null;
			clasificacionDocumental = clasificacionDocumentalService.getClasificacionByCodigoCargarHijos(clasificacionDocumentalRecibida.getCodigo(),clienteEmp.getCodigo(),obtenerClienteAspUser(),clasificacionDocumentalRecibida.getNodo());
			if("personal".equals(accion) && Constantes.CLASIFICACION_DOCUMENTAL_NODO_CARPETA.equals(clasificacionDocumental.getNodo())){
				List <Empleado> empleadosAsignados = obtenerEmpleadosPorIdsStr(empleadosSeleccionadosIdsStr);
				if(empleadosAsignados==null){
					empleadosAsignados = new ArrayList<Empleado>();
				}
				personalActualizado = clasificacionDocumentalService.guardarPersonarClasificacionDocumental(empleadosAsignados, clasificacionDocumental, clienteEmp, clienteAsp);
			}else{
				clasificacionDocumentalRecibida.setPadre(clasificacionDocumental.getPadre());
				clasificacionDocumentalRecibida.setListHijos(new HashSet<ClasificacionDocumental>(clasificacionDocumental.getNodosHijos()));
				clasificacionDocumentalService.actualizar(clasificacionDocumentalRecibida);
			}
		}else{
			ClasificacionDocumental padre = null;
			if(idPadre.longValue()!=0){
				padre = clasificacionDocumentalService.obtenerPorId(idPadre);
			}
			clasificacionDocumentalRecibida.setPadre(padre);
			clasificacionDocumentalService.guardar(clasificacionDocumentalRecibida);
		}
		
		List<ClasificacionDocumental> clasificacionesDocumentales = 
			clasificacionDocumentalService.getNodosRaizPorCliente(clienteEmp);
		
		atributos.put("clasificacionesDocumentales", clasificacionesDocumentales);
		atributos.put("cliente", clienteEmp);
		atributos.put("clienteAsp", clienteAsp);
		
		if("personal".equals(accion)){
			if(personalActualizado){
				generateAvisoExito("formularioClasificacionDocumental.notificacion.personalActualizado", atributos);
			}else{
				ArrayList<String> codigoErrores = new ArrayList<String>();
				codigoErrores.add("formularioClasificacionDocumental.notificacion.errorPersonal");
				generateErrors(codigoErrores, atributos);
			}
		}else{
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioClasificacionDocumental.notificacion.registrado", null);
			avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		
			
		}
		return "consultaClasificacionDocumental";
	}
	
	@RequestMapping(value="/exportarClasificacionDocPdf.html",method = RequestMethod.GET)
	public void exportarClasificacionDocPdf(HttpSession session,
			Map<String,Object> atributos,HttpServletResponse response) {
		try{
			Map<String,Object> parametros=new HashMap<String,Object>();
			JasperReport jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+"/reporteImpresionClasificacionDoc.jrxml");
			JasperReport jasperSubReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+"/subReporteImpresionClasificacionDoc.jrxml");
			//List<Requerimiento> requerimientos = (List<Requerimiento>) session.getAttribute("requerimientos");
			response.setContentType("application/pdf");
			ClasificacionDocumental cd = (ClasificacionDocumental) session.getAttribute("clasificacionSeleccionada");
			//cd.setNodosHijos(Collections.sort(cd.getNodosHijos()));
			parametros.put("clasificacionDoc", cd);
			parametros.put("SUB_REPORTE", jasperSubReport);
			parametros.put("fecha", formatoFechaFormularios.format(new Date()));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros);
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
			response.getOutputStream().close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private Integer obtenerUltimoCodigo(ClienteEmp clienteEmp){
		return clasificacionDocumentalService.getProximoCodigoByClienteEmp(clienteEmp, obtenerClienteAspUser());
	}
	
	/**
	 * Busca los empleados por id
	 * @param empleadosSeleccionadosIdsStr
	 * @return
	 */
	private List<Empleado> obtenerEmpleadosPorIdsStr(String empleadosSeleccionadosIdsStr){
		List<Empleado> result = null;
		String[] listaIdsStr = null;
		HashSet<Long> setIds = new HashSet<Long>();
		if(empleadosSeleccionadosIdsStr != null && !"".equals(empleadosSeleccionadosIdsStr)){
			listaIdsStr = empleadosSeleccionadosIdsStr.split(",");
			Long idLong =null;
			for(String id : listaIdsStr){		
				try{
					idLong = Long.valueOf(id);					
				}catch(NumberFormatException e){
					continue;
				}
				//se guardan los ids en un set para prevenir repetidos
				setIds.add(idLong);
				Long[] arrayIds = new Long[setIds.size()];
				result = empleadoService.listarEmpleadosConCarpetasAsignadas(setIds.toArray(arrayIds), obtenerClienteAspUser());
			}
		}
		return result;
	}
	/**
	 * Busca los empleados que no pueden ser asignados a la carpeta seleccionada 
	 * @param carpetaSeleccionada
	 * @param carpetasPadre
	 * @param clienteEmp
	 * @param clienteAsp
	 * @return
	 */
	private Set<Empleado> obtenerEmpleadosNoDisponibles(ClasificacionDocumental carpetaSeleccionada, List<ClasificacionDocumental> carpetasPadre, ClienteEmp clienteEmp, ClienteAsp clienteAsp){
		HashSet<ClasificacionDocumental> aux = new HashSet<ClasificacionDocumental>(carpetasPadre);
		aux.add(carpetaSeleccionada);
		
		return  clasificacionDocumentalService.getPersonalAsignadoPorNodos(aux, clienteEmp, clienteAsp);
	}
	
	/**
	 * genera el objeto BindingResult para mostrar los errores por pantalla en un popup y lo agrega al map atributos
	 * @param codigoErrores
	 * @param atributos
	 */
	private void generateErrors(List<String> codigoErrores,	Map<String, Object> atributos) {
		if (!codigoErrores.isEmpty()) {
			BindingResult result = new BeanPropertyBindingResult(new Object(),"");
			for (String codigo : codigoErrores) {
				result.addError(new FieldError(	"error.formBookingGroup.general", codigo, null, false, new String[] { codigo }, null, "?"));
			}
			atributos.put("result", result);
			atributos.put("errores", true);
		} else if(atributos.get("result") == null){
			atributos.put("errores", false);
		}
	}
	
	private void generateAvisoExito(String avisoExito, Map<String, Object> atributos) {
		//Genero las notificaciones 
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		ScreenMessage mensajeEstanteReg = new ScreenMessageImp(avisoExito, null);
		avisos.add(mensajeEstanteReg); //agrego el mensaje a la coleccion
		atributos.put("errores", false);
		atributos.remove("result");
		atributos.put("hayAvisos", true);
		atributos.put("hayAvisosNeg", false);
		atributos.put("avisos", avisos);
	}
}
