package com.security.servicios;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;

import com.security.accesoDatos.interfaz.AppLogService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.AppLog;
import com.security.modelo.seguridad.User;


/**
 * Logger que permite enviar el log a base de datos.
 * 
 * @author Federico Muñoz
 * 
 */
public abstract class LogListener extends AppenderSkeleton 
{
	protected static AppLogService service;
	@Autowired
	public void setService(AppLogService service){
		LogListener.service=service;
	}
	
	/**
	 * @param loggingEvent
	 */
	@Override
	protected void append(final LoggingEvent loggingEvent) 
	{
		if(LogListener.service==null)
			return;
		final Date fechaHora=new Date();
		new Thread(){
			public void run(){
				AppLog logMessage=new AppLog();
				logMessage.setApp(getApp());
				logMessage.setClase(loggingEvent.getLoggerName());
				logMessage.setLineaReferencia(loggingEvent.getLocationInformation().fullInfo);
				logMessage.setNivel(loggingEvent.getLevel().toString());
				logMessage.setMensaje(loggingEvent.getRenderedMessage());
				logMessage.setFechaHora(fechaHora);
				logMessage.setCliente(obtenerClienteAspUser());
				if(loggingEvent.getMessage() instanceof Throwable){
					try {
					    StringWriter sw = new StringWriter();
					    PrintWriter pw = new PrintWriter(sw);
					    ((Throwable)loggingEvent.getMessage()).printStackTrace(pw);
					    pw.flush();
					    logMessage.setExcepcion(sw.toString());
					    pw.close();
					  }catch(Exception e) {
						  //e.printStackTrace();
					  }
				}
				if (loggingEvent.getThrowableInformation()!=null && loggingEvent.getThrowableInformation().getThrowable()!=null){
					try {
					    StringWriter sw = new StringWriter();
					    PrintWriter pw = new PrintWriter(sw);
					    loggingEvent.getThrowableInformation().getThrowable().printStackTrace(pw);
					    pw.flush();
					    logMessage.setExcepcion(sw.toString());
					    pw.close();
					  }catch(Exception e) {
						  //e.printStackTrace();
					  }
				}
				//guardamos el log 
				if(!logMessage.isLogListenerException())
					LogListener.service.guardar(logMessage);
			}
		}.start();
	}
	
	// *************************************************************************************************
	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}
	protected abstract String getApp();
	
	private ClienteAsp obtenerClienteAspUser(){
		if(SecurityContextHolder.getContext().getAuthentication() == null)
			return null;
		else
			return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
}
