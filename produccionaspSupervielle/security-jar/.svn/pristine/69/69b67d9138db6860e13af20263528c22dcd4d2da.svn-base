/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.LoteRearchivo;
import com.security.modelo.configuraciongeneral.Rearchivo;
import com.security.modelo.configuraciongeneral.Referencia;

/**
 * @author Gabriel Mainero
 *
 */
public interface RearchivoService extends GeneralServiceInterface<Rearchivo>{

	public List<Rearchivo> obtenerParaReferencia(Referencia referencia);

	public List<Rearchivo> listarRearchivoPorLote(LoteRearchivo loteRearchivo,
			ClienteAsp cliente);

	public Rearchivo obtenerRearchivoPorElemento(Elemento elemento);

	public String obtenerPorReferencia(Long idReferencia);

	public Rearchivo obtenerRearchivoPorReferencia(Long idReferencia);

	
}
