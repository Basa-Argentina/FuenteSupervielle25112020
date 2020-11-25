/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores.jerarquias;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.accesoDatos.jerarquias.interfaz.OperacionService;
import com.security.accesoDatos.jerarquias.interfaz.TipoOperacionService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;
import com.security.modelo.jerarquias.TipoOperacion;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
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
				"/iniciarOperacion.html",
				"/mostrarOperacion.html",
				"/filtrarOperacion.html",
				"/imprimirOperacion.html",
				"/cambiarTipoOperacion.html",
				"/cambiarUsuarioAsignado.html"
			}
		)
public class ListaOperacionController {
	private OperacionService operacionService;
	private TipoOperacionService tipoOperacionService;
	private ReferenciaService referenciaService;
	private OperacionBusquedaValidator validator;
	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setOperacionService(OperacionService operacionService) {
		this.operacionService = operacionService;
	}
	
	@Autowired
	public void setTipoOperacionService(TipoOperacionService tipoOperacionService) {
		this.tipoOperacionService = tipoOperacionService;
	}
	
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
	
	@Autowired
	public void setValidator(OperacionBusquedaValidator validator) {
		this.validator = validator;
	}

	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(value="/iniciarOperacion.html", method = RequestMethod.GET)
	public String iniciarOperacion(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("operacionBusqueda");
		return "redirect:mostrarOperacion.html";
	}
	
	@RequestMapping(value="/mostrarOperacion.html", method = RequestMethod.GET)
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
		if(operacion != null){
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
		definirPopupResponsables(atributos, valSerie);

		return "consultaOperacion";
	}
	
