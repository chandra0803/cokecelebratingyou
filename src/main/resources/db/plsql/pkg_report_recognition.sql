CREATE OR REPLACE PACKAGE pkg_report_recognition
  IS 

/*-----------------------------------------------------------------------------

  Person             Date           Comments
  -----------     ----------   ------------------------------------------------

  Arun S          02/04/2011   Added Procedure P_RPT_PURL_CONTRIBUTION_DETAIL
                               Added Procedure P_RPT_PURL_CONTRIBUTION_SUM
  Arun S          04/27/2011   Added Procedure PRC_GET_MANAGER_DETAIL to retrive recepient manager details
  Chidamba        09/21/2012   Added parameter p_in_promotion_status to filter data based on promo status,
                                  and filter by role for Receivers 
  Ravi Dhanekula  10/05/2012   Changed the functions to accept multi-selects on promotions and departments.                                                                      
-----------------------------------------------------------------------------*/

  PROCEDURE P_RPT_RECOGNITION_DETAIL
  (p_in_requested_user_id      IN  NUMBER,
   p_in_start_date             IN  DATE,
   p_in_end_date               IN  DATE,
   p_out_return_code           OUT NUMBER,
   p_out_error_message         OUT VARCHAR2);
  /*PROCEDURE P_RPT_RECOGNITION_SUMMARY
  (p_in_requested_user_id      IN  NUMBER,
   p_out_return_code           OUT NUMBER,
   p_out_error_message         OUT VARCHAR2);*/
   
  PROCEDURE P_RPT_RECOG_DETL_FILELOAD;
  
  PROCEDURE P_INSERT_RPT_RECOGNITION_DTL
    (p_recognition_dtl_rec IN RPT_RECOGNITION_DETAIL%ROWTYPE);

  FUNCTION FNC_ELIGIBILITY_COUNT
  ( p_in_promotion_id IN VARCHAR2,
    p_in_node_id      IN VARCHAR2,
    p_in_giver_recvr  IN VARCHAR2,
    p_in_record_type  IN VARCHAR2,
    p_in_promotion_status IN VARCHAR2,  --09/21/2012
    p_in_pax_status   IN VARCHAR2,
    p_in_job_code     IN VARCHAR2,
    p_in_department   IN VARCHAR2,
    p_in_promo_type   IN VARCHAR2 DEFAULT 'recognition')
    RETURN  NUMBER;

  FUNCTION FNC_ACTUAL_GIVER_RECVR_COUNT
    ( p_in_node_id IN VARCHAR2,
      p_in_promotion_id IN VARCHAR2,
      p_in_award_type IN VARCHAR2,
      p_in_behavior IN VARCHAR2,
      p_in_promotion_status IN VARCHAR2,  --09/21/2012
--      p_in_date_approved IN DATE,
      p_in_from_date       IN DATE,
      p_in_to_date       IN DATE,
      p_in_giver_recvr_type IN VARCHAR2,
      p_in_record_type IN VARCHAR2,
      p_in_pax_status   IN VARCHAR2,
      p_in_job_code     IN VARCHAR2,
      p_in_department   IN VARCHAR2)
    RETURN  NUMBER;

  FUNCTION FNC_ACTUAL_RECOGNITION_COUNT
    ( p_in_node_id IN NUMBER,
      p_in_promotion_id IN VARCHAR2,
      p_in_award_type IN VARCHAR2,
      p_in_behavior IN VARCHAR2,
      p_in_promotion_status IN VARCHAR2,  --09/21/2012
--      p_in_date_approved IN DATE,
      p_in_from_date       IN DATE,
      p_in_to_date       IN DATE,
      p_in_giver_recvr_type IN VARCHAR2,
      p_in_record_type IN VARCHAR2,
      p_in_pax_status   IN VARCHAR2,
      p_in_job_code     IN VARCHAR2,
      p_in_department   IN VARCHAR2)
    RETURN  NUMBER;

FUNCTION FNC_ACTUAL_AWARD_COUNT
  ( p_in_node_id IN NUMBER,
    p_in_promotion_id IN VARCHAR2,
    p_in_award_type IN VARCHAR2,
    p_in_behavior IN VARCHAR2,
    p_in_promotion_status IN VARCHAR2,  
      p_in_from_date       IN DATE,
      p_in_to_date       IN DATE,
    p_in_giver_recvr_type IN VARCHAR2,
    p_in_record_type IN VARCHAR2,
    p_in_pax_status   IN VARCHAR2,
    p_in_job_code     IN VARCHAR2,
    p_in_department   IN VARCHAR2  )
  RETURN  NUMBER;
    
    
-- This function gives the total which is used in the calculation of percentage
-- on iReports
/*FUNCTION f_total (p_in_header_node_id IN NUMBER, 
                  p_promotion_id  NUMBER ,
                  p_award_type VARCHAR2,
                  p_giver_recvr_type VARCHAR2,
                  p_behavior VARCHAR2   ) RETURN NUMBER ;*/

FUNCTION fnc_get_level_item
  ( pi_merch_order_id   IN merch_order.merch_order_id%TYPE)
  RETURN VARCHAR2;
PROCEDURE P_PROGRAM_REFERENCE_NBR
  (p_in_requested_user_id      IN  NUMBER,
   p_in_start_date             IN  DATE,
   p_in_end_date               IN  DATE,
   p_out_return_code           OUT NUMBER,
   p_out_error_message         OUT VARCHAR2);
PROCEDURE P_AWARD_EARNINGS_REPORT;

--FUNCTION FNC_RECIPIENT_COUNT
--  ( p_in_promotion_id     IN VARCHAR2,
--    p_in_node_id          IN NUMBER,
--    p_in_record_type      IN VARCHAR2,
--    p_in_recepient_status IN VARCHAR2, 
--    p_in_from_date        IN DATE,
--    p_in_to_date          IN DATE)
--  RETURN  NUMBER;

PROCEDURE P_RPT_PURL_CONTRIBUTION_DETAIL(p_in_requested_user_id      IN  NUMBER,
                                         p_in_start_date             IN  DATE,
                                         p_in_end_date               IN  DATE,
                                         p_out_return_code           OUT NUMBER,
                                         p_out_error_message         OUT VARCHAR2);

/*PROCEDURE P_RPT_PURL_CONTRIBUTION_SUM(p_in_requested_user_id      IN  NUMBER,
                                      p_in_start_date             IN  DATE,
                                      p_in_end_date               IN  DATE,
                                      p_out_return_code           OUT NUMBER,
                                      p_out_error_message         OUT VARCHAR2);*/

PROCEDURE p_rpt_recognition_detail_sa (
                                   p_in_requested_user_id      IN  NUMBER,
                                   p_in_start_date             IN  DATE,
                                   p_in_end_date               IN  DATE,
                                   p_out_return_code           OUT NUMBER,
                                   p_out_error_message         OUT VARCHAR2);

END; -- Package spec

/
CREATE OR REPLACE PACKAGE BODY pkg_report_recognition IS
  /*-----------------------------------------------------------------------------

  Person             Date           Comments
  -----------     ----------   ------------------------------------------------ 
  KrishnaDeepika  9/29/2014    bug fix 55111      
 Ravi Dhanekula  07/20/2016 Replaced obfuscation toolkit with dbms_crypto 
 Suresh J        04/01/2019   SA Integeration with DayMaker changes for reports              
-----------------------------------------------------------------------------*/
-- global variables
g_merch_type      CONSTANT VARCHAR2(100) := 'merchandise';
g_sweep_type      CONSTANT VARCHAR2(100) := 'winner';
/*********** nagarajs 04/21/2017 Commented out  since no longer summary tables used in query packages***********/
/*FUNCTION f_total (p_in_header_node_id IN NUMBER,
                  p_promotion_id  NUMBER ,
                  p_award_type VARCHAR2,
                  p_giver_recvr_type VARCHAR2,
                  p_behavior VARCHAR2   ) RETURN NUMBER IS
v_total_count    NUMBER(18):= 0 ;
BEGIN
SELECT SUM(recognition_cnt)
  INTO v_total_count
  FROM
 (SELECT NVL(SUM(recognition_cnt),0) recognition_cnt
    FROM RPT_RECOGNITION_SUMMARY
   WHERE header_node_id = NVL(p_in_header_node_id,0)
     AND promotion_id = NVL(p_promotion_id, promotion_id)
     AND award_type = NVL(p_award_type, award_type)
     AND giver_recvr_type = NVL(p_giver_recvr_type, giver_recvr_type)
     AND behavior = NVL(p_behavior, behavior)
     AND record_type LIKE '%node%'
  UNION
  SELECT NVL(SUM(recognition_cnt),0)
    FROM RPT_RECOGNITION_SUMMARY
   WHERE detail_node_id = NVL(p_in_header_node_id,0)
     AND promotion_id = NVL(p_promotion_id, promotion_id)
     AND award_type = NVL(p_award_type, award_type)
     AND giver_recvr_type = NVL(p_giver_recvr_type, giver_recvr_type)
     AND behavior = NVL(p_behavior, behavior)
     AND record_type LIKE '%team%') ;
   RETURN v_total_count ;
EXCEPTION
  WHEN OTHERS THEN
   v_total_count := 0 ;
   RETURN v_total_count ;
END ; -- FUNCTION f_total
*/

FUNCTION fnc_get_level_item
  ( pi_merch_order_id   IN merch_order.merch_order_id%TYPE)
  RETURN VARCHAR2 IS
  
  v_return      VARCHAR2(2000);
  v_pmpl_id     merch_order.promo_merch_program_level_id%TYPE;
  
BEGIN

  SELECT  mo.product_description,
          mo.promo_merch_program_level_id
  INTO    v_return,
          v_pmpl_id
  FROM    merch_order mo
  WHERE   mo.merch_order_id = pi_merch_order_id;
  
  IF v_return IS NULL THEN
  
    BEGIN
      SELECT  p.cm_asset_key
      INTO    v_return
      FROM    promo_merch_program_level p
      WHERE   p.promo_merch_program_level_id = v_pmpl_id;
    EXCEPTION
      WHEN OTHERS THEN
        v_return  := NULL;
    END;
    
  END IF;
  
  RETURN v_return;
  
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END;  -- FUNCTION fnc_get_level_item
/*********** nagarajs 04/21/2017 Commented out since no longer summary tables used in query packages***********/
/*PROCEDURE p_ins_rpt_recognition_summary 
  (p_in_summary_rec RPT_RECOGNITION_SUMMARY%ROWTYPE)
  IS
  
BEGIN
          INSERT INTO RPT_RECOGNITION_SUMMARY
             (rpt_recognition_summary_id,
              record_type,
              header_node_id,
              detail_node_id,
              giver_recvr_type,
              eligible_giver_recvr_cnt,
              actual_giver_recvr_cnt,
              recognition_cnt,
              award_type,
              media_amt,
              promotion_id,
              behavior,
              date_approved,
              pax_status,
              job_position,
              department,
              hier_level,
              is_leaf,
              date_created,
              created_by)
          VALUES
             (p_in_summary_rec.rpt_recognition_summary_id,
              p_in_summary_rec.record_type,
              p_in_summary_rec.header_node_id,
              p_in_summary_rec.detail_node_id,
              p_in_summary_rec.giver_recvr_type,
              p_in_summary_rec.eligible_giver_recvr_cnt,
              p_in_summary_rec.actual_giver_recvr_cnt,
              p_in_summary_rec.recognition_cnt,
              p_in_summary_rec.award_type,
              p_in_summary_rec.media_amt,
              p_in_summary_rec.promotion_id,
              p_in_summary_rec.behavior,
              p_in_summary_rec.date_approved,
              p_in_summary_rec.pax_status,
              p_in_summary_rec.job_position,
              p_in_summary_rec.department,
              p_in_summary_rec.hier_level,
              p_in_summary_rec.is_leaf,
              p_in_summary_rec.date_created,
              p_in_summary_rec.created_by);
EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry('p_ins_rpt_recognition_summary',1,'ERROR',SQLERRM,null);
END  ;*/  -- PROCEDURE p_ins_rpt_recognition_summary

PROCEDURE p_rpt_recognition_detail(p_in_requested_user_id      IN  NUMBER,
                                   p_in_start_date             IN  DATE,
                                   p_in_end_date               IN  DATE,
                                   p_out_return_code           OUT NUMBER,
                                   p_out_error_message         OUT VARCHAR2)IS
/*******************************************************************************
   Purpose:  Populate the rpt_recognition_detail reporting tables


   ** NOTE ** 2 fields are still missing due to table not created yet.
              final_approver_id and final_approver_name

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray          11/16/2005     Initial Creation
   Raju N            01/24/2005     Changed the code to populate the proxy user ID and name
   John Ernste         04/16/2007     Added the Final Approver id and final approver name from
                                the claim_approver_snapshot table.
   murphyc           10/29/2007     if merch promo, include merchandise_description
                                if non-pax info on merch_order, put that in as receiver info
   murphyc           12/04/2007     cr.participant_id is NULL if non-pax
   Arun S            05/27/2011     Bug 36422 fix. when a self recognition happens then cur_recvr pulls submitter 
                                records also which causes additional/duplicate record in Recognition report.
                                So removed comment out for the condition AND a.is_submitter = 0 
                                from the cursor cur_recvr.
   J Flees           08/31/2011     Report Refresh Performance.
                                Replaced looping process with single insert statement.
   nagarajs         03/21/2012  Bug #40345 fix to generate recognition detail report for live and expired promotions
                            but not for deleted promotion. 
   Chidamba         03/13/2012  G5 report changes to make incremental   
   Ravi Dhanekula     10/25/2012  Added recvr_badges_earned to rpt_recognition_detail table.
   Chidamba           12/12/2012  Change to populate role for receiver in JOB_POSITION column in detail table
   Chidamba           06/17/2013  Change to implement few scenario 
                                1. pax status changed in application_user table for both receiver and giver. 
                                2. job_position or department changed for receiver and giver.  
                                3. node name change 
   Ravi Dhanekula      10/07/2013     Change required to include merch level sweepstakes.
                    12/20/2013     Reverted the changes done on 12/12/12.
  Suresh J             01/31/2014     Bug fix #50951    Commented ccv_b and its references from rpt_recognition_detail table
  Suresh J           03/11/2014     Bug fix #51350   Added is_primary condition to user_address tables (giver and receiver)
  Ravi Dhanekula     04/01/2014     Added missing parentheses     
  Swati                11/05/2014    Bug 57696 - p_rpt_recognition_detail recvr_country_id update  
  Ravi Dhanekula 11/5/2014 Added recvr_full_name for sweep records.
                        01/23/2015 Bug # 59292 - Fixed issue with date_approved. We were taking date_submitted before the fix.
 Suresh J          01/29/2015   Bug #57300 - Fixed the issue of Node Name Changes that are not reflected in reports
 NAGARAJS          05/20/2015  Bug 62379 - update job_position or department to NULL.
 Ravi Arumugam    03/28/2016  Bug 66208- Denied Recognitions to have reason code on the Recognition Given ? List of Givers report extract 
 nagarajs         05/19/2016  Bug 66865 - In the report of 'Recognition Given - List of Givers' winner record are displaying twice.
 nagarajs         06/14/2016 Bug 66208 - Denied Recognitions to have reason code on the Recognition Given – List of Givers report extract 
 Gorantla         07/14/2017 WIP 34834/G6-2770/Bug # 74352 - Submitter comments added
 murphyc          06/16/2017  Bug 73135 - Tuning [apply G6-3242]
chidamba         02/15/2018 Added receiver login Id for sweepstakes, column was populating null in report table before
*******************************************************************************/
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_recognition_detail');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
   c_created_by         CONSTANT rpt_recognition_detail.created_by%TYPE:= p_in_requested_user_id;

   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
   v_tab_node_id        dbms_sql.number_table;
   v_tab_node_name      dbms_sql.varchar2_table;   
 
     CURSOR cur_sweep_activity  --(in_activity_id IN NUMBER)  --Chidamba 06/29/2012 alter cursor
     IS     
      SELECT
        un.node_id as node_id,
        a.activity_id, 
        a.date_created as award_date,
        n.name as node_name,
        p.promotion_name as promotion_name,
        nvl(p.promotion_id,0) as promotion_id,
        nvl(p.award_type,' ') as award_type,
        p.is_taxable as is_taxable,
        a.quantity as points,
        ua.country_id as country_id,
        au.user_id as participant_id,
        au.first_name as participant_first_name,
        au.middle_name as participant_middle_name,
        au.last_name as participant_last_name,
        au.user_name as participant_login_id,  --02/15/2018 added receiver login Id for sweepstake
                nvl(DECODE(au.is_active,1,'active',0,'inactive'),' ') participant_status,
        nvl(pe.position_type,' ') as job_title,
        nvl(pe.department_type, ' ') as department,
        DECODE (a.activity_discrim,'sweep',1,'sweeplevel',1,0) AS sweepstakes_won
     FROM activity a,
          node n,
          promotion p,
          promo_recognition pq,
          application_user au,
          user_node un,
          user_address ua,
          participant pax,
          participant_employer pe
      WHERE 
         a.activity_discrim  IN ('sweep','sweeplevel') --10/07/2013
        AND a.user_id =un.user_id
        AND a.user_id =au.user_id
        AND un.is_primary =1 
        AND un.node_id = n.node_id
        AND a.promotion_id = p.promotion_id
        AND p.promotion_id = pq.promotion_id
        AND au.user_id = pax.user_id
        AND au.user_id = ua.user_id
        AND pax.user_id = pe.user_id(+)
        AND pe.termination_date IS NULL
        AND p.promotion_status IN ('live','expired')            
        AND p.is_deleted = 0      
        AND (p_in_start_date   <  a.date_created   AND a.date_created <= p_in_end_date);
        
--Cursor to pick modified node name
  CURSOR cur_node_changed IS  --06/19/2013   --01/29/2015
    SELECT node_id,
           name
      FROM node
     WHERE date_modified BETWEEN p_in_start_date AND p_in_end_date;  
 
  --Cursor hierarchy
  CURSOR cur_hier (p_in_node_id rpt_hierarchy.node_id%TYPE) IS
    SELECT parent_node_id header_node_id,
           node_id,
           hier_level,
           is_leaf
      FROM rpt_hierarchy
     START WITH node_id = p_in_node_id
   CONNECT BY PRIOR parent_node_id = node_id;     

BEGIN
    v_stage := 'Start';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
  FOR rec_sweep_activity IN cur_sweep_activity
    LOOP
    INSERT INTO rpt_recognition_detail 
                       (rpt_recognition_detail_id,
                        recvr_activity_id,
                        promotion_id,
                        promotion_name,                      
                        recvr_node_id,
                        recvr_node_name,
                  --      is_open,
                        award_type,
                        award_amt,
                        date_approved,
                        date_submitted,
                        recvr_login_id, --02/15/2018 added receiver login Id for sweepstake
                        recvr_user_id,
                        recvr_country_id,
                        recvr_first_name,
                        recvr_middle_name,
                        recvr_full_name,
                        recvr_last_name,
                        recvr_pax_status,                      
                        recvr_job_position,
                        recvr_department,
                        date_created,
                        created_by,      
                        version,               
                        recvr_sweepstakes_won)--10/30/2012
                VALUES (rpt_recognition_detail_pk_sq.NEXTVAL,
                        rec_sweep_activity.activity_id,
                        rec_sweep_activity.promotion_id,
                        rec_sweep_activity.promotion_name,
                        rec_sweep_activity.node_id,
                        rec_sweep_activity.node_name,
                   --     0,
                        rec_sweep_activity.award_type,
                        rec_sweep_activity.points,
                        rec_sweep_activity.award_date,
                        rec_sweep_activity.award_date,
                        rec_sweep_activity.participant_login_id,  --02/15/2018 added receiver login Id for sweepstake
                        rec_sweep_activity.participant_id,
                        rec_sweep_activity.country_id,
                        rec_sweep_activity.participant_first_name,
                        rec_sweep_activity.participant_middle_name,
                        fnc_format_user_name(rec_sweep_activity.participant_last_name, rec_sweep_activity.participant_first_name, rec_sweep_activity.participant_middle_name, NULL), --11/5/2014
                        rec_sweep_activity.participant_last_name,
                        rec_sweep_activity.participant_status,                     
                        rec_sweep_activity.job_title,
                        rec_sweep_activity.department,
                        SYSDATE,
                        p_in_requested_user_id,
                        1,
                        rec_sweep_activity.sweepstakes_won);--10/30/2012
    
    
    END LOOP;

--------------------------------------------------------------------------------
  DELETE gtt_rpt_badge_count; -- 06/16/2017
	INSERT INTO gtt_rpt_badge_count -- 06/16/2017
	(user_id, promo_type, date_created, badge_rule_count)
	SELECT pb.participant_id, p.promotion_type, TRUNC(pb.date_created), COUNT(DISTINCT pb.badge_rule_id)
	FROM 	participant_badge pb,
	      badge_promotion bp,
	      promotion P
	WHERE pb.promotion_id = bp.promotion_id
	AND   bp.eligible_promotion_id = p.promotion_id
	AND   p.promotion_type = 'recognition'
	AND   TRUNC(pb.date_created) BETWEEN TRUNC(p_in_start_date) AND p_in_end_date -- TRUNCs to make sure full day is counted
	GROUP BY pb.participant_id, p.promotion_type, TRUNC(pb.date_created);
