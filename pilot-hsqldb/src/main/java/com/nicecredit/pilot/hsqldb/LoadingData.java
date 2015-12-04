package com.nicecredit.pilot.hsqldb;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.FBApplPhone;

/**
 * <pre>
 * - create fbappladdr, fbapplphone table
 * - data insert to fbappladdr, fbapplphone table from mariadb
 * </pre>
 * @author BongJin Kwon
 */
public class LoadingData {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoadingData.class);

	public LoadingData() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		loadAddr();
		loadPhone();

	}
	
	private static void loadAddr() {
		
		long etime = 0;
		EntityManager entityManager = null;
		EntityManager emHsql = null;
		EntityTransaction tx = null;
		try {
			entityManager = DBRepository.getInstance().createEntityManager();
			emHsql = DBRepository.getInstance().createEntityManagerHsql();
			
			
			//List<FBApplAddr> list = entityManager.createNativeQuery("select * from fbappladdr where appl_no='0000000000'", FBApplAddr.class).getResultList();
			List<FBApplAddr> list = entityManager.createNativeQuery("select * from fbappladdr", FBApplAddr.class).getResultList();
			LOGGER.info("addr size: " + list.size());
			
			tx = emHsql.getTransaction();
			tx.begin();
			
			for (FBApplAddr item : list) {

				//System.out.println(item.toString());
				emHsql.persist(item);
			}
			tx.commit();
			list.clear();
			
			LOGGER.info("persisted all addr.");
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.toString(), e);
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
			if (emHsql != null) {
				emHsql.close();
			}
			DBRepository.close();
		}
	}
	
	private static void loadPhone() {
		
		long etime = 0;
		EntityManager entityManager = null;
		EntityManager emHsql = null;
		EntityTransaction tx = null;
		try {
			entityManager = DBRepository.getInstance().createEntityManager();
			emHsql = DBRepository.getInstance().createEntityManagerHsql();
			
			
			List<FBApplPhone> list2 = entityManager.createNativeQuery("select * from fbapplphone", FBApplPhone.class).getResultList();
			LOGGER.info("phone size: " + list2.size());
			
			tx = emHsql.getTransaction();
			tx.begin();
			
			for (FBApplPhone item : list2) {

				//System.out.println(item.toString());
				emHsql.persist(item);
			}
			tx.commit();
			System.out.println("persisted all phone.");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.toString(), e);
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			if (entityManager != null) {
				entityManager.close();
			}
			if (emHsql != null) {
				emHsql.close();
			}
			DBRepository.close();
		}
	}

}
//end of StartMain.java