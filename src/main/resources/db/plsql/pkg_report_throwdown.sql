CREATE OR REPLACE PACKAGE pkg_report_throwdown
  IS

  PROCEDURE P_RPT_THROWDOWN_ACTIVITY(p_in_requested_user_id      IN  NUMBER,                                
                                 p_out_return_code           OUT NUMBER,
                                 p_out_error_message         OUT VARCHAR2);
  
PROCEDURE P_RPT_THROWDOWN_PAYOUT(p_in_requested_user_id      IN  NUMBER,
                                       p_in_start_date             IN  DATE,
                                       p_in_end_date               IN  DATE,                                
                                 p_out_return_code           OUT NUMBER,
                                 p_out_error_message         OUT VARCHAR2);
END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_report_throwdown IS

PROCEDURE P_RPT_THROWDOWN_ACTIVITY(p_in_requested_user_id      IN  NUMBER,                                
                                 p_out_return_code           OUT NUMBER,
                                 p_out_error_message         OUT VARCHAR2)
IS
/*******************************************************************************
--
-- Purpose: Populate rpt_throwdown_activity table.
--
-- MODIFICATION HISTORY
-- Person      Date         Comments
-- ---------   ----------   ------------------------------------------
-- Ravi Dhanekula  10/16/2013   Intial version
                   10/23/2013   Changes made to include only rounds that are in progress or completed.
                   12/5/2014   Bug # 58490   Added stack rank points also to the activity.
--Suresh J        01/02/2015  Added NVL on Payout formula fields                     
*******************************************************************************/
c_process_name    CONSTANT execution_log.process_name%TYPE
                                 := UPPER ('p_rpt_throwdown_activity') ;
   c_release_level   CONSTANT execution_log.release_level%TYPE := 2.0;  
  c_created_by      CONSTANT rpt_throwdown_activity.created_by%TYPE:=0;
  v_stage                    execution_log.text_line%TYPE;
   v_rec_cnt                  INTEGER;
  v_language_code   VARCHAR2(10):='en_US';

BEGIN

v_stage :='Merge rpt_throwdown_activity';
      MERGE INTO rpt_throwdown_activity rpt
USING
(--12/5/2014  Start
/*SELECT td.division_id,td.promotion_id,td.division_name_asset_code division_name,td.minimum_qualifier,tr.round_id,tr.round_number,tm.match_id,tm.status match_status,tmto.team_id,tmto.outcome_type outcome,NVL(tmto.current_value,0) current_value,
a.award_quantity payout,a.activity_id,TRUNC(a.submission_date) payout_date,n.node_id,n.name node_name,pr.status user_status,au.user_id,au.last_name,au.first_name,au.middle_name,pe.department_type department,pe.position_type job_position,ua.country_id,p.promotion_name,
au.user_name login_id,tr.start_date round_start_date,TRUNC(tr.end_date) round_end_date
FROM
throwdown_division td,
throwdown_round tr,
promotion p,
throwdown_match tm,
throwdown_match_team_outcome tmto,
throwdown_team tt,
user_node un,
node n,
activity a,
application_user au,
participant pr,
vw_curr_pax_employer pe,
user_address ua
WHERE
td.division_id = tr.division_id
AND td.promotion_id = p.promotion_id
AND p.promotion_status  IN ('live','expired')
AND tm.round_id = tr.round_id
AND TRUNC(tr.start_date) <=TRUNC(SYSDATE) --10/23/2013
AND tm.match_id = tmto.match_id
AND tmto.match_outcome_id = a.match_outcome_id(+)
AND tmto.team_id = tt.team_id
AND tt.user_id = un.user_id
AND un.user_id = au.user_id
AND au.user_id = pr.user_id
AND un.is_primary = 1
AND un.node_id = n.node_id
AND un.user_id = pe.user_id(+)
AND un.user_id = ua.user_id
AND ua.is_primary = 1*/
WITH throwdown_activity AS (SELECT td.division_id,td.promotion_id,td.division_name_asset_code division_name,td.minimum_qualifier,tr.round_id,tr.round_number,tm.match_id,tm.status match_status,tmto.team_id,tmto.outcome_type outcome,NVL(tmto.current_value,0) current_value,
a.award_quantity payout,a.activity_id,TRUNC(a.submission_date) payout_date,n.node_id,n.name node_name,pr.status user_status,au.user_id,au.last_name,au.first_name,au.middle_name,pe.department_type department,pe.position_type job_position,ua.country_id,p.promotion_name,
au.user_name login_id,tr.start_date round_start_date,TRUNC(tr.end_date) round_end_date
FROM
throwdown_division td,
throwdown_round tr,
promotion p,
throwdown_match tm,
throwdown_match_team_outcome tmto,
throwdown_team tt,
user_node un,
node n,
activity a,
application_user au,
participant pr,
vw_curr_pax_employer pe,
user_address ua
WHERE
td.division_id = tr.division_id
AND td.promotion_id = p.promotion_id
AND p.promotion_status  IN ('live','expired')
AND tm.round_id = tr.round_id
AND TRUNC(tr.start_date) <=TRUNC(SYSDATE) --10/23/2013
AND tm.match_id = tmto.match_id
AND tmto.match_outcome_id = a.match_outcome_id(+)
AND tmto.team_id = tt.team_id
AND tt.user_id = un.user_id
AND un.user_id = au.user_id
AND au.user_id = pr.user_id
AND un.is_primary = 1
AND un.node_id = n.node_id
AND un.user_id = pe.user_id(+)
AND un.user_id = ua.user_id
AND ua.is_primary = 1)
SELECT ta.division_id,ta.promotion_id,ta.division_name,ta.minimum_qualifier,ta.round_id,ta.round_number,ta.match_id,ta.match_status,ta.team_id,ta.outcome,ta.current_value,
SUM(NVL(ta.payout,0)+ NVL(payout.award_quantity,0)) payout,ta.activity_id,ta.payout_date,ta.node_id,ta.node_name,ta.user_status,ta.user_id,ta.last_name,ta.first_name,ta.middle_name,ta.department,ta.job_position,ta.country_id,ta.promotion_name,   --01/02/2015
ta.login_id,ta.round_start_date,ta.round_end_date FROM 
throwdown_activity ta,
(SELECT a.user_id,ts.promotion_id,ts.round_number,a.award_quantity from throwdown_stackrank ts, throwdown_stackrank_node tsn,throwdown_stackrank_pax tsp,activity a
WHERE ts.stackrank_id = tsn.stackrank_id
and tsn.stackrank_node_id = tsp.stackrank_node_id
and a.stackstanding_pax_id = tsp.stackrank_pax_id
and ts.payouts_issued = 1
and ts.is_active = 1) payout
WHERE ta.user_id = payout.user_id(+)
AND ta.promotion_id = payout.promotion_id(+)
AND ta.round_number = payout.round_number(+)
GROUP BY ta.division_id,ta.promotion_id,ta.division_name,ta.minimum_qualifier,ta.round_id,ta.round_number,ta.match_id,ta.match_status,ta.team_id,ta.outcome,ta.current_value,
ta.activity_id,ta.payout_date,ta.node_id,ta.node_name,ta.user_status,ta.user_id,ta.last_name,ta.first_name,ta.middle_name,ta.department,ta.job_position,ta.country_id,ta.promotion_name,
ta.login_id,ta.round_start_date,ta.round_end_date --12/5/2014  End
) s
ON (rpt.user_id = s.user_id AND rpt.promotion_id = s.promotion_id AND rpt.match_id = s.match_id AND rpt.team_id = s.team_id AND rpt.round_id = s.round_id)
 WHEN MATCHED THEN UPDATE
