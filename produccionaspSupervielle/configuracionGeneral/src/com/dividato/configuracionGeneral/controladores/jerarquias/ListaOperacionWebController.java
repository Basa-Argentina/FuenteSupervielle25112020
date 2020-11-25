/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores.jerarquias;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
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

import com.dividato.configuracionGeneral.validadores.jerarquias.OperacionBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.jerarquias.interfaz.OperacionService;
import com.security.accesoDatos.jerarquias.interfaz.TipoOperacionService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;
import com.security.modelo.jerarquias.TipoOperacion;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de operacions.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarOperacionWeb.html",
				"/mostrarOperacionWeb.html",
				"/filtrarOperacionWeb.html",
				"/imprimirOperacionWeb.html",
				"/cambiarTipoOperacionWeb.html"
			}
		)
public class ListaOperacionWebController {
	private OperacionService operacionService;
	private TipoOperacionService tipoOperacionService;
	private EmpleadoService empleadoService;
	private OperacionBusquedaValidator validator;
	
	
	@Autowired
	public void setOperacionService(OperacionService operacionService) {
		this.operacionService = operacionService;
	}
	
	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	
	@Autowired
	public void setTipoOperacionService(TipoOperacionService tipoOperacionService) {
		this.tipoOperacionService = tipoOperacionService;
	}
	
	@Autowired
	public void setValidator(OperacionBusquedaValidator validator) {
		this.validator = validator;
	}

	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(value="/iniciarOperacionWeb.html", method = RequestMethod.GET)
	public String iniciarOperacion(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("operacionBusqueda");
		return "redirect:mostrarOperacionWeb.html";
	}
	
