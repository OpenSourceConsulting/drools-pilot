package com.nicecredit.pilot.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * <pre>
 * 테스트 결과
 * </pre>
 * @author BongJin Kwon
 */
@Entity
@Table( name = "test_result" )
public class TestResult {
	
	public static final String RESP_CD_0000 = "0000";
	public static final String RESP_CD_ER01 = "ER01";
	public static final String RESP_CD_ER02 = "ER02";
	public static final String RESP_CD_ER03 = "ER03";
	public static final String RESP_CD_ER11 = "ER11";
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	
	@Column(nullable = false)
	private String appl_no;
	
	@Column(nullable = false)
	private int version;
	
	@Column(nullable = false)
	private String store_cd;
	
	@Column(nullable = false)
	private String resp_cd;
	
	@Column(nullable = false)
	private String rule_result1;
	
	@Column(nullable = false)
	private String telegram;
	
	@Column(nullable = false)
	private Date reg_dt;
	
	@Column(nullable = false)
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