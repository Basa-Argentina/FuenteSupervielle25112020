package com.dividato.configuracionGeneral.controladores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.mapping.Array;
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










import com.dividato.configuracionGeneral.validadores.RemitoValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.MovimientoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SecuenciaTablaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteDireccion;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.Movimiento;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
import com.security.modelo.configuraciongeneral.SecuenciaTabla;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.configuraciongeneral.Transporte;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.Constantes;
import com.security.utils.EAN13;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de User.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Victor Kenis (23/09/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/iniciarPrecargaFormularioRemito.html",
				"/precargaFormularioRemito.html",
				"/guardarActualizarRemito.html",
				"/cargarListadoCodigosElementos.html",
				"/importarLecturaElementosRemito.html"
			}
		)
public class FormRemitoController {
	
	private DepositoService depositoService;
	private RemitoService remitoService;
	private RemitoDetalleService remitoDetalleService;
	private EmpleadoService empleadoService;
	private ElementoService elementoService;
	private SerieService serieService;
	private TransporteService transporteService;
	private RemitoValidator validator;
	private EmpresaService empresaService;
	private LecturaService lecturaService;
	private LecturaDetalleService lecturaDetalleService;
	private ClienteEmpService clienteEmpService;
	private ClienteDireccionService clienteDireccionService;
	private ListaRemitosController listaRemitosController;
	private MovimientoService movimientoService;
	private SecuenciaTablaService secuenciaTablaService;
	
	
	/**
	 * Setea el servicio de Remito.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto RemitoService.
	 * La clase RemitoImp implementa Remito y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param remitoService
	 */
	
	@Autowired
	public void setListaRemitosController(ListaRemitosController listaRemitosController) {
		this.listaRemitosController = listaRemitosController;
	}
	
	@Autowired
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	@Autowired
	public void setRemitoService(RemitoService remitoService) {
		this.remitoService = remitoService;
	}
	@Autowired
	public void setRemitoDetalleService(RemitoDetalleService remitoDetalleService) {
		this.remitoDetalleService = remitoDetalleService;
	}
	@Autowired
	public void setLecturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	@Autowired
	public void setLecturaDetalleService(LecturaDetalleService lecturaDetalleService) {
		this.lecturaDetalleService = lecturaDetalleService;
	}
	@Autowired
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	@Autowired
	public void setTransporteService(TransporteService transporteService) {
		this.transporteService = transporteService;
	}
	@Autowired
	public void setMovimientoService(MovimientoService movimientoService) {
		this.movimientoService = movimientoService;
	}
	
