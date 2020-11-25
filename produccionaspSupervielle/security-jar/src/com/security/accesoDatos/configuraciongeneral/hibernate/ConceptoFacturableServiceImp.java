/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.ConceptoFacturableService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.configuraciongeneral.TipoConceptoFacturable;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class ConceptoFacturableServiceImp extends GestorHibernate<ConceptoFacturable> implements ConceptoFacturableService {
	private static Logger logger=Logger.getLogger(ConceptoFacturableServiceImp.class);
	
	@Autowired
	public ConceptoFacturableServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<ConceptoFacturable> getClaseModelo() {
		return ConceptoFacturable.class;
	}

	@Override
	public boolean delete(ConceptoFacturable o) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(o);
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
	public boolean save(ConceptoFacturable o) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(o);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			tx.commit();
			return true;
		} 
		catch (RuntimeException e) {
			rollback(tx, e, "No fue posible Guardar");
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
	public boolean update(ConceptoFacturable o) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(o);
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
	public List<ConceptoFacturable> listarPorFiltro(Boolean habilitado,
			String codigo, String descrip, Boolean generaStock,
			String tipoCalculo, TipoConceptoFacturable tipo,
			ClienteAsp cliente) { 
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			//filtro por habilitado
			if(habilitado != null && !"".equals(habilitado))
				c.add(Restrictions.eq("habilitado", habilitado));
			//filtro por codigo
			if(codigo != null && !"".equals(codigo))
				c.add(Restrictions.ilike("codigo", codigo+"%"));
			//filtro por descripcion
			if(descrip != null && !"".equals(descrip))
				c.add(Restrictions.ilike("descripcion", descrip+"%"));
			//filtro por generaStock
			if(generaStock != null && !"".equals(generaStock))
				c.add(Restrictions.eq("generaStock", generaStock));			
			//filtro por tipoCalculo
			if(tipoCalculo != null && !"".equals(tipoCalculo))
				c.add(Restrictions.eq("tipoCalculo", tipoCalculo));			
			//filtro por tipo
			if(tipo != null && !"".equals(tipo))
				c.add(Restrictions.eq("tipo", tipo));			
			//filtro por cliente
			if(cliente != null)
				c.add(Restrictions.eq("clienteAsp", cliente));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			c.setFetchMode("tipo", FetchMode.JOIN);
			
			return c.list();
		} catch (HibernateException e) {
			logger.error("no se pudo listar conceptos facturables",e);
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
	@Override
	public List<TipoConceptoFacturable> listarTiposConceptosFacturables() {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(TipoConceptoFacturable.class);			
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return c.list();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener persona por mail",e);
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
	public TipoConceptoFacturable obtenerTipoPorId(Long id) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(TipoConceptoFacturable.class);
			//filtro por ID
			c.add(Restrictions.eq("id", id));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return (TipoConceptoFacturable) c.uniqueResult();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener persona por mail",e);
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
	public List<ConceptoFacturable> listarConceptosFacturablesPopup(String val, ClienteAsp clienteAsp,Boolean habilitado) {
		return listarConceptosFacturablesPopup(val, clienteAsp, 0, 0, habilitado);
	}
	
	@Override
	public List<ConceptoFacturable> listarConceptosFacturablesPopup(String val, ClienteAsp clienteAsp) {
		return listarConceptosFacturablesPopup(val, clienteAsp, 0, 0);
	}
	/**
	 * 
	 * @param val
	 * @param clienteAsp
	 * @param mode mode=0: todos, mode>0:solo los conceptos que generan stock, mode<0: solo los conceptos que no generan stock 
	 * @param tipo tipo=0: todos, tipo=1:solo los conceptos de tipo mensual, tipo=2: solo los conceptos de tipo demanda
	 * @return
	 */
	
	@Override
	public List<ConceptoFacturable> listarConceptosFacturablesPopup(String val, ClienteAsp clienteAsp, int mode, int tipo) {
		return listarConceptosFacturablesPopup(val, clienteAsp, mode, tipo, null);
	}
	/**
	 * 
	 * @param val
	 * @param clienteAsp
	 * @param mode mode=0: todos, mode>0:solo los conceptos que generan stock, mode<0: solo los conceptos que no generan stock 
	 * @param tipo tipo=0: todos, tipo=1:solo los conceptos de tipo mensual, tipo=2: solo los conceptos de tipo demanda
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ConceptoFacturable> listarConceptosFacturablesPopup(String val, ClienteAsp clienteAsp, int mode, int tipo, Boolean habilitado) {
		Session session = null;
		if(clienteAsp == null)
			return null;
        try {
        	String valores[] = null;
        	if(val!=null)
        		valores = val.split(" ");
        	//obtenemos una sesión
			session = getSession();
			
			
//        	Criteria c = session.createCriteria(getClaseModelo());
//        	//filtro value
//        	if(valores!=null){
//        		for(String filtro : valores){
//		        	c.add(Restrictions.or(
//		        			Restrictions.ilike("codigo", filtro+"%"), 
//		        			Restrictions.ilike("descripcion", filtro+"%")));		        				
//        		}
//        	}
//        	if(mode>0)
//        	{
//        		c.add(Restrictions.eq("generaStock", true));
//        	}else if(mode<0)
//        	{
//        		c.add(Restrictions.eq("generaStock", false));
//        	}
//        	//filtro Tipo
//        	if(tipo==1)
//        	{
//        		c.add(Restrictions.eq("tipo.id", Long.valueOf(tipo)));
//        	}else if(tipo==2)
//        	{
//        		c.add(Restrictions.eq("tipo.id", Long.valueOf(tipo)));
//        	}
//        	//filtro clienteAsp
//        	c.add(Restrictions.eq("clienteAsp", clienteAsp));
//        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			String consulta = "SELECT DISTINCT cf FROM ConceptoFacturable cf JOIN FETCH cf.tipo JOIN FETCH cf.impuestos WHERE 1 = 1 ";
							   if(valores != null){
								   for(String filtro : valores) {
									   consulta += "AND cf.codigo LIKE '" + filtro + "%' OR cf.descripcion LIKE '" + filtro + "%' "; 
								   }
							   }
							   if(mode > 0) {
								   consulta += "AND cf.generaStock = TRUE ";
							   }
							   else if(mode < 0) {
								   consulta += "AND cf.generaStock = FALSE ";
							   }
							   if(tipo == 1) {
								   consulta += "AND cf.tipo.id = " + Long.valueOf(tipo) + " ";
							   }
							   else if(tipo == 2) {
								   consulta += "AND cf.tipo.id = " + Long.valueOf(tipo) + " ";
							   }
							   consulta += "AND cf.clienteAsp.id = " + clienteAsp.getId().longValue() + " ";
							   
			List<ConceptoFacturable> conceptos = (List<ConceptoFacturable>) session.createQuery(consulta).list();
        	
			return conceptos;
			
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConceptoFacturable> listarConceptosFacturablesByListaPrecioPopup(
			String val, String valCodigo, ClienteAsp clienteAsp) {
		List<ConceptoFacturable> conceptos = new ArrayList<ConceptoFacturable>();
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(ListaPreciosDetalle.class);
			c.createCriteria("listaPrecios", "l");
			
			if(valCodigo != null && !"".equals(valCodigo)){
				//Para los detalle de lista de precio
				c.add(Restrictions.eq("l.codigo", valCodigo));
			}
				
			if(val != null && !"".equals(val)){
				c.add(Restrictions.ilike("conceptoFacturable.codigo", val+"%"));
			}
			
			if(clienteAsp != null){
				c.add(Restrictions.eq("l.clienteAsp", clienteAsp));
			}
			
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			for(ListaPreciosDetalle detalle: (ArrayList<ListaPreciosDetalle>)c.list()){
//				if("Mensual".equals(detalle.getConceptoFacturable().getTipo().getDescripcion())){
					conceptos.add(detalle.getConceptoFacturable());
//				}
			}
			return conceptos;
		} catch (HibernateException e) {
			logger.error("no se pudo obtener listado de detalles de lista de precios",e);
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
	public ConceptoFacturable obtenerConceptoFacturablePorCodigo(String codigo, ClienteAsp clienteAsp) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			//filtro por texto
			if(codigo != null || !"".equals(codigo)){
				c.add(Restrictions.eq("codigo", codigo));		
				//filtro por cliente
				if(clienteAsp != null)
					c.add(Restrictions.eq("clienteAsp", clienteAsp));
				return (ConceptoFacturable) c.uniqueResult();
			}
			return null;
		} catch (HibernateException e) {
			logger.error("no se pudo listar conceptos facturables",e);
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
