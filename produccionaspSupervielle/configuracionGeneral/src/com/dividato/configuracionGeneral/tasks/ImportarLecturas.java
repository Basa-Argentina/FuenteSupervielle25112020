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
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.dividato.configuracionGeneral.tasks.utils.Commons;
import com.dividato.configuracionGeneral.tasks.utils.ScheduledTaskException;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.accesoDatos.jerarquias.interfaz.RequerimientoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.seguridad.User;
import com.security.utils.EAN13;
import com.security.utils.LecturaCodigoBarraUtil;

public class ImportarLecturas {
	
	private static Logger logger = Logger.getLogger(ImportarLecturas.class);
	public static final String process = "Process 1: Importar Lecturas";
	
	private ElementoService elementoService;
	private UserService userService;
	private LecturaService lecturaService;
	private RequerimientoService requerimientoService;
	private RemitoService remitoService;
	
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
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setLecturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	@Autowired
	public void setRequerimientoService(RequerimientoService requerimientoService) {
		this.requerimientoService = requerimientoService;
	}
	@Autowired
	public void setRemitoService(RemitoService remitoService) {
		this.remitoService = remitoService;
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
		
		
		
		File[] lecturas=new File(getLecturasPath()).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return ((name.toLowerCase().startsWith("0001") || name.toLowerCase().startsWith("0002")) && name.toLowerCase().endsWith(".txt"));
			}
		});
		
		for (File txtFile:lecturas){
			

			System.out.println("**** LECTURAS ***" + txtFile.getName());

			try {
				
				String nombreLectura = txtFile.getName().substring(0,txtFile.getName().toLowerCase().indexOf(".txt"));
				
				//TODO: Verificar de hacer una validacion de lectura repetido en base
				Lectura repetida = new Lectura();
				repetida.setDescripcion(nombreLectura);
				List<Lectura> lecturasRepetidas = lecturaService.listarLecturaFiltradas(repetida, clienteAsp);
						
				if(lecturasRepetidas!=null && lecturasRepetidas.size()>0){
					throw new ScheduledTaskException("interchangeFile.error.repetido");
				}
				
				Lectura lectura = new Lectura();
				LecturaDetalle lecturaDetalle = new LecturaDetalle();
				List<LecturaDetalle> lecturaDetalles = new ArrayList<LecturaDetalle>();
			
				List<String> codigosElementos = new ArrayList<String>();
				boolean banderaLecturaCorrecta = true;
				
				LecturaCodigoBarraUtil lector = new LecturaCodigoBarraUtil();
				List<String> lista = new ArrayList<String>();
				lista = lector.decodificarLectura(txtFile);
				Long orden = (long)0;

				if (lista != null) {
					
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
								lecturaDetalle = new LecturaDetalle();
								if (codigo.startsWith("99")) {
									lecturaDetalle.setCodigoBarras(codigoTomado12);
									lecturaDetalle.setObservacion("Este código de barras pertenece a un módulo");
								} else {
									lecturaDetalle.setCodigoBarras(codigoTomado12);
									Elemento elemento = elementoService.getByCodigo(codigoTomado12, clienteAsp);
									if (elemento != null) {
										lecturaDetalle.setElemento(elemento);
										lecturaDetalle.setObservacion("Elemento "
												+ elemento.getCodigo());
									} else {
										lecturaDetalle.setObservacion("Elemento no existente.");
									}
								}
								if(lecturaDetalles.size()>0)
								{
									for(int f = (lecturaDetalles.size()-1);f>=0;f--)
									{
										if(codigoTomado12.equals(lecturaDetalles.get(f).getCodigoBarras()))
										{
											listaDescartados.add(codigoTomado12+"(Repetido)\n");
											repetido = true;
											break;
										}
									}
								}
									if(!repetido)
									{
										orden++;
										lecturaDetalle.setOrden(orden);
										lecturaDetalles.add(lecturaDetalle);
									}
								//}
//								else
//								{
//									listaDescartados.add(codigo+"(Dígito de control inválido)\n");					
//								}
						}
						else
						{
							listaDescartados.add(codigo+" ----  (Linea errónea)\n");					
						}
					}	
					
					if(listaDescartados.size() > 0)
					{
						String dirDescar = "c://Archivos_de_Lecturas//descartados//";
						new File(dirDescar).mkdirs();
						SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
						File archivoDescartados = new File(dirDescar, "Descartados en" + sd2.format(new Date())
								+ " de " + txtFile.getName());
						BufferedWriter bw = new BufferedWriter(new FileWriter(archivoDescartados));
						bw.write(listaDescartados.toString());
						bw.close();
					}
				}
				
				//archivo.delete();
				lectura.setClienteAsp(clienteAsp);
				lectura.setUsuario(user);
				lectura.setFecha(new Date());
				lectura.setUtilizada(Boolean.TRUE);
				Set<LecturaDetalle> setDetalles = new HashSet<LecturaDetalle>(lecturaDetalles);
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
				lectura.setObservacion("Import Automatico: " + nombreLectura);
				
				Boolean commit = lecturaService.guardarLecturaYDetalles(setDetalles,lectura);
				
				String[] lecturaArchivo = nombreLectura.split("-");
				
				if(lecturaArchivo.length==4){
					if(lecturaArchivo[3].toUpperCase().equals("M")){
						Requerimiento req = requerimientoService.obtenerPorNumero(Long.valueOf(lecturaArchivo[2]), null, empresa.getCodigo(), sucursal.getCodigo(), clienteAsp);
						if(req!=null && req.getRemitoId()!=null)
						{
							Remito remito = remitoService.obtenerPorId(req.getRemitoId());
							if(remito!=null){
								remito.setNumeroManual(Long.valueOf(lecturaArchivo[1]));
								remitoService.actualizarRemito(remito);
							}
						}
					}
				}
				
				txtFile.renameTo(new File(getLecturasProcessed()+"//"+nombreLectura+"_OK.txt"));	
			
			} catch (FileNotFoundException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			} catch (ScheduledTaskException e){
				txtFile.renameTo(new File(getLecturasError()+"//"+txtFile.getName().substring(0, txtFile.getName().indexOf(".txt"))+"_ERROR.txt"));
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
