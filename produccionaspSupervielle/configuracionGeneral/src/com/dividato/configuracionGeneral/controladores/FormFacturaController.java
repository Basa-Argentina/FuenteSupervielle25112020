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

import com.dividato.configuracionGeneral.validadores.FacturaValidator;
import com.security.accesoDatos.configuraciongeneral.hibernate.ClienteEmpServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.FacturaDetalle;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
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
				"/iniciarPrecargaFormularioFactura.html",
				"/precargaFormularioFactura.html",
				"/agregarFacturaDetalle.html",
				"/guardarActualizarFactura.html",
				"/eliminarDetalleFactura.html"
			}
		)
public class FormFacturaController {
	
	private FacturaService facturaService;
	private FacturaDetalleService facturaDetalleService;
	private EmpleadoService empleadoService;
	private SerieService serieService;
	private SucursalService sucursalService;
	private FacturaValidator validator;
	private EmpresaService empresaService;
	private LecturaDetalleService lecturaDetalleService;
	private ClienteEmpService clienteEmpService;
	private ClienteDireccionService clienteDireccionService;
	private AfipTipoComprobanteService afipTipoComprobanteService;
	private ListaComprobantesFacturaController listaComprobantesFacturaController;
	private FormFacturaDetalleController formFacturaDetalleController;
	
	/**
	 * Setea el servicio de Factura.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto FacturaService.
	 * La clase FacturaImp implementa Factura y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param facturaService
	 */
	
	@Autowired
	public void setListaFacturasController(ListaComprobantesFacturaController listaComprobantesFacturaController) {
		this.listaComprobantesFacturaController = listaComprobantesFacturaController;
	}
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	@Autowired
	public void setFormFacturaDetalleController(
			FormFacturaDetalleController formFacturaDetalleController) {
		this.formFacturaDetalleController = formFacturaDetalleController;
	}
	@Autowired
	public void setAfipTipoComprobanteService(
			AfipTipoComprobanteService afipTipoComprobanteService) {
		this.afipTipoComprobanteService = afipTipoComprobanteService;
	}
	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	@Autowired
	public void setFacturaService(FacturaService facturaService) {
		this.facturaService = facturaService;
	}
	@Autowired
	public void setFacturaDetalleService(FacturaDetalleService facturaDetalleService) {
		this.facturaDetalleService = facturaDetalleService;
	}
	@Autowired
	public void setLecturaDetalleService(LecturaDetalleService lecturaDetalleService) {
		this.lecturaDetalleService = lecturaDetalleService;
	}
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	
	@Autowired
	public void setValidator(FacturaValidator validator) {
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
	
	@Autowired
	public void setClienteDireccionService(ClienteDireccionService clienteDireccionService) {
		this.clienteDireccionService = clienteDireccionService;
	}
	
	@RequestMapping(
			value="/iniciarPrecargaFormularioFactura.html",
			method = RequestMethod.GET
		)
	public String iniciarPrecargaFormularioFactura(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accionFactura", required=false) String accionFactura,
			@RequestParam(value="idFactura", required=false) Long id)
	{
		atributos.remove("facturaDetalles");
		session.removeAttribute("facturaDetallesSession");
		atributos.remove("facturaFormulario");
		session.removeAttribute("facturaFormularioSession");
		return precargaFormularioFactura(session, atributos,accionFactura, id);
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
	 * @param accionFactura parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioFactura" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(			
			value="/precargaFormularioFactura.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioFactura(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accionFactura", required=false) String accionFactura,
			@RequestParam(value="idFactura", required=false) Long id){
		
		Factura facturaFormulario = (Factura) session.getAttribute("facturaFormularioSession");
		if(facturaFormulario==null)
		{facturaFormulario = new Factura();}
		
		Empresa empresa = obtenerEmpresaUser();
		Sucursal sucursal = obtenerSucursalUser();
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		
		List<FacturaDetalle> facturaDetalles = (List<FacturaDetalle>) session.getAttribute("facturaDetallesSession");
		if(facturaDetalles == null){
				facturaDetalles = new ArrayList<FacturaDetalle> ();
		}
		
		if(accionFactura==null) {
			accionFactura=Constantes.FACTURA_ACCION_NUEVO; //acción por defecto: nuevo
			facturaFormulario.setCodigoEmpresa(empresa.getCodigo());
			facturaFormulario.setCodigoSucursal(sucursal.getCodigo());
			facturaFormulario.setEmpresa(empresa);
			facturaFormulario.setSucursal(sucursal);
			session.setAttribute("facturaFormularioSession", facturaFormulario);
		}
		if(!accionFactura.equals(Constantes.FACTURA_ACCION_NUEVO)){
			facturaFormulario = (Factura) session.getAttribute("facturaFormularioSession");
			if(facturaFormulario==null){
				facturaFormulario = facturaService.obtenerPorId(id);
				facturaFormulario.setCodigoEmpresa(facturaFormulario.getEmpresa().getCodigo());
				facturaFormulario.setCodigoSucursal(facturaFormulario.getSucursal().getCodigo());
				facturaFormulario.setCodigoSerie(facturaFormulario.getSerie().getCodigo());
				facturaFormulario.setCodigoCliente(facturaFormulario.getClienteEmp().getCodigo());
				facturaFormulario.setIdAfipTipoComprobante(facturaFormulario.getAfipTipoDeComprobante().getId());
				if(facturaDetalles == null || facturaDetalles.size()<=0){
					facturaDetalles = facturaDetalleService.listarFacturaDetallePorFactura(facturaFormulario, clienteAsp);
				}
				
			}
			session.setAttribute("facturaFormularioSession", facturaFormulario);
			atributos.put("facturaFormulario", facturaFormulario);
		}
		
		if(facturaDetalles.size()>0){
			atributos.put("headerFacturaNoModificable", Boolean.TRUE);
		}else{
			atributos.put("headerFacturaNoModificable", Boolean.FALSE);
		}
		
		atributos.put("comboTipoComprobante", obtenerOpcionesComboTipoComprobante(facturaFormulario.getCodigoCliente(), facturaFormulario.getCodigoEmpresa()));
		
		atributos.put("facturaFormulario", facturaFormulario);
		session.setAttribute("facturaFormularioSession", facturaFormulario);
		atributos.put("accionFactura",accionFactura);
		atributos.put("idFactura",id);
		atributos.put("facturaDetalles",facturaDetalles);
		session.setAttribute("facturaDetallesSession",facturaDetalles);
		atributos.put("clienteAspId", clienteAsp.getId());
		calcularTotalesFactura(facturaFormulario, facturaDetalles);
		return "formularioFactura";
	}
	
