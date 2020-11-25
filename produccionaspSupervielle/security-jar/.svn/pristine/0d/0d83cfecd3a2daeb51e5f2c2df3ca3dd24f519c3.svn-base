/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.RearchivoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.LoteRearchivo;
import com.security.modelo.configuraciongeneral.Rearchivo;
import com.security.modelo.configuraciongeneral.Referencia;

/**
 * @author Gabriel Mainero
 *
 */
@Component
public class RearchivoServiceImp extends GestorHibernate<Rearchivo> implements RearchivoService{
	private static Logger logger=Logger.getLogger(RearchivoServiceImp.class);
	
	@Autowired
	public RearchivoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Rearchivo> getClaseModelo() {
		return Rearchivo.class;
	}
	
	
	@Override
	public List<Rearchivo> obtenerParaReferencia(Referencia referencia) {
		Session session = null;
	    try {
	    	//obtenemos una sesión
			session = getSession();
			List<Rearchivo> salida = null;
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.add(Restrictions.eq("estado", "Pendiente"));
	        crit.createCriteria("referencia", "ref");
	        crit.add(Restrictions.eq("ref.referenciaRearchivo", referencia));
	        crit.add(Restrictions.eq("ref.descripcionRearchivo", "Rearchivo"));

	        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        salida = crit.list();
	        return salida;
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
	
	@Override
	public String obtenerPorReferencia(Long idReferencia) {
		Session session = null;
	    try {
	    	//obtenemos una sesión
			session = getSession();
			
	       
			String consulta = "select rea.nombreArchivoDigital from rearchivo rea where rea.referencia_id = "+idReferencia;
			
			String nombreArchivo = (String) session.createSQLQuery(consulta).uniqueResult();
			
	       
	        return nombreArchivo;
	        
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
	
	@Override
	public Rearchivo obtenerRearchivoPorReferencia(Long idReferencia) {
		Session session = null;
	    try {
	    	//obtenemos una sesión
			session = getSession();
			
	       
			String consulta = "select * from rearchivo rea where rea.referencia_id = "+idReferencia;
			
			
			
			Rearchivo rearchivo = (Rearchivo) session.createSQLQuery(consulta).addEntity(Rearchivo.class).uniqueResult();
	       
	        return rearchivo;
	        
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
	
	@Override
	public List<Rearchivo> listarRearchivoPorLote(LoteRearchivo loteRearchivo, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("loteRearchivo", "loteRearchivo");
        	if(loteRearchivo!=null && cliente != null){
        		crit.add(Restrictions.eq("loteRearchivo", loteRearchivo));
        		crit.add(Restrictions.eq("loteRearchivo.clienteAsp", cliente));
        		crit.addOrder(Order.asc("referencia"));
            	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

                return crit.list();
        	}
        	else
        		return new ArrayList<Rearchivo>();
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
	public Rearchivo obtenerRearchivoPorElemento(Elemento elemento) {
		Session session = null;
	    try {
	    	//obtenemos una sesión
			session = getSession();
			List<Rearchivo> salida = null;
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.createCriteria("referencia", "ref");
	        crit.createCriteria("ref.elemento", "ele");
	        crit.add(Restrictions.eq("ele.id", elemento.getId()));

	        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        salida = crit.list();
	        if(salida!=null && salida.size()>0)
	        	return salida.get(0);
	        else
	        	return null;
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
}
