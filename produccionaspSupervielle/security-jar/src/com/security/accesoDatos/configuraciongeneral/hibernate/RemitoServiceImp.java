/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.ScrollMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.SerieService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
import com.security.modelo.configuraciongeneral.Serie;

/**
 * @author Victor Kenis
 *
 */
@Component
public class RemitoServiceImp extends GestorHibernate<Remito> implements RemitoService {
	private static Logger logger=Logger.getLogger(RemitoServiceImp.class);
	
	
	@Autowired
	public RemitoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Remito> getClaseModelo() {
		return Remito.class;
	}

	@Override
	public Boolean guardarRemito(Remito remito) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(remito);
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
	public Boolean actualizarRemito(Remito remito) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(remito);
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
	public synchronized Boolean guardarRemitoYDetalles(Set<RemitoDetalle> remitoDetalles, Remito remito) {
		
		
		Long proximoCodigoLong = null;
		SerieService serieService = new SerieServiceImp(HibernateControl.getInstance());
		Serie serie = serieService.obtenerPorId(remito.getSerie().getId());
		if(serie!=null)
			proximoCodigoLong = Long.valueOf(serie.getUltNroImpreso()) + 1L;
		
		
		String cadena = String.valueOf(proximoCodigoLong);
		int cantNumeros = cadena.length();
		int faltan = 8 - cantNumeros;
		for(int f = 0; f<faltan ; f++)
		{
			cadena= "0" + cadena;
		}
		
		//String ultNumeroSerie = remito.getSerie().getUltNroImpreso();
		
		//Long numeroSinPrefijoLong = parseLongCodigo(remito.getNumeroSinPrefijo());
		
		Session session = null;
		Transaction tx = null;
		
		try {
			
			// obtenemos una sesión
			session = getSession();
			// creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			
			
			if(proximoCodigoLong != null && cadena != null && !"".equals(cadena))
			{
				serie.setUltNroImpreso(cadena);
				session.update(serie);
			}
			//guardamos la remito primero
			//para que se cree el ID
			session.save(remito);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
	 //////tx.commit();
			
			//Recorro los interchanges para actualizarlos
			for(RemitoDetalle remitoDetalle:remitoDetalles){
				try {
					remitoDetalle.setRemito(remito);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
			//ya con el id guardado
			//le seteamos a la remito la lista de detalles
			remito.setDetalles(remitoDetalles);
	//////tx.begin();
			//Actualizamos la remito con la lista de detalles
			session.update(remito);
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
	public Boolean actualizarRemitoYDetalles(Boolean noAnexar,Set<RemitoDetalle> remitoDetalles, Remito remito) {

		Session session = null;
		Transaction tx = null;
		
		try {
			
			// obtenemos una sesión
			session = getSession();
			// creamos la transacción
			tx = session.getTransaction();
			
			if (noAnexar!=null && noAnexar == true) {
				Set<RemitoDetalle> remitoDetallesViejos = new HashSet<RemitoDetalle>();
				remitoDetallesViejos = remito.getDetalles();

				tx.begin();
				for (RemitoDetalle remitoDetalle : remitoDetallesViejos) {
					try {
						session.delete(remitoDetalle);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
				tx.commit();
			}
			
			//Recorro los detalles para actualizarlos
			for(RemitoDetalle remitoDetalle:remitoDetalles){
				try {
					remitoDetalle.setRemito(remito);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
			//ya con el id guardado
			//le seteamos a la remito la lista de detalles
			remito.setDetalles(remitoDetalles);
			tx.begin();
			//Actualizamos la remito con la lista de detalles
			session.update(remito);
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
	public Boolean actualizarRemitoList(List<Remito> listRemitos)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(Remito actualizar : listRemitos){
    			session.update(actualizar);
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo actualizar la coleccion de remitos ", hibernateException);
	        return false;
        }
   }
	
	@Override
	public Boolean guardarRemitoList(List<Remito> listRemitos)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(Remito actualizar : listRemitos){
    			session.save(actualizar);
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo grabar la coleccion de remitos ", hibernateException);
	        return false;
        }
   }
	
	@Override
	public Boolean eliminarRemito(Remito remito) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(remito);
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
	public Remito getByNumero(Long numero, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("numero", numero));
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
        	
            return (Remito) crit.uniqueResult();
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
	
//	@Override
//	public Remito busquedaServlet(Remito remitoBusqueda, ClienteAsp clienteAsp) {
//		Session session = null;
//        try {
//        	//obtenemos una sesión
//			session = getSession();
//        	Criteria crit = session.createCriteria(getClaseModelo());        	
//        	crit.createCriteria("clienteEmp", "cli");
//			crit.createCriteria("cli.empresa", "emp");
//        	
//        	if(remitoBusqueda!=null){
//        		if(remitoBusqueda.getCodigoDeposito()!=null && !"".equals(remitoBusqueda.getCodigoDeposito())){
//        			crit.createCriteria("posicion", "pos");
//                	crit.createCriteria("pos.estante", "est");
//                	crit.createCriteria("est.grupo", "grp");
//                	crit.createCriteria("grp.seccion", "sec");
//                	crit.createCriteria("sec.deposito", "dep");
//        			crit.add(Restrictions.eq("dep.codigo", remitoBusqueda.getCodigoDeposito()));
//        		}
//        		//codigo remito
//        		if(remitoBusqueda.getNumero()!=null && remitoBusqueda.getNumero()!=0){
//        			crit.add(Restrictions.eq("numero", remitoBusqueda.getNumero()));
//        		}
//        		//codigo deposito actual
//        		if(remitoBusqueda.getCodigoDeposito()!=null && remitoBusqueda.getCodigoDeposito().length()>0){
//        			crit.createCriteria("depositoActual", "depAct");
//        			crit.add(Restrictions.eq("depAct", remitoBusqueda.getCodigoDeposito()));
//        		}
//        		if(remitoBusqueda.getCodigoEmpresa()!=null && remitoBusqueda.getCodigoEmpresa().length()>0){
//        			crit.add(Restrictions.eq("emp.codigo", remitoBusqueda.getCodigoEmpresa()));
//        		}
//        		if(remitoBusqueda.getCodigoTipoRemito()!=null && remitoBusqueda.getCodigoTipoRemito().length()>0){
//        			crit.createCriteria("tipoRemito", "tipEle");
//        			crit.add(Restrictions.eq("tipEle", remitoBusqueda.getCodigoTipoRemito()));
//        		}
//        		if(remitoBusqueda.getCodigoCliente()!=null && remitoBusqueda.getCodigoCliente().length()>0){
//        			crit.add(Restrictions.eq("cli.codigo", remitoBusqueda.getCodigoCliente()));
//        		}
//        		
//        	}
//        	
//        	if(clienteAsp != null){
//        		crit.createCriteria("clienteAsp", "cliAsp");
//        		crit.add(Restrictions.eq("cliAsp.id", clienteAsp.getId()));
//        	}
//        	
//        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//        	
//            return (Remito) crit.uniqueResult();
//        } catch (HibernateException hibernateException) {
//        	logger.error("No se pudo listar ", hibernateException);
//	        return null;
//        }finally{
//        	try{
//        		session.close();
//        	}catch(Exception e){
//        		logger.error("No se pudo cerrar la sesión", e);
//        	}
//        }
//	}

	/**
	 * Recupera los remitos que contienen los codigos indicados en la lista
	 */
	@SuppressWarnings("unchecked")
	public List<Remito> getByNumeros(List<Long> numeros, ClienteAsp cliente){
		Session session = null;
		List<Remito> result = null;
		@SuppressWarnings("rawtypes")
		ArrayList<Remito> listaRemitosOrdenados = new ArrayList(numeros.size());
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	Disjunction disjunction = Restrictions.disjunction();
        	for(Long numero : numeros){
        		disjunction.add(Restrictions.eq("numero" , numero));
        		listaRemitosOrdenados.add(null);
        	}
        	crit.add(disjunction);
        	List<Remito> listaRemitos = null;
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        		listaRemitos = (List<Remito>)crit.list();        		
        	}else { 
        		listaRemitos = new ArrayList<Remito>();
        	}
        	//ordenamos los remitos por el orden de la lista de codigos
        	
        	if(numeros.size () == listaRemitos.size()){        						
        		Long numRem = null;
        		int index = 0;
        		Remito remito = null;
        		for(int i = 0; i<listaRemitos.size(); i++){
        			remito = listaRemitos.get(i);
        			numRem = remito.getNumero();
        			index = numeros.indexOf(numRem);
        			listaRemitosOrdenados.set(index, remito);
        		}
        		listaRemitos = listaRemitosOrdenados;
        	}else { 
        		listaRemitos = new ArrayList<Remito>();
        	}
        	
        	
            result = listaRemitos;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        result = null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
        return result;
	}
	
	/**
	 * Recupera los remitos que contienen los id indicados en la lista
	 */
	@Override
	public List<Remito> getByIds(List<Long> ids, ClienteAsp cliente){
		Session session = null;
		List<Remito> result = null;

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
        	result = (List<Remito>)crit.list();        		
        	
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
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Remito> listarRemitoFiltradas(Remito remito, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	if(remito!=null){
        		
        		if(remito.getCodigoEmpresa()!=null && !"".equals(remito.getCodigoEmpresa()))
        			crit.createCriteria("empresa", "emp").add(Restrictions.eq("emp.codigo", remito.getCodigoEmpresa()));
        		if(remito.getCodigoSucursal()!=null && !"".equals(remito.getCodigoSucursal()))
        			crit.createCriteria("sucursal", "suc").add(Restrictions.eq("suc.codigo", remito.getCodigoSucursal()));
        		if(remito.getCodigoTransporte()!=null && !"".equals(remito.getCodigoTransporte()))
        			crit.createCriteria("transporte", "tran").add(Restrictions.eq("tran.codigo", Integer.valueOf(remito.getCodigoTransporte())));
        		if(remito.getCodigoCliente()!=null && !"".equals(remito.getCodigoCliente()))
        			crit.createCriteria("clienteEmp", "cli").add(Restrictions.eq("cli.codigo", remito.getCodigoCliente()));
        		if(remito.getCodigoDepositoOrigen()!=null && !"".equals(remito.getCodigoDepositoOrigen()))
        			crit.createCriteria("depositoOrigen", "depOri").add(Restrictions.eq("depOri.codigo", remito.getCodigoDepositoOrigen()));
        		if(remito.getCodigoSerie()!=null && !"".equals(remito.getCodigoSerie()))
        			crit.createCriteria("serie", "ser").add(Restrictions.eq("ser.codigo", remito.getCodigoSerie()));
        		if(remito.getEstado() !=null && !"".equals(remito.getEstado()) && !"Seleccione un Estado".equals(remito.getEstado()))
        			crit.add(Restrictions.eq("estado", remito.getEstado()));
        		if(remito.getTipoRemito() !=null && !"".equals(remito.getTipoRemito()) && !"Todos".equals(remito.getTipoRemito()))
        			crit.add(Restrictions.eq("tipoRemito", remito.getTipoRemito()));
        		if(remito.getFechaDesde() != null && !"".equals(remito.getFechaDesde()))
        			crit.add(Restrictions.ge("fechaEmision", remito.getFechaDesde()));
        		if(remito.getFechaHasta() != null && !"".equals(remito.getFechaHasta()))
        			crit.add(Restrictions.le("fechaEmision", remito.getFechaHasta()));
        		if(remito.getNumeroDesde() != null && !"".equals(remito.getNumeroDesde()))
        			crit.add(Restrictions.ge("numero", parseLongCodigo(remito.getNumeroDesde())));
        		if(remito.getNumeroHasta() != null && !"".equals(remito.getNumeroHasta()))
        			crit.add(Restrictions.le("numero", parseLongCodigo(remito.getNumeroHasta())));
        		if(remito.getNumRequerimiento()!=null && !"".equals(remito.getNumRequerimiento()))
        			crit.add(Restrictions.eq("numRequerimiento", remito.getNumRequerimiento()));
        	}
        	if(clienteAsp != null){
        		crit.createCriteria("clienteAsp", "cliAsp");
        		crit.add(Restrictions.eq("cliAsp.id", clienteAsp.getId()));
        	}
        	crit.addOrder(Order.asc("fechaEmision"));
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
	public List<Remito> listarRemitosPorId(List<Remito> remitos, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	if(remitos!=null && remitos.size()>0)
				{
					Disjunction disjunction = Restrictions.disjunction();
		        	for(Remito remito : remitos){
		        			disjunction.add(Restrictions.eq("id", remito.getId()));
		        	}
		        	crit.add(disjunction);
			}
        	if(clienteAsp != null){
        		crit.createCriteria("clienteAsp", "cliAsp");
        		crit.add(Restrictions.eq("cliAsp.id", clienteAsp.getId()));
        	}
        	crit.setFetchMode("detalles", FetchMode.JOIN);
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            return crit.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.flush();
        		session.clear();
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	@Override
	public List<Remito> listarRemitosPorId(Long[] listaIds, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();

//			String consulta = "Select distinct re From Remito re " +
//					" join fetch re.detalles " +
//					" join fetch re.empresa " +
//					" join fetch re.empresa.direccion " +
//					" join fetch re.empresa.afipCondIva " +
//					" where re.id IN ("+listaIds+") ";
			
			//List<Remito> remitos = (List<Remito>)session.createQuery(consulta).list(); 
			
			Criteria crit = session.createCriteria(getClaseModelo());
	        crit.add(Restrictions.in("id", listaIds));

        	if(clienteAsp != null){
        		crit.add(Restrictions.eq("clienteAsp", clienteAsp));
        	}
        	crit.setFetchMode("detalles", FetchMode.JOIN);
        	crit.setFetchMode("empresa.direccion", FetchMode.JOIN);
        	crit.setFetchMode("empresa.afipCondIva", FetchMode.JOIN);
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			List<Remito> remitos = (List<Remito>)crit.list(); 
			
            return remitos;
            
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar ", hibernateException);
	        return null;
        }finally{
        	try{
        		session.flush();
        		session.clear();
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
//	@Override
//	public List<Remito> listarRemitosPopup(String val,String codigoContenedor, ClienteAsp cliente) {
//		Session session = null;
//        try {        	
//        	//obtenemos una sesión
//			session = getSession();
//        	Criteria c = session.createCriteria(getClaseModelo());
//        	c.createCriteria("tipoRemito", "te");
//        	//filtro value
//        	c.add(Restrictions.eq("te.contenedor", false));
//        	if(val!=null){        		
//        		c.add(Restrictions.ilike("te.descripcion", val+"%"));        	
//        	}
//        	if(codigoContenedor != null && !"".equals(codigoContenedor)){
//        		c.createCriteria("contenedor").add(Restrictions.eq("codigo", codigoContenedor));
//        	}
//        	if(cliente != null){
//	        	//filtro cliente
//	        	c.add(Restrictions.eq("clienteAsp", cliente));
//        	}
//        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//        	return c.list();
//        } catch (HibernateException hibernateException) {
//        	logger.error("No se pudo listar los remitos.", hibernateException);
//	        return null;
//        }finally{
//        	try{
//        		session.close();
//        	}catch(Exception e){
//        		logger.error("No se pudo cerrar la sesión", e);
//        	}
//        }
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Remito verificarExistenteEnSerie(Long numero, String codigoSerie,ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
	        
	        crit.add(Restrictions.eq("clienteAsp", clienteAsp));	        	
	        crit.add(Restrictions.eq("numero", numero));
	        crit.createCriteria("serie").add(Restrictions.eq("codigo", codigoSerie));
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Remito> result = crit.list(); 
        	if(result.size() == 1){
        		return result.get(0);
        	}
            return null; 
        } catch (HibernateException hibernateException) {
        	logger.error("no se pudo verificar existente de remito", hibernateException);
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
	public Remito verificarExistente(Remito remito){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(remito!=null){
	        	if(remito.getClienteAsp() !=null)
	        		crit.add(Restrictions.eq("clienteAsp", remito.getClienteAsp()));	        	
	        	if(remito.getNumero() !=null && remito.getNumero()!=0)
	        		crit.add(Restrictions.eq("numero", remito.getNumero()));	      
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Remito> result = crit.list(); 
        	if(result.size() == 1){
        		return result.get(0);
        	}
            return null; 
        } catch (HibernateException hibernateException) {
        	logger.error("no se pudo verificar existente de remito", hibernateException);
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
	public String traerUltNumeroPorSerie(Serie serie, ClienteAsp cliente){
		String proximoCodigo = null;
		Session session = null;
		String result = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(cliente!=null){
	        	crit.add(Restrictions.eq("clienteAsp", cliente));
	        	crit.add(Restrictions.eq("serie", serie));
	        	crit.setProjection(Projections.max("numeroSinPrefijo"));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
        	String ultimoNumero = (String)crit.uniqueResult();
        	
        	if(ultimoNumero != null && ultimoNumero.length()>0){
        		Long proximoCodigoLong = parseLongCodigo(ultimoNumero) + 1L;
	    		StringBuffer aux = new StringBuffer("");
	    		proximoCodigo = String.valueOf(proximoCodigoLong);
	    		for (int i = 0; i<(ultimoNumero.length() - proximoCodigo.length()); i++){
	    			aux.append("0");
	    		}
	    		aux.append(proximoCodigo);
	    		proximoCodigo = aux.toString();
        	}
        	else{
	    		proximoCodigo = "00000001";
	    	}
        	result = proximoCodigo;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo obtener el ultimo numero ", hibernateException);
	        result = null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
        return result;
	}
	
	@Override
	public List<Remito> listarRemitoPopup(String val,String clienteCodigo, ClienteAsp clienteAsp) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	if(clienteCodigo!=null && !clienteCodigo.equalsIgnoreCase("") 
        			&& !clienteCodigo.equalsIgnoreCase("null"))
        	{
        		crit.createCriteria("clienteEmp", "cli");
        		crit.add(Restrictions.eq("cli.codigo", clienteCodigo));
        	}
        	else if(clienteCodigo!=null && clienteCodigo.equalsIgnoreCase("null"))
        	{
        		crit.add(Restrictions.sqlRestriction("{alias}.clienteEmp_id is null"));
        		
        	}
        	crit.add(Restrictions.eq("clienteAsp", clienteAsp));
        	
        	crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
        	
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
	public Remito getByCodigo(Long codigo, ClienteEmp clienteEmp, ClienteAsp clienteAsp) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("clienteEmp", clienteEmp));
        	crit.add(Restrictions.eq("clienteAsp", clienteAsp));
        	crit.add(Restrictions.eq("id", codigo));
        	crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
        	List<Remito> candidatos = crit.list();
        	if(candidatos.size()>0)return candidatos.get(0);
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
	
	private Long parseLongCodigo(String codigo){
		Long result= null;
		//si el codigo es distinto de vacio o null
		if(codigo!=null && codigo.length()>0){
			//cuenta el primer digito diferente de 0
			int cont = 0;
			while(codigo.substring( cont, cont).equals("0")){
				cont++;
			}
			//si el codigo esta formado solo por 0
			if(cont == codigo.length()-1){
				result = new Long(0);
			}else{
				//devuelve el Integer formado por el substring desde el cont hasta el final del codigo
				result = Long.parseLong(codigo.substring(cont));
			}
		}else{
			result = new Long(0);
		}
		return result;
	}
	
	@Override
	public Integer contarObtenerPor(Remito remito, ClienteAsp clienteAsp) {
		
		Session session = null;
		Integer result = null;
		
		try {
	    	//obtenemos una sesión
	    	session = getSession();
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.setProjection(Projections.rowCount());
	        
	        if(remito!=null){
	        	
        		if(remito.getCodigoEmpresa()!=null && !"".equals(remito.getCodigoEmpresa()))
        			crit.createCriteria("empresa", "emp").add(Restrictions.eq("emp.codigo", remito.getCodigoEmpresa()));
        		if(remito.getCodigoSucursal()!=null && !"".equals(remito.getCodigoSucursal()))
        			crit.createCriteria("sucursal", "suc").add(Restrictions.eq("suc.codigo", remito.getCodigoSucursal()));
        		if(remito.getCodigoTransporte()!=null && !"".equals(remito.getCodigoTransporte()))
        			crit.createCriteria("transporte", "tran").add(Restrictions.eq("tran.codigo", Integer.valueOf(remito.getCodigoTransporte())));
        		if(remito.getCodigoCliente()!=null && !"".equals(remito.getCodigoCliente()))
        			crit.createCriteria("clienteEmp", "cli").add(Restrictions.eq("cli.codigo", remito.getCodigoCliente()));
        		if(remito.getCodigoDepositoOrigen()!=null && !"".equals(remito.getCodigoDepositoOrigen()))
        			crit.createCriteria("depositoOrigen", "depOri").add(Restrictions.eq("depOri.codigo", remito.getCodigoDepositoOrigen()));
        		if(remito.getCodigoSerie()!=null && !"".equals(remito.getCodigoSerie()))
        			crit.createCriteria("serie", "ser").add(Restrictions.eq("ser.codigo", remito.getCodigoSerie()));
        		if(remito.getEstado() !=null && !"".equals(remito.getEstado()) && !"Seleccione un Estado".equals(remito.getEstado()))
        			crit.add(Restrictions.eq("estado", remito.getEstado()));
        		if(remito.getTipoRemito() !=null && !"".equals(remito.getTipoRemito()) && !"Todos".equals(remito.getTipoRemito()))
        			crit.add(Restrictions.eq("tipoRemito", remito.getTipoRemito()));
        		if(remito.getFechaDesde() != null && !"".equals(remito.getFechaDesde()))
        			crit.add(Restrictions.ge("fechaEmision", remito.getFechaDesde()));
        		if(remito.getFechaHasta() != null && !"".equals(remito.getFechaHasta()))
        			crit.add(Restrictions.le("fechaEmision", remito.getFechaHasta()));
        		if(remito.getNumeroDesde() != null && !"".equals(remito.getNumeroDesde()))
        			crit.add(Restrictions.ge("numero", parseLongCodigo(remito.getNumeroDesde())));
        		if(remito.getNumeroHasta() != null && !"".equals(remito.getNumeroHasta()))
        			crit.add(Restrictions.le("numero", parseLongCodigo(remito.getNumeroHasta())));
        		if(remito.getNumRequerimiento()!=null && !"".equals(remito.getNumRequerimiento()))
        			crit.add(Restrictions.eq("numRequerimiento", remito.getNumRequerimiento()));
        	}
        	if(clienteAsp != null){
        		crit.createCriteria("clienteAsp", "cliAsp");
        		crit.add(Restrictions.eq("cliAsp.id", clienteAsp.getId()));
        	}

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        
        result = ((Integer)crit.list().get(0));
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
	
	private List<Long> obtenerIDsRemitosFiltrados(Remito remito, ClienteAsp clienteAsp,
			String fieldOrder, String sortOrder,Integer numeroPagina,Integer tamañoPagina) {
		
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.id());
        	
        	if(remito!=null){
        		
        		if(remito.getCodigoEmpresa()!=null && !"".equals(remito.getCodigoEmpresa()))
        			crit.createCriteria("empresa", "emp").add(Restrictions.eq("emp.codigo", remito.getCodigoEmpresa()));
        		if(remito.getCodigoSucursal()!=null && !"".equals(remito.getCodigoSucursal()))
        			crit.createCriteria("sucursal", "suc").add(Restrictions.eq("suc.codigo", remito.getCodigoSucursal()));
        		if(remito.getCodigoTransporte()!=null && !"".equals(remito.getCodigoTransporte()))
        			crit.createCriteria("transporte", "tran").add(Restrictions.eq("tran.codigo", Integer.valueOf(remito.getCodigoTransporte())));
        		if(remito.getCodigoCliente()!=null && !"".equals(remito.getCodigoCliente()))
        			crit.createCriteria("clienteEmp", "cli").add(Restrictions.eq("cli.codigo", remito.getCodigoCliente()));
        		if(remito.getCodigoDepositoOrigen()!=null && !"".equals(remito.getCodigoDepositoOrigen()))
        			crit.createCriteria("depositoOrigen", "depOri").add(Restrictions.eq("depOri.codigo", remito.getCodigoDepositoOrigen()));
        		if(remito.getCodigoSerie()!=null && !"".equals(remito.getCodigoSerie()))
        			crit.createCriteria("serie", "ser").add(Restrictions.eq("ser.codigo", remito.getCodigoSerie()));
        		if(remito.getEstado() !=null && !"".equals(remito.getEstado()) && !"Seleccione un Estado".equals(remito.getEstado()))
        			crit.add(Restrictions.eq("estado", remito.getEstado()));
        		if(remito.getTipoRemito() !=null && !"".equals(remito.getTipoRemito()) && !"Todos".equals(remito.getTipoRemito()))
        			crit.add(Restrictions.eq("tipoRemito", remito.getTipoRemito()));
        		if(remito.getFechaDesde() != null && !"".equals(remito.getFechaDesde()))
        			crit.add(Restrictions.ge("fechaEmision", remito.getFechaDesde()));
        		if(remito.getFechaHasta() != null && !"".equals(remito.getFechaHasta()))
        			crit.add(Restrictions.le("fechaEmision", remito.getFechaHasta()));
        		if(remito.getNumeroDesde() != null && !"".equals(remito.getNumeroDesde()))
        			crit.add(Restrictions.ge("numero", parseLongCodigo(remito.getNumeroDesde())));
        		if(remito.getNumeroHasta() != null && !"".equals(remito.getNumeroHasta()))
        			crit.add(Restrictions.le("numero", parseLongCodigo(remito.getNumeroHasta())));
        		if(remito.getNumRequerimiento()!=null && !"".equals(remito.getNumRequerimiento()))
        			crit.add(Restrictions.eq("numRequerimiento", remito.getNumRequerimiento()));
        	}
        	if(clienteAsp != null){
        		crit.createCriteria("clienteAsp", "cliAsp");
        		crit.add(Restrictions.eq("cliAsp.id", clienteAsp.getId()));
        	}
         	
         	//Ordenamos
        	if(sortOrder!=null && sortOrder.length()>0 &&
            	fieldOrder!=null && fieldOrder.length()>0){
            		
        			String fieldOrdenar = "";
            		String fieldOrdenar2 = "";
            		
            		if("fecha".equals(fieldOrder)){
            			fieldOrdenar = "fechaEmision";
            			fieldOrdenar2 = "id";
            		}
            		
            		if("comprobante".equals(fieldOrder))
            			fieldOrdenar = "numero";
            		
            		if("tipo".equals(fieldOrder))
            			fieldOrdenar = "tipoRemito";
            		
            		if("empresa".equals(fieldOrder)){
            			crit.createCriteria("empresa", "emp");
            			fieldOrdenar = "emp.codigo";
            		}
            		
            		if("sucursal".equals(fieldOrder)){
            			crit.createCriteria("sucursal", "suc");
            			fieldOrdenar = "suc.codigo";
            		}
            		
            		if("destino".equals(fieldOrder)){
            			crit.createCriteria("depositoOrigen", "dep");
            			fieldOrdenar = "dep.codigo";
            		}
            		
            		if("estado".equals(fieldOrder))
            			fieldOrdenar = "estado";

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
	public List<Remito> obtenerPor(Remito remito, ClienteAsp clienteAsp,
				String fieldOrder, String sortOrder,Integer numeroPagina,Integer tamañoPagina) {
		Session session = null;
	    try {
	    	List<Long> ids = obtenerIDsRemitosFiltrados(remito, clienteAsp, fieldOrder, sortOrder, numeroPagina, tamañoPagina);
	    	//obtenemos una sesión
			session = getSession();
			
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<Remito>();
			
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.add(Restrictions.in("id", ids));
	        
	        
//        	//Ordenamos
        	if(sortOrder!=null && sortOrder.length()>0 &&
                	fieldOrder!=null && fieldOrder.length()>0){
                		
            			String fieldOrdenar = "";
                		String fieldOrdenar2 = "";
                		
                		if("fecha".equals(fieldOrder)){
                			fieldOrdenar = "fechaEmision";
                			fieldOrdenar2 = "id";
                		}
                		
                		if("comprobante".equals(fieldOrder))
                			fieldOrdenar = "numero";
                		
                		if("tipo".equals(fieldOrder))
                			fieldOrdenar = "tipoRemito";
                		
                		if("empresa".equals(fieldOrder)){
                			crit.createCriteria("empresa", "emp");
                			fieldOrdenar = "emp.codigo";
                		}
                		
                		if("sucursal".equals(fieldOrder)){
                			crit.createCriteria("sucursal", "suc");
                			fieldOrdenar = "suc.codigo";
                		}
                		
                		if("destino".equals(fieldOrder)){
                    		crit.createCriteria("depositoOrigen", "dep");
                			fieldOrdenar = "dep.codigo";
                		}
                		
                		if("estado".equals(fieldOrder))
                			fieldOrdenar = "estado";
                		
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
	
	
}