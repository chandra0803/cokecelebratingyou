CREATE OR REPLACE PACKAGE pkg_report_cash_budget
  IS
  PROCEDURE P_RPT_CASH_BUDGET_PROMOTION
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

  PROCEDURE P_RPT_CASH_BUDGET_TRANS_DETAIL
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

  PROCEDURE P_RPT_CASH_BUDGET_USAGE_DETAIL
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2);

   
  PROCEDURE P_INS_RPT_CASH_BUDGET_TRAN_DTL
   (p_budget_trans_dtl_rec IN rpt_cash_budget_trans_detail%ROWTYPE);

  PROCEDURE P_INS_RPT_CASH_BUD_USAGE_DTL
   (p_budget_usage_dtl_rec IN rpt_cash_budget_usage_detail%ROWTYPE);

FUNCTION fnc_get_user_name
  ( pi_user_id    IN application_user.user_id%TYPE)
  RETURN VARCHAR2;
  

END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_report_cash_budget IS
-- global variables
g_merch_type      CONSTANT VARCHAR2(100) := 'merchandise';
g_tran_type_exp   CONSTANT VARCHAR2(100) := 'expenditure';
g_tran_type_dep   CONSTANT VARCHAR2(100) := 'deposit';

FUNCTION fnc_get_user_name
  ( pi_user_id    IN application_user.user_id%TYPE)
  RETURN VARCHAR2 IS
  
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

PROCEDURE P_RPT_CASH_BUDGET_PROMOTION
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
IS

/*******************************************************************************
   Purpose:  Populate the rpt_cash_budget_promotion reporting table

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Suresh J     11/16/2005 Initial Creation
   Loganathan   03/11/2019 Bug 78631 - PKG_REPORTs without logs or return code
*******************************************************************************/

  c_process_name       CONSTANT execution_log.process_name%TYPE := 'P_RPT_CASH_BUDGET_PROMOTION';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
  c_created_by         CONSTANT rpt_awardmedia_summary.created_by%TYPE := 0;

  v_stage              execution_log.text_line%TYPE;
  v_rec_cnt            INTEGER;
  v_parm_list     execution_log.text_line%TYPE;   --03/11/2019  Bug 78631
BEGIN
v_stage := 'initialize variables'; 
v_parm_list := 
'p_user_id 			     :='||p_user_id 		  ||CHR(10)||
'p_start_date        :='||p_start_date    ||CHR(10)|| 
'p_end_date          :='||p_end_date          ; 
   v_stage := 'Procedure started';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

DELETE  rpt_cash_budget_promotion;

    v_stage := 'Insert promo budget records into gtt_rpt_cash_budget_promotion';
    INSERT INTO rpt_cash_budget_promotion  (budget_id,budget_master_id,budget_segment_id,promotion_id,distribution_type )          
         SELECT b.budget_id, 
                bb.budget_master_id,
                bb.budget_segment_id,
                p.promotion_id,
                CASE WHEN BM.MULTI_PROMOTION=0 THEN 'one_to_one'
                ELSE 'shared'
                END  distribution_type
           FROM promotion p, 
                budget b,
                budget_segment bb, 
                budget_master bm
          WHERE
             p.cash_budget_master_id = bb.budget_master_id
            AND bm.award_type = 'cash'  
            AND p.cash_budget_master_id IS NOT NULL 
            AND bb.budget_segment_id = b.budget_segment_id
            AND bb.budget_master_id = bm.budget_master_id
          UNION                              
          SELECT b.budget_id, 
                bb.budget_master_id,
                bb.budget_segment_id,
                p.promotion_id,
                CASE WHEN BM.MULTI_PROMOTION=0 THEN 'one_to_one'
                ELSE 'shared'
                END  distribution_type
           FROM promotion p,
                promo_nomination pr, 
                budget b,
                budget_segment bb, 
                budget_master bm
          WHERE
                p.cash_budget_master_id = bb.budget_master_id
            AND bm.award_type = 'cash'  
            AND pr.public_rec_budget_master_id = bb.budget_master_id
            AND pr.public_rec_budget_master_id IS NOT NULL
            AND p.promotion_id = pr.promotion_id
            AND bb.budget_segment_id = b.budget_segment_id
            AND bb.budget_master_id = bm.budget_master_id;

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);


   v_stage := 'Procedure completed';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

   p_return_code := 0;
   
EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    p_error_message := v_stage || ': '|| SUBSTR(SQLERRM,1,250);
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage|| ': ' || v_parm_list|| ': ' || SQLCODE || ', ' || SQLERRM, NULL);
END; -- p_rpt_cash_budget_promotion

PROCEDURE P_RPT_CASH_BUDGET_TRANS_DETAIL
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
  IS
/*******************************************************************************
   Purpose:  Populate the rpt_cash_budget_TRANS_DETAIL reporting table
   
   NOTE: promo_id not required for non-merch budgets but IS REQUIRED for merch budgets

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Suresh J      04/22/2016 Initial Creation
   nagarajs      01/24/2017 G5.6.3.3 changes
   nagarajs     02/10/2017 Expendtiure amount not showing in reports
*******************************************************************************/

  c_process_name       CONSTANT execution_log.process_name%TYPE := 'P_RPT_CASH_BUDGET_TRANS_DETAIL';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
  c_created_by         CONSTANT rpt_awardmedia_summary.created_by%TYPE := 0;
  c_rpt_date_created   CONSTANT DATE := SYSDATE;
  
  v_stage               VARCHAR2(500);
  v_ins_rec             VARCHAR2(1);
  budget_trans_rec      rpt_cash_budget_trans_detail%ROWTYPE;
  v_budget_status       rpt_cash_budget_trans_detail.budget_status%TYPE;
  v_rpt_bud_detl_id     rpt_cash_budget_trans_detail.rpt_cash_budget_trans_dtl_id%TYPE;
  v_rec_cnt             INTEGER := 0;
  v_cnt_realloc         NUMBER;     
  v_parm_list     execution_log.text_line%TYPE;   --03/11/2019  Bug 78631
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
                       WHERE bb.budget_master_id = p.cash_budget_master_id
                         AND bb.budget_segment_id = b.budget_segment_id
                         UNION                  
                        SELECT p.promotion_id, --include public recognition budget promotions
                             p.promotion_name,
                             b.budget_id,
                             bb.budget_master_id,
                             b.budget_segment_id,
                             b.status,
                             b.effective_date
                        FROM promo_nomination pr, 
                             promotion p,
                             budget b,
                             budget_segment bb
                       WHERE bb.budget_master_id = pr.public_rec_budget_master_id
                         AND pr.promotion_id = p.promotion_id
                         AND bb.budget_segment_id = b.budget_segment_id
                         ),
                    expen_lis AS
                     (SELECT pl.*,
                             j.journal_id,
                             j.user_id,
                             j.transaction_date,
                             j.transaction_type,
                             --NVL(j.budget_value, j.transaction_amt) transaction_amt, --02/10/2017
                             j.transaction_cash_amt AS transaction_amt, --02/10/2017
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
                         AND j.user_id             = au.user_id   --01/24/2017     
                       UNION 
                      SELECT pl1.*,
                             jl.journal_id, 
                             jl.user_id, 
                             jl.transaction_date,
                             jl.transaction_type, 
                             --NVL(jl.budget_value, jl.transaction_amt) transaction_amt,  --02/10/2017
                             jl.transaction_cash_amt AS transaction_amt, --02/10/2017
                             jl.status_type,
                             fnc_get_user_name(jl.user_id) pax_name,
                             au.first_name, --01/24/2017
                             au.last_name --01/24/2017
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
                         AND jl.user_id             = au.user_id   --01/24/2017     
                       UNION
                      SELECT pl2.*,
                             jl.journal_id, 
                             jl.user_id, 
                             jl.transaction_date,
                             jl.transaction_type, 
                             --NVL(jl.budget_value, jl.transaction_amt) transaction_amt,  --02/10/2017
                             jl.transaction_cash_amt AS transaction_amt, --02/10/2017
                             jl.status_type,
                             fnc_get_user_name(jl.user_id) pax_name,
                             au.first_name, --01/24/2017
                             au.last_name --01/24/2017
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
              budget_segment bb, 
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
          AND p.cash_budget_master_id = bb.budget_master_id
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
         UNION                           
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
  DELETE FROM rpt_cash_budget_trans_detail rpt
   WHERE NOT EXISTS (SELECT 'X'
                       FROM budget
                      WHERE budget_id = rpt.budget_id);
                      
v_stage := 'Insert Deposit/transfer records rpt_cash_budget_trans_detail';      
              
MERGE INTO rpt_cash_budget_trans_detail rpt
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
                      ,budget_master bm
                WHERE 
                  b.budget_id     = bh.budget_id
                  AND b.budget_segment_id = bb.budget_segment_id
                  AND bm.award_type = 'cash'
                  AND bm.budget_master_id = bb.budget_master_id                    
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
                        (rpt_cash_budget_trans_dtl_id,
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
           ( rpt_cash_budget_TRAN_DTL_PK_SQ.NEXTVAL,
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

    
  v_stage := 'Insert rpt_cash_budget_trans_detail for expenditure budget trans type and non-merch-budget promos only';
  FOR rec_exp_non_merch IN cur_exp_non_merch LOOP
    
    budget_trans_rec := NULL;
    v_ins_rec         := 'N';
    v_rpt_bud_detl_id := NULL;

    BEGIN    
      v_stage := 'Check expenditure budget for non-merch-budget rec using journal_id';
      SELECT rpt_cash_budget_trans_dtl_id,
             budget_status
        INTO v_rpt_bud_detl_id,
             v_budget_status 
        FROM rpt_cash_budget_trans_detail
       WHERE budget_id         = rec_exp_non_merch.budget_id
         AND budget_master_id  = rec_exp_non_merch.budget_master_id
         AND journal_id        = rec_exp_non_merch.journal_id
         AND trans_date        = rec_exp_non_merch.trans_date
         AND budget_trans_type = g_tran_type_exp;
  
      IF v_budget_status <> rec_exp_non_merch.budget_status THEN

        v_stage := 'Update status change for expenditure budget for non-merch-budget';
        UPDATE rpt_cash_budget_trans_detail
           SET budget_status    = rec_exp_non_merch.budget_status,
               modified_by      = p_user_id,
               date_modified    = SYSDATE,
               version          = version + 1  
         WHERE rpt_cash_budget_trans_dtl_id = v_rpt_bud_detl_id;

      END IF;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_ins_rec := 'Y';
    END;

    IF v_ins_rec = 'Y' THEN
      
      budget_trans_rec.rpt_cash_budget_trans_dtl_id := rpt_cash_budget_TRAN_DTL_PK_SQ.NEXTVAL;  
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
       
      P_INS_RPT_CASH_BUDGET_TRAN_DTL(budget_trans_rec);
      v_rec_cnt := v_rec_cnt + 1;        
    END IF;
    
    
  END LOOP;

  v_stage := 'Insert rpt_cash_budget_trans_detail for expenditure budget_trans_type and merch-budget promos and pax';
  FOR rec_exp_merch_pax IN cur_exp_merch_pax LOOP
    
    budget_trans_rec := NULL;
    v_ins_rec         := 'N';
    v_rpt_bud_detl_id := NULL;

    BEGIN    
      v_stage := 'Check expenditure budget for merch-budget and pax records';
      SELECT rpt_cash_budget_trans_dtl_id,
             budget_status
        INTO v_rpt_bud_detl_id,
             v_budget_status 
        FROM rpt_cash_budget_trans_detail
       WHERE budget_id        = rec_exp_merch_pax.budget_id
         AND budget_master_id = rec_exp_merch_pax.budget_master_id
         AND journal_id       = rec_exp_merch_pax.journal_id
         AND merch_order_id   = rec_exp_merch_pax.merch_order_id;

      IF v_budget_status <> rec_exp_merch_pax.budget_status THEN
        v_stage := 'Update status change for expenditure budget for merch-budget and pax records';
        UPDATE rpt_cash_budget_trans_detail
           SET budget_status    = rec_exp_merch_pax.budget_status,
               modified_by      = p_user_id,
               date_modified    = SYSDATE,
               version          = version + 1 
         WHERE rpt_cash_budget_trans_dtl_id = v_rpt_bud_detl_id;

      END IF;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_ins_rec := 'Y';
    END;

    IF v_ins_rec = 'Y' THEN
      
      budget_trans_rec.rpt_cash_budget_trans_dtl_id := rpt_cash_budget_tran_dtl_pk_sq.NEXTVAL;  
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
            
      p_ins_rpt_cash_budget_tran_dtl(budget_trans_rec);
      v_rec_cnt := v_rec_cnt + 1;        
    END IF;
    
  END LOOP;


  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', 'Insert report table (' || v_rec_cnt || ' records processed)', NULL);
   
  v_stage := 'Delete removed budget if any';
  DELETE FROM rpt_cash_budget_trans_detail rpt
  WHERE NOT EXISTS (SELECT 'X'
                       FROM budget
                      WHERE budget_id = rpt.budget_id); 

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   v_stage := 'Procedure completed';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

   p_return_code := 0;
           
EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    p_error_message := v_stage || ': '|| SUBSTR(SQLERRM,1,250);
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage|| ': ' ||v_parm_list|| ': ' || SQLCODE || ', ' || SQLERRM, NULL);                                                
END; -- p_rpt_cash_budget_trans_detail

PROCEDURE P_RPT_CASH_BUDGET_USAGE_DETAIL
  (p_user_id        IN  NUMBER,
   p_start_date     IN  DATE, 
   p_end_date       IN  DATE, 
   p_return_code    OUT NUMBER,
   p_error_message  OUT VARCHAR2)
  IS 
/***************************************************************************************
   Purpose:  Populate the rpt_cash_budget_USAGE_DETAIL reporting tables
   

   Person        Date           Comments
   -----------   ----------     -----------------------------------------------------
   Suresh J      01/22/2016     Initial Creation
   nagarajs      02/13/2017     G5 6.3.3 - Fix to show one row per central/shared budget instead of a row per promotio in report
                                to avoid the repetition of same numbers for those promotions sharing budget master which is messing up totals.
*****************************************************************************************/
   c_process_name             CONSTANT execution_log.process_name%TYPE := 'P_RPT_CASH_BUDGET_USAGE_DETAIL';
   c_release_level            CONSTANT execution_log.release_level%TYPE := 2.0;
   c_created_by               CONSTANT rpt_awardmedia_summary.created_by%TYPE := 0;

   v_stage                    VARCHAR2(500);
   v_promotion_string         rpt_cash_budget_usage_detail.promotion_string%TYPE;
   budget_detail_rec          rpt_cash_budget_usage_detail%ROWTYPE;
   v_budget_master_id         budget_master.budget_master_id%TYPE := 0;
   v_user_id                  budget.user_id%TYPE := 0;
   v_node_id                  budget.node_id%TYPE := 0;
   v_promo_count              NUMBER := 0; --02/13/2017
   v_ins_rec                  VARCHAR2(1);
   v_rpt_id                   rpt_cash_budget_usage_detail.rpt_cash_budget_usage_dtl_id%TYPE;               
   v_budget_amt               rpt_cash_budget_usage_detail.budget_amt%TYPE;
   v_budget_used              rpt_cash_budget_usage_detail.budget_used%TYPE;
   v_budget_remaining         rpt_cash_budget_usage_detail.budget_remaining%TYPE;
   v_budget_status            rpt_cash_budget_usage_detail.budget_status%TYPE;
   v_is_active                rpt_cash_budget_usage_detail.is_active%TYPE;
   v_start_date               rpt_cash_budget_usage_detail.start_date%TYPE;
   v_end_date                 rpt_cash_budget_usage_detail.end_date%TYPE;
   v_inception_date           rpt_cash_budget_usage_detail.inception_date%TYPE;
   v_overrideable             rpt_cash_budget_usage_detail.overrideable%TYPE;
   v_rule_for_final_payout    rpt_cash_budget_usage_detail.rule_for_final_payout%TYPE;
   v_rec_cnt                  INTEGER := 0;
   v_reallocated_amount       rpt_cash_budget_usage_detail.reallocated_amount%TYPE;
   v_parm_list                execution_log.text_line%TYPE;   --03/11/2019  Bug 78631
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
              FROM rpt_cash_budget_trans_detail
             WHERE budget_trans_type IN ( 'deposit', 'transfer') 
             and budget_reallocation_history_id is null     
             GROUP BY budget_id) rpt_dep,
           (SELECT budget_id, 
                   SUM(NVL(trans_amount,0)) amount
              FROM rpt_cash_budget_trans_detail
             WHERE budget_trans_type = 'expenditure'
             and budget_reallocation_history_id is null     
             GROUP BY budget_id) rpt_exp  
     WHERE b.budget_segment_id = bb.budget_segment_id
       AND bb.budget_master_id = bm.budget_master_id
       AND bm.award_type = 'cash'
       AND b.user_id          = uc.user_id (+)
       AND b.budget_id        = rpt_dep.budget_id (+)
       AND b.budget_id        = rpt_exp.budget_id (+)
       AND (p_start_date      < b.date_created OR p_start_date    < b.date_modified OR p_start_date < bb.date_created OR p_start_date    < bb.date_modified) --Bug # 65423 Added check for Budget_segment table too
       AND (b.date_created    <= p_end_date    OR b.date_modified <= p_end_date OR bb.date_created    <= p_end_date    OR bb.date_modified <= p_end_date) --Bug # 65423 Added check for Budget_segment table too
     ORDER BY bb.budget_master_id, b.budget_id, b.node_id, b.user_id;

  CURSOR cur_promotion_name (p_master_budget_id NUMBER) IS
   SELECT promotion_name
     FROM promotion
    WHERE cash_budget_master_id = p_master_budget_id;

  CURSOR cur_budget_promo_owner (p_master_budget_id NUMBER) IS
   SELECT p.promotion_id, p.promotion_name
     FROM promotion p, budget b, budget_segment bb
    WHERE p.cash_budget_master_id = bb.budget_master_id
      AND bb.budget_segment_id = b.budget_segment_id
      AND bb.budget_master_id = p_master_budget_id
      AND p.promotion_status = 'live'
      AND b.user_id IS NULL
      AND b.node_id IS NULL
      UNION  
      SELECT p.promotion_id, p.promotion_name
     FROM promo_nomination pr,
          promotion p, 
          budget b, 
          budget_segment bb
          ,budget_master bm
    WHERE pr.public_rec_budget_master_id = bb.budget_master_id
      AND bm.budget_master_id = bb.budget_master_id
      AND bm.award_type = 'cash'  
      AND bb.budget_segment_id = b.budget_segment_id
      AND bb.budget_master_id = p_master_budget_id
      AND p.promotion_status = 'live'
      AND p.promotion_id= pr.promotion_id
      AND b.user_id IS NULL
      AND b.node_id IS NULL
      AND ROWNUM<2; 
            
BEGIN
v_parm_list := 
'p_user_id 			     :='||p_user_id 		  ||CHR(10)||
'p_start_date        :='||p_start_date    ||CHR(10)|| 
'p_end_date          :='||p_end_date          ; 
  v_stage := 'Procedure started';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  v_stage := 'Remove budget usage detail records those are not present in budget table';
  DELETE FROM rpt_cash_budget_usage_detail rpt
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
    budget_detail_rec.budget_segment_id   := rec_detail.budget_segment_id;
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
    budget_detail_rec.budget_remaining   := rec_detail.budget_remaining + NVL(budget_detail_rec.reallocated_amount,0);   
    budget_detail_rec.budget_owner_type  := 'promotion';
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
    <<skip_promo>>  --02/13/2017
    BEGIN
      v_stage := 'Check budget details in report table';
      SELECT rpt_cash_budget_usage_dtl_id,
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
        FROM rpt_cash_budget_usage_detail
       WHERE budget_id  = rec_detail.budget_id
         AND budget_master_id = rec_detail.budget_master_id AND ROWNUM=1;

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
        UPDATE rpt_cash_budget_usage_detail
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
               reallocated_amount = budget_detail_rec.reallocated_amount    
         WHERE rpt_cash_budget_usage_dtl_id = v_rpt_id;     
      
      END IF;
         
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        v_ins_rec := 'Y';       
    END;

    IF v_ins_rec = 'Y' THEN

      IF(budget_detail_rec.budget_owner_type = 'promotion') THEN

        --FOR rec_budget_promo_owner IN cur_budget_promo_owner(budget_detail_rec.budget_master_id) LOOP  --02/13/2017
          
--          budget_detail_rec.budget_owner_id := rec_budget_promo_owner.promotion_id;
--          budget_detail_rec.budget_owner_name := rec_budget_promo_owner.promotion_name;
             budget_detail_rec.budget_owner_id := NULL;
             budget_detail_rec.budget_owner_name := budget_detail_rec.promotion_string;


          budget_detail_rec.rpt_cash_budget_usage_dtl_id := rpt_cash_budgt_usage_dtl_pk_sq.NEXTVAL;  
          
          v_stage := 'Insert records into report table';   
          p_ins_rpt_cash_bud_usage_dtl(budget_detail_rec);
          
          
        --END LOOP; --02/13/2017
      
      ELSE
        budget_detail_rec.rpt_cash_budget_usage_dtl_id := rpt_cash_budgt_usage_dtl_pk_sq.NEXTVAL;
        v_stage := 'Insert records into report table2'; 
        P_INS_RPT_CASH_BUD_USAGE_DTL(budget_detail_rec);
        v_rec_cnt := v_rec_cnt + 1;
      END IF;
    
    END IF;
      
  END LOOP;

  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', 'Insert report table (' || v_rec_cnt || ' records processed)', NULL);

  v_stage := 'Delete removed budget if any from report table';
  DELETE FROM rpt_cash_budget_usage_detail rpt
   WHERE NOT EXISTS (SELECT 'X'
                       FROM budget
                      WHERE budget_id = rpt.budget_id); 

  v_rec_cnt := SQL%ROWCOUNT;
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);


  v_stage := 'Procedure completed';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    p_return_code   := 99;
    p_error_message := v_stage || ': '|| SUBSTR(SQLERRM,1,250);
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage|| ': ' ||v_parm_list|| ': ' || SQLCODE || ', ' || SQLERRM, NULL);
END; -- p_rpt_cash_budget_usage_detail


