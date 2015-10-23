package com.nicecredit.pilot;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class PilotMain {
	
	
	public PilotMain() {
	}

	public void startConsume() {
		String exchangeName = "pilot_ex";
		String queueName = "pilot_queue1";
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
			//ExecutorService es = Executors.newFixedThreadPool(10);
			conn = factory.newConnection();
			channel = conn.createChannel();
			
			System.out.println("created channel.");
			
			channel.exchangeDeclare(exchangeName, "direct", true);
			channel.queueDeclare(queueName, true, false, false, null);
			channel.queueBind(queueName, exchangeName, routingKey);
			
			boolean autoAck = false;
			
			
			
			Consumer consumer = new DefaultConsumer(channel) {
				
				private RuleExecutor ruleExecutor = new RuleExecutor();
		         @Override
		         public void handleDelivery(String consumerTag,
		                                    Envelope envelope,
		                                    AMQP.BasicProperties properties,
		                                    byte[] body)
		             throws IOException
		         {
		        	 
		             String routingKey = envelope.getRoutingKey();
		             String contentType = properties.getContentType();
		             
		             // (process the message components here ...)
		        	 
		        	 //System.out.println(new String(body));
		             ruleExecutor.execute(new String(body));
		        	 
		        	 long deliveryTag = envelope.getDeliveryTag();
		        	 
		             getChannel().basicAck(deliveryTag, false);
		         }
		     };
		    
		    channel.basicQos(10);
			String consumerTag = channel.basicConsume(queueName, autoAck, consumer);
			System.out.println(consumerTag + " wait for listening");
			
			//Thread.sleep(50000);
			
		} catch (IOException e) {
			e.printStackTrace();
		//} catch (InterruptedException e) {
		//	e.printStackTrace();
		
		} /*finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e2) {
					// ignore.
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (IOException e2) {
					// ignore.
				}
			}
		}*/
		
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