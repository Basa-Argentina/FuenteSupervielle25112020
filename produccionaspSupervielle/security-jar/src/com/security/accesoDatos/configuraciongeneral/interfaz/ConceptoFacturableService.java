/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.TipoConceptoFacturable;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;

/**
 * @author Ezequiel Beccaria
 *
 */
public interface ConceptoFacturableService extends GeneralServiceInterface<ConceptoFacturable>{
	public boolean save(ConceptoFacturable o);
	public boolean update(ConceptoFacturable o);
	public boolean delete(ConceptoFacturable o);
	public List<ConceptoFacturable> listarPorFiltro(
			Boolean habilitado, String codigo, 
			String descrip, Boolean generaStock, 
			String tipoCalculo, TipoConceptoFacturable tipo,
			ClienteAsp cliente);
	public List<TipoConceptoFacturable> listarTiposConceptosFacturables();
	public TipoConceptoFacturable obtenerTipoPorId(Long id);
	public List<ConceptoFacturable> listarConceptosFacturablesPopup(String val, ClienteAsp clienteAsp);
	public List<ConceptoFacturable> listarConceptosFacturablesPopup(String val, ClienteAsp clienteAsp,int mode, int tipo);
	public ConceptoFacturable obtenerConceptoFacturablePorCodigo(String codigo, ClienteAsp clienteAsp);
	public List<ConceptoFacturable> listarConceptosFacturablesByListaPrecioPopup(
			String val, String valCodigo,ClienteAsp clienteAsp) ;
	public List<ConceptoFacturable> listarConceptosFacturablesPopup(String val, ClienteAsp clienteAsp, int mode, int tipo, Boolean habilitado);
	public List<ConceptoFacturable> listarConceptosFacturablesPopup(String val, ClienteAsp clienteAsp,Boolean habilitado);
}
