package com.security.constants;

import java.util.Locale;
import java.util.ResourceBundle;

public class Constants {
	public static final String SEPARADOR_CLIENTE_USUARIO;
	public static final String NOMBRE_GROUP_ADMIN;
	public static final String NOMBRE_GROUP_EMPLEADOS;
	public static final String PAIS_DEFECTO;
	public static final String TIPO_DOC_DNI;
	public static final String TIPO_DOC_CUIT;
	public static final String URL_ARCHIVOS_DIGITALES;
	public static final String PATH_JASPER;
	public static final String CODIGO_TIPO_ELEMENTO_ELECTRONICO;

	static{
		SEPARADOR_CLIENTE_USUARIO = displayValue("SEPARADOR_CLIENTE_USUARIO");	
		NOMBRE_GROUP_ADMIN = displayValue("NOMBRE_GROUP_ADMIN");	
		NOMBRE_GROUP_EMPLEADOS = displayValue("NOMBRE_GROUP_EMPLEADOS");	
		PAIS_DEFECTO = displayValue("PAIS_DEFECTO");
		TIPO_DOC_DNI = displayValue("TIPO_DOC_DNI");
		TIPO_DOC_CUIT = displayValue("TIPO_DOC_CUIT");
		URL_ARCHIVOS_DIGITALES = displayValue("URL_ARCHIVOS_DIGITALES");
		PATH_JASPER = displayValue("PATH_JASPER");
		CODIGO_TIPO_ELEMENTO_ELECTRONICO = displayValue("CODIGO_TIPO_ELEMENTO_ELECTRONICO");
	}

	static String displayValue(String key) {	
	    ResourceBundle labels = 
	       ResourceBundle.getBundle("com.security.constants.Constants",Locale.FRENCH);
	    String value  = labels.getString(key);
	    return value;	
	}

}

