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
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.FacturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LoteFacturacionService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PreFacturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PreFacturaService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.jerarquias.hibernate.ConceptoOperacionClienteServiceImp;
import com.security.accesoDatos.jerarquias.interfaz.ConceptoOperacionClienteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Factura;
import com.security.modelo.configuraciongeneral.FacturaDetalle;
import com.security.modelo.configuraciongeneral.LoteFacturacion;
import com.security.modelo.configuraciongeneral.PreFactura;
import com.security.modelo.configuraciongeneral.PreFacturaDetalle;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.utils.DateUtil;

/**
 * @author Victor Kenis
 *
 */
@Component
public class LoteFacturacionServiceImp extends GestorHibernate<LoteFacturacion> implements LoteFacturacionService {
	private static Logger logger=Logger.getLogger(LoteFacturacionServiceImp.class);
	private PreFacturaService preFacturaService;
	private PreFacturaDetalleService preFacturaDetalleService;
	private FacturaService facturaService;
	private FacturaDetalleService facturaDetalleService;
	
	
	@Autowired
	public LoteFacturacionServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}
	
	@Autowired
	public void setPreFacturaService(PreFacturaService preFacturaService) {
		this.preFacturaService = preFacturaService;
	}
	
	@Autowired
	public void setPreFacturaDetalleService(PreFacturaDetalleService preFacturaDetalleService) {
		this.preFacturaDetalleService = preFacturaDetalleService;
	}
	
	@Autowired
	public void setFacturaService(FacturaService facturaService) {
		this.facturaService = facturaService;
	}
	
	@Autowired
	public void setFacturaDetalleService(FacturaDetalleService facturaDetalleService) {
		this.facturaDetalleService = facturaDetalleService;
	}
	
