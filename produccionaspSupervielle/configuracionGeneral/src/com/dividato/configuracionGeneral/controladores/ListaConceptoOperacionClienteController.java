package com.dividato.configuracionGeneral.controladores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

import com.dividato.configuracionGeneral.controladores.jerarquias.ListaRequerimientoController;
import com.dividato.configuracionGeneral.controladores.jerarquias.ListaRequerimientoController.requerimientoReporte;
import com.dividato.configuracionGeneral.validadores.ConceptoOperacionClienteBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.accesoDatos.jerarquias.interfaz.ConceptoOperacionClienteService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.modelo.jerarquias.Requerimiento;
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
				"/iniciarConceptoOperacionCliente.html",
				"/mostrarConceptoOperacionCliente.html",
				"/mostrarConceptoOperacionClienteSinFiltrar.html",
				"/eliminarConceptoOperacionCliente.html",
				"/filtrarConceptoOperacionCliente.html",
				"/borrarFiltrosConceptoOperacionCliente.html",
				"/exportarConceptoOperacionClientePdf.html"
			}
		)
public class ListaConceptoOperacionClienteController {
	
	private ConceptoOperacionClienteService conceptoOperacionClienteService;
	private ConceptoOperacionClienteBusquedaValidator validator;
	private DepositoService depositoService;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	private SerieService serieService;
	private TransporteService transporteService;
	private ClienteEmpService clienteEmpService;
	
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
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	
	@RequestMapping(
			value="/iniciarConceptoOperacionCliente.html",
			method = RequestMethod.GET
		)
	public String iniciarConceptoOperacionCliente(HttpSession session,
			Map<String,Object> atributos){
		
		session.removeAttribute("conceptoOperacionClienteBusqueda");
		session.removeAttribute("conceptosOperacionClienteSession");
		atributos.remove("conceptosOperacionCliente");
		
		return "redirect:mostrarConceptoOperacionClienteSinFiltrar.html";
	}
	
	@RequestMapping(
			value="/mostrarConceptoOperacionClienteSinFiltrar.html",
			method = RequestMethod.GET
		)
	public String mostrarConceptoOperacionClienteSinFiltrar(HttpSession session,
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
			value="/mostrarConceptoOperacionCliente.html",
			method = RequestMethod.GET
		)
	public String mostrarConceptoOperacionCliente(HttpSession session,
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
		List <ConceptoOperacionCliente> conceptosOperacionCliente = null;	
		
		ConceptoOperacionCliente conceptoOperacionCliente = (ConceptoOperacionCliente) session.getAttribute("conceptoOperacionClienteBusqueda");
		if(conceptoOperacionCliente != null){		
			//consulto en la base de datos
			conceptosOperacionCliente =(List<ConceptoOperacionCliente>) conceptoOperacionClienteService.conceptoOperacionClienteFiltradas(conceptoOperacionCliente, obtenerClienteAspUser());
		}
		
		session.setAttribute("conceptosOperacionClienteSession", conceptosOperacionCliente);
		atributos.put("conceptosOperacionCliente", conceptosOperacionCliente);
		
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("codigoEmpresa", obtenerEmpresaUser().getCodigo());
		
		//hacemos el forward
		return "consultaConceptoOperacionCliente";
	}
	
