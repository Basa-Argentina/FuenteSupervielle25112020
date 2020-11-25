package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.servlet.ModelAndView;

import com.dividato.configuracionGeneral.validadores.ClienteConceptoValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteConceptoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteConcepto;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de User.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 * @modificado Gonzalo Noriega (30/06/2011)
 * @modificado Victor Kenis	(06/09/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioClienteConcepto.html",
				"/guardarActualizarClienteConcepto.html",
				"/recargaListaPrecio.html"
			}
		)
public class FormClienteConceptoController {
	
	private ListaClienteConceptosController listaClienteConceptosController;
	private ClienteConceptoService clienteConceptoService;
	private ClienteConceptoValidator validator;
	private ClienteEmpService clienteEmpService;
	private ListaPreciosService listaPreciosService;
	private ConceptoFacturableService conceptoFacturableService;
	
	/**
	 * Setea el servicio de ClienteConcepto.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto ClienteConceptoService.
	 * La clase ClienteConceptoImp implementa ClienteConcepto y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param clienteConceptoService
	 */
	@Autowired
	public void setClienteConceptoService(ClienteConceptoService clienteConceptoService) {
		this.clienteConceptoService = clienteConceptoService;
	}
	@Autowired
	public void setValidator(ClienteConceptoValidator validator) {
		this.validator = validator;
	}
	
	@Autowired
	public void setListaClienteConceptoesController(ListaClienteConceptosController listaClienteConceptoesController) {
		this.listaClienteConceptosController = listaClienteConceptoesController;
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
	public void setConceptoFacturableService(ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
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
	 * para mostrar el formulario de ClienteConcepto, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioClienteConcepto" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioClienteConcepto.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioClienteConcepto(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="val", required=false) String valCliente,
			@RequestParam(value="val", required=false) String valListaPrecio,
			@RequestParam(value="val", required=false) String valConcepto,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo,
			@RequestParam(value="listaPrecioCodigo", required=false) String listaPrecioCodigo,
			Map<String,Object> atributos) {
		//if(accion==null) accion=(String) atributos.get("accion");
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			ClienteConcepto clienteConceptoFormulario;
			clienteConceptoFormulario=clienteConceptoService.obtenerPorId(Long.valueOf(id));
			
			atributos.put("clienteConceptoFormulario", clienteConceptoFormulario);
		}

		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		//Preparo el popup de clientes	
		definirPopupClientes(atributos, valCliente);
		//Preparo el popup de Listas de Precios
		definirPopupListaPrecios(atributos, valListaPrecio, clienteCodigo);
		//Preparo el popup de Conceptos
		definirPopupConceptos(atributos, valConcepto, listaPrecioCodigo);
		
		atributos.put("accion", accion);
		
		return "formularioClienteConcepto";
	}
	
	
	
	@RequestMapping(value = "recargaListaPrecio.html",
			method = RequestMethod.PUT
		)
	public ModelAndView recargaListaPrecio(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="val", required=false) String valListaPrecio,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo,
			Map<String,Object> atributos) {
		//Preparo el popup de Listas de Precios
		definirPopupListaPrecios(atributos, valListaPrecio, clienteCodigo);
		return new ModelAndView();
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos ClienteConcepto.
	 * 
	 * @param ClienteConcepto user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto ClienteConcepto con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioClienteConcepto" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarClienteConcepto.html",
			method= RequestMethod.POST
		)
	public String guardarClienteConcepto(
			@RequestParam(value="accion",required=false) String accion,
			@ModelAttribute("clienteConceptoFormulario") final ClienteConcepto clienteConcepto,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			clienteConcepto.setAccion(accion);			
			validator.validate(clienteConcepto,result);
		}		
	
		ClienteConcepto clienteConceptoFormulario;
		if(!result.hasErrors()){
		
			if(accion.equals("NUEVO")){
				clienteConceptoFormulario = new ClienteConcepto();				
			}
			else
				clienteConceptoFormulario = clienteConceptoService.obtenerPorId(clienteConcepto.getId());
			
			//Setear Cliente
			ClienteEmp cliente = new ClienteEmp();
			cliente.setCodigo(clienteConcepto.getClienteCodigo());
			cliente = clienteEmpService.getByCodigo(cliente, obtenerClienteAspUser());
			if(cliente != null){
				clienteConceptoFormulario.setCliente(cliente);
			}
			
			//Setear ListaPrecios
			ListaPrecios listaPrecio = listaPreciosService.obtenerListaPreciosPorCodigo(clienteConcepto.getListaPrecioCodigo(), obtenerClienteAspUser());
			if(listaPrecio != null){
				clienteConceptoFormulario.setListaPrecios(listaPrecio);
			}
			
			//Setear Concepto
			ConceptoFacturable concepto = conceptoFacturableService.obtenerConceptoFacturablePorCodigo(clienteConcepto.getConceptoCodigo(), obtenerClienteAspUser());
			if(concepto != null){
				clienteConceptoFormulario.setConcepto(concepto);
			}
			
			//empezamos a setear los datos nuevos,
			clienteConceptoFormulario.setId(clienteConcepto.getId());
			clienteConceptoFormulario.setHabilitado(clienteConcepto.getHabilitado() == null ? false : clienteConcepto.getHabilitado());
			clienteConceptoFormulario.setClienteAsp(obtenerClienteAspUser());
	
			if(accion.equals("NUEVO")){				
				commit = clienteConceptoService.guardarClienteConcepto(clienteConceptoFormulario);
			}
			else{
				commit = clienteConceptoService.actualizarClienteConcepto(clienteConceptoFormulario);
			
			}
			if(commit == null || !commit)
				clienteConcepto.setId(clienteConceptoFormulario.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("clienteConceptoFormulario", clienteConcepto);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioClienteConcepto(accion, clienteConcepto.getId()!= null ? clienteConcepto.getId().toString() : null, null, null, null, null, null, atributos);
		}	
		
		if(result.hasErrors()){
			atributos.put("clienteConceptoFormulario", clienteConcepto);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");
			return precargaFormularioClienteConcepto(accion, clienteConcepto.getId()!= null ? clienteConcepto.getId().toString() : null, null, null, null,null, null, atributos);
			
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeClienteConceptoReg = new ScreenMessageImp("formularioClienteConcepto.notificacion.clienteConceptoRegistrado", null);
			avisos.add(mensajeClienteConceptoReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);	
		}
		//hacemos el redirect
		return listaClienteConceptosController.mostrarClienteConcepto(null, null, null, session, atributos);
	}

