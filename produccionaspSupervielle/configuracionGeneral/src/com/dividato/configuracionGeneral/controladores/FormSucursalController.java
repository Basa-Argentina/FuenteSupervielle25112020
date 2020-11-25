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

import com.dividato.configuracionGeneral.validadores.SucursalValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Direccion;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.Provincia;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Sucursal.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 * @modificado Gonzalo Noriega (05/05/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioSucursal.html",
				"/guardarActualizarSucursal.html"
			}
		)
public class FormSucursalController {
	private SucursalService sucursalService;
	private SucursalValidator validator;
	private FormDireccionController formDireccionController;
	private PaisService paisService;
	private ProvinciaService provinciaService;
	private LocalidadService localidadService;
	private BarrioService barrioService;
	private EmpresaService empresaService;
	private ListaSucursalController listaSucursalController;
	
		
	/**
	 * Setea el servicio de Sucursal.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase SucursalImp implementa Sucursal y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param sucursalService
	 */
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}

	@Autowired
	public void setListaSucursalsController(ListaSucursalController listaSucursalsController) {
		this.listaSucursalController = listaSucursalsController;
	}
	@Autowired
	public void setBarrioService(BarrioService barrioService) {
		this.barrioService = barrioService;
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
	public void setFormUserController(FormDireccionController formDireccionController) {
		this.formDireccionController = formDireccionController;
	}	
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setValidator(SucursalValidator validator) {
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
	 * @param idSucursal parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioSucursal" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioSucursal.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioSucursal(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) Long id,		
			@RequestParam(value="val",required=false) String val,			
			@RequestParam(value="clienteCodigo",required=false) Long ubicacionId, //uso clienteCodigo pq ya estaba implementado asi para otras pantallas
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			Sucursal sucursalFormulario;
			sucursalFormulario = sucursalService.obtenerPorId(Long.valueOf(id));
			
			atributos.put("sucursalFormulario", sucursalFormulario);			
		}
		// busco todas las empresas
		List<Empresa> empresas = empresaService.listarEmpresaFiltradas(null, obtenerClienteAspUser());
		atributos.put("empresas", empresas);
	
		//Seteo la accion actual
		atributos.put("accion", accion);		
		//obtengo los paises registrados en el sistema
		definirPopupPais(atributos, val, accion, id);
		//si se selecciono un pais, obtengo las provincias del mismo
		definirPopupProvincia(atributos, val, accion, id, ubicacionId);
		//si se selecciono un provincia, obtengo las localidades de la misma
		definirPopupLocalidad(atributos, val, accion, id, ubicacionId);
		//si se selecciono una localidad, obtengo los barrios
		definirPopupBarrio(atributos, val, accion, ubicacionId, ubicacionId);
	
		//Se realiza el redirect
		return "formularioSucursal";
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
	 * @return "formularioSucursal" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarSucursal.html",
			method= RequestMethod.POST
		)
	public String guardarSucursal(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("sucursalFormulario") final Sucursal sucursalFormulario,
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
			sucursalFormulario.setAccion(accion);
			validator.validate(sucursalFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Sucursal sucursal;
		if(!result.hasErrors()){			
			//obtengo el tipo de documento seleccionado
			Empresa empresaSel = empresaService.obtenerPorId(sucursalFormulario.getIdEmpresa());
			sucursalFormulario.setEmpresa(empresaSel);
			//obtengo el barrio seleccionado
			Barrio barrioSel = barrioService.obtenerPorId(sucursalFormulario.getIdBarrio());
						
			if(accion.equals("NUEVO")){
				sucursal = sucursalFormulario;
				
				//crear Direccion	
				Direccion direccion = new Direccion();
				direccion = formDireccionController.crearDireccion(barrioSel, sucursal.getDireccion().getCalle(), 
						sucursal.getDireccion().getDpto(), sucursal.getDireccion().getEdificio(), 
						sucursal.getDireccion().getNumero(), sucursal.getDireccion().getObservaciones(),
						sucursal.getDireccion().getPiso(), 0f, 0f);
				
				sucursal.setDireccion(direccion);				
				//Se guarda el cliente en la BD
				commit = sucursalService.guardarSucursal(sucursal);
			}else{
				sucursalFormulario.getDireccion().setBarrio(barrioSel);
				sucursal = sucursalService.getByCodigo(sucursalFormulario.getCodigo(), empresaSel);
				setData(sucursal, sucursalFormulario);
				//Se Actualiza el cliente en la BD
				commit = sucursalService.actualizarSucursal(sucursal);
			}			
			if(commit == null || !commit)
				sucursalFormulario.setId(sucursal.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("sucursalFormulario", sucursalFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioSucursal(accion, sucursalFormulario.getId() != null ?sucursalFormulario.getId() :  null, null, null, atributos);
		}
		
		if(result.hasErrors()){
			atributos.put("sucursalFormulario", sucursalFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioSucursal(accion, sucursalFormulario.getId() != null ?sucursalFormulario.getId() :  null, null, null, atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeSucursalReg = new ScreenMessageImp("formularioSucursal.notificacion.sucursalRegistrada", null);
			avisos.add(mensajeSucursalReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaSucursalController.mostrarSucursal(session, atributos);
	}
	
	private void setData(Sucursal sucursal, Sucursal data){
		if(data != null){			
			sucursal.setCodigo(data.getCodigo());
			sucursal.setDescripcion(data.getDescripcion());
			sucursal.setPrincipal(data.getPrincipal());
			sucursal.setTelefono(data.getTelefono());
			sucursal.setMail(data.getMail());		
			sucursal.getDireccion().setCalle(data.getDireccion().getCalle());
			sucursal.getDireccion().setNumero(data.getDireccion().getNumero());
			sucursal.getDireccion().setEdificio(data.getDireccion().getEdificio());
			sucursal.getDireccion().setPiso(data.getDireccion().getPiso());
			sucursal.getDireccion().setDpto(data.getDireccion().getDpto());
			sucursal.getDireccion().setBarrio(data.getDireccion().getBarrio());
			sucursal.getDireccion().setObservaciones(data.getDireccion().getObservaciones());
			
//			Direccion direccion = sucursal.getDireccion();
//			Direccion direccionData = data.getDireccion();
//			direccion.setCalle(direccionData.getCalle());
//			direccion.setNumero(direccionData.getNumero());
//			direccion.setEdificio(direccionData.getEdificio());
//			direccion.setPiso(direccionData.getPiso());
//			direccion.setDpto(direccionData.getDpto());
//			direccion.setBarrio(direccionData.getBarrio());
//			direccion.setObservaciones(direccionData.getObservaciones());			
//			sucursal.setDireccion(direccion);
			sucursal.setEmpresa(data.getEmpresa());
		}
	
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private void definirPopupPais(Map<String,Object> atributos, String val, String accion, Long id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpresa.datosEmpresa.direccion.pais",false));		
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
				"precargaFormularioEmpresa.html?" +
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
		campos.add(new CampoDisplayTag("id","formularioEmpresa.datosEmpresa.direccion.provincia",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpresa.datosEmpresa.direccion.provincia",false));		
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
				"precargaFormularioEmpresa.html?" +
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
		campos.add(new CampoDisplayTag("id","formularioEmpresa.datosEmpresa.direccion.localidad",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpresa.datosEmpresa.direccion.localidad",false));		
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
				"precargaFormularioEmpresa.html?" +
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
		campos.add(new CampoDisplayTag("id","formularioEmpresa.datosEmpresa.direccion.barrio",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpresa.datosEmpresa.direccion.barrio",false));		
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
				"precargaFormularioEmpresa.html?" +
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
