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
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.jerarquias.RequerimientoReferencia;

/**
 * @author Gabriel Mainero
 *
 */
public interface RequerimientoReferenciaService extends GeneralServiceInterface<RequerimientoReferencia>{

	public RequerimientoReferencia obtenerPendienteOEnProceso(Long referencia,
			Long requerimiento);

	public List<RequerimientoReferencia> listarRequerimientoReferenciaPorRequerimiento(
			Requerimiento requerimiento, ClienteAsp cliente);
	
}
