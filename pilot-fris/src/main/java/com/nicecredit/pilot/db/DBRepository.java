package com.nicecredit.pilot.db;

import java.io.IOException;
import java.io.InputStream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
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
	
	private SqlSessionFactory factory;
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
		
		try {
			InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
			factory = builder.build(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
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
	
	public SqlSession openSession() {
		return factory.openSession();
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