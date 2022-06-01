CREATE OR REPLACE PACKAGE pkg_badge_verify_import  IS

/***********************************************************************************
   Purpose:  To verify or load STAGE_BADGE_LOAD into PARTICIPANT_BADGE table.

   Person             Date           Comments
   -----------        ----------     --------------------------------------------
  Ravi Dhanekul   10/02/2012     Initial Version  
************************************************************************************/

  PROCEDURE p_badge_verify_load(p_import_file_id  IN NUMBER,
                                      p_load_type       IN VARCHAR2,
                                      p_promotion_id   IN NUMBER,
                                      p_user_id         IN NUMBER,
                                      p_earned_date IN DATE,                                                    
                                      p_total_error_rec OUT NUMBER,
                                      p_out_returncode  OUT NUMBER) ;
END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_badge_verify_import
IS
   /***********************************************************************************
      Purpose:  To verify or load STAGE_BADGE_LOAD into participant_badge table.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
      Ravi Dhanekula   10/03/2012    Initial Version
                       01/30/2013   Fixed the QC defect 2435.
                       05/23/2013   Fixed defect 3236
                       09/04/2013   Fixed defect # 4120
      Chidamba         10/23/2013   Bug Fixed #49301
      Ravi Dhanekula 11/8/2013  Re-designed the process to do bulk processing.
                              12/30/2013 Corrected the Typo-errors.
     Suresh J        01/13/2015   Bug Fix 58887,58979 - updated p_rpt_invalid_badge_aud_err logic to accept both givers and receivers.
     Suresh J        01/19/2015   Bug Fix 58887 - Fixed to accept receiver of any audience type   
    nagarajs         11/17/2015   Bug 63721 - Badges were duplicated during file load 
    nagarajs         03/03/2016  Bug 66037 - Badge file load process raising no data found error when badge type is fileload   
    Ravi Dhanekula   09/26/2017  G6-3007 - Badge file load not handling duplicate records in case of earn_multiple = 'false'                              
    Chidamba         12/21/2017  G6-3005 - Earn Multiple = False, and the pax has already earned them; then Log 
                                 those badge records as Error during file load
    Chidamba         12/29/2017  G6-3697 User receiving a badge via load set multiple earned = false then pax receiving another badge which is 
                                 set multiple earned = true is not getting displayed in My Badge page
    Chidamba         03/22/2018  G6-3984 fix - badge not loading for all pax in the file when the badge name is same 
                                 and the badge earn multiple set to false
	LOGANATHAN		02/26/2019  Bug 78895 - Badge "L"oad ignores return_code from call to p_imp_pax_badge                                 
   ************************************************************************************/

-- global package constants
c_cms_locale               CONSTANT vw_cms_code_value.locale%TYPE := 'en_US';
c_cms_status               CONSTANT vw_cms_code_value.cms_status%TYPE := 'true';
c_cms_content_status_live  CONSTANT cms_content.content_status%TYPE := 'Live';


-- global package variables
g_created_by               import_record_error.created_by%TYPE := 0;
g_timestamp                import_record_error.date_created%TYPE := SYSDATE;

 v_returncode              NUMBER:= 0;      --11/14/2012 added
 
 v_msg                VARCHAR2(300);


---------------------
---------------------
-- private functions
---------------------
-- get the count of records with errors
FUNCTION f_get_error_record_count
( p_import_file_id  IN  NUMBER
) RETURN INTEGER
IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('f_get_error_record_count');
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

   PROCEDURE p_insert_import_record_error (
      p_import_record    import_record_error%ROWTYPE)
   IS
      PRAGMA AUTONOMOUS_TRANSACTION;
      v_import_record_error_id   import_record_error.import_record_error_id%TYPE;
   BEGIN
      SELECT IMPORT_record_error_pk_SQ.NEXTVAL
        INTO v_import_record_error_id
        FROM DUAL;

      INSERT INTO import_record_error a (import_record_error_id,
                                         import_file_id,
                                         import_record_id,
                                         item_key,
                                         param1,
                                         param2,
                                         param3,
                                         param4,
                                         created_by,
                                         date_created)
           VALUES (v_import_record_error_id,
                   p_import_record.import_file_id,
                   p_import_record.import_record_id,
                   p_import_record.item_key,
                   p_import_record.param1,
                   p_import_record.param2,
                   p_import_record.param3,
                   p_import_record.param4,
                   g_created_by,
                   SYSDATE);

      COMMIT;
   END;

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

