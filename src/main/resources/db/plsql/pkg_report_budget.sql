CREATE OR REPLACE PACKAGE pkg_report_budget
  IS
  /*
   Person        Date        Comments
   -----------   ----------  -----------------------------------------------------
   Bethke        06/26/2006  Bug 12774: Fix p_rpt_budget_trans_detail.
   Arun S        07/11/2012  G5 report process changes
                             Added in parameters p_user_id, p_start_date, p_end_date, 
                             p_return_code and p_error_message 
   Ravi Dhanekula 04/17/2014 Changes done as per the new design of budgets (Time period method)
  */
  PROCEDURE P_RPT_BUDGET_PROMOTION
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

  PROCEDURE P_RPT_BUDGET_TRANS_DETAIL
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

  PROCEDURE P_RPT_BUDGET_USAGE_DETAIL
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);
--
--  PROCEDURE P_RPT_BUDGET_USAGE_SUMMARY
--  (p_user_id        IN  NUMBER,
--   p_start_date     IN  DATE, 
--   p_end_date       IN  DATE, 
--   p_return_code    OUT NUMBER,
--   p_error_message  OUT VARCHAR2);

  PROCEDURE P_INSERT_RPT_BUDGET_TRANS_DTL
   (p_budget_trans_dtl_rec IN rpt_budget_trans_detail%ROWTYPE);

  PROCEDURE P_INSERT_RPT_BUDGET_USAGE_DTL
   (p_budget_usage_dtl_rec IN rpt_budget_usage_detail%ROWTYPE);

FUNCTION fnc_get_user_name
  ( pi_user_id    IN application_user.user_id%TYPE)
  RETURN VARCHAR2;
  
--FUNCTION fnc_get_reallocated_amount
--(p_budget_id IN NUMBER)
--  RETURN NUMBER;

END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_report_budget IS

/*
-- murphyc    10/23/2007  add global variables
--                        add fnc_get_user_name

*/

-- global variables
g_merch_type      CONSTANT VARCHAR2(100) := 'merchandise';
g_tran_type_exp   CONSTANT VARCHAR2(100) := 'expenditure';
g_tran_type_dep   CONSTANT VARCHAR2(100) := 'deposit';

/*FUNCTION fnc_get_reallocated_amount
  (p_budget_id IN NUMBER)
     RETURN NUMBER
     IS
     /*--------------------------------------------------------------------------
-- purpose: To get re-allocated amount to be populated to usage_detail table:
--
----------------------------------------------------------------------------
-- Ravi Dhanekula     10/10/2012  initial

--------------------------------------------------------------------------*/
   /*  v_realloc_amount NUMBER;
     
     BEGIN     
         --select decode(sign(sum(trans_amount)),-1,'('||sum(trans_amount)*-1||')',sum(trans_amount)) INTO v_realloc_amount from rpt_budget_trans_detail where budget_id=p_budget_id and budget_history_id is not null;
          select NVL(sum(trans_amount),0) INTO v_realloc_amount from rpt_budget_trans_detail where budget_id=p_budget_id and budget_reallocation_history_id is not null;
          RETURN v_realloc_amount;
          EXCEPTION WHEN NO_DATA_FOUND THEN
          RETURN 0;             
     END;
*/
FUNCTION fnc_get_user_name
  ( pi_user_id    IN application_user.user_id%TYPE)
  RETURN VARCHAR2 IS
  
/*--------------------------------------------------------------------------
-- purpose: format name to:
--          LName Suffix, Fname MI
--
----------------------------------------------------------------------------
-- murphyc    10/23/2007  initial

--------------------------------------------------------------------------*/
  
  v_return    VARCHAR2(200);
  
BEGIN

  SELECT  INITCAP(TRIM(p.last_name||' '||p.suffix)||', '||
          TRIM(p.first_name||' '||p.middle_name)) pax_name
  INTO    v_return
  FROM    application_user p
  WHERE   user_id = pi_user_id
  AND     ROWNUM = 1;
  
  RETURN v_return;
  
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END;  -- FUNCTION fnc_get_user_name

PROCEDURE P_RPT_BUDGET_PROMOTION
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
IS

/*******************************************************************************
   Purpose:  Populate the rpt_budget_promotion reporting table

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray      11/16/2005 Initial Creation
   Raju N        01/02/2005 Award_budget_master_id column has been moved from
                            promo_recognition to promotion table.
   Arun S        07/11/2012 G5 report changes
                            Replaced looping process with single insert statement.
                            Utilized temp table for adding or removing buget 
                            records into report table
  Ravi Dhanekula  05/02/2013 Removed the incremental processing as it is causing issues with the situation of budget_master replced for a promotion. And the non-incremental process is very fast in this case.
  Ravi Dhanekula 04/17/2014 Changes done as per the new design of budgets (Time period method)
*******************************************************************************/

  c_process_name       CONSTANT execution_log.process_name%TYPE := 'P_RPT_BUDGET_PROMOTION';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
  c_created_by         CONSTANT rpt_awardmedia_summary.created_by%TYPE := 0;

  v_stage              execution_log.text_line%TYPE;
  v_rec_cnt            INTEGER;
  v_parm_list     execution_log.text_line%TYPE;   --03/11/2019  Bug 78631
BEGIN
v_stage := 'initialize variables'; 
v_parm_list := 
'p_user_id 			     :='||p_user_id 		      ||CHR(10)||
'p_start_date        :='||p_start_date        ||CHR(10)|| 
'p_end_date          :='||p_end_date          ; 
   v_stage := 'Procedure started';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

DELETE  rpt_budget_promotion;

    v_stage := 'Insert promo budget records into gtt_rpt_budget_promotion';
    INSERT INTO rpt_budget_promotion  (budget_id,budget_master_id,budget_segment_id,promotion_id,distribution_type )          
         SELECT b.budget_id, 
                bb.budget_master_id,
                bb.budget_segment_id,
                p.promotion_id,
                CASE WHEN BM.MULTI_PROMOTION=0 THEN 'one_to_one'
                ELSE 'shared'
                END  distribution_type
           FROM promotion p, 
                budget b,
                budget_segment bb, --04/11/2014
                budget_master bm
          WHERE
             p.award_budget_master_id = bb.budget_master_id
            AND p.award_budget_master_id IS NOT NULL
            AND bb.budget_segment_id = b.budget_segment_id
            AND bb.budget_master_id = bm.budget_master_id
          UNION                             --08/04/2014 Bug 55222 
          SELECT b.budget_id, 
                bb.budget_master_id,
                bb.budget_segment_id,
                p.promotion_id,
                CASE WHEN BM.MULTI_PROMOTION=0 THEN 'one_to_one'
                ELSE 'shared'
                END  distribution_type
           FROM promotion p,
                promo_recognition PR, 
                budget b,
                budget_segment bb, 
                budget_master bm
          WHERE
             pr.public_rec_budget_master_id = bb.budget_master_id
            AND pr.public_rec_budget_master_id IS NOT NULL
            AND p.promotion_id = pr.promotion_id
            AND bb.budget_segment_id = b.budget_segment_id
            AND bb.budget_master_id = bm.budget_master_id;

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

--    v_stage := 'Delete promo budget records those are not present in rpt_budget_promotion';
--    DELETE FROM rpt_budget_promotion rpt
--     WHERE NOT EXISTS (SELECT 'X'
--                         FROM budget
--                        WHERE budget_id = rpt.budget_id
--                       );

--   v_rec_cnt := SQL%ROWCOUNT;
--   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   v_stage := 'Procedure completed';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

   p_return_code := 0;
   
EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    p_error_message := v_stage || ': '|| SUBSTR(SQLERRM,1,250);
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ': ' || v_parm_list|| ': ' || SQLCODE || ', ' || SQLERRM, NULL);
END; -- p_rpt_budget_promotion

PROCEDURE P_RPT_BUDGET_TRANS_DETAIL
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
  IS
