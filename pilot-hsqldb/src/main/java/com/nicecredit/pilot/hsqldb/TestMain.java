package com.nicecredit.pilot.hsqldb;


import javax.persistence.EntityManager;

public class TestMain {

	public TestMain() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EntityManager emHsql = null;
		try {
			emHsql = DBRepository.getInstance().createEntityManagerHsql();
			
			System.out.println("hsql count: " + emHsql.createNativeQuery("select count(*) from fbapplmst").getSingleResult()); 
			//System.out.println("hsql count: " + emHsql.createNativeQuery("select count(*) from fbapplphone where appl_no='0000000000'").getSingleResult()); 
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (emHsql != null) {
				emHsql.close();
			}
		}
	}

}
//end of TestMain.java