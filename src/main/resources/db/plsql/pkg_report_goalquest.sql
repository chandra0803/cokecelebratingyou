CREATE OR REPLACE PACKAGE pkg_report_goalquest
  IS
  
  PROCEDURE P_RPT_GOALQUEST_DETAIL(p_in_requested_user_id      IN  NUMBER,
                                 p_in_start_date             IN  DATE,
                                 p_in_end_date               IN  DATE,
                                 p_out_return_code           OUT NUMBER,
                                 p_out_error_message         OUT VARCHAR2);

  PROCEDURE P_RPT_GOALQUEST_SUMMARY(p_in_requested_user_id      IN  NUMBER,
                                    p_out_return_code           OUT NUMBER,
                                    p_out_error_message         OUT VARCHAR2);
  
  PROCEDURE P_RPT_GOAL_PARTNER(p_in_requested_user_id      IN  NUMBER,
                               p_out_return_code           OUT NUMBER,
                               p_out_error_message         OUT VARCHAR2); 
  PROCEDURE P_RPT_GOAL_MANAGER_OVERRIDE (p_in_requested_user_id      IN  NUMBER,
                               p_out_return_code           OUT NUMBER,
                               p_out_error_message         OUT VARCHAR2); 

  FUNCTION F_GET_NBR_PAX_PER_LEVEL(
    p_in_node_id IN NUMBER,
    p_in_promotion_id IN NUMBER,
    p_in_level IN NUMBER )
    return number;
    
  FUNCTION F_GET_TOTAL_PAX(
    p_in_node_id IN NUMBER,
    p_in_promotion_id IN NUMBER,
    p_in_goal_selected IN VARCHAR2 )
    return number;

  FUNCTION fnc_get_manager_points
    (p_in_node_id      IN VARCHAR2,
     p_in_promotion_id IN VARCHAR2,    
     p_in_record_type  IN VARCHAR2)
     RETURN NUMBER;
     
    FUNCTION fnc_eligible_participants 
  (p_in_node_id     IN VARCHAR2,
   is_team IN number,
   p_in_promo_status  IN promotion.promotion_status%TYPE,
   p_in_promo_id    IN VARCHAR2,      
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN VARCHAR2  
  )
  RETURN NUMBER;
       
END; -- Package spec
/

CREATE OR REPLACE PACKAGE BODY  pkg_report_goalquest IS

FUNCTION fnc_eligible_participants 
  (p_in_node_id     IN VARCHAR2,
   is_team IN number,
   p_in_promo_status  IN promotion.promotion_status%TYPE,
   p_in_promo_id    IN VARCHAR2,      
   p_in_pax_status  IN participant.status%TYPE,
   p_in_pax_role    IN user_node.role%TYPE,
   p_in_pax_dept    IN VARCHAR2  
  )
  RETURN NUMBER IS
  /*-----------------------------------------------------------------------------
  Purpose: Retrieves count of eligible participants for the input node and its
           child nodes
  
  Modification history
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Ravi          09/11/2013  Initial creation
-----------------------------------------------------------------------------*/

  v_eli_pax NUMBER := 0;
BEGIN

SELECT COUNT(user_id) 
INTO v_eli_pax
FROM (
SELECT p2.user_id    
      FROM vw_curr_pax_employer vue,
           participant p2,
           user_node un
     WHERE 
       p2.user_id =vue.user_id(+)
       AND p2.status            = NVL (p_in_pax_status, p2.status)
       AND NVL(vue.position_type,'job')    = NVL (p_in_pax_role, NVL(vue.position_type,'job'))
       AND p2.user_id = un.user_id
       AND un.is_primary = 1
       AND (       
       NVL(is_Team,0)=0 AND un.NODE_ID IN (    SELECT node_id
                                         FROM NODE N2
                                   CONNECT BY PRIOR node_id = parent_node_id
                                   START WITH node_id  IN (SELECT * FROM TABLE(get_array_varchar(p_in_node_id))))
                             OR
                           (  is_Team=1 AND un.NODE_ID=p_in_node_id))
        AND ((p_in_pax_dept IS NULL) OR ( vue.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))))      
      UNION
    SELECT p2.user_id 
      FROM vw_curr_pax_employer vue,
           participant p2
     WHERE  p2.user_id = vue.user_id(+)
       AND p2.status            = NVL (p_in_pax_status, p2.status)
       AND NVL(vue.position_type,'job')    = NVL (p_in_pax_role, NVL(vue.position_type,'job'))
       AND ((p_in_pax_dept IS NULL) OR ( vue.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))))
       AND EXISTS (SELECT 'X'
                     FROM rpt_pax_promo_eligibility ppe,
                          promotion p
                    WHERE ppe.promotion_id     = p.promotion_id
                      AND ppe.participant_id   = p2.user_id
                      AND ppe.giver_recvr_type = 'receiver'
                       AND ((p_in_promo_id IS NULL ) OR ( p.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promo_id)))))
                      AND p.promotion_status   = NVL(p_in_promo_status, p.promotion_status)
                       AND (       
       NVL(is_Team,0)=0 AND ppe.NODE_ID IN (    SELECT node_id
                                         FROM NODE N2
                                   CONNECT BY PRIOR node_id = parent_node_id
                                   START WITH node_id  IN (SELECT * FROM TABLE(get_array_varchar(p_in_node_id))))
                             OR
                           (  is_Team=1 AND ppe.NODE_ID=p_in_node_id))
        AND ((p_in_pax_dept IS NULL) OR ( vue.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_pax_dept)))))     
                      ));


  RETURN v_eli_pax;

EXCEPTION
  WHEN OTHERS THEN
    RETURN v_eli_pax;
END fnc_eligible_participants;

PROCEDURE P_RPT_GOALQUEST_DETAIL(p_in_requested_user_id      IN  NUMBER,
                                 p_in_start_date             IN  DATE,
                                 p_in_end_date               IN  DATE,
                                 p_out_return_code           OUT NUMBER,
                                 p_out_error_message         OUT VARCHAR2)
IS
/*******************************************************************************
--
-- Purpose: Populate rpt_goal_selection_detail table.
--
-- MODIFICATION HISTORY
-- Person              Date         Comments
-- ---------           ----------   ------------------------------------------
-- J Sedey             02/20/2007  Initial code.
-- M Lindvig           12/11/2007  Added code to fix bug #18483
-- M Lindvig
   S. Majumder         03/27/2008  Added code for Goal Partner Report
   J Flees             06/20/2011  Bug 28893, Add promo date range search to reports.
-- Arun S              11/28/2011  Bug 38589, Added promotion_status = 'live' in cur_pax 
--                              So that Goal Quest report generate only for live promotions
-- nagarajs            03/05/2012  Bug 39774, Commented promotion status where condition to 
--                              generate Goal Quest report for live and expired promotions.
-- Chidamba            07/16/2012  G5 Report Changes to Incremental approach
-- Arun S              10/02/2012  changes to populate country_id instead of cm_asset_code   
--Ravi Dhanekula      04/18/2013  Added promotion type also to the where clause to exclude Challenpoint data.
                    08/12/2013  Re-wrote the process with a single merge statement.
                    10/05/2013     for defect # 4421.
                    01/13/2014  Fixed the defect # 50807
                    07/31/2014  Added new processing for the records that are not included in rpt_pax_promo_eligbility.
-- murphyc             09/11/2014  Bug # 56562. add outer join to goalquest_goallevel - gp.goallevel_id can be NULL
-- Swati            09/25/2014    Bug 56887 - GoalQuest progress reports are not removing the inactivated, or removing participants from the reports. 
                                They are remaining on the reports
--Suresh J          10/08/2014 Bug 57273 - Fixed the Formula for Both and Perofbase scenario
--Gorantla         01/22/2015  Bug 59268 Include promotion audience table to remove exluded data from promotions
--Suresh J         01/29/2015  Bug #57300 - Fixed the issue of Node Name Changes that are not reflected in reports        
--Suresh J         01/30/2015 Bug #59199 -- Added UPDATE statement for status field in rpt_goal_selection_detail when an user becomes inactive 
--Suresh J         02/04/2015 Bug 59431 - Fixed issue of pax selected goals are not reflected on Goal Selection Report        
--Ravi Dhanekula 03/12/2015 Bug # 60324 promo_goalquest achievement_precision not used consistently   
--Ravi Dhanekula 03/13/2015  Bug # 60405 Data on levels not getting displayed in the summary  . Changes for this bug fix are related to the bug fix of 59268.              
--RaviSankar     11/18/2015 Bug #  64699 P_RPT_GOALQUEST_DETAIL.percent_of_goal divide by zero error
--Sherif Basha   03/03/2017 Bug 68754 - GQ - Participant who was removed from GQ audience list is displayed on GQ Goal selection report 
                                       (a scenario where it was not deleted is when a user belong to 2 audience list commented the delete changes of bug56887)       
*******************************************************************************/
  c_created_by      CONSTANT rpt_goal_selection_detail.created_by%TYPE:= p_in_requested_user_id;
  v_stage           VARCHAR2(250);
  v_goal_level_id   INTEGER;
  v_commit_cnt      INTEGER;
  v_achieved        INTEGER;
  v_language_code   VARCHAR2(10):='en_US';
  c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('P_RPT_GOALQUEST_DETAIL');
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1;   
  v_tab_node_id        dbms_sql.number_table;   --01/29/2015  
  v_tab_node_name      dbms_sql.varchar2_table;  --01/29/2015  
  v_parm_list       execution_log.text_line%TYPE;   --03/11/2019  Bug 78631
  --Cursor to pick modified node name   
  CURSOR cur_node_changed IS          --01/29/2015
    SELECT node_id,
           NAME
      FROM node
     WHERE date_modified BETWEEN p_in_start_date AND p_in_end_date;  


BEGIN

v_stage := 'initialize variables'; 
v_parm_list := 
'p_in_requested_user_id :='||p_in_requested_user_id ||CHR(10)||
'p_in_start_date        :='||p_in_start_date        ||CHR(10)|| 
'p_in_end_date          :='||p_in_end_date          ; 	
	
    v_stage :='Delete Goalquest detail records.';  --09/25/2014 Bug 56887
  
--03/03/2017 Bug 68754 commented the delete below  
  /*  DELETE FROM rpt_goal_selection_detail a
    WHERE user_ID NOT IN (SELECT user_ID
                        FROM participant_audience);*/
 --03/03/2017 Bug 68754 added the delete below                        

       DELETE FROM rpt_goal_selection_detail a
        WHERE EXISTS(SELECT proa.promotion_id
                       FROM promo_audience proa,
                            participant_audience para
                      WHERE proa.audience_id = para.audience_id
                        AND proa.promotion_id = a.promotion_id)
          AND a.user_id NOT IN (SELECT para.user_id
                                  FROM promo_audience proa,
                                       participant_audience para
                                 WHERE proa.audience_id = para.audience_id
                                   AND proa.promotion_id = a.promotion_id);                 
                        
                        
