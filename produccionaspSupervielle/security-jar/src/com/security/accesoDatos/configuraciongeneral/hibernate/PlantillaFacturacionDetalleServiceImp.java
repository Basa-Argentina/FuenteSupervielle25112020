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
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionDetalleService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.PlantillaFacturacionDetalle;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;

/**
 * @author Victor Kenis
 *
 */
@Component
public class PlantillaFacturacionDetalleServiceImp extends GestorHibernate<PlantillaFacturacionDetalle> implements PlantillaFacturacionDetalleService {
	private static Logger logger=Logger.getLogger(PlantillaFacturacionDetalleServiceImp.class);
	
	@Autowired
	public PlantillaFacturacionDetalleServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<PlantillaFacturacionDetalle> getClaseModelo() {
		return PlantillaFacturacionDetalle.class;
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
	public Boolean guardarActualizarPlantillaFacturacionDetalle(PlantillaFacturacionDetalle plantillaDetalle) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.saveOrUpdate(plantillaDetalle);
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
	public List<PlantillaFacturacionDetalle> listarPlantillaDetallesPorPlantilla(Long idPlantilla, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("plantillaFacturacion", "plantilla");
        	
        	if(idPlantilla!=null){
        		crit.add(Restrictions.eq("plantilla.id", idPlantilla));
        	}else
        	{
        		return null;
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("plantilla.clienteAsp", cliente));
        	}
        	crit.addOrder(Order.asc("orden"));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	crit.setFetchMode("conceptoFacturable", FetchMode.JOIN);

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

}
