CREATE OR REPLACE PACKAGE pkg_list_pend_nominations IS

/******************************************************************************
  NAME:       pkg_list_pend_nominations
  PURPOSE:    List out the data for the pending nomination tile, detail and landing page

   Person             Date               Comments
  ----------         -------------      ------------------------------------------------
  nagarajs           04/28/2016         Initial creation                    
******************************************************************************/

PROCEDURE prc_list_pend_nominations_tile 
         ( p_in_user_id             IN  NUMBER,
           p_in_rowNumStart         IN  NUMBER,
           p_in_rowNumEnd           IN  NUMBER,
           p_in_sortedBy            IN  VARCHAR2,
           p_in_sortedOn            IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,
           p_out_pend_claim_res     OUT SYS_REFCURSOR);
           
PROCEDURE prc_list_pend_nomination_alert 
         ( p_in_user_id             IN  NUMBER,
           p_in_sortedBy            IN  VARCHAR2,
           p_in_sortedOn            IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,
           p_out_pend_claim_res     OUT SYS_REFCURSOR);
           
PROCEDURE prc_list_pend_nominations_land 
         ( p_in_promotion_id        IN  NUMBER,
           p_in_level_number        IN  NUMBER,
           p_in_approver_id         IN  NUMBER,
           p_in_locale              IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,
           p_out_promo_levels       OUT SYS_REFCURSOR,
           p_out_time_periods       OUT SYS_REFCURSOR,
           p_out_dropdown_details   OUT SYS_REFCURSOR);
           
PROCEDURE prc_list_approver_detail_page 
         ( p_in_promotion_id        IN  NUMBER,
           p_in_level_number        IN  NUMBER,
           p_in_approver_id         IN  NUMBER,
           p_in_time_period_id      IN  NUMBER,
           p_in_submit_start_date   IN  DATE,
           p_in_submit_end_date     IN  DATE,
           p_in_status              IN  VARCHAR2,
           p_in_locale              IN  VARCHAR2,
           p_in_rowNumStart         IN  NUMBER,
           p_in_rowNumEnd           IN  NUMBER,
           p_in_sortedBy            IN  VARCHAR2,
           p_in_sortedOn            IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,  
           p_out_total_nomi_count   OUT NUMBER,
           p_out_prev_lvl_approvers OUT SYS_REFCURSOR,
           p_out_next_lvl_approvers OUT SYS_REFCURSOR,
           p_out_nomin_detail       OUT SYS_REFCURSOR,
           p_out_team_members_dtl   OUT SYS_REFCURSOR,
           p_out_behaviors_dtl      OUT SYS_REFCURSOR,
           p_out_custom_dtl         OUT SYS_REFCURSOR,
           p_out_award_amount_dtl   OUT SYS_REFCURSOR,
           p_out_max_win_claim_dtl  OUT SYS_REFCURSOR,
           p_out_dtl                OUT SYS_REFCURSOR,
           p_out_cummula_nomi_dtl   OUT SYS_REFCURSOR);
           
PROCEDURE prc_list_appr_cummula_dtl_page 
         ( p_in_promotion_id        IN  NUMBER,
           p_in_nominee_id          IN  NUMBER,
           p_in_claim_group_id      IN  NUMBER,
           p_in_level_number        IN  NUMBER,
           p_in_approver_id         IN  NUMBER,
           p_in_time_period_id      IN  NUMBER,
           p_in_submit_start_date   IN  DATE,
           p_in_submit_end_date     IN  DATE,
           p_in_status              IN  VARCHAR2,
           p_in_locale              IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,  
           p_out_nominator_detail   OUT SYS_REFCURSOR,
           p_out_custom_dtl         OUT SYS_REFCURSOR);
           
PROCEDURE prc_list_approver_elig_promos 
         ( p_in_approver_id         IN  NUMBER,
           p_out_returncode         OUT NUMBER,  
           p_out_promotion_detail   OUT SYS_REFCURSOR);
           
PROCEDURE prc_list_pend_nomination_extr
         ( p_in_promotion_id        IN  NUMBER,
           p_in_level_number        IN  NUMBER,
           p_in_approver_id         IN  NUMBER,
           p_in_time_period_id      IN  NUMBER,
           p_in_submit_start_date   IN  DATE,
           p_in_submit_end_date     IN  DATE,
           p_in_status              IN  VARCHAR2,
           p_in_locale              IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,  
           p_out_pend_claim_dtl    OUT SYS_REFCURSOR);
END;
/

CREATE OR REPLACE PACKAGE BODY pkg_list_pend_nominations IS

/******************************************************************************
  NAME:       pkg_list_pend_nominations
  PURPOSE:    List out the data for the pending nomination tile, detail and landing page

   Person             Date               Comments
  ----------         -------------      ------------------------------------------------
  nagarajs           04/28/2016         Initial creation               
******************************************************************************/

  
PROCEDURE prc_list_pend_nominations_tile
         ( p_in_user_id             IN  NUMBER,
           p_in_rowNumStart         IN  NUMBER,
           p_in_rowNumEnd           IN  NUMBER,
           p_in_sortedBy            IN  VARCHAR2,
           p_in_sortedOn            IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,
           p_out_pend_claim_res     OUT SYS_REFCURSOR)
AS
 /*******************************************************************************
  -- Purpose: Return data to decide the pending/winner/non-winner/more_info nomination tile to the approvers
  --
  -- Person             Date         Comments
  -- -----------        --------    -----------------------------------------------------
  -- nagarajs          04/28/2016    Initial Creation         
  -- Ravi Dhanekula    05/03/2016    Added count output,page number and sort parameters.     
  -- Ravi Dhanekula    04/20/2017    Bug # 72204 applied the fix.
  -- Gorantla          09/05/2017    JIRA# 2911 Nomination approval- The alert message is not triggered to the approver when the approval level is more than 3
  -- Chidamba          11/21/2017    JIRA# 3511 Nomination approval- The alert message is not triggered to the approver [ 2911 fix has date range validation problem].
  -- Chidamba          02/28/2018    JIRA# 3873 restricting 'more_info' status claim count shouldn't show in Nomination pending tile.
  -- Loganathan        03/25/2019    Bug 78985 - Nominations - Keep Private Until Specific Date Is Not Working
  -- DeepakrajS        04/12/2019    Bug 78401 - Approvers are getting nomination review alert for future approval start date
  *******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_LIST_PEND_NOMINATIONS_TILE';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   L_sort_on                VARCHAR2(30);
   L_sort_by                VARCHAR2(30);
   v_more_info_status       VARCHAR2(30) := pkg_const.gc_approval_status_more_info; --02/28/2018
BEGIN



  L_sort_on := LOWER(p_in_sortedOn); 
  L_sort_by := LOWER(p_in_sortedBy); 
   
  p_out_returncode := 0;
  
 
   OPEN p_out_pend_claim_res FOR
  SELECT s.*
    FROM (SELECT -- build row number sort field
                   ROW_NUMBER() OVER 
                   (ORDER BY
                     -- sort on field ascending
                     (CASE WHEN (L_sort_by = 'asc' AND L_sort_on = 'promotion_name')              THEN UPPER(res.promotion_name) END) ASC
                   , (CASE WHEN (L_sort_by = 'asc' AND L_sort_on = 'status')              THEN UPPER(res.status)        END) ASC
                   , (CASE WHEN (L_sort_by = 'asc' AND L_sort_on = 'level_label')                 THEN UPPER(res.level_label)    END) ASC
                    -- sort on field descending
                   , (CASE WHEN (L_sort_by = 'desc' AND L_sort_on = 'promotion_name')             THEN UPPER(res.promotion_name) END) DESC
                   , (CASE WHEN (L_sort_by = 'desc' AND L_sort_on = 'status')             THEN UPPER(res.status)        END) DESC
                   , (CASE WHEN (L_sort_by = 'desc' AND L_sort_on = 'level_label')                THEN UPPER(res.level_label)    END) DESC
                   ) AS rn, 
                   res.*
            FROM (  SELECT r.promotion_id,
                           r.promotion_name,
                           r.claim_id,
                           r.approval_round,
                           r.date_submitted,
                           NVL(r.level_label,r.approval_round) level_label,
                           r.payout_level_type,
                           r.promotion_start_date,
                           r.promotion_end_date,         
                           COUNT(DISTINCT r.promotion_id||r.level_label||r.status) OVER () AS total_promo_records, -- pagination count                  
                           COUNT(DISTINCT (DECODE(r.status,v_more_info_status,NULL,DECODE(r.evaluation_type, 'cumulative-pend',r.cumm_pax_id||r.claim_group_id,'independent-pend',r.claim_id)))) OVER () AS total_records, --pending nomination count in home tile --02/28/2018 Added decode validate status 'more_info'
                           RANK () OVER (PARTITION BY r.promotion_id,r.approval_round,r.status ORDER BY r.date_submitted desc,r.claim_id desc) AS rec_rank, --04/20/2017
                           CASE WHEN r.level_cnt > 1 THEN 1
                           ELSE 0 END is_multiple_level,
                           CASE WHEN r.payout_level_type = 'finalLevel' AND r.level_index = r.approval_round THEN 1
                           ELSE 0 END is_final_level,
                           r.status
                      FROM (SELECT p.promotion_id, 
                                   p.promotion_name,
                                   pn.evaluation_type||'-pend' evaluation_type,
                                   c.claim_id,
                                   c.claim_group_id,
                                   pnl.level_index,
                                   c.approval_round,
                                   NVL (c.date_modified, submission_date) date_submitted,
                                   pnl.level_label,
                                   pn.payout_level_type,
                                   p.promotion_start_date,
                                   p.promotion_end_date,
                                   (SELECT COUNT(DISTINCT c.approval_round)
                                      FROM claim_approver_snapshot cm,
                                           claim c
                                     WHERE cm.approver_user_id = p_in_user_id
                                       AND (cm.claim_id = c.claim_id OR c.claim_group_id = c.claim_group_id)
                                       AND c.is_open = 1
                                       AND c.promotion_id = p.promotion_id
                                    ) level_cnt,
                                    (SELECT participant_id
                                      FROM claim_item ci,
                                           claim_recipient cr,
                                           promo_nomination pn
                                     WHERE ci.claim_id = c.claim_id
                                       AND ci.claim_item_id = cr.claim_item_id
                                       And pn.promotion_id = c.promotion_id
                                       AND pn.evaluation_type = 'cumulative'
                                   ) cumm_pax_id,
                                   (SELECT DISTINCT ci.approval_status_type
                                      FROM claim_item ci
                                     WHERE ci.claim_id = c.claim_id) status,
                                   CASE WHEN  pn.payout_level_type = 'finalLevel' THEN NVL(p.award_budget_master_id,p.cash_budget_master_id)
                                        WHEN pn.payout_level_type = 'eachLevel' AND pnl.award_payout_type = 'points' THEN p.award_budget_master_id
                                        WHEN pn.payout_level_type = 'eachLevel' AND pnl.award_payout_type = 'cash' THEN p.cash_budget_master_id
                                    ELSE NVL(p.award_budget_master_id,p.cash_budget_master_id) 
                                   END as budget_master_id
                              FROM claim_approver_snapshot cm, --pending/more_info nominations
                                   claim c,
                                   promotion p,
                                   promo_nomination pn,
                                   promo_nomination_level pnl
                             WHERE cm.approver_user_id = p_in_user_id
                               AND (cm.claim_id = c.claim_id OR c.claim_group_id = cm.claim_group_id)
                               AND c.is_open = 1
                               AND c.promotion_id = p.promotion_id
                               AND p.promotion_id = pn.promotion_id
                               AND c.promotion_id = pnl.promotion_id
                               AND ((pn.payout_level_type = 'eachLevel' AND c.approval_round = pnl.level_index )
                                   OR (pn.payout_level_type = 'finalLevel' ) )
                               AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE
                               AND NVL(p.approval_start_date,SYSDATE) <= SYSDATE  --04/12/2019
                               AND EXISTS ( SELECT 1
                                              FROM claim_recipient cr,
                                                   claim_item ci,
                                                   claim cl,
                                                   application_user au
                                             WHERE cr.claim_item_id = ci.claim_item_id
                                               AND ci.claim_id = cl.claim_id
                                               AND cl.claim_id = c.claim_id
                                               AND cr.participant_id = au.user_id
                                               AND au.is_active = 1
                                                )
                               /*AND NOT EXISTS (SELECT 1						--03/25/2019    Bug 78985
                                                 FROM  claim_item ci,
                                                       claim_item_approver cia
                                                 WHERE ci.claim_id = c.claim_id
                                                   AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id) 
                                                   AND cia.approval_round = (c.approval_round - 1)
                                                   AND cia.notification_date > TRUNC(SYSDATE)
                                               ) */  
                             UNION ALL        
                            SELECT DISTINCT p.promotion_id,
                                   p.promotion_name,
                                   pn.evaluation_type,
                                   c.claim_id,
                                   c.claim_group_id,
                                   pnl.level_index,
                                   cia.approval_round,
                                   NVL (c.date_modified, c.submission_date) date_submitted,
                                   pnl.level_label,
                                   pn.payout_level_type,
                                   p.promotion_start_date,
                                   p.promotion_end_date,
                                   (SELECT COUNT(DISTINCT cia.approval_round)
                                      FROM claim_item_approver cia,
                                           claim_item ci,
                                           claim c
                                     WHERE cia.approver_user_id = p_in_user_id
                                       AND (cia.claim_item_id = ci.claim_item_id OR cia.claim_group_id = c.claim_group_id)
                                       AND ci.claim_id = c.claim_id
                                       AND c.promotion_id = p.promotion_id
                                    ) level_cnt,
                                    (SELECT participant_id
                                      FROM claim_recipient cr,
                                           promo_nomination pn
                                     WHERE ci.claim_item_id = cr.claim_item_id
                                       And pn.promotion_id = c.promotion_id
                                       AND pn.evaluation_type = 'cumulative'
                                   ) cumm_pax_id,
                                   cia.approval_status_type status,
                                   CASE WHEN  pn.payout_level_type = 'finalLevel' THEN NVL(p.award_budget_master_id,p.cash_budget_master_id)
                                        WHEN pn.payout_level_type = 'eachLevel' AND pnl.award_payout_type = 'points' THEN p.award_budget_master_id
                                        WHEN pn.payout_level_type = 'eachLevel' AND pnl.award_payout_type = 'cash' THEN p.cash_budget_master_id
                                    ELSE NVL(p.award_budget_master_id,p.cash_budget_master_id) 
                                   END as budget_master_id
                              FROM claim_item_approver cia,--winner/approv/non_winner
                                   claim_item ci,
                                   claim c,
                                   promotion p,
                                   promo_nomination pn,
                                   promo_nomination_level pnl
                             WHERE cia.approver_user_id = p_in_user_id
                               AND cia.approval_status_type IN ('winner','approv','non_winner')
                               AND (cia.claim_item_id = ci.claim_item_id OR cia.claim_group_id = c.claim_group_id)
                               AND ci.claim_id = c.claim_id
                               AND c.promotion_id = p.promotion_id
                               AND p.promotion_id = pn.promotion_id
                               AND c.promotion_id = pnl.promotion_id
                               AND ((pn.payout_level_type = 'eachLevel' AND cia.approval_round = pnl.level_index )
                                   OR (pn.payout_level_type = 'finalLevel' ) )
                               AND NVL(cia.notification_date,TRUNC(SYSDATE)) <= TRUNC(SYSDATE)     
                               AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE   
                               AND NVL(p.approval_start_date,SYSDATE) <= SYSDATE  --04/12/2019            
                           ) r,
                           budget_segment bs
                     WHERE r.budget_master_id = bs.budget_master_id(+)
                       AND (bs.budget_master_id IS NULL OR 
                          -- (bs.budget_master_id IS NOT NULL AND TRUNC(SYSDATE) BETWEEN bs.start_date AND NVL(bs.end_date,TRUNC(SYSDATE))))  -- 09/05/2017
                           (bs.budget_master_id IS NOT NULL     -- 09/05/2017
                       AND (bs.start_date <= TRUNC(SYSDATE)      -- 09/05/2017  --11/21/2017
                       AND (bs.end_date IS NULL OR (bs.end_date >= TRUNC(SYSDATE) AND bs.end_date IS NOT NULL)))))      -- 09/05/2017                      
                   ) res WHERE rec_rank = 1-- full result set
          ) s -- sort number result set
    WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
      AND s.rn < TO_NUMBER(p_in_rowNumEnd)
    ORDER BY s.rn;
   

EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_user_id: '||p_in_user_id||'-->'||SQLERRM, NULL);
      p_out_returncode := 99;
      OPEN p_out_pend_claim_res FOR SELECT NULL FROM DUAL;
END prc_list_pend_nominations_tile;

PROCEDURE prc_list_pend_nomination_alert
         ( p_in_user_id             IN  NUMBER,
           p_in_sortedBy            IN  VARCHAR2,
           p_in_sortedOn            IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,
           p_out_pend_claim_res     OUT SYS_REFCURSOR)
