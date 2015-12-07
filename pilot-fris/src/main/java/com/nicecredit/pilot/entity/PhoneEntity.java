package com.nicecredit.pilot.entity;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.kie.api.definition.type.Label;

import com.nice.pilot.pilot_rule.FBApplPhone;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author BongJin Kwon
 */
@Entity
@Cacheable
@Table(name = "fbapplphone")
public class PhoneEntity implements Serializable{

	static final long serialVersionUID = 1L;

	@javax.persistence.Transient
	private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

	@Label("신청서 번호")
	@javax.persistence.Id
	private java.lang.String appl_no;
	@Label("버전")
	@javax.persistence.Id
	private java.lang.Integer version;
	@Label("점포코드")
	@javax.persistence.Id
	private java.lang.String store_cd;
	@Label("구성원ID")
	@javax.persistence.Id
	private java.lang.String org_id;
	@Label("유무선구분")
	@javax.persistence.Id
	private java.lang.String wire_mobile_gb;
	@Label("start_dtim")
	@javax.persistence.Id
	private java.lang.String start_dtim;
	@Label("전체전화번호")
	private java.lang.String full_phone_no;
	@Label("NICE사기코드")
	private java.lang.String nice_fraud_stat_cd;
	@Label("lst_work_date")
	private java.lang.Long lst_work_date;

	@Label("응답코드")
	@javax.persistence.Transient
	private java.lang.String resp_cd;

	@Label("신청서 반복부")
	@javax.persistence.Transient
	private java.util.List<FBApplPhone> appls;

	public PhoneEntity() {
		// TODO Auto-generated constructor stub
	}

	public java.lang.String getAppl_no() {
		return this.appl_no;
	}

	public void setAppl_no(java.lang.String appl_no) {
		this.appl_no = appl_no;
	}

	public java.lang.Integer getVersion() {
		return this.version;
	}

	public void setVersion(java.lang.Integer version) {
		this.version = version;
	}

	public java.lang.String getStore_cd() {
		return this.store_cd;
	}

	public void setStore_cd(java.lang.String store_cd) {
		this.store_cd = store_cd;
	}

	public java.lang.String getOrg_id() {
		return this.org_id;
	}

	public void setOrg_id(java.lang.String org_id) {
		this.org_id = org_id;
	}

	public java.lang.String getWire_mobile_gb() {
		return this.wire_mobile_gb;
	}

	public void setWire_mobile_gb(java.lang.String wire_mobile_gb) {
		this.wire_mobile_gb = wire_mobile_gb;
	}

	public java.lang.String getStart_dtim() {
		return this.start_dtim;
	}

	public void setStart_dtim(java.lang.String start_dtim) {
		this.start_dtim = start_dtim;
	}

	public java.lang.String getFull_phone_no() {
		return this.full_phone_no;
	}

	public void setFull_phone_no(java.lang.String full_phone_no) {
		this.full_phone_no = full_phone_no;
	}

	public java.lang.String getNice_fraud_stat_cd() {
		return this.nice_fraud_stat_cd;
	}

	public void setNice_fraud_stat_cd(java.lang.String nice_fraud_stat_cd) {
		this.nice_fraud_stat_cd = nice_fraud_stat_cd;
	}

	public java.lang.Long getLst_work_date() {
		return this.lst_work_date;
	}

	public void setLst_work_date(java.lang.Long lst_work_date) {
		this.lst_work_date = lst_work_date;
	}

	public java.lang.String getResp_cd() {
		return this.resp_cd;
	}

	public void setResp_cd(java.lang.String resp_cd) {
		this.resp_cd = resp_cd;
	}

	public java.util.List<FBApplPhone> getAppls() {
		return this.appls;
	}

	public void setAppls(java.util.List<FBApplPhone> appls) {
		this.appls = appls;
	}

	@javax.persistence.PrePersist
	void prePersist() {
		this.start_dtim = sdf.format(new java.util.Date());
	}
}
// end of PhoneEntity.java