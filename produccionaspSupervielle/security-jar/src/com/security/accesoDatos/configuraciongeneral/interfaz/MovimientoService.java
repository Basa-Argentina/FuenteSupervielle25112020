/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 15/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.math.BigDecimal;
import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Movimiento;
import com.security.modelo.configuraciongeneral.Posicion;

/**
 * @author Gonzalo Noriega
 *
 */
public interface MovimientoService extends GeneralServiceInterface<Movimiento>{
	public Boolean guardarMovimiento(Movimiento movimiento);
	public Boolean actualizarMovimiento(Movimiento movimiento);
	public Boolean eliminarMovimiento(Movimiento movimiento);
	public Boolean actualizarMovimientoList(List<Movimiento> listMovimientos);
	public Movimiento verificarExistente(Movimiento movimiento);	
	public Boolean guardarMovimientoList(List<Movimiento> listMovimientos);
	public List<Movimiento> listarMovimientos(Movimiento movimiento, ClienteAsp clienteAsp);
	public Boolean guardarMovimientoListYActualizarPosiciones(
			List<Movimiento> listMovimientos, List<Posicion> listaPosiciones)
			throws RuntimeException;
	public Integer contarMovimientosFiltrados(Movimiento movimiento,
			ClienteAsp clienteAsp);
	public Movimiento traerMovimientoAnterior(Movimiento movimiento, ClienteAsp cliente);
	public Boolean actualizarMovimientoListYActualizarPosiciones(
			List<Movimiento> listMovimientos, List<Posicion> listaPosiciones)
			throws RuntimeException;
	public List<Movimiento> traerMovimientosPorRequerimiento(Movimiento Movimiento,
			ClienteAsp clienteAsp);
	public List verificarMovimientosEnRemito(Movimiento movimiento, ClienteAsp clienteAsp);
}
