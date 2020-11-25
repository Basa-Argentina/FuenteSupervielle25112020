/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 16/05/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoHistoricoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ElementoHistorico;

/**
 * @author Victor Kenis
 *
 */
@Component
public class ElementoHistoricoServiceImp extends GestorHibernate<ElementoHistorico> implements ElementoHistoricoService {
	private static Logger logger=Logger.getLogger(ElementoHistoricoServiceImp.class);
	
	@Autowired
	public ElementoHistoricoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<ElementoHistorico> getClaseModelo() {
		return ElementoHistorico.class;
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
	public List<ElementoHistorico> listarElementoHistorico(ElementoHistorico elementoHistorico, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	List<Long> ids = obtenerIDsElementoHistoricoFiltrados(elementoHistorico, clienteAsp);
        	
        	//obtenemos una sesión
			session = getSession();
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<ElementoHistorico>();
			
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.add(Restrictions.in("id", ids));

        	//Ordenamos
        	if(elementoHistorico.getSortOrder()!=null && elementoHistorico.getSortOrder().length()>0 &&
        			elementoHistorico.getFieldOrder()!=null && elementoHistorico.getFieldOrder().length()>0){
            				
    		
    			String fieldOrdenar = "";
        		String fieldOrdenar2 = "";
        		
        		if("codigoElemento".equals(elementoHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "codigoElemento";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("nombreTipoElemento".equals(elementoHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "nombreTipoElemento";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("nombreCliente".equals(elementoHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "nombreCliente";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("accion".equals(elementoHistorico.getFieldOrder())){
					fieldOrdenar = "accion";
					fieldOrdenar2 = "fechaHora";
				}
				if("usuario".equals(elementoHistorico.getFieldOrder())){
					c.createCriteria("usuario", "user");
        			fieldOrdenar = "user.id";
        			fieldOrdenar2 = "fechaHora";
				}
				if("fechaHora".equals(elementoHistorico.getFieldOrder())){
					fieldOrdenar = "fechaHora";
					fieldOrdenar2 = "id";
				}
            		
            		if("1".equals(elementoHistorico.getSortOrder())){
            			if(!"".equals(fieldOrdenar))
        					c.addOrder(Order.asc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2)){
            				if("id".equalsIgnoreCase(fieldOrdenar2))
            					c.addOrder(Order.desc(fieldOrdenar2));
            				else
            					c.addOrder(Order.asc(fieldOrdenar2));
            			}
        			}else if("2".equals(elementoHistorico.getSortOrder())){
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
	public Integer contarElementoHistoricoFiltrados(ElementoHistorico elementoHistorico, ClienteAsp clienteAsp){
		
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.setProjection(Projections.rowCount());
  			            	
    			if(elementoHistorico!=null){
    				//filtro por elemento
    				if(elementoHistorico.getCodigoContenedor()!=null && !"".equals(elementoHistorico.getCodigoContenedor()))
    					c.add(Restrictions.eq("codigoElemento", elementoHistorico.getCodigoContenedor()));
    				
    				//filtro por tipo de elemento
    				if(elementoHistorico.getCodigoTipoElemento() != null && !"".equals(elementoHistorico.getCodigoTipoElemento()))
            			c.add(Restrictions.in("codigoTipoElemento", elementoHistorico.getCodigoTipoElemento().split(",")));
            		
    				//filtro por cliente
    				if(elementoHistorico.getCodigoCliente()!=null && !"".equals(elementoHistorico.getCodigoCliente()))
    					c.add(Restrictions.or
    							(Restrictions.eq("codigoCliente", elementoHistorico.getCodigoCliente()),
    							Restrictions.isNull("codigoCliente")));
    				
    				//filtro por usuario
    				if(elementoHistorico.getCodigoUsuario()!=null && !elementoHistorico.getCodigoUsuario().equals(""))
    					c.createCriteria("usuario", "user").add(Restrictions.eq("user.id", elementoHistorico.getCodigoUsuario()));
    				
    				//filtro por accion
    				if(elementoHistorico.getAccion()!=null && !elementoHistorico.getAccion().equals(""))
    					c.add(Restrictions.eq("accion", elementoHistorico.getAccion()));
    				
    				//filtro por fecha
    				if(elementoHistorico.getFechaHora()!=null)
    					c.add(Restrictions.eq("fechaHora", elementoHistorico.getFechaHora()));
    				
    				//filtro por fecha desde 
    				if(elementoHistorico.getFechaDesde()!=null)
    					c.add(Restrictions.ge("fechaHora", elementoHistorico.getFechaDesde()));
    				
    				//filtro por fecha hasta
    				if(elementoHistorico.getFechaHasta()!=null)
    					c.add(Restrictions.le("fechaHora", elementoHistorico.getFechaHasta()));
    				    				
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
	
	private List<Long> obtenerIDsElementoHistoricoFiltrados(ElementoHistorico elementoHistorico, ClienteAsp clienteAsp)
	{
		
		Session session = null;
		List<Long> result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.setProjection(Projections.id());
        	
			if(elementoHistorico!=null){
				
				//filtro por elemento
				if(elementoHistorico.getCodigoContenedor()!=null && !"".equals(elementoHistorico.getCodigoContenedor()))	
					c.add(Restrictions.eq("codigoElemento", elementoHistorico.getCodigoContenedor()));
				
				//filtro por tipo de elemento
				if(elementoHistorico.getCodigoTipoElemento() != null && !"".equals(elementoHistorico.getCodigoTipoElemento()))
        			c.add(Restrictions.in("codigoTipoElemento", elementoHistorico.getCodigoTipoElemento().split(",")));
				
				//filtro por cliente
				if(elementoHistorico.getCodigoCliente()!=null && !"".equals(elementoHistorico.getCodigoCliente()))
					c.add(Restrictions.or
							(Restrictions.eq("codigoCliente", elementoHistorico.getCodigoCliente()),
							Restrictions.isNull("codigoCliente")));
				
				//filtro por usuario
				if(elementoHistorico.getCodigoUsuario()!=null && !elementoHistorico.getCodigoUsuario().equals(""))
					c.createCriteria("usuario", "user").add(Restrictions.eq("user.id", elementoHistorico.getCodigoUsuario()));
				
				//filtro por accion
				if(elementoHistorico.getAccion()!=null && !elementoHistorico.getAccion().equals(""))
					c.add(Restrictions.eq("accion", elementoHistorico.getAccion()));
				
				//filtro por fecha
				if(elementoHistorico.getFechaHora()!=null)
					c.add(Restrictions.eq("fechaHora", elementoHistorico.getFechaHora()));
				
				//filtro por fecha desde 
				if(elementoHistorico.getFechaDesde()!=null)
					c.add(Restrictions.ge("fechaHora", elementoHistorico.getFechaDesde()));
				
				//filtro por fecha hasta
				if(elementoHistorico.getFechaHasta()!=null)
					c.add(Restrictions.le("fechaHora", elementoHistorico.getFechaHasta()));
			}
        	
    		//filtro por clienteAsp
    		if(clienteAsp!=null)
    			c.add(Restrictions.eq("clienteAsp", clienteAsp));
        	
//        	//Ordenamos
        	if(elementoHistorico.getSortOrder()!=null && elementoHistorico.getSortOrder().length()>0 &&
        			elementoHistorico.getFieldOrder()!=null && elementoHistorico.getFieldOrder().length()>0){
            				
    		
    			String fieldOrdenar = "";
        		String fieldOrdenar2 = "";
        		
        		if("codigoElemento".equals(elementoHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "codigoElemento";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("nombreTipoElemento".equals(elementoHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "nombreTipoElemento";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("nombreCliente".equals(elementoHistorico.getFieldOrder()))
        		{
        			fieldOrdenar = "nombreCliente";
        			fieldOrdenar2 = "fechaHora";
        		}
        		if("accion".equals(elementoHistorico.getFieldOrder())){
					fieldOrdenar = "accion";
					fieldOrdenar2 = "fechaHora";
				}
				if("usuario".equals(elementoHistorico.getFieldOrder())){
					c.createCriteria("usuario", "user");
        			fieldOrdenar = "user.id";
        			fieldOrdenar2 = "fechaHora";
				}
				if("fechaHora".equals(elementoHistorico.getFieldOrder())){
					fieldOrdenar = "fechaHora";
					fieldOrdenar2 = "id";
				}
            		
            		if("1".equals(elementoHistorico.getSortOrder())){
            			if(!"".equals(fieldOrdenar))
        					c.addOrder(Order.asc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2)){
            				if("id".equalsIgnoreCase(fieldOrdenar2))
            					c.addOrder(Order.desc(fieldOrdenar2));
            				else
            					c.addOrder(Order.asc(fieldOrdenar2));
            			}
        			}else if("2".equals(elementoHistorico.getSortOrder())){
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
        	if(elementoHistorico.getNumeroPagina()!=null && elementoHistorico.getNumeroPagina().longValue()>0 
    				&& elementoHistorico.getTamañoPagina()!=null && elementoHistorico.getTamañoPagina().longValue()>0){
    			Integer paginaInicial = (elementoHistorico.getNumeroPagina() - 1);
    			Integer filaDesde = elementoHistorico.getTamañoPagina() * paginaInicial;
    			c.setFirstResult(filaDesde);
    			
    			c.setMaxResults(elementoHistorico.getTamañoPagina());
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
}
