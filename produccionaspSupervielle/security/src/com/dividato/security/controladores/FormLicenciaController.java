package com.dividato.security.controladores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.security.validadores.LicenciaValidator;
import com.security.accesoDatos.interfaz.CampoComparacion;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.accesoDatos.interfaz.EstadoLicenciaService;
import com.security.accesoDatos.interfaz.LicenciaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.administracion.EstadoLicencia;
import com.security.modelo.administracion.Licencia;
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
				"/precargaFormularioLicencia.html",
				"/guardarActualizarLicencia.html"
			}
		)
public class FormLicenciaController {
	private ClienteAspService clienteService;
	private LicenciaValidator validator;
	private LicenciaService licenciaService;
	private EstadoLicenciaService estadoLicenciaService;
	private ListaLicenciaController listaLicenciaController;
		
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
	public void setEstadoLicenciaService(EstadoLicenciaService estadoLicenciaService) {
		this.estadoLicenciaService = estadoLicenciaService;
	}
	@Autowired
	public void setLicenciaService(LicenciaService licenciaService) {
		this.licenciaService = licenciaService;
	}
	@Autowired
	public void setClienteService(ClienteAspService clienteAspService) {
		this.clienteService = clienteAspService;
	}
	@Autowired
	public void setValidator(LicenciaValidator validator) {
		this.validator = validator;
	}
	@Autowired
	public void setListaLicenciaController(
			ListaLicenciaController listaLicenciaController) {
		this.listaLicenciaController = listaLicenciaController;
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
			value="/precargaFormularioLicencia.html",
			method = RequestMethod.GET
		)
	public String precarga(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,			
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			Licencia licenciaFormulario;
			licenciaFormulario = licenciaService.obtenerPorId(Long.valueOf(id));			
			atributos.put("licenciaFormulario", licenciaFormulario);			
		}
		//busco los clientes del sistema y los cargo en session
		List<ClienteAsp> clientes = clienteService.listarTodos();
		atributos.put("clientes", clientes);
		//Seteo la accion actual
		atributos.put("accion", accion);
		//obtengo los estados
		List<EstadoLicencia> estados = estadoLicenciaService.listarTodosFiltradoPorLista(new CampoComparacion("asignable", true));
		Collections.sort(estados); //ordeno la coleccion a traves del compareTo
		atributos.put("estados", estados);
		//Se realiza el redirect
		return "formularioLicencia";
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
			value="/guardarActualizarLicencia.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("licenciaFormulario") final Licencia licenciaFormulario,
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
			licenciaFormulario.setAccion(accion);
			//obtengo el cliente
			ClienteAsp cliente = clienteService.obtenerPorId(licenciaFormulario.getIdCliente());
			licenciaFormulario.setCliente(cliente);
			validator.validateNewLicencia(licenciaFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Licencia licencia;
		
		if(!result.hasErrors()){			
			//obtengo el estado seleccionado
			EstadoLicencia nuevoEstado = estadoLicenciaService.obtenerPorId(licenciaFormulario.getEstadoId());
			licenciaFormulario.setEstado(nuevoEstado);
			if(accion.equals("NUEVO")){
				licencia = licenciaFormulario;				
				//Se guarda el cliente en la BD
				commit = licenciaService.guardarLicencia(licencia);
			}else{
				licencia = licenciaService.obtenerPorId(licenciaFormulario.getId());
				setData(licencia, licenciaFormulario);
				//Se Actualiza el cliente en la BD
				commit = licenciaService.actualizarLicencia(licencia);
			}			
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("formularioLicencia.error.Commit", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("licenciaFormulario", licenciaFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precarga(accion, String.valueOf(licenciaFormulario.getId()), atributos);
		}	
		if(result.hasErrors()){
			atributos.put("licenciaFormulario", licenciaFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", false);
			atributos.remove("avisos");	
			return precarga(accion, String.valueOf(licenciaFormulario.getId()), atributos);
		}else{
			//Genero las notificaciones
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			if("NUEVO".equals(accion)){		
				ScreenMessage mensajePass = new ScreenMessageImp("notif.licencia.registrada", null);
				avisos.add(mensajePass); //agrego el mensaje a la coleccion
			}else{
				ScreenMessage mensajeClienteReg = new ScreenMessageImp("notif.licencia.modificada", null);
				avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaLicenciaController.mostrar(session, atributos);
	}
	
	private void setData(Licencia lic, Licencia data){
		if(data != null){			
			lic.setFechaDesde(data.getFechaDesde());
			lic.setFechaHasta(data.getFechaHasta());
			lic.setEstado(data.getEstado());			
		}
	}

	
	
}
