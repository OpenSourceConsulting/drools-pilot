package com.nicecredit.pilot.hsqldb;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class DBRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DBRepository.class);
	
	public static DBRepository INSTANCE;
	
	private EntityManagerFactory emFactory;
	private EntityManagerFactory emFactoryHsql;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private DBRepository() {
		
		emFactory = Persistence.createEntityManagerFactory( "org.hibernate.nice.jpa" );
		emFactoryHsql = Persistence.createEntityManagerFactory( "org.hibernate.nice.hsql" );
		
	}
	
	public static DBRepository getInstance() {
		
		if (INSTANCE == null) {
			synchronized (DBRepository.class) {
				if (INSTANCE == null) {
					INSTANCE = new DBRepository();
				}
			}
		}
		
		return INSTANCE;
	}
	
	/**
	 * <pre>
	 * for hibernate
	 * </pre>
	 * @return
	 */
	public EntityManager createEntityManager() {
		return emFactory.createEntityManager();
	}
	
	/**
	 * <pre>
	 * for hibernate (HSQL)
	 * </pre>
	 * @return
	 */
	public EntityManager createEntityManagerHsql() {
		return emFactoryHsql.createEntityManager();
	}
	
	public static void close() {
		
		if (INSTANCE != null) {
			synchronized (DBRepository.class) {
				if (INSTANCE != null) {
					INSTANCE.doClose();
					INSTANCE = null;
				}
			}
		}
	}
	
	private void doClose() {
		
		if (emFactory != null) {
			emFactory.close();
			emFactory = null;
		}
		
		if (emFactoryHsql != null) {
			emFactoryHsql.close();
			emFactoryHsql = null;
		}
		
		LOGGER.info("all EntityManagerFactory closed!!");
	}

}
//end of DBRepository.java