v_stage :='Merge for Goalquest detail records.';
   MERGE INTO rpt_goal_selection_detail rpt
       USING (
       
       SELECT  --03/12/2015 New selected added Bug # 60324
user_id,
           promotion_id,
           node_id,
           node_name,          
           position_type,
           department_type,
           promo_start_end_date,
           first_name,
           middle_name,
           last_name,
           status,             
           country_id,
           pax_goal_id,
           promotion_name, 
           promotion_start_date, 
           promotion_end_date, 
           goal_level_name,
           level_id,
           trans_date,
           current_value,
           base_quantity,
           calculated_payout,
           CASE WHEN rounding_method = 'standard' THEN ROUND(amount_to_achieve,achievement_precision)            
                    WHEN rounding_method = 'up' AND achievement_precision = 0 THEN CEIL(amount_to_achieve)
                    WHEN rounding_method = 'up' AND achievement_precision <> 0 THEN ROUND(amount_to_achieve,achievement_precision)                     
                    WHEN rounding_method = 'down' THEN TRUNC(amount_to_achieve,achievement_precision)
                    ELSE amount_to_achieve END amount_to_achieve,
           CASE WHEN rounding_method = 'standard' AND ROUND(current_value,achievement_precision) >= ROUND(amount_to_achieve,achievement_precision) THEN 1             
                    WHEN rounding_method = 'up' AND achievement_precision = 0 AND CEIL(current_value)  >= CEIL(amount_to_achieve) THEN 1
                    WHEN rounding_method = 'up' AND achievement_precision <> 0 AND ROUND(current_value,achievement_precision) >= ROUND(amount_to_achieve,achievement_precision) THEN 1                              
                    WHEN rounding_method = 'down' AND TRUNC(current_value,achievement_precision) >= TRUNC(amount_to_achieve,achievement_precision) THEN 1
                    ELSE 0 END achieved,
             -- 11/17/2015 start
--            CASE WHEN rounding_method = 'standard' THEN ROUND(current_value,achievement_precision) / ROUND(amount_to_achieve,achievement_precision)*100             
--                    WHEN rounding_method = 'up' AND achievement_precision = 0 THEN CEIL(current_value)  / CEIL(amount_to_achieve)*100
--                    WHEN rounding_method = 'up' AND achievement_precision <> 0 THEN ROUND(current_value,achievement_precision) / ROUND(amount_to_achieve,achievement_precision)*100                              
--                    WHEN rounding_method = 'down' THEN TRUNC(current_value,achievement_precision) /TRUNC(amount_to_achieve,achievement_precision)*100
--                    ELSE 0 END percent_of_goal  
            CASE WHEN rounding_method = 'standard' THEN NVL((ROUND(current_value,achievement_precision) /
                               DECODE(ROUND(amount_to_achieve,achievement_precision),0,1,ROUND(amount_to_achieve,achievement_precision))*100 ),0)            
                    WHEN rounding_method = 'up' AND achievement_precision = 0 THEN NVL((CEIL(current_value)  / 
                               DECODE(CEIL(amount_to_achieve),0,1,CEIL(amount_to_achieve))*100),0)
                    WHEN rounding_method = 'up' AND achievement_precision <> 0 THEN NVL((ROUND(current_value,achievement_precision) / 
                               DECODE(ROUND(amount_to_achieve,achievement_precision),0,1,ROUND(amount_to_achieve,achievement_precision))*100 ),0)                             
                    WHEN rounding_method = 'down' THEN NVL((TRUNC(current_value,achievement_precision) /
                               DECODE(TRUNC(amount_to_achieve,achievement_precision),0,1,TRUNC(amount_to_achieve,achievement_precision))*100),0)
                    ELSE 0 END percent_of_goal
-- 11/17/2015 end
                    FROM ( 
SELECT au.user_id,
           b.promotion_id,
           un.node_id,
           n.name node_name,          
           e.position_type,
           e.department_type,
           fnc_locale_to_char_dt(p.promotion_start_date, v_language_code)||' - '|| fnc_locale_to_char_dt(promotion_end_date, v_language_code) as promo_start_end_date,
            au.first_name,
            au.middle_name,
            au.last_name,
            pa.status,             
            ua.country_id,
            gp.pax_goal_id,
            p.promotion_name, 
            p.promotion_start_date, 
            p.promotion_end_date, 
            gg.goal_level_cm_asset_code goal_level_name,
            gg.goallevel_id level_id, --10/05/2013
            NVL(gp.date_modified,gp.date_created) trans_date,
            gp.current_value current_value,
            gp.base_quantity,
            ba2.calculated_payout,
    CASE WHEN b.payout_structure = 'rate'  AND b.achievement_rule = 'fixed'   --01/13/2014
                              THEN gg.minimum_qualifier + gg.incremental_quantity                              
             WHEN b.payout_structure = 'rate' AND b.achievement_rule = 'perofbase'                               
                       THEN ((gg.minimum_qualifier/100)* gp.base_quantity + ((gg.incremental_quantity/100)*((gg.minimum_qualifier/100)* gp.base_quantity)))  
             WHEN b.payout_structure = 'fixed' AND b.achievement_rule = 'fixed'   
                       THEN (gg.achievement_amount+NVL(gp.base_quantity,0))  
             WHEN b.payout_structure = 'fixed' AND b.achievement_rule = 'perofbase'   
                       THEN ((gg.achievement_amount/100)* gp.base_quantity)
             WHEN b.payout_structure = 'both' AND b.achievement_rule = 'fixed'   
             THEN  (gg.achievement_amount+NVL(gp.base_quantity,0))   
             WHEN b.payout_structure = 'both' AND b.achievement_rule = 'perofbase'   
--             THEN (((gg.achievement_amount/100)* gp.base_quantity +(gg.minimum_qualifier/100)* gp.base_quantity + ((gg.incremental_quantity/100)*(gg.minimum_qualifier/100)* gp.base_quantity)))   --10/08/2014
               THEN ((gg.achievement_amount/100)* gp.base_quantity)  --10/08/2014                
             WHEN b.achievement_rule = 'baseFixed'
                THEN (gg.achievement_amount+NVL(gp.base_quantity,0))                               
                        ELSE gg.achievement_amount
                       END amount_to_achieve,     
--             CASE WHEN b.achievement_rule = 'fixed' 
--                              and gp.current_value >= gg.achievement_amount 
--                              THEN  1
--                            WHEN b.achievement_rule = 'perofbase'                               
--                              and gp.current_value >= ((gg.achievement_amount/100)* gp.base_quantity) THEN  1 
--                            WHEN b.achievement_rule = 'baseFixed' 
--                              and gp.current_value >= (gg.achievement_amount+NVL(gp.base_quantity,0)) THEN  1                               
--                        ELSE 0
--                       END achieved,              
--              CASE WHEN b.payout_structure = 'rate'  AND b.achievement_rule = 'fixed'  --03/12/2015
--                              AND gp.current_value >= gg.minimum_qualifier + gg.incremental_quantity 
--                              THEN  1
--                            WHEN b.payout_structure = 'rate' AND b.achievement_rule = 'perofbase'                               
--                              AND gp.current_value >= ((gg.minimum_qualifier/100)* gp.base_quantity + ((gg.incremental_quantity/100)*((gg.minimum_qualifier/100)* gp.base_quantity))  ) THEN  1 
--                        WHEN b.payout_structure = 'fixed' AND b.achievement_rule = 'fixed'   
--                        AND gp.current_value >= (gg.achievement_amount+NVL(gp.base_quantity,0)) THEN  1  
--                        WHEN b.payout_structure = 'fixed' AND b.achievement_rule = 'perofbase'   
--                        AND gp.current_value >= ((gg.achievement_amount/100)* gp.base_quantity) THEN  1   
--                        WHEN b.payout_structure = 'both' AND b.achievement_rule = 'fixed'   
--                        AND gp.current_value >= (gg.achievement_amount+NVL(gp.base_quantity,0)) THEN  1   
--                        WHEN b.payout_structure = 'both' AND b.achievement_rule = 'perofbase'   
----                        AND gp.current_value >= (((gg.achievement_amount/100)* gp.base_quantity +(gg.minimum_qualifier/100)* gp.base_quantity + ((gg.incremental_quantity/100)*(gg.minimum_qualifier/100)* gp.base_quantity))) THEN  1   --10/08/2014
--                          AND gp.current_value >= ((gg.achievement_amount/100)* gp.base_quantity) THEN  1  --10/08/2014                
--                            WHEN b.achievement_rule = 'baseFixed'
--                              AND gp.current_value >= (gg.achievement_amount+NVL(gp.base_quantity,0)) THEN  1                               
--                        ELSE 0
--                       END achieved,  
                       b.rounding_method, --03/12/2015
                       CASE WHEN b.achievement_precision ='zero' THEN 0--03/12/2015
                                WHEN b.achievement_precision ='one' THEN 1
                                WHEN b.achievement_precision ='two' THEN 2
                                WHEN b.achievement_precision ='three' THEN 3
                                WHEN b.achievement_precision ='four' THEN 4
                                ELSE 0 END  achievement_precision                        
--            CASE WHEN b.achievement_rule = 'fixed' 
--                               THEN  NVL(gp.current_value,0)/gg.achievement_amount*100
--                     WHEN b.achievement_rule = 'perofbase' 
--                              THEN NVL(gp.current_value,0)/ ((gg.achievement_amount/100)* gp.base_quantity)*100
--                            WHEN b.achievement_rule = 'baseFixed' 
--                              THEN ((gp.base_quantity/100)* gg.achievement_amount+ gg.achievement_amount)*100                               
--                        ELSE 0
--                       END percent_of_goal                     
    FROM   --rpt_pax_promo_eligibility r,
           goalquest_paxgoal gp,
           promo_goalquest b,
           vw_curr_pax_employer e,
           goalquest_goallevel gg,
           promotion p,
           user_node un,
           participant pa,
           application_user au,
           user_address ua,
           node n,           
             (SELECT SUM(transaction_amt)  calculated_payout, user_id,promotion_id                  
                        FROM journal where  journal_id in (select journal_id from  PAYOUT_CALCULATION_AUDIT WHERE REASON_TYPE LIKE '%goalquest_success%') group by user_id,promotion_id ) ba2                                  
   WHERE   
        p.promotion_type = 'goalquest'   
        AND promotion_status IN ('live','expired')  
--        AND r.promotion_id = p.promotion_id
--        AND r.participant_id = un.user_id   
        AND b.promotion_id = p.promotion_id
        AND p.promotion_id = gp.promotion_id
        AND  au.user_id = gp.user_id(+)
        AND un.is_primary = 1
        AND  ba2.user_id(+) = gp.user_id
        AND ba2.promotion_id(+) = gp.promotion_id        
        AND gp.goallevel_id = gg.goallevel_id(+)  -- 09/15/2014
        AND un.user_id = au.user_id
        AND au.user_id = e.user_id(+)        
        AND au.user_id = pa.user_id
        AND un.node_id = n.node_id
       -- AND r.node_id  = n.node_id       
        AND au.user_id = ua.user_id
        AND ua.is_primary = 1
        AND (p.primary_audience_type = 'allactivepaxaudience'       
        OR EXISTS (SELECT * FROM promo_audience proa,participant_audience para WHERE gp.promotion_id = proa.promotion_id    --01/22/2015  --02/04/2015 --03/13/2015
         AND proa.audience_id = para.audience_id AND para.user_id = pa.user_id)))
        UNION
           SELECT    
           au.user_id,
           p.promotion_id,
           un.node_id,
           n.name node_name,          
           e.position_type,
           e.department_type,
           fnc_locale_to_char_dt(p.promotion_start_date, v_language_code)||' - '|| fnc_locale_to_char_dt(promotion_end_date, v_language_code) as promo_start_end_date,
            au.first_name,
            au.middle_name,
            au.last_name,
            pa.status,             
            ua.country_id,
            NULL pax_goal_id,
            p.promotion_name, 
            p.promotion_start_date, 
            p.promotion_end_date, 
            NULL goal_level_name,
            NULL level_id,
            NULL trans_date,
            NULL current_value,
            NULL base_quantity,
            NULL calculated_payout,
            NULL amount_to_achieve,
            NULL achieved,  
            NULL AS percent_of_goal
                  FROM rpt_pax_promo_eligibility rpt,
                       promotion p,
                       user_node un,
                       participant pa,
                       application_user au,
                       user_address ua,
                       node n,
                       vw_curr_pax_employer e
            WHERE rpt.participant_id = au.user_id
              AND rpt.promotion_id   = p.promotion_id
              AND rpt.participant_id  = un.user_id
              AND rpt.node_id = un.node_id
              AND au.user_id = pa.user_id
              AND au.useR_id = ua.user_id(+)
              and au.user_id = e.user_id(+)
              AND un.is_primary =  1
              AND un.node_id = n.node_id
              AND ua.is_primary (+)= 1
              AND rpt.promotion_id IN(SELECT promotion_id 
                                    FROM promotion 
                                   WHERE promotion_type ='goalquest' and promotion_status = 'live')
              AND NOT EXISTS (select * from goalquest_paxgoal where promotion_id=rpt.promotion_id and user_id= rpt.participant_id) 
             UNION         --07/31/2014
              SELECT    
           au.user_id,
           p.promotion_id,
           un.node_id,
           n.name node_name,          
           e.position_type,
           e.department_type,
           fnc_locale_to_char_dt(p.promotion_start_date, 'en_US')||' - '|| fnc_locale_to_char_dt(promotion_end_date, 'en_US') as promo_start_end_date,
            au.first_name,
            au.middle_name,
            au.last_name,
            pa.status,             
            ua.country_id,
            NULL pax_goal_id,
            p.promotion_name, 
            p.promotion_start_date, 
            p.promotion_end_date, 
            NULL goal_level_name,
            NULL level_id,
            NULL trans_date,
            NULL current_value,
            NULL base_quantity,
            NULL calculated_payout,
            NULL amount_to_achieve,
            NULL achieved,  
            NULL AS percent_of_goal
                  FROM promotion p,
                       user_node un,
                       participant pa,
                       application_user au,
                       user_address ua,
                       node n,
                       vw_curr_pax_employer e
            WHERE 
               p.promotion_type ='goalquest' 
               AND promotion_status = 'live' 
               AND primary_audience_type = 'allactivepaxaudience'     
              AND au.user_id = pa.user_id
              AND pa.user_id = un.user_id
              AND pa.status = 'active'
              AND au.user_id = ua.user_id(+)
              and au.user_id = e.user_id(+)
              AND un.is_primary =  1
              AND un.node_id = n.node_id
              AND ua.is_primary (+)= 1              
              AND NOT EXISTS (select * from goalquest_paxgoal where promotion_id=p.promotion_id and user_id= au.user_id)
              ) s
          ON (rpt.user_id = s.user_id AND rpt.promotion_id = s. promotion_id)
        WHEN MATCHED THEN UPDATE 
         SET last_name     =   s.last_name,
              first_name    =   s.first_name,
              middle_init   =   s.middle_name,
              user_status   =   s.status,
              job_position  =   s.position_type,
              department    =   s.department_type,
              node_id       =   s.node_id,
              node_name       =   s.node_name,
              promo_start_end_date  =   s.promo_start_end_date,
              percent_of_goal             =  s.percent_of_goal,         
              pax_goal_id       =  s.pax_goal_id,
              level_id = s.level_id,
              version           =  version+1,
              achieved          =  s.achieved,
              current_value = s.current_value,
              base_quantity   = s.base_quantity,
              amount_to_achieve = s.amount_to_achieve,
              promotion_name    =  s.promotion_name ,
              promotion_start_date  =   s.promotion_start_date ,
              promotion_end_date    =   s.promotion_end_date,
              goal_level_name       =   s.goal_level_name,              
              country_id            = s.country_id,
              calculated_payout = s.calculated_payout,
              date_modified         = SYSDATE,
              modified_by           = c_created_by,
              trans_date            = s.trans_date
              WHERE NOT (   DECODE(rpt.first_name,  s.first_name,                    1, 0) = 1
                 AND DECODE(rpt.middle_init, s.middle_name,                   1, 0) = 1
                 AND DECODE(rpt.last_name,   s.last_name,                     1, 0) = 1
                 AND DECODE(rpt.user_status,   s.status,                     1, 0) = 1
                 AND DECODE(rpt.job_position,   s.position_type,                     1, 0) = 1
                 AND DECODE(rpt.department,   s.department_type,                     1, 0) = 1
                 AND DECODE(rpt.node_id,   s.node_id,                     1, 0) = 1
                 AND DECODE(rpt.promo_start_end_date,   s.promo_start_end_date,                     1, 0) = 1
                 AND DECODE(rpt.pax_goal_id,   s.pax_goal_id,                     1, 0) = 1
                 AND DECODE(rpt.current_value,   s.current_value,                     1, 0) = 1
                 AND DECODE(rpt.base_quantity,   s.base_quantity,                     1, 0) = 1                 
                 AND DECODE(rpt.amount_to_achieve,   s.amount_to_achieve,                     1, 0) = 1
                 AND DECODE(rpt.achieved,   s.achieved,                     1, 0) = 1                 
                 AND DECODE(rpt.level_id,   s.level_id,                     1, 0) = 1
                 AND DECODE(rpt.promotion_name,   s.promotion_name,                     1, 0) = 1
                 AND DECODE(rpt.goal_level_name,   s.goal_level_name,                     1, 0) = 1                 
                 AND DECODE(rpt.calculated_payout,   s.calculated_payout,                     1, 0) = 1
                 AND DECODE(rpt.country_id,   s.country_id,                     1, 0) = 1                 
                 )   
        WHEN NOT MATCHED THEN INSERT
                (rpt_goal_selection_detail_id,
                                            user_id,
                                            last_name,
                                            first_name,
                                            middle_init,
                                            user_status,
                                            job_position,
                                            department,
                                            node_id,
                                            node_name,
                                            promotion_id,
                                            promo_start_end_date,
                                            pax_goal_id,
                                            level_id,
                                            percent_of_goal,
                                            calculated_payout,
                                            date_created,
                                            created_by,
                                            version,
                                            achieved,
                                            current_value,
                                            base_quantity,
                                            amount_to_achieve,
                                            promotion_name,
                                            promotion_start_date,
                                            promotion_end_date,
                                            goal_level_name,                                           
                                            country_id,
                                            trans_date)
         VALUES(rpt_goal_selection_detail_sq.nextval,
                                            s.user_id,
                                            s.last_name,
                                            s.first_name,
                                            s.middle_name,
                                            nvl(s.status,' '),
                                            nvl(s.position_type,' '),
                                            nvl(s.department_type,' '),
                                            s.node_id,
                                            s.node_name,
                                            s.promotion_id,
                                            s.promo_start_end_date,
                                            s.pax_goal_id,
                                            s.level_id,
                                            s.percent_of_goal, 
                                            s.calculated_payout,                                          
                                            SYSDATE,
                                            c_created_by,
                                            0,
                                            s.achieved,
                                            s.current_value,
                                            s.base_quantity,
                                            s.amount_to_achieve,
                                            s.promotion_name,
                                            s.promotion_start_date,
                                            s.promotion_end_date,
                                            s.goal_level_name,                                            
                                            s.country_id,               
                                            s.trans_date
                                            );

    update rpt_goal_selection_detail d       --01/30/2015 
    set user_status = 'inactive'
    where d.user_status = 'active' and
    exists (SELECT au.user_id FROM application_user au where au.is_active = 0 and au.user_id = d.user_id);


  v_stage := 'Open and Fetch cursor to pick modified node name';      --01/29/2015
  OPEN cur_node_changed;   --01/29/2015
  FETCH cur_node_changed BULK COLLECT
   INTO v_tab_node_id,
        v_tab_node_name;
  CLOSE cur_node_changed;   

  v_stage := 'Update modified node name in rpt table';    --01/29/2015
  FORALL indx IN v_tab_node_id.FIRST .. v_tab_node_id.LAST    --01/29/2015
    UPDATE rpt_goal_selection_detail
       SET node_name     = DECODE (node_id, v_tab_node_id(indx), v_tab_node_name(indx), node_name),
           date_modified = SYSDATE,
           modified_by   = p_in_requested_user_id,
           VERSION       = VERSION + 1
     WHERE (node_id        = v_tab_node_id(indx)
            AND node_name != v_tab_node_name(indx)
            );
        
   p_out_return_code   := 00;

