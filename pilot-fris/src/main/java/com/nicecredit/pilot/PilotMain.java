package com.nicecredit.pilot;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nicecredit.pilot.consumer.CEPDataConsumer;
import com.nicecredit.pilot.consumer.RuleDataConsumer;
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
	
	//private static Connection conn = null;
	//private static Channel channel = null;
	
	public PilotMain() {
	}

	public void startConsume() {
		String exchangeName = "pilot_ex";
		String queueName1 = "rule_queue";
		String queueName2 = "cep_queue";
		String routingKey = "routingKey1";
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("user1");
		factory.setPassword("user1");
		factory.setVirtualHost("/");
		factory.setHost("207.46.141.43");
		factory.setPort(5672);
		
		Connection conn = null;
		Channel channel = null;
		try {
			ExecutorService es = Executors.newFixedThreadPool(10);
			conn = factory.newConnection(es);
			channel = conn.createChannel();
			
			System.out.println("created channel.");
			
			channel.exchangeDeclare(exchangeName, "direct", true);
			channel.queueDeclare(queueName1, true, false, false, null);
			channel.queueDeclare(queueName2, true, false, false, null);
			channel.queueBind(queueName1, exchangeName, routingKey);
			channel.queueBind(queueName2, exchangeName, routingKey);
			
			boolean autoAck = false;
			
			
		    //channel.basicQos(10);
			String consumerTag = channel.basicConsume(queueName1, autoAck, new RuleDataConsumer(channel));
			LOGGER.info(consumerTag + " wait for listening");
			consumerTag = channel.basicConsume(queueName2, autoAck, new CEPDataConsumer(channel));
			LOGGER.info(consumerTag + " wait for listening");
			
			
		} catch (IOException e) {
			e.printStackTrace();
			close();
		} 
		
	}
	
	public static void close(){
		/*
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
		*/
		LOGGER.info("closed!!");
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param args
	 */
	public static void main(String[] args) {
		
		PilotMain pilot = new PilotMain();
		pilot.startConsume();
		
		//DroolsTest3.main(args);
	}

}
//end of PilotMain.java