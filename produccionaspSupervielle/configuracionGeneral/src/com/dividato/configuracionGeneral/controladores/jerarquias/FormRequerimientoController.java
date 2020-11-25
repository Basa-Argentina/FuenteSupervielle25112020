/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/06/2011
 */
package com.dividato.configuracionGeneral.controladores.jerarquias;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.RefAddr;
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

import com.dividato.configuracionGeneral.validadores.jerarquias.RequerimientoValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RearchivoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.jerarquias.interfaz.RequerimientoReferenciaService;
import com.security.accesoDatos.jerarquias.interfaz.RequerimientoService;
import com.security.accesoDatos.jerarquias.interfaz.TipoOperacionService;
import com.security.accesoDatos.jerarquias.interfaz.TipoRequerimientoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Rearchivo;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.jerarquias.RequerimientoReferencia;
import com.security.modelo.jerarquias.TipoOperacion;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * @author Gabriel Mainero
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioRequerimiento.html",
				"/guardarActualizarRequerimiento.html",
				"/prepararActualizarRequerimiento.html"
			}
		)
public class FormRequerimientoController {
	private RequerimientoService service;
	private RequerimientoValidator validator;	
	private ListaRequerimientoController listaRequerimientosController;
	private ElementoService elementoService;
	private ClienteEmpService clienteEmpService;
	private ClienteDireccionService clienteDireccionService;
	private SerieService serieService;
	private TipoRequerimientoService tipoRequerimientoService;
	private EmpleadoService empleadoService;
	private ReferenciaService referenciaService;
	private TipoOperacionService tipoOperacionService;
	private DepositoService depositoService;
	private ListaPreciosService listaPreciosService;
	private RearchivoService rearchivoService;
	private RequerimientoReferenciaService requerimientoReferenciaService;
	
	@Autowired
	public void setRequerimientoReferenciaService(RequerimientoReferenciaService requerimientoReferenciaService) {
		this.requerimientoReferenciaService = requerimientoReferenciaService;
	}
	
	@Autowired
	public void setRearchivoService(RearchivoService rearchivoService) {
		this.rearchivoService = rearchivoService;
	}
	
	@Autowired
	public void setListaPreciosService(ListaPreciosService listaPreciosService) {
		this.listaPreciosService = listaPreciosService;
	}
	
