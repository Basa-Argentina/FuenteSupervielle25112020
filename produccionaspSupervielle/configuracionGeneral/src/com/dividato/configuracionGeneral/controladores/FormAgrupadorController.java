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

import com.dividato.configuracionGeneral.validadores.AgrupadorValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.AgrupadorService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.GrupoFacturacionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AgrupadorFacturacion;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.GrupoFacturacion;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Agrupador.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioAgrupador.html",
				"/guardarActualizarAgrupador.html",
				"/eliminarGrupoFacturacion.html"
			}
		)
public class FormAgrupadorController {
	private AgrupadorService agrupadorService;
	private AgrupadorValidator validator;
	private ListaAgrupadoresController listaAgrupadoresController;
	private GrupoFacturacionService grupoFacturacionService;
	private ClienteEmpService clienteEmpService;
	
		
	/**
	 * Setea el servicio de Agrupador.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase AgrupadorImp implementa Agrupador y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param agrupadorService
	 */
	@Autowired
	public void setAgrupadorService(AgrupadorService agrupadorService) {
		this.agrupadorService = agrupadorService;
	}
	
	@Autowired
	public void setGrupoFacturacionService(GrupoFacturacionService grupoFacturacionService) {
		this.grupoFacturacionService = grupoFacturacionService;
	}
	
	@Autowired
	public void setListaAgrupadoresController(ListaAgrupadoresController listaAgrupadoresController) {
		this.listaAgrupadoresController = listaAgrupadoresController;
	}
	
	@Autowired
	public void setValidator(AgrupadorValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioAgrupador.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Agrupador, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioAgrupador" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioAgrupador.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioAgrupador(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,			
			Map<String,Object> atributos, HttpSession session,
			@RequestParam(value="val", required=false) String val) {
		AgrupadorFacturacion agrupadorFormulario = null;
		if(session != null){
			session.setAttribute("idAgrupador", id);
		}
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			
			agrupadorFormulario = agrupadorService.obtenerPorId(Long.valueOf(id));
			
			atributos.put("agrupadorFormulario", agrupadorFormulario);			
		}
		
		GrupoFacturacion grupoFacturacionFil = new GrupoFacturacion();
		List<GrupoFacturacion> grupoFacturaciones = null;
		if(id != null){
			grupoFacturacionFil.setAgrupador(agrupadorFormulario);
			grupoFacturaciones = grupoFacturacionService.listarGrupoFacturacionFiltradas(grupoFacturacionFil);
		}
		int cantGrupoFacturaciones = 0;
		if(grupoFacturaciones != null){
			cantGrupoFacturaciones = grupoFacturaciones.size();
		}
		atributos.put("cantGrupoFacturaciones", cantGrupoFacturaciones);
		atributos.put("grupoFacturaciones", grupoFacturaciones);
	
		//Seteo la accion actual
		atributos.put("accion", accion);
		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		definirPopupClientes(atributos, val);
	
		//Se realiza el redirect
		return "formularioAgrupador";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos User.
	 * 
	 * @param Agrupador user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto Agrupador con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioAgrupador" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarAgrupador.html",
			method= RequestMethod.POST
		)
	public String guardarAgrupador(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="agrupadorTipo",required=false) String agrupadorTipo,
			@ModelAttribute("agrupadorFormulario") final AgrupadorFacturacion agrupadorFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		if(agrupadorTipo != null && !"".equals(agrupadorTipo) && agrupadorFormulario.getTipoAgrupador() == null){
			agrupadorFormulario.setTipoAgrupador(agrupadorTipo);
		}
		//Setear Cliente
		ClienteEmp cliente = new ClienteEmp();
		cliente.setCodigo(agrupadorFormulario.getClienteCodigo());
		cliente = clienteEmpService.getByCodigo(cliente, obtenerClienteAspUser());
		if(cliente != null){
			agrupadorFormulario.setClienteEmp(cliente);
		}
		
		if(agrupadorFormulario.getHabilitado() == null){
			agrupadorFormulario.setHabilitado(false);
		}
		
		
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){			
			agrupadorFormulario.setAccion(accion);
			agrupadorFormulario.setClienteAsp(obtenerClienteAspUser());
			validator.validate(agrupadorFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		AgrupadorFacturacion agrupador;
		if(!result.hasErrors()){
			
			if(accion.equals("NUEVO")){
				agrupador = agrupadorFormulario;								
				//Se guarda la agrupador en la BD
				commit = agrupadorService.guardarAgrupadorFacturacion(agrupador);
			}else{
				agrupador = agrupadorService.obtenerPorId(agrupadorFormulario.getId());
				setData(agrupador, agrupadorFormulario);
				//Se Actualiza el cliente en la BD
				commit = agrupadorService.actualizarAgrupadorFacturacion(agrupador);
			}			
			if(commit == null || !commit)
				agrupadorFormulario.setId(agrupador.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("agrupadorFormulario", agrupadorFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioAgrupador(accion, agrupadorFormulario.getId() !=null ? agrupadorFormulario.getId().toString() : null, atributos, session, null);
		}
		
		if(result.hasErrors()){
			atributos.put("agrupadorFormulario", agrupadorFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioAgrupador(accion, agrupadorFormulario.getId() !=null ? agrupadorFormulario.getId().toString() : null, atributos, session, null);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeAgrupadorReg = new ScreenMessageImp("formularioAgrupador.notificacion.agrupadorRegistrado", null);
			avisos.add(mensajeAgrupadorReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		return listaAgrupadoresController.mostrarAgrupador(session, atributos);
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar GrupoFacturacion.
	 * 
	 * @param idGrupoFacturacion el id de Empresa a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarGrupoFacturacion.html",
			method = RequestMethod.GET
		)
	public String eliminarCai(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos,
			@RequestParam(value="accion",required=false) String accion) {
		String idAgrupador = session.getAttribute("idAgrupador").toString();
		
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos el cai para eliminar luego
		GrupoFacturacion grupoFacturacion = grupoFacturacionService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la agrupador
		commit = grupoFacturacionService.eliminarGrupoFacturacion(grupoFacturacion);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.grupoFacturacion.grupoFacturacionEliminado", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return precargaFormularioAgrupador(accion, idAgrupador, atributos, session, null);
	}
	
/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void definirPopupClientes(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clientesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("razonSocial.razonSocial","formularioClienteDireccion.cliente.razonSocial",false));
		campos.add(new CampoDisplayTag("nombre","formularioClienteDireccion.cliente.nombre",false));
		campos.add(new CampoDisplayTag("apellido","formularioClienteDireccion.cliente.apellido",false));
		campos.add(new CampoDisplayTag("nombreYApellido","formularioClienteDireccion.cliente.apellido",true));
		campos.add(new CampoDisplayTag("codigo","formularioClienteDireccion.cliente.apellido",true));
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
	

	private void setData(AgrupadorFacturacion agrupador, AgrupadorFacturacion data){
		if(data != null){			
			agrupador.setCodigo(data.getCodigo());
			agrupador.setDescripcion(data.getDescripcion());
			agrupador.setHabilitado(data.getHabilitado());
			agrupador.setTipoAgrupador(data.getTipoAgrupador());
			agrupador.setClienteAsp(data.getClienteAsp());
			agrupador.setClienteEmp(data.getClienteEmp());
		}
	
	}
	

	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
}