---------------------

PROCEDURE p_rpt_user_err
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_dup_user_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- duplicate user validation
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
             e.param1,
             NULL AS param2,
             NULL AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_badge_user AS
               (  -- get user data from stage records
                  SELECT s.import_file_id,
                         s.import_record_id,
                         s.badge_name,
                         s.insert_or_update,
                         s.user_name,                        
                         s.user_id
                    FROM STAGE_BADGE_LOAD s
                   WHERE s.import_file_id = p_import_file_id                  
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              AND e.date_created < g_timestamp
                         )
               )
               --  user_id is not null, means there is no valid user with the user_name supplied thur file.
               SELECT su.import_file_id,
                      su.import_record_id,
                      'admin.fileload.errors.USER_NOT_VALID' AS item_key,
                      su.user_name,
                      su.user_name AS param1
                 FROM stg_badge_user su
                WHERE su.user_id IS NULL                
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_user_err;
---------------------

PROCEDURE p_rpt_invalid_badge_err (
      p_import_file_id  IN     stage_badge_load.import_file_id%TYPE,
      p_promotion_id         IN     NUMBER,      
      p_total_err_count OUT NUMBER)
   IS
      rec_import_record_error   import_record_error%ROWTYPE;
      v_badge_name              badge_rule.badge_name%TYPE := '';
      v_process_name            VARCHAR2 (330) := 'p_rpt_invalid_badge_err';
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
   (  SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
             e.import_file_id,
             e.import_record_id,
             e.item_key,
             e.param1,
             NULL AS param2,
             NULL AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_badge AS
               (  -- get user data from stage records
                  SELECT s.import_file_id,
                         s.insert_or_update,
                         s.import_record_id,
                         s.user_name,
                         s.badge_name,
                         s.user_id
                    FROM stage_badge_load s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              AND e.date_created < g_timestamp
                         )
               )
              --check the name of the badge to be valid
               SELECT su.import_file_id,
                      su.import_record_id,
                      'admin.fileload.errors.BADGE_IS_INVALID' AS item_key,
                      su.user_name,
                      'Badge Name ' || su.badge_name AS param1
                 FROM stg_badge su
          WHERE   NOT EXISTS
          (SELECT cc2.cms_value,
                br.promotion_id,
                badge_rule_id,
                br.cm_asset_key,vw.cms_value
                  FROM vw_cms_asset_value CC,
                badge_rule br,
                vw_cms_asset_value CC2,
                vw_cms_asset_value vw
          WHERE     CC.asset_code = 'promotion.badge'
                AND cc.key = 'NAME'
                AND CC.CMS_VALUE = br.cm_asset_key
                AND br.badge_name = vw.asset_code
            AND UPPER (vw.cms_value) = UPPER (su.badge_name)
                AND br.promotion_id = p_promotion_id
                AND cc.content_id = cc2.content_id
                AND cc.locale = 'en_US'
                AND cc2.key = 'EARN_MULTIPLE')
          /************************* 12/21/2017 - G6-3005 Start ****************************/
           UNION ALL
            SELECT su.import_file_id,
                   su.import_record_id,
                   'admin.fileload.errors.BADGE_EARN_MULTIPLE_ERROR' AS item_key,
                    su.user_name,
                   'Badge Name ' || su.badge_name AS param1
              FROM stg_badge su
            WHERE EXISTS(SELECT cc2.cms_value,
                            br.promotion_id,
                            badge_rule_id,
                            br.cm_asset_key,vw.cms_value
                       FROM vw_cms_asset_value CC,
                            badge_rule br,
                            vw_cms_asset_value CC2,
                            vw_cms_asset_value vw
                      WHERE CC.asset_code = 'promotion.badge'
                            AND cc.key = 'NAME'
                            AND CC.CMS_VALUE = br.cm_asset_key
                            AND br.badge_name = vw.asset_code
                            AND UPPER (vw.cms_value) = UPPER (su.badge_name)
                            AND br.promotion_id = p_promotion_id
                            AND cc.content_id = cc2.content_id
                            AND cc.locale = 'en_US'
                            AND cc2.key = 'EARN_MULTIPLE'
                            AND cc2.cms_value = 'false'
                            AND EXISTS (SELECT 1 
                                          FROM participant_badge pb
                                         WHERE pb.badge_rule_id   = br.badge_rule_id
                                           AND pb.promotion_id    = br.promotion_id 
                                           AND pb.participant_id  = su.user_id))
         /************************* 12/21/2017 - G6-3005 End ****************************/ 
             ) e
   );      

      prc_execution_log_entry (v_process_name,
                             1,
                             'INFO',
                             SQLERRM,
                             NULL);
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
            'p_rpt_invalid_badge_err',
            '1',
            'ERROR',
            ' p_import_file_id: ' || p_import_file_id || '. ' || SQLERRM,
            NULL);         
   END;
   
   PROCEDURE p_rpt_invalid_badge_type_err (
      p_import_file_id  IN     stage_badge_load.import_file_id%TYPE,
      p_promotion_id         IN     NUMBER,      
      p_total_err_count OUT NUMBER)
   IS
      rec_import_record_error   import_record_error%ROWTYPE;
      v_badge_name              badge_rule.badge_name%TYPE := '';
      v_process_name            VARCHAR2 (330) := 'p_rpt_invalid_badge_type_err';
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
   (  SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
             e.import_file_id,
             e.import_record_id,
             e.item_key,
             e.param1,
             NULL AS param2,
             NULL AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_badge AS
               (  -- get user data from stage records
                  SELECT s.import_file_id,
                         s.insert_or_update,
                         s.import_record_id,                         
                         s.user_name,
                         s.badge_name,
                         s.user_id
                    FROM stage_badge_load s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              AND e.date_created < g_timestamp
                         )
               )
              --check the name of the badge to be valid
               SELECT su.import_file_id,
                      su.import_record_id,
                      'admin.fileload.errors.BADGE_TYPE_IS_INVALID' AS item_key,
                      su.user_name,
                      'Badge Name ' || su.badge_name AS param1
                 FROM stg_badge su
          WHERE   NOT EXISTS
          (SELECT * FROM vw_cms_asset_value vw,badge_rule br,badge b WHERE  br.badge_name = vw.asset_code
                AND UPPER (vw.cms_value) = UPPER (su.badge_name)
                AND br.promotion_id = p_promotion_id
                AND br.promotion_id = b.promotion_id
                AND b.badge_type = 'fileload')                
             ) e
   );      

      prc_execution_log_entry (v_process_name,
                             1,
                             'INFO',
                             SQLERRM,
                             NULL);
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
            'p_rpt_invalid_badge_type_err',
            '1',
            'ERROR',
            SQLERRM,
            NULL);
