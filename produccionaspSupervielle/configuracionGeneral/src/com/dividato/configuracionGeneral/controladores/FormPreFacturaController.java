package com.dividato.configuracionGeneral.controladores;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.PreFacturaValidator;
import com.security.accesoDatos.configuraciongeneral.hibernate.ClienteEmpServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PreFacturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PreFacturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.jerarquias.interfaz.ConceptoOperacionClienteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.PreFactura;
import com.security.modelo.configuraciongeneral.PreFacturaDetalle;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
import com.security.utils.DateUtil;
import com.security.utils.ParseNumberUtils;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de User.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Victor Kenis (23/09/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/iniciarPrecargaFormularioPreFactura.html",
				"/precargaFormularioPreFactura.html",
				"/agregarPreFacturaDetalle.html",
				"/guardarActualizarPreFactura.html",
				"/eliminarDetallePreFactura.html",
				"/habilitarDetallePreFactura.html"
			}
		)
public class FormPreFacturaController {
	
	private PreFacturaService preFacturaService;
	private PreFacturaDetalleService preFacturaDetalleService;

	private SerieService serieService;
	private SucursalService sucursalService;
	private PreFacturaValidator validator;
	private EmpresaService empresaService;
	private ConceptoOperacionClienteService conceptoOperacionClienteService;
	private ClienteEmpService clienteEmpService;
	private AfipTipoComprobanteService afipTipoComprobanteService;
	private FormLoteFacturacionController formLoteFacturacionController;
	private FormPreFacturaDetalleController formPreFacturaDetalleController;
	
	/**
	 * Setea el servicio de PreFactura.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto PreFacturaService.
	 * La clase PreFacturaImp implementa PreFactura y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param preFacturaService
	 */
	
