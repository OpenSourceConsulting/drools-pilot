package com.nicecredit.pilot.consumer;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.FBApplAddr;
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
		
		try {
			/*
			 * 전문 파싱.
			 */
			Map<String, Object> teleMap = Utils.parseTelegram(telegram);
			
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
			Result1 result = (Result1)ruleExecutor.execute(teleMap);
	        
	        /*
	         * 결과저장
	         */
	        saveResult(result, start, teleMap, telegram);
	   	 
	        sendAck(envelope);
	        
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
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
		SqlSession sqlSession = DBRepository.getInstance().openSession();
		
		try {
			sqlSession.insert("PilotMapper.insertFBApplMst", teleMap.get(Utils.KEY_FBAPPLMST));
			sqlSession.insert("PilotMapper.insertFBApplAddr", teleMap.get(Utils.KEY_FBAPPLADDR));
			sqlSession.insert("PilotMapper.insertFBApplPhone", teleMap.get(Utils.KEY_FBAPPL_WPHONE));
			sqlSession.insert("PilotMapper.insertFBApplPhone", teleMap.get(Utils.KEY_FBAPPL_MPHONE));
			
			sqlSession.commit();
			LOGGER.debug("saved telegram.");
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}
	
	private void profiling(Map<String, Object> teleMap) {
		FBApplAddr addr = (FBApplAddr)teleMap.get(Utils.KEY_FBAPPLADDR);
		
		teleMap.put(Utils.KEY_INMEM, InfinispanHandler.getInstance().get(addr.getOrg_id()));
	}
	
	private void saveResult(Result1 res, long start, Map<String, Object> teleMap, String telegram) {
		LOGGER.debug("saving result.");
		SqlSession sqlSession = DBRepository.getInstance().openSession();
		
		FBApplAddr addr = (FBApplAddr)teleMap.get(Utils.KEY_FBAPPLADDR);
		TestResult result = new TestResult();
		try {
			BeanUtils.copyProperties(result, addr);
			
			result.setTelegram(telegram);
			result.setElapsed_time(System.currentTimeMillis() - start);
			result.setResp_cd(res.getResp_cd());
			result.setRule_result1(res.getResult1());
			
			sqlSession.insert("PilotMapper.insertTestResult", result);
			
			sqlSession.commit();
			LOGGER.debug("saved result.");
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}
}
//end of RuleConsumer.java