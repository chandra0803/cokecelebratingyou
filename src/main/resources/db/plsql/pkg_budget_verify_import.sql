CREATE OR REPLACE PACKAGE pkg_budget_verify_import IS

/*-----------------------------------------------------------------------------
   Purpose:  To verify stage_budget_import_record

   Person           Date           Comments
   -----------      ----------     -------------------------------------------
   Swati CR         11/19/2014     Initial creation                                          

-----------------------------------------------------------------------------*/

PROCEDURE p_budget_verify 
  (pi_import_file_id     IN  NUMBER,   
   pi_user_id            IN  NUMBER,
   pi_promotion_id       IN  NUMBER,
   p_out_total_error_rec OUT NUMBER,
   p_out_returncode      OUT NUMBER
   );

PROCEDURE p_budget_load
  (p_import_file_id  IN NUMBER,
   p_user_id         IN NUMBER,                                                                           
   p_total_error_rec OUT NUMBER,
   p_out_returncode  OUT NUMBER) ;
END; -- Package spec

/

CREATE OR REPLACE PACKAGE BODY pkg_budget_verify_import
IS

/*-----------------------------------------------------------------------------
   Purpose:  To verify stage_budget_import_record

   Person           Date           Comments
   -----------      ----------     -------------------------------------------
   Swati CR         11/19/2014     Initial creation                                            
  Suresh J          01/19/2015     Fix for Bug 58977 - error records count in File Load Page doesn't have any link attached   
  Ravi Dhanekula    04/21/2015     Fix for Bug # 61412 - INITIAL budget_history rec did not have zero for CURRENT_VALUE_BEFORE_XACTION
  Ravi Dhanekula    08/06/2015     Fix for Bug # 63608 - Budgets file load country option not working
  nagarajs          11/13/2015     Fix for Bug 64488 - Budget File Load Fails When Prior Budget Segment Budgets Are Closed
  nagarajs          03/30/2016    Bug 66243 - Budget file load is not excluding the error records when add amount to the existing budget
  Ravi Arumugam     05/04/2016    Bug 66596-Admin is able to load budget for PAX with negative amount
  Sherif            06/16/2016    Bug 66712--Admin is able to load node/org.unit budget for nodes with all inactive participants
                                  when recognition audience set to all active participants as givers.
  Suresh J           03/28/2017   G6-2006 Update budget load process to allow budget load for inactive when TnC's are on    
  Ravi Dhanekula    04/30/2017    Added seperate audience refresh logic to by pass active pax check which to allow budget load for inactive pax when TnC's are on. 
  Ravi Dhanekula    07/20/2017    Bug # 73517 - budget file load not working for 'pax' type audiences   
  Sherif Basha      07/28/2017    G6-2765 - Bravo - Budget Files with Negative values are converted to positive values in Stage process
  Gorantla          11/08/2017    G6-3363 Admin should able to load budget for PAX with negative values via load  
  Gorantla          11/29/2017    G6-3363 Admin should able to load budget for PAX with negative values via load and calling procedure in appropiate place
  Chidamba          01/05/2017    Bug 72881 - Budget File Load - Audit column populate the user_id of the person loading the file.                       
-----------------------------------------------------------------------------*/

  g_import_file_id          NUMBER(18) := 0;
  g_timestamp               import_record_error.date_created%TYPE := SYSDATE;
  g_created_by              import_record_error.created_by%TYPE ;  --01/05/2017 Bug#72881 removed := 0 


FUNCTION f_get_error_record_count
  (p_import_file_id  IN  NUMBER
  )
RETURN INTEGER
IS

  c_process_name       CONSTANT execution_log.process_name%type := 'F_GET_ERROR_RECORD_COUNT';
  c_release_level      CONSTANT execution_log.release_level%type := '1.0';
  v_error_record_count INTEGER;

BEGIN
  --get count of any existing errors
  SELECT COUNT(DISTINCT import_record_id) AS error_cnt
    INTO v_error_record_count
    FROM import_record_error 
   WHERE import_file_id = p_import_file_id;

   RETURN v_error_record_count;
   
EXCEPTION
  WHEN OTHERS THEN
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', SQLERRM, NULL);
    RAISE;
END f_get_error_record_count;


PROCEDURE p_rpt_budget_amount_err
( p_import_file_id   IN     stage_budget_import_record.import_file_id%TYPE,
  p_budget_master_id IN     budget_master.budget_master_id%TYPE,  -- 11/08/2017 
  p_total_err_count IN OUT NUMBER
) IS

