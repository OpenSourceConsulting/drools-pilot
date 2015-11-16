//	전문 발생기

package com.nicecredit.pilot.tester;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class TestMain {

	public static void main(String[] args) {
		
		
		String exchangeName = "pilot_ex";
		String queueName1 = "rule_queue";
		String queueName2 = "cep_queue";
		//String routingKey = "routingKey1";
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("user1");
		factory.setPassword("user1");
		factory.setVirtualHost("/");
		factory.setHost("207.46.141.43");
		factory.setPort(5672);
		
		Connection conn = null;
		Channel channel = null;
		try {
			conn = factory.newConnection();
			channel = conn.createChannel();
			
			System.out.println("created channel.");
			
			channel.exchangeDeclare(exchangeName, "direct", true);
			channel.queueDeclare(queueName1, true, false, false, null);
			channel.queueDeclare(queueName2, true, false, false, null);
			channel.queueBind(queueName1, exchangeName, "MATCH");
			channel.queueBind(queueName2, exchangeName, "REGIT");
			
			
			int msgCount = 5;
			boolean isMatchTest = true;
			
			String telegram   = null;
			String telegram2  = null;
			String telegram3  = null;
			if (isMatchTest) {
				telegram  = "MATCH1000           7001683169          2834014   1  44740811124.111.131cdabcdefg123456789a                                                                                                                                                      0221938302  01090670957                      ";
				telegram2 = "MATCH1000           7001683169          2834015   1  44740811124.111.131cdabcdefg123456789a                                                                                                                                                      0221938302  01090670957                      ";
				telegram3 = "MATCH1000           7001683169          2834016   1  44740811124.111.131cdabcdefg123456789a                                                                                                                                                      0221938302  01090670957                      ";
			} else {
				telegram   = "REGIT1000           0629119384          3684720   9  1RSCD000120151104111                 ";
				telegram2  = "REGIT1000           0629119384          3684720   9  1RSCD000120151104111                 ";
				telegram3  = "REGIT1000           0629119384          3684720   9  1RSCD000120151104111                 ";
			}
			
			for (int i = 0; i < msgCount; i++) {
				
				String routingKey = null;
				String msg = null;
				byte[] messageBodyBytes = null;
				if (i < 3) {
					msg = telegram;
				} else if (i == 3){
					msg = telegram2;
				} else {
					msg = telegram3;
				}
				
				
				//	MATCH or REGIT
				routingKey = msg.substring(0, 5);
				messageBodyBytes = msg.getBytes();
				
				channel.basicPublish(exchangeName, routingKey, true,
	                    MessageProperties.PERSISTENT_TEXT_PLAIN,
	                    messageBodyBytes);
				
				System.out.println("send count : "+ i);
				Thread.sleep(1000);
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		}
		
		
	}

}
//end of TestMain.java