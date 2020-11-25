/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores.jerarquias;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.jerarquias.RequerimientoBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.MovimientoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PosicionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.jerarquias.interfaz.OperacionElementoService;
import com.security.accesoDatos.jerarquias.interfaz.OperacionService;
import com.security.accesoDatos.jerarquias.interfaz.RequerimientoService;
import com.security.accesoDatos.jerarquias.interfaz.TipoRequerimientoService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Movimiento;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de requerimientos.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero *
 */

@Controller
@RequestMapping(
		value=
			{	
				"/iniciarRequerimiento.html",
				"/mostrarRequerimiento.html",
				"/eliminarRequerimiento.html",
				"/cancelarRequerimiento.html",
				"/filtrarRequerimiento.html",
				"/exportarPdf.html"
			}
		)
public class ListaRequerimientoController {
	private RequerimientoService requerimientoService;
	private RequerimientoBusquedaValidator validator;
	private RemitoService remitoService;
	private MovimientoService movimientoService;
	private PosicionService posicionService;
	private ClienteEmpService clienteEmpService;
	private ClienteDireccionService clienteDireccionService;
	private SerieService serieService;
	private TipoRequerimientoService tipoRequerimientoService;
	private EmpleadoService empleadoService;
	private OperacionService operacionService;
	private OperacionElementoService operacionElementoService;
	
	@Autowired
	public void setOperacionElementoService(OperacionElementoService operacionElementoService) {
		this.operacionElementoService = operacionElementoService;
	}
	
	@Autowired
	public void setRemitoService(RemitoService remitoService) {
		this.remitoService = remitoService;
	}
	
	@Autowired
	public void setMovimientoService(MovimientoService movimientoService) {
		this.movimientoService = movimientoService;
	}
	
	@Autowired
	public void setPosicionService(PosicionService posicionService) {
		this.posicionService = posicionService;
	}
	
	@Autowired
	public void setOperacionService(OperacionService operacionService) {
		this.operacionService = operacionService;
	}
	
	@Autowired
	public void setRequerimientoService(RequerimientoService requerimientoService) {
		this.requerimientoService = requerimientoService;
	}
	
	@Autowired
	public void setClienteDireccionService(ClienteDireccionService clienteDireccionService) {
		this.clienteDireccionService = clienteDireccionService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}

	@Autowired
	public void setTipoRequerimientoService(TipoRequerimientoService tipoRequerimientoService) {
		this.tipoRequerimientoService = tipoRequerimientoService;
	}
	
	@Autowired
	public void setValidator(RequerimientoBusquedaValidator validator) {
		this.validator = validator;
	}

	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(value="/iniciarRequerimiento.html", method = RequestMethod.GET)
	public String iniciarRequerimiento(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("requerimientoBusqueda");
		session.removeAttribute("requerimientos");
		session.removeAttribute("bandera");
		return "redirect:mostrarRequerimiento.html";
	}
	
