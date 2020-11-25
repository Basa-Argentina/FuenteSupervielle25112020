package com.dividato.configuracionGeneral.controladores;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.interfaz.UserLoginService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.Authority;
import com.security.modelo.seguridad.User;
import com.security.modelo.seguridad.UserLogin;
import com.security.recursos.Configuracion;
import com.security.recursos.RecursosPassword;
import com.security.servicios.UserDetailsService;
import com.security.utils.CampoDisplayTag;

@Controller
@RequestMapping(
		{
			"/index.html",
			"/menu.html",
			"/error404.html",
			"/error.html",
			"/accesoDenegado.html",
			"/pruebaPopup.html",
			"/logout.html",
			"/loginProxy.html"
		}
	)
public class IndexController {
	private UserLoginService userLoginService;
	private UserService userService;
	private EmpleadoService empleadoService;
	private UserDetailsService userDetailsService;
	
	@Autowired
	public void setUserLoginService(UserLoginService userLoginService) {
		this.userLoginService = userLoginService;
	}
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	
	@Autowired
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
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
		
		Empleado empleado = empleadoService.getByID(obtenerUser().getId());
		if(empleado!=null)
			session.setAttribute("empleadoSession", empleado);
		
		String link = (String) session.getAttribute("REDIRECT_TO_LINK");
		if(link != null){
			session.removeAttribute("REDIRECT_TO_LINK");
			return "redirect:/"+link+".html";
		}
		
		return "index";
	}

	@RequestMapping(value = "/loginProxy.html", 
			method = {RequestMethod.POST,RequestMethod.GET})
	public String loginProxy(
			HttpSession session, Map<String, Object> atributos,
			HttpServletRequest request,
			@RequestParam(value="sessionString",required=true)String securityString,
			HttpServletResponse response) {
		
		String cadena = "";
		
		try {
			
			cadena = RecursosPassword.decrypt(securityString);
			
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BadPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalBlockSizeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		String [] valores = cadena.split("\\|");
		
		User user = (User) userDetailsService.loadUserByUsername(valores[0]);
		 
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, "",user.getAuthorities()));
		 			
		session.setAttribute("REDIRECT_TO_LINK", valores[1]);

		return menu(request, atributos, session);
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
		userLogin.setApp("CFGMOD");
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
	
	@RequestMapping(
			value="/pruebaPopup.html",
			method = RequestMethod.GET
		)
	public String prueba(
			 @RequestParam(value="val", required=false) String val,
			 HttpServletRequest request,
			 Map<String,Object> atributos,
			 HttpSession session){	
		
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> userPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","usuarioRegistrado",true));
		campos.add(new CampoDisplayTag("username","usuarioRegistrado",false));
		campos.add(new CampoDisplayTag("persona.nombre","usuarioRegistrado",false));
		campos.add(new CampoDisplayTag("persona.apellido","usuarioRegistrado",false));
		userPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		userPopupMap.put("coleccionPopup", userService.listarPopup(val,obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		userPopupMap.put("referenciaPopup", "username");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		userPopupMap.put("referenciaPopup2", "username");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		userPopupMap.put("referenciaHtml", "usuario"); 		
		//url que se debe consumir con ajax
		userPopupMap.put("urlRequest", "pruebaPopup.html");
		//se vuelve a setear el texto utilizado para el filtrado
		userPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		userPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("userPopupMap", userPopupMap);
		
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> userPopupMap2 = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos2 = new ArrayList<CampoDisplayTag>();
		campos2.add(new CampoDisplayTag("id","usuarioRegistrado",true));
		campos2.add(new CampoDisplayTag("username","usuarioRegistrado",false));
		userPopupMap2.put("campos", campos2);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		userPopupMap2.put("coleccionPopup", userService.listarPopup(val,obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		userPopupMap2.put("referenciaPopup", "username");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		userPopupMap2.put("referenciaPopup2", "username");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		userPopupMap2.put("referenciaHtml", "usuario2"); 		
		//url que se debe consumir con ajax
		userPopupMap2.put("urlRequest", "pruebaPopup.html");
		//se vuelve a setear el texto utilizado para el filtrado
		userPopupMap2.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		userPopupMap2.put("tituloPopup", "textos.buscar");
		//Agrego el mapa a los atributos
		atributos.put("userPopupMap2", userPopupMap2);
		
		return "pruebaPopup";
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
		
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
}
