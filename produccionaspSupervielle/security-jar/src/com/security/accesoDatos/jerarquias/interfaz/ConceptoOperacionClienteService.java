/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 15/06/2011
 */
package com.security.accesoDatos.jerarquias.interfaz;

import java.util.List;
import java.util.Set;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;

/**
 * @author Victor Kenis
 *
 */
public interface ConceptoOperacionClienteService extends GeneralServiceInterface<ConceptoOperacionCliente>{

	public Boolean guardarConceptoOperacionCliente(ConceptoOperacionCliente conceptoOperacionCliente);
	public Boolean actualizarConceptoOperacionCliente(ConceptoOperacionCliente conceptoOperacionCliente);
	public Boolean eliminarConceptoOperacionCliente(ConceptoOperacionCliente conceptoOperacionCliente);
	public Boolean actualizarConceptoOperacionClienteList(List<ConceptoOperacionCliente> listConceptoOperacionCliente);
	public Boolean guardarConceptoOperacionClienteList(List<ConceptoOperacionCliente> listConceptosOperacionCliente);
	public List<ConceptoOperacionCliente> conceptoOperacionClienteFiltradas(
			ConceptoOperacionCliente conceptoOperacionCliente,
			ClienteAsp cliente);
	
	public List<ConceptoOperacionCliente> getByNumeros(List<Long> numeros, ClienteAsp cliente);
	
	public List<ConceptoOperacionCliente> conceptoOperacionClienteFiltradas(
			List<Long> idsAsociados,String periodo, ConceptoOperacionCliente conceptoOperacionCliente,
			ClienteAsp cliente);
	
	public List obtenerConceptosClientesEnPeriodo(List<Long> idsAsociados,
			String periodo, String fechaPeriodo, ClienteAsp cliente);
	
	public List<ConceptoOperacionCliente> guardarYTraerConceptoOperacionClienteList(
			List<ConceptoOperacionCliente> listConceptosOperacionCliente)
			throws RuntimeException;
	public List<ConceptoOperacionCliente> listarConceptosPorPreFacturaDetalle(
			Long idPreFacturaDetalle, ClienteAsp cliente);
	
}
