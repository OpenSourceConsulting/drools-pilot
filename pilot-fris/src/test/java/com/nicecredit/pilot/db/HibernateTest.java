package com.nicecredit.pilot.db;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class HibernateTest {

	EntityManagerFactory entityManagerFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		try{
			entityManagerFactory = Persistence.createEntityManagerFactory( "org.hibernate.nice.jpa" );
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFind() {
		
		try {
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			
			TestResult result = entityManager.find(TestResult.class, 1);
			
			assertNotNull(result);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void testQuery() {
		
		try {
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			
			List<TestResult> list = entityManager.createNamedQuery("myQuery").getResultList();
			
			assertNotNull(list);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	@Test
	public void testQueryWithParams() {
		
		try {
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			
			Query query = entityManager.createNamedQuery("ApplEntity.addr.phone");
			query.setParameter("APPL_NO", "7001683169");
			query.setParameter("VERSION", 1);
			query.setParameter("STORE_CD", "2834014");
			
			List<ApplEntity> list = query.getResultList();
			
			assertNotNull(list);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

}
//end of HibernateTest.java