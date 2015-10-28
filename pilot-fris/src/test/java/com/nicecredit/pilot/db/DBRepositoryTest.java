package com.nicecredit.pilot.db;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nice.pilot.pilot_rule.InMemData;

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
			
			List<InMemData> list = sqlSession.selectList("PilotMapper.selectINMEM_DATAList");
			
			System.out.println(list.size());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}

}
//end of DBRepositoryTest.java