package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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

import com.dividato.configuracionGeneral.validadores.TareaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.seguridad.User;
import com.security.servicios.MailManager;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Tarea.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 * @modificado Victor Kenis (10/08/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioTarea.html",
				"/guardarActualizarTarea.html"
			}
		)
public class FormTareaController {
	private static Logger logger=Logger.getLogger(FormTareaController.class);
	private ReferenciaService referenciaService;
	private TareaValidator validator;
	private EmpresaService empresaService;
	private ListaTareasController listaTareasController;
	private MailManager mailManager;
	private UserService userService;
	
		
	/**
	 * Setea el servicio de Tarea.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase TareaImp implementa Tarea y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param referenciaService
	 */
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}

	@Autowired
	public void setListaTareasController(ListaTareasController listaTareasController) {
		this.listaTareasController = listaTareasController;
	}	
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setMailManager(MailManager mailManager){
		this.mailManager=mailManager;
	}
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService=userService;
	}
	
	@Autowired
	public void setValidator(TareaValidator validator) {
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
	 * @param idTarea parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioTarea" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioTarea.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioTarea(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,		
			@RequestParam(value="val",required=false) String val,			
			@RequestParam(value="clienteCodigo",required=false) Long ubicacionId, //uso clienteCodigo pq ya estaba implementado asi para otras pantallas
			@RequestParam(value="val", required=false) String valEmpresa,
			Map<String,Object> atributos) {
		
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			Referencia tareaFormulario;
			tareaFormulario = referenciaService.obtenerPorId(Long.valueOf(id));
			atributos.put("tareaFormulario", tareaFormulario);			
		}
	
		//Seteo la accion actual
		atributos.put("accion", accion);		
		// busco las empresas
//		List<Empresa> empresas = empresaService.listarEmpresaFiltradas(null, obtenerClienteAspEmpleado());
//		atributos.put("empresas", empresas);
//		
//		definirPopupEmpresa(atributos, valEmpresa, accion, id);
//		
//		atributos.put("clienteId", obtenerClienteAspEmpleado().getId());
	
		//Se realiza el redirect
		return "formularioTarea";
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
	 * @return "formularioTarea" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarTarea.html",
			method= RequestMethod.POST
		)
	public String guardarTarea(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("tareaFormulario") final Referencia tareaFormulario,
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
			//transporteFormulario.setAccion(accion);
			validator.validate(tareaFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Referencia tarea;
		if(!result.hasErrors()){	
			
			//obtengo la empresa seleccionada
//			Empresa empresaSel = empresaService.getByCodigo(transporteFormulario.getCodigoEmpresa(), obtenerClienteAspEmpleado());
//			transporteFormulario.setEmpresa(empresaSel);
						
			if(accion.equals("NUEVO")){
				//transporte = transporteFormulario;
							
				//Se guarda el cliente en la BD
				referenciaService.guardar(tareaFormulario);
			}else{
				//transporteFormulario.getDireccion().setBarrio(barrioSel);
				tarea = referenciaService.obtenerPorId(tareaFormulario.getId());
				boolean enviar = false;
				if(tarea.getCodigoUsuario().longValue()!=tareaFormulario.getCodigoUsuario().longValue())
					enviar = true;
				setData(tarea, tareaFormulario);
				//Se Actualiza el cliente en la BD
				referenciaService.actualizar(tarea);
				
				if(enviar){
					if(tarea.getCodigoUsuario()!=null){
						User user = userService.obtenerPorId(tarea.getCodigoUsuario());
						final String para = user.getPersona().getMail();
						final String cuerpo = "Estimado "+user.getPersona().toString()+",<br><br> usted tiene una nueva tarea asignada para la etiqueta "+tarea.getElemento().getCodigo()+"<br><br>Por favor, revise su lista de tareas.<br><br>NO RESPONDA ESTE EMAIL<br><br>Atte. La Administracion";
						new Thread(){
							public void run(){
									enviarMail(para, "BASA - AVISO: Tiene una nueva tarea asignada", cuerpo, "BASA S.A.");
							}
						}.start();
					}
				}
			}			
		
		}
		
		if(result.hasErrors()){
			atributos.put("tareaFormulario", tareaFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioTarea(accion, tareaFormulario.getId() != null ?tareaFormulario.getId().toString() :  null, null, null, null, atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeTareaReg = new ScreenMessageImp("formularioTransporte.notificacion.tareaRegistrada", null);
			avisos.add(mensajeTareaReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaTareasController.mostrarTarea(session, atributos, null);
	}
	
	private void setData(Referencia tarea, Referencia tareaF){
		if(tareaF != null){			
			tarea.setCodigoUsuario(tareaF.getCodigoUsuario());
			tarea.setDescripcionTarea(tareaF.getDescripcionTarea());
			tarea.setEstadoTarea("En Proceso");
		}
	
	}

	private ClienteAsp obtenerClienteAspEmpleado(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private void enviarMail(String para,String asunto, String cuerpo,String sistema){
		try {
			mailManager.enviar(para, asunto, cuerpo, sistema);   
		} catch (MessagingException e) {
			logger.error("error al enviar mail",e);
		} catch (IllegalStateException e){
			logger.error("error al enviar mail",e);
		}
	}
	
}

