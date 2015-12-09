/* 
 *
 * Revision History
 * Author             Date              Description
 * ---------------	----------------	------------
 * Jerry Jeong	       2015. 12. 2.		    First Draft.
 */

package com.osc;

import org.hibernate.Cache;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osc.entity.FBAPPLADDR;
import com.osc.entity.FBAPPLMST;
import com.osc.entity.FBAPPLPHONE;

public class BasicCacheTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicCacheTest.class);
	
	public static void main(String[] args) {
		SessionFactory sessionFactory = null;
		Session session = null;
		try {
			Configuration configuration = new Configuration().configure("hibernate.cfg.xml")
					.addAnnotatedClass(FBAPPLADDR.class)
					.addAnnotatedClass(FBAPPLMST.class)
					.addAnnotatedClass(FBAPPLPHONE.class);
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
			sessionFactory = configuration.buildSessionFactory(builder.build());
			session = sessionFactory.openSession();
			
			FBAPPLADDR pk = new FBAPPLADDR();
			pk.appl_no = "0000000000";
			pk.version = 560d;
			pk.store_cd = "0812676";
			pk.org_id = "16707603";
			pk.start_dtim = "20151021192306";
//			for(int i = 0; i < 10; i++) {
//				FBAPPLADDR result = session.load(FBAPPLADDR.class, pk);
//				LOGGER.info(i + ": " + result);
//			}
			
//			Criteria ca = session.createCriteria(FBAPPLADDR.class).setCacheable(true).setCacheRegion("between").add(Restrictions.between("appl_no", "0000000000", "0000000168"));
			Criteria ca = session.createCriteria(FBAPPLADDR.class).setCacheable(true).setCacheRegion("0000000000").add(Restrictions.eq("appl_no", "0000000000"));
			LOGGER.info("Size: " + ca.list().size());
//			Iterator it = ca.list().iterator();
//			while(it.hasNext()) {
//				FBAPPLADDR aRec = (FBAPPLADDR)it.next();
//				System.out.println(aRec);
//			}

//			Transaction tx = session.beginTransaction();
//			FBAPPLADDR newRec = session.load(FBAPPLADDR.class, pk);
//			LOGGER.info("newRec=" + newRec);
//			LOGGER.info("newRec.nice_fraud_stat_cd=" + newRec.nice_fraud_stat_cd);
//			if(newRec.nice_fraud_stat_cd.equals("5")) {
//				newRec.nice_fraud_stat_cd = "9";		// Original Value: 5
//			}
//			else {
//				newRec.nice_fraud_stat_cd = "5";		// Original Value: 5
//			}
//			Object id = session.save(newRec);
//			LOGGER.info("Save ID: " + id);
//			session.flush();
//			LOGGER.info("Inserted or Updated new record");
//			tx.commit();
			
//			ca.add(Restrictions.eq("version", 560d));
			for(int i = 0; i < 10; i++) {
				if(i == 5) sessionFactory.getCache().evictQueryRegion("0000000000");
				LOGGER.info(i + ": Size: " + ca.list().size());
			}

//			Criteria cm = session.createCriteria(FBAPPLMST.class);
//			cm.setMaxResults(100);
//			cm.list();
//			
//			Criteria cp = session.createCriteria(FBAPPLPHONE.class);
//			cp.setMaxResults(100);
//			cp.list();

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(session != null) session.close();
			if(sessionFactory != null) sessionFactory.close();
		}
	}
}
//end of Test.java