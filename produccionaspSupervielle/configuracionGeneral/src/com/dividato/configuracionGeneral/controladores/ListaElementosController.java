package com.dividato.configuracionGeneral.controladores;

import java.io.IOException;
import java.util.ArrayList;
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

import com.dividato.configuracionGeneral.validadores.ElementoBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de Elemento.
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
				"/iniciarElemento.html",
				"/mostrarElementosSinFiltrar.html",
				"/mostrarElemento.html",
				"/eliminarElemento.html",
				"/filtrarElemento.html",
				"/mostrarErrorImpresionEtiquetaElemento.html",
				"/imprimirEtiquetasElementoCodigoBarra.html",
				"/borrarFiltrosElemento.html",
				"/validarEliminarElemento.html"
			}
		)
public class ListaElementosController {
	
	private ElementoService elementoService;
	private ElementoBusquedaValidator validator;
	private LecturaService lecturaService;
	private LecturaDetalleService lecturaDetalleService;
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	@Autowired
	public void setLeturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	@Autowired
	public void setLeturaDetalleService(LecturaDetalleService lecturaDetalleService) {
		this.lecturaDetalleService = lecturaDetalleService;
	}
	@Autowired
	public void setValidator(ElementoBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(
			value="/iniciarElemento.html",
			method = RequestMethod.GET
		)
	public String iniciarElemento(HttpSession session,
			Map<String,Object> atributos){
		session.removeAttribute("elementoBusqueda");
		session.removeAttribute("elementosSession");
		atributos.remove("elementos");
		session.removeAttribute("tablaPaginada");
		return "redirect:mostrarElementosSinFiltrar.html";
	}
	
	@RequestMapping(
			value="/mostrarElementosSinFiltrar.html",
			method = RequestMethod.GET
		)
	public String mostrarElementosSinFiltrar(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="val", required=false) String valCliente,
			@RequestParam(value="val", required=false) String valElemento,
			@RequestParam(value="val", required=false) String valLectura,
			@RequestParam(value="val", required=false) String valTipoElemento,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo){
		//buscamos en la base de datos y lo subimos a request.
		@SuppressWarnings("unchecked")
		List<Elemento> elementos = (List<Elemento>)session.getAttribute("elementosSession"); 
		if(elementos==null){
			elementos = new ArrayList<Elemento>();
		}
		
		Elemento elemento = (Elemento) session.getAttribute("elementoBusqueda");
		if(elemento == null){
			elemento = new Elemento();
			elemento.setCodigoEmpresa(empresaCodigo);
			elemento.setCodigoSucursal(sucursalCodigo);
			elemento.setCodigoCliente(clienteCodigo);
		}
		
		session.setAttribute("elementosSession", elementos);
		atributos.put("elementos",elementos);
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("size", Integer.valueOf(0));
		atributos.put("pagesize", "40");
		//hacemos el forward
		return "consultaElemento";
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de Elemento y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaElemento" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarElemento.html",
			method = RequestMethod.GET
		)
	public String mostrarElemento(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="val", required=false) String valCliente,
			@RequestParam(value="val", required=false) String valElemento,
			@RequestParam(value="val", required=false) String valLectura,
			@RequestParam(value="val", required=false) String valTiposElemento,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			@RequestParam(value="clienteCodigo", required=false) String clienteCodigo,
			HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		List <Elemento> elementos = null;	
		
		Elemento elemento = (Elemento) session.getAttribute("elementoBusqueda");
		//cuenta la cantidad de resultados
		Integer size = elementoService.contarElementoFiltradas(elemento, obtenerClienteAspUser());
		atributos.put("size", size);
		
		Integer pagesize = null;
		String pagesizeStr = "";
		try{
			 pagesizeStr = (String) session.getAttribute("pagesize");
		}catch(ClassCastException e){
			 pagesizeStr = session.getAttribute("pagesize").toString();
		}
		if(pagesizeStr== null)
			pagesizeStr = "40";
		if(pagesizeStr.equalsIgnoreCase("Todos"))
		{
			pagesize = size;
			
		}else{
			pagesize = Integer.valueOf(pagesizeStr);
		}
		if(pagesize == null)
			pagesize = 40;
		
		atributos.put("pagesize", pagesize);
		session.setAttribute("pagesize", pagesize);
		
		
		if(elemento!=null && elemento.getCodigoContenedor()!=null){
			elemento.setCodigoElemento(elemento.getCodigoContenedor());
		}
		if(elemento == null)
		{
			elemento = new Elemento();
		}
		//paginacion y orden de resultados de displayTag
		elemento.setTamañoPagina(pagesize);