/*
Budget amount validation : Mark error for any of the following found
                           i.Budget amount is null
                           ii.Budget amount is not a whole number
*/

   c_process_name       CONSTANT execution_log.process_name%type  := 'P_RPT_BUDGET_AMOUNT_ERR';
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';
   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN

   v_msg := 'INSERT import_record_error';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   ( SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
            stg_err.import_file_id,
            stg_err.import_record_id,
            stg_err.item_key,
            stg_err.field_name  AS param1,
            stg_err.field_value AS param2,
            NULL                AS param3,
            NULL                AS param4,
            g_created_by        AS created_by,
            g_timestamp         AS date_created
       FROM (                     
              WITH stg_rec AS
                   (
                    SELECT s.*,
                           i.budget_segment_id                    -- 11/08/2017
                      FROM stage_budget_import_record s,
                           import_file i                          -- 11/08/2017
                     WHERE s.import_file_id = i.import_file_id    -- 11/08/2017
                       AND s.import_file_id = p_import_file_id
                       AND s.import_record_id NOT IN
                             (SELECT e.import_record_id
                                FROM import_record_error e
                               WHERE e.import_file_id = p_import_file_id
                                 AND e.date_created < g_timestamp
                              )
                    )
                   SELECT sr.import_file_id,
                          sr.import_record_id,
                          'admin.fileload.errors.AMOUNT_NOT_WHOLE_NUMBER' AS item_key,
                          'Budget Amount' AS field_name,
                          sr.budget_amount AS field_value
                     FROM stg_rec sr
                    WHERE MOD(sr.budget_amount, 1) <> 0
                    UNION ALL
                   SELECT sr1.import_file_id,
                          sr1.import_record_id,
                          'system.errors.REQUIRED.BUDGET_AMOUNT' AS item_key,
                          'Budget Amount' AS field_name,
                          sr1.budget_amount AS field_value
                     FROM stg_rec sr1
                    WHERE sr1.budget_amount IS NULL
                    UNION ALL  -- Start  11/08/2017
                   SELECT sr2.import_file_id,
                          sr2.import_record_id,
                          'admin.fileload.errors.PAX_BUDGET_IN_NEGATIVE' AS item_key,
                          'Budget Id'     AS field_name,
                          sr2.budget_amount AS field_value                        
                     FROM stg_rec sr2
                    WHERE EXISTS (SELECT 1
                                    FROM budget_master bm,
                                         budget_segment bs,
                                         budget b
                                   WHERE bm.budget_master_id = p_budget_master_id
                                     AND bm.budget_master_id = bs.budget_master_id
                                     AND bs.budget_segment_id = b.budget_segment_id
                                     AND b.budget_segment_id = sr2.budget_segment_id 
                                     AND b.user_id = sr2.user_id
                                     AND (current_value + budget_amount) < 0 )
                    UNION ALL
                   SELECT sr3.import_file_id,
                          sr3.import_record_id,
                          'admin.fileload.errors.NODE_BUDGET_IN_NEGATIVE' AS item_key,
                          'Budget Id'     AS field_name,
                          sr3.budget_amount AS field_value                        
                     FROM stg_rec sr3
                    WHERE EXISTS (SELECT 1
                                    FROM budget_master bm,
                                         budget_segment bs,
                                         budget b
                                   WHERE bm.budget_master_id = p_budget_master_id
                                     AND bm.budget_master_id = bs.budget_master_id
                                     AND bs.budget_segment_id = b.budget_segment_id
                                     AND b.budget_segment_id = sr3.budget_segment_id 
                                     AND b.node_id = sr3.node_id
                                     AND (current_value + budget_amount) < 0 )                                     
                -- End  11/08/2017                              
         -- 07/28/2017 commented to allow negative value deposits
        /*           UNION ALL
                   SELECT sr2.import_file_id,                                              --05/04/2016 Bug 66596
                          sr2.import_record_id,                                            --05/04/2016 Bug 66596
                          'admin.fileload.errors.AMOUNT_NOT_NEGATIVE_NUMBER' AS item_key,  --05/04/2016 Bug 66596
                          'Budget Amount' AS field_name,                                   --05/04/2016 Bug 66596 
                          sr2.budget_amount AS field_value                                 --05/04/2016 Bug 66596
                    FROM  stg_rec sr2                                                      --05/04/2016 Bug 66596
                   WHERE sr2.budget_amount < 0          */                                   --05/04/2016 Bug 66596
             ) stg_err  
    );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_budget_amount_err;