/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void definirPopupClientes(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clientesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("razonSocial.razonSocial","formularioClienteConcepto.cliente.razonSocial",false));
		campos.add(new CampoDisplayTag("nombre","formularioClienteConcepto.cliente.nombre",false));
		campos.add(new CampoDisplayTag("apellido","formularioClienteConcepto.cliente.apellido",false));
		campos.add(new CampoDisplayTag("nombreYApellido","formularioClienteConcepto.cliente.apellido",true));
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.cliente.apellido",true));
		clientesPopupMap.put("campos", campos);
		//Obtengo la empresa seteada en la sesión como por defecto
		Empresa empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		clientesPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup(val, empresaDefecto.getCodigo(), obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup2", "nombreYApellido");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clientesPopupMap.put("referenciaHtml", "clienteCodigo"); 		
		//url que se debe consumir con ajax
		clientesPopupMap.put("urlRequest", "precargaFormularioClienteConcepto.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clientesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		clientesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("clientesPopupMap", clientesPopupMap);
	}
	
	private void definirPopupListaPrecios(Map<String,Object> atributos, String val, String clienteCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> listaPreciosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.listaPrecio.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioClienteConcepto.listaPrecio.descripcion",false));
		campos.add(new CampoDisplayTag("valor","formularioClienteConcepto.listaPrecio.valor",false));
		listaPreciosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		listaPreciosPopupMap.put("coleccionPopup", listaPreciosService.listarListasPreciosByClientePopup(val, clienteCodigo, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		listaPreciosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		listaPreciosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		listaPreciosPopupMap.put("referenciaHtml", "listaPrecioCodigo"); 		
		//url que se debe consumir con ajax
		listaPreciosPopupMap.put("urlRequest", "precargaFormularioClienteConcepto.html");
		//se vuelve a setear el texto utilizado para el filtrado
		listaPreciosPopupMap.put("textoBusqueda", val);
		
		//se setea el codigo del cliente seleccionado.
		listaPreciosPopupMap.put("filterPopUp", clienteCodigo);
		//codigo de la localización para el titulo del popup
		listaPreciosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("listaPreciosPopupMap", listaPreciosPopupMap);
	}
	
	private void definirPopupConceptos(Map<String,Object> atributos, String val, String valListaPrecio){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> conceptosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.concepto.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioClienteConcepto.concepto.descripcion",false));
		campos.add(new CampoDisplayTag("costo","formularioClienteConcepto.concepto.costo",false));
		campos.add(new CampoDisplayTag("precioBase","formularioClienteConcepto.concepto.precioBase",false));
		conceptosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		conceptosPopupMap.put("coleccionPopup", conceptoFacturableService.listarConceptosFacturablesByListaPrecioPopup(val,valListaPrecio, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		conceptosPopupMap.put("referenciaHtml", "conceptoCodigo"); 		
		//url que se debe consumir con ajax
		conceptosPopupMap.put("urlRequest", "precargaFormularioClienteConcepto.html");
		//se vuelve a setear el texto utilizado para el filtrado
		conceptosPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		conceptosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("conceptosPopupMap", conceptosPopupMap);
	}
	
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
}
