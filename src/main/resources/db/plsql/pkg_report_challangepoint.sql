CREATE OR REPLACE PACKAGE pkg_report_challengepoint
  IS

  PROCEDURE P_RPT_CP_SELECTION_DETAIL(p_in_requested_user_id      IN  NUMBER,
                                 p_in_start_date             IN  DATE,
                                 p_in_end_date               IN  DATE,
                                 p_out_return_code           OUT NUMBER,
                                 p_out_error_message         OUT VARCHAR2);

  PROCEDURE P_RPT_CP_SELECTION_SUMMARY(p_in_requested_user_id      IN  NUMBER,
                                       p_in_start_date             IN  DATE,
                                       p_in_end_date               IN  DATE,
                                       p_out_return_code           OUT NUMBER,
                                       p_out_error_message         OUT VARCHAR2);
  PROCEDURE P_RPT_CP_MANAGER_OVERRIDE (p_in_requested_user_id      IN  NUMBER,
                               p_out_return_code           OUT NUMBER,
                               p_out_error_message         OUT VARCHAR2); 
  
  FUNCTION F_GET_NBR_PAX_PER_LEVEL(
    p_in_node_id      IN NUMBER,
    p_in_promotion_id IN NUMBER,
    p_in_level IN NUMBER )
    return number;
    
  FUNCTION F_GET_TOTAL_PAX(
    p_in_node_id        IN VARCHAR2,
    p_in_promotion_id  IN VARCHAR2,
    p_in_challengepoint_selected IN VARCHAR2 )
    return number;
    
     FUNCTION fnc_get_manager_points
    (p_in_node_id      IN VARCHAR2,
     p_in_promotion_id IN VARCHAR2,     
     p_in_record_type  IN VARCHAR2)
     RETURN NUMBER;
       
END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_report_challengepoint IS

PROCEDURE P_RPT_CP_SELECTION_DETAIL(p_in_requested_user_id      IN  NUMBER,
                                    p_in_start_date             IN  DATE,
                                    p_in_end_date               IN  DATE,
                                    p_out_return_code           OUT NUMBER,
                                    p_out_error_message         OUT VARCHAR2)
IS
/*******************************************************************************
--
-- Purpose: Populate P_RPT_CP_SELECTION_DETAIL table.
--
-- MODIFICATION HISTORY
-- Person      Date         Comments
-- ---------   ----------   ------------------------------------------
-- S. Majumder 08/11/2008   Initial Version
-- S. Majumder 08/28/2008   Added two new columns(basic award earned and deposited) in detail table and removed manager column.
-- S. Majumder 09/18/2008   Added manager columns to both detail and summary tables
-- S. Majumder 09/19/2008   Added total_selected column into summary table
-- Ravi Dhanekula 06/13/2013  Added incremental processing for G5
-- Chidamba       09/11/2013  Bug#4226 - adding outer join condition and validating status_type = 'basic'
-- Chidamba       09/12/2013  Bug#4343 - Total Participants count should be updated with total 
                              eligible participants count for that promotion.   
--Ravi Dhanekula 07/31/2014  Added new processing for the records that are not included in rpt_pax_promo_eligbility.
--nagarajs       09/18/2014  Bug Fix 56658 - Fixed %of goal not matching issue
--nagarajs       09/26/2014  Bug Fix 56779 - Fixed %of goal not matching issue
-- murphyc       07/15/2016  Bug Fix 67566 - fix divide by zero error
--KrishnaDeepika 07/27/2016  Bug Fix 67906 - resolved the issue with achievement_amount calculation 
--chidamba       08/23/2017  G6-2898- Challengepoint Progress "Percentage of Goal" is rounding the half up
  Loganathan     03/11/2019  Bug 78631 - PKG_REPORTs without logs or return code
*******************************************************************************/
  c_created_by      CONSTANT RPT_CP_SELECTION_DETAIL.created_by%TYPE:=0;
  v_stage           VARCHAR2(250);
  v_challengepoint_level_id   INTEGER;
  v_commit_cnt      INTEGER;
  v_achieved        INTEGER;
  v_language_code   VARCHAR2(10):='en_US';
  v_parm_list     execution_log.text_line%TYPE;
  c_process_name  CONSTANT execution_log.process_name%TYPE         := 'P_RPT_CP_SELECTION_DETAIL';
  c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;

BEGIN

v_stage := 'initialize variables';
v_parm_list := 
'p_in_requested_user_id :='||p_in_requested_user_id ||CHR(10)||
'p_in_start_date        :='||p_in_start_date        ||CHR(10)|| 
'p_in_end_date          :='||p_in_end_date          ; 

