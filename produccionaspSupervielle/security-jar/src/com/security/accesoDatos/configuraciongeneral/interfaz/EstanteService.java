/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Estante;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.Posicion;

/**
 * @author Gonzalo Noriega
 *
 */
public interface EstanteService extends GeneralServiceInterface<Estante>{
	public Estante verificarEstante(Estante estante, ClienteAsp cliente);
	public List<Estante> listarEstanteFiltradas(Estante estante, ClienteAsp cliente);
	public Estante getByCodigo(String codigo);
	public Boolean guardarEstante(Estante seccion);
	public Boolean actualizarEstante(Estante seccion);
	public Boolean eliminarEstante(Estante seccion);
	public String traerUltCodigoPorDeposito(Long idDeposito);
	public Boolean estaVacio(Estante estante);
	public Estante getById(Long id);
	public Boolean eliminarEstanteModulosPosiciones(Estante estante);
	public Boolean NoHayElementos(Long estanteId);
}
