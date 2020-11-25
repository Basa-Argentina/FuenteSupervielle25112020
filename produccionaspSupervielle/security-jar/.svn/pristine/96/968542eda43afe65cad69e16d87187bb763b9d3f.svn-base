package com.security.accesoDatos.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.security.accesoDatos.interfaz.CampoComparacion;



/**
 * Gestor encargado de realizar todas 
 * las operaciones sobre la base de datos
 * utilizando hibernate
 * 
 * @author Gabriel Mainero
 * @modificado: Federico Muñoz: Agrego Generics y Log. 
 */

public abstract class GestorHibernate <E>{
	private static Logger logger = Logger.getLogger(GestorHibernate.class);
	
	//protected Session session;
	private HibernateControl hibernateControl;

	/**
	 * Constructor de GestorHibernate
	 * 
	 * @param hibernateControl
	 */
	
	public GestorHibernate(HibernateControl hibernateControl) {
		//this.session = hibernateControl.getSession();
		this.hibernateControl = hibernateControl;
	}
	
	protected Session getSession(){
		return hibernateControl.getSession();
	}
	
	/**
	 * Guarda un nuevo objeto en la base de datos
	 * @param objeto
	 */
	public synchronized void guardar(E objeto){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(objeto);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
		} 
		catch (RuntimeException e) {
			//si ocurre algún error intentamos hacer rollback
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo guardar", e);
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	/**
	 * Actualizar un objeto en la base de datos
	 * @param objeto
	 */
	public void actualizar(E objeto){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//actualizamos el objeto en la base de datos.
			session.update(objeto);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
		} 
		catch (RuntimeException e) {
			//si ocurre algún error intentamos hacer rollback
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo guardar", e);
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	/**
	 * Eliminar un objeto por su clase e id
	 * @param clase
	 * @param id
	 */
	public void eliminar(E object){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos una nueva transacción
			tx = session.getTransaction();
			tx.begin();
			session.delete(object);
			//hacemos commit a los cambios para que se refresque la base de datos.
			tx.commit();
		} 
		catch (RuntimeException e) {
			//si ocurre algún error, se hace rollback a los cambios.
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo guardar", e);
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		
	}
	/**
	 * Eliminar un objeto por su clase e id
	 * @param clase
	 * @param id
	 */
	public void eliminar(long id){
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos una nueva transacción
			tx = session.getTransaction();
			tx.begin();
			//si queremos utilizar el método delete de session, debemos
			//tener el objeto cargado.
			Object objeto = obtenerPorId(id);
			//borramos el objeto
			session.delete(objeto);
			//hacemos commit a los cambios para que se refresque la base de datos.
			tx.commit();
		} 
		catch (RuntimeException e) {
			//si ocurre algún error, se hace rollback a los cambios.
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo guardar", e);
			}
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		
	}
	
	/**
	 * Obtener un objeto por su ID
	 * @param clase
	 * @param id
	 * @return
	 */
	public E obtenerPorId(long id){
		Session session = null;
	    try {
	    	//obtenemos una sesión
			session = getSession();
	        return (E)session.get(getClaseModelo(),id);
	    } catch (HibernateException e) {
	        logger.error("ocurrió un error al obtener por id", e);
	    }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	    return null;
	} 
	
	/**
	 * Lista todos los objetos de la clase
	 * 
	 * @return Coleccion que contiene todos los objetos de la clase
	 */
	public List<E> listarTodos(){
		Session session = null;
	    try {
	    	//obtenemos una sesión
			session = getSession();
	        Criteria crit = session.createCriteria(getClaseModelo());
	        return crit.list();
	    } catch (HibernateException e1) {
	    	logger.error("no se pudo listar todos ", e1);
	        return null;
	    }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	/**
	 * Lista todos los objetos de la clase ordenados por campo
	 * 
	 * @param campoOrden
	 * @return Coleccion que contiene todos los objetos de la clase ordenados por campoOrden
	 */
	public List<E> listarTodosOrdenado(String campoOrden){
		Session session = null;
	    try {
	    	//obtenemos una sesión
			session = getSession();
	        Criteria crit = session.createCriteria(getClaseModelo()).addOrder(Order.asc(campoOrden));	        
	        return crit.list();
	    } catch (HibernateException e1) {
	    	logger.error("no se pudo listar todos ", e1);
	        return null;
	    }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	/**
     * implementar para saber sobre qué clase estamos trabajando
     * @return
     */
    public abstract Class<E> getClaseModelo();
    
    public List<E> listarTodosFiltradoPorLista(CampoComparacion... campos)
    {
    	Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	for (CampoComparacion campo:campos){
        		if (campo.getValor() == null){
        			crit.add(Restrictions.isNull(campo.getCampo()));
        		}else if (campo.getValor() instanceof String) {
                    crit.add(Restrictions.ilike(campo.getCampo(), campo.getValor()));
                } else {
                	crit.add(Restrictions.eq(campo.getCampo(), campo.getValor()));
                }	
        	}
            return crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("no se pudo hacer rollback ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
}
