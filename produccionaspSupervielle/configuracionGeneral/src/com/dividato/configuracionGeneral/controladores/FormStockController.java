package com.dividato.configuracionGeneral.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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

import com.dividato.configuracionGeneral.validadores.StockValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.configuraciongeneral.interfaz.DepositoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.StockService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Stock;
import com.security.modelo.configuraciongeneral.StockAcumulado;
import com.security.modelo.seguridad.User;
import com.security.recursos.Configuracion;
import com.security.utils.CampoDisplayTag;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;


/**
 * Controlador que se utiliza para los servicios asociados al formulario 
 * de User.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author Gabriel Mainero
 * @modificado Gonzalo Noriega (30/06/2011)
 *
 */
@Controller
@RequestMapping(
		value=
			{
				"/precargaFormularioStock.html",
				"/guardarActualizarStock.html"
			}
		)
public class FormStockController {
	
	private ListaStockController listaStockController;
	private StockService stockService;
	private StockValidator validator;
	private ConceptoFacturableService conceptoFacturableService;
	private DepositoService depositoService;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	
	/**
	 * Setea el servicio de Stock.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto StockService.
	 * La clase StockImp implementa Stock y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param stockService
	 */
	@Autowired
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}
	@Autowired
	public void setValidator(StockValidator validator) {
		this.validator = validator;
	}
	
	@Autowired
	public void setListaStockController(ListaStockController listaStockController) {
		this.listaStockController = listaStockController;
	}
	
	@Autowired
	public void setConceptoFacturableService(ConceptoFacturableService conceptoFacturableService) {
		this.conceptoFacturableService = conceptoFacturableService;
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
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Mapea la URL /precargaFormularioUser.html para que ejecute
	 * este método cuando venga GET.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de cargar en request y session los datos necesarios
	 * para mostrar el formulario de Stock, ya sea nuevo, consulta o modificación.
	 * 
	 * @param accion parámetro que se recibe del request 
	 * (Observar la anotación @RequestParam)
	 * @param idUser parámetro que se recibe del request
	 * (Observar la anotación @RequestParam)
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioStock" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/precargaFormularioStock.html",
			method = RequestMethod.GET
		)
	public String precargaFormularioStock(
			@RequestParam(value="accion",required=false) String accion,
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="val", required=false) String valConcepto,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="val", required=false) String valSucursal,
			@RequestParam(value="val", required=false) String valEmpresa,
			@RequestParam(value="empresaCodigo", required=false) String empresaCodigo,
			@RequestParam(value="sucursalCodigo", required=false) String sucursalCodigo,
			@RequestParam(value="codigoConcepto",required=false) String codigoConcepto,
			@RequestParam(value="codigoDeposito",required=false) String codigoDeposito,
			Map<String,Object> atributos) {
		
		if(accion.equals("VER_DETALLE")){
			Stock stock = new Stock();
			stock.setCodigoConcepto(codigoConcepto);
			stock.setCodigoDeposito(codigoDeposito);
			List<StockAcumulado> listaStock = null;
			listaStock =stockService.listarStockAcumulado(stock, obtenerClienteAspUser());
			//seteo el detalle de los ajustes de stock
			atributos.put("stocks", listaStock);
		}else if(accion.equals("NUEVO")){
			Stock stock = (Stock)atributos.get("stockFormulario");
			if(stock == null){
				stock = new Stock();
			}
			stock.setCodigoDeposito(codigoDeposito);
			stock.setTipoMovimiento("Ingreso");
			atributos.put("fechaAux", Configuracion.formatoFechaFormularios.format(new Date()));
			//seteo el detalle de los ajustes de stock
			atributos.put("stockFormulario", stock);
		}
		//seteo el id del cliente
		atributos.put("clienteId", obtenerClienteAspUser().getId());			
		//Preparo el popup de Empresas
		definirPopupEmpresa(atributos, valEmpresa);
		//Preparo el popup de Sucursales
		definirPopupSucursal(atributos, valSucursal, empresaCodigo);
		//Preparo el popup de Depositos
		definirPopupDepositos(atributos, valDeposito, sucursalCodigo);		
		//Preparo el popup de Conceptos
		definirPopupConceptos(atributos, valConcepto);
		
		
		atributos.put("accion", accion);
		
		return "formularioStock";
	}
	
	
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de guardar en base de datos Stock.
	 * 
	 * @param Stock user a guardar.
	 * Observar la anotación @ModelAttribute que le indica a SPRING
	 * que debe intentar llenar el objeto Stock con los parámetros del 
	 * request.
	 * @param session es la misma sesión que usabamos con los Servlets.
	 * @param atributos son los atributos del request
	 * @return "formularioStock" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@RequestMapping(
			value="/guardarActualizarStock.html",
			method= RequestMethod.POST
		)
	public String guardarStock(
			@RequestParam(value="accion",required=false) String accion,
			@ModelAttribute("stockFormulario") final Stock stock,
			BindingResult result,
			HttpSession session,
			Map<String,Object> atributos){
		Boolean commit = null;
		if(accion==null || accion.equals("") || accion.equals("NUEVO"))
			accion="NUEVO";
		
		//Seteamos el Deposito
		Deposito deposito = new Deposito();
		deposito.setCodigo(stock.getCodigoDeposito());
		deposito = depositoService.getByCodigo(deposito, obtenerClienteAspUser());
		if(deposito != null)
			stock.setDeposito(deposito);		
		
		if(!result.hasErrors()){
			stock.setAccion(accion);			
			validator.validate(stock,result);
		}
	
		Stock stockFormulario;
		Stock inverso = null;
		if(!result.hasErrors()){		
			if(accion.equals("NUEVO")){
				stockFormulario = new Stock();				
			}
			stockFormulario = stock;
			
			//Seteamos el Origen/Destino si no es venta/compra
			if(stockFormulario.getCodigoOrigenDestino() != null && !"".equals(stockFormulario.getCodigoOrigenDestino())){
				Deposito origenDestino = new Deposito();
				origenDestino.setCodigo(stock.getCodigoOrigenDestino());
				origenDestino = depositoService.getByCodigo(origenDestino, obtenerClienteAspUser());
				if(origenDestino != null)
					stockFormulario.setOrigenDestino(origenDestino);
			}
			
			//Setear Concepto
			ConceptoFacturable concepto = conceptoFacturableService.obtenerConceptoFacturablePorCodigo(stock.getCodigoConcepto(), obtenerClienteAspUser());
			if(concepto != null){
				stockFormulario.setConcepto(concepto);
			}			
			stockFormulario.setClienteAsp(obtenerClienteAspUser());
			
			if("Egreso".equals(stockFormulario.getTipoMovimiento())){
				int cantidadNegativa = stockFormulario.getCantidad()*(-1);
				stockFormulario.setCantidad(cantidadNegativa);				
			}
	
			if(stockFormulario.getCodigoOrigenDestino() != null && !"".equals(stockFormulario.getCodigoOrigenDestino())){
				inverso = generarMovimientoInverso(stockFormulario);
			}
			if(inverso == null){
				commit = stockService.save(stockFormulario);
			}else{
				List<Stock> saveList = new ArrayList<Stock>();
				saveList.add(stockFormulario);
				saveList.add(inverso);
				commit = stockService.saveList(saveList);
			}
		}
		
		//Ver errores
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("stockFormulario", stock);
			atributos.put("accion", accion);
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return precargaFormularioStock(accion, stock.getId()!= null ? stock.getId().toString() : null, null, null, null, null, null, null, null, null, atributos);
		}	
		
		if(result.hasErrors()){
			atributos.put("stockFormulario", stock);
			atributos.put("accion", accion);
			atributos.put("errores", true);
			atributos.put("result", result);
			atributos.put("hayAvisos", false);
			atributos.remove("avisos");
			
			return precargaFormularioStock(accion, stock.getId()!= null ? stock.getId().toString() : null, null, null, null,null, stock.getCodigoEmpresa(), stock.getCodigoSucursal(), stock.getCodigoConcepto(), stock.getCodigoDeposito(), atributos);
			
		}else{
			//Genero las notificaciones 
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeStockReg = new ScreenMessageImp("formularioStock.notificacion.stockRegistrado", null);
			avisos.add(mensajeStockReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("avisos", avisos);	
		}
		//hacemos el redirect
		return listaStockController.mostrarStock(session, atributos, null, null, null, null, null);
	}

