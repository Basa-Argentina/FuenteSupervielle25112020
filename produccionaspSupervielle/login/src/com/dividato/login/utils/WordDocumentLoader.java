/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 22/03/2011
 */
package com.dividato.login.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * @author Ezequiel Beccaria
 *
 */
public class WordDocumentLoader {
	private Range range;
	private String filepath;
	
	public WordDocumentLoader(String inputFilepath) {
		this.filepath = inputFilepath;
	}
	
	public List<String> getDocumentText() throws FileNotFoundException, IOException{
		//objetos utilizados para el documento word
		org.apache.poi.hwpf.usermodel.Paragraph docParagraph;
		org.apache.poi.hwpf.usermodel.CharacterRun docCharrun;
		List<String> texto = new ArrayList<String>();
		
		readDoc(filepath);
		for(int x=0; x<range.numParagraphs(); x++) {
			docParagraph = range.getParagraph(x);
			String cadenaPrev = "";
			for(int y=0; y<docParagraph.numCharacterRuns(); y++) {
				docCharrun = docParagraph.getCharacterRun(y);									
				if(!"".equals(cadenaPrev) && docCharrun.getFontSize() == docParagraph.getCharacterRun(y-1).getFontSize()){
					cadenaPrev = cadenaPrev + docCharrun.text();
				}else{
					cadenaPrev = docCharrun.text();
				}										
			}		
			texto.add(cadenaPrev);
		}
		return texto;
	}
	
	private void readDoc(String fileName) throws FileNotFoundException, IOException{
        POIFSFileSystem fs = null;

		fs = new POIFSFileSystem(new FileInputStream(fileName));
        HWPFDocument doc = new HWPFDocument(fs);
 
        /** Read the content **/
        range = doc.getRange();
    } 
	
}
