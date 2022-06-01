CREATE OR REPLACE PACKAGE pkg_quiz_reports IS

 PROCEDURE prc_rpt_quiz_activity_details
 (p_in_requested_user_id      IN  NUMBER,
  p_in_start_date             IN  DATE,
  p_in_end_date               IN  DATE,
  p_out_return_code           OUT NUMBER,
  p_out_error_message         OUT VARCHAR2);                                        
                                        
  PROCEDURE prc_rpt_quiz_activity_summary
  (p_in_requested_user_id      IN  NUMBER,
   p_out_return_code           OUT NUMBER,
   p_out_error_message         OUT VARCHAR2);
    
  FUNCTION FNC_ELIGIBILITY_COUNT
  ( p_in_promotion_id IN NUMBER,
    p_in_node_id      IN NUMBER,
--    p_in_giver_recvr  IN VARCHAR2,
    p_in_record_type  IN VARCHAR2,
    p_in_pax_status   IN VARCHAR2,
    p_in_job_code     IN VARCHAR2,
    p_in_department   IN VARCHAR2,
    p_in_promo_type   IN VARCHAR2 DEFAULT 'quiz')
  RETURN  NUMBER;

  FUNCTION FNC_ELIGIBILE_QUIZZES
  ( p_in_promotion_id IN NUMBER,
    p_in_user_id      IN NUMBER,
    p_in_node_id      IN NUMBER    )
  RETURN  NUMBER;


  FUNCTION FNC_GET_QUIZ_COUNTS
  ( p_in_node_id      IN NUMBER,
    p_in_node_name    IN VARCHAR2,
    p_in_promotion_id IN NUMBER,
    p_in_from_date    IN DATE,
    p_in_to_date      IN DATE,
    p_in_award_type   IN VARCHAR2,
    p_in_pax_status   IN VARCHAR2,
    p_in_job_title    IN VARCHAR2,
    p_in_department   IN VARCHAR2,
    p_in_count_type   IN VARCHAR2,
    p_in_quiz_date    IN DATE DEFAULT NULL)
  RETURN  NUMBER;
  
  FUNCTION FNC_GET_PERIOD_DATE
  ( p_in_date         IN DATE,
    p_in_period_type  IN VARCHAR2)
  RETURN  VARCHAR2;
  
  FUNCTION fnc_get_quiz_result
(p_participant_id IN Number,p_promotion_name IN varchar2)
RETURN VARCHAR2;

END;
/
CREATE OR REPLACE PACKAGE BODY pkg_quiz_reports IS

FUNCTION fnc_get_quiz_result
(p_participant_id IN Number,p_promotion_name IN varchar2)
RETURN VARCHAR2
is
V_cnt NUMBER:=0;
BEGIN

SELECT COUNT(1) into v_cnt
FROM
rpt_quiz_activity_detail rpt
WHERE
rpt.promo_quiz_name=p_promotion_name
AND rpt.participant_id=p_participant_id
AND quiz_result='passed';

IF v_cnt > 0 THEN
RETURN 'Passed';
ELSE
SELECT COUNT(*) into v_cnt
FROM
rpt_quiz_activity_detail rpt
WHERE
rpt.promo_quiz_name=p_promotion_name
AND rpt.participant_id=p_participant_id
AND quiz_result='progress';
 IF v_cnt > 0 THEN
 RETURN 'Incomplete';
 ELSE
 RETURN 'Failed';
 END IF;
 END IF;
 
 Exception WHEN OTHERS THEN
 RETURN NULL;
 
END;

PROCEDURE prc_rpt_quiz_activity_summary(p_in_requested_user_id      IN  NUMBER,
                                        p_out_return_code           OUT NUMBER,
                                        p_out_error_message         OUT VARCHAR2)
   IS
      /*******************************************************************************

   Purpose:  Populate the rpt_badge_summary reporting table

   Person                      Date       Comments
   -----------                ---------- -----------------------------------------------------
   ????????                  ???????           Initial
   Ravi Dhanekula    06/18/2014   Performance fix for default teamsum records. 
   nagarajs          03/29/2017   Change to sequence
*******************************************************************************/
   v_rpt_quiz_activity_summary_id   RPT_QUIZ_ACTIVITY_SUMMARY.RPT_QUIZ_ACTIVITY_SUMMARY_ID%TYPE;
    v_hierarchy_level               RPT_QUIZ_ACTIVITY_SUMMARY.HIERARCHY_LEVEL%TYPE;
    v_eligible_quizzes              RPT_QUIZ_ACTIVITY_SUMMARY.ELIGIBLE_QUIZZES%TYPE;
    v_quizzes_completed             RPT_QUIZ_ACTIVITY_SUMMARY.QUIZZES_COMPLETED%TYPE;
    v_quizzes_passed                RPT_QUIZ_ACTIVITY_SUMMARY.QUIZZES_PASSED%TYPE;
    v_quizzes_failed                RPT_QUIZ_ACTIVITY_SUMMARY.QUIZZES_failed%TYPE;
    v_quiz_date                     RPT_QUIZ_ACTIVITY_SUMMARY.QUIZ_DATE%TYPE;
    v_award_given                   RPT_QUIZ_ACTIVITY_SUMMARY.AWARD_GIVEN%TYPE;
    v_award_type                    RPT_QUIZ_ACTIVITY_SUMMARY.AWARD_TYPE%TYPE;
    v_node_name                     RPT_QUIZ_ACTIVITY_SUMMARY.NODE_NAME%TYPE;
    v_parent_node_id                RPT_QUIZ_ACTIVITY_SUMMARY.PARENT_NODE_ID%TYPE;
    v_date_created                  RPT_QUIZ_ACTIVITY_SUMMARY.DATE_CREATED%TYPE;
    v_created_by                    RPT_QUIZ_ACTIVITY_SUMMARY.CREATED_BY%TYPE:=0;

    v_promotion_id                  RPT_QUIZ_ACTIVITY_DETAIL.PROMOTION_ID%TYPE;
    v_pax_status                    RPT_QUIZ_ACTIVITY_SUMMARY.participant_status%TYPE;
    v_job_title                     RPT_QUIZ_ACTIVITY_SUMMARY.job_title%TYPE;
    v_department                    RPT_QUIZ_ACTIVITY_SUMMARY.department%TYPE;
    v_date_modified                 DATE;
    v_node_id                       RPT_HIERARCHY.NODE_ID%TYPE;
    v_is_leaf                       RPT_HIERARCHY.IS_LEAF%TYPE;


    v_child_node_id                 NUMBER(18);
    v_eligible_participants         NUMBER(18);
    v_rpt_quiz_activity_summry_id   NUMBER; 
    v_rec_cnt                       NUMBER;
    c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_quiz_actvity_summary');
    c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
    c_created_by         CONSTANT rpt_awardmedia_summary.created_by%TYPE := p_in_requested_user_id;

    v_stage              execution_log.text_line%TYPE;
    v_rpt_row_id    ROWID;

