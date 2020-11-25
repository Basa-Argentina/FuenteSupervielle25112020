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
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.PosicionService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.utils.Constantes;


/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class PosicionServiceImp extends GestorHibernate<Posicion> implements PosicionService {
	private static Logger logger=Logger.getLogger(PosicionServiceImp.class);
	
	@Autowired
	public PosicionServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}
	
	@Override
	public Class<Posicion> getClaseModelo() {
		return Posicion.class;
	}
	
	

	@Override
	public Boolean guardarPosicion(Posicion posicion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(posicion);
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
	public Boolean actualizarPosicion(Posicion posicion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(posicion);
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


	public Boolean actualizarPosicionList(List<Posicion> listPosiciones)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(Posicion actualizar : listPosiciones){        		
    			session.update(actualizar);
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo actualizar la coleccion de posiciones ", hibernateException);
	        return false;
        }
   }
	
	@Override
	public Boolean eliminarPosicion(Posicion posicion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(posicion);
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
	public Posicion getByCodigo(String codigo, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("modulo", "mod");
        	crit.createCriteria("estante", "est");
        	crit.createCriteria("est.grupo", "grp");
        	crit.createCriteria("grp.seccion", "sec");
        	crit.createCriteria("sec.deposito", "dep");
        	if(codigo != null && !"".equals(codigo)){
        		crit.add(Restrictions.eq("codigo", codigo));
        	}
        	
        	if(cliente != null)
        		crit.createCriteria("dep.sucursal").createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
            return (Posicion) crit.uniqueResult();
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
	public List<Posicion> listarPosicionFiltradas(Posicion posicion, ClienteAsp cliente){
		return listarPosicionFiltradas(posicion,cliente,null);
	}
	
	@Override
	public List<Posicion> listarPosicionFiltradas(Posicion posicion, ClienteAsp cliente,String estado){
		Session session = null;
        try {
        	List<Long> ids = obtenerIDsPosicionesFiltradas(posicion, cliente, estado);
        	
        	//obtenemos una sesión
			session = getSession();
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<Posicion>();
			
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.in("id", ids));

        	//Ordenamos
        	if(posicion.getSortOrder()!=null && posicion.getSortOrder().length()>0 &&
        			posicion.getFieldOrder()!=null && posicion.getFieldOrder().length()>0){
        			
        			crit.createCriteria("modulo", "modu");
        			crit.createCriteria("estante", "esta");
        			crit.createCriteria("esta.grupo", "grpo");
        			crit.createCriteria("grpo.seccion", "secc");
        			crit.createCriteria("secc.deposito", "depo");
            		
        			String fieldOrdenar = "";
            		String fieldOrdenar2 = "";
            		
            		if("deposito".equals(posicion.getFieldOrder()))
            		{
            			fieldOrdenar = "depo.codigo";
            		}
            		if("seccion".equals(posicion.getFieldOrder())){
    					fieldOrdenar = "secc.codigo";
    				}
            		if("estante".equals(posicion.getFieldOrder())){
    					fieldOrdenar = "esta.codigo";
    				}
            		if("estado".equals(posicion.getFieldOrder())){
    					fieldOrdenar = "estado";
    				}
            		if("posicion".equals(posicion.getFieldOrder())){
    					fieldOrdenar = "posVertical";
    					fieldOrdenar2 = "posHorizontal";
    				}
            		
            		
            		if("1".equals(posicion.getSortOrder())){
            			if(!"".equals(fieldOrdenar))
        					crit.addOrder(Order.asc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					crit.addOrder(Order.asc(fieldOrdenar2));
        			}else if("2".equals(posicion.getSortOrder())){
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
	
	private List<Long> obtenerIDsPosicionesFiltradas(Posicion posicion, ClienteAsp cliente,String estado)
	{
		
		Session session = null;
		List<Long> result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.id());
        	
			crit.createCriteria("modulo", "mod");
			Criteria crit2 = crit.createCriteria("estante", "est",CriteriaSpecification.LEFT_JOIN);
			//crit.createCriteria("estante", "est");
			crit.createCriteria("est.grupo", "grp");
			crit.createCriteria("grp.seccion", "sec");
			crit.createCriteria("sec.deposito", "dep");
        	if(posicion!=null){
        		if(posicion.getCodigoDesde() != null && !"".equals(posicion.getCodigoDesde()))
        			crit.add(Restrictions.ge("codigo",posicion.getCodigoDesde()));
        		if(posicion.getCodigoHasta() != null && !"".equals(posicion.getCodigoHasta()))
    				crit.add(Restrictions.le("codigo",posicion.getCodigoHasta()));
        		if(posicion.getCodigoDeposito() !=null && !"".equals(posicion.getCodigoDeposito()))
	        		crit.add(Restrictions.eq("dep.codigo", posicion.getCodigoDeposito()));
        		if(posicion.getCodigoSeccion() !=null && !"".equals(posicion.getCodigoSeccion()))
	        		crit.add(Restrictions.eq("sec.codigo", posicion.getCodigoSeccion()));
	        	if(posicion.getCodigoDesdeEstante() !=null && !"".equals(posicion.getCodigoDesdeEstante()))
	        		crit2.add(Restrictions.sqlRestriction(" CAST({alias}.codigo AS int) >= "+posicion.getCodigoDesdeEstante()+""));
	        		//crit.add(Restrictions.ge("est.codigo", posicion.getCodigoDesdeEstante()));
	        	if(posicion.getCodigoHastaEstante() !=null && !"".equals(posicion.getCodigoHastaEstante()))
	        		crit2.add(Restrictions.sqlRestriction(" CAST({alias}.codigo AS int) <= "+posicion.getCodigoHastaEstante()+""));
	        		//crit.add(Restrictions.le("est.codigo", posicion.getCodigoHastaEstante()));
	        	if(posicion.getCodigoDesdeModulo() !=null && !"".equals(posicion.getCodigoDesdeModulo()))
	        		crit.add(Restrictions.ge("mod.codigo", posicion.getCodigoDesdeModulo()));
	        	if(posicion.getCodigoHastaModulo() !=null && !"".equals(posicion.getCodigoHastaModulo()))
	        		crit.add(Restrictions.le("mod.codigo", posicion.getCodigoHastaModulo()));
	        	if(posicion.getPosDesdeHorModulo() !=null)
	        		crit.add(Restrictions.ge("mod.posHorizontal", posicion.getPosDesdeHorModulo()));
	        	if(posicion.getPosHastaHorModulo() !=null)
	        		crit.add(Restrictions.le("mod.posHorizontal", posicion.getPosHastaHorModulo()));
	        	if(posicion.getPosDesdeVertModulo() !=null)
	        		crit.add(Restrictions.ge("mod.posVertical", posicion.getPosDesdeVertModulo()));
	        	if(posicion.getPosHastaVertModulo() !=null)
	        		crit.add(Restrictions.le("mod.posVertical", posicion.getPosHastaVertModulo()));
	        	if(posicion.getPosDesdeHor() !=null)
	        		crit.add(Restrictions.ge("posHorizontal", posicion.getPosDesdeHor()));
	        	if(posicion.getPosHastaHor() !=null)
	        		crit.add(Restrictions.le("posHorizontal", posicion.getPosHastaHor()));
	        	if(posicion.getPosDesdeVert() !=null)
	        		crit.add(Restrictions.ge("posVertical", posicion.getPosDesdeVert()));
	        	if(posicion.getPosHastaVert() !=null)
	        		crit.add(Restrictions.le("posVertical", posicion.getPosHastaVert()));
	        	if(posicion.getEstado() != null && !"".equals(posicion.getEstado()))
	        		crit.add(Restrictions.eq("estado", posicion.getEstado()));
        	}
        	
        	if(cliente != null)
        		crit.createCriteria("dep.sucursal").createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
        	
        	
//        	//Ordenamos
        	if(posicion.getSortOrder()!=null && posicion.getSortOrder().length()>0 &&
        			posicion.getFieldOrder()!=null && posicion.getFieldOrder().length()>0){
            		
        			String fieldOrdenar = "";
            		String fieldOrdenar2 = "";
            		
            		if("deposito".equals(posicion.getFieldOrder()))
            		{
            			fieldOrdenar = "dep.codigo";
            		}
            		if("seccion".equals(posicion.getFieldOrder())){
    					fieldOrdenar = "sec.codigo";
    				}
            		if("estante".equals(posicion.getFieldOrder())){
    					fieldOrdenar = "est.codigo";
    				}
            		if("estado".equals(posicion.getFieldOrder())){
    					fieldOrdenar = "estado";
    				}
            		if("posicion".equals(posicion.getFieldOrder())){
    					fieldOrdenar = "posVertical";
    					fieldOrdenar2 = "posHorizontal";
    				}
            		
            		
            		if("1".equals(posicion.getSortOrder())){
            			if(!"".equals(fieldOrdenar))
        					crit.addOrder(Order.asc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					crit.addOrder(Order.asc(fieldOrdenar2));
        			}else if("2".equals(posicion.getSortOrder())){
        				if(!"".equals(fieldOrdenar))
        					crit.addOrder(Order.desc(fieldOrdenar));
            			if(!"".equals(fieldOrdenar2))
        					crit.addOrder(Order.desc(fieldOrdenar2));
        			}
            	
            	}

        	//Paginamos
        	if(posicion.getNumeroPagina()!=null && posicion.getNumeroPagina().longValue()>0 
    				&& posicion.getTamañoPagina()!=null && posicion.getTamañoPagina().longValue()>0){
    			Integer paginaInicial = (posicion.getNumeroPagina() - 1);
    			Integer filaDesde = posicion.getTamañoPagina() * paginaInicial;
    			crit.setFirstResult(filaDesde);
    			
    			crit.setMaxResults(posicion.getTamañoPagina());
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
	public Integer contarPosicionFiltradas(Posicion posicion, ClienteAsp cliente){
		
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.rowCount());

        	crit.createCriteria("modulo", "mod");
        	//crit.createCriteria("estante", "est");
        	Criteria crit2 = crit.createCriteria("estante", "est",CriteriaSpecification.LEFT_JOIN);
        	crit.createCriteria("est.grupo", "grp");
        	crit.createCriteria("grp.seccion", "sec");
        	crit.createCriteria("sec.deposito", "dep");
        	if(posicion!=null){
        		if(posicion.getCodigoDesde() != null && !"".equals(posicion.getCodigoDesde()))
        			crit.add(Restrictions.ge("codigo",posicion.getCodigoDesde()));
        		if(posicion.getCodigoHasta() != null && !"".equals(posicion.getCodigoHasta()))
    				crit.add(Restrictions.le("codigo",posicion.getCodigoHasta()));
        		if(posicion.getCodigoDeposito() !=null && !"".equals(posicion.getCodigoDeposito()))
	        		crit.add(Restrictions.eq("dep.codigo", posicion.getCodigoDeposito()));
        		if(posicion.getCodigoSeccion() !=null && !"".equals(posicion.getCodigoSeccion()))
	        		crit.add(Restrictions.eq("sec.codigo", posicion.getCodigoSeccion()));
	        	if(posicion.getCodigoDesdeEstante() !=null && !"".equals(posicion.getCodigoDesdeEstante()))
	        		crit2.add(Restrictions.sqlRestriction(" CAST({alias}.codigo AS int) >= "+posicion.getCodigoDesdeEstante()+""));
	        		//crit.add(Restrictions.ge("est.codigo", posicion.getCodigoDesdeEstante()));
	        	if(posicion.getCodigoHastaEstante() !=null && !"".equals(posicion.getCodigoHastaEstante()))
	        		crit2.add(Restrictions.sqlRestriction(" CAST({alias}.codigo AS int) <= "+posicion.getCodigoHastaEstante()+""));
	        		//crit.add(Restrictions.le("est.codigo", posicion.getCodigoHastaEstante()));
	        	if(posicion.getCodigoDesdeModulo() !=null && !"".equals(posicion.getCodigoDesdeModulo()))
	        		crit.add(Restrictions.ge("mod.codigo", posicion.getCodigoDesdeModulo()));
	        	if(posicion.getCodigoHastaModulo() !=null && !"".equals(posicion.getCodigoHastaModulo()))
	        		crit.add(Restrictions.le("mod.codigo", posicion.getCodigoHastaModulo()));
	        	if(posicion.getPosDesdeHorModulo() !=null)
	        		crit.add(Restrictions.ge("mod.posHorizontal", posicion.getPosDesdeHorModulo()));
	        	if(posicion.getPosHastaHorModulo() !=null)
	        		crit.add(Restrictions.le("mod.posHorizontal", posicion.getPosHastaHorModulo()));
	        	if(posicion.getPosDesdeVertModulo() !=null)
	        		crit.add(Restrictions.ge("mod.posVertical", posicion.getPosDesdeVertModulo()));
	        	if(posicion.getPosHastaVertModulo() !=null)
	        		crit.add(Restrictions.le("mod.posVertical", posicion.getPosHastaVertModulo()));
	        	if(posicion.getPosDesdeHor() !=null)
	        		crit.add(Restrictions.ge("posHorizontal", posicion.getPosDesdeHor()));
	        	if(posicion.getPosHastaHor() !=null)
	        		crit.add(Restrictions.le("posHorizontal", posicion.getPosHastaHor()));
	        	if(posicion.getPosDesdeVert() !=null)
	        		crit.add(Restrictions.ge("posVertical", posicion.getPosDesdeVert()));
	        	if(posicion.getPosHastaVert() !=null)
	        		crit.add(Restrictions.le("posVertical", posicion.getPosHastaVert()));
	        	if(posicion.getEstado() != null && !"".equals(posicion.getEstado()))
	        		crit.add(Restrictions.eq("estado", posicion.getEstado()));
        	}
        	
        	if(cliente != null)
        		crit.createCriteria("dep.sucursal").createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
        	
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
	
	@Override
	public Boolean verificarEstadoPosiciones(List<Posicion> posiciones, ClienteAsp cliente,String estado){
		List<Posicion> posicionesEncontradas = null;
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("modulo", "mod");
        	crit.createCriteria("estante", "est");
        	crit.createCriteria("est.grupo", "grp");
        	crit.createCriteria("grp.seccion", "sec");
        	crit.createCriteria("sec.deposito", "dep");
        	crit.createCriteria("dep.sucursal").createCriteria("empresa").add(Restrictions.eq("cliente", cliente));
        	Disjunction disjunction = Restrictions.disjunction();
        	for(Posicion posicion : posiciones){
        		disjunction.add(Restrictions.eq("id", posicion.getId()));
        	}
        	crit.add(disjunction);
        	crit.add(Restrictions.ne("estado", estado));
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	posicionesEncontradas = crit.list();
        	if(posicionesEncontradas.size()>0)
        	{
        		return true;
        	}
        	else
        	{
        		return false;
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
	
	@SuppressWarnings("unchecked")
	public List<Posicion> getPosicionesPorModuloParaReposicionamiento(Modulo modulo, ClienteAsp clienteAsp){
		List<Posicion> posiciones = null;
		Session session = null;
		try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("modulo", modulo));
        	
        	if(clienteAsp != null){
        		crit.createCriteria("modulo", "mod");
        		crit.createCriteria("mod.estante", "est");
            	crit.createCriteria("est.grupo", "grp");
            	crit.createCriteria("grp.seccion", "sec");
            	crit.createCriteria("sec.deposito", "dep");
        		crit.createCriteria("dep.sucursal").createCriteria("empresa").add(Restrictions.eq("cliente", clienteAsp));
        	}
        	crit.addOrder(Order.asc("posVertical"));
        	crit.addOrder(Order.asc("posHorizontal"));
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            posiciones =   crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        posiciones = new ArrayList<Posicion>();
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }		
		return posiciones;
	}
	
	@Override
	public List<Posicion> traerPosicionesPorSQL(Posicion posicion,ClienteAsp clienteAsp){
		Session session=null;
		
		try{
			session = getSession();
           	
        	String consulta = "select distinct p.* " +
				" from posiciones p " +
				" left join estanterias e on p.estante_id = e.id " + 
				" left join modulos m on p.modulo_id = m.id " +
				" where 1=1 "; 
        	
        	if(posicion.getEstante()!= null)
        		consulta+= " AND p.estante_id = "+ posicion.getEstante().getId()+" ";
        	
        	
        	
        	SQLQuery q = session.createSQLQuery(consulta).addEntity(Posicion.class);			
			
			return (List<Posicion>)q.list();
			
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
	public List<Posicion> traerPosicionesLibresPorSQL(Posicion posicion,ClienteAsp clienteAsp,TipoElemento tipo ){
		Session session=null;
		
		try{
			session = getSession();

			String consultaJerarquia = "";
			if(posicion.getCodigoTipoJerarquia() !=null && !"".equals(posicion.getCodigoTipoJerarquia()))
				consultaJerarquia = " and tj.codigo = '"+posicion.getCodigoTipoJerarquia()+"'"; 
			
			String consulta = "select distinct top 4000 posicion.*,estante.*,m.*, (CAST(j.valoracion as varchar) +' '+ j.descripcion) as [valoracion] " +
			" from posiciones posicion " +
			" left join estanterias estante on posicion.estante_id = estante.id " +
			" left join grupos grupo on estante.grupo_id = grupo.id " +
			" left join secciones sec on grupo.seccion_id = sec.id" +
			" left join depositos dep on sec.deposito_id = dep.id" +
			" left join sucursales suc on dep.sucursal_id = suc.id" +
			" left join empresas emp on suc.empresa_id = emp.id" +
			" left join clientesAsp cliAsp on emp.cliente_id = cliAsp.id " +
			" left join modulos m on posicion.modulo_id = m.id and estante.id = m.estante_id " + 
			" left join tipos_jerarquia tj on estante.tipojerarquia_id = tj.id " + consultaJerarquia +
			" left join jerarquia j on " +
			" (posicion.posvertical between j.verticaldesde and j.verticalhasta) and " +
			" (posicion.poshorizontal between j.horizontaldesde and j.horizontalhasta) and j.tipo_id = tj.id " +
			" where posicion.estado = 'DISPONIBLE' "; 
			
			if(clienteAsp!=null)
				consulta+= " and cliAsp.id = " + clienteAsp.getId();
        		   	
			if(posicion!=null && posicion.getPosDesdeVert()!=null)
				consulta+= " and posicion.posVertical>= " + posicion.getPosDesdeVert();
			
			if(posicion!=null && posicion.getPosDesdeHor()!=null)
				consulta+= " and posicion.posHorizontal>= " + posicion.getPosDesdeHor();
			
			if(posicion!=null && posicion.getPosHastaVert()!=null)
				consulta+= " and posicion.posVertical<= " + posicion.getPosHastaVert();
			
			if(posicion!=null && posicion.getPosHastaHor()!=null)
				consulta+= " and posicion.posHorizontal<= " + posicion.getPosHastaHor();
			
			if(posicion.getCodigoDesdeEstante() !=null && !"".equals(posicion.getCodigoDesdeEstante()))
        		consulta+= " and CAST(estante.codigo AS int) >= "+posicion.getCodigoDesdeEstante();

        	if(posicion.getCodigoHastaEstante() !=null && !"".equals(posicion.getCodigoHastaEstante()))
        		consulta+= " and CAST(estante.codigo AS int) <= "+posicion.getCodigoHastaEstante();
        	
        	if(posicion.getCodigoDesdeModulo() !=null && !"".equals(posicion.getCodigoDesdeModulo()))
        		consulta+= " and modulo.codigo >='"+ posicion.getCodigoDesdeModulo()+"'";
        	
        	if(posicion.getCodigoHastaModulo() !=null && !"".equals(posicion.getCodigoHastaModulo()))
        		consulta+= " and modulo.codigo <='"+ posicion.getCodigoHastaModulo()+"'";
        	
        	if(posicion.getCodigoJerarquia() !=null && !"".equals(posicion.getCodigoJerarquia()))
        		consulta+= " and j.id = " + posicion.getCodigoJerarquia(); 
        		
     		if(tipo !=null)
				consulta+= " and posicion.modulo_id not in (select distinct m.id " +
						" from modulos m " +
                        " left join posiciones p on p.modulo_id = m.id " +
                        " left join elementos e on p.id = e.posicion_id " +
                        " left join tipoElementos te on e.tipoElemento_id = te.id " +
                        " where te.id != "+ tipo.getId() +") " ; 
			
			
			
        	consulta+= " order by valoracion desc, posicion.posVertical asc, posicion.posHorizontal asc ";
        	SQLQuery q = session.createSQLQuery(consulta)
        	.addEntity("posicion",Posicion.class)
        	.addJoin("estante", "posicion.estante")
        	.addJoin("m", "posicion.modulo")
        	.addScalar("valoracion",Hibernate.STRING);		
			
        	//Query q = session.createSQLQuery(consulta).setResultTransformer(Transformers.aliasToBean(Posicion.class));
        	
			return (List<Posicion>)q.list();
			
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

