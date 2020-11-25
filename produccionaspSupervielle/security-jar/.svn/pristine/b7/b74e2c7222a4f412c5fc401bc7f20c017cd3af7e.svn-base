/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 29/08/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.ScrollMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.FacturaDetalle;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.utils.ParseNumberUtils;

/**
 * @author Victor Kenis
 *
 */
@Component
public class FacturaServiceImp extends GestorHibernate<Factura> implements FacturaService {
	private static Logger logger=Logger.getLogger(FacturaServiceImp.class);
	private FacturaDetalleService facturaDetalleService;
	
	@Autowired
	public FacturaServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}
	
	@Autowired
	public void setFacturaDetalleService(FacturaDetalleService facturaDetalleService) {
		this.facturaDetalleService = facturaDetalleService;
	}

	@Override
	public Class<Factura> getClaseModelo() {
		return Factura.class;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Factura> listarFacturasFiltradas(Factura factura, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.addOrder(Order.asc("fecha"));
        	if(factura!=null){
        		
        		//filtro por estado
				if(factura.getMostrarAnulados()== null || factura.getMostrarAnulados()==false){	
					crit.add(Restrictions.or(
							Restrictions.not(Restrictions.like("estado", "%ANULADO%")),
							Restrictions.isNull("estado")));
				}
        		
        		if( factura.getFechaDesde() != null && !"".equals(factura.getFechaDesde())){
        			crit.add(Restrictions.ge("fecha", factura.getFechaDesde()));
        		}
        		if( factura.getFechaHasta() != null && !"".equals(factura.getFechaHasta())){
        			crit.add(Restrictions.le("fecha", factura.getFechaHasta()));
        		}
        		if(factura.getCodigoEmpresa()!= null && factura.getCodigoEmpresa().length()>0){
        			crit.createCriteria("empresa").add(Restrictions.eq("codigo", factura.getCodigoEmpresa()));
        		}
        		if(factura.getCodigoSucursal()!=null && factura.getCodigoSucursal().length()>0){
        			crit.createCriteria("sucursal").add(Restrictions.eq("codigo",factura.getCodigoSucursal()));
        		}
        		if(factura.getCodigoCliente() != null && factura.getCodigoCliente().length()>0){
        			crit.createCriteria("clienteEmp").add(Restrictions.eq("codigo",factura.getCodigoCliente()));
        		}
        		if(factura.getIdAfipTipoComprobante()!= null && factura.getIdAfipTipoComprobante() >0){
        			crit.createCriteria("afipTipoDeComprobante").add(Restrictions.eq("id", factura.getIdAfipTipoComprobante()));
        		}
        		if(factura.getCodigoSerie()!=null && factura.getCodigoSerie().length()>0){
        			crit.createCriteria("serie").add(Restrictions.eq("codigo", factura.getCodigoSerie()));
        		}
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Factura> listarFacturasFiltradasPorAfipTipoComprobante(String tipoFacturas [], String codigoCliente, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(tipoFacturas!=null){
        		//filtro por estado
					crit.add(Restrictions.or(
							Restrictions.not(Restrictions.like("estado", "%ANULADO%")),
							Restrictions.isNull("estado")));
				crit.createCriteria("afipTipoDeComprobante").add(Restrictions.in("tipo", tipoFacturas));
        	}
        	if(codigoCliente != null && codigoCliente.length()>0){
    			crit.createCriteria("clienteEmp").add(Restrictions.eq("codigo",codigoCliente));
    		}
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
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
	
	@Override
	public Boolean eliminarFactura(Factura factura) {
		Session session = null;
		Transaction tx = null;
		List<FacturaDetalle> listaFacturaDetalle = new ArrayList<FacturaDetalle>();
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//Se traen los detalles de la factura
			listaFacturaDetalle = (List<FacturaDetalle>)facturaDetalleService.listarFacturaDetallePorFactura(factura, factura.getClienteAsp());
			if(listaFacturaDetalle != null && listaFacturaDetalle.size() > 0)
			{
				for(FacturaDetalle detalle: listaFacturaDetalle)
				{
					session.delete(detalle);
				}
			}
			//guardamos el objeto
			session.delete(factura);
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
	
	public Boolean guardarFactura (Factura factura, List<FacturaDetalle> detalles){
		Boolean result = false;
		
		Session session = null;
		Transaction tx = null;
		try {
			Serie serie = factura.getSerie();
			Long ultNum = ParseNumberUtils.parseLongCodigo(serie.getUltNroImpreso()) + 1L;
			if(ultNum.equals(factura.getNumeroComprobante())){
				serie.setUltNroImpreso(ParseNumberUtils.parseStringCodigo(ultNum, 8));
				factura.setSerie(serie);
			}
			session = getSession();
			tx = session.getTransaction();
			tx.begin();
			//guardamos la factura
			session.update(serie);
			session.save(factura);			
			tx.commit();
			session.close();
			
			session = getSession();
			//obtenemos la factura guardada
			Criteria c = session.createCriteria(getClaseModelo());
			c.add(Restrictions.eq("clienteAsp", factura.getClienteAsp()));
			c.add(Restrictions.eq("empresa", factura.getEmpresa()));
			c.add(Restrictions.eq("sucursal", factura.getSucursal()));
			c.add(Restrictions.eq("clienteEmp", factura.getClienteEmp()));
			c.add(Restrictions.eq("serie", factura.getSerie()));
			c.add(Restrictions.eq("numeroComprobante", factura.getNumeroComprobante()));
			Factura facturaGuardada = (Factura)c.uniqueResult();
			
			tx = session.getTransaction();
			tx.begin();
			if(facturaGuardada!=null){
				//actualizo el ultimo numero de la serie
				
				factura.setId(facturaGuardada.getId());
				//guardamos los detalles
				if(detalles!=null){
					for(FacturaDetalle detalle: detalles){
						detalle.setFactura(facturaGuardada);
						session.save(detalle);
					}
				}	
				tx.commit();
			}else{
				throw new RuntimeException();
			}
			result = true;
		}catch (HibernateException e) {
			rollback(tx, e, "No fue posible guardar Factura");
			result = false;
		} catch (RuntimeException e) {
			rollback(tx, e, "No fue posible guardar Factura");
			result = false;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión al guardar Factura", e);
        	}
        }
		
		return result;
	}
	
	@Override
	public synchronized Boolean actualizarFacturaYDetalles(Factura factura) {

		Session session = null;
		Transaction tx = null;
		
		try {
				
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			
			//detalles a mantener
			Collection<FacturaDetalle> detalles=new ArrayList<FacturaDetalle>(factura.getDetallesFactura());
			
			if(factura.getId()==null || factura.getId()==0){
				factura.setId(null);
				factura.getDetallesFactura().clear();
				session.save(factura);
			}else{
				//session.update(loteFacturacion);
				//borramos las detalles eliminadas
				List<FacturaDetalle> lotes = new ArrayList<FacturaDetalle>(facturaDetalleService.listarFacturaDetallePorFactura(factura, factura.getClienteAsp()));

				for(FacturaDetalle det:lotes){
					Boolean existe = false;
					if(det.getId()!=null){
						for(FacturaDetalle detMantener:detalles)
						{
							if(det.getId().equals(detMantener.getId()))
							{
								existe = true;
							}
							
						}
						if(existe==false)
						{
							session.delete(det);
						}
						//loteFacturacion.getDetalles().remove(det);
					}
				}
				factura.setDetallesFactura(null);
				session.update(factura);
				factura.setDetallesFactura(new HashSet<FacturaDetalle>());
				//rearchivos agregadas
				//detalles = CollectionUtils.subtract(detalles,loteFacturacion.getDetalles());
			}
			//guardamos las nuevas rearchivos
			for(FacturaDetalle det : detalles)
			{
				det.setFactura(factura);	
				session.saveOrUpdate(det);
				factura.getDetallesFactura().add(det);
				
			}
			//Reactualizo
			detalles=new ArrayList<FacturaDetalle>(factura.getDetallesFactura());
			for(FacturaDetalle det : detalles){
				session.update(det);
			}
			session.update(factura);
			
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

	@SuppressWarnings("unused")
	public Factura obtenerFacturaPorNumeroComprobante(ClienteAsp clienteAsp, Empresa empresa, 
			Sucursal sucursal, ClienteEmp clienteEmp, Serie serie, Long numeroComprobante){
		Factura result = null;
		
		Session session = null;
		Transaction tx = null;
		try {
			session = getSession();
			
			//obtenemos la factura guardada
			Criteria c = session.createCriteria(getClaseModelo());
			if(clienteAsp!=null){
				c.add(Restrictions.eq("clienteAsp", clienteAsp));
			}
			if(empresa!=null){
				c.add(Restrictions.eq("empresa", empresa));
			}
			if(sucursal!=null){
				c.add(Restrictions.eq("sucursal", sucursal));
			}
			if(clienteEmp!=null){
				c.add(Restrictions.eq("clienteEmp", clienteEmp));
			}
			if(serie!=null){
				c.add(Restrictions.eq("serie", serie));
			}
			if(numeroComprobante!=null){
				c.add(Restrictions.eq("numeroComprobante", numeroComprobante));
			}
			result = (Factura)c.uniqueResult();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener factura",e);
			result = null;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión al obtener la factura", e);
        	}
        }	
		return result;
	}
	
	public Boolean verificarExistenciaFactura(Serie serie, Long numeroComprobante){
		Boolean result = Boolean.FALSE;
		Session session = null;
		try {
			session = getSession();
			
			Criteria c = session.createCriteria(getClaseModelo());
			c.add(Restrictions.eq("serie", serie));
			c.add(Restrictions.eq("numeroComprobante", numeroComprobante));
			@SuppressWarnings("unchecked")
			List<Factura> lf = (List<Factura>)c.list();
			if(lf!=null && lf.size()>0){
				result = Boolean.TRUE;
			}
		} catch (HibernateException e) {
			logger.error("no se pudo obtener factura",e);
			result = Boolean.FALSE;
		}finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión al obtener la factura", e);
        	}
        }
		return result;
	}
	
	@Override
	public Boolean actualizarFactura(List<Factura> facturas)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			
        	for(Factura actualizar : facturas){
        		session.update(actualizar);
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo grabar la coleccion de Facturas ", hibernateException);
	        return false;
        }
   }


/**
 * Recupera las facturas que contienen los id indicados en la lista
 */
@SuppressWarnings("unchecked")
@Override
public List<Factura> getByIds(List<Long> ids, ClienteAsp cliente){
	Session session = null;
	List<Factura> result = null;

    try {
    	//obtenemos una sesión
		session = getSession();
    	Criteria crit = session.createCriteria(getClaseModelo());
    	crit.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
    	
    	Disjunction disjunction = Restrictions.disjunction();
    	for(Long id : ids){
    		disjunction.add(Restrictions.eq("id" , id));
    	}
    	crit.add(disjunction);
           	
    	if(cliente != null){
    		crit.add(Restrictions.eq("clienteAsp", cliente));
    	}
    	
    	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    	result = (List<Factura>)crit.list();        		
    	
    } catch (HibernateException hibernateException) {
    	logger.error("No se pudo listar ", hibernateException);
        result = null;
    }finally{
    	try{
    		session.flush();
    		session.clear();
    		session.close();
    	}catch(Exception e){
    		logger.error("No se pudo cerrar la sesión", e);
    	}
    }
    return result;
}

/**
 * Recupera las facturas que contienen los id indicados en la lista
 */
@SuppressWarnings("unchecked")
@Override
public List<Factura> getByIdsConDetalles(List<Long> ids, ClienteAsp cliente){
	Session session = null;
	List<Factura> result = null;

    try {
    	//obtenemos una sesión
		session = getSession();
    	Criteria crit = session.createCriteria(getClaseModelo());
    	crit.setCacheMode(CacheMode.IGNORE).scroll(ScrollMode.FORWARD_ONLY);
    	
    	Disjunction disjunction = Restrictions.disjunction();
    	for(Long id : ids){
    		disjunction.add(Restrictions.eq("id" , id));
    	}
    	crit.add(disjunction);
           	
    	if(cliente != null){
    		crit.add(Restrictions.eq("clienteAsp", cliente));
    	}
    	
    	crit.setFetchMode("detallesFactura", FetchMode.JOIN);
    	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    	result = (List<Factura>)crit.list();        		
    	
    } catch (HibernateException hibernateException) {
    	logger.error("No se pudo listar ", hibernateException);
        result = null;
    }finally{
    	try{
    		session.flush();
    		session.clear();
    		session.close();
    	}catch(Exception e){
    		logger.error("No se pudo cerrar la sesión", e);
    	}
    }
    return result;
}

}
