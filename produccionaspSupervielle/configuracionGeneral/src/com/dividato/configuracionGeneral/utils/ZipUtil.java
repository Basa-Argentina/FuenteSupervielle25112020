/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 23/03/2011
 */
package com.dividato.configuracionGeneral.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Gabriel Mainero
 *
 */
public class ZipUtil {

	private static final int BUFFER_SIZE = 2048;
	 
    
	public static void unZip(String pZipFile, String rutaDestino, List<String> listaNombresArchivos, List<String> listaPathsArchivos) throws Exception {
		BufferedOutputStream bos = null;
		FileInputStream fis = null;
		ZipInputStream zipis = null;
		FileOutputStream fos = null;
		String pFile = "";
		try {
			fis = new FileInputStream(pZipFile);
			zipis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entrada;
			while (null != (entrada=zipis.getNextEntry())){
				int len = 0;
				byte[] buffer = new byte[BUFFER_SIZE];
				pFile = rutaDestino + entrada.getName();
				
				if(listaNombresArchivos!=null && listaPathsArchivos!=null){
					listaNombresArchivos.add(entrada.getName());
					listaPathsArchivos.add(pFile);
				}
				fos = new FileOutputStream(pFile);
				bos = new BufferedOutputStream(fos, BUFFER_SIZE);
	
				while  ((len = zipis.read(buffer, 0, BUFFER_SIZE)) != -1)
					bos.write(buffer, 0, len);
				bos.close();
				fos.close();
				zipis.closeEntry();

			} 
		} catch (Exception e) {
			throw e;
		} finally {
			zipis.close();
			fis.close();
		} // end try
	} // end UnZip

	
	private static Date getDateTimeFromFileName(String name){
		String subDate = name.substring(7, 15);
		String subTime = name.substring(16, 22);
		Date d = getDateFormated(subDate + subTime, "yyyyMMddHHmmss");
		return d;
	}
	
	private static Date getDateFormated(String date, String pattern){
    	SimpleDateFormat sdfmt = new SimpleDateFormat(pattern);
    	try {
    		if(date!=null){    			
    			return sdfmt.parse(date);    			
    		}	
		} catch (ParseException e) {
			return null;
		}
		return null;
    }
}
