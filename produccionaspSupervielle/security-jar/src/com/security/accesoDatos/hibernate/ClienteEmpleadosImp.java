/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.security.accesoDatos.hibernate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.ClienteEmpleadosService;
import com.security.modelo.configuraciongeneral.ClienteEmpleados;

/**
 * @author Luis Manzanelli
 *
 */
@Component
public class ClienteEmpleadosImp extends GestorHibernate<ClienteEmpleados> implements ClienteEmpleadosService{
	private static Logger logger=Logger.getLogger(ClienteEmpleadosImp.class);
//	private ParameterService parameterService;
	
	@Autowired
	public ClienteEmpleadosImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}
	
	@Override
	public Class<ClienteEmpleados> getClaseModelo() {
		return ClienteEmpleados.class;
	}

	
}
