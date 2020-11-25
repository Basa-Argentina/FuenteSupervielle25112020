/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dividato.configuracionGeneral.objectForms.LoteRearchivoBusquedaForm;
import com.dividato.configuracionGeneral.utils.ZipUtil;
import com.dividato.configuracionGeneral.validadores.LoteRearchivoValidator;
import com.dividato.configuracionGeneral.validadores.RearchivoValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteRearchivoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RearchivoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.LoteRearchivo;
import com.security.modelo.configuraciongeneral.LoteReferencia;
import com.security.modelo.configuraciongeneral.Rearchivo;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.servicios.MailManager;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;

/**
 * Controlador que se utiliza para los servicios asociados a la administracion
 * de los Lotes de Rearchivos.
 *
 * @author Gabriel Mainero
 */

@Controller
@RequestMapping(
		value=
			{	
				"/consultaLotesRearchivo.html",
				"/borrarFiltrosLoteRearchivo.html",
				"/eliminarLoteRearchivo.html",
				"/iniciarFormularioLoteRearchivo.html",
				"/precargaFormularioLoteRearchivo.html",
				"/seccionRearchivos.html",
				"/agregarRearchivo.html",
				"/eliminarRearchivo.html",
				"/refrescarFormRearchivo.html",
				"/guardarActualizarLoteRearchivo.html",
				"/importarArchivoLoteRearchivo.html",
				"/downloadFileLoteRearchivo.html",
				"/viewFileJPGLoteRearchivo.html"
			}
		)
public class FormLoteRearchivoController {
	private static Logger logger=Logger.getLogger(FormLoteRearchivoController.class);
	
	private LoteRearchivoService loteRearchivoService;
	private LoteReferenciaService loteReferenciaService;
	private ClasificacionDocumentalService clasificacionDocumentalService;
	
	private ClienteEmpService clienteEmpService;
	private SucursalService sucursalService;
	private EmpresaService empresaService;
	private ElementoService elementoService;
	private RemitoService remitoService;
	private ReferenciaService referenciaService;
	private RearchivoService rearchivoService;
	
	private LoteRearchivoValidator loteValidator;
	private RearchivoValidator refValidator;
	private MailManager mailManager;
	private UserService userService;
	
	@Autowired
	public void setServices(LoteRearchivoService loteRearchivoService,
			LoteReferenciaService loteReferenciaService,
			ClasificacionDocumentalService clasificacionDocumentalService,
			SucursalService sucursalService,
			ClienteEmpService clienteEmpService,
			EmpresaService empresaService,
			ElementoService elementoService,
			RemitoService remitoService,
			ReferenciaService referenciaService,
			RearchivoService rearchivoService
			) {
		this.loteRearchivoService = loteRearchivoService;
		this.loteReferenciaService = loteReferenciaService;
		this.clasificacionDocumentalService = clasificacionDocumentalService;
		this.sucursalService = sucursalService;
		this.clienteEmpService = clienteEmpService;
		this.empresaService = empresaService;
		this.elementoService = elementoService;
		this.remitoService = remitoService;
		this.referenciaService = referenciaService;
		this.rearchivoService = rearchivoService;
	}
	
