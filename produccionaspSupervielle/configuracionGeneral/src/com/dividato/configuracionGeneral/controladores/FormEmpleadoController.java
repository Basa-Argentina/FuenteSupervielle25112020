package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

import com.dividato.configuracionGeneral.validadores.EmpleadoValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.GroupService;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.accesoDatos.interfaz.TipoDocumentoService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Direccion;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.Pais;
import com.security.modelo.general.Persona;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.general.Provincia;
import com.security.modelo.general.TipoDocumento;
import com.security.modelo.seguridad.Group;
import com.security.modelo.seguridad.User;
import com.security.recursos.RecursosPassword;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Empleado.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioEmpleado.html",
				"/guardarActualizarEmpleado.html"
			}
		)
public class FormEmpleadoController {
	private ListaEmpleadoController listaEmpleadoController;
	private EmpleadoService empleadoService;
	private EmpleadoValidator validator;
	private GroupService groupService;
	private ClienteEmpService clienteEmpService;
	private TipoDocumentoService tipoDocumentoService;
	private FormDireccionController formDireccionController;
	private BarrioService barrioService;
	private ProvinciaService provinciaService;
	private LocalidadService localidadService;
	private PaisService paisService;
	private List<Pais> paises;
	private ClienteDireccionService clienteDireccionService;
	
	/**
	 * Setea el servicio de Empleado.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto EmpleadoService.
	 * La clase EmpleadoImp implementa Empleado y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param personaService
	 */
	
	@Autowired
	public void setClienteDireccionService(ClienteDireccionService clienteDireccionService) {
		this.clienteDireccionService = clienteDireccionService;
	}
	
	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
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
	public void setBarrioService(BarrioService barrioService) {
		this.barrioService = barrioService;
	}
	
	@Autowired
	public void setFormUserController(FormDireccionController formDireccionController) {
		this.formDireccionController = formDireccionController;
	}
	
	@Autowired
	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	
	@Autowired
	public void setListaEmpleadoController(ListaEmpleadoController listaEmpleadoController) {
		this.listaEmpleadoController = listaEmpleadoController;
	}
	@Autowired
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
	@Autowired
	public void setValidator(EmpleadoValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioEmpleado.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Empleado, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idEmpleado parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioEmpleado" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioEmpleado.html",
			method = RequestMethod.GET
		)
	public String precargaFormulario(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="val",required=false) String val,
			@RequestParam(value="clienteCodigo",required=false) Long ubicacionId,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo,
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo
		if(accion.equals("NUEVO")){
			Pais paisDefecto = paisService.getPaisPorNombre(Constants.PAIS_DEFECTO);
			if(paisDefecto!=null)
				atributos.put("paisDefecto", paisDefecto);
			TipoDocumento tipoDocumentoDefecto = tipoDocumentoService.getTipoDocumentoPorCodigo(Constants.TIPO_DOC_DNI);
			if(tipoDocumentoDefecto!=null)
				atributos.put("tipoDocumentoDefecto", tipoDocumentoDefecto);
			
			Empleado empleado = (Empleado)atributos.get("empleadoFormulario");
			if(empleado != null){
				empleado.getClienteEmp().setCodigo(empleado.getClienteCodigo());
				empleado.setUsernameSinCliente(empleado.getUsername());			
				atributos.put("empleadoFormulario", empleado);
			}
		}
		if(!accion.equals("NUEVO")){
			Empleado empleadoFormulario;
			empleadoFormulario=empleadoService.obtenerPorId(id);
			
//			Barrio barrioSel = empleadoFormulario.getPersona().getDireccion().getBarrio(); //busco el barrio
//			List<Provincia> provincias = provinciaService.listarProvinciasPorPaisId(barrioSel.getLocalidad().getProvincia().getPais().getId());
//			List<Localidad> localidades = localidadService.listarLocalidadesPorProcinciaId(barrioSel.getLocalidad().getProvincia().getId());
//			List<Barrio> barrios = barrioService.listarBarriosPorLocalidadId(barrioSel.getLocalidad().getId());
//			atributos.put("provincias", provincias);			
//			atributos.put("localidades", localidades);			
//			atributos.put("barrios", barrios);	
			
			atributos.put("empleadoFormulario", empleadoFormulario);
			if(empleadoFormulario.getPersona()!=null && empleadoFormulario.getPersona().getDireccion()!=null && empleadoFormulario.getPersona().getDireccion().getBarrio()!=null
					&& empleadoFormulario.getPersona().getDireccion().getBarrio().getLocalidad()!=null && empleadoFormulario.getPersona().getDireccion().getBarrio().getLocalidad().getProvincia()!=null
					&& empleadoFormulario.getPersona().getDireccion().getBarrio().getLocalidad().getProvincia().getPais()!=null)
				atributos.put("paisDefecto", empleadoFormulario.getPersona().getDireccion().getBarrio().getLocalidad().getProvincia().getPais());
			if(empleadoFormulario.getPersona()!=null && empleadoFormulario.getPersona().getTipoDoc()!=null )
				atributos.put("tipoDocumentoDefecto", empleadoFormulario.getPersona().getTipoDoc());
		}
		
