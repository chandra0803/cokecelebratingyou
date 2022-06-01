CREATE OR REPLACE PROCEDURE prc_award_generator_extract
(p_awardGenBatchId IN NUMBER,
 p_out_awardtype   OUT VARCHAR2,
 p_out_promotionname OUT VARCHAR2,
 p_out_awardgen_setupname OUT VARCHAR2,
 p_out_batchdate OUT VARCHAR2,--DATE 04/20/2016 Bug#66419
 P_out_return_code OUT NUMBER,
 p_out_ref_cursor_merchandise OUT sys_refcursor,
 p_out_ref_cursor_points OUT sys_refcursor)
 IS
 /*******************************************************************************
  -- Purpose: To extract awardgen data for an awardgen batch
  --
  -- Person                      Date       Comments
  -- -----------                   --------   -----------------------------------------------------
  -- Ravi Dhanekula        08/19/2015 Creation
  -- nagarajs              04/18/2016 Bug 66418 - Extract is displaying pax that should not be on the report with award amount of 0
  -- Ravi Arumugam        04/20/2016  Bug 66419 - Batch Date is not displayed in the Award file extract email
  -- Loganathan  		  04/12/2019  Bug 79012 - Award File generator creates duplicate records for plateau file
  *******************************************************************************/
 
 v_awardtype promotion.AWARD_TYPE%TYPE;
 v_promotion_name promotion.promotion_name%TYPE;
 v_p_out_awardgen_setupname awardgenerator.name%TYPE;
 v_out_batch_date VARCHAR2(75);--DATE 04/20/2016 Bug#66419
 
 BEGIN
 
 prc_execution_log_entry ('PRC_AWARD_GENERATOR_EXTRACT',
                            1,
                            'INFO',
                            'Process Started ',
                            NULL);
 
 SELECT p.award_type,p.promotion_name,a.name,TO_CHAR(ab.start_date,'MM/DD/YYYY') || '-' || TO_CHAR(ab.end_date,'MM/DD/YYYY') AS issue_date /*--ab.issue_date 04/20/2016 Bug#66419*/ INTO v_awardtype,v_promotion_name,v_p_out_awardgen_setupname,v_out_batch_date FROM promotion p, awardgenerator a, awardgen_batch ab
             WHERE ab.awardgen_batch_id = p_awardGenBatchId
                 AND ab.awardgen_id = a.awardgen_id
                 AND a.promotion_id = p.promotion_id;

IF v_awardtype = 'merchandise' THEN

OPEN p_out_ref_cursor_merchandise FOR
SELECT au.user_name,
       pm.ordinal_position,
       ap.issue_date,
       ap.anniversary_num_days AS anniversaryDays,
       ap.anniversary_num_years AS anniversaryYears
  FROM awardgen_participant ap,
       application_user au,
       promo_merch_program_level pm,
       awardgen_batch ab,                        --04/18/2016
       awardgen_award aa                         --04/18/2016
 WHERE     ap.awardgen_batch_id = p_awardGenBatchId
       AND au.user_id = ap.user_id
       AND ap.level_id = pm.promo_merch_program_level_id
       AND ab.awardgen_batch_id = ap.awardgen_batch_id    --04/18/2016
       AND ab.awardgen_id = aa.awardgen_id                --04/18/2016
       AND aa.years = ap.anniversary_num_years            --04/18/2016
       AND aa.LEVEL_ID=PM.promo_merch_program_level_id;   --04/12/2019 Bug 79012
       
       OPEN p_out_ref_cursor_points FOR      
       SELECT * FROM DUAL WHERE 1 = 2;

ELSE

OPEN p_out_ref_cursor_merchandise FOR
       SELECT * FROM DUAL WHERE 1 = 2;

OPEN p_out_ref_cursor_points FOR
SELECT au.user_name AS userName,
       ap.award_amount AS awardAmount,
       ap.issue_date AS awardDate,
       ap.anniversary_num_days AS anniversaryDays,
       ap.anniversary_num_years AS anniversaryYears
  FROM awardgen_participant ap, application_user au,
          awardgen_batch ab,                        --04/18/2016
          awardgen_award aa                         --04/18/2016
 WHERE ap.awardgen_batch_id = p_awardGenBatchId 
 AND au.user_id = ap.user_id
 AND ab.awardgen_batch_id = ap.awardgen_batch_id    --04/18/2016
 AND ab.awardgen_id = aa.awardgen_id                --04/18/2016
 AND aa.years = ap.anniversary_num_years;           --04/18/2016
       

END IF;--IF v_awardtype = 'merchandise'
 

p_out_awardtype := v_awardtype;
p_out_promotionname := v_promotion_name;
p_out_awardgen_setupname := v_p_out_awardgen_setupname;
p_out_batchdate := v_out_batch_date;

P_out_return_code:=0;

EXCEPTION WHEN OTHERS THEN
P_out_return_code :=99;
 prc_execution_log_entry ('PRC_AWARD_GENERATOR_EXTRACT',
                            1,
                            'INFO',
                            SQLERRM,
                            NULL); 
 END;
/