EXCEPTION
  WHEN OTHERS THEN
     p_out_return_code   := 99;
     p_out_error_message := v_stage||' sqlerrm: '||sqlerrm;    
     prc_execution_log_entry(c_process_name, c_release_level, 'ERROR',v_parm_list||':'|| sqlerrm, NULL);          
END;

PROCEDURE P_RPT_GOALQUEST_SUMMARY(p_in_requested_user_id      IN  NUMBER,
                                  p_out_return_code           OUT NUMBER,
                                  p_out_error_message         OUT VARCHAR2)
IS
--
--
-- Purpose: Populate rpt_goalquest_summary table.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Ernste      03/06/2007   Initial code.
-- J Flees     06/20/2011   Bug 28893, Add promo date range search to reports.
-- Chidamba    07/19/2012   Commented old code for G5 Report Changes to Incremental approach
--Ravi Dhanekula 04/07/2014  Removed full outer join as a fix for the bug # 52694.
--                        06/18/2014  Added performance fix for default team summary.
--                        08/05/2014 Changed join to full outer join to fix the data issue.  
--Suresh J                01/07/2015 Bug Fix 58707 Percent_of_goal range issue. Updated range as suggested by Ravi  
--Sherif Basha   03/03/2017 Bug 68754 - GQ - Participant who was removed from GQ audience list is displayed on GQ Goal selection report 
--                                       (a scenario where it was not deleted is when a user belong to 2 audience list commented the delete changes of bug56887)  
-- Ravi Dhanekula 01/03/2018 G6-3735 Fixed issue found during report refresh process.
---------------------------------------------------------------------------
  c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('P_RPT_GOALQUEST_SUMMARY');
  c_release_level      CONSTANT execution_log.release_level%TYPE := 2;   
  c_created_by         CONSTANT rpt_enrollment_detail.created_by%TYPE:= p_in_requested_user_id;
  v_stage              execution_log.text_line%TYPE;  
  v_commit_cnt         INTEGER;
  v_rec_cnt            INTEGER;  
  
