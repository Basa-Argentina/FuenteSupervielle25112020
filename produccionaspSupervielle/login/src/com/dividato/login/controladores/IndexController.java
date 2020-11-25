package com.dividato.login.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.security.accesoDatos.interfaz.IpBlockedService;
import com.security.accesoDatos.interfaz.ParameterService;
import com.security.accesoDatos.interfaz.PasswordHistoryService;
import com.security.accesoDatos.interfaz.UserLoginService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.constants.Constants;
import com.security.modelo.general.Mail;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.Authority;
import com.security.modelo.seguridad.IpBlocked;
import com.security.modelo.seguridad.Parameter;
import com.security.modelo.seguridad.User;
import com.security.modelo.seguridad.UserLogin;
import com.security.recursos.Configuracion;
import com.security.recursos.RecursosPassword;
import com.security.servicios.MailManager;
import com.security.utils.Constantes;

@Controller
@RequestMapping(
		{
			"/index.html",
			"/menu.html",
			"/redirect.html",
			"/redirectProxy.html",
			"/error404.html",
			"/error.html",
			"/accesoDenegado.html",
			"/login.html",
			"/enviarMailAdministrador.html",
			"/noConfigurarEmpresaSucursal.html"
		}
	)
public class IndexController {
	private static Logger logger = Logger.getLogger(IndexController.class);
	private UserLoginService userLoginService;
	private UserService userService;
	private ParameterService parameterService;
	private PasswordHistoryService passwordHistoryService;
	private IpBlockedService ipBlockedService;
	private String urlOtherUsers = "redirect:index.html";
	private MailManager mailManager;
	
