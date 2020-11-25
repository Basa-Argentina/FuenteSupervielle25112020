package com.dividato.configuracionGeneral.controladores;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.dividato.configuracionGeneral.validadores.ConceptoOperacionClienteValidator;
import com.dividato.configuracionGeneral.validadores.TransporteValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.accesoDatos.jerarquias.interfaz.ConceptoOperacionClienteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Impuesto;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.configuraciongeneral.Transporte;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Transporte.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Victor Kenis
 * 
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioConceptoOperacionCliente.html",
				"/guardarActualizarConceptoOperacionCliente.html"
			}
		)
public class FormConceptoOperacionClienteController {
	private ConceptoOperacionClienteService conceptoOperacionClienteService;
	private ConceptoOperacionClienteValidator validator;
	private ListaConceptoOperacionClienteController listaConceptoOperacionClienteController;
		
	/**
	 * Setea el servicio de Transporte.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase TransporteImp implementa Transporte y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param transporteService
	 */
	@Autowired
	public void setConceptoOperacionClienteService(ConceptoOperacionClienteService conceptoOperacionClienteService) {
		this.conceptoOperacionClienteService = conceptoOperacionClienteService;
	}
	
	@Autowired
	public void setListaConceptoOperacionClienteController(ListaConceptoOperacionClienteController listaConceptoOperacionClienteController) {
		this.listaConceptoOperacionClienteController = listaConceptoOperacionClienteController;
	}

