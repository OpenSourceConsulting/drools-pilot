package com.nicecredit.pilot.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
//end of BaseConsumer.java