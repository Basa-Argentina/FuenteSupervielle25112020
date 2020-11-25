package com.dividato.configuracionGeneral.controladores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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

import com.dividato.configuracionGeneral.validadores.LoteFacturacionValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteConceptoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteFacturacionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PreFacturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PreFacturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.jerarquias.interfaz.ConceptoOperacionClienteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Impuesto;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.configuraciongeneral.LoteFacturacion;
import com.security.modelo.configuraciongeneral.PlantillaFacturacion;
import com.security.modelo.configuraciongeneral.PlantillaFacturacionDetalle;
import com.security.modelo.configuraciongeneral.PreFactura;
import com.security.modelo.configuraciongeneral.PreFacturaDetalle;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de User.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Victor Kenis (28/10/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/iniciarPrecargaFormularioLoteFacturacion.html",
				"/precargaFormularioLoteFacturacion.html",
				"/guardarActualizarLoteFacturacion.html",
				"/eliminarLoteFacturacionDetalle.html",
				"/importarLecturaElementosLoteFacturacion.html",
				"/generarConceptosMensualesLoteFacturacion.html",
				"/eliminarPreFactura.html"
			}
		)
public class FormLoteFacturacionController {
	
	private LoteFacturacionService loteFacturacionService;
	private PreFacturaService preFacturaService;
	private PreFacturaDetalleService preFacturaDetalleService;
	private LoteFacturacionValidator validator;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	private ListaLoteFacturacionController listaLoteFacturacionController;
	private ClienteConceptoService clienteConceptoService;
	private ElementoService elementoService;
	private ConceptoFacturableService conceptoFacturableService;
	private ClienteEmpService clienteEmpService;
	private ListaPreciosService listaPreciosService;
	private ConceptoOperacionClienteService conceptoOperacionClienteService;
	private PlantillaFacturacionService plantillaFacturacionService;
	private PlantillaFacturacionDetalleService plantillaFacturacionDetalleService;
	
	
	/**
	 * Setea el servicio de LoteFacturacion.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto LoteFacturacionService.
	 * La clase LoteFacturacionImp implementa LoteFacturacion y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param loteFacturacionService
	 */
	
	@Autowired
	public void setListaLoteFacturacionController(ListaLoteFacturacionController listaLoteFacturacionController) {
		this.listaLoteFacturacionController = listaLoteFacturacionController;
	}
	