	@RequestMapping(
			value="/filtrarConceptoOperacionCliente.html",
			method = RequestMethod.POST
		)
	public String filtrarConceptoOperacionCliente(@ModelAttribute("conceptoOperacionClienteBusqueda") ConceptoOperacionCliente conceptoOperacionCliente, 
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
		return mostrarConceptoOperacionCliente(session, atributos, null, null, null, null, null, null,null, null, null);
	}
	
	
	@RequestMapping(
			value="/borrarFiltrosConceptoOperacionCliente.html",
			method = RequestMethod.GET
		)
	public String borrarFiltrosConceptoOperacionCliente(HttpSession session){
		session.removeAttribute("conceptoOperacionClienteBusqueda");
		return "redirect:mostrarConceptoOperacionCliente.html";
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Remito.
	 * 
	 * @param idRemito el id de Remito a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarConceptoOperacionCliente.html",
			method = RequestMethod.GET
		)
	public String eliminarConceptoOperacionCliente(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos el concepto para eliminar luego
		ConceptoOperacionCliente conceptoOperacionCliente = conceptoOperacionClienteService.obtenerPorId(Long.valueOf(id));
		ScreenMessage mensaje;
		if(conceptoOperacionCliente!=null)
		{
			if(!conceptoOperacionCliente.getTipoConcepto().equals("Manual") || conceptoOperacionCliente.getEstado().equals("Facturado"))
			{
				mensaje = new ScreenMessageImp("formularioConceptoOperacionCliente.error.imposibleEliminar", null);
				hayAvisosNeg = true;
			}
			else
			{
				//Eliminamos el concepto
				commit = conceptoOperacionClienteService.eliminarConceptoOperacionCliente(conceptoOperacionCliente);
				
				//Controlamos su eliminacion.
				if(commit){
					mensaje = new ScreenMessageImp("formularioConceptoOperacionCliente.notif.conceptoEliminadoExito", null);
					hayAvisos = true;
				}else{
					mensaje = new ScreenMessageImp("error.deleteDataBase", null);
					hayAvisosNeg = true;
				}
			}
			
			avisos.add(mensaje);
		}

		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarConceptoOperacionCliente(session, atributos, null, null, null, null, null, null, null, null, null);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/exportarConceptoOperacionClientePdf.html",method = RequestMethod.GET)
	public void exportarPdf(HttpSession session,
			Map<String,Object> atributos,HttpServletResponse response) {
		try{
			Map<String,Object> parametros=new HashMap<String,Object>();
			JasperReport jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+"/reporteImpresionConceptosFacturacion.jrxml");
			
			List<ConceptoOperacionCliente> conceptosOperacion = (List<ConceptoOperacionCliente>) session.getAttribute("conceptosOperacionClienteSession");
			List<conceptoOperacionPdf> lista = new ArrayList<ListaConceptoOperacionClienteController.conceptoOperacionPdf>();
			for(ConceptoOperacionCliente o : conceptosOperacion){
				conceptoOperacionPdf p = new conceptoOperacionPdf();
				p.setCodigo(o.getConceptoFacturable().getCodigo());
				p.setConcepto(o.getConceptoFacturable().getDescripcion());
				p.setCliente(o.getClienteEmp().getRazonSocialONombreYApellido());
				p.setFecha(o.getFechaAltaStr());
				p.setTipoConcepto(o.getTipoConcepto());
				p.setFinalUnitario(o.getFinalUnitario().toString());
				p.setCantidad(o.getCantidad().toString());
				p.setFinalTotal(o.getFinalTotal().toString());
				p.setEstado(o.getEstado());
				p.setRequerimiento(o.getCodigoSeriePrefijoNumero());
				lista.add(p);
			}
			response.setContentType("application/pdf");
			parametros.put("fecha", formatoFechaFormularios.format(new Date()));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,new JRBeanCollectionDataSource(lista));
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
			response.getOutputStream().close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public class conceptoOperacionPdf{
		public String codigo;
		public String concepto;
		public String cliente;
		public String fecha;
		public String tipoConcepto;
		public String finalUnitario;
		public String cantidad;
		public String finalTotal;
		public String estado;
		public String requerimiento;
		public String getCodigo() {
			return codigo;
		}
		public void setCodigo(String codigo) {
			this.codigo = codigo;
		}
		public String getConcepto() {
			return concepto;
		}
		public void setConcepto(String concepto) {
			this.concepto = concepto;
		}
		public String getCliente() {
			return cliente;
		}
		public void setCliente(String cliente) {
			this.cliente = cliente;
		}
		public String getFecha() {
			return fecha;
		}
		public void setFecha(String fecha) {
			this.fecha = fecha;
		}
		public String getTipoConcepto() {
			return tipoConcepto;
		}
		public void setTipoConcepto(String tipoConcepto) {
			this.tipoConcepto = tipoConcepto;
		}
		public String getFinalUnitario() {
			return finalUnitario;
		}
		public void setFinalUnitario(String finalUnitario) {
			this.finalUnitario = finalUnitario;
		}
		public String getCantidad() {
			return cantidad;
		}
		public void setCantidad(String cantidad) {
			this.cantidad = cantidad;
		}
		public String getFinalTotal() {
			return finalTotal;
		}
		public void setFinalTotal(String finalTotal) {
			this.finalTotal = finalTotal;
		}
		public String getEstado() {
			return estado;
		}
		public void setEstado(String estado) {
			this.estado = estado;
		}
		public String getRequerimiento() {
			return requerimiento;
		}
		public void setRequerimiento(String requerimiento) {
			this.requerimiento = requerimiento;
		}
	}
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
}