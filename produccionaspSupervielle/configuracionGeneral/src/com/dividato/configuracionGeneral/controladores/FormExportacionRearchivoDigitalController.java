/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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

import com.dividato.configuracionGeneral.objectForms.LoteExportacionRearchivoBusquedaForm;
import com.dividato.configuracionGeneral.utils.ExportadorExcelRearchivosDigitales;
import com.dividato.configuracionGeneral.utils.LabelLink;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteExportacionRearchivoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteRearchivoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RearchivoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.LoteExportacionRearchivo;
import com.security.modelo.configuraciongeneral.LoteRearchivo;
import com.security.modelo.configuraciongeneral.Rearchivo;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que permite la exportación de referencias a excel
 *
 * @author Federico Mz
 */

@Controller
@RequestMapping(
		value={
				"/consultaExportacionRearchivo.html",
				"/eliminarLoteExportacionRearchivo.html",
				
				"/precargaLoteExportacionRearchivo.html",
				"/guardarActualizarLoteExportacionRearchivo.html",
				
				"/popUpSeleccionLoteRearchivoDigital.html",
				
				"/exportarLoteExportacionRearchivo.html",
				"/downloadZipRearchivoDigital.html"
		}
)
public class FormExportacionRearchivoDigitalController {
	private static Logger logger=Logger.getLogger(FormExportacionRearchivoDigitalController.class);
	
	private LoteExportacionRearchivoService loteExportacionRearchivoService;
	private LoteRearchivoService loteRearchivoService;
	
	private ClasificacionDocumentalService clasificacionDocumentalService;
	private ClienteEmpService clienteEmpService;
	private SucursalService sucursalService;
	private EmpresaService empresaService;
	private RearchivoService rearchivoService;
	
	
	@Autowired
	public void setServices(LoteExportacionRearchivoService loteExportacionRearchivoService,
			LoteRearchivoService loteRearchivoService,
			ClasificacionDocumentalService clasificacionDocumentalService,
			ClienteEmpService clienteEmpService,
			SucursalService sucursalService,
			EmpresaService empresaService,
			RearchivoService rearchivoService
			){
		this.clasificacionDocumentalService=clasificacionDocumentalService;
		this.loteRearchivoService = loteRearchivoService;
		this.loteExportacionRearchivoService = loteExportacionRearchivoService;
		this.clienteEmpService = clienteEmpService;
		this.sucursalService = sucursalService;
		this.empresaService = empresaService;
		this.rearchivoService = rearchivoService;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		if(binder.getTarget() instanceof LoteExportacionRearchivoBusquedaForm){
			binder.registerCustomEditor(Date.class, "fechaDesde",new CustomDateEditor(formatoFechaFormularios, true));
			binder.registerCustomEditor(Date.class, "fechaHasta",new CustomDateEditor(formatoFechaFormularios, true));
		}else if(binder.getTarget() instanceof LoteExportacionRearchivo){
			binder.registerCustomEditor(Date.class, "fechaRegistro",new CustomDateEditor(formatoFechaFormularios, true));
		}
	}
	
	@RequestMapping(value="/consultaExportacionRearchivo.html")
	public String consultaExportacionRearchivo(HttpSession session, Map<String,Object> atributos,
			@ModelAttribute("busquedaExportacionLoteRearchivoFormulario") LoteExportacionRearchivoBusquedaForm busquedaExportacionLoteRearchivoFormulario) {
		
		if(busquedaExportacionLoteRearchivoFormulario==null)
			busquedaExportacionLoteRearchivoFormulario=new LoteExportacionRearchivoBusquedaForm();
		if(busquedaExportacionLoteRearchivoFormulario.getIdClienteAsp()==null){
			busquedaExportacionLoteRearchivoFormulario.setIdClienteAsp(obtenerClienteAspUser().getId());
			busquedaExportacionLoteRearchivoFormulario.setCodigoEmpresa(obtenerCodigoEmpresaUser());
			busquedaExportacionLoteRearchivoFormulario.setCodigoSucursal(obtenerCodigoSucursalUser());
		}
		
		atributos.put("lotesExportacion", loteExportacionRearchivoService.obtenerPor(
				obtenerClienteAspUser(),
				busquedaExportacionLoteRearchivoFormulario.getCodigoEmpresa(),
				busquedaExportacionLoteRearchivoFormulario.getCodigoSucursal(),
				busquedaExportacionLoteRearchivoFormulario.getCodigoCliente(),
				busquedaExportacionLoteRearchivoFormulario.getCodigoClasificacionDocumental(),
				busquedaExportacionLoteRearchivoFormulario.getCodigoDesde(),
				busquedaExportacionLoteRearchivoFormulario.getCodigoHasta(),
				busquedaExportacionLoteRearchivoFormulario.getFechaDesde(),
				busquedaExportacionLoteRearchivoFormulario.getFechaHasta()
		));
		
		return "consultaLotesExportacionRearchivos";
	}
	
