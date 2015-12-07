package com.nicecredit.pilot.db;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.FBApplPhone;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class ItemQueryTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemQueryTest.class);

	//EntityManagerFactory entityManagerFactory;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DBRepository.close();
	}

	@Before
	public void setUp() throws Exception {
		/*
		try{
			entityManagerFactory = Persistence.createEntityManagerFactory( "org.hibernate.nice.jpa" );
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		*/
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	
	@Test
	public void testHSQLDBinit() {
		
		long etime = 0;
		EntityManager entityManager = null;
		EntityManager emHsql = null;
		SqlSession sqlSession = null;
		try {
			entityManager = DBRepository.getInstance().createEntityManager();
			emHsql = DBRepository.getInstance().createEntityManagerHsql();
			sqlSession = DBRepository.getInstance().openSession();
			
			long start = System.currentTimeMillis();
			
			
			//List<InMemData> list = sqlSession.selectList("PilotMapper.selectINMEM_DATAList");
			//List<FBApplAddr> list = sqlSession.selectList("PilotMapper.selectFBApplAddrList");
			
			//Session session = entityManager.unwrap(org.hibernate.Session.class);
			//List<InMemData> list = session.createCriteria(InMemData.class).list();
			List<FBApplAddr> list = entityManager.createNativeQuery("select * from fbappladdr where appl_no='0000000000'", FBApplAddr.class).getResultList();
			List<FBApplPhone> list2 = entityManager.createNativeQuery("select * from fbapplphone where appl_no='0000000000'", FBApplPhone.class).getResultList();
			
			System.out.println("list size: " + list.size());
			
			emHsql.getTransaction().begin();
			//for (FBApplAddr item : list) {
			for (FBApplAddr item : list) {

				//System.out.println(item.toString());
				emHsql.persist(item);
			}
			emHsql.getTransaction().commit();
			
			System.out.println("hsql count: " + emHsql.createNativeQuery("select count(*) from fbappladdr where appl_no='0000000000'").getSingleResult()); 
			//System.out.println("hsql count: " + emHsql.createNativeQuery("select count(*) from fbapplphone where appl_no='0000000000'").getSingleResult()); 
			
			etime = System.currentTimeMillis() - start;
			System.out.println("elapsed time: " + etime);
			
			//assertNotNull(result);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
		
	}
	
	@Test
	public void testItemQuery() {
		
		long etime = 0;
		EntityManager entityManager = null;
		EntityManager emHsql = null;
		SqlSession sqlSession = null;
		try {
			entityManager = DBRepository.getInstance().createEntityManager();
			emHsql = DBRepository.getInstance().createEntityManagerHsql();
			sqlSession = DBRepository.getInstance().openSession();
			
			long start = System.currentTimeMillis();
			
			
			
			System.out.println("hsql count: " + emHsql.createNativeQuery("select count(*) from fbapplmst").getSingleResult()); 
			//System.out.println("hsql count: " + emHsql.createNativeQuery("select count(*) from fbapplphone where appl_no='0000000000'").getSingleResult()); 
			
			
			etime = System.currentTimeMillis() - start;
			System.out.println("elapsed time: " + etime);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
			
			if (emHsql != null) {
				emHsql.close();
			}
		}
		
	}
	

}
//end of ItemQueryTest.java