v_stage :='Merging into rpt_cp_selection_detail table';
       MERGE INTO rpt_cp_selection_detail rpt
       USING (
       SELECT          participant_id,                       --09/12/2013
                       pax_goal_id,
                       level_id,
                       promotion_id,
                       promotion_name, 
                       goal_level_name,
                       node_id,               
                       job_position,
                       department,
                       promo_start_end_date,
                       first_name,
                       middle_init,
                       last_name,
                       user_status,
                       basic_award_deposited,
                       calculated_payout,
                       trans_date,
                       country_id,
                       current_value,
                       base_quantity,
                       CASE WHEN rounding_method = 'standard' THEN ROUND(achievement_amount,achievement_precision)            
                       WHEN rounding_method = 'up' AND achievement_precision = 0 THEN CEIL(achievement_amount)
                       WHEN rounding_method = 'up' AND achievement_precision <> 0 THEN ROUND(achievement_amount,achievement_precision)                     
                       WHEN rounding_method = 'down' THEN TRUNC(achievement_amount,achievement_precision)
                       ELSE achievement_amount END achievement_amount,
                       
                       CASE WHEN rounding_method = 'standard' AND ROUND(current_value,achievement_precision) >= ROUND(achievement_amount,achievement_precision) THEN 1             
                       WHEN rounding_method = 'up' AND achievement_precision = 0 AND CEIL(current_value)  >= CEIL(achievement_amount) THEN 1
                       WHEN rounding_method = 'up' AND achievement_precision <> 0 AND ROUND(current_value,achievement_precision) >= ROUND(achievement_amount,achievement_precision) THEN 1                              
                       WHEN rounding_method = 'down' AND TRUNC(current_value,achievement_precision) >= TRUNC(achievement_amount,achievement_precision) THEN 1
                       ELSE 0 END achieved,
                       ROUND(
                       CASE WHEN rounding_method = 'standard' THEN NVL((ROUND(current_value,achievement_precision) /
                                   DECODE(ROUND(achievement_amount,achievement_precision),0,1,ROUND(achievement_amount,achievement_precision))*100 ),0)            
                       WHEN rounding_method = 'up' AND achievement_precision = 0 THEN NVL((CEIL(current_value)  / 
                                   DECODE(CEIL(achievement_amount),0,1,CEIL(achievement_amount))*100),0)
                       WHEN rounding_method = 'up' AND achievement_precision <> 0 THEN NVL((ROUND(current_value,achievement_precision) / 
                                   DECODE(ROUND(achievement_amount,achievement_precision),0,1,ROUND(achievement_amount,achievement_precision))*100 ),0)                             
                       WHEN rounding_method = 'down' THEN NVL((TRUNC(current_value,achievement_precision) /
                                   DECODE(TRUNC(achievement_amount,achievement_precision),0,1,TRUNC(achievement_amount,achievement_precision))*100),0)
                       ELSE 0 END ,achievement_precision) progress_challengepoint ,
                       calculated_threshold,
                       threshold      
       FROM
       (SELECT  au.user_id participant_id,                       --08/23/2017 G6-2898
                       gp.pax_goal_id,
                       gp.goallevel_id level_id,
                       b.promotion_id,
                       p.promotion_name, 
                       gg.goal_level_cm_asset_code goal_level_name,
                       un.node_id,               
                       e.position_type job_position,
                       e.department_type department,
                      fnc_locale_to_char_dt(TRUNC(promotion_start_date), v_language_code)||' - '|| fnc_locale_to_char_dt(TRUNC(promotion_end_date),v_language_code) as promo_start_end_date,
                       au.first_name,
                       au.middle_name middle_init,
                       au.last_name,
                       pa.status user_status,
                       ba.basic_award_deposited,
                       ba2.payout calculated_payout,
                       NVL(gp.date_modified,gp.date_created) trans_date,
                       ua.country_id,
                       gp.current_value,
                       gp.base_quantity,
                       DECODE ( pg.achievement_rule,'perofbase',(gp.base_quantity/100)* gg.achievement_amount,
--                             'baseFixed',(gp.base_quantity/100)* gg.achievement_amount+ gg.achievement_amount, --07/27/2016
                                'baseFixed',gp.base_quantity+ gg.achievement_amount, --07/27/2016
                             gg.achievement_amount) achievement_amount,          
                
            -- 08/23/2017  G6-2898 commented to calculate with achievement precision 
                        /*      CASE WHEN pg.achievement_rule = 'fixed' 
                              AND gp.current_value >= gg.achievement_amount 
                              THEN  1
                            WHEN pg.achievement_rule = 'perofbase'                               
                              AND gp.current_value >= ((gg.achievement_amount/100)* gp.base_quantity) THEN  1 
                            WHEN pg.achievement_rule = 'baseFixed' 
                              AND gp.current_value >= (gg.achievement_amount+NVL(gp.base_quantity,0)) THEN  1                               
                        ELSE 0
                       END achieved,  */ --08/23/2017  G6-2898               
                       --DECODE(gp.goallevel_id,NULL, NULL,NVL(gp.current_value/gg.achievement_amount*100,0)) AS progress_challengepoint, --09/18/2014 Commented
--                       DECODE(gp.goallevel_id,NULL, NULL,NVL(ROUND(gp.current_value/                                  --09/18/2014 Added -- 07/15/2016 commented
--                       DECODE ( pg.achievement_rule,'perofbase',(gp.base_quantity/100)* gg.achievement_amount,
--                             'baseFixed',(gp.base_quantity/100)* gg.achievement_amount+ gg.achievement_amount,
--                             gg.achievement_amount)
--                       *100),0)) AS progress_challengepoint,
                       /*CASE                                           -- 07/15/2016 start
                         WHEN NVL(gp.base_quantity,0) + gg.achievement_amount = 0 
                            AND pg.achievement_rule = 'baseFixed' THEN
                         CASE
                           WHEN gp.current_value >= (NVL(gp.base_quantity,0) + gg.achievement_amount) THEN '100'
                           ELSE '0'
                         END
                         WHEN ((NVL(gp.base_quantity,0) = 0 
                            OR gg.achievement_amount = 0)
                            AND pg.achievement_rule = 'perofbase') THEN
                         CASE
                           WHEN gp.current_value >= 0 THEN '100'
                           ELSE '0'
                         END                                         -- 07/15/2016 end
                       ELSE
                         DECODE(gp.goallevel_id,NULL, NULL,NVL(ROUND(gp.current_value/                                   --09/18/2014 Added
                         DECODE ( pg.achievement_rule,'perofbase',(gp.base_quantity/100)* gg.achievement_amount,
                         --    'baseFixed',(gp.base_quantity/100)* gg.achievement_amount+ gg.achievement_amount,  -- commented 07/27/2016
                             'baseFixed',gp.base_quantity+ gg.achievement_amount,  -- added 07/27/2016
                               gg.achievement_amount)
                         *100),0))
                       END AS progress_challengepoint,*/  --08/23/2017  G6-2898
                       
                        pg.rounding_method, --08/23/2017  G6-2898
                       CASE WHEN pg.achievement_precision ='zero' THEN 0 --08/23/2017  G6-2898
                                WHEN pg.achievement_precision ='one' THEN 1
                                WHEN pg.achievement_precision ='two' THEN 2
                                WHEN pg.achievement_precision ='three' THEN 3
                                WHEN pg.achievement_precision ='four' THEN 4
                                ELSE 0 END  achievement_precision  ,
                       DECODE(b.award_threshold_type,'fixed',award_threshold_value,'perofbase',(award_threshold_value/100)*gp.base_quantity,0) calculated_threshold,
                       CASE WHEN 
                       CURRENT_VALUE >= DECODE(b.award_threshold_type,'fixed',award_threshold_value,'perofbase',(award_threshold_value/100)*gp.base_quantity,0) THEN 1
                       ELSE 0
                       END AS THRESHOLD
                FROM   promo_challengepoint b,                                   
                       promotion p,
                       user_node un,
                       participant pa,
                       application_user au,
                       user_address ua,
                       promo_goalquest pg,
                       goalquest_paxgoal gp
                       LEFT OUTER JOIN 
                            (SELECT award_earned  basic_award_deposited, user_id,promotion_id from (select RANK() OVER (PARTITION BY r.promotion_id,user_id ORDER BY r.date_created DESC) as rec_rank,R.*                
                        FROM challengepoint_award R WHERE LOWER(award_type) = 'basic' ) rr where rr.rec_rank=1) ba   --09/11/2013 bugfix#4226 adding award type = 'basic'
                    ON  ba.user_id = gp.user_id                      --09/11/2013 bugfix#4226  
                    AND ba.promotion_id = gp.promotion_id
                    LEFT OUTER JOIN 
                            (SELECT total_award_issued  payout, user_id,promotion_id from (select RANK() OVER (PARTITION BY r.promotion_id,user_id ORDER BY r.date_created DESC) as rec_rank,R.*                
                        FROM challengepoint_award R WHERE LOWER(award_type) = 'challengepoint' ) rr where rr.rec_rank=1) ba2
                    ON  ba2.user_id = gp.user_id
                    AND ba2.promotion_id = gp.promotion_id
                    LEFT OUTER JOIN goalquest_goallevel gg
                    ON gp.goallevel_id = gg.goallevel_id
                    LEFT OUTER JOIN vw_curr_pax_employer e
                    ON gp.user_id = e.user_id
                  WHERE b.promotion_id = pg.promotion_id
                    AND b.promotion_id = p.promotion_id
                    AND un.is_primary = 1      
                    AND un.user_id = au.user_id
                    AND au.user_id = pa.user_id                    
                    AND au.user_id = ua.user_id
                    AND ua.is_primary = 1                    
                    AND b.promotion_id = gp.promotion_id        
                   AND gp.user_id = au.user_id)
            UNION
           SELECT    rpt.participant_id,                       --09/12/2013
                       NULL pax_goal_id,
                       Null level_id,
                       rpt.promotion_id,
                       p.promotion_name, 
                       NULL goal_level_name,
                       un.node_id,
                       e.position_type job_position,
                       e.department_type department,
                       fnc_locale_to_char_dt(TRUNC(promotion_start_date), v_language_code)||' - '|| fnc_locale_to_char_dt(TRUNC(promotion_end_date),v_language_code) as promo_start_end_date,
                       au.first_name,
                       au.middle_name middle_init,
                       au.last_name,
                       pa.status user_status,
                       NULL basic_award_deposited,
                       NULL calculated_payout,
                       NULL trans_date,
                       ua.country_id,
                       NULL current_value,
                       NULL base_quantity,
                       NULL achievement_amount,
                       NULL achieved,                    
                       NULL progress_challengepoint,
                       NULL calculated_threshold,
                       NULL threshold 
                  FROM rpt_pax_promo_eligibility rpt,
                       promotion p,
                       user_node un,
                       participant pa,
                       application_user au,
                       user_address ua,
                       vw_curr_pax_employer e
            WHERE rpt.participant_id = au.user_id
              AND rpt.promotion_id   = p.promotion_id
              AND rpt.participant_id  = un.user_id
              AND rpt.node_id = un.node_id
              AND au.user_id = pa.user_id
              AND au.useR_id = ua.user_id(+)
              and au.user_id = e.user_id(+)
              AND un.is_primary =  1
              AND ua.is_primary (+)= 1
              AND rpt.promotion_id IN(SELECT promotion_id 
                                    FROM promotion 
                                   WHERE promotion_type ='challengepoint' and promotion_status = 'live')
              AND NOT EXISTS (select * from goalquest_paxgoal where promotion_id=rpt.promotion_id and user_id= rpt.participant_id)
              UNION
              SELECT    pa.user_id participant_id,                       --09/12/2013
                       NULL pax_goal_id,
                       Null level_id,
                       p.promotion_id,
                       p.promotion_name, 
                       NULL goal_level_name,
                       un.node_id,
                       e.position_type job_position,
                       e.department_type department,
                       fnc_locale_to_char_dt(TRUNC(promotion_start_date), v_language_code)||' - '|| fnc_locale_to_char_dt(TRUNC(promotion_end_date),v_language_code) as promo_start_end_date,
                       au.first_name,
                       au.middle_name middle_init,
                       au.last_name,
                       pa.status user_status,
                       NULL basic_award_deposited,
                       NULL calculated_payout,
                       NULL trans_date,
                       ua.country_id,
                       NULL current_value,
                       NULL base_quantity,
                       NULL achievement_amount,
                       NULL achieved,                    
                       NULL progress_challengepoint,
                       NULL calculated_threshold,
                       NULL threshold 
                  FROM promotion p,
                       user_node un,
                       participant pa,
                       application_user au,
                       user_address ua,
                       vw_curr_pax_employer e
            WHERE
            p.promotion_type ='challengepoint' 
               AND promotion_status = 'live' 
               AND primary_audience_type = 'allactivepaxaudience'     
              AND au.user_id = pa.user_id
              AND pa.user_id = un.user_id
              AND pa.status = 'active'
              AND au.user_id = ua.user_id(+)
              and au.user_id = e.user_id(+)
              AND un.is_primary =  1
              AND ua.is_primary (+)= 1              
              AND NOT EXISTS (select * from goalquest_paxgoal where promotion_id=p.promotion_id and user_id= pa.user_id)                    
              ) s
          ON (rpt.user_id = s.participant_id AND rpt.promotion_id = s. promotion_id)
        WHEN MATCHED THEN UPDATE 
         SET last_name = s.last_name,
                first_name = s.first_name,
                middle_init = s.middle_init,
                user_status = nvl(s.user_status,' '),
                job_position = nvl(s.job_position,' '),
                department = nvl(s.department,' '),
                node_id = s.node_id,              
                promo_start_end_date = s.promo_start_end_date,
                pax_goal_id = s.pax_goal_id,
                current_value = s.current_value,
                base_quantity = s.base_quantity,
                threshold = s.threshold,
                amount_to_achieve = s.achievement_amount,
                progress_challengepoint = s.progress_challengepoint,
                level_id    = s.level_id,
                version     = version+1,
                achieved    = s.achieved,
                promotion_name = s.promotion_name,
                level_name  = s.goal_level_name,
                basic_award_deposited = s.basic_award_deposited,
                calculated_threshold = s.calculated_threshold,
                calculated_payout = s.calculated_payout,
                trans_date = s.trans_date,
                country_id = s.country_id
        WHERE NOT (   DECODE(rpt.first_name,  s.first_name,                    1, 0) = 1
                 AND DECODE(rpt.middle_init, s.middle_init,                   1, 0) = 1
                 AND DECODE(rpt.last_name,   s.last_name,                     1, 0) = 1
                 AND DECODE(rpt.user_status,   s.user_status,                     1, 0) = 1
                 AND DECODE(rpt.job_position,   s.job_position,                     1, 0) = 1
                 AND DECODE(rpt.department,   s.department,                     1, 0) = 1
                 AND DECODE(rpt.node_id,   s.node_id,                     1, 0) = 1
                 AND DECODE(rpt.promo_start_end_date,   s.promo_start_end_date,                     1, 0) = 1
                 AND DECODE(rpt.pax_goal_id,   s.pax_goal_id,                     1, 0) = 1
                 AND DECODE(rpt.current_value,   s.current_value,                     1, 0) = 1
                 AND DECODE(rpt.base_quantity,   s.base_quantity,                     1, 0) = 1
                 AND DECODE(rpt.threshold,   s.threshold,                     1, 0) = 1
                 AND DECODE(rpt.amount_to_achieve,   s.achievement_amount,                     1, 0) = 1
                 AND DECODE(rpt.achieved,   s.achieved,                     1, 0) = 1
                 AND DECODE(rpt.progress_challengepoint,   s.progress_challengepoint,                     1, 0) = 1
                 AND DECODE(rpt.level_id,   s.level_id,                     1, 0) = 1
                 AND DECODE(rpt.promotion_name,   s.promotion_name,                     1, 0) = 1
                 AND DECODE(rpt.level_name,   s.goal_level_name,                     1, 0) = 1
                 AND DECODE(rpt.basic_award_deposited,   s.basic_award_deposited,                     1, 0) = 1
                 AND DECODE(rpt.calculated_payout,   s.calculated_payout,                     1, 0) = 1
                 AND DECODE(rpt.country_id,   s.country_id,                     1, 0) = 1                 
                 )
        WHEN NOT MATCHED THEN INSERT
                (rpt_cp_selection_detail_id,
                user_id,
                last_name,
                first_name,
                middle_init,
                user_status,
                job_position,
                department,
                node_id,
                promotion_id,
                promo_start_end_date,
                pax_goal_id,
                current_value,
                base_quantity,
                threshold,
                amount_to_achieve,
                progress_challengepoint,
                level_id,
                date_created,
                created_by,
                version,
                achieved,
                promotion_name,
                level_name,
                basic_award_deposited,
                calculated_threshold,
                calculated_payout,
                trans_date,
                country_id)
         VALUES(rpt_cp_selection_detail_sq.nextval,
                s.participant_id,
                s.last_name,
                s.first_name,
                s.middle_init,
                nvl(s.user_status,' '),
                nvl(s.job_position,' '),
                nvl(s.department,' '),
                s.node_id,
                s.promotion_id,
                s.promo_start_end_date,
                s.pax_goal_id,
                s.current_value,
                s.base_quantity,
                s.threshold,
                s.achievement_amount,
                s.progress_challengepoint,
                s.level_id,
                sysdate,
                c_created_by,
                0,
                s.achieved,
                s.promotion_name,
                s.goal_level_name,
                s.basic_award_deposited,
                s.calculated_threshold,
                s.calculated_payout,
                s.trans_date,
                s.country_id);

        v_commit_cnt := v_commit_cnt + 1;
        IF v_commit_cnt > 500 THEN
          v_commit_cnt := 0;
        END IF;
