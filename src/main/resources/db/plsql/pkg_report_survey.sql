CREATE OR REPLACE PACKAGE pkg_report_survey IS

/*-----------------------------------------------------------------------------
   Purpose : This package is created to report on Survey module for G5.

  Person                   Date                         Comments
  -----------             ----------             ------------------------------------------------
 Ravi Dhanekula           12/11/2013               Intial Creation
                                    01/10/2014              Added procedure prc_rpt_survey_minimum_resp to poupulate table    
                                                                   rpt_survey_minimum_response based on the system variable 'survey.report.response.count'                                                                  
-----------------------------------------------------------------------------*/

PROCEDURE prc_rpt_survey_detail
(p_in_requested_user_id      IN  NUMBER,
  p_out_return_code           OUT NUMBER,
  p_out_error_message         OUT VARCHAR2);
  
  PROCEDURE prc_rpt_survey_response_detail
(p_in_requested_user_id      IN  NUMBER,
  p_in_start_date             IN  DATE,
  p_in_end_date               IN  DATE,
  p_out_return_code           OUT NUMBER,
  p_out_error_message         OUT VARCHAR2);
  
PROCEDURE prc_rpt_survey_summary
(p_in_requested_user_id      IN  NUMBER,
   p_out_return_code           OUT NUMBER,
   p_out_error_message         OUT VARCHAR2);
   
PROCEDURE prc_rpt_survey_response_sum
(p_in_requested_user_id      IN  NUMBER,
   p_out_return_code           OUT NUMBER,
   p_out_error_message         OUT VARCHAR2);
   
  PROCEDURE prc_rpt_survey_openend_resp
(p_in_requested_user_id      IN  NUMBER,
  p_in_start_date             IN  DATE,
  p_in_end_date               IN  DATE,
  p_out_return_code           OUT NUMBER,
  p_out_error_message         OUT VARCHAR2);
  
  PROCEDURE prc_rpt_survey_minimum_resp
(p_in_requested_user_id      IN  NUMBER,  
  p_out_return_code           OUT NUMBER,
  p_out_error_message         OUT VARCHAR2);
   
END;
/
CREATE OR REPLACE PACKAGE BODY pkg_report_survey
IS

PROCEDURE prc_rpt_survey_response_detail (
   p_in_requested_user_id   IN     NUMBER,
   p_in_start_date          IN     DATE,
   p_in_end_date            IN     DATE,
   p_out_return_code           OUT NUMBER,
   p_out_error_message         OUT VARCHAR2)
IS

/*******************************************************************************
   Purpose:  Populate the reporting table rpt_survey_response_detail 

   Person                    Date                      Comments
   -----------              ----------         -----------------------------------------------------
   Ravi Dhanekula    12/11/2013              Initial Creation
                              03/11/2014              Updated the query to match the Survey enhancements done as part of G5.3.2.        
   
*******************************************************************************/
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('prc_rpt_survey_response_detail');
c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
c_created_by         CONSTANT rpt_survey_response_detail.created_by%TYPE:= p_in_requested_user_id;
v_parm_list          execution_log.text_line%TYPE;   --03/11/2019  Bug 78631
   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
  BEGIN
