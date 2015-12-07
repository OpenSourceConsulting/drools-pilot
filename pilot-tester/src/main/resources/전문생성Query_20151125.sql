SELECT 
      concat('MATCH1000           '
		   , org.appl_no, '          '
			, org.store_cd, '   '
			, RPAD(TRIM(CONVERT(COALESCE(mst.version,0) + 1,CHAR)), 3, ' ')
         , org.org_id
         , CONVERT(FLOOR(1+RAND()*1), CHAR),CONVERT(FLOOR(1+RAND()*4), CHAR),CONVERT(FLOOR(1+RAND()*4), CHAR),'.'
         , CONVERT(FLOOR(1+RAND()*1), CHAR),CONVERT(FLOOR(1+RAND()*4), CHAR),CONVERT(FLOOR(1+RAND()*4), CHAR),'.'
         , CONVERT(FLOOR(1+RAND()*1), CHAR),CONVERT(FLOOR(1+RAND()*4), CHAR),CONVERT(FLOOR(1+RAND()*4), CHAR)
         , '    '
         , 'cdabcdefg123456789a'
         , '                                                                                                                                                      '
         , RPAD(concat('02', CONVERT(FLOOR(RAND()*10), CHAR)
			                  , CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)),12,' ')
         , RPAD(concat('010', CONVERT(FLOOR(RAND()*10), CHAR)
			                  , CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)
									, CONVERT(FLOOR(RAND()*10), CHAR)),12,' ')
         , '                     '
			) AS 전문
FROM (SELECT appl.appl_no, store.store_cd, org.org_id
        FROM  (SELECT * FROM make_sample_appl_no ORDER BY Rand() Limit 10) appl
		       ,(SELECT * FROM make_sample_store_cd ORDER BY Rand() Limit 10) store
		       ,(SELECT * FROM make_sample_org_id ORDER BY Rand() Limit 10) org
		ORDER BY Rand() 
       Limit 100
      ) org
LEFT OUTER JOIN fbapplmst mst
  ON org.appl_no = mst.appl_no
 AND org.store_cd = mst.store_cd
 ;
 
 
 
 ## 전문2 생성쿼리
SELECT 
      concat('REGIT1000           '
		, mst.appl_no, '          '
		, mst.store_cd, '   '
		, RPAD(TRIM(CONVERT(mst.version,CHAR)), 3, ' ')
		, addr.org_id
		, '1'
		, 'RSCD0001'
		, '20151104'
		, ROUND(RAND())
		, ROUND(RAND())
		, ROUND(RAND())
		, '                   '
			) AS 전문
FROM (SELECT appl_no, store_cd, max(version) as version
        FROM fbapplmst mst
       WHERE appl_no <> '0000000000'
       GROUP BY appl_no, store_cd
       ORDER BY Rand()
       Limit 100
      ) mst
JOIN fbappladdr addr
  ON mst.appl_no = addr.appl_no
 AND mst.store_cd = addr.store_cd
 AND mst.version  = addr.version
JOIN fbapplphone phone1
  ON addr.appl_no = phone1.appl_no
 AND addr.store_cd = phone1.store_cd
 AND addr.version  = phone1.version
 AND addr.org_id  = phone1.org_id
 AND phone1.wire_mobile_gb = '1'
JOIN fbapplphone phone2
  ON addr.appl_no = phone2.appl_no
 AND addr.store_cd = phone2.store_cd
 AND addr.org_id  = phone2.org_id
 AND phone2.wire_mobile_gb = '2' 
;