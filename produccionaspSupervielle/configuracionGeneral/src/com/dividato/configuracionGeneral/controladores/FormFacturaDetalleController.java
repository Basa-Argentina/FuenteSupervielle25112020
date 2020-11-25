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
import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.FacturaDetalle;
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
@RequestMapping(value = { "/iniciarPrecargaFormularioFacturaDetalle.html",
		"/precargaFormularioFacturaDetalle.html",
		"/guardarActualizarFacturaDetalle.html" })
public class FormFacturaDetalleController {

	private FacturaService facturaService;
	private FacturaDetalleService facturaDetalleService;
	private FormFacturaController formFacturaController;
	private ClienteEmpService clienteEmpService;
	private ConceptoFacturableService conceptoFacturableService;
	private EmpresaService empresaService;
	private ListaPreciosService listaPreciosService;

	/**
	 * Setea el servicio de Factura. Observar la anotación @Autowired, que le
	 * indica a Spring que debe ejecutar este método e inyectarle un objeto
	 * FacturaService. La clase FacturaImp implementa Factura y está anotada con
	 * @Component, entonces Spring sabe que puede instanciar esta clase y
	 * pasársela a este método.
	 * 
	 * @param facturaService
	 */

	@Autowired
	public void setFacturaService(FacturaService facturaService) {
		this.facturaService = facturaService;
	}

