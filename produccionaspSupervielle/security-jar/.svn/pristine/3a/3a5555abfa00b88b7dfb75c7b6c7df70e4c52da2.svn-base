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

import com.security.accesoDatos.configuraciongeneral.interfaz.TipoMedioPagoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.configuraciongeneral.TipoMedioPago;

/**
 * @author X
 */
@Component
public class TipoMedioPagoServiceImp extends GestorHibernate<TipoMedioPago> implements TipoMedioPagoService{
	private static Logger logger=Logger.getLogger(TipoMedioPagoServiceImp.class);
	
	@Autowired
	public TipoMedioPagoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<TipoMedioPago> getClaseModelo() {
		return TipoMedioPago.class;
	}


}
