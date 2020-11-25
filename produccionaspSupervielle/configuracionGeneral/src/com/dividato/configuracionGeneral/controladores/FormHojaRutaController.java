/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;
import static com.security.utils.Constantes.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

import org.apache.log4j.Logger;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.objectForms.HojaRutaBusquedaForm;
import com.dividato.configuracionGeneral.validadores.HojaRutaValidator;
import com.dividato.configuracionGeneral.validadores.ReferenciaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.HojaRutaOperacionElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.HojaRutaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.accesoDatos.jerarquias.interfaz.OperacionElementoService;
import com.security.accesoDatos.jerarquias.interfaz.OperacionService;
import com.security.accesoDatos.jerarquias.interfaz.RequerimientoService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.HojaRuta;
import com.security.modelo.configuraciongeneral.HojaRutaOperacionElemento;
import com.security.modelo.configuraciongeneral.Impuesto;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.Stock;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la administración
 * de la ClasificacionDocumental.
 *
 * @author Luis Manzanelli
 */

@Controller
@RequestMapping(
		value=
			{	
				"/consultaHojaRuta.html",
				"/borrarFiltrosHojaRuta.html",
				"/eliminarHojaRuta.html",
				"/agregarOperacionHojaRuta.html",
				"/eliminarOperacionHojaRuta.html",
				"/precargaFormularioHojaRuta.html",
				"/guardarActualizarHojaRuta.html",
				"/marcarElemento.html",
				"/imprimirHojaRuta.html",
				"/chequearHojaRuta.html",
				"/guardarChequearHojaRuta.html",
			}
		)
public class FormHojaRutaController {
	private static final String OPERACIONES_SELECCIONADAS = "operacionesSeleccionadasHojaRuta";
	private static final String ELEMENTOS_SELECCIONADOS = "operacionesElementosSeleccionadasHojaRuta";

	private static Logger logger=Logger.getLogger(FormHojaRutaController.class);
	
	private HojaRutaService hojaRutaService;
	private ClasificacionDocumentalService clasificacionDocumentalService;
	private ClienteEmpService clienteEmpService;
	private SucursalService sucursalService;
	private EmpresaService empresaService;
	private ElementoService elementoService;
	private HojaRutaValidator hojaRutaValidator;
	private ReferenciaValidator refValidator;
	private OperacionService operacionService;
	private SerieService serieService;
	private TransporteService transporteService;
	private HojaRutaOperacionElementoService hojaRutaOperacionElementoService;
	private OperacionElementoService operacionElementoService;
	private ListaPreciosService listaPreciosService;
	private RequerimientoService requerimientoService;
	
	@Autowired
	public void setServices(HojaRutaService HojaRutaService,
			ClasificacionDocumentalService clasificacionDocumentalService,
			SucursalService sucursalService,
			ClienteEmpService clienteEmpService,
			EmpresaService empresaService,
			ElementoService elementoService,
			OperacionService operacionService,
			SerieService serieService,
			TransporteService transporteService,
			HojaRutaOperacionElementoService hojaRutaOperacionElementoService,
			OperacionElementoService operacionElementoService,
			ListaPreciosService listaPreciosService,
			RequerimientoService requerimientoService) {
		this.hojaRutaService = HojaRutaService;
		this.clasificacionDocumentalService = clasificacionDocumentalService;
		this.sucursalService = sucursalService;
		this.clienteEmpService = clienteEmpService;
		this.empresaService = empresaService;
		this.elementoService = elementoService;
		this.operacionService = operacionService;
		this.serieService = serieService;
		this.transporteService = transporteService;
		this.hojaRutaOperacionElementoService = hojaRutaOperacionElementoService;
		this.operacionElementoService= operacionElementoService;
		this.listaPreciosService = listaPreciosService;
		this.requerimientoService = requerimientoService;
	}
	
	@Autowired
	public void setValidator(HojaRutaValidator validator,ReferenciaValidator refValidator) {
		this.hojaRutaValidator = validator;
		this.refValidator = refValidator;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		if(binder.getTarget() instanceof HojaRutaBusquedaForm){
			binder.registerCustomEditor(Date.class, "fechaDesde",new CustomDateEditor(formatoFechaFormularios, true));
			binder.registerCustomEditor(Date.class, "fechaHasta",new CustomDateEditor(formatoFechaFormularios, true));
		}else if (binder.getTarget() instanceof HojaRuta){
			hojaRutaValidator.initDataBinder(binder);
		}else if (binder.getTarget() instanceof Referencia){
			refValidator.initDataBinder(binder);
		}		
	}
	