	@Autowired
	public void setValidator(RemitoValidator validator) {
		this.validator = validator;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setClienteDireccionService(ClienteDireccionService clienteDireccionService) {
		this.clienteDireccionService = clienteDireccionService;
	}
	
	@Autowired
	public void setSecuenciaTablaService(SecuenciaTablaService secuenciaTablaService) {
		this.secuenciaTablaService = secuenciaTablaService;
	}
	
	@RequestMapping(
			value="/iniciarPrecargaFormularioRemito.html",
			method = RequestMethod.GET
		)
	public String iniciarPrecargaFormularioRemito(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion", required=false) String accion,
			@RequestParam(value="id", required=false) String id)
	{
		session.removeAttribute("remitoDetalles");
		session.removeAttribute("remitoDetallesViejos");
		session.removeAttribute("noAnexar");
		session.removeAttribute("listaNuevos");
		session.removeAttribute("listaDevolucion");
		session.removeAttribute("verificoMovHdn");
		
		return precargaFormularioRemito(session, atributos, accion, id, null, null, null, null, null, null,null, null, null, null, null);
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
	 * @return "formularioRemito" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioRemito.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioRemito(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion", required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="val", required=false) String valCliente,
			@RequestParam(value="val", required=false) String valSerie,
			@RequestParam(value="val", required=false) String valTransporte,
			@RequestParam(value="val", required=false) String valDireccion,
			@RequestParam(value="val", required=false) String valLectura,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo){
		
		Remito remitoFormulario = new Remito();
		String empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto().getCodigo();
		String sucursalDefecto = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto().getCodigo();
		List<Movimiento> listaNuevos = (List<Movimiento>) session.getAttribute("listaNuevos");
		List<Movimiento> listaDevolucion = (List<Movimiento>) session.getAttribute("listaDevolucion");
		String verificoMovHdn = "NO";
		
		if(listaNuevos==null)
			listaNuevos = new ArrayList<Movimiento>();
		if(listaDevolucion==null)
			listaDevolucion = new ArrayList<Movimiento>();
		
		@SuppressWarnings("unchecked")
		List<RemitoDetalle> remitoDetalles = (List<RemitoDetalle>) session.getAttribute("remitoDetalles");
		if(remitoDetalles == null){
			remitoDetalles = new ArrayList<RemitoDetalle>();
		}
		
		if(accion==null) {
			accion="NUEVO"; //acción por defecto: nuevo
			remitoFormulario.setCodigoEmpresa(empresaDefecto);
			remitoFormulario.setCodigoSucursal(sucursalDefecto);
			remitoFormulario.setVerificaLectura(Boolean.TRUE);
			
		}
		if(!accion.equals("NUEVO")){
			remitoFormulario = remitoService.obtenerPorId(Long.valueOf(id));
			if(remitoFormulario.getVerificaLectura()!=null && remitoFormulario.getVerificaLectura())
				verificoMovHdn = "SI";
			if(remitoFormulario.getDepositoOrigen()!=null)
				remitoFormulario.setCodigoDepositoOrigen(remitoFormulario.getDepositoOrigen().getCodigo());
			if(remitoFormulario.getDepositoDestino()!=null)
				remitoFormulario.setCodigoDepositoDestino(remitoFormulario.getDepositoDestino().getCodigo());
			if(remitoFormulario.getClienteEmp()!=null){
				remitoFormulario.setCodigoCliente(remitoFormulario.getClienteEmp().getCodigo());
				remitoFormulario.setCodigoPersonal(remitoFormulario.getEmpleado().getCodigo());
				remitoFormulario.setCodigoDireccion(remitoFormulario.getDireccion().getCodigo());
			}
			remitoFormulario.setCodigoSerie(remitoFormulario.getSerie().getCodigo());
			if(remitoFormulario.getTransporte()!=null)
				remitoFormulario.setCodigoTransporte(String.valueOf(remitoFormulario.getTransporte().getCodigo()));
			remitoFormulario.setCodigoEmpresa(remitoFormulario.getEmpresa().getCodigo());
			remitoFormulario.setCodigoSucursal(remitoFormulario.getSucursal().getCodigo());
			if(remitoDetalles == null || remitoDetalles.size()<= 0){
				remitoDetalles = remitoDetalleService.listarRemitoDetallePorRemito(remitoFormulario, obtenerClienteAspUser());
				remitoFormulario.setCantidadElementos(remitoDetalles.size());
			}
			
			if(remitoFormulario.getVerificaLectura()!=null && remitoFormulario.getVerificaLectura() 
					&& remitoFormulario.getIngresoEgreso().equalsIgnoreCase("ingreso") 
					&& remitoFormulario.getTipoRemito().equalsIgnoreCase("cliente"))
			{
				if(listaNuevos.size()==0){
					Movimiento movNuevos = new Movimiento();
					movNuevos.setRemito(remitoFormulario);
					movNuevos.setEstadoElemento("En el Cliente");
					listaNuevos.addAll(movimientoService.listarMovimientos(movNuevos, obtenerClienteAspUser()));
				}
				if(listaDevolucion.size()==0){
					Movimiento movDevolucion = new Movimiento();
					movDevolucion.setRemito(remitoFormulario);
					movDevolucion.setEstadoElemento("En Consulta");
					listaDevolucion.addAll(movimientoService.listarMovimientos(movDevolucion, obtenerClienteAspUser()));
				}
			}
			
			atributos.put("remitoFormulario", remitoFormulario);			
		}
		
		if(atributos.get("remitoFormulario") != null)
		{
			remitoFormulario = (Remito) atributos.get("remitoFormulario");
		}
		else
		{
			atributos.put("remitoFormulario", remitoFormulario);
		}
		
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		
		
		definirPopupCliente(atributos, valCliente, empresaDefecto);
		definirPopupSerie(atributos, valSerie, empresaDefecto);
		definirPopupTransporte(atributos, valCliente, empresaDefecto);
		definirPopupDepositosDestino(atributos, valDeposito, null);
		definirPopupDepositosOrigen(atributos, valDeposito, sucursalDefecto);
		definirPopupEmpleado(atributos, valDeposito, clienteCodigo);
		definirPopupDirecciones(atributos, valDireccion, clienteCodigo);
		definirPopupLecturas(atributos, valLectura);
		
		
		
		if(remitoDetalles!= null && remitoDetalles.size()>0)
		{
			remitoFormulario.setCantidadElementos(remitoDetalles.size());
			atributos.put("remitoFormulario", remitoFormulario);
			atributos.put("remitoDetalles", remitoDetalles);
			session.setAttribute("remitoDetalles", remitoDetalles);
		}
		
		session.setAttribute("listaNuevos", listaNuevos);
		session.setAttribute("listaDevolucion", listaDevolucion);
		session.setAttribute("verificoMovHdn", verificoMovHdn);
		
		atributos.put("accion",accion);
		
		
		return "formularioRemito";
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
			value="/guardarActualizarRemito.html",
			method= RequestMethod.POST
		)
	public String guardarRemito(
			@RequestParam(value="accion",required=false) String accion,
			@ModelAttribute("remitoFormulario") final Remito remito,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos, HttpServletRequest request){
		
		List<Movimiento> listaNuevos = (List<Movimiento>) session.getAttribute("listaNuevos");
		List<Movimiento> listaDevolucion = (List<Movimiento>) session.getAttribute("listaDevolucion");
		
		Long nroGyC = null;
		Long nroDev = null;
		
		@SuppressWarnings("unchecked")
		List<RemitoDetalle> remitoDetalles = (List<RemitoDetalle>) session.getAttribute("remitoDetalles");
		if(remitoDetalles == null){
			remitoDetalles = new ArrayList<RemitoDetalle>();
		}
		
		Boolean commit = null;
		Remito remitoFormulario = new Remito();
		
		if (accion == null || accion.equals("") || accion.equals("NUEVO"))
			accion = "NUEVO";
		else 
		{
			accion = "MODIFICACION";
		}
		
		if(!result.hasErrors()){
			
			if(accion.equals("MODIFICACION")){
				remitoFormulario = remitoService.obtenerPorId(remito.getId());
				Set<RemitoDetalle> remitoDetallesViejos = new HashSet<RemitoDetalle>(remitoDetalleService.listarRemitoDetallePorRemito(remitoFormulario, obtenerClienteAspUser()));
				session.setAttribute("remitoDetallesViejos", remitoDetallesViejos);
				remito.setDetallesViejos(remitoDetallesViejos);
			}
			
			remito.setAccion(accion);
			Long numero = Long.valueOf(remito.getPrefijo() + remito.getNumeroSinPrefijo());
			remito.setNumero(numero);
			remito.setClienteAsp(obtenerClienteAspUser());
			Empresa empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
			Sucursal sucursalDefecto = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto();
			User usuario = obtenerUser();
			remito.setEmpresa(empresaDefecto);
			remito.setSucursal(sucursalDefecto);
			remito.setUsuario(usuario);
			Set<RemitoDetalle> detalles = new HashSet<RemitoDetalle>(remitoDetalles);
			remito.setDetalles(detalles);
			remito.setCantidadElementos(detalles.size());
			validator.validate(remito,result);
			
		}
	
		
		if(!result.hasErrors()){
		
			//Setear estado
			if("Pendiente".equalsIgnoreCase(remito.getEstado())){
				remito.setEstado(Constantes.REMITO_ESTADO_PENDIENTE);		
			}else if("En Transito".equalsIgnoreCase(remito.getEstado())){
				remito.setEstado(Constantes.REMITO_ESTADO_EN_TRANSITO);
			}else if("Cancelado".equalsIgnoreCase(remito.getEstado())){
				remito.setEstado(Constantes.REMITO_ESTADO_CANCELADO);
			}else if("Entregado".equalsIgnoreCase(remito.getEstado())){
				remito.setEstado(Constantes.REMITO_ESTADO_ENTREGADO);
			}else{
				//TODO verificar cual es el estado por defecto
				remito.setEstado(Constantes.REMITO_ESTADO_PENDIENTE);
			}
			
			//empezamos a setear los datos nuevos,
			remitoFormulario.setId(remito.getId());
			remitoFormulario.setClienteAsp(obtenerClienteAspUser());
			
			setData(remitoFormulario, remito);
			
	
			if(accion.equals("NUEVO"))
			{
					commit = remitoService.guardarRemitoYDetalles(remitoFormulario.getDetalles(), remitoFormulario);
					
					if(commit != null && commit 
							&& remito.getVerificaLectura()!=null && remito.getVerificaLectura())
					{
						
						List<Movimiento> listaMovimientos = new ArrayList<Movimiento>();
						List<Posicion> listaPosiciones = new ArrayList<Posicion>();
						if(listaNuevos!=null && listaNuevos.size()>0)
							nroGyC = secuenciaTablaService.obtenerSecuencia(obtenerClienteAspUser(), Movimiento.class);
						if(listaDevolucion!=null && listaDevolucion.size()>0)
							nroDev = secuenciaTablaService.obtenerSecuencia(obtenerClienteAspUser(), Movimiento.class);
						
						for(int i = 0 ; i< remito.getMovAsociados().size();i++)
						{
							
							remito.getMovAsociados().get(i).setRemito(remitoFormulario);
							if(remito.getTipoRemito().equalsIgnoreCase("cliente"))
							{
								if(remito.getIngresoEgreso().equalsIgnoreCase("INGRESO"))
								{	
									remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
									remito.getMovAsociados().get(i).getElemento().setDepositoActual(remito.getDepositoDestino());
									remito.getMovAsociados().get(i).setClienteEmpOrigenDestino(remito.getClienteEmp());
									if(remito.getMovAsociados().get(i).getEstadoElemento().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE))
										remito.getMovAsociados().get(i).setCodigoCarga(nroGyC);
									if(remito.getMovAsociados().get(i).getEstadoElemento().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_CONSULTA))
										remito.getMovAsociados().get(i).setCodigoCarga(nroDev);
								}
								else if(remito.getIngresoEgreso().equalsIgnoreCase("EGRESO"))
								{
										if(remito.getMovAsociados().get(i).getEstadoElemento().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_CREADO))
											remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE);
										else	
											remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_EN_CONSULTA);
										
										if(remito.getMovAsociados().get(i).getElemento().getPosicion()!=null)
										{
											remito.getMovAsociados().get(i).getElemento().setPosicion(null);
											Posicion posicion = remito.getMovAsociados().get(i).getPosicionOrigenDestino();
											posicion.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
											listaPosiciones.add(posicion);
										}
								}
							}
							else if(remito.getTipoRemito().equalsIgnoreCase("interno"))
							{
								if(remito.getIngresoEgreso().equalsIgnoreCase("INGRESO"))
								{
									
									remito.getMovAsociados().get(i).getElemento().setDepositoActual(remito.getDepositoDestino());
									
									//Se buscan movimientos de tipo Cliente e Ingreso existentes para ese elemento
									Movimiento mov = new Movimiento();
									mov.setTipoMovimiento("INGRESO");
									mov.setClaseMovimiento("cliente");
									mov.setElemento(remito.getMovAsociados().get(i).getElemento());
									Integer movIngCliAnterior = movimientoService.contarMovimientosFiltrados(mov, obtenerClienteAspUser());
									//Si existe al menos uno es que ya ha estado en el cliente por lo cual debe estar en Guarda
									if(movIngCliAnterior!=null && movIngCliAnterior>0)
									{
										//Se setea el nuevo estado al elemento
										remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
										//HABLADO CON LUIS -Se quita de esta regla y se hace para todos
										//listaElementos.get(i).setDepositoActual(nuevoMovimiento.getDeposito());
									}
									//Sino todavia no ha ido al cliente y su estado debe ser creado
									else
									{
										//Se setea el nuevo estado al elemento
										remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_CREADO);
									}
									
								}
								else if(remito.getIngresoEgreso().equalsIgnoreCase("EGRESO"))
								{
									remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_EN_TRANSITO);
									
									if(remito.getMovAsociados().get(i).getElemento().getPosicion()!=null)
									{
										remito.getMovAsociados().get(i).getElemento().setPosicion(null);
										Posicion posicion = remito.getMovAsociados().get(i).getPosicionOrigenDestino();
										posicion.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
										listaPosiciones.add(posicion);
									}
								}
							}
							
