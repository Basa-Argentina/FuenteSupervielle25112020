/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 23/05/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empleado;

/**
 * @author Luis Manzanelli
 *
 */
public interface ClasificacionDocumentalService extends GeneralServiceInterface<ClasificacionDocumental>{
	public List<ClasificacionDocumental> getNodosRaizPorCliente(ClienteEmp cliente);
	public List<ClasificacionDocumental> getByClienteCodigo(ClienteEmp clienteEmp, Integer codigo);
	public ClasificacionDocumental getClasificacionByCodigo(Integer codigo,String codigoClienteEmp, ClienteAsp clienteAsp,String nodo);
	public ClasificacionDocumental getClasificacionByCodigoCargarHijos(Integer codigo,String codigoClienteEmp, ClienteAsp clienteAsp,String nodo);
	public List<ClasificacionDocumental> listarClasificacionPopup(String val, String clienteCodigo,ClienteAsp obtenerClienteAspUser);
	public Integer getProximoCodigoByClienteEmp(ClienteEmp clienteEmp, ClienteAsp clienteAsp);
	public Set<Empleado> getPersonalAsignadoPorNodos(Set<ClasificacionDocumental> carpetas, ClienteEmp clienteEmp, ClienteAsp clienteAsp);
	public boolean guardarPersonarClasificacionDocumental(List<Empleado> empleadosAsignados, ClasificacionDocumental carpetaSeleccionada, 
			ClienteEmp clienteEmp, ClienteAsp clienteAsp);
	public ClasificacionDocumental traerCarpetaConEmpleadosAsignados(ClasificacionDocumental carpeta, ClienteEmp clienteEmp, ClienteAsp clienteAsp);
	public Boolean eliminarNodoYNodosHijos(ClasificacionDocumental nodo, ClienteAsp clienteAsp);
	public ClasificacionDocumental getByCodigo(Integer codigo,String codigoClienteEmp, ClienteAsp clienteAsp);
	public List<ClasificacionDocumental> listarClasificacionPopupEntera(String val,
			String clienteCodigo, ClienteAsp clienteAsp);
	public Collection<ClasificacionDocumental> getByPersonalAsignado(Empleado empleado);
}
