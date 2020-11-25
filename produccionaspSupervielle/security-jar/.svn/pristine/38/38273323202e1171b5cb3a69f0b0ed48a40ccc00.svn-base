/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.SecuenciaTablaService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.SecuenciaTabla;

/**
 * @author Victor Kenis
 *
 */
@Component
public class SecuenciaTablaServiceImp extends GestorHibernate<SecuenciaTabla> implements SecuenciaTablaService {
	private static Logger logger=Logger.getLogger(SecuenciaTablaServiceImp.class);
	
	@Autowired
	public SecuenciaTablaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<SecuenciaTabla> getClaseModelo() {
		return SecuenciaTabla.class;
	}

	@Override
	public Boolean guardarSecuenciaTabla(SecuenciaTabla afipCondIva) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(afipCondIva);
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
	public Boolean actualizarSecuenciaTabla(SecuenciaTabla afipCondIva) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(afipCondIva);
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
	public Boolean eliminarSecuenciaTabla(SecuenciaTabla afipCondIva) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(afipCondIva);
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
	
	@Override
	public synchronized Long obtenerSecuencia(ClienteAsp cliente,Class parmClass)
	{
		
		Session session = null;
		SecuenciaTabla secuencia;
		Long ultimoNumero = 1L;
		Transaction tx = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	if(cliente!=null)
        	{
	        	crit.add(Restrictions.eq("clienteAsp", cliente));
        		crit.add(Restrictions.eq("nombreTabla", parmClass.getSimpleName()));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
        	secuencia = (SecuenciaTabla) crit.uniqueResult();
        	
        	if(secuencia==null)
        	{
        		secuencia = new SecuenciaTabla();
    			secuencia.setClienteAsp(cliente);
    			secuencia.setNombreTabla(parmClass.getSimpleName());
    			secuencia.setNroSecuencia(ultimoNumero);
        	}
        	else
        	{
        		ultimoNumero = secuencia.getNroSecuencia();
        		ultimoNumero++;
        	}
        	        		
        	secuencia.setNroSecuencia(ultimoNumero);
        	
        	tx = session.getTransaction();
			tx.begin();
        	session.saveOrUpdate(secuencia);
        	tx.commit();
        	
        	return ultimoNumero;
        	
        }catch (RuntimeException e) {
    			rollback(tx, e, "No se pudo obtener el ultimo numero ");
    			return null;
        }
        finally
        {
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
}
