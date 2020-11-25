package com.dividato.configuracionGeneral.controladores;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.security.accesoDatos.configuraciongeneral.interfaz.AfipTipoComprobanteService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteDireccionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpleadoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TransporteService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.accesoDatos.jerarquias.interfaz.JerarquiaService;
import com.security.accesoDatos.jerarquias.interfaz.RequerimientoService;
import com.security.accesoDatos.jerarquias.interfaz.TipoJerarquiaService;
import com.security.accesoDatos.jerarquias.interfaz.TipoOperacionService;
import com.security.accesoDatos.jerarquias.interfaz.TipoRequerimientoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.jerarquias.Jerarquia;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.jerarquias.TipoJerarquia;
import com.security.modelo.jerarquias.TipoOperacion;
import com.security.modelo.jerarquias.TipoRequerimiento;
import com.security.modelo.seguridad.User;
import com.security.utils.CampoDisplayTag;
import com.security.utils.Constantes;

/**
 * TODO: Progresivamente todos los popup de las búsquedas deberían levantarse con este controlador.
 * De esa forma mantenemos el código en un único lugar y no replicado en todos los controladores que lo usan como ahora.
 * 
 * @author FedeMz
 *
 */
@Controller
@RequestMapping(
		value=
			{	
				"/popUpSeleccionEmpresa.html",
				"/popUpSeleccionSucursal.html",
				"/popUpSeleccionCliente.html",
				"/popUpSeleccionClienteEmpresaDefecto.html",
				"/popUpSeleccionClasificacionDocumental.html",
				"/popUpSelClasificacionDocumental.html",
				"/popUpSeleccionContenedor.html",
				"/popUpSeleccionContenedorEAN.html",
				"/popUpSeleccionElemento.html",
				"/popUpSeleccionElementoEAN.html",
				"/popUpSeleccionElemento2.html",
				"/popUpSeleccionElementoEditable.html",
				"/popUpConceptoFacturable.html",
				"/popUpSeleccionDeposito.html",
				"/popUpSeleccionDepositoPorSucursal.html",
				"/popUpSeleccionLectura.html",
				"/popUpSeleccionTipoOperacion.html",
				"/popUpSeleccionConceptoFacturable.html",
				"/popUpSeleccionListaPrecios.html",
				"/popUpSeleccionListaPreciosPorConceptoFacturable.html",
				"/popUpSeleccionTipoElemento.html",
				"/popUpSeleccionTiposElementos.html",
				"/popUpSeleccionTiposOperaciones.html",
				"/popUpSeleccionDescripcionClasificacionDocumental.html",
				"/popUpSeleccionRemito.html",
				"/popUpSeleccionTipoRequerimiento.html",
				"/popUpSeleccionSerie.html",
				"/popUpSeleccionSeriePorClienteYTipoComprobante.html",
				"/popUpSeleccionDirecciones.html",
				"/popUpSeleccionEmpleadoSolicitante.html",
				"/popUpSeleccionEmpleadoAutorizante.html",
				"/popUpSeleccionListaPreciosPorCliente.html",
				"/popUpSeleccionTipoJerarquia.html",
				"/popUpSeleccionJerarquia.html",
				"/popUpSeleccionRequerimiento.html",
				"/popUpSeleccionRequerimientoNumero.html",
				"/popUpSeleccionUsuario.html",
				"/popUpSerie.html",
				"/popUpSeriePorCodigos.html",
				"/popUpTransporte.html",
				"/popUpSerie.html",
				"/popUpSeriePorCodigos.html",
				"/popUpSeleccionTipoElementoPrefijo.html",
				"/seleccionEmpresa.html",
				"/seleccionSucursal.html",
				"/seleccionCliente.html",
				"/seleccionClasificacionDocumental.html",
				"/seleccionContenedor.html",
				"/seleccionContenedor2.html",
				"/seleccionTipoElementoByPrefijoCodigo.html",
				"/seleccionElemento.html",
				"/seleccionContenedorOElemento.html",
				"/seleccionTipoElemento.html",
				"/seleccionDeposito.html",
				"/seleccionLectura.html",
				"/seleccionTipoOperacion.html",
				"/seleccionTipoJerarquia.html",
				"/seleccionJerarquia.html",
				"/seleccionTipoOperacion.html",
				"/seleccionEmpleado.html",
				"/seleccionRequerimiento.html",
				"/searchListaPrecioDefecto.html",
				"/seleccionUsuario.html",
				"/seleccionRequerimientoNumero.html"
			}
		)
public class PopUpAndSearchController {
	private static Logger logger=Logger.getLogger(PopUpAndSearchController.class);
	
	private ClienteEmpService clienteEmpService;
	private SucursalService sucursalService;
	private EmpresaService empresaService;
	private ElementoService elementoService;
	private SerieService serieService;
	private ClasificacionDocumentalService clasificacionDocumentalService;
	private ReferenciaService referenciaService;
	private DepositoService depositoService;
	private LecturaService lecturaService;
	private TipoOperacionService tipoOperacionService;
	private ConceptoFacturableService conceptoFacturableService;
	private ListaPreciosService listaPreciosService;
	private TipoElementoService tipoElementoService;
	private RemitoService remitoService;
	private TipoRequerimientoService tipoRequerimientoService;
	private ClienteDireccionService clienteDireccionService;
	private EmpleadoService empleadoService;
	private AfipTipoComprobanteService afipTipoComprobanteService;
	private TipoJerarquiaService tipoJerarquiaService;
	private JerarquiaService jerarquiaService;
	private TransporteService transporteService;
	private RequerimientoService requerimientoService;
	private UserService userService;

	@Autowired
	public void setServices(
			SucursalService sucursalService,
			ClienteEmpService clienteEmpService,
			EmpresaService empresaService,
			ElementoService elementoService,
			ClasificacionDocumentalService clasificacionDocumentalService,
			ReferenciaService referenciaService,
			DepositoService depositoService,
			SerieService serieService,
			LecturaService lecturaService,
			TipoOperacionService tipoOperacionService,
			ConceptoFacturableService conceptoFacturableService,
			ListaPreciosService listaPreciosService,
			TipoElementoService tipoElementoService,
			RemitoService remitoService,
			TipoRequerimientoService tipoRequerimientoService,
			ClienteDireccionService clienteDireccionService,
			EmpleadoService empleadoService,
			AfipTipoComprobanteService afipTipoComprobanteService,
			TipoJerarquiaService tipoJerarquiaService,
			JerarquiaService jerarquiaService,
			TransporteService transporteService,
			RequerimientoService requerimientoService,
			UserService userService

			) {
		this.sucursalService = sucursalService;
		this.clienteEmpService = clienteEmpService;
		this.empresaService = empresaService;
		this.elementoService = elementoService;
		this.clasificacionDocumentalService = clasificacionDocumentalService;
		this.referenciaService = referenciaService;
		this.depositoService = depositoService;
		this.serieService = serieService;
		this.lecturaService = lecturaService;
		this.tipoOperacionService = tipoOperacionService;
		this.conceptoFacturableService = conceptoFacturableService;
		this.listaPreciosService = listaPreciosService;
		this.tipoElementoService = tipoElementoService;
		this.remitoService = remitoService;
		this.tipoRequerimientoService = tipoRequerimientoService;
		this.clienteDireccionService = clienteDireccionService;
		this.empleadoService = empleadoService;
		this.afipTipoComprobanteService = afipTipoComprobanteService;
		this.tipoJerarquiaService = tipoJerarquiaService;
		this.jerarquiaService = jerarquiaService;
		this.transporteService = transporteService;
		this.requerimientoService = requerimientoService;
		this.userService = userService;

	}
	