--  END LOOP; --rec_pax
p_out_return_code:=0;
EXCEPTION
  WHEN others THEN
     --dbms_output.put_line(v_stage||' sqlerrm: '||sqlerrm);
     --execution logs:1:2 TBD
     p_out_return_code:=99;
     prc_execution_log_entry(c_process_name, c_release_level, 'Error', v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
     
END;

PROCEDURE P_rpt_cp_selection_summary(p_in_requested_user_id      IN  NUMBER,
                                     p_in_start_date             IN  DATE,
                                     p_in_end_date               IN  DATE,
                                     p_out_return_code           OUT NUMBER,
                                     p_out_error_message         OUT VARCHAR2)
IS
---------------------------------------------------------------------------------------------------------------
--
-- Purpose: Populate rpt_cp_selection_summary table.
--
-- MODIFICATION HISTORY
-- Person             Date        Comments
-- ---------          ------      ------------------------------------------
-- Sandip Majumder    08/13/2008  Initial code.
-- Chidamba           08/06/2013  Changes to populate base_quantity, goal and actual_result columns.   
-- Chidamba           08/06/2013  Change to total_participants != 0 instead of  total_selected != 0 
-- nagarajs           09/30/2013  Fixed Defect #4722
-- Suresh J           05/13/2014  Bug#53315 Performance issue in Challenge point summary report 
-- Swati              06/05/2014  Bug 53853 - Reports: Product Claim - ORA-30926: unable to get a 
--                                    stable set of rows in the source tables - error occurs 
--Ravi Dhanekula 06/18/2014 Added performance fix for default summary.
-- nagarajs      07/22/2014 Fixed duplicate row issue
--Ravi Dhanekula 07/30/2014 Added fix for report refresh issue.
--                 08/07/2014 We no longer need the default permutaions data As we are taking all eligible pax into considerration in detail.
--Suresh J          08/11/2014 Bug fix 55650 Commented the above line that was causing compilation error.
--Suresh J       09/03/2014  Bug#56207 -Report Refresh issue. Modified Merge Key and UPDATE section
--Suresh J       09/04/2014  Bug#56286 -Report Refresh issue. Modified Merge Key and UPDATE section  
--nagarajs       09/11/2014  Fixed Report Refresh issue
--nagarajs       09/11/2014  Fixed Report count issues
--Chidamba       08/24/2017  G6-2895 Report 'Challengepoint Participant Achievement' is not showing the value in the field % of goa
--------------------------------------------------------------------------------------------------------------
  
  c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('P_rpt_cp_selection_summary_t');
  c_release_level      CONSTANT execution_log.release_level%TYPE := 2;   
  c_created_by         CONSTANT rpt_enrollment_detail.created_by%TYPE:= p_in_requested_user_id;
  v_stage              execution_log.text_line%TYPE;  
  v_rec_cnt            INTEGER;
  v_commit_cnt         INTEGER;  
  v_parm_list     execution_log.text_line%TYPE; --03/11/2019  Bug 78631
 

BEGIN
v_stage := 'initialize variables';  --03/11/2019  Bug 78631
v_parm_list := 
'p_in_requested_user_id :='||p_in_requested_user_id ||CHR(10)||  
'p_in_start_date        :='||p_in_start_date        ||CHR(10)|| 
'p_in_end_date          :='||p_in_end_date          ; 
   v_stage := 'Start';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  -- remove obsolete node summaries (node not found in rpt_hierarchy)
  v_stage := 'DELETE obsolete node summary records';
  DELETE
    FROM rpt_cp_selection_summary s
   WHERE s.detail_node_id NOT IN
         ( -- get node ID currently in the report hierarchy
           SELECT h.node_id
             FROM rpt_hierarchy h
         );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

      -- merge derived summary records
   v_stage := 'MERGE detail derived summary records';
   
   MERGE INTO rpt_cp_selection_summary d
   USING (
           WITH rpt_teamsum AS
            (            
            SELECT h.hier_level                    
                    ,h.parent_node_id              AS header_node_id 
                    ,nvl(d.node_id,h.node_id)      AS detail_node_id 
                    ,nvl(d.user_status,' ')        AS pax_status
                    ,NVL(d.job_position,' ')       AS job_position
                    ,NVL(d.department,' ')         AS department
                    ,SUM(NVL(d.threshold,0))       AS nbr_threshold_reached 
                    ,NVL(d.promotion_id,0)         AS promotion_id              --05/13/2014
                    ,NVL(d.promo_start_end_date,' ') AS promo_start_end_date    --05/13/2014
                    ,g.goallevel_id                 AS level_id                  --05/13/2014 --09/12/2014 Removed NVL
                    ,COUNT(d.user_id)              AS total_participants
                    ,COUNT(d.level_id)             AS total_selected
                    ,SUM(achieved)                 AS nbr_challengepoint_achieved
                    ,SUM(NVL(d.calculated_payout,0) + NVL(d.basic_award_deposited, 0)) calculated_payout
                    ,NVL(d.manager,0) AS manager                                --05/13/2014
                    ,SUM(CASE WHEN d.PROGRESS_CHALLENGEPOINT BETWEEN 0 AND 25
                        THEN 1 END) nbr_between_0_25
                    ,SUM(CASE WHEN d.PROGRESS_CHALLENGEPOINT BETWEEN 26 AND 50
                        THEN 1 END) nbr_between_26_50
                    ,SUM(CASE WHEN d.PROGRESS_CHALLENGEPOINT BETWEEN 51 AND 75
                        THEN 1 END) nbr_between_51_75
                    ,SUM(CASE WHEN d.PROGRESS_CHALLENGEPOINT BETWEEN 76 AND 99
                        THEN 1 END) nbr_between_76_99
                    ,SUM(CASE WHEN d.PROGRESS_CHALLENGEPOINT > 99
                        THEN 1 END) nbr_over_100
                   ,sum(NVL(d.base_quantity,0)) base    --08/06/2013
                   ,sum(NVL(d.amount_to_achieve,0)) goal  --08/06/2013
                   --,sum(NVL(d.current_value,0)) actual_result  --08/06/2013  --08/24/2017 G6-2895 commented
                   ,ROUND (SUM ( CASE WHEN rounding_method = 'standard' THEN ROUND(d.current_value,achievement_precision)            
                                                          WHEN rounding_method = 'up' AND achievement_precision = 0 THEN CEIL(d.current_value)
                                                          WHEN rounding_method = 'up' AND achievement_precision <> 0 THEN ROUND(d.current_value,achievement_precision)                     
                                                          WHEN rounding_method = 'down' THEN TRUNC(d.current_value,achievement_precision)
                                                     ELSE d.current_value
                                                     END),
                                            MAX (achievement_precision)) actual_result  --08/24/2017 G6-2895
               FROM rpt_cp_selection_detail d
                   ,rpt_hierarchy h
                   ,goalquest_paxgoal g
                   ,(select CASE WHEN p.achievement_precision ='zero' THEN 0
                                                       WHEN p.achievement_precision ='one' THEN 1
                                                       WHEN p.achievement_precision ='two' THEN 2
                                                       WHEN p.achievement_precision ='three' THEN 3
                                                       WHEN p.achievement_precision ='four' THEN 4
                                                   ELSE 0 
                                                   END  achievement_precision,p.promotion_id,p.rounding_method from promo_goalquest p ) promoGoal --08/24/2017 G6-2895
              WHERE h.node_id = d.node_id --07/22/2014
                 AND d.pax_goal_id = g.pax_goal_id(+)
                 AND d.promotion_id = promoGoal.promotion_id --08/24/2017 G6-2895
                 --AND (d.date_created > p_in_start_date AND d.date_created <= p_in_end_date  
                   --  OR d.date_modified > p_in_start_date AND d.date_modified <= p_in_end_date) 
               GROUP BY h.parent_node_id ,nvl(d.node_id,h.node_id)
                      , nvl(job_position,' '), nvl(department,' '), nvl(user_status,' '), nvl(manager,0) --09/11/2014 Added NVL
                      , nvl(d.promotion_id,0), nvl(d.promo_start_end_date,' '), g.goallevel_id, h.hier_level --09/11/2014 Added NVL
            ), detail_derived_summary AS
            (  -- derive summaries based on detail data
               SELECT -- key fields
                      rt.detail_node_id,
                      'teamsum' AS sum_type,                                       
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.nbr_threshold_reached,
                      rt.manager,
                      rt.promotion_id,     
                      rt.promo_start_end_date,
                      -- reference fields
                      rt.level_id,                      
                      rt.header_node_id,
--                      rt.hier_level,                      
                      1 AS is_leaf,  -- team summary always a leaf node
                      -- count fields
                      rt.total_selected,
                      rt.total_participants,
                      rt.nbr_challengepoint_achieved,
                      rt.calculated_payout,
                      rt.nbr_between_0_25,
                      rt.nbr_between_26_50,
                      rt.nbr_between_51_75,
                      rt.nbr_between_76_99,
                      rt.nbr_over_100 ,
                      rt.base,
                      rt.goal,
                      rt.actual_result                 
                 FROM rpt_teamsum rt
                UNION ALL
               SELECT -- key fields
                      h.node_id AS detail_node_id,
                      'nodesum' AS sum_type,                                       
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      SUM(rt.nbr_threshold_reached) nbr_threshold_reached, --09/11/2014 Added SUM
                      rt.manager,
                      rt.promotion_id,     
                      rt.promo_start_end_date,
                      -- reference fields
                      rt.level_id,
                      h.parent_node_id AS header_node_id,
--                      h.hier_level,
                      NVL(h.is_leaf, 1) AS is_leaf,
                      -- count fields
                      SUM(rt.total_selected) AS total_selected,
                      SUM(rt.total_participants) AS total_participants,
                      SUM(rt.nbr_challengepoint_achieved) AS nbr_challengepoint_achieved,
                      SUM(rt.calculated_payout) AS calculated_payout,
                      SUM(rt.nbr_between_0_25)  AS nbr_between_0_25,
                      SUM(rt.nbr_between_26_50) AS nbr_between_26_50,
                      SUM(rt.nbr_between_51_75) AS nbr_between_51_75,
                      SUM(rt.nbr_between_76_99) AS nbr_between_76_99,
                      SUM(rt.nbr_over_100) AS nbr_over_100,
                      SUM(rt.base) AS base,  --08/06/2013
                      SUM(rt.goal) AS goal,  --08/06/2013
                      SUM(rt.actual_result) AS actual_result    --08/06/2013                          
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
                WHERE (  rt.hier_level = h.hier_level  --rt.detail_node_id IS NULL -- non-pax --09/11/2014 Uncommented
                      OR total_participants != 0                 --09/12/2013  -- OR rt.recognition_cnt != 0     --replace OR with AND 07/22/2014        
                      )
                   -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  AND rt.detail_node_id = npn.node_id --07/22/2014
                  AND npn.path_node_id = h.node_id  --07/22/2014
                GROUP BY h.node_id,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      --rt.nbr_threshold_reached, --09/11/2014 commented
                      rt.manager,
                      rt.level_id,
                      rt.promotion_id,     
                      rt.promo_start_end_date,
                      h.parent_node_id,
--                      h.hier_level,
                      NVL(h.is_leaf,1)                                          --05/13/2014
            ) -- end detail_derived_summary            
            -- compare existing summary records with detail derived summaries
            SELECT es.s_rowid,
--                   NVL(dds.hier_level, 0) || '-' || 
                   dds.sum_type AS record_type,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.detail_node_id,
                            SUBSTR(s2.record_type, -7) AS sum_type,
                            s2.promotion_id,                            
                            s2.level_id,
                            s2.manager,
                            s2.promo_start_end_date,
                            s2.pax_status,
                            s2.job_position,
                            s2.department,
                            s2.hier_level,      
                            s2.total_participants,
                            s2.total_selected,
                            s2.nbr_threshold_reached,
                            s2.nbr_challengepoint_achieved,
                            s2.calculated_payout,
                            s2.nbr_pax_25_percent_of_cp  nbr_pax_25_percent_of_cp,
                            s2.nbr_pax_50_percent_of_cp  nbr_pax_50_percent_of_cp,
                            s2.nbr_pax_75_percent_of_cp  nbr_pax_75_percent_of_cp,
                            s2.nbr_pax_76_99_percent_of_cp nbr_pax_76_99_percent_of_cp,
                            s2.nbr_pax_100_percent_of_cp   nbr_pax_100_percent_of_cp
                       FROM rpt_cp_selection_summary s2
                      WHERE s2.promo_start_end_date IS NOT NULL) es
                   -- full outer join so unmatched existing summaries can be deleted
                   FULL OUTER JOIN detail_derived_summary dds --07/22/2014
                   ON (   --DECODE(es.detail_node_id, dds.detail_node_id, 1, 0) = 1--07/22/2014  --09/11/2014 commented
                          es.detail_node_id = dds.detail_node_id  --09/11/2014 Added
                      AND es.sum_type         = dds.sum_type
--                    AND es.nbr_threshold_reached = dds.nbr_threshold_reached       --09/30/2013                  
                      AND es.promotion_id     = dds.promotion_id
                      AND es.manager          = dds.manager
                      AND es.pax_status       = dds.pax_status
                      AND es.job_position     = dds.job_position
                      AND es.department       = dds.department
                      AND es.promo_start_end_date = dds.promo_start_end_date
                      AND NVL(es.level_id,0) = NVL(dds.level_id,0) --07/30/2014 --09/12/2014 Added NVL
                      --AND es.promo_start_end_date IS NOT NULL   --09/03/2014 --09/11/2014 commented
                      )
                ) s
       ON (d.ROWID = s.s_rowid
            --AND d.record_type = s.sum_type) --06/05/2014  Bug 53853
           )
    WHEN MATCHED THEN
      UPDATE SET
