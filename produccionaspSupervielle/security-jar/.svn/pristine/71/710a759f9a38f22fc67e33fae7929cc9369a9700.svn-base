/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 15/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.LoteFacturacion;
import com.security.modelo.configuraciongeneral.LoteFacturacionDetalle;
import com.security.modelo.configuraciongeneral.PreFactura;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;

/**
 * @author Victor Kenis
 *
 */
public interface LoteFacturacionService extends GeneralServiceInterface<LoteFacturacion>{
	
	public Boolean guardarLoteFacturacion(LoteFacturacion loteFacturacion);
	public Boolean actualizarLoteFacturacion(LoteFacturacion loteFacturacion);
	public Boolean eliminarLoteFacturacion(LoteFacturacion loteFacturacion);
	public Boolean actualizarLoteFacturacionList(List<LoteFacturacion> listLoteFacturacions);
	public LoteFacturacion verificarExistente(LoteFacturacion loteFacturacion);
	public Boolean guardarLoteFacturacionList(List<LoteFacturacion> listLoteFacturacions);
	public LoteFacturacion getByNumero(Long numero, ClienteAsp cliente);

	public List<LoteFacturacion> getByNumeros(List<Long> numeros, ClienteAsp cliente);
	//public Boolean guardarLoteFacturacionYDetalles(Set<LoteFacturacionDetalle> loteFacturacionDetalles,
	//		LoteFacturacion loteFacturacion);
	//public List<LoteFacturacion> listarLoteFacturacionFiltradas(LoteFacturacion loteFacturacion, ClienteAsp clienteAsp);
	public List<LoteFacturacion> listarLoteFacturacionsPorId(List<LoteFacturacion> loteFacturacions, ClienteAsp clienteAsp);
	public List<LoteFacturacion> listarLoteFacturacionPopup(String val, String clienteCodigo,
			ClienteAsp clienteAsp);

	public LoteFacturacion getByCodigo(Long codigo, ClienteEmp clienteEmp, ClienteAsp clienteAsp);
	public Long verificarExistentePeriodoPosterior(LoteFacturacion loteFacturacion,
			ClienteAsp clienteAsp);
	public Long traerUltNumero(LoteFacturacion lote, ClienteAsp cliente);
	//public Boolean actualizarLoteFacturacionYDetalles(Set<LoteFacturacionDetalle> loteFacturacionDetallesViejos,
	//		LoteFacturacion loteFacturacion);
	public Long contarObtenerPor(ClienteAsp cliente, String codigoEmpresa,
			String codigoSucursal, Date fechaDesde, Date fechaHasta,
			String estado);
	public List<LoteFacturacion> obtenerPor(ClienteAsp cliente, String codigoEmpresa,
			String codigoSucursal, Date fechaDesde, Date fechaHasta,
			String estado, String fieldOrder, String sortOrder,
			Integer numeroPagina, Integer tamañoPagina);
	public Boolean guardarActualizarLoteFacturacionYDetalles(
			Set<PreFactura> loteFacturacionDetallesViejos,
			LoteFacturacion loteFacturacion,List<ConceptoOperacionCliente> listaConceptos);
	public Boolean facturarLoteFacturacion(LoteFacturacion loteFacturacion,
			List<Factura> listaFacturas, Serie serieA, Serie serieB);
	public LoteFacturacion verificarExistenteMismoPeriodo(LoteFacturacion loteFacturacion,
			ClienteAsp clienteAsp);
	
}