PROCEDURE p_rpt_user_validation_err
( p_import_file_id  IN     stage_budget_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   
/*
User validation : Mark error for any of the following found
                  i.User id is null
                  ii.User id not found in system as a pax
                  iii.Inactive pax
*/

   c_process_name       CONSTANT execution_log.process_name%type  := 'P_RPT_USER_VALIDATION_ERR';
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';
   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
   v_tc_accepted        os_propertyset.boolean_val%TYPE;      --03/28/2017
   
BEGIN

-- Find status of Terms and Conditions variable --03/28/2017
   SELECT boolean_val 
      INTO v_tc_accepted 
      FROM os_propertyset 
     WHERE lower(ENTITY_NAME) = 'termsandconditions.used';

   v_msg := 'INSERT import_record_error';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   ( SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
            stg_err.import_file_id,
            stg_err.import_record_id,
            stg_err.item_key,
            stg_err.field_name  AS param1,
            stg_err.field_value AS param2,
            NULL                AS param3,
            NULL                AS param4,
            g_created_by        AS created_by,
            g_timestamp         AS date_created
       FROM (                     
              WITH stg_rec AS
                   (
                    SELECT s.*
                      FROM stage_budget_import_record s
                     WHERE s.import_file_id = p_import_file_id
                       AND s.import_record_id NOT IN
                             (SELECT e.import_record_id
                                FROM import_record_error e
                               WHERE e.import_file_id = p_import_file_id
                                 AND e.date_created < g_timestamp
                              )
                    )
                   SELECT sr.import_file_id,
                          sr.import_record_id,
                          'admin.fileload.errors.USER_NOT_DEFINED' AS item_key,
                          'User Id'     AS field_name,
                          sr.user_id    AS field_value
                     FROM stg_rec sr
                    WHERE sr.user_id IS NULL
                    UNION ALL
                   SELECT sr1.import_file_id,
                          sr1.import_record_id,
                          'system.errors.PAX_NOT_ACTIVE' AS item_key,
                          'User Id'     AS field_name,
                          sr1.user_id   AS field_value
                     FROM stg_rec sr1,
                          application_user au
                    WHERE sr1.user_id = au.user_id
                      AND au.user_type = 'pax'
                      AND au.is_active = 0        
                      AND NOT EXISTS (SELECT pax.user_id        --03/28/2017 
                                                FROM participant pax
                                                  WHERE pax.user_id = au.user_id AND
                                                        (v_tc_accepted = 1 AND pax.status = 'inactive' AND pax.termination_date IS NULL)
                                                )
                    UNION ALL
                   SELECT sr.import_file_id,
                          sr.import_record_id,
                          'system.errors.UNKNOWN_USER_NAME' AS item_key,
                          'User Id'     AS field_name,
                          sr.user_id    AS field_value
                     FROM stg_rec sr
                    WHERE sr.user_id IS NOT NULL
                      AND NOT EXISTS (SELECT 'X'
                                        FROM application_user au
                                       WHERE au.user_id = sr.user_id
                                         AND au.user_type = 'pax')
             ) stg_err  
    );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_user_validation_err;


PROCEDURE p_rpt_not_elig_submitter_err
( p_import_file_id  IN     stage_budget_import_record.import_file_id%TYPE,
  p_promotion_id    IN     promo_audience.promotion_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   
/*
Is eligible submitter validation : Mark error for the following
                                   i.If User not in Promotion Audience

Note:
      Audience list types : i. specific participants (pax) ii. search criteria (criteria)
      For the above list types we keep/create pax audience relation in participant_audience table,
      no need for finding pax audience relation using metadata so the logic found in Java process
      isParticipantMemberOfPromotionAudience and isUserInPromotionAudiences not required to convert in Oracle    
*/

   c_process_name       CONSTANT execution_log.process_name%type  := 'P_RPT_NOT_ELIG_SUBMITTER_ERR';
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';
   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN

   v_msg := 'INSERT import_record_error';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   ( SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
            stg_err.import_file_id,
            stg_err.import_record_id,
            stg_err.item_key,
            stg_err.field_name  AS param1,
            stg_err.field_value AS param2,
            NULL                AS param3,
            NULL                AS param4,
            g_created_by        AS created_by,
            g_timestamp         AS date_created
       FROM (                     
                   SELECT sr.import_file_id,
                          sr.import_record_id,
                          'system.errors.PAX_NOT_ELIGIBLE_SUBMITTER' AS item_key,
                          'User Id'     AS field_name,
                          sr.user_id    AS field_value
                     FROM (SELECT s.import_file_id,
                                  s.import_record_id,
                                  s.user_id
                             FROM stage_budget_import_record s
                            WHERE s.import_file_id = p_import_file_id
                              AND s.import_record_id NOT IN
                                     (SELECT e.import_record_id
                                        FROM import_record_error e
                                       WHERE e.import_file_id = p_import_file_id
                                         AND e.date_created < g_timestamp
                                      )
                           ) sr
                    WHERE sr.user_id IS NOT NULL
                      AND NOT EXISTS (SELECT 'X'
                                        FROM tmp_audience_user_id pa,--04/30/2017..replaced participant_audience table
                                             promo_audience promo_a
                                       WHERE pa.user_id     = sr.user_id
                                         AND pa.audience_id = promo_a.audience_id
                                         AND promo_a.promo_audience_type = 'PRIMARY'
                                         AND promo_a.promotion_id        = p_promotion_id
                                         )
                     AND NOT EXISTS (SELECT 'X'--07/20/2017 Bug # 73517 budget file load not working for 'pax' type audiences
                                        FROM participant_audience pa,
                                             promo_audience promo_a,
                                             audience a
                                       WHERE pa.user_id     = sr.user_id
                                         AND pa.audience_id = promo_a.audience_id
                                         AND promo_a.promo_audience_type = 'PRIMARY'
                                         AND promo_a.promotion_id        = p_promotion_id
                                         AND promo_a.audience_id = a.audience_id
                                         AND a.list_type = 'pax'
                                         )
             ) stg_err  
    );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_not_elig_submitter_err;


PROCEDURE p_rpt_inactive_budget_err
( p_import_file_id   IN     stage_budget_import_record.import_file_id%TYPE,
  p_budget_master_id IN     budget_master.budget_master_id%TYPE,
  p_total_err_count  IN OUT NUMBER
) IS
   
/*
User validation : Mark error for the following
                  i.If budget found and budget is not active for file userId and budgetMasterId
*/

   c_process_name       CONSTANT execution_log.process_name%type  := 'P_RPT_INACTIVE_BUDGET_ERR';
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';
   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN

   v_msg := 'INSERT import_record_error';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   ( SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
            stg_err.import_file_id,
            stg_err.import_record_id,
            stg_err.item_key,
            stg_err.field_name  AS param1,
            NULL                AS param2,
            NULL                AS param3,
            NULL                AS param4,
            g_created_by        AS created_by,
            g_timestamp         AS date_created
       FROM (                     
              WITH stg_rec AS
                   (
                    SELECT s.*,
                           i.budget_segment_id          --11/13/2015
                      FROM stage_budget_import_record s,
                           import_file i                --11/13/2015
                     WHERE s.import_file_id = p_import_file_id
                       AND s.import_file_id = i.import_file_id    --11/13/2015
                       AND s.import_record_id NOT IN
                             (SELECT e.import_record_id
                                FROM import_record_error e
                               WHERE e.import_file_id = p_import_file_id
                                 AND e.date_created < g_timestamp
                              )
                    )
                    SELECT sr.import_file_id,
                        sr.import_record_id,
                        'system.errors.BUDGET_NOT_ACTIVE' AS item_key,
                        'Budget Id'     AS field_name                        
                    FROM stg_rec sr
                    WHERE EXISTS 
                        (SELECT 1 
                        FROM budget_master bm,
                            budget_segment bs,
                            budget b
                        WHERE bm.budget_master_id = p_budget_master_id
                            AND bm.budget_master_id = bs.budget_master_id
                            AND bs.budget_segment_id = b.budget_segment_id
                            AND b.budget_segment_id = sr.budget_segment_id --11/13/2015
                            AND b.user_id = sr.user_id
                            AND b.status = 'closed')
             ) stg_err  
    );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_inactive_budget_err;


PROCEDURE p_rpt_node_err
( p_import_file_id  IN     stage_budget_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   
/*
Node validation : Mark error for any of the following found
                  i.If node is null
                  ii.If node is not found in system
*/

   c_process_name       CONSTANT execution_log.process_name%type  := 'P_RPT_NODE_ERR';
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';
   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- file load property validation
   v_msg := 'INSERT import_record_error';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   ( SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
            stg_err.import_file_id,
            stg_err.import_record_id,
            stg_err.item_key,
            stg_err.field_name  AS param1,
            stg_err.field_value AS param2,
            NULL                AS param3,
            NULL                AS param4,
            g_created_by        AS created_by,
            g_timestamp         AS date_created
       FROM (                     
              WITH stg_rec AS
                   (SELECT s.*
                      FROM stage_budget_import_record s
                     WHERE s.import_file_id = p_import_file_id
                       AND s.import_record_id NOT IN
                             (SELECT e.import_record_id
                                FROM import_record_error e
                               WHERE e.import_file_id = p_import_file_id
                                 AND e.date_created < g_timestamp
                              )
                    )
                   SELECT sr.import_file_id,
                          sr.import_record_id,
                          'admin.fileload.errors.NODE_NOT_DEFINED' AS item_key,
                          'Node Id'     AS field_name,
                          sr.node_id     AS field_value
                     FROM stg_rec sr
                    WHERE sr.node_id IS NULL
                    UNION
                   SELECT sr.import_file_id,
                          sr.import_record_id,
                          'system.errors.UNKNOWN_NODE_ID' AS item_key,
                          'Node Id'     AS field_name,
                          sr.node_id     AS field_value
                     FROM stg_rec sr
                    WHERE sr.node_id IS NOT NULL
                      AND NOT EXISTS (SELECT 'X'
                                        FROM node n
                                       WHERE n.node_id = sr.node_id)
                                        
             ) stg_err  
    );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_node_err;


PROCEDURE p_rpt_node_no_elig_submitter
( p_import_file_id  IN     stage_budget_import_record.import_file_id%TYPE,
  p_promotion_id    IN     promo_audience.promotion_id%TYPE,
  p_total_err_count IN OUT NUMBER,
  p_audience_type   IN     promotion.primary_audience_type%TYPE        
) IS
   
/*

Check if the Promotion is claimable for the node : Mark error if the following found
                                                   i.If User not in Promotion Audience

Note:
      Audience list types : i. specific participants (pax) ii. search criteria (criteria)
      For the above list types we keep/create pax audience relation in participant_audience table,
      no need for finding pax audience relation using metadata so the logic found in Java process
      isParticipantMemberOfPromotionAudience and isUserInPromotionAudiences not required to convert in Oracle  

      -- 06/16/2016    Bug 66712 fix added parameter p_audience_type

*/

   c_process_name       CONSTANT execution_log.process_name%type  := 'P_RPT_NODE_NO_ELIG_SUBMITTER';
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';
   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
   v_tc_accepted        os_propertyset.boolean_val%TYPE;      --03/28/2017
BEGIN

-- Find status of Terms and Conditions variable --03/28/2017
   SELECT boolean_val 
      INTO v_tc_accepted 
      FROM os_propertyset 
     WHERE lower(ENTITY_NAME) = 'termsandconditions.used';


   v_msg := 'INSERT import_record_error';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   ( SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
            stg_err.import_file_id,
            stg_err.import_record_id,
            stg_err.item_key,
            stg_err.field_name  AS param1,
            stg_err.field_value AS param2,
            NULL                AS param3,
            NULL                AS param4,
            g_created_by        AS created_by,
            g_timestamp         AS date_created
       FROM (                     
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.NODE_DOES_NOT_CONTAIN_ELIGIBLE_SUBMITTER' AS item_key,
                      'Node Id'     AS field_name,
                      sr.node_id    AS field_value
                 FROM (WITH stg_rec AS
                           (
                            SELECT s.*
                              FROM stage_budget_import_record s
                             WHERE s.import_file_id = p_import_file_id
                               AND s.import_record_id NOT IN
                                     (SELECT e.import_record_id
                                        FROM import_record_error e
                                       WHERE e.import_file_id = p_import_file_id
                                         AND e.date_created < g_timestamp
                                      )
                            )
                            SELECT sr1.import_file_id,
                                   sr1.import_record_id,
                                   sr1.node_id
                              FROM stg_rec sr1
                             MINUS
                           (SELECT sr2.import_file_id,
                                   sr2.import_record_id,
                                   sr2.node_id
                              FROM stg_rec sr2,
                                   user_node un,
                                   application_user au
                             WHERE sr2.node_id  = un.node_id
                               AND un.user_id = au.user_id
                               AND un.status  = 1 
                               -- AND au.is_active = 1       --03/28/2017
                               AND ( au.is_active = 1        
                                      OR  EXISTS (SELECT pax.user_id 
                                                        FROM participant pax
                                                          WHERE pax.user_id = au.user_id AND
                                                                (v_tc_accepted = 1 AND pax.status = 'inactive' AND pax.termination_date IS NULL)
                                                        ))
                               AND EXISTS (SELECT 'X'
                                             FROM tmp_audience_user_id pa,--04/30/2017 replaced participant_audience table.
                                                  promo_audience promo_a
                                            WHERE pa.audience_id = promo_a.audience_id
                                              AND pa.user_id     = au.user_id
                                              AND promo_a.promo_audience_type = 'PRIMARY'
                                              AND promo_a.promotion_id = p_promotion_id
                                              )
                            -- 06/16/2016    Bug 66712 added union to ensure atleast one active participant in the loading node                                               
                             UNION
                            SELECT sr2.import_file_id,
                                   sr2.import_record_id,
                                   sr2.node_id
                              FROM stg_rec sr2,
                                   user_node un,
                                   application_user au
                             WHERE sr2.node_id  = un.node_id
                               AND un.user_id = au.user_id
                               AND un.status  = 1 
                               -- AND au.is_active = 1       --03/28/2017
                               AND ( au.is_active = 1        
                                      OR  EXISTS (SELECT pax.user_id 
                                                        FROM participant pax
                                                          WHERE pax.user_id = au.user_id AND
                                                                (v_tc_accepted = 1 AND pax.status = 'inactive' AND pax.termination_date IS NULL)
                                                        ))
                               AND p_audience_type = 'allactivepaxaudience'
                           )                                                   
                       ) sr
             ) stg_err  
    );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_node_no_elig_submitter;


PROCEDURE p_rpt_inactive_node_budget_err
( p_import_file_id   IN     stage_budget_import_record.import_file_id%TYPE,
  p_budget_master_id IN     budget_master.budget_master_id%TYPE,
  p_total_err_count  IN OUT NUMBER
) IS
   
/*
User validation : Mark error for the following
                  i.If budget found and budget is not active for file nodeId and budgetMasterId
*/

   c_process_name       CONSTANT execution_log.process_name%type := 'P_RPT_INACTIVE_NODE_BUDGET_ERR';
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';
   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN

   v_msg := 'INSERT import_record_error';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   ( SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
            stg_err.import_file_id,
            stg_err.import_record_id,
            stg_err.item_key,
            stg_err.field_name  AS param1,
            NULL                 AS param2,
            NULL                AS param3,
            NULL                AS param4,
            g_created_by        AS created_by,
            g_timestamp         AS date_created
       FROM (                     
              WITH stg_rec AS
                   (
                    SELECT s.*, 
                           i.budget_segment_id          --11/13/2015
                      FROM stage_budget_import_record s,
                           import_file i                --11/13/2015
                     WHERE s.import_file_id = p_import_file_id
                       AND s.import_file_id = i.import_file_id  --11/13/2015
                       AND s.import_record_id NOT IN
                             (SELECT e.import_record_id
                                FROM import_record_error e
                               WHERE e.import_file_id = p_import_file_id
                                 AND e.date_created < g_timestamp
                              )
                    )
                    SELECT sr.import_file_id,
                        sr.import_record_id,
                        'system.errors.BUDGET_NOT_ACTIVE' AS item_key,
                        'Budget Id'     AS field_name                        
                    FROM stg_rec sr
                    WHERE EXISTS 
                        (SELECT 1 
                        FROM budget_master bm,
                            budget_segment bs,
                            budget b
                        WHERE bm.budget_master_id = p_budget_master_id
                            AND bm.budget_master_id = bs.budget_master_id
                            AND bs.budget_segment_id = b.budget_segment_id
                            AND b.budget_segment_id = sr.budget_segment_id  --11/13/2015
                            AND b.node_id = sr.node_id
                            AND b.status = 'closed')
             ) stg_err  
    );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_inactive_node_budget_err;

PROCEDURE p_insert_node_or_user_id
( p_import_file_id   IN     stage_budget_import_record.import_file_id%TYPE,
  p_budget_type      IN VARCHAR2,--pax/node
  p_total_err_count  IN OUT NUMBER
) IS
   
/*
Assign user_id/node_id based on budget_type : Mark error for the following
                  i.If user not found OR if node not found.
*/

   c_process_name       CONSTANT execution_log.process_name%type := 'P_RPT_INACTIVE_NODE_BUDGET_ERR';
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';
   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN

   v_msg := 'Assign user_id/node_id';
   IF p_budget_type = 'pax' THEN
   
   FOR rec_assign IN (
                SELECT s.import_record_id, au.user_id 
                  FROM application_user au, stage_budget_import_record s 
                 WHERE UPPER(au.user_name) = UPPER(ltrim(rtrim(s.budget_owner)))
                   AND s.import_file_id =  p_import_file_id
               )
  LOOP
       
       UPDATE stage_budget_import_record
         SET user_id = rec_assign.user_id
       WHERE import_record_id = rec_assign.import_record_id;
      
  END LOOP;
  ELSE 
  FOR rec_assign IN (
                SELECT s.import_record_id, n.node_id 
                  FROM node n, stage_budget_import_record s,hierarchy h
                 WHERE s.import_file_id =  p_import_file_id
                   AND UPPER(n.name) = UPPER(ltrim(rtrim(s.budget_owner)))
                  AND n. hierarchy_id = h.hierarchy_id 
                  AND h.is_primary = 1
               )
  LOOP
       
       UPDATE stage_budget_import_record
         SET node_id = rec_assign.node_id
       WHERE import_record_id = rec_assign.import_record_id;
      
  END LOOP;
  
  END IF;
   v_rec_cnt := SQL%ROWCOUNT;  
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_insert_node_or_user_id;

PROCEDURE p_budget_verify 
  (pi_import_file_id     IN  NUMBER,   
   pi_user_id            IN  NUMBER,
   pi_promotion_id       IN  NUMBER,
   p_out_total_error_rec OUT NUMBER,
   p_out_returncode      OUT NUMBER
   )
  IS


  --Exceptions
  exit_pgm_exception        EXCEPTION;

  --Execution log variables
  C_process_name            CONSTANT execution_log.process_name%TYPE  := 'P_BUDGET_VERIFY';
  C_release_level           CONSTANT execution_log.release_level%TYPE := '1';
  C_severity_i              CONSTANT execution_log.severity%TYPE      := 'INFO';
  C_severity_e              CONSTANT execution_log.severity%TYPE      := 'ERROR';
  C_in_params               CONSTANT VARCHAR2(200) := ' Import_file_ID: '||pi_import_file_id||                                                      
                                                      ' Promotion_Id: '  ||pi_promotion_id  ||
                                                      ' User_Id: '       ||pi_user_id;
  v_log_message             execution_log.text_line%TYPE;
  
  --Procedure variables
  v_stage                   VARCHAR2(500);  
  v_import_file_id          import_file.import_file_id%TYPE;
  v_promotion_id            import_file.promotion_id%TYPE;
  v_budget_master_id        budget_master.budget_master_id%TYPE;
  v_budget_type             budget_master.budget_type%TYPE;
  v_primary_audience_type   promotion.primary_audience_type%TYPE;
  v_promotion_type          promotion.promotion_type%TYPE;
  v_total_err_count         INTEGER; -- count of errors
  v_sql_parent_promo        VARCHAR2(1000);
  v_parent_promotion_id     promotion.promotion_id%TYPE;
  
BEGIN

  --Initializing all the variables
  p_out_returncode      := 0;
  v_total_err_count     := 0;
  g_import_file_id      := pi_import_file_id;
  v_sql_parent_promo    := NULL;
  v_parent_promotion_id := NULL;
  g_created_by            := pi_user_id; --01/05/2017 bug#72881

  v_stage := 'Write start to execution_log table';
  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i, 'Procedure Started for params :'||C_in_params, NULL);

  v_stage := 'Validate Import file id';
  BEGIN
    SELECT import_file_id
      INTO v_import_file_id
      FROM import_file
     WHERE file_type = 'bud'
       AND import_file_id = pi_import_file_id;
  EXCEPTION
    WHEN OTHERS THEN
      v_log_message := 'Import file id not found :'||pi_import_file_id;
      RAISE exit_pgm_exception;
  END;

  v_stage := 'Find Budget master and primary_audience_type';
  BEGIN
    SELECT promotion_id,
           promotion_type,
           award_budget_master_id,
           primary_audience_type
      INTO v_promotion_id,
           v_promotion_type,
           v_budget_master_id,
           v_primary_audience_type
      FROM promotion
     WHERE promotion_id = pi_promotion_id;
  EXCEPTION
    WHEN OTHERS THEN
      v_log_message := 'Budget master not found for the Promotion id :'||v_promotion_id;
      RAISE exit_pgm_exception;
  END;
  
  v_stage := 'Find Budget type';
  BEGIN
    SELECT budget_type
      INTO v_budget_type
      FROM budget_master
     WHERE budget_master_id = v_budget_master_id; 
  EXCEPTION
    WHEN OTHERS THEN
      v_log_message := 'Budget type not found for the Budget maste id :'||v_budget_master_id;
      RAISE exit_pgm_exception;
  END;
  
  v_stage := 'verify promotion type';
  BEGIN
  IF v_promotion_type <> 'recognition' THEN
    INSERT INTO import_record_error
    ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
    )
    (SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
            g_import_file_id,
            NULL,
            'admin.fileload.errors.INVALID_PROPERTY' AS item_key,
            'Budget Owner'      AS param1,
            NULL                AS param2,
            NULL                AS param3,
            NULL                AS param4,
            g_created_by        AS created_by,
            g_timestamp         AS date_created
    FROM DUAL);
  END IF;
  END;

  --Budget amount validation
  --p_rpt_budget_amount_err (g_import_file_id, v_total_err_count);  -- 11/08/2017
  --p_rpt_budget_amount_err (g_import_file_id, v_budget_master_id, v_total_err_count); -- 11/08/2017 commented out 11/29/2017
  
IF v_budget_type = 'pax' THEN
    p_insert_node_or_user_id (g_import_file_id,'pax',v_total_err_count);
    p_rpt_budget_amount_err (g_import_file_id, v_budget_master_id, v_total_err_count); -- 11/29/2017
    --User validation
    p_rpt_user_validation_err (g_import_file_id, v_total_err_count);

    --check if pax is in primary audience of promo
    IF v_primary_audience_type = 'allactivepaxaudience' THEN
        NULL;
    ELSIF v_primary_audience_type = 'specifyaudience' THEN
        v_stage := 'Check if pax is in primary audience of promo: '||v_promotion_id;
        prc_refresh_t_and_c_audience (v_promotion_id,p_out_returncode);--04/30/2017
        p_rpt_not_elig_submitter_err (g_import_file_id, v_promotion_id, v_total_err_count);
    ELSE
        NULL;  --Raise error 'Pax not eligible submitter error'
    END IF;

    p_rpt_inactive_budget_err (g_import_file_id, v_budget_master_id, v_total_err_count);

ELSIF v_budget_type = 'node' THEN

p_insert_node_or_user_id (g_import_file_id,'node',v_total_err_count);
p_rpt_budget_amount_err (g_import_file_id, v_budget_master_id, v_total_err_count); -- 11/29/2017

    p_rpt_node_err (g_import_file_id, v_total_err_count);

    --check if pax is in primary audience of promo
    IF v_primary_audience_type = 'allactivepaxaudience' THEN
      --  NULL;-- 06/16/2016    Bug 66712 commented NULL(no validation) and included validation for audience type set to All active pax
      v_stage := 'Check if there is atleast one active pax in the loading nodes: '||v_promotion_id;
        p_rpt_node_no_elig_submitter (g_import_file_id, v_promotion_id, v_total_err_count,v_primary_audience_type);
    ELSIF v_primary_audience_type = 'specifyaudience' THEN
        v_stage := 'Check if pax is in primary audience of promo: '||v_promotion_id;
        prc_refresh_t_and_c_audience (v_promotion_id,p_out_returncode);--04/30/2017
        p_rpt_node_no_elig_submitter (g_import_file_id, v_promotion_id, v_total_err_count,v_primary_audience_type);
    ELSE
        NULL;  --Raise error 'Pax not eligible submitter error'
    END IF;

    p_rpt_inactive_node_budget_err(g_import_file_id, v_budget_master_id, v_total_err_count);

END IF;

  p_out_total_error_rec := f_get_error_record_count(g_import_file_id);

  IF( p_out_total_error_rec > 0) THEN
     -- import file has errors
    p_out_returncode := 1;
  END IF;

  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i, 'Procedure Completed for params :'||C_in_params, NULL);

