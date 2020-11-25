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
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.ClienteConceptoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteConcepto;

/**
 * @author Gonzalo Noriega
 *
 */
@Component
public class ClienteConceptoServiceImp extends GestorHibernate<ClienteConcepto> implements ClienteConceptoService {
	private static Logger logger=Logger.getLogger(ClienteConceptoServiceImp.class);
	
	@Autowired
	public ClienteConceptoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<ClienteConcepto> getClaseModelo() {
		return ClienteConcepto.class;
	}

	@Override
	public Boolean guardarClienteConcepto(ClienteConcepto clienteConcepto) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(clienteConcepto);
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
	public Boolean actualizarClienteConcepto(ClienteConcepto clienteConcepto) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(clienteConcepto);
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
	public Boolean eliminarClienteConcepto(ClienteConcepto clienteConcepto) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(clienteConcepto);
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
	public List<ClienteConcepto> listarClienteConceptoesFiltradasPorCliente(ClienteConcepto clienteConcepto, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("cliente", "cli");
        	crit.createCriteria("concepto", "con");
        	crit.createCriteria("listaPrecios", "lista");
        	if(clienteConcepto!=null){
        		if(clienteConcepto.getClienteCodigo() !=null &&  !"".equals(clienteConcepto.getClienteCodigo())){
	        		crit.add(Restrictions.eq("cli.codigo", clienteConcepto.getClienteCodigo()));
        		}
	        	if(clienteConcepto.getListaPrecioCodigo() !=null &&  !"".equals(clienteConcepto.getListaPrecioCodigo())){
	        		crit.add(Restrictions.eq("lista.codigo", clienteConcepto.getListaPrecioCodigo()));
	        	}
	        	if(clienteConcepto.getConceptoCodigo() !=null &&  !"".equals(clienteConcepto.getConceptoCodigo())){
	        		crit.add(Restrictions.eq("con.codigo", clienteConcepto.getConceptoCodigo()));
	        	}
        	}
        	//filtro por cliente
        	if(cliente != null)
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	
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
	public List obtenerClienteConceptosEnPeriodo(String periodo,String codigoEmpresa,Boolean habilitado, ClienteAsp cliente) {
		
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        
//        	String consulta3 = "SELECT     con.id, perj.razonsocial, (cli.nombre + ' ' + cli.apellido) as Nombre, con.fechaAlta, con.tipoConcepto, con.finalUnitario, con.cantidad, con.finalTotal, con.estado, con.asignado, " + 
//            " con.requerimiento_id " +
//            " FROM concepto_operacion_cliente con " +
//            " left join clientesemp cli on con.clienteemp_id = cli.id " +
//            " left join personas_juridicas perj on cli.razonsocial_id = perj.id and cli.tipopersona='Juridica' " +
//            " WHERE (',' + cli.mesesFacturables + ',' LIKE '%,"+periodo+",%') AND (con.clienteAsp_id = "+cliente.getId()+") AND (con.fechaAlta <= '"+fechaPeriodo+"') AND (con.asignado = '0') AND (con.id IN " +
//            " (SELECT id FROM concepto_operacion_cliente AS c WHERE (c.id NOT IN ("+cadenaAsociados+")))) AND (con.estado = 'Pendiente') ";
        	
        	String consulta = "SELECT con.id, cli.id AS clienteEmp_id, cli.mesesFacturables, con.concepto_id, con.listaPrecios_id, ci.impuesto_id, imp.alicuota "+
        	" FROM clientesConceptos AS con LEFT OUTER JOIN " +
            " clientesEmp AS cli ON con.cliente_id = cli.id LEFT OUTER JOIN "+
            " empresas AS emp ON cli.empresa_id = emp.id "+
            " LEFT OUTER JOIN conceptoFacturable AS cfact ON con.concepto_id = cfact.id "+
            " LEFT OUTER JOIN x_conceptofacturable_impuesto AS ci ON con.concepto_id = ci.conceptofacturable_id"+
            " LEFT OUTER JOIN impuestos AS imp ON ci.impuesto_id = imp.id"+
            " WHERE (',' + cli.mesesFacturables + ',' LIKE '%,"+periodo+",%') AND (con.clienteAsp_id = "+cliente.getId()+") AND (emp.codigo = "+codigoEmpresa+") AND (con.habilitado = 1) ";
        	
        	SQLQuery q = session.createSQLQuery(consulta);			
			
			return q.list();
			
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
	
	@Override
	public ClienteConcepto verificarExistente(ClienteConcepto clienteConcepto, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("cliente", "cli");
        	crit.createCriteria("cli.empresa", "emp");
        	crit.createCriteria("concepto", "con");
        	crit.createCriteria("listaPrecios", "lista");
        	if(clienteConcepto!=null){
        		if(clienteConcepto.getClienteCodigo() !=null &&  !"".equals(clienteConcepto.getClienteCodigo())){
	        		crit.add(Restrictions.eq("cli.codigo", clienteConcepto.getClienteCodigo()));
	        		crit.add(Restrictions.eq("emp.cliente", cliente));
        		}
	        	if(clienteConcepto.getListaPrecioCodigo() !=null &&  !"".equals(clienteConcepto.getListaPrecioCodigo())){
	        		crit.add(Restrictions.eq("lista.codigo", clienteConcepto.getListaPrecioCodigo()));
	        		crit.add(Restrictions.eq("lista.clienteAsp", cliente));
	        	}
	        	if(clienteConcepto.getConceptoCodigo() !=null &&  !"".equals(clienteConcepto.getConceptoCodigo())){
	        		crit.add(Restrictions.eq("con.codigo", clienteConcepto.getConceptoCodigo()));
	        		crit.add(Restrictions.eq("con.clienteAsp", cliente));
	        	}
        	}
        	//filtro por cliente
        	if(cliente != null)
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	List<ClienteConcepto> result=crit.list();
        	if(result.size()==1)
				return result.get(0);
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
}
