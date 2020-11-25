package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
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

import com.dividato.configuracionGeneral.validadores.LoteFacturacionBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.hibernate.FacturaServiceImp;
import com.security.accesoDatos.configuraciongeneral.hibernate.PreFacturaDetalleServiceImp;
import com.security.accesoDatos.configuraciongeneral.hibernate.PreFacturaServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteFacturacionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PreFacturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PreFacturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SeccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.FacturaDetalle;
import com.security.modelo.configuraciongeneral.LoteFacturacion;
import com.security.modelo.configuraciongeneral.PreFactura;
import com.security.modelo.configuraciongeneral.PreFacturaDetalle;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.Constantes;
import com.security.utils.ParseNumberUtils;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de LoteFacturacion.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gonzalo Noriega
 *
 */
@Controller
@RequestMapping(
		value=
			{	
				"/iniciarLoteFacturacion.html",
				"/mostrarLoteFacturacion.html",
				"/filtrarLoteFacturacion.html",
				"/eliminarLoteFacturacion.html",
				"/mostrarLoteFacturacionSinFiltrar.html",
				"/cambiarEstadoLoteFacturacion.html",
				"/facturarLoteFacturacion.html"
			}
		)
public class ListaLoteFacturacionController {
	
	private LoteFacturacionService loteFacturacionService;
	private LoteFacturacionBusquedaValidator validator;