--         d.manager = s.manager,    --09/03/2014
--         d.header_node_id =   s.header_node_id,   09/04/2014
--         d.level_id =       s.level_id,      --09/03/2014
         d.is_leaf  =            s.is_leaf,
--         d.hier_level  =         s.hier_level,
         d.total_participants = s.total_participants,
         d.nbr_threshold_reached = s.nbr_threshold_reached, --09/30/2013
         d.total_selected = s.total_selected,
         d.calculated_payout = s.calculated_payout,
         d.nbr_challengepoint_achieved =  s.nbr_challengepoint_achieved,
         d.nbr_pax_25_percent_of_cp =  s.nbr_between_0_25,
         d.nbr_pax_50_percent_of_cp =  s.nbr_between_26_50,
         d.nbr_pax_75_percent_of_cp =  s.nbr_between_51_75,
         d.nbr_pax_76_99_percent_of_cp = s.nbr_between_76_99,
         d.nbr_pax_100_percent_of_cp = s.nbr_over_100,
         d.base_quantity = s.base,  --08/06/2013
         d.goal = s.goal,           --08/06/2013
         d.actual_result = s.actual_result, --08/06/2013
         d.modified_by        = c_created_by,
         d.date_modified      = SYSDATE
       WHERE ( -- only update records with different values
                -- DECODE(d.manager,            s.manager,          1, 0) = 0
--         DECODE(d.header_node_id,     s.header_node_id,   1, 0) = 0   09/04/2014
--         OR DECODE(d.level_id,       s.level_id,     1, 0) = 0   --09/03/2014
         DECODE(d.is_leaf,            s.is_leaf,          1, 0) = 0  --09/04/2014
--         OR DECODE(d.hier_level,         s.hier_level,         1, 0) = 0
         OR DECODE(d.total_participants, s.total_participants, 1, 0) = 0
         OR DECODE(d.total_selected,  s.total_selected,  1, 0) = 0
         OR DECODE(d.calculated_payout,  s.calculated_payout,  1, 0) = 0
         OR DECODE(d.nbr_challengepoint_achieved,   s.nbr_challengepoint_achieved,   1, 0) = 0
         OR DECODE(d.nbr_pax_25_percent_of_cp,  s.nbr_between_0_25,  1, 0) = 0
         OR DECODE(d.nbr_pax_50_percent_of_cp,  s.nbr_between_26_50,  1, 0) = 0
         OR DECODE(d.nbr_pax_75_percent_of_cp,  s.nbr_between_51_75,  1, 0) = 0
         OR DECODE(d.nbr_pax_76_99_percent_of_cp,       s.nbr_between_76_99,       1, 0) = 0
         OR DECODE(d.nbr_pax_100_percent_of_cp,       s.nbr_over_100,       1, 0) = 0
         OR DECODE(d.nbr_threshold_reached,       s.nbr_threshold_reached,       1, 0) = 0 --09/30/2013
         OR DECODE(d.base_quantity,       s.base,       1, 0) = 0  --08/06/2013
         OR DECODE(d.goal,       s.goal,       1, 0) = 0            --08/06/2013
         OR DECODE(d.actual_result,       s.actual_result,       1, 0) = 0  --08/06/2013
             )   
    WHEN NOT MATCHED THEN
      INSERT
      ( rpt_cp_selection_summary_id,
        header_node_id,
        detail_node_id,   
        total_participants,        
        total_selected,       
        level_id,           
        manager,   
        promotion_id,
        promo_start_end_date,        
        pax_status, 
        job_position,        
        department,    
--        hier_level,         
        is_leaf,        
        nbr_threshold_reached,  
        nbr_challengepoint_achieved,       
        calculated_payout,     
        record_type,       
        nbr_pax_25_percent_of_cp,         
        nbr_pax_50_percent_of_cp,         
        nbr_pax_75_percent_of_cp,          
        nbr_pax_76_99_percent_of_cp,         
        nbr_pax_100_percent_of_cp,  
        base_quantity,  --08/06/2013
        goal,           --08/06/2013
        actual_result,  --08/06/2013      
        date_created,
        created_by    
      )
      VALUES
      ( rpt_cp_selection_summary_sq.NEXTVAL,
        s.header_node_id,
        s.detail_node_id,   
        s.total_participants,        
        s.total_selected,       
        s.level_id,           
        s.manager,   
        s.promotion_id,
        s.promo_start_end_date,        
        s.pax_status, 
        s.job_position,        
        s.department,    
--        s.hier_level,         
        s.is_leaf,        
        s.nbr_threshold_reached,  
        s.nbr_challengepoint_achieved,       
        s.calculated_payout,     
        s.record_type,       
        s.nbr_between_0_25,         
        s.nbr_between_26_50,         
        s.nbr_between_51_75,          
        s.nbr_between_76_99,         
        s.nbr_over_100, 
        s.base,     --08/06/2013
        s.goal,     --08/06/2013
        s.actual_result,    --08/06/2013          
        SYSDATE,
        c_created_by    
      );




   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   
   /* --08/07/2014 Start
   -- add missing default summary permutations
  v_stage := 'INSERT missing default team summary permutations';  
  INSERT INTO rpt_cp_selection_summary
      ( rpt_cp_selection_summary_id,
        header_node_id,
        detail_node_id,   
        total_participants,        
        total_selected,       
        level_id,           
        manager,   
        promotion_id,
        promo_start_end_date,        
        pax_status, 
        job_position,        
        department,    
        hier_level,         
        is_leaf,        
        nbr_threshold_reached,  
        nbr_challengepoint_achieved,       
        calculated_payout,     
        record_type,       
        nbr_pax_25_percent_of_cp,         
        nbr_pax_50_percent_of_cp,         
        nbr_pax_75_percent_of_cp,          
        nbr_pax_76_99_percent_of_cp,         
        nbr_pax_100_percent_of_cp, 
        base_quantity,  --08/06/2013
        goal,           --08/06/2013
        actual_result,  --08/06/2013            
        date_created,
        created_by)
     (SELECT rpt_cp_selection_summary_sq.NEXTVAL,
             -- key fields
             nsp.header_node_id,
             nsp.detail_node_id,
             0 AS total_participants,
             0 AS total_selected,
             NULL  AS level_id,
             NULL  AS manager,   
             0    AS promotion_id,
             NULL AS promo_start_end_date,        
             ' '  AS pax_status,
             ' '  AS job_position,
             ' '  AS department,    
             nsp.hier_level,
             nsp.is_leaf,       
             NULL AS nbr_threshold_reached,  
             NULL AS nbr_challengepoint_achieved,       
             NULL AS calculated_payout,     
             nsp.record_type,       
             NULL AS nbr_pax_25_percent_of_cp,         
             NULL AS nbr_pax_50_percent_of_cp,         
             NULL AS nbr_pax_75_percent_of_cp,          
             NULL AS nbr_pax_76_99_percent_of_cp,         
             NULL AS nbr_pax_100_percent_of_cp, 
             NULL AS base,     --08/06/2013
             NULL AS goal,     --08/06/2013
             NULL AS actual_result,    --08/06/2013
             -- audit fields             
             SYSDATE      AS date_created,
             c_created_by AS created_by            
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
                 FROM rpt_cp_selection_summary s
                WHERE s.detail_node_id = nsp.detail_node_id
                  AND s.record_type    = nsp.record_type
                   -- any media date value matches on team summary records because detail derived summaries replace default team permutations
             )  
   );
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   
   v_stage := 'INSERT missing default node summary permutations';   
   INSERT INTO rpt_cp_selection_summary
      ( rpt_cp_selection_summary_id,
        header_node_id,
        detail_node_id,   
        total_participants,        
        total_selected,       
        level_id,           
        manager,   
        promotion_id,
        promo_start_end_date,        
        pax_status, 
        job_position,        
        department,    
        hier_level,         
        is_leaf,        
        nbr_threshold_reached,  
        nbr_challengepoint_achieved,       
        calculated_payout,     
        record_type,       
        nbr_pax_25_percent_of_cp,         
        nbr_pax_50_percent_of_cp,         
        nbr_pax_75_percent_of_cp,          
        nbr_pax_76_99_percent_of_cp,         
        nbr_pax_100_percent_of_cp,  
        base_quantity,  --08/06/2013
        goal,           --08/06/2013
        actual_result,  --08/06/2013            
        date_created,
        created_by)
         (SELECT rpt_cp_selection_summary_sq.NEXTVAL,
                 -- key fields
                 nsp.header_node_id,
                 nsp.detail_node_id,
                 0 AS total_participants,
                 0 AS total_selected,
                 NULL  AS level_id,
                 NULL  AS manager,   
                 0    AS promotion_id,
                 NULL AS promo_start_end_date,        
                 ' '  AS pax_status,
                 ' '  AS job_position,
                 ' '  AS department,    
                 nsp.hier_level,
                 nsp.is_leaf,       
                 NULL AS nbr_threshold_reached,  
                 NULL AS nbr_challengepoint_achieved,       
                 NULL AS calculated_payout,     
                 nsp.record_type,       
                 NULL AS nbr_pax_25_percent_of_cp,         
                 NULL AS nbr_pax_50_percent_of_cp,         
                 NULL AS nbr_pax_75_percent_of_cp,          
                 NULL AS nbr_pax_76_99_percent_of_cp,         
                 NULL AS nbr_pax_100_percent_of_cp, 
                 NULL AS base,     --08/06/2013
                 NULL AS goal,     --08/06/2013
                 NULL AS actual_result,    --08/06/2013
                 -- audit fields             
                 SYSDATE      AS date_created,
                 c_created_by AS created_by            
            FROM ( -- get all possible node summary type permutations
                   SELECT h.node_id AS detail_node_id,
                          h.hier_level || '-' || 'nodesum' AS record_type,
                          h.parent_node_id AS header_node_id,
                          h.hier_level,
                          h.is_leaf
                     FROM rpt_hierarchy h
                 ) nsp
              -- exclude default permutation when a matching summary record exists
            WHERE NOT EXISTS
                 ( SELECT 1
                     FROM rpt_cp_selection_summary s
                    WHERE s.detail_node_id = nsp.detail_node_id
                      AND s.record_type    = nsp.record_type
                      AND TRIM(s.promo_start_end_date) IS NULL                  --05/13/2014
                 )
              -- default node summary permutation must have default team summary permutation in its hierarchy
             AND nsp.detail_node_id IN --EXISTS --06/18/2014
                 ( -- get team permutations under node permutation
                   SELECT tp.detail_node_id
                     FROM rpt_cp_selection_summary tp
                    WHERE SUBSTR(tp.record_type, -7) = 'teamsum'
                      AND TRIM(tp.promo_start_end_date) IS NULL                 --05/13/2014
                    UNION ALL
                   -- get team permutations under node permutation hierarchy
                   SELECT tp.header_node_id
                     FROM rpt_cp_selection_summary tp
                       -- start with child node immediately under current node
                    START WITH SUBSTR(tp.record_type, -7) = 'teamsum'
                           AND TRIM(tp.promo_start_end_date) IS NULL            --05/13/2014
                    CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                           AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
                           AND TRIM(tp.promo_start_end_date) IS NULL )          --05/13/2014              
            );
             
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   */ --08/07/2014 End
   -- remove team permutations when an associated detail derived summary exists
   v_stage := 'DELETE obsolete team permutations';
   DELETE rpt_cp_selection_summary
    WHERE ROWID IN
    (SELECT DISTINCT
           s.ROWID
      FROM rpt_cp_selection_summary s,
           rpt_cp_selection_summary dd
        -- substr matches functional index
     WHERE SUBSTR(dd.record_type, -7) = 'teamsum'
        -- detail derived summaries have a media date
       AND TRIM(dd.promo_start_end_date) IS NOT NULL        --05/13/2014
       AND dd.detail_node_id          = s.detail_node_id
       AND SUBSTR(dd.record_type, -7) = SUBSTR(s.record_type, -7)
        -- default permutations have no media date
       AND TRIM(s.promo_start_end_date) IS NULL);           --05/13/2014
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- delete node permutation with no associated team permutation
   v_stage := 'DELETE obsolete node permutations with no associated team permutation';
   DELETE rpt_cp_selection_summary
    WHERE ROWID IN
   (SELECT np.ROWID
      FROM rpt_cp_selection_summary np
     WHERE SUBSTR(np.record_type, -7) = 'nodesum'
       AND TRIM(np.promo_start_end_date) IS NULL                --05/13/2014
        -- node permutation has no team permutation
       AND NOT EXISTS
           ( -- get team permutations under node permutation
             SELECT 1
               FROM rpt_cp_selection_summary tp
              WHERE tp.detail_node_id          = np.detail_node_id
                AND SUBSTR(tp.record_type, -7) = 'teamsum'
                AND TRIM(tp.promo_start_end_date) IS NULL           --05/13/2014
              UNION ALL
             -- get team permutations under node permutation hierarchy
             SELECT 1
               FROM rpt_cp_selection_summary tp
                 -- start with child node immediately under current node
              START WITH tp.header_node_id          = np.detail_node_id
                     AND SUBSTR(tp.record_type, -7) = 'teamsum'
                     AND TRIM(tp.promo_start_end_date) IS NULL          --05/13/2014
              CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                     AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
                     AND TRIM(tp.promo_start_end_date) IS NULL)         --05/13/2014
    ); 
   
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   
 p_out_return_code   := 00;  
 
      