	@Autowired
	public void setValidator(ConceptoOperacionClienteValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
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
	 * @param idTransporte parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioTransporte" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioConceptoOperacionCliente.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioConceptoOperacionCliente(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,		
			@RequestParam(value="codigoClienteEmp", required=false) String codigoClienteEmp,
			Map<String,Object> atributos) {
		
		
		
		if(accion==null)
			{
				accion="NUEVO"; //acción por defecto: nuevo
				ConceptoOperacionCliente conceptoOperacionClienteFormulario = new ConceptoOperacionCliente();
				conceptoOperacionClienteFormulario.setFechaAlta(new Date());
				atributos.put("conceptoOperacionClienteFormulario", conceptoOperacionClienteFormulario);
			}
		
		if(!accion.equals("NUEVO")){
			ConceptoOperacionCliente conceptoOperacionClienteFormulario; 
			conceptoOperacionClienteFormulario = conceptoOperacionClienteService.obtenerPorId(Long.valueOf(id));
			conceptoOperacionClienteFormulario.setCodigoEmpresa(conceptoOperacionClienteFormulario.getClienteEmp().getEmpresa().getCodigo());
			atributos.put("conceptoOperacionClienteFormulario", conceptoOperacionClienteFormulario);
		}
	
		//Seteo la accion actual
		atributos.put("accion", accion);		
		atributos.put("clienteAspId", obtenerClienteAspEmpleado().getId());
		atributos.put("codigoEmpresa", obtenerEmpresaUser().getCodigo());
	
		//Se realiza el redirect
		return "formularioConceptoOperacionCliente";
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
	 * @return "formularioTransporte" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarConceptoOperacionCliente.html",
			method= RequestMethod.POST
		)
	public String guardarConceptoOperacionCliente(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("conceptoOperacionClienteFormulario") final ConceptoOperacionCliente conceptoOperacionClienteFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		
		
		ConceptoOperacionCliente conceptoOperacionCliente;
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO")){
			accion="NUEVO";
			conceptoOperacionCliente = new ConceptoOperacionCliente();
		}
		else{
			accion="MODIFICACION";
			conceptoOperacionCliente = conceptoOperacionClienteService.obtenerPorId(conceptoOperacionClienteFormulario.getId());
			
		}
		if(!result.hasErrors()){
			
			conceptoOperacionClienteFormulario.setAccion(accion);
			conceptoOperacionClienteFormulario.setClienteAsp(obtenerClienteAspEmpleado());
			Empresa empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
			conceptoOperacionClienteFormulario.setCodigoEmpresa(empresaDefecto.getCodigo());
			conceptoOperacionClienteFormulario.setUsuario(obtenerUser());
			validator.validate(conceptoOperacionClienteFormulario,result);
		}
		
		
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		
		if(!result.hasErrors()){	
			
			ConceptoOperacionCliente conceptoOpeCliente = calcularConceptoOperacionCliente(conceptoOperacionCliente,conceptoOperacionClienteFormulario);
			
			if(accion.equals("NUEVO")){
							
				//Se guarda el cliente en la BD
				commit = conceptoOperacionClienteService.guardarConceptoOperacionCliente(conceptoOpeCliente);
			}else{
				
				//Se Actualiza el cliente en la BD
				commit = conceptoOperacionClienteService.actualizarConceptoOperacionCliente(conceptoOpeCliente);
			}			
			if(commit == null || !commit)
				conceptoOperacionClienteFormulario.setId(conceptoOperacionCliente.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("conceptoOperacionClienteFormulario", conceptoOperacionClienteFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioConceptoOperacionCliente(accion, conceptoOperacionClienteFormulario.getId() != null ?conceptoOperacionClienteFormulario.getId().toString() :  null, null, atributos);
		}
		
		if(result.hasErrors()){
			atributos.put("conceptoOperacionClienteFormulario", conceptoOperacionClienteFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioConceptoOperacionCliente(accion, conceptoOperacionClienteFormulario.getId() != null ?conceptoOperacionClienteFormulario.getId().toString() :  null, null, atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeTransporteReg = new ScreenMessageImp("formularioConceptoOperacionCliente.notif.conceptoGuardadoExito", null);
			avisos.add(mensajeTransporteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaConceptoOperacionClienteController.mostrarConceptoOperacionCliente(session, atributos, null, null, null, null, null, null, null, null, null);
	}
		
	private ConceptoOperacionCliente calcularConceptoOperacionCliente(ConceptoOperacionCliente conceptoOperacionClienteAnterior, ConceptoOperacionCliente data){
		
				Impuesto impuesto = data.getConceptoFacturable().getImpuesto();
				ListaPrecios listaPrecios = data.getListaPrecios();
			
				Long cantidad = new Long(1); //Ponemos en el caso de que el tipo de calculo sea unico
				if(data.getCantidad() != null)
					cantidad = data.getCantidad();
				if(cantidad==null)
					cantidad = new Long(1);
				
				ConceptoOperacionCliente conceptoOperacionCliente = conceptoOperacionClienteAnterior;
				conceptoOperacionCliente.setCantidad(cantidad);
				conceptoOperacionCliente.setClienteAsp(data.getClienteAsp());
				conceptoOperacionCliente.setClienteEmp(data.getClienteEmp());
				conceptoOperacionCliente.setConceptoFacturable(data.getConceptoFacturable());
				ConceptoFacturable concepto = data.getConceptoFacturable();
				conceptoOperacionCliente.setDescripcion(data.getConceptoFacturable().getDescripcion());
				conceptoOperacionCliente.setCosto(data.getConceptoFacturable().getCosto());
				conceptoOperacionCliente.setEstado("Pendiente");
				conceptoOperacionCliente.setListaPrecios(data.getListaPrecios());
				conceptoOperacionCliente.setPrecioBase(data.getPrecio());
				conceptoOperacionCliente.setTipoConcepto("Manual");
				conceptoOperacionCliente.setUsuario(data.getUsuario());
				conceptoOperacionCliente.setEmpresa(obtenerEmpresaUser());
				conceptoOperacionCliente.setSucursal(obtenerSucursalUser());
				conceptoOperacionCliente.setAsignado(false);
				conceptoOperacionCliente.setFechaAlta(data.getFechaAlta());
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				conceptoOperacionCliente.setHoraAlta(sdf.format(data.getFechaAlta()));
				
//				//-- CALCULOS -- METODO: EL PRECIO TIENE IMPUESTOS 
//				BigDecimal finalUnitario = new BigDecimal(0.0);
//				//finalUnitario = listaPrecios.getTipoVariacion().calcularMonto(conceptoOperacionCliente.getPrecioBase(), listaPrecios.getValor());
//				finalUnitario = data.getPrecio();	
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
				
				BigDecimal netoUnitario  = data.getPrecio();
				conceptoOperacionCliente.setNetoUnitario(netoUnitario);
				conceptoOperacionCliente.setNetoTotal(netoUnitario.multiply(new BigDecimal(cantidad)));
				
				if (impuesto != null && impuesto.getAlicuota() != null) {
					
					conceptoOperacionCliente.setFinalUnitario(netoUnitario.multiply(((impuesto.getAlicuota().divide(new BigDecimal(100))).add(new BigDecimal(1)))));
					conceptoOperacionCliente.setFinalTotal(conceptoOperacionCliente.getFinalUnitario().multiply(new BigDecimal(cantidad)));
					conceptoOperacionCliente.setImpuestos((conceptoOperacionCliente.getFinalUnitario().subtract(netoUnitario).multiply(BigDecimal.valueOf(cantidad))));
					
				}
				
				
				return conceptoOperacionCliente;
	}

	private ClienteAsp obtenerClienteAspEmpleado(){
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

