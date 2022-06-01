CREATE OR REPLACE PACKAGE pkg_query_quiz_activity AS
/******************************************************************************
   NAME:       pkg_quiz_activity_reports
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/24/2014      keshaboi       1. Created this package.
******************************************************************************/
/* getQuizActivitySummary */
procedure prc_getQuizActivitySummary(
      p_in_departments         IN     VARCHAR,
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_result              IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_paxId               IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);

       /* getQuizActivityDetailOneResults */
procedure prc_getQuizActivityDetailOne(
      p_in_departments         IN     VARCHAR,
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_result              IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_paxId               IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);

  /* prc_getQuizActivityDetailTwo */
procedure prc_getQuizActivityDetailTwo(
      p_in_departments         IN     VARCHAR,
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_result              IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_paxId               IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
/* CHARTS */

/* getQuizActivityForOrgBarResults */

procedure prc_getQuizActivityForOrgBar(
      p_in_departments         IN     VARCHAR,
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_result              IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_paxId               IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);

/* getQuizStatusPercentForOrgBarResults */

procedure prc_getQuizStatusPercentForOrB(
      p_in_departments         IN     VARCHAR,
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_result              IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_paxId               IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);

END pkg_query_quiz_activity;


/
CREATE OR REPLACE PACKAGE BODY pkg_query_quiz_activity
AS
   PROCEDURE prc_getQuizActivitySummary (
      p_in_departments         IN     VARCHAR,
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_result              IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_paxId               IN     NUMBER,
      p_out_return_code           OUT NUMBER,
      p_out_data                  OUT SYS_REFCURSOR,
      p_out_size_data             OUT NUMBER,
      p_out_totals_data           OUT SYS_REFCURSOR)
   IS
   
/******************************************************************************
  NAME:       prc_getQuizActivitySummary
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014      Initial..(the queries were in Java before the release 5.4. So no comments available so far) 
  KrishanDeepika        09/10/2014      56380 bug fix      
  Swati                 10/15/2014      Bug 57411 - In Reports column 'Eligible Participants' count should display based on the 
                                        Participant Status = Inactive/active selected in the Change Filter window
  Swati                 10/20/2014       Bug 57499 - Reports - Quiz Activity - Attempts In Progress column values always shows 0 as default
  Swati                 10/21/2014       Bug 57501 - Reports - Quiz Activity - Summary - Quiz Attempts column value is not matching with Extract.
  Suresh J              10/21/2014        Bug 57500 - Reports - Quiz Activity - Summary - Eligible Quizzes is not matching before and after drilldown       
  Swati                    10/31/2014        Bug 57676 - Reports-Recognition Given/Received - Results are not correct when Participant Status = 'Show all'.
  Suresh J              06/26/2015     Bug 61079 -  Issue in the 'Eligible Quizzes' column count while selecting a DIY Quiz promotion     
  nagarajs              10/01/2015     Bug 64138 -"Eligible Quizzes" column in the Quiz Activity report is not getting sorted or filtered 
  Ravi Dhanekula   12/30/2015     Bug # 65047 - Numbers for Eligible Quizzes are not correct (Please replace code between the start and end mentioned below for adding fix to clients.
  Ravi Dhanekula   1/5/2016 Bug # 65047 - Used rpt_hierarchy table instead of report summary table to make sure all the nodes are coming up in the summary table.
  Ravi Arumugam   02/16/2016 Bug # 65696-Quiz activity report - %passed and %failed numbers are wrong for individual org units, though it is OK at summary level (last line)
                                       . In-Progress is not considered while calculating percent
 Ravi Arumugam   04/05/2016  Bug-66316 -Quiz Activity - Quiz Results is Displaying as Progress when Pax didnot even Complete Taking Quiz 
 nagarajs         04/13/2016  Bug 66413, 66421 fixes                                          
 ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getQuizActivitySummary' ;
  v_stage        VARCHAR2 (500);
  v_sortCol             VARCHAR2(200);
  l_query VARCHAR2(32767);   
  v_non_diy_quiz_cnt NUMBER(5);
  
   /* getQuizActivitySummary */
   BEGIN
   
   DELETE gtt_recog_elg_count; --08/06/2014 Start
   
  SELECT COUNT(DISTINCT promotion_type) INTO v_non_diy_quiz_cnt
    FROM 
    (SELECT p.promotion_type ,-1*q.quiz_id AS ID, 'DIY Promotion ' ||q.name AS value
        FROM quiz q,
             promotion p,
             diy_quiz dq
     WHERE promotion_status IN ( 'live', 'expired' )
            AND is_deleted          = 0
            AND q.quiz_id           =dq.quiz_id
            AND dq.promotion_id     = p.promotion_id
            AND p.promotion_type    ='diy_quiz'
    UNION ALL
    SELECT promotion_type,promotion_id AS id, promotion_name    AS value
        FROM Promotion
    WHERE   promotion_status IN ( 'live', 'expired' )
            AND is_deleted          = 0
            AND promotion_type      ='quiz'
     )
    WHERE promotion_type <> 'diy_quiz';
   
IF (p_in_diyQuizId IS NOT NULL AND p_in_promotionId IS NULL  OR (p_in_diyQuizId IS NULL AND v_non_diy_quiz_cnt = 0 )) THEN   --06/26/2015 --12/30/2015 Bug # 12/30/2015 Start

--IF (p_in_diyQuizId IS NOT NULL OR (p_in_diyQuizId IS NULL AND v_non_diy_quiz_cnt = 0 )) THEN   --06/26/2015

INSERT INTO gtt_recog_elg_count     --06/26/2015
SELECT eligible_cnt,node_id,'nodesum' record_type 
FROM (
SELECT COUNT(pe.participant_id) eligible_cnt,
         rhr.node_id
  FROM   diy_quiz_participant pe,
         user_node un,
         vw_curr_pax_employer emp,
         rpt_hierarchy_rollup rhr
  WHERE  pe.quiz_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_diyQuizId  ) )) 
  AND    un.is_primary = 1
  AND    pe.participant_id = un.user_id
  AND    pe.participant_id = emp.user_id
  AND    rhr.child_node_id = un.node_id 
--  AND    rhr.child_node_id = p_in_parentNodeId 
  AND    rhr.child_node_id IN (SELECT child_node_id FROM rpt_hierarchy_rollup WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) ))) 
  AND    emp.position_type =  NVL(p_in_jobposition,emp.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active' -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
  AND ((p_in_departments IS NULL)
      OR emp.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY rhr.node_id)
UNION ALL  
SELECT eligible_cnt,node_id,'teamsum' record_type 
FROM (
SELECT COUNT(pe.participant_id) eligible_cnt,
         un.node_id
  FROM   diy_quiz_participant pe,
         user_node un,
         vw_curr_pax_employer emp
  WHERE  pe.quiz_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_diyQuizId  ) ))
  AND    un.is_primary = 1
  AND    pe.participant_id = un.user_id
  AND    pe.participant_id = emp.user_id
  AND    un.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    emp.position_type =  NVL(p_in_jobposition,emp.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active' -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
  AND ((p_in_departments IS NULL)
      OR emp.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  GROUP BY un.node_id);
ELSE --IF p_in_diyQuizId IS NOT NULL  --06/26/2015

prc_execution_log_entry (c_process_name,
                                  1,
                                  'INFO',
                                  'p_in_diyQuizId'||p_in_diyQuizId||'p_in_promotionId'||p_in_promotionId|| 'line 126',
                                  NULL);
   
 IF p_in_promotionId IS NOT NULL AND NVL(INSTR(p_in_promotionId,','),0) = 0 AND fnc_check_promo_aud('giver','quiz',p_in_promotionId) = 1 THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt,node_id,'nodesum' record_type FROM (
    SELECT SUM(elig_count) eligible_cnt,
         node_id FROM (
         SELECT pe.elig_count,
         pe.node_id
  FROM   rpt_pax_promo_elig_allaud_node pe
  WHERE  pe.giver_recvr_type = 'giver'
  AND pe.node_id IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) )
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active' -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
          UNION ALL          
          SELECT COUNT(pe.participant_id) elig_count,
         rhr.node_id
  FROM   diy_quiz_participant pe,
         user_node un,
         vw_curr_pax_employer emp,
         rpt_hierarchy_rollup rhr
  WHERE  pe.quiz_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_diyQuizId  ) )) 
  AND    un.is_primary = 1
  AND    pe.participant_id = un.user_id
  AND    pe.participant_id = emp.user_id
  AND    rhr.child_node_id = un.node_id 
--  AND    rhr.child_node_id = p_in_parentNodeId 
  AND    rhr.child_node_id IN (SELECT child_node_id FROM rpt_hierarchy_rollup WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) ))) 
  AND    emp.position_type =  NVL(p_in_jobposition,emp.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active' -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
  AND ((p_in_departments IS NULL)
      OR emp.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
          GROUP BY rhr.node_id)
  GROUP BY node_id)
  UNION ALL
  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(elig_count) eligible_cnt,node_id FROM (
         SELECT pe.elig_count,pe.node_id
  FROM   rpt_pax_promo_elig_allaud_team pe
  WHERE  pe.giver_recvr_type = 'giver'
  AND pe.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active' -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
          UNION ALL
          SELECT COUNT(pe.participant_id) eligible_count,
         un.node_id
  FROM   diy_quiz_participant pe,
         user_node un,
         vw_curr_pax_employer emp
  WHERE  pe.quiz_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_diyQuizId  ) ))
  AND    un.is_primary = 1
  AND    pe.participant_id = un.user_id
  AND    pe.participant_id = emp.user_id
  AND    un.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    emp.position_type =  NVL(p_in_jobposition,emp.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active' -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
  AND ((p_in_departments IS NULL)
      OR emp.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
          GROUP BY un.node_id)          
  GROUP BY node_id);

  ELSIF p_in_promotionId IS NOT NULL AND NVL(INSTR(p_in_promotionId,','),0) = 0 THEN
  
INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(elig_count) eligible_cnt,node_id FROM
    ( SELECT pe.elig_count,pe.node_id
  FROM   rpt_pax_promo_elig_speaud_node pe
  WHERE  pe.giver_recvr_type = 'giver'
  AND pe.promotion_id = NVL(p_in_promotionId,-1)
  AND pe.promotion_type IN ('quiz','diy_quiz')
  AND pe.node_id IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) )
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active' -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  UNION ALL          
          SELECT COUNT(pe.participant_id) elig_count,
         rhr.node_id
  FROM   diy_quiz_participant pe,
         user_node un,
         vw_curr_pax_employer emp,
         rpt_hierarchy_rollup rhr
  WHERE  pe.quiz_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_diyQuizId  ) )) 
  AND    un.is_primary = 1
  AND    pe.participant_id = un.user_id
  AND    pe.participant_id = emp.user_id
  AND    rhr.child_node_id = un.node_id 
--  AND    rhr.child_node_id = p_in_parentNodeId 
  AND    rhr.child_node_id IN (SELECT child_node_id FROM rpt_hierarchy_rollup WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) ))) 
  AND    emp.position_type =  NVL(p_in_jobposition,emp.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active' -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
  AND ((p_in_departments IS NULL)
      OR emp.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
          GROUP BY rhr.node_id)
  GROUP BY node_id)
  UNION ALL
  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(elig_count) eligible_cnt,node_id
  FROM ( SELECT pe.elig_count,pe.node_id
  FROM   rpt_pax_promo_elig_speaud_team pe
  WHERE  pe.giver_recvr_type = 'giver'
  AND pe.promotion_id = NVL(p_in_promotionId,-1)
  AND pe.promotion_type IN ('quiz','diy_quiz')
  AND pe.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    pe.position_type =  NVL(p_in_jobposition,pe.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active'
  AND ((p_in_departments IS NULL)
      OR pe.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
   UNION ALL
          SELECT COUNT(pe.participant_id) eligible_count,
         un.node_id
  FROM   diy_quiz_participant pe,
         user_node un,
         vw_curr_pax_employer emp
  WHERE  pe.quiz_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_diyQuizId  ) ))
  AND    un.is_primary = 1
  AND    pe.participant_id = un.user_id
  AND    pe.participant_id = emp.user_id
  AND    un.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    emp.position_type =  NVL(p_in_jobposition,emp.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active'
  AND ((p_in_departments IS NULL)
      OR emp.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
          GROUP BY un.node_id)
  GROUP BY node_id);

ELSE 

