package com.dividato.configuracionGeneral.tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.dividato.configuracionGeneral.i18n.I18nUtil;
import com.dividato.configuracionGeneral.tasks.utils.Commons;
import com.dividato.configuracionGeneral.tasks.utils.ScheduledTaskException;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ModuloService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PosicionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.TipoElementoService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;
import com.security.utils.EAN13;
import com.security.utils.LecturaCodigoBarraUtil;
import com.security.utils.ParseNumberUtils;
import com.security.utils.ReposicionamientoUtil;

public class ReposicionAutomatica {
	
	private static Logger logger = Logger.getLogger(ReposicionAutomatica.class);
	public static final String process = "Process 1: Import Extracts";
	
	private ElementoService elementoService;
	private ModuloService moduloService;
	private TipoElementoService tipoElementoService;
	private PosicionService posicionService;
	private UserService userService;
	private LecturaService lecturaService;
	
	private Commons utils;
	
	private Long clienteAspId;
	private String username;
	private Long empresaId;
	private Long sucursalId;
	
	private String lecturasPath;
	private String lecturasProcessed;
	private String lecturasError;
	
	/**
	 * Setea el objeto Commons con funcionalidad común a todos los procesos programados.<br/>
	 * Este método es ejecutado por spring cuando se crea la instancia.
	 * @param commons
	 */
	@Autowired
	public void setCommons(Commons commons){
		this.utils=commons;
	}
	
	@Autowired
	public void setElementoService(ElementoService elementoService) {
		this.elementoService = elementoService;
	}
	@Autowired
	public void setModuloService(ModuloService moduloService) {
		this.moduloService = moduloService;
	}
	@Autowired
	public void setTipoElementoService(TipoElementoService tipoElementoService) {
		this.tipoElementoService = tipoElementoService;
	}
	@Autowired
	public void setPosicionService(PosicionService posicionService) {
		this.posicionService = posicionService;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setLecturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	public Long getClienteAspId() {
		return clienteAspId;
	}

	public void setClienteAspId(Long clienteAspId) {
		this.clienteAspId = clienteAspId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(Long empresaId) {
		this.empresaId = empresaId;
	}

	public Long getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(Long sucursalId) {
		this.sucursalId = sucursalId;
	}

	/**
	 * método de ingreso a la tarea.<br/>
	 * Busca los archivos de la carpeta e inicia su analisis.
	 * Una vez completado, mueve el archivo de origen a la carpeta procesado o error, según corresponda.<br/>
	 *
	 */
	public synchronized void searchFiles() {
		SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );
		SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		ClienteAsp clienteAsp = new ClienteAsp();
		clienteAsp.setId(clienteAspId);
		User user = new User();
		user.setUsername(username);
		user = ((List<User>)userService.listarTodosUserFiltradosByCliente(user, clienteAsp)).get(0);
		System.out.println("**** ARRANCA ***");
		
		try {
			System.out.println("Arranca el SearchFiles");
			System.out.println(getLecturasPath());
			utils.validate(getLecturasPath());
			System.out.println(getLecturasProcessed());
			utils.validate(getLecturasProcessed());
			System.out.println(getLecturasError());
			utils.validate(getLecturasError());
			
			} catch (ScheduledTaskException e) {
				System.out.println(e);
				return;
			}
		
		
		
		File[] filesRio=new File(getLecturasPath()).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});
		
