package com.security.accesoDatos.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

/**
 * Realiza el control de las sesiones de Hibernate. Observar la anotación @Component
 * que es de SPRING. Esto le indica a Spring que esta clase es un recurso que
 * debe administrar y puede inyectar en otras clases.
 * 
 * @author Federico Muñoz
 * 
 */
@Component
public class HibernateControl {

	private static SessionFactory factory;
	private static HibernateControl myInstance;

	/**
	 * Constructor, crea el SessionFactory.
	 */
	public HibernateControl() {
		Configuration conf = new AnnotationConfiguration();
		/* CONFIGURACION ORACLE */
//		conf.configure("hibernate.oracle.cfg.xml");
//		conf.setNamingStrategy(new org.hibernate.cfg.DefaultNamingStrategy() {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public String classToTableName(String className) {
//				return className;
//			}
//		});
		//*/
		//CONFIGURACION MYSQL sin NamingStrategy
//		conf.configure("hibernate.mysql.cfg.xml");
		
		//CONFIGURACION MYSQL sin NamingStrategy
		conf.configure("hibernate.sqlserver.cfg.xml"); 
		
		factory = conf.buildSessionFactory();
		myInstance = this;
	}

	/**
	 * Retorna una sesión de Hibernate.
	 * 
	 * @return
	 */
	public Session getSession() {
		return factory.openSession();
	}
	
	public synchronized static HibernateControl getInstance(){
		if(myInstance == null) 
			myInstance = new HibernateControl();
		return myInstance;
	}

}