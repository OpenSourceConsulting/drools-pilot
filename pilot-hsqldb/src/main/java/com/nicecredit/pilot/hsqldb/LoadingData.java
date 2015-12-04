package com.nicecredit.pilot.hsqldb;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.FBApplMst;
import com.nice.pilot.pilot_rule.FBApplPhone;

/**
 * <pre>
 * - create fbappl** tables
 * - data insert to fbappl** tables from mariadb
 * </pre>
 * @author BongJin Kwon
 */
public class LoadingData {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoadingData.class);

	public LoadingData() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		String mode = "all";
		
		if (args.length == 1) {
			mode = args[0];
		}
		
		LOGGER.info("mode is {}", mode);
		
		if ("all".equals(mode) || "mst".equals(mode)) {
			loadMst();
		}
		
		if ("all".equals(mode) || "addr".equals(mode)) {
			loadAddr();
		}
		
		if ("all".equals(mode) || "phone".equals(mode)) {
			loadPhone();
		}
		
		DBRepository.close();

	}
	
	private static void loadMst() {
		
		long etime = 0;
		EntityManager entityManager = null;
		EntityManager emHsql = null;
		EntityTransaction tx = null;
		try {
			entityManager = DBRepository.getInstance().createEntityManager();
			emHsql = DBRepository.getInstance().createEntityManagerHsql();
			
			
			List<FBApplMst> list = entityManager.createNativeQuery("select * from fbapplmst", FBApplMst.class).getResultList();
			LOGGER.info("addr size: " + list.size());
			
			tx = emHsql.getTransaction();
			tx.begin();
			
			for (FBApplMst item : list) {

				//System.out.println(item.toString());
				emHsql.persist(item);
			}
			tx.commit();
			list.clear();
			
			LOGGER.info("persisted all mst.");
			
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
		}
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
		}
	}

}
//end of StartMain.java