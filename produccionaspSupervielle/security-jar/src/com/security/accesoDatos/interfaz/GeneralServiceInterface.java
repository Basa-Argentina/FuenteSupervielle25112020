package com.security.accesoDatos.interfaz;

import java.util.List;
/**
 * Interface general de los servicios. Implementada en forma genérica por GestorHibernate.
 * @author Federico Muñoz
 *
 * @param <E>
 */
public interface GeneralServiceInterface<E> {
	/**
	 * Guarda un nuevo objeto en la base de datos
	 * @param objeto
	 */
	public void guardar(E objeto);
	
	/**
	 * Actualizar un objeto en la base de datos
	 * @param objeto
	 */
	public void actualizar(E objeto);
	
	/**
	 * Eliminar un objeto de la base
	 * @param clase
	 * @param id
	 */
	public void eliminar(E object);
	/**
	 * Eliminar un objeto por su id
	 * @param clase
	 * @param id
	 */
	public void eliminar(long id);
	
	/**
	 * Obtener un objeto por su ID
	 * @param clase
	 * @param id
	 * @return
	 */
	public E obtenerPorId(long id);
	/**
	 * Lista todos los objetos de la clase
	 * 
	 * @param clase
	 * @return Coleccion que contiene todos los objetos de la clase
	 */
	public List<E> listarTodos();
	/**
	 * Lista todos los objetos de la clase filtrados por lista
	 * 
	 * @param clase
	 * @return Coleccion que contiene todos los objetos de la clase
	 */
	public List<E> listarTodosFiltradoPorLista(CampoComparacion... campos);
	
	/**
	 * Lista todos los objetos de la clase ordenados por campo
	 * 
	 * @param campoOrden
	 * @return Coleccion que contiene todos los objetos de la clase ordenados por campoOrden
	 */
	public List<E> listarTodosOrdenado(String campoOrden);
}
