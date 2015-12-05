package com.nicecredit.pilot.entity;

import java.io.Serializable;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class AddrEntityPK implements Serializable{
	
	//private static final long serialVersionUID = -3296439721127361517L;
	
	/**
	 * <pre>
	 * 
	 * </pre> 
	 */
	private static final long serialVersionUID = -3296439721127361517L;
	private java.lang.String appl_no;
	private java.lang.Integer version;
	private java.lang.String store_cd;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public AddrEntityPK() {
		// TODO Auto-generated constructor stub
	}

	public java.lang.String getAppl_no() {
		return appl_no;
	}

	public void setAppl_no(java.lang.String appl_no) {
		this.appl_no = appl_no;
	}

	public java.lang.Integer getVersion() {
		return version;
	}

	public void setVersion(java.lang.Integer version) {
		this.version = version;
	}

	public java.lang.String getStore_cd() {
		return store_cd;
	}

	public void setStore_cd(java.lang.String store_cd) {
		this.store_cd = store_cd;
	}

}
//end of AddrEntityPK.java