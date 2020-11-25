package com.dividato.configuracionGeneral.controladores;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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

import com.dividato.configuracionGeneral.validadores.FacturaBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.Constantes;
import com.security.utils.ParseNumberUtils;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Impresion de Etiquetas.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta notación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Luciano  de Asteinza
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/iniciarListaComprobantesFactura.html",
				"/mostrarListaComprobantesFactura.html",
				"/filtrarListaComprobantesFactura.html",
				"/eliminarFactura.html",
				"/imprimirFactura.html",
				"/anularFactura.html"
			}
		)
public class ListaComprobantesFacturaController {
	
	public static String ERROR_CODIGO_DEPOSITO_ACTUAL_REQUERIDO = "formularioFactura.error.codigoDeposito";
	public static String ERROR_FECHADESDE_MAYOR_FECHAHASTA = "formularioFactura.error.fechaDesdeMayorFechaHasta0";
	
	private ClienteEmpService clienteEmpService;
	private SucursalService sucursalService;
	private EmpresaService empresaService;
	private SerieService serieService;
	private AfipTipoComprobanteService afipTipoComprobanteService;
	private FacturaService facturaService;
	private FacturaBusquedaValidator validator;
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	@Autowired
	public void setSerieService(SerieService serieService){
		this.serieService = serieService;
	}
	@Autowired
	public void setAfipTipoComprobanteService(AfipTipoComprobanteService afipTipoComprobanteService){
		this.afipTipoComprobanteService = afipTipoComprobanteService;
	}
	@Autowired
	public void setFacturaService(FacturaService facturaService) {
		this.facturaService = facturaService;
	}
	