AS
 /*******************************************************************************
  -- Purpose: Return data to decide the pending/more_info nomination to the approversin alert page
  --
  -- Person             Date         Comments
  -- -----------        --------    -----------------------------------------------------
  -- nagarajs          09/26/2016    Initial Creation
  --Ravi Dhanekula     04/20/2017    Bug # 72204 applied the fix.
  *******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_LIST_PEND_NOMINATION_ALERT';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   L_sort_on                VARCHAR2(30);
   L_sort_by                VARCHAR2(30);
BEGIN

  L_sort_on := LOWER(p_in_sortedOn); 
  L_sort_by := LOWER(p_in_sortedBy); 
   
  p_out_returncode := 0;
  
   OPEN p_out_pend_claim_res FOR
  SELECT s.*
    FROM (SELECT -- build row number sort field
                   ROW_NUMBER() OVER 
                   (ORDER BY
                     -- sort on field ascending
                     (CASE WHEN (L_sort_by = 'asc' AND L_sort_on = 'promotion_name')              THEN UPPER(res.promotion_name) END) ASC
                   , (CASE WHEN (L_sort_by = 'asc' AND L_sort_on = 'date_submitted')              THEN res.date_submitted        END) ASC
                   , (CASE WHEN (L_sort_by = 'asc' AND L_sort_on = 'level_label')                 THEN UPPER(res.level_label)    END) ASC
                    -- sort on field descending
                   , (CASE WHEN (L_sort_by = 'desc' AND L_sort_on = 'promotion_name')             THEN UPPER(res.promotion_name) END) DESC
                   , (CASE WHEN (L_sort_by = 'desc' AND L_sort_on = 'date_submitted')             THEN res.date_submitted        END) DESC
                   , (CASE WHEN (L_sort_by = 'desc' AND L_sort_on = 'level_label')                THEN UPPER(res.level_label)    END) DESC
                   ) AS rn, 
                   res.*
            FROM (  SELECT r.promotion_id,
                           r.promotion_name,
                           r.claim_id,
                           r.approval_round,
                           r.date_submitted,
                           NVL(r.level_label,r.approval_round) level_label,
                           r.payout_level_type,
                           r.promotion_start_date,
                           r.promotion_end_date,         
                           COUNT(DISTINCT r.promotion_id||r.level_label||r.status) OVER () AS total_promo_records,                   
                           COUNT(DISTINCT (DECODE(r.evaluation_type, 'cumulative-pend',r.cumm_pax_id||r.claim_group_id,'independent-pend',r.claim_id))) OVER () AS total_records,
                           RANK () OVER (PARTITION BY r.promotion_id,r.approval_round,r.status ORDER BY r.date_submitted desc,r.claim_id desc) AS rec_rank,--04/20/2017
                           CASE WHEN r.level_cnt > 1 THEN 1
                           ELSE 0 END is_multiple_level,
                           CASE WHEN r.payout_level_type = 'finalLevel' AND r.level_index = r.approval_round THEN 1
                           ELSE 0 END is_final_level,
                           r.status
                      FROM (SELECT p.promotion_id,
                                   p.promotion_name,
                                   pn.evaluation_type||'-pend' evaluation_type,
                                   c.claim_id,
                                   c.claim_group_id,
                                   pnl.level_index,
                                   c.approval_round,
                                   NVL (c.date_modified, submission_date) date_submitted,
                                   pnl.level_label,
                                   pn.payout_level_type,
                                   p.approval_start_date promotion_start_date,
                                   p.approval_end_date promotion_end_date,
                                   (SELECT COUNT(DISTINCT c.approval_round)
                                      FROM claim_approver_snapshot cm,
                                           claim c
                                     WHERE cm.approver_user_id = p_in_user_id
                                       AND (cm.claim_id = c.claim_id OR c.claim_group_id = c.claim_group_id)
                                       AND c.is_open = 1
                                       AND c.promotion_id = p.promotion_id
                                    ) level_cnt,
                                    (SELECT participant_id
                                      FROM claim_item ci,
                                           claim_recipient cr,
                                           promo_nomination pn
                                     WHERE ci.claim_id = c.claim_id
                                       AND ci.claim_item_id = cr.claim_item_id
                                       And pn.promotion_id = c.promotion_id
                                       AND pn.evaluation_type = 'cumulative'
                                   ) cumm_pax_id,
                                   (SELECT DISTINCT ci.approval_status_type
                                      FROM claim_item ci
                                     WHERE ci.claim_id = c.claim_id) status,
                                     CASE WHEN  pn.payout_level_type = 'finalLevel' THEN NVL(p.award_budget_master_id,p.cash_budget_master_id)
                                        WHEN pn.payout_level_type = 'eachLevel' AND pnl.award_payout_type = 'points' THEN p.award_budget_master_id
                                        WHEN pn.payout_level_type = 'eachLevel' AND pnl.award_payout_type = 'cash' THEN p.cash_budget_master_id
                                    ELSE NVL(p.award_budget_master_id,p.cash_budget_master_id) 
                                   END as budget_master_id
                              FROM claim_approver_snapshot cm,
                                   claim c,
                                   promotion p,
                                   promo_nomination pn,
                                   promo_nomination_level pnl
                             WHERE cm.approver_user_id = p_in_user_id
                               AND (cm.claim_id = c.claim_id OR c.claim_group_id = cm.claim_group_id)
                               AND c.is_open = 1
                               AND c.promotion_id = p.promotion_id
                               AND p.promotion_id = pn.promotion_id
                               AND c.promotion_id = pnl.promotion_id
                               AND ((pn.payout_level_type = 'eachLevel' AND c.approval_round = pnl.level_index )
                                   OR (pn.payout_level_type = 'finalLevel' ) )
                               AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE
                               AND EXISTS ( SELECT 1
                                              FROM claim_recipient cr,
                                                   claim_item ci,
                                                   claim cl,
                                                   application_user au
                                             WHERE cr.claim_item_id = ci.claim_item_id
                                               AND ci.claim_id = cl.claim_id
                                               AND cl.claim_id = c.claim_id
                                               AND cr.participant_id = au.user_id
                                               AND au.is_active = 1
                                                )  
                               AND NOT EXISTS (SELECT 1
                                                 FROM  claim_item ci,
                                                       claim_item_approver cia
                                                 WHERE ci.claim_id = c.claim_id
                                                   AND (ci.claim_item_id = cia.claim_item_id OR c.claim_group_id = cia.claim_group_id) 
                                                   AND cia.approval_round = (c.approval_round - 1)
                                                   AND cia.notification_date > TRUNC(SYSDATE)
                                               )                  
                           ) r,
                           budget_segment bs
                     WHERE r.budget_master_id = bs.budget_master_id(+)
                       AND (bs.budget_master_id IS NULL OR 
                            (bs.budget_master_id IS NOT NULL AND TRUNC(SYSDATE) BETWEEN bs.start_date AND NVL(bs.end_date,TRUNC(SYSDATE))))
                   ) res WHERE rec_rank = 1-- full result set
          ) s -- sort number result set    
    ORDER BY s.rn;
   

EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_user_id: '||p_in_user_id||'-->'||SQLERRM, NULL);
      p_out_returncode := 99;
      OPEN p_out_pend_claim_res FOR SELECT NULL FROM DUAL;
END prc_list_pend_nomination_alert;

PROCEDURE prc_list_pend_nominations_land 
          (p_in_promotion_id        IN  NUMBER,
           p_in_level_number        IN  NUMBER,
           p_in_approver_id         IN  NUMBER,
           p_in_locale              IN  VARCHAR2,
           p_out_returncode         OUT NUMBER, 
           p_out_promo_levels       OUT SYS_REFCURSOR,
           p_out_time_periods       OUT SYS_REFCURSOR,
           p_out_dropdown_details   OUT SYS_REFCURSOR)
AS
/*******************************************************************************
 Purpose: To return the  nomination time periods , promo_levels and 
          null level approvers to the approvers while landing the tile to detail page
 
  Person             Date         Comments
  -----------       --------      -----------------------------------------------------
  nagarajs          05/03/2016    Initial Creation        
  Sherif Basha      02/15/2017    Bug 71445 - Admin approval - Nomination Promotions with 'payout @ final level' is not able to be approved by Admin 
                                  For admin the level number is always passed as 1 from application as there is no alert function for admin                       
  chidamba          01/31/2018    G6-3788 - permanent fix commeting DBMS_LOB.SUBSTR as it restricts only few content to be shown in View Rules link in pending nomination land page;
                                  replacing it with query to return CLOB content by this way it shows full content in view rules.
*******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_LIST_PEND_NOMINATIONS_LAND';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';

   
   v_stage                  VARCHAR2(200);
   v_approver_is_admin      NUMBER(10);
   v_default_locale         os_propertyset.string_val%TYPE;
   
BEGIN

  p_out_returncode := 0;
    
  SELECT COUNT(1)
    INTO v_approver_is_admin
    FROM application_user
   WHERE user_id = p_in_approver_id
     AND user_type = 'bi';
     
  BEGIN
    SELECT string_val 
      INTO v_default_locale
      FROM os_propertyset
     WHERE entity_name = 'default.language';
  EXCEPTION
    WHEN OTHERS THEN
      v_default_locale := 'en_US';
  END;
  
 DELETE temp_table_session;
 
 v_stage:= 'Insert temp_table_session';
 INSERT INTO temp_table_session
 SELECT asset_code,cms_value,key
   FROM (SELECT asset_code,cms_value,key ,  RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn
           FROM vw_cms_asset_value 
          WHERE key IN ('PROMOTION_LEVEL_LABEL_NAME_' , 'PROMOTION_TIME_PERIOD_NAME_')
            AND locale IN (v_default_locale, p_in_locale)
          UNION ALL
         SELECT asset_code, cms_value, key, RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn
           FROM vw_cms_asset_value
          WHERE asset_code IN (SELECT promo_name_asset_code FROM promotion WHERE promotion_type = 'nomination')
            AND locale IN (v_default_locale, p_in_locale)
--         UNION ALL --01/31/2018 start G6-3788
--         SELECT a.code AS asset_code,
--                DBMS_LOB.SUBSTR (cd.VALUE, 3500, 1) AS cms_value,
--                cd.key, RANK() OVER(PARTITION BY a.code ORDER BY DECODE(p_in_locale,locale,1,2)) rn
--           FROM cms_asset a,
--                cms_content_key ck,
--                cms_content c,
--                cms_content_data cd
--          WHERE a.id = ck.asset_id
--            AND ck.id = c.content_key_id
--            AND c.content_status = 'Live'
--            AND c.id = cd.content_id
--            AND cd.key = 'WEBRULES_NAME_'
--            AND locale IN (v_default_locale, p_in_locale) --01/31/2018 end G6-3788
      ) WHERE rn = 1;
  
  v_stage := 'Get level informations';
  OPEN p_out_promo_levels FOR
    SELECT distinct level_index level_number, --02/15/2017    Bug 71445 return distinct levels
           (SELECT cms_name FROM temp_table_session WHERE asset_code = level_label_asset_code) level_name,
           level_label
      FROM (  --02/15/2017    Bug 71445  Commented the admin query to make it same as user query
          /* SELECT pnl.level_index, pnl.level_label_asset_code,pnl.level_label
              FROM promotion p,
                   promo_nomination_level pnl
             WHERE p.promotion_id = pnl.promotion_id
               AND p.promotion_id = p_in_promotion_id
               AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE
               AND v_approver_is_admin = 1 --if admin return all nomination pro otions
             UNION ALL*/
            SELECT NVL(pnl.level_index,c.approval_round) level_index, -- NVL for get approval round from claim  for final level payout type
                   pnl.level_label_asset_code,pnl.level_label 
              FROM promotion p,
                   promo_nomination_level pnl,
                   (SELECT c.promotion_id,c.approval_round
                     FROM claim_approver_snapshot cm,
                          claim c,
                          promo_nomination pr,
                          promo_nomination_level pnl
                    WHERE (cm.approver_user_id = p_in_approver_id OR v_approver_is_admin = 1)  ----02/15/2017    Bug 71445 admin should get all approver's pending list
                      AND (cm.claim_id = c.claim_id OR cm.claim_group_id = c.claim_group_id)
                      AND c.is_open = 1
                      AND c.promotion_id = p_in_promotion_id
                      AND c.promotion_id = pr.promotion_id
                      AND (c.approval_round = p_in_level_number OR v_approver_is_admin = 1)    ----02/15/2017    Bug 71445 admin should get all levels pending list
                      AND ( (c.approval_round = pnl.level_index AND pr.payout_level_type = 'eachLevel') OR pr.payout_level_type = 'finalLevel' )
                      AND EXISTS ( SELECT 1
                                      FROM claim_recipient cr,
                                           claim_item ci,
                                           claim cl,
                                           application_user au
                                     WHERE cr.claim_item_id = ci.claim_item_id
                                       AND ci.claim_id = cl.claim_id
                                       AND cl.claim_id = c.claim_id
                                       AND cr.participant_id = au.user_id
                                       AND au.is_active = 1
                                 )
                    GROUP BY c.promotion_id,c.approval_round
                    ) c
             WHERE c.promotion_id = pnl.promotion_id(+)
               AND c.approval_round = pnl.level_index(+)
               AND p.promotion_id = p_in_promotion_id
               AND p.promotion_id = c.promotion_id
               AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE
              -- AND v_approver_is_admin = 0        ----02/15/2017    Bug 71445
             UNION ALL
            SELECT NVL(pnl.level_index,c.approval_round), -- NVL for get approval round from claim  for final level payout type
                   pnl.level_label_asset_code,pnl.level_label
              FROM promotion p,
                   promo_nomination_level pnl,
                   (SELECT c.promotion_id,cia.approval_round
                     FROM claim_item_approver cia,
                          claim_item ci,
                          claim c,
                          promo_nomination pr,
                          promo_nomination_level pnl
                    WHERE (cia.approver_user_id = p_in_approver_id OR v_approver_is_admin = 1)      ----02/15/2017    Bug 71445 admin should get all approver's winner/approve/nonwinner list
                      AND (cia.claim_item_id = ci.claim_item_id OR cia.claim_group_id = c.claim_group_id)
                      AND ci.claim_id = c.claim_id
                      AND c.promotion_id = p_in_promotion_id
                      AND c.promotion_id = pr.promotion_id
                      AND (cia.approval_round = p_in_level_number OR v_approver_is_admin = 1)      ----02/15/2017    Bug 71445 admin should get all levels winner/approve/nonwinner list
                      AND ( (cia.approval_round = pnl.level_index AND pr.payout_level_type = 'eachLevel') OR pr.payout_level_type = 'finalLevel' )
                      group by c.promotion_id,cia.approval_round
                    ) c
             WHERE c.promotion_id = pnl.promotion_id(+)
               AND c.approval_round = pnl.level_index(+)
               AND p.promotion_id = p_in_promotion_id
               AND p.promotion_id = c.promotion_id
               AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE
             --  AND v_approver_is_admin = 0    --02/15/2017    Bug 71445 
           ) order by level_number;             --02/15/2017    Bug 71445 added sort
           
  v_stage := 'Get nomination time periods';
  OPEN p_out_time_periods FOR
    SELECT pnt.nomination_time_period_id,
           (SELECT cms_name FROM temp_table_session WHERE asset_code = time_period_name_asset_code) time_period_name,
           CASE WHEN n.user_win_cnt >= pnt.max_wins_allowed THEN 1
           ELSE 0 end exceed_flag --if no.of user win more than the promotion setup, nominations are read only
      FROM promotion p,
           promo_nomination_time_period pnt,
           (SELECT nomination_time_period_id,COUNT(DISTINCT c.claim_id)  user_win_cnt
              FROM claim_item ci,
                   claim c,
                   nomination_claim nc
             WHERE c.claim_id = ci.claim_id
               AND c.is_open = 1
               AND ci.approval_status_type = 'winner'
               AND c.claim_id = nc.claim_id
               AND c.promotion_id = p_in_promotion_id
             GROUP BY nomination_time_period_id
           ) n
     WHERE pnt.promotion_id =  p_in_promotion_id
       AND pnt.nomination_time_period_id = n.nomination_time_period_id(+)
       AND pnt.promotion_id = p.promotion_id
       AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE;
       
  v_stage := 'Get award information';
  OPEN p_out_dropdown_details FOR
    SELECT (select cms_name from temp_table_session where asset_code = p.promo_name_asset_code) as promotion_name,
           CASE WHEN (SELECT MAX(level_index)
                        FROM promo_nomination_level
                       WHERE promotion_id = p_in_promotion_id) = p_in_level_number THEN 1
                ELSE 0
             END final_level_approver,
           CASE WHEN (SELECT COUNT(1)
                        FROM promo_nomination_time_period
                       WHERE promotion_id = p_in_promotion_id) > 0 THEN 1
                ELSE 0
             END time_period_enabled, --enable time period id dropdown
           DECODE(pn.payout_level_type,'eachLevel',1,0)  payout_each_level,
           pn.payout_level_type payout_type,
           pnl.award_type,
           pnl.currency_label,
           (--select cms_name from temp_table_session where asset_code = p.cm_asset_code --01/31/2018 G6-3788 replace with below query to handle CLOB
            /****************added 01/31/2018 G6-3788**************/
            select cms_value cms_name from
           (SELECT a.code AS asset_code,
                cd.value AS cms_value, --01/31/2018 G6-3788
                cd.key, RANK() OVER(PARTITION BY a.code ORDER BY DECODE(p_in_locale,locale,1,2)) rn
           FROM cms_asset a,
                cms_content_key ck,
                cms_content c,
                cms_content_data cd
          WHERE a.id = ck.asset_id
            AND ck.id = c.content_key_id
            AND c.content_status = 'Live'
            AND c.id = cd.content_id
            AND cd.key = 'WEBRULES_NAME_'
            AND locale IN (v_default_locale, p_in_locale)
            AND a.code = p.cm_asset_code) WHERE rn = 1) as rules_text,
           /****************end 01/31/2018 G6-3788 **************/
           total_promotion_count --to decide the promotion drop down size
      FROM promotion p,
           promo_nomination pn,
           (SELECT pn.promotion_id,
                   award_payout_type award_type,
                   award_amount_fixed,
                   award_amount_min,
                   award_amount_max ,
                   CASE WHEN award_payout_type = 'cash' THEN
                     (SELECT currency_code
                       FROM user_address ua,
                            country c
                      WHERE ua.user_id = p_in_approver_id
                        AND is_primary = 1
                        AND ua.country_id = c.country_id
                       ) 
                   ELSE NULL
                   END currency_label    
              FROM promo_nomination_level pnl,
                   promo_nomination pn
             WHERE pn.promotion_id = p_in_promotion_id
               AND pnl.promotion_id(+) = pn.promotion_id
               AND ((pnl.level_index = p_in_level_number AND pn.payout_level_type = 'eachLevel')
                   OR (pn.payout_level_type = 'finalLevel'))
           ) pnl,
           (SELECT COUNT(1) total_promotion_count
              FROM (SELECT c.promotion_id 
                      FROM claim_item_approver cia,
                           claim_item ci,
                           claim c,
                           promotion p
                     WHERE (cia.approver_user_id = p_in_approver_id OR v_approver_is_admin = 1)
                       AND (cia.claim_item_id = ci.claim_item_id OR c.claim_group_id = cia.claim_group_id)
                       AND ci.claim_id = c.claim_id
                       AND c.promotion_id = p.promotion_id
                       AND p.promotion_type = 'nomination'
                     UNION 
                    SELECT c.promotion_id 
                      FROM claim_approver_snapshot cas,
                           claim c,
                           promotion p
                     WHERE (cas.approver_user_id = p_in_approver_id OR v_approver_is_admin = 1)
                       AND (cas.claim_id = c.claim_id OR cas.claim_group_id = c.claim_group_id)
                       AND c.promotion_id = p.promotion_id
                       AND p.promotion_type = 'nomination'
                       AND EXISTS ( SELECT 1
                                      FROM claim_recipient cr,
                                           claim_item ci,
                                           claim cl,
                                           application_user au
                                     WHERE cr.claim_item_id = ci.claim_item_id
                                       AND ci.claim_id = cl.claim_id
                                       AND cl.claim_id = c.claim_id
                                       AND cr.participant_id = au.user_id
                                       AND au.is_active = 1
                                        )
                     UNION
                    SELECT ao.promotion_id
                      FROM approver a,
                           approver_criteria ac,
                           approver_option ao
                     WHERE (user_id = p_in_approver_id OR v_approver_is_admin = 1)
                       AND a.approver_criteria_id = ac.approver_criteria_id
                       AND ac.approver_option_id = ao.approver_option_id
                     )
               )
     WHERE p.promotion_id = pn.promotion_id
       AND pn.promotion_id = pnl.promotion_id
       AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE;
           
EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_promotion_id: '||p_in_promotion_id||
      ' ~ p_in_level_number: '||p_in_level_number||' ~ p_in_approver_id: '||p_in_approver_id||' ~ Error at stage :'||v_stage||'-->'||SQLERRM, NULL);
      p_out_returncode := 99;
      OPEN p_out_promo_levels FOR SELECT NULL FROM DUAL;
      OPEN p_out_time_periods FOR SELECT NULL FROM DUAL;
      OPEN p_out_dropdown_details FOR SELECT NULL FROM DUAL;

END prc_list_pend_nominations_land;

PROCEDURE prc_list_approver_detail_page 
         ( p_in_promotion_id        IN  NUMBER,
           p_in_level_number        IN  NUMBER,
           p_in_approver_id         IN  NUMBER,
           p_in_time_period_id      IN  NUMBER,
           p_in_submit_start_date   IN  DATE,
           p_in_submit_end_date     IN  DATE,
           p_in_status              IN  VARCHAR2,
           p_in_locale              IN  VARCHAR2,
           p_in_rowNumStart         IN  NUMBER,
           p_in_rowNumEnd           IN  NUMBER,
           p_in_sortedBy            IN  VARCHAR2,
           p_in_sortedOn            IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,  
           p_out_total_nomi_count   OUT NUMBER,
           p_out_prev_lvl_approvers OUT SYS_REFCURSOR,
           p_out_next_lvl_approvers OUT SYS_REFCURSOR,
           p_out_nomin_detail       OUT SYS_REFCURSOR,
           p_out_team_members_dtl   OUT SYS_REFCURSOR,
           p_out_behaviors_dtl      OUT SYS_REFCURSOR,
           p_out_custom_dtl         OUT SYS_REFCURSOR,
           p_out_award_amount_dtl   OUT SYS_REFCURSOR,
           p_out_max_win_claim_dtl  OUT SYS_REFCURSOR,
           p_out_dtl                OUT SYS_REFCURSOR,
           p_out_cummula_nomi_dtl   OUT SYS_REFCURSOR)
AS
/*******************************************************************************
 Purpose: To return the nomination detail and team members detail to the approvers
 
  Person             Date         Comments
  -----------       --------      -----------------------------------------------------
  nagarajs          05/06/2016    Initial Creation
  Ravi Dhanekula    07/26/2016    Add new budget exceed parameter.
  nagarajs          11/03/2016    Bug 69861 - Award amount is not saving when approval done for the
                                 approve in nominations approval page on award row in the table   
 Sherif Basha       02/02/2017   Bug 71155 - Nomination Comments are flipping around for approver if you have a comma and Why Box = Yes in Form setup    
 Sherif,nagarajs     02/08/2017  Bug 71330 - Nominations - Some of the nominees are not able to be made 'winners' in the approvals page  
                                 Records(claim_ids) resulting in p_out_nomin_detail were not appearing in p_out_max_win_claim_dtl due to missing sort   
                                 syncing sort in all other refcursor as there could be chance of missing claim ids    
  Suresh J           03/10/2017  G6-1879 - Team nominations returning multiple rows 
  Ravi Dhanekula     05/05/2017  G6-2320 - winning nomination showing up twice.                                                                          
  Chidamba           08/22/2017  G6-2835 - Converting the level number to number as the LISTAGG sort considers it as character instead of number      
                                 LISTAGG ignores NULL value input(,,) when returning the concatenated string.
  Chidamba           10/23/2017  G6-3134 Using max_wins_allowed and no_of_winners values, In frond end rows will restrict approval according.
  Suresh J           02/12/2019  Bug 78960 - Form elements description&copy block test in not showing on approval page
  Loganathan         03/01/2019  Bug 78967 - Winner option is grayed out due to time period unavailable
  Loganathan         03/25/2019  Bug 78985 - Nominations - Keep Private Until Specific Date Is Not Working
  Loganathan      	 05/21/2019  Bug 77739 - Nomination with the comments of 4000 characters length is not loading on nomination approval page.
  Gorantla           06/26/2019  Bug 77739 - Reverting above changes (05/21/2019) and added appropriate fix in all the impacted areas
  Loganathan 		 07/10/2019  Bug 75174 - Nomination - Review history is displaying exact amount deducted in the database for Non-US approver 
*******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_LIST_APPROVER_DETAIL_PAGE';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';

   
   v_stage                  VARCHAR2(500);
   v_approval_level         approver_option.approval_round%TYPE;
   v_budget_master_id       budget_master.budget_master_id%TYPE;
   v_budget_type            budget_master.budget_type%TYPE;
   v_approver_type          promotion.approver_type%TYPE;
   v_final_level_number     NUMBER(1);
   v_currency_value         cash_currency_current.bpom_entered_rate%TYPE;
   v_approver_is_admin      NUMBER(10);
   v_request_more_budget    NUMBER(1);                      --07/26/2016
   v_award_active           NUMBER(1);                      --07/26/2016
   v_budget_exceeded        NUMBER(18);                     --07/26/2016
   v_level_has_award        NUMBER(1) :=0;                  --07/26/2016
   v_calculator_id          calculator.calculator_id%TYPE;  --07/26/2016
   v_total_budget_required  NUMBER(18) := 0;                --07/26/2016
   v_calculator_max_award   NUMBER(18) := 0;                --07/26/2016
   v_award_payout_type      promo_nomination_level.award_payout_type%TYPE;--07/26/2016
   v_other_payout_quantity  NUMBER(10) := NULL;             --07/26/2016
   
   v_out_budget_balance     budget.current_value%TYPE;
   v_out_budget_exceeded    NUMBER;
   v_payout_level_type      promo_nomination.payout_level_type%TYPE;
   v_default_locale         os_propertyset.string_val%TYPE;
 
   type v_type_appr is table of tmp_approver_nomi_detail%ROWTYPE;
   
   v_rec_appr  v_type_appr;
    
   CURSOR cur_appr (v_approver_is_admin IN NUMBER, v_currency_value IN VARCHAR2,v_payout_level_type IN VARCHAR2, v_award_payout_type IN VARCHAR2 ) IS
    WITH nominee AS
      (SELECT c.claim_id,
              ci.claim_item_id,
              c.promotion_id,
              pnt.nomination_time_period_id,
              pnt.time_period_name_asset_code,
              c.approval_round,               
              TRUNC(c.submission_date) submission_date,
              cr.participant_id,
              c.submitter_id,
              au_giv.first_name nominator_first_name,
              au_giv.last_name nominator_last_name,
              au_rec.first_name nominee_first_name,
              au_rec.last_name nominee_last_name,
              cu.country_code country_name,
              n.name org_name,
              vw.position_type job_position_name,
              vw.department_type  department_name,
              pa.avatar_small avator_url,
              nc.team_name,
              team_id,
              nc.submitter_comments_lang_id,
              ca.small_image_name ecard_image,
              nc.own_card_name,
              nc.card_video_url,
              nc.card_video_image_url,
              nc.submitter_comments,
              nc.why_attachment_name,
              nc.why_attachment_url,
              nc.certificate_id,
              ci.approval_status_type,
              time_period_start_date,
              time_period_end_date,
              nc.more_info_comments,
              c.claim_group_id,
              CASE WHEN NVL(cr.award_qty ,0) > 0 THEN cr.award_qty
               WHEN NVL(cr.cash_award_qty ,0) > 0 THEN cr.cash_award_qty
               WHEN NVL(cr.calculator_score ,0) > 0 THEN cr.calculator_score
              END award_amount,
              c.approver_comments non_winner_comments,
              pa.is_opt_out_of_awards
         FROM claim c,
              claim_item ci,
              claim_recipient cr,
              (SELECT nc.claim_id, c.promotion_id, nc.card_id, 
                      nc.team_name,
                       nc.team_id,
                       nc.submitter_comments_lang_id,
                       nc.own_card_name,
                       nc.card_video_url,
                       nc.card_video_image_url,
                       nc.submitter_comments,
                       nc.why_attachment_name,
                       nc.why_attachment_url,
                       nc.certificate_id,
                       nc.more_info_comments, 
                       nomination_time_period_id
                  FROM nomination_claim nc,
                       claim c
                 WHERE nc.claim_id = c.claim_id)nc,
              card ca,
              promo_nomination_time_period pnt,
              promotion p,
              node n,
              application_user au_giv,
              application_user au_rec,
              participant pa,
              user_address ua,
              country cu,
              vw_curr_pax_employer vw
        WHERE c.promotion_id = NVL(p_in_promotion_id,c.promotion_id)
          AND c.claim_id = ci.claim_id
          AND ci.claim_item_id = cr.claim_item_id
          AND cr.node_id = n.node_id
          AND c.claim_id = nc.claim_id
          AND nc.card_id = ca.card_id (+)
          AND nc.promotion_id = pnt.promotion_id (+)
          AND nc.promotion_id = p.promotion_id
          AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE
          AND nc.nomination_time_period_id = pnt.nomination_time_period_id (+)
          AND (pnt.nomination_time_period_id = p_in_time_period_id OR p_in_time_period_id IS NULL)
          AND c.submitter_id = au_giv.user_id 
          AND cr.participant_id = au_rec.user_id
          AND au_rec.is_active = 1
          AND au_rec.user_id = pa.user_id
          AND au_rec.user_id = ua.user_id (+)
          AND ua.is_primary (+) = 1
          AND ua.country_id = cu.country_id (+)
          AND au_rec.user_id = vw.user_id (+)
          /*AND NOT EXISTS (SELECT 1												 --03/25/2019 Bug 78985
                             FROM  claim_item_approver cia
                            WHERE (cia.claim_item_id = ci.claim_item_id OR c.claim_group_id = cia.claim_group_id)
                              AND cia.approval_round = (c.approval_round - 1)
                              AND cia.notification_date > TRUNC(SYSDATE)
                          )*/
                   )     
    , approver_nom AS
       (SELECT c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               cia.approval_round,
               c.submission_date submitted_date,
               NULL participant_id,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               NULL nominee_first_name,
               NULL nominee_last_name,
               NULL country_name,
               NULL org_name,
               NULL job_position_name,
               NULL department_name,
               NULL avator_url,
               c.team_name,
               c.team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               cia.approval_status_type,
               c.more_info_comments,
               c.claim_group_id,
               NULL award_amount,
               COUNT(1) team_cnt,
               cia.approver_comments winner_more_info_comments,
               c.non_winner_comments,
               cia.notification_date,
               NULL is_opt_out_of_awards--05/05/2017
          FROM nominee c,
               claim_item_approver cia
         WHERE (c.claim_item_id = cia.claim_item_id 
            OR c.claim_group_id = cia.claim_group_id)
           AND c.team_name IS NOT NULL
           AND ( (p_in_status IS NULL 
                      AND( (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NOT NULL AND TRUNC(cia.date_approved) BETWEEN c.time_period_start_date AND c.time_period_end_date  ))
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND p_in_time_period_id IS NULL AND p_in_submit_start_date IS NULL AND p_in_submit_end_date IS NULL )
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NULL AND TRUNC(cia.date_approved) BETWEEN p_in_submit_start_date AND NVL(p_in_submit_end_date,TRUNC(SYSDATE)) ))
                      ))
                  OR ((cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NOT NULL AND TRUNC(cia.date_approved) BETWEEN c.time_period_start_date AND c.time_period_end_date  ))
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND p_in_time_period_id IS NULL AND p_in_submit_start_date IS NULL AND p_in_submit_end_date IS NULL )
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NULL AND TRUNC(cia.date_approved) BETWEEN p_in_submit_start_date AND NVL(p_in_submit_end_date,TRUNC(SYSDATE)) ))
                      )
                )
         GROUP BY c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               c.approval_round,
               cia.approval_round,
               c.submission_date,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               c.team_name,
               c.team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               c.approval_status_type,
               cia.approval_status_type,
               c.more_info_comments,
               c.claim_group_id,
               cia.approver_comments,
               c.non_winner_comments,
               cia.notification_date
