/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/07/2011
 */
package com.security.accesoDatos.jerarquias.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.jerarquias.interfaz.RequerimientoReferenciaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;
import com.security.modelo.jerarquias.Requerimiento;
import com.security.modelo.jerarquias.RequerimientoReferencia;

/**
 * @author Gabriel Mainero
 *
 */
@Component
public class RequerimientoReferenciaServiceImp extends GestorHibernate<RequerimientoReferencia> implements RequerimientoReferenciaService{
	private static Logger logger=Logger.getLogger(RequerimientoReferenciaServiceImp.class);
	
	@Autowired
	public RequerimientoReferenciaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	

	@Override
	public Class<RequerimientoReferencia> getClaseModelo() {
		return RequerimientoReferencia.class;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public RequerimientoReferencia obtenerPendienteOEnProceso(Long referencia, Long requerimiento){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("requerimiento", "req");
        	crit.createCriteria("referencia", "ref");
	    	Criterion pendiente = Restrictions.eq("req.estado","Pendiente");
	    	Criterion enProceso = Restrictions.eq("req.estado","En Proceso");
	        LogicalExpression orExp = Restrictions.or(pendiente,enProceso);
	        crit.add(orExp);
	        crit.add(Restrictions.eq("ref.id", referencia));
	        if(requerimiento!=null)
	        	crit.add(Restrictions.ne("req.id", requerimiento));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List<RequerimientoReferencia> salida = crit.list();
            if(salida!=null && salida.size()>0)
            	return salida.get(0);
            else
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
	public List<RequerimientoReferencia> listarRequerimientoReferenciaPorRequerimiento(Requerimiento requerimiento, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("requerimiento", "requerimiento");
        	if(requerimiento!=null && cliente != null){
        		crit.add(Restrictions.eq("requerimiento", requerimiento));
        		crit.add(Restrictions.eq("requerimiento.clienteAsp", cliente));
        		crit.addOrder(Order.asc("referencia"));
            	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

                return crit.list();
        	}
        	else
        		return new ArrayList<RequerimientoReferencia>();
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
