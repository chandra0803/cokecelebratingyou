CREATE OR REPLACE PACKAGE pkg_nom_approver_verify_import  IS
/***********************************************************************************
   Purpose:  To verify or load stage_nom_approvers_import into original approver tables.

   Person             Date           Comments
   -----------        ----------     --------------------------------------------
  nagarajs            04/19/2016     Initial Version                                           
************************************************************************************/

  PROCEDURE p_nom_approver_verify_load(p_import_file_id  IN NUMBER,
                                       p_load_type       IN VARCHAR2,
                                       p_user_id         IN NUMBER,
                                       p_promotion_id    IN NUMBER,
                                       p_total_error_rec OUT NUMBER,
                                       p_out_returncode  OUT NUMBER) ;
                                       
  FUNCTION f_is_date(date_value IN VARCHAR2) 
  RETURN NUMBER;
  
  FUNCTION f_is_integer(int_value IN VARCHAR2) 
  RETURN NUMBER;
END pkg_nom_approver_verify_import;
/
CREATE OR REPLACE PACKAGE BODY pkg_nom_approver_verify_import
IS
/***************************************************************************************************
    Purpose:  To verify or load stage_nom_approvers_import into original approver tables.

   Person             Date           Comments
   -----------        ----------     --------------------------------------------
  nagarajs            04/19/2016     Initial Version   
  nagarajs            02/02/2017    Bug 70570 - Custom Approver: Default approver receives the
                                    nomination for approval if custom approver characteristic type is of Text  
***************************************************************************************************/

-- global package constants
c_cms_locale               CONSTANT vw_cms_code_value.locale%TYPE := 'en_US';
c_cms_status               CONSTANT vw_cms_code_value.cms_status%TYPE := 'true';

c_approval_award           CONSTANT stage_nom_approvers_import.approval_type%TYPE := 'award';
c_approval_behavior        CONSTANT stage_nom_approvers_import.approval_type%TYPE := 'behavior';
c_approval_characteristic  CONSTANT stage_nom_approvers_import.approval_type%TYPE := 'characteristic';
c_approval_specific_approv CONSTANT stage_nom_approvers_import.approval_type%TYPE := 'specific_approv';


-- global package variables
g_created_by               import_record_error.created_by%TYPE := 0;
g_timestamp                import_record_error.date_created%TYPE := SYSDATE;

 v_returncode              NUMBER:= 0;      

---------------------
---------------------
-- private functions
---------------------
-- get the count of records with errors
FUNCTION f_get_error_record_count
( p_import_file_id  IN  NUMBER
) RETURN INTEGER
IS
   c_process_name       CONSTANT execution_log.process_name%type := 'F_GET_ERROR_RECORD_COUNT';
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_error_record_count INTEGER;

   CURSOR cur_import_error_rec_count
   ( cv_import_file_id  import_record_error.import_file_id%TYPE
   ) IS
   SELECT COUNT(DISTINCT ire.import_record_id) AS error_cnt
     FROM import_record_error ire
    WHERE ire.import_file_id = cv_import_file_id
      ;

BEGIN
   -- get count of any existing errors
   v_msg := 'OPEN cur_import_error_rec_count';
   OPEN cur_import_error_rec_count(p_import_file_id);
   v_msg := 'FETCH cur_import_error_rec_count';
   FETCH cur_import_error_rec_count INTO v_error_record_count;
   CLOSE cur_import_error_rec_count;

   RETURN v_error_record_count;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END f_get_error_record_count;


FUNCTION f_is_date(date_value IN VARCHAR2) 
  RETURN NUMBER IS
  v_return DATE;
BEGIN

  SELECT TO_DATE(date_value,'MM/DD/YYYY')
    INTO v_return
    FROM dual;

  RETURN 1;

EXCEPTION
  WHEN OTHERS THEN
    RETURN 0;
END;

FUNCTION f_is_integer(int_value IN VARCHAR2) 
  RETURN NUMBER IS
  v_return NUMBER;
BEGIN

  SELECT TO_NUMBER(int_value)
    INTO v_return
    FROM dual;

  RETURN 1;

EXCEPTION
  WHEN OTHERS THEN
    RETURN 0;
