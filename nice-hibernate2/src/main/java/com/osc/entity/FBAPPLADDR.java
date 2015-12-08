/* 
 *
 * Revision History
 * Author             Date              Description
 * ---------------	----------------	------------
 * Jerry Jeong	       2015. 12. 2.		    First Draft.
 */

package com.osc.entity;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;

@Entity
@Table(name = "fbappladdr")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Indexed
public class FBAPPLADDR implements Serializable {
	@Id
	@Field
	public String appl_no;
	@Id
	@Field
	@NumericField
	public Double version;
	@Id
	@Field
	public String store_cd;
	@Id
	@Field
	public String org_id;
	@Id
	@Field
	public String start_dtim;
	
	@Field
	public String strt_addr_2;
	@Field
	public String addr_pnu_cd;
	public String nice_fraud_stat_cd;
	public Double lst_work_date;

/*
	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumns({
		@JoinColumn(name="appl_no"),
		@JoinColumn(name="version"),
		@JoinColumn(name="store_cd"),
		@JoinColumn(name="start_dtim"),
		@JoinColumn(name="org_id")
	})
	@Where(clause="wire_mobile_gb='1'")
	public List<FBAPPLPHONE> self;
*/

	@Override
	public String toString() {
		return "FBAPPLADDR [appl_no=" + appl_no + ", version=" + version + ", store_cd=" + store_cd
				+ ", org_id=" + org_id + ", start_dtim=" + start_dtim + ", strt_addr_2="
				+ strt_addr_2 + ", addr_pnu_cd=" + addr_pnu_cd + ", nice_fraud_stat_cd="
				+ nice_fraud_stat_cd + ", lst_work_date=" + lst_work_date + "]";
	}
}
//end of FBAPPLADDR.java