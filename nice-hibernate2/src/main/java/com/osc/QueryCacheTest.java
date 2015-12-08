/* 
 *
 * Revision History
 * Author             Date              Description
 * ---------------	----------------	------------
 * Jerry Jeong	       2015. 12. 2.		    First Draft.
 */

package com.osc;

import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osc.entity.FBAPPLADDR;
import com.osc.entity.FBAPPLMST;
import com.osc.entity.FBAPPLPHONE;

public class QueryCacheTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryCacheTest.class);
	
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

			/*
				MariaDB [nicepilot]> select count(appl_no) from fbappladdr;
				+----------------+
				| count(appl_no) |
				+----------------+
				|         640512 |
				+----------------+
				1 row in set (0.31 sec)
				
				MariaDB [nicepilot]> select count(distinct appl_no) from fbappladdr;
				+-------------------------+
				| count(distinct appl_no) |
				+-------------------------+
				|                  184571 |
				+-------------------------+
				1 row in set (1.00 sec)
			 */
			
//			Criteria cApplNo = session.createCriteria(FBAPPLADDR.class).setProjection(Projections.distinct(Projections.property("appl_no")));
//			Iterator it = cApplNo.list().iterator();
//			while(it.hasNext()) {
//				String appl_no = (String)it.next();
//				LOGGER.info(appl_no);
//				
//				Criteria cAddr = session.createCriteria(FBAPPLADDR.class).setCacheable(true)
//						.add(Restrictions.eq("appl_no", appl_no));
////						.setCacheRegion(appl_no);
//				cAddr.list();
//			}

			Criteria cAddr = session.createCriteria(FBAPPLADDR.class).setCacheable(true).add(Restrictions.eq("appl_no", "0000000000"));
			LOGGER.info("Size of Addr: " + cAddr.list().size());
			
			Criteria cPhone = session.createCriteria(FBAPPLPHONE.class).setCacheable(true).add(Restrictions.eq("appl_no", "0000000000"));
			LOGGER.info("Size of Phone: " + cPhone.list().size());

			Criteria cMst = session.createCriteria(FBAPPLMST.class).setCacheable(true).add(Restrictions.eq("appl_no", "0000000000"));
			LOGGER.info("Size of MST: " + cMst.list().size());
			
			Iterator it = cAddr.list().iterator();
			while(it.hasNext()) {
				//	addr ���ڵ�(60�� ��)�� �������� ��ü ������ �ƿ� ĳ���ϴ� ���
				//	invalidate�� ����ġ ����. addr_pnu_cd, strt_addr_2�� ������ invalidate
				
				//	start_dtim ������ ������ ��
				//	�� ���� ����� 1��? or 2��?
				FBAPPLADDR addr = (FBAPPLADDR)it.next();
				FBAPPLPHONE pk = new FBAPPLPHONE();
				pk.appl_no = addr.appl_no;
				pk.version = addr.version;
				pk.store_cd = addr.store_cd;
				pk.org_id = addr.org_id;
				pk.wire_mobile_gb = "1";
				pk.start_dtim = addr.start_dtim;
				FBAPPLPHONE phone = session.load(FBAPPLPHONE.class, pk);
				LOGGER.info(phone.toString());
				
				//	ĳ�ð� �Ǿ�� �� (addr�� appl_no, version, store_cd�� Ű�� �ؼ�)
				//	addr�� appl_no, version, store_cd�� �����ϰ� �Ʒ� ���Ǹ����� ����
				Criteria cSelfAddr = session.createCriteria(FBAPPLADDR.class).setCacheable(true)
					.add(Restrictions.eq("addr_pnu_cd", addr.addr_pnu_cd))
					.add(Restrictions.eq("strt_addr_2", addr.strt_addr_2))
					.add(Restrictions.ne("org_id", addr.org_id));
				LOGGER.info("Self Addr: " + cSelfAddr.list().toString());

				//	ĳ�ð� �Ǿ�� �� (addr�� appl_no, version, store_cd�� Ű�� �ؼ�)
				Criteria cSelfPhone = session.createCriteria(FBAPPLPHONE.class).setCacheable(true)
						.add(Restrictions.eq("appl_no", phone.appl_no))
						.add(Restrictions.eq("version", phone.version))
						.add(Restrictions.eq("store_cd", phone.store_cd))
						.add(Restrictions.eq("full_phone_no", phone.full_phone_no))
						.add(Restrictions.ne("org_id", phone.org_id));
					LOGGER.info("Self Phone: " + cSelfPhone.list().toString());

//				Criteria cPhoneSub = session.createCriteria(FBAPPLPHONE.class).setCacheable(true).add(Restrictions.eq("appl_no", "0000000000"));
//				cPhoneSub
//					.add(Restrictions.eq("version", addr.version))
//					.add(Restrictions.eq("store_cd", addr.store_cd))
//					.add(Restrictions.eq("org_id", addr.org_id))
//					.add(Restrictions.eq("wire_mobile_gb", "1"));
//				LOGGER.info("PhoneSub: " + cPhoneSub.list().size());
			}
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