v_stage := 'initialize variables'; 
v_parm_list := 
'p_in_requested_user_id :='||p_in_requested_user_id ||CHR(10)||
'p_in_start_date        :='||p_in_start_date        ||CHR(10)|| 
'p_in_end_date          :='||p_in_end_date          ;   
INSERT INTO rpt_survey_response_detail (rpt_survey_response_detail_id,
                                        promotion_id,
                                        survey_id,
                                        user_id,
                                        node_id,
                                        survey_question_id,
                                        survey_question_response_id,
                                        survey_question_response,
                                        created_by,
                                        date_created)
   SELECT rpt_survey_response_detail_seq.NEXTVAL,
          p.promotion_id,
          ps.survey_id,
          psy.user_id,
          psy.node_id,
          psyr.survey_question_id,
          psyr.survey_question_response_id,
          sqr.cm_asset_name survey_question_response,
          c_created_by,
          SYSDATE
     FROM promo_survey ps,
          promotion p,
          participant_survey psy,
          participant_survey_response psyr,
          survey_question sq,
          survey_question_response sqr,
          node n
    WHERE     ps.promotion_id = p.promotion_id
          AND p.promotion_status IN ('live', 'expired')
          AND ps.survey_id = sq.survey_id
          AND psy.promotion_id = ps.promotion_id
          AND psy.participant_survey_id = psyr.participant_survey_id
          AND sqr.survey_question_id = sq.survey_question_id
          AND psyr.survey_question_response_id IS NOT NULL
          AND psy.is_completed = 1
          AND sqr.survey_question_response_id =
                 psyr.survey_question_response_id
          AND psy.node_id = n.node_id
          AND (    p_in_start_date < psyr.date_created
               AND psyr.date_created <= p_in_end_date);
   
  v_rec_cnt := SQL%ROWCOUNT;
  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  p_out_return_code := 00;

EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code := 99;
   p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;
   prc_execution_log_entry(c_process_name, c_release_level, 'ERROR',v_parm_list||':'||p_out_error_message, NULL);
  END;

PROCEDURE prc_rpt_survey_detail (p_in_requested_user_id   IN     NUMBER,
                                 p_out_return_code           OUT NUMBER,
                                 p_out_error_message         OUT VARCHAR2)
IS
   /*******************************************************************************
    Purpose:  Populate the reporting table rpt_survey_detail

    Person                    Date                      Comments
    -----------              ----------         -----------------------------------------------------
    Ravi Dhanekula    12/11/2013              Initial Creation
                               08/06/2014              Added this sequel to get the allactive data as we no longer insert allactive data to rpt_pax_promo_eligibility
    Chidamba          09/19/2014     Bugfix# 56622 - comment application tabel to prevent cartition product in detail table population

 *******************************************************************************/

   c_process_name    CONSTANT execution_log.process_name%TYPE
                                 := UPPER ('prc_rpt_survey_detail') ;
   c_release_level   CONSTANT execution_log.release_level%TYPE := 2.0;
   c_created_by      CONSTANT rpt_survey_detail.created_by%TYPE
                                 := p_in_requested_user_id ;

   v_stage                    execution_log.text_line%TYPE;
   v_rec_cnt                  INTEGER;
    
  BEGIN
MERGE INTO rpt_survey_detail rpt
     USING (SELECT p.promotion_id,
                   ps.survey_id,
                   psy.user_id,
                   psy.node_id,
                   psy.is_completed
              FROM promo_survey ps,
                   promotion p,
                   participant_survey psy,
                   node n
             WHERE     ps.promotion_id = p.promotion_id
                   AND p.promotion_status IN ('live', 'expired')
                   AND psy.promotion_id = ps.promotion_id
                   AND psy.node_id = n.node_id
                   AND psy.is_completed = 1
                   UNION
SELECT p.promotion_id,
       ps.survey_id,
       rpt.participant_id user_id,
       rpt.node_id,
       0 is_completed
  FROM rpt_pax_promo_eligibility rpt,
       promo_survey ps,
       promotion p,
       node n
 WHERE     ps.promotion_id = p.promotion_id
       AND p.promotion_status IN ('live', 'expired')
       AND ps.promotion_id = rpt.promotion_id
       AND rpt.node_id = n.node_id
       AND rpt.promotion_id IN (SELECT promotion_id
                                  FROM promotion
                                 WHERE     promotion_type = 'survey'
                                       AND promotion_status = 'live')
       AND NOT EXISTS
                  (SELECT *
                     FROM participant_survey
                    WHERE     promotion_id = rpt.promotion_id
                          AND user_id = rpt.participant_id
                          AND survey_id = ps.survey_id
                          AND is_completed = 1)
                          UNION  --08/06/2014
                          SELECT    p.promotion_id,
                             ps.survey_id,
                             pa.user_id,      --au.user_id
                             un.node_id,
                             0 is_completed 
              FROM promotion p,
                       user_node un,
                       participant pa,
                       --application_user au,  --09/19/2014
                       promo_survey ps
            WHERE
              p.promotion_type ='survey' 
              AND promotion_status = 'live' 
              AND primary_audience_type = 'allactivepaxaudience'     
              AND ps.promotion_id = p.promotion_id              
              AND pa.user_id = un.user_id
              AND pa.status = 'active'                      
              AND un.is_primary =  1                         
              AND NOT EXISTS
                  (SELECT *
                     FROM participant_survey
                    WHERE     promotion_id = p.promotion_id
                          AND user_id = pa.user_id     --09/19/2014 au.user_id
                          AND survey_id = ps.survey_id
                          AND is_completed = 1)                          
)s
    ON (rpt.user_id = s.user_id AND rpt.node_id = s.node_id AND rpt.promotion_id = s. promotion_id)
           WHEN MATCHED THEN UPDATE
