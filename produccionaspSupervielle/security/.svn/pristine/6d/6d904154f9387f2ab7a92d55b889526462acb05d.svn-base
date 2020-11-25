package com.dividato.security.controladores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.security.validadores.ClienteValidator;
import com.security.accesoDatos.hibernate.ClienteAspServiceImp;
import com.security.accesoDatos.interfaz.AuthorityService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.accesoDatos.interfaz.TipoDocumentoService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.Localidad;
import com.security.modelo.general.Pais;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.general.PersonaJuridica;
import com.security.modelo.general.Provincia;
import com.security.modelo.general.TipoDocumento;
import com.security.modelo.seguridad.Authority;
import com.security.modelo.seguridad.Group;
import com.security.modelo.seguridad.User;
import com.security.recursos.RecursosPassword;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
import com.security.utils.comparators.OrdenaPaisesPorNombrePrimeroArgentina;

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
				"/precargaFormularioCliente.html",
				"/guardarActualizarCliente.html"
			}
		)
public class FormClienteController {
	private ClienteAspService clienteAspService;
	private ClienteValidator validator;
	private FormUserController formUserController;
	private FormGroupController formGroupController;
	private AuthorityService authorityService;
	private TipoDocumentoService tipoDocumentoService;
	private UserService userService;
	private PaisService paisService;
	private ProvinciaService provinciaService;
	private LocalidadService localidadService;
	private BarrioService barrioService;
	private ListaClientesController listaClientesController;
	private static Logger logger=Logger.getLogger(FormClienteController.class);
		
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
	public void setAuthorityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}
	@Autowired
	public void setListaClientesController(ListaClientesController listaClientesController) {
		this.listaClientesController = listaClientesController;
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
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	@Autowired
	public void setFormGroupController(FormGroupController formGroupController) {
		this.formGroupController = formGroupController;
	}
	@Autowired
	public void setFormUserController(FormUserController formUserController) {
		this.formUserController = formUserController;
	}
	@Autowired
	public void setClienteAspService(ClienteAspServiceImp clienteService) {
		this.clienteAspService = clienteService;
	}
	@Autowired
	public void setValidator(ClienteValidator validator) {
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
			value="/precargaFormularioCliente.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioCliente(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,			
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			ClienteAsp clienteFormulario;
			clienteFormulario = clienteAspService.obtenerPorId(Long.valueOf(id));
			//obtengo el usuario del contato principal
			clienteFormulario.setUser((User) userService.listarPorPersona(clienteFormulario.getContacto()));
			//cargos todas las provincias, localidades y barrios para el cliente
			Barrio barrioSel = clienteFormulario.getPersona().getDireccion().getBarrio(); //busco el barrio
			List<Provincia> provincias = provinciaService.listarProvinciasPorPaisId(barrioSel.getLocalidad().getProvincia().getPais().getId());
			List<Localidad> localidades = localidadService.listarLocalidadesPorProcinciaId(barrioSel.getLocalidad().getProvincia().getId());
			List<Barrio> barrios = barrioService.listarBarriosPorLocalidadId(barrioSel.getLocalidad().getId());
			atributos.put("provincias", provincias);			
			atributos.put("localidades", localidades);			
			atributos.put("barrios", barrios);			
			
			//cargos todas las provincias, localidades y barrios para el contacto
			Barrio barrioSelCont = clienteFormulario.getContacto().getDireccion().getBarrio(); //busco el barrio
			List<Provincia> provinciasCont = provinciaService.listarProvinciasPorPaisId(barrioSelCont.getLocalidad().getProvincia().getPais().getId());
			List<Localidad> localidadesCont = localidadService.listarLocalidadesPorProcinciaId(barrioSelCont.getLocalidad().getProvincia().getId());
			List<Barrio> barriosCont = barrioService.listarBarriosPorLocalidadId(barrioSelCont.getLocalidad().getId());
			atributos.put("provinciasCont", provinciasCont);			
			atributos.put("localidadesCont", localidadesCont);			
			atributos.put("barriosCont", barriosCont);
			
			atributos.put("clienteFormulario", clienteFormulario);			
		}
		// busco los tipos de documentos
		List<TipoDocumento> tiposDocumento = tipoDocumentoService.listarTodos();
		atributos.put("tiposDocumento", tiposDocumento);
		//Seteo la accion actual
		atributos.put("accion", accion);		
		//obtengo los paises registrados en el sistema
		List<Pais> paises = paisService.listarPaises();
		Collections.sort(paises, new OrdenaPaisesPorNombrePrimeroArgentina());
		atributos.put("paises", paises); // los paso por get
		//Se realiza el redirect
		return "formularioCliente";
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
			value="/guardarActualizarCliente.html",
			method= RequestMethod.POST
		)
	public String guardarCliente(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("clienteFormulario") final ClienteAsp clienteFormulario,
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
			try{
				clienteFormulario.setAccion(accion);
				//obtengo el tipo de documento seleccionado
				TipoDocumento tipoSel = tipoDocumentoService.obtenerPorId(clienteFormulario.getIdTipoDocSel());
				clienteFormulario.getPersona().setTipoDoc(tipoSel); // seteo el mismo en la persona
			}catch(NullPointerException e){
				logger.error("Error antes de ir al validador", e);
			}
			validator.validate(clienteFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		ClienteAsp cliente;
		String clienteAsp = ""; //variable donde se almacena la abreviacion del ClienteAsp
		String username = ""; //variable donde se almacena el username para mostrar por pantalla
		String password = ""; //variable donde se almacena el password sin codificar para mostrar por pantalla
		if(!result.hasErrors()){	
			try{
				//obtengo el barrio seleccionado para el cliente
				Barrio barrioSelCliente = barrioService.obtenerPorId(clienteFormulario.getPersona().getDireccion().getIdBarrio());
				clienteFormulario.getPersona().getDireccion().setBarrio(barrioSelCliente); // seteo el mismo en la persona
				//obtengo el barrio seleccionado para el contacto
				if(barrioSelCliente.getId() == clienteFormulario.getContacto().getDireccion().getIdBarrio())
					clienteFormulario.getContacto().getDireccion().setBarrio(barrioSelCliente);
				else{
					Barrio barrioSelContacto = barrioService.obtenerPorId(clienteFormulario.getContacto().getDireccion().getIdBarrio());
					clienteFormulario.getContacto().getDireccion().setBarrio(barrioSelContacto); // seteo el mismo en el contacto
				}
			}catch(NullPointerException e){
				logger.error("Error en el bloque de los barrios", e);
			}
			if(accion.equals("NUEVO")){
				
					cliente = clienteFormulario;
				try{
					cliente.setHabilitado(true);
					//crear user	
					Set<Group> groupsUser = new TreeSet<Group>();
					//Creo el grupo Administradores
					groupsUser.add(formGroupController.crearGroup(
							cliente, 
							Constants.NOMBRE_GROUP_ADMIN, 
							new TreeSet<Authority>(authorityService.listAuthorityExceptAuthority("ROLE_ASP_ADMIN")),
							true,
							true));
					groupsUser.add(formGroupController.crearGroup(
							cliente, 
							Constants.NOMBRE_GROUP_EMPLEADOS, 
							new TreeSet<Authority>(authorityService.listarPorAuthority(new String[] {"ROLE_USUARIOS_WEB"})),
							true,
							true));				
					clienteAsp = cliente.getNombreAbreviado();
					username = cliente.getUser().getUsername(); //retengo el username para mostrar por pantalla
					//si el password es nulo o esta vacio: genero un password aleatorio
					if(cliente.getUser().getPassword() == null || "".equals(cliente.getUser().getPassword()))
						cliente.getUser().setPassword(RecursosPassword.generarPassword());
					password = cliente.getUser().getPassword(); //retengo el password para mostrar por pantalla
					cliente.setUser(formUserController.crearUsuario(
							cliente, 
							cliente.getContacto(), 
							cliente.getUser().getUsername(),
							cliente.getUser().getPassword(),
							groupsUser,
							true
							));
				}catch(NullPointerException e){
					logger.error("Error en el bloque de seteo de datos", e);
				}
				//Se crean los parametros del sistema para el cliente
//				Parameter param = new Parameter();
//				cliente.setParametros(param); // seteo el mismo en la persona
				//Se guarda el cliente en la BD
				commit = clienteAspService.guardarNuevoCliente(cliente);
			}else{
				
					cliente = clienteAspService.getByNombreAbreviado(clienteFormulario.getNombreAbreviado());
				try{
					setData(cliente, clienteFormulario);
				}catch(NullPointerException e){
					logger.error("Errore en seteo de datos antes de actualizar", e);
				}
				//Se Actualiza el cliente en la BD
				commit = clienteAspService.actualuzarCliente(cliente);
			}	
			if(commit == null || !commit)
				clienteFormulario.setId(cliente.getId());
		}
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		//Ver errores
		if(commit != null && !commit){			
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("clienteFormulario", clienteFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioCliente(accion, String.valueOf(clienteFormulario.getId()), atributos);
		}	
		if(result.hasErrors()){
			atributos.put("clienteFormulario", clienteFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", false);
			atributos.remove("avisos");		
			return precargaFormularioCliente(accion, String.valueOf(clienteFormulario.getId()), atributos);
		}else{
			//Genero las notificaciones			
			if("NUEVO".equals(accion)){				
				ScreenMessage mensajeClienteReg = new ScreenMessageImp("notif.clienteAsp.clienteRegistrado", null);
				avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
				//ClienteAsp
				List<String> clienteList = new ArrayList<String>();
				clienteList.add(clienteAsp+"   ");			
				ScreenMessage mensajeCli = new ScreenMessageImp("notif.clienteAsp.clienteContacto", clienteList);
				avisos.add(mensajeCli); //agrego el mensaje a la coleccion
				//Usuario
				List<String> usuarioList = new ArrayList<String>();
				usuarioList.add(username+"   ");			
				ScreenMessage mensajeUser = new ScreenMessageImp("notif.clienteAsp.usuarioContacto", usuarioList);
				avisos.add(mensajeUser); //agrego el mensaje a la coleccion
				//Password
				List<String> passwordsList = new ArrayList<String>();
				passwordsList.add(password+"   ");
				ScreenMessage mensajePass = new ScreenMessageImp("notif.clienteAsp.passwordContacto", passwordsList);
				avisos.add(mensajePass); //agrego el mensaje a la coleccion
			}else{
				ScreenMessage mensajeClienteReg = new ScreenMessageImp("notif.clienteAsp.clienteModificado", null);
				avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);
		}
		
//		//hacemos el redirect
		return listaClientesController.mostrarCliente(session, atributos);
	}
	
	private void setData(ClienteAsp cliente, ClienteAsp data){
		if(data != null){			
			cliente.setNombreAbreviado(data.getNombreAbreviado());
			cliente.setObservaciones(data.getObservaciones());
			//seteo los datos del cliente
			PersonaJuridica pj = (PersonaJuridica) cliente.getPersona();
			PersonaJuridica dpjData = (PersonaJuridica) data.getPersona();
			setData(pj, dpjData);
			cliente.setPersona(pj);
			//seteo los datos del contacto
			PersonaFisica pf = (PersonaFisica) cliente.getContacto();
			PersonaFisica pfData = (PersonaFisica) data.getContacto();
			setData(pf, pfData);
			cliente.setContacto(pf);			
		}
	}
	
	private void setData(PersonaJuridica p, PersonaJuridica d){
		p.setRazonSocial(d.getRazonSocial());
		p.getDireccion().setCalle(d.getDireccion().getCalle());			
		p.getDireccion().setNumero(d.getDireccion().getNumero());
		p.getDireccion().setPiso(d.getDireccion().getPiso());
		p.getDireccion().setDpto(d.getDireccion().getDpto());
		p.getDireccion().setBarrio(d.getDireccion().getBarrio());
		p.setTipoDoc(d.getTipoDoc());
		p.setNumeroDoc(d.getNumeroDoc());
		p.setTelefono(d.getTelefono());
		p.setMail(d.getMail());			
	}
	
	private void setData(PersonaFisica p, PersonaFisica d){
		p.setNombre(d.getNombre());
		p.setApellido(d.getApellido());
		p.getDireccion().setCalle(d.getDireccion().getCalle());			
		p.getDireccion().setNumero(d.getDireccion().getNumero());
		p.getDireccion().setPiso(d.getDireccion().getPiso());
		p.getDireccion().setDpto(d.getDireccion().getDpto());
		p.getDireccion().setBarrio(d.getDireccion().getBarrio());
		p.setTelefono(d.getTelefono());
		p.setMail(d.getMail());			
	}
}
