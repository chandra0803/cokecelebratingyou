CREATE OR REPLACE PROCEDURE prc_budget_segment_insert_cms 
   (pi_budget_segment_name          IN  VARCHAR2,
    po_name_asset_code_id  IN  NUMBER)
  IS

  /*----------------------------------------------------------------------------
    Purpose: Create cms records for budget_segment.cm_asset_code
  
    Modification History
    Person        Date         Comments
    -----------   ----------   -------------------------------------------------
    Ravi Dhanekula 07/29/2016   Initial creation
  
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
     WHERE name = '_BudgetPeriodNameData';
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
                  '_BudgetPeriodNameData', 
                  '_BudgetPeriodNameData', 
                  'T', 
                  'F', 
                  'T', 
                  'T', 
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
                   'BUDGET_PERIOD_NAME', 
                   'Budget Period Name', 
                   'Budget Period Name', 
                   'F', 
                   'F', 
                   'T', 
                   1, 
                   v_cms_asset_type_id, 
                   'STRING', 
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
     WHERE code = 'budget_period_name_data';
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
                   'budget_period_name_data', 
                   'Budget Period Name Data', 
                   'Budget Period Name Data', 
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
                 'budget_period_name_data.budgetperiodname.'||po_name_asset_code_id, 
               --  'New Central Budget Master G5 Demo'||trunc(sysdate), 
               --  'New Central Budget Master G5 Demo'||trunc(sysdate), 
                 NULL, 
                 'T', 
                 v_cms_section_id, 
                 v_cms_asset_type_id, 
                 NULL, 
                 NULL, 
                 NULL, 
                 'A',
                 'core',
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
                 pi_budget_segment_name,
                 NULL,
                 'BUDGET_PERIOD_NAME');

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

----    p_out_return_code:=0;

  EXCEPTION
    WHEN OTHERS THEN
    NULL;
--      p_out_return_code := 99;                     
  END;
/