SET is_completed = s.is_completed,
              date_modified         = SYSDATE,
              modified_by           = c_created_by
              WHERE NOT (   DECODE(rpt.is_completed,  s.is_completed,                    1, 0) = 1
               )
WHEN NOT MATCHED THEN INSERT
(rpt_survey_detail_id,promotion_id,survey_id,user_id,node_id,is_completed,created_by,date_created)
  VALUES
  (rpt_survey_detail_seq.Nextval,
 s.promotion_id,
 s.survey_id,
 s.user_id,
 s.node_id,
 s.is_completed,
 c_created_by,
 SYSDATE );
  v_rec_cnt := SQL%ROWCOUNT;
  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  p_out_return_code := 00;

EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code := 99;
   p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR','Param:'||p_in_requested_user_id||':'||p_out_error_message, NULL);
  END;  
  
PROCEDURE prc_rpt_survey_response_sum (
   p_in_requested_user_id   IN     NUMBER,
   p_out_return_code           OUT NUMBER,
   p_out_error_message         OUT VARCHAR2)
IS
   /*******************************************************************************
   Purpose:  Populate the reporting table rpt_survey_response_summary

   Person                    Date                      Comments
   -----------              ----------         -----------------------------------------------------
   Ravi Dhanekula    12/11/2013              Initial Creation

*******************************************************************************/

   c_process_name    CONSTANT execution_log.process_name%TYPE
                                 := UPPER ('prc_rpt_survey_response_sum') ;
   c_release_level   CONSTANT execution_log.release_level%TYPE := 2.0;
   c_created_by      CONSTANT rpt_survey_response_summary.created_by%TYPE
                                 := p_in_requested_user_id ;

   v_stage                    execution_log.text_line%TYPE;
   v_rec_cnt                  INTEGER;

  BEGIN
 v_stage := 'MERGE detail derived summary records';  

  MERGE INTO rpt_survey_response_summary d
  USING (WITH 
          rpt_teamsum AS
        (  -- build team summary records         
         SELECT h.hier_level
                ,h.parent_node_id  header_node_id     
                ,NVL(d.node_id,h.node_id) detail_node_id                                                          
                ,d.promotion_id,
                d.survey_question_response               
                ,COUNT(d.user_id) eligible_responses
                ,COUNT(d.survey_question_response) AS total_responses                          
           FROM rpt_survey_response_detail d
               ,rpt_hierarchy h              
          WHERE h.node_id = d.node_id            
          GROUP BY h.parent_node_id ,
                   nvl(d.node_id,h.node_id)                  
                   , d.promotion_id
                   , h.hier_level,
                   d.survey_question_response                   
        ), 
        detail_derived_summary AS            
        (  -- derive summaries based on team summary data
           SELECT -- key fields
                  rt.detail_node_id,
                  'teamsum' AS sum_type,
                  rt.promotion_id,
--                  rt.survey_id,
                  rt.survey_question_response,
                  rt.header_node_id,
                  rt.hier_level,
                  1 AS is_leaf, -- The team summary records are always a leaf
                  -- count fields                 
                  rt.eligible_responses,
                  rt.total_responses            
             FROM rpt_teamsum rt
            UNION ALL
           SELECT -- key fields
                  h.node_id AS detail_node_id,
                  'nodesum' AS sum_type,
                  rt.promotion_id,
--                  rt.survey_id,
                  rt.survey_question_response,
                  h.parent_node_id AS header_node_id,
                  h.hier_level,
                  h.is_leaf,
                  -- count fields
                  SUM(rt.eligible_responses) eligible_responses,
                  SUM(rt.total_responses) AS total_responses
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
            WHERE rt.eligible_responses != 0      -- create node summary for team summaries with non-zero media amounts                
               -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
              AND rt.detail_node_id = npn.node_id
              AND npn.path_node_id = h.node_id
            GROUP BY h.node_id,
                  rt.promotion_id,
                  h.parent_node_id,
                  h.hier_level,
                  h.is_leaf,
                  rt.survey_question_response
        ) -- end detail_derived_summary
        -- compare existing summary records with detail derived summaries
        SELECT es.s_rowid,
               dds.hier_level || '-' || dds.sum_type AS record_type,
               dds.*
          FROM (  -- get existing summaries based on derived details
                 SELECT s2.ROWID AS s_rowid,
                        s2.detail_node_id,
                        SUBSTR(s2.record_type, -7) AS sum_type,
                        s2.promotion_id,
                        s2.survey_question_response,
                        s2.total_responses
                   FROM rpt_survey_response_summary s2
                  ) es
               -- full outer join so unmatched existing summaries can be deleted
               FULL OUTER JOIN detail_derived_summary dds
               ON (   DECODE(es.detail_node_id, dds.detail_node_id, 1, 0) = 1
                  AND es.sum_type       = dds.sum_type                 
                  AND es.promotion_id   = dds.promotion_id                 
                  AND NVL(es.survey_question_response,0)      = NVL(dds.survey_question_response,0)
                  ) 
         ) s
      ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
      UPDATE SET         
         d.header_node_id     = s.header_node_id,
         d.is_leaf            = s.is_leaf,
         d.total_responses = NVL(s.total_responses,0),
         d.eligible_responses = NVL(s.eligible_responses,0),        
         d.modified_by        = c_created_by,
         d.date_modified      = SYSDATE
   WHERE ( -- only update records with different values          
         DECODE(d.header_node_id,     s.header_node_id,   1, 0) = 0
         OR DECODE(d.is_leaf,            s.is_leaf,          1, 0) = 0
         OR DECODE(d.hier_level,         s.hier_level,         1, 0) = 0
         OR DECODE(d.total_responses, s.total_responses, 1, 0) = 0
         OR DECODE(d.eligible_responses,  s.eligible_responses,  1, 0) = 0)
 -- remove existing summaries that no longer have product details
