package com.dividato.configuracionGeneral.controladores;

import java.util.Collections;
import java.util.List;
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

import com.dividato.configuracionGeneral.validadores.DireccionValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.DireccionService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.modelo.configuraciongeneral.Direccion;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.Pais;
import com.security.utils.comparators.OrdenaPaisesPorNombrePrimeroArgentina;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de User.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 * @modificado Gonzalo Noriega (03/06/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioDireccion.html",
				"/guardarActualizarDireccion.html"
			}
		)
public class FormDireccionController {
	
	private DireccionService direccionService;
	private DireccionValidator validator;
	private BarrioService barrioService;
	private List<Pais> paises;
	private PaisService paisService;
	
	/**
	 * Setea el servicio de Direccion.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto DireccionService.
	 * La clase DireccionImp implementa Direccion y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param direccionService
	 */
	@Autowired
	public void setDireccionService(DireccionService direccionService) {
		this.direccionService = direccionService;
	}
	@Autowired
	public void setValidator(DireccionValidator validator) {
		this.validator = validator;
	}
	
	@Autowired
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	
	/**
	 * Setea el servicio de Barrio.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto DireccionService.
	 * La clase BarrrioImp implementa Barrio y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param barrioService
	 */
	@Autowired
	public void setBarrioService(BarrioService barrioService) {
		this.barrioService = barrioService;
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
	 * para mostrar el formulario de Direccion, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioDireccion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioDireccion.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioDireccion(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			Map<String,Object> atributos) {
		//if(accion==null) accion=(String) atributos.get("accion");
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			Direccion direccionFormulario;
			direccionFormulario=direccionService.obtenerPorId(Long.valueOf(id));
			atributos.put("direccionFormulario", direccionFormulario);
		}
		
		paises = paisService.listarPaises();
		Collections.sort(paises, new OrdenaPaisesPorNombrePrimeroArgentina());
		atributos.put("paises", paises); // los paso por get
		
		atributos.put("accion", accion);
		
		return "formularioDireccion";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos Direccion.
	 * 
	 * @param Direccion user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto Direccion con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioDireccion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarDireccion.html",
			method= RequestMethod.POST
		)
	public String guardarDireccion(
			@RequestParam(value="accion",required=false) String accion,
			@ModelAttribute("direccionFormulario") final Direccion direccion,
			BindingResult result,
			Map<String,Object> atributos){
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			direccion.setAccion(accion);			
			validator.validate(direccion,result);
		}
		if(result.hasErrors()){
			atributos.put("direccionFormulario", direccion);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			return precargaFormularioDireccion(accion, direccion.getId()!= null ? direccion.getId().toString() : null, atributos);
			
		}else{
			atributos.put("errores", false);
			atributos.remove("result");			
		}
	
		Direccion direccionFormulario;
		
		if(accion.equals("NUEVO")){
			direccionFormulario = new Direccion();
		}
		else
			direccionFormulario=direccionService.obtenerPorId(direccion.getId());
		
		//empezamos a setear los datos nuevos,
		direccionFormulario.setId(direccion.getId());
		
		if(direccion.getIdBarrio() != null){
			Barrio barrio = barrioService.obtenerPorId(direccion.getIdBarrio());			
			direccionFormulario.setBarrio(barrio);
		}
		
		if(direccion.getCalle() !=null && !"".equals(direccion.getCalle()))
			direccionFormulario.setCalle(direccion.getCalle());
		
		if(direccion.getNumero() !=null && !"".equals(direccion.getNumero()))
			direccionFormulario.setNumero(direccion.getNumero());
		
		if(direccion.getPiso() !=null && !"".equals(direccion.getPiso()))
			direccionFormulario.setPiso(direccion.getPiso());
		
		if(direccion.getDpto() !=null && !"".equals(direccion.getDpto()))
			direccionFormulario.setDpto(direccion.getDpto());
		
		if(direccion.getObservaciones() !=null && !"".equals(direccion.getObservaciones()))
			direccionFormulario.setObservaciones(direccion.getObservaciones());
		
		if(direccion.getEdificio() !=null && !"".equals(direccion.getEdificio()))
			direccionFormulario.setEdificio(direccion.getEdificio());
		
		if(direccion.getLatitud() !=null && !"".equals(direccion.getLatitud()))
			direccionFormulario.setLatitud(direccion.getLatitud());
		
		if(direccion.getLongitud() !=null && !"".equals(direccion.getLongitud()))
			direccionFormulario.setLongitud(direccion.getLongitud());

		if(accion.equals("NUEVO"))
			direccionService.guardar(direccionFormulario);
		else
			direccionService.actualizar(direccionFormulario);
		
		//hacemos el redirect
		return "redirect:mostrarDireccion.html";
	}
	
	/**
	 * 
	 * Metodo para setear el objeto Direccion.
	 * @param barrio
	 * @param calle
	 * @param dpto
	 * @param edificio
	 * @param numero
	 * @param observaciones
	 * @param piso
	 * @return direccion seteada
	 */
	public Direccion crearDireccion(Barrio barrio, String calle, String dpto, String edificio, String numero, String observaciones, String piso,
									Float latitud, Float longitud){
		Direccion newDireccion = new Direccion();
		newDireccion.setBarrio(barrio);
		newDireccion.setCalle(calle);
		newDireccion.setDpto(dpto);
		newDireccion.setEdificio(edificio);
		newDireccion.setNumero(numero);
		newDireccion.setObservaciones(observaciones);
		newDireccion.setPiso(piso);
		newDireccion.setLatitud(latitud);
		newDireccion.setLongitud(longitud);
		return newDireccion;
	}
}
