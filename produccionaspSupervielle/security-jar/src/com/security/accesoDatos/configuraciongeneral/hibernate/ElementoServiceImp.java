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
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.ElementoService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.LecturaService;
import com.security.accesoDatos.configuraciongeneral.interfaz.PosicionService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.exceptions.BasaException;
import com.security.exceptions.ClienteAspNullException;
import com.security.exceptions.ElementoDepositoLocalDistintoDepositoDestinoReposicionamientoException;
import com.security.exceptions.PosicionDestinoNullEnReposicionamientoException;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Deposito;
import com.security.modelo.configuraciongeneral.Elemento;
import com.security.modelo.configuraciongeneral.ElementoHistorico;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.Modulo;
import com.security.modelo.configuraciongeneral.Movimiento;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Referencia;
import com.security.modelo.configuraciongeneral.TipoElemento;
import com.security.modelo.seguridad.User;
import com.security.utils.Constantes;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class ElementoServiceImp extends GestorHibernate<Elemento> implements ElementoService {
	private static Logger logger=Logger.getLogger(ElementoServiceImp.class);
	private PosicionService posicionService;
	private LecturaService lecturaService;
	private LecturaDetalleService lecturaDetalleService;
	
	@Autowired
	public void setPosicionService(PosicionService posicionService) {
		this.posicionService = posicionService;
	}
	@Autowired
	public void setLecturaService(LecturaService lecturaService) {
		this.lecturaService = lecturaService;
	}
	@Autowired
	public void setLecturaDetalleService(LecturaDetalleService lecturaDetalleService) {
		this.lecturaDetalleService = lecturaDetalleService;
	}
	
	@Autowired
	public ElementoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<Elemento> getClaseModelo() {
		return Elemento.class;
	}

	@Override
	public synchronized Boolean guardarElemento(Elemento elemento) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(elemento);
			//Lineas agregadas para el historico
			session.save(registrarHistoricoElementos("MS001ELE", elemento));
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
	public synchronized Boolean actualizarElemento(Elemento elemento) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(elemento);
			//Lineas agregadas para el historico
			session.save(registrarHistoricoElementos("MS002ELE", elemento));
			////////////////////////////////////
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
	public synchronized Boolean actualizarElementoList(List<Elemento> listElementos)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(Elemento actualizar : listElementos){
    			session.update(actualizar);
    			//Lineas agregadas para el historico
    			session.save(registrarHistoricoElementos("MS002ELE", actualizar));
    			////////////////////////////////////
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo actualizar la coleccion de elementos ", hibernateException);
	        return false;
        }
   }
	
	@Override
	public synchronized Boolean guardarElementoList(List<Elemento> listElementos)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(Elemento actualizar : listElementos){
    			session.save(actualizar);
    			//Lineas agregadas para el historico
    			session.save(registrarHistoricoElementos("MS001ELE", actualizar));
    			////////////////////////////////////
    			session.flush();
    			session.clear();
        	}
        	tx.commit();
        	return true;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo grabar la coleccion de elementos ", hibernateException);
	        return false;
        }
   }
	
	@Override
	public synchronized Boolean eliminarElemento(Elemento elemento) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(elemento);
			//Lineas agregadas para el historico
			session.save(registrarHistoricoElementos("MS003ELE", elemento));
			////////////////////////////////////
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
	public Elemento getByCodigo(String codigo, ClienteAsp cliente) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	crit.add(Restrictions.eq("codigo", codigo));
//        	if(cliente != null){
//        		crit.add(Restrictions.eq("clienteAsp", cliente));
//        	}
        	
        	String consulta = "SELECT el FROM Elemento el WHERE el.codigo = '"+codigo+"'";
        	if(cliente != null)
        		consulta+= " AND el.clienteAsp.id = "+cliente.getId().longValue();
        	
        	Elemento elemento = (Elemento) session.createQuery(consulta).uniqueResult();	
        		
            return elemento;
            
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
	public Elemento getContenedorByCodigo(String codigo, String codigoCliente,Boolean limitarCliente, ClienteAsp clienteAsp, String tipoCodigoElemento) {
		return getContenedorByCodigo(codigo, codigoCliente, limitarCliente, clienteAsp, tipoCodigoElemento, null);
	}
	@Override
	public Elemento getContenedorByCodigo(String codigo, String codigoCliente,Boolean limitarCliente, ClienteAsp clienteAsp, String tipoCodigoElemento, Boolean cerrado) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession(); 
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	crit.createCriteria("tipoElemento","te").add(Restrictions.eq("te.contenedor", true));
//        	crit.add(Restrictions.eq("codigo", codigo));
//        	if(clienteAsp != null){
//        		crit.add(Restrictions.eq("clienteAsp", clienteAsp));
//        	}
//        	if(codigoCliente!=null && !codigoCliente.equals("")){
//        		DetachedCriteria subCritClienteEmp = DetachedCriteria.forClass(ClienteEmp.class, "cli");
//        		subCritClienteEmp.add(Restrictions.eq("cli.codigo", codigoCliente));        
//        		subCritClienteEmp.setProjection(Property.forName("cli.id"));    
//        		if(limitarCliente!=null && limitarCliente==true)
//        			crit.add(Property.forName("clienteEmp").in(subCritClienteEmp));
//        		else
//        			crit.add(Restrictions.or(Property.forName("clienteEmp").in(subCritClienteEmp),Restrictions.isNull("clienteEmp")));
//        	}
//        	if(tipoCodigoElemento!=null && !"".equals(tipoCodigoElemento)){
//        		//crit.add(Restrictions.eq("te.codigo", tipoCodigoElemento));
//        		crit.add(Restrictions.in("te.codigo", tipoCodigoElemento.split(",")));
//        	}
//        	if(cerrado!=null){
//        		if(!cerrado)
//        			crit.add(Restrictions.or(Restrictions.isNull("cerrado"), Restrictions.eq("cerrado", false)));
//        		else
//        			crit.add(Restrictions.eq("cerrado", true));
//        	}
			
			String consulta = "SELECT e FROM Elemento e " +
							" LEFT JOIN e.clienteEmp cli " +
							" where e.tipoElemento.contenedor = true " +
		        			" and e.codigo = '"+ codigo +"'";
			
			if(clienteAsp != null){
				consulta += " and e.clienteAsp.id ="+ clienteAsp.getId().longValue() +"";
			}
				
			if(codigoCliente!=null && !codigoCliente.equals("")){
				if(limitarCliente!=null && limitarCliente==true){
					consulta += " and cli.codigo ='"+ codigoCliente +"'";
				}else{
					consulta += " and (cli.codigo ='"+ codigoCliente +"' or e.clienteEmp is null) ";
				}
			}
			
			if(tipoCodigoElemento!=null && !"".equals(tipoCodigoElemento)){
				consulta += " and e.tipoElemento.codigo = '"+tipoCodigoElemento+"'";
        	}
			
        	if(cerrado!=null){
	    		if(!cerrado)
	    			consulta += " and (e.cerrado is Null or e.cerrado = false) ";
	    		else
	    			consulta += " and (e.cerrado = true) ";
        	}
			
			Query query=session.createQuery(consulta);
			
            return (Elemento) query.uniqueResult();
            
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
	public Elemento getProxContenedorDisponByTipoContenedor(String codigo, String codigoCliente,Boolean limitarCliente, ClienteAsp clienteAsp, String tipoCodigoElemento, Boolean cerrado) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession(); 
			
			String consulta = "SELECT Distinct e FROM Elemento e " +
							" LEFT JOIN e.clienteEmp cli " +
							" where e.tipoElemento.contenedor = true " ;
		        			//" and e.codigo >= '"+ codigo +"'";
			
			if(clienteAsp != null){
				consulta += " and e.clienteAsp.id ="+ clienteAsp.getId().longValue() +"";
			}
				
			if(codigoCliente!=null && !codigoCliente.equals("")){
				if(limitarCliente!=null && limitarCliente==true){
					consulta += " and cli.codigo ='"+ codigoCliente +"'";
				}else{
					consulta += " and (cli.codigo ='"+ codigoCliente +"' or e.clienteEmp is null) ";
				}
			}
			
			if(tipoCodigoElemento!=null && !"".equals(tipoCodigoElemento)){
				consulta += " and e.tipoElemento.codigo = '"+tipoCodigoElemento+"'";
        	}
			
        	if(cerrado!=null){
	    		if(!cerrado)
	    			consulta += " and (e.cerrado is Null or e.cerrado = false) ";
	    		else
	    			consulta += " and (e.cerrado = true) ";
        	}
        	
        	consulta += "order by e.codigo asc";
			
			Query query=session.createQuery(consulta);
			
			List<Elemento> lista = (List<Elemento>)query.list();
			
			if(lista!=null && lista.size()>0)
				return lista.get(0);
			else
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
	public Elemento getElementoByCodigo(String codigo, String codigoCliente, ClienteAsp clienteAsp,Boolean limitarCliente,Long soloLibresOmitirLoteId) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	crit.createCriteria("tipoElemento","te").add(Restrictions.eq("te.contenido", true));
//        	crit.add(Restrictions.eq("codigo", codigo));
//        	if(clienteAsp != null){
//        		crit.add(Restrictions.eq("clienteAsp", clienteAsp));
//        	}
//        	if(codigoCliente!=null && !codigoCliente.equals("")){
//        		DetachedCriteria subCritClienteEmp = DetachedCriteria.forClass(ClienteEmp.class, "cli");
//        		subCritClienteEmp.add(Restrictions.eq("cli.codigo", codigoCliente));        
//        		subCritClienteEmp.setProjection(Property.forName("cli.id"));    
//        		if(limitarCliente!=null && limitarCliente==true)
//        			crit.add(Property.forName("clienteEmp").in(subCritClienteEmp));
//        		else
//        			crit.add(Restrictions.or(Property.forName("clienteEmp").in(subCritClienteEmp),Restrictions.isNull("clienteEmp")));
//        	}
//        	if(soloLibresOmitirLoteId!=null){
//        		DetachedCriteria subCritReferencias = DetachedCriteria.forClass(Referencia.class, "ref");
//        		subCritReferencias.createCriteria("loteReferencia","loteReferencia");
//        		subCritReferencias.add(Restrictions.ne("loteReferencia.id", soloLibresOmitirLoteId));
//        		subCritReferencias.createCriteria("elemento","elemento");
//        		subCritReferencias.add(Restrictions.isNotNull("elemento"));
//        		subCritReferencias.setProjection(Property.forName("elemento.id"));    	
//        		crit.add(Property.forName("id").notIn(subCritReferencias));
//        	}
        	
        	
        	String consulta = "SELECT e FROM Elemento e " +
        			" INNER JOIN e.tipoElemento tip " +
        			" LEFT JOIN e.clienteEmp cli " +
        			" where tip.contenido = true " +
        			" and e.codigo = '"+ codigo +"'";

			if(clienteAsp != null){
			consulta += " and e.clienteAsp.id ="+ clienteAsp.getId().longValue() +"";
			}
			
			if(codigoCliente!=null && !codigoCliente.equals("")){
				if(limitarCliente!=null && limitarCliente==true){
					consulta += " and cli.codigo ='"+ codigoCliente +"'";
				}else{
					consulta += " and (cli.codigo ='"+ codigoCliente +"' or e.clienteEmp is Null) ";
				}
			}
			
			if(soloLibresOmitirLoteId!=null){
				consulta += " and e.id not in (select ref.elemento.id from Referencia ref where ref.loteReferencia.id <> "+ soloLibresOmitirLoteId +" and ref.elemento is not Null ) ";
			}
			
			Query query=session.createQuery(consulta);
			
			return (Elemento) query.uniqueResult();
			
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
	public Elemento getContenedorOElementoByCodigo(String codigo, String codigoCliente, ClienteAsp clienteAsp,Boolean limitarCliente,Long soloLibresOmitirLoteId) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