	@RequestMapping(value="/precargaLoteExportacionRearchivo.html")
	public String precargaLoteExportacionRearchivo(Map<String,Object> atributos,
			HttpSession session,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) Long idLoteExportacionRearchivo) {
		
		LoteExportacionRearchivo loteExportacionRearchivo = null;
		
		if(accion==null) accion="NUEVO"; //acción por defecto: nueva carga individual
		if(accion.equals("NUEVO")){
			loteExportacionRearchivo=new LoteExportacionRearchivo();
			loteExportacionRearchivo.setClienteAsp(obtenerClienteAspUser());
			loteExportacionRearchivo.setEmpresa(empresaService.getByCodigo(obtenerCodigoEmpresaUser(), loteExportacionRearchivo.getClienteAsp()));
			loteExportacionRearchivo.setSucursal(sucursalService.getByCodigo(obtenerCodigoSucursalUser(), loteExportacionRearchivo.getClienteAsp()));
			loteExportacionRearchivo.setLotesRearchivo(new HashSet<LoteRearchivo>());
			loteExportacionRearchivo.setFechaRegistro(new Date());
			loteExportacionRearchivo.setEstado("NO_GENERADO");
		} else {
			loteExportacionRearchivo=loteExportacionRearchivoService.obtenerPorIdConLoteReferencias(idLoteExportacionRearchivo);
		}
		
		//Seteo la accion actual
		atributos.put("accion", accion);
		atributos.put("loteExportacionRearchivo", loteExportacionRearchivo);
		atributos.put("bloquearCampos", !loteExportacionRearchivo.getLotesRearchivo().isEmpty());
		session.setAttribute("lotesRearchivo", new ArrayList(loteExportacionRearchivo.getLotesRearchivo()));
		return "formularioLoteExportacionRearchivo";
	}

	/**
	 * Este método se encarga de guardar un lote de exportación de rearchivo,
	 * ya sea nuevo o modificación.
	 * Pero también es el encargado de realizar las acciones de Agregar Lote de Rearchivo al Lote de Exportación y 
	 * Eliminar Lote de Rearchivo al Lote de Exportación.
	 */
	@RequestMapping(value="/guardarActualizarLoteExportacionRearchivo.html")
	public String guardarActualizarLoteExportacionRearchivo(Map<String,Object> atributos, HttpSession session,
			@RequestParam(value="accion",required=false) String accion,
			@ModelAttribute("loteExportacionRearchivo") LoteExportacionRearchivo loteExportacionRearchivo,
			BindingResult result,
			@RequestParam(value="codigoEmpresa",required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal",required=false) String codigoSucursal,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente,
			@RequestParam(value="codigoClasificacionDocumental",required=false) Integer codigoClasificacionDocumental,
			@RequestParam(value="codigoLoteRearchivoSeleccionado",required=false) Long codigoLoteRearchivoSeleccionado,
			@RequestParam(value="codigoLoteRearchivoEliminar",required=false) Long codigoLoteRearchivoEliminar
			) {
		
		atributos.put("accion", accion);
		
		loteExportacionRearchivo.setClienteAsp(obtenerClienteAspUser());
		loteExportacionRearchivo.setEmpresa(empresaService.getByCodigo(obtenerCodigoEmpresaUser(), loteExportacionRearchivo.getClienteAsp()));
		loteExportacionRearchivo.setSucursal(sucursalService.getByCodigo(obtenerCodigoSucursalUser(), loteExportacionRearchivo.getClienteAsp()));
		loteExportacionRearchivo.setClienteEmp(clienteEmpService.getByCodigo(codigoCliente,codigoEmpresa,loteExportacionRearchivo.getClienteAsp()));
		loteExportacionRearchivo.setClasificacionDocumental(clasificacionDocumentalService.getByCodigo(codigoClasificacionDocumental,codigoCliente,loteExportacionRearchivo.getClienteAsp()));
		if(loteExportacionRearchivo.getSizeMaxArchivo() == null || loteExportacionRearchivo.getSizeMaxArchivo() < 0)
			loteExportacionRearchivo.setSizeMaxArchivo(0);
		
		//AGREGAR/ELIMINAR UN LOTE DE REARCHIVO?
		if(codigoLoteRearchivoSeleccionado != null || codigoLoteRearchivoEliminar != null){
			List<LoteRearchivo> lotesRearchivo = (List<LoteRearchivo>) session.getAttribute("lotesRearchivo");
			if(codigoLoteRearchivoSeleccionado != null){
				lotesRearchivo.add(loteRearchivoService.obtenerPorId(codigoLoteRearchivoSeleccionado));
			} else {
				Iterator<LoteRearchivo> it = lotesRearchivo.iterator();
				while(it.hasNext()){
					if(it.next().getId().equals(codigoLoteRearchivoEliminar)){
						it.remove();
						break;
					}
				}
			}
			atributos.put("bloquearCampos", !lotesRearchivo.isEmpty());
			return "formularioLoteExportacionRearchivo";
		}
		
		//GUARDAR O ACTUALIZAR
		HashSet<LoteRearchivo> lotesRearchivo = new HashSet<LoteRearchivo>();
		lotesRearchivo.addAll((List<LoteRearchivo>) session.getAttribute("lotesRearchivo"));
		loteExportacionRearchivo.setLotesRearchivo(lotesRearchivo);
		
		//Validación...
		if(lotesRearchivo.isEmpty()){
			//Genero las notificaciones 
			result.reject("formularioLoteExportacionRearchivo.notificacion.lotesRearchivoVacio");
			atributos.put("errores", true);
			atributos.put("result",result);
			atributos.put("bloquearCampos", false);
			return "formularioLoteExportacionRearchivo";
		}
		
		//REGISTRAMOS!
		if(loteExportacionRearchivo.getId()==null || loteExportacionRearchivo.getId().equals(0L))
			loteExportacionRearchivoService.guardar(loteExportacionRearchivo);
		else {
			if(loteExportacionRearchivo.getEstado().equals("GENERADO")) loteExportacionRearchivo.setEstado("GENERADO_INCONSISTENTE");
			loteExportacionRearchivoService.actualizar(loteExportacionRearchivo);
		}
		
		//Genero las notificaciones 
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioLoteExportacionRearchivo.notificacion.loteReferenciaRegistrado", Arrays.asList(new String[]{loteExportacionRearchivo.getId().toString()}));
		avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
		atributos.put("hayAvisos", true);
		atributos.put("avisos", avisos);
		//Redirigimos a la consulta
		return consultaExportacionRearchivo(session, atributos, null);
	}
	
	@RequestMapping(value="/eliminarLoteExportacionRearchivo.html")
	public String eliminarLoteExportacionRearchivo(
			@RequestParam(value="id",required=true) Long idLoteExportacionRearchivo){
		
		LoteExportacionRearchivo loteExportacionRearchivo=loteExportacionRearchivoService.obtenerPorIdConLoteReferencias(idLoteExportacionRearchivo);
		
		if(!loteExportacionRearchivo.getEstado().equals("NO_GENERADO")){
			eliminarArchivosGenerados(loteExportacionRearchivo);
		}
		
		loteExportacionRearchivoService.eliminar(loteExportacionRearchivo);
		
		return "redirect:consultaExportacionRearchivo.html";
	}
	
	@RequestMapping(value="/exportarLoteExportacionRearchivo.html")
	public String exportarLoteExportacionRearchivo(Map<String,Object> atributos, HttpServletRequest request,
			@RequestParam(value="id",required=true) Long idLoteExportacionRearchivo,
			@RequestParam(value="accion",required=false) String accion){
		
		//Mostramos el lote de exportación para exportar
		LoteExportacionRearchivo loteExportacionRearchivo=loteExportacionRearchivoService.obtenerPorIdConLoteReferencias(idLoteExportacionRearchivo);
		
		if(accion!=null){
			if(!loteExportacionRearchivo.getEstado().equals("NO_GENERADO")){
				eliminarArchivosGenerados(loteExportacionRearchivo);
			}
			try{
				int archivosGenerados = 1;
				do{
					String dir = com.security.constants.Constants.URL_ARCHIVOS_DIGITALES;
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					String ruta = loteExportacionRearchivo.getClienteAsp().getNombreAbreviado()+"//"
						+ loteExportacionRearchivo.getEmpresa().getCodigo()+"//"
						+ loteExportacionRearchivo.getSucursal().getCodigo()+"//"
						+ loteExportacionRearchivo.getClienteEmp().getCodigo()+"//"
						+ loteExportacionRearchivo.getClasificacionDocumental().getCodigo()+"//"
						+ "lote_codigo_" + loteExportacionRearchivo.getId() + "//";
					dir+="EXPORTACION//"+ruta;
					new File(dir).mkdirs();
					loteExportacionRearchivo.setPathArchivoBase(dir);
					
					
					String nombreArchivo = "exportacion_rearchivos_digitales_"+archivosGenerados+".zip";
					InputStream inp = request.getClass().getClassLoader().getResourceAsStream("exportacion_imagenes_base.xls");
					ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(new File(dir+nombreArchivo))); 
					ZipEntry entry = new ZipEntry("exportacion_imagenes.xls");
				    zipOut.putNextEntry(entry);
				    ExportadorExcelRearchivosDigitales exportadorExcel = ExportadorExcelRearchivosDigitales.getNewInstance();
				    exportadorExcel.createWorkbook(zipOut, inp,loteExportacionRearchivo.getFechaRegistro(),loteExportacionRearchivo.getClienteEmp(),loteExportacionRearchivo.getClasificacionDocumental());
				    for(LoteRearchivo loteRearchivo:loteExportacionRearchivo.getLotesRearchivo()){
				    	List<Rearchivo> rearchivos = rearchivoService.listarRearchivoPorLote(loteRearchivo, obtenerClienteAspUser());
				    	loteRearchivo.setRearchivos(new HashSet(rearchivos));
				    	for(Rearchivo rearchivo:loteRearchivo.getRearchivos()){
				    		exportadorExcel.addRearchivo(rearchivo);
				    	}
				    }
				    exportadorExcel.close();
				    zipOut.closeEntry();
				    for(LoteRearchivo loteRearchivo:loteExportacionRearchivo.getLotesRearchivo()){
				    	for(Rearchivo rearchivo: loteRearchivo.getRearchivos()){
				    		entry = new ZipEntry(loteRearchivo.getContenedor().getCodigo()+"/"+loteRearchivo.getId()+"/"+rearchivo.getNombreArchivoDigital());
				    		zipOut.putNextEntry(entry);
				    		File file = new File(rearchivo.getPathArchivoDigital());
				    		FileInputStream in = new FileInputStream(file);
				    		byte[] buf = new byte[1024];
				    		int len;
				    		while ((len = in.read(buf)) > 0){
				    			zipOut.write(buf, 0, len);
				    		}
				    		in.close();
				    		zipOut.closeEntry();
				    	}
				    }
				    zipOut.close();
				    
				} while(archivosGenerados!=1);//TODO: cortar los archivos
				
				loteExportacionRearchivo.setCantPartesGeneradas(archivosGenerados);
				loteExportacionRearchivo.setEstado("GENERADO");
				
				loteExportacionRearchivoService.actualizar(loteExportacionRearchivo);
				
			}catch(Exception e){
				logger.error("No se pudo generar",e);
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioLoteExportacionRearchivo.notificacion.fallaExportacion", Arrays.asList(new String[]{e.getMessage()}));
				avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
			}
		}
		
		atributos.put("loteExportacionRearchivo", loteExportacionRearchivo);
		
		//Si fue generado, mostramos los links para descarga
		ArrayList<LabelLink> links = new ArrayList<LabelLink>();
		if(!loteExportacionRearchivo.getEstado().equals("NO_GENERADO")){
			for(int i=1;i<=loteExportacionRearchivo.getCantPartesGeneradas();i++){
				LabelLink link = new LabelLink();
				link.setLabel("Parte "+i+" de "+loteExportacionRearchivo.getCantPartesGeneradas());
				link.setLink("downloadZipRearchivoDigital.html?lote="+loteExportacionRearchivo.getId()+"&parte="+i);
				links.add(link);
			}
		}
		atributos.put("links", links);
		
		return "exportacionLoteRearchivo";
	}
	
	@RequestMapping(
			value="/downloadZipRearchivoDigital.html",
			method = RequestMethod.GET
		)
	public String downloadFile(Map<String,Object> atributos, HttpServletResponse response,	
			@RequestParam(value="lote",required=true) Long idLoteExportacionRearchivo,
			@RequestParam(value="parte",required=true) Integer parte){
		
		LoteExportacionRearchivo loteExportacionRearchivo=loteExportacionRearchivoService.obtenerPorIdConLoteReferencias(idLoteExportacionRearchivo);
		String fileName=loteExportacionRearchivo.getPathArchivoBase()+"exportacion_rearchivos_digitales_"+parte+".zip";
		
		ServletOutputStream op = null;
		try {
			InputStream in = new FileInputStream(fileName);
			byte[] data = new byte[in.available()];
			in.read(data);
			
			response.setContentType("application/octet-stream;");
			response.setHeader("Content-Disposition","attachment;filename=\"" + "exportacion_rearchivos_digitales_"+parte+".zip" +"\";");
			response.setContentLength(data.length);
			op = response.getOutputStream();

			op.write(data);
			op.flush();
			op.close();

		} catch (IOException e) {
			
			try {
				op.close();
			} catch (Exception e1) {
				return null;
			}
			
		} catch (Exception e) {
			return null;
		};
		return null;
	}

	private void eliminarArchivosGenerados(LoteExportacionRearchivo loteExportacionRearchivo) {
		try {
			File carpetaEliminar = new File(loteExportacionRearchivo.getPathArchivoBase());
			//Recorro la carpeta para eliminar sus archivos
			if(carpetaEliminar.isDirectory()){
				File[] ficheros = carpetaEliminar.listFiles();
				for(int i=0;i<ficheros.length; i++)
					ficheros[i].delete();
			}
		} catch (Exception e) {
			logger.error("no se pudo eliminar",e);
		}
	}
	
	@RequestMapping(value="/popUpSeleccionLoteRearchivoDigital.html")
	public String SeleccionLoteRearchivoDigital(Map<String,Object> atributos, HttpSession session,
			@RequestParam(value="codigo", required=true) Integer codigoClasificacionDocumental,
			@RequestParam(value="codigo2", required=true) String codigoCliente,
			@RequestParam(value="val",required=false) String descripcionLoteRearchivo){
		/*
		 * NOTA: Este método de popUpSeleccion está en el controlador de Exportacion de Rearchivos porque hace uso de
		 * la sesión para filtrar los lotes de rearchivo que ya han sido cargados en el lote de exportación. 
		 */
		List<LoteRearchivo> lotesRearchivo = (List<LoteRearchivo>) session.getAttribute("lotesRearchivo");
		List<LoteRearchivo> lotesRearchivoSeleccionables = loteRearchivoService.obtenerPor(
				obtenerClienteAspUser(),
				codigoCliente,
				codigoClasificacionDocumental,
				descripcionLoteRearchivo,
				"Digital");
		Iterator<LoteRearchivo> iterator = lotesRearchivoSeleccionables.iterator();
		while(iterator.hasNext()){
			LoteRearchivo loteRearchivoSeleccionable = iterator.next();
			for(LoteRearchivo loteSeleccionado : lotesRearchivo){
				if(loteRearchivoSeleccionable.getId().equals(loteSeleccionado.getId())){
					iterator.remove();
					break;
				}
			}
		}
		atributos.put("mapa", "lotesRearchivoPopupMap");
		atributos.put("clase", "lotesRearchivoPopupMap");
		Map<String,Object> lotesRearchivoPopupMap = new HashMap<String, Object>();
		
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioLoteRearchivo.lista.codigo",false));
		campos.add(new CampoDisplayTag("tipo","formularioLoteRearchivo.lista.tipo",false));
		campos.add(new CampoDisplayTag("fechaRegistroStr","formularioLoteRearchivo.lista.fecha",false));
		campos.add(new CampoDisplayTag("indiceIndividualStr","formularioLoteRearchivo.lista.tipoIndice",false));
		campos.add(new CampoDisplayTag("contenedor.codigo","formularioLoteRearchivo.lista.contenedor",false));
		campos.add(new CampoDisplayTag("cantidad","formularioLoteRearchivo.lista.cantidad",false));
		campos.add(new CampoDisplayTag("remito.letraYNumeroComprobante","formularioLoteRearchivo.lista.remito",false));
		lotesRearchivoPopupMap.put("campos", campos);		
		lotesRearchivoPopupMap.put("coleccionPopup", lotesRearchivoSeleccionables);
		lotesRearchivoPopupMap.put("referenciaPopup", "id");
		lotesRearchivoPopupMap.put("referenciaHtml", "codigoLoteRearchivoSeleccionado"); 		
		lotesRearchivoPopupMap.put("urlRequest", "popUpSeleccionLoteRearchivoDigital.html?codigo2="+codigoCliente);
		lotesRearchivoPopupMap.put("textoBusqueda", descripcionLoteRearchivo);
		lotesRearchivoPopupMap.put("filterPopUp", codigoClasificacionDocumental);
		lotesRearchivoPopupMap.put("tituloPopup", "textos.seleccion");
		atributos.put("lotesRearchivoPopupMap", lotesRearchivoPopupMap);
		
		return "popupBusquedaNuevo";
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
}
