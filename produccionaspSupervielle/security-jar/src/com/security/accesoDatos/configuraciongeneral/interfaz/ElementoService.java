/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 15/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.interfaz;

import java.util.List;

import com.security.accesoDatos.interfaz.GeneralServiceInterface;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.seguridad.User;

/**
 * @author Gonzalo Noriega
 *
 */
public interface ElementoService extends GeneralServiceInterface<Elemento>{
	public List<Elemento> listarElementoFiltradas(Elemento elemento, ClienteAsp cliente);
	public Boolean guardarElemento(Elemento elemento);
	public Boolean actualizarElemento(Elemento elemento);
	public Boolean eliminarElemento(Elemento elemento);
	public Boolean actualizarElementoList(List<Elemento> listElementos);
	public Elemento verificarExistente(Elemento elemento);
	public Boolean guardarElementoList(List<Elemento> listElementos);
	public List<Elemento> listarElementosRelacionados(TipoElemento tipoElemento);
	
	public Elemento getByCodigo(String codigo, ClienteAsp cliente);
	public List<Elemento> getByCodigos(List<String> codigos, ClienteAsp cliente);
	public Elemento verificarExistenteeEnRango(Elemento elemento,
			String codigoInicial, String codigoFinal);
	
	public List<Elemento> obtenerElementosDePosiciones(List<Posicion> posicionesOrigen);
	/**
	 * Metodo que guarda los cambios relacionados al reposicionamiento:<br>
	 *  - Actualiza los elementos<br>
	 *  - Genera los movimientos<br>
	 *  - Actualiza las posiciones de origen (si existen) y destino (obligatorias)<br>
	 * @param posicionesOrigen contiene las posiciones de origen con los datos a actualizar
	 * @param posicionDestino contiene las destino de origen con los datos a actualizar
	 * @param elementosReposicionados contiene los elementos con los datos a actualizar, deben contener 
	 * su posicion de orige en el atributo posicion y el destino en el atributo posicionFutura
	 * @param elementos posicionados en el modulo destino antes del reposicionamiento (puede que no exista ninguno)
	 *  
	 * @return true si se realiza la transaccion en la base de datos
	 */	
	public Boolean guardarReposicionamiento(List<Posicion> posicionesOrigen,List<Posicion> posicionDestino, 
			List<Elemento> elementosReposicionados, List<Elemento> elementosAnterioresModuloDestino, ClienteAsp clienteAsp, User user, Lectura lectura);
	
	/**
	 * Verifica que todos los modulos adyacentes al modulo destino contengan el mismo tipo de elemento
	 * @param moduloDestino
	 * @return
	 */
	public Boolean verificarTipoElementoValidoParaGrupoDeModulos(TipoElemento tipoElemento, Modulo moduloDestino, ClienteAsp clienteAsp);
	public String traerUltCodigoPorTipoElemento(TipoElemento tipoElemento,
			ClienteAsp cliente);
	/**
	 * Metodo de soporte para el proceso de Reposicionamiento, devuelve una lista con los elemntos ubicados 
	 * en el modulo de destino antes del reposicionamiento. Dichos elementos deben ser actualizados a posicion=null
	 * @param moduloDestino El modulo de destino del reposicionamiento
	 * @param clienteAsp El ClienteAsp del usuario logueado al sistema
	 * @return 
	 */
	public List<Elemento> buscarElementosAnterioresModuloDestino(Modulo moduloDestino, ClienteAsp clienteAsp);
	
	/**
	 * Devuelve la lista de posiciones de origen (si existieran) para los elementos a reposicionar. Se utiliza 
	 * para traer una lista de posiciones actualizada antes de llamar al metodo guardarReposicionamiento 
	 * y asi evitar problemas de concurrencia
	 * @param elementosReposicionados
	 * @param clienteAsp
	 * @return
	 */
	public List<Posicion> obtenerPosicionesAnterioresPorElementos(List<Elemento> elementosReposicionados, ClienteAsp clienteAsp);

