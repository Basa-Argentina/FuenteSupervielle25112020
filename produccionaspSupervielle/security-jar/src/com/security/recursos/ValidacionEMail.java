package com.security.recursos;
/**
 * 
 * @author Gabriel Mainero
 *
 */
public class ValidacionEMail {
	private static String regex="^[\\w-Ò—\\-\\_]+(\\.[\\w-Ò—\\-\\_]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$"; 
	public static boolean validar(String eMailValidar){
		return eMailValidar.matches(regex);
	}
}
