/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.Date;
import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.ReferenciaHistorico;
import com.security.modelo.seguridad.User;

/**
 * @author Victor Kenis
 *
 */
public interface ReferenciaHistoricoService extends GeneralServiceInterface<ReferenciaHistorico>{
	
	public Integer contarReferenciaHistoricoFiltrados(ReferenciaHistorico referenciaHistorico, ClienteAsp clienteAsp);

	public List<ReferenciaHistorico> listarReferenciaHistorico(
			ReferenciaHistorico referenciaHistorico, ClienteAsp clienteAsp);

	ReferenciaHistorico obtenerReferenciaHistorico(
			Referencia referencia);

	public List<Long> obtenerCodigosReferenciaHistoricoPorUsuario(User usuario,
			java.util.Date fechaDesde, java.util.Date fechaHasta);

	public List<ReferenciaHistorico> obtenerReferenciaHistoricoPorUsuario(
			User usuario, java.util.Date fechaDesde, java.util.Date fechaHasta);

	public List<Long> obtenerIdsReferenciasPorUsuario(User usuario, Date fechaDesde,
			Date fechaHasta);

	public List<ReferenciaHistorico> traerReferenciasHistoricasPorSQL(User usuario,
			String fechaDesde, String fechaHasta);

	public List<Object> traerSumasReferenciasPorSQL(User usuario, String fechaDesde,
			String fechaHasta, Long clienteAspId);

	public List<Long> obtenerIdsReferenciasPorUsuarioEnDia(User usuario, Date fechaHora);

	public List<Object> traerUsuariosCargaReferenciasPorSQL(String fechaDesde,
			String fechaHasta, Long clienteAspId);
	
}
