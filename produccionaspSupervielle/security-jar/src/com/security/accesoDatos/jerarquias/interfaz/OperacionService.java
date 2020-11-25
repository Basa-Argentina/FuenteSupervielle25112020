/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/07/2011
 */
package com.security.accesoDatos.jerarquias.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Rearchivo;
import com.security.modelo.configuraciongeneral.Stock;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.Requerimiento;

/**
 * @author Gabriel Mainero
 *
 */
public interface OperacionService extends GeneralServiceInterface<Operacion>{
	public List<Operacion> listarOperacionFiltradas(
			Operacion operacion, ClienteAsp cliente);

	public boolean update(Operacion objeto, List<Operacion> operaciones);

	public Integer contarOperacionesPorRequerimientoYEstado(Requerimiento requerimiento,
			String estado, ClienteAsp cliente);

	public List<Operacion> listarOperacionPorRequerimiento(Requerimiento requerimiento, ClienteAsp cliente);


	public Integer contarOperacionFiltradas(Operacion operacion, ClienteAsp cliente);

	public boolean update(Operacion objeto, List<Operacion> operaciones,
			ConceptoOperacionCliente conceptoOperacionCliente,
			List<ConceptoOperacionCliente> conceptosVentas, List<Stock> stocks,
			List<Rearchivo> listaRearchivosActualizar);
	
	public List<Operacion> listarOperacionEstado(String estado, ClienteAsp cliente);

	Boolean actualizarOperacionList(List<Operacion> listOperaciones) throws RuntimeException;
}
