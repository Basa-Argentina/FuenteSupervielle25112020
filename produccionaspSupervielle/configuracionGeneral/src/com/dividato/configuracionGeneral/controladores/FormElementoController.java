package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dividato.configuracionGeneral.validadores.ElementoValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.LoteReferencia;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de User.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega (22/07/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioElemento.html",
				"/guardarActualizarElemento.html",
				"/precargaFormularioElementoSeleccionados.html",
				"/guardarActualizarElementoSeleccionados.html"
			}
		)
public class FormElementoController {
	
	private ListaElementosController listaElementoController;
	private ElementoService elementoService;
	private ElementoValidator validator;
	private ReferenciaService referenciaService;
	private ClienteEmpService clienteEmpService;
	private TipoElementoService tipoElementoService;
	private LoteReferenciaService loteReferenciaService;
	private DepositoService depositoService;
	
	/**
	 * Setea el servicio de Elemento.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto ElementoService.
	 * La clase ElementoImp implementa Elemento y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param elementoService
	 */
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	@Autowired
	public void setValidator(ElementoValidator validator) {
		this.validator = validator;
	}
	
	@Autowired
	public void setListaElementoController(ListaElementosController listaElementoController) {
		this.listaElementoController = listaElementoController;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
	
	@Autowired
	public void setTipoElementoService(TipoElementoService tipoElementoService) {
		this.tipoElementoService = tipoElementoService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setLoteReferenciaService(LoteReferenciaService loteReferenciaService) {
		this.loteReferenciaService = loteReferenciaService;
	}
	
	@Autowired
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioUser.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Elemento, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioElemento" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioElemento.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioElemento(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="val", required=false) String valCliente,
			@RequestParam(value="val", required=false) String valElemento,
			@RequestParam(value="val", required=false) String valTipoElemento,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="codigoElemento",required=false) String codigoElemento,
			@RequestParam(value="codigoTipoElemento",required=false) String codigoTipoElemento,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo,
			Map<String,Object> atributos) {
		Elemento elementoFormulario = null;
		
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo
		if(!accion.equals("NUEVO")){
			
			elementoFormulario = elementoService.obtenerPorId(Long.valueOf(id));
			if(elementoFormulario.getClienteEmp() != null)
			{
				elementoFormulario.setCodigoCliente(elementoFormulario.getClienteEmp().getCodigo());
				elementoFormulario.setCodigoEmpresa(elementoFormulario.getClienteEmp().getEmpresa().getCodigo());
			}
			elementoFormulario.setCodigoTipoElemento(elementoFormulario.getTipoElemento().getCodigo());
			String codigoSinPrefijo = elementoFormulario.getCodigo().substring(2);
			elementoFormulario.setCodigoSinPrefijo(codigoSinPrefijo);
			if(elementoFormulario.getContenedor() != null)
				elementoFormulario.setCodigoElemento(elementoFormulario.getContenedor().getCodigo().toString());
			
			atributos.put("elementoFormulario", elementoFormulario);			
		}
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		
		atributos.put("accion", accion);
		
		return "formularioElemento";
	}
	