//	@Autowired
//	public void setConceptoOperacionClienteService(ConceptoOperacionClienteService conceptoOperacionClienteService) {
//		this.conceptoOperacionClienteService = conceptoOperacionClienteService;
//	}

	@Override
	public Class<LoteFacturacion> getClaseModelo() {
		return LoteFacturacion.class;
	}

	@Override
	public Boolean guardarLoteFacturacion(LoteFacturacion loteFacturacion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(loteFacturacion);
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
	public Boolean actualizarLoteFacturacion(LoteFacturacion loteFacturacion) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(loteFacturacion);
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
	
//	@Override
//	public Boolean guardarLoteFacturacionYDetalles(Set<LoteFacturacionDetalle> loteFacturacionDetalles, LoteFacturacion loteFacturacion) {
//		
//		Session session = null;
//		Transaction tx = null;
//		
//		try {
//			
//			// obtenemos una sesión
//			session = getSession();
//			// creamos la transacción
//			tx = session.getTransaction();
//			tx.begin();
//			//guardamos la loteFacturacion primero
//			//para que se cree el ID
//			session.save(loteFacturacion);
//			//hacemos commit a la transacción para que 
//			//se refresque la base de datos.
//
//			//Recorro los interchanges para actualizarlos
//			for(LoteFacturacionDetalle loteFacturacionDetalle:loteFacturacionDetalles){
//				try {
//					session.saveOrUpdate(loteFacturacionDetalle.getConceptoOperacionCliente());
//					loteFacturacionDetalle.setLoteFacturacion(loteFacturacion);
//				} catch (Exception e) {
//					logger.error(e.getMessage());
//				}
//			}
//
//			//ya con el id guardado
//			//le seteamos a la loteFacturacion la lista de detalles
//			loteFacturacion.setDetalles(loteFacturacionDetalles);
//			//tx.begin();
//			//Actualizamos la loteFacturacion con la lista de detalles
//			session.update(loteFacturacion);
//			// Comiteo
//			tx.commit();
//			return true;
//
//		} catch (RuntimeException e) {
//			// si ocurre algún error intentamos hacer rollback
//			if (tx != null && tx.isActive()) {
//				try {
//					tx.rollback();
//					return false;
//				} catch (HibernateException e1) {
//					logger.error("no se pudo hacer rollback", e1);
//				}
//				logger.error("no se pudo guardar", e);
//				return false;
//			}
//			return true;
//
//		} finally {
//			try {
//				session.close();
//			} catch (Exception e) {
//				logger.error("No se pudo cerrar la sesión", e);
//			}
//		}
//	}

//	@Override
//	public synchronized Boolean actualizarLoteFacturacionYDetalles(Set<LoteFacturacionDetalle> loteFacturacionDetallesViejos, LoteFacturacion loteFacturacion) {
//
//		Session session = null;
//		Transaction tx = null;
//		
//		try {
//				
//			//obtenemos una sesión
//			session = getSession();
//			//creamos la transacción
//			tx = session.getTransaction();
//			tx.begin();
//			
//			//detalles a mantener
//			Collection<LoteFacturacionDetalle> detalles=new ArrayList<LoteFacturacionDetalle>(loteFacturacion.getDetalles());
//			
//			if(loteFacturacion.getId()==null || loteFacturacion.getId()==0){
//				loteFacturacion.setId(null);
//				loteFacturacion.getDetalles().clear();
//				session.save(loteFacturacion);
//			}else{
//				//session.update(loteFacturacion);
//				//borramos las detalles eliminadas
//				List<LoteFacturacionDetalle> lotes = new ArrayList<LoteFacturacionDetalle>(loteFacturacionDetalleService.listarLoteFacturacionDetallePorLoteFacturacion(loteFacturacion, loteFacturacion.getClienteAsp()));
//
//				for(LoteFacturacionDetalle det:lotes){
//					Boolean existe = false;
//					if(det.getId()!=null){
//						for(LoteFacturacionDetalle detMantener:detalles)
//						{
//							if(det.getId().equals(detMantener.getId()))
//							{
//								existe = true;
//							}
//							
//						}
//						if(existe==false)
//						{
//							if(det.getConceptoOperacionCliente().getTipoConcepto().equalsIgnoreCase("Mensual")){
//								session.delete(det.getConceptoOperacionCliente());
//							}
//							else{
//								det.getConceptoOperacionCliente().setAsignado(false);
//								session.update(det.getConceptoOperacionCliente());
//							}
//							session.delete(det);
//						}
//						//loteFacturacion.getDetalles().remove(det);
//					}
//				}
//				loteFacturacion.setDetalles(null);
//				session.update(loteFacturacion);
//				loteFacturacion.setDetalles(new HashSet<LoteFacturacionDetalle>());
//				//rearchivos agregadas
//				//detalles = CollectionUtils.subtract(detalles,loteFacturacion.getDetalles());
//			}
//			//guardamos las nuevas rearchivos
//			for(LoteFacturacionDetalle det : detalles)
//			{
//				det.setLoteFacturacion(loteFacturacion);	
//				session.saveOrUpdate(det);
//				loteFacturacion.getDetalles().add(det);
//				
//			}
//			//Reactualizo
//			detalles=new ArrayList<LoteFacturacionDetalle>(loteFacturacion.getDetalles());
//			for(LoteFacturacionDetalle det : detalles){
//				det.getConceptoOperacionCliente().setAsignado(true);
//				session.saveOrUpdate(det.getConceptoOperacionCliente());
//				session.update(det);
//				
//			}
//			session.update(loteFacturacion);
//			
//			tx.commit();
//			return true;
//
//		} catch (RuntimeException e) {
//			// si ocurre algún error intentamos hacer rollback
//			if (tx != null && tx.isActive()) {
//				try {
//					tx.rollback();
//					return false;
//				} catch (HibernateException e1) {
//					logger.error("no se pudo hacer rollback", e1);
//				}
//				logger.error("no se pudo guardar", e);
//				return false;
//			}
//			return true;
//
//		} finally {
//			try {
//				session.close();
//			} catch (Exception e) {
//				logger.error("No se pudo cerrar la sesión", e);
//			}
//		}
//	}
	
	@Override
	public Boolean actualizarLoteFacturacionList(List<LoteFacturacion> listLoteFacturacions)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(LoteFacturacion actualizar : listLoteFacturacions){
    			session.update(actualizar);
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo actualizar la coleccion de loteFacturacions ", hibernateException);
	        return false;
        }
   }
	
	@Override
	public Boolean guardarLoteFacturacionList(List<LoteFacturacion> listLoteFacturacions)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(LoteFacturacion actualizar : listLoteFacturacions){
    			session.save(actualizar);
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo grabar la coleccion de loteFacturacions ", hibernateException);
	        return false;
        }
   }
	
	@Override
	public Boolean eliminarLoteFacturacion(LoteFacturacion loteFacturacion) {
		Session session = null;
		Transaction tx = null;
		List<PreFactura> listaPreFacturas = new ArrayList<PreFactura>();
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			
			//Se traen los detalles del Lote
			listaPreFacturas = (List<PreFactura>) preFacturaService.listarPreFacturasPorLoteFacturacion(loteFacturacion, loteFacturacion.getClienteAsp());
			if(listaPreFacturas != null && listaPreFacturas.size() > 0)
			{
				//Se traen los detalles de cada Prefactura
				for(int i = 0;i<listaPreFacturas.size();i++){
					List<PreFacturaDetalle> detalles = preFacturaDetalleService.listarPreFacturaDetallesPorPreFactura(listaPreFacturas.get(i).getId(), listaPreFacturas.get(i).getClienteAsp());
					for(PreFacturaDetalle detalle:detalles){
						ConceptoOperacionClienteService service = new ConceptoOperacionClienteServiceImp(HibernateControl.getInstance());
						List<ConceptoOperacionCliente> listaConceptosAModificar = service.listarConceptosPorPreFacturaDetalle(detalle.getId(), loteFacturacion.getClienteAsp());
						if(listaConceptosAModificar!=null && listaConceptosAModificar.size()>0){
							for(ConceptoOperacionCliente concepto:listaConceptosAModificar){
								if(concepto.getTipoConcepto().equalsIgnoreCase("Mensual"))
								{
									session.delete(concepto);
								}
								else
								{
									concepto.setEstado("Pendiente");
									concepto.setAsignado(false);
									concepto.setPreFacturaDetalle(null);
									session.update(concepto);
								}
							}
						}
						//Se borra uno por uno cada detalle de preFactura
						session.delete(detalle);
					}
					//Se borra la prefactura
					session.delete(listaPreFacturas.get(i));
				}
				
			}
			//borramos el lote
			session.delete(loteFacturacion);
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
	public LoteFacturacion getByNumero(Long numero, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("numero", numero));
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
        	
            return (LoteFacturacion) crit.uniqueResult();
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
//	public LoteFacturacion busquedaServlet(LoteFacturacion loteFacturacionBusqueda, ClienteAsp clienteAsp) {
//		Session session = null;
//        try {
//        	//obtenemos una sesión
//			session = getSession();
//        	Criteria crit = session.createCriteria(getClaseModelo());        	
//        	crit.createCriteria("clienteEmp", "cli");
//			crit.createCriteria("cli.empresa", "emp");
//        	
//        	if(loteFacturacionBusqueda!=null){
//        		if(loteFacturacionBusqueda.getCodigoDeposito()!=null && !"".equals(loteFacturacionBusqueda.getCodigoDeposito())){
//        			crit.createCriteria("posicion", "pos");
//                	crit.createCriteria("pos.estante", "est");
//                	crit.createCriteria("est.grupo", "grp");
//                	crit.createCriteria("grp.seccion", "sec");
//                	crit.createCriteria("sec.deposito", "dep");
//        			crit.add(Restrictions.eq("dep.codigo", loteFacturacionBusqueda.getCodigoDeposito()));
//        		}
//        		//codigo loteFacturacion
//        		if(loteFacturacionBusqueda.getNumero()!=null && loteFacturacionBusqueda.getNumero()!=0){
//        			crit.add(Restrictions.eq("numero", loteFacturacionBusqueda.getNumero()));
//        		}
//        		//codigo deposito actual
//        		if(loteFacturacionBusqueda.getCodigoDeposito()!=null && loteFacturacionBusqueda.getCodigoDeposito().length()>0){
//        			crit.createCriteria("depositoActual", "depAct");
//        			crit.add(Restrictions.eq("depAct", loteFacturacionBusqueda.getCodigoDeposito()));
//        		}
//        		if(loteFacturacionBusqueda.getCodigoEmpresa()!=null && loteFacturacionBusqueda.getCodigoEmpresa().length()>0){
//        			crit.add(Restrictions.eq("emp.codigo", loteFacturacionBusqueda.getCodigoEmpresa()));
//        		}
//        		if(loteFacturacionBusqueda.getCodigoTipoLoteFacturacion()!=null && loteFacturacionBusqueda.getCodigoTipoLoteFacturacion().length()>0){
//        			crit.createCriteria("tipoLoteFacturacion", "tipEle");
//        			crit.add(Restrictions.eq("tipEle", loteFacturacionBusqueda.getCodigoTipoLoteFacturacion()));
//        		}
//        		if(loteFacturacionBusqueda.getCodigoCliente()!=null && loteFacturacionBusqueda.getCodigoCliente().length()>0){
//        			crit.add(Restrictions.eq("cli.codigo", loteFacturacionBusqueda.getCodigoCliente()));
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
//            return (LoteFacturacion) crit.uniqueResult();
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
	 * Recupera los loteFacturacions que contienen los codigos indicados en la lista
	 */
	@SuppressWarnings("unchecked")
	public List<LoteFacturacion> getByNumeros(List<Long> numeros, ClienteAsp cliente){
		Session session = null;
		List<LoteFacturacion> result = null;
		@SuppressWarnings("rawtypes")
		ArrayList<LoteFacturacion> listaLoteFacturacionsOrdenados = new ArrayList(numeros.size());
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	Disjunction disjunction = Restrictions.disjunction();
        	for(Long numero : numeros){
        		disjunction.add(Restrictions.eq("numero" , numero));
        		listaLoteFacturacionsOrdenados.add(null);
        	}
        	crit.add(disjunction);
        	List<LoteFacturacion> listaLoteFacturacions = null;
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        		listaLoteFacturacions = (List<LoteFacturacion>)crit.list();        		
        	}else { 
        		listaLoteFacturacions = new ArrayList<LoteFacturacion>();
        	}
        	//ordenamos los loteFacturacions por el orden de la lista de codigos
        	
        	if(numeros.size () == listaLoteFacturacions.size()){        						
        		Long numRem = null;
        		int index = 0;
        		LoteFacturacion loteFacturacion = null;
        		for(int i = 0; i<listaLoteFacturacions.size(); i++){
        			loteFacturacion = listaLoteFacturacions.get(i);
        			numRem = loteFacturacion.getNumero();
        			index = numeros.indexOf(numRem);
        			listaLoteFacturacionsOrdenados.set(index, loteFacturacion);
        		}
        		listaLoteFacturacions = listaLoteFacturacionsOrdenados;
        	}else { 
        		listaLoteFacturacions = new ArrayList<LoteFacturacion>();
        	}
        	
        	
            result = listaLoteFacturacions;
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
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<LoteFacturacion> listarLoteFacturacionFiltradas(LoteFacturacion loteFacturacion, ClienteAsp clienteAsp){
//		Session session = null;
//        try {
//        	//obtenemos una sesión
//			session = getSession();
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	
//        	
//        	
//        	if(loteFacturacion!=null){
//        		
//        		if(loteFacturacion.getCodigoEmpresa()!=null && !"".equals(loteFacturacion.getCodigoEmpresa()))
//        			crit.createCriteria("empresa", "emp").add(Restrictions.eq("emp.codigo", loteFacturacion.getCodigoEmpresa()));
//        		if(loteFacturacion.getCodigoSucursal()!=null && !"".equals(loteFacturacion.getCodigoSucursal()))
//        			crit.createCriteria("sucursal", "suc").add(Restrictions.eq("suc.codigo", loteFacturacion.getCodigoSucursal()));
//        		if(loteFacturacion.getCodigoTransporte()!=null && !"".equals(loteFacturacion.getCodigoTransporte()))
//        			crit.createCriteria("transporte", "tran").add(Restrictions.eq("tran.codigo", Integer.valueOf(loteFacturacion.getCodigoTransporte())));
//        		if(loteFacturacion.getCodigoCliente()!=null && !"".equals(loteFacturacion.getCodigoCliente()))
//        			crit.createCriteria("clienteEmp", "cli").add(Restrictions.eq("cli.codigo", loteFacturacion.getCodigoCliente()));
//        		if(loteFacturacion.getCodigoDepositoOrigen()!=null && !"".equals(loteFacturacion.getCodigoDepositoOrigen()))
//        			crit.createCriteria("depositoOrigen", "depOri").add(Restrictions.eq("depOri.codigo", loteFacturacion.getCodigoDepositoOrigen()));
//        		if(loteFacturacion.getCodigoSerie()!=null && !"".equals(loteFacturacion.getCodigoSerie()))
//        			crit.createCriteria("serie", "ser").add(Restrictions.eq("ser.codigo", loteFacturacion.getCodigoSerie()));
//        		if(loteFacturacion.getEstado() !=null && !"".equals(loteFacturacion.getEstado()) && !"Seleccione un Estado".equals(loteFacturacion.getEstado()))
//        			crit.add(Restrictions.eq("estado", loteFacturacion.getEstado()));
//        		if(loteFacturacion.getTipoLoteFacturacion() !=null && !"".equals(loteFacturacion.getTipoLoteFacturacion()) && !"Todos".equals(loteFacturacion.getTipoLoteFacturacion()))
//        			crit.add(Restrictions.eq("tipoLoteFacturacion", loteFacturacion.getTipoLoteFacturacion()));
//        		if(loteFacturacion != null && loteFacturacion.getFechaDesde() != null && !"".equals(loteFacturacion.getFechaDesde()))
//        			crit.add(Restrictions.ge("fechaEmision", loteFacturacion.getFechaDesde()));
//        		if(loteFacturacion != null && loteFacturacion.getFechaHasta() != null && !"".equals(loteFacturacion.getFechaHasta()))
//        			crit.add(Restrictions.le("fechaEmision", loteFacturacion.getFechaHasta()));
//        		if(loteFacturacion != null && loteFacturacion.getNumeroDesde() != null && !"".equals(loteFacturacion.getNumeroDesde()))
//        			crit.add(Restrictions.ge("numero", parseLongCodigo(loteFacturacion.getNumeroDesde())));
//        		if(loteFacturacion != null && loteFacturacion.getNumeroHasta() != null && !"".equals(loteFacturacion.getNumeroHasta()))
//        			crit.add(Restrictions.le("numero", parseLongCodigo(loteFacturacion.getNumeroHasta())));
//        	}
//        	if(clienteAsp != null){
//        		crit.createCriteria("clienteAsp", "cliAsp");
//        		crit.add(Restrictions.eq("cliAsp.id", clienteAsp.getId()));
//        	}
//        	crit.addOrder(Order.asc("fechaEmision"));
//        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//        	return crit.list();
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
//    }
	
	@Override
	public List<LoteFacturacion> listarLoteFacturacionsPorId(List<LoteFacturacion> loteFacturacions, ClienteAsp clienteAsp){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	if(loteFacturacions!=null && loteFacturacions.size()>0)
				{
					Disjunction disjunction = Restrictions.disjunction();
		        	for(LoteFacturacion loteFacturacion : loteFacturacions){
		        			disjunction.add(Restrictions.eq("id", loteFacturacion.getId()));
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
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
    }
	
	
//	@Override
//	public List<LoteFacturacion> listarLoteFacturacionsPopup(String val,String codigoContenedor, ClienteAsp cliente) {
//		Session session = null;
//        try {        	
//        	//obtenemos una sesión
//			session = getSession();
//        	Criteria c = session.createCriteria(getClaseModelo());
//        	c.createCriteria("tipoLoteFacturacion", "te");
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
//        	logger.error("No se pudo listar los loteFacturacions.", hibernateException);
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
	public Long verificarExistentePeriodoPosterior(LoteFacturacion loteFacturacion, ClienteAsp clienteAsp){
		Session session = null;
		Long result = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.rowCount());
	        
	        crit.add(Restrictions.eq("clienteAsp", clienteAsp));
	   
        	crit.add(Restrictions.eq("estado", "Pendiente"));

	        crit.add(Restrictions.eq("empresa", loteFacturacion.getEmpresa()));
	   
	        crit.add(Restrictions.gt("periodo", loteFacturacion.getPeriodo()));
	          
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	result = ((Long)crit.list().get(0));
        	return  result;
        } catch (HibernateException hibernateException) {
        	logger.error("no se pudo verificar existente de loteFacturacion", hibernateException);
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
	public LoteFacturacion verificarExistenteMismoPeriodo(LoteFacturacion loteFacturacion, ClienteAsp clienteAsp){
		Session session = null;

        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	crit.createCriteria("empresa", "emp");
	        
	        crit.add(Restrictions.eq("clienteAsp", clienteAsp));
	   
        	crit.add(Restrictions.eq("estado", "Pendiente"));

	        crit.add(Restrictions.eq("emp.codigo", loteFacturacion.getCodigoEmpresa()));
	   
	        crit.add(Restrictions.eq("periodo", loteFacturacion.getPeriodo()));
	          
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
           	List<LoteFacturacion> result=crit.list();
        	if(result.size()>=1)
				return result.get(0);
			return null;
        } catch (HibernateException hibernateException) {
        	logger.error("no se pudo verificar existente de loteFacturacion", hibernateException);
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
	public LoteFacturacion verificarExistente(LoteFacturacion loteFacturacion){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(loteFacturacion!=null){
	        	if(loteFacturacion.getClienteAsp() !=null)
	        		crit.add(Restrictions.eq("clienteAsp", loteFacturacion.getClienteAsp()));	        	
	        	if(loteFacturacion.getNumero() !=null && loteFacturacion.getNumero()!=0)
	        		crit.add(Restrictions.eq("numero", loteFacturacion.getNumero()));	      
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<LoteFacturacion> result = crit.list(); 
        	if(result.size() == 1){
        		return result.get(0);
        	}
            return null; 
        } catch (HibernateException hibernateException) {
        	logger.error("no se pudo verificar existente de loteFacturacion", hibernateException);
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
	public Long traerUltNumero(LoteFacturacion lote, ClienteAsp cliente){

		Session session = null;
		Long result = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	if(cliente!=null){
	        	crit.add(Restrictions.eq("clienteAsp", cliente));
	        	crit.add(Restrictions.eq("empresa", lote.getEmpresa()));
	        	crit.add(Restrictions.eq("sucursal", lote.getSucursal()));
	        	crit.setProjection(Projections.max("numero"));
        	}
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	
        	Long ultimoNumero = (Long)crit.uniqueResult();
        	result = ultimoNumero;
        	if(result==null)
        		result = Long.valueOf(0);
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
	public List<LoteFacturacion> listarLoteFacturacionPopup(String val,String clienteCodigo, ClienteAsp clienteAsp) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("clienteEmp", "cli");
        	crit.add(Restrictions.eq("cli.codigo", clienteCodigo));
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
	public LoteFacturacion getByCodigo(Long codigo, ClienteEmp clienteEmp, ClienteAsp clienteAsp) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.eq("clienteEmp", clienteEmp));
        	crit.add(Restrictions.eq("clienteAsp", clienteAsp));
        	crit.add(Restrictions.eq("id", codigo));
        	crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY); 
        	List<LoteFacturacion> candidatos = crit.list();
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
	
	private List<Long> obtenerIDsLoteFacturacionFiltradas(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal, Date fechaDesde, Date fechaHasta,
			String estado, String fieldOrder, String sortOrder,Integer numeroPagina,Integer tamañoPagina) {
		
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.id());
	       
        	 crit.add(Restrictions.eq("clienteAsp", cliente));
 	        
         	if(codigoEmpresa!=null && !codigoEmpresa.isEmpty()){
         		crit.createCriteria("empresa", "emp");
         		crit.add(Restrictions.eq("emp.codigo", codigoEmpresa));
         	}
         	if(codigoSucursal!=null && !codigoSucursal.isEmpty()){
         		crit.createCriteria("sucursal", "suc");
         		crit.add(Restrictions.eq("suc.codigo", codigoSucursal));
         	}
         	if(fechaDesde!=null){
         		crit.add(Restrictions.ge("fechaRegistro", DateUtil.getDateFrom(fechaDesde)));
         	}
         	if(fechaHasta!=null){
         		crit.add(Restrictions.le("fechaRegistro", DateUtil.getDateTo(fechaHasta)));
         	}
         	if(estado!=null && !estado.isEmpty()){
         		crit.add(Restrictions.eq("estado", estado));
         	}
         	
         	//Ordenamos
        	if(sortOrder!=null && sortOrder.length()>0 &&
            	fieldOrder!=null && fieldOrder.length()>0){
            		
        			String fieldOrdenar = "";
            		String fieldOrdenar2 = "";
            		
            		if("id".equals(fieldOrder))
            		{
            			fieldOrdenar = "id";
            		}
            		if("estado".equals(fieldOrder)){
    					fieldOrdenar = "estado";
    				}
            		if("fechaRegistroStr".equals(fieldOrder)){
    					fieldOrdenar = "fechaRegistro";
    				}
            		if("numero".equals(fieldOrder)){
    					fieldOrdenar = "numero";
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
	
	@Override
	public Long contarObtenerPor(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal, Date fechaDesde, Date fechaHasta,
			String estado) {
		
		Session session = null;
		Long result = null;
		
	    try {
	    	//obtenemos una sesión
	    	session = getSession();
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.setProjection(Projections.rowCount());
	        
	        crit.add(Restrictions.eq("clienteAsp", cliente));
 	        
         	if(codigoEmpresa!=null && !codigoEmpresa.isEmpty()){
         		crit.createCriteria("empresa", "emp");
         		crit.add(Restrictions.eq("emp.codigo", codigoEmpresa));
         	}
         	if(codigoSucursal!=null && !codigoSucursal.isEmpty()){
         		crit.createCriteria("sucursal", "suc");
         		crit.add(Restrictions.eq("suc.codigo", codigoSucursal));
         	}
         	if(fechaDesde!=null){
         		crit.add(Restrictions.ge("fechaRegistro", DateUtil.getDateFrom(fechaDesde)));
         	}
         	if(fechaHasta!=null){
         		crit.add(Restrictions.le("fechaRegistro", DateUtil.getDateTo(fechaHasta)));
         	}
         	if(estado!=null && !estado.isEmpty()){
         		crit.add(Restrictions.eq("estado", estado));
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LoteFacturacion> obtenerPor(ClienteAsp cliente,
			String codigoEmpresa, String codigoSucursal, Date fechaDesde, Date fechaHasta,
			String estado, String fieldOrder, String sortOrder,Integer numeroPagina,Integer tamañoPagina) {
		Session session = null;
	    try {
	    	List<Long> ids = obtenerIDsLoteFacturacionFiltradas(cliente, codigoEmpresa, codigoSucursal, fechaDesde, fechaHasta, estado, fieldOrder, sortOrder, numeroPagina, tamañoPagina);
	    	//obtenemos una sesión
			session = getSession();
			
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<LoteFacturacion>();
			
	        Criteria crit = session.createCriteria(getClaseModelo());
	        crit.add(Restrictions.in("id", ids));
	        
//        	//Ordenamos
        	if(sortOrder!=null && sortOrder.length()>0 &&
            	fieldOrder!=null && fieldOrder.length()>0){
            		
        			String fieldOrdenar = "";
            		String fieldOrdenar2 = "";
            		
            		if("id".equals(fieldOrder))
            		{
            			fieldOrdenar = "id";
            		}
            		if("estado".equals(fieldOrder)){
    					fieldOrdenar = "estado";
    				}
            		if("fechaRegistroStr".equals(fieldOrder)){
    					fieldOrdenar = "fechaRegistro";
    				}
            		if("numero".equals(fieldOrder)){
    					fieldOrdenar = "numero";
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
	public Boolean guardarActualizarLoteFacturacionYDetalles(Set<PreFactura> loteFacturacionDetallesViejos, LoteFacturacion loteFacturacion,List<ConceptoOperacionCliente> listaConceptos) {
		
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
			session.saveOrUpdate(loteFacturacion);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			
			//Recorro la lista de conceptos para actualizarlos
			if(listaConceptos!=null){
				for(ConceptoOperacionCliente concepto:listaConceptos){
					session.saveOrUpdate(concepto);
				}
			}
			

			//Recorro los interchanges para borrar los viejos
			if(loteFacturacionDetallesViejos!=null){
				for(PreFactura detalleViejo:loteFacturacionDetallesViejos){
					try {
							Boolean existe = false;
							for(PreFactura loteFacturacionDetalle:loteFacturacion.getDetalles()){
								if(loteFacturacionDetalle.getId()!=null 
										&& loteFacturacionDetalle.getId().longValue() == detalleViejo.getId().longValue()){
									existe = true;
									break;
								}
							}
							if(existe == false){
								List<PreFacturaDetalle> detalles = preFacturaDetalleService.listarPreFacturaDetallesPorPreFactura(detalleViejo.getId(), detalleViejo.getClienteAsp());
								for(PreFacturaDetalle detalle:detalles){
									ConceptoOperacionClienteService service = new ConceptoOperacionClienteServiceImp(HibernateControl.getInstance());
									List<ConceptoOperacionCliente> listaConceptosAModificar = service.listarConceptosPorPreFacturaDetalle(detalle.getId(), loteFacturacion.getClienteAsp());
									if(listaConceptosAModificar!=null && listaConceptosAModificar.size()>0){
										for(ConceptoOperacionCliente concepto:listaConceptosAModificar){
											concepto.setEstado("Pendiente");
											concepto.setAsignado(false);
											concepto.setPreFacturaDetalle(null);
											session.update(concepto);
										}
									}
									session.delete(detalle);
								}
								session.delete(detalleViejo);
							}
						
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
			
			//Recorro la lista de nuevos detalles para guardarlos o actualizarlos
			if(loteFacturacion.getDetalles()!=null){
				for(PreFactura loteFacturacionDetalle:loteFacturacion.getDetalles()){
					loteFacturacionDetalle.setLoteFacturacion(loteFacturacion);
					session.saveOrUpdate(loteFacturacionDetalle);
					for(PreFacturaDetalle preFacturaDetalle:loteFacturacionDetalle.getDetalles()){
						preFacturaDetalle.setPreFactura(loteFacturacionDetalle);
						session.saveOrUpdate(preFacturaDetalle);
						if(preFacturaDetalle.getListaConceptosAsociados()!=null 
								&& preFacturaDetalle.getListaConceptosAsociados().size()>0){
							for(ConceptoOperacionCliente concepto:preFacturaDetalle.getListaConceptosAsociados()){
								if(concepto.getId()!=null || (concepto.getTipoConcepto() != null && concepto.getTipoConcepto().equalsIgnoreCase("Mensual"))){
									concepto.setPreFacturaDetalle(preFacturaDetalle);
									session.saveOrUpdate(concepto);
								}
							}
						}
							
					}
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
	public Boolean facturarLoteFacturacion(LoteFacturacion loteFacturacion,List<Factura> listaFacturas,Serie serieA,Serie serieB) {
		
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
			session.saveOrUpdate(loteFacturacion);
			//hacemos commit a la transacción para que 
			//se refresque la base de datos.
			
			if(serieA!=null)
				session.update(serieA);
			
			if(serieB!=null)
				session.update(serieB);
			
			//Recorro las facturas generadas para guardarlas
			if(listaFacturas!=null && listaFacturas.size()>0){
				for(Factura factura : listaFacturas){
					session.saveOrUpdate(factura);
					for(FacturaDetalle detalle : factura.getDetallesFactura()){
						detalle.setFactura(factura);
						session.saveOrUpdate(detalle);
					}
				}
			}
			
			ConceptoOperacionClienteService service = new ConceptoOperacionClienteServiceImp(HibernateControl.getInstance());
			//Recorro la lista de nuevos detalles para guardarlos o actualizarlos
			if (loteFacturacion.getDetalles() != null) {
				for (PreFactura preFactura : loteFacturacion.getDetalles()) {
					preFactura.setLoteFacturacion(loteFacturacion);
					session.saveOrUpdate(preFactura);
					for (PreFacturaDetalle preFacturaDetalle : preFactura.getDetalles()) {
						
						List<ConceptoOperacionCliente> listaConceptosAModificar = service.listarConceptosPorPreFacturaDetalle(preFacturaDetalle.getId(), loteFacturacion.getClienteAsp());
						if(listaConceptosAModificar!=null && listaConceptosAModificar.size()>0){
							
							for(ConceptoOperacionCliente concepto:listaConceptosAModificar){
								concepto.setEstado("Facturado");
								concepto.setAsignado(true);
								concepto.setFactura(preFacturaDetalle.getFactura());
								session.update(concepto);
							}
						}
						preFacturaDetalle.setPreFactura(preFactura);
						session.saveOrUpdate(preFacturaDetalle);
					}

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
}