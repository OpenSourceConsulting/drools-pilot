package com.nicecredit.pilot.consumer;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.FBApplMst;
import com.nice.pilot.pilot_rule.FBApplPhone;
import com.nicecredit.pilot.db.DBRepository;
import com.nicecredit.pilot.db.TestResult;
import com.nicecredit.pilot.rule.CEPRuleExecutor;
import com.nicecredit.pilot.rule.RuleExecutor;
import com.nicecredit.pilot.util.Utils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class CEPDataConsumer extends BaseConsumer {
	
	public static final String CONSUMER_TAG = "CEPDataConsumer";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CEPDataConsumer.class);
	
	private RuleExecutor ruleExecutor = new CEPRuleExecutor(); 

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param channel
	 */
	public CEPDataConsumer(Channel channel) {
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
			 * parse 전문
			 */
			Map<String, Object> teleMap = Utils.parseTelegram(telegram);
			
			
			boolean isRegit = Utils.TELE_CD_REGIT.equals(Utils.getTeleCode(teleMap));
			/*
			 * REGIT 전문인경우 version 을 1 증가후 저장.
			 */
			if (isRegit) {
				saveTelegram(teleMap);
			}
			
			/*
			 * rule
			 */
			FBApplAddr addr = (FBApplAddr)ruleExecutor.execute(teleMap);
			
			
			
			/*
			 * 결과 저장
			 */
			if (isRegit) {
				saveResult(addr, start, telegram);
			}
			
			sendAck(envelope);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
	}
	
	/**
	 * <pre>
	 * REGIT 전문인경우 version 을 1 증가후 저장.
	 * </pre>
	 * @param teleMap
	 */
	private void saveTelegram(Map<String, Object> teleMap) {
		
		LOGGER.debug("saving telegram.");
		
		FBApplMst mst = (FBApplMst)teleMap.get(Utils.KEY_FBAPPLMST);
		FBApplAddr addr = (FBApplAddr)teleMap.get(Utils.KEY_FBAPPLADDR);
		FBApplPhone wphone = (FBApplPhone)teleMap.get(Utils.KEY_FBAPPL_WPHONE);
		FBApplPhone mphone = (FBApplPhone)teleMap.get(Utils.KEY_FBAPPL_MPHONE);
		
		int version = mst.getVersion() + 1;
		mst.setVersion(version);
		addr.setVersion(version);
		wphone.setVersion(version);
		mphone.setVersion(version);
		
		saveTelegram(mst, addr, wphone, mphone);
	}
	
	private void saveResult(FBApplAddr addr, long start, String telegram) {
		LOGGER.debug("saving result.");
		
		TestResult result = new TestResult();
		
		result.setAppl_no(addr.getAppl_no());
		result.setVersion(addr.getVersion());
		result.setStore_cd(addr.getStore_cd());
		result.setTelegram(telegram);
		result.setElapsed_time(System.currentTimeMillis() - start);
		result.setResp_cd(addr.getResp_cd());
		
		saveResult(result);
	}

}
//end of CepDataConsumer.java