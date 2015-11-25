//	전문 발생기

package com.nicecredit.pilot.tester;


public class TestMain {

	public static void main(String[] args) {
		
		
		RabbitmqClient rabbitClient = null;
		
		try {
			
			rabbitClient = new RabbitmqClient();
			
			boolean isMatchTest = false;
			String[] telegrams   = null;

			if (isMatchTest) {
				telegrams  = new String[]{
						// "MATCH1000           NICE001OSC001       0900100   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
						//,"MATCH1000           NICE001OSC001       0900100   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
						 "MATCH1000           NICE001OSC001       0900100   001ABCD1234255.255.255.255cdabcdefg123456789a                                                                                                                                                      0551234567  01012345678                      "
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
				
				rabbitClient.sendMessage(msg);
				
				System.out.println("send count : "+ i);
				i++;
				Thread.sleep(1000);
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rabbitClient != null) {
				rabbitClient.close();
			}
		}
		
		
	}

}
//end of TestMain.java