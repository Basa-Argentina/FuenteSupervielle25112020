/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores.jerarquias;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
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

import com.dividato.configuracionGeneral.objectForms.RequerimientoElementoBusquedaForm;
import com.dividato.configuracionGeneral.validadores.jerarquias.RequerimientoElementoBusquedaValidator;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RearchivoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.accesoDatos.interfaz.CampoComparacion;
import com.security.accesoDatos.jerarquias.interfaz.RequerimientoReferenciaService;
import com.security.accesoDatos.jerarquias.interfaz.TipoRequerimientoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Rearchivo;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.TipoRequerimiento;
import com.security.modelo.seguridad.User;
import com.security.recursos.Configuracion;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de requerimientoElementos.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero *
 */

/**
 * @author ldiaz
 *
 */
@Controller
@RequestMapping(
		value=
			{	
				"/iniciarRequerimientoElemento.html",
				"/mostrarRequerimientoElemento.html",
				"/filtrarRequerimientoElemento.html",
				"/filtrarReferencia.html",
				"/mostrarReferencia.html",
				"/verImagenesRearchivo.html",
				"/abrirFancyBoxImagenesRearchivo.html",
				"/iniciarReferencia.html",
				"/verLegajo.html",
				"/descargarImagenesZIP.html",
				"/descargarImagenesPDF.html",
				"/refrescarConsultaReferencia.html"
			}
		)
public class ListaRequerimientoElementoController {
	private static Logger logger=Logger.getLogger(ListaRequerimientoElementoController.class);
	private ElementoService elementoService;
	private TipoElementoService tipoElementoService;
	private TipoRequerimientoService tipoRequerimientoService;
	private ReferenciaService referenciaService;
	private ClienteEmpService clienteEmpService;
	private ClasificacionDocumentalService clasificacionDocumentalService;
	private RequerimientoReferenciaService requerimientoReferenciaService;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	private RequerimientoElementoBusquedaValidator validator;
	private RearchivoService rearchivoService;
	private static JasperReport compiledJasperReport = null;
	private List<Referencia> archives;
	
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	
	@Autowired
	public void setClasificacionDocumentalService(ClasificacionDocumentalService clasificacionDocumentalService) {
		this.clasificacionDocumentalService = clasificacionDocumentalService;
	}