INSERT INTO gtt_recog_elg_count
SELECT eligible_cnt,node_id,'nodesum' record_type FROM (
SELECT SUM(eligible_cnt) eligible_cnt,TO_NUMBER(TRIM(npn.path_node_id)) node_id  
                                       FROM   
                                     (                                     
                                     SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT ( ppe.participant_id)  
                                                          eligible_cnt,  
                                                       ppe.node_id  
                                                  FROM (SELECT *  
                                                          FROM rpt_pax_promo_elig_quiz  
                                                         WHERE giver_recvr_type = 'giver') ppe,  
                                                       promotion P,                                                        
                                                       rpt_participant_employer rad 
                                                 WHERE ppe.promotion_id = P.promotion_id  
                                                        AND ppe.participant_id = rad.user_id(+)
                                                       AND ( ( p_in_promotionId IS NULL ) OR P.promotion_id IN (
                                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))                                                
                                                       AND P.promotion_status =  
                                                              NVL (p_in_promotionstatus,  
                                                                   P.promotion_status)  
                                               AND ((p_in_departments IS NULL) OR rad.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
                                               AND NVL (rad.position_type, 'JOB') =  
                                                      NVL (  
                                                         p_in_jobposition,  
                                                         NVL (rad.position_type, 'JOB')) 
                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                      NVL (p_in_participantStatus,  
                                                           DECODE(rad.termination_date,NULL,'active','inactive'))  
                                                       AND P.promotion_type IN ('quiz','diy_quiz')                                                         
                                              GROUP BY ppe.node_id 
                                              UNION ALL
                                              SELECT COUNT(pe.participant_id) eligible_cnt,
         un.node_id
  FROM   diy_quiz_participant pe,
         user_node un,
         vw_curr_pax_employer emp
  WHERE  pe.quiz_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_diyQuizId  ) )) 
  AND    un.is_primary = 1
  AND    pe.participant_id = un.user_id
  AND    pe.participant_id = emp.user_id
  AND    emp.position_type =  NVL(p_in_jobposition,emp.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active' -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
  AND ((p_in_departments IS NULL)
      OR emp.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
          GROUP BY un.node_id                                    
                                     ) elg, 
                                     (SELECT child_node_id node_id,node_id path_node_id FROM rpt_hierarchy_rollup WHERE NODE_ID IN (SELECT node_id FROM rpt_hierarchy WHERE parent_node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) ))npn 
                                     WHERE elg.node_id(+) = npn.node_id 
                                     GROUP BY npn.path_node_id)
                                  UNION ALL
                                  SELECT eligible_cnt,node_id,'teamsum' record_type FROM (
                                  SELECT COUNT(participant_id)eligible_cnt,node_id  FROM  (SELECT  ppe.participant_id,ppe.node_id  
                                                  FROM (SELECT*  
                                                          FROM rpt_pax_promo_elig_quiz 
                                                         WHERE giver_recvr_type = 'giver' AND node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) ))) ppe,  
                                                       promotion P,                                                        
                                                       rpt_participant_employer rad 
                                                 WHERE ppe.promotion_id = P.promotion_id  
                                                        AND ppe.participant_id = rad.user_id(+)
                                                       AND ( ( p_in_promotionId IS NULL ) OR P.promotion_id IN (
                                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))                                                      
                                                       AND P.promotion_status =  
                                                              NVL (p_in_promotionstatus,  
                                                                   P.promotion_status)  
                                               AND ((p_in_departments IS NULL) OR rad.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
                                               AND NVL (rad.position_type, 'JOB') =  
                                                      NVL (  
                                                         p_in_jobposition,  
                                                         NVL (rad.position_type, 'JOB')) 
                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                      NVL (p_in_participantStatus,  
                                                           DECODE(rad.termination_date,NULL,'active','inactive'))  
                                                       AND P.promotion_type IN ('quiz','diy_quiz') 
                                                       UNION ALL
                                                       SELECT pe.participant_id,
         un.node_id
  FROM   diy_quiz_participant pe,
         user_node un,
         vw_curr_pax_employer emp
  WHERE  pe.quiz_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_diyQuizId  ) ))
  AND    un.is_primary = 1
  AND    pe.participant_id = un.user_id
  AND    pe.participant_id = emp.user_id
  AND    un.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    emp.position_type =  NVL(p_in_jobposition,emp.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active' -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
  AND ((p_in_departments IS NULL)
      OR emp.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))
                                              GROUP BY node_id) ;     
END IF;    --08/02/2014 End
END IF; --IF p_in_diyQuizId IS NOT NULL  --06/26/2015  ----12/30/2015 Bug # 12/30/2015 End

 l_query  := 'SELECT * FROM
  ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;
       
  l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                   FROM (  SELECT dtl.node_name AS Org_Name,
                                     NVL((SELECT eligible_count FROM gtt_recog_elg_count WHERE record_type = ''nodesum'' AND node_id = dtl.node_id),0) Eligible_Quizzes, --10/01/2015 Added NVL
                                  NVL (dtl.Quiz_Attempt, 0) AS Quiz_Attempts, --10/21/2014    Bug 57501
                                  NVL (
                                       dtl.Quiz_Attempt --10/20/2014 Bug 57499
                                     - (dtl.quiz_passed + dtl.quiz_failed),
                                     0)
                                     AS Attempts_In_Progress,
                                  NVL (dtl.quiz_failed, 0) AS Attempts_Failed,
                                  NVL (dtl.quiz_passed, 0) AS Attempts_Passed,
                                  DECODE (
                                     NVL (dtl.Quiz_Attempt, 0),        --dtl.quiz_completed   Bug# 65696 02/16/2016
                                     0, 0,
                                     ROUND (
                                          (  NVL (dtl.quiz_passed, 0)   
                                           / dtl.Quiz_Attempt)          --dtl.quiz_completed  Bug# 65696 02/16/2016
                                        * 100,
                                        2))
                                     AS Eligible_Passed_Pct,
                                  DECODE (
                                     NVL (dtl.Quiz_Attempt, 0),         --dtl.quiz_completed Bug#65696 02/16/2016      
                                     0, 0,
                                     ROUND (
                                          (  NVL (dtl.quiz_failed, 0)    
                                           / dtl.Quiz_Attempt)          --dtl.quiz_completed Bug#65696 02/16/2016  
                                        * 100,
                                        2))
                                     AS Eligible_Failed_Pct,
                                  NVL (dtl.amount, 0) AS Points,
                                  NVL (dtl.sweepstakes_won, 0)
                                     AS sweepstakes_won_cnt,
                                  dtl.node_id AS Node_Id
                             FROM rpt_hierarchy ras, --replaced rpt_quiz_activity_summary with rpt_hierarchy --01/04/2016
                                  (  SELECT h.node_id,
                                            h.parent_node_id,
                                            h.node_name,
                                            sum(dtl.Quiz_Attempt) Quiz_Attempt, --10/20/2014 Bug 57499
                                            SUM (dtl.quiz_completed)
                                               quiz_completed,
                                            SUM (dtl.quiz_passed) quiz_passed,
                                            SUM (dtl.quiz_failed) quiz_failed,
                                            SUM (dtl.amount) amount,
                                            SUM (dtl.sweepstakes_won)
                                               sweepstakes_won
                                       FROM (SELECT child_node_id node_id,
                                                    node_id path_node_id
                                               FROM rpt_hierarchy_rollup) npn,
                                            rpt_hierarchy h,                                           
--                                            (  SELECT COUNT (rqd.quiz_result)
                                             (  SELECT COUNT (rqd.quiz_result) Quiz_Attempt, --10/20/2014 Bug 57499
                                                    SUM (                         --56380 bug fix start    09/10/2014                
                                                 DECODE (rqd.quiz_result,
                                                         ''progress'', 0,         -- 04/05/2016 Bug 66316 --04/13/2016
                                                         1))                         --56380 bug fix end
                                                         quiz_completed,
                                                      SUM (
                                                         DECODE (rqd.quiz_result,
                                                                 ''passed'', 1,
                                                                 0))
                                                         quiz_passed,
                                                      SUM (
                                                         DECODE (rqd.quiz_result,
                                                                 ''failed'', 1,
                                                                 0))
                                                         quiz_failed,
                                                      SUM (rqd.award_amount) amount,
                                                      rqd.node_id,
                                                      SUM (rqd.sweepstakes_won)
                                                         sweepstakes_won
                                                 FROM rpt_quiz_activity_detail rqd,
                                                      promotion p
                                                WHERE     rqd.quiz_claim_id
                                                             IS NOT NULL
                                                      AND rqd.promotion_id =
                                                             p.promotion_id
                                                      AND (   (    '''||p_in_promotionId||'''
                                                                      IS NULL
                                                               AND '''||p_in_diyQuizId||'''
                                                                      IS NULL)
                                                           OR (       '''||p_in_promotionId||'''
                                                                         IS NOT NULL
                                                                  AND rqd.promotion_id IN (SELECT *
                                                                                             FROM TABLE (
                                                                                                     get_array_varchar (
                                                                                                        '''||p_in_promotionId||''')))
                                                               OR (    '''||p_in_diyQuizId||'''
                                                                          IS NOT NULL
                                                                   AND rqd.quiz_id IN (SELECT *
                                                                                         FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                    '''||p_in_diyQuizId||'''))))))
                                                      AND p.promotion_status =
                                                             NVL (
                                                                '''||p_in_promotionStatus||''',
                                                                p.promotion_status)
                                                      AND (   ('''||p_in_departments||'''
                                                                  IS NULL)
                                                           OR rqd.department IN (SELECT *
                                                                                         FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                    '''||p_in_departments||'''))))
                                                      AND NVL (rqd.job_title,
                                                               ''job'') =
                                                             NVL (
                                                                '''||p_in_jobPosition||''',
                                                                NVL (rqd.job_title,
                                                                     ''job''))
                                                      AND rqd.participant_status =
                                                             NVL (
                                                                '''||p_in_participantStatus||''',
                                                                rqd.participant_status)
                                                      AND NVL (
                                                             TRUNC (rqd.quiz_date),
                                                             TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                         '''||p_in_fromDate||''',
                                                                                         '''||p_in_localeDatePattern||''')
                                                                                  AND TO_DATE (
                                                                                         '''||p_in_toDate||''',
                                                                                         '''||p_in_localeDatePattern||''')
                                             GROUP BY rqd.node_id) dtl
                                      WHERE   dtl.node_id(+) = npn.node_id
                                            AND npn.path_node_id = h.node_id
                                   GROUP BY h.node_id,
                                            h.node_name,
                                            h.parent_node_id) dtl
                            WHERE     ras.node_id = dtl.node_id(+) --01/04/2016--added outer join
                                  AND NVL (ras.parent_node_id, 0) IN (SELECT *
                                                                        FROM TABLE (
                                                                                get_array_varchar (
                                                                                   NVL (
                                                                                      '''||p_in_parentNodeId||''',
                                                                                      0))))
--                                  AND ras.node_name NOT LIKE ''%Team%'' --01/04/2016
                         GROUP BY dtl.Quiz_Attempt, --10/20/2014 Bug 57499
                                  dtl.node_name,
                                  dtl.node_id,
                                  dtl.quiz_completed,
                                  dtl.quiz_failed,
                                  dtl.quiz_passed,
                                  dtl.amount,
                                  dtl.sweepstakes_won
                         UNION
                         SELECT rh.node_name || '' Team'' AS Org_Name,
                                NVL((SELECT eligible_count FROM gtt_recog_elg_count WHERE record_type = ''teamsum'' AND node_id = rh.node_id),0) Eligible_Quizzes,   --10/21/2014 Bug 57500 --10/01/2015 Added NVL
                                NVL (dtl.Quiz_Attempt, 0) AS Quiz_Attempts, --10/21/2014 Bug 57501
                                NVL (
                                     dtl.Quiz_Attempt --10/20/2014 Bug 57499
                                   - (dtl.quiz_passed + dtl.quiz_failed),
                                   0)
                                   AS Attempts_In_Progress,
                                NVL (dtl.quiz_failed, 0) AS Attempts_Failed,
                                NVL (dtl.quiz_passed, 0) AS Attempts_Passed,
                                DECODE (
                                   NVL (dtl.Quiz_Attempt, 0),         --dtl.quiz_attempt Bug#65696 02/16/2016
                                   0, 0,
                                   ROUND (
                                        (  NVL (dtl.quiz_passed, 0)   
                                         / dtl.Quiz_Attempt)          --dtl.quiz_completed Bug#65696 02/16/2016 
                                      * 100,
                                      2))
                                   AS Eligible_Passed_Pct,
                                DECODE (
                                   NVL (dtl.Quiz_Attempt, 0),         --dtl.quiz_completed Bug#65696 02/16/2016
                                   0, 0,
                                   ROUND (
                                        (  NVL (dtl.quiz_failed, 0)   
                                         / dtl.Quiz_Attempt)          --dtl.quiz_completed Bug#65696 02/16/2016
                                      * 100,
                                      2))
                                   AS Eligible_Failed_Pct,
                                NVL (dtl.amount, 0) AS Points,
                                NVL (dtl.sweepstakes_won, 0)
                                   AS sweepstakes_won_cnt,
                                rh.node_id AS Node_Id
                           FROM rpt_hierarchy rh,                                
