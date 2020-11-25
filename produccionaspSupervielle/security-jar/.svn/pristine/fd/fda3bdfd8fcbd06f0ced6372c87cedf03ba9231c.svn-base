/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.BancoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.configuraciongeneral.Banco;

/**
 * @author X
 */
@Component
public class BancoServiceImp extends GestorHibernate<Banco> implements BancoService{
	private static Logger logger=Logger.getLogger(BancoServiceImp.class);
	
	@Autowired
	public BancoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Banco> getClaseModelo() {
		return Banco.class;
	}


}