	@Autowired
	public void setMailManager(MailManager mailManager){
		this.mailManager=mailManager;
	}
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService=userService;
	}
	
	@Autowired
	public void setValidator(LoteRearchivoValidator validator,RearchivoValidator refValidator) {
		this.loteValidator = validator;
		this.refValidator = refValidator;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		if(binder.getTarget() instanceof LoteRearchivoBusquedaForm){
			binder.registerCustomEditor(Date.class, "fechaDesde",new CustomDateEditor(formatoFechaFormularios, true));
			binder.registerCustomEditor(Date.class, "fechaHasta",new CustomDateEditor(formatoFechaFormularios, true));
		}else if (binder.getTarget() instanceof LoteRearchivo){
			loteValidator.initDataBinder(binder);
		}else if (binder.getTarget() instanceof Rearchivo){
			refValidator.initDataBinder(binder);
		}		
	}
	
	@RequestMapping(value="/consultaLotesRearchivo.html")
	public String consultaLotesRearchivo(Map<String,Object> atributos,
			@ModelAttribute("busquedaLoteRearchivoFormulario") LoteRearchivoBusquedaForm busquedaLoteRearchivo,
			HttpServletRequest request) {
		if(busquedaLoteRearchivo==null)
			busquedaLoteRearchivo=new LoteRearchivoBusquedaForm();
		if(busquedaLoteRearchivo.getIdClienteAsp()==null){
			busquedaLoteRearchivo.setIdClienteAsp(obtenerClienteAspUser().getId());
		}
		//cuenta la cantidad de resultados
		Integer size = loteRearchivoService.contarObtenerPor(
				obtenerClienteAspUser(),
				busquedaLoteRearchivo.getCodigoEmpresa(),
				busquedaLoteRearchivo.getCodigoSucursal(),
				busquedaLoteRearchivo.getCodigoCliente(),
				busquedaLoteRearchivo.getCodigoDesde(),
				busquedaLoteRearchivo.getCodigoHasta(),
				busquedaLoteRearchivo.getFechaDesde(),
				busquedaLoteRearchivo.getFechaHasta(),
				busquedaLoteRearchivo.getTipo(),
				busquedaLoteRearchivo.getCodigoClasificacionDocumental(),
				busquedaLoteRearchivo.getCodigoContenedor()
				);
		
			atributos.put("size", size);
		
			//paginacion y orden de resultados de displayTag
			busquedaLoteRearchivo.setTamañoPagina(10);	
			if(request != null){
				String nPaginaStr= request.getParameter((new ParamEncoder("loteRearchivo").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
				if(nPaginaStr==null){
					nPaginaStr = (String)atributos.get((new ParamEncoder("loteRearchivo").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
				}
				String fieldOrder = request.getParameter( new ParamEncoder("loteRearchivo").encodeParameterName(TableTagParameters.PARAMETER_SORT));
				busquedaLoteRearchivo.setFieldOrder(fieldOrder);
				String sortOrder = request.getParameter(new ParamEncoder("loteRearchivo").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
				busquedaLoteRearchivo.setSortOrder(sortOrder);
				Integer nPagina = 1;		
				if(nPaginaStr!=null){
					nPagina = (Integer.parseInt(nPaginaStr));
				}
				busquedaLoteRearchivo.setNumeroPagina(nPagina);
			}
			
		List<LoteRearchivo> lotesRearchivo = loteRearchivoService.obtenerPor(
			obtenerClienteAspUser(),
			busquedaLoteRearchivo.getCodigoEmpresa(),
			busquedaLoteRearchivo.getCodigoSucursal(),
			busquedaLoteRearchivo.getCodigoCliente(),
			busquedaLoteRearchivo.getCodigoDesde(),
			busquedaLoteRearchivo.getCodigoHasta(),
			busquedaLoteRearchivo.getFechaDesde(),
			busquedaLoteRearchivo.getFechaHasta(),
			busquedaLoteRearchivo.getTipo(),
			busquedaLoteRearchivo.getCodigoClasificacionDocumental(),
			busquedaLoteRearchivo.getCodigoContenedor(),
			busquedaLoteRearchivo.getFieldOrder(), 
			busquedaLoteRearchivo.getSortOrder(), 
			busquedaLoteRearchivo.getNumeroPagina(), 
			busquedaLoteRearchivo.getTamañoPagina()
		);
		
		atributos.put("lotesRearchivo", lotesRearchivo);
		
		return "consultaLoteRearchivo";
	}
	
	@RequestMapping(value="/borrarFiltrosLoteRearchivo.html")
	public String borrarFiltrosLoteRearchivo(Map<String,Object> atributos,
			HttpServletRequest request) {
		return consultaLotesRearchivo(atributos, null, request);
	}
	@RequestMapping(value="/eliminarLoteRearchivo.html")
	public String eliminarLoteRearchivo(Map<String,Object> atributos,
			@RequestParam(value="id",required=true) Long id,
			HttpServletRequest request)
 {
		
		//Eliminamos los archivos digitales de rearchivo
		LoteRearchivo loteRearchivo = loteRearchivoService.obtenerPorId(id);
		if(loteRearchivo!=null && (loteRearchivo.getTipo().equals("Digital") ||loteRearchivo.getTipo().equals("Electronico"))){
			List<Rearchivo> rearchivos = rearchivoService.listarRearchivoPorLote(loteRearchivo, obtenerClienteAspUser());
			if(rearchivos!=null && rearchivos.size()>0){
				for(Rearchivo rearchivo:rearchivos){
					if(rearchivo.getPathArchivoDigital()!=null && !"".equals(rearchivo.getPathArchivoDigital())){
						try {
							//Borramos el archivo tiff
							File fileEliminar = new File(rearchivo.getPathArchivoDigital());
							fileEliminar.delete();
							
						} catch (Exception e) {
							
						}
					}
					//Borramos la carpeta de jpg
					if(rearchivo.getPathArchivoJPGDigital()!=null && !"".equals(rearchivo.getPathArchivoJPGDigital())){
						try {
							File carpetaEliminar = new File(rearchivo.getPathArchivoJPGDigital());
							//Recorro la carpeta para eliminar sus archivos
							if(carpetaEliminar.isDirectory()){
								File[] ficheros = carpetaEliminar.listFiles();
								for(int i=0;i<ficheros.length; i++)
									ficheros[i].delete();
							}
							carpetaEliminar.delete();
						} catch (Exception e) {
							
						}
					}
				}
			}
			
		}
		loteRearchivoService.eliminar(id);
		
		return consultaLotesRearchivo(atributos,null, request);
	}
	
	@RequestMapping(value="/iniciarFormularioLoteRearchivo.html")
	public String iniciarFormularioLotesRearchivo(Map<String,Object> atributos,
			HttpSession session,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) Long idLoteRearchivo) {
		
		session.removeAttribute("loteRearchivoSession");
		session.removeAttribute("rearchivos_lote");
		session.removeAttribute("referencias_loteRearchivo_digital");
		
		return precargaFormularioLotesRearchivo(atributos, session, accion, idLoteRearchivo);
	}
	
	@RequestMapping(value="/precargaFormularioLoteRearchivo.html")
	public String precargaFormularioLotesRearchivo(Map<String,Object> atributos,
			HttpSession session,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) Long idLoteRearchivo) {
		
		LoteRearchivo loteRearchivo = null;
		
		if(accion==null) accion="NUEVO"; //accion por defecto: nuevo
		if(accion.equals("NUEVO")){
			loteRearchivo=new LoteRearchivo();
			loteRearchivo.setClienteAsp(obtenerClienteAspUser());
			loteRearchivo.setEmpresa(empresaService.getByCodigo(obtenerCodigoEmpresaUser(), loteRearchivo.getClienteAsp()));
			loteRearchivo.setSucursal(sucursalService.getByCodigo(obtenerCodigoSucursalUser(), loteRearchivo.getClienteAsp()));
			loteRearchivo.setRearchivos(new HashSet<Rearchivo>());
			loteRearchivo.setFechaRegistro(new Date());
			loteRearchivo.setIndiceIndividual(true);
		} else {
			loteRearchivo=loteRearchivoService.obtenerPorId(idLoteRearchivo);
			loteRearchivo.setRearchivos(new HashSet<Rearchivo>(rearchivoService.listarRearchivoPorLote(loteRearchivo, obtenerClienteAspUser())));
			//Buscamos el primer rearchivo
			Rearchivo rearchivo = null;
			if(loteRearchivo.getRearchivos()!=null && loteRearchivo.getRearchivos().size()>0){
				for(Rearchivo re:loteRearchivo.getRearchivos()){
					if(re.getOrden()!=null && re.getOrden().intValue()==1){
						rearchivo=re;
						break;
					}
				}
			}
				
			atributos.put("rearchivo", rearchivo);
			//atributos.put("rearchivoSeleccionado", rearchivo);
			session.setAttribute("rearchivoSeleccionado", rearchivo);
		}
		
		//Seteo la accion actual
		atributos.put("accion", accion);
		atributos.put("loteRearchivo", loteRearchivo);
		session.setAttribute("loteRearchivoSession", loteRearchivo);
		ArrayList<Rearchivo> rearchivos = new ArrayList(loteRearchivo.getRearchivos());
		Collections.sort(rearchivos);
		session.setAttribute("rearchivos_lote", rearchivos);
		return "formularioLoteRearchivo";
	}
	@RequestMapping(value="/guardarActualizarLoteRearchivo.html",method=RequestMethod.POST)
	public String guardarLoteRearchivo(Map<String,Object> atributos, HttpSession session,
			@RequestParam(value="accion") String accion,
			@ModelAttribute("loteRearchivo") LoteRearchivo loteRearchivo,
			BindingResult result,
			@RequestParam(value="codigoEmpresa",required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal",required=false) String codigoSucursal,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente,
			@RequestParam(value="codigoRemito",required=false) Long codigoRemito,
			HttpServletRequest request) throws IOException{
		
		LoteRearchivo loteSession = (LoteRearchivo)session.getAttribute("loteRearchivoSession");
		loteRearchivo.setContenedor(loteSession.getContenedor());
		loteRearchivo.setCantidad(loteSession.getCantidad());
		loteRearchivo.setClasificacionDocumental(loteSession.getClasificacionDocumental());
		loteRearchivo.setIndiceIndividual(loteSession.getIndiceIndividual());
		if(loteSession.getTipo()!=null)
			loteRearchivo.setTipo(loteSession.getTipo());
		
		
		loteRearchivo.setClienteAsp(obtenerClienteAspUser());
		loteRearchivo.setEmpresa(empresaService.getByCodigo(codigoEmpresa, loteRearchivo.getClienteAsp()));
		loteRearchivo.setSucursal(sucursalService.getByCodigo(codigoSucursal, loteRearchivo.getClienteAsp()));
		loteRearchivo.setClienteEmp(clienteEmpService.getByCodigo(codigoCliente,codigoEmpresa,loteRearchivo.getClienteAsp()));
		if(codigoRemito!=null)
			loteRearchivo.setRemito(remitoService.obtenerPorId(codigoRemito));
		loteRearchivo.setRearchivos(new HashSet((List<Rearchivo>) session.getAttribute("rearchivos_lote")));
		
		//Actualizo el puntero del lote en el rearchivo
		if(loteRearchivo.getRearchivos()!=null)
			for(Rearchivo rea:loteRearchivo.getRearchivos())
				rea.setLoteRearchivo(loteRearchivo);
		if(!result.hasErrors()){
			loteValidator.validate(loteRearchivo, result);
		}
		
		if(!result.hasErrors()){
			LoteReferencia loteReferencia = null;
			//Si es nuevo creamos un lote de referencia y por cada detalle creamos una referencia que apunta a otra referencia
			if(accion.equals("NUEVO"))
			{
				loteReferencia = new LoteReferencia();
				setDataLoteReferencia(loteReferencia, loteRearchivo);
			}
			else if(loteSession.getTipo().equalsIgnoreCase("DIGITAL") || loteSession.getTipo().equalsIgnoreCase("ELECTRONICO"))
			{
				List<Referencia> referencias = (List<Referencia>)session.getAttribute("referencias_loteRearchivo_digital");
				if(referencias!=null && referencias.size()>=0)
					loteReferencia = referencias.get(0).getLoteReferencia();
				loteReferencia.setReferencias(referencias);
				loteReferencia.setModificadas(referencias);
			}
			loteRearchivoService.guardarActualizar(loteRearchivo, loteReferencia);
			
			if(loteRearchivo.getIndiceIndividual()){
				for(Referencia ref:loteReferencia.getReferencias()){
					if(ref.getCodigoUsuario()!=null){
						User user = userService.obtenerPorId(ref.getCodigoUsuario());
						final String para = user.getPersona().getMail();
						final String cuerpo = "Estimado "+user.getPersona().toString()+",<br><br> usted tiene una nueva tarea asignada para la etiqueta "+ref.getElemento().getCodigo()+"<br><br>Por favor, revise su lista de tareas.<br><br>NO RESPONDA ESTE EMAIL<br><br>Atte. La Administracion";
						new Thread(){
							public void run(){
								
									enviarMail(para, "BASA - AVISO: Tiene una nueva tarea asignada", cuerpo, "BASA S.A.");
								
							}
						}.start();
					}
				}
			}
			
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioLoteRearchivo.notificacion.loteRearchivoRegistrado", Arrays.asList(new String[]{loteRearchivo.getId().toString()}));
			avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);
			if(session.getAttribute("archivoPdf") != null){
				convertirPdf(session.getAttribute("archivoPdf").toString());
				session.setAttribute("archivoPdf",null);
			}
		}else{
			atributos.put("loteRearchivo", loteRearchivo);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			return precargaFormularioLotesRearchivo(atributos,session,accion, loteRearchivo.getId());
		}
		
		Process p = Runtime.getRuntime().exec("cmd.exe /c C:\\Archivos_Digitales\\vision.bat");

		
		//hacemos el redirect		
		return consultaLotesRearchivo(atributos, null, request);
	}
	
	public void convertirPdf (String archivoNombre) throws IOException{
		
		String archivoNombreTmp=archivoNombre.substring(0, archivoNombre.length()-2) +".pdf";
		
		PDDocument document = PDDocument.load(new File(archivoNombreTmp));
	    String extension = "png";
	    PDFRenderer pdfRenderer = new PDFRenderer(document);
	    for (int page = 0; page < document.getNumberOfPages(); ++page) {
	        BufferedImage bim = pdfRenderer.renderImageWithDPI(
	        page, 300, ImageType.GRAY);
	        ImageIOUtil.writeImage(
	          bim, String.format("%s%03d.%s",archivoNombreTmp, page + 1, extension), 300);
	    }
	    document.close();
	    
	}
	
	@RequestMapping(value="/seccionRearchivos.html")
	public String seccionRearchivos(Map<String,Object> atributos, HttpSession session){
		atributos.put("zoom", "70");
		return "formularioRearchivo";
	}
	
	@RequestMapping(value="/agregarRearchivo.html",method=RequestMethod.POST)
	public String agregarRearchivo(Map<String,Object> atributos, HttpSession session,
			@ModelAttribute(value="rearchivoFormulario") Rearchivo rearchivo,
			BindingResult result,
			@RequestParam(value="codigoCliente") String codigoCliente,
			@RequestParam(value="codigoClasificacionDocumental") Integer codigoClasificacionDocumental,
			@RequestParam(value="codigoContenedor",required=false) String codigoContenedor,
			@RequestParam(value="objetoSeleccionado") String objetoSeleccionado,
			@RequestParam(value="zoom",required=false) String zoom,
			@RequestParam(value="scrollY",required=false) String scrollY,
			@RequestParam(value="scrollX",required=false) String scrollX,
			@RequestParam(value="bloqueoNumero1",required=false) Boolean bloqueoNumero1,
			@RequestParam(value="indiceIndividual",required=false) Boolean indiceIndividual,
			@RequestParam(value="bloqueoNumero2",required=false) Boolean bloqueoNumero2,
			@RequestParam(value="bloqueoTexto1",required=false) Boolean bloqueoTexto1,
			@RequestParam(value="bloqueoTexto2",required=false) Boolean bloqueoTexto2,
			@RequestParam(value="bloqueoFecha1",required=false) Boolean bloqueoFecha1,
			@RequestParam(value="bloqueoFecha2",required=false) Boolean bloqueoFecha2,
			@RequestParam(value="accion",required=false) String accion){
		
		
		
		if(rearchivo.getNumero1Str()!=null){ 
			if(!rearchivo.getNumero1Str().trim().equalsIgnoreCase(""))
				rearchivo.setNumero1(Long.valueOf(rearchivo.getNumero1Str()));
			else
				rearchivo.setNumero1(null);
		}
		if(rearchivo.getNumero2Str()!=null){
			if(!rearchivo.getNumero2Str().trim().equalsIgnoreCase(""))
				rearchivo.setNumero2(Long.valueOf(rearchivo.getNumero2Str()));
			else
				rearchivo.setNumero2(null);
		}
		
		LoteRearchivo lote = (LoteRearchivo)session.getAttribute("loteRearchivoSession");
		rearchivo.setLoteRearchivo(lote);
		
		if(indiceIndividual!=null)
			rearchivo.getLoteRearchivo().setIndiceIndividual(indiceIndividual);
		procesarFormularioRearchivo(rearchivo, codigoCliente, codigoClasificacionDocumental,codigoContenedor);
		
		if(!result.hasErrors())
		{
			refValidator.validate(rearchivo, result);
		}
		atributos.put("bloqueoTexto1", bloqueoTexto1);
		atributos.put("bloqueoTexto2", bloqueoTexto2);
		atributos.put("bloqueoNumero1", bloqueoNumero1);
		atributos.put("bloqueoNumero2", bloqueoNumero2);
		atributos.put("bloqueoFecha1", bloqueoFecha1);
		atributos.put("bloqueoFecha2", bloqueoFecha2);
		
		if(!result.hasErrors())
		{
			List<Rearchivo> rearchivos = (List<Rearchivo>) session.getAttribute("rearchivos_lote");
			int i = 0;
			for(Rearchivo candidato:rearchivos){
				if(candidato.getObj_hash().equals(objetoSeleccionado) || rearchivo.getOrden().intValue() == candidato.getOrden().intValue()){
					setDataRearchivo(candidato, rearchivo);
					if(accion!=null && accion.equalsIgnoreCase("MODIFICACION") && (lote.getTipo().equalsIgnoreCase("DIGITAL") || lote.getTipo().equalsIgnoreCase("ELECTRONICO")))
					{
						List<Referencia> referencias = (List<Referencia>)session.getAttribute("referencias_loteRearchivo_digital");
						if(rearchivo.getCodigoClasifDoc()!=null && !rearchivo.getCodigoClasifDoc().equalsIgnoreCase(""))
						{
							candidato.setClasifDoc(clasificacionDocumentalService.getClasificacionByCodigo(Integer.valueOf(rearchivo.getCodigoClasifDoc()), codigoCliente, obtenerClienteAspUser(),"I"));
						}
						if(candidato.getReferencia()!=null)
						{
							Referencia ref = referenciaService.obtenerPorId(candidato.getReferencia().getId());
							candidato.setReferencia(ref);
							setDataReferencia(candidato.getReferencia(), rearchivo);

							if(referencias==null)
								referencias = referenciaService.traerReferenciasPorLote(ref.getLoteReferencia().getId());
							int indexRef = referencias.indexOf(ref);
							referencias.set(indexRef, candidato.getReferencia());
						}
						session.setAttribute("referencias_loteRearchivo_digital", referencias);
					}
					break;
				}
				i++;
			}
			i++;
			//Busco el siguiente para modificar
			if(i<rearchivos.size())
			{
				Rearchivo buscarRe = rearchivos.get(i);
				if(buscarRe!=null){
					Rearchivo modificar = new Rearchivo();
					setDataRearchivo(modificar, buscarRe);
					
					modificar.setLoteRearchivo(rearchivo.getLoteRearchivo());
					modificar.getLoteRearchivo().setIndiceIndividual(rearchivo.getLoteRearchivo().getIndiceIndividual());
								
					if(bloqueoNumero1!=null && bloqueoNumero1 
							&& (buscarRe.getNumero1()==null || "".equals(buscarRe.getNumero1())))
						modificar.setNumero1(rearchivo.getNumero1());
					if(bloqueoNumero2!=null && bloqueoNumero2
							&& (buscarRe.getNumero2()==null || "".equals(buscarRe.getNumero2())))
						modificar.setNumero2(rearchivo.getNumero2());
					if(bloqueoTexto1!=null && bloqueoTexto1 
							&& (buscarRe.getTexto1()==null || "".equals(buscarRe.getTexto1())))
						modificar.setTexto1(rearchivo.getTexto1());
					if(bloqueoTexto2!=null && bloqueoTexto2
							&& (buscarRe.getTexto2()==null || "".equals(buscarRe.getTexto2())))
						modificar.setTexto2(rearchivo.getTexto2());			
					if(bloqueoFecha1!=null && bloqueoFecha1
							&& buscarRe.getFecha1()==null)
						modificar.setFecha1(rearchivo.getFecha1());
					if(bloqueoFecha2!=null && bloqueoFecha2
							&& buscarRe.getFecha2()==null)
						modificar.setFecha2(rearchivo.getFecha2());
					
					atributos.put("id_hash", buscarRe.getObj_hash());
					atributos.put("rearchivo", modificar);
					//atributos.put("rearchivoSeleccionado", modificar);
					session.setAttribute("rearchivoSeleccionado", modificar);
				}
				
			}
			else
			{
				atributos.put("id_hash", rearchivo.getObj_hash());
				atributos.put("rearchivo", rearchivo);
				//atributos.put("rearchivoSeleccionado", rearchivo);
				session.setAttribute("rearchivoSeleccionado", rearchivo);
			}
			session.setAttribute("rearchivos_lote",rearchivos);
		}else{
			atributos.put("rearchivo", rearchivo);
			//atributos.put("rearchivoSeleccionado", rearchivo);
			session.setAttribute("rearchivoSeleccionado", rearchivo);
			atributos.put("id_hash", rearchivo.getObj_hash());
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
		}
		atributos.put("zoom", zoom);
		atributos.put("scrollY", scrollY);
		atributos.put("scrollX", scrollX);
		session.setAttribute("loteRearchivoSession",rearchivo.getLoteRearchivo());
		return "formularioRearchivo";
	}
	
	@RequestMapping(value="/eliminarRearchivo.html")
	public String eliminarRearchivo(Map<String,Object> atributos, HttpSession session,
			@RequestParam("obj_hash") String objectHash,
			@RequestParam(value="bloqueoNumero1",required=false) Boolean bloqueoNumero1,
			@RequestParam(value="bloqueoNumero2",required=false) Boolean bloqueoNumero2,
			@RequestParam(value="bloqueoTexto1",required=false) Boolean bloqueoTexto1,
			@RequestParam(value="bloqueoTexto2",required=false) Boolean bloqueoTexto2,
			@RequestParam(value="bloqueoFecha1",required=false) Boolean bloqueoFecha1,
			@RequestParam(value="bloqueoFecha2",required=false) Boolean bloqueoFecha2){
		List<Rearchivo> rearchivos = (List<Rearchivo>) session.getAttribute("rearchivos_lote");
		Rearchivo rearchivo=null;
		for(Rearchivo candidato:rearchivos){
			if(candidato.getObj_hash().equals(objectHash)){
				rearchivos.remove(candidato);
				rearchivo=candidato;
				break;
			}
		}
		atributos.put("rearchivo", rearchivo);//ponemos el eliminado por si le interesa cambiarle algo para guardar de nuevo
		//atributos.put("rearchivoSeleccionado", rearchivo);//ponemos el eliminado por si le interesa cambiarle algo para guardar de nuevo
		session.setAttribute("rearchivoSeleccionado", rearchivo);
		atributos.put("hacerFocusEn", "codigoClasificacionDocumental");
		atributos.put("bloqueoTexto1", bloqueoTexto1);
		atributos.put("bloqueoTexto2", bloqueoTexto2);
		atributos.put("bloqueoNumero1", bloqueoNumero1);
		atributos.put("bloqueoNumero2", bloqueoNumero2);
		atributos.put("bloqueoFecha1", bloqueoFecha1);
		atributos.put("bloqueoFecha2", bloqueoFecha2);
		

		return "formularioRearchivo";
	}
	
	@RequestMapping(value="/refrescarFormRearchivo.html",method=RequestMethod.POST)
	public String refrescarFormRearchivo(Map<String,Object> atributos, HttpSession session,
			@ModelAttribute(value="rearchivoFormulario") Rearchivo rearchivo,
			@RequestParam(value="codigoCliente")String codigoCliente,
			@RequestParam(value="codigoClasificacionDocumental")Integer codigoClasificacionDocumental,
			@RequestParam(value="codigoContenedor")String codigoContenedor,
			
			@RequestParam(value="indiceIndividual",required=false) Boolean indiceIndividual,
			@RequestParam(value="cantidad")Integer cantidad,
			
			@RequestParam(value="bloqueoNumero1",required=false) Boolean bloqueoNumero1,
			@RequestParam(value="bloqueoNumero2",required=false) Boolean bloqueoNumero2,
			@RequestParam(value="bloqueoTexto1",required=false) Boolean bloqueoTexto1,
			@RequestParam(value="bloqueoTexto2",required=false) Boolean bloqueoTexto2,
			@RequestParam(value="bloqueoFecha1",required=false) Boolean bloqueoFecha1,
			@RequestParam(value="bloqueoFecha2",required=false) Boolean bloqueoFecha2){
		
		rearchivo.setLoteRearchivo((LoteRearchivo)session.getAttribute("loteRearchivoSession"));
		if(indiceIndividual!=null)
			rearchivo.getLoteRearchivo().setIndiceIndividual(indiceIndividual);
		procesarFormularioRearchivo(rearchivo, codigoCliente, codigoClasificacionDocumental, codigoContenedor);
		if(rearchivo.getLoteRearchivo().getIndiceIndividual() && rearchivo.getLoteRearchivo().getClasificacionDocumental()!=null && !rearchivo.getLoteRearchivo().getClasificacionDocumental().getIndiceIndividual())
			rearchivo.getLoteRearchivo().setIndiceIndividual(false);
		if(!rearchivo.getLoteRearchivo().getIndiceIndividual() && rearchivo.getLoteRearchivo().getClasificacionDocumental()!=null && !rearchivo.getLoteRearchivo().getClasificacionDocumental().getIndiceGrupal())
			rearchivo.getLoteRearchivo().setIndiceIndividual(true);
		
		if(cantidad!=null){
			rearchivo.getLoteRearchivo().setCantidad(cantidad);
			List<Rearchivo> rearchivos = new ArrayList<Rearchivo>();
			for(int i = 1;i<=cantidad.intValue();i++ ){
				Rearchivo rearNuevo = new Rearchivo();
				rearNuevo.setLoteRearchivo(rearchivo.getLoteRearchivo());
				rearNuevo.setEstado("Pendiente");
				rearNuevo.setOrden(new Integer(i));
				rearchivos.add(rearNuevo);
			}
			session.setAttribute("rearchivos_lote",rearchivos);
			if(rearchivos.size()>0){
				//atributos.put("rearchivoSeleccionado", rearchivos.get(0));
				session.setAttribute("rearchivoSeleccionado", rearchivos.get(0));
				atributos.put("id_hash",  rearchivos.get(0).getObj_hash());
				atributos.put("seleccionarItemTabla", rearchivos.get(0).getOrden());
			}
		}

		atributos.put("rearchivo", rearchivo);
		atributos.put("bloqueoTexto1", bloqueoTexto1);
		atributos.put("bloqueoTexto2", bloqueoTexto2);
		atributos.put("bloqueoNumero1", bloqueoNumero1);
		atributos.put("bloqueoNumero2", bloqueoNumero2);
		atributos.put("bloqueoFecha1", bloqueoFecha1);
		atributos.put("bloqueoFecha2", bloqueoFecha2);
		session.setAttribute("loteRearchivoSession", rearchivo.getLoteRearchivo());
		return "formularioRearchivo";
	}
	
	@RequestMapping(
			value="/importarArchivoLoteRearchivo.html",
			method = RequestMethod.POST
		)
	public String importarLecturaDetalle
	(HttpSession session,
			@RequestParam(value="codigoCliente2",required=false)String codigoCliente,
			@RequestParam(value="codigoEmpresa2",required=false)String codigoEmpresa,
			@RequestParam(value="codigoSucursal2",required=false)String codigoSucursal,
			@RequestParam(value="codigoClasificacionDocumental2",required=false)Integer codigoClasificacionDocumental,
			@RequestParam(value="codigoContenedor2",required=false)String codigoContenedor,
			@RequestParam(value="indiceIndividual2",required=false) Boolean indiceIndividual,
			@RequestParam(value="tipo2",required=false)String tipo,
			@RequestParam(value="tipoImg",required=false)String tipoImg,
			@RequestParam(value="file",required=false) MultipartFile file,
			Map<String,Object> atributos,
			HttpServletRequest request){
		
		Rearchivo rearchivo = new Rearchivo();
		
		rearchivo.setLoteRearchivo((LoteRearchivo)session.getAttribute("loteRearchivoSession"));
		
		rearchivo.getLoteRearchivo().getEmpresa().setCodigo(codigoEmpresa);
		rearchivo.getLoteRearchivo().getSucursal().setCodigo(codigoSucursal);
		
		if(tipo!=null)
			rearchivo.getLoteRearchivo().setTipo(tipo);
		if(indiceIndividual!=null)
			rearchivo.getLoteRearchivo().setIndiceIndividual(indiceIndividual);
		procesarFormularioRearchivo(rearchivo, codigoCliente, codigoClasificacionDocumental, codigoContenedor);
		if(rearchivo.getLoteRearchivo().getIndiceIndividual() && rearchivo.getLoteRearchivo().getClasificacionDocumental()!=null && !rearchivo.getLoteRearchivo().getClasificacionDocumental().getIndiceIndividual())
			rearchivo.getLoteRearchivo().setIndiceIndividual(false);
		if(!rearchivo.getLoteRearchivo().getIndiceIndividual() && rearchivo.getLoteRearchivo().getClasificacionDocumental()!=null && !rearchivo.getLoteRearchivo().getClasificacionDocumental().getIndiceGrupal())
			rearchivo.getLoteRearchivo().setIndiceIndividual(true);
		
		if(codigoCliente!=null && !"".equals(codigoCliente)){
			if(rearchivo.getLoteRearchivo()!=null && rearchivo.getLoteRearchivo().getClienteEmp()==null)//Lo seteamos la primera vez
				rearchivo.getLoteRearchivo().setClienteEmp(clienteEmpService.getByCodigo(codigoCliente,rearchivo.getLoteRearchivo().getEmpresa().getCodigo(), obtenerClienteAspUser()));
		}
		try {
			String dir = com.security.constants.Constants.URL_ARCHIVOS_DIGITALES;
			String ruta = "";
			if(rearchivo.getLoteRearchivo().getClienteAsp()!=null && rearchivo.getLoteRearchivo().getEmpresa()!=null
					&& rearchivo.getLoteRearchivo().getSucursal()!=null && rearchivo.getLoteRearchivo().getClienteEmp()!=null 
					&& rearchivo.getLoteRearchivo().getClasificacionDocumental()!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String fecha = sdf.format(new Date());
				ruta = rearchivo.getLoteRearchivo().getClienteAsp().getNombreAbreviado()+"//"
					+ rearchivo.getLoteRearchivo().getEmpresa().getCodigo()+"//"+rearchivo.getLoteRearchivo().getSucursal().getCodigo()+"//"
					+ rearchivo.getLoteRearchivo().getClienteEmp().getCodigo()+"//"+rearchivo.getLoteRearchivo().getClasificacionDocumental().getCodigo()+"//"
					+ fecha + "//";
			}
			String extension=file.getOriginalFilename().split("\\.")[1];
			
			String tipoImagen=null;
			
			if(tipoImg!=null ) 
				tipoImagen = tipoImg;
			
			if(extension!=null && extension.equalsIgnoreCase("jpeg"))
				tipoImagen = "JPEG";
						
			String dirProcesados = dir+tipoImagen+"//"+ruta;
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
			new File(dirProcesados).mkdirs();
			ArrayList<String> listaNombresArchivos = new ArrayList<String>();
			ArrayList<String> listaPathsArchivos = new ArrayList<String>();
			
			if(extension.equalsIgnoreCase("zip")){
				dir+="ZIP//"+ruta;
				new File(dir).mkdirs();
				File archivo = new File(dir, "Digital " + sd.format(new Date())
				+ " " + file.getOriginalFilename());
				file.transferTo(archivo);
				try {
					ZipUtil.unZip(archivo.getPath(),dirProcesados, listaNombresArchivos, listaPathsArchivos);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				if(listaNombresArchivos!=null && listaPathsArchivos!=null)
				{
					String pFile = dirProcesados + file.getOriginalFilename().split("\\.")[0];
					listaNombresArchivos.add(file.getOriginalFilename());
					listaPathsArchivos.add(dirProcesados+file.getOriginalFilename());
					new File(pFile).mkdirs();
					File copia = new File(dirProcesados+file.getOriginalFilename());
					file.transferTo(copia);
				}
			}
			
			int cantidad = listaNombresArchivos.size();
			rearchivo.getLoteRearchivo().setCantidad(cantidad);
			
			if(cantidad>0){
				rearchivo.getLoteRearchivo().setCantidad(cantidad);
				List<Rearchivo> rearchivos = new ArrayList<Rearchivo>();
				for(int i = 0;i<cantidad;i++ ){
					String nombre = listaNombresArchivos.get(i);
					
				
					
					//if(nombre.toLowerCase().endsWith(extension)){
						Rearchivo rearNuevo = new Rearchivo();
						rearNuevo.setLoteRearchivo(rearchivo.getLoteRearchivo());
						rearNuevo.setEstado("Pendiente");
						rearNuevo.setOrden(new Integer(i + 1));
						rearNuevo.setNombreArchivoDigital(listaNombresArchivos.get(i));
						rearNuevo.setPathArchivoDigital(listaPathsArchivos.get(i));
						
						
						if(nombre.toLowerCase().endsWith(".pdf")){
							
							
							String rutaReplaced = ruta.replace("/", "\\");
							rearNuevo.setPathArchivoDigital(rutaReplaced);
							
							/* set output folder path */
					        String outputFolderName = null;
					        String[] temp = null;
					        temp = listaPathsArchivos.get(i).split("\\.");
					        outputFolderName = temp[0] + "//";
					        File fileObjForOPFolder = new File(outputFolderName);
					        
					        //fileObjForOPFolder.mkdirs();	
					        
					        
					        session.setAttribute("archivoPdf", outputFolderName);
					       
					        
					        // WITH PDFBOX -------------------------------------------------------------------
							PDDocument document = null; 
							document = PDDocument.load(new File(listaPathsArchivos.get(i))); 
							//List pages = document.getDocumentCatalog().getAllPages();
							//rearNuevo.setCantidadImagenes(pages.size());
							rearNuevo.setCantidadImagenes(document.getDocumentCatalog().getPages().getCount());
							rearNuevo.setPathArchivoJPGDigital(outputFolderName);
						    rearchivos.add(rearNuevo);
						    document.close();
						    System.gc();
							
						}
						if(nombre.toLowerCase().endsWith(".tif")||nombre.toLowerCase().endsWith(".tiff"))
						{
							//Cuento la cantidad de imagenes
							File fileTIFF = new File(listaPathsArchivos.get(i));
							SeekableStream s = new FileSeekableStream(fileTIFF);
		
					        TIFFDecodeParam param = null;
		
					        ImageDecoder dec = ImageCodec.createImageDecoder("tiff", s, param);
							
					        rearNuevo.setCantidadImagenes(dec.getNumPages());
					       
					        /*
					         * create object of RenderedIamge to produce
					         * image data in form of Rasters
					         */
					        RenderedImage renderedImage[], page;
					        
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
	
					        /* set output folder path */
					        String outputFolderName = null;
					        String[] temp = null;
					        temp = listaPathsArchivos.get(i).split("\\.");
					        
					        outputFolderName = temp[0] + "//";
					        /* 
					         * create file object of output folder
					         * and make a directory 
					         */
					        File fileObjForOPFolder = new File(outputFolderName);
					        fileObjForOPFolder.mkdirs();
	
					        /* 
					         * extract no. of image available inside 
					         * the input tiff file 
					         */
					        for (int j = 0; j < count; j++) {
					            page = dec.decodeAsRenderedImage(j);
					            File fileObj = new File(outputFolderName 
					                   + (j + 1));
					            /* 
					             * ParameterBlock create a generic 
					             * interface for parameter passing 
					             */
					            ParameterBlock parameterBlock = new ParameterBlock();
					            /* add source of page */
					            parameterBlock.addSource(page);
					            /* add o/p file path */
					            parameterBlock.add(fileObj.toString());
					            /* add o/p file type */
					            parameterBlock.add("jpeg");
					            /* create output image using JAI filestore */
					            RenderedOp renderedOp = JAI.create("filestore",
					                    parameterBlock);
					            renderedOp.dispose();
					           
					        }
					        rearNuevo.setPathArchivoJPGDigital(outputFolderName);
					        rearchivos.add(rearNuevo);
					        System.gc();
							
						}
					   if(nombre.toLowerCase().endsWith(".jpg") || nombre.toLowerCase().endsWith(".jpeg"))
						{
						    String outputFolderName = null;
					        String[] temp = null;
					        temp = listaPathsArchivos.get(i).split("\\.");
					        outputFolderName = temp[0] + "//";
				        	
					        
							rearNuevo.setCantidadImagenes(1);
							rearNuevo.setPathArchivoJPGDigital(outputFolderName);
						    rearchivos.add(rearNuevo);
						    System.gc();
					           
					        }
					
				}
				session.setAttribute("rearchivos_lote",rearchivos);
				if(rearchivos.size()>0){
					//atributos.put("rearchivoSeleccionado", rearchivos.get(0));
					session.setAttribute("rearchivoSeleccionado", rearchivos.get(0));
					atributos.put("id_hash",  rearchivos.get(0).getObj_hash());
					atributos.put("seleccionarItemTabla", rearchivos.get(0).getOrden());
				}
			}
	
			atributos.put("rearchivo", rearchivo);
			
			session.setAttribute("loteRearchivoSession", rearchivo.getLoteRearchivo());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
		return "formularioRearchivo";
	}
	
	@RequestMapping(
			value="/downloadFileLoteRearchivo.html",
			method = RequestMethod.GET
		)
	public String downloadFile(@RequestParam(value="fileName",required=false) String fileName,
			HttpSession session, Map<String,Object> atributos, HttpServletResponse response	){

		if(fileName==null || "".equals(fileName))
			return "";
		
		int idx = fileName.lastIndexOf("/"); 
		String fileNameEnviar = idx >= 0 ? fileName.substring(idx + 1) : fileName; 
		ServletOutputStream op = null;
		try {
			String urlFile = fileName;
			InputStream in = new FileInputStream(urlFile);
			byte[] data = new byte[in.available()];
			in.read(data);
			
			response.setContentType("application/octet-stream;");
			response.setHeader("Content-Disposition","attachment;filename=\"" + fileNameEnviar +"\";");
			response.setContentLength(data.length);
			op = response.getOutputStream();

			op.write(data);
			op.flush();
			op.close();

		} catch (IOException e) {
			
			try {
				op.close();
			} catch (Exception e1) {
				return "";
			}
			
		} catch (Exception e) {
			return "";
		};
		return "";
	}
	
	@RequestMapping(
			value="/viewFileJPGLoteRearchivo.html",
			method = RequestMethod.GET
		)
	public String viewFileJPGLoteRearchivo(@RequestParam(value="fileName",required=false) String fileName,
			HttpSession session, Map<String,Object> atributos, HttpServletResponse response	){
		if(fileName==null || "".equals(fileName))
			return "formularioRearchivoDigital"; //JSP en blanco
		 
		ServletOutputStream op = null;
		try {
			String urlFile = fileName;
			
			if(urlFile.contains("JPG")){
				urlFile=fileName.substring(0, fileName.length()-3)+".jpg";
			}
			InputStream in = new FileInputStream(urlFile);
			byte[] data = new byte[in.available()];
			in.read(data);
			
			response.setContentType("image/jpeg;");
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
	
	private void procesarFormularioRearchivo(Rearchivo rearchivo,String codigoCliente, Integer codigoClasificacionDocumental,String codigoContenedor){
		if(codigoClasificacionDocumental!=null && !codigoClasificacionDocumental.equals("")){
			if(rearchivo.getLoteRearchivo()!=null && rearchivo.getLoteRearchivo().getClasificacionDocumental()==null)//Lo seteamos la primera vez
				rearchivo.getLoteRearchivo().setClasificacionDocumental(clasificacionDocumentalService.getClasificacionByCodigo(codigoClasificacionDocumental, codigoCliente, obtenerClienteAspUser(),"I"));
		}
		if(rearchivo.getLoteRearchivo()!=null && rearchivo.getLoteRearchivo().getContenedor()==null)//Lo seteamos la primera vez
			if(codigoContenedor!=null && !codigoContenedor.trim().equals(""))
				rearchivo.getLoteRearchivo().setContenedor(elementoService.getContenedorByCodigo(codigoContenedor,codigoCliente,null, obtenerClienteAspUser(),""));
			else
				if(rearchivo.getLoteRearchivo().getTipo().equalsIgnoreCase("Electronico"))
					rearchivo.getLoteRearchivo().setContenedor(elementoService.getProxContenedorDisponByTipoContenedor(null, codigoCliente, null, obtenerClienteAspUser(),Constants.CODIGO_TIPO_ELEMENTO_ELECTRONICO,false));
		
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
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
	
	private void setDataRearchivo(Rearchivo candidato, Rearchivo rearchivo){
		candidato.setDescripcion(rearchivo.getDescripcion());
		candidato.setFecha1(rearchivo.getFecha1());
		if(rearchivo.getFecha2()!=null)
			candidato.setFecha2(rearchivo.getFecha2());
		else
			candidato.setFecha2(rearchivo.getFecha1());
		candidato.setNumero1(rearchivo.getNumero1());
		if(rearchivo.getNumero2()!=null)
			candidato.setNumero2(rearchivo.getNumero2());
		else
			candidato.setNumero2(rearchivo.getNumero1());
		candidato.setOrden(rearchivo.getOrden());
		candidato.setTexto1(rearchivo.getTexto1());
		if(rearchivo.getTexto2()!=null)
			candidato.setTexto2(rearchivo.getTexto2());
		else
			candidato.setTexto2(rearchivo.getTexto1());
		candidato.setEstado(rearchivo.getEstado());
		candidato.setNombreArchivoDigital(rearchivo.getNombreArchivoDigital());
		candidato.setPathArchivoDigital(rearchivo.getPathArchivoDigital());
		candidato.setCantidadImagenes(rearchivo.getCantidadImagenes());
		candidato.setPathArchivoJPGDigital(rearchivo.getPathArchivoJPGDigital());
		candidato.setDescripcionTarea(rearchivo.getDescripcionTarea());
		candidato.setCodigoUsuario(rearchivo.getCodigoUsuario());
	}
	
	private void setDataReferencia(Referencia candidato, Rearchivo rearchivo){
		candidato.setDescripcion(rearchivo.getDescripcion());
		candidato.setFecha1(rearchivo.getFecha1());
		if(rearchivo.getFecha2()!=null)
			candidato.setFecha2(rearchivo.getFecha2());
		else
			candidato.setFecha2(rearchivo.getFecha1());
		candidato.setNumero1(rearchivo.getNumero1());
		if(rearchivo.getNumero2()!=null)
			candidato.setNumero2(rearchivo.getNumero2());
		else
			candidato.setNumero2(rearchivo.getNumero1());
		candidato.setTexto1(rearchivo.getTexto1());
		if(rearchivo.getTexto2()!=null)
			candidato.setTexto2(rearchivo.getTexto2());
		else
			candidato.setTexto2(rearchivo.getTexto1());
		candidato.setDescripcionTarea(rearchivo.getDescripcionTarea());
		candidato.setCodigoUsuario(rearchivo.getCodigoUsuario());
		candidato.setClasificacionDocumental(rearchivo.getClasifDoc());
	}
	
	private void setDataLoteReferencia(LoteReferencia loteReferencia,LoteRearchivo loteRearchivo){
		loteReferencia.setClienteAsp(loteRearchivo.getClienteAsp());
		loteReferencia.setClienteEmp(loteRearchivo.getClienteEmp());
		loteReferencia.setEmpresa(loteRearchivo.getEmpresa());
		loteReferencia.setFechaRegistro(loteRearchivo.getFechaRegistro());
		loteReferencia.setSucursal(loteRearchivo.getSucursal());
		loteReferencia.setReferencias(crearReferencias(loteReferencia, loteRearchivo.getRearchivos()));
		//Si no tiene referencias lo pongo en null para que no se guarde
		if(loteReferencia.getReferencias()== null || loteReferencia.getReferencias().size()==0)
			loteReferencia = null;
	}
	
	private List<Referencia> crearReferencias(LoteReferencia loteReferencia, Set<Rearchivo> rearchivos ){
		List<Referencia> referencias = new ArrayList<Referencia>();
		if(rearchivos!=null && rearchivos.size()>0){
			for(Rearchivo rearchivo:rearchivos){
				Referencia referenciaBuscar = referenciaService.obtenerParaRearchivo(rearchivo.getLoteRearchivo().getClienteEmp(), rearchivo.getLoteRearchivo().getClasificacionDocumental(), rearchivo.getNumero1());
				//Si encuentro referencia creo una nueva referencia para el detalle
				//if(referenciaBuscar!=null){
					Referencia referencia = new Referencia();
					referencia.setDescripcion(rearchivo.getDescripcion());
					referencia.setDescripcionRearchivo("Rearchivo");
					referencia.setClasificacionDocumental(rearchivo.getLoteRearchivo().getClasificacionDocumental());
					referencia.setElemento(rearchivo.getLoteRearchivo().getContenedor());
					referencia.setFecha1(rearchivo.getFecha1());
					referencia.setFecha2(rearchivo.getFecha2());
					//Siempre son individuales
					referencia.setIndiceIndividual(new Boolean(true));
					referencia.setLoteReferencia(loteReferencia);
					referencia.setNumero1(rearchivo.getNumero1());
					referencia.setNumero2(rearchivo.getNumero2());
					if(referenciaBuscar!=null)
						referencia.setReferenciaRearchivo(referenciaBuscar);
					referencia.setTexto1(rearchivo.getTexto1());
					referencia.setTexto2(rearchivo.getTexto2());
					referencia.setOrdenRearchivo(rearchivo.getOrden());
					referencia.setPathArchivoDigital(rearchivo.getPathArchivoDigital());
					
					if(rearchivo.getPathArchivoJPGDigital()!=null && !rearchivo.getPathArchivoJPGDigital().equals("")){
						String carpeta = rearchivo.getPathArchivoJPGDigital().substring(0, rearchivo.getPathArchivoJPGDigital().lastIndexOf("/")-1);
						carpeta = carpeta.substring(0, carpeta.lastIndexOf("/")+1);
						referencia.setPathLegajo(carpeta+rearchivo.getNombreArchivoDigital());
					}
					
					referencia.setDescripcionTarea(rearchivo.getDescripcionTarea());
					referencia.setCodigoUsuario(rearchivo.getCodigoUsuario());
					if(rearchivo.getCodigoUsuario()!=null)
						referencia.setEstadoTarea("En Proceso");
					
					referencias.add(referencia);
					
					rearchivo.setReferencia(referencia);
				//}
			}
		}
		return referencias;
	}
	
	//TODO: Informar cuando se excede el tamaoo maximo de filas en el excel
			private void enviarMail(String para,String asunto, String cuerpo,String sistema){
				try {
					mailManager.enviar(para, asunto, cuerpo, sistema);   
				} catch (MessagingException e) {
					logger.error("error al enviar mail",e);
				} catch (IllegalStateException e){
					logger.error("error al enviar mail",e);
				}
			}
}
