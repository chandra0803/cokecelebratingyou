CREATE OR REPLACE PACKAGE pkg_query_quiz_analysis IS
/******************************************************************************
   NAME:       pkg_query_quiz_analysis
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/25/2014      keshaboi       1. Created this package.
******************************************************************************/
/* getQuizAnalysisSummaryResults */
 procedure prc_getQuizAnalysisSummary(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
      /* getQuizAnalysisDetailOneResults */ 
procedure prc_getQuizAnalysisDetailOne(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
        /* getQuizAnalysisDetailTwoResults */
      procedure prc_getQuizAnalysisDetailTwo(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
/* getQuizQuestionAnalysisBarResults */
   
procedure prc_getQuizQuestionAnalysisBar(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
       /* getQuizAttemptsPercentForOrgBarResults */
      procedure prc_getQuizAttemptPercentForOB(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getQuizAttemptStatusForOrgBarResults */
procedure prc_getQuizAttemptStatusForOB(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);

END pkg_query_quiz_analysis;
/
CREATE OR REPLACE PACKAGE BODY pkg_query_quiz_analysis IS
/* getQuizAnalysisSummaryResults */
procedure prc_getQuizAnalysisSummary(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
IS

/******************************************************************************
  NAME:       prc_getQuizAnalysisSummary
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J              01/08/2015        Bug Fix 58951 - Display Quiz Promotion Name as the first column in Summary Table              
  Ravi Dhanekula   08/27/2015        Bug # 63828 - Quiz report: In Quiz Analysis report, total for Max Attempts Allowed For Pax is not getting displayed                  
  ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getQuizAnalysisSummary' ;
  v_stage        VARCHAR2 (500);
  v_sortCol             VARCHAR2(200);
  l_query VARCHAR2(32767);   


 BEGIN
  v_stage   := 'getQuizAnalysisSummary';
  
   l_query  := 'SELECT * FROM
  ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;
       
  l_query := l_query ||'  '||'SELECT ROWNUM RN, 
         rs.* 
       FROM 
         (SELECT c.q_id                                                                                                AS quiz_id, 
--                c.q_name                                                                                               AS quiz_name,     --01/08/2015
                p.promotion_name AS quiz_name,  --01/08/2015                      
                fnc_cms_picklist_value ( ''picklist.quiz.type.items'', c.q_type, '''||p_in_languageCode||''')                      AS quiz_type,  
                c.q_in_pool                                                                                                 AS ques_in_pool,  
                c.q_number_asked                                                                                            AS number_to_ask,  
                c.q_passing_score                                                                                           AS req_to_pass,  
                NVL((d.passed    +d.failed+d.incomplete),0)                                                                 AS quiz_attempts,  
                ROUND((((d.passed+d.failed+d.incomplete)/(d.passed+d.failed+d.incomplete))*100),2)                          AS quiz_attempts_percent,  
                d.passed                                                                                                    AS quiz_attempts_passed,  
                ROUND(DECODE((d.passed +d.failed+d.incomplete),0,0,(d.passed/(d.passed +d.failed+d.incomplete))*100),2)     AS quiz_attempts_pass_perc,  
                d.failed                                                                                                    AS quiz_attempts_failed,  
                ROUND(DECODE((d.passed +d.failed+d.incomplete),0,0,(d.failed/(d.passed +d.failed+d.incomplete))*100),2)     AS quiz_attempts_fail_perc,  
                d.incomplete                                                                                                AS quiz_attempts_incomplete,  
                ROUND(DECODE((d.passed +d.failed+d.incomplete),0,0,(d.incomplete/(d.passed +d.failed+d.incomplete))*100),2) AS quiz_attempts_incompl_perc,  
                decode(d.maximum_attempts,0, fnc_cms_picklist_value(''claims.quiz.submission'',''UNLIMITED_ATTEMPTS'', '''||p_in_languageCode||'''),d.maximum_attempts)   AS max_attempts_allowed_per_pax  
              FROM rpt_quiz_analysis c,  
                promotion p,  
                (SELECT quiz_id,  
                  promotion_id,  
                  maximum_attempts,  
                  SUM(number_passed) passed,  
                  SUM(number_failed) failed,  
                  SUM(number_incomplete) incomplete  
                FROM rpt_quiz_attempts_analysis  
                WHERE  
                 (('''||p_in_promotionId||''' is NULL AND '''||p_in_diyQuizId||''' IS NULL)  
                  OR ('''||p_in_promotionId||''' is NOT NULL AND promotion_id IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_promotionId||'''))) OR ('''||p_in_diyQuizId||''' IS NOT NULL AND quiz_id IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_diyQuizId||''')))) ))  
                AND NVL(TRUNC(submission_date), TRUNC(SYSDATE)) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')  
                GROUP BY quiz_id,  
                  promotion_id,  
                  maximum_attempts  
                ) d  
              WHERE p.promotion_id   = d.promotion_id  
              AND d.quiz_id          = c.q_id  
              AND (('''||p_in_promotionId||''' IS NULL) OR p.promotion_id IN
             (SELECT * FROM TABLE(get_array_varchar('''||p_in_promotionId||''')))) 
              AND p.promotion_status = NVL('''||p_in_promotionStatus||''',promotion_status)  
              GROUP BY c.q_number_asked,  
                c.q_passing_score,  
                c.q_in_pool,  
                c.q_type,  
                d.passed,  
                d.failed,  
                d.incomplete,  
                d.maximum_attempts,  
--                c.q_name,        --01/08/2015  
                p.promotion_name,  --01/08/2015  
                c.q_id       
         ) rs 
         ORDER BY '|| v_sortCol ||'
    ) WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd;  
    
    dbms_output.put_line (l_query);

OPEN p_out_data FOR 
   l_query;
    
/* getQuizAnalysisSummaryResultsSize */

  SELECT COUNT (1) INTO p_out_size_data
     FROM  (SELECT c.q_id                                                                                               AS quiz_id, 
                c.q_name                                                                                            AS quiz_name,  
                fnc_cms_picklist_value ( 'picklist.quiz.type.items', c.q_type, p_in_languageCode)                      AS quiz_type,  
                c.q_in_pool                                                                                                 AS ques_in_pool,  
                c.q_number_asked                                                                                            AS number_to_ask,  
                c.q_passing_score                                                                                           AS req_to_pass,  
                NVL((d.passed    +d.failed+d.incomplete),0)                                                                 AS quiz_attempts,  
                ROUND((((d.passed+d.failed+d.incomplete)/(d.passed+d.failed+d.incomplete))*100),2)                          AS quiz_attempts_percent,  
                d.passed                                                                                                    AS quiz_attempts_passed,  
                ROUND(DECODE((d.passed +d.failed+d.incomplete),0,0,(d.passed/(d.passed +d.failed+d.incomplete))*100),2)     AS quiz_attempts_pass_perc,  
                d.failed                                                                                                    AS quiz_attempts_failed,  
                ROUND(DECODE((d.passed +d.failed+d.incomplete),0,0,(d.failed/(d.passed +d.failed+d.incomplete))*100),2)     AS quiz_attempts_fail_perc,  
                d.incomplete                                                                                                AS quiz_attempts_incomplete,  
                ROUND(DECODE((d.passed +d.failed+d.incomplete),0,0,(d.incomplete/(d.passed +d.failed+d.incomplete))*100),2) AS quiz_attempts_incompl_perc,  
                decode(d.maximum_attempts,0,'" + CmsResourceBundle.getCmsBundle().getString( "claims.quiz.submission.UNLIMITED_ATTEMPTS" ) + "', d.maximum_attempts)   AS max_attempts_allowed_per_pax  
                -- DECODE(pq.quiz_id,NULL,dq.maximum_attempts,pq.maximum_attempts) maximum_attempts
             FROM rpt_quiz_analysis c,  
                promotion p,  
                (SELECT quiz_id,  
                  promotion_id,  
                  maximum_attempts,  
                  SUM(number_passed) passed,  
                  SUM(number_failed) failed,  
                  SUM(number_incomplete) incomplete  
                FROM rpt_quiz_attempts_analysis  
                WHERE  
                 ((p_in_promotionId is NULL AND p_in_diyQuizId IS NULL)  
                  OR (p_in_promotionId is NOT NULL AND promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))) OR (p_in_diyQuizId IS NOT NULL AND quiz_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_diyQuizId)))) ))  
                AND NVL(TRUNC(submission_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  
                GROUP BY quiz_id,  
                  promotion_id,  
                  maximum_attempts  
                ) d  
              WHERE p.promotion_id   = d.promotion_id  
              AND d.quiz_id          = c.q_id  
              AND ((p_in_promotionId IS NULL) OR p.promotion_id IN
             (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))  
              AND p.promotion_status = NVL(p_in_promotionStatus,promotion_status)  
              GROUP BY c.q_number_asked,  
                c.q_passing_score,  
                c.q_in_pool,  
                c.q_type,  
                d.passed,  
                d.failed,  
                d.incomplete,  
                d.maximum_attempts,  
                c.q_name,  
                c.q_id  
       )  ;

/* getQuizAnalysisSummaryResultsTotals */
OPEN p_out_totals_data FOR
SELECT NVL(SUM(ques_in_pool),0)                                                                                                                                         AS ques_in_pool,
  NVL(SUM(number_to_ask),0)                                                                                                                                             AS number_to_ask,
  NVL(SUM(req_to_pass),0)                                                                                                                                               AS req_to_pass,
  NVL(SUM(quiz_attempts),0)                                                                                                                                             AS quiz_attempts,
  ROUND(NVL((SUM(quiz_attempts_passed+quiz_attempts_failed+quiz_attempts_incomplete)/SUM(quiz_attempts_passed+quiz_attempts_failed+quiz_attempts_incomplete))*100,0),2) AS quiz_attempts_percent,
  NVL(SUM(quiz_attempts_passed),0)                                                                                                                                      AS quiz_attempts_passed,
  ROUND(NVL((SUM(quiz_attempts_passed)/SUM(quiz_attempts_passed+quiz_attempts_failed+quiz_attempts_incomplete))*100,0),2)                                               AS quiz_attempts_pass_perc,
  NVL(SUM(quiz_attempts_failed),0)                                                                                                                                      AS quiz_attempts_failed,
  ROUND(NVL((SUM(quiz_attempts_failed)/SUM(quiz_attempts_passed+quiz_attempts_failed+quiz_attempts_incomplete))*100,0),2)                                               AS quiz_attempts_fail_perc,
  NVL(SUM(quiz_attempts_incomplete),0)                                                                                                                                  AS quiz_attempts_incomplete,
  ROUND(NVL((SUM(quiz_attempts_incomplete)/SUM(quiz_attempts_passed+quiz_attempts_failed+quiz_attempts_incomplete))*100,0),2)                                           AS quiz_attempts_incompl_perc,
  SUM(max_attempts_allowed_per_pax) as max_attempts_allowed_per_pax --08/27/2015  
FROM
  (SELECT ROWNUM RN,
    rs.*
  FROM
    (SELECT c.q_id                             AS quiz_id,
      c.q_name                                 AS quiz_name,
      c.q_type                                 AS quiz_type,
      c.q_in_pool                              AS ques_in_pool,
      c.q_number_asked                         AS number_to_ask,
      c.q_passing_score                        AS req_to_pass,
      NVL((d.passed +d.failed+d.incomplete),0) AS quiz_attempts,
      d.passed                                 AS quiz_attempts_passed,
      d.failed                                 AS quiz_attempts_failed,
      d.incomplete                             AS quiz_attempts_incomplete,
      d.maximum_attempts                       AS max_attempts_allowed_per_pax
    FROM rpt_quiz_analysis c,
      promotion p,
      (SELECT quiz_id,
        promotion_id,
        maximum_attempts,
        SUM(number_passed) passed,
        SUM(number_failed) failed,
        SUM(number_incomplete) incomplete
      FROM rpt_quiz_attempts_analysis
      WHERE ((p_in_promotionId IS NULL
      AND p_in_diyQuizId       IS NULL)
      OR (p_in_promotionId     IS NOT NULL
      AND promotion_id     IN
        (SELECT             *
        FROM TABLE(get_array_varchar(p_in_promotionId))
        )
      OR (p_in_diyQuizId IS NOT NULL
      AND quiz_id    IN
        (SELECT * FROM TABLE(get_array_varchar(p_in_diyQuizId))
        )) ))
      AND NVL(TRUNC(submission_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
      GROUP BY quiz_id,
        promotion_id,
        maximum_attempts
      ) d
    WHERE p.promotion_id   = d.promotion_id
    AND d.quiz_id          = c.q_id
    AND ((p_in_promotionId IS NULL) OR p.promotion_id IN
   (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))  
    AND p.promotion_status = NVL(p_in_promotionStatus,promotion_status)
    GROUP BY c.q_number_asked,
      c.q_passing_score,
      c.q_in_pool,
      c.q_type,
      d.passed,
      d.failed,
      d.incomplete,
      d.maximum_attempts,
      c.q_name,
      c.q_id
    ) rs
  ) ;
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
         
 END prc_getQuizAnalysisSummary;

/* getQuizAnalysisDetailOneResults */ 
 procedure prc_getQuizAnalysisDetailOne(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
IS
/******************************************************************************
  NAME:       prc_getQuizAnalysisDetailOne
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)         
                                 09/02/2014            Fixed the bug # 56205                        
  ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getQuizAnalysisDetailOne' ;
  v_stage        VARCHAR2 (500);
  v_sortCol             VARCHAR2(200);
  l_query VARCHAR2(32767); 

 BEGIN
  v_stage   := 'getQuizAnalysisDetailOne';
 
 l_query  := 'SELECT * FROM
  ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;
       
  l_query := l_query ||'  '||'SELECT ROWNUM RN, 
         rs.* 
       FROM 
     ( SELECT qq_id, 
       Question, 
       nbr_of_times_asked, 
       nbr_correct_responses, 
       ROUND(DECODE(NVL(nbr_of_times_asked,0),0,0,((nbr_correct_responses/nbr_of_times_asked)*100)),2) perc_correct_responses, 
       nbr_incorrect_responses, 
       ROUND(DECODE(NVL(nbr_of_times_asked,0),0,0,((nbr_incorrect_responses/nbr_of_times_asked)*100)),2) perc_incorrect_responses 
     FROM 
       (SELECT qq_id AS qq_id, 
         question.cms_value Question, 
         SUM(number_of_times_asked) nbr_of_times_asked, 
         SUM(number_of_correct_responses) nbr_correct_responses, 
         SUM(number_of_times_asked) - SUM(number_of_correct_responses) nbr_incorrect_responses 
       FROM rpt_quiz_qq_analysis rpt, 
         promotion p, 
         (SELECT asset_code, 
           cms_value 
         FROM vw_cms_asset_value 
         WHERE KEY  =''QUESTION_NAME'' 
         AND locale = 
           (SELECT string_val FROM os_propertyset WHERE entity_name = ''default.language'' 
           ) 
         AND NOT EXISTS 
           (SELECT * 
           FROM vw_cms_asset_value 
           WHERE KEY  =''QUESTION_NAME'' 
           AND locale = '''||p_in_languageCode||''' 
           ) 
         UNION ALL 
         SELECT asset_code, 
           cms_value 
         FROM vw_cms_asset_value 
         WHERE KEY  =''QUESTION_NAME''
         AND locale = '''||p_in_languageCode||''' 
         ) question 
       WHERE (('''||p_in_promotionId||''' IS NULL 
       AND '''||p_in_diyQuizId||'''       IS NULL) 
       OR ('''||p_in_promotionId||'''     IS NOT NULL 
       AND rpt.promotion_id IN 
         (SELECT             * 
         FROM TABLE(get_array_varchar('''||p_in_promotionId||''')) 
         ) 
       OR ('''||p_in_diyQuizId||''' IS NOT NULL 
       AND rpt.q_id   IN 
         (SELECT * FROM TABLE(get_array_varchar('''||p_in_diyQuizId||''')) 
         )) )) 
       AND rpt.promotion_id    = p.promotion_id 
       AND question.asset_code = qq_cm_asset_name 
       AND p.promotion_status  = NVL('''||p_in_promotionStatus||''', p.promotion_status) 
       AND TRUNC(rpt.date_asked) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') 
       AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
       GROUP BY q_id, 
         qq_id, 
         question.cms_value 
       ) 
         ) rs 
         ORDER BY '|| v_sortCol ||'
    ) WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd;
    
     OPEN p_out_data FOR 
   l_query;

/* getQuizAnalysisDetailOneResultsSize */

      SELECT COUNT (1) INTO p_out_size_data
     FROM 
     ( SELECT qq_id, 
       Question, 
       nbr_of_times_asked, 
       nbr_correct_responses, 
       ROUND(DECODE(NVL(nbr_of_times_asked,0),0,0,((nbr_correct_responses/nbr_of_times_asked)*100)),2) perc_correct_responses, 
       nbr_incorrect_responses, 
       ROUND(DECODE(NVL(nbr_of_times_asked,0),0,0,((nbr_incorrect_responses/nbr_of_times_asked)*100)),2) perc_incorrect_responses 
     FROM 
       (SELECT qq_id AS qq_id, 
         question.cms_value Question, 
         SUM(number_of_times_asked) nbr_of_times_asked, 
         SUM(number_of_correct_responses) nbr_correct_responses, 
         SUM(number_of_times_asked) - SUM(number_of_correct_responses) nbr_incorrect_responses 
       FROM rpt_quiz_qq_analysis rpt, 
         promotion p, 
         (SELECT asset_code, 
           cms_value 
         FROM vw_cms_asset_value 
         WHERE KEY  ='QUESTION_NAME' 
         AND locale = 
           (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language' 
           ) 
         AND NOT EXISTS 
           (SELECT * 
           FROM vw_cms_asset_value 
           WHERE KEY  ='QUESTION_NAME' 
           AND locale = p_in_languageCode
           ) 
         UNION ALL 
         SELECT asset_code, 
           cms_value 
         FROM vw_cms_asset_value 
         WHERE KEY  ='QUESTION_NAME' 
         AND locale = p_in_languageCode 
         ) question 
       WHERE ((p_in_promotionId IS NULL 
       AND p_in_diyQuizId       IS NULL) 
       OR (p_in_promotionId     IS NOT NULL 
       AND rpt.promotion_id IN 
         (SELECT             * 
         FROM TABLE(get_array_varchar(p_in_promotionId)) 
         ) 
       OR (p_in_diyQuizId IS NOT NULL 
       AND rpt.q_id   IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_diyQuizId)) 
         )) )) 
       AND rpt.promotion_id    = p.promotion_id 
       AND question.asset_code = qq_cm_asset_name 
       AND p.promotion_status  = NVL(p_in_promotionStatus, p.promotion_status) 
       AND TRUNC(rpt.date_asked) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
       AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       GROUP BY q_id, 
         qq_id, 
         question.cms_value 
       )
       )  ;

/* getQuizAnalysisDetailOneResultsTotals */
 OPEN p_out_totals_data FOR
   SELECT NVL(SUM(nbr_of_times_asked),0)                                                                          AS nbr_of_times_asked, 
      NVL(SUM(nbr_correct_responses),0)                                                                              AS nbr_correct_responses, 
      ROUND(DECODE(NVL(SUM(nbr_of_times_asked),0),0,0,(SUM(nbr_correct_responses)/SUM(nbr_of_times_asked)*100)),2)   AS perc_correct_responses, 
      NVL(SUM(nbr_incorrect_responses),0)                                                                            AS nbr_incorrect_responses, 
      ROUND(DECODE(NVL(SUM(nbr_of_times_asked),0),0,0,(SUM(nbr_incorrect_responses)/SUM(nbr_of_times_asked)*100)),2) AS perc_incorrect_responses 
    FROM 
      (SELECT qq_id                                                                   AS qq_id, 
        qq_cm_asset_name AS Question, 
        SUM(number_of_times_asked) nbr_of_times_asked, 
        SUM(number_of_correct_responses) nbr_correct_responses, 
        SUM(number_of_times_asked) - SUM(number_of_correct_responses) nbr_incorrect_responses 
      FROM rpt_quiz_qq_analysis rpt, 
        promotion p 
      WHERE ((p_in_promotionId IS NULL --09/02/2014
       AND p_in_diyQuizId       IS NULL) 
       OR (p_in_promotionId     IS NOT NULL 
       AND rpt.promotion_id IN 
         (SELECT             * 
         FROM TABLE(get_array_varchar(p_in_promotionId)) 
         ) 
       OR (p_in_diyQuizId IS NOT NULL 
       AND rpt.q_id   IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_diyQuizId)) 
         )) ))        
      AND rpt.promotion_id   = p.promotion_id 
      AND p.promotion_status = NVL(p_in_promotionStatus, p.promotion_status) 
      AND TRUNC(rpt.date_asked) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
      AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
      GROUP BY q_id, 
        qq_id, 
        qq_cm_asset_name 
      ) ;
      
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
 END prc_getQuizAnalysisDetailOne;

   /* getQuizAnalysisDetailTwoResults */