PROCEDURE P_INS_RPT_CASH_BUDGET_TRAN_DTL
  (p_budget_trans_dtl_rec IN RPT_CASH_BUDGET_TRANS_DETAIL%rowtype) IS
/*******************************************************************************

   Purpose:  Insert record into rpt_cash_budget_TRANS_DETAIL reporting table

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Suresh J     04/22/2016  Initial Creation
   nagarajs     01/24/2017  G5.6.3.3 changes
*******************************************************************************/

BEGIN

 INSERT INTO RPT_CASH_BUDGET_TRANS_DETAIL
      (rpt_cash_budget_TRANS_DTL_ID, budget_id, budget_master_id,budget_segment_id,budget_status,
       budget_trans_type, journal_id, trans_date,
       trans_amount, recipient_user_id, recipient_name,recipient_first_name,recipient_last_name, --01/24/2017
       promotion_id, promotion_name, date_created, created_by,
       recipient_country_id, budget_history_id, merch_order_id,
       modified_by, date_modified, version, budget_reallocation_history_id)
 VALUES
      (p_budget_trans_dtl_rec.RPT_CASH_BUDGET_TRANS_DTL_ID, p_budget_trans_dtl_rec.budget_id,
       p_budget_trans_dtl_rec.budget_master_id,p_budget_trans_dtl_rec.budget_segment_id, nvl(p_budget_trans_dtl_rec.budget_status,' '),
       p_budget_trans_dtl_rec.budget_trans_type,
       p_budget_trans_dtl_rec.journal_id, p_budget_trans_dtl_rec.trans_date,
       p_budget_trans_dtl_rec.trans_amount, p_budget_trans_dtl_rec.recipient_user_id,
       p_budget_trans_dtl_rec.recipient_name, p_budget_trans_dtl_rec.recipient_first_name,p_budget_trans_dtl_rec.recipient_last_name, --01/24/2017
       p_budget_trans_dtl_rec.promotion_id,
       p_budget_trans_dtl_rec.promotion_name, p_budget_trans_dtl_rec.date_created,
       p_budget_trans_dtl_rec.created_by, p_budget_trans_dtl_rec.recipient_country_id,
       p_budget_trans_dtl_rec.budget_history_id, p_budget_trans_dtl_rec.merch_order_id,
       p_budget_trans_dtl_rec.modified_by, p_budget_trans_dtl_rec.date_modified, p_budget_trans_dtl_rec.version,
       p_budget_trans_dtl_rec.budget_reallocation_history_id
       );