		for (File txtFile:filesRio){
			

			System.out.println("**** LECTURAS ***" + txtFile.getName());

			try {
				
				//TODO: Verificar de hacer una validacion de lectura repetido en base
//				List<InterchangeCP> interchanges = interchangeService.listarPorArchivo(csvFile.getName().toUpperCase());
//				if(interchanges!=null && interchanges.size()>0){
//					throw new ScheduledTaskException("interchangeFile.error.repetido");
//				}
				
				Lectura lectura = new Lectura();
				LecturaDetalle detalle = new LecturaDetalle();
				List<LecturaDetalle> listaDetalles = new ArrayList<LecturaDetalle>();
				List<Elemento> listaElementosAReposicionar = new ArrayList<Elemento>();
				List<Elemento> listaElementosAnterioresModuloDestino = new ArrayList<Elemento>();
				List<String> codigosElementos = new ArrayList<String>();
				List<Posicion> listaPosicionesDestino = new ArrayList<Posicion>();
				List <Posicion> listaPosicionesOrigen = new ArrayList<Posicion>();
				String prefijo = null;
				Modulo moduloDestino = null;
				String tipoElementoStr = null;
				TipoElemento tipoElemento = null;
				String nombreLectura = txtFile.getName().substring(0,txtFile.getName().toLowerCase().indexOf(".txt"));
				
				boolean banderaLecturaCorrecta = true;
				
				LecturaCodigoBarraUtil lector = new LecturaCodigoBarraUtil();
				List<String> lista = new ArrayList<String>();
				lista = lector.decodificarLectura(txtFile);
				Long orden = (long)0;
				
				if (lista != null) 
				{	
					List<String> listaDescartados = new ArrayList<String>();
					String codigo,codigoCorrecto,codigoTomado12;
					Boolean repetido;
					for (int i = 0; i < lista.size(); i++) {
						repetido = false;
						codigo = lista.get(i);
						if(codigo.length() >= 12){
						codigoTomado12 = lista.get(i).substring(0, 12);
						codigoCorrecto = codigoTomado12 + String.valueOf(EAN13.EAN13_CHECKSUM(codigoTomado12));
							//if(codigo.equals(codigoCorrecto)){
								detalle = new LecturaDetalle();
								prefijo = codigo.substring(0, 2);
								
								if (i==0 && codigo.startsWith("99")) 
								{
									detalle.setCodigoBarras(codigoTomado12);
									detalle.setObservacion("Este código de barras pertenece a un módulo");
									moduloDestino = getModuloPorCodigoBarras(detalle.getCodigoBarras().substring(0, 12), clienteAsp);
									if(moduloDestino==null)
									{
										//codigosErrores.add(Constantes.ERROR_MODULO_NO_EXISTE);
										lista.set(i, lista.get(i)+" --->  Error: EL MODULO NO EXISTE");
										banderaLecturaCorrecta = false;
										continue;
									}
									else
									{
										nombreLectura = agregarCeros(moduloDestino.getEstante().getCodigo(), 4)  + agregarCeros(moduloDestino.getPosVertical().toString(), 2) + agregarCeros(moduloDestino.getPosHorizontal().toString(), 2);
									}
								}
								else if(i==0 && !codigo.startsWith("99"))
								{
									detalle.setCodigoBarras(codigoTomado12);
									lista.set(i, lista.get(i)+" --->  Error: EL MODULO NO EXISTE");
									banderaLecturaCorrecta = false;
									continue;
								}
								else 
								{
									detalle.setCodigoBarras(codigoTomado12);
									
									Elemento elemento = elementoService.getByCodigo(codigoTomado12,clienteAsp);
									if (elemento != null) {
										detalle.setElemento(elemento);
										detalle.setObservacion("Elemento "+ elemento.getCodigo());
									} else {
										//detalle.setObservacion("Elemento no existente.");
										lista.set(i, lista.get(i)+" --->  Error: LA ETIQUETA NO EXISTE");
										banderaLecturaCorrecta = false;
										continue;
									}
									
									
									if(tipoElementoStr == null){
										tipoElementoStr = prefijo;
										tipoElemento = tipoElementoService.getByPrefijo(tipoElementoStr, clienteAsp);
										if(tipoElemento==null){
											//codigosErrores.add(Constantes.ERROR_TIPO_ELEMENTO_NO_EXISTE);
											lista.set(i, lista.get(i)+" --->  Error: EL TIPO DE ETIQUETA NO EXISTE");
											banderaLecturaCorrecta = false;
											continue;
										}else{
											codigosElementos.add(detalle.getCodigoBarras().substring(0, 12));
											listaElementosAReposicionar.add(elemento);
										}
									}else{
										if(tipoElementoStr.equals(detalle.getCodigoBarras().substring(0,2))){
											codigosElementos.add(detalle.getCodigoBarras().substring(0, 12));	
											listaElementosAReposicionar.add(elemento);
										}else{
											//codigosErrores.add(Constantes.ERROR_LOS_ELEMENTOS_DE_LA_LECTURA_NO_SON_DEL_MISMO_TIPO);
											lista.set(i, lista.get(i)+" --->  Error: LOS ELEMENTOS DE LA LECTURA NO SON DEL MISMO TIPO");
											banderaLecturaCorrecta = false;
											continue;
										}
									}					
									
									
									
									
								}
								if(listaDetalles.size()>0)
								{
									for(int f = (listaDetalles.size()-1);f>=0;f--)
									{
										if(codigoTomado12.equals(listaDetalles.get(f).getCodigoBarras()))
										{
											lista.set(i, lista.get(i)+" --->  Advertencia: CODIGO ETIQUETA REPETIDO, SE OMITE");
											repetido = true;
											break;
										}
									}
								}
								
								if(!repetido)
								{
									orden++;
									detalle.setOrden(orden);
									listaDetalles.add(detalle);
								}
								
						}
						else
						{
							lista.set(i, lista.get(i)+" --->  Error: CODIGO ERRONEO");	
							banderaLecturaCorrecta = false;
							continue;
						}
					}
					
					
					if(moduloDestino != null && tipoElemento != null && banderaLecturaCorrecta)
					{
						//listaElementosAReposicionar = elementoService.getByCodigos(codigosElementos, clienteAsp);
						if(listaElementosAReposicionar != null && codigosElementos.size() == listaElementosAReposicionar.size())
						{
							int cant = lista.size();
							verificarElementos(listaElementosAReposicionar,tipoElemento, moduloDestino, lista, listaPosicionesDestino,clienteAsp);
							if(cant<lista.size())
								finalizarConError(lista, txtFile, nombreLectura);
							listaPosicionesDestino = posicionService.getPosicionesPorModuloParaReposicionamiento(moduloDestino, clienteAsp);
							
								if(verificarPosicionesDestino(listaPosicionesDestino))
								{
									cargarNuevasPosiciones(listaPosicionesDestino, listaElementosAReposicionar);
									if(listaPosicionesDestino!=null && listaPosicionesDestino.size()>0 ){
										if(listaElementosAReposicionar != null && listaElementosAReposicionar.size() > 0){
										
											listaPosicionesOrigen = obtenerPosicionesOrigen(listaElementosAReposicionar, clienteAsp);					
											actualizarViejasPosiciones(listaPosicionesOrigen);
												listaElementosAnterioresModuloDestino = elementoService.buscarElementosAnterioresModuloDestino(moduloDestino, clienteAsp);
												
												lectura.setClienteAsp(clienteAsp);
												lectura.setUsuario(user);
												lectura.setFecha(new Date());
												lectura.setUtilizada(Boolean.TRUE);
												Set<LecturaDetalle> setDetalles = new HashSet<LecturaDetalle>(listaDetalles);
												Long cantElementos = (long)setDetalles.size();
												lectura.setElementos(cantElementos);
												lectura.setDetalles(setDetalles);
												Long prevCodigo = lecturaService.traerUltCodigoPorClienteAsp(clienteAsp);
												Long codigoLectura = prevCodigo + 1;
												lectura.setCodigo(codigoLectura);
												Empresa empresa = new Empresa();
												empresa.setId(empresaId);
												lectura.setEmpresa(empresa);
												Sucursal sucursal = new Sucursal();
												sucursal.setId(sucursalId);
												lectura.setSucursal(sucursal);
												lectura.setDescripcion(nombreLectura);
												lectura.setObservacion("Estantería "+nombreLectura);
												//Boolean commit = lecturaService.guardarLecturaYDetalles(setDetalles,lectura);
												
												
												//if(commit!=null && commit)
												//{
													if(elementoService.guardarReposicionamiento(listaPosicionesOrigen, listaPosicionesDestino, listaElementosAReposicionar, listaElementosAnterioresModuloDestino, clienteAsp,user,lectura)){

														txtFile.renameTo(new File(getLecturasProcessed()+"//E"+nombreLectura+"_"+lectura.getCodigo()+"_OK.txt"));
													}
													else
													{
														//codigoErrores.add(Constantes.ERROR_FALLA_REPOSICIONAMIENTO);
														lista.add("\nERROR: FALLO EN REPOSICIONAMIENTO. REVISE EL ARCHIVO Y VUELVA A PROBAR.");	
														finalizarConError(lista, txtFile,nombreLectura);
													}
//												}
//												else
//												{
//													lista.add("\nERROR: FALLA AL CARGAR LECTURA. REVISE EL ARCHIVO Y VUELVA A PROBAR.");	
//													finalizarConError(lista, txtFile,nombreLectura);
//												}
										}
										else
										{
											//codigoErrores.add(Constantes.ERROR_NO_HAY_ELEMENTOS_PARA_REPOSICIONAR);
											lista.add("\nERROR: NO HAY ELEMENTOS PARA REPOSICIONAR");	
											finalizarConError(lista, txtFile,nombreLectura);
										}
									}else{
										//codigoErrores.add(Constantes.ERROR_FALLA_REPOSICIONAMIENTO);
										lista.add("\nERROR: FALLA EN REPOSICIONAMIENTO. REVISE EL ARCHIVO Y VUELVA A PROBAR.");	
										finalizarConError(lista, txtFile,nombreLectura);
									}
								}
								else
								{
									//codigosErrores.add(Constantes.ERROR_EXISTEN_POSICIONES_OCUPADAS);
									lista.add("\nERROR: EXISTEN POSICIONES OCUPADAS");	
									finalizarConError(lista, txtFile,nombreLectura);
								}
							
						}
						else
						{
							lista.add("\nERROR: EXISTEN POSICIONES OCUPADAS");
							finalizarConError(lista, txtFile,nombreLectura);
						}
						
					}
					else
					{
						finalizarConError(lista, txtFile,nombreLectura);
					}
					
				
				}				
							
				
	
				
			
			} catch (FileNotFoundException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			} catch (ScheduledTaskException e){
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
				txtFile.renameTo(new File(getLecturasError()+"//"+txtFile.getName().substring(0, txtFile.getName().indexOf(".txt"))+"_ERROR.txt"));
			}
		}
}