BEGIN

    SELECT SYSDATE INTO v_date_created from dual;
 -- remove obsolete node summaries (node not found in rpt_hierarchy)
   v_stage := 'DELETE obsolete node summary records';
   DELETE
     FROM rpt_quiz_activity_summary s
    WHERE s.node_id NOT IN
          ( -- get node ID currently in the report hierarchy
            SELECT h.node_id
              FROM rpt_hierarchy h
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   ------------
   -- merge derived summary records
   v_stage := 'MERGE detail derived summary records';
   MERGE INTO rpt_quiz_activity_summary d
     USING ( WITH rpt_teamsum AS
            (  -- build team summary records
                SELECT -- key fields
                      h.hier_level as hier_level,
                      h.parent_node_id as header_node_id,
                      nvl(qd.node_id,h.node_id) as detail_node_id,
                      nvl(sum(qd.award_amount),0) as award_given ,
                      nvl(qd.promotion_id,0) as promotion_id,
                      nvl(qd.award_type, ' ') as award_type,
                      nvl(qd.participant_status,' ') AS pax_status,
                      nvl(qd.job_title,' ') AS job_position,
                      nvl(qd.department,' ') AS department,
                      h.node_name as node_name,
                      sysdate as date_created,
                      h.is_leaf as is_leaf,
                      TRUNC(qd.quiz_date) quiz_date,
                      result.quiz_completed,
                      result.quiz_passed,
                      result.quiz_failed,
                      egbq.eligible_quizzes
                 FROM rpt_quiz_activity_detail qd,
                      rpt_hierarchy h,
                      (SELECT COUNT(DECODE(quiz_result,'failed',1,NULL)) quiz_failed,
                              COUNT(DECODE(quiz_result,'passed',1,NULL)) quiz_passed,
                              COUNT(quiz_result)  quiz_completed ,node_id         
                        FROM  rpt_quiz_activity_detail
                       GROUP BY node_id) result,
                      (SELECT COUNT(DISTINCT(pq.promotion_id))  eligible_quizzes , un.node_id      
                         FROM rpt_pax_promo_eligibility rppe, promo_quiz pq,user_node un
                        WHERE rppe.participant_id = un.user_id(+)
                          AND rppe.promotion_id = pq.promotion_id 
                        GROUP BY un.node_id) egbq 
                WHERE h.node_id = qd.node_id
                  AND result.node_id = h.node_id
                  AND egbq.node_id = h.node_id
               GROUP BY    h.hier_level,
                      h.parent_node_id,
                      nvl(qd.node_id,h.node_id),
                      nvl(qd.promotion_id,0),
                      nvl(qd.award_type, ' '),
                      nvl(qd.participant_status,' '),
                      nvl(qd.job_title,' '),
                      nvl(qd.department,' '),
                      h.node_name,
                      h.is_leaf,
                      TRUNC(qd.quiz_date),
                      result.quiz_completed,
                      result.quiz_passed,
                      result.quiz_failed,
                      egbq.eligible_quizzes
            ), detail_derived_summary AS
            (  -- derive summaries based on team summary data
               SELECT -- key fields
                      rt.detail_node_id,
                      rt.node_name||'> Team' AS sum_type,
                      rt.promotion_id,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.quiz_date,
                      -- reference fields
                      rt.award_type,
                      rt.header_node_id,
                      rt.hier_level,
                      rt.node_name,
                      1 AS is_leaf, -- The team summary records are always a leaf
                      -- count fields
                      rt.award_given,
                      rt.quiz_completed,
                      rt.quiz_passed,
                      rt.quiz_failed,
                      rt.eligible_quizzes 
                 FROM rpt_teamsum rt
                UNION ALL
               SELECT -- key fields
                      h.node_id AS detail_node_id,
                      replace(RT.node_name,'> Team','') AS sum_type,
                      rt.promotion_id,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.quiz_date,
                      -- reference fields
                      rt.award_type,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      h.node_name,
                      h.is_leaf,
                      -- count fields
                      NVL(SUM(rt.award_given),0) AS award_given,
                      sum(rt.quiz_completed) quiz_completed,
                      sum(rt.quiz_passed) quiz_passed,
                      sum(rt.quiz_failed) quiz_failed,
                      rt.eligible_quizzes 
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
                   
                WHERE (  rt.hier_level = h.hier_level    -- always create node summary at team summary level
                      --OR rt.total_media_amount != 0      -- create node summary for team summaries with non-zero media amounts
                      )
                   -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  AND rt.detail_node_id = npn.node_id
                  AND npn.path_node_id = h.node_id
                GROUP BY h.node_id,
                      replace(RT.node_name,'> Team',''),
                      rt.promotion_id,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.quiz_date,
                      rt.award_type,
                     -- rt.quiz_completed,
                     -- rt.quiz_passed,
                     -- rt.quiz_failed,
                      h.parent_node_id,
                      h.hier_level,
                      h.node_name,
                      h.is_leaf,
                      rt.eligible_quizzes 
            ) -- end detail_derived_summary
            -- compare existing summary records with detail derived summaries
            SELECT es.s_rowid,
                   dds.hier_level || '-' || dds.sum_type AS record_type,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.node_id detail_node_id,
                            node_name AS sum_type,
                            s2.promotion_id,
                            s2.participant_status,
                            s2.job_title,
                            s2.department,
                            s2.quiz_date
                       FROM rpt_quiz_activity_summary s2
                      WHERE s2.quiz_date IS NOT NULL
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                   FULL OUTER JOIN detail_derived_summary dds
                   ON (   es.detail_node_id = dds.detail_node_id
                      AND es.sum_type       = dds.sum_type
                      AND es.promotion_id   = dds.promotion_id
                      AND es.participant_status     = dds.pax_status
                      AND es.job_title   = dds.job_position
                      AND es.department     = dds.department
                      AND es.quiz_date     = dds.quiz_date
                      )
         )s
      ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
      UPDATE SET
         d.award_type         = s.award_type,
         d.parent_node_id     = s.header_node_id,
         d.hierarchy_level    = s.hier_level,
         d.is_leaf            = s.is_leaf,
         d.award_given        = s.award_given,
         d.quizzes_passed     = s.quiz_passed,
         d.quizzes_failed     = s.quiz_failed,
         d.quizzes_completed  = s.quiz_completed, 
         d.eligible_quizzes   = s.eligible_quizzes,
         d.modified_by        = c_created_by,
         d.date_modified      = SYSDATE
       WHERE ( -- only update records with different values
                DECODE(d.award_type,         s.award_type,         1, 0) = 0
             OR DECODE(d.parent_node_id,     s.header_node_id,     1, 0) = 0
             OR DECODE(d.hierarchy_level,    s.hier_level,         1, 0) = 0
             OR DECODE(d.is_leaf,            s.is_leaf,            1, 0) = 0
             OR DECODE(d.award_given,        s.award_given,        1, 0) = 0
             OR DECODE(d.quizzes_passed,        s.quiz_passed,        1, 0) = 0 
             OR DECODE(d.quizzes_failed,        s.quiz_failed,        1, 0) = 0 
             OR DECODE(d.quizzes_completed,     s.quiz_completed,     1, 0) = 0
             )
      -- remove existing summaries that no longer have product details
      DELETE
       WHERE s.promotion_id IS NULL
    WHEN NOT MATCHED THEN
      INSERT
        ( rpt_quiz_activity_summary_id,
          promotion_id,
          hierarchy_level,
          parent_node_id,
          node_id,
          node_name,
          eligible_quizzes,
          quizzes_completed,
          quizzes_passed,
          quizzes_failed,
          quiz_date,
          award_type,
          award_given,
          is_leaf,
          participant_status,
          job_title,
          department,
          date_created,
          created_by)
        VALUES
         (rpt_quiz_activity_summry_pk_sq.NEXTVAL, --03/29/2017
          s.promotion_id,
          s.hier_level,
          s.header_node_id,
          s.detail_node_id,
          s.sum_type,
          s.eligible_quizzes,
          s.quiz_completed,
          s.quiz_passed,
          s.quiz_failed,
          s.quiz_date,
          s.award_type,
          s.award_given,
          s.is_leaf, -- all team records need to have the leaf set to 1
          s.pax_status,
          s.job_position,
          s.department,
          SYSDATE,
          v_created_by);

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   ------------
   -- add missing default summary permutations
   v_stage := 'INSERT missing default team summary permutations';
   INSERT INTO rpt_quiz_activity_summary
   (  rpt_quiz_activity_summary_id,
      promotion_id,
      hierarchy_level,
      parent_node_id,
      node_id,
      node_name,
      --eligible_quizzes,
      quizzes_completed,
      quizzes_passed,
      quizzes_failed,
      quiz_date,
      award_type,
      award_given,
      is_leaf,
      participant_status,
      job_title,
      department,
      date_created,
      created_by
   )
     (  -- find missing default permutations
      SELECT  rpt_quiz_activity_summry_pk_sq.NEXTVAL,
             -- key fields
             0    AS promotion_id,
             nsp.hier_level,
             nsp.header_node_id,
             nsp.detail_node_id,
             nsp.node_name,
             0 quizzes_completed,
             0 quizzes_passed,
             0 quizzes_failed,
             NULL AS quiz_date,
             ' '  AS award_type,
              0 AS award_amount,
               nsp.is_leaf,
             ' '  AS particpant_status,
             ' '  AS job_title,
             ' '  AS department,
             SYSDATE      AS date_created,
             v_created_by AS created_by             
        FROM ( -- get all possible node summary type permutations
               SELECT h.node_id AS detail_node_id,
                      node_name||'> Team' node_name,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      1 AS is_leaf   -- team summary always a leaf node
                 FROM rpt_hierarchy h
             ) nsp
          -- exclude default permutation when a matching summary record exists
        WHERE NOT EXISTS
             ( SELECT 1
                 FROM rpt_quiz_activity_summary s
                WHERE s.node_id = nsp.detail_node_id
                  AND s.node_name    = nsp.node_name
                   -- any media date value matches on team summary records because detail derived summaries replace default team permutations
             )
   );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   v_stage := 'INSERT missing default node summary permutations';
   INSERT INTO rpt_quiz_activity_summary
   (  rpt_quiz_activity_summary_id,
      promotion_id,
      hierarchy_level,
      parent_node_id,
      node_id,
      node_name,
      eligible_quizzes,
      quizzes_completed,
      quizzes_passed,
      quizzes_failed,
      quiz_date,
      award_type,
      award_given,
      is_leaf,
      participant_status,
      job_title,
      department,
      date_created,
      created_by
   )
     ( SELECT rpt_quiz_activity_summry_pk_sq.NEXTVAL,
             -- key fields
             0    AS promotion_id,
             nsp.hier_level,
             nsp.header_node_id,
             nsp.detail_node_id,
             nsp.node_name,
             0 eligible_quizzes,
             0 quizzes_completed,
             0 quizzes_passed,
             0 quizzes_failed,
             NULL AS quiz_date,
             ' '  AS award_type,
              0 AS award_amount,
               nsp.is_leaf,
             ' '  AS particpant_status,
             ' '  AS job_title,
             ' '  AS department,
             SYSDATE      AS date_created,
             v_created_by AS created_by  
        FROM ( -- get all possible node summary type permutations
               SELECT h.node_id AS detail_node_id,
                      h.node_name,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      h.is_leaf
                 FROM rpt_hierarchy h
             ) nsp
          -- exclude default permutation when a matching summary record exists
        WHERE NOT EXISTS
             ( SELECT 1
                 FROM rpt_quiz_activity_summary s
                WHERE s.node_id = nsp.detail_node_id
                  AND s.node_name  = nsp.node_name
                  AND s.quiz_date IS NULL
             )
          -- default node summary permutation must have default team summary permutation in its hierarchy
         AND nsp.detail_node_id IN --06/18/2014
             ( -- get team permutations under node permutation
               SELECT tp.node_id 
                 FROM rpt_quiz_activity_summary tp
                WHERE --tp.node_id          = nsp.detail_node_id
                  node_name like  '%> Team'
                  AND tp.quiz_date IS NULL
                UNION ALL
               -- get team permutations under node permutation hierarchy
               SELECT tp.parent_node_id
                 FROM rpt_quiz_activity_summary tp
                   -- start with child node immediately under current node
                START WITH --tp.parent_node_id          = nsp.detail_node_id
                       node_name like  '%> Team'
                       AND tp.quiz_date IS NULL
                CONNECT BY PRIOR tp.node_id          = tp.parent_node_id
                       AND PRIOR node_name = node_name
                       AND tp.quiz_date IS NULL
             ));
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   ------------
   -- remove team permutations when an associated detail derived summary exists
   v_stage := 'DELETE obsolete team permutations';
   DELETE rpt_quiz_activity_summary
    WHERE ROWID IN
          ( -- get default team permutation with a corresponding detail derived team summary
            SELECT DISTINCT
                   s.ROWID
              FROM rpt_quiz_activity_summary s,
                   rpt_quiz_activity_summary dd
                -- substr matches functional index
             WHERE dd.node_name like '%> Team' 
                -- detail derived summaries have a media date
               AND dd.quiz_date IS NOT NULL
               AND dd.node_id          = s.node_id
               AND dd.node_name = s.node_name
                -- default permutations have no media date
               AND s.quiz_date IS NULL
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- delete node permutation with no associated team permutation
   v_stage := 'DELETE obsolete node permutations with no associated team permutation';
   DELETE rpt_quiz_activity_summary
    WHERE ROWID IN
          ( SELECT np.ROWID
              FROM rpt_quiz_activity_summary np
             WHERE np.node_name = np.node_name
               AND np.quiz_date IS NULL
                -- node permutation has no team permutation
               AND NOT EXISTS
                   ( -- get team permutations under node permutation
                     SELECT 1
                       FROM rpt_quiz_activity_summary tp
                      WHERE tp.node_id          = np.node_id
                        AND tp.node_name like '%> Team' 
                        AND tp.quiz_date IS NULL
                      UNION ALL
                     -- get team permutations under node permutation hierarchy
                     SELECT 1
                       FROM rpt_quiz_activity_summary tp
                         -- start with child node immediately under current node
                      START WITH tp.parent_node_id          = np.node_id
                             AND tp.node_name like '%> Team' 
                             AND tp.quiz_date IS NULL
                      CONNECT BY PRIOR tp.node_id          = tp.parent_node_id
                             AND PRIOR tp.node_name = tp.node_name
                             AND tp.quiz_date IS NULL
                   )
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
         
   v_stage := 'Success';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
   
   p_out_return_code := 0;

EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code   := 99;
   p_out_error_message := v_stage ||'  '|| SQLERRM;   
   prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ': ' || SQLCODE || ', ' || SQLERRM, NULL);
END; 
 
PROCEDURE prc_rpt_quiz_activity_details(p_in_requested_user_id      IN  NUMBER,
                                        p_in_start_date             IN  DATE,
                                        p_in_end_date               IN  DATE,
                                        p_out_return_code           OUT NUMBER,
                                        p_out_error_message         OUT VARCHAR2)  IS
/*******************************************************************************
   Purpose:  Populate the rpt_quiz_activity_detail reporting tables

   Person        Date       Comments
   -----------   ---------- -----------------------------------------------------
   nagarajs     03/21/2012  Bug #40345 fix to generate Quiz Activity detail report for 
                            live and expired promotions but not for deleted promotion.
   Chidamba     06/29/2012  G5 Reports - alter flow for incrimental approach     
   Ravi Dhanekula  10/23/2012  Added country_id to detail table. 
                            10/25/2012 Added badges_earned to activity_detail table.   
                            10/30/2012 Added sweepstakes_won to activity_detail table.
   Chidamba     10/03/2013  Defect # 4059 - Fixed. adding diy_quiz to report table 
   Ravi Dhanekula  10/07/2013 Change required to include merch level sweepstakes.
                   12/31/2013  Fixed the bug # 50697
                   03/07/2014  Fixed the issue where node_name is shown an NULL in case if the quiz result is 'progress'.         
                   04/04/2014 Fixed the defect # 51044
                   04/10/2014 Fixed the defect # 52681
  Suresh J         01/29/2015   Bug #57300 - Fixed the issue of Node Name Changes that are not reflected in reports 
  Ravi Arumugam    04/05/2016   Bug-66316 -Quiz Activity - Quiz Results is Displaying as Progress when Pax didnot even Complete Taking Quiz  
  nagarajs         04/11/2016  Bug 66318 - Quiz Activity - In Summary Table Org Unit is Displaying as Blank For All Pax    
  nagarajs         04/13/2016  Bug 66413, 66421 fixes             
*******************************************************************************/
    v_rpt_quiz_activity_detail_id   rpt_quiz_activity_detail.RPT_QUIZ_ACTIVITY_DETAIL_ID%TYPE;
    v_activity_id                   rpt_quiz_activity_detail.ACTIVITY_ID%TYPE;
    v_promotion_id                  rpt_quiz_activity_detail.PROMOTION_ID%TYPE;
    v_award_type                    rpt_quiz_activity_detail.AWARD_TYPE%TYPE;
    v_award_date                    rpt_quiz_activity_detail.AWARD_DATE%TYPE;
    v_award_amount                  rpt_quiz_activity_detail.AWARD_AMOUNT%TYPE;
    v_is_taxable                    rpt_quiz_activity_detail.IS_TAXABLE%TYPE;
    v_promo_quiz_name               rpt_quiz_activity_detail.PROMO_QUIZ_NAME%TYPE;
    v_quiz_id                       rpt_quiz_activity_detail.QUIZ_ID%TYPE;
    v_quiz_name                     rpt_quiz_activity_detail.QUIZ_NAME%TYPE;
    v_quiz_score                    rpt_quiz_activity_detail.QUIZ_SCORE%TYPE;
    v_quiz_result                   rpt_quiz_activity_detail.QUIZ_RESULT%TYPE;
    v_quiz_passing_score            rpt_quiz_activity_detail.QUIZ_PASSING_SCORE%TYPE;
    v_quiz_question_count           rpt_quiz_activity_detail.QUIZ_QUESTION_COUNT%TYPE;
    v_quiz_date                     rpt_quiz_activity_detail.QUIZ_DATE%TYPE;
    v_quiz_claim_id                 rpt_quiz_activity_detail.QUIZ_CLAIM_ID%TYPE;
    v_node_name                     rpt_quiz_activity_detail.NODE_NAME%TYPE;
    v_node_id                       rpt_quiz_activity_detail.NODE_ID%TYPE;
    v_participant_id                rpt_quiz_activity_detail.PARTICIPANT_ID%TYPE;
    v_country_id                     rpt_quiz_activity_detail.COUNTRY_ID%TYPE;
    v_participant_last_name         rpt_quiz_activity_detail.PARTICIPANT_LAST_NAME%TYPE;
    v_participant_first_name        rpt_quiz_activity_detail.PARTICIPANT_FIRST_NAME%TYPE;
    v_participant_middle_name       rpt_quiz_activity_detail.PARTICIPANT_MIDDLE_NAME%TYPE;
    v_participant_status            rpt_quiz_activity_detail.PARTICIPANT_STATUS%TYPE;
    v_job_title                     rpt_quiz_activity_detail.JOB_TITLE%TYPE;
    v_department                    rpt_quiz_activity_detail.DEPARTMENT%TYPE;
    v_badges_earned          rpt_quiz_activity_detail.BADGES_EARNED%TYPE;
    v_sweepstakes_won          rpt_quiz_activity_detail.SWEEPSTAKES_WON%TYPE;--10/30/2012
    v_date_created                  rpt_quiz_activity_detail.DATE_CREATED%TYPE;
    v_rpt_quiz_activity_dtl         rpt_quiz_activity_detail%ROWTYPE;
    v_created_by                    rpt_quiz_activity_detail.created_by%TYPE:=p_in_requested_user_id ;
    v_rec_count                     NUMBER;
    v_stage                         EXECUTION_LOG.TEXT_LINE%TYPE;
    
    c_process_name                  CONSTANT execution_log.process_name%TYPE := UPPER('prc_rpt_quiz_activity_details');
    c_release_level                 CONSTANT execution_log.release_level%TYPE := 2.0;        
    
  
    CURSOR cur_quiz_activity (v_start_date IN DATE, v_end_date IN DATE)  --(in_activity_id IN NUMBER)  --Chidamba 06/29/2012 alter cursor -- --Fix for the bug # 52681 --04/10/2014
     IS     
     SELECT
        c.node_id as node_id,
        a.activity_id, 
        a.date_created as award_date,
        n.name as node_name,
        p.promotion_name as promo_quiz_name,
      nvl(p.promotion_id,0) as promotion_id,
       nvl(p.award_type,' ') as award_type,
       p.is_taxable as is_taxable,
       q.name as quiz_name,
       pq.quiz_id as quiz_id,
      qc.passing_score as quiz_passing_score,
      qc.score as quiz_score,
    qc.question_count as quiz_question_count,
                NVL(DECODE(qc.pass,1,'passed',0,'failed','progress'),' ') quiz_result,  --04/05/2016 Bug 66316 --04/13/2016
     --qc.pass as quiz_result,
        qc.claim_id as quiz_claim_id,
        c.submission_date as quiz_date,
       c.submitter_id as participant_id,
        ua.country_id as country_id,
        au.first_name as participant_first_name,
        au.middle_name as participant_middle_name,
               au.last_name as participant_last_name,
                nvl(DECODE(au.is_active,1,'active',0,'inactive'),' ') pax_status,
      nvl(pe.position_type,' ') as job_title,
       nvl(pe.department_type, ' ') as department,
        DECODE (ci.approval_status_type,'winner',1,0) AS sweepstakes_won        
     FROM activity a,
          node n,
         promotion p,
         promo_quiz pq,
          quiz q,
          claim c,
          quiz_claim qc,
          claim_item ci, --10/30/2012
          application_user au,
          user_address ua,
          participant pax,
          vw_curr_pax_employer pe
        --  participant_badge pb
      WHERE --a.activity_id = in_activity_id                      --Chidamba 06/29/2012 commented        
         c.claim_id = a.claim_id(+)
        AND qc.claim_id = c.claim_id
        AND c.claim_id = ci.claim_id(+) --10/30/2012
        --AND a.node_id = n.node_id(+) --04/11/2016
        AND c.node_id = n.node_id(+) --04/11/2016
        AND c.promotion_id = p.promotion_id
        AND p.promotion_type = 'quiz'
        AND p.promotion_id = pq.promotion_id
        AND pq.quiz_id = q.quiz_id
        AND c.submitter_id = au.user_id
        AND au.user_id = pax.user_id
        AND au.user_id = ua.user_id
        AND pax.user_id = pe.user_id(+)
        AND pe.termination_date IS NULL
        AND p.promotion_status IN ('live','expired')               --Chidamba 06/29/2012 added start
        AND p.is_deleted = 0                                         
        AND ((v_start_date   <  a.date_created   AND a.date_created <= v_end_date)
             OR (v_start_date   <  c.submission_date   AND c.submission_date <= v_end_date)
             OR (v_start_date < au.date_created  AND au.date_created <= v_end_date)
             OR (v_start_date < au.date_modified AND au.date_modified <= v_end_date)
             OR (v_start_date < pe.date_created  AND pe.date_created <= v_end_date)
             OR (v_start_date < pe.date_modified AND pe.date_modified <= v_end_date)
             OR (v_start_date < p.date_created  AND p.date_created <= v_end_date)
             OR (v_start_date < p.date_modified AND p.date_modified <= v_end_date))
           UNION ALL --Fix for the bug # 52681
    SELECT
        c.node_id as node_id,
        a.activity_id, 
        a.date_created as award_date,
        n.name as node_name,
       q.name as promo_quiz_name,
      nvl(p.promotion_id,0) as promotion_id,
       nvl(p.award_type,' ') as award_type,
       p.is_taxable as is_taxable,
        q.name as quiz_name,
       dq.quiz_id as quiz_id, 
      qc.passing_score as quiz_passing_score,
      qc.score as quiz_score,
      qc.question_count as quiz_question_count,
       NVL(DECODE(qc.pass,1,'passed',0,'failed','progress'),' ') quiz_result,  --04/05/2016 Bug 66316   --04/13/2016  
        qc.claim_id as quiz_claim_id,
        c.submission_date as quiz_date,
       c.submitter_id as participant_id,
        ua.country_id as country_id,
        au.first_name as participant_first_name,
        au.middle_name as participant_middle_name,
               au.last_name as participant_last_name,
                nvl(DECODE(au.is_active,1,'active',0,'inactive'),' ') pax_status,
      nvl(pe.position_type,' ') as job_title,
       nvl(pe.department_type, ' ') as department,
        DECODE (ci.approval_status_type,'winner',1,0) AS sweepstakes_won        
     FROM activity a,
          node n,
         promotion p,        
          quiz q,
          claim c,
          quiz_claim qc,
          claim_item ci, --10/30/2012
          application_user au,
          user_address ua,
          participant pax,
          vw_curr_pax_employer pe,
          diy_quiz dq                                    --10/03/2013   
        --  participant_badge pb
      WHERE --a.activity_id = in_activity_id                      --Chidamba 06/29/2012 commented        
         c.claim_id = a.claim_id(+)
        AND qc.claim_id = c.claim_id
        AND qc.quiz_id = q.quiz_id 
        AND c.claim_id = ci.claim_id(+) 
        AND c.node_id = n.node_id(+) 
        AND c.promotion_id = p.promotion_id
        AND p.promotion_type ='diy_quiz'       
        AND P.promotion_id = dq.promotion_id           
        AND dq.quiz_id = q.quiz_id      
        AND c.submitter_id = au.user_id
        AND au.user_id = pax.user_id
        AND au.user_id = ua.user_id
        AND pax.user_id = pe.user_id(+)
        AND pe.termination_date IS NULL
        AND p.promotion_status IN ('live','expired')              
        AND p.is_deleted = 0                                         
        AND ((v_start_date   <  a.date_created   AND a.date_created <= v_end_date)
             OR (v_start_date   <  c.submission_date   AND c.submission_date <= v_end_date)
             OR (v_start_date < au.date_created  AND au.date_created <= v_end_date)
             OR (v_start_date < au.date_modified AND au.date_modified <= v_end_date)
             OR (v_start_date < pe.date_created  AND pe.date_created <= v_end_date)
             OR (v_start_date < pe.date_modified AND pe.date_modified <= v_end_date)
             OR (v_start_date < p.date_created  AND p.date_created <= v_end_date)
             OR (v_start_date < p.date_modified AND p.date_modified <= v_end_date));

  -- BugFix   13707.
  CURSOR cur_quiz_progress(v_start_date IN DATE, v_end_date IN DATE)  IS       
     SELECT  distinct 
        c.node_id as node_id,
        c.date_created as award_date,
        n.name as node_name,
        p.promotion_name as promo_quiz_name,
        nvl(p.promotion_id,0) as promotion_id,
        nvl(p.award_type,' ') as award_type,
        p.is_taxable as is_taxable,
        q.name as quiz_name,
        NVL(pq.quiz_id,dq.quiz_id) as quiz_id,      --10/03/2013
        qc.passing_score as quiz_passing_score,
        qc.score as quiz_score,
        qc.question_count as quiz_question_count,
        NVL(DECODE(c.is_open,1,'progress',0,'completed'),' ') quiz_result, --04/05/2016 Bug 66316 --04/13/2016
        qc.claim_id as quiz_claim_id,
        c.submission_date as quiz_date,
        c.submitter_id as participant_id,
        ua.country_id as country_id,
        au.first_name as participant_first_name,
        au.middle_name as participant_middle_name,
        NULL as badges_earned,
        au.last_name as participant_last_name,
    nvl(DECODE(au.is_active,1,'active',0,'inactive'),' ') pax_status,
        nvl(pe.position_type,' ') as job_title,
        nvl(pe.department_type, ' ') as department,
        DECODE (ci.approval_status_type,'winner',1,0) AS sweepstakes_won
      FROM
        claim c,
        claim_item ci, --10/30/2012
        quiz q,
        quiz_claim qc,
        promo_quiz pq,
        node n,
        promotion p,
        application_user au,
        user_address ua,
        participant pax,
        vw_curr_pax_employer pe,
        participant_badge pb,
        diy_quiz dq          
      WHERE  c.claim_id = qc.claim_id     
        AND  c.claim_id = ci.claim_id --10/30/2012   
        AND  c.node_id = n.node_id
        AND  c.is_open = 1
        AND  c.promotion_id = p.promotion_id
        AND  p.promotion_type  IN('quiz','diy_quiz')    --10/03/2013
        AND  p.promotion_status IN ('live','expired')      --03/21/2012
        AND  p.is_deleted = 0                              --03/21/2012
        AND  p.promotion_id = pq.promotion_id(+)        --10/03/2013    
        AND  P.promotion_id = dq.promotion_id(+)        --10/03/2013
        AND  NVL(pq.quiz_id,dq.quiz_id) = q.quiz_id     --10/03/2013
        AND  c.submitter_id = au.user_id
        AND  v_start_date < c.submission_date            --Chidamba 06/29/2012
        AND  c.submission_date <= v_end_date             --Chidamba 06/29/2012
        AND  au.user_id = pax.user_id
        AND  au.user_id = ua.user_id 
        AND  pax.user_id = pe.user_id(+)
        AND  pe.termination_date is null
        AND au.user_id = pb.participant_id(+)
        AND (v_start_date < au.date_created  AND au.date_created <= v_end_date
             OR v_start_date < au.date_modified AND au.date_modified <= v_end_date
             OR v_start_date < pe.date_created  AND pe.date_created <= v_end_date
             OR v_start_date < pe.date_modified AND pe.date_modified <= v_end_date
             OR v_start_date < p.date_created  AND p.date_created <= v_end_date
             OR v_start_date < p.date_modified AND p.date_modified <= v_end_date
             OR v_start_date < pb.date_created AND pb.date_created <= v_end_date);--Chidamba 06/29/2012 end

CURSOR cur_sweep_activity (v_start_date IN DATE, v_end_date IN DATE)  --(in_activity_id IN NUMBER)  --Chidamba 06/29/2012 alter cursor
     IS     
      SELECT
        un.node_id as node_id,
        a.activity_id, 
        a.date_created as award_date,
        n.name as node_name,
        p.promotion_name as promo_quiz_name,
        nvl(p.promotion_id,0) as promotion_id,
        nvl(p.award_type,' ') as award_type,
        p.is_taxable as is_taxable,
        q.name as quiz_name,
        NVL(pq.quiz_id,dq.quiz_id) as quiz_id,      --10/03/2013
        a.quantity as points,
        ua.country_id as country_id,
        au.user_id as participant_id,
        au.first_name as participant_first_name,
        au.middle_name as participant_middle_name,
        au.last_name as participant_last_name,
        nvl(DECODE(au.is_active,1,'active',0,'inactive'),' ') participant_status,
        nvl(pe.position_type,' ') as job_title,
        nvl(pe.department_type, ' ') as department,
        DECODE (a.activity_discrim,'sweep',1,'sweeplevel',1,0) AS sweepstakes_won --10/07/2013
     FROM activity a,
          node n,
          promotion p,
          promo_quiz pq,
          quiz q,
          application_user au,
          user_node un,
          user_address ua,
          participant pax,
          vw_curr_pax_employer pe,
          diy_quiz dq  
      WHERE a.activity_discrim IN ('sweep','sweeplevel') --10/07/2013
        AND a.user_id =un.user_id
        AND a.user_id =au.user_id
        AND un.is_primary =1 
        AND un.node_id = n.node_id
        AND a.promotion_id = p.promotion_id
        AND p.promotion_type  IN('quiz','diy_quiz')     --10/03/2013
        AND p.promotion_id = pq.promotion_id(+)        --10/03/2013    
        AND P.promotion_id = dq.promotion_id(+)        --10/03/2013
        AND NVL(pq.quiz_id,dq.quiz_id) = q.quiz_id     --10/03/2013
        AND au.user_id = pax.user_id
        AND au.user_id = ua.user_id
        AND pax.user_id = pe.user_id(+)
        AND pe.termination_date IS NULL
        AND p.promotion_status IN ('live','expired')            
        AND p.is_deleted = 0      
        AND (v_start_date   <  a.date_created   AND a.date_created <= v_end_date);
             

  CURSOR cur_reversed (v_start_date IN DATE, v_end_date IN DATE)  --04/04/2014 Bug # 51044
  IS
  SELECT rpt.*
  FROM rpt_quiz_activity_detail rpt, activity_journal aj, activity a,journal j
 WHERE     rpt.quiz_claim_id = a.claim_id
       AND a.activity_id = aj.activity_id
       AND j.is_reverse = 1 
       AND J.journal_id = aJ.journal_id
       AND (v_start_date   <  J.date_modified   AND J.date_modified <= v_end_date); --10/8/2014 Bug # 51044

  v_tab_node_id        dbms_sql.number_table;   --01/29/2015  
  v_tab_node_name      dbms_sql.varchar2_table;  --01/29/2015  
  
  --Cursor to pick modified node name   
  CURSOR cur_node_changed IS          --01/29/2015
    SELECT node_id,
           NAME
      FROM node
     WHERE date_modified BETWEEN p_in_start_date AND p_in_end_date;  
     
  
  BEGIN

      SELECT SYSDATE INTO v_date_created from dual;

    --FOR rec_activity IN cur_activity(p_in_start_date,p_in_end_date) LOOP        --ic add inparm
   v_stage := 'Looping Quiz Activity';
    FOR rec_quiz_activity IN cur_quiz_activity(p_in_start_date,p_in_end_date) --(rec_activity.activity_id)  --Chidamba 06/29/2012 added
    LOOP


        v_promo_quiz_name         := rec_quiz_activity.promo_quiz_name ;
        v_promotion_id            := rec_quiz_activity.promotion_id ;
        v_quiz_id                 := rec_quiz_activity.quiz_id ;
        v_quiz_date               := rec_quiz_activity.quiz_date ;
        v_quiz_passing_score      := rec_quiz_activity.quiz_passing_score ;
        v_quiz_score              := rec_quiz_activity.quiz_score ;
        v_quiz_question_count     := rec_quiz_activity.quiz_question_count ;
        v_quiz_result             := rec_quiz_activity.quiz_result ;
        v_quiz_name               := rec_quiz_activity.quiz_name ;
        v_quiz_claim_id           := rec_quiz_activity.quiz_claim_id ;
        v_participant_id          := rec_quiz_activity.participant_id ;
        v_country_id          := rec_quiz_activity.country_id ;
        v_participant_first_name  := rec_quiz_activity.participant_first_name ;
        v_participant_middle_name := rec_quiz_activity.participant_middle_name ;
        v_participant_last_name   := rec_quiz_activity.participant_last_name ;
        --v_badges_earned    := rec_quiz_activity.badges_earned; --10/25/2012
        v_participant_status      := rec_quiz_activity.pax_status;
        v_node_id                 := rec_quiz_activity.node_id;
        v_node_name               := rec_quiz_activity.node_name ;
        v_award_type              := rec_quiz_activity.award_type ;
        v_award_date              := rec_quiz_activity.award_date ;
        v_is_taxable              := rec_quiz_activity.is_taxable ;
        v_job_title               := rec_quiz_activity.job_title ;
        v_department              := rec_quiz_activity.department;
        v_sweepstakes_won  := rec_quiz_activity.sweepstakes_won;

        SELECT NVL(SUM(transaction_amt), 0)
          INTO v_award_amount
          FROM journal
         WHERE journal_id IN(SELECT distinct( journal_id )
                                FROM activity_journal aj, activity a
                               WHERE a.claim_id in (SELECT claim_id
                                                        FROM claim
                                                       WHERE node_id = rec_quiz_activity.node_id
                                                         AND claim_id = rec_quiz_activity.quiz_claim_id) -- added by iyer,Satish 05/16/2007 -- to fix bug of awarding points 
                                                         AND aj.activity_id = a.activity_id -- for failed quizzes 
                                                         AND a.promotion_id in (SELECT promotion_id
                                                                                  FROM promotion
                                                                                 WHERE promotion_type = 'quiz')
                                                                                   AND a.node_id = rec_quiz_activity.node_id)
          AND user_id = v_participant_id          
          AND promotion_id = v_promotion_id;
          
        SELECT count(*)                                                           --Chidamba 06/29/2012 Start customization           
          INTO v_rec_count    
          FROM rpt_quiz_activity_detail 
         WHERE participant_id = rec_quiz_activity.participant_id
           AND promotion_id   = rec_quiz_activity.promotion_id
           AND quiz_claim_id  = rec_quiz_activity.quiz_claim_id;        
       
        IF v_rec_count = 1 THEN
            v_stage := 'Update Quiz Activity detail';
             UPDATE rpt_quiz_activity_detail
                SET activity_id     = rec_quiz_activity.activity_id ,
                    promotion_id    = v_promotion_id ,
                    promo_quiz_name = v_promo_quiz_name ,
                    quiz_id         = v_quiz_id ,
                    quiz_name       = v_quiz_name,
                    node_id         = v_node_id,
                    node_name       = v_node_name,
                    is_taxable      = v_is_taxable,
                    award_type      = v_award_type,
                    award_amount    = v_award_amount, 
                    award_date      = v_award_date,
                    quiz_date                = v_quiz_date,
                    participant_id           = v_participant_id,
                    country_id                =   v_country_id,
                    participant_first_name   = v_participant_first_name,
                    participant_middle_name  = v_participant_middle_name,
                    participant_last_name    = v_participant_last_name,
                    participant_status       = v_participant_status,
                    quiz_passing_score       = v_quiz_passing_score,
                    quiz_score               = v_quiz_score,
                    quiz_question_count      = v_quiz_question_count,
                    quiz_result              = v_quiz_result,
                    job_title                = v_job_title,
                    department               = v_department,
                    date_modified            = SYSDATE,
                    modified_by              = v_created_by,
                   -- badges_earned   = v_badges_earned,
                    sweepstakes_won = v_sweepstakes_won
              WHERE participant_id = rec_quiz_activity.participant_id
                AND promotion_id   = rec_quiz_activity.promotion_id
                AND quiz_claim_id  = rec_quiz_activity.quiz_claim_id;
      
        ELSE                                                             --Chidamba 06/29/2012 End customization
                        
            SELECT RPT_QUIZ_ACTIVITY_DETAIL_SQ_PK.nextval
              INTO v_rpt_quiz_activity_detail_id
              FROM DUAL;  
          v_stage := 'Insert Quiz Activity detail'; 
          INSERT INTO rpt_quiz_activity_detail(rpt_quiz_activity_detail_id,
                                              activity_id,
                                              promotion_id,
                                              promo_quiz_name,
                                              quiz_id,
                                              quiz_name,
                                              quiz_claim_id,
                                              node_id,
                                              node_name,
                                              is_taxable,
                                              award_type,
                                              award_amount,
                                              award_date,
                                              quiz_date,
                                              participant_id,
                                              country_id,
                                              participant_first_name,
                                              participant_middle_name,
                                              participant_last_name,
                                              participant_status,
                                              quiz_passing_score,
                                              quiz_score,
                                              quiz_question_count,
                                              quiz_result,
                                              job_title,
                                              department,
                                              date_created,
                                              created_by,
                                              badges_earned, --10/25/2012
                                              sweepstakes_won)--10/30/2012
                                      VALUES (v_rpt_quiz_activity_detail_id,
                                              rec_quiz_activity.activity_id,
                                              v_promotion_id,
                                              v_promo_quiz_name,
                                              v_quiz_id,
                                              v_quiz_name,
                                              v_quiz_claim_id,
                                              v_node_id,
                                              v_node_name,
                                              v_is_taxable,
                                              v_award_type,
                                              v_award_amount,
                                              v_award_date,
                                              v_quiz_date,
                                              v_participant_id,
                                              v_country_id,
                                              v_participant_first_name,
                                              v_participant_middle_name,
                                              v_participant_last_name,
                                              v_participant_status,
                                              v_quiz_passing_score,
                                              v_quiz_score,
                                              v_quiz_question_count,
                                              v_quiz_result,
                                              v_job_title,
                                              v_department,
                                              v_date_created,
                                              v_created_by,
                                              NULL, --10/25/2012
                                              v_sweepstakes_won);--10/30/2012
        
        END IF;
        
    END LOOP;

  --  END LOOP;
    -- BugFix   13707.  
    v_stage := 'Insert Quiz progess';
    FOR rec_quiz_progress in cur_quiz_progress(p_in_start_date,p_in_end_date) LOOP  --ic  --Chidamba 06/29/2012 start customization
   
            v_promo_quiz_name         := rec_quiz_progress.promo_quiz_name ;
            v_promotion_id            := rec_quiz_progress.promotion_id ;
            v_quiz_id                 := rec_quiz_progress.quiz_id ;
            v_quiz_date               := rec_quiz_progress.quiz_date ;
            v_quiz_passing_score      := rec_quiz_progress.quiz_passing_score ;
            v_quiz_score              := rec_quiz_progress.quiz_score ;
            v_quiz_question_count     := rec_quiz_progress.quiz_question_count ;
            v_quiz_result             := rec_quiz_progress.quiz_result ;
            v_quiz_name               := rec_quiz_progress.quiz_name ;
            v_quiz_claim_id           := rec_quiz_progress.quiz_claim_id ;
            v_participant_id          := rec_quiz_progress.participant_id ;
            v_country_id          := rec_quiz_progress.country_id ;
            v_participant_first_name  := rec_quiz_progress.participant_first_name ;
            v_participant_middle_name := rec_quiz_progress.participant_middle_name ;
            v_participant_last_name   := rec_quiz_progress.participant_last_name ;
            v_participant_status      := rec_quiz_progress.pax_status;
            v_node_id                 := rec_quiz_progress.node_id ;
            v_node_name               := rec_quiz_progress.node_name ;
            v_award_type              := rec_quiz_progress.award_type ;
            v_award_date              := rec_quiz_progress.award_date ;
            v_is_taxable              := rec_quiz_progress.is_taxable ;
            v_job_title               := rec_quiz_progress.job_title ;
            v_department              := rec_quiz_progress.department;
           -- v_badges_earned    := rec_quiz_progress.badges_earned;--10/25/2012
            v_sweepstakes_won    := rec_quiz_progress.sweepstakes_won;--10/25/2012
            v_award_amount            := 0;   
        
        SELECT count(*)
          INTO v_rec_count    
          FROM rpt_quiz_activity_detail 
         WHERE participant_id = rec_quiz_progress.participant_id
           AND promotion_id   = rec_quiz_progress.promotion_id
           AND quiz_claim_id  = rec_quiz_progress.quiz_claim_id;        
       
       IF v_rec_count = 1 THEN
         
         SELECT *
           INTO v_rpt_quiz_activity_dtl    
           FROM rpt_quiz_activity_detail 
          WHERE participant_id = rec_quiz_progress.participant_id
            AND promotion_id   = rec_quiz_progress.promotion_id
            AND quiz_claim_id  = rec_quiz_progress.quiz_claim_id;
            
          IF lower(v_rpt_quiz_activity_dtl.quiz_result) = 'progress' THEN
             v_stage := 'Insert Quiz Activity detail progress';
             UPDATE rpt_quiz_activity_detail
                SET activity_id     = 0,
                    promotion_id    = v_promotion_id ,
                    promo_quiz_name = v_promo_quiz_name ,
                    quiz_id         = v_quiz_id ,
                    quiz_name       = v_quiz_name,
                    node_id         = v_node_id,
                    node_name       = v_node_name,
                    is_taxable      = v_is_taxable,
                    award_type      = v_award_type,
                    award_amount    = v_award_amount, 
                    award_date      = v_award_date,
                    quiz_date                = v_quiz_date,
                    participant_id           = v_participant_id,
                    country_id               = v_country_id,
                    participant_first_name   = v_participant_first_name,
                    participant_middle_name  = v_participant_middle_name,
                    participant_last_name    = v_participant_last_name,
                    participant_status       = v_participant_status,
                    quiz_passing_score       = v_quiz_passing_score,
                    quiz_score               = v_quiz_score,
                    quiz_question_count      = v_quiz_question_count,
                    quiz_result              = v_quiz_result,
                    job_title                = v_job_title,
                    department               = v_department,
                    date_modified            = SYSDATE,
                    modified_by               = v_created_by,
                 --   badges_earned     = v_badges_earned, --10/25/2012
                    sweepstakes_won =v_sweepstakes_won --10/30/2012
              WHERE participant_id = rec_quiz_progress.participant_id
                AND promotion_id   = rec_quiz_progress.promotion_id
                AND quiz_claim_id  = rec_quiz_progress.quiz_claim_id;                 
          END IF ;
      
        ELSE                                                                     --Chidamba 06/29/2012 end customization
            SELECT RPT_QUIZ_ACTIVITY_DETAIL_SQ_PK.nextval
            INTO v_rpt_quiz_activity_detail_id
            FROM DUAL;
            v_stage := 'Insert Quiz Activity detail if not progress'; 
            INSERT INTO rpt_quiz_activity_detail 
                       (rpt_quiz_activity_detail_id,
                        activity_id,
                        promotion_id,
                        promo_quiz_name,
                        quiz_id,
                        quiz_name,
                        quiz_claim_id,
                        node_id,
                        node_name,
                        is_taxable,
                        award_type,
                        award_amount,
                        award_date,
                        quiz_date,
                        participant_id,
                        country_id,
                        participant_first_name,
                        participant_middle_name,
                        participant_last_name,
                        participant_status,
                        quiz_passing_score,
                        quiz_score,
                        quiz_question_count,
                        quiz_result,
                        job_title,
                        department,
                        date_created,
                        created_by,
                        badges_earned, --10/25/2012
                        sweepstakes_won)--10/30/2012
                VALUES (v_rpt_quiz_activity_detail_id,
                        0,
                        v_promotion_id,
                        v_promo_quiz_name,
                        v_quiz_id,
                        v_quiz_name,
                        v_quiz_claim_id,
                        v_node_id,
                        v_node_name,
                        v_is_taxable,
                        v_award_type,
                        v_award_amount,
                        v_award_date,
                        v_quiz_date,
                        v_participant_id,
                        v_country_id,
                        v_participant_first_name,
                        v_participant_middle_name,
                        v_participant_last_name,
                        v_participant_status,
                        v_quiz_passing_score,
                        v_quiz_score,
                        v_quiz_question_count,
                        v_quiz_result,
                        v_job_title,
                        v_department,
                        v_date_created,
                        v_created_by,
                        NULL, --10/25/2012
                        v_sweepstakes_won);--10/30/2012
       
        END IF;
        
    END LOOP;
    
    FOR rec_sweep_activity IN cur_sweep_activity(p_in_start_date,p_in_end_date) 
    LOOP
    INSERT INTO rpt_quiz_activity_detail 
                       (rpt_quiz_activity_detail_id,
                        activity_id,
                        promotion_id,
                        promo_quiz_name,
                        quiz_id,
                        quiz_name,
                        quiz_claim_id,
                        node_id,
                        node_name,
                        is_taxable,
                        award_type,
                        award_amount,
                        award_date,
                        quiz_date,
                        participant_id,
                        country_id,
                        participant_first_name,
                        participant_middle_name,
                        participant_last_name,
                        participant_status,
                        quiz_passing_score,
                        quiz_score,
                        quiz_question_count,
                        quiz_result,
                        job_title,
                        department,
                        date_created,
                        created_by,
                        badges_earned, --10/25/2012
                        sweepstakes_won)--10/30/2012
                VALUES (RPT_QUIZ_ACTIVITY_DETAIL_SQ_PK.nextval,
                        rec_sweep_activity.activity_id,
                        rec_sweep_activity.promotion_id,
                        rec_sweep_activity.promo_quiz_name,
                        rec_sweep_activity.quiz_id,
                        rec_sweep_activity.quiz_name,
                        NULL,
                        rec_sweep_activity.node_id,
                        rec_sweep_activity.node_name,
                        rec_sweep_activity.is_taxable,
                        rec_sweep_activity.award_type,
                        rec_sweep_activity.points,
                        rec_sweep_activity.award_date,
                        rec_sweep_activity.award_date,
                        rec_sweep_activity.participant_id,
                        rec_sweep_activity.country_id,
                        rec_sweep_activity.participant_first_name,
                        rec_sweep_activity.participant_middle_name,
                        rec_sweep_activity.participant_last_name,
                        rec_sweep_activity.participant_status,
                        NULL,
                        NULL,
                        NULL,
                        NULL,
                        rec_sweep_activity.job_title,
                        rec_sweep_activity.department,
                        SYSDATE,
                        v_created_by,
                        NULL, --10/25/2012
                        rec_sweep_activity.sweepstakes_won);--10/30/2012
        
    END LOOP;
    
    FOR rec_reversed IN cur_reversed(p_in_start_date,p_in_end_date)  LOOP --04/04/2014

v_stage := 'Deduct Summary record for reversed journals';

UPDATE rpt_quiz_activity_summary
SET award_given = award_given-rec_reversed.award_amount
WHERE
   node_id = rec_reversed.node_id
   AND promotion_id     = rec_reversed.promotion_id
             AND participant_status       = rec_reversed.participant_status
             AND job_title     = rec_reversed.job_title
             AND department       = rec_reversed.department;
             
v_stage := 'Remove award amount from detail record for reversed journals';

UPDATE rpt_quiz_activity_detail
SET award_amount = award_amount-rec_reversed.award_amount,
date_modified = SYSDATE
WHERE
   rpt_quiz_activity_detail_id = rec_reversed.rpt_quiz_activity_detail_id;

END LOOP;  
     
  v_stage := 'Open and Fetch cursor to pick modified node name';      --01/29/2015
  OPEN cur_node_changed;   --01/29/2015
  FETCH cur_node_changed BULK COLLECT
   INTO v_tab_node_id,
        v_tab_node_name;
  CLOSE cur_node_changed;   

  v_stage := 'Update modified node name in rpt table';    --01/29/2015
  FORALL indx IN v_tab_node_id.FIRST .. v_tab_node_id.LAST    --01/29/2015
    UPDATE rpt_quiz_activity_detail
       SET node_name     = DECODE (node_id, v_tab_node_id(indx), v_tab_node_name(indx), node_name),
           date_modified = SYSDATE,
           modified_by   = p_in_requested_user_id,
           VERSION       = VERSION + 1
     WHERE (node_id        = v_tab_node_id(indx)
            AND node_name != v_tab_node_name(indx)
            );

   v_stage := 'Success';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
   
   p_out_return_code := 0;

EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code   := 99;
   p_out_error_message := v_stage ||'  '|| SQLERRM;   
   prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ': ' || SQLCODE || ', ' || SQLERRM, NULL);
END; 


FUNCTION FNC_ELIGIBILITY_COUNT
  ( p_in_promotion_id IN NUMBER,
    p_in_node_id      IN NUMBER,
--    p_in_giver_recvr  IN VARCHAR2,
    p_in_record_type  IN VARCHAR2,
    p_in_pax_status   IN VARCHAR2,
    p_in_job_code     IN VARCHAR2,
    p_in_department   IN VARCHAR2,
    p_in_promo_type   IN VARCHAR2 DEFAULT 'quiz')
  RETURN  NUMBER IS
/*******************************************************************************

   Purpose:  Get the number of eligible givers or eligible receivers for a given
             promotion and a given node from the rpt_pax_promo_eligibility table.

   Person        Date       Comments
   ----------- ---------- -----------------------------------------------------
   D Murray    11/21/2005  Initial Creation
   Raju N      06/01/2006 join condtion for pax and pax emplyer to avoid the
                          cartersian product. Added to logic to pick up the most
                          recent employment record by participant_employer_index.
*******************************************************************************/
   v_eligible_cnt     NUMBER;

BEGIN
   IF p_in_record_type = 'team' THEN -- team or node
      SELECT   count(distinct(r.promotion_id)) *  COUNT(DISTINCT r.participant_id)
      INTO   v_eligible_cnt
      FROM   rpt_pax_promo_eligibility r,
               PARTICIPANT p
      WHERE  r.promotion_id = NVL(p_in_promotion_id,r.promotion_id)
        AND r.node_id = p_in_node_id
        AND p.user_id = r.participant_id
        AND p.status = NVL(p_in_pax_status,p.status)        
--        AND participant_id IN (SELECT pax.user_id
--                                 FROM PARTICIPANT pax,
--                                      PARTICIPANT_EMPLOYER pe
--                                WHERE pax.user_id = pe.user_id
--                                  AND pax.status          = NVL(p_in_pax_status,pax.status)
--                                  AND pe.position_type    = NVL(p_in_job_code,pe.position_type)
--                                  AND pe.department_type  = NVL(p_in_department,pe.department_type)
--                                  AND pe.participant_employer_index =
--                                     (SELECT max(pe1.participant_employer_index)
--                                        FROM participant_employer pe1
--                                       WHERE pe1.user_id = pe.user_id))
        AND r.promotion_id IN ( SELECT promotion_id FROM promotion WHERE promotion_type = nvl(p_in_promo_type,promotion_type));
   ELSE -- team or node
          SELECT   count(distinct(r.promotion_id)) * COUNT(DISTINCT r.participant_id)
          INTO   v_eligible_cnt
          FROM   rpt_pax_promo_eligibility r,
               PARTICIPANT p
          WHERE  r.promotion_id = nvl(p_in_promotion_id,r.promotion_id)
          AND r.node_id IN
             (SELECT node_id
                FROM rpt_hierarchy
             CONNECT BY prior node_id = parent_node_id
               START WITH node_id = p_in_node_id)
        AND p.user_id = r.participant_id
        AND p.status = NVL(p_in_pax_status,p.status)                       
--        AND participant_id IN (SELECT pax.user_id
--                                 FROM PARTICIPANT pax,
--                                      PARTICIPANT_EMPLOYER pe
--                                WHERE pax.user_id = pe.user_id
--                                  AND pax.status  = NVL(p_in_pax_status,pax.status)
--                                  AND pe.position_type = NVL(p_in_job_code,pe.position_type)
--                                  AND pe.department_type = NVL(p_in_department,pe.department_type)
--                                  AND pe.participant_employer_index =
--                                     (SELECT max(pe1.participant_employer_index)
--                                        FROM participant_employer pe1
--                                       WHERE pe1.user_id = pe.user_id))
        AND r.promotion_id IN ( SELECT promotion_id FROM promotion WHERE promotion_type = nvl(p_in_promo_type,promotion_type));
   END IF ; -- team or node
   RETURN v_eligible_cnt;
EXCEPTION
   WHEN others THEN
      -- ('FNC_ELIGIBILITY_COUNT  promo: '||p_in_promotion_id||' node: '||p_in_node_id||'  '||
                 --           '  '||SQLERRM);
       v_eligible_cnt := 0;
       RETURN v_eligible_cnt;
END; -- FNC_ELIGIBILITY_COUNT


FUNCTION FNC_GET_QUIZ_COUNTS
  ( p_in_node_id      IN NUMBER,
    p_in_node_name    IN VARCHAR2,
    p_in_promotion_id IN NUMBER,
    p_in_from_date    IN DATE,
    p_in_to_date      IN DATE,
    p_in_award_type   IN VARCHAR2,
    p_in_pax_status   IN VARCHAR2,
    p_in_job_title    IN VARCHAR2,
    p_in_department   IN VARCHAR2,
    p_in_count_type   IN VARCHAR2,
    p_in_quiz_date    IN DATE DEFAULT NULL)
  RETURN  NUMBER IS

v_return_value    NUMBER;

BEGIN

IF UPPER(p_in_count_type) = 'ATTEMPTS' THEN
  IF lower(p_in_node_name) LIKE '%team%' THEN
    SELECT COUNT(*)
      into v_return_value
    FROM rpt_quiz_activity_detail rqad
    WHERE rqad.node_id      = p_in_node_id
      AND nvl(TRUNC(rqad.quiz_date),trunc(sysdate)) BETWEEN p_in_from_date AND p_in_to_date
      AND rqad.promotion_id  = NVL(p_in_promotion_id, rqad.promotion_id)
      AND DECODE(rqad.award_type,' ', p_in_award_type,rqad.award_type) = NVL(p_in_award_type,rqad.award_type)
--      AND DECODE(rqad.participant_status,' ',p_in_pax_status,rqad.participant_status) = p_in_pax_status
      AND rqad.participant_status = NVL(p_in_pax_status,rqad.participant_status)
      AND rqad.job_title     = NVL(p_in_job_title,rqad.job_title)
      AND rqad.department    = NVL(p_in_department,rqad.department)
      AND TRUNC(rqad.quiz_date)     = TRUNC(NVL(p_in_quiz_date,rqad.quiz_date))
      ;
  ELSE
    SELECT COUNT(*)
      into v_return_value
    FROM rpt_quiz_activity_detail rqad
    WHERE rqad.node_id IN (SELECT rh.node_id
                        FROM rpt_hierarchy rh
                       CONNECT BY prior rh.node_id = rh.parent_node_id
                         START WITH rh.node_id = p_in_node_id)
      AND nvl(TRUNC(rqad.quiz_date),trunc(sysdate)) BETWEEN p_in_from_date AND p_in_to_date
      AND rqad.promotion_id  = NVL(p_in_promotion_id, rqad.promotion_id)
      AND DECODE(rqad.award_type,' ', p_in_award_type,rqad.award_type) = NVL(p_in_award_type,rqad.award_type)
--      AND DECODE(rqad.participant_status,' ',p_in_pax_status,rqad.participant_status) = p_in_pax_status
      AND rqad.participant_status = NVL(p_in_pax_status,rqad.participant_status)
      AND rqad.job_title     = NVL(p_in_job_title,rqad.job_title)
      AND rqad.department    = NVL(p_in_department,rqad.department)
      AND TRUNC(rqad.quiz_date)     = TRUNC(NVL(p_in_quiz_date,rqad.quiz_date))
      ;
   END IF;

ELSIF UPPER(p_in_count_type) = 'PASSED' THEN
  IF lower(p_in_node_name) LIKE '%team%' THEN
    SELECT nvl(SUM(DECODE(rqad.quiz_result,'passed',1,0)),0)
      into v_return_value
    FROM rpt_quiz_activity_detail rqad
    WHERE rqad.node_id = p_in_node_id
      AND nvl(TRUNC(rqad.quiz_date),trunc(sysdate)) BETWEEN p_in_from_date AND p_in_to_date
      AND rqad.promotion_id  = NVL(p_in_promotion_id, rqad.promotion_id)
      AND DECODE(rqad.award_type,' ', p_in_award_type,rqad.award_type) = NVL(p_in_award_type,rqad.award_type)
--      AND DECODE(rqad.participant_status,' ',p_in_pax_status,rqad.participant_status) = p_in_pax_status
      AND rqad.participant_status = NVL(p_in_pax_status,rqad.participant_status)
      AND rqad.job_title     = NVL(p_in_job_title,rqad.job_title)
      AND rqad.department    = NVL(p_in_department,rqad.department)
      AND TRUNC(rqad.quiz_date)     = TRUNC(NVL(p_in_quiz_date,rqad.quiz_date))
      ;
  ELSE
    SELECT nvl(SUM(DECODE(rqad.quiz_result,'passed',1,0)),0)
      into v_return_value
    FROM rpt_quiz_activity_detail rqad
    WHERE rqad.node_id IN (SELECT rh.node_id
                        FROM rpt_hierarchy rh
                       CONNECT BY prior rh.node_id = rh.parent_node_id
                         START WITH rh.node_id = p_in_node_id)
      AND nvl(TRUNC(rqad.quiz_date),trunc(sysdate)) BETWEEN p_in_from_date AND p_in_to_date
      AND rqad.promotion_id  = NVL(p_in_promotion_id, rqad.promotion_id)
      AND DECODE(rqad.award_type,' ', p_in_award_type,rqad.award_type) = NVL(p_in_award_type,rqad.award_type)
--      AND DECODE(rqad.participant_status,' ',p_in_pax_status,rqad.participant_status) = p_in_pax_status
      AND rqad.participant_status = NVL(p_in_pax_status,rqad.participant_status)
      AND rqad.job_title     = NVL(p_in_job_title,rqad.job_title)
      AND rqad.department    = NVL(p_in_department,rqad.department)
      AND TRUNC(rqad.quiz_date)     = TRUNC(NVL(p_in_quiz_date,rqad.quiz_date))
      ;
   END IF;

ELSIF UPPER(p_in_count_type) = 'FAILED' THEN
  IF lower(p_in_node_name) LIKE '%team%' THEN
    SELECT nvl(SUM(DECODE(rqad.quiz_result,'failed',1,0)),0)
      into v_return_value
    FROM rpt_quiz_activity_detail rqad
    WHERE rqad.node_id = p_in_node_id
      AND nvl(TRUNC(rqad.quiz_date),trunc(sysdate)) BETWEEN p_in_from_date AND p_in_to_date
      AND rqad.promotion_id  = NVL(p_in_promotion_id, rqad.promotion_id)
      AND DECODE(rqad.award_type,' ', p_in_award_type,rqad.award_type) = NVL(p_in_award_type,rqad.award_type)
--      AND DECODE(rqad.participant_status,' ',p_in_pax_status,rqad.participant_status) = p_in_pax_status
      AND rqad.participant_status = NVL(p_in_pax_status,rqad.participant_status)
      AND rqad.job_title     = NVL(p_in_job_title,rqad.job_title)
      AND rqad.department    = NVL(p_in_department,rqad.department)
      AND TRUNC(rqad.quiz_date)     = TRUNC(NVL(p_in_quiz_date,rqad.quiz_date))
      ;
  ELSE
    SELECT nvl(SUM(DECODE(rqad.quiz_result,'failed',1,0)),0)
      into v_return_value
    FROM rpt_quiz_activity_detail rqad
    WHERE rqad.node_id IN (SELECT rh.node_id
                        FROM rpt_hierarchy rh
                       CONNECT BY prior rh.node_id = rh.parent_node_id
                         START WITH rh.node_id = p_in_node_id)
      AND nvl(TRUNC(rqad.quiz_date),trunc(sysdate)) BETWEEN p_in_from_date AND p_in_to_date
      AND rqad.promotion_id  = NVL(p_in_promotion_id, rqad.promotion_id)
      AND DECODE(rqad.award_type,' ', p_in_award_type,rqad.award_type) = NVL(p_in_award_type,rqad.award_type)
--      AND DECODE(rqad.participant_status,' ',p_in_pax_status,rqad.participant_status) = p_in_pax_status
      AND rqad.participant_status = NVL(p_in_pax_status,rqad.participant_status)
      AND rqad.job_title     = NVL(p_in_job_title,rqad.job_title)
      AND rqad.department    = NVL(p_in_department,rqad.department)
      AND TRUNC(rqad.quiz_date)     = TRUNC(NVL(p_in_quiz_date,rqad.quiz_date))
      ;
   END IF;

ELSIF UPPER(p_in_count_type) = 'AWARD_AMT' THEN
  IF lower(p_in_node_name) LIKE '%team%' THEN
    SELECT nvl(SUM(nvl(rqad.award_amount,0)),0)
      into v_return_value
    FROM rpt_quiz_activity_detail rqad
    WHERE rqad.node_id = p_in_node_id
      AND lower(rqad.quiz_result) = LOWER('passed')
      AND nvl(TRUNC(rqad.quiz_date),trunc(sysdate)) BETWEEN p_in_from_date AND p_in_to_date
      AND rqad.promotion_id  = NVL(p_in_promotion_id, rqad.promotion_id)
      AND DECODE(rqad.award_type,' ', p_in_award_type,rqad.award_type) = NVL(p_in_award_type,rqad.award_type)
--      AND DECODE(rqad.participant_status,' ',p_in_pax_status,rqad.participant_status) = p_in_pax_status
      AND rqad.participant_status = NVL(p_in_pax_status,rqad.participant_status)
      AND rqad.job_title     = NVL(p_in_job_title,rqad.job_title)
      AND rqad.department    = NVL(p_in_department,rqad.department)
      AND TRUNC(rqad.quiz_date)     = TRUNC(NVL(p_in_quiz_date,rqad.quiz_date))
      ;
  ELSE
    SELECT nvl(SUM(nvl(award_amount,0)),0)
      into v_return_value
    FROM rpt_quiz_activity_detail rqad
    WHERE rqad.node_id IN (SELECT rh.node_id
                        FROM rpt_hierarchy rh
                       CONNECT BY prior rh.node_id = rh.parent_node_id
                         START WITH rh.node_id = p_in_node_id)
      AND lower(rqad.quiz_result) = LOWER('passed')
      AND nvl(TRUNC(rqad.quiz_date),trunc(sysdate)) BETWEEN p_in_from_date AND p_in_to_date
      AND rqad.promotion_id  = NVL(p_in_promotion_id, rqad.promotion_id)
      AND DECODE(rqad.award_type,' ', p_in_award_type,rqad.award_type) = NVL(p_in_award_type,rqad.award_type)
--      AND DECODE(rqad.participant_status,' ',p_in_pax_status,rqad.participant_status) = p_in_pax_status
      AND rqad.participant_status = NVL(p_in_pax_status,rqad.participant_status)
      AND rqad.job_title     = NVL(p_in_job_title,rqad.job_title)
      AND rqad.department    = NVL(p_in_department,rqad.department)
      AND TRUNC(rqad.quiz_date)     = TRUNC(NVL(p_in_quiz_date,rqad.quiz_date))
      ;
   END IF;
ELSIF UPPER(p_in_count_type) = 'IN_PROGRESS' THEN   -- BugFix   13707.
  IF lower(p_in_node_name) LIKE '%team%' THEN
    SELECT nvl(SUM(DECODE(rqad.quiz_result,'progress',1,0)),0)
      into v_return_value
    FROM rpt_quiz_activity_detail rqad
    WHERE rqad.node_id = p_in_node_id
      AND nvl(TRUNC(rqad.quiz_date),trunc(sysdate)) BETWEEN p_in_from_date AND p_in_to_date
      AND rqad.promotion_id  = NVL(p_in_promotion_id, rqad.promotion_id)
      AND DECODE(rqad.award_type,' ', p_in_award_type,rqad.award_type) = NVL(p_in_award_type,rqad.award_type)
--      AND DECODE(rqad.participant_status,' ',p_in_pax_status,rqad.participant_status) = p_in_pax_status
      AND rqad.participant_status = NVL(p_in_pax_status,rqad.participant_status)
      AND rqad.job_title     = NVL(p_in_job_title,rqad.job_title)
      AND rqad.department    = NVL(p_in_department,rqad.department)
      AND TRUNC(rqad.quiz_date)     = TRUNC(NVL(p_in_quiz_date,rqad.quiz_date))
      ;
  ELSE
    SELECT nvl(SUM(DECODE(rqad.quiz_result,'progress',1,0)),0)
      into v_return_value
    FROM rpt_quiz_activity_detail rqad
    WHERE rqad.node_id IN (SELECT rh.node_id
                        FROM rpt_hierarchy rh
                       CONNECT BY prior rh.node_id = rh.parent_node_id
                         START WITH rh.node_id = p_in_node_id)
      AND nvl(TRUNC(rqad.quiz_date),trunc(sysdate)) BETWEEN p_in_from_date AND p_in_to_date
      AND rqad.promotion_id  = NVL(p_in_promotion_id, rqad.promotion_id)
      AND DECODE(rqad.award_type,' ', p_in_award_type,rqad.award_type) = NVL(p_in_award_type,rqad.award_type)
--      AND DECODE(rqad.participant_status,' ',p_in_pax_status,rqad.participant_status) = p_in_pax_status
      AND rqad.participant_status = NVL(p_in_pax_status,rqad.participant_status)
      AND rqad.job_title     = NVL(p_in_job_title,rqad.job_title)
      AND rqad.department    = NVL(p_in_department,rqad.department)
      AND TRUNC(rqad.quiz_date)     = TRUNC(NVL(p_in_quiz_date,rqad.quiz_date))
      ;
   END IF;

END IF;

RETURN   v_return_value;

END;



FUNCTION FNC_GET_PERIOD_DATE
  ( p_in_date         IN DATE,
    p_in_period_type  IN VARCHAR2)
  RETURN VARCHAR2 IS

v_period_range    VARCHAR2(100);
v_ind             calendar.week_ind%TYPE;

BEGIN

    SELECT distinct DECODE(LOWER(p_in_period_type),'weekly',week_ind,
                                                   'monthly',month_ind,
                                                   'quarterly', quarter_ind,
                                                   'annually', null)
      into v_ind
    FROM calendar
    WHERE TRUNC(time_key) = TRUNC(p_in_date);
    
    SELECT DECODE(LOWER(p_in_period_type),'weekly', min(weekdaybeg)||' - '||max(weekdayend),
                                                    min(time_key)||' - '||max(time_key))
      into v_period_range
    FROM calendar
    WHERE DECODE(LOWER(p_in_period_type),'weekly',week_ind,
                                         'monthly',month_ind,
                                         'quarterly', quarter_ind,
                                         'annually', null)  =   V_IND
      AND to_char(time_key,'yyyy') = to_char(p_in_date,'yyyy');
      

    RETURN  v_period_range;

EXCEPTION WHEN OTHERS THEN
  v_period_range:=NULL;
END;


FUNCTION FNC_ELIGIBILE_QUIZZES
  ( p_in_promotion_id IN NUMBER,
    p_in_user_id      IN NUMBER,
    p_in_node_id      IN NUMBER    )
  RETURN  NUMBER IS

v_elligible_quizzes NUMBER;

BEGIN
     SELECT COUNT(*)
            INTO v_elligible_quizzes
     FROM rpt_pax_promo_eligibility a, promo_quiz b
     WHERE participant_id = p_in_user_id
       AND node_id        = p_in_node_id
       AND a.promotion_id = b.promotion_id
       AND a.promotion_id = NVL(p_in_promotion_id, a.promotion_id)
       ;
     RETURN v_elligible_quizzes;
     
EXCEPTION WHEN OTHERS THEN
     RETURN NULL ;

END;

END;
/
