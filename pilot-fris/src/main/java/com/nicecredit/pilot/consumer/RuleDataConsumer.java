package com.nicecredit.pilot.consumer;

import java.io.IOException;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nicecredit.pilot.db.DBRepository;
import com.nicecredit.pilot.rule.PilotRuleExecutor;
import com.nicecredit.pilot.rule.RuleExecutor;
import com.nicecredit.pilot.util.Utils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * <pre>
 * Rule 처리를 위한 data consumer
 * </pre>
 * @author BongJin Kwon
 */
public class RuleDataConsumer extends DefaultConsumer {
	
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
		
		String telegram = new String(body);
		LOGGER.debug("telegram:{}", telegram);
		
		/*
		 * 전문 파싱.
		 */
		Map<String, Object> teleMap = Utils.parseTelegram(telegram);
		
		/*
		 * 전문 저장
		 */
		saveTelegram(teleMap);
		
		/*
		 * TODO Profiling
		 */
		
		
		/*
		 * 룰 실행.
		 */
        ruleExecutor.execute(teleMap);
   	 
        sendAck(envelope);
	}
	
	/**
	 * <pre>
	 * 전문 저장.
	 * </pre>
	 * @param teleMap
	 */
	private void saveTelegram(Map<String, Object> teleMap) {
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
	
	/**
	 * <pre>
	 * rabbitmq 에 메시지 처리 완료 ack를 보낸다.
	 * - rabbitmq 는 이 ack 를 받아야 queue 에서 해당 메시지를 삭제함.
	 * </pre>
	 * @param envelope
	 * @throws IOException
	 */
	private void sendAck(Envelope envelope) throws IOException {
		long deliveryTag = envelope.getDeliveryTag();
        getChannel().basicAck(deliveryTag, false);
	}
	
}
//end of RuleConsumer.java