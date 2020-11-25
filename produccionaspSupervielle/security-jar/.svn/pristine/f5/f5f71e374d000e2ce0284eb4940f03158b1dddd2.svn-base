/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 21/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteEmpService;
import com.security.accesoDatos.configuraciongeneral.interfaz.ListaPreciosService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.ConceptoFacturable;
import com.security.modelo.configuraciongeneral.ListaPrecios;
import com.security.modelo.configuraciongeneral.ListaPreciosDetalle;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.TipoVariacion;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class ListaPreciosServiceImp extends GestorHibernate<ListaPrecios> implements ListaPreciosService {
	private static Logger logger=Logger.getLogger(ListaPreciosServiceImp.class);
	private ClienteEmpService clienteEmpService; 
	
	@Autowired
	public ListaPreciosServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Autowired
	public void setClienteEmpService(ClienteEmpService clienteEmpService) {
		this.clienteEmpService = clienteEmpService;
	}



	@Override
	public Class<ListaPrecios> getClaseModelo() {
		return ListaPrecios.class;
	}

	@Override
	public boolean delete(ListaPrecios objeto) {
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
	public boolean delete(ListaPreciosDetalle objeto) {
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

	@SuppressWarnings("unchecked")
	@Override
	public List<ListaPrecios> listarListasPrecios(String codigo, String descripcion, 
			TipoVariacion tipoVariacion, ClienteAsp cliente) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			//filtro por codigo
			if(codigo != null && !"".equals(codigo))
				c.add(Restrictions.ilike("codigo", codigo+"%"));
			//filtro por descripcion
			if(descripcion != null && !"".equals(descripcion))
				c.add(Restrictions.ilike("descripcion", descripcion+"%"));
			//filtro por tipoVariacion
			if(tipoVariacion != null)
				c.add(Restrictions.eq("tipoVariacion", tipoVariacion));
			//filtro por cliente
			if(cliente != null)
				c.add(Restrictions.eq("clienteAsp", cliente));
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
	public boolean save(ListaPrecios objeto) {
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
	public boolean update(ListaPrecios object) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(object);
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
	public boolean update(ListaPreciosDetalle objeto) {
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
	
	@Override
	public ListaPreciosDetalle obtenerListaPreciosDetallePorId(Long id) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(ListaPreciosDetalle.class);
			//filtro por codigo
			if(id != null)
				c.add(Restrictions.eq("id", id));			
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return (ListaPreciosDetalle) c.uniqueResult();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener detalle de lista de precios",e);
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
	public List<ListaPreciosDetalle> listarDetallesPorListaPreciosConceptoFacturable(
			ListaPrecios listaPrecios, ConceptoFacturable conceptoFacturable) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(ListaPreciosDetalle.class);
			//filtro por listaPrecios
			if(listaPrecios != null)
				c.add(Restrictions.eq("listaPrecios", listaPrecios));		
			//filtro por ConceptoFacturable
			if(conceptoFacturable != null)
				c.add(Restrictions.eq("conceptoFacturable", conceptoFacturable));	
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return c.list();
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
	public List<ListaPrecios> listarListasPreciosPopup(String val, ClienteAsp clienteAsp) {
		return listarListasPreciosPopup(val, clienteAsp, null,  null);
	}
	@Override
	public List<ListaPrecios> listarListasPreciosPopup(String val, ClienteAsp clienteAsp, Boolean habilitado) {
		return listarListasPreciosPopup(val, clienteAsp, null,  habilitado);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ListaPrecios> listarListasPreciosPopup(String val, ClienteAsp clienteAsp, ClienteEmp clienteEmp, Boolean habilitado) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			//filtro por codigo
			if(val != null && !"".equals(val))
				c.add(Restrictions.or(
						Restrictions.ilike("codigo", val+"%"), 
						Restrictions.ilike("descripcion", val+"%")));
			//filtro por habilitada
			if(habilitado != null){
				c.add(Restrictions.eq("habilitada", habilitado));
			}
			//filtro por clientesEmp
			if(clienteEmp !=null){
				c.createCriteria("clientesEmp").add(Restrictions.eq("id", clienteEmp.getId()));
			}			
			//filtro por cliente
			if(clienteAsp != null)
				c.add(Restrictions.eq("clienteAsp", clienteAsp));
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ListaPrecios> listarListasPreciosByClientePopup(String val, String valCodigo, ClienteAsp clienteAsp) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			ClienteEmp cliente = new ClienteEmp();
			if(valCodigo != null && !"".equals(valCodigo)){
				cliente.setCodigo(valCodigo);
				cliente = clienteEmpService.getByCodigo(cliente, clienteAsp);
				return new ArrayList<ListaPrecios>(cliente.getListasPrecio());
			}else{			
				//filtro por codigo
				if(val != null && !"".equals(val))
					c.add(Restrictions.or(
							Restrictions.ilike("codigo", val+"%"), 
							Restrictions.ilike("descripcion", val+"%")));
				//filtro por cliente
				if(clienteAsp != null)
					c.add(Restrictions.eq("clienteAsp", clienteAsp));
			}
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
	public ListaPrecios obtenerListaPreciosPorCodigo(String codigo, ClienteAsp clienteAsp) {
		return obtenerListaPreciosPorCodigo(codigo,clienteAsp, null ,null, null);
	}
	
	@Override
	public ListaPrecios obtenerListaPreciosPorCodigo(String codigo, ClienteAsp clienteAsp, Boolean habilitado) {
		return obtenerListaPreciosPorCodigo(codigo,clienteAsp, null ,null, habilitado);
	}
	
	@Override
	public ListaPrecios obtenerListaPreciosPorCodigo(String codigo, ClienteAsp clienteAsp, String codigoConceptoFacturable, ClienteEmp clienteEmp, Boolean habilitado) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			
//			Criteria c = session.createCriteria(getClaseModelo());
//			//filtro por codigo
//			if(codigo != null && !"".equals(codigo))
//				c.add(Restrictions.eq("codigo", codigo));
//			//filtro por clientesEmp
//			if(clienteEmp !=null){
//				c.createCriteria("clientesEmp").add(Restrictions.eq("id", clienteEmp.getId()));
//			}	
////			//filtro por codigoConceptoFacturable
//			if(codigoConceptoFacturable !=null && codigoConceptoFacturable.length()>0){
//				c.createCriteria("detalle").createCriteria("conceptoFacturable").add(Restrictions.eq("codigo", codigoConceptoFacturable));
//			}
//			//filtro habilitado
//			if(habilitado!=null){
//				c.add(Restrictions.eq("habilitada", habilitado));
//			}
//			//filtro por cliente
//			if(clienteAsp != null){
//				c.add(Restrictions.eq("clienteAsp", clienteAsp));
//			}
//			//Seteo propiedades de la consulta
//			//c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
//			String consulta = "SELECT lp FROM ListaPrecios lp JOIN FETCH lp.detalle JOIN FETCH lp.clientesEmp WHERE 1 = 1 ";
//							  if(codigo != null && !"".equals(codigo)) {
//								  consulta += "AND lp.codigo = '" + codigo + "' ";
//							  }
//							  if(clienteEmp != null) {
//								  consulta += "AND lp.clientesEmp.id = " + clienteEmp.getId().longValue() + " ";
//							  }
//							  if(codigoConceptoFacturable != null && codigoConceptoFacturable.length() > 0) {
//								  consulta += "AND lp.detalle.conceptoFacturable.codigo = '" + codigoConceptoFacturable + "' ";
//							  }
//							  if(habilitado != null) {
//								  consulta += "AND lp.habilitada = " + habilitado + " ";
//							  }
//							  if(clienteAsp != null) {
//								  consulta += "AND lp.clienteAsp.id = " + clienteAsp.getId().longValue() + " ";
//							  }
			
//			String consulta = "SELECT ListaPrecios.*, cp.* "
//					+ "  FROM x_clienteEmp_listaPrecio clp "
//					+ "inner join lista_precios ListaPrecios "
//					+ "on clp.listaPrecios_id = ListaPrecios.id "
//					+ "inner join conceptos_precios cp "
//					+ "on cp.listaPrecios_id = ListaPrecios.id "
//					+ "inner join conceptoFacturable cf "
//					+ "on cp.conceptoFacturable_id = cf.id "
//					+ "where 1=1 ";
//					
//					if(clienteEmp != null)
//						consulta += "and clienteEmp_id = "+clienteEmp.getId().longValue()+" ";
//					if(codigo != null && !"".equals(codigo))
//						consulta += "and ListaPrecios.codigo like '"+codigo+"' ";
//					if(habilitado != null && habilitado)
//						consulta += "and habilitada = 1 ";
//					if(codigoConceptoFacturable != null && codigoConceptoFacturable.length() > 0)
//						consulta += "and cf.codigo like '"+codigoConceptoFacturable+"'";
//					if(clienteAsp != null)
//						consulta += "and ListaPrecios.clienteAsp_id = "+clienteAsp.getId().longValue();
							  
//			ListaPrecios listaPre =(ListaPrecios) session.createQuery(consulta).uniqueResult();
//			String consulta = "Select listaPrecios.* from ListaPrecios lp where lp.codigo like '" + codigo +
//					"' and lp.clienteAsp_id = " + clienteAsp.getId();
//			SQLQuery q = session.createSQLQuery(consulta)
//		        	.addEntity("ListaPrecios",ListaPrecios.class)
//		        	.addJoin("cp", "ListaPrecios.detalle");
			
			String consulta = "from ListaPrecios listaPrecios where listaPrecios.clienteAsp.id = :clienteAsp_id " +
			" and listaPrecios.codigo = :codigo " ;
			
			ListaPrecios listaPrecios =  (ListaPrecios) session.createQuery(consulta)
					.setLong("clienteAsp_id", clienteAsp.getId())
					.setString("codigo", codigo).list()
					.get(0);
			
			
			//ListaPrecios listaPrecios = (ListaPrecios)session.createSQLQuery(consulta).setResultTransformer(Transformers.aliasToBean(ListaPrecios.class)).uniqueResult();
			
			
//			List<Object[]> lista= (List<Object[]>) q.list();
//			Object[] obj;
//			ListaPrecios listaP;
//			ListaPreciosDetalle listaPD;
//			
//			if(lista!=null && lista.size()>0){
//				obj = (Object[]) lista.get(0);
//				listaP = (ListaPrecios) obj[0];
//				listaPD = (ListaPreciosDetalle)obj[1];
//				Set<ListaPreciosDetalle> setDetalles = new HashSet<ListaPreciosDetalle>();
//				setDetalles.add(listaPD);
//				listaP.setDetalle(setDetalles);
//				return listaP;
//			}
				
			return listaPrecios;
			
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
	public List<ListaPrecios> listarTodosListaFiltrados(ListaPrecios listaPrecios, ClienteAsp cliente)
    {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtro por lista de precio
        	if(listaPrecios!=null && listaPrecios.getDescripcion()!= null && !"".equals(listaPrecios.getDescripcion()))
        		crit.add(Restrictions.ilike("descripcion", listaPrecios.getDescripcion() + "%"));	
        	//filtro por cliente
        	if(cliente != null)
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	else 
        		crit.add(Restrictions.isNull("cliente"));
        	//criterios de ordenación
        	crit.addOrder(Order.asc("descripcion"));
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
	public List<ListaPrecios> listarPorId(Long[] ids, ClienteAsp cliente) {
		Session session = null; 
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
			//Filtro por groupNames
			crit.add(Restrictions.in("id", ids));
			//Filtro por cliente
        	if(cliente != null)
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	else 
        		crit.add(Restrictions.isNull("clienteAsp"));
			return crit.list();
		} catch (HibernateException e) {
			logger.error("no se pudo obtener la lista de precio por identificador",e);
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
	public List<ListaPrecios> listarPorConceptoFacturable(ConceptoFacturable concepto, ClienteEmp clienteEmp, ClienteAsp clienteAsp) {
		Session session = null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			//filtro por codigo
			if(concepto != null)
				c.createCriteria("detalle").add(Restrictions.eq("conceptoFacturable", concepto));		
			//filtro por clientesEmp
			if(clienteEmp !=null){
				c.createCriteria("clientesEmp").add(Restrictions.eq("id", clienteEmp.getId()));
			}	
			//filtro por cliente
			if(clienteAsp != null)
				c.add(Restrictions.eq("clienteAsp", clienteAsp));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			c.setFetchMode("tipoVariacion", FetchMode.JOIN); // Modificado  LAZY EAGER LAZY-EAGER
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
	public ListaPrecios getByID(Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			String consulta = " SELECT DISTINCT lp FROM ListaPrecios lp WHERE lp.id = "+ id.longValue()+"";
			
			ListaPrecios listaPrecios = (ListaPrecios)session.createQuery(consulta).uniqueResult();
			Hibernate.initialize(listaPrecios.getDetalle());
			
			return listaPrecios;
			
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
