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

import com.dividato.configuracionGeneral.validadores.EmpresaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipCondIvaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.accesoDatos.interfaz.TipoDocumentoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipCondIva;
import com.security.modelo.configuraciongeneral.Direccion;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.PersonaJuridica;
import com.security.modelo.general.Provincia;
import com.security.modelo.general.TipoDocumento;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Empresa.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 * @modificado Gonzalo Noriega (05/05/2011)
 * @modificado Victor Kenis (12/08/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioEmpresa.html",
				"/guardarActualizarEmpresa.html"
			}
		)
public class FormEmpresaController {
	private EmpresaService empresaService;
	private SerieService serieService;
	private EmpresaValidator validator;
	private FormDireccionController formDireccionController;
	private TipoDocumentoService tipoDocumentoService;
	private PaisService paisService;
	private ProvinciaService provinciaService;
	private LocalidadService localidadService;
	private BarrioService barrioService;
	private AfipCondIvaService afipCondIvaService;
	private ListaEmpresasController listaEmpresasController;
	private ListaSeriesController listaSeriesController;
	
		
	/**
	 * Setea el servicio de Empresa.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase EmpresaImp implementa Empresa y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param empresaService
	 */
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@Autowired
	public void setListaEmpresasController(ListaEmpresasController listaEmpresasController) {
		this.listaEmpresasController = listaEmpresasController;
	}
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}

	@Autowired
	public void setListaSeriesController(ListaSeriesController listaSeriesController) {
		this.listaSeriesController = listaSeriesController;
	}
	@Autowired
	public void setBarrioService(BarrioService barrioService) {
		this.barrioService = barrioService;
	}
	@Autowired
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	
	@Autowired
	public void setProvinciaService(ProvinciaService provinciaService) {
		this.provinciaService = provinciaService;
	}
	@Autowired
	public void setLocalidadService(LocalidadService localidadService) {
		this.localidadService = localidadService;
	}
	
	@Autowired
	public void setTipoDocumentoService(TipoDocumentoService tipoDocumentoService) {
		this.tipoDocumentoService = tipoDocumentoService;
	}
	@Autowired
	public void setFormUserController(FormDireccionController formDireccionController) {
		this.formDireccionController = formDireccionController;
	}
	
	@Autowired
	public void setAfipCondIvaService(AfipCondIvaService afipCondIvaService) {
		this.afipCondIvaService = afipCondIvaService;
	}
	@Autowired
	public void setValidator(EmpresaValidator validator) {
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
	 * @return "formularioEmpresa" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioEmpresa.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioEmpresa(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) Long id,
			@RequestParam(value="val",required=false) String val,			
			@RequestParam(value="clienteCodigo",required=false) Long ubicacionId, //uso clienteCodigo pq ya estaba implementado asi para otras pantallas
			@RequestParam(value="id",required=false) String idEmpresa,
			Map<String,Object> atributos) {
			
			Empresa empresaFormulario = null;
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
			
			empresaFormulario = empresaService.getByID(Long.valueOf(id));
	
			atributos.put("empresaFormulario", empresaFormulario);			
		}
		// busco los tipos de documentos
		List<TipoDocumento> tiposDocumento = tipoDocumentoService.listarTodos();
		atributos.put("tiposDocumento", tiposDocumento);
		//Seteo la accion actual
		atributos.put("accion", accion);		
		//obtengo los paises registrados en el sistema
		definirPopupPais(atributos, val, accion, id);
		//si se selecciono un pais, obtengo las provincias del mismo
		definirPopupProvincia(atributos, val, accion, id, ubicacionId);
		//si se selecciono un provincia, obtengo las localidades de la misma
		definirPopupLocalidad(atributos, val, accion, id, ubicacionId);
		//si se selecciono una localidad, obtengo los barrios
		definirPopupBarrio(atributos, val, accion, ubicacionId, ubicacionId);
		//obtengo las series por defecto que usara la empresa
		definirPopupSerie(atributos, val, accion, "F", "RI", idEmpresa, empresaFormulario);
		definirPopupSerie2(atributos, val, accion, "F", "Otros", idEmpresa, empresaFormulario);
		
		//Busco los Iva.
		List<AfipCondIva> afipCondIvas = afipCondIvaService.listarTodos();
		atributos.put("afipCondIvas", afipCondIvas);
		atributos.put("clienteId", obtenerClienteAspUser().getId());
	
		//Se realiza el redirect
		return "formularioEmpresa";
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
	 * @return "formularioEmpresa" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarEmpresa.html",
			method= RequestMethod.POST
		)
	public String guardarEmpresa(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("empresaFormulario") final Empresa empresaFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//seteamos el ClienteAsp
		empresaFormulario.setCliente(obtenerClienteAspUser());
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			empresaFormulario.setAccion(accion);
			validator.validate(empresaFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Empresa empresa;
		if(!result.hasErrors()){			
			//obtengo el tipo de documento seleccionado
			TipoDocumento tipoSel = tipoDocumentoService.obtenerPorId(empresaFormulario.getIdTipoDocSel());
			empresaFormulario.setTipoDoc(tipoSel); // seteo el mismo en la persona
			
			//obtengo el barrio seleccionado
			Barrio barrioSel = barrioService.obtenerPorId(empresaFormulario.getIdBarrio());
			
			if(empresaFormulario.getIdAfipCondIva() != null){
				AfipCondIva afipCondIvaSel = afipCondIvaService.obtenerPorId(empresaFormulario.getIdAfipCondIva());			
				empresaFormulario.setAfipCondIva(afipCondIvaSel);
			}
			//obtengo la empresa seleccionada
			if(empresaFormulario.getCodigoSerie1() != null && empresaFormulario.getCodigoSerie1() != "")
			{
				Serie serie1Sel = serieService.obtenerPorCodigo(empresaFormulario.getCodigoSerie1(), obtenerClienteAspUser());
				empresaFormulario.setSerie1(serie1Sel);
			}
			if(empresaFormulario.getCodigoSerie2() != null && empresaFormulario.getCodigoSerie2() != "")
			{
				Serie serie2Sel = serieService.obtenerPorCodigo(empresaFormulario.getCodigoSerie2(), obtenerClienteAspUser());
				empresaFormulario.setSerie2(serie2Sel);
			}
			
			if(accion.equals("NUEVO")){
				empresa = empresaFormulario;
				
				//crear Direccion	
				Direccion direccion = new Direccion();
				direccion = formDireccionController.crearDireccion(barrioSel, empresa.getDireccion().getCalle(), 
						empresa.getDireccion().getDpto(), empresa.getDireccion().getEdificio(), 
						empresa.getDireccion().getNumero(), empresa.getDireccion().getObservaciones(),
						empresa.getDireccion().getPiso(), 0f, 0f);
				
				empresa.setDireccion(direccion);
				empresa.getRazonSocial().getDireccion().setBarrio(null);
				//Se guarda el cliente en la BD
				commit = empresaService.guardarEmpresa(empresa);
			}else{
				empresaFormulario.getDireccion().setBarrio(barrioSel);
				empresa = empresaService.getByCodigoConDireccion(empresaFormulario.getCodigo(), obtenerClienteAspUser());
				setData(empresa, empresaFormulario);
				//Se Actualiza el cliente en la BD
				commit = empresaService.actualizarEmpresa(empresa);
			}
			if(commit == null || !commit)
				empresaFormulario.setId(empresa.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("empresaFormulario", empresaFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioEmpresa(accion, empresaFormulario.getId() != null ? empresaFormulario.getId() : null, null, null, null,atributos);
		}	
		
		if(result.hasErrors()){
			atributos.put("empresaFormulario", empresaFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioEmpresa(accion, empresaFormulario.getId() != null ? empresaFormulario.getId() : null, null, null, null,atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeEmpresaReg = new ScreenMessageImp("formularioEmpresa.notificacion.empresaRegistrada", null);
			avisos.add(mensajeEmpresaReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
//		//hacemos el redirect
		return listaEmpresasController.mostrarEmpresa(session, atributos);
	}
	
	private void setData(Empresa empresa, Empresa data){
		if(data != null){			
			empresa.setCodigo(data.getCodigo());
			empresa.setDescripcion(data.getDescripcion());
			empresa.setPrincipal(data.getPrincipal());
			empresa.setTelefono(data.getTelefono());
			empresa.setMail(data.getMail());
			PersonaJuridica razonSocial = (PersonaJuridica) empresa.getRazonSocial();
			PersonaJuridica razonSocialData =(PersonaJuridica) data.getRazonSocial();
			razonSocial.setRazonSocial(razonSocialData.getRazonSocial());
			empresa.setRazonSocial(razonSocial);
			empresa.setAfipCondIva(data.getAfipCondIva());
			empresa.setTipoDoc(data.getTipoDoc());
			empresa.setNumeroDoc(data.getNumeroDoc());
			Direccion direccion = empresa.getDireccion();
			Direccion direccionData = data.getDireccion();
			direccion.setCalle(direccionData.getCalle());
			direccion.setNumero(direccionData.getNumero());
			direccion.setEdificio(direccionData.getEdificio());
			direccion.setPiso(direccionData.getPiso());
			direccion.setDpto(direccionData.getDpto());
			direccion.setBarrio(direccionData.getBarrio());
			direccion.setObservaciones(direccionData.getObservaciones());			
			empresa.setDireccion(direccion);
			empresa.setSerie1(data.getSerie1());
			empresa.setSerie2(data.getSerie2());
			empresa.setCliente(data.getCliente());
			empresa.setIngresosBrutos(data.getIngresosBrutos());
			empresa.setNumeroEstablecimiento(data.getNumeroEstablecimiento());
			empresa.setSedeTimbrado(data.getSedeTimbrado());
			empresa.setFechaInicioActividad(data.getFechaInicioActividad());
			
		}
	
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private void definirPopupPais(Map<String,Object> atributos, String val, String accion, Long id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpresa.datosEmpresa.direccion.pais",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio		
		map.put("coleccionPopup", paisService.listarPaisesPopup(val));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "pais"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioEmpresa.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("paisPopupMap", map);
	}

	private void definirPopupProvincia(Map<String,Object> atributos, String val, String accion, Long id, Long paisId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioEmpresa.datosEmpresa.direccion.provincia",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpresa.datosEmpresa.direccion.provincia",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio		
		map.put("coleccionPopup", (paisId != null)? provinciaService.listarProvinciasPopup(paisId, val): new ArrayList<Provincia>());
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "provincia"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioEmpresa.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//se setea el id del pais seleccionado.
		map.put("filterPopUp", paisId);
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("provinciaPopupMap", map);
	}
	
	private void definirPopupLocalidad(Map<String,Object> atributos, String val, String accion, Long id, Long provinciaId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioEmpresa.datosEmpresa.direccion.localidad",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpresa.datosEmpresa.direccion.localidad",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio		
		map.put("coleccionPopup", (provinciaId != null)? localidadService.listarLocalidadesPopup(provinciaId, val): new ArrayList<Provincia>());
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "localidad"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioEmpresa.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//se setea el id del pais seleccionado.
		map.put("filterPopUp", provinciaId);
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("localidadPopupMap", map);
	}
	
	private void definirPopupBarrio(Map<String,Object> atributos, String val, String accion, Long id, Long localidadId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioEmpresa.datosEmpresa.direccion.barrio",true));
		campos.add(new CampoDisplayTag("nombre","formularioEmpresa.datosEmpresa.direccion.barrio",false));		
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio		
		map.put("coleccionPopup", (localidadId != null)? barrioService.listarBarriosPopup(localidadId, val) : new ArrayList<Provincia>());
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "nombre");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "id");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "barrio"); 		
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		map.put("urlRequest", 
				"precargaFormularioEmpresa.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//se setea el id del pais seleccionado.
		map.put("filterPopUp", localidadId);
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("barrioPopupMap", map);
	}
	
	private void definirPopupSerie(Map<String,Object> atributos, String val, String accion, String tipoSerie, String condIvaClientes, String id, Empresa emp){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> seriesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioEmpresa.datosEmpresa.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioEmpresa.datosEmpresa.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		seriesPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		seriesPopupMap.put("coleccionPopup", serieService.listarSeriePopup(val, tipoSerie, condIvaClientes, obtenerClienteAspUser(), emp));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		seriesPopupMap.put("referenciaHtml", "codigoSerie1");
		//url que se debe consumir con ajax
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		seriesPopupMap.put("urlRequest", 
				"precargaFormularioEmpresa.html?" +
				"accion="+accion +				
				idParam);
		//se vuelve a setear el texto utilizado para el filtrado
		seriesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		seriesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("seriesPopupMap", seriesPopupMap);
	}
	
	private void definirPopupSerie2(Map<String,Object> atributos, String val, String accion, String tipoSerie, String condIvaClientes, String id, Empresa emp){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> seriesPopupMap2 = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioEmpresa.datosEmpresa.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioEmpresa.datosEmpresa.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		seriesPopupMap2.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		seriesPopupMap2.put("coleccionPopup", serieService.listarSeriePopup(val, tipoSerie, condIvaClientes, obtenerClienteAspUser(), emp));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap2.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap2.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		seriesPopupMap2.put("referenciaHtml", "codigoSerie2");
		//url que se debe consumir con ajax
		//url que se debe consumir con ajax		
		String idParam = (id != null)? "&id="+String.valueOf(id):"";
		seriesPopupMap2.put("urlRequest", 
				"precargaFormularioEmpresa.html?" +
				"accion="+accion +				
				idParam);
		//se vuelve a setear el texto utilizado para el filtrado
		seriesPopupMap2.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		seriesPopupMap2.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("seriesPopupMap2", seriesPopupMap2);
	}

}
