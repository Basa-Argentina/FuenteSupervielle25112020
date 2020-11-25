package com.dividato.configuracionGeneral.servicios;

import com.security.servicios.LogListener;

/**
 * Logger que permite enviar el log a base de datos.
 * 
 * @author Gonzalo Noriega
 * 
 */
public class ConfiguracionGeneralLogListener extends LogListener
{
	public String getApp(){
		return "CFGMOD";
	}
}
