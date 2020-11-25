package com.dividato.configuracionGeneral.controladores;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
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

import com.dividato.configuracionGeneral.validadores.PosicionLibreBusquedaValidator;
import com.security.accesoDatos.configuraciongeneral.interfaz.CambioEtiquetaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PosicionService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.CambioEtiqueta;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.general.PersonaFisica;
import com.security.modelo.seguridad.User;
import com.security.utils.ScreenMessage;
import com.security.utils.ScreenMessageImp;
/**
 * Controlador que se utiliza para los servicios asociados a la
 * lista de PosicionLibre.
 * Observar la anotación @Controller (que hereda de @Component) de SPRING.
 * Esta anotación le indica a SPRING que esta clase es de tipo controlador.
 * A continuación está la anotación @RequestMapping que indica cuales son
 * las URL que mapea este controlador.
 * @author X
 *
 */
@Controller
@RequestMapping(
		value=
			{	
				"/iniciarPrecargaFormularioCambioEtiqueta.html",
				"/mostrarCambioEtiqueta.html",
				"/guardarCambioEtiqueta.html",
				"/importarLecturaPosicionLibreCambioEtiqueta.html",
				"/mostrarHistorialCambioEtiqueta.html"
				
			}
		)
public class ListaCambioEtiquetaController {
	
	private PosicionService posicionService;
	private PosicionLibreBusquedaValidator validator;
	private LecturaService lecturaService;
	private LecturaDetalleService lecturaDetalleService;
	private ElementoService elementoService;
	private CambioEtiquetaService cambioEtiquetaService; 
		
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	@Autowired
	public void setPosicionService(PosicionService posicionService) {
		this.posicionService = posicionService;
	}
	
	@Autowired
	public void setCambioEtiquetaService(CambioEtiquetaService cambioEtiquetaService) {
		this.cambioEtiquetaService = cambioEtiquetaService;
	}
	
	@Autowired
	public void setValidator(PosicionLibreBusquedaValidator validator) {
		this.validator = validator;
	}
	@InitBinder
	public void initDataBinder(WebDataBinder binder) {
		validator.initDataBinder(binder);
	}
	
	/**
	 * Setea el servicio de Depositos.
	 * Observar la anotación @Autowired, que le indica a Spring que
	 * debe ejecutar este método e inyectarle un objeto PosicionService.
	 * La clase DepositoServiceImp implementa Deposito y está anotada
	 * con @Component, entonces Spring sabe que puede instanciar esta clase
	 * y pasársela a este método.
	 * @param depositoService
	 */
	
	@Autowired
	public void setLecturaDetalleService(LecturaDetalleService lecturaDetalleService) {
		this.lecturaDetalleService = lecturaDetalleService;
	}
	
	@Autowired
	public void setLecturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
			
	@RequestMapping(
			value="/iniciarPrecargaFormularioCambioEtiqueta.html",
			method = RequestMethod.GET
		)
	public String iniciarPosicionLibre(HttpSession session,
			Map<String,Object> atributos){
		
		session.removeAttribute("tipoSession");
		session.removeAttribute("listaElementosSession");
		session.removeAttribute("posicionesALiberarSession");
		session.removeAttribute("posicionsSession");
		session.removeAttribute("posicionesAsignadasSession");
		session.removeAttribute("lectura");
		session.removeAttribute("posicionLibreBusqueda");
		
		return "redirect:mostrarCambioEtiqueta.html";
	}
	
