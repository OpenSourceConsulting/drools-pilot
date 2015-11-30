package com.nicecredit.pilot.consumer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.InMemData;
import com.nice.pilot.pilot_rule.Result1;
import com.nicecredit.pilot.cache.InfinispanHandler;
import com.nicecredit.pilot.db.DBRepository;
import com.nicecredit.pilot.db.TestResult;
import com.nicecredit.pilot.rule.PilotRuleExecutor;
import com.nicecredit.pilot.rule.RuleExecutor;
import com.nicecredit.pilot.util.Utils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

/**
 * <pre>
 * Rule 처리를 위한 data consumer
 * </pre>
 * @author BongJin Kwon
 */
public class RuleDataConsumer extends BaseConsumer {
	
	public static final String CONSUMER_TAG = "RuleDataConsumer";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RuleDataConsumer.class);
	
	private static final String NAMED_QUERY1 = "ApplEntity.addr.phone";
	private static final String NAMED_QUERY2 = "ApplEntity2.mphone.wphone";
	private static final String NAMED_QUERY3 = "ApplEntity3.fraud";
	
	private static final String PARAM_APPL_NO = "APPL_NO";
	private static final String PARAM_STORE_CD = "STORE_CD";
	private static final String PARAM_VERSION = "VERSION";
	
	//private RuleExecutor ruleExecutor = new TestRuleExecutor();
	private RuleExecutor ruleExecutor = new PilotRuleExecutor();

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param channel
	 */
	public RuleDataConsumer(Channel channel) {
		super(channel);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws IOException {
		
		long start = System.currentTimeMillis();
		
		String telegram = new String(body);
		LOGGER.debug("telegram:{}", telegram);
		
		Map<String, Object> teleMap = null;
		Result1 result = null;
		String err_msg = null;
		try {
			/*
			 * 전문 파싱.
			 */
			teleMap = Utils.parseTelegram(telegram);
			
			/*
			 * 전문 저장
			 */
			saveTelegram(teleMap);
			
			/*
			 * Profiling
			 */
			profiling(teleMap);
			
			
			/*
			 * 룰 실행.
			 */
			result = (Result1)ruleExecutor.execute(teleMap);
	        
	        
	   	 
		} catch (Exception e) {
			err_msg = e.toString();
			LOGGER.error(err_msg, e);
			
		} finally {
			
			/*
	         * 결과저장
	         */
	        TestResult res = saveResult(result, start, teleMap, telegram, err_msg);
	        
	        /*
	         * 응답 전문 보내기.
	         */
	        sendResponse(res);
			
			sendAck(envelope);
		}
		
		
	}
	
	/**
	 * <pre>
	 * 전문 저장.
	 * </pre>
	 * @param teleMap
	 */
	private void saveTelegram(Map<String, Object> teleMap) {
		LOGGER.debug("saving telegram.");
		
		saveTelegram(teleMap.get(Utils.KEY_FBAPPLMST), teleMap.get(Utils.KEY_FBAPPLADDR), teleMap.get(Utils.KEY_FBAPPL_WPHONE), teleMap.get(Utils.KEY_FBAPPL_MPHONE));
	}
	
	private void profiling(Map<String, Object> teleMap) {
		FBApplAddr addr = (FBApplAddr)teleMap.get(Utils.KEY_FBAPPLADDR);
		
		
		
		
		if (Utils.MyBatis_Based) {
			InMemData inMemData = (InMemData)InfinispanHandler.getInstance().get(addr.getOrg_id());
			
			
			if (inMemData == null) {
				SqlSession sqlSession = DBRepository.getInstance().openSession();
				
				try {
					inMemData = sqlSession.selectOne("PilotMapper.selectINMEM_DATA", addr.getOrg_id());
				} finally {
					if (sqlSession != null) {
						sqlSession.close();
					}
				}
			}
			
			teleMap.put(Utils.KEY_INMEM, inMemData);
		} else {
			
			EntityManager entityManager = null;
			
			try {
				entityManager = DBRepository.getInstance().createEntityManager();
				teleMap.put(Utils.KEY_INMEM, entityManager.find(InMemData.class, addr.getOrg_id()));
			} finally {
				if (entityManager != null) {
					entityManager.close();
				}
			}
			
		}
		
		/*
		EntityManager entityManager = DBRepository.getInstance().createEntityManager();

		//Query query1 = entityManager.createNativeQuery("SELECT addr_pnu_cd FROM fbappladdr WHERE appl_no = ? and store_cd = ? and version = ?");
		Query query1 = entityManager.createNamedQuery(NAMED_QUERY1);
		Query query2 = entityManager.createNamedQuery(NAMED_QUERY2);
		Query query3 = entityManager.createNamedQuery(NAMED_QUERY3);
		
		query1.setParameter(PARAM_APPL_NO, addr.getAppl_no());
		query1.setParameter(PARAM_STORE_CD, addr.getStore_cd());
		query1.setParameter(PARAM_VERSION, addr.getVersion());
		
		query2.setParameter(PARAM_APPL_NO, addr.getAppl_no());
		query2.setParameter(PARAM_STORE_CD, addr.getStore_cd());
		query2.setParameter(PARAM_VERSION, addr.getVersion());
		
		query3.setParameter(PARAM_APPL_NO, addr.getAppl_no());
		query3.setParameter(PARAM_STORE_CD, addr.getStore_cd());
		query3.setParameter(PARAM_VERSION, addr.getVersion());
		
		List list1 = query1.getResultList();
		List list2 = query2.getResultList();
		List list3 = query3.getResultList();
		*/
	}
	
	private TestResult saveResult(Result1 res, long start, Map<String, Object> teleMap, String telegram, String err_msg) {
		LOGGER.debug("saving result.");
		
		
		TestResult result = new TestResult();
		
		if (teleMap != null) {
			FBApplAddr addr = (FBApplAddr)teleMap.get(Utils.KEY_FBAPPLADDR);
			result.setAppl_no(addr.getAppl_no());
			result.setVersion(addr.getVersion());
			result.setStore_cd(addr.getStore_cd());
		}
		
		result.setTelegram(telegram);
		result.setElapsed_time(System.currentTimeMillis() - start);
		
		if (res != null) {
			result.setResp_cd(res.getResp_cd());
			result.setRule_result1(res.getResult1());
		}
		
		if (err_msg != null) {
			result.setErr_msg(err_msg);
		}
		
		if (res != null) {
			saveResult(result, res.getDetails());
		} else {
			saveResult(result, null);
		}
		
		return result;
	}
}
//end of RuleConsumer.java