--         p_out_returncode := 99;
   END;
   
    PROCEDURE p_rpt_invalid_badge_status_err (
      p_import_file_id  IN     stage_badge_load.import_file_id%TYPE,
      p_promotion_id         IN     NUMBER,      
      p_total_err_count OUT NUMBER)
   IS
      rec_import_record_error   import_record_error%ROWTYPE;
      v_badge_name              badge_rule.badge_name%TYPE := '';
      v_process_name            VARCHAR2 (330) := 'p_rpt_invalid_badge_status_err';
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
   (  SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
             e.import_file_id,
             e.import_record_id,
             e.item_key,
             e.param1,
             NULL AS param2,
             NULL AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_badge AS
               (  -- get user data from stage records
                  SELECT s.import_file_id,
                         s.insert_or_update,
                         s.import_record_id,                         
                         s.user_name,
                         s.badge_name,
                         s.user_id
                    FROM stage_badge_load s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              AND e.date_created < g_timestamp
                         )
               )
              --check the name of the badge to be valid
               SELECT su.import_file_id,
                      su.import_record_id,
                      'admin.fileload.errors.BADGE_STATUS_IS_INVALID' AS item_key,
                      su.user_name,
                      'Badge Name ' || su.badge_name AS param1
                 FROM stg_badge su
          WHERE   NOT EXISTS
          (SELECT * FROM vw_cms_asset_value vw,badge_rule br,badge b WHERE  br.badge_name = vw.asset_code
                AND UPPER (vw.cms_value) = UPPER (su.badge_name)
                AND br.promotion_id = p_promotion_id
                AND br.promotion_id = b.promotion_id
                AND b.badge_type = 'fileload'
                AND b.status = 'A')                
             ) e
   );      

      prc_execution_log_entry (v_process_name,
                             1,
                             'INFO',
                             SQLERRM,
                             NULL);
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
            'p_rpt_invalid_badge_status_err',
            '1',
            'ERROR',
            SQLERRM,
            NULL);
