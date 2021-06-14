/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores.jerarquias;

import static com.security.utils.Constantes.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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

import com.dividato.configuracionGeneral.validadores.jerarquias.OperacionReferenciaBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.configuraciongeneral.interfaz.MovimientoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PosicionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RearchivoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.jerarquias.interfaz.OperacionElementoService;
import com.security.accesoDatos.jerarquias.interfaz.OperacionReferenciaService;
import com.security.accesoDatos.jerarquias.interfaz.OperacionService;
import com.security.accesoDatos.jerarquias.interfaz.RequerimientoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Impuesto;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.configuraciongeneral.Movimiento;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Rearchivo;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Stock;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.jerarquias.TipoOperacion;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
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
				"/iniciarOperacionReferencia.html",
				"/mostrarOperacionReferencia.html",
				"/filtrarOperacionReferencia.html",
				"/filtrarElementoOperacionReferencia.html",
				"/prepararActualizarOperacionReferencia.html",
				"/finalizarOperacionReferencia.html",
				"/cancelarOperacionReferencia.html",
				"/eliminarOperacionReferencia.html"
			}
		)
public class ListaOperacionReferenciaController {
	private OperacionReferenciaService operacionReferenciaService;
	private OperacionReferenciaBusquedaValidator validator;
	private OperacionService operacionService;
	private ReferenciaService referenciaService;
	private ListaOperacionController listaOperacionController;
	private LecturaService lecturaService;
	private LecturaDetalleService lecturaDetalleService;
	private RequerimientoService requerimientoService;
	private ListaPreciosService listaPreciosService;
	private OperacionElementoService operacionElementoService;
	private RearchivoService rearchivoService;
	private RemitoService remitoService;
	private MovimientoService movimientoService;
	private PosicionService posicionService;
	
	
	@Autowired
	public void setMovimientoService(MovimientoService movimientoService) {
		this.movimientoService = movimientoService;
	}
	
	@Autowired
	public void setRemitoService(RemitoService remitoService) {
		this.remitoService = remitoService;
	}
	
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
	
	@Autowired
	public void setRearchivoService(RearchivoService rearchivoService) {
		this.rearchivoService = rearchivoService;
	}
	
	@Autowired
	public void setOperacionElementoService(OperacionElementoService operacionElementoService) {
		this.operacionElementoService = operacionElementoService;
	}
	
	@Autowired
	public void setListaPreciosService(ListaPreciosService listaPreciosService) {
		this.listaPreciosService = listaPreciosService;
	}
	
	@Autowired
	public void setLecturaService(LecturaService lecturaService, LecturaDetalleService lecturaDetalleService) {
		this.lecturaService = lecturaService;
		this.lecturaDetalleService = lecturaDetalleService;
	}
	
	@Autowired
	public void setRequerimientoService(RequerimientoService requerimientoService) {
		this.requerimientoService = requerimientoService;
	}
	
	@Autowired
	public void setListaOperacionController(ListaOperacionController listaOperacionController) {
		this.listaOperacionController = listaOperacionController;
	}
	
	@Autowired
	public void setPosicionService(PosicionService posicionService) {
		this.posicionService = posicionService;
	}
	
	@Autowired
	public void setOperacionReferenciaService(OperacionReferenciaService operacionReferenciaService) {
		this.operacionReferenciaService = operacionReferenciaService;
	}
	
	@Autowired
	public void setOperacionService(OperacionService operacionService) {
		this.operacionService = operacionService;
	}
	
	@Autowired
	public void setValidator(OperacionReferenciaBusquedaValidator validator) {
		this.validator = validator;
	}

	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(value="/iniciarOperacionReferencia.html", method = RequestMethod.GET)
	public String iniciarOperacionReferencia(HttpSession session, Map<String,Object> atributos, 
			@RequestParam(value="idOperacion",required=false) Long idOperacion){
		session.removeAttribute("operacionReferenciaBusqueda");
		session.removeAttribute("operacionReferenciasSession");
		session.setAttribute("rearchivosPendientesEnOperacion", false);
		return mostrarOperacionReferencia(session, atributos, idOperacion);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/mostrarOperacionReferencia.html", method = RequestMethod.GET)
	public String mostrarOperacionReferencia(HttpSession session, 
			Map<String,Object> atributos,
			@RequestParam(value="idOperacion",required=false) Long idOperacion){
		//buscamos en la base de datos y lo subimos a request.
		List<OperacionElemento> operacionReferencias = null;
		List<OperacionElemento> lista = (List<OperacionElemento>)session.getAttribute("operacionReferenciasSession");
		OperacionElemento operacionReferencia = (OperacionElemento) session.getAttribute("operacionReferenciaBusqueda");
		Operacion operacion = null;
		if(idOperacion!=null){
			operacion = operacionService.obtenerPorId(idOperacion);
			operacion.setListaElementos(new HashSet<OperacionElemento>(operacionElementoService.listarOperacionElementoPorOperacion(operacion, obtenerClienteAsp())));
		}
		if(operacionReferencia != null)
		{ 
			if(operacionReferencia.getIdOperacion()!=null)
			{
				operacion = operacionService.obtenerPorId(operacionReferencia.getIdOperacion());
				operacion.setListaElementos(new HashSet<OperacionElemento>(operacionElementoService.listarOperacionElementoPorOperacion(operacion, obtenerClienteAsp())));
			}
			operacion.setObservaciones(operacionReferencia.getOperacion().getObservaciones());
		}
		if(lista==null){
			if(operacionReferencia != null){
				operacionReferencias = operacionReferenciaService.listarOperacionReferenciaFiltradas(operacionReferencia, operacion);
			}
			else{
				operacionReferencia = new OperacionElemento();
				//Poner filtros por defecto
				operacionReferencias = operacionReferenciaService.listarOperacionReferenciaFiltradas(operacionReferencia, operacion);
			}
		}
		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAsp().getId());
		atributos.put("idOperacion", obtenerClienteAsp().getId());
		Empresa emp = obtenerEmpresaDefault();
		if(emp!=null)
			atributos.put("codigoEmpresa", emp.getCodigo());
		atributos.put("operacion", operacion);
		
		if(lista==null){
			operacionReferencia.setOperacion(operacion);
			Collections.sort(operacionReferencias);
			atributos.put("operacionReferencias", operacionReferencias);
			session.setAttribute("operacionReferenciasSession", operacionReferencias);
		}
		else{
			Collections.sort(lista);
			atributos.put("operacionReferencias", lista);
		}
		contarElementosProcesados(operacionReferencia.getOperacion(), session, atributos);
		session.setAttribute("operacionReferenciaBusqueda", operacionReferencia);
		
		return "consultaOperacionReferencia";
	}
	
