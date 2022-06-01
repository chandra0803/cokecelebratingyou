CREATE OR REPLACE PROCEDURE prc_tier_2_data_convert
 (pi_old_schema   IN  VARCHAR2,
  po_return_code  OUT NUMBER)
IS

/*-----------------------------------------------------------------------------
  Process Name : PRC_TIER_2_DATA_CONVERT
  Purpose      : Convert Tier 2 data from G4 schema to G5 schema

  Tier 2
  1  Activity (recognition, quizzes, nominations, PURLs, product claims)
  2  Promotion setup - do not mark as complete;
  3  Audiences
  4  Budgets (will not be migrated for international programs)
  5  Libraries - quizzes, calculator, products, forms
  6  Unredeemed merchandise awards (recommend sweeping to SVA)

  
  Modification History
  Person          Date         Comments
  -------------  ----------  -------------------------------------------------
  Arun S         02/22/2012  Initial creation
  Arun S         02/04/2014  Removed columns number_attempts_given, playing_attempts_method, attempts_expiration_date
                             which are removed from Promotion table
  Ravi Dhanekula 02/28/2014 Commented out card and its realted tables. 
-----------------------------------------------------------------------------*/


  TYPE t_card               IS REF CURSOR;
  r_card                    t_card;

  --Procedure variables
  C_process_name             execution_log.process_name%TYPE  := 'prc_tier_2_data_convert';
  C_release_level            execution_log.release_level%TYPE := '1';
  C_severity_i               execution_log.severity%TYPE      := 'INFO';
  C_severity_e               execution_log.severity%TYPE      := 'ERROR';
  
  v_exe_log_msg              execution_log.text_line%TYPE;
  v_stage                    VARCHAR2(1000);
  v_tbl_name                 user_tables.table_name%TYPE;
  v_g4_schema                all_tables.owner%TYPE;
  v_dml_cnt                  NUMBER;
  v_sql                      VARCHAR2(32767);
  v_msg_tbl_nf               VARCHAR2(1000);
  v_card_id                  card.card_id%TYPE;
  v_card_id_g5               card.card_id%TYPE;
  v_claim_form_id            claim_form.claim_form_id%TYPE;
  v_claim_form_step_email_id claim_form_step_email.claim_form_step_email_id%TYPE;
  v_claim_form_step_id       claim_form_step.claim_form_step_id%TYPE;
  vo_name_asset_code_id      NUMBER;
  v_promotion_id             promotion.promotion_id%TYPE; 

  vg4_card_id                card.card_id%TYPE;
  vg4_card_name              card.card_name%TYPE;
  vg4_small_image_name       card.small_image_name%TYPE;
  vg4_large_image_name       card.large_image_name%TYPE;
  vg4_is_active              card.is_active%TYPE;
  vg4_is_mobile              card.is_mobile%TYPE;
  vg4_created_by             card.created_by%TYPE;
  vg4_date_created           card.date_created%TYPE;
  vg4_modified_by            card.modified_by%TYPE;
  vg4_date_modified          card.date_modified%TYPE;
  vg4_version                card.version%TYPE;
  vg4_flash_name             ecard.flash_name%TYPE; 
  vg4_is_translatable        ecard.is_translatable%TYPE;
  vg4_locale                 ecard_locale.locale%TYPE; 
  vg4_display_name           ecard_locale.display_name%TYPE;
  v_us_bud_cnt               NUMBER;
  v_budget_master_id         budget_master.budget_master_id%TYPE;
                          

  FUNCTION fnc_check_g5_tbl (pi_tbl_name IN VARCHAR2)
  RETURN VARCHAR2 IS
    v_tbl  user_tables.table_name%TYPE := 'X';
  BEGIN
    SELECT table_name
      INTO v_tbl
      FROM user_tables
     WHERE table_name = UPPER(pi_tbl_name);
    RETURN v_tbl;  
  EXCEPTION
    WHEN OTHERS THEN
      RETURN v_tbl;
  END;

  FUNCTION fnc_check_g4_tbl (pi_owner    IN VARCHAR2,
                             pi_tbl_name IN VARCHAR2)
  RETURN VARCHAR2 IS
    v_tbl  all_tables.table_name%TYPE := 'X';
  BEGIN
    SELECT table_name
      INTO v_tbl
      FROM all_tables
     WHERE owner      = UPPER(pi_owner)
       AND table_name = UPPER(pi_tbl_name);
    RETURN v_tbl;    
  EXCEPTION
    WHEN OTHERS THEN
      RETURN v_tbl;
  END;

  PROCEDURE p_execution_log_entry 
   (i_process_name  execution_log.process_name%type,
    i_release_level execution_log.release_level%type,
    i_severity      execution_log.severity%type,
    i_text_line     execution_log.text_line%type,
    i_dbms_job_nbr  execution_log.dbms_job_nbr%TYPE) IS

  PRAGMA AUTONOMOUS_TRANSACTION;

  BEGIN

    INSERT INTO execution_log
                (execution_log_id,
                 session_id,
                 process_name,
                 severity,
                 text_line,
                 release_level,
                 dbms_job_nbr,
                 created_by,
                 date_created)
          VALUES(execution_log_pk_sq.NEXTVAL,
                 USERENV('sessionid'),
                 i_process_name,
                 i_severity,
                 i_text_line,
                 i_release_level,
                 i_dbms_job_nbr,
                 0,
                 SYSDATE);
    COMMIT;
  END;

  PROCEDURE show_msg_count
   ( i_msg     VARCHAR2,
     i_rec_cnt INTEGER
   ) IS
      v_msg       execution_log.text_line%type;
      lv_rec_cnt  VARCHAR2(10) := TO_CHAR(i_rec_cnt, 'fm9,999,990');
  BEGIN
    
    v_msg := i_msg || RPAD('.', (37 - LENGTH(i_msg)), '.') || RPAD(':', 10-LENGTH(lv_rec_cnt)) || lv_rec_cnt || ' records';
    dbms_output.put_line(v_msg);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg,
                          NULL);
  END;


  PROCEDURE prc_promo_name_insert_cms 
   (pi_promo_name          IN  VARCHAR2,
    po_name_asset_code_id  OUT NUMBER)
  IS

  /*----------------------------------------------------------------------------
    Purpose: Create cms records for promotion.promo_name_asset_code
  
    Modification History
    Person        Date         Comments
    -----------   ----------   -------------------------------------------------
    Arun S        02/22/2012   Initial creation
  
  -----------------------------------------------------------------------------*/

  v_cms_application_id     cms_application.id%TYPE; 
  v_cms_asset_type_id      cms_asset_type.id%TYPE;
  v_cms_section_id         cms_section.id%TYPE;
  v_cms_asset_id           cms_asset.id%TYPE;
  v_name_asset_code_id     NUMBER;
  v_cms_content_id         cms_content.id%TYPE;
  v_cms_content_key_id     cms_content_key.id%TYPE;
  v_default_aud            cms_audience.id%TYPE;   
  v_stage                  VARCHAR2(100);
  
  BEGIN

    BEGIN
    v_stage := 'Find cms_asset_type id';
    SELECT id
      INTO v_cms_asset_type_id
      FROM cms_asset_type
     WHERE name = '_PROMOTION_NAME_DATA';
    EXCEPTION
    WHEN NO_DATA_FOUND THEN

      SELECT id
        INTO v_cms_application_id
        FROM cms_application
       WHERE name = 'Beacon Default';

      SELECT cms_generic_pk_sq.NEXTVAL
        INTO v_cms_asset_type_id
        FROM dual;
          
      v_stage := 'Insert cms_asset_type';
      INSERT INTO cms_asset_type
                 (id, 
                  entity_version, 
                  name, 
                  description, 
                  is_previewable, 
                  is_multi, 
                  is_public, 
                  is_display, 
                  application_id, 
                  created_by, 
                  date_created, 
                  modified_by, 
                  date_modified)
          VALUES (v_cms_asset_type_id, 
                  0, 
                  '_PROMOTION_NAME_DATA', 
                  '_PROMOTION_NAME_DATA', 
                  'T', 
                  'F', 
                  'T', 
                  'F', 
                  v_cms_application_id, 
                  '5662', 
                  SYSDATE, 
                  NULL, 
                  NULL);

      INSERT INTO cms_asset_type_item
                  (id, 
                   entity_version, 
                   key, 
                   name, 
                   description, 
                   is_required, 
                   is_unique, 
                   is_translatable, 
                   sort_order, 
                   asset_type_id, 
                   data_type, 
                   max_length, 
                   min_length, 
                   created_by, 
                   date_created, 
                   modified_by, 
                   date_modified)
           VALUES (cms_generic_pk_sq.NEXTVAL, 
                   0, 
                   'PROMOTION_NAME_', 
                   'Promotion Name', 
                   'Promotion Name', 
                   'F', 
                   'F', 
                   'T', 
                   1, 
                   v_cms_asset_type_id, 
                   'HTML', 
                   0, 
                   0, 
                   '5662', 
                   SYSDATE, 
                   NULL, 
                   NULL);
    END;
      
    BEGIN
    v_stage := 'Find cms_section id';
    SELECT id
      INTO v_cms_section_id
      FROM cms_section
     WHERE code = 'promotion_name';
    EXCEPTION
    WHEN NO_DATA_FOUND THEN

      SELECT id
        INTO v_cms_application_id
        FROM cms_application
       WHERE name = 'Beacon Default';

      SELECT cms_generic_pk_sq.NEXTVAL
        INTO v_cms_section_id
        FROM dual;

      v_stage := 'Insert cms_section';
      INSERT INTO cms_section
                  (id, 
                   entity_version, 
                   code, 
                   description, 
                   name, 
                   application_id, 
                   created_by, 
                   date_created, 
                   modified_by, 
                   date_modified)
           VALUES (v_cms_section_id, 
                   0, 
                   'promotion_name', 
                   'Promotion Name', 
                   'Promotion Name', 
                   v_cms_application_id, 
                   'testuser', 
                   SYSDATE, 
                   NULL, 
                   NULL);
    END;

    BEGIN
    SELECT cms_generic_pk_sq.NEXTVAL
      INTO v_cms_asset_id
      FROM dual;

    SELECT cms_unique_name_sq.NEXTVAL
      INTO v_name_asset_code_id
      FROM dual;

    v_stage := 'Insert cms_asset';
    INSERT INTO cms_asset
                (id, 
                 entity_version, 
                 code, 
                 name, 
                 description, 
                 preview_page, 
                 is_public, 
                 section_id, 
                 asset_type_id, 
                 parent_asset_id, 
                 pax_or_admin, 
                 module, 
                 product_version, 
                 created_by, 
                 date_created, 
                 modified_by, 
                 date_modified)
         VALUES (v_cms_asset_id, 
                 1, 
                 'promotion_name.'||v_name_asset_code_id, 
                 'Promotion Name', 
                 'Promotion Name', 
                 NULL, 
                 'T', 
                 v_cms_section_id, 
                 v_cms_asset_type_id, 
                 NULL, 
                 NULL, 
                 NULL, 
                 '5.0.0', 
                 '5662', 
                 SYSDATE, 
                 NULL, 
                 NULL);
    END;

    BEGIN
    SELECT cms_generic_pk_sq.NEXTVAL
      INTO v_cms_content_key_id
      FROM DUAL;

    v_stage := 'Insert cms_content_key';
    INSERT INTO cms_content_key
                (id, 
                 entity_version, 
                 sort_order, 
                 guid, 
                 asset_id, 
                 start_timestamp, 
                 end_timestamp, 
                 created_by, 
                 date_created, 
                 modified_by, 
                 date_modified, 
                 filter_string)
         VALUES (v_cms_content_key_id, 
                 0, 
                 1, 
                 SYS_GUID(), 
                 v_cms_asset_id, 
                 NULL, 
                 NULL, 
                 '5662', 
                 SYSDATE, 
                 NULL, 
                 NULL, 
                 NULL);
    END;                    

    BEGIN
    SELECT cms_generic_pk_sq.NEXTVAL
      INTO v_cms_content_id
      FROM DUAL;

    v_stage := 'Insert cms_content';
    INSERT INTO cms_content
                (id, 
                entity_version, 
                locale, 
                version, 
                content_status, 
                edited_by, 
                edited_timestamp, 
                added_by, 
                added_timestamp, 
                submitted_by, 
                submitted_timestamp, 
                approved_by, 
                approved_timestamp, 
                guid, 
                content_key_id, 
                export_date, 
                content_version, 
                created_by, 
                date_created, 
                modified_by, 
                date_modified)
        VALUES (v_cms_content_id, 
                1, 
                'en_US', 
                1, 
                'Live', 
                NULL, 
                 NULL, 
                '5662', 
                SYSDATE, 
                NULL, 
                NULL, 
                NULL, 
                NULL, 
                SYS_GUID(), 
                v_cms_content_key_id, 
                NULL, 
                0.1, 
                '5662', 
                SYSDATE, 
                NULL, 
                NULL);

    v_stage := 'Insert cms_content_data';
    INSERT INTO cms_content_data
                (content_id,
                 value,
                 google_value,
                 key)
         VALUES (v_cms_content_id,
                 pi_promo_name,
                 NULL,
                 'PROMOTION_NAME_');

    v_stage := 'Get audience_id'; 
    SELECT au.id
      INTO v_default_aud
      FROM cms_audience au, 
           cms_application ca
     WHERE au.application_id = ca.id
       AND UPPER(ca.code) = 'BEACON' 
       AND UPPER(au.code) = 'DEFAULT'
       AND ROWNUM = 1;
                
    v_stage := 'Insert cms_content_key_audience_lnk'; 
    INSERT INTO cms_content_key_audience_lnk
                (content_key_id,
                 audience_id)
         VALUES (v_cms_content_key_id,
                 v_default_aud);    

    END;

    po_name_asset_code_id := v_name_asset_code_id;

  EXCEPTION
    WHEN OTHERS THEN
      po_name_asset_code_id := NULL;                     
  END;

BEGIN   --Tier 2 Data Convert Starts

  p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                       'Procedure Started for in param pi_old_schema : '||pi_old_schema,
                        NULL);

  v_g4_schema  := UPPER(pi_old_schema);
  v_msg_tbl_nf := 'Table not found in G4 or G5: ';

  --Audience tables start
  v_stage      := 'MERGE audience';
  v_tbl_name := 'audience';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
       MERGE INTO audience d
       USING (SELECT audience_id, 
                     name, 
                     list_type, 
                     giftcode_only, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM audience_g4
               ORDER BY audience_id
              ) s
          ON (d.audience_id = s.audience_id)
        WHEN MATCHED THEN UPDATE 
         SET d.name          = s.name,
             d.list_type     = s.list_type,
             d.giftcode_only = s.giftcode_only,
             d.created_by    = s.created_by,
             d.date_created  = s.date_created,
             d.modified_by   = s.modified_by,
             d.date_modified = s.date_modified,
             d.version       = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.audience_id, 
              d.name, 
              d.list_type, 
              d.giftcode_only, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.audience_id, 
              s.name, 
              s.list_type, 
              s.giftcode_only, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE audience_criteria';
  v_tbl_name := 'audience_criteria';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO audience_criteria d
       USING (SELECT audience_criteria_id, 
                     audience_id, 
                     first_name, 
                     last_name, 
                     employer_id, 
                     position_type, 
                     department_type, 
                     language_id, 
                     node_id, 
                     node_name, 
                     node_type_id, 
                     user_node_role, 
                     include_child_nodes, 
                     exclude_country_id, 
                     exclude_node_id, 
                     exclude_node_name, 
                     exclude_node_type_id, 
                     exclude_user_node_role, 
                     exclude_include_child_nodes, 
                     exclude_position_type, 
                     exclude_department_type, 
                     created_by, 
                     date_created, 
                     version, 
                     country_id
                FROM audience_criteria_g4
               ORDER BY audience_criteria_id  
              ) s
          ON (d.audience_criteria_id = s.audience_criteria_id)
        WHEN MATCHED THEN UPDATE 
         SET d.audience_id                 = s.audience_id,
             d.first_name                  = s.first_name,
             d.last_name                   = s.last_name,
             d.employer_id                 = s.employer_id,
             d.position_type               = s.position_type,
             d.department_type             = s.department_type,
             d.language_id                 = s.language_id,
             d.node_id                     = s.node_id,
             d.node_name                   = s.node_name,
             d.node_type_id                = s.node_type_id,
             d.user_node_role              = s.user_node_role,
             d.include_child_nodes         = s.include_child_nodes,
             d.exclude_country_id          = s.exclude_country_id,
             d.exclude_node_id             = s.exclude_node_id,
             d.exclude_node_name           = s.exclude_node_name,
             d.exclude_node_type_id        = s.exclude_node_type_id,
             d.exclude_user_node_role      = s.exclude_user_node_role,
             d.exclude_include_child_nodes = s.exclude_include_child_nodes,
             d.exclude_position_type       = s.exclude_position_type,
             d.exclude_department_type     = s.exclude_department_type,
             d.created_by                  = s.created_by,
             d.date_created                = s.date_created,
             d.version                     = s.version,
             d.country_id                  = s.country_id
        WHEN NOT MATCHED THEN INSERT
             (d.audience_criteria_id, 
              d.audience_id, 
              d.first_name, 
              d.last_name, 
              d.employer_id, 
              d.position_type, 
              d.department_type, 
              d.language_id, 
              d.node_id, 
              d.node_name, 
              d.node_type_id, 
              d.user_node_role, 
              d.include_child_nodes, 
              d.exclude_country_id, 
              d.exclude_node_id, 
              d.exclude_node_name, 
              d.exclude_node_type_id, 
              d.exclude_user_node_role, 
              d.exclude_include_child_nodes, 
              d.exclude_position_type, 
              d.exclude_department_type, 
              d.created_by, 
              d.date_created, 
              d.version, 
              d.country_id
              )
      VALUES (s.audience_criteria_id, 
              s.audience_id, 
              s.first_name, 
              s.last_name, 
              s.employer_id, 
              s.position_type, 
              s.department_type, 
              s.language_id, 
              s.node_id, 
              s.node_name, 
              s.node_type_id, 
              s.user_node_role, 
              s.include_child_nodes, 
              s.exclude_country_id, 
              s.exclude_node_id, 
              s.exclude_node_name, 
              s.exclude_node_type_id, 
              s.exclude_user_node_role, 
              s.exclude_include_child_nodes, 
              s.exclude_position_type, 
              s.exclude_department_type, 
              s.created_by, 
              s.date_created, 
              s.version, 
              s.country_id
              );


    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  

  v_stage      := 'MERGE audience_criteria_char';
  v_tbl_name := 'audience_criteria_char';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO audience_criteria_char d
       USING (SELECT audience_criteria_char_id, 
                     audience_criteria_id, 
                     characteristic_id, 
                     criteria_characteristic_value, 
                     search_type, 
                     created_by, 
                     date_created, 
                     version
                FROM audience_criteria_char_g4
               ORDER BY audience_criteria_char_id  
              ) s
          ON (d.audience_criteria_char_id = s.audience_criteria_char_id)
        WHEN MATCHED THEN UPDATE 
         SET d.audience_criteria_id          = s.audience_criteria_id,
             d.characteristic_id             = s.characteristic_id,
             d.criteria_characteristic_value = s.criteria_characteristic_value,
             d.search_type                   = s.search_type,
             d.created_by                    = s.created_by,
             d.date_created                  = s.date_created,
             d.version                       = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.audience_criteria_char_id, 
              d.audience_criteria_id, 
              d.characteristic_id, 
              d.criteria_characteristic_value, 
              d.search_type, 
              d.created_by, 
              d.date_created, 
              d.version
              )
      VALUES (s.audience_criteria_char_id, 
              s.audience_criteria_id, 
              s.characteristic_id, 
              s.criteria_characteristic_value, 
              s.search_type, 
              s.created_by, 
              s.date_created, 
              s.version
              );

    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE participant_audience';
  v_tbl_name := 'participant_audience';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO participant_audience d
       USING (SELECT user_id, 
                     audience_id, 
                     participant_audience_index, 
                     created_by, 
                     date_created
                FROM participant_audience_g4
              ) s
          ON (d.user_id     = s.user_id AND
              d.audience_id = s.audience_id )
        WHEN MATCHED THEN UPDATE 
         SET d.participant_audience_index = s.participant_audience_index,
             d.created_by                 = s.created_by,
             d.date_created               = s.date_created
        WHEN NOT MATCHED THEN INSERT
             (d.user_id, 
              d.audience_id, 
              d.participant_audience_index, 
              d.created_by, 
              d.date_created
              )
      VALUES (s.user_id, 
              s.audience_id, 
              s.participant_audience_index, 
              s.created_by, 
              s.date_created
              );

    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  --Audience tables end  


  --Budget table start
  v_stage      := 'MERGE budget_master';
  v_tbl_name := 'budget_master';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    FOR rec_bud_mstr IN (SELECT budget_master_id, 
                                budget_type, 
                                is_active, 
                                cm_asset_code, 
                                name_cm_key, 
                                overrideable, 
                                --budget_overdraw_approver, 
                                multi_promotion, 
                                final_payout_rule, 
                                created_by, 
                                date_created, 
                                modified_by, 
                                date_modified, 
                                version
                           FROM budget_master_g4
                          ORDER BY budget_master_id
                         )
    LOOP

      v_us_bud_cnt := 0;
    
      IF rec_bud_mstr.budget_type = 'pax' THEN
    
        SELECT COUNT(c.country_id)
          INTO v_us_bud_cnt
          FROM country_g4 c
         WHERE c.country_code = 'us'
           AND EXISTS (SELECT 'X'
                         FROM budget_g4 b,
                              user_address_g4 ua
                        WHERE b.user_id          = ua.user_id
                          AND ua.is_primary      = 1
                          AND b.budget_master_id = rec_bud_mstr.budget_master_id
                          AND ua.country_id      = c.country_id
                       );

      ELSIF rec_bud_mstr.budget_type = 'node' THEN
      
        SELECT COUNT(c.country_id)
          INTO v_us_bud_cnt
          FROM country_g4 c
         WHERE c.country_code = 'us'
           AND EXISTS (SELECT 'X'
                         FROM budget_g4 b,
                              user_node_g4 un,
                              user_address_g4 ua
                        WHERE b.node_id          = un.node_id
                          AND un.user_id         = ua.user_id
                          AND ua.is_primary      = 1
                          AND b.budget_master_id = rec_bud_mstr.budget_master_id
                          AND ua.country_id      = c.country_id
                       );

      ELSIF rec_bud_mstr.budget_type = 'central' THEN

        SELECT COUNT(country_id)
          INTO v_us_bud_cnt
          FROM country_g4
         WHERE country_code = 'us'
           AND status = 'active';
                         
      END IF;

      IF v_us_bud_cnt > 0 THEN

        BEGIN
          SELECT budget_master_id
            INTO v_budget_master_id
            FROM budget_master
           WHERE budget_master_id = rec_bud_mstr.budget_master_id;
        EXCEPTION
          WHEN NO_DATA_FOUND THEN
        
              INSERT INTO budget_master
                         (budget_master_id, 
                          budget_type, 
                          is_active, 
                          cm_asset_code, 
                          name_cm_key, 
                          overrideable, 
                          --budget_overdraw_approver, 
                          multi_promotion, 
                          final_payout_rule, 
                          created_by, 
                          date_created, 
                          modified_by, 
                          date_modified, 
                          version
                          )
                  VALUES (rec_bud_mstr.budget_master_id, 
                          rec_bud_mstr.budget_type, 
                          rec_bud_mstr.is_active, 
                          rec_bud_mstr.cm_asset_code, 
                          rec_bud_mstr.name_cm_key, 
                          rec_bud_mstr.overrideable, 
                          --rec_bud_mstr.budget_overdraw_approver, 
                          rec_bud_mstr.multi_promotion, 
                          rec_bud_mstr.final_payout_rule, 
                          rec_bud_mstr.created_by, 
                          rec_bud_mstr.date_created, 
                          rec_bud_mstr.modified_by, 
                          rec_bud_mstr.date_modified, 
                          rec_bud_mstr.version
                          );         
        END;
        
      END IF;
      
    END LOOP;                         

    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE budget';
  v_tbl_name := 'budget';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO budget d
       USING (SELECT b.budget_id, 
                     b.budget_master_id, 
                     b.user_id, 
                     b.node_id, 
                     b.original_value, 
                     b.current_value, 
                     b.overdrawn, 
                     b.status, 
                     b.start_date, 
                     b.end_date, 
                     b.created_by, 
                     b.date_created, 
                     b.modified_by, 
                     b.date_modified, 
                     b.version, 
                     effective_date 
                FROM budget_g4 b,
                     budget_master bm
               WHERE b.budget_master_id = bm.budget_master_id
               ORDER BY budget_id
              ) s
          ON (d.budget_id = s.budget_id)
        WHEN MATCHED THEN UPDATE 
         SET d.budget_master_id = s.budget_master_id,
             d.user_id          = s.user_id,
             d.node_id          = s.node_id,
             d.original_value   = s.original_value,
             d.current_value    = s.current_value,
             d.overdrawn        = s.overdrawn,
             d.status           = s.status,
             d.start_date       = s.start_date,
             d.end_date         = s.end_date,
             d.created_by       = s.created_by,
             d.date_created     = s.date_created,
             d.modified_by      = s.modified_by,
             d.date_modified    = s.date_modified,
             d.version          = s.version,
             d.effective_date   = s.effective_date
        WHEN NOT MATCHED THEN INSERT
             (d.budget_id, 
              d.budget_master_id, 
              d.user_id, 
              d.node_id, 
              d.original_value, 
              d.current_value, 
              d.overdrawn, 
              d.status, 
              d.start_date, 
              d.end_date, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version, 
              d.effective_date
              )
      VALUES (s.budget_id, 
              s.budget_master_id, 
              s.user_id, 
              s.node_id, 
              s.original_value, 
              s.current_value, 
              s.overdrawn, 
              s.status, 
              s.start_date, 
              s.end_date, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version, 
              s.effective_date
              );

    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE budget_history';
  v_tbl_name := 'budget_history';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO budget_history d
       USING (SELECT bh.budget_history_id, 
                     bh.budget_id, 
                     bh.original_value_before_xaction, 
                     bh.current_value_before_xaction, 
                     bh.original_value_after_xaction, 
                     bh.current_value_after_xaction, 
                     bh.created_by, 
                     bh.date_created
                FROM budget_history_g4 bh,
                     budget b
               WHERE bh.budget_id = b.budget_id
              ) s
          ON (d.budget_history_id = s.budget_history_id)
        WHEN MATCHED THEN UPDATE 
         SET d.budget_id                     = s.budget_id,
             d.original_value_before_xaction = s.original_value_before_xaction,
             d.current_value_before_xaction  = s.current_value_before_xaction,
             d.original_value_after_xaction  = s.original_value_after_xaction,
             d.current_value_after_xaction   = s.current_value_after_xaction,
             d.created_by                    = s.created_by,
             d.date_created                  = s.date_created
        WHEN NOT MATCHED THEN INSERT
             (d.budget_history_id, 
              d.budget_id, 
              d.original_value_before_xaction, 
              d.current_value_before_xaction, 
              d.original_value_after_xaction, 
              d.current_value_after_xaction, 
              d.created_by, 
              d.date_created
              )
      VALUES (s.budget_history_id, 
              s.budget_id, 
              s.original_value_before_xaction, 
              s.current_value_before_xaction, 
              s.original_value_after_xaction, 
              s.current_value_after_xaction, 
              s.created_by, 
              s.date_created
              );

    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  --Budget tables end

  --Card related tables start
  v_stage      := 'MERGE odd_cast_category';
  v_tbl_name := 'odd_cast_category';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=
      'MERGE INTO odd_cast_category d
       USING (SELECT category_id, 
                     category_name, 
                     category_small_image_name, 
                     category_large_image_name, 
                     is_active, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM odd_cast_category_g4
               ORDER BY category_id
              ) s
          ON (d.category_id = s.category_id)
        WHEN MATCHED THEN UPDATE 
         SET d.category_name             = s.category_name,
             d.category_small_image_name = s.category_small_image_name,
             d.category_large_image_name = s.category_large_image_name,
             d.is_active                 = s.is_active,
             d.created_by                = s.created_by,
             d.date_created              = s.date_created,
             d.modified_by               = s.modified_by,
             d.date_modified             = s.date_modified,
             d.version                   = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.category_id, 
              d.category_name, 
              d.category_small_image_name, 
              d.category_large_image_name, 
              d.is_active, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.category_id, 
              s.category_name, 
              s.category_small_image_name, 
              s.category_large_image_name, 
              s.is_active, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  

