package com.dividato.login.controladores;

import java.util.ArrayList;
import java.util.Collections;
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

import com.dividato.login.validadores.UserPersonalDataValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.accesoDatos.interfaz.PersonaService;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.Localidad;
import com.security.modelo.general.Pais;
import com.security.modelo.general.Persona;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.general.Provincia;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
import com.security.utils.comparators.OrdenaPaisesPorNombrePrimeroArgentina;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de UserPersonalData.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioUserPersonalData.html",
				"/guardarActualizarUserPersonalData.html"
			}
		)
public class FormUserPersonalDataController {
	private PaisService paisService;
	private ProvinciaService provinciaService;
	private LocalidadService localidadService;
	private BarrioService barrioService;
	private PersonaService personaService;
	private UserPersonalDataValidator validator;
	private UserService userService;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	
	/**
	 * Setea el servicio de UserPersonalData.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserPersonalDataService.
	 * La clase UserPersonalDataImp implementa UserPersonalData y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param personaService
	 */
	
	
	@Autowired
	public void setBarrioService(BarrioService barrioService) {
		this.barrioService = barrioService;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
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
	public void setPersonaService(PersonaService personaService) {
		this.personaService = personaService;
	}
	@Autowired
	public void setValidator(UserPersonalDataValidator validator) {
		this.validator = validator;
	}
	@Autowired
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
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
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioUserPersonalData.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de UserPersonalData, ya sea nuevo, consulta o modificación.
	 * 
	 * @param atributos son los atributos del request
	 * @return "formularioUserPersonalData" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioUserPersonalData.html",
			method = RequestMethod.GET
		)
	public String precargaFormulario(Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo) {
		User userPersonalDataFormulario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userPersonalDataFormulario = userService.obtenerPorId(userPersonalDataFormulario.getId());
		List<Pais> paises = paisService.listarPaises();
		
		Collections.sort(paises, new OrdenaPaisesPorNombrePrimeroArgentina());
		atributos.put("paises", paises);
		atributos.put("userPersonalDataFormulario", userPersonalDataFormulario);
		Barrio barrioSel=null;
		if(userPersonalDataFormulario.getPersona().getDireccion()==null ){
			atributos.put("accion", "NUEVO");	
		}else if(userPersonalDataFormulario.getPersona().getDireccion().getBarrio() != null){
			atributos.put("accion", "NUEVO");
			barrioSel = userPersonalDataFormulario.getPersona().getDireccion().getBarrio(); //busco el barrio
		}
		//cargos todas las provincias, localidades y barrios para el cliente
		
		if(barrioSel != null){
			List<Provincia> provincias = provinciaService.listarProvinciasPorPaisId(barrioSel.getLocalidad().getProvincia().getPais().getId());
			List<Localidad> localidades = localidadService.listarLocalidadesPorProcinciaId(barrioSel.getLocalidad().getProvincia().getId());
			List<Barrio> barrios = barrioService.listarBarriosPorLocalidadId(barrioSel.getLocalidad().getId());
			atributos.put("provincias", provincias);			
			atributos.put("localidades", localidades);			
			atributos.put("barrios", barrios);
		}else{
			List<Provincia> provincias = null;
			List<Localidad> localidades = null;
			List<Barrio> barrios = null;
			if(paises != null && paises.size()==1){
				 provincias = provinciaService.listarProvinciasPorPaisId(paises.get(0).getId());
				atributos.put("provincias", provincias);	
			}
			if(provincias !=null && provincias.size()==1){
				localidades = localidadService.listarLocalidadesPorProcinciaId(provincias.get(0).getId());
				atributos.put("localidades", localidades);
			}
			
			if(localidades != null && localidades.size()==1){
				barrios = barrioService.listarBarriosPorLocalidadId(localidades.get(0).getId());
				atributos.put("barrios", barrios);
			}
		}
		
		//Preparo el popup de Empresas
		definirPopupEmpresa(atributos, valEmpresa);
		//Preparo el popup de Sucursales
		definirPopupSucursal(atributos, valSucursal, empresaCodigo);
		return "formularioUserPersonalData";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos UserPersonalData.
	 * 
	 * @param UserPersonalData insCost a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto UserPersonalData con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioUserPersonalData" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarUserPersonalData.html",
			method= RequestMethod.POST
		)
	public String guardarUserPersonalData(
			@ModelAttribute("userPersonalDataFormulario") User userPersonalData,
			BindingResult result,
			Map<String,Object> atributos,
			HttpSession session){
		Boolean commit = null;
		
		if(!result.hasErrors()){
			validator.validate(userPersonalData.getPersona(),result);
		}
		//Seteamos la Empresa
		Empresa empresa = new Empresa();		
		empresa = empresaService.getByCodigo(userPersonalData.getCodigoEmpresa(), obtenerClienteAspUser());
		if(empresa != null)
			((PersonaFisica)userPersonalData.getPersona()).setEmpresaDefecto(empresa);
		
		//Seteamos la Sucursal
		Sucursal sucursal = new Sucursal();		
		sucursal = sucursalService.getByCodigo(userPersonalData.getCodigoSucursal(), obtenerClienteAspUser());
		if(sucursal != null)
			((PersonaFisica)userPersonalData.getPersona()).setSucursalDefecto(sucursal);
		
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Persona persona;
		if(!result.hasErrors()){
			//obtengo la persona de la base de datos
			persona = personaService.obtenerPorId(userPersonalData.getPersona().getId());
			//obtengo el barrio seleccionado
			Barrio barrioSel = barrioService.obtenerPorId(userPersonalData.getPersona().getDireccion().getIdBarrio());
			userPersonalData.getPersona().getDireccion().setBarrio(barrioSel); // seteo el mismo en la persona
			//Seteo los nuevos datos
			setData(persona, userPersonalData.getPersona());
			//Se Actualiza el cliente en la BD
			commit = personaService.update(persona);
		}		
		//Ver errores
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		//Ver errores
		if(commit != null && !commit){			
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("userPersonalDataFormulario", userPersonalData);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormulario(atributos, null, null, null);
		}	
		
		if(result.hasErrors()){
			atributos.put("userPersonalDataFormulario", userPersonalData);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", false);
			atributos.remove("avisos");		
			return precargaFormulario(atributos, null, null, null);
		}else{
			//Genero las notificaciones 			
			ScreenMessage notificacion = new ScreenMessageImp("notif.persona.update", null);
			avisos.add(notificacion); //agrego el mensaje a la coleccion			
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);
		}
		atributos.put("setEmpresa", true);
		Empresa empDef = ((PersonaFisica)userPersonalData.getPersona()).getEmpresaDefecto();
		Sucursal sucDef = ((PersonaFisica)userPersonalData.getPersona()).getSucursalDefecto();
		if( empDef != null ){
			session.setAttribute("empresa", ((PersonaFisica)userPersonalData.getPersona()).getEmpresaDefecto().getDescripcion());		
		}else{
			atributos.remove("setEmpresa");
		}
		
