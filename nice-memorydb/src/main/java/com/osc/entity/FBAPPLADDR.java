/* 
 *
 * Revision History
 * Author             Date              Description
 * ---------------	----------------	------------
 * Jerry Jeong	       2015. 12. 2.		    First Draft.
 */

package com.osc.entity;

import java.io.Serializable;

public class FBAPPLADDR implements Serializable {
	public String appl_no;
	public Double version;
	public String store_cd;
	public String org_id;
	public String start_dtim;
	public String strt_addr_2;
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