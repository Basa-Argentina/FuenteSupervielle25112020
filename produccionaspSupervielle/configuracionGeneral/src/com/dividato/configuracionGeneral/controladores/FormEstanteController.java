package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

import com.dividato.configuracionGeneral.validadores.EstanteValidator;
import com.security.accesoDatos.configuraciongeneral.hibernate.ModuloServiceImp;
import com.security.accesoDatos.configuraciongeneral.hibernate.PosicionServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EstanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.GrupoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ModuloService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PosicionService;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.jerarquias.interfaz.TipoJerarquiaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Estante;
import com.security.modelo.configuraciongeneral.Grupo;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.jerarquias.TipoJerarquia;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Estante.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 * @modificado Gonzalo Noriega (08/06/2011)
 * @modificado Victor Kenis (16/08/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioEstante.html",
				"/guardarActualizarEstante.html",
				"/volverFormularioGrupo.html",
				"/eliminarEstanteria.html"
			}
		)
public class FormEstanteController {
	private EstanteService estanteService;
	private EstanteValidator validator;
	private FormGrupoController formGrupoController;
	private GrupoService grupoService;
	private TipoJerarquiaService tipoJerarquiaService;
	private EmpresaService empresaService;
	
		
	/**
	 * Setea el servicio de Estante.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase EstanteImp implementa Estante y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param estanteService
	 */
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setipoJerarquiaService(TipoJerarquiaService tipoJerarquiaService) {
		this.tipoJerarquiaService = tipoJerarquiaService;
	}
	
	@Autowired
	public void setEstanteService(EstanteService estanteService) {
		this.estanteService = estanteService;
	}
	
	@Autowired
	public void setFormGrupoController(FormGrupoController formGrupoController) {
		this.formGrupoController = formGrupoController;
	}
	
	@Autowired
	public void setGrupoService(GrupoService grupoService) {
		this.grupoService = grupoService;
	}
	
	@Autowired
	public void setValidator(EstanteValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioEstante.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Estante, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioEstante" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioEstante.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioEstante(
			HttpSession session,
			@RequestParam(value="accionGrupo",required=false) String accionGrupo,
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,	
			@RequestParam(value="val", required=false) String val,
			@RequestParam(value="clienteCodigo",required=false) Long ubicacionId,
			Map<String,Object> atributos) {
		String idGrupo = session.getAttribute("idGrupo").toString();
		//obtengo la empresa seleccionada seleccionada.
		Grupo grupoSel = grupoService.obtenerPorId(Long.valueOf(idGrupo));
		//acción por defecto: nuevo
		if(accion==null){
			accion="NUEVO";
			String prevCodigo = estanteService.traerUltCodigoPorDeposito(grupoSel.getSeccion().getDeposito().getId());
			Integer nextCodigo = Integer.parseInt(prevCodigo) + 1;
			String codigo = nextCodigo.toString();
			Estante estanteFormulario  = new Estante();
			estanteFormulario.setCodigo(codigo);
			atributos.put("estanteFormulario", estanteFormulario);	
		}
		if(!accion.equals("NUEVO")){
			Estante estanteFormulario;
			estanteFormulario = estanteService.obtenerPorId(Long.valueOf(id));
			
			atributos.put("estanteFormulario", estanteFormulario);			
		}
		definirPopupTipoJerarquia(atributos, val, accion, id);
		//Seteo la accion actual
		atributos.put("accion", accion);
		//Seteo la accion del Grupo
		atributos.put("accionGrupo", accionGrupo);
	
		//Se realiza el redirect
		return "formularioEstante";
	}
	