BEGIN

  v_stage := 'DELETE obsolete node summary records';
  DELETE
    FROM rpt_goal_selection_summary s
   WHERE s.detail_node_id NOT IN
          ( -- get node ID currently in the report hierarchy
            SELECT h.node_id
              FROM rpt_hierarchy h
          );
          


  v_rec_cnt := SQL%ROWCOUNT;
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  
  -- merge derived summary records
  v_stage := 'MERGE detail derived summary records';  
  MERGE INTO rpt_goal_selection_summary d
  USING (    
  WITH 
          rpt_teamsum AS
        (  -- build team summary records          
          SELECT h.hier_level
                ,h.parent_node_id  header_node_id     
                ,NVL(d.node_id,h.node_id) detail_node_id
                ,NVL(d.user_status,' ')   pax_status
                ,NVL(d.job_position,' ')  job_position
                ,NVL(d.department,' ')    department                                                
                ,d.promotion_id
                ,d.promo_start_end_date       -- Bug 28893
                ,g.goallevel_id   goal_level   
                ,COUNT(d.user_id) total_participants
                ,COUNT(d.level_id)             AS total_selected
                ,SUM(achieved) nbr_goal_achieved   
                ,SUM(d.calculated_payout) calculated_payout
                ,d.manager    
                 ,SUM(CASE WHEN CASE WHEN goallevel_id IS NOT NULL THEN NVL(d.percent_of_goal,0) --08/01/2014
                      ELSE 
--                     d.percent_of_goal END BETWEEN 0 AND 25   --01/07/2015
                       d.percent_of_goal END >=0 AND d.percent_of_goal<25.50  --01/07/2015
                    THEN 1 END) nbr_between_0_25 
                ,SUM(CASE WHEN 
--                d.percent_of_goal BETWEEN 26 AND 50 --01/07/2015
                d.percent_of_goal >=25.50 AND d.percent_of_goal <50.50   --01/07/2015 
                THEN 1 END) nbr_between_26_50
                ,SUM(CASE WHEN 
--                d.percent_of_goal BETWEEN 51 AND 75   --01/07/2015
                  d.percent_of_goal >=50.50 AND d.percent_of_goal <75.50  --01/07/2015 
                    then 1 end) nbr_between_51_75
                ,SUM(CASE WHEN 
--                d.percent_of_goal BETWEEN 76 AND 99    --01/07/2015
                  d.percent_of_goal >=75.50 AND d.percent_of_goal <100    --01/07/2015 
                    THEN 1 END) nbr_between_76_99
                ,SUM(CASE WHEN 
--                d.percent_of_goal > 99   --01/07/2015
                  d.percent_of_goal >=100    --01/07/2015
                    THEN 1 END) nbr_over_100 
              ,sum(NVL(d.base_quantity,0)) base
                   ,sum(NVL(d.amount_to_achieve,0)) goal
                   ,sum(NVL(d.current_value,0)) actual_result  
           FROM rpt_goal_selection_detail d
               ,rpt_hierarchy h
               ,goalquest_paxgoal g
          WHERE h.node_id = d.node_id
            and d.pax_goal_id = g.pax_goal_id (+)
          GROUP BY h.parent_node_id ,
                   nvl(d.node_id,h.node_id)
                   , NVL(d.user_status,' ')--1/3/2018 Start
                   ,NVL(d.job_position,' ')
                   ,NVL(d.department,' ')--1/3/2018 End
                   , manager
                   , d.promotion_id
                   , d.promo_start_end_date       -- Bug 28893
                   , g.goallevel_id
                  , h.hier_level                  
        ), 
        detail_derived_summary AS            
        (  -- derive summaries based on team summary data
           SELECT -- key fields
                  rt.detail_node_id,
                  'teamsum' AS sum_type,
                  rt.promotion_id,
                  rt.pax_status,
                  rt.job_position,
                  rt.department,
                  rt.promo_start_end_date,
                  -- reference fields
                  rt.goal_level,
                  rt.header_node_id,
                  rt.hier_level,
                  1 AS is_leaf, -- The team summary records are always a leaf
                  -- count fields
                  rt.total_participants,
                  rt.total_selected,
                  rt.nbr_goal_achieved,
                  rt.calculated_payout,
                  rt.manager,
                  rt.nbr_between_0_25,
                  rt.nbr_between_26_50,
                  rt.nbr_between_51_75,
                  rt.nbr_between_76_99,
                  rt.nbr_over_100,
                  rt.base,
                  rt.goal,
                  rt.actual_result
             FROM rpt_teamsum rt
            UNION ALL
           SELECT -- key fields
                  h.node_id AS detail_node_id,
                  'nodesum' AS sum_type,
                  rt.promotion_id,
                  rt.pax_status,
                  rt.job_position,
                  rt.department,
                  rt.promo_start_end_date,
                  -- reference fields
                  rt.goal_level,
                  h.parent_node_id AS header_node_id,
                  h.hier_level,
                  h.is_leaf,
                  -- count fields
                  SUM(rt.total_participants) total_participants,
                  SUM(rt.total_selected) AS total_selected,
                  SUM(rt.nbr_goal_achieved) no_goal_achieved,
                  SUM(rt.calculated_payout) calculated_payout,
                  rt.manager,
                  SUM(rt.nbr_between_0_25) nbr_between_0_25,
                  SUM(rt.nbr_between_26_50) nbr_between_26_50,
                  SUM(rt.nbr_between_51_75) nbr_between_51_75,
                  SUM(rt.nbr_between_76_99) nbr_between_76_99,
                  SUM(rt.nbr_over_100) nbr_over_100,
                  SUM(rt.base) AS base,
                  SUM(rt.goal) AS goal,
                  SUM(rt.actual_result) AS actual_result
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
            WHERE rt.total_participants != 0      -- create node summary for team summaries with non-zero media amounts                  
               -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
              AND rt.detail_node_id = npn.node_id
              AND npn.path_node_id = h.node_id
            GROUP BY h.node_id,
                  rt.promotion_id,
                  rt.pax_status,
                  rt.job_position,
                  rt.department,
                  rt.promo_start_end_date,
                  rt.goal_level,
                  rt.manager,
                  h.parent_node_id,
                  h.hier_level,
                  h.is_leaf
        ) -- end detail_derived_summary
--       select * from detail_derived_summary  
--      
        -- compare existing summary records with detail derived summaries
        SELECT es.s_rowid,
               dds.hier_level || '-' || dds.sum_type AS record_type,
               dds.*
          FROM (  -- get existing summaries based on derived details
                 SELECT s2.ROWID AS s_rowid,
                        s2.detail_node_id,
                        S2.header_node_id,
                        SUBSTR(s2.record_type, -7) AS sum_type,
                        s2.promotion_id,
                        s2.pax_status,
                        s2.job_position,
                        s2.department,
                        s2.promo_start_end_date,
                        s2.goal_level,
                        s2.total_selected,                        
                        s2.nbr_goal_achieved,
                        s2.calculated_payout,
                        s2.nbr_pax_25_percent_of_goal  nbr_pax_25_percent_of_goal,
                        s2.nbr_pax_50_percent_of_goal  nbr_pax_50_percent_of_goal,
                        s2.nbr_pax_75_percent_of_goal  nbr_pax_75_percent_of_goal,
                        s2.nbr_pax_76_99_percent_of_goal nbr_pax_76_99_percent_of_goal,
                        s2.nbr_pax_100_percent_of_goal   nbr_pax_100_percent_of_goal
                   FROM rpt_goal_selection_summary s2
                  WHERE s2.promo_start_end_date IS NOT NULL
               ) es
               -- full outer join so unmatched existing summaries can be deleted
               FULL OUTER JOIN detail_derived_summary dds --04/07/2014  Bug # 52694 --08/05/2014
               ON (   es.detail_node_id=dds.detail_node_id
                  AND es.sum_type       = dds.sum_type
                  AND es.promotion_id   = dds.promotion_id
                  AND es.pax_status     = dds.pax_status
                  AND es.job_position   = dds.job_position
                  AND es.department     = dds.department
                  AND es.promo_start_end_date   = dds.promo_start_end_date
                  AND NVL(es.goal_level,0)      = NVL(dds.goal_level,0)                  
                  ) 
         ) s
      ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
      UPDATE SET
         d.manager            = s.manager,
         d.header_node_id     = s.header_node_id,
         d.goal_level         = s.goal_level,
         d.is_leaf            = s.is_leaf,
         d.total_participants = s.total_participants,
         d.total_selected = s.total_selected,
         d.nbr_goal_achieved  = s.nbr_goal_achieved,
         d.calculated_payout  = s.calculated_payout,
         d.nbr_pax_25_percent_of_goal   = s.nbr_between_0_25,
         d.nbr_pax_50_percent_of_goal  = s.nbr_between_26_50,
         d.nbr_pax_75_percent_of_goal  = s.nbr_between_51_75,
         d.nbr_pax_76_99_percent_of_goal  = s.nbr_between_76_99,
         d.nbr_pax_100_percent_of_goal       = s.nbr_over_100,
         d.base_quantity = s.base, 
         d.goal = s.goal,           
         d.actual_result = s.actual_result,       
         d.modified_by        = c_created_by,
         d.date_modified      = SYSDATE
   WHERE ( -- only update records with different values
            DECODE(d.manager,            s.manager,          1, 0) = 0
         OR DECODE(d.header_node_id,     s.header_node_id,   1, 0) = 0
         OR DECODE(d.goal_level,         s.goal_level,     1, 0) = 0
         OR DECODE(d.is_leaf,            s.is_leaf,          1, 0) = 0
--         OR DECODE(d.hier_level,         s.hier_level,         1, 0) = 0
         OR DECODE(d.total_participants, s.total_participants, 1, 0) = 0
         OR DECODE(d.total_selected,  s.total_selected,  1, 0) = 0
         OR DECODE(d.nbr_goal_achieved,  s.nbr_goal_achieved,  1, 0) = 0
         OR DECODE(d.calculated_payout,  s.calculated_payout,  1, 0) = 0
         OR DECODE(d.nbr_pax_25_percent_of_goal,   s.nbr_between_0_25,   1, 0) = 0
         OR DECODE(d.nbr_pax_50_percent_of_goal,  s.nbr_between_26_50,  1, 0) = 0
         OR DECODE(d.nbr_pax_75_percent_of_goal,  s.nbr_between_51_75,  1, 0) = 0
         OR DECODE(d.nbr_pax_76_99_percent_of_goal,  s.nbr_between_76_99,  1, 0) = 0
         OR DECODE(d.nbr_pax_100_percent_of_goal,       s.nbr_over_100,       1, 0) = 0
         OR DECODE(d.base_quantity,       s.base,       1, 0) = 0
         OR DECODE(d.goal,       s.goal,       1, 0) = 0
         OR DECODE(d.actual_result,       s.actual_result,       1, 0) = 0)
 -- remove existing summaries that no longer have product details