	@Autowired
	public void setFacturaDetalleService(
			FacturaDetalleService facturaDetalleService) {
		this.facturaDetalleService = facturaDetalleService;
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
	public void setFormFacturaController(
			FormFacturaController formFacturaController) {
		this.formFacturaController = formFacturaController;
	}

	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}

	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@RequestMapping(value = "/iniciarPrecargaFormularioFacturaDetalle.html", method = RequestMethod.GET)
	public String iniciarPrecargaFormularioFacturaDetalle(
			HttpSession session,
			Map<String, Object> atributos,
			@RequestParam(value = "accionFactura", required = false) String accionFactura,
			@RequestParam(value = "accionFacturaDetalle", required = false) String accionFacturaDetalle,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "codigoClienteEmp", required = false) String codigoClienteEmp,
			@RequestParam(value = "fechaStr", required = false) String fechaStr,
			@RequestParam(value = "codigoEmpresa", required = false) String codigoEmpresa) {
		atributos.remove("facturaDetalleFormulario");
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		// ClienteEmp clienteEmp =
		// clienteEmpService.getByCodigo(codigoClienteEmp, codigoEmpresa,
		// clienteAsp);
		
		return precargaFormularioFacturaDetalle(session, atributos,
				accionFactura, accionFacturaDetalle, id, codigoClienteEmp,fechaStr,
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
	 * @return "formularioFactura" la vista que debe mostrarse (ver
	 *         dispatcher-servlet.xml/viewResolver)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/precargaFormularioFacturaDetalle.html", method = RequestMethod.GET)
	public String precargaFormularioFacturaDetalle(
			HttpSession session,
			Map<String, Object> atributos,
			@RequestParam(value = "accionFactura", required = false) String accionFactura,
			@RequestParam(value = "accionFacturaDetalle", required = false) String accionFacturaDetalle,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "codigoClienteEmp", required = false) String codigoClienteEmp,
			@RequestParam(value = "fechaStr", required = false) String fechaStr,
			@RequestParam(value = "codigoEmpresa", required = false) String codigoEmpresa) {

		@SuppressWarnings("unused")
		Sucursal sucursal = obtenerSucursalUser();
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		List<FacturaDetalle> facturaDetallesSession = (List<FacturaDetalle>) session.getAttribute("facturaDetallesSession");
		Empresa empresa = empresaService.getByCodigo(codigoEmpresa, clienteAsp);
		if (empresa == null) {
			empresa = obtenerEmpresaUser();
		}
		Factura facturaFormulario = (Factura) session.getAttribute("facturaFormularioSession");
		if(fechaStr!= null && !fechaStr.equals("")){
			facturaFormulario.setFechaStr(fechaStr);
		}
		FacturaDetalle facturaDetalleFormulario = (FacturaDetalle) atributos.get("facturaDetalleFormulario");
		if (facturaDetalleFormulario == null) {
			facturaDetalleFormulario = new FacturaDetalle();
			
			ListaPrecios listaPrecios = clienteEmpService.listaPrecioDefectoPorCliente(facturaFormulario.getCodigoCliente(), obtenerClienteAspUser());
			if(listaPrecios!=null)
				atributos.put("codigoListaDefecto", listaPrecios.getCodigo());
		}

		if (accionFacturaDetalle == null) {
			accionFacturaDetalle = "NUEVO"; // acción por defecto: nuevo
		}
		if(accionFacturaDetalle.equals("NUEVO")){
			facturaDetalleFormulario.setCantidad((long) 1);	
		}
		if (accionFacturaDetalle.equals("CONSULTAR") || accionFacturaDetalle.equals("MODIFICACION")) {
			if(id!=null){
				for(FacturaDetalle fd: facturaDetallesSession){
					if(fd.getId().longValue()==id.longValue()){
						facturaDetalleFormulario=fd;
						break;
					}
				}
				facturaDetalleFormulario.setCodigoConcepto(facturaDetalleFormulario.getConceptoFacturable().getCodigo());
				facturaDetalleFormulario.setListaPreciosCodigo(facturaDetalleFormulario.getListaprecios().getCodigo());
				
			}
		}
		atributos.put("idAfipTipoComprobante", facturaFormulario.getIdAfipTipoComprobante());
		atributos.put("facturaDetalleFormulario", facturaDetalleFormulario);
		atributos.put("accionFactura", accionFactura);
		atributos.put("accionFacturaDetalle", accionFacturaDetalle);
		atributos.put("id", id);
		atributos.put("clienteAspId", clienteAsp.getId());
		atributos.put("codigoClienteEmp", codigoClienteEmp);
		atributos.put("codigoEmpresa", empresa.getCodigo());
		return "formularioFacturaDetalle";
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
	@RequestMapping(value = "/guardarActualizarFacturaDetalle.html", method = RequestMethod.POST)
	public String guardarActualizarFacturaDetalle(
			@RequestParam(value = "accionFactura", required = false) String accionFactura,
			@RequestParam(value = "accionFacturaDetalle", required = false) String accionFacturaDetalle,
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "codigoClienteEmp", required = false) String codigoClienteEmp,
			@RequestParam(value = "codigoEmpresa", required = false) String codigoEmpresa,
			@RequestParam(value = "salir", required = false) Boolean salir,
			@ModelAttribute("facturaDetalleFormulario") FacturaDetalle facturaDetalleFormulario,
			BindingResult result, HttpSession session,
			Map<String, Object> atributos) {

		Boolean commit = null;
		Factura facturaFormulario = (Factura) session.getAttribute("facturaFormularioSession");
		@SuppressWarnings("unchecked")
		List<FacturaDetalle> facturaDetallesSession = (List<FacturaDetalle>) session.getAttribute("facturaDetallesSession");
		if (facturaDetallesSession == null) {
			facturaDetallesSession = new ArrayList<FacturaDetalle>();
		}
		ArrayList<String> errors = new ArrayList<String>();
		FacturaDetalle facturaDetalle = null;

		if (accionFacturaDetalle == null || accionFacturaDetalle.equals("")
				|| accionFacturaDetalle.equalsIgnoreCase("NUEVO")) {
			accionFacturaDetalle = "NUEVO";
			facturaDetalle = new FacturaDetalle();
		} else if (!"CONSULTA".equalsIgnoreCase(accionFacturaDetalle)) {
			accionFacturaDetalle = "MODIFICACION";
			for(FacturaDetalle detalle: facturaDetallesSession){
				if(detalle.getId().longValue() == id.longValue()){
					facturaDetalle = detalle;
				}
			}
			
		}
			Long signo = new Long(1);
			Long cantidad = new Long(1); //Ponemos en el caso de que el tipo de calculo sea unico
			if(facturaDetalleFormulario.getCantidad() != null)
				cantidad = facturaDetalleFormulario.getCantidad().longValue();
			if(cantidad==null)
				cantidad = new Long(1);
			
			if(facturaFormulario.getIdAfipTipoComprobante().longValue()==3){
				signo = -signo;
			}
			
			facturaDetalle.setCantidad(facturaDetalleFormulario.getCantidad());
			facturaDetalle.setDescripcion(facturaDetalleFormulario.getDescripcion());
			ClienteAsp clienteAsp = obtenerClienteAspUser();
			
			
			ConceptoFacturable concepto = conceptoFacturableService.obtenerConceptoFacturablePorCodigo(facturaDetalleFormulario.getCodigoConcepto(),clienteAsp);
			facturaDetalle.setConceptoFacturable(concepto);
			ClienteEmp clienteEmp = clienteEmpService.getByCodigo(codigoClienteEmp, codigoEmpresa, clienteAsp);

			ListaPrecios listaPrecios = null;
			if (concepto != null) {
				listaPrecios = listaPreciosService.obtenerListaPreciosPorCodigo(facturaDetalleFormulario.getListaPreciosCodigo(), clienteAsp, concepto.getCodigo(), clienteEmp, true);
				facturaDetalle.setListaprecios(listaPrecios);
				if (listaPrecios != null) {
					BigDecimal uno = BigDecimal.valueOf(1L);
					BigDecimal cien = BigDecimal.valueOf(100L);
					facturaDetalle.setListaprecios(listaPrecios);
					facturaDetalle.setCosto(concepto.getCosto());
					facturaDetalle.setPrecioBase(concepto.getPrecioBase());
					Impuesto impuesto = concepto.getImpuesto();
					BigDecimal valor = listaPrecios.getValor();
					if (valor != null) {
						
						//BigDecimal netoUnitario = variacionPrecio.multiply(concepto.getPrecioBase());
						//facturaDetalle.setNetoUnitario(netoUnitario);
						
						//////////////////////////////////////////////////////////////////////////////////
						////////////////METODO: EL PRECIO TIENE IMPUESTOS
//						BigDecimal variacionPrecio = uno.add(valor.divide(cien));
//						BigDecimal finalUnitario  = variacionPrecio.multiply(concepto.getPrecioBase());
//						facturaDetalle.setFinalUnitario(finalUnitario);
//						facturaDetalle.setFinalTotal(finalUnitario.multiply(new BigDecimal(cantidad)));
//						
//						facturaDetalle.setIVA(impuesto != null ? impuesto.getAlicuota() : null);
//						
//						if (impuesto != null && impuesto.getAlicuota() != null) {
//							
//							facturaDetalle.setNetoUnitario(finalUnitario.divide(((impuesto.getAlicuota().divide(new BigDecimal(100))).add(new BigDecimal(1))), 4, RoundingMode.HALF_UP));
//							facturaDetalle.setNetoTotal(facturaDetalle.getNetoUnitario().multiply(new BigDecimal(cantidad)));
//							facturaDetalle.setImpuestoUnitario(finalUnitario.subtract(facturaDetalle.getNetoUnitario()));
//							facturaDetalle.setImpuestoTotal((finalUnitario.subtract(facturaDetalle.getNetoUnitario()).multiply(BigDecimal.valueOf(cantidad))));
//							
//						}
						
						//////////////////////////////////////////////////////////////////////////////////
						////////////////METODO: EL PRECIO NO TIENE IMPUESTOS
						BigDecimal variacionPrecio = uno.add(valor.divide(cien));
						BigDecimal netoUnitario  = variacionPrecio.multiply(concepto.getPrecioBase()).multiply(new BigDecimal(signo));
						facturaDetalle.setNetoUnitario(netoUnitario);
						facturaDetalle.setNetoTotal(netoUnitario.multiply(new BigDecimal(cantidad)));
						
						facturaDetalle.setIVA(impuesto != null ? impuesto.getAlicuota() : null);
						
						if (impuesto != null && impuesto.getAlicuota() != null) {
							
							facturaDetalle.setFinalUnitario(netoUnitario.multiply(((impuesto.getAlicuota().divide(new BigDecimal(100))).add(new BigDecimal(1)))));
							facturaDetalle.setFinalTotal(facturaDetalle.getFinalUnitario().multiply(new BigDecimal(cantidad)));
							
							facturaDetalle.setImpuestoUnitario(facturaDetalle.getFinalUnitario().subtract(netoUnitario));
							facturaDetalle.setImpuestoTotal((facturaDetalle.getFinalUnitario().subtract(netoUnitario).multiply(BigDecimal.valueOf(cantidad))));
							
						}
						
						
						
					}
				}
			}

			if (validarFacturaDetalle(facturaDetalle, errors)) {
				//facturaDetalle.setIdEliminar(getProximoIdEliminar(facturaDetallesSession));
				generateAvisoExito("formularioFacturaDetalle.exito.registrar",atributos);
				if (accionFacturaDetalle.equalsIgnoreCase("NUEVO")){
					Integer ordenDetalle;
					
					if(facturaDetallesSession.size()>0){
						ordenDetalle = facturaDetallesSession.get(facturaDetallesSession.size()-1).getOrden();
						for(int i=0;i<facturaDetallesSession.size();i++){
							if(ordenDetalle.longValue() == facturaDetallesSession.get(i).getOrden().longValue()){
								ordenDetalle++;
								i--;
							}
						}
					}
					else{
						ordenDetalle=1;
					}
					facturaDetalle.setOrden(ordenDetalle);
					facturaDetallesSession.add(facturaDetalle);
					facturaDetalleFormulario = new FacturaDetalle();
				}else{
					session.setAttribute("facturaDetallesSession", facturaDetallesSession);
					atributos.put("codigoClienteEmp", codigoClienteEmp);
					atributos.put("accionFactura", accionFactura);
					atributos.put("codigoEmpresa", codigoEmpresa);
					atributos.put("facturaDetalleFormulario", facturaDetalleFormulario);
					return "redirect:precargaFormularioFactura.html";
				}
				
			} else {
				generateErrors(errors, atributos);
			}
		

		session.setAttribute("facturaDetallesSession", facturaDetallesSession);
		atributos.put("codigoClienteEmp", codigoClienteEmp);
		atributos.put("accionFactura", accionFactura);
		atributos.put("codigoEmpresa", codigoEmpresa);
		atributos.put("facturaDetalleFormulario", facturaDetalleFormulario);
		
		
		if(salir)
			return "redirect:precargaFormularioFactura.html"/*?accionFactura="+accionFactura*/;
		// hacemos el redirect
		return "formularioFacturaDetalle";
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

	private boolean validarFacturaDetalle(FacturaDetalle facturaDetalle,
			List<String> errors) {
		boolean result = true;
		if (facturaDetalle.getDescripcion().trim().equals("")
				|| facturaDetalle.getDescripcion().length()<1 
				|| facturaDetalle.getDescripcion().equalsIgnoreCase("Descripción:")) {
			errors.add("formularioFacturaDetalle.error.descripcion");
			result = false;
		}
		if (facturaDetalle.getCantidad() == null
				|| facturaDetalle.getCantidad().intValue() < 1) {
			errors.add("formularioFacturaDetalle.error.cantidad");
			result = false;
		}
		if (facturaDetalle.getConceptoFacturable() == null) {
			errors.add("formularioFacturaDetalle.error.codigoConcepto");
			result = false;
		}
		if (facturaDetalle.getListaprecios() == null) {
			errors.add("formularioFacturaDetalle.error.listaPrecios");
			result = false;
		} else if (facturaDetalle.getListaprecios().getValor() == null) {
			errors.add("formularioFacturaDetalle.error.listaPrecios.value");
			result = false;
		}

		if (facturaDetalle.getFinalTotal() == null) {
			errors.add("formularioFacturaDetalle.error.datosInsuficientes");
			result = false;
		}
		// TODO especificar los datos que impiden calcular el precio finalTotal
		if (facturaDetalle.getCantidad() == null
				|| facturaDetalle.getCantidad() <= 0) {
			errors.add("formularioFacturaDetalle.error.cantidad");
			result = false;
		}
		return result;
	}
	
	private Long getProximoIdEliminar(List<FacturaDetalle> detalles){
		Long mayor = Long.valueOf(0L);
		if(detalles != null && detalles.size()>0){
			for(FacturaDetalle fd : detalles){
				if(mayor<fd.getIdEliminar()){
					mayor = fd.getIdEliminar();
				}
			}
		}
		return mayor + 1L;
	}
}
