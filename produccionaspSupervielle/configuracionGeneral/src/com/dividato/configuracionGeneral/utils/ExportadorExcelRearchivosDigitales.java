package com.dividato.configuracionGeneral.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import jxl.Workbook;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaService;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.LoteExportacionRearchivo;
import com.security.modelo.configuraciongeneral.Rearchivo;

/**
 * TODO: configurar por spring
 * @author Federico Mz
 *
 */
public class ExportadorExcelRearchivosDigitales {
	private WritableWorkbook spreadsheet;
	private CellFormat cellFormats[] = null;
	private WritableCellFeatures cellFeatures[] = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private int actualRow = 9;
	
	private ExportadorExcelRearchivosDigitales(){}
	public static ExportadorExcelRearchivosDigitales getNewInstance(){
		return new ExportadorExcelRearchivosDigitales();
	}
	
	public void createWorkbook(OutputStream out, InputStream inp, Date fecha, ClienteEmp cliente, ClasificacionDocumental clasificacion) throws BiffException, IOException, RowsExceededException, WriteException {
		if(cellFormats!=null)
			throw new IllegalStateException("Obtenga una nueva instancia de ExportadorExcelRearchivosDigitales para exportar un segundo excel");
		
		Workbook baseWorkbook = Workbook.getWorkbook(inp);
		spreadsheet = Workbook.createWorkbook(out, baseWorkbook);
		
		readCellFormatAndFeatures();

		WritableSheet sheet = spreadsheet.getSheet(0);
		addLabelRespectCellFormat(sheet, 4, 1, getString((fecha)));
		addLabelRespectCellFormat(sheet, 5, 1, cliente.getRazonSocialONombreYApellido());
		addLabelRespectCellFormat(sheet, 6, 1, clasificacion.getNombre());
	}
	
	public void addRearchivo(Rearchivo rearchivo) throws BiffException, RowsExceededException,WriteException {
		WritableSheet sheet = spreadsheet.getSheet(0);
		addLabels(sheet, actualRow,
				rearchivo.getLoteRearchivo().getContenedor().getCodigo()+"\\"+rearchivo.getLoteRearchivo().getId()+"\\"+rearchivo.getNombreArchivoDigital(),
				rearchivo.getLoteRearchivo().getContenedor().getCodigo(),
				getString(rearchivo.getId()),
				getString(rearchivo.getOrden()),
				rearchivo.getFecha1Str(),
				rearchivo.getFecha2Str(),
				getString(rearchivo.getNumero1()),
				getString(rearchivo.getNumero2()),
				rearchivo.getTexto1(),
				rearchivo.getTexto2(),
				rearchivo.getDescripcion()
				);
		sheet.addHyperlink(new WritableHyperlink(0, actualRow, new File(rearchivo.getLoteRearchivo().getContenedor().getCodigo()+"//"+rearchivo.getLoteRearchivo().getId()+"//"+rearchivo.getNombreArchivoDigital())));
		actualRow++;
	}
	public void close() throws IOException, WriteException{
		spreadsheet.write();
		spreadsheet.close();
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
	

	private void addLabels(WritableSheet sheet, int row, String... content)
			throws RowsExceededException, WriteException {
		for (int col = 0; col < content.length; col++) {
			if(content[col]!=null)
				addLabelWithStandardCellFormat(sheet, row, col, content[col]);
			else
				addLabelWithStandardCellFormat(sheet, row, col, "");
		}
	}

	private void addLabelWithStandardCellFormat(WritableSheet sheet, int row, int col, String content)
			throws RowsExceededException, WriteException {
		CellFormat cellFormat = cellFormats[col];
		if(cellFormat!=null)
			sheet.addCell(new Label(col, row, content, cellFormat));
		else
			sheet.addCell(new Label(col, row, content));
		WritableCellFeatures cellFeature = cellFeatures[col];
		if (cellFeature != null)
			sheet.getWritableCell(col, row).setCellFeatures(cellFeature);
	}
	
	private void addLabelRespectCellFormat(WritableSheet sheet, int row, int col, String content)
			throws RowsExceededException, WriteException {
		WritableCell actualCell = sheet.getWritableCell(col, row);
		CellFormat cellFormat = actualCell != null ? actualCell.getCellFormat() : null;
		WritableCellFeatures cellFeature = actualCell != null ? actualCell.getWritableCellFeatures() : null;
		if(cellFormat!=null)
			sheet.addCell(new Label(col, row, content, cellFormat));
		else
			sheet.addCell(new Label(col, row, content));
		if (cellFeature != null)
			sheet.getWritableCell(col, row).setCellFeatures(cellFeature);
	}
	
	
	private void readCellFormatAndFeatures(){
		cellFormats = new CellFormat[11];
		cellFeatures = new WritableCellFeatures[11];
		
		WritableSheet sheet = spreadsheet.getSheet(0);
		for(int col = 0;col < 11;col++){
			WritableCell actualCell = sheet.getWritableCell(col, 9);
			cellFormats[col] = actualCell != null ? actualCell.getCellFormat() : null;
			cellFeatures[col] = actualCell != null ? actualCell.getWritableCellFeatures() : null;
		}
	}
	
}
