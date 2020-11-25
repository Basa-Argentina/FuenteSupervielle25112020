package com.dividato.security.controladores;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.security.accesoDatos.interfaz.UserLoginService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.Authority;
import com.security.modelo.seguridad.User;
import com.security.modelo.seguridad.UserLogin;
import com.security.recursos.Configuracion;

@Controller
@RequestMapping(
		{
			"/index.html",
			"/menu.html",
			"/error404.html",
			"/error.html",
			"/accesoDenegado.html",
			"/logout.html"
		}
	)
public class IndexController {
	private UserLoginService userLoginService;
	private UserService userService;
	
	@Autowired
	public void setUserLoginService(UserLoginService userLoginService) {
		this.userLoginService = userLoginService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
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
		userLogin.setApp("SECMOD");
		userLoginService.guardar(userLogin);
		session.setAttribute("lastLoginUsr", usuario.getLastLoginStr());
		usuario.setLastLogin(userLogin.getDateLogin());
		userService.actualizar(usuario);
		
		return "redirect:index.html";
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

	@RequestMapping("/logout.html")
	public void logout(HttpSession session, HttpServletResponse response){		
		session.invalidate();
		try {
			response.sendRedirect("/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