--               c.is_opt_out_of_awards--05/05/2017
        UNION ALL
        SELECT c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               c.approval_round,
               c.submission_date submitted_date,
               NULL participant_id,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               NULL nominee_first_name,
               NULL nominee_last_name,
               NULL country_name,
               NULL org_name,
               NULL job_position_name,
               NULL department_name,
               NULL avator_url,
               c.team_name,
               c.team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               c.approval_status_type,
               c.more_info_comments,
               c.claim_group_id,
               NULL award_amount,
               COUNT(1) team_cnt,
               null winner_more_info_comments,
               c.non_winner_comments,
               NULL notification_date,
               NULL is_opt_out_of_awards
          FROM nominee c
         WHERE c.team_name IS NOT NULL
           AND ( (p_in_status IS NULL 
                      AND((c.approval_status_type = 'pend' AND TRUNC(c.submission_date) BETWEEN NVL(p_in_submit_start_date,TRUNC(c.submission_date)) AND NVL(p_in_submit_end_date,TRUNC(SYSDATE))  AND c.approval_round = NVL(p_in_level_number,c.approval_round))))
                  OR ((c.approval_status_type = 'pend' and c.approval_status_type  IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND TRUNC(c.submission_date) BETWEEN NVL(p_in_submit_start_date,TRUNC(c.submission_date)) AND NVL(p_in_submit_end_date,TRUNC(SYSDATE))  AND c.approval_round = NVL(p_in_level_number,c.approval_round)))
                )
         GROUP BY c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               c.approval_round,
               c.approval_round,
               c.submission_date,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               c.team_name,
               c.team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               c.approval_status_type,
               c.approval_status_type,
               --c.award_amount,     --03/10/2017
               c.more_info_comments,
               c.claim_group_id,
               c.non_winner_comments
         UNION ALL
        SELECT c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               cia.approval_round,
               c.submission_date submitted_date,
               c.participant_id,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               c.nominee_first_name,
               c.nominee_last_name,
               c.country_name,
               c.org_name,
               c.job_position_name,
               c.department_name,
               c.avator_url,
               c.team_name,
               NULL team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               cia.approval_status_type,
               c.more_info_comments,
               c.claim_group_id,
               c.award_amount,
               0 team_cnt,
               cia.approver_comments winner_more_info_comments,
               c.non_winner_comments,
               cia.notification_date,
               c.is_opt_out_of_awards
         FROM nominee c,
              claim_item_approver cia
        WHERE (c.claim_item_id = cia.claim_item_id 
           OR c.claim_group_id = cia.claim_group_id )
          AND c.team_name IS NULL      
          AND ( (p_in_status IS NULL 
                      AND( (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NOT NULL AND TRUNC(cia.date_approved) BETWEEN c.time_period_start_date AND c.time_period_end_date  ))
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND p_in_time_period_id IS NULL AND p_in_submit_start_date IS NULL AND p_in_submit_end_date IS NULL )
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NULL AND TRUNC(cia.date_approved) BETWEEN p_in_submit_start_date AND NVL(p_in_submit_end_date,TRUNC(SYSDATE)) ))
                      ))
                  OR ((cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND  cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NOT NULL AND TRUNC(cia.date_approved) BETWEEN c.time_period_start_date AND c.time_period_end_date  ))
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND p_in_time_period_id IS NULL AND p_in_submit_start_date IS NULL AND p_in_submit_end_date IS NULL )
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NULL AND TRUNC(cia.date_approved) BETWEEN p_in_submit_start_date AND NVL(p_in_submit_end_date,TRUNC(SYSDATE)) ))
                      )
                )
        UNION ALL
        SELECT c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               c.approval_round,
               c.submission_date submitted_date,
               c.participant_id,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               c.nominee_first_name,
               c.nominee_last_name,
               c.country_name,
               c.org_name,
               c.job_position_name,
               c.department_name,
               c.avator_url,
               c.team_name,
               NULL team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               c.approval_status_type,
               c.more_info_comments,
               c.claim_group_id,
               c.award_amount,
               0 team_cnt,
               null winner_more_info_comments,
               c.non_winner_comments,
               NULL notification_date,
               c.is_opt_out_of_awards
         FROM nominee c
        WHERE c.team_name IS NULL      
          AND ( (p_in_status IS NULL 
                      AND((c.approval_status_type = 'pend' AND TRUNC(c.submission_date) BETWEEN NVL(p_in_submit_start_date,TRUNC(c.submission_date)) AND NVL(p_in_submit_end_date,TRUNC(SYSDATE))  AND c.approval_round = NVL(p_in_level_number,c.approval_round))))
                  OR ((c.approval_status_type = 'pend' and c.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND TRUNC(c.submission_date) BETWEEN NVL(p_in_submit_start_date,TRUNC(c.submission_date)) AND NVL(p_in_submit_end_date,TRUNC(SYSDATE))  AND c.approval_round = NVL(p_in_level_number,c.approval_round)))
                )
           ) 
       SELECT c.claim_id,
           c.claim_group_id,
           c.promotion_id,
           c.nomination_time_period_id,
           c.team_id,
           c.participant_id nominee_pax_id,
           c.submitter_id nominator_pax_id,
           pml.award_amount_fixed,
           pml.award_amount_type_fixed,
           CASE WHEN pml.award_amount_min IS NOT NULL AND pml.award_amount_max IS NOT NULL THEN 1
           ELSE 0
           END award_amount_type_range,
           CASE WHEN pml.award_amount_fixed IS NULL AND  pml.award_amount_min IS NULL AND pml.award_amount_max IS NULL THEN 1
           ELSE 0
           END award_amount_type_none,
               CASE WHEN award_payout_type = 'cash' THEN
             CASE WHEN c.approval_status_type IN ('approv', 'winner','pend') THEN  --11/03/2016 --07/10/2019 Bug 75174 Added pend status in the case statement
               CASE WHEN NVL(act.quantity ,c.award_amount) > 0 THEN NVL(act.quantity,c.award_amount) * v_currency_value
               WHEN NVL(act.cash_award_qty ,c.award_amount) > 0 THEN NVL(act.cash_award_qty,c.award_amount) * v_currency_value
               END
             ELSE c.award_amount * 1
             END
           ELSE-- award_payout_type='points'
             CASE WHEN c.approval_status_type IN ('approv', 'winner') THEN  --11/03/2016
               CASE WHEN NVL(act.quantity ,c.award_amount) > 0 THEN NVL(act.quantity ,c.award_amount)
               WHEN NVL(act.cash_award_qty ,c.award_amount) > 0 THEN NVL(act.cash_award_qty,c.award_amount)
               END
             ELSE c.award_amount
             END
           END award_amount,
           CASE WHEN p.approver_type = 'custom_approvers' AND a.approver_type ='award' THEN a.min_val 
           ELSE pml.award_amount_min
           END min_val , 
           CASE WHEN p.approver_type = 'custom_approvers' AND a.approver_type ='award' THEN a.max_val 
           ELSE pml.award_amount_max
           END  max_val,
           c.nominator_last_name||', '||c.nominator_first_name nominator_name,
           DECODE(team_name, NULL,c.nominee_last_name||', '||
           c.nominee_first_name,c.team_name) nominee_name,
           c.country_name,
           c.org_name,
           (select cms_name from temp_table_session where asset_code='picklist.positiontype.items' and cms_code = c.job_position_name) job_position_name,
           (select cms_name from temp_table_session where asset_code='picklist.department.type.items' and cms_code = c.department_name) department_name,
           cia.no_of_times_won,
           DECODE(NVL(cia.no_of_times_won,0),0,0,1) won_flag,
           cia.most_recent_date_won,
           CASE WHEN  c.approval_round = pml.level_index AND c.approval_status_type = 'winner' THEN c.time_period_name_asset_code
           ELSE NULL
           END recent_time_period_won,
           c.submitted_date,
           c.approval_round,
           pml.calculator_id,
           c.avator_url,
           DECODE(pn.evaluation_type, 'cumulative',1,0) is_cumulative_nomination,
           c.team_cnt,
           DECODE(c.team_id,NULL,0,1) is_team,
           c.ecard_image,
           c.card_video_url,
           c.card_video_image_url,
           c.submitter_comments,
           c.submitter_comments_lang_id,
           c.why_attachment_name,
           c.why_attachment_url,
           c.certificate_id,
           c.approval_status_type,
           (SELECT (SELECT cms_name FROM temp_table_session WHERE asset_code=level_label_asset_code)
              FROM promo_nomination_level pnl,
                   promo_nomination pn
             WHERE pnl.promotion_id = p_in_promotion_id
               AND pnl.promotion_id = pn.promotion_id
               AND pnl.level_index = (p_in_level_number - 1)
           ) previous_level_name,
           (SELECT (SELECT cms_name FROM temp_table_session WHERE asset_code=level_label_asset_code)
              FROM promo_nomination_level pnl,
                   promo_nomination pn
             WHERE pnl.promotion_id = p_in_promotion_id
               AND pnl.promotion_id = pn.promotion_id
               AND pnl.level_index = (p_in_level_number + 1)
           ) next_level_name,
           CASE WHEN pml.award_payout_type = 'points' THEN pn.last_pnt_bud_req_date
                WHEN pml.award_payout_type = 'cash' THEN pn.last_cash_bud_req_date END last_budget_request_date,
           (SELECT cms_name FROM temp_table_session WHERE asset_code = bs.cm_asset_code) budget_period_name,
           c.own_card_name,
           pml.quantity other_payout_quantity,
           pml.payout_description_asset_code,
           c.more_info_comments,
           c.winner_more_info_comments,
           c.non_winner_comments,
           c.notification_date,
           c.is_opt_out_of_awards nominee_is_opt_out_of_awards
      FROM (SELECT ci.claim_id, TRUNC(MAX(NVL(cia.notification_date,cia.date_approved))) most_recent_date_won,COUNT(1) no_of_times_won 
              FROM claim_item_approver cia, 
                   claim_item ci,
                   claim c--Added lookup to claim table to make sure to pick only the final level winners.
             WHERE (cia.claim_item_id = ci.claim_item_id OR c.claim_group_id = cia.claim_group_id)
               AND cia.approval_status_type = 'winner'
               AND ci.claim_id = c.claim_id
               AND c.is_open = 0
               AND c.approval_round = cia.approval_round
             GROUP BY ci.claim_id) cia,
           promotion p,
           promo_nomination pn,
           promo_nomination_level pml,
           budget_segment bs,
           approver_nom c,
           activity act,
           (SELECT DISTINCT a.user_id approver_user_id,ao.promotion_id, ao.approver_type, ao.approval_round,ac.min_val, ac.max_val
                FROM approver a,
                     approver_criteria ac,
                     approver_option ao
               WHERE ao.promotion_id = p_in_promotion_id
                 AND ao.approver_option_id = ac.approver_option_id
                 AND ac.approver_criteria_id = a.approver_criteria_id
                 AND ao.approver_type ='award') a
     WHERE c.promotion_id = pml.promotion_id(+)
       AND ((c.approval_round = pml.level_index AND pn.payout_level_type = 'eachLevel') OR (pn.payout_level_type = 'finalLevel' ))
       AND c.promotion_id = p.promotion_id
       AND p.promotion_id = pn.promotion_id
       AND (CASE WHEN v_payout_level_type = 'finalLevel' THEN NVL(p.award_budget_master_id,p.cash_budget_master_id)
                 WHEN v_payout_level_type = 'eachLevel' AND v_award_payout_type = 'points' THEN p.award_budget_master_id
                 WHEN v_payout_level_type = 'eachLevel' AND v_award_payout_type = 'cash' THEN p.cash_budget_master_id
                 ELSE NVL(p.award_budget_master_id,p.cash_budget_master_id) END) = bs.budget_master_id (+)
       AND (bs.budget_master_id IS NULL OR (bs.budget_master_id IS NOT NULL AND TRUNC(SYSDATE) BETWEEN bs.start_date AND NVL(bs.end_date,TRUNC(SYSDATE))))
       AND c.claim_id = cia.claim_id (+)
       AND c.promotion_id = a.promotion_id(+)
       AND c.approval_round = a.approval_round(+)
       AND c.claim_id = act.claim_id (+)
       AND c.participant_id = act.user_id (+)
       AND c.approval_round = act.approval_round(+)
       AND act.is_submitter (+) = 0
       AND EXISTS (SELECT 1
                     FROM claim_item_approver ciaa,
                          claim_item ci
                    WHERE (ciaa.approver_user_id = p_in_approver_id OR v_approver_is_admin = 1)
                      AND (ciaa.claim_item_id = ci.claim_item_id OR c.claim_group_id = ciaa.claim_group_id)
                      AND ci.claim_id = c.claim_id
                      AND ciaa.approval_round = c.approval_round
                    UNION ALL
                   SELECT 1
                     FROM claim_approver_snapshot cas, claim c2--bug # 69156
                    WHERE (cas.approver_user_id = p_in_approver_id OR v_approver_is_admin = 1)
                      AND (cas.claim_id = c.claim_id OR cas.claim_group_id = c.claim_group_id)
                      AND (cas.approver_user_id = a.approver_user_id OR a.approver_user_id IS NULL)
                      AND c2.approval_round = c.approval_round 
                      AND (cas.claim_id = c2.claim_id OR cas.claim_group_id = c2.claim_group_id)
                  );