--      DELETE
--       WHERE s.promotion_id IS NULL
    WHEN NOT MATCHED THEN
      INSERT
       (rpt_survey_response_summary_id,
        header_node_id,
        detail_node_id,
        total_responses,
        eligible_responses,
        survey_question_response,
        promotion_id,        
        hier_level,
        record_type,
        is_leaf,        
        date_created,
        created_by)
      VALUES
        (rpt_survey_response_summary_sq.NEXTVAL,
            s.header_node_id,
            s.detail_node_id,
            s.total_responses,
            s.eligible_responses,
            s.survey_question_response,
            s.promotion_id,            
            s.hier_level,
            s.record_type,
            s.is_leaf,            
            SYSDATE,
            c_created_by);
   
  v_rec_cnt := SQL%ROWCOUNT;
  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  p_out_return_code := 00;

EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code := 99;
   p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR','Param:'||p_in_requested_user_id||':'||p_out_error_message, NULL);
  END;
  
PROCEDURE prc_rpt_survey_summary (p_in_requested_user_id   IN     NUMBER,
                                  p_out_return_code           OUT NUMBER,
                                  p_out_error_message         OUT VARCHAR2)
IS
   /*******************************************************************************
   Purpose:  Populate the reporting table rpt_survey_summary

   Person                    Date                      Comments
   -----------              ----------         -----------------------------------------------------
   Ravi Dhanekula    12/11/2013              Initial Creation

*******************************************************************************/
   c_process_name    CONSTANT execution_log.process_name%TYPE := UPPER ('prc_rpt_survey_summary') ;
   c_release_level   CONSTANT execution_log.release_level%TYPE := 2.0;
   c_created_by      CONSTANT rpt_survey_summary.created_by%TYPE
                                 := p_in_requested_user_id ;

   v_stage                    execution_log.text_line%TYPE;
   v_rec_cnt                  INTEGER;
   
   BEGIN
   
   v_stage :='MERGE detail derived summary records';
   
   MERGE INTO rpt_survey_summary d
   USING (WITH 
          rpt_teamsum AS
        (  -- build team summary records
         SELECT h.hier_level
                ,h.parent_node_id  header_node_id     
                ,NVL(d.node_id,h.node_id) detail_node_id                                                          
                ,d.promotion_id                            
                ,COUNT(d.user_id) eligible_surveys
                ,SUM(is_completed) surveys_taken
                -- AS total_responses                          
           FROM rpt_survey_detail d
               ,rpt_hierarchy h              
          WHERE h.node_id = d.node_id            
          GROUP BY h.parent_node_id ,
                   nvl(d.node_id,h.node_id)                  
                   , d.promotion_id
                   , h.hier_level
        ), 
        detail_derived_summary AS            
        (  -- derive summaries based on team summary data
           SELECT -- key fields
                  rt.detail_node_id,
                  'teamsum' AS sum_type,
                  rt.promotion_id,              
                  rt.header_node_id,
                  rt.hier_level,
                  1 AS is_leaf, -- The team summary records are always a leaf
                  -- count fields                 
                  rt.eligible_surveys,
                  rt.surveys_taken            
             FROM rpt_teamsum rt
            UNION ALL
           SELECT -- key fields
                  h.node_id AS detail_node_id,
                  'nodesum' AS sum_type,
                  rt.promotion_id,                 
                  h.parent_node_id AS header_node_id,
                  h.hier_level,
                  h.is_leaf,
                  -- count fields
                  SUM(rt.eligible_surveys) eligible_surveys,
                  SUM(rt.surveys_taken) AS surveys_taken
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
            WHERE rt.eligible_surveys != 0      -- create node summary for team summaries with non-zero media amounts                 
               -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
              AND rt.detail_node_id = npn.node_id
              AND npn.path_node_id = h.node_id
            GROUP BY h.node_id,
                  rt.promotion_id,            
                  h.parent_node_id,
                  h.hier_level,
                  h.is_leaf               
        ) -- end detail_derived_summary
        -- compare existing summary records with detail derived summaries
        SELECT es.s_rowid,
               dds.hier_level || '-' || dds.sum_type AS record_type,
               dds.*
          FROM (  -- get existing summaries based on derived details
                 SELECT s2.ROWID AS s_rowid,
                        s2.detail_node_id,
                        SUBSTR(s2.record_type, -7) AS sum_type,
                        s2.promotion_id,
                        s2.surveys_taken
                   FROM rpt_survey_summary s2
                  ) es
               -- full outer join so unmatched existing summaries can be deleted
               FULL OUTER JOIN detail_derived_summary dds
               ON (   DECODE(es.detail_node_id, dds.detail_node_id, 1, 0) = 1
                  AND es.sum_type       = dds.sum_type                 
                  AND es.promotion_id   = dds.promotion_id
                  ) 
         ) s
      ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
      UPDATE SET         
         d.header_node_id     = s.header_node_id,
         d.is_leaf            = s.is_leaf,
         d.surveys_taken = NVL(s.surveys_taken,0),
         d.eligible_surveys = NVL(s.eligible_surveys,0),        
         d.modified_by        = c_created_by,
         d.date_modified      = SYSDATE
   WHERE ( -- only update records with different values          
         DECODE(d.header_node_id,     s.header_node_id,   1, 0) = 0
         OR DECODE(d.is_leaf,            s.is_leaf,          1, 0) = 0
         OR DECODE(d.hier_level,         s.hier_level,         1, 0) = 0
         OR DECODE(d.surveys_taken, s.surveys_taken, 1, 0) = 0
         OR DECODE(d.eligible_surveys,  s.eligible_surveys,  1, 0) = 0)
 -- remove existing summaries that no longer have product details
