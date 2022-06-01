CREATE OR REPLACE PROCEDURE prc_nominations_my_win_list
          ( p_in_user_id        IN  NUMBER,
            p_in_rowNumStart    IN  NUMBER,
            p_in_rowNumEnd      IN  NUMBER,
            p_in_sortedBy       IN  VARCHAR2,
            p_in_sortedOn       IN  VARCHAR2,
            p_in_locale         IN  VARCHAR2,
            p_out_returncode    OUT NUMBER,
            p_out_win_count     OUT NUMBER,
            p_out_data          OUT SYS_REFCURSOR)
AS
 /*******************************************************************************
  -- Purpose: To return the user winning nominations
  --
  -- Person             Date         Comments
  -- -----------        --------    -----------------------------------------------------
  -- nagarajs          06/27/2016    Initial Creation            
  -- Ravi Dhanekula   08/22/2016    Added level_number and level_name to the output columns list.                  
  *******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_NOMINATIONS_MY_WIN_LIST';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   v_stage                  VARCHAR2(200);

BEGIN  

DELETE temp_table_session;
  
  INSERT INTO temp_table_session
     SELECT asset_code,cms_value,key 
       FROM vw_cms_asset_value 
      WHERE key IN ('PROMOTION_LEVEL_LABEL_NAME_')
        AND locale = p_in_locale;

  p_out_returncode := 0;
  
  OPEN p_out_data FOR
    SELECT s.*
      FROM (SELECT ROW_NUMBER() OVER 
                   (ORDER BY
                     -- sort on field ascending
                     (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'promotion_name')  THEN promotion_name||' for Approval '||level_number   END) ASC
                   , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'date_won')        THEN date_won   END) ASC
                     -- sort on field descending
                   , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'promotion_name') THEN promotion_name||' for Approval '||level_number  END) DESC
                   , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'date_won')       THEN date_won  END) DESC
                   )
                   rn,
                   res.*
              FROM (SELECT nc.team_id,
                           cia.approver_user_id,
                           a.activity_id,
                           p.promotion_name,
                           NVL(cia.notification_date, TRUNC(cia.date_approved)) date_won,
                           cia.approval_round level_number,
                           (SELECT cms_name FROM temp_table_session WHERE asset_code = pnl.level_label_asset_code) level_name
                      FROM claim c,
                           claim_item ci,
                           claim_item_approver cia,
                           claim_recipient cr,
                           nomination_claim nc,
                           activity a,
                           promotion p,
                           promo_nomination_level pnl,
                           promo_nomination pn
                     WHERE c.claim_id = ci.claim_id
                       AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id)
                       AND cia.approval_status_type IN ('approv', 'winner')
                       AND NVL(cia.notification_date,TRUNC(SYSDATE)) <= TRUNC(SYSDATE)
                       AND ci.claim_item_id = cr.claim_item_id
                       AND cr.participant_id = p_in_user_id
                       AND ci.claim_id = nc.claim_id
                       AND (c.claim_id = a.claim_id OR c.claim_group_id = a.claim_group_id)
                       AND cr.participant_id = a.user_id
                       AND cia.approval_round = a.approval_round
                       AND a.is_submitter = 0
                       AND c.promotion_id = p.promotion_id
                       AND p.promotion_id = pnl.promotion_id
                       AND p.promotion_id = pn.promotion_id
                       AND ((cia.approval_round = pnl.level_index AND pn.payout_level_type = 'eachLevel')
                       OR pn.payout_level_type = 'finalLevel')
                   ) res
           ) s -- sort number result set
     WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
       AND s.rn < TO_NUMBER(p_in_rowNumEnd);
       
    SELECT COUNT(a.activity_id)
      INTO p_out_win_count
      FROM claim c,
           claim_item ci,
           claim_item_approver cia,
           claim_recipient cr,
           nomination_claim nc,
           activity a,
           promotion p,
           promo_nomination_level pnl,
           promo_nomination pn
     WHERE c.claim_id = ci.claim_id
       AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id)
       AND cia.approval_status_type IN ('approv', 'winner')
       AND NVL(cia.notification_date,TRUNC(SYSDATE)) <= TRUNC(SYSDATE)
       AND ci.claim_item_id = cr.claim_item_id
       AND cr.participant_id = p_in_user_id
       AND ci.claim_id = nc.claim_id
       AND (c.claim_id = a.claim_id OR c.claim_group_id = a.claim_group_id)
       AND cr.participant_id = a.user_id
       AND cia.approval_round = a.approval_round
       AND a.is_submitter = 0
       AND c.promotion_id = p.promotion_id
       AND p.promotion_id = pnl.promotion_id
       AND p.promotion_id = pn.promotion_id
       AND ((cia.approval_round = pnl.level_index AND pn.payout_level_type = 'eachLevel')
       OR pn.payout_level_type = 'finalLevel');

EXCEPTION 
  WHEN OTHERS THEN
      ROLLBACK;
      prc_execution_log_entry(c_process_name, c_release_level, '~ p_in_user_id: '||p_in_user_id||
      '~ ERROR at stage '||v_stage, SQLERRM, NULL);
      p_out_win_count := 0;
      OPEN p_out_data FOR SELECT NULL FROM dual;
      p_out_returncode := 99;
  
END;
/