/*******************************************************************************
   Purpose:  Populate the rpt_budget_trans_detail reporting table
   
   NOTE: promo_id not required for non-merch budgets
          but IS REQUIRED for merch budgets

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray      11/14/2005 Initial Creation
   D Murray      12/19/2005 Added budget_status
   Raju N        01/02/2005 both the "approve' and "post" are considered as valid
   Bethke        06/26/2006 Bug 12774. Add cur_first_deposit cursor
   Sandip M      09/17/2007 Bug 17861  Changed the code to pick up the budget_status 
                                       from BUDGET table not from BUDGET_MASTER table 
   murphyc       10/23/2007 if merch, used merch_order.points for rpt.trans_amount
                            add merch_promo_cur, non_merch_promo_cur and merch_cur
                            add fnc_get_user_name
   M Lindvig     01/07/2007 Bug 18697  Merch orders now give correct data and 
                            include non-pax orders
   S Majumder    01/09/2008 Bug 18865 : Merch_Order table is only used for recognition
   M LINDVIG     03/25/2008 Bug # 19900 - It populates wrong data in budget usage report for non-pax promotions 
   Arun S        12/18/2009 Bug 21055 Fix. added  effective_date in cur_budget, 
                            added cursor cur_rep_bud_expen, when effective_date is not null then 
                            made to look expenditures record from cur_rep_bud_expen
  Arun S         29/04/2011 Bug # 36017  Fix, In cur_rep_bud_expen added union to capture Reversal expenditure.
  Arun S         01/24/2012 Bug # 39834 Fix, Use budget_value instead of transaction_amt from Journal when 
                            budget_value is not null changed in cur_expenditures and cur_rep_bud_expen
  Arun S         07/11/2012 G5 report changes
                            Removed deleting rpt_budget_trans_detail,
                            If record not present in report table then Inserted,
                            If record present and status changed then update,
                            If any budget removed then removed from report table.
  Ravi Dhanekula 10/05/2012  Removed process for NON-PAX
  Ravi Dhanekula 10/16/2012 Bug # 43328, Added processing for new column reallocated_amount on usage_detail and sumary tables.   
                          01/08/2014 Fix for the bug # 50887. The issue is with the creation of duplicate records in rpt_budget_trans_detail.
                         04/17/2014 Changes done as per the new design of budgets (Time period method)
  nagarajs       08/04/2014  Bug 55222 - Budget reports are not considering 'Public Recognition Budgets'
 Suresh J        02/05/2015 Bug 59387 - Fixed to update pax status when they become Inactive  
 nagarajs        01/24/2017 G5.6.3.3 changes
 nagarajs        02/10/2017  Expendtiure amount not showing in reports
 chidamba        03/02/2018  The ‘Closed’ budget master under any budget distribution type displays when the filter is queried even for ‘Active’ in the ‘Points budget Balance’ report
 Loganathan      03/11/2019  Bug 78631 - PKG_REPORTs without logs or return code 
*******************************************************************************/

  c_process_name       CONSTANT execution_log.process_name%TYPE := 'P_RPT_BUDGET_TRANS_DETAIL';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
  c_created_by         CONSTANT rpt_awardmedia_summary.created_by%TYPE := 0;
  c_rpt_date_created   CONSTANT DATE := SYSDATE;
  
  v_stage               VARCHAR2(500);
  v_ins_rec             VARCHAR2(1);
  budget_trans_rec      rpt_budget_trans_detail%ROWTYPE;
  v_budget_status       rpt_budget_trans_detail.budget_status%TYPE;
  v_rpt_bud_detl_id     rpt_budget_trans_detail.rpt_budget_trans_detail_id%TYPE;
  v_rec_cnt             INTEGER := 0;
  v_cnt_realloc         NUMBER;     --10/16/2012
  v_parm_list           execution_log.text_line%TYPE;   --03/11/2019  Bug 78631          
  --Deposit budget trans type
  --Expenditure budget trans type and non-merch-budget promos
  CURSOR cur_exp_non_merch IS
       SELECT rec_expen.budget_id,
              rec_expen.budget_master_id,
              rec_expen.budget_segment_id,
              rec_expen.status                  AS budget_status,
              rec_expen.journal_id,
              rec_expen.transaction_date        AS trans_date,
              rec_expen.transaction_amt         AS trans_amount,
              rec_expen.user_id                 AS recipient_user_id,
              rec_expen.pax_name                AS recipient_name,
              rec_expen.recipient_first_name, --01/24/2017
              rec_expen.recipient_last_name, --01/24/2017
              rec_expen.country_id           AS recipient_country_id,
              rec_expen.promotion_id,
              rec_expen.promotion_name
         FROM (WITH promo_list AS
                     (SELECT p.promotion_id,
                             p.promotion_name,
                             b.budget_id,
                             bb.budget_master_id,
                             b.budget_segment_id,
                             b.status,
                             b.effective_date
                        FROM promotion p, 
                             budget b,
                             budget_segment bb
                       WHERE NVL(p.award_type,'X') <> g_merch_type -- non-merch-budget promos only --02/10/2017
                         AND bb.budget_master_id = p.award_budget_master_id
                         AND bb.budget_segment_id = b.budget_segment_id
                         UNION                 --08/04/2014 Bug 55222 
                        SELECT p.promotion_id, --include public recognition budget promotions
                             p.promotion_name,
                             b.budget_id,
                             bb.budget_master_id,
                             b.budget_segment_id,
                             b.status,
                             b.effective_date
                        FROM promo_recognition pr, 
                             promotion p,
                             budget b,
                             budget_segment bb
                       WHERE NVL(p.award_type,'X')  <> g_merch_type -- non-merch-budget promos only
                         AND bb.budget_master_id = pr.public_rec_budget_master_id
                         AND pr.promotion_id = p.promotion_id
                         AND bb.budget_segment_id = b.budget_segment_id
                         ),
                    expen_lis AS
                     (SELECT pl.*,
                             j.journal_id,
                             j.user_id,
                             j.transaction_date,
                             j.transaction_type,
                             NVL(j.budget_value, j.transaction_amt) transaction_amt,
                             j.status_type,
                             fnc_get_user_name(j.user_id) pax_name,
                             au.first_name, --01/24/2017
                             au.last_name --01/24/2017
                        FROM promo_list pl,
                             journal j,
                             application_user au --01/24/2017
                       WHERE j.status_type IN ( 'post','approve')
                         AND NVL(j.promotion_id,0) = NVL(pl.promotion_id,0)
                         AND j.budget_id = pl.budget_id
                         AND p_start_date   < j.date_created
                         AND j.date_created <= p_end_date
                         AND j.user_id = au.user_id --01/24/2017
                       UNION 
                      SELECT pl1.*,
                             jl.journal_id, 
                             jl.user_id, 
                             jl.transaction_date,
                             jl.transaction_type, 
                             NVL(jl.budget_value, jl.transaction_amt) transaction_amt, 
                             jl.status_type,
                             fnc_get_user_name(jl.user_id) pax_name,
                             au.first_name, --01/24/2017
                             au.last_name  --01/24/2017
                        FROM promo_list pl1,
                             activity act, 
                             activity_journal actjl,
                             journal jl,
                             application_user au --01/24/2017
                       WHERE jl.journal_id          = actjl.journal_id
                         AND actjl.activity_id      = act.activity_id
                         AND jl.budget_id           = pl1.budget_id   
                         AND NVL(jl.promotion_id,0) = NVL(pl1.promotion_id,0)
                         AND jl.status_type         IN ( 'post','approve')
                         AND act.submission_date    >= pl1.effective_date
                         AND p_start_date           < jl.date_created
                         AND jl.date_created        <= p_end_date
                         AND jl.user_id             = au.user_id --01/24/2017
                       UNION
                      SELECT pl2.*,
                             jl.journal_id, 
                             jl.user_id, 
                             jl.transaction_date,
                             jl.transaction_type, 
                             NVL(jl.budget_value, jl.transaction_amt) transaction_amt, 
                             jl.status_type,
                             fnc_get_user_name(jl.user_id) pax_name,
                             au.first_name, --01/24/2017
                             au.last_name  --01/24/2017
                        FROM promo_list pl2,
                             journal jl,
                             application_user au --01/24/2017
                       WHERE jl.budget_id           = pl2.budget_id  
                         AND NVL(jl.promotion_id,0) = NVL(pl2.promotion_id,0)
                         AND jl.transaction_type    = 'reverse'
                         AND jl.status_type         IN ('post','approve')
                         AND jl.transaction_date    >= pl2.effective_date
                         AND p_start_date           < jl.date_created
                         AND jl.date_created        <= p_end_date 
                         AND jl.user_id             = au.user_id   --01/24/2017                      
                        )
                      SELECT el.budget_id,
                             el.budget_master_id,
                             el.budget_segment_id,
                             el.status,
                             el.journal_id,
                             el.transaction_date,
                             el.transaction_amt,
                             el.user_id,
                             uc.country_id,
                             el.pax_name,
                             el.first_name AS recipient_first_name, --01/24/2017
                             el.last_name AS recipient_last_name, --01/24/2017
                             el.promotion_id,
                             el.promotion_name
                        FROM expen_lis el,
                             (SELECT ua.user_id, ua.country_id
                                FROM user_address ua
                                    -- country c
                               WHERE --ua.country_id = c.country_id
                                  ua.is_primary = 1) uc
                       WHERE el.user_id = uc.user_id            
               ) rec_expen; 

  --Expenditure budget trans type and merch-budget promos and pax
  CURSOR cur_exp_merch_pax IS
       SELECT b.budget_id, 
              bb.budget_master_id,
              bb.budget_segment_id,
              b.status                              AS budget_status,
              j.journal_id,
              mo.date_created                       AS trans_date,
              mo.points                             AS trans_amt,
              mo.participant_id                     AS recipient_user_id,
              fnc_get_user_name(mo.participant_id)  AS recipient_name,
              au.first_name AS recipient_first_name, --01/24/2017
              au.last_name AS recipient_last_name, --01/24/2017
              uc.country_id                      AS recipient_country_id,
              p.promotion_id, 
              p.promotion_name,
              mo.merch_order_id
         FROM merch_order mo,
              application_user au, --01/24/2017 
              claim c, 
              budget b,
              budget_segment bb, --04/11/2014
              promotion p,
              activity a,
              activity_journal aj,
              journal j,
              (SELECT ua.user_id, ua.country_id
                 FROM user_address ua
                      --country c
                WHERE --ua.country_id = c.country_id
                   ua.is_primary = 1) uc
        WHERE mo.claim_id       = c.claim_id
          AND mo.participant_id = au.user_id (+) --01/24/2017 
          AND c.node_id         = NVL(b.node_id, c.node_id)
          AND p.award_budget_master_id = bb.budget_master_id
          AND bb.budget_segment_id = b.budget_segment_id
          AND c.promotion_id    = p.promotion_id
          AND p.award_type      = g_merch_type
          AND mo.merch_order_id = a.merch_order_id
          AND a.activity_id     = aj.activity_id
          AND j.journal_id      = aj.journal_id
          AND a.promotion_id    = p.promotion_id
          AND j.promotion_id    = p.promotion_id
          AND j.budget_id       = b.budget_id
          AND mo.participant_id = uc.user_id (+)
          AND p_start_date      < mo.date_created
          AND mo.date_created   <= p_end_date
         UNION                           --08/04/2014 Bug 55222  --include public recognition budget promotions
         SELECT b.budget_id, 
              bb.budget_master_id,
              bb.budget_segment_id,
              b.status                              AS budget_status,
              j.journal_id,
              mo.date_created                       AS trans_date,
              mo.points                             AS trans_amt,
              mo.participant_id                     AS recipient_user_id,
              fnc_get_user_name(mo.participant_id)  AS recipient_name,
              au.first_name AS recipient_first_name, --01/24/2017
              au.last_name AS recipient_last_name, --01/24/2017
              uc.country_id                      AS recipient_country_id,
              p.promotion_id, 
              p.promotion_name,
              mo.merch_order_id
         FROM merch_order mo, 
              application_user au, --01/24/2017 
              claim c, 
              budget b,
              budget_segment bb, 
              promotion p,
              promo_recognition pr,
              activity a,
              activity_journal aj,
              journal j,
              (SELECT ua.user_id, ua.country_id
                 FROM user_address ua
                WHERE ua.is_primary = 1) uc
        WHERE mo.claim_id       = c.claim_id
          AND mo.participant_id = au.user_id (+) --01/24/2017 
          AND c.node_id         = NVL(b.node_id, c.node_id)
          AND pr.public_rec_budget_master_id = bb.budget_master_id
          AND pr.promotion_id = p.promotion_id
          AND bb.budget_segment_id = b.budget_segment_id
          AND c.promotion_id    = p.promotion_id
          AND p.award_type      = g_merch_type
          AND mo.merch_order_id = a.merch_order_id
          AND a.activity_id     = aj.activity_id
          AND j.journal_id      = aj.journal_id
          AND a.promotion_id    = p.promotion_id
          AND j.promotion_id    = p.promotion_id
          AND j.budget_id       = b.budget_id
          AND mo.participant_id = uc.user_id (+)
          AND p_start_date      < mo.date_created
          AND mo.date_created   <= p_end_date;

BEGIN
v_stage := 'initialize variables'; 
v_parm_list := 
'p_user_id 			     :='||p_user_id 		  ||CHR(10)||
'p_start_date        :='||p_start_date        ||CHR(10)|| 
'p_end_date          :='||p_end_date          ; 
  v_stage := 'Procedure started';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
 
  v_stage := 'Remove budget trans detail records those are not present in budget table';
  DELETE FROM rpt_budget_trans_detail rpt
   WHERE NOT EXISTS (SELECT 'X'
                       FROM budget
                      WHERE budget_id = rpt.budget_id);
                      