--------------------------------------------------------------------------------
	
   -- rebuild detail record
   v_stage := 'INSERT/UPDATE rpt_recognition_detail';
 
      MERGE INTO rpt_recognition_detail d    
     USING ( 
              WITH detail_recog AS          
              (SELECT -- rpt_recognition_detail_pk_sq.NEXTVAL, 
                     d.giver_user_id,
                     d.giver_node_id,
                     d.giver_country_id,
                     d.giver_node_name,
                     d.giver_last_name,
                     d.giver_first_name,
                     d.giver_middle_name,
                     d.giver_full_name,
                     NVL(d.giver_pax_status, ' ') AS giver_pax_status,
                     d.giver_department AS giver_department,
                     d.giver_job_position AS giver_job_position,
                     d.giver_login_id,
                     d.giver_activity_id,
                     d.recvr_user_id,
                     d.recvr_node_id,
                     d.recvr_country_id,
                     d.recvr_node_name,
                     d.recvr_last_name,
                     d.is_reverse,
                     d.recvr_first_name,
                     d.recvr_middle_name,
                     d.recvr_full_name,
                     d.recvr_badges_earned,--10/25/2012
                     NVL(d.recvr_pax_status, ' ') AS recvr_pax_status,
                     d.recvr_department AS recvr_department,
                     d.recvr_job_position AS recvr_job_position,
                     d.recvr_login_id,
                     d.recvr_activity_id,
                     d.claim_id,
                     d.claim_recipient_id,
                     d.promotion_id,
                     d.promotion_name,
                     NVL(d.behavior, ' ') AS behavior,
                     NVL(d.award_type, ' ') AS award_type,
                     d.plateau_earned,
                     d.sweeptakes_won,
                     d.trans_date,
                     d.award_amt,
                     d.merchandise_description,
                     d.date_submitted,
                     d.date_approved,
                     d.final_approver_id,
                     d.final_approver_name,
                     d.reason_denied,       --03/28/2016 Bug#66208
                     d.proxy_user_id,
                     d.proxy_user_name,
                     d.submitter_comments   -- 07/14/2017
                FROM ( -- build detail records
                       SELECT -- rpt_recognition_detail_id,
                              COUNT(*) OVER() AS rec_cnt,  -- for test purposes only
                              -- giver data
                              cc.giver_user_id,
                              cc.giver_node_id,
                              ua_gu.country_id AS giver_country_id,
                              n_g.name                  AS giver_node_name,
                              INITCAP(au_gu.last_name)  AS giver_last_name,
                              INITCAP(au_gu.first_name) AS giver_first_name,
                              TRIM(au_gu.middle_name)   AS giver_middle_name,
                              fnc_format_user_name(au_gu.last_name, au_gu.first_name, au_gu.middle_name, au_gu.suffix) AS giver_full_name,
                              DECODE(au_gu.is_active, 1, 'active', 'inactive') AS giver_pax_status,
                              cpe_g.department_type     AS giver_department,
                              cpe_g.position_type       AS giver_job_position,
                              au_gu.user_name           AS giver_login_id,
                              a_g.activity_id           AS giver_activity_id,
                              -- receiver data
                              -- non-pax merchandise overrides recipient
                              cc.recvr_user_id   AS recvr_user_id,   
                              cc.recvr_node_id   AS recvr_node_id,
                              ua_ru.country_id AS recvr_country_id,
                              n_r.name           AS recvr_node_name,
                              INITCAP(au_ru.last_name)  AS recvr_last_name,
                              INITCAP(au_ru.first_name) AS recvr_first_name,
                              TRIM(au_ru.middle_name)                AS recvr_middle_name,
                              fnc_format_user_name(au_ru.last_name, au_ru.first_name, au_ru.middle_name, au_ru.suffix) AS recvr_full_name,
                        --    fnc_get_badge_count_by_user (au_ru.user_id,'recognition',TRUNC(cc.trans_date)) AS recvr_badges_earned,--10/25/2012 --06/16/2017
                              fnc_get_rpt_badge_count (au_ru.user_id,'recognition',TRUNC(cc.trans_date)) AS recvr_badges_earned, --06/16/2017
                              DECODE(au_ru.is_active, 1, 'active', 'inactive') AS recvr_pax_status,
                              cpe_r.department_type                AS recvr_department,
                             cpe_r.position_type                AS recvr_job_position,  --12/12/2012
--                              (SELECT role FROM user_node
--                               WHERE user_id = cc.recvr_user_id 
--                               and node_id = cc.recvr_node_id)     AS recvr_job_position,  --12/12/2012        --12/20/2013  
                              au_ru.user_name                      AS recvr_login_id,
                              cc.recvr_activity_id,
                              -- recognition data
                              cc.claim_id,
                              cc.claim_recipient_id,
                              cc.promotion_id,
                              cc.is_reverse,
                              cc.promotion_name,
--                            ccv_b.cms_name AS behavior,     --01/31/2014 
                              cc.behavior,                  --01/31/2014          
                              cc.award_type,
                              cc.plateau_earned,
                              cc.sweeptakes_won,
                              cc.trans_date,
                              cc.award_amt,
                              cc.merchandise_description,
                              cc.date_submitted,
                              cc.date_approved,
                              cc.final_approver_id,
                              fnc_format_user_name(au_fa.last_name, au_fa.first_name, au_fa.middle_name, au_fa.suffix) AS final_approver_name,
                              cc.approval_option_reason_type AS reason_denied,  --03/28/2016 Bug#66208
                              cc.proxy_user_id,
                              cc.submitter_comments,   -- 07/14/2017
                              fnc_format_user_name(au_pu.last_name, au_pu.first_name, au_pu.middle_name, au_pu.suffix) AS proxy_user_name
                         FROM ( -- match claims with receipients
                                SELECT mc.*,
                                       p.award_type,
                                       p.promotion_name,
                                       cr.participant_id   AS recvr_user_id,
                                      -- ua_r.country_id     AS recvr_country_id,
                                       cr.claim_item_id    AS claim_recipient_id,
                                       cr.node_id          AS recvr_node_id,
                                       a_r.quantity        AS award_amt,
                                       a_r.submission_date AS trans_date,
--                                       a_r.submission_date AS date_approved,
                                       ci_r.date_approved AS date_approved,--01/23/2015
                                       a_r.activity_id     AS recvr_activity_id,
                                       DECODE(p.award_type,
                                         'merchandise', NVL(mo.product_description, pmpl.cm_asset_key),
                                         NULL
                                       ) AS merchandise_description,
                                       DECODE(p.award_type,
                                         'merchandise', 1, NULL) AS plateau_earned,
                                       DECODE(ci_r.approval_status_type, 
                                         'winner', 1, NULL) AS sweeptakes_won,
                                      INITCAP(au.first_name) AS non_pax_first_name,
                                       INITCAP(au.last_name)  AS non_pax_last_name,
                                       fnc_format_user_name(au.last_name, au.first_name) AS non_pax_full_name
                                  FROM ( -- get claim with create dates ranked in descending order
                                         -- lowest rank is the most recently approved
                                         SELECT RANK() OVER (PARTITION BY cia.claim_item_id ORDER BY cia.date_created DESC, CLAIM_ITEM_APPROVER_ID DESC) AS approved_rank,
                                                c.claim_id,
                                                c.submitter_id AS giver_user_id,
                                                c.promotion_id,
                                                c.submission_date AS date_submitted,
--                                                rc.behavior,
                                                case when  rc.behavior = 'none'    --9/29/2014 bug fix 55111
                                                then null
                                                else  rc.behavior
                                                end as behavior,                   --9/29/2014 bug fix 55111 end
                                                rc.is_reverse,
                                                rc.submitter_comments,   -- 07/14/2017
                                                c.proxy_user_id,
                                                cia.approver_user_id AS final_approver_id,
                                                cia.approval_option_reason_type,    --03/28/2016 Bug#66208 
                                                c.node_id AS giver_node_id
                                           FROM claim c,
                                                recognition_claim rc,
                                                claim_item ci,
                                                claim_item_approver cia
                                          WHERE c.claim_id = rc.claim_id
                                            AND c.claim_id = ci.claim_id
                                            --AND rc.is_reverse <> 1
                                            AND c.is_open = 0    -- to narrow down claims that are approved
                                            AND ci.claim_item_id = cia.claim_item_id (+)
                                            AND ((c.date_created > p_in_start_date  AND c.date_created <= p_in_end_date) --04/01/2014
                                                 OR (c.date_modified  > p_in_start_date AND c.date_modified  <= p_in_end_date)
                                                 OR (ci.date_created  > p_in_start_date  AND ci.date_created <= p_in_end_date)
                                                 OR (ci.date_modified > p_in_start_date AND ci.date_modified <= p_in_end_date)
                                                 OR (cia.date_created > p_in_start_date  AND cia.date_created  <= p_in_end_date)
                                                 OR (cia.date_modified >p_in_start_date  AND cia.date_modified <= p_in_end_date))
                                       ) mc,
                                       promotion p,
                                       -- receiver tables
                                       claim_item ci_r,
                                       claim_recipient cr,
                                     --  user_address ua_r,
                                       activity a_r, -- receiver
                                       merch_order mo,
                                       application_user au,-- Added this to get the pax details as we removed non pax data from merch_order table in G5
                                       promo_merch_program_level pmpl
                                    -- restrict to most recent approval create date                              
                                 WHERE mc.approved_rank = 1
                                   AND mc.promotion_id  = p.promotion_id
                                   AND p.promotion_status IN ('live','expired')  --03/21/2012
                                   AND p.is_deleted = 0                          --03/21/2012
                                   AND mc.claim_id  = ci_r.claim_id
                                   AND ci_r.claim_item_id = cr.claim_item_id
                                   AND ci_r.claim_id = a_r.claim_id  
                                   --AND ci_r.claim_id = gawd_r.claim_id(+)  --10/22/2013
                                    -- cr.participant_id is NULL if non-pax  -- 12/04/2007
                                   AND NVL(cr.participant_id, 0) = NVL(a_r.user_id , 0)                        --Bug66208 --03/28/2016     --05/19/2016                                                       
                                   AND a_r.is_submitter = 0                  -- 05/27/2011 Bug 36422 fix      --Bug66208 --03/28/2016     --05/19/2016
                                    -- include non-pax info from merch_order 
                                   AND a_r.merch_order_id = mo.merch_order_id (+)
                                   AND mo.participant_id=au.user_id(+)
                                  -- AND au.user_id=ua_r.user_id
                                   AND mo.promo_merch_program_level_id = pmpl.promo_merch_program_level_id (+)
                                   AND ((ci_r.date_created > p_in_start_date    AND ci_r.date_created <= p_in_end_date) --04/01/2014
                                       OR (ci_r.date_modified > p_in_start_date AND ci_r.date_modified <= p_in_end_date)
                                       OR (a_r.date_created > p_in_start_date   AND a_r.date_created  <= p_in_end_date)
                                       OR (a_r.date_modified > p_in_start_date  AND a_r.date_modified <= p_in_end_date)
                                       OR (p.date_created > p_in_start_date     AND p.date_created    <= p_in_end_date)
                                       OR (p.date_modified > p_in_start_date    AND p.date_modified   <= p_in_end_date))
                                UNION 
                                   SELECT mc.*,  --06/14/2016
                                       p.award_type,
                                       p.promotion_name,
                                       cr.participant_id   AS recvr_user_id,
                                      -- ua_r.country_id     AS recvr_country_id,
                                       cr.claim_item_id    AS claim_recipient_id,
                                       cr.node_id          AS recvr_node_id,
                                       0                   AS award_amt,
                                       NULL AS trans_date,
--                                       a_r.submission_date AS date_approved,
                                       ci_r.date_approved AS date_approved,--01/23/2015
                                       NULL     AS recvr_activity_id,
                                       NULL merchandise_description,
                                       DECODE(p.award_type,
                                         'merchandise', 1, NULL) AS plateau_earned,
                                       DECODE(ci_r.approval_status_type, 
                                         'winner', 1, NULL) AS sweeptakes_won,
                                      NULL AS non_pax_first_name,
                                       NULL AS non_pax_last_name,
                                       NULL AS non_pax_full_name
                                  FROM ( -- get claim with create dates ranked in descending order
                                         -- lowest rank is the most recently approved
                                         SELECT RANK() OVER (PARTITION BY cia.claim_item_id ORDER BY cia.date_created DESC, CLAIM_ITEM_APPROVER_ID DESC) AS approved_rank,
                                                c.claim_id,
                                                c.submitter_id AS giver_user_id,
                                                c.promotion_id,
                                                c.submission_date AS date_submitted,
--                                                rc.behavior,
                                                case when  rc.behavior = 'none'    --9/29/2014 bug fix 55111
                                                then null
                                                else  rc.behavior
                                                end as behavior,                   --9/29/2014 bug fix 55111 end
                                                rc.is_reverse,
                                                rc.submitter_comments,   -- 07/14/2017
                                                c.proxy_user_id,
                                                cia.approver_user_id AS final_approver_id,
                                                cia.approval_option_reason_type,    --03/28/2016 Bug#66208 
                                                c.node_id AS giver_node_id
                                           FROM claim c,
                                                recognition_claim rc,
                                                claim_item ci,
                                                claim_item_approver cia
                                          WHERE c.claim_id = rc.claim_id
                                            AND c.claim_id = ci.claim_id
                                            --AND rc.is_reverse <> 1
                                            AND c.is_open = 0    -- to narrow down claims that are approved
                                            AND ci.claim_item_id = cia.claim_item_id (+)
                                            AND ((c.date_created > p_in_start_date  AND c.date_created <= p_in_end_date) --04/01/2014
                                                 OR (c.date_modified  > p_in_start_date AND c.date_modified  <= p_in_end_date)
                                                 OR (ci.date_created  > p_in_start_date  AND ci.date_created <= p_in_end_date)
                                                 OR (ci.date_modified > p_in_start_date AND ci.date_modified <= p_in_end_date)
                                                 OR (cia.date_created > p_in_start_date  AND cia.date_created  <= p_in_end_date)
                                                 OR (cia.date_modified >p_in_start_date  AND cia.date_modified <= p_in_end_date))
                                       ) mc,
                                       promotion p,
                                       -- receiver tables
                                       claim_item ci_r,
                                       claim_recipient cr
                                    -- restrict to most recent approval create date                              
                                 WHERE mc.approved_rank = 1
                                   AND mc.promotion_id  = p.promotion_id
                                   AND p.promotion_status IN ('live','expired')  
                                   AND p.is_deleted = 0                         
                                   AND mc.claim_id  = ci_r.claim_id
                                   AND ci_r.claim_item_id = cr.claim_item_id  
                                   AND  NOT EXISTS (SELECT 1 FROM activity a where a.claim_id = mc.claim_id AND a.is_submitter = 0)
                                   AND ((ci_r.date_created > p_in_start_date    AND ci_r.date_created <= p_in_end_date) 
                                       OR (ci_r.date_modified > p_in_start_date AND ci_r.date_modified <= p_in_end_date)
                                       OR (p.date_created > p_in_start_date     AND p.date_created    <= p_in_end_date)
                                       OR (p.date_modified > p_in_start_date    AND p.date_modified   <= p_in_end_date))
                              ) cc,
                              -- reference data tables
                              activity a_g, -- giver
                              node n_g,     -- giver
                              node n_r,     -- receiver
                              application_user au_gu,   -- giver user
                              user_address ua_gu,       
                              application_user au_ru,   -- receiver user
                              user_address ua_ru,
                              application_user au_pu,   -- proxy user
                              application_user au_fa,   -- final approver
--                              vw_cms_code_value ccv_b,  -- behavior     --01/31/2014
                              vw_curr_pax_employer cpe_g, -- giver
                              vw_curr_pax_employer cpe_r  -- receiver                                                               
                           -- outer join activity for giver data
                        WHERE cc.claim_id = a_g.claim_id (+)
                          AND 1 = a_g.is_submitter (+)
                           -- outer join node to get node names
                          AND cc.giver_node_id = n_g.node_id (+)
                          AND cc.recvr_node_id = n_r.node_id (+)
                           -- outer join user ID's to get user names and employment
                          AND cc.giver_user_id = au_gu.user_id (+)
                          AND au_gu.user_id = ua_gu.user_id
                          AND ua_gu.is_primary = 1          --03/11/2014
                          AND cc.giver_user_id = cpe_g.user_id (+)
                          AND cc.recvr_user_id = au_ru.user_id (+)
                          AND au_ru.user_id = ua_ru.user_id                 
                          AND ua_ru.is_primary = 1                          --03/11/2014
                          AND cc.recvr_user_id = cpe_r.user_id (+)                                                    
                          AND cc.proxy_user_id = au_pu.user_id (+)
                          AND cc.final_approver_id = au_fa.user_id (+)
                           -- outer join behavior to CMS behavior data
--                          AND 'picklist.promo.recognition.behavior.items' = ccv_b.asset_code (+)      --01/31/2014  
--                          AND 'en_US' = ccv_b.locale (+)               --01/31/2014
--                          AND cc.behavior = ccv_b.cms_code (+)         --01/31/2014
                          AND ((a_g.date_created > p_in_start_date    AND a_g.date_created <= p_in_end_date) --04/01/2014
                               OR (a_g.date_modified > p_in_start_date AND a_g.date_modified <= p_in_end_date)
                               OR (n_g.date_created > p_in_start_date   AND n_g.date_created  <= p_in_end_date)
                               OR (n_g.date_modified > p_in_start_date  AND n_g.date_modified <= p_in_end_date)
                               OR (n_r.date_created > p_in_start_date   AND n_r.date_created  <= p_in_end_date)
                               OR (n_r.date_modified > p_in_start_date  AND n_r.date_modified <= p_in_end_date)
                               OR (au_gu.date_created > p_in_start_date   AND au_gu.date_created  <= p_in_end_date)
                               OR (au_gu.date_modified > p_in_start_date  AND au_gu.date_modified <= p_in_end_date)
                               OR (au_ru.date_created > p_in_start_date     AND au_ru.date_created    <= p_in_end_date)
                               OR (au_ru.date_modified > p_in_start_date    AND au_ru.date_modified   <= p_in_end_date)
                               OR (au_pu.date_created > p_in_start_date     AND au_pu.date_created    <= p_in_end_date)
                               OR (au_pu.date_modified > p_in_start_date    AND au_pu.date_modified   <= p_in_end_date)
                               OR (au_fa.date_created > p_in_start_date     AND au_fa.date_created    <= p_in_end_date)
                               OR (au_fa.date_modified > p_in_start_date    AND au_fa.date_modified   <= p_in_end_date)
                               OR (cpe_g.date_created > p_in_start_date     AND cpe_g.date_created    <= p_in_end_date)
                               OR (cpe_g.date_modified > p_in_start_date    AND cpe_g.date_modified   <= p_in_end_date)
                               OR (cpe_r.date_created > p_in_start_date     AND cpe_r.date_created    <= p_in_end_date)
                               OR (cpe_r.date_modified > p_in_start_date    AND cpe_r.date_modified   <= p_in_end_date)) 
                     ) d
          )
             SELECT es.s_rowid,
                    --dds.hier_level, 0) || '-' || dds.sum_type AS record_type,                    
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.rowid AS s_rowid,
                            s2.claim_id
                       FROM rpt_recognition_detail s2                      
                   ) es
                   -- right outer join so unmatched existing summaries can be deleted
                   RIGHT OUTER JOIN  detail_recog dds
                   ON ( es.claim_id         = dds.claim_id)
       )s
       ON (d.rowid = s.s_rowid)
     WHEN MATCHED THEN
     UPDATE SET
        d.giver_user_id    =    s.giver_user_id,
        d.giver_node_id    =    s.giver_node_id,
        d.giver_country_id =    s.giver_country_id,
        d.giver_node_name  =    s.giver_node_name,
        d.giver_last_name  =    s.giver_last_name,
        d.giver_first_name =    s.giver_first_name,
        d.giver_middle_name  =    s.giver_middle_name,
        d.giver_full_name    =    s.giver_full_name,
        d.giver_pax_status   =    s. giver_pax_status,
        d.giver_department   =    s.giver_department,
        d.giver_job_position =    s.giver_job_position,
        d.giver_login_id     =    s.giver_login_id,
        d.giver_activity_id  =    s.giver_activity_id,
        d.recvr_user_id      =    s.recvr_user_id,
        d.recvr_node_id      =    s.recvr_node_id,
        d.recvr_country_id      =    s.recvr_country_id,--11/05/2014 Bug 57696
        d.recvr_node_name    =    s.recvr_node_name,
        d.recvr_last_name    =    s.recvr_last_name,
        d.recvr_first_name   =    s.recvr_first_name,
        d.recvr_middle_name  =    s.recvr_middle_name,
        d.recvr_full_name    =    s.recvr_full_name,
        d.recvr_badges_earned = s.recvr_badges_earned,--10/25/2012
        d.recvr_pax_status   =    s.recvr_pax_status,
        d.recvr_department   =    s.recvr_department,
        d.recvr_job_position =    s.recvr_job_position,
        d.recvr_login_id     =    s.recvr_login_id,
        d.recvr_activity_id  =    s.recvr_activity_id,        
        d.claim_recipient_id =    s.claim_recipient_id,
        d.promotion_id       =    s.promotion_id,
        d.promotion_name     =    s.promotion_name,
        d.behavior           =    s.behavior,
        d.award_type         =    s.award_type,
        d.recvr_plateau_earned   =    s.plateau_earned,
        d.recvr_sweepstakes_won  =    s.sweeptakes_won,
        d.trans_date         =    s.trans_date,
        d.award_amt          =    s.award_amt,
        d.merchandise_description    =    s.merchandise_description,
        d.date_submitted     =    s.date_submitted,
        d.date_approved      =    s.date_approved,
        d.final_approver_id  =    s.final_approver_id,
        d.final_approver_name =    s.final_approver_name,
        d.reason_denied       =    s.reason_denied,        --03/28/2016 Bug#66208
        d.proxy_user_id      =  s.proxy_user_id,
        d.proxy_user_name    =  s.proxy_user_name,
        d.submitter_comments = s.submitter_comments, -- 07/14/2017 
        d.date_modified      =  SYSDATE,
        d.modified_by        =  c_created_by
        WHEN NOT MATCHED THEN
   INSERT 
     (rpt_recognition_detail_id,
      giver_user_id,
      giver_node_id,
      giver_country_id,
      giver_node_name,
      giver_last_name,
      giver_first_name,
      giver_middle_name,
      giver_full_name,
      giver_pax_status,
      giver_department,
      giver_job_position,
      giver_login_id,
      giver_activity_id,
      recvr_user_id,
      recvr_node_id,
      recvr_country_id,
      recvr_node_name,
      recvr_last_name,
      recvr_first_name,
      recvr_middle_name,
      recvr_full_name,
      recvr_pax_status,
      recvr_department,
      recvr_job_position,
      recvr_login_id,
      recvr_activity_id,     
      claim_id,
      claim_recipient_id,
      promotion_id,
      promotion_name,
      behavior,
      award_type,
      trans_date,
      award_amt,
      merchandise_description,
      date_submitted,
      date_approved,
      final_approver_id,
      final_approver_name,
      reason_denied,         --03/28/2016 Bug#66208
      proxy_user_id,
      proxy_user_name,
      recvr_points,
      recvr_plateau_earned,
      recvr_sweepstakes_won,
      recvr_badges_earned,
      submitter_comments, -- 07/14/2017
      created_by,
      date_created,
      version)  
     VALUES
     ( rpt_recognition_detail_pk_sq.NEXTVAL,
        s.giver_user_id,
        s.giver_node_id,
        s.giver_country_id,
        s.giver_node_name,
        s.giver_last_name,
        s.giver_first_name,
        s.giver_middle_name,
        s.giver_full_name,
        s.giver_pax_status,
        s.giver_department,
        s.giver_job_position,
        s.giver_login_id,
        s.giver_activity_id,
        s.recvr_user_id,
        s.recvr_node_id,
        s.recvr_country_id,
        s.recvr_node_name,
        s.recvr_last_name,
        s.recvr_first_name,
        s.recvr_middle_name,
        s.recvr_full_name,
        s.recvr_pax_status,
        s.recvr_department,
        s.recvr_job_position,
        s.recvr_login_id,
        s.recvr_activity_id,
        s.claim_id,
        s.claim_recipient_id,
        s.promotion_id,
        s.promotion_name,
        s.behavior,
        s.award_type,       
        s.trans_date,
        s.award_amt,
        s.merchandise_description,
        s.date_submitted,
        s.date_approved,
        s.final_approver_id,
        s.final_approver_name,
        s.reason_denied,        --03/28/2016 Bug#66208              
        s.proxy_user_id,
        s.proxy_user_name,
        s.award_amt,
        s.plateau_earned,
        s.sweeptakes_won,
        s.recvr_badges_earned,--10/25/2012
        s.submitter_comments, -- 07/14/2017
        c_created_by,
        SYSDATE,
        0)  WHERE s.is_reverse =0 ;       
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   
   v_stage := 'Delete reversed recognitions';
   
   DELETE FROM rpt_recognition_detail
   WHERE claim_id in (select claim_id from recognition_claim where is_reverse =1);
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   
   v_stage := 'Giver rpt_recognition_detail';               
  MERGE INTO rpt_recognition_detail dtl                 --06/17/2013
  USING ( 
    SELECT rpt.ROWID row_id,
           fnc_get_user_name(au.user_id) full_name,
           au.last_name,
           au.first_name,
           au.middle_name,
           DECODE(au.is_active,1,'active','inactive') pax_status,
           vcpe.department_type,
           vcpe.position_type
      FROM application_user au,
           rpt_recognition_detail rpt,
           vw_curr_pax_employer vcpe
     WHERE au.user_id   = rpt.giver_user_id
       AND rpt.giver_user_id= vcpe.user_id(+)
       AND (p_in_start_date < au.date_created AND au.date_created <= p_in_end_date
            OR p_in_start_date < au.date_modified  AND  au.date_modified <= p_in_end_date
            OR p_in_start_date < vcpe.date_created AND vcpe.date_created <= p_in_end_date
            OR p_in_start_date < vcpe.date_modified AND vcpe.date_modified <= p_in_end_date)
       AND (au.last_name            <> rpt.giver_last_name
            OR au.first_name        <> rpt.giver_first_name
            OR au.middle_name       <> rpt.giver_middle_name
            OR DECODE(au.is_active,1,'active','inactive') <> rpt.giver_pax_status
            OR NVL(vcpe.department_type,'X') <> NVL(rpt.giver_department,'X')       --05/20/2015 Added NVL
            OR NVL(vcpe.position_type,'X')   <> NVL(rpt.giver_job_position,'X'))) e --05/20/2015 Added NVL
    ON (dtl.rowid = e.row_id)
   WHEN MATCHED THEN 
     UPDATE SET 
        dtl.giver_last_name    = e.last_name
        ,dtl.giver_first_name   = e.first_name
        ,dtl.giver_middle_name  = e.middle_name
        ,dtl.giver_full_name    = e.full_name 
        ,dtl.giver_pax_status   = e.pax_status
        ,dtl.giver_department   = e.department_type
        ,dtl.giver_job_position = e.position_type
        ,dtl.date_modified      =  SYSDATE
        ,dtl.modified_by        =  c_created_by;
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  
  v_stage := 'Receiver rpt_recognition_detail';       
  MERGE INTO RPT_RECOGNITION_DETAIL dtl             --06/17/2013
  USING ( 
    SELECT rpt.ROWID row_id,
           fnc_get_user_name(au.user_id) full_name,
           au.last_name,
           au.first_name,
           au.middle_name,
           DECODE(au.is_active,1,'active','inactive') pax_status,
           vcpe.department_type,
           vcpe.position_type
      FROM application_user au,
           rpt_recognition_detail rpt,
           vw_curr_pax_employer vcpe
     WHERE au.user_id        = rpt.recvr_user_id
       AND rpt.recvr_user_id = vcpe.user_id(+)
       AND (p_in_start_date < au.date_created       AND au.date_created <= p_in_end_date
            OR p_in_start_date < au.date_modified   AND au.date_modified <= p_in_end_date
            OR p_in_start_date < vcpe.date_created  AND vcpe.date_created <= p_in_end_date
            OR p_in_start_date < vcpe.date_modified AND vcpe.date_modified <= p_in_end_date)
       AND (au.last_name         <> rpt.recvr_last_name
         OR au.first_name        <> rpt.recvr_first_name
         OR au.middle_name       <> rpt.recvr_middle_name
         OR DECODE(au.is_active,1,'active','inactive') <> rpt.recvr_pax_status
         OR NVL(vcpe.department_type,'X') <> NVL(rpt.recvr_department,'X')  --05/20/2015 Added NVL 
         OR NVL(vcpe.position_type,'X')   <> NVL(rpt.recvr_job_position,'X'))) e --05/20/2015 Added NVL
    ON (dtl.rowid = e.row_id)
   WHEN MATCHED THEN 
     UPDATE SET 
        dtl.recvr_last_name    = e.last_name
        ,dtl.recvr_first_name   = e.first_name
        ,dtl.recvr_middle_name  = e.middle_name
        ,dtl.recvr_full_name    = e.full_name 
        ,dtl.recvr_pax_status   = e.pax_status
        ,dtl.recvr_department   = e.department_type
        ,dtl.recvr_job_position = e.position_type
        ,dtl.date_modified      = SYSDATE
        ,dtl.modified_by        = c_created_by;
        
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

--  v_stage := 'Open and Fetch cursor to pick modified node name';
   OPEN cur_node_changed;                        --06/17/2013  --01/29/2015
    FETCH cur_node_changed BULK COLLECT          --06/17/2013  --01/29/2015
     INTO v_tab_node_id,
          v_tab_node_name;
    CLOSE cur_node_changed;   

--  v_stage := 'Update modified node name for giver and receiver in rpt table';
  FORALL indx IN v_tab_node_id.FIRST .. v_tab_node_id.LAST      --06/17/2013   --01/29/2015
    UPDATE rpt_recognition_detail
       SET giver_node_name = DECODE (giver_node_id, v_tab_node_id(indx), v_tab_node_name(indx), giver_node_name),
           recvr_node_name = DECODE (recvr_node_id, v_tab_node_id(indx), v_tab_node_name(indx), recvr_node_name),
           date_modified   = SYSDATE,
           modified_by     = c_created_by,
           version         = version + 1
     WHERE (   (    giver_node_id    = v_tab_node_id(indx)
                AND giver_node_name != v_tab_node_name(indx)
                ) 
            OR (  recvr_node_id    = v_tab_node_id(indx)
                AND recvr_node_name != v_tab_node_name(indx)
                )
            );  

   v_stage := 'Success';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
   p_out_return_code := 00;
   
EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code := 99;
   p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR',p_out_error_message, NULL);
