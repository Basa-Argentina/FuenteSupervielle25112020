/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.dividato.configuracionGeneral.controladores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
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

import com.dividato.configuracionGeneral.objectForms.LoteReferenciaBusquedaForm;
import com.dividato.configuracionGeneral.validadores.LoteReferenciaValidator;
import com.dividato.configuracionGeneral.validadores.ReferenciaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaHistoricoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.LoteReferencia;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.ReferenciaHistorico;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.servicios.MailManager;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la administración
 * de la ClasificacionDocumental.
 *
 * @author Federico Muñoz
 */

@Controller
@RequestMapping(
		value=
			{	
				"/consultaLotesReferencia.html",
				"/borrarFiltrosLoteReferencia.html",
				"/eliminarLoteReferencia.html",
				"/agregarRangoReferencia.html",
				"/iniciarFormularioLoteReferencia.html",
				"/precargaFormularioLoteReferencia.html",
				"/seccionReferencias.html",
				"/agregarReferencia.html",
				"/modificarReferencia.html",
				"/eliminarReferencia.html",
				"/refrescarFormReferencia.html",
				"/guardarActualizarLoteReferencia.html",
				"/imprimirLoteReferencia.html",
				"/validarReferenciaContenedor.html"
			}
		)
public class FormLoteReferenciaController {
	private static Logger logger=Logger.getLogger(FormLoteReferenciaController.class);
	private LoteReferenciaService loteReferenciaService;
	private ClasificacionDocumentalService clasificacionDocumentalService;
	private ClienteEmpService clienteEmpService;
	private SucursalService sucursalService;
	private EmpresaService empresaService;
	private ElementoService elementoService;
	private ReferenciaService referenciaService;
	private ReferenciaHistoricoService referenciaHistoricoService;
	private LoteReferenciaValidator loteValidator;
	private ReferenciaValidator refValidator;
	private MailManager mailManager;
	private UserService userService;
	private EmpleadoService empleadoService;
	
	@Autowired
	public void setServices(LoteReferenciaService loteReferenciaService,
			ClasificacionDocumentalService clasificacionDocumentalService,
			SucursalService sucursalService,
			ClienteEmpService clienteEmpService,
			EmpresaService empresaService,
			ElementoService elementoService,
			ReferenciaService referenciaService,
			ReferenciaHistoricoService referenciaHistoricoService,
			EmpleadoService empleadoService) {
		this.loteReferenciaService = loteReferenciaService;
		this.clasificacionDocumentalService = clasificacionDocumentalService;
		this.sucursalService = sucursalService;
		this.clienteEmpService = clienteEmpService;
		this.empresaService = empresaService;
		this.elementoService = elementoService;
		this.referenciaService = referenciaService;
		this.referenciaHistoricoService = referenciaHistoricoService;
		this.empleadoService = empleadoService;
	}
	
	@Autowired
	public void setValidator(LoteReferenciaValidator validator,ReferenciaValidator refValidator) {
		this.loteValidator = validator;
		this.refValidator = refValidator;
	}
	
	@Autowired
	public void setMailManager(MailManager mailManager){
		this.mailManager=mailManager;
	}
	
