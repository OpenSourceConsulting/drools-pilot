/* 
 *
 * Revision History
 * Author             Date              Description
 * ---------------	----------------	------------
 * Jerry Jeong	       2015. 12. 5.		    First Draft.
 */

package com.osc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import com.osc.entity.FBAPPLADDR;
import com.osc.entity.FBAPPLPHONE;


public class TestMemoryDB {
	static ArrayList<FBAPPLADDR> dataAddr = new ArrayList<FBAPPLADDR>();
	static ArrayList<FBAPPLPHONE> dataPhone = new ArrayList<FBAPPLPHONE>();

	static String drvName = null;
	static String url = null;
	static String user = null;
	static String password = null;
	static String tblAddr = null;
	static String tblPhone = null;

	static boolean MIG_ADDR = false;
	static boolean MIG_PHONE = false;

	static boolean QUERY_MYSQL = false;
	static boolean QUERY_MYSQL_MEM = false;
	static boolean QUERY_HSQLDB = false;
	static boolean QUERY_HSQLDB_EMBEDDED = false;
	static boolean QUERY_H2 = false;
	static boolean QUERY_H2_EMBEDDED = false;

	public static void main(String[] args) {
		mysqlExport();
		dbImport();
//		queryTest1();
		queryTest2();
//		queryTest3();
	}
	
	static void configJdbc() {
		if(QUERY_HSQLDB) {
			drvName = "org.hsqldb.jdbcDriver";
			url = "jdbc:hsqldb:hsql://localhost:9001/mydb";
			user = "sa";
			password = "";
			tblAddr = "fbappladdr";
			tblPhone = "fbapplphone";
		}
		else if(QUERY_HSQLDB_EMBEDDED) {
			drvName = "org.hsqldb.jdbcDriver";
			url = "jdbc:hsqldb:mem:memtest";
			user = "sa";
			password = "";
			tblAddr = "fbappladdr";
			tblPhone = "fbapplphone";
		}
		else if(QUERY_MYSQL || QUERY_MYSQL_MEM) {
			drvName = "com.mysql.jdbc.Driver";
			url = "jdbc:mysql://centos7:3306/nicepilot";
			user = "nicedb";
			password = "nicedb00";
			if(QUERY_MYSQL_MEM) {
				tblAddr = "mfbappladdr";
				tblPhone = "mfbapplphone";
			}
			else {
				tblAddr = "fbappladdr";
				tblPhone = "fbapplphone";
			}
		}
		else if(QUERY_H2) {
			drvName = "org.h2.Driver";
			url = "jdbc:h2:tcp://localhost/mem:db1";
			user = "sa";
			password = "";
			tblAddr = "fbappladdr";
			tblPhone = "fbapplphone";
		}
		else if(QUERY_H2_EMBEDDED) {
			drvName = "org.h2.Driver";
			url = "jdbc:h2:mem:db1";
			user = "sa";
			password = "";
			tblAddr = "fbappladdr";
			tblPhone = "fbapplphone";
		}
	}
	
