package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.Date;
import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.LoteRearchivo;
import com.security.modelo.configuraciongeneral.LoteReferencia;

/**
 * 
 * @author Gabriel Mainero
 *
 */
public interface LoteRearchivoService extends GeneralServiceInterface<LoteRearchivo>{
	
	public void guardarActualizar(LoteRearchivo loteRearchivo, LoteReferencia loteReferencia);
	public Integer contarObtenerPor(ClienteAsp cliente, String codigoEmpresa,
			String codigoSucursal, String codigoCliente, Long codigoDesde,
			Long codigoHasta, Date fechaDesde, Date fechaHasta, String tipo,
			Integer codigoClasificacionDocumental, String codigoContenedor);
	public List<LoteRearchivo> obtenerPor(ClienteAsp cliente, String codigoEmpresa,
			String codigoSucursal, String codigoCliente, Long codigoDesde,
			Long codigoHasta, Date fechaDesde, Date fechaHasta, String tipo,
			Integer codigoClasificacionDocumental, String codigoContenedor,
			String fieldOrder, String sortOrder, Integer numeroPagina,
			Integer tamañoPagina);
	public List<LoteRearchivo> obtenerPor(ClienteAsp clienteAsp,
			String codigoCliente, Integer codigoClasificacionDocumental,
			String descripcionLoteRearchivo, String tipo);
}