--      DELETE
--       WHERE s.promotion_id IS NULL
    WHEN NOT MATCHED THEN
      INSERT
       (rpt_goal_selection_summary_id,
        header_node_id,
        detail_node_id,
        total_participants,
        total_selected,        
        goal_level,
        promotion_id,
        promo_start_end_date,
        pax_status,
        job_position,
        department,
        hier_level,
        is_leaf,
        nbr_goal_achieved,
        calculated_payout,
        record_type,
        nbr_pax_25_percent_of_goal,
        nbr_pax_50_percent_of_goal,
        nbr_pax_75_percent_of_goal,
        nbr_pax_76_99_percent_of_goal,
        nbr_pax_100_percent_of_goal,
        base_quantity,
        goal,
        actual_result, 
        manager,
        date_created,
        created_by)
      VALUES
        (rpt_goal_selection_summary_sq.NEXTVAL,
            s.header_node_id,
            s.detail_node_id,
            s.total_participants,
            s.total_selected, 
            s.goal_level,
            s.promotion_id,
            s.promo_start_end_date,
            s.pax_status,
            s.job_position,
            s.department,
            s.hier_level,
            s.is_leaf,
            s.nbr_goal_achieved,
            s.calculated_payout,
            s.record_type,
            s.nbr_between_0_25,
            s.nbr_between_26_50,
            s.nbr_between_51_75,
            s.nbr_between_76_99,
            s.nbr_over_100,
            s.base,
            s.goal,
            s.actual_result,
            s.manager,
            SYSDATE,
            c_created_by);  

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
 
--  -- add missing default summary permutations
--  v_stage := 'INSERT missing default team summary permutations';  
--  INSERT INTO rpt_goal_selection_summary
--      (rpt_goal_selection_summary_id,
--       -- key fields
--       detail_node_id,
--       record_type,
--       promotion_id,
--       pax_status,
--       job_position,
--       department,
--       promo_start_end_date,  -- Bug 28893
--       -- reference fiel
--       goal_level,
--       manager,
--       header_node_id,       
--       hier_level,
--       is_leaf,       
--       -- cal fields
--       total_participants,
--       total_selected,
--       nbr_goal_achieved,
--       calculated_payout,       
--       nbr_pax_25_percent_of_goal,
--       nbr_pax_50_percent_of_goal,
--       nbr_pax_75_percent_of_goal,
--       nbr_pax_76_99_percent_of_goal,
--       nbr_pax_100_percent_of_goal,
--       base_quantity,
--        goal,
--        actual_result,
--       -- audit fields
--       date_created,
--       created_by)
--     (SELECT rpt_goal_selection_summary_sq.NEXTVAL,
--             -- key fields
--             nsp.detail_node_id,
--             nsp.record_type,
--             0    AS promotion_id,
--             ' '  AS pax_status,
--             ' '  AS job_position,
--             ' '  AS department,
--             NULL AS promo_start_end_date,
--             -- reference fiel
--             NULL  AS goal_level,
--             NULL  AS manager,
--             nsp.header_node_id,
--             nsp.hier_level,
--             nsp.is_leaf,
--             -- cal fields
--             0 AS total_participants,
--             0 AS total_selected,
--             NULL AS nbr_goal_achieved,
--             NULL AS calculated_payout,
--             NULL AS nbr_between_0_25,
--             NULL AS nbr_between_26_50,
--             NULL AS nbr_between_51_75,
--             NULL AS nbr_between_76_99,
--             NULL AS nbr_over_100,
--             NULL AS base,
--             NULL AS goal,
--             NULL AS actual_result,
--             -- audit fields             
--             SYSDATE      AS date_created,
--             c_created_by AS created_by            
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
--                 FROM rpt_goal_selection_summary s
--                WHERE s.detail_node_id = nsp.detail_node_id
--                  AND s.record_type    = nsp.record_type
--                   -- any media date value matches on team summary records because detail derived summaries replace default team permutations
--             )  
--   );
--   
--   v_rec_cnt := SQL%ROWCOUNT;
--   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
--
--   v_stage := 'INSERT missing default node summary permutations';   
--   INSERT INTO rpt_goal_selection_summary 
--          (rpt_goal_selection_summary_id,
--           detail_node_id,
--           record_type,
--           promotion_id,
--           pax_status,
--           job_position,
--           department,
--           promo_start_end_date,  -- Bug 28893
--           goal_level,
--           manager,
--           header_node_id,       
--           hier_level,
--           is_leaf,       
--           total_participants,
--           total_selected,
--           nbr_goal_achieved,
--           calculated_payout,       
--           nbr_pax_25_percent_of_goal,
--           nbr_pax_50_percent_of_goal,
--           nbr_pax_75_percent_of_goal,
--           nbr_pax_76_99_percent_of_goal,
--           nbr_pax_100_percent_of_goal,
--           base_quantity,
--           goal,
--           actual_result, 
--           date_created,
--           created_by)
--         (SELECT rpt_goal_selection_summary_sq.NEXTVAL,
--                 -- key fields
--                 nsp.detail_node_id,
--                 nsp.record_type,
--                 0    AS promotion_id,
--                 ' '  AS pax_status,
--                 ' '  AS job_position,
--                 ' '  AS department,
--                 NULL AS promo_start_end_date,
--                 -- reference fiel
--                 NULL  AS goal_level,
--                 NULL  AS manager,
--                 nsp.header_node_id,
--                 nsp.hier_level,
--                 nsp.is_leaf,
--                 -- count fields
--                 0 AS total_participants,
--                 0 AS total_selected,
--                 NULL AS nbr_goal_achieved,
--                 NULL AS calculated_payout,
--                 NULL AS nbr_between_0_25,
--                 NULL AS nbr_between_26_50,
--                 NULL AS nbr_between_51_75,
--                 NULL AS nbr_between_76_99,
--                 NULL AS nbr_over_100,
--                 NULL AS base,
--                 NULL AS goal,
--                 NULL AS actual_result,
--                 -- audit fields
--                 SYSDATE      AS date_created,
--                 c_created_by AS created_by           
--            FROM ( -- get all possible node summary type permutations
--                   SELECT h.node_id AS detail_node_id,
--                          h.hier_level || '-' || 'nodesum' AS record_type,
--                          h.parent_node_id AS header_node_id,
--                          h.hier_level,
--                          h.is_leaf
--                     FROM rpt_hierarchy h
--                 ) nsp
--              -- exclude default permutation when a matching summary record exists
--            WHERE NOT EXISTS
--                 ( SELECT 1
--                     FROM rpt_goal_selection_summary s
--                    WHERE s.detail_node_id = nsp.detail_node_id
--                      AND s.record_type    = nsp.record_type
--                      AND s.promo_start_end_date IS NULL
--                 )
--              -- default node summary permutation must have default team summary permutation in its hierarchy
--             AND nsp.detail_node_id IN --EXISTS --06/18/2014
--                 ( -- get team permutations under node permutation
--                   SELECT tp.detail_node_id
--                     FROM rpt_goal_selection_summary tp
--                    WHERE --tp.detail_node_id          = nsp.detail_node_id
--                      SUBSTR(tp.record_type, -7) = 'teamsum'
--                      AND tp.promo_start_end_date IS NULL
--                    UNION ALL
--                   -- get team permutations under node permutation hierarchy
--                   SELECT tp.header_node_id
--                     FROM rpt_goal_selection_summary tp
--                       -- start with child node immediately under current node
--                    START WITH SUBSTR(tp.record_type, -7) = 'teamsum'
--                           AND tp.promo_start_end_date IS NULL
--                    CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
--                           AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
--                           AND tp.promo_start_end_date IS NULL )
--            );
--             
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   
    --03/03/2017 Bug 68754 existing records become null when a pax is removed from audience  and creates inconsistency in subsequent runs
   
   v_stage := 'DELETE invalid records of nodes which has null total participants and level and header node and is_leaf created due to removal of pax';   
    DELETE
      FROM rpt_goal_selection_summary 
     WHERE total_participants IS NULL AND goal_level IS NULL AND is_leaf IS NULL;
     
     v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
   
   ------------
   -- remove team permutations when an associated detail derived summary exists
   v_stage := 'DELETE obsolete team permutations';
   DELETE rpt_goal_selection_summary
    WHERE ROWID IN
    (SELECT DISTINCT
           s.ROWID
      FROM rpt_goal_selection_summary s,
           rpt_goal_selection_summary dd
        -- substr matches functional index
     WHERE SUBSTR(dd.record_type, -7) = 'teamsum'
        -- detail derived summaries have a media date
       AND dd.promo_start_end_date IS NOT NULL
       AND dd.detail_node_id          = s.detail_node_id
       AND SUBSTR(dd.record_type, -7) = SUBSTR(s.record_type, -7)
        -- default permutations have no media date
       AND s.promo_start_end_date IS NULL);
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- delete node permutation with no associated team permutation
   v_stage := 'DELETE obsolete node permutations with no associated team permutation';
   DELETE rpt_goal_selection_summary
    WHERE ROWID IN
   (SELECT np.ROWID
      FROM rpt_goal_selection_summary np
     WHERE SUBSTR(np.record_type, -7) = 'nodesum'
       AND np.promo_start_end_date IS NULL
        -- node permutation has no team permutation
       AND NOT EXISTS
           ( -- get team permutations under node permutation
             SELECT 1
               FROM rpt_goal_selection_summary tp
              WHERE tp.detail_node_id          = np.detail_node_id
                AND SUBSTR(tp.record_type, -7) = 'teamsum'
                AND tp.promo_start_end_date IS NULL
              UNION ALL
             -- get team permutations under node permutation hierarchy
             SELECT 1
               FROM rpt_goal_selection_summary tp
                 -- start with child node immediately under current node
              START WITH tp.header_node_id          = np.detail_node_id
                     AND SUBSTR(tp.record_type, -7) = 'teamsum'
                     AND tp.promo_start_end_date IS NULL
              CONNECT BY PRIOR tp.detail_node_id          = tp.header_node_id
                     AND PRIOR SUBSTR(tp.record_type, -7) = SUBSTR(tp.record_type, -7)
                     AND tp.promo_start_end_date IS NULL)
    ); 
 
 p_out_return_code   := 00;  
   
EXCEPTION
  WHEN OTHERS THEN
  
  p_out_return_code   := 99;
  prc_execution_log_entry(c_process_name,
                          c_release_level,
                          'ERROR',
                          'Param:'||p_in_requested_user_id||':'||v_stage||SQLERRM,
                          NULL);
  p_out_error_message := v_stage||SQLERRM;                           