	@RequestMapping(
			value="/volverFormularioGrupo.html",
			method = RequestMethod.GET
		)
	public String volverFormularioGrupo(
			HttpSession session,	
			Map<String,Object> atributos,
			@RequestParam(value="accionGrupo",required=false) String accionGrupo,
			@RequestParam(value="accion",required=false) String accion) {
		String idGrupo = session.getAttribute("idGrupo").toString();
	
		session.removeAttribute("idGrupo");
		return formGrupoController.precargaFormularioGrupo(accionGrupo, idGrupo, atributos, session, null, null, null, null, null, null, null);
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos User.
	 * 
	 * @param Estante user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto Estante con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioEstante" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarEstante.html",
			method= RequestMethod.POST
		)
	public String guardarEstante(
			@RequestParam(value="accionGrupo",required=false) String accionGrupo,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@ModelAttribute("estanteFormulario") final Estante estanteFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		String idGrupo = session.getAttribute("idGrupo").toString();
		//obtengo la empresa seleccionada seleccionada.
		Grupo grupoSel = grupoService.obtenerPorId(Long.valueOf(idGrupo));			
		estanteFormulario.setGrupo(grupoSel); // seteo el mismo en la grupo.
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			estanteFormulario.setAccion(accion);
			validator.validate(estanteFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		//obtengo la empresa seleccionada
		TipoJerarquia tipoJerarquia = tipoJerarquiaService.getByCodigo(estanteFormulario.getCodigoTipoJ(), obtenerClienteAspEmpleado());
		estanteFormulario.setTipoJerarquia(tipoJerarquia);
		
		Estante estante;
		if(!result.hasErrors()){
			if(accion.equals("NUEVO")){
				estante = estanteFormulario;								
				//Se guarda la estante en la BD
				commit = estanteService.guardarEstante(estante);
			}else{
				estante = estanteService.obtenerPorId(Long.valueOf(id));
				setData(estante, estanteFormulario);
				//Se Actualiza el cliente en la BD
				commit = estanteService.actualizarEstante(estante);
			}			
			if(commit == null || !commit)
				estanteFormulario.setId(estante.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("estanteFormulario", estanteFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioEstante(session, accionGrupo, accion, estanteFormulario.getId()!= null? estanteFormulario.getId().toString():null, null,null,atributos);
		}
		
		if(result.hasErrors()){
			atributos.put("estanteFormulario", estanteFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioEstante(session, accionGrupo, accion, estanteFormulario.getId()!= null? estanteFormulario.getId().toString():null,null,null, atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeEstanteReg = new ScreenMessageImp("formularioEstante.notificacion.estanteRegistrado", null);
			avisos.add(mensajeEstanteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);
		}
		 session.removeAttribute("idGrupo");
		return formGrupoController.precargaFormularioGrupo(accionGrupo, idGrupo, atributos, session, null, null, null, null, null, null, null);
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Estante.
	 * 
	 * @param idGrupo el id de Grupo.
	 * @param idEstante el id de estante a eliminar
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de formulario y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarEstanteria.html",
			method = RequestMethod.GET
		)
	public String eliminarEstanteria(HttpSession session,
			@RequestParam("idEstante") String idEstante,
			@RequestParam("idGrupo") String idGrupo,
			Map<String,Object> atributos) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos estante para eliminar luego
		Estante estante = estanteService.obtenerPorId(Long.valueOf(idEstante));
		ScreenMessage mensaje = null;
		//verificamos que el estante no tenga posiciones ocupadas
		Boolean estanteVacio = estanteService.estaVacio(estante);
		//verificamos que no existan elementos en las posiciones
		Boolean NoHayElementos = estanteService.NoHayElementos(estante.getId());
		if(estanteVacio && NoHayElementos){

			//Eliminamos estante
			commit = estanteService.eliminarEstanteModulosPosiciones(estante);
			//Controlamos su eliminacion.
			if(commit){
				mensaje = new ScreenMessageImp("formularioGrupo.notificacion.estanteEliminado", null);
				hayAvisos = true;
			}else{
				mensaje = new ScreenMessageImp("error.deleteDataBase", null);
				hayAvisosNeg = true;
			}
		}else{
			mensaje = new ScreenMessageImp("formularioGrupo.errorEliminarEstante", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		String accion="MODIFICACION";
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return formGrupoController.precargaFormularioGrupo(accion, idGrupo, atributos, session, null, null, null, null, null, null, null);
	}
	
	private void setData(Estante estante, Estante data){
		if(data != null){			
			estante.setGrupo(data.getGrupo());
			estante.setCodigo(data.getCodigo());
			estante.setObservacion(data.getObservacion());
			estante.setTipoJerarquia(data.getTipoJerarquia());
		}
	
	}
	
	private void definirPopupTipoJerarquia(Map<String,Object> atributos, String val, String accion, String id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> tiposJPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioEmpresa.datosEmpresa.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioEmpresa.datosEmpresa.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		tiposJPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		tiposJPopupMap.put("coleccionPopup", tipoJerarquiaService.listarTipoJerarquiaPopup(val, obtenerClienteAspEmpleado()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		tiposJPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		tiposJPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		tiposJPopupMap.put("referenciaHtml", "codigoTipoJ");
		//url que se debe consumir con ajax
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		tiposJPopupMap.put("urlRequest", 
				"precargaFormularioEstante.html?" +
				"accion="+accion +				
				idParam);
		//se vuelve a setear el texto utilizado para el filtrado
		tiposJPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		tiposJPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("tiposJPopupMap", tiposJPopupMap);
	}
	
	private ClienteAsp obtenerClienteAspEmpleado(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
}
