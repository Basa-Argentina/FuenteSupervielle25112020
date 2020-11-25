/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 20/05/2011
 */
package com.security.accesoDatos.hibernate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.PersonaFisicaService;
import com.security.modelo.general.PersonaFisica;

/**
 * @author Luis Manzanelli
 *
 */
@Component
public class PersonaFisicaServiceImp extends GestorHibernate<PersonaFisica> implements PersonaFisicaService{
	private static Logger logger=Logger.getLogger(PersonaFisicaServiceImp.class);
	
	@Autowired
	public PersonaFisicaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<PersonaFisica> getClaseModelo() {
		return PersonaFisica.class;
	}

	
}