	@SuppressWarnings({ "unchecked"})
	@RequestMapping(value="/filtrarOperacionReferencia.html", method = RequestMethod.POST)
	public String filtrarOperacionReferencia(
			@RequestParam(value="observacionesHdn",required=false) String observaciones,
			@ModelAttribute("operacionReferenciaBusqueda") OperacionElemento operacionReferencia, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos, HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		if(operacionReferencia.isBuscarElemento()){
			this.validator.validate(operacionReferencia, result);
			if(!result.hasErrors()){
				session.setAttribute("operacionReferenciaBusqueda", operacionReferencia);
				atributos.put("errores", false);
				atributos.remove("result");
			}else{
				atributos.put("errores", true);
				atributos.put("result", result);			
			}	
			session.removeAttribute("operacionReferenciasSession");
			return mostrarOperacionReferencia(session, atributos, operacionReferencia.getIdOperacion());
		}
		else if(operacionReferencia.isBuscarElementoReferencia()){
			Operacion operacion = operacionService.obtenerPorId(operacionReferencia.getIdOperacion());
			operacion.setListaElementos(new HashSet<OperacionElemento>(operacionElementoService.listarOperacionElementoPorOperacion(operacion, obtenerClienteAsp())));
			return "redirect:iniciarRequerimientoElemento.html?clienteCodigoString=" + operacion.getClienteEmp().getCodigo() +"&destinoURL=prepararActualizarOperacionReferencia.html";
		}
		//Es guardar
		else{
			OperacionElemento operacionRef = (OperacionElemento) session.getAttribute("operacionReferenciaBusqueda");
			if(operacionRef!=null && operacionRef.getOperacion()!=null){
				
				operacionRef.getOperacion().setObservaciones(observaciones);
				operacionRef.getOperacion().getRequerimiento().setObservaciones(observaciones);
				
				//Armo un hash de los elementos existentes para actualizarlos, sino existe en el hash lo agrego
				Hashtable<Long, OperacionElemento> hashtable= new Hashtable<Long, OperacionElemento>();
				List<OperacionElemento> lista = (List<OperacionElemento>) session.getAttribute("operacionReferenciasSession");
				
				operacionRef.getOperacion().setListaElementos(new HashSet<OperacionElemento>(operacionReferenciaService.listarOperacionReferenciaFiltradas(operacionReferencia, operacionRef.getOperacion())));
				
				if(operacionRef.getOperacion().getListaElementos()!=null){
					for(OperacionElemento operacionElemento:operacionRef.getOperacion().getListaElementos()){
						hashtable.put(operacionElemento.getId(), operacionElemento);
					}
				}
				else{
					operacionRef.getOperacion().setListaElementos(new HashSet<OperacionElemento>());
				}
				
				String accionGuardar = null;
				if(operacionReferencia!=null)
					accionGuardar = operacionReferencia.getAccionGuardar();
				Boolean generaOperacSiguiente = operacionRef.getOperacion().getTipoOperacion().getGeneraOperacionAlCerrarse();
				TipoOperacion tipoOperacSiguiente = operacionRef.getOperacion().getTipoOperacion().getTipoOperacionSiguiente();
				List<Elemento> listaElementosProcesados = new ArrayList<Elemento>();
				List<OperacionElemento> listaElementosAFacturar = new ArrayList<OperacionElemento>();
				List<OperacionElemento> listaElementosLectura = new ArrayList<OperacionElemento>();
				List<Rearchivo> listaRearchivosActualizar = new ArrayList<Rearchivo>();
				for(OperacionElemento operacionElemento:lista){
					//Armo la lista de elementos de rearchivos a actualizar
					if(operacionElemento.getRearchivoDe()!=null && !operacionElemento.isTraspasado()){
						Rearchivo rearchivo = rearchivoService.obtenerRearchivoPorElemento(operacionElemento.getRearchivoDe());
						if(rearchivo!=null){
							rearchivo.setEstado("Procesado");
							listaRearchivosActualizar.add(rearchivo);
						}
					}
					if(operacionElemento.getId()==null) {//Es nuevo
						if(!operacionElemento.isFacturado() && !operacionElemento.isTraspasado() && operacionElemento.getEstado().equals("Procesado")){
							if(accionGuardar!=null && !"".equals(accionGuardar))
								if(accionGuardar.equals("finalizarOK") || accionGuardar.equals("finalizarError") || accionGuardar.equals("finalizarOKConTraspaso") 
										|| accionGuardar.equals("traspaso") || accionGuardar.equals("procesando") || accionGuardar.equals("finalizarErrorConTraspaso")){
									operacionElemento.setFacturado(true);
									operacionElemento.setProvieneLectura(true);
									listaElementosAFacturar.add(operacionElemento);
									listaElementosLectura.add(operacionElemento);
								}
								
						}
						if(generaOperacSiguiente!=null && generaOperacSiguiente.booleanValue() && tipoOperacSiguiente != null
								&& !operacionElemento.isTraspasado() && operacionElemento.getEstado().equals("Procesado")){
							if(accionGuardar!=null && !"".equals(accionGuardar))
								if(accionGuardar.equals("finalizarOKConTraspaso") || accionGuardar.equals("traspaso") || accionGuardar.equals("finalizarErrorConTraspaso")){
									operacionElemento.setTraspasado(true);
									if(operacionElemento.getRearchivoDe()==null)//No se pasan los rearchivos procesados
										listaElementosProcesados.add(operacionElemento.getElemento());
								}
						}
						operacionRef.getOperacion().getListaElementos().add(operacionElemento);
					}
					else{ //Ya existia, Busco y actualizo su estado
						OperacionElemento opBuscar = hashtable.get(operacionElemento.getId());
						if (opBuscar!=null){
							opBuscar.setEstado(operacionElemento.getEstado());
							if(!operacionElemento.isFacturado() && !operacionElemento.isTraspasado() && operacionElemento.getEstado().equals("Procesado")){
								if(accionGuardar!=null && !"".equals(accionGuardar))
									if(accionGuardar.equals("finalizarOK") || accionGuardar.equals("finalizarError") || accionGuardar.equals("finalizarOKConTraspaso") 
											|| accionGuardar.equals("traspaso") || accionGuardar.equals("procesando") || accionGuardar.equals("finalizarErrorConTraspaso"))
										opBuscar.setFacturado(true);
									
							}
							if(generaOperacSiguiente!=null && generaOperacSiguiente.booleanValue() && tipoOperacSiguiente != null
									&& !operacionElemento.isTraspasado() && operacionElemento.getEstado().equals("Procesado")){
								if(accionGuardar!=null && !"".equals(accionGuardar))
									if(accionGuardar.equals("finalizarOKConTraspaso") || accionGuardar.equals("traspaso"))
										opBuscar.setTraspasado(true);
							}
						}
					}
					if(!operacionElemento.isFacturado() && !operacionElemento.isTraspasado() && operacionElemento.getEstado().equals("Procesado")){
						if(accionGuardar!=null && !"".equals(accionGuardar))
							if(accionGuardar.equals("finalizarOK") || accionGuardar.equals("finalizarError") || accionGuardar.equals("finalizarOKConTraspaso") 
									|| accionGuardar.equals("traspaso") || accionGuardar.equals("procesando") || accionGuardar.equals("finalizarErrorConTraspaso")){
								operacionElemento.setFacturado(true);
								listaElementosAFacturar.add(operacionElemento);
								if(operacionElemento.isProvieneLectura())
									listaElementosLectura.add(operacionElemento);
							}
							
					}
					if(generaOperacSiguiente!=null && generaOperacSiguiente.booleanValue() && tipoOperacSiguiente != null
							&& !operacionElemento.isTraspasado() && operacionElemento.getEstado().equals("Procesado")){
						if(operacionElemento.getRearchivoDe()==null)//No se pasan los rearchivos procesados
							listaElementosProcesados.add(operacionElemento.getElemento());
						if(accionGuardar!=null && !"".equals(accionGuardar))
							if(accionGuardar.equals("finalizarOKConTraspaso") || accionGuardar.equals("traspaso") || accionGuardar.equals("finalizarErrorConTraspaso"))
								operacionElemento.setTraspasado(true);
					}
					
				}
				
				Boolean commit = null;
				ArrayList<Operacion> operaciones = new ArrayList<Operacion>();
				if(accionGuardar!=null && !"".equals(accionGuardar)){
					if(accionGuardar.equals("finalizarOKConTraspaso") || accionGuardar.equals("traspaso") || accionGuardar.equals("finalizarErrorConTraspaso")){
						//Genero operacion siguiente
						if(listaElementosProcesados!=null && listaElementosProcesados.size()>0){
							operaciones = (ArrayList<Operacion>) crearOperaciones(operacionRef.getOperacion(), operacionRef.getOperacion().getRequerimiento(), tipoOperacSiguiente, operacionRef.getOperacion().getDeposito(), listaElementosProcesados);
						}
						//En el caso de que se finalice una operacion sin elementos
						else{
							operaciones = (ArrayList<Operacion>) crearOperacionesSinElementos(operacionRef.getOperacion(), operacionRef.getOperacion().getRequerimiento(), tipoOperacSiguiente);
						}
					}
					if(accionGuardar.equals("finalizarOK") || accionGuardar.equals("finalizarOKConTraspaso"))
						operacionRef.getOperacion().setEstado("Finalizado OK");
					if(accionGuardar.equals("traspaso") || accionGuardar.equals("procesando"))
						operacionRef.getOperacion().setEstado("En Proceso");
					if(accionGuardar.equals("finalizarError") || accionGuardar.equals("finalizarErrorConTraspaso"))
						operacionRef.getOperacion().setEstado("Finalizado ERROR");
				}
				String tipo = null;
				if(operacionRef.getOperacion().getTipoOperacion()!=null && operacionRef.getOperacion().getTipoOperacion().getConceptoFacturable()!=null && operacionRef.getOperacion().getTipoOperacion().getConceptoFacturable().getTipoCalculo()!=null)
					tipo = operacionRef.getOperacion().getTipoOperacion().getConceptoFacturable().getTipoCalculo();
				//Creo los conceptos de operacion por el cliente cuando se finalicen parcial o total
				ConceptoOperacionCliente conceptoOperacionCliente = calcularConceptoOperacionCliente(listaElementosAFacturar, operacionRef.getOperacion(),operacionRef.getOperacion().getTipoOperacion().getConceptoFacturable(), tipo, operacionReferencia.getCantidadTipoCalculo());
				//Calculo los conceptos de venta y stock para los elementos provinientes de lectura
				ArrayList<ConceptoOperacionCliente> conceptosVentas = new ArrayList<ConceptoOperacionCliente>();
				ArrayList<Stock> stocks = new ArrayList<Stock>();
				calcularConceptosProvinientesLectura(listaElementosLectura, operacionRef.getOperacion(), tipo, operacionReferencia.getCantidadTipoCalculo(), conceptosVentas, stocks);
				
				commit = operacionService.update(operacionRef.getOperacion(),operaciones, conceptoOperacionCliente, conceptosVentas, stocks, listaRearchivosActualizar);
				
				String mensajeUpdate = "";
				//Cuento los requerimientos que esten en pendientes o en proceso, en caso de no existir actualizo el estado del requerimiento (finalizado ok o error si tiene operaciones con error)
				if(commit!=null && commit){
					Requerimiento requerimiento = operacionRef.getOperacion().getRequerimiento();
					if(requerimiento!=null){
						Integer pendientes = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_PENDIENTE, obtenerClienteAsp());
						Integer enProceso = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_EN_PROCESO, obtenerClienteAsp());
						Integer finalizadoOk = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_FINALIZADO_OK, obtenerClienteAsp());
						Integer finalizadoError = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_FINALIZADO_ERROR, obtenerClienteAsp());
						Integer cancelado = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_CANCELADO, obtenerClienteAsp());
						if(pendientes!=null && enProceso != null && finalizadoError != null && cancelado !=null && finalizadoOk != null){
							if(pendientes.intValue() == 0 && enProceso.intValue() == 0 && finalizadoError > 0 && cancelado == 0){
								if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("Finalizado ERROR"))
									mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoFinERROR";
								requerimiento.setEstado("Finalizado ERROR");
							}
							if(enProceso.intValue() > 0 && finalizadoError == 0 && cancelado == 0){
								if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("En Proceso"))
									mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoEnProceso";
								requerimiento.setEstado("En Proceso");
							}
							if(pendientes.intValue() == 0 && enProceso.intValue() == 0 && finalizadoError == 0 && cancelado > 0 && finalizadoOk == 0){
								if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("Cancelado"))
									mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoCancelado";
								requerimiento.setEstado("Cancelado");
							}
							if(pendientes.intValue() == 0 && enProceso.intValue() == 0 && finalizadoError == 0 && cancelado >= 0 && finalizadoOk > 0){
								if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("Finalizado OK"))
									mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoFinOK";
								requerimiento.setEstado("Finalizado OK");
							}
						}
						commit = requerimientoService.update(requerimiento);
					}
				}
				Remito remito = null; 
				if(commit!=null && commit){
					
					if(operacionRef.getOperacion().getTipoOperacion().getImprimeRemito())
					{
						remito = new Remito();
						remito.setClienteAsp(obtenerClienteAsp());
						remito.setClienteEmp(operacionRef.getOperacion().getClienteEmp());
						remito.setDepositoOrigen(operacionRef.getOperacion().getDeposito());
						remito.setDireccion(operacionRef.getOperacion().getRequerimiento().getDireccionDefecto());
						remito.setEmpleado(operacionRef.getOperacion().getRequerimiento().getEmpleadoSolicitante());
						remito.setEmpresa(operacionRef.getOperacion().getRequerimiento().getClienteEmp().getEmpresa());
						remito.setSucursal(operacionRef.getOperacion().getRequerimiento().getSucursal());
						remito.setObservacion(operacionRef.getOperacion().getRequerimiento().getObservaciones());
						remito.setEstado(Constantes.REMITO_ESTADO_PENDIENTE);
						remito.setFechaEmision(new Date());
						remito.setFechaEntrega(operacionRef.getOperacion().getRequerimiento().getFechaEntrega());
						remito.setIngresoEgreso("egreso");
						remito.setTipoRemito("cliente");
						remito.setSerie(operacionRef.getOperacion().getTipoOperacion().getSerieRemito());
						remito.setNumeroSinPrefijo(ultimoNumeroSerie(operacionRef.getOperacion().getTipoOperacion().getSerieRemito()));
						remito.setPrefijo(operacionRef.getOperacion().getTipoOperacion().getSerieRemito().getPrefijo());
						remito.setNumero(Long.valueOf(remito.getPrefijo() + remito.getNumeroSinPrefijo()));
						remito.setUsuario(obtenerUser());
						remito.setTipoRequerimiento(operacionRef.getOperacion().getRequerimiento().getTipoRequerimiento().getDescripcion());
						remito.setNumRequerimiento(operacionRef.getOperacion().getRequerimiento().getSerie().getCodigo()+": "+
								operacionRef.getOperacion().getRequerimiento().getPrefijoStr()+" - "+operacionRef.getOperacion().getRequerimiento().getNumeroStr());
						remito.setTipoComprobante(operacionRef.getOperacion().getTipoOperacion().getSerieRemito().getAfipTipoComprobante());
						remito.setLetraComprobante(operacionRef.getOperacion().getTipoOperacion().getSerieRemito().getAfipTipoComprobante().getLetra());
						remito.setEmpleadoSolicitante(operacionRef.getOperacion().getRequerimiento().getEmpleadoSolicitante().getApellidoYNombre());
						remito.setFechaSolicitud(operacionRef.getOperacion().getRequerimiento().getFechaAltaStr());
						Set<RemitoDetalle> detalles = new HashSet<RemitoDetalle>();
						
						List<Elemento> listaElementos = new ArrayList<Elemento>();
						if(listaElementosProcesados.size()>0){
							listaElementos = listaElementosProcesados;
						}
						else if(listaElementosAFacturar.size()>0){
							for(int i =0;i<listaElementosAFacturar.size();i++){
								listaElementos.add(listaElementosAFacturar.get(i).getElemento());
							}
						}					
						for(Elemento elemento: listaElementos)
						{
							RemitoDetalle detalle = new RemitoDetalle();
							detalle.setElemento(elemento);
							detalle.setRemito(remito);
							detalles.add(detalle);
						}
						
						remito.setDetalles(detalles);
						remito.setCantidadElementos(detalles.size());
						
						commit = remitoService.guardarRemitoYDetalles(detalles, remito);
						
					}
					
				}
				
				Boolean existeMovimiento = false;
				if(commit!=null && commit){
					
					if(operacionRef.getOperacion().getTipoOperacion().getGeneraMovimiento())
					{
						List<Movimiento> listaMovimientos = new ArrayList<Movimiento>();
						List<Posicion> listaPosiciones = new ArrayList<Posicion>();
						List<Elemento> listaElementos = new ArrayList<Elemento>();
						
						if(listaElementosProcesados.size()>0){
							listaElementos = listaElementosProcesados;
						}
						else if(listaElementosAFacturar.size()>0){
							for(int i =0;i<listaElementosAFacturar.size();i++){
								listaElementos.add(listaElementosAFacturar.get(i).getElemento());
							}
						}
						
						for(Elemento elemento: listaElementos)
						{
							
							Movimiento movimiento = new Movimiento();
							movimiento.setClienteAsp(obtenerClienteAsp());
							movimiento.setClienteEmpOrigenDestino(operacionRef.getOperacion().getClienteEmp());
							movimiento.setDeposito(operacionRef.getOperacion().getDeposito());
							movimiento.setTipoMovimiento(Constantes.MOVIMIENTO_TIPO_MOVIMIENTO_EGRESO);
							movimiento.setClaseMovimiento("cliente");
							movimiento.setFecha(new Date());
							movimiento.setUsuario(obtenerUser());
							movimiento.setResponsable(obtenerUser());
							movimiento.setRemito(remito);
							movimiento.setDescripcionRemito(operacionRef.getOperacion().getRequerimiento().getSerie().getCodigo()+": "+
										operacionRef.getOperacion().getRequerimiento().getPrefijoStr()+" - "+operacionRef.getOperacion().getRequerimiento().getNumeroStr());
							
							if(elemento.getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_GUARDA))
							{
								elemento.setEstado(Constantes.ELEMENTO_ESTADO_EN_CONSULTA);
							}
							else if(elemento.getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_CREADO))
							{
								elemento.setEstado(Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE);
							}
							
							movimiento.setEstadoElemento(elemento.getEstado());
							Posicion posicion = elemento.getPosicion();
							if(posicion != null)
							{
								posicion.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
								movimiento.setPosicionOrigenDestino(posicion);
								listaPosiciones.add(posicion);
								elemento.setPosicion(null);
							}
							
							movimiento.setElemento(elemento);
							listaMovimientos.add(movimiento);
						}
						commit = movimientoService.guardarMovimientoListYActualizarPosiciones(listaMovimientos, listaPosiciones);
						if(commit!=null && commit){
							existeMovimiento = true;
						}
					}
					
				}
				
				//Ver errores
				if(commit != null && !commit){
					List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
					ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
					avisos.add(mensaje); //agrego el mensaje a la coleccion
					atributos.put("errores", false);
					atributos.remove("result");
					atributos.put("hayAvisos", false);
					atributos.put("hayAvisosNeg", true);
					atributos.put("avisos", avisos);
					return mostrarOperacionReferencia(session, atributos, operacionRef.getOperacion().getId());	
				}
				//Genero las notificaciones 
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				if("".equals(mensajeUpdate))
					mensajeUpdate = "formularioOperacionReferencia.notif.modificado";
				ScreenMessage notificacion = new ScreenMessageImp(mensajeUpdate, null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
				if(remito!=null){
					//Remito
					List<String> remitoList = new ArrayList<String>();
					remitoList.add(remito.getLetraYNumeroComprobante()+" ");			
					ScreenMessage mensajeRemito = new ScreenMessageImp("formularioRemito.notificacion.remitoRegistradoEnOperacion", remitoList);
					avisos.add(mensajeRemito); //agrego el mensaje a la coleccion
				}
				if(existeMovimiento){
					//Movimiento
					ScreenMessage mensajeMovimiento = new ScreenMessageImp("formularioMovimiento.notificacion.movimientosRegistradoEnOperacion", null);
					avisos.add(mensajeMovimiento); //agrego el mensaje a la coleccion
				}
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", true);
				atributos.put("avisos", avisos);
				return listaOperacionController.mostrarOperacion(session, atributos, null, null, null, null, null, null, request);
			}
		}
		return mostrarOperacionReferencia(session, atributos, null);	
	}
	
	@RequestMapping(
			value="/prepararActualizarOperacionReferencia.html",
			method=RequestMethod.POST
		)
	public String preparar(
			@RequestParam(value="requerimientoElementosSel",required=false)String requerimientoElementosSel,
			HttpSession session, Map<String, Object> atributos
			) {
		OperacionElemento operacionReferencia = (OperacionElemento) session.getAttribute("operacionReferenciaBusqueda");
		if(requerimientoElementosSel!=null && !"".equals(requerimientoElementosSel) && operacionReferencia.getOperacion()!=null){
			if(operacionReferencia.getOperacion().getListaElementos()==null)
				operacionReferencia.getOperacion().setListaElementos(new HashSet<OperacionElemento>());
			Hashtable<Long, OperacionElemento> hashBuscar = new Hashtable<Long, OperacionElemento>();
			for(OperacionElemento req:operacionReferencia.getOperacion().getListaElementos()){
				if(req.getElemento()!=null && req.getElemento().getId()!=null){
					if(hashBuscar.get(req.getElemento().getId()) == null)
						hashBuscar.put(req.getElemento().getId(), req);
				}
					
			}
			String[] seleccionados = requerimientoElementosSel.split(",");
			for(String buscar:seleccionados){
				Referencia referencia = referenciaService.obtenerPorId(new Long(buscar));
				OperacionElemento opeR = new OperacionElemento();
				opeR.setOperacion(operacionReferencia.getOperacion());
				if(referencia!=null)
				opeR.setElemento(referencia.getElemento());
				opeR.setEstado(ESTADO_OPERACION_ELEMENTO_PENDIENTE);
				
				//Solamente se incorporan los del mismo deposito
				Deposito deposito = null;
				if(opeR.getElemento()!=null 
						&& (opeR.getElemento().getDepositoActual()!=null || 
								(opeR.getElemento().getContenedor()!= null && opeR.getElemento().getContenedor().getDepositoActual()!=null))){
					if(opeR.getElemento().getContenedor()==null)
						deposito = opeR.getElemento().getDepositoActual();
					else{
						if(opeR.getElemento().getContenedor().getDepositoActual()!=null)
							deposito = opeR.getElemento().getContenedor().getDepositoActual();
					}
				}
				
				if(deposito != null
						&& deposito.getId().longValue() == operacionReferencia.getOperacion().getDeposito().getId().longValue())
					if(hashBuscar.get(opeR.getElemento().getId())==null){
						operacionReferencia.getOperacion().getListaElementos().add(opeR);
						hashBuscar.put(opeR.getElemento().getId(), opeR);
					}
			}
		}
		if(operacionReferencia.getOperacion()!=null){
			session.setAttribute("operacionReferenciasSession", operacionReferencia.getOperacion().getListaElementos());
			if(operacionReferencia.getOperacion().getListaElementos()!=null && operacionReferencia.getOperacion().getListaElementos().size()>0)
				atributos.put("listaConElementos", "SI");
			else
				atributos.put("listaConElementos", "NO");
		}
		Long idOperacion = null;
		if(operacionReferencia.getOperacion()!=null)
			idOperacion = operacionReferencia.getOperacion().getId();
		session.setAttribute("operacionReferenciaBusqueda",operacionReferencia);
		return mostrarOperacionReferencia(session, atributos, idOperacion);
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/filtrarElementoOperacionReferencia.html", 
			method=RequestMethod.POST)
	public String filtrarElementoOperacionReferencia(HttpSession session, Map<String,Object> atributos, 
			@RequestParam(value="codigoElemento",required=false) String codigoElemento,
			@RequestParam(value="elementosSel",required=false)String elementosSel,
			@RequestParam(value="estadoSeleccion",required=false)String estadoSeleccion,
			@RequestParam(value="codigoLectura",required=false)String codigoLectura,
			@RequestParam(value="seleccionBusqueda",required=false)String seleccionBusqueda,
			@RequestParam(value="observaciones",required=false)String observaciones){
		OperacionElemento operacionReferencia = (OperacionElemento) session.getAttribute("operacionReferenciaBusqueda");
		Long idOperacion = null;
		if(operacionReferencia.getOperacion()!=null){
			idOperacion = operacionReferencia.getOperacion().getId();
			operacionReferencia.getOperacion().setObservaciones(observaciones);
			session.setAttribute("operacionReferenciaBusqueda", operacionReferencia);
		}
		List<OperacionElemento> lista = (List<OperacionElemento>) session.getAttribute("operacionReferenciasSession");
		String[] seleccion = null;
		if(seleccionBusqueda!=null){
			//Si actualiza estado
			if(!seleccionBusqueda.equals("porLectura")){
				if(elementosSel!=null && !"".equals(elementosSel))
					seleccion = elementosSel.split(",");
				if(lista!=null && lista.size()>0){
					HashMap<Long, OperacionElemento> hashElementosRearchivos = new HashMap<Long, OperacionElemento>();
					for(OperacionElemento operacionElemento:lista){
						
						//Cargo el hash de elementos de rearchivos para verificar su estado
						if(operacionElemento.getRearchivoDe()!=null){
							hashElementosRearchivos.put(operacionElemento.getRearchivoDe().getId(), operacionElemento);
						}
						//Actualiza por Codigo de Barras
						if(codigoElemento!=null && !"".equals(codigoElemento) && seleccionBusqueda.equals("porElemento")){
							String buscar = codigoElemento;
							if(codigoElemento.length()>12)
								buscar = codigoElemento.substring(0,12);
							if(buscar!=null && !"".equals(buscar) && operacionElemento.getElemento()!= null &&
									operacionElemento.getElemento().getCodigo().equalsIgnoreCase(buscar)){
								operacionElemento.setEstado(ESTADO_OPERACION_ELEMENTO_PROCESADO);
								//break;
							}
						}
						//Actualiza por seleccion
						if(seleccion != null && seleccion.length>0 && seleccionBusqueda.equals("porSeleccion")){
							for(String idStr:seleccion){
								Long id = null;
								try {
									id = Long.parseLong(idStr);
								} catch (NumberFormatException e) {
									
								}
								if(id != null && operacionElemento.getElemento()!=null && operacionElemento.getElemento().getId()!=null &&
										id.longValue() == operacionElemento.getElemento().getId().longValue()){
									operacionElemento.setEstado(estadoSeleccion);
									break;
								}
								
							}
						}
					}
					//Si existen rearchivos en la lista, recorro de nuevo para verificar sus estados sean distinto a Pendiente
					boolean rearchivosPendientes = false;
					if(hashElementosRearchivos!=null && hashElementosRearchivos.size()>0){
						for(OperacionElemento operacionElemento:lista){
							if(operacionElemento.getRearchivoDe()==null && !operacionElemento.getEstado().equals(ESTADO_OPERACION_ELEMENTO_PENDIENTE)){
								OperacionElemento opeRearchivo = hashElementosRearchivos.get(operacionElemento.getElemento().getId());
								if(opeRearchivo!=null && opeRearchivo.getEstado().equals(ESTADO_OPERACION_ELEMENTO_PENDIENTE)){
									rearchivosPendientes = true;
									break;
								}
							}
						}
					}
					session.setAttribute("rearchivosPendientesEnOperacion", rearchivosPendientes);
				}
			}
			//Si agrega elementos por lectura
			else{
				Long codigo = null;
				try {
					codigo = Long.parseLong(codigoLectura);
				} catch (NumberFormatException e) {
					
				}
				if(codigo!=null){
					//Armo un hash de los elementos existentes para no incorporarlos dos veces
					Hashtable<Long, Elemento> hashtable= new Hashtable<Long, Elemento>();
					for(OperacionElemento operacionElemento:lista){
						if(operacionElemento.getElemento()!=null && operacionElemento.getElemento().getId()!=null)
							hashtable.put(operacionElemento.getElemento().getId(), operacionElemento.getElemento());
					}
					if(operacionReferencia.getOperacion()!=null){
						Lectura lectura = lecturaService.obtenerPorCodigo(codigo, false, obtenerEmpresaDefault(), obtenerClienteAsp());
						Deposito deposito = operacionReferencia.getOperacion().getDeposito();
						boolean desagregaPorDeposito = false;
						if(operacionReferencia.getOperacion().getTipoOperacion()!=null && operacionReferencia.getOperacion().getTipoOperacion().getDesagregaPorDeposito())
							desagregaPorDeposito = true;
						//Busco el detalle de la lectura, ya que el set q esta en Lectura es LAZY
						List<LecturaDetalle> listDetalle = lecturaDetalleService.listarLecturaDetallePorLectura(lectura, obtenerClienteAsp());
						if(listDetalle!=null && listDetalle.size()>0){
							for(LecturaDetalle lecturaDetalle:listDetalle){
								Elemento elemento = lecturaDetalle.getElemento();
								if(elemento!=null){
									//Si la operacion desagrega por deposito, solo se incorporan los elementos del mismo deposito o en null
									Deposito depositoBuscar = null;
									if(elemento.getDepositoActual()!=null || 
													(elemento.getContenedor()!= null && elemento.getContenedor().getDepositoActual()!=null)){
										if(elemento.getContenedor()==null)
											depositoBuscar = elemento.getDepositoActual();
										else{
											if(elemento.getContenedor().getDepositoActual()!=null)
												depositoBuscar = elemento.getContenedor().getDepositoActual();
										}
									}
									
									if(desagregaPorDeposito && depositoBuscar!=null && deposito != null 
											&& depositoBuscar.getId() != null && deposito.getId() != null &&
											depositoBuscar.getId().longValue() != deposito.getId().longValue())
										continue;
									
									//Verifico que no este en la lista
									if(elemento.getId()!=null && hashtable.get(elemento.getId())!=null)
										continue;
									List<OperacionElemento> elementosPendientes = operacionElementoService.listarOperacionElementoPorElementoYEstado(elemento, ESTADO_OPERACION_ELEMENTO_PENDIENTE, obtenerClienteAsp());
									if(elementosPendientes!=null && elementosPendientes.size()>0)
										continue;
									OperacionElemento operacionElemento = new OperacionElemento();
									operacionElemento.setOperacion(operacionReferencia.getOperacion());
									operacionElemento.setElemento(elemento);
									operacionElemento.setEstado(ESTADO_OPERACION_ELEMENTO_PROCESADO);
									operacionElemento.setLectura(true);
									operacionElemento.setProvieneLectura(true);
									//Si el elemento no tiene deposito, seteamos el de la operacion
									if(elemento.getDepositoActual()==null)
										elemento.setDepositoActual(operacionReferencia.getOperacion().getDeposito());
									//Si el elemento no tiene cliente, seteamos el de la operacion
									if(elemento.getClienteEmp()==null)
										elemento.setClienteEmp(operacionReferencia.getOperacion().getClienteEmp());
									lista.add(operacionElemento);
								}
							}
						}
					}
				}
			}
		}
		//Verifico si se han encontrado todos los elementos
		session.setAttribute("operacionReferenciasSession",lista);
		return mostrarOperacionReferencia(session, atributos, idOperacion);
	}
	
	@RequestMapping(value="/finalizarOperacionReferencia.html", method = RequestMethod.GET)
	public String finalizarOperacionReferencia(HttpSession session, Map<String,Object> atributos, 
			@RequestParam(value="idOperacion",required=false) Long idOperacion,
			@RequestParam(value="accionGuardar",required=false) String accionGuardar,
			@RequestParam(value="tipoCalculo",required=false) String tipoCalculo,
			@RequestParam(value="cantidadTipoCalculo",required=false) Long cantidadTipoCalculo, HttpServletRequest request){
		Operacion operacion = operacionService.obtenerPorId(idOperacion);
		operacion.setListaElementos(new HashSet<OperacionElemento>(operacionElementoService.listarOperacionElementoPorOperacion(operacion, obtenerClienteAsp())));
		if(operacion!=null){
			if(operacion.getListaElementos()==null){
				operacion.setListaElementos(new HashSet<OperacionElemento>());
			}
			
			Boolean generaOperacSiguiente = operacion.getTipoOperacion().getGeneraOperacionAlCerrarse();
			TipoOperacion tipoOperacSiguiente = operacion.getTipoOperacion().getTipoOperacionSiguiente();
			List<Elemento> listaElementosProcesados = new ArrayList<Elemento>();
			List<OperacionElemento> listaElementosAFacturar = new ArrayList<OperacionElemento>();
			List<OperacionElemento> listaElementosLectura = new ArrayList<OperacionElemento>();
			List<Rearchivo> listaRearchivosActualizar = new ArrayList<Rearchivo>();
			for(OperacionElemento operacionElemento:operacion.getListaElementos()){
				//Armo la lista de elementos de rearchivos a actualizar
				if(operacionElemento.getRearchivoDe()!=null && !operacionElemento.isTraspasado()){
					Rearchivo rearchivo = rearchivoService.obtenerRearchivoPorElemento(operacionElemento.getRearchivoDe());
					if(rearchivo!=null){
						rearchivo.setEstado("Procesado");
						listaRearchivosActualizar.add(rearchivo);
					}
				}
				if(!operacionElemento.isFacturado() && !operacionElemento.isTraspasado() && operacionElemento.getEstado().equals("Procesado")){
					if(accionGuardar!=null && !"".equals(accionGuardar))
						if(accionGuardar.equals("finalizarOK") || accionGuardar.equals("finalizarError") || accionGuardar.equals("finalizarOKConTraspaso") 
								|| accionGuardar.equals("traspaso") || accionGuardar.equals("procesando") || accionGuardar.equals("finalizarErrorConTraspaso")){
							operacionElemento.setFacturado(true);
							listaElementosAFacturar.add(operacionElemento);
							if(operacionElemento.isProvieneLectura())
								listaElementosLectura.add(operacionElemento);
						}
						
				}
				if(generaOperacSiguiente!=null && generaOperacSiguiente.booleanValue() && tipoOperacSiguiente != null
						&& !operacionElemento.isTraspasado() && operacionElemento.getEstado().equals("Procesado")){
					if(operacionElemento.getRearchivoDe()==null)//No se pasan los rearchivos procesados
						listaElementosProcesados.add(operacionElemento.getElemento());
					if(accionGuardar!=null && !"".equals(accionGuardar))
						if(accionGuardar.equals("finalizarOKConTraspaso") || accionGuardar.equals("traspaso") || accionGuardar.equals("finalizarErrorConTraspaso"))		
							operacionElemento.setTraspasado(true);
				}
			}
			Boolean commit = null;
			ArrayList<Operacion> operaciones = new ArrayList<Operacion>();
			if(accionGuardar!=null && !"".equals(accionGuardar)){
				if(accionGuardar.equals("finalizarOKConTraspaso") || accionGuardar.equals("traspaso") || accionGuardar.equals("finalizarErrorConTraspaso")){
					//Genero operacion siguiente
					if(listaElementosProcesados!=null && listaElementosProcesados.size()>0){
						operaciones = (ArrayList<Operacion>) crearOperaciones(operacion, operacion.getRequerimiento(), tipoOperacSiguiente, operacion.getDeposito(), listaElementosProcesados);
					}
					//En el caso de que se finalice una operacion sin elementos
					else{
						operaciones = (ArrayList<Operacion>) crearOperacionesSinElementos(operacion, operacion.getRequerimiento(), tipoOperacSiguiente);
					}
				}
				if(accionGuardar.equals("finalizarOK") || accionGuardar.equals("finalizarOKConTraspaso"))
					operacion.setEstado("Finalizado OK");
				if(accionGuardar.equals("traspaso") || accionGuardar.equals("procesando"))
					operacion.setEstado("En Proceso");
				if(accionGuardar.equals("finalizarError") || accionGuardar.equals("finalizarErrorConTraspaso"))
					operacion.setEstado("Finalizado ERROR");
			}
			
			//Creo los conceptos de operacion por el cliente cuando se finalicen parcial o total
			ConceptoOperacionCliente conceptoOperacionCliente = calcularConceptoOperacionCliente(listaElementosAFacturar, operacion, operacion.getTipoOperacion().getConceptoFacturable(), tipoCalculo, cantidadTipoCalculo);
			
			//Calculo los conceptos de venta y stock para los elementos provinientes de lectura
			ArrayList<ConceptoOperacionCliente> conceptosVentas = new ArrayList<ConceptoOperacionCliente>();
			ArrayList<Stock> stocks = new ArrayList<Stock>();
			calcularConceptosProvinientesLectura(listaElementosLectura, operacion, tipoCalculo, cantidadTipoCalculo, conceptosVentas, stocks);
			
			//Comiteo para poder actualizar el estado del requerimiento
			commit = operacionService.update(operacion,operaciones, conceptoOperacionCliente, conceptosVentas, stocks, listaRearchivosActualizar);
			
			String mensajeUpdate = "";
			//Cuento los requerimientos que esten en pendientes o en proceso, en caso de no existir actualizo el estado del requerimiento (finalizado ok o error si tiene operaciones con error)
			if(commit!=null && commit){
				Requerimiento requerimiento = operacion.getRequerimiento();
				if(requerimiento!=null){
					Integer pendientes = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_PENDIENTE, obtenerClienteAsp());
					Integer enProceso = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_EN_PROCESO, obtenerClienteAsp());
					Integer finalizadoOk = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_FINALIZADO_OK, obtenerClienteAsp());
					Integer finalizadoError = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_FINALIZADO_ERROR, obtenerClienteAsp());
					Integer cancelado = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_CANCELADO, obtenerClienteAsp());
					if(pendientes!=null && enProceso != null && finalizadoError != null && cancelado !=null && finalizadoOk != null){
						if(pendientes.intValue() == 0 && enProceso.intValue() == 0 && finalizadoError > 0 && cancelado == 0){
							if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("Finalizado ERROR"))
								mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoFinERROR";
							requerimiento.setEstado("Finalizado ERROR");
						}
						if(enProceso.intValue() > 0 && finalizadoError == 0 && cancelado == 0){
							if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("En Proceso"))
								mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoEnProceso";
							requerimiento.setEstado("En Proceso");
						}
						if(pendientes.intValue() == 0 && enProceso.intValue() == 0 && finalizadoError == 0 && cancelado > 0 && finalizadoOk == 0){
							if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("Cancelado"))
								mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoCancelado";
							requerimiento.setEstado("Cancelado");
						}
						if(pendientes.intValue() == 0 && enProceso.intValue() == 0 && finalizadoError == 0 && cancelado >= 0 && finalizadoOk > 0){
							if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("Finalizado OK"))
								mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoFinOK";
							requerimiento.setEstado("Finalizado OK");
						}
					}
					commit = requerimientoService.update(requerimiento);
				}
			}
			
			//Ver errores
			if(commit != null && !commit){
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
				return mostrarOperacionReferencia(session, atributos, operacion.getId());	
			}
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			if("".equals(mensajeUpdate))
				mensajeUpdate = "formularioOperacionReferencia.notif.modificado";
			ScreenMessage notificacion = new ScreenMessageImp(mensajeUpdate, null);
			avisos.add(notificacion); //agrego el mensaje a la coleccion
			
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
			return listaOperacionController.mostrarOperacion(session, atributos, null, null, null, null, null, null, request);
		}
		return mostrarOperacionReferencia(session, atributos, idOperacion);
	}
	
	@RequestMapping(value="/cancelarOperacionReferencia.html", method = RequestMethod.GET)
	public String cancelarOperacionReferencia(HttpSession session, Map<String,Object> atributos, 
			@RequestParam(value="idOperacion",required=false) Long idOperacion, HttpServletRequest request){
		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		
		Operacion operacion = operacionService.obtenerPorId(idOperacion);
		operacion.setListaElementos(new HashSet<OperacionElemento>(operacionElementoService.listarOperacionElementoPorOperacion(operacion, obtenerClienteAsp())));
		if(operacion!=null){
			if(operacion.getListaElementos()==null){
				operacion.setListaElementos(new HashSet<OperacionElemento>());
			}

			for(OperacionElemento operacionElemento:operacion.getListaElementos()){
				operacionElemento.setEstado(ESTADO_OPERACION_ELEMENTO_CANCELADO);
			}
			Boolean commit = null;
			operacion.setEstado("Cancelado");
			
			commit = operacionService.update(operacion,null);	
			
			String mensajeUpdate = "";
			//Cuento los requerimientos que esten en pendientes o en proceso, en caso de no existir actualizo el estado del requerimiento (finalizado ok o error si tiene operaciones con error)
			if(commit!=null && commit){
				Requerimiento requerimiento = operacion.getRequerimiento();
				if(requerimiento!=null){
					Integer pendientes = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_PENDIENTE, obtenerClienteAsp());
					Integer enProceso = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_EN_PROCESO, obtenerClienteAsp());
					Integer finalizadoOk = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_FINALIZADO_OK, obtenerClienteAsp());
					Integer finalizadoError = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_FINALIZADO_ERROR, obtenerClienteAsp());
					Integer cancelado = operacionService.contarOperacionesPorRequerimientoYEstado(requerimiento, ESTADO_OPERACION_CANCELADO, obtenerClienteAsp());
					if(pendientes!=null && enProceso != null && finalizadoError != null && cancelado !=null && finalizadoOk != null){
						if(pendientes.intValue() == 0 && enProceso.intValue() == 0 && finalizadoError > 0 && cancelado == 0){
							if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("Finalizado ERROR"))
								mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoFinERROR";
							requerimiento.setEstado("Finalizado ERROR");
						}
						if(enProceso.intValue() > 0 && finalizadoError == 0 && cancelado == 0){
							if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("En Proceso"))
								mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoEnProceso";
							requerimiento.setEstado("En Proceso");
						}
						if(pendientes.intValue() == 0 && enProceso.intValue() == 0 && finalizadoError == 0 && cancelado > 0 && finalizadoOk == 0){
							if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("Cancelado"))
								mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoCancelado";
							requerimiento.setEstado("Cancelado");
						}
						if(pendientes.intValue() == 0 && enProceso.intValue() == 0 && finalizadoError == 0 && cancelado >= 0 && finalizadoOk > 0){
							if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase("Finalizado OK"))
								mensajeUpdate = "formularioOperacionReferencia.notif.requerimientoFinOK";
							requerimiento.setEstado("Finalizado OK");
						}
					}
					commit = requerimientoService.update(requerimiento);
				}
			}
			
			//Ver errores
			if(commit != null && !commit){
			
				ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
				return mostrarOperacionReferencia(session, atributos, operacion.getId());	
			}
			//Genero las notificaciones 
			//List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			if("".equals(mensajeUpdate))
				mensajeUpdate = "formularioOperacion.notif.cancelado";
			ScreenMessage notificacion = new ScreenMessageImp(mensajeUpdate, null);
			avisos.add(notificacion); //agrego el mensaje a la coleccion
			
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
			return listaOperacionController.mostrarOperacion(session, atributos, null, null, null, null, null, null, request);
		}
		return mostrarOperacionReferencia(session, atributos, idOperacion);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(
			value="/eliminarOperacionReferencia.html",
			method = RequestMethod.GET
		)
	public String eliminarOperacionReferencia(HttpSession session,
			@RequestParam("id") String idStr,
			Map<String,Object> atributos,
			HttpServletRequest request) {
		
		OperacionElemento operacionReferencia = (OperacionElemento) session.getAttribute("operacionReferenciaBusqueda");
		Long idOperacion = null;
		if(operacionReferencia.getOperacion()!=null)
			idOperacion = operacionReferencia.getOperacion().getId();
		List<OperacionElemento> lista = (List<OperacionElemento>) session.getAttribute("operacionReferenciasSession");
		
		if(lista!=null && lista.size()>0){
			Iterator<OperacionElemento> iteOp= lista.iterator();
			while(iteOp.hasNext()){
				OperacionElemento operacionElemento = iteOp.next();
				Long id = null;
				try {
					id = Long.parseLong(idStr);
				} catch (NumberFormatException e) {
					
				}
				if(id != null && operacionElemento.getElemento()!=null && operacionElemento.getElemento().getId()!=null &&
						id.longValue() == operacionElemento.getElemento().getId().longValue()){
					iteOp.remove();
					break;
				}	
			}
		}
		
		session.setAttribute("operacionReferenciasSession",lista);
		return mostrarOperacionReferencia(session, atributos, idOperacion);
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
	private Sucursal obtenerSucursalUser(){
		return ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto();
	}
	
	private List<Operacion> crearOperaciones (Operacion operacionAnterior, Requerimiento requerimiento, TipoOperacion tipoOperacion, Deposito deposito, List<Elemento> listaElementos){
		ArrayList<Operacion> salida = new ArrayList<Operacion>();
		//Se genera una sola operacion con todos los elementos
		if(!tipoOperacion.getDesagregaPorDeposito()){
			//Se genera una operacion por tipo de operacion
			Operacion operacion = new Operacion();
			setDataBasicoOperacion(operacion, requerimiento, tipoOperacion);
			//Si la cantidad de elementos es mayor a 1 y pertenecen a distintos depositos se utiliza el que se especifico en el requerimiento, sino se utiliza el deposito del primer elemento
			if(listaElementos!=null &&listaElementos.size()>1){
				if(verificarDistintoDeposito(listaElementos)){
					operacion.setDeposito(operacionAnterior.getDeposito());
				//Si son del mismo deposito asigno el de cualquier elemento
				}
				else{
					Elemento elemento = null;
					for(Elemento el:listaElementos){
						elemento = el;
						break;
					}
//					if(elemento!=null
//							&& elemento.getDepositoActual()!=null)
//						operacion.setDeposito(elemento.getDepositoActual());
//					else
//						operacion.setDeposito(operacionAnterior.getDeposito());
					
					if(elemento!=null 
							&& (elemento.getDepositoActual()!=null || 
									(elemento.getContenedor()!= null && elemento.getContenedor().getDepositoActual()!=null))){
						if(elemento!=null && elemento.getDepositoActual()!=null)
							operacion.setDeposito(elemento.getDepositoActual());
						else{
							if(elemento.getContenedor().getDepositoActual()!=null)
								operacion.setDeposito(elemento.getContenedor().getDepositoActual());
						}
					}
					else
						operacion.setDeposito(operacionAnterior.getDeposito());
				}
			}
			//Si la cantidad de elementos es igual a 1, se asigna al deposito de la operacion el mismo del unico elemento
			if(listaElementos!=null && listaElementos.size()==1){
				Elemento elemento = null;
				for(Elemento el:listaElementos){
					elemento = el;
					break;
				}
//				if(elemento!=null
//						&& elemento.getDepositoActual()!=null)
//					operacion.setDeposito(elemento.getDepositoActual());
//				else
//					operacion.setDeposito(operacionAnterior.getDeposito());
				
				if(elemento!=null 
						&& (elemento.getDepositoActual()!=null || 
								(elemento.getContenedor()!= null && elemento.getContenedor().getDepositoActual()!=null))){
					if(elemento!=null && elemento.getDepositoActual()!=null)
						operacion.setDeposito(elemento.getDepositoActual());
					else{
						if(elemento.getContenedor().getDepositoActual()!=null)
							operacion.setDeposito(elemento.getContenedor().getDepositoActual());
					}
				}
				else
					operacion.setDeposito(operacionAnterior.getDeposito());
			}
			//Traspaso los elementos a la unica operacion
			if(listaElementos!=null && listaElementos.size()>0){
				operacion.setListaElementos(new HashSet<OperacionElemento>());
				for(Elemento elemento: listaElementos){
					OperacionElemento oprr = new OperacionElemento();
					setDataOperacionRequerimientoReferencia(oprr, elemento, operacion);
					operacion.getListaElementos().add(oprr);
				}
				operacion.setCantidadPendientes(listaElementos.size());
			}
			salida.add(operacion);
		}
		//Sumarizo y genero una operacion por deposito
		else{
			Hashtable<Long, List<Elemento>> sumarizadoPorDeposito = new Hashtable<Long, List<Elemento>>();
			if(listaElementos!=null && listaElementos.size()>0){
				for(Elemento elemento:listaElementos){
					Deposito depositoBuscar = null;
					if(elemento.getDepositoActual()!=null || 
									(elemento.getContenedor()!= null && elemento.getContenedor().getDepositoActual()!=null)){
						if(elemento!=null && elemento.getDepositoActual()!=null)
							depositoBuscar = elemento.getDepositoActual();
						else{
							if(elemento.getContenedor().getDepositoActual()!=null)
								depositoBuscar = elemento.getContenedor().getDepositoActual();
						}
					}
					if(depositoBuscar!=null){
						if(sumarizadoPorDeposito.get(depositoBuscar.getId())==null){
							ArrayList<Elemento> lista = new ArrayList<Elemento>();
							lista.add(elemento);
							sumarizadoPorDeposito.put(depositoBuscar.getId(), lista);
						}
						else{
							ArrayList<Elemento> lista = (ArrayList<Elemento>) sumarizadoPorDeposito.get(depositoBuscar.getId());
							lista.add(elemento);
						}
					}
				}
			}
			if(sumarizadoPorDeposito.values()!=null){
				for(List<Elemento> lista:sumarizadoPorDeposito.values()){
					//Se genera una operacion por deposito
					Operacion operacion = new Operacion();
					setDataBasicoOperacion(operacion, requerimiento, tipoOperacion);
					operacion.setListaElementos(new HashSet<OperacionElemento>());
					if(lista != null){
						for(Elemento elemento:lista){
//							if(elemento!=null
//									&& elemento.getDepositoActual()!=null)
//								operacion.setDeposito(elemento.getDepositoActual());
//							else
//								operacion.setDeposito(operacionAnterior.getDeposito());
							
							if(elemento!=null 
									&& (elemento.getDepositoActual()!=null || 
											(elemento.getContenedor()!= null && elemento.getContenedor().getDepositoActual()!=null))){
								if(elemento!=null && elemento.getDepositoActual()!=null)
									operacion.setDeposito(elemento.getDepositoActual());
								else{
									if(elemento.getContenedor().getDepositoActual()!=null)
										operacion.setDeposito(elemento.getContenedor().getDepositoActual());
								}
							}
							else
								operacion.setDeposito(operacionAnterior.getDeposito());
							
							OperacionElemento oprr = new OperacionElemento();
							setDataOperacionRequerimientoReferencia(oprr, elemento, operacion);
							operacion.getListaElementos().add(oprr);
						}
						operacion.setCantidadPendientes(lista.size());
					}
					salida.add(operacion);
				}
			}
		}
		return salida;
	}
	
	private List<Operacion> crearOperacionesSinElementos (Operacion operacionAnterior, Requerimiento requerimiento, TipoOperacion tipoOperacion){
		ArrayList<Operacion> salida = new ArrayList<Operacion>();
		//Se genera una sola operacion 
		Operacion operacion = new Operacion();
		setDataBasicoOperacion(operacion, requerimiento, tipoOperacion);
		operacion.setDeposito(operacionAnterior.getDeposito());
		operacion.setCantidadPendientes(0);
		operacion.setObservaciones(operacionAnterior.getObservaciones());
		salida.add(operacion);
		
		return salida;
	}
	
	private void setDataOperacionRequerimientoReferencia(OperacionElemento op, Elemento elemento, Operacion operacion){
		op.setOperacion(operacion);
		op.setElemento(elemento);
		op.setEstado(ESTADO_OPERACION_ELEMENTO_PENDIENTE);
	}
	
	private void setDataBasicoOperacion(Operacion operacion, Requerimiento requerimiento, TipoOperacion tipoOperacion){
		operacion.setClienteAsp(requerimiento.getClienteAsp());
		operacion.setClienteEmp(requerimiento.getClienteEmp());
		operacion.setEstado(ESTADO_OPERACION_PENDIENTE);
		operacion.setFechaAlta(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		operacion.setHoraAlta(sdf.format(new Date()));
		operacion.setFechaEntrega(requerimiento.getFechaEntrega());
		operacion.setHoraEntrega(requerimiento.getHoraEntrega());
		operacion.setRequerimiento(requerimiento);
		operacion.setTipoOperacion(tipoOperacion);
		operacion.setUsuario(obtenerUser());
		operacion.setObservaciones(requerimiento.getObservaciones());
	}
	
	private boolean verificarDistintoDeposito(List<Elemento> listaElementos){
		Deposito deposito = null;
		boolean banderaDepositoDiferente = false;
		for(Elemento elemento:listaElementos){
			if(deposito==null){
				if(elemento!=null 
						&& (elemento.getDepositoActual()!=null || 
								(elemento.getContenedor()!= null && elemento.getContenedor().getDepositoActual()!=null))){
					if(elemento.getDepositoActual()!=null)
						deposito = elemento.getDepositoActual();
					else{
						if(elemento.getContenedor().getDepositoActual()!=null)
							deposito = elemento.getContenedor().getDepositoActual();
					}
				}
			}
			else{
				Deposito depositoComparar = null;
				if(elemento!=null 
						&& (elemento.getDepositoActual()!=null || 
								(elemento.getContenedor()!= null && elemento.getContenedor().getDepositoActual()!=null))){
					if(elemento!=null && elemento.getDepositoActual()!=null)
						depositoComparar = elemento.getDepositoActual();
					else{
						if(elemento.getContenedor().getDepositoActual()!=null)
							depositoComparar = elemento.getContenedor().getDepositoActual();
					}
				}
				if(depositoComparar!=null){
					if(deposito.getId().longValue() != depositoComparar.getId().longValue()){
						banderaDepositoDiferente = true;
						break;
					}
				}
					
			}
		}
		return banderaDepositoDiferente;
	}
	
	@SuppressWarnings({ "unchecked" })
	private void contarElementosProcesados(Operacion operacion, HttpSession session, Map<String,Object> atributos){
		ArrayList<OperacionElemento> lista = (ArrayList<OperacionElemento>) session.getAttribute("operacionReferenciasSession");
		if(lista!=null){
			int cantidadProcesados = 0, cantidadPendientes=0, cantidadOmitidos =0, cantidadProcesadosParaTraspaso =0;
			boolean traspaso = false;
			if(operacion!=null && operacion.getTipoOperacion()!=null && operacion.getTipoOperacion().getGeneraOperacionAlCerrarse().booleanValue())
				traspaso = true;
			for(OperacionElemento operacionElemento:lista){
				if(operacionElemento.getEstado()!=null){
					if(operacionElemento.getEstado().equals(ESTADO_OPERACION_ELEMENTO_PROCESADO))
						cantidadProcesados++;
					if(operacionElemento.getEstado().equals(ESTADO_OPERACION_ELEMENTO_PROCESADO) && !operacionElemento.isTraspasado() && traspaso)
						cantidadProcesadosParaTraspaso++;
					if(operacionElemento.getEstado().equals(ESTADO_OPERACION_ELEMENTO_OMITIDO))
						cantidadOmitidos++;
					if(operacionElemento.getEstado().equals(ESTADO_OPERACION_ELEMENTO_PENDIENTE))
						cantidadPendientes++;
				}
			}
			boolean finalizarOK = false, finalizarError = false, traspasar = false, procesando = false;
			if(cantidadProcesados == lista.size())
				finalizarOK = true;
			if(!finalizarOK && (cantidadProcesados + cantidadOmitidos) == lista.size())
				finalizarError = true;
			if(cantidadProcesadosParaTraspaso > 0)
				traspasar = true;
			if(!traspasar && !finalizarOK && !finalizarError && cantidadProcesados > 0)
				procesando = true;
			operacion.setCantidadProcesados(cantidadProcesados);
			operacion.setCantidadProcesadosParaTraspaso(cantidadProcesadosParaTraspaso);
			operacion.setCantidadPendientes(cantidadPendientes);
			operacion.setCantidadOmitidos(cantidadOmitidos);
			operacion.setFinalizarOK(finalizarOK);
			operacion.setFinalizarError(finalizarError);
			operacion.setTraspasar(traspasar);
			
			atributos.put("finalizarOK", finalizarOK);
			atributos.put("finalizarError", finalizarError);
			atributos.put("traspasar", traspasar);
			atributos.put("procesando", procesando);
		}
	}
	
	private ConceptoOperacionCliente calcularConceptoOperacionCliente(List<OperacionElemento> lista, Operacion operacion, ConceptoFacturable conceptoFacturable, String tipoCalculo, Long cantidadTipoCalculo){
		if(lista == null || lista.size()==0)
			return null;
		if(operacion!=null){
			if(operacion.getTipoOperacion()!=null && conceptoFacturable!=null 
					&& operacion.getRequerimiento() != null && operacion.getRequerimiento().getListaPrecios() != null){
				Requerimiento requerimiento = operacion.getRequerimiento();
				Impuesto impuesto = conceptoFacturable.getImpuesto();
				ListaPrecios listaPrecios = operacion.getRequerimiento().getListaPrecios();
				ArrayList<ListaPreciosDetalle> listaPreciosDetalles = (ArrayList<ListaPreciosDetalle>) listaPreciosService.listarDetallesPorListaPreciosConceptoFacturable(listaPrecios, conceptoFacturable);
				Long cantidad = new Long(1); //Ponemos en el caso de que el tipo de calculo sea unico
				if(tipoCalculo != null && tipoCalculo.equals("Manual"))
					cantidad = cantidadTipoCalculo;
				if(tipoCalculo != null && tipoCalculo.equals("Automatíco"))
					cantidad = new Long(lista.size());
				if(cantidad==null)
					cantidad = new Long(1);
				
				ConceptoOperacionCliente conceptoOperacionCliente = new ConceptoOperacionCliente();
				conceptoOperacionCliente.setCantidad(cantidad);
				conceptoOperacionCliente.setClienteAsp(obtenerClienteAsp());
				conceptoOperacionCliente.setClienteEmp(operacion.getClienteEmp());
				conceptoOperacionCliente.setConceptoFacturable(conceptoFacturable);
				conceptoOperacionCliente.setDescripcion(conceptoFacturable.getDescripcion());
				conceptoOperacionCliente.setCosto(conceptoFacturable.getCosto());
				conceptoOperacionCliente.setEstado("Pendiente");
				conceptoOperacionCliente.setListaPrecios(listaPrecios);
				conceptoOperacionCliente.setOperacion(operacion);
				conceptoOperacionCliente.setPrecioBase(conceptoFacturable.getPrecioBase());
				conceptoOperacionCliente.setRequerimiento(requerimiento);
				conceptoOperacionCliente.setTipoConcepto("Automatico");
				conceptoOperacionCliente.setUsuario(obtenerUser());
				conceptoOperacionCliente.setFechaAlta(new Date());
				conceptoOperacionCliente.setEmpresa(obtenerEmpresaDefault());
				conceptoOperacionCliente.setSucursal(obtenerSucursalUser());
				conceptoOperacionCliente.setAsignado(false);
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				conceptoOperacionCliente.setHoraAlta(sdf.format(new Date()));
				
				
//				//CALCULOS - METODO: EL PRECIO TIENE IMPUESTOS
//				BigDecimal finalUnitario = new BigDecimal(0.0);
//				if(listaPreciosDetalles!=null && listaPreciosDetalles.size()>0){
//					ListaPreciosDetalle listaPreciosDetalle = listaPreciosDetalles.get(0);
//					if(listaPreciosDetalle.getTipoVariacion() != null && listaPreciosDetalle.getValor()!=null && conceptoFacturable!=null )
//						finalUnitario = listaPreciosDetalle.getTipoVariacion().calcularMonto(conceptoOperacionCliente.getPrecioBase(), listaPreciosDetalle.getValor());
//						//finalUnitario = conceptoOperacionCliente.getPrecioBase();
//				}
//				conceptoOperacionCliente.setFinalUnitario(finalUnitario);
//				conceptoOperacionCliente.setFinalTotal(finalUnitario.multiply(new BigDecimal(cantidad)));
//				if(impuesto!=null){
//					conceptoOperacionCliente.setNetoUnitario(finalUnitario.divide(((impuesto.getAlicuota().divide(new BigDecimal(100))).add(new BigDecimal(1))), 4, RoundingMode.HALF_UP)); // finalUnitario / (1+(alicuota/100))
//					conceptoOperacionCliente.setNetoTotal(conceptoOperacionCliente.getNetoUnitario().multiply(new BigDecimal(cantidad)));
//					conceptoOperacionCliente.setImpuestos((finalUnitario.subtract(conceptoOperacionCliente.getNetoUnitario()).multiply(BigDecimal.valueOf(cantidad))));
//				}
				
				//////////////////////////////////////////////////////////////////////////////////
				////////////////METODO: EL PRECIO NO TIENE IMPUESTOS
				BigDecimal uno = BigDecimal.valueOf(1L);
				BigDecimal cien = BigDecimal.valueOf(100L);
				BigDecimal valor = listaPrecios.getValor();
				BigDecimal variacionPrecio = uno.add(valor.divide(cien));
				BigDecimal netoUnitario  = variacionPrecio.multiply(conceptoFacturable.getPrecioBase());
				conceptoOperacionCliente.setNetoUnitario(netoUnitario);
				conceptoOperacionCliente.setNetoTotal(netoUnitario.multiply(new BigDecimal(cantidad)));
				
				if (impuesto != null && impuesto.getAlicuota() != null) {
					
					conceptoOperacionCliente.setFinalUnitario(netoUnitario.multiply(((impuesto.getAlicuota().divide(new BigDecimal(100))).add(new BigDecimal(1)))));
					conceptoOperacionCliente.setFinalTotal(conceptoOperacionCliente.getFinalUnitario().multiply(new BigDecimal(cantidad)));
					conceptoOperacionCliente.setImpuestos((conceptoOperacionCliente.getFinalUnitario().subtract(netoUnitario).multiply(BigDecimal.valueOf(cantidad))));
					
				}
				
				return conceptoOperacionCliente;
			}
		}
		return null;
	}
	
	private void calcularConceptosProvinientesLectura(List<OperacionElemento> lista, Operacion operacion, String tipoCalculo, Long cantidadTipoCalculo, List<ConceptoOperacionCliente> conceptosVentas, List<Stock> stocks){
		if(lista == null || lista.size()==0)
			return;
		if(operacion!=null){
			//Recorro los elementos y agrupo por concepto de venta y stock
			Hashtable<Long, List<OperacionElemento>> hashSumarizadoConceptoVenta = new Hashtable<Long, List<OperacionElemento>>();
			Hashtable<Long, List<OperacionElemento>> hashSumarizadoConceptoStock = new Hashtable<Long, List<OperacionElemento>>();
			for(OperacionElemento operacionElemento:lista){
				if(operacionElemento.getElemento()!=null && operacionElemento.getElemento().getTipoElemento()!=null){
					// conceptoVenta
					if(operacionElemento.getElemento().getTipoElemento().getGeneraConceptoVenta()!=null && operacionElemento.getElemento().getTipoElemento().getConceptoVenta()!=null){
						if(operacionElemento.getElemento().getTipoElemento().getGeneraConceptoVenta().booleanValue()){
							//Busco en el hash por id del conceptoVenta, sino existe creo una lista
							//Solo se consideran los Conceptos Automaticos
							//TODO Revisar si pueden ser Manuales
							if(operacionElemento.getElemento().getTipoElemento().getConceptoVenta().getTipoCalculo().equals("Automatíco")){
								Long idConcepto = operacionElemento.getElemento().getTipoElemento().getConceptoVenta().getId();
								ArrayList<OperacionElemento> listaVenta = (ArrayList<OperacionElemento>) hashSumarizadoConceptoVenta.get(idConcepto);
								operacionElemento.setConceptoVenta(operacionElemento.getElemento().getTipoElemento().getConceptoVenta());
								if(listaVenta==null){
									listaVenta = new ArrayList<OperacionElemento>();
									listaVenta.add(operacionElemento);
									hashSumarizadoConceptoVenta.put(idConcepto, listaVenta);
								}
								else{
									listaVenta.add(operacionElemento);
								}
							}
						}
					}
					// conceptoStock
					if(operacionElemento.getElemento().getTipoElemento().getDescuentaStock()!=null && operacionElemento.getElemento().getTipoElemento().getConceptoStock()!=null){
						if(operacionElemento.getElemento().getTipoElemento().getDescuentaStock().booleanValue()){
							//Solo se consideran los Conceptos Automaticos
							//TODO Revisar si pueden ser Manuales
							if(operacionElemento.getElemento().getTipoElemento().getConceptoStock().getTipoCalculo().equals("Automatíco")){
								//Busco en el hash por id del conceptoStock, sino existe creo una lista
								Long idConcepto = operacionElemento.getElemento().getTipoElemento().getConceptoStock().getId();
								ArrayList<OperacionElemento> listaStock = (ArrayList<OperacionElemento>) hashSumarizadoConceptoStock.get(idConcepto);
								operacionElemento.setConceptoStock(operacionElemento.getElemento().getTipoElemento().getConceptoStock());
								if(listaStock==null){
									listaStock = new ArrayList<OperacionElemento>();
									listaStock.add(operacionElemento);
									hashSumarizadoConceptoStock.put(idConcepto, listaStock);
								}
								else{
									listaStock.add(operacionElemento);
								}
							}
						}
					}
				}
			}
			//Recorro los hash
			
			//ConceptoVenta 
			//Solo si tienen en cuenta los conceptos automaticos
			if(hashSumarizadoConceptoVenta.values()!=null){
				for(List<OperacionElemento> listaElementos:hashSumarizadoConceptoVenta.values()){
					if(listaElementos!=null && listaElementos.size()>0){
						OperacionElemento op = listaElementos.get(0);
						ConceptoFacturable conceptoFacturable = op.getConceptoVenta();
						ConceptoOperacionCliente conceptoOperacionCliente = calcularConceptoOperacionCliente(listaElementos, operacion, conceptoFacturable, conceptoFacturable.getTipoCalculo(), cantidadTipoCalculo);
						if(conceptoOperacionCliente!=null)
							conceptosVentas.add(conceptoOperacionCliente);
					}
				}
			}
			
			//ConceptoStock
			//Solo si tienen en cuenta los conceptos automaticos
			if(hashSumarizadoConceptoStock.values()!=null){
				for(List<OperacionElemento> listaElementos:hashSumarizadoConceptoStock.values()){
					if(listaElementos!=null && listaElementos.size()>0){
						OperacionElemento op = listaElementos.get(0);
						ConceptoFacturable conceptoFacturable = op.getConceptoStock();
						Stock stock = calcularStock(listaElementos, operacion, conceptoFacturable, conceptoFacturable.getTipoCalculo(), cantidadTipoCalculo);
						if(stock!=null)
							stocks.add(stock);
					}
				}
			}
			
			
		}
	}
	
	private Stock calcularStock(List<OperacionElemento> lista, Operacion operacion, ConceptoFacturable conceptoFacturable, String tipoCalculo, Long cantidadTipoCalculo){
		if(lista == null || lista.size()==0)
			return null;
		if(operacion!=null){
			if(operacion.getTipoOperacion()!=null && conceptoFacturable!=null){
				
				Long cantidad = new Long(1); //Ponemos en el caso de que el tipo de calculo sea unico
				if(tipoCalculo != null && tipoCalculo.equals("Manual"))
					cantidad = cantidadTipoCalculo;
				if(tipoCalculo != null && tipoCalculo.equals("Automático"))
					cantidad = new Long(lista.size());
				if(cantidad==null)
					cantidad = new Long(1);
				int cant = cantidad.intValue() * (-1);
				Stock stock = new Stock();
				stock.setCantidad(cant);
				stock.setFecha(new Date());
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				stock.setHora(sdf.format(new Date()));
				//stock.setNota("Generado por Procesamiento de Elementos de Operación");
				stock.setNota("Por venta a "+operacion.getClienteEmp().getRazonSocialONombreYApellido()
						+ " - "
						+ operacion.getRequerimiento().getSerie().getCodigo() + ": "
						+ operacion.getRequerimiento().getPrefijoStr()+ " - " 
						+ operacion.getRequerimiento().getNumeroStr());
				stock.setTipoMovimiento("Egreso");
				stock.setClienteAsp(obtenerClienteAsp());
				stock.setConcepto(conceptoFacturable);
				stock.setDeposito(operacion.getDeposito());
				
				return stock;
			}
		}
		return null;
	}
	
	private String ultimoNumeroSerie(Serie serie){
		String proximoNumero = null;
		
		if(serie.getUltNroImpreso() != null && serie.getUltNroImpreso().length()>0 && Long.valueOf(serie.getUltNroImpreso()) != 0){
			Long proximoCodigoLong = Long.valueOf(serie.getUltNroImpreso()) + 1L;
			StringBuffer aux = new StringBuffer("");
			proximoNumero = String.valueOf(proximoCodigoLong);
			for (int i = 0; i<(serie.getUltNroImpreso().length() - proximoNumero.length()) ; i++){
				aux.append("0");
			}
			aux.append(proximoNumero);
			proximoNumero = aux.toString();
		}
		else{
			proximoNumero = "00000001";
    	}
		
		return proximoNumero;
	}
	
}
