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
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.configuraciongeneral.TipoVariacion;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface ListaPreciosService extends GeneralServiceInterface<ListaPrecios>{
	public boolean save(ListaPrecios objeto);
	public boolean update(ListaPrecios objeto);
	public boolean update(ListaPreciosDetalle objeto);
	public boolean delete(ListaPrecios objeto);
	public boolean delete(ListaPreciosDetalle objeto);
	public List<ListaPrecios> listarListasPrecios(String codigo, String descripcion, TipoVariacion tipoVariacion, ClienteAsp clienteAsp);
	public ListaPreciosDetalle obtenerListaPreciosDetallePorId(Long id);
	public List<ListaPreciosDetalle> listarDetallesPorListaPreciosConceptoFacturable(ListaPrecios listaPrecios, ConceptoFacturable conceptoFacturable);
	public List<ListaPrecios> listarListasPreciosPopup(String val, ClienteAsp clienteAsp);
	public ListaPrecios obtenerListaPreciosPorCodigo(String codigo, ClienteAsp clienteAsp);
	public List<ListaPrecios> listarTodosListaFiltrados(ListaPrecios listaPrecios, ClienteAsp cliente);
	public List<ListaPrecios> listarPorId(Long[] ids, ClienteAsp cliente);
	public List<ListaPrecios> listarPorConceptoFacturable(ConceptoFacturable concepto,ClienteEmp clienteEmp, ClienteAsp clienteAsp);
	public List<ListaPrecios> listarListasPreciosByClientePopup(String val, String valCodigo, ClienteAsp clienteAsp);
	public List<ListaPrecios> listarListasPreciosPopup(String val, ClienteAsp clienteAsp, ClienteEmp clienteEmp, Boolean habilitado);
	public ListaPrecios obtenerListaPreciosPorCodigo(String codigo, ClienteAsp clienteAsp, String codigoConceptoFacturable,ClienteEmp clienteEmp, Boolean habilitado);
	public ListaPrecios obtenerListaPreciosPorCodigo(String codigo,
			ClienteAsp clienteAsp, Boolean habilitado);
	public List<ListaPrecios> listarListasPreciosPopup(String val,
			ClienteAsp clienteAsp, Boolean habilitado);
	public ListaPrecios getByID(Long id);
}
