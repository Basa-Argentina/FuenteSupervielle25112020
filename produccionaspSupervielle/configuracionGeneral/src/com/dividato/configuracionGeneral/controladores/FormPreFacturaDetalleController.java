package com.dividato.configuracionGeneral.controladores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PreFacturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PreFacturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.PreFactura;
import com.security.modelo.configuraciongeneral.PreFacturaDetalle;
import com.security.modelo.configuraciongeneral.Impuesto;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados al formulario de
 * User. Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador. A
 * continuación está la anotación @RequestMapping que indica cuales son las URL
 * que mapea este controlador.
 * 
 * @author Victor Kenis (23/09/2011)
 * 
 */
@Controller
@RequestMapping(value = { "/iniciarPrecargaFormularioPreFacturaDetalle.html",
		"/precargaFormularioPreFacturaDetalle.html",
		"/guardarActualizarPreFacturaDetalle.html" })
public class FormPreFacturaDetalleController {

	private PreFacturaService preFacturaService;
	private PreFacturaDetalleService preFacturaDetalleService;
	private FormPreFacturaController formPreFacturaController;
	private ClienteEmpService clienteEmpService;
	private ConceptoFacturableService conceptoFacturableService;
	private EmpresaService empresaService;
	private ListaPreciosService listaPreciosService;

	/**
	 * Setea el servicio de PreFactura. Observar la anotación @Autowired, que le
	 * indica a Spring que debe ejecutar este método e inyectarle un objeto
	 * PreFacturaService. La clase PreFacturaImp implementa PreFactura y está anotada con
	 * @Component, entonces Spring sabe que puede instanciar esta clase y
	 * pasársela a este método.
	 * 
	 * @param preFacturaService
	 */

	@Autowired
	public void setPreFacturaService(PreFacturaService preFacturaService) {
		this.preFacturaService = preFacturaService;
	}

	@Autowired
	public void setPreFacturaDetalleService(
			PreFacturaDetalleService preFacturaDetalleService) {
		this.preFacturaDetalleService = preFacturaDetalleService;
	}

	@Autowired
	public void setListaPreciosService(ListaPreciosService listaPreciosService) {
		this.listaPreciosService = listaPreciosService;
	}

	@Autowired
	public void setConceptoFacturableService(
			ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
	}