/////////////////////METODOS DE SOPORTE/////////////////////////////
	public Stock generarMovimientoInverso(Stock stock){
		Stock inverso = new Stock();
		inverso.setOrigenDestino(stock.getDeposito());
		inverso.setDeposito(stock.getOrigenDestino());
		inverso.setCantidad(stock.getCantidad());
		inverso.setFecha(stock.getFecha());
		inverso.setHora(stock.getHora());
		inverso.setNota(stock.getNota());
		inverso.setConcepto(stock.getConcepto());
		inverso.setClienteAsp(stock.getClienteAsp());
		if("Egreso".equals(stock.getTipoMovimiento())){
			inverso.setCantidad(stock.getCantidad()*(-1));
			inverso.setTipoMovimiento("Ingreso");
		}else{
			inverso.setTipoMovimiento("Egreso");
		}
		return inverso;
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
		depositosPopupMap.put("referenciaHtml", "codigoOrigenDestino"); 		
		//url que se debe consumir con ajax
		depositosPopupMap.put("urlRequest", "precargaFormularioStock.html");
		//se vuelve a setear el texto utilizado para el filtrado
		depositosPopupMap.put("textoBusqueda", val);
		
		depositosPopupMap.put("filterPopUp", sucursalCodigo);
		//codigo de la localización para el titulo del popup
		depositosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("depositosPopupMap", depositosPopupMap);
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
		sucursalesPopupMap.put("urlRequest", "precargaFormularioStock.html");
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
		empresasPopupMap.put("urlRequest", "precargaFormularioStock.html");
		//se vuelve a setear el texto utilizado para el filtrado
		empresasPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		empresasPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("empresasPopupMap", empresasPopupMap);
	}
	
	private void definirPopupConceptos(Map<String,Object> atributos, String val){
		//creo un hashmap para almacenar los parametros del popup
		Map<String,Object> conceptosPopupMap = new HashMap<String, Object>();
		//definicion de los campos a mostrar en la tabla
		//new CampoDisplayTag(Atributo,Titulo Columna, Invisible(true)/Visible(false))
		List<CampoDisplayTag> campos = new ArrayList<CampoDisplayTag>();
		campos.add(new CampoDisplayTag("codigo","formularioClienteConcepto.concepto.codigo",false));
		campos.add(new CampoDisplayTag("descripcion","formularioClienteConcepto.concepto.descripcion",false));
		campos.add(new CampoDisplayTag("costo","formularioClienteConcepto.concepto.costo",false));
		campos.add(new CampoDisplayTag("precioBase","formularioClienteConcepto.concepto.precioBase",false));
		conceptosPopupMap.put("campos", campos);
		//Coleccion de objetos a utilizar en el popup
		//el filtro del popup retorna un valor, el cual es utilizado para filtrar la colección dentro del servicio
		conceptosPopupMap.put("coleccionPopup", conceptoFacturableService.listarConceptosFacturablesPopup(val, obtenerClienteAspUser(), 1, 0));
		//atributo de referencia que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosPopupMap.put("referenciaPopup", "codigo");
		//atributo de referencia (segundo) que utiliza el popup para cargar cmponente html en la pantalla padre
		conceptosPopupMap.put("referenciaPopup2", "descripcion");
		//id del objeto html donde se va a escribir el valor del campo de referencia del objeto seleccionado
		conceptosPopupMap.put("referenciaHtml", "codigoConcepto"); 		
		//url que se debe consumir con ajax
		conceptosPopupMap.put("urlRequest", "precargaFormularioStock.html");
		//se vuelve a setear el texto utilizado para el filtrado
		conceptosPopupMap.put("textoBusqueda", val);		
		//codigo de la localización para el titulo del popup
		conceptosPopupMap.put("tituloPopup", "textos.seleccion");
		//Agrego el mapa a los atributos
		atributos.put("conceptosPopupMap", conceptosPopupMap);
	}
	
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