//        	Criteria crit = session.createCriteria(getClaseModelo());
//        	
//        	crit.add(Restrictions.eq("codigo", codigo));
//        	if(clienteAsp != null){
//        		crit.add(Restrictions.eq("clienteAsp", clienteAsp));
//        	}
//        	if(codigoCliente!=null && !codigoCliente.equals("")){
//        		DetachedCriteria subCritClienteEmp = DetachedCriteria.forClass(ClienteEmp.class, "cli");
//        		subCritClienteEmp.add(Restrictions.eq("cli.codigo", codigoCliente));        
//        		subCritClienteEmp.setProjection(Property.forName("cli.id"));    
//        		if(limitarCliente!=null && limitarCliente==true)
//        			crit.add(Property.forName("clienteEmp").in(subCritClienteEmp));
//        		else
//        			crit.add(Restrictions.or(Property.forName("clienteEmp").in(subCritClienteEmp),Restrictions.isNull("clienteEmp")));
//        	}
//        	if(soloLibresOmitirLoteId!=null){
//        		DetachedCriteria subCritReferencias = DetachedCriteria.forClass(Referencia.class, "ref");
//        		subCritReferencias.createCriteria("loteReferencia","loteReferencia");
//        		subCritReferencias.add(Restrictions.ne("loteReferencia.id", soloLibresOmitirLoteId));
//        		subCritReferencias.createCriteria("elemento","elemento");
//        		subCritReferencias.add(Restrictions.isNotNull("elemento"));
//        		subCritReferencias.setProjection(Property.forName("elemento.id"));    	
//        		crit.add(Property.forName("id").notIn(subCritReferencias));
//        	}
        	
        	String consulta = "SELECT elem FROM Elemento elem WHERE elem.codigo = '" + codigo + "' ";
        					   if(clienteAsp != null)
        							consulta += "AND elem.clienteAsp.id = " + clienteAsp.getId().longValue() + " ";
        					   if(limitarCliente != null && limitarCliente == true)
        					   {
        						   	consulta += "AND elem.clienteEmp.id IN (SELECT cli.id FROM ClienteEmp cli " +
        						   										  	"WHERE cli.codigo = '" + codigoCliente + "' ";
        					   }
        					   else
        					   {
        						   consulta += "AND (elem.clienteEmp.id IN (SELECT cli.id FROM ClienteEmp cli " +
        						   											"WHERE cli.codigo = '" + codigoCliente + "' ) " +
        						   									   "OR elem.clienteEmp IS NULL )";
        					   
        					   }
        					   if(soloLibresOmitirLoteId != null)
        					   {
        						   consulta += "AND elem.id NOT IN (SELECT ref.id FROM Referencia ref " +
        						   			   "WHERE ref.loteReferencia.id = " + soloLibresOmitirLoteId +
        						   			   "AND ref.elemento IS NOT NULL ";
        					   }
        					   
        	Query query = session.createQuery(consulta);
        			
        	
            return (Elemento) query.uniqueResult();
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
	
	// TODO: Generar el método obtenerProximoContenedorDisponible
	// 
	
	@Override
	public Elemento obtenerProximoElementoDisponible(ClienteAsp clienteAsp,String codigoCliente,List<String> codigosElementoUtilizados) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	
        	crit.createCriteria("tipoElemento","te").add(Restrictions.eq("te.contenido", true));
      
        	// crit.createCriteria("tipoElemento","te").add(Restrictions.eq("te.contenedor", true));
        	
	        	for(String codigo:codigosElementoUtilizados)
	        		crit.add(Restrictions.gt("codigo", codigo));
	        	crit.add(Restrictions.eq("clienteAsp", clienteAsp));
	        	if(codigoCliente!=null && !codigoCliente.equals("")){
	        		DetachedCriteria subCritClienteEmp = DetachedCriteria.forClass(ClienteEmp.class, "cli");
	        		subCritClienteEmp.add(Restrictions.eq("cli.codigo", codigoCliente));        
	        		subCritClienteEmp.setProjection(Property.forName("cli.id"));    
	        		crit.add(Restrictions.or(Property.forName("clienteEmp").in(subCritClienteEmp),Restrictions.isNull("clienteEmp")));
	        	}
	        	
	        	// Eliminado por pedido de Sancor. Supuestamente el autoincrementar del elemento verificaba si el siguiente elemento
	        	// se encontraba disponible o no. En caso que no, borrar automáticamente el dato de la casilla.
	        	// TODO: Descomentar este código y probarlo.
	        	
	//        	DetachedCriteria subCritReferencias = DetachedCriteria.forClass(Referencia.class, "ref");
	//    		subCritReferencias.createCriteria("elemento","elemento");
	//    		subCritReferencias.add(Restrictions.isNotNull("elemento"));
	//    		subCritReferencias.setProjection(Property.forName("elemento.id"));    	
	//    		crit.add(Property.forName("id").notIn(subCritReferencias));
	    		
	          	crit.addOrder(Order.asc("codigo"));
	        	crit.setMaxResults(1);

        	
        	return (Elemento) crit.uniqueResult();
        	   
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
	public Elemento busquedaServlet(Elemento elementoBusqueda, ClienteAsp clienteAsp) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());        	
        	
        	if(elementoBusqueda!=null){
        		if(elementoBusqueda.getCodigoDeposito()!=null && !"".equals(elementoBusqueda.getCodigoDeposito())){
        			crit.createCriteria("posicion", "pos");
                	crit.createCriteria("pos.estante", "est");
                	crit.createCriteria("est.grupo", "grp");
                	crit.createCriteria("grp.seccion", "sec");
                	crit.createCriteria("sec.deposito", "dep");
        			crit.add(Restrictions.eq("dep.codigo", elementoBusqueda.getCodigoDeposito()));
        		}
        		//codigo elemento
        		if(elementoBusqueda.getCodigo()!=null && elementoBusqueda.getCodigo().length()>0){
        			crit.add(Restrictions.eq("codigo", elementoBusqueda.getCodigo()));
        		}
        		//codigo deposito actual
        		if(elementoBusqueda.getCodigoDeposito()!=null && elementoBusqueda.getCodigoDeposito().length()>0){
        			crit.createCriteria("depositoActual", "depAct");
        			crit.add(Restrictions.eq("depAct", elementoBusqueda.getCodigoDeposito()));
        		}
        		//codigo contenedor
        		if(elementoBusqueda.getCodigoElemento()!=null && elementoBusqueda.getCodigoElemento().length()>0){
        			crit.createCriteria("contenedor", "con");
        			crit.add(Restrictions.ilike("con.codigo", elementoBusqueda.getCodigoElemento()+"%"));
        		}
        		if((elementoBusqueda.getCodigoEmpresa()!=null && elementoBusqueda.getCodigoEmpresa().length()>0) ||
        		(elementoBusqueda.getCodigoCliente()!=null && elementoBusqueda.getCodigoCliente().length()>0)){
        			crit.createCriteria("clienteEmp", "cli");
        			if(elementoBusqueda.getCodigoEmpresa()!=null && elementoBusqueda.getCodigoEmpresa().length()>0){
        				crit.createCriteria("cli.empresa", "emp");
        				crit.add(Restrictions.eq("emp.codigo", elementoBusqueda.getCodigoEmpresa()));
        			}
        			if(elementoBusqueda.getCodigoCliente()!=null && elementoBusqueda.getCodigoCliente().length()>0){
            			crit.add(Restrictions.eq("cli.codigo", elementoBusqueda.getCodigoCliente()));
            		}
        			
        		}
        		if(elementoBusqueda.getCodigoTipoElemento()!=null && elementoBusqueda.getCodigoTipoElemento().length()>0){
        			crit.createCriteria("tipoElemento", "tipEle");
        			crit.add(Restrictions.eq("tipEle.codigo", elementoBusqueda.getCodigoTipoElemento()));
        		}
        		
        		
        	}
        	
        	if(clienteAsp != null){
        		crit.add(Restrictions.eq("clienteAsp", clienteAsp));
        	}
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	Elemento result = (Elemento) crit.uniqueResult(); 
            return result;
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
	 * Recupera los elementos que contienen los codigos indicados en la lista
	 */
	@SuppressWarnings("unchecked")
	public List<Elemento> getByCodigos(List<String> codigos, ClienteAsp cliente){
		Session session = null;
		List<Elemento> result = null;
		@SuppressWarnings("rawtypes")
		ArrayList<Elemento> listaElementosOrdenados = new ArrayList(codigos.size());
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	Disjunction disjunction = Restrictions.disjunction();
        	for(String codigo : codigos){
        		disjunction.add(Restrictions.eq("codigo" , codigo));
        		listaElementosOrdenados.add(null);
        	}
        	crit.add(disjunction);
        	List<Elemento> listaElementos = null;
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        		listaElementos = (List<Elemento>)crit.list();        		
        	}else { 
        		listaElementos = new ArrayList<Elemento>();
        	}
        	//ordenamos los elementos por el orden de la lista de codigos
        	
        	if(codigos.size () == listaElementos.size()){        						
        		String codElem = null;
        		int index = 0;
        		Elemento elemento = null;
        		for(int i = 0; i<listaElementos.size(); i++){
        			elemento = listaElementos.get(i);
        			codElem = elemento.getCodigo();
        			index = codigos.indexOf(codElem);
        			listaElementosOrdenados.set(index, elemento);
        		}
        		listaElementos = listaElementosOrdenados;
        	}else { 
        		listaElementos = new ArrayList<Elemento>();
        	}
        	
        	
            result = listaElementos;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Elemento> listarElementoFiltradas(Elemento elemento, ClienteAsp clienteAsp){
		return listarElementoFiltradas(elemento, clienteAsp, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Elemento> listarElementoFiltradas(Elemento elemento, ClienteAsp clienteAsp, Boolean impresion){
		Session session = null;
        try {
        	List<Long> ids = obtenerIDsElementoFiltradas(elemento, clienteAsp, impresion);
        	
        	//obtenemos una sesión
			session = getSession();
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<Elemento>();
			
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.add(Restrictions.in("id", ids));
//        	//Ordenamos
        	if(elemento.getFieldOrder()!=null && elemento.getSortOrder()!=null){
        		String fieldOrdenar = "";
        		String fieldOrdenar2 = "";
        		
        		if("codigo".equals(elemento.getFieldOrder())){
        			fieldOrdenar = "codigo";
        		}
        		if("tipoElemento.descripcion".equals(elemento.getFieldOrder())){
        			crit.createCriteria("tipoElemento", "tipo");
        			fieldOrdenar = "tipo.descripcion";
        		}
        		if("depositoActual.descripcion".equals(elemento.getFieldOrder())){
        			crit.createCriteria("depositoActual", "dep");
        			fieldOrdenar = "dep.descripcion";
        		}
        		if("estado".equals(elemento.getFieldOrder()))
        			fieldOrdenar = "estado";

        	
        		if("1".equals(elemento.getSortOrder())){
        			if(!"".equals(fieldOrdenar))
    					crit.addOrder(Order.asc(fieldOrdenar));
        			if(!"".equals(fieldOrdenar2))
    					crit.addOrder(Order.asc(fieldOrdenar2));
    			}else if("2".equals(elemento.getSortOrder())){
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
	
	@Override
	public Integer contarElementoFiltradas(Elemento elemento, ClienteAsp clienteAsp){
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.rowCount());
        	
        	if(elemento!=null){
	    		
        		if(elemento.getCodigoEmpresa()!=null && !"".equals(elemento.getCodigoEmpresa())){
        			crit.createCriteria("depositoActual", "depAct");
    	    		crit.createCriteria("depAct.sucursal", "suc");
    	    		crit.createCriteria("suc.empresa", "emp");
        			crit.add(Restrictions.eq("emp.codigo", elemento.getCodigoEmpresa()));
        			if(elemento.getCodigoSucursal()!=null && !"".equals(elemento.getCodigoSucursal())){        			
                    	crit.add(Restrictions.eq("suc.codigo", elemento.getCodigoSucursal()));
                    	if(elemento.getCodigoDeposito()!=null && !"".equals(elemento.getCodigoDeposito())){
            				crit.add(Restrictions.eq("depAct.codigo", elemento.getCodigoDeposito()));
            			}
            		}
        		}
        		
        		if(elemento.getCodigoCliente()!=null && !"".equals(elemento.getCodigoCliente())){
        			
        			if(elemento.getCajasVacias()!=null && elemento.getCajasVacias())
        				crit.add(Restrictions.or(Restrictions.isNull("clienteEmp"), Restrictions.eq("clienteEmp", elemento.getClienteEmp())));
        			else{
        				crit.createCriteria("clienteEmp","clienteEmp");
        				crit.add(Restrictions.eq("clienteEmp.codigo", elemento.getCodigoCliente()));
        			}
        		}
        		
        		if(elemento.getCodigoTipoElemento() != null && !"".equals(elemento.getCodigoTipoElemento()))
        		{
        			crit.createCriteria("tipoElemento", "tipo");
        			crit.add(Restrictions.in("tipo.codigo", elemento.getCodigoTipoElemento().split(",")));
        		}
        		if(elemento.getCodigoElemento()!=null && !"".equals(elemento.getCodigoElemento())){
        			crit.createCriteria("contenedor", "con");
        			crit.add(Restrictions.ilike("con.codigo", elemento.getCodigoElemento()+"%"));
        		}
        	
        		if(elemento.getEstado() !=null && !"".equals(elemento.getEstado()) && !"Seleccione un Estado".equals(elemento.getEstado())){
        			crit.add(Restrictions.eq("estado", elemento.getEstado()));
        		}
        		if(elemento.getCodigoDesde() !=null && !"".equals(elemento.getCodigoDesde())){
        			crit.add(Restrictions.ge("codigo", elemento.getCodigoDesde()));
        		}
        		if(elemento.getCodigoHasta() !=null && !"".equals(elemento.getCodigoHasta())){
        			crit.add(Restrictions.le("codigo", elemento.getCodigoHasta()));
        		}
        		if(elemento.getCodigoLectura() !=null && !"".equals(elemento.getCodigoLectura())){
        			
        			Lectura lectura = lecturaService.obtenerPorCodigo(Long.parseLong(elemento.getCodigoLectura()), clienteAsp);
        			if(lectura!=null)
        			{
        				List<LecturaDetalle> listaDetalles = lecturaDetalleService.listarLecturaDetallePorLectura(lectura, clienteAsp);
        				if(listaDetalles!=null && listaDetalles.size()>0)
        				{
        					Disjunction disjunction = Restrictions.disjunction();
        		        	for(LecturaDetalle detalle : listaDetalles){
        		        		if(detalle.getElemento()!=null){
        		        			disjunction.add(Restrictions.eq("id", detalle.getElemento().getId()));
        		        		}
        		        	}
        		        	crit.add(disjunction);
        				}
        			}
        		}
        		
        		if(elemento.getSinReferencia()!=null && elemento.getSinReferencia()==true){
	        		DetachedCriteria subCritReferencias = DetachedCriteria.forClass(Referencia.class, "ref");
	        		subCritReferencias.add(Restrictions.isNotNull("elemento"));
	        		subCritReferencias.createCriteria("elemento","elemento");
	        		subCritReferencias.setProjection(Property.forName("elemento.id"));
	        		subCritReferencias.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        		crit.add(Property.forName("id").notIn(subCritReferencias));
	    		}	

        		
        	}
        	if(clienteAsp != null){
        		crit.add(Restrictions.eq("clienteAsp", clienteAsp));
        	}
        	
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
	
	private List<Long> obtenerIDsElementoFiltradas(Elemento elemento, ClienteAsp cliente, Boolean impresion){
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.id());
        	
        	if(elemento!=null){
	    		
        		if(elemento.getCodigoEmpresa()!=null && !"".equals(elemento.getCodigoEmpresa())){
        			crit.createCriteria("depositoActual", "depAct");
    	    		crit.createCriteria("depAct.sucursal", "suc");
    	    		crit.createCriteria("suc.empresa", "emp");
        			crit.add(Restrictions.eq("emp.codigo", elemento.getCodigoEmpresa()));
        			if(elemento.getCodigoSucursal()!=null && !"".equals(elemento.getCodigoSucursal())){        			
                    	crit.add(Restrictions.eq("suc.codigo", elemento.getCodigoSucursal()));
                    	if(elemento.getCodigoDeposito()!=null && !"".equals(elemento.getCodigoDeposito())){
            				crit.add(Restrictions.eq("depAct.codigo", elemento.getCodigoDeposito()));
            			}
            		}
        		}
        		
        		if(elemento.getCodigoCliente()!=null && !"".equals(elemento.getCodigoCliente())){
        			
        			if(elemento.getCajasVacias()!=null && elemento.getCajasVacias())
        				crit.add(Restrictions.or(Restrictions.isNull("clienteEmp"), Restrictions.eq("clienteEmp", elemento.getClienteEmp())));
        			else{
        				crit.createCriteria("clienteEmp","clienteEmp",CriteriaSpecification.LEFT_JOIN);
        				crit.add(Restrictions.eq("clienteEmp.codigo", elemento.getCodigoCliente()));
        			}
        		}
        		
        		if(elemento.getCodigoTipoElemento() != null && !"".equals(elemento.getCodigoTipoElemento()))
        		{
        			crit.createCriteria("tipoElemento", "tipo");
        			crit.add(Restrictions.in("tipo.codigo", elemento.getCodigoTipoElemento().split(",")));
        		}
        		if(elemento.getCodigoElemento()!=null && !"".equals(elemento.getCodigoElemento())){
        			crit.createCriteria("contenedor", "con");
        			crit.add(Restrictions.ilike("con.codigo", elemento.getCodigoElemento()+"%"));
        		}
        	
        		if(elemento.getEstado() !=null && !"".equals(elemento.getEstado()) && !"Seleccione un Estado".equals(elemento.getEstado())){
        			crit.add(Restrictions.eq("estado", elemento.getEstado()));
        		}
        		if(elemento.getCodigoDesde() !=null && !"".equals(elemento.getCodigoDesde())){
        			crit.add(Restrictions.ge("codigo", elemento.getCodigoDesde()));
        		}
        		if(elemento.getCodigoHasta() !=null && !"".equals(elemento.getCodigoHasta())){
        			crit.add(Restrictions.le("codigo", elemento.getCodigoHasta()));
        		}
        		if(elemento.getCodigoLectura() !=null && !"".equals(elemento.getCodigoLectura())){
        			
        			Lectura lectura = lecturaService.obtenerPorCodigo(Long.parseLong(elemento.getCodigoLectura()), cliente);
        			if(lectura!=null)
        			{
        				List<LecturaDetalle> listaDetalles = lecturaDetalleService.listarLecturaDetallePorLectura(lectura, cliente);
        				if(listaDetalles!=null && listaDetalles.size()>0)
        				{
        					Disjunction disjunction = Restrictions.disjunction();
        		        	for(LecturaDetalle detalle : listaDetalles){
        		        		if(detalle.getElemento()!=null){
        		        			disjunction.add(Restrictions.eq("id", detalle.getElemento().getId()));
        		        		}
        		        	}
        		        	crit.add(disjunction);
        				}
        			}
        		}
        		
        		if(elemento.getSinReferencia()!=null && elemento.getSinReferencia()==true){
	        		DetachedCriteria subCritReferencias = DetachedCriteria.forClass(Referencia.class, "ref");
	        		subCritReferencias.add(Restrictions.isNotNull("elemento"));
	        		subCritReferencias.createCriteria("elemento","elemento");
	        		subCritReferencias.setProjection(Property.forName("elemento.id"));
	        		subCritReferencias.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        		crit.add(Property.forName("id").notIn(subCritReferencias));
	    		}	

        		
        	}
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
        	
        	if(impresion == null || impresion == false){
//	        	//Ordenamos
	        	if(elemento.getFieldOrder()!=null && elemento.getSortOrder()!=null){
	        		String fieldOrdenar = "";
	        		String fieldOrdenar2 = "";
	        		
	        		if("codigo".equals(elemento.getFieldOrder())){
	        			fieldOrdenar = "codigo";
	        		}
	        		if("tipoElemento.descripcion".equals(elemento.getFieldOrder())){
	        			crit.createCriteria("tipoElemento", "tipo");
	        			fieldOrdenar = "tipo.descripcion";
	        		}
	        		if("depositoActual.descripcion".equals(elemento.getFieldOrder())){
	        			crit.createCriteria("depositoActual", "dep");
	        			fieldOrdenar = "dep.descripcion";
	        		}
	        		if("estado".equals(elemento.getFieldOrder()))
	        			fieldOrdenar = "estado";
	
	        	
	        		if("1".equals(elemento.getSortOrder())){
	        			if(!"".equals(fieldOrdenar))
	    					crit.addOrder(Order.asc(fieldOrdenar));
	        			if(!"".equals(fieldOrdenar2))
	    					crit.addOrder(Order.asc(fieldOrdenar2));
	    			}else if("2".equals(elemento.getSortOrder())){
	    				if(!"".equals(fieldOrdenar))
	    					crit.addOrder(Order.desc(fieldOrdenar));
	        			if(!"".equals(fieldOrdenar2))
	    					crit.addOrder(Order.desc(fieldOrdenar2));
	    			}
	        	}
	        	
	        	//Paginamos
	        	if(elemento.getNumeroPagina()!=null && elemento.getNumeroPagina().longValue()>0 
	    				&& elemento.getTamañoPagina()!=null && elemento.getTamañoPagina().longValue()>0){
	    			Integer paginaInicial = (elemento.getNumeroPagina() - 1);
	    			Integer filaDesde = elemento.getTamañoPagina() * paginaInicial;
	    			crit.setFirstResult(filaDesde);
	    			
	    			crit.setMaxResults(elemento.getTamañoPagina());
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Elemento> listarElementoPopupPaginado(ClienteAsp cliente, Elemento elemento,
			String descripcionTipoElemento, String codigoTipoElemento, String prefijoCodigoTipoElemento,
			String codigoCliente, Boolean limitarDependencia,
			Long libresODistintoLoteId, 
			Boolean esContenedor){
		
		return listarElementoPopupPaginado(cliente, elemento, descripcionTipoElemento, codigoTipoElemento, prefijoCodigoTipoElemento, codigoCliente, limitarDependencia, libresODistintoLoteId, esContenedor,null);
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Elemento> listarElementoPopupPaginado(ClienteAsp cliente, Elemento elemento,
			String descripcionTipoElemento, String codigoTipoElemento, String prefijoCodigoTipoElemento,
			String codigoCliente, Boolean limitarDependencia,
			Long libresODistintoLoteId, 
			Boolean esContenedor, Boolean cerrado){
		Session session = null;
        try {
        	List<Long> ids = obtenerIDsElementoPopupPaginado(cliente, elemento,
    				descripcionTipoElemento, codigoTipoElemento, prefijoCodigoTipoElemento,
    				codigoCliente, limitarDependencia, libresODistintoLoteId, esContenedor, cerrado);
        	//obtenemos una sesión
			session = getSession();
			//Si es null retornamos en cero, se puso dentro de session debido a que debe cerrar el finally
			if(ids==null || ids.size()==0)
        		return new ArrayList<Elemento>();
			
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.add(Restrictions.in("id", ids));
        	
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	return c.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar los elementos.", hibernateException);
	        return null;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
	}
	
	private List<Long> obtenerIDsElementoPopupPaginado(ClienteAsp cliente, Elemento elemento,
			String descripcionTipoElemento, String codigoTipoElemento, String prefijoCodigoTipoElemento,
			String codigoCliente, Boolean limitarDependencia,
			Long libresODistintoLoteId, 
			Boolean esContenedor,Boolean cerrado){
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
			
			ClienteEmp clienteEmp = null;
			if(codigoCliente != null && !"".equals(codigoCliente)){
        		Criteria critClienteEmp = session.createCriteria(ClienteEmp.class);
        		critClienteEmp.add(Restrictions.eq("codigo", codigoCliente));
        		critClienteEmp.createCriteria("empresa", "emp");
        		critClienteEmp.add(Restrictions.eq("emp.cliente", cliente));   
        		clienteEmp = (ClienteEmp) critClienteEmp.uniqueResult();
        	}
			
        	Criteria crit = session.createCriteria(getClaseModelo(),"ele");
        	crit.setProjection(Projections.id());
        	
        	crit.createCriteria("tipoElemento", "te");
        	if(prefijoCodigoTipoElemento!=null && !"".equals(prefijoCodigoTipoElemento)){
        		crit.add(Restrictions.eq("te.prefijoCodigo", prefijoCodigoTipoElemento));
        	}
        	//filtro por contenedor o contenido
        	if(esContenedor!=null && esContenedor)
        		crit.add(Restrictions.eq("te.contenedor", true));
        	else if(esContenedor!=null && esContenedor==false)
        		crit.add(Restrictions.eq("te.contenido", true)); 
        	
        	//filtro tipo de elemento
        	if(elemento.getCodigoTipoElemento()!=null && !elemento.getCodigoTipoElemento().equals("") && !"undefined".equalsIgnoreCase(codigoTipoElemento)){
        		//crit.add(Restrictions.eq("te.codigo", elemento.getCodigoTipoElemento()));
        		crit.add(Restrictions.in("te.codigo", elemento.getCodigoTipoElemento().split(",")));
        	}
        	
        	if(cerrado!=null){
        		if(!cerrado)
        			crit.add(Restrictions.or(Restrictions.isNull("cerrado"), Restrictions.eq("cerrado", false)));
        		else
        			crit.add(Restrictions.eq("cerrado", true));
        	}
        	
        	if(esContenedor!=null && !esContenedor && libresODistintoLoteId!=null){
        		DetachedCriteria subCritReferencias = DetachedCriteria.forClass(Referencia.class, "ref");
        		subCritReferencias.createCriteria("loteReferencia","loteReferencia");
        		subCritReferencias.add(Restrictions.ne("loteReferencia.id", libresODistintoLoteId));
        		subCritReferencias.createCriteria("elemento","elemento");
        		subCritReferencias.add(Restrictions.isNotNull("elemento"));
        		subCritReferencias.setProjection(Property.forName("elemento.id"));    	
        		crit.add(Property.forName("id").notIn(subCritReferencias));
//				DetachedCriteria subCritReferencias = DetachedCriteria.forClass(Referencia.class, "ref");
//        		subCritReferencias.createCriteria("elemento","elemento");
//        		subCritReferencias.setProjection(Property.forName("id"));   
//        		subCritReferencias.add(Restrictions.eqProperty("ref.elemento.id", "ele.id"));
//        		crit.add(Subqueries.notExists(subCritReferencias));
			}
        	
        	if(descripcionTipoElemento!=null){        		
        		crit.add(Restrictions.ilike("te.descripcion", descripcionTipoElemento+"%"));        	
        	}
        	if(clienteEmp!=null){
        		if(limitarDependencia!=null && limitarDependencia==true)
        			crit.add(Restrictions.eq("clienteEmp",clienteEmp));
    			else
    				crit.add(Restrictions.or(Restrictions.eq("clienteEmp",clienteEmp),Restrictions.isNull("clienteEmp")));
        	}else{
        		crit.add(Restrictions.isNull("clienteEmp"));
        	}
        	if(cliente != null){
	        	//filtro cliente
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
        	
        	//Paginamos
        	if(elemento.getNumeroPagina()!=null && elemento.getNumeroPagina().longValue()>0 
    				&& elemento.getTamañoPagina()!=null && elemento.getTamañoPagina().longValue()>0){
    			Integer paginaInicial = (elemento.getNumeroPagina() - 1);
    			Integer filaDesde = elemento.getTamañoPagina() * paginaInicial;
    			crit.setFirstResult(filaDesde);
    			
    			crit.setMaxResults(elemento.getTamañoPagina());
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
	public Integer contarElementoPopupPaginado(ClienteAsp cliente,
			String descripcionTipoElemento, String codigoTipoElemento, String prefijoCodigoTipoElemento,
			String codigoCliente, Boolean limitarDependencia,
			Long libresODistintoLoteId, 
			Boolean esContenedor){
		
		return contarElementoPopupPaginado(cliente, descripcionTipoElemento, codigoTipoElemento, prefijoCodigoTipoElemento, codigoCliente, limitarDependencia, libresODistintoLoteId, esContenedor, null);
	}
	
	@Override
	public Integer contarElementoPopupPaginado(ClienteAsp cliente,
			String descripcionTipoElemento, String codigoTipoElemento, String prefijoCodigoTipoElemento,
			String codigoCliente, Boolean limitarDependencia,
			Long libresODistintoLoteId, 
			Boolean esContenedor,Boolean cerrado){

		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
			
			ClienteEmp clienteEmp = null;
			if(codigoCliente != null && !"".equals(codigoCliente)){
        		Criteria critClienteEmp = session.createCriteria(ClienteEmp.class);
        		critClienteEmp.add(Restrictions.eq("codigo", codigoCliente));
        		critClienteEmp.createCriteria("empresa", "emp");
        		critClienteEmp.add(Restrictions.eq("emp.cliente", cliente));
        		clienteEmp = (ClienteEmp) critClienteEmp.uniqueResult();
        	}
			
        	Criteria crit = session.createCriteria(getClaseModelo(),"ele");
        	crit.setProjection(Projections.rowCount());
        	
        	crit.createCriteria("tipoElemento", "te");
        	if(prefijoCodigoTipoElemento!=null && !"".equals(prefijoCodigoTipoElemento)){
        		crit.add(Restrictions.eq("te.prefijoCodigo", prefijoCodigoTipoElemento));
        	}
        	//filtro por contenedor o contenido
        	if(esContenedor!=null && esContenedor)
        		crit.add(Restrictions.eq("te.contenedor", true));
        	else if(esContenedor!=null && esContenedor==false) 
        		crit.add(Restrictions.eq("te.contenido", true));
        	
        	//filtro tipo de elemento
        	if(codigoTipoElemento!=null && !codigoTipoElemento.equals("") && !"undefined".equalsIgnoreCase(codigoTipoElemento)){
        		//crit.add(Restrictions.eq("te.codigo", codigoTipoElemento));
        		crit.add(Restrictions.in("te.codigo", codigoTipoElemento.split(",")));
        	}
        	if(cerrado!=null){
        		if(!cerrado)
        			crit.add(Restrictions.or(Restrictions.isNull("cerrado"), Restrictions.eq("cerrado", false)));
        		else
        			crit.add(Restrictions.eq("cerrado", true));
        	}
        	
    		if(esContenedor!=null && !esContenedor && libresODistintoLoteId!=null){
        		DetachedCriteria subCritReferencias = DetachedCriteria.forClass(Referencia.class, "ref");
        		subCritReferencias.createCriteria("loteReferencia","loteReferencia");
        		subCritReferencias.add(Restrictions.ne("loteReferencia.id", libresODistintoLoteId));
        		subCritReferencias.createCriteria("elemento","elemento");
        		subCritReferencias.add(Restrictions.isNotNull("elemento"));
        		subCritReferencias.setProjection(Property.forName("elemento.id"));    	
        		crit.add(Property.forName("id").notIn(subCritReferencias));
//				DetachedCriteria subCritReferencias = DetachedCriteria.forClass(Referencia.class, "ref");
//        		subCritReferencias.createCriteria("elemento","elemento");
//        		subCritReferencias.setProjection(Property.forName("id"));   
//        		subCritReferencias.add(Restrictions.eqProperty("ref.elemento.id", "ele.id"));
//        		crit.add(Subqueries.notExists(subCritReferencias));
			}
        	
        	if(descripcionTipoElemento!=null){        		
        		crit.add(Restrictions.ilike("te.descripcion", descripcionTipoElemento+"%"));        	
        	}
        	if(clienteEmp!=null){
        		if(limitarDependencia!=null && limitarDependencia==true)
        			crit.add(Restrictions.eq("clienteEmp",clienteEmp));
    			else
    				crit.add(Restrictions.or(Restrictions.eq("clienteEmp",clienteEmp),Restrictions.isNull("clienteEmp")));
        	}else{
        		crit.add(Restrictions.isNull("clienteEmp"));
        	}
        	if(cliente != null){
	        	//filtro cliente
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
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
	
	@SuppressWarnings("unchecked")
	@Override
	public Elemento verificarExistente(Elemento elemento){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(elemento!=null){
	        	if(elemento.getClienteAsp() !=null)
	        		crit.add(Restrictions.eq("clienteAsp", elemento.getClienteAsp()));	        	
	        	if(elemento.getCodigo() !=null && !"".equals(elemento.getCodigo()))
	        		crit.add(Restrictions.ilike("codigo", elemento.getCodigo()+"%"));	      
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Elemento> result = crit.list(); 
        	if(result.size() == 1){
        		return result.get(0);
        	}
            return null; 
        } catch (HibernateException hibernateException) {
        	logger.error("no se pudo verificar existente de elemento", hibernateException);
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
	public Elemento verificarExistenteeEnRango(Elemento elemento,String codigoInicial, String codigoFinal){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(elemento!=null){
	        	if(elemento.getClienteAsp() !=null)
	        		crit.add(Restrictions.eq("clienteAsp", elemento.getClienteAsp()));	        	
	        	//if(elemento.getCodigo() !=null && !"".equals(elemento.getCodigo()))
	        		//crit.add(Restrictions.ilike("codigo", elemento.getCodigo()+"%"));
	        	if(codigoInicial !=null && !"".equals(codigoInicial)){
        			crit.add(Restrictions.ge("codigo", codigoInicial));
        		}
        		if(codigoFinal !=null && !"".equals(codigoFinal)){
        			crit.add(Restrictions.le("codigo", codigoFinal));
        		}
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<Elemento> result = crit.list(); 
        	if(result.size() >= 1){
        		return result.get(0);
        	}
            return null; 
        } catch (HibernateException hibernateException) {
        	logger.error("no se pudo verificar existente de elemento", hibernateException);
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
	public List<Elemento> listarElementosRelacionados(TipoElemento tipoElemento){
		Session session = null;
        try {        	
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.createCriteria("tipoElemento", "tipo");
        	//filtro value        	
        	if(tipoElemento.getCodigo()!=null && !"".equals(tipoElemento.getCodigo())){        		
        		c.add(Restrictions.ilike("tipo.codigo", tipoElemento.getCodigo()+"%"));        	
        	}
        	if(tipoElemento.getClienteAsp() != null){
	        	//filtro cliente
	        	c.add(Restrictions.eq("tipo.clienteAsp", tipoElemento.getClienteAsp()));
        	}
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	return c.list();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar los tipos de elementos.", hibernateException);
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
	public synchronized Boolean guardarReposicionamiento(List<Posicion> posicionesOrigen, List<Posicion> posicionesDestino, 
			List<Elemento> elementosReposicionados, List<Elemento> elementosAnterioresModuloDestino, 
			ClienteAsp clienteAsp, User user, Lectura lectura){
		Session session = null;
		Transaction tx = null;
		Boolean result = false;
		
		try {
			
			Date date = new Date();
			Movimiento movimiento = null;
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			
			//recorremos la lista de elementos ubicados en el modulo destino antes del reposicionamiento
			//y actualizamos su estado a "En Espera" y su posicion a null,
			//ademas creamos el movimiento correspondiente
			for (Elemento e : elementosAnterioresModuloDestino){
				movimiento = crearMovimientoTransferenciaParaActualizacionElementosAnterioresModuloDestino(e, clienteAsp, user, date, lectura);
				movimiento.setEstadoElemento(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
				session.save(movimiento);
				if(verificarElementoAnteriorModuloOrigenDistintoElementosReposicionados(e, elementosReposicionados)){
					e.setPosicion(null);
					e.setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
					session.update(e);
				}
			}
			
			//actualizamos las posiciones de origen
//			if(hayQueLiberarPosicionesModuloOrigen(elementosReposicionados, posicionesOrigen)){
//				for(Posicion posOrigen : posicionesOrigen){
//					if(verificarPosicionAnteriorElementoReposicionadoNoPerteneceModuloDestino(posOrigen, posicionesDestino)){	
//						session.update(posOrigen);
//					}					
//				}			
//			}
			
			List<Elemento> elementosActualesPosicionesOrigen = obtenerElementosAsignadosAPosicionesOrigen(elementosReposicionados, posicionesOrigen);
			List<Posicion> posicionesOrigenALiberar = obtenerListaPosicionesOrigenNoOcupadaPorOtroElemento(posicionesOrigen, elementosActualesPosicionesOrigen);			
			for(Posicion posOrigen : posicionesOrigenALiberar){
				if(verificarPosicionAnteriorElementoReposicionadoNoPerteneceModuloDestino(posOrigen, posicionesDestino)){	
					session.update(posOrigen);
				}					
			}			
			
			
			//recorremos la lista de elementos a reposicionar actualizamos su estado y su posicion,
			//y guardamos los movimientos correspondientes					
			for(Elemento elemento : elementosReposicionados){
				movimiento = crearMovimientoReposicionamiento(elemento, clienteAsp, user, date, lectura);
				movimiento.setEstadoElemento(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
				session.save(movimiento);				
				
				elemento.setPosicion(elemento.getPosicionFutura());
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
				session.update(elemento);
				//Lineas agregadas para el historico
    			session.save(registrarHistoricoElementos("MS013ELE", elemento,user,clienteAsp));
    			////////////////////////////////////
				
			}			
			
			//actualizamos las posiciones de destino
			for(Posicion posDes : posicionesDestino){
				session.update(posDes);
			}	
			
			lectura.setUtilizada(true);
			if(lectura.getId()!=null)
				session.update(lectura);
			else
			{	
				session.save(lectura);
				for(LecturaDetalle ld : lectura.getDetalles())
				{
					ld.setLectura(lectura);
				}
				lectura.setDetalles(lectura.getDetalles());
				session.update(lectura);
			}
			
			tx.commit();
			
			result = true;
			
			
		}
		catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			rollback(tx, e, "No fue posible guardar Reposicionamiento");
			result = false;
		}
		catch (BasaException e) {
			logger.error(e.getMessage(), e);
			rollback(tx, e, "No fue posible guardar Reposicionamiento");
			result = false;
		}
		catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			rollback(tx, e, "No fue posible guardar Reposicionamiento");
			result = false;
		}
		finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión durante el reposicionamiento", e);
        	}
        }
		
		return result;
	}
	
	@Override
	public synchronized Boolean guardarAsignacionPosicionesLibres(List<Posicion> posicionesOrigen, List<Posicion> posicionesDestino, 
			List<Elemento> elementosReposicionados, ClienteAsp clienteAsp, User user, Lectura lectura){
		Session session = null;
		Transaction tx = null;
		Boolean result = false;
		
		try {
			
			Date date = new Date();
			Movimiento movimiento = null;
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			
			Boolean siguenDisponibles = posicionService.verificarEstadoPosiciones(posicionesDestino, clienteAsp, Constantes.POSICION_ESTADO_DISPONIBLE);
			if(siguenDisponibles)
			{
				session.close();
				result = false;
				return result;
			}
			
			//actualizamos las posiciones de origen
//			if(hayQueLiberarPosicionesModuloOrigen(elementosReposicionados, posicionesOrigen)){
//				for(Posicion posOrigen : posicionesOrigen){
//					if(verificarPosicionAnteriorElementoReposicionadoNoPerteneceModuloDestino(posOrigen, posicionesDestino)){	
//						session.update(posOrigen);
//					}					
//				}			
//			}
			List<Elemento> elementosActualesPosicionesOrigen = obtenerElementosAsignadosAPosicionesOrigen(elementosReposicionados, posicionesOrigen);
			List<Posicion> posicionesOrigenALiberar = obtenerListaPosicionesOrigenNoOcupadaPorOtroElemento(posicionesOrigen, elementosActualesPosicionesOrigen);			
			for(Posicion posOrigen : posicionesOrigenALiberar){
				if(verificarPosicionAnteriorElementoReposicionadoNoPerteneceModuloDestino(posOrigen, posicionesDestino)){	
					session.update(posOrigen);
				}					
			}
			
			//recorremos la lista de elementos a reposicionar actualizamos su estado y su posicion,
			//y guardamos los movimientos correspondientes					
			for(Elemento elemento : elementosReposicionados){
				elemento.setDepositoActual(elemento.getPosicionFutura().getEstante().getGrupo().getSeccion().getDeposito());
				movimiento = crearMovimientoReposicionamiento(elemento, clienteAsp, user, date, lectura);
				movimiento.setEstadoElemento(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
				session.save(movimiento);				
				
				elemento.setPosicion(elemento.getPosicionFutura());
				elemento.setEstado(Constantes.ELEMENTO_ESTADO_EN_GUARDA);
				session.update(elemento);
				//Lineas agregadas para el historico
    			session.save(registrarHistoricoElementos("MS014ELE", elemento));
    			////////////////////////////////////
			}			
			
			//actualizamos las posiciones de destino
			for(Posicion posDes : posicionesDestino){
				session.update(posDes);
			}	
			
			lectura.setUtilizada(true);
			session.update(lectura);
			
			tx.commit();
			
			result = true;
		
		}catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			rollback(tx, e, "No fue posible guardar Reposicionamiento");
			result = false;
		}
		catch (BasaException e) {
			logger.error(e.getMessage(), e);
			rollback(tx, e, "No fue posible guardar Reposicionamiento");
			result = false;
		}
		catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			rollback(tx, e, "No fue posible guardar Reposicionamiento");
			result = false;
		}
		finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión durante el reposicionamiento", e);
        	}
        }
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized List<Posicion> obtenerPosicionesAnterioresPorElementos(List<Elemento> elementosReposicionados, ClienteAsp clienteAsp){
		List<Posicion> posiciones = null;
		Session session = null;
		List<Elemento> elemRep = null; 
		
		try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	        	
        	Disjunction disjuction = Restrictions.disjunction();
        	for(Elemento e: elementosReposicionados){
        		disjuction.add(Restrictions.eq("id", e.getId()));
        	}
        	crit.add(disjuction);
        	
        	if(clienteAsp != null){
        		crit.createCriteria("posicion").createCriteria("modulo").createCriteria("estante")
        		.createCriteria("grupo").createCriteria("seccion")
        		.createCriteria("deposito").createCriteria("sucursal").createCriteria("empresa").add(Restrictions.eq("cliente", clienteAsp));
        	}
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            elemRep = crit.list();
            posiciones = new ArrayList<Posicion>();
            if(elemRep!=null){
            	for(Elemento el:elemRep){
            		if(el.getPosicion()!=null){
            			posiciones.add(el.getPosicion());
            		}
            	}
            }
            
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
	
	public synchronized Boolean verificarTipoElementoValidoParaGrupoDeModulos(TipoElemento tipoElemento, Modulo moduloDestino, ClienteAsp clienteAsp){
		Session session = null;
		int cont = 1;
        //Buscamos en la base de datos la cantidad de elementos con tipo diferente al ingresado por parametro
		//que esten colocados en posiciones correspondientes a modulos adyacentes al modulo destino. 
		//Si la consulta devuelve 0 entonces los modulos adyacentes estan vacios o contienen elementos del mismo tipo
		//al ingresado por parametro, por lo tanto el metodo retorna true. 
		try {
			if(clienteAsp==null){
        		throw new ClienteAspNullException("No se puede ejecutar el metodo verificarTipoElementoValidoParaGrupoDeModulos porque clienteAsp = null");
        	}
        	//calculamos coordenadas de posiciones del modulo
        	int horPorModulo = moduloDestino.getEstante().getGrupo().getHorizontales() / moduloDestino.getEstante().getGrupo().getModulosHor();
        	int vertPorModulo = moduloDestino.getEstante().getGrupo().getVerticales() / moduloDestino.getEstante().getGrupo().getModulosVert(); 
        	Integer posVertDesde = moduloDestino.getOffsetVertical() + 1;
        	Integer posVertHasta = moduloDestino.getOffsetVertical()+vertPorModulo;  
        	Integer posHorDesde = moduloDestino.getOffsetHorizontal() + 1;
        	Integer posHorHasta = moduloDestino.getOffsetHorizontal() + horPorModulo;
        	
        	//obtenemos una sesión
        	session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.setProjection(Projections.rowCount());        	
        	c.createCriteria("posicion", "pos");
        	c.createCriteria("pos.modulo", "mod").add(Restrictions.ne("pos.modulo", moduloDestino));
        	c.createCriteria("pos.estante", "est").
        		add(Restrictions.eq("est.grupo", moduloDestino.getEstante().getGrupo())).        	
        	createCriteria("grupo").createCriteria("seccion").createCriteria("deposito").
        	createCriteria("sucursal").createCriteria("empresa","emp").
        		add(Restrictions.eq("emp.cliente", clienteAsp));
        	
        	//filtra las posiciones de los modulos adyacentes al modulo destino
        	Conjunction conjunction = Restrictions.conjunction();
        	conjunction.add(Restrictions.ge("pos.posHorizontal", posHorDesde));
        	conjunction.add(Restrictions.le("pos.posHorizontal", posHorHasta));
        	conjunction.add(Restrictions.ge("pos.posVertical", posVertDesde));
        	conjunction.add(Restrictions.le("pos.posVertical", posVertHasta));
        	c.add(conjunction);
        	
        	//filtra las estanterias del grupo que sean diferentes al moduloDestino
        	//c.add(Restrictions.eq("pos.estante.grupo", moduloDestino.getEstante().getGrupo()));
        	//c.add(Restrictions.ne("pos.modulo", moduloDestino));
        	
        	//filtra tipo de elemento diferente al de los elementos a reposicionar
        	c.add(Restrictions.ne("tipoElemento", tipoElemento));
        	
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	cont =  ((Integer)c.list().get(0)).intValue();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo verificar tipo elemento valido para reposicionamiento.", hibernateException);
	        cont = 1;
        }catch (BasaException ex){
        	logger.error("No se pudo verificar tipo elemento valido para reposicionamiento.", ex);
        	cont=1;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		//si la consulta devuelve distinto de 0 return false
		return cont==0;
	}
	
	@Override
	public synchronized Boolean verificarTipoElementoValidoParaMismoModulo(TipoElemento tipoElemento, Modulo moduloDestino, ClienteAsp clienteAsp){
		Session session = null;
		int cont = 1;
        //Buscamos en la base de datos la cantidad de elementos con tipo diferente al ingresado por parametro
		//que esten colocados en posiciones correspondientes al mismo modulo destino. 
		//Si la consulta devuelve 0 entonces los modulos adyacentes estan vacios o contienen elementos del mismo tipo
		//al ingresado por parametro, por lo tanto el metodo retorna true. 
		try {
			if(clienteAsp==null){
        		throw new ClienteAspNullException("No se puede ejecutar el metodo verificarTipoElementoValidoParaGrupoDeModulos porque clienteAsp = null");
        	}        	
        	//obtenemos una sesión
        	session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.setProjection(Projections.rowCount());        	
        	c.createCriteria("posicion", "pos");
        	c.createCriteria("pos.modulo", "mod").add(Restrictions.eq("pos.modulo", moduloDestino));
        	c.createCriteria("pos.estante", "est").
        		add(Restrictions.eq("est.grupo", moduloDestino.getEstante().getGrupo())).        	
        	createCriteria("grupo").createCriteria("seccion").createCriteria("deposito").
        	createCriteria("sucursal").createCriteria("empresa","emp").
        		add(Restrictions.eq("emp.cliente", clienteAsp));
        	
        	//filtra tipo de elemento diferente al de los elementos a reposicionar
        	c.add(Restrictions.ne("tipoElemento", tipoElemento));
        	
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	cont =  ((Integer)c.list().get(0)).intValue();
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo verificar tipo elemento valido para reposicionamiento.", hibernateException);
	        cont = 1;
        }catch (BasaException ex){
        	logger.error("No se pudo verificar tipo elemento valido para reposicionamiento.", ex);
        	cont=1;
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		//si la consulta devuelve distinto de 0 return false
		return cont==0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized String traerUltCodigoPorTipoElemento(TipoElemento tipoElemento,ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	if(cliente!=null){
	        	crit.add(Restrictions.eq("clienteAsp", cliente));
        	}
        	if(tipoElemento!=null)
        	{
        		crit.add(Restrictions.eq("tipoElemento", tipoElemento));	
        		crit.setProjection(Projections.max("codigo"));
        	}		
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<String> result = crit.list();
        	if(result.size() == 1){
        		String rta = result.get(0);
        		if(rta == null)
        			return "";
        		else
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
	
	@SuppressWarnings("unchecked")
	public synchronized List<Elemento> buscarElementosAnterioresModuloDestino(Modulo moduloDestino, ClienteAsp clienteAsp){
		Session session = null;
		List<Elemento> result = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria c = session.createCriteria(getClaseModelo());
        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	//filtra por posiciones pertenecientes al modulo destino
        	c.createCriteria("posicion").add(Restrictions.eq("modulo", moduloDestino));
        	
        	//filtra por clienteAsp
        	c.createCriteria("posicion.estante").        	        	
		    	createCriteria("grupo").createCriteria("seccion").createCriteria("deposito").
		    	createCriteria("sucursal").createCriteria("empresa","emp").
		    		add(Restrictions.eq("emp.cliente", clienteAsp));
        	
        	result = (List<Elemento>) c.list();
        	if(result==null){
        		result=new ArrayList<Elemento>();
        	}
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo listar elementos Anteriores modulo destino ", hibernateException);
	        result = new ArrayList<Elemento>();
        }finally{
        	try{
        		session.close();
        	}catch(Exception e){
        		logger.error("No se pudo cerrar la sesión", e);
        	}
        }
		
		return result;	
	}
	

	@SuppressWarnings("unchecked")
	public List<Elemento> listarElementoPopup(String val, String codigoDeposito, String codigoTipoElemento, String codigoClienteEmp, ClienteAsp clienteAsp){
		Session session = null;
		List <Elemento> result = null;
        if(codigoDeposito!=null && codigoDeposito.length()>0){
			try {
	        	//obtenemos una sesión
				session = getSession();
	        	Criteria crit = session.createCriteria(getClaseModelo());
	        	crit.createCriteria("depositoActual", "depAct");
	        	crit.add(Restrictions.eq("depAct.codigo", codigoDeposito));	        	
	        	
	        	if(codigoTipoElemento!=null && codigoTipoElemento.length()>0){
	        		crit.createCriteria("tipoElemento", "tipEl");
	        		crit.add(Restrictions.eq("tipEl.codigo", codigoTipoElemento));
	        	}
	        	
	        	if(codigoClienteEmp!=null && codigoClienteEmp.length()>0){
	        		crit.createCriteria("clienteEmp", "cliEmp");
	        		crit.add(Restrictions.eq("cliEmp.codigo", codigoClienteEmp));
	        	}
	        	
	        	if(clienteAsp != null){
	        		crit.add(Restrictions.eq("clienteAsp", clienteAsp));
	        	}else{
	        		throw new ClienteAspNullException();
	        	}
	        	
	        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        	result = crit.list();
	        } catch (HibernateException hibernateException) {
	        	logger.error("No se pudo listar elementos popup ", hibernateException);
	        	 result = new ArrayList<Elemento>();
	        } catch (ClienteAspNullException cEx) {
	        	logger.error("No se pudo listar elementos popup", cEx);
		        result = new ArrayList<Elemento>();
	        }finally{
	        	try{
	        		session.close();
	        	}catch(Exception e){
	        		logger.error("No se pudo cerrar la sesión", e);
	        	}
	        }
        }else{
        	result = new ArrayList<Elemento>();
        }
        return result;
	}
	
	@Override
	public List<Elemento> obtenerElementosDePosiciones(List<Posicion> posicionesOrigen){
		List<Elemento> result = null;
		Session session = null;
		if(posicionesOrigen!=null && posicionesOrigen.size()>0){
			try {  	
				
				//obtenemos una sesión
	        	session = getSession();
				
					session = getSession();
					Criteria c = session.createCriteria(getClaseModelo());
		        	
					Disjunction disjunction = Restrictions.disjunction();	    
		        	c.createCriteria("posicion", "posic");
		        	for(Posicion posicion: posicionesOrigen){		        		
		        		disjunction.add(Restrictions.eq("posic.id", posicion.getId()));
		        	}
		        	c.add(disjunction);
		        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		        	result = c.list();					
	        }catch (RuntimeException e) {				
				result = new ArrayList<Elemento>();
				logger.error(e);
			}finally{
	        	try{
	        		session.close();
	        	}catch(Exception e){
	        		logger.error("No se pudo cerrar la sesión durante el reposicionamiento", e);
	        	}
	        }
		}else{
			result = new ArrayList<Elemento>();
		}
		return result;
	}
	
	@Override
	public Long cantidadElementosPorConceptoFacturable(Long idClienteEmp, Long idConceptoFacturable, Boolean generaCostoXGuarda, ClienteAsp clienteAsp){
		Session session = null;
		Long result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.setProjection(Projections.rowCount());
        	
        		if(idClienteEmp!=null){
        			crit.createCriteria("clienteEmp","cli");
        			crit.add(Restrictions.eq("cli.id", idClienteEmp));
        		}
        		
        		if(idConceptoFacturable != null){
        			crit.createCriteria("tipoElemento", "tipo");
        			crit.add(Restrictions.eq("tipo.conceptoGuarda.id", idConceptoFacturable));
        		}
        		
        		if(generaCostoXGuarda != null){
        			crit.add(Restrictions.eq("generaCanonMensual", generaCostoXGuarda));
        		}
        		
        		crit.add(Restrictions.ne("estado", "Creado"));

     
        	 	if(clienteAsp != null){
        	 		crit.add(Restrictions.eq("clienteAsp", clienteAsp));
        	 	}
        	 	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	result = ((Long)crit.list().get(0));
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
	
	
	/**
	 * Metodo que devuelve el listado de elementos filtrado, pero realiza la busqueda por 
	 * consulta SQL.
	 * @author Victor Kenis
	 * @return 
	 */
	@Override
	public List<Elemento> traerElementosRetiroPorSQL(Elemento elemento,ClienteAsp clienteAsp,String clasificaciones){
		Session session=null;
		
		try{
			session = getSession();
           	
			String consulta = "select ele.* "
					+ "from elementos ele "
					+" LEFT JOIN tipoElementos AS tip ON ele.tipoElemento_id = tip.id ";
//	        		" LEFT JOIN clientesEmp cli ON ele.clienteEmp_id = cli.id " +
//	        		" LEFT JOIN empresas emp ON cli.empresa_id = emp.id ";
	        		
//	        if(elemento.getCodigoSucursal()!= null && !("").equalsIgnoreCase(elemento.getCodigoSucursal()))		
//	        	consulta += " LEFT JOIN sucursales AS suc ON emp.id = suc.empresa_id ";
//	        		
//			if(elemento.getCodigoDeposito()!= null && !elemento.getCodigoDeposito().equalsIgnoreCase(""))
//	        	consulta += " LEFT JOIN depositos dep ON ele.depositoActual_id = dep.id ";
	        		
			if(elemento.getCodigoContenedor()!= null && !elemento.getCodigoContenedor().equalsIgnoreCase(""))
	        	consulta += " LEFT JOIN elementos ee ON ele.contenedor_id = ee.id ";
					
			consulta+= "where 1=1 "
					+ "and ele.clienteAsp_id ="+clienteAsp.getId()+" " 
					+ "AND ele.clienteEmp_id ="+ elemento.getClienteEmp().getId().longValue() +" " 
					+ "and Exists(SELECT 1 "
					+ "from referencia "
					+ "where (referencia.elemento_id = ele.id or referencia.elemento_id = ele.contenedor_id) ";
					
			if(clasificaciones != null && !"".equals(clasificaciones))
				consulta += "AND (referencia.clasificacion_documental_id IN("+clasificaciones+" ))";
							
			consulta+= ") ";
					
        	
//        	if(elemento.getCodigoEmpresa()!= null && !elemento.getCodigoEmpresa().equalsIgnoreCase(""))
//        		consulta+= " AND emp.codigo = '"+ elemento.getCodigoEmpresa()+"' ";
//        	
//        	if(elemento.getCodigoSucursal()!= null && !("").equalsIgnoreCase(elemento.getCodigoSucursal()))
//        		consulta+= " AND suc.codigo = '"+elemento.getCodigoSucursal()+"' ";
//        	
//        	if(elemento.getCodigoDeposito()!= null && !elemento.getCodigoDeposito().equalsIgnoreCase(""))
//        		consulta+= " AND dep.codigo = '"+elemento.getCodigoDeposito()+"' ";
        	
        	if(elemento.getCodigoContenedor()!= null && !elemento.getCodigoContenedor().equalsIgnoreCase(""))
        		consulta+= " AND ee.codigo = '"+elemento.getCodigoContenedor()+"' ";
        	
        	if(elemento.getEstado()!=null && !elemento.getEstado().equalsIgnoreCase(""))
        		consulta+= " AND ele.estado = '" + elemento.getEstado()+ "' ";
        	else
        		consulta+= " and ele.estado IN ('En el Cliente','En Consulta') ";
        	
        	if(elemento.getCodigoDesde()!=null && !elemento.getCodigoDesde().equalsIgnoreCase(""))
        		consulta+= " AND ele.codigo >= '"+ elemento.getCodigoDesde()+"'";
        	
        	if(elemento.getCodigoHasta()!=null && !elemento.getCodigoHasta().equalsIgnoreCase(""))
        		consulta+= " AND ele.codigo <= '"+ elemento.getCodigoHasta()+"'";
        	
        	if(elemento.getCodigoTipoElemento()!=null && !elemento.getCodigoTipoElemento().equalsIgnoreCase(""))
        		consulta+= " AND tip.codigo = '"+elemento.getCodigoTipoElemento()+"' ";
        	
        	if(elemento.getCodigoLectura()!=null && !elemento.getCodigoLectura().equals(""))
        		consulta+= " AND ele.id IN (SELECT lecturaDetalle.elemento_id FROM lecturaDetalle " +
        		" Left JOIN lecturas lec on lecturaDetalle.lectura_id = lec.id WHERE lec.codigo = '"+elemento.getCodigoLectura()+"') ";
        	
        	if(elemento.getFieldOrder()!=null && elemento.getSortOrder()!=null){
        		
        		if("codigo".equals(elemento.getFieldOrder())){
        			consulta+= " ORDER BY ele.codigo ";
        		}
        		if("tipoElemento.descripcion".equals(elemento.getFieldOrder())){
        			consulta+= " ORDER BY tip.descripcion ";
        		}
//        		if("depositoActual.descripcion".equals(elemento.getFieldOrder())){
//        			consulta+= " ORDER BY dep.descripcion ";
//        		}
        		if("estado".equals(elemento.getFieldOrder()))
        			consulta+= " ORDER BY ele.estado ";
        	
        		if("1".equals(elemento.getSortOrder())){
        			consulta+= " ASC ";
    			}else if("2".equals(elemento.getSortOrder())){
    				consulta+= " DESC ";
    			}
        	}
        	
        	
        	SQLQuery q = session.createSQLQuery(consulta).addEntity(Elemento.class);			
			
			return (List<Elemento>)q.list();
			
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
	
	/**
	 * Metodo que devuelve el listado de elementos filtrado, pero realiza la busqueda por 
	 * consulta SQL.
	 * @author Victor Kenis
	 * @return 
	 */
	@Override
	public Elemento buscarElementosParaRequerimientosPorSQL(String codigoElemento,String codigoClienteEmp,String codigoTipoRequerimiento){
		Session session=null;

		try{
			session = getSession();

			String consulta = "SELECT        elementos.id, elementos.codigo, elementos.estado, tipoElementos.id AS Expr1, clientesEmp.codigo AS Expr2, clientesEmp.id AS Expr3, elementos.clienteAsp_id, " +
					"                         elementos.generaCanonMensual, elementos.clienteEmp_id, elementos.contenedor_id, elementos.posicion_id, elementos.tipoElemento_id,"+
					"                        elementos.codigoAlternativo, elementos.nroPrecinto, elementos.estadoTrabajo, elementos.depositoActual_id, elementos.fechaModificacion, " +
					"                         elementos.fechaModificacionPrecinto, elementos.nroPrecinto1, elementos.tipoTrabajo, elementos.usuarioModificacion_id,"+
					"                         elementos.usuarioModificacionPrecinto_id, elementos.ubicacionProvisoria, elementos.cerrado, elementos.habilitadoCerrar, "+
					"                         elementos.usuarioAsignacionElemento_id, elementos.basaReferencia, elementos.basaIdCaja "+
					"	FROM  elementos INNER JOIN "+
					"                        tipoElementos ON elementos.tipoElemento_id = tipoElementos.id LEFT OUTER JOIN "+
					"                        x_operacion_elemento ON elementos.id = x_operacion_elemento.elemento_id AND elementos.id = x_operacion_elemento.elemento_id LEFT OUTER JOIN "+
					"                        clientesEmp ON elementos.clienteEmp_id = clientesEmp.id " +
					" WHERE   (elementos.codigo = :codigoElemento) ";
	//				"AND (clientesEmp.codigo = :codigoClienteEmp) AND ((x_operacion_elemento.estado <> 'Pendiente') or (x_operacion_elemento.elemento_id is null)) \";\n" +
				//	" AND (clientesEmp.codigo = :codigoClienteEmp) ";


			Long idTipoRequerimiento = Long.valueOf(codigoTipoRequerimiento);

			if(idTipoRequerimiento==2L || idTipoRequerimiento==3L || idTipoRequerimiento==22L)
				consulta+= "AND (tipoElementos.id IN (2,8,11))";
			else if(idTipoRequerimiento==4L || idTipoRequerimiento==5L || idTipoRequerimiento==14L ||idTipoRequerimiento==6L ) {
				consulta += "AND (tipoElementos.id IN (1,7,10))";
			}



			SQLQuery q = session.createSQLQuery(consulta).addEntity(Elemento.class);
			q.setString("codigoElemento", codigoElemento);
		//	q.setString("codigoClienteEmp", codigoClienteEmp);

			return (Elemento)q.uniqueResult();

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
	public Integer contarElementosRetiroPorSQL(Elemento elemento,ClienteAsp clienteAsp,String clasificaciones){
		Session session=null;
		
		try{
			session = getSession();
           	
			String consulta = "select count(distinct ele.id) "
					+ "from elementos ele "
					+" LEFT JOIN tipoElementos AS tip ON ele.tipoElemento_id = tip.id ";
	        		//" LEFT JOIN clientesEmp cli ON ele.clienteEmp_id = cli.id " +
	        		//" LEFT JOIN empresas emp ON cli.empresa_id = emp.id ";
	        		
//	        if(elemento.getCodigoSucursal()!= null && !("").equalsIgnoreCase(elemento.getCodigoSucursal()))		
//	        	consulta += " LEFT JOIN sucursales AS suc ON emp.id = suc.empresa_id ";
	        		
//			if(elemento.getCodigoDeposito()!= null && !elemento.getCodigoDeposito().equalsIgnoreCase(""))
//	        	consulta += " LEFT JOIN depositos dep ON ele.depositoActual_id = dep.id ";
	        		
			if(elemento.getCodigoContenedor()!= null && !elemento.getCodigoContenedor().equalsIgnoreCase(""))
	        	consulta += " LEFT JOIN elementos ee ON (ele.contenedor_id = ee.id)";
					
			consulta+= "where 1=1 "
					+ "and ele.clienteAsp_id ="+clienteAsp.getId()+" "
					+ "AND ele.clienteEmp_id ="+ elemento.getClienteEmp().getId().longValue() +" " 
					+ "and Exists(SELECT 1 "
					+ "from referencia "
					+ "where (referencia.elemento_id = ele.id or referencia.elemento_id = ele.contenedor_id) ";
					
			if(clasificaciones != null && !"".equals(clasificaciones))
				consulta += "AND (referencia.clasificacion_documental_id IN("+clasificaciones+" ))";
							
			consulta+= ") ";
      	
        	if(elemento.getCodigoContenedor()!= null && !elemento.getCodigoContenedor().equalsIgnoreCase(""))
        		consulta+= " AND ee.codigo = '"+elemento.getCodigoContenedor()+"' ";
        	
        	if(elemento.getEstado()!=null && !elemento.getEstado().equalsIgnoreCase(""))
        		consulta+= " AND ele.estado = '" + elemento.getEstado()+ "' ";
        	else
        		consulta+= " and ele.estado IN ('En el Cliente','En Consulta') ";
        	
        	if(elemento.getCodigoDesde()!=null && !elemento.getCodigoDesde().equalsIgnoreCase(""))
        		consulta+= " AND ele.codigo >= '"+ elemento.getCodigoDesde()+"'";
        	
        	if(elemento.getCodigoHasta()!=null && !elemento.getCodigoHasta().equalsIgnoreCase(""))
        		consulta+= " AND ele.codigo <= '"+ elemento.getCodigoHasta()+"'";
        	
        	if(elemento.getCodigoTipoElemento()!=null && !elemento.getCodigoTipoElemento().equalsIgnoreCase(""))
        		consulta+= " AND tip.codigo = '"+elemento.getCodigoTipoElemento()+"' ";
        	
        	if(elemento.getCodigoLectura()!=null && !elemento.getCodigoLectura().equals(""))
        		consulta+= " AND ele.id IN (SELECT lecturaDetalle.elemento_id FROM lecturaDetalle " +
        		" Left JOIN lecturas lec on lecturaDetalle.lectura_id = lec.id WHERE lec.codigo = '"+elemento.getCodigoLectura()+"') ";
        	
        	
        	SQLQuery q = session.createSQLQuery(consulta);

            return (Integer) q.list().get(0); 
			
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
		
		/**
		 * Metodo que devuelve el listado de id de elementos filtrado por una lectura.
		 * @author Victor Kenis
		 * @return 
		 */
		@Override
		public List<Long> traerIdElementosEnLectura(String codigoLectura,ClienteAsp clienteAsp){
			Session session=null;
			
			try{
				session = getSession();
	           	
	        	String consulta = "SELECT ele.id " +
	        		" From elementos ele " +
	        		" where ele.clienteAsp_id ="+clienteAsp.getId()+" " +
	        		" AND ele.id IN (SELECT lecturaDetalle.elemento_id FROM lecturaDetalle " +
	        		" Left JOIN lecturas lec on lecturaDetalle.lectura_id = lec.id WHERE lec.codigo = '"+codigoLectura+"') " +
	        		" ORDER BY ele.id ";
	        			        	
	        	SQLQuery q = session.createSQLQuery(consulta);			
				
				return (List<Long>)q.list();
				
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
		public List<Elemento> traerElementosPorSQL(Elemento elemento,ClienteAsp clienteAsp){
			Session session=null;
			
			try{
				session = getSession();
	           	
	        	String consulta = "SELECT ele.* " +
	        		" From elementos ele " +
	        		" LEFT JOIN tipoElementos AS tip ON ele.tipoElemento_id = tip.id " +
	        		" LEFT JOIN clientesEmp cli ON ele.clienteEmp_id = cli.id and ele.clienteAsp_id ="+clienteAsp.getId()+" " +
	        		" LEFT JOIN empresas emp ON cli.empresa_id = emp.id " +
	        		" LEFT JOIN sucursales AS suc ON emp.id = suc.empresa_id " +    
	        		" LEFT JOIN depositos dep ON ele.depositoActual_id = dep.id " +
	        		" LEFT JOIN elementos ee ON ele.contenedor_id = ee.id " +
	        		" WHERE 1=1 ";
	        	
	        	if(elemento.getCodigoEmpresa()!= null && !elemento.getCodigoEmpresa().equalsIgnoreCase(""))
	        		consulta+= " AND emp.codigo = '"+ elemento.getCodigoEmpresa()+"' ";
	        	
	        	if(elemento.getCodigoSucursal()!= null && !("").equalsIgnoreCase(elemento.getCodigoSucursal()))
	        		consulta+= " AND suc.codigo = '"+elemento.getCodigoSucursal()+"' ";
	        	
	        	if(elemento.getCodigoDeposito()!= null && !elemento.getCodigoDeposito().equalsIgnoreCase(""))
	        		consulta+= " AND dep.codigo = '"+elemento.getCodigoDeposito()+"' ";
	        	
	        	if(elemento.getCodigoContenedor()!= null && !elemento.getCodigoContenedor().equalsIgnoreCase(""))
	        		consulta+= " AND ee.codigo = '"+elemento.getCodigoContenedor()+"' ";
	        	
	        	if(elemento.getEstado()!=null && !elemento.getEstado().equalsIgnoreCase(""))
	        		consulta+= " AND ele.estado = '" + elemento.getEstado()+ "' ";
	        	
	        	if(elemento.getCodigoDesde()!=null && !elemento.getCodigoDesde().equalsIgnoreCase(""))
	        		consulta+= " AND ele.codigo >= '"+ elemento.getCodigoDesde()+"'";
	        	
	        	if(elemento.getCodigoHasta()!=null && !elemento.getCodigoHasta().equalsIgnoreCase(""))
	        		consulta+= " AND ele.codigo <= '"+ elemento.getCodigoHasta()+"'";
	        	
	        	if(elemento.getCodigoTipoElemento()!=null && !elemento.getCodigoTipoElemento().equalsIgnoreCase(""))
	        		consulta+= " AND tip.codigo = '"+elemento.getCodigoTipoElemento()+"' ";
	        	
	        	if(elemento.getCodigoLectura()!=null && !elemento.getCodigoLectura().equals(""))
	        		consulta+= " AND ele.id IN (SELECT lecturaDetalle.elemento_id FROM lecturaDetalle " +
	        		" Left JOIN lecturas lec on lecturaDetalle.lectura_id = lec.id WHERE lec.codigo = '"+elemento.getCodigoLectura()+"') ";
	        	
	        	if(elemento.getFieldOrder()!=null && elemento.getSortOrder()!=null){
	        		
	        		if("codigo".equals(elemento.getFieldOrder())){
	        			consulta+= " ORDER BY ele.codigo ";
	        		}
	        		if("tipoElemento.descripcion".equals(elemento.getFieldOrder())){
	        			consulta+= " ORDER BY tip.descripcion ";
	        		}
	        		if("depositoActual.descripcion".equals(elemento.getFieldOrder())){
	        			consulta+= " ORDER BY dep.descripcion ";
	        		}
	        		if("estado".equals(elemento.getFieldOrder()))
	        			consulta+= " ORDER BY ele.estado ";
	        	
	        		if("1".equals(elemento.getSortOrder())){
	        			consulta+= " ASC ";
	    			}else if("2".equals(elemento.getSortOrder())){
	    				consulta+= " DESC ";
	    			}
	        	}
	        	
	        	
	        	SQLQuery q = session.createSQLQuery(consulta).addEntity(Elemento.class);			
				
				return (List<Elemento>)q.list();
				
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
	////////////////////////////////METODOS AUXILIARES - REPOSICIONAMIENTO///////////////////////////
	/**
	 * Crea un objeto movimietnto de tipo transferencia a partir de los valores de los 
	 * campos posicion y posicion futura del elemento pasado por parametro
	 * @param elementoReposicionado
	 * @return
	 */
	private Movimiento crearMovimientoTransferencia(
			Elemento elementoReposicionado, 
			ClienteAsp clienteAsp,User user, Date date, Lectura lectura)
			throws PosicionDestinoNullEnReposicionamientoException{
		if(elementoReposicionado.getPosicionFutura()==null){
			throw new PosicionDestinoNullEnReposicionamientoException();
		}
		
		Movimiento movimiento = new Movimiento();
		movimiento.setElemento(elementoReposicionado);
		movimiento.setFecha(date);
		movimiento.setUsuario(user);
		movimiento.setClaseMovimiento("interno");
		movimiento.setPosicionOrigenDestino(elementoReposicionado.getPosicionFutura());
		movimiento.setRemito(null);		
		movimiento.setTipoMovimiento(Constantes.MOVIMIENTO_TIPO_MOVIMIENTO_TRANSFERENCIA);		
		movimiento.setDeposito(elementoReposicionado.getDepositoActual());
		movimiento.setDepositoOrigenDestino(elementoReposicionado.getDepositoActual());
		movimiento.setClienteEmpOrigenDestino(null);		
		movimiento.setLectura(lectura);
		movimiento.setClienteAsp(clienteAsp);

		return movimiento;
	}
	/**
	 * Crea un objeto movimietnto de tipo transferencia a partir del valor del campo 
	 * posicion futura del elemento pasado por parametro, se usa para crear un 
	 * movimiento a partir de elementos que tienen seteado el deposito pero no tienen seteada la 
	 * posicion
	 * @param elementoReposicionado
	 * @return
	 */
	private Movimiento crearMovimientoTransferenciaPorIngreso(
			Elemento elementoReposicionado, 
			ClienteAsp clienteAsp,User user, Date date, Lectura lectura)
			throws PosicionDestinoNullEnReposicionamientoException{
		if(elementoReposicionado.getPosicionFutura()==null){
			throw new PosicionDestinoNullEnReposicionamientoException();
		}
		
		Movimiento movimiento = new Movimiento();
		movimiento.setElemento(elementoReposicionado);
		movimiento.setFecha(date);
		movimiento.setUsuario(user);
		movimiento.setClaseMovimiento("interno");
		movimiento.setPosicionOrigenDestino(elementoReposicionado.getPosicionFutura());
		movimiento.setRemito(null);		
		movimiento.setTipoMovimiento(Constantes.MOVIMIENTO_TIPO_MOVIMIENTO_TRANSFERENCIA);		
		movimiento.setDeposito(elementoReposicionado.getDepositoActual());
		movimiento.setDepositoOrigenDestino(elementoReposicionado.getDepositoActual());
		movimiento.setClienteEmpOrigenDestino(null);		
		movimiento.setLectura(lectura);
		movimiento.setClienteAsp(clienteAsp);

		return movimiento;
	}
	
	/**
	 * Devuelve un objeto movimiento acorde a los datos del elemento a reposicionar.
	 * @param elemento
	 * @param clienteAsp
	 * @param user
	 * @param date
	 * @param lectura
	 * @return
	 * @throws ElementoDepositoLocalDistintoDepositoDestinoReposicionamiento
 * @throws PosicionDestinoNullEnReposicionamientoException Exception
	 */
	private Movimiento crearMovimientoReposicionamiento(Elemento elemento, ClienteAsp clienteAsp, User user, Date date, Lectura lectura) 
			throws ElementoDepositoLocalDistintoDepositoDestinoReposicionamientoException, PosicionDestinoNullEnReposicionamientoException{
		
		Movimiento mov = null;
		
		if(elemento!=null){			
			Deposito depositoOrigen = elemento.getDepositoActual();
			Deposito depositoDestino = obtenerDepositoDestino(elemento);
			Posicion posOrigen = elemento.getPosicion();
			if(posOrigen==null && depositoOrigen.getId().equals(depositoDestino.getId())){
				mov = crearMovimientoTransferenciaPorIngreso(elemento, clienteAsp, user, date, lectura); 
			}else if(posOrigen!=null && depositoOrigen.getId().equals(depositoDestino.getId())){
				mov = crearMovimientoTransferencia(elemento, clienteAsp, user, date, lectura); 
			}else{
				throw new ElementoDepositoLocalDistintoDepositoDestinoReposicionamientoException();
			}			
		}
		
		return mov;
	}
	
	private Movimiento crearMovimientoTransferenciaParaActualizacionElementosAnterioresModuloDestino(
			Elemento elementoAnteriorModuloDestino, 
			ClienteAsp clienteAsp,User user, Date date, Lectura lectura)
			throws PosicionDestinoNullEnReposicionamientoException{
		
		Movimiento movimiento = new Movimiento();
		movimiento.setElemento(elementoAnteriorModuloDestino);
		movimiento.setFecha(date);
		movimiento.setUsuario(user);
		movimiento.setClaseMovimiento("interno");
		movimiento.setPosicionOrigenDestino(null);
		movimiento.setRemito(null);		
		movimiento.setTipoMovimiento(Constantes.MOVIMIENTO_TIPO_MOVIMIENTO_TRANSFERENCIA);		
		movimiento.setDeposito(elementoAnteriorModuloDestino.getDepositoActual());
		movimiento.setDepositoOrigenDestino(elementoAnteriorModuloDestino.getDepositoActual());
		movimiento.setClienteEmpOrigenDestino(null);		
		movimiento.setLectura(lectura);
		movimiento.setClienteAsp(clienteAsp);

		return movimiento;
	}
	
	@SuppressWarnings("unused")
	private Deposito obtenerDepositoOrigen(Elemento elemento){
		Deposito origen = null; 
		if(elemento.getPosicion()!=null){
			origen = elemento.getPosicionFutura().getEstante().getGrupo().getSeccion().getDeposito();
		}
		return origen;
	}
	
	private Deposito obtenerDepositoDestino(Elemento elemento){
		Deposito origen = elemento.getPosicionFutura().getEstante().getGrupo().getSeccion().getDeposito();
		return origen;
	}
	
	/**
	 * Verifica que existan posiciones Origen para los elementos a reposicionar, y que dichas posiciones no esten ocupadas por nuevos elementos.  
	 * @param elementos
	 * @param posicionesOrigen
	 * @param tipoMovimiento
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean hayQueLiberarPosicionesModuloOrigen(List<Elemento> elementos, List<Posicion> posicionesOrigen){
		boolean result = false;
		
		if(posicionesOrigen!=null && posicionesOrigen.size()>0){
			Session session = null;
	        try {        	
	        	//obtenemos una sesión
				session = getSession();
	        	Criteria c = session.createCriteria(getClaseModelo());
	        	c.setProjection(Projections.rowCount());
	        	Conjunction conjunction = Restrictions.conjunction();	    
	        	for(Posicion posicion: posicionesOrigen){
	        		conjunction.add(Restrictions.eq("posicion", posicion));
	        	}
	        	c.add(conjunction);
	        	
	        	Conjunction conjunction2 = Restrictions.conjunction();
	        	for(Elemento elemento : elementos){
	        		conjunction2.add(Restrictions.ne("id", elemento.getId()));
	        	}
	        	c.add(conjunction2);
	        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	        		
	        	result = ((Integer)c.uniqueResult())==0;	        	
	        }catch (RuntimeException e) {				
				result = false;
				logger.error(e);
			}finally{
	        	try{
	        		session.close();
	        	}catch(Exception e){
	        		logger.error("No se pudo cerrar la sesión durante el reposicionamiento", e);
	        	}
	        }
	        }
		return result;
	}
	/**
	 * Devuelve una lista con los elementos (diferentes a los elementos a reposicionar) que se encuentran en alguna (o todas) las posiciones de origen de los elementos a reposicionar.
	 * @param elementosReposicionados Los elementos a reposicionar
	 * @param posicionesOrigen Las posiciones de origen de los elementos a reposicionar
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Elemento> obtenerElementosAsignadosAPosicionesOrigen(List<Elemento> elementosReposicionados, List<Posicion> posicionesOrigen){
		List<Elemento> result = null;
		Session session = null;
		if(posicionesOrigen!=null && posicionesOrigen.size()>0){
			try {  	
	        	//obtenemos una sesión
	        	session = getSession();
				
					session = getSession();
					Criteria c = session.createCriteria(getClaseModelo());
		        	
					Disjunction disjunction = Restrictions.disjunction();	    
		        	c.createCriteria("posicion", "posic");
		        	for(Posicion posicion: posicionesOrigen){		        		
		        		disjunction.add(Restrictions.eq("posic.id", posicion.getId()));
		        	}
		        	c.add(disjunction);
		        
		        	Conjunction conjunction = Restrictions.conjunction();
		        	for(Elemento elemento : elementosReposicionados){
		        		conjunction.add(Restrictions.ne("id", elemento.getId()));
		        	}
		        	c.add(conjunction);	
		        	c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		        	result = c.list();					
	        }catch (RuntimeException e) {				
				result = new ArrayList<Elemento>();
				logger.error(e);
			}finally{
	        	try{
	        		session.close();
	        	}catch(Exception e){
	        		logger.error("No se pudo cerrar la sesión durante el reposicionamiento", e);
	        	}
	        }
		}else{
			result = new ArrayList<Elemento>();
		}
		return result;
	}
	
	/**
	 * Devuelve una lista con las posiciones que deben liberarce durante el proceso de reposicionamiento
	 * @param posicionesOrigen Las posiciones de origen de los elementos a reposicionar
	 * @param elementosAsignadosAPosicionesOrigen Los elementos no pertenecientes al conjunto de elementos a reposicionar, 
	 * que tienen asignadas posiciones incluidas en la lista de posiciones de origen
	 * @return
	 */
	private List<Posicion> obtenerListaPosicionesOrigenNoOcupadaPorOtroElemento(List <Posicion> posicionesOrigen, List<Elemento>  elementosAsignadosAPosicionesOrigen){
		List<Posicion> result = new ArrayList<Posicion>();
		Iterator<Elemento> itElem = elementosAsignadosAPosicionesOrigen.iterator();
		boolean agregar = true;
		Elemento el = null;
		if(elementosAsignadosAPosicionesOrigen.size()>0){
			for(Posicion pos:posicionesOrigen){
				agregar = true;
				
				for(int i = 0; i<elementosAsignadosAPosicionesOrigen.size(); i++){
					el = elementosAsignadosAPosicionesOrigen.get(i);
					if(el.getPosicion().getId().equals(pos.getId())){
						agregar = false;
						elementosAsignadosAPosicionesOrigen.remove(i);
						break;
					}
				}
				if(agregar){
					result.add(pos);
				}
			}	
		}else{
			result = posicionesOrigen;
		}
		return result;
	}
	/**
	 * Verifica que un elemento ubicado anteriormente en el modulo destino sea diferente a los elementos a reposicionar, de esta forma se evita 
	 * guardar el campo posicion del elemento a null y luego a la posicion destino en la misma sesion de hibernate, lo que provoca una exception
	 * @param elementoAnteriorModuloDestino
	 * @param elementosReposicionados
	 * @return
	 */
	private boolean verificarElementoAnteriorModuloOrigenDistintoElementosReposicionados(Elemento elementoAnteriorModuloDestino, List<Elemento> elementosReposicionados){
		boolean result = true;
		for(Elemento e: elementosReposicionados){
			if(e.getId().equals(elementoAnteriorModuloDestino.getId())){
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Verifica que una posicion anterior de alguno de los elementos reposicionados no pertenezca al modulo destino, de esta forma se evita liberar y 
	 * luego ocupar una posicion en la misma sesion de hibernate lo que provoca una exception
	 *
	 * @return
	 */
	private boolean verificarPosicionAnteriorElementoReposicionadoNoPerteneceModuloDestino(Posicion posicionOrigen, List<Posicion> posicionesDestino){
		boolean result = true;
		for(Posicion p: posicionesDestino){
			if(p.getId().equals(posicionOrigen.getId())){
				result = false;
				break;
			}
		}
		
		return result;
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
	
	
	////////////////////////////////FIN METODOS AUXILIARES - REPOSICIONAMIENTO///////////////////////////

	private User obtenerUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}
	
	private ClienteAsp obtenerClienteAspUser(){
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCliente();
	}
	
	private ElementoHistorico registrarHistoricoElementos(String mensaje, Elemento elemento){
		return registrarHistoricoElementos(mensaje, elemento, null,null);
	}
	
	private ElementoHistorico registrarHistoricoElementos(String mensaje, Elemento elemento, User user,ClienteAsp clienteAsp){
		ElementoHistorico elementoHis = new ElementoHistorico();
		elementoHis.setCodigoElemento(elemento.getCodigo());
		elementoHis.setAccion(mensaje);
		elementoHis.setFechaHora(new Date());
		if(user==null)
			elementoHis.setUsuario(obtenerUser());
		else
			elementoHis.setUsuario(user);
		if(clienteAsp==null)
			elementoHis.setClienteAsp(obtenerClienteAspUser());
		else
			elementoHis.setClienteAsp(clienteAsp);
		if(elemento.getClienteEmp()!=null){
			elementoHis.setCodigoCliente(elemento.getClienteEmp().getCodigo());
			elementoHis.setNombreCliente(elemento.getClienteEmp().getRazonSocialONombreYApellido());
		}
		elementoHis.setCodigoTipoElemento(elemento.getTipoElemento().getCodigo());
		elementoHis.setNombreTipoElemento(elemento.getTipoElemento().getDescripcion());
		return elementoHis;
	}
		
}