END P_RPT_GOALQUEST_SUMMARY;

/*07/18/2012 --Commented to impleted incremental approach for G5
 PROCEDURE P_RPT_GOALQUEST_SUMMARY
IS
--
--
-- Purpose: Populate rpt_enrollment_summary table.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Ernste      03/06/2007 Initial code.
-- J Flees     06/20/2011 Bug 28893, Add promo date range search to reports.

  c_created_by  CONSTANT rpt_enrollment_detail.created_by%TYPE:=0;
  v_stage       VARCHAR2(250);
  v_commit_cnt  INTEGER;

   CURSOR cur_level is
     SELECT DISTINCT hier_level
       FROM rpt_hierarchy
      ORDER BY hier_level desc;

   CURSOR cur_hier (p_hier_level NUMBER) IS
     SELECT parent_node_id, node_id, is_leaf
       FROM rpt_hierarchy
      WHERE hier_level = p_hier_level
      ORDER BY parent_node_id, node_id;

BEGIN


    DELETE  rpt_goal_selection_summary;

   MERGE INTO rpt_refresh_date B
    USING (
            SELECT 'goalselection' as report_name, 'summary' as report_type, sysdate as refresh_date
            FROM dual ) E
      ON (B.report_name = e.report_name and b.report_type = e.report_type)
    WHEN MATCHED THEN
        UPDATE SET B.refresh_date = e.refresh_date
    WHEN NOT MATCHED THEN
        INSERT (B.report_name, b.report_type, b.refresh_date)
        VALUES (E.report_name, E.report_type, e.refresh_date);
        
        
  FOR rec_level IN cur_level LOOP

    --insert teamsum records
    INSERT INTO rpt_goal_selection_summary
           (record_type,
           header_node_id,
           detail_node_id,
           pax_status,
           job_position,
           department,
           hier_level,
           is_leaf,
           date_created,
           created_by,
           promotion_id,
           promo_start_end_date,          -- Bug 28893
           goal_level,
           total_participants,
           nbr_goal_achieved,
           calculated_payout,
           manager,
           nbr_pax_25_percent_of_goal,
           nbr_pax_50_percent_of_goal,
           nbr_pax_75_percent_of_goal,
           nbr_pax_76_99_percent_of_goal,
           nbr_pax_100_percent_of_goal)
    (SELECT h.hier_level||'-teamsum'
            ,h.parent_node_id
            ,nvl(d.node_id,h.node_id)
            ,nvl(d.user_status,' ') user_status
            ,NVL(d.job_position,' ') job_position
            ,NVL(d.department,' ') department
            ,h.hier_level
            ,1  -- The team summary records are always a leaf
            ,sysdate
            ,c_created_by           
            ,d.promotion_id
            ,d.promo_start_end_date       -- Bug 28893
            ,g.goallevel_id
            ,COUNT(d.user_id) total_participants
            ,sum(achieved) nbr_goal_achieved   
            ,sum(d.calculated_payout) calculated_payout
            ,d.manager    
            ,SUM(CASE WHEN d.percent_of_goal between 0 and 25 
                THEN 1 END) nbr_between_0_25
            ,sum(case when d.percent_of_goal between 26 and 50 
                then 1 end) nbr_between_26_50
            ,sum(case when d.percent_of_goal between 51 and 75 
                then 1 end) nbr_between_51_75
            ,sum(case when d.percent_of_goal between 76 and 99 
                then 1 end) nbr_between_76_99
            ,sum(case when d.percent_of_goal > 99 
                then 1 end) nbr_over_100     
       FROM rpt_goal_selection_detail d
           ,rpt_hierarchy h
           ,goalquest_paxgoal g
      WHERE h.node_id = d.node_id (+)
         AND h.hier_level = rec_level.hier_level
         and d.pax_goal_id = g.pax_goal_id (+)
      GROUP BY h.parent_node_id ,nvl(d.node_id,h.node_id)
               , job_position, department, user_status, manager
               , d.promotion_id
               , d.promo_start_end_date       -- Bug 28893
               , g.goallevel_id, h.hier_level);

    v_commit_cnt := SQL%ROWCOUNT + 1;

    FOR rec_hier IN cur_hier(rec_level.hier_level) LOOP

      IF rec_hier.is_leaf = 1 THEN --they have no level below them

        --insert the same exact records as the teamsum insert
        --except change the record type
       INSERT INTO rpt_goal_selection_summary
           (record_type,
           header_node_id,
           detail_node_id,
           pax_status,
           job_position,
           department,
           hier_level,
           is_leaf,
           date_created,
           created_by,
           promotion_id,
           promo_start_end_date,          -- Bug 28893
           goal_level,
           total_participants,
           nbr_goal_achieved,
           calculated_payout,
           manager,
           nbr_pax_25_percent_of_goal,
           nbr_pax_50_percent_of_goal,
           nbr_pax_75_percent_of_goal,
           nbr_pax_76_99_percent_of_goal,
           nbr_pax_100_percent_of_goal)
        (SELECT d.hier_level||'-nodesum'
                ,d.header_node_id
                ,d.detail_node_id
                ,d.pax_status
                ,d.job_position
                ,d.department
                ,d.hier_level
                ,d.is_leaf
                ,sysdate
                ,c_created_by
                ,d.promotion_id
                ,d.promo_start_end_date   -- Bug 28893
                ,d.goal_level
                ,d.total_participants
                ,d.nbr_goal_achieved
                ,d.calculated_payout
                ,d.manager
                ,d.nbr_pax_25_percent_of_goal
                ,d.nbr_pax_50_percent_of_goal
                ,d.nbr_pax_75_percent_of_goal
                ,d.nbr_pax_76_99_percent_of_goal
                ,d.nbr_pax_100_percent_of_goal
           FROM rpt_goal_selection_summary d
          WHERE d.hier_level = rec_level.hier_level
          AND   d.header_node_id = rec_hier.parent_node_id
          AND   d.detail_node_id = rec_hier.node_id);

          v_commit_cnt := SQL%ROWCOUNT + 1;

      ELSE --rec_hier.is_leaf != 1

        --sum the node and the team records
    INSERT INTO rpt_goal_selection_summary
           (record_type,
           header_node_id,
           detail_node_id,
           pax_status,
           job_position,
           department,
           hier_level,
           is_leaf,
           date_created,
           created_by,
           promotion_id,
           promo_start_end_date, -- Bug 28893
           goal_level,
           total_participants,
           nbr_goal_achieved,
           calculated_payout,
           manager,
           nbr_pax_25_percent_of_goal,
           nbr_pax_50_percent_of_goal,
           nbr_pax_75_percent_of_goal,
           nbr_pax_76_99_percent_of_goal,
           nbr_pax_100_percent_of_goal)
        (SELECT a.record_type,
                a.parent_node_id,
                a.node_id,
                nvl(a.pax_status,' ') pax_status,
                NVL(a.job_position,' ') job_position,
                NVL(a.department,' ') department,
                a.hier_level,
                rec_hier.is_leaf,
                SYSDATE,
                c_created_by,
                a.promotion_id,
                a.promo_start_end_date,   -- Bug 28893
                a.goal_level,
                SUM(a.total_participants) total_participants,
                SUM(a.nbr_goal_achieved) no_goal_achieved,
                SUM(a.calculated_payout) calculated_payout,
                a.manager,
                SUM(a.nbr_pax_25_percent_of_goal) nbr_pax_25_percent_of_goal,
                SUM(a.nbr_pax_50_percent_of_goal) nbr_pax_50_percent_of_goal,
                SUM(a.nbr_pax_75_percent_of_goal) nbr_pax_75_percent_of_goal,
                SUM(a.nbr_pax_76_99_percent_of_goal) nbr_pax_76_99_percent_of_goal,
                SUM(a.nbr_pax_100_percent_of_goal) nbr_pax_100_percent_of_goal
        FROM (
        SELECT h.hier_level||'-nodesum' record_type
              ,h.parent_node_id
              ,h.node_id
              ,nodesum.pax_status
              ,nodesum.job_position
              ,nodesum.department
              ,nodesum.is_leaf
              ,nodesum.date_created
              ,nodesum.created_by
              ,nodesum.promotion_id 
              ,nodesum.promo_start_end_date  -- Bug 28893
              ,nodesum.goal_level
              ,nodesum.total_participants
              ,nodesum.nbr_goal_achieved
              ,nodesum.calculated_payout    
              ,nodesum.manager 
              ,nodesum.NBR_PAX_25_PERCENT_OF_GOAL
              ,nodesum.NBR_PAX_50_PERCENT_OF_GOAL
              ,nodesum.NBR_PAX_75_PERCENT_OF_GOAL
              ,nodesum.NBR_PAX_76_99_PERCENT_OF_GOAL
              ,nodesum.NBR_PAX_100_PERCENT_OF_GOAL            
              ,h.hier_level
         FROM rpt_goal_selection_summary nodesum
              ,rpt_hierarchy h
         WHERE h.node_id = nodesum.header_node_id
           AND nodesum.hier_level = rec_level.hier_level+1
           AND nodesum.record_type LIKE '%node%'
           AND nodesum.header_node_id = rec_hier.node_id
        UNION ALL
        SELECT nodesum.hier_level||'-nodesum' record_type
              ,nodesum.header_node_id
              ,nodesum.detail_node_id
              ,nodesum.pax_status
              ,nodesum.job_position
              ,nodesum.department
              ,nodesum.is_leaf
              ,nodesum.date_created
              ,nodesum.created_by
              ,nodesum.promotion_id 
              ,nodesum.promo_start_end_date  -- Bug 28893
              ,nodesum.goal_level
              ,nodesum.total_participants
              ,nodesum.nbr_goal_achieved
              ,nodesum.calculated_payout 
              ,nodesum.manager 
              ,nodesum.NBR_PAX_25_PERCENT_OF_GOAL
              ,nodesum.NBR_PAX_50_PERCENT_OF_GOAL
              ,nodesum.NBR_PAX_75_PERCENT_OF_GOAL
              ,nodesum.NBR_PAX_76_99_PERCENT_OF_GOAL
              ,nodesum.NBR_PAX_100_PERCENT_OF_GOAL            
              ,nodesum.hier_level
          FROM rpt_goal_selection_summary nodesum
         WHERE nodesum.hier_level = rec_level.hier_level
           AND nodesum.record_type LIKE '%team%'
           AND nodesum.detail_node_id = rec_hier.node_id
        ) a
        GROUP BY a.record_type,a.parent_node_id,a.node_id,
        a.pax_status,a.job_position,a.department,a.hier_level,
        a.promotion_id,
        a.promo_start_end_date,  -- Bug 28893
        a.goal_level, a.manager);

        v_commit_cnt := SQL%ROWCOUNT + 1;

        IF SQL%ROWCOUNT = 0 THEN

          --if the above insert does not insert any records
          --then insert an empty record for the level
          INSERT INTO rpt_goal_selection_summary
                 (record_type,
                   header_node_id,
                   detail_node_id,
                   pax_status,
                   job_position,
                   department,
                   hier_level,
                   is_leaf,
                   date_created,
                   created_by,
                   promotion_id, 
                   promo_start_end_date,  -- Bug 28893
                   goal_level,
                   total_participants,
                   nbr_goal_achieved,
                   calculated_payout,
                   manager,
                   nbr_pax_25_percent_of_goal,
                   nbr_pax_50_percent_of_goal,
                   nbr_pax_75_percent_of_goal,
                   nbr_pax_76_99_percent_of_goal,
                   nbr_pax_100_percent_of_goal)
            (SELECT h.hier_level||'-nodesum'
                  ,h.parent_node_id
                  ,h.node_id
                  ,nvl(s.pax_status,' ') pax_status
                  ,NVL(s.job_position,' ') job_position
                  ,NVL(s.department,' ') department
                  ,h.hier_level
                  ,rec_hier.is_leaf
                  ,sysdate
                  ,c_created_by
                  ,promotion_id
                  ,s.promo_start_end_date    -- Bug 28893
                  ,goal_level
                  ,SUM(total_participants) total_participants
                  ,SUM(nbr_goal_achieved) no_goal_achieved
                  ,SUM(calculated_payout) calculated_payout
                  ,s.manager
                  ,SUM(nbr_pax_25_percent_of_goal) nbr_pax_25_percent_of_goal
                  ,SUM(nbr_pax_50_percent_of_goal) nbr_pax_50_percent_of_goal
                  ,SUM(nbr_pax_75_percent_of_goal) nbr_pax_75_percent_of_goal
                  ,SUM(nbr_pax_76_99_percent_of_goal) nbr_pax_76_99_percent_of_goal                  
                  ,SUM(nbr_pax_100_percent_of_goal) nbr_pax_100_percent_of_goal                  
             FROM rpt_goal_selection_summary s
                 ,rpt_hierarchy h
            WHERE h.node_id = s.header_node_id (+)
            and   s.hier_level = rec_level.hier_level+1
            AND   s.record_type LIKE '%node%'
            AND   s.header_node_id = rec_hier.node_id
            GROUP BY h.parent_node_id ,h.node_id ,pax_status,
            job_position, department,h.hier_level,
            rec_hier.is_leaf, promotion_id,
            s.promo_start_end_date,    -- Bug 28893
            goal_level, manager);

          v_commit_cnt := SQL%ROWCOUNT + 1;

        END IF;

      END IF;

    END LOOP;

    IF v_commit_cnt > 500 THEN

      v_commit_cnt := 0;
    END IF;

  END LOOP; --rec_level


EXCEPTION
  WHEN others THEN
      dbms_output.put_line(v_stage||' sqlerrm: '||sqlerrm);
END; 

-- 07/18/2012 end 
*/
FUNCTION F_GET_NBR_PAX_PER_LEVEL(
           p_in_node_id IN NUMBER,
           p_in_promotion_id IN NUMBER,
           p_in_level IN NUMBER )
  return number IS