EXCEPTION
  WHEN exit_pgm_exception THEN
    prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'Stage: '||v_stage||' Message: '||v_log_message,NULL);
    p_out_returncode := 99;
  
  WHEN OTHERS THEN
    ROLLBACK;
    --p_total_error_rec := g_import_record_count; 
    prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'File: '||pi_import_file_id||
                            ' --> '||SQLERRM,NULL);
    p_out_returncode := 99;
    
END;

PROCEDURE p_budget_load(p_import_file_id  IN NUMBER,
                                      p_user_id         IN NUMBER,                                                                           
                                      p_total_error_rec OUT NUMBER,
                                      p_out_returncode  OUT NUMBER)
IS

PRAGMA AUTONOMOUS_TRANSACTION;
  
  --Execution log variables
  c_process_name            CONSTANT execution_log.process_name%TYPE  := 'P_BUDGET_LOAD';
  c_release_level           CONSTANT execution_log.release_level%TYPE := '1';
  C_severity_i              CONSTANT execution_log.severity%TYPE      := 'INFO';
  C_severity_e              CONSTANT execution_log.severity%TYPE      := 'ERROR';
  
  --Procedure variables
  v_stage                   VARCHAR2(500);  
  --v_created_by              application_user.user_id%TYPE := 0; --01/05/2017 Bug#72881
  e_no_data                     EXCEPTION;  -- Bug # 63608
  v_source_country_media_value COUNTRY.budget_media_value%TYPE;--Bug # 63608
  v_usa_country_media_value COUNTRY.budget_media_value%TYPE;--Bug # 63608