--                                (  SELECT COUNT (rqd.quiz_result)
                                 (  SELECT  COUNT (rqd.quiz_result) Quiz_Attempt, --10/20/2014 Bug 57499
                                          SUM (                                     --56380 bug fix start    09/10/2014                
                                                 DECODE (rqd.quiz_result,
                                                         ''progress'', 0,    --04/05/2016 Bug 66316    --04/13/2016
                                                         1))                         --56380 bug fix end
                                             quiz_completed,
                                          SUM (
                                             DECODE (rqd.quiz_result,
                                                     ''passed'', 1,
                                                     0))
                                             quiz_passed,
                                          SUM (
                                             DECODE (rqd.quiz_result,
                                                     ''failed'', 1,
                                                     0))
                                             quiz_failed,
                                          SUM (rqd.award_amount) amount,
                                          rqd.node_id,
                                          SUM (rqd.sweepstakes_won)
                                             sweepstakes_won
                                     FROM rpt_quiz_activity_detail rqd,
                                          promotion p
                                    WHERE     rqd.quiz_claim_id IS NOT NULL
                                          AND rqd.promotion_id = p.promotion_id
                                          AND (   (    '''||p_in_promotionId||''' IS NULL
                                                   AND '''||p_in_diyQuizId||''' IS NULL)
                                               OR (       '''||p_in_promotionId||'''
                                                             IS NOT NULL
                                                      AND rqd.promotion_id IN (SELECT *
                                                                                 FROM TABLE (
                                                                                         get_array_varchar (
                                                                                            '''||p_in_promotionId||''')))
                                                   OR (    '''||p_in_diyQuizId||'''
                                                              IS NOT NULL
                                                       AND rqd.quiz_id IN (SELECT *
                                                                             FROM TABLE (
                                                                                     get_array_varchar (
                                                                                        '''||p_in_diyQuizId||'''))))))
                                          AND p.promotion_status =
                                                 NVL ('''||p_in_promotionStatus||''',
                                                      p.promotion_status)
                                          AND (   ('''||p_in_departments||''' IS NULL)
                                               OR rqd.department IN (SELECT *
                                                                             FROM TABLE (
                                                                                     get_array_varchar (
                                                                                        '''||p_in_departments||'''))))
                                          AND NVL (rqd.job_title, ''job'') =
                                                 NVL (
                                                    '''||p_in_jobPosition||''',
                                                    NVL (rqd.job_title, ''job''))
                                          AND rqd.participant_status =
                                                 NVL ('''||p_in_participantStatus||''',
                                                      rqd.participant_status)
                                          AND NVL (TRUNC (rqd.quiz_date),
                                                   TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                               '''||p_in_fromDate||''',
                                                                               '''||p_in_localeDatePattern||''')
                                                                        AND TO_DATE (
                                                                               '''||p_in_toDate||''',
                                                                               '''||p_in_localeDatePattern||''')
                                 GROUP BY rqd.node_id) dtl
                          WHERE     rh.node_id = dtl.node_id(+)                                
                                AND rh.is_deleted = 0
                                AND NVL (rh.node_id, 0) IN (SELECT *
                                                              FROM TABLE (
                                                                      get_array_varchar (
                                                                         NVL (
                                                                            '''||p_in_parentNodeId||''',
                                                                            0))))
                         ORDER BY '|| v_sortCol ||'
    ) RS) WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd;                         

 OPEN p_out_data FOR 
   l_query;
      /* getQuizActivitySummaryResultsSize */

      SELECT COUNT (1) INTO p_out_size_data
           FROM (  SELECT dtl.node_name AS Org_Name,
                          dtl.node_id AS Node_Id
                     FROM rpt_hierarchy ras,--replaced rpt_quiz_activity_summary with rpt_hierarchy --01/04/2016
                          (  SELECT h.node_id,
                                    h.parent_node_id,
                                    h.node_name,
                                    SUM (dtl.quiz_completed) quiz_completed,
                                    SUM (dtl.quiz_passed) quiz_passed,
                                    SUM (dtl.quiz_failed) quiz_failed,
                                    SUM (dtl.amount) amount,
                                    SUM (dtl.sweepstakes_won) sweepstakes_won                                   
                               FROM (SELECT child_node_id node_id,
                                            node_id path_node_id
                                       FROM rpt_hierarchy_rollup) npn,
                                    rpt_hierarchy h,                                    
                                    (  SELECT SUM (
                                                 DECODE (rqd.quiz_result,
                                                         'progress', 0,   --04/05/2016 Bug 66316 --04/13/2016
                                                         1))
                                                 quiz_completed,
                                              SUM (
                                                 DECODE (rqd.quiz_result,
                                                         'passed', 1,
                                                         0))
                                                 quiz_passed,
                                              SUM (
                                                 DECODE (rqd.quiz_result,
                                                         'failed', 1,
                                                         0))
                                                 quiz_failed,
                                              SUM (rqd.award_amount) amount,
                                              rqd.node_id,
                                              SUM (rqd.sweepstakes_won)
                                                 sweepstakes_won
                                         FROM rpt_quiz_activity_detail rqd,
                                              promotion p
                                        WHERE     rqd.quiz_claim_id IS NOT NULL
                                              AND rqd.promotion_id = p.promotion_id
                                              AND (   (    p_in_promotionId IS NULL
                                                       AND p_in_diyQuizId IS NULL)
                                                   OR (       p_in_promotionId
                                                                 IS NOT NULL
                                                          AND rqd.promotion_id IN (SELECT *
                                                                                     FROM TABLE (
                                                                                             get_array_varchar (
                                                                                                p_in_promotionId)))
                                                       OR (    p_in_diyQuizId
                                                                  IS NOT NULL
                                                           AND rqd.quiz_id IN (SELECT *
                                                                                 FROM TABLE (
                                                                                         get_array_varchar (
                                                                                            p_in_diyQuizId))))))
                                              AND p.promotion_status =
                                                     NVL (p_in_promotionStatus,
                                                          p.promotion_status)
                                              AND (   (p_in_departments IS NULL)
                                                   OR rqd.department IN (SELECT *
                                                                                 FROM TABLE (
                                                                                         get_array_varchar (
                                                                                            p_in_departments))))
                                              AND NVL (rqd.job_title, 'job') =
                                                     NVL (
                                                        p_in_jobPosition,
                                                        NVL (rqd.job_title, 'job'))
                                              AND rqd.participant_status =
                                                     NVL (p_in_participantStatus,
                                                          rqd.participant_status)
                                              AND NVL (TRUNC (rqd.quiz_date),
                                                       TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                   p_in_fromDate,
                                                                                   p_in_localeDatePattern)
                                                                            AND TO_DATE (
                                                                                   p_in_toDate,
                                                                                   p_in_localeDatePattern)
                                     GROUP BY rqd.node_id) dtl
                              WHERE   dtl.node_id(+) = npn.node_id
                                    AND npn.path_node_id = h.node_id
                           GROUP BY h.node_id, h.node_name, h.parent_node_id)
                          dtl
                    WHERE     ras.node_id = dtl.node_id(+) --01/04/2016
                          AND NVL (ras.parent_node_id, 0) IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           NVL (
                                                                              p_in_parentNodeId,
                                                                              0))))
--                          AND ras.node_name NOT LIKE '%Team%'--01/04/2016
                 GROUP BY dtl.node_name,
                          dtl.node_id,                         
                          dtl.quiz_completed,
                          dtl.quiz_failed,
                          dtl.quiz_passed,
                          dtl.amount,
                          dtl.sweepstakes_won
                 UNION
                 SELECT rh.node_name || ' Team' AS Org_Name,
                        rh.node_id AS Node_Id
                   FROM rpt_hierarchy rh,                        
                        (  SELECT SUM (
                                     DECODE (rqd.quiz_result, 'progress', 0, 1))  --04/05/2016 Bug 66316 --04/13/2016
                                     quiz_completed,
                                  SUM (
                                     DECODE (rqd.quiz_result, 'passed', 1, 0))
                                     quiz_passed,
                                  SUM (
                                     DECODE (rqd.quiz_result, 'failed', 1, 0))
                                     quiz_failed,
                                  SUM (rqd.award_amount) amount,
                                  rqd.node_id,
                                  SUM (rqd.sweepstakes_won) sweepstakes_won
                             FROM rpt_quiz_activity_detail rqd, promotion p
                            WHERE     rqd.quiz_claim_id IS NOT NULL
                                  AND rqd.promotion_id = p.promotion_id
                                  AND (   (    p_in_promotionId IS NULL
                                           AND p_in_diyQuizId IS NULL)
                                       OR (       p_in_promotionId IS NOT NULL
                                              AND rqd.promotion_id IN (SELECT *
                                                                         FROM TABLE (
                                                                                 get_array_varchar (
                                                                                    p_in_promotionId)))
                                           OR (    p_in_diyQuizId IS NOT NULL
                                               AND rqd.quiz_id IN (SELECT *
                                                                     FROM TABLE (
                                                                             get_array_varchar (
                                                                                p_in_diyQuizId))))))
                                  AND p.promotion_status =
                                         NVL (p_in_promotionStatus,
                                              p.promotion_status)
                                  AND (   (p_in_departments IS NULL)
                                       OR rqd.department IN (SELECT *
                                                                     FROM TABLE (
                                                                             get_array_varchar (
                                                                                p_in_departments))))
                                  AND NVL (rqd.job_title, 'job') =
                                         NVL (p_in_jobPosition,
                                              NVL (rqd.job_title, 'job'))
                                  AND rqd.participant_status =
                                         NVL (p_in_participantStatus,
                                              rqd.participant_status)
                                  AND NVL (TRUNC (rqd.quiz_date),
                                           TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                       p_in_fromDate,
                                                                       p_in_localeDatePattern)
                                                                AND TO_DATE (
                                                                       p_in_toDate,
                                                                       p_in_localeDatePattern)
                         GROUP BY rqd.node_id) dtl
                  WHERE     rh.node_id = dtl.node_id(+)                        
                        AND rh.is_deleted = 0
                        AND NVL (rh.node_id, 0) IN (SELECT *
                                                      FROM TABLE (
                                                              get_array_varchar (
                                                                 NVL (
                                                                    p_in_parentNodeId,
                                                                    0))))
                 ORDER BY 1 ASC) rs;

      /* getQuizActivitySummaryResultsTotals */

      OPEN p_out_totals_data FOR
         SELECT NVL (SUM (Eligible_Quizzes), 0) AS Eligible_Quizzes,
                NVL (SUM (Quiz_Attempts), 0) AS Quiz_Attempts, 
                NVL (SUM (Attempts_In_Progress), 0) AS Attempts_In_Progress,
                NVL (SUM (Attempts_Failed), 0) AS Attempts_Failed,
                NVL (SUM (Attempts_Passed), 0) AS Attempts_Passed,
                ROUND (
                   DECODE (
                      SUM (Quiz_Attempts),
                      0, 0,
                      (SUM (Attempts_Passed) / SUM (Quiz_Attempts) * 100)),
                   2)
                   AS Eligible_Passed_Pct,
                ROUND (
                   DECODE (
                      SUM (Quiz_Attempts),
                      0, 0,
                      (SUM (Attempts_Failed) / SUM (Quiz_Attempts) * 100)),
                   2)
                   AS Eligible_Failed_Pct,
                NVL (SUM (Points), 0) AS Points,
                NVL (SUM (sweepstakes_won_cnt), 0) AS sweepstakes_won_cnt
           FROM (  SELECT dtl.node_name AS Org_Name,
                          (SELECT eligible_count FROM gtt_recog_elg_count WHERE record_type = 'nodesum' AND node_id = dtl.node_id) Eligible_Quizzes,
                          NVL (dtl.Quiz_Attempt, 0) AS Quiz_Attempts,--10/21/2014 Bug 57501
                          NVL (
                               dtl.Quiz_Attempt--10/20/2014 Bug 57499
                             - (dtl.quiz_passed + dtl.quiz_failed),
                             0)
                             AS Attempts_In_Progress,
                          NVL (dtl.quiz_failed, 0) AS Attempts_Failed,
                          NVL (dtl.quiz_passed, 0) AS Attempts_Passed,
                          DECODE (
                             NVL (dtl.quiz_completed, 0),
                             0, 0,
                             ROUND (
                                  (  NVL (dtl.quiz_passed, 0)
                                   / dtl.quiz_completed)
                                * 100,
                                2))
                             AS Eligible_Passed_Pct,
                          DECODE (
                             NVL (dtl.quiz_completed, 0),
                             0, 0,
                             ROUND (
                                  (  NVL (dtl.quiz_failed, 0)
                                   / dtl.quiz_completed)
                                * 100,
                                2))
                             AS Eligible_Failed_Pct,
                          NVL (dtl.amount, 0) AS Points,
                          NVL (dtl.sweepstakes_won, 0) AS sweepstakes_won_cnt,
                          dtl.node_id AS Node_Id
                     FROM rpt_hierarchy ras, --replaced rpt_quiz_activity_summary with rpt_hierarchy --01/04/2016
                          (  SELECT h.node_id,
                                    h.parent_node_id,
                                    h.node_name,
                                    SUM (dtl.Quiz_Attempt) Quiz_Attempt,--10/20/2014 Bug 57499
                                    SUM (dtl.quiz_completed) quiz_completed,
                                    SUM (dtl.quiz_passed) quiz_passed,
                                    SUM (dtl.quiz_failed) quiz_failed,
                                    SUM (dtl.amount) amount,
                                    SUM (dtl.sweepstakes_won) sweepstakes_won
                               FROM (SELECT child_node_id node_id,
                                            node_id path_node_id
                                       FROM rpt_hierarchy_rollup) npn,
                                    rpt_hierarchy h,                                    
--                                    (  SELECT COUNT (rqd.quiz_result)
                                     (  SELECT COUNT (rqd.quiz_result) Quiz_Attempt, --10/20/2014 Bug 57499
                                             SUM (                         --56380 bug fix start    09/10/2014                
                                                 DECODE (rqd.quiz_result,
                                                         'progress', 0,          --04/05/2016 Bug 66316 --04/13/2016
                                                         1))                         --56380 bug fix end
                                                 quiz_completed,
                                              SUM (
                                                 DECODE (rqd.quiz_result,
                                                         'passed', 1,
                                                         0))
                                                 quiz_passed,
                                              SUM (
                                                 DECODE (rqd.quiz_result,
                                                         'failed', 1,
                                                         0))
                                                 quiz_failed,
                                              SUM (rqd.award_amount) amount,
                                              rqd.node_id,
                                              SUM (rqd.sweepstakes_won)
                                                 sweepstakes_won
                                         FROM rpt_quiz_activity_detail rqd,
                                              promotion p
                                        WHERE     rqd.quiz_claim_id IS NOT NULL
                                              AND rqd.promotion_id = p.promotion_id
                                              AND (   (    p_in_promotionId IS NULL
                                                       AND p_in_diyQuizId IS NULL)
                                                   OR (       p_in_promotionId
                                                                 IS NOT NULL
                                                          AND rqd.promotion_id IN (SELECT *
                                                                                     FROM TABLE (
                                                                                             get_array_varchar (
                                                                                                p_in_promotionId)))
                                                       OR (    p_in_diyQuizId
                                                                  IS NOT NULL
                                                           AND rqd.quiz_id IN (SELECT *
                                                                                 FROM TABLE (
                                                                                         get_array_varchar (
                                                                                            p_in_diyQuizId))))))
                                              AND p.promotion_status =
                                                     NVL (p_in_promotionStatus,
                                                          p.promotion_status)
                                              AND (   (p_in_departments IS NULL)
                                                   OR rqd.department IN (SELECT *
                                                                                 FROM TABLE (
                                                                                         get_array_varchar (
                                                                                            p_in_departments))))
                                              AND NVL (rqd.job_title, 'job') =
                                                     NVL (
                                                        p_in_jobPosition,
                                                        NVL (rqd.job_title, 'job'))
                                              AND rqd.participant_status =
                                                     NVL (p_in_participantStatus,
                                                          rqd.participant_status)
                                              AND NVL (TRUNC (rqd.quiz_date),
                                                       TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                   p_in_fromDate,
                                                                                   p_in_localeDatePattern)
                                                                            AND TO_DATE (
                                                                                   p_in_toDate,
                                                                                   p_in_localeDatePattern)
                                     GROUP BY rqd.node_id) dtl
                              WHERE  dtl.node_id(+) = npn.node_id
                                    AND npn.path_node_id = h.node_id
                           GROUP BY h.node_id, h.node_name, h.parent_node_id)
                          dtl
                    WHERE     ras.node_id = dtl.node_id(+)--01/04/2016
                          AND NVL (ras.parent_node_id, 0) IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           NVL (
                                                                              p_in_parentNodeId,
                                                                              0))))