END p_rpt_recognition_detail;
/*********** nagarajs 04/21/2017 Commented out P_RPT_PURL_CONTRIBUTION_SUM since no longer summary tables used in query packages***********/
/*PROCEDURE p_rpt_recognition_summary  (p_in_requested_user_id      IN  NUMBER,
                                      p_out_return_code           OUT NUMBER,
                                      p_out_error_message         OUT VARCHAR2) IS
/*******************************************************************************

   Purpose:  Populate the recognition_summary reporting table

   ** Note **  2 fields are missing data yet.  Eligible_giver_recvr_cnt (details
               are still being worked out to populate rpt_pax_promo_eligibility.
               Actual_giver_recvr_cnt - fnc_actual_giver_recvr_count was created
               and iReports might have to populate this field.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray      11/18/2005 Initial Creation
   D Murray      12/06/2005 Added nvl to all queries for promotion_id, media_type,
                            behavior, date_approved - can not be null for Jasper
                            report parameters.
   D Murray      12/12/2005 Added pax_status, job_position and department.
   Raju N        12/25/2005 swapped the column order ( award_type and media_amt)
                            in the insert to fix the invalid number error.
   murphyc       11/05/2007 add merch_order recs
                            non-pax recs will have a "0" hier_level on the rpt table
   J Flees       08/31/2011 Report Refresh Performance.
                            Replaced hierarchy looping process with the following bulk processng SQL statements.
                             1.) Remove obsolete node records.
                             2.) Derive summary records based on the detail records.
                             3.) Insert default permutations to ensure report data can be searched from root node to all leaf nodes.
                             4.) Remove default permutation summaries that now have detail derived summaries.
   Ravi Dhanekula 05/22/2014 Fixed the issue with actual_giver_recvr_cnt in rpt_recognition_summary table.
                           06/18/2014 Added performance fix for default teamsum records.
                           06/30/2014 Added a fix for the unique constraint issue on Merge.
                           11/20/2014 removed default permutations for the reports data as we no longer need that in new reports design.
                           12/09/2014  Bug # 58560 Seperated Recognition award amount from Sweepstake award amount.
*******************************************************************************/
  /* c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_recognition_summary');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
   c_created_by         CONSTANT rpt_recognition_summary.created_by%TYPE:= p_in_requested_user_id;
      c_default_key_field_hash   CONSTANT rpt_recognition_summary.key_field_hash%TYPE :=
                               dbms_crypto.hash( utl_raw.cast_to_raw(--07/20/2016
                                           0    -- promotion_id
                                 || '/' || ' '  -- award_type
                                 || '/' || ' '  -- behavior
                                 || '/' || ' '  -- pax_status
                                 || '/' || ' '  -- job_position
                                 || '/' || ' '  -- department
                               ),2);

   

   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;

BEGIN
   v_stage := 'Start';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

   -- remove obsolete node summaries (node not found in rpt_hierarchy)
   v_stage := 'DELETE obsolete node summary records';
   DELETE
     FROM rpt_recognition_summary s
    WHERE s.detail_node_id NOT IN
          ( -- get node ID currently in the report hierarchy
            SELECT h.node_id
              FROM rpt_hierarchy h
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   ------------
   -- merge derived summary records
   v_stage := 'MERGE detail derived summary records';

  MERGE INTO rpt_recognition_summary d
   USING (
            WITH rpt_teamsum AS
            (  -- build team summary records               
               SELECT -- key fields
                      h.node_id                     AS detail_node_id,
                      'giver'                       AS giver_recvr_type,
                      NVL(d.promotion_id,0)         AS promotion_id,
                      NVL(d.award_type,' ')         AS award_type,
                      NVL(d.behavior,' ')           AS behavior,
                      NVL(d.giver_pax_status,' ')   AS pax_status,
                      NVL(d.giver_job_position,' ') AS job_position,
                      NVL(d.giver_department,' ')   AS department,
                      TRUNC(d.date_approved)        AS date_approved,
                      -- reference fields
                      h.parent_node_id              AS header_node_id,
                      h.hier_level,
                      -- count fields
                      0                                   AS eligible_giver_recvr_cnt,
                      NVL(SUM(CASE WHEN CLAIM_ID IS NULL THEN 0 ELSE d.award_amt END ),0)             AS media_amt,--12/09/2014
                      COUNT(DISTINCT d.giver_user_id)     AS actual_giver_recvr_cnt,
                      COUNT(DISTINCT d.giver_activity_id) AS recognition_cnt,
                      CAST(NULL AS NUMBER(12,0))          AS merchandise_cnt,
                      NVL(SUM(d.recvr_plateau_earned),0)    AS plateau_earned,                      
                      NVL(SUM(d.recvr_sweepstakes_won),0)   AS sweepstakes_won,
                      NVL(SUM(d.recvr_badges_earned),0)     AS badges_earned,
                      NVL(SUM(CASE WHEN CLAIM_ID IS NOT NULL THEN 0 ELSE d.award_amt END ),0)             AS sweepstake_amt         --12/09/2014             
                 FROM rpt_recognition_detail d,
                      rpt_hierarchy h
                WHERE h.node_id = d.giver_node_id
                GROUP BY h.node_id,
                      d.promotion_id,
                      d.award_type,
                      NVL(d.behavior,' '),
                     NVL(d.giver_pax_status,' '),
                      NVL(d.giver_job_position,' '),
                      NVL(d.giver_department,' '),
                      TRUNC(d.date_approved),
                      h.parent_node_id,
                      h.hier_level
                UNION ALL
               -- get receiver node hierachy details
               SELECT -- key fields
                      h.node_id                     AS detail_node_id,
                      'receiver'                    AS giver_recvr_type,
                      NVL(d.promotion_id,0)         AS promotion_id,
                      NVL(d.award_type,' ')         AS award_type,
                      NVL(d.behavior,' ') AS behavior,
                      NVL(d.recvr_pax_status,' ')   AS pax_status,
                      NVL(d.recvr_job_position,' ') AS job_position,
                      NVL(d.recvr_department,' ')   AS department,
                      TRUNC(d.date_approved)        AS date_approved,
                      -- reference fields
                      h.parent_node_id              AS header_node_id,
                      h.hier_level,
                      -- count fields
                      0                                   AS eligible_giver_recvr_cnt,
--                      NVL(SUM(d.award_amt),0)             AS media_amt,
                      NVL(SUM(CASE WHEN CLAIM_ID IS NULL THEN 0 ELSE d.award_amt END ),0)             AS media_amt,--12/09/2014
                     COUNT(DISTINCT d.recvr_user_id)                                  AS actual_giver_recvr_cnt,  --05/22/2014
                      COUNT(DISTINCT d.recvr_activity_id) AS recognition_cnt,
                      COUNT(DISTINCT
                        DECODE( d.award_type,
                           'g_merch_type', d.recvr_activity_id,
                           NULL
                        ))                                AS merchandise_cnt,
                      NVL(SUM(d.recvr_plateau_earned),0)    AS plateau_earned,
                      NVL(SUM(d.recvr_sweepstakes_won),0)   AS sweepstakes_won,
                      NVL(SUM(d.recvr_badges_earned),0)     AS badges_earned,
                      NVL(SUM(CASE WHEN CLAIM_ID IS NOT NULL THEN 0 ELSE d.award_amt END ),0)             AS sweepstake_amt       --12/09/2014
                 FROM rpt_recognition_detail d,
                      rpt_hierarchy h
                WHERE h.node_id = d.recvr_node_id
                GROUP BY h.node_id,
                      d.promotion_id,
                      d.award_type,
                      NVL(d.behavior,' '),                      
                      NVL(d.recvr_pax_status,' '),
                      NVL(d.recvr_job_position,' '),
                      NVL(d.recvr_department,' '),
                      TRUNC(d.date_approved),
                      h.parent_node_id,
                      h.hier_level                                     
            ), detail_derived_summary AS
            (  -- derive summaries based on detail data
               SELECT -- key fields
                      rt.detail_node_id,
                      'teamsum' AS sum_type,
                      rt.giver_recvr_type,
                      rt.promotion_id,
                      rt.award_type,
                      rt.behavior,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.date_approved,
                      -- reference fields
                                            dbms_crypto.hash( utl_raw.cast_to_raw(
                                  rt.promotion_id
                        || '/' || rt.award_type
                        || '/' || rt.behavior
                        || '/' || rt.pax_status
                        || '/' || rt.job_position
                        || '/' || rt.department
                      ),2) as key_field_hash,
                      rt.header_node_id,
                      rt.hier_level,
                      1 AS is_leaf,  -- team summary always a leaf node
                      -- count fields
                      rt.eligible_giver_recvr_cnt,
                      rt.media_amt,
                      rt.actual_giver_recvr_cnt,
                      rt.recognition_cnt,
                      rt.merchandise_cnt,
                      rt.plateau_earned,
                      rt.sweepstakes_won,
                      rt.sweepstake_amt--12/09/2014
                    --  rt.badges_earned  
                 FROM rpt_teamsum rt
                UNION ALL
               SELECT -- key fields
                      h.node_id AS detail_node_id,
                      'nodesum' AS sum_type,
                      rt.giver_recvr_type,
                      rt.promotion_id,
                      rt.award_type,
                      rt.behavior,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.date_approved,
                      -- reference fields
                                            dbms_crypto.hash( utl_raw.cast_to_raw(--07/20/2016
                                  rt.promotion_id
                        || '/' || rt.award_type
                        || '/' || rt.behavior
                        || '/' || rt.pax_status
                        || '/' || rt.job_position
                        || '/' || rt.department
                      ),2) as key_field_hash,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      NVL(h.is_leaf, 1) AS is_leaf,
                      -- count fields
                      SUM(rt.eligible_giver_recvr_cnt) AS eligible_giver_recvr_cnt,
                      SUM(rt.media_amt)                AS media_amt,
                      SUM(rt.actual_giver_recvr_cnt)   AS actual_giver_recvr_cnt,
                      SUM(rt.recognition_cnt)          AS recognition_cnt,
                      SUM(rt.merchandise_cnt)          AS merchandise_cnt,
                      SUM(rt.plateau_earned)           AS plateau_earned,
                      SUM(rt.sweepstakes_won)          AS sweepstakes_won,
                      SUM(rt.sweepstake_amt)                AS sweepstake_amt      --12/09/2014                
                   --   SUM(rt.badges_earned)            AS badges_earned                         
                 FROM ( -- associate each node to all its hierarchy nodes
                        SELECT np.node_id,
                               p.column_value AS path_node_id
                          FROM ( -- get node hierarchy path
                                 SELECT h.node_id,
                                        h.hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                   FROM rpt_hierarchy h
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
                               ) AS sys.odcinumberlist ) ) p
                      ) npn,
                      rpt_hierarchy h,
                      rpt_teamsum rt
                   -- recognition count restriction does not apply to non-pax merch
                WHERE (  rt.detail_node_id IS NULL -- non-pax
                      OR rt.recognition_cnt != 0
                      )
                   -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  AND rt.detail_node_id = npn.node_id (+)
                  AND npn.path_node_id = h.node_id (+)
                GROUP BY h.node_id,
                      rt.giver_recvr_type,
                      rt.promotion_id,
                      rt.award_type,
                      rt.behavior,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.date_approved,
                      h.parent_node_id,
                      h.hier_level,
                      h.is_leaf
            ) -- end detail_derived_summary
            -- compare existing summary records with detail derived summaries
            SELECT es.s_rowid,
                   NVL(dds.hier_level, 0) || '-' || dds.sum_type AS record_type,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.detail_node_id,
                            s2.sum_type,
                            s2.giver_recvr_type,
                            s2.date_approved,
                            s2.key_field_hash
                       FROM rpt_recognition_summary s2
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                   FULL OUTER JOIN detail_derived_summary dds
                   ON (   es.detail_node_id = dds.detail_node_id
                      AND es.sum_type         = dds.sum_type
                      AND es.giver_recvr_type = dds.giver_recvr_type
                      AND NVL(es.date_approved,SYSDATE)    = NVL(dds.date_approved,SYSDATE) --06/30/2014
                    AND es.key_field_hash   = dds.key_field_hash
                      )
         ) s
      ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
      UPDATE SET
         d.header_node_id           = s.header_node_id,
         d.hier_level               = s.hier_level,
         d.is_leaf                  = s.is_leaf,
         d.eligible_giver_recvr_cnt = s.eligible_giver_recvr_cnt,
         d.media_amt                = s.media_amt,
         d.actual_giver_recvr_cnt   = s.actual_giver_recvr_cnt,
         d.recognition_cnt          = s.recognition_cnt,
         d.merchandise_cnt          = s.merchandise_cnt,
         d.plateau_earned           = s.plateau_earned,
         d.sweepstakes_won          = s.sweepstakes_won,
         d.sweepstake_amt                = s.sweepstake_amt,
       --  d.badges_earned            = s.badges_earned,
         d.modified_by              = c_created_by,
         d.date_modified            = SYSDATE
       WHERE ( -- only update records with different values
                DECODE(d.header_node_id,           s.header_node_id,           1, 0) = 0
             OR DECODE(d.hier_level,               s.hier_level,               1, 0) = 0
             OR DECODE(d.is_leaf,                  s.is_leaf,                  1, 0) = 0
             OR DECODE(d.eligible_giver_recvr_cnt, s.eligible_giver_recvr_cnt, 1, 0) = 0
             OR DECODE(d.media_amt,                s.media_amt,                1, 0) = 0
             OR DECODE(d.actual_giver_recvr_cnt,   s.actual_giver_recvr_cnt,   1, 0) = 0
             OR DECODE(d.recognition_cnt,          s.recognition_cnt,          1, 0) = 0
             OR DECODE(d.merchandise_cnt,          s.merchandise_cnt,          1, 0) = 0
             OR DECODE(d.plateau_earned,           s.plateau_earned,           1, 0) = 0
             OR DECODE(d.sweepstakes_won,          s.sweepstakes_won,          1, 0) = 0
              OR DECODE(d.sweepstake_amt,                s.sweepstake_amt,                1, 0) = 0--12/09/2014
--             OR DECODE(d.badges_earned,            s.badges_earned,            1, 0) = 0
             )
      -- remove existing summaries that no longer have product details
      DELETE
       WHERE s.detail_node_id IS NULL
    WHEN NOT MATCHED THEN
      INSERT
      (  rpt_recognition_summary_id,
         -- key fields
         detail_node_id,
         sum_type,         
         giver_recvr_type,
         promotion_id,
         award_type,
         behavior,
         pax_status,
         job_position,
         department,
         date_approved,
         -- reference fields
         key_field_hash,
         header_node_id,
         hier_level,
         is_leaf,
         record_type,
         -- count fields
         eligible_giver_recvr_cnt,
         media_amt,
         actual_giver_recvr_cnt,
         recognition_cnt,
         merchandise_cnt,
         plateau_earned,
         sweepstakes_won,
         sweepstake_amt,--12/09/2014
     --    badges_earned,  
         -- audit fields
         created_by,
         date_created,
         modified_by,
         date_modified
      )
      VALUES
      (  fnc_get_pk_nextval('RPT_RECOGNITION_SUMMARY_PK_SQ'),
         -- key fields
         s.detail_node_id,
         s.sum_type,        
         s.giver_recvr_type,
         s.promotion_id,
         s.award_type,
         s.behavior,
         s.pax_status,
         s.job_position,
         s.department,
         s.date_approved,
         s.key_field_hash,
         s.header_node_id,
         s.hier_level,
         s.is_leaf,
          s.record_type,
         -- count fields
         s.eligible_giver_recvr_cnt,
         s.media_amt,
         s.actual_giver_recvr_cnt,
         s.recognition_cnt,
         s.merchandise_cnt,
         s.plateau_earned,
         s .sweepstakes_won,
         s.sweepstake_amt, --12/09/2014
--         s.badges_earned,
         -- audit fields
         c_created_by,
         SYSDATE,
         NULL,
         NULL
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

/* --11/20/2014 Start
-- 04/11/2013 Begin
   v_stage := 'MERGE default summary permutations';
   MERGE INTO rpt_recognition_summary d
   USING (  -- associate default records with report hierarchy
            SELECT s.ROWID AS s_rowid,
                   h.hier_level || '-' || SUBSTR(s.record_type, -7) AS record_type,
                   h.parent_node_id AS header_node_id,
                   h.hier_level,
                   h.is_leaf
              FROM rpt_recognition_summary s,
                   rpt_hierarchy h
                -- default permutations have no approved date
             WHERE s.date_approved IS NULL
               AND s.detail_node_id = h.node_id
         ) s
      ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
      UPDATE SET
         d.record_type              = s.record_type,
         d.header_node_id           = s.header_node_id,
         d.hier_level               = s.hier_level,
         d.is_leaf                  = s.is_leaf,
         d.modified_by              = c_created_by,
         d.date_modified            = SYSDATE
       WHERE ( -- only update records with different values
                d.record_type != s.record_type
             OR DECODE(d.header_node_id,           s.header_node_id,           1, 0) = 0
             OR DECODE(d.hier_level,               s.hier_level,               1, 0) = 0
             OR DECODE(d.is_leaf,                  s.is_leaf,                  1, 0) = 0
             );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   -- 04/11/2013 End
   ------------
   -- add missing default summary permutations
   v_stage := 'INSERT missing default team summary permutations';
   INSERT INTO rpt_recognition_summary
   (  rpt_recognition_summary_id,
      -- key fields
      detail_node_id,
            sum_type,
      giver_recvr_type,
      promotion_id,
      award_type,
      behavior,
      pax_status,
      job_position,
      department,
      date_approved,
      -- reference fields
      key_field_hash,
      header_node_id,
      hier_level,
      is_leaf,
      record_type,
      -- count fields
      eligible_giver_recvr_cnt,
      media_amt,
      actual_giver_recvr_cnt,
      recognition_cnt,
      merchandise_cnt,
      plateau_earned,
      sweepstakes_won,
      badges_earned,  
      -- audit fields
      created_by,
      date_created,
      modified_by,
      date_modified
   )
   (  -- find missing default permutations
      SELECT RPT_RECOGNITION_SUMMARY_PK_SQ.nextval,
             -- key fields
             nsp.detail_node_id,
            nsp.sum_type,
             grt.giver_recvr_type,
             0    AS promotion_id,
             ' '  AS award_type,
             ' '  AS behavior,
             ' '  AS pax_status,
             NULL  AS job_position,
             NULL  AS department,
             NULL AS date_approved,
             -- reference fields
             c_default_key_field_hash AS key_field_hash,
             nsp.header_node_id,
             nsp.hier_level,
             nsp.is_leaf,
             nsp.record_type,
             -- count fields
             0 AS eligible_giver_recvr_cnt,
             0 AS media_amt,
             0 AS actual_giver_recvr_cnt,
             0 AS recognition_cnt,
             0 AS merchandise_cnt,
             0 AS plateau_earned,
             0 AS sweepstakes_won,
             0 AS badges_earned,  
             -- audit fields
             c_created_by AS created_by,
             SYSDATE AS date_created,
             NULL,
             NULL
        FROM ( -- get all possible node summary type permutations
               SELECT h.node_id AS detail_node_id,
                      'teamsum' AS sum_type,
                      h.hier_level || '-' || 'teamsum' AS record_type,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      1 AS is_leaf   -- team summary always a leaf node
                 FROM rpt_hierarchy h
             ) nsp,
             ( -- all possible giver/receiver types
               SELECT DECODE(LEVEL,
                        1, 'giver',
                        2, 'receiver'
                      ) AS giver_recvr_type
                 FROM dual
              CONNECT BY LEVEL <= 2
             ) grt
          -- cartesian join node permutations to giver/receiver types
          -- exclude default permutation when a matching summary record exists
        WHERE NOT EXISTS
             ( SELECT 1
                 FROM rpt_recognition_summary s
                WHERE s.detail_node_id = nsp.detail_node_id
                   AND s.sum_type = nsp.sum_type
                  AND s.giver_recvr_type = grt.giver_recvr_type
                   -- any approved date value matches on team summary records because detail derived summaries replace default team permutations
             )
   );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   v_stage := 'INSERT missing default node summary permutations';
   INSERT INTO rpt_recognition_summary
   (  rpt_recognition_summary_id,
      -- key fields
      detail_node_id,
      sum_type,
      giver_recvr_type,
      promotion_id,
      award_type,
      behavior,
      pax_status,
      job_position,
      department,
      date_approved,
      -- reference fields
      key_field_hash,
      header_node_id,
      hier_level,
      is_leaf,
      record_type,
      -- count fields
      eligible_giver_recvr_cnt,
      media_amt,
      actual_giver_recvr_cnt,
      recognition_cnt,
      merchandise_cnt,
      plateau_earned,
      sweepstakes_won,
      badges_earned,  
      -- audit fields
      created_by,
      date_created,
      modified_by,
      date_modified
   )
   (  -- find missing default permutations
      SELECT RPT_RECOGNITION_SUMMARY_PK_SQ.NEXTVAL,
             -- key fields
             nsp.detail_node_id,
             nsp.sum_type,
             grt.giver_recvr_type,
             0    AS promotion_id,
             ' '  AS award_type,
             ' '  AS behavior,
             ' '  AS pax_status,
             NULL  AS job_position,
             NULL  AS department,
             NULL AS date_approved,
             -- reference fields
             c_default_key_field_hash AS key_field_hash,
             nsp.header_node_id,
             nsp.hier_level,
             nsp.is_leaf,
             nsp.record_type,
             -- count fields
             0 AS eligible_giver_recvr_cnt,
             0 AS media_amt,
             0 AS actual_giver_recvr_cnt,
             0 AS recognition_cnt,
             0 AS merchandise_cnt,
             0 AS plateau_earned,
             0 AS sweepstakes_won,
             0 AS badges_earned,  
             -- audit fields
             c_created_by AS created_by,
             SYSDATE AS date_created,
             NULL,
             NULL
        FROM ( -- get all possible node summary type permutations
               SELECT h.node_id AS detail_node_id,
                      'nodesum' AS sum_type,
                      h.hier_level || '-' || 'nodesum' AS record_type,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      h.is_leaf
                 FROM rpt_hierarchy h
             ) nsp,
             ( -- all possible giver/receiver types
               SELECT DECODE(LEVEL,
                        1, 'giver',
                        2, 'receiver'
                      ) AS giver_recvr_type
                 FROM dual
              CONNECT BY LEVEL <= 2
             ) grt
          -- cartesian join node permutations to giver/receiver types
          -- exclude default permutation when a matching summary record exists
        WHERE NOT EXISTS
             ( SELECT 1
                 FROM rpt_recognition_summary s
                WHERE s.detail_node_id   = nsp.detail_node_id
                  AND s.sum_type         = nsp.sum_type
                  AND s.giver_recvr_type = grt.giver_recvr_type
                  AND s.date_approved IS NULL
             )
          -- default node summary permutation must have default team summary permutation in its hierarchy
         AND nsp.detail_node_id IN --06/18/2014
             ( -- get team permutations under node permutation
               SELECT tp.detail_node_id
                 FROM rpt_recognition_summary tp
                WHERE tp.sum_type         = 'teamsum'
                  AND tp.giver_recvr_type        = grt.giver_recvr_type
                  AND tp.date_approved IS NULL
                UNION ALL
               -- get team permutations under node permutation hierarchy
               SELECT tp.header_node_id
                 FROM rpt_recognition_summary tp
                   -- start with child node immediately under current node
                START WITH tp.sum_type         = 'teamsum'
                       AND tp.giver_recvr_type        = grt.giver_recvr_type
                       AND tp.date_approved IS NULL
                CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                        AND PRIOR tp.sum_type         = tp.sum_type
                       AND PRIOR tp.giver_recvr_type        = tp.giver_recvr_type
                       AND tp.date_approved IS NULL
             )
   );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   ------------
   -- remove team permutations when an associated detail derived summary exists
   v_stage := 'DELETE obsolete team permutations';
   DELETE rpt_recognition_summary
    WHERE ROWID IN
          ( -- get default team permutation with a corresponding detail derived team summary
            SELECT DISTINCT
                   s.ROWID
              FROM rpt_recognition_summary s,
                   rpt_recognition_summary dd
                -- substr matches functional index
                          WHERE dd.sum_type = 'teamsum'
                -- detail derived summaries have an approved date
               AND dd.date_approved IS NOT NULL
               AND dd.detail_node_id          = s.detail_node_id
               AND dd.sum_type         = s.sum_type
               AND dd.giver_recvr_type        = s.giver_recvr_type
                -- default permutations have no approved date
               AND s.date_approved IS NULL
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- delete node permutation with no associated team permutation
   v_stage := 'DELETE obsolete node permutations with no associated team permutation';
   DELETE rpt_recognition_summary
    WHERE ROWID IN
          ( SELECT np.ROWID
              FROM rpt_recognition_summary np
                -- join detail derived node summary to node permutation
               WHERE np.sum_type = 'nodesum'
               AND np.date_approved IS NULL
                -- node permutation has no team permutation
               AND NOT EXISTS
                   ( -- get team permutations under node permutation
                     SELECT 1
                       FROM rpt_recognition_summary tp
                      WHERE tp.detail_node_id          = np.detail_node_id
                        AND tp.sum_type         = 'teamsum'
                        AND tp.giver_recvr_type        = np.giver_recvr_type
                        AND tp.date_approved IS NULL
                      UNION ALL
                     -- get team permutations under node permutation hierarchy
                     SELECT 1
                       FROM rpt_recognition_summary tp
                         -- start with child node immediately under current node
                      START WITH tp.header_node_id          = np.detail_node_id
                             AND tp.sum_type         = 'teamsum'
                             AND tp.giver_recvr_type        = np.giver_recvr_type
                             AND tp.date_approved IS NULL
                      CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                              AND PRIOR tp.sum_type         = tp.sum_type
                             AND PRIOR tp.giver_recvr_type        = tp.giver_recvr_type
                             AND tp.date_approved IS NULL
                   )
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   */ --11/20/2014 End

   /*v_stage := 'Success';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
   
   p_out_return_code := 00;

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ': ' || SQLCODE || ', ' || SQLERRM, NULL);
      p_out_return_code := 99;      
      p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;

END p_rpt_recognition_summary;*/

PROCEDURE P_INSERT_RPT_RECOGNITION_DTL
  (p_recognition_dtl_rec IN RPT_RECOGNITION_DETAIL%ROWTYPE) IS
/*******************************************************************************

   Purpose:  Insert record into rpt_recognition_detail reporting table

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/16/2005  Initial Creation
   D Murray    12/15/2005  Added nvl to promotion_id, award_type, behavior, pax_status,
                           job_position, and department - can not be null for Jasper
                           report parameters.
   murphyc     10/29/2007  add merchandise_description
*******************************************************************************/

BEGIN

 INSERT INTO rpt_recognition_detail
      (rpt_recognition_detail_id, giver_user_id, giver_node_id,
       giver_node_name, giver_last_name, giver_first_name,
       giver_middle_name, giver_full_name, giver_pax_status, giver_department,
       giver_job_position, giver_login_id, giver_activity_id,
       recvr_user_id, recvr_node_id, recvr_node_name,
       recvr_last_name, recvr_first_name, recvr_middle_name,
       recvr_full_name, recvr_pax_status, recvr_department, recvr_job_position,
       recvr_login_id, recvr_activity_id, claim_id, claim_recipient_id,
       promotion_id, promotion_name, behavior, award_type, trans_date,
       award_amt, date_submitted, date_approved, final_approver_id,
       final_approver_name,proxy_user_id,proxy_user_name,
       merchandise_description)
 VALUES
      (p_recognition_dtl_rec.rpt_recognition_detail_id, p_recognition_dtl_rec.giver_user_id,
       p_recognition_dtl_rec.giver_node_id, p_recognition_dtl_rec.giver_node_name,
       p_recognition_dtl_rec.giver_last_name, p_recognition_dtl_rec.giver_first_name,
       p_recognition_dtl_rec.giver_middle_name, p_recognition_dtl_rec.giver_full_name,
       NVL(p_recognition_dtl_rec.giver_pax_status,' '), NVL(p_recognition_dtl_rec.giver_department,' '),
       NVL(p_recognition_dtl_rec.giver_job_position,' '), p_recognition_dtl_rec.giver_login_id,
       p_recognition_dtl_rec.giver_activity_id, p_recognition_dtl_rec.recvr_user_id,
       p_recognition_dtl_rec.recvr_node_id, p_recognition_dtl_rec.recvr_node_name,
       p_recognition_dtl_rec.recvr_last_name, p_recognition_dtl_rec.recvr_first_name,
       p_recognition_dtl_rec.recvr_middle_name, p_recognition_dtl_rec.recvr_full_name,
       NVL(p_recognition_dtl_rec.recvr_pax_status,' '), NVL(p_recognition_dtl_rec.recvr_department,' '),
       NVL(p_recognition_dtl_rec.recvr_job_position,' '), p_recognition_dtl_rec.recvr_login_id,
       p_recognition_dtl_rec.recvr_activity_id, p_recognition_dtl_rec.claim_id,
       p_recognition_dtl_rec.claim_recipient_id, NVL(p_recognition_dtl_rec.promotion_id,0),
       p_recognition_dtl_rec.promotion_name, NVL(p_recognition_dtl_rec.behavior,' '),
       NVL(p_recognition_dtl_rec.award_type,' '), p_recognition_dtl_rec.trans_date,
       p_recognition_dtl_rec.award_amt, p_recognition_dtl_rec.date_submitted,
       p_recognition_dtl_rec.date_approved, p_recognition_dtl_rec.final_approver_id,
       p_recognition_dtl_rec.final_approver_name,p_recognition_dtl_rec.proxy_user_id,
       p_recognition_dtl_rec.proxy_user_name,
       p_recognition_dtl_rec.merchandise_description);
EXCEPTION
   WHEN OTHERS THEN
    prc_execution_log_entry('P_INSERT_RPT_RECOGNITION_DTL',1,'ERROR',SQLERRM,null);
END; -- P_INSERT_RPT_RECOGNITION_DTL
--------------------------------------------------------------------------------

FUNCTION FNC_ELIGIBILITY_COUNT
  ( p_in_promotion_id IN VARCHAR2,
    p_in_node_id      IN VARCHAR2,
    p_in_giver_recvr  IN VARCHAR2,
    p_in_record_type  IN VARCHAR2,
    p_in_promotion_status  IN VARCHAR2, --09/21/2012
    p_in_pax_status   IN VARCHAR2,
    p_in_job_code     IN VARCHAR2,
    p_in_department   IN VARCHAR2,
    p_in_promo_type   IN VARCHAR2 DEFAULT 'recognition')
  RETURN  NUMBER IS
/*******************************************************************************

   Purpose:  Get the number of eligible givers or eligible receivers for a given
             promotion and a given node from the rpt_pax_promo_eligibility table.

   Person          Date       Comments
   -----------     ---------- -----------------------------------------------------
   D Murray        11/21/2005 Initial Creation
   Rachel Robinson 02/02/2006 Changed to be able to accept a null input parameter
                              for participant status
   Raju N          06/01/2006 join condtion for pax and pax emplyer to avoid the
                              cartersian product. Added to logic to pick up the most
                              recent employment record by participant_employer_index.
   Raju N          02/08/2007 extra code to handle the existence of an employer records.
                              basically ignore the participant_employer join when
                              the job code and dept parms are null.
                              ** There might be performance overhead and no benchmarking
                                 was done to assess the impact
                              *** This should be considered to be moved to common pkg.
   Chidamba      09/21/2012  Changes for G5 reports
*******************************************************************************/
   v_eligible_cnt     NUMBER;

