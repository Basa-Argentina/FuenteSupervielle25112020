package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

import com.dividato.configuracionGeneral.validadores.ClienteEmpValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipCondIvaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.interfaz.BarrioService;
import com.security.accesoDatos.interfaz.LocalidadService;
import com.security.accesoDatos.interfaz.PaisService;
import com.security.accesoDatos.interfaz.ProvinciaService;
import com.security.accesoDatos.interfaz.TipoDocumentoService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipCondIva;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Direccion;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.Mes;
import com.security.modelo.general.Barrio;
import com.security.modelo.general.Pais;
import com.security.modelo.general.PersonaJuridica;
import com.security.modelo.general.Provincia;
import com.security.modelo.general.TipoDocumento;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Cliente.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 * @modificado Gonzalo Noriega (05/05/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioCliente.html",
				"/guardarActualizarCliente.html"
			}
		)
public class FormClienteController {
	private ClienteEmpService clienteEmpService;
	private ClienteEmpValidator validator;
	private FormDireccionController formDireccionController;
	private TipoDocumentoService tipoDocumentoService;
	private List<Pais> paises;
	private PaisService paisService;
	private ProvinciaService provinciaService;
	private LocalidadService localidadService;
	private BarrioService barrioService;
	private AfipCondIvaService afipCondIvaService;
	private EmpresaService empresaService;
	private ListaClienteController listaClientesController;
	private ListaPreciosService listaPreciosService;
	
		
	/**
	 * Setea el servicio de Cliente.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto ClienteEmpService.
	 * La clase ClienteEmpImp implementa ClienteEmp y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param clienteEmpService
	 */
	@Autowired
	public void setClienteEmpServicee(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}