--                          AND ras.node_name NOT LIKE '%Team%'--01/04/2016
                 GROUP BY dtl.Quiz_Attempt, --10/20/2014 Bug 57499
                          dtl.node_name,
                          dtl.node_id,                          
                          dtl.quiz_completed,
                          dtl.quiz_failed,
                          dtl.quiz_passed,
                          dtl.amount,
                          dtl.sweepstakes_won
                 UNION
                 SELECT rh.node_name || ' Team' AS Org_Name,
                        (SELECT eligible_count FROM gtt_recog_elg_count WHERE record_type = 'teamsum' AND node_id = rh.node_id) Eligible_Quizzes,  --10/21/2014
                        NVL (dtl.Quiz_Attempt, 0) AS Quiz_Attempts,--10/21/2014 Bug 57501
                        NVL (
                             dtl.Quiz_Attempt--10/20/2014 Bug 57499
                           - (dtl.quiz_passed + dtl.quiz_failed),
                           0)
                           AS Attempts_In_Progress,
                        NVL (dtl.quiz_failed, 0) AS Attempts_Failed,
                        NVL (dtl.quiz_passed, 0) AS Attempts_Passed,
                        DECODE (
                           NVL (dtl.quiz_completed, 0),
                           0, 0,
                           ROUND (
                                (  NVL (dtl.quiz_passed, 0)
                                 / dtl.quiz_completed)
                              * 100,
                              2))
                           AS Eligible_Passed_Pct,
                        DECODE (
                           NVL (dtl.quiz_completed, 0),
                           0, 0,
                           ROUND (
                                (  NVL (dtl.quiz_failed, 0)
                                 / dtl.quiz_completed)
                              * 100,
                              2))
                           AS Eligible_Failed_Pct,
                        NVL (dtl.amount, 0) AS Points,
                        NVL (dtl.sweepstakes_won, 0) AS sweepstakes_won_cnt,
                        rh.node_id AS Node_Id
                   FROM rpt_hierarchy rh,                        
--                        (  SELECT COUNT (rqd.quiz_result) 
                          (  SELECT COUNT (rqd.quiz_result) Quiz_Attempt, --10/20/2014 Bug 57499
                                SUM (                         --56380 bug fix start    09/10/2014                
                               DECODE (rqd.quiz_result,
                                       'progress', 0,         --04/05/2016 Bug 66316  --04/13/2016  
                                      1))                         --56380 bug fix end
                                      quiz_completed,
                                  SUM (
                                     DECODE (rqd.quiz_result, 'passed', 1, 0))
                                     quiz_passed,
                                  SUM (
                                     DECODE (rqd.quiz_result, 'failed', 1, 0))
                                     quiz_failed,
                                  SUM (rqd.award_amount) amount,
                                  rqd.node_id,
                                  SUM (rqd.sweepstakes_won) sweepstakes_won
                             FROM rpt_quiz_activity_detail rqd, promotion p
                            WHERE     rqd.quiz_claim_id IS NOT NULL
                                  AND rqd.promotion_id = p.promotion_id
                                  AND (   (    p_in_promotionId IS NULL
                                           AND p_in_diyQuizId IS NULL)
                                       OR (       p_in_promotionId IS NOT NULL
                                              AND rqd.promotion_id IN (SELECT *
                                                                         FROM TABLE (
                                                                                 get_array_varchar (
                                                                                    p_in_promotionId)))
                                           OR (    p_in_diyQuizId IS NOT NULL
                                               AND rqd.quiz_id IN (SELECT *
                                                                     FROM TABLE (
                                                                             get_array_varchar (
                                                                                p_in_diyQuizId))))))
                                  AND p.promotion_status =
                                         NVL (p_in_promotionStatus,
                                              p.promotion_status)
                                  AND (   (p_in_departments IS NULL)
                                       OR rqd.department IN (SELECT *
                                                                     FROM TABLE (
                                                                             get_array_varchar (
                                                                                p_in_departments))))
                                  AND NVL (rqd.job_title, 'job') =
                                         NVL (p_in_jobPosition,
                                              NVL (rqd.job_title, 'job'))
                                  AND rqd.participant_status =
                                         NVL (p_in_participantStatus,
                                              rqd.participant_status)
                                  AND NVL (TRUNC (rqd.quiz_date),
                                           TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                       p_in_fromDate,
                                                                       p_in_localeDatePattern)
                                                                AND TO_DATE (
                                                                       p_in_toDate,
                                                                       p_in_localeDatePattern)
                         GROUP BY rqd.node_id) dtl
                  WHERE     rh.node_id = dtl.node_id(+)
                        AND rh.is_deleted = 0
                        AND NVL (rh.node_id, 0) IN (SELECT *
                                                      FROM TABLE (
                                                              get_array_varchar (
                                                                 NVL (
                                                                    p_in_parentNodeId,
                                                                    0))))
                 ORDER BY 1 ASC) rs;

      p_out_return_code := '00';
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;

         p_out_size_data := NULL;

         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;
   END prc_getQuizActivitySummary;

   /* getQuizActivityDetailOneResults */
   PROCEDURE prc_getQuizActivityDetailOne (
      p_in_departments         IN     VARCHAR,
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_result              IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_paxId               IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
   
         /******************************************************************************
  NAME:       prc_getQuizActivityDetailOne
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014      Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J              10/21/2014      Bug 57500 - Reports - Quiz Activity - Summary - Eligible Quizzes is not matching before and after drilldown
  Suriya N             12/23/2015      Bug Bug 65047 - Quiz Activity Report - Numbers for Eligible Quizzes are not correct on pax level detail page.    
  Ravi Dhanekula      12/31/2015       Bug # 65047 -- Have to use the new GTT table for user level eligibility counts.                              
  nagarajs            02/16/2016       Bug 65792 - Quiz Activity Report - DIY Quiz Promotion - Getting "parsererror: 200 SyntaxError: Invalid character" after drill down the Org Unit.
  Ravi Arumugam      04/05/2016       Bug 66316 -Quiz Activity - Quiz Results is Displaying as Progress when Pax didnot even Complete Taking Quiz 
  nagarajs          04/13/2016        Bug 66413, 66421 fixes
  Suresh J          11/08/2016        Bug 69953 - Summary table org unit Count is mismatch for eligible quizzes
  Sherif Basha      12/21/2016        Bug 70600 - Quiz Activity Report - "Failed" Status, Drilling Down at bottom level Org Records are not displaying.  
                                                    p_in_result input ('passed','failed','Inprogress') is an export selection filter in the report so commenting it
  ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getQuizActivityDetailOne' ;
  v_stage        VARCHAR2 (500);
  v_sortCol             VARCHAR2(200);
  l_query VARCHAR2(32767);    
  v_non_diy_quiz_cnt NUMBER(5);--12/31/2015
   /* prc_getQuizActivityDetailOne */

   BEGIN
   
   DELETE gtt_recog_elg_pax_count; --12/31/2015 Start
   
  SELECT COUNT(DISTINCT promotion_type) INTO v_non_diy_quiz_cnt
    FROM 
    (SELECT p.promotion_type ,-1*q.quiz_id AS ID, 'DIY Promotion ' ||q.name AS value
        FROM quiz q,
             promotion p,
             diy_quiz dq
     WHERE promotion_status IN ( 'live', 'expired' )
            AND is_deleted          = 0
            AND q.quiz_id           =dq.quiz_id
            AND dq.promotion_id     = p.promotion_id
            AND p.promotion_type    ='diy_quiz'
    UNION ALL
    SELECT promotion_type,promotion_id AS id, promotion_name    AS value
        FROM Promotion
    WHERE   promotion_status IN ( 'live', 'expired' )
            AND is_deleted          = 0
            AND promotion_type      ='quiz'
     )
    WHERE promotion_type <> 'diy_quiz';
   
IF (p_in_diyQuizId IS NOT NULL AND p_in_promotionId IS NULL  OR (p_in_diyQuizId IS NULL AND v_non_diy_quiz_cnt = 0 )) THEN

INSERT INTO gtt_recog_elg_pax_count (eligible_count,node_id,participant_id)
SELECT eligible_cnt,node_id,participant_id
FROM (
SELECT COUNT(pe.diy_quiz_participant_id) eligible_cnt,
         un.node_id,pe.participant_id
  FROM   diy_quiz_participant pe,
         user_node un,
         vw_curr_pax_employer emp
  WHERE  pe.quiz_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_diyQuizId  ) ))
  AND    un.is_primary = 1
  AND    pe.participant_id = un.user_id
  AND    pe.participant_id = emp.user_id
  AND    un.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    emp.position_type =  NVL(p_in_jobposition,emp.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active'
  AND ((p_in_departments IS NULL)
      OR emp.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
  --GROUP BY un.user_id,un.node_id);        --02/16/2016
  GROUP BY pe.participant_id,un.node_id);   --02/16/2016
    
