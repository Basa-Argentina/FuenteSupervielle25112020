package com.dividato.configuracionGeneral.controladores;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.CuentaCorrienteValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DoctoCtaCteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.MedioPagoReciboService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.DoctoCtaCte;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.FacturaDetalle;
import com.security.modelo.configuraciongeneral.MedioPagoRecibo;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
import com.security.utils.ParseNumberUtils;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de CuentaCorriente.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author X
 *
 *
 */
@Controller
@RequestMapping(
		value=
			{	"/inicioFormularioCuentaCorriente.html",
				"/precargaFormularioCuentaCorriente.html",
				"/guardarActualizarCuentaCorriente.html",
				"/setImporte.html"
			}
		)
public class FormCuentaCorrienteController {
	
	private ClienteEmpService clienteEmpService;
	private FacturaService facturaService;
	private AfipTipoComprobanteService afipTipoComprobanteService;
	private MedioPagoReciboService medioPagoReciboService;
	private SerieService serieService;
	private SucursalService sucursalService;
	private DoctoCtaCteService doctoCtaCteService; 
	private EmpresaService empresaService;
	@SuppressWarnings("unused")
	private ListaCuentaCorrienteController listaCuentaCorrientesController;
	private CuentaCorrienteValidator validator;
	
	@Autowired
	public void setService(ClienteEmpService clienteEmpService,
			FacturaService facturaService,
			AfipTipoComprobanteService afipTipoComprobanteService,
			MedioPagoReciboService medioPagoReciboService,
			SerieService serieService,
			SucursalService sucursalService,
			DoctoCtaCteService doctoCtaCteService,
			EmpresaService empresaService) {
		this.clienteEmpService = clienteEmpService;
		this.facturaService = facturaService;
		this.afipTipoComprobanteService = afipTipoComprobanteService;
		this.medioPagoReciboService = medioPagoReciboService;
		this.serieService = serieService;
		this.sucursalService = sucursalService;
		this.doctoCtaCteService = doctoCtaCteService;
		this.empresaService = empresaService;
	}
	@Autowired
	public void setListaCuentaCorrientesController(ListaCuentaCorrienteController listaCuentaCorrientesController) {
		this.listaCuentaCorrientesController = listaCuentaCorrientesController;
	}
	
