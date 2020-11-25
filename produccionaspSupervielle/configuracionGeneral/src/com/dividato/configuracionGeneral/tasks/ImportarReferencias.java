package com.dividato.configuracionGeneral.tasks;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
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
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
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

public class ImportarReferencias {
	
	private static Logger logger = Logger.getLogger(ImportarReferencias.class);
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
	private ReferenciaService referenciaService;
	
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
	@Autowired
	public void setReferenciaService(ReferenciaService referenciaService) {
		this.referenciaService = referenciaService;
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
	    	Integer codigoClasificacion = null;
	    	String codigoContenedor = "";
	    	String codigoElemento = "";
	    	boolean indiceIndividual = false;
			int orden = 0;
			Long ultLote = null;
			List<Rearchivo> rearchivos = new ArrayList<Rearchivo>();
			List<LoteRearchivo> lotes = new ArrayList<LoteRearchivo>();
			List<LoteReferencia> lotesReferencias = new ArrayList<LoteReferencia>();
			
				
				Workbook workbook = null;
				workbook = WorkbookFactory.create(xlsFile); 
				System.out.println("**** Cargo correctamente el archivo al workbook ****");
				//Sheet sheet = workbook.getSheet(sheetName);
	            
				//Get the number of sheets in the xlsx file
				int numberOfSheets = workbook.getNumberOfSheets();
				System.out.println("**** Numero de sheets ****" + numberOfSheets);
				
				
	            //loop through each of the sheets
				//RECORRE LAS HOJAS
	            for(int i=0; i < numberOfSheets; i++)
	            {
	                
	                //Get the nth sheet from the workbook
	                Sheet sheet = workbook.getSheetAt(i);
	                
	                int iRowNum = sheet.getPhysicalNumberOfRows();
	                 
	                
	                
	              //BUCA EN LA CELDA DE CABECERA PREDETERMINADA EL CODIGO DEL CLIENTE-EMP
                	CellReference refe = new CellReference("D2");
	                Row r = sheet.getRow(refe.getRow());
	                if (r != null) {
	                   Cell c = r.getCell(refe.getCol());
	                   String clienteCodigo = c.getStringCellValue();
	                   clienteEmp = clienteEmpService.getByCodigo(""+clienteCodigo
	                		   +"", empresa.getCodigo(), clienteAsp);
	                   System.out.println("CLIENTEEMP = " + clienteEmp.getNombreRazonSocial());
	                }
	                
	                //every sheet has rows, iterate over them
	                //Iterator<Row> rowIterator = sheet.iterator();
	                         
	                
	                /*while (rowIterator.hasNext())*/
	                //RECORRE LAS FILAS
	                for(int f = 6; f < iRowNum ;f++)
	                {
	                	 //Get the row object
	                    Row row = sheet.getRow(f);// rowIterator.next();
	                    
	                    if(row==null)
	                    	continue;
	                    
	                	CellReference cellTitulo = new CellReference("B6");
		                Row rowTitulo = sheet.getRow(cellTitulo.getRow());
	                    int iCellNum = (Short.valueOf(rowTitulo.getLastCellNum()).intValue()-1);
	                    //Every row has columns, get the column iterator and iterate over them
	                    //Iterator<Cell> cellIterator = row.cellIterator();
	                    
	                    Rearchivo rearNuevo = new Rearchivo();
	                    orden++;
                        String rutaArchivo = "";
	                    
	                    //while (cellIterator.hasNext())
                        //RECORREMOS LAS CELDAS DE LA FILA
	                    for(int g = 1 ; g <= iCellNum ; g++)
	                    {
	                    	
	                        //Get the Cell object
	                        Cell cell =  row.getCell(g);  //cellIterator.next();                      
	                        
	                        if(cell==null){
	                        	continue;
	                        }

	                        rearNuevo.setEstado("Pendiente");
	                        rearNuevo.setOrden(orden);
	                       
	                       
	                        //check the cell type and process accordingly
                        	if(g==1){
                        		//OBTIENE CODIGO CONTENEDOR
                        		cell.setCellType(Cell.CELL_TYPE_STRING);
                        		codigoContenedor = cell.getStringCellValue().trim().toUpperCase();
                        		System.out.println(codigoContenedor);
                        		continue;
  	                        }  
                        	if(g==2){
                        		//OBTIENE CODIGO ELEMENTO
                        		cell.setCellType(Cell.CELL_TYPE_STRING);
                        		codigoElemento = cell.getStringCellValue();
                        		System.out.println(codigoElemento);
                        		if(codigoElemento!=null)
                        			codigoElemento = codigoElemento.trim().toUpperCase();
                        		if(!codigoElemento.equals(""))
                        		{
                        			Elemento elemento = null;
                        			elemento = elementoService.getByCodigo(codigoElemento, clienteAsp);
                        			if(elemento!=null){
                        				indiceIndividual = true;
                        				rearNuevo.setElemento(elemento);
                        				System.out.println("ELEMENTO = " + elemento.getCodigo());
                        			}
                 	            }
                        		continue;
  	                        }  
                        	if(g==3){
                        		//OBTIENE CODIGO CLASIFICACION DOCUMENTAL
                        		cell.setCellType(Cell.CELL_TYPE_STRING);
  	                            String clasi = cell.getStringCellValue().trim().toUpperCase();
  	                            codigoClasificacion = Integer.valueOf(clasi);
  	                            clasificacion = clasificacionDocumentalService.getByCodigo(codigoClasificacion, clienteEmp.getCodigo(),clienteAsp);
  	                            if(clasificacion!=null)
  	                            	rearNuevo.setClasifDoc(clasificacion);
  	                            else{
  	                            	System.out.println("No se encontro codigo clasificacion:" + clasi);
  	                            }
  	                            continue;
  	                        }  
	                        if(g==4){
	                        	//OBTIENE DESCRIPCION
	                        	cell.setCellType(Cell.CELL_TYPE_STRING);
	                            String descripcion = cell.getStringCellValue().trim().toUpperCase();
	                            rearNuevo.setDescripcion(descripcion);
	                            System.out.println(descripcion);
	                            continue;	
	                        }
	                        if(g==5){
	                        	//OBTIENE FECHA 1 
	                        	Date fecha1Str = cell.getDateCellValue();
	                        	if(fecha1Str!=null)
	                        		rearNuevo.setFecha1(fecha1Str);
	                        	continue;
	                        }
	                        if(g==6){
	                        	//OBTIENE FECHA 2
	                        	Date fecha2Str = cell.getDateCellValue();
	                        	if(fecha2Str!=null)
	                        		rearNuevo.setFecha2(fecha2Str);
	                        	continue;
	                        }
	                        if(g==7){
	                        	//OBTIENE NUMERO 1
	                        	cell.setCellType(Cell.CELL_TYPE_STRING);
	                        	String numero1Str = cell.getStringCellValue().trim();
	                        	if(numero1Str!=null && !numero1Str.trim().equals(""))
	                        		rearNuevo.setNumero1(Long.valueOf(numero1Str));
	                        	continue;
	                        }
	                        if(g==8){
	                        	//OBTIENE NUMERO 2
	                        	cell.setCellType(Cell.CELL_TYPE_STRING);
	                        	String numero2Str = cell.getStringCellValue().trim();
	                        	if(numero2Str!=null && !numero2Str.trim().equals(""))
	                        		rearNuevo.setNumero2(Long.valueOf(numero2Str));
	                        	continue;
	                        }
	                        if(g==9){
	                        	//OBTIENE TEXTO 1
	                        	cell.setCellType(Cell.CELL_TYPE_STRING);
	                        	String texto1 = cell.getStringCellValue().trim();
	                        	if(texto1!=null && !texto1.trim().equals(""))
	                        		rearNuevo.setTexto1(texto1);
	                        	continue;
	                        }
	                        if(g==10){
	                        	//OBTIENE TEXTO 2
	                        	cell.setCellType(Cell.CELL_TYPE_STRING);
	                        	String texto2 = cell.getStringCellValue().trim();
	                        	if(texto2!=null && !texto2.trim().equals(""))
	                        		rearNuevo.setTexto2(texto2);
	                        	continue;
	                        }
	                        if(g==11){
	                        	//OBTIENE CODIGO LOTE IDENTIFICA CUANDO CAMBIAR Y CREAR UNO NUEVO
	                        	cell.setCellType(Cell.CELL_TYPE_STRING);
	                        	String nroLoteStr = cell.getStringCellValue().trim();
	                        	if(nroLoteStr!=null && !nroLoteStr.trim().equals("")){
	                        		Long nroLote = Long.valueOf(nroLoteStr);
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
	                        	} 
	                        	continue;
	                        }

	                        if(g==12){
	                        	//OBTIENE RUTA EN DISCO DE LA IMAGEN DEL REARCHIVO
	                        	 cell.setCellType(Cell.CELL_TYPE_STRING);
	                        	 rutaArchivo = cell.getStringCellValue();
	                        	 if(rutaArchivo!=null){
	                        		 rutaArchivo = rutaArchivo.trim();
	                        		 rearNuevo.setPathArchivoDigital(rutaArchivo);
	                        	 }
	                        	 continue;
	                        }
	                        if(g==13){
	                        	//OBTIENE EL NOMBRE DEL ARCHIVO IMAGEN DEL REARCHIVO
	                        	cell.setCellType(Cell.CELL_TYPE_STRING);
	                        	String nombreArchivo = cell.getStringCellValue();
	                        	if(nombreArchivo!=null)
	                        	{
	                        		rearNuevo.setNombreArchivoDigital(nombreArchivo.trim());
	                        		rearNuevo.setPathArchivoJPGDigital(rutaArchivo + nombreArchivo);
	                        	}
	                        	//rutasArchivos.add(rutaArchivo);
	                        	continue;
	                        }
	                        
	                    } //end of cell iterator
	                    
	                    if(nuevoLote){
	                    	System.out.println("NUEVO LOTE");
                    		lote = new LoteRearchivo();
                        	                    		
        	                
        	                Elemento contenedor = null;
        	                contenedor = elementoService.getByCodigo(codigoContenedor, clienteAsp);
        	                System.out.println("CONTENEDOR = " + contenedor.getCodigo());
        	                  	                
                       	
                        	lote.setClasificacionDocumental(clasificacion);
                        	lote.setClienteAsp(clienteAsp);
                        	lote.setClienteEmp(clienteEmp);
                        	lote.setCodigoCliente(clienteEmp.getCodigo());
                        	lote.setFechaRegistro(new Date());
                        	lote.setEmpresa(empresa);
                        	lote.setSucursal(sucursal);
                        	lote.setIndiceIndividual(indiceIndividual);
                        	lote.setIndiceIndividualStr(indiceIndividual==true?"1":"0");
                        	lote.setTipo("Digital");
                        	lote.setDescripcion("Lote procesado automaticamente el dia" +  Configuracion.formatoFechaHoraFormularios.format(new Date()));
        					lote.setContenedor(contenedor);
        					lote.setUsuario_resp1(user);
        					
        					loteReferencia = new LoteReferencia();
        					loteReferencia.setNombreArchivoPlanilla(xlsFile.getName().toLowerCase());
        					lote.setNombreArchivoPlanilla(xlsFile.getName().toLowerCase());
        					rearchivos.clear();
        					orden=1;
        					
        					}
                        
	                    	rearNuevo.setLoteRearchivo(lote);
	                    	
                    		//dirProcesados = dir+"PDF"+"//"+ruta;
                    		//new File(dirProcesados).mkdirs();
                    		nuevoLote = false;
                            
                         	lote.setCantidad(orden);
                         	//String rutaReplaced = ruta.replace("/", "\\");
 	        				//rearNuevo.setPathArchivoDigital(rutaReplaced);
 	        				
 	        				rearNuevo.setCantidadImagenes(1);		
 	        				//rearNuevo.setPathArchivoJPGDigital(dirProcesados);
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
	                
	                
	                for(int c = 0;c<lotes.size();c++)
	                {
	                	if(lotesReferencias.get(c).getIndiceIndividual())
	                	{
	                		boolean primeraVez = true;
	                		String idReferencias = "";
	                		Integer cantRefe = new Integer(0);
	                		for(Referencia ref:lotesReferencias.get(c).getReferencias())
	                		{
	                			if(primeraVez)
	                			{
	        						idReferencias = ref.getElemento().getId().toString();
	        						primeraVez = false;
	        					}else{
	        						idReferencias += ","+ref.getElemento().getId().toString(); 
	        					}
	                		}
	                		if(!idReferencias.equals("")){
	        					cantRefe = referenciaService.cantReferenciasExistenPorElementos(idReferencias, clienteAsp.getId());
	        				}
	                		if(cantRefe.longValue() > 0)
	                		{
	                			throw new ScheduledTaskException("error.planillaReferencias.ReferenciasRepetidas");
	                		}
	                	}
	                	loteReferenciaService.guardarActualizarLoteYModificadas(lotesReferencias.get(c), null, null);
	        			//loteRearchivoService.guardarActualizar(lotes.get(c),lotesReferencias.get(c));	
	        		}
	                
	                //SE COMENTA POR QUE SON SOLO REFERENCIAS
//	                for(String rutaArchivo:rutasArchivos){
//		                File dataInputFile = new File(rutaArchivo);
//				    	//New path
//				    	File fileSendPath = new File(dirProcesados+"//", dataInputFile.getName());
//				    	//Moving the file.
//				    	dataInputFile.renameTo(fileSendPath);
//        			}
 
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
			} catch (ScheduledTaskException e) {
				// TODO Auto-generated catch block
				//close file input stream
	            xlsFile.renameTo(new File(getLecturasError()+"//"+Configuracion.formatoFechaNombreArchivos.format(new Date())+"-"+xlsFile.getName()));
				e.printStackTrace();
				System.out.println(e.getMessage());
				logger.error(e.getMessage());
			}catch (RuntimeException e) {
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
		loteReferencia.setIndiceIndividual(loteRearchivo.getIndiceIndividual());
		loteReferencia.setReferencias(crearReferencias(loteReferencia, loteRearchivo.getRearchivos()));
		loteReferencia.setUsuario(loteRearchivo.getUsuario_resp1());
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
					//referencia.setDescripcionRearchivo("Rearchivo");
					referencia.setClasificacionDocumental(rearchivo.getClasifDoc());
					if(loteReferencia.getIndiceIndividual()){
						referencia.setElemento(rearchivo.getElemento());
						referencia.setContenedor(rearchivo.getLoteRearchivo().getContenedor());
					}
					else
						referencia.setElemento(rearchivo.getLoteRearchivo().getContenedor());
					referencia.setFecha1(rearchivo.getFecha1());
					referencia.setFecha2(rearchivo.getFecha2());
					//Siempre son individuales
					referencia.setIndiceIndividual(loteReferencia.getIndiceIndividual());
					referencia.setLoteReferencia(loteReferencia);
					referencia.setNumero1(rearchivo.getNumero1());
					referencia.setNumero2(rearchivo.getNumero2());
//					if(referenciaBuscar!=null)
//						referencia.setReferenciaRearchivo(referenciaBuscar);
					referencia.setTexto1(rearchivo.getTexto1());
					referencia.setTexto2(rearchivo.getTexto2());
					referencia.setOrdenRearchivo(rearchivo.getOrden());
					referencia.setPathArchivoDigital(rearchivo.getPathArchivoDigital());
					referencia.setPathLegajo(rearchivo.getPathArchivoJPGDigital());
					referencia.setFechaHora(new Date());
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
