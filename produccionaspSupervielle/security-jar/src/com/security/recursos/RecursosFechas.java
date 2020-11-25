package com.security.recursos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 
 * @author Federico Muñoz
 * @Modificado Gabriel Mainero, se pasó a GregorianCalendar y redondeo
 *
 */
public class RecursosFechas
{
	/**
	 * retorna la diferencia de días entre la primer fecha y la segunda, si la segunda fecha es
	 * mayor el numero es positivo
	 * 
	 * @param desde Date
	 * @param hasta Date
	 * @return long
	 */
	public static long diasDiferencia(Date desde, Date hasta)
	{
		GregorianCalendar calendarInicio = new GregorianCalendar();
		calendarInicio.setTime(desde);
		Date fechaInicio = calendarInicio.getTime();
		long lfdesde = fechaInicio.getTime();
		
		GregorianCalendar calendarFin = new GregorianCalendar();
		calendarFin.setTime(hasta);
		Date fechaFin = calendarFin.getTime();
		long lfhasta = fechaFin.getTime();
		long ld = lfhasta - lfdesde;
		// Se calcula la dif y se lo redondea con 0 decimales
		double sD = RecursosNumeros.redondear((double)ld / (1000L * 60 * 60 * 24),0);
		ld= (long) sD;
		return ld; 
	}
	
	/**
	 * Metodo que tranforma una fecha en un String a tipo Date. Recibe por paramentros
	 * una fecha en forma de String y un patron que especifica como esta compuesta la 
	 * cadena. 
	 * @param date
	 * @param pattern
	 * @return Retorna un objeto de tipo Date. 
	 */
	public static Date getDateFormated(String date, String pattern){
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