BEGIN

  p_out_returncode := 0;
  
  v_approval_level := (p_in_level_number -1);
  
  v_stage := 'Get approver type';
  SELECT approver_type
    INTO v_approver_type
    FROM promotion
   WHERE promotion_id = p_in_promotion_id;

  v_stage := 'Get final level number';
  SELECT approval_node_levels
    INTO v_final_level_number
    FROM promotion
   WHERE promotion_id = p_in_promotion_id;
   
  SELECT request_more_budget,
         award_active,
         payout_level_type
    INTO v_request_more_budget,
         v_award_active,
         v_payout_level_type
    FROM promo_nomination 
   WHERE promotion_id = p_in_promotion_id;
     
  SELECT COUNT(1) 
    INTO v_level_has_award 
    FROM promo_nomination_level 
   WHERE promotion_id = p_in_promotion_id 
     AND level_index = p_in_level_number;
  
  SELECT COUNT(1)
    INTO v_approver_is_admin
    FROM application_user
   WHERE user_id = p_in_approver_id
     AND user_type = 'bi';
   
  BEGIN
    SELECT calculator_id,
           award_payout_type
      INTO v_calculator_id,
           v_award_payout_type 
      FROM promo_nomination_level 
     WHERE promotion_id = p_in_promotion_id 
       AND level_index = p_in_level_number;
  EXCEPTION 
     WHEN OTHERS THEN
       v_calculator_id :=NULL;
       v_award_payout_type := NULL;
  END;   
   
  v_stage := 'Get currency value';
  BEGIN
   SELECT ccc.bpom_entered_rate
     INTO v_currency_value
     FROM user_address ua,
          country c,
          cash_currency_current ccc 
    WHERE ua.user_id = p_in_approver_id
      AND is_primary = 1
      AND ua.country_id = c.country_id
      --AND c.country_code = ccc.to_cur  --07/10/2019 Bug 75174 commented
      AND c.currency_code = ccc.to_cur ; --07/10/2019 Bug 75174 Added
  EXCEPTION
    WHEN OTHERS THEN
      v_currency_value := 1;
  END;   
  
  BEGIN
    SELECT string_val 
      INTO v_default_locale
      FROM os_propertyset
     WHERE entity_name = 'default.language';
  EXCEPTION
     WHEN OTHERS THEN
       v_default_locale := 'en_US';
  END;
  
  DELETE temp_table_session;
  
  v_stage:= 'Insert temp_table_session';
  INSERT INTO temp_table_session
     SELECT asset_code,cms_value,key
       FROM (SELECT asset_code,cms_value,key ,  RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN ('COUNTRY_NAME', 'PROMOTION_LEVEL_LABEL_NAME_' , 'BUDGET_PERIOD_NAME', 'PROMOTION_TIME_PERIOD_NAME_','PAYOUT_DESCRIPTION_')
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT asset_code, cms_value, key, RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value
              WHERE asset_code IN (SELECT promo_name_asset_code FROM promotion WHERE promotion_type = 'nomination')
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT asset_code,cms_name,cms_code,  RANK() OVER(PARTITION BY asset_code, cms_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn 
               FROM vw_cms_code_value 
              WHERE asset_code IN ( 'picklist.promo.nomination.behavior.items', 'picklist.positiontype.items','picklist.department.type.items')
                AND locale IN (v_default_locale, p_in_locale)
                AND cms_status = 'true' 
              UNION ALL
              SELECT asset_code,cms_value,key ,RANK() OVER(PARTITION BY key ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN (SELECT 'ELEMENTLABEL_'||cm_key_fragment from claim_form_step_element )
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT cf.cm_asset_code,cms_name, cms_code||'_'||cfse.cm_key_fragment,RANK() OVER(PARTITION BY cf.cm_asset_code,cms_code||'_'||cfse.cm_key_fragment ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_code_value  vw, claim_form_step_element cfse, 
                    claim_form_step cfs, claim_form cf, promotion p
              WHERE asset_code =  cfse.selection_pick_list_name 
                AND cfse.claim_form_step_id = cfs.claim_form_step_id 
                AND cfs.claim_form_id = cf.claim_form_id
                and cf.claim_form_id = p.claim_form_id
                AND p.promotion_id = p_in_promotion_id
                AND locale IN (v_default_locale, p_in_locale)
                AND cms_status = 'true' 
                AND claim_form_element_type_code <> 'copy'
              UNION ALL
             SELECT asset_code,cms_value,key ,RANK() OVER(PARTITION BY key,cms_value ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN (SELECT 'LABELTRUE_'||cm_key_fragment from claim_form_step_element 
                            UNION SELECT 'LABELFALSE_'||cm_key_fragment from claim_form_step_element )
                AND locale IN (v_default_locale, p_in_locale)
               ) WHERE rn = 1;
   
  DELETE TEMP_HIER_LEVEL_NODES;
  
  v_stage := 'Insert owners for the upper level nodes';
  INSERT INTO TEMP_HIER_LEVEL_NODES
  SELECT re.parent_node_id,
         re.node_id,
         re.node_path,
         re.node_type_id,
         row_number() over(partition by re.node_id order by hier_level) hier_level,
         un.user_id 
    FROM (SELECT s.parent_node_id,
                 s.node_id,
                 s.node_path,
                 n.node_type_id,
                 REGEXP_COUNT(SUBSTR(node_path, 1, INSTR('/'||node_path||'/', '/'||RTRIM(LTRIM(s.parent_node_id, '/'), '/')||'/') ), '/')   hier_level 
           FROM (SELECT * 
                   FROM (SELECT pv.COLUMN_VALUE AS parent_node_id,
                                np.node_id AS node_id,node_path
                           FROM ( -- get node hierarchy path
                                 SELECT h.node_id,
                                        LEVEL AS hier_level,
                                        node_type_id,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                   FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                               ) np,
                               -- parse node path into individual nodes
                               -- pivoting the node path into separate records
                               TABLE( CAST( MULTISET(
                                  SELECT TO_NUMBER(
                                            SUBSTR(np.node_path,
                                                   INSTR(np.node_path, '/', 1, LEVEL)+1,
                                                   INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1
                                            )
                                         )
                                    FROM dual
                                 CONNECT BY LEVEL <= np.hier_level
                               ) AS sys.odcinumberlist ) ) pv
                        )
                ) s,
                node n
          WHERE s.node_id <> s.parent_node_id
            AND s.parent_node_id = n.node_id
         ) re,
         user_node un,
         application_user au
   WHERE re.parent_node_id = un.node_id
     AND un.is_primary = 1
     AND un.user_id = au.user_id
     AND au.is_active = 1
     AND un.role = 'own';
  
  
  v_stage := 'Get central budget amount for budget master id ';
  IF v_level_has_award = 0 THEN
    SELECT SUM(current_value) current_value
      INTO v_out_budget_balance
      FROM budget_segment bs,
           budget b,
           promotion p
     WHERE p.promotion_id = p_in_promotion_id
       AND NVL(p.award_budget_master_id, p.cash_budget_master_id) = bs.budget_master_id 
       AND TRUNC(SYSDATE) BETWEEN bs.start_date AND NVL(bs.end_date,TRUNC(SYSDATE))
       AND bs.budget_segment_id = b.budget_segment_id;
  ELSE
    SELECT SUM(current_value) current_value
      INTO v_out_budget_balance
      FROM budget_segment bs,
           budget b,
           promotion p,
           promo_nomination_level pnl
     WHERE p.promotion_id = p_in_promotion_id
       AND p.promotion_id = pnl.promotion_id
       AND pnl.level_index = p_in_level_number
       AND ((award_payout_type = 'cash' AND p.cash_budget_master_id = bs.budget_master_id)
           OR (award_payout_type = 'points' AND p.award_budget_master_id = bs.budget_master_id))
       AND TRUNC(SYSDATE) BETWEEN bs.start_date AND NVL(bs.end_date,TRUNC(SYSDATE))
       AND bs.budget_segment_id = b.budget_segment_id;
 
 
  END IF;
     
  IF v_approver_type LIKE '%_node_owner' THEN

    v_stage := 'Get previous level approvers for nominator node owner';
    OPEN p_out_prev_lvl_approvers FOR
       SELECT au.user_id approver_id, au.first_name,au.last_name
        FROM (SELECT DISTINCT cia.approver_user_id,un.node_id approver_node_id
                FROM claim c,
                     claim_item ci,
                     claim_item_approver cia,
                     user_node un
               WHERE c.promotion_id = p_in_promotion_id
                 AND c.is_open = 1
                 AND c.claim_id = ci.claim_id
                 AND (cia.claim_item_id = ci.claim_item_id OR cia.claim_group_id = c.claim_group_id)
                 AND cia.approval_round = v_approval_level
                 AND cia.approver_user_id = un.user_id
                 AND un.is_primary = 1
             ) ca,
             temp_hier_level_nodes th,
             application_user au 
       WHERE ca.approver_node_id  = th.node_id
         AND th.hier_level = (SELECT MAX(tl.hier_level)
                                FROM temp_hier_level_nodes tl
                               WHERE tl.node_id =th.node_id )
         AND th.user_id = p_in_approver_id
         AND ca.approver_user_id = au.user_id;
         
    v_stage := 'Get next level approvers for nominator node owner';
    OPEN p_out_next_lvl_approvers FOR
      SELECT au.user_id approver_id, au.first_name,au.last_name
        FROM (SELECT DISTINCT cas.source_node_id approver_node_id
                FROM claim c,
                     claim_approver_snapshot cas
               WHERE c.promotion_id = p_in_promotion_id
                 AND c.is_open = 1
                 AND (cas.claim_id = c.claim_id OR cas.claim_group_id = c.claim_group_id)
                 AND c.approval_round = p_in_level_number
                 AND cas.approver_user_id = p_in_approver_id
                 AND p_in_level_number <> v_final_level_number
             ) ca,
             temp_hier_level_nodes th,
             application_user au 
       WHERE ca.approver_node_id  = th.node_id
         AND th.hier_level = (SELECT MAX(tl.hier_level)
                                FROM temp_hier_level_nodes tl
                               WHERE tl.node_id =th.node_id )
         AND th.user_id = au.user_id;
    /*v_stage := 'Get previous level pending nomination count for nominator/nominee node owner';
    SELECT COUNT(1)
      INTO p_out_prev_pend_nom_cnt 
      FROM claim c,
           claim_approver_snapshot cas,
           temp_hier_level_nodes th
     WHERE c.promotion_id = p_in_promotion_id
       AND c.is_open = 1
       AND c.claim_id = cas.claim_id
       AND c.approval_round = v_approval_level
       AND cas.source_node_id  = th.node_id
       AND th.hier_level = (SELECT MAX(tl.hier_level)
                              FROM temp_hier_level_nodes tl
                             WHERE tl.node_id =th.node_id )
       AND th.user_id = p_in_approver_id;
       
    v_stage := 'Get next level pending nomination count for nominator/nominee node owner';
    SELECT COUNT(1)
      INTO p_out_next_pend_nom_cnt 
      FROM claim c,
           claim_approver_snapshot cas,
           temp_hier_level_nodes th
     WHERE c.promotion_id = p_in_promotion_id
       AND c.is_open = 1
       AND c.claim_id = cas.claim_id
       AND c.approval_round = p_in_level_number
       AND cas.approver_user_id = p_in_approver_id
       AND p_in_level_number <> v_final_level_number
       AND cas.source_node_id  = th.node_id
       AND th.hier_level = (SELECT MAX(tl.hier_level)
                              FROM temp_hier_level_nodes tl
                             WHERE tl.node_id =th.node_id );*/
         
  ELSIF v_approver_type LIKE '%_node_owner_by_type' THEN
  
    v_stage := 'Get previous level approvers for nominator node owner by type';
    OPEN p_out_prev_lvl_approvers FOR
      SELECT au.user_id approver_id, au.first_name,au.last_name
        FROM (SELECT DISTINCT cia.approver_user_id,un.node_id approver_node_id
                FROM claim c,
                     claim_item ci,
                     claim_item_approver cia,
                     user_node un
               WHERE c.promotion_id = p_in_promotion_id
                 AND c.is_open = 1
                 AND c.claim_id = ci.claim_id
                 AND (cia.claim_item_id = ci.claim_item_id OR cia.claim_group_id = c.claim_group_id)
                 AND cia.approval_round = v_approval_level
                 AND cia.approver_user_id = un.user_id
                 AND un.is_primary = 1
             ) ca,
             temp_hier_level_nodes th,
             node n,
             application_user au 
       WHERE ca.approver_node_id  = th.node_id
         AND th.hier_level = (SELECT MAX(tl.hier_level)
                                FROM temp_hier_level_nodes tl
                               WHERE tl.node_id =th.node_id
                                 AND tl.node_type_id = th.node_type_id )
         AND th.user_id = p_in_approver_id
         AND th.node_id = n.node_id
         AND n.node_type_id = th.node_type_id
         AND ca.approver_user_id = au.user_id;
         
    
    v_stage := 'Get next level approvers for nominator node owner by type';
    OPEN p_out_next_lvl_approvers FOR
      SELECT au.user_id approver_id, au.first_name,au.last_name
        FROM (SELECT DISTINCT cas.source_node_id approver_node_id
                FROM claim c,
                     claim_approver_snapshot cas
               WHERE c.promotion_id = p_in_promotion_id
                 AND c.is_open = 1
                 AND (cas.claim_id = c.claim_id OR cas.claim_group_id = c.claim_group_id)
                 AND c.approval_round = p_in_level_number
                 AND cas.approver_user_id = p_in_approver_id
                 AND p_in_level_number <> v_final_level_number
             ) ca,
             temp_hier_level_nodes th,
             application_user au,
             node n 
       WHERE ca.approver_node_id  = th.node_id
         AND th.hier_level = (SELECT MAX(tl.hier_level)
                                FROM temp_hier_level_nodes tl
                               WHERE tl.node_id =th.node_id
                                 AND tl.node_type_id = th.node_type_id )
         AND th.node_id = n.node_id
         AND n.node_type_id = th.node_type_id
         AND th.user_id = au.user_id;
         
    /*v_stage := 'Get previous level pending nomination count for nominator/nominee node owner type';
    SELECT COUNT(1)
      INTO p_out_prev_pend_nom_cnt 
      FROM claim c,
           claim_approver_snapshot cas,
           temp_hier_level_nodes th,
           node n 
     WHERE c.promotion_id = p_in_promotion_id
       AND c.is_open = 1
       AND c.claim_id = cas.claim_id
       AND c.approval_round = v_approval_level
       AND cas.source_node_id  = th.node_id
       AND th.hier_level = (SELECT MAX(tl.hier_level)
                              FROM temp_hier_level_nodes tl
                             WHERE tl.node_id =th.node_id )
       AND th.user_id = p_in_approver_id
       AND th.node_id = n.node_id
       AND n.node_type_id = th.node_type_id;
       
    v_stage := 'Get next level pending nomination count for nominator/nominee node owner type';
    SELECT COUNT(1)
      INTO p_out_next_pend_nom_cnt 
      FROM claim c,
           claim_approver_snapshot cas,
           temp_hier_level_nodes th,
           node n 
     WHERE c.promotion_id = p_in_promotion_id
       AND c.is_open = 1
       AND c.claim_id = cas.claim_id
       AND c.approval_round = p_in_level_number
       AND cas.approver_user_id = p_in_approver_id
       AND p_in_level_number <> v_final_level_number
       AND cas.source_node_id  = th.node_id
       AND th.hier_level = (SELECT MAX(tl.hier_level)
                              FROM temp_hier_level_nodes tl
                             WHERE tl.node_id =th.node_id )
       AND th.node_id = n.node_id
       AND n.node_type_id = th.node_type_id;*/
         
  
  ELSE 
    
    v_stage := 'Get previous level approvers for custom approver';
    OPEN p_out_prev_lvl_approvers FOR
      SELECT DISTINCT au.user_id approver_id, au.first_name,au.last_name
        FROM approver a,
             approver_criteria ac,
             approver_option ao,
             application_user au
       WHERE ao.promotion_id = p_in_promotion_id
         AND ao.approver_option_id = ac.approver_option_id
         AND ac.approver_criteria_id = a.approver_criteria_id
         AND a.user_id = au.user_id
         AND ao.approval_round = v_approval_level;
         
    v_stage := 'Get next level approvers for custom approver';
    OPEN p_out_next_lvl_approvers FOR
      SELECT DISTINCT au.user_id approver_id, au.first_name,au.last_name
        FROM approver a,
             approver_criteria ac,
             approver_option ao,
             application_user au
       WHERE ao.promotion_id = p_in_promotion_id
         AND ao.approver_option_id = ac.approver_option_id
         AND ac.approver_criteria_id = a.approver_criteria_id
         AND a.user_id = au.user_id
         AND ao.approval_round = (p_in_level_number + 1); 
              
  END IF;
  

  
  DELETE TMP_APPROVER_NOMI_DETAIL;
   
  OPEN cur_appr(v_approver_is_admin, v_currency_value,v_payout_level_type, v_award_payout_type);

  v_stage := 'Fetching data from cursor...';
  LOOP    

    FETCH cur_appr BULK COLLECT          
     INTO v_rec_appr ;
 
        IF v_rec_appr.COUNT > 0 THEN    
           FORALL indx IN 1 .. v_rec_appr.COUNT         
           
           INSERT INTO TMP_APPROVER_NOMI_DETAIL
                   (
                    claim_id ,             
                    claim_group_id,       
                    promotion_id,                 
                    nomination_time_period_id,    
                    team_id,                      
                    nominee_pax_id,               
                    nominator_pax_id,             
                    award_amount_fixed,           
                    award_amount_type_fixed,  
                    award_amount_type_range,
                    award_amount_type_none,    
                    award_amount,                 
                    min_val,                      
                    max_val,              
                    nominator_name,               
                    nominee_name,                 
                    country_name,                 
                    org_name,                     
                    job_position_name,            
                    department_name,              
                    no_of_times_won,              
                    won_flag,                    
                    most_recent_date_won,         
                    recent_time_period_won,       
                    submitted_date,               
                    level_index,               
                    calcuaLtor_id,                
                    avator_url,                   
                    is_cumulative_nomination,     
                    team_cnt,                     
                    is_team ,                     
                    ecard_image,                  
                    card_video_url,               
                    card_video_image_url,         
                    submitter_comments,           
                    submitter_comments_lang_id,   
                    why_attachment_name,          
                    why_attachment_url,           
                    certificate_id,               
                    approval_status_type,         
                    previous_level_name,          
                    next_level_name,              
                    last_budget_request_date,     
                    budget_period_name,           
                    own_card_name,                
                    other_payout_quantity,        
                    payout_description_asset_code,
                    more_info_comments  ,
                    winner_more_info_comments,
                    non_winner_comments ,
                    notification_date,
                    nominee_is_opt_out_of_awards      
                    )
           VALUES (
                    v_rec_appr(indx).claim_id ,      
                    v_rec_appr(indx).claim_group_id ,              
                    v_rec_appr(indx).promotion_id,                 
                    v_rec_appr(indx).nomination_time_period_id,    
                    v_rec_appr(indx).team_id ,                     
                    v_rec_appr(indx).nominee_pax_id   ,            
                    v_rec_appr(indx).nominator_pax_id,            
                    v_rec_appr(indx).award_amount_fixed,           
                    v_rec_appr(indx).award_amount_type_fixed, 
                    v_rec_appr(indx).award_amount_type_range,
                    v_rec_appr(indx).award_amount_type_none,     
                    v_rec_appr(indx).award_amount,                 
                    v_rec_appr(indx).min_val,                      
                    v_rec_appr(indx).max_val,                      
                    v_rec_appr(indx).nominator_name,               
                    v_rec_appr(indx).nominee_name,                 
                    v_rec_appr(indx).country_name,                 
                    v_rec_appr(indx).org_name,                     
                    v_rec_appr(indx).job_position_name,            
                    v_rec_appr(indx).department_name,              
                    v_rec_appr(indx).no_of_times_won,              
                    v_rec_appr(indx).won_flag,                    
                    v_rec_appr(indx).most_recent_date_won,         
                    v_rec_appr(indx).recent_time_period_won,       
                    v_rec_appr(indx).submitted_date,               
                    v_rec_appr(indx).level_index,               
                    v_rec_appr(indx).calcualtor_id,                
                    v_rec_appr(indx).avator_url,                   
                    v_rec_appr(indx).is_cumulative_nomination,     
                    v_rec_appr(indx).team_cnt,                     
                    v_rec_appr(indx).is_team ,                     
                    v_rec_appr(indx).ecard_image,                  
                    v_rec_appr(indx).card_video_url,               
                    v_rec_appr(indx).card_video_image_url,         
                    v_rec_appr(indx).submitter_comments,           
                    v_rec_appr(indx).submitter_comments_lang_id,   
                    v_rec_appr(indx).why_attachment_name,          
                    v_rec_appr(indx).why_attachment_url,           
                    v_rec_appr(indx).certificate_id,               
                    v_rec_appr(indx).approval_status_type,        
                    v_rec_appr(indx).previous_level_name,          
                    v_rec_appr(indx).next_level_name,              
                    v_rec_appr(indx).last_budget_request_date,     
                    v_rec_appr(indx).budget_period_name,           
                    v_rec_appr(indx).own_card_name,                
                    v_rec_appr(indx).other_payout_quantity,        
                    v_rec_appr(indx).payout_description_asset_code,
                    v_rec_appr(indx).more_info_comments    ,
                    v_rec_appr(indx).winner_more_info_comments,
                    v_rec_appr(indx).non_winner_comments ,
                    v_rec_appr(indx).notification_date,
                    v_rec_appr(indx).nominee_is_opt_out_of_awards        
                   );     
        END IF; 
        
        EXIT WHEN cur_appr%NOTFOUND;   

  END LOOP; 
  
  CLOSE cur_appr;
   
  v_stage:= 'Return p_out_nomin_detail';
  OPEN p_out_nomin_detail FOR --Return for Non-cumulative promotion
   SELECT s.*
    FROM (SELECT -- build row number sort field
           ROW_NUMBER() OVER 
           (ORDER BY
             -- sort on field ascending
             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominee_name')      THEN UPPER(res.nominee_name)    END) ASC
           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'submitted_date')    THEN res.submitted_date         END) ASC
           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominator_name')    THEN UPPER(res.nominator_name)  END) ASC
           , (CASE WHEN (p_in_sortedBy = 'asc')  THEN claim_id  END) ASC
             -- sort on field descending
           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominee_name')     THEN UPPER(res.nominee_name)    END) DESC
           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'submitted_date')   THEN res.submitted_date         END) DESC
           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominator_name')   THEN UPPER(res.nominator_name)  END) DESC
           , (CASE WHEN (p_in_sortedBy = 'desc') THEN claim_id  END) DESC           -- 02/08/2017  Bug 71330 changed ASC to DESC
           ) AS rn, 
           res.*
      FROM (SELECT claim_id,
                   team_id,
                   nominee_pax_id,
                   nominator_pax_id,
                   nominee_name,
                   org_name,
                   job_position_name,
                   department_name,
                   country_name,
                   won_flag,
                   no_of_times_won,
                   0 exceed_flag,--Added this for temporary purpose as Java and FED are still expecting this column
                   submitted_date,
                   nominator_name,
                   level_index,
                   avator_url,
                   is_cumulative_nomination,
                   team_cnt,
                   is_team,
                   CASE WHEN award_amount_type_fixed = 0 and level_index >1 AND pn.recommended_award = 0 AND approval_status_type NOT IN ('approv', 'winner') THEN NULL --11/03/2016
                        WHEN award_amount_type_fixed = 1 AND approval_status_type NOT IN ('approv', 'winner') THEN award_amount_fixed ELSE award_amount END award_amount, --11/03/2016
                   ecard_image,
                   card_video_url,
                   card_video_image_url,
                   submitter_comments,
                   submitter_comments_lang_id,
                   why_attachment_name,
                   why_attachment_url,
                   certificate_id,
                   approval_status_type status,
                   null total_promotion_count, --Added this for temporary purpose as Java and FED are still expecting this column
                   previous_level_name, 
                   next_level_name,  
                   budget_period_name,  
                   last_budget_request_date,
                   own_card_name,
                   most_recent_date_won,
                   (SELECT cms_name FROM temp_table_session WHERE asset_code = recent_time_period_won) recent_time_period_won,
                   other_payout_quantity,
                   (SELECT cms_name FROM temp_table_session WHERE asset_code = payout_description_asset_code) payout_description_asset_code,
                   more_info_comments,
                   DECODE(approval_status_type,'non_winner',non_winner_comments) denial_reason,
                   DECODE(approval_status_type,'approv' , winner_more_info_comments,'winner',winner_more_info_comments) winner_message,
                   DECODE(approval_status_type,'more_info' , winner_more_info_comments) moreinfo_message,
                   notification_date,
                   (SELECT (SELECT cms_name FROM temp_table_session WHERE asset_code=level_label_asset_code)
                      FROM promo_nomination_level pnl,
                           promo_nomination pn
                     WHERE pnl.promotion_id = p_in_promotion_id
                       AND pnl.promotion_id = pn.promotion_id
                       AND pnl.level_index = an.level_index
                   ) level_name,
                   an.nominee_is_opt_out_of_awards
              FROM tmp_approver_nomi_detail an,
                   promo_nomination pn
             WHERE an.is_cumulative_nomination = 0
               AND an.promotion_id = pn.promotion_id
           ) res
      ) s -- sort number result set
    WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
      AND s.rn < TO_NUMBER(p_in_rowNumEnd)
    ORDER BY s.rn;
    
  v_stage:= 'Return p_out_cummula_nomi_dtl';
  OPEN p_out_cummula_nomi_dtl FOR --Return for cumulative promotion
   SELECT s.*
    FROM (SELECT -- build row number sort field
           ROW_NUMBER() OVER 
           (ORDER BY
             -- sort on field ascending
             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominee_name')      THEN UPPER(res.nominee_name)    END) ASC
             -- sort on field descending
           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominee_name')     THEN UPPER(res.nominee_name)    END) DESC
           ) AS rn, 
           res.*
      FROM (SELECT LISTAGG(claim_id,',') WITHIN GROUP (ORDER BY claim_id) claim_id,
                   status,
                   nominee_pax_id,
                   nominee_name,
                   org_name,
                   department_name,
                   job_position_name,
                   country_name,
                   won_flag,
                   no_of_times_won,
                   exceed_flag,--Added this for temporary purpose as Java and FED are still expecting this column
                   level_index,
                   avator_url,
                   award_amount,
                   recent_time_period_won,
                   most_recent_date_won,
                   nominator_count,
                   denial_reason,
                   winner_message,
                   moreinfo_message,
                   notification_date,
                   level_name,
                   claim_group_id,
                   nominee_is_opt_out_of_awards
             FROM ( SELECT an.claim_id,
                           approval_status_type status,
                           nominee_pax_id,
                           an.claim_group_id,
                           nominee_name,
                           org_name,
                           department_name,
                           job_position_name,
                           country_name,
                           won_flag,
                           no_of_times_won,
                           0 exceed_flag,--Added this for temporary purpose as Java and FED are still expecting this column
                           level_index,
                           avator_url,
                           CASE WHEN award_amount_type_fixed = 0 and level_index >1 AND pn.recommended_award = 0 AND approval_status_type NOT IN ('approv', 'winner') THEN NULL  --11/03/2016
                                WHEN award_amount_type_fixed = 1 AND approval_status_type NOT IN ('approv', 'winner') THEN award_amount_fixed                                    --11/03/2016
                                WHEN NVL(act.cash_award_qty ,0) > 0 AND approval_status_type IN ('approv', 'winner') THEN NVL(act.cash_award_qty ,0) * v_currency_value          --11/03/2016
                                WHEN NVL(act.quantity ,0) > 0 AND approval_status_type IN ('approv', 'winner') THEN NVL(act.quantity ,0)                                         --11/03/2016
                                ELSE award_amount 
                           END award_amount,
                           (SELECT cms_name FROM temp_table_session WHERE asset_code = recent_time_period_won) recent_time_period_won,
                           most_recent_date_won,
                           COUNT(*) OVER(PARTITION BY nominee_name,an.claim_group_id) nominator_count,
                           DECODE(approval_status_type,'non_winner',non_winner_comments) denial_reason,
                           DECODE(approval_status_type,'approv' , winner_more_info_comments,'winner',winner_more_info_comments) winner_message,
                           DECODE(approval_status_type,'more_info' , winner_more_info_comments) moreinfo_message,
                           notification_date,
                           (SELECT (SELECT cms_name FROM temp_table_session WHERE asset_code=level_label_asset_code)
                              FROM promo_nomination_level pnl,
                                   promo_nomination pn
                             WHERE pnl.promotion_id = p_in_promotion_id
                               AND pnl.promotion_id = pn.promotion_id
                               AND pnl.level_index = an.level_index
                           ) level_name,
                           an.nominee_is_opt_out_of_awards
                      FROM tmp_approver_nomi_detail an,
                           promo_nomination pn,
                           activity act
                     WHERE an.is_cumulative_nomination = 1
                       AND an.promotion_id = pn.promotion_id
                       AND an.claim_group_id = act.claim_group_id (+)
                       AND an.nominee_pax_id = act.user_id (+)
                       AND an.level_index = act.approval_round(+)
                       AND act.is_submitter (+) = 0
                 )
        GROUP BY status,
                 nominee_pax_id,
                 claim_group_id,
                 nominee_name,
                 org_name,
                 department_name,
                 job_position_name,
                 country_name,
                 won_flag,
                 no_of_times_won,
                 exceed_flag,--Added this for temporary purpose as Java and FED are still expecting this column
                 level_index,
                 avator_url,
                 award_amount,
                 recent_time_period_won,
                 most_recent_date_won,
                 nominator_count,
                 denial_reason,
                 winner_message,
                 moreinfo_message,
                 notification_date,
                 level_name,
                 nominee_is_opt_out_of_awards
           ) res 
      ) s -- sort number result set
    WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
      AND s.rn < TO_NUMBER(p_in_rowNumEnd)
    ORDER BY s.rn;
               
    v_stage := 'Return team members detail for approver';
    OPEN p_out_team_members_dtl FOR
      SELECT ta.claim_id,
           re.user_id,
           re.user_name,
           re.country_name,
           re.org_name,
           (select cms_name from temp_table_session where asset_code='picklist.positiontype.items' and cms_code = re.position_type ) job_position_name,
           (select cms_name from temp_table_session where asset_code='picklist.department.type.items' and cms_code = re.department_type ) department_type,
           CASE WHEN ta.award_amount_type_fixed = 0 and ta.level_index >1 AND pn.recommended_award = 0 AND ta.approval_status_type NOT IN ('approv', 'winner') THEN NULL --11/03/2016
                WHEN ta.award_amount_type_fixed = 1 AND ta.approval_status_type NOT IN ('approv', 'winner') THEN ta.award_amount_fixed                                   --11/03/2016
                WHEN NVL(act.cash_award_qty ,0) > 0 AND ta.approval_status_type IN ('approv', 'winner') THEN NVL(act.cash_award_qty ,0) * v_currency_value               --11/03/2016
                WHEN NVL(act.quantity ,0) > 0 AND ta.approval_status_type IN ('approv', 'winner') THEN NVL(act.quantity ,0)                                              --11/03/2016
                ELSE award_amount 
           END award_amount,
           re.is_opt_out_of_awards nominee_is_opt_out_of_awards
      FROM (SELECT s.*
              FROM (SELECT ROW_NUMBER() OVER 
                           (ORDER BY
                             -- sort on field ascending
                             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominee_name')      THEN UPPER(nominee_name)    END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'submitted_date')    THEN submitted_date         END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominator_name')    THEN UPPER(nominator_name)  END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc')  THEN claim_id  END) ASC        -- 02/08/2017  Bug 71330
                             -- sort on field descending
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominee_name')     THEN UPPER(nominee_name)    END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'submitted_date')   THEN submitted_date         END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominator_name')   THEN UPPER(nominator_name)  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc') THEN claim_id  END) DESC       -- 02/08/2017  Bug 71330
                           ) AS rn, 
                           res.*
                      FROM tmp_approver_nomi_detail res
                  ) s -- sort number result set
            WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
              AND s.rn < TO_NUMBER(p_in_rowNumEnd)
           )ta,
           promo_nomination  pn,
           (SELECT res.claim_id,
                   res.level_index,
                   cr.participant_id,
                   au.user_id,
                   au.last_name||', '||au.first_name  user_name,
                   c.country_code  country_name,
                   n.name org_name,
                   vw.position_type ,
                   vw.department_type,
                   p.is_opt_out_of_awards
              FROM tmp_approver_nomi_detail res,
                   claim_item ci,
                   claim_recipient cr,
                   application_user au,
                   user_address ua,
                   country c,
                   node n,
                   vw_curr_pax_employer vw,
                   participant p
             WHERE res.claim_id =  ci.claim_id
               AND res.team_id IS NOT NULL
               AND ci.claim_item_id = cr.claim_item_id
               AND cr.participant_id = au.user_id
               AND au.is_active = 1
               AND au.user_id = ua.user_id (+)
               AND ua.is_primary (+) = 1
               AND ua.country_id = c.country_id(+)
               AND cr.participant_id = vw.user_id (+)
               AND cr.node_id = n.node_id
               AND au.user_id = p.user_id
            ) re,
           activity act
     WHERE ta.promotion_id = pn.promotion_id
        AND ta.claim_id = re.claim_id
        AND re.claim_id = act.claim_id (+)
        AND re.participant_id = act.user_id (+)
        AND re.level_index = act.approval_round(+)
        AND act.is_submitter (+) = 0;
       
  v_stage := 'Return behaviors detail for approver';
  OPEN p_out_behaviors_dtl FOR
    SELECT ncb.claim_id,
           (select cms_name from temp_table_session where asset_code = 'picklist.promo.nomination.behavior.items' and cms_code = behavior_type) behavior_name,
           bad.badge_name
      FROM promo_behavior pb,
           (SELECT bp.eligible_promotion_id , br.behavior_name, br.cm_asset_key badge_name
              FROM badge_promotion bp,
                   badge_rule br
             WHERE bp.promotion_id = br.promotion_id
               AND br.behavior_name IS NOT NULL
            ) bad,
           nomination_claim_behaviors ncb,
           (SELECT s.*
              FROM (SELECT ROW_NUMBER() OVER 
                           (ORDER BY
                             -- sort on field ascending
                             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominee_name')      THEN UPPER(nominee_name)    END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'submitted_date')    THEN submitted_date         END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominator_name')    THEN UPPER(nominator_name)  END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc')  THEN claim_id  END) ASC  -- 02/08/2017  Bug 71330
                             -- sort on field descending
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominee_name')     THEN UPPER(nominee_name)    END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'submitted_date')   THEN submitted_date         END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominator_name')   THEN UPPER(nominator_name)  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc') THEN claim_id  END) DESC -- 02/08/2017  Bug 71330
                           ) AS rn, 
                           res.*
                      FROM tmp_approver_nomi_detail res
                  ) s -- sort number result set
            WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
              AND s.rn < TO_NUMBER(p_in_rowNumEnd)
           )ta
     WHERE pb.promotion_id = p_in_promotion_id
       AND pb.promotion_id = bad.eligible_promotion_id (+)
       AND pb.behavior_type = bad.behavior_name  (+)
       AND pb.behavior_type = ncb.behavior
       AND ncb.claim_id = ta.claim_id;
       
  v_stage := 'Return Custom detail for approver';
  OPEN p_out_custom_dtl FOR
    SELECT cc.claim_id,
           ta.nominee_pax_id,
           claim_form_step_element_id,
           claim_form_step_element_name, 
           REPLACE(cc.cms_value,'DUMMYNULL',NULL) claim_form_step_element_desc   --08/22/2017 G6-2835
      FROM (
      SELECT *FROM  -- 06/26/2019
      (SELECT claim_id,
                   claim_cfse_id, 
                   sequence_num,
                   claim_form_step_element_id,
                   claim_form_step_element_name,
                   cms_value,  -- 06/26/2019
                   ROW_NUMBER() OVER (PARTITION BY claim_id,   -- 06/26/2019
                                                   claim_cfse_id, 
                                                   sequence_num,
                                                   claim_form_step_element_id 
                                          ORDER BY claim_id ) rownumber
                  -- LISTAGG(NVL(cms_value,'DUMMYNULL'),',') WITHIN GROUP(ORDER BY lvl,cms_value) cms_value         --08/22/2017 G6-2835   -- Commented out 06/26/2019
              FROM ( SELECT claim_id,
                            claim_cfse_id,
                            claim_form_step_element_id,
                            sequence_num,
                            cm_asset_code,
                            (SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND INSTR(cms_code,'ELEMENTLABEL_'||cm_key_fragment,1) > 0) claim_form_step_element_name,
                            -- NVL((SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND (lower(cms_code) = lower(value||'_'||cm_key_fragment) or lower(cms_code) = lower('label'||value||'_'||cm_key_fragment))), value)   cms_value,  -- Commented out 06/26/2019 
                            -- TO_NUMBER(lvl) lvl --08/22/2017  G6-2835  -- Commented out 06/26/2019
                            NVL((SELECT to_clob(cms_name) from temp_table_session 
                                where asset_code = cm_asset_code 
                                AND (dbms_lob.compare(lower(cms_code), to_clob(lower(value||'_'||cm_key_fragment))) = 0
                                 or dbms_lob.compare(lower(cms_code), to_clob(lower('label'||value||'_'||cm_key_fragment))) = 0)),value) cms_value -- 06/26/2019
                       FROM ( SELECT cc.claim_id,
                                     cc.value,   -- 06/26/2019
                                     -- SUBSTR(p.column_value,1,instr(p.column_value,'~',-1)-1) as value,      --02/02/2017   Bug 71155  -- Commented out 06/26/2019
                                     -- SUBSTR(p.column_value,instr(p.column_value,'~',-1)+1) as lvl,          --02/02/2017   Bug 71155    -- Commented out 06/26/2019
                                     cc.claim_cfse_id,
                                     cf.cm_asset_code, 
                                     cfse.claim_form_step_element_id,
                                     cfse.sequence_num,
                                     cfse.cm_key_fragment,
                                     selection_pick_list_name
                                FROM promotion p,
                                     claim_form cf, 
                                     claim_form_step cfs, 
                                     claim_form_step_element cfse,
                                     claim_cfse cc,
                                     tmp_approver_nomi_detail ta
                                    /* TABLE( CAST( MULTISET(                           -- Commented out 06/26/2019
                                      SELECT SUBSTR(','||cc.value||',',
                                                   INSTR(','||cc.value||',', ',', 1, LEVEL)+1, 
                                                   INSTR(','||cc.value||',', ',', 1, LEVEL+1) - INSTR(','||cc.value||',', ',', 1, LEVEL)-1 
                                                   ) || '~'||LEVEL              --02/02/2017   Bug 71155
                                                                     
                                        FROM dual
                                     CONNECT BY LEVEL <= REGEXP_COUNT(','||cc.value, ',') 
                                     ) AS sys.odcivarchar2list ) ) p */                     
                               WHERE p.promotion_id = p_in_promotion_id
                                 AND p.claim_form_id = cf.claim_form_id
                                 AND cf.claim_form_id = cfs.claim_form_id
                                 AND cfs.claim_form_step_id = cfse.claim_form_step_id
                                 AND cfse.claim_form_step_element_id = cc.claim_form_step_element_id 
                                 AND cc.value IS NOT NULL                         
                                 AND claim_form_element_type_code <> 'copy'           
                                 and cc.claim_id = ta.claim_id
                            ))) WHERE rownumber = 1  -- 06/26/2019
                 --  GROUP BY claim_id,claim_cfse_id, sequence_num,claim_form_step_element_id,claim_form_step_element_name   -- Commented out 06/26/2019
           ) cc,
           (SELECT s.*
              FROM (SELECT ROW_NUMBER() OVER 
                           (ORDER BY
                             -- sort on field ascending
                             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominee_name')      THEN UPPER(nominee_name)    END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'submitted_date')    THEN submitted_date         END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominator_name')    THEN UPPER(nominator_name)  END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc')  THEN claim_id  END) ASC  -- 02/08/2017  Bug 71330
                             -- sort on field descending
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominee_name')     THEN UPPER(nominee_name)    END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'submitted_date')   THEN submitted_date         END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominator_name')   THEN UPPER(nominator_name)  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc') THEN claim_id  END) DESC -- 02/08/2017  Bug 71330
                           ) AS rn, 
                           res.*
                      FROM tmp_approver_nomi_detail res
                  ) s -- sort number result set
            WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
              AND s.rn < TO_NUMBER(p_in_rowNumEnd)
           )ta
     WHERE cc.claim_id = ta.claim_id 
     ORDER BY CC.SEQUENCE_NUM;      --02/12/2019
  
   v_stage := 'Return award amounts detail for promotion and level number';
  OPEN p_out_award_amount_dtl FOR
    SELECT claim_id,
           level_index,
           min_val award_amount_min,
           max_val award_amount_max,
           calcualtor_id,
           award_amount_type_fixed,
           award_amount_type_range,
           award_amount_type_none 
      FROM (SELECT s.*
              FROM (SELECT ROW_NUMBER() OVER 
                           (ORDER BY
                             -- sort on field ascending
                             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominee_name')      THEN UPPER(nominee_name)    END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'submitted_date')    THEN submitted_date         END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominator_name')    THEN UPPER(nominator_name)  END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc')  THEN claim_id  END) ASC  -- 02/08/2017  Bug 71330
                             -- sort on field descending
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominee_name')     THEN UPPER(nominee_name)    END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'submitted_date')   THEN submitted_date         END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominator_name')   THEN UPPER(nominator_name)  END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc') THEN claim_id  END) DESC -- 02/08/2017  Bug 71330
                           ) AS rn, 
                           res.*
                      FROM tmp_approver_nomi_detail res
                  ) s -- sort number result set
            WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
              AND s.rn < TO_NUMBER(p_in_rowNumEnd)
           )ta;
           
  /*v_stage := 'Get nomination time periods';
  OPEN p_out_time_periods FOR
    SELECT pnt.nomination_time_period_id,
           (SELECT cms_name FROM temp_table_session WHERE asset_code = time_period_name_asset_code) time_period_name,
           ta.claim_id
      FROM promo_nomination_time_period pnt,
           (SELECT s.*
              FROM (SELECT ROW_NUMBER() OVER 
                           (ORDER BY
                             -- sort on field ascending
                             (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominee_name')      THEN UPPER(nominee_name)    END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'submitted_date')    THEN submitted_date         END) ASC
                           , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominator_name')    THEN UPPER(nominator_name)  END) ASC
                             -- sort on field descending
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominee_name')     THEN UPPER(nominee_name)    END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'submitted_date')   THEN submitted_date         END) DESC
                           , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominator_name')   THEN UPPER(nominator_name)  END) DESC
                           ) AS rn, 
                           res.*
                      FROM tmp_approver_nomi_detail res
                  ) s -- sort number result set
            WHERE s.rn > TO_NUMBER(p_in_rowNumStart)
              AND s.rn < TO_NUMBER(p_in_rowNumEnd)
           )ta
     WHERE pnt.promotion_id =  p_in_promotion_id
       AND pnt.nomination_time_period_id = ta.nomination_time_period_id
       AND pnt.promotion_id = ta.promotion_id;*/
      
  v_stage := 'Get nomination count for the approver';               
  SELECT SUM(nominee_count)
    INTO p_out_total_nomi_count
    FROM ( SELECT COUNT(1) AS nominee_count
             FROM tmp_approver_nomi_detail
            WHERE is_cumulative_nomination = 0
            UNION ALL
           SELECT COUNT(DISTINCT nominee_pax_id||claim_group_id) AS nominee_count
             FROM tmp_approver_nomi_detail
            WHERE is_cumulative_nomination = 1
         );
  
    
   v_stage := 'Return max win time period for the approver';  
  OPEN p_out_max_win_claim_dtl FOR
    SELECT claim_id, 
         pntp_nomination_time_period_id nomination_time_period_id, 
         (SELECT cms_name FROM temp_table_session WHERE asset_code = time_period_name_asset_code) time_period_name,
          max_wins_allowed, --10/23/2017 Chidamba
          no_of_winners     --10/23/2017 Chidamba
     FROM (     
     WITH winners AS
               (                       
               SELECT DISTINCT cia.time_period_id,cia.approval_round,COUNT(1) OVER(partition by cia.time_period_id,cia.approval_round) no_of_times_won 
                 FROM claim_recipient cr,
                       claim c,
                       claim_item_approver cia,
                       claim_item ci,
                       nomination_claim nc
                 WHERE (cia.claim_item_id = ci.claim_item_id OR c.claim_group_id = cia.claim_group_id)
                   AND cia.approval_status_type = 'winner'
                   AND ci.claim_id = c.claim_id
                   AND c.claim_id = nc.claim_id
                   AND cr.claim_item_id = ci.claim_item_id
                   AND c.promotion_id = p_in_promotion_id
                --   GROUP BY cr.participant_id,cia.time_period_id,cia.approval_round  --Bug # 69672 wins limit is per user per level.     
                )                
            SELECT ta.claim_id,
                   ta.pntp_nomination_time_period_id, 
                   ta.time_period_name_asset_code,
                   ta.max_wins_allowed, --10/23/2017 Chidamba
                   winners.no_of_times_won  no_of_winners  --10/23/2017 Chidamba 
              FROM winners,                   
                   (                   
                   SELECT s.*
                      FROM (SELECT ROW_NUMBER() OVER 
                                    (ORDER BY
                                     -- sort on field ascending
                                      (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominee_name')      THEN UPPER(nominee_name)    END) ASC
                                   , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'submitted_date')    THEN submitted_date         END) ASC
                                   , (CASE WHEN (p_in_sortedBy = 'asc' AND p_in_sortedOn = 'nominator_name')    THEN UPPER(nominator_name)  END) ASC
                                   , (CASE WHEN (p_in_sortedBy = 'asc')    THEN claim_id  END) ASC      -- 02/08/2017  Bug 71330 added sort as in p_out_nomin_detail
                                     -- sort on field descending
                                   , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominee_name')     THEN UPPER(nominee_name)    END) DESC
                                   , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'submitted_date')   THEN submitted_date         END) DESC
                                   , (CASE WHEN (p_in_sortedBy = 'desc' AND p_in_sortedOn = 'nominator_name')   THEN UPPER(nominator_name)  END) DESC
                                   , (CASE WHEN (p_in_sortedBy = 'asc')    THEN claim_id  END) DESC     -- 02/08/2017  Bug 71330 added sort as in p_out_nomin_detail
                                   ) AS rn, 
                                   res.claim_id,
                                   res.level_index,
                                   pntp.max_wins_allowed, 
                                   pntp.nomination_time_period_id pntp_nomination_time_period_id,
                                   pntp.time_period_name_asset_code
                              FROM tmp_approver_nomi_detail res,
                              promo_nomination_time_period pntp
                              where res.promotion_id = pntp.promotion_id (+) 
                          ) s -- sort number result set
                     --WHERE s.rn > TO_NUMBER(p_in_rowNumStart)  --03/01/2019 Bug 78967
                     --AND s.rn < TO_NUMBER(p_in_rowNumEnd)      --03/01/2019 Bug 78967                                                               
                   )ta
             WHERE ta.pntp_nomination_time_period_id = winners.time_period_id(+)
               AND p_in_status = 'pend'
               AND ta.level_index = winners.approval_round(+)
               --AND (ta.max_wins_allowed > NVL(winners.no_of_times_won,0) OR ta.max_wins_allowed IS NULL)
               )               
             UNION ALL --Bug # 69950
            SELECT claim_id, 
                   nomination_time_period_id, 
                   (SELECT cms_name FROM temp_table_session WHERE asset_code = time_period_name_asset_code) time_period_name,
                   max_wins_allowed, --10/23/2017 Chidamba
                   no_of_winners  --10/23/2017 Chidamba 
     FROM (SELECT c.claim_id,
                  cia.time_period_id nomination_time_period_id,
                  pntp.time_period_name_asset_code,
                  COUNT(distinct c.claim_id) over(partition by cia.time_period_id) no_of_winners, --10/23/2017 Chidamba       
                  pntp.max_wins_allowed  --10/23/2017 Chidamba 
             FROM claim_recipient cr,
                       claim c,
                       claim_item_approver cia,
                       claim_item ci,
                       nomination_claim nc,
                       promo_nomination_time_period pntp
                 WHERE (cia.claim_item_id = ci.claim_item_id OR c.claim_group_id = cia.claim_group_id)
                   AND cia.approval_status_type = 'winner'
                   AND ci.claim_id = c.claim_id
                   AND c.claim_id = nc.claim_id
                   AND cr.claim_item_id = ci.claim_item_id
                   AND p_in_status <>'pend'
                   AND pntp.nomination_time_period_id = cia.time_period_id
                   AND c.promotion_id = p_in_promotion_id
                   AND cia.approver_user_id = p_in_approver_id);
                     
  v_stage := 'Check if we need to give option for user to request more budget'; --07/26/2016  
    
  IF v_request_more_budget = 0 OR v_award_active = 0 OR v_level_has_award = 0 THEN
    
    v_out_budget_exceeded := 0;
    
  ELSE
    v_stage := 'Check if calculator is setup for award calculation';   
    
       
    IF v_calculator_id IS NULL THEN --No calculator involved
     
      SELECT SUM( CASE WHEN v_award_payout_type = 'other'  THEN 1
                       WHEN v_award_payout_type <> 'other' AND award_amount_type_fixed = 1 THEN award_amount_fixed 
                       WHEN v_award_payout_type <>'other' AND award_amount_type_fixed = 0 THEN max_val END) 
        INTO v_total_budget_required 
        FROM tmp_approver_nomi_detail;
           
    ELSE --calculator
     
     SELECT MAX(DECODE(cp.high_award,0,cp.low_award,cp.high_award)) 
       INTO v_calculator_max_award 
       FROM calculator c,
            calculator_criterion cc,
            (SELECT  r.calculator_criterion_id, 
                     rating_value
               FROM ( SELECT RANK () OVER (PARTITION BY pe.calculator_criterion_id ORDER BY rating_value DESC) AS rec_rank,
                             pe.*
                        FROM calculator_criterion_rating pe
                     ) r                                                     
               WHERE r.rec_rank = 1) ccr,
            calculator_payout cp
      WHERE c.calculator_id = v_calculator_id
        AND c.calculator_id = cc.calculator_id
        AND cc.calculator_criterion_id = ccr.calculator_criterion_id
        AND cp.calculator_id = c.calculator_id
        AND (ccr.rating_value * cc.weight_value/100) BETWEEN cp.low_score AND high_score;

     SELECT COUNT(1) * v_calculator_max_award,
            other_payout_quantity 
       INTO v_total_budget_required,
            v_other_payout_quantity 
       FROM tmp_approver_nomi_detail 
      GROUP BY other_payout_quantity;
     
    END IF;
     
    IF v_total_budget_required > NVL(v_other_payout_quantity, v_out_budget_balance) THEN
      v_out_budget_exceeded := 1 ;
       
    ELSE
      v_out_budget_exceeded := 0;
      
    END IF;
    
  END IF; --07/26/2016
    
  OPEN p_out_dtl FOR
    select v_out_budget_balance budget_balance,
           v_out_budget_exceeded budget_exceeded,
           DECODE(pn.evaluation_type, 'cumulative',1,0) is_cumulative_nomination,
           CASE WHEN pnl.award_payout_type = 'points' THEN pn.last_pnt_bud_req_date
                WHEN pnl.award_payout_type = 'cash' THEN pn.last_cash_bud_req_date 
           END last_budget_request_date,
           pnl.quantity other_payout_quantity,
           (SELECT cms_name FROM temp_table_session WHERE asset_code = pnl.payout_description_asset_code) payout_description_asset_code,
           (SELECT cms_name FROM temp_table_session WHERE asset_code = bs.cm_asset_code) budget_period_name,
           pnl.award_payout_type,
           (SELECT (SELECT cms_name FROM temp_table_session WHERE asset_code=level_label_asset_code)
              FROM promo_nomination_level pnl,
                   promo_nomination pn
             WHERE pnl.promotion_id = p_in_promotion_id
               AND pnl.promotion_id = pn.promotion_id
               AND pnl.level_index = (p_in_level_number - 1)
           ) previous_level_name,
           (SELECT (SELECT cms_name FROM temp_table_session WHERE asset_code=level_label_asset_code)
              FROM promo_nomination_level pnl,
                   promo_nomination pn
             WHERE pnl.promotion_id = p_in_promotion_id
               AND pnl.promotion_id = pn.promotion_id
               AND pnl.level_index = (p_in_level_number + 1)
           ) next_level_name,
           (SELECT COUNT(1) total_promotion_count
              FROM (SELECT c.claim_id 
                      FROM claim_approver_snapshot cas,
                           claim c,
                           promotion p
                     WHERE (cas.approver_user_id = p_in_approver_id OR v_approver_is_admin = 1)
                       AND c.approval_round = (p_in_level_number - 1)
                       AND (cas.claim_id = c.claim_id OR cas.claim_group_id = c.claim_group_id)
                       AND c.promotion_id = p.promotion_id
                       and c.promotion_id = NVL(p_in_promotion_id,c.promotion_id)
                       AND p.promotion_type = 'nomination'
                       AND EXISTS ( SELECT 1
                                      FROM claim_recipient cr,
                                           claim_item ci,
                                           claim cl,
                                           application_user au
                                     WHERE cr.claim_item_id = ci.claim_item_id
                                       AND ci.claim_id = cl.claim_id
                                       AND cl.claim_id = c.claim_id
                                       AND cr.participant_id = au.user_id
                                       AND ci.approval_status_type = 'pend'
                                       AND au.is_active = 1
                                   )
                     )
           )total_promotion_count
      FROM promo_nomination pn,
           promo_nomination_level pnl,
           budget_segment bs,
           promotion p
     WHERE p.promotion_id = pnl.promotion_id(+)
       AND p.promotion_id = pn.promotion_id
       AND ((p_in_level_number = pnl.level_index AND pn.payout_level_type = 'eachLevel')
              OR (pn.payout_level_type = 'finalLevel' ))
       AND p.promotion_id = p_in_promotion_id
       AND (CASE WHEN v_payout_level_type = 'finalLevel' THEN NVL(p.award_budget_master_id,p.cash_budget_master_id)
                 WHEN v_payout_level_type = 'eachLevel' AND v_award_payout_type = 'points' THEN p.award_budget_master_id
                 WHEN v_payout_level_type = 'eachLevel' AND v_award_payout_type = 'cash' THEN p.cash_budget_master_id
                 ELSE NVL(p.award_budget_master_id,p.cash_budget_master_id) 
           END) = bs.budget_master_id (+)
       AND (bs.budget_master_id IS NULL OR (bs.budget_master_id IS NOT NULL AND TRUNC(SYSDATE) BETWEEN bs.start_date AND NVL(bs.end_date,TRUNC(SYSDATE))));

EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_promotion_id: '||p_in_promotion_id||
      ' ~ p_in_level_number: '||p_in_level_number||' ~ p_in_approver_id: '||p_in_approver_id||' ~ p_in_time_period_id: '||p_in_time_period_id||
      ' ~ p_in_submit_start_date: '||p_in_submit_start_date||' ~ p_in_submit_end_date: '||p_in_submit_end_date||' ~ p_in_status: '||p_in_status||
      '~ p_in_locale: '||p_in_locale||' ~ Error at stage :'||v_stage||'-->'||SQLERRM, NULL);
      p_out_returncode := 99;
      p_out_total_nomi_count  := 0;      
--      p_out_prev_pend_nom_cnt := 0;
--      p_out_next_pend_nom_cnt := 0;  
      OPEN p_out_prev_lvl_approvers FOR SELECT NULL FROM DUAL;
      OPEN p_out_nomin_detail FOR SELECT NULL FROM DUAL;
      OPEN p_out_team_members_dtl FOR SELECT NULL FROM DUAL;
      OPEN p_out_behaviors_dtl FOR SELECT NULL FROM DUAL;
      OPEN p_out_custom_dtl FOR SELECT NULL FROM DUAL;
      OPEN p_out_award_amount_dtl FOR SELECT NULL FROM DUAL;
