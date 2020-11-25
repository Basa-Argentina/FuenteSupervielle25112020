package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
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

import com.dividato.configuracionGeneral.objectForms.ImpuestoBusquedaForm;
import com.dividato.configuracionGeneral.validadores.ImpuestoFormValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ImpuestoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Impuesto;
import com.security.modelo.configuraciongeneral.ImpuestoIva;
import com.security.modelo.seguridad.User;
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
				"/precargaFormularioImpuesto.html",
				"/guardarActualizarImpuesto.html"
			}
		)
public class FormImpuestoController {
	private ImpuestoService impuestoService;
	private ImpuestoFormValidator validator;	
	private ListaImpuestosController listaImpuestosController;
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
	public void setImpuestoService(ImpuestoService impuestoService) {
		this.impuestoService = impuestoService;
	}
	@Autowired
	public void setListaImpuestosController(
			ListaImpuestosController listaImpuestosController) {
		this.listaImpuestosController = listaImpuestosController;
	}
	@Autowired
	public void setValidator(ImpuestoFormValidator validator) {
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
			value="/precargaFormularioImpuesto.html",
			method = RequestMethod.GET
		)
	public String precarga(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,			
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			Impuesto impuestoFormulario;
			impuestoFormulario = impuestoService.obtenerPorId(Long.valueOf(id));			
			atributos.put("impuestoFormulario", impuestoFormulario);			
		}	
		//Seteo la accion actual
		atributos.put("accion", accion);	
		
		//Se realiza el redirect
		return "formularioImpuesto";
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
			value="/guardarActualizarImpuesto.html",
			method= RequestMethod.POST
		)
	public String guardar(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("impuestoFormulario") final ImpuestoBusquedaForm impuestoFormulario,
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
			//seteo la accion
			impuestoFormulario.setAccion(accion);
			//obtengo el cliente
			impuestoFormulario.setCliente(obtenerClienteAspUser());
			//valido datos
			validator.validate(impuestoFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Impuesto impuesto;
		
		if(!result.hasErrors()){			
			if(accion.equals("NUEVO")){
				impuesto = new ImpuestoIva(); //TODO falta implementar un bean factory para los impuestos
				setData(impuesto, impuestoFormulario);
				//Se guarda el cliente en la BD
				commit = impuestoService.save(impuesto);
			}else{
				impuesto = impuestoService.obtenerPorId(impuestoFormulario.getId());
				setData(impuesto, impuestoFormulario);
				//Se Actualiza el cliente en la BD
				commit = impuestoService.update(impuesto);
			}			
		}
		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		//Ver errores
		if(commit != null && !commit){
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("impuestoFormulario", impuestoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precarga(accion, String.valueOf(impuestoFormulario.getId()), atributos);
		}
		if(result.hasErrors()){
			atributos.put("impuestoFormulario", impuestoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", false);
			atributos.remove("avisos");	
			return precarga(accion, String.valueOf(impuestoFormulario.getId()), atributos);
		}else{
			//Genero las notificaciones			
			if("NUEVO".equals(accion)){	
				ScreenMessage notificacion = new ScreenMessageImp("notif.impuesto.registrado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}else{
				ScreenMessage notificacion = new ScreenMessageImp("notif.impuesto.modificado", null);
				avisos.add(notificacion); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);		
		}
		//hacemos el redirect
		return listaImpuestosController.mostrar(session, atributos);
	}
	
	private void setData(Impuesto obj, Impuesto data){
		if(data != null){			
			obj.setCodigo(data.getCodigo());
			obj.setDescripcion(data.getDescripcion());
			obj.setTipo(data.getTipo());
			obj.setAlicuota(data.getAlicuota());
			obj.setCliente(data.getCliente());
		}
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
