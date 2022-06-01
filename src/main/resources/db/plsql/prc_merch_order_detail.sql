CREATE OR REPLACE PROCEDURE prc_merch_order_detail(budget_merch_order IN rpt_budget_trans_detail%ROWTYPE, p_merch_type IN VARCHAR2, p_new_budget_master IN NUMBER) IS
/******************************************************************************
   NAME:       prc_merch_order_detail
   PURPOSE:    This procedure populates the rpt_budget_trans_detai table for Recognition promotion

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        1/9/2008    M Lindvig      1. Created this procedure.
   1.1        1/23/2008   M Lindvig      2. Fixed Bug #18887
   1.2        5/13/2008   M Lindvig	 3. Fixed Bug #19900 
   1.3        10/08/2012  Ravi Dhanekula  Removed processing for NON-PAX as a part of G5 changes.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     prc_merch_order_detail
      Sysdate:         1/9/2008
      Date and Time:   1/9/2008, 12:04:51 PM, and 1/9/2008 12:04:51 PM
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/


CURSOR merch_promo_cur(p_budget_master_id IN budget.BUDGET_MASTER_ID%type, p_budget_id IN budget.budget_id%TYPE) IS
        SELECT distinct p.PROMOTION_ID, p.PROMOTION_NAME
          FROM merch_order mo, claim c, budget b, promotion p
            WHERE mo.CLAIM_ID = c.CLAIM_ID
            AND c.NODE_ID = NVL(b.NODE_ID, c.NODE_ID)
            AND p.AWARD_BUDGET_MASTER_ID = b.BUDGET_MASTER_ID
            AND c.PROMOTION_ID = p.PROMOTION_ID
           -- AND mo.CREATED_BY = b.USER_ID
            AND p.award_budget_master_id = p_budget_master_id
            AND p.award_type = p_merch_type
            AND b.BUDGET_ID = p_budget_id;
    
   CURSOR merch_cur (pi_promo_id IN promotion.promotion_id%TYPE, pi_budget_id IN budget.BUDGET_ID%TYPE) IS    
    SELECT  mo.points trans_amt,
             mo.date_created trans_date,
             fnc_get_user_name(mo.participant_id) pax_name,
             mo.participant_id,
            j.JOURNAL_ID
     FROM    merch_order mo, claim c, budget b, journal j, activity a, activity_journal aj
     WHERE   mo.merch_order_id IN
             ( SELECT  a.merch_order_id
               FROM    activity a
               WHERE   a.promotion_id = pi_promo_id)
            AND c.CLAIM_ID = mo.CLAIM_ID
            AND j.BUDGET_ID = b.BUDGET_ID
            AND j.JOURNAL_ID = aj.JOURNAL_ID
            AND a.ACTIVITY_ID = aj.ACTIVITY_ID
            AND a.CLAIM_ID = c.CLAIM_ID
            AND b.BUDGET_ID = pi_budget_id
            AND j.PROMOTION_ID = pi_promo_id;
/***
 -- UNION
CURSOR merch_cur_non_pax (pi_budget_master_id IN BUDGET_MASTER.BUDGET_MASTER_ID%TYPE) IS
     SELECT  mo.points trans_amt,
             mo.date_created trans_date,
             NULL pax_name,
             mo.participant_id,
            NULL,
            c.promotion_id,
            p.PROMOTION_NAME,
            bu.budget_id
     FROM    merch_order mo, claim c, budget_master b, promotion p, budget bu
     WHERE   mo.merch_order_id IN
             ( SELECT  a.merch_order_id
               FROM    activity a)
               --WHERE   a.promotion_id = pi_promo_id)
            AND c.CLAIM_ID = mo.CLAIM_ID
            AND mo.PARTICIPANT_ID IS NULL
            --AND mo.CREATED_BY = b.USER_ID
            --AND c.NODE_ID = NVL(b.NODE_ID, c.NODE_ID)
            AND p.AWARD_BUDGET_MASTER_ID = b.BUDGET_MASTER_ID
            AND c.PROMOTION_ID = p.PROMOTION_ID
            AND DECODE(b.BUDGET_TYPE, 'node', c.NODE_ID , 'pax', c.SUBMITTER_ID , 'central', b.BUDGET_MASTER_ID) =  DECODE(b.BUDGET_TYPE, 'node', bu.NODE_ID , 'pax', bu.USER_ID , 'central', bu.BUDGET_MASTER_ID) --Bug # 19900 
            AND bu.BUDGET_MASTER_ID = b.BUDGET_MASTER_ID
            AND b.BUDGET_MASTER_ID = pi_budget_master_id;
***/
            
            
   budget_trans_rec              rpt_budget_trans_detail%ROWTYPE;
