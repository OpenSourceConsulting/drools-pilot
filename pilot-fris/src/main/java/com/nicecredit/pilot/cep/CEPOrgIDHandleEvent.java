package com.nicecredit.pilot.cep;

import java.util.HashMap;
import java.util.Map;

import com.nice.pilot.pilot_rule.CEPOrgIDEvent;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class CEPOrgIDHandleEvent {
	
	private String org_id;
	private int orgCount;
	private Map<String, String> handleMap = new HashMap<String, String>();

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public CEPOrgIDHandleEvent(String org_id) {
		this.org_id = org_id;
	}
	
	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	
	public int getOrgCount() {
		
		setOrgCount(handleMap.entrySet().size());
		return orgCount;
	}

	public void setOrgCount(int orgCount) {
		this.orgCount = orgCount;
	}

	public void addEvent(CEPOrgIDEvent event) {
		if (this.org_id.equals(event.getOrg_id())) {
			handleMap.put(event.getStore_cd(), "");
		}
	}
	
	public void removeEvent(CEPOrgIDEvent event) {
		if (this.org_id.equals(event.getOrg_id())) {
			handleMap.remove(event.getStore_cd());
		}
	}
	
}
//end of CEPOrgIDHandleEvent.java