	@Autowired
	public void setValidator(CuentaCorrienteValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(
			value="/inicioFormularioCuentaCorriente.html",
			method = RequestMethod.GET
		)
	public String inicioFormularioCuentaCorriente(HttpSession session,
			Map<String,Object> atributos){
		
		session.removeAttribute("comprobanteList");
		session.removeAttribute("medioPagoList");
		session.removeAttribute("facturaDetallesSession");
		session.removeAttribute("facturaFormularioSession");
		atributos.remove("facturaFormulario");
		return "redirect:precargaFormularioCuentaCorriente.html";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(
			value="/guardarActualizarCuentaCorriente.html",
			method= RequestMethod.POST
		)
	public String guardarActualizarCuentaCorriente(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="sinImputar",required=false) String sinImputar,
			@RequestParam(value="imputadoTotal",required=false) String imputadoTotal,
			@ModelAttribute("facturaFormulario") final Factura facturaFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		// Validar los datos requeridos crear los mensajes.
		List<String> codigoErrores = new ArrayList<String>();
		// Cliente
		if(facturaFormulario.getCodigoCliente()==null || facturaFormulario.getCodigoCliente().trim().length()==0)
		{
			codigoErrores.add("formularioCuentaCorriente.errorCliente");
		}
		// Serie
		if(facturaFormulario.getCodigoSerie()==null || facturaFormulario.getCodigoSerie().trim().length()==0)
		{
			codigoErrores.add("formularioCuentaCorriente.errorSerie");
			if(facturaFormulario.getNumeroStr()==null || facturaFormulario.getNumeroStr().trim().length()==0)
			{
				codigoErrores.add("formularioCuentaCorriente.errorNumero");
			}
		}
		
		// Inicio Grilla medio pago recibo
		List<MedioPagoRecibo> medioPagoList = (List<MedioPagoRecibo>)session.getAttribute("medioPagoList");
		if(medioPagoList==null  || medioPagoList.isEmpty()){
			codigoErrores.add("formularioCuentaCorriente.errorMedioPagoRecibo");
		}
		// Fin Grilla medio pago recibo 
		// Inicio Grilla Comprobante
		List<Factura> comprobanteList = (List<Factura>)session.getAttribute("comprobanteList");
		if(comprobanteList==null  || comprobanteList.isEmpty()){
			codigoErrores.add("formularioCuentaCorriente.errorComprobante");
		}
		// Inicio Valido que todos los comprobantes tengan importe imputado
		if(comprobanteList!=null){
			for(Factura fac:comprobanteList){
				if(fac.getImputado()==null || fac.getImputado().doubleValue()==0.0){
					codigoErrores.add("formularioCuentaCorriente.errorComprobanteImporte");
					break;
				}
			}
		}
		// Fin Grilla Comprobante
		// Inicio Validar Saldos Medio Cobro y Comprobante
		Double sinImputarValue = sinImputar!=null?Double.valueOf(sinImputar.replace(",", ".")):0.0;
		if(!sinImputarValue.equals(0.0)){
			codigoErrores.add("formularioCuentaCorriente.errorMedioCobroComprobanteImputado");
		}
		result = generateErrors(codigoErrores);	
		// Fin Validar Saldos Medio Cobro y Comprobante 
		// Crear Objeto tipo factura de tipo recibo (X)
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		ArrayList<String> errors = new ArrayList<String>();
		List<FacturaDetalle> detalles = (List<FacturaDetalle>)session.getAttribute("facturaDetallesSession");

		ClienteAsp clienteAsp = obtenerClienteAspUser();
		Factura facturaGuardada = null;

		if(accion.equalsIgnoreCase("NUEVO") && !result.hasErrors()){
			//TODO validar datos facturaFormulario
			Empresa empresa = empresaService.getByCodigo(facturaFormulario.getCodigoEmpresa(), clienteAsp);
			Sucursal sucursal = sucursalService.getByCodigo(facturaFormulario.getCodigoSucursal(), empresa.getCodigo(), clienteAsp);
			ClienteEmp clienteEmp = clienteEmpService.getByCodigo(facturaFormulario.getCodigoCliente(), empresa.getCodigo(), clienteAsp, Boolean.TRUE);
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
			factura.setImpreso(Boolean.FALSE);
			if(imputadoTotal!=null){
				factura.setTotalFinal(BigDecimal.valueOf(Double.valueOf(imputadoTotal.replace(",", "."))));
			}
			
			
			if(errors.size()==0 ){
				if(facturaService.guardarFactura(factura, detalles)){
					//generateAvisoExito("formularioFactura.exito.guardar", atributos);
					accion = Constantes.FACTURA_ACCION_MODIFICAR;
					facturaGuardada = facturaService.obtenerFacturaPorNumeroComprobante(clienteAsp, empresa, sucursal, clienteEmp, serie, factura.getNumeroComprobante());
					facturaFormulario.setId(facturaGuardada!=null?facturaGuardada.getId():null);
					// Guardar en la tabla los medioPagoRecibo
					for (MedioPagoRecibo m:medioPagoList){
						m.setFactura(facturaFormulario);
						if(m.getId()==null){
							medioPagoReciboService.guardar(m);
						}else{
							medioPagoReciboService.actualizar(m);
						}
						
					}
					// Guardar en la tabla doctoCtaCte
					for (Factura f:comprobanteList){
						DoctoCtaCte doctoCtaCte = new DoctoCtaCte();
						doctoCtaCte.setNc_rc(facturaFormulario);
						doctoCtaCte.setFc_nd(f);
						doctoCtaCte.setImporte(f.getImputado().doubleValue());
						// Servicio que guarda en la tabla.
						doctoCtaCteService.guardar(doctoCtaCte);
					}
					commit = true;
				}else{
					errors.add("formularioFactura.error.falloAlGuardar");
				}
			}else{
				//generateErrors(errors, atributos);
				//hacemos el redirect
				atributos.put("facturaFormulario", facturaFormulario);
				session.setAttribute("facturaFormularioSession", facturaFormulario);
				atributos.put("accionFactura",accion);
				atributos.put("clienteAspId", clienteAsp.getId());
				//atributos.put("comboTipoComprobante", obtenerOpcionesComboTipoComprobante(facturaFormulario.getCodigoCliente(), facturaFormulario.getCodigoEmpresa()));
				atributos.put("facturaDetalles",detalles);
				session.setAttribute("facturaDetallesSession",detalles);
				atributos.put("clienteAspId", clienteAsp.getId());
				return precargaFormularioCuentaCorriente(session, atributos, accion, null, null, null, null, null);
			}
			
			
		}
		
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("facturaFormulario", facturaFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioCuentaCorriente(session, atributos, accion, facturaFormulario.getId(), null, null, null, null);
		}	
		
		if(result.hasErrors()){
			atributos.put("facturaFormulario", facturaFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioCuentaCorriente(session, atributos, accion, facturaFormulario.getId(), null, null, null, null);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeCuentaCorrienteReg = new ScreenMessageImp("formularioCuentaCorriente.notificacion.clienteRegistrado", null);
			avisos.add(mensajeCuentaCorrienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		return precargaFormularioCuentaCorriente(session, atributos, accion, facturaFormulario.getId(), null, null, null, null);
	}
	
	@RequestMapping(
			value="/precargaFormularioCuentaCorriente.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioCuentaCorriente(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion", required=false) String accion,
			@RequestParam(value="idFactura", required=false) Long id,
			@RequestParam(value="codigoSerie", required=false) String codigoSerie,
			@RequestParam(value="codigoCliente", required=false) String codigoCliente,
			@RequestParam(value="numeroComprobanteStr", required=false) String numeroComprobanteStr,
			@RequestParam(value="notasFacturacion", required=false) String notasFacturacion
		){
		
		Factura facturaFormulario = (Factura) session.getAttribute("facturaFormularioSession");
		if(facturaFormulario==null)
		{facturaFormulario = new Factura();}
		
		Empresa empresa = obtenerEmpresaUser();
		Sucursal sucursal = obtenerSucursalUser();
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		
		if(codigoSerie!=null){
			facturaFormulario.setCodigoSerie(codigoSerie);
		}
		if(codigoCliente!=null){
			facturaFormulario.setCodigoCliente(codigoCliente);
		}
		
		if(numeroComprobanteStr!=null){
			facturaFormulario.setNumeroComprobanteStr(numeroComprobanteStr);
		}
		if(notasFacturacion!=null){
			facturaFormulario.setNotasFacturacion(notasFacturacion);
		}
		
		if(accion==null) {
			accion=Constantes.FACTURA_ACCION_NUEVO; //acción por defecto: nuevo
			facturaFormulario.setCodigoEmpresa(empresa.getCodigo());
			facturaFormulario.setCodigoSucursal(sucursal.getCodigo());
			facturaFormulario.setEmpresa(empresa);
			facturaFormulario.setSucursal(sucursal);
			facturaFormulario.setIdAfipTipoComprobante(4L);
			facturaFormulario.setAfipTipoDeComprobante(afipTipoComprobanteService.obtenerPorId(4L));
			session.setAttribute("facturaFormularioSession", facturaFormulario);
		}
		if(!accion.equals(Constantes.FACTURA_ACCION_NUEVO)){
			facturaFormulario = (Factura) session.getAttribute("facturaFormularioSession");
			if(facturaFormulario==null){
				facturaFormulario = facturaService.obtenerPorId(id);
				facturaFormulario.setCodigoEmpresa(facturaFormulario.getEmpresa().getCodigo());
				facturaFormulario.setCodigoSucursal(facturaFormulario.getSucursal().getCodigo());
				facturaFormulario.setCodigoSerie(facturaFormulario.getSerie().getCodigo());
				facturaFormulario.setCodigoCliente(facturaFormulario.getClienteEmp().getCodigo());
				facturaFormulario.setIdAfipTipoComprobante(4L);
				facturaFormulario.setAfipTipoDeComprobante(afipTipoComprobanteService.obtenerPorId(4L));
				
			}
			session.setAttribute("facturaFormularioSession", facturaFormulario);
			atributos.put("facturaFormulario", facturaFormulario);
		}
		atributos.put("headerFacturaNoModificable", Boolean.FALSE);
		atributos.put("comboTipoComprobante", null);
		atributos.put("facturaFormulario", facturaFormulario);
		session.setAttribute("facturaFormularioSession", facturaFormulario);
		atributos.put("accion",accion);
		atributos.put("idFactura",id);
		atributos.put("clienteAspId", clienteAsp.getId());
		
		// Inicio Calculo total Medio Pago
		Double medioPagoTotal = 0.0;
		List<MedioPagoRecibo> medioPagoList = (List<MedioPagoRecibo>)session.getAttribute("medioPagoList");
		if(medioPagoList!=null){
			for(MedioPagoRecibo m:medioPagoList){
				medioPagoTotal+= m.getImporte();
			}
			
		}
		atributos.put("medioPagoTotal", medioPagoTotal);
		// Fin Calculo total Medio Pago
		// Inicio Calculo total Comprobante
		Double comprobanteTotal = 0.0;
		List<Factura> comprobanteList = (List<Factura>)session.getAttribute("comprobanteList");
		if(comprobanteList!=null){
			for(Factura f:comprobanteList){
				comprobanteTotal+= f.getImputado()!=null?f.getImputado().doubleValue():0.0;
			}
			
		}
		atributos.put("comprobanteTotal", comprobanteTotal);
		// Fin Calculo total Comprobante
		// Inicio Calculo Sin imputar.
		Double sinImputar = medioPagoTotal - comprobanteTotal;
		atributos.put("sinImputar", sinImputar);
		// Fin Calculo Sin imputar.
		return "formularioCuentaCorriente";
	}
	
	@RequestMapping(value="/setImporte.html")
	public String setImporte(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="id",required=true) Long id,
			@RequestParam(value="importe",required=true) Double importe,
			HttpServletRequest request)
	{
		
		List<Factura> comprobanteList = (List<Factura>)session.getAttribute("comprobanteList");
		for (Factura f:comprobanteList){
			if(f.getId()==id){
				f.setImputado(BigDecimal.valueOf(importe));
			}
		}
		return "formularioCuentaCorriente";
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
	
	private BindingResult generateErrors(List<String> codigoErrores) {
		BindingResult result = new BeanPropertyBindingResult(new Object(),"");
		if (!codigoErrores.isEmpty()) {
			for (String codigo : codigoErrores) {
				result.addError(new FieldError(	"error.formBookingGroup.general", codigo, null, false, new String[] { codigo }, null, "?"));
			}
		}
		return result;
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
}