--      DELETE
--       WHERE s.promotion_id IS NULL
    WHEN NOT MATCHED THEN INSERT      
    (rpt_survey_summary_id,
        header_node_id,
        detail_node_id,
        surveys_taken,
        eligible_surveys,
        promotion_id,
        hier_level,
        record_type,
        is_leaf,
        date_created,
        created_by)
      VALUES
        (rpt_survey_summary_sq.NEXTVAL,
            s.header_node_id,
            s.detail_node_id,
            s.surveys_taken,
            s.eligible_surveys,
            s.promotion_id,
            s.hier_level,
            s.record_type,
            s.is_leaf,
            SYSDATE,
            c_created_by);
                        
  v_rec_cnt := SQL%ROWCOUNT;
  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  p_out_return_code := 00;

EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code := 99;
   p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;
   prc_execution_log_entry(c_process_name, c_release_level, 'ERROR',p_out_error_message, NULL);
END;

PROCEDURE prc_rpt_survey_openend_resp (
   p_in_requested_user_id   IN     NUMBER,
   p_in_start_date          IN     DATE,
   p_in_end_date            IN     DATE,
   p_out_return_code           OUT NUMBER,
   p_out_error_message         OUT VARCHAR2)
IS

/*******************************************************************************
   Purpose:  Populate the reporting table rpt_survey_response_detail 

   Person                    Date                      Comments
   -----------              ----------         -----------------------------------------------------
   Ravi Dhanekula    12/16/2013              Initial Creation
                              03/11/2014              Updated the query to match the Survey enhancements done as part of G5.3.2.       
                              05/12/2014              Added slider responses also to the open ended extract. Bug # 52209
   
*******************************************************************************/
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('prc_rpt_survey_response_detail');
c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
c_created_by         CONSTANT rpt_survey_response_detail.created_by%TYPE:= p_in_requested_user_id;
v_parm_list          execution_log.text_line%TYPE;   --03/11/2019  Bug 78631
   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
  BEGIN