--         p_out_returncode := 99;
   END;
   
   PROCEDURE p_rpt_invalid_badge_promo_err (
      p_import_file_id  IN     stage_badge_load.import_file_id%TYPE,
      p_promotion_id         IN     NUMBER,     
      p_total_err_count OUT NUMBER)
   IS
      rec_import_record_error   import_record_error%ROWTYPE;
      v_badge_name              badge_rule.badge_name%TYPE := '';
      v_process_name            VARCHAR2 (330) := 'p_rpt_invalid_badge_promo_err';
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
   (  SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
             e.import_file_id,
             e.import_record_id,
             e.item_key,
             e.param1,
             NULL AS param2,
             NULL AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_badge AS
               (  -- get user data from stage records
                  SELECT s.import_file_id,
                         s.insert_or_update,
                         s.import_record_id,                         
                         s.user_name,
                         s.badge_name,
                         s.user_id
                    FROM stage_badge_load s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              AND e.date_created < g_timestamp
                         )
                    AND EXISTS
                     (SELECT * FROM badge_promotion WHERE promotion_id=p_promotion_id)
               )
              --check  if the badge promotion is live
               SELECT su.import_file_id,
                      su.import_record_id,
                      'admin.fileload.errors.BADGE_PROMOTION_IS_EXPIRED' AS item_key,
                      su.user_name,
                      'Badge Name ' || su.badge_name AS param1
                 FROM stg_badge su
          WHERE NOT EXISTS
          (SELECT *
                FROM promotion p,
             badge_promotion bp,
             badge_rule br,
             vw_cms_asset_value vw
       WHERE     br.badge_name = vw.asset_code
             AND UPPER (vw.cms_value) = UPPER (su.badge_name)
             AND br.promotion_id = p_promotion_id
             AND br.promotion_id= bp.promotion_id
             AND bp.eligible_promotion_id = p.promotion_id
             AND NVL ( NVL (p.promotion_end_date,TO_DATE ('01/01/9999', 'MM/DD/YYYY')),SYSDATE +1) > TRUNC(SYSDATE))         
             ) e
   );
      prc_execution_log_entry (v_process_name,
                             1,
                             'INFO',
                             SQLERRM,
                             NULL);
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
            'p_rpt_invalid_badge_promo_err',
            '1',
            'ERROR',
            SQLERRM,
            NULL);
