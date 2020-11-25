/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.security.accesoDatos.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.ClienteAspService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.seguridad.Group;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class ClienteAspServiceImp extends GestorHibernate<ClienteAsp> implements ClienteAspService {
	private static Logger logger=Logger.getLogger(ClienteAspServiceImp.class);
//	private ParameterService parameterService;
	
	@Autowired
	public ClienteAspServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}
	
	@Override
	public Class<ClienteAsp> getClaseModelo() {
		return ClienteAsp.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClienteAsp> getByPersona(String persona, String contacto) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(persona!=null && !"".equals(persona)){
        		crit.createCriteria("persona").add(Restrictions.ilike("razonSocial", persona + "%"));     	
        	}
        	if(contacto!=null && !"".equals(contacto)){
	        	crit.createCriteria("contacto").add(Restrictions.or(
    						Restrictions.ilike("nombre", contacto + "%"),
    						Restrictions.ilike("apellido", contacto + "%"))
    					);	        	
        	}
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
	public boolean actualuzarCliente(ClienteAsp cliente) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(cliente);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			//si ocurre algún error intentamos hacer rollback
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
		        } catch (HibernateException e1) {
		        	logger.error("no se pudo hacer rollback", e1);
		        }
		        logger.error("no se pudo actualizar ClienteAsp", e);
			}
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
	public boolean guardarNuevoCliente(ClienteAsp cliente) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			for(Group g:cliente.getUser().getGroups())
				session.save(g);
			//guardo los parametros en la misma transaccion
//			parameterService.guardar(cliente.getParametros(), session, cliente); 
			session.save(cliente.getUser());
			session.save(cliente.getContacto());
			session.save(cliente);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} catch (RuntimeException e) {
			rollback(tx, e);
			return false;
		} finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}

	@Override
	public ClienteAsp getByNombreAbreviado(String nombreAbrev) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("nombreAbreviado", nombreAbrev));
            return (ClienteAsp) crit.uniqueResult();
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
	
	private void rollback(Transaction tx, Exception e){
		//si ocurre algún error intentamos hacer rollback
		if (tx != null && tx.isActive()) {
			try {
				tx.rollback();
	        } catch (HibernateException e1) {
	        	logger.error("no se pudo hacer rollback", e1);
	        }
	        logger.error("no se pudo guardar ClienteAsp", e);
		}
	}
}
