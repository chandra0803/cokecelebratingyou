CREATE OR REPLACE PROCEDURE prc_nomination_modal_window
         ( p_in_nominee_user_id     IN  NUMBER,
           p_in_locale              IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,
           p_out_nomniee_dtl        OUT SYS_REFCURSOR)
AS
 /*******************************************************************************
  -- Purpose: To return the details for nomination modal window if any winning nomination for user
  --
  -- Person             Date         Comments
  -- -----------        --------    -----------------------------------------------------
  -- nagarajs          06/24/2016    Initial Creation 
  -- Ravi Dhanekula   02/21/2017    Changes for Bug # 71506    
  -- Gorantla         09/05/2018    Bug 77584 - Time period not displayed adequately in the CSV extract, emails & the first time when the nominated winner logs in
  -- Loganathan       06/20/2019 	Bug 75186 - Application is displaying incorrect value for Non-US recipient
  -- Loganathan       07/02/2019    Bug 78682 - Budget - We are facing round of issues which is removing some points from the budget                         
  *******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_NOMINATION_MODAL_WINDOW';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   v_stage                  VARCHAR2(200);
   v_currency_value         cash_currency_current.bpom_entered_rate%TYPE;  --06/20/2019 Bug#75186
   
BEGIN

  p_out_returncode := 0;

 v_stage := 'Get currency value';  --06/20/2019 Bug#75186 -- Added for getting currency value
 BEGIN
   SELECT ccc.bpom_entered_rate
     INTO v_currency_value
     FROM user_address ua,
          country c,
          cash_currency_current ccc 
    WHERE ua.user_id = p_in_nominee_user_id
      AND is_primary = 1
      AND ua.country_id = c.country_id
      --AND c.country_code = ccc.to_cur
      AND c.CURRENCY_CODE = ccc.to_cur ;
  EXCEPTION
    WHEN OTHERS THEN
      v_currency_value := 1;
  END;   								--06/20/2019 Bug#75186 --upto here 
  
  DELETE FROM temp_table_session;
  
   v_stage:= 'Insert temp_table_session';
     INSERT INTO temp_table_session
     SELECT asset_code,cms_value,key 
       FROM vw_cms_asset_value 
      WHERE key IN ('PROMOTION_TIME_PERIOD_NAME_','PAYOUT_DESCRIPTION_')
        AND locale = p_in_locale
      UNION 
     SELECT asset_code, cms_value, key
       FROM vw_cms_asset_value
      WHERE asset_code IN (SELECT promo_name_asset_code FROM promotion WHERE promotion_type = 'nomination')
        AND locale = p_in_locale;
  
  DELETE FROM TMP_NOMINATION_WINDOW_MODAL_DL;
  
  v_stage:= 'Insert tmp_nomination_window_modal_dl';
  INSERT INTO TMP_NOMINATION_WINDOW_MODAL_DL
    SELECT claim_item_id,
           claim_group_id,
           approval_round,
           time_period_name_asset_code,
           promo_name_asset_code, 
           win_count,
           points_won,
           currency_code,
           cash_won,
           team_id,
           approver_user_id,
           activity_id,
           payout_description_asset_code
     FROM (SELECT cr.claim_item_id,
                   c.claim_group_id,
                   c.approval_round,
                   p.promo_name_asset_code,
                   COUNT(1) OVER(partition by ci.claim_item_id) win_count,
                   a.quantity points_won,
                   co.currency_code,
                   a.cash_award_qty cash_won,
                   nc.team_id,
                   cia.approver_user_id,
                   a.activity_id, 
                   -- nc.nomination_time_period_id,  -- 09/05/2018
                   cia.time_period_id,    -- 09/05/2018
                   p.promotion_id,
                   pnl.payout_description_asset_code
              FROM claim c,
                   claim_item ci,
                   claim_item_approver cia,
                   claim_recipient cr,
                   user_address ua,
                   country co,
                   nomination_claim nc,
                   activity a,
                   promotion p,
                   promo_nomination pn,
                   promo_nomination_level  pnl
             WHERE c.claim_id = ci.claim_id
               AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id)
               AND cia.approval_status_type IN ('approv', 'winner')
               AND NVL(cia.notification_date,TRUNC(SYSDATE)) <= TRUNC(SYSDATE)
               AND ci.claim_item_id = cr.claim_item_id
               AND (cr.winner_modal_viewed = 0 OR (C.CLAIM_GROUP_ID IS NOT NULL AND cia.approval_round > 1))--02/21/2017
               AND cr.participant_id = ua.user_id (+)
               AND ua.is_primary (+)= 1
               AND ua.country_id = co.country_id (+)
               AND cr.participant_id = p_in_nominee_user_id
               AND ci.claim_id = nc.claim_id
               AND (nc.claim_id = a.claim_id OR c.claim_group_id = a.claim_group_id)
               AND cr.participant_id = a.user_id
               AND cia.approval_round = a.approval_round
               AND a.is_submitter = 0
               AND c.promotion_id = p.promotion_id
               AND p.promotion_id = pn.promotion_id 
               AND p.promotion_id = pnl.promotion_id (+)
               AND ((pn.payout_level_type = 'eachLevel' AND cia.approval_round = pnl.level_index )
                                   OR (pn.payout_level_type = 'finalLevel' ) )
               AND NOT EXISTS (SELECT 1 FROM claim_group WHERE claim_group_id = c.claim_group_id AND WINNER_MODAL_VIEWED = 1)--02/21/2017
             ) ca,
             promo_nomination_time_period pnt
       WHERE ca.promotion_id = pnt.promotion_id (+)
         -- AND ca.nomination_time_period_id = pnt.nomination_time_period_id(+); -- 09/05/2018
         AND ca.time_period_id = pnt.nomination_time_period_id(+); -- 09/05/2018
     
   v_stage:= 'Return p_out_nomniee_dtl';
  OPEN p_out_nomniee_dtl FOR 
     SELECT DISTINCT (SELECT cms_name FROM temp_table_session WHERE asset_code = time_period_name) time_period_name,
           (select cms_name from temp_table_session where asset_code = promotion_name) as promotion_name, 
           win_count,
           DECODE(win_count,1,points_won,NULL) points_won,
           DECODE(win_count,1,currency_code,NULL) currency_code,
           DECODE(win_count,1,trunc(v_currency_value*cash_won,2),NULL) cash_won,   --06/20/2019 Bug#75186 Multiply the value with "v_currency_value"--07/02/2019 Bug#78682 handled the trunc for having 2 precesion
           CASE WHEN claim_group_id IS NULL THEN team_id
                ELSE NULL END team_id,
--           DECODE(win_count,1,team_id,claim_group_id,NULL,team_id,NULL) team_id,
           DECODE(win_count,1,approver_user_id,NULL) approver_user_id,
           DECODE(win_count,1,activity_id,NULL) activity_id,
           (SELECT cms_name FROM temp_table_session WHERE asset_code = payout_description_asset_code) payout_description_asset_code
      FROM tmp_nomination_window_modal_dl;
      
    v_stage := 'Update claim_recipient';
    UPDATE claim_recipient
       SET winner_modal_viewed = 1
     WHERE claim_item_id IN (SELECT claim_item_id
                               FROM tmp_nomination_window_modal_dl);
                            
    v_stage := 'Update claim_recipient';--02/21/2017
    UPDATE claim_group
       SET winner_modal_viewed = 1
     WHERE claim_group_id IN (SELECT claim_group_id
                                                 FROM tmp_nomination_window_modal_dl);
    
    COMMIT;                                    
EXCEPTION 
  WHEN OTHERS THEN
      ROLLBACK;
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR','~ p_in_nominee_user_id: '||p_in_nominee_user_id||
      '~ p_in_locale: '||p_in_locale||'~ ERROR at stage '||v_stage||'-->'||SQLERRM, NULL);
      OPEN p_out_nomniee_dtl FOR SELECT null FROM dual;
      p_out_returncode := 99;

END;
/