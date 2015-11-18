package com.nicecredit.pilot.consumer;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nicecredit.pilot.db.DBRepository;
import com.nicecredit.pilot.db.TestResult;
import com.nicecredit.pilot.util.Utils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class BaseConsumer extends DefaultConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseConsumer.class);

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param channel
	 */
	public BaseConsumer(Channel channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleCancelOk(String consumerTag) {
		LOGGER.info("cancel ok. {}", consumerTag);
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
		LOGGER.info("cancel. {}", consumerTag);
	}

	@Override
	public void handleShutdownSignal(String consumerTag,
			ShutdownSignalException sig) {
		LOGGER.info("shutdown signal. {}", consumerTag);
		LOGGER.error(sig.toString(), sig);
	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		LOGGER.info("recover ok. {}", consumerTag);
	}
	
	/**
	 * <pre>
	 * rabbitmq 에 메시지 처리 완료 ack를 보낸다.
	 * - rabbitmq 는 이 ack 를 받아야 queue 에서 해당 메시지를 삭제함.
	 * </pre>
	 * @param envelope
	 * @throws IOException
	 */
	protected void sendAck(Envelope envelope) throws IOException {
		long deliveryTag = envelope.getDeliveryTag();
        getChannel().basicAck(deliveryTag, false);
	}
	
	protected void saveTelegram(Object mst, Object addr, Object wphone, Object mphone) {
		
		if (Utils.MyBatis_Based) {
			SqlSession sqlSession = DBRepository.getInstance().openSession();
			
			try {			
				sqlSession.insert("PilotMapper.insertFBApplMst", mst);
				sqlSession.insert("PilotMapper.insertFBApplAddr", addr);
				sqlSession.insert("PilotMapper.insertFBApplPhone", wphone);
				sqlSession.insert("PilotMapper.insertFBApplPhone", mphone);
			
				
				sqlSession.commit();
				LOGGER.debug("saved telegram.");
			} catch (Exception e) {
				sqlSession.rollback();
				
				LOGGER.error(e.toString(), e);
			} finally {
				sqlSession.close();
			}
		} else {
			/*
			 * JPA(Hibernate) based code
			 */
			EntityManager entityManager = DBRepository.getInstance().createEntityManager();
			EntityTransaction tx = entityManager.getTransaction();
			
			try {
				tx.begin();
				
				entityManager.persist(mst);
				entityManager.persist(addr);
				entityManager.persist(wphone);
				entityManager.persist(mphone);
				
				tx.commit();
				LOGGER.debug("saved telegram.");
			} catch (Exception e) {
				tx.rollback();
				
				LOGGER.error(e.toString(), e);
			} finally {
				entityManager.close();
			}
		}
		
	}
	
	protected void saveResult(TestResult result) {
		
		if (Utils.MyBatis_Based) {
			SqlSession sqlSession = DBRepository.getInstance().openSession();
			try {
				
				sqlSession.insert("PilotMapper.insertTestResult", result);
				
				sqlSession.commit();
				LOGGER.debug("saved result.");
			} catch (Exception e) {
				LOGGER.error(e.toString(), e);
				sqlSession.rollback();
			} finally {
				sqlSession.close();
			}
		} else {
			EntityManager entityManager = DBRepository.getInstance().createEntityManager();
			EntityTransaction tx = entityManager.getTransaction();
			
			try {
				tx.begin();
				
				entityManager.persist(result);
				
				tx.commit();
				LOGGER.debug("saved result.");
			} catch (Exception e) {
				tx.rollback();
				
				LOGGER.error(e.toString(), e);
			} finally {
				entityManager.close();
			}
		}
	}

}
//end of BaseConsumer.java