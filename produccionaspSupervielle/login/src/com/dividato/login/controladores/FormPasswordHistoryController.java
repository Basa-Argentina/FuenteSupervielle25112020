package com.dividato.login.controladores;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dividato.login.validadores.PasswordHistoryValidator;
import com.security.accesoDatos.interfaz.ParameterService;
import com.security.accesoDatos.interfaz.PasswordHistoryService;
import com.security.modelo.seguridad.Parameter;
import com.security.modelo.seguridad.PasswordHistory;
import com.security.modelo.seguridad.User;
import com.security.recursos.RecursosPassword;

/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de PasswordHistory.
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
				"/precargaFormularioPasswordHistory.html",
				"/guardarActualizarPasswordHistory.html"
			}
		)
public class FormPasswordHistoryController {

	private ParameterService parameterService;
	private PasswordHistoryService passwordHistoryService;
	private PasswordHistoryValidator validator;
	
	/**
	 * Setea el servicio de PasswordHistory.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto PasswordHistoryService.
	 * La clase PasswordHistoryImp implementa PasswordHistory y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param personaService
	 */
	@Autowired
	public void setPasswordHistoryService(PasswordHistoryService passwordHistoryService) {
		this.passwordHistoryService = passwordHistoryService;
	}
	@Autowired
	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	@Autowired
	public void setValidator(PasswordHistoryValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioPasswordHistory.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de PasswordHistory, ya sea nuevo, consulta o modificación.
	 * 
	 * @param atributos son los atributos del request
	 * @return "formularioPasswordHistory" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioPasswordHistory.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioPasswordHistory(
			Map<String,Object> atributos) {
		PasswordHistory passwordHistoryFormulario = new PasswordHistory();
		atributos.put("passwordHistoryFormulario", passwordHistoryFormulario);
		return "formularioPasswordHistory";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos PasswordHistory.
	 * 
	 * @param PasswordHistory insCost a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto PasswordHistory con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioPasswordHistory" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarPasswordHistory.html",
			method= RequestMethod.POST
		)
	public String guardarPasswordHistory(
			@ModelAttribute("passwordHistoryForm") PasswordHistory passwordHistory,
			BindingResult result,
			Map<String,Object> atributos){
		User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!result.hasErrors()){
			validator.validate(passwordHistory,result);
		}
		if(result.hasErrors()){
			atributos.put("passwordHistoryFormulario", passwordHistory);
			atributos.put("errores", true);	
			atributos.put("result", result);	
			return precargaFormularioPasswordHistory(atributos);
		}
		
		String passwordAEncriptar = passwordHistory.getOldPassword();
		org.springframework.security.providers.encoding.ShaPasswordEncoder passEnc=new org.springframework.security.providers.encoding.ShaPasswordEncoder();
		String passEncriptado=passEnc.encodePassword(passwordAEncriptar, null);
		if(!passEncriptado.equals(usuario.getPassword())){
			atributos.put("passwordHistoryFormulario", passwordHistory);
			result.rejectValue("oldPassword", "formularioPasswordHistory.errorContraseniaAnterior");
			atributos.put("errores", true);	
			atributos.put("result", result);	
			return precargaFormularioPasswordHistory(atributos);
		}
		if(!RecursosPassword.validar(passwordHistory.getNuevaContrasenia())){
			atributos.put("passwordHistoryFormulario", passwordHistory);
			result.rejectValue("nuevaContrasenia", "formularioPasswordHistory.errorPassword");
			atributos.put("errores", true);	
			atributos.put("result", result);	
			return precargaFormularioPasswordHistory(atributos);
		}
		//Validamos si esta desactivado permitir contraseñas anteriores
		Parameter parameter = parameterService.obtenerParametros();
		if(parameter==null)
			parameter = new Parameter();
		if(!passwordHistory.getNuevaContrasenia().equals("") && parameter.getEnableOldPassword().equals(0)){
			String passwordAEncriptar2 = passwordHistory.getNuevaContrasenia();
			org.springframework.security.providers.encoding.ShaPasswordEncoder passEnc2=new org.springframework.security.providers.encoding.ShaPasswordEncoder();
			String passEncriptado2=passEnc2.encodePassword(passwordAEncriptar2, null);
			if(passwordHistoryService.validarContraseniaAnterior(usuario, passEncriptado2)){
				atributos.put("passwordHistoryFormulario", passwordHistory);
				result.rejectValue("nuevaContrasenia", "formularioPasswordHistory.errorContraseniaExistente");
				atributos.put("errores", true);	
				atributos.put("result", result);	
				return precargaFormularioPasswordHistory(atributos);
			}				
		}
		PasswordHistory passwordHistoryFormulario= new PasswordHistory();

		//empezamos a setear los datos nuevos,
		passwordHistoryFormulario.setOldPassword(usuario.getPassword());
		String passwordAEncriptar2 = passwordHistory.getNuevaContrasenia();
		org.springframework.security.providers.encoding.ShaPasswordEncoder passEnc2=new org.springframework.security.providers.encoding.ShaPasswordEncoder();
		String passEncriptado2=passEnc2.encodePassword(passwordAEncriptar2, null);
		usuario.setPassword(passEncriptado2);
		usuario.setPasswordChangeDate(new Date());
		passwordHistoryFormulario.setDateChange(usuario.getPasswordChangeDate());
//		passwordHistoryFormulario.setUsername(usuario.getUsername());
		passwordHistoryFormulario.setUser(usuario);
		passwordHistoryService.guardar(passwordHistoryFormulario);
		//hacemos el redirect
		return "redirect:menu.html";
	}
}
