package com.nicecredit.pilot.db;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.InMemData;
import com.nicecredit.pilot.cache.InfinispanHandler;

public class DBRepositoryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOpenSession() {
		
		/*
		 * DB 연동 테스트
		 */
		try {
			SqlSession sqlSession = DBRepository.getInstance().openSession();
			//InfinispanHandler cacheHandler = InfinispanHandler.getInstance();
			
			List<InMemData> list = sqlSession.selectList("PilotMapper.testINMEM_DATAList");
			
			/*for (InMemData inMemData : list) {
				cacheHandler.put(inMemData.getAppl_no() + "_" + inMemData.getVersion() + "_" + inMemData.getStore_cd(), inMemData);
			}*/
			
			int listSize = list.size();
			//int cacheSize = cacheHandler.keys().size();
			
			System.out.println(listSize);
			//System.out.println(cacheSize);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}
	
	@Test
	public void testSelect() {
		
		/*
		 * SELECT 테스트
		
		try {
			SqlSession sqlSession = DBRepository.getInstance().openSession();
			
			List<FBApplAddr> list = sqlSession.selectList("PilotMapper.selectFBApplAddrList");
			for (FBApplAddr fbApplAddr : list) {
				System.out.println(convert(fbApplAddr.getStrt_addr_2()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		 */
	}
	
	private String convert(String orgin) throws UnsupportedEncodingException {
		//return StringUtils.toEncodedString(orgin.getBytes("UTF-8"), Charset.forName("ISO-8859-1"));
		//return StringUtils.toEncodedString(orgin.getBytes("ISO-8859-1"), Charset.forName("EUC-KR"));
		return StringUtils.toEncodedString(orgin.getBytes("EUC-KR"), Charset.forName("ISO-8859-1"));
	}

}
//end of DBRepositoryTest.java