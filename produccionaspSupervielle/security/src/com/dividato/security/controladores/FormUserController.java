package com.dividato.security.controladores;

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

import com.dividato.security.validadores.UserValidator;
import com.security.accesoDatos.interfaz.GroupService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.Persona;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.Group;
import com.security.modelo.seguridad.User;
import com.security.recursos.RecursosPassword;
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
 * @modificado Ezequiel Beccaria (05/05/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioUser.html",
				"/guardarActualizarUser.html"
			}
		)
public class FormUserController {
//	private static Logger logger = Logger.getLogger(FormUserController.class);
	private ListaUserController listaUserController;
	private UserService userService;
	private UserValidator validator;
	private GroupService groupService;
//	private MailManager mailManager;
	
	/**
	 * Setea el servicio de User.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase UserImp implementa User y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param personaService
	 */
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setListaUserController(ListaUserController listaUserController) {
		this.listaUserController = listaUserController;
	}
	@Autowired
	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}
//	@Autowired
//	public void setMailManager(MailManager mailManager){
//		this.mailManager=mailManager;
//	}
	@Autowired
	public void setValidator(UserValidator validator) {
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
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioUser" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioUser.html",
			method = RequestMethod.GET
		)
	public String precargaFormulario(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) Long id,
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo
		Set<Group> gruposIzq=new TreeSet<Group>(groupService.listarTodosGroupFiltrados(null, obtenerClienteAspUser()));
		Set<Group> gruposDer=new TreeSet<Group>();
		if(!accion.equals("NUEVO")){
			User userFormulario;
			userFormulario=userService.obtenerPorId(id);
			atributos.put("userFormulario", userFormulario);
			gruposIzq.removeAll(userFormulario.getGroups());
			gruposDer.addAll(userFormulario.getGroups());
			if(userFormulario instanceof Empleado)
				atributos.put("editable", false);
			else
				atributos.put("editable", true);
		}else{
			atributos.put("editable", true);
		}
		atributos.put("accion", accion);		
		atributos.put("gruposIzq",gruposIzq);
		atributos.put("gruposDer",gruposDer);
		
		return "formularioUser";
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
	 * @return "formularioUser" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarUser.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="assignedRoles",required=false) String assignedRoles,
			@ModelAttribute("userForm") final User user,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			user.setCliente(obtenerClienteAspUser()); //seteo el cliente
			user.setUsernameSinCliente(user.getUsernameSinCliente()); //seteo el username concatenado al nombre del cliente
			user.setAccion(accion);
			user.setAssignedRoles(assignedRoles);
			validator.validate(user,result);
		}
			
		User userFormulario;
		Boolean commit = null;		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		String clienteAsp = obtenerClienteAspUser().getNombreAbreviado(); //variable donde se almacena la abreviacion del ClienteAsp
		String username = user.getUsernameSinCliente(); //variable donde se almacena el username para mostrar por pantalla
		String password = ""; //variable donde se almacena el password sin codificar para mostrar por pantalla
		if(!result.hasErrors()){
			//obtengo los grupos a asignar al user
			String[] groupsId = user.getAssignedRoles().split(",");
			user.setGroups(new TreeSet<Group>(groupService.listarPorId(stringToLong(groupsId), obtenerClienteAspUser())));		
			
			if(accion.equals("NUEVO")){
				if("".equals(user.getPassword())) //si no asignaron password
					user.setPassword(RecursosPassword.generarPassword()); //genero un password aleatorio
				password = user.getPassword();
				userFormulario = crearUsuario(
						obtenerClienteAspUser(), 
						user.getPersona(),
						user.getUsernameSinCliente(), 
						user.getPassword(),
						user.getGroups(),
						false);
			}else{
				if(!"".equals(user.getPassword()))
					password = user.getPassword();
				userFormulario = setearDatos(
						userService.obtenerPorId(user.getId()), 
						user.getCliente(), 
						user.getPersona(), 
						user.getUsername(), 
						user.getPassword(), 
						user.getGroups());
			}			
			
			if(userFormulario.getPersona().getDireccion() != null){
				Barrio b = userFormulario.getPersona().getDireccion().getBarrio();
				if(b != null){
					if(b.getId() == null)
						userFormulario.getPersona().getDireccion().setBarrio(null);
				}
			}			
			if(accion.equals("NUEVO"))
				commit = userService.save(userFormulario);
			else
				commit = userService.update(userFormulario);			
		}		
		//Ver errores
		if(commit != null && !commit){			
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("userFormulario", user);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormulario(accion, user.getId(), atributos);
		}		
		if(result.hasErrors()){
			atributos.put("userFormulario", user);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", false);
			atributos.remove("avisos");		
			return precargaFormulario(accion, user.getId(), atributos);
		}else{
			//Genero las notificaciones			
			if("NUEVO".equals(accion)){				
				ScreenMessage mensajeReg = new ScreenMessageImp("notif.user.guardado", null);
				avisos.add(mensajeReg); //agrego el mensaje a la coleccion				
			}else{
				ScreenMessage mensajeClienteReg = new ScreenMessageImp("notif.user.modificado", null);
				avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			}
			if(!"".equals(password)){
				//ClienteAsp
				List<String> clienteList = new ArrayList<String>();
				clienteList.add(clienteAsp+"   ");			
				ScreenMessage mensajeCli = new ScreenMessageImp("notif.user.guardado.cliente", clienteList);
				avisos.add(mensajeCli); //agrego el mensaje a la coleccion
				//Usuario
				List<String> usuarioList = new ArrayList<String>();
				usuarioList.add(username+"   ");			
				ScreenMessage mensajeUser = new ScreenMessageImp("notif.user.guardado.usuario", usuarioList);
				avisos.add(mensajeUser); //agrego el mensaje a la coleccion
				//Password
				List<String> passwordsList = new ArrayList<String>();
				passwordsList.add(password+"   ");
				ScreenMessage mensajePass = new ScreenMessageImp("notif.user.guardado.password", passwordsList);
				avisos.add(mensajePass); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaUserController.mostrar(session, atributos);
	}

	/**
	 * Metodo que crea un nuevo usuario del sistema.
	 * @param cliente
	 * @param propietario
	 * @param username
	 * @param password
	 * @param userGroups
	 * @return
	 */
	public User crearUsuario(ClienteAsp cliente, Persona propietario, String username, String password, Set<Group> userGroups, Boolean admin){
		User newUser = new User();
		newUser.setEnable(true);
		newUser.setCliente(cliente);
		newUser.setPersona(propietario);
		newUser.setUsernameSinCliente(username);
		newUser.setPassword(encriptarString(password));
		newUser.setGroups(userGroups);
		newUser.setAdmin(admin);
		return newUser;
	}
	
	public User setearDatos(User user, ClienteAsp cliente, Persona propietario, String username, String password, Set<Group> userGroups){
		user.setCliente(cliente);
		user.setPersona(setearDatos(user.getPersona(), propietario));
		if(!"".equals(password))
			user.setPassword(encriptarString(password));
		user.setGroups(userGroups);
		return user;
	}
	
	public Persona setearDatos(Persona persona, Persona datos){
		PersonaFisica pf = (PersonaFisica) persona;
		PersonaFisica pfDatos = (PersonaFisica) datos;
		pf.setNombre(pfDatos.getNombre());
		pf.setApellido(pfDatos.getApellido());
		pf.setMail(pfDatos.getMail());
		return pf;
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
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private Long[] stringToLong(String[] array){
		Long[] lArray = new Long[array.length];
		for(int i=0;i<array.length;i++)
			lArray[i] = Long.valueOf(array[i]);
		return lArray;
	}
}