	@Autowired
	public void setFormPreFacturaController(
			FormPreFacturaController formPreFacturaController) {
		this.formPreFacturaController = formPreFacturaController;
	}

	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}

	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@RequestMapping(value = "/iniciarPrecargaFormularioPreFacturaDetalle.html", method = RequestMethod.GET)
	public String iniciarPrecargaFormularioPreFacturaDetalle(
			HttpSession session,
			Map<String, Object> atributos,
			@RequestParam(value = "accionPreFactura", required = false) String accionPreFactura,
			@RequestParam(value = "accionPreFacturaDetalle", required = false) String accionPreFacturaDetalle,
			@RequestParam(value = "ordenDetalle", required = false) Long orden,
			@RequestParam(value = "codigoClienteEmp", required = false) String codigoClienteEmp,
			@RequestParam(value = "fechaStr", required = false) String fechaStr,
			@RequestParam(value = "codigoEmpresa", required = false) String codigoEmpresa) {
		
		atributos.remove("preFacturaDetalleFormulario");

		return precargaFormularioPreFacturaDetalle(session, atributos,
				accionPreFactura, accionPreFacturaDetalle, orden, codigoClienteEmp,fechaStr,
				codigoEmpresa);
	}

	/**
	 * Observar la anotación @RequestMapping de SPRING. Mapea la URL
	 * /precargaFormularioUser.html para que ejecute este método cuando venga
	 * GET. Todos los parámetros son inyectados por SPRING cuando ejecuta el
	 * método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios para
	 * mostrar el formulario de Elemento, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion
	 *            parámetro que se recibe del request (Observar la anotación
	 *            @RequestParam)
	 * @param idUser
	 *            parámetro que se recibe del request (Observar la anotación
	 *            @RequestParam)
	 * @param session
	 *            es la misma sesión que usabamos con los Servlets.
	 * @param atributos
	 *            son los atributos del request
	 * @return "formularioPreFactura" la vista que debe mostrarse (ver
	 *         dispatcher-servlet.xml/viewResolver)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/precargaFormularioPreFacturaDetalle.html", method = RequestMethod.GET)
	public String precargaFormularioPreFacturaDetalle(
			HttpSession session,
			Map<String, Object> atributos,
			@RequestParam(value = "accionPreFactura", required = false) String accionPreFactura,
			@RequestParam(value = "accionPreFacturaDetalle", required = false) String accionPreFacturaDetalle,
			@RequestParam(value = "ordenDetalle", required = false) Long orden,
			@RequestParam(value = "codigoClienteEmp", required = false) String codigoClienteEmp,
			@RequestParam(value = "fechaStr", required = false) String fechaStr,
			@RequestParam(value = "codigoEmpresa", required = false) String codigoEmpresa) {

		@SuppressWarnings("unused")
		Sucursal sucursal = obtenerSucursalUser();
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		
		List<PreFacturaDetalle> preFacturaDetallesSession = (List<PreFacturaDetalle>) session.getAttribute("preFacturaDetallesSession");
		
		Empresa empresa = empresaService.getByCodigo(codigoEmpresa, clienteAsp);
		if (empresa == null) {
			empresa = obtenerEmpresaUser();
		}
		PreFactura preFacturaFormulario = (PreFactura) session.getAttribute("preFacturaFormularioSession");
		if(fechaStr!= null && !fechaStr.equals("")){
			preFacturaFormulario.setFechaStr(fechaStr);
		}
		PreFacturaDetalle preFacturaDetalleFormulario = (PreFacturaDetalle) atributos.get("preFacturaDetalleFormulario");
		if (preFacturaDetalleFormulario == null) {
			preFacturaDetalleFormulario = new PreFacturaDetalle();
		}

		if (accionPreFacturaDetalle == null) {
			accionPreFacturaDetalle = "NUEVO"; // acción por defecto: nuevo
		}
		if (accionPreFacturaDetalle.equals("CONSULTAR") || accionPreFacturaDetalle.equals("MODIFICACION")) {
			if(orden!=null){
				for(PreFacturaDetalle fd: preFacturaDetallesSession){
					if(fd.getOrden().longValue()==orden.longValue()){
						preFacturaDetalleFormulario=fd;
						break;
					}
				}
				if(preFacturaDetalleFormulario.getConceptoFacturable() != null)
					preFacturaDetalleFormulario.setCodigoConcepto(preFacturaDetalleFormulario.getConceptoFacturable().getCodigo());
				if(preFacturaDetalleFormulario.getListaprecios() != null)
					preFacturaDetalleFormulario.setListaPreciosCodigo(preFacturaDetalleFormulario.getListaprecios().getCodigo());
				
			}
		}
		atributos.put("idAfipTipoComprobante", preFacturaFormulario.getIdAfipTipoComprobante());
		atributos.put("preFacturaDetalleFormulario", preFacturaDetalleFormulario);
		atributos.put("accionPreFactura", accionPreFactura);
		atributos.put("accionPreFacturaDetalle", accionPreFacturaDetalle);
		atributos.put("ordenDetalle", orden);
		atributos.put("clienteAspId", clienteAsp.getId());
		atributos.put("codigoClienteEmp", codigoClienteEmp);
		atributos.put("codigoEmpresa", empresa.getCodigo());
		return "formularioPreFacturaDetalle";
	}

	/**
	 * Observar la anotación @RequestMapping de SPRING. Todos los parámetros son
	 * inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos Elemento.
	 * 
	 * @param Elemento
	 *            user a guardar. Observar la anotación @ModelAttribute que le
	 *            indica a SPRING que debe intentar llenar el objeto Elemento
	 *            con los parámetros del request.
	 * @param session
	 *            es la misma sesión que usabamos con los Servlets.
	 * @param atributos
	 *            son los atributos del request
	 * @return "formularioElemento" la vista que debe mostrarse (ver
	 *         dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(value = "/guardarActualizarPreFacturaDetalle.html", method = RequestMethod.POST)
	public String guardarActualizarPreFacturaDetalle(
			@RequestParam(value = "accionPreFactura", required = false) String accionPreFactura,
			@RequestParam(value = "accionPreFacturaDetalle", required = false) String accionPreFacturaDetalle,
			@RequestParam(value = "ordenDetalle", required = false) Long orden,
			@RequestParam(value = "codigoClienteEmp", required = false) String codigoClienteEmp,
			@RequestParam(value = "codigoEmpresa", required = false) String codigoEmpresa,
			@ModelAttribute("preFacturaDetalleFormulario") PreFacturaDetalle preFacturaDetalleFormulario,
			BindingResult result, HttpSession session,
			Map<String, Object> atributos) {

		Boolean commit = null;
		PreFactura preFacturaFormulario = (PreFactura) session.getAttribute("preFacturaFormularioSession");
		@SuppressWarnings("unchecked")
		List<PreFacturaDetalle> preFacturaDetallesSession = (List<PreFacturaDetalle>) session.getAttribute("preFacturaDetallesSession");
		if (preFacturaDetallesSession == null) {
			preFacturaDetallesSession = new ArrayList<PreFacturaDetalle>();
		}
		ArrayList<String> errors = new ArrayList<String>();
		PreFacturaDetalle preFacturaDetalle = null;

		if (accionPreFacturaDetalle == null || accionPreFacturaDetalle.equals("")
				|| accionPreFacturaDetalle.equalsIgnoreCase("NUEVO")) {
			accionPreFacturaDetalle = "NUEVO";
			preFacturaDetalle = new PreFacturaDetalle();
		} else if (!"CONSULTA".equalsIgnoreCase(accionPreFacturaDetalle)) {
			accionPreFacturaDetalle = "MODIFICACION";
			for(PreFacturaDetalle detalle: preFacturaDetallesSession){
				if(detalle.getOrden().longValue() == orden.longValue()){
					preFacturaDetalle = detalle;
				}
			}
			
		}
			Long signo = new Long(1);
			Long cantidad = new Long(1); //Ponemos en el caso de que el tipo de calculo sea unico
			if(preFacturaDetalleFormulario.getCantidad() != null)
				cantidad = preFacturaDetalleFormulario.getCantidad().longValue();
			if(cantidad==null)
				cantidad = new Long(1);
			
			if(preFacturaFormulario.getIdAfipTipoComprobante().longValue()==3){
				signo = -signo;
			}
			
			preFacturaDetalle.setCantidad(preFacturaDetalleFormulario.getCantidad());
			preFacturaDetalle.setDescripcion(preFacturaDetalleFormulario.getDescripcion());
			ClienteAsp clienteAsp = obtenerClienteAspUser();
			
			
			ConceptoFacturable concepto = conceptoFacturableService.obtenerConceptoFacturablePorCodigo(preFacturaDetalleFormulario.getCodigoConcepto(),clienteAsp);
			preFacturaDetalle.setConceptoFacturable(concepto);
			ClienteEmp clienteEmp = clienteEmpService.getByCodigo(codigoClienteEmp, codigoEmpresa, clienteAsp);

			ListaPrecios listaPrecios = null;
			if (concepto != null) {
				listaPrecios = listaPreciosService.obtenerListaPreciosPorCodigo(preFacturaDetalleFormulario.getListaPreciosCodigo(), clienteAsp, concepto.getCodigo(), clienteEmp, true);
				preFacturaDetalle.setListaprecios(listaPrecios);
				if (listaPrecios != null) {
					BigDecimal uno = BigDecimal.valueOf(1L);
					BigDecimal cien = BigDecimal.valueOf(100L);
					preFacturaDetalle.setListaprecios(listaPrecios);
					preFacturaDetalle.setCosto(concepto.getCosto());
					preFacturaDetalle.setPrecioBase(concepto.getPrecioBase());
					Impuesto impuesto = concepto.getImpuesto();
					BigDecimal valor = listaPrecios.getValor();
					if (valor != null) {
						
						//BigDecimal netoUnitario = variacionPrecio.multiply(concepto.getPrecioBase());
						//preFacturaDetalle.setNetoUnitario(netoUnitario);
						
						//////////////////////////////////////////////////////////////////////////////////
						////////////////METODO: EL PRECIO TIENE IMPUESTOS
//						BigDecimal variacionPrecio = uno.add(valor.divide(cien));
//						BigDecimal finalUnitario  = variacionPrecio.multiply(concepto.getPrecioBase());
//						preFacturaDetalle.setFinalUnitario(finalUnitario);
//						preFacturaDetalle.setFinalTotal(finalUnitario.multiply(new BigDecimal(cantidad)));
//						
//						preFacturaDetalle.setIVA(impuesto != null ? impuesto.getAlicuota() : null);
//						
//						if (impuesto != null && impuesto.getAlicuota() != null) {
//							
//							preFacturaDetalle.setNetoUnitario(finalUnitario.divide(((impuesto.getAlicuota().divide(new BigDecimal(100))).add(new BigDecimal(1))), 4, RoundingMode.HALF_UP));
//							preFacturaDetalle.setNetoTotal(preFacturaDetalle.getNetoUnitario().multiply(new BigDecimal(cantidad)));
//							preFacturaDetalle.setImpuestoUnitario(finalUnitario.subtract(preFacturaDetalle.getNetoUnitario()));
//							preFacturaDetalle.setImpuestoTotal((finalUnitario.subtract(preFacturaDetalle.getNetoUnitario()).multiply(BigDecimal.valueOf(cantidad))));
//							
//						}
						
						//////////////////////////////////////////////////////////////////////////////////
						////////////////METODO: EL PRECIO NO TIENE IMPUESTOS
						BigDecimal variacionPrecio = uno.add(valor.divide(cien));
						BigDecimal netoUnitario  = variacionPrecio.multiply(concepto.getPrecioBase()).multiply(new BigDecimal(signo));
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
				}
			}

//			if (validarPreFacturaDetalle(preFacturaDetalle, errors)) {
//				//preFacturaDetalle.setIdEliminar(getProximoIdEliminar(preFacturaDetallesSession));
//				generateAvisoExito("formularioPreFacturaDetalle.exito.registrar",atributos);
				
				if (accionPreFacturaDetalle.equalsIgnoreCase("NUEVO")){
					Integer ordenDetalle = preFacturaDetallesSession.get(preFacturaDetallesSession.size()-1).getOrden();
					for(int i=0;i<preFacturaDetallesSession.size();i++){
						if(ordenDetalle.longValue() == preFacturaDetallesSession.get(i).getOrden().longValue()){
							ordenDetalle++;
							i--;
						}
					}
					preFacturaDetalle.setOrden(ordenDetalle);
					preFacturaDetallesSession.add(preFacturaDetalle);
					preFacturaDetalleFormulario = new PreFacturaDetalle();
				}else{
					session.setAttribute("preFacturaDetallesSession", preFacturaDetallesSession);
					atributos.put("codigoClienteEmp", codigoClienteEmp);
					atributos.put("accionPreFactura", accionPreFactura);
					atributos.put("codigoEmpresa", codigoEmpresa);
					atributos.put("preFacturaDetalleFormulario", preFacturaDetalleFormulario);
					return "redirect:precargaFormularioPreFactura.html";
				}
				
//			} else {
//				generateErrors(errors, atributos);
//			}
		

		session.setAttribute("preFacturaDetallesSession", preFacturaDetallesSession);
		atributos.put("codigoClienteEmp", codigoClienteEmp);
		atributos.put("accionPreFactura", accionPreFactura);
		atributos.put("codigoEmpresa", codigoEmpresa);
		atributos.put("preFacturaDetalleFormulario", preFacturaDetalleFormulario);
		// hacemos el redirect
		return "formularioPreFacturaDetalle";
	}

	private ClienteAsp obtenerClienteAspUser() {
		return ((User) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal()).getCliente();
	}

	private User obtenerUser() {
		return ((User) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal());
	}

	private Empresa obtenerEmpresaUser() {
		return ((PersonaFisica) obtenerUser().getPersona()).getEmpresaDefecto();
	}

	private Sucursal obtenerSucursalUser() {
		return ((PersonaFisica) obtenerUser().getPersona())
				.getSucursalDefecto();
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

	private boolean validarPreFacturaDetalle(PreFacturaDetalle preFacturaDetalle,
			List<String> errors) {
		boolean result = true;
		if (preFacturaDetalle.getDescripcion().trim().equals("")
				|| preFacturaDetalle.getDescripcion().length()<1 
				|| preFacturaDetalle.getDescripcion().equalsIgnoreCase("Descripción:")) {
			errors.add("formularioPreFacturaDetalle.error.descripcion");
			result = false;
		}
		if (preFacturaDetalle.getCantidad() == null
				|| preFacturaDetalle.getCantidad().intValue() < 1) {
			errors.add("formularioPreFacturaDetalle.error.cantidad");
			result = false;
		}
		if (preFacturaDetalle.getConceptoFacturable() == null) {
			errors.add("formularioPreFacturaDetalle.error.codigoConcepto");
			result = false;
		}
		if (preFacturaDetalle.getListaprecios() == null) {
			errors.add("formularioPreFacturaDetalle.error.listaPrecios");
			result = false;
		} else if (preFacturaDetalle.getListaprecios().getValor() == null) {
			errors.add("formularioPreFacturaDetalle.error.listaPrecios.value");
			result = false;
		}

		if (preFacturaDetalle.getFinalTotal() == null) {
			errors.add("formularioPreFacturaDetalle.error.datosInsuficientes");
			result = false;
		}
		// TODO especificar los datos que impiden calcular el precio finalTotal
		if (preFacturaDetalle.getCantidad() == null
				|| preFacturaDetalle.getCantidad() <= 0) {
			errors.add("formularioPreFacturaDetalle.error.cantidad");
			result = false;
		}
		return result;
	}
	
	private Long getProximoIdEliminar(List<PreFacturaDetalle> detalles){
		Long mayor = Long.valueOf(0L);
		if(detalles != null && detalles.size()>0){
			for(PreFacturaDetalle fd : detalles){
				if(mayor<fd.getIdEliminar()){
					mayor = fd.getIdEliminar();
				}
			}
		}
		return mayor + 1L;
	}
}
