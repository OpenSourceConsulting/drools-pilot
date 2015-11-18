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

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class DBRepository {
	
	public static DBRepository INSTANCE;
	
	private SqlSessionFactory factory;
	private EntityManagerFactory entityManagerFactory;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private DBRepository() {
		
		entityManagerFactory = Persistence.createEntityManagerFactory( "org.hibernate.nice.jpa" );
		
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
		return entityManagerFactory.createEntityManager();
	}

}
//end of DBRepository.java