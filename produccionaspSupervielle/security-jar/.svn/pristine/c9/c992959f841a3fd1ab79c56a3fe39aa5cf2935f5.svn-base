/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PlantillaFacturacionService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.FacturaDetalle;
import com.security.modelo.configuraciongeneral.LoteFacturacion;
import com.security.modelo.configuraciongeneral.LoteFacturacionDetalle;
import com.security.modelo.configuraciongeneral.PlantillaFacturacion;
import com.security.modelo.configuraciongeneral.PlantillaFacturacionDetalle;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.utils.DateUtil;

/**
 * @author Victor Kenis
 *
 */
@Component
public class PlantillaFacturacionServiceImp extends GestorHibernate<PlantillaFacturacion> implements PlantillaFacturacionService {
	private static Logger logger=Logger.getLogger(PlantillaFacturacionServiceImp.class);
	private PlantillaFacturacionDetalleService plantillaFacturacionDetalleService;
	
	@Autowired
	public PlantillaFacturacionServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}
	
	@Autowired
	public void PlantillaFacturacionDetalleService(PlantillaFacturacionDetalleService plantillaFacturacionDetalleService) {
		this.plantillaFacturacionDetalleService = plantillaFacturacionDetalleService;
	}

	@Override
	public Class<PlantillaFacturacion> getClaseModelo() {
		return PlantillaFacturacion.class;
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
	public Boolean guardarPlantillaFacturacionYDetalles(Set<PlantillaFacturacionDetalle> plantillaFacturacionDetalles, PlantillaFacturacion plantillaFacturacion) {
		
		Session session = null;
		Transaction tx = null;
		
		try {
			
			// obtenemos una sesión
			session = getSession();
			// creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos la loteFacturacion primero
			//para que se cree el ID
			session.saveOrUpdate(plantillaFacturacion);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.

			//Recorro los interchanges para borrar los viejos
			if(plantillaFacturacion.getDetalles()!=null){
				for(PlantillaFacturacionDetalle detalleViejo:plantillaFacturacion.getDetalles()){
					try {
							Boolean existe = false;
							for(PlantillaFacturacionDetalle plantillaFacturacionDetalle:plantillaFacturacionDetalles){
								if(plantillaFacturacionDetalle.getId()!=null 
										&& plantillaFacturacionDetalle.getId().longValue() == detalleViejo.getId().longValue()){
									existe = true;
								}
							}
							if(existe == false)
								session.delete(detalleViejo);					
						
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
			
			//Recorro la lista de nuevos detalles para guardarlos o actualizarlos
			if(plantillaFacturacionDetalles!=null){
				for(PlantillaFacturacionDetalle plantillaFacturacionDetalle:plantillaFacturacionDetalles){
					plantillaFacturacionDetalle.setPlantillaFacturacion(plantillaFacturacion);
					session.saveOrUpdate(plantillaFacturacionDetalle);
				}	
			}
//			//ya con el id guardado
//			//le seteamos a la loteFacturacion la lista de detalles
//			loteFacturacion.setDetalles(loteFacturacionDetalles);
//			//tx.begin();
//			//Actualizamos la loteFacturacion con la lista de detalles
//			session.update(loteFacturacion);
			
			// Comiteo
			tx.commit();
			return true;

		} catch (RuntimeException e) {
			// si ocurre algún error intentamos hacer rollback
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
					return false;
				} catch (HibernateException e1) {
					logger.error("no se pudo hacer rollback", e1);
				}
				logger.error("no se pudo guardar", e);
				return false;
			}
			return true;

		} finally {
			try {
				session.close();
			} catch (Exception e) {
				logger.error("No se pudo cerrar la sesión", e);
			}
		}
	}
	
	@Override
	public Long contarObtenerPor(ClienteAsp cliente,
			String clienteCodigo, String codigoSerie, String listaPreciosCodigo, Long tipoComprobanteId,
			Boolean habilitado) {
		
		Session session = null;
		Long result = null;
		
	    try {
	    	//obtenemos una sesión
	    	session = getSession();
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.setProjection(Projections.rowCount());
	        
	        crit.add(Restrictions.eq("clienteAsp", cliente));
 	        
         	if(clienteCodigo!=null && !clienteCodigo.isEmpty()){
         		crit.createCriteria("clienteEmp", "cli");
         		crit.add(Restrictions.eq("cli.codigo", clienteCodigo));
         	}
         	if(codigoSerie!=null && !codigoSerie.isEmpty()){
         		crit.createCriteria("serie", "ser");
         		crit.add(Restrictions.eq("ser.codigo", codigoSerie));
         	}
         	if(listaPreciosCodigo!=null && !listaPreciosCodigo.isEmpty()){
         		crit.createCriteria("listaPrecios", "list");
         		crit.add(Restrictions.eq("list.codigo", listaPreciosCodigo));
         	}
         	if(tipoComprobanteId!=null && tipoComprobanteId!=0){
         		crit.createCriteria("afipTipoComprobante", "afip");
         		crit.add(Restrictions.eq("afip.id", tipoComprobanteId));
         	}
         	if(habilitado!=null){
         		crit.add(Restrictions.eq("habilitado", habilitado));
         	}
        	
	        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        
	        result = ((Long)crit.list().get(0));
	        return result;
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
	
	private List<Long> obtenerIDsPlantillaFacturacionFiltradas(ClienteAsp cliente,
			String clienteCodigo, String codigoSerie, String listaPreciosCodigo, Long tipoComprobanteId,
			Boolean habilitado, String fieldOrder, String sortOrder,Integer numeroPagina,Integer tamañoPagina) {
		
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.id());
        	crit.createCriteria("clienteEmp", "cli");
        	crit.createCriteria("serie", "ser");
        	crit.createCriteria("listaPrecios", "list");
        	crit.createCriteria("afipTipoComprobante", "afip");
	       
        	 crit.add(Restrictions.eq("clienteAsp", cliente));
 	        
        	 if(clienteCodigo!=null && !clienteCodigo.isEmpty()){
          		crit.add(Restrictions.eq("cli.codigo", clienteCodigo));
          	}
          	if(codigoSerie!=null && !codigoSerie.isEmpty()){
          		crit.add(Restrictions.eq("ser.codigo", codigoSerie));
          	}
          	if(listaPreciosCodigo!=null && !listaPreciosCodigo.isEmpty()){
          		crit.add(Restrictions.eq("list.codigo", listaPreciosCodigo));
          	}
          	if(tipoComprobanteId!=null && tipoComprobanteId!=0){
          		crit.add(Restrictions.eq("afip.id", tipoComprobanteId));
          	}
          	if(habilitado!=null){
          		crit.add(Restrictions.eq("habilitado", habilitado));
          	}
         	
         	//Ordenamos
        	if(sortOrder!=null && sortOrder.length()>0 &&
            	fieldOrder!=null && fieldOrder.length()>0){
            		
        			String fieldOrdenar = "";
            		String fieldOrdenar2 = "";
            		
            		if("clienteEmp".equals(fieldOrder))
            		{
            			fieldOrdenar = "cli.codigo";
            		}
            		if("serie".equals(fieldOrder)){
    					fieldOrdenar = "ser.descripcion";
    				}
            		if("afipTipoComprobante".equals(fieldOrder)){
    					fieldOrdenar = "afip.descripcion";
    				}
            		if("listaPrecios".equals(fieldOrder)){
    					fieldOrdenar = "list.descripcion";
    				}
            		if("habilitado".equals(fieldOrder)){
    					fieldOrdenar = "habilitado";
    				}
            		
            		if("1".equals(sortOrder)){
            			if(!"".equals(fieldOrdenar))
        					crit.addOrder(Order.asc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					crit.addOrder(Order.asc(fieldOrdenar2));
        			}else if("2".equals(sortOrder)){
        				if(!"".equals(fieldOrdenar))
        					crit.addOrder(Order.desc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					crit.addOrder(Order.desc(fieldOrdenar2));
        			}
            	
            	}

        	//Paginamos
        	if(numeroPagina!=null && numeroPagina.longValue()>0 
    				&& tamañoPagina!=null && tamañoPagina.longValue()>0){
    			Integer paginaInicial = (numeroPagina - 1);
    			Integer filaDesde = tamañoPagina * paginaInicial;
    			crit.setFirstResult(filaDesde);
    			
    			crit.setMaxResults(tamañoPagina);
    		}
        	
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PlantillaFacturacion> obtenerPor(ClienteAsp cliente,
			String clienteCodigo, String codigoSerie, String listaPreciosCodigo, Long tipoComprobanteId,
			Boolean habilitado, String fieldOrder, String sortOrder,Integer numeroPagina,Integer tamañoPagina) {
		Session session = null;
	    try {
	    	List<Long> ids = obtenerIDsPlantillaFacturacionFiltradas(cliente, clienteCodigo, codigoSerie, listaPreciosCodigo, tipoComprobanteId, habilitado, fieldOrder, sortOrder, numeroPagina, tamañoPagina);
	    	//obtenemos una sesión
			session = getSession();
			
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<PlantillaFacturacion>();
			
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.add(Restrictions.in("id", ids));
	        
        	crit.createCriteria("clienteEmp", "cli");
        	crit.createCriteria("serie", "ser");
        	crit.createCriteria("listaPrecios", "list");
        	crit.createCriteria("afipTipoComprobante", "afip");
	        
//        	//Ordenamos
        	if(sortOrder!=null && sortOrder.length()>0 &&
                	fieldOrder!=null && fieldOrder.length()>0){
                		
            			String fieldOrdenar = "";
                		String fieldOrdenar2 = "";
                		
                		if("clienteEmp".equals(fieldOrder))
                		{
                			fieldOrdenar = "cli.codigo";
                		}
                		if("serie".equals(fieldOrder)){
        					fieldOrdenar = "ser.descripcion";
        				}
                		if("afipTipoComprobante".equals(fieldOrder)){
        					fieldOrdenar = "afip.descripcion";
        				}
                		if("listaPrecios".equals(fieldOrder)){
        					fieldOrdenar = "list.descripcion";
        				}
                		if("habilitado".equals(fieldOrder)){
        					fieldOrdenar = "habilitado";
        				}
                		
                		if("1".equals(sortOrder)){
                			if(!"".equals(fieldOrdenar))
            					crit.addOrder(Order.asc(fieldOrdenar));
                			if(!"".equals(fieldOrdenar2))
            					crit.addOrder(Order.asc(fieldOrdenar2));
            			}else if("2".equals(sortOrder)){
            				if(!"".equals(fieldOrdenar))
            					crit.addOrder(Order.desc(fieldOrdenar));
                			if(!"".equals(fieldOrdenar2))
            					crit.addOrder(Order.desc(fieldOrdenar2));
            			}
                	
               }
        		
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        return crit.list();
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
	public Boolean eliminarPlantillaFacturacion(PlantillaFacturacion plantillaFacturacion) {
		Session session = null;
		Transaction tx = null;
		List<PlantillaFacturacionDetalle> listaPlantillaFacturacionDetalle = new ArrayList<PlantillaFacturacionDetalle>();
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//Se traen los detalles de la factura
			listaPlantillaFacturacionDetalle = (List<PlantillaFacturacionDetalle>)plantillaFacturacionDetalleService.listarPlantillaDetallesPorPlantilla(plantillaFacturacion.getId(), plantillaFacturacion.getClienteAsp());
			if(listaPlantillaFacturacionDetalle != null && listaPlantillaFacturacionDetalle.size() > 0)
			{
				for(PlantillaFacturacionDetalle detalle: listaPlantillaFacturacionDetalle)
				{
					session.delete(detalle);
				}
			}
			//guardamos el objeto
			session.delete(plantillaFacturacion);
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

	@Override
	public List<PlantillaFacturacion> listarPlantillasClientesEnPeriodo(String periodo,String codigoEmpresa,Boolean habilitado, ClienteAsp cliente) {
		Session session = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
               	
        	String consulta = "SELECT pla.* "+
        	" From plantillasFacturacion AS pla " +
            " LEFT OUTER JOIN clientesEmp AS cli ON pla.clienteEmp_id = cli.id "+
            " LEFT OUTER JOIN empresas AS emp ON cli.empresa_id = emp.id "+
            " WHERE (',' + cli.mesesFacturables + ',' LIKE '%,"+periodo+",%') AND (pla.clienteAsp_id = "+cliente.getId()+") AND (emp.codigo = "+codigoEmpresa+") AND (pla.habilitado = 1) ";

        	SQLQuery q = session.createSQLQuery(consulta).addEntity(PlantillaFacturacion.class);			
			
			return (List<PlantillaFacturacion>)q.list();
			
		}catch(Exception e){
			logger.error("no se pudo listar",e);
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