public String getLecturasPath() {
	return utils.getBasePath() + lecturasPath;
}

public void setLecturasPath(String lecturasPath) {
	this.lecturasPath = lecturasPath;
}

public String getLecturasProcessed() {
	return utils.getBasePath() + lecturasProcessed;
}

public void setLecturasProcessed(String lecturasProcessed) {
	this.lecturasProcessed = lecturasProcessed;
}

public String getLecturasError() {
	return utils.getBasePath() + lecturasError;
}

public void setLecturasError(String lecturasError) {
	this.lecturasError = lecturasError;
}


/**
 * Agrega las posiciones del modulo de destino a cada elemento del parametro listaElementos
 * en el campo transcient posicionFutura. Ademas setea las posiciones asignadas como "ocupadas"
 * y las libres como "temporalmente anuladas"  
 * @param listaPosiciones
 * @param listaElementos
 * @return
 */
private boolean cargarNuevasPosiciones(List <Posicion> listaPosiciones, List<Elemento> listaElementos){		
	boolean result = true;
	if(listaElementos!=null && listaPosiciones!=null){
		Iterator<Elemento> itElementos = listaElementos.iterator();
		Iterator<Posicion> itPosicion = listaPosiciones.iterator();
		Elemento elemento = null;
		Posicion posicion = null;
		while(itPosicion.hasNext()){
			posicion = itPosicion.next();
			if(itElementos.hasNext()){
				elemento = itElementos.next();
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
				elemento.setPosicionFutura(posicion);
				posicion.setEstado(Constantes.POSICION_ESTADO_OCUPADA);
			}else{
				posicion.setEstado(Constantes.POSICION_ESTADO_TEMPORALMENTE_ANULADA);
			}
		}			
	}else{
		result = false;
	}
	return result;
}

