package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.HojaRuta;
import com.security.modelo.jerarquias.Operacion;

/**
 * 
 * @author Luis Manzanelli
 *
 */
public interface HojaRutaService extends GeneralServiceInterface<HojaRuta>{
	public List<HojaRuta> obtenerPor(ClienteAsp obtenerClienteAspUser,
			String codigoEmpresa, String codigoSucursal, String codigoCliente,
			Long codigoDesde, Long codigoHasta, Date fechaDesde, Date fechaHasta);
	public void guardarActualizar(HojaRuta hojaRuta);
	public Integer contarObtenerPor(ClienteAsp cliente, String codigoEmpresa,
			String codigoSucursal, String codigoCliente, Long codigoDesde,
			Long codigoHasta, Date fechaDesde, Date fechaHasta);
	public List<HojaRuta> listarHojaRutaFiltradas(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal,
			Date fechaDesde,
			Date fechaHasta , String codigoSerie, BigInteger serieDesde,
			BigInteger serieHasta, String estado, Integer codigoTransporte);
	public void guardarActualizar(HojaRuta hojaRuta, List<Operacion> operaciones);
}
