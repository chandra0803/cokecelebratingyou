CREATE OR REPLACE PACKAGE pkg_quiz_analysis_reports IS
    PROCEDURE prc_quiz_analysis_reports (p_in_requested_user_id IN NUMBER,
                                         p_in_start_date        IN DATE,
                                         p_in_end_date          IN DATE,
                                         p_out_return_code      OUT NUMBER,
                                         p_out_error_message    OUT VARCHAR2);
                                    
    PROCEDURE prc_rpt_quiz_analysis(p_in_requested_user_id IN  NUMBER,
                                    p_in_start_date        IN  DATE,
                                    p_in_end_date          IN  DATE);
    PROCEDURE prc_rpt_qq_analysis(p_in_requested_user_id IN  NUMBER,
                                    p_in_start_date        IN  DATE,
                                    p_in_end_date          IN  DATE);
    PROCEDURE prc_rpt_qqa_analysis(p_in_requested_user_id IN  NUMBER,
                                    p_in_start_date        IN  DATE,
                                    p_in_end_date          IN  DATE);
    PROCEDURE prc_rpt_quiz_claim_analysis(p_in_requested_user_id IN  NUMBER,
                                    p_in_start_date        IN  DATE,
                                    p_in_end_date          IN  DATE);
END pkg_quiz_analysis_reports;
/
CREATE OR REPLACE PACKAGE BODY pkg_quiz_analysis_reports
IS

PROCEDURE prc_quiz_analysis_reports(p_in_requested_user_id IN NUMBER,
                                    p_in_start_date        IN DATE,
                                    p_in_end_date          IN DATE,
                                    p_out_return_code      OUT NUMBER,
                                    p_out_error_message    OUT VARCHAR2)
IS
  v_stage        execution_log.text_line%type;
BEGIN
  v_stage := 'pkg_quiz_analysis_reports.prc_rpt_quiz_analysis';
  pkg_quiz_analysis_reports.prc_rpt_quiz_analysis(p_in_requested_user_id,
                                                     p_in_start_date,
                                                     p_in_end_date);
  
  v_stage := 'pkg_quiz_analysis_reports.prc_rpt_quiz_claim_analysis';                                                    
  pkg_quiz_analysis_reports.prc_rpt_quiz_claim_analysis(p_in_requested_user_id,
                                                           p_in_start_date,
                                                           p_in_end_date);
                                                           
  v_stage := 'pkg_quiz_analysis_reports.prc_rpt_qq_analysis';
  pkg_quiz_analysis_reports.prc_rpt_qq_analysis(p_in_requested_user_id,
                                                   p_in_start_date,
                                                   p_in_end_date);
  
  v_stage := 'pkg_quiz_analysis_reports.prc_rpt_qqa_analysis';                                                 
  pkg_quiz_analysis_reports.prc_rpt_qqa_analysis(p_in_requested_user_id,
                                                    p_in_start_date,
                                                    p_in_end_date);
  p_out_return_code := 0;
  
EXCEPTION
 WHEN OTHERS THEN
  p_out_return_code := 99;
  --p_out_error_message : = 'Error occoured during '||v_stage||' --> '||SQLERRM;
  
  prc_execution_log_entry('prc_quiz_analysis_reports',1,'ERROR',SQLERRM,null);
  
END;



