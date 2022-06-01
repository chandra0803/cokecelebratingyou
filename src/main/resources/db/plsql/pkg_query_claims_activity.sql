CREATE OR REPLACE PACKAGE pkg_query_claims_activity
IS
    PROCEDURE prc_getByOrgTabRes (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR);

    PROCEDURE prc_getByOrgStatus (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);


    PROCEDURE prc_getByOrgMonthly (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);


    PROCEDURE prc_getByOrgPartRate (                        --participation rate
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);


    PROCEDURE prc_getByOrgPartLevel (                      --participation level
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);


    PROCEDURE prc_getByOrgItemStatus (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);


    PROCEDURE prc_getByOrgTotals (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR);

    PROCEDURE prc_getByPaxTabRes (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data             OUT SYS_REFCURSOR);

    PROCEDURE prc_getByPaxClaimListTabRes (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR);

    PROCEDURE prc_getByPaxSubmittedClaims (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_average_data          OUT SYS_REFCURSOR);


    PROCEDURE prc_getByPaxClaimStatus (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);


    PROCEDURE prc_getByPaxItemStatus (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);
END pkg_query_claims_activity;

/
CREATE OR REPLACE PACKAGE BODY pkg_query_claims_activity
IS
    PROCEDURE prc_getByOrgTabRes (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)
    IS