v_stage := 'Insert Deposit/transfer records rpt_budget_trans_detail';      
              
MERGE INTO rpt_budget_trans_detail rpt
USING (SELECT bh.budget_id,
                      bb.budget_master_id,
                      b.budget_segment_id,
                      b.status,
                      bh.action_type,
                      bh.budget_history_id,
                     (bh.current_value_after_xaction - bh.current_value_before_xaction) trans_amount,
                      bh.date_created
                 FROM budget_history bh,
                      budget b,
                      budget_segment bb
                WHERE 
                  b.budget_id     = bh.budget_id
                  AND b.budget_segment_id = bb.budget_segment_id
                  AND bh.action_type IN ('deposit','transfer')
                  AND ((p_start_date    < bh.date_created AND bh.date_created <= p_end_date)
                          OR (p_start_date    < b.date_modified AND b.date_modified <= p_end_date))    
               ) rec_history
         ON (rec_history.budget_history_id = rpt.budget_history_id)
         WHEN MATCHED THEN UPDATE
         SET budget_status = rec_history.status,
                date_modified = SYSDATE,
                modified_by = p_user_id,
                version = version + 1
          WHERE NOT ( DECODE(rpt.budget_status,  rec_history.status,                    1, 0) = 1)
          WHEN NOT MATCHED THEN INSERT
                        (rpt_budget_trans_detail_id,
                 budget_id,
                 budget_master_id,
                 budget_segment_id,
                 budget_status,
                 budget_history_id,
                 trans_date,
                 budget_trans_type,
                 trans_amount,
                 date_created,
                 created_by,
                 version) 
                 VALUES 
           ( rpt_budget_trans_detail_pk_sq.NEXTVAL,
              rec_history.budget_id,
              rec_history.budget_master_id,
              rec_history.budget_segment_id,
              rec_history.status,
              rec_history.budget_history_id,
              rec_history.date_created,
              rec_history.action_type,
              rec_history.trans_amount,
              SYSDATE,
              p_user_id,
              0);

 /* v_stage := 'Insert rpt_budget_trans_detail for deposit/transfer budget trans type';
  FOR rec_bud_deposit IN cur_bud_deposit LOOP
  
    budget_trans_rec  := NULL;
    v_ins_rec         := 'N';
    v_rpt_bud_detl_id := NULL;
  
    IF rec_bud_deposit.budget_history_id IS NULL THEN

      --Original Budget value no budget_history records found
      BEGIN    

        v_stage := 'Check Original Budget value exists already';
        SELECT rpt_budget_trans_detail_id,
               budget_status
          INTO v_rpt_bud_detl_id,
               v_budget_status 
          FROM rpt_budget_trans_detail
         WHERE budget_id         = rec_bud_deposit.budget_id
           AND budget_master_id  = rec_bud_deposit.budget_master_id
           AND trans_date        = rec_bud_deposit.trans_date           
           ;
      
        IF v_budget_status <> rec_bud_deposit.budget_status THEN

          v_stage := 'Update status change for Original Budget value';
          UPDATE rpt_budget_trans_detail
             SET budget_status    = rec_bud_deposit.budget_status,
                 modified_by      = p_user_id,
                 date_modified    = SYSDATE,
                 version          = version + 1
           WHERE rpt_budget_trans_detail_id = v_rpt_bud_detl_id;

        END IF;
      
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_ins_rec := 'Y';
      END;
         
    ELSE

      BEGIN    
        v_stage := 'Check additional deposit exists already';
        SELECT rpt_budget_trans_detail_id,
               budget_status
          INTO v_rpt_bud_detl_id,
               v_budget_status 
          FROM rpt_budget_trans_detail
         WHERE budget_id         = rec_bud_deposit.budget_id
           AND budget_master_id  = rec_bud_deposit.budget_master_id
           AND budget_history_id = rec_bud_deposit.budget_history_id
           AND trans_date        = rec_bud_deposit.trans_date
           AND budget_trans_type = g_tran_type_dep
           AND budget_reallocation_history_id IS NULL   --10/16/2012
           ;

        IF v_budget_status <> rec_bud_deposit.budget_status THEN

          v_stage := 'Update status change for additional deposits';
          UPDATE rpt_budget_trans_detail
             SET budget_status    = rec_bud_deposit.budget_status,
                 modified_by      = p_user_id,
                 date_modified    = SYSDATE,
                 version          = version + 1 
           WHERE rpt_budget_trans_detail_id = v_rpt_bud_detl_id;

        END IF;

      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_ins_rec := 'Y';
      END;
      
    END IF;

    IF v_ins_rec = 'Y' THEN
      
      budget_trans_rec.rpt_budget_trans_detail_id := rpt_budget_trans_detail_pk_sq.NEXTVAL;  
      budget_trans_rec.budget_id                  := rec_bud_deposit.budget_id;
      budget_trans_rec.budget_master_id           := rec_bud_deposit.budget_master_id;
      budget_trans_rec.budget_status              := rec_bud_deposit.budget_status;
      budget_trans_rec.budget_trans_type          := g_tran_type_dep;
      budget_trans_rec.journal_id                 := NULL;
      budget_trans_rec.trans_date                 := rec_bud_deposit.trans_date;
      budget_trans_rec.trans_amount               := rec_bud_deposit.trans_amount;
      budget_trans_rec.recipient_user_id          := NULL;
      budget_trans_rec.recipient_name             := NULL;
      budget_trans_rec.promotion_id               := NULL;
      budget_trans_rec.promotion_name             := NULL;
      budget_trans_rec.date_created               := c_rpt_date_created;
      budget_trans_rec.created_by                 := c_created_by;
      budget_trans_rec.recipient_country_id := NULL;
      budget_trans_rec.budget_history_id          := rec_bud_deposit.budget_history_id;
      budget_trans_rec.merch_order_id             := NULL;
      budget_trans_rec.modified_by                := NULL;
      budget_trans_rec.date_modified              := NULL;
      budget_trans_rec.version                    := 1;
                  
      p_insert_rpt_budget_trans_dtl(budget_trans_rec);
      v_rec_cnt := v_rec_cnt + 1;        
    END IF;
                
  END LOOP; */

