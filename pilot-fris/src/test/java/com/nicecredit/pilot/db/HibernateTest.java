package com.nicecredit.pilot.db;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nice.pilot.pilot_rule.FBApplMst;
import com.nice.pilot.pilot_rule.InMemData;

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
		
		long total_time = 0;
		for (int i = 0; i < 20; i++) {
			total_time = total_time + testFind();
			//total_time = total_time + testFindMyBatis();
		}
		System.out.println("--------------- elapsed time: " + total_time);
	}
	
	//@Test
	public long testFind() {
		
		long etime = 0;
		EntityManager entityManager = null;
		try {
			entityManager = DBRepository.getInstance().createEntityManager();
			
			long start = System.currentTimeMillis();
			
			for (int i = 392; i < 432; i++) {
				TestResult result = entityManager.find(TestResult.class, i);
			}
			
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
		
		return etime;
	}
	
	//@Test
	public long testFindMyBatis() {
		
		long etime = 0;
		SqlSession sqlSession = null;
		try {
			sqlSession = DBRepository.getInstance().openSession();
			
			
			
			long start = System.currentTimeMillis();
			
			for (int i = 392; i < 432; i++) {
				TestResult result = sqlSession.selectOne("PilotMapper.selectTestResult", i);
			}
			
			etime = System.currentTimeMillis() - start;
			System.out.println("my elapsed time: " + etime);
			
			//assertNotNull(result);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		
		return etime;
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