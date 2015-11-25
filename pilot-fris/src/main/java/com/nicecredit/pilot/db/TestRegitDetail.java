package com.nicecredit.pilot.db;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.FBApplPhone;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
@Entity
@Table( name = "test_regit_detail" )
public class TestRegitDetail {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int seq;
	
	private int result_id;
	private String appl_no;
	private int version;
	private String store_cd;
	private String resp_cd;
	
	@ManyToOne
	@JoinColumn(name = "result_id", nullable=false, insertable=false, updatable=false)
	private TestResult result;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public TestRegitDetail() {
		// TODO Auto-generated constructor stub
	}
	
	public TestRegitDetail(FBApplAddr addr) {
		this.appl_no = addr.getAppl_no();
		this.store_cd = addr.getStore_cd();
		this.version = addr.getVersion();
		this.resp_cd = addr.getResp_cd();
	}
	
	public TestRegitDetail(FBApplPhone phone) {
		this.appl_no = phone.getAppl_no();
		this.store_cd = phone.getStore_cd();
		this.version = phone.getVersion();
		this.resp_cd = phone.getResp_cd();
	}

	public TestResult getResult() {
		return result;
	}

	public void setResult(TestResult result) {
		this.result = result;
	}

	public int getResult_id() {
		return result_id;
	}

	public void setResult_id(int result_id) {
		this.result_id = result_id;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
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
	
	@PrePersist
    void prePersist() {
		this.result_id = this.result.getId();
    }

}
//end of TestResultDetail.java