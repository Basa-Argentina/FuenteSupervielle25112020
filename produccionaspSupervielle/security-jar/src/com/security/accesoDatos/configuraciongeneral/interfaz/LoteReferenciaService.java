package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.Date;
import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.LoteReferencia;
import com.security.modelo.configuraciongeneral.Referencia;

/**
 * 
 * @author FedeMz
 *
 */
public interface LoteReferenciaService extends GeneralServiceInterface<LoteReferencia>{
	
	public List<LoteReferencia> obtenerPor(ClienteAsp obtenerClienteAspUser,
			String codigoEmpresa, String codigoSucursal, String codigoCliente,
			Long codigoDesde, Long codigoHasta, Date fechaDesde, Date fechaHasta);
	
	public void guardarActualizar(LoteReferencia loteReferencia);
	
	public Integer contarObtenerPor(ClienteAsp cliente, String codigoEmpresa,
			String codigoSucursal, String codigoCliente, Empleado personal,Long codigoDesde,
			Long codigoHasta, Date fechaDesde, Date fechaHasta);
	
	public List<LoteReferencia> listarLoteReferenciaFiltradas(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal, String codigoCliente,Empleado personal,
			Long codigoDesde, Long codigoHasta, Date fechaDesde,
			Date fechaHasta, String fieldOrder, String sortOrder,
			Integer numeroPagina, Integer tamañoPagina);

	public int eliminarLoteReferencia(long idLoteReferencia);

	//Metodo agregado para el codigo
	public Long traerUltCodigoPorClienteAsp(ClienteAsp cliente);

	public boolean guardarActualizarLoteYModificadas(LoteReferencia loteReferencia,
			List<Referencia> modificadas, List<Referencia> eliminadas);

	public boolean guardarLoteYActualizarReferencias(LoteReferencia loteReferencia);

	public LoteReferencia getByID(Long id);

	public Integer contarLotesSql(ClienteAsp cliente, String codigoEmpresa, String codigoSucursal, String codigoCliente,
			Empleado personal, Long codigoDesde, Long codigoHasta, Date fechaDesde, Date fechaHasta);

	public List<LoteReferencia> obtenerLoteReferenciaSQL(ClienteAsp cliente, String codigoEmpresa, String codigoSucursal,
			String codigoCliente, Empleado personal, Long codigoDesde, Long codigoHasta, Date fechaDesde,
			Date fechaHasta, String fieldOrder, String sortOrder, Integer numeroPagina, Integer tamañoPagina);
	
}
