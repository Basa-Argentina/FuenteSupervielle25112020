/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 08/06/2011
 */
package com.security.accesoDatos.configuraciongeneral.hibernate;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.configuraciongeneral.interfaz.LoteExportacionRearchivoService;
import com.security.accesoDatos.hibernate.GestorHibernate;
import com.security.accesoDatos.hibernate.HibernateControl;
import com.security.modelo.administracion.ClienteAsp;
import com.security.modelo.configuraciongeneral.LoteExportacionRearchivo;
import com.security.modelo.configuraciongeneral.LoteRearchivo;
import com.security.utils.DateUtil;

/**
 * 
 * @author Federico Mz
 *
 */
@Component
public class LoteExportacionRearchivoServiceImp extends GestorHibernate<LoteExportacionRearchivo> implements LoteExportacionRearchivoService {
	private static Logger logger=Logger.getLogger(LoteExportacionRearchivoServiceImp.class);
	
	@Autowired
	public LoteExportacionRearchivoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);
	}

	@Override
	public Class<LoteExportacionRearchivo> getClaseModelo() {
		return LoteExportacionRearchivo.class;
	}

	@Override
	public List<LoteExportacionRearchivo> obtenerPor(ClienteAsp clienteAsp,
			String codigoEmpresa, String codigoSucursal, String codigoCliente,
			Integer codigoClasificacionDocumental, Long codigoDesde,
			Long codigoHasta, Date fechaDesde, Date fechaHasta) {
		Session session = null;
		Integer result = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
	       
        	 crit.add(Restrictions.eq("clienteAsp", clienteAsp));
 	        
         	if(codigoEmpresa!=null && !codigoEmpresa.isEmpty()){
         		crit.createCriteria("empresa", "emp");
         		crit.add(Restrictions.eq("emp.codigo", codigoEmpresa));
         	}
         	if(codigoSucursal!=null && !codigoSucursal.isEmpty()){
         		crit.createCriteria("sucursal", "suc");
         		crit.add(Restrictions.eq("suc.codigo", codigoSucursal));
         	}
         	if(codigoCliente!=null && !codigoCliente.isEmpty()){
         		crit.createCriteria("clienteEmp", "cli");
         		crit.add(Restrictions.eq("cli.codigo", codigoCliente));
         	}
         	if(codigoDesde!=null)
         		crit.add(Restrictions.ge("id", codigoDesde));
         	if(codigoHasta!=null && codigoHasta.intValue()!=0)
         		crit.add(Restrictions.le("id", codigoHasta));
         	if(fechaDesde!=null){
         		crit.add(Restrictions.ge("fechaRegistro", DateUtil.getDateFrom(fechaDesde)));
         	}
         	if(fechaHasta!=null){
         		crit.add(Restrictions.le("fechaRegistro", DateUtil.getDateTo(fechaHasta)));
         	}
         	if(codigoClasificacionDocumental!=null){
         		crit.createCriteria("clasificacionDocumental", "cla");
         		crit.add(Restrictions.eq("cla.codigo", codigoClasificacionDocumental));
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
	public LoteExportacionRearchivo obtenerPorIdConLoteReferencias(Long idLoteExportacionRearchivo) {
		Session session = null;
		
        try {
        	//obtenemos una sesión
			session = getSession();
        	LoteExportacionRearchivo loteExportacion = (LoteExportacionRearchivo) session.get(getClaseModelo(), idLoteExportacionRearchivo);
        	for(LoteRearchivo loteRearchivo:loteExportacion.getLotesRearchivo()){}
        	
        	return loteExportacion;
        	
        } catch (HibernateException hibernateException) {
        	logger.error("No se pudo obtener el lote ", hibernateException);
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
