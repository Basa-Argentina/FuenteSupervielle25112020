/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.jerarquias.hibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.LoteFacturacionDetalleService;
import com.security.accesoDatos.configuraciongeneral.interfaz.RemitoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.accesoDatos.jerarquias.interfaz.ConceptoOperacionClienteService;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.ClienteEmp;
import com.security.modelo.configuraciongeneral.Lectura;
import com.security.modelo.configuraciongeneral.LecturaDetalle;
import com.security.modelo.configuraciongeneral.LoteFacturacionDetalle;
import com.security.modelo.configuraciongeneral.Posicion;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
import com.security.modelo.configuraciongeneral.Remito;
import com.security.modelo.configuraciongeneral.RemitoDetalle;
import com.security.modelo.configuraciongeneral.Serie;
import com.security.modelo.jerarquias.ConceptoOperacionCliente;
import com.security.utils.DateUtil;

/**
 * @author Victor Kenis
 *
 */
@Component
public class ConceptoOperacionClienteServiceImp extends GestorHibernate<ConceptoOperacionCliente> implements ConceptoOperacionClienteService {
	private static Logger logger=Logger.getLogger(ConceptoOperacionClienteServiceImp.class);

	
	@Autowired
	public ConceptoOperacionClienteServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<ConceptoOperacionCliente> getClaseModelo() {
		return ConceptoOperacionCliente.class;
	}

