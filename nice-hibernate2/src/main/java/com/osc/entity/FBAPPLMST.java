/* 
 *
 * Revision History
 * Author             Date              Description
 * ---------------	----------------	------------
 * Jerry Jeong	       2015. 12. 2.		    First Draft.
 */

package com.osc.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fbapplmst")
public class FBAPPLMST implements Serializable {
	@Id
	public String appl_no;
	@Id
	public double version;
	@Id
	public String store_cd;
	@Id
	public String start_dtim;
	
	public String ip_addr;
	public String nice_fraud_stat_cd;
	public String fraud_rsn_cd1;
	public String lst_work_date;
	
	@Override
	public String toString() {
		return "FBAPPLMST [appl_no=" + appl_no + ", version=" + version + ", store_cd=" + store_cd
				+ ", start_dtim=" + start_dtim + ", ip_addr=" + ip_addr + ", nice_fraud_stat_cd="
				+ nice_fraud_stat_cd + ", fraud_rsn_cd1=" + fraud_rsn_cd1 + ", lst_work_date="
				+ lst_work_date + "]";
	}
}
//end of FBAPPLADDR.java