/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 17/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;
import java.util.Set;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClasificacionDocumental;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.seguridad.User;

/**
 * @author Gonzalo Noriega
 *
 */
public interface EmpleadoService extends GeneralServiceInterface<Empleado>{
	public List<Empleado> listarEmpleadoFiltradas(Empleado empleado, ClienteAsp cliente);
	public Boolean guardarEmpleado(Empleado empleado);
	public Boolean actualizarEmpleado(Empleado empleado);
	public Boolean eliminarEmpleado(Empleado empleado);
	public boolean delete(Long id);
	public Empleado verificarExistente(Empleado empleado, ClienteAsp cliente);
	public List<Empleado> listarTodosEmpleadoFiltradosByCliente(User empleado, ClienteAsp cliente);
	public Empleado obtenerPorUsername(String username);
	public Empleado obtenerPorUsernameRoles(String username, String roles);
	public Empleado obtenerPorEMail(String eMail);
	public List<Empleado> listarPorId(Long[] ids, ClienteAsp cliente);
	public Empleado obtenerPorCodigo(String codigo, ClienteAsp clienteAsp);
	public List<Empleado> listarEmpleadoPopup(String val, ClienteAsp clienteAsp,
			String codigoCliente);
	public List<Empleado> listarEmpleadosDisponiblesParaCarpeta(Set<Empleado> empleadosNoDisponibles, ClienteEmp clienteEmp, ClienteAsp clienteAsp);
	public List<Empleado> listarEmpleadosAsignadosCarpeta(ClasificacionDocumental carpetaSeleccionada, ClienteEmp clienteEmp, ClienteAsp clienteAsp);
	public List<Empleado> listarEmpleadosConCarpetasAsignadas(Long[] ids, ClienteAsp cliente);
	public Empleado getByCodigo(String codigoPersonal, String codigoCliente,
			ClienteAsp clienteAsp);
	public Empleado getByCodigo(String codigoPersonal, String codigoCliente,
			ClienteAsp clienteAsp, Boolean habilitado);
	public List<Empleado> listarEmpleadoPopup(String val, ClienteAsp clienteAsp,
			String codigoCliente, Boolean habilitado);
	public Empleado getByID(Long id);

}
