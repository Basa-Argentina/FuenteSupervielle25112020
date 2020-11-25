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

import com.dividato.configuracionGeneral.validadores.ClienteDireccionValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.Pais;
import com.security.modelo.general.Provincia;
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
 * @modificado Gonzalo Noriega (03/06/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioClienteDireccion.html",
				"/guardarActualizarClienteDireccion.html"
			}
		)
public class FormClienteDireccionController {
	
	private ListaClienteDireccionesController listaClienteDireccionesController;
	private ClienteDireccionService clienteDireccionService;
	private ClienteDireccionValidator validator;
	private BarrioService barrioService;
	private PaisService paisService;
	private ProvinciaService provinciaService;
	private LocalidadService localidadService;
	private ClienteEmpService clienteEmpService;
	
	/**
	 * Setea el servicio de ClienteDireccion.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto ClienteDireccionService.
	 * La clase ClienteDireccionImp implementa ClienteDireccion y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param clienteDireccionService
	 */
	@Autowired
	public void setClienteDireccionService(ClienteDireccionService clienteDireccionService) {
		this.clienteDireccionService = clienteDireccionService;
	}
	@Autowired
	public void setValidator(ClienteDireccionValidator validator) {
		this.validator = validator;
	}
	
	@Autowired
	public void setListaClienteDireccionesController(ListaClienteDireccionesController listaClienteDireccionesController) {
		this.listaClienteDireccionesController = listaClienteDireccionesController;
	}
	
	
	@Autowired
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	
	@Autowired
	public void setProvinciaService(ProvinciaService provinciaService) {
		this.provinciaService = provinciaService;
	}
	@Autowired
	public void setLocalidadService(LocalidadService localidadService) {
		this.localidadService = localidadService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	/**
	 * Setea el servicio de Barrio.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto ClienteDireccionService.
	 * La clase BarrrioImp implementa Barrio y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param barrioService
	 */
	@Autowired
	public void setBarrioService(BarrioService barrioService) {
		this.barrioService = barrioService;
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
	 * para mostrar el formulario de ClienteDireccion, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioClienteDireccion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioClienteDireccion.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioClienteDireccion(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="val", required=false) String val,
			@RequestParam(value="clienteCodigo",required=false) Long ubicacionId, 
			Map<String,Object> atributos) {
		//if(accion==null) accion=(String) atributos.get("accion");
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo	
		if(accion.equals("NUEVO")){
			Pais paisDefecto = paisService.getPaisPorNombre(Constants.PAIS_DEFECTO);
			if(paisDefecto!=null)
				atributos.put("paisDefecto", paisDefecto);
		}
		if(!accion.equals("NUEVO")){
			ClienteDireccion clienteDireccionFormulario;
			clienteDireccionFormulario=clienteDireccionService.getClienteDireccionById(Long.valueOf(id));
			if(clienteDireccionFormulario!=null && clienteDireccionFormulario.getCliente()!=null)
			{clienteDireccionFormulario.setClienteCodigo(clienteDireccionFormulario.getCliente().getCodigo());}
			if(clienteDireccionFormulario!=null && clienteDireccionFormulario.getDireccion()!=null && clienteDireccionFormulario.getDireccion().getBarrio()!=null
					&& clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad()!=null && clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad().getProvincia()!=null
					&& clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad().getProvincia().getPais()!=null)
				atributos.put("paisDefecto", clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad().getProvincia().getPais());
			
			atributos.put("clienteDireccionFormulario", clienteDireccionFormulario);
		}
		
		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		//Preparo el popup de impuestos	
		definirPopupClientes(atributos, val);
		
		//obtengo los paises registrados en el sistema
		definirPopupPais(atributos, val, accion, id);
		//si se selecciono un pais, obtengo las provincias del mismo
		definirPopupProvincia(atributos, val, accion, id, ubicacionId);
		//si se selecciono un provincia, obtengo las localidades de la misma
		definirPopupLocalidad(atributos, val, accion, id, ubicacionId);
		//si se selecciono una localidad, obtengo los barrios
		definirPopupBarrio(atributos, val, accion, ubicacionId, ubicacionId);
		
		atributos.put("accion", accion);
		
		return "formularioClienteDireccion";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos ClienteDireccion.
	 * 
	 * @param ClienteDireccion user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto ClienteDireccion con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioClienteDireccion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarClienteDireccion.html",
			method= RequestMethod.POST
		)
	public String guardarClienteDireccion(
			@RequestParam(value="accion",required=false) String accion,
			@ModelAttribute("clienteDireccionFormulario") final ClienteDireccion clienteDireccion,
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
			clienteDireccion.setAccion(accion);			
			validator.validate(clienteDireccion,result);
		}		
	
		ClienteDireccion clienteDireccionFormulario;
		if(!result.hasErrors()){
		
			if(accion.equals("NUEVO")){
				clienteDireccionFormulario = new ClienteDireccion();				
			}
			else
				clienteDireccionFormulario = clienteDireccionService.obtenerPorId(clienteDireccion.getId());
			
			//Setear Cliente
			ClienteEmp cliente = new ClienteEmp();
			cliente.setCodigo(clienteDireccion.getClienteCodigo());
			cliente = clienteEmpService.getByCodigo(cliente, obtenerClienteAspUser());
			if(cliente != null){
				clienteDireccionFormulario.setCliente(cliente);
			}
			
			//empezamos a setear los datos nuevos,
			clienteDireccionFormulario.setId(clienteDireccion.getId());
			
			//Setear Direccion del Cliente
			clienteDireccionFormulario = setearClienteDireccion(clienteDireccionFormulario, clienteDireccion.getDireccion().getBarrio(), 
					clienteDireccion.getDireccion().getCalle(), clienteDireccion.getDireccion().getDpto(), 
					clienteDireccion.getDireccion().getEdificio(), clienteDireccion.getDireccion().getNumero(), 
					clienteDireccion.getDireccion().getObservaciones(), clienteDireccion.getDireccion().getPiso(), 
					clienteDireccion.getDireccion().getLatitud(), clienteDireccion.getDireccion().getLongitud());
			
			//Setear Barrio
			if(clienteDireccion.getIdBarrio() != null){
				Barrio barrio = barrioService.obtenerPorId(clienteDireccion.getIdBarrio());			
				clienteDireccionFormulario.getDireccion().setBarrio(barrio);
			}
			
			clienteDireccionFormulario.setCodigo(clienteDireccion.getCodigo());
			clienteDireccionFormulario.setDescripcion(clienteDireccion.getDescripcion());
			clienteDireccionFormulario.setObservacion(clienteDireccion.getObservacion());
			if(clienteDireccionFormulario.getDireccion()!=null && clienteDireccionFormulario.getDireccion().getBarrio()!=null)
				clienteDireccionFormulario.setBarrioAux(clienteDireccionFormulario.getDireccion().getBarrio());
			if(clienteDireccionFormulario.getDireccion()!=null &&  clienteDireccionFormulario.getDireccion().getBarrio()!=null 
					&& clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad()!=null)
				clienteDireccionFormulario.setLocalidadAux(clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad());
			if(clienteDireccionFormulario.getDireccion()!=null &&  clienteDireccionFormulario.getDireccion().getBarrio()!=null 
					&& clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad()!=null 
					&& clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad().getProvincia()!=null)
				clienteDireccionFormulario.setProvinciaAux(clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad().getProvincia());
			if(clienteDireccionFormulario.getDireccion()!=null &&  clienteDireccionFormulario.getDireccion().getBarrio()!=null 
					&& clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad()!=null 
					&& clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad().getProvincia()!=null
					&& clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad().getProvincia().getPais()!=null)
				clienteDireccionFormulario.setPaisAux(clienteDireccionFormulario.getDireccion().getBarrio().getLocalidad().getProvincia().getPais());
			
			if(accion.equals("NUEVO")){				
				commit = clienteDireccionService.guardarClienteDireccion(clienteDireccionFormulario);
			}
			else{
				commit = clienteDireccionService.actualizarClienteDireccion(clienteDireccionFormulario);
			
			}
			if(commit == null || !commit)
				clienteDireccion.setId(clienteDireccionFormulario.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("clienteDireccionFormulario", clienteDireccion);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioClienteDireccion(accion, clienteDireccion.getId()!= null ? clienteDireccion.getId().toString() : null, null, null,atributos);
		}	
		
		if(result.hasErrors()){
			atributos.put("clienteDireccionFormulario", clienteDireccion);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");
			return precargaFormularioClienteDireccion(accion, clienteDireccion.getId()!= null ? clienteDireccion.getId().toString() : null, null, null,atributos);
			
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeClienteDireccionReg = new ScreenMessageImp("formularioClienteDireccion.notificacion.clienteDireccionRegistrada", null);
			avisos.add(mensajeClienteDireccionReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);	
		}
		//hacemos el redirect
		return listaClienteDireccionesController.mostrarClienteDireccion(null,null, session, atributos);
	}
	
	/**
	 * 
	 * Metodo para setear el objeto ClienteDireccion.
	 * @param barrio
	 * @param calle
	 * @param dpto
	 * @param edificio
	 * @param numero
	 * @param observaciones
	 * @param piso
	 * @return clienteDireccion seteada
	 */
	public ClienteDireccion setearClienteDireccion(ClienteDireccion setClienteDireccion, Barrio barrio, String calle, String dpto, String edificio, String numero, String observaciones, String piso,
									Float latitud, Float longitud){
		setClienteDireccion.getDireccion().setBarrio(barrio);
		setClienteDireccion.getDireccion().setCalle(calle);
		setClienteDireccion.getDireccion().setDpto(dpto);
		setClienteDireccion.getDireccion().setEdificio(edificio);
		setClienteDireccion.getDireccion().setNumero(numero);
		setClienteDireccion.getDireccion().setObservaciones(observaciones);
		setClienteDireccion.getDireccion().setPiso(piso);
		setClienteDireccion.getDireccion().setLatitud(latitud);
		setClienteDireccion.getDireccion().setLongitud(longitud);
		return setClienteDireccion;
	}
	
/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void definirPopupClientes(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clientesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("empresa.razonSocial","formularioClienteDireccion.cliente.razonSocialEmpresa",false));
		
		campos.add(new CampoDisplayTag("razonSocialONombreYApellido","formularioClienteDireccion.cliente.nombreRazonSocial",false));
		
		campos.add(new CampoDisplayTag("razonSocialONombreYApellido","formularioClienteDireccion.cliente.apellido",true));
		campos.add(new CampoDisplayTag("codigo","formularioClienteDireccion.cliente.apellido",true));
		clientesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		clientesPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup(val, null, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup2", "razonSocialONombreYApellido");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clientesPopupMap.put("referenciaHtml", "clienteCodigo"); 		
		//url que se debe consumir con ajax
		clientesPopupMap.put("urlRequest", "precargaFormularioClienteDireccion.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clientesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		clientesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("clientesPopupMap", clientesPopupMap);
	}
	
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private void definirPopupPais(Map<String,Object> atributos, String val, String accion, String id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.pais",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio		
		map.put("coleccionPopup", paisService.listarPaisesPopup(val));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "pais"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioClienteDireccion.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("paisPopupMap", map);
	}

	private void definirPopupProvincia(Map<String,Object> atributos, String val, String accion, String id, Long paisId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioCliente.datosCliente.direccion.provincia",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.provincia",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio		
		map.put("coleccionPopup", (paisId != null)? provinciaService.listarProvinciasPopup(paisId, val): new ArrayList<Provincia>());
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "provincia"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioClienteDireccion.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//se setea el id del pais seleccionado.
		map.put("filterPopUp", paisId);
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("provinciaPopupMap", map);
	}
	
	private void definirPopupLocalidad(Map<String,Object> atributos, String val, String accion, String id, Long provinciaId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioCliente.datosCliente.direccion.localidad",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.localidad",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio		
		map.put("coleccionPopup", (provinciaId != null)? localidadService.listarLocalidadesPopup(provinciaId, val): new ArrayList<Provincia>());
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "localidad"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioClienteDireccion.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//se setea el id del pais seleccionado.
		map.put("filterPopUp", provinciaId);
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("localidadPopupMap", map);
	}
	
	private void definirPopupBarrio(Map<String,Object> atributos, String val, String accion, Long id, Long localidadId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioCliente.datosCliente.direccion.barrio",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.barrio",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio		
		map.put("coleccionPopup", (localidadId != null)? barrioService.listarBarriosPopup(localidadId, val) : new ArrayList<Provincia>());
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "barrio"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioClienteDireccion.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//se setea el id del pais seleccionado.
		map.put("filterPopUp", localidadId);
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("barrioPopupMap", map);
	}
}
