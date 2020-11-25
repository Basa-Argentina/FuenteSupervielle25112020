/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
 */
package com.security.accesoDatos.jerarquias.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;

/**
 * @author Gabriel Mainero
 *
 */
public interface OperacionElementoService extends GeneralServiceInterface<OperacionElemento>{

	public List<OperacionElemento> listarOperacionElementoPorOperacion(Operacion operacion,	ClienteAsp cliente);

	public List<OperacionElemento> listarOperacionElementoPorElementoYEstado(
			Elemento elemento, String estado, ClienteAsp cliente);

	public Integer cantidadOperacionElementoPorOperacion(Operacion operacion,ClienteAsp cliente);
	
}
