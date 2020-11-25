package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
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

import com.dividato.configuracionGeneral.validadores.GrupoFacturacionValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.AgrupadorService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.GrupoFactDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.GrupoFacturacionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AgrupadorFacturacion;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.GrupoFactDetalle;
import com.security.modelo.configuraciongeneral.GrupoFacturacion;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de GrupoFacturacion.
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
				"/precargaFormularioGrupoFacturacion.html",
				"/guardarActualizarGrupoFacturacion.html",
				"/volverFormularioAgrupador.html"
			}
		)
public class FormGrupoFacturacionController {
	private GrupoFacturacionService grupoFacturacionService;
	private GrupoFacturacionValidator validator;
	private FormAgrupadorController formAgrupadorController;
	private AgrupadorService agrupadorService;
	private EmpleadoService empleadoService;
	private ClienteDireccionService clienteDireccionService;
	private GrupoFactDetalleService grupoFactDetalleService;
	
		
	/**
	 * Setea el servicio de GrupoFacturacion.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase GrupoFacturacionImp implementa GrupoFacturacion y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param grupoFacturacionService
	 */
	@Autowired
	public void setGrupoFacturacionService(GrupoFacturacionService grupoFacturacionService) {
		this.grupoFacturacionService = grupoFacturacionService;
	}
	
	@Autowired
	public void setGrupoFactDetalleService(GrupoFactDetalleService grupoFactDetalleService) {
		this.grupoFactDetalleService = grupoFactDetalleService;
	}
	
	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	
	@Autowired
	public void setClienteDireccionService(ClienteDireccionService clienteDireccionService) {
		this.clienteDireccionService = clienteDireccionService;
	}
	
	@Autowired
	public void setFormAgrupadorController(FormAgrupadorController formAgrupadorController) {
		this.formAgrupadorController = formAgrupadorController;
	}
	
	@Autowired
	public void setAgrupadorService(AgrupadorService agrupadorService) {
		this.agrupadorService = agrupadorService;
	}
	
	@Autowired
	public void setValidator(GrupoFacturacionValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioGrupoFacturacion.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de GrupoFacturacion, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioGrupoFacturacion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(
			value="/precargaFormularioGrupoFacturacion.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioGrupoFacturacion(
			@RequestParam(value="accionAgrupador",required=false) String accionAgrupador,
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,			
			Map<String,Object> atributos,
			HttpSession session) {	
		//Lista de Direcciones
		Set<ClienteDireccion> direccionesIzq = new TreeSet<ClienteDireccion>();
		Set<ClienteDireccion> direccionesDer=new TreeSet<ClienteDireccion>();
		//Lista de Empleados
		Set<Empleado> empleadosIzq=new TreeSet<Empleado>();
		Set<Empleado> empleadosDer=new TreeSet<Empleado>();
		
		String idAgrupador = session.getAttribute("idAgrupador").toString();
		AgrupadorFacturacion agrupadorSel = agrupadorService.obtenerPorId(Long.valueOf(idAgrupador));
		Empleado emp = new Empleado();
		emp.setClienteCodigo(agrupadorSel.getClienteEmp().getId().toString());
		//Creamos la Direccion
		ClienteDireccion direccion = new ClienteDireccion();
		direccion.setClienteCodigo(agrupadorSel.getClienteEmp().getCodigo());
		
		//Creamos el Detalle para sacar todos los empleados y direcciones ya asociadas
		GrupoFacturacion grupo = new GrupoFacturacion();
		grupo.setAgrupador(agrupadorSel);
		GrupoFactDetalle detalle  = new GrupoFactDetalle();
		detalle.setGrupoFacturacion(grupo);
		
		//Cargamos la lista segun el agrupador
		if("E".equals(agrupadorSel.getTipoAgrupador())){
			empleadosIzq=new TreeSet<Empleado>(empleadoService.listarEmpleadoFiltradas(emp, obtenerClienteAspUser()));
			//Sacamos todos los empleados ya asociados
			empleadosIzq = eliminar(new TreeSet(grupoFactDetalleService.listarGrupoFactDetalleEmpleados(detalle, obtenerClienteAspUser())), empleadosIzq);
		
			atributos.put("empleadosIzq",empleadosIzq);
			
		}else if("D".equals(agrupadorSel.getTipoAgrupador())){
			direccionesIzq.addAll(clienteDireccionService.listarClienteDireccionesFiltradasPorCliente(direccion, obtenerClienteAspUser()));
			//Sacamos todas las direcciones ya asociadas
			direccionesIzq = eliminar(new TreeSet(grupoFactDetalleService.listarGrupoFactDetalleDirecciones(detalle, obtenerClienteAspUser())), direccionesIzq);
			
			atributos.put("direccionesIzq",direccionesIzq);
		}
		
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			GrupoFacturacion grupoFacturacionFormulario;
			grupoFacturacionFormulario = grupoFacturacionService.obtenerPorId(Long.valueOf(id));
			
			atributos.put("grupoFacturacionFormulario", grupoFacturacionFormulario);
			
			if("E".equals(agrupadorSel.getTipoAgrupador())){
				empleadosDer = grupoFacturacionFormulario.getEmpleados();
				empleadosIzq.removeAll(grupoFacturacionFormulario.getEmpleados());
			}else if("D".equals(agrupadorSel.getTipoAgrupador())){
				direccionesDer = grupoFacturacionFormulario.getDirecciones();
				direccionesIzq.removeAll(grupoFacturacionFormulario.getDirecciones());
			}

			
		}
		//Seteo la accion actual
		atributos.put("accion", accion);
		
		//Seteo la accion actual
		atributos.put("tipoAgrupador", agrupadorSel.getTipoAgrupador());
		
		//Seteo las listas direcciones y empleados
		atributos.put("direccionesDer", direccionesDer);
		atributos.put("empleadosDer", empleadosDer);
		
		//Seteo la accion de la Agrupador
		atributos.put("accionAgrupador", accionAgrupador);
	
		//Se realiza el redirect
		return "formularioGrupoFacturacion";
	}
	
