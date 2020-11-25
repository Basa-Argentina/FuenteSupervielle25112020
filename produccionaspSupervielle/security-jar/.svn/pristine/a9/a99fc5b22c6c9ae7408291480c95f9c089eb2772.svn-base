/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import sun.nio.cs.HistoricallyNamedCharset;

import com.security.accesoDatos.configuraciongeneral.interfaz.ReferenciaHistoricoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.ReferenciaHistorico;
import com.security.modelo.seguridad.User;

/**
 * @author Victor Kenis
 *
 */
@Component
public class ReferenciaHistoricoServiceImp extends GestorHibernate<ReferenciaHistorico> implements ReferenciaHistoricoService {
	private static Logger logger=Logger.getLogger(ReferenciaHistoricoServiceImp.class);
	
	@Autowired
	public ReferenciaHistoricoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<ReferenciaHistorico> getClaseModelo() {
		return ReferenciaHistorico.class;
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
	@SuppressWarnings("unchecked")
	public List<ReferenciaHistorico> listarReferenciaHistorico(ReferenciaHistorico referenciaHistorico, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	List<Long> ids = obtenerIDsReferenciaHistoricoFiltrados(referenciaHistorico, clienteAsp);
        	
        	//obtenemos una sesión
			session = getSession();
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<ReferenciaHistorico>();
			
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.add(Restrictions.in("id", ids));

        	//Ordenamos
        	if(referenciaHistorico.getSortOrder()!=null && referenciaHistorico.getSortOrder().length()>0 &&
        			referenciaHistorico.getFieldOrder()!=null && referenciaHistorico.getFieldOrder().length()>0){
            				
    		
    			String fieldOrdenar = "";
        		String fieldOrdenar2 = "";
        		
        		if("idReferencia".equals(referenciaHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "idReferencia";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("idLoteReferencia".equals(referenciaHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "idLoteReferencia";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("nombreCliente".equals(referenciaHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "nombreCliente";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("accion".equals(referenciaHistorico.getFieldOrder())){
					fieldOrdenar = "accion";
					fieldOrdenar2 = "fechaHora";
				}
				if("usuario".equals(referenciaHistorico.getFieldOrder())){
					c.createCriteria("usuario", "user");
        			fieldOrdenar = "user.id";
        			fieldOrdenar2 = "fechaHora";
				}
				if("fechaHora".equals(referenciaHistorico.getFieldOrder())){
					fieldOrdenar = "fechaHora";
					fieldOrdenar2 = "id";
				}
				if("nombreCliente".equals(referenciaHistorico.getFieldOrder())){
					fieldOrdenar = "nombreCliente";
					fieldOrdenar2= "fechaHora";
				}
				if("codigoElemento".equals(referenciaHistorico.getFieldOrder())){
					fieldOrdenar = "codigoElemento";
					fieldOrdenar2= "fechaHora";
				}
				if("codigoContenedor".equals(referenciaHistorico.getFieldOrder())){
					fieldOrdenar = "codigoContenedor";
					fieldOrdenar2= "fechaHora";
				}
						
            		
            		if("1".equals(referenciaHistorico.getSortOrder())){
            			if(!"".equals(fieldOrdenar))
        					c.addOrder(Order.asc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2)){
            				if("id".equalsIgnoreCase(fieldOrdenar2))
            					c.addOrder(Order.desc(fieldOrdenar2));
            				else
            					c.addOrder(Order.asc(fieldOrdenar2));
            			}
        			}else if("2".equals(referenciaHistorico.getSortOrder())){
        				if(!"".equals(fieldOrdenar))
        					c.addOrder(Order.desc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					c.addOrder(Order.desc(fieldOrdenar2));
        			}
            	
            	}else{
            		String fieldOrdenar = "fechaHora";
        			String fieldOrdenar2 = "id";
        			c.addOrder(Order.desc(fieldOrdenar));
        			c.addOrder(Order.desc(fieldOrdenar2));
            	}
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            return c.list();
			
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
	public Integer contarReferenciaHistoricoFiltrados(ReferenciaHistorico referenciaHistorico, ClienteAsp clienteAsp){
		
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.setProjection(Projections.rowCount());
  			            	
    			if(referenciaHistorico!=null){
    				//filtro por referencia
    				if(referenciaHistorico.getIdReferencia()!=null && !"".equals(referenciaHistorico.getIdReferencia()))
    					c.add(Restrictions.eq("idReferencia", referenciaHistorico.getIdReferencia()));
    				
    				//filtro por lote de referencia
    				if(referenciaHistorico.getIdLoteReferencia()!=null && !"".equals(referenciaHistorico.getIdLoteReferencia()))
    					c.add(Restrictions.eq("idLoteReferencia", referenciaHistorico.getIdLoteReferencia()));
    				
    				//filtro por usuario
    				if(referenciaHistorico.getCodigoUsuario()!=null && !referenciaHistorico.getCodigoUsuario().equals(""))
    					c.createCriteria("usuario", "user").add(Restrictions.eq("user.id", referenciaHistorico.getCodigoUsuario()));
    				
    				//filtro por accion
    				if(referenciaHistorico.getAccion()!=null && !referenciaHistorico.getAccion().equals(""))
    					c.add(Restrictions.eq("accion", referenciaHistorico.getAccion()));
    				
    				//filtro por fecha
    				if(referenciaHistorico.getFechaHora()!=null)
    					c.add(Restrictions.eq("fechaHora", referenciaHistorico.getFechaHora()));
    				
    				//filtro por fecha desde 
    				if(referenciaHistorico.getFechaDesde()!=null)
    					c.add(Restrictions.ge("fechaHora", referenciaHistorico.getFechaDesde()));
    				
    				//filtro por fecha hasta
    				if(referenciaHistorico.getFechaHasta()!=null)
    					c.add(Restrictions.le("fechaHora", referenciaHistorico.getFechaHasta()));
    				
    				//filtro por clienteEmp
    				if(referenciaHistorico.getCodigoCliente()!=null && !referenciaHistorico.getCodigoCliente().equals(""))
    					c.add(Restrictions.eq("codigoCliente", referenciaHistorico.getCodigoCliente()));
    				
    				//filtro por elemento
    				if(referenciaHistorico.getCodigoElemento()!=null && !referenciaHistorico.getCodigoElemento().equals(""))
    					c.add(Restrictions.eq("codigoElemento", referenciaHistorico.getCodigoElemento()));
    				
    				//filtro por contenedor
    				if(referenciaHistorico.getCodigoContenedor()!=null && !referenciaHistorico.getCodigoContenedor().equals(""))
    					c.add(Restrictions.eq("codigoContenedor", referenciaHistorico.getCodigoContenedor()));
    			}
    			
    		//filtro por clienteAsp
    		if(clienteAsp!=null)
    			c.add(Restrictions.eq("clienteAsp", clienteAsp));
        	
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	        	
        	result = ((Integer)c.list().get(0));
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
	
	private List<Long> obtenerIDsReferenciaHistoricoFiltrados(ReferenciaHistorico referenciaHistorico, ClienteAsp clienteAsp)
	{
		
		Session session = null;
		List<Long> result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.setProjection(Projections.id());
        	
			if(referenciaHistorico!=null){
				
				//filtro por referencia
				if(referenciaHistorico.getIdReferencia()!=null && !"".equals(referenciaHistorico.getIdReferencia()))
					c.add(Restrictions.eq("idReferencia", referenciaHistorico.getIdReferencia()));
				
				//filtro por lote de referencia
				if(referenciaHistorico.getIdLoteReferencia()!=null && !"".equals(referenciaHistorico.getIdLoteReferencia()))
					c.add(Restrictions.eq("idLoteReferencia", referenciaHistorico.getIdLoteReferencia()));
				
				//filtro por usuario
				if(referenciaHistorico.getCodigoUsuario()!=null && !referenciaHistorico.getCodigoUsuario().equals(""))
					c.createCriteria("usuario", "user").add(Restrictions.eq("user.id", referenciaHistorico.getCodigoUsuario()));
				
				//filtro por accion
				if(referenciaHistorico.getAccion()!=null && !referenciaHistorico.getAccion().equals(""))
					c.add(Restrictions.eq("accion", referenciaHistorico.getAccion()));
				
				//filtro por fecha
				if(referenciaHistorico.getFechaHora()!=null)
					c.add(Restrictions.eq("fechaHora", referenciaHistorico.getFechaHora()));
				
				//filtro por fecha desde 
				if(referenciaHistorico.getFechaDesde()!=null)
					c.add(Restrictions.ge("fechaHora", referenciaHistorico.getFechaDesde()));
				
				//filtro por fecha hasta
				if(referenciaHistorico.getFechaHasta()!=null)
					c.add(Restrictions.le("fechaHora", referenciaHistorico.getFechaHasta()));
				
				//filtro por clienteEmp
				if(referenciaHistorico.getCodigoCliente()!=null && !referenciaHistorico.getCodigoCliente().equals(""))
					c.add(Restrictions.eq("codigoCliente", referenciaHistorico.getCodigoCliente()));
				
				//filtro por elemento
				if(referenciaHistorico.getCodigoElemento()!=null && !referenciaHistorico.getCodigoElemento().equals(""))
					c.add(Restrictions.eq("codigoElemento", referenciaHistorico.getCodigoElemento()));
				
				//filtro por contenedor
				if(referenciaHistorico.getCodigoContenedor()!=null && !referenciaHistorico.getCodigoContenedor().equals(""))
					c.add(Restrictions.eq("codigoContenedor", referenciaHistorico.getCodigoContenedor()));
			}
        	
    		//filtro por clienteAsp
    		if(clienteAsp!=null)
    			c.add(Restrictions.eq("clienteAsp", clienteAsp));
        	
//        	//Ordenamos
        	if(referenciaHistorico.getSortOrder()!=null && referenciaHistorico.getSortOrder().length()>0 &&
        			referenciaHistorico.getFieldOrder()!=null && referenciaHistorico.getFieldOrder().length()>0){
            				
    		
    			String fieldOrdenar = "";
        		String fieldOrdenar2 = "";
        		
        		if("idReferencia".equals(referenciaHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "idReferencia";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("idLoteReferencia".equals(referenciaHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "idLoteReferencia";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("nombreCliente".equals(referenciaHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "nombreCliente";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("accion".equals(referenciaHistorico.getFieldOrder())){
					fieldOrdenar = "accion";
					fieldOrdenar2 = "fechaHora";
				}
				if("usuario".equals(referenciaHistorico.getFieldOrder())){
					c.createCriteria("usuario", "user");
        			fieldOrdenar = "user.id";
        			fieldOrdenar2 = "fechaHora";
				}
				if("fechaHora".equals(referenciaHistorico.getFieldOrder())){
					fieldOrdenar = "fechaHora";
					fieldOrdenar2 = "id";
				}
				if("nombreCliente".equals(referenciaHistorico.getFieldOrder())){
					fieldOrdenar = "nombreCliente";
					fieldOrdenar2= "fechaHora";
				}
				if("codigoElemento".equals(referenciaHistorico.getFieldOrder())){
					fieldOrdenar = "codigoElemento";
					fieldOrdenar2= "fechaHora";
				}
				if("codigoContenedor".equals(referenciaHistorico.getFieldOrder())){
					fieldOrdenar = "codigoContenedor";
					fieldOrdenar2= "fechaHora";
				}
            		
            		if("1".equals(referenciaHistorico.getSortOrder())){
            			if(!"".equals(fieldOrdenar))
        					c.addOrder(Order.asc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2)){
            				if("id".equalsIgnoreCase(fieldOrdenar2))
            					c.addOrder(Order.desc(fieldOrdenar2));
            				else
            					c.addOrder(Order.asc(fieldOrdenar2));
            			}
        			}else if("2".equals(referenciaHistorico.getSortOrder())){
        				if(!"".equals(fieldOrdenar))
        					c.addOrder(Order.desc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					c.addOrder(Order.desc(fieldOrdenar2));
        			}
            	
            	}else{
            		String fieldOrdenar = "fechaHora";
        			String fieldOrdenar2 = "id";
        			c.addOrder(Order.desc(fieldOrdenar));
        			c.addOrder(Order.desc(fieldOrdenar2));
            	}

        	//Paginamos
        	if(referenciaHistorico.getNumeroPagina()!=null && referenciaHistorico.getNumeroPagina().longValue()>0 
    				&& referenciaHistorico.getTamañoPagina()!=null && referenciaHistorico.getTamañoPagina().longValue()>0){
    			Integer paginaInicial = (referenciaHistorico.getNumeroPagina() - 1);
    			Integer filaDesde = referenciaHistorico.getTamañoPagina() * paginaInicial;
    			c.setFirstResult(filaDesde);
    			
    			c.setMaxResults(referenciaHistorico.getTamañoPagina());
    		}
        	
        	return c.list();
        	
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
	public ReferenciaHistorico obtenerReferenciaHistorico(Referencia referencia){
		Session session = null;
        try {
        		//obtenemos una sesión
    			session = getSession();
//            	Criteria c = session.createCriteria(getClaseModelo());
//            	if(referencia!=null){
//    				
//    				//filtro por referencia
//    				if(referencia.getId()!=null && !"".equals(referencia.getId()))
//    					c.add(Restrictions.eq("idReferencia", referencia.getId()));
//    			}
//            	c.addOrder(Order.desc("fechaHora"));
            	
            	String consulta = "Select r FROM ReferenciaHistorico r WHERE 1 = 1 ";
            	
            	if(referencia!=null){
    				//filtro por referencia
    				if(referencia.getId()!=null)
    					consulta += " AND r.idReferencia = "+referencia.getId().longValue()+" ";
    			}
            	
            	consulta += " ORDER BY r.fechaHora DESC";
            	
            	Query query = session.createQuery(consulta);
            	
            	if(query.list().isEmpty()){
            		return null;
            	}else{
            		return (ReferenciaHistorico) query.list().get(0);
            	}
            	
			
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
	public List<Long> obtenerCodigosReferenciaHistoricoPorUsuario(User usuario,Date fechaDesde, Date fechaHasta ){
		Session session = null;
        try {
        		//obtenemos una sesión
    			session = getSession();
            	Criteria c = session.createCriteria(getClaseModelo());
            	c.setProjection(Projections.property("idReferencia"));
            	
            	if(usuario!=null)
    				c.add(Restrictions.eq("usuario", usuario));
          
            	if(fechaDesde!=null)
            		c.add(Restrictions.ge("fechaHora", fechaDesde));
            	
            	if(fechaHasta!=null)
            		c.add(Restrictions.le("fechaHora", fechaHasta));
            	
            	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            	
            	if(c.list().isEmpty())
            	{
            		return null;
            	}
            	else
            	{
            		return (List<Long>)c.list();
            	}
            	
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
	public List<ReferenciaHistorico> obtenerReferenciaHistoricoPorUsuario(User usuario,Date fechaDesde, Date fechaHasta ){
		Session session = null;
        try {
        		//obtenemos una sesión
    			session = getSession();
            	Criteria c = session.createCriteria(getClaseModelo());
            	
            	if(usuario!=null)
    				c.add(Restrictions.eq("usuario", usuario));
          
            	if(fechaDesde!=null)
            		c.add(Restrictions.ge("fechaHora", fechaDesde));
            	
            	if(fechaHasta!=null)
            		c.add(Restrictions.le("fechaHora", fechaHasta));
            	
//            	Criterion first = Restrictions.eq("accion", "MS004REF");
//            	Criterion second = Restrictions.eq("accion", "MS006REF");
//            	Criterion third = Restrictions.eq("accion", "MS008REF");
//            	
//            	Disjunction disjunction = Restrictions.disjunction();
//                disjunction.add(first);
//                disjunction.add(second);
//                disjunction.add(third);
//                
//                c.add(disjunction);
            	
            	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            	
            	return (List<ReferenciaHistorico>)c.list();
            	
            	
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
	public List<Long> obtenerIdsReferenciasPorUsuario(User usuario,Date fechaDesde, Date fechaHasta ){
		Session session = null;
        try {
        		//obtenemos una sesión
    			session = getSession();
            	Criteria c = session.createCriteria(getClaseModelo());
            	c.setProjection(Projections.property("idReferencia"));
            	
            	if(usuario!=null)
    				c.add(Restrictions.eq("usuario", usuario));
          
            	if(fechaDesde!=null)
            		c.add(Restrictions.ge("fechaHora", fechaDesde));
            	
            	if(fechaHasta!=null)
            		c.add(Restrictions.le("fechaHora", fechaHasta));
            	
            	Criterion first = Restrictions.eq("accion", "MS004REF");
            	Criterion second = Restrictions.eq("accion", "MS006REF");
            	Criterion third = Restrictions.eq("accion", "MS008REF");
            	
            	Disjunction disjunction = Restrictions.disjunction();
                disjunction.add(first);
                disjunction.add(second);
                disjunction.add(third);
                
                c.add(disjunction);
            	
            	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            	
            	return (List<Long>)c.list();
            	
            	
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
	public List<Long> obtenerIdsReferenciasPorUsuarioEnDia(User usuario,Date fechaHora){
		Session session = null;
        try {
        		//obtenemos una sesión
    			session = getSession();
            	Criteria c = session.createCriteria(getClaseModelo());
            	c.setProjection(Projections.property("idReferencia"));
            	
            	if(usuario!=null)
    				c.add(Restrictions.eq("usuario", usuario));
          
            	if(fechaHora!=null)
            		c.add(Restrictions.eq("fechaHora", fechaHora));
            	            	
            	Criterion first = Restrictions.eq("accion", "MS004REF");
            	Criterion second = Restrictions.eq("accion", "MS006REF");
            	Criterion third = Restrictions.eq("accion", "MS008REF");
            	
            	Disjunction disjunction = Restrictions.disjunction();
                disjunction.add(first);
                disjunction.add(second);
                disjunction.add(third);
                
                c.add(disjunction);
            	
            	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            	
            	return (List<Long>)c.list();
               	
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
	public List<ReferenciaHistorico> traerReferenciasHistoricasPorSQL(User usuario,String fechaDesde, String fechaHasta){
		Session session=null;
		
		try{
			session = getSession();
           	
        	String consulta = "select distinct r.* " +
				" from referencias_historico r " +
				" where (r.accion like 'MS004REF' OR r.accion like 'MS006REF' OR r.accion like 'MS008REF') "; 
        	
        	if(usuario!= null)
        		consulta+= " AND r.usuario_id = "+ usuario.getId()+" ";
        	
        	if(fechaDesde!=null)
        		consulta+= " AND r.fechaHora >= '"+ fechaDesde+"' ";
        	
        	if(fechaHasta!=null)
        		consulta+= " AND r.fechaHora <= '"+ fechaHasta+"' ";
        		
        	SQLQuery q = session.createSQLQuery(consulta).addEntity(ReferenciaHistorico.class);			
			
			return (List<ReferenciaHistorico>)q.list();
			
		}
		catch(HibernateException hibernateException){
			logger.error("No se pudo listar",hibernateException);
			return null;
		}finally{
			try{
				session.close();
			}catch(Exception e){
				logger.error("No se pudo cerrar la sesión",e);
			}
		}
		
	}
	
	@Override
	public List<Object> traerSumasReferenciasPorSQL(User usuario,String fechaDesde, String fechaHasta, Long clienteAspId){
		Session session=null;
		
		try{
			session = getSession();
           	
//        	String consulta = "SELECT distinct count (r.id),accion" +
//				" from referencias_historico r " +
//				" where clienteAsp_id = "+clienteAspId
//				+ " and (r.accion like 'MS004REF' OR r.accion like 'MS006REF' OR r.accion like 'MS008REF') "; 
        	

        	String consulta = "         	SELECT count (*) cantidad, accion, c.codigo, c.nombre \r\n" + 
        			"        	from referencias_historico rh \r\n" + 
        			"        	inner join referencia r \r\n" + 
        			"        	on r.id = rh.idReferencia  \r\n" + 
        			"        	inner join clasificacionDocumental c \r\n" + 
        			"        	on c.id = r.clasificacion_documental_id \r\n" + 
        			"        	inner join users u \r\n" + 
        			"        	on u.id = rh.usuario_id \r\n" + 
        			"        	where " + 
        			"        	(rh.accion like 'MS004REF' OR rh.accion like 'MS006REF' OR rh.accion like 'MS008REF')  \r\n";
//        			"        	AND rh.usuario_id = 127  \r\n" + 
//        			"        	AND rh.fechaHora >= CONVERT(DATETIME,'01/09/2014 00:00:00',103)  \r\n" + 
//        			"        	AND rh.fechaHora <= CONVERT(DATETIME,'30/09/2014 23:59:59',103)  ";        	

		        	if(clienteAspId!= null)
		        		consulta+= " AND clienteAsp_Id = "+ clienteAspId + " ";
        		
                	if(usuario!= null)
                		consulta+= " AND rh.usuario_id = "+ usuario.getId()+" ";
                	
                	if(fechaDesde!=null)
                		consulta+= " AND rh.fechaHora >= CONVERT(DATETIME,'"+ fechaDesde+"',103) ";
                	
                	if(fechaHasta!=null)
                		consulta+= " AND rh.fechaHora <= CONVERT(DATETIME,'"+ fechaHasta+"',103) ";
                		
                	
                	//consulta += " GROUP BY accion ORDER BY accion asc";
        	
                	consulta += "        	GROUP BY accion, r.clasificacion_documental_id, c.codigo, c.nombre  \r\n" + 
                			"        	ORDER BY r.clasificacion_documental_id ";

        		
        	SQLQuery q = session.createSQLQuery(consulta);		
			
			return (List<Object>)q.list();
			
		}
		catch(HibernateException hibernateException){
			logger.error("No se pudo listar",hibernateException);
			return null;
		}finally{
			try{
				session.close();
			}catch(Exception e){
				logger.error("No se pudo cerrar la sesión",e);
			}
		}
		
	}
	
	@Override
	public List<Object> traerUsuariosCargaReferenciasPorSQL(String fechaDesde, String fechaHasta, Long clienteAspId){
		Session session=null;
		
		try{
			session = getSession();
           	
        	String consulta = "SELECT distinct  p.apellido, p.nombre "
        			+ " FROM [referencias_historico] rh "
        			+ " inner join users u on u.id = rh.usuario_id "
        			+ " inner join personas_fisicas p on p.id = u.persona_id "
        			+ " where clienteAsp_id = " + clienteAspId
        			+ " and (rh.accion like 'MS004REF' OR rh.accion like 'MS006REF' OR rh.accion like 'MS008REF') ";
        	
        	
        	if(fechaDesde!=null)
        		consulta+= " AND rh.fechaHora >= CONVERT(DATETIME,'"+ fechaDesde+"',103) ";
        	
        	if(fechaHasta!=null)
        		consulta+= " AND rh.fechaHora <= CONVERT(DATETIME,'"+ fechaHasta+"',103) ";
        	
        	consulta += " order by p.apellido";
        		
        	SQLQuery q = session.createSQLQuery(consulta);		
			
			return (List<Object>)q.list();
			
		}
		catch(HibernateException hibernateException){
			logger.error("No se pudo listar",hibernateException);
			return null;
		}finally{
			try{
				session.close();
			}catch(Exception e){
				logger.error("No se pudo cerrar la sesión",e);
			}
		}
		
	}
}
