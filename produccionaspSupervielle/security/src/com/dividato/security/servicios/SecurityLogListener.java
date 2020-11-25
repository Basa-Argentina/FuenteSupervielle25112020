package com.dividato.security.servicios;

import com.security.servicios.LogListener;

/**
 * Logger que permite enviar el log a base de datos.
 * 
 * @author Federico Muñoz
 * 
 */
public class SecurityLogListener extends LogListener
{
	public String getApp(){
		return "SECMOD";
	}
}
