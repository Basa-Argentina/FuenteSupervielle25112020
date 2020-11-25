/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 12/07/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.MedioPagoReciboService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.MedioPagoRecibo;

/**
 * @author X
 */
@Component
public class MedioPagoReciboServiceImp extends GestorHibernate<MedioPagoRecibo> implements MedioPagoReciboService{
	private static Logger logger=Logger.getLogger(MedioPagoReciboServiceImp.class);
	
	@Autowired
	public MedioPagoReciboServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<MedioPagoRecibo> getClaseModelo() {
		return MedioPagoRecibo.class;
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
	public Boolean eliminarMedioPagoRecibo(Factura objeto) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			String hql = "delete from medio_pago_recibo where factura_id = :idFactura";
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
