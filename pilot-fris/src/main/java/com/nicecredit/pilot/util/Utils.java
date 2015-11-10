package com.nicecredit.pilot.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.FBApplMst;
import com.nice.pilot.pilot_rule.FBApplPhone;
import com.nice.pilot.pilot_rule.InMemData;

public abstract class Utils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
	
	private static final String KEY_DELIMETER = "_";
	public static final String TELE_CD_MATCH = "MATCH";
	public static final String TELE_CD_REGIT = "REGIT";
	
	public static final String KEY_FBAPPLMST = "mst";
	public static final String KEY_FBAPPLADDR = "addr";
	public static final String KEY_FBAPPL_WPHONE = "wire";
	public static final String KEY_FBAPPL_MPHONE = "mobile";

	public static String getCacheKey(InMemData inMemData) {
		//return inMemData.getAppl_no() + KEY_DELIMETER + inMemData.getVersion() + KEY_DELIMETER + inMemData.getStore_cd();
		return inMemData.getOrg_id();
	}
	
	public static Map<String, Object> parseTelegram(String telegram) {
		Map<String, Object> teleMap = new HashMap<String, Object>();
		
		String teleCode = telegram.substring(0, 5);//전문코드
		teleMap.put("teleCode", teleCode);
		
		String appl_no 	= telegram.substring(20, 40).trim();
		String store_cd = telegram.substring(40, 50).trim();
		int version 	= Integer.parseInt(telegram.substring(50, 53).trim());
		
		FBApplMst fbapplmst = new FBApplMst();
		fbapplmst.setAppl_no(appl_no);
		fbapplmst.setStore_cd(store_cd);
		fbapplmst.setVersion(version);
		
		FBApplAddr fbappladdr = new FBApplAddr();
		fbappladdr.setAppl_no(appl_no);
		fbappladdr.setStore_cd(store_cd);
		fbappladdr.setVersion(version);
		
		FBApplPhone wirephone = new FBApplPhone();//유선전화
		wirephone.setAppl_no(appl_no);
		wirephone.setStore_cd(store_cd);
		wirephone.setVersion(version);
		wirephone.setWire_mobile_gb("1");
		
		FBApplPhone mobilephone = new FBApplPhone();//무선전화
		mobilephone.setAppl_no(appl_no);
		mobilephone.setStore_cd(store_cd);
		mobilephone.setVersion(version);
		mobilephone.setWire_mobile_gb("2");
		
		if (teleCode.startsWith(TELE_CD_MATCH)) {
			
			fbapplmst.setIp_addr(telegram.substring(61, 76));
			
			String org_id = telegram.substring(53, 61);
			fbappladdr.setOrg_id(telegram.substring(53, 61));
			fbappladdr.setAddr_pnu_cd(telegram.substring(76, 95).trim());
			fbappladdr.setStrt_addr_2(telegram.substring(95, 245).trim());
			
			wirephone.setOrg_id(org_id);
			//wirephone.setFull_phone_no(telegram.substring(245, 257).trim());
			wirephone.setFull_phone_no(telegram.substring(241, 253).trim());
			
			mobilephone.setOrg_id(org_id);
			//mobilephone.setFull_phone_no(telegram.substring(257, 269).trim());
			mobilephone.setFull_phone_no(telegram.substring(253, 265).trim());
			
		} else if (teleCode.startsWith(TELE_CD_REGIT)) {
			
			fbapplmst.setNice_fraud_stat_cd(telegram.substring(53, 54));//사기신청서 여부
			fbapplmst.setFraud_rsn_cd1(telegram.substring(54, 62));		//사기신청서 사유
			fbapplmst.setFraud_confirm_date(telegram.substring(62, 70));//사기신청서 확정일자
			
			fbappladdr.setNice_fraud_stat_cd(telegram.substring(70, 71));//사기주소 여부
			wirephone.setNice_fraud_stat_cd(telegram.substring(71, 72));//사기유선전화 여부
			mobilephone.setNice_fraud_stat_cd(telegram.substring(72, 73));//사기무선전화 여부
		}
		
		teleMap.put(KEY_FBAPPLMST, fbapplmst);
		teleMap.put(KEY_FBAPPLADDR, fbappladdr);
		teleMap.put(KEY_FBAPPL_WPHONE, wirephone);
		teleMap.put(KEY_FBAPPL_MPHONE, mobilephone);
		
		//System.out.println(appl_no + "," + store_cd + "," + version + "," + fbappladdr.getOrg_id() + "," + fbapplmst.getIp_addr() + "," + fbappladdr.getStrt_addr_2() + "," + wirephone.getFull_phone_no() + "," + mobilephone.getFull_phone_no());
		
		//System.out.println(appl_no + "," + store_cd + "," + version + "," + fbapplmst.getNice_fraud_stat_cd() + "," + fbapplmst.getFraud_rsn_cd1() + "," + fbapplmst.getFraud_confirm_date() + "," + wirephone.getNice_fraud_stat_cd() + "," + mobilephone.getNice_fraud_stat_cd());
		
		LOGGER.debug("parse completed.");
		
		
		return teleMap;
	}
	
	public static String getTeleCode(Map<String, Object> teleMap) {
		return (String)teleMap.get("teleCode");
	}
	
	/*
	public static void main(String[] args) {
		String telegram = "REGIT1000           0629119384          3684720   9  1RSCD000120151104111                 ";
		//String telegram = "MATCH1000           7001683169          2834014   1  44740811124.111.131cdabcdefg123456789a                                                                                                                                                      0221938302  01090670957                      ";
		System.out.println(telegram);
		Utils.parseTelegram(telegram);
	}
	*/
}
//end of Utils.java