SET          last_name                        = s.last_name,
                first_name                       = s.first_name,
                middle_name               = s.middle_name,
                activity_id = s.activity_id,
                login_id               = s.login_id,
                user_status       = NVL(s.user_status,' '),
                job_position      = NVL(s.job_position,' '),
                department       = NVL(s.department,' '),
                node_id                              = s.node_id,              
                promotion_name   = s.promotion_name,
                division_name           = s.division_name,
                minimum_qualifier = s.minimum_qualifier,                
                node_name                    = s.node_name,
                match_status              = s.match_status,    
                outcome                             = s.outcome,    
                current_value              = s.current_value, 
                payout                                  = s.payout,
                payout_date = s.payout_date,    
                country_id                       = s.country_id,
                date_modified                 = SYSDATE,
                modified_by                    = c_created_by          
        WHERE NOT (   DECODE(rpt.first_name,  s.first_name,                    1, 0) = 1
                 AND DECODE(rpt.middle_name, s.middle_name,                   1, 0) = 1
                 AND DECODE(rpt.last_name,   s.last_name,                     1, 0) = 1
                 AND DECODE(rpt.user_status,   s.user_status,                     1, 0) = 1
                 AND DECODE(rpt.login_id,   s.login_id,                     1, 0) = 1
                 AND DECODE(rpt.job_position,   s.job_position,                     1, 0) = 1
                 AND DECODE(rpt.department,   s.department,                     1, 0) = 1
                 AND DECODE(rpt.node_id,   s.node_id,                     1, 0) = 1
                 AND DECODE(rpt.promotion_name,   s.promotion_name,                     1, 0) = 1
                 AND DECODE(rpt.division_name,   s.division_name,                     1, 0) = 1
                 AND DECODE(rpt.minimum_qualifier,   s.minimum_qualifier,                     1, 0) = 1
                 AND DECODE(rpt.round_id,   s.round_id,                     1, 0) = 1
                 AND DECODE(rpt.round_number,   s.round_number,                     1, 0) = 1
                 AND DECODE(rpt.node_name,   s.node_name,                     1, 0) = 1
                 AND DECODE(rpt.round_start_date,   s.round_start_date,                     1, 0) = 1
                 AND DECODE(rpt.round_end_date,   s.round_end_date,                     1, 0) = 1
                 AND DECODE(rpt.match_status,   s.match_status,                     1, 0) = 1
                 AND DECODE(rpt.outcome,   s.outcome,                     1, 0) = 1
                 AND DECODE(rpt.current_value,   s.current_value,                     1, 0) = 1
                 AND DECODE(rpt.payout,   s.payout,                     1, 0) = 1
                 AND DECODE(rpt.payout_date,   s.payout_date,                     1, 0) = 1
                 AND DECODE(rpt.activity_id,   s.activity_id,                     1, 0) = 1
                 AND DECODE(rpt.country_id,   s.country_id,                     1, 0) = 1                          
                 )
        WHEN NOT MATCHED THEN INSERT
        (rpt_throwdown_activity_id,
        promotion_id,
        promotion_name,
        division_name,
        minimum_qualifier,
        round_id,
        round_number,
        node_id,
        node_name,
        match_id,
        match_status,
        team_id,
        user_id,
        login_id,
        last_name,
        first_name,
        middle_name,
        user_status,
        job_position,
        department,
        outcome,
        current_value,
        round_start_date,
        round_end_date,
        payout,
        activity_id,
        payout_date,      
        country_id,
        date_created,
        created_by
        ) VALUES
        (rpt_throwdown_activity_id_seq.NEXTVAL,
        s.promotion_id,
        s.promotion_name,
        s.division_name,
        s.minimum_qualifier,
        s.round_id,
        s.round_number,
        s.node_id,
        s.node_name,
        s.match_id,
        s.match_status,
        s.team_id,
        s.user_id,
        s.login_id,
        s.last_name,
        s.first_name,
        s.middle_name,
        s.user_status,
        s.job_position,
        s.department,
        s.outcome,
        s.current_value,
        s.round_start_date,
        s.round_end_date,
        s.payout,
        s.activity_id,
        s.payout_date,     
        s.country_id,
        SYSDATE,
        c_created_by);
