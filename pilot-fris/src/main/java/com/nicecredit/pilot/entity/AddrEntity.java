package com.nicecredit.pilot.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author BongJin Kwon
 */
@Entity
@Cacheable
@IdClass(AddrEntityPK.class)
@Table(name = "fbappladdr")
public class AddrEntity implements Serializable{

	private static final long serialVersionUID = -3144454647194312817L;

	@javax.persistence.Transient
	private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

	@org.kie.api.definition.type.Label("신청서 번호")
	@javax.persistence.Id
	private java.lang.String appl_no;
	@org.kie.api.definition.type.Label("버전")
	@javax.persistence.Id
	private java.lang.Integer version;
	@org.kie.api.definition.type.Label("점포코드")
	@javax.persistence.Id
	private java.lang.String store_cd;
	
	private java.lang.String org_id;
	
	private java.lang.String start_dtim;
	@org.kie.api.definition.type.Label("도로명하위")
	private java.lang.String strt_addr_2;
	@org.kie.api.definition.type.Label("주소PNU코드")
	private java.lang.String addr_pnu_cd;
	@org.kie.api.definition.type.Label("NICE사기코드")
	private java.lang.String nice_fraud_stat_cd;
	@org.kie.api.definition.type.Label("lst_work_date")
	private java.lang.Long lst_work_date;
	
	

	public AddrEntity() {
		// TODO Auto-generated constructor stub
	}

	public java.text.SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(java.text.SimpleDateFormat sdf) {
		this.sdf = sdf;
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

	public java.lang.String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(java.lang.String org_id) {
		this.org_id = org_id;
	}

	public java.lang.String getStart_dtim() {
		return start_dtim;
	}

	public void setStart_dtim(java.lang.String start_dtim) {
		this.start_dtim = start_dtim;
	}

	public java.lang.String getStrt_addr_2() {
		return strt_addr_2;
	}

	public void setStrt_addr_2(java.lang.String strt_addr_2) {
		this.strt_addr_2 = strt_addr_2;
	}

	public java.lang.String getAddr_pnu_cd() {
		return addr_pnu_cd;
	}

	public void setAddr_pnu_cd(java.lang.String addr_pnu_cd) {
		this.addr_pnu_cd = addr_pnu_cd;
	}

	public java.lang.String getNice_fraud_stat_cd() {
		return nice_fraud_stat_cd;
	}

	public void setNice_fraud_stat_cd(java.lang.String nice_fraud_stat_cd) {
		this.nice_fraud_stat_cd = nice_fraud_stat_cd;
	}

	public java.lang.Long getLst_work_date() {
		return lst_work_date;
	}

	public void setLst_work_date(java.lang.Long lst_work_date) {
		this.lst_work_date = lst_work_date;
	}

	@javax.persistence.PrePersist
	void prePersist() {
		this.start_dtim = sdf.format(new java.util.Date());
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return new HashCodeBuilder()
		.append(appl_no)
		.append(store_cd)
		.append(version)
		.append(start_dtim)
		.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
        if (!(obj instanceof AddrEntity)) {
            return false;
        }
        AddrEntity that = (AddrEntity) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(appl_no, that.appl_no);
        eb.append(store_cd, that.store_cd);
        eb.append(version, that.version);
        eb.append(start_dtim, that.start_dtim);
        
        return eb.isEquals();
	}
	
	

}
// end of AddrEntity.java