BEGIN
  IF p_in_giver_recvr = 'giver' THEN
   IF p_in_record_type = 'team' THEN -- team or node
     -- check to see if we have to join the particpant employer records.
     IF p_in_job_code IS NULL AND p_in_department IS NULL THEN  
       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM RPT_PAX_PROMO_ELIGIBILITY r,
                   PARTICIPANT p,
                   promotion pr           --09/21/2012              
        WHERE  ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id))))) --change made to accept multiple selections  
          AND pr.promotion_id = r.promotion_id                                   --09/21/2012
          AND pr.promotion_status = NVL(p_in_promotion_status,promotion_status)  --09/21/2012
          AND r.node_id = p_in_node_id
          AND r.giver_recvr_type = p_in_giver_recvr
          AND p.user_id = r.participant_id
            AND p.status = NVL(p_in_pax_status,p.status)
          AND r.promotion_id IN ( SELECT promotion_id FROM PROMOTION WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     ELSE 
       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM RPT_PAX_PROMO_ELIGIBILITY r,
                   PARTICIPANT p,
                   promotion pr           --09/21/2012  
        WHERE  ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections
        --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id)))   
          AND pr.promotion_id = r.promotion_id                                   --09/21/2012
          AND pr.promotion_status = NVL(p_in_promotion_status,promotion_status)  --09/21/2012
          AND r.node_id = p_in_node_id
          AND r.giver_recvr_type = p_in_giver_recvr
          AND p.user_id = r.participant_id
            AND p.status = NVL(p_in_pax_status,p.status)
          AND participant_id IN (SELECT pe.user_id
                                 FROM PARTICIPANT_EMPLOYER pe
                                WHERE NVL(pe.position_type,'job')    = NVL(p_in_job_code,NVL(pe.position_type,'job')) AND
                                ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND pe.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_department)))))   --change made to accept multiple selections
                                        AND pe.participant_employer_index =
                                     (SELECT max(pe1.participant_employer_index)
                                        FROM participant_employer pe1
                                       WHERE pe1.user_id = pe.user_id))
        AND r.promotion_id IN ( SELECT promotion_id FROM PROMOTION WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     END IF ;
   ELSE -- team or node
     -- check to see if we have to join the particpant employer records.
     IF p_in_job_code IS NULL AND p_in_department IS NULL THEN  
       -- ignore the pax employer record
       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM RPT_PAX_PROMO_ELIGIBILITY r,
                   PARTICIPANT p,
                   promotion pr           --09/21/2012  
        WHERE ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections
          AND pr.promotion_id = r.promotion_id                                   --09/21/2012
          AND pr.promotion_status = NVL(p_in_promotion_status,promotion_status)  --09/21/2012
          AND r.node_id  IN
               (SELECT node_id
                  FROM RPT_HIERARCHY
               CONNECT BY PRIOR node_id = parent_node_id
                 START WITH node_id IN (SELECT *
                                 FROM TABLE (
                                         get_array_varchar (
                                           NVL (p_in_node_id,node_id)))))
          AND r.giver_recvr_type = p_in_giver_recvr
          AND p.user_id = r.participant_id
            AND p.status = NVL(p_in_pax_status,p.status)
          AND r.promotion_id IN ( SELECT promotion_id FROM PROMOTION WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     ELSE 
       -- include pax employer record
       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM RPT_PAX_PROMO_ELIGIBILITY r,
                  PARTICIPANT p,
                  promotion pr           --09/21/2012  
        WHERE --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id)))
         ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections 
          AND pr.promotion_id = r.promotion_id                                   --09/21/2012
          AND pr.promotion_status = NVL(p_in_promotion_status,promotion_status)  --09/21/2012
          AND r.node_id IN
               (SELECT node_id
                  FROM RPT_HIERARCHY
               CONNECT BY PRIOR node_id = parent_node_id
                 START WITH node_id IN (SELECT *
                                 FROM TABLE (
                                         get_array_varchar (
                                           NVL (p_in_node_id,node_id)))))
          AND r.giver_recvr_type = p_in_giver_recvr
          AND p.user_id = r.participant_id
            AND p.status = NVL(p_in_pax_status,p.status)        
          AND participant_id IN (SELECT pe.user_id
                                   FROM PARTICIPANT_EMPLOYER pe
                                  WHERE NVL(pe.position_type,'job')    = NVL(p_in_job_code,NVL(pe.position_type,'job'))
                                    AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND pe.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) 
                                    AND pe.participant_employer_index =
                                       (SELECT max(pe1.participant_employer_index)
                                          FROM participant_employer pe1
                                         WHERE pe1.user_id = pe.user_id))
        AND r.promotion_id IN ( SELECT promotion_id FROM PROMOTION WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     END IF;   
   END IF ; -- team or node
  ELSIF p_in_giver_recvr = 'receiver' THEN              --09/21/2012
   IF p_in_record_type = 'team' THEN -- team or node
     -- check to see if we have to join the particpant employer records.
     IF p_in_job_code IS NULL  THEN  
       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM RPT_PAX_PROMO_ELIGIBILITY r,
                   PARTICIPANT p,
                   promotion pr           --09/21/2012              
        WHERE --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id)))
         ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections 
          AND pr.promotion_id = r.promotion_id                                   
          AND pr.promotion_status = NVL(p_in_promotion_status,promotion_status) 
          AND r.node_id = p_in_node_id
          AND r.giver_recvr_type = p_in_giver_recvr
          AND p.user_id = r.participant_id
            AND p.status = NVL(p_in_pax_status,p.status)
          AND r.promotion_id IN ( SELECT promotion_id FROM PROMOTION WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     ELSE 
       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM RPT_PAX_PROMO_ELIGIBILITY r,
                   PARTICIPANT p,
                   promotion pr          
        WHERE --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id)))
         ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections 
          AND pr.promotion_id = r.promotion_id                                   
          AND pr.promotion_status = NVL(p_in_promotion_status,promotion_status) 
          AND r.node_id = p_in_node_id
          AND r.giver_recvr_type = p_in_giver_recvr
          AND p.user_id = r.participant_id
            AND p.status = NVL(p_in_pax_status,p.status)
          AND participant_id IN (SELECT un.user_id
                                 FROM user_node un
                                WHERE un.role    = NVL(p_in_job_code,un.role))
        AND r.promotion_id IN ( SELECT promotion_id FROM PROMOTION WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     END IF ;
   ELSE -- team or node
     -- check to see if we have to join the particpant employer records.
     IF p_in_job_code IS NULL AND p_in_department IS NULL THEN  
       -- ignore the pax employer record
       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM RPT_PAX_PROMO_ELIGIBILITY r,
                   PARTICIPANT p,
                   promotion pr           
        WHERE --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id)))
         ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections 
          AND pr.promotion_id = r.promotion_id                                   
          AND pr.promotion_status = NVL(p_in_promotion_status,promotion_status)  
          AND r.node_id  IN
              (SELECT node_id
                  FROM RPT_HIERARCHY
               CONNECT BY PRIOR node_id = parent_node_id
                 START WITH node_id IN (SELECT *
                                 FROM TABLE (
                                         get_array_varchar (
                                           NVL (p_in_node_id,node_id)))))
          AND r.giver_recvr_type = p_in_giver_recvr
          AND p.user_id = r.participant_id
            AND p.status = NVL(p_in_pax_status,p.status)
          AND r.promotion_id IN ( SELECT promotion_id FROM PROMOTION WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     ELSE 
       -- include pax employer record
       SELECT COUNT(DISTINCT participant_id)
         INTO v_eligible_cnt
         FROM RPT_PAX_PROMO_ELIGIBILITY r,
                  PARTICIPANT p,
                  promotion pr          
        WHERE --r.promotion_id = NVL(p_in_promotion_id,r.promotion_id)
         ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections
          AND pr.promotion_id = r.promotion_id                                   
          AND pr.promotion_status = NVL(p_in_promotion_status,promotion_status) 
          AND r.node_id IN
               (SELECT node_id
                  FROM RPT_HIERARCHY
               CONNECT BY PRIOR node_id = parent_node_id
                 START WITH node_id IN (SELECT *
                                 FROM TABLE (
                                         get_array_varchar (
                                           NVL (p_in_node_id,node_id)))))
          AND r.giver_recvr_type = p_in_giver_recvr
          AND p.user_id = r.participant_id
            AND p.status = NVL(p_in_pax_status,p.status)        
          AND participant_id IN (SELECT un.user_id
                                 FROM user_node un
                                WHERE un.role    = NVL(p_in_job_code,un.role))
        AND r.promotion_id IN ( SELECT promotion_id FROM PROMOTION WHERE promotion_type = NVL(p_in_promo_type,promotion_type));
     END IF;   
   END IF ; -- team or node
   
  END IF;
   RETURN v_eligible_cnt;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry('FNC_ELIGIBILITY_COUNT',1,'ERROR','promo: '||p_in_promotion_id||' node: '||p_in_node_id||'  '||
                            p_in_giver_recvr||'  '||SQLERRM,null);
       v_eligible_cnt := 0;
       RETURN v_eligible_cnt;
END; -- FNC_ELIGIBILITY_COUNT
--------------------------------------------------------------------------------

FUNCTION FNC_ACTUAL_GIVER_RECVR_COUNT
  ( p_in_node_id IN VARCHAR2,
    p_in_promotion_id IN VARCHAR2,
    p_in_award_type IN VARCHAR2,
    p_in_behavior IN VARCHAR2,
    p_in_promotion_status IN VARCHAR2,  --09/21/2012
--    p_in_date_approved IN DATE,
    p_in_from_date       IN DATE,
    p_in_to_date       IN DATE,
    p_in_giver_recvr_type IN VARCHAR2,
    p_in_record_type IN VARCHAR2,
    p_in_pax_status   IN VARCHAR2,
    p_in_job_code     IN VARCHAR2,
    p_in_department   IN VARCHAR2  )
  RETURN  NUMBER IS
/*******************************************************************************

   Purpose:  Get the number of actual givers of at least one recognition or
             actual receivers of at least one recognition for a given node and
             possibly by promotion_id, award_type, behavior and date_approved
             if those parameters are chosen in the recognition summary report.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   D Murray    11/23/2005  Initial Creation
   Chidamba      09/21/2012  Changes for G5 reports
*******************************************************************************/
   v_actual_cnt     NUMBER;

BEGIN

  IF p_in_giver_recvr_type = 'giver' THEN
     IF p_in_record_type = 'team' THEN
       SELECT COUNT( DISTINCT giver_user_id)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL r,
              promotion p                                                       --09/21/2012
        WHERE giver_node_id = p_in_node_id
          AND r.claim_id is not null
          AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections 
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
--          AND date_approved = NVL(p_in_date_approved,date_approved)
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND giver_pax_status = NVL(p_in_pax_status,giver_pax_status)
          AND NVL(giver_job_position,'job') = NVL(p_in_job_code,NVL(giver_job_position,'job'))
--          AND giver_department = NVL(p_in_department,giver_department)
          AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND giver_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;

     ELSE
       SELECT COUNT(DISTINCT giver_user_id)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL r,
              promotion p                                                     --09/21/2012
        WHERE giver_node_id IN
               (SELECT node_id
                  FROM RPT_HIERARCHY
               CONNECT BY PRIOR node_id = parent_node_id
                 START WITH node_id IN (SELECT *
                                 FROM TABLE (
                                         get_array_varchar (
                                           NVL (p_in_node_id,node_id)))))
               AND r.claim_id is not null
          AND --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id))) 
           ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
--          AND date_approved = NVL(p_in_date_approved,date_approved)
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND giver_pax_status = NVL(p_in_pax_status,giver_pax_status)
          AND NVL(giver_job_position,'job') = NVL(p_in_job_code,NVL(giver_job_position,'job'))
          AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND giver_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;

     END IF ;
  ELSIF p_in_giver_recvr_type = 'receiver' THEN
     IF p_in_record_type = 'team' THEN
       SELECT COUNT( DISTINCT recvr_user_id)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL r,
              USER_NODE u,                                 --09/21/2012
              promotion p                                  --09/21/2012
        WHERE recvr_node_id = p_in_node_id
          AND r.claim_id is not null
          AND r.recvr_user_id = u.user_id                       --09/21/2012
          and r.recvr_node_id = u.node_id                       --09/21/2012
          AND u.role = NVL(p_in_job_code,role)            --09/21/2012
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id)))
           ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections 
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
--          AND date_approved = NVL(p_in_date_approved,date_approved)
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND recvr_pax_status = NVL(p_in_pax_status,recvr_pax_status)
          --AND recvr_job_position = NVL(p_in_job_code,recvr_job_position)  --09/21/2012
--          AND recvr_department = NVL(p_in_department,recvr_department);
          AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND recvr_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;

     ELSE
       SELECT COUNT( DISTINCT recvr_user_id)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL r,
              USER_NODE u,                                 --09/21/2012
              promotion p                                  --09/21/2012
        WHERE recvr_node_id IN
               (SELECT node_id
                  FROM RPT_HIERARCHY
               CONNECT BY PRIOR node_id = parent_node_id
                 START WITH node_id IN (SELECT *
                                 FROM TABLE (
                                         get_array_varchar (
                                           NVL (p_in_node_id,node_id)))))
               AND r.claim_id is not null
          AND r.recvr_user_id = u.user_id                       --09/21/2012
          and r.recvr_node_id = u.node_id                       --09/21/2012
          AND u.role = NVL(p_in_job_code,role)            --09/21/2012
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections 
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
--          AND date_approved = NVL(p_in_date_approved,date_approved)
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND recvr_pax_status = NVL(p_in_pax_status,recvr_pax_status)
          --AND recvr_job_position = NVL(p_in_job_code,recvr_job_position)  --09/21/2012
          AND ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND recvr_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;
    END IF ;
  END IF;

  RETURN v_actual_cnt;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry('FNC_ACTUAL_GIVER_RECVR_COUNT',1, 'ERROR' ,'node: '||p_in_node_id||
                            '  promo: '||p_in_promotion_id||'  award_type: '||p_in_award_type||
                            '  behavior: '||p_in_behavior||
                            '  giver or receiver type: '||p_in_giver_recvr_type||'  '||SQLERRM,null);
       v_actual_cnt := 0;
       RETURN v_actual_cnt;
END; -- FNC_ACTUAL_GIVER_RECVR_COUNT


FUNCTION FNC_ACTUAL_RECOGNITION_COUNT
  ( p_in_node_id IN NUMBER,
    p_in_promotion_id IN VARCHAR2,
    p_in_award_type IN VARCHAR2,
    p_in_behavior IN VARCHAR2,
    p_in_promotion_status IN VARCHAR2,   --09/21/2012
--    p_in_date_approved IN DATE,
      p_in_from_date       IN DATE,
      p_in_to_date       IN DATE,
    p_in_giver_recvr_type IN VARCHAR2,
    p_in_record_type IN VARCHAR2,
    p_in_pax_status   IN VARCHAR2,
    p_in_job_code     IN VARCHAR2,
    p_in_department   IN VARCHAR2  )
  RETURN  NUMBER IS
/*******************************************************************************

   Purpose:  Get the number of actual givers of at least one recognition or
             actual receivers of at least one recognition for a given node and
             possibly by promotion_id, award_type, behavior and date_approved
             if those parameters are chosen in the recognition summary report.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Arun S        01/13/2006  Initial Creation
  Chidamba      09/21/2012  Changes for G5 reports
*******************************************************************************/
   v_actual_cnt     NUMBER;

BEGIN

  IF p_in_giver_recvr_type = 'giver' THEN
     IF p_in_record_type = 'team' THEN
       SELECT COUNT( giver_user_id)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL r,
              promotion p                                                       --09/21/2012
        WHERE giver_node_id = p_in_node_id
           AND r.claim_id is not null
          AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
--          AND TRUNC(date_approved) = TRUNC(NVL(p_in_date_approved,date_approved))
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND giver_pax_status = NVL(p_in_pax_status,giver_pax_status)
          AND NVL(giver_job_position,'job') = NVL(p_in_job_code,NVL(giver_job_position,'job'))
--          AND giver_department = NVL(p_in_department,giver_department);
          AND  ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND giver_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;--10/05/2012

     ELSE
       SELECT COUNT(giver_user_id)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL r,
              promotion p                                                       --09/21/2012
        WHERE giver_node_id IN
               (SELECT node_id
                  FROM RPT_HIERARCHY
               CONNECT BY PRIOR node_id = parent_node_id
                 START WITH node_id =p_in_node_id)
               AND r.claim_id is not null
         AND --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id))) --10/05/2012
          ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
--          AND TRUNC(date_approved) = TRUNC(NVL(p_in_date_approved,date_approved))
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND giver_pax_status = NVL(p_in_pax_status,giver_pax_status)
          AND NVL(giver_job_position,'job') = NVL(p_in_job_code,NVL(giver_job_position,'job'))
          AND  ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND giver_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;--10/05/2012

     END IF ;
  ELSIF p_in_giver_recvr_type = 'receiver' THEN
     IF p_in_record_type = 'team' THEN
       SELECT COUNT( recvr_user_id)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL R,
              user_node u,                              --09/21/2012
              promotion p                               --09/21/2012
        WHERE recvr_node_id = p_in_node_id
        AND r.claim_id is not null
          AND 
           ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections
          AND r.recvr_user_id = u.user_id                       --09/21/2012
          and r.recvr_node_id = u.node_id                       --09/21/2012
          AND u.role = NVL(p_in_job_code,role)            --09/21/2012
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND recvr_pax_status = NVL(p_in_pax_status,recvr_pax_status)
          --AND recvr_job_position = NVL(p_in_job_code,recvr_job_position)      --09/21/2012
          AND  ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND recvr_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;--10/05/2012

     ELSE
       SELECT COUNT( recvr_user_id)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL r,
              user_node u,                              --09/21/2012
              promotion p                               --09/21/2012
        WHERE recvr_node_id IN
               (SELECT node_id
                  FROM RPT_HIERARCHY
               CONNECT BY PRIOR node_id = parent_node_id
                 START WITH node_id =p_in_node_id)
               AND r.claim_id is not null
          AND --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id))) --10/05/2012
           ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections
          AND r.recvr_user_id = u.user_id                       --09/21/2012
          and r.recvr_node_id = u.node_id                       --09/21/2012
          AND u.role = NVL(p_in_job_code,role)            --09/21/2012
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND recvr_pax_status = NVL(p_in_pax_status,recvr_pax_status)
          --AND recvr_job_position = NVL(p_in_job_code,recvr_job_position)       --09/21/2012
         AND  ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND recvr_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;--10/05/2012

    END IF ;
  END IF;

  RETURN v_actual_cnt;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry('FNC_ACTUAL_RECOGNITION_COUNT',1,'ERROR','node: '||p_in_node_id||
                            '  promo: '||p_in_promotion_id||'  award_type: '||p_in_award_type||
                            '  behavior: '||p_in_behavior||
                            '  giver or receiver type: '||p_in_giver_recvr_type||'  '||SQLERRM,null);
       v_actual_cnt := 0;
       RETURN v_actual_cnt;
END; -- FNC_ACTUAL_RECOGNITION_COUNT


FUNCTION FNC_ACTUAL_AWARD_COUNT
  ( p_in_node_id IN NUMBER,
    p_in_promotion_id IN VARCHAR2,
    p_in_award_type IN VARCHAR2,
    p_in_behavior IN VARCHAR2,
    p_in_promotion_status IN VARCHAR2,  --09/21/2012
--    p_in_date_approved IN DATE,
      p_in_from_date       IN DATE,
      p_in_to_date       IN DATE,
    p_in_giver_recvr_type IN VARCHAR2,
    p_in_record_type IN VARCHAR2,
    p_in_pax_status   IN VARCHAR2,
    p_in_job_code     IN VARCHAR2,
    p_in_department   IN VARCHAR2  )
  RETURN  NUMBER IS
/*******************************************************************************

   Purpose:  Get the number of actual givers of at least one recognition or
             actual receivers of at least one recognition for a given node and
             possibly by promotion_id, award_type, behavior and date_approved
             if those parameters are chosen in the recognition summary report.

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   Arun S        01/13/2006  Initial Creation
   Chidamba      09/21/2012  Changes for G5 reports
*******************************************************************************/
   v_actual_cnt     NUMBER;

BEGIN

  IF p_in_giver_recvr_type = 'giver' THEN
     IF p_in_record_type = 'team' THEN
       SELECT NVL(SUM(NVL(award_amt,0)),0)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL r,
              promotion p                                                       --09/21/2012
        WHERE giver_node_id = p_in_node_id
        AND r.claim_id is not null
          AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections 
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
--          AND TRUNC(date_approved) = TRUNC(NVL(p_in_date_approved,date_approved))
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND giver_pax_status = NVL(p_in_pax_status,giver_pax_status)
          AND NVL(giver_job_position,'job') = NVL(p_in_job_code,NVL(giver_job_position,'job'))
         AND  ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND giver_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;--10/05/2012

     ELSE
       SELECT NVL(SUM(NVL(award_amt,0)),0)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL r,
              promotion p                                                       --09/21/2012
        WHERE giver_node_id IN
             (SELECT node_id
                FROM RPT_HIERARCHY
             CONNECT BY PRIOR node_id = parent_node_id
               START WITH node_id = p_in_node_id)
               AND r.claim_id is not null
          AND --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id)))
           ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections 
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
--          AND TRUNC(date_approved) = TRUNC(NVL(p_in_date_approved,date_approved))
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND giver_pax_status = NVL(p_in_pax_status,giver_pax_status)
          AND NVL(giver_job_position,'job') = NVL(p_in_job_code,NVL(giver_job_position,'job'))
          AND  ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND giver_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;--10/05/2012

     END IF ;
  ELSIF p_in_giver_recvr_type = 'receiver' THEN
     IF p_in_record_type = 'team' THEN
       SELECT NVL(SUM(NVL(award_amt,0)),0)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL r,
              user_node u,                              --09/21/2012
              promotion p                               --09/21/2012
        WHERE recvr_node_id = p_in_node_id
        AND r.claim_id is not null
          AND --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id)))
           ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections 
          AND r.recvr_user_id = u.user_id                       --09/21/2012
          and r.recvr_node_id = u.node_id                       --09/21/2012
          AND u.role = NVL(p_in_job_code,role)            --09/21/2012
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
--          AND TRUNC(date_approved) = TRUNC(NVL(p_in_date_approved,date_approved))
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND recvr_pax_status = NVL(p_in_pax_status,recvr_pax_status)
          --AND recvr_job_position = NVL(p_in_job_code,recvr_job_position)   --09/21/2012
          AND  ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND recvr_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;--10/05/2012


     ELSE
       SELECT NVL(SUM(NVL(award_amt,0)),0)
         INTO v_actual_cnt
         FROM RPT_RECOGNITION_DETAIL r,
              user_node u,                              --09/21/2012
              promotion p                               --09/21/2012
        WHERE recvr_node_id IN
             (SELECT node_id
                FROM RPT_HIERARCHY
             CONNECT BY PRIOR node_id = parent_node_id
               START WITH node_id = p_in_node_id)
               AND r.claim_id is not null
          AND --((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (p_in_promotion_id)))
           ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND r.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections 
          AND r.recvr_user_id = u.user_id                       --09/21/2012
          and r.recvr_node_id = u.node_id                       --09/21/2012
          AND u.role = NVL(p_in_job_code,role)            --09/21/2012
          AND p.promotion_id = r.promotion_id                                   --09/21/2012
          AND p.promotion_status = NVL(p_in_promotion_status,p.promotion_status)  --09/21/2012
          AND r.award_type = NVL(p_in_award_type,r.award_type)
          AND behavior = NVL(p_in_behavior,behavior)
--          AND TRUNC(date_approved) = TRUNC(NVL(p_in_date_approved,date_approved))
          AND TRUNC(date_approved) BETWEEN p_in_from_date AND p_in_to_date
          AND recvr_pax_status = NVL(p_in_pax_status,recvr_pax_status)
          --AND recvr_job_position = NVL(p_in_job_code,recvr_job_position)      --09/21/2012    
          AND  ((p_in_department IS NULL) OR (p_in_department is NOT NULL AND recvr_department IN (SELECT * FROM TABLE(get_array_varchar(p_in_department))))) ;--10/05/2012

    END IF ;
  END IF;

  RETURN v_actual_cnt;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry('FNC_ACTUAL_AWARD_COUNT',1,'ERROR','node: '||p_in_node_id||
                            '  promo: '||p_in_promotion_id||'  award_type: '||p_in_award_type||
                            '  behavior: '||p_in_behavior||
                            '  giver or receiver type: '||p_in_giver_recvr_type||'  '||SQLERRM,null);
       v_actual_cnt := 0;
       RETURN v_actual_cnt;
END; -- FNC_ACTUAL_AWARD_COUNT


PROCEDURE P_RPT_RECOG_DETL_FILELOAD IS

/*******************************************************************************
   Purpose:  Populate the rpt_recognition_detail reporting table for deposits 
             made through file load (Bug #18304)

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   S Majumder    04/15/2008 Initial Creation
   nagarajs     03/21/2012  Bug #40345 fix to generate Recognition Detail report for live and expired promotions
                            but not for deleted promotion.
   Ravi Dhanekula 05/19/2014  bug # 53509 Changed the definition of the variable v_hierarchy_path to read from the node.path column
*******************************************************************************/

CURSOR cur_recog_detl_fileload IS
               SELECT j.user_id, j.promotion_id, j.transaction_date, j.transaction_amt, p.promotion_name, p.award_type
               FROM journal j, promotion p
               WHERE journal_id IN (SELECT journal_id from journal
                                    MINUS
                                    SELECT journal_id from activity_journal)
               AND LOWER(transaction_type) in ('deposit', 'payout')
               AND LOWER(journal_type) = 'award'
               AND j.promotion_id IS NOT NULL
               AND j.promotion_id = p.promotion_id
               AND p.promotion_status IN ('live','expired')  --03/21/2012
               AND p.is_deleted = 0                          --03/21/2012
               AND LOWER(p.PROMOTION_TYPE) = 'recognition';

v_rpt_recognition_detail            RPT_RECOGNITION_DETAIL%ROWTYPE;

  v_hierarchy_path      node.path%TYPE;--VARCHAR2(100); --05/19/2014 bug # 53509
  v_hierarchy_id        NUMBER;
  v_hierarchy_name      VARCHAR2(100);
  v_user_name           VARCHAR2(100);


BEGIN
   FOR rec_recog_detl_fileload IN cur_recog_detl_fileload LOOP
        SELECT RPT_RECOGNITION_DETAIL_PK_SQ.nextval
        INTO v_rpt_recognition_detail.RPT_RECOGNITION_DETAIL_ID
        FROM DUAL;
        
        pkg_report_common.P_USER_DETAILS(rec_recog_detl_fileload.user_id, v_rpt_recognition_detail.RECVR_FULL_NAME, v_rpt_recognition_detail.RECVR_FIRST_NAME, v_rpt_recognition_detail.RECVR_MIDDLE_NAME, v_rpt_recognition_detail.RECVR_LAST_NAME, v_rpt_recognition_detail.RECVR_NODE_NAME, v_rpt_recognition_detail.RECVR_NODE_ID, v_hierarchy_path, v_hierarchy_id , v_hierarchy_name, v_rpt_recognition_detail.RECVR_PAX_STATUS, v_user_name, v_rpt_recognition_detail.RECVR_JOB_POSITION, v_rpt_recognition_detail.RECVR_DEPARTMENT);
        
        v_rpt_recognition_detail.RECVR_USER_ID := rec_recog_detl_fileload.user_id;
        v_rpt_recognition_detail.promotion_id := rec_recog_detl_fileload.promotion_id;
        v_rpt_recognition_detail.promotion_name := rec_recog_detl_fileload.promotion_name;
        v_rpt_recognition_detail.award_type := rec_recog_detl_fileload.award_type;
        v_rpt_recognition_detail.trans_date := rec_recog_detl_fileload.transaction_date;
        v_rpt_recognition_detail.award_amt := rec_recog_detl_fileload.transaction_amt;
        v_rpt_recognition_detail.date_submitted := rec_recog_detl_fileload.transaction_date;
        v_rpt_recognition_detail.date_approved := rec_recog_detl_fileload.transaction_date;
        v_rpt_recognition_detail.giver_full_name := 'File Load';
        v_rpt_recognition_detail.giver_node_name := '-';        
        
        P_INSERT_RPT_RECOGNITION_DTL(v_rpt_recognition_detail);
        
   END LOOP;

   EXCEPTION
     WHEN OTHERS THEN
       prc_execution_log_entry('P_RPT_RECOG_DETL_FILELOAD',1,'ERROR',SQLERRM,null);
END P_RPT_RECOG_DETL_FILELOAD;


PROCEDURE p_program_reference_nbr(p_in_requested_user_id      IN  NUMBER,
                                  p_in_start_date             IN  DATE,
                                  p_in_end_date               IN  DATE,
                                  p_out_return_code           OUT NUMBER,
                                  p_out_error_message         OUT VARCHAR2)
IS
/****************************************************************************/
-- Chidamba    13/09/2012   G5 changes to make reports incremental  
/****************************************************************************/
      CURSOR cur_pax_recipient
      IS
         SELECT n.NAME, au.first_name, au.last_name, uc.addr1, uc.addr2,
                uc.city, uc.state, uc.postal_code, uc.country_code,
                uea.email_addr, 
                mo.reference_number, mo.expiration_date,
                mo.date_created, p.promotion_name, pmp.program_id,
                fnc_cms_asset_code_value (pmp.cm_asset_key) award_level,
                au.user_name
           FROM application_user au,
                user_node un,
                node n,
                (select ua.user_id, ua.addr1, ua.addr2, 
                        ua.city, ua.state, ua.postal_code, c.COUNTRY_CODE 
                   from user_address ua, country c 
                  where ua.COUNTRY_ID = c.COUNTRY_ID
                    AND (ua.date_created < p_in_start_date  AND ua.date_created >= p_in_end_date  
                         OR ua.date_modified < p_in_start_date AND ua.date_modified >= p_in_end_date
                         OR c.date_created < p_in_start_date   AND c.date_created >= p_in_end_date  
                         OR c.date_modified < p_in_start_date  AND c.date_modified >= p_in_end_date)) uc,
                user_email_address uea,
                merch_order mo,
                promotion p,
                promo_merch_program_level pmp,
                promo_merch_country pmc
          WHERE au.user_id = uc.user_id(+)
            AND pmp.promo_merch_country_id = pmc.promo_merch_country_id
            AND pmc.promotion_id = p.promotion_id
            AND p.promotion_status IN ('live','expired')  --03/21/2012 --Bug Fix #40345
            AND p.is_deleted = 0                          --03/21/2012 --Bug Fix #40345
            AND uea.user_id(+) = au.user_id
            AND au.user_id = mo.participant_id
            AND au.user_id = un.user_id
            AND un.node_id = n.node_id
            AND pmp.promo_merch_program_level_id = mo.promo_merch_program_level_id
            AND LOWER (p.promotion_type) = LOWER ('recognition')
            AND LOWER (p.award_type) = LOWER ('merchandise')
            AND (uea.date_created < p_in_start_date    AND uea.date_created >= p_in_end_date  
              OR uea.date_modified < p_in_start_date   AND uea.date_modified >= p_in_end_date
              OR pmp.date_created < p_in_start_date    AND pmp.date_created >= p_in_end_date  
              OR pmp.date_modified < p_in_start_date   AND pmp.date_modified >= p_in_end_date
              OR pmc.date_created < p_in_start_date    AND pmc.date_created >= p_in_end_date  
              OR pmc.date_modified < p_in_start_date   AND pmc.date_modified >= p_in_end_date
              OR p.date_created < p_in_start_date      AND p.date_created >= p_in_end_date  
              OR p.date_modified < p_in_start_date     AND p.date_modified >= p_in_end_date
              OR mo.date_created < p_in_start_date     AND mo.date_created >= p_in_end_date  
              OR mo.date_modified < p_in_start_date    AND mo.date_modified >= p_in_end_date);

--      CURSOR cur_non_pax_recipient ***********Commented out as we no longer have non pax recognition in G5
--      IS
--         SELECT mo.non_participant_first_name, mo.non_participant_last_name,
--                mo.non_participant_email, c.country_code, mo.reference_number,
--                mo.expiration_date, mo.date_created, p.promotion_name,
--                pmp.program_id,
--                fnc_cms_asset_code_value (pmp.cm_asset_key) award_level
--           FROM merch_order mo,
--                application_user au,
--                promotion p,
--                promo_merch_program_level pmp,
--                promo_merch_country pmc,
--                country c
--          WHERE pmc.country_id = c.country_id
--            AND pmp.promo_merch_country_id = pmc.promo_merch_country_id
--            AND pmc.promotion_id = p.promotion_id
--            AND p.promotion_status IN ('live','expired')  --03/21/2012  --Bug Fix #40345
--            AND p.is_deleted = 0                          --03/21/2012  --Bug Fix #40345
--            AND mo.participant_id IS NULL
--            AND pmp.promo_merch_program_level_id =
--                                               mo.promo_merch_program_level_id
--            AND LOWER (p.promotion_type) = LOWER ('recognition')
--            AND LOWER (p.award_type) = LOWER ('merchandise')
--            AND (pmp.date_created < p_in_start_date    AND pmp.date_created >= p_in_end_date  
--              OR pmp.date_modified < p_in_start_date   AND pmp.date_modified >= p_in_end_date
--              OR pmc.date_created < p_in_start_date    AND pmc.date_created >= p_in_end_date  
--              OR pmc.date_modified < p_in_start_date   AND pmc.date_modified >= p_in_end_date
--              OR p.date_created < p_in_start_date      AND p.date_created >= p_in_end_date  
--              OR p.date_modified < p_in_start_date     AND p.date_modified >= p_in_end_date
--              OR mo.date_created < p_in_start_date     AND mo.date_created >= p_in_end_date  
--              OR mo.date_modified < p_in_start_date    AND mo.date_modified >= p_in_end_date
--              OR c.date_created < p_in_start_date   AND c.date_created >= p_in_end_date  
--              OR c.date_modified < p_in_start_date  AND c.date_modified >= p_in_end_date);

      v_pax_site   os_propertyset.string_val%TYPE;
   BEGIN
      DELETE      rpt_program_reference;

      SELECT string_val
        INTO v_pax_site
        FROM os_propertyset
       WHERE entity_name = 'site.url.prod';

      FOR rec_pax_recipient IN cur_pax_recipient
      LOOP
         INSERT INTO tmp_program_reference_rpt
                     (hierarchy_name, first_name,
                      last_name, addr1,
                      addr2, city,
                      state,
                      postal_code,
                      country,
                      email,
                      reference_number,
                      award_level,
                      date_expired, date_redeemed,
                      date_issued,
                      promotion_name,
                      om_program_number, pax_site,
                      login_id
                     )
              VALUES (rec_pax_recipient.NAME, rec_pax_recipient.first_name,
                      rec_pax_recipient.last_name, rec_pax_recipient.addr1,
                      rec_pax_recipient.addr2, rec_pax_recipient.city,
                      rec_pax_recipient.state,
                      rec_pax_recipient.postal_code,
                      rec_pax_recipient.country_code,
                      rec_pax_recipient.email_addr,
                      rec_pax_recipient.reference_number,
                      rec_pax_recipient.award_level,
                      rec_pax_recipient.expiration_date, NULL,
                      rec_pax_recipient.date_created,
                      rec_pax_recipient.promotion_name,
                      rec_pax_recipient.program_id, v_pax_site,
                      rec_pax_recipient.user_name
                     );
      END LOOP;

