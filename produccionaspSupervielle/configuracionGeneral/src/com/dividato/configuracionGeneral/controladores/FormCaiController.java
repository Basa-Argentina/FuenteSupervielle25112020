package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
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

import com.dividato.configuracionGeneral.validadores.CaiValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.CaiService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.modelo.configuraciongeneral.Cai;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Cai.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 * @modificado Gonzalo Noriega (08/06/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioCai.html",
				"/guardarActualizarCai.html",
				"/volverFormularioSerie.html"
			}
		)
public class FormCaiController {
	private CaiService caiService;
	private CaiValidator validator;
	private FormSerieController formSerieController;
	private SerieService serieService;
	
		
	/**
	 * Setea el servicio de Cai.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase CaiImp implementa Cai y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param caiService
	 */
	@Autowired
	public void setCaiService(CaiService caiService) {
		this.caiService = caiService;
	}
	
	@Autowired
	public void setFormSerieController(FormSerieController formSerieController) {
		this.formSerieController = formSerieController;
	}
	
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	
	@Autowired
	public void setValidator(CaiValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioCai.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Cai, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioCai" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioCai.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioCai(
			@RequestParam(value="accionSerie",required=false) String accionSerie,
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,			
			Map<String,Object> atributos) {		
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			Cai caiFormulario;
			caiFormulario = caiService.obtenerPorId(Long.valueOf(id));
			
			atributos.put("caiFormulario", caiFormulario);			
		}
		//Seteo la accion actual
		atributos.put("accion", accion);
		
		//Seteo la accion de la Serie
		atributos.put("accionSerie", accionSerie);
	
		//Se realiza el redirect
		return "formularioCai";
	}
	
	@RequestMapping(
			value="/volverFormularioSerie.html",
			method = RequestMethod.GET
		)
	public String volverFormularioSerie(
			HttpSession session,	
			Map<String,Object> atributos,
			@RequestParam(value="accionSerie",required=false) String accionSerie,
			@RequestParam(value="accion",required=false) String accion) {
		String idSerie = session.getAttribute("idSerie").toString();
	
		session.removeAttribute("idSerie");
		return formSerieController.precargaFormularioSerie(accionSerie, idSerie, atributos, session);
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos User.
	 * 
	 * @param Cai user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto Cai con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioCai" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarCai.html",
			method= RequestMethod.POST
		)
	public String guardarCai(
			@RequestParam(value="accionSerie",required=false) String accionSerie,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@ModelAttribute("caiFormulario") final Cai caiFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		String idSerie = session.getAttribute("idSerie").toString();
		//obtengo la empresa seleccionada seleccionada.
		Serie serieSel = serieService.obtenerPorId(Long.valueOf(idSerie));			
		caiFormulario.setSerie(serieSel); // seteo el mismo en la serie.
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			caiFormulario.setAccion(accion);
			validator.validate(caiFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Cai cai;
		if(!result.hasErrors()){
			if(accion.equals("NUEVO")){
				cai = caiFormulario;								
				//Se guarda la cai en la BD
				commit = caiService.guardarCai(cai);
			}else{
				cai = caiService.obtenerPorId(Long.valueOf(id));
				setData(cai, caiFormulario);
				//Se Actualiza el cliente en la BD
				commit = caiService.actualizarCai(cai);
			}
			if(commit == null || !commit)
				caiFormulario.setId(cai.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("caiFormulario", caiFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioCai(accionSerie, accion, caiFormulario.getId() != null ?caiFormulario.getId().toString() : null, atributos);
		}	
		
		if(result.hasErrors()){
			atributos.put("caiFormulario", caiFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioCai(accionSerie, accion, caiFormulario.getId() != null ?caiFormulario.getId().toString() : null, atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeCaiReg = new ScreenMessageImp("formularioCai.notificacion.caiRegistrada", null);
			avisos.add(mensajeCaiReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		 session.removeAttribute("idSerie");
		return formSerieController.precargaFormularioSerie(accionSerie, idSerie, atributos, session);
	}
	
	private void setData(Cai cai, Cai data){
		if(data != null){			
			cai.setSerie(data.getSerie());
			cai.setNumero(data.getNumero());
			cai.setFechaVencimiento(data.getFechaVencimiento());
		}
	
	}

	
	
}
