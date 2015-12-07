package com.nicecredit.pilot.consumer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nicecredit.pilot.db.DBRepository;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class CEPDataConsumerTest {

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DBRepository.close();
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.nicecredit.pilot.consumer.CEPDataConsumer#handleDelivery(java.lang.String, com.rabbitmq.client.Envelope, com.rabbitmq.client.AMQP.BasicProperties, byte[])}.
	 */
	@Test
	public void testHandleDeliveryMATCH() {
		
		MockCEPDataConsumer consumer = new MockCEPDataConsumer(null);
		
		String[] telegrams  = new String[]{
				 "MATCH1000           NICE001OSC001       0900100   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
				,"MATCH1000           NICE001OSC001       0900100   002ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
				,"MATCH1000           NICE001OSC001       0900101   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
				,"MATCH1000           NICE001OSC001       0900102   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
				,"MATCH1000           NICE001OSC001       0900103   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
		};

		
		try {
			for (String msg : telegrams) {
				consumer.handleDelivery(null, null, null, msg.getBytes());
				
				Thread.sleep(500);
			}
			
			/*
			 * 1분동안 유지되는지
			 * - "[LatestAppl1_3] old version 주소 제거" log 찍혀야 함.
			 
			Thread.sleep(1000 * 58);
			consumer.handleDelivery(null, null, null, telegrams[telegrams.length - 1].getBytes());
			 */
			 
			/*
			 * 1분후에 사라지는지
			 * - "[LatestAppl1_3] old version 주소 제거" log 없어야 함.
			 
			Thread.sleep(1000 * 61);
			consumer.handleDelivery(null, null, null, telegrams[telegrams.length - 1].getBytes());
			*/
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
	
	@Test
	public void testHandleDeliveryREGIT() {
		
		MockCEPDataConsumer consumer = new MockCEPDataConsumer(null);
		
		String[] telegrams  = new String[]{
				// "MATCH1000           NICE001OSC001       0900100   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
				//,"MATCH1000           NICE001OSC001       0900101   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
				//,"MATCH1000           NICE001OSC001       0900102   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
				"REGIT1000           NICE001OSC001       0900100   001ABCD12341RSCD000120151030111                   "
				,"REGIT1000           NICE001OSC001       0900101   001ABCD12341RSCD000120151030111                   "
				,"REGIT1000           NICE001OSC001       0900102   001ABCD12341RSCD000120151030111                   "
		};
		
		try {
			for (String msg : telegrams) {
				consumer.handleDelivery(null, null, null, msg.getBytes());
				
				Thread.sleep(500);
			}
			
			/*
			 * 1분동안 유지되는지
			 * - "[LatestAppl1_3] old version 주소 제거" log 찍혀야 함.
			 
			Thread.sleep(1000 * 58);
			consumer.handleDelivery(null, null, null, telegrams[telegrams.length - 1].getBytes());
			 */
			 
			/*
			 * 1분후에 사라지는지
			 * - "[LatestAppl1_3] old version 주소 제거" log 없어야 함.
			 
			Thread.sleep(1000 * 61);
			consumer.handleDelivery(null, null, null, telegrams[telegrams.length - 1].getBytes());
			*/
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}

}
//end of CEPDataConsumerTest.java