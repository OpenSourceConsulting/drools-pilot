/* 
 *
 * Revision History
 * Author             Date              Description
 * ---------------	----------------	------------
 * Jerry Jeong	       2015. 12. 2.		    First Draft.
 */

package com.osc.entity;

import java.io.Serializable;

public class FBAPPLPHONE implements Serializable {
	public String appl_no;
	public double version;
	public String store_cd;
	public String org_id;
	public String wire_mobile_gb;
	public String start_dtim;
	public String full_phone_no;
	public String nice_fraud_stat_cd;
	public double lst_work_date;
	
	@Override
	public String toString() {
		return "FBAPPLPHONE [appl_no=" + appl_no + ", version=" + version + ", store_cd="
				+ store_cd + ", org_id=" + org_id + ", wire_mobile_gb=" + wire_mobile_gb
				+ ", start_dtim=" + start_dtim + ", full_phone_no=" + full_phone_no
				+ ", nice_fraud_stat_cd=" + nice_fraud_stat_cd + ", lst_work_date=" + lst_work_date
				+ "]";
	}
}
//end of FBAPPLADDR.java