	@Autowired
	public void setFormLoteFacturacionController(FormLoteFacturacionController formLoteFacturacionController) {
		this.formLoteFacturacionController = formLoteFacturacionController;
	}
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	@Autowired
	public void setFormPreFacturaDetalleController(
			FormPreFacturaDetalleController formPreFacturaDetalleController) {
		this.formPreFacturaDetalleController = formPreFacturaDetalleController;
	}
	@Autowired
	public void setAfipTipoComprobanteService(
			AfipTipoComprobanteService afipTipoComprobanteService) {
		this.afipTipoComprobanteService = afipTipoComprobanteService;
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
	public void setConceptoOperacionClienteService(ConceptoOperacionClienteService conceptoOperacionClienteService) {
		this.conceptoOperacionClienteService = conceptoOperacionClienteService;
	}
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	
	@Autowired
	public void setValidator(PreFacturaValidator validator) {
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
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
		
	@RequestMapping(
			value="/iniciarPrecargaFormularioPreFactura.html",
			method = RequestMethod.GET
		)
	public String iniciarPrecargaFormularioPreFactura(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accionPreFactura", required=false) String accionPreFactura,
			@RequestParam(value="ordenPreFactura", required=false) Long orden)
	{
		atributos.remove("preFacturaDetalles");
		session.removeAttribute("preFacturaDetallesSession");
		atributos.remove("preFacturaFormulario");
		session.removeAttribute("preFacturaFormularioSession");
		return precargaFormularioPreFactura(session, atributos,accionPreFactura, orden);
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
	 * @param accionPreFactura parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioPreFactura" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(			
			value="/precargaFormularioPreFactura.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioPreFactura(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accionPreFactura", required=false) String accionPreFactura,
			@RequestParam(value="ordenPreFactura", required=false) Long orden){
		
		List<PreFactura> listaPreFacturas = (List<PreFactura>) session.getAttribute("listaPreFacturasSession");
		if(listaPreFacturas == null){
			listaPreFacturas = new ArrayList<PreFactura>();
		}
		
		PreFactura preFacturaFormulario = (PreFactura) session.getAttribute("preFacturaFormularioSession");
		if(preFacturaFormulario==null)
		{preFacturaFormulario = new PreFactura();}
		
		Empresa empresa = obtenerEmpresaUser();
		Sucursal sucursal = obtenerSucursalUser();
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		
		List<PreFacturaDetalle> preFacturaDetalles = (List<PreFacturaDetalle>) session.getAttribute("preFacturaDetallesSession");
		if(preFacturaDetalles == null){
				preFacturaDetalles = new ArrayList<PreFacturaDetalle> ();
		}
		
		if(accionPreFactura==null) {
			accionPreFactura=Constantes.FACTURA_ACCION_NUEVO; //acción por defecto: nuevo
			preFacturaFormulario.setCodigoEmpresa(empresa.getCodigo());
			preFacturaFormulario.setCodigoSucursal(sucursal.getCodigo());
			preFacturaFormulario.setEmpresa(empresa);
			preFacturaFormulario.setSucursal(sucursal);
			session.setAttribute("preFacturaFormularioSession", preFacturaFormulario);
		}
		if(!accionPreFactura.equals(Constantes.FACTURA_ACCION_NUEVO)){
			preFacturaFormulario = (PreFactura) session.getAttribute("preFacturaFormularioSession");
			if(preFacturaFormulario==null){
				for(int i = 0; i<listaPreFacturas.size();i++){
					if(listaPreFacturas.get(i).getOrden().longValue() == orden.longValue()){
						preFacturaFormulario = listaPreFacturas.get(i);
						preFacturaDetalles = listaPreFacturas.get(i).getDetalles();
						preFacturaFormulario.setCodigoEmpresa(preFacturaFormulario.getEmpresa().getCodigo());
						preFacturaFormulario.setCodigoSucursal(preFacturaFormulario.getSucursal().getCodigo());
						preFacturaFormulario.setCodigoCliente(preFacturaFormulario.getClienteEmp().getCodigo());
						preFacturaFormulario.setCodigoSerie(preFacturaFormulario.getSerie().getCodigo());
						preFacturaFormulario.setIdAfipTipoComprobante(preFacturaFormulario.getAfipTipoDeComprobante().getId());
						break;
					}
				}
			}
			session.setAttribute("preFacturaFormularioSession", preFacturaFormulario);
			atributos.put("preFacturaFormulario", preFacturaFormulario);
		}
		
		if(preFacturaDetalles.size()>0){
			atributos.put("headerPreFacturaNoModificable", Boolean.TRUE);
		}else{
			atributos.put("headerPreFacturaNoModificable", Boolean.FALSE);
		}
		
		atributos.put("comboTipoComprobante", obtenerOpcionesComboTipoComprobante(preFacturaFormulario.getCodigoCliente(), preFacturaFormulario.getCodigoEmpresa()));
		
		atributos.put("preFacturaFormulario", preFacturaFormulario);
		session.setAttribute("preFacturaFormularioSession", preFacturaFormulario);
		atributos.put("accionPreFactura",accionPreFactura);
		atributos.put("ordenPreFactura",orden);
		if(orden!=null){
			session.setAttribute("ordenPreFacturaSession", orden);
		}
		atributos.put("preFacturaDetalles",preFacturaDetalles);
		session.setAttribute("preFacturaDetallesSession",preFacturaDetalles);
		atributos.put("clienteAspId", clienteAsp.getId());
		String accionLote = (String) session.getAttribute("accionLoteSession");
		atributos.put("accionLote", accionLote);
		return "formularioPreFactura";
	}
	
	@RequestMapping(
			value="/agregarPreFacturaDetalle.html",
			method = RequestMethod.GET
		)
	public String agregarPreFacturaDetalle(
			HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accionPreFactura", required=false) String accionPreFactura,
			@RequestParam(value="accionPreFacturaDetalle", required=false) String accionPreFacturaDetalle,
			@RequestParam(value="id", required=false) Long id,
			@RequestParam(value="codigoEmpresa", required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal", required=false) String codigoSucursal,
			@RequestParam(value="codigoCliente", required=false) String codigoCliente,
			@RequestParam(value="idAfipTipoComprobante", required=false) Long idAfipTipoComprobante,
			@RequestParam(value="codigoSerie", required=false) String codigoSerie,
			@RequestParam(value="fechaStr", required=false) String fechaStr,
			@RequestParam(value="prefijoSerie", required=false) String prefijoSerie,
			@RequestParam(value="numeroComprobanteStr", required=false) String numeroComprobanteStr
			){
		PreFactura preFacturaFormulario = (PreFactura) session.getAttribute("preFacturaFormularioSession");
		preFacturaFormulario.setCodigoEmpresa(codigoEmpresa);
		preFacturaFormulario.setCodigoSucursal(codigoSucursal);
		preFacturaFormulario.setCodigoCliente(codigoCliente);
		preFacturaFormulario.setIdAfipTipoComprobante(idAfipTipoComprobante);
		preFacturaFormulario.setCodigoSerie(codigoSerie);
		preFacturaFormulario.setFechaStr(fechaStr);
		preFacturaFormulario.setPrefijoSerie(prefijoSerie);
		
		atributos.put("preFacturaFormulario", preFacturaFormulario);
		session.setAttribute("preFacturaFormularioSession", preFacturaFormulario);
		return formPreFacturaDetalleController.iniciarPrecargaFormularioPreFacturaDetalle(session, atributos, accionPreFactura, accionPreFacturaDetalle, id, codigoCliente, fechaStr,codigoEmpresa);
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
			value="/guardarActualizarPreFactura.html",
			method= RequestMethod.POST
		)
	@SuppressWarnings("unchecked")
	public String guardarPreFactura(
			@RequestParam(value="accionLote",required=false) String accionLote,
			@RequestParam(value="accionPreFactura",required=false) String accionPreFactura,
			@RequestParam(value="ordenPreFactura",required=false) Long orden,
			@RequestParam(value="codigoEmpresa",required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal",required=false) String codigoSucursal,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente,
			@RequestParam(value="idAfipTipoComprobante",required=false) Long idAfipTipoComprobante,
			@RequestParam(value="codigoSerie",required=false) String codigoSerie,
			@RequestParam(value="fechaStr",required=false) String fechaStr,
			@RequestParam(value="numeroComprobanteStr",required=false) String numeroComprobanteStr,
			HttpSession session,
			Map<String,Object> atributos){

		Long ordenPreFactura = (Long) session.getAttribute("ordenPreFacturaSession");
		
		List<PreFactura> listaPreFacturas = (List<PreFactura>) session.getAttribute("listaPreFacturasSession");
		if(listaPreFacturas == null){
			listaPreFacturas = new ArrayList<PreFactura>();
		}
		
		PreFactura preFacturaFormulario = (PreFactura) session.getAttribute("preFacturaFormularioSession");
		if(preFacturaFormulario==null)
		{preFacturaFormulario = new PreFactura();}
		
		ArrayList<String> errors = new ArrayList<String>();
		List<PreFacturaDetalle> detalles = (List<PreFacturaDetalle>)session.getAttribute("preFacturaDetallesSession");

		ClienteAsp clienteAsp = obtenerClienteAspUser();

		if (accionPreFactura == null || accionPreFactura.equals("") || Constantes.FACTURA_ACCION_NUEVO.equalsIgnoreCase(accionPreFactura)){
			accionPreFactura = Constantes.FACTURA_ACCION_NUEVO;
		}else if(!Constantes.FACTURA_ACCION_MODIFICAR.equalsIgnoreCase(accionPreFactura)){
			accionPreFactura = Constantes.FACTURA_ACCION_CONSULTAR;
		}
		
		if (Constantes.FACTURA_ACCION_NUEVO.equalsIgnoreCase(accionPreFactura)){
			
			preFacturaFormulario = new PreFactura();
			preFacturaFormulario.setCodigoEmpresa(codigoEmpresa);
			preFacturaFormulario.setCodigoSucursal(codigoSucursal);
			preFacturaFormulario.setCodigoCliente(codigoCliente);
			preFacturaFormulario.setIdAfipTipoComprobante(idAfipTipoComprobante);
			preFacturaFormulario.setCodigoSerie(codigoSerie);
			preFacturaFormulario.setFechaStr(fechaStr);
			
			//TODO validar datos preFacturaFormulario
			Empresa empresa = empresaService.getByCodigo(preFacturaFormulario.getCodigoEmpresa(), clienteAsp);
			Sucursal sucursal = sucursalService.getByCodigo(preFacturaFormulario.getCodigoSucursal(), empresa.getCodigo(), clienteAsp);
			ClienteEmp clienteEmp = clienteEmpService.getByCodigo(preFacturaFormulario.getCodigoCliente(), empresa.getCodigo(), clienteAsp, Boolean.TRUE);
			AfipTipoComprobante afipTipoComprobante = afipTipoComprobanteService.obtenerPorId(preFacturaFormulario.getIdAfipTipoComprobante());
			Serie serie = serieService.obtenerPorCodigo(preFacturaFormulario.getCodigoSerie(), null, empresa.getCodigo(), clienteAsp);
			
			String letraComprobante = obtenerLetraComprobante(empresa, clienteEmp);
			
			//cargando preFactura a guardar
			PreFactura preFactura = new PreFactura();
			preFactura.setClienteAsp(clienteAsp);
			preFactura.setEmpresa(empresa);
			preFactura.setSucursal(sucursal);
			preFactura.setClienteEmp(clienteEmp);
			preFactura.setAfipTipoDeComprobante(afipTipoComprobante);
			preFactura.setSerie(serie);
			preFactura.setFecha(preFacturaFormulario.getFecha());
			preFactura.setPrefijoSerie(serie.getPrefijo());
			
			preFactura.setLetraComprobante(letraComprobante);
			this.calcularTotalesPreFactura(preFactura, detalles);
			
			verificarNuevaPreFactura(preFactura, errors);
			if(errors.size()==0 ){
//				if(preFacturaService.guardarPreFactura(preFactura, detalles)){
//					generateAvisoExito("formularioPreFactura.exito.guardar", atributos);
//					accionPreFactura = Constantes.FACTURA_ACCION_MODIFICAR;
//					preFacturaGuardada = preFacturaService.obtenerPreFacturaPorNumeroComprobante(clienteAsp, empresa, sucursal, clienteEmp, serie, preFactura.getNumeroComprobante());
//					preFacturaFormulario.setId(preFacturaGuardada!=null?preFacturaGuardada.getId():null);
//				}else{
					errors.add("formularioPreFactura.error.falloAlGuardar");
					generateErrors(errors, atributos);
				//}
			}else{
				generateErrors(errors, atributos);
			}
			
			
		}
		else if (Constantes.FACTURA_ACCION_MODIFICAR.equalsIgnoreCase(accionPreFactura)){
				for(int i = 0; i<listaPreFacturas.size();i++){
					if(listaPreFacturas.get(i).getOrden().longValue() == ordenPreFactura.longValue()){
						listaPreFacturas.get(i).setFechaStr(fechaStr);
						this.calcularTotalesPreFactura(listaPreFacturas.get(i), detalles);
						listaPreFacturas.get(i).setDetalles(detalles);
						break;
					}
				}
				
			session.setAttribute("listaPreFacturasSession", listaPreFacturas);
			session.setAttribute("preFacturaFormularioSession", preFacturaFormulario);
			atributos.put("preFacturaFormulario", preFacturaFormulario);

		}
		
		
		//hacemos el redirect
		atributos.put("preFacturaFormulario", preFacturaFormulario);
		session.setAttribute("preFacturaFormularioSession", preFacturaFormulario);
		
		atributos.put("accionPreFactura",accionPreFactura);
		
		atributos.put("clienteAspId", clienteAsp.getId());
		
		atributos.put("preFacturaDetalles",detalles);
		session.setAttribute("preFacturaDetallesSession",detalles);
		
		atributos.put("clienteAspId", clienteAsp.getId());

		return formLoteFacturacionController.precargaFormularioLoteFacturacion(session, atributos, accionLote, null, null, null, null, null);
	}
	
	@RequestMapping(
			value="/eliminarDetallePreFactura.html",
			method= RequestMethod.GET
		)
	public String eliminarDetallePreFactura(
			@RequestParam(value="accionPreFactura",required=false) String accionPreFactura,
			@RequestParam(value="ordenDetalle",required=false) Long orden,			
			HttpSession session,
			Map<String,Object> atributos){
		
		List<PreFacturaDetalle> detalles = (List<PreFacturaDetalle>)session.getAttribute("preFacturaDetallesSession");
		if(detalles != null && detalles.size()>0 && orden!=null)
		{
			for(int i = 0;i<detalles.size();i++){
				if(detalles.get(i).getOrden().longValue() == orden.longValue()){
					detalles.get(i).setEstado("Anulado");
					List<ConceptoOperacionCliente> listaConceptosAsociados = conceptoOperacionClienteService.listarConceptosPorPreFacturaDetalle(detalles.get(i).getId(), obtenerClienteAspUser());
					if(listaConceptosAsociados!=null && listaConceptosAsociados.size()>0)
					{
						for(ConceptoOperacionCliente concepto:listaConceptosAsociados){
							Boolean commit = false;
							concepto.setEstado("Pendiente");
							concepto.setAsignado(false);
							commit = conceptoOperacionClienteService.actualizarConceptoOperacionCliente(concepto);
						}
							
					}
					break;
				}
			}
		}
		if(detalles.size()>0){
			atributos.put("headerPreFacturaNoModificable", Boolean.TRUE);
		}else{
			atributos.put("headerPreFacturaNoModificable", Boolean.FALSE);
		}
		session.setAttribute("preFacturaDetallesSession", detalles);
		atributos.put("preFacturaDetalles", detalles);
		return precargaFormularioPreFactura(session, atributos, accionPreFactura, null);
	}
	
	
	@RequestMapping(
			value="/habilitarDetallePreFactura.html",
			method= RequestMethod.GET
		)
	public String habilitarDetallePreFactura(
			@RequestParam(value="accionPreFactura",required=false) String accionPreFactura,
			@RequestParam(value="ordenDetalle",required=false) Long orden,			
			HttpSession session,
			Map<String,Object> atributos){
		
		List<PreFacturaDetalle> detalles = (List<PreFacturaDetalle>)session.getAttribute("preFacturaDetallesSession");
		if(detalles != null && detalles.size()>0 && orden!=null)
		{
			for(int i = 0;i<detalles.size();i++){
				if(detalles.get(i).getOrden().longValue() == orden.longValue()){
					detalles.get(i).setEstado("Pendiente");
					List<ConceptoOperacionCliente> listaConceptosAsociados = conceptoOperacionClienteService.listarConceptosPorPreFacturaDetalle(detalles.get(i).getId(), obtenerClienteAspUser());
					if(listaConceptosAsociados!=null && listaConceptosAsociados.size()>0)
					{
						for(ConceptoOperacionCliente concepto:listaConceptosAsociados){
							Boolean commit = false;
							concepto.setEstado("Facturado");
							concepto.setAsignado(true);
							commit = conceptoOperacionClienteService.actualizarConceptoOperacionCliente(concepto);
						}
							
					}
					break;
				}
			}
		}
//		PreFacturaDetalle fd = null;
//		if(Constantes.FACTURA_ACCION_NUEVO.equals(accionPreFactura)){
//			Iterator<PreFacturaDetalle> it = detalles.iterator();
//			while(it.hasNext()){
//				fd = it.next();
//				if(idEliminar.longValue() == fd.getIdEliminar().longValue()){
//					it.remove();
//					break;
//				}
//			}
//		}
		if(detalles.size()>0){
			atributos.put("headerPreFacturaNoModificable", Boolean.TRUE);
		}else{
			atributos.put("headerPreFacturaNoModificable", Boolean.FALSE);
		}
		session.setAttribute("preFacturaDetallesSession", detalles);
		atributos.put("preFacturaDetalles", detalles);
		return precargaFormularioPreFactura(session, atributos, accionPreFactura, null);
	}
	
	
	/////////////Metodos auxiliares
	
	private void calcularTotalesPreFactura(PreFactura preFactura, List<PreFacturaDetalle> detalles){
		BigDecimal totalNeto = BigDecimal.valueOf(0D);
		BigDecimal totalImpuestos = BigDecimal.valueOf(0D);
		BigDecimal totalFinal = BigDecimal.valueOf(0D);
		
		for (PreFacturaDetalle detalle:detalles){
			if(!detalle.getEstado().equalsIgnoreCase("Anulado")){
				totalNeto = totalNeto.add(detalle.getNetoTotal());
				totalImpuestos = totalImpuestos.add(detalle.getImpuestoTotal());
				totalFinal = totalFinal.add(detalle.getFinalTotal());
			}
		}
		
		preFactura.setTotalNeto(totalNeto);
		preFactura.setTotalIVA(totalImpuestos);
		preFactura.setTotalFinal(totalFinal);
	}
	
	/**
	 * Calcula la letra del comprobante, si el clienteEmp y la empresa son responsables inscriptos devuelve A, caso contrario devuelve B
	 * @param empresa
	 * @param clienteEmp
	 * @return
	 */
	private String obtenerLetraComprobante(Empresa empresa, ClienteEmp clienteEmp ){
		String result = Constantes.FACTURA_LETRA_COMPROBANTE_B;
		
		if(empresa != null && clienteEmp!=null 
				&& empresa.getAfipCondIva()!=null && clienteEmp.getAfipCondIva()!=null 
				&& "1".equals(empresa.getAfipCondIva().getCodigo()) 
				&& "1".equals(clienteEmp.getAfipCondIva().getCodigo())){
			result = Constantes.FACTURA_LETRA_COMPROBANTE_A;			
		}
		return result;
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
	
	private String obtenerOpcionesComboTipoComprobante(String codigoClienteEmp, String codigoEmpresa){
		StringBuilder result = new StringBuilder("");
		
		result.append("<option value=\"0\" selected=\"selected\" >Seleccionar</option>\n");
		AfipTipoComprobante afipTipoComprobante = new AfipTipoComprobante();
		if(codigoClienteEmp!=null && codigoClienteEmp.length()>0){
			ClienteEmp clienteEmp = new ClienteEmp();
			clienteEmp.setCodigo(codigoClienteEmp);
			clienteEmp.setCodigoEmpresa(codigoEmpresa);
			ClienteEmpService service = new ClienteEmpServiceImp(HibernateControl.getInstance());
			ClienteAsp clienteAsp = obtenerClienteAspUser();
			clienteEmp = service.getByCodigo(clienteEmp, clienteAsp);
			
			//se cargan los codigos para filtrar los tipos de comprobantes que aplican a preFactura		
			ArrayList<String> codigos = new ArrayList<String>();
			codigos.add("001");
			codigos.add("002");
			codigos.add("003");
			codigos.add("006");
			codigos.add("007");
			codigos.add("008");
			afipTipoComprobante.setCodigos(codigos);
			
			List<AfipTipoComprobante> listaAfipTipoComprobantes = afipTipoComprobanteService.listarTipoComprobanteFiltrados(afipTipoComprobante);
			//genero la respuesta
			for ( AfipTipoComprobante tipo : listaAfipTipoComprobantes){
				result.append("<option value =\"").append(tipo.getId()).append("\" ");
				Empresa empresa =empresaService.getByCodigo(codigoEmpresa, clienteAsp);
				if(empresa != null && empresa.getAfipCondIva()!=null && "1".equals(empresa.getAfipCondIva().getCodigo()) && "1".equals(clienteEmp.getAfipCondIva().getCodigo()) &&
						("006".equals(tipo.getCodigo()) || "007".equals(tipo.getCodigo()) || "008".equals(tipo.getCodigo()))){
					result.append(" disabled=\"disabled\" ");						
				}else if((empresa != null && empresa.getAfipCondIva()!=null && !"1".equals(empresa.getAfipCondIva().getCodigo()) || !"1".equals(clienteEmp.getAfipCondIva().getCodigo())) &&
						("001".equals(tipo.getCodigo()) || "002".equals(tipo.getCodigo()) || "003".equals(tipo.getCodigo()) )){
					result.append(" disabled=\"disabled\" ");
				}else{
					//TODO solo si la empresa no tiene condicion de iva 
					//respuestaBuilder.append(" disabled=\"disabled\"");
				}
				result.append(">")
					.append(tipo.getDescripcion())
					.append("</option>");
			}
		}
		return result.toString();
	}
	
	/**
	 * genera el objeto BindingResult para mostrar los errores por pantalla en
	 * un popup y lo agrega al map atributos
	 * 
	 * @param codigoErrores
	 * @param atributos
	 */
	private void generateErrors(List<String> codigoErrores,
			Map<String, Object> atributos) {
		if (!codigoErrores.isEmpty()) {
			BindingResult result = new BeanPropertyBindingResult(new Object(),
					"");
			for (String codigo : codigoErrores) {
				result.addError(new FieldError(
						"error.formBookingGroup.general", codigo, null, false,
						new String[] { codigo }, null, "?"));
			}
			atributos.put("result", result);
			atributos.put("errores", true);
		} else if (atributos.get("result") == null) {
			atributos.put("errores", false);
		}
	}

	private void generateAvisoExito(String avisoExito,
			Map<String, Object> atributos) {
		// Genero las notificaciones
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		ScreenMessage mensajeEstanteReg = new ScreenMessageImp(avisoExito, null);
		avisos.add(mensajeEstanteReg); // agrego el mensaje a la coleccion
		atributos.put("errores", false);
		atributos.remove("result");
		atributos.put("hayAvisos", true);
		atributos.put("hayAvisosNeg", false);
		atributos.put("avisos", avisos);
	}
	
	private void verificarNuevaPreFactura(PreFactura preFactura, List<String> errors){
		if(preFactura.getEmpresa()==null){
			errors.add("formularioPreFactura.error.empresaRequerida");
		}
		if(preFactura.getSucursal()==null){
			errors.add("formularioPreFactura.error.sucursalRequerida");
		}
		if(preFactura.getClienteEmp()==null){
			errors.add("formularioPreFactura.error.clienteEmpRequerido");
		}
		if(preFactura.getAfipTipoDeComprobante()==null){
			errors.add("formularioPreFactura.error.tipoComprobanteRequerido");
		}
		
		if(preFactura.getSerie()==null){
			errors.add("formularioPreFactura.error.serieRequerida");
		}
		if(DateUtil.verificarFechaMayorActual(preFactura.getFecha())){
			errors.add("formularioPreFactura.error.fechaMayorActual");
		}
	}
}