	@Autowired
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	
	@Autowired
	public void setTipoOperacionService(TipoOperacionService tipoOperacionService) {
		this.tipoOperacionService = tipoOperacionService;
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
	public void setService(RequerimientoService impuestoService) {
		this.service = impuestoService;
	}
	@Autowired
	public void setListaRequerimientosController(ListaRequerimientoController listaRequerimientosController) {
		this.listaRequerimientosController = listaRequerimientosController;
	}
	@Autowired
	public void setValidator(RequerimientoValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	@Autowired
	public void setTipoRequerimientoService(TipoRequerimientoService tipoRequerimientoService) {
		this.tipoRequerimientoService = tipoRequerimientoService;
	}
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioUser.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de User, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioUser" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioRequerimiento.html",
			method = RequestMethod.GET
		)
	public String precarga(
			@RequestParam(value="accion",required=false) String accion,	
			@RequestParam(value="primeraVez",required=false) String primeraVez,
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="val",required=false) String val,
			@RequestParam(value="val",required=false) String valCliente,
			@RequestParam(value="val", required=false) String valListaPrecio,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo,
			Map<String,Object> atributos, HttpSession session) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			Requerimiento requerimiento;
			if(atributos.get("requerimientoFormulario") == null){
				requerimiento = service.obtenerPorId(id);
				if(requerimiento!=null){
					ArrayList<RequerimientoReferencia> list = (ArrayList<RequerimientoReferencia>) requerimientoReferenciaService.listarRequerimientoReferenciaPorRequerimiento(requerimiento, obtenerClienteAspUser());
					if(list!=null)
						requerimiento.setListaElementos(new HashSet<RequerimientoReferencia>(list));
				}
				
				requerimiento.setCodigoPersonal(requerimiento.getEmpleadoSolicitante().getCodigo());
				requerimiento.setCodigoPersonalAutorizante(requerimiento.getEmpleadoAutorizante().getCodigo());
				
				if(requerimiento!=null && requerimiento.getClienteEmp()!=null)
					clienteCodigo = requerimiento.getClienteEmp().getCodigo();
				if(requerimiento.getListaElementos()!=null && requerimiento.getListaElementos().size() == 0)
					requerimiento.setListaElementos(null);
				if(primeraVez==null && requerimiento.getListaElementos()!=null ){
					HashSet<RequerimientoReferencia> detalle = new HashSet<RequerimientoReferencia>();
					for(RequerimientoReferencia req:requerimiento.getListaElementos())
						detalle.add(req);
					session.setAttribute("listaElementosRequerimientoFormulario",detalle);
					requerimiento.setListaElementos(detalle);
				}
				if(requerimiento.getListaElementos()==null)
					session.removeAttribute("listaElementosRequerimientoFormulario");
			}
			else{
				requerimiento = (Requerimiento) atributos.get("requerimientoFormulario");
				if(requerimiento.getListaElementos()!=null && requerimiento.getListaElementos().size() == 0)
					requerimiento.setListaElementos(null);
			}
			atributos.put("requerimientoFormulario", requerimiento);			
		}
		else{
			if(atributos.get("requerimientoFormulario") == null){
				if(clienteCodigo==null){
					Requerimiento requerimiento = new Requerimiento();
					requerimiento.setFechaAlta(new Date());
					SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
					String hora = formato.format(new Date());
					requerimiento.setHoraAlta(hora);
					atributos.put("requerimientoFormulario", requerimiento);	
					session.removeAttribute("requerimientoFormularioBuscarElemento");
					session.removeAttribute("accionRequerimientoFormularioBuscarElemento");
					session.removeAttribute("listaElementosRequerimientoFormulario");
				}
			}
			else{
				Requerimiento requerimiento = (Requerimiento) atributos.get("requerimientoFormulario");
				if(requerimiento.getListaElementos()!=null && requerimiento.getListaElementos().size() == 0)
					requerimiento.setListaElementos(null);
				atributos.put("requerimientoFormulario", requerimiento);	
			}
		}
		
		//Seteo la accion actual
		atributos.put("accion", accion);		
		//seteo el id del clienteAsp en la pantalla
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		atributos.put("codigoEmpresa", obtenerEmpresa().getCodigo());
		
		//Se realiza el redirect
		return "formularioRequerimiento";
	}

	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos User.
	 * 
	 * @param User user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto User con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioUser" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(
			value="/guardarActualizarRequerimiento.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("requerimientoFormulario") final Requerimiento requerimientoFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request){
		Boolean commit = null, tipoOperacionExistente = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		
		Boolean bandera = (Boolean)session.getAttribute("bandera");
		if(bandera==null)
			bandera = false;
		
		//cago las asociaciones y la accion
		setAsociaciones(requerimientoFormulario, accion, bandera);
		requerimientoFormulario.setListaElementos((HashSet<RequerimientoReferencia>) session.getAttribute("listaElementosRequerimientoFormulario"));
		if(!result.hasErrors())		
			validator.validate(requerimientoFormulario, result); //valido datos	
		/// Si no es para buscar elementos ni para elmiminarlos, es para guardar///////////////////////////////////
		if(!requerimientoFormulario.isBuscarElemento() && !requerimientoFormulario.isEliminarElemento() && !requerimientoFormulario.isBuscarElementoSinReferencia()){
			//Si la validacion sale exitosa comienzo el proceso de registro en BD
			Requerimiento req;

			if(!result.hasErrors()){	
				if(accion.equals("NUEVO")){
					req = new Requerimiento();
					setData(req, requerimientoFormulario);
					req.setEstado("Pendiente");
					//Busco el ultimo numero de serie y le sumo uno
					String numero = req.getSerie().getUltNroImpreso();
					Long numeroLong = null;
					try {
						numeroLong = Long.parseLong(numero);
						numeroLong +=1;
						req.setNumero(new BigInteger(numeroLong.toString()));
						requerimientoFormulario.setNumero(new BigInteger(numeroLong.toString()));
						req.getSerie().setUltNroImpreso(req.getNumeroStr());
					} catch (NumberFormatException e) {
						
					}
					req.setListaElementos((HashSet<RequerimientoReferencia>) session.getAttribute("listaElementosRequerimientoFormulario"));
					//Verifico que exista tipo de operaciones para el requerimiento seleccionado
					ArrayList<TipoOperacion> listaTipoOperaciones = (ArrayList<TipoOperacion>) tipoOperacionService.listarTipoOperacion(null, null, req.getTipoRequerimiento(), req.getClienteAsp());
					if(listaTipoOperaciones==null || listaTipoOperaciones.size()==0)
						tipoOperacionExistente = new Boolean(false);
					else
						tipoOperacionExistente = new Boolean(true);
					//Se guarda el cliente en la BD
					if(tipoOperacionExistente!=null && tipoOperacionExistente)
						commit = service.save(req, req.getSerie(),(HashSet<RequerimientoReferencia>) session.getAttribute("listaElementosRequerimientoFormulario"), crearOperaciones(req, listaTipoOperaciones));
				}else{
					req = service.obtenerPorId(requerimientoFormulario.getId());
					Requerimiento requerimiento = service.obtenerPorId(req.getId());
					if(requerimiento!=null){
						ArrayList<RequerimientoReferencia> list = (ArrayList<RequerimientoReferencia>) requerimientoReferenciaService.listarRequerimientoReferenciaPorRequerimiento(requerimiento, obtenerClienteAspUser());
						if(list!=null)
							requerimiento.setListaElementos(new HashSet<RequerimientoReferencia>(list));
					}
					requerimientoFormulario.setNumero(requerimiento.getNumero());
					requerimientoFormulario.setEstado(requerimiento.getEstado());
					setData(req, requerimientoFormulario);
					//Se Actualiza el cliente en la BD
					
					commit = service.update(req,(HashSet<RequerimientoReferencia>) session.getAttribute("listaElementosRequerimientoFormulario"),requerimiento.getListaElementos());
				}
				if(commit == null || !commit)
					requerimientoFormulario.setId(req.getId());
			}
			
			//Ver errores
			if(tipoOperacionExistente != null && !tipoOperacionExistente){
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				ScreenMessage mensaje = new ScreenMessageImp("formularioRequerimiento.error.tipoOperacionNoExistente", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				requerimientoFormulario.setListaElementos((HashSet<RequerimientoReferencia>) session.getAttribute("listaElementosRequerimientoFormulario"));
				atributos.put("requerimientoFormulario", requerimientoFormulario);
				atributos.put("accion", accion);
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
				return precarga(accion, "NO", requerimientoFormulario.getId(), null, null, null, null, atributos, session);
			}
			if(commit != null && !commit){
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				requerimientoFormulario.setListaElementos((HashSet<RequerimientoReferencia>) session.getAttribute("listaElementosRequerimientoFormulario"));
				atributos.put("requerimientoFormulario", requerimientoFormulario);
				atributos.put("accion", accion);
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
				return precarga(accion, "NO", requerimientoFormulario.getId(), null, null, null, null, atributos, session);			
			}
			
			if(result.hasErrors()){
				requerimientoFormulario.setListaElementos((HashSet<RequerimientoReferencia>) session.getAttribute("listaElementosRequerimientoFormulario"));
				atributos.put("requerimientoFormulario", requerimientoFormulario);
				atributos.put("accion", accion);
				atributos.put("errores", true);
				atributos.put("result", result);
				return precarga(accion, "NO", requerimientoFormulario.getId(), null, null, null, null, atributos, session);
			}else{
				//Genero las notificaciones 
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				if("NUEVO".equals(accion)){	
					ScreenMessage notificacion = new ScreenMessageImp("notif.requerimiento.registrado", Arrays.asList(new String[]{requerimientoFormulario.getSerie().getCodigo() + ": " + requerimientoFormulario.getPrefijoStr()+"-"+requerimientoFormulario.getNumeroStr()}));
					avisos.add(notificacion); //agrego el mensaje a la coleccion
				}else{
					ScreenMessage notificacion = new ScreenMessageImp("notif.requerimiento.modificado", Arrays.asList(new String[]{requerimientoFormulario.getSerie().getCodigo() + ": " + requerimientoFormulario.getPrefijoStr()+"-"+requerimientoFormulario.getNumeroStr()}));
					avisos.add(notificacion); //agrego el mensaje a la coleccion
				}
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", true);
				atributos.put("avisos", avisos);
				//Lineas agregadas para probar resolver errores
				atributos.remove("requerimientoFormulario");
				session.removeAttribute("requerimientoFormularioBuscarElemento");
				session.removeAttribute("accionRequerimientoFormularioBuscarElemento");
				session.removeAttribute("listaElementosRequerimientoFormulario");
			}
			
			//hacemos el redirect
			return listaRequerimientosController.mostrarRequerimiento(session, atributos,null,null,null,null,null,null, request);
		}
		//Entonces si es para buscar elementos////////////////
		else if(requerimientoFormulario.isBuscarElemento()){
			HashSet<RequerimientoReferencia> referencias = (HashSet<RequerimientoReferencia>) session.getAttribute("listaElementosRequerimientoFormulario");
			if(referencias!=null)
				requerimientoFormulario.setListaElementos(referencias);
			atributos.put("requerimientoFormulario", requerimientoFormulario);
			atributos.put("accion", accion);
			session.setAttribute("requerimientoFormularioBuscarElemento", requerimientoFormulario);
			session.setAttribute("accionRequerimientoFormularioBuscarElemento", accion);
			if(result.hasErrors()){
				atributos.put("errores", true);
				atributos.put("result", result);
				return precarga(accion, "NO", requerimientoFormulario.getId(), null, null, null, null, atributos, session);
			}
			String idReq = "";
			if(requerimientoFormulario.getId()!=null)
				idReq = "&idRequerimiento="+requerimientoFormulario.getId();
			return "redirect:iniciarRequerimientoElemento.html?clienteCodigoString=" + requerimientoFormulario.getClienteCodigo() + "&destinoURL=prepararActualizarRequerimiento.html"+idReq;
		}
		//Entonces si es para buscar elementos sin referencia////////////////
		else if(requerimientoFormulario.isBuscarElementoSinReferencia()){
			HashSet<RequerimientoReferencia> referencias = (HashSet<RequerimientoReferencia>) session.getAttribute("listaElementosRequerimientoFormulario");
			if(referencias!=null)
				requerimientoFormulario.setListaElementos(referencias);
			atributos.put("requerimientoFormulario", requerimientoFormulario);
			atributos.put("accion", accion);
			session.setAttribute("requerimientoFormularioBuscarElemento", requerimientoFormulario);
			session.setAttribute("accionRequerimientoFormularioBuscarElemento", accion);
			if(result.hasErrors()){
				atributos.put("errores", true);
				atributos.put("result", result);
				return precarga(accion, "NO", requerimientoFormulario.getId(), null, null, null, null, atributos, session);
			}
			String idReq = "";
			if(requerimientoFormulario.getId()!=null)
				idReq = "&idRequerimiento="+requerimientoFormulario.getId();
			return "redirect:iniciarRequerimientoElementoSinReferencia.html?clienteCodigoString=" + requerimientoFormulario.getClienteCodigo() + "&destinoURL=prepararActualizarRequerimiento.html"+idReq;
		}
		//Entonces es para eliminar elementos/////////////////
		else{
			HashSet<RequerimientoReferencia> referencias = (HashSet<RequerimientoReferencia>) session.getAttribute("listaElementosRequerimientoFormulario");
			if(referencias!=null)
				requerimientoFormulario.setListaElementos(referencias);
			if(requerimientoFormulario.getListaElementos()!=null && requerimientoFormulario.getEliminarElementoId()!=null){
				Iterator<RequerimientoReferencia> ite = requerimientoFormulario.getListaElementos().iterator();
				Referencia refBuscarRearchivos = null;
				while(ite.hasNext()){
					RequerimientoReferencia req = ite.next();
					if(req.getReferencia()!=null && req.getReferencia().getId()!=null 
							&& req.getReferencia().getId().longValue() == requerimientoFormulario.getEliminarElementoId().longValue()){
						if((req.getReferencia().getDescripcionRearchivo()==null || "".equals(req.getReferencia().getDescripcionRearchivo())) && req.getReferencia().getReferenciaRearchivo()==null)
							refBuscarRearchivos = req.getReferencia();
						ite.remove();
						break;
					}
				}
				//Eliminamos las referencias de rearchivo
				if(refBuscarRearchivos!=null){
					Iterator<RequerimientoReferencia> iteRear = requerimientoFormulario.getListaElementos().iterator();
					while(iteRear.hasNext()){
						RequerimientoReferencia req = iteRear.next();
						if(req.getReferencia()!=null && req.getReferencia().getReferenciaRearchivo()!=null 
								&& req.getReferencia().getReferenciaRearchivo().getId().longValue() == refBuscarRearchivos.getId().longValue()){
							iteRear.remove();
						}
					}
				}
				session.setAttribute("listaElementosRequerimientoFormulario",requerimientoFormulario.getListaElementos());
			}
			atributos.put("requerimientoFormulario", requerimientoFormulario);
			atributos.put("accion", accion);
			if(requerimientoFormulario.getListaElementos()!=null && requerimientoFormulario.getListaElementos().size()>0)
				atributos.put("listaConElementos", "SI");
			else
				atributos.put("listaConElementos", "NO");
			return precarga(accion, "NO", requerimientoFormulario.getId(), null, null, null, requerimientoFormulario.getClienteCodigo(), atributos, session);
		}
	}
	
	@RequestMapping(
			value="/prepararActualizarRequerimiento.html",
			method=RequestMethod.POST
		)
	public String preparar(
			@RequestParam(value="requerimientoElementosSel",required=false)String requerimientoElementosSel,
			@RequestParam(value="sinReferencias",required=false)Boolean sinReferencias,
			HttpSession session, Map<String, Object> atributos
			) {
		Requerimiento requerimientoFormulario = (Requerimiento) session.getAttribute("requerimientoFormularioBuscarElemento");
		String accion = (String) session.getAttribute("accionRequerimientoFormularioBuscarElemento");
		if(requerimientoElementosSel!=null && !"".equals(requerimientoElementosSel)){
			if(requerimientoFormulario.getListaElementos()==null)
				requerimientoFormulario.setListaElementos(new HashSet<RequerimientoReferencia>());
			Hashtable<Long, RequerimientoReferencia> hashBuscar = new Hashtable<Long, RequerimientoReferencia>();
			
			for(RequerimientoReferencia req:requerimientoFormulario.getListaElementos()){
				if(req.getElemento() != null && req.getElemento().getId()!=null)
				{
					if(hashBuscar.get(req.getElemento().getId()) == null)
					hashBuscar.put(req.getElemento().getId(), req);
				}
					
			}
			String[] seleccionados = requerimientoElementosSel.split(",");
			for(String buscar:seleccionados){
				Referencia referencia = null;
				if(sinReferencias==null || !sinReferencias)
					referencia = referenciaService.obtenerPorId(new Long(buscar));
				RequerimientoReferencia requerimientoReferencia = new RequerimientoReferencia();
				requerimientoReferencia.setRequerimiento(requerimientoFormulario);
				requerimientoReferencia.setReferencia(referencia);
				if(referencia!=null && requerimientoReferencia.getReferencia().getOrdenRearchivo()!=null)
					requerimientoReferencia.getReferencia().setDescripcion(requerimientoReferencia.getReferencia().getDescripcionRearchivo()+" / "+requerimientoReferencia.getReferencia().getOrdenRearchivo().toString());

				String busqueda = "";
				Elemento elemento = null;
				if(referencia!=null && referencia.getElemento()!=null && requerimientoReferencia.getRequerimiento().isBuscarElementoSinReferencia() != true){
					elemento = referencia.getElemento();
				}else{
					elemento = elementoService.obtenerPorId(new Long(buscar));
				}
				if(elemento!=null){
					requerimientoReferencia.setElemento(elemento);
					
					if(elemento.getTipoElemento()!=null && !"".equals(elemento.getTipoElemento().getDescripcion()))
						busqueda = elemento.getTipoElemento().getDescripcion()+": ";
					
					if(referencia != null && referencia.getIndiceIndividual() != null 
							&& referencia.getIndiceIndividual() && elemento!=null && elemento.getContenedor()!=null 
							&& elemento.getContenedor().getTipoElemento()!=null){
						busqueda = elemento.getContenedor().getTipoElemento().getDescripcion()+": " + elemento.getContenedor().getCodigo();
					}
					else{
						if(elemento.getCodigo() != null && !"".equals(elemento.getCodigo()))
							busqueda += elemento.getCodigo();
					}
					
				
					if(busqueda.length()>60)
						busqueda = busqueda.substring(0, 59);
					requerimientoReferencia.setBusqueda(busqueda);
				}
				
				if(elemento.getId()!=null)
					if(hashBuscar.get(elemento.getId())==null)
					{
						requerimientoFormulario.getListaElementos().add(requerimientoReferencia);
						hashBuscar.put(elemento.getId(), requerimientoReferencia);
					}else if(hashBuscar.get(elemento.getId()).getReferencia().getOrdenRearchivo()!= null 
							&& requerimientoReferencia.getReferencia().getOrdenRearchivo()!= null 
							&& hashBuscar.get(elemento.getId()).getReferencia().getOrdenRearchivo().longValue() 
							!= requerimientoReferencia.getReferencia().getOrdenRearchivo().longValue()){
								requerimientoFormulario.getListaElementos().add(requerimientoReferencia);
					}
				
				if(referencia!=null){
					//Buscamos si la referencia tiene rearchivos asociados pendientes y los incorporamos a la lista
					ArrayList<Rearchivo> listaRearchivos = (ArrayList<Rearchivo>) rearchivoService.obtenerParaReferencia(referencia);
					if(listaRearchivos!=null && listaRearchivos.size()>0){
						for(Rearchivo rearchivo:listaRearchivos){
							Referencia refRearchivo = rearchivo.getReferencia();
							if(refRearchivo!=null){
								RequerimientoReferencia requerimientoReferenciaRearchivo = new RequerimientoReferencia();
								requerimientoReferenciaRearchivo.setRequerimiento(requerimientoFormulario);
								requerimientoReferenciaRearchivo.setReferencia(refRearchivo);
								requerimientoReferenciaRearchivo.setElemento(refRearchivo.getElemento());
								requerimientoReferenciaRearchivo.getReferencia().setDescripcion(refRearchivo.getDescripcionRearchivo()+" / "+refRearchivo.getOrdenRearchivo().toString());
								busqueda = "";
								if(refRearchivo.getElemento()!=null){
									if(refRearchivo.getElemento().getTipoElemento()!=null && !"".equals(refRearchivo.getElemento().getTipoElemento().getDescripcion()))
										busqueda = refRearchivo.getElemento().getTipoElemento().getDescripcion()+": ";
									if(refRearchivo.getIndiceIndividual() != null){
										if(refRearchivo.getIndiceIndividual() && refRearchivo.getElemento()!=null && refRearchivo.getElemento().getContenedor()!=null && refRearchivo.getElemento().getContenedor().getTipoElemento()!=null){
											busqueda = refRearchivo.getElemento().getContenedor().getTipoElemento().getDescripcion()+": " + refRearchivo.getElemento().getContenedor().getCodigo();
										}
										else{
											if(refRearchivo.getElemento().getCodigo() != null && !"".equals(refRearchivo.getElemento().getCodigo()))
												busqueda += refRearchivo.getElemento().getCodigo();
										}
									}
								}
								if(busqueda.length()>60)
									busqueda = busqueda.substring(0, 59);
								requerimientoReferenciaRearchivo.setBusqueda(busqueda);
								if(requerimientoReferenciaRearchivo.getReferencia()!=null && requerimientoReferenciaRearchivo.getReferencia().getElemento() !=null 
										&& requerimientoReferenciaRearchivo.getReferencia().getElemento().getId()!=null)
									if(hashBuscar.get(requerimientoReferenciaRearchivo.getReferencia().getElemento().getId())==null){
										requerimientoFormulario.getListaElementos().add(requerimientoReferenciaRearchivo);
										hashBuscar.put(requerimientoReferenciaRearchivo.getReferencia().getElemento().getId(), requerimientoReferenciaRearchivo);
									}else if(hashBuscar.get(requerimientoReferenciaRearchivo.getReferencia().getElemento().getId()).getReferencia().getOrdenRearchivo()!=null &&
											requerimientoReferenciaRearchivo.getReferencia().getOrdenRearchivo()!=null && 
											hashBuscar.get(requerimientoReferenciaRearchivo.getReferencia().getElemento().getId()).getReferencia().getOrdenRearchivo().longValue() != 
												requerimientoReferenciaRearchivo.getReferencia().getOrdenRearchivo().longValue()){
										requerimientoFormulario.getListaElementos().add(requerimientoReferenciaRearchivo);
										
									}
							}
						}
					}
				}	
			}
		}
		atributos.put("requerimientoFormulario", requerimientoFormulario);
		session.setAttribute("listaElementosRequerimientoFormulario", requerimientoFormulario.getListaElementos());
		if(requerimientoFormulario.getListaElementos()!=null && requerimientoFormulario.getListaElementos().size()>0)
			atributos.put("listaConElementos", "SI");
		else
			atributos.put("listaConElementos", "NO");
		return precarga(accion, "NO", null, null, null, null, requerimientoFormulario.getClienteCodigo(), atributos, session);
	}
	
	private void setData(Requerimiento o, Requerimiento d){
		if(d != null){			
			o.setClienteAsp(d.getClienteAsp());
			o.setTipoRequerimiento(d.getTipoRequerimiento());
			o.setSucursal(d.getSucursal());
			o.setClienteEmp(d.getClienteEmp());
			o.setEmpleadoSolicitante(d.getEmpleadoSolicitante());
			o.setEmpleadoAutorizante(d.getEmpleadoAutorizante());
			o.setFechaAlta(d.getFechaAlta());
			o.setFechaEntrega(d.getFechaEntrega());
			o.setFechaCierre(d.getFechaCierre());
			o.setSerie(d.getSerie());
			o.setPrefijo(d.getPrefijo());
			o.setNumero(d.getNumero());
			o.setDireccionDefecto(d.getDireccionDefecto());
			o.setEstado(d.getEstado());
			o.setHoraAlta(d.getHoraAlta());
			o.setHoraEntrega(d.getHoraEntrega());
			o.setListaElementos(d.getListaElementos());
			o.setDepositoDefecto(d.getDepositoDefecto());
			User user = obtenerUser();
			if(user!=null)
				o.setUsuario(user);
			o.setCambioDireccionDefecto(d.isCambioDireccionDefecto());
			o.setListaPrecios(d.getListaPrecios());
			
			o.setObservaciones(d.getObservaciones());
		}
	}
	
	private void setAsociaciones(Requerimiento d, String accion,boolean bandera){
		//seteo la accion
		d.setAccion(accion);
		//obtengo el cliente
		d.setClienteAsp(obtenerClienteAspUser());
		//obtengo tipo de requerimiento
		d.setTipoRequerimiento(tipoRequerimientoService.obtenerPorCodigo(
				d.getTipoRequerimientoCod(), d.getClienteAsp()));
		//obtengo serie
		if(d.getCodigoSerie() != null && !"".equals(d.getCodigoSerie()))
			d.setSerie(serieService.obtenerPorCodigo(
					d.getCodigoSerie(), "I", d.getClienteAsp()));	
		//obtengo el prefijo
		if(d.getSerie()!=null)
			d.setPrefijo(new BigInteger(d.getSerie().getPrefijo()));
		//obtengo cliente
		if(d.getClienteCodigo() != null && !"".equals(d.getClienteCodigo())){
			ClienteEmp cli = new ClienteEmp();
			cli.setCodigo(d.getClienteCodigo());
			d.setClienteEmp(clienteEmpService.getByCodigo(cli, obtenerClienteAspUser()));
		}
		//obtengo direccion cliente
		if(d.getCodigoDireccion() != null && !"".equals(d.getCodigoDireccion())){
			if(bandera==true)
				d.setDireccionDefecto(clienteDireccionService.getByCodigo(d.getCodigoDireccion(), obtenerClienteAspUser()));
			else
				d.setDireccionDefecto(clienteDireccionService.obtenerPorCodigo(d.getCodigoDireccion(), d.getClienteEmp(), obtenerClienteAspUser()));
		}
		//obtengo empleado solicitante
		if(d.getCodigoPersonal() != null && !"".equals(d.getCodigoPersonal())){
			if(bandera==true)
				d.setEmpleadoSolicitante(empleadoService.getByCodigo(d.getCodigoPersonal(), null, obtenerClienteAspUser()));
			else
				d.setEmpleadoSolicitante(empleadoService.getByCodigo(d.getCodigoPersonal(), d.getClienteCodigo(), obtenerClienteAspUser()));
		}
		//obtengo empleado autorizante
		if(d.getCodigoPersonalAutorizante() != null && !"".equals(d.getCodigoPersonalAutorizante())){
			if(bandera==true)
				d.setEmpleadoAutorizante(empleadoService.getByCodigo(d.getCodigoPersonalAutorizante(), null, obtenerClienteAspUser()));
			else
				d.setEmpleadoAutorizante(empleadoService.getByCodigo(d.getCodigoPersonalAutorizante(), d.getClienteCodigo(), obtenerClienteAspUser()));
		}
		
		String codigoSucursal = null;
		Sucursal sucursal = obtenerSucursal();
		if(sucursal!=null){
			d.setSucursal(sucursal);
			codigoSucursal = sucursal.getCodigo();
		}
		
		//obtengo el deposito
		if(d.getCodigoDeposito()!= null && !"".equals(d.getCodigoDeposito()))
			d.setDepositoDefecto(depositoService.getByCodigoYSucursal(d.getCodigoDeposito(), codigoSucursal, obtenerClienteAspUser()));
		//obtengo la lista de precios
		if(d.getListaPrecioCodigo() != null && !"".equals(d.getListaPrecioCodigo()))
			d.setListaPrecios(listaPreciosService.obtenerListaPreciosPorCodigo(d.getListaPrecioCodigo(), obtenerClienteAspUser()));
	}
	
	
	
	
	
	private ClienteAsp obtenerClienteAspUser(){
		return obtenerUser().getCliente();
	}
	
	private User obtenerUser(){		
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	private Sucursal obtenerSucursal(){
		User usuario = obtenerUser();
		return ((PersonaFisica)usuario.getPersona()).getSucursalDefecto();
	}
	private Empresa obtenerEmpresa(){
		User usuario = obtenerUser();
		return ((PersonaFisica)usuario.getPersona()).getEmpresaDefecto();
	}
	
	private List<Operacion> crearOperaciones(Requerimiento requerimiento, ArrayList<TipoOperacion> listaTipoOperaciones){
		ArrayList<Operacion> salida = new ArrayList<Operacion>();
		if(listaTipoOperaciones!=null){
			for(TipoOperacion tipoOperacion:listaTipoOperaciones){
				
				//Se genera una sola operacion con todos los elementos
				if(!tipoOperacion.getDesagregaPorDeposito()){
					//Se genera una operacion por tipo de operacion
					Operacion operacion = new Operacion();
					setDataBasicoOperacion(operacion, requerimiento, tipoOperacion);
					//Si la cantidad de elementos es mayor a 1 y pertenecen a distintos depositos se utiliza el que se especifico en el requerimiento, sino se utiliza el deposito del primer elemento
					if(requerimiento.getListaElementos()!=null && requerimiento.getListaElementos().size()>1){
						if(verificarDistintoDeposito(requerimiento))
							operacion.setDeposito(requerimiento.getDepositoDefecto());
						//Si son del mismo deposito asigno el de cualquier elemento
						else{
							RequerimientoReferencia reqRef = null;
							for(RequerimientoReferencia req:requerimiento.getListaElementos()){
								reqRef = req;
								if(reqRef.getElemento()!=null && (reqRef.getElemento().getDepositoActual()!=null || 
										(reqRef.getElemento().getContenedor()!= null && reqRef.getElemento().getContenedor().getDepositoActual()!=null)))
											break;
							}
							if(reqRef.getElemento()!=null && (reqRef.getElemento().getDepositoActual()!=null || 
											(reqRef.getElemento().getContenedor()!= null && reqRef.getElemento().getContenedor().getDepositoActual()!=null))){
								if(reqRef.getElemento().getContenedor()==null || reqRef.getElemento().getContenedor().getDepositoActual()==null)
									operacion.setDeposito(reqRef.getElemento().getDepositoActual());
								else{
									if(reqRef.getElemento().getContenedor().getDepositoActual()!=null)
										operacion.setDeposito(reqRef.getElemento().getContenedor().getDepositoActual());
								}
							}
							else
								operacion.setDeposito(requerimiento.getDepositoDefecto());
						}
					}
					//Si la cantidad de elementos es igual a 1, se asigna al deposito de la operacion el mismo del unico elemento
					if(requerimiento.getListaElementos()!=null && requerimiento.getListaElementos().size()==1){
						RequerimientoReferencia reqRef = null;
						for(RequerimientoReferencia req:requerimiento.getListaElementos()){
							reqRef = req;
							break;
						}
						if(reqRef.getElemento()!=null 
								&& (reqRef.getElemento().getDepositoActual()!=null || 
										(reqRef.getElemento().getContenedor()!= null && reqRef.getElemento().getContenedor().getDepositoActual()!=null))){
							if(reqRef.getElemento().getContenedor()==null || reqRef.getElemento().getContenedor().getDepositoActual()==null)
								operacion.setDeposito(reqRef.getElemento().getDepositoActual());
							else{
								if(reqRef.getElemento().getContenedor().getDepositoActual()!=null)
									operacion.setDeposito(reqRef.getElemento().getContenedor().getDepositoActual());
							}
						}
						else
							operacion.setDeposito(requerimiento.getDepositoDefecto());
					}
					//Cambio Adrian 18092017
			
					//Si la lista de elementos es igual a 0 o Null , setea la cantidad , en cantidad Pendientes.
					
					if(requerimiento.getListaElementos()==null || requerimiento.getListaElementos().size()==0){
						operacion.setDeposito(requerimiento.getDepositoDefecto());
						operacion.setCantidadPendientes(requerimiento.getCantidad());
					}
					//Traspaso los elementos a la unica operacion
					if(requerimiento.getListaElementos()!=null && requerimiento.getListaElementos().size()>0){
						operacion.setListaElementos(new HashSet<OperacionElemento>());
						for(RequerimientoReferencia requerimientoReferencia: requerimiento.getListaElementos()){
							OperacionElemento oprr = new OperacionElemento();
							setDataOperacionRequerimientoReferencia(oprr, requerimientoReferencia, operacion);
							operacion.getListaElementos().add(oprr);
						}
						operacion.setCantidadPendientes(requerimiento.getListaElementos().size());
					}
					salida.add(operacion);
				}
				//Sumarizo y genero una operacion por deposito
				else{
					Hashtable<Long, List<RequerimientoReferencia>> sumarizadoPorDeposito = new Hashtable<Long, List<RequerimientoReferencia>>();
					if(requerimiento.getListaElementos()!=null && requerimiento.getListaElementos().size()>0){
						for(RequerimientoReferencia reqRef:requerimiento.getListaElementos()){
							Deposito deposito = null;
							if(reqRef.getElemento()!=null 
									&& (reqRef.getElemento().getDepositoActual()!=null || 
											(reqRef.getElemento().getContenedor()!= null && reqRef.getElemento().getContenedor().getDepositoActual()!=null))){
								if(reqRef.getElemento().getContenedor()==null || reqRef.getElemento().getContenedor().getDepositoActual()==null)
									deposito = reqRef.getElemento().getDepositoActual();
								else{
									if(reqRef.getElemento().getContenedor().getDepositoActual()!=null)
										deposito = reqRef.getElemento().getContenedor().getDepositoActual();
								}
							}
							else
								deposito = requerimiento.getDepositoDefecto();
							if(deposito!=null){
								if(sumarizadoPorDeposito.get(deposito.getId())==null){
									ArrayList<RequerimientoReferencia> lista = new ArrayList<RequerimientoReferencia>();
									lista.add(reqRef);
									sumarizadoPorDeposito.put(deposito.getId(), lista);
								}
								else{
									ArrayList<RequerimientoReferencia> lista = (ArrayList<RequerimientoReferencia>) sumarizadoPorDeposito.get(deposito.getId());
									lista.add(reqRef);
								}
							}
						}
					}
					else{
						//Si la cantidad de elementos es igual a 0, se crean las operaciones para el deposito por defecto
						if(requerimiento.getListaElementos()==null || requerimiento.getListaElementos().size()==0){
							Operacion operacion = new Operacion();
							setDataBasicoOperacion(operacion, requerimiento, tipoOperacion);
							operacion.setListaElementos(new HashSet<OperacionElemento>());
							operacion.setDeposito(requerimiento.getDepositoDefecto());
							operacion.setCantidadPendientes(0);
							salida.add(operacion);
						}
					}
					if(sumarizadoPorDeposito.values()!=null){
						for(List<RequerimientoReferencia> lista:sumarizadoPorDeposito.values()){
							//Se genera una operacion por deposito
							Operacion operacion = new Operacion();
							setDataBasicoOperacion(operacion, requerimiento, tipoOperacion);
							operacion.setListaElementos(new HashSet<OperacionElemento>());
							if(lista != null){
								for(RequerimientoReferencia reqRef:lista){
									if(reqRef.getElemento()!=null 
											&& (reqRef.getElemento().getDepositoActual()!=null || 
													(reqRef.getElemento().getContenedor()!= null && reqRef.getElemento().getContenedor().getDepositoActual()!=null))){
										if(reqRef.getElemento().getContenedor()==null || reqRef.getElemento().getContenedor().getDepositoActual()==null)
											operacion.setDeposito(reqRef.getElemento().getDepositoActual());
										else{
											if(reqRef.getElemento().getContenedor().getDepositoActual()!=null)
												operacion.setDeposito(reqRef.getElemento().getContenedor().getDepositoActual());
										}
									}
									else
										operacion.setDeposito(requerimiento.getDepositoDefecto());
									OperacionElemento oprr = new OperacionElemento();
									setDataOperacionRequerimientoReferencia(oprr, reqRef, operacion);
									operacion.getListaElementos().add(oprr);
								}
								operacion.setCantidadPendientes(lista.size());
							}
							salida.add(operacion);
						}
					}
				}
				
				
				
			}
		}
		return salida;
	}
	
	private void setDataBasicoOperacion (Operacion operacion, Requerimiento requerimiento, TipoOperacion tipoOperacion){
		operacion.setClienteAsp(requerimiento.getClienteAsp());
		operacion.setClienteEmp(requerimiento.getClienteEmp());
		operacion.setEstado("Pendiente");
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
	
	private void setDataOperacionRequerimientoReferencia(OperacionElemento op, RequerimientoReferencia rf, Operacion operacion){
		op.setOperacion(operacion);
		if(rf.getReferencia()!=null){
			op.setElemento(rf.getReferencia().getElemento());
			op.setPathArchivoDigital(rf.getReferencia().getPathArchivoDigital());
			if(rf.getReferencia().getDescripcionRearchivo()!=null && "Rearchivo".equals(rf.getReferencia().getDescripcionRearchivo()) 
					&& rf.getReferencia().getReferenciaRearchivo()!=null && rf.getReferencia().getReferenciaRearchivo().getElemento()!=null)
				op.setRearchivoDe(rf.getReferencia().getReferenciaRearchivo().getElemento());
				
		}else if(rf.getElemento()!=null){
			op.setElemento(rf.getElemento());
		}
		op.setEstado(Constantes.ESTADO_OPERACION_ELEMENTO_PENDIENTE);
	}
	
	private boolean verificarDistintoDeposito(Requerimiento requerimiento){
		Deposito deposito = null;
		boolean banderaDepositoDiferente = false;
		for(RequerimientoReferencia requerimientoReferencia:requerimiento.getListaElementos()){
			if(deposito==null){
				if(requerimientoReferencia !=null && requerimientoReferencia.getElemento()!=null 
						&& (requerimientoReferencia.getElemento().getDepositoActual()!=null || 
								(requerimientoReferencia.getElemento().getContenedor()!= null && requerimientoReferencia.getElemento().getContenedor().getDepositoActual()!=null))){
					if(requerimientoReferencia.getElemento().getContenedor()==null || requerimientoReferencia.getElemento().getContenedor().getDepositoActual()==null)
						deposito = requerimientoReferencia.getElemento().getDepositoActual();
					else{
						if(requerimientoReferencia.getElemento().getContenedor().getDepositoActual()!=null)
							deposito = requerimientoReferencia.getElemento().getContenedor().getDepositoActual();
					}
				}
			}
			else{
				Deposito depositoComparar = null;
				if(requerimientoReferencia!=null && requerimientoReferencia.getElemento()!=null 
						&& (requerimientoReferencia.getElemento().getDepositoActual()!=null || 
								(requerimientoReferencia.getElemento().getContenedor()!= null && requerimientoReferencia.getElemento().getContenedor().getDepositoActual()!=null))){
					if(requerimientoReferencia.getElemento().getContenedor()==null || requerimientoReferencia.getElemento().getContenedor().getDepositoActual()==null)
						depositoComparar = requerimientoReferencia.getElemento().getDepositoActual();
					else{
						if(requerimientoReferencia.getElemento().getContenedor().getDepositoActual()!=null)
							depositoComparar = requerimientoReferencia.getElemento().getContenedor().getDepositoActual();
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
}