	@RequestMapping(
			value="/agregarFacturaDetalle.html",
			method = RequestMethod.GET
		)
	public String agregarFacturaDetalle(
			HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accionFactura", required=false) String accionFactura,
			@RequestParam(value="accionFacturaDetalle", required=false) String accionFacturaDetalle,
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
		Factura facturaFormulario = (Factura) session.getAttribute("facturaFormularioSession");
		facturaFormulario.setCodigoEmpresa(codigoEmpresa);
		facturaFormulario.setCodigoSucursal(codigoSucursal);
		facturaFormulario.setCodigoCliente(codigoCliente);
		facturaFormulario.setIdAfipTipoComprobante(idAfipTipoComprobante);
		facturaFormulario.setCodigoSerie(codigoSerie);
		facturaFormulario.setFechaStr(fechaStr);
		facturaFormulario.setPrefijoSerie(prefijoSerie);
		facturaFormulario.setNumeroComprobanteStr(numeroComprobanteStr);
		
		atributos.put("facturaFormulario", facturaFormulario);
		session.setAttribute("facturaFormularioSession", facturaFormulario);
		return formFacturaDetalleController.iniciarPrecargaFormularioFacturaDetalle(session, atributos, accionFactura, accionFacturaDetalle, id, codigoCliente, fechaStr,codigoEmpresa);
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
			value="/guardarActualizarFactura.html",
			method= RequestMethod.POST
		)
	public String guardarFactura(
			@RequestParam(value="accionFactura",required=false) String accionFactura,
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="codigoEmpresa",required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal",required=false) String codigoSucursal,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente,
			@RequestParam(value="idAfipTipoComprobante",required=false) Long idAfipTipoComprobante,
			@RequestParam(value="codigoSerie",required=false) String codigoSerie,
			@RequestParam(value="fechaStr",required=false) String fechaStr,
			@RequestParam(value="numeroComprobanteStr",required=false) String numeroComprobanteStr,
			@RequestParam(value="notasFacturacion",required=false) String notasFacturacion,
			HttpSession session,
			Map<String,Object> atributos){
		@SuppressWarnings("unchecked")
		
		Factura facturaFormulario = new Factura();
		facturaFormulario.setCodigoEmpresa(codigoEmpresa);
		facturaFormulario.setCodigoSucursal(codigoSucursal);
		facturaFormulario.setCodigoCliente(codigoCliente);
		facturaFormulario.setIdAfipTipoComprobante(idAfipTipoComprobante);
		facturaFormulario.setCodigoSerie(codigoSerie);
		facturaFormulario.setFechaStr(fechaStr);
		facturaFormulario.setNotasFacturacion(notasFacturacion);
		facturaFormulario.setNumeroComprobanteStr(numeroComprobanteStr);
		ArrayList<String> errors = new ArrayList<String>();
		List<FacturaDetalle> detalles = (List<FacturaDetalle>)session.getAttribute("facturaDetallesSession");

		ClienteAsp clienteAsp = obtenerClienteAspUser();
		Factura facturaGuardada = null;
		if (accionFactura == null || accionFactura.equals("") || Constantes.FACTURA_ACCION_NUEVO.equalsIgnoreCase(accionFactura)){
			accionFactura = Constantes.FACTURA_ACCION_NUEVO;
		}else if(!Constantes.FACTURA_ACCION_MODIFICAR.equalsIgnoreCase(accionFactura)){
			accionFactura = Constantes.FACTURA_ACCION_CONSULTAR;
		}
		
		if (Constantes.FACTURA_ACCION_NUEVO.equalsIgnoreCase(accionFactura)){
			//TODO validar datos facturaFormulario
			Empresa empresa = empresaService.getByCodigoConCondAfip(facturaFormulario.getCodigoEmpresa(), clienteAsp);
			Sucursal sucursal = sucursalService.getByCodigo(facturaFormulario.getCodigoSucursal(), empresa.getCodigo(), clienteAsp);
			ClienteEmp clienteEmp = new ClienteEmp();
			clienteEmp.setCodigo(facturaFormulario.getCodigoCliente());
			clienteEmp = clienteEmpService.getByCodigoFactura(clienteEmp, clienteAsp);
			AfipTipoComprobante afipTipoComprobante = afipTipoComprobanteService.obtenerPorId(facturaFormulario.getIdAfipTipoComprobante());
			Serie serie = serieService.obtenerPorCodigo(facturaFormulario.getCodigoSerie(), null, empresa.getCodigo(), clienteAsp);
			Long numeroComprobante = ParseNumberUtils.parseLongCodigo(facturaFormulario.getNumeroComprobanteStr());
			String letraComprobante = obtenerLetraComprobante(empresa, clienteEmp);
			
			//cargando factura a guardar
			Factura factura = new Factura();
			factura.setClienteAsp(clienteAsp);
			factura.setEmpresa(empresa);
			factura.setSucursal(sucursal);
			factura.setClienteEmp(clienteEmp);
			factura.setAfipTipoDeComprobante(afipTipoComprobante);
			factura.setSerie(serie);
			factura.setFecha(facturaFormulario.getFecha());
			factura.setPrefijoSerie(serie.getPrefijo());
			factura.setNumeroComprobante(numeroComprobante);
			factura.setLetraComprobante(letraComprobante);
			factura.setEstado("PENDIENTE");
			this.calcularTotalesFactura(factura, detalles);
			factura.setImpreso(Boolean.FALSE);
			verificarNuevaFactura(factura, errors);
			if(errors.size()==0 ){
				if(facturaService.guardarFactura(factura, detalles)){
					generateAvisoExito("formularioFactura.exito.guardar", atributos);
					accionFactura = Constantes.FACTURA_ACCION_MODIFICAR;
					facturaGuardada = facturaService.obtenerFacturaPorNumeroComprobante(clienteAsp, empresa, sucursal, clienteEmp, serie, factura.getNumeroComprobante());
					facturaFormulario.setId(facturaGuardada!=null?facturaGuardada.getId():null);
				}else{
					errors.add("formularioFactura.error.falloAlGuardar");
					generateErrors(errors, atributos);
				}
			}else{
				generateErrors(errors, atributos);
				//hacemos el redirect
				atributos.put("facturaFormulario", facturaFormulario);
				session.setAttribute("facturaFormularioSession", facturaFormulario);
				atributos.put("accionFactura",accionFactura);
				atributos.put("clienteAspId", clienteAsp.getId());
				atributos.put("comboTipoComprobante", obtenerOpcionesComboTipoComprobante(facturaFormulario.getCodigoCliente(), facturaFormulario.getCodigoEmpresa()));
				atributos.put("facturaDetalles",detalles);
				session.setAttribute("facturaDetallesSession",detalles);
				atributos.put("clienteAspId", clienteAsp.getId());
				//return "formularioFactura";
				return precargaFormularioFactura(session, atributos, accionFactura, null);
			}
			
			
		}
		else if (Constantes.FACTURA_ACCION_MODIFICAR.equalsIgnoreCase(accionFactura)){
			facturaFormulario = facturaService.obtenerPorId(id);
			facturaFormulario.setFechaStr(fechaStr);
			facturaFormulario.setNotasFacturacion(notasFacturacion);
			this.calcularTotalesFactura(facturaFormulario, detalles);
			Set<FacturaDetalle> detallesFactura = new HashSet<FacturaDetalle>(detalles);
			facturaFormulario.setDetallesFactura(detallesFactura);
			if(facturaService.actualizarFacturaYDetalles(facturaFormulario)){
				generateAvisoExito("formularioFactura.exito.guardar", atributos);
			}
			else{
				errors.add("formularioFactura.error.falloAlGuardar");
				generateErrors(errors, atributos);
			}
			
		}
		
		
		//hacemos el redirect
		atributos.put("facturaFormulario", facturaFormulario);
		session.setAttribute("facturaFormularioSession", facturaFormulario);
		atributos.put("accionFactura",accionFactura);
		if(facturaGuardada!=null){
			atributos.put("idFactura",facturaGuardada.getId());
		}
		atributos.put("clienteAspId", clienteAsp.getId());
		atributos.put("comboTipoComprobante", obtenerOpcionesComboTipoComprobante(facturaFormulario.getCodigoCliente(), facturaFormulario.getCodigoEmpresa()));
		atributos.put("facturaDetalles",detalles);
		session.setAttribute("facturaDetallesSession",detalles);
		atributos.put("clienteAspId", clienteAsp.getId());
		//return "formularioFactura";
		return listaComprobantesFacturaController.mostrarListaComprobantesFactura(session, atributos, null, null, null, null, null, null, null, null, null);
	}
	
