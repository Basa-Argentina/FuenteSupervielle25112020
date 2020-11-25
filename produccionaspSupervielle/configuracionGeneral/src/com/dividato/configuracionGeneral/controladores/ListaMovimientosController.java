package com.dividato.configuracionGeneral.controladores;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
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

import com.dividato.configuracionGeneral.validadores.MovimientosValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.MovimientoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.LoteFacturacion;
import com.security.modelo.configuraciongeneral.Movimiento;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.Constantes;
import com.security.utils.ParseNumberUtils;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Impresion de Etiquetas.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta notación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Luciano  de Asteinza
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/iniciarListaMovimientos.html",
				"/mostrarListaMovimientos.html",
				"/filtrarListaMovimiento.html"
			}
		)
public class ListaMovimientosController {
	
	public static String ERROR_CODIGO_DEPOSITO_ACTUAL_REQUERIDO = "formularioMovimiento.error.codigoDeposito";
	public static String ERROR_FECHADESDE_MAYOR_FECHAHASTA = "formularioMovimiento.error.fechaDesdeMayorFechaHasta0";
	private DepositoService depositoService;
	private ElementoService elementoService;
	private ClienteEmpService clienteEmpService;
	private MovimientosValidator validator;
	private TipoElementoService tipoElementoService;
	private MovimientoService movimientoService;
	
	/**
	 * Setea el servicio de Depositos.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto ModuloService.
	 * La clase DepositoServiceImp implementa Deposito y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param depositoService
	 */
	@Autowired
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	@Autowired
	public void setValidator(MovimientosValidator validator) {
		this.validator = validator;
	}
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	@Autowired
	public void setTipoElementoService(TipoElementoService tipoElementoService) {
		this.tipoElementoService = tipoElementoService;
	}
	@Autowired
	public void setMovimientoService(MovimientoService movimientoService) {
		this.movimientoService = movimientoService;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}