v_stage := 'Success';

v_rec_cnt := SQL%ROWCOUNT;
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  p_out_return_code := 00;

EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code := 99;
   p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;
   prc_execution_log_entry(c_process_name, c_release_level, 'ERROR','Param:'||p_in_requested_user_id||':'||p_out_error_message, NULL);
END;

PROCEDURE p_rpt_throwdown_payout(p_in_requested_user_id      IN  NUMBER,      
                                 p_in_start_date             IN  DATE,
                                 p_in_end_date               IN  DATE,                              
                                 p_out_return_code           OUT NUMBER,
                                 p_out_error_message         OUT VARCHAR2)
IS
/*******************************************************************************
--
-- Purpose: Populate rpt_throwdown_payout table.
--
-- MODIFICATION HISTORY
-- Person      Date         Comments
-- ---------   ----------   ------------------------------------------
-- Ravi Dhanekula  12/20/2013  Intial version                             
*******************************************************************************/
  c_process_name    CONSTANT execution_log.process_name%TYPE
                                 := UPPER ('p_rpt_throwdown_payout') ;
  c_release_level   CONSTANT execution_log.release_level%TYPE := 2.0;  
  v_stage                    execution_log.text_line%TYPE;
   v_rec_cnt                  INTEGER;
  c_created_by      CONSTANT rpt_throwdown_payout.created_by%TYPE:=0;
  v_language_code   VARCHAR2(10):='en_US';
  v_parm_list     execution_log.text_line%TYPE;   --03/11/2019  Bug 78631
BEGIN
v_stage := 'initialize variables'; 
v_parm_list := 
'p_in_requested_user_id :='||p_in_requested_user_id ||CHR(10)||
'p_in_start_date        :='||p_in_start_date        ||CHR(10)|| 
'p_in_end_date          :='||p_in_end_date    ;
INSERT INTO rpt_throwdown_payout
  (rpt_throwdown_payout_id,
   promotion_id,
   promotion_name,   
   user_id,   
   payout,
   payout_date,   
   date_created,
   created_by)
(SELECT rpt_throwdown_payout_id_seq.NEXTVAL,td.promotion_id,p.promotion_name,tt.user_id,
a.award_quantity payout,TRUNC(a.submission_date) payout_date,SYSDATE,c_created_by
FROM
throwdown_division td,
throwdown_round tr,
promotion p,
throwdown_match tm,
throwdown_match_team_outcome tmto,
throwdown_team tt,
activity a
WHERE
td.division_id = tr.division_id
AND td.promotion_id = p.promotion_id
AND p.promotion_status  IN ('live','expired')
AND tm.round_id = tr.round_id
AND TRUNC(tr.start_date) <=TRUNC(SYSDATE) 
AND tm.match_id = tmto.match_id
AND tmto.match_outcome_id = a.match_outcome_id(+)
AND tmto.team_id = tt.team_id
 AND (p_in_start_date < A.date_created AND A.date_created <= p_in_end_date));
v_stage := 'Success';

v_rec_cnt := SQL%ROWCOUNT;
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  p_out_return_code := 00;

EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code := 99;
   p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;
   prc_execution_log_entry(c_process_name, c_release_level, 'ERROR',v_parm_list||':'||p_out_error_message, NULL);
END;

END;
/