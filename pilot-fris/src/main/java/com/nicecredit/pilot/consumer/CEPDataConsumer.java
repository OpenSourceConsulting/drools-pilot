package com.nicecredit.pilot.consumer;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.FBApplMst;
import com.nice.pilot.pilot_rule.FBApplPhone;
import com.nicecredit.pilot.db.DBRepository;
import com.nicecredit.pilot.db.TestRegitDetail;
import com.nicecredit.pilot.db.TestResult;
import com.nicecredit.pilot.rule.CEPRuleExecutor;
import com.nicecredit.pilot.rule.RuleExecutor;
import com.nicecredit.pilot.util.Utils;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class CEPDataConsumer extends BaseConsumer {
	
	public static final String CONSUMER_TAG = "CEPDataConsumer";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CEPDataConsumer.class);
	
	private static final String SELECT_SQL1 = "SELECT addr_pnu_cd FROM fbappladdr WHERE appl_no = ? and store_cd = ? and version = ?";
	private static final String SELECT_SQL2 = "SELECT full_phone_no FROM fbapplphone WHERE appl_no = ? and store_cd = ? and version = ? and wire_mobile_gb = '1'";
	private static final String SELECT_SQL3 = "SELECT full_phone_no FROM fbapplphone WHERE appl_no = ? and store_cd = ? and version = ? and wire_mobile_gb = '2'";
	
	private RuleExecutor ruleExecutor = new CEPRuleExecutor(); 

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param channel
	 */
	public CEPDataConsumer(Channel channel) {
		super(channel);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws IOException {
		
		long start = System.currentTimeMillis();
		
		String telegram = new String(body);
		LOGGER.debug("telegram:{}", telegram);
		
		Map<String, Object> teleMap = null;
		FBApplAddr addr = null;
		boolean isRegit = true;
		String err_msg = null;
		try {
			/*
			 * parse 전문
			 */
			teleMap = Utils.parseTelegram(telegram);
			
			
			isRegit = Utils.TELE_CD_REGIT.equals(Utils.getTeleCode(teleMap));
			
			if (isRegit) {
				
				/*
				 * profiling.
				 */
				profiling(teleMap);
				
				try{
					/*
					 * REGIT 전문인경우 version 을 1 증가후 저장.
					 */
					saveTelegram(teleMap);
				} catch (Exception e) {
					// 데이터 저장 중 키 값(신청서번호, 점포코드, 버전) 중복: ER11 / "사기 조사 요청 후 신청서 추가 등록"
					addr = (FBApplAddr)teleMap.get(Utils.KEY_FBAPPLADDR);
					addr.setResp_cd(TestResult.RESP_CD_ER11);
				}
			}
				
			
			/*
			 * rule
			 * - saveTelegram(..) 에러시 skip.
			 */
			if (addr == null) {
				addr = (FBApplAddr)ruleExecutor.execute(teleMap);
			}
			
			
		} catch (Exception e) {
			
			err_msg = e.toString();
			LOGGER.error(err_msg, e);
			
		} finally {
			
			
			if (isRegit) {
				/*
				 * 결과 저장
				 */
				TestResult result = saveResult(addr, teleMap, start, telegram, err_msg);
				
				
				/*
		         * 응답 전문 보내기.
		         */
				sendResponse(result);
			}
			sendAck(envelope);
			
		}
	}
	
	/**
	 * <pre>
	 * REGIT 전문인경우 version 을 1 증가후 저장.
	 * </pre>
	 * @param teleMap
	 */
	private void saveTelegram(Map<String, Object> teleMap) {
		
		LOGGER.debug("saving telegram.");
		
		FBApplMst mst = (FBApplMst)teleMap.get(Utils.KEY_FBAPPLMST);
		FBApplAddr addr = (FBApplAddr)teleMap.get(Utils.KEY_FBAPPLADDR);
		FBApplPhone wphone = (FBApplPhone)teleMap.get(Utils.KEY_FBAPPL_WPHONE);
		FBApplPhone mphone = (FBApplPhone)teleMap.get(Utils.KEY_FBAPPL_MPHONE);
		
		int version = mst.getVersion() + 1;
		mst.setVersion(version);
		addr.setVersion(version);
		wphone.setVersion(version);
		mphone.setVersion(version);
		
		saveTelegram(mst, addr, wphone, mphone);
	}
	
	private void profiling(Map<String, Object> teleMap) {
		
		FBApplAddr addr = (FBApplAddr)teleMap.get(Utils.KEY_FBAPPLADDR);
		FBApplPhone wphone = (FBApplPhone)teleMap.get(Utils.KEY_FBAPPL_WPHONE);
		FBApplPhone mphone = (FBApplPhone)teleMap.get(Utils.KEY_FBAPPL_MPHONE);
		
		String addr_pnu_cd = null;
		String wphone_no = null;
		String mphone_no = null;
				
		if (Utils.MyBatis_Based) {
			SqlSession sqlSession = DBRepository.getInstance().openSession();
			
			try {
				addr_pnu_cd = sqlSession.selectOne("PilotMapper.selectAddrPnuCd", addr);
				wphone_no = sqlSession.selectOne("PilotMapper.selectWPhoneNo", addr);
				mphone_no = sqlSession.selectOne("PilotMapper.selectMPhoneNo", addr);
			} finally {
				sqlSession.close();
			}
			
			
		} else {
			EntityManager entityManager = DBRepository.getInstance().createEntityManager();
			
			try{
				//Query query1 = entityManager.createNativeQuery("SELECT addr_pnu_cd FROM fbappladdr WHERE appl_no = ? and store_cd = ? and version = ?");
				Query query1 = entityManager.createNativeQuery(SELECT_SQL1);
				Query query2 = entityManager.createNativeQuery(SELECT_SQL2);
				Query query3 = entityManager.createNativeQuery(SELECT_SQL3);
				
				query1.setParameter(1, addr.getAppl_no());
				query1.setParameter(2, addr.getStore_cd());
				query1.setParameter(3, addr.getVersion());
				
				query2.setParameter(1, addr.getAppl_no());
				query2.setParameter(2, addr.getStore_cd());
				query2.setParameter(3, addr.getVersion());
				
				query3.setParameter(1, addr.getAppl_no());
				query3.setParameter(2, addr.getStore_cd());
				query3.setParameter(3, addr.getVersion());
				
				try {
					addr_pnu_cd = (String)query1.getSingleResult();
				} catch (NoResultException e) {
					// ignore
				}
				
				try {
					wphone_no = (String)query2.getSingleResult();
				} catch (NoResultException e) {
					// ignore
				}
				
				try {
					mphone_no = (String)query3.getSingleResult();
				} catch (NoResultException e) {
					// ignore
				}
			} finally {
				entityManager.close();
			}
			
			
		}
		
		addr.setAddr_pnu_cd(addr_pnu_cd);
		wphone.setFull_phone_no(wphone_no);
		mphone.setFull_phone_no(mphone_no);
		
		
	}
	
	private TestResult saveResult(FBApplAddr addr, Map<String, Object> teleMap, long start, String telegram, String err_msg) {
		LOGGER.debug("saving result.");
		
		TestResult result = new TestResult();
		
		if (addr != null) {
			result.setAppl_no(addr.getAppl_no());
			result.setVersion(addr.getVersion());
			result.setStore_cd(addr.getStore_cd());
		}
		
		result.setTelegram(telegram);
		result.setElapsed_time(System.currentTimeMillis() - start);
		if (err_msg != null) {
			result.setErr_msg(err_msg);
		}
		
		int seq = 0;
		if (addr != null && addr.getAppls() != null) {
			for (FBApplAddr _addr : addr.getAppls()) {
				result.addDetail(new TestRegitDetail(_addr));
			}
		}
		
		if (teleMap != null) {
			FBApplPhone wphone = (FBApplPhone)teleMap.get(Utils.KEY_FBAPPL_WPHONE);
			FBApplPhone mphone = (FBApplPhone)teleMap.get(Utils.KEY_FBAPPL_MPHONE);
			
			if (wphone.getAppls() != null) {
				for (FBApplPhone _phone : wphone.getAppls()) {
					result.addDetail(new TestRegitDetail(_phone));
				}
			}
			
			if (mphone.getAppls() != null) {
				for (FBApplPhone _phone : mphone.getAppls()) {
					result.addDetail(new TestRegitDetail(_phone));
				}
			}
		}
		
		
		if (seq > 0) {
			result.setResp_cd(TestResult.RESP_CD_0000);
		} else {
			result.setResp_cd(TestResult.RESP_CD_ERROR);
		}
		
		saveResult(result, null);
		
		return result;
	}

}
//end of CepDataConsumer.java