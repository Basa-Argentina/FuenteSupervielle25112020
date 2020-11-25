package com.dividato.configuracionGeneral.controladores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.dividato.configuracionGeneral.validadores.ConceptoOperacionClienteBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteFacturacionDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.accesoDatos.jerarquias.interfaz.ConceptoOperacionClienteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.LoteFacturacion;
import com.security.modelo.configuraciongeneral.LoteFacturacionDetalle;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Remito.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Victor Kenis
 *
 */
@Controller
@RequestMapping(
		value=
			{	
				"/iniciarLoteFacturacionDetalle.html",
				"/mostrarLoteFacturacionDetalle.html",
				"/mostrarLoteFacturacionDetalleSinFiltrar.html",
//				"/eliminarLoteFacturacionDetalle.html",
				"/filtrarLoteFacturacionDetalle.html",
				"/borrarFiltrosLoteFacturacionDetalle.html",
				"/iniciarPopUpLoteFacturacionDetalle.html",
				"/guardarActualizarLoteFacturacionDetalle.html"
			}
		)
public class FormLoteFacturacionDetalleController {
	
	private ConceptoOperacionClienteService conceptoOperacionClienteService;
	private ConceptoOperacionClienteBusquedaValidator validator;
	private DepositoService depositoService;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	private SerieService serieService;
	private TransporteService transporteService;
	private ClienteEmpService clienteEmpService;
	private LoteFacturacionDetalleService loteFacturacionDetalleService;
	