	@RequestMapping(value="/mostrarOperacionWeb.html", method = RequestMethod.GET)
	public String mostrarOperacion(HttpSession session, 
			Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String valClienteDireccion,
			@RequestParam(value="val", required=false) String valClienteEmp,
			@RequestParam(value="val", required=false) String valSerie,
			@RequestParam(value="val",required=false) String valTipoOperacion,
			@RequestParam(value="val",required=false) String valEmpleadoSolicitante,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo,
			HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		List<Operacion> operacions = null;	
		Operacion operacion = (Operacion) session.getAttribute("operacionBusqueda");
		User usuario = obtenerUser();
		Empleado empleado = empleadoService.obtenerPorId(usuario.getId());
		ClienteEmp cliente = empleado!=null?empleado.getClienteEmp():null;
		if(operacion != null){
			if(empleado!=null){
				operacion.setCodigoPersonal(empleado.getCodigo());
			}
			if(cliente!=null){
				operacion.setClienteCodigo(cliente.getCodigo());
				operacion.setClienteEmp(cliente);
			}
				
			//cuenta la cantidad de resultados
			Integer size = operacionService.contarOperacionFiltradas(operacion, obtenerClienteAsp());
			atributos.put("size", size);
			
			//paginacion y orden de resultados de displayTag
			operacion.setTamañoPagina(7);

			String nPaginaStr= request.getParameter((new ParamEncoder("operacion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("operacion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			String fieldOrder = request.getParameter( new ParamEncoder("operacion").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			operacion.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("operacion").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			operacion.setSortOrder(sortOrder);
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			operacion.setNumeroPagina(nPagina);
			operacions = operacionService.listarOperacionFiltradas(operacion, obtenerClienteAsp());
		}
		else{
			operacion = new Operacion();
			if(empleado!=null){
				operacion.setCodigoPersonal(empleado.getCodigo());
			}
			if(cliente!=null){
				operacion.setClienteCodigo(cliente.getCodigo());
				operacion.setClienteEmp(cliente);
			}
			Date fechaDesde= new Date();
			long diasRestar = 5 * (24 * 60 * 60 * 1000L);
			long dias = fechaDesde.getTime() - diasRestar;
			fechaDesde= new Date(dias);
			operacion.setFechaDesde(fechaDesde);
			operacion.setFechaHasta(new Date());
			operacion.setEstado("Pendiente");
			operacion.setCodigoEmpresa(obtenerEmpresaUser().getCodigo());
			operacion.setCodigoSucursal(obtenerSucursalUser().getCodigo());
			
			//cuenta la cantidad de resultados
			Integer size = operacionService.contarOperacionFiltradas(operacion, obtenerClienteAsp());
			atributos.put("size", size);
			//paginacion y orden de resultados de displayTag
			operacion.setTamañoPagina(7);

			String nPaginaStr= request.getParameter((new ParamEncoder("operacion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("operacion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			String fieldOrder = request.getParameter( new ParamEncoder("operacion").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			operacion.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("operacion").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			operacion.setSortOrder(sortOrder);
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			operacion.setNumeroPagina(nPagina);
			operacions = operacionService.listarOperacionFiltradas(operacion, obtenerClienteAsp());
		}
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAsp().getId());
		atributos.put("operacions", operacions);
		session.setAttribute("operacionBusqueda", operacion);
		return "consultaOperacionWeb";
	}
	
	@RequestMapping(value="/filtrarOperacionWeb.html", method = RequestMethod.POST)
	public String filtrarOperacion(
			@ModelAttribute("operacionBusqueda") Operacion operacion, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos, HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(operacion, result);
		if(!result.hasErrors()){
			session.setAttribute("operacionBusqueda", operacion);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarOperacion(session, atributos, null, null, null, null, null, null,request);
	}
	
	@RequestMapping(
			value="/imprimirOperacionWeb.html",
			method = RequestMethod.GET
		)

	public void imprimirOperacion(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="id",required=true) Long id,
			HttpServletResponse response) {
		try{
			Operacion operacion = operacionService.obtenerPorId(id);

			String requerimiento = operacion.getRequerimiento().getSerie().getCodigo()+": "+operacion.getRequerimiento().getSerie().getPrefijo()+"-"+operacion.getRequerimiento().getNumeroStr();
			String tipoOperacion = operacion.getTipoOperacion().getDescripcion();
			String numero =operacion.getId().toString();
			String deposito = operacion.getDeposito().getDescripcion();
			String fechaAlta = operacion.getFechaAltaStr();
			String fechaEntrega = operacion.getFechaEntregaStr();
			String estado = operacion.getEstado();
			String cliente = operacion.getRequerimiento().getClienteEmp().getRazonSocialONombreYApellido();
			String solicitante = operacion.getRequerimiento().getEmpleadoSolicitante().getNombreYApellido();
			String autorizante = operacion.getRequerimiento().getEmpleadoAutorizante().getNombreYApellido();
			String observaciones = operacion.getRequerimiento().getObservaciones();
			
			ArrayList<OperacionElementoReporte> opElementos=new ArrayList<OperacionElementoReporte>();
			
			for(OperacionElemento opElemento : operacion.getListaElementos()){

				OperacionElementoReporte opElementoReporte=new OperacionElementoReporte();

				if(opElemento.getElemento()!=null){
					opElementoReporte.setTipoElemento(getProperty(opElemento, "elemento.tipoElemento.descripcion"));
					opElementoReporte.setCodigo(getProperty(opElemento, "elemento.codigo"));
					opElementoReporte.setDeposito(getProperty(opElemento, "elemento.contenedor.depositoActual.descripcion"));
					opElementoReporte.setSeccion(getProperty(opElemento, "elemento.contenedor.posicion.estante.grupo.seccion.descripcion"));
					opElementoReporte.setModulo(getProperty(opElemento, "elemento.contenedor.posicion.modulo.moduloPosicionStr"));
					opElementoReporte.setPosicion(getProperty(opElemento, "elemento.contenedor.posicion.posicionStr"));			
					//opElementoReporte.setRearchivoDe(opElemento.getRearchivoDe().getCodigoAlternativo());
					if(opElemento.getElemento()!=null && opElemento.getElemento().getId()!=null && opElemento.getElemento().getId()!=0)
						opElementoReporte.setCodigoBarras(opElemento.getElemento().getId());
				}
				if(opElemento.isLectura()==true)
					opElementoReporte.setOrigen("Lectura");
				else
					opElementoReporte.setOrigen("Requerimiento");
				if(opElemento.getEstado()!=null)
					opElementoReporte.setEstado(opElemento.getEstado().toString());			
				opElementos.add(opElementoReporte);
			}
			JasperReport jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+"/reporteImpresionOperacion.jrxml");
			Map<String,Object> parametros=new HashMap<String,Object>();	
			parametros.put("requerimiento", requerimiento);
			parametros.put("tipoOperacion", tipoOperacion);
			parametros.put("numero", numero);
			parametros.put("deposito", deposito);
			parametros.put("fechaAlta", fechaAlta);
			parametros.put("fechaEntrega", fechaEntrega);
			parametros.put("estado", estado);
			parametros.put("cliente", cliente);
			parametros.put("solicitante", solicitante);
			parametros.put("autorizante", autorizante);
			parametros.put("observaciones", observaciones);			
			
			response.setContentType("application/pdf");
			//response.addHeader("Content-Disposition", "attachment; filename=lista_operaciones.pdf");

			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(opElementos);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, dataSource);
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
	}
	
	private String getProperty(OperacionElemento opElemento, String property) {
		try {
			return BeanUtils.getProperty(opElemento, property);
		} catch (NestedNullException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

	@RequestMapping(
			value="/cambiarTipoOperacionWeb.html",
			method = RequestMethod.GET
		)

	public String cambiarTipoOperacion(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="id",required=true) Long id,
			@RequestParam(value="codigoTipoOperacion",required=false) String codigoTipoOperacion,
			@RequestParam(value="accion",required=false) String accion,
			HttpServletRequest request) {
		
		atributos.put("clienteId", obtenerClienteAsp().getId());
		if(accion.equalsIgnoreCase("MOSTRAR")){
			Operacion operacion = operacionService.obtenerPorId(id);
			if(operacion!=null){
				
				atributos.put("operacion", operacion);
			}
		}
		else if(accion.equalsIgnoreCase("GUARDAR")){
			Operacion operacion = operacionService.obtenerPorId(id);
			if(operacion!=null){
				String codigoActual = operacion.getTipoOperacion().getCodigo();
				if(codigoActual.equals(codigoTipoOperacion)){
					List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
					ScreenMessage tipoOperIguales = new ScreenMessageImp("formularioOperacion.errorTipoOperacionIguales", null);
					avisos.add(tipoOperIguales); //agrego el mensaje a la coleccion
					atributos.put("errores", false);
					atributos.put("hayAvisosNeg", true);
					atributos.put("avisos", avisos);
					atributos.put("operacion", operacion);
				}
				else{
					TipoOperacion tipoOperacion = tipoOperacionService.obtenerTipoOperacionPorCodigo(codigoTipoOperacion, obtenerClienteAsp());
					if(tipoOperacion!=null){
						operacion.setTipoOperacion(tipoOperacion);
						operacionService.actualizar(operacion);
						//Genero las notificaciones 
						List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
						ScreenMessage mensajeTipoOperMod = new ScreenMessageImp("formularioOperacion.notif.tipoOperacionModificado", null);
						avisos.add(mensajeTipoOperMod); //agrego el mensaje a la coleccion
						atributos.put("errores", false);
						atributos.remove("result");
						atributos.put("hayAvisos", true);
						atributos.put("avisos", avisos);
						return mostrarOperacion(session, atributos, null, null, null, null, null, null, request);
					}
					else{
						List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
						ScreenMessage tipoOperIguales = new ScreenMessageImp("formularioOperacion.errorTipoOperacionIncorrecto", null);
						avisos.add(tipoOperIguales); //agrego el mensaje a la coleccion
						atributos.put("errores", false);
						atributos.put("hayAvisosNeg", true);
						atributos.put("avisos", avisos);
						atributos.put("operacion", operacion);
					}
				}
			}
			
		}
			return "cambiarTipoOperacionWeb";
	
	}
	
	public class OperacionElementoReporte{
		private String tipoElemento;
		private String codigo;
		private String deposito;
		private String seccion;
		private String modulo;
		private String posicion;
		private String rearchivoDe;
		private String origen;
		private String estado;
		private Long codigoBarras;
		
		public String getTipoElemento(){
			return tipoElemento;
		}
		public void setTipoElemento(String tipoElemento){
			this.tipoElemento=tipoElemento;
		}
		public String getCodigo() {
			return codigo;
		}
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		public String getDeposito() {
			return deposito;
		}
		public void setDeposito(String deposito) {
			this.deposito = deposito;
		}
		public String getSeccion() {
			return seccion;
		}
		public void setSeccion(String seccion) {
			this.seccion = seccion;
		}
		public String getModulo() {
			return modulo;
		}
		public void setModulo(String modulo) {
			this.modulo = modulo;
		}
		public String getPosicion() {
			return posicion;
		}
		public void setPosicion(String posicion) {
			this.posicion = posicion;
		}
		public String getRearchivoDe() {
			return rearchivoDe;
		}
		public void setRearchivoDe(String rearchivoDe) {
			this.rearchivoDe = rearchivoDe;
		}
		public String getOrigen() {
			return origen;
		}
		public void setOrigen(String origen) {
			this.origen = origen;
		}
		public String getEstado() {
			return estado;
		}
		public void setEstado(String estado) {
			this.estado = estado;
		}
		public Long getCodigoBarras() {
			return codigoBarras;
		}
		public void setCodigoBarras(Long codigoBarras) {
			this.codigoBarras = codigoBarras;
		}	
		
	}
	
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	private Empresa obtenerEmpresaUser(){
		return ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
	}
	private Sucursal obtenerSucursalUser(){
		return ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto();
	}
	
}
