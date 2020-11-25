package com.dividato.configuracionGeneral.controladores;

import static com.security.recursos.Configuracion.formatoFechaFormularios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

import com.dividato.configuracionGeneral.validadores.PosicionBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PosicionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SeccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.constants.Constants;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Seccion;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.Constantes;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;

/**
 * Controlador que se utiliza para los servicios asociados a la lista de
 * Posicion. Observar la anotación @Controller (que hereda de @Component) de
 * SPRING. Esta anotación le indica a SPRING que esta clase es de tipo
 * controlador. A continuación está la anotación @RequestMapping que indica
 * cuales son las URL que mapea este controlador.
 * 
 * @author Gonzalo Noriega
 * 
 */
@Controller
@RequestMapping(value = { "/iniciarPosicion.html", "/mostrarPosicion.html",
		"/filtrarPosicion.html", "/mostrarPosicionesSinFiltrar.html",
		"/cambiarEstadoPosiciones.html", "/exportar.html" })
public class ListaPosicionesController {

	private PosicionService posicionService;
	private PosicionBusquedaValidator validator;
	private SeccionService seccionService;
	private DepositoService depositoService;
	private EmpresaService empresaService;
	private ElementoService elementoService;
	private SucursalService sucursalService;

	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}

	@Autowired
	public void setPosicionService(PosicionService posicionService) {
		this.posicionService = posicionService;
	}

	@Autowired
	public void setValidator(PosicionBusquedaValidator validator) {
		this.validator = validator;
	}

	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}

	/**
	 * Setea el servicio de Depositos. Observar la anotación @Autowired, que le
	 * indica a Spring que debe ejecutar este método e inyectarle un objeto
	 * PosicionService. La clase DepositoServiceImp implementa Deposito y está
	 * anotada con @Component, entonces Spring sabe que puede instanciar esta
	 * clase y pasársela a este método.
	 * 
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
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}

	@RequestMapping(value = "/iniciarPosicion.html", method = RequestMethod.GET)
	public String iniciarPosicion(HttpSession session,
			Map<String, Object> atributos) {
		session.removeAttribute("posicionBusqueda");
		session.removeAttribute("posicionesSession");
		atributos.remove("posiciones");
		session.removeAttribute("tablaPaginada");
		return "redirect:mostrarPosicionesSinFiltrar.html";
	}

	@RequestMapping(value = "/mostrarPosicionesSinFiltrar.html", method = RequestMethod.GET)
	public String mostrarPosicionesSinFiltrar(
			HttpSession session,
			Map<String, Object> atributos,
			@RequestParam(value = "val", required = false) String valSeccion,
			@RequestParam(value = "val", required = false) String valDeposito,
			@RequestParam(value = "val", required = false) String valSucursal,
			@RequestParam(value = "val", required = false) String valEmpresa,
			@RequestParam(value = "empresaCodigo", required = false) String empresaCodigo,
			@RequestParam(value = "sucursalCodigo", required = false) String sucursalCodigo,
			@RequestParam(value = "depositoCodigo", required = false) String depositoCodigo) {

		// buscamos en la base de datos y lo subimos a request.
		@SuppressWarnings("unchecked")
		List<Posicion> posiciones = (List<Posicion>) session
				.getAttribute("posicionesSession");
		if (posiciones == null) {
			posiciones = new ArrayList<Posicion>();
		}

		Posicion posicion = (Posicion) session.getAttribute("posicionBusqueda");
		if (posicion == null) {
			posicion = new Posicion();
			posicion.setCodigoEmpresa(empresaCodigo);
			posicion.setCodigoSucursal(sucursalCodigo);
		}

		session.setAttribute("posicionesSession", posiciones);
		atributos.put("posiciones", posiciones);
		// si no hay errores
		if (atributos.get("errores") == null)
			atributos.put("errores", false);

		// seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("size", Integer.valueOf(0));

		definirPopupEmpresa(atributos, valEmpresa);
		definirPopupSucursal(atributos, valSucursal, empresaCodigo);
		definirPopupDepositos(atributos, valDeposito, sucursalCodigo);
		definirPopupSecciones(atributos, valSeccion, depositoCodigo);
		atributos.put("pagesize", "40");
		// hacemos el forward
		return "consultaPosicion";
	}

	/**
	 * Observar la anotación @RequestMapping de SPRING. Todos los parámetros son
	 * inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de Posicion y subirla a request.
	 * 
	 * @param atributos
	 *            son los atributos del request
	 * @return "consultaPosicion" la vista que debe mostrarse (ver
	 *         dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(value = "/mostrarPosicion.html", method = RequestMethod.GET)
	public String mostrarPosicion(
			HttpSession session,
			Map<String, Object> atributos,
			@RequestParam(value = "val", required = false) String valSeccion,
			@RequestParam(value = "val", required = false) String valDeposito,
			@RequestParam(value = "val", required = false) String valSucursal,
			@RequestParam(value = "val", required = false) String valEmpresa,
			@RequestParam(value = "empresaCodigo", required = false) String empresaCodigo,
			@RequestParam(value = "sucursalCodigo", required = false) String sucursalCodigo,
			@RequestParam(value = "depositoCodigo", required = false) String depositoCodigo,
			HttpServletRequest request) {
		// buscamos en la base de datos y lo subimos a request.
		List<Posicion> posicions = null;

		Posicion posicion = (Posicion) session.getAttribute("posicionBusqueda");

		if (posicion != null) {

			// cuenta la cantidad de resultados
			Integer size = posicionService.contarPosicionFiltradas(posicion,
					obtenerClienteAspUser());
			atributos.put("size", size);

			Integer pagesize = null;
			String pagesizeStr = "";
			try {
				pagesizeStr = (String) session.getAttribute("pagesize");
			} catch (ClassCastException e) {
				pagesizeStr = session.getAttribute("pagesize").toString();
			}
			if (pagesizeStr.equalsIgnoreCase("Todos")) {
				pagesize = size;

			} else {
				pagesize = Integer.valueOf(pagesizeStr);
			}
			if (pagesize == null)
				pagesize = 40;

			atributos.put("pagesize", pagesize);
			session.setAttribute("pagesize", pagesize);

			// if(elemento!=null && elemento.getCodigoContenedor()!=null){
			// elemento.setCodigoElemento(elemento.getCodigoContenedor());
			// }
			// paginacion y orden de resultados de displayTag
			posicion.setTamañoPagina(pagesize);
			String nPaginaStr = request.getParameter((new ParamEncoder(
					"posicion")
					.encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if (nPaginaStr == null) {
				nPaginaStr = (String) atributos
						.get((new ParamEncoder("posicion")
								.encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			String fieldOrder = request.getParameter(new ParamEncoder(
					"posicion")
					.encodeParameterName(TableTagParameters.PARAMETER_SORT));
			posicion.setFieldOrder(fieldOrder);
			String sortOrder = request
					.getParameter(new ParamEncoder("posicion")
							.encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			posicion.setSortOrder(sortOrder);
			Integer nPagina = 1;
			if (nPaginaStr != null) {
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			posicion.setNumeroPagina(nPagina);

			// Se busca en la base de datos las posiciones con los filtros de
			// paginacion agregados a la posicion
			posicions = (List<Posicion>) posicionService
					.listarPosicionFiltradas(posicion, obtenerClienteAspUser());

			if (posicions != null && posicions.size() > 0) {

				List<Elemento> listaElementosPosicionados = elementoService
						.obtenerElementosDePosiciones(posicions);
				if (listaElementosPosicionados != null
						&& listaElementosPosicionados.size() > 0) {
					for (Elemento elemento : listaElementosPosicionados) {
						for (int i = 0; i < posicions.size(); i++) {
							if (posicions.get(i).getId()
									.equals(elemento.getPosicion().getId())) {
								posicions.get(i).setElementoAsignado(elemento);
							}
						}
					}
				}
			}

			session.setAttribute("posicionBusqueda", posicion);

		} else if (posicion == null) {
			posicion = new Posicion();
			// posicion.setCodigoDesdeEstante("0000");
			// posicion.setCodigoHastaEstante("9999");
			// session.setAttribute("posicionBusqueda", posicion);
		}

		atributos.put("posiciones", posicions);
		session.setAttribute("posiciones", posicions);

		atributos.put("clienteId", obtenerClienteAspUser().getId());

		// si no hay errores
		if (atributos.get("errores") == null)
			atributos.put("errores", false);

		definirPopupEmpresa(atributos, valEmpresa);
		definirPopupSucursal(atributos, valSucursal, empresaCodigo);
		definirPopupDepositos(atributos, valDeposito, sucursalCodigo);
		definirPopupSecciones(atributos, valSeccion, depositoCodigo);

		// hacemos el forward
		return "consultaPosicion";
	}

	@RequestMapping(value = "/filtrarPosicion.html", method = RequestMethod.POST)
	public String filtrarPosicion(
			@ModelAttribute("posicionBusqueda") Posicion posicion,
			BindingResult result, HttpSession session,
			Map<String, Object> atributos, HttpServletRequest request,
			@RequestParam(value = "pagesize", required = false) String pagesize) {
		session.setAttribute("pagesize", pagesize);
		atributos.put("pagesize", pagesize);
		// buscamos en la base de datos y lo subimos a request.
		this.validator.validate(posicion, result);
		if (!result.hasErrors()) {
			session.setAttribute("posicionBusqueda", posicion);
			atributos.put("errores", false);
			atributos.remove("result");
		} else {
			atributos.put("errores", true);
			atributos.put("result", result);
		}
		return mostrarPosicion(session, atributos, null, null, null, null,
				null, null, null, request);
	}

	@RequestMapping(value = "/cambiarEstadoPosiciones.html", method = RequestMethod.POST)
	public String cambiarEstadoPosiciones(
			HttpSession session,
			@RequestParam(value = "selectedSel", required = false) String selectedSel,
			@RequestParam(value = "estado", required = false) String estadoSel,
			Map<String, Object> atributos, HttpServletRequest request) {
		
		List<Posicion> posiciones = new ArrayList<Posicion>();
		List<Posicion> posicionesSel = new ArrayList<Posicion>();
		ScreenMessage mensaje = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Boolean hayAvisos = false;
		Boolean hayAvisosNeg = false;
		Boolean commit = false;
		boolean existenOcupadas = false;
		String selIds[] = null;

		if (selectedSel != null && !"".equals(selectedSel)) 
		{
			// Obtenemos las posiciones por id's
			selIds = selectedSel.split(",");
			if (selIds != null && selIds.length > 0) 
			{
				
				posiciones = (List<Posicion>) session.getAttribute("posiciones");
				
				for (String idPosicion : selIds) {
//					Posicion posicion = posicionService.obtenerPorId(Long.valueOf(idPosicion));
//					if (posicion != null)
//						if(!posicion.getEstado().equalsIgnoreCase("OCUPADA"))
//							posiciones.add(posicion);
//						else
//							existenOcupadas=true;
					
					for(Posicion pos : posiciones){
						if(pos.getId().longValue()==Long.valueOf(idPosicion).longValue()){
							posicionesSel.add(pos);
							break;
						}
					}	
				}
								
				// recorremos las posiciones seleccionadas y cambiamos el estado
				for (Posicion update : posicionesSel) 
				{
					if(update.getEstado().equalsIgnoreCase(Constantes.POSICION_ESTADO_OCUPADA) && update.getElementoAsignado()!=null)
						existenOcupadas=true;
					else
						update.setEstado(estadoSel);
				}

			}
			
			
			commit = posicionService.actualizarPosicionList(posicionesSel);
			if (commit) 
			{
				if(existenOcupadas)
					mensaje = new ScreenMessageImp("formularioPosicion.notificacion.posicionRegistradoConOcupadas",null);
				else	
					mensaje = new ScreenMessageImp("formularioPosicion.notificacion.posicionRegistrado",null);
				hayAvisos = true;
			} 
			else
			{
				mensaje = new ScreenMessageImp("error.resumirMovimientoDataBase", null);
				hayAvisosNeg = true;
			}
		} 
		else 
		{
			mensaje = new ScreenMessageImp("notif.posicion.seleccionPosicion",null);
			hayAvisosNeg = true;
		}

		avisos.add(mensaje);

		atributos.put("hayAvisosNeg", hayAvisosNeg);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);

		return mostrarPosicion(session, atributos, null, null, null, null, null, null, null, request);
	}

	@RequestMapping(value = "/exportar.html", method = RequestMethod.GET)
	public void exportar(HttpSession session, Map<String, Object> atributos,
			HttpServletResponse response) {
		// buscamos en la base de datos y lo subimos a request.
		Posicion posicion = (Posicion) session.getAttribute("posicionBusqueda");
		if (posicion != null) {
			posicion.setEstado("DISPONIBLE");
			Integer disponibles = posicionService.contarPosicionFiltradas(
					posicion, obtenerClienteAspUser());
			posicion.setEstado("OCUPADA");
			Integer noDisponibles = posicionService.contarPosicionFiltradas(
					posicion, obtenerClienteAspUser());
			posicion.setEstado("ANULADO");
			Integer anuladas = posicionService.contarPosicionFiltradas(
					posicion, obtenerClienteAspUser());
			posicion.setEstado("TEMPORALMENTE ANULADA");
			Integer temporalmenteAnuladas = posicionService
					.contarPosicionFiltradas(posicion, obtenerClienteAspUser());

			HashMap<String, Object> params = new HashMap<String, Object>();

			List<posicionChart> ohList = new ArrayList<ListaPosicionesController.posicionChart>();

			posicionChart disponible = new posicionChart();
			disponible.setNombre("DISPONIBLES");
			disponible.setCantidad(disponibles);
			ohList.add(disponible);
			posicionChart anulada = new posicionChart();
			anulada.setNombre("ANULADAS");
			anulada.setCantidad(anuladas);
			ohList.add(anulada);
			posicionChart ocupadas = new posicionChart();
			ocupadas.setNombre("OCUPADAS");
			ocupadas.setCantidad(noDisponibles);
			ohList.add(ocupadas);

			posicionChart temporalmenteAnulada = new posicionChart();
			temporalmenteAnulada.setNombre("TEMPORALMENTE ANULADAS");
			temporalmenteAnulada.setCantidad(temporalmenteAnuladas);
			ohList.add(temporalmenteAnulada);

			posicionForm pForm = new posicionForm();
			if (posicion.getCodigoEmpresa() != null) {
				Empresa e = empresaService.getByCodigo(posicion.getCodigoEmpresa(), obtenerClienteAspUser());
				if(e!=null){
					pForm.setEmpresa(e.getDescripcion());
				}	
				if (posicion.getCodigoSucursal() != null) {
					Sucursal s = sucursalService.getByCodigo(posicion.getCodigoSucursal(), e);
					if(s!=null){
						pForm.setSucursal(s.getDescripcion());
					}	
				}
			}

			if (posicion.getCodigoDeposito() != null) {
				Deposito d = depositoService.getByCodigo(posicion.getCodigoDeposito(), obtenerClienteAspUser());
				if(d!=null){
					pForm.setDeposito(d.getDescripcion());
				}	
			}

			if (posicion.getCodigoSeccion() != null) {
				Seccion sec = seccionService.getByCodigo(posicion.getCodigoSeccion());
				if(sec!=null){
					pForm.setSeccion(sec.getDescripcion());
				}	
			}
			if(posicion.getCodigoDesde()!=null)
				pForm.setCodigoDesde(posicion.getCodigoDesde());
			if(posicion.getCodigoHasta()!=null)
				pForm.setCodigoHasta(posicion.getCodigoHasta());
			
			if(posicion.getCodigoDesdeEstante()!=null)
				pForm.setEstanteDesde(posicion.getCodigoDesdeEstante());
			if(posicion.getCodigoHastaEstante()!=null)
				pForm.setEstanteHasta(posicion.getCodigoHastaEstante());
			if(posicion.getPosDesdeVertModulo()!=null)
				pForm.setModDesdeV(String.valueOf(posicion.getPosDesdeVertModulo()));
			if(posicion.getPosDesdeHorModulo()!=null)
				pForm.setModDesdeH(String.valueOf(posicion.getPosDesdeHorModulo())); 
			
			if(posicion.getPosHastaVertModulo()!=null)
				pForm.setModHastaV(String.valueOf(posicion.getPosHastaVertModulo()));
			if(posicion.getPosHastaHorModulo()!=null)
				pForm.setModHastaH(String.valueOf(posicion.getPosHastaHorModulo())); 
			
			if(posicion.getPosDesdeVert()!=null)
				pForm.setPosDesdeV(String.valueOf(posicion.getPosDesdeVert()));
			if(posicion.getPosDesdeHor()!=null)
				pForm.setPosDesdeH(String.valueOf(posicion.getPosDesdeHor())); 
			
			if(posicion.getPosHastaVert()!=null)
				pForm.setPosHastaV(String.valueOf(posicion.getPosHastaVert()));
			if(posicion.getPosHastaHor()!=null)
				pForm.setPosHastaH(String.valueOf(posicion.getPosHastaHor())); 
			if(posicion.getEstado()!=null)
				pForm.setEstado(posicion.getEstado()); 
			
			params.put("posicion", pForm);

			byte[] pdfByteArray;
			ServletOutputStream op = null;
			try {
				params.put("fecha", formatoFechaFormularios.format(new Date()));
				// se crea el reporte
				JasperReport jasperReport = JasperCompileManager
						.compileReport(session.getServletContext().getRealPath(
								Constants.PATH_JASPER)
								+ "/chartInformePosicion.jrxml");
				pdfByteArray = JasperRunManager.runReportToPdf(jasperReport,
						params, new JRBeanCollectionDataSource(ohList));
				// se envia el reporte
				response.setContentType("application/pdf");
				// response.setHeader( "Content-disposition",
				// "attachment; filename=reporte.pdf");

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
	}

	public class posicionForm {
		public String empresa;
		public String sucursal;
		public String deposito;
		public String seccion;
		public String estanteDesde;
		public String estanteHasta;
		public String codigoDesde;
		public String codigoHasta;
		
		public String modDesdeV;
		public String modDesdeH;
		public String modHastaV;
		public String modHastaH;
		
		public String posDesdeV;
		public String posDesdeH;
		public String posHastaV;
		public String posHastaH;
		public String estado;
		

		public String getEmpresa() {
			return empresa;
		}

		public void setEmpresa(String empresa) {
			this.empresa = empresa;
		}

		public String getSucursal() {
			return sucursal;
		}

		public void setSucursal(String sucursal) {
			this.sucursal = sucursal;
		}

		public String getDeposito() {
			return deposito;
		}

		public void setDeposito(String deposito) {
			this.deposito = deposito;
		}

		public String getSeccion() {
			return seccion;
		}

		public void setSeccion(String seccion) {
			this.seccion = seccion;
		}

		public String getEstanteDesde() {
			return estanteDesde;
		}

		public void setEstanteDesde(String estanteDesde) {
			this.estanteDesde = estanteDesde;
		}

		public String getEstanteHasta() {
			return estanteHasta;
		}

		public void setEstanteHasta(String estanteHasta) {
			this.estanteHasta = estanteHasta;
		}

		public String getCodigoDesde() {
			return codigoDesde;
		}

		public void setCodigoDesde(String codigoDesde) {
			this.codigoDesde = codigoDesde;
		}

		public String getCodigoHasta() {
			return codigoHasta;
		}

		public void setCodigoHasta(String codigoHasta) {
			this.codigoHasta = codigoHasta;
		}

		public String getModDesdeV() {
			return modDesdeV;
		}

		public void setModDesdeV(String modDesdeV) {
			this.modDesdeV = modDesdeV;
		}

		public String getModDesdeH() {
			return modDesdeH;
		}

		public void setModDesdeH(String modDesdeH) {
			this.modDesdeH = modDesdeH;
		}

		public String getModHastaV() {
			return modHastaV;
		}

		public void setModHastaV(String modHastaV) {
			this.modHastaV = modHastaV;
		}

		public String getModHastaH() {
			return modHastaH;
		}

		public void setModHastaH(String modHastaH) {
			this.modHastaH = modHastaH;
		}

		public String getPosDesdeV() {
			return posDesdeV;
		}

		public void setPosDesdeV(String posDesdeV) {
			this.posDesdeV = posDesdeV;
		}

		public String getPosDesdeH() {
			return posDesdeH;
		}

		public void setPosDesdeH(String posDesdeH) {
			this.posDesdeH = posDesdeH;
		}

		public String getPosHastaV() {
			return posHastaV;
		}

		public void setPosHastaV(String posHastaV) {
			this.posHastaV = posHastaV;
		}

		public String getPosHastaH() {
			return posHastaH;
		}

		public void setPosHastaH(String posHastaH) {
			this.posHastaH = posHastaH;
		}

		public String getEstado() {
			return estado;
		}

		public void setEstado(String estado) {
			this.estado = estado;
		}
	}

	public class posicionChart {
		public String nombre;
		public Integer cantidad;

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public Integer getCantidad() {
			return cantidad;
		}

		public void setCantidad(Integer cantidad) {
			this.cantidad = cantidad;
		}

	}

	// ///////////////////METODOS DE SOPORTE/////////////////////////////

	private void definirPopupSecciones(Map<String, Object> atributos,
			String val, String depositoCodigo) {
		// creo un hashmap para almacenar los parametros del popup
		Map<String, Object> seccionesPopupMap = new HashMap<String, Object>();
		// definicion de los campos a mostrar en la tabla
		// new CampoDisplayTag(Atributo,Titulo Columna,
		// Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo",
				"formularioSeccion.datosSeccion.codigo", false));
		campos.add(new CampoDisplayTag("descripcion",
				"formularioSeccion.datosSeccion.descripcion", false));
		// Coleccion de objetos a utilizar en el popup
		seccionesPopupMap.put("campos", campos);
		// el filtro del popup retorna un valor, el cual es utilizado para
		// filtrar la colección dentro del servicio
		seccionesPopupMap.put("coleccionPopup", seccionService
				.listarSeccionPopup(val, depositoCodigo,
						obtenerClienteAspUser()));
		// atributo de referencia que utiliza el popup para cargar cmponente
		// html en la pantalla padre
		seccionesPopupMap.put("referenciaPopup", "codigo");
		// atributo de referencia (segundo) que utiliza el popup para cargar
		// cmponente html en la pantalla padre
		seccionesPopupMap.put("referenciaPopup2", "descripcion");
		// id del objeto html donde se va a escribir el valor del campo de
		// referencia del objeto seleccionado
		seccionesPopupMap.put("referenciaHtml", "codigoSeccion");
		// url que se debe consumir con ajax
		seccionesPopupMap.put("urlRequest", "mostrarPosicion.html");
		// se vuelve a setear el texto utilizado para el filtrado
		seccionesPopupMap.put("textoBusqueda", val);

		seccionesPopupMap.put("filterPopUp", depositoCodigo);
		// codigo de la localización para el titulo del popup
		seccionesPopupMap.put("tituloPopup", "textos.seleccion");
		// Agrego el mapa a los atributos
		atributos.put("seccionesPopupMap", seccionesPopupMap);
	}

	private void definirPopupDepositos(Map<String, Object> atributos,
			String val, String sucursalCodigo) {
		// creo un hashmap para almacenar los parametros del popup
		Map<String, Object> depositosPopupMap = new HashMap<String, Object>();
		// definicion de los campos a mostrar en la tabla
		// new CampoDisplayTag(Atributo,Titulo Columna,
		// Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo",
				"formularioDeposito.datosDeposito.codigo", false));
		campos.add(new CampoDisplayTag("descripcion",
				"formularioDeposito.datosDeposito.descripcion", false));
		// Coleccion de objetos a utilizar en el popup
		depositosPopupMap.put("campos", campos);
		// el filtro del popup retorna un valor, el cual es utilizado para
		// filtrar la colección dentro del servicio
		depositosPopupMap.put("coleccionPopup", depositoService
				.listarDepositoPopup(val, sucursalCodigo,
						obtenerClienteAspUser()));
		// atributo de referencia que utiliza el popup para cargar cmponente
		// html en la pantalla padre
		depositosPopupMap.put("referenciaPopup", "codigo");
		// atributo de referencia (segundo) que utiliza el popup para cargar
		// cmponente html en la pantalla padre
		depositosPopupMap.put("referenciaPopup2", "descripcion");
		// id del objeto html donde se va a escribir el valor del campo de
		// referencia del objeto seleccionado
		depositosPopupMap.put("referenciaHtml", "codigoDeposito");
		// url que se debe consumir con ajax
		depositosPopupMap.put("urlRequest", "mostrarPosicion.html");
		// se vuelve a setear el texto utilizado para el filtrado
		depositosPopupMap.put("textoBusqueda", val);

		depositosPopupMap.put("filterPopUp", sucursalCodigo);
		// codigo de la localización para el titulo del popup
		depositosPopupMap.put("tituloPopup", "textos.seleccion");
		// Agrego el mapa a los atributos
		atributos.put("depositosPopupMap", depositosPopupMap);
	}

	private void definirPopupSucursal(Map<String, Object> atributos,
			String val, String empresaCodigo) {
		// creo un hashmap para almacenar los parametros del popup
		Map<String, Object> sucursalesPopupMap = new HashMap<String, Object>();
		// definicion de los campos a mostrar en la tabla
		// new CampoDisplayTag(Atributo,Titulo Columna,
		// Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo",
				"formularioSucursal.datosSucursal.codigo", false));
		campos.add(new CampoDisplayTag("descripcion",
				"formularioSucursal.datosSucursal.descripcion", false));
		// Coleccion de objetos a utilizar en el popup
		sucursalesPopupMap.put("campos", campos);
		// el filtro del popup retorna un valor, el cual es utilizado para
		// filtrar la colección dentro del servicio
		sucursalesPopupMap.put("coleccionPopup", sucursalService
				.listarSucursalPopup(val, empresaCodigo,
						obtenerClienteAspUser()));
		// atributo de referencia que utiliza el popup para cargar cmponente
		// html en la pantalla padre
		sucursalesPopupMap.put("referenciaPopup", "codigo");
		// atributo de referencia (segundo) que utiliza el popup para cargar
		// cmponente html en la pantalla padre
		sucursalesPopupMap.put("referenciaPopup2", "descripcion");
		// id del objeto html donde se va a escribir el valor del campo de
		// referencia del objeto seleccionado
		sucursalesPopupMap.put("referenciaHtml", "codigoSucursal");
		// url que se debe consumir con ajax
		sucursalesPopupMap.put("urlRequest", "mostrarPosicion.html");
		// se vuelve a setear el texto utilizado para el filtrado
		sucursalesPopupMap.put("textoBusqueda", val);
		// se setea el codigo del cliente seleccionado.
		sucursalesPopupMap.put("filterPopUp", empresaCodigo);
		// codigo de la localización para el titulo del popup
		sucursalesPopupMap.put("tituloPopup", "textos.seleccion");
		// Agrego el mapa a los atributos
		atributos.put("sucursalesPopupMap", sucursalesPopupMap);
	}

	private void definirPopupEmpresa(Map<String, Object> atributos, String val) {
		// creo un hashmap para almacenar los parametros del popup
		Map<String, Object> empresasPopupMap = new HashMap<String, Object>();
		// definicion de los campos a mostrar en la tabla
		// new CampoDisplayTag(Atributo,Titulo Columna,
		// Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo",
				"formularioEmpresa.datosEmpresa.codigo", false));
		campos.add(new CampoDisplayTag("razonSocial.razonSocial",
				"formularioEmpresa.datosEmpresa.razonSocial", false));
		campos.add(new CampoDisplayTag("descripcion",
				"formularioEmpresa.datosEmpresa.descripcion", false));
		// Coleccion de objetos a utilizar en el popup
		empresasPopupMap.put("campos", campos);
		// el filtro del popup retorna un valor, el cual es utilizado para
		// filtrar la colección dentro del servicio
		empresasPopupMap
				.put("coleccionPopup", empresaService.listarEmpresaPopup(val,
						obtenerClienteAspUser()));
		// atributo de referencia que utiliza el popup para cargar cmponente
		// html en la pantalla padre
		empresasPopupMap.put("referenciaPopup", "codigo");
		// atributo de referencia (segundo) que utiliza el popup para cargar
		// cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup2", "descripcion");
		// id del objeto html donde se va a escribir el valor del campo de
		// referencia del objeto seleccionado
		empresasPopupMap.put("referenciaHtml", "codigoEmpresa");
		// url que se debe consumir con ajax
		empresasPopupMap.put("urlRequest", "mostrarPosicion.html");
		// se vuelve a setear el texto utilizado para el filtrado
		empresasPopupMap.put("textoBusqueda", val);
		// codigo de la localización para el titulo del popup
		empresasPopupMap.put("tituloPopup", "textos.seleccion");
		// Agrego el mapa a los atributos
		atributos.put("empresasPopupMap", empresasPopupMap);
	}

	private ClienteAsp obtenerClienteAspUser() {
		return ((User) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal()).getCliente();
	}

}