	@Autowired
	public void setTransporteService(TransporteService transporteService) {
		this.transporteService = transporteService;
	}
	@Autowired
	public void setConceptoOperacionClienteService(ConceptoOperacionClienteService conceptoOperacionClienteService) {
		this.conceptoOperacionClienteService = conceptoOperacionClienteService;
	}
	@Autowired
	public void setValidator(ConceptoOperacionClienteBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@Autowired
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
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
	public void setLoteFacturacionDetalleService(LoteFacturacionDetalleService loteFacturacionDetalleService) {
		this.loteFacturacionDetalleService = loteFacturacionDetalleService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	
	@RequestMapping(
			value="/iniciarLoteFacturacionDetalle.html",
			method = RequestMethod.GET
		)
	public String iniciarLoteFacturacionDetalle(HttpSession session,
			Map<String,Object> atributos){
		
		session.removeAttribute("conceptoOperacionClienteBusqueda");
		session.removeAttribute("conceptosOperacionClienteSession");
		atributos.remove("conceptosOperacionCliente");
		
		return "redirect:mostrarConceptoOperacionClienteSinFiltrar.html";
	}
	
	@RequestMapping(
			value="/iniciarPopUpLoteFacturacionDetalle.html",
			method = RequestMethod.GET
		)
	public String iniciarPopUpLoteFacturacionDetalle(HttpSession session,
			Map<String,Object> atributos){

		
		return "popUpformularioLoteFacturacionDetalle";
	}
	
	@RequestMapping(
			value="/mostrarLoteFacturacionDetalleSinFiltrar.html",
			method = RequestMethod.GET
		)
	public String mostrarLoteFacturacionDetalleSinFiltrar(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="val", required=false) String valCliente,
			@RequestParam(value="val", required=false) String valSerie,
			@RequestParam(value="val", required=false) String valTransporte,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo){
		//buscamos en la base de datos y lo subimos a request.
		
		
		@SuppressWarnings("unchecked")
		List<ConceptoOperacionCliente> conceptosOperacionCliente = (List<ConceptoOperacionCliente>)session.getAttribute("conceptosOperacionClienteSession"); 
		if(conceptosOperacionCliente==null){
			conceptosOperacionCliente = new ArrayList<ConceptoOperacionCliente>();
		}
		
		ConceptoOperacionCliente conceptoOperacionCliente = (ConceptoOperacionCliente) session.getAttribute("conceptoOperacionClienteBusqueda");
		if(conceptoOperacionCliente == null){
			conceptoOperacionCliente = new ConceptoOperacionCliente();
			conceptoOperacionCliente.setCodigoEmpresa(obtenerEmpresaUser().getCodigo());
			conceptoOperacionCliente.setCodigoCliente(clienteCodigo);
		}
		
		session.setAttribute("conceptosOperacionClienteSession", conceptosOperacionCliente);
		atributos.put("conceptosOperacionCliente",conceptosOperacionCliente);
		
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("codigoEmpresa", obtenerEmpresaUser().getCodigo());

		//hacemos el forward
		return "consultaConceptoOperacionCliente";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de Remito y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaRemito" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarLoteFacturacionDetalle.html",
			method = RequestMethod.GET
		)
	public String mostrarLoteFacturacionDetalle(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion", required=false) String accion,
			@RequestParam(value="estado", required=false) String estado,
			@RequestParam(value="descripcion", required=false) String descripcion,
			@RequestParam(value="fechaFacturacion", required=false) String fechaFacturacion,
			@RequestParam(value="fechaRegistro", required=false) String fechaRegistro,
			@RequestParam(value="periodo", required=false) String periodo,
			@RequestParam(value="codigoEmpresa", required=false) String empresaCodigo,
			@RequestParam(value="codigoSucursal", required=false) String sucursalCodigo,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo){
		//buscamos en la base de datos y lo subimos a request.
		List listConceptosOperacionCliente = null;	
		
		
		LoteFacturacion loteFacturacionFormulario = (LoteFacturacion)session.getAttribute("loteFacturacionSession");
		//Si no existe se crea un loteFacturacion con los datos cargados en la pagina padre
		if(loteFacturacionFormulario==null)
		{
			loteFacturacionFormulario = new LoteFacturacion();
			loteFacturacionFormulario.setCodigoEmpresa(empresaCodigo);
			loteFacturacionFormulario.setCodigoSucursal(sucursalCodigo);
			loteFacturacionFormulario.setAccion(accion);
		}
			loteFacturacionFormulario.setPeriodo(periodo);
			SimpleDateFormat sdaf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				loteFacturacionFormulario.setFechaRegistro(sdaf.parse(fechaRegistro));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				loteFacturacionFormulario.setFechaFacturacion(sdaf.parse(fechaFacturacion));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		loteFacturacionFormulario.setEstado(estado);
		loteFacturacionFormulario.setDescripcion(descripcion);
		//Se guarda el loteFacturacion en session
		session.setAttribute("loteFacturacionSession", loteFacturacionFormulario);
			
		
		ConceptoOperacionCliente conceptoOperacionCliente = (ConceptoOperacionCliente) session.getAttribute("conceptoOperacionClienteBusqueda");
		//Se crea un concepto para realizar la busqueda y traer solo los conceptos validos para el loteFacturacion creado
		if(conceptoOperacionCliente == null){		
			//consulto en la base de datos
			ConceptoOperacionCliente concepto = new ConceptoOperacionCliente();
			concepto.setAsignado(false);
			concepto.setCodigoEmpresa(loteFacturacionFormulario.getCodigoEmpresa());
			concepto.setCodigoSucursal(loteFacturacionFormulario.getCodigoSucursal());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			String añoActual = sdf.format(new Date()) ;
			String per = loteFacturacionFormulario.getPeriodo();
			String fechaPeriodo = "31/12/"+añoActual;
			
			if(per.equals("1") || per.equals("3") || per.equals("5") || per.equals("7") || per.equals("8") ||
					per.equals("10") || per.equals("12"))
			{fechaPeriodo = "31/"+loteFacturacionFormulario.getPeriodo()+"/"+añoActual;}
			else if(per.equals("4") || per.equals("6") || per.equals("9") || per.equals("11"))
			{fechaPeriodo = "30/"+loteFacturacionFormulario.getPeriodo()+"/"+añoActual;}
			else if(per.equals("2"))
			{fechaPeriodo = "28/"+loteFacturacionFormulario.getPeriodo()+"/"+añoActual;}
			
			try {
				concepto.setFechaPeriodo(sd.parse(fechaPeriodo));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			concepto.setEstado("Pendiente");
			
			@SuppressWarnings("unchecked")
			List<Long> listaSeleccionados = (List<Long>) session.getAttribute("conceptosSeleccionados");
			@SuppressWarnings("unchecked")
			List<Long> listaAsignados = (List<Long>) session.getAttribute("conceptosAsignados");
			if(listaAsignados!=null && listaAsignados.size()>0)
			{
				if(listaSeleccionados!=null && listaSeleccionados.size()>0){
				listaAsignados.addAll(listaSeleccionados);
				session.removeAttribute("conceptosSeleccionados");
				}
			}
			else
			{
				listaAsignados = listaSeleccionados;
				session.removeAttribute("conceptosSeleccionados");
			}
			session.setAttribute("conceptosAsignados", listaAsignados);
			List<ConceptoOperCliente> conceptosOperacionCliente = null;
			//conceptosOperacionCliente = (List<ConceptoOperacionCliente>) conceptoOperacionClienteService.conceptoOperacionClienteFiltradas(listaAsignados,periodo,concepto, obtenerClienteAspUser());
			listConceptosOperacionCliente = (List<ConceptoOperacionCliente>) conceptoOperacionClienteService.obtenerConceptosClientesEnPeriodo(listaAsignados, loteFacturacionFormulario.getPeriodo(), fechaPeriodo, obtenerClienteAspUser());
			if(listConceptosOperacionCliente != null && listConceptosOperacionCliente.size() > 0){
				Long idReg = (Long) session.getAttribute("idRequerimientoElemento");
				conceptosOperacionCliente = obtenerLista(listConceptosOperacionCliente,idReg);
			}
			session.setAttribute("conceptosSession", conceptosOperacionCliente);
			atributos.put("detalles", conceptosOperacionCliente);
		}
		
		
		
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("loteFacturacionFormulario", loteFacturacionFormulario);

		
		//hacemos el forward
		return "formularioLoteFacturacionDetalle";
	}
	
	@RequestMapping(
			value="/filtrarLoteFacturacionDetalle.html",
			method = RequestMethod.POST
		)
	public String filtrarLoteFacturacionDetalle(@ModelAttribute("conceptoOperacionClienteBusqueda") ConceptoOperacionCliente conceptoOperacionCliente, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(conceptoOperacionCliente, result);
		if(!result.hasErrors()){
			session.setAttribute("conceptoOperacionClienteBusqueda", conceptoOperacionCliente);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		return mostrarLoteFacturacionDetalle(session, atributos, null,null, null, null, null, null,null, null, null);
	}
	
	
	@RequestMapping(
			value="/borrarFiltrosLoteFacturacionDetalle.html",
			method = RequestMethod.GET
		)
	public String borrarFiltrosLoteFacturacionDetalle(HttpSession session){
		session.removeAttribute("conceptoOperacionClienteBusqueda");
		return "redirect:mostrarConceptoOperacionCliente.html";
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos Elemento.
	 * 
	 * @param Elemento user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto Elemento con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioElemento" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarLoteFacturacionDetalle.html",
			method= RequestMethod.GET
		)
	public String guardarLoteFacturacionDetalle(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id", required=false) Long id,
			@RequestParam(value="fechaRegistro", required=false) String fechaRegistro,
			@RequestParam(value="fechaFacturacion", required=false) String fechaFacturacion,
			@RequestParam(value="periodo", required=false) String periodo,
			@RequestParam(value="estado", required=false) String estado,
			@RequestParam(value="descripcion", required=false) String descripcion,
			@RequestParam(value="codigoEmpresa", required=false) String codigoEmpresa,
			@RequestParam(value="seleccionados", required=false) String seleccionados,
			/*@ModelAttribute("loteFacturacionDetalleFormulario") LoteFacturacionDetalle loteFacturacionDetalleFormulario,*/
			/*BindingResult result,*/
			HttpSession session,
			Map<String,Object> atributos){
		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		ScreenMessage mensaje;
		
		@SuppressWarnings("unchecked")
		List <LoteFacturacionDetalle> loteFacturacionDetallesSession = (List <LoteFacturacionDetalle>)session.getAttribute("loteFacturacionDetallesSession");
		@SuppressWarnings("unchecked")
		List<ConceptoOperacionCliente> conceptosSession = (List<ConceptoOperacionCliente>) session.getAttribute("conceptosSession");
		
		if(loteFacturacionDetallesSession==null)
		{
			loteFacturacionDetallesSession = new ArrayList<LoteFacturacionDetalle> ();
		}
		
		if (accion == null || accion.equals("") || accion.equalsIgnoreCase("NUEVO"))
		{
			accion = "NUEVO";
			
			if(seleccionados != null && seleccionados.length()> 0)
			{
				
				String[] listaConceptosSeleccionados = seleccionados.split("\\,");
				List<Long> listaNumeros = new ArrayList<Long>();
				for(int i = 0; i<listaConceptosSeleccionados.length;i++)
				{
					listaNumeros.add(Long.valueOf(listaConceptosSeleccionados[i]));
				}
			
				List<ConceptoOperacionCliente> listaConceptos = conceptoOperacionClienteService.getByNumeros(listaNumeros, obtenerClienteAspUser());
			
				if(listaConceptos != null && listaConceptos.size()>0)
				{
					session.setAttribute("conceptosSeleccionados", listaNumeros);
					for(ConceptoOperacionCliente concepto: listaConceptos)
					{
						LoteFacturacionDetalle loteFacturacionDetalle = new LoteFacturacionDetalle();
						concepto.setAsignado(true);
						loteFacturacionDetalle.setConceptoOperacionCliente(concepto);
						loteFacturacionDetallesSession.add(loteFacturacionDetalle);
					}
				}
				
				mensaje = new ScreenMessageImp("formularioLoteFacturacion.notificacion.conceptosAsociados", null);
				hayAvisos = true;
				avisos.add(mensaje);
			}
		}
		else if(!"CONSULTA".equalsIgnoreCase(accion))
		{
			accion = "MODIFICACION";
		}
	session.setAttribute("loteFacturacionDetallesSession", loteFacturacionDetallesSession);

	atributos.put("loteFacturacionDetalles", loteFacturacionDetallesSession);
	
	
	atributos.put("hayAvisos", hayAvisos);
	atributos.put("avisos", avisos);
	atributos.put("codigoEmpresa", codigoEmpresa);
	atributos.put("detalles", conceptosSession);
	/*atributos.put("facturaDetalleFormulario", loteFacturacionDetalleFormulario);*/
	//hacemos el redirect
	//return "formularioLoteFacturacionDetalle";
	return mostrarLoteFacturacionDetalle(session, atributos, null, estado, descripcion, fechaFacturacion, fechaRegistro, periodo,null, null, null);
	}
	
	
//	/**
//	 * Observar la anotación @RequestMapping de SPRING.
//	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
//	 * 
//	 * Se encarga de eliminar Remito.
//	 * 
//	 * @param idRemito el id de Remito a eliminar.
//	 * (Observar la anotación @RequestParam)
//	 * @param atributos son los atributos del request
//	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
//	 */
//	@RequestMapping(
//			value="/eliminarLoteFacturacionDetalle.html",
//			method = RequestMethod.GET
//		)
//	public String eliminarLoteFacturacionDetalle(HttpSession session,
//			@RequestParam("id") String id,
//			Map<String,Object> atributos) {
//		Boolean commit = null;
//		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
//		boolean hayAvisos = false;
//		boolean hayAvisosNeg = false;
//		//Obtenemos el concepto para eliminar luego
//		ConceptoOperacionCliente conceptoOperacionCliente = conceptoOperacionClienteService.obtenerPorId(Long.valueOf(id));
//		ScreenMessage mensaje;
//		if(conceptoOperacionCliente!=null)
//		{
//			if(!conceptoOperacionCliente.getTipoConcepto().equals("Manual") || conceptoOperacionCliente.getEstado().equals("Facturado"))
//			{
//				mensaje = new ScreenMessageImp("formularioConceptoOperacionCliente.error.imposibleEliminar", null);
//				hayAvisosNeg = true;
//			}
//			else
//			{
//				//Eliminamos el concepto
//				commit = conceptoOperacionClienteService.eliminarConceptoOperacionCliente(conceptoOperacionCliente);
//				
//				//Controlamos su eliminacion.
//				if(commit){
//					mensaje = new ScreenMessageImp("formularioConceptoOperacionCliente.notif.conceptoEliminadoExito", null);
//					hayAvisos = true;
//				}else{
//					mensaje = new ScreenMessageImp("error.deleteDataBase", null);
//					hayAvisosNeg = true;
//				}
//			}
//			
//			avisos.add(mensaje);
//		}
//
//		atributos.put("hayAvisosNeg", hayAvisosNeg);
//		atributos.put("hayAvisos", hayAvisos);
//		atributos.put("avisos", avisos);
//		return mostrarLoteFacturacionDetalle(session, atributos, null,null, null, null, null, null, null, null, null);
//	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de imprimir los codigos de barra de los elementos en un reporte y subirlo al response.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaImpresionEtiqueta" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
//	@RequestMapping(
//			value="/imprimirRemito.html",
//			method = RequestMethod.GET
//		)
//	public void imprimirRemito(HttpSession session,
//			Map<String,Object> atributos,			
//			HttpServletResponse response,
//			@RequestParam(value="seleccionados", required=false) String seleccionados) {
//		
//		List<Remito> listaRemitos = new ArrayList<Remito>();
//		
//		if(seleccionados != null && seleccionados.length()> 0){
//			
//			String[] listaRemitosSeleccionados = seleccionados.split("\\,");
//			List<Long> listaNumeros = new ArrayList<Long>();
//			for(int i = 0; i<listaRemitosSeleccionados.length;i++)
//			{
//				listaNumeros.add(Long.valueOf(listaRemitosSeleccionados[i]));
//			}
//			
//			listaRemitos = remitoService.getByNumeros(listaNumeros, obtenerClienteAspUser());
//			
//			//buscamos en la base de datos y lo subimos a request.
//			String reportName = "C:\\jasper\\reporteImpresionRemito.jasper";
//			File file = new File(reportName);
//				
//			@SuppressWarnings("rawtypes")
//			HashMap params = new HashMap();
//			byte[] pdfByteArray ;
//			ServletOutputStream op = null;
//			if (file.exists()) {
//				try{
//					//se trae busca nuevamente la misma lista en la base pero esta vez con los detales en FetchMode.Join
//					List<Remito> remitosYDetalles = remitoService.listarRemitosPorId(listaRemitos, obtenerClienteAspUser());
//					//se crea el data source
//					JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(remitosYDetalles);
//					//se crea el reporte
//					pdfByteArray = JasperRunManager.runReportToPdf(reportName, params,ds);
//					//se envia el reporte 
//					response.setContentType("application/pdf");
//	                //response.setHeader( "Content-disposition", "attachment; filename=reporte.pdf");
//
//					op = null;
//					op = response.getOutputStream();
//					op.write(pdfByteArray, 0, pdfByteArray.length);
//					op.flush();
//					op.close();
//				} catch (IOException e) {
//					try {
//						op.close();
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//					e.printStackTrace();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//			}
//			
//		}
//		
//		//@SuppressWarnings("unchecked")
//		//List<Remito> remitos = (List<Remito>)session.getAttribute("remitosSession");
//				
//		//if(remitos==null){
//			//remitos=new ArrayList<Remito>();
//		//}
//		
//		
//		
//		
//	}
	
	
	/////////////////////METODOS DE SOPORTE/////////////////////////////
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
	
	@SuppressWarnings("unchecked")
	private List<ConceptoOperCliente> obtenerLista (List<Object> entrada, Long idReq){
		List <ConceptoOperCliente> salida = new ArrayList<ConceptoOperCliente>();
		for(Object ob:entrada){
			Object[] lista = (Object[]) ob;
			ConceptoOperCliente conceptoOperCliente = new ConceptoOperCliente((BigDecimal)lista[0], (String)lista[1],
					(String) lista[2], (Date) lista[3], (String) lista[4], (BigDecimal)lista[5], 
					(BigDecimal)lista[6], (BigDecimal)lista[7], (String)lista[8], (Byte)lista[9],(BigDecimal)lista[10]);
//			//Busco si ya fue asignado a otro requerimiento y si esta pendiente o en proceso
//			RequerimientoReferencia requerimientoReferencia = requerimientoReferenciaService.obtenerPendienteOEnProceso(requerimientoElemento.getIdReferencia(),idReq);
//			if(requerimientoReferencia == null)
//				requerimientoElemento.utilizado = false;
//			else
//				requerimientoElemento.utilizado = true;
			salida.add(conceptoOperCliente);
		}
		return salida;
	}
	
	public class ConceptoOperCliente{
		private Long idConcepto;
		private String razonCliente;
		private Date fechaAlta;
		private String tipoConcepto;
		private BigDecimal finalUnitario;
		private Long cantidad;
		private BigDecimal finalTotal;
		private String estado;
		private Boolean asignado;
		private BigDecimal requerimiento;
		
		public ConceptoOperCliente(BigDecimal id, String cliJur, String cliFis, Date fechaA, String tipoCon, BigDecimal finUni, BigDecimal cant, 
				BigDecimal finTotal, String estado, Byte asignado, BigDecimal req){
			if(id != null)
				this.idConcepto = Long.parseLong(id.toString());
			
			this.fechaAlta = fechaA;
			this.tipoConcepto = tipoCon;
			this.finalUnitario = finUni;
			this.cantidad = Long.parseLong(cant.toString());
			this.finalTotal = finTotal;
			this.estado = estado;
			this.requerimiento = req;
			if(cliJur != null)
				this.razonCliente = cliJur;
			else if(cliFis != null)
				this.razonCliente = cliFis;
			
		}
		
		public Long getIdConcepto() {
			return idConcepto;
		}

		public void setIdConcepto(Long idConcepto) {
			this.idConcepto = idConcepto;
		}

		public String getRazonCliente() {
			return razonCliente;
		}

		public void setRazonCliente(String razonCliente) {
			this.razonCliente = razonCliente;
		}

		public Date getFechaAlta() {
			return fechaAlta;
		}

		public void setFechaAlta(Date fechaAlta) {
			this.fechaAlta = fechaAlta;
		}

		public String getTipoConcepto() {
			return tipoConcepto;
		}

		public void setTipoConcepto(String tipoConcepto) {
			this.tipoConcepto = tipoConcepto;
		}

		public BigDecimal getFinalUnitario() {
			return finalUnitario;
		}

		public void setFinalUnitario(BigDecimal finalUnitario) {
			this.finalUnitario = finalUnitario;
		}

		public Long getCantidad() {
			return cantidad;
		}

		public void setCantidad(Long cantidad) {
			this.cantidad = cantidad;
		}

		public BigDecimal getFinalTotal() {
			return finalTotal;
		}

		public void setFinalTotal(BigDecimal finalTotal) {
			this.finalTotal = finalTotal;
		}

		public String getEstado() {
			return estado;
		}

		public void setEstado(String estado) {
			this.estado = estado;
		}

		public boolean isAsignado() {
			return asignado;
		}

		public void setAsignado(boolean asignado) {
			this.asignado = asignado;
		}

		public BigDecimal getRequerimiento() {
			return requerimiento;
		}

		public void setRequerimiento(BigDecimal requerimiento) {
			this.requerimiento = requerimiento;
		}

		public String getFechaAltaStr() {
			if(fechaAlta==null)
				return "";
			return formatoFechaFormularios.format(fechaAlta);
		}
		
	}
}