	/**
	 * Verifica que en el modulo destino haya elementos del mismo tipo de elemento al ingresado por parametro
	 * @param tipoElemento
	 * @param moduloDestino
	 * @param clienteAsp
	 * @return
	 */
	public Boolean verificarTipoElementoValidoParaMismoModulo(
			TipoElemento tipoElemento, Modulo moduloDestino,
			ClienteAsp clienteAsp);
	public Boolean guardarAsignacionPosicionesLibres(List<Posicion> posicionesOrigen,
			List<Posicion> posicionesDestino,
			List<Elemento> elementosReposicionados, ClienteAsp clienteAsp,
			User user, Lectura lectura);
	
	public Elemento obtenerProximoElementoDisponible(ClienteAsp clienteAsp,String codigoCliente,List<String> codigosElementoUtilizados);
	
	public Integer contarElementoFiltradas(Elemento elemento, ClienteAsp clienteAsp);
	
	//popup y searchAjax
	@Deprecated
	public List<Elemento> listarElementoPopup(String val, String codigoDeposito, String codigoTipoElemento, String codigoClienteEmp, ClienteAsp clienteAsp);
	@Deprecated
	public Elemento busquedaServlet(Elemento elementoBusqueda, ClienteAsp object);
	
	public Elemento getContenedorByCodigo(String codigo, String codigoCliente,Boolean limitarCliente, ClienteAsp clienteAsp, String tipoCodigoElemento);
	public Elemento getElementoByCodigo(String codigo, String codigoCliente, ClienteAsp clienteAsp,Boolean limitarCliente, Long soloLibresOmitirLoteId);
	
	public Long cantidadElementosPorConceptoFacturable(Long idClienteEmp,
			Long idConceptoFacturable, Boolean generaCostoXGuarda,
			ClienteAsp clienteAsp);
	public List<Elemento> listarElementoFiltradas(Elemento elemento,
			ClienteAsp clienteAsp, Boolean impresion);
	
	public Integer contarElementoPopupPaginado(ClienteAsp cliente,
			String descripcionTipoElemento, String codigoTipoElemento, String prefijoCodigoTipoElemento,
			String codigoCliente, Boolean limitarDependencia,
			Long libresODistintoLoteId, 
			Boolean esContenedor);
	public Object listarElementoPopupPaginado(ClienteAsp cliente, Elemento elemento, 
			String descripcionTipoElemento, String codigoTipoElemento, String prefijoCodigoTipoElemento,
			String codigoCliente, Boolean limitarDependencia,
			Long libresODistintoLoteId, 
			Boolean esContenedor);
	public List<Elemento> traerElementosPorSQL(Elemento elemento, ClienteAsp clienteAsp);
	public Elemento getContenedorOElementoByCodigo(String codigo,
			String codigoCliente, ClienteAsp clienteAsp,
			Boolean limitarCliente, Long soloLibresOmitirLoteId);
	public Elemento getContenedorByCodigo(String codigo, String codigoCliente,
			Boolean limitarCliente, ClienteAsp clienteAsp,
			String tipoCodigoElemento, Boolean cerrado);
	public Integer contarElementoPopupPaginado(ClienteAsp cliente,
			String descripcionTipoElemento, String codigoTipoElemento,
			String prefijoCodigoTipoElemento, String codigoCliente,
			Boolean limitarDependencia, Long libresODistintoLoteId,
			Boolean esContenedor, Boolean cerrado);
	public List<Elemento> listarElementoPopupPaginado(ClienteAsp cliente,
			Elemento elemento, String descripcionTipoElemento,
			String codigoTipoElemento, String prefijoCodigoTipoElemento,
			String codigoCliente, Boolean limitarDependencia,
			Long libresODistintoLoteId, Boolean esContenedor, Boolean cerrado);
	public List<Long> traerIdElementosEnLectura(String codigoLectura, ClienteAsp clienteAsp);
	public Elemento getProxContenedorDisponByTipoContenedor(String codigo, String codigoCliente, Boolean limitarCliente,
			ClienteAsp clienteAsp, String tipoCodigoElemento, Boolean cerrado);
	public List<Elemento> traerElementosRetiroPorSQL(Elemento elemento, ClienteAsp clienteAsp, String clasificaciones);
	public Integer contarElementosRetiroPorSQL(Elemento elemento, ClienteAsp clienteAsp, String clasificaciones);
	public Elemento buscarElementosParaRequerimientosPorSQL(String codigoElemento, String codigoClienteEmp,
			String idTipoElemento);
	public  String revisarNumReqSQL(Long id);
	
}