							listaMovimientos.add(remito.getMovAsociados().get(i));
							
						}
						
						
						commit = movimientoService.actualizarMovimientoListYActualizarPosiciones(listaMovimientos, listaPosiciones);
					}
			}
			else
			{				
					Boolean noAnexar = (Boolean)session.getAttribute("noAnexar");
					List<Movimiento> movimientosEliminados = new ArrayList<Movimiento>();
					@SuppressWarnings("unchecked")
					Set<RemitoDetalle> remitoDetallesViejos = (HashSet<RemitoDetalle>)session.getAttribute("remitoDetallesViejos");
					remitoFormulario.setDetalles(remitoDetallesViejos);
					Set<RemitoDetalle> remitoDetallesNuevos = new HashSet<RemitoDetalle>(remitoDetalles);
					
					commit = remitoService.actualizarRemitoYDetalles(noAnexar, remitoDetallesNuevos, remitoFormulario);
					
					if(commit != null && commit 
							&& remito.getVerificaLectura()!=null && remito.getVerificaLectura())
					{
						//PREGUNTO SI SE AGREGARON NUEVAS ETIQUETAS AL REMITO
						if(remito.getMovAsociados()!=null && remito.getMovAsociados().size()>0)
						{
							//SI SE AGREGARON, BUSCO EL CODIGO PARA GyC YA CREADO AL CREAR EL REMITO
							if(listaNuevos!=null && listaNuevos.size()>0)
							{
								for(Movimiento mov:listaNuevos){
									if(mov.getCodigoCarga()!=null){
										nroGyC = mov.getCodigoCarga();
										break;
									}
								}
								//SI NO SE ENCONTRO EN NINGUNO ES POR QUE NO HABIA GyC y AHROA SI, POR LO QUE GENERO UNO NUEVO
								if(nroGyC==null)
									nroGyC = secuenciaTablaService.obtenerSecuencia(obtenerClienteAspUser(), Movimiento.class);
								
								remito.setNroGyC(nroGyC);
								
							}
							//SI SE AGREGARON, BUSCO EL CODIGO PARA Devoluciones YA CREADO AL CREAR EL REMITO
							if(listaDevolucion!=null && listaDevolucion.size()>0)
							{
								for(Movimiento mov:listaDevolucion){
									if(mov.getCodigoCarga()!=null){
										nroDev = mov.getCodigoCarga();
										break;
									}
								}
								//SI NO SE ENCONTRO EN NINGUNO ES POR QUE NO HABIA GyC y AHROA SI, POR LO QUE GENERO UNO NUEVO
								if(nroDev==null)
									nroDev = secuenciaTablaService.obtenerSecuencia(obtenerClienteAspUser(), Movimiento.class);
								
								remito.setNroDev(nroDev);
							}
						}
						
						commit = verificarDetallesConMovimientos(commit, remito, remitoFormulario);
						
						Iterator it = remitoDetallesViejos.iterator();
						while(it.hasNext())
						{
							if(remitoDetallesNuevos!=null && remitoDetallesNuevos.size()>0)
							{
								RemitoDetalle viejo = (RemitoDetalle) it.next();
								for(RemitoDetalle det:remitoDetallesNuevos)
								{
									if(det.getElemento().getId().longValue()==viejo.getElemento().getId().longValue())
									{
										it.remove();
									}
								}
							}
						}
						
						if(remitoDetallesViejos!=null && remitoDetallesViejos.size()>0)
						{
							Movimiento mov = new Movimiento();
							mov.setCodigoRemito(remitoFormulario.getId());
							List<Movimiento> movimientos = movimientoService.listarMovimientos(mov, obtenerClienteAspUser());
							for(RemitoDetalle detalle: remitoDetallesViejos)
							{
								for(Movimiento movi:movimientos)
								{
									if(detalle.getElemento().getId().longValue()==movi.getElemento().getId().longValue())
									{	
										movi.setRemito(null);
										movimientosEliminados.add(movi);
										break;
									}
								}	
							}
							commit = movimientoService.actualizarMovimientoListYActualizarPosiciones(movimientosEliminados, null);
						}
						
					}
			}
			if(commit == null || !commit)
				remito.setId(remitoFormulario.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("remitoFormulario", remito);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioRemito(session,atributos, accion, remito.getId()!= null ? remito.getId().toString() : null, null, null, null, null, null, null,null, null, null, null, null);
		}	
		
		if(result.hasErrors()){
			atributos.put("remitoFormulario", remito);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");
			return precargaFormularioRemito(session,atributos, accion, remito.getId()!= null ? remito.getId().toString() : null, null, null, null, null, null, null,null, null, null, null, null);
			
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioRemito.notificacion.remitoRegistrado", null);
			avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
			if(nroGyC!=null){
				ScreenMessage mensajeCodigoGyC = new ScreenMessageImp("formularioRemito.notificacion.remitoRegistrado.codigoGyC", Arrays.asList(new String[]{nroGyC.toString()}));
				avisos.add(mensajeCodigoGyC); //agrego el mensaje a la coleccion
			}
			if(nroDev!=null){
				ScreenMessage mensajeCodigoDev = new ScreenMessageImp("formularioRemito.notificacion.remitoRegistrado.codigoDev", Arrays.asList(new String[]{nroDev.toString()}));
				avisos.add(mensajeCodigoDev); //agrego el mensaje a la coleccion
			}
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);	
		}
		
		session.removeAttribute("remitoDetalles");
		session.removeAttribute("remitoDetallesViejos");
		session.removeAttribute("noAnexar");
		remitoDetalles = null;
		//hacemos el redirect
		return listaRemitosController.mostrarRemito(session, atributos, null, null, null, null, null, null, null, null, null, request);
	}
	
	@RequestMapping(
			value="/cargarListadoCodigosElementos.html",
			method = RequestMethod.GET
		)
	public String cargarListadoCodigosElementos(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="codigoDepositoOrigen",required=false) String codigoDepositoOrigen,
			@RequestParam(value="codigoDepositoDestino",required=false) String codigoDepositoDestino,
			@RequestParam(value="codigoTransporte",required=false) String codigoTransporte,
			@RequestParam(value="codigoSerie",required=false) String codigoSerie,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente,
			@RequestParam(value="codigoPersonal",required=false) String codigoPersonal,
			@RequestParam(value="codigoDireccion",required=false) String codigoDireccion,
			@RequestParam(value="fechaEmision",required=false) String fechaEmision,
			@RequestParam(value="fechaEntrega",required=false) String fechaEntrega,
			@RequestParam(value="prefijo",required=false) String prefijo,
			@RequestParam(value="numeroSinPrefijo",required=false) String numeroSinPrefijo,
			@RequestParam(value="tipoRemito",required=false) String tipoRemito,
			@RequestParam(value="ingresoEgreso", required=false) String ingresoEgreso,
			@RequestParam(value="listaCodigosElementos",required=false) String cadenaCodigosElementos,
			@RequestParam(value="anexarCodigos",required=false) Boolean anexarCodigos,
			@RequestParam(value="verificaLectura",required=false) Boolean verificaLectura){
		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Remito remitoFormulario;
		
		@SuppressWarnings("unchecked")
		List<RemitoDetalle> remitoDetalles = (List<RemitoDetalle>) session.getAttribute("remitoDetalles");
		if(remitoDetalles == null){
			remitoDetalles = new ArrayList<RemitoDetalle>();
		}
		
		Remito remito = (Remito) session.getAttribute("remito");
		if(remito != null)
		{
			remitoFormulario = remito;
		}
		else
		{
			remitoFormulario = new Remito();
		}
		if(id!=null && id != "")
		{
		remitoFormulario.setId(Long.valueOf(id));
		}
		String empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto().getCodigo();
		String sucursalDefecto = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto().getCodigo();
		remitoFormulario.setCodigoEmpresa(empresaDefecto);
		remitoFormulario.setCodigoSucursal(sucursalDefecto);
		remitoFormulario.setCodigoDepositoOrigen(codigoDepositoOrigen);
		remitoFormulario.setCodigoDepositoDestino(codigoDepositoDestino);
		remitoFormulario.setCodigoCliente(codigoCliente);
		remitoFormulario.setCodigoTransporte(codigoTransporte);
		remitoFormulario.setCodigoSerie(codigoSerie);
		remitoFormulario.setPrefijo(prefijo);
		remitoFormulario.setNumeroSinPrefijo(numeroSinPrefijo);
		remitoFormulario.setCodigoPersonal(codigoPersonal);
		remitoFormulario.setCodigoDireccion(codigoDireccion);
		remitoFormulario.setTipoRemito(tipoRemito);
		remitoFormulario.setIngresoEgreso(ingresoEgreso);
		remitoFormulario.setVerificaLectura(verificaLectura);
		String fechaEmisionRemito = fechaEmision;
		String fechaEntregaRemito = fechaEntrega;
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		try {
			if(fechaEmisionRemito!= null && !fechaEmisionRemito.equals("")){
				Date date=sdf.parse(fechaEmisionRemito);
				remitoFormulario.setFechaEmision(date);
			}
			if(fechaEntregaRemito!= null && !fechaEntregaRemito.equals("")){
				Date date2=sdf.parse(fechaEntregaRemito);
				remitoFormulario.setFechaEntrega(date2);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//if(codigoDepositoOrigen!= null && codigoDepositoOrigen.trim() != ""){
			//Deposito depositoActual = depositoService.getByCodigoYSucursal(codigoDepositoOrigen, obtenerSucursalUser().getCodigo(), obtenerClienteAspUser());
			//if(depositoActual!= null){
				if(cadenaCodigosElementos!= null && cadenaCodigosElementos.length()>0)
				{
					validarListaCodigos(session,cadenaCodigosElementos,anexarCodigos,null,atributos, avisos);
					cadenaCodigosElementos = null;
					atributos.remove("listaCodigosElementos");
				}
			//}
		//}
		if(remitoDetalles!= null && remitoDetalles.size()>0)
		{
			remitoFormulario.setCantidadElementos(remitoDetalles.size());
			atributos.put("remitoDetalles", remitoDetalles);
			session.setAttribute("remitoDetalles", remitoDetalles);
		}
		
		atributos.put("remitoFormulario", remitoFormulario);
		atributos.put("accion", accion);
		atributos.put("errores", false);
		atributos.put("avisos", avisos);
		return precargaFormularioRemito(session, atributos, accion, id, null, null, null, null, null, null,null, null, null, null, null);

	}
	
	
	@RequestMapping(
			value="/importarLecturaElementosRemito.html",
			method = RequestMethod.GET
		)
	public String importarLecturaElementosRemito(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="codigoDepositoOrigen",required=false) String codigoDepositoOrigen,
			@RequestParam(value="codigoDepositoDestino",required=false) String codigoDepositoDestino,
			@RequestParam(value="codigoTransporte",required=false) String codigoTransporte,
			@RequestParam(value="codigoSerie",required=false) String codigoSerie,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente,
			@RequestParam(value="codigoPersonal",required=false) String codigoPersonal,
			@RequestParam(value="codigoDireccion",required=false) String codigoDireccion,
			@RequestParam(value="fechaEmision",required=false) String fechaEmision,
			@RequestParam(value="fechaEntrega",required=false) String fechaEntrega,
			@RequestParam(value="prefijo",required=false) String prefijo,
			@RequestParam(value="numeroSinPrefijo",required=false) String numeroSinPrefijo,
			@RequestParam(value="tipoRemito",required=false) String tipoRemito,
			@RequestParam(value="listaCodigosElementos",required=false) String cadenaCodigosElementos,
			@RequestParam(value="codigoLectura", required=false) String codigoLectura,
			@RequestParam(value="ingresoEgreso", required=false) String ingresoEgreso,
			@RequestParam(value="anexar",required=false) Boolean anexar,
			@RequestParam(value="verificaLectura",required=false) Boolean verificaLectura){
		
		ScreenMessage mensaje = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Boolean hayAvisos = false;
		Boolean hayAvisosNeg = false;
		Boolean banderaModulos = false, banderaInexistentes = false, 
		banderaNoGuarda = false, banderaDepositoDiferente = false, banderaRepetido = false;
		
		List<Movimiento> listaNuevos = (List<Movimiento>) session.getAttribute("listaNuevos");
		List<Movimiento> listaDevolucion = (List<Movimiento>) session.getAttribute("listaDevolucion");
		
		@SuppressWarnings("unchecked")
		List<RemitoDetalle> remitoDetalles = (List<RemitoDetalle>) session.getAttribute("remitoDetalles");
		if(remitoDetalles == null){
			remitoDetalles = new ArrayList<RemitoDetalle>();
		}
		
		Remito remitoFormulario;
		
		Remito remito = (Remito) session.getAttribute("remito");
		if(remito != null)
		{
			remitoFormulario = remito;
		}
		else
		{
			remitoFormulario = new Remito();
		}
		if(id!=null && id != "")
		{
		remitoFormulario.setId(Long.valueOf(id));
		}
		String empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto().getCodigo();
		String sucursalDefecto = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto().getCodigo();
		remitoFormulario.setCodigoEmpresa(empresaDefecto);
		remitoFormulario.setCodigoSucursal(sucursalDefecto);
		remitoFormulario.setCodigoDepositoOrigen(codigoDepositoOrigen);
		remitoFormulario.setCodigoDepositoDestino(codigoDepositoDestino);
		remitoFormulario.setCodigoCliente(codigoCliente);
		remitoFormulario.setCodigoTransporte(codigoTransporte);
		remitoFormulario.setCodigoSerie(codigoSerie);
		remitoFormulario.setPrefijo(prefijo);
		remitoFormulario.setNumeroSinPrefijo(numeroSinPrefijo);
		remitoFormulario.setCodigoPersonal(codigoPersonal);
		remitoFormulario.setCodigoDireccion(codigoDireccion);
		remitoFormulario.setTipoRemito(tipoRemito);
		remitoFormulario.setIngresoEgreso(ingresoEgreso);
		String fechaEmisionRemito = fechaEmision;
		String fechaEntregaRemito = fechaEntrega;
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date date=sdf.parse(fechaEmisionRemito);
			remitoFormulario.setFechaEmision(date);
			Date date2=sdf.parse(fechaEntregaRemito);
			remitoFormulario.setFechaEntrega(date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//if(codigoDepositoOrigen!= null && codigoDepositoOrigen.trim() != ""){
			List<LecturaDetalle> listaLecturaElementos;
			//Deposito depositoActual = depositoService.getByCodigoYSucursal(codigoDepositoOrigen, obtenerSucursalUser().getCodigo(), obtenerClienteAspUser());
			//if(depositoActual!= null){
				//Si se importa una lectura
			 	if (codigoLectura != null) {
						Lectura lectura = lecturaService.obtenerPorCodigo(Long.valueOf(codigoLectura), null, obtenerEmpresaUser(),obtenerClienteAspUser());
						if(lectura==null)
						{
							session.removeAttribute("lectura");
							listaLecturaElementos = null;
							hayAvisosNeg = true;
							mensaje = new ScreenMessageImp("formularioPosicionLibre.notificacion.lecturaIvalida", null);
							avisos.add(mensaje);
							atributos.put("hayAvisosNeg", hayAvisosNeg);
							atributos.put("avisos", avisos);
							return precargaFormularioRemito(session, atributos, null, null, null, null, null, null, null, null,null, null, null, null, null);
						}
						session.setAttribute("lectura", lectura);
						listaLecturaElementos = lecturaDetalleService.listarLecturaDetallePorLectura(lectura,obtenerClienteAspUser());
						//Si la lectura contiene al menos un elemento
						if (listaLecturaElementos != null && listaLecturaElementos.size() > 0) {
							
		
							//Recorro la lista de elementos
							for (int i = 0; i < listaLecturaElementos.size(); i++) {
								//Pregunto si el codigo de barras pertenece a un modulo
								if (listaLecturaElementos.get(i).getCodigoBarras().startsWith("99")) {
									//Si es asi se remueve de la lista
									listaLecturaElementos.remove(i);
									i = i - 1;
									banderaModulos = true;
								} 
								else
								{
									//Pregunto si la lectura no pertenece a un elemento existente
									if (listaLecturaElementos.get(i).getElemento() == null) {
										//Si se es asi se remueve
										listaLecturaElementos.remove(i);
										i = i - 1;
										banderaInexistentes = true;
									}
									//Si llega aqui es que es un elemento existente
									else
									{
										if("En el Cliente".equals(listaLecturaElementos.get(i).getElemento().getEstado())){
											Movimiento mov = new Movimiento();
											mov.setElemento(listaLecturaElementos.get(i).getElemento());
											listaNuevos.add(mov);
										}
										else if("En Consulta".equals(listaLecturaElementos.get(i).getElemento().getEstado())){
											Movimiento mov = new Movimiento();
											mov.setElemento(listaLecturaElementos.get(i).getElemento());
											listaDevolucion.add(mov);
										}
											//Se pregunta si el elemento esta en el mismo deposito que el deposito de origen seleccionado
											//if(depositoActual.getId().longValue() == listaLecturaElementos.get(i).getElemento().getDepositoActual().getId().longValue())
											//{
												//Se pregunta si el elemento esta en estado de EN GUARDA
//												if(!"En Guarda".equals(listaLecturaElementos.get(i).getElemento().getEstado()))
//												{
//													//Si se es asi se remueve
//													listaLecturaElementos.remove(i);
//													i = i - 1;
//													banderaNoGuarda = true;
//												}
											//}
//										else
//											{
//												//Si no es asi se remueve
//												listaLecturaElementos.remove(i);
//												i = i - 1;
//												banderaDepositoDiferente = true;
//											}
									}
								}
							}
							if(banderaModulos)
							{
								//Se avisa que uno o mas modulos fueron descartados de la lectura
								mensaje = new ScreenMessageImp("formularioPosicionLibre.notificacion.filtradoElementoModulo", null);
								avisos.add(mensaje);
								hayAvisos = true;
								
							}
							if(banderaInexistentes)
							{
								//Se avisa que uno o mas elementos inexistentes fueron descartados de la lectura
								mensaje = new ScreenMessageImp("formularioPosicionLibre.notificacion.filtradoElementoInexistente", null);
								avisos.add(mensaje);
								hayAvisos = true;
							}
							if(banderaNoGuarda)
							{
								//Se avisa que uno o mas elementos no estan En Guarda
								mensaje = new ScreenMessageImp("formularioRemito.notificacion.elementosNoEnGuardaFiltrados", null);
								avisos.add(mensaje);
								hayAvisos = true;
							}
							if(banderaDepositoDiferente)
							{
								//Se avisa que uno o mas elementos no estan en el mismo deposito que el de origen seleccionado
								mensaje = new ScreenMessageImp("formularioRemito.notificacion.elementosDepositoDiferente", null);
								avisos.add(mensaje);
								hayAvisos = true;
							}
							//Se pregunta si quedaron elementos existentes en la lectura para asignarles posiciones libres
							if (listaLecturaElementos.size() > 0) {
								
								Long orden = (long)0;
								if (anexar == null || anexar== false) {
									remitoDetalles.removeAll(remitoDetalles);
									session.setAttribute("noAnexar", true);
								} else {
									//session.setAttribute("noAnexar", false);
									if(remitoDetalles!= null && remitoDetalles.size()>0){
										for (RemitoDetalle remitoDetalleOrden : remitoDetalles) {
											if(remitoDetalleOrden.getOrden()>orden)
											{
												orden = remitoDetalleOrden.getOrden();
											}
										}
									}
								}
								for (LecturaDetalle lecturaDetalle : listaLecturaElementos) {
									Boolean repetido = false;
									RemitoDetalle remitoDetalle = new RemitoDetalle();
									remitoDetalle.setElemento(lecturaDetalle.getElemento());
									if(remitoDetalles.size()>0)
									{
										for(int f = (remitoDetalles.size()-1);f>=0;f--)
										{
											if(remitoDetalle.getElemento().equals(remitoDetalles.get(f).getElemento()))
											{
												banderaRepetido = true;
												repetido = true;
												break;
											}
										}
									}
									if(!repetido)
									{
										orden++;
										remitoDetalle.setOrden(orden);
										remitoDetalles.add(remitoDetalle);
									}
									
								}
								if(banderaRepetido)
								{
									//Se avisa que uno o mas elementos no estan en el mismo deposito que el de origen seleccionado
									mensaje = new ScreenMessageImp("formularioRemito.notificacion.elementosRepetidos", null);
									avisos.add(mensaje);
									hayAvisos = true;
								}
								//Se muesta la lista de elementos
								atributos.put("remitoDetalles", remitoDetalles);
								session.setAttribute("remitoDetalles", remitoDetalles);
							} else {
								//Se avisa que la lectura no contiene elmentos validos para asignarles posicion libre
								mensaje = new ScreenMessageImp("formularioPosicionLibre.notificacion.lecturaSinElementos", null);
								avisos.add(mensaje);
								hayAvisosNeg = true;
							}
		
						}
					}//salida de la importacion de lectura
				//}
		//}
		//atributos.put("actualizaNumero", "NO");
		atributos.put("accion", accion);
		remitoFormulario.setCantidadElementos(remitoDetalles.size());
		atributos.put("remitoFormulario", remitoFormulario);
		atributos.put("codigoLectura", codigoLectura);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		session.setAttribute("listaNuevos", listaNuevos);
		session.setAttribute("listaDevolucion", listaDevolucion);
		//hacemos el forward
		return precargaFormularioRemito(session, atributos, accion, id, null, null, null, null, null, null,null, null, null, null, null);
	}
	
		/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void validarListaCodigos(HttpSession session, String cadenaCodigosElementos,Boolean anexar,Deposito depositoActual,Map<String,Object> atributos, List<ScreenMessage> avisos)
	{
		ScreenMessage mensaje = null;
		Boolean hayAvisos = false;
		
		@SuppressWarnings("unchecked")
		List<RemitoDetalle> remitoDetalles = (List<RemitoDetalle>) session.getAttribute("remitoDetalles");
		if(remitoDetalles == null){
			remitoDetalles = new ArrayList<RemitoDetalle>();
		}
		
		RemitoDetalle remitoDetalle = null;
		Long orden = (long)0;
		String[] listaCodigos = cadenaCodigosElementos.split("\\,");
		if (listaCodigos != null) {
			if (anexar == null || anexar == false) {
				remitoDetalles.removeAll(remitoDetalles);
				session.setAttribute("noAnexar", true);
			} else {
				//session.setAttribute("noAnexar", false);
				for (RemitoDetalle remitoDetalleOrden : remitoDetalles) {
					if(remitoDetalleOrden.getOrden()>orden)
					{
						orden = remitoDetalleOrden.getOrden();
					}
				}
			}
		List<String> listaDescartados = new ArrayList<String>();
		Boolean banderaModulos = false, banderaInexistentes = false, banderaNoGuarda = false, banderaDepositoDiferente = false,banderaRepetido = false;
		String codigo,codigoCorrecto,codigoTomado12;
		Boolean repetido, noValido;
		for (int i = 0; i < listaCodigos.length; i++) {
			repetido = false;
			noValido = false;
			banderaModulos = false;
			banderaInexistentes = false;
			banderaNoGuarda = false;
			banderaDepositoDiferente = false;
			codigo = listaCodigos[i];
			if(codigo.length() == 13)
			{
				codigoTomado12 = listaCodigos[i].substring(0, 12);
				codigoCorrecto = codigoTomado12 + String.valueOf(EAN13.EAN13_CHECKSUM(codigoTomado12));
				if(codigo.equals(codigoCorrecto))
				{
					remitoDetalle = new RemitoDetalle();
					if (codigo.startsWith("99")) 
					{
						noValido = true;
						banderaModulos= true;
					} 
					else 
					{
						Elemento elemento = elementoService.getByCodigo(codigoTomado12,obtenerClienteAspUser());
						if (elemento != null) 
						{
							//if(depositoActual.getId().longValue() == elemento.getDepositoActual().getId().longValue()){
								//if(Constantes.ELEMENTO_ESTADO_EN_GUARDA.equals(elemento.getEstado()))
								//{
									remitoDetalle.setElemento(elemento);
								//}
//								else
//								{
//									banderaNoGuarda = true;
//									noValido = true;
//								}
							//}
//							else
//							{
//								banderaDepositoDiferente = true;
//								noValido = true;
//							}
							
						} 
						else 
						{
							noValido = true;
							banderaInexistentes = true;
						}
					}
						if(noValido==false){
							if(remitoDetalles.size()>0)
							{
								for(int f = (remitoDetalles.size()-1);f>=0;f--)
								{
									if(codigoTomado12.equals(remitoDetalles.get(f).getElemento().getCodigo()))
									{
										banderaRepetido = true;
										repetido = true;
										break;
									}
								}
							}
							if(!repetido)
							{
								orden++;
								remitoDetalle.setOrden(orden);
								remitoDetalles.add(remitoDetalle);
							}
						}
					}
					else
					{
						noValido = true;
						banderaInexistentes = true;					
					}
			}
			else
			{
				listaDescartados.add(codigo+"(Código erróneo)\n");					
			}
			if(banderaModulos)
			{
				//Se avisa que uno o mas modulos fueron descartados de la lectura
				mensaje = new ScreenMessageImp("formularioPosicionLibre.notificacion.filtradoElementoModulo", null);
				avisos.add(mensaje);
				hayAvisos = true;
				
			}
			if(banderaInexistentes)
			{
				//Se avisa que uno o mas elementos inexistentes fueron descartados de la lectura
				mensaje = new ScreenMessageImp("formularioPosicionLibre.notificacion.filtradoElementoInexistente", null);
				avisos.add(mensaje);
				hayAvisos = true;
			}
			if(banderaNoGuarda)
			{
				//Se avisa que uno o mas elementos no estan En Guarda
				mensaje = new ScreenMessageImp("formularioRemito.notificacion.elementosNoEnGuardaFiltrados", null);
				avisos.add(mensaje);
				hayAvisos = true;
			}
			if(banderaDepositoDiferente)
			{
				//Se avisa que uno o mas elementos no estan en el mismo deposito que el de origen seleccionado
				mensaje = new ScreenMessageImp("formularioRemito.notificacion.elementosDepositoDiferente", null);
				avisos.add(mensaje);
				hayAvisos = true;
			}
			if(banderaRepetido)
			{
				//Se avisa que uno o mas elementos no estan en el mismo deposito que el de origen seleccionado
				mensaje = new ScreenMessageImp("formularioRemito.notificacion.elementosRepetidos", null);
				avisos.add(mensaje);
				hayAvisos = true;
			}
		}
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		session.setAttribute("remitoDetalles", remitoDetalles);
		}
	}
	
	
	
	
	private void setData(Remito remitoFormulario, Remito remito){
		if(remito != null){			
			remitoFormulario.setNumero(remito.getNumero());
			remitoFormulario.setNumeroSinPrefijo(remito.getNumeroSinPrefijo());
			remitoFormulario.setPrefijo(remito.getPrefijo());
			remitoFormulario.setEstado(remito.getEstado());
			remitoFormulario.setTipoRemito(remito.getTipoRemito());
			remitoFormulario.setIngresoEgreso(remito.getIngresoEgreso());
			remitoFormulario.setEmpresa(remito.getEmpresa());
			remitoFormulario.setSucursal(remito.getSucursal());
			remitoFormulario.setUsuario(remito.getUsuario());
			if("cliente".equals(remito.getTipoRemito())){
				if("ingreso".equals(remito.getIngresoEgreso()))
				{remitoFormulario.setDepositoDestino(remito.getDepositoDestino());}
				else
				{remitoFormulario.setDepositoOrigen(remito.getDepositoOrigen());}
				remitoFormulario.setClienteEmp(remito.getClienteEmp());
				remitoFormulario.setEmpleado(remito.getEmpleado());
				remitoFormulario.setDireccion(remito.getDireccion());
			}
			else
			{
				remitoFormulario.setDepositoOrigen(remito.getDepositoOrigen());
				remitoFormulario.setDepositoDestino(remito.getDepositoDestino());
			}
			remitoFormulario.setTransporte(remito.getTransporte());
			remitoFormulario.setSerie(remito.getSerie());
			remitoFormulario.setTipoComprobante(remito.getSerie().getAfipTipoComprobante());
			remitoFormulario.setLetraComprobante(remito.getSerie().getAfipTipoComprobante().getLetra());
			remitoFormulario.setFechaEmision(remito.getFechaEmision());
			remitoFormulario.setObservacion(remito.getObservacion());
			remitoFormulario.setDetalles(remito.getDetalles());
			remitoFormulario.setCantidadElementos(remito.getDetalles().size());
			remitoFormulario.setVerificaLectura(remito.getVerificaLectura());
		}
	
	}
	
	private void definirPopupSerie(Map<String,Object> atributos, String val, String empresaCodigo){
		Empresa empresa = null;
		if(empresaCodigo!=null)
		{
			empresa = empresaService.getByCodigo(empresaCodigo, obtenerClienteAspUser());
		}
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> seriesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioRemito.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioRemito.descripcion",false));		
		seriesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		seriesPopupMap.put("coleccionPopup", serieService.listarSeriePopup(val, "R", null, obtenerClienteAspUser(), empresa, true));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		seriesPopupMap.put("referenciaHtml", "codigoSerie"); 		
		//url que se debe consumir con ajax
		seriesPopupMap.put("urlRequest", "mostrarRemito.html");
		//se vuelve a setear el texto utilizado para el filtrado
		seriesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		seriesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("seriesPopupMap", seriesPopupMap);
	}
	
	private void definirPopupDepositosDestino(Map<String,Object> atributos, String val, String sucursalCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> depositosDestinoPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioDeposito.datosDeposito.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioDeposito.datosDeposito.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		depositosDestinoPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		depositosDestinoPopupMap.put("coleccionPopup", depositoService.listarDepositoPopup(val, sucursalCodigo, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		depositosDestinoPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		depositosDestinoPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		depositosDestinoPopupMap.put("referenciaHtml", "codigoDepositoDestino"); 		
		//url que se debe consumir con ajax
		depositosDestinoPopupMap.put("urlRequest", "precargaFormularioRemito.html");
		//se vuelve a setear el texto utilizado para el filtrado
		depositosDestinoPopupMap.put("textoBusqueda", val);
		depositosDestinoPopupMap.put("filterPopUp", sucursalCodigo);
		//codigo de la localización para el titulo del popup
		depositosDestinoPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("depositosDestinoPopupMap", depositosDestinoPopupMap);
	}
	
	private void definirPopupDepositosOrigen(Map<String,Object> atributos, String val, String sucursalCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> depositosOrigenPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioDeposito.datosDeposito.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioDeposito.datosDeposito.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		depositosOrigenPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		depositosOrigenPopupMap.put("coleccionPopup", depositoService.listarDepositoPopup(val, sucursalCodigo, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		depositosOrigenPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		depositosOrigenPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		depositosOrigenPopupMap.put("referenciaHtml", "codigoDepositoOrigen"); 		
		//url que se debe consumir con ajax
		depositosOrigenPopupMap.put("urlRequest", "precargaFormularioRemito.html");
		//se vuelve a setear el texto utilizado para el filtrado
		depositosOrigenPopupMap.put("textoBusqueda", val);
		depositosOrigenPopupMap.put("filterPopUp", sucursalCodigo);
		//codigo de la localización para el titulo del popup
		depositosOrigenPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("depositosOrigenPopupMap", depositosOrigenPopupMap);
	}
	
	private void definirPopupCliente(Map<String,Object> atributos, String val, String empresaCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clientesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("razonSocialONombreYApellido","formularioClienteDireccion.cliente.razonSocial",false));
		campos.add(new CampoDisplayTag("codigo","formularioClienteDireccion.cliente.apellido",true));
		clientesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		clientesPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup(val, empresaCodigo, obtenerClienteAspUser(), true));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup2", "razonSocialONombreYApellido");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clientesPopupMap.put("referenciaHtml", "codigoCliente"); 		
		//url que se debe consumir con ajax
		clientesPopupMap.put("urlRequest", "precargaFormularioRemito.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clientesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		clientesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("clientesPopupMap", clientesPopupMap);
	}
	
	private void definirPopupTransporte(Map<String,Object> atributos, String val,String codigoEmpresa){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> transportesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioRemito.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioRemito.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		transportesPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		transportesPopupMap.put("coleccionPopup", transporteService.listarTransportePopup(val, codigoEmpresa, obtenerClienteAspUser(),true));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		transportesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		transportesPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		transportesPopupMap.put("referenciaHtml", "codigoTransporte");
		//url que se debe consumir con ajax
		transportesPopupMap.put("urlRequest", "precargaFormularioRemito.html");
		//se vuelve a setear el texto utilizado para el filtrado
		transportesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		transportesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("transportesPopupMap", transportesPopupMap);
	}
	
	private void definirPopupEmpleado(Map<String,Object> atributos, String val,String codigoCliente){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> empleadosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioRemito.codigo",false));
		campos.add(new CampoDisplayTag("nombreYApellido","formularioRemito.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		empleadosPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		empleadosPopupMap.put("coleccionPopup", empleadoService.listarEmpleadoPopup(val, obtenerClienteAspUser(), codigoCliente,true));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		empleadosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		empleadosPopupMap.put("referenciaPopup2", "observaciones");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		empleadosPopupMap.put("referenciaHtml", "codigoPersonal");
		//url que se debe consumir con ajax
		empleadosPopupMap.put("urlRequest", "precargaFormularioRemito.html");
		//se vuelve a setear el texto utilizado para el filtrado
		empleadosPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		empleadosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("personalPopupMap", empleadosPopupMap);
	}
	
	private void definirPopupDirecciones(Map<String,Object> atributos, String val, String clienteCodigo){
		ClienteEmp cliente = null;
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> direccionesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioEmpleado.datosEmpleado.codigo",true));
		campos.add(new CampoDisplayTag("direccion.calle","formularioEmpleado.datosEmpleado.direccion.calle",false));
		campos.add(new CampoDisplayTag("direccion.numero","formularioEmpleado.datosEmpleado.direccion.numero",false));
		campos.add(new CampoDisplayTag("direccion.barrio.nombre","formularioEmpleado.datosEmpleado.direccion.barrio",false));
		campos.add(new CampoDisplayTag("direccion.barrio.localidad.nombre","formularioEmpleado.datosEmpleado.direccion.localidad",false));
		campos.add(new CampoDisplayTag("direccion.barrio.localidad.provincia.nombre","formularioEmpleado.datosEmpleado.direccion.provincia",false));
		//campos.add(new CampoDisplayTag("descripcion","formularioSucursal.datosSucursal.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		direccionesPopupMap.put("campos", campos);
		if(clienteCodigo != "" && clienteCodigo != null)
		{
			cliente = clienteEmpService.getByCodigo(clienteCodigo);
		}
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		direccionesPopupMap.put("coleccionPopup", clienteDireccionService.listarDireccionesPopup(val, cliente, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		direccionesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		direccionesPopupMap.put("referenciaPopup2", "direccion.calle"+"direccion.numero");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		direccionesPopupMap.put("referenciaHtml", "codigoDireccion"); 		
		//url que se debe consumir con ajax
		direccionesPopupMap.put("urlRequest", "mostrarEmpleado.html");
		//se vuelve a setear el texto utilizado para el filtrado
		direccionesPopupMap.put("textoBusqueda", val);
		//se setea el codigo del cliente seleccionado.
		direccionesPopupMap.put("filterPopUp", clienteCodigo);
		//codigo de la localización para el titulo del popup
		direccionesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("direccionesPopupMap", direccionesPopupMap);
	}
	
	private void definirPopupLecturas(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> lecturasPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioPosicionLibre.posicionLibreDetalle.lectura.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioPosicionLibre.posicionLibreDetalle.lectura.descripcion",false));		
		lecturasPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		ClienteAsp casp= obtenerClienteAspUser();
		Empresa emp = obtenerEmpresaUser();
		lecturasPopupMap.put("coleccionPopup", lecturaService.listarLecturaPopup(val, casp, emp ));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		lecturasPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		lecturasPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		lecturasPopupMap.put("referenciaHtml", "codigoLectura"); 		
		//url que se debe consumir con ajax
		lecturasPopupMap.put("urlRequest", "mostrarPosicionLibre.html");
		//se vuelve a setear el texto utilizado para el filtrado
		lecturasPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		lecturasPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("lecturasPopupMap", lecturasPopupMap);
	}
	
private Movimiento setData(Movimiento movimientoFormulario, Movimiento movimiento){
		
		
		if(movimiento != null){			

			movimientoFormulario.setAccion(movimiento.getAccion());
			movimientoFormulario.setClienteAsp(movimiento.getClienteAsp());
			movimientoFormulario.setTipoMovimiento(movimiento.getTipoMovimiento());
			movimientoFormulario.setClaseMovimiento(movimiento.getClaseMovimiento());
			movimientoFormulario.setUsuario(movimiento.getUsuario());
			movimientoFormulario.setDescripcion(movimiento.getDescripcion());
			movimientoFormulario.setResponsable(movimiento.getResponsable());
			if("cliente".equals(movimiento.getClaseMovimiento())){
				
				movimientoFormulario.setDeposito(movimiento.getDeposito());
				movimientoFormulario.setClienteEmpOrigenDestino(movimiento.getClienteEmpOrigenDestino());
				
			}
			else
			{
				movimientoFormulario.setDeposito(movimiento.getDeposito());
				movimientoFormulario.setDepositoOrigenDestino(movimiento.getDepositoOrigenDestino());
			}
			movimientoFormulario.setFecha(movimiento.getFecha());
			movimientoFormulario.setCantidadElementos(movimiento.getListaElementos().size());
			
		}
		return movimientoFormulario;
	
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	private Empresa obtenerEmpresaUser(){
		return ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
	}
	
	private Sucursal obtenerSucursalUser(){
		return ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto();
	}
	
	private Boolean verificarDetallesConMovimientos(Boolean commit,Remito remito, Remito remitoFormulario)
	{
			List<Movimiento> listaMovimientos = new ArrayList<Movimiento>();
			List<Posicion> listaPosiciones = new ArrayList<Posicion>();
			
			for(int i = 0 ; i< remito.getMovAsociados().size();i++)
			{
				
				remito.getMovAsociados().get(i).setRemito(remitoFormulario);
				if(remito.getTipoRemito().equalsIgnoreCase("cliente"))
				{
					if(remito.getIngresoEgreso().equalsIgnoreCase("INGRESO"))
					{	
						remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
						remito.getMovAsociados().get(i).getElemento().setDepositoActual(remito.getDepositoDestino());
						remito.getMovAsociados().get(i).setClienteEmpOrigenDestino(remito.getClienteEmp());	
						if(remito.getMovAsociados().get(i).getEstadoElemento().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE))
							remito.getMovAsociados().get(i).setCodigoCarga(remito.getNroGyC());
						if(remito.getMovAsociados().get(i).getEstadoElemento().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_CONSULTA))
							remito.getMovAsociados().get(i).setCodigoCarga(remito.getNroDev());
					}
					else if(remito.getIngresoEgreso().equalsIgnoreCase("EGRESO"))
					{
							if(remito.getMovAsociados().get(i).getEstadoElemento().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_CREADO))
								remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE);
							else	
								remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_EN_CONSULTA);
							
							if(remito.getMovAsociados().get(i).getElemento().getPosicion()!=null)
							{
								remito.getMovAsociados().get(i).getElemento().setPosicion(null);
								Posicion posicion = remito.getMovAsociados().get(i).getPosicionOrigenDestino();
								posicion.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
								listaPosiciones.add(posicion);
							}
					}
				}
				else if(remito.getTipoRemito().equalsIgnoreCase("interno"))
				{
					if(remito.getIngresoEgreso().equalsIgnoreCase("INGRESO"))
					{
						
						remito.getMovAsociados().get(i).getElemento().setDepositoActual(remito.getDepositoDestino());
						
						//Se buscan movimientos de tipo Cliente e Ingreso existentes para ese elemento
						Movimiento mov = new Movimiento();
						mov.setTipoMovimiento("INGRESO");
						mov.setClaseMovimiento("cliente");
						mov.setElemento(remito.getMovAsociados().get(i).getElemento());
						Integer movIngCliAnterior = movimientoService.contarMovimientosFiltrados(mov, obtenerClienteAspUser());
						//Si existe al menos uno es que ya ha estado en el cliente por lo cual debe estar en Guarda
						if(movIngCliAnterior!=null && movIngCliAnterior>0)
						{
							//Se setea el nuevo estado al elemento
							remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
							//HABLADO CON LUIS -Se quita de esta regla y se hace para todos
							//listaElementos.get(i).setDepositoActual(nuevoMovimiento.getDeposito());
						}
						//Sino todavia no ha ido al cliente y su estado debe ser creado
						else
						{
							//Se setea el nuevo estado al elemento
							remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_CREADO);
						}
						
					}
					else if(remito.getIngresoEgreso().equalsIgnoreCase("EGRESO"))
					{
						remito.getMovAsociados().get(i).getElemento().setEstado(Constantes.ELEMENTO_ESTADO_EN_TRANSITO);
						
						if(remito.getMovAsociados().get(i).getElemento().getPosicion()!=null)
						{
							remito.getMovAsociados().get(i).getElemento().setPosicion(null);
							Posicion posicion = remito.getMovAsociados().get(i).getPosicionOrigenDestino();
							posicion.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
							listaPosiciones.add(posicion);
						}
					}
				}
				
				listaMovimientos.add(remito.getMovAsociados().get(i));
				
			}
			
			commit = movimientoService.actualizarMovimientoListYActualizarPosiciones(listaMovimientos, listaPosiciones);
			return commit;
	}
}
