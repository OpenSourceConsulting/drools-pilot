package com.nicecredit.pilot.db;

import java.util.Date;

/**
 * <pre>
 * 테스트 결과
 * </pre>
 * @author BongJin Kwon
 */
public class TestResult {
	
	public static final String RESP_CD_0000 = "0000";
	public static final String RESP_CD_ER01 = "ER01";
	public static final String RESP_CD_ER02 = "ER02";
	public static final String RESP_CD_ER03 = "ER03";
	public static final String RESP_CD_ER11 = "ER11";
	
	private int id;
	private String appl_no;
	private int version;
	private String store_cd;
	private String resp_cd;
	private String rule_result1;
	private String telegram;
	private Date reg_dt;
	private long elapsed_time;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TestResult() {
		// TODO Auto-generated constructor stub
	}

	public String getRule_result1() {
		return rule_result1;
	}

	public void setRule_result1(String rule_result1) {
		this.rule_result1 = rule_result1;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppl_no() {
		return appl_no;
	}

	public void setAppl_no(String appl_no) {
		this.appl_no = appl_no;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getStore_cd() {
		return store_cd;
	}

	public void setStore_cd(String store_cd) {
		this.store_cd = store_cd;
	}

	public String getResp_cd() {
		return resp_cd;
	}

	public void setResp_cd(String resp_cd) {
		this.resp_cd = resp_cd;
	}

	public String getTelegram() {
		return telegram;
	}

	public void setTelegram(String telegram) {
		this.telegram = telegram;
	}

	public Date getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(Date reg_dt) {
		this.reg_dt = reg_dt;
	}

	public long getElapsed_time() {
		return elapsed_time;
	}

	public void setElapsed_time(long elapsed_time) {
		this.elapsed_time = elapsed_time;
	}
	
}
//end of TestResult.java