		//Traemos las lista de clientes.
//		List<ClienteEmp> clientes = clienteEmpService.listarClienteFiltradas(null, obtenerClienteAspEmpleado());
//		atributos.put("clientes", clientes);
		
		//Traemos todos los tipos de documentos.
		List<TipoDocumento> tiposDocumento = tipoDocumentoService.listarTodos();
		atributos.put("tiposDocumento", tiposDocumento);
		
//		//obtengo los paises registrados en el sistema
//		paises = paisService.listarPaises();
//		Collections.sort(paises, new OrdenaPaisesPorNombrePrimeroArgentina());
//		atributos.put("paises", paises); // los paso por get
		
		atributos.put("accion", accion);
		
		//obtengo los paises registrados en el sistema
		definirPopupPais(atributos, val, accion, id);
		//si se selecciono un pais, obtengo las provincias del mismo
		definirPopupProvincia(atributos, val, accion, id, ubicacionId);
		//si se selecciono un provincia, obtengo las localidades de la misma
		definirPopupLocalidad(atributos, val, accion, id, ubicacionId);
		
		//si se selecciono una localidad, obtengo los barrios
		definirPopupBarrio(atributos, val, accion, ubicacionId, ubicacionId);
		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		//Preparo el popup de impuestos	
		definirPopupClientes(atributos, val);
		definirPopupDirecciones(atributos, val, clienteCodigo);
		