--  --Added on 10/16/2012
--  FOR rec_budget IN cur_budget LOOP
--
--    budget_trans_rec  := NULL;
--    budget_trans_rec.budget_master_id  := rec_budget.budget_master_id;
--    budget_trans_rec.budget_id         := rec_budget.budget_id;
--    budget_trans_rec.budget_status     := rec_budget.budget_status;
--    budget_trans_rec.budget_trans_type := g_tran_type_dep;
--    budget_trans_rec.date_created      := c_rpt_date_created;
--    budget_trans_rec.created_by        := c_created_by;
--
--    SELECT COUNT(*)
--      INTO v_cnt_realloc
--      FROM budget_reallocation_history
--     WHERE budget_id = rec_budget.budget_id;
--
--    IF v_cnt_realloc != 0 THEN
--      FOR rec_reallocation IN cur_reallocation (rec_budget.budget_id) LOOP
--
--        budget_trans_rec.trans_amount := rec_reallocation.amount;
--        budget_trans_rec.trans_date := rec_reallocation.date_created;
--        budget_trans_rec.budget_reallocation_history_id := rec_reallocation.budget_reallocation_history_id;
--
--        BEGIN
--          v_stage := 'Check Budget reallocation exists already';
--          SELECT rpt_budget_trans_detail_id,
--                 budget_status
--            INTO v_rpt_bud_detl_id,
--                 v_budget_status 
--            FROM rpt_budget_trans_detail
--           WHERE budget_id = rec_budget.budget_id
--             AND budget_reallocation_history_id = rec_reallocation.budget_reallocation_history_id;
--
--          IF v_budget_status <> rec_budget.budget_status THEN             
--            UPDATE rpt_budget_trans_detail
--               SET budget_status = rec_budget.budget_status,
--                   modified_by   = p_user_id,
--                   date_modified = SYSDATE,
--                   version       = version + 1
--             WHERE rpt_budget_trans_detail_id = v_rpt_bud_detl_id;
--          END IF;          
--             
--        EXCEPTION
--          WHEN NO_DATA_FOUND THEN
--            SELECT rpt_budget_trans_detail_pk_sq.NEXTVAL INTO budget_trans_rec.rpt_budget_trans_detail_id FROM dual;
--            budget_trans_rec.created_by := 2;
--            P_INSERT_RPT_BUDGET_TRANS_DTL(budget_trans_rec);        
--        END;
--
--      END LOOP; -- cur_reallocation
--    END IF; -- v_cnt_realloc != 0
--  
--  END LOOP;
    
  v_stage := 'Insert rpt_budget_trans_detail for expenditure budget trans type and non-merch-budget promos only';
  FOR rec_exp_non_merch IN cur_exp_non_merch LOOP
    
    budget_trans_rec := NULL;
    v_ins_rec         := 'N';
    v_rpt_bud_detl_id := NULL;

    BEGIN    
      v_stage := 'Check expenditure budget for non-merch-budget rec using journal_id';
      SELECT rpt_budget_trans_detail_id,
             budget_status
        INTO v_rpt_bud_detl_id,
             v_budget_status 
        FROM rpt_budget_trans_detail
       WHERE budget_id         = rec_exp_non_merch.budget_id
         AND budget_master_id  = rec_exp_non_merch.budget_master_id
         AND journal_id        = rec_exp_non_merch.journal_id
         AND trans_date        = rec_exp_non_merch.trans_date
         AND budget_trans_type = g_tran_type_exp;
  
      IF v_budget_status <> rec_exp_non_merch.budget_status THEN

        v_stage := 'Update status change for expenditure budget for non-merch-budget';
        UPDATE rpt_budget_trans_detail
           SET budget_status    = rec_exp_non_merch.budget_status,
               modified_by      = p_user_id,
               date_modified    = SYSDATE,
               version          = version + 1  
         WHERE rpt_budget_trans_detail_id = v_rpt_bud_detl_id;

      END IF;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_ins_rec := 'Y';
    END;

    IF v_ins_rec = 'Y' THEN
      
      budget_trans_rec.rpt_budget_trans_detail_id := rpt_budget_trans_detail_pk_sq.NEXTVAL;  
      budget_trans_rec.budget_id                  := rec_exp_non_merch.budget_id;
      budget_trans_rec.budget_master_id           := rec_exp_non_merch.budget_master_id;
      budget_trans_rec.budget_segment_id           := rec_exp_non_merch.budget_segment_id;
      budget_trans_rec.budget_status              := rec_exp_non_merch.budget_status;
      budget_trans_rec.budget_trans_type          := g_tran_type_exp;
      budget_trans_rec.journal_id                 := rec_exp_non_merch.journal_id;
      budget_trans_rec.trans_date                 := rec_exp_non_merch.trans_date;
      budget_trans_rec.trans_amount               := rec_exp_non_merch.trans_amount;
      budget_trans_rec.recipient_user_id          := rec_exp_non_merch.recipient_user_id;
      budget_trans_rec.recipient_name             := rec_exp_non_merch.recipient_name;
      budget_trans_rec.recipient_first_name       := rec_exp_non_merch.recipient_first_name; --01/24/2017
      budget_trans_rec.recipient_last_name        := rec_exp_non_merch.recipient_last_name; --01/24/2017
      budget_trans_rec.promotion_id               := rec_exp_non_merch.promotion_id;
      budget_trans_rec.promotion_name             := rec_exp_non_merch.promotion_name;
      budget_trans_rec.date_created               := c_rpt_date_created;
      budget_trans_rec.created_by                 := c_created_by;
      budget_trans_rec.recipient_country_id := rec_exp_non_merch.recipient_country_id;
      budget_trans_rec.budget_history_id          := NULL;
      budget_trans_rec.merch_order_id             := NULL;
      budget_trans_rec.modified_by                := NULL;
      budget_trans_rec.date_modified              := NULL;
      budget_trans_rec.version                    := 1;
                
      p_insert_rpt_budget_trans_dtl(budget_trans_rec);
      v_rec_cnt := v_rec_cnt + 1;        
    END IF;
    
    
  END LOOP;

  v_stage := 'Insert rpt_budget_trans_detail for expenditure budget_trans_type and merch-budget promos and pax';
  FOR rec_exp_merch_pax IN cur_exp_merch_pax LOOP
    
    budget_trans_rec := NULL;
    v_ins_rec         := 'N';
    v_rpt_bud_detl_id := NULL;

    BEGIN    
      v_stage := 'Check expenditure budget for merch-budget and pax records';
      SELECT rpt_budget_trans_detail_id,
             budget_status
        INTO v_rpt_bud_detl_id,
             v_budget_status 
        FROM rpt_budget_trans_detail
       WHERE budget_id        = rec_exp_merch_pax.budget_id
         AND budget_master_id = rec_exp_merch_pax.budget_master_id
         AND journal_id       = rec_exp_merch_pax.journal_id
         AND merch_order_id   = rec_exp_merch_pax.merch_order_id;

      IF v_budget_status <> rec_exp_merch_pax.budget_status THEN
        v_stage := 'Update status change for expenditure budget for merch-budget and pax records';
        UPDATE rpt_budget_trans_detail
           SET budget_status    = rec_exp_merch_pax.budget_status,
               modified_by      = p_user_id,
               date_modified    = SYSDATE,
               version          = version + 1 
         WHERE rpt_budget_trans_detail_id = v_rpt_bud_detl_id;

      END IF;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_ins_rec := 'Y';
    END;

    IF v_ins_rec = 'Y' THEN
      
      budget_trans_rec.rpt_budget_trans_detail_id := rpt_budget_trans_detail_pk_sq.NEXTVAL;  
      budget_trans_rec.budget_id                  := rec_exp_merch_pax.budget_id;
      budget_trans_rec.budget_master_id           := rec_exp_merch_pax.budget_master_id;
      budget_trans_rec.budget_segment_id           := rec_exp_merch_pax.budget_segment_id;
      budget_trans_rec.budget_status              := rec_exp_merch_pax.budget_status;
      budget_trans_rec.budget_trans_type          := g_tran_type_exp;
      budget_trans_rec.journal_id                 := rec_exp_merch_pax.journal_id;
      budget_trans_rec.trans_date                 := rec_exp_merch_pax.trans_date;
      budget_trans_rec.trans_amount               := rec_exp_merch_pax.trans_amt;
      budget_trans_rec.recipient_user_id          := rec_exp_merch_pax.recipient_user_id;
      budget_trans_rec.recipient_name             := rec_exp_merch_pax.recipient_name;
      budget_trans_rec.recipient_first_name       := rec_exp_merch_pax.recipient_first_name; --01/24/2017
      budget_trans_rec.recipient_last_name        := rec_exp_merch_pax.recipient_last_name; --01/24/2017
      budget_trans_rec.promotion_id               := rec_exp_merch_pax.promotion_id;
      budget_trans_rec.promotion_name             := rec_exp_merch_pax.promotion_name;
      budget_trans_rec.date_created               := c_rpt_date_created;
      budget_trans_rec.created_by                 := c_created_by;
      budget_trans_rec.recipient_country_id := rec_exp_merch_pax.recipient_country_id;
      budget_trans_rec.budget_history_id          := NULL;
      budget_trans_rec.merch_order_id             := rec_exp_merch_pax.merch_order_id;
      budget_trans_rec.modified_by                := NULL;
      budget_trans_rec.date_modified              := NULL;
      budget_trans_rec.version                    := 1;
            
      p_insert_rpt_budget_trans_dtl(budget_trans_rec);
      v_rec_cnt := v_rec_cnt + 1;        
    END IF;
    
  END LOOP;

  --v_stage := 'Insert rpt_budget_trans_detail for expenditure budget_trans_type and merch-budget promos and Non-pax';
--  FOR rec_exp_merch_non_pax IN cur_exp_merch_non_pax LOOP
--
--    budget_trans_rec := NULL;
--    v_ins_rec         := 'N';
--    v_rpt_bud_detl_id := NULL;
--
--    BEGIN    
--      v_stage := 'Check expenditure budget for merch-budget and Non pax records';
--      SELECT rpt_budget_trans_detail_id,
--             budget_status
--        INTO v_rpt_bud_detl_id,
--             v_budget_status 
--        FROM rpt_budget_trans_detail
--       WHERE budget_id        = rec_exp_merch_non_pax.budget_id
--         AND budget_master_id = rec_exp_merch_non_pax.budget_master_id
--         AND merch_order_id   = rec_exp_merch_non_pax.merch_order_id;
--
--      IF v_budget_status <> rec_exp_merch_non_pax.budget_status THEN
--
--        v_stage := 'Update status change for expenditure budget for merch-budget and Non pax records';
--        UPDATE rpt_budget_trans_detail
--           SET budget_status    = rec_exp_merch_non_pax.budget_status,
--               modified_by      = p_user_id,
--               date_modified    = SYSDATE,
--               version          = version + 1 
--         WHERE rpt_budget_trans_detail_id = v_rpt_bud_detl_id;
--
--      END IF;
--
--    EXCEPTION
--      WHEN NO_DATA_FOUND THEN
--        v_ins_rec := 'Y';
--    END;
--
--    IF v_ins_rec = 'Y' THEN
--      
--      budget_trans_rec.rpt_budget_trans_detail_id := rpt_budget_trans_detail_pk_sq.NEXTVAL;  
--      budget_trans_rec.budget_id                  := rec_exp_merch_non_pax.budget_id;
--      budget_trans_rec.budget_master_id           := rec_exp_merch_non_pax.budget_master_id;
--      budget_trans_rec.budget_status              := rec_exp_merch_non_pax.budget_status;
--      budget_trans_rec.budget_trans_type          := g_tran_type_exp;
--      budget_trans_rec.trans_date                 := rec_exp_merch_non_pax.trans_date;
--      budget_trans_rec.trans_amount               := rec_exp_merch_non_pax.trans_amt;
--      budget_trans_rec.recipient_name             := rec_exp_merch_non_pax.non_pax_name;
--      budget_trans_rec.promotion_id               := rec_exp_merch_non_pax.promotion_id;
--      budget_trans_rec.promotion_name             := rec_exp_merch_non_pax.promotion_name;
--      budget_trans_rec.date_created               := c_rpt_date_created;
--      budget_trans_rec.created_by                 := c_created_by;
--      budget_trans_rec.recipient_country_id := NULL;
--      budget_trans_rec.budget_history_id          := NULL;
--      budget_trans_rec.merch_order_id             := rec_exp_merch_non_pax.merch_order_id;
--      budget_trans_rec.modified_by                := NULL;
--      budget_trans_rec.date_modified              := NULL;
--      budget_trans_rec.version                    := 1;
--            
--      p_insert_rpt_budget_trans_dtl(budget_trans_rec);
--      v_rec_cnt := v_rec_cnt + 1;        
--    END IF;
--  
--  END LOOP;

  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', 'Insert report table (' || v_rec_cnt || ' records processed)', NULL);
   
  v_stage := 'Delete removed budget if any';
  DELETE FROM rpt_budget_trans_detail rpt
   WHERE NOT EXISTS (SELECT 'X'
                       FROM budget
                      WHERE budget_id = rpt.budget_id); 

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

  v_stage := 'Update budget status for inactive pax';   --02/05/2015
    UPDATE rpt_budget_trans_detail d      --02/05/2015
       SET d.budget_status = 'inactive'
    WHERE     d.budget_status = 'active'
           AND EXISTS  --Checks if the budget type is PAX
               (SELECT bm.budget_master_id
                                                   FROM budget_master bm
                                                  WHERE     bm.budget_type =
                                                               'pax'
                                                        AND bm.budget_master_id =
                                                               d.budget_master_id)
           AND EXISTS --Checks if the PAX became inactive
                  (SELECT au.user_id
                     FROM application_user au
                    WHERE au.user_id = d.recipient_user_id AND au.is_active = 0);
                    
 v_stage := 'Update budget status for closed budget';   --03/02/2018
    UPDATE rpt_budget_trans_detail d      --03/02/2018
       SET d.budget_status = 'closed'
           ,d.modified_by   = p_user_id
           ,d.date_modified = SYSDATE
     WHERE d.budget_trans_type = 'expenditure'
           AND EXISTS  --Checks if the budget became inactive
               (SELECT bm.budget_master_id
                  FROM budget_master bm
                 WHERE bm.is_active = 0
                   AND bm.budget_master_id = d.budget_master_id);


   v_stage := 'Procedure completed';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

   p_return_code := 0;
           
EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    p_error_message := v_stage || ': '|| SUBSTR(SQLERRM,1,250);
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ': ' ||v_parm_list|| ': ' ||SQLCODE || ', ' || SQLERRM, NULL);                                                
END; -- p_rpt_budget_trans_detail

PROCEDURE P_RPT_BUDGET_USAGE_DETAIL
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
  IS 
