package com.nicecredit.pilot.hsqldb;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class DBRepository {
	
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
	}

}
//end of DBRepository.java