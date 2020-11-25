package com.dividato.configuracionGeneral.controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
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

import com.dividato.configuracionGeneral.validadores.RemitoBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.PlantillaFacturacion;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.Constantes;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Remito.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Victor Kenis
 *
 */
@Controller
@RequestMapping(
		value=
			{	
				"/iniciarRemito.html",
				"/mostrarRemito.html",
				"/mostrarRemitosSinFiltrar.html",
				"/eliminarRemito.html",
				"/filtrarRemito.html",
				"/borrarFiltrosRemito.html",
				"/imprimirRemito.html",
				"/cambiarEstadoRemitos.html"
			}
		)
public class ListaRemitosController {
	
	private RemitoService remitoService;
	private RemitoBusquedaValidator validator;
	private DepositoService depositoService;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	private SerieService serieService;
	private TransporteService transporteService;
	private ClienteEmpService clienteEmpService;
	private ReferenciaService referenciaService;
	
	@Autowired
	public void setTransporteService(TransporteService transporteService) {
		this.transporteService = transporteService;
	}
	@Autowired
	public void setRemitoService(RemitoService remitoService) {
		this.remitoService = remitoService;
	}
	@Autowired
	public void setValidator(RemitoBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@Autowired
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
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
	public void setSerieService(SerieService serieService) {
		this.serieService = serieService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
	
	
	@RequestMapping(
			value="/iniciarRemito.html",
			method = RequestMethod.GET
		)
	public String iniciarRemito(HttpSession session,
			Map<String,Object> atributos){
		
		session.removeAttribute("remitoBusqueda");
		session.removeAttribute("remitosSession");
		atributos.remove("remitos");
		
		return "redirect:mostrarRemitosSinFiltrar.html";
	}
	
	@RequestMapping(
			value="/mostrarRemitosSinFiltrar.html",
			method = RequestMethod.GET
		)
	public String mostrarRemitosSinFiltrar(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="val", required=false) String valCliente,
			@RequestParam(value="val", required=false) String valSerie,
			@RequestParam(value="val", required=false) String valTransporte,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo){
		
		//buscamos en la base de datos y lo subimos a request.
		@SuppressWarnings("unchecked")
		List<Remito> remitos = (List<Remito>)session.getAttribute("remitosSession"); 
		if(remitos==null){
			remitos = new ArrayList<Remito>();
		}
		
		Remito remito = (Remito) session.getAttribute("remitoBusqueda");
		if(remito == null){
			remito = new Remito();
			remito.setCodigoEmpresa(empresaCodigo);
			remito.setCodigoSucursal(sucursalCodigo);
			remito.setCodigoCliente(clienteCodigo);
		}
		
		session.setAttribute("remitosSession", remitos);
		atributos.put("remitos",remitos);
		
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("size", Integer.valueOf(0));
		
		definirPopupEmpresa(atributos, valEmpresa);
		definirPopupSucursal(atributos, valSucursal, empresaCodigo);
		definirPopupDepositosOrigen(atributos, valDeposito, sucursalCodigo);
		definirPopupCliente(atributos, valCliente, empresaCodigo);
		definirPopupSerie(atributos, valSerie, empresaCodigo);
		definirPopupTransporte(atributos, valTransporte, empresaCodigo);

		//hacemos el forward
		return "consultaRemito";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de Remito y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaRemito" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarRemito.html",
			method = RequestMethod.GET
		)
	public String mostrarRemito(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="val", required=false) String valCliente,
			@RequestParam(value="val", required=false) String valSerie,
			@RequestParam(value="val", required=false) String valTransporte,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo,
			HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		List<Remito> remitos = null;	
		
		Remito remito = (Remito) session.getAttribute("remitoBusqueda");
		if(remito != null){		
		//consulto en la base de datos
			Integer size = remitoService.contarObtenerPor(remito, obtenerClienteAspUser());
			atributos.put("size",size);
			
			//paginacion y orden de resultados de displayTag
			remito.setTamañoPagina(20);		
			String nPaginaStr= request.getParameter((new ParamEncoder("remito").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("remito").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			String fieldOrder = request.getParameter( new ParamEncoder("remito").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			remito.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("remito").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			remito.setSortOrder(sortOrder);
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			remito.setNumeroPagina(nPagina);
			
			//Se busca en la base de datos los plantillasFacturacion con los filtros de paginacion agregados a la plantillaFacturacion
			remitos =(List<Remito>) remitoService.obtenerPor(remito,obtenerClienteAspUser(),remito.getFieldOrder(),
					remito.getSortOrder(),remito.getNumeroPagina(),remito.getTamañoPagina());
			
		}else{
			remito = new Remito();
			Integer size = 0;
			atributos.put("size",size);
		}
		
		session.setAttribute("remitosSession", remitos);
		atributos.put("remitos", remitos);
		
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		
		definirPopupEmpresa(atributos, valEmpresa);
		definirPopupSucursal(atributos, valSucursal, empresaCodigo);
		definirPopupDepositosOrigen(atributos, valDeposito, sucursalCodigo);
		definirPopupCliente(atributos, valCliente, empresaCodigo);
		definirPopupSerie(atributos, valSerie, empresaCodigo);
		definirPopupTransporte(atributos, valTransporte, empresaCodigo);
		
		//hacemos el forward
		return "consultaRemito";
	}
	
	@RequestMapping(
			value="/cambiarEstadoRemitos.html",
			method = RequestMethod.POST
		)
	public String cambiarEstadoRemitos(
			HttpSession session,
			@RequestParam(value="selectedSel",required=false) String selectedSel,
			@RequestParam(value="estadoRemito",required=false) String estadoSel,
			Map<String,Object> atributos,
			HttpServletRequest request){
		List<Remito> remitos = new ArrayList<Remito>();
		ScreenMessage mensaje = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Boolean hayAvisos = false;
		Boolean hayAvisosNeg = false;
		Boolean commit = false;
		String selIds[] = null;
		
		if(selectedSel != null && !"".equals(selectedSel)){
			//Obtenemos las posiciones por id's
			selIds = selectedSel.split(",");
			if(selIds != null && selIds.length > 0){
				for(String idRemito : selIds){
					Remito remito = remitoService.obtenerPorId(Long.valueOf(idRemito));
					if(remito != null)
						remitos.add(remito);
				}
			}
			//recorremos las posiciones seleccionadas y cambiamos el estado
			for(Remito update: remitos){
				update.setEstado(estadoSel);			
			}
			commit = remitoService.actualizarRemitoList(remitos);
					
			if(commit){
				mensaje = new ScreenMessageImp("formularioRemito.notificacion.remitoActualizado", null);
				hayAvisos = true;
			}else{
				mensaje = new ScreenMessageImp("error.resumirMovimientoDataBase", null);
				hayAvisosNeg = true;
			}
		}else{
			mensaje = new ScreenMessageImp("notif.posicion.seleccionPosicion", null);
			hayAvisosNeg = true;
		}
		
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		
		return mostrarRemito(session, atributos, null, null, null, null, null, null, null,null,null,request);
	}
	
	@RequestMapping(
			value="/filtrarRemito.html",
			method = RequestMethod.POST
		)
	public String filtrarRemito(@ModelAttribute("remitoBusqueda") Remito remito, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos, HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(remito, result);
		if(!result.hasErrors()){
			session.setAttribute("remitoBusqueda", remito);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		return mostrarRemito(session, atributos, null, null, null, null, null, null,null, null, null, request);
	}
	
	
	@RequestMapping(
			value="/borrarFiltrosRemito.html",
			method = RequestMethod.GET
		)
	public String filtrarRemito(HttpSession session){
		session.removeAttribute("remitoBusqueda");
		return "redirect:mostrarRemito.html";
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
			value="/eliminarRemito.html",
			method = RequestMethod.GET
		)
	public String eliminarRemito(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos, HttpServletRequest request) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la remito para eliminar luego
		Remito remito = remitoService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la remito
		commit = remitoService.eliminarRemito(remito);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("formularioRemito.notificacion.remitoEliminadoExito", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarRemito(session, atributos, null, null, null, null, null, null, null, null, null,request);
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de imprimir los codigos de barra de los elementos en un reporte y subirlo al response.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaImpresionEtiqueta" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(
			value="/imprimirRemito.html",
			method = RequestMethod.GET
		)
	public void imprimirRemito(HttpSession session,
			Map<String,Object> atributos,			
			HttpServletResponse response,
			@RequestParam(value="seleccionados", required=false) String seleccionados) {
				
		if(seleccionados != null && seleccionados.length()> 0){
			
			String[] ids = seleccionados.split(",");
			Long[] idsLong = new Long[ids.length];
			for(int i = 0;i<ids.length;i++)
				idsLong[i]=Long.valueOf(ids[i]);
			HashMap<String,Object> params = new HashMap<String,Object>();
			byte[] pdfByteArray ;
			ServletOutputStream op = null;
			try{
				//se trae busca nuevamente la misma lista en la base pero esta vez con los detales en FetchMode.Join
				List<Remito> remitosYDetalles = remitoService.listarRemitosPorId(idsLong, obtenerClienteAspUser());
				List<Remito> remitosYDetallesFinal = new ArrayList<Remito>();	
					for (int i=0;i<remitosYDetalles.size();i++){
						// Recorro los detalles
						int xn = 0;
						Date defaultValue = null;
			            DateConverter converter = new DateConverter (defaultValue);
			            ConvertUtils.register (converter, java.util.Date.class);
						
						Set<RemitoDetalle> detalles = new HashSet<RemitoDetalle>();
						for(Iterator it = remitosYDetalles.get(i).getDetalles().iterator(); it.hasNext(); ) { 
							RemitoDetalle x = (RemitoDetalle)it.next();
							System.out.println("Nro: "+xn+ x.getElemento().getCodigo());
							xn++;
							Referencia r = referenciaService.obtenerByElemento(x.getElemento());
							if(r!=null){
								String numero = "";
								String texto = "";
								if(r.getNumero1()!=null)
									numero = r.getNumero1Str().equalsIgnoreCase(r.getNumero2Str())?r.getNumero1Str():r.getNumero1Str()+" "+r.getNumero2Str();
								if(r.getTexto1()!=null)
									texto = r.getTexto1().equalsIgnoreCase(r.getTexto2())?r.getTexto1():r.getTexto1()+" "+r.getTexto2();
								x.setReferencia(numero+" "+texto);
							}	
							detalles.add(x);
							if(xn==8){
								Set<RemitoDetalle> detallesCopy = new HashSet<RemitoDetalle>();
								for(Iterator itc = detalles.iterator(); itc.hasNext(); ) { 
									detallesCopy.add((RemitoDetalle)itc.next());
								}
								remitosYDetalles.get(i).setDetalles(detallesCopy);
								Remito remito = new Remito();
								BeanUtils.copyProperties(remito, remitosYDetalles.get(i));
								remitosYDetallesFinal.add(remito);
								xn=0;
								detalles.removeAll(detalles);
							}
						}
						Set<RemitoDetalle> detallesCopy = new HashSet<RemitoDetalle>();
						for(Iterator itc = detalles.iterator(); itc.hasNext(); ) { 
							detallesCopy.add((RemitoDetalle)itc.next());
						}
						remitosYDetalles.get(i).setDetalles(detallesCopy);
						Remito remito = new Remito();
						BeanUtils.copyProperties(remito, remitosYDetalles.get(i));
						remitosYDetallesFinal.add(remito);
						xn=0;
						detalles.removeAll(detalles);
					}
					
				//se crea el data source
				JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(remitosYDetallesFinal);
				//se crea el reporte
				JasperReport jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+Constantes.REPORTE_REMITO);
				JasperReport subReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+Constantes.REPORTE_REMITO_DETALLE);
				params.put("subreporte_detalle", subReport);
				pdfByteArray = JasperRunManager.runReportToPdf(jasperReport, params,ds);
				//se envia el reporte 
				response.setContentType("application/pdf");
                //response.setHeader( "Content-disposition", "attachment; filename=reporte.pdf");

				op = null;
				op = response.getOutputStream();
				op.write(pdfByteArray, 0, pdfByteArray.length);
				op.flush();
				op.close();
			} catch (IOException e) {
				try {
					op.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//@SuppressWarnings("unchecked")
		//List<Remito> remitos = (List<Remito>)session.getAttribute("remitosSession");
				
		//if(remitos==null){
			//remitos=new ArrayList<Remito>();
		//}
	
	}
	
	/////////////////////METODOS DE SOPORTE/////////////////////////////
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
		seriesPopupMap.put("coleccionPopup", serieService.listarSeriePopup(val, "R", null, obtenerClienteAspUser(), empresa));
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
	
	private void definirPopupCliente(Map<String,Object> atributos, String val, String empresaCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clientesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("razonSocial.razonSocial","formularioClienteDireccion.cliente.razonSocial",false));
		campos.add(new CampoDisplayTag("nombre","formularioClienteDireccion.cliente.nombre",false));
		campos.add(new CampoDisplayTag("apellido","formularioClienteDireccion.cliente.apellido",false));
		campos.add(new CampoDisplayTag("nombreYApellido","formularioClienteDireccion.cliente.apellido",true));
		campos.add(new CampoDisplayTag("codigo","formularioClienteDireccion.cliente.apellido",true));
		clientesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		clientesPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup(val, empresaCodigo, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup2", "nombreYApellido");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clientesPopupMap.put("referenciaHtml", "codigoCliente"); 		
		//url que se debe consumir con ajax
		clientesPopupMap.put("urlRequest", "mostrarClienteDireccion.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clientesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		clientesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("clientesPopupMap", clientesPopupMap);
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
		sucursalesPopupMap.put("referenciaHtml", "codigoSucursal"); 		
		//url que se debe consumir con ajax
		sucursalesPopupMap.put("urlRequest", "mostrarStock.html");
		//se vuelve a setear el texto utilizado para el filtrado
		sucursalesPopupMap.put("textoBusqueda", val);
		//se setea el codigo del cliente seleccionado.
		sucursalesPopupMap.put("filterPopUp", empresaCodigo);
		//codigo de la localización para el titulo del popup
		sucursalesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("sucursalesPopupMap", sucursalesPopupMap);
	}
	
	private void definirPopupEmpresa(Map<String,Object> atributos, String val){
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
		empresasPopupMap.put("coleccionPopup", empresaService.listarEmpresaPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		empresasPopupMap.put("referenciaHtml", "codigoEmpresa");
		//url que se debe consumir con ajax
		empresasPopupMap.put("urlRequest", "mostrarStock.html");
		//se vuelve a setear el texto utilizado para el filtrado
		empresasPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		empresasPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("empresasPopupMap", empresasPopupMap);
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
		transportesPopupMap.put("coleccionPopup", transporteService.listarTransportePopup(val, codigoEmpresa, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		transportesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		transportesPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		transportesPopupMap.put("referenciaHtml", "codigoTransporte");
		//url que se debe consumir con ajax
		transportesPopupMap.put("urlRequest", "mostrarRemito.html");
		//se vuelve a setear el texto utilizado para el filtrado
		transportesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		transportesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("transportesPopupMap", transportesPopupMap);
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}