END;
-- Reports validation errors
PROCEDURE p_rpt_validation_err
( p_import_file_id  IN      stage_pax_import_record.import_file_id%TYPE,
  p_promotion_id    IN      NUMBER,
  p_total_err_count IN OUT  NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := 'P_RPT_VALIDATION_ERR';
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
   


BEGIN
   -- validate miscellaneous requirements
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
   (  SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
             e.import_file_id,
             e.import_record_id,
             e.item_key,
             e.field_name AS param1,
             e.field_value1 AS param2,
             e.field_value2 AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT sr.*,
                         c.characteristic_data_type,
                         c.min_value char_min_value  ,
                         c.max_value char_max_value,
                         c.max_size,
                         c.date_start,
                         c.date_end,
                         c.pl_name,
                         pml.award_amount_type_fixed,
                         pml.level_index,
                         award_amount_fixed,
                         NVL(award_amount_min,clp.low_award) award_amount_min,
                         NVL(award_amount_max,clp.high_award) award_amount_max,
                         f_is_date(sr.min_value) date_min_value,
                         f_is_date(sr.max_value) date_max_value,
                         f_is_integer(sr.min_value) int_min_value,
                         f_is_integer(sr.max_value) int_max_value,
                         DECODE(c.award_type, 'fixed', 1, 'range',0) calc_award_fixed
                    FROM (SELECT s.*,
                                 cpe.cms_name approval_type_cms_name,
                                 cpe.cms_code approval_type_cms_code,
                                 cpe_nb.cms_name min_value_cms_name,
                                 cpe_nb.cms_code min_value_cms_code
                            FROM stage_nom_approvers_import s,
                                 (SELECT ccv.cms_name,
                                         cms_code
                                    FROM vw_cms_code_value ccv
                                   WHERE ccv.asset_code = 'picklist.customapprover.items'
                                     AND ccv.locale     = c_cms_locale
                                     AND ccv.cms_status = c_cms_status
                                  ) cpe,
                                  (SELECT *
                                     FROM vw_cms_code_value ccv
                                    WHERE ccv.asset_code = 'picklist.promo.nomination.behavior.items'
                                      AND ccv.locale     = c_cms_locale
                                      AND ccv.cms_status = c_cms_status
                                  ) cpe_nb
                            WHERE s.import_file_id = p_import_file_id
                              AND LOWER(s.approval_type) = LOWER(cpe.cms_name (+))
                              AND LOWER(s.min_value) = LOWER(cpe_nb.cms_name (+))
                         ) sr,
                         approver_option ao,
                         characteristic c,
                         promo_nomination pn,
                         promo_nomination_level pml,
                         calculator c,
                         calculator_payout clp
                   WHERE ao.promotion_id (+) = p_promotion_id   
                     AND sr.approval_round = ao.approval_round (+)
                     AND sr.approval_type_cms_code = ao.approver_type (+) 
                     AND ao.characteristic_id = c.characteristic_id (+)
                     AND ao.promotion_id = pml.promotion_id (+)
                     AND ao.promotion_id = pn.promotion_id 
                     AND ((ao.approval_round =  pml.level_index AND pn.payout_level_type = 'eachLevel') OR pn.payout_level_type = 'finalLevel')
                     AND pml.calculator_id = c.calculator_id (+)
                     AND c.calculator_id = clp.calculator_id (+)
               )
               --Wrong Approval type in file
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.INVALID_APPROVAL_TYPE' AS item_key,
                      'Approval Type' AS field_name,
                      sr.approval_type AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr
                WHERE approval_type_cms_name IS NULL
                UNION ALL 
                --Approval type not eligible/more than approval type for the approval round 
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.UNELIGIBLE_APPROVAL_TYPE' AS item_key,
                      'Approval Type' AS field_name,
                      sr.approval_type AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr,
                      approver_option ao
                WHERE ao.promotion_id = p_promotion_id
                  AND ao.approval_round = sr.approval_round   
                  AND ao.approver_type <> approval_type_cms_code 
                UNION ALL 
                --Given Behavior value not exist for the promotion
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.UNELIGIBLE_BEHAVIOR' AS item_key,
                      'Min Value Behavior' AS field_name,
                      sr.min_value AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr   
                WHERE LOWER(sr.approval_type) = c_approval_behavior
                  AND min_value IS NOT NULL
                  AND NOT EXISTS ( SELECT 1
                                     FROM promo_behavior pb
                                    WHERE pb.promotion_id = p_promotion_id   
                                      AND pb.behavior_type = sr.min_value_cms_code
                                 )
                UNION ALL 
                --Behavior aprpoval type shouldn't have Max value
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.BEHAVIOR_MAXVALUE' AS item_key,
                      'Max Value Behavior' AS field_name,
                      sr.max_value AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr
                WHERE LOWER(sr.approval_type) = c_approval_behavior
                  AND sr.max_value IS NOT NULL
                UNION ALL
                --All Behaviors tied to promotion not exist in file
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.MISSING_SOME_BEHAVIOR' AS item_key,
                      'Min Value Behavior' AS field_name,
                      sr.min_value AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr 
               WHERE LOWER(approval_type) = c_approval_behavior
                 AND 1 <= ( SELECT COUNT(1)
                               FROM promo_behavior pb
                              WHERE promotion_id = p_promotion_id
                                AND pb.behavior_type NOT IN (SELECT min_value_cms_code
                                                               FROM stg_rec 
                                                              WHERE LOWER(approval_type) = c_approval_behavior))
                UNION ALL 
                 --Fixed amount not matched
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.FIXED_AMOUNT_MIN_INVALID' AS item_key,
                      'Min Value Award Fixed' AS field_name,
                      sr.min_value AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr
                WHERE LOWER(sr.approval_type) = c_approval_award
                  AND (sr.award_amount_type_fixed = 1 OR sr.calc_award_fixed = 1)
                  AND sr.award_amount_fixed <> NVL(sr.min_value,'0') 
                  AND int_min_value = 1
                UNION ALL 
                --award amount other than integer
                SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.AMOUNT_INVALID_DATA' AS item_key,
                      'Min Value Award Fixed' AS field_name,
                      sr.min_value AS field_value1,
                      sr.max_value field_value2
                 FROM stg_rec sr
                WHERE LOWER(sr.approval_type) = c_approval_award
                  AND (int_min_value = 0 OR int_max_value = 0)
                UNION ALL 
                 --Fixed amount shouldn't have Max value
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.FIXED_AMOUNT_MAX_INVALID' AS item_key,
                      'Max Value Award Fixed' AS field_name,
                      sr.max_value AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr
                WHERE LOWER(sr.approval_type) = c_approval_award 
                  AND (sr.award_amount_type_fixed = 1 OR sr.calc_award_fixed = 1)
                  AND sr.max_value IS NOT NULL
                  UNION ALL 
                  --Range amount should have Max value
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.RANGE_AMOUNT_MAX_NULL' AS item_key,
                      'Max Value Award Range' AS field_name,
                      sr.max_value AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr
                WHERE LOWER(sr.approval_type) = c_approval_award 
                  AND (sr.award_amount_type_fixed = 0 OR sr.calc_award_fixed = 0)
                  AND sr.max_value IS NULL
                UNION ALL
                --shouldn't have  GAP/OVERLAP/MISSING/EXCEED in Range award amount
                SELECT sr.import_file_id,
                       sr.import_record_id,
                       'nomination.approver.fileload.RANGE_AMOUNT_GAP_OVERLAP_EXCEED' AS item_key,
                       'Award Range' AS field_name,
                       sr.min_value AS field_value1,
                       sr.max_value field_value2
                  FROM ( SELECT approval_round, 
                                COUNT(DISTINCT grp_appr_round) as gap_appr_round, 
                                MIN(TO_NUMBER(min_value)) as  min_value, 
                                MAX(TO_NUMBER(max_value)) as max_value  
                           FROM (SELECT approval_round,  
                                        min_value, 
                                        max_value, 
                                        SUM(srt_appr_round) 
                                        OVER(PARTITION  BY approval_round ORDER BY min_value, max_value) grp_appr_round
                                   From (SELECT sr.*,  
                                                CASE WHEN min_value - 1 <= MAX(max_value)  
                                                    OVER(PARTITION BY approval_round ORDER BY min_value, max_value ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING) then  
                                                    0  
                                                    ELSE  
                                                    1  
                                                END srt_appr_round  
                                           FROM stg_rec sr
                                          WHERE LOWER(approval_type) = c_approval_award
                                            AND award_amount_type_fixed = 0
                                            AND int_min_value = 1
                                            AND int_max_value = 1
                                         )
                                 )  
                           GROUP BY approval_round
                        ) stg,
                        stg_rec sr
                  WHERE sr.approval_round = stg.approval_round
                    AND LOWER(sr.approval_type) = c_approval_award
                    AND award_amount_type_fixed = 0
                    AND (gap_appr_round > 1  
                          OR EXISTS (SELECT 1  
                                       FROM stg_rec s  
                                      WHERE LOWER(approval_type) = c_approval_award
                                        AND award_amount_type_fixed = 0
                                        AND (s.award_amount_min < stg.min_value  
                                         OR s.award_amount_max > stg.max_value
                                         OR s.award_amount_min > stg.min_value)
                                     ) 
                         )
                UNION ALL 
                --Date/Decimal/Integer Characteristic should have Max value
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.CHARACTERISTIC_MISSING_MAXVAL' AS item_key,
                      'Characteristic' AS field_name,
                      'Max value' AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr
                WHERE sr.characteristic_data_type IN ('date','decimal','int')
                  AND sr.max_value IS NULL
                UNION ALL 
                --Characteristic other than Date/Decimal/Integer shouldn't have Max value
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.CHARACTERISTIC_EXIST_MAXVAL' AS item_key,
                      'Characteristic' AS field_name,
                      sr.max_value AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr
                WHERE sr.characteristic_data_type NOT IN ('date','decimal','int')
                  AND sr.max_value IS NOT NULL  
                UNION ALL
                --shouldn't have GAP/OVERLAP/MISSING/EXCEED value in Characteristic Range
                SELECT sr.import_file_id,
                       sr.import_record_id,
                       'nomination.approver.fileload.CHARACTERISTIC_GAP_OVERLAP_EXCEED' AS item_key,
                       'Characteristic Range' AS field_name,
                       sr.min_value AS field_value1,
                       sr.max_value field_value2
                  FROM ( SELECT approval_round, 
                                COUNT(DISTINCT grp_appr_round) as gap_appr_round, 
                                MIN(TO_NUMBER(min_value)) as  min_value, 
                                MAX(TO_NUMBER(max_value)) as max_value  
                           FROM (SELECT approval_round,  
                                        min_value, 
                                        max_value, 
                                        SUM(srt_appr_round) 
                                        OVER(PARTITION  BY approval_round ORDER BY min_value, max_value) grp_appr_round
                                   From (SELECT sr.*,  
                                                CASE WHEN min_value - 1 <= MAX(max_value)  
                                                    OVER(PARTITION BY approval_round ORDER BY min_value, max_value ROWS BETWEEN UNBOUNDED PRECEDING AND 1 PRECEDING) then  
                                                    0  
                                                    ELSE  
                                                    1  
                                                END srt_appr_round  
                                           FROM stg_rec sr
                                          WHERE characteristic_data_type IN ('decimal','int')
                                            AND int_min_value = 1
                                            AND int_max_value = 1
                                         )
                                 )  
                           GROUP BY approval_round
                        ) stg,
                        stg_rec sr
                  WHERE sr.approval_round = stg.approval_round
                    AND sr.characteristic_data_type IN ('decimal','int')
                    AND (gap_appr_round > 1  
                          OR EXISTS (SELECT 1  
                                       FROM stg_rec s  
                                      WHERE s.char_min_value < stg.min_value  
                                         OR s.char_max_value > stg.max_value
                                         OR s.char_min_value > stg.min_value
                                     ) 
                         )
                UNION ALL
                --Given Characteristic Date value not in the Range
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.CHARACTERISTIC_DATE_NOTIN_RANGE' AS item_key,
                      'Characteristic' AS field_name,
                      sr.min_value AS field_value1,
                      sr.max_value AS field_value2
                 FROM stg_rec sr
                WHERE sr.characteristic_data_type = 'date'
                  AND date_min_value = 1
                  AND date_max_value = 1
                  AND (TO_DATE(sr.min_value,'MM/DD/YYYY') NOT BETWEEN sr.date_start AND sr.date_end
                   OR  TO_DATE(sr.max_value,'MM/DD/YYYY') NOT BETWEEN sr.date_start AND sr.date_end)
                UNION ALL
                --Given Characteristic value for date data type is string/number
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.CHARACTERISTIC_INVALID_DATE' AS item_key,
                      'Characteristic' AS field_name,
                      sr.min_value AS field_value1,
                      sr.max_value field_value2
                 FROM stg_rec sr
                WHERE sr.characteristic_data_type = 'date'
                  AND (date_min_value = 0
                   OR  date_max_value = 0)
                UNION ALL
                 --Given Characteristic value for int/decimal data type is string/date
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.CHARACTERISTIC_INVALID_INTEGER' AS item_key,
                      'Characteristic' AS field_name,
                      sr.min_value AS field_value1,
                      sr.max_value field_value2
                 FROM stg_rec sr
                WHERE sr.characteristic_data_type IN ('decimal','int')
                  AND (int_min_value = 0
                   OR  int_max_value = 0)
                UNION ALL
                --Given Characteristic value not present in the picklist.xxxx.xxxx
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.CHARACTERISTIC_INVALID_DATA' AS item_key,
                      'Characteristic' AS field_name,
                      sr.min_value AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr
                WHERE sr.characteristic_data_type = 'single_select'
                  AND sr.pl_name IS NOT NULL 
                  AND sr.max_value IS NULL 
                  AND NOT EXISTS (SELECT 1
                                    FROM vw_cms_code_value vw 
                                   WHERE vw.asset_code = sr.pl_name 
                                     AND locale = c_cms_locale
                                     AND cms_status = c_cms_status
                                     AND LOWER(vw.cms_name) = LOWER(sr.min_value)
                                  )
                UNION ALL
                --Given Characteristic value not present in the picklist.xxxx.xxxx
               SELECT DISTINCT sr.import_file_id,
                         sr.import_record_id,
                         'nomination.approver.fileload.CHARACTERISTIC_INVALID_DATA' AS item_key,
                         'Characteristic' AS field_name,
                         sr.min_value AS field_value1,
                         NULL field_value2
                    FROM (SELECT s.* , 
                                 TRIM(REGEXP_SUBSTR(min_value, '[^,]+', 1, LEVEL)) min_value_str
                            FROM ( SELECT *          
                                     FROM stg_rec 
                                    WHERE characteristic_data_type = 'multi_select'
                                      AND pl_name IS NOT NULL 
                                      AND max_value IS NULL
                                      ) s
                                  CONNECT BY LEVEL <= LENGTH(min_value) - LENGTH(REPLACE(min_value, ',')) + 1
                         ) sr
                   WHERE NOT EXISTS ( SELECT 1
                                        FROM vw_cms_code_value vw 
                                       WHERE vw.asset_code = sr.pl_name 
                                         AND locale = c_cms_locale
                                         AND cms_status = c_cms_status
                                         AND LOWER(vw.cms_name) = LOWER(sr.min_value_str)
                                      )
                UNION ALL
                --Given Characteristic value not a boolean value
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.CHARACTERISTIC_INVALID_DATA' AS item_key,
                      'Characteristic' AS field_name,
                      sr.min_value AS field_value1,
                      NULL field_value2
                 FROM stg_rec sr
                WHERE sr.characteristic_data_type = 'boolean'
                  AND LOWER(sr.min_value) NOT IN ('true','false')
                  AND sr.max_value IS NULL 
               UNION ALL
                --Given Characteristic value exceed the maximum text size
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'nomination.approver.fileload.CHARACTERISTIC_SIZE_EXCEED' AS item_key,
                      'Characteristic' AS field_name,
                      sr.min_value AS field_value1,
                      TO_CHAR(max_size) field_value2
                 FROM stg_rec sr
                WHERE sr.characteristic_data_type = 'txt'
                  AND LENGTH(sr.max_value) > max_size 
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_validation_err;

-- imports approver user from the stage table
PROCEDURE p_imp_approver
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_promotion_id    IN      NUMBER,
  p_out_return_code OUT     NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := 'P_IMP_APPROVER';
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;

BEGIN

   v_msg := 'Delete approver';
   DELETE FROM approver
    WHERE approver_criteria_id IN (SELECT approver_criteria_id
                                     FROM approver_option ao,
                                          approver_criteria ac
                                    WHERE ao.promotion_id = p_promotion_id
                                      AND ao.approver_type  <> c_approval_specific_approv
                                      AND ao.approver_option_id = ac.approver_option_id);
     
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);
   
   v_msg := 'Delete approver_criteria';
   DELETE FROM approver_criteria
    WHERE approver_option_id IN (SELECT approver_option_id
                                   FROM approver_option
                                  WHERE promotion_id = p_promotion_id
                                    AND approver_type  <> c_approval_specific_approv);
                                  
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);
   
   -- import staged records into approver_criteria
   v_msg := 'Insert approver_criteria';
   INSERT INTO approver_criteria
            ( approver_criteria_id,
              approver_option_id,
              approver_value,
              min_val,
              max_val,
              created_by,
              date_created,
              version
            )
  WITH stg_rec AS
           (  -- get stage records
              SELECT sr.*,
                     c.characteristic_data_type,
                     c.min_value char_min_value  ,
                     c.max_value char_max_value,
                     c.date_start,
                     c.date_end,
                     c.pl_name,
                     pml.award_amount_type_fixed,
                     pml.level_index,
                     award_amount_fixed,
                     award_amount_min,
                     award_amount_max,
                     f_is_date(sr.min_value) date_min_value,
                     f_is_date(sr.max_value) date_max_value,
                     f_is_integer(sr.min_value) int_min_value,
                     f_is_integer(sr.max_value) int_max_value
                FROM (SELECT s.*,
                             cpe.cms_name approval_type_cms_name,
                             cpe.cms_code approval_type_cms_code,
                             cpe_nb.cms_name min_value_cms_name,
                             cpe_nb.cms_code min_value_cms_code
                        FROM stage_nom_approvers_import s,
                             (SELECT ccv.cms_name,
                                     cms_code
                                FROM vw_cms_code_value ccv
                               WHERE ccv.asset_code = 'picklist.customapprover.items'
                                 AND ccv.locale     = c_cms_locale
                                 AND ccv.cms_status = c_cms_status
                              ) cpe,
                              (SELECT *
                                 FROM vw_cms_code_value ccv
                                WHERE ccv.asset_code = 'picklist.promo.nomination.behavior.items'
                                  AND ccv.locale     = c_cms_locale
                                  AND ccv.cms_status = c_cms_status
                              ) cpe_nb
                        WHERE s.import_file_id = p_import_file_id
                          AND LOWER(s.approval_type) = LOWER(cpe.cms_name (+))
                          AND LOWER(s.min_value) = LOWER(cpe_nb.cms_name (+))
                     ) sr,
                     approver_option ao,
                     characteristic c,
                     promo_nomination_level pml
               WHERE ao.promotion_id (+) = p_promotion_id   
                 AND sr.approval_round = ao.approval_round (+)
                 AND sr.approval_type_cms_code = ao.approver_type (+) 
                 AND ao.characteristic_id = c.characteristic_id (+)
                 AND ao.promotion_id = pml.promotion_id (+)
                 AND ao.approval_round =  pml.level_index (+)
           )
       SELECT approver_criteria_pk_sq.NEXTVAL,
              approver_option_id,
              approver_value,
              min_value,
              max_value,
              g_created_by,
              g_timestamp,
              0
         FROM approver_option ao,
              ( SELECT DISTINCT approval_type_cms_code AS approval_type,
                      approval_round,
                       CASE WHEN approval_type_cms_code = c_approval_characteristic
                            THEN CASE WHEN max_value IS NULL AND characteristic_data_type IN ('multi_select', 'single_select') THEN NVL(cm_ml.multi_cms_code,cm_si.single_cms_code)  -- 02/02/2017
                                      WHEN characteristic_data_type IN ('txt','boolean') THEN min_value                                                                             -- 02/02/2017
                                      ELSE min_value||'-'||max_value
                                  END
                            WHEN approval_type_cms_code = c_approval_behavior
                            THEN  min_value_cms_code
                       END approver_value,
                       DECODE(approval_type_cms_code,c_approval_award,min_value, NULL) min_value,
                       DECODE(approval_type_cms_code,c_approval_award,max_value, NULL) max_value
                 FROM stg_rec sr,
                      ( SELECT import_record_id, listagg(ccv.cms_code,',') within group(order by cms_code) multi_cms_code
                          FROM ( -- parse pick list items from multi-select characteristic
                                 SELECT p.column_value AS picklist_item,
                                        ms.*
                                   FROM ( -- get multi-select characteristics
                                          SELECT srp.*,
                                                 ',' AS delimiter,
                                                 ',' || srp.min_value || ',' AS picklist,
                                                 LENGTH(srp.min_value) - LENGTH(REPLACE(srp.min_value,',',NULL)) +1 AS field_cnt
                                            FROM stg_rec srp
                                           WHERE srp.characteristic_data_type = 'multi_select'
                                             AND max_value IS NULL
                                        ) ms,
                                        -- parse characteristic into individual pick list codes
                                        -- pivoting pick list codes into separate records
                                        TABLE( CAST( MULTISET(
                                           SELECT SUBSTR(ms.picklist,
                                                   INSTR(ms.picklist, ms.delimiter, 1, LEVEL)+1, 
                                                   INSTR(ms.picklist, ms.delimiter, 1, LEVEL+1) - INSTR(ms.picklist, ms.delimiter, 1, LEVEL)-1 
                                                  )
                                             FROM dual
                                          CONNECT BY LEVEL <= ms.field_cnt
                                        ) AS sys.odcivarchar2list ) ) p
                               ) pl,
                               vw_cms_code_value ccv
                            -- outer join characteristic fields to CMS table
                         WHERE ccv.asset_code = pl.pl_name
                           AND ccv.locale     = c_cms_locale
                           AND ccv.cms_status = c_cms_status
                           AND LOWER(ccv.cms_name) = LOWER(trim(pl.picklist_item))
                        GROUP BY import_record_id    
                      ) cm_ml,
                      (SELECT import_record_id, vw.cms_code single_cms_code
                         FROM stg_rec srp,
                              vw_cms_code_value vw 
                        WHERE vw.asset_code = srp.pl_name 
                          AND srp.characteristic_data_type = 'single_select'
                          AND locale = c_cms_locale
                          AND cms_status = c_cms_status
                          AND LOWER(vw.cms_name) = LOWER(srp.min_value)
                      ) cm_si
                WHERE sr.import_record_id = cm_ml.import_record_id (+)
                  AND sr.import_record_id = cm_si.import_record_id (+)
               ) stg
         WHERE ao.promotion_id = p_promotion_id
           AND ao.approver_type = stg.approval_type
           AND ao.approval_round = stg.approval_round ;

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);
   
   -- import staged records into approver
   v_msg := 'Insert approver for award';
   INSERT INTO approver
            ( approver_criteria_id,
              user_id,
              created_by,
              date_created,
              version
            )
    WITH stg_rec AS
           (  -- get stage records
              SELECT sr.*,
                     c.characteristic_data_type,
                     c.min_value char_min_value  ,
                     c.max_value char_max_value,
                     c.date_start,
                     c.date_end,
                     c.pl_name,
                     pml.award_amount_type_fixed,
                     pml.level_index,
                     award_amount_fixed,
                     award_amount_min,
                     award_amount_max,
                     f_is_date(sr.min_value) date_min_value,
                     f_is_date(sr.max_value) date_max_value,
                     f_is_integer(sr.min_value) int_min_value,
                     f_is_integer(sr.max_value) int_max_value
                FROM (SELECT s.*,
                             cpe.cms_name approval_type_cms_name,
                             cpe.cms_code approval_type_cms_code,
                             cpe_nb.cms_name min_value_cms_name,
                             cpe_nb.cms_code min_value_cms_code
                        FROM stage_nom_approvers_import s,
                             (SELECT ccv.cms_name,
                                     cms_code
                                FROM vw_cms_code_value ccv
                               WHERE ccv.asset_code = 'picklist.customapprover.items'
                                 AND ccv.locale     = c_cms_locale
                                 AND ccv.cms_status = c_cms_status
                              ) cpe,
                              (SELECT *
                                 FROM vw_cms_code_value ccv
                                WHERE ccv.asset_code = 'picklist.promo.nomination.behavior.items'
                                  AND ccv.locale     = c_cms_locale
                                  AND ccv.cms_status = c_cms_status
                              ) cpe_nb
                        WHERE s.import_file_id = p_import_file_id
                          AND LOWER(s.approval_type) = LOWER(cpe.cms_name (+))
                          AND LOWER(s.min_value) = LOWER(cpe_nb.cms_name (+))
                     ) sr,
                     approver_option ao,
                     characteristic c,
                     promo_nomination_level pml
               WHERE ao.promotion_id (+) = p_promotion_id   
                 AND sr.approval_round = ao.approval_round (+)
                 AND sr.approval_type_cms_code = ao.approver_type (+) 
                 AND ao.characteristic_id = c.characteristic_id (+)
                 AND ao.promotion_id = pml.promotion_id (+)
                 AND ao.approval_round =  pml.level_index (+)
           )
   SELECT ac.approver_criteria_id,
          app.user_id,
          g_created_by,
          g_timestamp,
          0
     FROM (SELECT approver_option_id,
                  approver_value,
                  min_value,
                  max_value,
                  user_id
             FROM approver_option ao,
                  ( SELECT approval_type_cms_code AS approval_type,
                          approval_round,
                           CASE WHEN approval_type_cms_code = c_approval_characteristic
                                THEN CASE WHEN max_value IS NULL AND characteristic_data_type IN ('multi_select', 'single_select') THEN NVL(cm_ml.multi_cms_code,cm_si.single_cms_code)  -- 02/02/2017
                                          WHEN  characteristic_data_type IN ('txt','boolean') THEN min_value                                                                             -- 02/02/2017
                                      ELSE min_value||'-'||max_value
                                      END
                                WHEN approval_type_cms_code = c_approval_behavior
                                THEN  min_value_cms_code
                           END approver_value,
                           DECODE(approval_type_cms_code,c_approval_award,min_value, NULL) min_value,
                           DECODE(approval_type_cms_code,c_approval_award,max_value, NULL) max_value,
                           user_id
                     FROM stg_rec sr,
                          ( SELECT import_record_id, listagg(ccv.cms_code,',') within group(order by cms_code) multi_cms_code
                              FROM ( -- parse pick list items from multi-select characteristic
                                     SELECT p.column_value AS picklist_item,
                                            ms.*
                                       FROM ( -- get multi-select characteristics
                                              SELECT srp.*,
                                                     ',' AS delimiter,
                                                     ',' || srp.min_value || ',' AS picklist,
                                                     LENGTH(srp.min_value) - LENGTH(REPLACE(srp.min_value,',',NULL)) +1 AS field_cnt
                                                FROM stg_rec srp
                                               WHERE srp.characteristic_data_type = 'multi_select'
                                                 AND max_value IS NULL
                                            ) ms,
                                            -- parse characteristic into individual pick list codes
                                            -- pivoting pick list codes into separate records
                                            TABLE( CAST( MULTISET(
                                               SELECT SUBSTR(ms.picklist,
                                                       INSTR(ms.picklist, ms.delimiter, 1, LEVEL)+1, 
                                                       INSTR(ms.picklist, ms.delimiter, 1, LEVEL+1) - INSTR(ms.picklist, ms.delimiter, 1, LEVEL)-1 
                                                      )
                                                 FROM dual
                                              CONNECT BY LEVEL <= ms.field_cnt
                                            ) AS sys.odcivarchar2list ) ) p
                                   ) pl,
                                   vw_cms_code_value ccv
                                -- outer join characteristic fields to CMS table
                             WHERE ccv.asset_code = pl.pl_name
                               AND ccv.locale     = c_cms_locale
                               AND ccv.cms_status = c_cms_status
                               AND LOWER(ccv.cms_name) = LOWER(trim(pl.picklist_item))
                            GROUP BY import_record_id    
                          ) cm_ml,
                          (SELECT import_record_id, vw.cms_code single_cms_code
                             FROM stg_rec srp,
                                  vw_cms_code_value vw 
                            WHERE vw.asset_code = srp.pl_name 
                              AND srp.characteristic_data_type = 'single_select'
                              AND locale = c_cms_locale
                              AND cms_status = c_cms_status
                              AND LOWER(vw.cms_name) = LOWER(srp.min_value)
                          ) cm_si
                    WHERE sr.import_record_id = cm_ml.import_record_id (+)
                      AND sr.import_record_id = cm_si.import_record_id (+)
                    ) stg
              WHERE ao.promotion_id = p_promotion_id
                   AND ao.approver_type = stg.approval_type
                   AND ao.approval_round = stg.approval_round    
           ) app,
           approver_criteria ac
     WHERE app.approver_option_id = ac.approver_option_id
       AND NVL(app.approver_value,DECODE(TO_CHAR(app.max_value), NULL,TO_CHAR(app.min_value),TO_CHAR(app.max_value)||'-'||TO_CHAR(app.max_value))) = 
       NVL(ac.approver_value, DECODE(TO_CHAR(max_val), NULL,TO_CHAR(min_val),TO_CHAR(max_val)||'-'||TO_CHAR(max_val)));  
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      p_out_return_code := 99;
      RAISE;