		String nPaginaStr= request.getParameter((new ParamEncoder("elemento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		if(nPaginaStr==null){
			nPaginaStr = (String)atributos.get((new ParamEncoder("elemento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
		}
		String fieldOrder = request.getParameter( new ParamEncoder("elemento").encodeParameterName(TableTagParameters.PARAMETER_SORT));
		elemento.setFieldOrder(fieldOrder);
		String sortOrder = request.getParameter(new ParamEncoder("elemento").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
		elemento.setSortOrder(sortOrder);
		Integer nPagina = 1;		
		if(nPaginaStr!=null){
			nPagina = (Integer.parseInt(nPaginaStr));
		}
		elemento.setNumeroPagina(nPagina);
		
		//consulto en la base de datos
		if(elemento.getFieldOrder()==null)
			elemento.setFieldOrder("codigo");
		if(elemento.getSortOrder()==null)
			elemento.setSortOrder("1");
		
		if(pagesizeStr.equalsIgnoreCase("Todos"))
			elementos = elementoService.traerElementosPorSQL(elemento, obtenerClienteAspUser());
		else
			elementos =(List<Elemento>) elementoService.listarElementoFiltradas(elemento, obtenerClienteAspUser());
		
		session.setAttribute("elementosSession", elementos);
		atributos.put("elementos", elementos);
		
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		//hacemos el forward
		return "consultaElemento";
	}
	
	@RequestMapping(
			value="/filtrarElemento.html",
			method = RequestMethod.POST
		)
	public String filtrarElemento(@ModelAttribute("elementoBusqueda") Elemento elemento, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos, HttpServletRequest request,
			@RequestParam(value="pagesize",required=false)String pagesize){
		//buscamos en la base de datos y lo subimos a request.
		session.setAttribute("pagesize",pagesize);
		atributos.put("pagesize",pagesize);
		this.validator.validate(elemento, result);
		if(!result.hasErrors()){
			session.setAttribute("elementoBusqueda", elemento);
			atributos.put("errores", false);
			atributos.remove("result");			
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		return mostrarElemento(session, atributos, null, null, null, null, null,null,null,null, null, null,request);
	}
	
	
	@RequestMapping(
			value="/borrarFiltrosElemento.html",
			method = RequestMethod.GET
		)
	public String filtrarElemento(HttpSession session){
		session.removeAttribute("elementoBusqueda");
		return "redirect:mostrarElemento.html";
	}
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Elemento.
	 * 
	 * @param idElemento el id de Elemento a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/eliminarElemento.html",
			method = RequestMethod.GET
		)
	public String eliminarElemento(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos,
			HttpServletRequest request) {
		Boolean commit = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		//Obtenemos la elemento para eliminar luego
		Elemento elemento = elementoService.obtenerPorId(Long.valueOf(id));
		//Eliminamos la elemento
		commit = elementoService.eliminarElemento(elemento);
		ScreenMessage mensaje;
		//Controlamos su eliminacion.
		if(commit){
			mensaje = new ScreenMessageImp("notif.elemento.elementoEliminado", null);
			hayAvisos = true;
		}else{
			mensaje = new ScreenMessageImp("error.deleteDataBase", null);
			hayAvisosNeg = true;
		}
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return mostrarElemento(session, atributos, null, null, null, null,null, null,null, null, null, null,request);
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
			value="/imprimirEtiquetasElementoCodigoBarra.html",
			method = RequestMethod.GET
		)
	public void imprimirEtiquetasElementoCodigoBarra(HttpSession session,
			Map<String,Object> atributos,			
			HttpServletResponse response,
			@RequestParam(value="cantidad", required=false) Integer cantidad,
			@RequestParam(value="codigoLectura", required=false) String codigoLectura,
			@RequestParam(value="modo", required=false) String modo) {
		
		Elemento elemento = (Elemento) session.getAttribute("elementoBusqueda");
		if(elemento==null){
			elemento=new Elemento();
		}
		Boolean imprimirTodos = false;
		
		if(cantidad != null && cantidad == 2){
			imprimirTodos = true;
		}
		
		List<Elemento> elementos = new ArrayList<Elemento>(); 
		
		if(imprimirTodos)
			elementos = elementoService.traerElementosPorSQL(elemento, obtenerClienteAspUser());
		else
			elementos = elementoService.listarElementoFiltradas(elemento, obtenerClienteAspUser(),imprimirTodos);
		
		String tipoEtiqueta = "";
		
		if(elementos.size()>0)
		{
			tipoEtiqueta = elementos.get(0).getTipoElemento().getTipoEtiqueta();
			if(tipoEtiqueta != null && tipoEtiqueta!="")
			{
				for(int i = 1;i<elementos.size();i++)
				{
					if(!tipoEtiqueta.equals(elementos.get(i).getTipoElemento().getTipoEtiqueta()))
					{
					    	try {
							String aviso = "variosTipos";
							response.sendRedirect("mostrarErrorImpresionEtiquetaElemento.html?avisos="+aviso);
							} catch (IOException e) {
								e.printStackTrace();
							}
					    	return;
					}
				}
				
				//buscamos en la base de datos y lo subimos a request.
				String reportName = "";
				if(tipoEtiqueta.equals(Constantes.TIPO_ELEMENTO_TIPO_ETIQUETA_ETIQUETA_MEDIA))
				{
					reportName = Constantes.REPORTE_ELEMENTO_ETIQUETA_MEDIA;
				}
				else
				{
					reportName = Constantes.REPORTE_ELEMENTO_ETIQUETA_CHICA;
					//Linea agregada por si se quiere imprimir etiquetas de copia
					//reportName = "C:\\jasper\\reporteImpresionElementoCodigoBarraCopia";
				}
					
				@SuppressWarnings("rawtypes")
				Map<String,Object> params =new HashMap<String,Object>();	
				//params.put("codigoCopia", "000000000001");
				byte[] pdfByteArray ;
				ServletOutputStream op = null;
				try{
					if(codigoLectura != null && !codigoLectura.equals(""))
					{
						Lectura lectura = lecturaService.obtenerPorCodigo(Long.parseLong(codigoLectura),obtenerClienteAspUser());
						if(lectura!=null)
						{
							List<LecturaDetalle> detalles = lecturaDetalleService.listarLecturaDetalleEnListaElementos(elementos, lectura, obtenerClienteAspUser());
							if(detalles != null)
							{
								elementos.removeAll(elementos);
								for(LecturaDetalle detalle : detalles){
									detalle.getElemento().setOrden(detalle.getOrden());
									detalle.getElemento().setCodigoLectura(codigoLectura);
									Boolean yaInsertado = false;
									if(elementos.size()>0){
										for(int i= 0;i<elementos.size();i++)
										{
											Long ordenElemento = elementos.get(i).getOrden();
											if(detalle.getElemento().getOrden().longValue()<ordenElemento.longValue())
											{
												elementos.add(i,detalle.getElemento());
												yaInsertado = true;
												break;
											}
										}
										if(yaInsertado == false){
											elementos.add(detalle.getElemento());
										}
									}
									else
									{elementos.add(detalle.getElemento());}
								}
							}
							
						}
					}
					//se crea el data source
					JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(elementos);
					//se crea el reporte
					JasperReport jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+reportName);
					//Linea agregada por si se quiere imprimir etiquetas de copias
					//JasperReport jasperReport = JasperCompileManager.compileReport(reportName+".jrxml");
					
					pdfByteArray = JasperRunManager.runReportToPdf(jasperReport, params,ds);
					//se envia el reporte 
					if(modo.equals("ventana"))
					{response.setContentType("application/pdf;");}
					else if(modo.equals("descarga"))
					{
						response.setContentType("application/octet-stream;");
						response.setHeader( "Content-disposition", "attachment; filename=impresionElementos.pdf");
					}
	                

					op = null;
					op = response.getOutputStream();
					op.write(pdfByteArray, 0, pdfByteArray.length);
					op.flush();
					op.close();
					
					params.clear();
					System.gc();
					return;
	
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
			else
			{
				try {
					String aviso = "etiquetaVacia";
					response.sendRedirect("mostrarErrorImpresionEtiquetaElemento.html?avisos="+aviso);
					} catch (IOException e) {
						e.printStackTrace();
					}
			    	
			}
		}
	}
	
	@RequestMapping(
			value="/mostrarErrorImpresionEtiquetaElemento.html",
			method = RequestMethod.GET
		)
	public String mostrarErrorImpresionEtiquetaElemento(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="avisos", required=false) String aviso){
		
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisosNeg = true;
		
		if(aviso.equals("variosTipos"))
		{
			ScreenMessage mensaje = new ScreenMessageImp("formularioElemento.errorImpresionDistintasEtiquetas",null);
			avisos.add(mensaje);
		}
		if(aviso.equals("etiquetaVacia"))
		{
			ScreenMessage mensaje = new ScreenMessageImp("formularioElemento.errorImpresionEtiquetaVacia",null);
			avisos.add(mensaje);
		}
		atributos.put("avisos", avisos);
		atributos.put("hayAvisosNeg",hayAvisosNeg);
		return "errorImpresionEtiquetaElemento";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de eliminar Elemento.
	 * 
	 * @param idElemento el id de Elemento a eliminar.
	 * (Observar la anotación @RequestParam)
	 * @param atributos son los atributos del request
	 * @return ejecuta el método de consulta de inscost y retorna su resultado.
	 */
	@RequestMapping(
			value="/validarEliminarElemento.html",
			method = RequestMethod.GET
		)
	public void validarEliminarElemento(HttpSession session,
			@RequestParam("id") String id,
			Map<String,Object> atributos,
			HttpServletResponse response,
			HttpServletRequest request) {
		try {
		//Obtenemos la elemento para eliminar luego
		Elemento elemento = elementoService.obtenerPorId(Long.valueOf(id));
		// Si es contenedor, tengo que buscar 
		if(elemento.getTipoElemento().getContenedor()==true){
			if(elemento.getContenedor()!=null){
				response.getWriter().write(id);
			}
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/////////////////////METODOS DE SOPORTE/////////////////////////////
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}