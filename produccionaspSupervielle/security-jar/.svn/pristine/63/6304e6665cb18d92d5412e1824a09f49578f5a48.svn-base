/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 11/07/2011
 */
package com.security.accesoDatos.jerarquias.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.jerarquias.interfaz.OperacionReferenciaService;
import com.security.modelo.jerarquias.Operacion;
import com.security.modelo.jerarquias.OperacionElemento;

/**
 * @author Gabriel Mainero
 *
 */
@Component
public class OperacionReferenciaServiceImp extends GestorHibernate<OperacionElemento> implements OperacionReferenciaService{
	private static Logger logger=Logger.getLogger(OperacionReferenciaServiceImp.class);
	
	@Autowired
	public OperacionReferenciaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	

	@Override
	public Class<OperacionElemento> getClaseModelo() {
		return OperacionElemento.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OperacionElemento> listarOperacionReferenciaFiltradas(OperacionElemento operacionReferencia, Operacion operacion){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(operacionReferencia!=null){
        		if(operacionReferencia.getEstado()!=null && !operacionReferencia.getEstado().equals("Todos"))
        			crit.add(Restrictions.eq("estado", operacionReferencia.getEstado())); 			
        	}
        	if(operacion != null)
        		crit.add(Restrictions.eq("operacion", operacion));
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
	
	private Date getDateFrom(Date from) {
		Calendar calendarInicio = Calendar.getInstance();
		calendarInicio.setTime(from);
		calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
		calendarInicio.set(Calendar.MINUTE, 0);
		calendarInicio.set(Calendar.SECOND, 0);
		calendarInicio.set(Calendar.MILLISECOND, 0);
		return calendarInicio.getTime();
	}

	private Date getDateTo(Date to) {
		Calendar calendarFin = Calendar.getInstance();
		calendarFin.setTime(to);
		calendarFin.set(Calendar.HOUR_OF_DAY, 23);
		calendarFin.set(Calendar.MINUTE, 59);
		calendarFin.set(Calendar.SECOND, 59);
		calendarFin.set(Calendar.MILLISECOND, 999);
		return calendarFin.getTime();
	}
	
}