EXCEPTION
  WHEN others THEN
     p_out_return_code   := 99;
     prc_execution_log_entry(c_process_name,
                          c_release_level,
                          'ERROR',
                          v_stage||':'||v_parm_list||':'||SQLERRM,
                          NULL);
     p_out_error_message := v_stage||SQLERRM;      
END;
FUNCTION F_GET_NBR_PAX_PER_LEVEL(
           p_in_node_id      IN NUMBER,
           p_in_promotion_id IN NUMBER,
           p_in_level        IN NUMBER )
  return number IS
-- Purpose: Return number of pax in each level from rpt_cp_selection_detail table
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Sandip Majumder      08/13/2008 Initial code.

v_pax_count   NUMBER :=0;

BEGIN

  IF p_in_level = 0 THEN
    SELECT count(1)
      INTO v_pax_count
      FROM rpt_cp_selection_detail gsd
     WHERE gsd.node_id = p_in_node_id
       AND gsd.promotion_id = p_in_promotion_id;
  ELSE
    SELECT count(1)
      INTO v_pax_count
      FROM rpt_cp_selection_detail gsd,
           goalquest_goallevel gl,
           goalquest_paxgoal pg
     WHERE gsd.pax_goal_id = pg.pax_goal_id
       AND pg.goallevel_id = gl.goallevel_id
       AND gsd.achieved = 1
       AND gsd.node_id = p_in_node_id
       AND gsd.promotion_id = p_in_promotion_id
       AND gl.sequence_num = p_in_level;
  END IF;

  return v_pax_count;