		return "formularioEmpleado";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos Empleado.
	 * 
	 * @param Empleado empleado a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto Empleado con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioEmpleado" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarEmpleado.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,
			@ModelAttribute("empleadoForm") final Empleado empleado,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			empleado.setCliente(obtenerClienteAspEmpleado()); //seteo el cliente
			empleado.setUsernameSinCliente(empleado.getUsername()); //seteo el Username concatenado al nombre del cliente
			empleado.setAccion(accion);
			validator.validate(empleado,result);
		}
			
		Empleado empleadoFormulario;
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;		
		boolean commit = false;		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		
		if(!result.hasErrors()){
			//SETEAR UN GRUPO DE EMPLEADOS POR DEFECTO SEGUN EL CLIENTE LOGUEADO.
			Group group = new Group();
			group.setGroupName(Constants.NOMBRE_GROUP_EMPLEADOS);
			empleado.setGroups(new TreeSet<Group>(groupService.listarTodosGroupFiltrados(group, obtenerClienteAspEmpleado())));	
			
			//obtengo el barrio seleccionado
			Barrio barrioSel = barrioService.obtenerPorId(empleado.getIdBarrio());
			
			
			TipoDocumento tipoDocSel  = tipoDocumentoService.obtenerPorId(empleado.getIdTipoDoc());
			
			if(accion.equals("NUEVO")){
				if("".equals(empleado.getPassword())) //si no asignaron password
					empleado.setPassword(RecursosPassword.generarPassword()); //genero un password aleatorio
				empleadoFormulario = crearEmpleado(
						obtenerClienteAspEmpleado(), 
						empleado.getPersona(),
						empleado.getUsernameSinCliente(),
						empleado.getPassword(),
						empleado.getGroups());
				Direccion direccion = new Direccion();
				direccion = formDireccionController.crearDireccion(barrioSel, empleado.getPersona().getDireccion().getCalle(), 
						empleado.getPersona().getDireccion().getDpto(), empleado.getPersona().getDireccion().getEdificio(), 
						empleado.getPersona().getDireccion().getNumero(), empleado.getPersona().getDireccion().getObservaciones(),
						empleado.getPersona().getDireccion().getPiso(), 0f, 0f);
				
				empleadoFormulario.getPersona().setDireccion(direccion);
				
				
			}else{
				empleadoFormulario = setearDatos(
						empleadoService.obtenerPorId(empleado.getId()), 
						empleado.getCliente(), 
						empleado.getPersona(), 
						empleado.getUsername(), 
						empleado.getPassword(), 
						empleado.getGroups(),
						empleado.getPersona().getDireccion());
			}
			
			//Setear Cliente
			ClienteEmp cliente = new ClienteEmp();
			cliente.setCodigo(empleado.getClienteCodigo());
			cliente = clienteEmpService.getByCodigo(cliente, obtenerClienteAspUser());
			if(cliente != null){
				empleadoFormulario.setClienteEmp(cliente);
			}
			//Setear la direccion por defecto
			ClienteDireccion direccionDefecto = new ClienteDireccion();
			direccionDefecto = clienteDireccionService.getByCodigo(empleado.getCodigoDireccion(), obtenerClienteAspUser());
			if(direccionDefecto != null){
				empleadoFormulario.setDireccionDefecto(direccionDefecto);
			}
			
			
			empleadoFormulario.getPersona().getDireccion().setBarrio(barrioSel);
			empleadoFormulario.getPersona().setTipoDoc(tipoDocSel);
			
			empleadoFormulario.setCelular(empleado.getCelular());
			empleadoFormulario.setCodigo(empleado.getCodigo());
			empleadoFormulario.setInterno(empleado.getInterno());
			empleadoFormulario.setFax(empleado.getFax());
			empleadoFormulario.setHabilitado(empleado.getHabilitado());
			empleadoFormulario.setObservaciones(empleado.getObservaciones());
						
			if(accion.equals("NUEVO"))
				commit = empleadoService.guardarEmpleado(empleadoFormulario);
			else
				commit = empleadoService.actualizarEmpleado(empleadoFormulario);
			//seteo los menjes
			ScreenMessage mensaje;
			if(commit){
				mensaje = new ScreenMessageImp("notif.empleado.guardado", null);
				hayAvisos = true;
			}else{
				mensaje = new ScreenMessageImp("error.commitDataBase", null);
				hayAvisosNeg = true;
			}
			avisos.add(mensaje);
		}
		//Si existen errores
		if(result.hasErrors()){
			atributos.put("empleadoFormulario", empleado);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", false);
			atributos.put("result", result);
			return precargaFormulario(accion, empleado.getId(), null, null, null,atributos);
		}else{
			atributos.put("errores", false);
			atributos.remove("result");			
		}
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		//hacemos el redirect
		return listaEmpleadoController.mostrar(session, atributos);
	}
	
	/**
	 * Metodo que crea un nuevo usuario del sistema.
	 * @param cliente
	 * @param propietario
	 * @param username
	 * @param password
	 * @param empleadoGroups
	 * @return
	 */
	public Empleado crearEmpleado(ClienteAsp cliente, Persona propietario, String username, String password, Set<Group> empleadoGroups){
		Empleado newEmpleado = new Empleado();
		newEmpleado.setEnable(true);
		newEmpleado.setCliente(cliente);
		newEmpleado.setPersona(propietario);
		newEmpleado.setUsernameSinCliente(username);
		newEmpleado.setPassword(encriptarString(password));
		newEmpleado.setGroups(empleadoGroups);
		newEmpleado.setAdmin(false);
		return newEmpleado;
	}
	
	public Empleado setearDatos(Empleado empleado, ClienteAsp cliente, Persona propietario, 
			String username, String password, Set<Group> empleadoGroups, Direccion direccion){
		empleado.setCliente(cliente);
		empleado.setPersona(setearDatos(empleado.getPersona(), propietario));
		if(!"".equals(password))
			empleado.setPassword(encriptarString(password));
		empleado.setGroups(empleadoGroups);
		empleado.getPersona().setDireccion(setearDatos(empleado.getPersona().getDireccion(), direccion));
		return empleado;
	}
	
	public Persona setearDatos(Persona persona, Persona datos){
		PersonaFisica pf = (PersonaFisica) persona;
		PersonaFisica pfDatos = (PersonaFisica) datos;
		pf.setNombre(pfDatos.getNombre());
		pf.setApellido(pfDatos.getApellido());
		pf.setTipoDoc(pfDatos.getTipoDoc());
		pf.setNumeroDoc(pfDatos.getNumeroDoc());
		pf.setTelefono(pfDatos.getTelefono());
		pf.setMail(pfDatos.getMail());
		return pf;
	}
	
	public Direccion setearDatos(Direccion direccion, Direccion datos){
		direccion.setCalle(datos.getCalle());
		direccion.setNumero(datos.getNumero());
		direccion.setEdificio(datos.getEdificio());
		direccion.setPiso(datos.getPiso());
		direccion.setDpto(datos.getDpto());
		direccion.setBarrio(datos.getBarrio());
		direccion.setObservaciones(datos.getObservaciones());
		return direccion;
	}
	
	/**
	 * Metodo que encripta una cadena de texto pasada por parametro.
	 * @param s
	 * @return
	 */
	private String encriptarString(String s){
		if(!"".equals(s)){
			org.springframework.security.providers.encoding.ShaPasswordEncoder passEnc=new org.springframework.security.providers.encoding.ShaPasswordEncoder();
			String passEncriptado = passEnc.encodePassword(s, null);
			return passEncriptado;
		}
		return null;
	}
	
	private ClienteAsp obtenerClienteAspEmpleado(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private void definirPopupPais(Map<String,Object> atributos, String val, String accion, Long id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpleado.datosEmpleado.direccion.pais",false));		
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
				"precargaFormularioEmpleado.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("paisPopupMap", map);
	}

	private void definirPopupProvincia(Map<String,Object> atributos, String val, String accion, Long id, Long paisId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioEmpleado.datosEmpleado.direccion.provincia",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpleado.datosEmpleado.direccion.provincia",false));		
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
				"precargaFormularioEmpleado.html?" +
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
	
	private void definirPopupLocalidad(Map<String,Object> atributos, String val, String accion, Long id, Long provinciaId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioEmpleado.datosEmpleado.direccion.localidad",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpleado.datosEmpleado.direccion.localidad",false));		
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
				"precargaFormularioEmpleado.html?" +
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
		campos.add(new CampoDisplayTag("id","formularioEmpleado.datosEmpleado.direccion.barrio",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpleado.datosEmpleado.direccion.barrio",false));		
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
				"precargaFormularioEmpleado.html?" +
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
	
	private void definirPopupClientes(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clientesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("empresa.razonSocial","formularioEmpleado.cliente.razonSocial",false));
		campos.add(new CampoDisplayTag("razonSocial.razonSocial","formularioEmpleado.cliente.razonSocial",false));
		campos.add(new CampoDisplayTag("nombre","formularioEmpleado.cliente.nombre",false));
		campos.add(new CampoDisplayTag("apellido","formularioEmpleado.cliente.apellido",false));
		campos.add(new CampoDisplayTag("nombreYApellido","formularioEmpleado.cliente.apellido",true));
		campos.add(new CampoDisplayTag("codigo","formularioEmpleado.cliente.apellido",true));
		clientesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		clientesPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup(val, null, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup2", "nombreYApellido");
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
	
	
	private void definirPopupDirecciones(Map<String,Object> atributos, String val, String clienteCodigo){
		ClienteEmp cliente = null;
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> direccionesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioEmpleado.datosEmpleado.codigo",true));
		campos.add(new CampoDisplayTag("direccion.calle","formularioEmpleado.datosEmpleado.direccion.calle",false));
		campos.add(new CampoDisplayTag("direccion.numero","formularioEmpleado.datosEmpleado.direccion.numero",false));
		campos.add(new CampoDisplayTag("direccion.barrio.nombre","formularioEmpleado.datosEmpleado.direccion.barrio",false));
		campos.add(new CampoDisplayTag("direccion.barrio.localidad.nombre","formularioEmpleado.datosEmpleado.direccion.localidad",false));
		campos.add(new CampoDisplayTag("direccion.barrio.localidad.provincia.nombre","formularioEmpleado.datosEmpleado.direccion.provincia",false));
		//campos.add(new CampoDisplayTag("descripcion","formularioSucursal.datosSucursal.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		direccionesPopupMap.put("campos", campos);
		if(clienteCodigo != "" && clienteCodigo != null)
		{
			cliente = clienteEmpService.getByCodigo(clienteCodigo);
		}
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		direccionesPopupMap.put("coleccionPopup", clienteDireccionService.listarDireccionesPopup(val, cliente, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		direccionesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		direccionesPopupMap.put("referenciaPopup2", "direccion.calle"+"direccion.numero");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		direccionesPopupMap.put("referenciaHtml", "codigoDireccion"); 		
		//url que se debe consumir con ajax
		direccionesPopupMap.put("urlRequest", "mostrarEmpleado.html");
		//se vuelve a setear el texto utilizado para el filtrado
		direccionesPopupMap.put("textoBusqueda", val);
		//se setea el codigo del cliente seleccionado.
		direccionesPopupMap.put("filterPopUp", clienteCodigo);
		//codigo de la localización para el titulo del popup
		direccionesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("direccionesPopupMap", direccionesPopupMap);
	}
	
	
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
