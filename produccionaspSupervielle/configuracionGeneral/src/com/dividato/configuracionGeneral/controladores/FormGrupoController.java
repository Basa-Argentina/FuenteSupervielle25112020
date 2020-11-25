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

import com.dividato.configuracionGeneral.validadores.GrupoValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EstanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.GrupoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SeccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Estante;
import com.security.modelo.configuraciongeneral.Grupo;
import com.security.modelo.configuraciongeneral.Seccion;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Grupo.
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
				"/precargaFormularioGrupo.html",
				"/guardarActualizarGrupo.html"
				
			}
		)
public class FormGrupoController {
	private GrupoService grupoService;
	private GrupoValidator validator;
	private DepositoService depositoService;
	private SeccionService seccionService;
	private EstanteService estanteService;
	private ListaGrupoController listaGrupoController;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	
		
	/**
	 * Setea el servicio de Grupo.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase GrupoImp implementa Grupo y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param grupoService
	 */
	@Autowired
	public void setGrupoService(GrupoService grupoService) {
		this.grupoService = grupoService;
	}
	@Autowired
	public void setListaGrupoController(ListaGrupoController listaGrupoController) {
		this.listaGrupoController = listaGrupoController;
	}	
	@Autowired
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	@Autowired
	public void setSeccionService(SeccionService seccionService) {
		this.seccionService = seccionService;
	}
	@Autowired
	public void setEstanteService(EstanteService estanteService) {
		this.estanteService = estanteService;
	}
	@Autowired
	public void setValidator(GrupoValidator validator) {
		this.validator = validator;
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
	 * Mapea la URL /precargaFormularioUser.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Grupo, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioGrupo" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioGrupo.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioGrupo(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,			
			Map<String,Object> atributos, HttpSession session,
			@RequestParam(value="val", required=false) String valSeccion,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			@RequestParam(value="depositoCodigo", required=false) String depositoCodigo) {
		if(session != null){
			session.setAttribute("idGrupo", id);
		}
		if(accion==null){
			accion="NUEVO";//acción por defecto: nuevo	
			Grupo grupoFormulario = new Grupo();
			String empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto().getCodigo();
			String sucursalDefecto = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto().getCodigo();
			grupoFormulario.setCodigoEmpresa(empresaDefecto);
			grupoFormulario.setCodigoSucursal(sucursalDefecto);
			atributos.put("grupoFormulario", grupoFormulario);
				
		}
		if(!accion.equals("NUEVO")){
			Grupo grupoFormulario;
			grupoFormulario = grupoService.obtenerPorId(Long.valueOf(id));
			grupoFormulario.setCodigoSeccion(grupoFormulario.getSeccion().getCodigo());
			grupoFormulario.setCodigoSucursal(grupoFormulario.getSeccion().getDeposito().getSucursal().getCodigo());
			grupoFormulario.setCodigoDeposito(grupoFormulario.getSeccion().getDeposito().getCodigo());
			grupoFormulario.setCodigoEmpresa(grupoFormulario.getSeccion().getDeposito().getSucursal().getEmpresa().getCodigo());
			
			atributos.put("grupoFormulario", grupoFormulario);			
		}	
		
		Estante estanteFil = new Estante();
		List<Estante> estantes = null;
		if(id != null){
			estanteFil.setIdGrupo(Long.valueOf(id));
			estantes = estanteService.listarEstanteFiltradas(estanteFil, obtenerClienteAspUser());
		}
		atributos.put("estantes", estantes);
		
		//Seteo la accion actual
		atributos.put("accion", accion);
		
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		definirPopupEmpresa(atributos, valEmpresa);
		definirPopupSucursal(atributos, valSucursal, empresaCodigo);
		definirPopupDepositos(atributos, valDeposito, sucursalCodigo);
		definirPopupSecciones(atributos, valSeccion, depositoCodigo);	
	
		//Se realiza el redirect
		return "formularioGrupo";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos Grupo.
	 * 
	 * @param Grupo grupo a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto User con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioGrupo" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarGrupo.html",
			method= RequestMethod.POST
		)
	public String guardarGrupo(
			@RequestParam(value="accion",required=false) String accion,	
			@RequestParam(value="seccionId",required=false) String seccionId,
			@ModelAttribute("grupoFormulario") final Grupo grupoFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//seteamos el ClienteAsp
		//grupoFormulario.setCliente(obtenerClienteAspUser());
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			if(seccionId != null && !"".equals(seccionId)){
				grupoFormulario.setIdSeccion(Long.parseLong(seccionId));
			}
			grupoFormulario.setAccion(accion);
			validator.validate(grupoFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Grupo grupo;
		if(!result.hasErrors()){
			Seccion seccionSel = new Seccion();
			seccionSel.setCodigo(grupoFormulario.getCodigoSeccion());
			//obtengo la seccion seleccionada
			seccionSel = seccionService.getByCodigo(seccionSel, obtenerClienteAspUser());
			if(seccionSel != null){
				grupoFormulario.setSeccion(seccionSel); // seteo el mismo en la seccion
			}			
			
			if(accion.equals("NUEVO")){
				grupo = grupoFormulario;				
				//Se guarda el cliente en la BD
				commit = grupoService.guardarGrupo(grupo);
			}else{
				grupo = grupoService.obtenerPorId(grupoFormulario.getId());
				setData(grupo, grupoFormulario);
				//Se Actualiza el cliente en la BD
				commit = grupoService.actualizarGrupo(grupo);
			}			
			if(commit == null || !commit)
				grupoFormulario.setId(grupo.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("grupoFormulario", grupoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioGrupo(accion, grupoFormulario.getId() != null ? grupoFormulario.getId().toString() : null, atributos, session, null, null, null, null, null, null, null);
		}
		
		if(result.hasErrors()){
			atributos.put("grupoFormulario", grupoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioGrupo(accion, grupoFormulario.getId() != null ? grupoFormulario.getId().toString() : null, atributos, session, null, null, null, null, null, null, null);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeGrupoReg = new ScreenMessageImp("formularioGrupo.notificacion.grupoRegistrado", null);
			avisos.add(mensajeGrupoReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
//		//hacemos el redirect
//		return "redirect:mostrarCliente.html";
		return listaGrupoController.mostrarGrupo(session, atributos, null, null, null, null, null, null, null);
	}
	
	private void setData(Grupo grupo, Grupo data){
		if(data != null){			
			grupo.setCodigo(data.getCodigo());
			grupo.setDescripcion(data.getDescripcion());
			grupo.setSeccion(data.getSeccion());
			grupo.setVerticales(data.getVerticales());
			grupo.setHorizontales(data.getHorizontales());
			grupo.setModulosVert(data.getModulosVert());
			grupo.setModulosHor(data.getModulosHor());
		}
	
	}
	
/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void definirPopupSecciones(Map<String,Object> atributos, String val, String depositoCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> seccionesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioSeccion.datosSeccion.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioSeccion.datosSeccion.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		seccionesPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		seccionesPopupMap.put("coleccionPopup", seccionService.listarSeccionPopup(val, depositoCodigo, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		seccionesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		seccionesPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		seccionesPopupMap.put("referenciaHtml", "codigoSeccion"); 		
		//url que se debe consumir con ajax
		seccionesPopupMap.put("urlRequest", "precargaFormularioGrupo.html");
		//se vuelve a setear el texto utilizado para el filtrado
		seccionesPopupMap.put("textoBusqueda", val);
		
		seccionesPopupMap.put("filterPopUp", depositoCodigo);
		//codigo de la localización para el titulo del popup
		seccionesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("seccionesPopupMap", seccionesPopupMap);
	}
	
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
		depositosPopupMap.put("urlRequest", "precargaFormularioGrupo.html");
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
		sucursalesPopupMap.put("urlRequest", "precargaFormularioGrupo.html");
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
		empresasPopupMap.put("urlRequest", "precargaFormularioGrupo.html");
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
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
}