EXCEPTION
  WHEN OTHERS THEN
    return v_pax_count;

END;

FUNCTION F_GET_TOTAL_PAX(
           p_in_node_id      IN VARCHAR2,
           p_in_promotion_id IN VARCHAR2,
           p_in_challengepoint_selected IN VARCHAR2 )
  return number IS

--
--
-- Purpose: Number of pax selected challengepoint from a specific node under a certain CP promotion.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Sandip Majumder      08/13/2008 Initial code.

v_pax_count   NUMBER :=0;

BEGIN

  IF p_in_challengepoint_selected = 'true' THEN
    SELECT count(1)
      INTO v_pax_count
      FROM rpt_cp_selection_detail gsd
     WHERE gsd.node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id)))
       AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND gsd.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
       AND gsd.pax_goal_id is not null;
  ELSE
    SELECT count(1)
      INTO v_pax_count
      FROM rpt_cp_selection_detail gsd
     WHERE gsd.node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id)))       
       AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND gsd.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))))
       AND gsd.pax_goal_id is null;
  END IF;

  return v_pax_count;

EXCEPTION
  WHEN OTHERS THEN
    return v_pax_count;

END;

PROCEDURE P_RPT_CP_MANAGER_OVERRIDE (p_in_requested_user_id      IN  NUMBER,
                               p_out_return_code           OUT NUMBER,
                               p_out_error_message         OUT VARCHAR2) IS
  /*******************************************************************************
--
-- Purpose: Populate RPT_CP_MANAGER_OVERRIDE table.
--
-- MODIFICATION HISTORY
-- Person      Date         Comments
-- ---------   ----------   ------------------------------------------
--Ravi Dhanekula  09/30/2013  Initial-
--nagarajs        02/15/2016 Bug 65749 - Admin View/Challengepoint Manager Achievement : Points mismatch in between 
                            Total Manager points column in thAdmin View/Challenge point Manager Achievement : Points
                            mismatch in between" Total Manager points" ce report and the Statement page of the Manager
*******************************************************************************/
  
  c_created_by      CONSTANT rpt_cp_manager_override.created_by%TYPE:= p_in_requested_user_id;
  v_stage           VARCHAR2(250);  
  v_language_code   VARCHAR2(10):='en_US';
  c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('P_RPT_CP_MANAGER_OVERRIDE');
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1;   
BEGIN
MERGE INTO rpt_cp_manager_override rpt
USING (
SELECT pg.promotion_id,
vue.position_type,
           vue.department_type,
          fnc_locale_to_char_dt(promo.promotion_start_date, v_language_code)||' - '|| fnc_locale_to_char_dt(promo.promotion_end_date, v_language_code) as promo_start_end_date,
            au.first_name,
            au.middle_name,
            promo.promotion_name,
            au.user_id,
            au.last_name,            
            pa.status,
            SUM(j.transaction_amt) override_amount,
            un.node_id
 FROM 
journal J,
challengepoint_award pca,
application_user au,
vw_curr_pax_employer vue,
promo_challengepoint pg,
participant pa,
promotion promo,
user_node un
WHERE pca.award_type = 'manageroverride'
AND pca.journal_id = j.journal_id
AND pca.user_id = au.user_id
AND au.user_id = vue.user_id(+)
AND au.user_id = un.user_id
AND un.role = 'own'
AND un.is_primary = 1       --02/15/2016
AND pca.user_id = pa.user_id
AND j.promotion_id = pg.promotion_id
AND pg.promotion_id = promo.promotion_id
group by pg.promotion_id,
vue.position_type,
           vue.department_type,
          fnc_locale_to_char_dt(promo.promotion_start_date, v_language_code)||' - '|| fnc_locale_to_char_dt(promo.promotion_end_date, v_language_code) ,
            au.first_name,
            au.middle_name,
            promo.promotion_name,
            au.user_id,
            au.last_name,            
            pa.status,            
            un.node_id ) s
 ON (rpt.mgr_user_id = s.user_id AND rpt.promotion_id = s. promotion_id AND rpt.node_id = s.node_id)
        WHEN MATCHED THEN UPDATE 
         SET mgr_last_name     =   s.last_name,
              mgr_first_name    =   s.first_name,
              mgr_middle_init   =   s.middle_name,
              mgr_user_status   =   s.status,
              mgr_job_position  =   s.position_type,
              mgr_department    =   s.department_type,
              promo_start_end_date  =   s.promo_start_end_date,
              version           =  version+1,              
              promotion_name    =  s.promotion_name ,
              override_amount = s.override_amount,      
              date_modified         = SYSDATE,
              modified_by           = c_created_by              
              WHERE NOT (   DECODE(rpt.mgr_first_name,  s.first_name,                    1, 0) = 1
                 AND DECODE(rpt.mgr_middle_init, s.middle_name,                   1, 0) = 1
                 AND DECODE(rpt.mgr_last_name,   s.last_name,                     1, 0) = 1
                 AND DECODE(rpt.mgr_user_status,   s.status,                     1, 0) = 1
                 AND DECODE(rpt.mgr_job_position,   s.position_type,                     1, 0) = 1
                 AND DECODE(rpt.mgr_department,   s.department_type,                     1, 0) = 1                 
                 AND DECODE(rpt.promo_start_end_date,   s.promo_start_end_date,                     1, 0) = 1
                 AND DECODE(rpt.promotion_name,   s.promotion_name,                     1, 0) = 1
                 AND DECODE(rpt.override_amount,   s.override_amount,                     1, 0) = 1                    
                 )   
        WHEN NOT MATCHED THEN INSERT