PROCEDURE prc_rpt_quiz_claim_analysis(p_in_requested_user_id  IN NUMBER,
                                      p_in_start_date         IN DATE,
                                      p_in_end_date           IN DATE)
   IS
      v_rpt_q_attempts_analysis_id   rpt_quiz_attempts_analysis.rpt_quiz_attempts_analysis_id%TYPE;
      v_quiz_id                      rpt_quiz_attempts_analysis.quiz_id%TYPE;
      v_promotion_id                 rpt_quiz_attempts_analysis.promotion_id%TYPE;
      v_number_passed                rpt_quiz_attempts_analysis.number_passed%TYPE;
      v_number_failed                rpt_quiz_attempts_analysis.number_failed%TYPE;
      v_total_attempts                rpt_quiz_attempts_analysis.total_attempts%TYPE;
      v_number_incomplete            rpt_quiz_attempts_analysis.number_incomplete%TYPE;
      v_submission_date              rpt_quiz_attempts_analysis.date_created%TYPE;
      v_date_created                 rpt_quiz_attempts_analysis.date_created%TYPE;
      v_created_by                   rpt_quiz_attempts_analysis.created_by%TYPE:= p_in_requested_user_id;
      v_claim_id                     claim.claim_id%TYPE;
      v_number_taken                 NUMBER (10);
      
      /***************************************************************
      
      Ravi Dhanekula       12/3/2012    Added total_attempts to the report rpt_quiz_attempts_analysis
                                    10/07/2013  Fixed the defect # 4059.
      ***************************************************************/

      CURSOR cur_quiz_promotion_attempts
      IS
          SELECT DISTINCT (TRUNC (c.submission_date)) AS v_submission_date,
                 c.promotion_id AS v_promotion_id,
                 NVL(pq.quiz_id,dq.quiz_id) AS v_quiz_id,
                 DECODE(pq.quiz_id,NULL,dq.maximum_attempts,pq.maximum_attempts) maximum_attempts
            FROM claim c, 
                 quiz_claim qc, 
                 promo_quiz pq,
                 promotion p,
                 diy_quiz dq
           WHERE c.claim_id = qc.claim_id
           AND p.promotion_type IN('quiz','diy_quiz')
             AND c.promotion_id = pq.promotion_id 
             AND p.promotion_id = pq.promotion_id(+)       
             AND P.promotion_id = dq.promotion_id(+) --10/07/2013
             AND (p_in_start_date <= c.submission_date AND c.submission_date <= p_in_end_date)
                 ;
             
   BEGIN

      FOR rec_quiz_promotion_attempts IN cur_quiz_promotion_attempts
      LOOP
         SELECT rpt_q_attempts_analysis_sq_pk.NEXTVAL
           INTO v_rpt_q_attempts_analysis_id
           FROM DUAL;

         v_submission_date := rec_quiz_promotion_attempts.v_submission_date;
         v_quiz_id := rec_quiz_promotion_attempts.v_quiz_id;
         v_promotion_id := rec_quiz_promotion_attempts.v_promotion_id;

         -- get the total quizzes passed for the promotion
         SELECT COUNT (*)
           INTO v_number_passed
           FROM claim c, quiz_claim qc
          WHERE TRUNC (c.submission_date) = v_submission_date
            AND c.promotion_id = v_promotion_id
            AND qc.claim_id = c.claim_id
            AND qc.pass = 1;

         -- get the total quizzes failed for the promotion
         SELECT COUNT (*)
           INTO v_number_failed
           FROM claim c, quiz_claim qc
          WHERE TRUNC (c.submission_date) = v_submission_date
            AND c.promotion_id = v_promotion_id
            AND qc.claim_id = c.claim_id
            AND qc.pass = 0;

         -- get the total quizzes for the promotion and the submission_date
         SELECT COUNT (*)
           INTO v_number_taken
           FROM claim c, quiz_claim qc
          WHERE TRUNC (c.submission_date) = v_submission_date
            AND c.promotion_id = v_promotion_id
            AND qc.claim_id = c.claim_id;

         -- calculate the incomplete quizzes for the promotion
         v_total_attempts := v_number_taken;
         v_number_incomplete := v_number_taken;
         v_number_incomplete := v_number_incomplete - v_number_passed;
         v_number_incomplete := v_number_incomplete - v_number_failed;
         
         BEGIN
            SELECT rpt_quiz_attempts_analysis_id
              INTO v_rpt_q_attempts_analysis_id
              FROM rpt_quiz_attempts_analysis
             WHERE promotion_id    = rec_quiz_promotion_attempts.v_promotion_id
               AND quiz_id         = rec_quiz_promotion_attempts.v_quiz_id   
               AND submission_date = rec_quiz_promotion_attempts.v_submission_date;
               
            UPDATE rpt_quiz_attempts_analysis
               SET maximum_attempts  = rec_quiz_promotion_attempts.maximum_attempts,
                   number_incomplete = v_number_incomplete,
                   number_failed     = v_number_failed,
                   total_attempts = v_total_attempts,
                   number_passed     = v_number_passed,
                   modified_by       = v_created_by,
                   date_modified     = SYSDATE
             WHERE rpt_quiz_attempts_analysis_id   = v_rpt_q_attempts_analysis_id;
               
         EXCEPTION
          WHEN NO_DATA_FOUND THEN   
            INSERT INTO rpt_quiz_attempts_analysis
                       (rpt_quiz_attempts_analysis_id, 
                        quiz_id,
                        promotion_id, 
                        number_passed, 
                        number_failed,
                        number_incomplete, 
                        total_attempts,
                        submission_date,
                        maximum_attempts,
                        date_created,
                        created_by)
                VALUES (v_rpt_q_attempts_analysis_id, 
                        v_quiz_id,
                        v_promotion_id, 
                        v_number_passed, 
                        v_number_failed,
                        v_number_incomplete, 
                        v_total_attempts,
                        v_submission_date,
                        rec_quiz_promotion_attempts.maximum_attempts,
                        SYSDATE,
                        v_created_by);
         END;
      END LOOP;                                 -- cur_quiz_promotion_attempts

   END prc_rpt_quiz_claim_analysis;

   PROCEDURE prc_rpt_qqa_analysis(p_in_requested_user_id IN NUMBER,
                                      p_in_start_date IN DATE,
                                      p_in_end_date IN DATE)
   
   IS
      v_rpt_quiz_qqa_analysis_id   rpt_quiz_qqa_analysis.rpt_quiz_qqa_analysis_id%TYPE;
      v_qq_id                      rpt_quiz_qqa_analysis.qq_id%TYPE;
      v_qqa_id                     rpt_quiz_qqa_analysis.qqa_id%TYPE;
      v_promotion_id               rpt_quiz_qqa_analysis.promotion_id%TYPE;
      v_is_correct                 rpt_quiz_qqa_analysis.is_correct%TYPE;
      v_number_of_times_selected   rpt_quiz_qqa_analysis.number_of_times_selected%TYPE;
      v_date_asked                 rpt_quiz_qqa_analysis.date_asked%TYPE;
      v_date_created               rpt_quiz_qqa_analysis.date_created%TYPE;
      v_created_by                 rpt_quiz_qqa_analysis.created_by%TYPE:= p_in_requested_user_id;

      CURSOR cur_date_submitted
      IS
         SELECT DISTINCT (TRUNC (date_modified)) AS date_modified
                    FROM claim;

      CURSOR cur_quiz_answer_claim (in_date_modified DATE)
      IS
          SELECT  a.quiz_question_id AS qq_id,
                  COUNT(c.submission_date) AS number_of_times_selected,
                  c.promotion_id AS promotion_id,
                  TRUNC (c.submission_date) AS date_asked,
                  is_correct,
                  cm_asset_code,
                  answer_cm_key
          FROM quiz_question_answer a, quiz_response b, claim c
          WHERE a.quiz_question_answer_id = b.selected_quiz_answer_id(+)
            AND b.claim_id = c.claim_id(+)
            AND (p_in_start_date <= c.submission_date AND c.submission_date <= p_in_end_date
                 OR p_in_start_date <= b.date_created AND b.date_created <= p_in_end_date
                   OR p_in_start_date <= b.date_modified AND b.date_modified <= p_in_end_date
                   OR p_in_start_date <= a.date_created AND a.date_created <= p_in_end_date 
                   OR p_in_start_date <= a.date_modified AND a.date_modified <= p_in_end_date)
        GROUP BY a.quiz_question_id ,
                 c.promotion_id,
                 TRUNC (c.submission_date),
                 is_correct,
                 cm_asset_code,
                 answer_cm_key;
               
   BEGIN
       FOR rec_quiz_answer_claim IN cur_quiz_answer_claim (v_date_asked)
      LOOP
          
           
             BEGIN
              
               SELECT rpt_quiz_qqa_analysis_id
                 INTO v_rpt_quiz_qqa_analysis_id
                 FROM rpt_quiz_qqa_analysis
                WHERE qq_id         = rec_quiz_answer_claim.qq_id                  
                  AND promotion_id  = rec_quiz_answer_claim.promotion_id
                  AND NVL(date_asked,TO_DATE ('1900-01-01', 'yyyy-mm-dd'))    = NVL(rec_quiz_answer_claim.date_asked,TO_DATE ('1900-01-01', 'yyyy-mm-dd'))
                  AND is_correct    = rec_quiz_answer_claim.is_correct
                  AND qqa_cm_asset_code = rec_quiz_answer_claim.cm_asset_code
                  and qqa_answer_cm_key = rec_quiz_answer_claim.answer_cm_key;
                  
              UPDATE rpt_quiz_qqa_analysis
                 SET number_of_times_selected = rec_quiz_answer_claim.number_of_times_selected,
                     date_modified = SYSDATE,
                     modified_by   = v_created_by
               WHERE rpt_quiz_qqa_analysis_id = v_rpt_quiz_qqa_analysis_id;  
            
            EXCEPTION 
            WHEN NO_DATA_FOUND THEN
            
              SELECT rpt_qqa_analysis_sq_pk.NEXTVAL
                INTO v_rpt_quiz_qqa_analysis_id
                FROM DUAL;
                
             INSERT INTO rpt_quiz_qqa_analysis
                        (rpt_quiz_qqa_analysis_id, 
                         qq_id, 
                         qqa_id, 
                         promotion_id,
                         number_of_times_selected, 
                         is_correct,
                         date_asked, 
                         date_created, 
                         created_by,
                         qqa_cm_asset_code,
                         qqa_answer_cm_key
                        )
                 VALUES (v_rpt_quiz_qqa_analysis_id, 
                         rec_quiz_answer_claim.qq_id,
                         NULL,--rec_quiz_answer_claim.qqa_id,
                         rec_quiz_answer_claim.promotion_id,
                         rec_quiz_answer_claim.number_of_times_selected, 
                         rec_quiz_answer_claim.is_correct,
                         rec_quiz_answer_claim.date_asked, 
                         SYSDATE, 
                         v_created_by,
                         rec_quiz_answer_claim.cm_asset_code,
                         rec_quiz_answer_claim.answer_cm_key
                        );
            END;
      END LOOP;                      -- cur_quiz_answer_claim(v_date_asked)

   END prc_rpt_qqa_analysis;

   PROCEDURE prc_rpt_qq_analysis(p_in_requested_user_id IN NUMBER,
                                      p_in_start_date IN DATE,
                                      p_in_end_date IN DATE)
   IS
      v_rpt_quiz_qq_analysis_id       rpt_quiz_qq_analysis.rpt_quiz_qq_analysis_id%TYPE;
      v_q_id                          rpt_quiz_qq_analysis.q_id%TYPE;
      v_qq_id                         rpt_quiz_qq_analysis.qq_id%TYPE;
      v_promotion_id                  rpt_quiz_qq_analysis.promotion_id%TYPE;
      v_number_of_times_asked         rpt_quiz_qq_analysis.number_of_times_asked%TYPE;
      v_number_of_correct_responses   rpt_quiz_qq_analysis.number_of_correct_responses%TYPE;
      v_date_asked                    rpt_quiz_qq_analysis.date_asked%TYPE;
      v_date_created                  rpt_quiz_qq_analysis.date_created%TYPE;
      v_created_by                    rpt_quiz_qq_analysis.created_by%TYPE:= p_in_requested_user_id;

      CURSOR cur_date_submitted
      IS
         SELECT DISTINCT (TRUNC (date_modified)) AS date_modified
                    FROM claim;

      CURSOR cur_quiz_question_claim (in_date_modified DATE)
      IS
         SELECT   qq.quiz_id AS q_id,
                  qr.quiz_question_id AS qq_id,