procedure prc_getQuizAnalysisDetailTwo(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
IS
/******************************************************************************
  NAME:       prc_getQuizAnalysisDetailTwo
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)                                 
  ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getQuizAnalysisDetailTwo' ;
  v_stage        VARCHAR2 (500);
  v_sortCol             VARCHAR2(200);
  l_query VARCHAR2(32767); 

 BEGIN
  v_stage   := 'getQuizAnalysisDetailTwo';
  
 l_query  := 'SELECT * FROM
  ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;
       
  l_query := l_query ||'  '||'SELECT ROWNUM RN, 
         rs.* 
       FROM 
    ( SELECT qqa_answer, 
       is_correct, 
       number_of_times_selected, 
       ROUND(DECODE (number_of_times_selected, 0, 0, ((number_of_times_selected/number_of_times_asked) * 100)), 2) perc_of_times_selected 
     FROM 
       (SELECT fnc_cms_asset_code_val_extr(QQA_CM_ASSET_CODE,QQA_ANSWER_CM_KEY, '''||p_in_languageCode||''') AS qqa_answer, 
         DECODE(is_correct,0,''Incorrect'',''Correct'') is_correct, 
         SUM(number_of_times_selected) number_of_times_selected, 
         number_of_times_asked 
       FROM RPT_QUIZ_QQA_ANALYSIS qqa, 
         (SELECT rpt.qq_id, 
           rpt.promotion_id, 
           SUM(rpt.number_of_times_asked) number_of_times_asked 
         FROM RPT_QUIZ_QQ_ANALYSIS rpt, 
           promotion p 
         WHERE rpt.promotion_id = p.promotion_id 
         AND qq_id              = '''||p_in_qqId||''' 
         AND rpt.promotion_id   = NVL('''||p_in_promotionId||''',rpt.promotion_id) 
         AND TRUNC(rpt.date_asked) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') 
         AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
         AND p.promotion_status = NVL('''||p_in_promotionStatus||''', p.promotion_status) 
         GROUP BY rpt.qq_id, 
           rpt.promotion_id 
         ) qq 
       WHERE qqa.qq_id      = qq.qq_id 
       AND qqa.promotion_id = qq.promotion_id 
       AND qqa.qq_id        = '''||p_in_qqId||''' 
       AND qqa.promotion_id = NVL('''||p_in_promotionId||''',qqa.promotion_id) 
       AND TRUNC(qqa.date_asked) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') 
       AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''') 
       GROUP BY qqa_cm_asset_code,qqa_answer_cm_key,
         is_correct, 
         number_of_times_asked 
       ) 
         ) rs 
         ORDER BY  '|| v_sortCol ||'
    ) WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd;
     
         OPEN p_out_data FOR 
   l_query;

/* getQuizAnalysisDetailTwoResultsSize */
      SELECT COUNT (1) INTO p_out_size_data
     FROM 
    (SELECT qqa_answer, 
       is_correct, 
       number_of_times_selected, 
       ROUND(DECODE (number_of_times_selected, 0, 0, ((number_of_times_selected/number_of_times_asked) * 100)), 2) perc_of_times_selected 
     FROM 
       (SELECT fnc_cms_asset_code_val_extr(QQA_CM_ASSET_CODE,QQA_ANSWER_CM_KEY,p_in_languageCode) AS qqa_answer, 
         DECODE(is_correct,0,'Incorrect','Correct') is_correct, 
         SUM(number_of_times_selected) number_of_times_selected, 
         number_of_times_asked 
       FROM RPT_QUIZ_QQA_ANALYSIS qqa, 
         (SELECT rpt.qq_id, 
           rpt.promotion_id, 
           SUM(rpt.number_of_times_asked) number_of_times_asked 
         FROM RPT_QUIZ_QQ_ANALYSIS rpt, 
           promotion p 
         WHERE rpt.promotion_id = p.promotion_id 
         AND qq_id              = p_in_qqId 
         AND rpt.promotion_id   = NVL(p_in_promotionId,rpt.promotion_id) 
         AND TRUNC(rpt.date_asked) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
         AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         AND p.promotion_status = NVL(p_in_promotionStatus, p.promotion_status) 
         GROUP BY rpt.qq_id, 
           rpt.promotion_id 
         ) qq 
       WHERE qqa.qq_id      = qq.qq_id 
       AND qqa.promotion_id = qq.promotion_id 
       AND qqa.qq_id        = p_in_qqId 
       AND qqa.promotion_id = NVL(p_in_promotionId,qqa.promotion_id) 
       AND TRUNC(qqa.date_asked) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
       AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       GROUP BY fnc_cms_asset_code_val_extr(QQA_CM_ASSET_CODE,QQA_ANSWER_CM_KEY, p_in_languageCode), 
         is_correct, 
         number_of_times_asked 
       ) 
       )  ;

/* getQuizAnalysisDetailTwoResultsTotals */
 OPEN p_out_totals_data FOR
   SELECT NVL(SUM(number_of_times_selected),0)                                                                   AS number_of_times_selected, 
       ROUND(DECODE(NVL(number_of_times_asked,0),0,0,(SUM(number_of_times_selected)/number_of_times_asked*100)),2) AS perc_of_times_selected 
     FROM 
       (SELECT fnc_cms_asset_code_val_extr(QQA_CM_ASSET_CODE,QQA_ANSWER_CM_KEY, p_in_languageCode) AS qqa_answer, 
         DECODE(is_correct,0,'Incorrect','Correct') is_correct, 
         SUM(number_of_times_selected) number_of_times_selected, 
         number_of_times_asked 
       FROM RPT_QUIZ_QQA_ANALYSIS qqa, 
         (SELECT rpt.qq_id, 
           rpt.promotion_id, 
           SUM(rpt.number_of_times_asked) number_of_times_asked 
         FROM RPT_QUIZ_QQ_ANALYSIS rpt, 
           promotion p 
         WHERE rpt.promotion_id = p.promotion_id 
         AND qq_id              = p_in_qqId 
         AND rpt.promotion_id   = NVL(p_in_promotionId,rpt.promotion_id) 
         AND rpt.date_asked BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
         AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
         AND p.promotion_status = NVL(p_in_promotionStatus, p.promotion_status) 
         GROUP BY rpt.qq_id, 
           rpt.promotion_id 
         ) qq 
       WHERE qqa.qq_id      = qq.qq_id 
       AND qqa.promotion_id = qq.promotion_id 
       AND qqa.qq_id        = p_in_qqId 
       AND qqa.promotion_id = NVL(p_in_promotionId,qqa.promotion_id) 
       AND qqa.date_asked BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
       AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
       GROUP BY fnc_cms_asset_code_val_extr(QQA_CM_ASSET_CODE,QQA_ANSWER_CM_KEY, p_in_languageCode), 
         is_correct, 
         number_of_times_asked 
       ) 
     GROUP BY number_of_times_asked ;
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
 END prc_getQuizAnalysisDetailTwo;

   /* CHARTS */

/* getQuizQuestionAnalysisBarResults */
   
procedure prc_getQuizQuestionAnalysisBar(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR
    )
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getQuizQuestionAnalysisBar';
  v_stage        VARCHAR2(500);
 BEGIN
  v_stage   := 'getQuizQuestionAnalysisBar';

    OPEN p_out_data FOR SELECT fnc_cms_asset_code_val_extr(Question, 'QUESTION_NAME', p_in_languageCode) Question, 
       ROUND(((nbr_correct_responses  /nbr_of_times_asked)*100),2) perc_correct_responses, 
       ROUND(((nbr_incorrect_responses/nbr_of_times_asked)*100),2) perc_incorrect_responses 
     FROM 
       (SELECT qq_cm_asset_name AS Question, 
         SUM(number_of_times_asked) nbr_of_times_asked, 
         SUM(number_of_correct_responses) nbr_correct_responses, 
         SUM(number_of_times_asked) - SUM(number_of_correct_responses) nbr_incorrect_responses 
       FROM rpt_quiz_qq_analysis q, 
         promotion p 
       WHERE p.promotion_id = q.promotion_id 
       AND q.qq_id          = NVL(p_in_qqId,q.qq_id) 
       AND TRUNC(q.date_asked) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) 
       AND TO_DATE(p_in_toDate,p_in_localeDatePattern) 
          AND ((p_in_promotionId is NULL AND p_in_diyQuizId IS NULL)  
                  OR (p_in_promotionId is NOT NULL AND q.promotion_id IN 
                  (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))) OR 
                  (p_in_diyQuizId IS NOT NULL AND q.q_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_diyQuizId)))) ))  
       AND p.promotion_status = NVL(p_in_promotionStatus,p.promotion_status) 
       GROUP BY q_id, 
         qq_id, 
         qq_cm_asset_name 
       ) 
     ORDER BY 2 DESC ;
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
 END prc_getQuizQuestionAnalysisBar;  
     
     /* getQuizAttemptsPercentForOrgBarResults */
            
