package com.dividato.configuracionGeneral.tasks;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.springframework.beans.factory.annotation.Autowired;

import com.dividato.configuracionGeneral.tasks.utils.Commons;
import com.dividato.configuracionGeneral.tasks.utils.ScheduledTaskException;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.EmpresaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteRearchivoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteReferenciaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SucursalService;
import com.security.accesoDatos.interfaz.CampoComparacion;
import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.accesoDatos.interfaz.UserService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.LoteRearchivo;
import com.security.modelo.configuraciongeneral.LoteReferencia;
import com.security.modelo.configuraciongeneral.Rearchivo;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.modelo.seguridad.User;
import com.security.recursos.Configuracion;

public class ImportarRearchivos {
	
	private static Logger logger = Logger.getLogger(ImportarRearchivos.class);
	public static final String process = "Process 1: Import Extracts";
	
	private ElementoService elementoService;
	private UserService userService;
	private ClienteEmpService clienteEmpService;
	private ClasificacionDocumentalService clasificacionDocumentalService;
	private ClienteAspService clienteAspService;
	private EmpresaService empresaService;
	private SucursalService sucursalService;
	private LoteRearchivoService loteRearchivoService;
	private LoteReferenciaService loteReferenciaService;
	
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
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}
	@Autowired
	public void setClasificacionDocumentalService(
			ClasificacionDocumentalService clasificacionDocumentalService) {
		this.clasificacionDocumentalService = clasificacionDocumentalService;
	}
	@Autowired
	public void setClienteAspService(ClienteAspService clienteAspService) {
		this.clienteAspService = clienteAspService;
	}
	@Autowired
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	@Autowired
	public void setSucursalService(SucursalService sucursalService) {
		this.sucursalService = sucursalService;
	}
	@Autowired
	public void setLoteRearchivoService(LoteRearchivoService loteRearchivoService) {
		this.loteRearchivoService = loteRearchivoService;
	}
	@Autowired
	public void setLoteReferenciaService(LoteReferenciaService loteReferenciaService) {
		this.loteReferenciaService = loteReferenciaService;
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
		
		ClienteAsp clienteAsp = clienteAspService.obtenerPorId(clienteAspId);
		System.out.println("**** ClienteASP = " + clienteAsp.getNombreAbreviado());
		Empresa empresa = empresaService.obtenerPorId(empresaId);
		System.out.println("**** Empresa = " + empresa.getNombreRazonSocial()); 
		Sucursal sucursal = sucursalService.obtenerPorId(sucursalId);
		System.out.println("**** Sucursal = " + sucursal.getDescripcion());
		User user = new User();
		user.setUsername(username);
		user = ((List<User>)userService.listarTodosUserFiltradosByCliente(user, clienteAsp)).get(0);
		System.out.println("**** Usuario = " + user.getUsernameSinCliente());
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
				return name.toLowerCase().endsWith(".xls");
			}
		});
		
				
		for (File xlsFile:filesRio){
			
			try {
				
			CampoComparacion campo = new CampoComparacion("nombreArchivoPlanilla",xlsFile.getName().toLowerCase());
			List<LoteReferencia> repetidos = loteReferenciaService.listarTodosFiltradoPorLista(campo);
			if(repetidos!=null && repetidos.size()>0){
				throw new ScheduledTaskException("error.planillaReferencias.repetido");
			}

			System.out.println("**** LECTURAS ***" + xlsFile.getName());
			LoteRearchivo lote = null;
	    	LoteReferencia loteReferencia=null;
	    	boolean nuevoLote = true;
	    	boolean cambiarLote = false;
	    	ClienteEmp clienteEmp = null;
	    	ClasificacionDocumental clasificacion = null;
	    	Long idClasificacion = null;
	    	String codigoContenedor = "";
	    	String dir = com.security.constants.Constants.URL_ARCHIVOS_DIGITALES;
			String ruta = "";
			String dirProcesados = "";
			int orden = 0;
			Long ultLote = null;
			List<Rearchivo> rearchivos = new ArrayList<Rearchivo>();
			List<LoteRearchivo> lotes = new ArrayList<LoteRearchivo>();
			List<LoteReferencia> lotesReferencias = new ArrayList<LoteReferencia>();
			List<String> rutasArchivos = new ArrayList<String>();

			
				
				Workbook workbook = null;
				workbook = WorkbookFactory.create(xlsFile); 
				System.out.println("**** Cargo correctamente el archivo al workbook ****");
				//Sheet sheet = workbook.getSheet(sheetName);
	            
				//Get the number of sheets in the xlsx file
				int numberOfSheets = workbook.getNumberOfSheets();
				System.out.println("**** Numero de sheets ****" + numberOfSheets);
	            //loop through each of the sheets
	            for(int i=0; i < numberOfSheets; i++)
	            {
	                
	                //Get the nth sheet from the workbook
	                Sheet sheet = workbook.getSheetAt(i);
	                
	                int iRowNum = sheet.getPhysicalNumberOfRows();
	                 
	                //every sheet has rows, iterate over them
	                //Iterator<Row> rowIterator = sheet.iterator();
	                         
	                 
	                
	                /*while (rowIterator.hasNext())*/
	                for(int f = 6; f < iRowNum ;f++)
	                {
	                    
	                
	                	 //Get the row object
	                    Row row = sheet.getRow(f);// rowIterator.next();
	                    
	                    if(row==null)
	                    	continue;
	                    
	                	
	                	
	                    int iCellNum = row.getPhysicalNumberOfCells();
	                    //Every row has columns, get the column iterator and iterate over them
	                    //Iterator<Cell> cellIterator = row.cellIterator();
	                    
	                    Rearchivo rearNuevo = new Rearchivo();
	                    orden++;
                        String rutaArchivo = "";
	                    
	                    //while (cellIterator.hasNext())
	                    for(int g = 1 ; g <= iCellNum ; g++)
	                    {
	                    	
	                        //Get the Cell object
	                        Cell cell =  row.getCell(g);  //cellIterator.next();                      
	                        
	                        if(cell==null){
	                        	continue;
	                        }
	                        
	                        rearNuevo.setEstado("Pendiente");
	                        rearNuevo.setOrden(orden);
	                       
	                        
                        	cell.setCellType(Cell.CELL_TYPE_STRING); 
	                        //check the cell type and process accordingly
                        	if(g==1){
                        		codigoContenedor = cell.getStringCellValue().trim().toUpperCase();
                        		System.out.println(codigoContenedor);
                        		continue;
  	                        }  
                        	if(g==3){
  	                            String clasi = cell.getStringCellValue().trim().toUpperCase();
  	                            idClasificacion = Long.valueOf(clasi);
  	                            System.out.println(idClasificacion);
  	                            continue;
  	                        }  
	                        if(g==4){
	                            String descripcion = cell.getStringCellValue().trim().toUpperCase();
	                            rearNuevo.setDescripcion(descripcion);
	                            System.out.println(descripcion);
	                            continue;	
	                        }
	                        if(g==5){
	                        	String fecha1Str = cell.getStringCellValue().trim();
	                        	if(fecha1Str!=null && !fecha1Str.trim().equals(""))
	                        		rearNuevo.setFecha1(Configuracion.formatoFechaFormularios.parse(fecha1Str));
	                        	continue;
	                        }
	                        if(g==6){
	                        	String fecha2Str = cell.getStringCellValue().trim();
	                        	if(fecha2Str!=null && !fecha2Str.trim().equals(""))
	                        		rearNuevo.setFecha2(Configuracion.formatoFechaFormularios.parse(fecha2Str));
	                        	continue;
	                        }
	                        if(g==7){
	                        	String numero1Str = cell.getStringCellValue().trim();
	                        	if(numero1Str!=null && !numero1Str.trim().equals(""))
	                        		rearNuevo.setNumero1(Long.valueOf(numero1Str));
	                        	continue;
	                        }
	                        if(g==8){
	                        	String numero2Str = cell.getStringCellValue().trim();
	                        	if(numero2Str!=null && !numero2Str.trim().equals(""))
	                        		rearNuevo.setNumero2(Long.valueOf(numero2Str));
	                        	continue;
	                        }
	                        if(g==9){
	                        	String texto1 = cell.getStringCellValue().trim();
	                        	if(texto1!=null)
	                        		rearNuevo.setTexto1(texto1);
	                        	continue;
	                        }
	                        if(g==10){
	                        	String texto2 = cell.getStringCellValue().trim();
	                        	if(texto2!=null)
	                        		rearNuevo.setTexto2(texto2);
	                        	continue;
	                        }
	                        if(g==11){
	                        	Long nroLote = Long.valueOf(cell.getStringCellValue().trim());
	                        	if(nroLote!=null && ultLote==null)
	                        		ultLote = nroLote;
	                        	
	                        	Row proximaRow = sheet.getRow(f+1);
	                        	Cell proximaCell = null;
	                        	if(proximaRow!=null)
	                        	{
	                        		proximaCell =  proximaRow.getCell(g);
	                        		Double proximoNroLote = proximaCell.getNumericCellValue();
	                        		if(nroLote==null || ultLote==null || proximoNroLote.intValue()!=ultLote.intValue()){
		                        		cambiarLote=true;
		                        		ultLote = proximoNroLote.longValue();
		                        	}
	                        	}
	                        	
	                        	continue;
	                        }

	                        if(g==12){
	                        	 rutaArchivo = cell.getStringCellValue().trim();
	                        	 continue;
	                        }
	                        if(g==13){
	                        	rearNuevo.setNombreArchivoDigital(cell.getStringCellValue().trim());
	                        	rutaArchivo += cell.getStringCellValue().trim();
	                        	rutasArchivos.add(rutaArchivo);
	                        	continue;
	                        }
	                        
	                    } //end of cell iterator
	                    
	                    if(nuevoLote){
	                    	System.out.println("NUEVO LOTE");
                    		lote = new LoteRearchivo();
                        	
                        	CellReference ref = new CellReference("D2");
        	                Row r = sheet.getRow(ref.getRow());
        	                if (r != null) {
        	                   Cell c = r.getCell(ref.getCol());
        	                   String clienteCodigo = c.getStringCellValue();
        	                   clienteEmp = clienteEmpService.getByCodigo(""+clienteCodigo
        	                		   +"", empresa.getCodigo(), clienteAsp);
        	                   System.out.println("CLIENTEEMP = " + clienteEmp.getNombreRazonSocial());
        	                }
        	                
        	                Elemento contenedor = null;
        	                contenedor = elementoService.getByCodigo(codigoContenedor, clienteAsp);
        	                System.out.println("CONTENEDOR = " + contenedor.getCodigo());
                        	clasificacion = clasificacionDocumentalService.obtenerPorId(idClasificacion);
                        	System.out.println("clasificacion = " + clasificacion.getCodigo());
                        	
                        	lote.setClasificacionDocumental(clasificacion);
                        	lote.setClienteAsp(clienteAsp);
                        	lote.setClienteEmp(clienteEmp);
                        	lote.setCodigoCliente(clienteEmp.getCodigo());
                        	lote.setFechaRegistro(new Date());
                        	lote.setEmpresa(empresa);
                        	lote.setSucursal(sucursal);
                        	lote.setIndiceIndividual(Boolean.FALSE);
                        	lote.setIndiceIndividualStr("0");
                        	lote.setTipo("Digital");
                        	lote.setDescripcion("Lote procesado automaticamente el dia" +  Configuracion.formatoFechaHoraFormularios.format(new Date()));
        					lote.setContenedor(contenedor);
        					lote.setUsuario_resp1(user);
        					
        					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        					String fecha = sdf.format(new Date());
        					ruta = lote.getClienteAsp().getNombreAbreviado()+"//"
        							+ lote.getEmpresa().getCodigo()+"//"+lote.getSucursal().getCodigo()+"//"
        							+ lote.getClienteEmp().getCodigo()+"//"+lote.getClasificacionDocumental().getCodigo()+"//"
        							+ fecha + "//";
        					
        					loteReferencia = new LoteReferencia();
        					loteReferencia.setNombreArchivoPlanilla(xlsFile.getName().toLowerCase());
        					lote.setNombreArchivoPlanilla(xlsFile.getName().toLowerCase());
        					rearchivos.clear();
        					orden=1;
        					
        					}
                        
	                    	rearNuevo.setLoteRearchivo(lote);
	                    	
                    		dirProcesados = dir+"PDF"+"//"+ruta;
                    		new File(dirProcesados).mkdirs();
                    		nuevoLote = false;
                            
                         	lote.setCantidad(orden);
                         	String rutaReplaced = ruta.replace("/", "\\");
 	        				rearNuevo.setPathArchivoDigital(rutaReplaced);
 	        				
 	        				rearNuevo.setCantidadImagenes(1);		
 	        				rearNuevo.setPathArchivoJPGDigital(dirProcesados);
 	        			    rearchivos.add(rearNuevo);
 	        			    
 	        			   if(orden>99 || cambiarLote || f==(iRowNum-1))
 	        			   {
 	        				   lote.setRearchivos(new HashSet<Rearchivo>(rearchivos));
 	        				   lotes.add(lote);
 	        				   nuevoLote = true;
 	        				   cambiarLote = false;
 	        				   setDataLoteReferencia(loteReferencia, lote);
 	        				   lotesReferencias.add(loteReferencia);
 	        			   }

	                } //end of rows iterator
	                
	                
	                for(int c = 0;c<lotes.size();c++){
	        			loteRearchivoService.guardarActualizar(lotes.get(c),lotesReferencias.get(c));	
	        		}
	                
	                for(String rutaArchivo:rutasArchivos){
		                File dataInputFile = new File(rutaArchivo);
				    	//New path
				    	File fileSendPath = new File(dirProcesados+"//", dataInputFile.getName());
				    	//Moving the file.
				    	dataInputFile.renameTo(fileSendPath);
        			}
 
	            } //end of sheets for loop
	            
	            
	            workbook.close(); 
	            //close file input stream
	            xlsFile.renameTo(new File(getLecturasProcessed()+"//"+xlsFile.getName()));
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			} catch (EncryptedDocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			} catch (ScheduledTaskException e) {
				// TODO Auto-generated catch block
				//close file input stream
	            xlsFile.renameTo(new File(getLecturasError()+"//"+Configuracion.formatoFechaNombreArchivos.format(new Date())+"-"+xlsFile.getName()));
				e.printStackTrace();
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			}
		}
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
				//Referencia referenciaBuscar = referenciaService.obtenerParaRearchivo(rearchivo.getLoteRearchivo().getClienteEmp(), rearchivo.getLoteRearchivo().getClasificacionDocumental(), rearchivo.getNumero1());
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
					referencia.setIndiceIndividual(new Boolean(false));
					referencia.setLoteReferencia(loteReferencia);
					referencia.setNumero1(rearchivo.getNumero1());
					referencia.setNumero2(rearchivo.getNumero2());
//					if(referenciaBuscar!=null)
//						referencia.setReferenciaRearchivo(referenciaBuscar);
					referencia.setTexto1(rearchivo.getTexto1());
					referencia.setTexto2(rearchivo.getTexto2());
					referencia.setOrdenRearchivo(rearchivo.getOrden());
					referencia.setPathArchivoDigital(rearchivo.getPathArchivoDigital());
					referencia.setPathLegajo(rearchivo.getPathArchivoJPGDigital()+rearchivo.getNombreArchivoDigital());
					referencias.add(referencia);	
					rearchivo.setReferencia(referencia);
				//}
			}
		}
		return referencias;
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


}