	@RequestMapping(value="/consultaHojaRuta.html")
	public String consultaHojaRuta(Map<String,Object> atributos,
			@ModelAttribute("busquedaHojaRutaFormulario") HojaRutaBusquedaForm busquedaHojaRuta,
			HttpServletRequest request) {
		if(busquedaHojaRuta==null)
			busquedaHojaRuta=new HojaRutaBusquedaForm();
		if(busquedaHojaRuta.getIdClienteAsp()==null){
			busquedaHojaRuta.setIdClienteAsp(obtenerClienteAspUser().getId());
			busquedaHojaRuta.setCodigoEmpresa(obtenerCodigoEmpresaUser());
			busquedaHojaRuta.setCodigoSucursal(obtenerCodigoSucursalUser());
			busquedaHojaRuta.setEstado(true);
		}
			//paginacion y orden de resultados de displayTag
			busquedaHojaRuta.setTamañoPagina(20);	
			if(request != null){
				String nPaginaStr= request.getParameter((new ParamEncoder("HojaRuta").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
				if(nPaginaStr==null){
					nPaginaStr = (String)atributos.get((new ParamEncoder("HojaRuta").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
				}
				String fieldOrder = request.getParameter( new ParamEncoder("HojaRuta").encodeParameterName(TableTagParameters.PARAMETER_SORT));
				busquedaHojaRuta.setFieldOrder(fieldOrder);
				String sortOrder = request.getParameter(new ParamEncoder("HojaRuta").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
				busquedaHojaRuta.setSortOrder(sortOrder);
				Integer nPagina = 1;		
				if(nPaginaStr!=null){
					nPagina = (Integer.parseInt(nPaginaStr));
				}
				busquedaHojaRuta.setNumeroPagina(nPagina);
			}
			// -- Filtrar 
			// Serie Numero, desde y hasta ok
			// Estado 
			// Transporte - codigoTransporte
			//
			
			List<HojaRuta> hojaRuta = hojaRutaService.listarHojaRutaFiltradas(obtenerClienteAspUser(),
					busquedaHojaRuta.getCodigoEmpresa(),
					busquedaHojaRuta.getCodigoSucursal(),
					//busquedaHojaRuta.getCodigoCliente(),
					busquedaHojaRuta.getFechaDesde(),
					busquedaHojaRuta.getFechaHasta(),
					busquedaHojaRuta.getCodigoSerie(),
					busquedaHojaRuta.getSerieDesde(),
					busquedaHojaRuta.getSerieHasta(),
					busquedaHojaRuta.isEstado()?ESTADO_HOJA_RUTA_PENDIENTE:null,
					busquedaHojaRuta.getCodigoTransporte()
					);	
		
		atributos.put("hojaRuta", hojaRuta);
		atributos.put("size", hojaRuta.size());
		
		return "consultaHojaRuta";
	}
	
	@RequestMapping(value="/borrarFiltrosHojaRuta.html")
	public String borrarFiltrosHojaRuta(Map<String,Object> atributos,
			HttpServletRequest request) {
		return consultaHojaRuta(atributos, null,request);
	}
	
	@RequestMapping(value="/eliminarHojaRuta.html")
	public String eliminarHojaRuta(Map<String,Object> atributos,
			@RequestParam(value="id",required=true) Long id,
			HttpServletRequest request)
	{
		hojaRutaService.eliminar(id);
		
		return consultaHojaRuta(atributos,null,request);
	}
	
	
	@RequestMapping(value="/marcarElemento.html")
	public String marcarElemento(Map<String,Object> atributos,
			HttpSession session,
			@RequestParam(value="idElemento",required=true) String idElemento,
			@RequestParam(value="checkeado",required=true) boolean checkeado,
			HttpServletRequest request)
	{
		if(!idElemento.equals("selectAll")){
			List<HojaRutaOperacionElemento> attribute = (List<HojaRutaOperacionElemento>) session.getAttribute(ELEMENTOS_SELECCIONADOS);
			for (HojaRutaOperacionElemento e: attribute){
				if(e.getObj_hash().trim().equals(idElemento.trim())){
					if(checkeado==true){
						e.setEstado(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_SELECCIONADO);
					}else{
						e.setEstado(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_NO_SELECCIONADO);
					}
					break;
				}
			}
			try {
				request.getInputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else{
			List<HojaRutaOperacionElemento> attribute = (List<HojaRutaOperacionElemento>) session.getAttribute(ELEMENTOS_SELECCIONADOS);
			for (HojaRutaOperacionElemento e: attribute){
				e.setEstado(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_SELECCIONADO);
			}
		}
		return null;
	}
	
	
	@RequestMapping(value="/agregarOperacionHojaRuta.html")
	public String agregarOperacionHojaRuta(Map<String,Object> atributos,
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="idOperacion",required=false) String idOperaciones,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="codigoSerie",required=false) String codigoSerie,
			@RequestParam(value="numeroStr",required=false) String numero,
			@RequestParam(value="codigoTransporte",required=false) Integer codigoTransporte,
			@RequestParam(value="observacion",required=false) String observacion,
			@ModelAttribute("hojaRuta") HojaRuta hojaRuta,
			HttpServletRequest request, HttpSession session)
	{
		atributos.put("accion", accion);
		// Si es modificacion, validar que no se repitan las operaciones y elementos.
		hojaRuta.setClienteAsp(obtenerClienteAspUser());
		hojaRuta.setEmpresa(empresaService.getByCodigo(obtenerCodigoEmpresaUser(), hojaRuta.getClienteAsp()));
		hojaRuta.setSucursal(sucursalService.getByCodigo(obtenerCodigoSucursalUser(), hojaRuta.getClienteAsp()));
		hojaRuta.setSerie(serieService.obtenerPorCodigo(codigoSerie, hojaRuta.getClienteAsp()));
		if(!numero.equals("")){
			hojaRuta.setNumero(BigInteger.valueOf(Long.valueOf(numero)));
		}
		
		hojaRuta.setEstado(ESTADO_HOJA_RUTA_PENDIENTE);
		hojaRuta.setObservacion(observacion);
		if(codigoTransporte!=null){
			hojaRuta.setTransporte(transporteService.obtenerPorCodigo(codigoTransporte, hojaRuta.getClienteAsp()));
		}
		
		List<Long> listaNumeros = new ArrayList<Long>();
		if(idOperaciones != null && idOperaciones.length()> 0){
		
			String[] listaRemitosSeleccionados = idOperaciones.split("\\,");
			
			for(int i = 0; i<listaRemitosSeleccionados.length;i++)
			{
				listaNumeros.add(Long.valueOf(listaRemitosSeleccionados[i]));
			}
		}
		
		List<Operacion> operaciones = (List<Operacion>) session.getAttribute(OPERACIONES_SELECCIONADAS);
		
		for(Long idOperacion:listaNumeros){
			// Obtengo la operacion
			Operacion operacion = operacionService.obtenerPorId(idOperacion);
			
			
			List<HojaRutaOperacionElemento> operacionElementos = (List<HojaRutaOperacionElemento>) session.getAttribute(ELEMENTOS_SELECCIONADOS);
			
			// Inicio - Valido que la operacion no exista.
			boolean noExisteOperacion = true;
			for (Operacion op: operaciones){
				if(op.getId().equals(operacion.getId())){
					noExisteOperacion = false;
					break;
				}
			}
			if(noExisteOperacion){
				operacion.setEstado(ESTADO_OPERACION_EN_PROCESO);
				operaciones.add(operacion);
			}
			// Fin - Valido que la operacion no exista.
			Integer n=0;
			
			for(OperacionElemento operacionElemento:operacion.getListaElementos()){
				HojaRutaOperacionElemento hojaRutaOpElemento = new HojaRutaOperacionElemento(operacionElemento);
				boolean seleccionable = hojaRutaOperacionElementoService.chequearOperacionElementoPendiente(hojaRuta.getId(),operacionElemento.getId());
				hojaRutaOpElemento.setSeleccionable(seleccionable);
				operacionElementos.add(hojaRutaOpElemento);
			}
			for (HojaRutaOperacionElemento hojaRutaOperacionElemento :operacionElementos){
	//			if(hojaRutaOperacionElemento.getEstado().equals("seleccionado")){
	//				n++;
	//			}
				if(hojaRutaOperacionElemento.getSeleccionable()){
					hojaRutaOperacionElemento.setEstado(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_SELECCIONADO);
					n++;
				}
			}
			if(operacion.getListaElementos().size()==0){
				OperacionElemento operacionElemento = new OperacionElemento();
				operacionElemento.setOperacion(operacion);
				HojaRutaOperacionElemento hojaRutaOpElemento = new HojaRutaOperacionElemento(operacionElemento);
				hojaRutaOpElemento.setSeleccionable(true);
				operacionElementos.add(hojaRutaOpElemento);
				n++;
			}
		    
			// Busco la lista con operaciones en estado pendiente para listarlas de nuevo, pero saco las actualmente seleccionadas.
			List<Operacion> listaOperacion = operacionService.listarOperacionEstado(ESTADO_OPERACION_PENDIENTE, obtenerClienteAspUser());
			for(Operacion seleccionada:operaciones){
				for (int i=0;i<listaOperacion.size();i++){
					Integer cantidadElementos = operacionElementoService.cantidadOperacionElementoPorOperacion(listaOperacion.get(i), listaOperacion.get(i).getClienteAsp());
					listaOperacion.get(i).setCantidadElementos(cantidadElementos);
					if(listaOperacion.get(i).getId().equals(seleccionada.getId())){
						listaOperacion.remove(i);
						//break;
					}
				}
			}
		
			atributos.put("elementoSelec", n);
			atributos.put("hojaRuta", hojaRuta);
			atributos.put("listaOperacion", listaOperacion);
			atributos.put("direccionesSelec", calcularTotalDirecciones(operaciones));
			
		}

		atributos.put("totalRequerimientos", calcularTotalRequerimientos(operaciones));
		atributos.remove("avisos");		
		return "formularioHojaRuta";
	}
	
	@RequestMapping(value="/eliminarOperacionHojaRuta.html")
	public String eliminarOperacionHojaRuta(Map<String,Object> atributos,
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="idOperacionPlanificada",required=false) Long idOperacionPlanificada,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="codigoSerie",required=false) String codigoSerie,
			@RequestParam(value="numeroStr",required=false) String numero,
			@RequestParam(value="codigoTransporte",required=false) Integer codigoTransporte,
			@RequestParam(value="observacion",required=false) String observacion,
			@ModelAttribute("hojaRuta") HojaRuta hojaRuta,
			
			
			HttpServletRequest request, HttpSession session)
	{
		atributos.put("accion", accion);
		hojaRuta.setClienteAsp(obtenerClienteAspUser());
		hojaRuta.setEmpresa(empresaService.getByCodigo(obtenerCodigoEmpresaUser(), hojaRuta.getClienteAsp()));
		hojaRuta.setSucursal(sucursalService.getByCodigo(obtenerCodigoSucursalUser(), hojaRuta.getClienteAsp()));
		hojaRuta.setSerie(serieService.obtenerPorCodigo(codigoSerie, hojaRuta.getClienteAsp()));
		if(!numero.equals("")){
			hojaRuta.setNumero(BigInteger.valueOf(Long.valueOf(numero)));
		}
		hojaRuta.setEstado(ESTADO_HOJA_RUTA_PENDIENTE);
		hojaRuta.setObservacion(observacion);
		if(codigoTransporte!=null){
			hojaRuta.setTransporte(transporteService.obtenerPorCodigo(codigoTransporte, hojaRuta.getClienteAsp()));
		}
		List<Operacion> operaciones = (List<Operacion>) session.getAttribute(OPERACIONES_SELECCIONADAS);
		List<HojaRutaOperacionElemento> operacionElementos = (List<HojaRutaOperacionElemento>) session.getAttribute(ELEMENTOS_SELECCIONADOS);
		
		Operacion operacion = operacionService.obtenerPorId(idOperacionPlanificada);
		//elimino la operación de las seleccionadas y los elementos
		for (int i=0;i<operaciones.size();i++){
			if(operaciones.get(i).getId().equals(operacion.getId())){
				operaciones.remove(i);
				break;
			}
		}
		
		for(OperacionElemento eliminar : operacion.getListaElementos()){
			for(HojaRutaOperacionElemento operacionElemento:operacionElementos){
				if(operacionElemento.getOperacionElemento().getId().equals(eliminar.getId())){
					operacionElementos.remove(operacionElemento);
					break;
				}
			}
		}
		
		// Busco la lista con operaciones en estado pendiente para listarlas de nuevo, pero saco las actualmente seleccionadas.
		List<Operacion> listaOperacion = operacionService.listarOperacionEstado(ESTADO_OPERACION_PENDIENTE, obtenerClienteAspUser());
		for(Operacion seleccionada:operaciones){
			for (int i=0;i<listaOperacion.size();i++){
				if(listaOperacion.get(i).getId().equals(seleccionada.getId())){
					listaOperacion.remove(i);
					break;
				}
			}
		}
		Integer n=0;
		for (HojaRutaOperacionElemento hojaRutaOperacionElemento :operacionElementos){
			if(hojaRutaOperacionElemento.getEstado().equals(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_SELECCIONADO)){
				n++;
			}
		}
		atributos.put("elementoSelec", n);
		atributos.put("hojaRuta", hojaRuta);
		atributos.put("listaOperacion", listaOperacion);
		atributos.put("direccionesSelec", calcularTotalDirecciones(operaciones));
		
		atributos.remove("avisos");		
		return "formularioHojaRuta";
	}
	
	
	
	
	@RequestMapping(value="/precargaFormularioHojaRuta.html")
	public String precargaFormularioHojaRuta(Map<String,Object> atributos,
			HttpSession session,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) Long idHojaRuta) {
		
		HojaRuta hojaRuta = null;
		Integer elementoSelec = 0;
		if(accion==null) accion="NUEVO";
		List<Operacion> listaOperacion = operacionService.listarOperacionEstado(ESTADO_OPERACION_PENDIENTE, obtenerClienteAspUser());
		if(accion.equals("NUEVO")){
			hojaRuta = new HojaRuta();
			hojaRuta.setClienteAsp(obtenerClienteAspUser());
			hojaRuta.setEmpresa(empresaService.getByCodigo(obtenerCodigoEmpresaUser(), hojaRuta.getClienteAsp()));
			hojaRuta.setSucursal(sucursalService.getByCodigo(obtenerCodigoSucursalUser(), hojaRuta.getClienteAsp()));
			hojaRuta.setFecha(new Date());
			hojaRuta.setHora(new SimpleDateFormat("HH:mm").format(hojaRuta.getFecha()));
			hojaRuta.setFechaSalida(new Date());
			session.setAttribute(OPERACIONES_SELECCIONADAS,new ArrayList<Operacion>());
			session.setAttribute(ELEMENTOS_SELECCIONADOS,new ArrayList<HojaRutaOperacionElemento>());
			if(listaOperacion!=null && listaOperacion.size()>0){
				for (Operacion op: listaOperacion){
					Integer cantidadElementos = operacionElementoService.cantidadOperacionElementoPorOperacion(op, op.getClienteAsp());
					op.setCantidadElementos(cantidadElementos);
				}
			}
			atributos.put("listaOperacion", listaOperacion);
			atributos.put("direccionesSelec", 0);
			atributos.put("totalRequerimientos", 0);
		}else {
			hojaRuta=hojaRutaService.obtenerPorId(idHojaRuta);
			
			List<Operacion> operaciones = new ArrayList<Operacion>();
			for(HojaRutaOperacionElemento opElemento : hojaRuta.getOperacionesElementos()){
				if(!opElemento.getEstado().equals(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_SELECCIONADO)){
					boolean seleccionable = hojaRutaOperacionElementoService.chequearOperacionElementoPendiente(hojaRuta.getId(),opElemento.getOperacionElemento().getId());
					opElemento.setSeleccionable(seleccionable);
				}
				if(!operaciones.contains(opElemento.getOperacionElemento().getOperacion()))
					operaciones.add(opElemento.getOperacionElemento().getOperacion());
			}
			session.setAttribute(ELEMENTOS_SELECCIONADOS,new ArrayList<HojaRutaOperacionElemento>(hojaRuta.getOperacionesElementos()));
			session.setAttribute(OPERACIONES_SELECCIONADAS,operaciones);
			List<Operacion> listaOperacionResult = new ArrayList<Operacion>();
			// Inicio - Si la operacion esta en la Grilla planificado, no se agrega a la Grilla Pendiente
			for (Operacion op: listaOperacion){
				boolean noExisteOp=true;
				for (Operacion opx:operaciones){
					if(op.getId().equals(opx.getId())){
						noExisteOp = false;
						break;
					}
				}
				if(noExisteOp==true){
					Integer cantidadElementos = operacionElementoService.cantidadOperacionElementoPorOperacion(op, op.getClienteAsp());
					op.setCantidadElementos(cantidadElementos);
					listaOperacionResult.add(op);
				}
			}
			atributos.put("listaOperacion", listaOperacionResult);
			atributos.put("direccionesSelec", calcularTotalDirecciones(operaciones));
			atributos.put("totalRequerimientos", calcularTotalRequerimientos(operaciones));
			// Fin - Si la operacion esta en la Grilla planificado, no se agrega a la Grilla Pendiente
		}
		atributos.put("elementoSelec", calcularTotalElementos(hojaRuta));
		atributos.put("accion", accion);
		atributos.put("hojaRuta", hojaRuta);
		return "formularioHojaRuta";
	}
	private Integer calcularTotalDirecciones(Collection<Operacion> operaciones) {
		List<Long> direcciones=new ArrayList<Long>();
		for(Operacion operacion : operaciones){
			ClienteDireccion dir = operacion.getRequerimiento().getDireccionDefecto();
			if(dir!=null && !direcciones.contains(dir.getId())){
				direcciones.add(dir.getId());
			}
		}
		return direcciones.size();
	}
	
	private Integer calcularTotalElementos(HojaRuta hojaRuta) {
		
		Integer contador=0;
		if(hojaRuta.getOperacionesElementos()!=null){
			for(HojaRutaOperacionElemento opElemento : hojaRuta.getOperacionesElementos()){
				if(opElemento.getEstado().equals(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_SELECCIONADO)){
					contador++;
				}
			}
		}	
		return contador;
	}
	
	private Integer calcularTotalRequerimientos(List<Operacion> operacionesPlanificadas) {
		
		List<Long> listaContados = new ArrayList<Long>();
		Integer contador = 0;
		boolean contado;
		if(operacionesPlanificadas!=null && operacionesPlanificadas.size()>0)
		{
			
			for(Operacion op: operacionesPlanificadas){
				
				contado = false;
				
				for(Long idContado: listaContados){
					if(op.getRequerimiento().getId().longValue() == idContado.longValue()){
						contado = true;
						break;
					}
				}
				
				if(contado==false){
					contador++;
					listaContados.add(op.getRequerimiento().getId());
				}
				
			}
		}
		
		return contador;
	}

	@RequestMapping(value="/guardarActualizarHojaRuta.html",method=RequestMethod.POST)
	public String guardarHojaRuta(Map<String,Object> atributos, HttpSession session,
			@RequestParam(value="accion") String accion,
			@ModelAttribute("hojaRuta") HojaRuta hojaRuta,
			BindingResult result,
			@RequestParam(value="codigoEmpresa",required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal",required=false) String codigoSucursal,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente,
			@RequestParam(value="codigoSerie",required=false) String codigoSerie,
			@RequestParam(value="numeroStr",required=false) String numero,
			@RequestParam(value="codigoTransporte",required=false) Integer codigoTransporte,
			@RequestParam(value="observacion",required=false) String observacion,
			@RequestParam(value="direccionesSelec",required=false) Integer direccionesSelec,
			@RequestParam(value="cantidadElementosSeleccionados",required=false) Integer cantidadElementosSeleccionados,
			@RequestParam(value="guardarContinuar",required=false) Boolean guardarContinuar,
			HttpServletRequest request){
		
		atributos.put("direccionesSelec", direccionesSelec);
		atributos.put("elementoSelec", cantidadElementosSeleccionados);
		atributos.put("observacion", observacion);
		
		hojaRuta.setClienteAsp(obtenerClienteAspUser());
		hojaRuta.setEmpresa(empresaService.getByCodigo(codigoEmpresa, hojaRuta.getClienteAsp()));
		hojaRuta.setSucursal(sucursalService.getByCodigo(codigoSucursal, hojaRuta.getClienteAsp()));
		hojaRuta.setClienteEmp(clienteEmpService.getByCodigo(codigoCliente));
		if(codigoSerie!=null && !codigoSerie.equals("")){
			hojaRuta.setSerie(serieService.obtenerPorCodigo(codigoSerie, hojaRuta.getClienteAsp()));
		}
		if(codigoTransporte!=null && !codigoTransporte.equals("")){
			hojaRuta.setTransporte(transporteService.obtenerPorCodigo(codigoTransporte, hojaRuta.getClienteAsp()));
		}
		if(!numero.equals("")){
			hojaRuta.setNumero(BigInteger.valueOf(Long.valueOf(numero)));
		}
		hojaRuta.setEstado(ESTADO_HOJA_RUTA_PENDIENTE);
		hojaRuta.setObservacion(observacion);
		// Tomar los elementos seleccionados
		List<HojaRutaOperacionElemento> attribute = (List<HojaRutaOperacionElemento>) session.getAttribute(ELEMENTOS_SELECCIONADOS);
		//Tomar las operaciones seleccionadas
		List<Operacion> operaciones = (List<Operacion>) session.getAttribute(OPERACIONES_SELECCIONADAS);
		
		if(!result.hasErrors()){
			hojaRutaValidator.validate(hojaRuta, result);
		}
		
		if(!result.hasErrors()){
			if(accion.equalsIgnoreCase("nuevo")){
				//Busco el ultimo numero de serie y le sumo uno
				try {
					if(numero.equals("")){
						numero=String.valueOf(new Long(hojaRuta.getSerie().getUltNroImpreso())+1);
					}
					hojaRuta.setNumero(new BigInteger(numero));
					hojaRuta.getSerie().setUltNroImpreso(numero);
					serieService.actualizarSerie(hojaRuta.getSerie());
				} catch (Exception e) {
				}
			}
			hojaRutaService.guardarActualizar(hojaRuta,operaciones);
			hojaRutaOperacionElementoService.eliminarPorHojaRuta(hojaRuta.getId());
			for(HojaRutaOperacionElemento hojaRutaOperacionElemento :attribute){
				hojaRutaOperacionElemento.setHojaRuta(hojaRuta);
				if(hojaRutaOperacionElemento.getOperacionElemento().getId()==null){
					operacionElementoService.guardar(hojaRutaOperacionElemento.getOperacionElemento());
				}
				hojaRutaOperacionElementoService.guardar(hojaRutaOperacionElemento);
			}
			
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			
			ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioHojaRuta.notificacion.HojaRutaRegistrado", Arrays.asList(new String[]{hojaRuta.getSerie().getCodigo()+" "+ hojaRuta.getSerie().getPrefijo()+"-"+hojaRuta.getNumeroStr()}));
			avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
			if(guardarContinuar!=null && guardarContinuar){
				return precargaFormularioHojaRuta(atributos,session,"MODIFICACION", hojaRuta.getId());
			}
			//hacemos el redirect
			return consultaHojaRuta(atributos, null,request);
		}else{
			atributos.put("HojaRuta", hojaRuta);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return "formularioHojaRuta";
		}
	
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de imprimir los codigos de barra de de los modulos en un reporte y subirlo al response.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaImpresionEtiqueta" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/imprimirHojaRuta.html",
			method = RequestMethod.GET
		)
	public void imprimirHojaRuta(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="id",required=true) Long id,
			HttpServletResponse response) {
		
		try{
			HojaRuta hojaRuta = hojaRutaService.obtenerPorId(id);
			String fechaHoraSalida = hojaRuta.getFechaHoraSalidaStr();
			String nroSerieHr = hojaRuta.getSerie().getCodigo()+" "+ hojaRuta.getSerie().getPrefijo()+" - " +hojaRuta.getNumeroStr();
			String responsable = hojaRuta.getTransporte().getDescripcion();
			
			JasperReport jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+"/informeHojaRuta.jrxml");
			Map<String,Object> parametros=new HashMap<String,Object>();	
			parametros.put("fechaHoraSalida", fechaHoraSalida);
			parametros.put("nroSerieHr", nroSerieHr);
			parametros.put("responsable", responsable);
			
			response.setContentType("application/pdf");
            //response.addHeader("Content-Disposition", "attachment; filename=reporte.pdf");
            
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listaOperacionesHojaRuta(hojaRuta));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,dataSource);
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de imprimir los codigos de barra de de los modulos en un reporte y subirlo al response.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaImpresionEtiqueta" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/chequearHojaRuta.html",
			method = RequestMethod.GET
		)
	public String chequearHojaRuta(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="estado",required=false) String estado,
			@RequestParam(value="id",required=false) Long idHojaRuta,
			HttpServletResponse response) {
		
		try{
			
			HojaRuta hojaRuta = hojaRutaService.obtenerPorId(idHojaRuta);
			String fechaHoraSalida = hojaRuta.getFechaHoraSalidaStr();
			String nroSerieHr = hojaRuta.getSerie().getCodigo()+" "+ hojaRuta.getSerie().getPrefijo()+" - " +hojaRuta.getNumeroStr();
			String responsable = hojaRuta.getTransporte().getDescripcion();
			List<hojaRutaReporte> listaOperaciones = listaOperacionesHojaRuta(hojaRuta);
		    // Subir a requet la colleccion con las operaciones de la hoja de ruta
			// Validar que la hoja de ruta este en estado pendiente.
			
			atributos.put("estado", estado);
			atributos.put("idHojaRuta", idHojaRuta);
			atributos.put("fechaHoraSalida", fechaHoraSalida);
			atributos.put("nroSerieHr", nroSerieHr);
			atributos.put("responsable", responsable);
			atributos.put("listaOperaciones", listaOperaciones);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "formularioHojaRutaChequearOp";
	}
	
	@RequestMapping(value="/guardarChequearHojaRuta.html",method=RequestMethod.POST)
	public String guardarChequearHojaRuta(Map<String,Object> atributos, HttpSession session,
			@RequestParam(value="operacionSeleccionada",required=false) String[] operacionSeleccionada,
			@RequestParam(value="hd_idHojaRuta",required=false) Long idHojaRuta,
			HttpServletRequest request){
			
			if(operacionSeleccionada!=null && operacionSeleccionada.length >0){
				
				Boolean commit = null;
				String mensajeUpdate = "";
				List<OperacionElemento> listOpElemento = new ArrayList<OperacionElemento>();
				boolean cierroOperacion = false;
				HojaRuta hojaRuta = hojaRutaService.obtenerPorId(idHojaRuta);
				hojaRuta.setEstado(ESTADO_HOJA_RUTA_ENTREGADO);
				hojaRutaService.guardarActualizar(hojaRuta);
			
				for (int i=0;i<operacionSeleccionada.length;i++){
					for(HojaRutaOperacionElemento opElemento : hojaRuta.getOperacionesElementos()){
						if(opElemento.getOperacionElemento().getOperacion().getId().equals(Long.valueOf(operacionSeleccionada[i])) 
								&& opElemento.getEstado().equals(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_SELECCIONADO)){
							
							opElemento.setEstado(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_PROCESADO);
							hojaRutaOperacionElementoService.actualizar(opElemento);
							
							opElemento.getOperacionElemento().setEstado(ESTADO_OPERACION_ELEMENTO_PROCESADO);
							operacionElementoService.actualizar(opElemento.getOperacionElemento());
							
							opElemento.getOperacionElemento().getOperacion().setEstado(ESTADO_OPERACION_FINALIZADO_OK);
							operacionService.actualizar(opElemento.getOperacionElemento().getOperacion());
							
	//						if(opElemento.getOperacionElemento().getElemento()!=null){
	//							opElemento.getOperacionElemento().getElemento().setEstado(ELEMENTO_ESTADO_EN_EL_CLIENTE);
	//							elementoService.actualizar(opElemento.getOperacionElemento().getElemento());
	//						}
							
							cierroOperacion = true;
						}
						listOpElemento.add(opElemento.getOperacionElemento());
					}
					// Si cambio el estado de un elemento calculo el concepto
					if(cierroOperacion==true){
						// Genero los conceptos facturables.
						Operacion operacion = operacionService.obtenerPorId(Long.valueOf(operacionSeleccionada[i]));
						String tipoCalculo = "Automatíco";
						long cantidadTipoCalculo = 1;
						ConceptoOperacionCliente conceptoOperacionCliente = calcularConceptoOperacionCliente(listOpElemento, operacion, operacion.getTipoOperacion().getConceptoFacturable(), tipoCalculo, cantidadTipoCalculo);
						//Calculo los conceptos de venta y stock para los elementos provinientes de lectura
						ArrayList<ConceptoOperacionCliente> conceptosVentas = new ArrayList<ConceptoOperacionCliente>();
						ArrayList<Stock> stocks = new ArrayList<Stock>();
						//calcularConceptosProvinientesLectura(listaElementosLectura, operacion, tipoCalculo, cantidadTipoCalculo, conceptosVentas, stocks);
						
						//Comiteo para poder actualizar el estado del requerimiento
						commit = operacionService.update(operacion,null, conceptoOperacionCliente, conceptosVentas, stocks, null);
						
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
										if(requerimiento.getEstado() != null && !requerimiento.getEstado().equalsIgnoreCase(ESTADO_OPERACION_FINALIZADO_ERROR))
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
					}
				}
				for(HojaRutaOperacionElemento opEle : hojaRuta.getOperacionesElementos()){
					if(!opEle.getEstado().equals(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_PROCESADO)){
						opEle.setEstado(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_OMITIDO);
					}
				}
				
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioHojaRutaChequearOp.notificacion.HojaRutaEntregada",  Arrays.asList(new String[]{hojaRuta.getSerie().getCodigo()+" "+ hojaRuta.getSerie().getPrefijo()+"-"+hojaRuta.getNumeroStr()}));
				avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", true);
				atributos.put("avisos", avisos);
				
			}
			
			return consultaHojaRuta(atributos, null,request);
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
//				if(tipoCalculo != null && tipoCalculo.equals("Manual"))
//					cantidad = cantidadTipoCalculo;
//				if(tipoCalculo != null && tipoCalculo.equals("Automatíco"))
//					cantidad = new Long(lista.size());
//				if(cantidad==null)
					//cantidad = new Long(1);
				
				ConceptoOperacionCliente conceptoOperacionCliente = new ConceptoOperacionCliente();
				conceptoOperacionCliente.setCantidad(cantidad);
				conceptoOperacionCliente.setClienteAsp(obtenerClienteAspUser());
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
				
//				//CALCULOS - METODOS: EL PRECIO TIENE IMPUESTOS
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
	
	private List<hojaRutaReporte> listaOperacionesHojaRuta(HojaRuta hojaRuta){
		ArrayList operaciones = new ArrayList();
		List<hojaRutaReporte> listOperaciones = new ArrayList<FormHojaRutaController.hojaRutaReporte>();
		hojaRutaReporte hojaRutaReporte = null;
		for(HojaRutaOperacionElemento opElemento : hojaRuta.getOperacionesElementos()){
			if(!operaciones.contains(opElemento.getOperacionElemento().getOperacion())){
				Operacion op = opElemento.getOperacionElemento().getOperacion();
				operaciones.add(op);
				hojaRutaReporte = new hojaRutaReporte();
				hojaRutaReporte.setIdHojaRutaOpElemnt(op.getId());
				hojaRutaReporte.setSerie(op.getRequerimiento().getSerie().getCodigo()+" \n "+op.getRequerimiento().getSerie().getPrefijo()+"-"+op.getRequerimiento().getNumeroStr());
				hojaRutaReporte.setFechaEntrega(op.getRequerimiento().getFechaEntregaStr()+"\n "+op.getRequerimiento().getHoraEntrega());
				hojaRutaReporte.setTipoRequerimiento(op.getRequerimiento().getTipoRequerimiento().getDescripcion());
				hojaRutaReporte.setSolicitante(op.getRequerimiento().getEmpleadoSolicitante().getApellidoYNombre());
				hojaRutaReporte.setDireccionEntrega(op.getRequerimiento().getDireccionDefecto().getDireccion().getCalle()+". Nro: "+op.getRequerimiento().getDireccionDefecto().getDireccion().getNumero());
				hojaRutaReporte.setCliente(op.getRequerimiento().getClienteEmp().getRazonSocialONombreYApellido());
				hojaRutaReporte.setObservaciones(op.getRequerimiento().getObservaciones());
				if(opElemento.getEstado().equals(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_SELECCIONADO)){
					hojaRutaReporte.setCantidadElemento(1);
				}else{
					hojaRutaReporte.setCantidadElemento(0);
				}
				listOperaciones.add(hojaRutaReporte);
			}else{
				for(hojaRutaReporte hr:listOperaciones){
					if(hr.getIdHojaRutaOpElemnt().equals(opElemento.getOperacionElemento().getOperacion().getId())){
						if(opElemento.getEstado().equals(ESTADO_HOJA_RUTA_OPERACION_ELEMENTO_SELECCIONADO)){
							hr.setCantidadElemento(hr.getCantidadElemento()+1);
						}	
					}
				}
				
			}
			
				
		}
		return listOperaciones;
	}
	// Elemento para la grilla
	public class hojaRutaReporte{
		
		public String serie;
		public String fechaEntrega;
		public String cliente;
		public String tipoRequerimiento;
		public String direccionEntrega;
		public Integer cantidadElemento;
		public String solicitante;
		public Long idHojaRutaOpElemnt;
		public String observaciones;
		
		public String getSerie() {
			return serie;
		}
		public void setSerie(String serie) {
			this.serie = serie;
		}
		public String getFechaEntrega() {
			return fechaEntrega;
		}
		public void setFechaEntrega(String fechaEntrega) {
			this.fechaEntrega = fechaEntrega;
		}
		public String getCliente() {
			return cliente;
		}
		public void setCliente(String cliente) {
			this.cliente = cliente;
		}
		public String getTipoRequerimiento() {
			return tipoRequerimiento;
		}
		public void setTipoRequerimiento(String tipoRequerimiento) {
			this.tipoRequerimiento = tipoRequerimiento;
		}
		public String getDireccionEntrega() {
			return direccionEntrega;
		}
		public void setDireccionEntrega(String direccionEntrega) {
			this.direccionEntrega = direccionEntrega;
		}
		public Integer getCantidadElemento() {
			return cantidadElemento;
		}
		public void setCantidadElemento(Integer cantidadElemento) {
			this.cantidadElemento = cantidadElemento;
		}
		public String getSolicitante() {
			return solicitante;
		}
		public void setSolicitante(String solicitante) {
			this.solicitante = solicitante;
		}
		public Long getIdHojaRutaOpElemnt() {
			return idHojaRutaOpElemnt;
		}
		public void setIdHojaRutaOpElemnt(Long idHojaRutaOpElemnt) {
			this.idHojaRutaOpElemnt = idHojaRutaOpElemnt;
		}
		public String getObservaciones() {
			return observaciones;
		}
		public void setObservaciones(String observaciones) {
			this.observaciones = observaciones;
		}
		
		
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
}
