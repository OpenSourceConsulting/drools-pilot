package com.nicecredit.pilot.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.cep_rule.Telegram;
import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.FBApplPhone;
import com.nice.pilot.pilot_rule.MatchDetail;
import com.nicecredit.pilot.db.DBRepository;
import com.nicecredit.pilot.util.Utils;

/**
 * <pre>
 * CEP Rule 처리 클래스.
 * </pre>
 * @author BongJin Kwon
 */
public class CEPRuleExecutor implements RuleExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CEPRuleExecutor.class);
	
	private KieContainer kContainer;
	private KieSession kSession;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public CEPRuleExecutor() {
		KieServices ks = KieServices.Factory.get();
		ReleaseId releaseId = ks.newReleaseId( "com.nice.pilot", "cep-rule", "1.0.0-SNAPSHOT" );
        kContainer = ks.newKieContainer( releaseId );
        //KieScanner kScanner = ks.newKieScanner( kContainer );

        //Start the KieScanner polling the Maven repository every 10 seconds
        //kScanner.start( 10000L );
        
        kSession = kContainer.newKieSession("cepSession");
	}

	/* (non-Javadoc)
	 * @see com.nicecredit.pilot.rule.RuleExecutor#execute(java.lang.String)
	 */
	@Override
	public Object execute(Map<String, Object> teleMap) {
		
		String teleCode = Utils.getTeleCode(teleMap);
		FBApplAddr addr = (FBApplAddr)teleMap.get(Utils.KEY_FBAPPLADDR);
		FBApplPhone wphone = (FBApplPhone)teleMap.get(Utils.KEY_FBAPPL_WPHONE);
		FBApplPhone mphone = (FBApplPhone)teleMap.get(Utils.KEY_FBAPPL_MPHONE);

        if (Utils.CEP_SQL) {
        	return executeSql(addr, wphone, mphone);
		} else {
			return executeRule(teleCode, addr, wphone, mphone);
		}
	}
	
	private Object executeRule(String teleCode, FBApplAddr addr, FBApplPhone wphone, FBApplPhone mphone) {

        kSession.insert(addr);
        kSession.insert(wphone);
        kSession.insert(mphone);
        kSession.insert(new Telegram(teleCode));
        
        kSession.startProcess("CepRule.CepRuleFlow");
        kSession.fireAllRules();
        
        LOGGER.debug("------- result of cep -----");
        LOGGER.debug("전문코드: {}, wphone: {}, mphone: {}, addr: {}", teleCode, wphone.getResp_cd(), mphone.getResp_cd(), addr.getResp_cd());
        
        return addr;
	}
	
	private Object executeSql(FBApplAddr addr, FBApplPhone wphone, FBApplPhone mphone) {
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("appl_no", addr.getAppl_no());
		paramMap.put("store_cd", addr.getStore_cd());
		paramMap.put("addr_pnu_cd", addr.getAddr_pnu_cd());
		paramMap.put("wphone_no", wphone.getFull_phone_no());
		paramMap.put("mphone_no", mphone.getFull_phone_no());
		
		SqlSession sqlSession = null;
		try {
			
			sqlSession = DBRepository.getInstance().openSession();
			
			List<FBApplAddr> list = sqlSession.selectList("PilotMapper.selectDeplicatedRegit", paramMap);
			
			if (list != null) {
				addr.setAppls(list);
			}
			
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		} finally {
			if (sqlSession != null) {
				sqlSession.close();
			}
		}
		
		return addr;
	}

}
//end of CEPRuleExecutor.java