	@Autowired
	public void setLoteFacturacionService(LoteFacturacionService loteFacturacionService) {
		this.loteFacturacionService = loteFacturacionService;
	}
	@Autowired
	public void setPreFacturaService(PreFacturaService preFacturaService) {
		this.preFacturaService = preFacturaService;
	}
	@Autowired
	public void setPreFacturaDetalleService(PreFacturaDetalleService preFacturaDetalleService) {
		this.preFacturaDetalleService = preFacturaDetalleService;
	}
	@Autowired
	public void setValidator(LoteFacturacionValidator validator) {
		this.validator = validator;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	
	@Autowired
	public void setClienteConceptoService(ClienteConceptoService clienteConceptoService) {
		this.clienteConceptoService = clienteConceptoService;
	}
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	
	@Autowired
	public void setConceptoFacturableService(ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setListaPreciosService(ListaPreciosService listaPreciosService) {
		this.listaPreciosService = listaPreciosService;
	}
	
	@Autowired
	public void setConceptoOperacionClienteService(ConceptoOperacionClienteService conceptoOperacionClienteService) {
		this.conceptoOperacionClienteService = conceptoOperacionClienteService;
	}
	
	@Autowired
	public void setPlantillaFacturacionService(PlantillaFacturacionService plantillaFacturacionService) {
		this.plantillaFacturacionService = plantillaFacturacionService;
	}
	
	@Autowired
	public void setPlantillaFacturacionDetalleService(PlantillaFacturacionDetalleService plantillaFacturacionDetalleService) {
		this.plantillaFacturacionDetalleService = plantillaFacturacionDetalleService;
	}
	
	@RequestMapping(
			value="/iniciarPrecargaFormularioLoteFacturacion.html",
			method = RequestMethod.GET
		)
	public String iniciarPrecargaFormularioLoteFacturacion(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion", required=false) String accion,
			@RequestParam(value="id", required=false) String id)
	{
		session.removeAttribute("listaPreFacturasSession");
		session.removeAttribute("loteFacturacionDetallesViejos");
		session.removeAttribute("conceptosSession");
		session.removeAttribute("conceptoOperacionClienteBusqueda");
		session.removeAttribute("conceptosSeleccionados");
		session.removeAttribute("conceptosAsignados");
		session.removeAttribute("loteFacturacionSession");
		
		return precargaFormularioLoteFacturacion(session, atributos, accion, id, null, null, null,null);
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioUser.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Elemento, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioLoteFacturacion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioLoteFacturacion.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioLoteFacturacion(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion", required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo,
			@RequestParam(value="periodo", required=false) String periodo){
		
		
		LoteFacturacion loteFacturacionFormulario = (LoteFacturacion)session.getAttribute("loteFacturacionSession");
		if(loteFacturacionFormulario== null)
		{
			loteFacturacionFormulario=new LoteFacturacion();
		}
		
		Empresa empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
		Sucursal sucursalDefecto = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto();
		
		@SuppressWarnings("unchecked")
		List<PreFactura> listaPreFacturas = (List<PreFactura>) session.getAttribute("listaPreFacturasSession");
		if(listaPreFacturas == null){
			listaPreFacturas = new ArrayList<PreFactura>();
		}
		
		if(accion==null) {
			accion="NUEVO"; //acción por defecto: nuevo
			loteFacturacionFormulario.setCodigoEmpresa(empresaDefecto.getCodigo());
			loteFacturacionFormulario.setCodigoSucursal(sucursalDefecto.getCodigo());
			loteFacturacionFormulario.setFechaRegistro(new Date());
			session.setAttribute("loteFacturacionSession", loteFacturacionFormulario);
		}
		if(!accion.equals("NUEVO")){
			loteFacturacionFormulario = (LoteFacturacion)session.getAttribute("loteFacturacionSession");
			if(loteFacturacionFormulario == null)
			{
				loteFacturacionFormulario = loteFacturacionService.obtenerPorId(Long.valueOf(id));
				loteFacturacionFormulario.setCodigoEmpresa(loteFacturacionFormulario.getEmpresa().getCodigo());
				loteFacturacionFormulario.setCodigoSucursal(loteFacturacionFormulario.getSucursal().getCodigo());
				if(listaPreFacturas == null || listaPreFacturas.size()<= 0){
					listaPreFacturas = preFacturaService.listarPreFacturasPorLoteFacturacion(loteFacturacionFormulario, obtenerClienteAspUser());
					loteFacturacionFormulario.setCantidadConceptos(listaPreFacturas.size());
					for(int i = 0;i<listaPreFacturas.size();i++){
						List<PreFacturaDetalle> detalles = preFacturaDetalleService.listarPreFacturaDetallesPorPreFactura(listaPreFacturas.get(i).getId(), listaPreFacturas.get(i).getClienteAsp());
						listaPreFacturas.get(i).setDetalles(detalles);
						for(PreFacturaDetalle detalle:detalles){
							List<ConceptoOperacionCliente> listaConceptosAModificar = conceptoOperacionClienteService.listarConceptosPorPreFacturaDetalle(detalle.getId(), listaPreFacturas.get(i).getClienteAsp());
							if(listaConceptosAModificar!=null && listaConceptosAModificar.size()>0){
								detalle.setListaConceptosAsociados(listaConceptosAModificar);
							}
						}
					}
					
				}
			}
			session.setAttribute("loteFacturacionSession", loteFacturacionFormulario);
			atributos.put("loteFacturacionFormulario", loteFacturacionFormulario);			
		}

		if(atributos.get("loteFacturacionFormulario") != null)
		{
			loteFacturacionFormulario = (LoteFacturacion) atributos.get("loteFacturacionFormulario");
		}
		else
		{
			atributos.put("loteFacturacionFormulario", loteFacturacionFormulario);
		}
		
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		
		if(listaPreFacturas!= null && listaPreFacturas.size()>0)
		{
			loteFacturacionFormulario.setCantidadConceptos(listaPreFacturas.size());
			atributos.put("loteFacturacionFormulario", loteFacturacionFormulario);
			atributos.put("listaPreFacturas", listaPreFacturas);
			atributos.put("activado", "no");
			session.setAttribute("listaPreFacturasSession", listaPreFacturas);
		}
		else
		{
			atributos.put("activado", "");
		}
		
		List<String> anos = new ArrayList<String>();
		SimpleDateFormat sd = new SimpleDateFormat("yyyy");
		Integer anoActual = Integer.valueOf(sd.format(new Date()));
		for(Integer i = anoActual-2;i<anoActual+10;i++)
		{
			anos.add(i.toString());
		}
		
		atributos.put("accion",accion);
		atributos.put("anos", anos);
		session.setAttribute("accionLoteSession", accion);
		return "formularioLoteFacturacion";
	}
	
//	/**
//	 * Observar la anotación @RequestMapping de SPRING.
//	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
//	 * 
//	 * Se encarga de guardar en base de datos Elemento.
//	 * 
//	 * @param Elemento user a guardar.
//	 * Observar la anotación @ModelAttribute que le indica a SPRING
//	 * que debe intentar llenar el objeto Elemento con los parámetros del 
//	 * request.
//	 * @param session es la misma sesión que usabamos con los Servlets.
//	 * @param atributos son los atributos del request
//	 * @return "formularioElemento" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
//	 */
	@RequestMapping(
			value="/guardarActualizarLoteFacturacion.html",
			method= RequestMethod.POST
		)
	public String guardarLoteFacturacion(
			@RequestParam(value="accion",required=false) String accion,
			@ModelAttribute("loteFacturacionFormulario") final LoteFacturacion loteFacturacion,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,HttpServletRequest request){
		
		@SuppressWarnings("unchecked")
		List<ConceptoOperacionCliente> listaConceptosActualizar = (List<ConceptoOperacionCliente>) session.getAttribute("listaConceptosSession");
		if(listaConceptosActualizar == null){
			listaConceptosActualizar = new ArrayList<ConceptoOperacionCliente>();
		}
		
		@SuppressWarnings("unchecked")
		List<PreFactura> listaPreFacturas = (List<PreFactura>) session.getAttribute("listaPreFacturasSession");
		if(listaPreFacturas == null){
			listaPreFacturas = new ArrayList<PreFactura>();
		}
		
		Boolean commit = null;
		if (accion == null || accion.equals("") || accion.equals("NUEVO"))
			accion = "NUEVO";
		else {
			accion = "MODIFICACION";
		}
		
		if(!result.hasErrors()){
			loteFacturacion.setAccion(accion);
			loteFacturacion.setClienteAsp(obtenerClienteAspUser());
			Empresa empresa = empresaService.getByCodigo(loteFacturacion.getCodigoEmpresa(), obtenerClienteAspUser());
			Sucursal sucursal = sucursalService.getByCodigo(loteFacturacion.getCodigoSucursal(), obtenerClienteAspUser());
			loteFacturacion.setEmpresa(empresa);
			loteFacturacion.setSucursal(sucursal);
			Set<PreFactura> detalles = new HashSet<PreFactura>(listaPreFacturas);
			loteFacturacion.setDetalles(detalles);
			
			validator.validate(loteFacturacion,result);
		}
	
		LoteFacturacion loteFacturacionFormulario = new LoteFacturacion();
		if(!result.hasErrors()){
		
			if(accion.equals("NUEVO")){
				loteFacturacionFormulario.setEstado("Pendiente");
				loteFacturacionFormulario.setUsuarioRegistro(obtenerUser());
			}
			else
			{
				
				loteFacturacionFormulario = loteFacturacionService.obtenerPorId(loteFacturacion.getId());
				//Setear estado
				if("Pendiente".equalsIgnoreCase(loteFacturacion.getEstado())){
					loteFacturacionFormulario.setEstado("Pendiente");		
				}else if("Anulado".equalsIgnoreCase(loteFacturacion.getEstado())){
					loteFacturacionFormulario.setEstado("Anulado");
				}else if("Facturado".equalsIgnoreCase(loteFacturacion.getEstado())){
					loteFacturacionFormulario.setEstado("Facturado");
				}else{
					loteFacturacionFormulario.setEstado("Pendiente");
				}
				//setear usuario que esta modificando
				loteFacturacionFormulario.setUsuarioModificacion(obtenerUser());
				//setear fecha en la que se esta modificando
				loteFacturacionFormulario.setFechaModificacion(new Date());
				Set<PreFactura> loteFacturacionDetallesViejos = new HashSet<PreFactura>(preFacturaService.listarPreFacturasPorLoteFacturacion(loteFacturacionFormulario, obtenerClienteAspUser()));
				session.setAttribute("loteFacturacionDetallesViejos", loteFacturacionDetallesViejos);
			}
						
			//empezamos a setear los datos nuevos,
			loteFacturacionFormulario.setId(loteFacturacion.getId());
			loteFacturacionFormulario.setClienteAsp(obtenerClienteAspUser());
			
			setData(loteFacturacionFormulario, loteFacturacion);
	

			@SuppressWarnings("unchecked")
			Set<PreFactura> loteFacturacionDetallesViejos = (HashSet<PreFactura>)session.getAttribute("loteFacturacionDetallesViejos");
			if(accion == null || accion.equals("") || accion.equals("NUEVO")){
				loteFacturacionFormulario.setNumero(loteFacturacionService.traerUltNumero(loteFacturacionFormulario, obtenerClienteAspUser())+1L);
			}
			commit = loteFacturacionService.guardarActualizarLoteFacturacionYDetalles(loteFacturacionDetallesViejos, loteFacturacionFormulario, listaConceptosActualizar);
			//commit = loteFacturacionService.actualizarLoteFacturacionYDetalles(loteFacturacionDetallesViejos, loteFacturacionFormulario)
		
			if(commit == null || !commit)
				loteFacturacion.setId(loteFacturacionFormulario.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("loteFacturacionFormulario", loteFacturacion);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioLoteFacturacion(session,atributos, accion, loteFacturacion.getId()!= null ? loteFacturacion.getId().toString() : null, null, null, null, null);
		}	
		
		if(result.hasErrors()){
			atributos.put("loteFacturacionFormulario", loteFacturacion);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");
			return precargaFormularioLoteFacturacion(session,atributos, accion, loteFacturacion.getId()!= null ? loteFacturacion.getId().toString() : null, null, null, null, null);
			
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioLoteFacturacion.notificacion.loteFacturacionRegistrado", null);
			avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);	
		}
		
		session.removeAttribute("loteFacturacionDetallesSession");
		session.removeAttribute("loteFacturacionDetallesViejos");
		session.removeAttribute("conceptosSession");
		session.removeAttribute("conceptoOperacionClienteBusqueda");
		session.removeAttribute("conceptosSeleccionados");
		session.removeAttribute("conceptosAsignados");
		session.removeAttribute("loteFacturacionSession");
		atributos.remove("loteFacturacionFormulario");
		//loteFacturacionDetalles = null;
		//hacemos el redirect
		return listaLoteFacturacionController.mostrarLoteFacturacion(session, atributos, null, null, null, null, null, null, request);
		//return iniciarPrecargaFormularioLoteFacturacion(session, atributos, null,null );
	}
	
	@RequestMapping(
			value="/eliminarPreFactura.html",
			method= RequestMethod.GET
		)
	public String eliminarPreFactura(
			@RequestParam(value="accionPreFactura",required=false) String accionPreFactura,
			@RequestParam(value="ordenPreFactura",required=false) Long orden,			
			HttpSession session,
			Map<String,Object> atributos){
		
		@SuppressWarnings("unchecked")
		List<PreFactura> listaPreFacturas = (List<PreFactura>) session.getAttribute("listaPreFacturasSession");
		if(listaPreFacturas == null){
			listaPreFacturas = new ArrayList<PreFactura>();
		}
		
		if(listaPreFacturas != null && listaPreFacturas.size()>0 && orden!=null)
		{
			for(int i = 0;i<listaPreFacturas.size();i++){
				
				if(listaPreFacturas.get(i).getOrden().longValue() == orden.longValue()){
					listaPreFacturas.remove(i);
					i--;
				}
			}
		}
		
		session.setAttribute("listaPreFacturasSession", listaPreFacturas);
		atributos.put("listaPreFacturas", listaPreFacturas);
		
		return precargaFormularioLoteFacturacion(session, atributos, accionPreFactura, null, null, null, null, null);
	}
	
	@RequestMapping(
			value="/generarConceptosMensualesLoteFacturacion.html",
			method= RequestMethod.GET
		)
	public String generarConceptosMensualesLoteFacturacion(
			HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="periodo",required=false) String periodo,
			@RequestParam(value="anoPeriodo",required=false) String anoPeriodo,
			@RequestParam(value="codigoEmpresa",required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal",required=false) String codigoSucursal,
			@RequestParam(value="fechaRegistro",required=false) String fechaRegistro,
			@RequestParam(value="fechaFacturacion",required=false) String fechaFacturacion,
			@RequestParam(value="descripcion",required=false) String descripcion,
			@RequestParam(value="id",required=false) Long id){
		
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		
			@SuppressWarnings("unchecked")
			List<ConceptoOperacionCliente> listaConceptosActualizar = (List<ConceptoOperacionCliente>) session.getAttribute("listaConceptosSession");
			if(listaConceptosActualizar == null){
				listaConceptosActualizar = new ArrayList<ConceptoOperacionCliente>();
			}
			
			@SuppressWarnings("unchecked")
			List<PreFactura> listaPreFacturas = (List<PreFactura>) session.getAttribute("listaPreFacturasSession");
			if(listaPreFacturas == null){
				listaPreFacturas = new ArrayList<PreFactura>();
			}
		
			LoteFacturacion loteFacturacionFormulario = (LoteFacturacion) session.getAttribute("loteFacturacionSession");
			if(loteFacturacionFormulario == null){
				loteFacturacionFormulario = new LoteFacturacion();
			}
			
			loteFacturacionFormulario.setPeriodo(periodo);
			loteFacturacionFormulario.setAnoPeriodo(anoPeriodo);
			loteFacturacionFormulario.setCodigoEmpresa(codigoEmpresa);
			loteFacturacionFormulario.setCodigoSucursal(codigoSucursal);
			loteFacturacionFormulario.setDescripcion(descripcion);
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			if(fechaRegistro != null && !fechaRegistro.trim().equals("")){
				try {loteFacturacionFormulario.setFechaRegistro(sd.parse(fechaRegistro));} 
				catch (ParseException e) {e.printStackTrace();}
			}
			if(fechaFacturacion != null && !fechaFacturacion.trim().equals("")){
				try {loteFacturacionFormulario.setFechaFacturacion(sd.parse(fechaFacturacion));} 
				catch (ParseException e) {e.printStackTrace();}
			}
			
	
			if(codigoEmpresa!= null && !codigoEmpresa.trim().equals("")){
				
				if(periodo!=null && !periodo.trim().equals(""))
				{
					
					LoteFacturacion existenteMismoMes = loteFacturacionService.verificarExistenteMismoPeriodo(loteFacturacionFormulario, obtenerClienteAspUser());
						
					//Preguntamos si ya existe un lote en el mismo periodo elegido o si existe si es diferente al actual
					if(existenteMismoMes==null || (id!=null && existenteMismoMes.getId().longValue() == id.longValue())){
						
						//Si el lote actual ya esta guardado
						if(id!=null)
						{
							for(PreFactura preFactura:listaPreFacturas)
							{
								//Recorremos sus prefacturas y las eliminamos de la base
								//junto con los detalles de cada prefactura y los conceptosOperacionClientes
								//para crearlas luego de cero
								preFacturaService.eliminarPreFactura(preFactura);
							}
						}
						
						//Ponemos en blanco las dos listas utilizadas
						listaConceptosActualizar = new ArrayList<ConceptoOperacionCliente>();
						listaPreFacturas = new ArrayList<PreFactura>();
						
						
						List<PlantillaFacturacion> plantillasFacturacion =	plantillaFacturacionService.listarPlantillasClientesEnPeriodo(periodo, codigoEmpresa, null, obtenerClienteAspUser());	

						if (plantillasFacturacion != null && plantillasFacturacion.size() > 0) {
							
							
							listaPreFacturas = generarPreFacturasMensuales(plantillasFacturacion, periodo,listaConceptosActualizar,loteFacturacionFormulario);

							if (listaPreFacturas != null && listaPreFacturas.size() > 0) {
								
//								for (PreFactura preFactura : listaPreFacturas) {
//									LoteFacturacionDetalle loteFacturacionDetalle = new LoteFacturacionDetalle();
//									//loteFacturacionDetalle.setConceptoOperacionCliente(preFactura);
//									loteFacturacionDetalles.add(loteFacturacionDetalle);
//								}
								ScreenMessage mensajeConceptosGen = new ScreenMessageImp("formularioLoteFacturacion.notificacion.conceptosMensualesGenerados", null);
								avisos.add(mensajeConceptosGen); //agrego el mensaje a la coleccion
								atributos.put("hayAvisos", true);
								atributos.put("avisos", avisos);
								session.setAttribute("listaPreFacturasSession",listaPreFacturas);
								session.setAttribute("listaConceptosSession", listaConceptosActualizar);
							}
						
							
						}
						else
						{
							ScreenMessage nada = new ScreenMessageImp("formularioLoteFacturacion.notificacion.plantillasNoEncontradas", null);
							avisos.add(nada); //agrego el mensaje a la coleccion
							atributos.put("hayAvisos", true);
							atributos.put("avisos", avisos);
						}
					}	
					else
					{
						ScreenMessage error = new ScreenMessageImp("formularioLoteFacturacion.errorLoteExistentePeriodo", null);
						avisos.add(error); //agrego el mensaje a la coleccion
						atributos.put("hayAvisosNeg", true);
						atributos.put("avisos", avisos);	
					}
				}
				else
				{
					ScreenMessage error = new ScreenMessageImp("formularioLoteFacturacion.errorPeriodo", null);
					avisos.add(error); //agrego el mensaje a la coleccion
					atributos.put("hayAvisosNeg", true);
					atributos.put("avisos", avisos);
				}
			}
			else
			{
				ScreenMessage error = new ScreenMessageImp("formularioLoteFacturacion.errorEmpresa", null);
				avisos.add(error); //agrego el mensaje a la coleccion
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
			}
	
		session.setAttribute("listaPreFacturasSession", listaPreFacturas);	
		atributos.put("accion",accion);	
		return precargaFormularioLoteFacturacion(session,atributos, accion, null, codigoEmpresa, codigoSucursal, null, periodo);
	}

////////////////////////////////////////////METODOS DE SOPORTE////////////////////////////////////////////////////////////
	
	private void setData(LoteFacturacion loteFacturacionFormulario, LoteFacturacion loteFacturacion){
		if(loteFacturacion != null){			
			loteFacturacionFormulario.setFechaRegistro(loteFacturacion.getFechaRegistro());
			loteFacturacionFormulario.setPeriodo(loteFacturacion.getPeriodo());
			loteFacturacionFormulario.setAnoPeriodo(loteFacturacion.getAnoPeriodo());
			loteFacturacionFormulario.setFechaFacturacion(loteFacturacion.getFechaFacturacion());
			loteFacturacionFormulario.setDescripcion(loteFacturacion.getDescripcion());
			loteFacturacionFormulario.setEmpresa(loteFacturacion.getEmpresa());
			loteFacturacionFormulario.setSucursal(loteFacturacion.getSucursal());
			loteFacturacionFormulario.setDetalles(loteFacturacion.getDetalles());
			loteFacturacionFormulario.setCantidadConceptos(loteFacturacion.getDetalles().size());
		}
	}

	
//	private List<ConceptoOperacionCliente> generarConceptosMensuales(List<ClienteConcept> clientesConceptos, String periodo){
//		
//		List<ConceptoOperacionCliente> listaConceptosMensuales = new ArrayList<ConceptoOperacionCliente>();
//		//Se recorre la lista de clientesConceptos obtenida
//		for(int i = 0;i<clientesConceptos.size();i++){
//			
//			//A cada clienteConcepto se le setea la cantidad de elementos que existentes que generanCanon y que tiene conceptoFacturable
//			Integer cantidad = elementoService.cantidadElementosPorConceptoFacturable(clientesConceptos.get(i).getIdClienteEmp(), clientesConceptos.get(i).getIdConceptoFacturable(), true, obtenerClienteAspUser());
//			if(cantidad != null && cantidad >0){
//					
//					clientesConceptos.get(i).setCantidad(cantidad);
//					
//					//Creamos una cadena con los meses facturables y la pasamos una lista de numeros
//					String[] cadenaMesesFacturables = clientesConceptos.get(i).mesesFacturables.split("\\,");
//					List<Long> listaMeses = new ArrayList<Long>();
//					for(int f = 0; f<cadenaMesesFacturables.length;f++)
//					{
//						listaMeses.add(Long.valueOf(cadenaMesesFacturables[f]));
//					}
//					
//					//Obtenemos la posicion del periodo del lote en la lista de meses
//					int posicionPeriodo = listaMeses.indexOf(Long.valueOf(periodo));
//					int cantidadConceptosACrear = 1;
//					//Si tiene mas de un mes
//					if(posicionPeriodo>0){
//						//Restamos el actual periodo con el anterior facturado para saber cuantos conceptos intermedios crear
//						Long periodoAnteriorFacturado = listaMeses.get(posicionPeriodo-1); 
//						cantidadConceptosACrear = (Integer.valueOf(periodo)) - periodoAnteriorFacturado.intValue();
//					}
//					//Creamos la cantidad obtenida de conceptos y detalles del lote para agregar
//					for(int g=0;g<cantidadConceptosACrear;g++)
//					{
//						ConceptoOperacionCliente conceptoMensual = new ConceptoOperacionCliente();
//						clientesConceptos.get(i).setMesConcepto(Integer.valueOf(periodo)-g);
//						clientesConceptos.get(i).setPeriodo(periodo);
//						conceptoMensual = calcularConceptoOperacionCliente(conceptoMensual, clientesConceptos.get(i));
//						listaConceptosMensuales.add(conceptoMensual);
//					}	
//			}
//			
//		}
//		
//		return listaConceptosMensuales;
//	}
	
		private List<PreFactura> generarPreFacturasMensuales(List<PlantillaFacturacion> plantillasFacturacion, String periodo, List<ConceptoOperacionCliente> listaConceptosActualizar,LoteFacturacion loteFacturacion){
		
		List<PreFactura> listaPreFacturas = new ArrayList<PreFactura>();
		Integer orden = 0;
		//Se recorre la lista de plantillas de facturacion obtenidas
		for(int i = 0;i<plantillasFacturacion.size();i++){
		
		Integer ordenDetalle= 0;	
			
			List<PreFacturaDetalle> listaPreFacturasDetalles = new ArrayList<PreFacturaDetalle>();
			
			//Se trae una lista de conceptos para facturar del cliente de la plantilla
			ConceptoOperacionCliente conceptoOperacionCliente = new ConceptoOperacionCliente();
			conceptoOperacionCliente.setCodigoCliente(plantillasFacturacion.get(i).getClienteEmp().getCodigo());
			conceptoOperacionCliente.setAsignado(false);
			conceptoOperacionCliente.setEstado("Pendiente");
			conceptoOperacionCliente.setCodigoEmpresa(obtenerEmpresaUser().getCodigo());
			List<ConceptoOperacionCliente> listaConceptos = conceptoOperacionClienteService.conceptoOperacionClienteFiltradas(
					conceptoOperacionCliente, obtenerClienteAspUser());
			
			
			//Se trae para cada plantilla la lista de sus detalles
			List<PlantillaFacturacionDetalle> listaPlantillaDetalles = plantillaFacturacionDetalleService.listarPlantillaDetallesPorPlantilla(
					plantillasFacturacion.get(i).getId(), obtenerClienteAspUser());
			
			//Se recorren los detalles
			for(PlantillaFacturacionDetalle detalle:listaPlantillaDetalles){
				
				
				
				if(detalle.getConceptoFacturable()!=null){
					//Se busca para cada concepto de los detalles si es un concepto por guarda
					Long cantidad = elementoService.cantidadElementosPorConceptoFacturable(
							plantillasFacturacion.get(i).getClienteEmp().getId(), detalle.getConceptoFacturable().getId(), 
							true, obtenerClienteAspUser());
					if(cantidad != null && cantidad >0)
					{
						
						//Creamos una cadena con los meses facturables y la pasamos a una lista de numeros
						String[] cadenaMesesFacturables = plantillasFacturacion.get(i).getClienteEmp().getMesesFacturables().split("\\,");
						List<Long> listaMeses = new ArrayList<Long>();
						for(int f = 0; f<cadenaMesesFacturables.length;f++)
						{
							listaMeses.add(Long.valueOf(cadenaMesesFacturables[f]));
						}
						
						//Obtenemos la posicion del periodo del lote en la lista de meses
						int posicionPeriodo = listaMeses.indexOf(Long.valueOf(periodo));
						int cantidadConceptosACrear = 1;
						//Si tiene mas de un mes
						if(posicionPeriodo>0){
							//Restamos el actual periodo con el anterior facturado para saber cuantos conceptos intermedios crear
							Long periodoAnteriorFacturado = listaMeses.get(posicionPeriodo-1); 
							cantidadConceptosACrear = (Integer.valueOf(periodo)) - periodoAnteriorFacturado.intValue();
						}
						//Creamos la cantidad obtenida de conceptos y detalles del lote para agregar
						for(int g=0;g<cantidadConceptosACrear;g++)
						{	
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
							SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
							Integer mes = (Integer.valueOf(periodo)) - g;
							String fechaAlta = "01/"+mes+"/"+sdf.format(loteFacturacion.getFechaRegistro());
							PreFacturaDetalle preFacturaDetalle = new PreFacturaDetalle();
							preFacturaDetalle.setCantidad(cantidad);
							preFacturaDetalle = calcularPreFacturaDetalle(preFacturaDetalle, null, detalle, plantillasFacturacion.get(i));
							
							//Creamos el conceptoOperacionCliente mensual correspondiente
							ConceptoOperacionCliente concepto = new ConceptoOperacionCliente();
							concepto.setCantidad(cantidad.longValue());
							concepto.setAsignado(true);
							concepto.setClienteAsp(obtenerClienteAspUser());
							concepto.setClienteEmp(plantillasFacturacion.get(i).getClienteEmp());
							concepto.setEstado("Pendiente");
							concepto.setUsuario(obtenerUser());
							concepto.setTipoConcepto("Mensual");
							concepto.setConceptoFacturable(preFacturaDetalle.getConceptoFacturable());
							concepto.setListaPrecios(preFacturaDetalle.getListaprecios());
							concepto.setCosto(preFacturaDetalle.getCosto());
							concepto.setDescripcion(preFacturaDetalle.getDescripcion());
							try {
								concepto.setFechaAlta(sd.parse(fechaAlta));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							concepto.setNetoTotal(preFacturaDetalle.getNetoTotal());
							concepto.setNetoUnitario(preFacturaDetalle.getNetoUnitario());
							concepto.setFinalTotal(preFacturaDetalle.getFinalTotal());
							concepto.setFinalUnitario(preFacturaDetalle.getFinalUnitario());
							concepto.setImpuestos(preFacturaDetalle.getImpuestoTotal());
							concepto.setEmpresa(loteFacturacion.getEmpresa());
							concepto.setSucursal(loteFacturacion.getSucursal());
							
							List<ConceptoOperacionCliente> listaConceptosMensuales = new ArrayList<ConceptoOperacionCliente>();
						
							ordenDetalle++;
							preFacturaDetalle.setOrden(ordenDetalle);
							//Agregamos el concepto a la lista
							listaConceptosMensuales.add(concepto);
							//Le seteamos la lista a la prefactura
							preFacturaDetalle.setListaConceptosAsociados(listaConceptosMensuales);
							//Se agrega el detalle a la lista de prefacturasDetalles
							listaPreFacturasDetalles.add(preFacturaDetalle);
						}
					}
					//Sino es por Guarda, se buscara en la lista de conceptos para facturacion
					else{
						

						//Se crea una lista donde se agregaran los conceptos a facturar
						List<ConceptoOperacionCliente> listaAFacturar = new ArrayList<ConceptoOperacionCliente>();
						
						Boolean existe = false;
						//Se recorre la lista de conceptos
						for(ConceptoOperacionCliente concepto:listaConceptos){
							
							//Se busca los conceptos que todavia no han sido asignados
							if(concepto.getAsignado()!=true){
								
								//Se busca los conceptos del mismo tipo del detalle de la plantilla
								if(concepto.getConceptoFacturable().getId().longValue() 
										== detalle.getConceptoFacturable().getId().longValue()){
									
									//Cuando se encuentra uno se le setea como asignado
									concepto.setAsignado(true);
									concepto.setEstado("Pendiente");
									existe = true;
									//Se agrega el concepto a la lista a facturar
									listaAFacturar.add(concepto);
									//Se agrega el concepto a la lista de conceptos a actualizar en base
									listaConceptosActualizar.add(concepto);
								}
								
							}
							
						}
						if(existe == false){
							ConceptoOperacionCliente concepto = new ConceptoOperacionCliente();
							concepto.setCantidad(new Long(0));
							concepto.setListaPrecios(plantillasFacturacion.get(i).getListaPrecios());
							concepto.setConceptoFacturable(detalle.getConceptoFacturable());
							concepto.setCosto(detalle.getConceptoFacturable().getCosto());
							concepto.setFinalUnitario(new BigDecimal(0.0));
							concepto.setFinalTotal(new BigDecimal(0.0));
							concepto.setNetoTotal(new BigDecimal(0.0));
							concepto.setNetoUnitario(new BigDecimal(0.0));
							concepto.setImpuestos(new BigDecimal(0.0));
							concepto.setEmpresa(loteFacturacion.getEmpresa());
							concepto.setSucursal(loteFacturacion.getSucursal());
							//Se agrega el concepto a la lista a facturar
							listaAFacturar.add(concepto);
						}
						
						List<PreFacturaDetalle> listaPreFacturasASumarizar = new ArrayList<PreFacturaDetalle>();
						
						//Se recorre la lista de conceptos a facturar del mismo tipo y se crean prefacturasDetalles con cada uno
						for(int f = 0;f<listaAFacturar.size();f++){	
							ConceptoOperacionCliente concepto = listaAFacturar.get(f);
							//Si es el primero de la lista, le resto la cantidadSinCosto especificada
							if(f==0){
								Long cantidadFinal = concepto.getCantidad() - detalle.getCantidadSinCosto();
								if(cantidadFinal<0)
									cantidadFinal=new Long(0);
								concepto.setCantidad(cantidadFinal);
								concepto.setNetoTotal(concepto.getNetoUnitario().multiply(new BigDecimal(cantidadFinal)));
								concepto.setFinalTotal(concepto.getFinalUnitario().multiply(new BigDecimal(cantidadFinal)));
								concepto.setImpuestos((concepto.getFinalUnitario().subtract(concepto.getNetoUnitario()).multiply(BigDecimal.valueOf(cantidadFinal))));	
							}
							PreFacturaDetalle preFacturaDetalle = new PreFacturaDetalle();
							preFacturaDetalle = obtenerPreFacturaDetalle(preFacturaDetalle, concepto, detalle, plantillasFacturacion.get(i));
							listaPreFacturasASumarizar.add(preFacturaDetalle);
						}
						
						//Se manda la lista de preFacturas generadas a sumarizar y se crea la preFacturaDetalle final
						PreFacturaDetalle detalleSumarizado = new PreFacturaDetalle();
						detalleSumarizado = sumarizarPreFacturas(listaPreFacturasASumarizar);
						detalleSumarizado.setDescripcion(detalle.getDescripcionConcepto());
						detalleSumarizado.setListaprecios(plantillasFacturacion.get(i).getListaPrecios());
						detalleSumarizado.setEstado("Pendiente");
						
						//Se la agrega a la lista de prefacturasDetalles
						ordenDetalle++;
						detalleSumarizado.setOrden(ordenDetalle);
						detalleSumarizado.setListaConceptosAsociados(listaAFacturar);
						listaPreFacturasDetalles.add(detalleSumarizado);
								
					}
				}
				//Entonces es una linea sin concepto de comentario
				else{
					
					PreFacturaDetalle detalleSinConcepto = new PreFacturaDetalle();
					detalleSinConcepto.setDescripcion(detalle.getDescripcionConcepto());
					detalleSinConcepto.setEstado("Pendiente");					
					detalleSinConcepto.setNetoTotal(BigDecimal.valueOf(0D));
					detalleSinConcepto.setImpuestoTotal(BigDecimal.valueOf(0D));
					detalleSinConcepto.setFinalTotal(BigDecimal.valueOf(0D));
					
					//Se la agrega a la lista de prefacturasDetalles
					ordenDetalle++;
					detalleSinConcepto.setOrden(ordenDetalle);
					listaPreFacturasDetalles.add(detalleSinConcepto);
				}
				
			}
			
			orden++;
			PreFactura preFactura = new PreFactura();
			preFactura.setOrden(orden);
			preFactura.setAfipTipoDeComprobante(plantillasFacturacion.get(i).getAfipTipoComprobante());
			preFactura.setClienteAsp(plantillasFacturacion.get(i).getClienteAsp());
			preFactura.setClienteEmp(plantillasFacturacion.get(i).getClienteEmp());
			preFactura.setEmpresa(plantillasFacturacion.get(i).getClienteEmp().getEmpresa());
			preFactura.setSucursal(obtenerSucursalUser());
			preFactura.setEstado("Pendiente");
			preFactura.setSerie(plantillasFacturacion.get(i).getSerie());
			preFactura.setIdAfipTipoComprobante(preFactura.getAfipTipoDeComprobante().getId());
			preFactura.setNotasFacturacion(plantillasFacturacion.get(i).getClienteEmp().getNotasFacturacion());
			preFactura.setDetalles(listaPreFacturasDetalles);
			calcularTotalesFactura(preFactura, listaPreFacturasDetalles);
			listaPreFacturas.add(preFactura);
			
			
		}
		
		return listaPreFacturas;
	}
	
	private PreFacturaDetalle calcularPreFacturaDetalle(PreFacturaDetalle actual, ConceptoOperacionCliente concepto,PlantillaFacturacionDetalle data,PlantillaFacturacion plantilla){
		
		PreFacturaDetalle preFacturaDetalle;
		Long cantidad = new Long(1); //Ponemos en el caso de que el tipo de calculo sea unico		
		if(concepto==null){
			if(actual.getCantidad() != null)
				cantidad = actual.getCantidad();
			if(cantidad==null)
				cantidad = new Long(1);
			
			preFacturaDetalle = actual;
		}else{
			if(concepto.getCantidad() != null)
				cantidad = Long.valueOf(concepto.getCantidad().toString());
			if(cantidad==null)
				cantidad = new Long(1);
			
			preFacturaDetalle = new PreFacturaDetalle();
			preFacturaDetalle.setCantidad(cantidad);
		}
		
		preFacturaDetalle.setListaprecios(plantilla.getListaPrecios());
		ConceptoFacturable conceptoFacturable = data.getConceptoFacturable();
		preFacturaDetalle.setConceptoFacturable(conceptoFacturable);
		preFacturaDetalle.setCosto(conceptoFacturable.getCosto());
		preFacturaDetalle.setEstado("Pendiente");

	
		ListaPrecios listaPrecios;
		if(concepto==null){
			listaPrecios = plantilla.getListaPrecios();
		}else{
			listaPrecios = concepto.getListaPrecios();
		}
		
		preFacturaDetalle.setListaprecios(listaPrecios);
		ListaPreciosDetalle detalleSeleccionado = null;
		for(ListaPreciosDetalle detalle:listaPrecios.getDetalle()){
			if(detalle.getConceptoFacturable().getCodigo().equals(conceptoFacturable.getCodigo())){
				detalleSeleccionado = detalle;
				break;}
		}
		
		preFacturaDetalle.setPrecioBase(detalleSeleccionado.getCalcularMonto());
		preFacturaDetalle.setDescripcion(data.getDescripcionConcepto());
		
		BigDecimal uno = BigDecimal.valueOf(1L);
		BigDecimal cien = BigDecimal.valueOf(100L);

		preFacturaDetalle.setCosto(conceptoFacturable.getCosto());
		preFacturaDetalle.setPrecioBase(conceptoFacturable.getPrecioBase());
		Impuesto impuesto = conceptoFacturable.getImpuesto();
		BigDecimal valor = listaPrecios.getValor();
		if (valor != null) {
			
			//////////////////////////////////////////////////////////////////////////////////
			////////////////METODO: EL PRECIO TIENE IMPUESTOS
			//					BigDecimal netoUnitario = variacionPrecio.multiply(concepto.getPrecioBase());
			//					facturaDetalle.setNetoUnitario(netoUnitario);
			//					BigDecimal variacionPrecio = uno.add(valor.divide(cien));
			//					BigDecimal finalUnitario  = variacionPrecio.multiply(concepto.getPrecioBase());
			//					facturaDetalle.setFinalUnitario(finalUnitario);
			//					facturaDetalle.setFinalTotal(finalUnitario.multiply(new BigDecimal(cantidad)));
			//					
			//					facturaDetalle.setIVA(impuesto != null ? impuesto.getAlicuota() : null);
			//					
			//					if (impuesto != null && impuesto.getAlicuota() != null) {
			//						
			//						facturaDetalle.setNetoUnitario(finalUnitario.divide(((impuesto.getAlicuota().divide(new BigDecimal(100))).add(new BigDecimal(1))), 4, RoundingMode.HALF_UP));
			//						facturaDetalle.setNetoTotal(facturaDetalle.getNetoUnitario().multiply(new BigDecimal(cantidad)));
			//						facturaDetalle.setImpuestoUnitario(finalUnitario.subtract(facturaDetalle.getNetoUnitario()));
			//						facturaDetalle.setImpuestoTotal((finalUnitario.subtract(facturaDetalle.getNetoUnitario()).multiply(BigDecimal.valueOf(cantidad))));
			//						
			//					}
			
			//////////////////////////////////////////////////////////////////////////////////
			////////////////METODO: EL PRECIO NO TIENE IMPUESTOS
			BigDecimal variacionPrecio = uno.add(valor.divide(cien));
			BigDecimal netoUnitario  = variacionPrecio.multiply(conceptoFacturable.getPrecioBase());
			preFacturaDetalle.setNetoUnitario(netoUnitario);
			preFacturaDetalle.setNetoTotal(netoUnitario.multiply(new BigDecimal(cantidad)));
			
			preFacturaDetalle.setIVA(impuesto != null ? impuesto.getAlicuota() : null);
			
			if (impuesto != null && impuesto.getAlicuota() != null) {
				
				preFacturaDetalle.setFinalUnitario(netoUnitario.multiply(((impuesto.getAlicuota().divide(new BigDecimal(100))).add(new BigDecimal(1)))));
				preFacturaDetalle.setFinalTotal(preFacturaDetalle.getFinalUnitario().multiply(new BigDecimal(cantidad)));
				
				preFacturaDetalle.setImpuestoUnitario(preFacturaDetalle.getFinalUnitario().subtract(netoUnitario));
				preFacturaDetalle.setImpuestoTotal((preFacturaDetalle.getFinalUnitario().subtract(netoUnitario).multiply(BigDecimal.valueOf(cantidad))));
				
			}
		}
		
		return preFacturaDetalle;
	}
	
	private PreFacturaDetalle sumarizarPreFacturas(List<PreFacturaDetalle> listaPreFacturasDetalles){
				
		Long cantidad = (long) 0;
		BigDecimal costo = BigDecimal.valueOf(0D);
		BigDecimal netoUnitario = BigDecimal.valueOf(0D);
		BigDecimal impuestoUnitario = BigDecimal.valueOf(0D);	
		BigDecimal finalUnitario = BigDecimal.valueOf(0D);
		BigDecimal netoTotal = BigDecimal.valueOf(0D);
		BigDecimal impuestoTotal = BigDecimal.valueOf(0D);
		BigDecimal finalTotal = BigDecimal.valueOf(0D);
		
		for(PreFacturaDetalle detalle:listaPreFacturasDetalles){
			cantidad += detalle.getCantidad();
			costo = costo.add(detalle.getCosto());
			netoUnitario = netoUnitario.add(detalle.getNetoUnitario());
			impuestoUnitario = impuestoUnitario.add(detalle.getImpuestoUnitario());
			finalUnitario = finalUnitario.add(detalle.getFinalUnitario());
			netoTotal = netoTotal.add(detalle.getNetoTotal());
			impuestoTotal = impuestoTotal.add(detalle.getImpuestoTotal());
			finalTotal = finalTotal.add(detalle.getFinalTotal());
		}
		
		PreFacturaDetalle detalleSumarizado = new PreFacturaDetalle();	
		detalleSumarizado.setConceptoFacturable(listaPreFacturasDetalles.get(0).getConceptoFacturable());
		detalleSumarizado.setPrecioBase(detalleSumarizado.getConceptoFacturable().getPrecioBase());
		detalleSumarizado.setIVA(detalleSumarizado.getConceptoFacturable().getImpuesto().getAlicuota());
		detalleSumarizado.setCantidad(cantidad);
		detalleSumarizado.setCosto(costo);
		detalleSumarizado.setNetoUnitario(netoUnitario);
		detalleSumarizado.setImpuestoUnitario(impuestoUnitario);
		detalleSumarizado.setFinalUnitario(finalUnitario);
		detalleSumarizado.setNetoTotal(netoTotal);
		detalleSumarizado.setImpuestoTotal(impuestoTotal);
		detalleSumarizado.setFinalTotal(finalTotal);
		
		return detalleSumarizado;
	}
	
private PreFacturaDetalle obtenerPreFacturaDetalle(PreFacturaDetalle actual, ConceptoOperacionCliente concepto,PlantillaFacturacionDetalle data,PlantillaFacturacion plantilla){
		
		PreFacturaDetalle preFacturaDetalle;
		Long cantidad = new Long(1); //Ponemos en el caso de que el tipo de calculo sea unico		
		if(concepto==null){
			if(actual.getCantidad() != null)
				cantidad = actual.getCantidad();
			if(cantidad==null)
				cantidad = new Long(1);
			
			preFacturaDetalle = actual;
		}else{
			if(concepto.getCantidad() != null)
				cantidad = Long.valueOf(concepto.getCantidad().toString());
			if(cantidad==null)
				cantidad = new Long(1);
			
			preFacturaDetalle = new PreFacturaDetalle();
			preFacturaDetalle.setCantidad(cantidad);
		}
		
		ConceptoFacturable conceptoFacturable = concepto.getConceptoFacturable();
		preFacturaDetalle.setConceptoFacturable(conceptoFacturable);
		preFacturaDetalle.setCosto(conceptoFacturable.getCosto());
		preFacturaDetalle.setEstado("Pendiente");
		ListaPrecios listaPrecios;
		listaPrecios = concepto.getListaPrecios();
		preFacturaDetalle.setListaprecios(listaPrecios);
		
		preFacturaDetalle.setPrecioBase(conceptoFacturable.getPrecioBase());
		preFacturaDetalle.setDescripcion(data.getDescripcionConcepto());

		preFacturaDetalle.setCosto(concepto.getCosto());
		preFacturaDetalle.setPrecioBase(concepto.getPrecioBase());

		Impuesto impuesto = conceptoFacturable.getImpuesto();
			
						
			//////////////////////////////////////////////////////////////////////////////////
			////////////////METODO: EL PRECIO NO TIENE IMPUESTOS			
			preFacturaDetalle.setNetoUnitario(concepto.getNetoUnitario());
			preFacturaDetalle.setNetoTotal(concepto.getNetoTotal());
			
			preFacturaDetalle.setIVA(impuesto != null ? impuesto.getAlicuota() : null);
			
			if (impuesto != null && impuesto.getAlicuota() != null) {
				
				preFacturaDetalle.setFinalUnitario(concepto.getFinalUnitario());
				preFacturaDetalle.setFinalTotal(concepto.getFinalTotal());
				if(cantidad == 0){
					preFacturaDetalle.setImpuestoUnitario(new BigDecimal(0.0));
				}else{
					preFacturaDetalle.setImpuestoUnitario(concepto.getImpuestos().divide(new BigDecimal(cantidad), 4, RoundingMode.HALF_UP));
				}
				
				preFacturaDetalle.setImpuestoTotal(concepto.getImpuestos());
				
			
			}
		
		return preFacturaDetalle;
	}
	
	private void calcularTotalesFactura(PreFactura preFactura, List<PreFacturaDetalle> detalles){
		BigDecimal totalNeto = BigDecimal.valueOf(0D);
		BigDecimal totalImpuestos = BigDecimal.valueOf(0D);
		BigDecimal totalFinal = BigDecimal.valueOf(0D);
		
		for (PreFacturaDetalle detalle:detalles){
			totalNeto = totalNeto.add(detalle.getNetoTotal());
			totalImpuestos = totalImpuestos.add(detalle.getImpuestoTotal());
			totalFinal = totalFinal.add(detalle.getFinalTotal());
		}
		
		preFactura.setTotalNeto(totalNeto);
		preFactura.setTotalIVA(totalImpuestos);
		preFactura.setTotalFinal(totalFinal);
	}
	
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	private Empresa obtenerEmpresaUser(){
		return ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
	}
	
	private Sucursal obtenerSucursalUser(){
		return ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto();
	}
	
	
	
}