--      OPEN p_out_time_periods FOR SELECT NULL FROM DUAL;
      OPEN p_out_max_win_claim_dtl FOR SELECT NULL FROM DUAL;
      OPEN p_out_dtl FOR SELECT NULL FROM DUAL;

END prc_list_approver_detail_page;

PROCEDURE prc_list_appr_cummula_dtl_page 
         ( p_in_promotion_id        IN  NUMBER,
           p_in_nominee_id          IN  NUMBER,
           p_in_claim_group_id      IN  NUMBER,
           p_in_level_number        IN  NUMBER,
           p_in_approver_id         IN  NUMBER,
           p_in_time_period_id      IN  NUMBER,
           p_in_submit_start_date   IN  DATE,
           p_in_submit_end_date     IN  DATE,
           p_in_status              IN  VARCHAR2,
           p_in_locale              IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,  
           p_out_nominator_detail   OUT SYS_REFCURSOR,
           p_out_custom_dtl         OUT SYS_REFCURSOR)
AS
/*******************************************************************************
 Purpose: To return the nominator detail and custom field deail for cumulative promotion
 
  Person             Date         Comments
  -----------       --------      -----------------------------------------------------
  nagarajs          09/14/2016    Initial Creation   
    chidamba        08/22/2017   G6-2835 Nomination Comments are flipping around for approver if you have a comma and Why Box = Yes in Form setup                        
                 Converting the level number to number as the LISTAGG sort considers it as character instead of number 
                  LISTAGG ignores NULL value input(,,) when returning the concatenated string                     
*******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_LIST_APPR_CUMMULA_DTL_PAGE';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';

   
   v_stage                  VARCHAR2(500);
   v_award_payout_type      promo_nomination_level.award_payout_type%TYPE;
   v_currency_value         cash_currency_current.bpom_entered_rate%TYPE;
   v_approver_is_admin      NUMBER(10);
   v_payout_level_type      promo_nomination.payout_level_type%TYPE;
   v_default_locale         os_propertyset.string_val%TYPE;
 
   TYPE v_type_appr IS TABLE OF TMP_APPROVER_CUMUL_NOMI_DETAIL%ROWTYPE;
   
   v_rec_appr       v_type_appr;
    
   CURSOR cur_appr (v_approver_is_admin IN NUMBER, v_currency_value IN VARCHAR2,v_payout_level_type IN VARCHAR2, v_award_payout_type IN VARCHAR2 ) IS
     WITH nominee AS
      (SELECT  c.claim_id,
               ci.claim_item_id,
               c.promotion_id,
               pnt.nomination_time_period_id,
               pnt.time_period_name_asset_code,
               c.approval_round,               
               TRUNC(c.submission_date) submission_date,
               cr.participant_id,
               c.submitter_id,
               au_giv.first_name nominator_first_name,
               au_giv.last_name nominator_last_name,
               au_rec.first_name nominee_first_name,
               au_rec.last_name nominee_last_name,
               cu.country_code country_name,
               n.name org_name,
               vw.position_type job_position_name,
               vw.department_type  department_name,
               pa.avatar_small avator_url,
               nc.team_name,
               team_id,
               nc.submitter_comments_lang_id,
               ca.small_image_name ecard_image,
               nc.own_card_name,
               nc.card_video_url,
               nc.card_video_image_url,
               nc.submitter_comments,
               nc.why_attachment_name,
               nc.why_attachment_url,
               nc.certificate_id,
               ci.approval_status_type,
               time_period_start_date,
               time_period_end_date,
               nc.more_info_comments,
               c.claim_group_id,
               CASE WHEN NVL(cr.award_qty ,0) > 0 THEN cr.award_qty
               WHEN NVL(cr.cash_award_qty ,0) > 0 THEN cr.cash_award_qty
               WHEN NVL(cr.calculator_score ,0) > 0 THEN cr.calculator_score
               END award_amount,
               c.approver_comments non_winner_comments
          FROM claim c,
              claim_item ci,
              claim_recipient cr,
              (SELECT nc.claim_id, c.promotion_id, nc.card_id, 
                      nc.team_name,
                       nc.team_id,
                       nc.submitter_comments_lang_id,
                       nc.own_card_name,
                       nc.card_video_url,
                       nc.card_video_image_url,
                       nc.submitter_comments,
                       nc.why_attachment_name,
                       nc.why_attachment_url,
                       nc.certificate_id,
                       nc.more_info_comments, 
                       nomination_time_period_id
                  FROM nomination_claim nc,
                       claim c
                 WHERE nc.claim_id = c.claim_id)nc,
              card ca,
              promo_nomination_time_period pnt,
              node n,
              application_user au_giv,
              application_user au_rec,
              participant pa,
              user_address ua,
              country cu,
              vw_curr_pax_employer vw
        WHERE c.promotion_id = NVL(p_in_promotion_id,c.promotion_id)
          AND c.claim_id = ci.claim_id
          AND ci.claim_item_id = cr.claim_item_id
          AND cr.participant_id =p_in_nominee_id
          AND NVL(c.claim_group_id,0) = NVL(p_in_claim_group_id,0)  --claim group id will be NULL untill it's approved at 1st level
          AND cr.node_id = n.node_id
          AND c.claim_id = nc.claim_id
          AND nc.card_id = ca.card_id (+)
          AND nc.promotion_id = pnt.promotion_id (+)
          AND nc.nomination_time_period_id = pnt.nomination_time_period_id (+)
          AND (pnt.nomination_time_period_id = p_in_time_period_id OR p_in_time_period_id IS NULL)
          AND c.submitter_id = au_giv.user_id 
          AND cr.participant_id = au_rec.user_id 
          AND au_rec.is_active = 1
          AND au_rec.user_id = pa.user_id
          AND au_rec.user_id = ua.user_id (+)
          AND ua.is_primary (+) = 1
          AND ua.country_id = cu.country_id (+)
          AND au_rec.user_id = vw.user_id (+)
          AND NOT EXISTS (SELECT 1
                             FROM  claim_item_approver cia
                            WHERE (cia.claim_item_id = ci.claim_item_id OR c.claim_group_id = cia.claim_group_id)
                              AND cia.approval_round = (c.approval_round - 1)
                              AND cia.notification_date > TRUNC(SYSDATE)
                          )
                   )     
    , approver_nom AS
       (SELECT c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               cia.approval_round,
               c.submission_date submitted_date,
               NULL participant_id,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               NULL nominee_first_name,
               NULL nominee_last_name,
               NULL country_name,
               NULL org_name,
               NULL job_position_name,
               NULL department_name,
               NULL avator_url,
               c.team_name,
               c.team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               cia.approval_status_type,
               c.more_info_comments,
               c.claim_group_id,
               c.award_amount,
               COUNT(1) team_cnt,
               cia.approver_comments winner_more_info_comments,
               c.non_winner_comments
          FROM nominee c,
               claim_item_approver cia
         WHERE (c.claim_item_id = cia.claim_item_id 
            OR c.claim_group_id = cia.claim_group_id or cia.claim_item_id is null or cia.claim_group_id is null)
           AND c.team_name IS NOT NULL
           AND ( (p_in_status IS NULL 
                      AND( (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NOT NULL AND TRUNC(cia.date_approved) BETWEEN c.time_period_start_date AND c.time_period_end_date  ))
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND p_in_time_period_id IS NULL AND p_in_submit_start_date IS NULL AND p_in_submit_end_date IS NULL )
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NULL AND TRUNC(cia.date_approved) BETWEEN p_in_submit_start_date AND NVL(p_in_submit_end_date,TRUNC(SYSDATE)) ))
                      ))
                  OR ((cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NOT NULL AND TRUNC(cia.date_approved) BETWEEN c.time_period_start_date AND c.time_period_end_date  ))
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND p_in_time_period_id IS NULL AND p_in_submit_start_date IS NULL AND p_in_submit_end_date IS NULL )
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NULL AND TRUNC(cia.date_approved) BETWEEN p_in_submit_start_date AND NVL(p_in_submit_end_date,TRUNC(SYSDATE)) ))
                      )
                )
         GROUP BY c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               c.approval_round,
               cia.approval_round,
               c.submission_date,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               c.team_name,
               c.team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               c.approval_status_type,
               cia.approval_status_type,
               c.award_amount,
               c.more_info_comments,
               c.claim_group_id,
               cia.approver_comments,
               c.non_winner_comments
        UNION ALL
        SELECT c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               c.approval_round,
               c.submission_date submitted_date,
               NULL participant_id,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               NULL nominee_first_name,
               NULL nominee_last_name,
               NULL country_name,
               NULL org_name,
               NULL job_position_name,
               NULL department_name,
               NULL avator_url,
               c.team_name,
               c.team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               c.approval_status_type,
               c.more_info_comments,
               c.claim_group_id,
               c.award_amount,
               COUNT(1) team_cnt,
               null winner_more_info_comments,
               c.non_winner_comments
          FROM nominee c
         WHERE c.team_name IS NOT NULL
           AND ( (p_in_status IS NULL 
                      AND((c.approval_status_type = 'pend' AND TRUNC(c.submission_date) BETWEEN NVL(p_in_submit_start_date,TRUNC(c.submission_date)) AND NVL(p_in_submit_end_date,TRUNC(SYSDATE))  AND c.approval_round = NVL(p_in_level_number,c.approval_round))))
                  OR ((c.approval_status_type = 'pend' and c.approval_status_type  IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND TRUNC(c.submission_date) BETWEEN NVL(p_in_submit_start_date,TRUNC(c.submission_date)) AND NVL(p_in_submit_end_date,TRUNC(SYSDATE))  AND c.approval_round = NVL(p_in_level_number,c.approval_round)))
                )
         GROUP BY c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               c.approval_round,
               c.approval_round,
               c.submission_date,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               c.team_name,
               c.team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               c.approval_status_type,
               c.approval_status_type,
               c.award_amount,
               c.more_info_comments,
               c.claim_group_id,
               c.non_winner_comments
         UNION ALL
        SELECT c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               cia.approval_round,
               c.submission_date submitted_date,
               c.participant_id,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               c.nominee_first_name,
               c.nominee_last_name,
               c.country_name,
               c.org_name,
               c.job_position_name,
               c.department_name,
               c.avator_url,
               c.team_name,
               NULL team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               cia.approval_status_type,
               c.more_info_comments,
               c.claim_group_id,
               c.award_amount,
               0 team_cnt,
               cia.approver_comments winner_more_info_comments,
               c.non_winner_comments
         FROM nominee c,
              claim_item_approver cia
        WHERE (c.claim_item_id = cia.claim_item_id 
           OR c.claim_group_id = cia.claim_group_id )
          AND c.team_name IS NULL      
          AND ( (p_in_status IS NULL 
                      AND( (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NOT NULL AND TRUNC(cia.date_approved) BETWEEN c.time_period_start_date AND c.time_period_end_date  ))
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND p_in_time_period_id IS NULL AND p_in_submit_start_date IS NULL AND p_in_submit_end_date IS NULL )
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NULL AND TRUNC(cia.date_approved) BETWEEN p_in_submit_start_date AND NVL(p_in_submit_end_date,TRUNC(SYSDATE)) ))
                      ))
                  OR ((cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND  cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NOT NULL AND TRUNC(cia.date_approved) BETWEEN c.time_period_start_date AND c.time_period_end_date  ))
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND p_in_time_period_id IS NULL AND p_in_submit_start_date IS NULL AND p_in_submit_end_date IS NULL )
                      OR (cia.approval_status_type IN ('more_info', 'approv' , 'winner','non_winner') AND cia.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND cia.approval_round = NVL(p_in_level_number,cia.approval_round) AND (p_in_time_period_id IS NULL AND TRUNC(cia.date_approved) BETWEEN p_in_submit_start_date AND NVL(p_in_submit_end_date,TRUNC(SYSDATE)) ))
                      )
                )
        UNION ALL
        SELECT c.claim_id,
               c.promotion_id,
               c.nomination_time_period_id,
               c.time_period_name_asset_code,
               c.approval_round,
               c.submission_date submitted_date,
               c.participant_id,
               c.submitter_id,
               c.nominator_first_name,
               c.nominator_last_name,
               c.nominee_first_name,
               c.nominee_last_name,
               c.country_name,
               c.org_name,
               c.job_position_name,
               c.department_name,
               c.avator_url,
               c.team_name,
               NULL team_id,
               c.submitter_comments_lang_id,
               c.ecard_image,
               c.own_card_name,
               c.card_video_url,
               c.card_video_image_url,
               c.submitter_comments,
               c.why_attachment_name,
               c.why_attachment_url,
               c.certificate_id,
               c.approval_status_type,
               c.more_info_comments,
               c.claim_group_id,
               c.award_amount,
               0 team_cnt,
               null winner_more_info_comments,
               c.non_winner_comments
         FROM nominee c
        WHERE c.team_name IS NULL      
          AND ( (p_in_status IS NULL 
                      AND((c.approval_status_type = 'pend' AND TRUNC(c.submission_date) BETWEEN NVL(p_in_submit_start_date,TRUNC(c.submission_date)) AND NVL(p_in_submit_end_date,TRUNC(SYSDATE))  AND c.approval_round = NVL(p_in_level_number,c.approval_round))))
                  OR ((c.approval_status_type = 'pend' and c.approval_status_type IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_status))) AND TRUNC(c.submission_date) BETWEEN NVL(p_in_submit_start_date,TRUNC(c.submission_date)) AND NVL(p_in_submit_end_date,TRUNC(SYSDATE))  AND c.approval_round = NVL(p_in_level_number,c.approval_round)))
                )
           ) 
    SELECT c.claim_id,
           c.submitter_id nominator_pax_id,
           c.nominator_last_name||', '||c.nominator_first_name nominator_name,
           c.submitted_date,
           DECODE(pn.evaluation_type, 'cumulative',1,0) is_cumulative_nomination,
           c.submitter_comments,
           c.why_attachment_name,          
           c.why_attachment_url,  
           c.certificate_id,
           c.more_info_comments
      FROM promotion p,
           promo_nomination pn,
           promo_nomination_level pml,
           budget_segment bs,
           approver_nom c
     WHERE c.promotion_id = pml.promotion_id(+)
       AND ((c.approval_round = pml.level_index AND pn.payout_level_type = 'eachLevel') OR (pn.payout_level_type = 'finalLevel'))
       AND c.promotion_id = p.promotion_id
       AND p.promotion_id = pn.promotion_id
       AND (CASE WHEN v_payout_level_type = 'finalLevel' THEN NVL(p.award_budget_master_id,p.cash_budget_master_id)
                 WHEN v_payout_level_type = 'eachLevel' AND v_award_payout_type = 'points' THEN p.award_budget_master_id
                 WHEN v_payout_level_type = 'eachLevel' AND v_award_payout_type = 'cash' THEN p.cash_budget_master_id
                 ELSE NVL(p.award_budget_master_id,p.cash_budget_master_id) END) = bs.budget_master_id (+)
       AND (bs.budget_master_id IS NULL OR (bs.budget_master_id IS NOT NULL AND TRUNC(SYSDATE) BETWEEN bs.start_date AND NVL(bs.end_date,TRUNC(SYSDATE))))
       AND EXISTS (SELECT 1
                     FROM claim_item_approver ciaa,
                          claim_item ci
                    WHERE (ciaa.approver_user_id = p_in_approver_id OR v_approver_is_admin = 1)
                      AND (ciaa.claim_item_id = ci.claim_item_id OR c.claim_group_id = ciaa.claim_group_id)
                      AND ci.claim_id = c.claim_id
                      AND ciaa.approval_round = c.approval_round
                   UNION ALL
                   SELECT 1
                     FROM claim_approver_snapshot cas, claim c2 --Bug # 69138
                    WHERE (cas.approver_user_id = p_in_approver_id OR v_approver_is_admin = 1)
                      AND (cas.claim_id = c.claim_id OR cas.claim_group_id = c.claim_group_id)
                      AND c2.approval_round = c.approval_round 
                      AND (cas.claim_id = c2.claim_id  OR cas.claim_group_id = c2.claim_group_id)
                  );

BEGIN
   
  p_out_returncode := 0;
 
  SELECT payout_level_type
    INTO v_payout_level_type
    FROM promo_nomination 
   WHERE promotion_id = p_in_promotion_id;    
  
  SELECT COUNT(1)
    INTO v_approver_is_admin
    FROM application_user
   WHERE user_id = p_in_approver_id
     AND user_type = 'bi';
   
  BEGIN
    SELECT award_payout_type
      INTO v_award_payout_type 
      FROM promo_nomination_level 
     WHERE promotion_id = p_in_promotion_id 
       AND level_index = p_in_level_number;
  EXCEPTION 
     WHEN OTHERS THEN
       v_award_payout_type := NULL;
  END;   
   
  v_stage := 'Get currency value';
  BEGIN
   SELECT ccc.bpom_entered_rate
     INTO v_currency_value
     FROM user_address ua,
          country c,
          cash_currency_current ccc 
    WHERE ua.user_id = p_in_approver_id
      AND is_primary = 1
      AND ua.country_id = c.country_id
      AND c.country_code = ccc.to_cur;
  EXCEPTION
    WHEN OTHERS THEN
      v_currency_value := 1;
  END;   
  
  BEGIN
    SELECT string_val 
      INTO v_default_locale
      FROM os_propertyset
     WHERE entity_name = 'default.language';
  EXCEPTION
     WHEN OTHERS THEN
       v_default_locale := 'en_US';
  END;
  
  DELETE temp_table_session;
  
  v_stage:= 'Insert temp_table_session';
  INSERT INTO temp_table_session
     SELECT asset_code,cms_value,key
       FROM (SELECT asset_code,cms_value,key ,RANK() OVER(PARTITION BY key ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN (SELECT 'ELEMENTLABEL_'||cm_key_fragment from claim_form_step_element )
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT cf.cm_asset_code,cms_name, cms_code||'_'||cfse.cm_key_fragment,RANK() OVER(PARTITION BY cf.cm_asset_code,cms_code||'_'||cfse.cm_key_fragment ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_code_value  vw, claim_form_step_element cfse, 
                    claim_form_step cfs, claim_form cf, promotion p
              WHERE asset_code =  cfse.selection_pick_list_name 
                AND cfse.claim_form_step_id = cfs.claim_form_step_id 
                AND cfs.claim_form_id = cf.claim_form_id
                and cf.claim_form_id = p.claim_form_id
                AND p.promotion_id = p_in_promotion_id
                AND locale IN (v_default_locale, p_in_locale)
                AND cms_status = 'true' 
                AND claim_form_element_type_code <> 'copy'
              UNION ALL
             SELECT asset_code,cms_value,key ,RANK() OVER(PARTITION BY key,cms_value ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN (SELECT 'LABELTRUE_'||cm_key_fragment from claim_form_step_element 
                            UNION SELECT 'LABELFALSE_'||cm_key_fragment from claim_form_step_element )
                AND locale IN (v_default_locale, p_in_locale)
               ) WHERE rn = 1;
   
  DELETE TMP_APPROVER_CUMUL_NOMI_DETAIL;
   
  OPEN cur_appr(v_approver_is_admin, v_currency_value,v_payout_level_type, v_award_payout_type);

  v_stage := 'Fetching data from cursor...';
  LOOP    

    FETCH cur_appr BULK COLLECT          
     INTO v_rec_appr ;
 
        IF v_rec_appr.COUNT > 0 THEN    
           FORALL indx IN 1 .. v_rec_appr.COUNT         
           
           INSERT INTO TMP_APPROVER_CUMUL_NOMI_DETAIL 
                   (
                    claim_id ,                    
                    nominator_pax_id,             
                    nominator_name,               
                    submitted_date,               
                    is_cumulative_nomination,     
                    submitter_comments,           
                    why_attachment_name,          
                    why_attachment_url,           
                    certificate_id,               
                    more_info_comments        
                    )
           VALUES (
                    v_rec_appr(indx).claim_id ,                    
                    v_rec_appr(indx).nominator_pax_id,            
                    v_rec_appr(indx).nominator_name,               
                    v_rec_appr(indx).submitted_date,               
                    v_rec_appr(indx).is_cumulative_nomination,     
                    v_rec_appr(indx).submitter_comments,           
                    v_rec_appr(indx).why_attachment_name,          
                    v_rec_appr(indx).why_attachment_url,           
                    v_rec_appr(indx).certificate_id,               
                    v_rec_appr(indx).more_info_comments            
                   );     
        END IF; 
        
        EXIT WHEN cur_appr%NOTFOUND;   

  END LOOP; 
  
  CLOSE cur_appr;
   
  v_stage:= 'Return p_out_nominator_detail';
  OPEN p_out_nominator_detail FOR
   SELECT claim_id,
          submitted_date,
          nominator_pax_id,
          nominator_name,
          more_info_comments,
          submitter_comments,
          why_attachment_url,
          why_attachment_name,
          certificate_id
     FROM tmp_approver_cumul_nomi_detail an
    WHERE is_cumulative_nomination = 1;
         
  v_stage := 'Return Custom detail for approver';
  OPEN p_out_custom_dtl FOR
    SELECT cc.claim_id,
           claim_form_step_element_id,
           claim_form_step_element_name, 
           REPLACE(cc.cms_value,'DUMMYNULL',NULL) claim_form_step_element_desc  --08/22/2017  G6-2835
      FROM (SELECT claim_id,
                   claim_cfse_id, 
                   sequence_num,
                   claim_form_step_element_id,
                   claim_form_step_element_name,
                   LISTAGG(NVL(cms_value,'DUMMYNULL'),',') WITHIN GROUP(ORDER BY lvl,cms_value) cms_value     --08/22/2017  G6-2835
                 --  LISTAGG(cms_value,', ') WITHIN GROUP(ORDER BY cms_value) cms_value 
              FROM ( SELECT claim_id,
                            claim_cfse_id,
                            claim_form_step_element_id,
                            sequence_num,
                            cm_asset_code,
                            TO_NUMBER(lvl) lvl, --08/22/2017  G6-2835
                            (SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND INSTR(cms_code,'ELEMENTLABEL_'||cm_key_fragment,1) > 0) claim_form_step_element_name,
                            NVL((SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND (lower(cms_code) = lower(value||'_'||cm_key_fragment) or lower(cms_code) = lower('label'||value||'_'||cm_key_fragment))), value)   cms_value 
                       FROM ( SELECT cc.claim_id,
                                     SUBSTR(p.column_value,1,instr(p.column_value,'~',-1)-1) as value,      --08/22/2017 G6-2835
                                     SUBSTR(p.column_value,instr(p.column_value,'~',-1)+1) as lvl,          --08/22/2017 G6-2835
                                --   column_value as value,
                                     cc.claim_cfse_id,
                                     cf.cm_asset_code, 
                                     cfse.claim_form_step_element_id,
                                     cfse.sequence_num,
                                     cfse.cm_key_fragment,
                                     selection_pick_list_name
                                FROM promotion p,
                                     claim_form cf, 
                                     claim_form_step cfs, 
                                     claim_form_step_element cfse,
                                     claim_cfse cc,
                                     tmp_approver_cumul_nomi_detail ta,
                                     TABLE( CAST( MULTISET(
                                      SELECT SUBSTR(','||cc.value||',',
                                                   INSTR(','||cc.value||',', ',', 1, LEVEL)+1, 
                                                   INSTR(','||cc.value||',', ',', 1, LEVEL+1) - INSTR(','||cc.value||',', ',', 1, LEVEL)-1 
                                                   )|| '~'||LEVEL              --08/22/2017  G6-2835
                                                                     
                                        FROM dual
                                     CONNECT BY LEVEL <= REGEXP_COUNT(','||cc.value, ',') 
                                     ) AS sys.odcivarchar2list ) ) p                                  
                               WHERE p.promotion_id = p_in_promotion_id
                                 AND p.claim_form_id = cf.claim_form_id
                                 AND cf.claim_form_id = cfs.claim_form_id
                                 AND cfs.claim_form_step_id = cfse.claim_form_step_id
                                 AND cfse.claim_form_step_element_id = cc.claim_form_step_element_id 
                                 AND cc.value IS NOT NULL
                                 AND claim_form_element_type_code <> 'copy'
                                 and cc.claim_id = ta.claim_id
                            ))
                   GROUP BY claim_id,claim_cfse_id, sequence_num,claim_form_step_element_id,claim_form_step_element_name
           ) cc;
  
EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_promotion_id: '||p_in_promotion_id||
      ' ~ p_in_level_number: '||p_in_level_number||' ~ p_in_approver_id: '||p_in_approver_id||' ~ p_in_time_period_id: '||p_in_time_period_id||
      ' ~ p_in_submit_start_date: '||p_in_submit_start_date||' ~ p_in_submit_end_date: '||p_in_submit_end_date||' ~ p_in_status: '||p_in_status||
      '~ p_in_locale: '||p_in_locale||' ~ Error at stage :'||v_stage||'-->'||SQLERRM, NULL);
      p_out_returncode := 99;    
      OPEN p_out_nominator_detail FOR SELECT NULL FROM DUAL;
      OPEN p_out_custom_dtl  FOR SELECT NULL FROM DUAL;

END prc_list_appr_cummula_dtl_page ;

PROCEDURE prc_list_approver_elig_promos 
         ( p_in_approver_id         IN  NUMBER,
           p_out_returncode         OUT NUMBER,  
           p_out_promotion_detail   OUT SYS_REFCURSOR)
AS
/*******************************************************************************
 Purpose: To return the eligible promotions of approvers
 
  Person             Date         Comments
  -----------       --------      -----------------------------------------------------
  nagarajs          05/25/2016    Initial Creation                              
*******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_LIST_APPROVER_ELIG_PROMOS';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   
   v_stage                  VARCHAR2(500);
   v_approver_is_admin      NUMBER(10);
BEGIN

  p_out_returncode := 0;
  
  SELECT COUNT(1)
    INTO v_approver_is_admin
    FROM application_user
   WHERE user_id = p_in_approver_id
     AND user_type = 'bi';
     
  v_stage := 'Return promotion detail';
  OPEN p_out_promotion_detail FOR
    SELECT promotion_id, promotion_name
      FROM (SELECT p.promotion_id, p.promotion_name 
              FROM promotion p
             WHERE p.promotion_type = 'nomination'
               AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE
               AND EXISTS (SELECT 1
                             FROM claim_item_approver cia,
                                  claim_item ci,
                                  claim c
                            WHERE (cia.approver_user_id = p_in_approver_id OR v_approver_is_admin = 1)
                              AND (cia.claim_item_id = ci.claim_item_id OR c.claim_group_id = cia.claim_group_id)
                              AND ci.claim_id = c.claim_id
                              AND c.promotion_id = p.promotion_id
                           )
                                   
             UNION 
            SELECT p.promotion_id, p.promotion_name  
              FROM promotion p
             WHERE p.promotion_type = 'nomination'
               AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE
               AND EXISTS (SELECT 1
                             FROM claim_approver_snapshot cas,
                                  claim c
                            WHERE (cas.approver_user_id = p_in_approver_id OR v_approver_is_admin = 1)
                              AND (cas.claim_id = c.claim_id OR cas.claim_group_id = c.claim_group_id)
                              AND c.promotion_id = p.promotion_id
                              AND EXISTS ( SELECT 1
                                             FROM claim_recipient cr,
                                                  claim_item ci,
                                                  claim cl,
                                                  application_user au
                                            WHERE cr.claim_item_id = ci.claim_item_id
                                              AND ci.claim_id = cl.claim_id
                                              AND cl.claim_id = c.claim_id
                                              AND cr.participant_id = au.user_id
                                              AND au.is_active = 1
                                          )
                           )
             UNION 
            SELECT p.promotion_id, p.promotion_name 
              FROM promotion p
             WHERE p.promotion_type = 'nomination'
               AND NVL(p.approval_end_date,SYSDATE) >= SYSDATE
               AND EXISTS (SELECT 1
                              FROM approver a,
                                   approver_criteria ac,
                                   approver_option ao
                             WHERE (user_id = p_in_approver_id OR v_approver_is_admin = 1)
                               AND a.approver_criteria_id = ac.approver_criteria_id
                               AND ac.approver_option_id = ao.approver_option_id
                               AND ao.promotion_id = p.promotion_id
                           )
            
           )
     ORDER BY promotion_name;

EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_approver_id: '||p_in_approver_id
      ||' ~ Error at stage :'||v_stage||'-->'||SQLERRM, NULL);
      p_out_returncode := 99;
      OPEN p_out_promotion_detail FOR SELECT NULL FROM DUAL;
END prc_list_approver_elig_promos;

PROCEDURE prc_list_pend_nomination_extr
         ( p_in_promotion_id        IN  NUMBER,
           p_in_level_number        IN  NUMBER,
           p_in_approver_id         IN  NUMBER,
           p_in_time_period_id      IN  NUMBER,
           p_in_submit_start_date   IN  DATE,
           p_in_submit_end_date     IN  DATE,
           p_in_status              IN  VARCHAR2,
           p_in_locale              IN  VARCHAR2,
           p_out_returncode         OUT NUMBER,  
           p_out_pend_claim_dtl    OUT SYS_REFCURSOR)
AS
/*******************************************************************************************
 Purpose: To return the nomination detail and team members detail to the approvers in extract
 
  Person             Date         Comments
  -----------       --------      -----------------------------------------------------------
  nagarajs          07/22/2016    Initial Creation
  nagarajs          11/03/2016    Bug 69861 - Award amount is not saving when approval done for the approve in nominations approval page on award row in the table.  
  nagarajs          05/12/2017    G6-2424 - The award column remains empty when the approver extracts the excel sheet after approving the nomination with award.
  Chidamba          08/17/2017    G6-2844 - Approval Extract - Nomination Comments are flipping when we insert comma inbetween comments
  Chidamba          09/01/2017    G6-2871 - Report-Nominations-List of nominator and nominee -When admin extract the summary table in extract , <!DOCTYPE html> error message is displaying
  Chidamba          09/07/2017    G6-2941 - In Approver Extract Records are not displaying and junk value is displayed.  Added CLOB to the result set.
                    09/08/2017    G6-2844 - Replace HTML tag for WHY field comments. this is not applicable to G5 as we dont save html tags in (g5 db) 
  Ravi Dhanekula    11/30/2017    G6-3578 - FIxed issue with formatting of comments field.  
  Chidamba          03/23/2018    G6-3961/Bug 75841 - replace single apostrophe with 2 in v_csfe_name write out v_query in OTHERS error
  Gorantla          04/05/2018    Bug 75272 - apply fnc_format_comments to comments and "custom_f" cfse
  Gorantla          04/25/2018    G6-4036/Bug 76291 - Nomination Approver excel extract error
  Gorantla          09/06/2018    Bug 77584 - Time period not displayed adequately in the CSV extract, emails & the first time when the nominated winner logs in
  Ramkumar          11/20/2018    Bug 77938 - Applied from WIP 48919 fix
  Loganathan        05/16/2019    Bug 77584 - Time period not displayed adequately in the CSV extract, emails & the first time when the nominated winner logs in
  Gorantla          06/26/2019    Bug 77739 - Nomination with the comments of 4000 characters length is not loading on nomination approval page.
  Gorantla          08/13/2019    GitLab# 2233 Nomination specific approver promotion .csv approval extract file count not matching with nominee details in the screen
  Gorantla          08/23/2019    GitLab# 2288 Nomination with 4K and Formatted Character are facing issue in display content on various pages & extract  
********************************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_LIST_PEND_NOMINATION_EXTR';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';

   c_delimiter              CONSTANT VARCHAR2(10) := ''',''' ;
   c2_delimiter             CONSTANT VARCHAR2(10) := '''"''' ;
   
   v_stage                  VARCHAR2(500);
   v_approver_is_admin      NUMBER(10);
   v_pattern                VARCHAR2(15);
   v_cfse_name              VARCHAR2(4000);
   v_list_columns           VARCHAR2(4000);
   v_result_columns         VARCHAR2(4000);
   v_col_names              VARCHAR2(4000);
   v_is_why                 promo_nomination.is_why%TYPE;
   v_query                  CLOB;
   v_default_locale         os_propertyset.string_val%TYPE;
   
   v_string                 VARCHAR2(4000); -- 03/23/2018
   v_clob_length            NUMBER; -- 03/23/2018
   v_loops                  NUMBER; -- 03/23/2018
   v_loop_start             NUMBER; -- 03/23/2018
   
BEGIN

prc_execution_log_entry(c_process_name, c_release_level, 'INFO', 'Parms : p_in_promotion_id: '||p_in_promotion_id 
                          || ' p_in_level_number: '||p_in_level_number
                          || ' p_in_approver_id: '||p_in_approver_id
                          || ' p_in_time_period_id: '||p_in_time_period_id
                          || ' p_in_submit_start_date: '||p_in_submit_start_date
                          || ' p_in_submit_end_date: '||p_in_submit_end_date
                          || ' p_in_status: '||p_in_status
                          || ' p_in_locale: '||p_in_locale
                        , NULL);

  SELECT COUNT(1)
    INTO v_approver_is_admin
    FROM application_user
   WHERE user_id = p_in_approver_id 
     AND user_type = 'bi';

  BEGIN
    SELECT string_val 
      INTO v_default_locale
      FROM os_propertyset
     WHERE entity_name = 'default.language';
  EXCEPTION
    WHEN OTHERS THEN
       v_default_locale := 'en_US';
  END;
  
  v_stage := 'INSERT INTO temp_table_session';
  DELETE FROM temp_table_session;
  
  INSERT INTO temp_table_session
     SELECT asset_code,cms_value,key
       FROM (SELECT asset_code,cms_value,key ,  RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key = 'PROMOTION_TIME_PERIOD_NAME_'
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT asset_code,cms_name,cms_code,  RANK() OVER(PARTITION BY asset_code, cms_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn  
               FROM vw_cms_code_value 
              WHERE asset_code IN ('picklist.promo.nomination.behavior.items','picklist.approval.status.items')
                AND locale IN (v_default_locale, p_in_locale)
                AND cms_status = 'true' 
              UNION ALL
             SELECT asset_code, cms_value, key,  RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_locale,locale,1,2)) rn 
               FROM vw_cms_asset_value
              WHERE asset_code IN (SELECT badge_name FROM badge_rule )
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT asset_code,cms_value,key ,RANK() OVER(PARTITION BY key ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN (SELECT 'ELEMENTLABEL_'||cm_key_fragment from claim_form_step_element )
                AND locale IN (v_default_locale, p_in_locale)
              UNION ALL
             SELECT cf.cm_asset_code,cms_name, cms_code||'_'||cfse.cm_key_fragment,RANK() OVER(PARTITION BY cf.cm_asset_code,cms_code||'_'||cfse.cm_key_fragment ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_code_value  vw, claim_form_step_element cfse, 
                    claim_form_step cfs, claim_form cf, promotion p
              WHERE asset_code =  cfse.selection_pick_list_name 
                AND cfse.claim_form_step_id = cfs.claim_form_step_id 
                AND cfs.claim_form_id = cf.claim_form_id
                and cf.claim_form_id = p.claim_form_id
                AND p.promotion_id = p_in_promotion_id
                AND locale IN (v_default_locale, p_in_locale)
                AND cms_status = 'true' 
                AND claim_form_element_type_code <> 'copy'
              UNION ALL
             SELECT asset_code,cms_value,key ,RANK() OVER(PARTITION BY key,cms_value ORDER BY DECODE(p_in_locale,locale,1,2)) rn
               FROM vw_cms_asset_value 
              WHERE key IN (SELECT 'LABELTRUE_'||cm_key_fragment from claim_form_step_element 
                            UNION SELECT 'LABELFALSE_'||cm_key_fragment from claim_form_step_element )
                AND locale IN (v_default_locale, p_in_locale)
              ) WHERE rn = 1;
                       
  v_stage := 'Get Date pattern';
  BEGIN
    SELECT pattern INTO v_pattern FROM locale_date_pattern WHERE locale=p_in_locale;
  EXCEPTION WHEN no_data_found THEN 
    v_pattern :='MM/DD/YYYY';
  END;
  
  v_stage := 'Check is why';
  BEGIN
    SELECT is_why
      INTO v_is_why
      FROM promo_nomination
     WHERE promotion_id = p_in_promotion_id;
  EXCEPTION 
    WHEN OTHERS THEN 
    v_is_why :=1;
  END;
  
  v_stage := 'Get CFSE Name';
  SELECT listagg( '''"''||'''||cfse_name||'''||''"''||'',''||','') WITHIN GROUP(ORDER BY sequence_num)
    INTO v_cfse_name
     -- FROM ( SELECT DISTINCT claim_form_step_element_name cfse_name ,sequence_num -- 03/23/2018
    FROM ( SELECT DISTINCT REPLACE(claim_form_step_element_name,'''','''''') cfse_name ,sequence_num -- 03/23/2018
             FROM ( SELECT claim_id,
                           claim_cfse_id, 
                           sequence_num,
                           claim_form_step_element_id,
                           claim_form_step_element_name,
                           LISTAGG(cms_value,', ') WITHIN GROUP(ORDER BY cms_value) cms_value 
                      FROM ( SELECT claim_id,
                                    claim_cfse_id,
                                    claim_form_step_element_id,
                                    sequence_num,
                                    cm_asset_code,
                                    (SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND INSTR(cms_code,'ELEMENTLABEL_'||cm_key_fragment,1) > 0) claim_form_step_element_name,
                                    NVL((SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND (lower(cms_code) = lower(value||'_'||cm_key_fragment) or lower(cms_code) = lower('label'||value||'_'||cm_key_fragment))), value)   cms_value 
                               FROM ( SELECT cc.claim_id,
                                             column_value as value,
                                             cc.claim_cfse_id,
                                             cf.cm_asset_code, 
                                             cfse.claim_form_step_element_id,
                                             cfse.sequence_num,
                                             cfse.cm_key_fragment,
                                             selection_pick_list_name
                                        FROM promotion p,
                                             claim_form cf, 
                                             claim_form_step cfs, 
                                             claim_form_step_element cfse,
                                             claim_cfse cc,
                                             TABLE( CAST( MULTISET(
                                              SELECT SUBSTR(','||cc.value||',',
                                                           INSTR(','||cc.value||',', ',', 1, LEVEL)+1, 
                                                           INSTR(','||cc.value||',', ',', 1, LEVEL+1) - INSTR(','||cc.value||',', ',', 1, LEVEL)-1 
                                                           )
                                                                             
                                                FROM dual
                                             CONNECT BY LEVEL <= REGEXP_COUNT(','||cc.value, ',') 
                                             ) AS sys.odcivarchar2list ) ) p                                  
                                       WHERE p.promotion_id = p_in_promotion_id
                                         AND p.claim_form_id = cf.claim_form_id
                                         AND cf.claim_form_id = cfs.claim_form_id
                                         AND cfs.claim_form_step_id = cfse.claim_form_step_id
                                         AND cfse.claim_form_step_element_id = cc.claim_form_step_element_id 
                                         AND cc.value IS NOT NULL
                                         AND claim_form_element_type_code <> 'copy'
                                    ))
                           GROUP BY claim_id,claim_cfse_id, sequence_num,claim_form_step_element_id,claim_form_step_element_name
                )
         );
  
  v_stage := 'Get CFSE output columns'; 
  SELECT listagg(sequence_num||' as custom_f'||sequence_num,', ') WITHIN GROUP(ORDER BY sequence_num),
         listagg('''"''||to_clob(custom_f'||sequence_num||')||''"''||',''''||','||'''||') WITHIN GROUP(ORDER BY sequence_num)||''',''||', -- 06/26/2019
         listagg('custom_f'||sequence_num,', ') WITHIN GROUP(ORDER BY sequence_num)||','
    INTO v_list_columns,
         v_result_columns,
         v_col_names
    FROM ( SELECT DISTINCT sequence_num 
             FROM ( SELECT claim_id,
                           claim_cfse_id, 
                           sequence_num,
                           claim_form_step_element_id,
                           claim_form_step_element_name,
                           LISTAGG(cms_value,', ') WITHIN GROUP(ORDER BY cms_value) cms_value 
                      FROM ( SELECT claim_id,
                                    claim_cfse_id,
                                    claim_form_step_element_id,
                                    sequence_num,
                                    cm_asset_code,
                                    (SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND INSTR(cms_code,'ELEMENTLABEL_'||cm_key_fragment,1) > 0) claim_form_step_element_name,
                                    NVL((SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND (lower(cms_code) = lower(value||'_'||cm_key_fragment) or lower(cms_code) = lower('label'||value||'_'||cm_key_fragment))), value)   cms_value 
                               FROM ( SELECT cc.claim_id,
                                             column_value as value,
                                             cc.claim_cfse_id,
                                             cf.cm_asset_code, 
                                             cfse.claim_form_step_element_id,
                                             cfse.sequence_num,
                                             cfse.cm_key_fragment,
                                             selection_pick_list_name
                                        FROM promotion p,
                                             claim_form cf, 
                                             claim_form_step cfs, 
                                             claim_form_step_element cfse,
                                             claim_cfse cc,
                                             TABLE( CAST( MULTISET(
                                              SELECT SUBSTR(','||cc.value||',',
                                                           INSTR(','||cc.value||',', ',', 1, LEVEL)+1, 
                                                           INSTR(','||cc.value||',', ',', 1, LEVEL+1) - INSTR(','||cc.value||',', ',', 1, LEVEL)-1 
                                                           )
                                                                             
                                                FROM dual
                                             CONNECT BY LEVEL <= REGEXP_COUNT(','||cc.value, ',') 
                                             ) AS sys.odcivarchar2list ) ) p                                  
                                       WHERE p.promotion_id = p_in_promotion_id
                                         AND p.claim_form_id = cf.claim_form_id
                                         AND cf.claim_form_id = cfs.claim_form_id
                                         AND cfs.claim_form_step_id = cfse.claim_form_step_id
                                         AND cfse.claim_form_step_element_id = cc.claim_form_step_element_id 
                                         AND cc.value IS NOT NULL
                                         AND claim_form_element_type_code <> 'copy'
                                    ))
                           GROUP BY claim_id,claim_cfse_id, sequence_num,claim_form_step_element_id,claim_form_step_element_name
                     )
         );
       
-- 08/23/2019 start      
v_stage := 'Populate gtt table';   -- added this step bcs piplined function is not supporting for clob with 4K data   
INSERT INTO gtt_array_tbl
SELECT column_value FROM TABLE ( get_array_varchar (p_in_status));      
-- 08/23/2019 end   
                                     
  v_stage := 'Get Full query';

  v_query := 'SELECT textline FROM (
       SELECT 1,TO_CLOB('||c2_delimiter||'||''Nominee Name'' ||'||c2_delimiter||'||'||c_delimiter||'||'||  --09/01/2017
                c2_delimiter||'||''Date Submitted'' ||'||c2_delimiter||'||'||c_delimiter||'||'||
                c2_delimiter||'||''Nominator Name'' ||'||c2_delimiter||'||'||c_delimiter||'||'|| 
                c2_delimiter||'||''Status'' ||'||c2_delimiter||'||'||c_delimiter||'||'||
                c2_delimiter||'||''Award Amount'' ||'||c2_delimiter||'||'||c_delimiter||'||'|| 
                c2_delimiter||'||''Notification Date'' ||'||c2_delimiter||'||'||c_delimiter||'||'|| 
                c2_delimiter||'||''Time Period Name'' ||'||c2_delimiter||'||'||c_delimiter||'||'|| 
                CASE WHEN v_is_why = 1 THEN c2_delimiter||'||''Reason'' ||'||c2_delimiter||'||'||c_delimiter||'||' END
                || c2_delimiter||'||''More Info''||' ||c2_delimiter||'||'||c_delimiter||'||'
                ||CASE WHEN v_cfse_name IS NOT NULL THEN v_cfse_name ELSE NULL END|| 
                c2_delimiter||'||''Behavior''||' ||c2_delimiter||'||'||c_delimiter|| '||'||
                c2_delimiter||'||''Badges'' ||'||c2_delimiter||') Textline          --09/01/2017
           FROM dual      
          UNION ALL
         SELECT (ROWNUM+1),TO_CLOB('|| 
                c2_delimiter||'||nominee_name||'||c2_delimiter||'||'||c_delimiter||'||'||
                c2_delimiter||'||date_submitted||'||c2_delimiter||'||'||c_delimiter||'||'||
                c2_delimiter||'||nominator_name||'||c2_delimiter||'||'||c_delimiter||'||'||
                c2_delimiter||'||status||'||c2_delimiter||'||'||c_delimiter||'||'||
                c2_delimiter||'||award_amount||'||c2_delimiter||'||'||c_delimiter||'||'||
                c2_delimiter||'||notification_date||'||c2_delimiter||'||'||c_delimiter||'||'||
                c2_delimiter||'||time_period_name||'||c2_delimiter||'||'||c_delimiter||'||'||
                CASE WHEN v_is_why = 1 THEN c2_delimiter||'||reason||'||c2_delimiter||'||'||c_delimiter||'||' END
                ||c2_delimiter||')||TO_CLOB(more_info_comments||'||c2_delimiter||'||'||c_delimiter||'||'  -- 04/25/2018 added extra CLOB
                ||CASE WHEN v_cfse_name IS NOT NULL THEN v_result_columns ELSE NULL END||
                c2_delimiter||'||behavior_name||'||c2_delimiter||'||'||c_delimiter||'||'||
                c2_delimiter||'||badge_name||'||c2_delimiter||
           ') --09/07/2017 G6-2941 added CLOB  
            FROM ( WITH nominee AS             --05/16/2019 Bug#77584 nominee block has rewritten to show the pending nominns as well 
                  (select 
                            cas.claim_id,
                           cas.claim_item_id,
                           cas.promotion_id,
                           pnt.nomination_time_period_id,
                           pnt.time_period_name_asset_code,
                           cas.approval_round,               
                           cas.submission_date,
                           au_giv.first_name nominator_first_name,
                           cr.participant_id nominee_pax_id,
                           au_giv.last_name nominator_last_name,
                           au_rec.first_name nominee_first_name,
                           au_rec.last_name nominee_last_name,'||
--                           CASE WHEN v_is_why = 1 THEN 'REGEXP_REPLACE(REGEXP_REPLACE(REGEXP_REPLACE(REGEXP_REPLACE(nc.submitter_comments,''<[^>]+>'',''''),''"'',''''''"''''''),CHR(10),''''),''null;'','''') reason,' END -- 04/05/2018
                           CASE WHEN v_is_why = 1 THEN 'FNC_FORMAT_COMMENTS(nc.submitter_comments) reason,' END -- 04/05/2018
--                           ||'REGEXP_REPLACE(REGEXP_REPLACE(REGEXP_REPLACE(REGEXP_REPLACE(REGEXP_REPLACE(nc.more_info_comments,''<[^>]+>'',''''),''"'',''''''"''''''),CHR(10),''''),CHR(13),''''),''null;'','''') more_info_comments,'--11/30/2017 -- 04/05/2018
                           ||'FNC_FORMAT_COMMENTS(nc.more_info_comments) more_info_comments,' -- 04/05/2018
                           ||CASE WHEN v_cfse_name IS NOT NULL THEN v_col_names ELSE NULL END||
                          'ncb.behavior_name,
                           ncb.badge_name,
                           cas.approval_status_type,
                           time_period_start_date,
                           time_period_end_date,
                           cas.claim_group_id,
                           cntry.currency_code,
                           CASE WHEN NVL(cr.award_qty ,0) > 0 THEN cr.award_qty  --05/12/2017
                                WHEN NVL(cr.cash_award_qty ,0) > 0 THEN cr.cash_award_qty
                                WHEN NVL(cr.calculator_score ,0) > 0 THEN cr.calculator_score
                           END award_amount
from 
(select distinct  -- 08/13/2019 added distinct
c.claim_id,
ci.claim_item_id,
c.promotion_id,
c.approval_round,               
TRUNC(c.submission_date) submission_date,
ci.approval_status_type,
c.claim_group_id,cia.time_period_id,c.submitter_id
from 
claim C,claim_item ci,CLAIM_APPROVER_SNAPSHOT cas,claim_item_approver cia
where c.claim_id=ci.claim_id
and   C.PROMOTION_ID=NVL('''||p_in_promotion_id||''',c.promotion_id)
and   c.claim_id=cas.claim_id(+)
and   ci.CLAIM_ITEM_ID=cia.CLAIM_ITEM_ID(+)) cas,
claim_recipient cr,
(SELECT nc.claim_id, c.promotion_id, 
                                   nc.submitter_comments,
                                   nc.more_info_comments, 
                                   nomination_time_period_id
                              FROM nomination_claim nc,
                                   claim c
                             WHERE nc.claim_id = c.claim_id)nc,
promo_nomination_time_period pnt,
application_user au_giv,
application_user au_rec,
participant pa,
user_address ua,
country cntry,
(SELECT claim_id,
        listagg(behavior_name,'', '') WITHIN GROUP(ORDER BY behavior_name) behavior_name,
        listagg(badge_name,'', '') WITHIN GROUP(ORDER BY badge_name) badge_name
      FROM (SELECT ncb.claim_id,
        (select cms_name from temp_table_session where asset_code = ''picklist.promo.nomination.behavior.items'' and cms_code = behavior_type) behavior_name,
          (select cms_name from temp_table_session where asset_code = badge_name) badge_name 
                                     FROM promo_behavior pb,
                                          (SELECT bp.eligible_promotion_id , br.behavior_name, br.badge_name
                                             FROM badge_promotion bp,
                                                  badge_rule br
                                            WHERE bp.promotion_id = br.promotion_id
                                              AND br.behavior_name IS NOT NULL
                                          ) bad,
                                          nomination_claim_behaviors ncb
                                    WHERE pb.promotion_id = '''||p_in_promotion_id||'''
                                      AND pb.promotion_id = bad.eligible_promotion_id (+)
                                      AND pb.behavior_type = bad.behavior_name  (+)
                                      AND pb.behavior_type = ncb.behavior
                                    )
                               GROUP BY claim_id
                          )ncb,
                          (SELECT * 
                             FROM
                             (select *from  ( SELECT claim_id,  -- 06/26/2019
                                           sequence_num,
                                           ROW_NUMBER() OVER (PARTITION BY claim_id,  
                                                                                              sequence_num
                                                                                ORDER BY claim_id ) rownumber,  -- 06/26/2019
                             -- REPLACE(LISTAGG(NVL(cms_value,''DUMMYNULL''),'','') WITHIN GROUP(ORDER BY lvl,cms_value),''DUMMYNULL'',NULL) cms_value  --08/17/2017  --G6-2844
                             -- SUBSTR(XMLAGG(XMLELEMENT(E,cms_value,'','').EXTRACT(''//text()'') ORDER BY lvl).GetClobVal(),1,LENGTH(XMLAGG(XMLELEMENT(E,cms_value,'','').EXTRACT(''//text()'') ORDER BY lvl).GetClobVal())-1) cms_value --09/01/2017  -- commented out 06/26/2019
                                     --LISTAGG(cms_value,'', '') WITHIN GROUP(ORDER BY cms_value) cms_value  --08/17/2017 G6-2844
                                    -- SUBSTR (REGEXP_REPLACE(REGEXP_REPLACE (cms_value,''<.+?>''), ''<[^>]+>|\&(nbsp;)|(amp;)'', ''''),1,4000) cms_value  -- 06/26/2019 --commented out 08/23/2019
                                     dbms_lob.substr (REPLACE(REGEXP_REPLACE(REGEXP_REPLACE (cms_value,''<.+?>''), ''<[^>]+>|\&(nbsp;)|(amp;)'', ''''),chr(10)||chr(10)),4000,1) cms_value  -- 06/26/2019 -- 08/23/2019
                                      FROM ( SELECT claim_id,
                                                    sequence_num,
                                                    cm_asset_code,
                                                   -- TO_NUMBER(lvl) lvl,   --08/17/2017  G6-2844
                                                    (SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND INSTR(cms_code,''ELEMENTLABEL_''||cm_key_fragment,1) > 0) claim_form_step_element_name,
--                                                    REGEXP_REPLACE(REGEXP_REPLACE(REGEXP_REPLACE( --09/08/2017 -- 04/05/2018
                                                   -- FNC_FORMAT_COMMENTS( -- 04/05/2018  -- commented out 06/26/2019
                                                   -- NVL((SELECT cms_name from temp_table_session where asset_code = cm_asset_code AND (lower(cms_code) = lower(value||''_''||cm_key_fragment) or lower(cms_code) = lower(''label''||value||''_''||cm_key_fragment))), value) -- commented out 06/26/2019    
--                                                    ,''<[^>]+>'',''''),''"'',''''''"''''''),CHR(10),'''')   cms_value --09/08/2017 G6-2844    -- 04/05/2018
                                                   --  )   cms_value -- 04/05/2018 -- commented out 06/26/2019
                                                    NVL((SELECT to_clob(cms_name) from temp_table_session 
                                                      where asset_code = cm_asset_code 
                                                       AND (dbms_lob.compare(lower(cms_code), to_clob(lower(value||''_''||cm_key_fragment))) = 0
                                                         or dbms_lob.compare(lower(cms_code), to_clob(lower(''label''||value||''_''||cm_key_fragment))) = 0)),value) cms_value -- 06/26/2019
                                               FROM ( SELECT cc.claim_id,
                                                            -- SUBSTR(p.column_value,1,instr(p.column_value,''~'',-1)-1) as value,      --08/17/2017 G6-2844  -- commented out 06/26/2019
                                                            -- SUBSTR(p.column_value,instr(p.column_value,''~'',-1)+1) as lvl,          --08/17/2017 G6-2844    -- commented out 06/26/2019
                                                            -- column_value as value, --08/17/2017 G6-2844
                                                             cc.value,  -- 06/26/2019
                                                             cc.claim_cfse_id,
                                                             cf.cm_asset_code, 
                                                             cfse.sequence_num,
                                                             cfse.cm_key_fragment,
                                                             selection_pick_list_name
                                                        FROM promotion p,
                                                             claim_form cf, 
                                                             claim_form_step cfs, 
                                                             claim_form_step_element cfse,
                                                             claim_cfse cc
                                                             /*TABLE( CAST( MULTISET(                     -- commented out 06/26/2019
                                                              SELECT SUBSTR('',''||cc.value||'','',
                                                                           INSTR('',''||cc.value||'','', '','', 1, LEVEL)+1, 
                                                                           INSTR('',''||cc.value||'','', '','', 1, LEVEL+1) - INSTR('',''||cc.value||'','', '','', 1, LEVEL)-1 
                                                                           )|| ''~''||LEVEL              --08/17/2017   G6-2844
                                                                                             
                                                                FROM dual
                                                             CONNECT BY LEVEL <= REGEXP_COUNT('',''||cc.value, '','')  
                                                             ) AS sys.odcivarchar2list ) ) p */                                  
                                                       WHERE p.promotion_id = '''||p_in_promotion_id||'''
                                                         AND p.claim_form_id = cf.claim_form_id
                                                         AND cf.claim_form_id = cfs.claim_form_id
                                                         AND cfs.claim_form_step_id = cfse.claim_form_step_id
                                                         AND cfse.claim_form_step_element_id = cc.claim_form_step_element_id 
                                                         AND cc.value IS NOT NULL
                                                         AND claim_form_element_type_code <> ''copy''
                                                    )))   WHERE rownumber = 1) -- 06/26/2019 
                                         --  GROUP BY claim_id, sequence_num)  -- commented out 06/26/2019
                              PIVOT (MIN(to_char(cms_value))  for (sequence_num) in ('||v_list_columns||'))
                          )cu