/******************************************************************************
  NAME:       prc_getByOrgTabRes
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014      Initial..(the queries were in Java before the release 5.4. So no comments available so far)    
  Ravi Dhanekula        08/29/2014      Fixed the bug # 56110. re-formatted code to reduce the size of the query.   
  Swati					10/23/2014		Bug 57533 - Reports - Claims Activity - Participation by Organization - "Claims Open" 
										column value is not matching before and after drilldown. 		
  KrishnaDeepika        11/04/2014      Bug 57535 - Reports - Claims Activity - Participation by Organization - 
                                        Totals are displayed wrongly after done sweepstake.    
  Sherif Basha         11/07/2016       Bug 69891 - Eligible submitter count is mismatched when reports is drill down for org_unit (edit)
                                        (totals query is different from the summary query.making it sync)                                                                	
  ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getByOrgTabRes' ;
  v_stage        VARCHAR2 (500);
  v_sortCol             VARCHAR2(200);
  l_query VARCHAR2(32767);   
  
    BEGIN
    
    DELETE gtt_recog_elg_count; --08/06/2014 Start
    
    IF fnc_check_promo_aud('giver','product_claim',p_in_promotionId) = 1 THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_allaud_node pe
  WHERE  pe.giver_recvr_type = 'giver'
  AND pe.node_id IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) )
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id)
  UNION ALL
  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_allaud_team pe
  WHERE  pe.giver_recvr_type = 'giver'
  AND pe.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id);

  ELSIF NVL(INSTR(p_in_promotionId,','),0) = 0 THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_speaud_node pe
  WHERE  pe.giver_recvr_type = 'receiver'
  AND pe.promotion_id = NVL(p_in_promotionId,-1)
  AND pe.promotion_type = 'product_claim'
  AND pe.node_id IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) )
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id)
  UNION ALL
  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_speaud_team pe
  WHERE  pe.giver_recvr_type = 'receiver'
  AND pe.promotion_id = NVL(p_in_promotionId,-1)
  AND pe.promotion_type = 'product_claim'
  AND pe.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id);

ELSE 

INSERT INTO gtt_recog_elg_count
SELECT eligible_cnt,node_id,'nodesum' record_type FROM (
SELECT SUM(eligible_cnt) eligible_cnt,TO_NUMBER(TRIM(npn.path_node_id)) node_id  
                                       FROM   
                                     (                                     
                                     SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                          eligible_cnt,  
                                                       ppe.node_id  
                                                  FROM (SELECT *  
                                                          FROM rpt_pax_promo_eligibility  
                                                         WHERE giver_recvr_type = 'receiver') ppe,  
                                                       promotion P,                                                        
                                                       rpt_participant_employer rad 
                                                 WHERE ppe.promotion_id = P.promotion_id  
                                                        AND ppe.participant_id = rad.user_id(+)
                                                       AND ( ( p_in_promotionId IS NULL ) OR P.promotion_id IN (
                                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))                                                
                                                       AND P.promotion_status =  
                                                              NVL (p_in_promotionstatus,  
                                                                   P.promotion_status)  
                                               AND ((p_in_departments IS NULL) OR rad.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
                                               AND NVL (rad.position_type, 'JOB') =  
                                                      NVL (  
                                                         p_in_jobposition,  
                                                         NVL (rad.position_type, 'JOB')) 
                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                      NVL (p_in_participantStatus,  
                                                           DECODE(rad.termination_date,NULL,'active','inactive'))  
                                                       AND P.promotion_type = 'product_claim'                                          
                                              GROUP BY ppe.node_id                                     
                                     ) elg, 
                                     (SELECT child_node_id node_id,node_id path_node_id FROM rpt_hierarchy_rollup WHERE NODE_ID IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) ))npn 
                                     WHERE elg.node_id(+) = npn.node_id 
                                     GROUP BY npn.path_node_id)
                                  UNION ALL
                                  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (
                                  SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                          eligible_cnt,  
                                                       ppe.node_id  
                                                  FROM (SELECT *  
                                                          FROM rpt_pax_promo_eligibility  
                                                         WHERE giver_recvr_type = 'receiver' AND node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) ))) ppe,  
                                                       promotion P,                                                        
                                                       rpt_participant_employer rad 
                                                 WHERE ppe.promotion_id = P.promotion_id  
                                                        AND ppe.participant_id = rad.user_id(+)
                                                       AND ( ( p_in_promotionId IS NULL ) OR P.promotion_id IN (
                                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))                                                      
                                                       AND P.promotion_status =  
                                                              NVL (p_in_promotionstatus,  
                                                                   P.promotion_status)  
                                               AND ((p_in_departments IS NULL) OR rad.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
                                               AND NVL (rad.position_type, 'JOB') =  
                                                      NVL (  
                                                         p_in_jobposition,  
                                                         NVL (rad.position_type, 'JOB')) 
                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                      NVL (p_in_participantStatus,  
                                                           DECODE(rad.termination_date,NULL,'active','inactive'))  
                                                       AND P.promotion_type = 'product_claim'                                                  
                                              GROUP BY ppe.node_id) ;     
END IF;    --08/06/2014 End

 l_query  := 'SELECT * FROM
  ( ';
    
    v_sortCol := ' '|| p_in_sortedOn || ' ' || p_in_sortedBy;
       
  l_query := l_query ||'  '||'SELECT ROWNUM RN, --08/29/2014 Bug # 56110
       org_id AS ORG_ID,
       org_name AS ORG_NAME,
       elig_sub,
       act_sub,
       DECODE (elig_sub, 0, 0, ROUND ( (act_sub / elig_sub) * 100, 2)) AS PARTICIPATION_RATE,
       TOTAL_CLAIMS AS TOTAL_CLAIMS,
       CLAIMS_OPEN AS CLAIMS_OPEN,
       CLAIMS_CLOSED AS CLAIMS_CLOSED,
       ITEMS_APPROVED AS ITEMS_APPROVED,
       ITEMS_DENIED AS ITEMS_DENIED,
       ITEMS_HOLD AS ITEMS_HOLD,
       POINTS AS POINTS,
       SWEEPSTAKES_WON AS SWEEPSTAKES_WON,
       is_leaf AS is_leaf
  FROM (SELECT c_s.org_id AS ORG_ID,
               c_s.org_name AS ORG_NAME,
               (SELECT eligible_count
                  FROM gtt_recog_elg_count
                 WHERE node_id = c_s.org_id)
                  elig_sub,
               pkg_report_claim.fnc_actual_submitter ('''||p_in_promotionId||''','''||p_in_promotionStatus||''',org_id,
               CASE               
                     WHEN ORG_ID IN (SELECT *           --08/25/2014 Bug 55594
                                       FROM TABLE (
                                               get_array_varchar (
                                                  NVL (
                                                     '''||p_in_parentNodeId||''',
                                                     0))))
                     THEN ''team'' ELSE ''node'' END,'''||p_in_claimStatus||''','''||p_in_participantStatus||''','''||p_in_jobPosition||''','''||p_in_departments||''',TO_DATE ('''||p_in_fromDate||''','''||p_in_localeDatePattern||'''),
                                         TO_DATE ('''||p_in_toDate||''',  '''||p_in_localeDatePattern||'''))   act_sub,
                                     c_s.total_claim AS TOTAL_CLAIMS,
                                     c_s.claim_open AS CLAIMS_OPEN,
                                     c_s.claim_closed AS CLAIMS_CLOSED,
                                     c_s.item_approv AS ITEMS_APPROVED,
                                     c_s.item_deny AS ITEMS_DENIED,
                                     c_s.item_hold AS ITEMS_HOLD,
                                     c_s.points AS POINTS,
                                     c_s.sweepstakes_won AS SWEEPSTAKES_WON,
                                     c_s.is_leaf
                                FROM (SELECT node_id AS org_id,
                                             node_name || '' Team'' org_name,
                                             0 total_claim,
                                             0 claim_open,
                                             0 claim_closed,
                                             0 item_approv,
                                             0 item_deny,
                                             0 item_hold,
                                             0 sweepstakes_won,
                                             1 is_leaf,
                                             0 points
                                        FROM rpt_hierarchy
                                       WHERE     NVL (node_id, 0) IN (SELECT *
                                                                        FROM TABLE (get_array_varchar (NVL ('''||p_in_parentNodeId||''',0))))
                                             AND node_id NOT IN (SELECT DISTINCT
                                                                        rh.node_id
                                                                            org_id
                                                                   FROM rpt_claim_summary rcs,
                                                                        rpt_hierarchy rh,
                                                                        promotion p
                                                                  WHERE     rcs.detail_node_id(+) =
                                                                                rh.node_id
                                                                        AND DECODE (
                                                                                rcs.promotion_id,
                                                                                0, p.promotion_id,
                                                                                rcs.promotion_id) =
                                                                                p.promotion_id
                                                                        AND rcs.record_type LIKE
                                                                                ''%team%''
                                                                        AND p.promotion_status =
                                                                                NVL (
                                                                                    '''||p_in_promotionStatus||''',
                                                                                    p.promotion_status)
                                                                        AND ( ( '''||p_in_promotionId||''' IS NULL )
                                                                        OR
                                                                        p.promotion_id IN (
                                                                            SELECT * FROM TABLE ( get_array_varchar('''||p_in_promotionId||'''))))
                                                                        AND NVL (rcs.status,''open'') =NVL ('''||p_in_claimStatus||''', NVL (rcs.status,''open'')) --10/23/2014 Bug 57533
                                                                        AND NVL (rcs.detail_node_id,0) IN (SELECT * FROM TABLE (get_array_varchar (NVL ('''||p_in_parentNodeId||''',0)))) --08/25/2014 Bug 55594
                                                                        AND rcs.pax_status = NVL ('''||p_in_participantStatus||''',rcs.pax_status)
                                                                        AND NVL (rcs.job_position,''JOB'') = NVL ('''||p_in_jobPosition||''',NVL (rcs.job_position,''JOB''))
                                                                        AND ( ( '''||p_in_departments||''' IS NULL ) OR rcs.department IN (SELECT * FROM TABLE ( get_array_varchar('''||p_in_departments||'''))))
                                                                        AND NVL (TRUNC (rcs.date_submitted),TRUNC (SYSDATE)) BETWEEN TO_DATE ('''||p_in_fromDate||''','''||p_in_localeDatePattern||''')
                                                                                                  AND TO_DATE ('''||p_in_toDate||''','''||p_in_localeDatePattern||'''))
                                      UNION
                                        SELECT rh.node_id AS org_id,
                                               rh.node_name || '' Team'' org_name,
                                               SUM (NVL (rcs.claim_count, 0))
                                                   total_claim,
                                               SUM (
                                                   DECODE (rcs.status,
                                                           ''open'', rcs.claim_count,--10/23/2014 Bug 57533
                                                           0))
                                                   claim_open,
                                               SUM (
                                                   DECODE (
                                                       rcs.status,
                                                       ''closed'', rcs.claim_count,
                                                       0))
                                                   claim_closed,
                                               pkg_report_claim.fnc_item_count (
                                                       '''||p_in_promotionId||''',  --11/04/2014 Bug fix 57535
                                                       '''||p_in_promotionStatus||''',
                                                       rcs.detail_node_id,
                                                       ''team'',
                                                       ''approv'',
                                                       '''||p_in_claimStatus||''',
                                                       '''||p_in_participantStatus||''',
                                                       '''||p_in_jobPosition||''',
                                                       '''||p_in_departments||''',  --11/04/2014 Bug fix 57535
                                                       TO_DATE (
                                                           '''||p_in_fromDate||''',
                                                           '''||p_in_localeDatePattern||'''),
                                                       TO_DATE (
                                                           '''||p_in_toDate||''',
                                                           '''||p_in_localeDatePattern||'''))
                                                   item_approv,
                                             pkg_report_claim.fnc_item_count (
                                                       '''||p_in_promotionId||''',  --11/04/2014 Bug fix 57535
                                                       '''||p_in_promotionStatus||''',
                                                       rcs.detail_node_id,
                                                       ''team'',
                                                       ''deny'',
                                                       '''||p_in_claimStatus||''',
                                                       '''||p_in_participantStatus||''',
                                                       '''||p_in_jobPosition||''',
                                                       '''||p_in_departments||''',  --11/04/2014 Bug fix 57535
                                                       TO_DATE (
                                                           '''||p_in_fromDate||''',
                                                           '''||p_in_localeDatePattern||'''),
                                                       TO_DATE (
                                                           '''||p_in_toDate||''',
                                                           '''||p_in_localeDatePattern||'''))
                                                   item_deny,
                                               pkg_report_claim.fnc_item_count (
                                                       '''||p_in_promotionId||''',  --11/04/2014 Bug fix 57535
                                                       '''||p_in_promotionStatus||''',
                                                       rcs.detail_node_id,
                                                       ''team'',
                                                       ''hold'',
                                                       '''||p_in_claimStatus||''',
                                                       '''||p_in_participantStatus||''',
                                                       '''||p_in_jobPosition||''',
                                                       '''||p_in_departments||''',  --11/04/2014 Bug fix 57535
                                                       TO_DATE (
                                                           '''||p_in_fromDate||''',
                                                           '''||p_in_localeDatePattern||'''),
                                                       TO_DATE (
                                                           '''||p_in_toDate||''',
                                                           '''||p_in_localeDatePattern||'''))
                                                   item_hold,
                                               SUM (
                                                   NVL (
                                                       rcs.submitter_sweepstakes_won,
                                                       0))
                                                   sweepstakes_won,
                                               NVL (rcs.is_leaf, 1) is_leaf,
                                               SUM (NVL (rcs.award_amount, 0))
                                                   points
                                          FROM rpt_claim_summary rcs,
                                               rpt_hierarchy rh,
                                               promotion p
                                         WHERE     rcs.detail_node_id = rh.node_id
                                               AND DECODE (rcs.promotion_id,0, p.promotion_id,rcs.promotion_id) = p.promotion_id
                                               AND rcs.record_type LIKE ''%team%''
                                               AND p.promotion_status =
                                                       NVL ('''||p_in_promotionStatus||''',
                                                            p.promotion_status)
                                               AND ( ( '''||p_in_promotionId||''' IS NULL )
                                               OR
                                               p.promotion_id IN (
                                                    SELECT * FROM TABLE ( get_array_varchar('''||p_in_promotionId||'''))))
                                               AND NVL (rcs.status, ''open'') =NVL ('''||p_in_claimStatus||''',NVL (rcs.status, ''open'')) --10/23/2014 Bug 57533
                                               AND NVL (rcs.detail_node_id, 0) IN
                                                                        (SELECT * FROM TABLE ( get_array_varchar (
                                                                            NVL ('''||p_in_parentNodeId||''', 0)))) --08/25/2014 Bug 55594
                                               AND rcs.pax_status = NVL ('''||p_in_participantStatus||''',rcs.pax_status)
                                               AND NVL (rcs.job_position, ''JOB'') =NVL ('''||p_in_jobPosition||''',NVL (rcs.job_position,''JOB''))
                                               AND ( ( '''||p_in_departments||''' IS NULL )
                                               OR  rcs.department IN (SELECT * FROM TABLE ( get_array_varchar('''||p_in_departments||'''))))
                                               AND NVL (TRUNC (rcs.date_submitted),
                                                        TRUNC (SYSDATE)) BETWEEN TO_DATE ('''||p_in_fromDate||''','''||p_in_localeDatePattern||''')
                                                                             AND TO_DATE ('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
                                      GROUP BY rh.node_id,rh.node_name,rcs.detail_node_id,rcs.is_leaf
                                      UNION
                                        SELECT rh.node_id org_id,
                                               rh.node_name org_name,
                                               SUM (NVL (rcs.claim_count, 0))
                                                   total_claim,
                                               SUM (DECODE (rcs.status,''open'', rcs.claim_count,0))--10/23/2014 Bug 57533
                                                   open_status,
                                               SUM (
                                                   DECODE (
                                                       rcs.status,
                                                       ''closed'', rcs.claim_count,
                                                       0))
                                                   closed_status,                                               
                                                       pkg_report_claim.fnc_item_count (
                                                           '''||p_in_promotionId||''',  --11/04/2014 Bug fix 57535
                                                           '''||p_in_promotionStatus||''',
                                                           rh.node_id,
                                                           ''node'',
                                                           ''approv'',
                                                           '''||p_in_claimStatus||''',
                                                           '''||p_in_participantStatus||''',
                                                           '''||p_in_jobPosition||''',
                                                           '''||p_in_departments||''',  --11/04/2014 Bug fix 57535
                                                           TO_DATE ('''||p_in_fromDate||''','''||p_in_localeDatePattern||'''),
                                                           TO_DATE ('''||p_in_toDate||''','''||p_in_localeDatePattern||''')) item_approv,
                                               pkg_report_claim.fnc_item_count (
                                                           '''||p_in_promotionId||''',  --11/04/2014 Bug fix 57535
                                                           '''||p_in_promotionStatus||''',
                                                           rh.node_id,
                                                           ''node'',
                                                           ''deny'',
                                                           '''||p_in_claimStatus||''',
                                                           '''||p_in_participantStatus||''',
                                                           '''||p_in_jobPosition||''',
                                                           '''||p_in_departments||''',  --11/04/2014 Bug fix 57535
                                                           TO_DATE ('''||p_in_fromDate||''','''||p_in_localeDatePattern||'''),
                                                           TO_DATE ('''||p_in_toDate||''','''||p_in_localeDatePattern||'''))                                                  item_deny,
                                              pkg_report_claim.fnc_item_count (
                                                           '''||p_in_promotionId||''',  --11/04/2014 Bug fix 57535
                                                           '''||p_in_promotionStatus||''',
                                                           rh.node_id,
                                                           ''node'',
                                                           ''hold'',
                                                           '''||p_in_claimStatus||''',
                                                           '''||p_in_participantStatus||''',
                                                           '''||p_in_jobPosition||''',
                                                           '''||p_in_departments||''',  --11/04/2014 Bug fix 57535
                                                           TO_DATE ('''||p_in_fromDate||''','''||p_in_localeDatePattern||'''),
                                                           TO_DATE ('''||p_in_toDate||''','''||p_in_localeDatePattern||''')) item_hold,
                                               SUM (NVL (rcs.submitter_sweepstakes_won,0)) sweepstakes_won,
                                               0 is_leaf,SUM (NVL (rcs.award_amount, 0)) points
                                          FROM rpt_claim_summary rcs,
                                               rpt_hierarchy rh,
                                               promotion p
                                         WHERE     rcs.detail_node_id(+) =
                                                       rh.node_id
                                               AND DECODE (rcs.promotion_id,
                                                           0, p.promotion_id,
                                                           rcs.promotion_id) =
                                                       p.promotion_id
                                               AND rcs.record_type LIKE ''%node%''
                                               AND p.promotion_status =
                                                       NVL ('''||p_in_promotionStatus||''',
                                                            p.promotion_status)
                                               AND ( ( '''||p_in_promotionId||''' IS NULL )
                                               OR
                                               p.promotion_id IN (
                                                    SELECT * FROM TABLE ( get_array_varchar('''||p_in_promotionId||'''))))
                                               AND NVL (rcs.status, ''open'') = --10/23/2014 Bug 57533
                                                       NVL (
                                                           '''||p_in_claimStatus||''',
                                                           NVL (rcs.status, ''open'')) --10/23/2014 Bug 57533
                                               AND NVL (rh.parent_node_id, 0) IN
                                                        (SELECT *
                                                            FROM TABLE (
                                                                     get_array_varchar (
                                                                        NVL ('''||p_in_parentNodeId||''', 0)))) --08/25/2014 Bug 55594
                                               AND rcs.pax_status = NVL ('''||p_in_participantStatus||''',rcs.pax_status)
                                               AND NVL (rcs.job_position, ''JOB'') = NVL ('''||p_in_jobPosition||''',NVL (rcs.job_position,''JOB''))
                                               AND ( ( '''||p_in_departments||''' IS NULL ) OR rcs.department IN (SELECT * FROM TABLE ( get_array_varchar('''||p_in_departments||'''))))
                                               AND NVL (TRUNC (rcs.date_submitted),
                                                        TRUNC (SYSDATE)) BETWEEN TO_DATE ('''||p_in_fromDate||''','''||p_in_localeDatePattern||''')
                                                                             AND TO_DATE ('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
                                      GROUP BY rh.node_id,rh.node_name,rcs.detail_node_id,rcs.is_leaf
                                      UNION ALL
                                      SELECT node_id AS org_id,
                                             node_name org_name,
                                             0 total_claim,
                                             0 claim_open,
                                             0 claim_closed,
                                             0 item_approv,
                                             0 item_deny,
                                             0 item_hold,
                                             0 sweepstakes_won,
                                             0 is_leaf,
                                             0 points
                                        FROM RPT_HIERARCHY
                                       WHERE     PARENT_NODE_ID IN
                                                     (SELECT * FROM TABLE (get_array_varchar (
                                                        NVL ( '''||p_in_parentNodeId||''',0)))) --08/25/2014 Bug 55594
                                             AND node_id NOT IN (SELECT DISTINCT
                                                                        rh.node_id
                                                                            org_id
                                                                   FROM rpt_claim_summary rcs,
                                                                        rpt_hierarchy rh,
                                                                        promotion p
                                                                  WHERE     rcs.detail_node_id(+) =
                                                                                rh.node_id
                                                                        AND DECODE (
                                                                                rcs.promotion_id,
                                                                                0, p.promotion_id,
                                                                                rcs.promotion_id) =
                                                                                p.promotion_id
                                                                        AND rcs.record_type LIKE
                                                                                ''%node%''
                                                                        AND p.promotion_status =
                                                                                NVL (
                                                                                    '''||p_in_promotionStatus||''',
                                                                                    p.promotion_status)
                                                                        AND ( ( '''||p_in_promotionId||''' IS NULL )
                                                                        OR
                                                                        p.promotion_id IN (SELECT * FROM TABLE ( get_array_varchar('''||p_in_promotionId||'''))))
                                                                        AND NVL (rcs.status,''open'') =NVL ('''||p_in_claimStatus||''',NVL (rcs.status,''open''))--10/23/2014 Bug 57533
                                                                        AND NVL (rh.parent_node_id,0) IN (SELECT * FROM TABLE (get_array_varchar (NVL ('''||p_in_parentNodeId||''', 0)))) --08/25/2014 Bug 55594
                                                                        AND rcs.pax_status=NVL ('''||p_in_participantStatus||''',rcs.pax_status)
                                                                        AND NVL (rcs.job_position,''JOB'') =NVL ('''||p_in_jobPosition||''',NVL (rcs.job_position,''JOB''))
                                                                       AND ( ( '''||p_in_departments||''' IS NULL )
                                                                       OR rcs.department IN (SELECT * FROM TABLE ( get_array_varchar('''||p_in_departments||'''))))
                                                                        AND NVL (TRUNC (rcs.date_submitted),TRUNC (SYSDATE)) BETWEEN TO_DATE ('''||p_in_fromDate||''','''||p_in_localeDatePattern||''')AND TO_DATE ('''||p_in_toDate||''','''||p_in_localeDatePattern||''')))
                                     c_s)
                    ORDER BY '|| v_sortCol ||'
    ) WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd;


              OPEN p_out_data FOR l_query;
              
              -- 11/07/2016       Bug 69891 using the above query for totals
   
   OPEN p_out_totals_data FOR
    SELECT SUM (elig_sub) AS elig_sub,
           SUM (act_sub) AS act_sub,
           DECODE (
                     SUM (elig_sub), 0, 0,
                         ROUND ( (SUM (act_sub) / SUM (elig_sub)) * 100, 2))
               AS PARTICIPATION_RATE,
           SUM (TOTAL_CLAIMS) AS TOTAL_CLAIMS,
           SUM (CLAIMS_OPEN) AS CLAIMS_OPEN,
           SUM (CLAIMS_CLOSED) AS CLAIMS_CLOSED,
           SUM (ITEMS_APPROVED) AS ITEMS_APPROVED,
           SUM (ITEMS_DENIED) AS ITEMS_DENIED,
           SUM (ITEMS_HOLD) AS ITEMS_HOLD,
           SUM (POINTS) AS POINTS,
           SUM (SWEEPSTAKES_WON) AS SWEEPSTAKES_WON
      FROM (
            SELECT ROWNUM RN, --08/29/2014 Bug # 56110
                   org_id AS ORG_ID,
                   org_name AS ORG_NAME,
                   elig_sub,
                   act_sub,
                   DECODE (elig_sub, 0, 0, ROUND ( (act_sub / elig_sub) * 100, 2)) AS PARTICIPATION_RATE,
                   TOTAL_CLAIMS AS TOTAL_CLAIMS,
                   CLAIMS_OPEN AS CLAIMS_OPEN,
                   CLAIMS_CLOSED AS CLAIMS_CLOSED,
                   ITEMS_APPROVED AS ITEMS_APPROVED,
                   ITEMS_DENIED AS ITEMS_DENIED,
                   ITEMS_HOLD AS ITEMS_HOLD,
                   POINTS AS POINTS,
                   SWEEPSTAKES_WON AS SWEEPSTAKES_WON,
                   is_leaf AS is_leaf
              FROM (SELECT c_s.org_id AS ORG_ID,
               c_s.org_name AS ORG_NAME,
               (SELECT eligible_count
                  FROM gtt_recog_elg_count
                 WHERE node_id = c_s.org_id)
                  elig_sub,
               pkg_report_claim.fnc_actual_submitter (p_in_promotionId,p_in_promotionStatus,org_id,
               CASE               
                     WHEN ORG_ID IN (SELECT *           --08/25/2014 Bug 55594
                                       FROM TABLE (
                                               get_array_varchar (
                                                  NVL (
                                                     p_in_parentNodeId,
                                                     0))))
                     THEN 'team' ELSE 'node' END,p_in_claimStatus,
                                        p_in_participantStatus,p_in_jobPosition,p_in_departments
                                        ,TO_DATE (p_in_fromDate,p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,  p_in_localeDatePattern))   act_sub,
                                     c_s.total_claim AS TOTAL_CLAIMS,
                                     c_s.claim_open AS CLAIMS_OPEN,
                                     c_s.claim_closed AS CLAIMS_CLOSED,
                                     c_s.item_approv AS ITEMS_APPROVED,
                                     c_s.item_deny AS ITEMS_DENIED,
                                     c_s.item_hold AS ITEMS_HOLD,
                                     c_s.points AS POINTS,
                                     c_s.sweepstakes_won AS SWEEPSTAKES_WON,
                                     c_s.is_leaf
                                FROM (SELECT node_id AS org_id,
                                             node_name || ' Team' org_name,
                                             0 total_claim,
                                             0 claim_open,
                                             0 claim_closed,
                                             0 item_approv,
                                             0 item_deny,
                                             0 item_hold,
                                             0 sweepstakes_won,
                                             1 is_leaf,
                                             0 points
                                        FROM rpt_hierarchy
                                       WHERE     NVL (node_id, 0) IN (SELECT *
                                                                        FROM TABLE (get_array_varchar (NVL (p_in_parentNodeId,0))))
                                             AND node_id NOT IN (SELECT DISTINCT
                                                                        rh.node_id
                                                                            org_id
                                                                   FROM rpt_claim_summary rcs,
                                                                        rpt_hierarchy rh,
                                                                        promotion p
                                                                  WHERE     rcs.detail_node_id(+) =
                                                                                rh.node_id
                                                                        AND DECODE (
                                                                                rcs.promotion_id,
                                                                                0, p.promotion_id,
                                                                                rcs.promotion_id) =
                                                                                p.promotion_id
                                                                        AND rcs.record_type LIKE
                                                                                '%team%'
                                                                        AND p.promotion_status =
                                                                                NVL (
                                                                                    p_in_promotionStatus,
                                                                                    p.promotion_status)
                                                                        AND ( ( p_in_promotionId IS NULL )
                                                                        OR
                                                                        p.promotion_id IN (
                                                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                                                        AND NVL (rcs.status,'open') =NVL (p_in_claimStatus, NVL (rcs.status,'open')) --10/23/2014 Bug 57533
                                                                        AND NVL (rcs.detail_node_id,0) IN (SELECT * FROM TABLE (get_array_varchar (NVL (p_in_parentNodeId,0)))) --08/25/2014 Bug 55594
                                                                        AND rcs.pax_status = NVL (p_in_participantStatus,rcs.pax_status)
                                                                        AND NVL (rcs.job_position,'JOB') = NVL (p_in_jobPosition,NVL (rcs.job_position,'JOB'))
                                                                        AND ( ( p_in_departments IS NULL ) OR rcs.department IN (SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                                                        AND NVL (TRUNC (rcs.date_submitted),TRUNC (SYSDATE)) BETWEEN TO_DATE (p_in_fromDate,p_in_localeDatePattern)
                                                                                                  AND TO_DATE (p_in_toDate,p_in_localeDatePattern))
                                      UNION
                                        SELECT rh.node_id AS org_id,
                                               rh.node_name || ' Team' org_name,
                                               SUM (NVL (rcs.claim_count, 0))
                                                   total_claim,
                                               SUM (
                                                   DECODE (rcs.status,
                                                           'open', rcs.claim_count,--10/23/2014 Bug 57533
                                                           0))
                                                   claim_open,
                                               SUM (
                                                   DECODE (
                                                       rcs.status,
                                                       'closed', rcs.claim_count,
                                                       0))
                                                   claim_closed,
                                               pkg_report_claim.fnc_item_count (
                                                       p_in_promotionId,  --11/04/2014 Bug fix 57535
                                                       p_in_promotionStatus,
                                                       rcs.detail_node_id,
                                                       'team',
                                                       'approv',
                                                       p_in_claimStatus,
                                                       p_in_participantStatus,
                                                       p_in_jobPosition,
                                                       p_in_departments,  --11/04/2014 Bug fix 57535
                                                       TO_DATE (
                                                           p_in_fromDate,
                                                           p_in_localeDatePattern),
                                                       TO_DATE (
                                                           p_in_toDate,
                                                           p_in_localeDatePattern))
                                                   item_approv,
                                             pkg_report_claim.fnc_item_count (
                                                       p_in_promotionId,  --11/04/2014 Bug fix 57535
                                                       p_in_promotionStatus,
                                                       rcs.detail_node_id,
                                                       'team',
                                                       'deny',
                                                       p_in_claimStatus,
                                                       p_in_participantStatus,
                                                       p_in_jobPosition,
                                                       p_in_departments,  --11/04/2014 Bug fix 57535
                                                       TO_DATE (
                                                           p_in_fromDate,
                                                           p_in_localeDatePattern),
                                                       TO_DATE (
                                                           p_in_toDate,
                                                           p_in_localeDatePattern))
                                                   item_deny,
                                               pkg_report_claim.fnc_item_count (
                                                       p_in_promotionId,  --11/04/2014 Bug fix 57535
                                                       p_in_promotionStatus,
                                                       rcs.detail_node_id,
                                                       'team',
                                                       'hold',
                                                       p_in_claimStatus,
                                                       p_in_participantStatus,
                                                       p_in_jobPosition,
                                                       p_in_departments,  --11/04/2014 Bug fix 57535
                                                       TO_DATE (
                                                           p_in_fromDate,
                                                           p_in_localeDatePattern),
                                                       TO_DATE (
                                                           p_in_toDate,
                                                           p_in_localeDatePattern))
                                                   item_hold,
                                               SUM (
                                                   NVL (
                                                       rcs.submitter_sweepstakes_won,
                                                       0))
                                                   sweepstakes_won,
                                               NVL (rcs.is_leaf, 1) is_leaf,
                                               SUM (NVL (rcs.award_amount, 0))
                                                   points
                                          FROM rpt_claim_summary rcs,
                                               rpt_hierarchy rh,
                                               promotion p
                                         WHERE     rcs.detail_node_id = rh.node_id
                                               AND DECODE (rcs.promotion_id,0, p.promotion_id,rcs.promotion_id) = p.promotion_id
                                               AND rcs.record_type LIKE '%team%'
                                               AND p.promotion_status =
                                                       NVL (p_in_promotionStatus,
                                                            p.promotion_status)
                                               AND ( ( p_in_promotionId IS NULL )
                                               OR
                                               p.promotion_id IN (
                                                    SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                               AND NVL (rcs.status, 'open') =NVL (p_in_claimStatus,NVL (rcs.status, 'open')) --10/23/2014 Bug 57533
                                               AND NVL (rcs.detail_node_id, 0) IN
                                                                        (SELECT * FROM TABLE ( get_array_varchar (
                                                                            NVL (p_in_parentNodeId, 0)))) --08/25/2014 Bug 55594
                                               AND rcs.pax_status = NVL (p_in_participantStatus,rcs.pax_status)
                                               AND NVL (rcs.job_position, 'JOB') =NVL (p_in_jobPosition,NVL (rcs.job_position,'JOB'))
                                               AND ( ( p_in_departments IS NULL )
                                               OR  rcs.department IN (SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                               AND NVL (TRUNC (rcs.date_submitted),
                                                        TRUNC (SYSDATE)) BETWEEN TO_DATE (p_in_fromDate,p_in_localeDatePattern)
                                                                             AND TO_DATE (p_in_toDate,p_in_localeDatePattern)
                                      GROUP BY rh.node_id,rh.node_name,rcs.detail_node_id,rcs.is_leaf
                                      UNION
                                        SELECT rh.node_id org_id,
                                               rh.node_name org_name,
                                               SUM (NVL (rcs.claim_count, 0))
                                                   total_claim,
                                               SUM (DECODE (rcs.status,'open', rcs.claim_count,0))--10/23/2014 Bug 57533
                                                   open_status,
                                               SUM (
                                                   DECODE (
                                                       rcs.status,
                                                       'closed', rcs.claim_count,
                                                       0))
                                                   closed_status,                                               
                                                       pkg_report_claim.fnc_item_count (
                                                           p_in_promotionId,  --11/04/2014 Bug fix 57535
                                                           p_in_promotionStatus,
                                                           rh.node_id,
                                                           'node',
                                                           'approv',
                                                           p_in_claimStatus,
                                                           p_in_participantStatus,
                                                           p_in_jobPosition,
                                                           p_in_departments,  --11/04/2014 Bug fix 57535
                                                           TO_DATE (p_in_fromDate,p_in_localeDatePattern),
                                                           TO_DATE (p_in_toDate,p_in_localeDatePattern)) item_approv,
                                               pkg_report_claim.fnc_item_count (
                                                           p_in_promotionId,  --11/04/2014 Bug fix 57535
                                                           p_in_promotionStatus,
                                                           rh.node_id,
                                                           'node',
                                                           'deny',
                                                           p_in_claimStatus,
                                                           p_in_participantStatus,
                                                           p_in_jobPosition,
                                                           p_in_departments,  --11/04/2014 Bug fix 57535
                                                           TO_DATE (p_in_fromDate,p_in_localeDatePattern),
                                                           TO_DATE (p_in_toDate,p_in_localeDatePattern))                                                  item_deny,
                                              pkg_report_claim.fnc_item_count (
                                                           p_in_promotionId,  --11/04/2014 Bug fix 57535
                                                           p_in_promotionStatus,
                                                           rh.node_id,
                                                           'node',
                                                           'hold',
                                                           p_in_claimStatus,
                                                           p_in_participantStatus,
                                                           p_in_jobPosition,
                                                           p_in_departments,  --11/04/2014 Bug fix 57535
                                                           TO_DATE (p_in_fromDate,p_in_localeDatePattern),
                                                           TO_DATE (p_in_toDate,p_in_localeDatePattern)) item_hold,
                                               SUM (NVL (rcs.submitter_sweepstakes_won,0)) sweepstakes_won,
                                               0 is_leaf,SUM (NVL (rcs.award_amount, 0)) points
                                          FROM rpt_claim_summary rcs,
                                               rpt_hierarchy rh,
                                               promotion p
                                         WHERE     rcs.detail_node_id(+) =
                                                       rh.node_id
                                               AND DECODE (rcs.promotion_id,
                                                           0, p.promotion_id,
                                                           rcs.promotion_id) =
                                                       p.promotion_id
                                               AND rcs.record_type LIKE '%node%'
                                               AND p.promotion_status =
                                                       NVL (p_in_promotionStatus,
                                                            p.promotion_status)
                                               AND ( ( p_in_promotionId IS NULL )
                                               OR
                                               p.promotion_id IN (
                                                    SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                               AND NVL (rcs.status, 'open') = --10/23/2014 Bug 57533
                                                       NVL (
                                                           p_in_claimStatus,
                                                           NVL (rcs.status, 'open')) --10/23/2014 Bug 57533
                                               AND NVL (rh.parent_node_id, 0) IN
                                                        (SELECT *
                                                            FROM TABLE (
                                                                     get_array_varchar (
                                                                        NVL (p_in_parentNodeId, 0)))) --08/25/2014 Bug 55594
                                               AND rcs.pax_status = NVL (p_in_participantStatus,rcs.pax_status)
                                               AND NVL (rcs.job_position, 'JOB') = NVL (p_in_jobPosition,NVL (rcs.job_position,'JOB'))
                                               AND ( ( p_in_departments IS NULL ) OR rcs.department IN (SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                               AND NVL (TRUNC (rcs.date_submitted),
                                                        TRUNC (SYSDATE)) BETWEEN TO_DATE (p_in_fromDate,p_in_localeDatePattern)
                                                                             AND TO_DATE (p_in_toDate,p_in_localeDatePattern)
                                      GROUP BY rh.node_id,rh.node_name,rcs.detail_node_id,rcs.is_leaf
                                      UNION ALL
                                      SELECT node_id AS org_id,
                                             node_name org_name,
                                             0 total_claim,
                                             0 claim_open,
                                             0 claim_closed,
                                             0 item_approv,
                                             0 item_deny,
                                             0 item_hold,
                                             0 sweepstakes_won,
                                             0 is_leaf,
                                             0 points
                                        FROM RPT_HIERARCHY
                                       WHERE     PARENT_NODE_ID IN
                                                     (SELECT * FROM TABLE (get_array_varchar (
                                                        NVL ( p_in_parentNodeId,0)))) --08/25/2014 Bug 55594
                                             AND node_id NOT IN (SELECT DISTINCT
                                                                        rh.node_id
                                                                            org_id
                                                                   FROM rpt_claim_summary rcs,
                                                                        rpt_hierarchy rh,
                                                                        promotion p
                                                                  WHERE     rcs.detail_node_id(+) =
                                                                                rh.node_id
                                                                        AND DECODE (
                                                                                rcs.promotion_id,
                                                                                0, p.promotion_id,
                                                                                rcs.promotion_id) =
                                                                                p.promotion_id
                                                                        AND rcs.record_type LIKE
                                                                                '%node%'
                                                                        AND p.promotion_status =
                                                                                NVL (
                                                                                    p_in_promotionStatus,
                                                                                    p.promotion_status)
                                                                        AND ( ( p_in_promotionId IS NULL )
                                                                        OR
                                                                        p.promotion_id IN (SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                                                        AND NVL (rcs.status,'open') =NVL (p_in_claimStatus,NVL (rcs.status,'open'))--10/23/2014 Bug 57533
                                                                        AND NVL (rh.parent_node_id,0) IN (SELECT * FROM TABLE (get_array_varchar (NVL (p_in_parentNodeId, 0)))) --08/25/2014 Bug 55594
                                                                        AND rcs.pax_status=NVL (p_in_participantStatus,rcs.pax_status)
                                                                        AND NVL (rcs.job_position,'JOB') =NVL (p_in_jobPosition,NVL (rcs.job_position,'JOB'))
                                                                       AND ( ( p_in_departments IS NULL )
                                                                       OR rcs.department IN (SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                                                        AND NVL (TRUNC (rcs.date_submitted),TRUNC (SYSDATE)) BETWEEN TO_DATE (p_in_fromDate,p_in_localeDatePattern)AND TO_DATE (p_in_toDate,p_in_localeDatePattern)))
                                     c_s)
              
              );
   
   
            SELECT COUNT (1) INTO p_out_size_data
              FROM (SELECT HIERARCHY,
                           HEADER_NODE_ID,
                           DETAIL_NODE_ID,
                           SWEEPSTAKES_WON_COUNT,
                           HIER_LEVEL,
                           IS_LEAF,
                           NODE_NAME,
                           NODE_ID,
                           PARENT_NODE_ID,
                           ELIGIBLE_CNT,
                           claim_count,
                           claim_open,
                           claim_closed,
                           act_sub,
                           item_approv,
                           item_deny,
                           item_hold,
                           points
                      FROM ( (SELECT rn.HIERARCHY,
                                     rn.HEADER_NODE_ID,
                                     rn.DETAIL_NODE_ID,
                                     rn.SWEEPSTAKES_WON_COUNT,
                                     rn.HIER_LEVEL,
                                     rn.IS_LEAF,
                                     rn.NODE_NAME,
                                     rn.NODE_ID,
                                     rn.PARENT_NODE_ID,
                                     (SELECT eligible_count FROM gtt_recog_elg_count WHERE node_id = rn.detail_node_id) eligible_cnt,        
                                     claim_count,
                                     claim_open,
                                     claim_closed,
                                     pkg_report_claim.fnc_actual_submitter (
                                         p_in_promotionId,
                                         p_in_promotionStatus,
                                         detail_node_id,
                                         'team',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         act_sub,
                                     pkg_report_claim.fnc_item_count (
                                         p_in_promotionId,
                                         p_in_promotionStatus,
                                         detail_node_id,
                                         'team',
                                         'approv',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         item_approv,
                                     pkg_report_claim.fnc_item_count (
                                         p_in_promotionId,
                                         p_in_promotionStatus,
                                         detail_node_id,
                                         'team',
                                         'deny',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         item_deny,
                                     pkg_report_claim.fnc_item_count (
                                         p_in_promotionId,
                                         p_in_promotionStatus,
                                         detail_node_id,
                                         'team',
                                         'hold',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         item_hold,
                                     points
                                FROM (  SELECT hierarchy,
                                               header_node_id,
                                               detail_node_id,
                                               SUM (points) points,
                                               SUM (claim_count) claim_count,
                                               SUM (claim_open) claim_open,
                                               SUM (claim_closed) claim_closed,
                                               SUM (sweepstakes_won_count)
                                                   sweepstakes_won_count,
                                               hier_level,
                                               NVL (is_leaf, 0) is_leaf,
                                               node_name,
                                               node_id,
                                               node_type_name,
                                               parent_node_id
                                          FROM (SELECT node_name || ' Team'
                                                           hierarchy,
                                                       parent_node_id
                                                           header_node_id,
                                                       node_id detail_node_id,
                                                       0 points,
                                                       0 claim_count,
                                                       0 claim_open,
                                                       0 claim_closed,
                                                       0 sweepstakes_won_count,
                                                       hier_level,
                                                       1 is_leaf,
                                                       node_name,
                                                       node_id,
                                                       node_type_name,
                                                       parent_node_id
                                                  FROM rpt_hierarchy
                                                 WHERE NVL (node_id, 0) IN (SELECT *
                                                                              FROM TABLE (
                                                                                       get_array_varchar (
                                                                                           NVL (
                                                                                               p_in_parentNodeId,
                                                                                               0))))
                                                UNION
                                                  SELECT rh.node_name || ' Team'
                                                             hierarchy,
                                                         r.header_node_id,
                                                         r.detail_node_id,
                                                         SUM (
                                                             NVL (r.award_amount,
                                                                  0))
                                                             points,
                                                         SUM (r.claim_count)
                                                             claim_count,
                                                         SUM (
                                                             DECODE (
                                                                 r.status,
                                                                 'open', r.claim_count,
                                                                 0))
                                                             claim_open,
                                                         SUM (
                                                             DECODE (
                                                                 r.status,
                                                                 'closed', r.claim_count,
                                                                 0))
                                                             claim_closed,
                                                         SUM (
                                                             NVL (
                                                                 r.submitter_sweepstakes_won,
                                                                 0))
                                                             sweepstakes_won_count,
                                                         r.hier_level,
                                                         NVL (r.is_leaf, 0) is_leaf,
                                                         rh.node_name,
                                                         rh.node_id,
                                                         rh.node_type_name,
                                                         rh.parent_node_id
                                                    FROM rpt_claim_summary r,
                                                         rpt_hierarchy rh,
                                                         promotion p
                                                   WHERE     r.detail_node_id =
                                                                 rh.node_id(+)
                                                         AND r.promotion_id =
                                                                 p.promotion_id(+)
                                                         AND r.record_type LIKE
                                                                 '%team%'
                                                         AND NVL (rh.node_id, 0) IN (SELECT *
                                                                                       FROM TABLE (
                                                                                                get_array_varchar (
                                                                                                    NVL (
                                                                                                        p_in_parentNodeId,
                                                                                                        0))))
                                                         AND r.pax_status =
                                                                 NVL (
                                                                     p_in_participantStatus,
                                                                     r.pax_status)
                                                         AND r.job_position =
                                                                 NVL (
                                                                     p_in_jobPosition,
                                                                     r.job_position)
                                                         AND NVL (r.status, 'open') = --10/23/2014 Bug 57533
                                                                 NVL (
                                                                     p_in_claimStatus,
                                                                     NVL (r.status,
                                                                          'open')) --10/23/2014 Bug 57533
                                                         AND TRUNC (
                                                                 NVL (
                                                                     r.date_submitted,
                                                                     SYSDATE)) BETWEEN TO_DATE (
                                                                                           p_in_fromDate,
                                                                                           p_in_localeDatePattern)
                                                                                   AND TO_DATE (
                                                                                           p_in_toDate,
                                                                                           p_in_localeDatePattern)
                                                         AND (   (p_in_departments
                                                                      IS NULL)
                                                              OR (    p_in_departments
                                                                          IS NOT NULL
                                                                  AND r.department IN (SELECT *
                                                                                         FROM TABLE (
                                                                                                  get_array_varchar (
                                                                                                      p_in_departments)))))
                                                         AND (   (p_in_promotionId
                                                                      IS NULL)
                                                              OR (    p_in_promotionId
                                                                          IS NOT NULL
                                                                  AND r.promotion_id IN (SELECT *
                                                                                           FROM TABLE (
                                                                                                    get_array_varchar (
                                                                                                        p_in_promotionId)))))
                                                         AND NVL (
                                                                 p.promotion_status,
                                                                 0) =
                                                                 NVL (
                                                                     p_in_promotionStatus,
                                                                     NVL (
                                                                         p.promotion_status,
                                                                         0))
                                                         AND rh.is_deleted = 0
                                                GROUP BY rh.node_name,
                                                         rh.node_id,
                                                         r.header_node_id,
                                                         r.detail_node_id,
                                                         r.hier_level,
                                                         NVL (r.is_leaf, 0),
                                                         rh.node_type_name,
                                                         rh.parent_node_id)
                                      GROUP BY hierarchy,
                                               header_node_id,
                                               detail_node_id,
                                               hier_level,
                                               NVL (is_leaf, 0),
                                               node_name,
                                               node_id,
                                               node_type_name,
                                               parent_node_id) rn)
                            UNION
                            (SELECT rn.HIERARCHY,
                                    rn.HEADER_NODE_ID,
                                    rn.DETAIL_NODE_ID,
                                    rn.SWEEPSTAKES_WON_COUNT,
                                    rn.HIER_LEVEL,
                                    rn.IS_LEAF,
                                    rn.NODE_NAME,
                                    rn.NODE_ID,
                                    rn.PARENT_NODE_ID,
                                    (SELECT eligible_count FROM gtt_recog_elg_count WHERE node_id = rn.detail_node_id) eligible_cnt,        
                                    claim_count,
                                    claim_open,
                                    claim_closed,
                                    pkg_report_claim.fnc_actual_submitter (
                                        p_in_promotionId,
                                        p_in_promotionStatus,
                                        detail_node_id,
                                        'node',
                                        p_in_claimStatus,
                                        p_in_participantStatus,
                                        p_in_jobPosition,
                                        p_in_departments,
                                        TO_DATE (p_in_fromDate,
                                                 p_in_localeDatePattern),
                                        TO_DATE (p_in_toDate,
                                                 p_in_localeDatePattern))
                                        act_sub,
                                    pkg_report_claim.fnc_item_count (
                                        p_in_promotionId,
                                        p_in_promotionStatus,
                                        detail_node_id,
                                        'node',
                                        'approv',
                                        p_in_claimStatus,
                                        p_in_participantStatus,
                                        p_in_jobPosition,
                                        p_in_departments,
                                        TO_DATE (p_in_fromDate,
                                                 p_in_localeDatePattern),
                                        TO_DATE (p_in_toDate,
                                                 p_in_localeDatePattern))
                                        item_approv,
                                    pkg_report_claim.fnc_item_count (
                                        p_in_promotionId,
                                        p_in_promotionStatus,
                                        detail_node_id,
                                        'node',
                                        'deny',
                                        p_in_claimStatus,
                                        p_in_participantStatus,
                                        p_in_jobPosition,
                                        p_in_departments,
                                        TO_DATE (p_in_fromDate,
                                                 p_in_localeDatePattern),
                                        TO_DATE (p_in_toDate,
                                                 p_in_localeDatePattern))
                                        item_deny,
                                    pkg_report_claim.fnc_item_count (
                                        p_in_promotionId,
                                        p_in_promotionStatus,
                                        detail_node_id,
                                        'node',
                                        'hold',
                                        p_in_claimStatus,
                                        p_in_participantStatus,
                                        p_in_jobPosition,
                                        p_in_departments,
                                        TO_DATE (p_in_fromDate,
                                                 p_in_localeDatePattern),
                                        TO_DATE (p_in_toDate,
                                                 p_in_localeDatePattern))
                                        item_hold,
                                    points
                               FROM (  SELECT hierarchy,
                                              header_node_id,
                                              detail_node_id,
                                              SUM (points) points,
                                              SUM (claim_count) claim_count,
                                              SUM (claim_open) claim_open,
                                              SUM (claim_closed) claim_closed,
                                              SUM (sweepstakes_won_count)
                                                  sweepstakes_won_count,
                                              hier_level,
                                              NVL (is_leaf, 0) is_leaf,
                                              node_name,
                                              node_id,
                                              node_type_name,
                                              parent_node_id
                                         FROM (SELECT node_name hierarchy,
                                                      parent_node_id
                                                          header_node_id,
                                                      node_id detail_node_id,
                                                      0 points,
                                                      0 claim_count,
                                                      0 claim_open,
                                                      0 claim_closed,
                                                      0 sweepstakes_won_count,
                                                      hier_level,
                                                      NVL (is_leaf, 0) is_leaf,
                                                      node_name,
                                                      node_id,
                                                      node_type_name,
                                                      parent_node_id
                                                 FROM rpt_hierarchy
                                                WHERE NVL (parent_node_id, 0) IN (SELECT *
                                                                                    FROM TABLE (
                                                                                             get_array_varchar (
                                                                                                 NVL (
                                                                                                     p_in_parentNodeId,
                                                                                                     0))))
                                               UNION
                                                 SELECT rh.node_name hierarchy,
                                                        r.header_node_id,
                                                        r.detail_node_id,
                                                        SUM (
                                                            NVL (r.award_amount, 0))
                                                            points,
                                                        SUM (r.claim_count)
                                                            claim_count,
                                                        SUM (
                                                            DECODE (
                                                                r.status,
                                                                'open', r.claim_count,
                                                                0))
                                                            claim_open,
                                                        SUM (
                                                            DECODE (
                                                                r.status,
                                                                'closed', r.claim_count,
                                                                0))
                                                            claim_closed,
                                                        SUM (
                                                            NVL (
                                                                r.submitter_sweepstakes_won,
                                                                0))
                                                            sweepstakes_won_count,
                                                        r.hier_level,
                                                        NVL (r.is_leaf, 0) is_leaf,
                                                        rh.node_name,
                                                        rh.node_id,
                                                        rh.node_type_name,
                                                        rh.parent_node_id
                                                   FROM rpt_claim_summary r,
                                                        rpt_hierarchy rh,
                                                        promotion p
                                                  WHERE     r.detail_node_id =
                                                                rh.node_id(+)
                                                        AND r.promotion_id =
                                                                p.promotion_id(+)
                                                        AND r.record_type LIKE
                                                                '%node%'
                                                        AND NVL (rh.parent_node_id,
                                                                 0) IN (SELECT *
                                                                          FROM TABLE (
                                                                                   get_array_varchar (
                                                                                       NVL (
                                                                                           p_in_parentNodeId,
                                                                                           0))))
                                                        AND r.pax_status =
                                                                NVL (
                                                                    p_in_participantStatus,
                                                                    r.pax_status)
                                                        AND r.job_position =
                                                                NVL (
                                                                    p_in_jobPosition,
                                                                    r.job_position)
                                                        AND NVL (r.status, 'open') = --10/23/2014 Bug 57533
                                                                NVL (
                                                                    p_in_claimStatus,
                                                                    NVL (r.status,
                                                                         'open')) --10/23/2014 Bug 57533
                                                        AND TRUNC (
                                                                NVL (
                                                                    r.date_submitted,
                                                                    SYSDATE)) BETWEEN TO_DATE (
                                                                                          p_in_fromDate,
                                                                                          p_in_localeDatePattern)
                                                                                  AND TO_DATE (
                                                                                          p_in_toDate,
                                                                                          p_in_localeDatePattern)
                                                        AND (   (p_in_departments
                                                                     IS NULL)
                                                             OR (    p_in_departments
                                                                         IS NOT NULL
                                                                 AND r.department IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_departments)))))
                                                        AND (   (p_in_promotionId
                                                                     IS NULL)
                                                             OR (    p_in_promotionId
                                                                         IS NOT NULL
                                                                 AND r.promotion_id IN (SELECT *
                                                                                          FROM TABLE (
                                                                                                   get_array_varchar (
                                                                                                       p_in_promotionId)))))
                                                        AND NVL (
                                                                p.promotion_status,
                                                                0) =
                                                                NVL (
                                                                    p_in_promotionStatus,
                                                                    NVL (
                                                                        p.promotion_status,
                                                                        0))
                                                        AND rh.is_deleted = 0
                                               GROUP BY rh.node_name,
                                                        rh.node_id,
                                                        r.header_node_id,
                                                        r.detail_node_id,
                                                        r.hier_level,
                                                        NVL (r.is_leaf, 0),
                                                        rh.node_type_name,
                                                        rh.parent_node_id)
                                     GROUP BY hierarchy,
                                              header_node_id,
                                              detail_node_id,
                                              hier_level,
                                              NVL (is_leaf, 0),
                                              node_name,
                                              node_id,
                                              node_type_name,
                                              parent_node_id) rn)));

        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;
            p_out_size_data := NULL;
            OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;
    END prc_getByOrgTabRes;


    PROCEDURE prc_getByOrgStatus (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getByOrgStatus  ' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT *
              FROM (  SELECT c_s.org_name AS ORG_NAME,
                             c_s.claim_open AS CLAIMS_OPEN,
                             c_s.claim_closed AS CLAIMS_CLOSED
                        FROM (  SELECT org_name,
                                       SUM (act_sub) act_sub,
                                       SUM (claim_open) claim_open,
                                       SUM (claim_closed) claim_closed
                                  FROM (SELECT node_name || ' Team' org_name,
                                               0 act_sub,
                                               0 claim_open,
                                               0 claim_closed
                                          FROM rpt_hierarchy
                                         WHERE NVL (node_id, 0) IN (SELECT *
                                                                      FROM TABLE (
                                                                               get_array_varchar (
                                                                                   NVL (
                                                                                       p_in_parentNodeId,
                                                                                       0))))
                                        UNION
                                          SELECT rh.node_name || ' Team' org_name,
                                                 SUM (
                                                     pkg_report_claim.fnc_actual_submitter (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'team',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     act_sub,
                                                 SUM (
                                                     DECODE (rcs.status,
                                                             'open', rcs.claim_count,
                                                             0))
                                                     claim_open,
                                                 SUM (
                                                     DECODE (
                                                         rcs.status,
                                                         'closed', rcs.claim_count,
                                                         0))
                                                     claim_closed
                                            FROM rpt_claim_summary rcs,
                                                 rpt_hierarchy rh,
                                                 promotion p
                                           WHERE     rcs.detail_node_id = rh.node_id
                                                 AND DECODE (rcs.promotion_id,
                                                             0, p.promotion_id,
                                                             rcs.promotion_id) =
                                                         p.promotion_id
                                                 AND rcs.record_type LIKE '%team%'
                                                 AND p.promotion_status =
                                                         NVL (p_in_promotionStatus,
                                                              p.promotion_status)
                                                 AND ( ( p_in_promotionId IS NULL )
                                                 OR
                                                 p.promotion_id IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                                 AND NVL (rcs.status, 'open') = --10/23/2014 Bug 57533
                                                         NVL (
                                                             p_in_claimStatus,
                                                             NVL (rcs.status, 'open')) --10/23/2014 Bug 57533
                                                 AND NVL (rcs.detail_node_id, 0) IN 
                                                         (SELECT * FROM TABLE (get_array_varchar (NVL (p_in_parentNodeId, 0)))) --08/25/2014 Bug 55594        
                                                 AND (   (p_in_participantStatus
                                                              IS NULL)
                                                      OR (    p_in_participantStatus
                                                                  IS NOT NULL
                                                          AND rcs.pax_status =
                                                                  p_in_participantStatus))
                                                 AND (   (p_in_jobPosition IS NULL)
                                                      OR (    p_in_jobPosition
                                                                  IS NOT NULL
                                                          AND rcs.job_position =
                                                                  p_in_jobPosition))
                                                 AND ( ( p_in_departments IS NULL )
                                                 OR
                                                 rcs.department IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                                 AND NVL (TRUNC (rcs.date_submitted),
                                                          TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                       p_in_fromDate,
                                                                                       p_in_localeDatePattern)
                                                                               AND TO_DATE (
                                                                                       p_in_toDate,
                                                                                       p_in_localeDatePattern)
                                        GROUP BY rh.node_id,
                                                 rh.node_name,
                                                 rcs.detail_node_id)
                              GROUP BY org_name
                              UNION
                                SELECT org_name,
                                       SUM (act_sub) act_sub,
                                       SUM (claim_open) claim_open,
                                       SUM (claim_closed) claim_closed
                                  FROM (SELECT node_name org_name,
                                               0 act_sub,
                                               0 claim_open,
                                               0 claim_closed
                                          FROM rpt_hierarchy
                                         WHERE NVL (parent_node_id, 0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          NVL (
                                                                                              p_in_parentNodeId,
                                                                                              0))))
                                        UNION
                                          SELECT rh.node_name org_name,
                                                 SUM (
                                                     pkg_report_claim.fnc_actual_submitter (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'node',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     act_sub,
                                                 SUM (
                                                     DECODE (rcs.status,
                                                             'open', rcs.claim_count,
                                                             0))
                                                     claim_open,
                                                 SUM (
                                                     DECODE (
                                                         rcs.status,
                                                         'closed', rcs.claim_count,
                                                         0))
                                                     claim_closed
                                            FROM rpt_claim_summary rcs,
                                                 rpt_hierarchy rh,
                                                 promotion p
                                           WHERE     rcs.detail_node_id = rh.node_id
                                                 AND DECODE (rcs.promotion_id,
                                                             0, p.promotion_id,
                                                             rcs.promotion_id) =
                                                         p.promotion_id
                                                 AND rcs.record_type LIKE '%node%'
                                                 AND p.promotion_status =
                                                         NVL (p_in_promotionStatus,
                                                              p.promotion_status)
                                                 AND ( ( p_in_promotionId IS NULL )
                                                 OR
                                                 p.promotion_id IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                                 AND NVL (rcs.status, 'open') =--10/23/2014 Bug 57533
                                                         NVL (
                                                             p_in_claimStatus,
                                                             NVL (rcs.status, 'open')) --10/23/2014 Bug 57533
                                                 AND NVL (rcs.header_node_id, 0) IN (SELECT * FROM TABLE (get_array_varchar (
                                                         NVL (p_in_parentNodeId, 0)))) --08/25/2014 Bug 55594    
                                                 AND (   (p_in_participantStatus
                                                              IS NULL)
                                                      OR (    p_in_participantStatus
                                                                  IS NOT NULL
                                                          AND rcs.pax_status =
                                                                  p_in_participantStatus))
                                                 AND (   (p_in_jobPosition IS NULL)
                                                      OR (    p_in_jobPosition
                                                                  IS NOT NULL
                                                          AND rcs.job_position =
                                                                  p_in_jobPosition))
                                                 AND ( ( p_in_departments IS NULL )
                                                 OR
                                                 rcs.department IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                                 AND NVL (TRUNC (rcs.date_submitted),
                                                          TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                       p_in_fromDate,
                                                                                       p_in_localeDatePattern)
                                                                               AND TO_DATE (
                                                                                       p_in_toDate,
                                                                                       p_in_localeDatePattern)
                                        GROUP BY rh.node_name,
                                                 rcs.detail_node_id,
                                                 rcs.is_leaf)
                              GROUP BY org_name) c_s
                    ORDER BY (claims_open + claims_closed) DESC)
             WHERE ROWNUM < 21;

        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;
    END prc_getByOrgStatus;

    PROCEDURE prc_getByOrgMonthly (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getByOrgMonthly  ' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT
                MONTH || '-' || YEAR AS MONTH_NAME,
                total_claims_count AS CLAIM_COUNT
              FROM (WITH month_list
                         AS ( SELECT CMS_NAME month,
                             YEAR,
                             month_sort
                             FROM
                             (SELECT lower(TO_CHAR(ADD_MONTHS(TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern),
                             'mm'), ROWNUM - 1), 'MON' )) MONTH,
                                        TO_CHAR (
                                            ADD_MONTHS (
                                                TRUNC (
                                                    TO_DATE (
                                                        p_in_fromDate,
                                                        p_in_localeDatePattern),
                                                    'mm'),
                                                ROWNUM - 1),
                                            'yy')
                                            YEAR,
                                        ADD_MONTHS (
                                            TRUNC (
                                                TO_DATE (p_in_fromDate,
                                                         p_in_localeDatePattern),
                                                'mm'),
                                            ROWNUM - 1)
                                            month_sort
                                   FROM DUAL
                             CONNECT BY LEVEL <=
                                              MONTHS_BETWEEN (
                                                  TRUNC (
                                                      TO_DATE (
                                                          p_in_toDate,
                                                          p_in_localeDatePattern),
                                                      'mm'),
                                                  TRUNC (
                                                      TO_DATE (
                                                          p_in_fromDate,
                                                          p_in_localeDatePattern),
                                                          'mm')) + 1 ) mn,
                                                   (SELECT CMS_CODE, CMS_NAME
                                                   FROM VW_CMS_CODE_VALUE where asset_code = 'picklist.monthname.type.items'
                                                   and locale = p_in_languageCode) cm
                                                   WHERE lower(mn.month) = lower(cm.cms_code (+)))--05/22/2015 Bug 61982
                      SELECT ml.month,
                             ml.year,
                             NVL (rpt_c.claim_count, 0) total_claims_count
                        FROM (  SELECT MONTH, YEAR, SUM (claim_count) claim_count
                                  FROM (SELECT TO_CHAR (rcs.date_submitted, 'MON')
                                                   AS MONTH,
                                               TO_CHAR (rcs.date_submitted, 'YY')
                                                   AS YEAR,
                                               NVL (rcs.claim_count, 0) claim_count
                                          FROM rpt_claim_summary rcs,
                                               rpt_hierarchy rh,
                                               promotion p
                                         WHERE     rcs.detail_node_id = rh.node_id
                                               AND DECODE (rcs.promotion_id,
                                                           0, p.promotion_id,
                                                           rcs.promotion_id) =
                                                       p.promotion_id
                                               AND rcs.record_type LIKE '%node%'
                                               AND p.promotion_status =
                                                       NVL (p_in_promotionStatus,
                                                            p.promotion_status)
                                               AND ( ( p_in_promotionId IS NULL )
                                               OR
                                               p.promotion_id IN (
                                                    SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                               AND NVL (rcs.status, 'open') = --10/23/2014 Bug 57533
                                                       NVL (
                                                           p_in_claimStatus,
                                                           NVL (rcs.status, 'open')) --10/23/2014 Bug 57533
                                               AND NVL (rcs.header_node_id, 0) IN (SELECT * FROM TABLE (get_array_varchar (
                                                       NVL (p_in_parentNodeId, 0)))) --08/25/2014 Bug 55594    
                                               AND (   (p_in_participantStatus
                                                            IS NULL)
                                                    OR (    p_in_participantStatus
                                                                IS NOT NULL
                                                        AND rcs.pax_status =
                                                                p_in_participantStatus))
                                               AND NVL (rcs.job_position, 'job') =
                                                       NVL (
                                                           p_in_jobPosition,
                                                           NVL (rcs.job_position,
                                                                'job'))
                                               AND ( ( p_in_departments IS NULL )
                                               OR
                                               rcs.department IN (
                                                    SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                               AND rcs.date_submitted IS NOT NULL
                                               AND TRUNC (rcs.date_submitted) BETWEEN TO_DATE (
                                                                                          p_in_fromDate,
                                                                                          p_in_localeDatePattern)
                                                                                  AND TO_DATE (
                                                                                          p_in_toDate,
                                                                                          p_in_localeDatePattern)
                                        UNION ALL
                                        SELECT TO_CHAR (rcs.date_submitted, 'MON')
                                                   AS MONTH,
                                               TO_CHAR (rcs.date_submitted, 'YY')
                                                   AS YEAR,
                                               NVL (rcs.claim_count, 0) total_claim
                                          FROM rpt_claim_summary rcs,
                                               rpt_hierarchy rh,
                                               promotion p
                                         WHERE     rcs.detail_node_id = rh.node_id
                                               AND DECODE (rcs.promotion_id,
                                                           0, p.promotion_id,
                                                           rcs.promotion_id) =
                                                       p.promotion_id
                                               AND rcs.record_type LIKE '%team%'
                                               AND p.promotion_status =
                                                       NVL (p_in_promotionStatus,
                                                            p.promotion_status)
                                               AND ( ( p_in_promotionId IS NULL )
                                               OR
                                               p.promotion_id IN (
                                                    SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                               AND NVL (rcs.status, 'open') = --10/23/2014 Bug 57533
                                                       NVL (
                                                           p_in_claimStatus,
                                                           NVL (rcs.status, 'open')) --10/23/2014 Bug 57533
                                               AND NVL (rcs.detail_node_id, 0) IN (SELECT * FROM TABLE (get_array_varchar (
                                                       NVL (p_in_parentNodeId, 0)))) --08/25/2014 Bug 55594        
                                               AND (   (p_in_participantStatus
                                                            IS NULL)
                                                    OR (    p_in_participantStatus
                                                                IS NOT NULL
                                                        AND rcs.pax_status =
                                                                p_in_participantStatus))
                                               AND NVL (rcs.job_position, 'job') =
                                                       NVL (
                                                           p_in_jobPosition,
                                                           NVL (rcs.job_position,
                                                                'job'))
                                               AND ( ( p_in_departments IS NULL )
                                               OR
                                               rcs.department IN (
                                                    SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                               AND rcs.date_submitted IS NOT NULL
                                               AND NVL (TRUNC (rcs.date_submitted),
                                                        TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                     p_in_fromDate,
                                                                                     p_in_localeDatePattern)
                                                                             AND TO_DATE (
                                                                                     p_in_toDate,
                                                                                     p_in_localeDatePattern))
                              GROUP BY MONTH, YEAR) rpt_c,
                             month_list ml
                       WHERE     lower(ml.month) = lower(rpt_c.month(+)) --05/22/2015 Bug 61982
                             AND ml.year = rpt_c.year(+)
                    ORDER BY ml.month_sort);

        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;
    END prc_getByOrgMonthly;

    PROCEDURE prc_getByOrgPartRate (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getByOrgPartRate  ' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
    
    DELETE gtt_recog_elg_count; --08/06/2014 Start
    
    IF fnc_check_promo_aud('giver','product_claim',p_in_promotionId) = 1 THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_allaud_node pe
  WHERE  pe.giver_recvr_type = 'giver'
  AND pe.node_id IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) )
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id)
  UNION ALL
  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_allaud_team pe
  WHERE  pe.giver_recvr_type = 'giver'
  AND pe.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id);

  ELSIF NVL(INSTR(p_in_promotionId,','),0) = 0 THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_speaud_node pe
  WHERE  pe.giver_recvr_type = 'receiver'
  AND pe.promotion_id = NVL(p_in_promotionId,-1)
  AND pe.promotion_type = 'product_claim'
  AND pe.node_id IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) )
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id)
  UNION ALL
  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_speaud_team pe
  WHERE  pe.giver_recvr_type = 'receiver'
  AND pe.promotion_id = NVL(p_in_promotionId,-1)
  AND pe.promotion_type = 'product_claim'
  AND pe.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id);

ELSE 

INSERT INTO gtt_recog_elg_count
SELECT eligible_cnt,node_id,'nodesum' record_type FROM (
SELECT SUM(eligible_cnt) eligible_cnt,TO_NUMBER(TRIM(npn.path_node_id)) node_id  
                                       FROM   
                                     (                                     
                                     SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                          eligible_cnt,  
                                                       ppe.node_id  
                                                  FROM (SELECT *  
                                                          FROM rpt_pax_promo_eligibility  
                                                         WHERE giver_recvr_type = 'receiver') ppe,  
                                                       promotion P,                                                        
                                                       rpt_participant_employer rad 
                                                 WHERE ppe.promotion_id = P.promotion_id  
                                                        AND ppe.participant_id = rad.user_id(+)
                                                       AND ( ( p_in_promotionId IS NULL ) OR P.promotion_id IN (
                                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))                                                
                                                       AND P.promotion_status =  
                                                              NVL (p_in_promotionstatus,  
                                                                   P.promotion_status)  
                                               AND ((p_in_departments IS NULL) OR rad.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
                                               AND NVL (rad.position_type, 'JOB') =  
                                                      NVL (  
                                                         p_in_jobposition,  
                                                         NVL (rad.position_type, 'JOB')) 
                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                      NVL (p_in_participantStatus,  
                                                           DECODE(rad.termination_date,NULL,'active','inactive'))  
                                                       AND P.promotion_type = 'product_claim'                                          
                                              GROUP BY ppe.node_id                                     
                                     ) elg, 
                                     (SELECT child_node_id node_id,node_id path_node_id FROM rpt_hierarchy_rollup WHERE NODE_ID IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) ))npn 
                                     WHERE elg.node_id(+) = npn.node_id 
                                     GROUP BY npn.path_node_id)
                                  UNION ALL
                                  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (
                                  SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                          eligible_cnt,  
                                                       ppe.node_id  
                                                  FROM (SELECT *  
                                                          FROM rpt_pax_promo_eligibility  
                                                         WHERE giver_recvr_type = 'receiver' AND node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) ))) ppe,  
                                                       promotion P,                                                        
                                                       rpt_participant_employer rad 
                                                 WHERE ppe.promotion_id = P.promotion_id  
                                                        AND ppe.participant_id = rad.user_id(+)
                                                       AND ( ( p_in_promotionId IS NULL ) OR P.promotion_id IN (
                                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))                                                      
                                                       AND P.promotion_status =  
                                                              NVL (p_in_promotionstatus,  
                                                                   P.promotion_status)  
                                               AND ((p_in_departments IS NULL) OR rad.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
                                               AND NVL (rad.position_type, 'JOB') =  
                                                      NVL (  
                                                         p_in_jobposition,  
                                                         NVL (rad.position_type, 'JOB')) 
                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                      NVL (p_in_participantStatus,  
                                                           DECODE(rad.termination_date,NULL,'active','inactive'))  
                                                       AND P.promotion_type = 'product_claim'                                                  
                                              GROUP BY ppe.node_id) ;     
END IF;    --08/06/2014 End

        OPEN p_out_data FOR
            SELECT NVL (SUM (c_s.act_sub), 0) AS SUBMITTERS,
                   NVL ( (SUM (c_s.elig_sub) - SUM (c_s.act_sub)), 0)
                       AS NON_SUBMITTERS
              FROM (SELECT rh.node_id org_id,
                           rh.node_name org_name,
                           (SELECT eligible_count FROM gtt_recog_elg_count WHERE node_id = rh.node_id) elig_sub,
                           pkg_report_claim.fnc_actual_submitter (
                               p_in_promotionId,
                               p_in_promotionStatus,
                               rh.node_id,
                               'team',
                               p_in_claimStatus,
                               p_in_participantStatus,
                               p_in_jobPosition,
                               p_in_departments,
                               TO_DATE (p_in_fromDate, p_in_localeDatePattern),
                               TO_DATE (p_in_toDate, p_in_localeDatePattern))
                               act_sub
                      FROM rpt_hierarchy rh
                     WHERE rh.node_id IN (SELECT *
                                            FROM TABLE (
                                                     get_array_varchar (
                                                         p_in_parentNodeId)))
                    UNION
                    SELECT rh.node_id org_id,
                           rh.node_name org_name,
                           (SELECT eligible_count FROM gtt_recog_elg_count WHERE node_id = rh.node_id) elig_sub,
                           pkg_report_claim.fnc_actual_submitter (
                               p_in_promotionId,
                               p_in_promotionStatus,
                               rh.node_id,
                               'node',
                               p_in_claimStatus,
                               p_in_participantStatus,
                               p_in_jobPosition,
                               p_in_departments,
                               TO_DATE (p_in_fromDate, p_in_localeDatePattern),
                               TO_DATE (p_in_toDate, p_in_localeDatePattern))
                               act_sub
                      FROM rpt_hierarchy rh
                     WHERE rh.parent_node_id IN (SELECT *
                                                   FROM TABLE (
                                                            get_array_varchar (
                                                                p_in_parentNodeId))))
                   c_s;

        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;
    END prc_getByOrgPartRate;

    PROCEDURE prc_getByOrgPartLevel (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getByOrgPartLevel  ' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
    
    DELETE gtt_recog_elg_count; --08/06/2014 Start
    
    IF fnc_check_promo_aud('giver','product_claim',p_in_promotionId) = 1 THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_allaud_node pe
  WHERE  pe.giver_recvr_type = 'giver'
  AND pe.node_id IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) )
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id)
  UNION ALL
  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_allaud_team pe
  WHERE  pe.giver_recvr_type = 'giver'
  AND pe.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id);

  ELSIF NVL(INSTR(p_in_promotionId,','),0) = 0 THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_speaud_node pe
  WHERE  pe.giver_recvr_type = 'receiver'
  AND pe.promotion_id = NVL(p_in_promotionId,-1)
  AND pe.promotion_type = 'product_claim'
  AND pe.node_id IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) )
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id)
  UNION ALL
  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_speaud_team pe
  WHERE  pe.giver_recvr_type = 'receiver'
  AND pe.promotion_id = NVL(p_in_promotionId,-1)
  AND pe.promotion_type = 'product_claim'
  AND pe.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY pe.node_id);

ELSE 

INSERT INTO gtt_recog_elg_count
SELECT eligible_cnt,node_id,'nodesum' record_type FROM (
SELECT SUM(eligible_cnt) eligible_cnt,TO_NUMBER(TRIM(npn.path_node_id)) node_id  
                                       FROM   
                                     (                                     
                                     SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                          eligible_cnt,  
                                                       ppe.node_id  
                                                  FROM (SELECT *  
                                                          FROM rpt_pax_promo_eligibility  
                                                         WHERE giver_recvr_type = 'receiver') ppe,  
                                                       promotion P,                                                        
                                                       rpt_participant_employer rad 
                                                 WHERE ppe.promotion_id = P.promotion_id  
                                                        AND ppe.participant_id = rad.user_id(+)
                                                       AND ( ( p_in_promotionId IS NULL ) OR P.promotion_id IN (
                                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))                                                
                                                       AND P.promotion_status =  
                                                              NVL (p_in_promotionstatus,  
                                                                   P.promotion_status)  
                                               AND ((p_in_departments IS NULL) OR rad.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
                                               AND NVL (rad.position_type, 'JOB') =  
                                                      NVL (  
                                                         p_in_jobposition,  
                                                         NVL (rad.position_type, 'JOB')) 
                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                      NVL (p_in_participantStatus,  
                                                           DECODE(rad.termination_date,NULL,'active','inactive'))  
                                                       AND P.promotion_type = 'product_claim'                                          
                                              GROUP BY ppe.node_id                                     
                                     ) elg, 
                                     (SELECT child_node_id node_id,node_id path_node_id FROM rpt_hierarchy_rollup WHERE NODE_ID IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) ))npn 
                                     WHERE elg.node_id(+) = npn.node_id 
                                     GROUP BY npn.path_node_id)
                                  UNION ALL
                                  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (
                                  SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                          eligible_cnt,  
                                                       ppe.node_id  
                                                  FROM (SELECT *  
                                                          FROM rpt_pax_promo_eligibility  
                                                         WHERE giver_recvr_type = 'receiver' AND node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) ))) ppe,  
                                                       promotion P,                                                        
                                                       rpt_participant_employer rad 
                                                 WHERE ppe.promotion_id = P.promotion_id  
                                                        AND ppe.participant_id = rad.user_id(+)
                                                       AND ( ( p_in_promotionId IS NULL ) OR P.promotion_id IN (
                                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))                                                      
                                                       AND P.promotion_status =  
                                                              NVL (p_in_promotionstatus,  
                                                                   P.promotion_status)  
                                               AND ((p_in_departments IS NULL) OR rad.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
                                               AND NVL (rad.position_type, 'JOB') =  
                                                      NVL (  
                                                         p_in_jobposition,  
                                                         NVL (rad.position_type, 'JOB')) 
                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                      NVL (p_in_participantStatus,  
                                                           DECODE(rad.termination_date,NULL,'active','inactive'))  
                                                       AND P.promotion_type = 'product_claim'                                                  
                                              GROUP BY ppe.node_id) ;     
END IF;    --08/06/2014 End
        OPEN p_out_data FOR
            SELECT NVL (SUM (c_s.act_sub), 0) AS SUBMITTERS,
                   NVL ( (SUM (c_s.elig_sub) - SUM (c_s.act_sub)), 0)
                       AS NON_SUBMITTERS
              FROM (SELECT rh.node_id org_id,
                           rh.node_name org_name,
                           (SELECT eligible_count FROM gtt_recog_elg_count WHERE node_id = rh.node_id) elig_sub,
                           pkg_report_claim.fnc_actual_submitter (
                               p_in_promotionId,
                               p_in_promotionStatus,
                               rh.node_id,
                               'team',
                               p_in_claimStatus,
                               p_in_participantStatus,
                               p_in_jobPosition,
                               p_in_departments,
                               TO_DATE (p_in_fromDate, p_in_localeDatePattern),
                               TO_DATE (p_in_toDate, p_in_localeDatePattern))
                               act_sub
                      FROM rpt_hierarchy rh
                     WHERE rh.node_id IN (SELECT *
                                            FROM TABLE (
                                                     get_array_varchar (
                                                         p_in_parentNodeId)))
                    UNION
                    SELECT rh.node_id org_id,
                           rh.node_name org_name,
                           (SELECT eligible_count FROM gtt_recog_elg_count WHERE node_id = rh.node_id) elig_sub,
                           pkg_report_claim.fnc_actual_submitter (
                               p_in_promotionId,
                               p_in_promotionStatus,
                               rh.node_id,
                               'node',
                               p_in_claimStatus,
                               p_in_participantStatus,
                               p_in_jobPosition,
                               p_in_departments,
                               TO_DATE (p_in_fromDate, p_in_localeDatePattern),
                               TO_DATE (p_in_toDate, p_in_localeDatePattern))
                               act_sub
                      FROM rpt_hierarchy rh
                     WHERE rh.parent_node_id IN (SELECT *
                                                   FROM TABLE (
                                                            get_array_varchar (
                                                                p_in_parentNodeId))))
                   c_s;

        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;
    END prc_getByOrgPartLevel;

    PROCEDURE prc_getByOrgItemStatus (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getByOrgItemStatus  ' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
              SELECT *
                FROM (SELECT ROW_NUMBER ()
                             OVER (
                                 ORDER BY
                                     (  c_s.item_approv
                                      + c_s.item_deny
                                      + c_s.item_hold) DESC)
                                 RN,
                             c_s.org_name AS ORG_NAME,
                             c_s.item_approv AS ITEMS_APPROVED,
                             c_s.item_deny AS ITEMS_DENIED,
                             c_s.item_hold AS ITEMS_HOLD
                        FROM (  SELECT org_id,
                                       org_name,
                                       SUM (act_sub) act_sub,
                                       SUM (item_approv) item_approv,
                                       SUM (item_deny) item_deny,
                                       SUM (item_hold) item_hold
                                  FROM (SELECT node_id org_id,
                                               node_name || ' Team' org_name,
                                               0 act_sub,
                                               0 item_approv,
                                               0 item_deny,
                                               0 item_hold
                                          FROM rpt_hierarchy
                                         WHERE NVL (node_id, 0) IN (SELECT *
                                                                      FROM TABLE (
                                                                               get_array_varchar (
                                                                                   NVL (
                                                                                       p_in_parentNodeId,
                                                                                       0))))
                                        UNION
                                          SELECT rh.node_id org_id,
                                                 rh.node_name || ' Team' org_name,
                                                 SUM (
                                                     pkg_report_claim.fnc_actual_submitter (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'team',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     act_sub,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'team',
                                                         'approv',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_approv,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'team',
                                                         'deny',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_deny,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'team',
                                                         'hold',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_hold
                                            FROM rpt_claim_summary rcs,
                                                 rpt_hierarchy rh,
                                                 promotion p
                                           WHERE     rcs.detail_node_id = rh.node_id
                                                 AND DECODE (rcs.promotion_id,
                                                             0, p.promotion_id,
                                                             rcs.promotion_id) =
                                                         p.promotion_id
                                                 AND rcs.record_type LIKE '%team%'
                                                 AND p.promotion_status =
                                                         NVL (p_in_promotionStatus,
                                                              p.promotion_status)
                                                 AND ( ( p_in_promotionId IS NULL )
                                                 OR
                                                 p.promotion_id IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                                 AND NVL (rcs.status, 'open') = --10/23/2014 Bug 57533
                                                         NVL (
                                                             p_in_claimStatus,
                                                             NVL (rcs.status, 'open'))--10/23/2014 Bug 57533
                                                 AND NVL (rcs.detail_node_id, 0) IN (SELECT * FROM TABLE (get_array_varchar (
                                                         NVL (p_in_parentNodeId, 0)))) --08/25/2014 Bug 55594        
                                                 AND (   (p_in_participantStatus
                                                              IS NULL)
                                                      OR (    p_in_participantStatus
                                                                  IS NOT NULL
                                                          AND rcs.pax_status =
                                                                  p_in_participantStatus))
                                                 AND (   (p_in_jobPosition IS NULL)
                                                      OR (    p_in_jobPosition
                                                                  IS NOT NULL
                                                          AND rcs.job_position =
                                                                  p_in_jobPosition))
                                                 AND ( ( p_in_departments IS NULL )
                                                 OR
                                                 rcs.department IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                                 AND NVL (TRUNC (rcs.date_submitted),
                                                          TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                       p_in_fromDate,
                                                                                       p_in_localeDatePattern)
                                                                               AND TO_DATE (
                                                                                       p_in_toDate,
                                                                                       p_in_localeDatePattern)
                                        GROUP BY rh.node_id,
                                                 rh.node_name,
                                                 rcs.detail_node_id)
                              GROUP BY org_id, org_name
                              UNION
                                SELECT org_id,
                                       org_name,
                                       SUM (act_sub) act_sub,
                                       SUM (item_approv) item_approv,
                                       SUM (item_deny) item_deny,
                                       SUM (item_hold) item_hold
                                  FROM (SELECT node_id org_id,
                                               node_name org_name,
                                               0 act_sub,
                                               0 item_approv,
                                               0 item_deny,
                                               0 item_hold
                                          FROM rpt_hierarchy
                                         WHERE NVL (parent_node_id, 0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          NVL (
                                                                                              p_in_parentNodeId,
                                                                                              0))))
                                        UNION
                                          SELECT rh.node_id org_id,
                                                 rh.node_name org_name,
                                                 SUM (
                                                     pkg_report_claim.fnc_actual_submitter (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'node',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     act_sub,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'node',
                                                         'approv',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_approv,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'node',
                                                         'deny',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_deny,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'node',
                                                         'hold',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_hold
                                            FROM rpt_claim_summary rcs,
                                                 rpt_hierarchy rh,
                                                 promotion p
                                           WHERE     rcs.detail_node_id = rh.node_id
                                                 AND DECODE (rcs.promotion_id,
                                                             0, p.promotion_id,
                                                             rcs.promotion_id) =
                                                         p.promotion_id
                                                 AND rcs.record_type LIKE '%node%'
                                                 AND p.promotion_status =
                                                         NVL (p_in_promotionStatus,
                                                              p.promotion_status)
                                                 AND ( ( p_in_promotionId IS NULL )
                                                 OR
                                                 p.promotion_id IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                                 AND NVL (rcs.status, 'open') =--10/23/2014 Bug 57533
                                                         NVL (
                                                             p_in_claimStatus,
                                                             NVL (rcs.status, 'open'))--10/23/2014 Bug 57533
                                                 AND NVL (rcs.header_node_id, 0) IN (SELECT * FROM TABLE (get_array_varchar (
                                                         NVL (p_in_parentNodeId, 0)))) --08/25/2014 Bug 55594        
                                                 AND (   (p_in_participantStatus
                                                              IS NULL)
                                                      OR (    p_in_participantStatus
                                                                  IS NOT NULL
                                                          AND rcs.pax_status =
                                                                  p_in_participantStatus))
                                                 AND (   (p_in_jobPosition IS NULL)
                                                      OR (    p_in_jobPosition
                                                                  IS NOT NULL
                                                          AND rcs.job_position =
                                                                  p_in_jobPosition))
                                                 AND ( ( p_in_departments IS NULL )
                                                 OR
                                                 rcs.department IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                                 AND NVL (TRUNC (rcs.date_submitted),
                                                          TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                       p_in_fromDate,
                                                                                       p_in_localeDatePattern)
                                                                               AND TO_DATE (
                                                                                       p_in_toDate,
                                                                                       p_in_localeDatePattern)
                                        GROUP BY rh.node_id,
                                                 rh.node_name,
                                                 rcs.detail_node_id,
                                                 rcs.is_leaf)
                              GROUP BY org_id, org_name) c_s)
               WHERE RN > 0 AND RN < 21
            ORDER BY org_name ASC;

        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;
    END prc_getByOrgItemStatus;

    PROCEDURE prc_getByOrgTotals (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getByOrgTotals  ' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_totals_data FOR
              SELECT *
                FROM (SELECT ROW_NUMBER () OVER (ORDER BY (c_s.total_claim) DESC)
                                 RN,
                             c_s.org_name AS ORG_NAME,
                             CASE
                                 WHEN NVL (c_s.act_sub, 0) = 0 THEN 0
                                 ELSE ROUND (c_s.total_claim / c_s.act_sub, 2)
                             END
                                 AS CLAIMS_PER_SUBMITTER,
                             CASE
                                 WHEN NVL (c_s.act_sub, 0) = 0
                                 THEN
                                     0
                                 ELSE
                                     ROUND (
                                           (  c_s.item_approv
                                            + c_s.item_deny
                                            + c_s.item_hold)
                                         / c_s.act_sub,
                                         2)
                             END
                                 AS ITEMS_PER_SUBMITTER
                        FROM (  SELECT org_id,
                                       org_name,
                                       SUM (act_sub) act_sub,
                                       SUM (total_claim) total_claim,
                                       SUM (item_approv) item_approv,
                                       SUM (item_deny) item_deny,
                                       SUM (item_hold) item_hold
                                  FROM (SELECT node_id org_id,
                                               node_name || ' Team' org_name,
                                               0 act_sub,
                                               0 total_claim,
                                               0 item_approv,
                                               0 item_deny,
                                               0 item_hold
                                          FROM rpt_hierarchy
                                         WHERE NVL (node_id, 0) IN (SELECT *
                                                                      FROM TABLE (
                                                                               get_array_varchar (
                                                                                   NVL (
                                                                                       p_in_parentNodeId,
                                                                                       0))))
                                        UNION
                                          SELECT rh.node_id org_id,
                                                 rh.node_name || ' Team' org_name,
                                                 SUM (
                                                     pkg_report_claim.fnc_actual_submitter (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'team',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     act_sub,
                                                 SUM (NVL (rcs.claim_count, 0))
                                                     total_claim,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'team',
                                                         'approv',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_approv,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'team',
                                                         'deny',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_deny,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'team',
                                                         'hold',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_hold
                                            FROM rpt_claim_summary rcs,
                                                 rpt_hierarchy rh,
                                                 promotion p
                                           WHERE     rcs.detail_node_id = rh.node_id
                                                 AND DECODE (rcs.promotion_id,
                                                             0, p.promotion_id,
                                                             rcs.promotion_id) =
                                                         p.promotion_id
                                                 AND rcs.record_type LIKE '%team%'
                                                 AND p.promotion_status =
                                                         NVL (p_in_promotionStatus,
                                                              p.promotion_status)
                                                 AND ( ( p_in_promotionId IS NULL )
                                                 OR
                                                 p.promotion_id IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                                 AND NVL (rcs.status, 'open') = --10/23/2014 Bug 57533
                                                         NVL (
                                                             p_in_claimStatus,
                                                             NVL (rcs.status, 'open'))--10/23/2014 Bug 57533
                                                 AND NVL (rcs.detail_node_id, 0) IN (SELECT * FROM TABLE (get_array_varchar (
                                                         NVL (p_in_parentNodeId, 0)))) --08/25/2014 Bug 55594        
                                                 AND (   (p_in_participantStatus
                                                              IS NULL)
                                                      OR (    p_in_participantStatus
                                                                  IS NOT NULL
                                                          AND rcs.pax_status =
                                                                  p_in_participantStatus))
                                                 AND (   (p_in_jobPosition IS NULL)
                                                      OR (    p_in_jobPosition
                                                                  IS NOT NULL
                                                          AND rcs.job_position =
                                                                  p_in_jobPosition))
                                                 AND ( ( p_in_departments IS NULL )
                                                 OR
                                                 rcs.department IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                                 AND NVL (TRUNC (rcs.date_submitted),
                                                          TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                       p_in_fromDate,
                                                                                       p_in_localeDatePattern)
                                                                               AND TO_DATE (
                                                                                       p_in_toDate,
                                                                                       p_in_localeDatePattern)
                                        GROUP BY rh.node_id,
                                                 rh.node_name,
                                                 rcs.detail_node_id,
                                                 rcs.is_leaf)
                              GROUP BY org_id, org_name
                              UNION
                                SELECT org_id,
                                       org_name,
                                       SUM (act_sub) act_sub,
                                       SUM (total_claim) total_claim,
                                       SUM (item_approv) item_approv,
                                       SUM (item_deny) item_deny,
                                       SUM (item_hold) item_hold
                                  FROM (SELECT node_id org_id,
                                               node_name org_name,
                                               0 act_sub,
                                               0 total_claim,
                                               0 item_approv,
                                               0 item_deny,
                                               0 item_hold
                                          FROM rpt_hierarchy
                                         WHERE NVL (parent_node_id, 0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          NVL (
                                                                                              p_in_parentNodeId,
                                                                                              0))))
                                        UNION
                                          SELECT rh.node_id org_id,
                                                 rh.node_name org_name,
                                                 SUM (
                                                     pkg_report_claim.fnc_actual_submitter (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'node',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     act_sub,
                                                 SUM (NVL (rcs.claim_count, 0))
                                                     total_claim,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'node',
                                                         'approv',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_approv,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'node',
                                                         'deny',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_deny,
                                                 SUM (
                                                     pkg_report_claim.fnc_item_count (
                                                         p.promotion_id,
                                                         p_in_promotionStatus,
                                                         rcs.detail_node_id,
                                                         'node',
                                                         'hold',
                                                         p_in_claimStatus,
                                                         p_in_participantStatus,
                                                         p_in_jobPosition,
                                                         rcs.department,
                                                         TO_DATE (
                                                             p_in_fromDate,
                                                             p_in_localeDatePattern),
                                                         TO_DATE (
                                                             p_in_toDate,
                                                             p_in_localeDatePattern)))
                                                     item_hold
                                            FROM rpt_claim_summary rcs,
                                                 rpt_hierarchy rh,
                                                 promotion p
                                           WHERE     rcs.detail_node_id = rh.node_id
                                                 AND DECODE (rcs.promotion_id,
                                                             0, p.promotion_id,
                                                             rcs.promotion_id) =
                                                         p.promotion_id
                                                 AND rcs.record_type LIKE '%node%'
                                                 AND p.promotion_status =
                                                         NVL (p_in_promotionStatus,
                                                              p.promotion_status)
                                                 AND ( ( p_in_promotionId IS NULL )
                                                 OR
                                                 p.promotion_id IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                                 AND NVL (rcs.status, 'open') =--10/23/2014 Bug 57533
                                                         NVL (
                                                             p_in_claimStatus,
                                                             NVL (rcs.status, 'open'))--10/23/2014 Bug 57533
                                                 AND NVL (rcs.header_node_id, 0) IN (SELECT * FROM TABLE (get_array_varchar (
                                                         NVL (p_in_parentNodeId, 0)))) --08/25/2014 Bug 55594        
                                                 AND (   (p_in_participantStatus
                                                              IS NULL)
                                                      OR (    p_in_participantStatus
                                                                  IS NOT NULL
                                                          AND rcs.pax_status =
                                                                  p_in_participantStatus))
                                                 AND (   (p_in_jobPosition IS NULL)
                                                      OR (    p_in_jobPosition
                                                                  IS NOT NULL
                                                          AND rcs.job_position =
                                                                  p_in_jobPosition))
                                                 AND ( ( p_in_departments IS NULL )
                                                 OR
                                                 rcs.department IN (
                                                      SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                                 AND NVL (TRUNC (rcs.date_submitted),
                                                          TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                       p_in_fromDate,
                                                                                       p_in_localeDatePattern)
                                                                               AND TO_DATE (
                                                                                       p_in_toDate,
                                                                                       p_in_localeDatePattern)
                                        GROUP BY rh.node_id,
                                                 rh.node_name,
                                                 rcs.detail_node_id,
                                                 rcs.is_leaf)
                              GROUP BY org_id, org_name) c_s)
               WHERE RN > 0 AND RN < 21
            ORDER BY org_name ASC;

        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;
    END prc_getByOrgTotals;

    PROCEDURE prc_getByPaxTabRes (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)
    IS
    
    /******************************************************************************
  NAME:       prc_getByPaxTabRes
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)              \
  krishnaDeepika        10/23/2014            Bug # 57535 fix - Reports - Claims Activity - Participation by Organization - Totals are displayed wrongly after done sweepstake.
  Suresh J               04/24/2015           Bug # 61851 - Fixed ORDER BY issue that was causing sort issue                     
  ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getByPaxTabRes' ;
  v_stage        VARCHAR2 (500);
  v_sortCol             VARCHAR2(200);
  l_query VARCHAR2(32767);   
  
    BEGIN
     l_query  := 'SELECT * FROM
  ( ';
    
    v_sortCol := ' '|| p_in_sortedOn || ' ' || p_in_sortedBy;
       
  l_query := l_query ||'  '||'SELECT ROWNUM RN,
                           submitter_id,
                           submitter,
                           fnc_cms_asset_code_val_extr (cm_asset_code,
                                                        name_cm_key,
                                                        '''||p_in_languageCode||''')
                               AS country,
                           org_name,
                           fnc_cms_picklist_value (
                               ''picklist.department.type.items'',
                               department,
                               '''||p_in_languageCode||''')
                               AS DEPARTMENT,
                           fnc_cms_picklist_value (
                               ''picklist.positiontype.items'',
                               job_position,
                               '''||p_in_languageCode||''')
                               AS JOB_POSITION,
                           promotion,
                           total_claims,
                           open_claims,
                           closed_claims,
                           pkg_report_claim.fnc_submitter_item_count (
                                         promotion_id,
                                         '''||p_in_promotionStatus||''',
                                         submitter_id,
                                         ''approv'',
                                         '''||p_in_claimStatus||''',
                                         '''||p_in_participantStatus||''',
                                         '''||p_in_jobPosition||''',
                                         '''||p_in_departments||''',
                                         TO_DATE ('''||p_in_fromDate||''',
                                                  '''||p_in_localeDatePattern||'''),
                                         TO_DATE ('''||p_in_toDate||''',
                                                  '''||p_in_localeDatePattern||'''))
                                         AS APPROVED_ITEMS,
                                     pkg_report_claim.fnc_submitter_item_count (
                                         promotion_id,
                                         '''||p_in_promotionStatus||''',
                                         submitter_id,
                                         ''deny'',
                                         '''||p_in_claimStatus||''',
                                         '''||p_in_participantStatus||''',
                                         '''||p_in_jobPosition||''',
                                         '''||p_in_departments||''',
                                         TO_DATE ('''||p_in_fromDate||''',
                                                  '''||p_in_localeDatePattern||'''),
                                         TO_DATE ('''||p_in_toDate||''',
                                                  '''||p_in_localeDatePattern||'''))
                                         AS DENIED_ITEMS,
                                     pkg_report_claim.fnc_submitter_item_count (
                                         promotion_id,
                                         '''||p_in_promotionStatus||''',
                                         submitter_id,
                                         ''hold'',
                                         '''||p_in_claimStatus||''',
                                         '''||p_in_participantStatus||''',
                                         '''||p_in_jobPosition||''',
                                         '''||p_in_departments||''',
                                         TO_DATE ('''||p_in_fromDate||''',
                                                  '''||p_in_localeDatePattern||'''),
                                         TO_DATE ('''||p_in_toDate||''',
                                                  '''||p_in_localeDatePattern||'''))
                                         AS HELD_ITEMS,
                           points,
                           sweepstakes_won,
                           promotion_id
                      FROM (  SELECT rcd.submitter_user_id AS SUBMITTER_ID,
                                     rcd.submitter_name AS SUBMITTER,
                                     c.cm_asset_code,
                                     c.name_cm_key,
                                     rcd.node_name AS ORG_NAME,
                                     rcd.submitter_department AS DEPARTMENT,
                                     rcd.submitter_job_position AS JOB_POSITION,
                                     p.promotion_name AS PROMOTION,
                                     COUNT (rcd.claim_id) AS TOTAL_CLAIMS,
                                     SUM (
                                         DECODE (rcd.claim_status, ''open'', 1, 0))
                                         AS OPEN_CLAIMS,
                                     SUM (
                                         DECODE (rcd.claim_status,
                                                 ''closed'', 1,
                                                 0))
                                         AS CLOSED_CLAIMS,                 
                                     SUM (NVL (rcd.award_amount, 0)) AS POINTS,
                                     SUM (NVL (rcd.submitter_sweepstakes_won, 0))
                                         AS SWEEPSTAKES_WON,
                                     p.promotion_id AS PROMOTION_ID
                                FROM rpt_claim_detail rcd, promotion p, country c
                               WHERE     rcd.promotion_id = p.promotion_id
                                     AND rcd.submitter_country_id = c.country_id
                                     AND p.promotion_status =
                                             NVL ('''||p_in_promotionStatus||''',
                                                  p.promotion_status)
                                     AND (   ('''||p_in_promotionId||''' IS NULL)
                                          OR rcd.promotion_id IN (SELECT *
                                                                    FROM TABLE (
                                                                             get_array (
                                                                                 '''||p_in_promotionId||'''))))
                                     AND NVL (rcd.claim_status, ''open'') = --10/23/2014 Bug 57533
                                             NVL ('''||p_in_claimStatus||''',
                                                  NVL (rcd.claim_status, ''open''))--10/23/2014 Bug 57533
                                      AND (   (    '''||p_in_includeChildNodes||''' = ''true''
                                              AND rcd.node_id IN (SELECT child_node_id
                                                                    FROM rpt_hierarchy_rollup
                                                                   WHERE node_id IN (SELECT *
                                                                                       FROM TABLE (
                                                                                                get_array_varchar (
                                                                                                    '''||p_in_parentNodeId||''')))))
                                     OR (    '''||p_in_includeChildNodes||''' =
                                                      ''false''
                                              AND rcd.node_id IN (SELECT * FROM TABLE (get_array_varchar (
                                                      NVL ('''||p_in_parentNodeId||''',rcd.node_id)))) --08/25/2014 Bug 55594        
                                                      ))    --10/23/2014 Bug # 57535 fix  
                                     AND rcd.submitter_pax_status =
                                             NVL ('''||p_in_participantStatus||''',
                                                  rcd.submitter_pax_status)
                                     AND rcd.submitter_job_position =
                                             NVL ('''||p_in_jobPosition||''',
                                                  rcd.submitter_job_position)
                                     AND ( ( '''||p_in_departments||''' IS NULL )
                                     OR
                                     rcd.submitter_department IN (
                                          SELECT * FROM TABLE ( get_array_varchar('''||p_in_departments||'''))))
                                     AND ( ( '''||p_in_countryIds||''' IS NULL )
                                     OR
                                     rcd.submitter_country_id IN (
                                          SELECT * FROM TABLE ( get_array_varchar('''||p_in_countryIds||'''))))
                                     AND NVL (TRUNC (rcd.date_submitted),
                                              TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                           '''||p_in_fromDate||''',
                                                                           '''||p_in_localeDatePattern||''')
                                                                   AND TO_DATE (
                                                                           '''||p_in_toDate||''',
                                                                           '''||p_in_localeDatePattern||''')
                            GROUP BY rcd.submitter_user_id,
                                     rcd.submitter_name,
                                     c.cm_asset_code,
                                     c.name_cm_key,
                                     rcd.node_name,
                                     rcd.submitter_job_position,
                                     rcd.submitter_department,
                                     p.promotion_name,
                                     p.promotion_id,
                                     rcd.submitter_user_id
    ) 
                           ORDER BY '|| v_sortCol ||'         --Bug #61851 04/24/2015
    ) WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd;

  OPEN p_out_data FOR 
   l_query;

        SELECT COUNT (1) INTO p_out_size_data
              FROM (SELECT submitter_id,
                           submitter,
                           fnc_cms_asset_code_val_extr (cm_asset_code,
                                                        name_cm_key,
                                                        p_in_languageCode)
                               AS country,
                           org_name,
                           fnc_cms_picklist_value (
                               'picklist.department.type.items',
                               department,
                               p_in_languageCode)
                               AS DEPARTMENT,
                           fnc_cms_picklist_value (
                               'picklist.positiontype.items',
                               job_position,
                               p_in_languageCode)
                               AS JOB_POSITION,
                           promotion,
                           total_claims,
                           open_claims,
                           closed_claims,
                           approved_items,
                           denied_items,
                           held_items,
                           points,
                           sweepstakes_won
                      FROM (  SELECT rcd.submitter_user_id AS SUBMITTER_ID,
                                     rcd.submitter_name AS SUBMITTER,
                                     c.cm_asset_code,
                                     c.name_cm_key,
                                     rcd.node_name AS ORG_NAME,
                                     rcd.submitter_department AS DEPARTMENT,
                                     rcd.submitter_job_position AS JOB_POSITION,
                                     p.promotion_name AS PROMOTION,
                                     COUNT (rcd.claim_id) AS TOTAL_CLAIMS,
                                     SUM (
                                         DECODE (rcd.claim_status, 'open', 1, 0))
                                         AS OPEN_CLAIMS,
                                     SUM (
                                         DECODE (rcd.claim_status,
                                                 'closed', 1,
                                                 0))
                                         AS CLOSED_CLAIMS,
                                     pkg_report_claim.fnc_submitter_item_count (
                                         p_in_promotionId,
                                         p_in_promotionStatus,
                                         rcd.submitter_user_id,
                                         'approv',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         AS APPROVED_ITEMS,
                                     pkg_report_claim.fnc_submitter_item_count (
                                         p_in_promotionId,
                                         p_in_promotionStatus,
                                         rcd.submitter_user_id,
                                         'deny',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         AS DENIED_ITEMS,
                                     pkg_report_claim.fnc_submitter_item_count (
                                         p_in_promotionId,
                                         p_in_promotionStatus,
                                         rcd.submitter_user_id,
                                         'hold',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         AS HELD_ITEMS,
                                     SUM (NVL (rcd.award_amount, 0)) AS POINTS,
                                     SUM (NVL (rcd.submitter_sweepstakes_won, 0))
                                         AS SWEEPSTAKES_WON
                                FROM rpt_claim_detail rcd, promotion p, country c
                               WHERE     rcd.promotion_id = p.promotion_id
                                     AND rcd.submitter_country_id = c.country_id
                                     AND p.promotion_status =
                                             NVL (p_in_promotionStatus,
                                                  p.promotion_status)
                                     AND ( ( p_in_promotionId IS NULL )
                                     OR
                                     rcd.promotion_id IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                     AND NVL (rcd.claim_status, 'open') =--10/23/2014 Bug 57533	
                                             NVL (p_in_claimStatus,
                                                  NVL (rcd.claim_status, 'open'))--10/23/2014 Bug 57533
                                     AND (   (    p_in_includeChildNodes = 'true'
                                              AND rcd.node_id IN (SELECT child_node_id
                                                                    FROM rpt_hierarchy_rollup
                                                                   WHERE node_id IN (SELECT *
                                                                                       FROM TABLE (
                                                                                                get_array_varchar (
                                                                                                    p_in_parentNodeId)))))
                                          OR (    p_in_includeChildNodes =
                                                      'false'
                                              AND rcd.node_id IN (SELECT * FROM TABLE (get_array_varchar (
                                                      NVL (p_in_parentNodeId,rcd.node_id)))) --08/25/2014 Bug 55594        
                                                      ))
                                     AND rcd.submitter_pax_status =
                                             NVL (p_in_participantStatus,
                                                  rcd.submitter_pax_status)
                                     AND rcd.submitter_job_position =
                                             NVL (p_in_jobPosition,
                                                  rcd.submitter_job_position)
                                     AND ( ( p_in_departments IS NULL )
                                     OR
                                     rcd.submitter_department IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                     AND ( ( p_in_countryIds IS NULL )
                                     OR
                                     rcd.submitter_country_id IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_countryIds))))
                                     AND NVL (TRUNC (rcd.date_submitted),
                                              TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                           p_in_fromDate,
                                                                           p_in_localeDatePattern)
                                                                   AND TO_DATE (
                                                                           p_in_toDate,
                                                                           p_in_localeDatePattern)
                            GROUP BY rcd.submitter_user_id,
                                     rcd.submitter_name,
                                     c.cm_asset_code,
                                     c.name_cm_key,
                                     rcd.node_name,
                                     rcd.submitter_job_position,
                                     rcd.submitter_department,
                                     p.promotion_name,
                                     rcd.submitter_user_id));
        OPEN p_out_totals_data FOR
            SELECT NVL (SUM (total_claims), 0) total_claims,
                   NVL (SUM (open_claims), 0) open_claims,
                   NVL (SUM (closed_claims), 0) closed_claims,
                   NVL (SUM (approved_items), 0) approved_items,
                   NVL (SUM (denied_items), 0) denied_items,
                   NVL (SUM (held_items), 0) held_items,
                   NVL (SUM (points), 0) points,
                   NVL (SUM (sweepstakes_won), 0) sweepstakes_won
              FROM (SELECT submitter_id,
                           submitter,
                           fnc_cms_asset_code_val_extr (cm_asset_code,
                                                        name_cm_key,
                                                        p_in_languageCode)
                               AS country,
                           org_name,
                           fnc_cms_picklist_value (
                               'picklist.department.type.items',
                               department,
                               p_in_languageCode)
                               AS DEPARTMENT,
                           fnc_cms_picklist_value (
                               'picklist.positiontype.items',
                               job_position,
                               p_in_languageCode)
                               AS JOB_POSITION,
                           promotion_id promotion,
                           total_claims,
                           open_claims,
                           closed_claims,
                           pkg_report_claim.fnc_submitter_item_count (
                                         promotion_id,
                                         p_in_promotionStatus,
                                         submitter_id,
                                         'approv',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         AS APPROVED_ITEMS,
                                     pkg_report_claim.fnc_submitter_item_count (
                                         promotion_id,
                                         p_in_promotionStatus,
                                         submitter_id,
                                         'deny',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         AS DENIED_ITEMS,
                                     pkg_report_claim.fnc_submitter_item_count (
                                         promotion_id,
                                         p_in_promotionStatus,
                                         submitter_id,
                                         'hold',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         AS HELD_ITEMS,
                           points,
                           sweepstakes_won
                      FROM (  SELECT rcd.submitter_user_id AS SUBMITTER_ID,
                                     rcd.submitter_name AS SUBMITTER,
                                     c.cm_asset_code,
                                     c.name_cm_key,
                                     rcd.node_name AS ORG_NAME,
                                     rcd.submitter_department AS DEPARTMENT,
                                     rcd.submitter_job_position AS JOB_POSITION,
--                                     p.promotion_name AS PROMOTION,
                                     p.promotion_id,
                                     COUNT (rcd.claim_id) AS TOTAL_CLAIMS,
                                     SUM (
                                         DECODE (rcd.claim_status, 'open', 1, 0))
                                         AS OPEN_CLAIMS,
                                     SUM (
                                         DECODE (rcd.claim_status,
                                                 'closed', 1,
                                                 0))
                                         AS CLOSED_CLAIMS,                                     
                                     SUM (NVL (rcd.award_amount, 0)) AS POINTS,
                                     SUM (NVL (rcd.submitter_sweepstakes_won, 0))
                                         AS SWEEPSTAKES_WON
                                FROM rpt_claim_detail rcd, promotion p, country c
                               WHERE     rcd.promotion_id = p.promotion_id
                                     AND rcd.submitter_country_id = c.country_id
                                     AND p.promotion_status =
                                             NVL (p_in_promotionStatus,
                                                  p.promotion_status)
                                     AND ( ( p_in_promotionId IS NULL )
                                     OR
                                     rcd.promotion_id IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                     AND NVL (rcd.claim_status, 'open') = --10/23/2014 Bug 57533
                                             NVL (p_in_claimStatus,
                                                  NVL (rcd.claim_status, 'open'))--10/23/2014 Bug 57533
                                     AND (   (    p_in_includeChildNodes = 'true'
                                              AND rcd.node_id IN (SELECT child_node_id
                                                                    FROM rpt_hierarchy_rollup
                                                                   WHERE node_id IN (SELECT * FROM TABLE (get_array_varchar(p_in_parentNodeId)))
                                                                  )   --08/25/2014 Bug 55594        
                                                )
                                          OR (    p_in_includeChildNodes =
                                                      'false'
                                              AND rcd.node_id IN (SELECT * FROM TABLE 
                                                                    (get_array_varchar(NVL (p_in_parentNodeId,rcd.node_id))))--08/25/2014 Bug 55594
                                             ))
                                     AND rcd.submitter_pax_status =
                                             NVL (p_in_participantStatus,
                                                  rcd.submitter_pax_status)
                                     AND rcd.submitter_job_position =
                                             NVL (p_in_jobPosition,
                                                  rcd.submitter_job_position)
                                     AND ( ( p_in_departments IS NULL )
                                     OR
                                     rcd.submitter_department IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                     AND ( ( p_in_countryIds IS NULL )
                                     OR
                                     rcd.submitter_country_id IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_countryIds))))
                                     AND NVL (TRUNC (rcd.date_submitted),
                                              TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                           p_in_fromDate,
                                                                           p_in_localeDatePattern)
                                                                   AND TO_DATE (
                                                                           p_in_toDate,
                                                                           p_in_localeDatePattern)
                            GROUP BY rcd.submitter_user_id,
                                     rcd.submitter_name,
                                     c.cm_asset_code,
                                     c.name_cm_key,
                                     rcd.node_name,
                                     rcd.submitter_job_position,
                                     rcd.submitter_department,
                                     p.promotion_id,
                                     rcd.submitter_user_id) rs);


        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;
            p_out_size_data := NULL;
    END prc_getByPaxTabRes;


    PROCEDURE prc_getByPaxClaimListTabRes (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)

    IS
    
/******************************************************************************
  NAME:       prc_getByPaxClaimListTabRes
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)                                 
  ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getByPaxClaimListTabRes' ;
  v_stage        VARCHAR2 (500);
  v_sortCol             VARCHAR2(200);
  l_query VARCHAR2(32767);  
   
    BEGIN
    
     l_query  := 'SELECT * FROM
  ( ';
    
    v_sortCol := ' '|| p_in_sortedOn || ' ' || p_in_sortedBy;
       
  l_query := l_query ||'  '||'SELECT ROW_NUMBER ()
                           OVER (
                               ORDER BY
                                   '|| v_sortCol ||' )
                               RN,
                           rcd.claim_number AS CLAIM,
                           fnc_cms_picklist_value (
                               ''picklist.claim.status.type.items'',
                               rcd.claim_status,
                               '''||p_in_languageCode||''')
                               AS CLAIM_STATUS,
                           rcd.date_submitted AS DATE_SUBMITTED,
                           rcd.node_name AS ORG_NAME,
                           rcd.promotion_name AS PROMOTION,
                           NVL (rcd.award_amount, 0) AS POINTS,
                           NVL (rcd.submitter_sweepstakes_won, 0)
                               AS SWEEPSTAKES_WON,
                           rcd.submitter_user_id AS SUBMITTER_USER_ID,
                           rcd.claim_id AS CLAIM_ID
                      FROM rpt_claim_detail rcd, promotion p
                     WHERE     rcd.promotion_id = p.promotion_id
                           AND p.promotion_status =
                                   NVL ('''||p_in_promotionStatus||''',
                                        p.promotion_status)
                           AND ( ( '''||p_in_promotionId||''' IS NULL )
                           OR
                           rcd.promotion_id IN (
                                SELECT * FROM TABLE ( get_array_varchar('''||p_in_promotionId||'''))))
                           AND NVL (rcd.claim_status, ''open'') =--10/23/2014 Bug 57533
                                   NVL ('''||p_in_claimStatus||''',
                                        NVL (rcd.claim_status, ''open''))--10/23/2014 Bug 57533
                           AND rcd.node_id IN (SELECT child_node_id
                                                 FROM rpt_hierarchy_rollup
                                                WHERE node_id =
                                                          '''||p_in_parentNodeId||''')
                           AND rcd.submitter_pax_status =
                                   NVL ('''||p_in_participantStatus||''',
                                        rcd.submitter_pax_status)
                           AND rcd.submitter_job_position =
                                   NVL ('''||p_in_jobPosition||''',
                                        rcd.submitter_job_position)
                           AND ( ( '''||p_in_departments||''' IS NULL )
                           OR
                           rcd.submitter_department IN (
                                SELECT * FROM TABLE ( get_array_varchar('''||p_in_departments||'''))))
                           AND ( ( '''||p_in_countryIds||''' IS NULL )
                           OR
                           rcd.submitter_country_id IN (
                                SELECT * FROM TABLE ( get_array_varchar('''||p_in_countryIds||'''))))
                           AND rcd.submitter_user_id =
                                   NVL ('''||p_in_paxId||''', rcd.submitter_user_id)
                           AND NVL (TRUNC (rcd.date_submitted),
                                    TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                 '''||p_in_fromDate||''',
                                                                 '''||p_in_localeDatePattern||''')
                                                         AND TO_DATE (
                                                                 '''||p_in_toDate||''',
                                                                 '''||p_in_localeDatePattern||'''))
             WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd;
             
                           OPEN p_out_data FOR 
   l_query;

        SELECT COUNT (1) INTO p_out_size_data
              FROM rpt_claim_detail rcd, promotion p
             WHERE     rcd.promotion_id = p.promotion_id
                   AND p.promotion_status =
                           NVL (p_in_promotionStatus, p.promotion_status)
                   AND ( ( p_in_promotionId IS NULL )
                   OR
                   rcd.promotion_id IN (
                        SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                   AND NVL (rcd.claim_status, 'open') =--10/23/2014 Bug 57533
                           NVL (p_in_claimStatus,
                                NVL (rcd.claim_status, 'open'))--10/23/2014 Bug 57533
                   AND rcd.node_id IN (SELECT child_node_id
                                         FROM rpt_hierarchy_rollup
                                        WHERE node_id IN (SELECT * FROM TABLE (get_array_varchar(p_in_parentNodeId))))--08/25/2014 Bug 55594        
                   AND rcd.submitter_pax_status =
                           NVL (p_in_participantStatus,
                                rcd.submitter_pax_status)
                   AND rcd.submitter_job_position =
                           NVL (p_in_jobPosition, rcd.submitter_job_position)
                   AND ( ( p_in_departments IS NULL )
                   OR
                   rcd.submitter_department IN (
                        SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                   AND ( ( p_in_countryIds IS NULL )
                   OR
                   rcd.submitter_country_id IN (
                        SELECT * FROM TABLE ( get_array_varchar(p_in_countryIds))))
                   AND rcd.submitter_user_id =
                           NVL (p_in_paxId, rcd.submitter_user_id)
                   AND NVL (TRUNC (rcd.date_submitted), TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                     p_in_fromDate,
                                                                                     p_in_localeDatePattern)
                                                                             AND TO_DATE (
                                                                                     p_in_toDate,
                                                                                     p_in_localeDatePattern);
        OPEN p_out_totals_data FOR
            SELECT NVL (SUM (rcd.award_amount), 0) AS POINTS,
                   NVL (SUM (rcd.submitter_sweepstakes_won), 0)
                       AS SWEEPSTAKES_WON
              FROM rpt_claim_detail rcd, promotion p
             WHERE     rcd.promotion_id = p.promotion_id
                   AND p.promotion_status =
                           NVL (p_in_promotionStatus, p.promotion_status)
                   AND ( ( p_in_promotionId IS NULL )
                   OR
                   rcd.promotion_id IN (
                        SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                   AND NVL (rcd.claim_status, 'open') =--10/23/2014 Bug 57533
                           NVL (p_in_claimStatus,
                                NVL (rcd.claim_status, 'open'))--10/23/2014 Bug 57533
                   AND rcd.node_id IN (SELECT child_node_id
                                         FROM rpt_hierarchy_rollup
                                        WHERE node_id IN  (SELECT * FROM TABLE (get_array_varchar(p_in_parentNodeId))))--08/25/2014 Bug 55594        
                   AND rcd.submitter_pax_status =
                           NVL (p_in_participantStatus,
                                rcd.submitter_pax_status)
                   AND rcd.submitter_job_position =
                           NVL (p_in_jobPosition, rcd.submitter_job_position)
                   AND ( ( p_in_departments IS NULL )
                   OR
                   rcd.submitter_department IN (
                        SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                   AND ( ( p_in_countryIds IS NULL )
                   OR
                   rcd.submitter_country_id IN (
                        SELECT * FROM TABLE ( get_array_varchar(p_in_countryIds))))
                   AND rcd.submitter_user_id =
                           NVL (p_in_paxId, rcd.submitter_user_id)
                   AND NVL (TRUNC (rcd.date_submitted), TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                     p_in_fromDate,
                                                                                     p_in_localeDatePattern)
                                                                             AND TO_DATE (
                                                                                     p_in_toDate,
                                                                                     p_in_localeDatePattern);


        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;
            p_out_size_data := NULL;
    END prc_getByPaxClaimListTabRes;

    PROCEDURE prc_getByPaxSubmittedClaims (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_average_data          OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getByPaxSubmittedClaims   ' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
              SELECT *
                FROM (SELECT ROW_NUMBER () OVER (ORDER BY total_claims DESC) RN,
                             submitter,
                             total_claims
                        FROM (  SELECT rcd.submitter_name AS SUBMITTER,
                                       COUNT (rcd.claim_id) AS TOTAL_CLAIMS
                                  FROM rpt_claim_detail rcd, promotion p
                                 WHERE     rcd.promotion_id = p.promotion_id
                                       AND p.promotion_status =
                                               NVL (p_in_promotionStatus,
                                                    p.promotion_status)
                                       AND ( ( p_in_promotionId IS NULL )
                                       OR
                                       rcd.promotion_id IN (
                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                       AND NVL (rcd.claim_status, 'open') =--10/23/2014 Bug 57533
                                               NVL (p_in_claimStatus,
                                                    NVL (rcd.claim_status, 'open'))--10/23/2014 Bug 57533
                                       AND rcd.node_id IN (SELECT child_node_id
                                                             FROM rpt_hierarchy_rollup
                                                            WHERE node_id IN
                                                                      (SELECT * FROM TABLE (get_array_varchar(p_in_parentNodeId))))--08/25/2014 Bug 55594        
                                       AND rcd.submitter_pax_status =
                                               NVL (p_in_participantStatus,
                                                    rcd.submitter_pax_status)
                                       AND rcd.submitter_job_position =
                                               NVL (p_in_jobPosition,
                                                    rcd.submitter_job_position)
                                       AND ( ( p_in_departments IS NULL )
                                       OR
                                       rcd.submitter_department IN (
                                            SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                       AND ( ( p_in_countryIds IS NULL )
                                       OR
                                       rcd.submitter_country_id IN (
                                            SELECT * FROM TABLE ( get_array_varchar(p_in_countryIds))))
                                       AND NVL (TRUNC (rcd.date_submitted),
                                                TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                             p_in_fromDate,
                                                                             p_in_localeDatePattern)
                                                                     AND TO_DATE (
                                                                             p_in_toDate,
                                                                             p_in_localeDatePattern)
                              GROUP BY rcd.submitter_user_id,
                                       rcd.submitter_name,
                                       rcd.submitter_country_id,
                                       rcd.node_name,
                                       rcd.submitter_department,
                                       rcd.submitter_job_position,
                                       p.promotion_name))
               WHERE RN > 0 AND RN < 21
            ORDER BY submitter ASC;

        OPEN p_out_average_data FOR
            SELECT ROUND (
                       DECODE (COUNT (1), 0, 0, SUM (total_claims) / COUNT (1)),
                       2) AS AVERAGE_SUBMITTED
              FROM (  SELECT rcd.submitter_user_id AS SUBMITTER_ID,
                             rcd.submitter_name AS SUBMITTER,
                             fnc_cms_asset_code_val_extr (c.cm_asset_code,
                                                          c.name_cm_key,
                                                          p_in_languageCode)
                                 AS COUNTRY,
                             rcd.node_name AS ORG_NAME,
                             rcd.submitter_department AS DEPARTMENT,
                             rcd.submitter_job_position AS JOB_POSITION,
                             p.promotion_name AS PROMOTION,
                             COUNT (rcd.claim_id) AS TOTAL_CLAIMS
                        FROM rpt_claim_detail rcd, promotion p, country c
                       WHERE     rcd.promotion_id = p.promotion_id
                             AND rcd.submitter_country_id = c.country_id
                             AND p.promotion_status =
                                     NVL (p_in_promotionStatus,
                                          p.promotion_status)
                             AND ( ( p_in_promotionId IS NULL )
                             OR
                             rcd.promotion_id IN (
                                  SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                             AND NVL (rcd.claim_status, 'open') =--10/23/2014 Bug 57533
                                     NVL (p_in_claimStatus,
                                          NVL (rcd.claim_status, 'open'))--10/23/2014 Bug 57533
                             AND rcd.node_id IN (SELECT child_node_id
                                                   FROM rpt_hierarchy_rollup
                                                  WHERE node_id IN(SELECT * FROM TABLE (get_array_varchar(
                                                            p_in_parentNodeId))))--08/25/2014 Bug 55594        
                             AND rcd.submitter_pax_status =
                                     NVL (p_in_participantStatus,
                                          rcd.submitter_pax_status)
                             AND rcd.submitter_job_position =
                                     NVL (p_in_jobPosition,
                                          rcd.submitter_job_position)
                             AND ( ( p_in_departments IS NULL )
                             OR
                             rcd.submitter_department IN (
                                  SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                             AND ( ( p_in_countryIds IS NULL )
                             OR
                             rcd.submitter_country_id IN (
                                  SELECT * FROM TABLE ( get_array_varchar(p_in_countryIds))))
                             AND NVL (TRUNC (rcd.date_submitted),
                                      TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                   p_in_fromDate,
                                                                   p_in_localeDatePattern)
                                                           AND TO_DATE (
                                                                   p_in_toDate,
                                                                   p_in_localeDatePattern)
                    GROUP BY rcd.submitter_user_id,
                             rcd.submitter_name,
                             fnc_cms_asset_code_val_extr (c.cm_asset_code,
                                                          c.name_cm_key,
                                                          p_in_languageCode),
                             rcd.node_name,
                             rcd.submitter_department,
                             rcd.submitter_job_position,
                             p.promotion_name);

        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;
    END prc_getByPaxSubmittedClaims;


    PROCEDURE prc_getByPaxClaimStatus (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getByPaxClaimStatus  ' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT *
              FROM (SELECT ROW_NUMBER ()
                               OVER (ORDER BY open_claims + closed_claims DESC)
                               RN,
                           submitter_id,
                           submitter,
                           open_claims,
                           closed_claims
                      FROM (  SELECT rcd.submitter_user_id AS SUBMITTER_ID,
                                     rcd.submitter_name AS SUBMITTER,
                                     SUM (
                                         DECODE (rcd.claim_status, 'open', 1, 0))
                                         AS OPEN_CLAIMS,
                                     SUM (
                                         DECODE (rcd.claim_status,
                                                 'closed', 1,
                                                 0))
                                         AS CLOSED_CLAIMS
                                FROM rpt_claim_detail rcd, promotion p
                               WHERE     rcd.promotion_id = p.promotion_id
                                     AND p.promotion_status =
                                             NVL (p_in_promotionStatus,
                                                  p.promotion_status)
                                     AND ( ( p_in_promotionId IS NULL )
                                     OR
                                     rcd.promotion_id IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                     AND NVL (rcd.claim_status, 'open') =--10/23/2014 Bug 57533
                                             NVL (p_in_claimStatus,
                                                  NVL (rcd.claim_status, 'open'))--10/23/2014 Bug 57533
                                     AND rcd.node_id IN (SELECT child_node_id
                                                           FROM rpt_hierarchy_rollup
                                                          WHERE node_id IN (SELECT * FROM TABLE (get_array_varchar( 
                                                                    p_in_parentNodeId))))--08/25/2014 Bug 55594        
                                     AND rcd.submitter_pax_status =
                                             NVL (p_in_participantStatus,
                                                  rcd.submitter_pax_status)
                                     AND rcd.submitter_job_position =
                                             NVL (p_in_jobPosition,
                                                  rcd.submitter_job_position)
                                     AND ( ( p_in_departments IS NULL )
                                     OR
                                     rcd.submitter_department IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                     AND ( ( p_in_countryIds IS NULL )
                                     OR
                                     rcd.submitter_country_id IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_countryIds))))
                                     AND NVL (TRUNC (rcd.date_submitted),
                                              TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                           p_in_fromDate,
                                                                           p_in_localeDatePattern)
                                                                   AND TO_DATE (
                                                                           p_in_toDate,
                                                                           p_in_localeDatePattern)
                            GROUP BY rcd.submitter_user_id, rcd.submitter_name
                            ORDER BY (open_claims + closed_claims)) rs)
             WHERE RN > 0 AND RN < 21;

        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;
    END prc_getByPaxClaimStatus;


    PROCEDURE prc_getByPaxItemStatus (
        p_in_paxId               IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_claimStatus         IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_languageCode        IN     VARCHAR,
        p_in_includeChildNodes   IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getByPaxItemStatus  ' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT *
              FROM (SELECT ROW_NUMBER ()
                           OVER (
                               ORDER BY
                                   approved_items + denied_items + held_items DESC)
                               RN,
                           submitter_id,
                           submitter,
                           approved_items,
                           denied_items,
                           held_items
                      FROM (  SELECT rcd.submitter_user_id AS SUBMITTER_ID,
                                     rcd.submitter_name AS SUBMITTER,
                                     pkg_report_claim.fnc_submitter_item_count (
                                         p_in_promotionId,
                                         p_in_promotionStatus,
                                         rcd.submitter_user_id,
                                         'approv',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         AS APPROVED_ITEMS,
                                     pkg_report_claim.fnc_submitter_item_count (
                                         p_in_promotionId,
                                         p_in_promotionStatus,
                                         rcd.submitter_user_id,
                                         'deny',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         AS DENIED_ITEMS,
                                     pkg_report_claim.fnc_submitter_item_count (
                                         p_in_promotionId,
                                         p_in_promotionStatus,
                                         rcd.submitter_user_id,
                                         'hold',
                                         p_in_claimStatus,
                                         p_in_participantStatus,
                                         p_in_jobPosition,
                                         p_in_departments,
                                         TO_DATE (p_in_fromDate,
                                                  p_in_localeDatePattern),
                                         TO_DATE (p_in_toDate,
                                                  p_in_localeDatePattern))
                                         AS HELD_ITEMS
                                FROM rpt_claim_detail rcd, promotion p
                               WHERE     rcd.promotion_id = p.promotion_id
                                     AND p.promotion_status =
                                             NVL (p_in_promotionStatus,
                                                  p.promotion_status)
                                     AND ( ( p_in_promotionId IS NULL )
                                     OR
                                     rcd.promotion_id IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))
                                     AND NVL (rcd.claim_status, 'open') =--10/23/2014 Bug 57533
                                             NVL (p_in_claimStatus,
                                                  NVL (rcd.claim_status, 'open'))--10/23/2014 Bug 57533
                                     AND rcd.node_id IN (SELECT child_node_id
                                                           FROM rpt_hierarchy_rollup
                                                          WHERE node_id IN (SELECT * FROM TABLE (get_array_varchar(
                                                                    p_in_parentNodeId))))--08/25/2014 Bug 55594        
                                     AND rcd.submitter_pax_status =
                                             NVL (p_in_participantStatus,
                                                  rcd.submitter_pax_status)
                                     AND rcd.submitter_job_position =
                                             NVL (p_in_jobPosition,
                                                  rcd.submitter_job_position)
                                     AND ( ( p_in_departments IS NULL )
                                     OR
                                     rcd.submitter_department IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_departments))))
                                     AND ( ( p_in_countryIds IS NULL )
                                     OR
                                     rcd.submitter_country_id IN (
                                          SELECT * FROM TABLE ( get_array_varchar(p_in_countryIds))))
                                     AND NVL (TRUNC (rcd.date_submitted),
                                              TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                           p_in_fromDate,
                                                                           p_in_localeDatePattern)
                                                                   AND TO_DATE (
                                                                           p_in_toDate,
                                                                           p_in_localeDatePattern)
                            GROUP BY rcd.submitter_user_id, rcd.submitter_name
                            ORDER BY (  approved_items
                                      + denied_items
                                      + held_items)) rs)
             WHERE RN > 0 AND RN < 21;

        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            p_out_return_code := '99';
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;
    END prc_getByPaxItemStatus;
END pkg_query_claims_activity;
/