	@Autowired
	public void setListaClientesController(ListaClienteController listaClientesController) {
		this.listaClientesController = listaClientesController;
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
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setListaPreciosService(ListaPreciosService listaPreciosService) {
		this.listaPreciosService = listaPreciosService;
	}	
	
	@Autowired
	public void setValidator(ClienteEmpValidator validator) {
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
	 * para mostrar el formulario de ClienteEmp, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioCliente" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioCliente.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioCliente(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,	
			@RequestParam(value="val",required=false) String val,
			@RequestParam(value="clienteCodigo",required=false) Long ubicacionId, 
			@RequestParam(value="val", required=false) String valEmpresa,
			Map<String,Object> atributos) {
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo
		if(accion.equals("NUEVO")){
			Pais paisDefecto = paisService.getPaisPorNombre(Constants.PAIS_DEFECTO);
			if(paisDefecto!=null)
				atributos.put("paisDefecto", paisDefecto);
			TipoDocumento tipoDocumentoDefecto = tipoDocumentoService.getTipoDocumentoPorCodigo(Constants.TIPO_DOC_CUIT);
			if(tipoDocumentoDefecto!=null)
				atributos.put("tipoDocumentoDefecto", tipoDocumentoDefecto);
			//Cargo meses
			List<Mes> meses = new ArrayList<Mes>();
			for(int i=1;i<13;i++){
				Mes mes = new Mes(i, true);
				meses.add(mes);
			}
			atributos.put("meses", meses);
		}
		Set<ListaPrecios> listasIzq=new TreeSet<ListaPrecios>(listaPreciosService.listarTodosListaFiltrados(null, obtenerClienteAspUser()));
		Set<ListaPrecios> listasDer=new TreeSet<ListaPrecios>();
		ListaPrecios listasDef=new ListaPrecios();
		
		if(!accion.equals("NUEVO")){
			ClienteEmp clienteFormulario;
			clienteFormulario = clienteEmpService.getByID(Long.valueOf(id));
			clienteFormulario.setCodigoEmpresa(clienteFormulario.getEmpresa().getCodigo());
			if(clienteFormulario!=null && clienteFormulario.getDireccion()!=null && clienteFormulario.getDireccion().getBarrio()!=null
					&& clienteFormulario.getDireccion().getBarrio().getLocalidad()!=null && clienteFormulario.getDireccion().getBarrio().getLocalidad().getProvincia()!=null
					&& clienteFormulario.getDireccion().getBarrio().getLocalidad().getProvincia().getPais()!=null)
				atributos.put("paisDefecto", clienteFormulario.getDireccion().getBarrio().getLocalidad().getProvincia().getPais());
			if(clienteFormulario!=null && clienteFormulario.getTipoDoc()!=null )
				atributos.put("tipoDocumentoDefecto", clienteFormulario.getTipoDoc());
			
//			Barrio barrioSel = clienteFormulario.getDireccion().getBarrio(); //busco el barrio
//			List<Provincia> provincias = provinciaService.listarProvinciasPorPaisId(barrioSel.getLocalidad().getProvincia().getPais().getId());
//			List<Localidad> localidades = localidadService.listarLocalidadesPorProcinciaId(barrioSel.getLocalidad().getProvincia().getId());
//			List<Barrio> barrios = barrioService.listarBarriosPorLocalidadId(barrioSel.getLocalidad().getId());
//			atributos.put("provincias", provincias);			
//			atributos.put("localidades", localidades);			
//			atributos.put("barrios", barrios);	
			atributos.put("clienteFormulario", clienteFormulario);
			listasDer.addAll(clienteFormulario.getListasPrecio());
			Iterator<ListaPrecios> iteratorIzq = listasIzq.iterator(); 
			while(iteratorIzq.hasNext()){
				ListaPrecios listaPreciosIzq = iteratorIzq.next();
				for(ListaPrecios listaPreciosDer:listasDer){
					if(listaPreciosIzq.compareTo(listaPreciosDer)==0){
						iteratorIzq.remove();
						break;
					}
				}
			}
			listasIzq.removeAll(clienteFormulario.getListasPrecio());
			
			//Armamos los meses
			if(clienteFormulario.getMesesFacturables()==null)
				clienteFormulario.setMesesFacturables("1,2,3,4,5,6,7,8,9,10,11,12");
			String[] mesesSeleccionados = clienteFormulario.getMesesFacturables().split(",");
			List<Mes> meses = new ArrayList<Mes>();
			for(int i=1;i<13;i++){
				boolean selec = false;
				if(mesesSeleccionados!=null && mesesSeleccionados.length>0){
					for(String m:mesesSeleccionados){
						Integer mi = new Integer(m);
						if(mi.intValue()==i){
							Mes mes = new Mes(i, true);
							meses.add(mes);
							selec=true;
							break;
						}
					}
				}
				if(!selec){
					Mes mes = new Mes(i, false);
					meses.add(mes);
				}
				
			}
			atributos.put("meses", meses);
			
		}
		// busco los tipos de documentos
		List<TipoDocumento> tiposDocumento = tipoDocumentoService.listarTodos();
		atributos.put("tiposDocumento", tiposDocumento);		

		//obtengo los paises registrados en el sistema
//		paises = paisService.listarPaises();
//		Collections.sort(paises, new OrdenaPaisesPorNombrePrimeroArgentina());
//		atributos.put("paises", paises); // los paso por get
		
		//Busco los Iva.
		List<AfipCondIva> afipCondIvas = afipCondIvaService.listarTodos();
		atributos.put("afipCondIvas", afipCondIvas);
		
		// busco las empresas
		List<Empresa> empresas = empresaService.listarEmpresaFiltradas(null, obtenerClienteAspEmpleado());
		atributos.put("empresas", empresas);
		
		
		atributos.put("listasIzq",listasIzq);
		atributos.put("listasDer",listasDer);
		
		
		
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
		
		definirPopupEmpresa(atributos, valEmpresa, accion, id);
		
		atributos.put("clienteId", obtenerClienteAspEmpleado().getId());
		//Se realiza el redirect
		return "formularioCliente";
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
	 * @return "formularioCliente" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarCliente.html",
			method= RequestMethod.POST
		)
	public String guardarCliente(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="listasSeleccionadas",required=false) String listasSeleccionadas,
			@RequestParam(value="listaDefecto",required=false) Long idListaDefecto,
			@ModelAttribute("clienteFormulario") final ClienteEmp clienteFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//seteamos el ClienteAsp
		//clienteFormulario.setCliente(obtenerClienteAspUser());
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			clienteFormulario.setAccion(accion);
			validator.validate(clienteFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		ClienteEmp cliente;
		if(!result.hasErrors()){
			if(listasSeleccionadas != null && !"".equals(listasSeleccionadas)){
				String[] listasId = listasSeleccionadas.split(",");
				TreeSet<ListaPrecios>lista=new TreeSet<ListaPrecios>(listaPreciosService.listarPorId(stringToLong(listasId), obtenerClienteAspUser()));
				clienteFormulario.setListasPrecio(lista);
				if(idListaDefecto != null){
					ListaPrecios listaDefecto = null;
					for(ListaPrecios listaCandidato : lista){
						if(listaCandidato.getId().equals(idListaDefecto)){
							listaDefecto = listaCandidato;
							break;
						}
					}
					clienteFormulario.setListaPreciosDefecto(listaDefecto);
				}
			}
			//obtengo el tipo de documento seleccionado
			TipoDocumento tipoSel = tipoDocumentoService.obtenerPorId(clienteFormulario.getIdTipoDocSel());
			clienteFormulario.setTipoDoc(tipoSel); // seteo el mismo en la persona
			
			Empresa empresaSel = empresaService.getByCodigo(clienteFormulario.getCodigoEmpresa(), obtenerClienteAspUser());
			clienteFormulario.setEmpresa(empresaSel); // seteo el mismo en la empresa
			
			//obtengo el barrio seleccionado
			Barrio barrioSel = barrioService.obtenerPorId(clienteFormulario.getIdBarrio());
			
			if(clienteFormulario.getIdAfipCondIva() != null){
				AfipCondIva afipCondIvaSel = afipCondIvaService.obtenerPorId(clienteFormulario.getIdAfipCondIva());			
				clienteFormulario.setAfipCondIva(afipCondIvaSel);
			}
			
			//Si no selecciona ningun mes lo pongo en cero
			if(clienteFormulario.getMesesFacturables()==null || "".equals(clienteFormulario.getMesesFacturables()))
				clienteFormulario.setMesesFacturables("0");
			
			if(accion.equals("NUEVO")){
				cliente = clienteFormulario;
				
				//crear Direccion	
				Direccion direccion = new Direccion();
				direccion = formDireccionController.crearDireccion(barrioSel, cliente.getDireccion().getCalle(), 
						cliente.getDireccion().getDpto(), cliente.getDireccion().getEdificio(), 
						cliente.getDireccion().getNumero(), cliente.getDireccion().getObservaciones(),
						cliente.getDireccion().getPiso(), 0f, 0f);
				
				cliente.setDireccion(direccion);
				
				cliente.getRazonSocial().getDireccion().setBarrio(null);
				//Se guarda el cliente en la BD
				commit = clienteEmpService.guardarCliente(cliente);
			}else{
				clienteFormulario.getDireccion().setBarrio(barrioSel);
				cliente = clienteEmpService.getByIdConDireccion(clienteFormulario.getId());
				setData(cliente, clienteFormulario);
				//Se Actualiza el cliente en la BD
				commit = clienteEmpService.actualizarCliente(cliente);
			}
			if(commit == null || !commit)
				clienteFormulario.setId(cliente.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("clienteFormulario", clienteFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioCliente(accion, clienteFormulario.getId() != null ? clienteFormulario.getId().toString() : null, null, null, null, atributos);
		}	
		
		if(result.hasErrors()){
			atributos.put("clienteFormulario", clienteFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioCliente(accion, clienteFormulario.getId() != null ? clienteFormulario.getId().toString() : null, null, null, null, atributos);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioCliente.notificacion.clienteRegistrado", null);
			avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		
//		//hacemos el redirect
//		return "redirect:mostrarCliente.html";
		return listaClientesController.mostrarCliente(session, atributos, null);
	}
	
	private void setData(ClienteEmp cliente, ClienteEmp data){
		if(data != null){			
			cliente.setCodigo(data.getCodigo());
			cliente.setNombre(data.getNombre());
			cliente.setApellido(data.getApellido());
			cliente.setHabilitado(data.getHabilitado());
			cliente.setTelefono(data.getTelefono());
			cliente.setInterno(data.getInterno());
			cliente.setFax(data.getFax());
			cliente.setEmail(data.getEmail());
			cliente.setTipoPersona(data.getTipoPersona());
			cliente.setObservaciones(data.getObservaciones());
			PersonaJuridica razonSocial = (PersonaJuridica) cliente.getRazonSocial();
			PersonaJuridica razonSocialData =(PersonaJuridica) data.getRazonSocial();
			razonSocial.setRazonSocial(razonSocialData.getRazonSocial());
			cliente.setRazonSocial(razonSocial);
			cliente.setAfipCondIva(data.getAfipCondIva());
			cliente.setTipoDoc(data.getTipoDoc());
			cliente.setNumeroDoc(data.getNumeroDoc());
			Direccion direccion = cliente.getDireccion();
			Direccion direccionData = data.getDireccion();
			direccion.setCalle(direccionData.getCalle());
			direccion.setNumero(direccionData.getNumero());
			direccion.setEdificio(direccionData.getEdificio());
			direccion.setPiso(direccionData.getPiso());
			direccion.setDpto(direccionData.getDpto());
			direccion.setBarrio(direccionData.getBarrio());
			direccion.setObservaciones(direccionData.getObservaciones());			
			cliente.setDireccion(direccion);
			cliente.setListasPrecio(data.getListasPrecio());
			cliente.setMesesFacturables(data.getMesesFacturables());
			cliente.setNotasFacturacion(data.getNotasFacturacion());
			cliente.setEmpresa(data.getEmpresa());
			cliente.setListaPreciosDefecto(data.getListaPreciosDefecto());
		}
	
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private Long[] stringToLong(String[] array){
		Long[] lArray = new Long[array.length];
		for(int i=0;i<array.length;i++)
			lArray[i] = Long.valueOf(array[i]);
		return lArray;
	}
	
	private void definirPopupPais(Map<String,Object> atributos, String val, String accion, String id){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.pais",false));		
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
				"precargaFormularioCliente.html?" +
				"accion="+accion +				
				idParam);	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("paisPopupMap", map);
	}

	private void definirPopupProvincia(Map<String,Object> atributos, String val, String accion, String id, Long paisId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioCliente.datosCliente.direccion.provincia",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.provincia",false));		
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
				"precargaFormularioCliente.html?" +
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
	
	private void definirPopupLocalidad(Map<String,Object> atributos, String val, String accion, String id, Long provinciaId){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioCliente.datosCliente.direccion.localidad",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.localidad",false));		
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
				"precargaFormularioCliente.html?" +
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
		campos.add(new CampoDisplayTag("id","formularioCliente.datosCliente.direccion.barrio",true));
		campos.add(new CampoDisplayTag("nombre","formularioCliente.datosCliente.direccion.barrio",false));		
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
				"precargaFormularioCliente.html?" +
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
				"precargaFormularioCliente.html?" +
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
