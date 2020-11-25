/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 20/05/2011
 */
package com.security.accesoDatos.interfaz;

import com.security.modelo.general.Persona;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface PersonaService extends GeneralServiceInterface<Persona>{
	public Persona obtenerPorMail(String mail);
	public boolean update(Persona persona);
}