--                  qr.selected_quiz_answer_id AS qqa_id,
                  COUNT (qr.quiz_question_id) AS number_of_times_asked,
                  c.promotion_id AS promotion_id,
                  TRUNC (c.submission_date) AS date_asked,
                  qq.cm_asset_name,
                  SUM(DECODE(qr.correct,1,1,0)) nbr_of_correct_responses
             FROM claim c, quiz_response qr, quiz_question qq
            WHERE --TRUNC (c.date_modified) = in_date_modified
                  qr.claim_id = c.claim_id
              AND qr.quiz_question_id = qq.quiz_question_id
              AND (p_in_start_date <= c.submission_date AND c.submission_date <= p_in_end_date
                   OR p_in_start_date <= qr.date_created AND qr.date_created <= p_in_end_date
                   OR p_in_start_date <= qr.date_modified AND qr.date_modified <= p_in_end_date
                   OR p_in_start_date <= qq.date_created AND qq.date_created <= p_in_end_date 
                   OR p_in_start_date <= qq.date_modified AND qq.date_modified <= p_in_end_date)
         GROUP BY --c.date_modified,
                  TRUNC(c.submission_date),
                  qr.quiz_question_id,
                  qq.cm_asset_name,
--                  qr.selected_quiz_answer_id,
                  c.promotion_id,
                  qq.quiz_id;

   BEGIN
    

       FOR rec_quiz_question_claim IN cur_quiz_question_claim (v_date_asked)
       LOOP
            v_q_id := rec_quiz_question_claim.q_id;
            v_qq_id := rec_quiz_question_claim.qq_id;
            v_promotion_id := rec_quiz_question_claim.promotion_id;
            v_number_of_times_asked :=  rec_quiz_question_claim.number_of_times_asked;

         BEGIN
            SELECT rpt_quiz_qq_analysis_id
              INTO v_rpt_quiz_qq_analysis_id
              FROM rpt_quiz_qq_analysis 
             WHERE q_id     = rec_quiz_question_claim.q_id
               AND qq_id    = rec_quiz_question_claim.qq_id   
               AND promotion_id = rec_quiz_question_claim.promotion_id
               AND date_asked   = rec_quiz_question_claim.date_asked;
            
            UPDATE rpt_quiz_qq_analysis
               SET number_of_times_asked       = rec_quiz_question_claim.number_of_times_asked, 
                   number_of_correct_responses = rec_quiz_question_claim.nbr_of_correct_responses,
                   modified_by    =  v_created_by,
                   date_modified  =  SYSDATE
             WHERE rpt_quiz_qq_analysis_id = v_rpt_quiz_qq_analysis_id; 
         
         EXCEPTION      
         WHEN NO_DATA_FOUND THEN
         
            SELECT rpt_qq_analysis_sq_pk.NEXTVAL
              INTO v_rpt_quiz_qq_analysis_id
              FROM DUAL;
                         
            INSERT INTO rpt_quiz_qq_analysis
                        (rpt_quiz_qq_analysis_id, 
                         q_id, 
                         qq_id,
                         promotion_id, 
                         number_of_times_asked,
                         number_of_correct_responses, 
                         date_asked,
                         date_created, 
                         created_by,
                         qq_cm_asset_name
                        )
                 VALUES (v_rpt_quiz_qq_analysis_id, 
                         v_q_id, 
                         v_qq_id,
                         v_promotion_id, 
                         v_number_of_times_asked,
                         rec_quiz_question_claim.nbr_of_correct_responses,
                         rec_quiz_question_claim.date_asked,
                         SYSDATE, 
                         v_created_by,
                         rec_quiz_question_claim.cm_asset_name
                        );
                        
         END;
       
       END LOOP;                    -- cur_quiz_question_claim(v_date_asked)

   END prc_rpt_qq_analysis;