	@RequestMapping(value="/popUpSeleccionEmpresa.html")
	public String popUpSeleccionEmpresas(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String descripcionEmpresa) {
		atributos.put("mapa", "empresasPopupMap");
		atributos.put("clase", "empresasPopupMap");
		definirPopupEmpresa(atributos, descripcionEmpresa);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionSucursal.html")
	public String popUpSeleccionSucursal(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=true) String empresaCodigo,
			@RequestParam(value="val",required=false) String descripcionSucursal) {
		atributos.put("mapa", "sucursalesPopupMap");
		atributos.put("clase", "sucursalesPopupMap");
		definirPopupSucursal(atributos, descripcionSucursal,empresaCodigo);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionCliente.html")
	public String popUpSeleccionCliente(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=true) String empresaCodigo,
			@RequestParam(value="val",required=false) String descripcionCliente) {
		atributos.put("mapa", "clientesPopupMap");
		atributos.put("clase", "clientesPopupMap");
		definirPopupCliente(atributos, descripcionCliente, empresaCodigo);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionClienteEmpresaDefecto.html")
	public String popUpSeleccionClienteEmpresaDefecto(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String descripcionCliente) {
		atributos.put("mapa", "clientesPopupMap");
		atributos.put("clase", "clientesPopupMap");
		String empresaCodigo = "";
		if(obtenerEmpresa()!=null)
			empresaCodigo = obtenerEmpresa().getCodigo();
		definirPopupClienteEmpresaDefecto(atributos, descripcionCliente, empresaCodigo);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionClasificacionDocumental.html")
	public String popUpSeleccionClasificacionDocumental(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=true) String clienteCodigo,
			@RequestParam(value="val",required=false) String nombreClasificacion,
			HttpSession session) {
		atributos.put("mapa", "clasificacionDocumentalPopupMap");
		atributos.put("clase", "clasificacionDocumentalPopupMap");
		definirPopupClasificacionDocumental(atributos, nombreClasificacion, clienteCodigo,session);
		return "popupBusquedaClasificacionDocumental";
	}
	@RequestMapping(value="/popUpSelClasificacionDocumental.html")
	public String popUpSelClasificacionDocumental(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=true) String clienteCodigo,
			@RequestParam(value="destino",required=false) String destino,
			@RequestParam(value="val",required=false) String nombreClasificacion,
			HttpSession session) {
		atributos.put("mapa", "clasificacionDocumentalPopupMap");
		atributos.put("clase", "clasificacionDocumentalPopupMap");
		definirPopupClasDocumental(atributos, nombreClasificacion, clienteCodigo, destino,session);
		return "popupBusquedaClasificacionDocumental";
	}
	@RequestMapping(value="/popUpSeleccionContenedor.html")
	public String popUpSeleccionContenedor(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=false) String codigoCliente,
			@RequestParam(value="val",required=false) String descripcionTipoElemento,
			@RequestParam(value="codigoTipoElemento",required=false) String codigoTipoElemento,
			@RequestParam(value="limitarCliente",required=false) Boolean limitarCliente,
			@RequestParam(value="prefijoCodigoTipoElemento", required=false) String prefijoCodigoTipoElemento,
			@RequestParam(value="cerrado", required=false) Boolean cerrado,
			HttpServletRequest request) {
		atributos.put("mapa", "contenedoresPopupMap");
		atributos.put("clase", "contenedoresPopupMap"); 
		definirPopupElemento(atributos,request,descripcionTipoElemento,codigoTipoElemento,prefijoCodigoTipoElemento,codigoCliente,limitarCliente,null,true,cerrado);
		return "popupBusquedaNuevoPaginado";
	}
	@RequestMapping(value="/popUpSeleccionContenedorEAN.html")
	public String popUpSeleccionContenedorEAN(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=false) String codigoCliente,
			@RequestParam(value="val",required=false) String descripcionTipoElemento,
			@RequestParam(value="codigoTipoElemento",required=false) String codigoTipoElemento,
			@RequestParam(value="limitarCliente",required=false) Boolean limitarCliente,
			@RequestParam(value="prefijoCodigoTipoElemento", required=false) String prefijoCodigoTipoElemento,
			@RequestParam(value="cerrado", required=false) Boolean cerrado,
			HttpServletRequest request) {
		atributos.put("mapa", "contenedoresPopupMap");
		atributos.put("clase", "contenedoresPopupMap"); 
		definirPopupElementoEAN(atributos,request,descripcionTipoElemento,codigoTipoElemento,prefijoCodigoTipoElemento,codigoCliente,limitarCliente,null,true,cerrado);
		return "popupBusquedaNuevoPaginado";
	}
	@RequestMapping(value="/popUpSeleccionElemento2.html")
	public String popUpSeleccionElemento2(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=false) String codigoCliente,
			@RequestParam(value="val",required=false) String descripcionTipoElemento,
			@RequestParam(value="codigoTipoElemento",required=false) String codigoTipoElemento,
			@RequestParam(value="limitarCliente",required=false) Boolean limitarCliente,
			@RequestParam(value="prefijoCodigoTipoElemento", required=false) String prefijoCodigoTipoElemento,
			HttpServletRequest request) {
		atributos.put("mapa", "elementosPopupMap");
		atributos.put("clase", "elementosPopupMap"); 
		definirPopupElemento2(atributos,request,descripcionTipoElemento,codigoTipoElemento,prefijoCodigoTipoElemento,codigoCliente,limitarCliente,null,null);
		return "popupBusquedaNuevoPaginado";
	}
	
	@RequestMapping(value="/popUpSeleccionElemento.html")
	public String popUpSeleccionElemento(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=true) String codigoCliente,
			@RequestParam(value="val",required=false) String descripcionTipoElemento,
			@RequestParam(value="limitarCliente",required=false) Boolean limitarCliente,
			@RequestParam(value="libresODistintoLoteId",required=false) Long libresODistintoLoteId,
			HttpServletRequest request) {
		atributos.put("mapa", "elementosPopupMap");
		atributos.put("clase", "elementosPopupMap"); 
		definirPopupElemento(atributos,request,descripcionTipoElemento,null,null,codigoCliente,limitarCliente,libresODistintoLoteId,false,null);
		return "popupBusquedaNuevoPaginado";
	}
	
	@RequestMapping(value="/popUpSeleccionElementoEAN.html")
	public String popUpSeleccionElementoEAN(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=true) String codigoCliente,
			@RequestParam(value="val",required=false) String descripcionTipoElemento,
			@RequestParam(value="limitarCliente",required=false) Boolean limitarCliente,
			@RequestParam(value="libresODistintoLoteId",required=false) Long libresODistintoLoteId,
			HttpServletRequest request) {
		atributos.put("mapa", "elementosPopupMap");
		atributos.put("clase", "elementosPopupMap"); 
		definirPopupElementoEAN(atributos,request,descripcionTipoElemento,null,null,codigoCliente,limitarCliente,libresODistintoLoteId,false,null);
		return "popupBusquedaNuevoPaginado";
	}
	
	@RequestMapping(value="/popUpSeleccionElementoEditable.html")
	public String popUpSeleccionElemento(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=true) String codigoCliente,
			@RequestParam(value="val",required=false) String descripcionTipoElemento,
			@RequestParam(value="limitarCliente",required=false) Boolean limitarCliente,
			@RequestParam(value="libresODistintoLoteId",required=false) Long libresODistintoLoteId,
			@RequestParam(value="cajaRespuesta",required=false) String cajaRespuesta,
			HttpServletRequest request) {
		atributos.put("mapa", "elementosPopupMap");
		atributos.put("clase", "elementosPopupMap"); 
		definirPopupElementoEditable(atributos,request,descripcionTipoElemento,null,null,codigoCliente,limitarCliente,libresODistintoLoteId,false,null,cajaRespuesta);
		return "popupBusquedaNuevoPaginado";
	}
	
	@RequestMapping(value="/popUpSeleccionDeposito.html")
	public String popUpSeleccionDepositos(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String descripcionDeposito) {
		atributos.put("mapa", "depositosPopupMap");
		atributos.put("clase", "depositosPopupMap");
		String codigoSucursal = null;
		Sucursal sucursal = obtenerSucursal();
		if(sucursal!=null){
			codigoSucursal = sucursal.getCodigo();
		}
		definirPopupDepositos(atributos, descripcionDeposito, codigoSucursal);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionDepositoPorSucursal.html")
	public String popUpSeleccionDepositosPorSucursal(Map<String,Object> atributos,
			@RequestParam(value="codigo",required=false) String codigoSucursal,
			@RequestParam(value="val",required=false) String val) {
		atributos.put("mapa", "depositosPopupMap");
		atributos.put("clase", "depositosPopupMap");
		
		definirPopupDepositos(atributos, val, codigoSucursal);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionListaPrecios.html")
	public String popUpSeleccionListaPrecios(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String descripcionDeposito,
			@RequestParam(value="habilitado",required=false) Boolean habilitado) {
		atributos.put("mapa", "listaPreciosPopupMap");
		atributos.put("clase", "listaPreciosPopupMap");
		definirPopupListaPrecios(atributos,descripcionDeposito, habilitado );
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionListaPreciosPorConceptoFacturable.html")
	public String popUpSeleccionListaPreciosPorConceptoFacturable(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String val,
			@RequestParam(value="habilitado",required=false) Boolean habilitado,
			@RequestParam(value="codigoClienteEmp",required=false) String codigoClienteEmp,
			@RequestParam(value="codigoEmpresa",required=false) String codigoEmpresa,
			@RequestParam(value="codigoConceptoFacturable",required=false) String codigoConceptoFacturable,
			@RequestParam(value="fancyboxMode",required=false) Boolean fancyboxMode) {
		atributos.put("mapa", "listaPreciosPopupMap");
		atributos.put("clase", "listaPreciosPopupMap");
		definirPopupListaPreciosPorConceptos(atributos, val, habilitado, codigoClienteEmp, codigoEmpresa, codigoConceptoFacturable);
		if(Boolean.TRUE.equals(fancyboxMode)){
			return "popupBusquedaNuevoFancyBox";
		}else{
			return "popupBusquedaNuevo";
		}
	}
	
	@RequestMapping(value="/popUpSeleccionTipoElemento.html")
	public String popUpSeleccionTipoElemento(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String descripcionTipoElemento) {
		atributos.put("mapa", "tipoElementosPopupMap");
		atributos.put("clase", "tipoElementosPopupMap");
		definirPopupTipoElemento(atributos, descripcionTipoElemento );
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionTipoElementoPrefijo.html")
	public String popUpSeleccionTipoElementoPrefijo(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String descripcionTipoElemento) {
		atributos.put("mapa", "tipoElementosPopupMap");
		atributos.put("clase", "tipoElementosPopupMap");
		definirPopupTipoElementoPrefijo(atributos, descripcionTipoElemento );
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionTiposElementos.html")
	public String popUpSeleccionTiposElementos(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String descripcionTipoElemento) {
		atributos.put("mapa", "tipoElementosPopupMap");
		atributos.put("clase", "tipoElementosPopupMap");
		definirPopupTipoElemento(atributos, descripcionTipoElemento );
		return "popupTipoElemento";
	}

	
	@RequestMapping(value="/popUpSeleccionTiposOperaciones.html")
	public String popUpSeleccionTiposOperaciones(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String descripcionTipoOperacion) {
		atributos.put("mapa", "tipoOperacionPopupMap");
		atributos.put("clase", "tipoOperacionPopupMap");
		definirPopupTipoOperaciones(atributos, descripcionTipoOperacion);
		return "popupTipoOperacion";
	}
	

	@RequestMapping(value="/popUpSeleccionDescripcionClasificacionDocumental.html")
	public String popUpSeleccionDescripcionClasificacionDocumental(Map<String,Object> atributos,
			@RequestParam(value="codigo",required=false) Integer codigoClasificacionDocumental,
			@RequestParam(value="val",required=false) String descripcion,
			@RequestParam(value="codigo2",required=false) String codigoCliente) {
		atributos.put("mapa", "descripcionesPopupMap");
		atributos.put("clase", "descripcionesPopupMap");
		definirPopupDescripcionClasificacionDocumental(atributos,codigoClasificacionDocumental, descripcion,codigoCliente );
		return "popupBusquedaNuevo";
	}

	@RequestMapping(value="/popUpSeleccionRemito.html")
	public String popUpSeleccionRemito(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=true) String clienteCodigo,
			@RequestParam(value="val",required=false) String nombreRemito) {
		atributos.put("mapa", "remitoPopupMap");
		atributos.put("clase", "remitoPopupMap");
		definirPopupRemito(atributos, nombreRemito, clienteCodigo);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionDirecciones.html")
	public String popUpSeleccionDirecciones(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String valClienteDireccion,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo) {
		atributos.put("mapa", "direccionesPopupMap");
		atributos.put("clase", "direccionesPopupMap");
		definirPopupDirecciones(atributos, valClienteDireccion, clienteCodigo);
		return "popupBusquedaNuevo";
	}

	@RequestMapping(value="/popUpSeleccionEmpleadoSolicitante.html")
	public String popUpSeleccionEmpleadoSolicitante(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String valEmpleadoSolicitante,
			@RequestParam(value="codigo",required=false) String clienteCodigo,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigoString) {
		atributos.put("mapa", "personalPopupMap");
		atributos.put("clase", "personalPopupMap");
		if(clienteCodigo==null || clienteCodigo.equals(""))
			clienteCodigo=clienteCodigoString;
		definirPopupEmpleadoSolicitante(atributos, valEmpleadoSolicitante, clienteCodigo);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionEmpleadoAutorizante.html")
	public String popUpSeleccionEmpleadoAutorizante(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String valEmpleadoAutorizante,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo) {
		atributos.put("mapa", "personalPopupMapAutorizante");
		atributos.put("clase", "personalPopupMapAutorizante");
		definirPopupEmpleadoAutorizante(atributos, valEmpleadoAutorizante, clienteCodigo);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionListaPreciosPorCliente.html")
	public String popUpSeleccionListaPreciosPorCliente(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String valListaPreciosPorCliente,
			@RequestParam(value="clienteCodigoString",required=false) String clienteCodigo) {
		atributos.put("mapa", "listaPreciosPopupMap");
		atributos.put("clase", "listaPreciosPopupMap");
		definirPopupListaPreciosPorCliente(atributos, valListaPreciosPorCliente, clienteCodigo);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionTipoJerarquia.html")
	public String popUpSeleccionTipoJerarquia(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String descripcionTipoJerarquia) {
		atributos.put("mapa", "tiposJerarquiaPopupMap");
		atributos.put("clase", "tiposJerarquiaPopupMap");
		definirPopupTipoJerarquia(atributos, descripcionTipoJerarquia);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionJerarquia.html")
	public String popUpSeleccionJerarquia(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String descripcionJerarquia,
			@RequestParam(value="codigo",required=false) String codigoTipoJerarquia) {
		atributos.put("mapa", "jerarquiasPopupMap");
		atributos.put("clase", "jerarquiasPopupMap");
		definirPopupJerarquia(atributos, codigoTipoJerarquia,descripcionJerarquia);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionRequerimiento.html")
	public String popUpSeleccionRequerimiento(Map<String,Object> atributos,
			@RequestParam(value="codigoEmpresa",required=false) String codigoEmpresa,
			@RequestParam(value="val",required=false) String descripcion,
			@RequestParam(value="codigoSucursal",required=false) String codigoSucursal,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente) {
		atributos.put("mapa", "requerimientosPopupMap");
		atributos.put("clase", "requerimientosPopupMap");
		definirPopupRequerimiento(atributos,descripcion,codigoCliente,codigoEmpresa,codigoSucursal);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionRequerimientoNumero.html")
	public String popUpSeleccionRequerimientoNumero(Map<String,Object> atributos,
			@RequestParam(value="codigoEmpresa",required=false) String codigoEmpresa,
			@RequestParam(value="val",required=false) String descripcion,
			@RequestParam(value="codigoSucursal",required=false) String codigoSucursal,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente) {
		atributos.put("mapa", "requerimientosPopupMap");
		atributos.put("clase", "requerimientosPopupMap");
		definirPopupRequerimiento(atributos,descripcion,codigoCliente,codigoEmpresa,codigoSucursal);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionUsuario.html")
	public String popUpSeleccionUsuario(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=true) String empresaCodigo,
			@RequestParam(value="val",required=false) String nombreOApellido,
			@RequestParam(value="field", required=false) String field) {
		atributos.put("mapa", "responsablesPopupMap");
		atributos.put("clase", "responsablesPopupMap");
		definirPopupUsuario(atributos, nombreOApellido, field);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/seleccionEmpresa.html")
	public void seleccionEmpresa(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		if(!codigo.isEmpty()){
			Empresa emp = empresaService.getByCodigo(codigo,clienteAsp);
			if(emp!=null){
				respuesta = emp.getDescripcion();
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
			
	@RequestMapping(value="/seleccionSucursal.html")
	public void seleccionSucursal(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=true) String dependencia){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		if(!codigo.isEmpty() && !dependencia.isEmpty()){
			Sucursal suc = sucursalService.getByCodigo(codigo,dependencia,clienteAsp);
			if(suc!=null){
				respuesta = suc.getDescripcion();
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
	@RequestMapping(value="/seleccionCliente.html")
	public void seleccionCliente(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=true) String dependencia){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		if(!codigo.isEmpty() && !dependencia.isEmpty()){
			ClienteEmp cli = clienteEmpService.getByCodigo(codigo,dependencia,clienteAsp);
			if(cli!=null){
				respuesta = cli.getRazonSocialONombreYApellido();
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
	
	@RequestMapping(value="/searchListaPrecioDefecto.html")
	public void searchListaPrecioDefecto(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigoCliente,
			@RequestParam(value="dependencia", required=true) String codigoEmpresa)
			{
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		if(!codigoCliente.isEmpty() && !codigoEmpresa.isEmpty()){
			ClienteEmp cli = clienteEmpService.getClienteConListaDefectoByCodigo(codigoCliente,codigoEmpresa,clienteAsp);
			if(cli!=null && cli.getListaPreciosDefecto()!=null){
				respuesta = cli.getListaPreciosDefecto().getCodigo();
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
	
	
	@RequestMapping(value="/seleccionClasificacionDocumental.html")
	public void seleccionClasificacionDocumental(HttpServletResponse response,
			@RequestParam(value="nodo", required=false) String nodo,
			@RequestParam(value="codigo", required=true) String codigoStr,
			@RequestParam(value="dependencia", required=true) String dependencia,
			HttpSession session){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		Integer codigo = null;
		try{
			codigo = Integer.parseInt(codigoStr);
		}catch(Exception e){}
		if(codigo!=null && !dependencia.isEmpty())
		{
			ClasificacionDocumental cla = clasificacionDocumentalService.getClasificacionByCodigo(codigo,dependencia,clienteAsp,nodo);
			if(cla!=null)
			{
				Empleado empleado = (Empleado) session.getAttribute("empleadoSession");
				if(empleado!=null){
					List<ClasificacionDocumental> porEmpleado = new ArrayList<ClasificacionDocumental>(clasificacionDocumentalService.getByPersonalAsignado(empleado));
					if(porEmpleado!=null && porEmpleado.size()>0 && verificarClasificacionAsignadaEmpleado(porEmpleado.get(0), cla))
						respuesta = cla.getNombre();
				}
				else
					respuesta = cla.getNombre();
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
	
	@RequestMapping(value="/seleccionContenedor.html")
	public void seleccionContenedor(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=true) String codigoCliente,
			@RequestParam(value="codigoTipoElemento",required=false) String codigoTipoElemento,
			@RequestParam(value="limitarCliente",required=false) Boolean limitarCliente){

		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		if(!codigo.isEmpty() && !codigoCliente.isEmpty()){
			Elemento ele = elementoService.getContenedorByCodigo(codigo,codigoCliente,limitarCliente,clienteAsp,codigoTipoElemento);
			if(ele!=null){
				respuesta = ele.getTipoElemento().getDescripcion();
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
	
	@RequestMapping(value="/seleccionContenedor2.html")
	public void seleccionContenedor2(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=true) String codigoCliente,
			@RequestParam(value="codigoTipoElemento",required=false) String codigoTipoElemento,
			@RequestParam(value="limitarCliente",required=false) Boolean limitarCliente,
			@RequestParam(value="cerrado",required=false) Boolean cerrado){

		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		if(!codigo.isEmpty() && !codigoCliente.isEmpty()){
			Elemento ele = elementoService.getContenedorByCodigo(codigo,codigoCliente,limitarCliente,clienteAsp,codigoTipoElemento,cerrado);
			if(ele!=null){
				respuesta = ele.getTipoElemento().getDescripcion()+"-"+(ele.getCerrado()!=null?ele.getCerrado():false);
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
	
	@RequestMapping(value="/seleccionElemento.html")
	public void seleccionElemento(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=true) String codigoCliente,
			@RequestParam(value="limitarCliente",required=false) Boolean limitarCliente,
			@RequestParam(value="libresODistintoLoteId",required=false) Long libresODistintoLoteId){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		if(!codigo.isEmpty() && !codigoCliente.isEmpty()){
			Elemento ele = elementoService.getElementoByCodigo(codigo,codigoCliente,clienteAsp,limitarCliente,libresODistintoLoteId);
			if(ele!=null){
				respuesta = ele.getTipoElemento().getDescripcion();
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
	
	@RequestMapping(value="/seleccionContenedorOElemento.html")
	public void seleccionContenedorOElemento(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=true) String codigoCliente,
			@RequestParam(value="limitarCliente",required=false) Boolean limitarCliente,
			@RequestParam(value="libresODistintoLoteId",required=false) Long libresODistintoLoteId){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		if(!codigo.isEmpty() && !codigoCliente.isEmpty()){
			Elemento ele = elementoService.getContenedorOElementoByCodigo(codigo,codigoCliente,clienteAsp,limitarCliente,null);
			if(ele!=null){
				respuesta = ele.getTipoElemento().getDescripcion();
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
	
	@RequestMapping(value="/seleccionTipoElemento.html")
	public void seleccionTipoElemento(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo){
		String respuesta = "";
		
		ClienteAsp clienteAsp = obtenerClienteAsp();
		if(!codigo.isEmpty()){
			TipoElemento tipoEle = tipoElementoService.getByCodigo(codigo,clienteAsp);
			if(tipoEle!=null){
				respuesta = tipoEle.getDescripcion();
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
	
	
	
	@RequestMapping(value="/seleccionTipoElementoByPrefijoCodigo.html")
	public void seleccionTipoElementoByPrefijoCodigo(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo){
		String respuesta = "";
		
		ClienteAsp clienteAsp = obtenerClienteAsp();
		if(!codigo.isEmpty()){
			TipoElemento tipoEle = tipoElementoService.getByPrefijo(codigo,clienteAsp);
			if(tipoEle!=null){
				respuesta = tipoEle.getDescripcion();
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
	
	@RequestMapping(value="/popUpSerie.html")
	public String popupSerie(Map<String,Object> atributos,
			@RequestParam(value="codigoEmpresa", required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal", required=false) String codigoSucursal,
			@RequestParam(value="idAfipTipoComprobante", required=false) Long idAfipTipoComprobante,
			@RequestParam(value="val", required=false) String val){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		
		//se crea el objeto serie para realizar la busqueda del popup de series
		Serie serieBusquedaPopup = new Serie();
		serieBusquedaPopup.setCodigoSucursal(codigoSucursal);
		serieBusquedaPopup.setIdAfipTipoComprobante(idAfipTipoComprobante);
		serieBusquedaPopup.setTipoSerie(Constantes.SERIE_TIPO_SERIE_FACTURA);
		if(codigoEmpresa!=null && codigoEmpresa.length()>0){
			serieBusquedaPopup.setCodigoEmpresa(codigoEmpresa);
		}
		if(idAfipTipoComprobante!=null && idAfipTipoComprobante.longValue()>0){
			serieBusquedaPopup.setIdAfipTipoComprobante(idAfipTipoComprobante);
		}
		if(codigoSucursal!=null && codigoSucursal.length()>0){
			serieBusquedaPopup.setCodigoSucursal(codigoSucursal);
		}
		
		atributos.put("mapa", "seriesPopupMap");
		atributos.put("clase", "seriesPopupMap");
		definirPopupSerie(serieBusquedaPopup, atributos, val);
		
		return "popupBusquedaNuevo";
			
	}
	@RequestMapping(value="/popUpSeriePorCodigos.html")
	public String popupSeriePorCodigos(Map<String,Object> atributos,
			@RequestParam(value="codigoEmpresa", required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal", required=false) String codigoSucursal,
			@RequestParam(value="codigoAfipTipoComprobante", required=false) String codigoAfipTipoComprobante,
			@RequestParam(value="val", required=false) String val){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		
		//se crea el objeto serie para realizar la busqueda del popup de series
		Serie serieBusquedaPopup = new Serie();
		serieBusquedaPopup.setCodigoSucursal(codigoSucursal);
		serieBusquedaPopup.setCodigoAfipTipoComprobante(codigoAfipTipoComprobante);
		serieBusquedaPopup.setTipoSerie(Constantes.SERIE_TIPO_SERIE_FACTURA);
		if(codigoEmpresa!=null && codigoEmpresa.length()>0){
			serieBusquedaPopup.setCodigoEmpresa(codigoEmpresa);
		}
		if(codigoAfipTipoComprobante!=null && codigoAfipTipoComprobante.length()>0){
			serieBusquedaPopup.setCodigoAfipTipoComprobante(codigoAfipTipoComprobante);
		}
		if(codigoSucursal!=null && codigoSucursal.length()>0){
			serieBusquedaPopup.setCodigoSucursal(codigoSucursal);
		}
		
		atributos.put("mapa", "seriesPopupMap");
		atributos.put("clase", "seriesPopupMap");
		definirPopupSerie(serieBusquedaPopup, atributos, val);
		
		return "popupBusquedaNuevo";
			
	}
	@RequestMapping(value="/popUpSeleccionConceptoFacturable.html")
	public String popupSeleccionConceptoFacturable(Map<String,Object> atributos,
			@RequestParam(value="habilitado", required=false) Boolean habilitado,
			@RequestParam(value="fancyboxMode", required=false) Boolean fancyboxMode,
			@RequestParam(value="val", required=false) String val){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAsp();
		
		//se crea el objeto serie para realizar la busqueda del popup de series
		
		atributos.put("mapa", "conceptoFacturablePopupMap");
		atributos.put("clase", "conceptoFacturablePopupMap");
		definirPopupConcepto(atributos, val, habilitado);
		if(Boolean.TRUE.equals(fancyboxMode)){
			return "popupBusquedaNuevoFancyBox";
		}else{
			return "popupBusquedaNuevo";
		}
			
	}
	
	@RequestMapping(value="/popUpSeleccionLectura.html")
	public String popUpSeleccionLecturas(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=false) String empresaCodigo,
			@RequestParam(value="val",required=false) String valLectura,
			@RequestParam(value="utilizada",required=false) Boolean utilizada) {
		atributos.put("mapa", "lecturasPopupMap");
		atributos.put("clase", "lecturasPopupMap");
		definirPopupLecturas(atributos, valLectura,empresaCodigo,utilizada);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/seleccionDeposito.html")
	public void seleccionDeposito(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=true) String dependencia){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		if(!codigo.isEmpty() && !dependencia.isEmpty()){
			Deposito dep = depositoService.getByCodigoYSucursal(codigo, dependencia, clienteAsp);
			if(dep!=null){
				respuesta = dep.getDescripcion();
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
	
	@RequestMapping(value="/seleccionLectura.html")
	public void seleccionLectura(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=true) String dependencia){
		
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		
		Long codigoLong = null;
		Empresa empresa = null;
		if(codigo!= null && !"".equals(codigo) && !"undefined".equals(codigo))
		{
			codigoLong = Long.valueOf(codigo);
		}
		
		if(dependencia!= null && !"".equals(dependencia) && !"undefined".equals(dependencia))
		{
			empresa = empresaService.getByCodigo(dependencia, clienteAsp);
		}
		
		if(codigoLong!= null && codigoLong != 0){
			Lectura lec = lecturaService.obtenerPorCodigo(codigoLong, null, empresa, clienteAsp);
			if(lec!=null){
				respuesta = lec.getDescripcion();
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
	
	@RequestMapping(value="/popUpSeleccionTipoOperacion.html")
	public String popUpSeleccionTipoOperacion(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String descripcionTipoOperacion) {
		atributos.put("mapa", "tipoOperacionPopupMap");
		atributos.put("clase", "tipoOperacionPopupMap");
		definirPopupTipoOperacion(atributos, descripcionTipoOperacion);
		return "popupBusquedaNuevo";
		//return "popupBusquedaNuevoPaginado";
	}
	
	@RequestMapping(value="/popUpSeleccionTipoRequerimiento.html")
	public String popUpSeleccionTipoRequerimiento(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String valTipoRequerimiento) {
		atributos.put("mapa", "tipoRequerimientoPopupMap");
		atributos.put("clase", "tipoRequerimientoPopupMap");
		definirPopupTipoRequerimiento(atributos, valTipoRequerimiento);
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionSerie.html")
	public String popUpSeleccionSerie(Map<String,Object> atributos,
			@RequestParam(value="val",required=false) String valSerie) {
		atributos.put("mapa", "seriesPopupMap");
		atributos.put("clase", "seriesPopupMap");
		definirPopupSerie(atributos, valSerie, "I");
		return "popupBusquedaNuevo";
	}
	
	@RequestMapping(value="/popUpSeleccionSeriePorClienteYTipoComprobante.html")
	public String popUpSeleccionSeriePorClienteYTipoComprobante(
			Map<String,Object> atributos,
			@RequestParam(value="idAfipTipoComprobante",required=false) Long idAfipTipoComprobante,
			@RequestParam(value="idAfipTipoComprobante",required=false) String habilitadoStr,
			@RequestParam(value="codigoCliente",required=false) String codigoCliente,			
			@RequestParam(value="codigoSucursal",required=false) String codigoSucursal,
			@RequestParam(value="codigoEmpresa",required=false) String codigoEmpresa,
			@RequestParam(value="val",required=false) String valSerie,
			@RequestParam(value="fecha",required=false) String fecha) {
		ClienteAsp clienteAsp = obtenerClienteAsp();
		
		Empresa empresa = obtenerEmpresa();
		if(codigoEmpresa != null && codigoEmpresa.length()>0){
			empresa = empresaService.getByCodigo(codigoEmpresa, clienteAsp);
		}
		
		Sucursal sucursal = obtenerSucursal(); 
		if(codigoSucursal !=null && codigoSucursal.length()>0){
			sucursal = sucursalService.getByCodigo(codigoSucursal, empresa.getCodigo(), clienteAsp);
		}
		
		ClienteEmp clienteEmpBusqueda = new ClienteEmp();
		clienteEmpBusqueda.setCodigo(codigoCliente);
		clienteEmpBusqueda.setEmpresa(empresa);
		ClienteEmp clienteEmp = clienteEmpService.getByCodigoFactura(clienteEmpBusqueda, clienteAsp);
		
		AfipTipoComprobante afipTipoComprobante = null; 
		if(idAfipTipoComprobante!=null && idAfipTipoComprobante>0){
			afipTipoComprobante = afipTipoComprobanteService.obtenerPorId(idAfipTipoComprobante);
		}
		
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		Serie serieBusqueda = new Serie();
		try {
			serieBusqueda.setFechaParaCai(sd.parse(fecha));
		} catch (ParseException e1) {
			serieBusqueda.setFechaParaCai(new Date());
		}
		serieBusqueda.setDescripcion(valSerie);
		serieBusqueda.setEmpresa(empresa);
		serieBusqueda.setSucursal(sucursal);
		serieBusqueda.setAfipTipoComprobante(afipTipoComprobante);
		serieBusqueda.setDescripcion(valSerie);
		if(habilitadoStr != null && habilitadoStr.length()>0){
			Boolean habilitado = true;
			try{
				habilitado = Boolean.valueOf(habilitadoStr);
			}catch (Exception e){
				habilitado = true;
			}
			serieBusqueda.setHabilitado(habilitado);
		}else{
			serieBusqueda.setHabilitado(true);
		}
		if(clienteEmp!=null && clienteEmp.getAfipCondIva()!=null && idAfipTipoComprobante!=4){
			serieBusqueda.setCondIvaClientes(clienteEmp.getAfipCondIva().getAbreviatura());
		}
		atributos.put("mapa", "seriesPopupMap");
		atributos.put("clase", "seriesPopupMap");
		definirPopupSeriePorClienteYTipoComprobante(atributos, valSerie, serieBusqueda, clienteAsp);
		return "popupBusquedaNuevo";
	}
	
	
	@RequestMapping(value="/seleccionTipoOperacion.html")
	public void seleccionTipoOperacion(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=false) String dependencia){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		if(!codigo.isEmpty() && !dependencia.isEmpty()){
			TipoOperacion tip = tipoOperacionService.obtenerTipoOperacionPorCodigo(codigo, clienteAsp);
			if(tip!=null){
				respuesta = tip.getDescripcion();
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
	
	@RequestMapping(value="/seleccionTipoJerarquia.html")
	public void seleccionTipoJerarquia(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=false) String dependencia){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		if(!codigo.isEmpty() && !dependencia.isEmpty()){
			TipoJerarquia tip = tipoJerarquiaService.obtenerTipoJerarquiaPorCodigo(codigo, clienteAsp);
			if(tip!=null){
				respuesta = tip.getDescripcion();
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
	
	@RequestMapping(value="/seleccionJerarquia.html")
	public void seleccionJerarquia(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=false) String dependencia){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		if(!codigo.isEmpty() && !dependencia.isEmpty()){
			Jerarquia jer = jerarquiaService.obtenerJerarquiaPorCodigo(codigo, dependencia, clienteAsp);
			if(jer!=null){
				respuesta = jer.getDescripcion();
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
	@RequestMapping(value="/seleccionEmpleado.html")
	public void seleccionEmpleado(HttpServletResponse response,
			@RequestParam(value="codigo", required=true) String codigo,
			@RequestParam(value="dependencia", required=true) String dependencia){
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		if(!codigo.isEmpty() && !dependencia.isEmpty()){
			Empleado empleado = empleadoService.getByCodigo(codigo, dependencia, clienteAsp);
			if(empleado!=null){
				respuesta = empleado.getApellidoYNombre();
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
	
	@RequestMapping(value="/seleccionRequerimiento.html")
	public void seleccionRequerimiento(HttpServletResponse response,
			@RequestParam(value="codigo", required=false) String codigo,
			@RequestParam(value="codigoClienteEmp", required=false) String codigoClienteEmp,
			@RequestParam(value="codigoEmpresa", required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal", required=false) String codigoSucursal){
		
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		
		Long codigoLong = null;
		if(codigo!= null && !"".equals(codigo) && !"undefined".equals(codigo))
		{
			codigoLong = Long.valueOf(codigo);
		}
	
		if(codigoLong!= null && codigoLong != 0){
			Requerimiento req = requerimientoService.obtenerPorId(codigoLong, codigoClienteEmp, codigoEmpresa, codigoSucursal, clienteAsp);
			if(req!=null){
				respuesta = req.getRequerimientoStr();
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
	
	@RequestMapping(value="/seleccionRequerimientoNumero.html")
	public void seleccionRequerimientoNumero(HttpServletResponse response,
			@RequestParam(value="codigo", required=false) String codigo,
			@RequestParam(value="codigoClienteEmp", required=false) String codigoClienteEmp,
			@RequestParam(value="codigoEmpresa", required=false) String codigoEmpresa,
			@RequestParam(value="codigoSucursal", required=false) String codigoSucursal){
		
		String respuesta = "";
		ClienteAsp clienteAsp = obtenerClienteAspUser();
		
		Long codigoLong = null;
		if(codigo!= null && !"".equals(codigo) && !"undefined".equals(codigo))
		{
			codigoLong = Long.valueOf(codigo);
		}
	
		if(codigoLong!= null && codigoLong != 0){
			Requerimiento req = requerimientoService.obtenerPorNumero(codigoLong, codigoClienteEmp, codigoEmpresa, codigoSucursal, clienteAsp);
			if(req!=null){
				respuesta = req.getRequerimientoStr();
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
	
	@RequestMapping(value="/seleccionUsuario.html")
	public void seleccionUsuario(HttpServletResponse response,
			@RequestParam(value="codigo", required=false) String codigo){
		
		String respuesta = "";
		User lista = null;
		if(!"".equals(codigo))
			lista = userService.obtenerPorId(Long.valueOf(codigo));
		if(lista != null){
			if(lista.getPersona()!=null)
				respuesta = lista.getPersona().toString();
		}else{
			respuesta = "";
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
	
	
	@RequestMapping(value="/popUpTransporte.html")
	public String popupTransporte(Map<String,Object> atributos,
			@RequestParam(value="codigo", required=false) String codigoEmpresa,
			@RequestParam(value="val", required=false) String valTransporte){
		
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("mapa", "transportesPopupMap");
		atributos.put("clase", "transportesPopupMap");
		definirPopupTransporte(atributos, valTransporte, codigoEmpresa);
		return "popupBusquedaNuevo";
	}
	
	
	
/////////////////////METODOS DE SOPORTE/////////////////////////////
	private void definirPopupCliente(Map<String,Object> atributos, String descripcion, String empresaCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clientesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("empresa.razonSocial","formularioClienteDireccion.cliente.razonSocialEmpresa",false));
		campos.add(new CampoDisplayTag("razonSocialONombreYApellido","formularioClienteDireccion.cliente.nombreRazonSocial",false));
		campos.add(new CampoDisplayTag("nombreYApellido","formularioClienteDireccion.cliente.apellido",true));
		campos.add(new CampoDisplayTag("codigo","formularioClienteDireccion.cliente.apellido",true));
		clientesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		clientesPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup(descripcion, empresaCodigo, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup2", "razonSocialONombreYApellido");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clientesPopupMap.put("referenciaHtml", "codigoCliente"); 		
		//url que se debe consumir con ajax
		clientesPopupMap.put("urlRequest", "popUpSeleccionCliente.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clientesPopupMap.put("textoBusqueda", descripcion);		
		//codigo de la localización para el titulo del popup
		clientesPopupMap.put("tituloPopup", "textos.seleccion");
		//se setea el codigo del cliente seleccionado.
		clientesPopupMap.put("filterPopUp", empresaCodigo);
		//Agrego el mapa a los atributos
		atributos.put("clientesPopupMap", clientesPopupMap);
	}
	
	private void definirPopupSucursal(Map<String,Object> atributos, String descripcion, String empresaCodigo){
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
		sucursalesPopupMap.put("coleccionPopup", sucursalService.listarSucursalPopup(descripcion, empresaCodigo, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		sucursalesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		sucursalesPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		sucursalesPopupMap.put("referenciaHtml", "codigoSucursal"); 		
		//url que se debe consumir con ajax
		sucursalesPopupMap.put("urlRequest", "popUpSeleccionSucursal.html");
		//se vuelve a setear el texto utilizado para el filtrado
		sucursalesPopupMap.put("textoBusqueda", descripcion);
		//se setea el codigo del cliente seleccionado.
		sucursalesPopupMap.put("filterPopUp", empresaCodigo);
		//codigo de la localización para el titulo del popup
		sucursalesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("sucursalesPopupMap", sucursalesPopupMap);
	}
	
	private void definirPopupEmpresa(Map<String,Object> atributos, String descripcion){
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
		empresasPopupMap.put("coleccionPopup", empresaService.listarEmpresaPopup(descripcion, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		empresasPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		empresasPopupMap.put("referenciaHtml", "codigoEmpresa");
		//url que se debe consumir con ajax
		empresasPopupMap.put("urlRequest", "popUpSeleccionEmpresa.html");
		//se vuelve a setear el texto utilizado para el filtrado
		empresasPopupMap.put("textoBusqueda", descripcion);		
		//codigo de la localización para el titulo del popup
		empresasPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("empresasPopupMap", empresasPopupMap);
	}
	
	private void definirPopupElemento(Map<String,Object> atributos, HttpServletRequest request,
			String descripcionTipoElemento, 
			String codigoTipoElemento,
			String prefijoCodigoTipoElemento,
			String codigoCliente, 
			Boolean limitarDependencia,
			Long libresODistintoLoteId, 
			boolean esContenedor,Boolean cerrado){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> elementosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioElemento.datosElemento.codigo",false));
		campos.add(new CampoDisplayTag("tipoElemento.descripcion","formularioElemento.datosElemento.descripcion",false));		
		elementosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		
		//(+) Paginacion
			elementosPopupMap.put("pageSize", "20"); 	
			//cuenta la cantidad de resultados
			Integer size = elementoService.contarElementoPopupPaginado(obtenerClienteAsp(), 
					descripcionTipoElemento, codigoTipoElemento, prefijoCodigoTipoElemento,
					codigoCliente, limitarDependencia, libresODistintoLoteId, esContenedor, cerrado);
			
			elementosPopupMap.put("size", size);
			
			//Creo un objeto elemento para pasar los parametros de paginacion
			Elemento elemento = new Elemento();
			elemento.setTamañoPagina(20); //Debe ser igual a pageSize
			//objetoColeccion NO RENOMBRAR
			String nPaginaStr= request.getParameter((new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			//fieldOrder y sortOrder por ahora no se utilizan, pero se los crea para luego ser tenidos en cuenta
			String fieldOrder = request.getParameter( new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			elemento.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			elemento.setSortOrder(sortOrder);
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			elemento.setNumeroPagina(nPagina);
		//(-) Paginacion
		elemento.setCodigoTipoElemento(codigoTipoElemento);
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		elementosPopupMap.put("coleccionPopup", elementoService.listarElementoPopupPaginado(obtenerClienteAsp(), elemento,
				descripcionTipoElemento, codigoTipoElemento, prefijoCodigoTipoElemento,
				codigoCliente, limitarDependencia, libresODistintoLoteId, esContenedor, cerrado));
		if(esContenedor){
			//url que se debe consumir con ajax
			elementosPopupMap.put("urlRequest", "popUpSeleccionContenedor.html");
			//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
			elementosPopupMap.put("referenciaHtml", "codigoContenedor"); 	
		}else{
			elementosPopupMap.put("urlRequest", "popUpSeleccionElemento.html");
			//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
			elementosPopupMap.put("referenciaHtml", "codigoElemento"); 	
		}
		
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		elementosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		elementosPopupMap.put("referenciaPopup2", "tipoElemento.descripcion");
			
		//se vuelve a setear el texto utilizado para el filtrado
		elementosPopupMap.put("textoBusqueda", descripcionTipoElemento);		
		//codigo de la localización para el titulo del popup
		elementosPopupMap.put("tituloPopup", "textos.seleccion"); 
		//dependencia
		elementosPopupMap.put("filterPopUp", codigoCliente);
		//Agrego el mapa a los atributos
		if(esContenedor)
			atributos.put("contenedoresPopupMap", elementosPopupMap);
		else
			atributos.put("elementosPopupMap", elementosPopupMap);
	}
	
	private void definirPopupElementoEAN(Map<String,Object> atributos, HttpServletRequest request,
			String descripcionTipoElemento, 
			String codigoTipoElemento,
			String prefijoCodigoTipoElemento,
			String codigoCliente, 
			Boolean limitarDependencia,
			Long libresODistintoLoteId, 
			boolean esContenedor,Boolean cerrado){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> elementosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigoBarra","formularioElemento.datosElemento.codigo",true));
		campos.add(new CampoDisplayTag("codigo","formularioElemento.datosElemento.codigo",false));
		campos.add(new CampoDisplayTag("tipoElemento.descripcion","formularioElemento.datosElemento.descripcion",false));		
		elementosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		
		//(+) Paginacion
			elementosPopupMap.put("pageSize", "20"); 	
			//cuenta la cantidad de resultados
			Integer size = elementoService.contarElementoPopupPaginado(obtenerClienteAsp(), 
					descripcionTipoElemento, codigoTipoElemento, prefijoCodigoTipoElemento,
					codigoCliente, limitarDependencia, libresODistintoLoteId, esContenedor, cerrado);
			
			elementosPopupMap.put("size", size);
			
			//Creo un objeto elemento para pasar los parametros de paginacion
			Elemento elemento = new Elemento();
			elemento.setTamañoPagina(20); //Debe ser igual a pageSize
			//objetoColeccion NO RENOMBRAR
			String nPaginaStr= request.getParameter((new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			//fieldOrder y sortOrder por ahora no se utilizan, pero se los crea para luego ser tenidos en cuenta
			String fieldOrder = request.getParameter( new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			elemento.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			elemento.setSortOrder(sortOrder);
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			elemento.setNumeroPagina(nPagina);
		//(-) Paginacion
		elemento.setCodigoTipoElemento(codigoTipoElemento);
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		elementosPopupMap.put("coleccionPopup", elementoService.listarElementoPopupPaginado(obtenerClienteAsp(), elemento,
				descripcionTipoElemento, codigoTipoElemento, prefijoCodigoTipoElemento,
				codigoCliente, limitarDependencia, libresODistintoLoteId, esContenedor, cerrado));
		if(esContenedor){
			//url que se debe consumir con ajax
			elementosPopupMap.put("urlRequest", "popUpSeleccionContenedor.html");
			//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
			elementosPopupMap.put("referenciaHtml", "codigoContenedor"); 	
		}else{
			elementosPopupMap.put("urlRequest", "popUpSeleccionElementoEAN.html");
			//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
			elementosPopupMap.put("referenciaHtml", "codigoElemento"); 	
		}
		
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		elementosPopupMap.put("referenciaPopup", "codigoBarra");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		elementosPopupMap.put("referenciaPopup2", "tipoElemento.descripcion");
			
		//se vuelve a setear el texto utilizado para el filtrado
		elementosPopupMap.put("textoBusqueda", descripcionTipoElemento);		
		//codigo de la localización para el titulo del popup
		elementosPopupMap.put("tituloPopup", "textos.seleccion"); 
		//dependencia
		elementosPopupMap.put("filterPopUp", codigoCliente);
		//Agrego el mapa a los atributos
		if(esContenedor)
			atributos.put("contenedoresPopupMap", elementosPopupMap);
		else
			atributos.put("elementosPopupMap", elementosPopupMap);
	}
	
	private void definirPopupElemento2(Map<String,Object> atributos, HttpServletRequest request,
			String descripcionTipoElemento, 
			String codigoTipoElemento,
			String prefijoCodigoTipoElemento,
			String codigoCliente, 
			Boolean limitarDependencia,
			Long libresODistintoLoteId, 
			Boolean esContenedor){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> elementosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioElemento.datosElemento.codigo",false));
		campos.add(new CampoDisplayTag("tipoElemento.descripcion","formularioElemento.datosElemento.descripcion",false));		
		elementosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		
		//(+) Paginacion
			elementosPopupMap.put("pageSize", "20"); 	
			//cuenta la cantidad de resultados
			Integer size = elementoService.contarElementoPopupPaginado(obtenerClienteAsp(), 
					descripcionTipoElemento, codigoTipoElemento, prefijoCodigoTipoElemento,
					codigoCliente, limitarDependencia, libresODistintoLoteId, esContenedor);
			
			elementosPopupMap.put("size", size);
			
			//Creo un objeto elemento para pasar los parametros de paginacion
			Elemento elemento = new Elemento();
			elemento.setTamañoPagina(20); //Debe ser igual a pageSize
			//objetoColeccion NO RENOMBRAR
			String nPaginaStr= request.getParameter((new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			//fieldOrder y sortOrder por ahora no se utilizan, pero se los crea para luego ser tenidos en cuenta
			String fieldOrder = request.getParameter( new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			elemento.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			elemento.setSortOrder(sortOrder);
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			elemento.setNumeroPagina(nPagina);
		//(-) Paginacion
		elemento.setCodigoTipoElemento(codigoTipoElemento);
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		elementosPopupMap.put("coleccionPopup", elementoService.listarElementoPopupPaginado(obtenerClienteAsp(), elemento,
				descripcionTipoElemento, codigoTipoElemento, prefijoCodigoTipoElemento,
				codigoCliente, limitarDependencia, libresODistintoLoteId, esContenedor));
		
			elementosPopupMap.put("urlRequest", "popUpSeleccionElemento.html");
			//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
			elementosPopupMap.put("referenciaHtml", "codigoElemento"); 	
		
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		elementosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		elementosPopupMap.put("referenciaPopup2", "tipoElemento.descripcion");
			
		//se vuelve a setear el texto utilizado para el filtrado
		elementosPopupMap.put("textoBusqueda", descripcionTipoElemento);		
		//codigo de la localización para el titulo del popup
		elementosPopupMap.put("tituloPopup", "textos.seleccion"); 
		//dependencia
		elementosPopupMap.put("filterPopUp", codigoCliente);
		//Agrego el mapa a los atributos
		atributos.put("elementosPopupMap", elementosPopupMap);
	}
	
	private void definirPopupElementoEditable(Map<String,Object> atributos, HttpServletRequest request,
			String descripcionTipoElemento, 
			String codigoTipoElemento,
			String prefijoCodigoTipoElemento,
			String codigoCliente, 
			Boolean limitarDependencia,
			Long libresODistintoLoteId, 
			boolean esContenedor,Boolean cerrado,
			String cajaRespuesta){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> elementosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioElemento.datosElemento.codigo",false));
		campos.add(new CampoDisplayTag("tipoElemento.descripcion","formularioElemento.datosElemento.descripcion",false));		
		elementosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		
		//(+) Paginacion
			elementosPopupMap.put("pageSize", "20"); 	
			//cuenta la cantidad de resultados
			Integer size = elementoService.contarElementoPopupPaginado(obtenerClienteAsp(), 
					descripcionTipoElemento, codigoTipoElemento, prefijoCodigoTipoElemento,
					codigoCliente, limitarDependencia, libresODistintoLoteId, esContenedor, cerrado);
			
			elementosPopupMap.put("size", size);
			
			//Creo un objeto elemento para pasar los parametros de paginacion
			Elemento elemento = new Elemento();
			elemento.setTamañoPagina(20); //Debe ser igual a pageSize
			//objetoColeccion NO RENOMBRAR
			String nPaginaStr= request.getParameter((new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			if(nPaginaStr==null){
				nPaginaStr = (String)atributos.get((new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
			}
			//fieldOrder y sortOrder por ahora no se utilizan, pero se los crea para luego ser tenidos en cuenta
			String fieldOrder = request.getParameter( new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_SORT));
			elemento.setFieldOrder(fieldOrder);
			String sortOrder = request.getParameter(new ParamEncoder("objetoColeccion").encodeParameterName(TableTagParameters.PARAMETER_ORDER));
			elemento.setSortOrder(sortOrder);
			Integer nPagina = 1;		
			if(nPaginaStr!=null){
				nPagina = (Integer.parseInt(nPaginaStr));
			}
			elemento.setNumeroPagina(nPagina);
		//(-) Paginacion
		elemento.setCodigoTipoElemento(codigoTipoElemento);
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		elementosPopupMap.put("coleccionPopup", elementoService.listarElementoPopupPaginado(obtenerClienteAsp(), elemento,
				descripcionTipoElemento, codigoTipoElemento, prefijoCodigoTipoElemento,
				codigoCliente, limitarDependencia, libresODistintoLoteId, esContenedor, cerrado));
		if(esContenedor){
			//url que se debe consumir con ajax
			elementosPopupMap.put("urlRequest", "popUpSeleccionContenedor.html");
			//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
			elementosPopupMap.put("referenciaHtml", cajaRespuesta); 	
		}else{
			elementosPopupMap.put("urlRequest", "popUpSeleccionElemento.html");
			//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
			elementosPopupMap.put("referenciaHtml", cajaRespuesta); 	
		}
		
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		elementosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		elementosPopupMap.put("referenciaPopup2", "tipoElemento.descripcion");
			
		//se vuelve a setear el texto utilizado para el filtrado
		elementosPopupMap.put("textoBusqueda", descripcionTipoElemento);		
		//codigo de la localización para el titulo del popup
		elementosPopupMap.put("tituloPopup", "textos.seleccion"); 
		//dependencia
		elementosPopupMap.put("filterPopUp", codigoCliente);
		//Agrego el mapa a los atributos
		if(esContenedor)
			atributos.put("contenedoresPopupMap", elementosPopupMap);
		else
			atributos.put("elementosPopupMap", elementosPopupMap);
	}
	
	private void definirPopupClasificacionDocumental(Map<String, Object> atributos, String val,String clienteCodigo, HttpSession session) {
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clasificacionDocumentalPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioClasificacionDocumental.codigo",false));
		campos.add(new CampoDisplayTag("nombre","formularioClasificacionDocumental.nombre",false));		
		clasificacionDocumentalPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		
		Empleado empleado = (Empleado) session.getAttribute("empleadoSession");
		if(empleado!=null){
			List<ClasificacionDocumental> porEmpleado = new ArrayList<ClasificacionDocumental>(clasificacionDocumentalService.getByPersonalAsignado(empleado));
			clasificacionDocumentalPopupMap.put("coleccionPopup", porEmpleado);
		}
		else{
			//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
			clasificacionDocumentalPopupMap.put("coleccionPopup", clasificacionDocumentalService.listarClasificacionPopup(val, clienteCodigo, obtenerClienteAsp()));
		}
		
		
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clasificacionDocumentalPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clasificacionDocumentalPopupMap.put("referenciaPopup2", "nombre");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clasificacionDocumentalPopupMap.put("referenciaHtml", "codigoClasificacionDocumental"); 		
		//url que se debe consumir con ajax
		clasificacionDocumentalPopupMap.put("urlRequest", "popUpSeleccionClasificacionDocumental.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clasificacionDocumentalPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		clasificacionDocumentalPopupMap.put("tituloPopup", "textos.seleccion");
		//dependencia
		clasificacionDocumentalPopupMap.put("filterPopUp", clienteCodigo);
		//Agrego el mapa a los atributos
		atributos.put("clasificacionDocumentalPopupMap", clasificacionDocumentalPopupMap);
	}
	
	private void definirPopupClasDocumental(Map<String, Object> atributos, String val,String clienteCodigo,String destino,HttpSession session) {
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clasificacionDocumentalPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioClasificacionDocumental.codigo",false));
		campos.add(new CampoDisplayTag("nombre","formularioClasificacionDocumental.nombre",false));		
		clasificacionDocumentalPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		Empleado empleado = (Empleado) session.getAttribute("empleadoSession");
		if(empleado!=null){
			List<ClasificacionDocumental> porEmpleado = new ArrayList<ClasificacionDocumental>(clasificacionDocumentalService.getByPersonalAsignado(empleado));
			clasificacionDocumentalPopupMap.put("coleccionPopup", porEmpleado);
		}
		else{
			clasificacionDocumentalPopupMap.put("coleccionPopup", clasificacionDocumentalService.listarClasificacionPopup(val, clienteCodigo, obtenerClienteAsp()));
		}
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clasificacionDocumentalPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clasificacionDocumentalPopupMap.put("referenciaPopup2", "nombre");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clasificacionDocumentalPopupMap.put("referenciaHtml", "codigoClasificacionDocumental"); 	
		if(destino!=null && !destino.trim().equalsIgnoreCase(""))
			clasificacionDocumentalPopupMap.put("referenciaHtml", destino); 	
		//url que se debe consumir con ajax
		clasificacionDocumentalPopupMap.put("urlRequest", "popUpSelClasificacionDocumental.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clasificacionDocumentalPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		clasificacionDocumentalPopupMap.put("tituloPopup", "textos.seleccion");
		//dependencia
		clasificacionDocumentalPopupMap.put("filterPopUp", clienteCodigo);
		//Agrego el mapa a los atributos
		atributos.put("clasificacionDocumentalPopupMap", clasificacionDocumentalPopupMap);
	}
	
	private void definirPopupDescripcionClasificacionDocumental(Map<String, Object> atributos,
			Integer codigoClasificacionDocumental, String descripcion, String codigoCliente) {
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> descripcionesClasificacionDocumentalPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("descripcion","formularioClasificacionDocumental.descripcion",false));
		descripcionesClasificacionDocumentalPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		descripcionesClasificacionDocumentalPopupMap.put("coleccionPopup", referenciaService.listarDescripcionesPopup( codigoClasificacionDocumental,codigoCliente,descripcion, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		descripcionesClasificacionDocumentalPopupMap.put("referenciaPopup", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		descripcionesClasificacionDocumentalPopupMap.put("referenciaHtml", "descripcion"); 		
		//url que se debe consumir con ajax
		descripcionesClasificacionDocumentalPopupMap.put("urlRequest", "popUpSeleccionDescripcionClasificacionDocumental.html?codigo2="+codigoCliente);
		//se vuelve a setear el texto utilizado para el filtrado
		descripcionesClasificacionDocumentalPopupMap.put("textoBusqueda", descripcion);		
		//codigo de la localización para el titulo del popup
		descripcionesClasificacionDocumentalPopupMap.put("tituloPopup", "textos.seleccion");
		//dependencia
		descripcionesClasificacionDocumentalPopupMap.put("filterPopUp", codigoClasificacionDocumental);
		//Agrego el mapa a los atributos
		atributos.put("descripcionesPopupMap", descripcionesClasificacionDocumentalPopupMap);
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
		depositosPopupMap.put("urlRequest", "popUpSeleccionDeposito.html");
		//se vuelve a setear el texto utilizado para el filtrado
		depositosPopupMap.put("textoBusqueda", val);
		//codigo de la localización para el titulo del popup
		depositosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("depositosPopupMap", depositosPopupMap);
	}
	
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
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
		seriesPopupMap.put("urlRequest", "popUpSerie.html");
		//se vuelve a setear el texto utilizado para el filtrado
		seriesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		seriesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("seriesPopupMap", seriesPopupMap);
	}
	
	private void definirPopupSerie(Map<String,Object> atributos, String val, String tipoSerie){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> seriesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioEmpresa.datosEmpresa.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioEmpresa.datosEmpresa.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		seriesPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		seriesPopupMap.put("coleccionPopup", serieService.listarSeriePopup(val, tipoSerie, null, obtenerClienteAsp(), null));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup2", "prefijo");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		seriesPopupMap.put("referenciaHtml", "codigoSerie");
		//url que se debe consumir con ajax
		seriesPopupMap.put("urlRequest", "popUpSeleccionSerie.html");
		//se vuelve a setear el texto utilizado para el filtrado
		seriesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		seriesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("seriesPopupMap", seriesPopupMap);
	}
	
	private void definirPopupSeriePorClienteYTipoComprobante(Map<String,Object> atributos, String val, Serie serieBusqueda, ClienteAsp clienteAsp ){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> seriesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioEmpresa.datosEmpresa.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioEmpresa.datosEmpresa.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		seriesPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		seriesPopupMap.put("coleccionPopup", serieService.listarSerieFiltradasPopup(serieBusqueda, clienteAsp));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		seriesPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		seriesPopupMap.put("referenciaHtml", "codigoSerie");
		//url que se debe consumir con ajax
		seriesPopupMap.put("urlRequest", "popUpSeleccionSeriePorClienteYTipoComprobante.html");
		//se vuelve a setear el texto utilizado para el filtrado
		seriesPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		seriesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("seriesPopupMap", seriesPopupMap);
	}
	
	private void definirPopupLecturas(Map<String,Object> atributos, String val, String codigoEmpresa){
		definirPopupLecturas(atributos, val, codigoEmpresa,null);
	}
	
	private void definirPopupLecturas(Map<String,Object> atributos, String val, String codigoEmpresa,Boolean utilizada){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> lecturasPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioLectura.datosLectura.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioLectura.datosLectura.descripcion",false));		
		lecturasPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		ClienteAsp casp= obtenerClienteAspUser();
		Empresa emp = null;
		if(codigoEmpresa != null){
			emp = empresaService.getByCodigo(codigoEmpresa, casp);
		}
		lecturasPopupMap.put("coleccionPopup", lecturaService.listarLecturaPopup(val, casp, emp, utilizada ));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		lecturasPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		lecturasPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		lecturasPopupMap.put("referenciaHtml", "codigoLectura"); 		
		//url que se debe consumir con ajax
		lecturasPopupMap.put("urlRequest", "popUpSeleccionLectura.html");
		//se vuelve a setear el texto utilizado para el filtrado
		lecturasPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		lecturasPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("lecturasPopupMap", lecturasPopupMap);
	}
	
	private void definirPopupTipoOperacion(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> tipoOperacionPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioDeposito.datosDeposito.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioDeposito.datosDeposito.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		tipoOperacionPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		tipoOperacionPopupMap.put("coleccionPopup", tipoOperacionService.listarTipoOperacionPopup(val, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		tipoOperacionPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		tipoOperacionPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		tipoOperacionPopupMap.put("referenciaHtml", "codigoTipoOperacion"); 		
		//url que se debe consumir con ajax		
		tipoOperacionPopupMap.put("urlRequest", "popUpSeleccionTipoOperacion.html");
		//se vuelve a setear el texto utilizado para el filtrado
		tipoOperacionPopupMap.put("textoBusqueda", val);
		//codigo de la localización para el titulo del popup
		tipoOperacionPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("tipoOperacionPopupMap", tipoOperacionPopupMap);
	}
	
	private void definirPopupTipoRequerimiento(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioTipoRequerimiento.datos.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioTipoRequerimiento.datos.descripcion",false));
		campos.add(new CampoDisplayTag("plazo","formularioTipoRequerimiento.datos.plazo",false));		
		map.put("campos", campos);
		
		//Coleccion de objetos a utilizar en el popup
		List<TipoRequerimiento> listaTipoReq = tipoRequerimientoService.listarTipoRequerimientoPopup(val, obtenerClienteAsp());
		
		Empleado usuario = null;
		try{
			usuario = (Empleado)obtenerUser();
		}catch(ClassCastException e){
			
		}
		
		if(usuario!=null && listaTipoReq!=null && listaTipoReq.size()>0)
		{
			if(usuario.getClienteEmp().getId().longValue() == 20042)
			{
			 if (usuario.getId().longValue() != 60592 )
			{
				List<TipoRequerimiento> listaAux = new ArrayList<TipoRequerimiento>();
				for(TipoRequerimiento tipo: listaTipoReq)
				{
					
					if(tipo.getId().longValue()== 1   ||  tipo.getId().longValue()== 14 ||  tipo.getId().longValue()== 22 ){
					
						listaAux.add(tipo);
						
					}
				}
				listaTipoReq = listaAux;
			}
			 else {
					List<TipoRequerimiento> listaAux = new ArrayList<TipoRequerimiento>();
					for(TipoRequerimiento tipo: listaTipoReq)
					{
						if(tipo.getId().longValue()== 1 ||  tipo.getId().longValue()== 4 ||  tipo.getId().longValue()== 5 ||  tipo.getId().longValue()== 8 ||  tipo.getId().longValue()== 16 ||  tipo.getId().longValue()== 14 ||  tipo.getId().longValue()== 22){
							listaAux.add(tipo);
						}
					}
					listaTipoReq = listaAux;
					
				}
			}
		}
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		map.put("coleccionPopup", listaTipoReq);
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "tipoRequerimientoCod"); 		
		//url que se debe consumir con ajax		
		map.put("urlRequest", "popUpSeleccionTipoRequerimiento.html");
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("tipoRequerimientoPopupMap", map);
	}
	
	private void definirPopupConcepto(Map<String,Object> atributos, String val, Boolean habilitado){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioConceptoFacturable.datos.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioConceptoFacturable.datos.descripcion",false));
		campos.add(new CampoDisplayTag("tipo.descripcion","formularioConceptoFacturable.datos.tipo",false));
		campos.add(new CampoDisplayTag("habilitado","formularioConceptoFacturable.datos.habilitado",false));
		campos.add(new CampoDisplayTag("generaStock","formularioConceptoFacturable.datos.generaStock",false));
		campos.add(new CampoDisplayTag("costo","formularioConceptoFacturable.datos.costo",false));
		campos.add(new CampoDisplayTag("precioBase","formularioConceptoFacturable.datos.precioBase",false));
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		map.put("coleccionPopup", conceptoFacturableService.listarConceptosFacturablesPopup(val, obtenerClienteAspUser(), habilitado));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "codigoConcepto"); 		
		//url que se debe consumir con ajax		
		
		map.put("urlRequest", "popUpSeleccionConceptoFacturable.html" );	
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("conceptoFacturablePopupMap", map);
	}
	
	
	private void definirPopupListaPrecios(Map<String,Object> atributos, String val, Boolean habilitado){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioListaPrecios.datos.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioListaPrecios.datos.descripcion",false));
		campos.add(new CampoDisplayTag("tipoVariacion.descripcion","formularioListaPrecios.datos.tipoVariacion",false));	
		campos.add(new CampoDisplayTag("valor","formularioListaPrecios.datos.valor",false));
		campos.add(new CampoDisplayTag("habilitada","formularioListaPrecios.datos.habilitada",false));
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		map.put("coleccionPopup", listaPreciosService.listarListasPreciosPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "listaPreciosCodigo"); 		
		//url que se debe consumir con ajax
		
		map.put("urlRequest", "popUpSeleccionListaPrecios.html");				
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("listaPreciosPopupMap", map);
	}

	private void definirPopupListaPreciosPorConceptos(Map<String,Object> atributos, String val, Boolean habilitado,String codigoClienteEmp, String codigoEmpresa, String codigoConceptoFacturable){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioListaPrecios.datos.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioListaPrecios.datos.descripcion",false));
		campos.add(new CampoDisplayTag("tipoVariacion.descripcion","formularioListaPrecios.datos.tipoVariacion",false));	
		campos.add(new CampoDisplayTag("valor","formularioListaPrecios.datos.valor",false));
		campos.add(new CampoDisplayTag("habilitada","formularioListaPrecios.datos.habilitada",false));
		map.put("campos", campos);
		ClienteAsp clienteAsp = obtenerClienteAsp();
		ConceptoFacturable concepto = conceptoFacturableService.obtenerConceptoFacturablePorCodigo(codigoConceptoFacturable, clienteAsp); 
		ClienteEmp clienteEmp = clienteEmpService.getByCodigo(codigoClienteEmp, codigoEmpresa, clienteAsp);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		map.put("coleccionPopup", listaPreciosService.listarPorConceptoFacturable(concepto, clienteEmp, clienteAsp));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "listaPreciosCodigo"); 		
		//url que se debe consumir con ajax
		
		map.put("urlRequest", "popUpSeleccionListaPrecios.html");				
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("listaPreciosPopupMap", map);
	}
	
	private void definirPopupTipoElemento(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> tipoElementosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioTipoElemento.datosTipoElemento.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioTipoElemento.datosTipoElemento.descripcion",false));		
		tipoElementosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		tipoElementosPopupMap.put("coleccionPopup", tipoElementoService.listarTipoElementoPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		tipoElementosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		tipoElementosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		tipoElementosPopupMap.put("referenciaHtml", "codigoTipoElemento"); 		
		//url que se debe consumir con ajax
		tipoElementosPopupMap.put("urlRequest", "popUpSeleccionTipoElemento.html");	
		//se vuelve a setear el texto utilizado para el filtrado
		tipoElementosPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		tipoElementosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("tipoElementosPopupMap", tipoElementosPopupMap);
	}
	
	private void definirPopupTipoOperaciones(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> tipoOperacionPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioDeposito.datosDeposito.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioDeposito.datosDeposito.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		tipoOperacionPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		tipoOperacionPopupMap.put("coleccionPopup", tipoOperacionService.listarTipoOperacionPopup(val, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		tipoOperacionPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		tipoOperacionPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		tipoOperacionPopupMap.put("referenciaHtml", "codigoTipoOperacion"); 		
		//url que se debe consumir con ajax		
		tipoOperacionPopupMap.put("urlRequest", "popUpSeleccionTipoOperacion.html");
		//se vuelve a setear el texto utilizado para el filtrado
		tipoOperacionPopupMap.put("textoBusqueda", val);
		//codigo de la localización para el titulo del popup
		tipoOperacionPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("tipoOperacionPopupMap", tipoOperacionPopupMap);
	}
	
	private void definirPopupTipoElementoPrefijo(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> tipoElementosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("prefijoCodigo","formularioTipoElemento.datosTipoElemento.prefijoCodigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioTipoElemento.datosTipoElemento.descripcion",false));		
		tipoElementosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		tipoElementosPopupMap.put("coleccionPopup", tipoElementoService.listarTipoElementoPopup(val, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		tipoElementosPopupMap.put("referenciaPopup", "prefijoCodigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		tipoElementosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		tipoElementosPopupMap.put("referenciaHtml", "prefijoCodigoTipoElemento"); 		
		//url que se debe consumir con ajax
		tipoElementosPopupMap.put("urlRequest", "popUpSeleccionTipoElemento.html");	
		//se vuelve a setear el texto utilizado para el filtrado
		tipoElementosPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		tipoElementosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("tipoElementosPopupMap", tipoElementosPopupMap);
	}
	
	private void definirPopupRemito(Map<String, Object> atributos, String val,String clienteCodigo) {
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> remitoPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioRemito.datosRemito.fechaEmision",true));
		campos.add(new CampoDisplayTag("fechaEmisionStr","formularioRemito.datosRemito.fechaEmision",false));
		campos.add(new CampoDisplayTag("letraYNumeroComprobante","formularioRemito.datosRemito.numero",false));		
		remitoPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		remitoPopupMap.put("coleccionPopup", remitoService.listarRemitoPopup(val, clienteCodigo, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		remitoPopupMap.put("referenciaPopup", "id");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		remitoPopupMap.put("referenciaPopup2", "letraYNumeroComprobante");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		remitoPopupMap.put("referenciaHtml", "codigoRemito"); 		
		//url que se debe consumir con ajax
		remitoPopupMap.put("urlRequest", "popUpSeleccionRemito.html");
		//se vuelve a setear el texto utilizado para el filtrado
		remitoPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		remitoPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("remitoPopupMap", remitoPopupMap);
	}
	
	private void definirPopupDirecciones(Map<String,Object> atributos, String val, String clienteCodigo){
		ClienteEmp cliente = null;
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> direccionesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioEmpleado.datosEmpleado.codigo",true));
		campos.add(new CampoDisplayTag("direccion.calle","formularioEmpleado.datosEmpleado.direccion.calle",false));
		campos.add(new CampoDisplayTag("direccion.numero","formularioEmpleado.datosEmpleado.direccion.numero",false));
		campos.add(new CampoDisplayTag("direccion.barrio.nombre","formularioEmpleado.datosEmpleado.direccion.barrio",false));
		campos.add(new CampoDisplayTag("direccion.barrio.localidad.nombre","formularioEmpleado.datosEmpleado.direccion.localidad",false));
		campos.add(new CampoDisplayTag("direccion.barrio.localidad.provincia.nombre","formularioEmpleado.datosEmpleado.direccion.provincia",false));
		//campos.add(new CampoDisplayTag("descripcion","formularioSucursal.datosSucursal.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		direccionesPopupMap.put("campos", campos);
		if(clienteCodigo != "" && clienteCodigo != null)
		{
			cliente = clienteEmpService.getByCodigo(clienteCodigo);
		}
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		direccionesPopupMap.put("coleccionPopup", clienteDireccionService.listarDireccionesPopup(val, cliente, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		direccionesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		direccionesPopupMap.put("referenciaPopup2", "direccion.calle"+"direccion.numero");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		direccionesPopupMap.put("referenciaHtml", "codigoDireccion"); 		
		//url que se debe consumir con ajax
		direccionesPopupMap.put("urlRequest", "popUpSeleccionDirecciones.html");
		//se vuelve a setear el texto utilizado para el filtrado
		direccionesPopupMap.put("textoBusqueda", val);
		//se setea el codigo del cliente seleccionado.
		direccionesPopupMap.put("filterPopUp", clienteCodigo);
		//codigo de la localización para el titulo del popup
		direccionesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("direccionesPopupMap", direccionesPopupMap);
	}
	
	private void definirPopupClienteEmpresaDefecto(Map<String,Object> atributos, String descripcion, String empresaCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> clientesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("razonSocialONombreYApellido","formularioClienteConcepto.cliente",false));
		campos.add(new CampoDisplayTag("empresa.razonSocial.razonSocial","formularioClienteConcepto.cliente.empresa",false));
		campos.add(new CampoDisplayTag("nombreYApellido","formularioClienteConcepto.cliente.apellido",true));
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.cliente.apellido",true));
		clientesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		clientesPopupMap.put("coleccionPopup", clienteEmpService.listarClientesPopup(descripcion, empresaCodigo, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		clientesPopupMap.put("referenciaPopup2", "nombreYApellido");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		clientesPopupMap.put("referenciaHtml", "clienteCodigo"); 		
		//url que se debe consumir con ajax
		clientesPopupMap.put("urlRequest", "popUpSeleccionClienteEmpresaDefecto.html");
		//se vuelve a setear el texto utilizado para el filtrado
		clientesPopupMap.put("textoBusqueda", descripcion);		
		//codigo de la localización para el titulo del popup
		clientesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("clientesPopupMap", clientesPopupMap);
	}
	
	private void definirPopupEmpleadoSolicitante(Map<String,Object> atributos, String val, String clienteCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> empleadosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioRemito.codigo",false));
		campos.add(new CampoDisplayTag("nombreYApellido","formularioEmpleado.cliente.nombre",false));

		
		//Coleccion de objetos a utilizar en el popup
		empleadosPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		empleadosPopupMap.put("coleccionPopup", empleadoService.listarEmpleadoPopup(val, obtenerClienteAsp(), clienteCodigo, true));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		empleadosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		empleadosPopupMap.put("referenciaPopup2", "nombreYApellido");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		empleadosPopupMap.put("referenciaHtml", "codigoPersonal");
		//url que se debe consumir con ajax
		empleadosPopupMap.put("urlRequest", "popUpSeleccionEmpleadoSolicitante.html");
		//se vuelve a setear el texto utilizado para el filtrado
		empleadosPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		empleadosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("personalPopupMap", empleadosPopupMap);
	}
	
	private void definirPopupEmpleadoAutorizante(Map<String,Object> atributos, String val, String clienteCodigo){
		//creo un hashmap parea almacenar los parametros del popup
		Map<String,Object> empleadosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioRemito.codigo",false));
		campos.add(new CampoDisplayTag("nombreYApellido","formularioEmpleado.cliente.nombre",false));
		//Coleccion de objetos a utilizar en el popup
		empleadosPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		empleadosPopupMap.put("coleccionPopup", empleadoService.listarEmpleadoPopup(val, obtenerClienteAspUser(), clienteCodigo, true));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		empleadosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		empleadosPopupMap.put("referenciaPopup2", "nombreYApellido");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		empleadosPopupMap.put("referenciaHtml", "codigoPersonalAutorizante");
		//url que se debe consumir con ajax
		empleadosPopupMap.put("urlRequest", "popUpSeleccionEmpleadoAutorizante.html");
		//se vuelve a setear el texto utilizado para el filtrado
		empleadosPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		empleadosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("personalPopupMapAutorizante", empleadosPopupMap);
	}
	
	private void definirPopupListaPreciosPorCliente(Map<String,Object> atributos, String val, String clienteCodigo){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> listaPreciosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.listaPrecio.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioClienteConcepto.listaPrecio.descripcion",false));
		campos.add(new CampoDisplayTag("valor","formularioClienteConcepto.listaPrecio.valor",false));
		listaPreciosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		ClienteEmp cliente = new ClienteEmp();
		cliente.setCodigo(clienteCodigo);
		cliente.setCodigoEmpresa(obtenerEmpresa().getCodigo());
		ClienteEmp clienteEmp = clienteEmpService.getByCodigo(cliente, obtenerClienteAspUser());
		listaPreciosPopupMap.put("coleccionPopup", listaPreciosService.listarListasPreciosPopup(val, obtenerClienteAspUser(), new Boolean(true)));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		listaPreciosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		listaPreciosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		listaPreciosPopupMap.put("referenciaHtml", "listaPrecioCodigo"); 		
		//url que se debe consumir con ajax
		listaPreciosPopupMap.put("urlRequest", "popUpSeleccionListaPreciosPorCliente.html");
		//se vuelve a setear el texto utilizado para el filtrado
		listaPreciosPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		listaPreciosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("listaPreciosPopupMap", listaPreciosPopupMap);
	}
	

	private void definirPopupTipoJerarquia(Map<String,Object> atributos, String descripcion){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> tiposJerarquiaPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioTipoJerarquia.datos.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioTipoJerarquia.datos.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		tiposJerarquiaPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		tiposJerarquiaPopupMap.put("coleccionPopup", tipoJerarquiaService.listarTipoJerarquiaPopup(descripcion, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		tiposJerarquiaPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		tiposJerarquiaPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		tiposJerarquiaPopupMap.put("referenciaHtml", "codigoTipoJerarquia");
		//url que se debe consumir con ajax
		tiposJerarquiaPopupMap.put("urlRequest", "popUpSeleccionTipoJerarquia.html");
		//se vuelve a setear el texto utilizado para el filtrado
		tiposJerarquiaPopupMap.put("textoBusqueda", descripcion);		
		//codigo de la localización para el titulo del popup
		tiposJerarquiaPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("tiposJerarquiaPopupMap", tiposJerarquiaPopupMap);
	}
	
	private void definirPopupJerarquia(Map<String,Object> atributos, String codigoTipoJerarquia, String descripcion){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> jerarquiasPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioTipoJerarquia.datos.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioTipoJerarquia.datos.descripcion",false));
		//Coleccion de objetos a utilizar en el popup
		jerarquiasPopupMap.put("campos", campos);		
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		jerarquiasPopupMap.put("coleccionPopup", jerarquiaService.listarJerarquiasPopup(descripcion, codigoTipoJerarquia, obtenerClienteAsp()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		jerarquiasPopupMap.put("referenciaPopup", "id");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		jerarquiasPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		jerarquiasPopupMap.put("referenciaHtml", "codigoJerarquia");
		//url que se debe consumir con ajax
		jerarquiasPopupMap.put("urlRequest", "popUpSeleccionJerarquia.html");
		//se vuelve a setear el texto utilizado para el filtrado
		jerarquiasPopupMap.put("textoBusqueda", descripcion);		
		//codigo de la localización para el titulo del popup
		jerarquiasPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("jerarquiasPopupMap", jerarquiasPopupMap);
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
	
	private void definirPopupRequerimiento(Map<String,Object> atributos, String val, String codigoClienteEmp, String codigoEmpresa, String codigoSucursal){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> map = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("numero","formularioListaPrecios.datos.codigo",false));
		campos.add(new CampoDisplayTag("requerimientoStr","formularioRequerimiento.nombreRequerimiento",false));
		campos.add(new CampoDisplayTag("sucursal.descripcion","formularioRequerimiento.sucursal",false));
		campos.add(new CampoDisplayTag("clienteEmp.razonSocialONombreYApellido","formularioRequerimiento.cliente",false));
		map.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		map.put("coleccionPopup", requerimientoService.listarRequerimientoPopup(val, obtenerClienteAsp(), codigoClienteEmp, codigoEmpresa, codigoSucursal));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup", "numero");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		map.put("referenciaPopup2", "requerimientoStr");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		map.put("referenciaHtml", "codigoRequerimiento"); 		
		//url que se debe consumir con ajax
		map.put("urlRequest", "popUpSeleccionRequerimiento.html");				
		//se vuelve a setear el texto utilizado para el filtrado
		map.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		map.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("requerimientosPopupMap", map);
	}
	
	private void definirPopupUsuario(Map<String,Object> atributos, String nombreOApellido, String field){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> responsablesPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("id","formularioPosicionLibre.posicionLibreDetalle.lectura.codigo",false));
		campos.add(new CampoDisplayTag("persona","formularioMovimiento.responsable",false));		
		responsablesPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		responsablesPopupMap.put("coleccionPopup", userService.listarPopup(nombreOApellido, obtenerClienteAspUser()));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		responsablesPopupMap.put("referenciaPopup", "id");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		responsablesPopupMap.put("referenciaPopup2", "persona");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		responsablesPopupMap.put("referenciaHtml", field!=null && !field.equals("")?field:"codigoUsuario"); 		
		//url que se debe consumir con ajax
		responsablesPopupMap.put("urlRequest", "popUpSeleccionUsuario.html");
		//se vuelve a setear el texto utilizado para el filtrado
		responsablesPopupMap.put("textoBusqueda", nombreOApellido);		
		//codigo de la localización para el titulo del popup
		responsablesPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("responsablesPopupMap", responsablesPopupMap);
	}
	
	public boolean verificarClasificacionAsignadaEmpleado(ClasificacionDocumental clasificacionAsignada,ClasificacionDocumental clasificacionVerificar){
		boolean asignada = false;
		if(clasificacionAsignada!=null)
		{
			if(clasificacionAsignada.getId().longValue() == clasificacionVerificar.getId().longValue())
				return true;
			else if(clasificacionAsignada.getNodosHijos()!=null && clasificacionAsignada.getNodosHijos().size()>0)
			{
				for(ClasificacionDocumental docHija:clasificacionAsignada.getNodosHijos())
				{
					if(verificarClasificacionAsignadaEmpleado(docHija,clasificacionVerificar)){
						asignada = true;
						break;
					}
						
							
				}
			}
		}
		return asignada;
	}
	
	private User obtenerUser(){
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	private ClienteAsp obtenerClienteAsp(){
		return obtenerUser().getCliente();
	}
	
	private Sucursal obtenerSucursal(){
		User usuario = obtenerUser();
		return ((PersonaFisica)usuario.getPersona()).getSucursalDefecto();
	}
	
	private Empresa obtenerEmpresa(){
		User usuario = obtenerUser();
		return ((PersonaFisica)usuario.getPersona()).getEmpresaDefecto();
	}
}
