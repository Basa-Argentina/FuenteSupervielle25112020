/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/07/2011
 */
package com.security.accesoDatos.jerarquias.hibernate;

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
import com.security.accesoDatos.jerarquias.interfaz.JerarquiaService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.jerarquias.Jerarquia;
import com.security.modelo.jerarquias.TipoJerarquia;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class JerarquiaServiceImp extends GestorHibernate<Jerarquia> implements JerarquiaService{
	private static Logger logger=Logger.getLogger(JerarquiaServiceImp.class);

	@Autowired
	public JerarquiaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Jerarquia> getClaseModelo() {
		return Jerarquia.class;
	}

	@Override
	public boolean delete(Jerarquia objeto) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(objeto);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible Eliminar");
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
	public boolean save(Jerarquia objeto) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(objeto);
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
	public boolean update(Jerarquia objeto) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(objeto);
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Jerarquia> listarJerarquiaPorTipoJerarquia(TipoJerarquia tipo, ClienteAsp clienteAsp) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			//filtro por tipo y cliente
			if(tipo != null && !"".equals(tipo))
				c.createCriteria("tipo").add(Restrictions.idEq(tipo.getId()))
					.add(Restrictions.eq("clienteAsp", clienteAsp));
				c.add(Restrictions.eq("tipo", tipo));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return c.list();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener listas de precios",e);
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
	public List<Jerarquia> listarJerarquiasPopup(String val, String codigoTipoJerarquia, ClienteAsp cliente){
		Session session = null;
        try {        	
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.createCriteria("tipo", "tip");
        	
        	//filtro value
        	if(val!=null && !"".equals(val)){        		
        		c.add(Restrictions.ilike("descripcion", val+"%"));
        	}
        	if(codigoTipoJerarquia!= null && !"".equals(codigoTipoJerarquia)){
        		c.add(Restrictions.eq("tip.codigo", codigoTipoJerarquia));
        	}
        	if(cliente != null){
	        	//filtro cliente
	        	c.add(Restrictions.eq("tip.clienteAsp", cliente));
        	}
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	return c.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar los tipos de jerarquia.", hibernateException);
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
	public Jerarquia obtenerJerarquiaPorCodigo(String codigo, String codigoTipoJerarquia,ClienteAsp clienteAsp) {
		Session session = null;
		if(clienteAsp == null)
			return null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			c.createCriteria("tipo", "tip");
			//filtro por id
			c.add(Restrictions.eq("id", Long.valueOf(codigo)));
			//filtro por codigo del tipo de jerarquia
			if(codigoTipoJerarquia!= null && !"".equals(codigoTipoJerarquia))
				c.add(Restrictions.eq("tip.codigo", codigoTipoJerarquia));
			//filtro por cliente
			c.add(Restrictions.eq("tip.clienteAsp", clienteAsp));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return (Jerarquia) c.uniqueResult();		
		} catch (HibernateException e) {
			logger.error("no se pudo obtener lista",e);
			return null;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	@SuppressWarnings("unchecked")
	public boolean seSuperPonenJerarquias(Jerarquia j, TipoJerarquia tipo, ClienteAsp clienteAsp){
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			//filtro por tipo y cliente
			if(tipo != null && !"".equals(tipo))
				c.createCriteria("tipo").add(Restrictions.idEq(tipo.getId()))
					.add(Restrictions.eq("clienteAsp", clienteAsp));
				c.add(Restrictions.eq("tipo", tipo));			
			//filtro por rangos jerarquias
			c.add(
				Restrictions.or(
					Restrictions.and(
						Restrictions.le("verticalDesde", j.getVerticalDesde()), 
						Restrictions.ge("verticalHasta", j.getVerticalDesde())
					), 
					Restrictions.and(
						Restrictions.le("verticalDesde", j.getVerticalHasta()), 
						Restrictions.ge("verticalHasta", j.getVerticalHasta())
					)
				)					
			);
			c.add(
					Restrictions.or(
						Restrictions.and(
							Restrictions.le("horizontalDesde", j.getHorizontalDesde()), 
							Restrictions.ge("horizontalHasta", j.getHorizontalDesde())
						), 
						Restrictions.and(
							Restrictions.le("horizontalDesde", j.getHorizontalHasta()), 
							Restrictions.ge("horizontalHasta", j.getHorizontalHasta())
						)
					)	
					
				);
			//si es modificacion que no me traiga el original
			if(!"NUEVO".equals(j.getAccion()))
				c.add(Restrictions.ne("id", j.getId()));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List l = c.list();
			if(l.isEmpty())
				return false;
			else
				return true;
		} catch (HibernateException e) {
			logger.error("no se pudo obtener listas de precios",e);
			return true;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	@SuppressWarnings("unchecked")
	public boolean seRepiteValoracion(Jerarquia j, TipoJerarquia tipo, ClienteAsp clienteAsp){
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			//filtro por tipo y cliente
			if(tipo != null && !"".equals(tipo))
				c.createCriteria("tipo").add(Restrictions.idEq(tipo.getId()))
					.add(Restrictions.eq("clienteAsp", clienteAsp));
				c.add(Restrictions.eq("tipo", tipo));			
			//filtro por valoracion
			c.add(Restrictions.eq("valoracion", j.getValoracion()));
			//si es modificacion que no me traiga el original
			if(!"NUEVO".equals(j.getAccion()))
				c.add(Restrictions.ne("id", j.getId()));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List l = c.list();
			if(l.isEmpty())
				return false;
			else
				return true;
		} catch (HibernateException e) {
			logger.error("no se pudo obtener listas de precios",e);
			return true;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean seRepiteDescripcion(Jerarquia j, TipoJerarquia tipo,	ClienteAsp clienteAsp) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			//filtro por tipo y cliente
			if(tipo != null && !"".equals(tipo))
				c.createCriteria("tipo").add(Restrictions.idEq(tipo.getId()))
					.add(Restrictions.eq("clienteAsp", clienteAsp));
				c.add(Restrictions.eq("tipo", tipo));			
			//filtro por descripcion
			c.add(Restrictions.eq("descripcion", j.getDescripcion()));
			//si es modificacion que no me traiga el original
			if(!"NUEVO".equals(j.getAccion()))
				c.add(Restrictions.ne("id", j.getId()));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List l = c.list();
			if(l.isEmpty())
				return false;
			else
				return true;
		} catch (HibernateException e) {
			logger.error("no se pudo obtener listas de precios",e);
			return true;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public Jerarquia traerValoracionDePosicion(Posicion posicionLibre,TipoJerarquia tipo, ClienteAsp clienteAsp) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			//filtro por tipo y cliente
			if(tipo != null && !"".equals(tipo))
				c.createCriteria("tipo").add(Restrictions.idEq(tipo.getId()))
					.add(Restrictions.eq("clienteAsp", clienteAsp));
				c.add(Restrictions.eq("tipo", tipo));
			//filtro la jerarquia segun donde cae la posicion libre
			if (posicionLibre != null) {
				if (posicionLibre.getPosVertical() != null && posicionLibre.getPosVertical() != 0) {
					c.add(Restrictions.le("verticalDesde",posicionLibre.getPosVertical()));
					c.add(Restrictions.ge("verticalHasta",posicionLibre.getPosVertical()));
				}
				if(posicionLibre.getPosHorizontal() != null && posicionLibre.getPosHorizontal() != 0)
				{
					c.add(Restrictions.le("horizontalDesde",posicionLibre.getPosHorizontal()));
					c.add(Restrictions.ge("horizontalHasta",posicionLibre.getPosHorizontal()));
				}
			}
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return (Jerarquia) c.uniqueResult();
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
}