PROCEDURE prc_rpt_quiz_analysis(p_in_requested_user_id IN  NUMBER,
                                p_in_start_date        IN  DATE,
                                p_in_end_date          IN  DATE)
IS   

 /***************************************************************
      
      Ravi Dhanekula       04/04/2014 Fixed the Bug # 51801.
      ***************************************************************/

 v_created_by                   RPT_QUIZ_ANALYSIS.CREATED_BY%TYPE := NVL(p_in_requested_user_id,0);
 
BEGIN
                
      MERGE INTO rpt_quiz_analysis s
       USING (SELECT q.quiz_id AS q_id,
                     q.NAME AS q_name, q.description AS q_description,
                     DECODE(UPPER(quiz_type),'FIXED',count(qq.quiz_question_id),q.number_asked) questions_to_ask,
                     q.passing_score  AS q_passing_score,
                     INITCAP(q.quiz_type)      AS q_type, --04/04/2014 Bug # 51801
                     q.status_type    AS q_status_type,                     
                     count(qq.quiz_question_id) questions_in_pool                                  
                FROM quiz q, quiz_question qq                
               WHERE q.quiz_id = qq.quiz_id
                 AND (p_in_start_date <= q.date_created AND q.date_created <= p_in_end_date
                      OR p_in_start_date <= q.date_modified AND q.date_modified <= p_in_end_date
                      OR p_in_start_date <= qq.date_created AND qq.date_created <= p_in_end_date 
                      OR p_in_start_date <= qq.date_modified AND qq.date_modified <= p_in_end_date)
            GROUP BY q.quiz_id,
                     q.name,
                     q.description ,
                     q.number_asked,
                     q.passing_score,
                     q.quiz_type,
                     q.status_type) t
          ON ( s.q_id = t.q_id ) 
        WHEN MATCHED THEN UPDATE SET  s.Q_NAME          = t.Q_NAME,
                                      s.Q_DESCRIPTION   = t.Q_DESCRIPTION,
                                      s.Q_NUMBER_ASKED  = t.QUESTIONS_TO_ASK,
                                      s.Q_PASSING_SCORE = t.Q_PASSING_SCORE,
                                      s.Q_TYPE          = t.Q_TYPE,
                                      s.Q_STATUS_TYPE   = t.Q_STATUS_TYPE,                                      
                                      s.date_modified   = SYSDATE,                                                                 
                                      s.modified_by     = v_created_by                                                                                                          
        WHEN NOT MATCHED THEN INSERT (rpt_quiz_analysis_id,
                                      q_id,
                                      q_name, 
                                      q_description,
                                      q_number_asked, 
                                      q_passing_score, 
                                      q_type, 
                                      q_status_type,  
                                      q_in_pool,
                                      date_created,
                                      created_by)
                                    VALUES
                                     (RPT_QUIZ_ANALYSIS_SQ_PK.NEXTVAL,
                                      t.q_id,
                                      t.q_name,
                                      t.q_description,
                                      t.questions_to_ask,
                                      t.q_passing_score,
                                      t.q_type,
                                      t.q_status_type,
                                      t.questions_in_pool,
                                      SYSDATE,
                                      v_created_by);  

END prc_rpt_quiz_analysis;
   
END pkg_quiz_analysis_reports;
/