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

import com.dividato.configuracionGeneral.validadores.TipoElementoValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.Constantes;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de TipoElemento.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega (13/06/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioTipoElemento.html",
				"/guardarActualizarTipoElemento.html"
			}
		)
public class FormTipoElementoController {
	private TipoElementoService tipoElementoService;
	private TipoElementoValidator validator;
	private ListaTipoElementosController listaTipoElementosController;
	private ConceptoFacturableService conceptoFacturableService;
	
		
	/**
	 * Setea el servicio de TipoElemento.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase TipoElementoImp implementa TipoElemento y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param tipoElementoService
	 */
	
	@Autowired
	public void setConceptoFacturableService(ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
	}
	
	@Autowired
	public void setTipoElementoService(TipoElementoService tipoElementoService) {
		this.tipoElementoService = tipoElementoService;
	}

	@Autowired
	public void setListaTipoElementoController(ListaTipoElementosController listaTipoElementosController) {
		this.listaTipoElementosController = listaTipoElementosController;
	}
	
	@Autowired
	public void setValidator(TipoElementoValidator validator) {
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
	 * @return "formularioTipoElemento" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioTipoElemento.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioTipoElemento(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,	
			@RequestParam(value="val", required=false) String valConcepto,
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO";
		
		TipoElemento tipoElementoFormulario = (TipoElemento)atributos.get("tipoElementoFormulario");
		if(tipoElementoFormulario == null){
			tipoElementoFormulario = new TipoElemento();
			tipoElementoFormulario.setTipoEtiquetaInt(1);
		}
		
		if(!accion.equals("NUEVO")){			
			tipoElementoFormulario = tipoElementoService.getByID(Long.valueOf(id));
			tipoElementoFormulario.setTipoEtiquetaInt(obtenerTipoEtiquetaInt(tipoElementoFormulario.getTipoEtiqueta()));
			atributos.put("tipoElementoFormulario", tipoElementoFormulario);	
		}
		
		//cargo el tipo de etiqueta pre-seleccionado
		atributos.put("tipoEtiquetaPreseleccionado", String.valueOf(tipoElementoFormulario.getTipoEtiquetaInt()));
		
		//Seteo la accion actual
		atributos.put("accion", accion);
		
		//Preparo el popup de Conceptos por Venta
		definirPopupConceptosVenta(atributos, valConcepto);
		
		//Preparo el popup de Conceptos por Guarda
		definirPopupConceptosGuarda(atributos, valConcepto);
		
		//Preparo el popup de Conceptos por Stock
		definirPopupConceptosStock(atributos, valConcepto);
	
		//Se realiza el redirect
		return "formularioTipoElemento";
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
	 * @return "formularioTipoElemento" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarTipoElemento.html",
			method= RequestMethod.POST
		)
	public String guardarTipoElemento(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("tipoElementoFormulario") final TipoElemento tipoElementoFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//seteamos el ClienteAsp
		tipoElementoFormulario.setClienteAsp(obtenerClienteAspUser());
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			tipoElementoFormulario.setAccion(accion);
			validator.validate(tipoElementoFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		TipoElemento tipoElemento;
		if(!result.hasErrors()){			
			
			if(tipoElementoFormulario.getConceptoCodigoVenta() != null && tipoElementoFormulario.getConceptoCodigoVenta() != "")
			{
				ConceptoFacturable conceptoVenta = conceptoFacturableService.obtenerConceptoFacturablePorCodigo(tipoElementoFormulario.getConceptoCodigoVenta(), obtenerClienteAspUser());
				tipoElementoFormulario.setConceptoVenta(conceptoVenta);
			}
			
			if(tipoElementoFormulario.getConceptoCodigoGuarda() != null && tipoElementoFormulario.getConceptoCodigoGuarda() != "")
			{
				ConceptoFacturable conceptoGuarda = conceptoFacturableService.obtenerConceptoFacturablePorCodigo(tipoElementoFormulario.getConceptoCodigoGuarda(), obtenerClienteAspUser());
				tipoElementoFormulario.setConceptoGuarda(conceptoGuarda);
			}
			
			if(tipoElementoFormulario.getConceptoCodigoStock() != null && tipoElementoFormulario.getConceptoCodigoStock() != "")
			{
				ConceptoFacturable conceptoStock = conceptoFacturableService.obtenerConceptoFacturablePorCodigo(tipoElementoFormulario.getConceptoCodigoStock(), obtenerClienteAspUser());
				tipoElementoFormulario.setConceptoStock(conceptoStock);
			}
			
			tipoElementoFormulario.setTipoEtiqueta(obtenerTipoEtiqueta(tipoElementoFormulario.getTipoEtiquetaInt()));
			
			if(accion.equals("NUEVO")){
				tipoElemento = tipoElementoFormulario;
					
				//Se guarda el cliente en la BD
				commit = tipoElementoService.save(tipoElemento);
			}else{		
				tipoElemento = tipoElementoService.obtenerPorId(tipoElementoFormulario.getId());
				setData(tipoElemento, tipoElementoFormulario);
				//Se Actualiza el cliente en la BD
				commit = tipoElementoService.update(tipoElemento);
			}			
			if(commit == null || !commit)
				tipoElementoFormulario.setId(tipoElemento.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			tipoElementoFormulario.setTipoEtiqueta(obtenerTipoEtiqueta(tipoElementoFormulario.getTipoEtiquetaInt()));
			atributos.put("tipoElementoFormulario", tipoElementoFormulario);			
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioTipoElemento(accion, tipoElementoFormulario.getId() != null ? tipoElementoFormulario.getId().toString() : null, null, atributos);
		}
		
		if(result.hasErrors()){
			tipoElementoFormulario.setTipoEtiqueta(obtenerTipoEtiqueta(tipoElementoFormulario.getTipoEtiquetaInt()));
			atributos.put("tipoElementoFormulario", tipoElementoFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioTipoElemento(accion, tipoElementoFormulario.getId() != null ? tipoElementoFormulario.getId().toString() : null, null, atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeTipoElementoReg = new ScreenMessageImp("formularioTipoElemento.notificacion.tipoElementoRegistrado", null);
			avisos.add(mensajeTipoElementoReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
		return listaTipoElementosController.mostrarTipoElemento(session, atributos);
	}
	
	private void setData(TipoElemento tipoElemento, TipoElemento data){
		if(data != null){			
			tipoElemento.setCodigo(data.getCodigo());
			tipoElemento.setDescripcion(data.getDescripcion());
			tipoElemento.setPrefijoCodigo(data.getPrefijoCodigo());
			tipoElemento.setContenedor(data.getContenedor());
			tipoElemento.setContenido(data.getContenido());
			tipoElemento.setPosicionable(data.getPosicionable());
			tipoElemento.setTipoEtiqueta(data.getTipoEtiqueta());
			if(data.getGeneraConceptoVenta() != null)
			{
				tipoElemento.setGeneraConceptoVenta(data.getGeneraConceptoVenta());
				tipoElemento.setConceptoVenta(data.getConceptoVenta());
			}
			else
			{
				tipoElemento.setGeneraConceptoVenta(false);
				tipoElemento.setConceptoVenta(null);
			}
			if(data.getGeneraConceptoGuarda() != null)
			{
				tipoElemento.setGeneraConceptoGuarda(data.getGeneraConceptoGuarda());
				tipoElemento.setConceptoGuarda(data.getConceptoGuarda());
			}
			else
			{
				tipoElemento.setGeneraConceptoGuarda(false);
				tipoElemento.setConceptoGuarda(null);
			}
			if(data.getDescuentaStock() != null)
			{
				tipoElemento.setDescuentaStock(data.getDescuentaStock());
				tipoElemento.setConceptoStock(data.getConceptoStock());
			}
			else
			{
				tipoElemento.setDescuentaStock(false);
				tipoElemento.setConceptoStock(null);
			}
			if(data.getPrecintoSeguridad() != null)
			{
				tipoElemento.setPrecintoSeguridad(data.getPrecintoSeguridad());
			}
			else
			{
				tipoElemento.setPrecintoSeguridad(false);
			}
			if(data.getSeleccionaTipoTrabajo() != null)
			{
				tipoElemento.setSeleccionaTipoTrabajo(data.getSeleccionaTipoTrabajo());
			}
			else
			{
				tipoElemento.setSeleccionaTipoTrabajo(false);
			}
		}
	
	}
	
	private void definirPopupConceptosVenta(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> conceptosVentaPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.concepto.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioClienteConcepto.concepto.descripcion",false));
		campos.add(new CampoDisplayTag("costo","formularioClienteConcepto.concepto.costo",false));
		campos.add(new CampoDisplayTag("precioBase","formularioClienteConcepto.concepto.precioBase",false));
		conceptosVentaPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		conceptosVentaPopupMap.put("coleccionPopup", conceptoFacturableService.listarConceptosFacturablesPopup(val, obtenerClienteAspUser(),-1,0));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosVentaPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosVentaPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		conceptosVentaPopupMap.put("referenciaHtml", "conceptoCodigoVenta"); 		
		//url que se debe consumir con ajax
		conceptosVentaPopupMap.put("urlRequest", "precargaFormularioTipoElemento.html");
		//se vuelve a setear el texto utilizado para el filtrado
		conceptosVentaPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		conceptosVentaPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("conceptosVentaPopupMap", conceptosVentaPopupMap);
	}
	
	private void definirPopupConceptosGuarda(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> conceptosGuardaPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.concepto.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioClienteConcepto.concepto.descripcion",false));
		campos.add(new CampoDisplayTag("costo","formularioClienteConcepto.concepto.costo",false));
		campos.add(new CampoDisplayTag("precioBase","formularioClienteConcepto.concepto.precioBase",false));
		conceptosGuardaPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		conceptosGuardaPopupMap.put("coleccionPopup", conceptoFacturableService.listarConceptosFacturablesPopup(val, obtenerClienteAspUser(),-1,2));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosGuardaPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosGuardaPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		conceptosGuardaPopupMap.put("referenciaHtml", "conceptoCodigoGuarda"); 		
		//url que se debe consumir con ajax
		conceptosGuardaPopupMap.put("urlRequest", "precargaFormularioTipoElemento.html");
		//se vuelve a setear el texto utilizado para el filtrado
		conceptosGuardaPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		conceptosGuardaPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("conceptosGuardaPopupMap", conceptosGuardaPopupMap);
	}
	
	private void definirPopupConceptosStock(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> conceptosStockPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.concepto.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioClienteConcepto.concepto.descripcion",false));
		campos.add(new CampoDisplayTag("costo","formularioClienteConcepto.concepto.costo",false));
		campos.add(new CampoDisplayTag("precioBase","formularioClienteConcepto.concepto.precioBase",false));
		conceptosStockPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		conceptosStockPopupMap.put("coleccionPopup", conceptoFacturableService.listarConceptosFacturablesPopup(val, obtenerClienteAspUser(),1,0));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosStockPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosStockPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		conceptosStockPopupMap.put("referenciaHtml", "conceptoCodigoStock"); 		
		//url que se debe consumir con ajax
		conceptosStockPopupMap.put("urlRequest", "precargaFormularioTipoElemento.html");
		//se vuelve a setear el texto utilizado para el filtrado
		conceptosStockPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		conceptosStockPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("conceptosStockPopupMap", conceptosStockPopupMap);
	}
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private Integer obtenerTipoEtiquetaInt(String tipoEtiqueta){
		if(Constantes.TIPO_ELEMENTO_TIPO_ETIQUETA_ETIQUETA_CHICA.equalsIgnoreCase(tipoEtiqueta)){
			return Integer.valueOf(1);
		}else if(Constantes.TIPO_ELEMENTO_TIPO_ETIQUETA_ETIQUETA_MEDIA.equalsIgnoreCase(tipoEtiqueta)){
			return Integer.valueOf(2);
		}else{
			return Integer.valueOf(1);
		}
	}
	
	private String obtenerTipoEtiqueta(Integer tipoEtiquetaInt){
		if(tipoEtiquetaInt.intValue()==1){
			return Constantes.TIPO_ELEMENTO_TIPO_ETIQUETA_ETIQUETA_CHICA;
		}else if(tipoEtiquetaInt.intValue()==2){
			return Constantes.TIPO_ELEMENTO_TIPO_ETIQUETA_ETIQUETA_MEDIA;
		}else{
			return Constantes.TIPO_ELEMENTO_TIPO_ETIQUETA_ETIQUETA_CHICA;
		}
	}
}
