package com.dividato.configuracionGeneral.i18n;	
	
	
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;

	/**
	 * @author Victor Kenis
	 *
	 */
public class I18nUtil {
	
		public static String GENERAL = "generales";
		
		/**
		 * 
		 * @param key
		 * @param sourceName
		 * @return
		 */
		public static String getText(String key, String sourceName){
			ResourceBundle rb= ResourceBundle.getBundle("//i18n//"+ sourceName,LocaleContextHolder.getLocale());
			String text = rb.getString(key);
			return text;
		}
		
}


