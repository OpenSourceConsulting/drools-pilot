/* 
 *
 * Revision History
 * Author             Date              Description
 * ---------------	----------------	------------
 * Jerry Jeong	       2015. 12. 3.		    First Draft.
 */

package com.osc.infini;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.query.Search;
import org.infinispan.query.SearchManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.osc.QueryCacheTest;
import com.osc.entity.FBAPPLADDR;
import com.osc.entity.FBAPPLMST;
import com.osc.entity.FBAPPLPHONE;

public class InfinispanCache {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryCacheTest.class);

	public static void main(String[] args) {
		Cache<Object, Object> cacheAddr = null, cacheMst = null, cachePhone = null;
		DefaultCacheManager mgr = null;
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

			mgr = new DefaultCacheManager("infinispan-hibernate.xml");			
			cacheAddr = mgr.getCache("fbappladdr");
			cacheMst = mgr.getCache("fbapplmst");
			cachePhone = mgr.getCache("fbapplphone");
			
//			if(cacheAddr.size() == 0 || cacheMst.size() == 0 || cachePhone.size() == 0) {
				LOGGER.info("Cache will be created");
				
				cacheAddr.clear();
				cacheMst.clear();
				cachePhone.clear();
				
				Criteria cAddr = session.createCriteria(FBAPPLADDR.class).setCacheable(true)
//						.add(Restrictions.eq("org_id", "16707603"))
//						.add(Restrictions.eq("appl_no", "0000000000"))
						;
				LOGGER.info("Size of Addr: " + cAddr.list().size());
				
				Criteria cPhone = session.createCriteria(FBAPPLPHONE.class).setCacheable(true)
						.add(Restrictions.eq("appl_no", "0000000000"));
				LOGGER.info("Size of Phone: " + cPhone.list().size());

				Criteria cMst = session.createCriteria(FBAPPLMST.class).setCacheable(true)
						.add(Restrictions.eq("appl_no", "0000000000"));
				LOGGER.info("Size of MST: " + cMst.list().size());

				int i = 0;
				for(Iterator it = cAddr.list().iterator(); it.hasNext(); i++) {
					FBAPPLADDR a = (FBAPPLADDR)it.next();
					cacheAddr.put(UUID.randomUUID(), a);
					if(i % 1000 == 0) LOGGER.info("Caching Addr: " + a);
				}
				
				LOGGER.info("Cache Addr has been created");

//				for(Iterator it = cMst.list().iterator(); it.hasNext();) {
//					cacheMst.put(UUID.randomUUID(), it.next());
//				}
//				
//				LOGGER.info("Cache Mst has been created");

				for(Iterator it = cPhone.list().iterator(); it.hasNext();) {
					FBAPPLPHONE phone = (FBAPPLPHONE)it.next();
					cachePhone.put(UUID.randomUUID(), phone);
					
//					LOGGER.debug("Caching Phone, " + phone);
				}
				
				LOGGER.info("Cache Phone has been created");
//			}
//			else {
//				LOGGER.info("Cache will be reused");
//				LOGGER.info("Cache Addr Size: " + cacheAddr.size());
//				LOGGER.info("Cache Mst Size: " + cacheMst.size());
//				LOGGER.info("Cache Phone Size: " + cachePhone.size());
//			}
			
			
			/*
			 * SELECT DISTINCT t4.APPL_NO ,      t4.VERSION ,      t4.STORE_CD
				FROM fbappladdr s0 ,      fbappladdr t2 ,      fbapplphone s3 ,      fbapplphone t4
				WHERE s0.APPL_NO = '0000000000'   AND s0.VERSION = 560  AND s0.STORE_CD = '0812676'  
				AND t2.addr_pnu_cd = s0.addr_pnu_cd   AND t2.strt_addr_2 = s0.strt_addr_2   AND t2.ORG_ID <> s0.ORG_ID  
				AND s3.APPL_NO = s0.APPL_NO   AND s3.VERSION = s0.VERSION   AND s3.STORE_CD = s0.STORE_CD   AND s3.ORG_ID = s0.ORG_ID   AND s3.wire_mobile_gb = '1'  
				AND t4.APPL_NO = t2.APPL_NO   AND t4.VERSION = t2.VERSION   AND t4.STORE_CD = t2.STORE_CD   AND t4.full_phone_no = s3.full_phone_no   AND t4.ORG_ID <> s3.ORG_ID;
				
				쿼리 조건을 네 개로 분리...
				  (1) 전문에 들어온 appl_no, version, store_cd로 레코드 선택 (primary key는 5개 이므로 복수 개의 레코드가 조회됨)
				  (2) appl_no, version, store_cd는 다른데 addr_pnu_cd, strt_addr_2가 같은 그러면서도 org_id는 다른 레코드들을 동일한 fbappladdr 테이블에서 조회
				  (3) fbapplphone 테이블은 appl_no, version, store_cd, org_id가 동일하면서 wire_mobile_gb가 1인 조건으로 연결
				  (4) fbapplphone 테이블에서도 appl_no, version, store_cd, full_phone_no는 동일한데 org_id가 다른 레코드들이 있을 수 있어 조회
			 */
			
			//	  (1) 전문에 들어온 appl_no, version, store_cd로 레코드 선택 (primary key는 5개 이므로 복수 개의 레코드가 조회됨)
			
			for(Iterator itx = cAddr.list().iterator(); itx.hasNext(); ) {
				FBAPPLADDR x = (FBAPPLADDR)itx.next();
				LOGGER.info("ITERATION X: " + x);
			
			SearchManager sMgrAddr = Search.getSearchManager(cacheAddr);
			QueryBuilder qbAddr = sMgrAddr.buildQueryBuilderForClass(FBAPPLADDR.class).get();
			Query queryAddr = qbAddr.bool()
				.must(qbAddr.phrase().onField("appl_no").sentence(x.appl_no).createQuery())
				.must(qbAddr.range().onField("version").from(x.version).to(x.version).createQuery())
				.must(qbAddr.phrase().onField("store_cd").sentence(x.store_cd).createQuery())
//				.must(qbAddr.phrase().onField("appl_no").sentence("0000000000").createQuery())
//				.must(qbAddr.range().onField("version").from(560d).to(560d).createQuery())
//				.must(qbAddr.phrase().onField("store_cd").sentence("0812676").createQuery())
				.createQuery();
			List resultAddr = sMgrAddr.getQuery(queryAddr, FBAPPLADDR.class).list();
			for(Iterator it = resultAddr.iterator(); it.hasNext(); ) {
				FBAPPLADDR addr = (FBAPPLADDR)it.next();
				LOGGER.info("FBAPPLADDR: " + addr);

				//  (3) fbapplphone 테이블은 appl_no, version, store_cd, org_id가 동일하면서 wire_mobile_gb가 1인 조건으로 연결
				
				SearchManager sMgrPhone = Search.getSearchManager(cachePhone);
				QueryBuilder qbPhone = sMgrPhone.buildQueryBuilderForClass(FBAPPLPHONE.class).get();
				Query queryPhone = qbPhone.bool()
					.must(qbPhone.phrase().onField("appl_no").sentence(addr.appl_no).createQuery())
					.must(qbPhone.range().onField("version").from(addr.version).to(addr.version).createQuery())
					.must(qbPhone.phrase().onField("store_cd").sentence(addr.store_cd).createQuery())
					.must(qbPhone.phrase().onField("org_id").sentence(addr.org_id).createQuery())
//					.must(qbPhone.phrase().onField("appl_no").sentence("0000000000").createQuery())
//					.must(qbPhone.range().onField("version").from(560d).to(560d).createQuery())
//					.must(qbPhone.phrase().onField("store_cd").sentence("0812676").createQuery())
//					.must(qbPhone.phrase().onField("org_id").sentence("16707603").createQuery())
					.must(qbPhone.phrase().onField("wire_mobile_gb").sentence("1").createQuery())
					.createQuery();
				List resultPhone = sMgrPhone.getQuery(queryPhone, FBAPPLPHONE.class).list();
				LOGGER.debug("resultPhone.size: " + resultPhone.size());
				for(Iterator itPhone = resultPhone.iterator(); itPhone.hasNext(); ) {
					FBAPPLPHONE phone = (FBAPPLPHONE)itPhone.next();
					LOGGER.info("FBAPPLPHONE: " + phone);
					
					//  (4) fbapplphone 테이블에서도 appl_no, version, store_cd, full_phone_no는 동일한데 org_id가 다른 레코드들이 있을 수 있어 조회
					
					SearchManager sMgrPhone2 = Search.getSearchManager(cachePhone);
					QueryBuilder qbPhone2 = sMgrPhone2.buildQueryBuilderForClass(FBAPPLPHONE.class).get();
					Query queryPhone2 = qbPhone2.bool()
							.must(qbPhone2.phrase().onField("appl_no").sentence(phone.appl_no == null ? "" : phone.appl_no).createQuery())
							.must(qbPhone2.range().onField("version").from(phone.version).to(phone.version).createQuery())
							.must(qbPhone2.phrase().onField("store_cd").sentence(phone.store_cd == null ? "" : phone.store_cd).createQuery())
//							.must(qbPhone2.phrase().onField("full_phone_no").sentence("022110133").createQuery())
							.must(qbPhone2.phrase().onField("full_phone_no").sentence(phone.full_phone_no == null ? "" : phone.full_phone_no).createQuery())
							.must(qbPhone2.phrase().onField("org_id").sentence(phone.org_id).createQuery()).not()
							.createQuery();

					List resultPhone2 = sMgrPhone2.getQuery(queryPhone2, FBAPPLPHONE.class).list();
					for(Iterator itPhone2 = resultPhone2.iterator(); itPhone2.hasNext(); ) {
						FBAPPLPHONE phone2 = (FBAPPLPHONE)itPhone2.next();
						LOGGER.info("FBAPPLPHONE2: " + phone2);
					}
				}

				//	(2) appl_no, version, store_cd는 다른데 addr_pnu_cd, strt_addr_2가 같은 그러면서도 org_id는 다른 레코드들을 동일한 fbappladdr 테이블에서 조회
				
				QueryBuilder qbAddr2 = sMgrAddr.buildQueryBuilderForClass(FBAPPLADDR.class).get();
				Query queryAddr2 = qbAddr2.bool()
					.must(qbAddr2.phrase().onField("addr_pnu_cd").sentence(addr.addr_pnu_cd == null ? "" : addr.addr_pnu_cd).createQuery())
					.must(qbAddr2.phrase().onField("strt_addr_2").sentence(addr.strt_addr_2 == null ? "" : addr.strt_addr_2).createQuery())
					.must(qbAddr2.phrase().onField("org_id").sentence(addr.org_id).createQuery()).not()
					.createQuery();
				List resultAddr2 = sMgrAddr.getQuery(queryAddr2, FBAPPLADDR.class).list();
				for(Iterator itAddr2 = resultAddr2.iterator(); itAddr2.hasNext(); ) {
					FBAPPLADDR addr2 = (FBAPPLADDR)itAddr2.next();
					LOGGER.info("FBAPPLADDR2: " + addr2);
				}				
			}
			
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(session != null) session.close();
			if(sessionFactory != null) sessionFactory.close();
			if(cacheAddr != null) cacheAddr.stop();
			if(cacheMst != null) cacheMst.stop();
			if(cachePhone != null) cachePhone.stop();
			if(mgr != null) mgr.stop();
		}
	}
}
//end of InfinispanCache.java