/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.CaiService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.AfipTipoComprobante;
import com.security.modelo.configuraciongeneral.Cai;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Empresa;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.configuraciongeneral.Sucursal;
import com.security.utils.Constantes;

/**
 * @author Gonzalo Noriega
 * @modificado Victor Kenis (11/08/2011)
 *
 */
@Component
public class SerieServiceImp extends GestorHibernate<Serie> implements SerieService {
	private static Logger logger=Logger.getLogger(SerieServiceImp.class);
	private CaiService caiService;
	
	@Autowired
	public SerieServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}
	
	@Autowired
	public void setCaiService(CaiService caiService){
		this.caiService = caiService;
	}

	@Override
	public Class<Serie> getClaseModelo() {
		return Serie.class;
	}

	@Override
	public Boolean guardarSerie(Serie serie) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(serie);
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
	public Boolean actualizarSerie(Serie serie) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(serie);
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
	public Boolean eliminarSerie(Serie serie) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(serie);
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
	public Serie obtenerPorCodigo(String codigo, ClienteAsp clienteAsp) {
		Session session = null;
		if(clienteAsp == null)
			return null;
		try {
			//obtenemos una sesión
			session = getSession();
			
//			Criteria c = session.createCriteria(getClaseModelo());			
//			//filtro por codigo
//			if(codigo != null && !"".equals(codigo))
//				c.add(Restrictions.ilike("codigo", codigo +"%"));
//			//filtro por cliente
//			c.add(Restrictions.eq("cliente", clienteAsp));
//			//Seteo propiedades de la consulta
//			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			String consulta = "SELECT DISTINCT sr FROM Serie sr WHERE 1 = 1 ";
						if(codigo != null && !"".equals(codigo)) {
							consulta += "AND sr.codigo LIKE '" + codigo + "%' ";
						}
						consulta += "AND sr.cliente.id = " + clienteAsp.getId().longValue() + " ";
			
			Serie serie = (Serie) session.createQuery(consulta).uniqueResult();	
			
			return serie;
			
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
	
	@Override
	public Serie obtenerPorCodigo(String codigo, String tipoSerie, ClienteAsp clienteAsp) {
		Session session = null;
		if(clienteAsp == null)
			return null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());			
			//filtro por codigo
			if(codigo != null && !"".equals(codigo))
				c.add(Restrictions.eq("codigo", codigo));
			//filtro por tipo
        	if(tipoSerie != null && !"".equals(tipoSerie))
        		c.add(Restrictions.eq("tipoSerie", tipoSerie));
			//filtro por cliente
			c.add(Restrictions.eq("cliente", clienteAsp));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return (Serie) c.uniqueResult();		
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
	
	@Override
	public Serie obtenerPorCodigo(String codigo, String tipoSerie, String codigoEmpresa, Boolean habilitado,ClienteAsp clienteAsp) {
		Session session = null;
		if(clienteAsp == null)
			return null;
		try {
			//obtenemos una sesión
			session = getSession();
			Criteria c = session.createCriteria(getClaseModelo());
			
			//filtro por codigo
			if(codigo != null && !"".equals(codigo))
				c.add(Restrictions.eq("codigo", codigo));
			//filtro por tipo
        	if(tipoSerie != null && !"".equals(tipoSerie))
        		c.add(Restrictions.eq("tipoSerie", tipoSerie));
        	//filtro por Empresa
        	if(codigoEmpresa != null && !"".equals(codigoEmpresa))
        		c.createCriteria("empresa").add(Restrictions.eq("codigo", codigoEmpresa));
        	//filtro por habilitadas
        	if(habilitado != null)
        		c.add(Restrictions.eq("habilitado", habilitado));
			//filtro por cliente
			c.add(Restrictions.eq("cliente", clienteAsp));
			//Seteo propiedades de la consulta
			c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return (Serie) c.uniqueResult();		
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
	
	@Override
	public Serie obtenerPorCodigo(String codigo, String tipoSerie, String codigoEmpresa, ClienteAsp clienteAsp) {
		return obtenerPorCodigo(codigo, tipoSerie, codigoEmpresa, null, clienteAsp);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Serie> listarSerieFiltradas(Serie serie, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(serie!=null){
	        	if(serie.getDescripcion() !=null && !"".equals(serie.getDescripcion()))
	        		crit.add(Restrictions.ilike("descripcion", serie.getDescripcion() + "%"));
	        	if(serie.getCodigo() !=null && !"".equals(serie.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", serie.getCodigo() + "%"));	      
	        	if(serie.getCodigoEmpresa()!=null && serie.getCodigoEmpresa().length()>0){
	        		crit.createCriteria("empresa", "emp").add(Restrictions.eq("codigo", serie.getCodigoEmpresa()));
	        	}
	        	if(serie.getCodigoSucursal()!=null && serie.getCodigoSucursal().length()>0){
	        		crit.createCriteria("sucursal").add(Restrictions.eq("codigo", serie.getCodigoSucursal()));
	        	}
	        	if(serie.getIdAfipTipoComprobante()!=null && serie.getIdAfipTipoComprobante()>0){
	        		crit.createCriteria("afipTipoComprobante").add(Restrictions.eq("id", serie.getIdAfipTipoComprobante()));
	        	}
	        	if(serie.getTipoSerie()!=null && 
	        			(Constantes.SERIE_TIPO_SERIE_DOCUMENTO_INTERNO.equals(serie.getTipoSerie())
	        				|| Constantes.SERIE_TIPO_SERIE_FACTURA.equals(serie.getTipoSerie())
	        				|| Constantes.SERIE_TIPO_SERIE_REMITO.equals(serie.getTipoSerie()
	        			))){
	        		crit.add(Restrictions.eq("tipoSerie", serie.getTipoSerie()));
	        	}
        	}
        	if(cliente != null)
        		crit.add(Restrictions.eq("cliente", cliente));
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
	public List<Serie> listarSerieFiltradasPopup(Serie serie,String val, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(serie!=null){
        		String valores[] = null;
            	if(val!=null){
            		valores = val.split(" ");
	            	if(valores!=null){
	            		for(String filtro : valores){
	    		        	crit.add(Restrictions.or(
	    		        			Restrictions.ilike("codigo", filtro+"%"),
	    		        			Restrictions.ilike("descripcion", filtro+"%")));
	            		}
	            	}
            	}
	        	if(serie.getDescripcion() !=null && !"".equals(serie.getDescripcion()))
	        		crit.add(Restrictions.ilike("descripcion", serie.getDescripcion() + "%"));
	        	if(serie.getCodigo() !=null && !"".equals(serie.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", serie.getCodigo() + "%"));	      
	        	if(serie.getCodigoEmpresa()!=null && serie.getCodigoEmpresa().length()>0){
	        		crit.createCriteria("empresa", "emp").add(Restrictions.eq("codigo", serie.getCodigoEmpresa()));
	        	}
	        	if(serie.getCodigoSucursal()!=null && serie.getCodigoSucursal().length()>0){
	        		crit.createCriteria("sucursal").add(Restrictions.eq("codigo", serie.getCodigoSucursal()));
	        	}
	        	if(serie.getIdAfipTipoComprobante()!=null && serie.getIdAfipTipoComprobante()>0){
	        		crit.createCriteria("afipTipoComprobante").add(Restrictions.eq("id", serie.getIdAfipTipoComprobante()));
	        	}
	        	if(serie.getTipoSerie()!=null && 
	        			(Constantes.SERIE_TIPO_SERIE_DOCUMENTO_INTERNO.equals(serie.getTipoSerie())
	        				|| Constantes.SERIE_TIPO_SERIE_FACTURA.equals(serie.getTipoSerie())
	        				|| Constantes.SERIE_TIPO_SERIE_REMITO.equals(serie.getTipoSerie()
	        			))){
	        		crit.add(Restrictions.eq("tipoSerie", serie.getTipoSerie()));
	        	}
        	}
        	if(clienteAsp != null)
        		crit.add(Restrictions.eq("cliente", clienteAsp));
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
	public Serie verificarSerie(Serie serie){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(serie!=null){
	        	if(serie.getCliente() !=null)
	        		crit.createCriteria("cliente").add(Restrictions.eq("id", serie.getCliente().getId()));
	        	if(serie.getIdEmpresa() !=null)
	        		crit.createCriteria("empresa").add(Restrictions.eq("id", serie.getIdEmpresa()));
	        	if(serie.getIdSucursal() !=null)
	        		crit.createCriteria("sucursal").add(Restrictions.eq("id", serie.getIdSucursal()));
	        	if(serie.getCodigo() !=null && !"".equals(serie.getCodigo()))
	        		crit.add(Restrictions.eq("codigo", serie.getCodigo()));	      
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Serie> result = crit.list();
        	if(result.size() == 1){
        		return result.get(0);
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

	@Override
	public List<Serie> listarSeriePopup(String val, String tipoSerie, String condIvaClientes, ClienteAsp clienteAsp, Empresa empresa){
		return listarSeriePopup(val, tipoSerie, condIvaClientes, clienteAsp, empresa, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Serie> listarSeriePopup(String val, String tipoSerie, String condIvaClientes, ClienteAsp clienteAsp, Empresa empresa, Boolean habilitado) {
		Session session = null;
		if(clienteAsp == null)
			return null;
        try {
        	String valores[] = null;
        	if(val!=null)
        		valores = val.split(" ");
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.createCriteria("empresa", "emp");
        	//filtro value
        	if(valores!=null){
        		for(String filtro : valores){
		        	c.add(Restrictions.or(
		        			Restrictions.ilike("codigo", filtro+"%"),
		        			Restrictions.ilike("descripcion", filtro+"%")));
        		}
        	}
        	//filtro por empresa
        	if(empresa != null)
        		c.add(Restrictions.eq("emp.id", empresa.getId()));
        	//filtro por tipo
        	if(tipoSerie != null && !"".equals(tipoSerie))
        		c.add(Restrictions.ilike("tipoSerie", tipoSerie+"%"));
        	//filtro por habilitado
        	if(habilitado != null)
        		c.add(Restrictions.eq("habilitado", habilitado));
        	//filtro por condicion IVA del cliente
        	if(condIvaClientes != null && !"".equals(condIvaClientes))
        		c.add(Restrictions.ilike("condIvaClientes", condIvaClientes+"%"));
        	//filtro clienteAsp
        	c.add(Restrictions.eq("cliente", clienteAsp));
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Serie> listarSerieFiltradasPopup(Serie serie, ClienteAsp clienteAsp) {
		Session session = null;
		try {
			// obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
			if (serie != null) {
				if (serie.getDescripcion() != null	&& serie.getDescripcion().length()>0){
					crit.add(Restrictions.ilike("descripcion",
							"%" + serie.getDescripcion() + "%"));
				}
				if(serie.getCondIvaClientes()!=null && serie.getCondIvaClientes().length()>0){
					crit.add(Restrictions.eq("condIvaClientes", serie.getCondIvaClientes()));
				}
				if (serie.getEmpresa() != null) {
					crit.add(Restrictions.eq("empresa",serie.getEmpresa()));
				}
				if (serie.getSucursal() != null) {
					crit.add(Restrictions.eq("sucursal",serie.getSucursal()));
				}
				if (serie.getAfipTipoComprobante() != null) {
					crit.add(Restrictions.eq("afipTipoComprobante", serie.getAfipTipoComprobante()));
				}
				if (serie.getTipoSerie() != null
						&& (Constantes.SERIE_TIPO_SERIE_DOCUMENTO_INTERNO.equals(serie.getTipoSerie())
								|| Constantes.SERIE_TIPO_SERIE_FACTURA.equals(serie.getTipoSerie()) 
								|| Constantes.SERIE_TIPO_SERIE_REMITO.equals(serie.getTipoSerie()))) {
					crit.add(Restrictions.eq("tipoSerie", serie.getTipoSerie()));
				}
				crit.add(Restrictions.eq("habilitado", true));
				
				if (serie.getFechaParaCai() != null){
					
					Cai cai = new Cai();
					cai.setFechaVencimiento(serie.getFechaParaCai());
					List<Cai> listCai = caiService.listarCaiNoVencidas(cai, clienteAsp);
    				if(listCai!=null && listCai.size()>0)
    				{
    					Disjunction disjunction = Restrictions.disjunction();
    		        	for(Cai caiComp : listCai){
    		        		disjunction.add(Restrictions.eq("id", caiComp.getSerie().getId()));
    		        	}
    		        	crit.add(disjunction);
    				}else
    				{
    					return null;
    				}
				}
			}
			
			
			
			if (clienteAsp != null)
				crit.add(Restrictions.eq("cliente", clienteAsp));
			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return crit.list();
		} catch (HibernateException hibernateException) {
			logger.error("No se pudo listar ", hibernateException);
			return null;
		} finally {
			try {
				session.close();
			} catch (Exception e) {
				logger.error("No se pudo cerrar la sesión", e);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Serie obtenerSerieFiltradaServlet(Serie serie, ClienteAsp clienteAsp) {
		Session session = null;
		try {
			// obtenemos una sesión
			session = getSession();
			Criteria crit = session.createCriteria(getClaseModelo());
			if (serie != null) {
				if (serie.getDescripcion() != null	&& serie.getDescripcion().length()>0){
					crit.add(Restrictions.ilike("descripcion",
							"%" + serie.getDescripcion() + "%"));
				}
				if(serie.getCodigo()!=null && serie.getCodigo().length()>0){
					crit.add(Restrictions.eq("codigo", serie.getCodigo()));
				}
				if(serie.getCondIvaClientes()!=null && serie.getCondIvaClientes().length()>0){
					crit.add(Restrictions.eq("condIvaClientes", serie.getCondIvaClientes()));
				}
				if (serie.getEmpresa() != null) {
					crit.add(Restrictions.eq("empresa",serie.getEmpresa()));
				}
				if (serie.getSucursal() != null) {
					crit.add(Restrictions.eq("sucursal",serie.getSucursal()));
				}
				if (serie.getAfipTipoComprobante() != null) {
					crit.add(Restrictions.eq("afipTipoComprobante", serie.getAfipTipoComprobante()));
				}
				if (serie.getTipoSerie() != null
						&& (Constantes.SERIE_TIPO_SERIE_DOCUMENTO_INTERNO.equals(serie.getTipoSerie())
								|| Constantes.SERIE_TIPO_SERIE_FACTURA.equals(serie.getTipoSerie()) 
								|| Constantes.SERIE_TIPO_SERIE_REMITO.equals(serie.getTipoSerie()))
								|| Constantes.SERIE_TIPO_SERIE_RECIBO.equals(serie.getTipoSerie())) {
					crit.add(Restrictions.eq("tipoSerie", serie.getTipoSerie()));
				}
				crit.add(Restrictions.eq("habilitado", true));
				
					if (serie.getFechaParaCai() != null){
					
					Cai cai = new Cai();
					cai.setFechaVencimiento(serie.getFechaParaCai());
					CaiService caiService = new CaiServiceImp(HibernateControl.getInstance());
					List<Cai> listCai = caiService.listarCaiNoVencidas(cai, clienteAsp);
    				if(listCai!=null && listCai.size()>0)
    				{
    					Disjunction disjunction = Restrictions.disjunction();
    		        	for(Cai caiComp : listCai){
    		        		disjunction.add(Restrictions.eq("id", caiComp.getSerie().getId()));
    		        	}
    		        	crit.add(disjunction);
    				}else
    				{
    					return null;
    				}
				}
			}
			if (clienteAsp != null)
				crit.add(Restrictions.eq("cliente", clienteAsp));
			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			return (Serie)crit.uniqueResult();
		} catch (HibernateException hibernateException) {
			logger.error("No se pudo listar ", hibernateException);
			return null;
		} finally {
			try {
				session.close();
			} catch (Exception e) {
				logger.error("No se pudo cerrar la sesión", e);
			}
		}
	}
	
	@Override
	public Serie getByID(Long id) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
			
			String consulta = " SELECT DISTINCT se FROM Serie se WHERE se.id = "+ id.longValue()+"";
			
			Serie serie = (Serie)session.createQuery(consulta).uniqueResult();
			Hibernate.initialize(serie.getEmpresa());
			Hibernate.initialize(serie.getEmpresa().getSucursales());
			
			return serie;
			
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