	@Autowired
	public void setUserService(UserService userService){
		this.userService=userService;
	}
	
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		if(binder.getTarget() instanceof LoteReferenciaBusquedaForm){
			binder.registerCustomEditor(Date.class, "fechaDesde",new CustomDateEditor(formatoFechaFormularios, true));
			binder.registerCustomEditor(Date.class, "fechaHasta",new CustomDateEditor(formatoFechaFormularios, true));
		}else if (binder.getTarget() instanceof LoteReferencia){
			loteValidator.initDataBinder(binder);
		}else if (binder.getTarget() instanceof Referencia){
			refValidator.initDataBinder(binder);
		}		
	}
	
	@RequestMapping(value="/consultaLotesReferencia.html")
	public String consultaLotesReferencia(Map<String,Object> atributos,
			@ModelAttribute("busquedaLoteReferenciaFormulario") LoteReferenciaBusquedaForm busquedaLoteReferencia,
			HttpServletRequest request, HttpSession session) {
		if(busquedaLoteReferencia==null)
			busquedaLoteReferencia=new LoteReferenciaBusquedaForm();
		
		Empleado empleado = (Empleado) session.getAttribute("empleadoSession");
		
		if(busquedaLoteReferencia.getIdClienteAsp()==null)
		{
			busquedaLoteReferencia.setIdClienteAsp(obtenerClienteAspUser().getId());
			busquedaLoteReferencia.setCodigoEmpresa(obtenerCodigoEmpresaUser());
			busquedaLoteReferencia.setCodigoSucursal(obtenerCodigoSucursalUser());
			if(empleado!=null)
			{
				busquedaLoteReferencia.setCodigoCliente(empleado.getClienteEmp().getCodigo());
				busquedaLoteReferencia.setCodigoPersonal(empleado.getCodigo());
			}
		}
		
		if(busquedaLoteReferencia.getCodigoPersonal()!=null && !busquedaLoteReferencia.getCodigoPersonal().isEmpty())
		{
			busquedaLoteReferencia.setPersonal(empleadoService.getByCodigo(busquedaLoteReferencia.getCodigoPersonal(), busquedaLoteReferencia.getCodigoCliente(), obtenerClienteAspUser()));
		}
		
		
		//cuenta la cantidad de resultados
 		Integer size = loteReferenciaService.contarLotesSql(
				obtenerClienteAspUser(),
				busquedaLoteReferencia.getCodigoEmpresa(),
				busquedaLoteReferencia.getCodigoSucursal(),
				busquedaLoteReferencia.getCodigoCliente(),
				busquedaLoteReferencia.getPersonal(),
				busquedaLoteReferencia.getCodigoDesde(),
				busquedaLoteReferencia.getCodigoHasta(),
				busquedaLoteReferencia.getFechaDesde(),
				busquedaLoteReferencia.getFechaHasta()
				);
		
			atributos.put("size", size);
		
			//paginacion y orden de resultados de displayTag
			busquedaLoteReferencia.setTamañoPagina(20);	
			if(request != null){
				String nPaginaStr= request.getParameter((new ParamEncoder("loteReferencia").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
				if(nPaginaStr==null){
					nPaginaStr = (String)atributos.get((new ParamEncoder("loteReferencia").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
				}
				String fieldOrder = request.getParameter( new ParamEncoder("loteReferencia").encodeParameterName(TableTagParameters.PARAMETER_SORT));
				busquedaLoteReferencia.setFieldOrder(fieldOrder);
				String sortOrder = request.getParameter(new ParamEncoder("loteReferencia").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
				busquedaLoteReferencia.setSortOrder(sortOrder);
				Integer nPagina = 1;		
				if(nPaginaStr!=null){
					nPagina = (Integer.parseInt(nPaginaStr));
				}
				busquedaLoteReferencia.setNumeroPagina(nPagina);
			}
		
			
		List<LoteReferencia> lotesReferencia = loteReferenciaService
				.obtenerLoteReferenciaSQL(
						obtenerClienteAspUser(),
						busquedaLoteReferencia.getCodigoEmpresa(),
						busquedaLoteReferencia.getCodigoSucursal(),
						busquedaLoteReferencia.getCodigoCliente(),
						busquedaLoteReferencia.getPersonal(),
						busquedaLoteReferencia.getCodigoDesde(),
						busquedaLoteReferencia.getCodigoHasta(),
						busquedaLoteReferencia.getFechaDesde(),
						busquedaLoteReferencia.getFechaHasta(),
						busquedaLoteReferencia.getFieldOrder(), 
						busquedaLoteReferencia.getSortOrder(), 
						busquedaLoteReferencia.getNumeroPagina(), 
						busquedaLoteReferencia.getTamañoPagina()
						);
		
		atributos.put("lotesReferencia", lotesReferencia);

		return "consultaLoteReferencia";
	}
	
	@RequestMapping(value="/borrarFiltrosLoteReferencia.html")
	public String borrarFiltrosLoteReferencia(Map<String,Object> atributos,
			HttpServletRequest request, HttpSession session) {
		return consultaLotesReferencia(atributos, null,request,session);
	}
	
	@RequestMapping(value="/eliminarLoteReferencia.html")
	public String eliminarLoteReferencia(Map<String,Object> atributos,
			@RequestParam(value="id",required=true) Long id,
			HttpServletRequest request,HttpSession session){
	
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		boolean hayAvisos = false;
		boolean hayAvisosNeg = false;
		ScreenMessage mensaje;
		
		int resultado = loteReferenciaService.eliminarLoteReferencia(id);
		
		if(resultado == 2){
			mensaje = new ScreenMessageImp("formularioLoteReferencia.notificacion.loteReferenciaEliminado", null);
			hayAvisos = true;
		}else if(resultado == 1){
			mensaje = new ScreenMessageImp("formularioLoteReferencia.notificacion.loteReferenciaEliminadoAMedias", null);
			hayAvisos = true;
		}else
		{
			mensaje = new ScreenMessageImp("formularioLoteReferencia.notificacion.loteReferenciaNoEliminado", null);
			hayAvisosNeg = true;
		}
		
		avisos.add(mensaje);
		
		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		return consultaLotesReferencia(atributos,null,request,session);
	}
	
	@RequestMapping(value="/iniciarFormularioLoteReferencia.html")
	public String iniciarFormularioLotesReferencia(Map<String,Object> atributos,
			HttpSession session,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) Long idLoteReferencia) {
		
		session.removeAttribute("referencias_lote");
		session.removeAttribute("referencias_modificadas");
		session.removeAttribute("referencias_eliminadas");
		session.removeAttribute("ordenSession");
		logger.error(obtenerUser().getPersona() + "--Entro al iniciar");
		return precargaFormularioLotesReferencia(atributos, session, accion, idLoteReferencia);
	}
	
	@RequestMapping(value="/precargaFormularioLoteReferencia.html")
	public String precargaFormularioLotesReferencia(Map<String,Object> atributos,
			HttpSession session,
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) Long idLoteReferencia) {
		logger.error(obtenerUser().getPersona() + "--Entro a la precarga");
		LoteReferencia loteReferencia = null;
		
		if(accion==null) accion="nueva_carga_individual"; //acción por defecto: nueva carga individual
		if(accion.equals("nueva_carga_individual")){
			loteReferencia=new LoteReferencia();
			loteReferencia.setId(0L);
//////////////Linea agregada para el codigo del lote
			loteReferencia.setCodigo(0L);
////////////////////////////////////////////////////
			loteReferencia.setIndiceIndividual(true);
			accion="NUEVO";
		}else if (accion.equals("nueva_carga_grupal")){
			loteReferencia=new LoteReferencia();
			loteReferencia.setIndiceIndividual(false);
			accion="NUEVO";
		}
		else if (accion.equals("nueva_carga_rango")){
			loteReferencia=new LoteReferencia();
			loteReferencia.setId(0L);
//////////////Linea agregada para el codigo del lote
			loteReferencia.setCodigo(0L);
////////////////////////////////////////////////////
			loteReferencia.setIndiceIndividual(false);
			loteReferencia.setCargaPorRango(true);
			atributos.put("porRango", true);
			accion="NUEVO";
		}
		
		if(accion.equals("NUEVO"))
		{
			Empleado empleado = (Empleado) session.getAttribute("empleadoSession");
			if(empleado!=null){
				atributos.put("bloqueoEmpleado", true);
				loteReferencia.setClienteEmp(empleado.getClienteEmp());
			}
			loteReferencia.setClienteAsp(obtenerClienteAspUser());
			loteReferencia.setEmpresa(empresaService.getByCodigo(obtenerCodigoEmpresaUser(), loteReferencia.getClienteAsp()));
			loteReferencia.setSucursal(sucursalService.getByCodigo(obtenerCodigoSucursalUser(), loteReferencia.getClienteAsp()));
			loteReferencia.setReferencias(new ArrayList<Referencia>());
			loteReferencia.setFechaRegistro(new Date());
			session.removeAttribute("ordenSession");
		} 
		else if(accion.equals("MODIFICACIONRANGO"))
		{
			loteReferencia=loteReferenciaService.obtenerPorId(idLoteReferencia);
			List<Referencia> referencias = referenciaService.listarReferenciaPorLote(loteReferencia, obtenerClienteAspUser());
			loteReferencia.setReferencias(new ArrayList<Referencia>(referencias));
			if(!loteReferencia.getReferencias().isEmpty())
				
			{
				loteReferencia.setIndiceIndividual(false);
				atributos.put("porRango", true);
				session.setAttribute("ordenSession", referencias.get(referencias.size()-1).getOrdenRearchivo());
			}
		} 
		else
		{
			loteReferencia=loteReferenciaService.obtenerPorId(idLoteReferencia);
			List<Referencia> referencias = referenciaService.listarReferenciaPorLote(loteReferencia, obtenerClienteAspUser());
			loteReferencia.setReferencias(new ArrayList<Referencia>(referencias));
			if(!loteReferencia.getReferencias().isEmpty())
			{
				loteReferencia.setIndiceIndividual(loteReferencia.getReferencias().iterator().next().getIndiceIndividual());
				if(!accion.equals("CONSULTA"))
					session.setAttribute("ordenSession", referencias.get(referencias.size()-1).getOrdenRearchivo());
				
			}
		}
		
		
		//Seteo la accion actual
		atributos.put("accion", accion);
		atributos.put("loteReferencia", loteReferencia);
		session.setAttribute("referencias_lote", new ArrayList(loteReferencia.getReferencias()));
		return "formularioLoteReferencia";
	}
	@RequestMapping(value="/guardarActualizarLoteReferencia.html",method=RequestMethod.POST)
	public String guardarLoteReferencia(Map<String,Object> atributos, HttpSession session,
			@RequestParam(value="accion") String accion,
			@ModelAttribute("loteReferencia") LoteReferencia loteReferencia,
			BindingResult result,
			@RequestParam(value="codigoEmpresa",required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal",required=false) String codigoSucursal,
			@RequestParam(value="codigoClasificacionDocumentalPadre",required=false) String codigoClasificacionDocumental,
			@RequestParam(value="codigoTipoPadre",required=false) String codigoTipo,
			@RequestParam(value="codigoContenedorPadre",required=false) String codigoContenedor,
			@RequestParam(value="codigoContenedorComparar",required=false) String codigoContenedorComparar,
			@RequestParam(value="bloqueoClasificacion",required=false) Boolean bloqueoClasificacionDocumental,
			@RequestParam(value="bloqueoTipo",required=false) Boolean bloqueoTipo,
			@RequestParam(value="bloqueoContenedor",required=false) Boolean bloqueoContenedor,
			@RequestParam(value="bloqueoNumero1",required=false) Boolean bloqueoNumero1,
			@RequestParam(value="bloqueoTexto1",required=false) Boolean bloqueoTexto1,
			@RequestParam(value="numero1Padre",required=false) String numero1,
			@RequestParam(value="texto1Padre",required=false) String texto1,
			@RequestParam(value="bloqueoNumero2",required=false) Boolean bloqueoNumero2,
			@RequestParam(value="bloqueoTexto2",required=false) Boolean bloqueoTexto2,
			@RequestParam(value="numero2Padre",required=false) String numero2,
			@RequestParam(value="texto2Padre",required=false) String texto2,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente,
			@RequestParam(value="incrementoElemento",required=false) Boolean incrementoElemento,
			@RequestParam(value="codigoElementoPadre",required=false) String codigoElemento,
			@RequestParam(value="guardarContinuar",required=false) Boolean guardarContinuar,
			HttpServletRequest request){
		
		String entrada = " Entro al Guardar y Salir";
		if(guardarContinuar!=null && guardarContinuar)
			entrada = " Entro al Guardar y Continuar";
		
		logger.error(obtenerUser().getPersona() + entrada);
		Boolean commit = null;
		
		loteReferencia.setClienteAsp(obtenerClienteAspUser());
		loteReferencia.setEmpresa(empresaService.getByCodigo(codigoEmpresa, loteReferencia.getClienteAsp()));
		loteReferencia.setSucursal(sucursalService.getByCodigo(codigoSucursal, loteReferencia.getClienteAsp()));
		loteReferencia.setClienteEmp(clienteEmpService.getByCodigo(codigoCliente));
		
		loteReferencia.setReferencias((List<Referencia>) session.getAttribute("referencias_lote"));
		List<Referencia> referenciasModificadas = (List<Referencia>) session.getAttribute("referencias_modificadas");
		List<Referencia> referenciasEliminadas = (List<Referencia>) session.getAttribute("referencias_eliminadas");
		loteReferencia.setModificadas(referenciasModificadas);
		loteReferencia.setEliminadas(referenciasEliminadas);
		if(!result.hasErrors()){
			
			logger.error(obtenerUser().getPersona() + "--Entra al validador");
			String lote = "Lote ID: "+loteReferencia.getId()+" CODIGO: "+ loteReferencia.getCodigo() +" Referencias : ";
			logger.error(lote);
			
			for(Referencia ref: loteReferencia.getReferencias()){
				String elemento= "Codigo: " + ref.getElemento().getCodigo() +"ID Elemento: "+ref.getElemento().getId() + " ID Ref :" + (ref.getId()!=null? ref.getId().toString():" - "); 
				logger.error(elemento);
			}
			
			if(referenciasModificadas!=null && referenciasModificadas.size()>0){
				logger.error("Referencias Modificadas :");
				for(Referencia ref: referenciasModificadas){
					String elemento = "Codigo: " + ref.getElemento().getCodigo() +"ID Elemento: "+ref.getElemento().getId() + " ID Ref :"+ ref.getId()!=null?ref.getId().toString():" - "; 
					logger.error(elemento);
				}
			}
			
			if(referenciasEliminadas!=null && referenciasEliminadas.size()>0){
				logger.error("Referencias Eliminadas :");
				for(Referencia ref: referenciasEliminadas){
					String elemento = "Codigo: " + ref.getElemento().getCodigo() +"ID Elemento: "+ref.getElemento().getId() + " ID Ref :"+ ref.getId()!=null?ref.getId().toString():" - "; 
					logger.error(elemento);
				}
			}
			logger.error("Usuario: " + obtenerUser().getPersona());
			loteValidator.validate(loteReferencia, result);
		}
		
		if(!result.hasErrors()){
			logger.error(obtenerUser().getPersona()+ " Paso satisfactoriamente, y intenta guardar en base.");
			//loteReferenciaService.guardarActualizar(loteReferencia);
			commit = loteReferenciaService.guardarActualizarLoteYModificadas(loteReferencia, referenciasModificadas, referenciasEliminadas);
			
			
			if(commit!=null && commit)
			{
				if(loteReferencia.getIndiceIndividual()){
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
				ScreenMessage mensajeLoteReg = new ScreenMessageImp("formularioLoteReferencia.notificacion.loteReferenciaRegistrado", Arrays.asList(new String[]{loteReferencia.getCodigo().toString()}));
				avisos.add(mensajeLoteReg); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", true);
				atributos.put("avisos", avisos);
				
				session.removeAttribute("referencias_lote");
				session.removeAttribute("referencias_modificadas");
				session.removeAttribute("referencias_eliminadas");
				
				logger.error(obtenerUser().getPersona()+ " Guardo correctamente y limpio las sessiones");
				if(guardarContinuar!=null && guardarContinuar){
						atributos.put("bloqueoClasificacion",bloqueoClasificacionDocumental);
						atributos.put("codigoContenedorComparar",codigoContenedorComparar);
						atributos.put("codigoClasificacionDocumentalPadre", codigoClasificacionDocumental);
						atributos.put("bloqueoTipo",bloqueoTipo);
						atributos.put("codigoTipoPadre", codigoTipo);
						atributos.put("bloqueoContenedor",bloqueoContenedor);
						atributos.put("codigoContenedorPadre", codigoContenedor);
						atributos.put("bloqueoNumero1",bloqueoNumero1);
						atributos.put("numero1Padre", numero1);
						atributos.put("bloqueoTexto1",bloqueoTexto1);
						atributos.put("texto1Padre", texto1);
						atributos.put("bloqueoNumero2",bloqueoNumero2);
						atributos.put("numero2Padre", numero2);
						atributos.put("bloqueoTexto2",bloqueoTexto2);
						atributos.put("texto2Padre", texto2);
						if(incrementoElemento != null && incrementoElemento){
	//						List<String> codigosUtilizados = new ArrayList<String>();
	//						codigosUtilizados.add(codigoElemento);
	//						Elemento elemento = elementoService.obtenerProximoElementoDisponible(obtenerClienteAspUser(),codigoCliente,codigosUtilizados);
							atributos.put("codigoElementoPadre", codigoElemento);
							atributos.put("incrementoElemento", incrementoElemento);
						}
						logger.error(obtenerUser().getPersona() + " --Guardar y continuar - vuelve a pagina de carga");
					return precargaFormularioLotesReferencia(atributos,session,"MODIFICACION", loteReferencia.getId());
				}
				//hacemos el redirect
				logger.error(obtenerUser().getPersona() + " --Guardar y salir - Vuelve a pagina de lotes");
				return consultaLotesReferencia(atributos, null,request, session);
			}else{
				logger.error(obtenerUser().getPersona() + " --Error al guardar en base de datos - se vuelve a pantalla de carga");
				//Genero las notificaciones 
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				ScreenMessage mensajeLoteNOReg = new ScreenMessageImp("formularioLoteReferencia.error.loteReferenciaNORegistrado", null);
				avisos.add(mensajeLoteNOReg); //agrego el mensaje a la coleccion
				atributos.put("errores", false);
				atributos.remove("result");
				atributos.put("hayAvisos", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
				return "formularioLoteReferencia";
			}
		}
		else
		{
			logger.error("No paso las validaciones - se vuelve a pagina de carga");
			atributos.put("loteReferencia", loteReferencia);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
			atributos.remove("codigoClasificacionDocumentalPadre");
			return "formularioLoteReferencia";
		}
	
	}
	
	@RequestMapping(value="/seccionReferencias.html")
	public String seccionReferencias(Map<String,Object> atributos, HttpSession session,
			@RequestParam(value="indiceIndividual",required=false)Boolean indiceIndividual,
			@RequestParam(value="porRango",required=false) Boolean porRango){
			
		if(indiceIndividual== null)
			indiceIndividual = false;
		
		inicializarFormularioReferenciaContenedor(atributos, session, indiceIndividual);
		//if(indiceIndividual) atributos.put("incrementoElemento", true);
		if(porRango!=null && porRango)
			atributos.put("porRango", true);
		return "formularioReferencia";
	}
	
	@RequestMapping(value="/validarReferenciaContenedor.html")
	public void validarReferenciaContenedor(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo
			){

		String respuesta = "";
		if(!codigo.isEmpty()){
			//Elemento elemento = elementoService.getByCodigo(codigo, obtenerClienteAspUser());
			Long refList = referenciaService.obtenerCantidadByElementoContenedor(codigo);
			if(refList!=null && refList!=0){
				respuesta = codigo;
			}
		}
		
		try {
			response.setContentType("text/xml"); 
			response.setCharacterEncoding("ISO-8859-1"); //seteo el encoding de los caracteres 
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(respuesta);
		} catch (IOException e) {
			logger.error("No se pudo escribir la respuesta", e);
		}	
	}

	@RequestMapping(value="/agregarReferencia.html",method=RequestMethod.POST)
	public String agregarReferencia(Map<String,Object> atributos, HttpSession session,
			@ModelAttribute(value="referenciaFormulario") Referencia referencia,
			BindingResult result,
			@RequestParam(value="obj_hash",required=false) String objectHash,
			@RequestParam(value="idLoteReferencia",required=false) Long idLoteReferencia,
			@RequestParam(value="codigoCliente") String codigoCliente,
			@RequestParam(value="codigoClasificacionDocumental") Integer codigoClasificacionDocumental,
			@RequestParam(value="codigoContenedor") String codigoContenedor,
			@RequestParam(value="codigoElemento",required=false) String codigoElemento,
			@RequestParam(value="codigoContenedorComparar")String codigoContenedorComparar,
			@RequestParam(value="bloqueoClasificacionDocumental",required=false) Boolean bloqueoClasificacionDocumental,
			@RequestParam(value="bloqueoTipoElementoContenedor",required=false) Boolean bloqueoTipoElementoContenedor,
			@RequestParam(value="bloqueoContenedor",required=false) Boolean bloqueoContenedor,
			@RequestParam(value="bloqueoNumero1",required=false) Boolean bloqueoNumero1,
			@RequestParam(value="bloqueoNumero2",required=false) Boolean bloqueoNumero2,
			@RequestParam(value="bloqueoTexto1",required=false) Boolean bloqueoTexto1,
			@RequestParam(value="bloqueoTexto2",required=false) Boolean bloqueoTexto2,
			@RequestParam(value="bloqueoFecha1",required=false) Boolean bloqueoFecha1,
			@RequestParam(value="bloqueoFecha2",required=false) Boolean bloqueoFecha2,
			@RequestParam(value="incrementoElemento",required=false) Boolean incrementoElemento,
			@RequestParam(value="cerrarCaja",required=false) Boolean cerrarCaja,
			@RequestParam(value="chkDoubleInput",required=false) Boolean chkDoubleInput)
	{
		
		procesarFormularioReferencia(referencia, codigoCliente, codigoClasificacionDocumental,codigoContenedor, codigoElemento,idLoteReferencia!=null?idLoteReferencia:0);

		List<Referencia> referencias = (List<Referencia>) session.getAttribute("referencias_lote");
		List<Referencia> referenciasModificadas = (List<Referencia>) session.getAttribute("referencias_modificadas");
		if(referenciasModificadas==null)
			referenciasModificadas = new ArrayList<Referencia>();
		if(!result.hasErrors()){
			refValidator.validate(referencia, result);
		}
		if(!result.hasErrors()){
			if(referencia.getIndiceIndividual()){
				for(Referencia ref:referencias){
					if(ref.getIndiceIndividual()){
						if(ref.getElemento().getId().equals(referencia.getElemento().getId())){
							if(objectHash==null || !objectHash.equals(ref.getObj_hash()))
								result.rejectValue("elemento", "repetido");
						}
					}
				}
			}
		}
		
		if(!result.hasErrors()){
			if(objectHash!=null){
				for(Referencia candidato:referencias){
					if(candidato.getObj_hash().equals(objectHash)){
						if(candidato!=null && candidato.getId()!=null)
							referenciasModificadas.add(candidato);
						referencias.remove(candidato);
						break;
					}
				}
			}
			
			if(referencia.getContenedor()!=null){
				referencia.getContenedor().setCerrado(cerrarCaja);
				elementoService.actualizarElemento(referencia.getContenedor());
			}
			
			if(referencia.getCodigoUsuario()!=null){
				referencia.setEstadoTarea("En Proceso");
			}
			
			
			Integer orden = (Integer) session.getAttribute("ordenSession");
			if(orden==null)
				orden = 0;
			orden++;
			referencia.setOrdenRearchivo(orden);
			session.setAttribute("ordenSession", orden);

			//AGREGADO PEDIDO POR SANCOR PARA SABER EN EL REPORTE LA HORA DE CARGA DE CADA UNA
			if(referencia.getId()==null)
				referencia.setFechaHora(new Date());
			completarCampos(referencia);
			referencias.add(referencia);
			Referencia nuevaReferencia=new Referencia();
			nuevaReferencia.setIndiceIndividual(referencia.getIndiceIndividual());
			if(referencia.getIndiceIndividual()){
				atributos.put("hacerFocusEn", "codigoElemento");
				atributos.put("fijarContenedor", true);
				List<String> codigosElementoUtilizados = new ArrayList<String>();
				
				if(incrementoElemento){
					for(Referencia ref:referencias)
						codigosElementoUtilizados.add(ref.getElemento().getCodigo());
					nuevaReferencia.setElemento(elementoService.obtenerProximoElementoDisponible(obtenerClienteAspUser(),codigoCliente,codigosElementoUtilizados));
					atributos.put("hacerFocusEn", "codigoClasificacionDocumental");
				}
				//nuevaReferencia.setContenedor(referencia.getContenedor());
				//nuevaReferencia.setPrefijoCodigoTipoElemento(referencia.getContenedor().getTipoElemento().getPrefijoCodigo());
				//--- SE COMENTARON LAS DOS LINEAS ANTERIORES Y SE AGREGO EL SIGUIENTE BLOQUE IF POR QUE SINO PERSISTIAN CAMPOS
				//--- CON EL CANDADO ABIERTO
				if(bloqueoContenedor){
					nuevaReferencia.setContenedor(referencia.getContenedor());//Ver el problema del codigo del contenedor
					if(referencia.getContenedor()!=null){
						nuevaReferencia.setPrefijoCodigoTipoElemento(referencia.getContenedor().getTipoElemento().getPrefijoCodigo());
						bloqueoTipoElementoContenedor=true;
					}
				}else {
					//nuevaReferencia.setCajaCerrada
					if(bloqueoTipoElementoContenedor){
						nuevaReferencia.setPrefijoCodigoTipoElemento(referencia.getPrefijoCodigoTipoElemento());
						atributos.put("hacerFocusEn", "codigoContenedor");
					}else
						atributos.put("hacerFocusEn", "prefijoCodigoTipoElemento");
				}
			}
			// Referencia no es individual (Puede ser grupal)
			else {
				if(bloqueoContenedor){
					nuevaReferencia.setContenedor(referencia.getContenedor());
					if(referencia.getContenedor()!=null){
						nuevaReferencia.setPrefijoCodigoTipoElemento(referencia.getContenedor().getTipoElemento().getPrefijoCodigo());
						bloqueoTipoElementoContenedor=true;
					}
				}else {
					if(bloqueoTipoElementoContenedor){
						nuevaReferencia.setPrefijoCodigoTipoElemento(referencia.getPrefijoCodigoTipoElemento());
						atributos.put("hacerFocusEn", "codigoContenedor");
					}else
						atributos.put("hacerFocusEn", "prefijoCodigoTipoElemento");
				}					
			}
			if(bloqueoClasificacionDocumental)
				nuevaReferencia.setClasificacionDocumental(referencia.getClasificacionDocumental());
			else
				atributos.put("hacerFocusEn", "codigoClasificacionDocumental");
			
			if(bloqueoNumero1!=null && bloqueoNumero1)
				nuevaReferencia.setNumero1(referencia.getNumero1());
			if(bloqueoNumero2!=null && bloqueoNumero2)
				nuevaReferencia.setNumero2(referencia.getNumero2());
			if(bloqueoTexto1!=null && bloqueoTexto1)
				nuevaReferencia.setTexto1(referencia.getTexto1());
			if(bloqueoTexto2!=null && bloqueoTexto2)
				nuevaReferencia.setTexto2(referencia.getTexto2());			
			if(bloqueoFecha1!=null && bloqueoFecha1)
				nuevaReferencia.setFecha1(referencia.getFecha1());
			if(bloqueoFecha2!=null && bloqueoFecha2)
				nuevaReferencia.setFecha2(referencia.getFecha2());
			
			atributos.put("referencia", nuevaReferencia);
		}else{
			if(referencia.getIndiceIndividual() && !referencias.isEmpty())
				atributos.put("fijarContenedor", true);
			atributos.put("referencia", referencia);
			atributos.put("objectHash", objectHash);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");		
		}
		
		//Bloque que controla donde hara el foco de acuerdo a los campos bloqueados al regresar a la pagina
		String hacerFoco = "prefijoCodigoTipoElemento";
		if(bloqueoTipoElementoContenedor!= null && !bloqueoTipoElementoContenedor){
			hacerFoco = "prefijoCodigoTipoElemento";	
		}else{
			if(bloqueoContenedor!= null && !bloqueoContenedor){	
				hacerFoco = "codigoContenedor";
			}else{
				if(incrementoElemento!=null && !incrementoElemento){	
					hacerFoco = "codigoElemento";
				}else{
					if(bloqueoClasificacionDocumental!=null && !bloqueoClasificacionDocumental){
						hacerFoco = "codigoClasificacionDocumental";
					}else{
						if(bloqueoNumero1!=null && !bloqueoNumero1){
							hacerFoco = "numero1";
						}else{
							if(bloqueoNumero2!=null && !bloqueoNumero2){
								hacerFoco = "numero2";
							}else{
								if(bloqueoFecha1!=null && !bloqueoFecha1){
									hacerFoco = "fecha1";
								}else{
									if(bloqueoFecha2!=null && !bloqueoFecha2){
										hacerFoco = "fecha2";
									}else{
										if(bloqueoTexto1!=null && !bloqueoTexto1){
											hacerFoco = "texto1";
										}else{
											if(bloqueoTexto2!=null && !bloqueoTexto2){
												hacerFoco = "texto2";
											}else{
												hacerFoco = "descripcion";
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
				
				
				
				
				//				if(incrementoElemento!=null && incrementoElemento && referencia.getIndiceIndividual()){
//					hacerFoco = "codigoClasificacionDocumental";
//					if(bloqueoClasificacionDocumental!=null && bloqueoClasificacionDocumental){
//						hacerFoco = "numero1";
//						if(bloqueoNumero1!=null && bloqueoNumero1){
//							hacerFoco = "texto1";
//							if(bloqueoTexto1!=null && bloqueoTexto1){
//								hacerFoco = "numero2";
//								if(bloqueoNumero2!=null && bloqueoNumero2)
//									hacerFoco = "texto2";
//							}
//						}
//					}
//				}else{
//					if(bloqueoClasificacionDocumental!=null && bloqueoClasificacionDocumental){
//						hacerFoco = "numero1";
//						if(bloqueoNumero1!=null && bloqueoNumero1){
//							hacerFoco = "texto1";
//							if(bloqueoTexto1!=null && bloqueoTexto1){
//								hacerFoco = "numero2";
//								if(bloqueoNumero2!=null && bloqueoNumero2)
//									hacerFoco = "texto2";
//							}
//						}
//					}
//				}
//			}
//		}
		
		atributos.put("bloqueoClasificacionDocumental", bloqueoClasificacionDocumental);
		atributos.put("bloqueoTipoElementoContenedor", bloqueoTipoElementoContenedor);
		atributos.put("bloqueoContenedor", bloqueoContenedor);
		atributos.put("bloqueoTexto1", bloqueoTexto1);
		atributos.put("bloqueoTexto2", bloqueoTexto2);
		atributos.put("bloqueoNumero1", bloqueoNumero1);
		atributos.put("bloqueoNumero2", bloqueoNumero2);
		atributos.put("bloqueoFecha1", bloqueoFecha1);
		atributos.put("bloqueoFecha2", bloqueoFecha2);
		atributos.put("incrementoElemento", incrementoElemento);
		atributos.put("hacerFocusEn", hacerFoco);
		atributos.put("codigoContenedorComparar", codigoContenedorComparar);
		atributos.put("chkDoubleInput", chkDoubleInput);
		session.setAttribute("referencias_modificadas", referenciasModificadas);
		
		return "formularioReferencia";
	}
	
	@RequestMapping(value="/eliminarReferencia.html")
	public String eliminarReferencia(Map<String,Object> atributos, HttpSession session,
			@RequestParam("obj_hash") String objectHash,
			@RequestParam("indiceIndividual") Boolean indiceIndividual,
			@RequestParam(value="bloqueoClasificacionDocumental",required=false) Boolean bloqueoClasificacionDocumental,
			@RequestParam(value="bloqueoTipoElementoContenedor",required=false) Boolean bloqueoTipoElementoContenedor,
			@RequestParam(value="bloqueoContenedor",required=false) Boolean bloqueoContenedor,
			@RequestParam(value="bloqueoNumero1",required=false) Boolean bloqueoNumero1,
			@RequestParam(value="bloqueoNumero2",required=false) Boolean bloqueoNumero2,
			@RequestParam(value="bloqueoTexto1",required=false) Boolean bloqueoTexto1,
			@RequestParam(value="bloqueoTexto2",required=false) Boolean bloqueoTexto2,
			@RequestParam(value="bloqueoFecha1",required=false) Boolean bloqueoFecha1,
			@RequestParam(value="bloqueoFecha2",required=false) Boolean bloqueoFecha2){
		
		List<Referencia> referencias = (List<Referencia>) session.getAttribute("referencias_lote");
		List<Referencia> referenciasEliminadas = (List<Referencia>) session.getAttribute("referencias_eliminadas");
		if(referenciasEliminadas==null)
			referenciasEliminadas=new ArrayList<Referencia>();
		
		for(Referencia candidato:referencias){
			if(candidato.getObj_hash().equals(objectHash)){
				if(candidato!=null && candidato.getId()!=null)
					referenciasEliminadas.add(candidato);
				referencias.remove(candidato);
				break;
			}
		}
		inicializarFormularioReferenciaContenedor(atributos, session, indiceIndividual);

		atributos.put("hacerFocusEn", "codigoClasificacionDocumental");
		atributos.put("bloqueoClasificacionDocumental", bloqueoClasificacionDocumental);
		atributos.put("bloqueoTipoElementoContenedor",bloqueoTipoElementoContenedor);
		atributos.put("bloqueoContenedor", bloqueoContenedor);
		atributos.put("bloqueoTexto1", bloqueoTexto1);
		atributos.put("bloqueoTexto2", bloqueoTexto2);
		atributos.put("bloqueoNumero1", bloqueoNumero1);
		atributos.put("bloqueoNumero2", bloqueoNumero2);
		atributos.put("bloqueoFecha1", bloqueoFecha1);
		atributos.put("bloqueoFecha2", bloqueoFecha2);
		session.setAttribute("referencias_eliminadas", referenciasEliminadas);

		return "formularioReferencia";
	}
	
	@RequestMapping(value="/modificarReferencia.html")
	public String modificarReferencia(Map<String,Object> atributos, HttpSession session,
			@RequestParam("obj_hash") String objectHash,
			@RequestParam(value="bloqueoClasificacionDocumental",required=false) Boolean bloqueoClasificacionDocumental,
			@RequestParam(value="bloqueoTipoElementoContenedor",required=false) Boolean bloqueoTipoElementoContenedor,
			@RequestParam(value="bloqueoContenedor",required=false) Boolean bloqueoContenedor,
			@RequestParam(value="bloqueoNumero1",required=false) Boolean bloqueoNumero1,
			@RequestParam(value="bloqueoNumero2",required=false) Boolean bloqueoNumero2,
			@RequestParam(value="bloqueoTexto1",required=false) Boolean bloqueoTexto1,
			@RequestParam(value="bloqueoTexto2",required=false) Boolean bloqueoTexto2,
			@RequestParam(value="bloqueoFecha1",required=false) Boolean bloqueoFecha1,
			@RequestParam(value="bloqueoFecha2",required=false) Boolean bloqueoFecha2){
		
		List<Referencia> referencias = (List<Referencia>) session.getAttribute("referencias_lote");
		Referencia referencia=null;
		for(Referencia candidato:referencias){
			if(candidato.getObj_hash().equals(objectHash)){
				referencia=candidato;
				break;
			}
		}
		if(referencia!=null){
			referencia.setContenedor(referencia.getElementoContenedor());
		}
		atributos.put("referencia", referencia);//ponemos el eliminado por si le interesa cambiarle algo para guardar de nuevo
		
		if(referencia.getIndiceIndividual()){
			atributos.put("fijarContenedor", true);
		}
		atributos.put("objectHash", objectHash);
		//atributos.put("hacerFocusEn", "codigoClasificacionDocumental");
		atributos.put("bloqueoClasificacionDocumental", bloqueoClasificacionDocumental);
		atributos.put("bloqueoTipoElementoContenedor",bloqueoTipoElementoContenedor);
		atributos.put("bloqueoContenedor", bloqueoContenedor);
		atributos.put("bloqueoTexto1", bloqueoTexto1);
		atributos.put("bloqueoTexto2", bloqueoTexto2);
		atributos.put("bloqueoNumero1", bloqueoNumero1);
		atributos.put("bloqueoNumero2", bloqueoNumero2);
		atributos.put("bloqueoFecha1", bloqueoFecha1);
		atributos.put("bloqueoFecha2", bloqueoFecha2);

		return "formularioReferencia";
	}
	
	
	@RequestMapping(value="/refrescarFormReferencia.html",method=RequestMethod.POST)
	public String refrescarFormReferencia(Map<String,Object> atributos, HttpSession session,
			@ModelAttribute(value="referenciaFormulario") Referencia referencia,
			@RequestParam(value="obj_hash",required=false) String objectHash,
			@RequestParam(value="codigoCliente")String codigoCliente,
			@RequestParam(value="codigoClasificacionDocumental")Integer codigoClasificacionDocumental,
			@RequestParam(value="codigoContenedor")String codigoContenedor,
			@RequestParam(value="codigoElemento",required=false)String codigoElemento,
			@RequestParam(value="bloqueoClasificacionDocumental",required=false) Boolean bloqueoClasificacionDocumental,
			@RequestParam(value="bloqueoTipoElementoContenedor",required=false) Boolean bloqueoTipoElementoContenedor,
			@RequestParam(value="bloqueoContenedor",required=false) Boolean bloqueoContenedor,
			@RequestParam(value="bloqueoNumero1",required=false) Boolean bloqueoNumero1,
			@RequestParam(value="bloqueoNumero1Hijo",required=false) Boolean bloqueoNumero1Hijo,
			@RequestParam(value="numero1Hijo",required=false) Long numero1Hijo,
			@RequestParam(value="bloqueoNumero2",required=false) Boolean bloqueoNumero2,
			@RequestParam(value="bloqueoNumero2Hijo",required=false) Boolean bloqueoNumero2Hijo,
			@RequestParam(value="numero2Hijo",required=false) Long numero2Hijo,
			@RequestParam(value="bloqueoTexto1",required=false) Boolean bloqueoTexto1,
			@RequestParam(value="bloqueoTexto1Hijo",required=false) Boolean bloqueoTexto1Hijo,
			@RequestParam(value="texto1Hijo",required=false) String texto1Hijo,
			@RequestParam(value="bloqueoTexto2",required=false) Boolean bloqueoTexto2,
			@RequestParam(value="bloqueoTexto2Hijo",required=false) Boolean bloqueoTexto2Hijo,
			@RequestParam(value="texto2Hijo",required=false) String texto2Hijo,
			@RequestParam(value="bloqueoFecha1",required=false) Boolean bloqueoFecha1,
			@RequestParam(value="bloqueoFecha2",required=false) Boolean bloqueoFecha2,
			@RequestParam(value="incrementoElemento",required=false) Boolean incrementoElemento,
			@RequestParam(value="codigoContenedorComparar")String codigoContenedorComparar,
			@RequestParam(value="cerrarCaja",required=false) Boolean cerrarCaja){
		
		bloqueoContenedor = true;
		procesarFormularioReferencia(referencia, codigoCliente, codigoClasificacionDocumental, codigoContenedor, codigoElemento,null);
		
		if(referencia.getIndiceIndividual() && referencia.getClasificacionDocumental()!=null && !referencia.getClasificacionDocumental().getIndiceIndividual()){
			referencia.setClasificacionDocumental(null);
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioLoteReferencia.notificacion.clasificacionDocumentalNoSoportaIndiceIndividual", null);
			avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
		}
		if(!referencia.getIndiceIndividual() && referencia.getClasificacionDocumental()!=null && !referencia.getClasificacionDocumental().getIndiceGrupal()){
			referencia.setClasificacionDocumental(null);
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeClienteReg = new ScreenMessageImp("formularioLoteReferencia.notificacion.clasificacionDocumentalNoSoportaIndiceGrupal", null);
			avisos.add(mensajeClienteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
		}
		if(referencia.getContenedor()!=null){
			referencia.getContenedor().setCerrado(cerrarCaja);
			elementoService.actualizarElemento(referencia.getContenedor());
		}
		
		atributos.put("referencia", referencia);
		if(referencia.getIndiceIndividual() && !((List<Referencia>) session.getAttribute("referencias_lote")).isEmpty()){
			atributos.put("fijarContenedor", true);
		}
		
		//Bloque que controla donde hara el foco de acuerdo a los campos bloqueados al regresar a la pagina
//		String hacerFoco = "prefijoCodigoTipoElemento";
//		if(bloqueoTipoElementoContenedor!=null && bloqueoTipoElementoContenedor){
//			hacerFoco = "codigoContenedor";
//			if(bloqueoContenedor!= null && bloqueoContenedor){	
//				hacerFoco = "codigoElemento";
//				if(incrementoElemento!= null && incrementoElemento){
//					hacerFoco = "codigoClasificacionDocumental";
//					if(bloqueoClasificacionDocumental!= null && bloqueoClasificacionDocumental){
//						hacerFoco = "numero1";
//						if(bloqueoNumero1!= null && bloqueoNumero1){
//							hacerFoco = "texto1";
//							if(bloqueoTexto1!= null && bloqueoTexto1){
//								hacerFoco = "numero2";
//								if(bloqueoNumero2!=null && bloqueoNumero2)
//									hacerFoco = "texto2";
//							}
//						}
//					}
//				}
//			}
//		}
		
		//Bloque que controla donde hara el foco de acuerdo a los campos bloqueados al regresar a la pagina
				String hacerFoco = "prefijoCodigoTipoElemento";
				if(bloqueoTipoElementoContenedor!= null && !bloqueoTipoElementoContenedor){
					hacerFoco = "prefijoCodigoTipoElemento";	
				}else{
					if(bloqueoContenedor!= null && !bloqueoContenedor){	
						hacerFoco = "codigoContenedor";
					}else{
						if(incrementoElemento!=null && !incrementoElemento){	
							hacerFoco = "codigoElemento";
						}else{
							if(bloqueoClasificacionDocumental!=null && !bloqueoClasificacionDocumental){
								hacerFoco = "codigoClasificacionDocumental";
							}else{
								if(bloqueoNumero1!=null && !bloqueoNumero1){
									hacerFoco = "numero1";
								}else{
									if(bloqueoNumero2!=null && !bloqueoNumero2){
										hacerFoco = "numero2";
									}else{
										if(bloqueoFecha1!=null && !bloqueoFecha1){
											hacerFoco = "fecha1";
										}else{
											if(bloqueoFecha2!=null && !bloqueoFecha2){
												hacerFoco = "fecha2";
											}else{
												if(bloqueoTexto1!=null && !bloqueoTexto1){
													hacerFoco = "texto1";
												}else{
													if(bloqueoTexto2!=null && !bloqueoTexto2){
														hacerFoco = "texto2";
													}else{
														hacerFoco = "descripcion";
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
		
		if(bloqueoNumero1Hijo!=null && bloqueoNumero1Hijo){
			bloqueoNumero1 = bloqueoNumero1Hijo;
			if(numero1Hijo!=null)
				referencia.setNumero1(numero1Hijo);
		}
		if(bloqueoTexto1Hijo!=null && bloqueoTexto1Hijo){
			bloqueoTexto1 = bloqueoTexto1Hijo;
			if(texto1Hijo!=null)
				referencia.setTexto1(texto1Hijo);
		}
		if(bloqueoNumero2Hijo!=null && bloqueoNumero2Hijo){
			bloqueoNumero2 = bloqueoNumero2Hijo;
			if(numero2Hijo!=null)
				referencia.setNumero2(numero2Hijo);
		}
		if(bloqueoTexto2Hijo!=null && bloqueoTexto2Hijo){
			bloqueoTexto2 = bloqueoTexto2Hijo;
			if(texto2Hijo!=null)
				referencia.setTexto2(texto2Hijo);
		}
		
		atributos.put("objectHash", objectHash);
		atributos.put("bloqueoClasificacionDocumental", bloqueoClasificacionDocumental);
		atributos.put("bloqueoTipoElementoContenedor",bloqueoTipoElementoContenedor);
		atributos.put("bloqueoContenedor", bloqueoContenedor);
		atributos.put("bloqueoTexto1", bloqueoTexto1);
		atributos.put("bloqueoTexto2", bloqueoTexto2);
		atributos.put("bloqueoNumero1", bloqueoNumero1);
		atributos.put("bloqueoNumero2", bloqueoNumero2);
		atributos.put("bloqueoFecha1", bloqueoFecha1);
		atributos.put("bloqueoFecha2", bloqueoFecha2);
		atributos.put("incrementoElemento", incrementoElemento);
		atributos.put("hacerFocusEn", hacerFoco);
		atributos.put("codigoContenedorComparar", codigoContenedorComparar);
		
		return "formularioReferencia";
	}

	@RequestMapping(value="/agregarRangoReferencia.html",method=RequestMethod.POST)
	public String agregarRangoReferencia(Map<String,Object> atributos, HttpSession session,
			@ModelAttribute(value="referenciaFormulario") Referencia referencia,
			BindingResult result,
			@RequestParam(value="obj_hash",required=false) String objectHash,
			@RequestParam(value="idLoteReferencia",required=false) Long idLoteReferencia,
			@RequestParam(value="codigoCliente") String codigoCliente,
			@RequestParam(value="codigoClasificacionDocumental") Integer codigoClasificacionDocumental,
			@RequestParam(value="codigoContenedor") String codigoContenedor,
			@RequestParam(value="codigoElemento",required=false) String codigoElemento,
			@RequestParam(value="codigoContenedorComparar")String codigoContenedorComparar,
			@RequestParam(value="bloqueoClasificacionDocumental",required=false) Boolean bloqueoClasificacionDocumental,
			@RequestParam(value="bloqueoTipoElementoContenedor",required=false) Boolean bloqueoTipoElementoContenedor,
			@RequestParam(value="bloqueoContenedor",required=false) Boolean bloqueoContenedor,
			@RequestParam(value="bloqueoNumero1",required=false) Boolean bloqueoNumero1,
			@RequestParam(value="bloqueoNumero2",required=false) Boolean bloqueoNumero2,
			@RequestParam(value="bloqueoTexto1",required=false) Boolean bloqueoTexto1,
			@RequestParam(value="bloqueoTexto2",required=false) Boolean bloqueoTexto2,
			@RequestParam(value="bloqueoFecha1",required=false) Boolean bloqueoFecha1,
			@RequestParam(value="bloqueoFecha2",required=false) Boolean bloqueoFecha2,
			@RequestParam(value="porRango",required=false) Boolean porRango,
			@RequestParam(value="cerrarCaja",required=false) Boolean cerrarCaja){
		
		bloqueoContenedor = true;
		procesarFormularioReferencia(referencia, codigoCliente, codigoClasificacionDocumental, codigoContenedor, codigoElemento,null);
		
		if(!result.hasErrors()){
			refValidator.validate(referencia, result);
		}
		
		if(!result.hasErrors()){
			
			List<Referencia> referencias = (List<Referencia>) session.getAttribute("referencias_lote");
			List<Elemento> listaElementos = new ArrayList<Elemento>();
			Elemento elemento = new Elemento();
			elemento.setCodigoDesde(referencia.getCodigoElementoDesde());
			elemento.setCodigoHasta(referencia.getCodigoElementoHasta());
			Long codigoDesde =  new Long(referencia.getCodigoElementoDesde().trim());
			Long codigoHasta =  new Long(referencia.getCodigoElementoHasta().trim());
			Long extensionRango = codigoHasta.longValue() - codigoDesde.longValue() + 1L;
			//Se establecio con Luis Borello el numero limite de etiquetas permitidas por rango
			Integer limite = 500;
			
			if(extensionRango<limite)
				listaElementos = elementoService.listarElementoFiltradas(elemento, obtenerClienteAspUser());
			
			//Se pregunta si el rango esta vacio, si el rango esta incompleto o si el rango pasa las 500 etiquetas
			if(listaElementos==null || listaElementos.size()<=0 
					|| listaElementos.size()<extensionRango || extensionRango>limite){
				//Genero las notificaciones 
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				ScreenMessage mensajeRango;
				if(extensionRango>limite)
					mensajeRango = new ScreenMessageImp("formularioLoteReferencia.error.rangoExcedido", Arrays.asList(new String[]{limite.toString()+" etiquetas."}));
				else if(listaElementos==null || listaElementos.size()<=0)
					mensajeRango = new ScreenMessageImp("formularioLoteReferencia.error.rangoVacio", null);
				else
					mensajeRango = new ScreenMessageImp("formularioLoteReferencia.error.rangoIncompleto", null);
					
				avisos.add(mensajeRango); //agrego el mensaje a la coleccion
				atributos.put("referencia", referencia);
				atributos.put("objectHash", objectHash);
				atributos.put("errores", false);
				atributos.put("hayAvisos", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
				atributos.put("porRango", referencia.getPorRango());
				return "formularioReferencia";//Salir y avisar que no hay elementos en ese rango
			}
			
			//Traigo con los codigos del rango ingresado los elementos para sacar su id
			Elemento elementoDesde = elementoService.getByCodigo(referencia.getCodigoElementoDesde(), obtenerClienteAspUser());
			Elemento elementoHasta = elementoService.getByCodigo(referencia.getCodigoElementoHasta(), obtenerClienteAspUser());
			//Con los id de los elementos busco si existe alguna referencia en ese rango
			List<Referencia> listaReferencias = referenciaService.getListaByRangoElemento(elementoDesde.getId(), elementoHasta.getId());
			
			if(listaReferencias!=null && listaReferencias.size()>0){
				//Genero las notificaciones 
				List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
				ScreenMessage existentesEnRango = new ScreenMessageImp("formularioLoteReferencia.error.referenciasExistentesEnRango", null);
				avisos.add(existentesEnRango); //agrego el mensaje a la coleccion
				atributos.put("referencia", referencia);
				atributos.put("objectHash", objectHash);
				atributos.put("errores", false);
				atributos.put("hayAvisos", false);
				atributos.put("hayAvisosNeg", true);
				atributos.put("avisos", avisos);
				atributos.put("porRango", referencia.getPorRango());
				return "formularioReferencia";//Error y salir y avisar que existen referencias ya en ese rango
			}
			
			
			
			if(referencia.getContenedor()!=null){
				referencia.getContenedor().setCerrado(cerrarCaja);
				elementoService.actualizarElemento(referencia.getContenedor());
			}
			
			for(Elemento nuevoElemento: listaElementos){
				Referencia nuevaReferencia = new Referencia();
				//Le seteo a la referencia la clasificacion, el elemento y el contenedor
				procesarFormularioReferencia(nuevaReferencia, codigoCliente, codigoClasificacionDocumental,codigoContenedor, nuevoElemento.getCodigo(),idLoteReferencia!=null?idLoteReferencia:0);
				nuevaReferencia.setIndiceIndividual(true);
				boolean repetido = false;
				if(nuevaReferencia.getIndiceIndividual()){
					for(Referencia ref:referencias){
						if(ref.getIndiceIndividual()){
							if(ref.getElemento().getId().equals(nuevaReferencia.getElemento().getId())){
								if(objectHash==null || !objectHash.equals(ref.getObj_hash())){
									result.rejectValue("elemento", "repetido");
									repetido = true;
									break;
								}
							}
						}
					}
				}			
				if(!repetido)
					referencias.add(nuevaReferencia);
				else{
					atributos.put("referencia", referencia);
					atributos.put("objectHash", objectHash);
					atributos.put("errores", true);
					atributos.put("result", result);
					atributos.put("hayAvisos", false);
					atributos.remove("avisos");
					break;
				}
			}
			
		}
		else
		{
			atributos.put("referencia", referencia);
			atributos.put("objectHash", objectHash);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");	
		}
		
		atributos.put("porRango", referencia.getPorRango());
		
		return "formularioReferencia";
	}
	
	
	@RequestMapping(
			value="/imprimirLoteReferencia.html",
			method = RequestMethod.GET
		)

	public void imprimirLoteReferencia(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="id",required=true) Long id,
			HttpServletResponse response) {
		try{
			LoteReferencia loteReferencia = loteReferenciaService.obtenerPorId(id);
			loteReferencia.setReferencias(referenciaService.listarReferenciaPorLote(loteReferencia, obtenerClienteAspUser()));
			if(!loteReferencia.getReferencias().isEmpty()){
				loteReferencia.setIndiceIndividual(loteReferencia.getReferencias().iterator().next().getIndiceIndividual());
			}
			//Long codigo = loteReferencia.getId();
			Long codigo = loteReferencia.getCodigo();
			String fechaRegistro = loteReferencia.getFechaRegistroStr();
			String empresa = loteReferencia.getEmpresa().getNombreRazonSocial();
			String sucursal = loteReferencia.getSucursal().getDescripcion();
			String cliente = loteReferencia.getClienteEmp().getRazonSocialONombreYApellido();
			ArrayList<ReferenciaReporte> referencias=new ArrayList<ReferenciaReporte>();

			ReferenciaHistorico refHistorico = referenciaHistoricoService.obtenerReferenciaHistorico(loteReferencia.getReferencias().iterator().next());
			
			
			for(Referencia ref : loteReferencia.getReferencias()){
				
				ReferenciaReporte referenciaReporte=new ReferenciaReporte();
				
				if(ref.getClasificacionDocumental()!=null)
					referenciaReporte.setClasificacionDocumental(ref.getClasificacionDocumental().getNombre());
				if(ref.getElementoContenido()!=null)
					referenciaReporte.setElemento(ref.getElementoContenido().getCodigo());
				referenciaReporte.setFecha1(ref.getFecha1Str());
				referenciaReporte.setFecha2(ref.getFecha2Str());
				referenciaReporte.setIndiceIndividual(ref.getIndiceIndividualStr());
				referenciaReporte.setNumero1(ref.getNumero1Str());
				referenciaReporte.setNumero2(ref.getNumero2Str());
				referenciaReporte.setTexto1(ref.getTexto1());
				referenciaReporte.setTexto2(ref.getTexto2());
				if(refHistorico!=null)
					referenciaReporte.setNombreCliente(refHistorico.getUsuario().getPersona().toString());
				if(ref.getElementoContenedor()!=null)
					referenciaReporte.setContenedor(ref.getElementoContenedor().getCodigo());
				referencias.add(referenciaReporte);
			}
			JasperReport jasperReport = JasperCompileManager.compileReport(session.getServletContext().getRealPath(Constants.PATH_JASPER)+"/informeLoteReferencia.jrxml");
			Map<String,Object> parametros=new HashMap<String,Object>();	
			parametros.put("codigo", codigo);
			parametros.put("fechaRegistro", fechaRegistro);
			parametros.put("empresa", empresa);
			parametros.put("sucursal", sucursal);
			parametros.put("cliente", cliente);
			parametros.put("cantidadRef", referencias.size());
			
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=lote_referencia.pdf");
			
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(referencias);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros,dataSource);
			JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
	}		
	
	private void inicializarFormularioReferenciaContenedor(Map<String, Object> atributos,
			HttpSession session, Boolean indiceIndividual) {
		Referencia nuevaReferencia=new Referencia();
		nuevaReferencia.setIndiceIndividual(indiceIndividual);
		List<Referencia> referencias = (List<Referencia>) session.getAttribute("referencias_lote");
		if(indiceIndividual && !referencias.isEmpty()){
			nuevaReferencia.setContenedor(referencias.iterator().next().getElementoContenedor());
			atributos.put("fijarContenedor", true);
			if(nuevaReferencia.getContenedor()!=null)
				atributos.put("codigoContenedorComparar", nuevaReferencia.getContenedor().getCodigo());
		}
		atributos.put("referencia", nuevaReferencia);
	}
	
	
	
	private void procesarFormularioReferencia(Referencia referencia,String codigoCliente, Integer codigoClasificacionDocumental,String codigoContenedor,String codigoElemento,Long idLoteReferencia){
		if(codigoClasificacionDocumental!=null && !codigoClasificacionDocumental.equals("")){
			referencia.setClasificacionDocumental(clasificacionDocumentalService.getClasificacionByCodigo(codigoClasificacionDocumental, codigoCliente, obtenerClienteAspUser(), "I"));
		}
		//el oren es importante, setear primero el elemento y después el contenedor
		if(codigoElemento!=null && !codigoElemento.equals("")){
			referencia.setElemento(elementoService.getElementoByCodigo(codigoElemento,codigoCliente,obtenerClienteAspUser(),null,idLoteReferencia));
		}
		if(codigoContenedor!=null && !codigoContenedor.equals("")){
			Elemento contenedor = elementoService.getContenedorByCodigo(codigoContenedor,codigoCliente,null, obtenerClienteAspUser(),null);
			referencia.setContenedor(contenedor);
			if(contenedor!=null)
				referencia.setPrefijoCodigoTipoElemento(contenedor.getTipoElemento().getPrefijoCodigo());
		}
	}
	
	private void completarCampos(Referencia referencia) {
		ClasificacionDocumental clasificacionDocumental = referencia.getClasificacionDocumental();
		if(referencia.getIndiceIndividual()){
			if(clasificacionDocumental.getIndividualFecha1Seleccionado() && !clasificacionDocumental.getIndividualFecha2Seleccionado()){
				referencia.setFecha2(referencia.getFecha1());
			}
			if(clasificacionDocumental.getIndividualNumero1Seleccionado() && !clasificacionDocumental.getIndividualNumero2Seleccionado()){
				referencia.setNumero2(referencia.getNumero1());
			}
			if(clasificacionDocumental.getIndividualTexto1Seleccionado() && !clasificacionDocumental.getIndividualTexto2Seleccionado()){
				referencia.setTexto2(referencia.getTexto1());
			}
		}
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
	
	/**
	 * 
	 * @author Luis
	 *
	 */
	public class ReferenciaReporte{
		
		private LoteReferencia loteReferencia;
		private String clasificacionDocumental;
		private String elemento;
		private String indiceIndividual;
		private String numero1;
		private String numero2;
		private String fecha1;
		private String fecha2;
		private String texto1;
		private String texto2;
		private String contenedor;
		private String nombreCliente;
		private Long codigoLote;
		private String accion;
		private String fecha;
		
		public LoteReferencia getLoteReferencia() {
			return loteReferencia;
		}

		public void setLoteReferencia(LoteReferencia loteReferencia) {
			this.loteReferencia = loteReferencia;
		}

		public String getClasificacionDocumental() {
			return clasificacionDocumental;
		}

		public void setClasificacionDocumental(
				String clasificacionDocumental) {
			this.clasificacionDocumental = clasificacionDocumental;
		}

		public String getElemento() {
			return elemento;
		}

		public void setElemento(String elemento) {
			this.elemento = elemento;
		}

		public String getIndiceIndividual() {
			return indiceIndividual;
		}

		public void setIndiceIndividual(String indiceIndividual) {
			this.indiceIndividual = indiceIndividual;
		}

		public String getNumero1() {
			return numero1;
		}

		public void setNumero1(String numero1) {
			this.numero1 = numero1;
		}

		public String getNumero2() {
			return numero2;
		}

		public void setNumero2(String numero2) {
			this.numero2 = numero2;
		}

		public String getFecha1() {
			return fecha1;
		}

		public void setFecha1(String fecha1) {
			this.fecha1 = fecha1;
		}

		public String getFecha2() {
			return fecha2;
		}

		public void setFecha2(String fecha2) {
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

		public String getContenedor() {
			return contenedor;
		}

		public void setContenedor(String contenedor) {
			this.contenedor = contenedor;
		}

		public String getNombreCliente() {
			return nombreCliente;
		}

		public void setNombreCliente(String nombreCliente) {
			this.nombreCliente = nombreCliente;
		}

		public Long getCodigoLote() {
			return codigoLote;
		}

		public void setCodigoLote(Long codigoLote) {
			this.codigoLote = codigoLote;
		}

		public String getAccion() {
			return accion;
		}

		public void setAccion(String accion) {
			this.accion = accion;
		}

		public String getFecha() {
			return fecha;
		}

		public void setFecha(String fecha) {
			this.fecha = fecha;
		}

	}
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	//TODO: Informar cuando se excede el tamaño maximo de filas en el excel
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
