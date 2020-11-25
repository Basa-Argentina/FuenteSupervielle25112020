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

import com.dividato.configuracionGeneral.validadores.SeccionValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SeccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Seccion;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Seccion.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega (13/06/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioSeccion.html",
				"/guardarActualizarSeccion.html"
			}
		)
public class FormSeccionController {
	private SeccionService seccionService;
	private SeccionValidator validator;
	private DepositoService depositoService;
	private ListaSeccionController listaSeccionController;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	
		
	/**
	 * Setea el servicio de Seccion.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase SeccionImp implementa Seccion y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param seccionService
	 */
	@Autowired
	public void setSeccionService(SeccionService seccionService) {
		this.seccionService = seccionService;
	}
	@Autowired
	public void setListaSeccionController(ListaSeccionController listaSeccionController) {
		this.listaSeccionController = listaSeccionController;
	}	
	@Autowired
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	@Autowired
	public void setValidator(SeccionValidator validator) {
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
	 * para mostrar el formulario de Seccion, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioSeccion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioSeccion.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioSeccion(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,			
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo) {
		String clienteId = (String)atributos.get("clienteId");
		if(clienteId==null || clienteId.length()==0){
			atributos.put("clienteId", obtenerClienteAspUser().getId().toString());
		}
		
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			Seccion seccionFormulario;
			seccionFormulario = seccionService.obtenerPorId(Long.valueOf(id));
			
			atributos.put("seccionFormulario", seccionFormulario);			
		}
		
		
		//Seteo la accion actual
		atributos.put("accion", accion);
	
		definirPopupEmpresa(atributos, valEmpresa);
		definirPopupSucursal(atributos, valSucursal, empresaCodigo);
		definirPopupDepositos(atributos, valDeposito, sucursalCodigo);
		//Se realiza el redirect
		return "formularioSeccion";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos Seccion.
	 * 
	 * @param Seccion seccion a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto User con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioSeccion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarSeccion.html",
			method= RequestMethod.POST
		)
	public String guardarSeccion(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("seccionFormulario") final Seccion seccionFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//seteamos el ClienteAsp
		//seccionFormulario.setCliente(obtenerClienteAspUser());
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		
		//Seteamos el Deposito
		Deposito deposito = new Deposito();
		deposito.setCodigo(seccionFormulario.getCodigoDeposito());
		deposito = depositoService.getByCodigo(deposito, obtenerClienteAspUser());
		if(deposito != null)
			seccionFormulario.setDeposito(deposito);
		
		if(!result.hasErrors()){
			seccionFormulario.setAccion(accion);
			validator.validate(seccionFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Seccion seccion;
		if(!result.hasErrors()){			
			if(accion.equals("NUEVO")){
				seccion = seccionFormulario;				
				//Se guarda el cliente en la BD
				commit = seccionService.guardarSeccion(seccion);
			}else{
				seccion = seccionService.obtenerPorId(seccionFormulario.getId());
				setData(seccion, seccionFormulario);
				//Se Actualiza el cliente en la BD
				commit = seccionService.actualizarSeccion(seccion);
			}			
			if(commit == null || !commit)
				seccionFormulario.setId(seccion.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("seccionFormulario", seccionFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioSeccion(accion, seccionFormulario.getId() != null ? seccionFormulario.getId().toString() : null, atributos,null, null, null, null, null);
		}
		
		if(result.hasErrors()){
			atributos.put("seccionFormulario", seccionFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioSeccion(accion, seccionFormulario.getId() != null ? seccionFormulario.getId().toString() : null, atributos, null, null, null, null, null);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeSeccionReg = new ScreenMessageImp("formularioSeccion.notificacion.seccionRegistrada", null);
			avisos.add(mensajeSeccionReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
//		//hacemos el redirect
//		return "redirect:mostrarCliente.html";
		return listaSeccionController.mostrarSeccion(session, atributos, null, null, null, null, null);
	}
	
	private void setData(Seccion seccion, Seccion data){
		if(data != null){			
			seccion.setCodigo(data.getCodigo());
			seccion.setDescripcion(data.getDescripcion());
			seccion.setDeposito(data.getDeposito());			
		}
	
	}
	
/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void definirPopupDepositos(Map<String,Object> atributos, String val, String sucursalCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> depositosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioDeposito.datosDeposito.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioDeposito.datosDeposito.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		depositosPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		depositosPopupMap.put("coleccionPopup", depositoService.listarDepositoPopup(val, sucursalCodigo, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		depositosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		depositosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		depositosPopupMap.put("referenciaHtml", "codigoDeposito"); 		
		//url que se debe consumir con ajax
		depositosPopupMap.put("urlRequest", "mostrarStock.html");
		//se vuelve a setear el texto utilizado para el filtrado
		depositosPopupMap.put("textoBusqueda", val);
		
		depositosPopupMap.put("filterPopUp", sucursalCodigo);
		//codigo de la localización para el titulo del popup
		depositosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("depositosPopupMap", depositosPopupMap);
	}
	
	private void definirPopupSucursal(Map<String,Object> atributos, String val, String empresaCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> sucursalesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioSucursal.datosSucursal.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioSucursal.datosSucursal.descripcion",false));
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
		sucursalesPopupMap.put("urlRequest", "mostrarStock.html");
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
		campos.add(new CampoDisplayTag("codigo","formularioEmpresa.datosEmpresa.codigo",false));
		campos.add(new CampoDisplayTag("razonSocial.razonSocial","formularioEmpresa.datosEmpresa.razonSocial",false));
		campos.add(new CampoDisplayTag("descripcion","formularioEmpresa.datosEmpresa.descripcion",false));
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
		empresasPopupMap.put("urlRequest", "mostrarStock.html");
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