		if(sucDef != null){
			session.setAttribute("sucursal", ((PersonaFisica)userPersonalData.getPersona()).getSucursalDefecto().getDescripcion());
		}
		//hacemos el redirect
		return "index";
	}
	
	private void setData(Persona persona, Persona data){
		persona.setMail(data.getMail());
		persona.setTelefono(data.getTelefono());
		persona.getDireccion().setBarrio(data.getDireccion().getBarrio());
		persona.getDireccion().setCalle(data.getDireccion().getCalle());
		persona.getDireccion().setNumero(data.getDireccion().getNumero());
		persona.getDireccion().setPiso(data.getDireccion().getPiso());
		persona.getDireccion().setDpto(data.getDireccion().getDpto());
		((PersonaFisica) persona).setNombre(data.getNombre());
		((PersonaFisica) persona).setApellido(data.getApellido());
		((PersonaFisica) persona).setEmpresaDefecto(((PersonaFisica)data).getEmpresaDefecto());
		((PersonaFisica) persona).setSucursalDefecto(((PersonaFisica)data).getSucursalDefecto());
	}
	
//////////////////////////METODOS  DE SOPORTE /////////////////
	
	private void definirPopupSucursal(Map<String,Object> atributos, String val, String empresaCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> sucursalesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioUserPersonalData.sucursal.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioUserPersonalData.sucursal.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		sucursalesPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		sucursalesPopupMap.put("coleccionPopup", sucursalService.listarSucursalPopup(val, empresaCodigo, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		sucursalesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		sucursalesPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		sucursalesPopupMap.put("referenciaHtml", "codigoSucursal"); 		
		//url que se debe consumir con ajax
		sucursalesPopupMap.put("urlRequest", "precargaFormularioUserPersonalData.html");
		//se vuelve a setear el texto utilizado para el filtrado
		sucursalesPopupMap.put("textoBusqueda", val);
		//se setea el codigo del cliente seleccionado.
		sucursalesPopupMap.put("filterPopUp", empresaCodigo);
		//codigo de la localización para el titulo del popup
		sucursalesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("sucursalesPopupMap", sucursalesPopupMap);
	}
	
	private void definirPopupEmpresa(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> empresasPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioUserPersonalData.empresa.codigo",false));
		campos.add(new CampoDisplayTag("razonSocial.razonSocial","formularioUserPersonalData.empresa.razonSocial",false));
		campos.add(new CampoDisplayTag("descripcion","formularioUserPersonalData.empresa.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		empresasPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		empresasPopupMap.put("coleccionPopup", empresaService.listarEmpresaPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		empresasPopupMap.put("referenciaHtml", "codigoEmpresa");
		//url que se debe consumir con ajax
		empresasPopupMap.put("urlRequest", "precargaFormularioUserPersonalData.html");
		//se vuelve a setear el texto utilizado para el filtrado
		empresasPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		empresasPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("empresasPopupMap", empresasPopupMap);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
