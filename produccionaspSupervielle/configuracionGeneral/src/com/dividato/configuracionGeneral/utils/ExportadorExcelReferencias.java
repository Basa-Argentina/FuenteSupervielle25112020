package com.dividato.configuracionGeneral.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.security.accesoDatos.configuraciongeneral.hibernate.ClasificacionDocumentalServiceImp;
import com.security.accesoDatos.configuraciongeneral.interfaz.ClasificacionDocumentalService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;

/**
 * TODO: configurar por spring
 * @author FedeMz
 *
 */
public class ExportadorExcelReferencias {
	WritableWorkbook spreadsheet;
	private CellFormat cellFormats[][] = null;
	private WritableCellFeatures cellFeatures[][] = null;
	private List<Long> tiposElemento = null;
	private int rowIndice = 3;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private Hashtable<Long,ClasificacionDocumental> arrayClasificacionesCompleto = new Hashtable<Long,ClasificacionDocumental>();
	
	private ReferenciaService referenciaService;
	
	private ExportadorExcelReferencias(ReferenciaService referenciaService){
		this.referenciaService=referenciaService;
	}
	public static ExportadorExcelReferencias getNewInstance(ReferenciaService referenciaService){
		return new ExportadorExcelReferencias(referenciaService);
	}
	/**
	 * Escribe el excel en el output stream, pero no lo lo cierra.
	 * @param out
	 * @param inp
	 * @param clasificacionesDocumentales
	 * @param tiposElemento
	 * @throws IOException
	 * @throws BiffException
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	public void createWorkbook(OutputStream out, InputStream inp, List<ClasificacionDocumental> clasificacionesDocumentales,List<Long> tiposElemento) throws IOException, BiffException, RowsExceededException,WriteException {
		if(cellFormats!=null)
			throw new IllegalStateException("Obtenga una nueva instancia de ExportadorExcelReferencias para exportar un segundo excel");
		
		Workbook baseWorkbook = Workbook.getWorkbook(inp);
		spreadsheet = Workbook.createWorkbook(out, baseWorkbook);
		this.tiposElemento=tiposElemento;
		readCellFormatAndFeatures(spreadsheet);
		
		int row = 3;
		ClasificacionDocumentalService service = new ClasificacionDocumentalServiceImp(HibernateControl.getInstance());
		for(ClasificacionDocumental clasificacion:clasificacionesDocumentales){
			ClasificacionDocumental clasificacionConHijos = service.getClasificacionByCodigoCargarHijos(clasificacion.getCodigo(), clasificacion.getClienteEmp().getCodigo(), clasificacion.getClienteAsp(), null);
			row = generarArbolClasificacion(row,0,clasificacionConHijos);
		}
		
		generarPorElemento();

		spreadsheet.write();
		spreadsheet.close();
		out.flush();
		//out.close();
	}

	

	private int generarArbolClasificacion(int row, int prof, ClasificacionDocumental clasificacion) throws RowsExceededException, WriteException {
		arrayClasificacionesCompleto.put(clasificacion.getId(),clasificacion);
		WritableSheet sheet = spreadsheet.getSheet(0);
		if(prof>8)prof=8;
		String[] values = new String[15];
		for(int i=0;i<prof;i++)
			values[i]="- - - - - -";
		String codigo = clasificacion.getCodigo().toString();
		while(codigo.length()<4)codigo="0"+codigo;
		values[prof] = "DOC:"+codigo+" - "+clasificacion.getNombre();
		if(clasificacion.getNodo().equals("N"))
			values[11] = "SECCION";
		else{
			values[11] = "DOCUMENTOS";
			values[12] = clasificacion.tieneFechaRequerido()?"SI":"NO";
			values[13] = clasificacion.tieneNumeroRequerido()?"SI":"NO";
			values[14] = clasificacion.tieneTextoRequerido()?"SI":"NO";			
		}
		addLabels(sheet, 0, row++, values);
		generarPorIndice(clasificacion);
		for(ClasificacionDocumental clasificacionHija : clasificacion.getListaHijosOrdenada())
			row = generarArbolClasificacion(row, prof+1, clasificacionHija);
		return row;
	}
	
	private void generarPorIndice(ClasificacionDocumental clasificacion) throws RowsExceededException, WriteException {
		WritableSheet sheet = spreadsheet.getSheet(1);
		String values[] = new String[10];
		String codigo = clasificacion.getCodigo().toString();
		while(codigo.length()<4)codigo="0"+codigo;
		String nombreClasificacion = "DOC:"+codigo+" //"+clasificacion.getClienteEmp().getRazonSocialONombreYApellido() + clasificacion.getNombreCompleto();
		values[0] = nombreClasificacion;
		addLabels(sheet, 1, rowIndice++, values);
		List<Map<String,Object>> referencias = referenciaService.listarPorClasificacionDocumental(clasificacion,tiposElemento);
		for(Map<String,Object> referencia:referencias){
			values = new String[10];
			values[0] = nombreClasificacion;
			values[1] = getString(referencia.get("descTipoElemento"));
			values[2] = getString(referencia.get("codigoElemento"));
			values[3] = getString(referencia.get("descripcion"));
			values[4] = getString(referencia.get("fecha1"));
			values[5] = getString(referencia.get("fecha2"));
			values[6] = getString(referencia.get("numero1"));
			values[7] = getString(referencia.get("numero1"));
			values[8] = getString(referencia.get("texto1"));
			values[9] = getString(referencia.get("texto1"));
			addLabels(sheet, 1, rowIndice++, values);
		}
	}
	
	private void generarPorElemento() throws RowsExceededException, WriteException {
		WritableSheet sheet = spreadsheet.getSheet(2);
		int row = 3;
		String values[] = new String[10];
		List<Map<String,Object>> referencias = referenciaService.listarPorClasificacionDocumental(arrayClasificacionesCompleto.keySet(),tiposElemento);
		if(referencias.size()==0){
			sheet.removeRow(row);
		}
		
		for(Map<String,Object> referencia:referencias){
			ClasificacionDocumental clasificacion = arrayClasificacionesCompleto.get(referencia.get("idClasificacion"));
			String codigo = clasificacion.getCodigo().toString();
			while(codigo.length()<4)codigo="0"+codigo;
			String nombreClasificacion = "DOC:"+codigo+" //"+clasificacion.getClienteEmp().getRazonSocialONombreYApellido() + clasificacion.getNombreCompleto();
			values[0] = nombreClasificacion;
			values[1] = getString(referencia.get("descTipoElemento"));
			values[2] = getString(referencia.get("codigoElemento"));
			values[3] = getString(referencia.get("descripcion"));
			values[4] = getString(referencia.get("fecha1"));
			values[5] = getString(referencia.get("fecha2"));
			values[6] = getString(referencia.get("numero1"));
			values[7] = getString(referencia.get("numero1"));
			values[8] = getString(referencia.get("texto1"));
			values[9] = getString(referencia.get("texto1"));
			addLabels(sheet, 1, row++, values);
		}
	}
	
	private String getString(Object object) {
		if(object == null)
			return null;
		else if(object instanceof Date){
			return sdf.format((Date)object);
		}
		else
			return object.toString();
	}

	private void addLabels(WritableSheet sheet,int sh, int row, String... content)
			throws RowsExceededException, WriteException {
		for (int col = 0; col < content.length; col++) {
			if(content[col]!=null)
				addLabel(sheet, sh, row, col, content[col]);
			else
				removeLabel(sheet, row, col);
		}
	}

	

	private void addLabel(WritableSheet sheet, int sh, int row, int col, String content)
			throws RowsExceededException, WriteException {
		CellFormat cellFormat = cellFormats[sh][col];
		if(cellFormat!=null)
			sheet.addCell(new Label(col, row, content, cellFormat));
		else
			sheet.addCell(new Label(col, row, content));
		WritableCellFeatures cellFeature = cellFeatures[sh][col];
		if (cellFeature != null)
			sheet.getWritableCell(col, row).setCellFeatures(cellFeature);
	}
	
	private void removeLabel(WritableSheet sheet, int row, int col) throws RowsExceededException, WriteException {
		sheet.addCell(new Blank(col, row));
	}
	
	private void readCellFormatAndFeatures(WritableWorkbook workbook){
		cellFormats = new CellFormat[3][15];
		cellFeatures = new WritableCellFeatures[3][15];
		for(int sh = 0;sh < 3;sh++){
			WritableSheet sheet = workbook.getSheet(sh);
			for(int col = 0;col < 15;col++){
				WritableCell actualCell = sheet.getWritableCell(col, 3);
				cellFormats[sh][col] = actualCell != null ? actualCell.getCellFormat() : null;
				cellFeatures[sh][col] = actualCell != null ? actualCell.getWritableCellFeatures() : null;
			}
		}
	}
}