	/**
	 * Observar la anotación @RequestMapping de SPRING.
	 * Todos los parámetros son inyectados por SPRING cuando ejecuta el método.
	 * 
	 * Se encarga de buscar la lista de PosicionLibre y subirla a request.
	 * 
	 * @param atributos son los atributos del request
	 * @return "consultaPosicionLibre" la vista que debe mostrarse (ver dispatcher-servlet.xml/viewResolver)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(
			value="/mostrarCambioEtiqueta.html",
			method = RequestMethod.GET
		)
	public String mostrarCambioEtiqueta(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="val", required=false) String valSeccion,
			@RequestParam(value="val", required=false) String valDeposito,
			@RequestParam(value="accion", required=false) String accion,
			@RequestParam(value="val", required=false) String valLectura,
			@RequestParam(value="codigoLectura", required=false) String codigoLectura,
			@RequestParam(value="importar", required=false) String importar,
			@RequestParam(value="depositoCodigo", required=false) String depositoCodigo){

		TipoElemento tipo = (TipoElemento) session.getAttribute("tipoSession");
		List<LecturaDetalle> listaElementos = (List<LecturaDetalle>) session.getAttribute("listaElementosSession");
		@SuppressWarnings("unused")
		List<Posicion> posicionesALiberar = (List<Posicion>) session.getAttribute("posicionesALiberarSession");
		List<PosicionLibre> posicionesAsignadas = (List<PosicionLibre>) session.getAttribute("posicionesAsignadasSession");
		List<Posicion> posicions = (List<Posicion>) session.getAttribute("posicionsSession");
		if(posicions==null)
			posicions = new ArrayList<Posicion>();
		
		List<PosicionLibre> listaPosicionesLibres = new ArrayList<PosicionLibre>();
		Posicion posicion = (Posicion) session.getAttribute("posicionLibreBusqueda");		
//		if(posicion != null && posicion.getCodigoDeposito() != null && !"".equals(posicion.getCodigoDeposito())
//				&&  posicion.getCodigoSeccion() != null && !"".equals(posicion.getCodigoSeccion())){
		//Este anterior if estaba en la busqueda de posicion.. ver si sirve o no
		
//		}else if(posicion==null){
//			posicion=new Posicion();
//			posicion.setCodigoDesdeEstante("0000");
//			posicion.setCodigoHastaEstante("9999");
//			session.setAttribute("posicionBusqueda", posicion);		
		
		if("CANCELAR".equalsIgnoreCase(accion))
		{
			posicionesAsignadas = null;
			posicionesALiberar = null;
			accion = "ASIGNAR";
			atributos.put("accion", accion);
		}
		
		//Si no hay posiciones asignadas hay que buscar en base de datos
		if(posicionesAsignadas==null){
		
			posicions = null;
			if(posicion!= null){
				posicion.setEstado("DISPONIBLE");
				//posicions =(List<Posicion>) posicionService.listarPosicionFiltradas(posicion, obtenerClienteAspUser());
				posicions = posicionService.traerPosicionesLibresPorSQL(posicion, obtenerClienteAspUser(),tipo);

				session.setAttribute("posicionLibreBusqueda", posicion);
			}
			//Si se encontraton posiciones
			if(posicions!=null)
			{
				for(Object ob:posicions){
					Object[] lista = (Object[]) ob;
					PosicionLibre posicionLibre = new PosicionLibre((Posicion)lista[0],(String)lista[1]);
					listaPosicionesLibres.add(posicionLibre);
				}
				
				
				//Si existe una lectura con elementos validos cargada
				if(listaElementos != null && listaElementos.size()>0){
				//Si hay un tipo seteado
					if(tipo!=null)
					{
						atributos.put("accion", "ASIGNAR");
					}
				}
			}
		}else
		{
			listaPosicionesLibres = posicionesAsignadas;
			atributos.put("accion", "GUARDAR");
		}

		if(session.getAttribute("lectura")!=null)
		{
			Lectura lectura = (Lectura)session.getAttribute("lectura");
			atributos.put("codigoLectura", lectura.getCodigo());
			atributos.put("descripcion", lectura.getDescripcion());
		}
		atributos.put("listaElementos", listaElementos);
		session.setAttribute("listaElementosSession", listaElementos);
		atributos.put("posicionesLibres", listaPosicionesLibres);
		session.setAttribute("posicionesAsignadasSession", posicionesAsignadas);
		session.setAttribute("posicionesLibresSession", listaPosicionesLibres);
		session.setAttribute("posicionsSession", listaPosicionesLibres);
		atributos.put("clienteId", obtenerClienteAspUser().getId());
		atributos.put("codigoEmpresa", obtenerEmpresaDefault().getCodigo());
	
		//si no hay errores
		if(atributos.get("errores") == null)
			atributos.put("errores", false);

		//definirPopupLecturas(atributos, valLectura);

		//hacemos el forward
		return "consultaCambioEtiqueta";
	}
	
	
	
	@RequestMapping(
			value="/guardarCambioEtiqueta.html", 
			method = RequestMethod.GET
		)
	public String guardarCambioEtiqueta(HttpSession session, Map<String,Object> atributos){
		

		@SuppressWarnings("unchecked")
		List<LecturaDetalle> listaElementos = (List<LecturaDetalle>) session.getAttribute("listaElementosSession");
		Long codigo = listaElementos.get(0).getLectura().getCodigo();
		// Sacar el tipo de elemento.
		TipoElemento tipoElemento = listaElementos.get(0).getElemento().getTipoElemento();
		// Buscar el utlimo codigo
		String l = elementoService.traerUltCodigoPorTipoElemento(tipoElemento, obtenerClienteAsp());
		// Calcular el rango de codigos de acuerdo a la cantidad de elementos
		BigDecimal ultimoCodigo = new BigDecimal(l.substring(2));
		for(LecturaDetalle lec : listaElementos){
			ultimoCodigo = ultimoCodigo.add(new BigDecimal("1"));
			CambioEtiqueta ce= new CambioEtiqueta();
			String prefijo = lec.getElemento().getTipoElemento().getPrefijoCodigo();
			
			String cadena = String.valueOf(ultimoCodigo);
			int cantNumeros = cadena.length();
			int faltan = 10 - cantNumeros;
			for(int f = 0; f<faltan ; f++)
			{
				cadena= "0" + cadena;
			}
			
			
			ce.setEtiquetaNueva(prefijo+cadena);
			ce.setEtiquetaOriginal(lec.getElemento().getCodigo());
			ce.setFechaModificacion(new Date());
			ce.setUsuarioModificacion(obtenerUser());
			ce.setIdLectura(codigo);
			lec.getElemento().setCodigo(prefijo+cadena);
			lec.getElemento().setCodigoSinPrefijo(cadena);
			// Registro en la tabla cambio_etiqueta
			cambioEtiquetaService.guardar(ce);
			
		}
		
		Boolean commit;
		List<Elemento> elementos = new ArrayList<Elemento>();
		
		for(int i = 0; i<listaElementos.size();i++){
			elementos.add(listaElementos.get(i).getElemento());
		}
		commit = elementoService.actualizarElementoList(elementos);
		
		if(commit != null && !commit){
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensaje = new ScreenMessageImp("error.commitDataBase", null);
			avisos.add(mensaje); //agrego el mensaje a la coleccion
			atributos.put("listaElementos", listaElementos);
			atributos.put("accion", "GUARDAR");
			atributos.put("errores", false);
			atributos.put("hayAvisos", false);
			atributos.put("hayAvisosNeg", true);
			atributos.put("avisos", avisos);
			return mostrarCambioEtiqueta(session,atributos, null, null, null, null, null, null, null);
		}
		else
		{
			List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
			ScreenMessage mensajeEstanteReg = new ScreenMessageImp("formularioCambioEtiqueta.notificacion.cambioEtiqueta", null);
			avisos.add(mensajeEstanteReg); //agrego el mensaje a la coleccion
			atributos.put("errores", false);
			atributos.remove("result");
			atributos.put("hayAvisos", true);
			atributos.put("hayAvisosNeg", false);
			atributos.put("avisos", avisos);
		}
		
		session.removeAttribute("tipoSession");
		session.removeAttribute("listaElementosSession");
		session.removeAttribute("posicionesALiberarSession");
		session.removeAttribute("posicionesAsignadasSession");
		session.removeAttribute("posicionsSession");
		session.removeAttribute("lectura");
		
		atributos.remove("accion");
		atributos.remove("posicions");
		atributos.remove("listaElementos");
		session.removeAttribute("posicionLibreBusqueda");
		return mostrarCambioEtiqueta(session,atributos, null, null, null, null, null, null, null);
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@RequestMapping(
			value="/importarLecturaPosicionLibreCambioEtiqueta.html",
			method = RequestMethod.GET
		)
	public String importarLecturaPosicionLibreCambioEtiqueta(HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="codigoLectura", required=false) String codigoLectura){
		
		TipoElemento tipo = (TipoElemento) session.getAttribute("tipoSession");
		List<LecturaDetalle> listaElementos = (List<LecturaDetalle>) session.getAttribute("listaElementosSession");
		List<Posicion> posicions = (List<Posicion>) session.getAttribute("posicionsSession");
		
		ScreenMessage mensaje = null;
		List<ScreenMessage> avisos = new ArrayList<ScreenMessage>();
		Boolean hayAvisos = false;
		Boolean hayAvisosNeg = false;
		Boolean banderaModulos = false, banderaInexistentes = false;
		
		//Si se importa una lectura
	 	if (codigoLectura != null) {
				Lectura lectura = lecturaService.obtenerPorCodigo(
						Long.valueOf(codigoLectura), null, obtenerEmpresaDefault(),obtenerClienteAsp());
				if(lectura==null)
				{
					session.removeAttribute("lectura");
					listaElementos = null;
					hayAvisosNeg = true;
					mensaje = new ScreenMessageImp("formularioPosicionLibre.notificacion.lecturaIvalida", null);
					avisos.add(mensaje);
					atributos.put("hayAvisosNeg", hayAvisosNeg);
					atributos.put("avisos", avisos);
					return mostrarCambioEtiqueta(session, atributos, null, null, null, null, null, null, null);
				}
				session.setAttribute("lectura", lectura);
				listaElementos = lecturaDetalleService.listarOrderByOrdenLecturaDetallePorLectura(lectura,obtenerClienteAsp());
				//Si la lectura contiene al menos un elemento
				if (listaElementos != null && listaElementos.size() > 0) {
					
					tipo = null;
					int cantidadTipo = 0;
					//Recorro la lista de elementos
					for (int i = 0; i < listaElementos.size(); i++) {
						//Pregunto si el codigo de barras pertenece a un modulo
						if (listaElementos.get(i).getCodigoBarras().startsWith("99")) {
							//Si es asi se remueve de la lista
							listaElementos.remove(i);
							i = i - 1;
							banderaModulos = true;
						} 
						else
						{
							//Pregunto si la lectura pertenece a un elemento existente
							if (listaElementos.get(i).getElemento() == null) {
							//Si se es asi se remueve
							listaElementos.remove(i);
							i = i - 1;
							banderaInexistentes = true;
							}
							//Si llega aqui es que es un elemento existente
							else
							{
								cantidadTipo = 1;
								//La primera vez que entra aca la variable tipo estara en nula
								if(tipo != null)
								{
									//Si se entro aqui es el segundo o mas elemento existente por lo que se compara el tipoElemento
									//con el tipoElemento del elemento existente anterior
									//Todos deben tener el mismo tipo
									if(!tipo.equals(listaElementos.get(i).getElemento().getTipoElemento()))
									{
										//Si es diferente se avisa que la lectura contiene elementos de tipo diferente y sale
										cantidadTipo = 2;
										hayAvisosNeg = true;
										mensaje = new ScreenMessageImp("formularioPosicionLibre.notificacion.filtradoElementoDiferentesTipo", null);
										avisos.add(mensaje);
										atributos.put("hayAvisosNeg", hayAvisosNeg);
										atributos.put("avisos", avisos);
										listaElementos = null;
										//hacemos el forward
										return mostrarCambioEtiqueta(session, atributos, null, null, null, null, null, null, null);
									}
								}
								//Se setea el tipo de elemento del ultimo elemento leido en la lectura
								tipo = listaElementos.get(i).getElemento().getTipoElemento();
							}
						}

					}
					if(banderaModulos)
					{
						//Se avisa que uno o mas modulos fueron descartados de la lectura
						mensaje = new ScreenMessageImp("formularioPosicionLibre.notificacion.filtradoElementoModulo", null);
						avisos.add(mensaje);
						hayAvisos = true;
						
					}
					if(banderaInexistentes)
					{
						//Se avisa que uno o mas elementos inexistentes fueron descartados de la lectura
						mensaje = new ScreenMessageImp("formularioPosicionLibre.notificacion.filtradoElementoInexistente", null);
						avisos.add(mensaje);
						hayAvisos = true;
					}
					//Se pregunta si quedaron elementos existentes en la lectura para asignarles posiciones libres
					if (listaElementos.size() > 0) {
						//Se muesta la lista de elementos
						session.setAttribute("listaElementosSession", listaElementos);
						atributos.put("listaElementos", listaElementos);
						session.setAttribute("tipoSession", tipo);
					} else {
						//Se avisa que la lectura no contiene elmentos validos para asignarles posicion libre
						mensaje = new ScreenMessageImp("formularioPosicionLibre.notificacion.lecturaSinElementos", null);
						avisos.add(mensaje);
						hayAvisosNeg = true;
					}

				}
			}//salida de la importacion de lectura
	
		atributos.put("codigoLectura", codigoLectura);
		atributos.put("hayAvisos", hayAvisos);
		atributos.put("avisos", avisos);
		if(posicions!=null)
		{
			atributos.put("posicionesLibres", posicions);
			session.setAttribute("posicionsSession", posicions);
		}
		//hacemos el forward
		return mostrarCambioEtiqueta(session, atributos, null, null, null, null, null, null, null);
		
	}
	
	@RequestMapping(value="/mostrarHistorialCambioEtiqueta.html", method = RequestMethod.GET)
	public String mostrarCuentaCorriente(
			HttpSession session,
			Map<String,Object> atributos,
			@RequestParam(value="idLectura", required=false) Long codigoLectura){
		
	    List<CambioEtiqueta> listaCambioEtiqueta = cambioEtiquetaService.listarCambioEtiquetaPorCodigoLectura(codigoLectura);
		atributos.put("listaCambioEtiqueta", listaCambioEtiqueta);
		return "consultaHistorialCambioEtiqueta";
	}	
	
	/////////////////////METODOS DE SOPORTE/////////////////////////////
	
	public class PosicionLibre{
		
		private Posicion pos;
		private String valoracion;
		
		public PosicionLibre(Posicion posi, String val){
			pos = posi;
			valoracion = val;
		}
			
		public Posicion getPos() {
			return pos;
		}
		public void setPos(Posicion posicion) {
			this.pos = posicion;
		}
		public String getValoracion() {
			return valoracion;
		}
		public void setValoracion(String valoracion) {
			this.valoracion = valoracion;
		}

	}
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
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
	
}