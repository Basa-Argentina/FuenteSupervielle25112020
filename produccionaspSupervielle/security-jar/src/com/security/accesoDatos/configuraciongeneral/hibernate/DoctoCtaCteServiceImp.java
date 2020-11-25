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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.DoctoCtaCteService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.configuraciongeneral.DoctoCtaCte;
import com.security.modelo.configuraciongeneral.Factura;

/**
 * @author X
 */
@Component
public class DoctoCtaCteServiceImp extends GestorHibernate<DoctoCtaCte> implements DoctoCtaCteService{
	private static Logger logger=Logger.getLogger(DoctoCtaCteServiceImp.class);
	
	@Autowired
	public DoctoCtaCteServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<DoctoCtaCte> getClaseModelo() {
		return DoctoCtaCte.class;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<DoctoCtaCte> getByFactura(Factura objeto){
		Session session = null;
		List<DoctoCtaCte> result = null;
		try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	List<DoctoCtaCte> listaElementos = null;
        	if(objeto != null){
        		crit.add(Restrictions.eq("fc_nd", objeto));
        		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        		listaElementos = (List<DoctoCtaCte>)crit.list();        		
        	}else { 
        		listaElementos = new ArrayList<DoctoCtaCte>();
        	}
        	result = listaElementos;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        result = null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
        return result;
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
	public Boolean eliminarDoctoCtaCte(Factura objeto) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			String hql = "delete from docto_cta_cte where nc_rc_id = :idFactura";
	        Query query = session.createQuery(hql);
	        query.setLong("idFactura",objeto.getId());
	        query.executeUpdate();
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
}
