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

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;

@Entity
@Table(name = "fbapplphone")
@Indexed
public class FBAPPLPHONE implements Serializable {
	@Id
	@Field
	public String appl_no;
	@Id
	@Field
	@NumericField
	public double version;
	@Id
	@Field
	public String store_cd;
	@Id
	@Field
	public String org_id;
	@Id
	@Field
	public String wire_mobile_gb;
	@Id
	@Field
	public String start_dtim;
	
	@Field
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