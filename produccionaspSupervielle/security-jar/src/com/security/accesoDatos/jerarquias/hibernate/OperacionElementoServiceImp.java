/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.accesoDatos.jerarquias.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.jerarquias.interfaz.OperacionElementoService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;

/**
 * @author Gabriel Mainero
 *
 */
@Component
public class OperacionElementoServiceImp extends GestorHibernate<OperacionElemento> implements OperacionElementoService{
	private static Logger logger=Logger.getLogger(OperacionElementoServiceImp.class);
	
	@Autowired
	public OperacionElementoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<OperacionElemento> getClaseModelo() {
		return OperacionElemento.class;
	}
	
	@Override
	public List<OperacionElemento> listarOperacionElementoPorOperacion(Operacion operacion, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("operacion", "operacion");
        	if(operacion!=null && cliente != null){
        		crit.add(Restrictions.eq("operacion", operacion));
        		crit.add(Restrictions.eq("operacion.clienteAsp", cliente));
        		crit.addOrder(Order.asc("elemento"));
            	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

                return crit.list();
        	}
        	else
        		return new ArrayList<OperacionElemento>();
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
	public Integer cantidadOperacionElementoPorOperacion(Operacion operacion, ClienteAsp cliente){
		Session session = null;
		Integer result = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.rowCount());
        	
        	crit.createCriteria("operacion", "operacion");
        	
        	crit.add(Restrictions.eq("operacion", operacion));
        	crit.add(Restrictions.eq("operacion.clienteAsp", cliente));
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            result = ((Integer)crit.list().get(0));
        	return  result;
        
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
	public List<OperacionElemento> listarOperacionElementoPorElementoYEstado(Elemento elemento, String estado, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	if(elemento!=null && cliente != null){
        		crit.add(Restrictions.eq("elemento", elemento));
        		crit.add(Restrictions.eq("estado", estado));
        		crit.createCriteria("operacion", "operacion");
        		crit.add(Restrictions.eq("operacion.clienteAsp", cliente));
        		crit.addOrder(Order.asc("elemento"));
            	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

                return crit.list();
        	}
        	else
        		return new ArrayList<OperacionElemento>();
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