BEGIN
   FOR promo_rec IN merch_promo_cur (budget_merch_order.budget_master_id, budget_merch_order.budget_id) LOOP
      --resets the variable
      budget_trans_rec                   := NULL;
      --sets values given from the package
      budget_trans_rec.budget_master_id  := budget_merch_order.budget_master_id;
      budget_trans_rec.budget_id         := budget_merch_order.budget_id;
      budget_trans_rec.budget_status     := budget_merch_order.budget_status;
      budget_trans_rec.budget_trans_type := budget_merch_order.budget_trans_type;
      budget_trans_rec.date_created      := budget_merch_order.date_created;
      budget_trans_rec.created_by        := budget_merch_order.created_by;
      
      budget_trans_rec.promotion_id      := promo_rec.promotion_id;
      budget_trans_rec.promotion_name    := promo_rec.promotion_name;
      -- loop thru merch detail, by promo
      
      FOR merch_rec IN merch_cur (promo_rec.promotion_id, budget_merch_order.budget_id) LOOP
      
        budget_trans_rec.recipient_name     := merch_rec.pax_name;
        budget_trans_rec.recipient_user_id  := merch_rec.participant_id;
        budget_trans_rec.trans_date        := merch_rec.trans_date;
        budget_trans_rec.trans_amount      := merch_rec.trans_amt;
        budget_trans_rec.journal_id        := merch_rec.journal_id;   
          
        SELECT rpt_budget_trans_detail_pk_sq.NEXTVAL INTO budget_trans_rec.rpt_budget_trans_detail_id FROM dual;
        
        PKG_REPORT_BUDGET.P_INSERT_RPT_BUDGET_TRANS_DTL(budget_trans_rec);
      END LOOP; -- FOR merch_rec IN merch_cur
    END LOOP; -- FOR promo_rec IN promo_cur
    
    
    budget_trans_rec := NULL; --Reset Value for Non-Pax
    IF(p_new_budget_master = 1) THEN
        budget_trans_rec.budget_master_id  := budget_merch_order.budget_master_id;
        budget_trans_rec.budget_status     := budget_merch_order.budget_status;
        budget_trans_rec.budget_trans_type := budget_merch_order.budget_trans_type;
        budget_trans_rec.date_created      := budget_merch_order.date_created;
        budget_trans_rec.created_by        := budget_merch_order.created_by;
       -- budget_trans_rec.promotion_id      := promo_rec.promotion_id;
       -- budget_trans_rec.promotion_name    := promo_rec.promotion_name;
    
--        FOR merch_rec_non_pax IN merch_cur_non_pax(budget_trans_rec.budget_master_id)  LOOP
--            budget_trans_rec.recipient_name     := merch_rec_non_pax.non_pax_name;
--            budget_trans_rec.recipient_user_id  := NULL;  -- leave null if non-pax
--            budget_trans_rec.trans_date         := merch_rec_non_pax.trans_date;
--            budget_trans_rec.trans_amount       := merch_rec_non_pax.trans_amt;
--            budget_trans_rec.promotion_id       := merch_rec_non_pax.promotion_id;
--            budget_trans_rec.promotion_name     := merch_rec_non_pax.promotion_name;
--            budget_trans_rec.budget_id          := merch_rec_non_pax.budget_id;  --Bug # 19900 
--  
--            
--            SELECT rpt_budget_trans_detail_pk_sq.NEXTVAL INTO budget_trans_rec.rpt_budget_trans_detail_id FROM dual;
--        
--            PKG_REPORT_BUDGET.P_INSERT_RPT_BUDGET_TRANS_DTL(budget_trans_rec);
--        END LOOP;
    
    
    END IF;
    
   EXCEPTION
     WHEN NO_DATA_FOUND THEN
       NULL;
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       RAISE;
END prc_merch_order_detail;
/