/*******************************************************************************
   Purpose:  Populate the rpt_budget_usage_detail reporting tables
   
   
   ** NOTE ** Cost center information is still missing - this is a change order
              that will be added to the budget_master table.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray      11/14/2005 Initial Creation
   D Murray      12/19/2005 Added budget_status
   Sandip M      09/17/2007 Bug 17861  Changed the code to pick up the budget_status 
                                       from BUDGET table not from BUDGET_MASTER table   
   Matt L        11/8/2007  Addition details for Budget Details Extract
   iyer,Satish   05/13/2007 Added NVL to node_id check for Bug # 20524
   Arun S        07/11/2012 G5 report changes
                            Removed entire deleting of rpt_budget_usage_detail,
                            If record not present in report table then Inserted,
                            If record present and column value changed then update,
                            If any budget removed then removed from report table.
Ravi Dhanekula  10/16/2012 Bug # 43328, Added processing for new column reallocated_amount on usage_detail and sumary tables.
                01/15/2014  Fixed the bug # 51084
Suresh J        02/05/2015 Bug 59387 - Fixed to update pax status when they become Inactive                         
Ravi Dhanekula  09/16/2015 Bug # 64058
nagarajs        10/09/2015 Bug 64249 - 'Transfer' type of amounts are not included to calculate the budget
                          amount in report refresh process
nagarajs       01/18/2015 Bug 65256 - P_RPT_BUDGET_USAGE_DETAIL duplicates
Ravi Dhanekula 01/29/2016 Bug 65423 - Duplicate Budgets Display on Budget Summary Page when Closing out Budget
Sherif Basha   12/08/2016 Bug 70431 - Report Refresh Process is Failing (Fix for Multipromo central budget scenarios failure)
 nagarajs      02/13/2017 Revert back Bug 70431 and  G5 6.3.3 - Fix to show one row per central/shared budget instead of a row per promotio in report
                          to avoid the repetition of same numbers for those promotions sharing budget master which is messing up totals.
DeepakrajS     04/08/2019 Bug 78216 - Pax name updated in overview is not getting updated in Budget report
Loganathan 	   05/31/2019 Bug 79052 - Multiple budget time periods are not appearing in Points Budget Balance report                           
*******************************************************************************/

  CURSOR cur_detail IS
    SELECT b.budget_id, 
           bb.budget_master_id,
           bb.budget_segment_id,
           b.user_id, 
           b.node_id, 
           b.original_value,
           bb.start_date, 
           bb.end_date, 
           b.status, 
           NVL(rpt_dep.amount,0) budget_amt,
           NVL(rpt_exp.amount,0) budget_used,
           (rpt_dep.amount - NVL(rpt_exp.amount,0)) budget_remaining,
           bm.cm_asset_code, 
           bm.name_cm_key, 
           decode(bm.multi_promotion,0,'one_to_one',1,'shared') multi_promotion,
           bm.overrideable, 
           bm.budget_type, 
           bm.final_payout_rule, 
           bm.is_active,
           uc.country_id budget_owner_cntry_id
      FROM budget b, 
           budget_master bm,
           budget_segment bb,
           (SELECT ua.user_id, ua.country_id
              FROM user_address ua
                   --country c
             WHERE --ua.country_id = c.country_id
                ua.is_primary = 1) uc,
           (SELECT budget_id, 
                   SUM(trans_amount) amount
              FROM rpt_budget_trans_detail
             WHERE budget_trans_type IN ( 'deposit', 'transfer') --10/09/2015 Added transfer type
             and budget_reallocation_history_id is null     --10/16/2012
             GROUP BY budget_id) rpt_dep,
           (SELECT budget_id, 
                   SUM(NVL(trans_amount,0)) amount
              FROM rpt_budget_trans_detail
             WHERE budget_trans_type = 'expenditure'
             and budget_reallocation_history_id is null     --10/16/2012
             GROUP BY budget_id) rpt_exp  
     WHERE b.budget_segment_id = bb.budget_segment_id
       AND bb.budget_master_id = bm.budget_master_id
       AND b.user_id          = uc.user_id (+)
       AND b.budget_id        = rpt_dep.budget_id (+)
       AND b.budget_id        = rpt_exp.budget_id (+)
       AND (p_start_date      < b.date_created OR p_start_date    < b.date_modified OR p_start_date < bb.date_created OR p_start_date    < bb.date_modified) --Bug # 65423 Added check for Budget_segment table too
       AND (b.date_created    <= p_end_date    OR b.date_modified <= p_end_date OR bb.date_created    <= p_end_date    OR bb.date_modified <= p_end_date) --Bug # 65423 Added check for Budget_segment table too
     ORDER BY bb.budget_master_id, b.budget_id, b.node_id, b.user_id;

  CURSOR cur_promotion_name (p_master_budget_id NUMBER) IS
   SELECT promotion_name
     FROM promotion
    WHERE award_budget_master_id = p_master_budget_id;

  CURSOR cur_budget_promo_owner (p_master_budget_id NUMBER) IS
   SELECT p.promotion_id, p.promotion_name
     FROM promotion p, budget b, budget_segment bb
    WHERE p.award_budget_master_id = bb.budget_master_id
      AND bb.budget_segment_id = b.budget_segment_id
      AND bb.budget_master_id = p_master_budget_id
      AND p.promotion_status = 'live'
      AND b.user_id IS NULL
      AND b.node_id IS NULL
      UNION  --09/16/2015 --01/18/2016 Removed UNION ALL
      SELECT p.promotion_id, p.promotion_name
     FROM promo_recognition pr,promotion p, budget b, budget_segment bb
    WHERE pr.public_rec_budget_master_id = bb.budget_master_id
      AND bb.budget_segment_id = b.budget_segment_id
      AND bb.budget_master_id = p_master_budget_id
      AND p.promotion_status = 'live'
      AND p.promotion_id= pr.promotion_id
      AND b.user_id IS NULL
      AND b.node_id IS NULL
      AND ROWNUM<2; --01/15/2014

   c_process_name             CONSTANT execution_log.process_name%TYPE := 'P_RPT_BUDGET_USAGE_DETAIL';
   c_release_level            CONSTANT execution_log.release_level%TYPE := 2.0;
   c_created_by               CONSTANT rpt_awardmedia_summary.created_by%TYPE := 0;

   v_stage                    VARCHAR2(500);
   v_promotion_string         rpt_budget_usage_detail.promotion_string%TYPE;
   budget_detail_rec          rpt_budget_usage_detail%ROWTYPE;
   v_budget_master_id         budget_master.budget_master_id%TYPE := 0;
   v_user_id                  budget.user_id%TYPE := 0;
   v_node_id                  budget.node_id%TYPE := 0;
   v_promo_count              NUMBER;
   v_ins_rec                  VARCHAR2(1);
   v_rpt_id                   rpt_budget_usage_detail.rpt_budget_usage_detail_id%TYPE;               
   v_budget_amt               rpt_budget_usage_detail.budget_amt%TYPE;
   v_budget_used              rpt_budget_usage_detail.budget_used%TYPE;
   v_budget_remaining         rpt_budget_usage_detail.budget_remaining%TYPE;
   v_budget_status            rpt_budget_usage_detail.budget_status%TYPE;
   v_is_active                rpt_budget_usage_detail.is_active%TYPE;
   v_start_date               rpt_budget_usage_detail.start_date%TYPE;
   v_end_date                 rpt_budget_usage_detail.end_date%TYPE;
   v_inception_date           rpt_budget_usage_detail.inception_date%TYPE;
   v_overrideable             rpt_budget_usage_detail.overrideable%TYPE;
   v_rule_for_final_payout    rpt_budget_usage_detail.rule_for_final_payout%TYPE;
   v_rec_cnt                  INTEGER := 0;
   v_reallocated_amount       rpt_budget_usage_detail.reallocated_amount%TYPE;
   v_parm_list                execution_log.text_line%TYPE;   --03/11/2019  Bug 78631         
BEGIN
v_stage := 'initialize variables'; 
v_parm_list := 
'p_user_id 			     :='||p_user_id 		  ||CHR(10)||
'p_start_date        :='||p_start_date        ||CHR(10)|| 
'p_end_date          :='||p_end_date          ; 

  v_stage := 'Procedure started';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  v_stage := 'Remove budget usage detail records those are not present in budget table';
  DELETE FROM rpt_budget_usage_detail rpt
   WHERE NOT EXISTS (SELECT 'X'
                       FROM budget
                      WHERE budget_id = rpt.budget_id);

  FOR rec_detail IN cur_detail LOOP

    v_promo_count := 0;         --02/13/2017
    budget_detail_rec := NULL;  --02/13/2017

    budget_detail_rec.budget_id          := rec_detail.budget_id;
    budget_detail_rec.budget_name_cm_asset_code := rec_detail.cm_asset_code;
    budget_detail_rec.budget_name_cm_key := rec_detail.name_cm_key;
    budget_detail_rec.budget_amt         := rec_detail.budget_amt;
    budget_detail_rec.budget_used        := rec_detail.budget_used;
    --budget_detail_rec.budget_remaining   := rec_detail.budget_remaining; 
    budget_detail_rec.budget_master_id   := rec_detail.budget_master_id;
    budget_detail_rec.budget_segment_id  := rec_detail.budget_segment_id;
    budget_detail_rec.multi_promotion    := rec_detail.multi_promotion;
    budget_detail_rec.budget_status      := rec_detail.status;
    budget_detail_rec.inception_date     := rec_detail.start_date;
    budget_detail_rec.overrideable       := rec_detail.overrideable;
    budget_detail_rec.rule_for_final_payout := rec_detail.final_payout_rule;
    budget_detail_rec.cap_amount         := rec_detail.original_value;
    budget_detail_rec.budget_master_name := fnc_cms_asset_code_value(rec_detail.cm_asset_code);
    budget_detail_rec.is_active          := rec_detail.is_active;
    budget_detail_rec.start_date         := rec_detail.start_date;
    budget_detail_rec.end_date           := rec_detail.end_date;
    budget_detail_rec.budget_type        := rec_detail.budget_type;
    budget_detail_rec.node_owner         := NULL;
    v_promotion_string                   := NULL;
    budget_detail_rec.budget_owner_cntry_id := rec_detail.budget_owner_cntry_id;
    budget_detail_rec.date_created       := SYSDATE;
    budget_detail_rec.created_by         := p_user_id;
    budget_detail_rec.version            := 1;
