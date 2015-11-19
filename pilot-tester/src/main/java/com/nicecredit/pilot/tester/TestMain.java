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
			
			/*
			channel.exchangeDeclare(exchangeName, "direct", true);
			channel.queueDeclare(queueName1, true, false, false, null);
			channel.queueDeclare(queueName2, true, false, false, null);
			channel.queueBind(queueName1, exchangeName, "MATCH");
			channel.queueBind(queueName2, exchangeName, "REGIT");
			*/
			
			boolean isMatchTest = true;
			String[] telegrams   = null;

			if (isMatchTest) {
				telegrams  = new String[]{
						 "MATCH1000           NICE001OSC001       0900100   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
						,"MATCH1000           NICE001OSC001       0900100   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
						,"MATCH1000           NICE001OSC001       0900100   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
						,"MATCH1000           NICE001OSC001       0900101   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
						,"MATCH1000           NICE001OSC001       0900102   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
				};
			} else {
				telegrams  = new String[]{
						 "REGIT1000           NICE001OSC001       0900100   001ABCD12341RSCD000120151030111                   "
						,"REGIT1000           NICE001OSC001       0900100   002ABCD12341RSCD000120151030111                   "
						,"REGIT1000           NICE001OSC001       0900100   003ABCD12341RSCD000120151030111                   "
				};
			}
			
			int i = 1;
			for (String msg : telegrams) {
				
				String routingKey = null;
				byte[] messageBodyBytes = null;
				
				//	MATCH or REGIT
				routingKey = msg.substring(0, 5);
				messageBodyBytes = msg.getBytes();
				
				channel.basicPublish(exchangeName, routingKey, true,
	                    MessageProperties.PERSISTENT_TEXT_PLAIN,
	                    messageBodyBytes);
				
				System.out.println("send count : "+ i);
				i++;
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