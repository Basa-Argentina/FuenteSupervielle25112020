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
import com.security.modelo.configuraciongeneral.TipoElemento;

/**
 * @author Gonzalo Noriega
 *
 */
public interface TipoElementoService extends GeneralServiceInterface<TipoElemento>{
	public boolean save(TipoElemento variacion);
	public boolean update(TipoElemento elemento);
	public boolean delete(TipoElemento variacion);
	public List<TipoElemento> listarTipoElementoFiltrados(TipoElemento tipoElemento, ClienteAsp clienteAsp);
	public TipoElemento verificarExistente(TipoElemento tipoElemento);
	public List<TipoElemento> listarTipoElementoPopup(String val, ClienteAsp cliente);
	public TipoElemento getByCodigo(String codigo, ClienteAsp cliente);
	public TipoElemento traerUltCodigoTipoElemento(ClienteAsp cliente);
	public TipoElemento verificarExistentePrefijoCodigo(TipoElemento tipoElemento);
	public TipoElemento getByPrefijo(String prefijo, ClienteAsp cliente);
	public TipoElemento getByID(Long id);
}
