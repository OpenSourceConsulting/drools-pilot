package com.nicecredit.pilot.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <pre>
 * HSQLDB shutdown.
 * </pre>
 * @author BongJin Kwon
 */
public class ShutdownDB {

	public ShutdownDB() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Connection conn = null;

		try{
			String url = "jdbc:hsqldb:hsql://localhost/nicedb";
			String id = "SA";
			String pw = "";
	
			Class.forName("org.hsqldb.jdbcDriver");
			conn = DriverManager.getConnection(url,id,pw);
			System.out.println("connected.");

			conn.createStatement().execute("SHUTDOWN");
			System.out.println("HSQLDB shutdowned.");
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(conn != null) {
				try{
					conn.close();
				} catch (SQLException e) {
					//ignore.
				}
				
			}
		}

	}

}
//end of ShutdownDB.java