where  cas.claim_item_id = cr.claim_item_id
AND    cas.claim_id = nc.claim_id
AND nc.promotion_id = pnt.promotion_id (+)
AND cas.time_period_id = pnt.nomination_time_period_id(+) -- 09/06/2018 --05/14/2019--outer join added
-- AND nc.nomination_time_period_id = pnt.nomination_time_period_id (+) -- 09/06/2018
--AND (pnt.nomination_time_period_id = '''||p_in_time_period_id||''' OR '''||p_in_time_period_id||''' IS NULL) --05/14/2019 Commented
AND cas.submitter_id = au_giv.user_id 
AND cr.participant_id = au_rec.user_id 
AND au_rec.is_active = 1
AND au_rec.user_id = pa.user_id
AND au_rec.user_id = ua.user_id (+)
AND ua.is_primary (+) = 1
AND ua.country_id = cntry.country_id (+)
AND cas.claim_id = ncb.claim_id (+)
AND cas.claim_id = cu.claim_id (+)
AND NOT EXISTS (SELECT 1
                                         FROM  claim_item_approver cia
                                        WHERE (cia.claim_item_id = cas.claim_item_id OR cas.claim_group_id = cia.claim_group_id)
                                          AND cia.approval_round = (cas.approval_round - 1)
                                          AND cia.notification_date > TRUNC(SYSDATE)
                                      ))     
                , approver_nom AS
                   (
                    SELECT c.claim_id,
                           c.claim_item_id,
                           c.promotion_id,
                           c.nomination_time_period_id,
                           c.time_period_name_asset_code,
                           cia.approval_round,               
                           TO_CHAR(TRUNC(c.submission_date),'''||v_pattern||''') AS date_submitted,
                           c.nominator_last_name||'', ''||c.nominator_first_name  AS nominator_name,
                           c.nominee_last_name||'', ''||c.nominee_first_name  AS nominee_name,
                           cia.notification_date,
                           cia.approval_status_type AS status,
                           (SELECT cms_name FROM temp_table_session WHERE asset_code = time_period_name_asset_code) time_period_name,'||
                           CASE WHEN v_is_why = 1 THEN 'reason,' END
                           ||'c.more_info_comments,'
                           ||CASE WHEN v_cfse_name IS NOT NULL THEN v_col_names ELSE NULL END||
                           '
                           c.claim_group_id,
                           c.currency_code,
                           c.behavior_name,
                           c.badge_name,
                           c.nominee_pax_id,
                           c.award_amount --05/12/2017
                     FROM nominee c,
                          claim_item_approver cia,
                          gtt_array_tbl  arr  -- 08/23/2019
                    WHERE (c.claim_item_id = cia.claim_item_id 
                       OR c.claim_group_id = cia.claim_group_id )
                      AND ( ('''||p_in_status||''' IS NULL 
                                  AND( (cia.approval_status_type IN (''more_info'', ''approv'' , ''winner'',''non_winner'') AND cia.approval_round = NVL('''||p_in_level_number||''',cia.approval_round) AND ('''||p_in_time_period_id||''' IS NOT NULL AND TRUNC(cia.date_approved) BETWEEN c.time_period_start_date AND c.time_period_end_date  ))
                                  OR (cia.approval_status_type IN (''more_info'', ''approv'' , ''winner'',''non_winner'') AND cia.approval_round = NVL('''||p_in_level_number||''',cia.approval_round) AND '''||p_in_time_period_id||''' IS NULL AND '''||p_in_submit_start_date||''' IS NULL AND '''||p_in_submit_end_date||''' IS NULL )
                                  OR (cia.approval_status_type IN (''more_info'', ''approv'' , ''winner'',''non_winner'') AND cia.approval_round = NVL('''||p_in_level_number||''',cia.approval_round) AND ('''||p_in_time_period_id||''' IS NULL AND TRUNC(cia.date_approved) BETWEEN '''||p_in_submit_start_date||''' AND NVL('''||p_in_submit_end_date||''',TRUNC(SYSDATE)) ))
                                  ))
                              OR ((cia.approval_status_type IN (''more_info'', ''approv'' , ''winner'',''non_winner'') AND  cia.approval_status_type = arr.column_desc AND cia.approval_round = NVL('''||p_in_level_number||''',cia.approval_round) AND ('''||p_in_time_period_id||''' IS NOT NULL AND TRUNC(cia.date_approved) BETWEEN c.time_period_start_date AND c.time_period_end_date  ))  -- 08/23/2019
                                  OR (cia.approval_status_type IN (''more_info'', ''approv'' , ''winner'',''non_winner'') AND cia.approval_status_type = arr.column_desc AND cia.approval_round = NVL('''||p_in_level_number||''',cia.approval_round) AND '''||p_in_time_period_id||''' IS NULL AND '''||p_in_submit_start_date||''' IS NULL AND '''||p_in_submit_end_date||''' IS NULL )  -- 08/23/2019
                                  OR (cia.approval_status_type IN (''more_info'', ''approv'' , ''winner'',''non_winner'') AND cia.approval_status_type = arr.column_desc AND cia.approval_round = NVL('''||p_in_level_number||''',cia.approval_round) AND ('''||p_in_time_period_id||''' IS NULL AND TRUNC(cia.date_approved) BETWEEN '''||p_in_submit_start_date||''' AND NVL('''||p_in_submit_end_date||''',TRUNC(SYSDATE)) )) -- 08/23/2019
                                  )
                            )
                    UNION ALL
                    SELECT c.claim_id,
                           c.claim_item_id,
                           c.promotion_id,
                           c.nomination_time_period_id,
                           c.time_period_name_asset_code,
                           c.approval_round,               
                           TO_CHAR(TRUNC(c.submission_date),'''||v_pattern||''') date_submitted,
                           c.nominator_last_name||'', ''||c.nominator_first_name  nominator_name,
                           c.nominee_last_name||'', ''||c.nominee_first_name  nominee_name,
                           NULL notification_date,
                           c.approval_status_type AS status,
                           (SELECT cms_name FROM temp_table_session WHERE asset_code = time_period_name_asset_code) time_period_name,'||
                           CASE WHEN v_is_why = 1 THEN 'reason,' END
                           ||'c.more_info_comments,'
                           ||CASE WHEN v_cfse_name IS NOT NULL THEN v_col_names ELSE NULL END||
                           ' 
                           c.claim_group_id,
                           c.currency_code,
                           c.behavior_name,
                           c.badge_name,
                           c.nominee_pax_id,
                           c.award_amount --05/12/2017
                     FROM nominee c,
                              gtt_array_tbl  arr  -- 08/23/2019
                    WHERE ( ('''||p_in_status||''' IS NULL 
                                  AND((c.approval_status_type = ''pend'' AND TRUNC(c.submission_date) BETWEEN NVL('''||p_in_submit_start_date||''',TRUNC(c.submission_date)) AND NVL('''||p_in_submit_end_date||''',TRUNC(SYSDATE))  AND c.approval_round = NVL('''||p_in_level_number||''',c.approval_round))))
                              OR ((c.approval_status_type = ''pend'' and c.approval_status_type = arr.column_desc AND TRUNC(c.submission_date) BETWEEN NVL('''||p_in_submit_start_date||''',TRUNC(c.submission_date)) AND NVL('''||p_in_submit_end_date||''',TRUNC(SYSDATE))  AND c.approval_round = NVL('''||p_in_level_number||''',c.approval_round))) -- 08/23/2019
                            ) 
                  )
           SELECT nominee_name,
                  date_submitted,
                  nominator_name,
                  (SELECT cms_name FROM temp_table_session WHERE asset_code = ''picklist.approval.status.items'' AND cms_code = status) status,                  
                   /*  --11/20/2018 commented
                     CASE WHEN status = ''pend'' AND  pml.award_payout_type = ''points'' AND pml.award_amount_type_fixed = 1 THEN pml.award_amount_fixed||'' Points''
                          WHEN status = ''pend'' AND pml.award_payout_type = ''cash'' AND pml.award_amount_type_fixed = 1 THEN pml.award_amount_fixed||'' ''||c.currency_code
                          WHEN status IN ( ''approv'', ''winner'', ''pend'') AND  pml.award_payout_type = ''other'' THEN pml.payout_description --11/03/2016
                          WHEN status IN ( ''approv'', ''winner'') AND pml.award_payout_type = ''cash'' THEN TO_CHAR(NVL(ac.cash_award_qty,c.award_amount))  --11/03/2016 --05/12/2017
                          WHEN status IN ( ''approv'', ''winner'') AND pml.award_payout_type = ''points''  THEN TO_CHAR(NVL(ac.quantity,c.award_amount))     --11/03/2016 --05/12/2017
                  */
                  --11/20/2018 start
                   CASE WHEN pml.award_payout_type = ''points'' AND pml.award_amount_type_fixed = 1 THEN pml.award_amount_fixed||'' Points''
                       WHEN pml.award_payout_type = ''cash'' AND pml.award_amount_type_fixed = 1 THEN pml.award_amount_fixed||'' ''||c.currency_code
                       WHEN pml.award_payout_type = ''other'' THEN pml.payout_description 
                  END award_amount,
                  notification_date,
                  time_period_name,
                  '||CASE WHEN v_is_why = 1 THEN 'reason,' END||'
                  more_info_comments,'
                  ||CASE WHEN v_cfse_name IS NOT NULL THEN v_col_names ELSE NULL END||
                  'behavior_name,
                  badge_name
             FROM approver_nom c,
                  promo_nomination pn,
                  promo_nomination_level pml
            WHERE EXISTS (SELECT 1
                            FROM claim_approver_snapshot cas
                           WHERE (cas.approver_user_id = '''||p_in_approver_id||''' OR '''||v_approver_is_admin||''' = 1)
                             AND (c.claim_id = cas.claim_id OR c.claim_group_id = cas.claim_group_id)
                          )
              AND c.promotion_id = pn.promotion_id
              AND status = ''pend'' 
              AND c.promotion_id = pml.promotion_id(+)
              AND ((c.approval_round = pml.level_index AND pn.payout_level_type = ''eachLevel'') OR (pn.payout_level_type = ''finalLevel''))
            UNION ALL
           SELECT nominee_name,
                  date_submitted,
                  nominator_name,
                  (SELECT cms_name FROM temp_table_session WHERE asset_code = ''picklist.approval.status.items'' AND cms_code = status) status,
                  CASE 
                       WHEN status IN ( ''approv'', ''winner'') AND  pml.award_payout_type = ''other'' THEN pml.payout_description --11/03/2016 --08/23/2018 Removed pend status
                       WHEN status IN ( ''approv'', ''winner'') AND pml.award_payout_type = ''cash'' THEN TO_CHAR(NVL(ac.cash_award_qty,c.award_amount))  --11/03/2016 --05/12/2017
                       WHEN status IN ( ''approv'', ''winner'') AND pml.award_payout_type = ''points''  THEN TO_CHAR(NVL(ac.quantity,c.award_amount))     --11/03/2016 --05/12/2017
            -- 11/20/2018 end
