package com.dividato.configuracionGeneral.controladores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import com.dividato.configuracionGeneral.validadores.MovimientoValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.MovimientoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PosicionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.Movimiento;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Sucursal;
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
				"/iniciarPrecargaFormularioMovimiento.html",
				"/precargaFormularioMovimiento.html",
				"/guardarActualizarMovimiento.html",
				"/cargarListadoCodigosElementosMovimiento.html",
				"/importarLecturaElementosMovimiento.html",
				"/anularMovimiento.html",
				"/eliminarMovimientoDetalle.html"
				
			}
		)
public class FormMovimientoController {
	
	private DepositoService depositoService;
	private MovimientoService movimientoService;
	private PosicionService posicionService;
	private ElementoService elementoService;
	private UserService userService;
	private MovimientoValidator validator;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	private LecturaService lecturaService;
	private LecturaDetalleService lecturaDetalleService;
	private ClienteEmpService clienteEmpService;
	private EmpleadoService empleadoService;

	private ListaMovimientosController listaMovimientosController;
	
	
	/**
	 * Setea el servicio de Movimiento.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto MovimientoService.
	 * La clase MovimientoImp implementa Movimiento y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param movimientoService
	 */
	
	@Autowired
	public void setListaMovimientosController(ListaMovimientosController listaMovimientosController) {
		this.listaMovimientosController = listaMovimientosController;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	@Autowired
	public void setPosicionService(PosicionService posicionService) {
		this.posicionService = posicionService;
	}
	@Autowired
	public void setMovimientoService(MovimientoService movimientoService) {
		this.movimientoService = movimientoService;
	}
	@Autowired
	public void setLecturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
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
	public void setEmpleadoService(EmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	
	@Autowired
	public void setValidator(MovimientoValidator validator) {
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
	

	@RequestMapping(
			value="/iniciarPrecargaFormularioMovimiento.html",
			method = RequestMethod.GET
		)
	public String iniciarPrecargaFormularioMovimiento(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion", required=false) String accion,
			@RequestParam(value="id", required=false) String id)
	{
		session.removeAttribute("listaElementos");
		session.removeAttribute("movimientoSession");
		session.removeAttribute("noAnexar");
		
		return precargaFormularioMovimiento(session, atributos, accion, id, null, null, null, null, null, null,null, null, null, null, null);
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
	 * @return "formularioMovimiento" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioMovimiento.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioMovimiento(HttpSession session,
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
		
		Movimiento movimientoFormulario;
		
		
		String empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto().getCodigo();
		String sucursalDefecto = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto().getCodigo();
		
		Empleado empleado = (Empleado) session.getAttribute("empleadoSession");
		if(empleado==null)
			sucursalDefecto="";
			
		atributos.put("codigoEmpresa", empresaDefecto);
		atributos.put("codigoSucursalActual", sucursalDefecto);
		
		@SuppressWarnings("unchecked")
		List<Elemento> listaElementos = (List<Elemento>) session.getAttribute("listaElementos");
		if(listaElementos == null){
			listaElementos = new ArrayList<Elemento>();
		}
		
		if(accion==null) {
			accion="NUEVO"; //acción por defecto: nuevo
			movimientoFormulario = new Movimiento();
			Date fechaHoy = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			movimientoFormulario.setFecha(fechaHoy);
			movimientoFormulario.setDescripcion("Movimiento registrado el "+sdf.format(fechaHoy));
			atributos.put("movimientoFormulario", movimientoFormulario);
		}
		if(!accion.equals("NUEVO")){
			movimientoFormulario = movimientoService.obtenerPorId(Long.valueOf(id));
			atributos.put("movimientoFormulario", movimientoFormulario);
		}
		
		@SuppressWarnings("unchecked")
		Movimiento movimientoSession = (Movimiento) session.getAttribute("movimientoSession");
		if(movimientoSession != null){
			atributos.put("movimientoFormulario", movimientoSession);
		}
		
//		if(atributos.get("movimientoFormulario") != null)
//		{
//			movimientoFormulario = (Movimiento) atributos.get("movimientoFormulario");
//		}
//		else
//		{
//			atributos.put("movimientoFormulario", movimientoFormulario);
//		}
		
		atributos.put("listaElementos", listaElementos);
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		definirPopupCliente(atributos, valCliente, empresaDefecto);
		definirPopupSucursal(atributos,valSucursal,empresaDefecto);
		definirPopupDepositosDestino(atributos, valDeposito, sucursalCodigo);
		definirPopupDepositosOrigen(atributos, valDeposito, sucursalDefecto);
		definirPopupLecturas(atributos, valLectura);
		definirPopupResponsables(atributos, valSerie);

		atributos.put("accion",accion);
		
		return "formularioMovimiento";
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
			value="/guardarActualizarMovimiento.html",
			method= RequestMethod.POST
		)
	public String guardarMovimiento(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="listaTipoTrabajo",required=false) String listaTipoTrabajo,
			@ModelAttribute("movimientoFormulario") final Movimiento movimiento,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		
		@SuppressWarnings("unchecked")
		List<Elemento> listaElementos = (List<Elemento>) session.getAttribute("listaElementos");
		if(listaElementos == null){
			listaElementos = new ArrayList<Elemento>();
		}
		
		Boolean commit = null;
		if (accion == null || accion.equals("") || accion.equals("NUEVO"))
			accion = "NUEVO";
		else {
			accion = "MODIFICACION";
		}
		
		if(!result.hasErrors()){
			movimiento.setAccion(accion);
			movimiento.setClienteAsp(obtenerClienteAspUser());
			Empresa empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto();
			Deposito depositoActual = depositoService.getByCodigo(movimiento.getCodigoDepositoActual(), obtenerClienteAspUser());
			Sucursal sucursalDefecto = depositoActual.getSucursal();
			User usuario = obtenerUser();
			movimiento.setCodigoEmpresa(empresaDefecto.getCodigo());
			movimiento.setCodigoSucursalActual(sucursalDefecto.getCodigo());
			movimiento.setUsuario(usuario);
			movimiento.setListaElementos(listaElementos);
			if(movimiento.getCodigoResponsable()!=null){
				User responsable = userService.obtenerPorId(movimiento.getCodigoResponsable());
			if(responsable!=null)
				movimiento.setResponsable(responsable);
			}
			validator.validate(movimiento,result);
		}
	
		Movimiento movimientoFormulario = new Movimiento();
		
		if(!result.hasErrors()){
		
//			if(accion.equals("NUEVO")){
//				movimientoFormulario = new Movimiento();				
//			}
			
			//empezamos a setear los datos nuevos,
			movimientoFormulario.setId(movimiento.getId());
			movimientoFormulario.setClienteAsp(obtenerClienteAspUser());
			
			
	
			if(accion.equals("NUEVO")){
				

					String[] listadoTipoTrabajo = listaTipoTrabajo.split("\\,");
					List<Long> listaIds = new ArrayList<Long>();
					List<String> listaTrabajos = new ArrayList<String>();
					if(listadoTipoTrabajo!=null && listadoTipoTrabajo.length>0 && !listadoTipoTrabajo[0].equals("")){
						for(int i = 0; i<listadoTipoTrabajo.length;i++)
						{
							listaIds.add(Long.valueOf(listadoTipoTrabajo[i].substring(0,listadoTipoTrabajo[i].indexOf("-"))));
							listaTrabajos.add(listadoTipoTrabajo[i].substring((listadoTipoTrabajo[i].indexOf("-")+1),listadoTipoTrabajo[i].length()));
							
						}
					}
					List<Movimiento> listaMovimientos = new ArrayList<Movimiento>();
					List<Posicion> listaPosiciones = new ArrayList<Posicion>();
					for(int i = 0 ; i< listaElementos.size();i++)
					{
						Movimiento nuevoMovimiento = new Movimiento();
						nuevoMovimiento = setData(nuevoMovimiento, movimiento);
						Elemento elemento = listaElementos.get(i);
						
						//Se guarda siempre la posicion y estado anterior del elemento para mantener historico
						nuevoMovimiento.setPosicionOrigenDestino(elemento.getPosicion());
						nuevoMovimiento.setEstadoElemento(elemento.getEstado());
						nuevoMovimiento.setClienteEmpOrigenDestino(elemento.getClienteEmp());

						
						if(nuevoMovimiento.getClaseMovimiento().equals("cliente"))
						{
							if(nuevoMovimiento.getTipoMovimiento().equals("INGRESO"))
							{	
									
									//Se setea el nuevo estado al elemento
									//listaElementos.get(i).setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
									//Se seta siempre el nuevo Deposito
									//listaElementos.get(i).setDepositoActual(nuevoMovimiento.getDeposito());
									//Posicion posicion = listaElementos.get(i).getPosicion();
									//if(posicion != null)
									//{
										//posicion.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
										//nuevoMovimiento.setPosicionOrigenDestino(posicion);
										//listaPosiciones.add(posicion);
										//listaElementos.get(i).setPosicion(null);
									//}
									//Se setean los tipos de trabajo
									for(int f=0;f<listaIds.size();f++)
									{
										if(listaIds.get(f).longValue() == elemento.getId().longValue())
										{
											listaElementos.get(i).setTipoTrabajo(listaTrabajos.get(f));
											listaIds.remove(f);
											listaTrabajos.remove(f);
											break;
										}
									}
									
							}
							else if(nuevoMovimiento.getTipoMovimiento().equals("EGRESO"))
							{
								//if(elemento.getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_GUARDA))
								//{
									//Se setea el nuevo estado al elemento
									//listaElementos.get(i).setEstado(Constantes.ELEMENTO_ESTADO_EN_CONSULTA);
									//HABLADO CON LUIS 19/04/2016 - LA POSICION NO SE TOCA
									//Posicion posicion = listaElementos.get(i).getPosicion();
									//if(posicion != null)
									//{
										//posicion.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
										//nuevoMovimiento.setPosicionOrigenDestino(posicion);
										//listaPosiciones.add(posicion);
										//listaElementos.get(i).setPosicion(null);
									//}
								//}
								//else if(listaElementos.get(i).getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_CREADO))
								//{
									//Se setea el nuevo estado al elemento
									//listaElementos.get(i).setEstado(Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE);
								//}
								
							}
						}
						else if(nuevoMovimiento.getClaseMovimiento().equals("interno"))
						{
							if(nuevoMovimiento.getTipoMovimiento().equals("INGRESO"))
							{
								//Se buscan movimientos de tipo Cliente e Ingreso existentes para ese elemento
//								Movimiento mov = new Movimiento();
//								mov.setTipoMovimiento("INGRESO");
//								mov.setClaseMovimiento("cliente");
//								mov.setElemento(listaElementos.get(i));
								//Se cambia el deposito para todos los casos
								//listaElementos.get(i).setDepositoActual(nuevoMovimiento.getDeposito());
								//Posicion posicion = listaElementos.get(i).getPosicion();
								//if(posicion != null)
								//{
									//posicion.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
									//nuevoMovimiento.setPosicionOrigenDestino(posicion);
									//listaPosiciones.add(posicion);
									//listaElementos.get(i).setPosicion(null);
								//}
								//Integer movIngCliAnterior = movimientoService.contarMovimientosFiltrados(mov, obtenerClienteAspUser());
								//Si existe al menos uno es que ya ha estado en el cliente por lo cual debe estar en Guarda
								//if(movIngCliAnterior!=null && movIngCliAnterior>0)
								//{
									//Se setea el nuevo estado al elemento
									//listaElementos.get(i).setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
									
									//HABLADO CON LUIS -Se quita de esta regla y se hace para todos
									//listaElementos.get(i).setDepositoActual(nuevoMovimiento.getDeposito());
								//}
								//Sino todavia no ha ido al cliente y su estado debe ser creado
								//else
								//{
									//Se setea el nuevo estado al elemento
									//listaElementos.get(i).setEstado(Constantes.ELEMENTO_ESTADO_CREADO);
								//}
							}
							else if(nuevoMovimiento.getTipoMovimiento().equals("EGRESO"))
							{
								//Se setea el nuevo estado al elemento
								//listaElementos.get(i).setEstado(Constantes.ELEMENTO_ESTADO_EN_TRANSITO);
								
								//HABLADO CON LUIS 19/04/2016 - LA POSICION NO SE TOCA
//								Posicion posicion = listaElementos.get(i).getPosicion();
//								if(posicion != null)
//								{
									//posicion.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
									//nuevoMovimiento.setPosicionOrigenDestino(posicion);
									//listaPosiciones.add(posicion);
									//listaElementos.get(i).setPosicion(null);
								//}
							}
						}
						
						nuevoMovimiento.setElemento(listaElementos.get(i));
						listaMovimientos.add(nuevoMovimiento);
						nuevoMovimiento = null;
					}
					
					commit = movimientoService.guardarMovimientoListYActualizarPosiciones(listaMovimientos, listaPosiciones);
			}
			else
			{				
//					Boolean noAnexar = (Boolean)session.getAttribute("noAnexar");
//					@SuppressWarnings("unchecked")
//					Set<MovimientoDetalle> movimientoDetallesViejos = (HashSet<MovimientoDetalle>)session.getAttribute("movimientoDetallesViejos");
//					movimientoFormulario.setDetalles(movimientoDetallesViejos);
//					Set<MovimientoDetalle> movimientoDetallesNuevos = new HashSet<MovimientoDetalle>(movimientoDetalles);
//					commit = movimientoService.actualizarMovimientoYDetalles(noAnexar, movimientoDetallesNuevos, movimientoFormulario);
			}
			if(commit == null || !commit)
				movimiento.setId(movimientoFormulario.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("movimientoFormulario", movimiento);
			session.setAttribute("movimientoSession", movimiento);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioMovimiento(session,atributos, accion, movimiento.getId()!= null ? movimiento.getId().toString() : null, null, null, null, null, null, null,null, null, null, null, null);
		}	
		
		if(result.hasErrors()){
			atributos.put("movimientoFormulario", movimiento);
			session.setAttribute("movimientoSession", movimiento);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");
			return precargaFormularioMovimiento(session,atributos, accion, movimiento.getId()!= null ? movimiento.getId().toString() : null, null, null, null, null, null, null,null, null, null, null, null);
			
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioMovimiento.notificacion.movimientosRegistrado", null);
			avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);	
		}
		
		session.removeAttribute("movimientoSession");
		session.removeAttribute("listaElementos");
		session.removeAttribute("noAnexar");
		atributos.remove("codigoDepositoActual");
		atributos.remove("codigoClienteEmp");
		listaElementos = null;
		//hacemos el redirect
		return listaMovimientosController.mostrarListaMovimientos(session, atributos, null, null, null, null, null, null, null,null);
	}
	
	@RequestMapping(
			value="/cargarListadoCodigosElementosMovimiento.html",
			method = RequestMethod.GET
		)
	public String cargarListadoCodigosElementosMovimiento(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="codigoDepositoActual",required=false) String codigoDepositoActual,
			@RequestParam(value="codigoDepositoOrigenDestino",required=false) String codigoDepositoOrigenDestino,
			@RequestParam(value="codigoSucursalOrigenDestino",required=false) String codigoSucursalOrigenDestino,
			@RequestParam(value="codigoClienteEmp",required=false) String codigoCliente,
			@RequestParam(value="fecha",required=false) String fecha,
			@RequestParam(value="claseMovimiento",required=false) String claseMovimiento,
			@RequestParam(value="listaCodigosElementos",required=false) String cadenaCodigosElementos,
			@RequestParam(value="codigoLectura", required=false) String codigoLectura,
			@RequestParam(value="tipoMovimiento", required=false) String tipoMovimiento,
			@RequestParam(value="anexarCodigos",required=false) Boolean anexarCodigos,
			@RequestParam(value="codigoResponsable",required=false) Long codigoResponsable,
			@RequestParam(value="descripcion", required=false) String descripcion){
		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Movimiento movimientoFormulario;
		
		@SuppressWarnings("unchecked")
		List<Elemento> listaElementos = (List<Elemento>) session.getAttribute("listaElementos");
		if(listaElementos == null){
			listaElementos = new ArrayList<Elemento>();
		}
		
		Movimiento movimiento = (Movimiento) session.getAttribute("movimiento");
		if(movimiento != null)
		{
			movimientoFormulario = movimiento;
		}
		else
		{
			movimientoFormulario = new Movimiento();
		}
		if(id!=null && id != "")
		{
		movimientoFormulario.setId(Long.valueOf(id));
		}
		String empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto().getCodigo();
		String sucursalDefecto = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto().getCodigo();
		movimientoFormulario.setCodigoEmpresa(empresaDefecto);
		movimientoFormulario.setCodigoSucursalOrigenDestino(codigoSucursalOrigenDestino);
		movimientoFormulario.setCodigoDepositoActual(codigoDepositoActual);
		movimientoFormulario.setCodigoDepositoOrigenDestino(codigoDepositoOrigenDestino);
		movimientoFormulario.setCodigoClienteEmp(codigoCliente);
		movimientoFormulario.setTipoMovimiento(tipoMovimiento);
		movimientoFormulario.setClaseMovimiento(claseMovimiento);
		movimientoFormulario.setDescripcion(descripcion);
		movimientoFormulario.setCodigoLectura(codigoLectura);
		movimientoFormulario.setCodigoResponsable(codigoResponsable);
		if(fecha!= null && fecha.trim() != "" && fecha != "undefined"){
			String fechaMovimiento = fecha;
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date date=sdf.parse(fechaMovimiento);
				movimientoFormulario.setFecha(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
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
		
		listaElementos = (List<Elemento>) session.getAttribute("listaElementos");
				
		if(listaElementos!= null && listaElementos.size()>0)
		{
			movimientoFormulario.setCantidadElementos(listaElementos.size());
			atributos.put("listaElementos", listaElementos);
			session.setAttribute("listaElementos", listaElementos);
		}
		
		//atributos.put("actualizaNumero", "NO");
		atributos.put("movimientoFormulario", movimientoFormulario);
		session.setAttribute("movimientoSession", movimientoFormulario);
		atributos.put("accion", accion);
		atributos.put("errores", false);
		atributos.put("avisos", avisos);
		return precargaFormularioMovimiento(session, atributos, accion, id, null, null, null, null, null, null,null, null, null, null, null);

	}
	
	
	@RequestMapping(
			value="/importarLecturaElementosMovimiento.html",
			method = RequestMethod.GET
		)
	public String importarLecturaElementosMovimiento(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="codigoDepositoActual",required=false) String codigoDepositoActual,
			@RequestParam(value="codigoDepositoOrigenDestino",required=false) String codigoDepositoOrigenDestino,
			@RequestParam(value="codigoSucursalOrigenDestino",required=false) String codigoSucursalOrigenDestino,
			@RequestParam(value="codigoClienteEmp",required=false) String codigoCliente,
			@RequestParam(value="fecha",required=false) String fecha,
			@RequestParam(value="claseMovimiento",required=false) String claseMovimiento,
			@RequestParam(value="listaCodigosElementos",required=false) String cadenaCodigosElementos,
			@RequestParam(value="codigoLectura", required=false) String codigoLectura,
			@RequestParam(value="tipoMovimiento", required=false) String tipoMovimiento,
			@RequestParam(value="anexar",required=false) Boolean anexar,
			@RequestParam(value="codigoResponsable",required=false) Long codigoResponsable,
			@RequestParam(value="descripcion", required=false) String descripcion){
		
		ScreenMessage mensaje = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Boolean hayAvisos = false;
		Boolean hayAvisosNeg = false;
		Boolean banderaModulos = false, banderaInexistentes = false, banderaNoGuarda = false, banderaDepositoDiferente = false, banderaRepetido = false;
		
		@SuppressWarnings("unchecked")
		List<Elemento> listaElementos = (List<Elemento>) session.getAttribute("listaElementos");
		if(listaElementos == null){
			listaElementos = new ArrayList<Elemento>();
		}
		
		Movimiento movimientoFormulario;
		
		Movimiento movimiento = (Movimiento) session.getAttribute("movimiento");
		if(movimiento != null)
		{
			movimientoFormulario = movimiento;
		}
		else
		{
			movimientoFormulario = new Movimiento();
		}
		if(id!=null && id != "")
		{
		movimientoFormulario.setId(Long.valueOf(id));
		}
		String empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto().getCodigo();
		String sucursalDefecto = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto().getCodigo();
		movimientoFormulario.setCodigoEmpresa(empresaDefecto);
		movimientoFormulario.setCodigoSucursalOrigenDestino(codigoSucursalOrigenDestino);
		movimientoFormulario.setCodigoDepositoActual(codigoDepositoActual);
		movimientoFormulario.setCodigoDepositoOrigenDestino(codigoDepositoOrigenDestino);
		movimientoFormulario.setCodigoClienteEmp(codigoCliente);
		movimientoFormulario.setTipoMovimiento(tipoMovimiento);
		movimientoFormulario.setClaseMovimiento(claseMovimiento);
		movimientoFormulario.setCodigoLectura(codigoLectura);
		movimientoFormulario.setDescripcion(descripcion);
		movimientoFormulario.setCodigoResponsable(codigoResponsable);
		if(fecha!= null && fecha.trim() != "" && fecha != "undefined"){
			String fechaMovimiento = fecha;
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date date=sdf.parse(fechaMovimiento);
				movimientoFormulario.setFecha(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
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
							return precargaFormularioMovimiento(session, atributos, null, null, null, null, null, null, null, null,null, null, null, null, null);
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
								mensaje = new ScreenMessageImp("formularioMovimiento.notificacion.elementosNoEnGuardaFiltrados", null);
								avisos.add(mensaje);
								hayAvisos = true;
							}
							if(banderaDepositoDiferente)
							{
								//Se avisa que uno o mas elementos no estan en el mismo deposito que el de origen seleccionado
								mensaje = new ScreenMessageImp("formularioMovimiento.notificacion.elementosDepositoDiferente", null);
								avisos.add(mensaje);
								hayAvisos = true;
							}
							//Se pregunta si quedaron elementos existentes en la lectura para asignarles posiciones libres
							if (listaLecturaElementos.size() > 0) {
								
								if (anexar == null || anexar== false) {
									listaElementos.removeAll(listaElementos);
									session.setAttribute("noAnexar", true);
								}
								
								for (LecturaDetalle lecturaDetalle : listaLecturaElementos) {
									Boolean repetido = false;
									if(listaElementos.size()>0)
									{
										for(int f = (listaElementos.size()-1) ; f>=0 ; f--)
										{
											if(lecturaDetalle.getElemento().equals(listaElementos.get(f)))
											{
												repetido = true;
												banderaRepetido = true;
												break;
											}
										}
									}
									if(!repetido)
									{
										listaElementos.add(lecturaDetalle.getElemento());
									}
									
								}
								//Se verifica si existieron elementos repetidos para informar
								if(banderaRepetido)
								{
									mensaje = new ScreenMessageImp("formularioMovimiento.notificacion.elementosRepetidos", null);
									avisos.add(mensaje);
									hayAvisos = true;
								}
								//Se muesta la lista de elementos
								atributos.put("listaElementos", listaElementos);
								session.setAttribute("listaElementos", listaElementos);
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
		movimientoFormulario.setCantidadElementos(listaElementos.size());
		atributos.put("movimientoFormulario", movimientoFormulario);
		session.setAttribute("movimientoSession", movimientoFormulario);
		atributos.put("codigoLectura", codigoLectura);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);

		//hacemos el forward
		return precargaFormularioMovimiento(session, atributos, accion, id, null, null, null, null, null, null,null, null, null, null, null);
	}
	
	
	@RequestMapping(
			value="/anularMovimiento.html",
			method = RequestMethod.GET
		)
	public String anularMovimiento(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="id",required=false) Long id,
			HttpServletRequest request){
		
		Boolean commit = false;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		
		List<Movimiento> listaMovimientos = new ArrayList<Movimiento>();
		List<Posicion> listaPosiciones = new ArrayList<Posicion>();
		
		Movimiento movimiento = movimientoService.obtenerPorId(id);
		
		if(movimiento.getEstado()!=null && movimiento.getEstado().equalsIgnoreCase("ANULADO")){
			ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioMovimiento.notificacion.movimientoYaAnulado", null);
			avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);	
		}else{
			movimiento.setEstado("ANULADO");
			Elemento elemento = movimiento.getElemento();
			Movimiento movimientoAnterior = new Movimiento();
			Posicion posicion = null;
			Posicion posicionLiberar = null;
			movimientoAnterior = movimientoService.traerMovimientoAnterior(movimiento,obtenerClienteAspUser());
			
			if(movimientoAnterior!=null){
	
				elemento.setEstado(movimientoAnterior.getEstadoElemento());
	
					if(movimiento.getTipoMovimiento().equalsIgnoreCase("EGRESO")){
						//Seteo al elemento el deposito del cual salio
						elemento.setDepositoActual(movimiento.getDeposito());
						//Si el elemento no es la primera vez que viene del cliente, entonces tenia posicion y hay que restaurarla
						if(!elemento.getEstado().equalsIgnoreCase(Constantes.ELEMENTO_ESTADO_EN_EL_CLIENTE)){
							
							//HABLADO CON LUIS 19/04/2016 - LA POSICION NO SE TOCA
//							//Traigo la posicion que tenia antes						
//							posicion = posicionService.obtenerPorId(movimiento.getPosicionOrigenDestino().getId());
//							//Si la posicion todavia esta libre
//							if(posicion.getEstado().equalsIgnoreCase(Constantes.POSICION_ESTADO_DISPONIBLE)){
//								//Se vuelve a poner ocupada
//								posicion.setEstado(Constantes.POSICION_ESTADO_OCUPADA);
//								//Se setea la posicion que tenia al objeto
//								elemento.setPosicion(posicion);		
//							}					
							//Si ya esta ocupada hay que avisar al usuario
//							else{
//								
//								ScreenMessage mensaje = new ScreenMessageImp("formularioMovimiento.error.posicionAnteriorOcupada", null);
//								avisos.add(mensaje); //agrego el mensaje a la coleccion
//								atributos.remove("result");
//								atributos.put("hayAvisos", true);
//								atributos.put("avisos", avisos);
//							}
						}
					}
					else if(movimiento.getTipoMovimiento().equalsIgnoreCase("INGRESO")){
						//Borro el deposito actual del elemento
						elemento.setDepositoActual(null);
						elemento.setTipoTrabajo(Constantes.ELEMENTO_TIPO_TRABAJO_NO_ESPECIFICADO);
						
					}else{
						//HABLADO CON LUIS 19/04/2016 - LA POSICION NO SE TOCA
//						//Traigo la actual posicion
//						posicionLiberar = posicionService.obtenerPorId(movimiento.getPosicionOrigenDestino().getId());
//						//La seteo como disponible
//						posicionLiberar.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
//						//Traigo la posicion que tenia antes						
//						posicion = posicionService.obtenerPorId(movimientoAnterior.getPosicionOrigenDestino().getId());
//						//Si la posicion todavia esta libre
//						if(posicion.getEstado().equalsIgnoreCase(Constantes.POSICION_ESTADO_DISPONIBLE)){
//							//Se vuelve a poner ocupada
//							posicion.setEstado(Constantes.POSICION_ESTADO_OCUPADA);
//							//Se setea la posicion que tenia al objeto
//							elemento.setPosicion(posicion);		
//						}
//						//Si ya esta ocupada hay que avisar al usuario
//						else{
//							ScreenMessage mensaje = new ScreenMessageImp("formularioMovimiento.error.posicionAnteriorOcupada", null);
//							avisos.add(mensaje); //agrego el mensaje a la coleccion
//							atributos.remove("result");
//							atributos.put("hayAvisos", true);
//							atributos.put("avisos", avisos);
//						}
					}			
			}else{
				elemento.setDepositoActual(movimiento.getDeposito());
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_CREADO);	
			}
				
			listaMovimientos.add(movimiento);
			if(posicionLiberar!=null){
				listaPosiciones.add(posicionLiberar);
			}
			if(posicion!=null){
				listaPosiciones.add(posicion);
			}
			
			commit = movimientoService.actualizarMovimientoListYActualizarPosiciones(listaMovimientos, listaPosiciones);
			
			//Ver errores
			if(commit != null && !commit){
				ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
				return listaMovimientosController.mostrarListaMovimientos(session, atributos, null, null, null, null, null, null, null, request);
			}	
			
			//Genero las notificaciones 
			ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioMovimiento.notificacion.movimientoAnuladoExito", null);
			avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);	
		}
		
		return listaMovimientosController.mostrarListaMovimientos(session, atributos, null, null, null, null, null, null, null, request);
	}
	
