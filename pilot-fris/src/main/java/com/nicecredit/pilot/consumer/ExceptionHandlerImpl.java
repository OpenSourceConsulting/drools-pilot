package com.nicecredit.pilot.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ExceptionHandler;
import com.rabbitmq.client.TopologyRecoveryException;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class ExceptionHandlerImpl implements ExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerImpl.class);

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public ExceptionHandlerImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.rabbitmq.client.ExceptionHandler#handleUnexpectedConnectionDriverException(com.rabbitmq.client.Connection, java.lang.Throwable)
	 */
	@Override
	public void handleUnexpectedConnectionDriverException(Connection conn, Throwable exception) {
		LOGGER.error(exception.toString(), exception);

	}

	/* (non-Javadoc)
	 * @see com.rabbitmq.client.ExceptionHandler#handleReturnListenerException(com.rabbitmq.client.Channel, java.lang.Throwable)
	 */
	@Override
	public void handleReturnListenerException(Channel channel, Throwable exception) {
		LOGGER.error(exception.toString(), exception);

	}

	/* (non-Javadoc)
	 * @see com.rabbitmq.client.ExceptionHandler#handleFlowListenerException(com.rabbitmq.client.Channel, java.lang.Throwable)
	 */
	@Override
	public void handleFlowListenerException(Channel channel, Throwable exception) {
		LOGGER.error(exception.toString(), exception);

	}

	/* (non-Javadoc)
	 * @see com.rabbitmq.client.ExceptionHandler#handleConfirmListenerException(com.rabbitmq.client.Channel, java.lang.Throwable)
	 */
	@Override
	public void handleConfirmListenerException(Channel channel, Throwable exception) {
		LOGGER.error(exception.toString(), exception);

	}

	/* (non-Javadoc)
	 * @see com.rabbitmq.client.ExceptionHandler#handleBlockedListenerException(com.rabbitmq.client.Connection, java.lang.Throwable)
	 */
	@Override
	public void handleBlockedListenerException(Connection connection, Throwable exception) {
		LOGGER.error(exception.toString(), exception);

	}

	/* (non-Javadoc)
	 * @see com.rabbitmq.client.ExceptionHandler#handleConsumerException(com.rabbitmq.client.Channel, java.lang.Throwable, com.rabbitmq.client.Consumer, java.lang.String, java.lang.String)
	 */
	@Override
	public void handleConsumerException(Channel channel, Throwable exception,
			Consumer consumer, String consumerTag, String methodName) {
		
		LOGGER.error("consumer exception: {}#{}", consumerTag, methodName);
		LOGGER.error(exception.toString(), exception);
		
	}

	/* (non-Javadoc)
	 * @see com.rabbitmq.client.ExceptionHandler#handleConnectionRecoveryException(com.rabbitmq.client.Connection, java.lang.Throwable)
	 */
	@Override
	public void handleConnectionRecoveryException(Connection conn,
			Throwable exception) {
		LOGGER.error(exception.toString(), exception);

	}

	/* (non-Javadoc)
	 * @see com.rabbitmq.client.ExceptionHandler#handleChannelRecoveryException(com.rabbitmq.client.Channel, java.lang.Throwable)
	 */
	@Override
	public void handleChannelRecoveryException(Channel ch, Throwable exception) {
		LOGGER.error(exception.toString(), exception);

	}

	/* (non-Javadoc)
	 * @see com.rabbitmq.client.ExceptionHandler#handleTopologyRecoveryException(com.rabbitmq.client.Connection, com.rabbitmq.client.Channel, com.rabbitmq.client.TopologyRecoveryException)
	 */
	@Override
	public void handleTopologyRecoveryException(Connection conn, Channel ch,
			TopologyRecoveryException exception) {
		LOGGER.error(exception.toString(), exception);

	}

}
//end of ExceptionHandlerImpl.java