END award_amount,
                  notification_date,
                  time_period_name,
                  '||CASE WHEN v_is_why = 1 THEN 'reason,' END||'
                  more_info_comments,'
                  ||CASE WHEN v_cfse_name IS NOT NULL THEN v_col_names ELSE NULL END||
                  'behavior_name,
                  badge_name
             FROM approver_nom c,
                  promo_nomination pn,
                  promo_nomination_level pml,
                  activity ac
            WHERE EXISTS (SELECT 1
                            FROM claim_item_approver ciaa
                           WHERE (ciaa.approver_user_id = '''||p_in_approver_id||''' OR '''||v_approver_is_admin||''' = 1)
                             AND (c.claim_item_id = ciaa.claim_item_id OR c.claim_group_id = ciaa.claim_group_id)
                             AND ciaa.approval_round = c.approval_round
                          /* UNION ALL  --11/20/2018 commented
                          SELECT 1
                            FROM claim_approver_snapshot cas
                           WHERE (cas.approver_user_id = '''||p_in_approver_id||''' OR '''||v_approver_is_admin||''' = 1)
                             AND (c.claim_id = cas.claim_id OR c.claim_group_id = cas.claim_group_id)*/
                          )
              AND c.promotion_id = pn.promotion_id
              AND status <> ''pend'' --11/20/2018 added
              AND c.promotion_id = pml.promotion_id(+)
              AND ((c.approval_round = pml.level_index AND pn.payout_level_type = ''eachLevel'') OR (pn.payout_level_type = ''finalLevel''))
              AND ((c.claim_id = ac.claim_id OR c.claim_group_id = ac.claim_group_id) OR (ac.activity_id IS NULL OR status = ''pend'')) --05/12/2017 --09/07/2017 G6-2941
              AND c.approval_round = ac.approval_round(+)
              AND c.nominee_pax_id = ac.user_id (+)
              AND ac.is_submitter (+) = 0
                )
        )';
        
  --dbms_output.put_line(v_query);
  v_stage := 'Execute query';
  OPEN p_out_pend_claim_dtl FOR v_query;
  -- column header
     
   p_out_returncode := 0;
   
EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Parms : p_in_promotion_id: '||p_in_promotion_id||
      ' ~ p_in_level_number: '||p_in_level_number||' ~ p_in_approver_id: '||p_in_approver_id||' ~ p_in_time_period_id: '||p_in_time_period_id||
      ' ~ p_in_submit_start_date: '||p_in_submit_start_date||' ~ p_in_submit_end_date: '||p_in_submit_end_date||' ~ p_in_status: '||p_in_status||
      '~ p_in_locale: '||p_in_locale||' ~ Error at stage :'||v_stage||'-->'||SQLERRM, NULL);
      p_out_returncode := 99;
      OPEN p_out_pend_claim_dtl FOR SELECT NULL FROM dual;
      
      -- write out v_query for debugging -- 03/23/2018
      v_clob_length := DBMS_LOB.GETLENGTH(NVL(v_query,' '));
      v_loops := CEIL(v_clob_length/3800); -- 3800 is max length a VARCHAR2 will consistently hold
      v_loop_start := 1;
      FOR idx IN 1..v_loops LOOP
                v_string := DBMS_LOB.SUBSTR( v_query, 3800, v_loop_start );
                prc_execution_log_entry(c_process_name, 1, 'INFO',v_string,NULL);
                v_loop_start := v_loop_start + 3800;
      END LOOP;
END;


END;
/