--         p_out_returncode := 99;
   END;
   
   PROCEDURE p_rpt_invalid_badge_aud_err (
      p_import_file_id  IN     stage_badge_load.import_file_id%TYPE,
      p_promotion_id         IN     NUMBER,      
      p_total_err_count OUT NUMBER)
   IS
      rec_import_record_error   import_record_error%ROWTYPE;
      v_badge_name              badge_rule.badge_name%TYPE := '';
      v_process_name            VARCHAR2 (330) := 'p_rpt_invalid_badge_aud_err';
      v_primary_audience_type   VARCHAR2(50);  --01/19/2015
      v_secondary_audience_type   VARCHAR2(50);  --01/19/2015
   BEGIN
      BEGIN
        select p.primary_audience_type,p.secondary_audience_type    --01/19/2015
        into v_primary_audience_type,v_secondary_audience_type
        from 
        promotion p,
        badge_promotion bp 
        where bp.promotion_id = p_promotion_id   
          and bp.eligible_promotion_id = p.promotion_id;
      EXCEPTION  -- 03/03/2016
        WHEN OTHERS THEN
          v_primary_audience_type := NULL;
          v_secondary_audience_type := NULL;
      END;
           
IF (v_secondary_audience_type = 'allactivepaxaudience' OR (v_secondary_audience_type = 'sameasprimaryaudience' and v_primary_audience_type = 'allactivepaxaudience'))THEN   --01/19/2015
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
                 e.param1,
                 NULL AS param2,
                 NULL AS param3,
                 NULL AS param4,
                 g_created_by AS created_by,
                 g_timestamp AS date_created
            FROM (
                   WITH stg_badge AS
                   (  -- get user data from stage records
                      SELECT s.import_file_id,
                             s.insert_or_update,
                             s.import_record_id,                        
                             s.user_name,
                             s.badge_name,
                             s.user_id
                        FROM stage_badge_load s
                       WHERE s.import_file_id = p_import_file_id
                          -- skip records previously marked as erred
                         AND s.import_record_id NOT IN
                             ( SELECT e.import_record_id
                                 FROM import_record_error e
                                WHERE e.import_file_id = p_import_file_id
                                  AND e.date_created < g_timestamp
                             )
                             AND EXISTS
                              (SELECT * FROM badge_promotion WHERE promotion_id=p_promotion_id)
                   )
                  --check if the pax is part of the audience
                   SELECT su.import_file_id,
                          su.import_record_id,
                          'admin.fileload.errors.BADGE_USER_NOT_IN_AUDIENCE' AS item_key,
                          su.user_name,
                          'Badge Name ' || su.badge_name AS param1
                     FROM stg_badge su
              WHERE NOT EXISTS
              (SELECT *
                    FROM promotion p,
                 badge_promotion bp,
                 badge_rule br,
                 vw_cms_asset_value vw
           WHERE     br.badge_name = vw.asset_code
                 AND UPPER (vw.cms_value) = UPPER (su.badge_name)
                 AND br.promotion_id = p_promotion_id
                 AND br.promotion_id= bp.promotion_id
                 AND bp.eligible_promotion_id = p.promotion_id
                 AND (p.secondary_audience_type = 'allactivepaxaudience' OR (p.secondary_audience_type = 'sameasprimaryaudience' AND p.primary_audience_type = 'allactivepaxaudience')))  
            ) e
      );       
ELSIF v_primary_audience_type IS NOT NULL OR v_secondary_audience_type IS NOT NULL THEN  -- 03/03/2016
--ELSE  --01/19/2015  -- 03/03/2016
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
             e.param1,
             NULL AS param2,
             NULL AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_badge AS
               (  -- get user data from stage records
                  SELECT s.import_file_id,
                         s.insert_or_update,
                         s.import_record_id,                        
                         s.user_name,
                         s.badge_name,
                         s.user_id
                    FROM stage_badge_load s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              AND e.date_created < g_timestamp
                         )
                         AND EXISTS
                          (SELECT * FROM badge_promotion WHERE promotion_id=p_promotion_id)
               )
              --check if the pax is part of the audience
               SELECT su.import_file_id,
                      su.import_record_id,
                      'admin.fileload.errors.BADGE_USER_NOT_IN_AUDIENCE' AS item_key,
                      su.user_name,
                      'Badge Name ' || su.badge_name AS param1
                 FROM stg_badge su
          WHERE NOT EXISTS
          (SELECT *           
           FROM participant_audience pa,
                promo_audience paa,
                badge_promotion bp,
                badge_rule br,
                vw_cms_asset_value vw 
          WHERE     br.badge_name = vw.asset_code
                AND UPPER (vw.cms_value) = UPPER (su.badge_name)
                AND br.promotion_id = p_promotion_id
                AND br.promotion_id = bp.promotion_id
                AND bp.eligible_promotion_id = paa.promotion_id
                AND paa.audience_id = pa.audience_id
                AND ((promo_audience_type = 'SECONDARY' AND v_primary_audience_type = 'specifyaudience') OR v_secondary_audience_type = 'sameasprimaryaudience'OR v_secondary_audience_type = 'activepaxfromprimarynodeaudience' OR v_secondary_audience_type = 'activepaxfromprimarynodebelowaudience')
                AND pa.user_id = su.user_id) 
        ) e
  );   
