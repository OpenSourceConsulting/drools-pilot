package com.nicecredit.pilot;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.InMemData;
import com.nicecredit.pilot.cache.InfinispanHandler;
import com.nicecredit.pilot.consumer.CEPDataConsumer;
import com.nicecredit.pilot.consumer.ExceptionHandlerImpl;
import com.nicecredit.pilot.consumer.RuleDataConsumer;
import com.nicecredit.pilot.db.DBRepository;
import com.nicecredit.pilot.util.Utils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class PilotMain {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PilotMain.class);
	
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param args
	 */
	public static void main(String[] args) {
		
		initializing();
		startConsume();

	}

	/**
	 * <pre>
	 * 메시지 수신 시작.
	 * </pre>
	 */
	public static void startConsume() {
		String exchangeName = "pilot_ex";
		String queueName1 = "rule_queue";
		String queueName2 = "cep_queue";
		//String routingKey = "routingKey1";
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("user1");
		factory.setPassword("user1");
		factory.setVirtualHost("/");
		//factory.setHost("207.46.141.43");// 메시지 미전달 오류 (unack message queueing 발생) 로 주석처리.
		factory.setHost("nice-osc-ap.cloudapp.net");
		//factory.setHost("localhost");// for nice server
		factory.setPort(5672);
		factory.setExceptionHandler(new ExceptionHandlerImpl());
		// connection that will recover automatically
		factory.setAutomaticRecoveryEnabled(true);
		factory.setNetworkRecoveryInterval(5000);// attempt recovery every 10 seconds
		factory.setConnectionTimeout(5000);
		
		Connection conn = null;
		Channel channel = null;
		try {
			//ExecutorService es = Executors.newFixedThreadPool(10);
			conn = factory.newConnection();
			channel = conn.createChannel();
			
			System.out.println("created channel.");
			
			channel.exchangeDeclare(exchangeName, "direct", true);
			channel.queueDeclare(queueName1, true, false, false, null);
			channel.queueDeclare(queueName2, true, false, false, null);
			channel.queueBind(queueName1, exchangeName, "MATCH");
			channel.queueBind(queueName2, exchangeName, "REGIT");
			
			boolean autoAck = false;
			
			
		    //channel.basicQos(10);
			String consumerTag = channel.basicConsume(queueName1, autoAck, RuleDataConsumer.CONSUMER_TAG, new RuleDataConsumer(channel));
			LOGGER.info(consumerTag + " wait for listening");
			consumerTag = channel.basicConsume(queueName2, autoAck, CEPDataConsumer.CONSUMER_TAG, new CEPDataConsumer(channel));
			LOGGER.info(consumerTag + " wait for listening");
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e2) {
					// ignore.
				}
				channel = null;
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (IOException e2) {
					// ignore.
				}
				conn = null;
			}
			LOGGER.info("closed!!");
		} 
		
	}
	
	
	/**
	 * <pre>
	 * fris 기동전 초기화 작업
	 * - INMEM_DATA 테이블 데이타를 캐시(Infinispan)에 올린다.
	 * </pre>
	 */
	private static void initializing () {
		
		SqlSession sqlSession = DBRepository.getInstance().openSession();
		InfinispanHandler cacheHandler = InfinispanHandler.getInstance();
		
		LOGGER.debug("----------- initializing...");
		
		try {
			List<InMemData> list = sqlSession.selectList("PilotMapper.selectINMEM_DATAList");
			
			int cnt = 0;
			for (InMemData inMemData : list) {
				if(cnt % 100 == 0) {
					LOGGER.debug("--"+ cnt);
				}
				
				cacheHandler.put(Utils.getCacheKey(inMemData), inMemData);
				cnt++;
			}
			
			LOGGER.debug("----------- initialized {}.", list.size());
		} catch (Throwable e) {
			cacheHandler.stop();
			throw e;
		}
		
	}

}
//end of PilotMain.java