procedure prc_getQuizAttemptPercentForOB(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR
)
IS
/******************************************************************************
  NAME:       prc_getQuizAttemptPercentForOB
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014        Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J              01/08/2015       Bug Fix 58951 - Display Quiz Promotion Name in place of Badge Name                                  
  ******************************************************************************/

  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getQuizAttemptPercentForOB';
  v_stage        VARCHAR2(500);
 
 BEGIN
  v_stage   := 'getQuizAttemptPercentForOB';
 
    OPEN p_out_data FOR
 SELECT 
 --         c.q_name                       AS quiz_name,    --01/09/2015
            p.promotion_name AS quiz_name,  --01/08/2015   
            ROUND(((d.passed    /d.total_attempts)*100),2) AS quiz_attempts_pass_perc,  
            ROUND(((failed      /d.total_attempts)*100),2) AS quiz_attempts_fail_perc,  
            ROUND(((d.incomplete/d.total_attempts)*100),2) AS quiz_attempts_incompl_perc  
          FROM rpt_quiz_analysis c,  
            promotion p,  
            (SELECT quiz_id,  
              promotion_id,  
              maximum_attempts,  
              SUM(TOTAL_ATTEMPTS) total_attempts,  
              SUM(number_passed) passed,  
              SUM(number_failed) failed,  
              SUM(number_incomplete) incomplete  
            FROM rpt_quiz_attempts_analysis  
            WHERE 
             ((p_in_promotionId is NULL AND p_in_diyQuizId IS NULL)  
                  OR (p_in_promotionId is NOT NULL AND promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))) OR (p_in_diyQuizId IS NOT NULL AND quiz_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_diyQuizId)))) )) 
            AND NVL(TRUNC(submission_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern)  
            AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  
            GROUP BY quiz_id,  
              promotion_id,  
              maximum_attempts  
            ) d  
          WHERE p.promotion_id = d.promotion_id  
          AND d.quiz_id        = c.q_id  
          AND ((p_in_promotionId is NULL AND p_in_diyQuizId IS NULL)  
                  OR (p_in_promotionId is NOT NULL AND p.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))) OR (p_in_diyQuizId IS NOT NULL AND d.quiz_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_diyQuizId)))) ))         
          AND p.promotion_status = NVL(p_in_promotionStatus,promotion_status)  
          GROUP BY c.q_number_asked,  
            c.q_passing_score,  
            c.q_in_pool,  
            c.q_type,  
            passed,  
            failed,  
            incomplete,  
            total_attempts,  
