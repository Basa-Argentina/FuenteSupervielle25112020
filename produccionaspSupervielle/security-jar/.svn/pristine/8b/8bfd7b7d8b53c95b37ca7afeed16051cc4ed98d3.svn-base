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

import com.security.accesoDatos.interfaz.PersonaJuridicaService;
import com.security.modelo.general.PersonaJuridica;

/**
 * @author Luis Manzanelli
 *
 */
@Component
public class PersonaJuridicaServiceImp extends GestorHibernate<PersonaJuridica> implements PersonaJuridicaService{
	private static Logger logger=Logger.getLogger(PersonaJuridicaServiceImp.class);
	
	@Autowired
	public PersonaJuridicaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<PersonaJuridica> getClaseModelo() {
		return PersonaJuridica.class;
	}

	
}