	public static void queryTest1() {
		configJdbc();
		
		Driver drv = null;
		try {
			Class cls = Class.forName(drvName);
			drv = (Driver)cls.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", password);
		try {
			Connection conn = drv.connect(url, props);
			long startTime = System.currentTimeMillis();
			
			{
				//	쿼리 조건 로딩
				
				PreparedStatement pstmt1 = conn.prepareStatement(
						"SELECT * FROM " + tblAddr
				); 
	
				ResultSet rs1 = pstmt1.executeQuery();
				for(int i = 0; rs1.next(); i++) {
					FBAPPLADDR addr = new FBAPPLADDR();
					addr.appl_no = rs1.getString("appl_no");
					addr.version = rs1.getDouble("version");
					addr.store_cd = rs1.getString("store_cd");
					addr.org_id = rs1.getString("org_id");
					addr.start_dtim = rs1.getString("start_dtim");
					addr.strt_addr_2 = rs1.getString("strt_addr_2");
					addr.addr_pnu_cd = rs1.getString("addr_pnu_cd");
					addr.nice_fraud_stat_cd = rs1.getString("nice_fraud_stat_cd");
					addr.lst_work_date = rs1.getDouble("lst_work_date");
					dataAddr.add(addr);
					
					if(i % 1000 == 0) {
						System.out.println(i + ", " + addr.appl_no);
					}
				}
				
				System.out.println("Elapsed Time: " + (System.currentTimeMillis() - startTime));
			}

			{
				//	조회 테스트
				
				PreparedStatement pstmt2 = conn.prepareStatement("SELECT\r\n" + 
						"DISTINCT t4.APPL_NO , t4.VERSION , t4.STORE_CD\r\n" + 
						"FROM " + tblAddr + " s0 , " + tblAddr + " t2 , " + tblPhone + " s3 , " + tblPhone + " t4\r\n" + 
						"WHERE s0.APPL_NO = ? AND s0.VERSION = ? AND s0.STORE_CD = ?\r\n" + 
						"AND t2.addr_pnu_cd = s0.addr_pnu_cd AND t2.strt_addr_2 = s0.strt_addr_2 AND t2.ORG_ID <> s0.ORG_ID\r\n" + 
						"AND s3.APPL_NO = s0.APPL_NO AND s3.VERSION = s0.VERSION AND s3.STORE_CD = s0.STORE_CD AND s3.ORG_ID = s0.ORG_ID AND s3.wire_mobile_gb = '1'\r\n" + 
						"AND t4.APPL_NO = t2.APPL_NO AND t4.VERSION = t2.VERSION AND t4.STORE_CD = t2.STORE_CD AND t4.full_phone_no = s3.full_phone_no AND t4.ORG_ID <> s3.ORG_ID;\r\n" 
				);
				
				for(int i = 0; i < dataAddr.size(); i++) {
					FBAPPLADDR id = dataAddr.get(i);
					if(i % 10 == 0) {
						long elapseTime = System.currentTimeMillis();
						System.out.println(i + ": " + id.appl_no + ", " + id.version + ", " + id.store_cd + ", " + (elapseTime - startTime));
					}
					
					pstmt2.setString(1, id.appl_no);
					pstmt2.setDouble(2, id.version);
					pstmt2.setString(3, id.store_cd);
					
					ResultSet rs2 = pstmt2.executeQuery();
					while(rs2.next()) {
						String s1 = rs2.getString(1);
						System.out.println(s1);
					}
					
					pstmt2.clearParameters();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void queryTest2() {
		configJdbc();
		
		Driver drv = null;
		try {
			Class cls = Class.forName(drvName);
			drv = (Driver)cls.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", password);
		try {
			Connection conn = drv.connect(url, props);
			long startTime = System.currentTimeMillis();
			
			{
				//	쿼리 조건 로딩
				
				PreparedStatement pstmt1 = conn.prepareStatement(
						"SELECT * FROM " + tblPhone
				); 
	
				ResultSet rs1 = pstmt1.executeQuery();
				for(int i = 0; rs1.next(); i++) {
					FBAPPLPHONE addr = new FBAPPLPHONE();
					addr.appl_no = rs1.getString("appl_no");
					addr.version = rs1.getDouble("version");
					addr.store_cd = rs1.getString("store_cd");
					dataPhone.add(addr);
					
					if(i % 1000 == 0) {
						System.out.println(i + ", " + addr.appl_no);
					}
				}
				
				System.out.println("Elapsed Time: " + (System.currentTimeMillis() - startTime));
			}

			{
				//	조회 테스트
				
				PreparedStatement pstmt2 = conn.prepareStatement("SELECT DISTINCT t6.APPL_NO ,\r\n" + 
						"       t6.VERSION ,\r\n" + 
						"       t6.STORE_CD\r\n" + 
						"FROM " + tblPhone + " s0 ,\r\n" + 
						"     " + tblPhone + " t2 ,\r\n" + 
						"     " + tblPhone + " s3 ,\r\n" + 
						"     " + tblPhone + " t6\r\n" + 
						"WHERE s0.APPL_NO = ?\r\n" + 
						"  AND s0.VERSION = ?\r\n" + 
						"  AND s0.STORE_CD = ?\r\n" + 
						"  AND s0.wire_mobile_gb = '3'\r\n" + 
						"  AND t2.full_phone_no = s0.full_phone_no\r\n" + 
						"  AND t2.ORG_ID <> s0.ORG_ID\r\n" + 
						"  AND s3.APPL_NO = s0.APPL_NO\r\n" + 
						"  AND s3.VERSION = s0.VERSION\r\n" + 
						"  AND s3.STORE_CD = s0.STORE_CD\r\n" + 
						"  AND s3.ORG_ID = s0.ORG_ID\r\n" + 
						"  AND s3.wire_mobile_gb = '1'\r\n" + 
						"  AND t6.APPL_NO = t2.APPL_NO\r\n" + 
						"  AND t6.VERSION = t2.VERSION\r\n" + 
						"  AND t6.STORE_CD = t2.STORE_CD\r\n" + 
						"  AND t6.ORG_ID = t2.ORG_ID\r\n" + 
						"  AND t6.full_phone_no = s3.full_phone_no\r\n" + 
						"  AND t6.ORG_ID <> s3.ORG_ID" 
				);
				
				for(int i = 0; i < dataPhone.size(); i++) {
					FBAPPLPHONE id = dataPhone.get(i);
					if(i % 10 == 0) {
						long elapseTime = System.currentTimeMillis();
						System.out.println(i + ": " + id.appl_no + ", " + id.version + ", " + id.store_cd + ", " + (elapseTime - startTime));
					}
					
					pstmt2.setString(1, id.appl_no);
					pstmt2.setDouble(2, id.version);
					pstmt2.setString(3, id.store_cd);
					
					ResultSet rs2 = pstmt2.executeQuery();
					while(rs2.next()) {
						String s1 = rs2.getString(1);
						System.out.println("***** " + s1);
					}
					
					pstmt2.clearParameters();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void queryTest3() {
		configJdbc();
		
		Driver drv = null;
		try {
			Class cls = Class.forName(drvName);
			drv = (Driver)cls.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", password);
		try {
			Connection conn = drv.connect(url, props);
			long startTime = System.currentTimeMillis();
			
			{
				//	쿼리 조건 로딩
				
				PreparedStatement pstmt1 = conn.prepareStatement(
						"SELECT * FROM " + tblAddr
				); 
	
				ResultSet rs1 = pstmt1.executeQuery();
				for(int i = 0; rs1.next(); i++) {
					FBAPPLADDR addr = new FBAPPLADDR();
					addr.appl_no = rs1.getString("appl_no");
					addr.version = rs1.getDouble("version");
					addr.store_cd = rs1.getString("store_cd");
					addr.org_id = rs1.getString("org_id");
					addr.start_dtim = rs1.getString("start_dtim");
					addr.strt_addr_2 = rs1.getString("strt_addr_2");
					addr.addr_pnu_cd = rs1.getString("addr_pnu_cd");
					addr.nice_fraud_stat_cd = rs1.getString("nice_fraud_stat_cd");
					addr.lst_work_date = rs1.getDouble("lst_work_date");
					dataAddr.add(addr);
					
					if(i % 1000 == 0) {
						System.out.println(i + ", " + addr.appl_no);
					}
				}
				
				System.out.println("Elapsed Time: " + (System.currentTimeMillis() - startTime));
			}

			{
				//	조회 테스트
				
				PreparedStatement pstmt2 = conn.prepareStatement("SELECT DISTINCT t3.APPL_NO ,\r\n" + 
						"     t3.VERSION ,\r\n" + 
						"     t3.STORE_CD\r\n" + 
						"FROM " + tblPhone + " s0 ,\r\n" + 
						"     " + tblPhone + " t2 ,\r\n" + 
						"     " + tblPhone + " t3\r\n" + 
						"WHERE s0.APPL_NO = ?\r\n" + 
						"  AND s0.VERSION = ?\r\n" + 
						"  AND s0.STORE_CD = ?\r\n" + 
						"  AND s0.wire_mobile_gb = '1'\r\n" + 
						"  AND t2.PHONE_FRAUD_RULE_CD__X1 = '7'\r\n" + 
						"  AND t2.full_phone_no = s0.full_phone_no\r\n" + 
						"  AND t3.APPL_NO = t2.APPL_NO\r\n" + 
						"  AND t3.VERSION = t2.VERSION\r\n" + 
						"  AND t3.STORE_CD = t2.STORE_CD\r\n" + 
						"  AND t3.FRAUD_RSN_CD1 not in ( '10',\r\n" + 
						"      '11',\r\n" + 
						"      '12',\r\n" + 
						"      '13' )\r\n" + 
						""
				);
				
				for(int i = 0; i < dataAddr.size(); i++) {
					FBAPPLADDR id = dataAddr.get(i);
					if(i % 10 == 0) {
						long elapseTime = System.currentTimeMillis();
						System.out.println(i + ": " + id.appl_no + ", " + id.version + ", " + id.store_cd + ", " + (elapseTime - startTime));
					}
					
					pstmt2.setString(1, id.appl_no);
					pstmt2.setDouble(2, id.version);
					pstmt2.setString(3, id.store_cd);
					
					ResultSet rs2 = pstmt2.executeQuery();
					while(rs2.next()) {
						String s1 = rs2.getString(1);
						System.out.println("***** " + s1);
					}
					
					pstmt2.clearParameters();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void mysqlExport() {
		Driver drv = null;
		try {
			Class cls = Class.forName("com.mysql.jdbc.Driver");
			drv = (Driver)cls.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Properties props = new Properties();
		props.setProperty("user", "nicedb");
		props.setProperty("password", "nicedb00");
		try {
			Connection conn = drv.connect("jdbc:mysql://centos7:3306/nicepilot", props);
			long startTime = System.currentTimeMillis();

			if(MIG_ADDR) {
				PreparedStatement pstmt1 = conn.prepareStatement(
						"SELECT * FROM fbappladdr"
				); 
	
				ResultSet rs1 = pstmt1.executeQuery();
				for(int i = 0; rs1.next(); i++) {
					FBAPPLADDR addr = new FBAPPLADDR();
					addr.appl_no = rs1.getString("appl_no");
					addr.version = rs1.getDouble("version");
					addr.store_cd = rs1.getString("store_cd");
					addr.org_id = rs1.getString("org_id");
					addr.start_dtim = rs1.getString("start_dtim");
					addr.strt_addr_2 = rs1.getString("strt_addr_2");
					addr.addr_pnu_cd = rs1.getString("addr_pnu_cd");
					addr.nice_fraud_stat_cd = rs1.getString("nice_fraud_stat_cd");
					addr.lst_work_date = rs1.getDouble("lst_work_date");
					dataAddr.add(addr);
					
					if(i % 1000 == 0) {
						System.out.println(i + ", " + addr.appl_no);
					}
				}
			}

			if(MIG_PHONE) {
				PreparedStatement pstmt2 = conn.prepareStatement(
						"SELECT * FROM fbapplphone"
				); 
	
				ResultSet rs2 = pstmt2.executeQuery();
				for(int i = 0; rs2.next(); i++) {
					FBAPPLPHONE phone = new FBAPPLPHONE();
					phone.appl_no = rs2.getString("appl_no");
					phone.version = rs2.getDouble("version");
					phone.store_cd = rs2.getString("store_cd");
					phone.org_id = rs2.getString("org_id");
					phone.wire_mobile_gb = rs2.getString("wire_mobile_gb");
					phone.start_dtim = rs2.getString("start_dtim");
					phone.full_phone_no = rs2.getString("full_phone_no");
					phone.nice_fraud_stat_cd = rs2.getString("nice_fraud_stat_cd");
					phone.lst_work_date = rs2.getDouble("lst_work_date");
					dataPhone.add(phone);
					
					if(i % 1000 == 0) {
						System.out.println(i + ", " + phone.appl_no);
					}
				}
			}

			System.out.println("***** Export done: size=" + dataAddr.size() + ", " + dataPhone.size());
			System.out.println("Elapsed Time: " + (System.currentTimeMillis() - startTime));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void dbImport() {
		configJdbc();
		
		Driver drv = null;
		try {
			Class cls = Class.forName(drvName);
			drv = (Driver)cls.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Properties props = new Properties();
		props.setProperty("user", user);
		props.setProperty("password", password);
		try {
			Connection conn = drv.connect(url, props);
			conn.setAutoCommit(false);

			long startTime = System.currentTimeMillis();

			if(MIG_ADDR) {
				try {
					conn.prepareStatement("drop table fbappladdr").executeUpdate();
				}
				catch(SQLException e) {}
				conn.prepareStatement("CREATE TABLE\r\n" + 
						"    fbappladdr\r\n" + 
						"    (\r\n" + 
						"        appl_no VARCHAR(20) NOT NULL,\r\n" + 
						"        version DECIMAL(3,0) NOT NULL,\r\n" + 
						"        store_cd VARCHAR(7) NOT NULL,\r\n" + 
						"        org_id VARCHAR(8) NOT NULL,\r\n" + 
						"        start_dtim VARCHAR(14) NOT NULL,\r\n" + 
						"        strt_addr_2 VARCHAR(150),\r\n" + 
						"        addr_pnu_cd VARCHAR(19),\r\n" + 
						"        nice_fraud_stat_cd VARCHAR(1) ,\r\n" + 
						"        lst_work_date DECIMAL(9,0),\r\n" + 
						"        PRIMARY KEY (appl_no, version, store_cd, org_id, start_dtim)\r\n" + 
						"    )\r\n" 
					).executeUpdate();
				conn.commit();
				
				PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO fbappladdr values (?, ?, ?, ?, ?, ?, ?, ?, ?)"); 
				
				for(int i = 0; i < dataAddr.size(); i++) {
					FBAPPLADDR addr = dataAddr.get(i);
					
					pstmt1.setString(1, addr.appl_no);
					pstmt1.setDouble(2, addr.version);
					pstmt1.setString(3, addr.store_cd);
					pstmt1.setString(4, addr.org_id);
					pstmt1.setString(5, addr.start_dtim);
					pstmt1.setString(6, addr.strt_addr_2);
					pstmt1.setString(7, addr.addr_pnu_cd);
					pstmt1.setString(8, addr.nice_fraud_stat_cd);
					pstmt1.setDouble(9, addr.lst_work_date);
					
					pstmt1.executeUpdate();
					pstmt1.clearParameters();
					
					if(i % 100 == 0) {
						long elapseTime = System.currentTimeMillis();
						System.out.println(i + ", Elapsed Time: " + (elapseTime - startTime));
					}
				}
				
				conn.commit();

				dataAddr.clear();
			}
			
			if(MIG_PHONE) {
				try {
					conn.prepareStatement("drop table fbapplphone").executeUpdate();
				}
				catch(SQLException e) {}
				conn.prepareStatement("CREATE TABLE\r\n" + 
						"    fbapplphone\r\n" + 
						"    (\r\n" + 
						"        appl_no VARCHAR(20) NOT NULL,\r\n" + 
						"        version DECIMAL(3,0) NOT NULL,\r\n" + 
						"        store_cd VARCHAR(7) NOT NULL,\r\n" + 
						"        org_id VARCHAR(8) NOT NULL,\r\n" + 
						"        wire_mobile_gb VARCHAR(1) NOT NULL,\r\n" + 
						"        start_dtim VARCHAR(14) NOT NULL,\r\n" + 
						"        full_phone_no VARCHAR(12),\r\n" + 
						"        nice_fraud_stat_cd VARCHAR(1),\r\n" + 
						"        lst_work_date DECIMAL(9,0),\r\n" + 
						"        PRIMARY KEY (appl_no, version, store_cd, org_id, wire_mobile_gb, start_dtim)\r\n" + 
						"    )" + 
						"").executeUpdate();
				conn.commit();
	
				PreparedStatement pstmt2 = conn.prepareStatement("INSERT INTO fbapplphone values (?, ?, ?, ?, ?, ?, ?, ?, ?)"); 
				
				for(int i = 0; i < dataPhone.size(); i++) {
					FBAPPLPHONE phone = dataPhone.get(i);
					
					pstmt2.setString(1, phone.appl_no);
					pstmt2.setDouble(2, phone.version);
					pstmt2.setString(3, phone.store_cd);
					pstmt2.setString(4, phone.org_id);
					pstmt2.setString(5, phone.wire_mobile_gb);
					pstmt2.setString(6, phone.start_dtim);
					pstmt2.setString(7, phone.full_phone_no);
					pstmt2.setString(8, phone.nice_fraud_stat_cd);
					pstmt2.setDouble(9, phone.lst_work_date);
					
					pstmt2.executeUpdate();
					pstmt2.clearParameters();
					
					if(i % 100 == 0) {
						long elapseTime = System.currentTimeMillis();
						System.out.println(i + ", Elapsed Time: " + (elapseTime - startTime));
					}
				}
				
				conn.commit();
				
				dataPhone.clear();
			}
			
			System.out.println("***** Import done: size=" + dataAddr.size() + ", " + dataPhone.size());
			System.out.println("Elapsed Time: " + (System.currentTimeMillis() - startTime));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
//end of MySQLMemory.java