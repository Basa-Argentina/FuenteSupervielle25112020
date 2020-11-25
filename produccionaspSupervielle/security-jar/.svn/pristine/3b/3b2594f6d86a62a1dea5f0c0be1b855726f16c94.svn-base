package com.security.recursos;

import java.text.NumberFormat;
import java.util.Locale;


/**
 * 
 * @author Gabriel Mainero
 *
 */
public class RecursosNumeros
{
	/**
	 * 
	 * @param numero
	 * @param decimales
	 * @return retorna el numero redondeado en base a la cantidad de decimales
	 */
	public static double redondear( double numero, int decimales ) {
        return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
	}
	public static String formatoMoneda(Double numero){
		if(numero==null)
			return "";
		NumberFormat numberFormatter;
		String amountOut;
		numberFormatter = NumberFormat.getNumberInstance(new Locale("es"));
		numberFormatter.setMinimumFractionDigits(2);
		numberFormatter.setMaximumFractionDigits(2);
		numberFormatter.setGroupingUsed(true);
		amountOut = numberFormatter.format(numero);
		return amountOut;
	}
	/**
	 * 
	 * @param numero
	 * @param decimales
	 * @return retorna el numero truncado en base a la cantidad de decimales
	 */
	public static double truncar( double numero, int decimales ) {
		if(numero < 0)
			return (-1)*truncar((-1)*numero,decimales);
		
		double result = numero * Math.pow(10,decimales);
		result += (1 / Math.pow(10,decimales+2)); //evitar esto: 150.48 * 100 = 15047.99999998
		result = Math.floor(result);
		result /= Math.pow(10,decimales);
		return result;
	}
}
