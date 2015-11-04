package com.nicecredit.pilot.util;

import com.nice.pilot.pilot_rule.InMemData;

public abstract class Utils {
	
	private static final String KEY_DELIMETER = "_";

	public static String getCacheKey(InMemData inMemData) {
		return inMemData.getAppl_no() + KEY_DELIMETER + inMemData.getVersion() + KEY_DELIMETER + inMemData.getStore_cd();
	}

}
//end of Utils.java