--
--
-- Purpose: Populate rpt_enrollment_summary table.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Sedey      04/04/2007 Initial code.

v_pax_count   NUMBER :=0;

BEGIN

  IF p_in_level = 0 THEN
    SELECT count(1)
      INTO v_pax_count
      FROM rpt_goal_selection_detail gsd
     WHERE gsd.manager = 0
       AND gsd.node_id = p_in_node_id
       AND gsd.promotion_id = p_in_promotion_id;
  ELSE
    SELECT count(1)
      INTO v_pax_count
      FROM rpt_goal_selection_detail gsd,
           goalquest_goallevel gl,
           goalquest_paxgoal pg
     WHERE gsd.pax_goal_id = pg.pax_goal_id
       AND pg.goallevel_id = gl.goallevel_id
       AND gsd.manager = 0
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
           p_in_node_id IN NUMBER,
           p_in_promotion_id IN NUMBER,
           p_in_goal_selected IN VARCHAR2 )
  return number IS

--
--
-- Purpose: Populate rpt_enrollment_summary table.
--
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     ------------------------------------------
-- Sedey      04/20/2007 Initial code.

v_pax_count   NUMBER :=0;

BEGIN

  IF p_in_goal_selected = 'true' THEN
    SELECT count(1)
      INTO v_pax_count
      FROM rpt_goal_selection_detail gsd,goalquest_goallevel gl,
      goalquest_paxgoal pg
     WHERE gsd.manager = 0
       AND gsd.node_id = p_in_node_id
       AND gsd.promotion_id = p_in_promotion_id
       AND gsd.pax_goal_id = pg.pax_goal_id 
       AND pg.goallevel_id = gl.goallevel_id;
  ELSE
    SELECT count(1)
      INTO v_pax_count
      FROM rpt_goal_selection_detail gsd,goalquest_goallevel gl,
      goalquest_paxgoal pg
     WHERE gsd.manager = 0
       AND gsd.node_id = p_in_node_id
       AND gsd.promotion_id = p_in_promotion_id
       AND gsd.pax_goal_id = pg.pax_goal_id (+)
       AND pg.goallevel_id = gl.goallevel_id (+)
       AND pg.goallevel_id is null ;
  END IF;

  return v_pax_count;

EXCEPTION
  WHEN OTHERS THEN
    return v_pax_count;

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
                           10/1/2013  Added new report tables for GQ mgr override.
--------------------------------------------------------------------*/

  v_points   NUMBER := 0;

BEGIN

  IF p_in_record_type = 'team' THEN

    SELECT NVL(SUM(rpt.override_amount), 0)
      INTO v_points
      FROM rpt_goal_manager_override rpt,
           promotion promo
     WHERE rpt.promotion_id   = promo.promotion_id
       AND rpt.node_id IN (SELECT * FROM TABLE(get_array(p_in_node_id)))     
       AND ((p_in_promotion_id IS NULL) OR (p_in_promotion_id is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotion_id)))));
       
  ELSE
  
    SELECT NVL(SUM(rpt.override_amount), 0)
      INTO v_points
      FROM rpt_goal_manager_override rpt,
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


PROCEDURE P_RPT_GOAL_PARTNER(p_in_requested_user_id      IN  NUMBER,
                             p_out_return_code           OUT NUMBER,
                             p_out_error_message         OUT VARCHAR2)
IS
/*******************************************************************************
--
-- Purpose: Populate RPT_GOAL_PARTNER table.
--
-- MODIFICATION HISTORY
-- Person      Date         Comments
-- ---------   ----------   ------------------------------------------
-- M Lindvig   03/24/2008   Created
-- Arun S      10/02/2012   changes to populate country id instead of cm_asset_code
--Ravi Dhanekula  04/18/2013  Added promotion type also to the where clause to exclude Challenpoint data.
-- Chidamba       10/03/2013  Defect #4811 Report Refresh Process is getting failed fixed    
*******************************************************************************/
  c_created_by      CONSTANT RPT_GOAL_PARTNER.created_by%TYPE:=0;
  v_process_name    VARCHAR2(50) := 'P_RPT_GOAL_PARTNER';
  v_release_level   NUMBER := 0;
  v_stage           execution_log.text_line%TYPE;
 /*
  -- get all pax
    CURSOR cur_goalquest_detail IS
       SELECT pp.PARTICIPANT_ID, au.FIRST_NAME, au.MIDDLE_NAME, au.LAST_NAME, pa.STATUS, un.NODE_ID, pp.PARTNER_ID, sau.FIRST_NAME partner_first_name, sau.MIDDLE_NAME partner_middle_name, sau.LAST_NAME partner_last_name, 
       pp.PROMOTION_ID, p.PROMOTION_NAME, p.PROMOTION_START_DATE, p.PROMOTION_END_DATE,gp.PAX_GOAL_ID, gp.GOALLEVEL_ID, pe.position_type, pe.department_type,
       gg.GOAL_LEVEL_NAME, gp.BASE_QUANTITY, DECODE(pg.issue_awards_run, 1, 0, null) ACHIEVED
       FROM participant_partner pp, goalquest_paxgoal gp, goalquest_goallevel gg, application_user au, 
            promotion p, rpt_pax_promo_eligibility un, promo_goalquest pg, participant_employer pe, participant pa,
            (SELECT u.USER_ID, u.user_name, u.FIRST_NAME, u.MIDDLE_NAME, u.LAST_NAME FROM application_user u) sau
            WHERE pp.PARTNER_ID IS NOT NULL
            AND gp.USER_ID = pp.PARTICIPANT_ID
            AND gp.PROMOTION_ID = pp.PROMOTION_ID
            AND gp.GOALLEVEL_ID = gg.GOALLEVEL_ID
            AND pa.USER_ID = pp.PARTICIPANT_ID
            AND pp.PARTICIPANT_ID = au.USER_ID
            AND pp.PROMOTION_ID = p.PROMOTION_ID
            AND un.PARTICIPANT_ID = pp.PARTICIPANT_ID
            AND un.promotion_id = pp.promotion_id 
            AND pg.PROMOTION_ID = gg.PROMOTION_ID
            AND sau.USER_ID = pp.PARTNER_ID
            AND pe.user_id(+) = gp.USER_ID
            AND (pe.termination_date IS NULL OR pe.termination_date >= trunc(SYSDATE));

  rec_rpt RPT_GOAL_PARTNER%ROWTYPE;
 */
 
