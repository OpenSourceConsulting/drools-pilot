package com.nicecredit.pilot.rule;

import java.util.Map;

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

}
//end of CEPRuleExecutor.java