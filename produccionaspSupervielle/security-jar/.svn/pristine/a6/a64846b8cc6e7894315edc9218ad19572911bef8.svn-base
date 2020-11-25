/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 26/05/2011
 */
package com.security.accesoDatos.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.EstadoLicenciaService;
import com.security.modelo.administracion.EstadoLicencia;


/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class EstadoLicenciaImp extends GestorHibernate<EstadoLicencia> implements EstadoLicenciaService{

	@Autowired
	public EstadoLicenciaImp(HibernateControl hibernateControl) {
		super(hibernateControl);		
	}

	@Override
	public Class<EstadoLicencia> getClaseModelo() {
		return EstadoLicencia.class;
	}

}
