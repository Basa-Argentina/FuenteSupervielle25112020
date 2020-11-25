/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/04/2011
 */
package com.security.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Ezequiel Beccaria
 *
 */
public class DateUtil {
	
	/**
	 * Metodo utilizado para obtener la fecha inicial de un rango
	 * de fechas. Setea a 0hs 00min 00seg 000mil la hora de un 
	 * objeto Date pasado por parametro.
	 * @param from
	 * @return
	 */
	public static Date getDateFrom(Date from) {
		Calendar calendarInicio = GregorianCalendar.getInstance();
		calendarInicio.setTime(from);
		calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
		calendarInicio.set(Calendar.MINUTE, 0);
		calendarInicio.set(Calendar.SECOND, 0);
		calendarInicio.set(Calendar.MILLISECOND, 0);
		return calendarInicio.getTime();
	}

	/**
	 * Metodo utilizado para obtener la fecha final de un rango
	 * de fechas. Setea a 23hs 59min 59seg 999mil la hora de un
	 * objeto Date pasado por parametro.
	 * @param to
	 * @return
	 */
	public static Date getDateTo(Date to) {
		Calendar calendarFin = GregorianCalendar.getInstance();
		calendarFin.setTime(to);
		calendarFin.set(Calendar.HOUR_OF_DAY, 23);
		calendarFin.set(Calendar.MINUTE, 59);
		calendarFin.set(Calendar.SECOND, 59);
		calendarFin.set(Calendar.MILLISECOND, 999);
		return calendarFin.getTime();
	}
	
	/**
	 * Metodo que devuelve el un objeto de tipo Date 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date getDateWithDaysDiference(Date date, Integer days){
		Long time = date.getTime();
		Long diff = (24L*60L*60L*1000L)*days;		
		return new Date(time-diff);
	}
	
	/**
	 * devuelve true si la fecha ingresada como parametro es mayor que la fecha igual al dia actual hora 23:59:59
	 * @param fecha
	 * @return
	 */
	public static Boolean verificarFechaMayorActual(Date fecha){
		Boolean result = Boolean.TRUE;
		SimpleDateFormat sdfFechaHora = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		SimpleDateFormat sdfFecha = new SimpleDateFormat("dd-MM-yyyy");
		Date fechaActual = new Date();
		
		try {
			String fechaActualStr = sdfFecha.format(fechaActual);
			fechaActual = sdfFechaHora.parse(fechaActualStr+" 23:59:59");
			if(fechaActual.getTime()>=fecha.getTime()){
				return Boolean.FALSE;
			}
		} catch (ParseException e) {
			result = Boolean.TRUE;
		}
		return result;
	}
}
