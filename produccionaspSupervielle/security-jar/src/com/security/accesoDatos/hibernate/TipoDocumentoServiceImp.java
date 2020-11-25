/*
 * Copyright 2011 Dividato. All rights reserved
 *
 * Copyright Version 1.0
 *
 * Create on 20/05/2011
 */
package com.security.accesoDatos.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.security.accesoDatos.interfaz.TipoDocumentoService;
import com.security.modelo.general.Pais;
import com.security.modelo.general.TipoDocumento;

/**
 * @author Ezequiel Beccaria
 *
 */
@Component
public class TipoDocumentoServiceImp extends GestorHibernate<TipoDocumento> implements TipoDocumentoService{
	private static Logger logger=Logger.getLogger(TipoDocumentoServiceImp.class);
	@Autowired
	public TipoDocumentoServiceImp(HibernateControl hibernateControl) {
		super(hibernateControl);		
	}

	@Override
	public Class<TipoDocumento> getClaseModelo() {
		return TipoDocumento.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public TipoDocumento getTipoDocumentoPorCodigo(String codigo) {
		Session session = null;
        try {
        	//obtenemos una sesión
			session = getSession();
        	Criteria crit = session.createCriteria(getClaseModelo());
        	//filtro por nombre
        	if(codigo != null && !"".equals(codigo))
        		crit.add(Restrictions.eq("codigo",codigo));
        	//solo listar etidades diferentes
        	crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List<TipoDocumento> salida = crit.list();
            if(salida!=null && salida.size()>0)
            	return salida.get(0);
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
}