	@Autowired
	public void setLoteFacturacionService(LoteFacturacionService loteFacturacionService) {
		this.loteFacturacionService = loteFacturacionService;
	}
	@Autowired
	public void setValidator(LoteFacturacionBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	/**
	 * Setea el servicio de Depositos.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto LoteFacturacionService.
	 * La clase DepositoServiceImp implementa Deposito y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param depositoService
	 */

	
	
	@RequestMapping(
			value="/iniciarLoteFacturacion.html",
			method = RequestMethod.GET
		)
	public String iniciarLoteFacturacion(HttpSession session,
			Map<String,Object> atributos){
		session.removeAttribute("loteFacturacionBusqueda");
		session.removeAttribute("loteFacturacionSession");
		atributos.remove("loteFacturacion");
		session.removeAttribute("tablaPaginada");
		return "redirect:mostrarLoteFacturacionSinFiltrar.html";
	}
	
	
	@RequestMapping(
			value="/mostrarLoteFacturacionSinFiltrar.html",
			method = RequestMethod.GET
		)
	public String mostrarLoteFacturacionSinFiltrar(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valSeccion,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			@RequestParam(value="depositoCodigo", required=false) String depositoCodigo){
		
		//buscamos en la base de datos y lo subimos a request.
		@SuppressWarnings("unchecked")
		List<LoteFacturacion> lotesFacturacion = (List<LoteFacturacion>)session.getAttribute("lotesFacturacionSession"); 
		if(lotesFacturacion==null){
			lotesFacturacion = new ArrayList<LoteFacturacion>();
		}
		
		LoteFacturacion loteFacturacion = (LoteFacturacion) session.getAttribute("loteFacturacionBusqueda");
		if(loteFacturacion == null){
			loteFacturacion = new LoteFacturacion();
			loteFacturacion.setCodigoEmpresa(empresaCodigo);
			loteFacturacion.setCodigoSucursal(sucursalCodigo);
		}
		
		session.setAttribute("lotesFacturacionSession", lotesFacturacion);
		atributos.put("lotesFacturacion",lotesFacturacion);
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("size", Integer.valueOf(0));
		

		//hacemos el forward
		return "consultaLoteFacturacion";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de LoteFacturacion y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaLoteFacturacion" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarLoteFacturacion.html",
			method = RequestMethod.GET
		)
	public String mostrarLoteFacturacion(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valSeccion,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		List<LoteFacturacion> lotesFacturacion = null;
		
		 
		 LoteFacturacion loteFacturacion = (LoteFacturacion) session.getAttribute("loteFacturacionBusqueda");
		 if(loteFacturacion == null)
		 {
			 loteFacturacion = new LoteFacturacion();
		 }
					
			//cuenta la cantidad de resultados
			Long size = loteFacturacionService.contarObtenerPor(obtenerClienteAspUser(), loteFacturacion.getCodigoEmpresa(), loteFacturacion.getCodigoSucursal(), 
					loteFacturacion.getFechaDesde(), loteFacturacion.getFechaHasta(), loteFacturacion.getEstado());
			atributos.put("size", size.intValue());
			
			//paginacion y orden de resultados de displayTag
			loteFacturacion.setTamanoPagina(20);		
			String nPaginaStr= request.getParameter((new ParamEncoder("loteFacturacion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("loteFacturacion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			String fieldOrder = request.getParameter( new ParamEncoder("loteFacturacion").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			loteFacturacion.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("loteFacturacion").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			loteFacturacion.setSortOrder(sortOrder);
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			loteFacturacion.setNumeroPagina(nPagina);
			
			//Se busca en la base de datos los lotesFacturacion con los filtros de paginacion agregados a la loteFacturacion
			lotesFacturacion =(List<LoteFacturacion>) loteFacturacionService.obtenerPor(obtenerClienteAspUser(), loteFacturacion.getCodigoEmpresa(), loteFacturacion.getCodigoSucursal(), 
					loteFacturacion.getFechaDesde(), loteFacturacion.getFechaHasta(), loteFacturacion.getEstado(), fieldOrder, sortOrder, loteFacturacion.getNumeroPagina(), loteFacturacion.getTamanoPagina());
								
		
		session.setAttribute("lotesFacturacionSession", lotesFacturacion);
		atributos.put("lotesFacturacion", lotesFacturacion);
		
		atributos.put("clienteId", obtenerClienteAspUser().getId());
	
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);
	
	
		//hacemos el forward
		return "consultaLoteFacturacion";
	}
	
	@RequestMapping(
			value="/filtrarLoteFacturacion.html",
			method = RequestMethod.POST
		)
	public String filtrarLoteFacturacion(@ModelAttribute("loteFacturacionBusqueda") LoteFacturacion loteFacturacion, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(loteFacturacion, result);
		if(!result.hasErrors()){
			session.setAttribute("loteFacturacionBusqueda", loteFacturacion);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		return mostrarLoteFacturacion(session,atributos, null, null, null, null, null, null, request);
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Remito.
	 * 
	 * @param idRemito el id de Remito a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarLoteFacturacion.html",
			method = RequestMethod.GET
		)
	public String eliminarLoteFacturacion(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos,
			HttpServletRequest request) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la remito para eliminar luego
		LoteFacturacion loteFacturacion = loteFacturacionService.obtenerPorId(Long.valueOf(id));
		if(loteFacturacion!= null && (loteFacturacion.getEstado().equalsIgnoreCase("Pendiente") ||
				loteFacturacion.getEstado().equalsIgnoreCase("Anulado"))){
			//Eliminamos el Lote de Facturacion
			commit = loteFacturacionService.eliminarLoteFacturacion(loteFacturacion);
			ScreenMessage mensaje;
			//Controlamos su eliminacion.
			if(commit){
				mensaje = new ScreenMessageImp("formularioLoteFacturacion.notificacion.loteFacturacionEliminado", null);
				hayAvisos = true;
			}else{
				mensaje = new ScreenMessageImp("error.deleteDataBase", null);
				hayAvisosNeg = true;
			}
			avisos.add(mensaje);
		}
		else
		{
			ScreenMessage mensaje;
			mensaje = new ScreenMessageImp("formularioLoteFacturacion.errorEliminar", null);
			hayAvisosNeg = true;
			avisos.add(mensaje);
		}
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarLoteFacturacion(session, atributos, null, null, null, null, null, null, request);
	}
	
	
	@RequestMapping(
			value="/facturarLoteFacturacion.html",
			method = RequestMethod.GET
		)
	public String facturarLoteFacturacion(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos,
			HttpServletRequest request) {
		
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		
		List<Factura> listaFacturas = new ArrayList<Factura>();
		
		Serie serieA = null;
		Serie serieB = null;
		Long ultimoNumeroA = new Long(0);
		Long ultimoNumeroB = new Long(0);
		Long numero = null;
		//Traemos el lote seleccionado de la base
		LoteFacturacion loteFacturacion = loteFacturacionService.obtenerPorId(Long.valueOf(id));
		//Preguntamos si existe y si no fue ya facturado anteriormente
		if(loteFacturacion!= null && (loteFacturacion.getEstado().equalsIgnoreCase("Pendiente") ||
				loteFacturacion.getEstado().equalsIgnoreCase("Anulado"))){
			
			loteFacturacion.setEstado("Facturado");
			
			PreFacturaService service = new PreFacturaServiceImp(HibernateControl.getInstance());
			//Se traen las prefacturas del lote de la base
			List<PreFactura> listaPreFactura = service.listarPreFacturasPorLoteFacturacion(loteFacturacion, obtenerClienteAspUser());
			if(listaPreFactura!= null && listaPreFactura.size()>0){
					
				PreFacturaDetalleService serviceDetalle = new PreFacturaDetalleServiceImp(HibernateControl.getInstance());
				
				for(PreFactura preFactura:listaPreFactura){
					
					preFactura.setEstado("Facturado");
					
					if(preFactura.getAfipTipoDeComprobante().getId().longValue()==1)
					{
						serieA = preFactura.getSerie();
						if(ultimoNumeroA==0)
							ultimoNumeroA = ParseNumberUtils.parseLongCodigo(preFactura.getSerie().getUltNroImpreso());
						numero = ultimoNumeroA;
					}
					else
					{	
						serieB = preFactura.getSerie();
						if(ultimoNumeroB==0)
							ultimoNumeroB = ParseNumberUtils.parseLongCodigo(preFactura.getSerie().getUltNroImpreso());
						numero = ultimoNumeroB;
					}

					
					
					Factura factura = new Factura();
					List<FacturaDetalle> listaFacturaDetalles = new ArrayList<FacturaDetalle>();
					//Por cada Prefactura traemos sus detalles de la base y se los seteamos
					List<PreFacturaDetalle> listaDetalles = serviceDetalle.listarPreFacturaDetallesPorPreFactura(preFactura.getId(), obtenerClienteAspUser());
					
					if(listaDetalles!= null && listaDetalles.size()>0){
						
						preFactura.setDetalles(listaDetalles);
						
						
						for(PreFacturaDetalle detalle:listaDetalles){

							FacturaDetalle facturaDetalle = new FacturaDetalle();
							facturaDetalle.setCantidad(detalle.getCantidad());
							if(detalle.getConceptoFacturable()!=null)
								facturaDetalle.setConceptoFacturable(detalle.getConceptoFacturable());
							if(detalle.getListaprecios()!=null)
								facturaDetalle.setListaprecios(detalle.getListaprecios());
							facturaDetalle.setCosto(detalle.getCosto());
							facturaDetalle.setDescripcion(detalle.getDescripcion());
							facturaDetalle.setFinalTotal(detalle.getFinalTotal());
							facturaDetalle.setFinalUnitario(detalle.getFinalUnitario());
							facturaDetalle.setImpuestoTotal(detalle.getImpuestoTotal());
							facturaDetalle.setImpuestoUnitario(detalle.getImpuestoUnitario());
							facturaDetalle.setIVA(detalle.getIVA());
							facturaDetalle.setNetoTotal(detalle.getNetoTotal());
							facturaDetalle.setNetoUnitario(detalle.getNetoUnitario());
							facturaDetalle.setPrecioBase(detalle.getPrecioBase());
							facturaDetalle.setOrden(detalle.getOrden());
							
							listaFacturaDetalles.add(facturaDetalle);
							
							detalle.setEstado("Facturado");
							detalle.setFactura(factura);
						}
						
						Set<FacturaDetalle> setFacturaDetalles = new HashSet<FacturaDetalle>(listaFacturaDetalles);
						factura.setDetallesFactura(setFacturaDetalles);
						factura.setAfipTipoDeComprobante(preFactura.getAfipTipoDeComprobante());
						factura.setClienteAsp(obtenerClienteAspUser());
						factura.setClienteEmp(preFactura.getClienteEmp());
						factura.setEmpresa(preFactura.getEmpresa());
						factura.setEstado("PENDIENTE");
						factura.setFecha(loteFacturacion.getFechaFacturacion());
						factura.setImpreso(false);
						factura.setLetraComprobante(preFactura.getAfipTipoDeComprobante().getLetra());
						factura.setSerie(preFactura.getSerie());
						factura.setPrefijoSerie(preFactura.getSerie().getPrefijo());
						
						
						//Se valida que el numero de la factura no exista ya.
						FacturaService facturaService = new FacturaServiceImp(HibernateControl.getInstance());
						Boolean existe = true;
						do{
							numero++;
							existe = facturaService.verificarExistenciaFactura(preFactura.getSerie(), numero);
						}
						while(existe==true);
							
						factura.setNumeroComprobante(numero);
						if(preFactura.getAfipTipoDeComprobante().getId().longValue()==1)
						{
							ultimoNumeroA=numero;
							serieA.setUltNroImpreso(ParseNumberUtils.parseStringCodigo(ultimoNumeroA, 8));
						}
						else
						{
							ultimoNumeroB=numero;
							serieB.setUltNroImpreso(ParseNumberUtils.parseStringCodigo(ultimoNumeroB, 8));
						}
						factura.setSucursal(preFactura.getSucursal());
						factura.setTotalFinal(preFactura.getTotalFinal());
						factura.setTotalIVA(preFactura.getTotalIVA());
						factura.setTotalNeto(preFactura.getTotalNeto());
						factura.setLoteFacturacion(loteFacturacion);
						factura.setNotasFacturacion(preFactura.getNotasFacturacion());
						
						listaFacturas.add(factura);
					}
					else
					{
						ScreenMessage mensaje = new ScreenMessageImp("formularioLoteFacturacion.errorPreFacturaVacia", null);
						avisos.add(mensaje); //agrego el mensaje a la coleccion
						atributos.put("errores", false);
						atributos.remove("result");
						atributos.put("hayAvisos", false);
						atributos.put("hayAvisosNeg", true);
						atributos.put("avisos", avisos);
						return mostrarLoteFacturacion(session, atributos, null, null, null, null, null, null, request);
					}

				}
				
				Set<PreFactura> setPreFactura = new HashSet<PreFactura>(listaPreFactura);
				loteFacturacion.setDetalles(setPreFactura);
				
				//Una vez que se tienen todas las facturas hechas
				commit = loteFacturacionService.facturarLoteFacturacion(loteFacturacion, listaFacturas,serieA,serieB);
				
				if(commit != null && !commit){
					ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
					avisos.add(mensaje); //agrego el mensaje a la coleccion
					atributos.put("errores", false);
					atributos.remove("result");
					atributos.put("hayAvisos", false);
					atributos.put("hayAvisosNeg", true);
					atributos.put("avisos", avisos);
					return mostrarLoteFacturacion(session, atributos, null, null, null, null, null, null, request);
				}
				//Genero las notificaciones
				ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioLoteFacturacion.notificacion.loteFacturadoExitosamente", null);
				avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", true);
				atributos.put("avisos", avisos);
				
			}
			else
			{
				ScreenMessage mensaje = new ScreenMessageImp("formularioLoteFacturacion.errorLoteVacio", null);
				avisos.add(mensaje); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
			}

		}
		else{
			ScreenMessage mensaje = new ScreenMessageImp("formularioLoteFacturacion.errorLoteFacturado", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
		}
		
		
		return mostrarLoteFacturacion(session, atributos, null, null, null, null, null, null, request);
	}

	/////////////////////METODOS DE SOPORTE/////////////////////////////
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
}