--      FOR rec_non_pax_recipient IN cur_non_pax_recipient
--      LOOP
--         INSERT INTO tmp_program_reference_rpt
--                     (hierarchy_name,
--                      first_name,
--                      last_name, addr1,
--                      addr2, city, state, postal_code,
--                      country,
--                      email,
--                      reference_number,
--                      award_level,
--                      date_expired, date_redeemed,
--                      date_issued,
--                      promotion_name,
--                      om_program_number, pax_site, login_id
--                     )
--              VALUES (NULL,
--                      rec_non_pax_recipient.non_participant_first_name,
--                      rec_non_pax_recipient.non_participant_last_name, NULL,
--                      NULL, NULL, NULL, NULL,
--                      rec_non_pax_recipient.country_code,
--                      rec_non_pax_recipient.non_participant_email,
--                      rec_non_pax_recipient.reference_number,
--                      rec_non_pax_recipient.award_level,
--                      rec_non_pax_recipient.expiration_date, NULL,
--                      rec_non_pax_recipient.date_created,
--                      rec_non_pax_recipient.promotion_name,
--                      rec_non_pax_recipient.program_id, v_pax_site, NULL
--                     );
--      END LOOP;

      /*INSERT INTO rpt_program_reference
                  (hierarchy_name, first_name, last_name, addr1, addr2, city,
                   state, postal_code, country, email, reference_number,
                   award_level, date_expired, date_redeemed, date_issued,
                   promotion_name, om_program_number, pax_site, login_id)
         (SELECT hierarchy_name, first_name, last_name, addr1, addr2, city,
                 state, postal_code, country, email, reference_number,
                 award_level, date_expired, date_redeemed, date_issued,
                 promotion_name, om_program_number, pax_site, login_id
            FROM tmp_program_reference_rpt);*/
       MERGE INTO rpt_program_reference d
      USING (  SELECT rowid rw,
                      hierarchy_name, first_name, last_name, addr1, addr2, city,
                      state, postal_code, country, email, reference_number,
                      award_level, date_expired, date_redeemed, date_issued,
                      promotion_name, om_program_number, pax_site, login_id
                 FROM tmp_program_reference_rpt) s
               ON (d.rowid =  s.rw)
           WHEN MATCHED THEN
             UPDATE 
                SET d.hierarchy_name  =     s.hierarchy_name, 
              d.first_name   =  s.first_name, 
              d.last_name    =  s.last_name, 
              d.addr1   =  s.addr1, 
              d.addr2   =  s.addr2, 
              d.city    =    s.city,
              d.state   =  s.state, 
              d.postal_code =  s.postal_code, 
              d.country =  s.country, 
              d.email   =  s.email, 
              d.reference_number    =    s.reference_number,
              d.award_level  =  s.award_level, 
              d.date_expired    =  s.date_expired, 
              d.date_redeemed   =  s.date_redeemed, 
              d.date_issued  =    s.date_issued,
              d.promotion_name  =  s.promotion_name, 
              d.om_program_number   =  s.om_program_number, 
              d.pax_site    =  s.pax_site, 
              d.login_id =   s.login_id
           WHEN NOT MATCHED THEN
           INSERT 
             (hierarchy_name, 
              first_name, 
              last_name, 
              addr1, 
              addr2, 
              city,
              state, 
              postal_code, 
              country, 
              email, 
              reference_number,
              award_level, 
              date_expired, 
              date_redeemed, 
              date_issued,
              promotion_name, 
              om_program_number, 
              pax_site, 
              login_id)
          VALUES(s.hierarchy_name, 
              s.first_name, 
              s.last_name, 
              s.addr1, 
              s.addr2, 
              s.city,
              s.state, 
              s.postal_code, 
              s.country, 
              s.email, 
              s.reference_number,
              s.award_level, 
              s.date_expired, 
              s.date_redeemed, 
              s.date_issued,
              s.promotion_name, 
              s.om_program_number, 
              s.pax_site, 
      s.login_id);

   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry ('P_PROGRAM_REFERENCE_NBR',
                                  1,
                                  'ERROR',
                                  SQLERRM,
                                  NULL
                                 );
   END;
   
   
    PROCEDURE P_AWARD_EARNINGS_REPORT IS
    
       CURSOR cur_award_earnings IS
        SELECT c.user_id, c.last_name, c.first_name, c.ssn,
               fnc_cms_asset_code_value (b.cm_asset_key) award_level,
               a.points max_value_level, a.points item_value, a.reference_number,
               b.program_id program_number, a.is_redeemed item_redeemed,
               a.date_created date_issued, c.email_addr, c.addr1, c.addr2, c.addr3,
               c.city, c.state, c.postal_code, c.country_code country, c.user_name,
               b.promotion_id, c.country_id
          FROM (SELECT mo.participant_id, mo.promo_merch_program_level_id,
                       mo.reference_number, mo.is_redeemed, mo.date_created,
                       mo.points, merch_gift_code_type
                  FROM merch_order mo, claim c
                 WHERE mo.claim_id = c.claim_id(+)) a,
               (SELECT pmcl.promo_merch_program_level_id, pmcl.program_id,
                       pmcl.cm_asset_key, p.promotion_id
                  FROM promo_merch_country pmc,
                       promo_merch_program_level pmcl,
                       promotion p
                 WHERE p.promotion_id = pmc.promotion_id
                   AND p.promotion_status IN ('live','expired')  --03/21/2012  --Bug Fix #40345
                   AND p.is_deleted = 0                          --03/21/2012  --Bug Fix #40345
                   AND pmc.promo_merch_country_id = pmcl.promo_merch_country_id
                   AND LOWER (p.promotion_type) = LOWER ('recognition')
                   AND LOWER (p.award_type) = LOWER ('merchandise')) b,
               (SELECT au.user_id, au.last_name, au.first_name, au.ssn, em.email_addr,
                       ua.addr1, ua.addr2, ua.addr3, ua.city, ua.state,
                       ua.postal_code, co.country_code, au.user_name, co.country_id
                  FROM application_user au,
                       (SELECT uea.user_id, uea.email_addr
                          FROM user_email_address uea
                         WHERE uea.is_primary = 1) em,
                       user_address ua,
                       country co
                 WHERE au.user_id = em.user_id(+)
                   AND au.user_id = ua.user_id
                   AND ua.country_id = co.country_id
                   AND ua.is_primary = 1) c
         WHERE a.participant_id = c.user_id AND a.promo_merch_program_level_id(+) =
                                                        b.promo_merch_program_level_id;
           

           
           
           TYPE temp_char_value_table IS TABLE OF user_characteristic.CHARACTERISTIC_VALUE%TYPE;

           -- Define a variable of the associative array type.
           user_characteristic_value temp_char_value_table;
           
           rpt_award_earnings_pk NUMBER;
           
           
        BEGIN
        
            DELETE RPT_AWARD_EARNINGS;
        
          FOR rec_award_earnings IN cur_award_earnings LOOP
            SELECT rpt_award_earnings_pk_sq.nextval
                INTO rpt_award_earnings_pk
            FROM dual;
          
            SELECT characteristic_value
                BULK COLLECT INTO user_characteristic_value
            FROM user_characteristic uc
            WHERE uc.USER_ID = rec_award_earnings.user_id;

            user_characteristic_value.extend(5);
            INSERT INTO RPT_AWARD_EARNINGS (RPT_AWARD_EARNINGS_ID,LAST_NAME, FIRST_NAME, PROMOTION_ID, AWARD_LEVEL, REFERENCE_NUMBER, PROGRAM_NUMBER, MAX_VALUE_LEVEL, ITEM_VALUE, DATE_REDEEMED, DATE_ISSUED, ITEM_REDEEMED, EMAIL_ADDRESS, SSN, ADDR1, ADDR2, ADDR3, CITY, STATE, ZIP, COUNTRY_ID, COUNTRY, LOGIN_ID, CHARACTERISTIC_VALUE1, CHARACTERISTIC_VALUE2, CHARACTERISTIC_VALUE3, CHARACTERISTIC_VALUE4, CHARACTERISTIC_VALUE5)
                                           VALUES
                                           (rpt_award_earnings_pk, rec_award_earnings.LAST_NAME, rec_award_earnings.FIRST_NAME, rec_award_earnings.promotion_id, rec_award_earnings.AWARD_LEVEL, rec_award_earnings.REFERENCE_NUMBER, rec_award_earnings.PROGRAM_NUMBER, rec_award_earnings.MAX_VALUE_LEVEL, rec_award_earnings.ITEM_VALUE, NULL, rec_award_earnings.DATE_ISSUED, rec_award_earnings.ITEM_REDEEMED, rec_award_earnings.EMAIL_ADDR, rec_award_earnings.SSN, rec_award_earnings.ADDR1, rec_award_earnings.ADDR2, rec_award_earnings.ADDR3, rec_award_earnings.CITY, rec_award_earnings.STATE, rec_award_earnings.postal_code, rec_award_earnings.country_id, rec_award_earnings.COUNTRY, rec_award_earnings.user_name, user_characteristic_value(1), user_characteristic_value(2), user_characteristic_value(3), user_characteristic_value(4), user_characteristic_value(5));
          
          END LOOP;
        
        END;   -- End of procedure P_AWARD_EARNINGS_REPORT

      
PROCEDURE p_get_user_details 
  (p_in_user_id      IN  application_user.user_id%TYPE,
   p_out_rec         OUT application_user%ROWTYPE,
   p_out_email_addr  OUT user_email_address.email_addr%TYPE,
   p_out_user_country_id OUT NUMBER)
IS

  v_stage       VARCHAR2(500);
           
BEGIN

  BEGIN  
    v_stage := 'Get user name: '||p_in_user_id;
    SELECT last_name,
           first_name,
           user_name,
           is_active
      INTO p_out_rec.last_name,
           p_out_rec.first_name,
           p_out_rec.user_name,
           p_out_rec.is_active     
      FROM application_user
     WHERE user_id = p_in_user_id;

    v_stage := 'Get user email: '||p_in_user_id;
    BEGIN
      SELECT email_addr
        INTO p_out_email_addr
        FROM user_email_address
       WHERE user_id = p_in_user_id
         AND is_primary = 1;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        p_out_email_addr := NULL;           
    END;   
    
     v_stage := 'Get user email: '||p_in_user_id;
    BEGIN
      SELECT country_id
        INTO p_out_user_country_id
        FROM user_address
       WHERE user_id = p_in_user_id
         AND is_primary = 1;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        p_out_user_country_id := NULL;           
    END;   

  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      p_out_rec := NULL;
      p_out_email_addr := NULL;           
  END;

EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry ('P_GET_USER_DETAILS',
                              1,
                             'ERROR',
                              'Stage: '||v_stage||
                              ' Message: '||SQLERRM,
                              NULL);
       
END p_get_user_details;  --p_get_user_details

PROCEDURE PRC_GET_MANAGER_DETAIL 
  (p_in_user_id      IN application_user.user_id%TYPE,
   p_in_node_id      IN user_node.node_id%TYPE,
   p_out_node_name   OUT node.name%TYPE,
   p_out_rec         OUT application_user%ROWTYPE,
   p_out_email_addr  OUT user_email_address.email_addr%TYPE,
   p_out_manager_country_id  OUT NUMBER)
  IS
/*-----------------------------------------------------------------------------

  Person       Date           Comments
  --------    ----------    ------------------------------------------------ 
  Swati            10/27/2014    Bug 57598 - In the report Recognition PURL Activity it is not populating the Manager Country                                                               
-----------------------------------------------------------------------------*/
  v_node_id      user_node.node_id%TYPE;
  v_node_name    node.name%TYPE;
  v_stage        VARCHAR2(500);
BEGIN

  v_node_id := p_in_node_id;

  LOOP
    p_out_rec := NULL;
    v_node_name := NULL;
      
    BEGIN
      v_stage := 'Get Owner user name: '||p_in_user_id;
      SELECT au.user_id,
             au.last_name,
             au.first_name,
             au.user_name,
             n.name
        INTO p_out_rec.user_id,
             p_out_rec.last_name,
             p_out_rec.first_name,
             p_out_rec.user_name,
             v_node_name
        FROM user_node un,
             node n,
             application_user au,
             user_address ua
       WHERE un.node_id = n.node_id
         AND un.user_id = au.user_id
         AND un.status = 1
         AND au.is_active = 1
         AND un.role = 'own'
         AND au.user_id = ua.user_id
         AND ua.is_primary = 1 
         AND un.user_id != p_in_user_id
         AND un.node_id = v_node_id;
    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        SELECT parent_node_id
          INTO v_node_id
          FROM node
         WHERE node_id = v_node_id;     

    END;
    
    IF p_out_rec.user_id IS NOT NULL THEN
      EXIT;
    END IF;  
    
  END LOOP; 
 
  p_out_node_name := v_node_name;
   
  BEGIN
    v_stage := 'Get user email: '||p_in_user_id;
    SELECT email_addr
      INTO p_out_email_addr
      FROM user_email_address
     WHERE is_primary = 1
       AND user_id = p_out_rec.user_id;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      p_out_email_addr := NULL;           
  END;
  
  BEGIN --10/27/2014 Bug 57598
    v_stage := 'Get user country: '||p_in_user_id;
    SELECT country_id 
        INTO p_out_manager_country_id
    FROM user_address
    WHERE user_id = p_out_rec.user_id ;
        
 EXCEPTION
 WHEN NO_DATA_FOUND THEN
   p_out_manager_country_id := NULL;           
 END;
  
EXCEPTION
  WHEN OTHERS THEN
    p_out_rec := NULL;
    p_out_email_addr := NULL;
    prc_execution_log_entry ('PRC_GET_MANAGER_DETAIL',
                              1,
                             'ERROR',
                              'Stage: '||v_stage||
                              ' Message: '||SQLERRM,
                              NULL);
      
END PRC_GET_MANAGER_DETAIL;                               
                             

--FUNCTION FNC_RECIPIENT_COUNT
--  (p_in_promotion_id     IN VARCHAR2,     --10/05/2012 change NUMBER to VARCHAR
--   p_in_node_id          IN NUMBER,
--   p_in_record_type      IN VARCHAR2,
--   p_in_recepient_status IN VARCHAR2, 
--   p_in_from_date        IN DATE,
--   p_in_to_date          IN DATE)
--  RETURN NUMBER
--  IS
--
--/*******************************************************************************
-- 
--  Purpose:  Get the number of recipient for a given
--            promotion and given node from rpt_purl_contribution_detail
--              
--   Person         Date        Comments
--   -----------   ----------  ---------------------------------------------------
--  Arun S        02/18/2011   Initial creation
--  Chidamba      10/05/2012   G5 Changes to make promotion, country and department Multiselect     
--*******************************************************************************/
--
--  v_recipient_cnt     NUMBER;
--
--BEGIN
--
--  IF p_in_record_type = 'team' THEN
--    SELECT COUNT(DISTINCT recipient_user_id)
--      INTO v_recipient_cnt
--      FROM rpt_purl_contribution_detail
--     WHERE ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selectionspromotion_id  IN (NVL( p_in_promotion_id,promotion_id))
--       AND node_id      = NVL(p_in_node_id,node_id)
--       AND recipient_pax_status = NVL(p_in_recepient_status,recipient_pax_status)
--       AND TRUNC(purl_created_date) BETWEEN p_in_from_date AND p_in_to_date;      
--  ELSE
--    SELECT COUNT(DISTINCT recipient_user_id)
--      INTO v_recipient_cnt
--      FROM rpt_purl_contribution_detail
--     WHERE --promotion_id = NVL(p_in_promotion_id, promotion_id)
--      ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))--change made to accept multiple selections
--       AND node_id IN (SELECT node_id
--                         FROM rpt_hierarchy
--                      CONNECT BY PRIOR node_id = parent_node_id
--                        START WITH node_id = NVL(p_in_node_id,node_id))
--       AND recipient_pax_status = NVL(p_in_recepient_status,recipient_pax_status)                         
--       AND TRUNC(purl_created_date) BETWEEN p_in_from_date AND p_in_to_date;      
--
--  END IF;
--
--  RETURN v_recipient_cnt;
--  
--EXCEPTION
--  WHEN OTHERS THEN
--    prc_execution_log_entry('FNC_RECIPIENT_COUNT',
--                            1,
--                            'ERROR',
--                            'node: '||p_in_node_id||
--                            ' promo: '||p_in_promotion_id||SQLERRM,
--                            null);
--    v_recipient_cnt := 0;
--    RETURN v_recipient_cnt;
--END;

--PROCEDURE P_RPT_PURL_CONTRIBUTION_DETAIL(p_in_requested_user_id      IN  NUMBER,
--                                         p_in_start_date             IN  DATE,
--                                         p_in_end_date               IN  DATE,
--                                         p_out_return_code           OUT NUMBER,
--                                         p_out_error_message         OUT VARCHAR2) IS
--
--/*******************************************************************************
-- 
--  Purpose:  Populates Rpt_purl_contribution_detail reporting table
--
--   Person         Date        Comments
--   -----------   ----------  ---------------------------------------------------
--  Arun S        02/04/2011   Initial creation
--  Arun S        03/09/2011   Bug # 36143 Fix, added and populated column recipient_pax_status
--  Arun S        04/27/2011   Added and populated manager_node_name,manager_last_name,manager_first_name,
--                             manager_user_name and manager_email_addr for Recepient manager details
--  nagarajs     03/21/2012   Bug #40345 fix to generate Recognition PURL Activity report for 
--                            live and expired promotions but not for deleted promotion.   
--  Ravi Dhanekula 03/06/2014  Removed the usage of purl_contributor_media table as we no longer use that in G5. 
--  nagarajs       08/12/2014  Bug 55407 - Fixed the Contribution posted column count issue 
--                 09/02/2014  Bug 55407 - Fixed Summary count issue in detail report   
--  Swati             10/27/2014  Bug 57604 - In Recognition PURL Activity report Job Title criteria is not working
--                             Bug 57595 - In Recognition PURL Activity report department criteria is not working
-- Suresh J      01/29/2015   Bug #57300 - Fixed the issue of Node Name Changes that are not reflected in reports        
-- Suresh J      05/26/2015   Bug 62417,62418,62419,62431 - Fixed count issues in PURL reports 
-- nagarajs     11/30/2015   Bug 64832 - Report refresh keep on running for long time
-- nagarajs     12/16/2015   Bug 64993 - Exported report total values are mismatched                        
--*******************************************************************************/
--
--  CURSOR cur_dtl IS
--    SELECT pr.purl_recipient_id,
--           pr.promotion_id,
--           promo.promotion_name,
--           pr.node_id,
--           pr.user_id,
--           pr.award_date,
--           pr.award_level_id,
--           pr.award_amount,
--           nvl(pe.position_type,' ') as job_title, --10/27/2014 Bug 57604
--           nvl(pe.department_type, ' ') as department, --10/27/2014 Bug 57595
--           pc.purl_contributor_id,
--           pc.state,
--           pc.user_id pc_user_id,
--           pc.first_name,
--           pc.last_name,
--           pc.email_addr,
--           TRUNC(pc.date_created) date_invitation_sent,
--           --TRUNC(pc.date_created) purl_created_date --12/16/2015
--           TRUNC(pr.date_created) purl_created_date   --12/16/2015
--      FROM purl_recipient pr,
--           purl_contributor pc,
--           promotion promo,
--           participant_employer pe --10/27/2014 Bug 57595
--     WHERE pc.purl_recipient_id (+) = pr.purl_recipient_id
--       AND pr.promotion_id      = promo.promotion_id
--       AND promo.promotion_status IN ('live','expired')  --03/21/2012  --Bug Fix #40345
--       AND promo.is_deleted = 0                          --03/21/2012  --Bug Fix #40345
--       AND pr.user_id = pe.user_id(+) --10/27/2014 Bug 57595
--       AND pe.termination_date IS NULL --10/27/2014 Bug 57595
--       AND (pr.date_created > p_in_start_date  AND pr.date_created <= p_in_end_date
--         OR pr.date_modified > p_in_start_date AND pr.date_modified <= p_in_end_date
--         OR pc.date_created > p_in_start_date  AND pc.date_created <= p_in_end_date
--         OR pc.date_modified > p_in_start_date AND pc.date_modified <= p_in_end_date
--         OR pe.date_created > p_in_start_date   AND pe.date_created <= p_in_end_date        --11/30/2015 replaced promo with pe
--         OR pe.date_modified > p_in_start_date  AND pe.date_modified <= p_in_end_date)      --11/30/2015 replaced promo with pe    
--     ORDER BY pr.user_id, pr.promotion_id;
--
--  v_recip_rec           application_user%ROWTYPE;
--  v_email_addr          user_email_address.email_addr%TYPE;
--  v_country_id NUMBER;
--  v_manager_country_id NUMBER;
--  v_user_id_old         application_user.user_id%TYPE := 0000;
--  v_node_id_old         node.node_id%TYPE := 0000;
--  v_node_name           node.name%TYPE;
--  v_award_amount        rpt_purl_contribution_detail.award_amount%TYPE;     
--  v_contr_email_addr    rpt_purl_contribution_detail.contributor_email_addr%TYPE;
--  v_contributed_flg     rpt_purl_contribution_detail.contributed_flg%TYPE;
--  v_contribution_posted rpt_purl_contribution_detail.contribution_posted%TYPE;
--  v_contr_count_comment NUMBER := 0;
----  v_contr_count_media   NUMBER := 0;
--  v_sysdate             DATE := TRUNC(SYSDATE);
--  v_mgr_node_name       node.name%TYPE;
--  v_mgr_rec             application_user%ROWTYPE;
--  v_mgr_email_addr      user_email_address.email_addr%TYPE;
--  v_rpt_purl_cntr_detl_id rpt_purl_contribution_detail.rpt_purl_cntr_detl_id%TYPE; 
--   v_tab_node_id        dbms_sql.number_table;   --01/29/2015  
--   v_tab_node_name      dbms_sql.varchar2_table;  --01/29/2015 
--   v_manager_user_name  application_user.USER_NAME%type; --12/17/2015
--
--    --Cursor to pick modified node name   
--  CURSOR cur_node_changed IS          --01/29/2015
--    SELECT node_id,
--           NAME
--      FROM node
--     WHERE date_modified BETWEEN p_in_start_date AND p_in_end_date;  
--  
--BEGIN
--  
-- 
--        
--  FOR rec_dtl IN cur_dtl LOOP
--    
--    v_contributed_flg := 'N';
--     
--    IF v_node_id_old <> rec_dtl.node_id THEN
--      v_node_id_old := rec_dtl.node_id;
--      SELECT name
--        INTO v_node_name
--        FROM node
--       WHERE node_id = rec_dtl.node_id;
--    END IF;
--      
--    IF v_user_id_old <> rec_dtl.user_id THEN
--      v_recip_rec   := NULL;
--      v_email_addr  := NULL;
--      v_user_id_old := rec_dtl.user_id;
--      p_get_user_details(rec_dtl.user_id, v_recip_rec, v_email_addr,v_country_id);
--      prc_get_manager_detail(rec_dtl.user_id,       --04/27/2011
--                             rec_dtl.node_id,
--                             v_mgr_node_name,
--                             v_mgr_rec,
--                             v_mgr_email_addr,v_manager_country_id);
--    END IF;
--    
--    IF rec_dtl.award_amount IS NULL THEN
--      BEGIN
--        SELECT fnc_cms_asset_code_value(cm_asset_key)
--          INTO v_award_amount
--          FROM promo_merch_program_level
--         WHERE promo_merch_program_level_id = rec_dtl.award_level_id;
--      EXCEPTION
--        WHEN NO_DATA_FOUND THEN
--          v_award_amount := NULL;
--      END;
--    ELSE
--      v_award_amount := ROUND(rec_dtl.award_amount,2);
--    END IF;
--    
--    IF rec_dtl.pc_user_id IS NULL THEN
--      v_contr_email_addr := rec_dtl.email_addr; 
--    ELSE
--
--      BEGIN 
--        SELECT email_addr
--          INTO v_contr_email_addr
--          FROM user_email_address
--         WHERE is_primary = 1
--           AND user_id = rec_dtl.pc_user_id;
--      EXCEPTION
--        WHEN NO_DATA_FOUND THEN
--          v_contr_email_addr := NULL;
--      END;
--   
--    END IF;
--    
--    BEGIN
--      
--      v_contr_count_comment := 0;
--      v_contribution_posted := 0; --08/12/2014
----      v_contr_count_media   := 0; --03/06/2014  
--      
--      SELECT COUNT(1)
--        INTO v_contr_count_comment
--        FROM purl_contributor_comment
--       WHERE purl_contributor_id = rec_dtl.purl_contributor_id;
--      
----      SELECT COUNT(1) --03/06/2014 Start 
----        INTO v_contr_count_media
----        FROM purl_contributor_media
----       WHERE purl_contributor_id = rec_dtl.purl_contributor_id;   --03/06/2014 End
--    
--      IF (v_contr_count_comment > 0 ) THEN 
--        v_contributed_flg := 'Y';
--        v_contribution_posted := v_contr_count_comment; --1; --08/12/2014 
--      END IF;
--      
--    END;
--    
--    BEGIN
--      SELECT rpt_purl_cntr_detl_id, manager_user_name --12/16/2015
--        INTO v_rpt_purl_cntr_detl_id, v_manager_user_name
--        FROM rpt_purl_contribution_detail
--       WHERE recipient_user_id  =  rec_dtl.user_id 
--         AND node_id            =  rec_dtl.node_id
--         AND purl_contributor_id = rec_dtl.purl_contributor_id;
--         
--      IF v_manager_user_name <> v_mgr_rec.user_name THEN --12/16/2015
--        UPDATE rpt_purl_contribution_detail
--           SET manager_node_name      = v_mgr_node_name,                
--             manager_last_name      = v_mgr_rec.last_name,  
--             manager_first_name     = v_mgr_rec.first_name,
--             manager_user_name      = v_mgr_rec.user_name,
--             manager_email_addr     = v_mgr_email_addr,
--             manager_country_id = v_manager_country_id
--        WHERE purl_recipient_id = rec_dtl.purl_recipient_id;
--      END IF;
--      
--      UPDATE rpt_purl_contribution_detail
--         SET promotion_name       = rec_dtl.promotion_name,
--             node_name            = v_node_name, 
--             recipient_last_name  = v_recip_rec.last_name,
--             recipient_first_name = v_recip_rec.first_name,
--             recipient_user_name  =  v_recip_rec.user_name,
--             recipient_email_addr = v_email_addr,
--             recipient_country_id = v_country_id,
--             recipient_pax_status = DECODE(v_recip_rec.is_active,1,'active',0,'inactive',null),
--             recipient_department =  rec_dtl.department, --10/27/2014 Bug 57595
--             recipient_job_position = rec_dtl.job_title, --10/27/2014 Bug 57604
--             award_date           =  rec_dtl.award_date,
--             award_amount         =  v_award_amount,                
--             contribution_state   =  rec_dtl.state,                
--             contributor_user_id  =   rec_dtl.pc_user_id,                 
--             contributor_first_name = rec_dtl.first_name,
--             contributor_last_name  = rec_dtl.last_name,
--             contributor_email_addr = v_contr_email_addr,
--             date_invitation_sent   = NVL(rec_dtl.date_invitation_sent,v_sysdate),
--             contributed_flg        = v_contributed_flg,
--             contribution_posted = v_contribution_posted,
--             purl_created_date      = NVL(rec_dtl.purl_created_date,v_sysdate),
--             date_modified          = p_in_end_date, -- SYSDATE,  --09/02/2014 Bug 55407
--             modified_by            = p_in_requested_user_id,
--             manager_node_name      = v_mgr_node_name,                
--             manager_last_name      = v_mgr_rec.last_name,  
--             manager_first_name     = v_mgr_rec.first_name,
--             manager_user_name      = v_mgr_rec.user_name,
--             manager_email_addr     = v_mgr_email_addr,
--             manager_country_id = v_manager_country_id
--             ,purl_recipient_id = rec_dtl.purl_recipient_id   --05/26/2015
--       WHERE rpt_purl_cntr_detl_id  = v_rpt_purl_cntr_detl_id;
--    
--    EXCEPTION
--     WHEN NO_DATA_FOUND THEN
--      INSERT INTO rpt_purl_contribution_detail 
--                (rpt_purl_cntr_detl_id, 
--                 promotion_id, 
--                 promotion_name,   
--                 node_id, 
--                 node_name, 
--                 recipient_user_id,   
--                 recipient_last_name, 
--                 recipient_first_name, 
--                 recipient_user_name,   
--                 recipient_email_addr,
--                 recipient_country_id,
--                 recipient_pax_status,  --03/09/2011 
--                 recipient_department, --10/27/2014 Bug 57595
--                 recipient_job_position, --10/27/2014 Bug 57604
--                 award_date, 
--                 award_amount,
--                 contribution_state,
--                 purl_contributor_id,
--                 contributor_user_id,   
--                 contributor_first_name, 
--                 contributor_last_name, 
--                 contributor_email_addr, 
--                 contribution_posted,  
--                 date_invitation_sent, 
--                 contributed_flg, 
--                 purl_created_date,   
--                 date_created, 
--                 created_by,
--                 manager_node_name,     --04/27/2011
--                 manager_last_name,     --04/27/2011
--                 manager_first_name,    --04/27/2011
--                 manager_user_name,     --04/27/2011
--                 manager_email_addr,    --04/27/2011
--                 manager_country_id
--                 ,purl_recipient_id   --05/26/2015
--                 ) 
--         VALUES (RPT_PURL_CNTR_DETL_PK_SQ.NEXTVAL, 
--                 rec_dtl.promotion_id, 
--                 rec_dtl.promotion_name,   
--                 rec_dtl.node_id, 
--                 v_node_name, 
--                 rec_dtl.user_id,   
--                 v_recip_rec.last_name, 
--                 v_recip_rec.first_name, 
--                 v_recip_rec.user_name,   
--                 v_email_addr,                  
--                 v_country_id,
--                 DECODE(v_recip_rec.is_active,1,'active',0,'inactive',null),    --03/09/2011
--                 rec_dtl.department, --10/27/2014 Bug 57595
--                 rec_dtl.job_title, --10/27/2014 Bug 57604
--                 rec_dtl.award_date, 
--                 v_award_amount,
--                 rec_dtl.state,
--                 rec_dtl.purl_contributor_id,
--                 rec_dtl.pc_user_id,   
--                 rec_dtl.first_name, 
--                 rec_dtl.last_name, 
--                 v_contr_email_addr,   
--                 v_contribution_posted,
--                 NVL(rec_dtl.date_invitation_sent,v_sysdate), 
--                 v_contributed_flg, 
--                 NVL(rec_dtl.purl_created_date,v_sysdate),   
--                 SYSDATE, 
--                 0,
--                 v_mgr_node_name,       --04/27/2011
--                 v_mgr_rec.last_name,   --04/27/2011
--                 v_mgr_rec.first_name,  --04/27/2011
--                 v_mgr_rec.user_name,   --04/27/2011
--                 v_mgr_email_addr,       --04/27/2011
--                 v_manager_country_id
--                 ,rec_dtl.purl_recipient_id  --05/26/2015
--                 );
--    END; 
--                 
--  END LOOP;
--
----  v_stage := 'Open and Fetch cursor to pick modified node name';      --01/29/2015
--  OPEN cur_node_changed;   --01/29/2015
--  FETCH cur_node_changed BULK COLLECT
--   INTO v_tab_node_id,
--        v_tab_node_name;
--  CLOSE cur_node_changed;   
--
----  v_stage := 'Update modified node name in rpt table';    --01/29/2015
--  FORALL indx IN v_tab_node_id.FIRST .. v_tab_node_id.LAST    --01/29/2015
--    UPDATE rpt_purl_contribution_detail
--       SET node_name     = DECODE (node_id, v_tab_node_id(indx), v_tab_node_name(indx), node_name),
--           date_modified = SYSDATE,
--           modified_by   = p_in_requested_user_id
--     WHERE (node_id        = v_tab_node_id(indx)
--            AND node_name != v_tab_node_name(indx)
--            );
--
--EXCEPTION
--  WHEN OTHERS THEN
--    prc_execution_log_entry ('P_RPT_PURL_CONTRIBUTION_DETAIL',
--                              1,
--                             'ERROR',
--                              SQLERRM,
--                              NULL);
--  
--END;    --P_RPT_PURL_CONTRIBUTION_DETAIL