(rpt_cp_mgr_override_id,
  mgr_user_id,
  mgr_first_name ,
  mgr_middle_init       ,
  mgr_last_name        ,
  mgr_user_status      ,
  mgr_job_position     ,
  mgr_department      ,
  promotion_id       ,
  promotion_name  ,
  promo_start_end_date, 
  override_amount ,
  node_id,
  created_by      ,
  date_created    ,
  version         )
  VALUES(rpt_cp_mgr_override_id.NEXTVAL,
  s.user_id,
  s.first_name ,
  s.middle_name       ,
  s.last_name        ,
  s.status      ,
  s.position_type     ,
  s.department_type      ,
  s.promotion_id       ,
  s.promotion_name  ,
  s.promo_start_end_date,
  s.override_amount ,
  s.node_id,
  c_created_by      ,
  SYSDATE    ,
  0
 );
   p_out_return_code   := 00;

EXCEPTION
  WHEN OTHERS THEN
     p_out_return_code   := 99;
     p_out_error_message := v_stage||' sqlerrm: '||sqlerrm;    
     prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', sqlerrm, NULL);    

END;

FUNCTION fnc_get_manager_points
  (p_in_node_id      IN VARCHAR2,
   p_in_promotion_id IN VARCHAR2,   
   p_in_record_type  IN VARCHAR2)