BEGIN

 /* 
  DELETE  RPT_GOAL_PARTNER;
  
  MERGE INTO rpt_refresh_date B
        USING (
            SELECT 'partner' as report_name, 'details' as report_type, sysdate as refresh_date
            FROM dual) E
        ON (B.report_name = e.report_name and b.report_type = e.report_type)
        WHEN MATCHED THEN
            UPDATE SET B.refresh_date = e.refresh_date
        WHEN NOT MATCHED THEN
            INSERT (B.report_name, b.report_type, b.refresh_date)
        VALUES (E.report_name, E.report_type, e.refresh_date);


  FOR rec_goalquest_detail IN cur_goalquest_detail LOOP
      SELECT rpt_goal_partner_pk_sq.nextval
        INTO rec_rpt.rpt_goal_partner_id
      FROM DUAL;
      
      rec_rpt.pax_user_id := rec_goalquest_detail.PARTICIPANT_ID;
      rec_rpt.pax_first_name := rec_goalquest_detail.first_name;
      rec_rpt.pax_middle_init := rec_goalquest_detail.middle_name;
      rec_rpt.pax_last_name := rec_goalquest_detail.last_name;
      rec_rpt.prtnr_user_id :=rec_goalquest_detail.PARTNER_ID;
      rec_rpt.PRTNR_FIRST_NAME :=rec_goalquest_detail.partner_first_name;
      rec_rpt.PRTNR_MIDDLE_INIT :=rec_goalquest_detail.partner_middle_name;
      rec_rpt.PRTNR_LAST_NAME := rec_goalquest_detail.partner_last_name;
      rec_rpt.pax_user_status :=rec_goalquest_detail.status;
      rec_rpt.pax_job_position :=rec_goalquest_detail.position_type;
      rec_rpt.pax_department := rec_goalquest_detail.department_type;
      rec_rpt.promotion_id := rec_goalquest_detail.promotion_id;
      rec_rpt.promotion_name := rec_goalquest_detail.promotion_name;
      rec_rpt.promotion_start_date := rec_goalquest_detail.promotion_start_date;
      rec_rpt.promotion_end_date := rec_goalquest_detail.promotion_end_date;
      rec_rpt.node_id := rec_goalquest_detail.node_id;
      rec_rpt.pax_goal_id := rec_goalquest_detail.pax_goal_id;
      rec_rpt.goallevel_id := rec_goalquest_detail.goallevel_id;
      rec_rpt.goal_level_name := rec_goalquest_detail.goal_level_name;
      rec_rpt.base_quantity := rec_goalquest_detail.base_quantity;
      rec_rpt.ACHIEVED := rec_goalquest_detail.ACHIEVED;
      rec_rpt.created_by := c_created_by;
      rec_rpt.DATE_CREATED := sysdate;
      rec_rpt.version := 0;
      
      INSERT INTO RPT_GOAL_PARTNER VALUES rec_rpt;
  END LOOP;
  */
  
  v_stage := 'MERGE goal partner records';
  
 MERGE INTO rpt_goal_partner d
   USING (WITH goalquest_detail AS
          (SELECT pp.participant_id, 
                  au.first_name, 
                  au.middle_name, 
                  au.last_name, 
                  pa.status, 
                  un.node_id, 
                  pp.partner_id, 
                  sau.first_name partner_first_name, 
                  sau.middle_name partner_middle_name, 
                  sau.last_name partner_last_name, 
                  pp.promotion_id, 
                  p.promotion_name, 
                  p.promotion_start_date, 
                  p.promotion_end_date,
                  gp.pax_goal_id, 
                  gp.goallevel_id, 
                  pe.position_type, 
                  pe.department_type,
                  gg.goal_level_cm_asset_code goal_level_name, 
                  gp.base_quantity, 
                  DECODE(pg.issue_awards_run, 1, 0, null) ACHIEVED,
                  --sau.cm_asset_code
                  sau.country_id                        --10/02/2012
             FROM participant_partner pp, 
                  goalquest_paxgoal gp, 
                  goalquest_goallevel gg,
                  application_user au, 
                  promotion p,
                  rpt_pax_promo_eligibility un,
                  promo_goalquest pg,
                  participant_employer pe, 
                  participant pa,                  
                  (SELECT u.USER_ID, u.user_name, u.FIRST_NAME, u.MIDDLE_NAME, u.LAST_NAME ,ua.country_id  --c.cm_asset_code  --10/02/2012
                    FROM application_user u,             
                         --country c,
                         user_address ua
                   WHERE u.user_id = ua.user_id(+)
                     --AND ua.country_id = c.country_id(+)                                                                                             
                     AND ua.is_primary(+) = 1) sau
        WHERE pp.partner_id IS NOT NULL
          AND gp.user_id = pp.participant_id
          AND gp.promotion_id = pp.promotion_id
          AND gp.goallevel_id = gg.goallevel_id
          AND pa.user_id = pp.participant_id
          AND pp.participant_id = au.user_id
          AND pp.promotion_id = p.promotion_id
          AND p.promotion_type = 'goalquest' --04/18/2013
          AND un.participant_id = pp.participant_id
          AND un.promotion_id = pp.promotion_id 
          AND pg.promotion_id = gg.promotion_id
          AND sau.user_id = pp.partner_id
          AND pe.user_id(+) = gp.user_id
          AND (pe.termination_date IS NULL 
            OR pe.termination_date >= trunc(SYSDATE)))
          SELECT es.s_rowid,
                 gd.*
          FROM (  -- get existing summaries based on derived details
                 SELECT rgp.ROWID AS s_rowid,
                        rgp.promotion_id,
                        rgp.pax_user_id,
                        rgp.prtnr_user_id,
                        rgp.node_id                 --10/03/2013
                   FROM rpt_goal_partner rgp
               ) es
               -- full outer join so unmatched existing summaries can be deleted
               FULL OUTER JOIN goalquest_detail gd
               ON (   es.prtnr_user_id = gd.PARTNER_ID
                  AND es.pax_user_id   = gd.participant_id
                  AND es.promotion_id  = gd.promotion_id
                  AND es.node_id       = gd.node_id         --10/03/2013
                  ) 
         ) s
       ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
     UPDATE SET       
        d.pax_first_name  = s.first_name,
        d.pax_middle_init = s.middle_name,
        d.pax_last_name     = s.last_name,       
        d.prtnr_first_name  = s.partner_first_name,
        d.prtnr_middle_init = s.partner_middle_name,
        d.prtnr_last_name   = s.partner_last_name,
        d.pax_user_status   = s.status,
        d.pax_job_position  = s.position_type,
        d.pax_department  = s.department_type,
        d.promotion_name  = s.promotion_name,
        d.promotion_start_date = s.promotion_start_date,
        d.promotion_end_date = s.promotion_end_date,
        d.node_id         =   s.node_id,
        d.pax_goal_id     = s.pax_goal_id,
        d.goallevel_id    = s.goallevel_id,
        d.goal_level_name = s.goal_level_name,
        d.base_quantity   = s.base_quantity,
        d.achieved        = s.achieved,  
        --d.cm_asset_code   = s.cm_asset_code,
        d.country_id      = s.country_id,                       --10/02/2012     
        d.modified_by     = c_created_by,
        d.date_modified   = SYSDATE        
   WHERE ( -- only update records with different values
           DECODE(d.pax_first_name  , s.first_name,1,0)   = 0
        OR DECODE(d.pax_middle_init , s.middle_name,1,0)  = 0
        OR DECODE(d.pax_last_name     , s.last_name,1,0)  = 0        
        OR DECODE(d.prtnr_first_name  , s.partner_first_name,1,0)  = 0
        OR DECODE(d.prtnr_middle_init , s.partner_middle_name,1,0) = 0
        OR DECODE(d.prtnr_last_name  , s.partner_last_name,1,0)  = 0
        OR DECODE(d.pax_user_status  , s.status,1,0) = 0
        OR DECODE(d.pax_job_position , s.position_type,1,0)  = 0
        OR DECODE(d.pax_department  , s.department_type,1,0) = 0
        OR DECODE(d.promotion_name  , s.promotion_name,1,0)  = 0
        OR DECODE(d.promotion_start_date , s.promotion_start_date,1,0) = 0
        OR DECODE(d.promotion_end_date   , s.promotion_end_date,1,0)   = 0
        OR DECODE(d.node_id         ,   s.node_id,1,0)   = 0
        OR DECODE(d.pax_goal_id     , s.pax_goal_id,1,0) = 0
        OR DECODE(d.goallevel_id    , s.goallevel_id,1,0)    = 0
        OR DECODE(d.goal_level_name , s.goal_level_name,1,0) = 0
        OR DECODE(d.base_quantity   , s.base_quantity,1,0) = 0
        OR DECODE(d.achieved        , s.achieved,1,0) = 0
        --OR DECODE(d.cm_asset_code   , s.cm_asset_code,1,0) = 0
        OR DECODE(d.country_id   , s.country_id,1,0) = 0                        --10/02/2012
        )
  WHEN NOT MATCHED THEN      
    INSERT (rpt_goal_partner_id,
            pax_user_id,
            pax_first_name,
            pax_middle_init,
            pax_last_name,
            prtnr_user_id,
            prtnr_first_name,
            prtnr_middle_init,
            prtnr_last_name,
            pax_user_status,
            pax_job_position,
            pax_department,
            promotion_id,
            promotion_name,
            promotion_start_date,
            promotion_end_date,
            node_id,
            pax_goal_id,
            goallevel_id,
            goal_level_name,
            base_quantity,      
            achieved,  
            --cm_asset_code,
            country_id,                                 --10/02/2012                  
            created_by,
            date_created,
            version) 
    VALUES (rpt_goal_partner_pk_sq.NEXTVAL,
            s.participant_id,
            s.first_name,
            s.middle_name,
            s.last_name,
            s.partner_id,
            s.partner_first_name,
            s.partner_middle_name,
            s.partner_last_name,
            s.status,
            s.position_type,
            s.department_type,
            s.promotion_id,
            s.promotion_name,
            s.promotion_start_date,
            s.promotion_end_date,
            s.node_id,
            s.pax_goal_id,
            s.goallevel_id,
            s.goal_level_name,
            s.base_quantity,
            s.achieved,
            --s.cm_asset_code,
            s.country_id,                               --10/02/2012
            c_created_by,
            SYSDATE,
            0);
            
  p_out_return_code   := 00;  
   
EXCEPTION
  WHEN OTHERS THEN
  
  p_out_return_code   := 99;
  prc_execution_log_entry(v_process_name,
                          v_release_level,
                          'ERROR',
                          'Param:'||p_in_requested_user_id||':'||v_stage||SQLERRM,
                          NULL);
  p_out_error_message := v_stage||SQLERRM;                           

END;

PROCEDURE P_RPT_GOAL_MANAGER_OVERRIDE (p_in_requested_user_id      IN  NUMBER,
                               p_out_return_code           OUT NUMBER,
                               p_out_error_message         OUT VARCHAR2) IS
  /*******************************************************************************
--
-- Purpose: Populate RPT_GOAL_MANAGER_OVERRIDE table.
--
-- MODIFICATION HISTORY
-- Person      Date         Comments
-- ---------   ----------   ------------------------------------------
--Ravi Dhanekula  09/30/2013  Initial
*******************************************************************************/
  
  c_created_by      CONSTANT rpt_goal_manager_override.created_by%TYPE:= p_in_requested_user_id;
  v_stage           VARCHAR2(250);  
  v_language_code   VARCHAR2(10):='en_US';
  c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('P_RPT_GOAL_MANAGER_OVERRIDE');
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1;   
BEGIN
MERGE INTO rpt_goal_manager_override rpt
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
payout_calculation_audit pca,
application_user au,
vw_curr_pax_employer vue,
promo_goalquest pg,
participant pa,
promotion promo,
user_node un
WHERE pca.reason_type = 'mo_success'
AND pca.journal_id = j.journal_id
AND pca.participant_id = au.user_id
AND au.user_id = vue.user_id(+)
AND au.user_id = un.user_id
AND un.role = 'own'
AND pca.participant_id = pa.user_id
AND j.promotion_id = pg.promotion_id
AND pg.promotion_id = promo.promotion_id
AND promo.promotion_type = 'goalquest' 
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
            un.node_id) s
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
(rpt_goal_mgr_override_id,
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
  VALUES(rpt_goal_mgr_override_id.NEXTVAL,
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
     prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Param:'||p_in_requested_user_id||':'||sqlerrm, NULL);        

END;

END;
/
