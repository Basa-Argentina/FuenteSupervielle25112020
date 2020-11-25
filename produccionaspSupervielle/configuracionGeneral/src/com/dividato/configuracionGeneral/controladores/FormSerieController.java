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

import com.dividato.configuracionGeneral.validadores.SerieValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipCondIvaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.CaiService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipCondIva;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.Cai;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de Serie.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 * @modificado Gonzalo Noriega (08/06/2011)
 * @modificado Victor Kenis (11/08/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioSerie.html",
				"/guardarActualizarSerie.html",
				"/eliminarCai.html"
			}
		)
public class FormSerieController {
	private SerieService serieService;
	private SerieValidator validator;
	private AfipTipoComprobanteService afipTipoComprobanteService;	
	private AfipCondIvaService afipCondIvaService;
	private ListaSeriesController listaSeriesController;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	private CaiService caiService;
	
		
	/**
	 * Setea el servicio de Serie.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto UserService.
	 * La clase SerieImp implementa Serie y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param serieService
	 */
	@Autowired
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}

	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	
	@Autowired
	public void setCaiService(CaiService caiService) {
		this.caiService = caiService;
	}
	
	@Autowired
	public void setListaSeriesController(ListaSeriesController listaSeriesController) {
		this.listaSeriesController = listaSeriesController;
	}
	
	@Autowired
	public void setAfipTipoComprobanteService(AfipTipoComprobanteService afipTipoComprobanteService) {
		this.afipTipoComprobanteService = afipTipoComprobanteService;
	}
	
	@Autowired
	public void setAfipCondIvaService(AfipCondIvaService afipCondIvaService) {
		this.afipCondIvaService = afipCondIvaService;
	}
	@Autowired
	public void setValidator(SerieValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioSerie.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Serie, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioSerie" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioSerie.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioSerie(
			@RequestParam(value="accion",required=false) String accion,			
			@RequestParam(value="id",required=false) String id,			
			Map<String,Object> atributos, HttpSession session) {
		if(session != null){
			session.setAttribute("idSerie", id);
		}
		Serie serieFormulario;
		if(accion==null) accion="NUEVO"; //acción por defecto: nuevo		
		if(!accion.equals("NUEVO")){
//			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
//			boolean hayAvisos = false;
//			boolean hayAvisosNeg = false;
//			ScreenMessage mensaje;
			serieFormulario = serieService.getByID(Long.valueOf(id));
//			Empresa empresa = empresaService.obtenerPorId(serieFormulario.getEmpresa().getId());
//			if(empresa.getSerie1() != null || empresa.getSerie2() != null)
//			{
//				if(empresa.getSerie1().getId().equals(serieFormulario.getId()) || empresa.getSerie2().getId().equals(serieFormulario.getId()))
//				{
//					mensaje = new ScreenMessageImp("formularioSerie.errorModificarSerie1PorDefecto", null);
//					hayAvisosNeg = true;
//					avisos.add(mensaje);
//					atributos.put("hayAvisosNeg", hayAvisosNeg);
//					atributos.put("hayAvisos", hayAvisos);
//					atributos.put("avisos", avisos);
//					return listaSeriesController.mostrarSerie(session, atributos);
//				}
//			}
			//serieFormulario.setBooleanCondIvaClientes();
			
				
		}else{
			serieFormulario = new Serie();
			/*
			 * 
			 *  109	02/01/12	Pendiente	Series	"En la alta proponer el código siguiente.
			 *	VERIFICAR EL RESTO DE LOS ABM Y SIEMPRE PROPONER EL CODIGO SIGUIENTE CUANDO CORRESPONDA."
			 * 
			 */
			// Buscar numero de codigo y sumarle 1
			List<Serie> series = serieService.listarSerieFiltradas(null, obtenerClienteAspUser());
			try{
				if(!series.isEmpty()){
					Serie s = (Serie)series.get(series.size()-1);
					Long codigo = Long.valueOf(s.getCodigo())+1;
					serieFormulario.setCodigo(codigo.toString());
				}
			}catch(Exception e){
				e.getStackTrace();
			}
		}
		atributos.put("serieFormulario", serieFormulario);		
		// busco los tipos de comprobantes del afip		
		List<AfipTipoComprobante> afipTipoComprobantes = afipTipoComprobanteService.listarTodos();
		atributos.put("afipTipoComprobantes", afipTipoComprobantes);
		
		Cai caiFil = new Cai();
		List<Cai> cais = null;
		if(id != null){
			caiFil.setIdSerie(Long.valueOf(id));
			cais = caiService.listarCaiFiltradas(caiFil, obtenerClienteAspUser());
		}
		atributos.put("cais", cais);
		
		// busco las empresas
		List<Empresa> empresas = empresaService.listarEmpresaFiltradasConSucursales(null, obtenerClienteAspUser());
		atributos.put("empresas", empresas);	
		
		//Busco los Iva.
		List<AfipCondIva> afipCondIvas = afipCondIvaService.listarTodos();
		atributos.put("afipCondIvas", afipCondIvas);
		

		//Seteo la accion actual
		atributos.put("accion", accion);
	
		//Se realiza el redirect
		return "formularioSerie";
	}
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos User.
	 * 
	 * @param Serie user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto Serie con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioSerie" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarSerie.html",
			method= RequestMethod.POST
		)
	public String guardarSerie(
			@RequestParam(value="accion",required=false) String accion,			
			@ModelAttribute("serieFormulario") final Serie serieFormulario,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		else{
			accion="MODIFICACION";
		}
		if(!result.hasErrors()){
			serieFormulario.setAccion(accion);
			serieFormulario.setCliente(obtenerClienteAspUser());
			Empresa empresaSel = empresaService.obtenerPorId(serieFormulario.getIdEmpresa());
			serieFormulario.setEmpresa(empresaSel); // seteo el mismo en la empresa.
			
			validator.validate(serieFormulario,result);
		}
				
		//Si la validacion sale exitosa comienzo el proceso de registro en BD
		Serie serie;
		if(!result.hasErrors()){			
			//obtengo la empresa seleccionada seleccionada.
			Empresa empresaSel = empresaService.obtenerPorId(serieFormulario.getIdEmpresa());
			serieFormulario.setEmpresa(empresaSel); // seteo el mismo en la empresa.
			
			//obtengo la sucursal seleccionada
			Sucursal sucursalSel = sucursalService.obtenerPorId(serieFormulario.getIdSucursal());
			serieFormulario.setSucursal(sucursalSel); // seteo el mismo en la sucursal.
			
			if(serieFormulario.getIdAfipTipoComprobante() != null){
				//obtengo el afipTipoComprobante seleccionado
				AfipTipoComprobante afipTipoComprobanteSel = afipTipoComprobanteService.obtenerPorId(serieFormulario.getIdAfipTipoComprobante());
				serieFormulario.setAfipTipoComprobante(afipTipoComprobanteSel); // seteo el mismo en la afipTipoComprobante.
			}
			
			// ALGORITMO PARA GUARDAR LAS ABREVIATURAS DE IVA.
			//serieFormulario.utilsCondIvaClientes(); // Por el momento se cambio la forma de guardar el IVA
			
			if(accion.equals("NUEVO")){
				serie = serieFormulario;								
				//Se guarda la serie en la BD
				commit = serieService.guardarSerie(serie);
			}else{
				serie = serieService.obtenerPorCodigo(serieFormulario.getCodigo(), obtenerClienteAspUser());
				setData(serie, serieFormulario);
				//Se Actualiza el cliente en la BD
				commit = serieService.actualizarSerie(serie);
			}			
			if(commit == null || !commit)
				serieFormulario.setId(serie.getId());
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("serieFormulario", serieFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioSerie(accion, serieFormulario.getId() !=null ? serieFormulario.getId().toString() : null, atributos, session);
		}
		
		if(result.hasErrors()){
			atributos.put("serieFormulario", serieFormulario);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioSerie(accion, serieFormulario.getId() !=null ? serieFormulario.getId().toString() : null, atributos, session);
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeSerieReg = new ScreenMessageImp("formularioSerie.notificacion.serieRegistrada", null);
			avisos.add(mensajeSerieReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
		}
		return listaSeriesController.mostrarSerie(session, atributos);
	}
	
	private void setData(Serie serie, Serie data){
		if(data != null){			
			serie.setCodigo(data.getCodigo());
			serie.setDescripcion(data.getDescripcion());
			serie.setHabilitado(data.getHabilitado());
			serie.setAfipTipoComprobante(data.getAfipTipoComprobante());
			serie.setEmpresa(data.getEmpresa());
			serie.setSucursal(data.getSucursal());
			serie.setPrefijo(data.getPrefijo());
			serie.setTipoSerie(data.getTipoSerie());
			serie.setCondIvaClientes(data.getCondIvaClientes());
			serie.setUltNroImpreso(data.getUltNroImpreso());
			serie.setCliente(data.getCliente());
		}
	
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Cai.
	 * 
	 * @param idCai el id de Empresa a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarCai.html",
			method = RequestMethod.GET
		)
	public String eliminarCai(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos,
			@RequestParam(value="accion",required=false) String accion) {
		String idSerie = session.getAttribute("idSerie").toString();
		
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos el cai para eliminar luego
		Cai cai = caiService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la serie
		commit = caiService.eliminarCai(cai);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.cai.caiEliminado", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return precargaFormularioSerie(accion, idSerie, atributos, session);
	}
	
}