	@RequestMapping(
			value="/precargaFormularioElementoSeleccionados.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioElementoSeleccionados(
			@RequestParam(value="elementoSeleccionados",required=false) String elementoSeleccionados,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request) {
		
		session.removeAttribute("elementos");
		
		// Cargar la lista con los elementos seleccionados
		List<Elemento> elementos = new ArrayList<Elemento>();
		for (String id : elementoSeleccionados.split(",")){
			elementos.add(elementoService.obtenerPorId(Long.valueOf(id)));
		}
		
		Elemento eComparador = null;
		Boolean bandera = false, contenedor = false;
//		for(Elemento e : elementos)
//		{
//			if(e.getClienteEmp()==null)
//			{
//				bandera=true;
//				break;
//			}
//			if(e.getTipoElemento().getContenedor())
//			{
//				contenedor = true;
//				break;
//			}
//			if(eComparador==null)
//			{
//				eComparador = e ;
//			}
//			else
//			{
//				if(!eComparador.getClienteEmp().getId().equals(e.getClienteEmp().getId()))
//				{
//					bandera = true;
//					break;
//				}
//			}
//		}
		String codigoCliente = null;
//		if(!elementos.isEmpty() && !bandera){
//			Elemento e = elementos.get(0);
//			codigoCliente = e.getClienteEmp().getCodigo();
//		}
		atributos.put("codigoCliente", codigoCliente);
		session.setAttribute("elementos", elementos);
		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		
		// Valido que seleccione los elementos del el mismo clienteEmp
		if(bandera || contenedor){
			BindingResult result = new BeanPropertyBindingResult(new Object(),"");
			if(bandera)
				result.addError(new FieldError(	"error.formBookingGroup.general", "formularioElemento.errorClienteDistintos", null, false, new String[] { "formularioElemento.errorClienteDistintos" }, null, "?"));
			if(contenedor)
				result.addError(new FieldError(	"error.formBookingGroup.general", "formularioElemento.errorTipoElemento", null, false, new String[] { "formularioElemento.errorClienteDistintos" }, null, "?"));
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");
			return listaElementoController.mostrarElemento(session, atributos, null, null, null, null, null,null,null, null, null,null,request);
		}else{
			return "formularioElementoSeleccionados";
		}
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos Elemento.
	 * 
	 * @param Elemento user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto Elemento con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioElemento" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarElemento.html",
			method= RequestMethod.POST
		)
	public String guardarElemento(
			@RequestParam(value="accion",required=false) String accion,
			@ModelAttribute("elementoFormulario") final Elemento elemento,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request){
		
		Boolean cambio = false;
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		
		if(!result.hasErrors()){
			elemento.setAccion(accion);
			//Setear Tipo Elemento
			TipoElemento tipoElemento = new TipoElemento();
			tipoElemento  = tipoElementoService.getByCodigo(elemento.getCodigoTipoElemento(), obtenerClienteAspUser());
			if(tipoElemento != null){
				elemento.setTipoElemento(tipoElemento);
			}
			String codigo = elemento.getTipoElemento().getPrefijoCodigo()+ elemento.getCodigo();
			elemento.setCodigo(codigo);
			String codigoSinPrefijo = elemento.getCodigo().substring(2);
			elemento.setCodigoSinPrefijo(codigoSinPrefijo);
			validator.validate(elemento,result);
		}
	
		Elemento elementoFormulario;
		if(!result.hasErrors()){
		
			if(accion.equals("NUEVO")){
				elementoFormulario = new Elemento();				
			}
			else{
				elementoFormulario = elementoService.obtenerPorId(elemento.getId());
				if(elemento.getNroPrecinto() != null)
				{
					if(elementoFormulario.getNroPrecinto()!=null)
					{
						if(!elemento.getNroPrecinto().equals(elementoFormulario.getNroPrecinto()))
						{
							elementoFormulario.setUsuarioModificacionPrecinto(obtenerUser());
							elementoFormulario.setFechaModificacionPrecinto(new Date());
							cambio = true;
						}
					}else
					{
						elementoFormulario.setUsuarioModificacionPrecinto(obtenerUser());
						elementoFormulario.setFechaModificacionPrecinto(new Date());
						cambio = true;
					}
				}
				if(elemento.getNroPrecinto1() != null)
				{
					if(elementoFormulario.getNroPrecinto1()!=null)
					{
						if(!elemento.getNroPrecinto1().equals(elementoFormulario.getNroPrecinto1()))
						{
							elementoFormulario.setUsuarioModificacionPrecinto(obtenerUser());
							elementoFormulario.setFechaModificacionPrecinto(new Date());
							cambio = true;
						}	
					}
					else
					{
						elementoFormulario.setUsuarioModificacionPrecinto(obtenerUser());
						elementoFormulario.setFechaModificacionPrecinto(new Date());
						cambio = true;
					}
				}
			}
			
			//Setear Cliente
			if(elemento.getCodigoCliente() != null && !"".equals(elemento.getCodigoCliente())){
				ClienteEmp cliente = new ClienteEmp();
				cliente.setCodigo(elemento.getCodigoCliente());
				cliente = clienteEmpService.getByCodigo(cliente, obtenerClienteAspUser());
				if(cliente != null){
					elemento.setClienteEmp(cliente);
				}
			}
			else
			{elemento.setClienteEmp(null);}
			
			//Setear Contenedor
			if(elemento.getCodigoContenedor() != null && !"".equals(elemento.getCodigoContenedor())){
				Elemento contenedor = new Elemento();
				contenedor = elementoService.getByCodigo(elemento.getCodigoContenedor(), obtenerClienteAspUser());
				if(contenedor != null){
					elemento.setContenedor(contenedor);
				}
			}else
			{
				elemento.setContenedor(null);
			}
			
			//Setear estado
			if("Creado".equalsIgnoreCase(elemento.getEstado())){
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_CREADO);		
			}else if("En el Cliente".equalsIgnoreCase(elemento.getEstado())){
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE);
			}else if("En Guarda".equalsIgnoreCase(elemento.getEstado())){
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
			}else if("En Transito".equalsIgnoreCase(elemento.getEstado())){
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_EN_TRANSITO);
			}else if("En Consulta".equalsIgnoreCase(elemento.getEstado())){
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_EN_CONSULTA);
			}else if("Unificado".equalsIgnoreCase(elemento.getEstado())){
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_UNIFICADO);
			}else if ("No Existe".equalsIgnoreCase(elemento.getEstado())){
				elemento.setEstado("No Existe");
			}
			else{
				//TODO verificar cual es el estado por defecto
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_EN_CONSULTA);
			}
			
			//
			//Setear Tipo Elemento
			TipoElemento tipoElemento = new TipoElemento();
			tipoElemento  = tipoElementoService.getByCodigo(elemento.getCodigoTipoElemento(), obtenerClienteAspUser());
			if(tipoElemento != null){
				elemento.setTipoElemento(tipoElemento);
			}
			
			//Setear tipo de trabajo
			if(elemento.getTipoElemento().getSeleccionaTipoTrabajo() != null && 
					elemento.getTipoElemento().getSeleccionaTipoTrabajo() == true)
			{
				if("Digitalización".equalsIgnoreCase(elemento.getTipoTrabajo())){
					elemento.setTipoTrabajo(Constantes.ELEMENTO_TIPO_TRABAJO_DIGITALIZACION);		
				}else if("Referencia en planta".equalsIgnoreCase(elemento.getTipoTrabajo())){
					elemento.setTipoTrabajo(Constantes.ELEMENTO_TIPO_TRABAJO_REFERENCIA_EN_PLANTA);
				}else if("Referencia adm. por terceros".equalsIgnoreCase(elemento.getTipoTrabajo())){
					elemento.setTipoTrabajo(Constantes.ELEMENTO_TIPO_TRABAJO_REFERENCIA_ADM_POR_TERCEROS);
				}else if("Referenciado por terceros".equalsIgnoreCase(elemento.getTipoTrabajo())){
					elemento.setTipoTrabajo(Constantes.ELEMENTO_TIPO_TRABAJO_REFERENCIADO_POR_TERCEROS);
				}else if("Sin referencia".equalsIgnoreCase(elemento.getTipoTrabajo())){
					elemento.setTipoTrabajo(Constantes.ELEMENTO_TIPO_TRABAJO_SIN_REFERENCIA);
				}else if("No especificado".equalsIgnoreCase(elemento.getTipoTrabajo())){
					elemento.setTipoTrabajo(null);
				}else{
					//TODO verificar cual es el estado por defecto
					elemento.setTipoTrabajo(null);
				}
			}
			else
			{
				elemento.setTipoTrabajo(null);
			}
			
			//empezamos a setear los datos nuevos,
			elementoFormulario.setId(elemento.getId());
			elementoFormulario.setClienteAsp(obtenerClienteAspUser());
			
			setData(elementoFormulario, elemento);
	
			if(accion.equals("NUEVO")){
				
				Elemento copia = null;
				int codigo = 0;
				List<Elemento> elementos = new ArrayList<Elemento>();
				if(elemento.getCantidad() != null){
					for(int i=0; i<elemento.getCantidad(); i++){
						if(i == 0){
							elementos.add(elementoFormulario);
							String codigoElemento = elemento.getCodigo().substring(2);
							codigo = Integer.valueOf(codigoElemento);
						}else{
							copia = elementoFormulario.clone();
							codigo++;
							String cadena = String.valueOf(codigo);
							int cantNumeros = cadena.length();
							int faltan = 10 - cantNumeros;
							for(int f = 0; f<faltan ; f++)
							{
								cadena= "0" + cadena;
							}
							cadena = elemento.getTipoElemento().getPrefijoCodigo() + cadena;
							copia.setCodigo(cadena);
							elementos.add(copia);
						}
					}
				}

				if(elementos.size() > 0){
					commit = elementoService.guardarElementoList(elementos);
				}else{
					commit = elementoService.guardarElemento(elementoFormulario);
				}
			}
			else{
				Elemento elementoAnterior = elementoService.obtenerPorId(elemento.getId());
				if(!elementoFormulario.equals(elementoAnterior))
				{
					elementoFormulario.setUsuarioModificacion(obtenerUser());
					elementoFormulario.setFechaModificacion(new Date());
				}
				commit = elementoService.actualizarElemento(elementoFormulario);
			
			}
			if(commit == null || !commit)
				elemento.setId(elementoFormulario.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("elementoFormulario", elemento);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioElemento(accion, elemento.getId()!= null ? elemento.getId().toString() : null, null, null, null, null, null, null, null, null, atributos);
		}	
		
		if(result.hasErrors()){
			atributos.put("elementoFormulario", elemento);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");
			return precargaFormularioElemento(accion, elemento.getId()!= null ? elemento.getId().toString() : null, null, null, null, null, null, null, null, null, atributos);
			
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioElemento.notificacion.elementoRegistrado", null);
			avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);	
		}
		//hacemos el redirect
		return listaElementoController.mostrarElemento(session, atributos, null, null, null, null, null,null,null, null, null,null,request);
	}
	
	@RequestMapping(
			value="/guardarActualizarElementoSeleccionados.html",
			method= RequestMethod.POST
		)
	public String guardarElementoSeleccionados(
			@RequestParam(value="hdn_guardar",required=false) Boolean guardar,
			@RequestParam(value="elementoSeleccionados",required=false) String elementoSeleccionados,
			@ModelAttribute("elementoFormulario") final Elemento elemento,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request){
		
		Boolean commit = null, commit2 = null;
		LoteReferencia lote = new LoteReferencia();
		ScreenMessage mensajeLoteReg = null;
		String [] elementos = null;
		List<Long> idElementos = new ArrayList<Long>();
		List<Referencia> listaReferencias = new ArrayList<Referencia>();
		boolean distintosTipoElementos = false;
		boolean tieneContenedor = false;
		boolean tienePosicion = false;
		if(elementoSeleccionados!=null){
			elementos = elementoSeleccionados.split(",");
		}

		if(elementos!=null)
		{
			ClienteEmp clienteEmp = null;
			if(elemento.getCodigoCliente() != null && !"".equals(elemento.getCodigoCliente())){
				clienteEmp = clienteEmpService.getByCodigo(elemento.getCodigoCliente(),elemento.getCodigoEmpresa(), obtenerClienteAspUser());
			}
			//Setear Contenedor
			Elemento contenedor = null;
			if(elemento.getCodigoContenedor() != null && !"".equals(elemento.getCodigoContenedor()))
			{
				contenedor = elementoService.getByCodigo(elemento.getCodigoContenedor(), obtenerClienteAspUser());
			}
			Deposito deposito= null;
			if(elemento.getCodigoDeposito() != null && !"".equals(elemento.getCodigoDeposito()))
			{
				deposito = depositoService.getByCodigoYSucursal(elemento.getCodigoDeposito(), elemento.getCodigoSucursal(), obtenerClienteAspUser());
			}
			// Buscar los elementos seleccionados.
			List<Elemento> elementoList = (List<Elemento>) session.getAttribute("elementos");
			for (String id : elementos){
				idElementos.add(Long.valueOf(id));
					
				for(Elemento e:elementoList)
				{
					if(Long.valueOf(id).equals(e.getId()))
					{
						if(contenedor != null){
							if(e.getTipoElemento().getContenedor())
							{
								distintosTipoElementos=true;
								break;
							}
							else
								e.setContenedor(contenedor);
						}
						if(clienteEmp!=null)
							e.setClienteEmp(clienteEmp);
						
						if(deposito!=null){
							if(e.getContenedor()!=null){
								tieneContenedor=true;
								break;
							}
							else if(e.getTipoElemento().getContenedor()){
								if(e.getPosicion()!=null)
								{
									tienePosicion=true;
									break;
								}
								else
									e.setDepositoActual(deposito);
							}
						}
						if(!elemento.getEstado().equalsIgnoreCase(""))
							e.setEstado(elemento.getEstado());
						
						e.setHabilitadoCerrar(elemento.getHabilitadoCerrar());
						
						e.setCerrado(elemento.getCerrado());
						
						if(!elemento.getTipoTrabajo().equalsIgnoreCase(""))
							e.setTipoTrabajo(elemento.getTipoTrabajo());
						
						//empezamos a setear los datos nuevos.
						e.setClienteAsp(obtenerClienteAspUser());
						e.setUsuarioModificacion(obtenerUser());
						e.setFechaModificacion(new Date());
					}
				}
				if(distintosTipoElementos){
					result.addError(new FieldError(	"error.formBookingGroup.general", "codigoTipoElemento", null, false, new String[] { "formularioElemento.errorTipoElemento" }, null, "?"));
					break;
				}
				if(tieneContenedor){
					result.addError(new FieldError(	"error.formBookingGroup.general", "codigoTipoElemento", null, false, new String[] { "formularioElemento.errorCambioDeposito" }, null, "?"));
					break;
				}
				if(tienePosicion){
					result.addError(new FieldError(	"error.formBookingGroup.general", "codigoTipoElemento", null, false, new String[] { "formularioElemento.errorCambioDeposito2" }, null, "?"));
					break;
				}
			}
			
			if(!result.hasErrors())
			{
				commit = elementoService.actualizarElementoList(elementoList);
				session.setAttribute("elementos", elementoList);
			}
			else
			{
				atributos.put("errores", true);
				atributos.put("result", result);
				atributos.put("hayAvisos", false);
				atributos.remove("avisos");	
				return "formularioElementoSeleccionados";
			}
			if(commit!=null && commit){
				//Buscamos las referencias existentes de los elementos seleccionados
				listaReferencias = referenciaService.getListaReferenciasByListaIDsElementos(idElementos);
				
				//Si existen referencias creamos un nuevo lote de Referencias y le seteamos las referencias al nuevo lote
				if(listaReferencias.size()>0)
				{
					List<Referencia> set = new ArrayList<Referencia>();
					lote = new LoteReferencia();
					lote.setClienteAsp(obtenerClienteAspUser());
					lote.setClienteEmp(elementoList.get(0).getClienteEmp());
					lote.setEmpresa(lote.getClienteEmp().getEmpresa());
					if(contenedor.getDepositoActual()!=null)
						lote.setSucursal(contenedor.getDepositoActual().getSucursal());
					else
						lote.setSucursal(obtenerSucursalUser());
					lote.setIndiceIndividual(true);
					lote.setFechaRegistro(new Date());
					for(Referencia ref : listaReferencias){
						ref.setLoteReferencia(null);
						set.add(ref);
					}
					lote.setReferencias(set);
					
					commit2 = loteReferenciaService.guardarLoteYActualizarReferencias(lote);
					
					if(commit2!=null && commit2){
						mensajeLoteReg = new ScreenMessageImp("formularioElemento.notificacion.loteReferenciaRegistrado", Arrays.asList(new String[]{lote.getCodigo().toString()}));
					}else{
						mensajeLoteReg = new ScreenMessageImp("formularioElemento.notificacion.elementosActualizados", null);
					}
				}else{
					mensajeLoteReg = new ScreenMessageImp("formularioElemento.notificacion.elementosActualizadosSinReferencias", null);
				}
			}else{
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				mensajeLoteReg = new ScreenMessageImp("formularioElemento.notificacion.elementosNOActualizados", null);
				avisos.add(mensajeLoteReg); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);	
				return "formularioElementoSeleccionados";
			}
			
		}
		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		avisos.add(mensajeLoteReg); //agrego el mensaje a la coleccion
		atributos.put("errores", false);
		atributos.remove("result");
		atributos.put("hayAvisos", true);
		atributos.put("avisos", avisos);	

		//hacemos el redirect
		if(guardar){
			return listaElementoController.mostrarElemento(session, atributos, null, null, null, null, null,null,null, null, null,null,request);
		}else{
			return "formularioElementoSeleccionados";
		}
	}
	
		/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void setData(Elemento elemento, Elemento data){
		if(data != null){			
			elemento.setCodigo(data.getCodigo());
			elemento.setEstado(data.getEstado());
			elemento.setContenedor(data.getContenedor());
			elemento.setGeneraCanonMensual(data.getGeneraCanonMensual());
			elemento.setCerrado(data.getCerrado());
			elemento.setHabilitadoCerrar(data.getHabilitadoCerrar());
			//elemento.setPosicion(data.getPosicion());
			elemento.setTipoElemento(data.getTipoElemento());
			elemento.setClienteAsp(data.getClienteAsp());
			elemento.setClienteEmp(data.getClienteEmp());
			if(elemento.getTipoElemento().getPrecintoSeguridad() != null && 
				elemento.getTipoElemento().getPrecintoSeguridad() == true){
					if(data.getNroPrecinto() != null)
					{
						elemento.setNroPrecinto(data.getNroPrecinto());
					}
					if(data.getNroPrecinto1() != null)
					{
						elemento.setNroPrecinto1(data.getNroPrecinto1());
					}
			}
			elemento.setTipoTrabajo(data.getTipoTrabajo());
		}
	
	}	
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	private Sucursal obtenerSucursalUser(){
		return ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto();
	}
}
