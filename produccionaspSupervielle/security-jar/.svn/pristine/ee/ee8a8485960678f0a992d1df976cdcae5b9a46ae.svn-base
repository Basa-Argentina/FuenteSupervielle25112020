/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/07/2011
 */
package com.security.accesoDatos.jerarquias.interfaz;

import java.util.List;
import java.util.Set;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.jerarquias.RequerimientoReferencia;

/**
 * @author Gabriel Mainero
 *
 */
public interface RequerimientoService extends GeneralServiceInterface<Requerimiento>{
	public boolean delete(Requerimiento objeto);
	public List<Requerimiento> listarRequerimientoFiltradas(
			Requerimiento requerimiento, ClienteAsp cliente);
	public boolean update(Requerimiento objeto, Set<RequerimientoReferencia> detalle,
			Set<RequerimientoReferencia> detalleOld);
	public boolean update(Requerimiento objeto);
	public boolean save(Requerimiento objeto, Serie serie,
			Set<RequerimientoReferencia> detalle, List<Operacion> operaciones);
	public boolean update(Requerimiento objeto, List<Operacion> operaciones);
	public boolean delete(Requerimiento objeto, List<Operacion> operaciones);
	public Integer contarRequerimientoFiltradas(Requerimiento requerimiento,
			ClienteAsp cliente);
	public List<Requerimiento> listarRequerimientoPopup(String val,
			ClienteAsp clienteAsp, String codigoCliente, String codigoEmpresa,
			String codigoSucursal);
	public Requerimiento obtenerPorId(Long id, String codigoClienteEmp,
			String codigoEmpresa, String codigoSucursal, ClienteAsp clienteAsp);
	public Requerimiento obtenerPorNumero(Long numero, String codigoClienteEmp,
			String codigoEmpresa, String codigoSucursal, ClienteAsp clienteAsp);
}