	@Override
	public Boolean guardarConceptoOperacionCliente(ConceptoOperacionCliente conceptoOperacionCliente) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.save(conceptoOperacionCliente);
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
	public Boolean actualizarConceptoOperacionCliente(ConceptoOperacionCliente conceptoOperacionCliente) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.update(conceptoOperacionCliente);
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
	public Boolean actualizarConceptoOperacionClienteList(List<ConceptoOperacionCliente> listConceptosOperacionCliente)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(ConceptoOperacionCliente actualizar : listConceptosOperacionCliente){
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
	public Boolean guardarConceptoOperacionClienteList(List<ConceptoOperacionCliente> listConceptosOperacionCliente)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(ConceptoOperacionCliente actualizar : listConceptosOperacionCliente){
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
	public List<ConceptoOperacionCliente> guardarYTraerConceptoOperacionClienteList(List<ConceptoOperacionCliente> listConceptosOperacionCliente)throws RuntimeException{
		Session session = null;
		Transaction tx = null;
		List<ConceptoOperacionCliente> listaGuardada = new ArrayList<ConceptoOperacionCliente>();
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();		        	
        	for(ConceptoOperacionCliente actualizar : listConceptosOperacionCliente){
    			session.save(actualizar);
    			session.flush();
    			session.clear();
    			listaGuardada.add(actualizar);
        	}
        	tx.commit();
        	return listaGuardada;
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo grabar la coleccion de remitos ", hibernateException);
	        return null;
        }
   }
	
	@Override
	public Boolean eliminarConceptoOperacionCliente(ConceptoOperacionCliente conceptoOperacionCliente) {
		Session session = null;
		Transaction tx = null;
		try {
			//obtenemos una sesión
			session = getSession();
			//creamos la transacción
			tx = session.getTransaction();
			tx.begin();
			//guardamos el objeto
			session.delete(conceptoOperacionCliente);
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
	public List<ConceptoOperacionCliente> getByNumeros(List<Long> numeros, ClienteAsp cliente){
		Session session = null;
		List<ConceptoOperacionCliente> result = null;
		@SuppressWarnings("rawtypes")
		ArrayList<ConceptoOperacionCliente> listaConceptosOrdenados = new ArrayList(numeros.size());
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	Disjunction disjunction = Restrictions.disjunction();
        	for(Long numero : numeros){
        		disjunction.add(Restrictions.eq("id" , numero));
        		listaConceptosOrdenados.add(null);
        	}
        	crit.add(disjunction);
        	List<ConceptoOperacionCliente> listaConceptos = null;
        	if(cliente != null){
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        		listaConceptos = (List<ConceptoOperacionCliente>)crit.list();        		
        	}else { 
        		listaConceptos = new ArrayList<ConceptoOperacionCliente>();
        	}
        	//ordenamos los remitos por el orden de la lista de codigos
        	
        	if(numeros.size () == listaConceptos.size()){        						
        		Long idConcepto = null;
        		int index = 0;
        		ConceptoOperacionCliente concepto = null;
        		for(int i = 0; i<listaConceptos.size(); i++){
        			concepto = listaConceptos.get(i);
        			idConcepto = concepto.getId();
        			index = numeros.indexOf(idConcepto);
        			listaConceptosOrdenados.set(index, concepto);
        		}
        		listaConceptos = listaConceptosOrdenados;
        	}else { 
        		listaConceptos = new ArrayList<ConceptoOperacionCliente>();
        	}
        	
        	
            result = listaConceptos;
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
	
	@Override
	public List<ConceptoOperacionCliente> conceptoOperacionClienteFiltradas(ConceptoOperacionCliente conceptoOperacionCliente, ClienteAsp cliente){
		return conceptoOperacionClienteFiltradas(null, null, conceptoOperacionCliente, cliente);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConceptoOperacionCliente> conceptoOperacionClienteFiltradas(List<Long> idsAsociados,String periodo, ConceptoOperacionCliente conceptoOperacionCliente, ClienteAsp cliente){
		List<ConceptoOperacionCliente> conceptosOperacionCliente = null;
		Session session = null;
		//Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//crit.setProjection(Projections.rowCount());
        	
        	//obtenemos una sesión
			session = getSession();
        	
        	if(conceptoOperacionCliente!=null){
        		
        		if((conceptoOperacionCliente.getCodigoCliente() != null && !"".equals(conceptoOperacionCliente.getCodigoCliente())) 
        				|| (periodo != null && !"".equals(periodo)))
        		{
        			crit.createCriteria("clienteEmp", "cli");


        			if(conceptoOperacionCliente.getCodigoCliente() != null && !"".equals(conceptoOperacionCliente.getCodigoCliente()))
        				crit.add(Restrictions.eq("cli.codigo",conceptoOperacionCliente.getCodigoCliente()));
        			
        			if(periodo != null && !"".equals(periodo)){
        				crit.add(Restrictions.sqlRestriction("(,{alias}.clienteEmp,) LIKE %,"+periodo+",%"));
        				//crit.add(Restrictions.ilike(","+"cli.mesesFacturables"+",", "%,"+periodo+",%"));
        			}
        			
        		}
        		
        		if(conceptoOperacionCliente.getAsignado() !=null){
        			crit.add(Restrictions.eq("asignado", conceptoOperacionCliente.getAsignado()));
        		}
        		
        		if(conceptoOperacionCliente.getCodigoEmpresa() != null && !"".equals(conceptoOperacionCliente.getCodigoEmpresa()))
        			crit.createCriteria("empresa", "emp").add(Restrictions.eq("emp.codigo",conceptoOperacionCliente.getCodigoEmpresa()));
        		
        		if(conceptoOperacionCliente.getCodigoSucursal() != null && !"".equals(conceptoOperacionCliente.getCodigoSucursal()))
        			crit.createCriteria("sucursal", "suc").add(Restrictions.eq("suc.codigo",conceptoOperacionCliente.getCodigoSucursal()));

        		if(conceptoOperacionCliente.getFechaPeriodo() != null && !"".equals(conceptoOperacionCliente.getFechaPeriodo()))
        			crit.add(Restrictions.le("fechaAlta", conceptoOperacionCliente.getFechaPeriodo()));

        		if(conceptoOperacionCliente.getFechaDesde() != null && !"".equals(conceptoOperacionCliente.getFechaDesde()))
        			crit.add(Restrictions.ge("fechaAlta", conceptoOperacionCliente.getFechaDesde()));
        		
        		if(conceptoOperacionCliente.getFechaHasta() != null && !"".equals(conceptoOperacionCliente.getFechaHasta()))
        			crit.add(Restrictions.le("fechaAlta", conceptoOperacionCliente.getFechaHasta()));
        		
        		if(conceptoOperacionCliente.getEstado() !=null && !"".equals(conceptoOperacionCliente.getEstado()))
	        		crit.add(Restrictions.eq("estado", conceptoOperacionCliente.getEstado()));
        		
        		if(conceptoOperacionCliente.getTipoConcepto() !=null && !"".equals(conceptoOperacionCliente.getTipoConcepto()))
	        		crit.add(Restrictions.eq("tipoConcepto", conceptoOperacionCliente.getTipoConcepto()));
        		
        		if(conceptoOperacionCliente.getFinalUnitarioDesde() !=null && !"".equals(conceptoOperacionCliente.getFinalUnitarioDesde()))
	        		crit.add(Restrictions.ge("finalUnitario", conceptoOperacionCliente.getFinalUnitarioDesde()));
        		
        		if(conceptoOperacionCliente.getFinalUnitarioHasta() !=null && !"".equals(conceptoOperacionCliente.getFinalUnitarioHasta()))
	        		crit.add(Restrictions.le("finalUnitario", conceptoOperacionCliente.getFinalUnitarioHasta()));
        		
	        	if(conceptoOperacionCliente.getCantidadDesde() !=null && !"".equals(conceptoOperacionCliente.getCantidadDesde()))
	        		crit.add(Restrictions.ge("cantidad", conceptoOperacionCliente.getCantidadDesde()));
	        	
	        	if(conceptoOperacionCliente.getCantidadHasta() !=null && !"".equals(conceptoOperacionCliente.getCantidadHasta()))
	        		crit.add(Restrictions.le("cantidad", conceptoOperacionCliente.getCantidadHasta()));
	        	
	        	if(conceptoOperacionCliente.getFinalTotalDesde() !=null && !"".equals(conceptoOperacionCliente.getFinalTotalDesde()))
	        		crit.add(Restrictions.ge("finalTotal", conceptoOperacionCliente.getFinalTotalDesde()));
	        	
	        	if(conceptoOperacionCliente.getFinalTotalHasta() !=null && !"".equals(conceptoOperacionCliente.getFinalTotalHasta()))
	        		crit.add(Restrictions.le("finalTotal", conceptoOperacionCliente.getFinalTotalHasta()));
	        	
	        	if(idsAsociados!=null && idsAsociados.size()>0){
	        		Conjunction conjunction = Restrictions.conjunction();
	        		for(Long id : idsAsociados){
	        			conjunction.add(Restrictions.ne("id" , id));
	        		}
	        		crit.add(conjunction);
	        	}
	        	
	        	if((conceptoOperacionCliente.getPrefijoRequerimientoDesde() !=null && !"".equals(conceptoOperacionCliente.getPrefijoRequerimientoDesde()))
	        			||(conceptoOperacionCliente.getPrefijoRequerimientoHasta() !=null && !"".equals(conceptoOperacionCliente.getPrefijoRequerimientoHasta()))
	        			||(conceptoOperacionCliente.getNumeroRequerimientoDesde() !=null && !"".equals(conceptoOperacionCliente.getNumeroRequerimientoDesde()))
	        			||(conceptoOperacionCliente.getNumeroRequerimientoHasta() !=null && !"".equals(conceptoOperacionCliente.getNumeroRequerimientoHasta())))
	        	{
	        		crit.createCriteria("requerimiento","req");
	        		       		
	        		if((conceptoOperacionCliente.getPrefijoRequerimientoDesde() !=null && !"".equals(conceptoOperacionCliente.getPrefijoRequerimientoDesde())))
	        			crit.add(Restrictions.ge("req.prefijo", BigInteger.valueOf(parseLongCodigo(conceptoOperacionCliente.getPrefijoRequerimientoDesde()))));
	        			        		
	        		if((conceptoOperacionCliente.getPrefijoRequerimientoHasta() !=null && !"".equals(conceptoOperacionCliente.getPrefijoRequerimientoHasta())))
	        			crit.add(Restrictions.le("req.prefijo", BigInteger.valueOf(parseLongCodigo(conceptoOperacionCliente.getPrefijoRequerimientoHasta()))));
	        		
	        		if((conceptoOperacionCliente.getNumeroRequerimientoDesde() !=null && !"".equals(conceptoOperacionCliente.getNumeroRequerimientoDesde())))
	        			crit.add(Restrictions.ge("req.numero", BigInteger.valueOf(parseLongCodigo(conceptoOperacionCliente.getNumeroRequerimientoDesde()))));
	        		
	        		if((conceptoOperacionCliente.getNumeroRequerimientoHasta() !=null && !"".equals(conceptoOperacionCliente.getNumeroRequerimientoHasta())))
	        			crit.add(Restrictions.le("req.numero", BigInteger.valueOf(parseLongCodigo(conceptoOperacionCliente.getNumeroRequerimientoHasta()))));
	        	}
	        	
        	}

        	if(cliente != null)
        		crit.add(Restrictions.eq("clienteAsp", cliente));
        	
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        	        	
        	//result = ((Integer)crit.list().get(0));
        	conceptosOperacionCliente = (List<ConceptoOperacionCliente>) crit.list();
        	return conceptosOperacionCliente;
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
	public List obtenerConceptosClientesEnPeriodo(List<Long> idsAsociados,String periodo, String fechaPeriodo, ClienteAsp cliente) {
		
		Session session = null;
		Integer result = null;

		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
	       
//        	String consulta2 = "SELECT  con.id, cli.id AS clienteEmp, con.fechaAlta, con.tipoConcepto, con.finalUnitario, con.cantidad, con.finalTotal, con.estado, con.asignado, con.requerimiento_id " +
//        	" FROM clientesEmp AS cli CROSS JOIN concepto_operacion_cliente AS con " +
//        	" WHERE (',' + cli.mesesFacturables + ',' LIKE '%," +periodo+ ",%') AND (con.clienteAsp_id = "+cliente.getId()+") AND (con.fechaAlta <= "+fechaPeriodo+") ";
        	
        	if(idsAsociados == null){
        		idsAsociados = new ArrayList<Long>();
        		idsAsociados.add((long)0);
        	}
        	
        	String cadenaAsociados = "0";
        	for(Long asociado: idsAsociados)
        	{
        		cadenaAsociados+= ","+asociado;
        	}
        
        	String consulta3 = "SELECT     con.id, perj.razonsocial, (cli.nombre + ' ' + cli.apellido) as Nombre, con.fechaAlta, con.tipoConcepto, con.finalUnitario, con.cantidad, con.finalTotal, con.estado, con.asignado, " + 
            " con.requerimiento_id " +
            " FROM concepto_operacion_cliente con " +
            " left join clientesemp cli on con.clienteemp_id = cli.id " +
            " left join personas_juridicas perj on cli.razonsocial_id = perj.id and cli.tipopersona='Juridica' " +
            " WHERE (',' + cli.mesesFacturables + ',' LIKE '%,"+periodo+",%') AND (con.clienteAsp_id = "+cliente.getId()+") AND (con.fechaAlta <= '"+fechaPeriodo+"') AND (con.asignado = '0') AND (con.id IN " +
            " (SELECT id FROM concepto_operacion_cliente AS c WHERE (c.id NOT IN ("+cadenaAsociados+")))) AND (con.estado = 'Pendiente') ";
        	
        	SQLQuery q = session.createSQLQuery(consulta3);			
			
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
	public List<ConceptoOperacionCliente> listarConceptosPorPreFacturaDetalle(Long idPreFacturaDetalle, ClienteAsp cliente){
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	crit.createCriteria("preFacturaDetalle", "pre");
        	
        	if(idPreFacturaDetalle!=null){
        		crit.add(Restrictions.eq("pre.id", idPreFacturaDetalle));
        	}else
        	{
        		return null;
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