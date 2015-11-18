package com.nicecredit.pilot.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * <pre>
 * 동일 이동전화 and 동일 자택전화 and 다른 주민번호_30일
 * Query 결과 클래스.
 * </pre>
 * @author BongJin Kwon
 */
@Entity
public class ApplEntity2 {
	
	@Id
	private String appl_no;
	
	@Column
	private int version;
	
	@Column
	private String store_cd;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public ApplEntity2() {
		// TODO Auto-generated constructor stub
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

}
//end of ApplEntity.java