ELSE

INSERT INTO gtt_recog_elg_pax_count (eligible_count,node_id,participant_id)        
    SELECT eligible_cnt,node_id,participant_id FROM (
                                  SELECT COUNT(1)eligible_cnt,node_id,participant_id  FROM  (SELECT  ppe.participant_id,ppe.node_id  
                                                  FROM (SELECT*  
                                                          FROM rpt_pax_promo_elig_quiz 
                                                         WHERE giver_recvr_type = 'giver' AND node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) ))) ppe,  
                                                       promotion P,                                                        
                                                       rpt_participant_employer rad 
                                                 WHERE ppe.promotion_id = P.promotion_id  
                                                        AND ppe.participant_id = rad.user_id(+)
                                                       AND ( ( p_in_promotionId IS NULL ) OR P.promotion_id IN (
                                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))                                                      
                                                       AND P.promotion_status =  
                                                              NVL (p_in_promotionstatus,  
                                                                   P.promotion_status)  
                                               AND ((p_in_departments IS NULL) OR rad.department_type IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))
                                               AND NVL (rad.position_type, 'JOB') =  
                                                      NVL (  
                                                         p_in_jobposition,  
                                                         NVL (rad.position_type, 'JOB')) 
                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                      NVL (p_in_participantStatus,  
                                                           DECODE(rad.termination_date,NULL,'active','inactive'))  
                                                       AND P.promotion_type IN ('quiz','diy_quiz') 
                                                       UNION ALL
                                                       SELECT pe.participant_id,
         un.node_id
  FROM   diy_quiz_participant pe,
         user_node un,
         vw_curr_pax_employer emp
  WHERE  pe.quiz_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_diyQuizId  ) ))
  AND    un.is_primary = 1
  AND    pe.participant_id = un.user_id
  AND    pe.participant_id = emp.user_id
  AND    un.node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) )) 
  AND    emp.position_type =  NVL(p_in_jobposition,emp.position_type)
  AND nvl(p_in_participantStatus,'active') = 'active' -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
  AND ((p_in_departments IS NULL)
      OR emp.department_type IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))
                                              GROUP BY node_id,participant_id);
END IF;

   v_stage := 'getQuizActivityDetailOne';
     