	@RequestMapping(value="/filtrarOperacion.html", method = RequestMethod.POST)
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
			value="/imprimirOperacion.html",
			method = RequestMethod.GET
		)

	public void imprimirOperacion(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="id",required=true) Long id,
			HttpServletResponse response) {
		try{
			Operacion operacion = operacionService.obtenerPorId(id);
			
			// Incrementar el contador de veces que ha sido impreso el reporte.
			if( operacion.getCantidadImpresiones() == null) operacion.setCantidadImpresiones(1);
			else operacion.setCantidadImpresiones(operacion.getCantidadImpresiones() + 1);
			operacionService.actualizar(operacion);
			
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
					
					Referencia referencia = referenciaService.obtenerByElemento(opElemento.getElemento());
					if(referencia!=null){
						
						String numeroYTextos = "";
						if(referencia.getTexto1()!= null && !referencia.getTexto1().equals(""))
							numeroYTextos += referencia.getTexto1()+" ";
						if(referencia.getTexto2()!= null && !referencia.getTexto2().equals("")){
							if(referencia.getTexto1()!= null && !referencia.getTexto1().equals("") && !referencia.getTexto1().equalsIgnoreCase(referencia.getTexto2()))
								numeroYTextos += referencia.getTexto2()+" ";
							else if(referencia.getTexto1()== null || referencia.getTexto1().equals(""))
								numeroYTextos += referencia.getTexto2()+" ";
						}
						if(referencia.getNumero1()!=null)
							numeroYTextos += referencia.getNumero1()+" ";
						if(referencia.getNumero2() != null){
							if(referencia.getNumero1()==null || (referencia.getNumero1()!=null && referencia.getNumero1().longValue() != referencia.getNumero2().longValue()))
								numeroYTextos += referencia.getNumero2()+" ";
						}
						
						if(numeroYTextos==null || numeroYTextos.equals("")){
							if(referencia.getFecha1Str()!=null && !referencia.getFecha1Str().equals(""))
								numeroYTextos += referencia.getFecha1Str()+" ";
							if(referencia.getFecha2Str()!=null && !referencia.getFecha2Str().equals(""))
								numeroYTextos += referencia.getFecha2Str();
						}
						
						opElementoReporte.setTextosYNumeros(numeroYTextos);
						opElementoReporte.setCodigoLoteReferencia(referencia.getLoteReferencia().getCodigo());
						opElementoReporte.setClasificacion(referencia.getClasificacionDocumental().getNombre());
					}
					opElementoReporte.setTipoElemento(getProperty(opElemento, "elemento.tipoElemento.descripcion"));
					opElementoReporte.setCodigo(getProperty(opElemento, "elemento.codigo"));
					
					if(opElemento.getElemento().getContenedor()!=null)
					{
						opElementoReporte.setCodigoContenedor(getProperty(opElemento, "elemento.contenedor.codigo"));
						opElementoReporte.setDeposito(getProperty(opElemento, "elemento.contenedor.depositoActual.descripcion"));
						opElementoReporte.setSeccion(getProperty(opElemento, "elemento.contenedor.posicion.estante.grupo.seccion.descripcion"));
						opElementoReporte.setModulo(getProperty(opElemento, "elemento.contenedor.posicion.modulo.moduloPosicionStr"));
						opElementoReporte.setPosicion(getProperty(opElemento, "elemento.contenedor.posicion.estanteYPosicionStr"));
					}
					else
					{
						opElementoReporte.setCodigoContenedor(getProperty(opElemento, "elemento.codigo"));
						opElementoReporte.setDeposito(getProperty(opElemento, "elemento.depositoActual.descripcion"));
						opElementoReporte.setSeccion(getProperty(opElemento, "elemento.posicion.estante.grupo.seccion.descripcion"));
						opElementoReporte.setModulo(getProperty(opElemento, "elemento.posicion.modulo.moduloPosicionStr"));
						opElementoReporte.setPosicion(getProperty(opElemento, "elemento.posicion.estanteYPosicionStr"));
					}
					
					if(opElemento.getElemento().getUbicacionProvisoria()!=null)
						opElementoReporte.setUbicacionProvisoria(getProperty(opElemento, "elemento.ubicacionProvisoria"));
					else if(opElemento.getElemento().getContenedor()!=null && opElemento.getElemento().getContenedor().getUbicacionProvisoria()!=null)
						opElementoReporte.setUbicacionProvisoria(getProperty(opElemento, "elemento.contenedor.ubicacionProvisoria"));
					
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
			if(null != operacion.getUsuarioAsignado()){
				parametros.put("usuarioAsignado", operacion.getUsuarioAsignado().getPersona().toString());
			} else {
				parametros.put("usuarioAsignado", "Sin usuario Asignado");
			}
			
			response.setContentType("application/pdf");
			//response.addHeader("Content-Disposition", "attachment; filename=lista_operaciones.pdf");

			if(opElementos!=null && opElementos.size()>1)
				Collections.sort(opElementos);
			
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
			value="/cambiarTipoOperacion.html",
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
			return "cambiarTipoOperacion";
	
	}
	
	@RequestMapping(
			value="/cambiarUsuarioAsignado.html",
			method = RequestMethod.POST
		)
	public String cambiarUsuarioAsignado(
			HttpSession session,
			@RequestParam(value="selectedSel",required=false) String selectedSel,
			@RequestParam(value="codigoResponsable",required=false) String usuarioAsignadoSel,
			Map<String,Object> atributos,
			HttpServletRequest request){
		/*
		 * 
		 * Los ids recibidos por selectedSel actualizarles el usuario usuarioAsignadoSel.
		 * 
		 */ 
		List<Operacion> operaciones = new ArrayList<Operacion>();
		ScreenMessage mensaje = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Boolean hayAvisos = false;
		Boolean hayAvisosNeg = false;
		Boolean commit = false;
		String selIds[] = null;
		
		if(selectedSel != null && !"".equals(selectedSel)){
			//Obtenemos las posiciones por id's
			selIds = selectedSel.split(",");
			if(selIds != null && selIds.length > 0){
				for(String idOperacion : selIds){
					Operacion operacion = operacionService.obtenerPorId(Long.valueOf(idOperacion));
					if(operacion != null)
						operaciones.add(operacion);
				}
			}
			//recorremos las posiciones seleccionadas y cambiamos el usuario asignado
			for(Operacion update: operaciones){
				User usuarioAsignado = userService.obtenerPorIdNoPersonal(Long.valueOf(usuarioAsignadoSel));
				update.setUsuarioAsignado(usuarioAsignado); 		
			}
			commit = operacionService.actualizarOperacionList(operaciones);
					
			if(commit){
				mensaje = new ScreenMessageImp("formularioOperacion.notificacion.usuarioAsignadoActualizado", null);
				hayAvisos = true;
			}else{
				mensaje = new ScreenMessageImp("formularioOperacion.errorCommit", null);
				hayAvisosNeg = true;
			}
		}else{
			mensaje = new ScreenMessageImp("formularioOperacion.errorDebeSeleccionarAlMenosUno", null);
			hayAvisosNeg = true;
		}
		
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		//return mostrarRemito(session, atributos, null, null, null, null, null, null, null,null,null,request);
		/* */
		return mostrarOperacion(session, atributos, null, null, null, null, null, null,request);

	}
	
	
	public class OperacionElementoReporte implements Comparable<OperacionElementoReporte>{
		private String tipoElemento;
		private String codigo;
		private String codigoContenedor;
		private String textosYNumeros;
		private String deposito;
		private String seccion;
		private String modulo;
		private String posicion;
		private String rearchivoDe;
		private String origen;
		private String estado;
		private Long codigoBarras;
		private Long codigoLoteReferencia;
		private String clasificacion;
		private String ubicacionProvisoria;
		
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
		public String getCodigoContenedor() {
			return codigoContenedor;
		}
		public void setCodigoContenedor(String codigoContenedor) {
			this.codigoContenedor = codigoContenedor;
		}			
		public String getTextosYNumeros() {
			return textosYNumeros;
		}
		public void setTextosYNumeros(String textosYNumeros) {
			this.textosYNumeros = textosYNumeros;
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
		public Long getCodigoLoteReferencia() {
			return codigoLoteReferencia;
		}
		public void setCodigoLoteReferencia(Long codigoLoteReferencia) {
			this.codigoLoteReferencia = codigoLoteReferencia;
		}
		public String getClasificacion() {
			return clasificacion;
		}
		public void setClasificacion(String clasificacion) {
			this.clasificacion = clasificacion;
		}
		public String getUbicacionProvisoria() {
			return ubicacionProvisoria;
		}
		public void setUbicacionProvisoria(String ubicacionProvisoria) {
			this.ubicacionProvisoria = ubicacionProvisoria;
		}
		@Override
		public int compareTo(OperacionElementoReporte o) {
			
			if(this.codigoContenedor!=null && o.getCodigoContenedor()!=null)
				return this.codigoContenedor.compareTo(o.getCodigoContenedor());
			else
				return 0;
			
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
	
	private void definirPopupResponsables(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> responsablesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioPosicionLibre.posicionLibreDetalle.lectura.codigo",false));
		campos.add(new CampoDisplayTag("persona","formularioMovimiento.responsable",false));		
		responsablesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		ClienteAsp casp= obtenerClienteAspUser();
		responsablesPopupMap.put("coleccionPopup", userService.listarPopupNoPersonal(val, casp));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		responsablesPopupMap.put("referenciaPopup", "id");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		responsablesPopupMap.put("referenciaPopup2", "persona");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		responsablesPopupMap.put("referenciaHtml", "codigoResponsable"); 		
		//url que se debe consumir con ajax
		responsablesPopupMap.put("urlRequest", "precargaFormularioMovimiento.html");
		//se vuelve a setear el texto utilizado para el filtrado
		responsablesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		responsablesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("responsablesPopupMap", responsablesPopupMap);
	}
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