--    budget_detail_rec.reallocated_amount := pkg_report_budget.fnc_get_reallocated_amount(rec_detail.budget_id); --10/16/2012
    budget_detail_rec.budget_remaining   := rec_detail.budget_remaining + NVL(budget_detail_rec.reallocated_amount,0); --10/16/2012  --Added re-allocated amount to budget_remaining
    v_ins_rec := 'N';

   --Gets promotion count
    BEGIN 
      v_stage := 'Gets promotion count';
      /*SELECT COUNT(*)  --02/13/2017
        INTO v_promo_count
        FROM promotion
       WHERE cash_budget_master_id = budget_detail_rec.budget_master_id;
       
   
      IF(v_promo_count <= 5) THEN*/

        --FOR rec_promotion_name IN cur_promotion_name(budget_detail_rec.budget_master_id) LOOP --02/13/2017
        FOR rec_promotion_name IN cur_budget_promo_owner(budget_detail_rec.budget_master_id) LOOP --02/13/2017
          v_promo_count := v_promo_count + 1;   --02/13/2017
          IF v_promo_count > 5 THEN             --02/13/2017
            budget_detail_rec.promotion_string := '5+ Promotions';
            GOTO skip_promo;
          END IF;
          v_promotion_string := v_promotion_string || rec_promotion_name.promotion_name || '~';
           
        END LOOP;
        
        budget_detail_rec.promotion_string := substr(v_promotion_string,1, length(v_promotion_string)-1);
        
      /*ELSE
        budget_detail_rec.promotion_string := '5+ Promotions';
      END IF;*/
      
    EXCEPTION
      WHEN OTHERS THEN
      v_promotion_string := NULL;
    END;
    <<skip_promo>> --02/13/2017
    BEGIN
      v_stage := 'Check budget details in report table';
           SELECT rpt_budget_usage_detail_id,
                 budget_amt,
                 budget_used,
                 budget_remaining,
                 budget_status,
                 inception_date,
                 overrideable,
                 rule_for_final_payout,
                 promotion_string,
                 is_active,
                 start_date,
                 end_date,
                 reallocated_amount
            INTO v_rpt_id,
                 v_budget_amt,
                 v_budget_used,
                 v_budget_remaining,
                 v_budget_status,
                 v_inception_date,
                 v_overrideable,
                 v_rule_for_final_payout,
                 v_promotion_string,
                 v_is_active,
                 v_start_date,
                 v_end_date,
                 v_reallocated_amount      
            FROM rpt_budget_usage_detail
           WHERE budget_id  = rec_detail.budget_id
             AND budget_master_id = rec_detail.budget_master_id;

      v_stage := 'Check for changes in budget details with report table';
      IF NVL(v_budget_amt, 0)       <> NVL(budget_detail_rec.budget_amt, 0)       OR
         NVL(v_budget_used, 0)      <> NVL(budget_detail_rec.budget_used, 0)      OR  
         NVL(v_budget_remaining, 0) <> NVL(budget_detail_rec.budget_remaining, 0) OR
         v_budget_status            <> budget_detail_rec.budget_status    OR
         v_inception_date           <> budget_detail_rec.inception_date   OR
         v_overrideable             <> budget_detail_rec.overrideable     OR
         NVL(v_rule_for_final_payout, 'X') <> NVL(budget_detail_rec.rule_for_final_payout, 'X') OR
         v_promotion_string         <> budget_detail_rec.promotion_string OR
         v_is_active                <> budget_detail_rec.is_active        OR
         v_start_date               <> budget_detail_rec.start_date       OR
         NVL(v_end_date,sysdate)    <> NVL(budget_detail_rec.end_date,sysdate) OR
         NVL(v_reallocated_amount, 0) <> NVL(budget_detail_rec.reallocated_amount, 0) THEN

        v_stage := 'Update changes in report table';
        UPDATE rpt_budget_usage_detail
           SET budget_amt         = budget_detail_rec.budget_amt,
               budget_used        = budget_detail_rec.budget_used,
               budget_remaining   = budget_detail_rec.budget_remaining,
               budget_status      = budget_detail_rec.budget_status,
               promotion_string   = budget_detail_rec.promotion_string,
               is_active          = budget_detail_rec.is_active,
               start_date         = budget_detail_rec.start_date,
               end_date           = budget_detail_rec.end_date,
               inception_date     = budget_detail_rec.inception_date,
               modified_by        = p_user_id,
               date_modified      = SYSDATE,
               version            = 1,
               reallocated_amount = budget_detail_rec.reallocated_amount    --10/16/2012
         WHERE rpt_budget_usage_detail_id = v_rpt_id;     
      
      END IF;
         
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_ins_rec := 'Y';       
    END;  

    IF v_ins_rec = 'Y' THEN

      BEGIN

        budget_detail_rec.node_owner := 'n/a';

        IF(UPPER(budget_detail_rec.budget_type) != 'CENTRAL') THEN

          budget_detail_rec.rule_for_final_payout := NULL;

          IF UPPER(budget_detail_rec.budget_type) = 'NODE' THEN

            v_stage := 'Get node Owner';
            SELECT user_name
              INTO budget_detail_rec.node_owner
              FROM application_user au, 
                   user_node un
             WHERE rec_detail.node_id = un.node_id
               AND au.user_id = un.user_id
               AND un.role = 'own';
          END IF;
        END IF;
      EXCEPTION
        WHEN OTHERS THEN
          budget_detail_rec.node_owner := 'VACANT';
      END;
  
      -- get budget_owner info only once per budget_owner (user_id or pax_id)
      IF rec_detail.node_id IS NOT NULL THEN

        v_user_id := 0;
        budget_detail_rec.budget_owner_type := 'node';
      
        -- get name from node
        --IF NVL(v_node_id,0) <> rec_detail.node_id THEN --05/31/2019   Bug#79052 If same node having multiple time period this condition is skipping . so that node id getting null
          v_node_id :=  rec_detail.node_id;
          budget_detail_rec.budget_owner_id := v_node_id;
          budget_detail_rec.node_id := v_node_id;

          BEGIN
            v_stage := 'Get Owner name from node';
            SELECT NAME
              INTO budget_detail_rec.budget_owner_name
              FROM node
             WHERE node_id = budget_detail_rec.budget_owner_id;
          EXCEPTION
            WHEN OTHERS THEN
              budget_detail_rec.budget_owner_name := NULL;
          END;
        --END IF; -- node_id changed --05/31/2019   Bug#79052

      ELSIF rec_detail.user_id IS NOT NULL THEN
        v_node_id := 0;
        budget_detail_rec.budget_owner_type := 'user';
      
        --  get name from user_application
        --IF NVL(v_user_id,0) <> rec_detail.user_id THEN--05/31/2019   Bug#79052 
          v_user_id :=  rec_detail.user_id;
          budget_detail_rec.budget_owner_id := v_user_id;
          BEGIN
            v_stage := 'Get Owner name from application_user';
            SELECT initcap(last_name)||' '||initcap(first_name)
                   || decode(ltrim(rtrim(middle_name)),null,null,(' '||ltrim(rtrim(middle_name))))
                   || decode(ltrim(rtrim(suffix)),null,null,(' '||ltrim(rtrim(suffix))))  pax_name,
                   un.node_id
              INTO budget_detail_rec.budget_owner_name, 
                   budget_detail_rec.node_id
              FROM application_user a, 
                   user_node un
             WHERE a.user_id = un.user_id(+)
               AND a.user_id = budget_detail_rec.budget_owner_id
               AND ROWNUM = 1;
          EXCEPTION
            WHEN OTHERS THEN
              budget_detail_rec.budget_owner_name := NULL;
          END;
        --END IF; -- user_id changed --05/31/2019   Bug#79052

      ELSE
        v_node_id := NULL;
        budget_detail_rec.node_id := NULL;
        budget_detail_rec.budget_owner_type := 'promotion';
      END IF; -- rec_detail.node_id is not null

      IF(budget_detail_rec.budget_owner_type = 'promotion') THEN

        /*FOR rec_budget_promo_owner IN cur_budget_promo_owner(budget_detail_rec.budget_master_id) LOOP --02/13/2017
          
          budget_detail_rec.budget_owner_id := rec_budget_promo_owner.promotion_id;
          budget_detail_rec.budget_owner_name := rec_budget_promo_owner.promotion_name;*/
          budget_detail_rec.budget_owner_id := NULL;
          budget_detail_rec.budget_owner_name := budget_detail_rec.promotion_string;
          budget_detail_rec.rpt_budget_usage_detail_id := rpt_budget_usage_detail_pk_sq.NEXTVAL;
          
          v_stage := 'Insert records into report table';   
          P_INSERT_RPT_BUDGET_USAGE_DTL(budget_detail_rec);
          
          
        --END LOOP; --02/13/2017
      
      ELSE
        budget_detail_rec.rpt_budget_usage_detail_id := rpt_budget_usage_detail_pk_sq.NEXTVAL;
        v_stage := 'Insert records into report table2'; 
        P_INSERT_RPT_BUDGET_USAGE_DTL(budget_detail_rec);
        v_rec_cnt := v_rec_cnt + 1;
      END IF;
    
    END IF;
      
  END LOOP;

  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', 'Insert report table (' || v_rec_cnt || ' records processed)', NULL);

  v_stage := 'Delete removed budget if any from report table';
  DELETE FROM rpt_budget_usage_detail rpt
   WHERE NOT EXISTS (SELECT 'X'
                       FROM budget
                      WHERE budget_id = rpt.budget_id); 

  v_rec_cnt := SQL%ROWCOUNT;
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

  v_stage := 'Update budget status for inactive pax';   --02/05/2015
  UPDATE rpt_budget_usage_detail d      --02/05/2015
       SET d.budget_status = 'inactive', d.is_active = 0
    WHERE d.budget_status = 'active'
           AND d.budget_type = 'pax'   --Checks if the budget type is PAX
           AND EXISTS --Checks if the PAX became inactive
                  (SELECT au.user_id
                     FROM application_user au
                    WHERE au.user_id = d.budget_owner_id AND au.is_active = 0);
     
     --Added on 04/08/2019                 
      v_stage := 'Update budget owner name'; 
            FOR i in (WITH pax_dtl
                 AS (SELECT au.user_id,
                               INITCAP (au.last_name)
                            || ' '
                            || INITCAP (au.first_name)
                            || DECODE (LTRIM (RTRIM (au.middle_name)),
                                       NULL, NULL,
                                       (' ' || LTRIM (RTRIM (au.middle_name))))
                            || DECODE (LTRIM (RTRIM (au.suffix)),
                                       NULL, NULL,
                                       (' ' || LTRIM (RTRIM (au.suffix))))
                               as budget_owner_name
                       FROM application_user au)
            SELECT DISTINCT r.budget_owner_id, p.budget_owner_name
              FROM rpt_budget_usage_detail r, pax_dtl p
             WHERE r.budget_owner_id = p.user_id
             AND r.budget_owner_name<> p.budget_owner_name) Loop
             
             UPDATE rpt_budget_usage_detail d 
                   SET d.budget_owner_name = i.budget_owner_name
                WHERE d.budget_owner_id = i.budget_owner_id;         
            END LOOP;               
                    

  v_stage := 'Procedure completed';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    p_return_code   := 99;
    p_error_message := v_stage || ': '|| SUBSTR(SQLERRM,1,250);
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ': ' ||v_parm_list|| ':' || SQLCODE ||', ' || SQLERRM, NULL);
END; -- p_rpt_budget_usage_detail

/*PROCEDURE P_RPT_BUDGET_USAGE_SUMMARY
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
  IS */