PROCEDURE P_RPT_PURL_CONTRIBUTION_DETAIL(p_in_requested_user_id      IN  NUMBER,
                                         p_in_start_date             IN  DATE,
                                         p_in_end_date               IN  DATE,
                                         p_out_return_code           OUT NUMBER,
                                         p_out_error_message         OUT VARCHAR2) IS

 /******************************************************************************
  NAME:       P_RPT_PURL_CONTRIBUTION_DETAIL   
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Suresh J             12/28/2015       Bug 61865 - Recognition PURL Activity Report - incorrect Recipients count 
  nagarajs             01/29/2016       Bug 65434 - Inactive participants is not showing in the summary table under purl activity report.
  nagarajs             01/29/2016       Bug 65436 - Purl status is not showing up in purl activity report 
 Suresh J              02/17/2016       Bug 65799 - Count not matching between extract and summary in purl activity report 
 RaviArumugam          03/14/2016      Bug  66114 - Contributed posted is displaying wrong count in PURL report
 Ravi Arumugam         03/24/2016     Bug  66133  - Recognition Purl Activity Report - Changes
 nagarajs              04/12/2016     Bug 65706 - Report refresh failing when a PURL recipient has more than one address
 Sherif Basha          10/19/2016     Bug 69528 - In PURL report for some pax Manager details are not displaying
 Suresh J              03/03/2017     G6-1847  - Report Rrefresh was failing
 Chidamba              08/24/2017     G6-2870  - When a participant becomes inactive, their pending purl should also expire
 
 ******************************************************************************/
 

c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('P_RPT_PURL_CONTRIBUTION_DETAIL');
c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
v_stage                   execution_log.text_line%TYPE;
v_rec_cnt            INTEGER;

BEGIN

  v_stage := 'Start';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', 
                                v_stage||'p_in_requested_user_id->'||p_in_requested_user_id||
                                         ' ,p_in_start_date->'||to_char(p_in_start_date,'MM/DD/YYYY')||
                                         ' ,p_in_end_date->'||to_char(p_in_end_date,'MM/DD/YYYY') , NULL);

   
    MERGE INTO rpt_purl_recipient_detail d
    USING (
            SELECT pr.purl_recipient_id,
                       pr.promotion_id,
                       promo.promotion_name,
                       pr.node_id,
                       pr.user_id as recipient_user_id,
                       au.first_name as recipient_first_name,
                       au.last_name as recipient_last_name,
                       au.user_name recipient_user_name,
                       n.name as node_name,
                       ua.country_id as recipient_country_id,
                       uea.email_addr as recipient_email_addr,
                       CASE 
                        WHEN au.is_active = 1 THEN 'active' 
                        ELSE 'inactive' 
                       END AS recipient_pax_status,
                       pr.award_date,
                       pr.award_level_id,
                       --pr.award_amount,
                       CASE WHEN pr.award_level_id IS NOT NULL AND pr.award_amount IS NULL THEN 
                             fnc_cms_asset_code_value(pm.cm_asset_key) 
                            ELSE TO_CHAR(pr.award_amount)
                       END as award_amount,
                       NVL(cps.contribution_status,pr.state) as contribution_state, --01/29/2016 Added NVL
                       nvl(pe.position_type,' ') as recipient_job_position, 
                       nvl(pe.department_type, ' ') as recipient_department,
                       TRUNC(pr.date_created) purl_created_date,
                       NVL(pc.purl_contributor_count,0) purl_contributor_count,  
                       NVL(pcc.contributed_count,0)  contributed_count, 
                       NVL(pcc.posted_count,0)  contribution_posted_count,
                       mgr.first_name as manager_first_name,
                       mgr.last_name as manager_last_name, 
                       mgr.user_name as manager_user_name,
                       mgr.node_name as manager_node_name,
                       mgr.country_id as manager_country_id,
                       mgr.email_addr as manager_email_addr,
                       pr.invitation_start_date as date_invitation_sent 
                  FROM purl_recipient pr,
                       promotion promo,
                       --participant_employer pe,  --01/29/2016       --02/17/2016  --03/03/2017
                       vw_curr_pax_employer pe,    --01/29/2016   --02/17/2016    --03/03/2017
                       (SELECT pc.purl_recipient_id,
                               COUNT(DISTINCT pc.purl_contributor_id) purl_contributor_count
                        FROM purl_contributor pc
                        GROUP BY pc.purl_recipient_id 
                       ) pc,
                    (SELECT pc.purl_recipient_id,
                       COUNT(DISTINCT pcc.purl_contributor_id) as contributed_count,
                       COUNT(pcc.purl_contributor_id) as posted_count  
                    FROM 
                    purl_recipient pr,
                    purl_contributor pc,
                    purl_contributor_comment pcc
                    WHERE pr.purl_recipient_id = pc.purl_recipient_id AND
                          pc.purl_contributor_id = pcc.purl_contributor_id
                    GROUP BY pc.purl_recipient_id) pcc,
                    application_user au,
                    node n,
                    (select * from user_email_address where is_primary = 1) uea,
                    user_address ua,
                    (
                      SELECT n.node_id,  --user node_id
                             au.user_id,
                             au.last_name,
                             au.first_name,
                             au.user_name,
                             (SELECT name FROM node WHERE node_id = n.mgr_node_id) as node_name,
                             ua.country_id,
                             uea.email_addr
                        FROM user_node un,
                             -- node n,  --10/19/2016 Bug 69528 commented
                             (select a.node_id,a.name,fnc_get_manager_node(a.node_id) mgr_node_id from node a) n,  --10/19/2016 Bug 69528 added
                             application_user au,
                             user_address ua,
                             (select * from user_email_address uea where is_primary = 1) uea
                       WHERE un.node_id = n.mgr_node_id
                         AND un.user_id = au.user_id
                         AND un.status = 1
                         AND au.is_active = 1
                         AND un.role = 'own'
                         AND au.user_id = ua.user_id
                         AND ua.is_primary = 1 
                         AND au.user_id = uea.user_id (+) 
                     ) mgr,
                     (SELECT s.*
                        FROM
                        (SELECT purl_recipient_id,contribution_status, max(purl_status_order) as max_purl_status_order
                                    FROM 
                                    (SELECT pc.purl_recipient_id, 
                                           pc.state AS contribution_status, 
                                           CASE                                             
                                               WHEN pc.state = 'contribution'       --03/24/2016  Bug#66133
                                                      THEN 3
                                               WHEN pc.state = 'invitation'         --03/24/2016 Bug#66133
                                                      THEN 2
                                               WHEN pc.state = 'complete' 
                                                      THEN 1
                                               WHEN pc.state = 'expired' 
                                                      THEN 1
                                               WHEN pc.state = 'archived' 
                                                      THEN 1
                                           END as purl_status_order         
                                           FROM purl_contributor pc
                                                GROUP BY pc.purl_recipient_id,pc.state)
                            GROUP BY purl_recipient_id,contribution_status
                            ) s  WHERE s.max_purl_status_order = (SELECT MAX(pc.purl_status_order) FROM (SELECT pc.purl_recipient_id, 
                                                                                                       pc.state AS contribution_status, 
                                                                                                       CASE                                             
                                                                                                           WHEN pc.state = 'contribution'     --03/24/2016  Bug#66133
                                                                                                                  THEN 3
                                                                                                           WHEN pc.state = 'invitation'       --03/24/2016 Bug#66133
                                                                                                                  THEN 2
                                                                                                           WHEN pc.state = 'complete' 
                                                                                                                  THEN 1
                                                                                                           WHEN pc.state = 'expired' 
                                                                                                                  THEN 1
                                                                                                           WHEN pc.state = 'archived' 
                                                                                                                  THEN 1
                                                                                                       END as purl_status_order         
                                                                                                       FROM purl_contributor pc
                                                                                                            GROUP BY pc.purl_recipient_id,pc.state) pc where pc.purl_recipient_id = s.purl_recipient_id)
                        ) cps,
                        promo_merch_program_level pm         
                 WHERE pr.promotion_id      = promo.promotion_id
                   AND pr.user_id  = au.user_id
                   AND pr.user_id = ua.user_id (+)
                   AND ua.is_primary (+) = 1            --04/12/2016
                   AND pr.user_id = uea.user_id (+)
                   AND pr.node_id = n.node_id  
                   AND n.node_id  = mgr.node_id (+)       
                   AND pr.purl_recipient_id = pc.purl_recipient_id (+)
                   AND pr.purl_recipient_id = pcc.purl_recipient_id (+)
                   AND pr.purl_recipient_id = cps.purl_recipient_id (+)      
                   AND pr.award_level_id = pm.promo_merch_program_level_id (+)                    
                   AND promo.promotion_status IN ('live','expired')  
                   AND promo.is_deleted = 0                          
                   AND pr.user_id = pe.user_id(+) 
                   AND pe.termination_date IS NULL  --01/29/2016  --02/17/2016
--                   AND (                                                                                --03/14/2016
--                        pr.date_created > p_in_start_date  AND pr.date_created <= p_in_end_date
--                     OR pr.date_modified > p_in_start_date AND pr.date_modified <= p_in_end_date OR
--                     pe.date_created > p_in_start_date   AND pe.date_created <= p_in_end_date        
--                     OR pe.date_modified > p_in_start_date  AND pe.date_modified <= p_in_end_date OR
--                     au.date_modified > p_in_start_date  AND au.date_modified <= p_in_end_date   --01/29/2016
--                     )          
                 ORDER BY pr.user_id, pr.promotion_id ) s
            ON (d.purl_recipient_id = s.purl_recipient_id AND d.promotion_id = s.promotion_id AND d.node_id = s.node_id )
            WHEN MATCHED THEN UPDATE
            SET 
                recipient_user_id           = s.recipient_user_id, 
                recipient_email_addr        = s.recipient_email_addr, 
                recipient_pax_status        = s.recipient_pax_status, 
                recipient_job_position      = s.recipient_job_position, 
                recipient_department        = s.recipient_department, 
                award_date                  = s.award_date, 
                award_amount                = s.award_amount, 
                contribution_state          = s.contribution_state,
                purl_contributor_count      = s.purl_contributor_count,
                contributed_count           = s.contributed_count,
                contribution_posted_count   = s.contribution_posted_count, 
                manager_node_name           = s.manager_node_name, 
                manager_last_name           = s.manager_last_name, 
                manager_first_name          = s.manager_first_name, 
                manager_user_name           = s.manager_user_name, 
                manager_email_addr          = s.manager_email_addr, 
                manager_country_id          = s.manager_country_id, 
                date_modified               = SYSDATE, 
                modified_by                 = 0
       WHERE ( -- only update records with different values
                DECODE(d.recipient_user_id,       s.recipient_user_id,           1, 0) = 0              
             OR DECODE(d.recipient_email_addr,    s.recipient_email_addr,        1, 0) = 0
             OR DECODE(d.recipient_pax_status,    s.recipient_pax_status,        1, 0) = 0        
             OR DECODE(d.recipient_job_position,  s.recipient_job_position,      1, 0) = 0
             OR DECODE(d.recipient_department,    s.recipient_department,        1, 0) = 0        
             OR DECODE(d.award_date,              s.award_date,                  1, 0) = 0
             OR DECODE(d.award_amount,            s.award_amount,                1, 0) = 0        
             OR DECODE(d.contribution_state,      s.contribution_state,          1, 0) = 0
             OR DECODE(d.purl_contributor_count,  s.purl_contributor_count,      1, 0) = 0
             OR DECODE(d.contributed_count,       s.contributed_count,           1, 0) = 0        
             OR DECODE(d.contribution_posted_count, s.contribution_posted_count, 1, 0) = 0
             OR DECODE(d.manager_node_name,       s.manager_node_name,           1, 0) = 0        
             OR DECODE(d.manager_last_name,       s.manager_last_name,           1, 0) = 0
             OR DECODE(d.manager_first_name,      s.manager_first_name,          1, 0) = 0        
             OR DECODE(d.manager_user_name,       s.manager_user_name,           1, 0) = 0        
             OR DECODE(d.manager_email_addr,      s.manager_email_addr,          1, 0) = 0
             OR DECODE(d.manager_country_id,      s.manager_country_id,          1, 0) = 0        
             )     
            WHEN NOT MATCHED THEN
            INSERT
            (   rpt_purl_cntr_detl_id, 
                promotion_id, 
                promotion_name, 
                node_id, 
                node_name, 
                purl_recipient_id,
                recipient_user_id, 
                recipient_country_id, 
                recipient_last_name, 
                recipient_first_name, 
                recipient_user_name, 
                recipient_email_addr, 
                recipient_pax_status, 
                recipient_job_position, 
                recipient_department, 
                award_date, 
                award_amount, 
                contribution_state,
                purl_contributor_count,
                contributed_count,
                contribution_posted_count, 
                date_invitation_sent, 
                manager_node_name, 
                manager_last_name, 
                manager_first_name, 
                manager_user_name, 
                manager_email_addr, 
                manager_country_id, 
                purl_created_date, 
                date_created, 
                created_by, 
                date_modified, 
                modified_by 

          )