END IF; 

      prc_execution_log_entry (v_process_name,
                             1,
                             'INFO',
                             SQLERRM,
                             NULL);
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
            'p_rpt_invalid_badge_aud_err',
            '1',
            'ERROR',
            SQLERRM,
            NULL);
--         p_out_returncode := 99;
   END;
   
   PROCEDURE p_imp_pax_badge (
      p_import_file_id  IN     stage_badge_load.import_file_id%TYPE,
      p_promotion_id         IN     NUMBER,
      p_earned_date          IN     DATE,
      p_out_returncode OUT NUMBER) --12/30/2013
   IS
      rec_import_record_error   import_record_error%ROWTYPE;
      v_badge_name              badge_rule.badge_name%TYPE := '';
      v_process_name            VARCHAR2 (330) := 'p_imp_pax_badge';
   BEGIN
     MERGE INTO participant_badge pb
USING(       
  select  cms_value,promotion_id,badge_rule_id,cm_asset_key,user_id,import_file_id from (select  cc2.cms_value,--09/26/2017 start
                br.promotion_id,
                badge_rule_id,
                br.cm_asset_key,
                sbl.user_id,
                sbl.import_file_id,
                RANK() OVER ( PARTITION BY br.promotion_id,badge_rule_id,cm_asset_key, sbl.user_id --03/22/2018 added user id
                                                   ORDER BY decode(cc2.cms_value,'false',rownum,import_file_id) DESC
                                                 ) AS rec_rank--09/26/2017 end
           from vw_cms_asset_value cc,
                badge_rule br,
                vw_cms_asset_value cc2,
                vw_cms_asset_value vw,
                stage_badge_load sbl
          where     cc.asset_code = 'promotion.badge'
                and cc.key = 'NAME'
                and cc.cms_value = br.cm_asset_key
                and br.badge_name = vw.asset_code
                and upper (vw.cms_value) = upper (sbl.badge_name)
                and br.promotion_id = p_promotion_id
                and cc.content_id = cc2.content_id
                and cc.locale =  'en_US'
                and cc2.locale = 'en_US'   --11/17/2015
                and vw.locale =  'en_US'   --11/17/2015
                and cc2.key = 'EARN_MULTIPLE'
                AND sbl.import_file_id = p_import_file_id
                and sbl.import_record_id not in
                         ( select e.import_record_id
                             from import_record_error e
                            where e.import_file_id = p_import_file_id
                              and e.date_created < g_timestamp
                         )) WHERE rec_rank = 1) s--09/26/2017
                ON (pb.participant_id = s.user_id AND s.cms_value ='false' AND pb.promotion_id = p_promotion_id
                    and pb.badge_rule_id = s.badge_rule_id --12/29/2017 G6-3697
                    )
                 WHEN MATCHED THEN UPDATE 
                   SET  -- badge_rule_id = s.badge_rule_id, --12/29/2017 G6-3697
                  date_modified = SYSDATE,
                   earned_date = p_earned_date,
                   version = version + 1   
         WHEN NOT MATCHED THEN INSERT
         (pb.participant_badge_id,
                                             pb.participant_id,
                                             pb.promotion_id,
                                             pb.badge_rule_id,
                                             pb.status,
                                             pb.earned_date,
                                             pb.created_by,
                                             pb.date_created,
                                             pb.is_earned,
                                             version)
                 VALUES (PARTICIPANT_BADGE_PK_SQ.NEXTVAL,
                         s.user_id,
                         p_promotion_id,
                         s.badge_rule_id,
                         'A',
                         p_earned_date,
                         1,
                         SYSDATE,
                         1,
                         1);     
                   
      prc_execution_log_entry (v_process_name,
                             1,
                             'INFO',
                             SQLERRM,
                             NULL);
                             
                             p_out_returncode :=00; --12/30/2013
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (
            'p_imp_pax_badge',
            '1',
            'ERROR',
            SQLERRM,
            NULL);
       p_out_returncode := 99; --12/30/2013
   END;