RETURN NUMBER IS

/*------------------------------------------------------------------
-- Purpose: Function used to retrieve manager points while summary
--          report calculation 
--
-- MODIFICATION HISTORY
-- Person      Date         Comments
-- ---------   ------       -----------------------------------------
-- Arun S      10/01/2012   Initial code.
-- Ravi Dhanekul 10/05/2012  Changed the function to accpt multiple promotion ids.
                           09/10/2013  Removed usage of Date parameters as we dont use them in any of GQ/CP reports.
                           10/1/2013  Added new report tables for CP mgr override.
--------------------------------------------------------------------*/

  v_points   NUMBER := 0;

BEGIN

  IF p_in_record_type = 'team' THEN

    SELECT NVL(SUM(rpt.override_amount), 0)
      INTO v_points
      FROM rpt_cp_manager_override rpt,
           promotion promo
     WHERE rpt.promotion_id   = promo.promotion_id
       AND rpt.node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id)))     
       AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))));
       
  ELSE
  
    SELECT NVL(SUM(rpt.override_amount), 0)
      INTO v_points
      FROM rpt_cp_manager_override rpt,
           promotion promo
     WHERE rpt.promotion_id   = promo.promotion_id
       AND rpt.node_id IN (SELECT child_node_id FROM  rpt_hierarchy_rollup WHERE node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id))))       
       AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))));

  END IF;
   
  RETURN v_points;

EXCEPTION
  WHEN OTHERS THEN
    RETURN v_points;
END;

END;
/