/*******************************************************************************
   Purpose:  Populate the rpt_budget_usage_summary reporting tables

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray      11/14/2005 Initial Creation
   D Murray      12/06/2005 Added nvl to all queries for budget_master, budget_id,
                            promotion_id - can not be null for Jasper report parameters.
   D Murray      12/19/2005 Added budget_status
   Raju N      01/05/2005  moved the PK into the trigger to avoid the duplicate values.
   TBD: Break up the select into's to a loop.
   Rachel R      01/13/2006 Comment out trend and piechart in the refresh date section.
                            Currently the pie and trend reports do not exist.
   Percy M       09/21/2006 fix for bug# 13535
  Arun S         07/11/2012 G5 report changes
                            Replaced hierarchy looping process with the following bulk processng SQL statements.
                             1) Remove obsolete node records.
                             2) Derive summary records based on the detail records.
                             3) Insert default permutations to ensure report data can be searched from root node to all leaf nodes.
                             4) Remove default permutation summaries that now have detail derived summaries.
  Ravi Dhanekula 10/16/2012 Bug # 43328, Added processing for new column reallocated_amount on usage_detail and sumary tables.
*******************************************************************************/
/*
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'P_RPT_BUDGET_USAGE_SUMMARY';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
  c_created_by         CONSTANT rpt_awardmedia_summary.created_by%TYPE := 0;

  v_stage              execution_log.text_line%TYPE;
  v_rec_cnt            INTEGER;
  v_summary_id         rpt_budget_usage_summary.rpt_budget_usage_summary_id%TYPE;
  
  CURSOR cur_summary IS
    SELECT h.parent_node_id                header_node_id,
           NVL(d.node_id,h.node_id)        detail_node_id,
           NVL(SUM(d.budget_amt),0)        budget_amt,
           NVL(SUM(d.budget_used),0)       budget_used,
           NVL(SUM(d.budget_remaining),0)  budget_remaining,
           NVL(sum(d.reallocated_amount),0) reallocated_amount, --10/16/2012
           NVL(d.budget_master_id,0)       budget_master_id,
           NVL(d.budget_id,0)              budget_id,
           NVL(d.budget_status,' ')        budget_status,
           h.hier_level,
           1                               is_leaf,
           d.multi_promotion
      FROM rpt_budget_usage_detail d,
           budget b,
           rpt_hierarchy h
     WHERE d.budget_id     = b.budget_id 
       AND h.node_id       = d.node_id (+)
       AND (p_start_date   < b.date_created OR p_start_date    < b.date_modified)
       AND (b.date_created <= p_end_date    OR b.date_modified <= p_end_date)
     GROUP BY h.parent_node_id, 
              NVL(d.node_id,h.node_id), 
              d.budget_master_id,
              d.budget_id, 
              d.budget_status,
              h.hier_level,
              d.multi_promotion;  

  CURSOR cur_hier (p_in_node_id rpt_hierarchy.node_id%TYPE) IS
    SELECT parent_node_id header_node_id,
           node_id,
           hier_level,
           is_leaf
      FROM rpt_hierarchy
     START WITH node_id = p_in_node_id
   CONNECT BY PRIOR parent_node_id = node_id;       

BEGIN

  v_stage := 'Procedure started';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  v_stage := 'Remove budget usage detail records those are not present in budget table';
  DELETE FROM rpt_budget_usage_summary rpt
   WHERE budget_id <> 0
     AND NOT EXISTS (SELECT 'X'
                       FROM budget
                      WHERE budget_id = rpt.budget_id);
                      
  -- remove obsolete node summaries (node not found in rpt_hierarchy)
  v_stage := 'DELETE obsolete node summary records';
  DELETE
    FROM rpt_budget_usage_summary
   WHERE detail_node_id NOT IN
         ( -- get node ID currently in the report hierarchy
           SELECT node_id
             FROM rpt_hierarchy
         );

  v_rec_cnt := SQL%ROWCOUNT;
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

  FOR rec_summary IN cur_summary LOOP

    BEGIN
      v_stage := 'Check teamsum record for budget id: '||rec_summary.budget_id;
      SELECT rpt_budget_usage_summary_id
        INTO v_summary_id
        FROM rpt_budget_usage_summary
       WHERE record_type            = rec_summary.hier_level||'-teamsum'
         AND NVL(header_node_id, 0) = NVL(rec_summary.header_node_id, 0)
         AND detail_node_id         = rec_summary.detail_node_id
         AND budget_master_id       = rec_summary.budget_master_id
         AND budget_id              = rec_summary.budget_id
         AND hier_level             = rec_summary.hier_level
         AND is_leaf                = rec_summary.is_leaf;

      v_stage := 'Update teamsum record for budget id: '||rec_summary.budget_id;
      UPDATE rpt_budget_usage_summary
         SET budget_amt             = rec_summary.budget_amt,
             budget_used            = rec_summary.budget_used,
             budget_remaining       = rec_summary.budget_remaining,
             reallocated_amount     = rec_summary.reallocated_amount,   --10/16/2012
             budget_status          = rec_summary.budget_status,
             modified_by            = p_user_id,
             date_modified          = SYSDATE,
             version                = version + 1
       WHERE rpt_budget_usage_summary_id = v_summary_id;
    
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_stage := 'Insert teamsum record for budget id: '||rec_summary.budget_id;
        INSERT INTO rpt_budget_usage_summary
                    (rpt_budget_usage_summary_id, 
                     record_type, 
                     header_node_id, 
                     detail_node_id, 
                     budget_amt,
                     budget_used, 
                     budget_remaining, 
                     budget_master_id,
                     budget_id, 
                     budget_status, 
                     promotion_id, 
                     hier_level, 
                     is_leaf,
                     date_created, 
                     created_by, 
                     version,
                     multi_promotion,
                     reallocated_amount     --10/16/2012
                     )
             VALUES (rpt_budget_usage_summary_pk_sq.NEXTVAL,
                     rec_summary.hier_level||'-teamsum',
                     rec_summary.header_node_id, 
                     rec_summary.detail_node_id, 
                     rec_summary.budget_amt,
                     rec_summary.budget_used, 
                     rec_summary.budget_remaining, 
                     rec_summary.budget_master_id,
                     rec_summary.budget_id, 
                     rec_summary.budget_status, 
                     0,                          --promotion_id, 
                     rec_summary.hier_level, 
                     rec_summary.is_leaf,
                     SYSDATE,
                     p_user_id,
                     1,
                     rec_summary.multi_promotion,
                     rec_summary.reallocated_amount     --10/16/2012
                     );                      
    END;

    FOR rec_hier IN cur_hier (rec_summary.detail_node_id) LOOP

      BEGIN
        v_stage := 'Check nodesum record for budget id: '||rec_summary.budget_id;
        SELECT rpt_budget_usage_summary_id
          INTO v_summary_id
          FROM rpt_budget_usage_summary
         WHERE record_type            = rec_hier.hier_level||'-nodesum'
           AND NVL(header_node_id, 0) = NVL(rec_hier.header_node_id, 0)
           AND detail_node_id         = rec_hier.node_id
           AND budget_master_id       = rec_summary.budget_master_id
           AND budget_id              = rec_summary.budget_id
           AND hier_level             = rec_hier.hier_level
           AND is_leaf                = rec_hier.is_leaf;

        v_stage := 'Update nodesum record for budget id: '||rec_summary.budget_id;
        UPDATE rpt_budget_usage_summary
           SET budget_amt             = rec_summary.budget_amt,
               budget_used            = rec_summary.budget_used,
               budget_remaining       = rec_summary.budget_remaining,
               reallocated_amount     = rec_summary.reallocated_amount,     --10/16/2012
               budget_status          = rec_summary.budget_status,
               modified_by            = p_user_id,
               date_modified          = SYSDATE,
               version                = version + 1
         WHERE rpt_budget_usage_summary_id = v_summary_id;
      
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_stage := 'Insert nodesum record for budget id: '||rec_summary.budget_id;
          INSERT INTO rpt_budget_usage_summary
                      (rpt_budget_usage_summary_id, 
                       record_type, 
                       header_node_id, 
                       detail_node_id, 
                       budget_amt,
                       budget_used, 
                       budget_remaining, 
                       budget_master_id,
                       budget_id, 
                       budget_status, 
                       promotion_id, 
                       hier_level, 
                       is_leaf,
                       date_created, 
                       created_by, 
                       version,
                       reallocated_amount     --10/16/2012
                       )
               VALUES (rpt_budget_usage_summary_pk_sq.NEXTVAL,
                       rec_hier.hier_level||'-nodesum',
                       rec_hier.header_node_id, 
                       rec_hier.node_id, 
                       rec_summary.budget_amt,
                       rec_summary.budget_used, 
                       rec_summary.budget_remaining, 
                       rec_summary.budget_master_id,
                       rec_summary.budget_id, 
                       rec_summary.budget_status, 
                       0,                           --promotion_id, 
                       rec_hier.hier_level, 
                       rec_hier.is_leaf,
                       SYSDATE,
                       p_user_id,
                       1,
                       rec_summary.reallocated_amount     --10/16/2012
                       );                      
      
      END;
    
    END LOOP;
        
  END LOOP;

   v_stage := 'INSERT missing default team summary permutations';
  INSERT INTO rpt_budget_usage_summary 
              (--rpt_budget_usage_summary_id, 
               record_type, 
               header_node_id, 
               detail_node_id, 
               budget_amt, 
               budget_used, 
               budget_remaining, 
               budget_master_id, 
               budget_id, 
               budget_status, 
               promotion_id, 
               hier_level, 
               is_leaf, 
               date_created, 
               created_by, 
               version,
               reallocated_amount     --10/16/2012
               ) 
              (SELECT --rpt_budget_usage_summary_pk_sq.NEXTVAL,
                      nsp.record_type, 
                      nsp.header_node_id, 
                      nsp.detail_node_id, 
                      0 budget_amt, 
                      0 budget_used, 
                      0 budget_remaining, 
                      0 budget_master_id, 
                      0 budget_id, 
                      ' ' budget_status, 
                      NULL promotion_id, 
                      nsp.hier_level, 
                      nsp.is_leaf, 
                      SYSDATE, 
                      p_user_id,
                      1,
                      0 reallocated_amount     --10/16/2012
                 FROM ( -- get all possible node summary type permutations
                       SELECT h.node_id AS detail_node_id,
                              h.hier_level || '-' || 'teamsum' AS record_type,
                              h.parent_node_id AS header_node_id,
                              h.hier_level,
                              1 AS is_leaf   -- team summary always a leaf node
                         FROM rpt_hierarchy h
                       ) nsp                          
                     -- exclude default permutation when a matching summary record exists
                WHERE NOT EXISTS
                     ( SELECT 1
                         FROM rpt_budget_usage_summary s
                        WHERE s.detail_node_id = nsp.detail_node_id
                          AND s.record_type    = nsp.record_type
                      )
                      );

   v_stage := 'INSERT missing default node summary permutations';
  INSERT INTO rpt_budget_usage_summary 
              (--rpt_budget_usage_summary_id, 
               record_type, 
               header_node_id, 
               detail_node_id, 
               budget_amt, 
               budget_used, 
               budget_remaining, 
               budget_master_id, 
               budget_id, 
               budget_status, 
               promotion_id, 
               hier_level, 
               is_leaf, 
               date_created, 
               created_by, 
               version,
               reallocated_amount     --10/16/2012
               ) 
              (SELECT --rpt_budget_usage_summary_pk_sq.NEXTVAL,
                      nsp.record_type, 
                      nsp.header_node_id, 
                      nsp.detail_node_id, 
                      0 budget_amt, 
                      0 budget_used, 
                      0 budget_remaining, 
                      0 budget_master_id, 
                      0 budget_id, 
                      ' ' budget_status, 
                      NULL promotion_id, 
                      nsp.hier_level, 
                      nsp.is_leaf, 
                      SYSDATE, 
                      p_user_id,
                      1,
                      0 reallocated_amount     --10/16/2012
                 FROM ( -- get all possible node summary type permutations
                       SELECT h.node_id AS detail_node_id,
                              h.hier_level || '-' || 'nodesum' AS record_type,
                              h.parent_node_id AS header_node_id,
                              h.hier_level,
                              1 AS is_leaf   -- team summary always a leaf node
                         FROM rpt_hierarchy h
                       ) nsp                          
                     -- exclude default permutation when a matching summary record exists
                WHERE NOT EXISTS
                     ( SELECT 1
                         FROM rpt_budget_usage_summary s
                        WHERE s.detail_node_id = nsp.detail_node_id
                          AND s.record_type    = nsp.record_type
                      )
          -- default node summary permutation must have default team summary permutation in its hierarchy
         AND EXISTS (SELECT 1
                       FROM rpt_budget_usage_summary tp
                      WHERE tp.detail_node_id          = nsp.detail_node_id
                        AND SUBSTR(tp.record_type, -7) = 'teamsum'
                      UNION ALL
                     -- get team permutations under node permutation hierarchy
                     SELECT 1
                       FROM rpt_budget_usage_summary tp
                         -- start with child node immediately under current node
                      START WITH tp.header_node_id          = nsp.detail_node_id
                             AND SUBSTR(tp.record_type, -7) = 'teamsum'
                      CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                             AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
                     )
                    );

   -- remove team permutations when an associated detail derived summary exists
   v_stage := 'DELETE obsolete team permutations';
   DELETE rpt_budget_usage_summary
    WHERE ROWID IN
          ( -- get default team permutation with a corresponding detail derived team summary
            SELECT DISTINCT
                   s.ROWID
              FROM rpt_budget_usage_summary s,
                   rpt_budget_usage_summary dd
                -- substr matches functional index
             WHERE SUBSTR(dd.record_type, -7) = 'teamsum'
                -- detail derived summaries promotion_id won't be null
                AND s.promotion_id IS NOT NULL
               AND dd.detail_node_id          = s.detail_node_id
               AND SUBSTR(dd.record_type, -7) = SUBSTR(s.record_type, -7)
                -- default permutations will be null
               AND s.promotion_id IS NULL 
          );

   -- delete node permutation with no associated team permutation
   v_stage := 'DELETE obsolete node permutations with no associated team permutation';
   DELETE rpt_budget_usage_summary
    WHERE ROWID IN
          ( SELECT np.ROWID
              FROM rpt_budget_usage_summary np
             WHERE SUBSTR(np.record_type, -7) = 'nodesum'
               AND np.promotion_id IS NULL
                -- node permutation has no team permutation
               AND NOT EXISTS
                   ( -- get team permutations under node permutation
                     SELECT 1
                       FROM rpt_budget_usage_summary tp
                      WHERE tp.detail_node_id          = np.detail_node_id
                        AND SUBSTR(tp.record_type, -7) = 'teamsum'
                        AND tp.promotion_id IS NULL
                      UNION ALL
                     -- get team permutations under node permutation hierarchy
                     SELECT 1
                       FROM rpt_budget_usage_summary tp
                         -- start with child node immediately under current node
                      START WITH tp.header_node_id          = np.detail_node_id
                             AND SUBSTR(tp.record_type, -7) = 'teamsum'
                             AND tp.promotion_id IS NULL
                      CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                             AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
                             AND tp.promotion_id IS NULL
                   )
          );
                              
  v_stage := 'Procedure completed';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_return_code := 0;
               
EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    p_error_message := v_stage || ': '|| SUBSTR(SQLERRM,1,250);
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ': ' || SQLCODE || ', ' || SQLERRM, NULL);
END; -- p_rpt_budget_usage_summary
*/

