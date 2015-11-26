package com.nicecredit.pilot.db;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nice.pilot.pilot_rule.FBApplMst;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class HibernateTest {

	//EntityManagerFactory entityManagerFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
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
	
	/*
	 * infinispan 연동 튜닝용.
	 */
	@Test
	public void testFindLoop() {
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			testFind();
		}
		System.out.println("--------------- elapsed time: " + (System.currentTimeMillis() - start));
	}
	
	@Test
	public void testFind() {
		
		EntityManager entityManager = null;
		try {
			entityManager = DBRepository.getInstance().createEntityManager();
			
			long start = System.currentTimeMillis();
			
			for (int i = 392; i < 432; i++) {
				TestResult result = entityManager.find(TestResult.class, 392);
			}
			System.out.println("elapsed time: " + (System.currentTimeMillis() - start));
			
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
	public void testQuery() {
		
		EntityManager entityManager = null;
		try {
			entityManager = DBRepository.getInstance().createEntityManager();
			
			List<TestResult> list = entityManager.createNamedQuery("myQuery").getResultList();
			
			assertNotNull(list);
			
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
	public void testQueryWithParams() {
		
		EntityManager entityManager = null;
		try {
			entityManager = DBRepository.getInstance().createEntityManager();
			
			Query query = entityManager.createNamedQuery("ApplEntity.addr.phone");
			query.setParameter("APPL_NO", "7001683169");
			query.setParameter("VERSION", 1);
			query.setParameter("STORE_CD", "2834014");
			
			List<ApplEntity> list = query.getResultList();
			
			assertNotNull(list);
			
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
	public void testPersistence() {
		
		EntityManager entityManager = null;
		EntityTransaction tx = null;
		
		FBApplMst mst = new FBApplMst();
		mst.setAppl_no("12345");
		mst.setStore_cd("9999");
		mst.setVersion(1);
		mst.setIp_addr("ip_addr");
		
		try {
			entityManager = DBRepository.getInstance().createEntityManager();
			tx = entityManager.getTransaction();
			tx.begin();
			
			entityManager.persist(mst);
			
			entityManager.remove(mst);
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			fail(e.toString());
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}
	
	@Test
	public void testPersistence2() {
		
		EntityManager entityManager = null;
		EntityTransaction tx = null;
		
		TestResult result = new TestResult();
		result.setAppl_no("12345");
		result.setStore_cd("9999");
		result.setVersion(1);
		result.setResp_cd("JUNIT");
		
		TestRegitDetail detail = new TestRegitDetail();
		detail.setAppl_no("12345");
		detail.setResult(result);
		
		result.addDetail(detail);
		
		try {
			entityManager = DBRepository.getInstance().createEntityManager();
			tx = entityManager.getTransaction();
			tx.begin();
			
			entityManager.persist(result);
			
			entityManager.remove(result);
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			fail(e.toString());
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
		}
	}

}
//end of HibernateTest.java