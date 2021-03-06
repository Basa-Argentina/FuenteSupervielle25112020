/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.MovimientoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.exceptions.ClienteAspNullException;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.ElementoHistorico;
import com.security.modelo.configuraciongeneral.Empleado;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.configuraciongeneral.Movimiento;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.SecuenciaTabla;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.seguridad.User;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class MovimientoServiceImp extends GestorHibernate<Movimiento> implements MovimientoService {
	private static Logger logger=Logger.getLogger(MovimientoServiceImp.class);
	private LecturaService lecturaService;
	private LecturaDetalleService lecturaDetalleService;
	
	@Autowired
	public MovimientoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}
	@Autowired
	public void setLecturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	@Autowired
	public void setLecturaDetalleService(LecturaDetalleService lecturaDetalleService) {
		this.lecturaDetalleService = lecturaDetalleService;
	}

	@Override
	public Class<Movimiento> getClaseModelo() {
		return Movimiento.class;
	}

	@Override
	public Boolean guardarMovimiento(Movimiento Movimiento) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(Movimiento);
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
	public Boolean actualizarMovimiento(Movimiento Movimiento) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(Movimiento);
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

	@Override
	public Boolean actualizarMovimientoList(List<Movimiento> listMovimientos)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(Movimiento actualizar : listMovimientos){
    			session.update(actualizar);
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo actualizar la coleccion de Movimientos ", hibernateException);
	        return false;
        }
   }
	
	@Override
	public Boolean guardarMovimientoList(List<Movimiento> listMovimientos)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(Movimiento actualizar : listMovimientos){
    			session.save(actualizar);
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo grabar la coleccion de Movimientos ", hibernateException);
	        return false;
        }
   }
	
	@Override
	public Boolean guardarMovimientoListYActualizarPosiciones(List<Movimiento> listMovimientos,List<Posicion> listaPosiciones)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			
			if(listaPosiciones!= null && listaPosiciones.size() >0)
			{
				for(Posicion posicion : listaPosiciones){
	    			session.update(posicion);
	    			session.flush();
	    			session.clear();
				}
			}
			
        	for(Movimiento actualizar : listMovimientos){
        		session.update(actualizar.getElemento());
        		//Lineas agregadas para el historico
				registrarHistoricoElementos("MS011ELE", actualizar.getElemento(),session);
				////////////////////////////////////
    			session.save(actualizar);
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo grabar la coleccion de Movimientos ", hibernateException);
	        return false;
        }
   }
	
	
	@Override
	public Boolean actualizarMovimientoListYActualizarPosiciones(List<Movimiento> listMovimientos,List<Posicion> listaPosiciones)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			
			if(listaPosiciones!= null && listaPosiciones.size() >0)
			{
				for(Posicion posicion : listaPosiciones){
	    			session.update(posicion);
	    			session.flush();
	    			session.clear();
				}
			}
			
        	for(Movimiento actualizar : listMovimientos){
        		session.update(actualizar.getElemento());
        		//Lineas agregadas para el historico
				registrarHistoricoElementos("MS012ELE", actualizar.getElemento(),session);
				////////////////////////////////////
    			session.update(actualizar);
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo grabar la coleccion de Movimientos ", hibernateException);
	        return false;
        }
   }
	
	@Override
	public Boolean eliminarMovimiento(Movimiento Movimiento) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(Movimiento);
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
	public Movimiento verificarExistente(Movimiento Movimiento){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(Movimiento!=null){
	        	if(Movimiento.getDeposito() != null)
	        		crit.add(Restrictions.eq("deposito", Movimiento.getDeposito()));	        	
	        	if(Movimiento.getId() !=null && 0!=Movimiento.getId())
	        		crit.add(Restrictions.ilike("id", Movimiento.getId()));	      
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Movimiento> result = crit.list(); 
        	if(result.size() == 1){
        		return result.get(0);
        	}
            return null; 
        } catch (HibernateException hibernateException) {
        	logger.error("no se pudo verificar existente de Movimiento", hibernateException);
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
	public List<Movimiento> traerMovimientosPorRequerimiento(Movimiento Movimiento, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	       	
	        crit.add(Restrictions.eq("descripcionRemito", Movimiento.getDescripcionRemito()));	        	
	        crit.add(Restrictions.eq("clienteAsp", clienteAsp));	      
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
        	return crit.list(); 
        	 
        } catch (HibernateException hibernateException) {
        	logger.error("no se pudo verificar existente de Movimiento", hibernateException);
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
	public List<Movimiento> listarMovimientos(Movimiento movimiento, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	List<Long> ids = obtenerIDsMovimientosFiltrados(movimiento, clienteAsp);
        	
        	//obtenemos una sesión
			session = getSession();
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<Movimiento>();
			
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.in("id", ids));

        	//Ordenamos
        	if(movimiento.getSortOrder()!=null && movimiento.getSortOrder().length()>0 &&
        			movimiento.getFieldOrder()!=null && movimiento.getFieldOrder().length()>0){
        			
        			crit.createCriteria("elemento","ele");
        			crit.createCriteria("ele.tipoElemento","tipoE");
        			
        		
        			String fieldOrdenar = "";
            		String fieldOrdenar2 = "";
            		
            		if("tipoMovimiento".equals(movimiento.getFieldOrder()))
            		{
            			fieldOrdenar = "tipoMovimiento";
            		}
            		if("tipoElemento".equals(movimiento.getFieldOrder())){
    					fieldOrdenar = "tipoE.descripcion";
    				}
            		if("codigoElemento".equals(movimiento.getFieldOrder())){
    					fieldOrdenar = "ele.codigo";
    					fieldOrdenar2 = "fecha";
    				}
            		if("deposito".equals(movimiento.getFieldOrder())){
    					fieldOrdenar = "deposito";
    					fieldOrdenar2 = "fecha";
    				}
            		if("fecha".equals(movimiento.getFieldOrder())){
            			fieldOrdenar = "fecha";
            			fieldOrdenar2 = "id";
    				}
            		if("posicion".equals(movimiento.getFieldOrder())){
            			crit.createCriteria("posicionOrigenDestino", "pos");
            			fieldOrdenar = "pos.posVertical";
            			fieldOrdenar2 = "pos.posHorizontal";
    				}
            		
            		
            		if("1".equals(movimiento.getSortOrder())){
            			if(!"".equals(fieldOrdenar))
        					crit.addOrder(Order.asc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2)){
            				if("id".equalsIgnoreCase(fieldOrdenar2))
            					crit.addOrder(Order.desc(fieldOrdenar2));
            				else
            					crit.addOrder(Order.asc(fieldOrdenar2));
            			}
        			}else if("2".equals(movimiento.getSortOrder())){
        				if(!"".equals(fieldOrdenar))
        					crit.addOrder(Order.desc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					crit.addOrder(Order.desc(fieldOrdenar2));
        			}
            	
            	}else{
        		String fieldOrdenar = "fecha";
    			String fieldOrdenar2 = "id";
    			crit.addOrder(Order.desc(fieldOrdenar));
    			crit.addOrder(Order.desc(fieldOrdenar2));
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
	
	private List<Long> obtenerIDsMovimientosFiltrados(Movimiento movimiento, ClienteAsp clienteAsp)
	{
		
		Session session = null;
		List<Long> result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.setProjection(Projections.id());
        	
			if(movimiento!=null){
				//filtro por id
				if(movimiento.getId() != null)
					c.add(Restrictions.eq("id", movimiento.getId()));
				
				//filtro por estado
				if(movimiento.getMostrarAnulados()== null || movimiento.getMostrarAnulados()==false)	
					c.add(Restrictions.isNull("estado"));
				
				//filtro por fecha desde
				if(movimiento.getFechaDesde()!=null){
					c.add(Restrictions.ge("fecha", movimiento.getFechaDesde()));
				}
				//filtro por fecha hasta
				if(movimiento.getFechaHasta()!=null){
					c.add(Restrictions.le("fecha", movimiento.getFechaHasta()));
				}
				
				//filtro por tipoMovimiento
				if(movimiento.getTipoMovimiento()!=null && movimiento.getTipoMovimiento().length()>0){
					c.add(Restrictions.eq("tipoMovimiento", movimiento.getTipoMovimiento()));
				}
				
				//filtro por claseMovimiento
				if(movimiento.getClaseMovimiento()!=null && movimiento.getClaseMovimiento().length()>0){
					c.add(Restrictions.eq("claseMovimiento", movimiento.getClaseMovimiento()));
				}
				
				//filtro por estadoElemento
				if(movimiento.getEstadoElemento()!=null && movimiento.getEstadoElemento().length()>0){
					c.add(Restrictions.eq("estadoElemento", movimiento.getEstadoElemento()));
				}
				
				//filtro por codigo deposito actual
				if(movimiento.getCodigoDepositoActual()!=null && movimiento.getCodigoDepositoActual().length()>0){
					c.createCriteria("deposito")
						.add(Restrictions.eq("codigo", movimiento.getCodigoDepositoActual()));
				}
				
				//filtro por codigo deposito actual
				if(movimiento.getDeposito()!=null){
					c.add(Restrictions.eq("deposito", movimiento.getDeposito()));
				}
				
				//filtro por clienteAsp de elemento
				c.createCriteria("elemento", "elem").add(Restrictions.eq("clienteAsp", clienteAsp));
				
				//filtro por codigo elemento
				if(movimiento.getCodigoElemento()!=null && movimiento.getCodigoElemento().length()>0){
					c.add(Restrictions.eq("elem.codigo", movimiento.getCodigoElemento()));				
				}
				
				//filtro por codigo tipoElemento
				if(movimiento.getCodigoTipoElemento()!=null && movimiento.getCodigoTipoElemento().length()>0){
					c.createCriteria("elem.tipoElemento").add(Restrictions.eq("codigo", movimiento.getCodigoTipoElemento()));
				}
				
				//filtro por codigo clienteEmp
				if(movimiento.getCodigoClienteEmp()!=null && movimiento.getCodigoClienteEmp().length()>0){
					c.createCriteria("elem.clienteEmp").add(Restrictions.eq("codigo", movimiento.getCodigoClienteEmp()));
				}
				
				//filtro por codigo remito
				if(movimiento.getCodigoRemito()!=null)
					c.createCriteria("remito", "rem").add(Restrictions.eq("rem.id", movimiento.getCodigoRemito()));
				else if(movimiento.getRemito()!=null)
					c.add(Restrictions.eq("remito", movimiento.getRemito()));
				else if(movimiento.getTieneRemitoAsoc()!=null && movimiento.getTieneRemitoAsoc())
					c.add(Restrictions.isNotNull("remito"));
				else if(movimiento.getTieneRemitoAsoc()!=null && !movimiento.getTieneRemitoAsoc())
					c.add(Restrictions.isNull("remito"));
				
				if(movimiento.getCodigoLectura() !=null && !"".equals(movimiento.getCodigoLectura())){
        			
        			Lectura lectura = lecturaService.obtenerPorCodigo(Long.parseLong(movimiento.getCodigoLectura()), clienteAsp);
        			if(lectura!=null)
        			{
        				List<LecturaDetalle> listaDetalles = lecturaDetalleService.listarLecturaDetallePorLectura(lectura, clienteAsp);
        				if(listaDetalles!=null && listaDetalles.size()>0)
        				{
        					Disjunction disjunction = Restrictions.disjunction();
        		        	for(LecturaDetalle detalle : listaDetalles){
        		        		if(detalle.getElemento()!=null){
        		        			disjunction.add(Restrictions.eq("elem.id", detalle.getElemento().getId()));
        		        		}
        		        	}
        		        	c.add(disjunction);
        				}
        			}
        		}
			}
        	
    		//filtro por clienteAsp
    		if(clienteAsp!=null)
    			c.add(Restrictions.eq("clienteAsp", clienteAsp));
        	
//        	//Ordenamos
        	if(movimiento.getSortOrder()!=null && movimiento.getSortOrder().length()>0 &&
        			movimiento.getFieldOrder()!=null && movimiento.getFieldOrder().length()>0){
            		
    			c.createCriteria("elem.tipoElemento","tipoE");
    			
    		
    			String fieldOrdenar = "";
        		String fieldOrdenar2 = "";
        		
        		if("tipoMovimiento".equals(movimiento.getFieldOrder()))
        		{
        			fieldOrdenar = "tipoMovimiento";
        		}
        		if("tipoElemento".equals(movimiento.getFieldOrder())){
					fieldOrdenar = "tipoE.descripcion";
				}
        		if("codigoElemento".equals(movimiento.getFieldOrder())){
					fieldOrdenar = "elem.codigo";
					fieldOrdenar2 = "fecha";
				}
        		if("deposito".equals(movimiento.getFieldOrder())){
					fieldOrdenar = "deposito";
					fieldOrdenar2 = "fecha";
				}
        		if("fecha".equals(movimiento.getFieldOrder())){
        			fieldOrdenar = "fecha";
        			fieldOrdenar2 = "id";
				}
				if("posicion".equals(movimiento.getFieldOrder())){
					c.createCriteria("posicionOrigenDestino", "pos");
        			fieldOrdenar = "pos.posVertical";
        			fieldOrdenar2 = "pos.posHorizontal";
				}	
            		
            		if("1".equals(movimiento.getSortOrder())){
            			if(!"".equals(fieldOrdenar))
        					c.addOrder(Order.asc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2)){
            				if("id".equalsIgnoreCase(fieldOrdenar2))
            					c.addOrder(Order.desc(fieldOrdenar2));
            				else
            					c.addOrder(Order.asc(fieldOrdenar2));
            			}
        			}else if("2".equals(movimiento.getSortOrder())){
        				if(!"".equals(fieldOrdenar))
        					c.addOrder(Order.desc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					c.addOrder(Order.desc(fieldOrdenar2));
        			}
            	
            	}else{
            		String fieldOrdenar = "fecha";
        			String fieldOrdenar2 = "id";
        			c.addOrder(Order.desc(fieldOrdenar));
        			c.addOrder(Order.desc(fieldOrdenar2));
            	}

        	//Paginamos
        	if(movimiento.getNumeroPagina()!=null && movimiento.getNumeroPagina().longValue()>0 
    				&& movimiento.getTamañoPagina()!=null && movimiento.getTamañoPagina().longValue()>0){
    			Integer paginaInicial = (movimiento.getNumeroPagina() - 1);
    			Integer filaDesde = movimiento.getTamañoPagina() * paginaInicial;
    			c.setFirstResult(filaDesde);
    			
    			c.setMaxResults(movimiento.getTamañoPagina());
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
public List verificarMovimientosEnRemito(Movimiento movimiento,ClienteAsp clienteAsp) {
		
		Session session = null;
		
	    try {
	    	//obtenemos una sesi�n
	    	session = getSession();
	    	
	    	String consulta = "SELECT m.id,m.elemento_id "
	    			+ "FROM movimientos m "
	    			+ "where m.tipoMovimiento = '"+movimiento.getTipoMovimiento().toUpperCase()+"' "
	    			+ "and m.deposito_id = "+movimiento.getDeposito().getId()+" "
	    			+ "and m.claseMovimiento = '"+movimiento.getClaseMovimiento().toLowerCase()+"' "
	    			+ "and m.remito_id is null "
	    			+ "and m.estado is null "
	    			+ "and m.clienteAsp_id = "+clienteAsp.getId();
	            		
	            	SQLQuery q = session.createSQLQuery(consulta);

	            	return q.list(); 
	    
	    } catch (HibernateException e1) {
	    	logger.error("no se pudo listar todos ", e1);
	        return null;
	    }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesi�n", e);
        	}
        }
	}
	
	@Override
	public Integer contarMovimientosFiltrados(Movimiento movimiento, ClienteAsp clienteAsp){
		
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.setProjection(Projections.rowCount());
  			            	
    			if(movimiento!=null){
    				//filtro por id
					if(movimiento.getId() != null)
						c.add(Restrictions.eq("id", movimiento.getId()));			
					
					//filtro por estado
					if(movimiento.getMostrarAnulados()== null || movimiento.getMostrarAnulados()==false)	
						c.add(Restrictions.isNull("estado"));				
					
					//filtro por fecha desde
					if(movimiento.getFechaDesde()!=null){
						c.add(Restrictions.ge("fecha", movimiento.getFechaDesde()));
					}
					//filtro por fecha hasta
					if(movimiento.getFechaHasta()!=null){
						c.add(Restrictions.le("fecha", movimiento.getFechaHasta()));
					}
					
					//filtro por tipoMovimiento
					if(movimiento.getTipoMovimiento()!=null && movimiento.getTipoMovimiento().length()>0){
						c.add(Restrictions.eq("tipoMovimiento", movimiento.getTipoMovimiento()));
					}
					
					//filtro por claseMovimiento
					if(movimiento.getClaseMovimiento()!=null && movimiento.getClaseMovimiento().length()>0){
						c.add(Restrictions.eq("claseMovimiento", movimiento.getClaseMovimiento()));
					}
					
					//filtro por estadoElemento
					if(movimiento.getEstadoElemento()!=null && movimiento.getEstadoElemento().length()>0){
						c.add(Restrictions.eq("estadoElemento", movimiento.getEstadoElemento()));
					}
					
					//filtro por codigo deposito actual
					if(movimiento.getCodigoDepositoActual()!=null && movimiento.getCodigoDepositoActual().length()>0){
						c.createCriteria("deposito")
							.add(Restrictions.eq("codigo", movimiento.getCodigoDepositoActual()));
					}
					
					//filtro por clienteAsp de elemento
					c.createCriteria("elemento", "elem").add(Restrictions.eq("clienteAsp", clienteAsp));
					
					//filtro por codigo elemento
					if(movimiento.getCodigoElemento()!=null && movimiento.getCodigoElemento().length()>0){
						c.add(Restrictions.eq("elem.codigo", movimiento.getCodigoElemento()));				
					}
					
					//filtro por codigo tipoElemento
					if(movimiento.getCodigoTipoElemento()!=null && movimiento.getCodigoTipoElemento().length()>0){
						c.createCriteria("elem.tipoElemento")
							.add(Restrictions.eq("codigo", movimiento.getCodigoTipoElemento()));
					}
					
					//filtro por codigo clienteEmp
					if(movimiento.getCodigoClienteEmp()!=null && movimiento.getCodigoClienteEmp().length()>0){
						c.createCriteria("elem.clienteEmp")
							.add(Restrictions.eq("codigo", movimiento.getCodigoClienteEmp()));
					}
					
					//filtro por elemento
					if(movimiento.getElemento()!=null)
						c.add(Restrictions.eq("elemento", movimiento.getElemento()));
					
					
					//filtro por codigo remito
					if(movimiento.getCodigoRemito()!=null)
					{	
						c.createCriteria("remito", "rem").add(Restrictions.eq("rem.id", movimiento.getCodigoRemito()));
					}
					else if(movimiento.getTieneRemitoAsoc()!=null && movimiento.getTieneRemitoAsoc())
						c.add(Restrictions.isNotNull("remito"));
					else if(movimiento.getTieneRemitoAsoc()!=null && !movimiento.getTieneRemitoAsoc())
						c.add(Restrictions.isNull("remito"));
					
					if(movimiento.getCodigoLectura() !=null && !"".equals(movimiento.getCodigoLectura())){
	        			
	        			Lectura lectura = lecturaService.obtenerPorCodigo(Long.parseLong(movimiento.getCodigoLectura()), clienteAsp);
	        			if(lectura!=null)
	        			{
	        				List<LecturaDetalle> listaDetalles = lecturaDetalleService.listarLecturaDetallePorLectura(lectura, clienteAsp);
	        				if(listaDetalles!=null && listaDetalles.size()>0)
	        				{
	        					Disjunction disjunction = Restrictions.disjunction();
	        		        	for(LecturaDetalle detalle : listaDetalles){
	        		        		if(detalle.getElemento()!=null){
	        		        			disjunction.add(Restrictions.eq("elem.id", detalle.getElemento().getId()));
	        		        		}
	        		        	}
	        		        	c.add(disjunction);
	        				}
	        			}
	        		}
					
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
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Movimiento traerMovimientoAnterior(Movimiento movimiento,ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
	        
        	//filtro por estado
			if(movimiento.getMostrarAnulados()== null || movimiento.getMostrarAnulados()==false)	
				crit.add(Restrictions.isNull("estado"));
        	
        	//filtro por clienteAsp
        	crit.add(Restrictions.eq("clienteAsp", cliente));
        	
        	//filtro Id distinto
        	crit.add(Restrictions.ne("id", movimiento.getId()));
        	
        	//filtro por elemento
			if(movimiento.getElemento()!=null)
				crit.add(Restrictions.eq("elemento", movimiento.getElemento()));
        	
			//filtro por fecha desde
			if(movimiento.getFechaDesde()!=null){
				crit.add(Restrictions.ge("fecha", movimiento.getFechaDesde()));
			}
			//filtro por fecha hasta
			if(movimiento.getFechaHasta()!=null){
				crit.add(Restrictions.le("fecha", movimiento.getFechaHasta()));
			}
			
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	crit.addOrder(Order.desc("fecha"));
        	crit.addOrder(Order.desc("id"));
        	
        	List<Movimiento> result = crit.list();
        	if(result.size() > 0){
        		Movimiento rta = result.get(0);
      			return rta; 
        	}
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
	
	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private void registrarHistoricoElementos(String mensaje, Elemento elemento,Session session){
		ElementoHistorico elementoHis = new ElementoHistorico();
		elementoHis.setCodigoElemento(elemento.getCodigo());
		elementoHis.setAccion(mensaje);
		elementoHis.setFechaHora(new Date());
		elementoHis.setUsuario(obtenerUser());
		elementoHis.setClienteAsp(obtenerClienteAspUser());
		if(elemento.getClienteEmp()!=null){
			elementoHis.setCodigoCliente(elemento.getClienteEmp().getCodigo());
			elementoHis.setNombreCliente(elemento.getClienteEmp().getRazonSocialONombreYApellido());
		}
		elementoHis.setCodigoTipoElemento(elemento.getTipoElemento().getCodigo());
		elementoHis.setNombreTipoElemento(elemento.getTipoElemento().getDescripcion());
		session.save(elementoHis);
	}
	
}