VALUES
      (         rpt_purl_rec_detl_pk_sq.nextval,
                s.promotion_id, 
                s.promotion_name, 
                s.node_id, 
                s.node_name, 
                s.purl_recipient_id,
                s.recipient_user_id, 
                s.recipient_country_id, 
                s.recipient_last_name, 
                s.recipient_first_name, 
                s.recipient_user_name, 
                s.recipient_email_addr, 
                s.recipient_pax_status, 
                s.recipient_job_position, 
                s.recipient_department, 
                s.award_date, 
                s.award_amount, 
                s.contribution_state,
                s.purl_contributor_count,
                s.contributed_count,
                s.contribution_posted_count, 
                s.date_invitation_sent, 
                s.manager_node_name, 
                s.manager_last_name, 
                s.manager_first_name, 
                s.manager_user_name, 
                s.manager_email_addr, 
                s.manager_country_id, 
                s.purl_created_date, 
                SYSDATE, 
                0, 
                NULL, 
                NULL 
      );

       v_rec_cnt := SQL%ROWCOUNT;
       v_stage := 'End of Merge for Checkin :';
       prc_execution_log_entry(c_process_name, 1, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
       -- 08/24/2017 G6-2870       
       v_stage := 'Update recently inactivated pax contributions to expired';
       UPDATE rpt_purl_recipient_detail c 
          SET c.recipient_pax_status='inactive',c.contribution_state =  'expired',c.date_modified = SYSDATE,c.modified_by = 0
        WHERE c.contribution_state in ('invitation','contribution')
          AND EXISTS(SELECT '1' 
                       FROM application_user b
                      WHERE c.recipient_user_id = b.user_id
                        AND b.is_active = 0 
                        AND b.date_modified BETWEEN p_in_start_date AND p_in_end_date);   
                              
       v_rec_cnt := SQL%ROWCOUNT;
       v_stage := 'End of update for inactive pax :';
       prc_execution_log_entry(c_process_name, 1, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);  
     
       p_out_return_code :=0;

  commit;
  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_out_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := 99;
    p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;    
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', p_out_error_message, NULL);


END P_RPT_PURL_CONTRIBUTION_DETAIL;

PROCEDURE p_rpt_recognition_detail_sa (
                                   p_in_requested_user_id      IN  NUMBER,
                                   p_in_start_date             IN  DATE,
                                   p_in_end_date               IN  DATE,
                                   p_out_return_code           OUT NUMBER,
                                   p_out_error_message         OUT VARCHAR2)
IS
/*******************************************************************************
  Purpose:  Populate the rpt_recognition_detail reporting tables
  Person             Date           Comments
  -----------     ----------   ------------------------------------------------
  Suresh J        04/01/2019   SA Integeration with DayMaker changes for reports              
*******************************************************************************/
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_recognition_detail_sa');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
   c_created_by         CONSTANT rpt_recognition_detail.created_by%TYPE:= p_in_requested_user_id;

   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
   v_tab_node_id        dbms_sql.number_table;
   v_tab_node_name      dbms_sql.varchar2_table;   
 
     CURSOR cur_sweep_activity  
     IS     
      SELECT
        un.node_id as node_id,
        a.activity_id, 
        a.date_created as award_date,
        n.name as node_name,
        p.promotion_name as promotion_name,
        nvl(p.promotion_id,0) as promotion_id,
        nvl(p.award_type,' ') as award_type,
        p.is_taxable as is_taxable,
        a.quantity as points,
        ua.country_id as country_id,
        au.user_id as participant_id,
        au.first_name as participant_first_name,
        au.middle_name as participant_middle_name,
        au.last_name as participant_last_name,
        au.user_name as participant_login_id,  
                nvl(DECODE(au.is_active,1,'active',0,'inactive'),' ') participant_status,
        nvl(pe.position_type,' ') as job_title,
        nvl(pe.department_type, ' ') as department,
        DECODE (a.activity_discrim,'sweep',1,'sweeplevel',1,0) AS sweepstakes_won
     FROM activity a,
          node n,
          promotion p,
          promo_recognition pq,
          application_user au,
          user_node un,
          user_address ua,
          participant pax,
          participant_employer pe
      WHERE 
         a.activity_discrim  IN ('sweep','sweeplevel') 
        AND a.user_id =un.user_id
        AND a.user_id =au.user_id
        AND un.is_primary =1 
        AND un.node_id = n.node_id
        AND a.promotion_id = p.promotion_id
        AND p.promotion_id = pq.promotion_id
        AND au.user_id = pax.user_id
        AND au.user_id = ua.user_id
        AND pax.user_id = pe.user_id(+)
        AND pe.termination_date IS NULL
        AND p.promotion_status IN ('live','expired')            
        AND p.is_deleted = 0      
        AND (p_in_start_date   <  a.date_created   AND a.date_created <= p_in_end_date);
        
--Cursor to pick modified node name
  CURSOR cur_node_changed IS  
    SELECT node_id,
           name
      FROM node
     WHERE date_modified BETWEEN p_in_start_date AND p_in_end_date;  
 
  --Cursor hierarchy
  CURSOR cur_hier (p_in_node_id rpt_hierarchy.node_id%TYPE) IS
    SELECT parent_node_id header_node_id,
           node_id,
           hier_level,
           is_leaf
      FROM rpt_hierarchy
     START WITH node_id = p_in_node_id
   CONNECT BY PRIOR parent_node_id = node_id;     

BEGIN
    v_stage := 'Start';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
    
  FOR rec_sweep_activity IN cur_sweep_activity
    LOOP
    INSERT INTO rpt_recognition_detail 
                       (rpt_recognition_detail_id,
                        recvr_activity_id,
                        promotion_id,
                        promotion_name,                      
                        recvr_node_id,
                        recvr_node_name,
                  --      is_open,
                        award_type,
                        award_amt,
                        date_approved,
                        date_submitted,
                        recvr_login_id, 
                        recvr_user_id,
                        recvr_country_id,
                        recvr_first_name,
                        recvr_middle_name,
                        recvr_full_name,
                        recvr_last_name,
                        recvr_pax_status,                      
                        recvr_job_position,
                        recvr_department,
                        date_created,
                        created_by,      
                        version,               
                        recvr_sweepstakes_won)
                VALUES (rpt_recognition_detail_pk_sq.NEXTVAL,
                        rec_sweep_activity.activity_id,
                        rec_sweep_activity.promotion_id,
                        rec_sweep_activity.promotion_name,
                        rec_sweep_activity.node_id,
                        rec_sweep_activity.node_name,
                   --     0,
                        rec_sweep_activity.award_type,
                        rec_sweep_activity.points,
                        rec_sweep_activity.award_date,
                        rec_sweep_activity.award_date,
                        rec_sweep_activity.participant_login_id,  
                        rec_sweep_activity.participant_id,
                        rec_sweep_activity.country_id,
                        rec_sweep_activity.participant_first_name,
                        rec_sweep_activity.participant_middle_name,
                        fnc_format_user_name(rec_sweep_activity.participant_last_name, rec_sweep_activity.participant_first_name, rec_sweep_activity.participant_middle_name, NULL), 
                        rec_sweep_activity.participant_last_name,
                        rec_sweep_activity.participant_status,                     
                        rec_sweep_activity.job_title,
                        rec_sweep_activity.department,
                        SYSDATE,
                        p_in_requested_user_id,
                        1,
                        rec_sweep_activity.sweepstakes_won);
    
    
    END LOOP;

--------------------------------------------------------------------------------
  DELETE gtt_rpt_badge_count; 
	INSERT INTO gtt_rpt_badge_count 
	(user_id, promo_type, date_created, badge_rule_count)
	SELECT pb.participant_id, p.promotion_type, TRUNC(pb.date_created), COUNT(DISTINCT pb.badge_rule_id)
	FROM 	participant_badge pb,
	      badge_promotion bp,
	      promotion P
	WHERE pb.promotion_id = bp.promotion_id
	AND   bp.eligible_promotion_id = p.promotion_id
	AND   p.promotion_type = 'recognition'
	AND   TRUNC(pb.date_created) BETWEEN TRUNC(p_in_start_date) AND p_in_end_date -- TRUNCs to make sure full day is counted
	GROUP BY pb.participant_id, p.promotion_type, TRUNC(pb.date_created);
--------------------------------------------------------------------------------
	
   -- rebuild detail record
   v_stage := 'INSERT/UPDATE rpt_recognition_detail';
 
      MERGE INTO rpt_recognition_detail d    
     USING ( 
              WITH detail_recog AS          
              (SELECT -- rpt_recognition_detail_pk_sq.NEXTVAL, 
                     d.giver_user_id,
                     d.giver_node_id,
                     d.giver_country_id,
                     d.giver_node_name,
                     d.giver_last_name,
                     d.giver_first_name,
                     d.giver_middle_name,
                     d.giver_full_name,
                     NVL(d.giver_pax_status, ' ') AS giver_pax_status,
                     d.giver_department AS giver_department,
                     d.giver_job_position AS giver_job_position,
                     d.giver_login_id,
                     d.giver_activity_id,
                     d.recvr_user_id,
                     d.recvr_node_id,
                     d.recvr_country_id,
                     d.recvr_node_name,
                     d.recvr_last_name,
                     d.is_reverse,
                     d.recvr_first_name,
                     d.recvr_middle_name,
                     d.recvr_full_name,
                     d.recvr_badges_earned,
                     NVL(d.recvr_pax_status, ' ') AS recvr_pax_status,
                     d.recvr_department AS recvr_department,
                     d.recvr_job_position AS recvr_job_position,
                     d.recvr_login_id,
                     d.recvr_activity_id,
                     d.claim_id,
                     d.claim_recipient_id,
                     d.promotion_id,
                     d.promotion_name,
                     NVL(d.behavior, ' ') AS behavior,
                     NVL(d.award_type, ' ') AS award_type,
                     d.plateau_earned,
                     d.sweeptakes_won,
                     d.trans_date,
                     d.award_amt,
                     d.merchandise_description,
                     d.date_submitted,
                     d.date_approved,
                     d.final_approver_id,
                     d.final_approver_name,
                     d.reason_denied,       
                     d.proxy_user_id,
                     d.proxy_user_name,
                     d.submitter_comments   
                FROM ( -- build detail records
                       SELECT -- rpt_recognition_detail_id,
                              COUNT(*) OVER() AS rec_cnt,  
                              -- giver data
                              cc.giver_user_id,
                              cc.giver_node_id,
                              au_gu.country_id AS giver_country_id,
                              n_g.name                  AS giver_node_name,
                              INITCAP(au_gu.last_name)  AS giver_last_name,
                              INITCAP(au_gu.first_name) AS giver_first_name,
                              TRIM(au_gu.middle_name)   AS giver_middle_name,
                              fnc_format_user_name(au_gu.last_name, au_gu.first_name, au_gu.middle_name, au_gu.suffix) AS giver_full_name,
                              DECODE(au_gu.is_active, 1, 'active', 'inactive') AS giver_pax_status,
                              cpe_g.department_type     AS giver_department,
                              cpe_g.position_type       AS giver_job_position,
                              au_gu.user_name           AS giver_login_id,
                              a_g.activity_id           AS giver_activity_id,
                              -- receiver data
                              -- non-pax merchandise overrides recipient
                              cc.recvr_user_id   AS recvr_user_id,   
                              cc.recvr_node_id   AS recvr_node_id,
                              ua_ru.country_id AS recvr_country_id,
                              n_r.name           AS recvr_node_name,
                              INITCAP(au_ru.last_name)  AS recvr_last_name,
                              INITCAP(au_ru.first_name) AS recvr_first_name,
                              TRIM(au_ru.middle_name)                AS recvr_middle_name,
                              fnc_format_user_name(au_ru.last_name, au_ru.first_name, au_ru.middle_name, au_ru.suffix) AS recvr_full_name,
                              fnc_get_rpt_badge_count (au_ru.user_id,'recognition',TRUNC(cc.trans_date)) AS recvr_badges_earned, --06/16/2017
                              DECODE(au_ru.is_active, 1, 'active', 'inactive') AS recvr_pax_status,
                              cpe_r.department_type                AS recvr_department,
                             cpe_r.position_type                AS recvr_job_position,  --12/12/2012
                              au_ru.user_name                      AS recvr_login_id,
                              cc.recvr_activity_id,
                              -- recognition data
                              cc.claim_id,
                              cc.claim_recipient_id,
                              cc.promotion_id,
                              cc.is_reverse,
                              cc.promotion_name,
--                            ccv_b.cms_name AS behavior,      
                              cc.behavior,                            
                              cc.award_type,
                              cc.plateau_earned,
                              cc.sweeptakes_won,
                              cc.trans_date,
                              cc.award_amt,
                              cc.merchandise_description,
                              cc.date_submitted,
                              cc.date_approved,
                              cc.final_approver_id,
                              fnc_format_user_name(au_fa.last_name, au_fa.first_name, au_fa.middle_name, au_fa.suffix) AS final_approver_name,
                              cc.approval_option_reason_type AS reason_denied,  
                              cc.proxy_user_id,
                              cc.submitter_comments,   
                              fnc_format_user_name(au_pu.last_name, au_pu.first_name, au_pu.middle_name, au_pu.suffix) AS proxy_user_name
                         FROM ( -- match claims with receipients
                                SELECT mc.*,
                                       p.award_type,
                                       p.promotion_name,
                                       cr.participant_id   AS recvr_user_id,
                                      -- ua_r.country_id     AS recvr_country_id,
                                       cr.claim_item_id    AS claim_recipient_id,
                                       cr.node_id          AS recvr_node_id,
                                       a_r.quantity        AS award_amt,
                                       a_r.submission_date AS trans_date,
--                                       a_r.submission_date AS date_approved,
                                       ci_r.date_approved AS date_approved,
                                       a_r.activity_id     AS recvr_activity_id,
                                       DECODE(p.award_type,
                                         'merchandise', NVL(mo.product_description, pmpl.cm_asset_key),
                                         NULL
                                       ) AS merchandise_description,
                                       DECODE(p.award_type,
                                         'merchandise', 1, NULL) AS plateau_earned,
                                       DECODE(ci_r.approval_status_type, 
                                         'winner', 1, NULL) AS sweeptakes_won,
                                      INITCAP(au.first_name) AS non_pax_first_name,
                                       INITCAP(au.last_name)  AS non_pax_last_name,
                                       fnc_format_user_name(au.last_name, au.first_name) AS non_pax_full_name
                                  FROM ( -- get claim with create dates ranked in descending order
                                         -- lowest rank is the most recently approved
                                         SELECT RANK() OVER (PARTITION BY cia.claim_item_id ORDER BY cia.date_created DESC, CLAIM_ITEM_APPROVER_ID DESC) AS approved_rank,
                                                c.claim_id,
                                                c.submitter_id AS giver_user_id,
                                                c.promotion_id,
                                                c.submission_date AS date_submitted,
--                                                rc.behavior,
                                                case when  rc.behavior = 'none'    
                                                then null
                                                else  rc.behavior
                                                end as behavior,                   
                                                rc.is_reverse,
                                                rc.submitter_comments,   
                                                c.proxy_user_id,
                                                cia.approver_user_id AS final_approver_id,
                                                cia.approval_option_reason_type,     
                                                c.node_id AS giver_node_id
                                           FROM claim c,
                                                recognition_claim rc,
                                                claim_item ci,
                                                claim_item_approver cia
                                          WHERE c.claim_id = rc.claim_id
                                            AND c.claim_id = ci.claim_id
                                            --AND rc.is_reverse <> 1
                                            AND c.is_open = 0    -- to narrow down claims that are approved
                                            AND ci.claim_item_id = cia.claim_item_id (+)
                                            AND ((c.date_created > p_in_start_date  AND c.date_created <= p_in_end_date) 
                                                 OR (c.date_modified  > p_in_start_date AND c.date_modified  <= p_in_end_date)
                                                 OR (ci.date_created  > p_in_start_date  AND ci.date_created <= p_in_end_date)
                                                 OR (ci.date_modified > p_in_start_date AND ci.date_modified <= p_in_end_date)
                                                 OR (cia.date_created > p_in_start_date  AND cia.date_created  <= p_in_end_date)
                                                 OR (cia.date_modified >p_in_start_date  AND cia.date_modified <= p_in_end_date))
                                       ) mc,
                                       promotion p,
                                       -- receiver tables
                                       claim_item ci_r,
                                       claim_recipient cr,
                                     --  user_address ua_r,
                                       activity a_r, -- receiver
                                       merch_order mo,
                                       application_user au,-- Added this to get the pax details as we removed non pax data from merch_order table in G5
                                       promo_merch_program_level pmpl
                                    -- restrict to most recent approval create date                              
                                 WHERE mc.approved_rank = 1
                                   AND mc.promotion_id  = p.promotion_id
                                   AND p.promotion_status IN ('live','expired')  
                                   AND p.is_deleted = 0      
                                   AND NOT EXISTS (SELECT r.promotion_id            --04/01/2019
                                                                        FROM promo_recognition r,
                                                                             promotion pp   
                                                                        WHERE r.promotion_id = pp.promotion_id AND
                                                                              r.promotion_id = p.promotion_id  AND
                                                                              --pp.award_type = 'merchandise' AND 
                                                                              (r.is_include_purl = 1 OR r.include_celebrations = 1))                                                       
                                   AND mc.claim_id  = ci_r.claim_id
                                   AND ci_r.claim_item_id = cr.claim_item_id
                                   AND ci_r.claim_id = a_r.claim_id  
                                   AND NVL(cr.participant_id, 0) = NVL(a_r.user_id , 0)                                                                               
                                   AND a_r.is_submitter = 0                  
                                    -- include non-pax info from merch_order 
                                   AND a_r.merch_order_id = mo.merch_order_id (+)
                                   AND mo.participant_id=au.user_id(+)
                                  -- AND au.user_id=ua_r.user_id
                                   AND mo.promo_merch_program_level_id = pmpl.promo_merch_program_level_id (+)
                                   AND ((ci_r.date_created > p_in_start_date    AND ci_r.date_created <= p_in_end_date) 
                                       OR (ci_r.date_modified > p_in_start_date AND ci_r.date_modified <= p_in_end_date)
                                       OR (a_r.date_created > p_in_start_date   AND a_r.date_created  <= p_in_end_date)
                                       OR (a_r.date_modified > p_in_start_date  AND a_r.date_modified <= p_in_end_date)
                                       OR (p.date_created > p_in_start_date     AND p.date_created    <= p_in_end_date)
                                       OR (p.date_modified > p_in_start_date    AND p.date_modified   <= p_in_end_date))
                                UNION --Query where is_submitter <> 0 
                                   SELECT mc.*,  
                                       p.award_type,
                                       p.promotion_name,
                                       cr.participant_id   AS recvr_user_id,
                                      -- ua_r.country_id     AS recvr_country_id,
                                       cr.claim_item_id    AS claim_recipient_id,
                                       cr.node_id          AS recvr_node_id,
                                       0                   AS award_amt,
                                       NULL AS trans_date,
--                                       a_r.submission_date AS date_approved,
                                       ci_r.date_approved AS date_approved,
                                       NULL     AS recvr_activity_id,
                                       NULL merchandise_description,
                                       DECODE(p.award_type,
                                         'merchandise', 1, NULL) AS plateau_earned,
                                       DECODE(ci_r.approval_status_type, 
                                         'winner', 1, NULL) AS sweeptakes_won,
                                      NULL AS non_pax_first_name,
                                       NULL AS non_pax_last_name,
                                       NULL AS non_pax_full_name
                                  FROM ( -- get claim with create dates ranked in descending order
                                         -- lowest rank is the most recently approved
                                         SELECT RANK() OVER (PARTITION BY cia.claim_item_id ORDER BY cia.date_created DESC, CLAIM_ITEM_APPROVER_ID DESC) AS approved_rank,
                                                c.claim_id,
                                                c.submitter_id AS giver_user_id,
                                                c.promotion_id,
                                                c.submission_date AS date_submitted,
--                                                rc.behavior,
                                                case when  rc.behavior = 'none'    
                                                then null
                                                else  rc.behavior
                                                end as behavior,                   
                                                rc.is_reverse,
                                                rc.submitter_comments,   
                                                c.proxy_user_id,
                                                cia.approver_user_id AS final_approver_id,
                                                cia.approval_option_reason_type,     
                                                c.node_id AS giver_node_id
                                           FROM claim c,
                                                recognition_claim rc,
                                                claim_item ci,
                                                claim_item_approver cia
                                          WHERE c.claim_id = rc.claim_id
                                            AND c.claim_id = ci.claim_id
                                            --AND rc.is_reverse <> 1
                                            AND c.is_open = 0    -- to narrow down claims that are approved
                                            AND ci.claim_item_id = cia.claim_item_id (+)
                                            AND ((c.date_created > p_in_start_date  AND c.date_created <= p_in_end_date) 
                                                 OR (c.date_modified  > p_in_start_date AND c.date_modified  <= p_in_end_date)
                                                 OR (ci.date_created  > p_in_start_date  AND ci.date_created <= p_in_end_date)
                                                 OR (ci.date_modified > p_in_start_date AND ci.date_modified <= p_in_end_date)
                                                 OR (cia.date_created > p_in_start_date  AND cia.date_created  <= p_in_end_date)
                                                 OR (cia.date_modified >p_in_start_date  AND cia.date_modified <= p_in_end_date))
                                       ) mc,
                                       promotion p,
                                       -- receiver tables
                                       claim_item ci_r,
                                       claim_recipient cr
                                    -- restrict to most recent approval create date                              
                                 WHERE mc.approved_rank = 1
                                   AND mc.promotion_id  = p.promotion_id
                                   AND p.promotion_status IN ('live','expired')  
                                   AND p.is_deleted = 0         
                                   AND NOT EXISTS (SELECT r.promotion_id            --04/01/2019
                                                                        FROM promo_recognition r,
                                                                             promotion pp   
                                                                        WHERE r.promotion_id = pp.promotion_id AND
                                                                              r.promotion_id = p.promotion_id  AND
                                                                              --pp.award_type = 'merchandise' AND 
                                                                              (r.is_include_purl = 1 OR r.include_celebrations = 1))                                                       
                                   AND mc.claim_id  = ci_r.claim_id
                                   AND ci_r.claim_item_id = cr.claim_item_id  
                                   AND  NOT EXISTS (SELECT 1 FROM activity a where a.claim_id = mc.claim_id AND a.is_submitter = 0)
                                   AND ((ci_r.date_created > p_in_start_date    AND ci_r.date_created <= p_in_end_date) 
                                       OR (ci_r.date_modified > p_in_start_date AND ci_r.date_modified <= p_in_end_date)
                                       OR (p.date_created > p_in_start_date     AND p.date_created    <= p_in_end_date)
                                       OR (p.date_modified > p_in_start_date    AND p.date_modified   <= p_in_end_date))
                              ) cc,
                              -- reference data tables
                              activity a_g, -- giver
                              node n_g,     -- giver
                              node n_r,     -- receiver
                              --application_user au_gu,   -- giver user
                              --user_address ua_gu,
                              (select au_gu.*,
                                ua_gu.country_id
                                from
                                application_user au_gu,   -- giver user
                                user_address ua_gu
                                where au_gu.user_id = ua_gu.user_id AND ua_gu.is_primary = 1 ) au_gu,                                     
                              application_user au_ru,   -- receiver user
                              user_address ua_ru,
                              application_user au_pu,   -- proxy user
                              application_user au_fa,   -- final approver
--                              vw_cms_code_value ccv_b,  -- behavior     
                              vw_curr_pax_employer cpe_g, -- giver
                              vw_curr_pax_employer cpe_r  -- receiver                                                               
                           -- outer join activity for giver data
                        WHERE cc.claim_id = a_g.claim_id (+)
                          AND 1 = a_g.is_submitter (+)
                           -- outer join node to get node names
                          AND cc.giver_node_id = n_g.node_id (+)
                          AND cc.recvr_node_id = n_r.node_id (+)
                           -- outer join user ID's to get user names and employment
                          AND cc.giver_user_id = au_gu.user_id (+)
                          --AND au_gu.user_id = ua_gu.user_id       --04/01/2019
                          --AND ua_gu.is_primary = 1                --04/01/2019          
                          AND cc.giver_user_id = cpe_g.user_id (+)
                          AND cc.recvr_user_id = au_ru.user_id (+)
                          AND au_ru.user_id = ua_ru.user_id                 
                          AND ua_ru.is_primary = 1                          
                          AND cc.recvr_user_id = cpe_r.user_id (+)                                                    
                          AND cc.proxy_user_id = au_pu.user_id (+)
                          AND cc.final_approver_id = au_fa.user_id (+)
                          AND ((a_g.date_created > p_in_start_date    AND a_g.date_created <= p_in_end_date) 
                               OR (a_g.date_modified > p_in_start_date AND a_g.date_modified <= p_in_end_date)
                               OR (n_g.date_created > p_in_start_date   AND n_g.date_created  <= p_in_end_date)
                               OR (n_g.date_modified > p_in_start_date  AND n_g.date_modified <= p_in_end_date)
                               OR (n_r.date_created > p_in_start_date   AND n_r.date_created  <= p_in_end_date)
                               OR (n_r.date_modified > p_in_start_date  AND n_r.date_modified <= p_in_end_date)
                               OR (au_gu.date_created > p_in_start_date   AND au_gu.date_created  <= p_in_end_date)
                               OR (au_gu.date_modified > p_in_start_date  AND au_gu.date_modified <= p_in_end_date)
                               OR (au_ru.date_created > p_in_start_date     AND au_ru.date_created    <= p_in_end_date)
                               OR (au_ru.date_modified > p_in_start_date    AND au_ru.date_modified   <= p_in_end_date)
                               OR (au_pu.date_created > p_in_start_date     AND au_pu.date_created    <= p_in_end_date)
                               OR (au_pu.date_modified > p_in_start_date    AND au_pu.date_modified   <= p_in_end_date)
                               OR (au_fa.date_created > p_in_start_date     AND au_fa.date_created    <= p_in_end_date)
                               OR (au_fa.date_modified > p_in_start_date    AND au_fa.date_modified   <= p_in_end_date)
                               OR (cpe_g.date_created > p_in_start_date     AND cpe_g.date_created    <= p_in_end_date)
                               OR (cpe_g.date_modified > p_in_start_date    AND cpe_g.date_modified   <= p_in_end_date)
                               OR (cpe_r.date_created > p_in_start_date     AND cpe_r.date_created    <= p_in_end_date)
                               OR (cpe_r.date_modified > p_in_start_date    AND cpe_r.date_modified   <= p_in_end_date)) 
                     ) d
          )
             SELECT es.s_rowid,
                    --dds.hier_level, 0) || '-' || dds.sum_type AS record_type,                    
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.rowid AS s_rowid,
                            s2.claim_id
                       FROM rpt_recognition_detail s2                      
                   ) es
                   -- right outer join so unmatched existing summaries can be deleted
                   RIGHT OUTER JOIN  detail_recog dds
                   ON ( es.claim_id = dds.claim_id )
       )s
       ON (d.rowid = s.s_rowid)
     WHEN MATCHED THEN
     UPDATE SET
        d.giver_user_id    =    s.giver_user_id,
        d.giver_node_id    =    s.giver_node_id,
        d.giver_country_id =    s.giver_country_id,
        d.giver_node_name  =    s.giver_node_name,
        d.giver_last_name  =    s.giver_last_name,
        d.giver_first_name =    s.giver_first_name,
        d.giver_middle_name  =    s.giver_middle_name,
        d.giver_full_name    =    s.giver_full_name,
        d.giver_pax_status   =    s. giver_pax_status,
        d.giver_department   =    s.giver_department,
        d.giver_job_position =    s.giver_job_position,
        d.giver_login_id     =    s.giver_login_id,
        d.giver_activity_id  =    s.giver_activity_id,
        d.recvr_user_id      =    s.recvr_user_id,
        d.recvr_node_id      =    s.recvr_node_id,
        d.recvr_country_id      =    s.recvr_country_id,
        d.recvr_node_name    =    s.recvr_node_name,
        d.recvr_last_name    =    s.recvr_last_name,
        d.recvr_first_name   =    s.recvr_first_name,
        d.recvr_middle_name  =    s.recvr_middle_name,
        d.recvr_full_name    =    s.recvr_full_name,
        d.recvr_badges_earned = s.recvr_badges_earned,
        d.recvr_pax_status   =    s.recvr_pax_status,
        d.recvr_department   =    s.recvr_department,
        d.recvr_job_position =    s.recvr_job_position,
        d.recvr_login_id     =    s.recvr_login_id,
        d.recvr_activity_id  =    s.recvr_activity_id,        
        d.claim_recipient_id =    s.claim_recipient_id,
        d.promotion_id       =    s.promotion_id,
        d.promotion_name     =    s.promotion_name,
        d.behavior           =    s.behavior,
        d.award_type         =    s.award_type,
        d.recvr_plateau_earned   =    s.plateau_earned,
        d.recvr_sweepstakes_won  =    s.sweeptakes_won,
        d.trans_date         =    s.trans_date,
        d.award_amt          =    s.award_amt,
        d.merchandise_description    =    s.merchandise_description,
        d.date_submitted     =    s.date_submitted,
        d.date_approved      =    s.date_approved,
        d.final_approver_id  =    s.final_approver_id,
        d.final_approver_name =    s.final_approver_name,
        d.reason_denied       =    s.reason_denied,        
        d.proxy_user_id      =  s.proxy_user_id,
        d.proxy_user_name    =  s.proxy_user_name,
        d.submitter_comments = s.submitter_comments,  
        d.date_modified      =  SYSDATE,
        d.modified_by        =  c_created_by
        WHEN NOT MATCHED THEN
   INSERT 
     (rpt_recognition_detail_id,
      giver_user_id,
      giver_node_id,
      giver_country_id,
      giver_node_name,
      giver_last_name,
      giver_first_name,
      giver_middle_name,
      giver_full_name,
      giver_pax_status,
      giver_department,
      giver_job_position,
      giver_login_id,
      giver_activity_id,
      recvr_user_id,
      recvr_node_id,
      recvr_country_id,
      recvr_node_name,
      recvr_last_name,
      recvr_first_name,
      recvr_middle_name,
      recvr_full_name,
      recvr_pax_status,
      recvr_department,
      recvr_job_position,
      recvr_login_id,
      recvr_activity_id,     
      claim_id,
      claim_recipient_id,
      promotion_id,
      promotion_name,
      behavior,
      award_type,
      trans_date,
      award_amt,
      merchandise_description,
      date_submitted,
      date_approved,
      final_approver_id,
      final_approver_name,
      reason_denied,         
      proxy_user_id,
      proxy_user_name,
      recvr_points,
      recvr_plateau_earned,
      recvr_sweepstakes_won,
      recvr_badges_earned,
      submitter_comments, 
      created_by,
      date_created,
      version)  
     VALUES
     ( rpt_recognition_detail_pk_sq.NEXTVAL,
        s.giver_user_id,
        s.giver_node_id,
        s.giver_country_id,
        s.giver_node_name,
        s.giver_last_name,
        s.giver_first_name,
        s.giver_middle_name,
        s.giver_full_name,
        s.giver_pax_status,
        s.giver_department,
        s.giver_job_position,
        s.giver_login_id,
        s.giver_activity_id,
        s.recvr_user_id,
        s.recvr_node_id,
        s.recvr_country_id,
        s.recvr_node_name,
        s.recvr_last_name,
        s.recvr_first_name,
        s.recvr_middle_name,
        s.recvr_full_name,
        s.recvr_pax_status,
        s.recvr_department,
        s.recvr_job_position,
        s.recvr_login_id,
        s.recvr_activity_id,
        s.claim_id,
        s.claim_recipient_id,
        s.promotion_id,
        s.promotion_name,
        s.behavior,
        s.award_type,       
        s.trans_date,
        s.award_amt,
        s.merchandise_description,
        s.date_submitted,
        s.date_approved,
        s.final_approver_id,
        s.final_approver_name,
        s.reason_denied,                      
        s.proxy_user_id,
        s.proxy_user_name,
        s.award_amt,
        s.plateau_earned,
        s.sweeptakes_won,
        s.recvr_badges_earned,
        s.submitter_comments, 
        c_created_by,
        SYSDATE,
        0)  WHERE s.is_reverse =0 ;       
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   
   v_stage := 'Delete reversed recognitions';
   
   DELETE FROM rpt_recognition_detail
   WHERE claim_id in (select claim_id from recognition_claim where is_reverse =1);
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   
   v_stage := 'Giver rpt_recognition_detail';               
  MERGE INTO rpt_recognition_detail dtl                 
  USING ( 
    SELECT rpt.ROWID row_id,
           fnc_get_user_name(au.user_id) full_name,
           au.last_name,
           au.first_name,
           au.middle_name,
           DECODE(au.is_active,1,'active','inactive') pax_status,
           vcpe.department_type,
           vcpe.position_type
      FROM application_user au,
           rpt_recognition_detail rpt,
           vw_curr_pax_employer vcpe
     WHERE au.user_id   = rpt.giver_user_id
       AND rpt.giver_user_id= vcpe.user_id(+)
       AND (p_in_start_date < au.date_created AND au.date_created <= p_in_end_date
            OR p_in_start_date < au.date_modified  AND  au.date_modified <= p_in_end_date
            OR p_in_start_date < vcpe.date_created AND vcpe.date_created <= p_in_end_date
            OR p_in_start_date < vcpe.date_modified AND vcpe.date_modified <= p_in_end_date)
       AND (au.last_name            <> rpt.giver_last_name
            OR au.first_name        <> rpt.giver_first_name
            OR au.middle_name       <> rpt.giver_middle_name
            OR DECODE(au.is_active,1,'active','inactive') <> rpt.giver_pax_status
            OR NVL(vcpe.department_type,'X') <> NVL(rpt.giver_department,'X')       
            OR NVL(vcpe.position_type,'X')   <> NVL(rpt.giver_job_position,'X'))) e 
    ON (dtl.rowid = e.row_id)
   WHEN MATCHED THEN 
     UPDATE SET 
        dtl.giver_last_name    = e.last_name
        ,dtl.giver_first_name   = e.first_name
        ,dtl.giver_middle_name  = e.middle_name
        ,dtl.giver_full_name    = e.full_name 
        ,dtl.giver_pax_status   = e.pax_status
        ,dtl.giver_department   = e.department_type
        ,dtl.giver_job_position = e.position_type
        ,dtl.date_modified      =  SYSDATE
        ,dtl.modified_by        =  c_created_by;
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  
  v_stage := 'Receiver rpt_recognition_detail';       
  MERGE INTO RPT_RECOGNITION_DETAIL dtl             
  USING ( 
    SELECT rpt.ROWID row_id,
           fnc_get_user_name(au.user_id) full_name,
           au.last_name,
           au.first_name,
           au.middle_name,
           DECODE(au.is_active,1,'active','inactive') pax_status,
           vcpe.department_type,
           vcpe.position_type
      FROM application_user au,
           rpt_recognition_detail rpt,
           vw_curr_pax_employer vcpe
     WHERE au.user_id        = rpt.recvr_user_id
       AND rpt.recvr_user_id = vcpe.user_id(+)
       AND (p_in_start_date < au.date_created       AND au.date_created <= p_in_end_date
            OR p_in_start_date < au.date_modified   AND au.date_modified <= p_in_end_date
            OR p_in_start_date < vcpe.date_created  AND vcpe.date_created <= p_in_end_date
            OR p_in_start_date < vcpe.date_modified AND vcpe.date_modified <= p_in_end_date)
       AND (au.last_name         <> rpt.recvr_last_name
         OR au.first_name        <> rpt.recvr_first_name
         OR au.middle_name       <> rpt.recvr_middle_name
         OR DECODE(au.is_active,1,'active','inactive') <> rpt.recvr_pax_status
         OR NVL(vcpe.department_type,'X') <> NVL(rpt.recvr_department,'X')   
         OR NVL(vcpe.position_type,'X')   <> NVL(rpt.recvr_job_position,'X'))) e 
    ON (dtl.rowid = e.row_id)
   WHEN MATCHED THEN 
     UPDATE SET 
        dtl.recvr_last_name    = e.last_name
        ,dtl.recvr_first_name   = e.first_name
        ,dtl.recvr_middle_name  = e.middle_name
        ,dtl.recvr_full_name    = e.full_name 
        ,dtl.recvr_pax_status   = e.pax_status
        ,dtl.recvr_department   = e.department_type
        ,dtl.recvr_job_position = e.position_type
        ,dtl.date_modified      = SYSDATE
        ,dtl.modified_by        = c_created_by;
        
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

--  v_stage := 'Open and Fetch cursor to pick modified node name';
   OPEN cur_node_changed;                        
    FETCH cur_node_changed BULK COLLECT          
     INTO v_tab_node_id,
          v_tab_node_name;
    CLOSE cur_node_changed;   

--  v_stage := 'Update modified node name for giver and receiver in rpt table';
  FORALL indx IN v_tab_node_id.FIRST .. v_tab_node_id.LAST      
    UPDATE rpt_recognition_detail
       SET giver_node_name = DECODE (giver_node_id, v_tab_node_id(indx), v_tab_node_name(indx), giver_node_name),
           recvr_node_name = DECODE (recvr_node_id, v_tab_node_id(indx), v_tab_node_name(indx), recvr_node_name),
           date_modified   = SYSDATE,
           modified_by     = c_created_by,
           version         = version + 1
     WHERE (   (    giver_node_id    = v_tab_node_id(indx)
                AND giver_node_name != v_tab_node_name(indx)
                ) 
            OR (  recvr_node_id    = v_tab_node_id(indx)
                AND recvr_node_name != v_tab_node_name(indx)
                )
            );  

   v_stage := 'Success';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
   p_out_return_code := 00;
   
EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code := 99;
   p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR',p_out_error_message, NULL);
END p_rpt_recognition_detail_sa;

