//	전문 발생기

package com.nicecredit.pilot.tester;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.commons.io.IOUtils;

/**
 * <pre>
 * MATCH 전문을 우선으로 REGIT 전문과 번갈아 보낸다.
 * </pre>
 * @author BongJin Kwon
 */
public class FileTestMain {

	public static void main(String[] args) {
		
		RabbitmqClient rabbitClient = null;
		
		BufferedReader br = null;
		BufferedReader br2 = null;
		try {
			
			rabbitClient = new RabbitmqClient();
			rabbitClient.basicConsume();
			
			//boolean isMatchTest = false;
			br = new BufferedReader(new FileReader("./src/main/resources/match.txt"));
			br2 = new BufferedReader(new FileReader("./src/main/resources/regit.txt"));
			
			String line = null;
			int i = 0;
			while (true){
				
				
				if (i % 2 == 0) {
					line = br.readLine();
					
					if (line == null) {
						line = br2.readLine();
					}
				} else {
					line = br2.readLine();
					
					if (line == null) {
						line = br.readLine();
					}
				}
				
				if (line == null) {
					break;
				}
				
				rabbitClient.sendMessage(line);
				
				//System.out.println("send count : "+ i);
				i++;
				Thread.sleep(200);
				
				/*
				if(i == 6) {
					break;// for test.
				}*/
			}
			
			System.out.println("all data sended!! " + i);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			IOUtils.closeQuietly(br);
			IOUtils.closeQuietly(br2);
			/*
			if (rabbitClient != null) {
				rabbitClient.close();
			}*/
		}
		
		
	}

}
//end of TestMain.java