PROCEDURE P_INSERT_RPT_BUDGET_TRANS_DTL
  (p_budget_trans_dtl_rec IN rpt_budget_trans_detail%rowtype) IS
/*******************************************************************************

   Purpose:  Insert record into rpt_budget_trans_detail reporting table

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/14/2005  Initial Creation
   D Murray    12/19/2005  Added budget_status
   Arun S      07/11/2012  G5 report process changes
                           Added recipient_country_asset_code, budget_history_id, merch_order_id
                           modified_by, date_modified, version
   Ravi Dhanekula 04/15/2014  Added budgetsegmentId.
  nagarajs        01/24/2017 G5.6.3.3 changes
*******************************************************************************/

BEGIN

 INSERT INTO rpt_budget_trans_detail
      (rpt_budget_trans_detail_id, budget_id, budget_master_id,budget_segment_id,budget_status,
       budget_trans_type, journal_id, trans_date,
       trans_amount, recipient_user_id, recipient_name,recipient_first_name,recipient_last_name, --01/24/2017
       promotion_id, promotion_name, date_created, created_by,
       recipient_country_id, budget_history_id, merch_order_id,
       modified_by, date_modified, version, budget_reallocation_history_id)
 VALUES
      (p_budget_trans_dtl_rec.rpt_budget_trans_detail_id, p_budget_trans_dtl_rec.budget_id,
       p_budget_trans_dtl_rec.budget_master_id,p_budget_trans_dtl_rec.budget_segment_id, nvl(p_budget_trans_dtl_rec.budget_status,' '),
       p_budget_trans_dtl_rec.budget_trans_type,
       p_budget_trans_dtl_rec.journal_id, p_budget_trans_dtl_rec.trans_date,
       p_budget_trans_dtl_rec.trans_amount, p_budget_trans_dtl_rec.recipient_user_id,
       p_budget_trans_dtl_rec.recipient_name,p_budget_trans_dtl_rec.recipient_first_name,p_budget_trans_dtl_rec.recipient_last_name, --01/24/2017
       p_budget_trans_dtl_rec.promotion_id,
       p_budget_trans_dtl_rec.promotion_name, p_budget_trans_dtl_rec.date_created,
       p_budget_trans_dtl_rec.created_by, p_budget_trans_dtl_rec.recipient_country_id,
       p_budget_trans_dtl_rec.budget_history_id, p_budget_trans_dtl_rec.merch_order_id,
       p_budget_trans_dtl_rec.modified_by, p_budget_trans_dtl_rec.date_modified, p_budget_trans_dtl_rec.version,
       p_budget_trans_dtl_rec.budget_reallocation_history_id
       );
EXCEPTION
   WHEN others THEN
    prc_execution_log_entry('P_INSERT_RPT_BUDGET_TRANS_DTL',1,'ERROR',SQLERRM,null);
END; -- P_INSERT_RPT_BUDGET_TRANS_DTL
--------------------------------------------------------------------------------

PROCEDURE P_INSERT_RPT_BUDGET_USAGE_DTL
  (p_budget_usage_dtl_rec IN rpt_budget_usage_detail%rowtype) IS
/*******************************************************************************

   Purpose:  Insert record into rpt_budget_usage_detail reporting table

** NOTE ** Still missing cost center

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/15/2005  Initial Creation
   D Murray    12/19/2005 Added budget_status
   Matt L      11/09/2007 Added additional columns for budget extract
   Arun S      07/11/2012 G5 report process changes
                          Added budget_owner_cntry_asset_code, date_created, created_by,
                          modified_by, date_modified, version
   Ravi Dhanekula 04/15/2014  Added budgetsegmentId.
*******************************************************************************/

BEGIN

 INSERT INTO rpt_budget_usage_detail
      (rpt_budget_usage_detail_id, node_id, budget_name_cm_asset_code,
       budget_name_cm_key, budget_owner_id, budget_owner_name,
       budget_owner_type, budget_amt, budget_used, budget_remaining,
       budget_master_id,budget_segment_id, budget_id, budget_status, inception_date, overrideable,
       cap_amount, cost_center, budget_master_name, rule_for_final_payout, node_owner, promotion_string, budget_type, is_active, start_date, end_date,
       budget_owner_cntry_id, date_created, created_by, 
       modified_by, date_modified, version,multi_promotion, reallocated_amount
       )
 VALUES
      (p_budget_usage_dtl_rec.rpt_budget_usage_detail_id, p_budget_usage_dtl_rec.node_id,
       p_budget_usage_dtl_rec.budget_name_cm_asset_code, p_budget_usage_dtl_rec.budget_name_cm_key,
       p_budget_usage_dtl_rec.budget_owner_id, p_budget_usage_dtl_rec.budget_owner_name,
       p_budget_usage_dtl_rec.budget_owner_type, p_budget_usage_dtl_rec.budget_amt,
       p_budget_usage_dtl_rec.budget_used, p_budget_usage_dtl_rec.budget_remaining,
       p_budget_usage_dtl_rec.budget_master_id, p_budget_usage_dtl_rec.budget_segment_id, p_budget_usage_dtl_rec.budget_id,
       nvl(p_budget_usage_dtl_rec.budget_status,' '),
       p_budget_usage_dtl_rec.inception_date, p_budget_usage_dtl_rec.overrideable,
       p_budget_usage_dtl_rec.cap_amount, p_budget_usage_dtl_rec.cost_center,
       p_budget_usage_dtl_rec.budget_master_name, p_budget_usage_dtl_rec.rule_for_final_payout, p_budget_usage_dtl_rec.node_owner, p_budget_usage_dtl_rec.promotion_string, p_budget_usage_dtl_rec.budget_type, p_budget_usage_dtl_rec.is_active, p_budget_usage_dtl_rec.start_date, p_budget_usage_dtl_rec.end_date,
       p_budget_usage_dtl_rec.budget_owner_cntry_id, p_budget_usage_dtl_rec.date_created, p_budget_usage_dtl_rec.created_by,
       p_budget_usage_dtl_rec.modified_by, p_budget_usage_dtl_rec.date_modified, p_budget_usage_dtl_rec.version,p_budget_usage_dtl_rec.multi_promotion
       ,p_budget_usage_dtl_rec.reallocated_amount);
EXCEPTION
   WHEN others THEN
    prc_execution_log_entry('P_INSERT_RPT_BUDGET_USAGE_DTL',1,'ERROR',SQLERRM,null);
END; -- P_INSERT_RPT_BUDGET_USAGE_DTL
--------------------------------------------------------------------------------

END;
/