	@RequestMapping(
		value="/iniciarListaMovimientos.html",
		method = RequestMethod.GET
	)
	public String iniciarListaMovimientos(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("movimientoBusqueda");
		atributos.remove("movimientos");
		return "redirect:mostrarListaMovimientos.html";
	}	
	
		
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de mostrar la lista de Movimientos.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaMovimientos" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(
			value="/mostrarListaMovimientos.html",
			method = RequestMethod.GET
		)
	public String mostrarListaMovimientos(HttpSession session,
			Map<String,Object> atributos,			
			@RequestParam(value="val", required=false) String valDepositoActual,
			@RequestParam(value="val", required=false) String valTipoElementos,
			@RequestParam(value="val", required=false) String valClienteEmp,
			@RequestParam(value="val", required=false) String valElementos,
			@RequestParam(value="codigoDepositoActual", required=false) String codigoDepositoActual,
			@RequestParam(value="codigoTipoElemento", required=false) String codigoTipoElemento,
			@RequestParam(value="codigoClienteEmp", required=false) String codigoClienteEmp,
			HttpServletRequest request){
		
		//buscamos en la base de datos y lo subimos a request.
		List<Movimiento> movimientos = null;	
		
		Movimiento movimientoBusqueda = (Movimiento) session.getAttribute("movimientoBusqueda");		
		Integer size = 0;
		
		if(movimientoBusqueda != null){
			
			movimientoBusqueda.setTipoMovimiento(obtenerTipoMovimiento(movimientoBusqueda.getTipoMovimientoInt()));
			
			//cuenta la cantidad de resultados
			size = movimientoService.contarMovimientosFiltrados(movimientoBusqueda, obtenerClienteAsp());

			//paginacion y orden de resultados de displayTag
			movimientoBusqueda.setTamañoPagina(20);
			String nPaginaStr = null;
			if(request!=null){
			nPaginaStr = request.getParameter((new ParamEncoder("movimiento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("movimiento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			String fieldOrder = request.getParameter( new ParamEncoder("movimiento").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			movimientoBusqueda.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("movimiento").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			movimientoBusqueda.setSortOrder(sortOrder);
			}
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			movimientoBusqueda.setNumeroPagina(nPagina);
			
			//Se busca en la base de datos las posiciones con los filtros de paginacion agregados a la posicion
			movimientos =(List<Movimiento>) movimientoService.listarMovimientos(movimientoBusqueda, obtenerClienteAsp());	
			
			if(movimientos!=null && movimientos.size()>0 && movimientoBusqueda.getCodigoRemito()!=null)
				movimientoBusqueda.setRemito(movimientos.get(0).getRemito());
			
			session.setAttribute("movimientoBusqueda", movimientoBusqueda);
			
		}else if(movimientoBusqueda==null){
			
			movimientoBusqueda=new Movimiento();
			
			if(codigoDepositoActual!=null && codigoDepositoActual.length()>0){
				movimientoBusqueda.setCodigoDepositoActual(codigoDepositoActual);			
			}
						
			if(codigoTipoElemento!=null && codigoTipoElemento.length()>0){
				movimientoBusqueda.setCodigoTipoElemento(codigoTipoElemento);
			}
			if(codigoClienteEmp!=null && codigoClienteEmp.length()>0){
				movimientoBusqueda.setCodigoClienteEmp(codigoClienteEmp);
			}
		}
		
		atributos.put("movimientos", movimientos);
		atributos.put("size", size);
		
		atributos.put("clienteAspId", obtenerClienteAsp().getId());
		atributos.put("sucursalId", obtenerSucursalDefault().getId());
		atributos.put("codigoEmpresa", obtenerEmpresaDefault().getCodigo());
	
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);
		
		definirPopupDepositosActuales(atributos, valDepositoActual);
		definirPopupTiposElementos(atributos, valTipoElementos);
		definirPopupClienteEmp(atributos, valClienteEmp);
		definirPopupElementos(
				atributos,
				movimientoBusqueda.getCodigoDepositoActual(), 
				movimientoBusqueda.getCodigoTipoElemento(), 
				movimientoBusqueda.getCodigoClienteEmp(), 
				valElementos
			);

		return "consultaMovimiento";
	}
	
	@RequestMapping(
			value="/filtrarListaMovimiento.html",
			method = RequestMethod.POST
		)
	public String filtrarListaMovimiento(
			@ModelAttribute("movimientoBusqueda") Movimiento movimientoBusqueda, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request){
		
		this.validator.validate(movimientoBusqueda, result);
		if(!result.hasErrors()){
			session.setAttribute("movimientoBusqueda", movimientoBusqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		return mostrarListaMovimientos(session, atributos, null, null, null, null, null, null, null,request);
	}
	
	/////////////////////METODOS DE SOPORTE/////////////////////////////
	
	private String obtenerTipoMovimiento(Integer tipoMovimientoInt){
		String result = null;
		if(tipoMovimientoInt!=null){
			switch (tipoMovimientoInt){
				case 1:
					result = Constantes.MOVIMIENTO_TIPO_MOVIMIENTO_INGRESO;
					break;
				case 2:
					result = Constantes.MOVIMIENTO_TIPO_MOVIMIENTO_TRANSFERENCIA;
					break;
				case 3: 
					result = Constantes.MOVIMIENTO_TIPO_MOVIMIENTO_EGRESO;
					break;
				default:
					result = null;					
			}
		}
		return result;
	}
	
	private void definirPopupDepositosActuales(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> depositosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioMovimiento.datosDepositoActual.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioMovimiento.datosDepositoActual.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		depositosPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		depositosPopupMap.put("coleccionPopup", depositoService.listarDepositoPopup(val, obtenerSucursalDefault().getCodigo(), obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		depositosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		depositosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		depositosPopupMap.put("referenciaHtml", "codigoDepositoActual"); 		
		//url que se debe consumir con ajax
		depositosPopupMap.put("urlRequest", "mostrarListaMovimientos.html");
		//se vuelve a setear el texto utilizado para el filtrado
		depositosPopupMap.put("textoBusqueda", val);
		
		//depositosPopupMap.put("filterPopUp", obtenerSucursalDefault().getCodigo());
		//codigo de la localización para el titulo del popup
		depositosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("depositosActualesPopupMap", depositosPopupMap);
	}
	
	private void definirPopupTiposElementos(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> tiposElementosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioMovimiento.datosTiposElementos.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioMovimiento.datosTiposElementos.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		tiposElementosPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		tiposElementosPopupMap.put("coleccionPopup", tipoElementoService.listarTipoElementoPopup(val, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		tiposElementosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		tiposElementosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		tiposElementosPopupMap.put("referenciaHtml", "codigoTipoElemento"); 		
		//url que se debe consumir con ajax
		tiposElementosPopupMap.put("urlRequest", "mostrarListaMovimientos.html");
		//se vuelve a setear el texto utilizado para el filtrado
		tiposElementosPopupMap.put("textoBusqueda", val);
		
		//codigo de la localización para el titulo del popup
		tiposElementosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("tiposElementosPopupMap", tiposElementosPopupMap);
	}
	
	private void definirPopupElementos(Map<String,Object> atributos, String codigoDeposito,
			String codigoTipoElemento, String codigoClienteEmp, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> elementosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioMovimiento.datosElementos.codigo",false));
		campos.add(new CampoDisplayTag("tipoElemento.descripcion","formularioMovimiento.datosElementos.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		elementosPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		elementosPopupMap.put("coleccionPopup", elementoService.listarElementoPopup(val, codigoDeposito, codigoTipoElemento, codigoClienteEmp, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		elementosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		elementosPopupMap.put("referenciaPopup2", "tipoElemento.descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		elementosPopupMap.put("referenciaHtml", "codigoElemento"); 		
		//url que se debe consumir con ajax
		elementosPopupMap.put("urlRequest", "mostrarListaMovimientos.html");
		//se vuelve a setear el texto utilizado para el filtrado
		elementosPopupMap.put("textoBusqueda", val);
		
		//codigo de la localización para el titulo del popup
		elementosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("elementosPopupMap", elementosPopupMap);
	}
	
	private void definirPopupClienteEmp(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clienteEmpPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioMovimiento.datosClienteEmp.codigo",false));
		campos.add(new CampoDisplayTag("nombreRazonSocial","formularioMovimiento.datosClienteEmp.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		clienteEmpPopupMap.put("campos", campos);
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		clienteEmpPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup(val,obtenerEmpresaDefault().getCodigo(), obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clienteEmpPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clienteEmpPopupMap.put("referenciaPopup2", "nombreRazonSocial");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clienteEmpPopupMap.put("referenciaHtml", "codigoClienteEmp"); 		
		//url que se debe consumir con ajax
		clienteEmpPopupMap.put("urlRequest", "mostrarListaMovimientos.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clienteEmpPopupMap.put("textoBusqueda", val);
		
		//codigo de la localización para el titulo del popup
		clienteEmpPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("clienteEmpPopupMap", clienteEmpPopupMap);
	}
	
	/**
	 * genera el objeto BindingResult para mostrar los errores por pantalla en un popup y lo agrega al map atributos
	 * @param codigoErrores
	 * @param atributos
	 */
	private void generateErrors(List<String> codigoErrores,	Map<String, Object> atributos) {
		if (!codigoErrores.isEmpty()) {
			BindingResult result = new BeanPropertyBindingResult(new Object(),"");
			for (String codigo : codigoErrores) {
				result.addError(new FieldError(	"error.formBookingGroup.general", codigo, null, false, new String[] { codigo }, null, "?"));
			}
			atributos.put("result", result);
			atributos.put("errores", true);
		} else if(atributos.get("result") == null){
			atributos.put("errores", false);
		}
	}
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	private Empresa obtenerEmpresaDefault(){
		return ((PersonaFisica)obtenerClienteAsp().getContacto()).getEmpresaDefecto();
	}
	private Sucursal obtenerSucursalDefault(){
		return ((PersonaFisica)obtenerClienteAsp().getContacto()).getSucursalDefecto();
	}
		
}