l_query  := 'SELECT * FROM
  ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;
       
  l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                     FROM (  SELECT    rad.participant_last_name
                                    || '', ''
                                    || rad.participant_first_name
                                       participant_name,
                                    fnc_cms_asset_code_val_extr (
                                       c.cm_asset_code,
                                       c.name_cm_key,
                                       '''||p_in_languageCode||''')
                                       AS COUNTRY,
--                                  (SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT ( ppe.participant_id)     --10/21/2014--12/31/2015--Commented out this code
--                                                          eligible_cnt
--                                                  FROM (SELECT *  
--                                                          FROM rpt_pax_promo_elig_quiz 
--                                                         WHERE giver_recvr_type = ''giver'' AND node_id IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||'''  ) ))) ppe,  
--                                                       promotion P,                                                        
--                                                       rpt_participant_employer rad 
--                                                 WHERE ppe.promotion_id = P.promotion_id  
--                                                        AND ppe.participant_id = rad.user_id(+)
--                                                       AND ( ('''|| p_in_promotionId||''' IS NULL ) OR P.promotion_id IN (
--                                                            SELECT * FROM TABLE ( get_array_varchar('''||p_in_promotionId||'''))))                                                      
--                                                       AND P.promotion_status =  
--                                                              NVL ('''||p_in_promotionstatus||''',  
--                                                                   P.promotion_status)  
--                                                       AND NVL (rad.position_type, ''JOB'') =     --12/23/2015
--                                                      NVL (  
--                                                         '''||p_in_jobPosition||''',  
--                                                         NVL (rad.position_type, ''JOB'')) 
--                                               AND DECODE(rad.termination_date,NULL,''active'',''inactive'') =      --12/23/2015
--                                                      NVL ('''||p_in_participantStatus||''',  
--                                                           DECODE(rad.termination_date,NULL,''active'',''inactive''))
--                                                       AND P.promotion_type IN (''quiz'',''diy_quiz'')
--                                              )   
(select SUM(eligible_count) FROM gtt_recog_elg_pax_count WHERE node_id = rad.node_id)  --12/31/2015  --11/08/2016
                                       eligible_quizzes_cnt,
                                    COUNT (rad.quiz_result) quiz_attempts_cnt,
                                    SUM (
                                       DECODE (rad.quiz_result, ''progress'', 1, 0))   --04/05/2016 Bug 66316 --04/13/2016
                                       attempts_in_progress_cnt, 
                                    SUM (
                                       DECODE (rad.quiz_result, ''failed'', 1, 0))
                                       attempts_failed_cnt,
                                    SUM (
                                       DECODE (rad.quiz_result, ''passed'', 1, 0))
                                       attempts_passed_cnt,
                                    SUM (rad.award_amount) points_cnt,
                                    SUM (rad.sweepstakes_won) sweepstakes_won_cnt,
                                    rad.participant_id participant_id,
                                    rad.node_id
                               FROM rpt_quiz_activity_detail rad,
                                    promotion p,
                                    promo_quiz pq,
                                    country c
                              WHERE     rad.quiz_claim_id IS NOT NULL
                                    AND rad.promotion_id = p.promotion_id
                                    AND p.promotion_id = pq.promotion_id
                                    AND rad.country_id = c.country_id
                                    AND rad.node_id IN (SELECT *
                                                          FROM TABLE (
                                                                  get_array_varchar (
                                                                     NVL (
                                                                        '''||p_in_parentNodeId||''',
                                                                        rad.node_id))))
                                    AND (   (    '''||p_in_promotionId||''' IS NULL
                                             AND '''||p_in_diyQuizId||''' IS NULL)
                                         OR (       '''||p_in_promotionId||''' IS NOT NULL
                                                AND rad.promotion_id IN (SELECT *
                                                                           FROM TABLE (
                                                                                   get_array_varchar (
                                                                                      '''||p_in_promotionId||''')))
                                             OR (    '''||p_in_diyQuizId||''' IS NOT NULL
                                                 AND rad.quiz_id IN (SELECT *
                                                                       FROM TABLE (
                                                                               get_array_varchar (
                                                                                  '''||p_in_diyQuizId||'''))))))
                                    AND p.promotion_status =
                                           NVL ('''||p_in_promotionStatus||''',
                                                p.promotion_status)
                                    AND (   rad.department IN (SELECT *
                                                                 FROM TABLE (
                                                                         get_array_varchar (
                                                                            '''||p_in_departments||''')))
                                         OR ('''||p_in_departments||''' IS NULL))
                                    AND NVL (rad.job_title, ''job'') =
                                           NVL ('''||p_in_jobPosition||''',
                                                NVL (rad.job_title, ''job''))
                                    AND rad.participant_status =
                                           NVL ('''||p_in_participantStatus||''',
                                                rad.participant_status)
                      /*              AND NVL (LOWER (rad.quiz_result), ''sweeps'') =  -- 12/21/2016 Bug 70600 commented the filter
                                           NVL (
                                              LOWER ('''||p_in_result||'''),
                                              NVL (LOWER (rad.quiz_result),
                                                   ''sweeps'')) */                     -- 12/21/2016 Bug 70600 commented the filter
                                    AND NVL (TRUNC (rad.quiz_date),
                                             TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                         '''||p_in_fromDate||''',
                                                                         '''||p_in_localeDatePattern||''')
                                                                  AND TO_DATE (
                                                                         '''||p_in_toDate||''',
                                                                         '''||p_in_localeDatePattern||''')
                           GROUP BY rad.participant_last_name,
                                    rad.participant_first_name,
                                    rad.participant_id,  
                                    rad.node_id,                                  
                                    c.cm_asset_code,
                                    c.name_cm_key) rs
                 ORDER BY '|| v_sortCol ||'
    ) WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd;    
    
--delete from test_lquery;
--insert into test_lquery values (l_query);
                                 
 OPEN p_out_data FOR 
   l_query;
      /* getQuizActivityDetailOneResultsSize */

      SELECT COUNT (1) INTO p_out_size_data
           FROM (  SELECT    rad.participant_last_name
                          || ', '
                          || rad.participant_first_name
                             participant_name,
                          fnc_cms_asset_code_val_extr (c.cm_asset_code,
                                                       c.name_cm_key,
                                                       p_in_languageCode)
                             AS COUNTRY,
                          COUNT (rad.rpt_quiz_activity_detail_id)
                             quiz_attempts_cnt,
                          SUM (DECODE (rad.quiz_result, NULL, 1, 0))
                             attempts_in_progress_cnt,
                          SUM (DECODE (rad.quiz_result, 'failed', 1, 0))
                             attempts_failed_cnt,
                          SUM (DECODE (rad.quiz_result, 'passed', 1, 0))
                             attempts_passed_cnt,
                          SUM (rad.award_amount) points_cnt,
                          SUM (rad.sweepstakes_won) sweepstakes_won_cnt,
                          rad.participant_id participant_id
                     FROM rpt_quiz_activity_detail rad,
                          promotion p,
                          promo_quiz pq,
                          country c
                    WHERE     rad.quiz_claim_id IS NOT NULL
                          AND rad.promotion_id = p.promotion_id
                          AND p.promotion_id = pq.promotion_id
                          AND rad.country_id = c.country_id
                          AND rad.node_id IN (SELECT *
                                                FROM TABLE (
                                                        get_array_varchar (
                                                           NVL (
                                                              p_in_parentNodeId,
                                                              rad.node_id))))
                          AND (   (    p_in_promotionId IS NULL
                                   AND p_in_diyQuizId IS NULL)
                               OR (       p_in_promotionId IS NOT NULL
                                      AND rad.promotion_id IN (SELECT *
                                                                 FROM TABLE (
                                                                         get_array_varchar (
                                                                            p_in_promotionId)))
                                   OR (    p_in_diyQuizId IS NOT NULL
                                       AND rad.quiz_id IN (SELECT *
                                                             FROM TABLE (
                                                                     get_array_varchar (
                                                                        p_in_diyQuizId))))))
                          AND p.promotion_status =
                                 NVL (p_in_promotionStatus, p.promotion_status)
                          AND (   (p_in_departments IS NULL)
                               OR rad.department IN (SELECT *
                                                             FROM TABLE (
                                                                     get_array_varchar (
                                                                        p_in_departments))))
                          AND rad.job_title =
                                 NVL (p_in_jobPosition, rad.job_title)
                          AND rad.participant_status =
                                 NVL (p_in_participantStatus,
                                      rad.participant_status)
                    /*      AND LOWER (rad.quiz_result) =                       -- 12/21/2016 Bug 70600 commented the filter
                                 NVL (LOWER (p_in_result), rad.quiz_result)         */     -- 12/21/2016 Bug 70600 commented the filter
                          AND NVL (TRUNC (rad.quiz_date), TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                      p_in_fromDate,
                                                                                      p_in_localeDatePattern)
                                                                               AND TO_DATE (
                                                                                      p_in_toDate,
                                                                                      p_in_localeDatePattern)
                 GROUP BY rad.participant_last_name,
                          rad.participant_first_name,
                          rad.participant_id,
                          fnc_cms_asset_code_val_extr (c.cm_asset_code,
                                                       c.name_cm_key,
                                                       p_in_languageCode));

      /* getQuizActivityDetailOneResultsTotals */
      OPEN p_out_totals_data FOR       
                --SELECT *     --11/08/2016
                SELECT 
                       NVL((select SUM(eligible_count) FROM gtt_recog_elg_pax_count),0) AS   eligible_quizzes_cnt,
                       quiz_attempts_cnt,
                       quiz_attempts_in_progress_cnt,
                       attempts_failed_cnt,
                       attempts_passed_cnt,   
                       points_cnt,   
                       sweepstakes_won_cnt 
                From
--                                        (SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT ( ppe.participant_id)      --10/21/2014--12/31/2015 Commented out this code
--                                                                           eligible_quizzes_cnt
--                                                                  FROM (SELECT *  
--                                                                          FROM rpt_pax_promo_elig_quiz 
--                                                                         WHERE giver_recvr_type = 'giver' AND node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId  ) ))) ppe,  
--                                                                       promotion P,                                                        
--                                                                       rpt_participant_employer rad 
--                                                                 WHERE ppe.promotion_id = P.promotion_id  
--                                                                        AND ppe.participant_id = rad.user_id(+)
--                                                                       AND ( ( p_in_promotionId IS NULL ) OR P.promotion_id IN (
--                                                                            SELECT * FROM TABLE ( get_array_varchar(p_in_promotionId))))                                                      
--                                                                       AND P.promotion_status =  
--                                                                              NVL (p_in_promotionstatus,  
--                                                                                   P.promotion_status)  
--                                                                       AND NVL (rad.position_type, 'JOB') =     --12/23/2015
--                                                                       NVL (  
--                                                                             p_in_jobPosition,  
--                                                                             NVL (rad.position_type, 'JOB')) 
--                                                                        AND DECODE(rad.termination_date,NULL,'active','inactive') =    --12/23/2015  
--                                                                       NVL (p_in_participantStatus,  
--                                                                           DECODE(rad.termination_date,NULL,'active','inactive'))
--                                                                       AND P.promotion_type IN ('quiz','diy_quiz')
--                --                                                       AND ppe.participant_id =  rad.participant_id                                                         
--                                                              )   t1,
                         (SELECT 
                               --NVL (SUM (eligible_quizzes_cnt), 0) AS eligible_quizzes_cnt,         --10/21/2014--12/31/2015  --11/08/2016
                                NVL (SUM (quiz_attempts_cnt), 0) AS quiz_attempts_cnt,
                                NVL (SUM (attempts_in_progress_cnt), 0)
                                   AS quiz_attempts_in_progress_cnt,
                                NVL (SUM (attempts_failed_cnt), 0) AS attempts_failed_cnt,
                                NVL (SUM (attempts_passed_cnt), 0) AS attempts_passed_cnt,
                                NVL (SUM (points_cnt), 0) AS points_cnt,
                                NVL (SUM (sweepstakes_won_cnt), 0) AS sweepstakes_won_cnt
                           FROM (  SELECT    rad.participant_last_name
                                          || ', '
                                          || rad.participant_first_name
                                             participant_name,
                                          fnc_cms_asset_code_val_extr (c.cm_asset_code,
                                                                       c.name_cm_key,
                                                                       p_in_languageCode)
                                             AS COUNTRY,
                --                          (SELECT COUNT (PPE.PARTICIPANT_ID)      --10/21/2014
                --                             FROM rpt_pax_promo_elig_quiz PPE, PROMOTION P
                --                            WHERE     P.PROMOTION_TYPE IN ('quiz', 'diy_quiz')
                --                                  AND (   (    p_in_promotionId IS NULL
                --                                           AND p_in_diyQuizId IS NULL)
                --                                       OR (       p_in_promotionId IS NOT NULL
                --                                              AND ppe.promotion_id IN (SELECT *
                --                                                                         FROM TABLE (
                --                                                                                 get_array_varchar (
                --                                                                                    p_in_promotionId)))
                --                                           OR (    p_in_diyQuizId IS NOT NULL
                --                                               AND ppe.promotion_id IN (SELECT promotion_id
                --                                                                          FROM promotion
                --                                                                         WHERE     promotion_type =
                --                                                                                      'diy_quiz'
                --                                                                               AND promotion_status =
                --                                                                                      NVL (
                --                                                                                         p_in_promotionStatus,
                --                                                                                         promotion_status)))))
                --                                  AND p.promotion_id = ppe.promotion_id
                --                                  AND participant_id = rad.participant_id) as eligible_quizzes_cnt,
                                            --(select eligible_count FROM gtt_recog_elg_pax_count WHERE participant_id = rad.participant_id)    as eligible_quizzes_cnt,--12/31/2015  --11/08/2016
                                          COUNT (rad.quiz_result) quiz_attempts_cnt,
                                          SUM (DECODE (rad.quiz_result, 'progress', 1, 0))  --04/05/2016 Bug 66316 --04/13/2016
                                             attempts_in_progress_cnt,
                                          SUM (DECODE (rad.quiz_result, 'failed', 1, 0))
                                             attempts_failed_cnt,
                                          SUM (DECODE (rad.quiz_result, 'passed', 1, 0))
                                             attempts_passed_cnt,
                                          SUM (rad.award_amount) points_cnt,
                                          SUM (rad.sweepstakes_won) sweepstakes_won_cnt,
                                          rad.participant_id participant_id
                                     FROM rpt_quiz_activity_detail rad,
                                          promotion p,
                                          promo_quiz pq,
                                          country c
                                    WHERE     rad.quiz_claim_id IS NOT NULL
                                          AND rad.promotion_id = p.promotion_id
                                          AND p.promotion_id = pq.promotion_id
                                          AND rad.country_id = c.country_id
                                          AND rad.node_id IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           NVL (
                                                                              p_in_parentNodeId,
                                                                              rad.node_id))))
                                          AND (   (    p_in_promotionId IS NULL
                                                   AND p_in_diyQuizId IS NULL)
                                               OR (       p_in_promotionId IS NOT NULL
                                                      AND rad.promotion_id IN (SELECT *
                                                                                 FROM TABLE (
                                                                                         get_array_varchar (
                                                                                            p_in_promotionId)))
                                                   OR (    p_in_diyQuizId IS NOT NULL
                                                       AND rad.quiz_id IN (SELECT *
                                                                             FROM TABLE (
                                                                                     get_array_varchar (
                                                                                        p_in_diyQuizId))))))
                                          AND p.promotion_status =
                                                 NVL (p_in_promotionStatus, p.promotion_status)
                                          AND (   rad.department IN (SELECT *
                                                                       FROM TABLE (
                                                                               get_array_varchar (
                                                                                  p_in_departments)))
                                               OR (p_in_departments IS NULL))
                                          AND NVL (rad.job_title, 'job') =
                                                 NVL (p_in_jobPosition,
                                                      NVL (rad.job_title, 'job'))
                                          AND rad.participant_status =
                                                 NVL (p_in_participantStatus,
                                                      rad.participant_status)
                              /*            AND NVL (LOWER (rad.quiz_result), 'sweeps') =        -- 12/21/2016 Bug 70600 commented the filter
                                                 NVL (LOWER (p_in_result),
                                                      NVL (LOWER (rad.quiz_result), 'sweeps'))      */     -- 12/21/2016 Bug 70600 commented the filter
                                          AND NVL (TRUNC (rad.quiz_date), TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                                      p_in_fromDate,
                                                                                                      p_in_localeDatePattern)
                                                                                               AND TO_DATE (
                                                                                                      p_in_toDate,
                                                                                                      p_in_localeDatePattern)
                                 GROUP BY rad.participant_last_name,
                                          rad.participant_first_name,
                                          rad.participant_id,
                                          fnc_cms_asset_code_val_extr (c.cm_asset_code,
                                                                       c.name_cm_key,
                                                                       p_in_languageCode)) rs) t2 ;

      p_out_return_code := '00';
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;

         p_out_size_data := NULL;

         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;
   END prc_getQuizActivityDetailOne;

   PROCEDURE prc_getQuizActivityDetailTwo (
      p_in_departments         IN     VARCHAR,
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_result              IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_paxId               IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
   
   /******************************************************************************
  NAME:       prc_getQuizActivityDetailTwo
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  nagarajs              04/13/2016  Bug 66413, 66421 fixes    
  ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getQuizActivityDetailTwo' ;
  v_stage        VARCHAR2 (500);
  v_sortCol             VARCHAR2(200);
  l_query VARCHAR2(32767);     
   
   /* getQuizActivityDetailTwo */

   BEGIN

   v_stage := 'getQuizActivityDetailTwo';
      
       l_query  := 'SELECT * FROM
  ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;
       
  l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                     FROM (SELECT rad.quiz_date q_date,
                                     rad.participant_last_name
                                  || '', ''
                                  || rad.participant_first_name
                                     completed_by,
                                  rad.node_name org_name,
                                  rad.promo_quiz_name promotion,
                                     rad.quiz_score
                                  || '' of ''
                                  || rad.quiz_passing_score
                                  || '' (''
                                  || rad.quiz_passing_score
                                  || '')''
                                     score_passing,
                                  DECODE(rad.quiz_result,''progress'',''In Progress'',INITCAP (rad.quiz_result) ) results, --04/13/2016
                                  NVL (rad.award_amount, 0) points_cnt,
                                  NVL (rad.sweepstakes_won, 0)
                                     sweepstakes_won_cnt,
                                  CASE
                                     WHEN     pq.INCLUDE_PASS_QUIZ_CERTIFICATE =
                                                 1
                                          AND INITCAP (rad.quiz_result) =
                                                 ''Passed''
                                     THEN
                                        1
                                     ELSE
                                        0
                                  END
                                     certificate,
                                  quiz_claim_id claim_id,
                                  participant_id participant_id
                             FROM rpt_quiz_activity_detail rad,
                                  promotion p,
                                  promo_quiz pq
                            WHERE     rad.quiz_claim_id IS NOT NULL
                                  AND rad.promotion_id = p.promotion_id
                                  AND rad.participant_id = '''||p_in_paxId||'''
                                  AND (   (    '''||p_in_promotionId||''' IS NULL
                                           AND '''||p_in_diyQuizId||''' IS NULL)
                                       OR (       '''||p_in_promotionId||''' IS NOT NULL
                                              AND rad.promotion_id IN (SELECT *
                                                                         FROM TABLE (
                                                                                 get_array_varchar (
                                                                                    '''||p_in_promotionId||''')))
                                           OR (    '''||p_in_diyQuizId||''' IS NOT NULL
                                               AND rad.quiz_id IN (SELECT *
                                                                     FROM TABLE (
                                                                             get_array_varchar (
                                                                                '''||p_in_diyQuizId||'''))))))
                                  AND (   ('''||p_in_departments||''' IS NULL)
                                       OR rad.department IN (SELECT *
                                                                     FROM TABLE (
                                                                             get_array_varchar (
                                                                                '''||p_in_departments||'''))))
                                  AND NVL(rad.job_title,''JOB'') =
                                         NVL ('''||p_in_jobPosition||''', NVL(rad.job_title,''JOB''))
                                  AND p.promotion_id = pq.promotion_id
                                  AND rad.participant_status =
                                         NVL ('''||p_in_participantStatus||''',
                                              rad.participant_status)
                                  AND NVL (TRUNC (rad.quiz_date),
                                           TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                       '''||p_in_fromDate||''',
                                                                       '''||p_in_localeDatePattern||''')
                                                                AND TO_DATE (
                                                                       '''||p_in_toDate||''',
                                                                       '''||p_in_localeDatePattern||'''))
                          rs
                 ORDER BY '|| v_sortCol ||'
    ) WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd; 


 OPEN p_out_data FOR 
   l_query;
      /* getQuizActivityDetailTwoResultsSize */

      SELECT COUNT (1) INTO p_out_size_data
           FROM (SELECT rad.quiz_date q_date,
                           rad.participant_last_name
                        || ', '
                        || rad.participant_first_name
                           completed_by,
                        rad.node_name org_name,
                        rad.promo_quiz_name promotion,
                           rad.quiz_score
                        || ' of '
                        || rad.quiz_passing_score
                        || ' ('
                        || rad.quiz_passing_score
                        || ')'
                           score_passing,
                        INITCAP (rad.quiz_result) results,
                        NVL (rad.award_amount, 0) points_cnt,
                        NVL (rad.sweepstakes_won, 0) sweepstakes_won_cnt,
                        0 certificate,
                        quiz_claim_id claim_id,
                        participant_id participant_id
                   FROM rpt_quiz_activity_detail rad, promotion p
                  WHERE     rad.quiz_claim_id IS NOT NULL
                        AND rad.promotion_id = p.promotion_id
                        AND rad.participant_id = p_in_paxId
                        AND (   (    p_in_promotionId IS NULL
                                 AND p_in_diyQuizId IS NULL)
                             OR (       p_in_promotionId IS NOT NULL
                                    AND rad.promotion_id IN (SELECT *
                                                               FROM TABLE (
                                                                       get_array_varchar (
                                                                          p_in_promotionId)))
                                 OR (    p_in_diyQuizId IS NOT NULL
                                     AND rad.quiz_id IN (SELECT *
                                                           FROM TABLE (
                                                                   get_array_varchar (
                                                                      p_in_diyQuizId))))))
                        AND (   (p_in_departments IS NULL)
                             OR rad.department IN (SELECT *
                                                           FROM TABLE (
                                                                   get_array_varchar (
                                                                      p_in_departments))))
                        AND rad.job_title =
                               NVL (p_in_jobPosition, rad.job_title)
                        AND rad.participant_status =
                               NVL (p_in_participantStatus,
                                    rad.participant_status)
                        AND NVL (TRUNC (rad.quiz_date), TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                    p_in_fromDate,
                                                                                    p_in_localeDatePattern)
                                                                             AND TO_DATE (
                                                                                    p_in_toDate,
                                                                                    p_in_localeDatePattern));

      /* getQuizActivityDetailTwoResultsTotals */
      OPEN p_out_totals_data FOR
         SELECT NVL (SUM (points_cnt), 0) AS points_cnt,
                NVL (SUM (sweepstakes_won_cnt), 0) AS sweepstakes_won_cnt,
                NVL (SUM (certificate), 0) AS certificate_cnt
           FROM (SELECT rad.quiz_date q_date,
                           rad.participant_last_name
                        || ', '
                        || rad.participant_first_name
                           completed_by,
                        rad.node_name org_name,
                        rad.promo_quiz_name promotion,
                           rad.quiz_score
                        || ' of '
                        || rad.quiz_passing_score
                        || ' ('
                        || rad.quiz_passing_score
                        || ')'
                           score_passing,
                        INITCAP (rad.quiz_result) results,
                        NVL (rad.award_amount, 0) points_cnt,
                        NVL (rad.sweepstakes_won, 0) sweepstakes_won_cnt,
                        CASE
                           WHEN     pq.INCLUDE_PASS_QUIZ_CERTIFICATE = 1
                                AND INITCAP (rad.quiz_result) = 'Passed'
                           THEN
                              1
                           ELSE
                              0
                        END
                           certificate,
                        quiz_claim_id claim_id,
                        participant_id participant_id
                   FROM rpt_quiz_activity_detail rad,
                        promotion p,
                        promo_quiz pq
                  WHERE     rad.quiz_claim_id IS NOT NULL
                        AND rad.promotion_id = p.promotion_id
                        AND rad.participant_id = p_in_paxId
                        AND (   (    p_in_promotionId IS NULL
                                 AND p_in_diyQuizId IS NULL)
                             OR (       p_in_promotionId IS NOT NULL
                                    AND rad.promotion_id IN (SELECT *
                                                               FROM TABLE (
                                                                       get_array_varchar (
                                                                          p_in_promotionId)))
                                 OR (    p_in_diyQuizId IS NOT NULL
                                     AND rad.quiz_id IN (SELECT *
                                                           FROM TABLE (
                                                                   get_array_varchar (
                                                                      p_in_diyQuizId))))))
                        AND (   (p_in_departments IS NULL)
                             OR rad.department IN (SELECT *
                                                           FROM TABLE (
                                                                   get_array_varchar (
                                                                      p_in_departments))))
                        AND rad.job_title =
                               NVL (p_in_jobPosition, rad.job_title)
                        AND p.promotion_id = pq.promotion_id
                        AND rad.participant_status =
                               NVL (p_in_participantStatus,
                                    rad.participant_status)
                        AND NVL (TRUNC (rad.quiz_date), TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                    p_in_fromDate,
                                                                                    p_in_localeDatePattern)
                                                                             AND TO_DATE (
                                                                                    p_in_toDate,
                                                                                    p_in_localeDatePattern));

      p_out_return_code := '00';
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;

         p_out_size_data := NULL;

         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;
   END prc_getQuizActivityDetailTwo;

   /* CHARTS */

   /* getQuizActivityForOrgBarResults */


   PROCEDURE prc_getQuizActivityForOrgBar (
      p_in_departments         IN     VARCHAR,
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_result              IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_paxId               IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
   
/******************************************************************************
  NAME:       prc_getQuizActivityForOrgBar
  Author             Date             Description
  ----------        ---------------  ------------------------------------------------
  ?????                 ?????        Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  nagarajs          02/16/2016       Bug 65801 - Quiz activity-Reports-Summary table "attempts passed" column total value is mismatch with pie chart "attempts passed total" values
   
  ******************************************************************************/
      c_process_name   CONSTANT execution_log.process_name%TYPE
                                   := 'prc_getQuizActivityForOrgBar' ;
      v_stage                   VARCHAR2 (500);
   /* getQuizActivityForOrgBar */

   BEGIN
      OPEN p_out_data FOR
         SELECT *
           FROM    --( reportParameters.get( "nodeAndBelow" ).equals( true ) )
                (  SELECT dtl.node_name AS Org_Name,
                          dtl.node_id AS Node_Id,
                          NVL (dtl.quiz_failed, 0) AS Attempts_Failed,
                          NVL (dtl.quiz_passed, 0) AS Attempts_Passed
                     FROM rpt_quiz_activity_summary ras,
                          (  SELECT h.node_id,
                                    h.parent_node_id,
                                    h.node_name,
                                    SUM (dtl.quiz_passed) quiz_passed,
                                    SUM (dtl.quiz_failed) quiz_failed
                               FROM (SELECT child_node_id node_id,
                                            node_id path_node_id
                                       FROM rpt_hierarchy_rollup) npn,
                                    rpt_hierarchy h,
                                    (  SELECT COUNT (ppe.node_id) quiz_eligible,
                                              ppe.node_id
                                         FROM rpt_pax_promo_elig_quiz ppe,
                                              promotion p
                                        WHERE     ppe.promotion_id = p.promotion_id
                                              AND p.promotion_type IN ('quiz',
                                                                       'diy_quiz')
                                              AND (   (    p_in_promotionId IS NULL
                                                       AND p_in_diyQuizId IS NULL)
                                                   OR (       p_in_promotionId
                                                                 IS NOT NULL
                                                          AND ppe.promotion_id IN (SELECT *
                                                                                     FROM TABLE (
                                                                                             get_array_varchar (
                                                                                                p_in_promotionId)))
                                                       OR (    p_in_diyQuizId
                                                                  IS NOT NULL
                                                           AND ppe.promotion_id IN (SELECT promotion_id
                                                                                      FROM promotion
                                                                                     WHERE     promotion_type =
                                                                                                  'diy_quiz'
                                                                                           AND promotion_status =
                                                                                                  NVL (
                                                                                                     p_in_promotionStatus,
                                                                                                     promotion_status)))))
                                              AND p.promotion_status =
                                                     NVL (p_in_promotionStatus,
                                                          p.promotion_status)
                                     GROUP BY ppe.node_id) rt,
                                    (  SELECT SUM (
                                                 DECODE (rqd.quiz_result,
                                                         'passed', 1,
                                                         0))
                                                 quiz_passed,
                                              SUM (
                                                 DECODE (rqd.quiz_result,
                                                         'failed', 1,
                                                         0))
                                                 quiz_failed,
                                              rqd.node_id
                                         FROM rpt_quiz_activity_detail rqd,
                                              promotion p
                                        WHERE     rqd.quiz_claim_id IS NOT NULL
                                              AND rqd.promotion_id = p.promotion_id
                                              AND (   (    p_in_promotionId IS NULL
                                                       AND p_in_diyQuizId IS NULL)
                                                   OR (       p_in_promotionId
                                                                 IS NOT NULL
                                                          AND rqd.promotion_id IN (SELECT *
                                                                                     FROM TABLE (
                                                                                             get_array_varchar (
                                                                                                p_in_promotionId)))
                                                       OR (    p_in_diyQuizId
                                                                  IS NOT NULL
                                                           AND rqd.quiz_id IN (SELECT *
                                                                                 FROM TABLE (
                                                                                         get_array_varchar (
                                                                                            p_in_diyQuizId))))))
                                              AND p.promotion_status =
                                                     NVL (p_in_promotionStatus,
                                                          p.promotion_status)
                                              AND (   (p_in_departments IS NULL)
                                                   OR rqd.department IN (SELECT *
                                                                           FROM TABLE (
                                                                                   get_array_varchar (
                                                                                      p_in_departments))))
                                              AND rqd.job_title =
                                                     NVL (p_in_jobPosition,
                                                          rqd.job_title)
                                              AND rqd.participant_status =
                                                     NVL (p_in_participantStatus,
                                                          rqd.participant_status)
                                              AND NVL (TRUNC (rqd.quiz_date),
                                                       TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                   p_in_fromDate,
                                                                                   p_in_localeDatePattern)
                                                                            AND TO_DATE (
                                                                                   p_in_toDate,
                                                                                   p_in_localeDatePattern)
                                     GROUP BY rqd.node_id) dtl
                              WHERE     rt.node_id (+)= npn.node_id  --02/16/2016 added outer join
                                    AND dtl.node_id(+) = npn.node_id
                                    AND npn.path_node_id = h.node_id
                           GROUP BY h.node_id, h.node_name, h.parent_node_id)
                          dtl
                    WHERE     ras.node_id = dtl.node_id
                          AND NVL (ras.parent_node_id, 0) IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           NVL (
                                                                              p_in_parentNodeId,
                                                                              0))))
                          AND ras.node_name NOT LIKE '%Team%'
                          AND p_in_nodeAndBelow = 1
                 GROUP BY dtl.node_name,
                          dtl.node_id,
                          dtl.quiz_failed,
                          dtl.quiz_passed
                 UNION
                 SELECT rh.node_name || ' Team' AS Org_Name,
                        rh.node_id AS Node_Id,
                        NVL (dtl.quiz_failed, 0) AS Attempts_Failed,
                        NVL (dtl.quiz_passed, 0) AS Attempts_Passed
                   FROM rpt_hierarchy rh,
                        (  SELECT COUNT (ppe.node_id) quiz_eligible,
                                  ppe.node_id
                             FROM rpt_pax_promo_elig_quiz ppe, promotion p
                            WHERE     ppe.promotion_id = p.promotion_id
                                  AND p.promotion_type IN ('quiz', 'diy_quiz')
                                  AND (   (    p_in_promotionId IS NULL
                                           AND p_in_diyQuizId IS NULL)
                                       OR (       p_in_promotionId IS NOT NULL
                                              AND ppe.promotion_id IN (SELECT *
                                                                         FROM TABLE (
                                                                                 get_array_varchar (
                                                                                    p_in_promotionId)))
                                           OR (    p_in_diyQuizId IS NOT NULL
                                               AND ppe.promotion_id IN (SELECT promotion_id
                                                                          FROM promotion
                                                                         WHERE     promotion_type =
                                                                                      'diy_quiz'
                                                                               AND promotion_status =
                                                                                      NVL (
                                                                                         p_in_promotionStatus,
                                                                                         promotion_status)))))
                                  AND p.promotion_status =
                                         NVL (p_in_promotionStatus,
                                              p.promotion_status)
                         GROUP BY ppe.node_id) rt,
                        (  SELECT SUM (
                                     DECODE (rqd.quiz_result, 'passed', 1, 0))
                                     quiz_passed,
                                  SUM (
                                     DECODE (rqd.quiz_result, 'failed', 1, 0))
                                     quiz_failed,
                                  rqd.node_id
                             FROM rpt_quiz_activity_detail rqd, promotion p
                            WHERE     rqd.quiz_claim_id IS NOT NULL
                                  AND rqd.promotion_id = p.promotion_id
                                  AND (   (    p_in_promotionId IS NULL
                                           AND p_in_diyQuizId IS NULL)
                                       OR (       p_in_promotionId IS NOT NULL
                                              AND rqd.promotion_id IN (SELECT *
                                                                         FROM TABLE (
                                                                                 get_array_varchar (
                                                                                    p_in_promotionId)))
                                           OR (    p_in_diyQuizId IS NOT NULL
                                               AND rqd.quiz_id IN (SELECT *
                                                                     FROM TABLE (
                                                                             get_array_varchar (
                                                                                p_in_diyQuizId))))))
                                  AND p.promotion_status =
                                         NVL (p_in_promotionStatus,
                                              p.promotion_status)
                                  AND (   (p_in_departments IS NULL)
                                       OR rqd.department IN (SELECT *
                                                               FROM TABLE (
                                                                       get_array_varchar (
                                                                          p_in_departments))))
                                  AND rqd.job_title =
                                         NVL (p_in_jobPosition, rqd.job_title)
                                  AND rqd.participant_status =
                                         NVL (p_in_participantStatus,
                                              rqd.participant_status)
                                  AND NVL (TRUNC (rqd.quiz_date),
                                           TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                       p_in_fromDate,
                                                                       p_in_localeDatePattern)
                                                                AND TO_DATE (
                                                                       p_in_toDate,
                                                                       p_in_localeDatePattern)
                         GROUP BY rqd.node_id) dtl
                  WHERE     rh.node_id = dtl.node_id(+)
                        AND rh.node_id = rt.node_id(+)
                        AND rh.is_deleted = 0
                        AND NVL (rh.node_id, 0) IN (SELECT *
                                                      FROM TABLE (
                                                              get_array_varchar (
                                                                 NVL (
                                                                    p_in_parentNodeId,
                                                                    0))))
                 ORDER BY Attempts_Passed DESC)
          WHERE ROWNUM <= 20;

      p_out_return_code := '00';
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
   END prc_getQuizActivityForOrgBar;


   /* getQuizStatusPercentForOrgBarResults */

   PROCEDURE prc_getQuizStatusPercentForOrB (
      p_in_departments         IN     VARCHAR,
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_result              IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_paxId               IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
   /******************************************************************************
  NAME:       prc_getQuizActivityForOrgBar
  Author             Date             Description
  ----------        ---------------  ------------------------------------------------
                                      Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Ravi Arumugam          03/30/2016   Bug 66261 - The quiz activity pass fail percentage chart report is producing the parsererror: 200 SyntaxError
  Ravi Arumugam          04/05/2016   Bug 66316 - Quiz Activity - Quiz Results is Displaying as Progress when Pax didnot even Complete Taking Quiz 
 nagarajs                 04/13/2016   Bug 66413, 66421 fixes     
 ******************************************************************************/
      c_process_name   CONSTANT execution_log.process_name%TYPE
                                   := 'prc_getQuizStatusPercentForOrB' ;
      v_stage                   VARCHAR2 (500);
   
   /* getQuizStatusPercentForOrgBar */


   BEGIN
      OPEN p_out_data FOR
         SELECT *
           FROM --(if ( reportParameters.get( "nodeAndBelow" ).equals( true ) )
                (  SELECT dtl.node_name AS Org_Name,
                          dtl.node_id AS Node_Id,
                          DECODE (
                                     NVL (dtl.quiz_attempt, 0),        --dtl.quiz_completed   Bug# 66261 03/30/2016
                                     0, 0,
                                     ROUND (
                                          (  NVL (dtl.quiz_failed, 0)   
                                           / dtl.quiz_attempt)          --dtl.quiz_completed  Bug# 66261 03/30/2016
                                        * 100,
                                        2)) As Failed_pct,
                         DECODE (
                                     NVL (dtl.quiz_attempt, 0),        --dtl.quiz_completed   Bug# 66261 03/30/2016
                                     0, 0,
                                     ROUND (
                                          (  NVL (dtl.quiz_passed, 0)   
                                           / dtl.quiz_attempt)          --dtl.quiz_completed  Bug# 66261 03/30/2016
                                        * 100,
                                        2)) AS passed_pct
                     FROM rpt_quiz_activity_summary ras,
                          (  SELECT h.node_id,
                                    h.parent_node_id,
                                    h.node_name,
                                    SUM (dtl.quiz_passed) quiz_passed,
                                    SUM (dtl.quiz_failed) quiz_failed,
                                    SUM(dtl.quiz_attempt) quiz_attempt,           --03/30/2016 Bug#66261
                                    SUM (dtl.quiz_completed) quiz_completed
                               FROM (SELECT child_node_id node_id,
                                            node_id path_node_id
                                       FROM rpt_hierarchy_rollup) npn,
                                    rpt_hierarchy h,
                                    (  SELECT COUNT (ppe.node_id) quiz_eligible,
                                              ppe.node_id
                                         FROM rpt_pax_promo_elig_quiz ppe,
                                              promotion p
                                        WHERE     ppe.promotion_id = p.promotion_id
                                              AND p.promotion_type IN ('quiz',
                                                                       'diy_quiz')
                                              AND (   (    p_in_promotionId IS NULL
                                                       AND p_in_diyQuizId IS NULL)
                                                   OR (       p_in_promotionId
                                                                 IS NOT NULL
                                                          AND ppe.promotion_id IN (SELECT *
                                                                                     FROM TABLE (
                                                                                             get_array_varchar (
                                                                                                p_in_promotionId)))
                                                       OR (    p_in_diyQuizId
                                                                  IS NOT NULL
                                                           AND ppe.promotion_id IN (SELECT promotion_id
                                                                                      FROM promotion
                                                                                     WHERE     promotion_type =
                                                                                                  'diy_quiz'
                                                                                           AND promotion_status =
                                                                                                  NVL (
                                                                                                     p_in_promotionStatus,
                                                                                                     promotion_status)))))
                                              AND p.promotion_status =
                                                     NVL (p_in_promotionStatus,
                                                          p.promotion_status)
                                     GROUP BY ppe.node_id) rt,
                                    (  SELECT COUNT(rqd.quiz_result) quiz_attempt,   --03/30/2016 Bug#66261 
                                    SUM (
                                                 DECODE (rqd.quiz_result,
                                                         'progress', 0,  --04/05/2016 Bug 66316 --04/13/2016
                                                         1))
                                                 quiz_completed,
                                              SUM (
                                                 DECODE (rqd.quiz_result,
                                                         'passed', 1,
                                                         0))
                                                 quiz_passed,
                                              SUM (
                                                 DECODE (rqd.quiz_result,
                                                         'failed', 1,
                                                         0))
                                                 quiz_failed,
                                              rqd.node_id
                                         FROM rpt_quiz_activity_detail rqd,
                                              promotion p
                                        WHERE     rqd.quiz_claim_id IS NOT NULL
                                              AND rqd.promotion_id = p.promotion_id
                                              AND (   (    p_in_promotionId IS NULL
                                                       AND p_in_diyQuizId IS NULL)
                                                   OR (       p_in_promotionId
                                                                 IS NOT NULL
                                                          AND rqd.promotion_id IN (SELECT *
                                                                                     FROM TABLE (
                                                                                             get_array_varchar (
                                                                                                p_in_promotionId)))
                                                       OR (    p_in_diyQuizId
                                                                  IS NOT NULL
                                                           AND rqd.quiz_id IN (SELECT *
                                                                                 FROM TABLE (
                                                                                         get_array_varchar (
                                                                                            p_in_diyQuizId))))))
                                              AND p.promotion_status =
                                                     NVL (p_in_promotionStatus,
                                                          p.promotion_status)
                                              AND (   (p_in_departments IS NULL)
                                                   OR rqd.department IN (SELECT *
                                                                           FROM TABLE (
                                                                                   get_array_varchar (
                                                                                      p_in_departments))))
                                              AND rqd.job_title =
                                                     NVL (p_in_jobPosition,
                                                          rqd.job_title)
                                              AND rqd.participant_status =
                                                     NVL (p_in_participantStatus,
                                                          rqd.participant_status)
                                              AND NVL (TRUNC (rqd.quiz_date),
                                                       TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                                   p_in_fromDate,
                                                                                   p_in_localeDatePattern)
                                                                            AND TO_DATE (
                                                                                   p_in_toDate,
                                                                                   p_in_localeDatePattern)
                                     GROUP BY rqd.node_id) dtl
                              WHERE     rt.node_id = npn.node_id
                                    AND dtl.node_id(+) = npn.node_id
                                    AND npn.path_node_id = h.node_id
                           GROUP BY h.node_id, h.node_name, h.parent_node_id)
                          dtl
                    WHERE     ras.node_id = dtl.node_id
                          AND NVL (ras.parent_node_id, 0) IN (SELECT *
                                                                FROM TABLE (
                                                                        get_array_varchar (
                                                                           NVL (
                                                                              p_in_parentNodeId,
                                                                              0))))
                          AND ras.node_name NOT LIKE '%Team%'
                          AND p_in_nodeAndBelow = 1
                 GROUP BY dtl.node_name,
                          dtl.node_id,
                          dtl.quiz_failed,
                          dtl.quiz_passed,
                          dtl.quiz_attempt,     --03/30/2016  Bug#66261
                          dtl.quiz_completed
                 UNION
                 SELECT rh.node_name || ' Team' AS Org_Name,
                        rh.node_id AS Node_Id,
                       DECODE (
                                     NVL (dtl.quiz_attempt, 0),        --dtl.quiz_completed   Bug# 66261 03/30/2016
                                     0, 0,
                                     ROUND (
                                          (  NVL (dtl.quiz_failed, 0)   
                                           / dtl.quiz_attempt)          --dtl.quiz_completed  Bug# 66261 03/30/2016
                                        * 100,
                                        2)) As Failed_pct,
                         DECODE (
                                     NVL (dtl.quiz_attempt, 0),        --dtl.quiz_completed   Bug# 66261 03/30/2016
                                     0, 0,
                                     ROUND (
                                          (  NVL (dtl.quiz_passed, 0)   
                                           / dtl.quiz_attempt)          --dtl.quiz_completed  Bug# 66261 03/30/2016
                                        * 100,
                                        2)) AS passed_pct
                   FROM rpt_hierarchy rh,
                        (  SELECT COUNT (ppe.node_id) quiz_eligible,
                                  ppe.node_id
                             FROM rpt_pax_promo_elig_quiz ppe, promotion p
                            WHERE     ppe.promotion_id = p.promotion_id
                                  AND p.promotion_type IN ('quiz', 'diy_quiz')
                                  AND (   (    p_in_promotionId IS NULL
                                           AND p_in_diyQuizId IS NULL)
                                       OR (       p_in_promotionId IS NOT NULL
                                              AND ppe.promotion_id IN (SELECT *
                                                                         FROM TABLE (
                                                                                 get_array_varchar (
                                                                                    p_in_promotionId)))
                                           OR (    p_in_diyQuizId IS NOT NULL
                                               AND ppe.promotion_id IN (SELECT promotion_id
                                                                          FROM promotion
                                                                         WHERE     promotion_type =
                                                                                      'diy_quiz'
                                                                               AND promotion_status =
                                                                                      NVL (
                                                                                         p_in_promotionStatus,
                                                                                         promotion_status)))))
                                  AND p.promotion_status =
                                         NVL (p_in_promotionStatus,
                                              p.promotion_status)
                         GROUP BY ppe.node_id) rt,
                        (  SELECT COUNT(rqd.quiz_result) quiz_attempt,    --03/30/2016 Bug# 66261 
                        SUM (
                                     DECODE (rqd.quiz_result, 'passed', 1, 0))
                                     quiz_passed,
                                  SUM (
                                     DECODE (rqd.quiz_result, 'failed', 1, 0))
                                     quiz_failed,
                                  SUM (
                                     DECODE (rqd.quiz_result, 'progress', 0, 1))   --04/05/2016 Bug 66316 --04/13/2016
                                     quiz_completed,
                                  rqd.node_id
                             FROM rpt_quiz_activity_detail rqd, promotion p
                            WHERE     rqd.quiz_claim_id IS NOT NULL
                                  AND rqd.promotion_id = p.promotion_id
                                  AND (   (    p_in_promotionId IS NULL
                                           AND p_in_diyQuizId IS NULL)
                                       OR (       p_in_promotionId IS NOT NULL
                                              AND rqd.promotion_id IN (SELECT *
                                                                         FROM TABLE (
                                                                                 get_array_varchar (
                                                                                    p_in_promotionId)))
                                           OR (    p_in_diyQuizId IS NOT NULL
                                               AND rqd.quiz_id IN (SELECT *
                                                                     FROM TABLE (
                                                                             get_array_varchar (
                                                                                p_in_diyQuizId))))))
                                  AND p.promotion_status =
                                         NVL (p_in_promotionStatus,
                                              p.promotion_status)
                                  AND (   (p_in_departments IS NULL)
                                       OR rqd.department IN (SELECT *
                                                               FROM TABLE (
                                                                       get_array_varchar (
                                                                          p_in_departments))))
                                  AND rqd.job_title =
                                         NVL (p_in_jobPosition, rqd.job_title)
                                  AND rqd.participant_status =
                                         NVL (p_in_participantStatus,
                                              rqd.participant_status)
                                  AND NVL (TRUNC (rqd.quiz_date),
                                           TRUNC (SYSDATE)) BETWEEN TO_DATE (
                                                                       p_in_fromDate,
                                                                       p_in_localeDatePattern)
                                                                AND TO_DATE (
                                                                       p_in_toDate,
                                                                       p_in_localeDatePattern)
                         GROUP BY rqd.node_id) dtl
                  WHERE     rh.node_id = dtl.node_id(+)
                        AND rh.node_id = rt.node_id(+)
                        AND rh.is_deleted = 0
                        AND NVL (rh.node_id, 0) IN (SELECT *
                                                      FROM TABLE (
                                                              get_array_varchar (
                                                                 NVL (
                                                                    p_in_parentNodeId,
                                                                    0))))
                 ORDER BY Passed_Pct DESC)
          WHERE ROWNUM <= 20;

      p_out_return_code := '00';
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
   END prc_getQuizStatusPercentForOrB;
END pkg_query_quiz_activity;
/