--PROCEDURE P_RPT_PURL_CONTRIBUTION_SUM(p_in_requested_user_id      IN  NUMBER,
--                                      p_in_start_date             IN  DATE,
--                                      p_in_end_date               IN  DATE,
--                                      p_out_return_code           OUT NUMBER,
--                                      p_out_error_message         OUT VARCHAR2)IS
--
--/*******************************************************************************
-- 
--  Purpose:  Populates Rpt_purl_contribution_summary reporting table
--
--   Person         Date        Comments
--   -----------   ----------  ---------------------------------------------------
--  Arun S        02/08/2011   Initial creation
--  Arun S        03/09/2011   Bug # 36143 Fix, added and populated column recipient_pax_status
--  Chidamba      10/12/2012   G5 reports to make incremental.
--  Ravi Dhanekula 03/06/2014  Fix for defect # 51887. Corrected the issue with rpt_teamsum data below.
--  nagarajs       08/12/2014  Bug 55407 - Fixed the Contribution posted column count issue  
-- Suresh J       05/26/2015       Bug 62417,62418,62419,62431 - Fixed count issues in PURL reports  
-- nagarajs       12/16/2015   Bug 64993 - Exported report total values are mismatched 
--*******************************************************************************/
--
--  c_process_name            execution_log.process_name%TYPE := 'P_RPT_PURL_CONTRIBUTION_SUM'; 
--  c_release_level           execution_log.release_level %TYPE := 1.0;
--  c_created_by              CONSTANT rpt_purl_contribution_summary.created_by%TYPE := p_in_requested_user_id;
--  v_rpt_purl_cntr_summ_id   rpt_purl_contribution_summary.rpt_purl_cntr_summ_id%TYPE;
--  v_stage                   execution_log.text_line%TYPE;
--  v_rec_cnt                 NUMBER; 
--BEGIN
--
--   v_stage := 'Merge summary rpt_purl_contribution_summary'; 
--   MERGE INTO rpt_purl_contribution_summary d
--   USING (
--           WITH rpt_teamsum AS
--           (  -- build team summary records
--            SELECT h.parent_node_id header_node_id,
--                   h.node_id detail_node_id,
--                   d.recipient_pax_status     pax_status,
--                   NVL(d.promotion_id,0)      promotion_id,
--                   trunc(d.purl_created_date) purl_created_date,                   
--                   NVL(d.recipient_job_position,' ')   job_position,
--                   NVL(d.recipient_department,' ')    department,
--                   SUM(NVL(d.contribution_posted,0))     contribution_posted, --08/12/2014
----                   COUNT(DISTINCT d.recipient_user_id) recipient_count, --03/06/2014  Added DISTINCT just to count the recipients Bug # 51887
--                   COUNT(DISTINCT D.purl_recipient_id)  recipient_count,     --05/26/2015               
--                   --COUNT(NVL(d.contributor_user_id,0)) num_contributors_invited, --12/16/2015
--                   COUNT(d.purl_contributor_id) num_contributors_invited, --12/16/2015
--                   COUNT(decode(d.contributed_flg,'Y',1,NULL)) num_contributors_actual,
--                   h.hier_level,
--                   h.is_leaf
--              FROM rpt_purl_contribution_detail d,
--                   rpt_hierarchy h
--             WHERE h.node_id = d.node_id
--               /*AND (d.date_created > p_in_start_date      AND d.date_created   <= SYSDATE  --03/06/2014 Start Bug # 51887 --12/16/2015
--                    OR d.date_modified > p_in_start_date  AND d.date_modified  <= SYSDATE
--                    OR h.date_created  > p_in_start_date  AND h.date_created   <= SYSDATE
--                    OR h.date_modified > p_in_start_date  AND h.date_modified  <= SYSDATE)*/ --03/06/2014 End
--             GROUP BY h.node_id ,
--                      d.recipient_pax_status,
----                      d.promotion_id,                           --05/26/2015
--                      NVL(d.promotion_id,0),                      --05/26/2015
--                      trunc(d.purl_created_date),                 --05/26/2015
----                      d.recipient_job_position,                 --05/26/2015
--                      NVL(d.recipient_job_position,' '),          --05/26/2015
----                      d.recipient_department,                   --05/26/2015
--                      NVL(d.recipient_department,' '),            --05/26/2015
--                      --d.contribution_posted, --08/12/2014
--                      h.hier_level,
--                      h.is_leaf,
--                      h.parent_node_id
--            ), detail_derived_summary AS
--            (  -- derive summaries based on team summary data
--               SELECT -- key fields
--                      rt.detail_node_id,
--                      rt.hier_level||'-teamsum' AS sum_type,
--                      rt.promotion_id,
--                      rt.pax_status,
--                      rt.job_position,
--                      rt.department,
--                      rt.purl_created_date,
--                      -- reference fields
--                      rt.contribution_posted,
--                      rt.header_node_id,
--                      rt.hier_level,
--                      1 AS is_leaf, -- The team summary records are always a leaf
--                      --count
--                      rt.recipient_count,
--                      rt.num_contributors_invited,
--                      rt.num_contributors_actual
--                 FROM rpt_teamsum rt
--                UNION ALL
--               SELECT -- key fields
--                      h.node_id AS detail_node_id,
--                      h.hier_level||'-nodesum' AS sum_type,
--                      rt.promotion_id,
--                      rt.pax_status,
--                      rt.job_position,
--                      rt.department,
--                      rt.purl_created_date,
--                      -- reference fields
--                      rt.contribution_posted,
--                      h.parent_node_id AS header_node_id,
--                      h.hier_level,
--                      h.is_leaf,
--                      -- count fields
--                      NVL(SUM(rt.recipient_count),0) AS recipient_count,
--                      NVL(SUM(rt.num_contributors_invited),0) AS num_contributors_invited,
--                      NVL(SUM(rt.num_contributors_actual),0)  AS num_contributors_actual                      
--                 FROM ( -- associate each node to all its hierarchy nodes
--                        SELECT np.node_id,
--                               p.column_value AS path_node_id
--                          FROM ( -- get node hierarchy path
--                                 SELECT h.node_id,
--                                        h.hier_level,
--                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
--                                   FROM rpt_hierarchy h
--                                  START WITH h.parent_node_id IS NULL
--                                CONNECT BY PRIOR h.node_id = h.parent_node_id
--                               ) np,
--                               -- parse node path into individual nodes
--                               -- pivoting the node path into separate records
--                               TABLE( CAST( MULTISET(
--                                  SELECT TO_NUMBER(
--                                            SUBSTR(np.node_path,
--                                                   INSTR(np.node_path, '/', 1, LEVEL)+1, 
--                                                   INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
--                                            )
--                                         )
--                                    FROM dual
--                                 CONNECT BY LEVEL <= np.hier_level 
--                               ) AS sys.odcinumberlist ) ) p
--                      ) npn,
--                      rpt_hierarchy h,
--                      rpt_teamsum rt
--                WHERE --(  rt.hier_level = h.hier_level    -- always create node summary at team summary level    --05/26/2015
----                      OR rt.recipient_count != 0         -- create node summary for team summaries with non-zero recepient count  --05/26/2015 
----                      )  --05/26/2015
--                   -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
----                  AND 
--                  rt.detail_node_id = npn.node_id
--                  AND npn.path_node_id = h.node_id
--                GROUP BY h.node_id ,
--                         rt.pax_status,
--                         rt.promotion_id,
--                         rt.purl_created_date,
--                         rt.job_position,
--                         rt.department,
--                         rt.contribution_posted,
--                         h.hier_level,
--                         h.is_leaf,
--                         h.parent_node_id
--            ) -- end detail_derived_summary
--             SELECT dds.*
--              FROM detail_derived_summary dds                 
--          ) s
--      ON (DECODE(d.detail_node_id, s.detail_node_id, 1, 0) = 1
--                      AND d.record_type       = s.sum_type
--                      and d.promotion_id   = s.promotion_id
--                      AND d.job_position   = s.job_position
--                      AND d.department     = s.department
--                      AND d.recipient_pax_status = s.pax_status
--                      AND d.purl_created_date = NVL(s.purl_created_date,d.purl_created_date)
--                      AND NVL(d.header_node_id, 0) = NVL(s.header_node_id, 0) 
--                      AND d.hier_level     = s.hier_level     
--                      AND NVL(d.is_leaf, 0) = NVL(s.is_leaf, 0))
--    WHEN MATCHED THEN 
--      UPDATE SET
--         d.recipient_count    = s.recipient_count,
--         d.contribution_posted = s.contribution_posted,
--         d.num_contributors_invited = s.num_contributors_invited,
--         d.num_contributors_actual  = s.num_contributors_actual,
--         d.modified_by        = 0,
--         d.date_modified      = SYSDATE
--       WHERE ( -- only update records with different values
--                DECODE(d.header_node_id,     s.header_node_id,     1, 0) = 0
--             OR DECODE(d.hier_level,         s.hier_level,         1, 0) = 0
--             OR DECODE(d.is_leaf,            s.is_leaf,            1, 0) = 0
--             OR DECODE(d.contribution_posted, s.contribution_posted, 1, 0) = 0
--             OR DECODE(d.num_contributors_invited, s.num_contributors_invited,      1, 0) = 0
--             OR DECODE(d.num_contributors_actual, s.num_contributors_actual,      1, 0)   = 0
--             OR DECODE(d.recipient_count,    s.recipient_count,   1, 0)   = 0
--             )
--    WHEN NOT MATCHED THEN
--      INSERT
--      (  rpt_purl_cntr_summ_id,
--         record_type,
--         header_node_id,
--         detail_node_id,
--         recipient_pax_status,
--         promotion_id,
--         purl_created_date,
--         recipient_count,
--         job_position,
--         department,
--         contribution_posted,
--         num_contributors_invited,         
--         num_contributors_actual,          
--         hier_level,        
--         is_leaf,       
--         date_created,          
--         created_by
--      )
--      VALUES
--      (  rpt_purl_cntr_summ_pk_sq.NEXTVAL,
--         -- key fields
--         s.sum_type,  --s.record_type,
--         s.header_node_id,
--         s.detail_node_id,         
--         s.pax_status,
--         s.promotion_id,
--         nvl(s.purl_created_date,SYSDATE),
--         s.recipient_count,
--         s.job_position,
--         s.department,
--         s.contribution_posted,
--         s.num_contributors_invited,
--         s.num_contributors_actual,
--         s.hier_level,
--         s.is_leaf,
--         SYSDATE,
--         0
--         );
--
-- v_stage := 'INSERT missing default node summary permutations';
-- INSERT INTO rpt_purl_contribution_summary
--   ( rpt_purl_cntr_summ_id,
--     record_type,
--     header_node_id,
--     detail_node_id,
--     recipient_pax_status,
--     promotion_id,
--     purl_created_date,
--     recipient_count,
--     job_position,
--     department,
--     contribution_posted,
--     num_contributors_invited,         
--     num_contributors_actual,          
--     hier_level,        
--     is_leaf,       
--     date_created,          
--     created_by
--   )
--    (  -- find missing default permutations
--      SELECT rpt_purl_cntr_summ_pk_sq.NEXTVAL,
--             -- key fields             
--             nsp.record_type,
--             nsp.header_node_id,
--             nsp.detail_node_id,             
--             ' '  AS pax_status,
--             0    AS promotion_id,
--             TRUNC(SYSDATE) AS purl_created_date,
--             0    AS recipient_count,
--             ' '  AS job_position,
--             ' '  AS department,             
--             -- reference fiel
--             0    AS contribution_posted,
--             0    AS num_contributors_invited,
--             0    AS num_contributors_actual,
--             -- audit fields                          
--             nsp.hier_level,
--             nsp.is_leaf,
--             -- count fields             
--             SYSDATE      AS date_created,
--             0 AS created_by             
--        FROM ( -- get all possible node summary type permutations
--               SELECT h.node_id AS detail_node_id,
--                      h.hier_level || '-' || 'teamsum' AS record_type,
--                      h.parent_node_id AS header_node_id,
--                      h.hier_level,
--                      1 AS is_leaf   -- team summary always a leaf node
--                 FROM rpt_hierarchy h
--             ) nsp
--          -- exclude default permutation when a matching summary record exists
--        WHERE NOT EXISTS
--             ( SELECT 1
--                 FROM rpt_purl_contribution_summary s
--                WHERE s.detail_node_id = nsp.detail_node_id
--                  AND s.record_type    = nsp.record_type
--                   -- any media date value matches on team summary records because detail derived summaries replace default team permutations
--             )
--   );
--
--
--   v_rec_cnt := SQL%ROWCOUNT;
--   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
--
--   v_stage := 'INSERT missing default node summary permutations';
--   INSERT INTO rpt_purl_contribution_summary
--   (  rpt_purl_cntr_summ_id,
--     record_type,
--     header_node_id,
--     detail_node_id,
--     recipient_pax_status,
--     promotion_id,
--     purl_created_date,
--     recipient_count,
--     job_position,
--     department,
--     contribution_posted,
--     num_contributors_invited,         
--     num_contributors_actual,          
--     hier_level,        
--     is_leaf,       
--     date_created,          
--     created_by
--   )
--    (  -- find missing default permutations
--      SELECT rpt_purl_cntr_summ_pk_sq.NEXTVAL,
--             -- key fields             
--             nsp.record_type,
--             nsp.header_node_id,
--             nsp.detail_node_id,             
--             ' '  AS pax_status,
--             0    AS promotion_id,
--             TRUNC(SYSDATE) AS purl_created_date,
--             0    AS recipient_count,
--             ' '  AS job_position,
--             ' '  AS department,             
--             -- reference fiel
--             0    AS contribution_posted,
--             0    AS num_contributors_invited,
--             0    AS num_contributors_actual,
--             -- audit fields                          
--             nsp.hier_level,
--             nsp.is_leaf,
--             -- count fields             
--             SYSDATE      AS date_created,
--             0 AS created_by             
--        FROM ( -- get all possible node summary type permutations
--               SELECT h.node_id AS detail_node_id,
--                      h.hier_level || '-' || 'nodesum' AS record_type,
--                      h.parent_node_id AS header_node_id,
--                      h.hier_level,
--                      h.is_leaf
--                 FROM rpt_hierarchy h
--             ) nsp
--          -- exclude default permutation when a matching summary record exists
--        WHERE NOT EXISTS
--             ( SELECT 1
--                 FROM rpt_purl_contribution_summary s
--                WHERE s.detail_node_id = nsp.detail_node_id
--                  AND s.record_type    = nsp.record_type
--                  AND s.purl_created_date IS NOT NULL
--             )
--          -- default node summary permutation must have default team summary permutation in its hierarchy
--         AND EXISTS
--             ( -- get team permutations under node permutation
--               SELECT 1
--                 FROM rpt_purl_contribution_summary tp
--                WHERE tp.detail_node_id          = nsp.detail_node_id
--                  AND SUBSTR(tp.record_type, -7) = 'teamsum'
--                  AND tp.purl_created_date IS NOT NULL
--                UNION ALL
--               -- get team permutations under node permutation hierarchy
--               SELECT 1
--                 FROM rpt_purl_contribution_summary tp
--                   -- start with child node immediately under current node
--                START WITH tp.header_node_id          = nsp.detail_node_id
--                       AND SUBSTR(tp.record_type, -7) = 'teamsum'
--                       AND tp.purl_created_date IS NOT NULL
--                CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
--                       AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
--                       AND tp.purl_created_date IS NOT NULL
--             )
--   );
--
--   v_rec_cnt := SQL%ROWCOUNT;
--   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
--
--   ------------
--   -- remove team permutations when an associated detail derived summary exists
--   v_stage := 'DELETE obsolete team permutations';
--   DELETE rpt_purl_contribution_summary
--    WHERE ROWID IN
--          ( -- get default team permutation with a corresponding detail derived team summary
--            SELECT DISTINCT
--                   s.ROWID
--              FROM rpt_purl_contribution_summary s,
--                   rpt_purl_contribution_summary dd
--                -- substr matches functional index
--             WHERE SUBSTR(dd.record_type, -7) = 'teamsum'
--                -- detail derived summaries have a media date
--               AND dd.purl_created_date IS NOT NULL
--               AND dd.detail_node_id          = s.detail_node_id
--               AND SUBSTR(dd.record_type, -7) = SUBSTR(s.record_type, -7)
--                -- default permutations have no media date
--               AND s.purl_created_date IS NULL
--          );
--
--
--
--   -- delete node permutation with no associated team permutation
--   v_stage := 'DELETE obsolete node permutations with no associated team permutation';
--  DELETE rpt_purl_contribution_summary
--    WHERE ROWID IN
--          ( SELECT np.ROWID
--              FROM rpt_purl_contribution_summary np
--             WHERE SUBSTR(np.record_type, -7) = 'nodesum'
--               AND np.purl_created_date IS NOT NULL
--                -- node permutation has no team permutation
--               AND NOT EXISTS
--                   ( -- get team permutations under node permutation
--                     SELECT 1
--                       FROM rpt_purl_contribution_summary tp
--                      WHERE tp.detail_node_id          = np.detail_node_id
--                        AND SUBSTR(tp.record_type, -7) = 'teamsum'
--                        AND tp.purl_created_date IS NOT NULL
--                      UNION ALL
--                     -- get team permutations under node permutation hierarchy
--                     SELECT 1
--                       FROM rpt_purl_contribution_summary tp
--                         -- start with child node immediately under current node
--                      START WITH tp.header_node_id          = np.detail_node_id
--                             AND SUBSTR(tp.record_type, -7) = 'teamsum'
--                             AND tp.purl_created_date IS NOT NULL
--                      CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
--                             AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
--                             AND tp.purl_created_date IS NOT NULL
--                   )
--          );
--
--  v_rec_cnt := SQL%ROWCOUNT;
--  prc_execution_log_entry(c_process_name, c_release_level, 'INFO',  ' End Process ', NULL);
--
--
--EXCEPTION
--  WHEN OTHERS THEN
--    prc_execution_log_entry ('P_RPT_PURL_CONTRIBUTION_SUM',
--                              1,
--                             'ERROR',
--                              SQLERRM||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE,
--                              NULL);
--
--END;    --P_RPT_PURL_CONTRIBUTION_SUM

/*********** nagarajs 04/21/2017 Commented out P_RPT_PURL_CONTRIBUTION_SUM since no longer summary tables used in query packages***********/

/*PROCEDURE P_RPT_PURL_CONTRIBUTION_SUM (p_in_requested_user_id      IN  NUMBER,
                                      p_in_start_date             IN  DATE,
                                      p_in_end_date               IN  DATE,
                                      p_out_return_code           OUT NUMBER,
                                      p_out_error_message         OUT VARCHAR2)IS

 /******************************************************************************
  NAME:       P_RPT_PURL_CONTRIBUTION_SUM   
  Date                 Author           Description
  ----------           ----------  ------------------------------------------------
  Suresh J             12/28/2015       Bug 61865 - Recognition PURL Activity Report - incorrect Recipients count  
  Ravi Dhanekula  12/28/2015       Bug 61865 -- Removed the old code.  (default node summary permutations)
  nagarajs          01/04/2016     Bug 65031 - Contributions posted in Recognition PURL Report Summary doesn't match with it's detail report 
  Ravi Dhanekula    01/08/2016     Bug # 61865 - Commented out DELETE obsolete team/node permutations
  Suresh J          02/17/2016     Bug 65799 - Count not matching between extract and summary in purl activity report
  Sherif Basha      11/04/2016     Bug 69894 - Recognition PURL Activity report count is mismatch when admin is drill down the data
                                  For an existing summary record and nodesum record type if the is_leaf in rpt_hierarchy for a node
                                  is changed it creates new record and hence it created the duplicate record.Restricting the duplication for the fix. 
 ******************************************************************************/

 /* c_process_name            execution_log.process_name%TYPE := 'P_RPT_PURL_CONTRIBUTION_SUM'; 
  c_release_level           execution_log.release_level %TYPE := 1.0;
  c_created_by              CONSTANT rpt_purl_recipient_summary.created_by%TYPE := p_in_requested_user_id;
  v_rpt_purl_cntr_summ_id   rpt_purl_recipient_summary.rpt_purl_cntr_summ_id%TYPE;
  v_stage                   execution_log.text_line%TYPE;
  v_rec_cnt                 NUMBER; 

BEGIN



  --Delete obsolete job position and department from Summary            --02/17/2016  
  DELETE FROM rpt_purl_recipient_summary t1
      WHERE NOT EXISTS
                   (SELECT node_id, position_type, department_type
                      FROM rpt_purl_recipient_detail prd,
                           vw_curr_pax_employer r
                     WHERE     r.user_id = prd.recipient_user_id
                           AND r.position_type = t1.job_position
                           AND r.department_type = t1.department
                           AND prd.node_id = t1.detail_node_id);

   v_stage := 'Merge summary rpt_purl_recipient_summary'; 
   MERGE INTO rpt_purl_recipient_summary d
   USING (
           WITH rpt_teamsum AS
           (  -- build team summary records
            SELECT h.parent_node_id header_node_id,
                   h.node_id detail_node_id,
                   d.recipient_pax_status     pax_status,
                   NVL(d.promotion_id,0)      promotion_id,
                   trunc(d.purl_created_date) purl_created_date,                   
                   NVL(d.recipient_job_position,' ')   job_position,
                   NVL(d.recipient_department,' ')    department,
                   SUM(NVL(contribution_posted_count,0))     contribution_posted, 
                   COUNT(DISTINCT D.purl_recipient_id)  recipient_count,                    
                   SUM (d.purl_contributor_count) num_contributors_invited, 
                   SUM (D.contributed_count) num_contributors_actual,
                   h.hier_level,
                   h.is_leaf
              FROM rpt_purl_recipient_detail d,
                   rpt_hierarchy h
             WHERE h.node_id = d.node_id
             GROUP BY h.node_id ,
                      d.recipient_pax_status,
                      NVL(d.promotion_id,0),                      
                      trunc(d.purl_created_date),                 
                      NVL(d.recipient_job_position,' '),          
                      NVL(d.recipient_department,' '),            
                      h.hier_level,
                      h.is_leaf,
                      h.parent_node_id
            ), detail_derived_summary AS
            (  -- derive summaries based on team summary data
               SELECT -- key fields
                      rt.detail_node_id,
                      rt.hier_level||'-teamsum' AS sum_type,
                      rt.promotion_id,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.purl_created_date,
                      -- reference fields
                      rt.contribution_posted,
                      rt.header_node_id,
                      rt.hier_level,
                      1 AS is_leaf, -- The team summary records are always a leaf
                      --count
                      rt.recipient_count,
                      rt.num_contributors_invited,
                      rt.num_contributors_actual
                 FROM rpt_teamsum rt
                UNION ALL
               SELECT -- key fields
                      h.node_id AS detail_node_id,
                      h.hier_level||'-nodesum' AS sum_type,
                      rt.promotion_id,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.purl_created_date,
                      -- reference fields
                      NVL(SUM(rt.contribution_posted),0) contribution_posted, --01/04/2016
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      h.is_leaf,
                      -- count fields
                      NVL(SUM(rt.recipient_count),0) AS recipient_count,
                      NVL(SUM(rt.num_contributors_invited),0) AS num_contributors_invited,
                      NVL(SUM(rt.num_contributors_actual),0)  AS num_contributors_actual                      
                 FROM ( -- associate each node to all its hierarchy nodes
                        SELECT np.node_id,
                               p.column_value AS path_node_id
                          FROM ( -- get node hierarchy path
                                 SELECT h.node_id,
                                        h.hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                   FROM rpt_hierarchy h
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
                               ) AS sys.odcinumberlist ) ) p
                      ) npn,
                      rpt_hierarchy h,
                      rpt_teamsum rt
                WHERE rt.detail_node_id = npn.node_id
                    AND npn.path_node_id = h.node_id
                GROUP BY h.node_id ,
                         rt.pax_status,
                         rt.promotion_id,
                         rt.purl_created_date,
                         rt.job_position,
                         rt.department,
                         --rt.contribution_posted, --01/04/2016
                         h.hier_level,
                         h.is_leaf,
                         h.parent_node_id
            ) -- end detail_derived_summary
             SELECT dds.*
              FROM detail_derived_summary dds                 
          ) s
      ON (DECODE(d.detail_node_id, s.detail_node_id, 1, 0) = 1
                      AND d.record_type    = s.sum_type
                      and d.promotion_id   = s.promotion_id
                      AND d.job_position   = s.job_position
                      AND d.department     = s.department
                      AND d.recipient_pax_status = s.pax_status
                      AND d.purl_created_date = NVL(s.purl_created_date,d.purl_created_date)
                      AND NVL(d.header_node_id, 0) = NVL(s.header_node_id, 0) 
                      AND d.hier_level     = s.hier_level     
                     -- AND NVL(d.is_leaf, 0) = NVL(s.is_leaf, 0)           --11/04/2016  Bug 69894 commenting this condition for fix
                      )
    WHEN MATCHED THEN 
      UPDATE SET
         d.recipient_count    = s.recipient_count,
         d.contribution_posted = s.contribution_posted,
         d.num_contributors_invited = s.num_contributors_invited,
         d.num_contributors_actual  = s.num_contributors_actual,
         d.modified_by        = 0,
         d.date_modified      = SYSDATE
       WHERE ( -- only update records with different values
                DECODE(d.header_node_id,     s.header_node_id,     1, 0) = 0
             OR DECODE(d.hier_level,         s.hier_level,         1, 0) = 0
             OR DECODE(d.is_leaf,            s.is_leaf,            1, 0) = 0
             OR DECODE(d.contribution_posted, s.contribution_posted, 1, 0) = 0
             OR DECODE(d.num_contributors_invited, s.num_contributors_invited,      1, 0) = 0
             OR DECODE(d.num_contributors_actual, s.num_contributors_actual,      1, 0)   = 0
             OR DECODE(d.recipient_count,    s.recipient_count,   1, 0)   = 0
             )
    WHEN NOT MATCHED THEN
      INSERT
      (  rpt_purl_cntr_summ_id,
         record_type,
         header_node_id,
         detail_node_id,
         recipient_pax_status,
         promotion_id,
         purl_created_date,
         recipient_count,
         job_position,
         department,
         contribution_posted,
         num_contributors_invited,         
         num_contributors_actual,          
         hier_level,        
         is_leaf,       
         date_created,          
         created_by
      )
      VALUES
      (  rpt_purl_rec_summ_pk_sq.NEXTVAL,
         -- key fields
         s.sum_type,  --s.record_type,
         s.header_node_id,
         s.detail_node_id,         
         s.pax_status,
         s.promotion_id,
         nvl(s.purl_created_date,SYSDATE),
         s.recipient_count,
         s.job_position,
         s.department,
         s.contribution_posted,
         s.num_contributors_invited,
         s.num_contributors_actual,
         s.hier_level,
         s.is_leaf,
         SYSDATE,
         0
         );

-- v_stage := 'INSERT missing default node summary permutations'; 12/28/2015 Ravi Dhanekula --We no longer need this code.
-- INSERT INTO rpt_purl_recipient_summary
--   ( rpt_purl_cntr_summ_id,
--     record_type,
--     header_node_id,
--     detail_node_id,
--     recipient_pax_status,
--     promotion_id,
--     purl_created_date,
--     recipient_count,
--     job_position,
--     department,
--     contribution_posted,
--     num_contributors_invited,         
--     num_contributors_actual,          
--     hier_level,        
--     is_leaf,       
--     date_created,          
--     created_by
--   )
--    (  -- find missing default permutations
--      SELECT rpt_purl_rec_summ_pk_sq.NEXTVAL,
--             -- key fields             
--             nsp.record_type,
--             nsp.header_node_id,
--             nsp.detail_node_id,             
--             ' '  AS pax_status,
--             0    AS promotion_id,
--             TRUNC(SYSDATE) AS purl_created_date,
--             0    AS recipient_count,
--             ' '  AS job_position,
--             ' '  AS department,             
--             -- reference fiel
--             0    AS contribution_posted,
--             0    AS num_contributors_invited,
--             0    AS num_contributors_actual,
--             -- audit fields                          
--             nsp.hier_level,
--             nsp.is_leaf,
--             -- count fields             
--             SYSDATE      AS date_created,
--             0 AS created_by             
--        FROM ( -- get all possible node summary type permutations
--               SELECT h.node_id AS detail_node_id,
--                      h.hier_level || '-' || 'teamsum' AS record_type,
--                      h.parent_node_id AS header_node_id,
--                      h.hier_level,
--                      1 AS is_leaf   -- team summary always a leaf node
--                 FROM rpt_hierarchy h
--             ) nsp
--          -- exclude default permutation when a matching summary record exists
--        WHERE NOT EXISTS
--             ( SELECT 1
--                 FROM rpt_purl_recipient_summary s
--                WHERE s.detail_node_id = nsp.detail_node_id
--                  AND s.record_type    = nsp.record_type
--                   -- any media date value matches on team summary records because detail derived summaries replace default team permutations
--             )
--   );
--
--
--   v_rec_cnt := SQL%ROWCOUNT;
--   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
--
--   v_stage := 'INSERT missing default node summary permutations';
--   INSERT INTO rpt_purl_recipient_summary
--   (  rpt_purl_cntr_summ_id,
--     record_type,
--     header_node_id,
--     detail_node_id,
--     recipient_pax_status,
--     promotion_id,
--     purl_created_date,
--     recipient_count,
--     job_position,
--     department,
--     contribution_posted,
--     num_contributors_invited,         
--     num_contributors_actual,          
--     hier_level,        
--     is_leaf,       
--     date_created,          
--     created_by
--   )
--    (  -- find missing default permutations
--      SELECT rpt_purl_rec_summ_pk_sq.NEXTVAL,
--             -- key fields             
--             nsp.record_type,
--             nsp.header_node_id,
--             nsp.detail_node_id,             
--             ' '  AS pax_status,
--             0    AS promotion_id,
--             TRUNC(SYSDATE) AS purl_created_date,
--             0    AS recipient_count,
--             ' '  AS job_position,
--             ' '  AS department,             
--             -- reference fiel
--             0    AS contribution_posted,
--             0    AS num_contributors_invited,
--             0    AS num_contributors_actual,
--             -- audit fields                          
--             nsp.hier_level,
--             nsp.is_leaf,
--             -- count fields             
--             SYSDATE      AS date_created,
--             0 AS created_by             
--        FROM ( -- get all possible node summary type permutations
--               SELECT h.node_id AS detail_node_id,
--                      h.hier_level || '-' || 'nodesum' AS record_type,
--                      h.parent_node_id AS header_node_id,
--                      h.hier_level,
--                      h.is_leaf
--                 FROM rpt_hierarchy h
--             ) nsp
--          -- exclude default permutation when a matching summary record exists
--        WHERE NOT EXISTS
--             ( SELECT 1
--                 FROM rpt_purl_recipient_summary s
--                WHERE s.detail_node_id = nsp.detail_node_id
--                  AND s.record_type    = nsp.record_type
--                  AND s.purl_created_date IS NOT NULL
--             )
--          -- default node summary permutation must have default team summary permutation in its hierarchy
--         AND EXISTS
--             ( -- get team permutations under node permutation
--               SELECT 1
--                 FROM rpt_purl_recipient_summary tp
--                WHERE tp.detail_node_id          = nsp.detail_node_id
--                  AND SUBSTR(tp.record_type, -7) = 'teamsum'
--                  AND tp.purl_created_date IS NOT NULL
--                UNION ALL
--               -- get team permutations under node permutation hierarchy
--               SELECT 1
--                 FROM rpt_purl_recipient_summary tp
--                   -- start with child node immediately under current node
--                START WITH tp.header_node_id          = nsp.detail_node_id
--                       AND SUBSTR(tp.record_type, -7) = 'teamsum'
--                       AND tp.purl_created_date IS NOT NULL
--                CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
--                       AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
--                       AND tp.purl_created_date IS NOT NULL
--             )
--   );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   ------------
   -- remove team permutations when an associated detail derived summary exists
/*   v_stage := 'DELETE obsolete team permutations';--01/08/2016
   DELETE rpt_purl_recipient_summary
    WHERE ROWID IN
          ( -- get default team permutation with a corresponding detail derived team summary
            SELECT DISTINCT
                   s.ROWID
              FROM rpt_purl_recipient_summary s,
                   rpt_purl_recipient_summary dd
                -- substr matches functional index
             WHERE SUBSTR(dd.record_type, -7) = 'teamsum'
                -- detail derived summaries have a media date
               AND dd.purl_created_date IS NOT NULL
               AND dd.detail_node_id          = s.detail_node_id
               AND SUBSTR(dd.record_type, -7) = SUBSTR(s.record_type, -7)
                -- default permutations have no media date
               AND s.purl_created_date IS NULL
          );



   -- delete node permutation with no associated team permutation
   v_stage := 'DELETE obsolete node permutations with no associated team permutation';
  DELETE rpt_purl_recipient_summary
    WHERE ROWID IN
          ( SELECT np.ROWID
              FROM rpt_purl_recipient_summary np
             WHERE SUBSTR(np.record_type, -7) = 'nodesum'
               AND np.purl_created_date IS NOT NULL
                -- node permutation has no team permutation
               AND NOT EXISTS
                   ( -- get team permutations under node permutation
                     SELECT 1
                       FROM rpt_purl_recipient_summary tp
                      WHERE tp.detail_node_id          = np.detail_node_id
                        AND SUBSTR(tp.record_type, -7) = 'teamsum'
                        AND tp.purl_created_date IS NOT NULL
                      UNION ALL
                     -- get team permutations under node permutation hierarchy
                     SELECT 1
                       FROM rpt_purl_recipient_summary tp
                         -- start with child node immediately under current node
                      START WITH tp.header_node_id          = np.detail_node_id
                             AND SUBSTR(tp.record_type, -7) = 'teamsum'
                             AND tp.purl_created_date IS NOT NULL
                      CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                             AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
                             AND tp.purl_created_date IS NOT NULL
                   )
          ); */
/*
  v_rec_cnt := SQL%ROWCOUNT;
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO',  ' End Process ', NULL);
  p_out_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry (c_process_name,
                              1,
                             'ERROR',
                              SQLERRM||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE,
                              NULL);
   p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;                              

END P_RPT_PURL_CONTRIBUTION_SUM;    */

END;
/