/**
 * Verifica que en las posiciones de destino obtenidas no exista ninguna con el estado OCUPADA.
 * @param listaPosicionesDestino
 * 
 */
private Boolean verificarPosicionesDestino(List<Posicion> listaPosicionesDestino){
	
	for(Posicion pos : listaPosicionesDestino){
		if(!Constantes.POSICION_ESTADO_DISPONIBLE.equals(pos.getEstado())){
			return false;
		}
	}
	return true;
}
		
/**
 * A partir del codigo de barra obtiene el codigo de estanteria 
 * y las coordenadas de offset del modulo para realizar una consulta
 * (filtrando por clienteAsp) en la base de datos y devolver el modulo 
 * correspondiente. 
 * @param codigoBarras
 * @return
 */
private Modulo getModuloPorCodigoBarras(String codigoBarras, ClienteAsp clienteAsp){
	String codEstante = codigoBarras.substring(2, 6);
	String offSetVStr = codigoBarras.substring(6, 9);
	String offSetHStr = codigoBarras.substring(9, 12);
	return moduloService.getModuloByOffset(codEstante, parseIntegerCodigo(offSetVStr), parseIntegerCodigo(offSetHStr), clienteAsp);
}

private Integer parseIntegerCodigo(String codigo){
	return ParseNumberUtils.parseIntegerCodigo(codigo);
}

/**
 * Devuelve la lista de posiciones anteriores de los elementos a reposicionar.
 * @param elementosReposicionados Los elementos a reposicionar.
 * @return Devuelve una lista vacia si los elementos a reposicionar no tienen ninguna posicion anterior.
 */
private List<Posicion> obtenerPosicionesOrigen(List<Elemento> elementosReposicionados, ClienteAsp clienteAsp){
	return elementoService.obtenerPosicionesAnterioresPorElementos(elementosReposicionados, clienteAsp);
}