	@Autowired
	public void setRequerimientoReferenciaService(RequerimientoReferenciaService requerimientoReferenciaService) {
		this.requerimientoReferenciaService = requerimientoReferenciaService;
	}
	
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	
	@Autowired
	public void setTipoElementoService(TipoElementoService tipoElementoService) {
		this.tipoElementoService = tipoElementoService;
	}
	
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
	}
	
	@Autowired
	public void setRearchivoService(RearchivoService rearchivoService) {
		this.rearchivoService = rearchivoService;
	}
	
	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	
	@Autowired
	public void setTipoRequerimientoService(TipoRequerimientoService tipoRequerimientoService) {
		this.tipoRequerimientoService = tipoRequerimientoService;
	}

	@Autowired
	public void setValidator(RequerimientoElementoBusquedaValidator validator) {
		this.validator = validator;
	}
	
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	@RequestMapping(value="/iniciarRequerimientoElemento.html", method = RequestMethod.GET)
	public String iniciarRequerimientoElemento(HttpSession session, Map<String,Object> atributos,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo,
			@RequestParam(value="idRequerimiento",required=false) Long idRequerimiento,
			@RequestParam(value="destinoURL",required=false) String destinoURL,
			@RequestParam(value="tipoReqCodigoString",required=false) String tipoReqCodigo){
		session.removeAttribute("requerimientoElementoBusqueda");
		session.setAttribute("clienteCodigoRequerimientoElemento", clienteCodigo);
		session.setAttribute("tipoReqCodigoRequerimientoElemento", tipoReqCodigo);
		session.setAttribute("idRequerimientoElemento", idRequerimiento);
		session.setAttribute("destinoURLRequerimientoElemento", destinoURL);
		return "redirect:mostrarRequerimientoElemento.html";
	}
	
	@RequestMapping(value="/mostrarRequerimientoElemento.html", method = RequestMethod.GET)
	public String mostrarRequerimientoElemento(HttpSession session, 
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valElemento,
			@RequestParam(value="val", required=false) String valTipoElemento,
			@RequestParam(value="val", required=false) String valClasificacionDocumental,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo,
			@RequestParam(value="codigoTipoElemento",required=false) String codigoTipoElemento,
			HttpServletRequest request){
		
		Empleado empleado = (Empleado) session.getAttribute("empleadoSession");
		
		//buscamos en la base de datos y lo subimos a request.
		List referenciaElementos = null;	
		String listaIdElementosLecturaStr = null;
		RequerimientoElementoBusquedaForm requerimientoElemento = (RequerimientoElementoBusquedaForm) session.getAttribute("requerimientoElementoBusqueda");
		atributos.put("size", Integer.valueOf(0));
		if(requerimientoElemento != null){
			//cuenta la cantidad de resultados
			
			ClienteEmp clienteEmp = clienteEmpService.getByCodigo(String.valueOf(session.getAttribute("clienteCodigoRequerimientoElemento")));
			
			TipoRequerimiento tipoReq = tipoRequerimientoService.obtenerPorCodigo((String)session.getAttribute("tipoReqCodigoRequerimientoElemento"),obtenerClienteAsp());
			if(tipoReq!=null)
				requerimientoElemento.setTipoReq(tipoReq);
			
			if(requerimientoElemento.getCodigoContenedor()!=null && !"".equals(requerimientoElemento.getCodigoContenedor()))
				requerimientoElemento.setElemento(elementoService.getByCodigo(requerimientoElemento.getCodigoContenedor(), obtenerClienteAsp()));
//			if(requerimientoElemento.getCodigoTipoElemento()!=null && !"".equals(requerimientoElemento.getCodigoTipoElemento()))
//				requerimientoElemento.setTipoElemento(tipoElementoService.getByCodigo(requerimientoElemento.getCodigoTipoElemento(), obtenerClienteAsp()));
			if(requerimientoElemento.getCodigoClasificacionDocumental()!=null && !"".equals(requerimientoElemento.getCodigoClasificacionDocumental()))
				requerimientoElemento.setClasificacionDocumental(clasificacionDocumentalService.getClasificacionByCodigoCargarHijos(Integer.parseInt(requerimientoElemento.getCodigoClasificacionDocumental()), clienteEmp.getCodigo(), obtenerClienteAsp(),null));
			if(requerimientoElemento.getCodigoLectura()!=null && !"".equals(requerimientoElemento.getCodigoLectura())){
				List<Long> listaIdElementosLectura = elementoService.traerIdElementosEnLectura(requerimientoElemento.getCodigoLectura(),obtenerClienteAsp());
				if(listaIdElementosLectura!=null && listaIdElementosLectura.size()>0){
					listaIdElementosLecturaStr = "";
					listaIdElementosLecturaStr = listaIdElementosLectura.toString();
					listaIdElementosLecturaStr = listaIdElementosLecturaStr.replace("[", "").replace("]", "");
				}
			}
			
//			Long elementoId = null;
//			if(requerimientoElemento.getElemento()!=null)
//				elementoId = requerimientoElemento.getElemento().getId();
			
			String codigoContenedor = requerimientoElemento.getCodigoContenedor();

				
			Long tipoElementoId = null;
			if(requerimientoElemento.getTipoElemento()!=null)
				tipoElementoId = requerimientoElemento.getTipoElemento().getId();
			
			String numero1Texto = "";
			if(requerimientoElemento.getNumero1()!=null){
				if(requerimientoElemento.isNumero1Izquierda())
					numero1Texto = "%"+requerimientoElemento.getNumero1();
				if(requerimientoElemento.isNumero1Derecha()){
					if(!requerimientoElemento.isNumero1Izquierda())
						numero1Texto = requerimientoElemento.getNumero1() + "%";
					else
						numero1Texto += "%";
				}
			}
			String numero2Texto = "";
			if(requerimientoElemento.getNumero2()!=null){
				if(requerimientoElemento.isNumero2Izquierda())
					numero2Texto = "%"+requerimientoElemento.getNumero2();
				if(requerimientoElemento.isNumero2Derecha()){
					if(!requerimientoElemento.isNumero2Izquierda())
						numero2Texto = requerimientoElemento.getNumero2() + "%";
					else
						numero2Texto += "%";
				}
			}
			String texto1 = "";
			if(requerimientoElemento.getTexto1()!=null && !requerimientoElemento.getTexto1().trim().equals("")){
				texto1 = requerimientoElemento.getTexto1();
				if(requerimientoElemento.isTexto1Izquierda())
					texto1 = "%"+requerimientoElemento.getTexto1();
				if(requerimientoElemento.isTexto1Derecha()){
					if(!requerimientoElemento.isTexto1Izquierda())
						texto1 = requerimientoElemento.getTexto1() + "%";
					else
						texto1 += "%";
				}
			}
			String texto2 = "";
			if(requerimientoElemento.getTexto2()!=null && !requerimientoElemento.getTexto2().trim().equals("")){
				texto2 = requerimientoElemento.getTexto2();
				if(requerimientoElemento.isTexto2Izquierda())
					texto2 = "%"+requerimientoElemento.getTexto2();
				if(requerimientoElemento.isTexto2Derecha()){
					if(!requerimientoElemento.isTexto2Izquierda())
						texto2 = requerimientoElemento.getTexto2() + "%";
					else
						texto2 += "%";
				}
			}
			String descripcion = "";
			if(requerimientoElemento.getDescripcion()!=null && !requerimientoElemento.getDescripcion().trim().equals("")){
				descripcion = requerimientoElemento.getDescripcion();
				if(requerimientoElemento.isDescripcionIzquierda())
					descripcion = "%"+requerimientoElemento.getDescripcion();
				if(requerimientoElemento.isDescripcionDerecha()){
					if(!requerimientoElemento.isDescripcionIzquierda())
						descripcion = requerimientoElemento.getDescripcion() + "%";
					else
						descripcion += "%";
				}
			}
			//Armamos la lista de nodos para hacer in
			String clasificaciones = null;
			if(requerimientoElemento.getClasificacionDocumental()!=null){
				clasificaciones = "";
				int i = 0;
				Set<ClasificacionDocumental> clasificacionDocumentals = requerimientoElemento.getClasificacionDocumental().getListaCompletaHijos();
				clasificacionDocumentals.add(requerimientoElemento.getClasificacionDocumental());
				int total = clasificacionDocumentals.size();
				for(ClasificacionDocumental clasificacionDocumental:clasificacionDocumentals){
					clasificaciones += ""+clasificacionDocumental.getId();
					i++;
					if(i<total)
						clasificaciones += ",";
				}
				
			}
			else if(empleado!=null)
			{
				List<ClasificacionDocumental> porEmpleado = new ArrayList<ClasificacionDocumental>(clasificacionDocumentalService.getByPersonalAsignado(empleado));
				clasificaciones = "";
				int i = 0;
				if(porEmpleado!=null && porEmpleado.size()>0)
				{
					Set<ClasificacionDocumental> clasificacionDocumentals = porEmpleado.get(0).getListaCompletaHijos();
					clasificacionDocumentals.add(porEmpleado.get(0));
					int total = clasificacionDocumentals.size();
					for(ClasificacionDocumental clasificacionDocumental:clasificacionDocumentals){
						clasificaciones += ""+clasificacionDocumental.getId();
						i++;
						if(i<total)
							clasificaciones += ",";
					}
				}
				else
				{
					//Genero las notificaciones 
					List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
					ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioLoteReferencia.notificacion.empleadoSinClasifAsignada", null);
					avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
					atributos.put("errores", false);
					atributos.put("hayAvisosNeg", true);
					atributos.put("avisos", avisos);
					 session.removeAttribute("requerimientoElementoBusqueda");
					return mostrarRequerimientoElemento(session, atributos, valElemento, valTipoElemento, valClasificacionDocumental, clienteCodigo, codigoTipoElemento, request);
				}
				
			}
			Long seleccion = null;
			if(requerimientoElemento.getSeleccion()!=null && !"".equals(requerimientoElemento.getSeleccion())){
				if(requerimientoElemento.getSeleccion().equals("unico"))
					seleccion = new Long(1);
				if(requerimientoElemento.getSeleccion().equals("grupo"))
					seleccion = new Long(0);
			}
			Integer size = referenciaService.contarElementoFiltradas(obtenerClienteAsp().getId(),requerimientoElemento.getFecha1(), requerimientoElemento.getFecha2(), 
					requerimientoElemento.getFechaEntre(), requerimientoElemento.getFechaInicio(), requerimientoElemento.getFechaFin(),
					requerimientoElemento.getNumero1(), requerimientoElemento.getNumero2(), requerimientoElemento.getNumeroEntre(), numero1Texto, numero2Texto, 
					texto1, texto2, descripcion, requerimientoElemento.getCodigoTipoElemento()/*tipoElementoId*/, codigoContenedor, requerimientoElemento.getCodigoElemento(),
					clienteEmp!=null?clienteEmp.getId():null, clasificaciones, seleccion,listaIdElementosLecturaStr);
			atributos.put("size", size);
			
			//paginacion y orden de resultados de displayTag
			Integer numeroPagina;
			Integer tamañoPagina;
			Integer cantExportar = null;
			String fieldOrder;
			String sortOrder;
			
			if(request.getParameter(TableTagParameters.PARAMETER_EXPORTING) != null) {
				tamañoPagina = cantExportar = size;
			}else{
				tamañoPagina = size;
			}
			atributos.put("pageSize",tamañoPagina);
			String nPaginaStr= request.getParameter((new ParamEncoder("requerimientoElemento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("requerimientoElemento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			fieldOrder = request.getParameter( new ParamEncoder("requerimientoElemento").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			sortOrder = request.getParameter(new ParamEncoder("requerimientoElemento").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			numeroPagina = nPagina;
			if(fieldOrder == null || "".equals(fieldOrder))
				fieldOrder = "elementos.codigo";
			if(sortOrder == null || "".equals(sortOrder))
				sortOrder = "1";
			
		
			referenciaElementos = referenciaService.listarReferencias(obtenerClienteAsp().getId(),requerimientoElemento.getFecha1(), requerimientoElemento.getFecha2(), requerimientoElemento.getFechaEntre(),
					requerimientoElemento.getFechaInicio(), requerimientoElemento.getFechaFin(),
					requerimientoElemento.getNumero1(), requerimientoElemento.getNumero2(), requerimientoElemento.getNumeroEntre(), numero1Texto, numero2Texto, 
					texto1, texto2, descripcion, requerimientoElemento.getCodigoTipoElemento()/*tipoElementoId*/, codigoContenedor, requerimientoElemento.getCodigoElemento(),clienteEmp!=null?clienteEmp.getId():null, 
							clasificaciones, seleccion,	listaIdElementosLecturaStr,numeroPagina, tamañoPagina, fieldOrder, sortOrder, true, cantExportar);
		}
		else
			referenciaElementos = null;
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAsp().getId());
		List<RequerimientoElemento> requerimientoElementos = null;
		if(referenciaElementos != null && referenciaElementos.size() > 0){
			Long idReg = (Long) session.getAttribute("idRequerimientoElemento");
			requerimientoElementos = obtenerLista(referenciaElementos,idReg);
		}
	
		atributos.put("requerimientoElementos", requerimientoElementos);
		session.setAttribute("requerimientoElementoBusqueda", requerimientoElemento);
		return "consultaRequerimientoElemento";
	}
	
	@RequestMapping(value="/iniciarReferencia.html", method = RequestMethod.GET)
	public String iniciarReferencia(HttpSession session, Map<String,Object> atributos,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo,
			@RequestParam(value="idRequerimiento",required=false) Long idRequerimiento,
			@RequestParam(value="destinoURL",required=false) String destinoURL){
		session.removeAttribute("requerimientoElementoBusqueda");
		session.setAttribute("clienteCodigoRequerimientoElemento", clienteCodigo);
		session.setAttribute("idRequerimientoElemento", idRequerimiento);
		session.setAttribute("destinoURLRequerimientoElemento", destinoURL);
		return "redirect:mostrarReferencia.html";
	}
	
	public List<String> webs(String listaPalabras) throws IOException
    {
            
            String uri ="http://localhost:9955/?query=" + URLEncoder.encode(listaPalabras, "UTF-8");            
            StringBuilder sb = new StringBuilder();
            
            URL url = new URL(uri);
            StringBuilder postData = new StringBuilder();
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            connection.setDoOutput(true);
            connection.getOutputStream().write(postDataBytes);
            
            connection.setConnectTimeout(10000);
            connection.connect();
            int status = connection.getResponseCode();

            switch (status) {
            case 200:
            case 201:
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
            }
            return Arrays.asList(sb.toString().split("\\s*,\\s*"));     
    }
	
	@RequestMapping(value="/mostrarReferencia.html", method = RequestMethod.GET)
	public String mostrarReferencia(HttpSession session, 
			Map<String,Object> atributos,
			HttpServletRequest request){
		
		Empleado empleado = (Empleado) session.getAttribute("empleadoSession");
		String listaPalabras = null;
		List<String[]> listaReferenciaArchivo = null;
		List<String> listaArchivos = null;
		Long numeroUno;
		
		if (request.getParameter("listaPalabras")!= null) {
			if (!request.getParameter("listaPalabras").equals("")){
				listaPalabras = (String) request.getParameter("listaPalabras");

				try {
					listaArchivos = webs(listaPalabras);
				} catch (IOException e1) {
					logger.error("No se pudo comunicar con el web service",e1);
					e1.printStackTrace();
				}
				try {
					listaReferenciaArchivo = matchArchives(listaArchivos,request.getParameter("codigoCliente"));
				} catch (Exception e) {
					logger.error("No se pudieron matchear archivos",e);
					e.printStackTrace();
				}
			}
		}
		
		//buscamos en la base de datos y lo subimos a request.
		List referenciaElementos = null;	
		RequerimientoElementoBusquedaForm requerimientoElemento = (RequerimientoElementoBusquedaForm) session.getAttribute("requerimientoElementoBusqueda");
		atributos.put("size", Integer.valueOf(0));
		if(requerimientoElemento != null){
			ClienteEmp clienteEmp = null;
			//cuenta la cantidad de resultados
			if(requerimientoElemento.getCodigoCliente()!=null && !"".equals(requerimientoElemento.getCodigoCliente()))
			 clienteEmp = clienteEmpService.getByCodigo(requerimientoElemento.getCodigoCliente());
			if(requerimientoElemento.getCodigoContenedor()!=null && !"".equals(requerimientoElemento.getCodigoContenedor()))
				requerimientoElemento.setElemento(elementoService.getByCodigo(requerimientoElemento.getCodigoContenedor(), obtenerClienteAsp()));
//			if(requerimientoElemento.getCodigoTipoElemento()!=null && !"".equals(requerimientoElemento.getCodigoTipoElemento()))
//				requerimientoElemento.setTipoElemento(tipoElementoService.getByCodigo(requerimientoElemento.getCodigoTipoElemento(), obtenerClienteAsp()));
			if(requerimientoElemento.getCodigoClasificacionDocumental()!=null && !"".equals(requerimientoElemento.getCodigoClasificacionDocumental()))
				requerimientoElemento.setClasificacionDocumental(clasificacionDocumentalService.getClasificacionByCodigoCargarHijos(Integer.parseInt(requerimientoElemento.getCodigoClasificacionDocumental()), clienteEmp.getCodigo(), obtenerClienteAsp(),null));
			if(requerimientoElemento.getCodigoEmpresa()!=null && !"".equals(requerimientoElemento.getCodigoEmpresa()))
				requerimientoElemento.setEmpresa(empresaService.getByCodigo(requerimientoElemento.getCodigoEmpresa(), obtenerClienteAsp()));
			if(requerimientoElemento.getCodigoSucursal()!=null && !"".equals(requerimientoElemento.getCodigoSucursal()))
				requerimientoElemento.setSucursal(sucursalService.getByCodigo(requerimientoElemento.getCodigoSucursal(), obtenerClienteAsp()));
//			Long elementoId = null;
//			if(requerimientoElemento.getElemento()!=null)
//				elementoId = requerimientoElemento.getElemento().getId();
						
			String codigoContenedor = requerimientoElemento.getCodigoContenedor();
				
			Long tipoElementoId = null;
			if(requerimientoElemento.getTipoElemento()!=null)
				tipoElementoId = requerimientoElemento.getTipoElemento().getId();
			
			String numero1Texto = "";
			if(requerimientoElemento.getNumero1()!=null){
				if(requerimientoElemento.isNumero1Izquierda())
					numero1Texto = "%"+requerimientoElemento.getNumero1();
				if(requerimientoElemento.isNumero1Derecha()){
					if(!requerimientoElemento.isNumero1Izquierda())
						numero1Texto = requerimientoElemento.getNumero1() + "%";
					else
						numero1Texto += "%";
				}
			}
			String numero2Texto = "";
			if(requerimientoElemento.getNumero2()!=null){
				if(requerimientoElemento.isNumero2Izquierda())
					numero2Texto = "%"+requerimientoElemento.getNumero2();
				if(requerimientoElemento.isNumero2Derecha()){
					if(!requerimientoElemento.isNumero2Izquierda())
						numero2Texto = requerimientoElemento.getNumero2() + "%";
					else
						numero2Texto += "%";
				}
			}
			String texto1 = "";
			if(requerimientoElemento.getTexto1()!=null && !requerimientoElemento.getTexto1().trim().equals("")){
				texto1 = requerimientoElemento.getTexto1();
				if(requerimientoElemento.isTexto1Izquierda())
					texto1 = "%"+requerimientoElemento.getTexto1();
				if(requerimientoElemento.isTexto1Derecha()){
					if(!requerimientoElemento.isTexto1Izquierda())
						texto1 = requerimientoElemento.getTexto1() + "%";
					else
						texto1 += "%";
				}
			}
			String texto2 = "";
			if(requerimientoElemento.getTexto2()!=null && !requerimientoElemento.getTexto2().trim().equals("")){
				texto2 = requerimientoElemento.getTexto2();
				if(requerimientoElemento.isTexto2Izquierda())
					texto2 = "%"+requerimientoElemento.getTexto2();
				if(requerimientoElemento.isTexto2Derecha()){
					if(!requerimientoElemento.isTexto2Izquierda())
						texto2 = requerimientoElemento.getTexto2() + "%";
					else
						texto2 += "%";
				}
			}
			String descripcion = "";
			if(requerimientoElemento.getDescripcion()!=null && !requerimientoElemento.getDescripcion().trim().equals("")){
				descripcion = requerimientoElemento.getDescripcion();	
				if(requerimientoElemento.isDescripcionIzquierda())
					descripcion = "%"+requerimientoElemento.getDescripcion();
				if(requerimientoElemento.isDescripcionDerecha()){
					if(!requerimientoElemento.isDescripcionIzquierda())
						descripcion = requerimientoElemento.getDescripcion() + "%";
					else
						descripcion += "%";
				}
			}
			//Armamos la lista de nodos para hacer in
			String clasificaciones = null;
			if(requerimientoElemento.getClasificacionDocumental()!=null){
				clasificaciones = "";
				int i = 0;
				Set<ClasificacionDocumental> clasificacionDocumentals = requerimientoElemento.getClasificacionDocumental().getListaCompletaHijos();
				clasificacionDocumentals.add(requerimientoElemento.getClasificacionDocumental());
				int total = clasificacionDocumentals.size();
				for(ClasificacionDocumental clasificacionDocumental:clasificacionDocumentals){
					clasificaciones += ""+clasificacionDocumental.getId();
					i++;
					if(i<total)
						clasificaciones += ",";
				}
				
			}
			else if(empleado!=null)
			{
				List<ClasificacionDocumental> porEmpleado = new ArrayList<ClasificacionDocumental>(clasificacionDocumentalService.getByPersonalAsignado(empleado));
				clasificaciones = "";
				int i = 0;
				if(porEmpleado!=null && porEmpleado.size()>0)
				{
					Set<ClasificacionDocumental> clasificacionDocumentals = porEmpleado.get(0).getListaCompletaHijos();
					clasificacionDocumentals.add(porEmpleado.get(0));
					int total = clasificacionDocumentals.size();
					for(ClasificacionDocumental clasificacionDocumental:clasificacionDocumentals){
						clasificaciones += ""+clasificacionDocumental.getId();
						i++;
						if(i<total)
							clasificaciones += ",";
					}
				}
				else
				{
					//Genero las notificaciones 
					List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
					ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioLoteReferencia.notificacion.empleadoSinClasifAsignada", null);
					avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
					atributos.put("errores", false);
					atributos.put("hayAvisosNeg", true);
					atributos.put("avisos", avisos);
					 session.removeAttribute("requerimientoElementoBusqueda");
					return mostrarReferencia(session, atributos, request);
				}
				
			}
			Long seleccion = null;
			if(requerimientoElemento.getSeleccion()!=null && !"".equals(requerimientoElemento.getSeleccion())){
				if(requerimientoElemento.getSeleccion().equals("unico"))
					seleccion = new Long(1);
				if(requerimientoElemento.getSeleccion().equals("grupo"))
					seleccion = new Long(0);
			}
			int size = 0;
 			if (listaReferenciaArchivo != null) {
				if (listaReferenciaArchivo.size()!=0){
					for(String[] itemArchivo: listaReferenciaArchivo){			
						Long numeroUnoTmp = itemArchivo[2].equals("")?null:Long.parseLong(itemArchivo[2]);
						clienteEmp = clienteEmpService.getByCodigo(itemArchivo[1]);	
						numeroUno = requerimientoElemento.getNumero1()==null?numeroUnoTmp:requerimientoElemento.getNumero1();
						//numeroUno = requerimientoElemento.getNumero1() ? requerimientoElemento.getNumero1() : itemArchivo[2];
						Integer sizeTemp = referenciaService.contarElementoFiltradas(obtenerClienteAsp().getId(),requerimientoElemento.getFecha1(), requerimientoElemento.getFecha2(), requerimientoElemento.getFechaEntre(),
								requerimientoElemento.getFechaInicio(), requerimientoElemento.getFechaFin(),
								numeroUno, requerimientoElemento.getNumero2(), requerimientoElemento.getNumeroEntre(), numero1Texto, numero2Texto, 
								texto1, texto2, descripcion, requerimientoElemento.getCodigoTipoElemento()/*tipoElementoId*/, codigoContenedor,itemArchivo[0], clienteEmp!=null?clienteEmp.getId():null, clasificaciones, seleccion, null);
						size += sizeTemp;
						//atributos.put("size", size);
					}
				}
			}
			else if (!request.getParameter("listaPalabras").equals("") && listaReferenciaArchivo == null) {
				size = referenciaService.contarElementoFiltradas(obtenerClienteAsp().getId(),requerimientoElemento.getFecha1(), requerimientoElemento.getFecha2(), requerimientoElemento.getFechaEntre(),
						requerimientoElemento.getFechaInicio(), requerimientoElemento.getFechaFin(),
						requerimientoElemento.getNumero1(), requerimientoElemento.getNumero2(), requerimientoElemento.getNumeroEntre(), numero1Texto, numero2Texto, 
						texto1, texto2, descripcion, requerimientoElemento.getCodigoTipoElemento()/*tipoElementoId*/, codigoContenedor,requerimientoElemento.getCodigoElemento(), (long)0, clasificaciones, seleccion, null);
			}
			else {
				size = referenciaService.contarElementoFiltradas(obtenerClienteAsp().getId(),requerimientoElemento.getFecha1(), requerimientoElemento.getFecha2(), requerimientoElemento.getFechaEntre(),
						requerimientoElemento.getFechaInicio(), requerimientoElemento.getFechaFin(),
						requerimientoElemento.getNumero1(), requerimientoElemento.getNumero2(), requerimientoElemento.getNumeroEntre(), numero1Texto, numero2Texto, 
						texto1, texto2, descripcion, requerimientoElemento.getCodigoTipoElemento()/*tipoElementoId*/, codigoContenedor,requerimientoElemento.getCodigoElemento(), clienteEmp!=null?clienteEmp.getId():null, clasificaciones, seleccion, null);
			}
			
			atributos.put("size", size);
			
			//paginacion y orden de resultados de displayTag
			Integer numeroPagina;
			Integer tamañoPagina;
			Integer cantExportar = null;
			String fieldOrder;
			String sortOrder;
			if(request.getParameter(TableTagParameters.PARAMETER_EXPORTING) != null) {
				tamañoPagina = cantExportar = 65000;
			}else{
				tamañoPagina = 30;
				
			}
			atributos.put("pageSize",tamañoPagina);
			String nPaginaStr= request.getParameter((new ParamEncoder("requerimientoElemento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("requerimientoElemento").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			fieldOrder = request.getParameter( new ParamEncoder("requerimientoElemento").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			sortOrder = request.getParameter(new ParamEncoder("requerimientoElemento").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			numeroPagina = nPagina;
			if(fieldOrder == null || "".equals(fieldOrder))
				fieldOrder = "elementos.codigo";
			if(sortOrder == null || "".equals(sortOrder))
				sortOrder = "1";
			 				
		
			if(listaReferenciaArchivo != null){
				if (listaReferenciaArchivo.size()!=0) {

					for(String[] itemArchivo: listaReferenciaArchivo){		
						Long numeroUnoTmp = itemArchivo[2].equals("")?null:Long.parseLong(itemArchivo[2]);
						clienteEmp = clienteEmpService.getByCodigo(itemArchivo[1]);
						numeroUno = requerimientoElemento.getNumero1()==null?numeroUnoTmp:requerimientoElemento.getNumero1();
						List referenciaElementosTmp = referenciaService.listarReferencias(obtenerClienteAsp().getId(),requerimientoElemento.getFecha1(), requerimientoElemento.getFecha2(), requerimientoElemento.getFechaEntre(),
								requerimientoElemento.getFechaInicio(), requerimientoElemento.getFechaFin(),
								numeroUno, requerimientoElemento.getNumero2(), requerimientoElemento.getNumeroEntre(), numero1Texto, numero2Texto, 
								texto1, texto2, descripcion,requerimientoElemento.getCodigoTipoElemento()/*tipoElementoId*/, codigoContenedor, itemArchivo[0],
								clienteEmp!=null?clienteEmp.getId():null, clasificaciones, seleccion, null,
										numeroPagina, tamañoPagina, fieldOrder, sortOrder, null, cantExportar);
						if(referenciaElementos != null)
							Collections.addAll(referenciaElementos, referenciaElementosTmp.toArray());
						else
							referenciaElementos = referenciaElementosTmp;
					}
				} 
			}
			else if (!request.getParameter("listaPalabras").equals("") && listaReferenciaArchivo == null) {
				List referenciaElementosTmp = referenciaService.listarReferencias(obtenerClienteAsp().getId(),null, null, null,
						null, null, null, null, null, numero1Texto, numero2Texto, texto1, texto2, descripcion, null, codigoContenedor, "0",
						(long) 0, clasificaciones, seleccion, null,
								numeroPagina, tamañoPagina, fieldOrder, sortOrder, null, cantExportar);
				if(referenciaElementos != null)
					Collections.addAll(referenciaElementos, referenciaElementosTmp.toArray());
				else
					referenciaElementos = referenciaElementosTmp;
			}
			else {
			
				List referenciaElementosTmp = referenciaService.listarReferencias(obtenerClienteAsp().getId(),requerimientoElemento.getFecha1(), requerimientoElemento.getFecha2(), requerimientoElemento.getFechaEntre(),
						requerimientoElemento.getFechaInicio(), requerimientoElemento.getFechaFin(),
						requerimientoElemento.getNumero1(), requerimientoElemento.getNumero2(), requerimientoElemento.getNumeroEntre(), numero1Texto, numero2Texto, 
						texto1, texto2, descripcion,requerimientoElemento.getCodigoTipoElemento()/*tipoElementoId*/, codigoContenedor, requerimientoElemento.getCodigoElemento(),
						clienteEmp!=null?clienteEmp.getId():null, clasificaciones, seleccion, null,
								numeroPagina, tamañoPagina, fieldOrder, sortOrder, null, cantExportar);
				referenciaElementos = referenciaElementosTmp;

			}
			
		}
		else {
			referenciaElementos = null;
			requerimientoElemento = new RequerimientoElementoBusquedaForm();
			requerimientoElemento.setCodigoEmpresa(obtenerCodigoEmpresaUser());
			requerimientoElemento.setCodigoSucursal(obtenerCodigoSucursalUser());
			if(empleado!=null)
				requerimientoElemento.setCodigoCliente(empleado.getClienteEmp().getCodigo());
		}
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAsp().getId());
		List<RequerimientoElemento> requerimientoElementos = null;
		if(referenciaElementos != null && referenciaElementos.size() > 0){
			Long idReg = (Long) session.getAttribute("idRequerimientoElemento");
			requerimientoElementos = obtenerLista(referenciaElementos,idReg);
		}
		atributos.put("requerimientoElementos", requerimientoElementos);
		session.setAttribute("requerimientoElementosSession", requerimientoElementos);
		session.setAttribute("requerimientoElementoBusqueda", requerimientoElemento);
		/// return the concrete view
		return "consultaReferencia";
	}
	/**
	  * This Method returns all the files on the
	  * Database.
	  * 
	  * @return List<String> of files
	  * @throws Exception
	  */
	synchronized void getArchives(String value) throws Exception{
		try{
			/// we crate an empty array for the dump of the array
			File archivoValue = new File(value);
			value = value.replace(archivoValue.separator, "//");
			CampoComparacion a = new CampoComparacion("pathLegajo",value);
			this.archives = referenciaService.listarTodosFiltradoPorLista(a);
		}
		catch(Exception e) {
			logger.error("No se pudo exportar el ZIP",e);
			throw new Exception();
		}
	}
	
	/**
	  * This Method returns all the matching files with the
	  * Database.
	  * 
	  * @param List<String> of files
	  * @return List<String> of files
	  * @throws Exception
	  */
	List<String[]> matchArchives(List<String> listaArchivos, String codigoCliente) throws Exception{

		try{
			/// we crate an empty array for the dump of the array
			List<String[]> referenciaArchivos = new ArrayList<String[]>();
			String referenciaArchivo[] = new String[3]; 
			Referencia referencia;

			/// now we iterate on the entered files and the stored ones
			for(String itemArchivos: listaArchivos) {
				getArchives(itemArchivos);
				referencia  = archives.isEmpty()?null:archives.get(0);
				if (referencia != null){
					String searchArchivo = new File(referencia.getPathLegajo().trim()).getPath(); 

					boolean clienteIngresado = false;
					boolean elementoCreado = false;
					if (referencia.getElemento().getClienteEmp().getCodigo().equals(codigoCliente) || codigoCliente.equals(""))
						clienteIngresado = true;

					String searchArchivoTmp = new File(searchArchivo.trim().toLowerCase()).toString();
					if(itemArchivos.trim().toLowerCase().equals(searchArchivoTmp) && referencia.getElemento() != null && (clienteIngresado == true)){
						referenciaArchivo[0] = referencia.getElemento().getCodigo();
						referenciaArchivo[1] = referencia.getElemento().getClienteEmp().getCodigo();
						referenciaArchivo[2] = referencia.getNumero1Str();

						// Verifies if the current set of elemento/cliente was currently added to the list. 
						for(int j=0; j<referenciaArchivos.size(); j++){
							if(referenciaArchivos.get(j)[0].equals(referenciaArchivo[0]) && referenciaArchivos.get(j)[1].equals(referenciaArchivo[1]) && referenciaArchivos.get(j)[2].equals(referenciaArchivo[2]) )
								elementoCreado = true;
						}								

						if (!elementoCreado){
							referenciaArchivos.add(referenciaArchivo.clone());
						}

					}
				}
			}
			// returns file's information found
			return referenciaArchivos;
		}
		catch(Exception e) {
			logger.error("No se pudo exportar el ZIP",e);
			throw new Exception();
		}
	}
	 
	
	@RequestMapping(value="/filtrarRequerimientoElemento.html", method = RequestMethod.POST)
	public String filtrarRequerimientoElemento(
			@ModelAttribute("requerimientoElementoBusqueda") RequerimientoElementoBusquedaForm requerimientoElemento, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(requerimientoElemento, result);
		if(!result.hasErrors()){
			session.setAttribute("requerimientoElementoBusqueda", requerimientoElemento);
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarRequerimientoElemento(session, atributos, null, null, null, null, null,request);
	}
	
	@RequestMapping(value="/filtrarReferencia.html", method = RequestMethod.POST)
	public String filtrarReferencia(
			@ModelAttribute("requerimientoElementoBusqueda") RequerimientoElementoBusquedaForm requerimientoElemento, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request){
		//buscamos en la base de datos y lo subimos a request.
		this.validator.validate(requerimientoElemento, result);
		if(!result.hasErrors()){
			session.setAttribute("requerimientoElementoBusqueda", requerimientoElemento);
			if(request.getParameter("listaPalabras") != null) {
				session.setAttribute("listaPalabras", request.getParameter("listaPalabras"));
			}
			atributos.put("errores", false);
			atributos.remove("result");
		}else{
			atributos.put("errores", true);
			atributos.put("result", result);			
		}	
		return mostrarReferencia(session, atributos, request);
	}
	
	
	@RequestMapping(value="/refrescarConsultaReferencia.html", method = RequestMethod.POST)
	public String refrescarConsultaReferenci(
			@ModelAttribute("requerimientoElementoBusqueda") RequerimientoElementoBusquedaForm requerimientoElemento, 
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos,
			HttpServletRequest request){
		
		if(requerimientoElemento.getCodigoClasificacionDocumental()!=null && !requerimientoElemento.getCodigoClasificacionDocumental().equals("")){
			requerimientoElemento.setClasificacionDocumental(clasificacionDocumentalService.getClasificacionByCodigo(Integer.valueOf(requerimientoElemento.getCodigoClasificacionDocumental()), requerimientoElemento.getCodigoCliente(), obtenerClienteAsp(), "I"));
		}else{
			requerimientoElemento.setClasificacionDocumental(null);
		}
		
		
		
		if(requerimientoElemento.getSeleccion().equalsIgnoreCase("unico") && requerimientoElemento.getClasificacionDocumental()!=null && !requerimientoElemento.getClasificacionDocumental().getIndiceIndividual()){
			requerimientoElemento.setClasificacionDocumental(null);
			requerimientoElemento.setCodigoClasificacionDocumental("");
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioLoteReferencia.notificacion.clasificacionDocumentalNoSoportaIndiceIndividual", null);
			avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
		}
		if(requerimientoElemento.getSeleccion().equalsIgnoreCase("grupo") && requerimientoElemento.getClasificacionDocumental()!=null && !requerimientoElemento.getClasificacionDocumental().getIndiceGrupal()){
			requerimientoElemento.setClasificacionDocumental(null);
			requerimientoElemento.setCodigoClasificacionDocumental("");
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioLoteReferencia.notificacion.clasificacionDocumentalNoSoportaIndiceGrupal", null);
			avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
		}
		
		//buscamos en la base de datos y lo subimos a request.
		atributos.put("size", Integer.valueOf(0));
		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAsp().getId());
		
		session.setAttribute("requerimientoElementosSession", null);
		session.setAttribute("requerimientoElementoBusqueda", requerimientoElemento);
			
		if(request.getParameter("isRequerimiento")!=null && request.getParameter("isRequerimiento").equalsIgnoreCase("true"))
		{
			return "consultaRequerimientoElemento";
		}
		else
			return "consultaReferencia";
	}
	
	@RequestMapping(
			value="/verImagenesRearchivo.html",
			method = RequestMethod.GET
		)
	public String verImagenesRearchivo(
			@RequestParam(value="fileName",required=false) String fileName,
			@RequestParam(value="pos",required=false) int pos,
			HttpSession session, Map<String,Object> atributos, HttpServletResponse response	){
		
		//fileName="c://1.JPG";
		if(fileName==null || "".equals(fileName))
			return "formularioRearchivoDigital"; //JSP en blanco
		 
		int pos1 = fileName.lastIndexOf("/");
		int pos2 = fileName.lastIndexOf(".");
		
		String carpeta = fileName.substring(pos1, pos2);
		
		fileName = fileName.substring(0, pos1) + carpeta + "//" + pos + ".jpg";
		
		ServletOutputStream op = null;
		try {
			String urlFile = fileName;
			InputStream in = new FileInputStream(urlFile);
			byte[] data = new byte[in.available()];
			in.read(data);
			
			response.setContentType("image/jpeg;");
			//response.setHeader("Content-Disposition","attachment;filename=\"" + fileNameEnviar +"\";");
			response.setContentLength(data.length);
			op = response.getOutputStream();

			op.write(data);
			op.flush();
			op.close();

		} catch (IOException e) {
			
			try {
				op.close();
			} catch (Exception e1) {
				return "formularioRearchivo";
			}
			
		} catch (Exception e) {
			return "formularioRearchivo";
		};
		return "formularioRearchivo";
		
	}
	
	@RequestMapping(
			value="/abrirFancyBoxImagenesRearchivo.html",
			method = RequestMethod.GET
		)
	public String abrirFancyBoxImagenesRearchivo(
			@RequestParam(value="fileName",required=false) String fileName,
			@RequestParam(value="idReferencia",required=false) Long idReferencia,
			HttpSession session, 
			Map<String,Object> atributos, 
			HttpServletResponse response	){
		
		String nombreArchivo= "";
		int cantidadImagenes = 0;
		
		if(fileName==null || "".equals(fileName)){
			cantidadImagenes = 0;
			fileName = "";
		}
		else
		{
			if(fileName!=null && fileName.endsWith(".tif")){
				try{
					File fileTIFF = new File(fileName);
					SeekableStream s = new FileSeekableStream(fileTIFF);
					
					TIFFDecodeParam param = null;
					
			        ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
					
			        /*
			         * create object of RenderedIamge to produce
			         * image data in form of Rasters
			         */
			        RenderedImage renderedImage[];
			        
			        /*
			         * SeekabaleStream is use for taking input from file.
			         * FileSeekableStream is not committed part of JAI API.
			         */
			        renderedImage = new RenderedImage[dec.getNumPages()];
		
			        /* count no. of pages available inside input tiff file */
			        int count = 0;
			        for (int j = 0; j < dec.getNumPages(); j++) {
			            renderedImage[j] = dec.decodeAsRenderedImage(j);
			            count++;
			        }
			        
			        cantidadImagenes = count;
				}
				catch(Exception e) {
					return "popUpImagenesFancyBox";
				}
			}
			else if(fileName!=null && fileName.endsWith(".pdf"))
			{
				nombreArchivo = fileName.substring(fileName.lastIndexOf('/'), fileName.length());
			}
			else
			{
				nombreArchivo = rearchivoService.obtenerPorReferencia(idReferencia);
			}
		}
		
		atributos.put("nombreArchivoDigital", nombreArchivo);
		atributos.put("cantidadImagenes", cantidadImagenes);
		atributos.put("pathArchivoDigital", fileName);
		
		return "popUpImagenesFancyBox";
	}
	
	@RequestMapping(value = "/verLegajo.html", method = RequestMethod.GET)
	public void verLegajo(
			@RequestParam(value = "fileName", required = false) String fileName,
			HttpSession session, Map<String, Object> atributos,
			HttpServletResponse response) {

		if(fileName==null || "".equals(fileName))
			return;
		ServletOutputStream op = null;
		try {
			
			String urlFile = fileName;
			InputStream in = new FileInputStream(urlFile);
			byte[] data = new byte[in.available()];
			in.read(data);
			
			if(fileName.toLowerCase().endsWith(".pdf"))
				response.setContentType("application/pdf");
			else if(fileName.toLowerCase().endsWith(".gif"))
				response.setContentType("image/gif");
			else if(fileName.toLowerCase().endsWith(".png"))
				response.setContentType("image/png");
			else if(fileName.toLowerCase().endsWith(".tif") || fileName.toLowerCase().endsWith(".tiff"))
				response.setContentType("image/tiff");
			else if(fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg"))
				response.setContentType("image/jpeg");
			else if(fileName.toLowerCase().endsWith(".xls"))
				response.setContentType("application/vnd.ms-excel");
			else if(fileName.toLowerCase().endsWith(".xlsx"))
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			else{
				response.setContentType("application/octet-stream;");
				response.setHeader("Content-Disposition", "attachment;filename=\""+ fileName + "\";");
			}
			
			response.setContentLength(data.length);
			op = response.getOutputStream();

			op.write(data);
			op.flush();
			op.close();
			in.close();

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
		;
	}
	
	@RequestMapping(
	value="/descargarImagenesZIP.html",
	method= RequestMethod.GET
)
public String descargarImagenesZIP(HttpSession session,
		Map<String,Object> atributos,			
		HttpServletResponse response,
		@RequestParam(value="seleccionados", required=false) String seleccionados){
		response.setContentType("application/zip");
		List<RequerimientoElemento> listaReferencias1 = (List<RequerimientoElemento>) session.getAttribute("requerimientoElementosSession");
		RequerimientoElemento Name = listaReferencias1.get(0);
		String NombreZip = "";
		if (Name.getCodigoContenedor()!=null) {
		NombreZip = Name.getCodigoContenedor().toString();
		}else {
		NombreZip = "Imagenes";
		}
        response.setHeader( "Content-disposition", "attachment; filename="+NombreZip+".zip");
		byte[] buffer = new byte[1024];
		
		try{
		
			List<Rearchivo> rearchivos = new ArrayList<Rearchivo>();
			List<RequerimientoElemento> listaReferencias = (List<RequerimientoElemento>) session.getAttribute("requerimientoElementosSession");
			
			if(seleccionados != null && seleccionados.length()> 0){
					
				String[] ids = seleccionados.split(",");
				Long[] idsLong = new Long[ids.length];
				for(int i = 0;i<ids.length;i++)
				{
					for(RequerimientoElemento req:listaReferencias){
						if(req.getIdReferencia().longValue()==Long.valueOf(ids[i]))
						{
							if(req.getPathLegajo()!=null && !req.getPathLegajo().equalsIgnoreCase("")){
								Rearchivo rear = new Rearchivo();
								rear.setPathArchivoJPGDigital(req.getPathLegajo().substring(0, req.getPathLegajo().lastIndexOf("\\")+1));
								rear.setNombreArchivoDigital(req.getPathLegajo().substring(req.getPathLegajo().lastIndexOf("\\")+1,req.getPathLegajo().length()));
								rearchivos.add(rear);
								break;
							}
							else
							{
								Rearchivo rear = rearchivoService.obtenerRearchivoPorReferencia(Long.valueOf(ids[i]));
								String carpeta = rear.getPathArchivoJPGDigital().substring(0, rear.getPathArchivoJPGDigital().lastIndexOf("/")-1);
								carpeta = carpeta.substring(0, carpeta.lastIndexOf("/")+1);
								rear.setPathArchivoJPGDigital(carpeta);
								rearchivos.add(rear);
								break;
							}
						}
					}
				}
				ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream()); 
			
				for(Rearchivo rearchivo : rearchivos){
					
					File file = new File(rearchivo.getPathArchivoJPGDigital()+rearchivo.getNombreArchivoDigital());
					FileInputStream inp = new FileInputStream(file);
					
					ZipEntry entry = new ZipEntry(file.getName());
				    zipOut.putNextEntry(entry);
				    
				    int len;
		    		while ((len = inp.read(buffer)) > 0) {
		    			zipOut.write(buffer, 0, len);
		    		}

		    		inp.close();
		    	}
		    		
				zipOut.closeEntry();
		    	//remember close it
				zipOut.flush();
                zipOut.close();
			}
	
	}
	catch(Exception e) {
		logger.error("No se pudo exportar el ZIP",e);
	}
	
	return "";
}
	
	
	@RequestMapping(
			value="/descargarImagenesPDF.html",
			method= RequestMethod.GET
		)
	public String descargarImagenesPDF(HttpSession session,
			Map<String,Object> atributos,			
			HttpServletResponse response,
			@RequestParam(value="seleccionados", required=false) String seleccionados){

			response.setContentType("application/pdf");
	        response.setHeader( "Content-disposition", "attachment; filename=Imagenes.pdf");
			
			try{
			
				List<Rearchivo> rearchivos = new ArrayList<Rearchivo>();
				List<RequerimientoElemento> listaReferencias = (List<RequerimientoElemento>) session.getAttribute("requerimientoElementosSession");
				
				// INICIO SELECCIONADOS
				if(seleccionados != null && seleccionados.length()> 0){
					
					InputStream inputStream = null;
					OutputStream outputStream = response.getOutputStream();
					Collection<ImagenExportar> source = new ArrayList<ImagenExportar>();	
					List<File> imagenesTIFF = new ArrayList<File>();
					List<File> imagenesPDF = new ArrayList<File>();
					
					String[] ids = seleccionados.split(",");
					Long[] idsLong = new Long[ids.length];
					for(int i = 0;i<ids.length;i++){
						for(RequerimientoElemento req:listaReferencias){
							if(req.getIdReferencia().longValue()==Long.valueOf(ids[i]))
							{
								if(req.getPathLegajo()!=null && !req.getPathLegajo().equalsIgnoreCase("")){
									Rearchivo rear = new Rearchivo();
									rear.setPathArchivoJPGDigital(req.getPathLegajo().substring(0, req.getPathLegajo().lastIndexOf("\\")+1));
									rear.setNombreArchivoDigital(req.getPathLegajo().substring(req.getPathLegajo().lastIndexOf("\\")+1,req.getPathLegajo().length()));
									rearchivos.add(rear);
									break;
								}
								else
								{
									Rearchivo rear = rearchivoService.obtenerRearchivoPorReferencia(Long.valueOf(ids[i]));
									String carpeta = rear.getPathArchivoJPGDigital().substring(0, rear.getPathArchivoJPGDigital().lastIndexOf("/")-1);
									carpeta = carpeta.substring(0, carpeta.lastIndexOf("/")+1);
									rear.setPathArchivoJPGDigital(carpeta);
									rearchivos.add(rear);
									break;
								}
							}
						}
					}
					
					for(Rearchivo rearchivo : rearchivos){
						if(rearchivo.getNombreArchivoDigital().toLowerCase().endsWith(".tif") 
								|| rearchivo.getNombreArchivoDigital().toLowerCase().endsWith(".tiff"))
							imagenesTIFF.add(new File(rearchivo.getPathArchivoJPGDigital()+rearchivo.getNombreArchivoDigital()));
						else if(rearchivo.getNombreArchivoDigital().toLowerCase().endsWith(".pdf"))
							imagenesPDF.add(new File(rearchivo.getPathArchivoJPGDigital()+rearchivo.getNombreArchivoDigital()));	
					}
						
					if(imagenesTIFF.size()>0)
					{
						
						for(File fileTIFF : imagenesTIFF){
							
							SeekableStream s = new FileSeekableStream(fileTIFF);
					
							//configuracion del lector de TIF
							TIFFDecodeParam param = null;
					        ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
					        RenderedImage renderedImage[];
					        renderedImage = new RenderedImage[dec.getNumPages()];
				
					        int count = 0;
					        for (int j = 0; j < dec.getNumPages(); j++) {
					            renderedImage[j] = dec.decodeAsRenderedImage(j);
					            count++;
					        }
					        
					        //levantamos las imï¿½genes del archivo...
					        for(int pos = 0; pos<count; pos++){
					        	
					        	BufferedImage original = convertRenderedImage(renderedImage[pos]);
					        	BufferedImage scaled = Scalr.resize(original, Method.ULTRA_QUALITY, 1100);
					        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
					        	ImageIO.write( scaled, "jpg", baos );
					        	baos.flush();
					        	ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
					        
					        	source.add(new ImagenExportar(bais));
					        }
						}
				       
						//exportamos
						if(compiledJasperReport==null){
							String basePath = session.getServletContext().getRealPath("/WEB-INF/classes/jasper/");
							compiledJasperReport = JasperCompileManager.compileReport(basePath+"/imagesExport.jrxml");
						}
						
						Map<String,Object> parametros=new HashMap<String,Object>();
						JRDataSource datos = new JRBeanCollectionDataSource(source);
						JasperPrint jasperPrint = JasperFillManager.fillReport(compiledJasperReport, parametros, datos);
						clearBlankPages(jasperPrint);
						
						inputStream = new ByteArrayInputStream(JasperExportManager.exportReportToPdf(jasperPrint));
					}
				
					if(imagenesPDF.size()>0)
					{
						try{
							
							Document document = new Document(PageSize.A4);
						    document.setMargins(0F, 0F, 0F, 0F);
						   
						    PdfCopy pdfCopy = new PdfCopy(document, outputStream);
						    pdfCopy.setMargins(0, 0, 0, 0);
						    document.open();
	
						    PdfReader pdfReader = null; 
						    
						    if(inputStream!=null)
						    {
						    	pdfReader = new PdfReader(inputStream);
						    	
						        for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
						            pdfCopy.addPage(pdfCopy.getImportedPage(pdfReader, i));
						        }
						        
						        pdfCopy.freeReader(pdfReader);
						        pdfReader.close();
						    }
	
						    for (File arquivo : imagenesPDF)
						    {
						    	inputStream = new FileInputStream(arquivo);
								
						    	pdfReader = new PdfReader(inputStream);
	
						        for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
						            pdfCopy.addPage(pdfCopy.getImportedPage(pdfReader, i));
						        }
						        pdfCopy.freeReader(pdfReader);
						        pdfReader.close();
						    }
						    
						    document.close();
						    
						    outputStream.flush();
						    outputStream.close();
					    
						} catch (DocumentException e) {
							e.printStackTrace();
						}
						
					}
					else
					{
						IOUtils.copy(inputStream,outputStream);
						inputStream.close();
						outputStream.flush();
						outputStream.close();
					}
					
					
			       
	        
			}//FIN CONDICION SELECCIONADOS
	        
		}
		catch(Exception e) {
			logger.error("no se pudo exportar a PDF",e);
			atributos.put("avisoMsj", "Hubo un error al enviar el correo. Por favor, intente nuevamente.");
			return "formMailImagenesPopUp";
		}
		
		atributos.put("avisoMsj", "El correo se ha enviado con ï¿½xito.");
		return "formMailImagenesPopUp";
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////// METODOS AUXILIARES //////////////////////////////////////////////////
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	private String obtenerCodigoEmpresaUser(){
		User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if(user.getPersona()!=null && user.getPersona() instanceof PersonaFisica){
			return ((PersonaFisica)user.getPersona()).getEmpresaDefecto().getCodigo();
		}
		return null;
	}
	private String obtenerCodigoSucursalUser(){
		User user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if(user.getPersona()!=null && user.getPersona() instanceof PersonaFisica){
			return ((PersonaFisica)user.getPersona()).getSucursalDefecto().getCodigo();
		}
		return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private List<RequerimientoElemento> obtenerLista (List<Object> entrada, Long idReq){
		List <RequerimientoElemento> salida = new ArrayList<RequerimientoElemento>();
		for(Object ob:entrada){
			Object[] lista = (Object[]) ob;
			RequerimientoElemento requerimientoElemento = new RequerimientoElemento((BigDecimal)lista[0], (String)lista[1],
					(String) lista[2], (String) lista[3], (String)lista[4], (String)lista[5], (Date) lista[6], (Date) lista[7], (String)lista[8], 
					(String)lista[9], (BigDecimal)lista[10], (BigDecimal)lista[11], (String)lista[12], (String)lista[13], (String)lista[14], 
					(String)lista[15], (BigDecimal)lista[16], (Integer)lista[17],(String)lista[18],(BigDecimal)lista[19],(String) lista[20],
					(String) lista[21], (Date)lista[22], (String) lista[23],(String) lista[24],(String) lista[25],(String) lista[26],(BigDecimal) lista[27]);
//			//Busco si ya fue asignado a otro requerimiento y si esta pendiente o en proceso
//			RequerimientoReferencia requerimientoReferencia = requerimientoReferenciaService.obtenerPendienteOEnProceso(requerimientoElemento.getIdReferencia(),idReq);
//			if(requerimientoReferencia == null)
//				requerimientoElemento.utilizado = false;
//			else
//				requerimientoElemento.utilizado = true;
			salida.add(requerimientoElemento);
		}
		return salida;
	}
	
	public class RequerimientoElemento{
		private Long idReferencia;
		private String descripcionTipoElemento;
		private String codigoElemento;
		private String codigoContenedor;
		private String clasificacionDocumental;
		private String descripcionReferencia;
		private String estadoElemento;
		private Date fecha1;
		private Date fecha2;
		private String texto1;
		private String texto2;
		private Long numero1;
		private Long numero2;
		private String descripcionRearchivo;
		private String posicionEle;
		private String posicionCon;
		private Long idLoteReferencia;
		private String pathArchivoDigital;
		private Long refRearchivo;
		private String cliente;
		private String pathLegajo;
		private Date fechaHora;
		private String enReq;
		private String userAsig;
		private String descripcionTarea;
		private String estadoContenedor;
		private Long  cantImagenes;
		private String  elemento;
		private String  caja;
		private String pathLegajo1;
		private String fechaRegistro;
		
		public RequerimientoElemento (BigDecimal id, String desTE, String codEl, String clasificacionDocumental, String desRef, String estEl, Date fecha1,
				Date fecha2, String texto1, String texto2, BigDecimal num1, BigDecimal num2, String descRearchivo, String codCon,String posicionEle, 
				String posicionCon, BigDecimal idLoteReferencia, Integer ordenRearchivo, String pathArchivoDigital, BigDecimal refRearchivo, 
				String cliente, String pathLegajo, Date fechaHora, String enReq,String userAsig,String descripcionTarea,String estadoContenedor,BigDecimal cImagenes){
			if(id != null)
				this.idReferencia = Long.parseLong(id.toString());
			this.descripcionTipoElemento = desTE;
			this.codigoElemento = codEl;
			this.clasificacionDocumental = clasificacionDocumental;
			this.descripcionReferencia = desRef;
			this.estadoElemento = estEl;
			this.fecha1 = fecha1;
			this.fecha2 = fecha2;
			this.texto1 = texto1;
			this.texto2 = texto2;
			if(num1 != null)
				this.numero1 = Long.parseLong(num1.toString());
			if(num2 != null)
				this.numero2 = Long.parseLong(num2.toString());
			this.descripcionRearchivo = (descRearchivo != null)? descRearchivo+" / "+((ordenRearchivo!=null)?ordenRearchivo.toString():"-" ): "" ;
			if(codCon!=null && !"".equals(codCon)){
				this.codigoContenedor = codCon;
			}else if(descRearchivo!=null && !descRearchivo.equals("")){
				this.codigoContenedor = codEl;				
			}
			this.posicionEle = posicionEle;
			this.posicionCon = posicionCon;
			if(idLoteReferencia!=null)
					this.idLoteReferencia = Long.parseLong(idLoteReferencia.toString());
			this.pathArchivoDigital = pathArchivoDigital;
			if(refRearchivo!=null){
				this.refRearchivo = Long.parseLong(refRearchivo.toString());}
			this.cliente = cliente;
			this.pathLegajo = pathLegajo;
			this.fechaHora = fechaHora;
			this.enReq = enReq;
			this.userAsig = userAsig;
			this.descripcionTarea = descripcionTarea;
			this.estadoContenedor = estadoContenedor;
			if(cImagenes != null) {
			this.cantImagenes = Long.parseLong(cImagenes.toString());
			}else {
				this.cantImagenes = (long) 0;
			}
		
			this.pathLegajo1 = "=HIPERVINCULO(\""+codigoContenedor+"\\"+codigoElemento+".pdf\""+";\"Imagen\")";
			
			this.elemento = "Leg: "+ codigoElemento;
			
			this.caja = "Caja: " + codigoContenedor;
			
			
			}
		
		public Long getIdReferencia() {
			return idReferencia;
		}
		public void setIdReferencia(Long idReferencia) {
			this.idReferencia = idReferencia;
		}
		public String getDescripcionTipoElemento() {
			return descripcionTipoElemento;
		}
		public void setDescripcionTipoElemento(String descripcionTipoElemento) {
			this.descripcionTipoElemento = descripcionTipoElemento;
		}
		public String getCodigoElemento() {
			return codigoElemento;
		}
		public void setCodigoElemento(String codigoElemento) {
			this.codigoElemento = codigoElemento;
		}
		public String getDescripcionReferencia() {
			return descripcionReferencia;
		}
		public void setDescripcionReferencia(String descripcionReferencia) {
			this.descripcionReferencia = descripcionReferencia;
		}
		public String getEstadoElemento() {
			return estadoElemento;
		}
		public void setEstadoElemento(String estadoElemento) {
			this.estadoElemento = estadoElemento;
		}
		
		public Date getFecha1() {
			return fecha1;
		}

		public void setFecha1(Date fecha1) {
			this.fecha1 = fecha1;
		}

		public Date getFecha2() {
			return fecha2;
		}

		public void setFecha2(Date fecha2) {
			this.fecha2 = fecha2;
		}

		public String getTexto1() {
			return texto1;
		}

		public void setTexto1(String texto1) {
			this.texto1 = texto1;
		}

		public String getTexto2() {
			return texto2;
		}

		public void setTexto2(String texto2) {
			this.texto2 = texto2;
		}

		public Long getNumero1() {
			return numero1;
		}

		public void setNumero1(Long numero1) {
			this.numero1 = numero1;
		}

		public Long getNumero2() {
			return numero2;
		}

		public void setNumero2(Long numero2) {
			this.numero2 = numero2;
		}

		public String getDescripcionRearchivo() {
			return descripcionRearchivo;
		}

		public void setDescripcionRearchivo(String descripcionRearchivo) {
			this.descripcionRearchivo = descripcionRearchivo;
		}

		public String getCodigoContenedor() {
			return codigoContenedor;
		}

		public void setCodigoContenedor(String codigoContenedor) {
			this.codigoContenedor = codigoContenedor;
		}

		public String getPosicionEle() {
			return posicionEle;
		}

		public void setPosicionEle(String posicionEle) {
			this.posicionEle = posicionEle;
		}

		public String getPosicionCon() {
			return posicionCon;
		}

		public void setPosicionCon(String posicionCon) {
			this.posicionCon = posicionCon;
		}
		
		public Long getIdLoteReferencia() {
			return idLoteReferencia;
		}

		public void setIdLoteReferencia(Long idLoteReferencia) {
			this.idLoteReferencia = idLoteReferencia;
		}
		
		public String getPathArchivoDigital() {
			return pathArchivoDigital;
		}

		public void setPathArchivoDigital(String pathArchivoDigital) {
			this.pathArchivoDigital = pathArchivoDigital;
		}
		
		public Long getRefRearchivo() {
			return refRearchivo;
		}

		public void setRefRearchivo(Long refRearchivo) {
			this.refRearchivo = refRearchivo;
		}

		public String getFecha1Str() {
			if(fecha1==null)
				return "";
			return formatoFechaFormularios.format(fecha1);
		}
		public String getFecha2Str() {
			if(fecha2==null)
				return "";
			return formatoFechaFormularios.format(fecha2);
		}

		public String getClasificacionDocumental() {
			return clasificacionDocumental;
		}

		public void setClasificacionDocumental(String clasificacionDocumental) {
			this.clasificacionDocumental = clasificacionDocumental;
		}

		public String getCliente() {
			return cliente;
		}

		public void setCliente(String cliente) {
			this.cliente = cliente;
		}

		public String getPathLegajo() {
			return pathLegajo;
		}

		public void setPathLegajo(String pathLegajo) {
			this.pathLegajo = pathLegajo;
		}

		public Date getFechaHora() {
			return fechaHora;
		}

		public void setFechaHora(Date fechaHora) {
			this.fechaHora = fechaHora;
		}
		
		public String getFechaHoraStr(){
			if(this.fechaHora==null)
				return "";
			return Configuracion.formatoFechaHoraFormularios.format(this.fechaHora);
		}

		public String getEnReq() {
			return enReq;
		}

		public void setEnReq(String enReq) {
			this.enReq = enReq;
		}

		public String getUserAsig() {
			return userAsig;
		}

		public void setUserAsig(String userAsig) {
			this.userAsig = userAsig;
		}

		public String getDescripcionTarea() {
			return descripcionTarea;
		}

		public void setDescripcionTarea(String descripcionTarea) {
			this.descripcionTarea = descripcionTarea;
		}

		public String getEstadoContenedor() {
			return estadoContenedor;
		}

		public void setEstadoContenedor(String estadoContenedor) {
			this.estadoContenedor = estadoContenedor;
		}

		public Long getCantImagenes() {
			return cantImagenes;
		}

		public void setCantImagenes(Long cantImagenes) {
			this.cantImagenes = cantImagenes;
		}

		public String getPathLegajo1() {
			return pathLegajo1;
		}

		public void setPathLegajo1(String pathLegajo1) {
			this.pathLegajo1 = pathLegajo1;
		}

		public String getElemento() {
			return elemento;
		}

		public void setElemento(String elemento) {
			this.elemento = elemento;
		}

		public String getCaja() {
			return caja;
		}

		public void setCaja(String caja) {
			this.caja = caja;
		}

		public String getFechaRegistro() {
			return fechaRegistro;
		}

		public void setFechaRegistro(String fechaRegistro) {
			this.fechaRegistro = fechaRegistro;
		}

	}
	
	public class ImagenExportar{
		private InputStream source;
		
		public ImagenExportar(InputStream source){
			this.source=source;
		}

		public void setSource(InputStream source) {
			this.source = source;
		}

		public InputStream getSource() {
			return source;
		}
	}
	
	public BufferedImage convertRenderedImage(RenderedImage img) {
	    if (img instanceof BufferedImage) {
	        return (BufferedImage)img;  
	    }   
	    ColorModel cm = img.getColorModel();
	    int width = img.getWidth();
	    int height = img.getHeight();
	    WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
	    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
	    Hashtable properties = new Hashtable();
	    String[] keys = img.getPropertyNames();
	    if (keys!=null) {
	        for (int i = 0; i < keys.length; i++) {
	            properties.put(keys[i], img.getProperty(keys[i]));
	        }
	    }
	    BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
	    img.copyData(raster);
	    return result;
	}
	
	private void clearBlankPages(JasperPrint jasperPrint){
		for (Iterator<JRPrintPage> i=jasperPrint.getPages().iterator(); i.hasNext();) {
			JRPrintPage page = i.next();
			if (page.getElements().size() == 0)
				i.remove();
		}
	}
}
