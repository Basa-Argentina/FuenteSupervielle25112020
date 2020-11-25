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

import com.dividato.configuracionGeneral.validadores.ImpresionEtiquetaModuloBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EstanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ModuloService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SeccionService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.Constantes;

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
				"/iniciarImpresionEtiquetaModulo.html",
				"/filtrarImpresionEtiquetaModulo.html",
				"/imprimirEtiquetasModuloCodigoBarra.html",
				"/mostrarImpresionEtiquetaModulo.html"
			}
		)
public class ListaImpresionEtiquetaModuloController {
	
	private DepositoService depositoService;
	private SeccionService seccionService;
	private EstanteService estanteService;
	private ModuloService moduloService;
	private ImpresionEtiquetaModuloBusquedaValidator validator;
	
	//private List<Modulo> modulos;
	/**
	 * Setea el servicio de Depositos.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto ModuloService.
	 * La clase DepositoServiceImp implementa Deposito y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param depositoService
	 */
	@Autowired
	public void setDepositoService(DepositoService depositoService) {
		this.depositoService = depositoService;
	}
	@Autowired
	public void setSeccionService(SeccionService seccionService) {
		this.seccionService = seccionService;
	}
	@Autowired
	public void setEstanteService(EstanteService estanteService) {
		this.estanteService = estanteService;
	}
	@Autowired
	public void setModuloService(ModuloService moduloService) {
		this.moduloService = moduloService;
	}
	
	@Autowired
	public void setValidator(ImpresionEtiquetaModuloBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}