/**
 * Actualiza el estado de las posiciones de origen a DISPONIBLE, siempre que la posicion de origen no este ANULADA o TEMPORALMENTE ANULADA.
 * @param listaPosicionesOrigen
 */
private void actualizarViejasPosiciones(List<Posicion> listaPosicionesOrigen){
	
	for(Posicion pos : listaPosicionesOrigen){
				if(!Constantes.POSICION_ESTADO_ANULADO.equals(pos.getEstado()) && !Constantes.POSICION_ESTADO_TEMPORALMENTE_ANULADA.equals(pos.getEstado())){
					pos.setEstado(Constantes.POSICION_ESTADO_DISPONIBLE);
				}
	}
}

/**
 * Realiza las siguientes verificaciones y carga sus codigos de error en la lista de codigos de error:<br>
 *  1 - Que el reposicionamiento sea a modulo completo (numero de elementos multiplo del numero de posiciones horizontales del modulo).
 * 	2 - Que todos los elementos sean del mismo tipo
 *  3 - Que todos los elementos a reposicionar tengan el mismo origen (nulo o el mismo modulo).
 *  4 - Que todos los modulos del grupo al que pertenece el modulo destino contienen el mismo tipo de elemento.
 *  
 * @param elementos
 * @param tipoElemento
 * @param moduloDestino
 * @param errores
 * @return
 */
private void verificarElementos(List<Elemento> elementos, TipoElemento tipoElemento, Modulo moduloDestino, List<String> errores, List<Posicion> posicionesDisponibles, ClienteAsp clienteAsp) {
	ReposicionamientoUtil util = new ReposicionamientoUtil();
	/*if (!util.verificarReposicionamientoModuloCompleto(elementos, moduloDestino)) {   ///// ESTA VALIDACION SE PIDIO SACAR POR LA GENTE DE BASA - LUIS Y MIGUEL
		errores.add(Constantes.ERROR_NUM_ELEMENTOS_NO_ES_MULTIPLO_POS_HOR);
	}else*/ if(!util.verificarTodosElementosMismoTipo(elementos, tipoElemento)) {
		errores.add(I18nUtil.getText(Constantes.ERROR_LOS_ELEMENTOS_DE_LA_LECTURA_NO_SON_DEL_MISMO_TIPO,"formularioReposicionamiento"));
	}else if(!util.verificarSuficientesPosicionesModuloDestino(elementos, moduloDestino)) {
		errores.add(I18nUtil.getText(Constantes.ERROR_POSICIONES_INSUFICIENTES_MODULO_DESTINO,"formularioReposicionamiento"));
	}else if (!util.verificarTodosElementosPosicionables(elementos)){
		errores.add(I18nUtil.getText(Constantes.ERROR_ELEMENTOS_NO_POSICIONABLES_EN_LECTURA,"formularioReposicionamiento"));
	}else if(!util.verificarTipoValidoParaModuloDestino(tipoElemento, moduloDestino, elementoService, clienteAsp)){
		errores.add(I18nUtil.getText(Constantes.ERROR_EL_GRUPO_DEL_MODULO_DESTINO_CONTIENE_UN_TIPO_ELEMENTO_DIFERENTE,"formularioReposicionamiento"));
	}else if(!util.verificarDepositoActualIgualDepositoDestino(moduloDestino,elementos)){
		errores.add(I18nUtil.getText(Constantes.ERROR_DEPOSITO_ACTUAL_DE_ELEMENTOS_DISTINTO_DEPOSITO_DESTINO,"formularioReposicionamiento"));
	}
}

private void finalizarConError(List<String> lista,File txtFile,String nombreLectura) throws ScheduledTaskException, IOException {
	File errorTxt = new File(getLecturasError()+"//E"+nombreLectura+"_ERROR.txt");
	BufferedWriter bfWriter = new BufferedWriter(new FileWriter(errorTxt));
	for(String linea:lista){
		bfWriter.write(linea+"\r\n");
	}
	bfWriter.close();
	txtFile.delete();
	throw new ScheduledTaskException("Error controlado");
}

//le pasas el string y luego indicas la cantidad de 0 que quieres que te complete saludos
private static String agregarCeros(String string, int largo){
		String ceros = "";
		int cantidad = largo - string.length();
		if (cantidad >= 1)
		{
			for(int i=0;i<cantidad;i++)
			{
				ceros += "0";
			}
			return (ceros + string);
		}
		else
			return string;
}

}