	@RequestMapping(
			value="/eliminarMovimientoDetalle.html",
			method = RequestMethod.GET
		)
	public String eliminarMovimientoDetalle(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="codigoDepositoActual",required=false) String codigoDepositoActual,
			@RequestParam(value="codigoDepositoOrigenDestino",required=false) String codigoDepositoOrigenDestino,
			@RequestParam(value="codigoSucursalOrigenDestino",required=false) String codigoSucursalOrigenDestino,
			@RequestParam(value="codigoClienteEmp",required=false) String codigoCliente,
			@RequestParam(value="fecha",required=false) String fecha,
			@RequestParam(value="claseMovimiento",required=false) String claseMovimiento,
			@RequestParam(value="codigoLectura", required=false) String codigoLectura,
			@RequestParam(value="tipoMovimiento", required=false) String tipoMovimiento,
			@RequestParam(value="descripcion", required=false) String descripcion,
			@RequestParam(value="codigoResponsable",required=false) Long codigoResponsable,
			@RequestParam(value="seleccionados", required=false) String seleccionados) {
		
		ScreenMessage mensaje = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Boolean hayAvisos = false;
		Movimiento movimientoFormulario;
		
		@SuppressWarnings("unchecked")
		List<Elemento> listaElementos = (List<Elemento>) session.getAttribute("listaElementos");
		if(listaElementos == null){
			listaElementos = new ArrayList<Elemento>();
		}
		
		Movimiento movimiento = (Movimiento) session.getAttribute("movimiento");
		if(movimiento != null)
		{
			movimientoFormulario = movimiento;
		}
		else
		{
			movimientoFormulario = new Movimiento();
		}
		if(id!=null && id != "")
		{
		movimientoFormulario.setId(Long.valueOf(id));
		}
		String empresaDefecto = ((PersonaFisica)obtenerUser().getPersona()).getEmpresaDefecto().getCodigo();
		String sucursalDefecto = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto().getCodigo();
		movimientoFormulario.setCodigoEmpresa(empresaDefecto);
		movimientoFormulario.setCodigoSucursalOrigenDestino(codigoSucursalOrigenDestino);
		movimientoFormulario.setCodigoDepositoActual(codigoDepositoActual);
		movimientoFormulario.setCodigoDepositoOrigenDestino(codigoDepositoOrigenDestino);
		movimientoFormulario.setCodigoClienteEmp(codigoCliente);
		movimientoFormulario.setTipoMovimiento(tipoMovimiento);
		movimientoFormulario.setClaseMovimiento(claseMovimiento);
		movimientoFormulario.setCodigoLectura(codigoLectura);
		movimientoFormulario.setDescripcion(descripcion);
		movimientoFormulario.setCodigoResponsable(codigoResponsable);
		if(fecha!= null && fecha.trim() != "" && fecha != "undefined"){
			String fechaMovimiento = fecha;
			SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date date=sdf.parse(fechaMovimiento);
				movimientoFormulario.setFecha(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
			
		if(seleccionados != null && seleccionados.length()> 0){
			if(!seleccionados.equals("todos")){
				String[] listaElementosSeleccionados = seleccionados.split("\\,");
				
				for(int i = 0; i<listaElementosSeleccionados.length;i++)
				{
					for(int f =0;f<listaElementos.size();f++){
						if(listaElementosSeleccionados[i].equalsIgnoreCase(listaElementos.get(f).getCodigo())){
							listaElementos.remove(f);
							break;
						}
					}
				}
			}else{
				listaElementos.removeAll(listaElementos);
			}
			mensaje = new ScreenMessageImp("formularioMovimiento.notificacion.elementosEliminados", null);
			avisos.add(mensaje);
			hayAvisos = true;
	
		}
		
		session.setAttribute("listaElementos", listaElementos);		
		atributos.put("accion", accion);
		movimientoFormulario.setCantidadElementos(listaElementos.size());
		atributos.put("movimientoFormulario", movimientoFormulario);
		session.setAttribute("movimientoSession", movimientoFormulario);
		atributos.put("codigoLectura", codigoLectura);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);

		//hacemos el forward
		return precargaFormularioMovimiento(session, atributos, accion, id, null, null, null, null, null, null,null, null, null, null, null);
		
	}
	
		/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void validarListaCodigos(HttpSession session, String cadenaCodigosElementos,Boolean anexar,Deposito depositoActual,Map<String,Object> atributos, List<ScreenMessage> avisos)
	{
		ScreenMessage mensaje = null;
		Boolean hayAvisos = false;
		
		@SuppressWarnings("unchecked")
		List<Elemento> listaElementos = (List<Elemento>) session.getAttribute("listaElementos");
		if(listaElementos == null){
			listaElementos = new ArrayList<Elemento>();
		}
		
		Elemento elemento = null;
		Long orden = (long)0;
		String[] listaCodigos = cadenaCodigosElementos.split("\\,");
		if (listaCodigos != null) {
			
			if (anexar == null || anexar == false) {
				listaElementos.removeAll(listaElementos);
				session.setAttribute("noAnexar", true);
			}
			
		List<String> listaDescartados = new ArrayList<String>();
		Boolean banderaModulos = false, banderaInexistentes = false, banderaNoGuarda = false, banderaDepositoDiferente = false,banderaRepetido = false;
		String codigo,codigoTomado12="",codigoCorrecto = "";
		Boolean repetido, noValido;
		for (int i = 0; i < listaCodigos.length; i++) {
			repetido = false;
			noValido = false;
//			banderaModulos = false;
//			banderaInexistentes = false;
//			banderaNoGuarda = false;
//			banderaRepetido = false;
//			banderaDepositoDiferente = false;
			codigo = listaCodigos[i];
			if(codigo.length() == 13 || codigo.length() == 12){
				if(codigo.length() == 13){
					codigoTomado12 = listaCodigos[i].substring(0, 12);
					codigoCorrecto = codigoTomado12 + String.valueOf(EAN13.EAN13_CHECKSUM(codigoTomado12));
				}else{
					codigoTomado12 = codigo;
				}
				if((codigo.length() == 13 && codigo.equals(codigoCorrecto)) || (codigo.length() == 12)){
					elemento = new Elemento();
					if (codigo.startsWith("99")) {
						noValido = true;
						banderaModulos= true;
					} else {
						elemento = elementoService.getByCodigo(codigoTomado12,obtenerClienteAspUser());
						if (elemento != null) 
						{
							//if(depositoActual.getId().longValue() == elemento.getDepositoActual().getId().longValue()){
								//if(Constantes.ELEMENTO_ESTADO_EN_GUARDA.equals(elemento.getEstado()))
								//{
									//movimientoDetalle.setElemento(elemento);
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
							
						} else {
							noValido = true;
							banderaInexistentes = true;
						}
					}
						if(noValido==false){
							if(listaElementos.size()>0)
							{
								for(int f = (listaElementos.size()-1);f>=0;f--)
								{
									if(codigoTomado12.equals(listaElementos.get(f).getCodigo()))
									{
										banderaRepetido = true;
										repetido = true;
										break;
									}
								}
							}
							if(!repetido)
							{
								listaElementos.add(elemento);
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
				banderaInexistentes = true;
				listaDescartados.add(codigo+"(Código erróneo)\n");					
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
				mensaje = new ScreenMessageImp("formularioMovimiento.notificacion.elementosNoEnGuardaFiltrados", null);
				avisos.add(mensaje);
				hayAvisos = true;
			}
			if(banderaDepositoDiferente)
			{
				//Se avisa que uno o mas elementos no estan en el mismo deposito que el de origen seleccionado
				mensaje = new ScreenMessageImp("formularioMovimiento.notificacion.elementosDepositoDiferente", null);
				avisos.add(mensaje);
				hayAvisos = true;
			}
			if(banderaRepetido)
			{
				//Se avisa que uno o mas elementos no estan en el mismo deposito que el de origen seleccionado
				mensaje = new ScreenMessageImp("formularioMovimiento.notificacion.elementosRepetidos", null);
				avisos.add(mensaje);
				hayAvisos = true;
			}
		
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		session.setAttribute("listaElementos", listaElementos);
		}
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
			if("cliente".equals(movimiento.getClaseMovimiento()))
			{
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
		depositosDestinoPopupMap.put("referenciaHtml", "codigoDepositoOrigenDestino"); 		
		//url que se debe consumir con ajax
		depositosDestinoPopupMap.put("urlRequest", "precargaFormularioMovimiento.html");
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
		depositosOrigenPopupMap.put("referenciaHtml", "codigoDepositoActual"); 		
		//url que se debe consumir con ajax
		depositosOrigenPopupMap.put("urlRequest", "precargaFormularioMovimiento.html");
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
		clientesPopupMap.put("referenciaHtml", "codigoClienteEmp"); 		
		//url que se debe consumir con ajax
		clientesPopupMap.put("urlRequest", "precargaFormularioMovimiento.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clientesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		clientesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("clientesPopupMap", clientesPopupMap);
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
		lecturasPopupMap.put("urlRequest", "precargaFormularioMovimiento.html");
		//se vuelve a setear el texto utilizado para el filtrado
		lecturasPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		lecturasPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("lecturasPopupMap", lecturasPopupMap);
	}
		
		private void definirPopupResponsables(Map<String,Object> atributos, String val){
			//creo un hashmap para almacenar los parametros del popup
			Map<String,Object> responsablesPopupMap = new HashMap<String, Object>();
			//definicion de los campos a mostrar en la tabla
			//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
			List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
			campos.add(new CampoDisplayTag("id","formularioPosicionLibre.posicionLibreDetalle.lectura.codigo",false));
			campos.add(new CampoDisplayTag("persona","formularioMovimiento.responsable",false));		
			responsablesPopupMap.put("campos", campos);
			//Coleccion de objetos a utilizar en el popup
			//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
			ClienteAsp casp= obtenerClienteAspUser();
			responsablesPopupMap.put("coleccionPopup", userService.listarPopupNoPersonal(val, casp));
			//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
			responsablesPopupMap.put("referenciaPopup", "id");
			//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
			responsablesPopupMap.put("referenciaPopup2", "persona");
			//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
			responsablesPopupMap.put("referenciaHtml", "codigoResponsable"); 		
			//url que se debe consumir con ajax
			responsablesPopupMap.put("urlRequest", "precargaFormularioMovimiento.html");
			//se vuelve a setear el texto utilizado para el filtrado
			responsablesPopupMap.put("textoBusqueda", val);		
			//codigo de la localización para el titulo del popup
			responsablesPopupMap.put("tituloPopup", "textos.seleccion");
			//Agrego el mapa a los atributos
			atributos.put("responsablesPopupMap", responsablesPopupMap);
		}
	
		private void definirPopupSucursal(Map<String,Object> atributos, String val, String empresaCodigo){
			//creo un hashmap para almacenar los parametros del popup
			Map<String,Object> sucursalesPopupMap = new HashMap<String, Object>();
			//definicion de los campos a mostrar en la tabla
			//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
			List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
			campos.add(new CampoDisplayTag("codigo","formularioSucursal.datosSucursal.codigo",false));
			campos.add(new CampoDisplayTag("descripcion","formularioSucursal.datosSucursal.descripcion",false));
			//Coleccion de objetos a utilizar en el popup
			sucursalesPopupMap.put("campos", campos);		
			//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
			sucursalesPopupMap.put("coleccionPopup", sucursalService.listarSucursalPopup(val, empresaCodigo, obtenerClienteAspUser()));
			//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
			sucursalesPopupMap.put("referenciaPopup", "codigo");
			//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
			sucursalesPopupMap.put("referenciaPopup2", "descripcion");
			//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
			sucursalesPopupMap.put("referenciaHtml", "codigoSucursalOrigenDestino"); 		
			//url que se debe consumir con ajax
			sucursalesPopupMap.put("urlRequest", "precargaFormularioMovimiento.html");
			//se vuelve a setear el texto utilizado para el filtrado
			sucursalesPopupMap.put("textoBusqueda", val);
			//se setea el codigo del cliente seleccionado.
			sucursalesPopupMap.put("filterPopUp", empresaCodigo);
			//codigo de la localización para el titulo del popup
			sucursalesPopupMap.put("tituloPopup", "textos.seleccion");
			//Agrego el mapa a los atributos
			atributos.put("sucursalesPopupMap", sucursalesPopupMap);
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
}
