package com.security.utils;
/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 02/02/2011
 * 
 */


/**
 * @author Ezequiel Beccaria
 * @modificado Gonzalo Noriega (21/06/2011)
 */
public class CuitUtils {
	private static int dniStc; 
	private static int xyStc; 
	private static int digitoStc; 
	
	
	/*
	 * Validamos que el valor ingresado se corresponda con el formato
	 * de CUIT o CUIL, si no tiene el formato, retornamos false y termina
	 * la validacion.
	 */
	public static boolean validar(String cuit) { 
		int digitoTmp;
		int comparador = 0;
		int n = cuit.lastIndexOf("-");
		comparador = cuit.length() - 1;
		if(n == -1 || comparador==n ){
			return false;
		}
		String xyStr = cuit.substring(0, 2); 
		String dniStr = cuit.substring(cuit.indexOf("-") + 1, n); 
		String digitoStr = cuit.substring(n + 1, n + 2); 
		if (xyStr.length() != 2 || dniStr.length() > 8 || digitoStr.length() != 1) 
			return false; 
		try { 
			xyStc = Integer.parseInt(xyStr); 
			dniStc = Integer.parseInt(dniStr); 
			digitoTmp = Integer.parseInt(digitoStr); 
		} catch (NumberFormatException e) { 
			return false; 
		} 
		if (xyStc != 20 && xyStc != 23 && xyStc != 24 && xyStc != 27 && xyStc != 30 && xyStc != 33 && xyStc != 34) 
			return false; 
		calcular(); 
		if (digitoStc == digitoTmp && xyStc == Integer.parseInt(xyStr)) 
			return true; 
		return false; 
	} 
	
	public static String generar(int dniInt, char xyChar) { 
		if (xyChar == 'F' || xyChar == 'f') 
			xyStc = 27; 
		else if (xyChar == 'M' || xyChar == 'm') 
			xyStc = 20; 
		else 
			xyStc = 30; 
		dniStc = dniInt; 
		calcular(); 
		return formatear(); 
	} 
	
	/** * Metodo privado q da formato al CUIT como String * */ 
	private static String formatear() { 
		return String.valueOf(xyStc) + "-" + completar(String.valueOf(dniStc)) + "-" + String.valueOf(digitoStc); 
	} 
	
	/** * Metodo privado q completa con ceros el DNI para q quede con 8 digitos * */ 
	private static String completar(String dniStr) { 
		while (dniStr.length() < 8) { 
			dniStr = "0" + dniStr; 
		} 
		return dniStr; 
	} 
	
	/** * Metodo privado q calcula el CUIT * */ 
	private static void calcular() { 
		long tmp1, tmp2; 
		long acum = 0; 
		int n = 2; 
		tmp1 = xyStc * 100000000L + dniStc; 
		for (int i = 0; i < 10; i++) { 
			tmp2 = tmp1 / 10; 
			acum += (tmp1 - tmp2 * 10L) * n; 
			tmp1 = tmp2; 
			if (n < 7) 
				n++; 
			else 
				n = 2; 
		} 
		n = (int) (11 - acum % 11); 
		if (n == 10) { 
			if (xyStc == 20 || xyStc == 27 || xyStc == 24) 
				xyStc = 23; 
			else 
				xyStc = 33; 
			/* 
			* No es necesario hacer la llamada recursiva a calcular(), 
			* se puede poner el digito en 9 si el prefijo original 
			* era 23 o 33 o poner el dijito en 4 si el prefijo era 27 
			*/ 
			calcular(); 
		} else { 
			if (n == 11) 
				digitoStc = 0; 
			else 
				digitoStc = n; 
		} 
	} 
	
	/** 
	* * Metodo estatico que retorna el digito verificador de un CUIT/CUIL. * * 
	* 
	* @param xyInt 
	* El prefijo como int * 
	* @param dniInt 
	* El DNI como int * * 
	* @return El digito como int. Si se modifico el prefijo (por 23 o 33) * 
	* retorna 23x o 33x donde x es el digito * 
	*/ 
	public static int digito(int xyInt, int dniInt) { 
		xyStc = xyInt; 
		dniStc = dniInt; 
		calcular(); 
		if (xyInt == xyStc) 
			return digitoStc; 
		else 
			return (xyStc * 10 + digitoStc); 
	} 
}