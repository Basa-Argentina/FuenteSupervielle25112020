package com.dividato.security.controladores;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.security.validadores.ParameterValidator;
import com.security.accesoDatos.interfaz.ParameterService;
import com.security.modelo.seguridad.Parameter;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Parameter.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioParameter.html",
				"/guardarActualizarParameter.html"
			}
		)
public class FormParameterController {
	private ParameterService parameterService;
	private ParameterValidator validator;
	
	/**
	 * Setea el servicio de Parameter.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto ParameterService.
	 * La clase ParameterImp implementa Parameter y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param personaService
	 */
	
	
	@Autowired
	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	@Autowired
	public void setValidator(ParameterValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioParameter.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Parameter, ya sea nuevo, consulta o modificación.
	 * 
	 * @param atributos son los atributos del request
	 * @return "formularioParameter" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioParameter.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioParameter(
			@RequestParam(value="cliente",required=false) Long idCliente,	
			Map<String,Object> atributos) {
		String accion;		
		Parameter parameterFormulario=parameterService.obtenerParametros();
		if(parameterFormulario==null){
			parameterFormulario = new Parameter();
			accion=new String("NUEVO");			
		}
		else{
			accion=new String("MODIFICACION");
		}
		if(parameterFormulario.getPasswordSMTP()!= null && !parameterFormulario.getPasswordSMTP().equals("")){
			parameterFormulario.setPasswordSMTP(parameterFormulario.getPasswordSMTP());
		}

		atributos.put("parameterInsModFormulario", parameterFormulario);
		atributos.put("accion", accion);
		return "formularioParameterInsMod";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos Parameter.
	 * 
	 * @param Parameter insCost a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto Parameter con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioParameter" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarParameter.html",
			method= RequestMethod.POST
		)
	public String guardarParameter(
			@RequestParam(value="cliente",required=false) Long idCliente,
			@ModelAttribute("parameterInsModForm") Parameter parameter,
			BindingResult result,
			Map<String,Object> atributos){		
		
		if(!result.hasErrors()){
			validator.validate(parameter,result);
		}
		if(result.hasErrors()){
			atributos.put("parameterInsModFormulario", parameter);
			String accion;			
			if(parameterService.obtenerParametros()==null)
				accion="NUEVO";
			else{
				accion="MODIFICACION";
			}
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			return precargaFormularioParameter(idCliente, atributos);
		}else{
			atributos.put("errores", false);
			atributos.remove("result");		
		}		
		
		Parameter parameterFormulario;
		if(parameterService.obtenerParametros()==null){
			parameterFormulario = new Parameter();
		}
		else
			parameterFormulario=parameterService.obtenerParametros();

		//empezamos a setear los datos nuevos,
		parameterFormulario.setPasswordExpirationDays(parameter.getPasswordExpirationDays());
		parameterFormulario.setPasswordWarningDays(parameter.getPasswordWarningDays());
		parameterFormulario.setFailedLoginCounter(parameter.getFailedLoginCounter());
		parameterFormulario.setMinutesSanctionLogin(parameter.getMinutesSanctionLogin());
		if(parameter.isEnable())
			parameterFormulario.setEnableOldPassword(new Integer(1));
		else
			parameterFormulario.setEnableOldPassword(new Integer(0));
		//SMTP
		parameterFormulario.setHostSMTP(parameter.getHostSMTP());
		parameterFormulario.setPortSMTP(parameter.getPortSMTP());
		if(parameter.isEnableSSL())
			parameterFormulario.setEnableSSLSMTP(new Integer(1));
		else
			parameterFormulario.setEnableSSLSMTP(new Integer(0));
		parameterFormulario.setUserSMTP(parameter.getUserSMTP());
		parameterFormulario.seteMailUserSMTP(parameter.geteMailUserSMTP());
		if(parameter.getPasswordSMTP()!= null && !parameter.getPasswordSMTP().equals("")){
			parameterFormulario.setPasswordSMTP(parameter.getPasswordSMTP());
		}
		if(parameter.isEnableSPA())
			parameterFormulario.setEnableSPASMTP(new Integer(1));
		else
			parameterFormulario.setEnableSPASMTP(new Integer(0));
		
		if(parameterService.obtenerParametros()==null)
			parameterService.guardar(parameterFormulario);
		else
			parameterService.actualizar(parameterFormulario);
		
		//hacemos el redirect
		return "redirect:menu.html";
	}
}
