/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/08/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.FacturaDetalle;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;

/**
 * @author Victor Kenis
 *
 */
public interface FacturaService extends GeneralServiceInterface<Factura>{
	public List<Factura> listarFacturasFiltradas(Factura factura, ClienteAsp cliente);
	public Boolean guardarFactura (Factura factura, List<FacturaDetalle> detalles);
	public Boolean eliminarFactura(Factura Factura);
	public Factura obtenerFacturaPorNumeroComprobante(ClienteAsp clienteAsp,
			Empresa empresa, Sucursal sucursal, ClienteEmp clienteEmp, Serie serie, Long numeroComprobante);
	public Boolean verificarExistenciaFactura(Serie serie, Long numeroComprobante);
	public Boolean actualizarFacturaYDetalles(Factura factura);
	public Boolean actualizarFactura(List<Factura> facturas) throws RuntimeException;
	public List<Factura> getByIds(List<Long> ids, ClienteAsp cliente);
	public List<Factura> getByIdsConDetalles(List<Long> ids, ClienteAsp cliente);
	List<Factura> listarFacturasFiltradasPorAfipTipoComprobante(
			String tipoFacturas [], String codigoCliente, ClienteAsp cliente);
}