	@RequestMapping(value="/mostrarRequerimiento.html", method = RequestMethod.GET)
	public String mostrarRequerimiento(HttpSession session, 
			Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String valClienteDireccion,
			@RequestParam(value="val", required=false) String valClienteEmp,
			@RequestParam(value="val", required=false) String valSerie,
			@RequestParam(value="val",required=false) String valTipoRequerimiento,
			@RequestParam(value="val",required=false) String valEmpleadoSolicitante,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo,
			HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		List<Requerimiento> requerimientos = null;	
		Requerimiento requerimiento = (Requerimiento) session.getAttribute("requerimientoBusqueda");
		User usuario = obtenerUser();
		Empleado empleado = empleadoService.obtenerPorId(usuario.getId());
		GrantedAuthority[] autorities = usuario.getAuthorities();
		boolean permite = false;
		for(GrantedAuthority autoridad:autorities)
		{
			if(autoridad.getAuthority().equalsIgnoreCase("ROLE_CARGA_REQ_CUALQUIER_CLIENTEEMP")){
				permite = true;
				break;
			}
		}
	
		session.setAttribute("bandera", permite);
		
		if(requerimiento != null){
			//cuenta la cantidad de resultados
			Integer size = requerimientoService.contarRequerimientoFiltradas(requerimiento, obtenerClienteAsp());
			atributos.put("size", size);
			
			//paginacion y orden de resultados de displayTag
			requerimiento.setTamañoPagina(20);

			String nPaginaStr= request.getParameter((new ParamEncoder("requerimiento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("requerimiento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			String fieldOrder = request.getParameter( new ParamEncoder("requerimiento").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			requerimiento.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("requerimiento").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			requerimiento.setSortOrder(sortOrder);
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			requerimiento.setNumeroPagina(nPagina);
			
			requerimientos = requerimientoService.listarRequerimientoFiltradas(requerimiento, obtenerClienteAsp());
		}
		else{
			requerimiento = new Requerimiento();
			Date fechaDesde= new Date();
			long diasRestar = 30 * (24 * 60 * 60 * 1000L);
			long dias = fechaDesde.getTime() - diasRestar;
			fechaDesde= new Date(dias);
			requerimiento.setFechaDesde(fechaDesde);
			requerimiento.setFechaHasta(new Date());
			requerimiento.setEstado("Pendiente");
			
			//cuenta la cantidad de resultados
			Integer size = requerimientoService.contarRequerimientoFiltradas(requerimiento, obtenerClienteAsp());
			atributos.put("size", size);
			
			//paginacion y orden de resultados de displayTag
			requerimiento.setTamañoPagina(20);

			String nPaginaStr= request.getParameter((new ParamEncoder("requerimiento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("requerimiento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			String fieldOrder = request.getParameter( new ParamEncoder("requerimiento").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			requerimiento.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("requerimiento").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			requerimiento.setSortOrder(sortOrder);
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			requerimiento.setNumeroPagina(nPagina);
			
			requerimientos = requerimientoService.listarRequerimientoFiltradas(requerimiento, obtenerClienteAsp());
		}
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAsp().getId());
		
		//atributos.put("requerimientos", requerimientos);
		session.setAttribute("requerimientos", requerimientos);
		session.setAttribute("requerimientoBusqueda", requerimiento);
		return "consultaRequerimiento";
	}
	
	@RequestMapping(value="/filtrarRequerimiento.html", method = RequestMethod.POST)
	public String filtrarRequerimiento(
			@ModelAttribute("requerimientoBusqueda") Requerimiento requerimiento, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		if(requerimiento.getSerieDesdeStr()!=null && !requerimiento.getSerieDesdeStr().trim().equals("")){
			requerimiento.setSerieDesde(new BigInteger(requerimiento.getSerieDesdeStr()));
		}
		if(requerimiento.getSerieHastaStr()!=null && !requerimiento.getSerieHastaStr().trim().equals("")){
			requerimiento.setSerieHasta(new BigInteger(requerimiento.getSerieHastaStr()));
		}	
		this.validator.validate(requerimiento, result);
		if(!result.hasErrors()){
			session.setAttribute("requerimientoBusqueda", requerimiento);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarRequerimiento(session, atributos, null, null, null, null, null, null, request);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/exportarPdf.html",method = RequestMethod.GET)
	public void exportarPdf(HttpSession session,
			Map<String,Object> atributos,HttpServletResponse response) {
		try{
			Map<String,Object> parametros=new HashMap<String,Object>();
			JasperReport jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+"/reporteImpresionRequerimientos.jrxml");
			List<Requerimiento> requerimientos = (List<Requerimiento>) session.getAttribute("requerimientos");
			response.setContentType("application/pdf");
			List<requerimientoReporte> req = new ArrayList<ListaRequerimientoController.requerimientoReporte>();
			for(Requerimiento r:requerimientos){
				requerimientoReporte reqVo = new requerimientoReporte();
				reqVo.setTipo(r.getTipoRequerimiento().getDescripcion());
				reqVo.setNumero(r.getSerie().getCodigo()+":"+r.getPrefijoStr()+"-"+r.getNumeroStr());
				reqVo.setCliente(r.getClienteEmp().getRazonSocialONombreYApellido());
				reqVo.setSolicitante(r.getEmpleadoSolicitante().getNombreYApellido());
				reqVo.setFechaAlta(r.getFechaHoraAltaStr());
				reqVo.setFechaEntrega(r.getFechaHoraEntregaStr());
				reqVo.setEstado(r.getEstado());
				req.add(reqVo);
			}
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,new JRBeanCollectionDataSource(req));
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
			response.getOutputStream().close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Requerimiento.
	 * 
	 * @param idDireccion el id de Requerimiento a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarRequerimiento.html",
			method = RequestMethod.GET
		)
	public String eliminarRequerimiento(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos,
			HttpServletRequest request) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la requerimiento para eliminar luego
		Requerimiento requerimiento = requerimientoService.obtenerPorId(Long.valueOf(id));
		List<Operacion> operaciones = null;
		int cantidadNoPendientes = 0;
		if(requerimiento!=null){
			operaciones = operacionService.listarOperacionPorRequerimiento(requerimiento, obtenerClienteAsp());
			for(Operacion operacion:operaciones)
				if(!operacion.getEstado().equals("Pendiente"))
					cantidadNoPendientes ++;
			//Eliminamos el requerimiento y sus operaciones
			
		}
		if(cantidadNoPendientes == 0){
			commit = requerimientoService.delete(requerimiento,operaciones);
			ScreenMessage mensaje;
			//Controlamos su eliminacion.
			if(commit){
				mensaje = new ScreenMessageImp("notif.requerimiento.requerimientoEliminado", null);
				hayAvisos = true;
			}else{
				mensaje = new ScreenMessageImp("error.deleteDataBase", null);
				hayAvisosNeg = true;
			}
			avisos.add(mensaje);
		}
		else{
			ScreenMessage mensaje;
			mensaje = new ScreenMessageImp("formularioRequerimiento.errorEliminar", null);
			hayAvisosNeg = true;
			avisos.add(mensaje);
		}
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarRequerimiento(session, atributos, null, null, null, null, null, null, request);
	}
	
	@RequestMapping(
			value="/cancelarRequerimiento.html",
			method = RequestMethod.GET
		)
	public String cancelarRequerimiento(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos,
			HttpServletRequest request) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la requerimiento para eliminar luego
		Requerimiento requerimiento = requerimientoService.obtenerPorId(Long.valueOf(id));
		
		//Actualizamos el requerimiento
		requerimiento.setEstado("Cancelado");
		User user = obtenerUser();
		if(user!=null)
			requerimiento.setUsuario(user);
		
		//Buscamos las operaciones del requerimiento y las cancelamos
		ArrayList<Operacion> operaciones = (ArrayList<Operacion>) operacionService.listarOperacionPorRequerimiento(requerimiento, obtenerClienteAsp());
		if(operaciones!=null && operaciones.size()>0){
			for(Operacion operacion:operaciones){
				operacion.setListaElementos(new HashSet<OperacionElemento>(operacionElementoService.listarOperacionElementoPorOperacion(operacion, obtenerClienteAsp())));
				if(operacion.getListaElementos()==null){
					operacion.setListaElementos(new HashSet<OperacionElemento>());
				}
	
				for(OperacionElemento operacionElemento:operacion.getListaElementos()){
					operacionElemento.setEstado(Constantes.ESTADO_OPERACION_ELEMENTO_CANCELADO);
				}
				operacion.setEstado("Cancelado");
			}
		}

		commit = requerimientoService.update(requerimiento, operaciones);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			
			////////////////Buscamos si el requerimiento genero un remito para anularlo/////////////////////////
			Boolean commit2=null;
			Remito remito = new Remito();
			List<Remito> remitos = new ArrayList<Remito>();
			remito.setNumRequerimiento(requerimiento.getRequerimientoStr());
			remitos = remitoService.listarRemitoFiltradas(remito, requerimiento.getClienteAsp());
			if(remitos.size()>0){
				for(int i =0;i<remitos.size();i++)
				 remitos.get(i).setEstado(Constantes.REMITO_ESTADO_CANCELADO);
			}
			commit2 = remitoService.actualizarRemitoList(remitos);
			
			ScreenMessage mensaje2;
			if(commit2){
				mensaje2 = new ScreenMessageImp("formularioRequerimiento.notificacion.requerimientoRemitosCancelados", null);
			}else{
				mensaje2 = new ScreenMessageImp("formularioRequerimiento.notificacion.requerimientoRemitosNoCancelados", null);
			}
			avisos.add(mensaje2);
			
			/////////////////Buscamos si el requerimiento genero movimientos de elementos///////////////////////
			Boolean commit3 = null;
			Movimiento movimiento = new Movimiento();
			List<Movimiento> movimientos = new ArrayList<Movimiento>();
			List<Posicion> posiciones;
			movimiento.setDescripcionRemito(requerimiento.getRequerimientoStr());
			movimientos = movimientoService.traerMovimientosPorRequerimiento(movimiento, requerimiento.getClienteAsp());
				
			if(movimientos.size()>0){
				posiciones = new ArrayList<Posicion>();
				for(int i = 0;i<movimientos.size();i++){
					movimientos.get(i).setEstado("ANULADO");
					
					if(movimientos.get(i).getElemento().getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE)){
						movimientos.get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_CREADO);
					}else{
						movimientos.get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
						
						if(movimientos.get(i).getPosicionOrigenDestino()!=null && movimientos.get(i).getElemento().getTipoElemento().getContenedor()==true){
							Posicion posicion = posicionService.obtenerPorId(movimientos.get(i).getPosicionOrigenDestino().getId());
							//Si la posicion todavia esta libre
							if(posicion.getEstado().equalsIgnoreCase(Constantes.POSICION_ESTADO_DISPONIBLE)){
								//Se vuelve a poner ocupada
								posicion.setEstado(Constantes.POSICION_ESTADO_OCUPADA);
								//Se setea la posicion que tenia al objeto
								movimientos.get(i).getElemento().setPosicion(posicion);	
								//Se agrega la posicion a la lista para actualizar
								posiciones.add(posicion);
							}
							//Si ya esta ocupada hay que avisar al usuario
							else{
								ScreenMessage mensajePosicion = new ScreenMessageImp("formularioMovimiento.error.posicionAnteriorOcupada", null);
								avisos.add(mensajePosicion); //agrego el mensaje a la coleccion
								atributos.put("hayAvisos", true);
								atributos.put("avisos", avisos);
							}
						}
						
					}	
					
				}		
				commit3 = movimientoService.actualizarMovimientoListYActualizarPosiciones(movimientos, posiciones);
				
				ScreenMessage mensaje3;
				if(commit3){
					mensaje3 = new ScreenMessageImp("formularioRequerimiento.notificacion.requerimientoMovimientosCancelados", null);
				}else{
					mensaje3 = new ScreenMessageImp("formularioRequerimiento.notificacion.requerimientoMovimientosNoCancelados", null);
				}
				avisos.add(mensaje3);
			}
			
			mensaje = new ScreenMessageImp("formularioRequerimiento.notificacion.requerimientoCancelado", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.commitDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarRequerimiento(session, atributos, null, null, null, null, null, null, request);
	}
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	
	public class requerimientoReporte{
		public String tipo;
		public String numero;
		public String cliente;
		public String solicitante;
		public String fechaAlta;
		public String fechaEntrega;
		public String estado;
		
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		public String getNumero() {
			return numero;
		}
		public void setNumero(String numero) {
			this.numero = numero;
		}
		public String getCliente() {
			return cliente;
		}
		public void setCliente(String cliente) {
			this.cliente = cliente;
		}
		public String getSolicitante() {
			return solicitante;
		}
		public void setSolicitante(String solicitante) {
			this.solicitante = solicitante;
		}
		public String getFechaAlta() {
			return fechaAlta;
		}
		public void setFechaAlta(String fechaAlta) {
			this.fechaAlta = fechaAlta;
		}
		public String getFechaEntrega() {
			return fechaEntrega;
		}
		public void setFechaEntrega(String fechaEntrega) {
			this.fechaEntrega = fechaEntrega;
		}
		public String getEstado() {
			return estado;
		}
		public void setEstado(String estado) {
			this.estado = estado;
		}
	}
	
}