	@RequestMapping(
			value="/eliminarDetalleFactura.html",
			method= RequestMethod.GET
		)
	public String eliminarDetalleFactura(
			@RequestParam(value="accionFactura",required=false) String accionFactura,
			@RequestParam(value="idEliminar",required=false) Long idEliminar,			
			HttpSession session,
			Map<String,Object> atributos){
		
		List<FacturaDetalle> detalles = (List<FacturaDetalle>)session.getAttribute("facturaDetallesSession");
		if(detalles != null && detalles.size()>0 && idEliminar!=null)
		{
			for(int i = 0;i<detalles.size();i++){
				if(detalles.get(i).getId().longValue() == idEliminar.longValue()){
					detalles.remove(i);
					i--;
				}
			}
		}
//		FacturaDetalle fd = null;
//		if(Constantes.FACTURA_ACCION_NUEVO.equals(accionFactura)){
//			Iterator<FacturaDetalle> it = detalles.iterator();
//			while(it.hasNext()){
//				fd = it.next();
//				if(idEliminar.longValue() == fd.getIdEliminar().longValue()){
//					it.remove();
//					break;
//				}
//			}
//		}
		if(detalles.size()>0){
			atributos.put("headerFacturaNoModificable", Boolean.TRUE);
		}else{
			atributos.put("headerFacturaNoModificable", Boolean.FALSE);
		}
		session.setAttribute("facturaDetallesSession", detalles);
		atributos.put("facturaDetalles", detalles);
		return precargaFormularioFactura(session, atributos, accionFactura, null);
	}
	
	
	/////////////Metodos auxiliares
	
