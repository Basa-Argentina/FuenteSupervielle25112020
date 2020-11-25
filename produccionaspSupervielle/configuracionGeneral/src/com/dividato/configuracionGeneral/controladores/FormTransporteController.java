package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.dividato.configuracionGeneral.validadores.TransporteValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Transporte;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Transporte.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 * @modificado Victor Kenis (10/08/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioTransporte.html",
				"/guardarActualizarTransporte.html"
			}
		)
public class FormTransporteController {
	private TransporteService transporteService;
	private TransporteValidator validator;
	private EmpresaService empresaService;
	private ListaTransporteController listaTransporteController;
	
		
	/**
	 * Setea el servicio de Transporte.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase TransporteImp implementa Transporte y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param transporteService
	 */
	@Autowired
	public void setTransporteService(TransporteService transporteService) {
		this.transporteService = transporteService;
	}

	@Autowired
	public void setListaTransportesController(ListaTransporteController listaTransportesController) {
		this.listaTransporteController = listaTransportesController;
	}	
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setValidator(TransporteValidator validator) {
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
	 * @param idTransporte parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioTransporte" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioTransporte.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioTransporte(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,		
			@RequestParam(value="val",required=false) String val,			
			@RequestParam(value="clienteCodigo",required=false) Long ubicacionId, //uso clienteCodigo pq ya estaba implementado asi para otras pantallas
			@RequestParam(value="val", required=false) String valEmpresa,
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			Transporte transporteFormulario;
			transporteFormulario = transporteService.obtenerPorId(Long.valueOf(id));
			transporteFormulario.setCodigoEmpresa(transporteFormulario.getEmpresa().getCodigo());
			
			atributos.put("transporteFormulario", transporteFormulario);			
		}
	
		//Seteo la accion actual
		atributos.put("accion", accion);		
		// busco las empresas
		List<Empresa> empresas = empresaService.listarEmpresaFiltradas(null, obtenerClienteAspEmpleado());
		atributos.put("empresas", empresas);
		
		definirPopupEmpresa(atributos, valEmpresa, accion, id);
		
		atributos.put("clienteId", obtenerClienteAspEmpleado().getId());
	
		//Se realiza el redirect
		return "formularioTransporte";
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
	 * @return "formularioTransporte" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarTransporte.html",
			method= RequestMethod.POST
		)
	public String guardarTransporte(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("transporteFormulario") final Transporte transporteFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		transporteFormulario.setClienteAsp(obtenerClienteAspEmpleado());
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			transporteFormulario.setAccion(accion);
			validator.validate(transporteFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Transporte transporte;
		if(!result.hasErrors()){	
			
			//obtengo la empresa seleccionada
			Empresa empresaSel = empresaService.getByCodigo(transporteFormulario.getCodigoEmpresa(), obtenerClienteAspEmpleado());
			transporteFormulario.setEmpresa(empresaSel);
						
			if(accion.equals("NUEVO")){
				transporte = transporteFormulario;
							
				//Se guarda el cliente en la BD
				commit = transporteService.guardarTransporte(transporte);
			}else{
				//transporteFormulario.getDireccion().setBarrio(barrioSel);
				transporte = transporteService.getByCodigo(transporteFormulario.getCodigo(), null);
				setData(transporte, transporteFormulario);
				//Se Actualiza el cliente en la BD
				commit = transporteService.actualizarTransporte(transporte);
			}			
			if(commit == null || !commit)
				transporteFormulario.setId(transporte.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("transporteFormulario", transporteFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioTransporte(accion, transporteFormulario.getId() != null ?transporteFormulario.getId().toString() :  null, null, null, null, atributos);
		}
		
		if(result.hasErrors()){
			atributos.put("transporteFormulario", transporteFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioTransporte(accion, transporteFormulario.getId() != null ?transporteFormulario.getId().toString() :  null, null, null, null, atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeTransporteReg = new ScreenMessageImp("formularioTransporte.notificacion.transporteRegistrada", null);
			avisos.add(mensajeTransporteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
		//hacemos el redirect
		return listaTransporteController.mostrarTransporte(session, atributos, null);
	}
	
	private void setData(Transporte transporte, Transporte data){
		if(data != null){			
			transporte.setCodigo(data.getCodigo());
			transporte.setDescripcion(data.getDescripcion());
			transporte.setHabilitado(data.isHabilitado());
			transporte.setPatente(data.getPatente());		
			transporte.setCapacidad(data.getCapacidad());
			transporte.setEmpresa(data.getEmpresa());
			transporte.setClienteAsp(data.getClienteAsp());
		}
	
	}
	
	private void definirPopupEmpresa(Map<String,Object> atributos, String val, String accion, String id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> empresasPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioEmpresa.datosEmpresa.codigo",false));
		campos.add(new CampoDisplayTag("razonSocial.razonSocial","formularioEmpresa.datosEmpresa.razonSocial",false));
		campos.add(new CampoDisplayTag("descripcion","formularioEmpresa.datosEmpresa.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		empresasPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		empresasPopupMap.put("coleccionPopup", empresaService.listarEmpresaPopup(val, obtenerClienteAspEmpleado()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		empresasPopupMap.put("referenciaHtml", "codigoEmpresa");
		//url que se debe consumir con ajax
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		empresasPopupMap.put("urlRequest", 
				"precargaFormularioTransporte.html?" +
				"accion="+accion +				
				idParam);
		//se vuelve a setear el texto utilizado para el filtrado
		empresasPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		empresasPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("empresasPopupMap", empresasPopupMap);
	}

	private ClienteAsp obtenerClienteAspEmpleado(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
}