BEGIN
  g_created_by := p_user_id;  --01/05/2017 Bug#72881
BEGIN --Bug # 63608
SELECT c.budget_media_value INTO v_usa_country_media_value FROM country c
 WHERE c.cm_asset_code = 'country_data.country.usa';
 EXCEPTION WHEN OTHERS THEN
   RAISE e_no_data;
   END;

BEGIN --Bug # 63608
SELECT c.budget_media_value INTO v_source_country_media_value FROM country c,import_file i
 WHERE i.import_file_id = p_import_file_id AND
             i.country_id = c.country_id;
 EXCEPTION WHEN OTHERS THEN
   RAISE e_no_data;
   END;
   
   IF v_usa_country_media_value IS NULL OR v_source_country_media_value IS NULL THEN
   RAISE e_no_data;
   END IF;   
   
 v_stage := 'Write start to execution_log table';
  prc_execution_log_entry(c_process_name,c_release_level,'INFO','Procedure Started.',NULL);
  
  v_stage := 'Add budget to the existing budget for p_import_file_id: '||p_import_file_id;
  FOR rec_upd IN (--Bug # 63608
                SELECT b.budget_id, s.budget_amount*v_source_country_media_value/v_usa_country_media_value AS budget_amount, b.original_value, b.current_value 
                  FROM import_file i, budget b, stage_budget_import_record s 
                 WHERE i.import_file_id = s.import_file_id
                   AND NVL(s.node_id,s.user_id) = NVL(b.node_id,b.user_id)
                   AND i.file_type = 'bud'
--                   AND i.status = 'ver'
                   AND b.budget_segment_id = i.budget_segment_id
                   AND i.import_file_id =  p_import_file_id
                   AND NOT EXISTS (SELECT import_record_id FROM import_record_error E WHERE E.import_record_id = s.import_record_id) --03/30/2016
               )
  LOOP
       
       UPDATE budget
         SET original_value = original_value + rec_upd.budget_amount,
             current_value = current_value + rec_upd.budget_amount,
             modified_by = g_created_by, --01/05/2017 Bug#72881 replaced v_created_by
             date_modified = sysdate,
             VERSION = VERSION + 1         
       WHERE budget_id = rec_upd.budget_id;
       
       INSERT INTO budget_history (BUDGET_HISTORY_ID, BUDGET_ID, ORIGINAL_VALUE_BEFORE_XACTION, CURRENT_VALUE_BEFORE_XACTION, ORIGINAL_VALUE_AFTER_XACTION, 
       CURRENT_VALUE_AFTER_XACTION,ACTION_TYPE, CREATED_BY, 
       DATE_CREATED)
       VALUES (BUDGET_HISTORY_PK_SQ.NEXTVAL, rec_upd.budget_id, rec_upd.original_value, rec_upd.current_value,  rec_upd.original_value + rec_upd.budget_amount, 
       rec_upd.current_value + rec_upd.budget_amount,'deposit', g_created_by, --01/05/2017 Bug#72881 replace v_created_by
       sysdate);
   
  END LOOP;
    
  
  v_stage := 'Create budget that does not exist in budget';
  FOR rec_add IN (--Bug # 63608
               SELECT i.budget_segment_id,s.node_id,s.user_id,s.budget_amount*v_source_country_media_value/v_usa_country_media_value AS budget_amount
                 FROM import_file i, stage_budget_import_record s 
                WHERE i.import_file_id = s.import_file_id
                  AND i.file_type = 'bud' 
--                  AND i.status = 'ver'
                  AND i.import_file_id =  p_import_file_id
                  AND NOT EXISTS (SELECT import_record_id FROM import_record_error E WHERE E.import_record_id = s.import_record_id)
                  AND NOT EXISTS (SELECT import_record_id FROM budget b WHERE b.node_id = s.node_id AND budget_segment_id = i.budget_segment_id)    
                  AND NOT EXISTS (SELECT import_record_id FROM budget b WHERE b.user_id = s.user_id AND budget_segment_id = i.budget_segment_id)                   
               )
  LOOP
       
    INSERT INTO budget (budget_id, budget_segment_id, node_id,user_id,original_value, current_value, status,action_type, created_by, date_created, VERSION)
      VALUES (BUDGET_PK_SQ.NEXTVAL, rec_add.budget_segment_id, rec_add.node_id,rec_add.user_id, rec_add.budget_amount, rec_add.budget_amount, 'active','deposit',g_created_by, --01/05/2017 Bug#72881 replaced v_created_by 
             sysdate, 0);
    
    INSERT INTO budget_history (BUDGET_HISTORY_ID, BUDGET_ID, ORIGINAL_VALUE_BEFORE_XACTION, CURRENT_VALUE_BEFORE_XACTION, ORIGINAL_VALUE_AFTER_XACTION, CURRENT_VALUE_AFTER_XACTION,ACTION_TYPE, CREATED_BY, DATE_CREATED)
       VALUES (BUDGET_HISTORY_PK_SQ.NEXTVAL, BUDGET_PK_SQ.CURRVAL, 0, 0,  rec_add.budget_amount , rec_add.budget_amount,'deposit', g_created_by,  --01/05/2017 Bug#72881 replaced v_created_by 
       sysdate);--04/21/2015 Changed initial value of CURRENT_VALUE_BEFORE_XACTION to 0
   
  END LOOP;
    
  v_stage := 'Update import file';
  UPDATE import_file
     SET status = 'imp',
         imported_by  = g_created_by, --01/05/2017 Bug#72881 replaced v_created_by 
         date_imported = sysdate, 
         VERSION = VERSION + 1
   WHERE status = 'ver'
     AND import_file_id =  p_import_file_id;
                   
  prc_execution_log_entry(c_process_name,c_release_level,'INFO','Procedure Completed Successfully.',NULL);
  COMMIT;      
   p_out_returncode := 0;
--   p_total_error_rec:=0;   --01/19/2015
   p_total_error_rec := f_get_error_record_count(p_import_file_id);    --01/19/2015
   
 EXCEPTION WHEN e_no_data THEN --Bug # 63608
  ROLLBACK;
    prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'File: '||p_import_file_id||
                            ' --> '||'Budget Media Value is not set at the country level',NULL);
    p_out_returncode := 99;    
   WHEN OTHERS THEN
    ROLLBACK;
    --p_total_error_rec := g_import_record_count; 
    prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'File: '||p_import_file_id||
                            ' --> '||SQLERRM,NULL);
    p_out_returncode := 99;
    

END;

END;
/
