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

import com.dividato.configuracionGeneral.validadores.EmpleadoBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Empleado.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega
 *
 */
@Controller
@RequestMapping(
		value=
			{	
				"/iniciarEmpleado.html",
				"/mostrarEmpleado.html",
				"/eliminarEmpleado.html",
				"/filtrarEmpleado.html",
				"/habilitarEmpleado.html",
				"/desHabilitarEmpleado.html"
			}
		)
public class ListaEmpleadoController {
	
	private EmpleadoService empleadoService;
	private EmpleadoBusquedaValidator validator;
	private ClienteEmpService clienteEmpService;
	
	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setValidator(EmpleadoBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(
			value="/iniciarEmpleado.html",
			method = RequestMethod.GET
		)
	public String iniciar(HttpSession session,
			Map<String,Object> atributos){
		session.removeAttribute("empleadoBusqueda");
		return "redirect:mostrarEmpleado.html";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de Empleado y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaEmpleado" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarEmpleado.html",
			method = RequestMethod.GET
		)
	public String mostrar(HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		List<Empleado> empleados = null;	
		Empleado empleado = (Empleado) session.getAttribute("empleadoBusqueda");
		empleados =(List<Empleado>) empleadoService.listarTodosEmpleadoFiltradosByCliente(empleado,obtenerClienteAspEmpleado());		
		atributos.put("empleados", empleados);
		
		List<ClienteEmp> clientesEmp = clienteEmpService.listarTodos();
		atributos.put("clientesEmp", clientesEmp);
		User usuario = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String codigoEmpresa = ((PersonaFisica)usuario.getPersona()).getEmpresaDefecto().getCodigo();
		atributos.put("codigoEmpresa", codigoEmpresa);
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);
		//hacemos el forward
		return "consultaEmpleado";
	}
	
	@RequestMapping(
			value="/filtrarEmpleado.html",
			method = RequestMethod.GET
		)
	public String filtrar(@ModelAttribute("empleadoBusqueda") Empleado empleado, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(empleado, result);
		if(!result.hasErrors()){
			session.setAttribute("empleadoBusqueda", empleado);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
//		return "redirect:mostrarEmpleado.html";
		return mostrar(session,atributos);
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Empleado.
	 * 
	 * @param idEmpleado el id de empleado a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarEmpleado.html",
			method = RequestMethod.GET
		)
	public String eliminar(HttpSession session,
			@RequestParam("id") Long id,
			Map<String,Object> atributos) {
	
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		
		//Busco el usuario
		Empleado empleado = empleadoService.obtenerPorId(id);
		if(empleado != null){
			//eliminamos empleado
			commit = empleadoService.delete(id);
			ScreenMessage mensaje;
			if(commit){
				mensaje = new ScreenMessageImp("notif.empleado.eliminado", null);
				hayAvisos = true;
			}else{
				mensaje = new ScreenMessageImp("error.commitDataBase", null);
				hayAvisosNeg = true;
			}
			avisos.add(mensaje);
		}
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrar(session,atributos);
	}
	
	@RequestMapping(
			value="/habilitarEmpleado.html",
			method = RequestMethod.GET
		)
	public String habilitarEmpleado(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
	
		Empleado empleado = empleadoService.obtenerPorId(Long.valueOf(id));
		empleado.setEnable(true);
		empleado.setHabilitado(true);
		empleadoService.actualizar(empleado);
		return "redirect:mostrarEmpleado.html";
	}
	
	@RequestMapping(
			value="/desHabilitarEmpleado.html",
			method = RequestMethod.GET
		)
	public String desHabilitarEmpleado(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
	
		Empleado empleado = empleadoService.obtenerPorId(Long.valueOf(id));
		empleado.setEnable(false);
		empleado.setHabilitado(false);
		empleadoService.actualizar(empleado);
		return "redirect:mostrarEmpleado.html";
	}
	
	private ClienteAsp obtenerClienteAspEmpleado(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}