package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Transporte;



public interface TransporteService extends GeneralServiceInterface<Transporte>{

	public List<Transporte> listarTransporteFiltradas(Transporte transporte,
			ClienteAsp cliente);

	public Boolean eliminarTransporte(Transporte transporte);

	public Boolean guardarTransporte(Transporte transporte);

	public Boolean actualizarTransporte(Transporte transporte);

	public Transporte getByCodigo(Integer codigo, Empresa empresa);

	public Transporte verificarTransporte(Transporte transporte);

	public List<Transporte> listarTransportePopup(String val, String codigoEmpresa,
			ClienteAsp cliente);

	public Transporte obtenerPorCodigo(Integer codigo, ClienteAsp clienteAsp);

	public Transporte obtenerPorCodigo(Integer codigo, String codigoEmpresa,
			ClienteAsp clienteAsp);

	public Transporte obtenerPorCodigo(Integer codigo, String codigoEmpresa,
			Boolean habilitado, ClienteAsp clienteAsp);

	public List<Transporte> listarTransportePopup(String val, String codigoEmpresa,
			ClienteAsp cliente, Boolean habilitado);

}