	@Autowired
	public void setValidator(FacturaBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(
		value="/iniciarListaComprobantesFactura.html",
		method = RequestMethod.GET
	)
	public String iniciarListaComprobantesFactura(HttpSession session, Map<String,Object> atributos){
		session.removeAttribute("facturaBusqueda");
		atributos.remove("facturas");
		
		return mostrarListaComprobantesFactura(session, atributos, null, null, null, null, null, null, null, null, null);
	}	
	

	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de mostrar la lista de facturas.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultafacturas" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(
			value="/mostrarListaComprobantesFactura.html",
			method = RequestMethod.GET
		)
	public String mostrarListaComprobantesFactura(HttpSession session,
			Map<String,Object> atributos,			
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valClienteEmp,
			@RequestParam(value="val", required=false) String valSerie,
			@RequestParam(value="val", required=false) String valTipo,
			@RequestParam(value="codigoEmpresa", required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal", required=false) String codigoSucursal,
			@RequestParam(value="idAfipTipoComprobante", required=false) Long idAfipTipoComprobante,
			@RequestParam(value="codigoCliente", required=false) String codigoCliente){
		//buscamos en la base de datos y lo subimos a request.
		List <Factura> facturas = null;
		
		Factura facturaBusqueda = (Factura) session.getAttribute("facturaBusqueda");
		if(facturaBusqueda != null){		
		//consulto en la base de datos
			facturas = facturaService.listarFacturasFiltradas(facturaBusqueda, obtenerClienteAsp());
		}
		
		session.setAttribute("facturasSession", facturas);
		atributos.put("facturas", facturas);
		
		//seteo el id del cliente
		if(atributos.get("clienteAspId")==null){
			atributos.put("clienteAspId", obtenerClienteAsp().getId());
		}

		
		// busco los tipos de comprobantes del afip
		if(atributos.get("afipTipoComprobantes")==null){
			AfipTipoComprobante afipTipoComprobante = new AfipTipoComprobante();
			ArrayList<String> codigos = new ArrayList<String>();
			codigos.add("001");
			codigos.add("002");
			codigos.add("003");
			codigos.add("006");
			codigos.add("007");
			codigos.add("008");
			afipTipoComprobante.setCodigos(codigos);
			List<AfipTipoComprobante> afipTipoComprobantes = afipTipoComprobanteService.listarTipoComprobanteFiltrados(afipTipoComprobante);
			atributos.put("afipTipoComprobantes", afipTipoComprobantes);
		}
		if(session.getAttribute("facturaBusqueda")==null ){
			facturaBusqueda = new Factura();			
			Date fecha = new Date();
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(fecha);
			gc.add(GregorianCalendar.DAY_OF_MONTH, -60);
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			facturaBusqueda.setFechaHastaStr(sd.format(fecha));
			facturaBusqueda.setFechaDesdeStr(sd.format(gc.getTime()));
			facturaBusqueda.setIdAfipTipoComprobante(0L);
			facturaBusqueda.setCodigoEmpresa("");
			facturaBusqueda.setCodigoSucursal("");
			facturaBusqueda.setCodigoCliente("");
			session.setAttribute("facturaBusqueda", facturaBusqueda);
		}else if(session.getAttribute("facturaBusqueda") != null){
			facturaBusqueda = (Factura)session.getAttribute("facturaBusqueda");
			session.setAttribute("facturaBusqueda", facturaBusqueda);
		}
		
		if(atributos.get("facturas")==null ){
			facturas = new ArrayList<Factura>();
			atributos.put("facturas", facturas);
		}else if(atributos.get("facturas")!=null){
			facturas = (List<Factura>)atributos.get("facturas");
		}
		
		if(atributos.get("errores") == null){
			atributos.put("errores", false);
		}		
		if(atributos.get("result")==null){
			atributos.put("errors", false);
		}
		
		if(codigoEmpresa!=null && codigoEmpresa.length()>0){
			facturaBusqueda.setCodigoEmpresa(codigoEmpresa);			
		}
		if(codigoSucursal!=null && codigoSucursal.length()>0){
			facturaBusqueda.setCodigoSucursal(codigoSucursal);
		}
		if(codigoCliente!=null && codigoCliente.length()>0){
			facturaBusqueda.setCodigoCliente(codigoCliente);
		}
		//se crea el objeto serie para realizar la busqueda del popup de series
		Serie serieBusquedaPopup = new Serie();
		serieBusquedaPopup.setCodigoEmpresa(codigoEmpresa);
		serieBusquedaPopup.setCodigoSucursal(codigoSucursal);
		serieBusquedaPopup.setIdAfipTipoComprobante(idAfipTipoComprobante);
		serieBusquedaPopup.setTipoSerie(Constantes.SERIE_TIPO_SERIE_FACTURA);
		
		definirPopupEmpresa(atributos, valEmpresa);
		definirPopupSucursal(atributos, codigoEmpresa, valSucursal);
		definirPopupClienteEmp(atributos,codigoEmpresa , valClienteEmp);
		definirPopupSerie(serieBusquedaPopup, atributos, valSerie);

		session.setAttribute("facturaBusqueda", facturaBusqueda);
		atributos.put("facturas", facturas);
		
		return "consultaComprobantesFactura";
	}
	
	@RequestMapping(
			value="/filtrarListaComprobantesFactura.html",
			method = RequestMethod.POST
		)
	public String filtrarListaComprobantesFactura(
			@ModelAttribute("facturaBusqueda") Factura facturaBusqueda, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		this.validator.validate(facturaBusqueda, result);
		if(!result.hasErrors()){
			session.setAttribute("facturaBusqueda", facturaBusqueda);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		
//		if (facturaBusqueda == null) {
//			facturaBusqueda = (Factura) atributos.get("facturaBusqueda");
//		}
//		// buscamos en la base de datos y lo subimos a request.
//		ArrayList<String> codigoErrores = new ArrayList<String>();
//		ClienteAsp clienteAsp = obtenerClienteAsp();
//		List<Factura> facturas = facturaService.listarFacturasFiltradas(
//				facturaBusqueda, clienteAsp);
//		atributos.put("facturaBusqueda", facturaBusqueda);
//		atributos.put("facturas", facturas);
		
		return mostrarListaComprobantesFactura(session, atributos, null, null, null, null, null, null, null, null, null);
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
			value="/eliminarFactura.html",
			method = RequestMethod.GET
		)
	public String eliminarFactura(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la remito para eliminar luego
		Factura factura = facturaService.obtenerPorId(Long.valueOf(id));
		
		//Eliminamos la remito
		commit = facturaService.eliminarFactura(factura);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("formularioFactura.notificacion.facturaEliminadaExito", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarListaComprobantesFactura(session, atributos, null, null, null, null, null, null, null, null, null);
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
	@RequestMapping(
			value="/imprimirFactura.html",
			method = RequestMethod.GET
		)
	public void imprimirFactura(HttpSession session,
			Map<String,Object> atributos,			
			HttpServletResponse response,
			@RequestParam(value="seleccionados", required=false) String seleccionados) {
		
		if(seleccionados != null && seleccionados.length()> 0){
			
			String[] listaRemitosSeleccionados = seleccionados.split("\\,");
			List<Long> listaNumeros = new ArrayList<Long>();
			for(int i = 0; i<listaRemitosSeleccionados.length;i++)
			{
				listaNumeros.add(Long.valueOf(listaRemitosSeleccionados[i]));
			}
			
			//buscamos en la base de datos y lo subimos a request.
				
			HashMap<String,Object> params = new HashMap<String,Object>();
			byte[] pdfByteArray ;
			ServletOutputStream op = null;
			try{
				//se trae busca nuevamente la misma lista en la base pero esta vez con los detales en FetchMode.Join
				List<Factura> facturasYDetalles = facturaService.getByIdsConDetalles(listaNumeros, obtenerClienteAsp());
				
				Long idTipo = null;
				Boolean distintos = false;
			
				if(facturasYDetalles!=null && facturasYDetalles.size()>0){
					idTipo = facturasYDetalles.get(0).getAfipTipoDeComprobante().getId();
					
					for(Factura factura:facturasYDetalles){
						if(factura.getAfipTipoDeComprobante().getId().longValue() != idTipo.longValue()){
							distintos = true;
							break;
						}
					}
					
				}
				
				if(distintos == false){
					
					for(Factura factura:facturasYDetalles){
						factura.setClienteEmp(clienteEmpService.getByIdConDireccion(factura.getClienteEmp().getId()));
						factura.setEmpresa(empresaService.getByCodigoConCondAfip(factura.getEmpresa().getCodigo(), obtenerClienteAsp()));
					}
					
					//se crea el data source
					JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(facturasYDetalles);	
					
					JasperReport jasperReport = null;
					JasperReport subReport = null;
					
					if(idTipo.longValue() == 1){
						//se crea el reporte
						jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+Constantes.REPORTE_FACTURA_A);
						subReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+Constantes.REPORTE_FACTURA_A_DETALLE);
					}
					else if(idTipo.longValue() == 5){
						//se crea el reporte
						jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+Constantes.REPORTE_FACTURA_B);
						subReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+Constantes.REPORTE_FACTURA_B_DETALLE);
					}
					
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
				}
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
		
	
		//return "impresionFactura";
	}
	
	
	@RequestMapping(
			value="/anularFactura.html",
			method = RequestMethod.GET
		)
	public String anularFactura(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="id",required=false) Long id,
			HttpServletRequest request){
		
		Boolean commit = false;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		
		Factura factura = facturaService.obtenerPorId(id);
		if(factura != null)
		{
			if(factura.getEstado()!= null && factura.getEstado().equalsIgnoreCase("ANULADO")){
				ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioFactura.notificacion.facturaYaAnulada", null);
				avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
			}
			else
			{
				factura.setEstado("ANULADO");
				
				List<Factura> facturas = new ArrayList<Factura>();
				facturas.add(factura);
							
				commit = facturaService.actualizarFactura(facturas);
				
				//Ver errores
				if(commit != null && !commit){
					ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
					avisos.add(mensaje); //agrego el mensaje a la coleccion
					atributos.put("errores", false);
					atributos.remove("result");
					atributos.put("hayAvisos", false);
					atributos.put("hayAvisosNeg", true);
					atributos.put("avisos", avisos);
					return mostrarListaComprobantesFactura(session, atributos, null, null, null, null, null, null, null, null, null);
				}
				
				//Genero las notificaciones 
				ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioFactura.notificacion.facturaAnuladaExito", null);
				avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", true);
				atributos.put("avisos", avisos);
				
			}
		}
		else
		{
			ScreenMessage mensajeElementoReg = new ScreenMessageImp("formularioFactura.error.facturaInvalida", null);
			avisos.add(mensajeElementoReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			
		}
		
		return mostrarListaComprobantesFactura(session, atributos, null, null, null, null, null, null, null, null, null);
	}
	/////////////////////METODOS DE SOPORTE/////////////////////////////
	
	private void definirPopupSucursal(Map<String,Object> atributos, String empresaCodigo, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> sucursalesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioComprobanteFactura.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioComprobanteFactura.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		sucursalesPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		sucursalesPopupMap.put("coleccionPopup", sucursalService.listarSucursalPopup(val, empresaCodigo, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		sucursalesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		sucursalesPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		sucursalesPopupMap.put("referenciaHtml", "codigoSucursal"); 		
		//url que se debe consumir con ajax
		sucursalesPopupMap.put("urlRequest", "mostrarListaComprobantesFactura.html");
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
		campos.add(new CampoDisplayTag("codigo","formularioComprobanteFactura.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioComprobanteFactura.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		empresasPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		empresasPopupMap.put("coleccionPopup", empresaService.listarEmpresaPopup(val, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		empresasPopupMap.put("referenciaHtml", "codigoEmpresa");
		//url que se debe consumir con ajax
		empresasPopupMap.put("urlRequest", "mostrarListaComprobantesFactura.html");
		//se vuelve a setear el texto utilizado para el filtrado
		empresasPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		empresasPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("empresasPopupMap", empresasPopupMap);
	}	
	private void definirPopupClienteEmp(Map<String,Object> atributos,String codigoEmpresa, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clienteEmpPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioComprobanteFactura.codigo",false));
		campos.add(new CampoDisplayTag("nombreRazonSocial","formularioComprobanteFactura.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		clienteEmpPopupMap.put("campos", campos);
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		clienteEmpPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup( val, codigoEmpresa, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clienteEmpPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clienteEmpPopupMap.put("referenciaPopup2", "nombreRazonSocial");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clienteEmpPopupMap.put("referenciaHtml", "codigoCliente"); 		
		//url que se debe consumir con ajax
		clienteEmpPopupMap.put("urlRequest", "mostrarListaComprobantesFactura.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clienteEmpPopupMap.put("textoBusqueda", val);
		
		//codigo de la localización para el titulo del popup
		clienteEmpPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("clienteEmpPopupMap", clienteEmpPopupMap);
	}
	
	private void definirPopupSerie(Serie serie, Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> seriesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioComprobanteFactura.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioComprobanteFactura.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		seriesPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		seriesPopupMap.put("coleccionPopup", serieService.listarSerieFiltradasPopup(serie, val,obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup2", "prefijo");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		seriesPopupMap.put("referenciaHtml", "codigoSerie");
		//url que se debe consumir con ajax
		seriesPopupMap.put("urlRequest", "mostrarSerie.html");
		//se vuelve a setear el texto utilizado para el filtrado
		seriesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		seriesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("seriesPopupMap", seriesPopupMap);
	}
	
	/**
	 * genera el objeto BindingResult para mostrar los errores por pantalla en un popup y lo agrega al map atributos
	 * @param codigoErrores
	 * @param atributos
	 */
	private void generateErrors(List<String> codigoErrores,	Map<String, Object> atributos) {
		if (!codigoErrores.isEmpty()) {
			BindingResult result = new BeanPropertyBindingResult(new Object(),"");
			for (String codigo : codigoErrores) {
				result.addError(new FieldError(	"error.formBookingGroup.general", codigo, null, false, new String[] { codigo }, null, "?"));
			}
			atributos.put("result", result);
			atributos.put("errores", true);
		} else if(atributos.get("result") == null){
			atributos.put("errores", false);
		}
	}
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	private Empresa obtenerEmpresaDefault(){
		return ((PersonaFisica)obtenerClienteAsp().getContacto()).getEmpresaDefecto();
	}
	private Sucursal obtenerSucursalDefault(){
		return ((PersonaFisica)obtenerClienteAsp().getContacto()).getSucursalDefecto();
	}
	
	private Long parseLongCodigo(String codigo){
		return ParseNumberUtils.parseLongCodigo(codigo);
	}
}
