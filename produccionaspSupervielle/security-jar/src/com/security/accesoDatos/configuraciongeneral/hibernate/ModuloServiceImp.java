/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.ModuloService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.Movimiento;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class ModuloServiceImp extends GestorHibernate<Modulo> implements ModuloService {
	private static Logger logger=Logger.getLogger(ModuloServiceImp.class);
	
	@Autowired
	public ModuloServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Modulo> getClaseModelo() {
		return Modulo.class;
	}

	@Override
	public Boolean guardarModulo(Modulo modulo) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(modulo);
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
	public Boolean actualizarModulo(Modulo modulo) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(modulo);
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
	public Boolean eliminarModulo(Modulo modulo) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(modulo);
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
	public Modulo getByCodigo(String codigo, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("codigo", codigo));
        	if(cliente != null)
        		crit.createCriteria("estante").createCriteria("grupo").createCriteria("seccion").
        			createCriteria("deposito").createCriteria("sucursal").
        			createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
            return (Modulo) crit.uniqueResult();
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
	public List<Modulo> listarModuloFiltradas(Modulo modulo, ClienteAsp cliente, Boolean impresion){
		Session session = null;
        try {
        	List<Long> ids = obtenerIDsModulosFiltrados(modulo, cliente,impresion);
        	
        	//obtenemos una sesión
			session = getSession();
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<Modulo>();
			
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.in("id", ids));
        	
        	crit.createCriteria("estante", "est");
        	crit.createCriteria("est.grupo", "grp");
        	crit.createCriteria("grp.seccion", "sec");
        	crit.createCriteria("sec.deposito", "dep");

        	//Ordenamos
        	if(modulo.getSortOrder()!=null && modulo.getSortOrder().length()>0 &&
        			modulo.getFieldOrder()!=null && modulo.getFieldOrder().length()>0){
            		
    			String fieldOrdenar = "";
        		String fieldOrdenar2 = "";
        		
        		if("deposito".equals(modulo.getFieldOrder())){
        			fieldOrdenar = "dep.codigo";
        		}
        		if("seccion".equals(modulo.getFieldOrder())){
					fieldOrdenar = "sec.codigo";
				}
        		if("estante".equals(modulo.getFieldOrder())){
					fieldOrdenar = "est.codigo";
				}
        		if("modulo".equals(modulo.getFieldOrder())){
					fieldOrdenar = "posVertical";
					fieldOrdenar2 = "posHorizontal";
				}
            		
            		if("1".equals(modulo.getSortOrder())){
            			if(!"".equals(fieldOrdenar))
        					crit.addOrder(Order.asc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					crit.addOrder(Order.asc(fieldOrdenar2));
        			}else if("2".equals(modulo.getSortOrder())){
        				if(!"".equals(fieldOrdenar))
        					crit.addOrder(Order.desc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					crit.addOrder(Order.desc(fieldOrdenar2));
        			}
            	
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
	/**
	 * 
	 * @param codigoEstante
	 * @param offsetV
	 * @param offsetH
	 * @param clienteAsp
	 * @return El modulo especificado, devuelve null si no encuentra nada o si clienteAsp es nulo
	 */
	public Modulo getModuloByOffset(String codigoEstante, Integer offsetV, Integer offsetH, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("estante", "est");
        	crit.createCriteria("est.grupo", "grp");
        	crit.createCriteria("grp.seccion", "sec");
        	crit.createCriteria("sec.deposito", "dep");
        	crit.add(Restrictions.eq("est.codigo", codigoEstante));
        	crit.add(Restrictions.eq("offsetVertical", offsetV));
        	crit.add(Restrictions.eq("offsetHorizontal", offsetH));
        	if(clienteAsp != null){
        		if(clienteAsp != null){
            		crit.createCriteria("dep.sucursal").
            			createCriteria("empresa").add(Restrictions.eq("cliente", clienteAsp));
            	}
        	}
            return (Modulo) crit.uniqueResult();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pud o cerrar la sesión", e);
        	}
        }
	}
	
	private List<Long> obtenerIDsModulosFiltrados(Modulo modulo, ClienteAsp cliente,Boolean impresion)
	{
		Session session = null;
		List<Long> result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.id());
  			
        	Criteria crit2 = crit.createCriteria("estante", "est",CriteriaSpecification.LEFT_JOIN);
        	
        	crit.createCriteria("est.grupo", "grp");
        	crit.createCriteria("grp.seccion", "sec");
        	crit.createCriteria("sec.deposito", "dep");
        	if(modulo!=null){
        		if(modulo.getIdEstante() !=null)
	        		crit.add(Restrictions.eq("est.id", modulo.getIdEstante()));	        	
	        	if(modulo.getCodigo() !=null && !"".equals(modulo.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", modulo.getCodigo() + "%"));
				if (modulo.getCodigoEstante()!= null && !"".equals(modulo.getCodigoEstante())) {
					crit.add(Restrictions.eq("est.codigo", modulo.getCodigoEstante()));
				}
				if (modulo.getCodigoGrupo() != null && !"".equals(modulo.getCodigoGrupo())){
					crit.add(Restrictions.eq("grp.codigo", modulo.getCodigoGrupo()));
				}
				if(modulo.getCodigoSeccion() != null && !"".equals(modulo.getCodigoSeccion())){
					crit.add(Restrictions.eq("sec.codigo", modulo.getCodigoSeccion()));
				}
				if(modulo.getCodigoDesdeEstante() !=null && !"".equals(modulo.getCodigoDesdeEstante())){
					crit2.add(Restrictions.sqlRestriction(" CAST({alias}.codigo AS int) >= "+modulo.getCodigoDesdeEstante()+""));
				}
	        	if(modulo.getCodigoHastaEstante() !=null && !"".equals(modulo.getCodigoHastaEstante())){
	        		crit2.add(Restrictions.sqlRestriction(" CAST({alias}.codigo AS int) <= "+modulo.getCodigoHastaEstante()+""));
	        	}
				if (modulo.getCodigoDeposito() != null && !"".equals(modulo.getCodigoDeposito())){
					crit.add(Restrictions.eq("dep.codigo", modulo.getCodigoDeposito()));					
				}
				if(modulo.getPosDesdeHorModulo() !=null){
	        		crit.add(Restrictions.ge("mod.offsetHorizontal", modulo.getPosDesdeHorModulo()));
				}
	        	if(modulo.getPosHastaHorModulo() !=null){
	        		crit.add(Restrictions.le("mod.offsetHorizontal", modulo.getPosHastaHorModulo()));
	        	}
	        	if(modulo.getPosDesdeVertModulo() !=null){
	        		crit.add(Restrictions.ge("mod.offsetVertical", modulo.getPosDesdeVertModulo()));
	        	}
	        	if(modulo.getPosHastaVertModulo() !=null){
	        		crit.add(Restrictions.le("mod.offsetVertical", modulo.getPosHastaVertModulo()));
	        	}
        	}
        	if(cliente != null){
        		crit.createCriteria("dep.sucursal").
        			createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
        	}
        	
        	if(impresion == null || impresion == false){
	//        	//Ordenamos
	        	if(modulo.getSortOrder()!=null && modulo.getSortOrder().length()>0 &&
	        			modulo.getFieldOrder()!=null && modulo.getFieldOrder().length()>0){
	            		
	    			String fieldOrdenar = "";
	        		String fieldOrdenar2 = "";
	        		
	        		if("deposito".equals(modulo.getFieldOrder())){
	        			fieldOrdenar = "dep.codigo";
	        		}
	        		if("seccion".equals(modulo.getFieldOrder())){
						fieldOrdenar = "sec.codigo";
					}
	        		if("estante".equals(modulo.getFieldOrder())){
						fieldOrdenar = "est.codigo";
					}
	        		if("modulo".equals(modulo.getFieldOrder())){
						fieldOrdenar = "posVertical";
						fieldOrdenar2 = "posHorizontal";
					}
	            		
	            		if("1".equals(modulo.getSortOrder())){
	            			if(!"".equals(fieldOrdenar))
	        					crit.addOrder(Order.asc(fieldOrdenar));
	            			if(!"".equals(fieldOrdenar2))
	        					crit.addOrder(Order.asc(fieldOrdenar2));
	        			}else if("2".equals(modulo.getSortOrder())){
	        				if(!"".equals(fieldOrdenar))
	        					crit.addOrder(Order.desc(fieldOrdenar));
	            			if(!"".equals(fieldOrdenar2))
	        					crit.addOrder(Order.desc(fieldOrdenar2));
	        			}
	            	
	            	}
	
	        	//Paginamos
	        	if(modulo.getNumeroPagina()!=null && modulo.getNumeroPagina().longValue()>0 
	    				&& modulo.getTamañoPagina()!=null && modulo.getTamañoPagina().longValue()>0){
	    			Integer paginaInicial = (modulo.getNumeroPagina() - 1);
	    			Integer filaDesde = modulo.getTamañoPagina() * paginaInicial;
	    			crit.setFirstResult(filaDesde);
	    			
	    			crit.setMaxResults(modulo.getTamañoPagina());
	    		}
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
	
	@Override
	public Integer contarModulosFiltrados(Modulo modulo, ClienteAsp cliente){
		
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.rowCount());
  			
        	Criteria crit2 = crit.createCriteria("estante", "est",CriteriaSpecification.LEFT_JOIN);
        	
        	crit.createCriteria("est.grupo", "grp");
        	crit.createCriteria("grp.seccion", "sec");
        	crit.createCriteria("sec.deposito", "dep");
        	if(modulo!=null){
        		if(modulo.getIdEstante() !=null)
	        		crit.add(Restrictions.eq("est.id", modulo.getIdEstante()));	        	
	        	if(modulo.getCodigo() !=null && !"".equals(modulo.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", modulo.getCodigo() + "%"));
				if (modulo.getCodigoEstante()!= null && !"".equals(modulo.getCodigoEstante())) {
					crit.add(Restrictions.eq("est.codigo", modulo.getCodigoEstante()));
				}
				if (modulo.getCodigoGrupo() != null && !"".equals(modulo.getCodigoGrupo())){
					crit.add(Restrictions.eq("grp.codigo", modulo.getCodigoGrupo()));
				}
				if(modulo.getCodigoSeccion() != null && !"".equals(modulo.getCodigoSeccion())){
					crit.add(Restrictions.eq("sec.codigo", modulo.getCodigoSeccion()));
				}
				if(modulo.getCodigoDesdeEstante() !=null && !"".equals(modulo.getCodigoDesdeEstante())){
					crit2.add(Restrictions.sqlRestriction(" CAST({alias}.codigo AS int) >= "+modulo.getCodigoDesdeEstante()+""));
				}
	        	if(modulo.getCodigoHastaEstante() !=null && !"".equals(modulo.getCodigoHastaEstante())){
	        		crit2.add(Restrictions.sqlRestriction(" CAST({alias}.codigo AS int) <= "+modulo.getCodigoHastaEstante()+""));
	        	}
				if (modulo.getCodigoDeposito() != null && !"".equals(modulo.getCodigoDeposito())){
					crit.add(Restrictions.eq("dep.codigo", modulo.getCodigoDeposito()));					
				}
				if(modulo.getPosDesdeHorModulo() !=null){
	        		crit.add(Restrictions.ge("mod.offsetHorizontal", modulo.getPosDesdeHorModulo()));
				}
	        	if(modulo.getPosHastaHorModulo() !=null){
	        		crit.add(Restrictions.le("mod.offsetHorizontal", modulo.getPosHastaHorModulo()));
	        	}
	        	if(modulo.getPosDesdeVertModulo() !=null){
	        		crit.add(Restrictions.ge("mod.offsetVertical", modulo.getPosDesdeVertModulo()));
	        	}
	        	if(modulo.getPosHastaVertModulo() !=null){
	        		crit.add(Restrictions.le("mod.offsetVertical", modulo.getPosHastaVertModulo()));
	        	}
        	}
        	if(cliente != null){
        		crit.createCriteria("dep.sucursal").
        			createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
        	}
//        	crit.addOrder(Order.asc("dep.codigo"));
//			crit.addOrder(Order.asc("sec.codigo"));
//			crit.addOrder(Order.asc("est.codigo"));
//			crit.addOrder(Order.asc("offsetVertical"));
//			crit.addOrder(Order.asc("offsetHorizontal"));
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	        	
        	result = ((Integer)crit.list().get(0));
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
	
}