--  v_stage    := 'MERGE card'; --02/28/2014
--  v_tbl_name := 'card';
--  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
--    v_dml_cnt := 0;
--
--    v_sql := 'SELECT card_id, 
--                     card_name, 
--                     small_image_name, 
--                     large_image_name, 
--                     is_active, 
--                     is_mobile, 
--                     created_by, 
--                     date_created, 
--                     modified_by, 
--                     date_modified, 
--                     version
--                FROM card_g4
--               ORDER BY card_id';
--
--    OPEN r_card FOR v_sql;
--
--    LOOP
--
--      FETCH r_card INTO vg4_card_id, 
--                        vg4_card_name, 
--                        vg4_small_image_name, 
--                        vg4_large_image_name, 
--                        vg4_is_active, 
--                        vg4_is_mobile, 
--                        vg4_created_by, 
--                        vg4_date_created, 
--                        vg4_modified_by, 
--                        vg4_date_modified, 
--                        vg4_version;
--      
--      EXIT WHEN r_card%NOTFOUND;
--      
--      BEGIN
--
--        SELECT card_id
--          INTO v_card_id
--          FROM card
--         WHERE UPPER(card_name) = UPPER(vg4_card_name);
--
--        IF v_card_id <> vg4_card_id THEN
--          
--          BEGIN
--            SELECT card_id
--              INTO v_card_id_g5
--              FROM card
--             WHERE card_id = vg4_card_id; 
--          EXCEPTION
--            WHEN NO_DATA_FOUND THEN
--              INSERT INTO card
--                         (card_id, 
--                          card_name, 
--                          small_image_name, 
--                          large_image_name, 
--                          is_active, 
--                          is_mobile, 
--                          created_by, 
--                          date_created, 
--                          modified_by, 
--                          date_modified, 
--                          version
--                          )
--                  VALUES (vg4_card_id, 
--                          vg4_card_name||'_NEW', 
--                          vg4_small_image_name, 
--                          vg4_large_image_name, 
--                          vg4_is_active, 
--                          vg4_is_mobile, 
--                          vg4_created_by, 
--                          vg4_date_created, 
--                          vg4_modified_by, 
--                          vg4_date_modified, 
--                          vg4_version
--                          );        
--          
--            UPDATE ecard
--               SET card_id = vg4_card_id
--             WHERE card_id = v_card_id; 
--            
--            UPDATE ecard_locale
--               SET card_id = vg4_card_id
--             WHERE card_id = v_card_id;
--
--            DELETE FROM card 
--             WHERE card_id = v_card_id;
--
--            UPDATE card
--               SET card_name = vg4_card_name
--             WHERE card_id = vg4_card_id; 
--            
--            v_dml_cnt := v_dml_cnt + 1;
--            
--          END;
--          
--        END IF;
--       
--      EXCEPTION
--        WHEN OTHERS THEN
--          INSERT INTO card
--                     (card_id, 
--                      card_name, 
--                      small_image_name, 
--                      large_image_name, 
--                      is_active, 
--                      is_mobile, 
--                      created_by, 
--                      date_created, 
--                      modified_by, 
--                      date_modified, 
--                      version
--                      )
--              VALUES (vg4_card_id, 
--                      vg4_card_name, 
--                      vg4_small_image_name, 
--                      vg4_large_image_name, 
--                      vg4_is_active, 
--                      vg4_is_mobile, 
--                      vg4_created_by, 
--                      vg4_date_created, 
--                      vg4_modified_by, 
--                      vg4_date_modified, 
--                      vg4_version
--                      );        
--        v_dml_cnt := v_dml_cnt + 1;
--      END;
--      
--    END LOOP;
--    show_msg_count(v_stage, v_dml_cnt);
--  ELSE
--    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
--    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
--                          v_msg_tbl_nf||v_tbl_name,
--                          NULL);
--  END IF;
--
--
--  v_stage    := 'MERGE ecard';
--  v_tbl_name := 'ecard';
--  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
--    v_dml_cnt := 0;
--
--    v_sql := 'SELECT card_id, 
--                     flash_name, 
--                     is_translatable
--                FROM ecard_g4
--               ORDER BY card_id';
--
--    OPEN r_card FOR v_sql;
--    
--    LOOP
--
--      FETCH r_card INTO vg4_card_id, 
--                        vg4_flash_name, 
--                        vg4_is_translatable;
--    
--      EXIT WHEN r_card%NOTFOUND;
--    
--      BEGIN
--        SELECT card_id
--          INTO v_card_id
--          FROM ecard
--         WHERE flash_name = vg4_flash_name;
--      EXCEPTION
--        WHEN NO_DATA_FOUND THEN
--          BEGIN
--            SELECT card_id
--              INTO v_card_id
--              FROM ecard
--             WHERE card_id = vg4_card_id; 
--          EXCEPTION
--            WHEN NO_DATA_FOUND THEN
--              v_stage := 'Insert ecard card_id: '||vg4_card_id; 
--              INSERT INTO ecard
--                         (card_id, 
--                          flash_name, 
--                          is_translatable
--                          )
--                  VALUES (vg4_card_id, 
--                          vg4_flash_name, 
--                          vg4_is_translatable
--                          );
--          
--            v_dml_cnt := v_dml_cnt + 1;
--          END;
--      END;
--    
--    END LOOP; 
--    v_stage := 'MERGE ecard';
--    show_msg_count(v_stage, v_dml_cnt);
--  ELSE
--    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
--    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
--                          v_msg_tbl_nf||v_tbl_name,
--                          NULL);
--  END IF;
--
--
--  v_stage    := 'MERGE ecard_locale';
--  v_tbl_name := 'ecard_locale';
--  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
--    v_dml_cnt := 0;
--
--    v_sql := 'SELECT card_id,
--                     locale,
--                     display_name
--                FROM ecard_locale_g4
--               ORDER BY card_id';
--
--    OPEN r_card FOR v_sql;    
--
--    LOOP
--    
--      FETCH r_card INTO vg4_card_id, 
--                        vg4_locale, 
--                        vg4_display_name;
--      
--      EXIT WHEN r_card%NOTFOUND;
--          
--      BEGIN
--        SELECT card_id
--          INTO v_card_id
--          FROM ecard_locale
--         WHERE card_id = vg4_card_id
--           AND locale  = vg4_locale;
--      EXCEPTION
--        WHEN NO_DATA_FOUND THEN
--          INSERT INTO ecard_locale 
--                      (card_id, 
--                       locale, 
--                       display_name) 
--               VALUES (vg4_card_id, 
--                       vg4_locale, 
--                       vg4_display_name);
--        v_dml_cnt := v_dml_cnt + 1;
--      END;
--    END LOOP;
--    show_msg_count(v_stage, v_dml_cnt);
--  ELSE
--    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
--    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
--                          v_msg_tbl_nf||v_tbl_name,
--                          NULL);
--  END IF;
--
--
--  v_stage      := 'MERGE odd_cast_card';
--  v_tbl_name := 'odd_cast_card';
--  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
--
--    v_sql :=
--      'MERGE INTO odd_cast_card d
--       USING (SELECT card_id, 
--                     category_id, 
--                     character_id, 
--                     character_name, 
--                     char_small_image_name, 
--                     char_large_image_name, 
--                     outfit_id, 
--                     outfit_name, 
--                     outfit_small_image_name, 
--                     outfit_large_image_name, 
--                     accessory_id, 
--                     accessory_name, 
--                     accessory_small_image_name, 
--                     accessory_large_image_name, 
--                     scene_header_script, 
--                     scene_body_script, 
--                     voice_id, 
--                     language_id, 
--                     voice_family_id
--                FROM odd_cast_card_g4
--               ORDER BY card_id
--              ) s
--          ON (d.card_id = s.card_id)
--        WHEN MATCHED THEN UPDATE 
--         SET d.category_id                = s.category_id,
--             d.character_id               = s.character_id,
--             d.character_name             = s.character_name,
--             d.char_small_image_name      = s.char_small_image_name,
--             d.char_large_image_name      = s.char_large_image_name,
--             d.outfit_id                  = s.outfit_id,
--             d.outfit_name                = s.outfit_name,
--             d.outfit_small_image_name    = s.outfit_small_image_name,
--             d.outfit_large_image_name    = s.outfit_large_image_name,
--             d.accessory_id               = s.accessory_id,
--             d.accessory_name             = s.accessory_name,
--             d.accessory_small_image_name = s.accessory_small_image_name,
--             d.accessory_large_image_name = s.accessory_large_image_name,
--             d.scene_header_script        = s.scene_header_script,
--             d.scene_body_script          = s.scene_body_script,
--             d.voice_id                   = s.voice_id,
--             d.language_id                = s.language_id,
--             d.voice_family_id            = s.voice_family_id
--        WHEN NOT MATCHED THEN INSERT
--             (d.card_id, 
--              d.category_id, 
--              d.character_id, 
--              d.character_name, 
--              d.char_small_image_name, 
--              d.char_large_image_name, 
--              d.outfit_id, 
--              d.outfit_name, 
--              d.outfit_small_image_name, 
--              d.outfit_large_image_name, 
--              d.accessory_id, 
--              d.accessory_name, 
--              d.accessory_small_image_name, 
--              d.accessory_large_image_name, 
--              d.scene_header_script, 
--              d.scene_body_script, 
--              d.voice_id, 
--              d.language_id, 
--              d.voice_family_id
--              )
--      VALUES (s.card_id, 
--              s.category_id, 
--              s.character_id, 
--              s.character_name, 
--              s.char_small_image_name, 
--              s.char_large_image_name, 
--              s.outfit_id, 
--              s.outfit_name, 
--              s.outfit_small_image_name, 
--              s.outfit_large_image_name, 
--              s.accessory_id, 
--              s.accessory_name, 
--              s.accessory_small_image_name, 
--              s.accessory_large_image_name, 
--              s.scene_header_script, 
--              s.scene_body_script, 
--              s.voice_id, 
--              s.language_id, 
--              s.voice_family_id
--              )';
--
--    EXECUTE IMMEDIATE v_sql;
--    show_msg_count(v_stage, SQL%ROWCOUNT);
--    
--  ELSE
--    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
--    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
--                          v_msg_tbl_nf||v_tbl_name,
--                          NULL);
--  END IF;
--  --Card tables end

  --Quiz related tables start

  v_stage    := 'MERGE quiz';
  v_tbl_name := 'quiz';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO quiz d
       USING (SELECT quiz_id, 
                     name, 
                     description, 
                     number_asked, 
                     passing_score, 
                     quiz_type, 
                     status_type, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM quiz_g4
               ORDER BY quiz_id
              ) s
          ON (d.quiz_id = s.quiz_id)
        WHEN MATCHED THEN UPDATE 
         SET d.name          = s.name,
             d.description   = s.description,
             d.number_asked  = s.number_asked,
             d.passing_score = s.passing_score,
             d.quiz_type     = s.quiz_type,
             d.status_type   = s.status_type,
             d.created_by    = s.created_by,
             d.date_created  = s.date_created,
             d.modified_by   = s.modified_by,
             d.date_modified = s.date_modified,
             d.version       = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.quiz_id, 
              d.name, 
              d.description, 
              d.number_asked, 
              d.passing_score, 
              d.quiz_type, 
              d.status_type, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.quiz_id, 
              s.name, 
              s.description, 
              s.number_asked, 
              s.passing_score, 
              s.quiz_type, 
              s.status_type, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              )';
                  
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE quiz_question';
  v_tbl_name := 'quiz_question';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=
      'MERGE INTO quiz_question d
       USING (SELECT quiz_question_id, 
                     quiz_id, 
                     status_type, 
                     is_required, 
                     cm_asset_name, 
                     sequence_num, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM quiz_question_g4
               ORDER BY quiz_question_id
              ) s
          ON (d.quiz_question_id = s.quiz_question_id)
        WHEN MATCHED THEN UPDATE 
         SET d.quiz_id          = s.quiz_id,
             d.status_type      = s.status_type,
             d.is_required      = s.is_required,
             d.cm_asset_name    = s.cm_asset_name,
             d.sequence_num     = s.sequence_num,
             d.created_by       = s.created_by,
             d.date_created     = s.date_created,
             d.modified_by      = s.modified_by,
             d.date_modified    = s.date_modified,
             d.version          = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.quiz_question_id, 
                d.quiz_id, 
                d.status_type, 
                d.is_required, 
                d.cm_asset_name, 
                d.sequence_num, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.quiz_question_id, 
                s.quiz_id, 
                s.status_type, 
                s.is_required, 
                s.cm_asset_name, 
                s.sequence_num, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  

  v_stage      := 'MERGE quiz_question_answer';
  v_tbl_name := 'quiz_question_answer';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=
      'MERGE INTO quiz_question_answer d
       USING (SELECT quiz_question_answer_id, 
                     quiz_question_id, 
                     is_correct, 
                     cm_asset_code, 
                     answer_cm_key, 
                     explanation_cm_key, 
                     sequence_num, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM quiz_question_answer_g4
               ORDER BY quiz_question_answer_id
              ) s
          ON (d.quiz_question_answer_id = s.quiz_question_answer_id)
        WHEN MATCHED THEN UPDATE 
         SET d.quiz_question_id        = s.quiz_question_id,
             d.is_correct              = s.is_correct,
             d.cm_asset_code           = s.cm_asset_code,
             d.answer_cm_key           = s.answer_cm_key,
             d.explanation_cm_key      = s.explanation_cm_key,
             d.sequence_num            = s.sequence_num,
             d.created_by              = s.created_by,
             d.date_created            = s.date_created,
             d.modified_by             = s.modified_by,
             d.date_modified           = s.date_modified,
             d.version                 = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.quiz_question_answer_id, 
                d.quiz_question_id, 
                d.is_correct, 
                d.cm_asset_code, 
                d.answer_cm_key, 
                d.explanation_cm_key, 
                d.sequence_num, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.quiz_question_answer_id, 
                s.quiz_question_id, 
                s.is_correct, 
                s.cm_asset_code, 
                s.answer_cm_key, 
                s.explanation_cm_key, 
                s.sequence_num, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;  
  --Quiz related tables end

  v_stage      := 'MERGE calculator';
  v_tbl_name := 'calculator';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO calculator d
       USING (SELECT calculator_id, 
                     name, 
                     description, 
                     status_type, 
                     weighted_score, 
                     display_weights, 
                     weight_cm_asset_name, 
                     display_scores, 
                     score_cm_asset_name, 
                     award_type, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM calculator_g4
               ORDER BY calculator_id
              ) s
          ON (d.calculator_id = s.calculator_id)
        WHEN MATCHED THEN UPDATE 
         SET d.name                 = s.name, 
             d.description          = s.description, 
             d.status_type          = s.status_type, 
             d.weighted_score       = s.weighted_score, 
             d.display_weights      = s.display_weights, 
             d.weight_cm_asset_name = s.weight_cm_asset_name, 
             d.display_scores       = s.display_scores, 
             d.score_cm_asset_name  = s.score_cm_asset_name, 
             d.award_type           = s.award_type, 
             d.created_by           = s.created_by,      
             d.date_created         = s.date_created, 
             d.modified_by          = s.modified_by, 
             d.date_modified        = s.date_modified, 
             d.version              = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.calculator_id, 
              d.name, 
              d.description, 
              d.status_type, 
              d.weighted_score, 
              d.display_weights, 
              d.weight_cm_asset_name, 
              d.display_scores, 
              d.score_cm_asset_name, 
              d.award_type, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.calculator_id, 
              s.name, 
              s.description, 
              s.status_type, 
              s.weighted_score, 
              s.display_weights, 
              s.weight_cm_asset_name, 
              s.display_scores, 
              s.score_cm_asset_name, 
              s.award_type, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  --Forms related tables start
  v_stage      := 'MERGE claim_form';      
  v_tbl_name := 'claim_form';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
    v_dml_cnt := 0;
    FOR rec_cf IN (SELECT claim_form_id, 
                          cm_asset_code, 
                          form_name, 
                          description, 
                          module_code, 
                          status_code, 
                          created_by, 
                          date_created, 
                          modified_by, 
                          date_modified, 
                          version
                     FROM claim_form_g4
                     WHERE cm_asset_code not in ('claim_form_data.claimform.100952','claim_form_data.claimform.100957','claim_form_data.claimform.100970')
                    ORDER BY claim_form_id 
                   )
    LOOP
      BEGIN
        SELECT claim_form_id
          INTO v_claim_form_id
          FROM claim_form
         WHERE UPPER(form_name) = UPPER(rec_cf.form_name);
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO claim_form
                     (claim_form_id, 
                      cm_asset_code, 
                      form_name, 
                      description, 
                      module_code, 
                      status_code, 
                      created_by, 
                      date_created, 
                      modified_by, 
                      date_modified, 
                      version
                      )
              VALUES (rec_cf.claim_form_id, 
                      rec_cf.cm_asset_code, 
                      rec_cf.form_name, 
                      rec_cf.description, 
                      rec_cf.module_code, 
                      rec_cf.status_code, 
                      rec_cf.created_by, 
                      rec_cf.date_created, 
                      rec_cf.modified_by, 
                      rec_cf.date_modified, 
                      rec_cf.version
                      );
        v_dml_cnt := v_dml_cnt + 1;
      END;
   
    END LOOP; 
    show_msg_count(v_stage, v_dml_cnt);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE claim_form_step';
  v_tbl_name := 'claim_form_step';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
    v_dml_cnt := 0;
    FOR rec_cfs IN (SELECT cg4.claim_form_step_id, 
                           cg4.claim_form_id, 
                           cg4.cm_key_fragment, 
                           cg4.sequence_num, 
                           cg4.is_sales_required, 
                           cg4.created_by, 
                           cg4.date_created, 
                           cg4.modified_by, 
                           cg4.date_modified, 
                           cg4.version, 
                           cg4.type
                      FROM claim_form_step_g4 cg4,
                      claim_form cf
                      WHERE cg4.claim_form_id=cf.claim_form_id
                     ORDER BY claim_form_step_id
                    )
    LOOP
   
      BEGIN
        SELECT claim_form_step_id
          INTO v_claim_form_step_id
          FROM claim_form_step
         WHERE claim_form_step_id = rec_cfs.claim_form_step_id
           AND claim_form_id      = rec_cfs.claim_form_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO claim_form_step
                      ( claim_form_step_id, 
                        claim_form_id, 
                        cm_key_fragment, 
                        sequence_num, 
                        is_sales_required, 
                        created_by, 
                        date_created, 
                        modified_by, 
                        date_modified, 
                        version, 
                        type
                       )
               VALUES ( rec_cfs.claim_form_step_id, 
                        rec_cfs.claim_form_id, 
                        rec_cfs.cm_key_fragment, 
                        rec_cfs.sequence_num, 
                        rec_cfs.is_sales_required, 
                        rec_cfs.created_by, 
                        rec_cfs.date_created, 
                        rec_cfs.modified_by, 
                        rec_cfs.date_modified, 
                        rec_cfs.version, 
                        rec_cfs.type
                      );   
        v_dml_cnt := v_dml_cnt + 1;
      END;
   
    END LOOP;
    show_msg_count(v_stage, v_dml_cnt);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  

  v_stage      := 'MERGE claim_form_step_email';
  v_tbl_name := 'claim_form_step_email';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
    v_dml_cnt := 0;
    FOR rec_cfse IN (SELECT claim_form_step_email_id, 
                            claim_form_step_id, 
                            notification_type
                       FROM claim_form_step_email_g4
                      ORDER BY claim_form_step_email_id
                     )
    LOOP
      BEGIN
        SELECT claim_form_step_email_id
          INTO v_claim_form_step_email_id
          FROM claim_form_step_email
         WHERE claim_form_step_id = rec_cfse.claim_form_step_id 
           AND notification_type  = rec_cfse.notification_type;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO claim_form_step_email
                      (claim_form_step_email_id, 
                       claim_form_step_id, 
                       notification_type
                       )
               VALUES ( rec_cfse.claim_form_step_email_id, 
                        rec_cfse.claim_form_step_id, 
                        rec_cfse.notification_type
                       );
        v_dml_cnt := v_dml_cnt + 1;
      END;
    
    END LOOP;                                                                
    show_msg_count(v_stage, v_dml_cnt);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;

    
  v_stage      := 'MERGE claim_form_step_element';      
  v_tbl_name := 'claim_form_step_element';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO claim_form_step_element d
       USING (SELECT claim_form_step_element_id, 
                     claim_form_step_id, 
                     cm_key_fragment, 
                     description, 
                     is_required, 
                     claim_form_element_type_code, 
                     number_of_decimals, 
                     nbr_input_format_type_code, 
                     masked_on_entry, 
                     customer_information_block_id, 
                     uniqueness, 
                     should_encrypt, 
                     max_size_text_field, 
                     text_input_format_type_code, 
                     link_url, 
                     date_start, 
                     date_end, 
                     sequence_num, 
                     selection_pick_list_name, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM claim_form_step_element_g4
               ORDER BY claim_form_step_element_id
              ) s
          ON (d.claim_form_step_element_id = s.claim_form_step_element_id)
        WHEN NOT MATCHED THEN INSERT
             (d.claim_form_step_element_id, 
              d.claim_form_step_id, 
              d.cm_key_fragment, 
              d.description, 
              d.is_required, 
              d.claim_form_element_type_code, 
              d.number_of_decimals, 
              d.nbr_input_format_type_code, 
              d.masked_on_entry, 
              d.customer_information_block_id, 
              d.uniqueness, 
              d.should_encrypt, 
              d.max_size_text_field, 
              d.text_input_format_type_code, 
              d.link_url, 
              d.date_start, 
              d.date_end, 
              d.sequence_num, 
              d.selection_pick_list_name, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.claim_form_step_element_id, 
              s.claim_form_step_id, 
              s.cm_key_fragment, 
              s.description, 
              s.is_required, 
              s.claim_form_element_type_code, 
              s.number_of_decimals, 
              s.nbr_input_format_type_code, 
              s.masked_on_entry, 
              s.customer_information_block_id, 
              s.uniqueness, 
              s.should_encrypt, 
              s.max_size_text_field, 
              s.text_input_format_type_code, 
              s.link_url, 
              s.date_start, 
              s.date_end, 
              s.sequence_num, 
              s.selection_pick_list_name, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;  
  --Forms related tables end

  --Product related tables starts
  v_stage      := 'MERGE product_category';
  v_tbl_name := 'product_category';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=   
      'MERGE INTO product_category d
       USING (SELECT product_category_id, 
                     product_category_name, 
                     --is_active, 
                     description, 
                     parent_category_id, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM product_category_g4
               ORDER BY product_category_id
              ) s
          ON (d.product_category_id = s.product_category_id)
        WHEN MATCHED THEN UPDATE 
         SET d.product_category_name = s.product_category_name,
             --d.is_active             = s.is_active,
             d.description           = s.description,
             d.parent_category_id    = s.parent_category_id,
             d.created_by            = s.created_by,
             d.date_created          = s.date_created,
             d.modified_by           = s.modified_by,
             d.date_modified         = s.date_modified,
             d.version               = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.product_category_id, 
                d.product_category_name, 
                --d.is_active, 
                d.description, 
                d.parent_category_id, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.product_category_id, 
                s.product_category_name, 
                --s.is_active, 
                s.description, 
                s.parent_category_id, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  
  v_stage      := 'MERGE product';
  v_tbl_name := 'product';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=  
     'MERGE INTO product d
       USING (SELECT product_id, 
                     product_name, 
                     description, 
                     category_id, 
                     sku_code, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM product_g4
               ORDER BY product_id
              ) s
          ON (d.product_id = s.product_id)
        WHEN MATCHED THEN UPDATE 
         SET d.product_name  = s.product_name,
             d.description   = s.description,
             d.category_id   = s.category_id,
             d.sku_code      = s.sku_code,
             d.created_by    = s.created_by,
             d.date_created  = s.date_created,
             d.modified_by   = s.modified_by,
             d.date_modified = s.date_modified,
             d.version       = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.product_id, 
                d.product_name, 
                d.description, 
                d.category_id, 
                d.sku_code, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.product_id, 
                s.product_name, 
                s.description, 
                s.category_id, 
                s.sku_code, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;

  v_stage      := 'MERGE product_characteristic';
  v_tbl_name := 'product_characteristic';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=
      'MERGE INTO product_characteristic d
       USING (SELECT product_characteristic_id, 
                     product_id, 
                     characteristic_id, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM product_characteristic_g4
               ORDER BY product_characteristic_id
              ) s
          ON (d.product_characteristic_id = s.product_characteristic_id)
        WHEN MATCHED THEN UPDATE 
         SET d.product_id                = s.product_id,
             d.characteristic_id         = s.characteristic_id,
             d.created_by                = s.created_by,
             d.date_created              = s.date_created,
             d.modified_by               = s.modified_by,
             d.date_modified             = s.date_modified,
             d.version                   = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.product_characteristic_id, 
                d.product_id, 
                d.characteristic_id, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.product_characteristic_id, 
                s.product_id, 
                s.characteristic_id, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
 --Product related tables starts  

--
--  v_stage      := 'MERGE process';
--  v_tbl_name := 'process';
--  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
--
--       MERGE INTO process d
--       USING (SELECT process_id, 
--                     process_name, 
--                     process_last_executed_date, 
--                     process_bean_name, 
--                     process_status_type, 
--                     description, 
--                     created_by, 
--                     date_created, 
--                     modified_by, 
--                     date_modified, 
--                     version
--                FROM process_g4
--               ORDER BY process_id 
--              ) s
--          ON (d.process_id = s.process_id)
--        WHEN MATCHED THEN UPDATE 
--         SET d.process_name               = s.process_name,
--             d.process_last_executed_date = s.process_last_executed_date,
--             d.process_bean_name          = s.process_bean_name,
--             d.process_status_type        = s.process_status_type,
--             d.description                = s.description,
--             d.created_by                 = s.created_by,
--             d.date_created               = s.date_created,
--             d.modified_by                = s.modified_by,
--             d.date_modified              = s.date_modified,
--             d.version                    = s.version
--        WHEN NOT MATCHED THEN INSERT
--             (d.process_id, 
--              d.process_name, 
--              d.process_last_executed_date, 
--              d.process_bean_name, 
--              d.process_status_type, 
--              d.description, 
--              d.created_by, 
--              d.date_created, 
--              d.modified_by, 
--              d.date_modified, 
--              d.version
--              )
--      VALUES (s.process_id, 
--              s.process_name, 
--              s.process_last_executed_date, 
--              s.process_bean_name, 
--              s.process_status_type, 
--              s.description, 
--              s.created_by, 
--              s.date_created, 
--              s.modified_by, 
--              s.date_modified, 
--              s.version
--              );
--    show_msg_count(v_stage, SQL%ROWCOUNT);
--  ELSE
--    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
--    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
--                          v_msg_tbl_nf||v_tbl_name,
--                          NULL);
--  END IF; 
--  
--  
--  v_stage      := 'MERGE process_role';
--  v_tbl_name := 'process_role';
--  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
--
--       MERGE INTO process_role d
--       USING (SELECT pr4.process_role_id, 
--                     pg.process_id, 
--                     r.role_id, 
--                     pr4.process_role_type,                      
--                     pr4.created_by, 
--                     pr4.date_created, 
--                     pr4.modified_by, 
--                     pr4.date_modified, 
--                     pr4.version
--                FROM process_role_g4 pr4,
--                process p,process_g4 pg,role r,role_g4 r4
--                WHERE p.process_name = pg.process_name
--                AND pg.process_id = pr4.process_id
--                AND pr4.role_id= r4.role_id
--                AND r4.name = r.name
--               ORDER BY process_id 
--              ) s
--          ON (d.process_role_id = s.process_role_id)
--        WHEN MATCHED THEN UPDATE 
--         SET d.process_id = s.process_id,
--             d.role_id          = s.role_id,
--             d.process_role_type        = s.process_role_type,             
--             d.created_by                 = s.created_by,
--             d.date_created               = s.date_created,
--             d.modified_by                = s.modified_by,
--             d.date_modified              = s.date_modified,
--             d.version                    = s.version
--        WHEN NOT MATCHED THEN INSERT
--             (d.process_role_id,
--               d.process_id, 
--              d.role_id, 
--              d.process_role_type,                      
--              d.created_by, 
--              d.date_created, 
--              d.modified_by, 
--              d.date_modified, 
--              d.version
--              )
--      VALUES (s.process_role_id,
--               s.process_id, 
--              s.role_id, 
--              s.process_role_type,                      
--              s.created_by, 
--              s.date_created, 
--              s.modified_by, 
--              s.date_modified, 
--              s.version
--              );
--              
--    show_msg_count(v_stage, SQL%ROWCOUNT);
--  ELSE
--    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
--    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
--                          v_msg_tbl_nf||v_tbl_name,
--                          NULL);
--  END IF; 


  --Promotion realted tables end
  v_stage    := 'MERGE promotion';
  v_tbl_name := 'promotion';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
    v_dml_cnt := 0;
    FOR rec_p IN (SELECT promotion_id, 
                         --promo_name_asset_code, 
                         promotion_name, 
                         promotion_type, 
                         promotion_status, 
                         certificate, 
                         is_deleted, 
                         claim_form_id, 
                         promotion_start_date, 
                         promotion_end_date, 
                         has_web_rules, 
                         cm_asset_code, 
                         web_rules_cm_key, 
                         web_rules_start_date, 
                         web_rules_end_date, 
                         approval_start_date, 
                         approval_end_date, 
                         approval_type, 
                         approver_type, 
                         approval_auto_delay_days, 
                         approval_cond_claim_cnt, 
                         approval_cond_amt_field_id, 
                         approval_cond_amt_operator, 
                         approval_cond_amt, 
                         approver_node_id, 
                         approval_hierarchy_id, 
                         approval_node_type_id, 
                         approval_node_levels, 
                         award_budget_master_id, 
                         is_taxable, 
                         is_online_entry, 
                         is_fileload_entry, 
                         primary_audience_type, 
                         secondary_audience_type, 
                         web_rules_audience_type, 
                         partner_audience_type, 
                         DECODE(award_type,'awardperqs','points',award_type) award_type, 
                         sweeps_active, 
                         sweeps_claim_eligibility_type, 
                         sweeps_winner_eligibility_type, 
                         sweeps_multiple_award_type, 
                         sweeps_primary_basis_type, 
                         sweeps_primary_winners, 
                         sweeps_primary_award_amount, 
                         sweeps_primary_award_level, 
                         sweeps_secondary_basis_type, 
                         sweeps_secondary_winners, 
                         sweeps_secondary_award_amount, 
                         sweeps_secondary_award_level, 
                         number_attempts_given, 
                         playing_attempts_method, 
                         attempts_expiration_date, 
                         score_by, 
                         calculator_id, 
                         allow_self_enroll, 
                         enroll_program_code, 
                         created_by, 
                         date_created, 
                         modified_by, 
                         date_modified, 
                         version, 
                         purpose 
                         --low_payment_threshold, 
                    FROM promotion_g4 WHERE promotion_type NOT IN ('survey','wellness','goalquest','challengepoint')
                   ORDER BY promotion_id
                   )
    LOOP
      BEGIN
        SELECT promotion_id
          INTO v_promotion_id
          FROM promotion
         WHERE promotion_id = rec_p.promotion_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
        
          prc_promo_name_insert_cms (rec_p.promotion_name, vo_name_asset_code_id);
          
          INSERT INTO promotion
                       (promotion_id, 
                        promo_name_asset_code, 
                        promotion_name, 
                        promotion_type, 
                        promotion_status, 
                        certificate, 
                        is_deleted, 
                        claim_form_id, 
                        promotion_start_date, 
                        promotion_end_date, 
                        has_web_rules, 
                        cm_asset_code, 
                        web_rules_cm_key, 
                        web_rules_start_date, 
                        web_rules_end_date, 
                        approval_start_date, 
                        approval_end_date, 
                        approval_type, 
                        approver_type, 
                        approval_auto_delay_days, 
                        approval_cond_claim_cnt, 
                        approval_cond_amt_field_id, 
                        approval_cond_amt_operator, 
                        approval_cond_amt, 
                        approver_node_id, 
                        approval_hierarchy_id, 
                        approval_node_type_id, 
                        approval_node_levels, 
                        award_budget_master_id, 
                        is_taxable, 
                        is_online_entry, 
                        is_fileload_entry, 
                        primary_audience_type, 
                        secondary_audience_type, 
                        web_rules_audience_type, 
                        partner_audience_type, 
                        award_type, 
                        sweeps_active, 
                        sweeps_claim_eligibility_type, 
                        sweeps_winner_eligibility_type, 
                        sweeps_multiple_award_type, 
                        sweeps_primary_basis_type, 
                        sweeps_primary_winners, 
                        sweeps_primary_award_amount, 
                        sweeps_primary_award_level, 
                        sweeps_secondary_basis_type, 
                        sweeps_secondary_winners, 
                        sweeps_secondary_award_amount, 
                        sweeps_secondary_award_level, 
                        --number_attempts_given,        --02/04/2014
                        --playing_attempts_method,      --02/04/2014
                        --attempts_expiration_date,     --02/04/2014
                        score_by, 
                        calculator_id, 
                        allow_self_enroll, 
                        enroll_program_code, 
                        created_by, 
                        date_created, 
                        modified_by, 
                        date_modified, 
                        version, 
                        purpose 
                        --low_payment_threshold 
                        )
                VALUES (rec_p.promotion_id, 
                        'promotion_name.'||vo_name_asset_code_id,
                        rec_p.promotion_name, 
                        rec_p.promotion_type, 
                        rec_p.promotion_status, 
                        rec_p.certificate, 
                        rec_p.is_deleted, 
                        rec_p.claim_form_id, 
                        rec_p.promotion_start_date, 
                        rec_p.promotion_end_date, 
                        rec_p.has_web_rules, 
                        rec_p.cm_asset_code, 
                        rec_p.web_rules_cm_key, 
                        rec_p.web_rules_start_date, 
                        rec_p.web_rules_end_date, 
                        rec_p.approval_start_date, 
                        rec_p.approval_end_date, 
                        rec_p.approval_type, 
                        rec_p.approver_type, 
                        rec_p.approval_auto_delay_days, 
                        rec_p.approval_cond_claim_cnt, 
                        rec_p.approval_cond_amt_field_id, 
                        rec_p.approval_cond_amt_operator, 
                        rec_p.approval_cond_amt, 
                        rec_p.approver_node_id, 
                        rec_p.approval_hierarchy_id, 
                        rec_p.approval_node_type_id, 
                        rec_p.approval_node_levels, 
                        rec_p.award_budget_master_id, 
                        rec_p.is_taxable, 
                        rec_p.is_online_entry, 
                        rec_p.is_fileload_entry, 
                        rec_p.primary_audience_type, 
                        rec_p.secondary_audience_type, 
                        rec_p.web_rules_audience_type, 
                        rec_p.partner_audience_type, 
                        rec_p.award_type, 
                        rec_p.sweeps_active, 
                        rec_p.sweeps_claim_eligibility_type, 
                        rec_p.sweeps_winner_eligibility_type, 
                        rec_p.sweeps_multiple_award_type, 
                        rec_p.sweeps_primary_basis_type, 
                        rec_p.sweeps_primary_winners, 
                        rec_p.sweeps_primary_award_amount, 
                        rec_p.sweeps_primary_award_level, 
                        rec_p.sweeps_secondary_basis_type, 
                        rec_p.sweeps_secondary_winners, 
                        rec_p.sweeps_secondary_award_amount, 
                        rec_p.sweeps_secondary_award_level, 
                        --rec_p.number_attempts_given,      --02/04/2014
                        --rec_p.playing_attempts_method,    --02/04/2014
                        --rec_p.attempts_expiration_date,   --02/04/2014 
                        rec_p.score_by, 
                        rec_p.calculator_id, 
                        rec_p.allow_self_enroll, 
                        rec_p.enroll_program_code, 
                        rec_p.created_by, 
                        rec_p.date_created, 
                        rec_p.modified_by, 
                        rec_p.date_modified, 
                        rec_p.version, 
                        rec_p.purpose 
                        --rec_p.low_payment_threshold 
                      );        
        v_dml_cnt := v_dml_cnt + 1;
      END;
    END LOOP;
    
    show_msg_count(v_stage, v_dml_cnt);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;

  v_stage      := 'MERGE promo_approval_option';
  v_tbl_name := 'promo_approval_option';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO promo_approval_option d
       USING (SELECT promo_approval_option_id, 
                     promotion_id, 
                     approval_option, 
                     created_by, 
                     date_created, 
                     version
                FROM promo_approval_option_g4
                WHERE promotion_id NOT IN ( select promotion_id from promotion_g4 where promotion_type IN ('survey','wellness','goalquest','challengepoint'))
               ORDER BY promo_approval_option_id
              ) s
          ON (d.promo_approval_option_id = s.promo_approval_option_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id             = s.promotion_id,
             d.approval_option          = s.approval_option,
             d.created_by               = s.created_by,
             d.date_created             = s.date_created,
             d.version                  = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.promo_approval_option_id, 
                d.promotion_id, 
                d.approval_option, 
                d.created_by, 
                d.date_created, 
                d.version
              )
      VALUES (  s.promo_approval_option_id, 
                s.promotion_id, 
                s.approval_option, 
                s.created_by, 
                s.date_created, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;

  v_stage      := 'MERGE promo_approval_participant';
  v_tbl_name := 'promo_approval_participant';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO promo_approval_participant d
       USING (SELECT promo_approval_participant_id, 
                     promotion_id, 
                     user_id, 
                     sequence_num, 
                     promo_participant_type, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM promo_approval_participant_g4
                WHERE promotion_id NOT IN ( select promotion_id from promotion_g4 where promotion_type IN ('survey','wellness','goalquest','challengepoint'))
               ORDER BY promo_approval_participant_id
              ) s
          ON (d.promo_approval_participant_id = s.promo_approval_participant_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id                  = s.promotion_id,
             d.user_id                       = s.user_id,
             d.sequence_num                  = s.sequence_num,
             d.promo_participant_type        = s.promo_participant_type,
             d.created_by                    = s.created_by,
             d.date_created                  = s.date_created,
             d.modified_by                   = s.modified_by,
             d.date_modified                 = s.date_modified,
             d.version                       = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.promo_approval_participant_id, 
                d.promotion_id, 
                d.user_id, 
                d.sequence_num, 
                d.promo_participant_type, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.promo_approval_participant_id, 
                s.promotion_id, 
                s.user_id, 
                s.sequence_num, 
                s.promo_participant_type, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  

  v_stage      := 'MERGE promo_audience';
  v_tbl_name := 'promo_audience';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO promo_audience d
       USING (SELECT promo_audience_id, 
                     promotion_id, 
                     promo_audience_type, 
                     audience_id, 
                     created_by, 
                     date_created, 
                     version
                FROM promo_audience_g4
                WHERE promotion_id NOT IN ( select promotion_id from promotion_g4 where promotion_type IN ('survey','wellness','goalquest','challengepoint'))
               ORDER BY promo_audience_id 
              ) s
          ON (d.promo_audience_id = s.promo_audience_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id        = s.promotion_id,
             d.promo_audience_type = s.promo_audience_type,
             d.audience_id         = s.audience_id,
             d.created_by          = s.created_by,
             d.date_created        = s.date_created,
             d.version             = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.promo_audience_id, 
                d.promotion_id, 
                d.promo_audience_type, 
                d.audience_id, 
                d.created_by, 
                d.date_created, 
                d.version
              )
      VALUES (  s.promo_audience_id, 
                s.promotion_id, 
                s.promo_audience_type, 
                s.audience_id, 
                s.created_by, 
                s.date_created, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;    


  v_stage      := 'MERGE promo_behavior';
  v_tbl_name := 'promo_behavior';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=
      'MERGE INTO promo_behavior d
       USING (SELECT promotion_id, 
                     behavior_type, 
                     created_by, 
                     date_created
                FROM promo_behavior_g4
                WHERE promotion_id NOT IN ( select promotion_id from promotion_g4 where promotion_type IN ('''||'survey'||''','''||'wellness'||''','''||'goalquest'||''','''||'challengepoint'||'''))
               ORDER BY promotion_id 
              ) s
          ON (d.promotion_id  = s.promotion_id AND
              d.behavior_type = s.behavior_type)
        WHEN MATCHED THEN UPDATE 
         SET d.created_by    = s.created_by,
             d.date_created  = s.date_created 
        WHEN NOT MATCHED THEN INSERT
             (  d.promotion_id, 
                d.behavior_type, 
                d.created_by, 
                d.date_created
              )
      VALUES (  s.promotion_id, 
                s.behavior_type, 
                s.created_by, 
                s.date_created
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;

--
--  v_stage      := 'MERGE promo_card';  --02/28/2014
--  v_tbl_name := 'promo_card';
--  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
--
--    v_sql :=
--      'MERGE INTO promo_card d
--       USING (SELECT promo_card_id, 
--                     promotion_id, 
--                     card_type, 
--                     card_id, 
--                     category_id, 
--                     created_by, 
--                     date_created
--                FROM promo_card_g4
--                WHERE promotion_id NOT IN ( select promotion_id from promotion_g4 where promotion_type IN ('''||'survey'||''','''||'wellness'||''','''||'goalquest'||''','''||'challengepoint'||'''))
--               ORDER BY promo_card_id
--              ) s
--          ON (d.promo_card_id = s.promo_card_id)
--        WHEN MATCHED THEN UPDATE 
--         SET d.promotion_id  = s.promotion_id,
--             d.card_type     = s.card_type,
--             d.card_id       = s.card_id,
--             d.category_id   = s.category_id,
--             d.created_by    = s.created_by,
--             d.date_created  = s.date_created
--        WHEN NOT MATCHED THEN INSERT
--             (  d.promo_card_id, 
--                d.promotion_id, 
--                d.card_type, 
--                d.card_id, 
--                d.category_id, 
--                d.created_by, 
--                d.date_created
--              )
--      VALUES (  s.promo_card_id, 
--                s.promotion_id, 
--                s.card_type, 
--                s.card_id, 
--                s.category_id, 
--                s.created_by, 
--                s.date_created
--              )';
--    
--    EXECUTE IMMEDIATE v_sql;
--    show_msg_count(v_stage, SQL%ROWCOUNT);
--  ELSE
--    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
--    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
--                          v_msg_tbl_nf||v_tbl_name,
--                          NULL);
--  END IF;
--

  v_stage      := 'MERGE promo_certificate';
  v_tbl_name := 'promo_certificate';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO promo_certificate d
       USING (SELECT promo_certificate_id, 
                     promotion_id, 
                     certificate_id, 
                     created_by, 
                     date_created
                FROM promo_certificate_g4
                WHERE promotion_id NOT IN ( select promotion_id from promotion_g4 where promotion_type IN ('''||'survey'||''','''||'wellness'||''','''||'goalquest'||''','''||'challengepoint'||'''))
               ORDER BY promo_certificate_id
              ) s
          ON (d.promo_certificate_id = s.promo_certificate_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id         = s.promotion_id,
             d.certificate_id       = s.certificate_id,
             d.created_by           = s.created_by,
             d.date_created         = s.date_created
        WHEN NOT MATCHED THEN INSERT
             (  d.promo_certificate_id, 
                d.promotion_id, 
                d.certificate_id, 
                d.created_by, 
                d.date_created
              )
      VALUES (  s.promo_certificate_id, 
                s.promotion_id, 
                s.certificate_id, 
                s.created_by, 
                s.date_created
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE promo_cfse_validation';
  v_tbl_name := 'promo_cfse_validation';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO promo_cfse_validation d
       USING (SELECT promo_cfse_validation_id, 
                     promotion_id, 
                     claim_form_step_element_id, 
                     validation_type, 
                     min_value, 
                     max_value, 
                     max_length, 
                     start_date, 
                     end_date, 
                     starts_with, 
                     not_start_with, 
                     ends_with, 
                     not_end_with, 
                     contains, 
                     not_contain, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM promo_cfse_validation_g4
                WHERE promotion_id NOT IN ( select promotion_id from promotion_g4 where promotion_type IN ('survey','wellness','goalquest','challengepoint'))
               ORDER BY promo_cfse_validation_id
              ) s
          ON (d.promo_cfse_validation_id = s.promo_cfse_validation_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id               = s.promotion_id,
             d.claim_form_step_element_id = s.claim_form_step_element_id,
             d.validation_type            = s.validation_type,
             d.min_value                  = s.min_value,
             d.max_value                  = s.max_value,
             d.max_length                 = s.max_length,
             d.start_date                 = s.start_date,
             d.end_date                   = s.end_date,
             d.starts_with                = s.starts_with,
             d.not_start_with             = s.not_start_with,
             d.ends_with                  = s.ends_with,
             d.not_end_with               = s.not_end_with,
             d.contains                   = s.contains,
             d.not_contain                = s.not_contain,
             d.created_by                 = s.created_by,
             d.date_created               = s.date_created,
             d.modified_by                = s.modified_by,
             d.date_modified              = s.date_modified,
             d.version                    = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.promo_cfse_validation_id, 
                d.promotion_id, 
                d.claim_form_step_element_id, 
                d.validation_type, 
                d.min_value, 
                d.max_value, 
                d.max_length, 
                d.start_date, 
                d.end_date, 
                d.starts_with, 
                d.not_start_with, 
                d.ends_with, 
                d.not_end_with, 
                d.contains, 
                d.not_contain, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.promo_cfse_validation_id, 
                s.promotion_id, 
                s.claim_form_step_element_id, 
                s.validation_type, 
                s.min_value, 
                s.max_value, 
                s.max_length, 
                s.start_date, 
                s.end_date, 
                s.starts_with, 
                s.not_start_with, 
                s.ends_with, 
                s.not_end_with, 
                s.contains, 
                s.not_contain, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE promo_merch_country';
  v_tbl_name := 'promo_merch_country';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO promo_merch_country d
       USING (SELECT promo_merch_country_id, 
                     promotion_id, 
                     country_id, 
                     program_id, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM promo_merch_country_g4
                WHERE promotion_id NOT IN ( select promotion_id from promotion_g4 where promotion_type IN ('survey','wellness','goalquest','challengepoint'))
               ORDER BY promo_merch_country_id
              ) s
          ON (d.promo_merch_country_id = s.promo_merch_country_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id           = s.promotion_id,
             d.country_id             = s.country_id,
             d.program_id             = s.program_id,
             d.created_by             = s.created_by,
             d.date_created           = s.date_created,
             d.modified_by            = s.modified_by,
             d.date_modified          = s.date_modified,
             d.version                = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.promo_merch_country_id, 
                d.promotion_id, 
                d.country_id, 
                d.program_id, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.promo_merch_country_id, 
                s.promotion_id, 
                s.country_id, 
                s.program_id, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE promo_merch_program_level';      --cm_asset_key
  v_tbl_name := 'promo_merch_program_level';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO promo_merch_program_level d
       USING (SELECT promo_merch_program_level_id, 
                     promo_merch_country_id, 
                     ordinal_position, 
                     cm_asset_key, 
                     om_level_name, 
                     program_id, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM promo_merch_program_level_g4
               ORDER BY promo_merch_program_level_id
              ) s
          ON (d.promo_merch_program_level_id = s.promo_merch_program_level_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promo_merch_country_id       = s.promo_merch_country_id,
             d.ordinal_position             = s.ordinal_position,
             d.cm_asset_key                 = s.cm_asset_key,
             d.om_level_name                = s.om_level_name,
             d.program_id                   = s.program_id,
             d.created_by                   = s.created_by,
             d.date_created                 = s.date_created,
             d.modified_by                  = s.modified_by,
             d.date_modified                = s.date_modified,
             d.version                      = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.promo_merch_program_level_id, 
                d.promo_merch_country_id, 
                d.ordinal_position, 
                d.cm_asset_key, 
                d.om_level_name, 
                d.program_id, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.promo_merch_program_level_id, 
                s.promo_merch_country_id, 
                s.ordinal_position, 
                s.cm_asset_key, 
                s.om_level_name, 
                s.program_id, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;

  v_stage      := 'MERGE promo_product_claim';
  v_tbl_name := 'promo_product_claim';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO promo_product_claim d
       USING (SELECT promotion_id, 
                     parent_promotion_id, 
                     payout_type, 
                     promotion_processing_mode, 
                     is_team_used, 
                     is_team_collected_as_group, 
                     team_max_count, 
                     team_has_max_count, 
                     has_manager_payout, 
                     payout_manager_percent, 
                     payout_manager_period, 
                     payout_carry_over, 
                     stack_rank_approval_type, 
                     stack_rank_factor_type, 
                     stack_rank_cfse_id, 
                     display_stack_rank_factor, 
                     display_full_list_link_to_pax
                FROM promo_product_claim_g4
               ORDER BY promotion_id
              ) s
          ON (d.promotion_id = s.promotion_id)
        WHEN MATCHED THEN UPDATE 
         SET d.parent_promotion_id           = s.parent_promotion_id,
             d.payout_type                   = s.payout_type,
             d.promotion_processing_mode     = s.promotion_processing_mode,
             d.is_team_used                  = s.is_team_used,
             d.is_team_collected_as_group    = s.is_team_collected_as_group,
             d.team_max_count                = s.team_max_count,
             d.team_has_max_count            = s.team_has_max_count,
             d.has_manager_payout            = s.has_manager_payout,
             d.payout_manager_percent        = s.payout_manager_percent,
             d.payout_manager_period         = s.payout_manager_period,
             d.payout_carry_over             = s.payout_carry_over,
             d.stack_rank_approval_type      = s.stack_rank_approval_type,
             d.stack_rank_factor_type        = s.stack_rank_factor_type,
             d.stack_rank_cfse_id            = s.stack_rank_cfse_id,
             d.display_stack_rank_factor     = s.display_stack_rank_factor,
             d.display_full_list_link_to_pax = s.display_full_list_link_to_pax
        WHEN NOT MATCHED THEN INSERT
             (  d.promotion_id, 
                d.parent_promotion_id, 
                d.payout_type, 
                d.promotion_processing_mode, 
                d.is_team_used, 
                d.is_team_collected_as_group, 
                d.team_max_count, 
                d.team_has_max_count, 
                d.has_manager_payout, 
                d.payout_manager_percent, 
                d.payout_manager_period, 
                d.payout_carry_over, 
                d.stack_rank_approval_type, 
                d.stack_rank_factor_type, 
                d.stack_rank_cfse_id, 
                d.display_stack_rank_factor, 
                d.display_full_list_link_to_pax
              )
      VALUES (  s.promotion_id, 
                s.parent_promotion_id, 
                s.payout_type, 
                s.promotion_processing_mode, 
                s.is_team_used, 
                s.is_team_collected_as_group, 
                s.team_max_count, 
                s.team_has_max_count, 
                s.has_manager_payout, 
                s.payout_manager_percent, 
                s.payout_manager_period, 
                s.payout_carry_over, 
                s.stack_rank_approval_type, 
                s.stack_rank_factor_type, 
                s.stack_rank_cfse_id, 
                s.display_stack_rank_factor, 
                s.display_full_list_link_to_pax
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;

  --allow_public_recognition is not in g4 but in g5 not null column so populated 1
  v_stage    := 'MERGE promo_nomination';
  v_tbl_name := 'promo_nomination';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=    
      'MERGE INTO promo_nomination d
       USING (SELECT promotion_id, 
                     award_active, 
                     award_amount_type_fixed, 
                     award_amount_fixed, 
                     award_amount_min, 
                     award_amount_max, 
                     behavior_active, 
                     fileload_budget_amount, 
                     card_active, 
                     card_client_email, 
                     card_client_setup_done, 
                     include_certificate, 
                     award_group_type, 
                     max_group_members, 
                     evaluation_type, 
                     self_nomination, 
                     award_specifier_type, 
                     publication_date_active, 
                     publication_date
                FROM promo_nomination_g4
               ORDER BY promotion_id 
              ) s
          ON (d.promotion_id = s.promotion_id)
        WHEN MATCHED THEN UPDATE 
         SET d.award_active            = s.award_active,
             d.award_amount_type_fixed = s.award_amount_type_fixed,
             d.award_amount_fixed      = s.award_amount_fixed,
             d.award_amount_min        = s.award_amount_min,
             d.award_amount_max        = s.award_amount_max,
             d.behavior_active         = s.behavior_active,
             d.fileload_budget_amount  = s.fileload_budget_amount,
             d.card_active             = s.card_active,
             d.card_client_email       = s.card_client_email,
             d.card_client_setup_done  = s.card_client_setup_done,
             d.include_certificate     = s.include_certificate,
             d.award_group_type        = s.award_group_type,
             d.max_group_members       = s.max_group_members,
             d.evaluation_type         = s.evaluation_type,
             d.self_nomination         = s.self_nomination,
             d.award_specifier_type    = s.award_specifier_type,
             d.publication_date_active = s.publication_date_active,
             d.publication_date        = s.publication_date
        WHEN NOT MATCHED THEN INSERT
             (  d.promotion_id, 
                d.award_active, 
                d.award_amount_type_fixed, 
                d.award_amount_fixed, 
                d.award_amount_min, 
                d.award_amount_max, 
                d.behavior_active, 
                d.fileload_budget_amount, 
                d.card_active, 
                d.card_client_email, 
                d.card_client_setup_done, 
                d.include_certificate, 
                d.award_group_type, 
                d.max_group_members, 
                d.evaluation_type, 
                d.self_nomination,
                d.allow_public_recognition, 
                d.award_specifier_type, 
                d.publication_date_active, 
                d.publication_date
              )
      VALUES (  s.promotion_id, 
                s.award_active, 
                s.award_amount_type_fixed, 
                s.award_amount_fixed, 
                s.award_amount_min, 
                s.award_amount_max, 
                s.behavior_active, 
                s.fileload_budget_amount, 
                s.card_active, 
                s.card_client_email, 
                s.card_client_setup_done, 
                s.include_certificate, 
                s.award_group_type, 
                s.max_group_members, 
                s.evaluation_type, 
                s.self_nomination,
                1, 
                s.award_specifier_type, 
                s.publication_date_active, 
                s.publication_date
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE promo_notification';
  v_tbl_name := 'promo_notification';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO promo_notification d
       USING ( SELECT png4.promo_notification_id, 
                     png4.promotion_id, 
                     png4.promotion_notification_type, 
                     png4.notification_type, 
--                   png4.notification_message_id, 
                     NVL(m.message_id,png4.notification_message_id) notification_message_id,
                     png4.claim_form_step_email_id, 
                     png4.number_of_days, 
                     png4.frequency_type, 
                     png4.dayofweek_type, 
                     png4.descriminator, 
                     png4.created_by, 
                     png4.date_created, 
                     png4.modified_by, 
                     png4.date_modified, 
                     png4.version, 
                     png4.sequence_num
                FROM promo_notification_g4 png4,
                message_g4 mg4,
                message m
                WHERE png4.promotion_id NOT IN ( select promotion_id from promotion_g4 where promotion_type IN ('survey','wellness','goalquest','challengepoint'))
                AND png4.notification_message_id =  mg4.message_id(+)
                AND mg4.cm_asset_code=m.cm_asset_code(+)
                ORDER BY promo_notification_id
              ) s
          ON (d.promo_notification_id = s.promo_notification_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id                = s.promotion_id,
             d.promotion_notification_type = s.promotion_notification_type,
             d.notification_type           = s.notification_type,
             d.notification_message_id     = s.notification_message_id,
             d.claim_form_step_email_id    = s.claim_form_step_email_id,
             d.number_of_days              = s.number_of_days,
             d.frequency_type              = s.frequency_type,
             d.dayofweek_type              = s.dayofweek_type,
             d.descriminator               = s.descriminator,
             d.created_by                  = s.created_by,
             d.date_created                = s.date_created,
             d.modified_by                 = s.modified_by,
             d.date_modified               = s.date_modified,
             d.version                     = s.version,
             d.sequence_num                = s.sequence_num
        WHEN NOT MATCHED THEN INSERT
             (  d.promo_notification_id, 
                d.promotion_id, 
                d.promotion_notification_type, 
                d.notification_type, 
                d.notification_message_id, 
                d.claim_form_step_email_id, 
                d.number_of_days, 
                d.frequency_type, 
                d.dayofweek_type, 
                d.descriminator, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version, 
                d.sequence_num
              )
      VALUES (  s.promo_notification_id, 
                s.promotion_id, 
                s.promotion_notification_type, 
                s.notification_type, 
                s.notification_message_id, 
                s.claim_form_step_email_id, 
                s.number_of_days, 
                s.frequency_type, 
                s.dayofweek_type, 
                s.descriminator, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version, 
                s.sequence_num
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE promo_quiz';
  v_tbl_name := 'promo_quiz';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=   
      'MERGE INTO promo_quiz d
       USING (SELECT promotion_id, 
                     quiz_id, 
                     allow_unlimited_attempts, 
                     maximum_attempts, 
                     include_pass_quiz_certificate, 
                     award_active, 
                     award_amount
                FROM promo_quiz_g4
               ORDER BY promotion_id
              ) s
          ON (d.promotion_id = s.promotion_id)
        WHEN MATCHED THEN UPDATE 
         SET d.quiz_id                       = s.quiz_id,
             d.allow_unlimited_attempts      = s.allow_unlimited_attempts,
             d.maximum_attempts              = s.maximum_attempts,
             d.include_pass_quiz_certificate = s.include_pass_quiz_certificate,
             d.award_active                  = s.award_active,
             d.award_amount                  = s.award_amount
        WHEN NOT MATCHED THEN INSERT
             (  d.promotion_id, 
                d.quiz_id, 
                d.allow_unlimited_attempts, 
                d.maximum_attempts, 
                d.include_pass_quiz_certificate, 
                d.award_active, 
                d.award_amount
              )
      VALUES (  s.promotion_id, 
                s.quiz_id, 
                s.allow_unlimited_attempts, 
                s.maximum_attempts, 
                s.include_pass_quiz_certificate, 
                s.award_active, 
                s.award_amount
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  --public_rec_award_type_fixed is not in g4 and in g5 not null column so inserted 1
  v_stage    := 'MERGE promo_recognition';
  v_tbl_name := 'promo_recognition';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO promo_recognition d
       USING (SELECT promotion_id, 
                     award_active, 
                     award_amount_type_fixed, 
                     award_amount_fixed, 
                     award_amount_min, 
                     award_amount_max, 
                     behavior_active, 
                     fileload_budget_amount, 
                     card_active, 
                     card_client_email, 
                     card_client_setup_done, 
                     include_certificate, 
                     copy_recipient_manager, 
                     allow_manager_award, 
                     mgr_award_promotion_id, 
                     activity_number, 
                     award_structure, 
                     gift_wrap, 
                     apq_conversion, 
                     payment_method, 
                     no_notification, 
                     featured_awards_enabled, 
                     activity_tracker_enabled, 
                     is_open_enrollment_enabled, 
                     is_self_recognition_enabled, 
                     is_budget_sweep_enabled, 
                     budget_sweep_date, 
                     is_budget_sweep_run, 
                     budget_sweep_run_date, 
                     is_show_in_budget_tracker, 
                     is_include_purl, 
                     purl_media_type, 
                     purl_media_value, 
                     allow_public_recognition, 
                     allow_your_own_card, 
                     is_copy_others
                FROM promo_recognition_g4
               ORDER BY promotion_id
              ) s
          ON (d.promotion_id = s.promotion_id)
        WHEN MATCHED THEN UPDATE 
         SET d.award_active                = s.award_active,
             d.award_amount_type_fixed     = s.award_amount_type_fixed,
             d.award_amount_fixed          = s.award_amount_fixed,
             d.award_amount_min            = s.award_amount_min,
             d.award_amount_max            = s.award_amount_max,
             d.behavior_active             = s.behavior_active,
             d.fileload_budget_amount      = s.fileload_budget_amount,
             d.card_active                 = s.card_active,
             d.card_client_email           = s.card_client_email,
             d.card_client_setup_done      = s.card_client_setup_done,
             d.include_certificate         = s.include_certificate,
             d.copy_recipient_manager      = s.copy_recipient_manager,
             d.allow_manager_award         = s.allow_manager_award,
             d.mgr_award_promotion_id      = s.mgr_award_promotion_id,
             --d.activity_number             = s.activity_number,
             d.award_structure             = s.award_structure,
             --d.gift_wrap                   = s.gift_wrap,
             d.apq_conversion              = s.apq_conversion,
             --d.payment_method              = s.payment_method,
             d.no_notification             = s.no_notification,
             d.featured_awards_enabled     = s.featured_awards_enabled,
             --d.activity_tracker_enabled    = s.activity_tracker_enabled,
             d.is_open_enrollment_enabled  = s.is_open_enrollment_enabled,
             d.is_self_recognition_enabled = s.is_self_recognition_enabled,
             d.is_budget_sweep_enabled     = s.is_budget_sweep_enabled,
             d.budget_sweep_date           = s.budget_sweep_date,
             d.is_budget_sweep_run         = s.is_budget_sweep_run,
             d.budget_sweep_run_date       = s.budget_sweep_run_date,
             d.is_show_in_budget_tracker   = s.is_show_in_budget_tracker,
             d.is_include_purl             = s.is_include_purl,
             d.purl_media_type             = s.purl_media_type,
             d.purl_media_value            = s.purl_media_value,
             d.allow_public_recognition    = s.allow_public_recognition,
             d.allow_your_own_card         = s.allow_your_own_card,
             d.is_copy_others              = s.is_copy_others
        WHEN NOT MATCHED THEN INSERT
             (  d.promotion_id, 
                d.award_active, 
                d.award_amount_type_fixed, 
                d.award_amount_fixed, 
                d.award_amount_min, 
                d.award_amount_max, 
                d.behavior_active, 
                d.fileload_budget_amount, 
                d.card_active, 
                d.card_client_email, 
                d.card_client_setup_done, 
                d.include_certificate, 
                d.copy_recipient_manager, 
                d.allow_manager_award, 
                d.mgr_award_promotion_id, 
                --d.activity_number, 
                d.award_structure, 
                --d.gift_wrap, 
                d.apq_conversion, 
                --d.payment_method, 
                d.no_notification, 
                d.featured_awards_enabled, 
                --d.activity_tracker_enabled, 
                d.is_open_enrollment_enabled, 
                d.is_self_recognition_enabled, 
                d.is_budget_sweep_enabled, 
                d.budget_sweep_date, 
                d.is_budget_sweep_run, 
                d.budget_sweep_run_date, 
                d.is_show_in_budget_tracker, 
                d.is_include_purl, 
                d.purl_media_type, 
                d.purl_media_value, 
                d.allow_public_recognition, 
                d.allow_your_own_card, 
                d.is_copy_others,
                d.public_rec_award_type_fixed
              )
      VALUES (  s.promotion_id, 
                s.award_active, 
                s.award_amount_type_fixed, 
                s.award_amount_fixed, 
                s.award_amount_min, 
                s.award_amount_max, 
                s.behavior_active, 
                s.fileload_budget_amount, 
                s.card_active, 
                s.card_client_email, 
                s.card_client_setup_done, 
                s.include_certificate, 
                s.copy_recipient_manager, 
                s.allow_manager_award, 
                s.mgr_award_promotion_id, 
                --s.activity_number, 
                s.award_structure, 
                --s.gift_wrap, 
                s.apq_conversion, 
                --s.payment_method, 
                s.no_notification, 
                s.featured_awards_enabled, 
                --s.activity_tracker_enabled, 
                s.is_open_enrollment_enabled, 
                s.is_self_recognition_enabled, 
                s.is_budget_sweep_enabled, 
                s.budget_sweep_date, 
                s.is_budget_sweep_run, 
                s.budget_sweep_run_date, 
                s.is_show_in_budget_tracker, 
                s.is_include_purl, 
                s.purl_media_type, 
                s.purl_media_value, 
                s.allow_public_recognition, 
                s.allow_your_own_card, 
                s.is_copy_others,
                1
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE promo_home_page_item';
  v_tbl_name := 'promo_home_page_item';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO promo_home_page_item d
       USING (SELECT promo_home_page_item_id, 
                     promotion_id, 
                     product_id, 
                     catalog_id, 
                     product_set_id, 
                     copy, 
                     description, 
                     dtl_img_url, 
                     tmb_img_url, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM promo_home_page_item_g4
                WHERE promotion_id NOT IN ( select promotion_id from promotion_g4 where promotion_type IN ('''||'survey'||''','''||'wellness'||''','''||'goalquest'||''','''||'challengepoint'||'''))
               ORDER BY promo_home_page_item_id
              ) s
          ON (d.promo_home_page_item_id = s.promo_home_page_item_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id            = s.promotion_id,
             d.product_id              = s.product_id,
             d.catalog_id              = s.catalog_id,
             d.product_set_id          = s.product_set_id,
             d.copy                    = s.copy,
             d.description             = s.description,
             d.dtl_img_url             = s.dtl_img_url,
             d.tmb_img_url             = s.tmb_img_url,
             d.created_by              = s.created_by,
             d.date_created            = s.date_created,
             d.modified_by             = s.modified_by,
             d.date_modified           = s.date_modified,
             d.version                 = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.promo_home_page_item_id, 
              d.promotion_id, 
              d.product_id, 
              d.catalog_id, 
              d.product_set_id, 
              d.copy, 
              d.description, 
              d.dtl_img_url, 
              d.tmb_img_url, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.promo_home_page_item_id, 
              s.promotion_id, 
              s.product_id, 
              s.catalog_id, 
              s.product_set_id, 
              s.copy, 
              s.description, 
              s.dtl_img_url, 
              s.tmb_img_url, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  
  

 -- v_stage      := 'MERGE promo_wellness';
--  v_tbl_name := 'promo_wellness';
--  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
--
--    v_sql := 
--      'MERGE INTO promo_wellness d
--       USING (SELECT promotion_id, 
--                     award_active, 
--                     award_amount_type_fixed, 
--                     award_amount_fixed, 
--                     award_amount_min, 
--                     award_amount_max
--                FROM promo_wellness_g4
--               ORDER BY promotion_id
--              ) s
--          ON (d.promotion_id = s.promotion_id)
--        WHEN MATCHED THEN UPDATE 
--         SET d.award_active            = s.award_active,
--             d.award_amount_type_fixed = s.award_amount_type_fixed,
--             d.award_amount_fixed      = s.award_amount_fixed,
--             d.award_amount_min        = s.award_amount_min,
--             d.award_amount_max        = s.award_amount_max
--        WHEN NOT MATCHED THEN INSERT
--             (d.promotion_id, 
--              d.award_active, 
--              d.award_amount_type_fixed, 
--              d.award_amount_fixed, 
--              d.award_amount_min, 
--              d.award_amount_max
--              )
--      VALUES (s.promotion_id, 
--              s.award_active, 
--              s.award_amount_type_fixed, 
--              s.award_amount_fixed, 
--              s.award_amount_min, 
--              s.award_amount_max
--              )';
--
--    EXECUTE IMMEDIATE v_sql;
--    show_msg_count(v_stage, SQL%ROWCOUNT);
--    
--  ELSE
--    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
--    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
--                          v_msg_tbl_nf||v_tbl_name,
--                          NULL);
--  END IF;


  v_stage    := 'MERGE promo_sweepstake_drawing';
  v_tbl_name := 'promo_sweepstake_drawing';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO promo_sweepstake_drawing d
       USING (SELECT sweepstake_drawing_id, 
                     promotion_id, 
                     sweepstake_start_date, 
                     sweepstake_end_date, 
                     is_processed, 
                     guid, 
                     created_by, 
                     date_created, 
                     version
                FROM promo_sweepstake_drawing_g4
               ORDER BY sweepstake_drawing_id
              ) s
          ON (d.sweepstake_drawing_id = s.sweepstake_drawing_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id          = s.promotion_id,
             d.sweepstake_start_date = s.sweepstake_start_date,
             d.sweepstake_end_date   = s.sweepstake_end_date,
             d.is_processed          = s.is_processed,
             d.guid                  = s.guid,
             d.created_by            = s.created_by,
             d.date_created          = s.date_created,
             d.version               = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.sweepstake_drawing_id, 
                d.promotion_id, 
                d.sweepstake_start_date, 
                d.sweepstake_end_date, 
                d.is_processed, 
                d.guid, 
                d.created_by, 
                d.date_created, 
                d.version
              )
      VALUES (  s.sweepstake_drawing_id, 
                s.promotion_id, 
                s.sweepstake_start_date, 
                s.sweepstake_end_date, 
                s.is_processed, 
                s.guid, 
                s.created_by, 
                s.date_created, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE promo_sweepstake_winners';
  v_tbl_name := 'promo_sweepstake_winners';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO promo_sweepstake_winners d
       USING (SELECT winner_id, 
                     sweepstake_drawing_id, 
                     participant_id, 
                     consumer_id, 
                     is_removed, 
                     winner_type, 
                     guid, 
                     created_by, 
                     date_created, 
                     version
                FROM promo_sweepstake_winners_g4
               ORDER BY winner_id
              ) s
          ON (d.winner_id = s.winner_id)
        WHEN MATCHED THEN UPDATE 
         SET d.sweepstake_drawing_id = s.sweepstake_drawing_id,
             d.participant_id        = s.participant_id,
             d.consumer_id           = s.consumer_id,
             d.is_removed            = s.is_removed,
             d.winner_type           = s.winner_type,
             d.guid                  = s.guid,
             d.created_by            = s.created_by,
             d.date_created          = s.date_created,
             d.version               = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.winner_id, 
              d.sweepstake_drawing_id, 
              d.participant_id, 
              d.consumer_id, 
              d.is_removed, 
              d.winner_type, 
              d.guid, 
              d.created_by, 
              d.date_created, 
              d.version
              )
      VALUES (s.winner_id, 
              s.sweepstake_drawing_id, 
              s.participant_id, 
              s.consumer_id, 
              s.is_removed, 
              s.winner_type, 
              s.guid, 
              s.created_by, 
              s.date_created, 
              s.version
              );
  
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  

  --Proxy related tables start
  v_stage      := 'MERGE proxy';
  v_tbl_name := 'proxy';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO proxy d
       USING (SELECT proxy_id, 
                     proxy_user_id, 
                     user_id, 
                     is_all_module, 
                     core_access, 
                     version, 
                     created_by, 
                     date_created
                FROM proxy_g4
               ORDER BY proxy_id
              ) s
          ON (d.proxy_id = s.proxy_id)
        WHEN MATCHED THEN UPDATE 
         SET d.proxy_user_id = s.proxy_user_id,
             d.user_id       = s.user_id,
             d.is_all_module = s.is_all_module,
             d.core_access   = s.core_access,
             d.version       = s.version,
             d.created_by    = s.created_by,
             d.date_created  = s.date_created
        WHEN NOT MATCHED THEN INSERT
             (d.proxy_id, 
              d.proxy_user_id, 
              d.user_id, 
              d.is_all_module, 
              d.core_access, 
              d.version, 
              d.created_by, 
              d.date_created
              )
      VALUES (s.proxy_id, 
              s.proxy_user_id, 
              s.user_id, 
              s.is_all_module, 
              s.core_access, 
              s.version, 
              s.created_by, 
              s.date_created
              );

    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;

  --ORA-02291: integrity constraint (ABYF.PROXY_MODULE_PROXY_FK) violated - parent key not found  - proxy table required
  v_stage      := 'MERGE proxy_module';
  v_tbl_name := 'proxy_module';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO proxy_module d
       USING (SELECT proxy_module_id, 
                     proxy_id, 
                     promotion_type, 
                     is_all_promotion, 
                     version, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified
                FROM proxy_module_g4
               ORDER BY proxy_module_id
              ) s
          ON (d.proxy_module_id = s.proxy_module_id)
        WHEN MATCHED THEN UPDATE 
         SET d.proxy_id         = s.proxy_id,
             d.promotion_type   = s.promotion_type,
             d.is_all_promotion = s.is_all_promotion,
             d.version          = s.version,
             d.created_by       = s.created_by,
             d.date_created     = s.date_created,
             d.modified_by      = s.modified_by,
             d.date_modified    = s.date_modified
        WHEN NOT MATCHED THEN INSERT
             (  d.proxy_module_id, 
                d.proxy_id, 
                d.promotion_type, 
                d.is_all_promotion, 
                d.version, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified
              )
      VALUES (  s.proxy_module_id, 
                s.proxy_id, 
                s.promotion_type, 
                s.is_all_promotion, 
                s.version, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  -- ORA-02291: integrity constraint (ABYF.PROXY_MODULE_PROMO_MODULE_FK) violated - parent key not found required proxy_module
  v_stage      := 'MERGE proxy_module_promotion';
  v_tbl_name := 'proxy_module_promotion';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO proxy_module_promotion d
       USING (SELECT proxy_module_promotion_id, 
                     proxy_module_id, 
                     promotion_id, 
                     version, 
                     created_by, 
                     date_created
                FROM proxy_module_promotion_g4
                WHERE promotion_id NOT IN ( select promotion_id from promotion_g4 where promotion_type IN ('survey','wellness','goalquest','challengepoint'))
               ORDER BY proxy_module_promotion_id
              ) s
          ON (d.proxy_module_promotion_id = s.proxy_module_promotion_id)
        WHEN MATCHED THEN UPDATE 
         SET d.proxy_module_id           = s.proxy_module_id,
             d.promotion_id              = s.promotion_id,
             d.version                   = s.version,
             d.created_by                = s.created_by,
             d.date_created              = s.date_created
        WHEN NOT MATCHED THEN INSERT
             (  d.proxy_module_promotion_id, 
                d.proxy_module_id, 
                d.promotion_id, 
                d.version, 
                d.created_by, 
                d.date_created
              )
      VALUES (  s.proxy_module_promotion_id, 
                s.proxy_module_id, 
                s.promotion_id, 
                s.version, 
                s.created_by, 
                s.date_created
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  --Proxy related tables start ends

  v_stage      := 'MERGE stack_rank';
  v_tbl_name := 'stack_rank';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO stack_rank d
       USING (SELECT stack_rank_id, 
                     promotion_id, 
                     guid, 
                     state, 
                     start_date, 
                     end_date, 
                     calculate_payout, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM stack_rank_g4
               ORDER BY stack_rank_id
              ) s
          ON (d.stack_rank_id = s.stack_rank_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id     = s.promotion_id,
             d.guid             = s.guid,
             d.state            = s.state,
             d.start_date       = s.start_date,
             d.end_date         = s.end_date,
             d.calculate_payout = s.calculate_payout,
             d.created_by       = s.created_by,
             d.date_created     = s.date_created,
             d.modified_by      = s.modified_by,
             d.date_modified    = s.date_modified,
             d.version          = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.stack_rank_id, 
                d.promotion_id, 
                d.guid, 
                d.state, 
                d.start_date, 
                d.end_date, 
                d.calculate_payout, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.stack_rank_id, 
                s.promotion_id, 
                s.guid, 
                s.state, 
                s.start_date, 
                s.end_date, 
                s.calculate_payout, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE stack_rank_node';
  v_tbl_name := 'stack_rank_node';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO stack_rank_node d
       USING (SELECT stack_rank_node_id, 
                     stack_rank_id, 
                     node_id, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM stack_rank_node_g4
               ORDER BY stack_rank_node_id
              ) s
          ON (d.stack_rank_node_id = s.stack_rank_node_id)
        WHEN MATCHED THEN UPDATE 
         SET d.stack_rank_id      = s.stack_rank_id,
             d.node_id            = s.node_id,
             d.created_by         = s.created_by,
             d.date_created       = s.date_created,
             d.modified_by        = s.modified_by,
             d.date_modified      = s.date_modified,
             d.version            = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.stack_rank_node_id, 
              d.stack_rank_id, 
              d.node_id, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.stack_rank_node_id, 
              s.stack_rank_id, 
              s.node_id, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              );
  
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE stack_rank_participant';
  v_tbl_name := 'stack_rank_participant';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO stack_rank_participant d
       USING (SELECT stack_rank_participant_id, 
                     stack_rank_node_id, 
                     user_id, 
                     stack_rank_factor, 
                     rank, 
                     tied, 
                     payout, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM stack_rank_participant_g4
               ORDER BY stack_rank_participant_id
              ) s
          ON (d.stack_rank_participant_id = s.stack_rank_participant_id)
        WHEN MATCHED THEN UPDATE 
         SET d.stack_rank_node_id        = s.stack_rank_node_id,
             d.user_id                   = s.user_id,
             d.stack_rank_factor         = s.stack_rank_factor,
             d.rank                      = s.rank,
             d.tied                      = s.tied,
             d.payout                    = s.payout,
             d.created_by                = s.created_by,
             d.date_created              = s.date_created,
             d.modified_by               = s.modified_by,
             d.date_modified             = s.date_modified,
             d.version                   = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.stack_rank_participant_id, 
              d.stack_rank_node_id, 
              d.user_id, 
              d.stack_rank_factor, 
              d.rank, 
              d.tied, 
              d.payout, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.stack_rank_participant_id, 
              s.stack_rank_node_id, 
              s.user_id, 
              s.stack_rank_factor, 
              s.rank, 
              s.tied, 
              s.payout, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  
  v_stage      := 'MERGE claim';
  v_tbl_name := 'claim';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO claim d
       USING (SELECT claim_id, 
                     submitter_id, 
                     promotion_id, 
                     node_id, 
                     proxy_user_id, 
                     claim_group_id, 
                     is_open, 
                     approval_round, 
                     last_approval_node_id, 
                     claim_number, 
                     approver_comments, 
                     submission_date, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM claim_g4
                WHERE NOT EXISTS (SELECT * FROM promotion_g4 WHERE promotion_id = claim_g4.promotion_id AND promotion_type IN ('survey','wellness','goalquest','challengepoint'))
               ORDER BY claim_id
              ) s
          ON (d.claim_id = s.claim_id)
        WHEN MATCHED THEN UPDATE 
         SET d.submitter_id          = s.submitter_id,
             d.promotion_id          = s.promotion_id,
             d.node_id               = s.node_id,
             d.proxy_user_id         = s.proxy_user_id,
             d.claim_group_id        = s.claim_group_id,
             d.is_open               = s.is_open,
             d.approval_round        = s.approval_round,
             d.last_approval_node_id = s.last_approval_node_id,
             d.claim_number          = s.claim_number,
             d.approver_comments     = s.approver_comments,
             d.submission_date       = s.submission_date,
             d.created_by            = s.created_by,
             d.date_created          = s.date_created,
             d.modified_by           = s.modified_by,
             d.date_modified         = s.date_modified,
             d.version               = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_id, 
                d.submitter_id, 
                d.promotion_id, 
                d.node_id, 
                d.proxy_user_id, 
                d.claim_group_id, 
                d.is_open, 
                d.approval_round, 
                d.last_approval_node_id, 
                d.claim_number, 
                d.approver_comments, 
                d.submission_date, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.claim_id, 
                s.submitter_id, 
                s.promotion_id, 
                s.node_id, 
                s.proxy_user_id, 
                s.claim_group_id, 
                s.is_open, 
                s.approval_round, 
                s.last_approval_node_id, 
                s.claim_number, 
                s.approver_comments, 
                s.submission_date, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  
  
  v_stage      := 'MERGE claim_approver_snapshot';
  v_tbl_name := 'claim_approver_snapshot';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO claim_approver_snapshot d
       USING (SELECT claim_approver_snapshot_id, 
                     claim_id, 
                     claim_group_id, 
                     approver_user_id, 
                     source_node_id, 
                     date_created, 
                     created_by, 
                     date_modified, 
                     modified_by, 
                     version
                FROM claim_approver_snapshot_g4
                WHERE EXISTS (SELECT * FROM claim WHERE claim_id=claim_approver_snapshot_g4.claim_id)
               ORDER BY claim_approver_snapshot_id
              ) s
          ON (d.claim_approver_snapshot_id = s.claim_approver_snapshot_id)
        WHEN MATCHED THEN UPDATE 
         SET d.claim_id                   = s.claim_id,
             d.claim_group_id             = s.claim_group_id,
             d.approver_user_id           = s.approver_user_id,
             d.source_node_id             = s.source_node_id,
             d.date_created               = s.date_created,
             d.created_by                 = s.created_by,
             d.date_modified              = s.date_modified,
             d.modified_by                = s.modified_by,
             d.version                    = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_approver_snapshot_id, 
                d.claim_id, 
                d.claim_group_id, 
                d.approver_user_id, 
                d.source_node_id, 
                d.date_created, 
                d.created_by, 
                d.date_modified, 
                d.modified_by, 
                d.version
              )
      VALUES (  s.claim_approver_snapshot_id, 
                s.claim_id, 
                s.claim_group_id, 
                s.approver_user_id, 
                s.source_node_id, 
                s.date_created, 
                s.created_by, 
                s.date_modified, 
                s.modified_by, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  

  v_stage      := 'MERGE claim_cfse';
  v_tbl_name := 'claim_cfse';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO claim_cfse d
       USING (SELECT claim_cfse_id, 
                     claim_form_step_element_id, 
                     claim_id, 
                     value, 
                     sequence_num, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM claim_cfse_g4
                WHERE EXISTS (SELECT * FROM claim WHERE claim_id=claim_cfse_g4.claim_id)
               ORDER BY claim_cfse_id
              ) s
          ON (d.claim_cfse_id = s.claim_cfse_id)
        WHEN MATCHED THEN UPDATE 
         SET d.claim_form_step_element_id = s.claim_form_step_element_id,
             d.claim_id                   = s.claim_id,
             d.value                      = s.value,
             d.sequence_num               = s.sequence_num,
             d.created_by                 = s.created_by,
             d.date_created               = s.date_created,
             d.modified_by                = s.modified_by,
             d.date_modified              = s.date_modified,
             d.version                    = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_cfse_id, 
                d.claim_form_step_element_id, 
                d.claim_id, 
                d.value, 
                d.sequence_num, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.claim_cfse_id, 
                s.claim_form_step_element_id, 
                s.claim_id, 
                s.value, 
                s.sequence_num, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE claim_group';
  v_tbl_name := 'claim_group';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO claim_group d
       USING (SELECT claim_group_id, 
                     promotion_id, 
                     participant_id, 
                     node_id, 
                     serial_id, 
                     approval_status_type, 
                     approval_option_reason_type, 
                     approver_user_id, 
                     date_approved, 
                     notification_date, 
                     award_qty, 
                     date_created, 
                     created_by, 
                     date_modified, 
                     modified_by, 
                     version, 
                     is_open, 
                     approval_round, 
                     last_approval_node_id, 
                     selection_start_date, 
                     selection_end_date, 
                     approver_comments
                FROM claim_group_g4
                WHERE NOT EXISTS (SELECT * FROM promotion_g4 WHERE promotion_id = claim_group_g4.promotion_id AND promotion_type IN ('survey','wellness','goalquest','challengepoint'))
               ORDER BY claim_group_id
              ) s
          ON (d.claim_group_id = s.claim_group_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id                = s.promotion_id,
             d.participant_id              = s.participant_id,
             d.node_id                     = s.node_id,
             d.serial_id                   = s.serial_id,
             d.approval_status_type        = s.approval_status_type,
             d.approval_option_reason_type = s.approval_option_reason_type,
             d.approver_user_id            = s.approver_user_id,
             d.date_approved               = s.date_approved,
             d.notification_date           = s.notification_date,
             d.award_qty                   = s.award_qty,
             d.date_created                = s.date_created,
             d.created_by                  = s.created_by,
             d.date_modified               = s.date_modified,
             d.modified_by                 = s.modified_by,
             d.version                     = s.version,
             d.is_open                     = s.is_open,
             d.approval_round              = s.approval_round,
             d.last_approval_node_id       = s.last_approval_node_id,
             d.selection_start_date        = s.selection_start_date,
             d.selection_end_date          = s.selection_end_date,
             d.approver_comments           = s.approver_comments
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_group_id, 
                d.promotion_id, 
                d.participant_id, 
                d.node_id, 
                d.serial_id, 
                d.approval_status_type, 
                d.approval_option_reason_type, 
                d.approver_user_id, 
                d.date_approved, 
                d.notification_date, 
                d.award_qty, 
                d.date_created, 
                d.created_by, 
                d.date_modified, 
                d.modified_by, 
                d.version, 
                d.is_open, 
                d.approval_round, 
                d.last_approval_node_id, 
                d.selection_start_date, 
                d.selection_end_date, 
                d.approver_comments
              )
      VALUES (  s.claim_group_id, 
                s.promotion_id, 
                s.participant_id, 
                s.node_id, 
                s.serial_id, 
                s.approval_status_type, 
                s.approval_option_reason_type, 
                s.approver_user_id, 
                s.date_approved, 
                s.notification_date, 
                s.award_qty, 
                s.date_created, 
                s.created_by, 
                s.date_modified, 
                s.modified_by, 
                s.version, 
                s.is_open, 
                s.approval_round, 
                s.last_approval_node_id, 
                s.selection_start_date, 
                s.selection_end_date, 
                s.approver_comments
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE claim_item';
  v_tbl_name := 'claim_item';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO claim_item d
       USING (SELECT claim_item_id, 
                     claim_id, 
                     serial_id, 
                     approval_status_type, 
                     approval_option_reason_type, 
                     approver_user_id, 
                     date_approved, 
                     date_created, 
                     created_by, 
                     date_modified, 
                     modified_by, 
                     version
                FROM claim_item_g4
                WHERE EXISTS (SELECT * FROM claim WHERE claim_id=claim_item_g4.claim_id)
               ORDER BY claim_item_id
              ) s
          ON (d.claim_item_id = s.claim_item_id)
        WHEN MATCHED THEN UPDATE 
         SET d.claim_id                    = s.claim_id,
             d.serial_id                   = s.serial_id,
             d.approval_status_type        = s.approval_status_type,
             d.approval_option_reason_type = s.approval_option_reason_type,
             d.approver_user_id            = s.approver_user_id,
             d.date_approved               = s.date_approved,
             d.date_created                = s.date_created,
             d.created_by                  = s.created_by,
             d.date_modified               = s.date_modified,
             d.modified_by                 = s.modified_by,
             d.version                     = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_item_id, 
                d.claim_id, 
                d.serial_id, 
                d.approval_status_type, 
                d.approval_option_reason_type, 
                d.approver_user_id, 
                d.date_approved, 
                d.date_created, 
                d.created_by, 
                d.date_modified, 
                d.modified_by, 
                d.version
              )
      VALUES (  s.claim_item_id, 
                s.claim_id, 
                s.serial_id, 
                s.approval_status_type, 
                s.approval_option_reason_type, 
                s.approver_user_id, 
                s.date_approved, 
                s.date_created, 
                s.created_by, 
                s.date_modified, 
                s.modified_by, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE claim_item_approver';
  v_tbl_name := 'claim_item_approver';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO claim_item_approver d
       USING (SELECT claim_item_approver_id, 
                     claim_item_id, 
                     claim_group_id, 
                     item_approver_discrim, 
                     approval_round, 
                     approval_status_type, 
                     approval_option_reason_type, 
                     approver_user_id, 
                     date_approved, 
                     date_created, 
                     created_by, 
                     date_modified, 
                     modified_by, 
                     version
                FROM claim_item_approver_g4
                WHERE EXISTS (SELECT * FROM claim_item WHERE claim_item_id=claim_item_approver_g4.claim_item_id)
               ORDER BY claim_item_approver_id
              ) s
          ON (d.claim_item_approver_id = s.claim_item_approver_id)
        WHEN MATCHED THEN UPDATE 
         SET d.claim_item_id               = s.claim_item_id,
             d.claim_group_id              = s.claim_group_id,
             d.item_approver_discrim       = s.item_approver_discrim,
             d.approval_round              = s.approval_round,
             d.approval_status_type        = s.approval_status_type,
             d.approval_option_reason_type = s.approval_option_reason_type,
             d.approver_user_id            = s.approver_user_id,
             d.date_approved               = s.date_approved,
             d.date_created                = s.date_created,
             d.created_by                  = s.created_by,
             d.date_modified               = s.date_modified,
             d.modified_by                 = s.modified_by,
             d.version                     = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_item_approver_id, 
                d.claim_item_id, 
                d.claim_group_id, 
                d.item_approver_discrim, 
                d.approval_round, 
                d.approval_status_type, 
                d.approval_option_reason_type, 
                d.approver_user_id, 
                d.date_approved, 
                d.date_created, 
                d.created_by, 
                d.date_modified, 
                d.modified_by, 
                d.version
              )
      VALUES (  s.claim_item_approver_id, 
                s.claim_item_id, 
                s.claim_group_id, 
                s.item_approver_discrim, 
                s.approval_round, 
                s.approval_status_type, 
                s.approval_option_reason_type, 
                s.approver_user_id, 
                s.date_approved, 
                s.date_created, 
                s.created_by, 
                s.date_modified, 
                s.modified_by, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE claim_participant';
  v_tbl_name := 'claim_participant';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO claim_participant d
       USING (SELECT claim_participant_id, 
                     claim_id, 
                     participant_id, 
                     node_id, 
                     promo_team_position_id, 
                     date_created, 
                     created_by, 
                     date_modified, 
                     modified_by, 
                     version
                FROM claim_participant_g4
                WHERE EXISTS (SELECT * FROM claim WHERE claim_id = claim_participant_g4.claim_id)
               ORDER BY claim_participant_id
              ) s
          ON (d.claim_participant_id = s.claim_participant_id)
        WHEN MATCHED THEN UPDATE 
         SET d.claim_id               = s.claim_id,
             d.participant_id         = s.participant_id,
             d.node_id                = s.node_id,
             d.promo_team_position_id = s.promo_team_position_id,
             d.date_created           = s.date_created,
             d.created_by             = s.created_by,
             d.date_modified          = s.date_modified,
             d.modified_by            = s.modified_by,
             d.version                = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_participant_id, 
                d.claim_id, 
                d.participant_id, 
                d.node_id, 
                d.promo_team_position_id, 
                d.date_created, 
                d.created_by, 
                d.date_modified, 
                d.modified_by, 
                d.version
              )
      VALUES (  s.claim_participant_id, 
                s.claim_id, 
                s.participant_id, 
                s.node_id, 
                s.promo_team_position_id, 
                s.date_created, 
                s.created_by, 
                s.date_modified, 
                s.modified_by, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE claim_recipient';
  v_tbl_name := 'claim_recipient';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO claim_recipient d
       USING (SELECT claim_item_id, 
                     participant_id, 
                     node_id, 
                     award_qty, 
                     promo_merch_country_id, 
                     promo_merch_program_level_id, 
                     product_id, 
                     calculator_score, 
                     notification_date 
                     --non_participant_first_name, 
                     --non_participant_last_name, 
                     --non_participant_email
                FROM claim_recipient_g4
                WHERE EXISTS (SELECT * FROM claim_item WHERE claim_item_id=claim_recipient_g4.claim_item_id)
               ORDER BY claim_item_id
              ) s
          ON (d.claim_item_id = s.claim_item_id)
        WHEN MATCHED THEN UPDATE 
         SET d.participant_id               = s.participant_id,
             d.node_id                      = s.node_id,
             d.award_qty                    = s.award_qty,
             d.promo_merch_country_id       = s.promo_merch_country_id,
             d.promo_merch_program_level_id = s.promo_merch_program_level_id,
             d.product_id                   = s.product_id,
             d.calculator_score             = s.calculator_score,
             d.notification_date            = s.notification_date
             --d.non_participant_first_name   = s.non_participant_first_name,
             --d.non_participant_last_name    = s.non_participant_last_name,
             --d.non_participant_email        = s.non_participant_email
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_item_id, 
                d.participant_id, 
                d.node_id, 
                d.award_qty, 
                d.promo_merch_country_id, 
                d.promo_merch_program_level_id, 
                d.product_id, 
                d.calculator_score, 
                d.notification_date 
                --d.non_participant_first_name, 
                --d.non_participant_last_name, 
                --d.non_participant_email
              )
      VALUES (  s.claim_item_id, 
                s.participant_id, 
                s.node_id, 
                s.award_qty, 
                s.promo_merch_country_id, 
                s.promo_merch_program_level_id, 
                s.product_id, 
                s.calculator_score, 
                s.notification_date 
                --s.non_participant_first_name, 
                --s.non_participant_last_name, 
                --s.non_participant_email
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE claim_product';
  v_tbl_name := 'claim_product';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO claim_product d
       USING (SELECT claim_item_id, 
                     product_id, 
                     product_qty, 
                     comments, 
                     is_primary 
                     --revenue
                FROM claim_product_g4
               ORDER BY claim_item_id
              ) s
          ON (d.claim_item_id = s.claim_item_id)
        WHEN MATCHED THEN UPDATE 
         SET d.product_id    = s.product_id,
             d.product_qty   = s.product_qty,
             d.comments      = s.comments,
             d.is_primary    = s.is_primary
             --d.revenue       = s.revenue
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_item_id, 
                d.product_id, 
                d.product_qty, 
                d.comments, 
                d.is_primary
                --d.revenue
              )
      VALUES (  s.claim_item_id, 
                s.product_id, 
                s.product_qty, 
                s.comments, 
                s.is_primary 
                --s.revenue
              )';
    
    EXECUTE IMMEDIATE v_sql;              
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE claim_product_characteristic';
  v_tbl_name := 'claim_product_characteristic';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO claim_product_characteristic d
       USING (SELECT claim_prod_characteristic_id, 
                     characteristic_id, 
                     claim_product_id, 
                     characteristic_value, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM claim_product_characteristi_g4  --restricted length of table claim_product_characteristic to 27
               ORDER BY claim_prod_characteristic_id
              ) s
          ON (d.claim_prod_characteristic_id = s.claim_prod_characteristic_id)
        WHEN MATCHED THEN UPDATE 
         SET d.characteristic_id            = s.characteristic_id,
             d.claim_product_id             = s.claim_product_id,
             d.characteristic_value         = s.characteristic_value,
             d.created_by                   = s.created_by,
             d.date_created                 = s.date_created,
             d.modified_by                  = s.modified_by,
             d.date_modified                = s.date_modified,
             d.version                      = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_prod_characteristic_id, 
                d.characteristic_id, 
                d.claim_product_id, 
                d.characteristic_value, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.claim_prod_characteristic_id, 
                s.characteristic_id, 
                s.claim_product_id, 
                s.characteristic_value, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE nomination_claim';
  v_tbl_name := 'nomination_claim';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO nomination_claim d
       USING (SELECT claim_id, 
                     card_id, 
                     copy_sender, 
                     behavior, 
                     submitter_comments, 
                     team_name                     
                FROM nomination_claim_g4
               ORDER BY claim_id
              ) s
          ON (d.claim_id = s.claim_id)
        WHEN MATCHED THEN UPDATE 
         SET d.card_id            = s.card_id,
             d.copy_sender        = s.copy_sender,
             d.behavior           = s.behavior,
             d.submitter_comments = s.submitter_comments,
             d.team_name          = s.team_name
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_id, 
                d.card_id, 
                d.copy_sender, 
                d.behavior, 
                d.submitter_comments, 
                d.team_name,
                d.submitter_comments_lang_id
              )
      VALUES (  s.claim_id, 
                s.card_id, 
                s.copy_sender, 
                s.behavior, 
                s.submitter_comments, 
                s.team_name,
                'en_US'
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;

/*      --Product Claim module not required to migrate data from G4 to G5 in 1.1 release
  v_stage      := 'MERGE product_claim'F;
  v_tbl_name := 'product_claim';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO product_claim d
       USING (SELECT claim_id
                FROM product_claim_g4
              ) s
          ON (d.claim_id = s.claim_id)
        WHEN NOT MATCHED THEN INSERT
             (d.claim_id
              )
      VALUES (s.claim_id
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
*/

  v_stage      := 'MERGE quiz_claim';
  v_tbl_name := 'quiz_claim';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO quiz_claim d
       USING (SELECT qc.claim_id, 
                     qc.current_question_id, 
                     qc.pass, 
                     qc.passing_score, 
                     qc.score,
                     qq.quiz_id, 
                     qc.question_count
                FROM quiz_claim_g4 qc,
                quiz_question_g4 qq
                WHERE 
                 qc.current_question_id = qq.quiz_question_id 
               ORDER BY qc.claim_id
              ) s
          ON (d.claim_id = s.claim_id)
        WHEN MATCHED THEN UPDATE 
         SET d.current_question_id = s.current_question_id,
             d.pass                = s.pass,
             d.passing_score       = s.passing_score,
             d.score               = s.score,
             d.question_count      = s.question_count
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_id,
                d.quiz_id, 
                d.current_question_id, 
                d.pass, 
                d.passing_score, 
                d.score, 
                d.question_count
              )
      VALUES (  s.claim_id, 
                s.quiz_id,
                s.current_question_id, 
                s.pass, 
                s.passing_score, 
                s.score, 
                s.question_count
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE quiz_response';
  v_tbl_name := 'quiz_response';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO quiz_response d
       USING (SELECT quiz_response_id, 
                     claim_id, 
                     quiz_question_id, 
                     selected_quiz_answer_id, 
                     correct, 
                     sequence_num, 
                     date_created, 
                     created_by, 
                     date_modified, 
                     modified_by, 
                     approval_status_type, 
                     approval_option_reason_type, 
                     version
                FROM quiz_response_g4
               ORDER BY quiz_response_id
              ) s
          ON (d.quiz_response_id = s.quiz_response_id)
        WHEN MATCHED THEN UPDATE 
         SET d.claim_id                    = s.claim_id,
             d.quiz_question_id            = s.quiz_question_id,
             d.selected_quiz_answer_id     = s.selected_quiz_answer_id,
             d.correct                     = s.correct,
             d.sequence_num                = s.sequence_num,
             d.date_created                = s.date_created,
             d.created_by                  = s.created_by,
             d.date_modified               = s.date_modified,
             d.modified_by                 = s.modified_by,
             d.approval_status_type        = s.approval_status_type,
             d.approval_option_reason_type = s.approval_option_reason_type,
             d.version                     = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.quiz_response_id, 
                d.claim_id, 
                d.quiz_question_id, 
                d.selected_quiz_answer_id, 
                d.correct, 
                d.sequence_num, 
                d.date_created, 
                d.created_by, 
                d.date_modified, 
                d.modified_by, 
                d.approval_status_type, 
                d.approval_option_reason_type, 
                d.version
              )
      VALUES (  s.quiz_response_id, 
                s.claim_id, 
                s.quiz_question_id, 
                s.selected_quiz_answer_id, 
                s.correct, 
                s.sequence_num, 
                s.date_created, 
                s.created_by, 
                s.date_modified, 
                s.modified_by, 
                s.approval_status_type, 
                s.approval_option_reason_type, 
                s.version
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE recognition_claim';
  v_tbl_name := 'recognition_claim';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO recognition_claim d
       USING (SELECT claim_id, 
                     card_id, 
                     certificate_id, 
                     copy_manager, 
                     copy_sender, 
                     behavior, 
                     submitter_comments, 
                     is_reverse, 
                     own_card_name
                FROM recognition_claim_g4
               ORDER BY claim_id
              ) s
          ON (d.claim_id = s.claim_id)
        WHEN MATCHED THEN UPDATE 
         SET d.card_id            = s.card_id,
             d.certificate_id     = s.certificate_id,
             d.copy_manager       = s.copy_manager,
             d.copy_sender        = s.copy_sender,
             d.behavior           = s.behavior,
             d.submitter_comments = s.submitter_comments,
             d.is_reverse         = s.is_reverse,
             d.own_card_name      = s.own_card_name
        WHEN NOT MATCHED THEN INSERT
             (  d.claim_id, 
                d.card_id, 
                d.certificate_id, 
                d.copy_manager, 
                d.copy_sender, 
                d.behavior,
                d.submitter_comments_lang_id, 
                d.submitter_comments, 
                d.is_reverse, 
                d.own_card_name
              )
      VALUES (  s.claim_id, 
                s.card_id, 
                s.certificate_id, 
                s.copy_manager, 
                s.copy_sender, 
                s.behavior,
                ''en_US'',
                s.submitter_comments, 
                s.is_reverse, 
                s.own_card_name
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE merch_order';
  v_tbl_name := 'merch_order';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=
      'MERGE INTO merch_order d
       USING (SELECT mo.merch_order_id, 
                     fnc_java_encrypt(pem.gift_code) gift_code, 
                     fnc_java_encrypt(pem.gift_code_key) gift_code_key, 
                     claim_id, 
                     participant_id, 
                     order_number, 
                     is_redeemed, 
                     merch_gift_code_type, 
                     product_id, 
                     product_description, 
                     promo_merch_program_level_id, 
                     points, 
                     reference_number, 
                     expiration_date, 
                     --non_participant_first_name, 
                     --non_participant_last_name, 
                     --non_participant_email, 
                     mo.date_created, 
                     mo.created_by, 
                     date_modified, 
                     modified_by, 
                     version, 
                     order_status
                FROM merch_order_g4 mo,
                     prep_encrypt_merch_g4 pem
               WHERE mo.merch_order_id = pem.merch_order_id (+)
                 --AND mo.is_redeemed = 0
               ORDER BY merch_order_id
              ) s
          ON (d.merch_order_id = s.merch_order_id)
        WHEN MATCHED THEN UPDATE 
         SET d.gift_code                    = s.gift_code,
             d.gift_code_key                = s.gift_code_key,
             d.claim_id                     = s.claim_id,
             d.participant_id               = s.participant_id,
             d.order_number                 = s.order_number,
             d.is_redeemed                  = s.is_redeemed,
             d.merch_gift_code_type         = s.merch_gift_code_type,
             d.product_id                   = s.product_id,
             d.product_description          = s.product_description,
             d.promo_merch_program_level_id = s.promo_merch_program_level_id,
             d.points                       = s.points,
             d.reference_number             = s.reference_number,
             d.expiration_date              = s.expiration_date,
             --d.non_participant_first_name   = s.non_participant_first_name,
             --d.non_participant_last_name    = s.non_participant_last_name,
             --d.non_participant_email        = s.non_participant_email,
             d.date_created                 = s.date_created,
             d.created_by                   = s.created_by,
             d.date_modified                = s.date_modified,
             d.modified_by                  = s.modified_by,
             d.version                      = s.version,
             d.order_status                 = s.order_status
        WHEN NOT MATCHED THEN INSERT
             (  d.merch_order_id, 
                d.gift_code, 
                d.gift_code_key, 
                d.claim_id, 
                d.participant_id, 
                d.order_number, 
                d.is_redeemed, 
                d.merch_gift_code_type, 
                d.product_id, 
                d.product_description, 
                d.promo_merch_program_level_id, 
                d.points, 
                d.reference_number, 
                d.expiration_date, 
                --d.non_participant_first_name, 
                --d.non_participant_last_name, 
                --d.non_participant_email, 
                d.date_created, 
                d.created_by, 
                d.date_modified, 
                d.modified_by, 
                d.version, 
                d.order_status
              )
      VALUES (  s.merch_order_id, 
                s.gift_code, 
                s.gift_code_key, 
                s.claim_id, 
                s.participant_id, 
                s.order_number, 
                s.is_redeemed, 
                s.merch_gift_code_type, 
                s.product_id, 
                s.product_description, 
                s.promo_merch_program_level_id, 
                s.points, 
                s.reference_number, 
                s.expiration_date, 
                --s.non_participant_first_name, 
                --s.non_participant_last_name, 
                --s.non_participant_email, 
                s.date_created, 
                s.created_by, 
                s.date_modified, 
                s.modified_by, 
                s.version, 
                s.order_status
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  --Claim related tables end
  
  v_stage      := 'MERGE calculator_criterion';
  v_tbl_name := 'calculator_criterion';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO calculator_criterion d
       USING (SELECT calculator_criterion_id, 
                     calculator_id, 
                     status_type, 
                     cm_asset_name, 
                     weight_value, 
                     sequence_num, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM calculator_criterion_g4
               ORDER BY calculator_criterion_id
              ) s
          ON (d.calculator_criterion_id = s.calculator_criterion_id)
        WHEN MATCHED THEN UPDATE 
         SET d.calculator_id           = s.calculator_id,
             d.status_type             = s.status_type,
             d.cm_asset_name           = s.cm_asset_name,
             d.weight_value            = s.weight_value,
             d.sequence_num            = s.sequence_num,
             d.created_by              = s.created_by,
             d.date_created            = s.date_created,
             d.modified_by             = s.modified_by,
             d.date_modified           = s.date_modified,
             d.version                 = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.calculator_criterion_id, 
                d.calculator_id, 
                d.status_type, 
                d.cm_asset_name, 
                d.weight_value, 
                d.sequence_num, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.calculator_criterion_id, 
                s.calculator_id, 
                s.status_type, 
                s.cm_asset_name, 
                s.weight_value, 
                s.sequence_num, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE calculator_criterion_rating';
  v_tbl_name := 'calculator_criterion_rating';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO calculator_criterion_rating d
       USING (SELECT calculator_criterion_rating_id, 
                     calculator_criterion_id, 
                     cm_asset_name, 
                     rating_value, 
                     sequence_num, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM calculator_criterion_rating_g4
               ORDER BY calculator_criterion_rating_id
              ) s
          ON (d.calculator_criterion_rating_id = s.calculator_criterion_rating_id)
        WHEN MATCHED THEN UPDATE 
         SET d.calculator_criterion_id        = s.calculator_criterion_id,
             d.cm_asset_name                  = s.cm_asset_name,
             d.rating_value                   = s.rating_value,
             d.sequence_num                   = s.sequence_num,
             d.created_by                     = s.created_by,
             d.date_created                   = s.date_created,
             d.modified_by                    = s.modified_by,
             d.date_modified                  = s.date_modified,
             d.version                        = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.calculator_criterion_rating_id, 
                d.calculator_criterion_id, 
                d.cm_asset_name, 
                d.rating_value, 
                d.sequence_num, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.calculator_criterion_rating_id, 
                s.calculator_criterion_id, 
                s.cm_asset_name, 
                s.rating_value, 
                s.sequence_num, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE calculator_response';
  v_tbl_name := 'calculator_response';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO calculator_response d
       USING (SELECT calculator_response_id, 
                     claim_id, 
                     calculator_criterion_id, 
                     selected_rating_id, 
                     rating_value, 
                     criterion_weight, 
                     sequence_num, 
                     date_created, 
                     created_by, 
                     date_modified, 
                     modified_by, 
                     version
                FROM calculator_response_g4
               ORDER BY calculator_response_id
              ) s
          ON (d.calculator_response_id = s.calculator_response_id)
        WHEN MATCHED THEN UPDATE 
         SET d.claim_id                = s.claim_id,
             d.calculator_criterion_id = s.calculator_criterion_id,
             d.selected_rating_id      = s.selected_rating_id,
             d.rating_value            = s.rating_value,
             d.criterion_weight        = s.criterion_weight,
             d.sequence_num            = s.sequence_num,
             d.date_created            = s.date_created,
             d.created_by              = s.created_by,
             d.date_modified           = s.date_modified,
             d.modified_by             = s.modified_by,
             d.version                 = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.calculator_response_id, 
                d.claim_id, 
                d.calculator_criterion_id, 
                d.selected_rating_id, 
                d.rating_value, 
                d.criterion_weight, 
                d.sequence_num, 
                d.date_created, 
                d.created_by, 
                d.date_modified, 
                d.modified_by, 
                d.version
              )
      VALUES (  s.calculator_response_id, 
                s.claim_id, 
                s.calculator_criterion_id, 
                s.selected_rating_id, 
                s.rating_value, 
                s.criterion_weight, 
                s.sequence_num, 
                s.date_created, 
                s.created_by, 
                s.date_modified, 
                s.modified_by, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE calculator_payout';
  v_tbl_name := 'calculator_payout';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO calculator_payout d
       USING (SELECT calculator_payout_id, 
                     calculator_id, 
                     low_score, 
                     high_score, 
                     low_award, 
                     high_award, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM calculator_payout_g4
               ORDER BY calculator_payout_id
              ) s
          ON (d.calculator_payout_id = s.calculator_payout_id)
        WHEN MATCHED THEN UPDATE 
         SET d.calculator_id        = s.calculator_id,
             d.low_score            = s.low_score,
             d.high_score           = s.high_score,
             d.low_award            = s.low_award,
             d.high_award           = s.high_award,
             d.created_by           = s.created_by,
             d.date_created         = s.date_created,
             d.modified_by          = s.modified_by,
             d.date_modified        = s.date_modified,
             d.version              = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.calculator_payout_id, 
                d.calculator_id, 
                d.low_score, 
                d.high_score, 
                d.low_award, 
                d.high_award, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.calculator_payout_id, 
                s.calculator_id, 
                s.low_score, 
                s.high_score, 
                s.low_award, 
                s.high_award, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  --Recognition related tables start
  v_stage      := 'MERGE scheduled_recognition';
  v_tbl_name := 'scheduled_recognition';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO scheduled_recognition d
       USING (SELECT scheduled_recog_id, 
                     trigger_name, 
                     trigger_group, 
                     promotion_id, 
                     sender_id, 
                     recipient_id, 
                     is_fired, 
                     delivery_date, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM scheduled_recognition_g4
               ORDER BY scheduled_recog_id
              ) s
          ON (d.scheduled_recog_id = s.scheduled_recog_id)
        WHEN MATCHED THEN UPDATE 
         SET d.trigger_name       = s.trigger_name,
             d.trigger_group      = s.trigger_group,
             d.promotion_id       = s.promotion_id,
             d.sender_id          = s.sender_id,
             d.recipient_id       = s.recipient_id,
             d.is_fired           = s.is_fired,
             d.delivery_date      = s.delivery_date,
             d.created_by         = s.created_by,
             d.date_created       = s.date_created,
             d.modified_by        = s.modified_by,
             d.date_modified      = s.date_modified,
             d.version            = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.scheduled_recog_id, 
              d.trigger_name, 
              d.trigger_group, 
              d.promotion_id, 
              d.sender_id, 
              d.recipient_id, 
              d.is_fired, 
              d.delivery_date, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.scheduled_recog_id, 
              s.trigger_name, 
              s.trigger_group, 
              s.promotion_id, 
              s.sender_id, 
              s.recipient_id, 
              s.is_fired, 
              s.delivery_date, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  
  
  v_stage      := 'MERGE purl_recipient';
  v_tbl_name := 'purl_recipient';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO purl_recipient d
       USING (SELECT purl_recipient_id, 
                     promotion_id, 
                     user_id, 
                     node_id, 
                     invitation_start_date, 
                     award_date, 
                     award_level_id, 
                     award_amount, 
                     state, 
                     claim_id, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version, 
                     is_show_default_contributors
                FROM purl_recipient_g4
               ORDER BY purl_recipient_id
              ) s
          ON (d.purl_recipient_id = s.purl_recipient_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id                 = s.promotion_id,
             d.user_id                      = s.user_id,
             d.node_id                      = s.node_id,
             d.invitation_start_date        = s.invitation_start_date,
             d.award_date                   = s.award_date,
             d.award_level_id               = s.award_level_id,
             d.award_amount                 = s.award_amount,
             d.state                        = s.state,
             d.claim_id                     = s.claim_id,
             d.created_by                   = s.created_by,
             d.date_created                 = s.date_created,
             d.modified_by                  = s.modified_by,
             d.date_modified                = s.date_modified,
             d.version                      = s.version,
             d.is_show_default_contributors = s.is_show_default_contributors
        WHEN NOT MATCHED THEN INSERT
             (  d.purl_recipient_id, 
                d.promotion_id, 
                d.user_id, 
                d.node_id, 
                d.invitation_start_date, 
                d.award_date, 
                d.award_level_id, 
                d.award_amount, 
                d.state, 
                d.claim_id, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version, 
                d.is_show_default_contributors
              )
      VALUES (  s.purl_recipient_id, 
                s.promotion_id, 
                s.user_id, 
                s.node_id, 
                s.invitation_start_date, 
                s.award_date, 
                s.award_level_id, 
                s.award_amount, 
                s.state, 
                s.claim_id, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version, 
                s.is_show_default_contributors
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE purl_recipient_cfse';
  v_tbl_name := 'purl_recipient_cfse';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

     v_sql := 
      'MERGE INTO purl_recipient_cfse d
       USING (SELECT purl_recipient_cfse_id, 
                     claim_form_step_element_id, 
                     purl_recipient_id, 
                     value, 
                     sequence_num, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM purl_recipient_cfse_g4
               ORDER BY purl_recipient_cfse_id
              ) s
          ON (d.purl_recipient_cfse_id = s.purl_recipient_cfse_id)
        WHEN MATCHED THEN UPDATE 
         SET d.claim_form_step_element_id = s.claim_form_step_element_id,
             d.purl_recipient_id          = s.purl_recipient_id,
             d.value                      = s.value,
             d.sequence_num               = s.sequence_num,
             d.created_by                 = s.created_by,
             d.date_created               = s.date_created,
             d.modified_by                = s.modified_by,
             d.date_modified              = s.date_modified,
             d.version                    = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.purl_recipient_cfse_id, 
              d.claim_form_step_element_id, 
              d.purl_recipient_id, 
              d.value, 
              d.sequence_num, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.purl_recipient_cfse_id, 
              s.claim_form_step_element_id, 
              s.purl_recipient_id, 
              s.value, 
              s.sequence_num, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE purl_contributor';
  v_tbl_name := 'purl_contributor';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO purl_contributor d
       USING (SELECT purl_contributor_id, 
                     purl_recipient_id, 
                     user_id, 
                     state, 
                     first_name, 
                     last_name, 
                     email_addr, 
                     invite_contributor_id, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version, 
                     avatar_url, 
                     avatar_state, 
                     is_send_later
                FROM purl_contributor_g4
               ORDER BY purl_contributor_id
              ) s
          ON (d.purl_contributor_id = s.purl_contributor_id)
        WHEN MATCHED THEN UPDATE 
         SET d.purl_recipient_id     = s.purl_recipient_id,
             d.user_id               = s.user_id,
             d.state                 = s.state,
             d.first_name            = s.first_name,
             d.last_name             = s.last_name,
             d.email_addr            = s.email_addr,
             d.invite_contributor_id = s.invite_contributor_id,
             d.created_by            = s.created_by,
             d.date_created          = s.date_created,
             d.modified_by           = s.modified_by,
             d.date_modified         = s.date_modified,
             d.version               = s.version,
             d.avatar_url            = s.avatar_url,
             d.avatar_state          = s.avatar_state,
             d.is_send_later         = s.is_send_later
        WHEN NOT MATCHED THEN INSERT
             (  d.purl_contributor_id, 
                d.purl_recipient_id, 
                d.user_id, 
                d.state, 
                d.first_name, 
                d.last_name, 
                d.email_addr, 
                d.invite_contributor_id, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version, 
                d.avatar_url, 
                d.avatar_state, 
                d.is_send_later
              )
      VALUES (  s.purl_contributor_id, 
                s.purl_recipient_id, 
                s.user_id, 
                s.state, 
                s.first_name, 
                s.last_name, 
                s.email_addr, 
                s.invite_contributor_id, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version, 
                s.avatar_url, 
                s.avatar_state, 
                s.is_send_later
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE purl_contributor_comment';
  v_tbl_name := 'purl_contributor_comment';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO purl_contributor_comment d
       USING (SELECT purl_contributor_comment_id, 
                     purl_contributor_id, 
                     comments, 
                     sequence_num, 
                     status, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM purl_contributor_comment_g4
               ORDER BY purl_contributor_comment_id
              ) s
          ON (d.purl_contributor_comment_id = s.purl_contributor_comment_id)
        WHEN MATCHED THEN UPDATE 
         SET d.purl_contributor_id         = s.purl_contributor_id,
             d.comments                    = s.comments,
             d.sequence_num                = s.sequence_num,
             d.status                      = s.status,
             d.created_by                  = s.created_by,
             d.date_created                = s.date_created,
             d.modified_by                 = s.modified_by,
             d.date_modified               = s.date_modified,
             d.version                     = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.purl_contributor_comment_id, 
                d.purl_contributor_id, 
                d.comments, 
                d.sequence_num, 
                d.status,
                d.Comments_lang_id, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.purl_contributor_comment_id, 
                s.purl_contributor_id, 
                s.comments, 
                s.sequence_num, 
                s.status,
                ''en_US'', 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE purl_contributor_media';
  v_tbl_name := 'purl_contributor_media';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO purl_contributor_media d
       USING (SELECT purl_contributor_media_id, 
                     purl_contributor_id, 
                     type, 
                     caption, 
                     url, 
                     url_thumb, 
                     sequence_num, 
                     status, 
                     state, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM purl_contributor_media_g4
               ORDER BY purl_contributor_media_id
              ) s
          ON (d.purl_contributor_media_id = s.purl_contributor_media_id)
        WHEN MATCHED THEN UPDATE 
         SET d.purl_contributor_id       = s.purl_contributor_id,
             d.type                      = s.type,
             d.caption                   = s.caption,
             d.url                       = s.url,
             d.url_thumb                 = s.url_thumb,
             d.sequence_num              = s.sequence_num,
             d.status                    = s.status,
             d.state                     = s.state,
             d.created_by                = s.created_by,
             d.date_created              = s.date_created,
             d.modified_by               = s.modified_by,
             d.date_modified             = s.date_modified,
             d.version                   = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.purl_contributor_media_id, 
                d.purl_contributor_id, 
                d.type, 
                d.caption, 
                d.url, 
                d.url_thumb, 
                d.sequence_num, 
                d.status, 
                d.state, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.purl_contributor_media_id, 
                s.purl_contributor_id, 
                s.type, 
                s.caption, 
                s.url, 
                s.url_thumb, 
                s.sequence_num, 
                s.status, 
                s.state, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              )';
    
    EXECUTE IMMEDIATE v_sql;  
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  --Recognition related tables ends
  

  --Product claim related tables starts 
/*   --Product Claim module not required to migrate data from G4 to G5 in 1.1 release
  v_stage    := 'MERGE promo_payout_group';
  v_tbl_name := 'promo_payout_group';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO promo_payout_group d
       USING (SELECT promo_payout_group_id, 
                     guid, 
                     promotion_id, 
                     sequence_num, 
                     parent_promo_payout_group_id, 
                     quantity, 
                     submitter_payout, 
                     team_member_payout, 
                     minimum_qualifier, 
                     retro_payout, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM promo_payout_group_g4
               ORDER BY promo_payout_group_id
              ) s
          ON (d.promo_payout_group_id = s.promo_payout_group_id)
        WHEN MATCHED THEN UPDATE 
         SET d.guid                         = s.guid,
             d.promotion_id                 = s.promotion_id,
             d.sequence_num                 = s.sequence_num,
             d.parent_promo_payout_group_id = s.parent_promo_payout_group_id,
             d.quantity                     = s.quantity,
             d.submitter_payout             = s.submitter_payout,
             d.team_member_payout           = s.team_member_payout,
             d.minimum_qualifier            = s.minimum_qualifier,
             d.retro_payout                 = s.retro_payout,
             d.created_by                   = s.created_by,
             d.date_created                 = s.date_created,
             d.modified_by                  = s.modified_by,
             d.date_modified                = s.date_modified,
             d.version                      = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.promo_payout_group_id, 
              d.guid, 
              d.promotion_id, 
              d.sequence_num, 
              d.parent_promo_payout_group_id, 
              d.quantity, 
              d.submitter_payout, 
              d.team_member_payout, 
              d.minimum_qualifier, 
              d.retro_payout, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.promo_payout_group_id, 
              s.guid, 
              s.promotion_id, 
              s.sequence_num, 
              s.parent_promo_payout_group_id, 
              s.quantity, 
              s.submitter_payout, 
              s.team_member_payout, 
              s.minimum_qualifier, 
              s.retro_payout, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;

  v_stage    := 'MERGE promo_payout';
  v_tbl_name := 'promo_payout';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO promo_payout d
       USING (SELECT promo_payout_id, 
                     promo_payout_group_id, 
                     sequence_num, 
                     product_category_id, 
                     product_id, 
                     quantity, 
                     product_or_category_start_date, 
                     product_or_category_end_date, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM promo_payout_g4
               ORDER BY promo_payout_id
              ) s
          ON (d.promo_payout_id = s.promo_payout_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promo_payout_group_id          = s.promo_payout_group_id,
             d.sequence_num                   = s.sequence_num,
             d.product_category_id            = s.product_category_id,
             d.product_id                     = s.product_id,
             d.quantity                       = s.quantity,
             d.product_or_category_start_date = s.product_or_category_start_date,
             d.product_or_category_end_date   = s.product_or_category_end_date,
             d.created_by                     = s.created_by,
             d.date_created                   = s.date_created,
             d.modified_by                    = s.modified_by,
             d.date_modified                  = s.date_modified,
             d.version                        = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.promo_payout_id, 
              d.promo_payout_group_id, 
              d.sequence_num, 
              d.product_category_id, 
              d.product_id, 
              d.quantity, 
              d.product_or_category_start_date, 
              d.product_or_category_end_date, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.promo_payout_id, 
              s.promo_payout_group_id, 
              s.sequence_num, 
              s.product_category_id, 
              s.product_id, 
              s.quantity, 
              s.product_or_category_start_date, 
              s.product_or_category_end_date, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE promo_team_position';
  v_tbl_name := 'promo_team_position';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO promo_team_position d
       USING (SELECT promo_team_position_id, 
                     promotion_id, 
                     team_position_type, 
                     is_required, 
                     created_by, 
                     date_created, 
                     version
                FROM promo_team_position_g4
               ORDER BY promo_team_position_id
              ) s
          ON (d.promo_team_position_id = s.promo_team_position_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promotion_id           = s.promotion_id,
             d.team_position_type     = s.team_position_type,
             d.is_required            = s.is_required,
             d.created_by             = s.created_by,
             d.date_created           = s.date_created,
             d.version                = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.promo_team_position_id, 
              d.promotion_id, 
              d.team_position_type, 
              d.is_required, 
              d.created_by, 
              d.date_created, 
              d.version
              )
      VALUES (s.promo_team_position_id, 
              s.promotion_id, 
              s.team_position_type, 
              s.is_required, 
              s.created_by, 
              s.date_created, 
              s.version
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE min_qualifier_status';
  v_tbl_name := 'min_qualifier_status';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO min_qualifier_status d
       USING (SELECT min_qualifier_status_id, 
                     submitter_id, 
                     promotion_payout_group_id, 
                     min_qualifier_met, 
                     completed_quantity, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM min_qualifier_status_g4
               ORDER BY min_qualifier_status_id
              ) s
          ON (d.min_qualifier_status_id = s.min_qualifier_status_id)
        WHEN MATCHED THEN UPDATE 
         SET d.submitter_id              = s.submitter_id,
             d.promotion_payout_group_id = s.promotion_payout_group_id,
             d.min_qualifier_met         = s.min_qualifier_met,
             d.completed_quantity        = s.completed_quantity,
             d.created_by                = s.created_by,
             d.date_created              = s.date_created,
             d.modified_by               = s.modified_by,
             d.date_modified             = s.date_modified,
             d.version                   = s.version 
        WHEN NOT MATCHED THEN INSERT
             (d.min_qualifier_status_id, 
              d.submitter_id, 
              d.promotion_payout_group_id, 
              d.min_qualifier_met, 
              d.completed_quantity, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.min_qualifier_status_id, 
              s.submitter_id, 
              s.promotion_payout_group_id, 
              s.min_qualifier_met, 
              s.completed_quantity, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE stack_rank_payout_group';
  v_tbl_name := 'stack_rank_payout_group';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql := 
      'MERGE INTO stack_rank_payout_group d
       USING (SELECT stack_rank_payout_group_id, 
                     node_type_id, 
                     promotion_id, 
                     submitter_to_rank_type, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM stack_rank_payout_group_g4
               ORDER BY stack_rank_payout_group_id
              ) s
          ON (d.stack_rank_payout_group_id = s.stack_rank_payout_group_id)
        WHEN MATCHED THEN UPDATE 
         SET d.node_type_id               = s.node_type_id,
             d.promotion_id               = s.promotion_id,
             d.submitter_to_rank_type     = s.submitter_to_rank_type,
             d.created_by                 = s.created_by,
             d.date_created               = s.date_created,
             d.modified_by                = s.modified_by,
             d.date_modified              = s.date_modified,
             d.version                    = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.stack_rank_payout_group_id, 
              d.node_type_id, 
              d.promotion_id, 
              d.submitter_to_rank_type, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.stack_rank_payout_group_id, 
              s.node_type_id, 
              s.promotion_id, 
              s.submitter_to_rank_type, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE stack_rank_payout';
  v_tbl_name := 'stack_rank_payout';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=   
      'MERGE INTO stack_rank_payout d
       USING (SELECT stack_rank_payout_id, 
                     stack_rank_payout_group_id, 
                     start_rank, 
                     end_rank, 
                     payout, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM stack_rank_payout_g4
               ORDER BY stack_rank_payout_id
              ) s
          ON (d.stack_rank_payout_id = s.stack_rank_payout_id)
        WHEN MATCHED THEN UPDATE 
         SET d.stack_rank_payout_group_id = s.stack_rank_payout_group_id,
             d.start_rank                 = s.start_rank,
             d.end_rank                   = s.end_rank,
             d.payout                     = s.payout,
             d.created_by                 = s.created_by,
             d.date_created               = s.date_created,
             d.modified_by                = s.modified_by,
             d.date_modified              = s.date_modified,
             d.version                    = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.stack_rank_payout_id, 
              d.stack_rank_payout_group_id, 
              d.start_rank, 
              d.end_rank, 
              d.payout, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.stack_rank_payout_id, 
              s.stack_rank_payout_group_id, 
              s.start_rank, 
              s.end_rank, 
              s.payout, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              )';

    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  --Product claim related tables end
*/

  --Activity Journal related tables start
  v_stage      := 'MERGE activity';
  v_tbl_name := 'activity';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO activity d
       USING (SELECT a_g4.activity_id, 
                     a_g4.user_id, 
                     a_g4.product_id, 
                     a_g4.node_id, 
                     a_g4.promotion_id, 
                     a_g4.claim_id, 
                     a_g4.quantity, 
                     a_g4.submission_date, 
                     a_g4.is_posted, 
                     a_g4.guid, 
                     a_g4.created_by, 
                     a_g4.date_created, 
                     a_g4.modified_by, 
                     a_g4.date_modified, 
                     a_g4.version, 
                     a_g4.is_carryover, 
                     a_g4.activity_discrim, 
                     a_g4.mo_submitter_payout, 
                     a_g4.is_submitter, 
                     a_g4.min_qualifier_status_id, 
                     a_g4.sweepstake_drawing_id, 
                     a_g4.stack_rank_participant_id, 
                     a_g4.claim_group_id, 
                     a_g4.merch_order_id, 
                     a_g4.gift_code_issue_date, 
                     a_g4.comments, 
                     a_g4.date_posted, 
                     a_g4.is_primary, 
                     a_g4.revenue, 
                     a_g4.award_quantity, 
                     a_g4.ref_payout_recipient, 
                     a_g4.ref_payout_instance, 
                     a_g4.promo_merch_program_level_id
                FROM activity_g4 a_g4,
                     promotion
                WHERE a_g4.promotion_id = promotion.promotion_id
               ORDER BY activity_id
              ) s
          ON (d.activity_id = s.activity_id)
        WHEN MATCHED THEN UPDATE 
         SET d.user_id                      = s.user_id,
             d.product_id                   = s.product_id,
             d.node_id                      = s.node_id,
             d.promotion_id                 = s.promotion_id,
             d.claim_id                     = s.claim_id,
             d.quantity                     = s.quantity,
             d.submission_date              = s.submission_date,
             d.is_posted                    = s.is_posted,
             d.guid                         = s.guid,
             d.created_by                   = s.created_by,
             d.date_created                 = s.date_created,
             d.modified_by                  = s.modified_by,
             d.date_modified                = s.date_modified,
             d.version                      = s.version,
             d.is_carryover                 = s.is_carryover,
             d.activity_discrim             = s.activity_discrim,
             d.mo_submitter_payout          = s.mo_submitter_payout,
             d.is_submitter                 = s.is_submitter,
             d.min_qualifier_status_id      = s.min_qualifier_status_id,
             d.sweepstake_drawing_id        = s.sweepstake_drawing_id,
             d.stack_rank_participant_id    = s.stack_rank_participant_id,
             d.claim_group_id               = s.claim_group_id,
             d.merch_order_id               = s.merch_order_id,
             d.gift_code_issue_date         = s.gift_code_issue_date,
             d.comments                     = s.comments,
             d.date_posted                  = s.date_posted,
             d.is_primary                   = s.is_primary,
             d.revenue                      = s.revenue,
             d.award_quantity               = s.award_quantity,
             d.ref_payout_recipient         = s.ref_payout_recipient,
             d.ref_payout_instance          = s.ref_payout_instance,
             d.promo_merch_program_level_id = s.promo_merch_program_level_id
        WHEN NOT MATCHED THEN INSERT
             (  d.activity_id, 
                d.user_id, 
                d.product_id, 
                d.node_id, 
                d.promotion_id, 
                d.claim_id, 
                d.quantity, 
                d.submission_date, 
                d.is_posted, 
                d.guid, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version, 
                d.is_carryover, 
                d.activity_discrim, 
                d.mo_submitter_payout, 
                d.is_submitter, 
                d.min_qualifier_status_id, 
                d.sweepstake_drawing_id, 
                d.stack_rank_participant_id, 
                d.claim_group_id, 
                d.merch_order_id, 
                d.gift_code_issue_date, 
                d.comments, 
                d.date_posted, 
                d.is_primary, 
                d.revenue, 
                d.award_quantity, 
                d.ref_payout_recipient, 
                d.ref_payout_instance, 
                d.promo_merch_program_level_id
              )
      VALUES (  s.activity_id, 
                s.user_id, 
                s.product_id, 
                s.node_id, 
                s.promotion_id, 
                s.claim_id, 
                s.quantity, 
                s.submission_date, 
                s.is_posted, 
                s.guid, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version, 
                s.is_carryover, 
                s.activity_discrim, 
                s.mo_submitter_payout, 
                s.is_submitter, 
                s.min_qualifier_status_id, 
                s.sweepstake_drawing_id, 
                s.stack_rank_participant_id, 
                s.claim_group_id, 
                s.merch_order_id, 
                s.gift_code_issue_date, 
                s.comments, 
                s.date_posted, 
                s.is_primary, 
                s.revenue, 
                s.award_quantity, 
                s.ref_payout_recipient, 
                s.ref_payout_instance, 
                s.promo_merch_program_level_id
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE activity_merch_order';
  v_tbl_name := 'activity_merch_order';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_sql :=
      'MERGE INTO activity_merch_order d
       USING (SELECT amo_g4.activity_id, 
                     amo_g4.merch_order_id, 
                     amo_g4.date_created, 
                     amo_g4.created_by
                FROM activity_merch_order_g4 amo_g4,
                     activity a
                WHERE a.activity_id= amo_g4.activity_id
               ORDER BY activity_id
              ) s
          ON (d.activity_id    = s.activity_id AND
              d.merch_order_id = s.merch_order_id
              )
        WHEN MATCHED THEN UPDATE 
         SET d.date_created   = s.date_created,
             d.created_by     = s.created_by
        WHEN NOT MATCHED THEN INSERT
             (  d.activity_id, 
                d.merch_order_id, 
                d.date_created, 
                d.created_by
              )
      VALUES (  s.activity_id, 
                s.merch_order_id, 
                s.date_created, 
                s.created_by
              )';
    
    EXECUTE IMMEDIATE v_sql;
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE budget';
  v_tbl_name := 'budget';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO budget d
       USING (SELECT budget_id, 
                     budget_master_id, 
                     user_id, 
                     node_id, 
                     original_value, 
                     current_value, 
                     overdrawn, 
                     status, 
                     start_date, 
                     end_date, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version, 
                     effective_date
                FROM budget_g4
               ORDER BY budget_id
              ) s
          ON (d.budget_id = s.budget_id)
        WHEN MATCHED THEN UPDATE 
         SET d.budget_master_id = s.budget_master_id,
             d.user_id          = s.user_id,
             d.node_id          = s.node_id,
             d.original_value   = s.original_value,
             d.current_value    = s.current_value,
             d.overdrawn        = s.overdrawn,
             d.status           = s.status,
             d.start_date       = s.start_date,
             d.end_date         = s.end_date,
             d.created_by       = s.created_by,
             d.date_created     = s.date_created,
             d.modified_by      = s.modified_by,
             d.date_modified    = s.date_modified,
             d.version          = s.version,
             d.effective_date   = s.effective_date
        WHEN NOT MATCHED THEN INSERT
             (  d.budget_id, 
                d.budget_master_id, 
                d.user_id, 
                d.node_id, 
                d.original_value, 
                d.current_value, 
                d.overdrawn, 
                d.status, 
                d.start_date, 
                d.end_date, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version, 
                d.effective_date
              )
      VALUES (  s.budget_id, 
                s.budget_master_id, 
                s.user_id, 
                s.node_id, 
                s.original_value, 
                s.current_value, 
                s.overdrawn, 
                s.status, 
                s.start_date, 
                s.end_date, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version, 
                s.effective_date
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
  
  
  v_stage      := 'MERGE journal';
  v_tbl_name := 'journal';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO journal d
       USING (SELECT j.journal_id, 
                     j.user_id, 
                     j.budget_id, 
                     j.promotion_id, 
                     j.transaction_date, 
                     j.transaction_type, 
                     j.transaction_amt, 
                     j.transaction_description, 
                     fnc_java_encrypt(pej.gift_code) gift_code, 
                     fnc_java_encrypt(pej.gift_code_pin) gift_code_pin, 
                     fnc_java_encrypt(pej.awardbanq_nbr) awardbanq_nbr, 
                     j.account_balance, 
                     j.comments, 
                     j.journal_type, 
                     j.guid, 
                     j.status_type, 
                     j.reason_type, 
                     j.primary_billing_code, 
                     j.secondary_billing_code, 
                     j.created_by, 
                     j.date_created, 
                     j.modified_by, 
                     j.date_modified, 
                     j.version, 
                     j.is_reverse, 
                     j.is_redeemed, 
                     j.budget_value
                FROM journal_g4 j,
                     prep_encrypt_journal_g4 pej                     
               WHERE j.journal_id = pej.journal_id (+)
               AND NOT EXISTS (SELECT * FROM promotion_g4 WHERE promotion_id = j.promotion_id AND promotion_type IN ('survey','wellness','goalquest','challengepoint'))
               ORDER BY journal_id
              ) s
          ON (d.journal_id = s.journal_id)
        WHEN MATCHED THEN UPDATE 
         SET d.user_id                 = s.user_id,
             d.budget_id               = s.budget_id,
             d.promotion_id            = s.promotion_id,
             d.transaction_date        = s.transaction_date,
             d.transaction_type        = s.transaction_type,
             d.transaction_amt         = s.transaction_amt,
             d.transaction_description = s.transaction_description,
             d.gift_code               = s.gift_code,
             d.gift_code_pin           = s.gift_code_pin,
             d.awardbanq_nbr           = s.awardbanq_nbr,
             d.account_balance         = s.account_balance,
             d.comments                = s.comments,
             d.journal_type            = s.journal_type,
             d.guid                    = s.guid,
             d.status_type             = s.status_type,
             d.reason_type             = s.reason_type,
             d.primary_billing_code    = s.primary_billing_code,
             d.secondary_billing_code  = s.secondary_billing_code,
             d.created_by              = s.created_by,
             d.date_created            = s.date_created,
             d.modified_by             = s.modified_by,
             d.date_modified           = s.date_modified,
             d.version                 = s.version,
             d.is_reverse              = s.is_reverse,
             d.is_redeemed             = s.is_redeemed,
             d.budget_value            = s.budget_value
        WHEN NOT MATCHED THEN INSERT
             (  d.journal_id, 
                d.user_id, 
                d.budget_id, 
                d.promotion_id, 
                d.transaction_date, 
                d.transaction_type, 
                d.transaction_amt, 
                d.transaction_description, 
                d.gift_code, 
                d.gift_code_pin, 
                d.awardbanq_nbr, 
                d.account_balance, 
                d.comments, 
                d.journal_type, 
                d.guid, 
                d.status_type, 
                d.reason_type, 
                d.primary_billing_code, 
                d.secondary_billing_code, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version, 
                d.is_reverse, 
                d.is_redeemed, 
                d.budget_value
              )
      VALUES (  s.journal_id, 
                s.user_id, 
                s.budget_id, 
                s.promotion_id, 
                s.transaction_date, 
                s.transaction_type, 
                s.transaction_amt, 
                s.transaction_description, 
                s.gift_code, 
                s.gift_code_pin, 
                s.awardbanq_nbr, 
                s.account_balance, 
                s.comments, 
                s.journal_type, 
                s.guid, 
                s.status_type, 
                s.reason_type, 
                s.primary_billing_code, 
                s.secondary_billing_code, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version, 
                s.is_reverse, 
                s.is_redeemed, 
                s.budget_value
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage      := 'MERGE activity_journal';
  v_tbl_name := 'activity_journal';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO activity_journal d
       USING (SELECT activity_id, 
                     journal_id, 
                     date_created, 
                     created_by
                FROM activity_journal_g4
                WHERE EXISTS (SELECT * FROM activity WHERE activity_id = activity_journal_g4.activity_id)
               ORDER BY activity_id
              ) s
          ON (d.activity_id = s.activity_id  AND
              d.journal_id  = s.journal_id)
        WHEN MATCHED THEN UPDATE 
         SET d.date_created = s.date_created,
             d.created_by   = s.created_by
        WHEN NOT MATCHED THEN INSERT
             (  d.activity_id, 
                d.journal_id, 
                d.date_created, 
                d.created_by
              )
      VALUES (  s.activity_id, 
                s.journal_id, 
                s.date_created, 
                s.created_by
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE payout_calculation_audit';
  v_tbl_name := 'payout_calculation_audit';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO payout_calculation_audit d
       USING (SELECT payout_calculation_audit_id, 
                     discriminator, 
                     reason_type, 
                     reason_text, 
                     journal_id, 
                     participant_id, 
                     claim_id, 
                     claim_group_id, 
                     promo_payout_group_id, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM payout_calculation_audit_g4
                WHERE EXISTS (SELECT * FROM claim WHERE claim_id = payout_calculation_audit_g4.claim_id)
               ORDER BY payout_calculation_audit_id
              ) s
          ON (d.payout_calculation_audit_id = s.payout_calculation_audit_id)
        WHEN MATCHED THEN UPDATE 
         SET d.discriminator               = s.discriminator,
             d.reason_type                 = s.reason_type,
             d.reason_text                 = s.reason_text,
             d.journal_id                  = s.journal_id,
             d.participant_id              = s.participant_id,
             d.claim_id                    = s.claim_id,
             d.claim_group_id              = s.claim_group_id,
             d.promo_payout_group_id       = s.promo_payout_group_id,
             d.created_by                  = s.created_by,
             d.date_created                = s.date_created,
             d.modified_by                 = s.modified_by,
             d.date_modified               = s.date_modified,
             d.version                     = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.payout_calculation_audit_id, 
              d.discriminator, 
              d.reason_type, 
              d.reason_text, 
              d.journal_id, 
              d.participant_id, 
              d.claim_id, 
              d.claim_group_id, 
              d.promo_payout_group_id, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.payout_calculation_audit_id, 
              s.discriminator, 
              s.reason_type, 
              s.reason_text, 
              s.journal_id, 
              s.participant_id, 
              s.claim_id, 
              s.claim_group_id, 
              s.promo_payout_group_id, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              );

    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
 --Activity Journal related tables end


  v_stage    := 'MERGE mock_account_transaction';
  v_tbl_name := 'mock_account_transaction';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO mock_account_transaction d
       USING (SELECT account_transaction_id, 
                     account_number, 
                     transaction_date, 
                     transaction_type, 
                     transaction_amount, 
                     transaction_description, 
                     account_balance, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM mock_account_transaction_g4
               ORDER BY account_transaction_id
              ) s
          ON (d.account_transaction_id = s.account_transaction_id)
        WHEN MATCHED THEN UPDATE 
         SET d.account_number          = s.account_number,
             d.transaction_date        = s.transaction_date,
             d.transaction_type        = s.transaction_type,
             d.transaction_amount      = s.transaction_amount,
             d.transaction_description = s.transaction_description,
             d.account_balance         = s.account_balance,
             d.created_by              = s.created_by,
             d.date_created            = s.date_created,
             d.modified_by             = s.modified_by,
             d.date_modified           = s.date_modified,
             d.version                 = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.account_transaction_id, 
              d.account_number, 
              d.transaction_date, 
              d.transaction_type, 
              d.transaction_amount, 
              d.transaction_description, 
              d.account_balance, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.account_transaction_id, 
              s.account_number, 
              s.transaction_date, 
              s.transaction_type, 
              s.transaction_amount, 
              s.transaction_description, 
              s.account_balance, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE promo_approval_option_reason';
  v_tbl_name := 'promo_approval_option_reason';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO promo_approval_option_reason d
       USING (SELECT promo_apprvl_option_reason_id, 
                     promo_approval_option_id, 
                     approval_option_reason, 
                     created_by, 
                     date_created, 
                     version
                FROM promo_approval_option_reaso_g4
               ORDER BY promo_apprvl_option_reason_id
              ) s
          ON (d.promo_apprvl_option_reason_id = s.promo_apprvl_option_reason_id)
        WHEN MATCHED THEN UPDATE 
         SET d.promo_approval_option_id      = s.promo_approval_option_id,
             d.approval_option_reason        = s.approval_option_reason,
             d.created_by                    = s.created_by,
             d.date_created                  = s.date_created,
             d.version                       = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.promo_apprvl_option_reason_id, 
              d.promo_approval_option_id, 
              d.approval_option_reason, 
              d.created_by, 
              d.date_created, 
              d.version
              )
      VALUES (s.promo_apprvl_option_reason_id, 
              s.promo_approval_option_id, 
              s.approval_option_reason, 
              s.created_by, 
              s.date_created, 
              s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;


  v_stage    := 'MERGE welcome_message';
  v_tbl_name := 'welcome_message';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO welcome_message d
       USING (SELECT welcome_message_id, 
                     message_id, 
                     notification_date, 
                     created_by, 
                     date_created, 
                     version
                FROM welcome_message_g4
               ORDER BY welcome_message_id
              ) s
          ON (d.welcome_message_id = s.welcome_message_id)
        WHEN MATCHED THEN UPDATE 
         SET d.message_id         = s.message_id,
             d.notification_date  = s.notification_date,
             d.created_by         = s.created_by,
             d.date_created       = s.date_created,
             d.version            = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.welcome_message_id, 
              d.message_id, 
              d.notification_date, 
              d.created_by, 
              d.date_created, 
              d.version
              )
      VALUES (s.welcome_message_id, 
              s.message_id, 
              s.notification_date, 
              s.created_by, 
              s.date_created, 
              s.version
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;

 
  v_stage    := 'MERGE welcome_message_audience';
  v_tbl_name := 'welcome_message_audience';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO welcome_message_audience d
       USING (SELECT welcome_message_id,
                     audience_id
                FROM welcome_message_audience_g4
               ORDER BY welcome_message_id
              ) s
          ON (d.welcome_message_id = s.welcome_message_id AND
              d.audience_id  = s.audience_id)
        WHEN NOT MATCHED THEN INSERT
             (d.welcome_message_id, 
              d.audience_id
              )
      VALUES (s.welcome_message_id, 
              s.audience_id
              );
    show_msg_count(v_stage, SQL%ROWCOUNT);

  ELSE
    dbms_output.put_line(v_msg_tbl_nf||v_tbl_name);
    p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_msg_tbl_nf||v_tbl_name,
                          NULL);
  END IF;
    
  
  v_exe_log_msg := 'Procedure completed.';
  p_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                        v_exe_log_msg,
                        NULL);
                        
 BEGIN                        
    pkg_build_audience.prc_refresh_criteria_audience (NULL, po_return_code);          
 END;                       
  COMMIT;
  po_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    po_return_code := 99;
    v_exe_log_msg := 'Error at stage:'||v_stage||' message:'||SQLERRM;
    p_execution_log_entry(C_process_name,C_release_level,C_severity_e,
                          v_exe_log_msg,
                          NULL);
    COMMIT;
END;
/

DECLARE
  po_return_code  NUMBER;
BEGIN
  prc_tier_2_data_convert ('SourcePrefix',po_return_code);
  dbms_output.put_line('Return code:'||po_return_code);
END;
/