	@Autowired
	public void setUserLoginService(UserLoginService userLoginService) {
		this.userLoginService = userLoginService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setIpBlockedService(IpBlockedService ipBlockedService) {
		this.ipBlockedService = ipBlockedService;
	}

	@Autowired
	public void setParameterService(ParameterService parameterService) {
		this.parameterService = parameterService;
	}
	
	@Autowired
	public void setPasswordHistoryService(PasswordHistoryService passwordHistoryService) {
		this.passwordHistoryService = passwordHistoryService;
	}
	@Autowired
	public void setMailManager(MailManager mailManager){
		this.mailManager=mailManager;
	}
	
	@RequestMapping("/index.html")
	public String index(HttpServletRequest request,HttpSession session){
		String version = request.getHeader("User-Agent");
		int ie6 = version.indexOf("MSIE 6");
		int ie5 = version.indexOf("MSIE 5");
		if(ie6 >=0 || ie5 >= 0)
			session.setAttribute("compatibilidadIE", "SI");
		else
			session.setAttribute("compatibilidadIE", "NO");
		String fecha = Configuracion.formatoFechaFormularios.format(new Date());
		session.setAttribute("fecha", fecha);		
		session.setAttribute("separador", Constants.SEPARADOR_CLIENTE_USUARIO);		
		
		return "index";
	}

	

	@RequestMapping(
			value="/menu.html",
			method = RequestMethod.GET
		)
	public String menu(
			 HttpServletRequest request,
			 Map<String,Object> atributos,
			 HttpSession session){
		User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//Escritura en Log del ingreso del usuario
		String mensaje = "El usuario "+ (usuario.getPersona()!=null?usuario.getPersona().toString():"adminASP")+" se loguea.";
		logger.error(mensaje);
		//Buscamos el rol de admistrador
		Boolean administrador = false;
		for(Authority a:usuario.obtenerAutorizaciones()){
			if("ROLE_ASP_ADMIN".equals(a.getAuthority()))
				administrador = true;
		}
		if((usuario.getPersona() != null && (((PersonaFisica)usuario.getPersona()).getEmpresaDefecto() == null || ((PersonaFisica)usuario.getPersona()).getSucursalDefecto() == null))
				&& !administrador){
			session.setAttribute("setEmpresa", null);	
			session.setAttribute("empresa", "No Definida");
			session.setAttribute("sucursal", "No Definida");
		}else{
			session.setAttribute("setEmpresa", true);
			session.setAttribute("empresa", usuario.getPersona() !=null ? ((PersonaFisica)usuario.getPersona()).getEmpresaDefecto().getDescripcion() : "No Definida");
			session.setAttribute("sucursal", usuario.getPersona() !=null ?((PersonaFisica)usuario.getPersona()).getSucursalDefecto().getDescripcion(): "No Definida");
		}
		UserLogin userLogin = new UserLogin();
		String remoteIpAddress = request.getRemoteAddr();
		userLogin.setDateLogin(new Date());
		userLogin.setIp(remoteIpAddress);
		userLogin.setState("Login");
		userLogin.setUser(usuario);
		userLogin.setApp("LOGIN");
		userLoginService.guardar(userLogin);
		session.setAttribute("lastLoginUsr", usuario.getLastLoginStr());
		usuario.setLastLogin(userLogin.getDateLogin());
		userService.actualizar(usuario);
		//Verificar dias cambio de contraseña
		Long diasL = verificarDiasContrasenia(usuario);
		if(diasL==null){
			session.setAttribute("passwordWarningDays", "");
			return "redirect:precargaFormularioPasswordHistory.html";
		}
		long dias = diasL;
		int aviso = 0;
		int diasCambio = 0;
		Parameter parameter = parameterService.obtenerParametros();
		if(parameter==null)
			parameter = new Parameter();
		aviso = parameter.getPasswordWarningDays();
		diasCambio = parameter.getPasswordExpirationDays();
		if(diasCambio == 0){
			session.setAttribute("passwordWarningDays", "");
			return urlOtherUsers;
		}
		int dif = diasCambio-aviso;
		if(dias<0){
			session.setAttribute("passwordWarningDays", 0);
			return "redirect:precargaFormularioPasswordHistory.html";
		}
			
		if(dias >= dif){
			if(dias >= diasCambio){
				session.setAttribute("passwordWarningDays", 0);
				return "redirect:precargaFormularioPasswordHistory.html";
			}
			else
				session.setAttribute("passwordWarningDays", diasCambio - dias);
		}
		else
			session.setAttribute("passwordWarningDays", "");
		return urlOtherUsers;
	}
	
	@RequestMapping("/redirect.html")
	public String redirect(
			@RequestParam(value="modulo") String modulo,
			@RequestParam(value="submodulo", required=false) String submodulo,
			Map<String,Object> atributos,
			HttpSession session
			){
		User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String fecha = String.valueOf(new Date().getTime());
		String username = usuario.getUsername();
		String securityString = fecha + "|" + username;
		try{
			securityString = RecursosPassword.encrypt(securityString);
			atributos.put("action", "/"+modulo+"/j_spring_security_check");
			atributos.put("securityString", securityString);
			atributos.put("newPage", false);
		}catch(Exception e){
			logger.fatal(e);
		}
		
		return "index";
	}
	
	@RequestMapping("/redirectProxy.html")
	public String redirectProxy(
			@RequestParam(value="modulo") String modulo,
			@RequestParam(value="submodulo", required=false) String submodulo,
			Map<String,Object> atributos,
			HttpSession session
			){
		User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = usuario.getUsername();
		String securityString = username + "|" + submodulo;
		try{
			securityString = RecursosPassword.encrypt(securityString);
			atributos.put("action", "/"+modulo+"/loginProxy.html");
			atributos.put("securityString", securityString);
			atributos.put("newPage", false);
		}catch(Exception e){
			logger.fatal(e);
		}
		
		return "index";
	}
	
	@RequestMapping("/error404.html")
	public String error404(
			Map<String, Object> atributos
			){
		if(!atributos.containsKey("message")){
			atributos.put("message","error.404");
		}
		return "error";
	}
	@RequestMapping("/error.html")
	public String error(
			Map<String, Object> atributos
			){
		if(!atributos.containsKey("message")){
			atributos.put("message","error.tryagain");
		}
		return "error";
	}
	
	@RequestMapping("/accesoDenegado.html")
	public String accesoDenegado(
			Map<String, Object> atributos
			){
		if(!atributos.containsKey("message")){
			atributos.put("message","error.accesoDenegado");
		}
		return "error";
	}
		
	private Long verificarDiasContrasenia(User user){
		return passwordHistoryService.diasCambioContrasenia(user);	
	}	
	
	@RequestMapping("/login.html")
	public String login(
			@RequestParam(value="login_error",required=false) String error,
			HttpServletRequest request
			){		
		if(error!=null){
			UserLogin userLogin = new UserLogin();
			String remoteIpAddress = request.getRemoteAddr();
			userLogin.setDateLogin(new Date());
			userLogin.setIp(remoteIpAddress);
			userLogin.setState("Error");
			userLoginService.guardar(userLogin);
			if(validarIp(remoteIpAddress)){
				IpBlocked ipBlocked = new IpBlocked();
				ipBlocked.setIp(remoteIpAddress);
				ipBlocked.setTimeBlocked(new Date());
				ipBlockedService.guardar(ipBlocked);
			}				
		}
		return "index";
	}
	
	@RequestMapping(value="/enviarMailAdministrador.html", method= RequestMethod.POST)
	public String enviarMailAdministrador(
			HttpServletRequest request,
			@ModelAttribute("mail") Mail mail){		
			try {
				User userAsp = userService.obtenerPorUsername("admin"+Constants.SEPARADOR_CLIENTE_USUARIO+"asp");
				mailManager.enviar(userAsp.getPersona().getMail(), mail.getAsunto(), mail.getMensaje(),"");
			} catch (MessagingException e) {
				logger.error(e);
			} catch (Exception e){
				logger.error(e);
			}
		return "";
	}
	
	@RequestMapping(value="/noConfigurarEmpresaSucursal.html", method= RequestMethod.POST)
	public String noConfigurarEmpresaSucursal(
			HttpServletRequest request,
			Map<String,Object> atributos,
			HttpSession session){
		//el usuario tiene definida la empresa y la sucursal
		String empSess= session.getAttribute("empresa").toString();
		String sucSess= session.getAttribute("sucursal").toString();
		if(empSess != null && !"NO DEFINIDA".equals(empSess.toUpperCase())
				&& sucSess != null && !"NO DEFINIDA".equals(sucSess.toUpperCase())){
			atributos.put("setEmpresa", true);		
		}else{
			//el usuario es el administrador por defecto 
			User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if( user.getUsername().indexOf(Constantes.USER_ADMIN_DEFAULT)==0 ){
				atributos.put("setEmpresa", true);
			}else{
				//el usuario no es administrador y no tiene seteada la empresa ni la sucursal
				atributos.remove("setEmpresa");
			}
		}
		
		return "index";
	}
	
	
	
	private boolean validarIp(String remoteIpAddress){
		Parameter parameter = parameterService.obtenerParametros();
		if(parameter==null)
			parameter = new Parameter();
		if(parameter.getFailedLoginCounter().intValue()==0)
			return false;
		ArrayList<UserLogin> userLogins = (ArrayList<UserLogin>) userLoginService.listarPorIps(remoteIpAddress);
		if(userLogins.size()>0){
			int cont = 0;
			Iterator<UserLogin> iteUsers = userLogins.iterator();
			while(iteUsers.hasNext()){
				UserLogin userLogin2 = iteUsers.next();
				if(userLogin2.getState().equals("Error"))
					cont++;
				if(userLogin2.getState().equals("Login") && parameter.getFailedLoginCounter() >= cont)
					return false;
				if(cont >= parameter.getFailedLoginCounter())
					return true;					
			}
		}
		return false;
	}
}
