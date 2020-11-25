package com.security.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class LecturaCodigoBarraUtil {
	private static String prefijoModulo = "99";
	private static Logger logger = Logger.getLogger(LecturaCodigoBarraUtil.class);
		
	public List<String> decodificarLectura(File file) throws FileNotFoundException{
		List<String> lecturasList = new ArrayList<String>();
		if(file!= null && file.exists()){
			FileReader fr = new FileReader(file);
			BufferedReader bufRead = new BufferedReader(fr);
			String codigoBarra = null;
			boolean hasNext = true;
			do{
				try {
					codigoBarra = bufRead.readLine();
					
					if(codigoBarra == null){
						hasNext = false; 
					}else {
						codigoBarra = codigoBarra.trim();
						if (!"".equals(codigoBarra)) {
							lecturasList.add(codigoBarra);
						}
					}				
				} catch (IOException e) {
					logger.error(e);
					hasNext = false;
				}				
			}while(hasNext);
			try {
				bufRead.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);				
			}
		}
		return lecturasList;
	}
	
	
}