EXCEPTION
   WHEN others THEN
    prc_execution_log_entry('P_INS_RPT_CASH_BUDGET_TRAN_DTL',1,'ERROR',SQLERRM,null);
END; -- P_INS_RPT_CASH_BUDGET_TRAN_DTL
--------------------------------------------------------------------------------

PROCEDURE P_INS_RPT_CASH_BUD_USAGE_DTL
  (p_budget_usage_dtl_rec IN rpt_cash_budget_usage_detail%rowtype) IS
/*******************************************************************************

   Purpose:  Insert record into rpt_cash_budget_usage_detail reporting table

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Suresh J     04/22/2016  Initial Creation
*******************************************************************************/

BEGIN

 INSERT INTO rpt_cash_budget_usage_detail
      (rpt_cash_budget_usage_dtl_id, node_id, budget_name_cm_asset_code,
       budget_name_cm_key, budget_owner_id, budget_owner_name,
       budget_owner_type, budget_amt, budget_used, budget_remaining,
       budget_master_id,budget_segment_id, budget_id, budget_status, inception_date, overrideable,
       cap_amount, cost_center, budget_master_name, rule_for_final_payout, node_owner, promotion_string, budget_type, is_active, start_date, end_date,
       budget_owner_cntry_id, date_created, created_by, 
       modified_by, date_modified, version,multi_promotion, reallocated_amount
       )
 VALUES
      (p_budget_usage_dtl_rec.rpt_cash_budget_usage_dtl_id, p_budget_usage_dtl_rec.node_id,
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
    prc_execution_log_entry('P_INS_RPT_CASH_BUD_USAGE_DTL',1,'ERROR',SQLERRM,null);
END; -- P_INS_RPT_CASH_BUD_USAGE_DTL
--------------------------------------------------------------------------------

END;
/