--            c.q_name    --01/08/2015
            p.promotion_name;  --01/08/2015
            
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
 END prc_getQuizAttemptPercentForOB;  

/* getQuizAttemptStatusForOrgBarResults */
procedure prc_getQuizAttemptStatusForOB(
      p_in_diyQuizId           IN     VARCHAR,
      p_in_fromDate            IN     VARCHAR,
      p_in_toDate              IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_localeDatePattern   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_in_qqId                IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR
)
IS
/******************************************************************************
  NAME:       prc_getQuizAttemptPercentForOB
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014        Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J              01/08/2015        Bug Fix 58951 - Display Quiz Promotion Name in place of Badge Name                                  
  ******************************************************************************/

  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getQuizAttemptStatusForOB';
  v_stage        VARCHAR2(500);
 
 BEGIN
  v_stage   := 'getQuizAttemptStatusForOB';

    OPEN p_out_data FOR
 SELECT 
--            c.q_name              AS quiz_name,   --01/08/2015
            p.promotion_name AS quiz_name,  --01/08/2015  
            d.passed              AS quiz_attempts_passed,  
            d.failed              AS quiz_attempts_failed,  
            d.incomplete          AS quiz_attempts_incomplete  
          FROM rpt_quiz_analysis c,  
            promotion p,  
            (SELECT quiz_id,  
              promotion_id,  
              maximum_attempts,  
              SUM(number_passed) passed,  
              SUM(number_failed) failed,  
              SUM(number_incomplete) incomplete  
            FROM rpt_quiz_attempts_analysis  
            WHERE         
            ((p_in_promotionId is NULL AND p_in_diyQuizId IS NULL)  
                  OR (p_in_promotionId is NOT NULL AND promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))) OR (p_in_diyQuizId IS NOT NULL AND quiz_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_diyQuizId)))) )) 
             AND NVL(TRUNC(submission_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern)  
            AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  
            GROUP BY quiz_id,  
              promotion_id,  
              maximum_attempts  
            ) d  
          WHERE p.promotion_id = d.promotion_id  
          AND d.quiz_id        = c.q_id  
          AND ((p_in_promotionId is NULL AND p_in_diyQuizId IS NULL)  
                  OR (p_in_promotionId is NOT NULL AND p.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))) OR (p_in_diyQuizId IS NOT NULL AND quiz_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_diyQuizId)))) )) 
             AND p.promotion_status = NVL(p_in_promotionStatus,promotion_status)  
          GROUP BY c.q_number_asked,  
            c.q_passing_score,  
            c.q_in_pool,  
            c.q_type,  
            passed,  
            failed,  
            incomplete,  
            maximum_attempts,  
--            c.q_name      --01/08/2015;
            p.promotion_name;  --01/08/2015;
        
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
 END prc_getQuizAttemptStatusForOB;
END pkg_query_quiz_analysis;
/