END p_imp_approver;

-- public procecures
PROCEDURE p_nom_approver_verify_load ( p_import_file_id  IN NUMBER,
                                       p_load_type       IN VARCHAR2,
                                       p_user_id         IN NUMBER,
                                       p_promotion_id    IN NUMBER,
                                       p_total_error_rec OUT NUMBER,
                                       p_out_returncode  OUT NUMBER) IS
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name           CONSTANT execution_log.process_name%type :='P_NOM_APPROVER_VERIFY_LOAD';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   
   e_pgm_exit               EXCEPTION;

   v_msg                    execution_log.text_line%TYPE;
   v_total_err_count        INTEGER; -- count of errors
   v_import_record_count    import_file.import_record_count%TYPE;
   v_out_returncode         NUMBER :=0; 
   
   v_promo_approval_round   NUMBER(1);
   v_stg_approval_round     NUMBER(1);
   v_specific_appr_cnt      NUMBER(1);

   CURSOR cur_import_file
   ( cv_import_file_id  import_record_error.import_file_id%TYPE
   ) IS
   SELECT f.import_record_count
     FROM import_file f
    WHERE f.import_file_id = cv_import_file_id
      ;

BEGIN
   v_msg := 'Start'
      || ': p_import_file_id >' || p_import_file_id
      || '<, p_load_type >'     || p_load_type
      || '<, p_promotion_id >'  || p_promotion_id
      || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   -- initialize variables
   g_created_by := p_user_id;
   g_timestamp := SYSDATE;
   p_out_returncode := 0;
   v_total_err_count := 0;
   
   v_msg := 'OPEN cur_import_file';
   OPEN cur_import_file(p_import_file_id);
   v_msg := 'FETCH cur_import_file';
   FETCH cur_import_file INTO v_import_record_count;
   CLOSE cur_import_file;
   
   SELECT approval_node_levels
     INTO v_promo_approval_round
     FROM promotion p
    WHERE p.promotion_id = p_promotion_id;
    
   SELECT COUNT(1)
     INTO v_specific_appr_cnt
     FROM approver_option 
    WHERE promotion_id = p_promotion_id
      AND approver_type =  'specific_approv';
   
   SELECT COUNT(DISTINCT approval_round)
     INTO v_stg_approval_round
     FROM stage_nom_approvers_import s
    WHERE s.import_file_id = p_import_file_id;

   IF (v_specific_appr_cnt > 0 AND (v_promo_approval_round - v_specific_appr_cnt) <> v_stg_approval_round ) OR
      (v_specific_appr_cnt = 0 AND v_promo_approval_round <> v_stg_approval_round) THEN
      
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
     SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
           sr.import_file_id,
           sr.import_record_id,
           'nomination.approver.fileload.PARTIAL_FILE_LOAD' item_key,
           v_stg_approval_round AS param1,
           (v_promo_approval_round - v_specific_appr_cnt) AS param2,
           NULL AS param3,
           NULL AS param4,
           g_created_by AS created_by,
           g_timestamp AS date_created
      FROM stage_nom_approvers_import sr
     WHERE import_file_id = p_import_file_id;
     
     RAISE e_pgm_exit;   
   END IF;

   -- get count of any existing errors
   p_total_error_rec := f_get_error_record_count(p_import_file_id);
   v_msg := 'Previous error count >' || p_total_error_rec || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);
   
   -- validation errors
   v_msg := 'Call p_rpt_validation_err';
   p_rpt_validation_err(p_import_file_id, p_promotion_id, v_total_err_count);

   -- get count of any existing errors
   v_msg := 'Get p_total_error_rec';
   p_total_error_rec := f_get_error_record_count(p_import_file_id);

   v_msg := 'Check p_total_error_rec';
   IF( p_total_error_rec > 0) THEN
      -- import file has errors
      p_out_returncode := 1;
   END IF;
   v_msg := 'Process Load';
   -- load file records without errors
   IF (p_load_type = 'L') THEN

      p_imp_approver(p_import_file_id,p_promotion_id,v_out_returncode); 
      -- loaded without error
      p_out_returncode := NVL(v_out_returncode,0);
   END IF; -- load file
   
   v_msg := 'Success'
      || ': p_import_file_id >'   || p_import_file_id
      || '<, p_out_returncode >'  || p_out_returncode
      || '<, v_total_err_count >' || v_total_err_count
      || '<, p_total_error_rec >' || p_total_error_rec
      || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   COMMIT;

EXCEPTION
   WHEN e_pgm_exit THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Partial File load is not allowed. Please load approver for all levels', NULL);
      p_total_error_rec := v_import_record_count;
      p_out_returncode := 98; 
        COMMIT;
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      ROLLBACK;
      p_total_error_rec := v_import_record_count;
      p_out_returncode := 99;
END p_nom_approver_verify_load;

END pkg_nom_approver_verify_import;
/