	private void calcularTotalesFactura(Factura factura, List<FacturaDetalle> detalles){
		BigDecimal totalNeto = BigDecimal.valueOf(0D);
		BigDecimal totalImpuestos = BigDecimal.valueOf(0D);
		BigDecimal totalFinal = BigDecimal.valueOf(0D);
		
		for (FacturaDetalle detalle:detalles){
			totalNeto = totalNeto.add(detalle.getNetoTotal());
			totalImpuestos = totalImpuestos.add(detalle.getImpuestoTotal());
			totalFinal = totalFinal.add(detalle.getFinalTotal());
		}
		
		factura.setTotalNeto(totalNeto);
		factura.setTotalIVA(totalImpuestos);
		factura.setTotalFinal(totalFinal);
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
			clienteEmp = service.getByCodigoFactura(clienteEmp, clienteAsp);
			
			//se cargan los codigos para filtrar los tipos de comprobantes que aplican a factura		
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
				Empresa empresa =empresaService.getByCodigoConCondAfip(codigoEmpresa, clienteAsp);
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
	
	private void verificarNuevaFactura(Factura factura, List<String> errors){
		if(facturaService.verificarExistenciaFactura(factura.getSerie(), factura.getNumeroComprobante())){
			errors.add("formularioFactura.error.numeroFacturaYaExiste");
		}
		if(factura.getEmpresa()==null){
			errors.add("formularioFactura.error.empresaRequerida");
		}
		if(factura.getSucursal()==null){
			errors.add("formularioFactura.error.sucursalRequerida");
		}
		if(factura.getClienteEmp()==null){
			errors.add("formularioFactura.error.clienteEmpRequerido");
		}
		if(factura.getAfipTipoDeComprobante()==null){
			errors.add("formularioFactura.error.tipoComprobanteRequerido");
		}
		
		if(factura.getSerie()==null){
			errors.add("formularioFactura.error.serieRequerida");
		}
		if(DateUtil.verificarFechaMayorActual(factura.getFecha())){
			errors.add("formularioFactura.error.fechaMayorActual");
		}
	}
}