	@RequestMapping(
			value="/volverFormularioAgrupador.html",
			method = RequestMethod.GET
		)
	public String volverFormularioAgrupador(
			HttpSession session,	
			Map<String,Object> atributos,
			@RequestParam(value="accionAgrupador",required=false) String accionAgrupador,
			@RequestParam(value="accion",required=false) String accion) {
		String idAgrupador = session.getAttribute("idAgrupador").toString();
	
		session.removeAttribute("idAgrupador");
		return formAgrupadorController.precargaFormularioAgrupador(accionAgrupador, idAgrupador, atributos, session, null);
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos User.
	 * 
	 * @param GrupoFacturacion user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto GrupoFacturacion con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioGrupoFacturacion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarGrupoFacturacion.html",
			method= RequestMethod.POST
		)
	public String guardarGrupoFacturacion(
			@RequestParam(value="accionAgrupador",required=false) String accionAgrupador,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="direccionesSeleccionadas",required=false) String direccionesSeleccionadas,
			@RequestParam(value="empleadosSeleccionados",required=false) String empleadosSeleccionados,
			@ModelAttribute("grupoFacturacionFormulario") final GrupoFacturacion grupoFacturacionFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		String idAgrupador = session.getAttribute("idAgrupador").toString();
		//obtengo la empresa seleccionada seleccionada.
		AgrupadorFacturacion agrupadorSel = agrupadorService.obtenerPorId(Long.valueOf(idAgrupador));			
		grupoFacturacionFormulario.setAgrupador(agrupadorSel); // seteo el mismo en la agrupador.
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			grupoFacturacionFormulario.setAccion(accion);
			validator.validate(grupoFacturacionFormulario,result);
		}
		//Setea los empleados seleccionados en el detalle
		if(empleadosSeleccionados != null && !"".equals(empleadosSeleccionados)){
			String[] listasId = empleadosSeleccionados.split(",");
			//Seteamos el Detalle
			grupoFacturacionFormulario.setDetalles(crearDetalleConEmpleado(listasId, grupoFacturacionFormulario));
		}
		
		//Setea las direcciones del cliente seleccionadas en el detalle
		if(direccionesSeleccionadas != null && !"".equals(direccionesSeleccionadas)){
			String[] listasId = direccionesSeleccionadas.split(",");
			//Seteamos el Detalle
			grupoFacturacionFormulario.setDetalles(crearDetalleConDirecciones(listasId, grupoFacturacionFormulario));
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		GrupoFacturacion grupoFacturacion;
		if(!result.hasErrors()){
			if(accion.equals("NUEVO")){
				grupoFacturacion = grupoFacturacionFormulario;								
				//Se guarda la grupoFacturacion en la BD
				commit = grupoFacturacionService.guardarGrupoFacturacion(grupoFacturacion);
			}else{
				grupoFacturacion = grupoFacturacionService.obtenerPorId(Long.valueOf(id));
				setData(grupoFacturacion, grupoFacturacionFormulario);
				//Se Actualiza el cliente en la BD
				commit = grupoFacturacionService.actualizarGrupoFacturacion(grupoFacturacion);
			}
			if(commit == null || !commit)
				grupoFacturacionFormulario.setId(grupoFacturacion.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("grupoFacturacionFormulario", grupoFacturacionFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioGrupoFacturacion(accionAgrupador, accion, grupoFacturacionFormulario.getId() != null ?grupoFacturacionFormulario.getId().toString() : null, atributos, session);
		}	
		
		if(result.hasErrors()){
			atributos.put("grupoFacturacionFormulario", grupoFacturacionFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioGrupoFacturacion(accionAgrupador, accion, grupoFacturacionFormulario.getId() != null ?grupoFacturacionFormulario.getId().toString() : null, atributos, session);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeGrupoFacturacionReg = new ScreenMessageImp("formularioGrupoFacturacion.notificacion.grupoFacturacionRegistrado", null);
			avisos.add(mensajeGrupoFacturacionReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		 session.removeAttribute("idAgrupador");
		return formAgrupadorController.precargaFormularioAgrupador(accionAgrupador, idAgrupador, atributos, session, null);
	}
	
	private void setData(GrupoFacturacion grupoFacturacion, GrupoFacturacion data){
		if(data != null){			
			grupoFacturacion.setAgrupador(data.getAgrupador());
			grupoFacturacion.setCodigo(data.getCodigo());
			grupoFacturacion.setDescripcion(data.getDescripcion());
			grupoFacturacion.setObservacion(data.getObservacion());
			grupoFacturacion.setDetalles(data.getDetalles());
		}
	
	}
	
	private Set<GrupoFactDetalle> crearDetalleConEmpleado(String[] listId, GrupoFacturacion grupo){
		Set<GrupoFactDetalle> setGrupoFactDetalle = new TreeSet<GrupoFactDetalle>();
		List<Empleado> empleados = empleadoService.listarPorId(stringToLong(listId), obtenerClienteAspUser());
		GrupoFactDetalle agregar = null;
		if(empleados != null){
			for(Empleado emp : empleados){
				agregar = new GrupoFactDetalle();
				agregar.setGrupoFacturacion(grupo);
				agregar.setEmpleado(emp);
				setGrupoFactDetalle.add(agregar);
			}
		}
			
		return setGrupoFactDetalle;
	}
	
	private Set<GrupoFactDetalle> crearDetalleConDirecciones(String[] listId, GrupoFacturacion grupo){
		Set<GrupoFactDetalle> setGrupoFactDetalle = new TreeSet<GrupoFactDetalle>();
		List<ClienteDireccion> direcciones = clienteDireccionService.listarPorId(stringToLong(listId), obtenerClienteAspUser());
		GrupoFactDetalle agregar = null;
		if(direcciones != null){
			for(ClienteDireccion dire : direcciones){
				agregar = new GrupoFactDetalle();
				agregar.setGrupoFacturacion(grupo);
				agregar.setDireccionEntrega(dire);
				setGrupoFactDetalle.add(agregar);
			}		
		}
			
		return setGrupoFactDetalle;
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	
	/*
	 * Creamos el nuevo Set con los valores que no se encuentra dentro
	 * de la lista para eliminar.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Set eliminar(Set elemntsDelete, Set elemntsCompare){
		Set reemplazar = new TreeSet();
		for(Object compare : elemntsCompare){
			if(!elemntsDelete.contains(compare)){
				reemplazar.add(compare);
			}
		}			
		return reemplazar;		
	}
	

	private Long[] stringToLong(String[] array){
		Long[] lArray = new Long[array.length];
		for(int i=0;i<array.length;i++)
			lArray[i] = Long.valueOf(array[i]);
		return lArray;
	}
	
}
