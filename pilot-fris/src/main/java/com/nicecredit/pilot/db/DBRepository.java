package com.nicecredit.pilot.db;

import java.io.IOException;
import java.io.InputStream;

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

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private DBRepository() {
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

}
//end of DBRepository.java