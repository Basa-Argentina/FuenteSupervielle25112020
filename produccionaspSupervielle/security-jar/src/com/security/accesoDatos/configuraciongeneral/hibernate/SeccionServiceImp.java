/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.SeccionService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Seccion;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class SeccionServiceImp extends GestorHibernate<Seccion> implements SeccionService {
	private static Logger logger=Logger.getLogger(SeccionServiceImp.class);
	
	@Autowired
	public SeccionServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Seccion> getClaseModelo() {
		return Seccion.class;
	}

	@Override
	public Boolean guardarSeccion(Seccion seccion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(seccion);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible guardar");
			return false;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	@Override
	public Boolean actualizarSeccion(Seccion seccion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(seccion);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible Actualizar");
			return false;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}	
	
	@Override
	public Boolean eliminarSeccion(Seccion seccion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(seccion);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible eliminar");
			return false;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}	
	
	
	private void rollback(Transaction tx, Exception e, String mensaje){
		//si ocurre algún error intentamos hacer rollback
		if (tx != null && tx.isActive()) {
			try {
				tx.rollback();
	        } catch (HibernateException e1) {
	        	logger.error("no se pudo hacer rollback "+getClaseModelo().getName(), e1);
	        }
	        logger.error(mensaje+" "+getClaseModelo().getName(), e);
		}
	}
	
	@Override
	public Seccion getByCodigo(String codigo) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("codigo", codigo));
            return (Seccion) crit.uniqueResult();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	@Override
	public List<Seccion> listarSeccionFiltradas(Seccion seccion, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("deposito", "dep");
        	crit.createCriteria("dep.sucursal", "suc");
        	crit.createCriteria("suc.empresa", "emp");
        	if(seccion!=null){
        		if(seccion.getIdDeposito() !=null)
	        		crit.add(Restrictions.eq("dep.id", seccion.getIdDeposito()));
        		if(seccion.getCodigoEmpresa()!= null && !"".equals(seccion.getCodigoEmpresa()))
            		crit.add(Restrictions.eq("emp.codigo", seccion.getCodigoEmpresa()));
        		if(seccion.getCodigoSucursal()!= null && !"".equals(seccion.getCodigoSucursal()))
            		crit.add(Restrictions.eq("suc.codigo", seccion.getCodigoSucursal()));
            	if(seccion.getCodigoDeposito() != null && !"".equals(seccion.getCodigoDeposito()))
            		crit.add(Restrictions.eq("dep.codigo", seccion.getCodigoDeposito()));
	        	if(seccion.getDescripcion() !=null && !"".equals(seccion.getDescripcion()))
	        		crit.add(Restrictions.ilike("descripcion", seccion.getDescripcion() + "%"));
	        	if(seccion.getCodigo() !=null && !"".equals(seccion.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", seccion.getCodigo() + "%"));	      
        	}
        	if(cliente != null)
        		crit.add(Restrictions.eq("emp.cliente", cliente));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	@Override
	public Seccion verificarSeccion(Seccion seccion, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("deposito", "dep");
        	if(seccion!=null){
        		if(seccion.getCodigoDeposito() != null)
            		crit.add(Restrictions.eq("dep.codigo", seccion.getCodigoDeposito()));
	        	if(seccion.getIdDeposito() !=null)
	        		crit.add(Restrictions.eq("dep.id", seccion.getIdDeposito()));	        	
	        	if(seccion.getCodigo() !=null && !"".equals(seccion.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", seccion.getCodigo()));	      
        	}
        	if(cliente != null)
        		crit.createCriteria("dep.sucursal").
        			createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Seccion> result = crit.list();
        	if(result.size() == 1){
        		return result.get(0);
        	}
            return null;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	@Override
	public List<Seccion> listarSeccionPopup(String val, String codigoDeposito, ClienteAsp cliente){
		Session session = null;
        try {        	
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.createCriteria("deposito", "dep");
        	//filtro value
        	if(val!=null){        		
        		c.add(Restrictions.ilike("descripcion", val+"%"));        	
        	}
        	if(codigoDeposito != null && !"".equals(codigoDeposito)){
        		c.add(Restrictions.eq("dep.codigo", codigoDeposito));
        	}
        	if(cliente != null){
	        	//filtro cliente
	        	c.createCriteria("dep.sucursal").createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
        	}
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	return c.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar las secciones.", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	@Override
	public Seccion getByCodigo(Seccion seccion, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("deposito", "dep");
        	crit.createCriteria("dep.sucursal", "suc");
        	crit.createCriteria("suc.empresa", "emp");
        	if(seccion!=null){
	        	if(seccion.getCodigo() !=null && !"".equals(seccion.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", seccion.getCodigo()));
        	}
        	if(clienteAsp != null){        		
        		crit.add(Restrictions.eq("emp.cliente", clienteAsp));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return (Seccion)crit.uniqueResult();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
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