	@RequestMapping(
		value="/iniciarImpresionEtiquetaModulo.html",
		method = RequestMethod.GET
	)
	public String iniciarImpresionEtiquetaModulo(HttpSession session,
			Map<String,Object> atributos){
		session.removeAttribute("moduloBusqueda");
		session.removeAttribute("modulosSession");
		atributos.remove("modulos");
		return "redirect:mostrarImpresionEtiquetaModulo.html";
	}
	
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de Modulos a imprimir y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaImpresionEtiqueta" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/mostrarImpresionEtiquetaModulo.html",
			method = RequestMethod.GET
		)
	public String mostrarImpresionEtiquetaModulo(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valSeccion,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="depositoCodigo", required=false) String depositoCodigo,
			@RequestParam(value="seccionCodigo", required=false) String seccionCodigo,
			HttpServletRequest request){
		
		//buscamos en la base de datos y lo subimos a request.
		List <Modulo> modulos = null;
		Integer size = 0;
		
		Modulo modulo = (Modulo) session.getAttribute("moduloBusqueda");
		if(modulo!=null){
		//cuenta la cantidad de resultados
		size = moduloService.contarModulosFiltrados(modulo, obtenerClienteAspUser());
		
		//paginacion y orden de resultados de displayTag
		modulo.setTamañoPagina(20);
		String nPaginaStr = null;
		
		if(request!=null){

			nPaginaStr= request.getParameter((new ParamEncoder("modulo").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("modulo").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			String fieldOrder = request.getParameter( new ParamEncoder("modulo").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			modulo.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("modulo").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			modulo.setSortOrder(sortOrder);
		}
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
		modulo.setNumeroPagina(nPagina);
		
		//consulto en la base de datos
		modulos =(List<Modulo>) moduloService.listarModuloFiltradas(modulo, obtenerClienteAspUser(),false);
		
		}else{
				modulo = new Modulo();
		}
		
		session.setAttribute("modulosSession", modulos);
		atributos.put("modulos", modulos);
		
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("size", size);
		
		String sucursalCodigo = ((PersonaFisica)obtenerUser().getPersona()).getSucursalDefecto().getCodigo();
		
		definirPopupDepositos(atributos, valDeposito, sucursalCodigo);
		definirPopupSecciones(atributos, valSeccion, depositoCodigo);
		
		//hacemos el forward
		return "consultaImpresionEtiquetaModulo";	
	}
	
	@RequestMapping(
			value="/filtrarImpresionEtiquetaModulo.html",
			method = RequestMethod.POST
		)
	public String filtrarImpresionEtiquetaModulo(@ModelAttribute("moduloBusqueda") Modulo modulo, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(modulo, result);
		if(!result.hasErrors()){
			session.setAttribute("moduloBusqueda", modulo);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}
		return mostrarImpresionEtiquetaModulo(session,atributos, null, null, null, null,null);
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de imprimir los codigos de barra de de los modulos en un reporte y subirlo al response.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaImpresionEtiqueta" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/imprimirEtiquetasModuloCodigoBarra.html",
			method = RequestMethod.GET
		)
	public void imprimirEtiquetasModuloCodigoBarra(HttpSession session,
			Map<String,Object> atributos,			
			HttpServletResponse response,
			@RequestParam(value="cantidad", required=false) Integer cantidad,
			@RequestParam(value="modo", required=false) String modo) {
		
		Modulo modulo = (Modulo) session.getAttribute("moduloBusqueda");
		if(modulo==null){
			modulo=new Modulo();
		}
		
		Boolean imprimirTodos = false;
		
		if(cantidad != null && cantidad == 2){
			imprimirTodos = true;
		}
		
		List<Modulo> modulos = moduloService.listarModuloFiltradas(modulo, obtenerClienteAspUser(), imprimirTodos);
		
		//buscamos en la base de datos y lo subimos a request.
		String reportName = Constantes.REPORTE_MODULO_ETIQUETA;
			
		@SuppressWarnings("rawtypes")
		HashMap params = new HashMap();
		byte[] pdfByteArray ;
		ServletOutputStream op = null;
		try{
			//se crea el data source
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(modulos);
			//se crea el reporte
			JasperReport jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+reportName);
			
			pdfByteArray = JasperRunManager.runReportToPdf(jasperReport, params,ds);
			//se envia el reporte 
			if(modo.equals("ventana"))
			{response.setContentType("application/pdf;");}
			else if(modo.equals("descarga"))
			{
				response.setContentType("application/octet-stream;");
				response.setHeader( "Content-disposition", "attachment; filename=impresionElementos.pdf");
			}
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
	/////////////////////METODOS DE SOPORTE/////////////////////////////
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private void definirPopupSecciones(Map<String,Object> atributos, String val, String depositoCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> seccionesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioSeccion.datosSeccion.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioSeccion.datosSeccion.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		seccionesPopupMap.put("campos", campos);
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		seccionesPopupMap.put("coleccionPopup", seccionService.listarSeccionPopup(val, depositoCodigo, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		seccionesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		seccionesPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		seccionesPopupMap.put("referenciaHtml", "codigoSeccion"); 		
		//url que se debe consumir con ajax
		seccionesPopupMap.put("urlRequest", "mostrarModulo.html");
		//se vuelve a setear el texto utilizado para el filtrado
		seccionesPopupMap.put("textoBusqueda", val);
		
		seccionesPopupMap.put("filterPopUp", depositoCodigo);
		//codigo de la localización para el titulo del popup
		seccionesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("seccionesPopupMap", seccionesPopupMap);
	}
	
	private void definirPopupDepositos(Map<String,Object> atributos, String val, String sucursalCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> depositosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioDeposito.datosDeposito.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioDeposito.datosDeposito.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		depositosPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		depositosPopupMap.put("coleccionPopup", depositoService.listarDepositoPopup(val, sucursalCodigo, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		depositosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		depositosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		depositosPopupMap.put("referenciaHtml", "codigoDeposito"); 		
		//url que se debe consumir con ajax
		depositosPopupMap.put("urlRequest", "mostrarModulo.html");
		//se vuelve a setear el texto utilizado para el filtrado
		depositosPopupMap.put("textoBusqueda", val);
		
		depositosPopupMap.put("filterPopUp", sucursalCodigo);
		//codigo de la localización para el titulo del popup
		depositosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("depositosPopupMap", depositosPopupMap);
	}
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
}