v_stage := 'initialize variables'; 
v_parm_list := 
'p_in_requested_user_id :='||p_in_requested_user_id ||CHR(10)||
'p_in_start_date        :='||p_in_start_date        ||CHR(10)|| 
'p_in_end_date          :='||p_in_end_date          ; 
INSERT INTO rpt_survey_openend_response (rpt_survey_openend_response_id,
                                        promotion_id,
                                        survey_id,                                        
                                        node_id,
                                        survey_question,                                        
                                        open_end_response,
                                        created_by,
                                        date_created)
   SELECT rpt_survey_openend_resp_seq.NEXTVAL,
       p.promotion_id,
       ps.survey_id,
       psy.node_id,
       sq.cm_asset_name survey_question,
       NVL(psyr.open_end_response,psyr.slider_response), --05/12/2014
       c_created_by,
       SYSDATE
  FROM participant_survey_response psyr,
       participant_survey psy,
       promo_survey ps,
       promotion p,
       survey_question sq
 WHERE     (psyr.open_end_response IS NOT NULL OR psyr.slider_response IS NOT NULL) --03/11/2014 --05/12/2014
       AND psyr.participant_survey_id = psy.participant_survey_id
       AND psy.is_completed = 1
       AND psy.survey_id = ps.survey_id
       AND ps.promotion_id = p.promotion_id
       AND p.promotion_status IN ('live', 'expired')
       AND psyr.survey_question_id = sq.survey_question_id
          AND (    p_in_start_date < psyr.date_created
               AND psyr.date_created <= p_in_end_date);
   
  v_rec_cnt := SQL%ROWCOUNT;
  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  p_out_return_code := 00;

EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code := 99;
   p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;
   prc_execution_log_entry(c_process_name, c_release_level, 'ERROR',v_parm_list||':'||p_out_error_message, NULL);
  END;
  
  PROCEDURE prc_rpt_survey_minimum_resp
 (p_in_requested_user_id      IN  NUMBER,  
  p_out_return_code           OUT NUMBER,
  p_out_error_message         OUT VARCHAR2)
IS

/*******************************************************************************
   Purpose:  poupulate table  rpt_survey_minimum_response based on the system variable 'survey.report.response.count'    

   Person                    Date                      Comments
   -----------              ----------         -----------------------------------------------------
   Ravi Dhanekula    01/10/2014              Initial Creation
                              03/20/2014              Changed the process to consider only the responses with a response_id (non-openeded responses)
   
*******************************************************************************/
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('prc_rpt_survey_minimum_resp');
c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;
c_created_by         CONSTANT rpt_survey_response_detail.created_by%TYPE:= p_in_requested_user_id;

   v_stage              execution_log.text_line%TYPE;
   v_minimum_response            INTEGER;
   v_rec_cnt                              INTEGER;
  BEGIN
  
  BEGIN
  SELECT int_val 
      INTO v_minimum_response
      FROM os_propertyset 
     WHERE lower(entity_name) = 'survey.report.response.count';
     EXCEPTION WHEN OTHERS THEN
     v_minimum_response :=3;
  END;
   
 INSERT INTO rpt_survey_minimum_response
           (promotion_id,node_id,is_leaf)           
           SELECT rpt.promotion_id,rpt.node_id,0 FROM (
 SELECT  rhr.node_id,p.promotion_id
              FROM promo_survey ps,
                   promotion p,
                   participant_survey psy,
                   participant_survey_response psyr,--03/20/2014
                   node n,
                   rpt_hierarchy_rollup rhr
             WHERE     ps.promotion_id = p.promotion_id
                   AND p.promotion_status IN ('live', 'expired')
                   AND psy.promotion_id = ps.promotion_id
                   AND psy.node_id = n.node_id
                   AND n.node_id =rhr.child_node_id
                   AND psy.is_completed = 1
                   AND psy.participant_survey_id = psyr.participant_survey_id     --03/20/2014          
                   AND psyr.survey_question_response_id is not null
                   group by rhr.node_id,p.promotion_id HAVING COUNT(*) >= v_minimum_response) rpt
                   WHERE  NOT EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=rpt.promotion_id AND node_id = rpt.node_id AND is_leaf =0);
                   
                   INSERT INTO rpt_survey_minimum_response
           (promotion_id,node_id,is_leaf)           
           SELECT rpt.promotion_id,rpt.node_id,1 FROM (
 SELECT  n.node_id,p.promotion_id
              FROM promo_survey ps,
                   promotion p,
                   participant_survey psy,
                   participant_survey_response psyr,--03/20/2014
                   node n                   
             WHERE     ps.promotion_id = p.promotion_id
                   AND p.promotion_status IN ('live', 'expired')
                   AND psy.promotion_id = ps.promotion_id
                   AND psy.node_id = n.node_id                  
                   AND psy.is_completed = 1
                   AND psy.participant_survey_id = psyr.participant_survey_id         --03/20/2014      
                   AND psyr.survey_question_response_id is not null
                   group by n.node_id,p.promotion_id HAVING COUNT(*) >= v_minimum_response) rpt
                   WHERE  NOT EXISTS (SELECT * FROM rpt_survey_minimum_response WHERE promotion_id=rpt.promotion_id AND node_id = rpt.node_id AND is_leaf =1);
   
  v_rec_cnt := SQL%ROWCOUNT;
  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
  p_out_return_code := 00;

EXCEPTION
   WHEN OTHERS THEN
   p_out_return_code := 99;
   p_out_error_message := v_stage || ': ' || SQLCODE || ', ' || SQLERRM;
   prc_execution_log_entry(c_process_name, c_release_level, 'ERROR','Param:'||p_in_requested_user_id||':'||p_out_error_message, NULL);
  END;

END;
/