-- public procecures
PROCEDURE p_badge_verify_load(p_import_file_id  IN NUMBER,
                                      p_load_type       IN VARCHAR2,
                                      p_promotion_id   IN NUMBER,
                                      p_user_id         IN NUMBER,
                                      p_earned_date IN DATE,                                                    
                                      p_total_error_rec OUT NUMBER,
                                      p_out_returncode  OUT NUMBER) 
IS
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_badge_verify_load');
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';

   v_msg                execution_log.text_line%TYPE;
   v_total_err_count    INTEGER; -- count of errors
   v_import_record_count   import_file.import_record_count%TYPE;

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

   -- get count of any existing errors
   p_total_error_rec := f_get_error_record_count(p_import_file_id);
   v_msg := 'Previous error count >' || p_total_error_rec || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   -- report validation errors
   v_msg := 'Call p_rpt_user_err';
   p_rpt_user_err(p_import_file_id, v_total_err_count);

   v_msg := 'Call p_rpt_invalid_badge_err';
   p_rpt_invalid_badge_err(p_import_file_id,p_promotion_id,v_total_err_count);
   
   v_msg := 'Call p_rpt_invalid_badge_type_err';
   p_rpt_invalid_badge_type_err(p_import_file_id,p_promotion_id,v_total_err_count);
   
    v_msg := 'Call p_rpt_invalid_badge_status_err';
   p_rpt_invalid_badge_status_err(p_import_file_id,p_promotion_id,v_total_err_count);

   v_msg := 'Call p_rpt_invalid_badge_promo_err';
   p_rpt_invalid_badge_promo_err(p_import_file_id,p_promotion_id,v_total_err_count);

   v_msg := 'Call p_rpt_invalid_badge_aud_err';
   p_rpt_invalid_badge_aud_err(p_import_file_id,p_promotion_id,v_total_err_count);

      -- get count of any existing errors
   p_total_error_rec := f_get_error_record_count(p_import_file_id);


   -- load file records without errors
   IF (p_load_type = 'L') THEN
      p_imp_pax_badge (p_import_file_id,
                             p_promotion_id,
                             p_earned_date,
                             p_out_returncode);

      -- loaded without error
      --p_out_returncode := 0;   --02/26/2019
   END IF; -- load file

    IF v_returncode <> 0 THEN

      prc_execution_log_entry(c_process_name,c_release_level,'ERROR',
                            'Error in p_badge_verify_load while invoking for file ID '
                            ||p_import_file_id,null);
      
    END IF;
    


   v_msg := CASE WHEN p_out_returncode=0 THEN 'Success' ELSE 'Error' END     --02/26/2019  Bug 78895
      || ': p_import_file_id >'   || p_import_file_id
      || '<, p_out_returncode >'  || p_out_returncode
      || '<, v_total_err_count >' || v_total_err_count
      || '<, p_total_error_rec >' || p_total_error_rec
      || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   COMMIT;

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      ROLLBACK;
      p_total_error_rec := v_import_record_count;
      p_out_returncode := 99;
END p_badge_verify_load;
END pkg_badge_verify_import;
/
