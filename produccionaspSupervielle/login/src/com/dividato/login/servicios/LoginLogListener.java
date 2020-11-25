package com.dividato.login.servicios;

import com.security.servicios.LogListener;

/**
 * Logger que permite enviar el log a base de datos.
 * 
 * @author Federico Muñoz
 * 
 */
public class LoginLogListener extends LogListener
{
	public String getApp(){
		return "LOGIN";
	}
}
