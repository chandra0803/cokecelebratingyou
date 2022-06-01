CREATE OR REPLACE PROCEDURE prc_tier_1_data_convert
 (pi_old_schema   IN  VARCHAR2,
  po_return_code  OUT NUMBER)
IS

/*-----------------------------------------------------------------------------
  Process Name : PRC_TIER_1_DATA_CONVERT
  Purpose      : Convert Tier 1 data from G4 schema to G5 schema

  Tier 1
  1. System variables (changes to prefix required)
  2. Countries and suppliers
  3. Employer
  4. Hierarchy
  5. Participant information included logins, passwords, roles, and Stored 
     Value Account information.

  
  Modification History
  Person          Date         Comments
  -------------  ----------  -------------------------------------------------
  Arun S         02/19/2012  Initial creation
 Ravi Dhanekula 03/08/2013 Changed process for os_propertyset to include only a particular set of variables and no inserts needed.
                01/31/2014  Fixed the defect # 51348

-----------------------------------------------------------------------------*/

  --Procedure variables
  C_process_name          execution_log.process_name%TYPE  := 'prc_tier_1_data_convert';
  C_release_level         execution_log.release_level%TYPE := '1';
  C_severity_i            execution_log.severity%TYPE      := 'INFO';
  C_severity_e            execution_log.severity%TYPE      := 'ERROR';
  
  v_exe_log_msg           execution_log.text_line%TYPE;
  v_stage                 VARCHAR2(100);
  v_msg                   VARCHAR2(1000);
  v_tbl_name              user_tables.table_name%TYPE;
  v_g4_schema             all_tables.owner%TYPE;
  v_acl_id                acl.acl_id%TYPE;
  v_entity_id             os_propertyset.entity_id%TYPE;
  v_supplier_id           supplier.supplier_id%TYPE;
  v_role_id               role.role_id%TYPE;
  v_role_id_new      role.role_id%TYPE;
  v_dml_cnt               NUMBER;
  v_country_id            country.country_id%TYPE;
  v_country_id_g4         country.country_id%TYPE;
  v_node_type_id          node_type.node_type_id%TYPE;
  v_node_type_id_new      node_type.node_type_id%TYPE;
  v_node_id               node.node_id%TYPE;
  
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
  
  PROCEDURE show_msg_count
   ( i_msg     VARCHAR2,
     i_rec_cnt INTEGER
   ) IS
      lv_rec_cnt  VARCHAR2(10) := TO_CHAR(i_rec_cnt, 'fm9,999,990');
  BEGIN
    dbms_output.put_line(i_msg || RPAD('.', (37 - LENGTH(i_msg)), '.') || RPAD(':', 10-LENGTH(lv_rec_cnt)) || lv_rec_cnt || ' records');
  END;

BEGIN

  prc_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          'Procedure Started for in param pi_old_schema : '||pi_old_schema,
                          NULL);
  COMMIT;

  v_g4_schema := UPPER(pi_old_schema);

  --v_msg := 'Create Synonym in G5 for G4 tables';
  --prc_create_synonym_for_g4_tbl (v_g4_schema);

--1. System variables  START
   /*--Tables--
  acl
  characteristic
  os_propertyset
  encryption_conversion
  message
  */
  v_msg      := 'MERGE acl';
  v_tbl_name := 'acl';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_dml_cnt := 0;
    FOR rec_acl IN (SELECT acl_id, 
                           name, 
                           code, 
                           help_text, 
                           class_name, 
                           is_active, 
                           created_by, 
                           date_created, 
                           modified_by, 
                           date_modified, 
                           version
                      FROM acl_g4
                    )
    LOOP
      BEGIN
        v_msg := 'Check acl';
        SELECT acl_id
          INTO v_acl_id
          FROM acl
         WHERE code = rec_acl.code;
         
        IF v_acl_id <> rec_acl.acl_id THEN
          v_msg := 'Update user_acl'; 
          UPDATE user_acl
             SET acl_id = rec_acl.acl_id
           WHERE acl_id = v_acl_id;
          
          v_msg := 'Update acl';  
          UPDATE acl
             SET acl_id = rec_acl.acl_id
           WHERE acl_id = v_acl_id;
             
        END IF; 
        v_dml_cnt := v_dml_cnt + 1;  
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_msg := 'Insert acl';
          INSERT INTO acl
                     (acl_id, 
                      name, 
                      code, 
                      help_text, 
                      class_name, 
                      is_active, 
                      created_by, 
                      date_created, 
                      modified_by, 
                      date_modified, 
                      version
                      )
              VALUES (rec_acl.acl_id, 
                      rec_acl.name, 
                      rec_acl.code, 
                      rec_acl.help_text, 
                      rec_acl.class_name, 
                      rec_acl.is_active, 
                      rec_acl.created_by, 
                      rec_acl.date_created, 
                      rec_acl.modified_by, 
                      rec_acl.date_modified, 
                      rec_acl.version
                      );
        v_dml_cnt := v_dml_cnt + 1;
      END;
    
    END LOOP; 
    v_msg      := 'MERGE acl';
    show_msg_count(v_msg, v_dml_cnt);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;

  v_msg      := 'MERGE characteristic';
  v_tbl_name := 'characteristic';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO characteristic d
       USING (SELECT characteristic_id, 
                     characteristic_type, 
                     domain_id, 
                     description, 
                     characteristic_data_type, 
                     cm_asset_code, 
                     name_cm_key, 
                     min_value, 
                     max_value, 
                     max_size, 
                     pl_name, 
                     date_start, 
                     date_end, 
                     is_required, 
                     is_active, 
                     is_unique, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM characteristic_g4 
              ) s
          ON (d.characteristic_id = s.characteristic_id)
        WHEN MATCHED THEN UPDATE 
         SET d.characteristic_type      = s.characteristic_type, 
             d.domain_id                = s.domain_id, 
             d.description              = s.description, 
             d.characteristic_data_type = s.characteristic_data_type, 
             d.cm_asset_code            = s.cm_asset_code, 
             d.name_cm_key              = s.name_cm_key, 
             d.min_value                = s.min_value, 
             d.max_value                = s.max_value, 
             d.max_size                 = s.max_size, 
             d.pl_name                  = s.pl_name, 
             d.date_start               = s.date_start, 
             d.date_end                 = s.date_end, 
             d.is_required              = s.is_required, 
             d.is_active                = s.is_active, 
             d.is_unique                = s.is_unique, 
             d.created_by               = s.created_by, 
             d.date_created             = s.date_created, 
             d.modified_by              = s.modified_by, 
             d.date_modified            = s.date_modified, 
             d.version                  = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.characteristic_id, 
              d.characteristic_type, 
              d.domain_id, 
              d.description, 
              d.characteristic_data_type, 
              d.cm_asset_code, 
              d.name_cm_key, 
              d.min_value, 
              d.max_value, 
              d.max_size, 
              d.pl_name, 
              d.date_start, 
              d.date_end, 
              d.is_required, 
              d.is_active, 
              d.is_unique, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.characteristic_id, 
              s.characteristic_type, 
              s.domain_id, 
              s.description, 
              s.characteristic_data_type, 
              s.cm_asset_code, 
              s.name_cm_key, 
              s.min_value, 
              s.max_value, 
              s.max_size, 
              s.pl_name, 
              s.date_start, 
              s.date_end, 
              s.is_required, 
              s.is_active, 
              s.is_unique, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;

  v_msg      := 'MERGE os_propertyset';
  v_tbl_name := 'os_propertyset';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
      
    v_dml_cnt := 0;
    FOR rec_os IN (SELECT entity_id, 
                          entity_name,
                          entity_key, 
                          key_type, 
                          boolean_val, 
                          double_val, 
                          string_val, 
                          long_val, 
                          int_val, 
                          date_val
                     FROM os_propertyset_g4
                    WHERE entity_name IN ('awardbanq.convertcert.used',
'awardbanq.deposit.retry.delay',
'awardbanq.organization.number',
'awardbanq.pax.update.process.batch.size',
'claim.processing.allow.batch',
'discretionary.award.max',
'discretionary.award.min',
'display.table.max.per.multi.page',
'display.table.max.per.single.page',
'email.welcome.loginAndPassword.separate',
'fileload.userrole.delete',
'force.https.dev',
'force.https.preprod',
'force.https.prod',
'force.https.qa',
'goalquest.participant.partners.allowed',
'google.analytics.account',
'import.page.size',
'import.pax.file.name',
'ips.to.allow.csv.regex',
'merchorder.phone',
'messenger.app.code',
'participant.allow.contacts',
'participant.allow.estatements',
'password.initial',
'password.pattern',
'password.use.initial',
'should.restrict.by.ip',
'sso.internal.aes256.key',
'sso.internal.identifier',
'sso.internal.timelag.allowed',
'termsAndConditions.used',
'termsAndConditions.usercanaccept',
'termsAndConditionsView.display',
'texteditor.dictionaries',
'twitter.consumer.key',
'twitter.consumer.secret')
                   )
    LOOP
               
      BEGIN
        v_msg := 'Check os_propertyset';
        SELECT entity_id
          INTO v_entity_id
          FROM os_propertyset
         WHERE entity_name = rec_os.entity_name
           AND entity_key  = rec_os.entity_key;
        
        v_msg := 'Update os_propertyset'; 
        UPDATE os_propertyset
           SET key_type     = rec_os.key_type,     
               boolean_val  = rec_os.boolean_val, 
               double_val   = rec_os.double_val, 
               string_val   = rec_os.string_val, 
               long_val     = rec_os.long_val, 
               int_val      = rec_os.int_val, 
               date_val     = rec_os.date_val
         WHERE entity_id    = v_entity_id;
        
        v_dml_cnt := v_dml_cnt + 1; 
        
      EXCEPTION  --No inserts needed. 03/08/2013
        WHEN NO_DATA_FOUND THEN
        NULL; --01/31/2014
--          v_msg := 'Insert os_propertyset';
--          INSERT INTO os_propertyset
--                       (entity_id, 
--                        entity_name, 
--                        entity_key, 
--                        key_type, 
--                        boolean_val, 
--                        double_val, 
--                        string_val, 
--                        long_val, 
--                        int_val, 
--                        date_val
--                        )
--                VALUES (ENTITY_ID_PK_SQ.nextval, 
--                        rec_os.entity_name, 
--                        rec_os.entity_key, 
--                        rec_os.key_type, 
--                        rec_os.boolean_val, 
--                        rec_os.double_val, 
--                        rec_os.string_val, 
--                        rec_os.long_val, 
--                        rec_os.int_val, 
--                        rec_os.date_val
--                        );
--        v_dml_cnt := v_dml_cnt + 1;
      END;               

    END LOOP;
    v_msg := 'MERGE os_propertyset';
    show_msg_count(v_msg, v_dml_cnt);
    
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;

  v_msg      := 'MERGE encryption_conversion';
  v_tbl_name := 'encryption_conversion';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO encryption_conversion d
       USING (SELECT table_name, 
                     column_name, 
                     data_type, 
                     needs_encryption
                FROM encryption_conversion_g4
              ) s
          ON (d.table_name  = s.table_name  AND
              d.column_name = s.column_name)
        WHEN MATCHED THEN UPDATE 
         SET d.data_type        = s.data_type,
             d.needs_encryption = s.needs_encryption
        WHEN NOT MATCHED THEN INSERT
             (d.table_name, 
              d.column_name, 
              d.data_type, 
              d.needs_encryption
              )
      VALUES (s.table_name, 
              s.column_name, 
              s.data_type, 
              s.needs_encryption
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;
--1. System variables  END

--2. Countries and suppliers  START
/*--Tables--
  supplier 
  country
  country_suppliers
*/
  v_msg      := 'MERGE supplier';
  v_tbl_name := 'supplier';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN
    v_dml_cnt := 0;
    FOR rec_sup IN (SELECT supplier_id, 
                           DECODE(supplier_name,'Surfgold','Edenred',supplier_name) supplier_name, 
                           supplier_type, 
                           description, 
                           status, 
                           --allow_partner_sso, 
                           catalog_url, 
                           catalog_target_id, 
                           statement_url, 
                           statement_target_id, 
                           cm_asset_code, 
                           image_cm_key, 
                           title_cm_key, 
                           desc_cm_key, 
                           button_cm_key, 
                           created_by, 
                           date_created, 
                           modified_by, 
                           date_modified, 
                           version
                      FROM supplier_g4
                    )
    LOOP              

      BEGIN
        v_msg := 'Check supplier';
        SELECT supplier_id
          INTO v_supplier_id
          FROM supplier
         WHERE UPPER(supplier_name) = UPPER(rec_sup.supplier_name);
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_msg := 'Insert supplier';
          INSERT INTO supplier
                     (supplier_id, 
                      supplier_name, 
                      supplier_type, 
                      description, 
                      status, 
                      --allow_partner_sso, 
                      catalog_url, 
                      catalog_target_id, 
                      statement_url, 
                      statement_target_id, 
                      cm_asset_code, 
                      image_cm_key, 
                      title_cm_key, 
                      desc_cm_key, 
                      button_cm_key, 
                      created_by, 
                      date_created, 
                      modified_by, 
                      date_modified, 
                      version
                      )
              VALUES (rec_sup.supplier_id, 
                      rec_sup.supplier_name, 
                      rec_sup.supplier_type, 
                      rec_sup.description, 
                      rec_sup.status, 
                      --rec_sup.allow_partner_sso, 
                      rec_sup.catalog_url, 
                      rec_sup.catalog_target_id, 
                      rec_sup.statement_url, 
                      rec_sup.statement_target_id, 
                      rec_sup.cm_asset_code, 
                      rec_sup.image_cm_key, 
                      rec_sup.title_cm_key, 
                      rec_sup.desc_cm_key, 
                      rec_sup.button_cm_key, 
                      rec_sup.created_by, 
                      rec_sup.date_created, 
                      rec_sup.modified_by, 
                      rec_sup.date_modified, 
                      rec_sup.version
                      );
        v_dml_cnt := v_dml_cnt + 1;
      END;                   
                    
    END LOOP;                    
    v_msg := 'MERGE supplier';
    show_msg_count(v_msg, v_dml_cnt);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;
  

  v_msg      := 'MERGE country';
  v_tbl_name := 'country';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_dml_cnt  := 0;
    FOR rec_c IN (SELECT country_id, 
                         DECODE(country_code,'pri','pr','vir','vi','yug','yu', country_code) country_code,
                         cm_asset_code, 
                         name_cm_key, 
                         awardbanq_country_abbrev, 
                         address_method, 
                         --supplier_id, 
                         campaign_nbr, 
                         campaign_code, 
                         campaign_password, 
                         program_id, 
                         program_password, 
                         status, 
                         date_status, 
                         shopping_banner_url, 
                         allow_sms, 
                         display_travel_award, 
                         support_email_addr, 
                         created_by, 
                         date_created, 
                         modified_by, 
                         date_modified, 
                         version, 
                         phone_country_code, 
                         timezone_id, 
                         --budget_media_value, 
                         require_postalcode
                    FROM country_g4)
    LOOP
    
      v_msg := 'Find g5 country code: '||rec_c.country_code;
      SELECT country_id
        INTO v_country_id
        FROM country
       WHERE country_code = rec_c.country_code;
    
      IF v_country_id <> rec_c.country_id THEN
    
        BEGIN
          v_msg := 'Find g5 country id: '||rec_c.country_id;
          SELECT country_id
            INTO v_country_id_g4
            FROM country
           WHERE country_id = rec_c.country_id;
      
        EXCEPTION
          WHEN NO_DATA_FOUND THEN

           INSERT INTO country
                       (country_id, 
                        country_code, 
                        cm_asset_code, 
                        name_cm_key, 
                        awardbanq_country_abbrev, 
                        address_method, 
                        --supplier_id, 
                        campaign_nbr, 
                        campaign_code, 
                        campaign_password, 
                        program_id, 
                        program_password, 
                        status, 
                        date_status, 
                        shopping_banner_url, 
                        allow_sms, 
                        display_travel_award, 
                        support_email_addr, 
                        created_by, 
                        date_created, 
                        modified_by, 
                        date_modified, 
                        version, 
                        phone_country_code, 
                        timezone_id, 
                        --budget_media_value, 
                        require_postalcode)
                VALUES (rec_c.country_id, 
                        rec_c.country_code||'_NEW', 
                        rec_c.cm_asset_code, 
                        rec_c.name_cm_key, 
                        rec_c.awardbanq_country_abbrev, 
                        rec_c.address_method, 
                        --supplier_id, 
                        rec_c.campaign_nbr, 
                        rec_c.campaign_code, 
                        rec_c.campaign_password, 
                        rec_c.program_id, 
                        rec_c.program_password, 
                        rec_c.status, 
                        rec_c.date_status, 
                        rec_c.shopping_banner_url, 
                        rec_c.allow_sms, 
                        rec_c.display_travel_award, 
                        rec_c.support_email_addr, 
                        rec_c.created_by, 
                        rec_c.date_created, 
                        rec_c.modified_by, 
                        rec_c.date_modified, 
                        rec_c.version, 
                        rec_c.phone_country_code, 
                        rec_c.timezone_id, 
                        --budget_media_value, 
                        rec_c.require_postalcode
                        );          
        END;
      
        v_msg := 'Update country_suppliers country id: '||rec_c.country_id;
        UPDATE country_suppliers
           SET country_id = rec_c.country_id
         WHERE country_id = v_country_id;

        v_msg := 'Update user_address country id: '||rec_c.country_id;
        UPDATE user_address
           SET country_id = rec_c.country_id
         WHERE country_id = v_country_id;

        DELETE FROM country
              WHERE country_id = v_country_id;

        v_msg := '1 Update country country id: '||rec_c.country_id;
        UPDATE country
           SET country_code = rec_c.country_code
         WHERE country_id   = rec_c.country_id;

        v_dml_cnt := v_dml_cnt + 1;
      
      ELSIF v_country_id = rec_c.country_id THEN

        v_msg := '2 Update country country id: '||rec_c.country_id;
        UPDATE country
           SET country_code              = rec_c.country_code,
               cm_asset_code             = rec_c.cm_asset_code,
               name_cm_key               = rec_c.name_cm_key,
               awardbanq_country_abbrev  = rec_c.awardbanq_country_abbrev,
               address_method            = rec_c.address_method,
               --supplier_id               = rec_c.supplier_id,
               campaign_nbr              = rec_c.campaign_nbr,
               campaign_code             = rec_c.campaign_code,
               campaign_password         = rec_c.campaign_password,
               program_id                = rec_c.program_id,
               program_password          = rec_c.program_password,
               status                    = rec_c.status,
               date_status               = rec_c.date_status,
               shopping_banner_url       = rec_c.shopping_banner_url,
               allow_sms                 = rec_c.allow_sms,
               display_travel_award      = rec_c.display_travel_award,
               support_email_addr        = rec_c.support_email_addr,
               created_by                = rec_c.created_by,
               date_created              = rec_c.date_created,
               modified_by               = rec_c.modified_by,
               date_modified             = rec_c.date_modified,
               version                   = rec_c.version,
               phone_country_code        = rec_c.phone_country_code,
               timezone_id               = rec_c.timezone_id,
               --budget_media_value        = rec_c.budget_media_value,
               require_postalcode        = rec_c.require_postalcode
         WHERE country_id                = rec_c.country_id;       
      
        v_dml_cnt := v_dml_cnt + 1;
      
      END IF;
              
    END LOOP;
  
    v_msg      := 'MERGE country';
    show_msg_count(v_msg, v_dml_cnt);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE country_suppliers';
  v_tbl_name := 'country_suppliers';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_dml_cnt  := 0;
    FOR rec_cs IN (SELECT country_id, 
                          supplier_id, 
                          is_primary, 
                          created_by, 
                          date_created, 
                          modified_by, 
                          date_modified
                     FROM country_suppliers_g4
                   )
    LOOP
      BEGIN
        v_msg := 'Check country_suppliers';
        SELECT country_id
          INTO v_country_id
          FROM country_suppliers
         WHERE country_id  = rec_cs.country_id
           AND supplier_id = rec_cs.supplier_id;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          INSERT INTO country_suppliers
                      (country_id, 
                       supplier_id, 
                       is_primary, 
                       created_by, 
                       date_created, 
                       modified_by, 
                       date_modified
                      )
               VALUES (rec_cs.country_id, 
                       rec_cs.supplier_id, 
                       rec_cs.is_primary, 
                       rec_cs.created_by, 
                       rec_cs.date_created, 
                       rec_cs.modified_by, 
                       rec_cs.date_modified
                       );
        v_dml_cnt := v_dml_cnt + 1;
      END;
    END LOOP;
    v_msg := 'MERGE country_suppliers';
    show_msg_count(v_msg, v_dml_cnt);
    
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;
--2. Countries and suppliers  END


--3. Employer
  v_msg      := 'MERGE employer';
  v_tbl_name := 'employer';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO employer d
       USING (SELECT employer_id, 
                     name, 
                     is_active, 
                     status_reason, 
                     federal_tax_id, 
                     state_tax_id, 
                     country_id, 
                     addr1, 
                     addr2, 
                     addr3, 
                     addr4, 
                     addr5, 
                     addr6, 
                     city, 
                     state, 
                     postal_code, 
                     phone_nbr, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version, 
                     phone_ext, 
                     country_phone_code
                FROM employer_g4
              ) s
          ON (d.employer_id = s.employer_id)
        WHEN MATCHED THEN UPDATE 
         SET d.name               = s.name,
             d.is_active          = s.is_active,
             d.status_reason      = s.status_reason,
             d.federal_tax_id     = s.federal_tax_id,
             d.state_tax_id       = s.state_tax_id,
             d.country_id         = s.country_id,
             d.addr1              = s.addr1,
             d.addr2              = s.addr2,
             d.addr3              = s.addr3,
             d.addr4              = s.addr4,
             d.addr5              = s.addr5,
             d.addr6              = s.addr6,
             d.city               = s.city,
             d.state              = s.state,
             d.postal_code        = s.postal_code,
             d.phone_nbr          = s.phone_nbr,
             d.created_by         = s.created_by,
             d.date_created       = s.date_created,
             d.modified_by        = s.modified_by,
             d.date_modified      = s.date_modified,
             d.version            = s.version,
             d.phone_ext          = s.phone_ext,
             d.country_phone_code = s.country_phone_code
        WHEN NOT MATCHED THEN INSERT
             (  d.employer_id, 
                d.name, 
                d.is_active, 
                d.status_reason, 
                d.federal_tax_id, 
                d.state_tax_id, 
                d.country_id, 
                d.addr1, 
                d.addr2, 
                d.addr3, 
                d.addr4, 
                d.addr5, 
                d.addr6, 
                d.city, 
                d.state, 
                d.postal_code, 
                d.phone_nbr, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version, 
                d.phone_ext, 
                d.country_phone_code
              )
      VALUES (  s.employer_id, 
                s.name, 
                s.is_active, 
                s.status_reason, 
                s.federal_tax_id, 
                s.state_tax_id, 
                s.country_id, 
                s.addr1, 
                s.addr2, 
                s.addr3, 
                s.addr4, 
                s.addr5, 
                s.addr6, 
                s.city, 
                s.state, 
                s.postal_code, 
                s.phone_nbr, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version, 
                s.phone_ext, 
                s.country_phone_code
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;
--3. Employer


--4. Hierarchy   START
/*--Tables--
  hierarchy 
  hierarchy_node
  node_type
  hierarchy_node_type
  node
  node_characteristic
*/
  v_msg      := 'MERGE hierarchy';
  v_tbl_name := 'hierarchy';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO hierarchy d
       USING (SELECT hierarchy_id, 
                     cm_asset_code, 
                     name_cm_key, 
                     description, 
                     is_active, 
                     is_primary, 
                     is_deleted, 
                     --is_node_type_req, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM hierarchy_g4
               ORDER BY hierarchy_id
              ) s
          ON (d.hierarchy_id = s.hierarchy_id)
        WHEN MATCHED THEN UPDATE 
         SET d.cm_asset_code    = s.cm_asset_code, 
             d.name_cm_key      = s.name_cm_key, 
             d.description      = s.description, 
             d.is_active        = s.is_active, 
             d.is_primary       = s.is_primary, 
             d.is_deleted       = s.is_deleted, 
             --d.is_node_type_req = s.is_node_type_req, 
             d.created_by       = s.created_by, 
             d.date_created     = s.date_created, 
             d.modified_by      = s.modified_by, 
             d.date_modified    = s.date_modified, 
             d.version          = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.hierarchy_id, 
              d.cm_asset_code, 
              d.name_cm_key, 
              d.description, 
              d.is_active, 
              d.is_primary, 
              d.is_deleted, 
              --d.is_node_type_req, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.hierarchy_id, 
              s.cm_asset_code, 
              s.name_cm_key, 
              s.description, 
              s.is_active, 
              s.is_primary, 
              s.is_deleted, 
              --s.is_node_type_req, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;
  
  
  v_msg      := 'MERGE hierarchy_node';
  v_tbl_name := 'hierarchy_node';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO hierarchy_node d
       USING (SELECT hierarchy_id, 
                     node_id, 
                     created_by, 
                     date_created
                FROM hierarchy_node_g4
               ORDER BY hierarchy_id
              ) s
          ON (d.hierarchy_id = s.hierarchy_id AND
              d.node_id      = s.node_id)
        WHEN NOT MATCHED THEN INSERT
             (d.hierarchy_id, 
              d.node_id, 
              d.created_by, 
              d.date_created
              )
      VALUES (s.hierarchy_id, 
              s.node_id, 
              s.created_by, 
              s.date_created
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE node_type';
  v_tbl_name := 'node_type';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_dml_cnt := 0;
    FOR rec_nt IN (SELECT nt.node_type_id, 
                          nt.cm_asset_code, 
                          nt.name_cm_key, 
                          nt.is_deleted,
                          vcav.cms_value  node_type_name, 
                          nt.created_by, 
                          nt.date_created, 
                          nt.modified_by, 
                          nt.date_modified, 
                          nt.version
                    FROM node_type_g4 nt,
                         vw_cms_asset_value_g4 vcav
                   WHERE nt.cm_asset_code = vcav.asset_code
                     AND nt.name_cm_key   = vcav.key
                   ORDER BY nt.node_type_id
                   )
    LOOP
      v_node_type_id_new := null;
      BEGIN
        v_msg := 'Check g5_1 node_type';
        SELECT node_type_id
          INTO v_node_type_id
          FROM node_type nt,
               vw_cms_asset_value vcav
         WHERE nt.cm_asset_code = vcav.asset_code
           AND nt.name_cm_key   = vcav.key
           AND vcav.cms_value   = rec_nt.node_type_name;
        
        IF v_node_type_id <> rec_nt.node_type_id THEN
          v_msg := 'Insert node_type_1 G4 node type id:'||rec_nt.node_type_id;
          INSERT INTO node_type
                      (node_type_id, 
                       cm_asset_code, 
                       name_cm_key, 
                       is_deleted, 
                       created_by, 
                       date_created, 
                       modified_by, 
                       date_modified, 
                       version
                       )
               VALUES (rec_nt.node_type_id, 
                       rec_nt.cm_asset_code, 
                       rec_nt.name_cm_key, 
                       rec_nt.is_deleted, 
                       rec_nt.created_by, 
                       rec_nt.date_created, 
                       rec_nt.modified_by, 
                       rec_nt.date_modified, 
                       rec_nt.version
                       );        
        
          v_msg := 'Update1 hierarchy_node_type G5 node type id:'||rec_nt.node_type_id;
          UPDATE hierarchy_node_type
             SET node_type_id = rec_nt.node_type_id
           WHERE node_type_id = v_node_type_id;  
        
          v_msg := 'Update1 node';
          UPDATE node
             SET node_type_id = rec_nt.node_type_id
           WHERE node_type_id = v_node_type_id;
        
          v_msg := 'Delete1 node_type';
          DELETE node_type
           WHERE node_type_id = v_node_type_id;

        END IF;
        
        v_dml_cnt := v_dml_cnt + 1;
                
      EXCEPTION
        WHEN NO_DATA_FOUND THEN

          BEGIN
            v_msg := 'Check node_type g52 for G4 node type id:'||rec_nt.node_type_id;
            SELECT node_type_id
              INTO v_node_type_id
              FROM node_type
             WHERE node_type_id = rec_nt.node_type_id;

            <<loop_nt_id1>>
            LOOP
              v_node_type_id_new := NODE_TYPE_PK_SQ.NEXTVAL;
              BEGIN
                v_msg := 'Check g5 new seq:'||v_node_type_id_new;
                SELECT node_type_id
                  INTO v_node_type_id
                  FROM node_type_g4
                 WHERE node_type_id = v_node_type_id_new;
                 
              EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  EXIT loop_nt_id1;
              END;
            END LOOP;

             v_msg := 'Insert node_type3 G4 node type id:'||rec_nt.node_type_id;
             INSERT INTO node_type
                         (node_type_id, 
                          cm_asset_code, 
                          name_cm_key, 
                          is_deleted, 
                          created_by, 
                          date_created, 
                          modified_by, 
                          date_modified, 
                          version
                          )
                   SELECT v_node_type_id_new, 
                          cm_asset_code, 
                          name_cm_key, 
                          is_deleted, 
                          created_by, 
                          date_created, 
                          modified_by, 
                          date_modified, 
                          version
                     FROM node_type
                    WHERE node_type_id = rec_nt.node_type_id;      

            v_msg := 'Update2 hierarchy_node_type G5 node type id:'||rec_nt.node_type_id||' g5 new id:'||v_node_type_id_new;
            UPDATE hierarchy_node_type
               SET node_type_id = v_node_type_id_new
             WHERE node_type_id = rec_nt.node_type_id;  
            
            v_msg := 'Update2 node';
            UPDATE node
               SET node_type_id = v_node_type_id_new
             WHERE node_type_id = rec_nt.node_type_id;

            v_msg := 'Delete2 node_type';
            DELETE FROM node_type
             WHERE node_type_id = rec_nt.node_type_id;

          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              v_msg := 'Insert node_type3 G4 node type id:'||rec_nt.node_type_id;
              INSERT INTO node_type
                         (node_type_id, 
                          cm_asset_code, 
                          name_cm_key, 
                          is_deleted, 
                          created_by, 
                          date_created, 
                          modified_by, 
                          date_modified, 
                          version
                          )
                  VALUES (rec_nt.node_type_id, 
                          rec_nt.cm_asset_code, 
                          rec_nt.name_cm_key, 
                          rec_nt.is_deleted, 
                          rec_nt.created_by, 
                          rec_nt.date_created, 
                          rec_nt.modified_by, 
                          rec_nt.date_modified, 
                          rec_nt.version
                          );
          END;

        v_dml_cnt := v_dml_cnt + 1;
      END;
    
    END LOOP;
    v_msg      := 'MERGE node_type';
    show_msg_count(v_msg, v_dml_cnt);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE hierarchy_node_type';
  v_tbl_name := 'hierarchy_node_type';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO hierarchy_node_type d
       USING (SELECT hng4.hierarchy_id,
                     ntg5.node_type_id,
                     hng4.created_by,
                     hng4.date_created
                FROM hierarchy_node_type_g4 hng4,
                     node_type_g4 ntg4,
                     node_type ntg5
               WHERE hng4.node_type_id = ntg4.node_type_id
                 AND ntg4.cm_asset_code = ntg5.cm_asset_code
               ORDER BY hng4.hierarchy_id,ntg5.node_type_id
              ) s
          ON (d.hierarchy_id = s.hierarchy_id AND 
              d.node_type_id = s.node_type_id)
        WHEN MATCHED THEN UPDATE 
         SET --d.node_type_id = s.node_type_id, 
             d.created_by   = s.created_by, 
             d.date_created = s.date_created
        WHEN NOT MATCHED THEN INSERT
             (d.hierarchy_id, 
              d.node_type_id, 
              d.created_by, 
              d.date_created
              )
      VALUES (s.hierarchy_id, 
              s.node_type_id, 
              s.created_by, 
              s.date_created
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE node';
  v_tbl_name := 'node';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    FOR rec_node IN (SELECT ng4.node_id,
                            ntg5.node_type_id,
                            ng4.name,
                            ng4.path,
                            ng4.description,
                            ng4.parent_node_id,
                            ng4.hierarchy_id,
                            ng4.is_deleted,
                            ng4.created_by,
                            ng4.date_created,
                            ng4.modified_by,
                            ng4.date_modified,
                            ng4.version
                       FROM node_g4 ng4,
                            node_type_g4 ntg4,
                            node_type ntg5
                      WHERE ng4.node_type_id = ntg4.node_type_id        
                        AND ntg4.cm_asset_code = ntg5.cm_asset_code
                      ORDER BY ng4.node_id
                    )
    LOOP
      
      BEGIN
        v_msg := 'Check node name exists: '||rec_node.name;
        SELECT node_id
          INTO v_node_id
          FROM node
         WHERE name = rec_node.name; 
      EXCEPTION
        WHEN NO_DATA_FOUND THEN        
          v_msg := 'Insert node : '||rec_node.node_id;
          INSERT INTO node
                 (node_id, 
                  node_type_id, 
                  name, 
                  path, 
                  description, 
                  parent_node_id, 
                  hierarchy_id, 
                  is_deleted, 
                  created_by, 
                  date_created, 
                  modified_by, 
                  date_modified, 
                  version
                  )        
          VALUES (rec_node.node_id, 
                  rec_node.node_type_id, 
                  rec_node.name, 
                  rec_node.path, 
                  rec_node.description, 
                  rec_node.parent_node_id, 
                  rec_node.hierarchy_id, 
                  rec_node.is_deleted, 
                  rec_node.created_by, 
                  rec_node.date_created, 
                  rec_node.modified_by, 
                  rec_node.date_modified, 
                  rec_node.version
                  ); 
      END;
      
    END LOOP;        
    v_msg := 'MERGE node';
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE node_characteristic';
  v_tbl_name := 'node_characteristic';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO node_characteristic d
       USING (SELECT node_characteristic_id, 
                     node_id, 
                     characteristic_id, 
                     characteristic_value, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM node_characteristic_g4
              ) s
          ON (d.node_characteristic_id = s.node_characteristic_id)
        WHEN MATCHED THEN UPDATE 
         SET d.node_id              = s.node_id, 
             d.characteristic_id    = s.characteristic_id, 
             d.characteristic_value = s.characteristic_value, 
             d.created_by           = s.created_by, 
             d.date_created         = s.date_created, 
             d.modified_by          = s.modified_by, 
             d.date_modified        = s.date_modified, 
             d.version              = s.version
        WHEN NOT MATCHED THEN INSERT
             (d.node_characteristic_id, 
              d.node_id, 
              d.characteristic_id, 
              d.characteristic_value, 
              d.created_by, 
              d.date_created, 
              d.modified_by, 
              d.date_modified, 
              d.version
              )
      VALUES (s.node_characteristic_id, 
              s.node_id, 
              s.characteristic_id, 
              s.characteristic_value, 
              s.created_by, 
              s.date_created, 
              s.modified_by, 
              s.date_modified, 
              s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;
--4. Hierarchy   END


--5. Participant related tables Start
/*--Participant information tables--
  role
  user_type_role
  application_user
  participant
  participant_employer
  participant_contact_method
  user_email_address
  user_address
  user_phone
  user_characteristic
  user_node
  user_node_history
  user_acl
  user_role
  login_activity
  participant_comm_preference
  participant_address_book
  user_twitter
  user_facebook 
  user_country_changes
*/

  v_msg      := 'MERGE role';
  v_tbl_name := 'role';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

    v_dml_cnt := 0;
    FOR rec_r IN (SELECT role_id, 
                         name, 
                         code, 
                         help_text, 
                         is_active, 
                         created_by, 
                         date_created, 
                         modified_by, 
                         date_modified, 
                         version
                    FROM role_g4
                    WHERE CODE NOT in ('CLIENT_ADMIN_COMM','SPECIAL_REPORT_ACCESS','CLIENT_ADMIN_PAX','CLIENT_ADMIN_RPT','CLIENT_ADMIN_PROMO')
                  ) LOOP

       BEGIN
         v_msg := 'Check role';
         SELECT role_id
           INTO v_role_id
           FROM role
          WHERE code = rec_r.code;

         IF v_role_id <> rec_r.role_id THEN
           v_msg := 'Insert role';
           v_role_id_new:=ROLE_PK_SQ.NEXTVAL;
           INSERT INTO role
                       (role_id, 
                        name, 
                        code, 
                        help_text, 
                        is_active, 
                        created_by, 
                        date_created,
                        modified_by, 
                        date_modified, 
                        version
                        )
                VALUES (v_role_id_new, 
                        rec_r.name, 
                        rec_r.code||'_NEW', 
                        rec_r.help_text, 
                        rec_r.is_active, 
                        rec_r.created_by, 
                        rec_r.date_created, 
                        rec_r.modified_by, 
                        rec_r.date_modified, 
                        rec_r.version
                        ); 

           v_msg := 'Update user_type_role';
           UPDATE user_type_role
              SET role_id = v_role_id_new
            WHERE role_id = v_role_id;
           
           v_msg := 'Update user_role';
           UPDATE user_role
              SET role_id = v_role_id_new
            WHERE role_id = v_role_id;
            
            v_msg := 'Update process_role';--01/31/2014
           UPDATE user_role
              SET role_id = v_role_id_new
            WHERE role_id = v_role_id;

           v_msg := 'Delete role';
           DELETE FROM role
                 WHERE role_id = v_role_id;

           v_msg := 'Update role';
           UPDATE role
              SET code    = rec_r.code
            WHERE role_id = v_role_id_new;

           v_dml_cnt := v_dml_cnt + 1;

         ELSE
           v_msg := 'Update role1';
           UPDATE role
              SET name          = rec_r.name,
                  help_text     = rec_r.help_text,
                  is_active     = rec_r.is_active,
                  created_by    = rec_r.created_by,
                  date_created  = rec_r.date_created,
                  modified_by   = rec_r.modified_by,
                  date_modified = rec_r.date_modified,
                  version       = rec_r.version
            WHERE code = rec_r.code;
         
           v_dml_cnt := v_dml_cnt + 1;
         
         END IF;
         
       EXCEPTION
         WHEN NO_DATA_FOUND THEN
           v_msg := 'Insert role1';
           v_role_id_new:=ROLE_PK_SQ.NEXTVAL;
           INSERT INTO role
                       (role_id, 
                        name, 
                        code, 
                        help_text, 
                        is_active, 
                        created_by, 
                        date_created, 
                        modified_by, 
                        date_modified, 
                        version
                        )
                VALUES (v_role_id_new, 
                        rec_r.name, 
                        rec_r.code, 
                        rec_r.help_text, 
                        rec_r.is_active, 
                        rec_r.created_by, 
                        rec_r.date_created, 
                        rec_r.modified_by, 
                        rec_r.date_modified, 
                        rec_r.version
                        );
         v_dml_cnt := v_dml_cnt + 1;
       END;                

    END LOOP;
    v_msg := 'MERGE role';
    show_msg_count(v_msg, v_dml_cnt);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE user_type_role';
  v_tbl_name := 'user_type_role';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_type_role d
       USING (SELECT * FROM (SELECT user_type_code,(select role.role_id from role,role_g4
                WHERE role.code=role_g4.code AND role_g4.role_id= user_type_role_g4.role_id )
                     role_id
                FROM user_type_role_g4) WHERE role_id IS NOT NULL
              ) s
          ON (d.role_id        = s.role_id AND
              d.user_type_code = s.user_type_code)
        WHEN NOT MATCHED THEN INSERT
             (d.user_type_code,
              d.role_id
              )
      VALUES (s.user_type_code,
              s.role_id
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;

                                    
  v_msg      := 'MERGE application_user';
  v_tbl_name := 'application_user';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO application_user d
       USING (SELECT au.user_id, 
                     user_name, 
                     user_type, 
                     force_password_change, 
                     title, 
                     first_name, 
                     middle_name, 
                     last_name, 
                     suffix, 
                     fnc_java_encrypt(pep.ssn) ssn, 
                     birth_date, 
                     gender, 
                     password, 
                     date_modified, 
                     is_active, 
                     --is_profile_setup_done, 
                     is_welcome_email_sent, 
                     is_cms_terms_accepted, 
                     is_text_messages_terms_accept, 
                     login_failures_count, 
                     last_reset_date, 
                     secret_question, 
                     fnc_java_encrypt(pep.secret_answer) secret_answer,  
                     master_user_id, 
                     language_id, 
                     enrollment_source, 
                     enrollment_date, 
                     au.created_by, 
                     au.date_created, 
                     modified_by, 
                     version
                FROM application_user_g4 au,prep_encrypt_pax_g4 pep
                     WHERE pep.user_id(+) = au.user_id
                ) s
             ON (d.user_id = s.user_id)
           WHEN MATCHED THEN UPDATE 
            SET d.user_name                     = s.user_name,
                d.user_type                     = s.user_type,
                d.force_password_change         = s.force_password_change,
                d.title                         = s.title,
                d.first_name                    = s.first_name,
                d.middle_name                   = s.middle_name,
                d.last_name                     = s.last_name,
                d.suffix                        = s.suffix,
                d.ssn                           = s.ssn,
                d.birth_date                    = s.birth_date,
                d.gender                        = s.gender,
                d.password                      = s.password,
                d.date_modified                 = s.date_modified,
                d.is_active                     = s.is_active,
                --d.is_profile_setup_done         = s.is_profile_setup_done,
                d.is_welcome_email_sent         = s.is_welcome_email_sent,
                d.is_cms_terms_accepted         = s.is_cms_terms_accepted,
                d.is_text_messages_terms_accept = s.is_text_messages_terms_accept,
                d.login_failures_count          = s.login_failures_count,
                d.last_reset_date               = s.last_reset_date,
                d.secret_question               = s.secret_question,
                d.secret_answer                 = s.secret_answer,
                d.master_user_id                = s.master_user_id,
                d.language_id                   = s.language_id,
                d.enrollment_source             = s.enrollment_source,
                d.enrollment_date               = s.enrollment_date,
                d.created_by                    = s.created_by,
                d.date_created                  = s.date_created,
                d.modified_by                   = s.modified_by,
                d.version                       = s.version
          WHEN NOT MATCHED THEN INSERT
               (d.user_id, 
                d.user_name, 
                d.user_type, 
                d.force_password_change, 
                d.title, 
                d.first_name, 
                d.middle_name, 
                d.last_name, 
                d.suffix, 
                d.ssn, 
                d.birth_date, 
                d.gender, 
                d.password, 
                d.date_modified, 
                d.is_active, 
                --d.is_profile_setup_done, 
                d.is_welcome_email_sent, 
                d.is_cms_terms_accepted, 
                d.is_text_messages_terms_accept, 
                d.login_failures_count, 
                d.last_reset_date, 
                d.secret_question, 
                d.secret_answer, 
                d.master_user_id, 
                d.language_id, 
                d.enrollment_source, 
                d.enrollment_date, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.version
                )
        VALUES (s.user_id, 
                s.user_name, 
                s.user_type, 
                s.force_password_change, 
                s.title, 
                s.first_name, 
                s.middle_name, 
                s.last_name, 
                s.suffix, 
                s.ssn, 
                s.birth_date, 
                s.gender, 
                s.password, 
                s.date_modified, 
                s.is_active, 
                --s.is_profile_setup_done, 
                s.is_welcome_email_sent, 
                s.is_cms_terms_accepted, 
                s.is_text_messages_terms_accept, 
                s.login_failures_count, 
                s.last_reset_date, 
                s.secret_question, 
                s.secret_answer, 
                s.master_user_id, 
                s.language_id, 
                s.enrollment_source, 
                s.enrollment_date, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.version
                );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE participant';
  v_tbl_name := 'participant';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO participant d
       USING (SELECT p.user_id, 
                     fnc_java_encrypt(pep.awardbanq_nbr) awardbanq_nbr, 
                     date_awardbanq_extract, 
                     fnc_java_encrypt(pep.centrax_id) centrax_id,
                     relationship_type, 
                     status, 
                     date_status_change, 
                     suspension_status, 
                     terms_acceptance, 
                     user_id_accepted, 
                     date_terms_accepted, 
                     facebook_id, 
                     twitter_id, 
                     --avatar_original, 
                     --avatar_small, 
                     allow_public_recognition, 
                     --allow_public_information, 
                     giftcode_only
                FROM participant_g4 p,prep_encrypt_pax_g4 pep
                     WHERE pep.user_id(+) = p.user_id
              ) s
          ON (d.user_id = s.user_id)
        WHEN MATCHED THEN UPDATE 
         SET    d.awardbanq_nbr            = s.awardbanq_nbr,
                d.date_awardbanq_extract   = s.date_awardbanq_extract,
                d.centrax_id               = s.centrax_id,
                d.relationship_type        = s.relationship_type,
                d.status                   = s.status,
                d.date_status_change       = s.date_status_change,
                d.suspension_status        = s.suspension_status,
                d.terms_acceptance         = s.terms_acceptance,
                d.user_id_accepted         = s.user_id_accepted,
                d.date_terms_accepted      = s.date_terms_accepted,
                d.facebook_id              = s.facebook_id,
                d.twitter_id               = s.twitter_id,
                --d.avatar_original          = s.avatar_original,
                --d.avatar_small             = s.avatar_small,
                d.allow_public_recognition = s.allow_public_recognition,
                --d.allow_public_information = s.allow_public_information,
                d.giftcode_only            = s.giftcode_only
        WHEN NOT MATCHED THEN INSERT
             (  d.user_id, 
                d.awardbanq_nbr, 
                d.date_awardbanq_extract, 
                d.centrax_id, 
                d.relationship_type, 
                d.status, 
                d.date_status_change, 
                d.suspension_status, 
                d.terms_acceptance, 
                d.user_id_accepted, 
                d.date_terms_accepted, 
                d.facebook_id, 
                d.twitter_id, 
                --d.avatar_original, 
                --d.avatar_small, 
                d.allow_public_recognition, 
                --d.allow_public_information, 
                d.giftcode_only
              )
      VALUES (  s.user_id, 
                s.awardbanq_nbr, 
                s.date_awardbanq_extract, 
                s.centrax_id, 
                s.relationship_type, 
                s.status, 
                s.date_status_change, 
                s.suspension_status, 
                s.terms_acceptance, 
                s.user_id_accepted, 
                s.date_terms_accepted, 
                s.facebook_id, 
                s.twitter_id, 
                --s.avatar_original, 
                --s.avatar_small, 
                s.allow_public_recognition, 
                --s.allow_public_information, 
                s.giftcode_only
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE participant_employer';
  v_tbl_name := 'participant_employer';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO participant_employer d
       USING (SELECT user_id, 
                     employer_id, 
                     participant_employer_index, 
                     position_type, 
                     department_type, 
                     hire_date, 
                     termination_date, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified
                FROM participant_employer_g4
              ) s
          ON (d.user_id                    = s.user_id AND 
              d.participant_employer_index = s.participant_employer_index)
        WHEN MATCHED THEN UPDATE 
         SET    d.employer_id                = s.employer_id,
                d.position_type              = s.position_type,
                d.department_type            = s.department_type,
                d.hire_date                  = s.hire_date,
                d.termination_date           = s.termination_date,
                d.created_by                 = s.created_by,
                d.date_created               = s.date_created,
                d.modified_by                = s.modified_by,
                d.date_modified              = s.date_modified
        WHEN NOT MATCHED THEN INSERT
             (  d.user_id, 
                d.employer_id, 
                d.participant_employer_index, 
                d.position_type, 
                d.department_type, 
                d.hire_date, 
                d.termination_date, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified
              )
      VALUES (  s.user_id, 
                s.employer_id, 
                s.participant_employer_index, 
                s.position_type, 
                s.department_type, 
                s.hire_date, 
                s.termination_date, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified                                 
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE participant_contact_method';          --**
  v_tbl_name := 'participant_contact_method';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO participant_contact_method d
       USING (SELECT user_id, 
                     contact_method_code, 
                     is_primary, 
                     created_by, 
                     date_created
                FROM participant_contact_method_g4
              ) s
          ON (d.user_id = s.user_id AND
              d.contact_method_code = s.contact_method_code)
        WHEN MATCHED THEN UPDATE 
         SET d.is_primary          = s.is_primary,
             d.created_by          = s.created_by,
             d.date_created        = s.date_created
        WHEN NOT MATCHED THEN INSERT
             (d.user_id, 
              d.contact_method_code, 
              d.is_primary, 
              d.created_by, 
              d.date_created
              )
      VALUES (s.user_id, 
              s.contact_method_code, 
              s.is_primary, 
              s.created_by, 
              s.date_created
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE user_email_address';
  v_tbl_name := 'user_email_address';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_email_address d
       USING (SELECT email_address_id, 
                     user_id, 
                     email_type, 
                     email_addr, 
                     email_status, 
                     created_by, 
                     is_primary, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM user_email_address_g4
              ) s
          ON (d.email_address_id = s.email_address_id)
        WHEN MATCHED THEN UPDATE 
         SET d.user_id          = s.user_id,
             d.email_type       = s.email_type,
             d.email_addr       = s.email_addr,
             d.email_status     = s.email_status,
             d.created_by       = s.created_by,
             d.is_primary       = s.is_primary,
             d.date_created     = s.date_created,
             d.modified_by      = s.modified_by,
             d.date_modified    = s.date_modified,
             d.version          = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.email_address_id, 
                d.user_id, 
                d.email_type, 
                d.email_addr, 
                d.email_status, 
                d.created_by, 
                d.is_primary, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.email_address_id, 
                s.user_id, 
                s.email_type, 
                s.email_addr, 
                s.email_status, 
                s.created_by, 
                s.is_primary, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;
  

  v_msg      := 'MERGE user_address';
  v_tbl_name := 'user_address';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_address d
       USING (SELECT user_address_id, 
                     user_id, 
                     address_type, 
                     country_id, 
                     addr1, 
                     addr2, 
                     addr3, 
                     addr4, 
                     addr5, 
                     addr6, 
                     city, 
                     state, 
                     postal_code, 
                     is_primary, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM user_address_g4
              ) s
          ON (d.user_address_id = s.user_address_id)
        WHEN MATCHED THEN UPDATE 
         SET d.user_id         = s.user_id,
             d.address_type    = s.address_type,
             d.country_id      = s.country_id,
             d.addr1           = s.addr1,
             d.addr2           = s.addr2,
             d.addr3           = s.addr3,
             d.addr4           = s.addr4,
             d.addr5           = s.addr5,
             d.addr6           = s.addr6,
             d.city            = s.city,
             d.state           = s.state,
             d.postal_code     = s.postal_code,
             d.is_primary      = s.is_primary,
             d.created_by      = s.created_by,
             d.date_created    = s.date_created,
             d.modified_by     = s.modified_by,
             d.date_modified   = s.date_modified,
             d.version         = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.user_address_id, 
                d.user_id, 
                d.address_type, 
                d.country_id, 
                d.addr1, 
                d.addr2, 
                d.addr3, 
                d.addr4, 
                d.addr5, 
                d.addr6, 
                d.city, 
                d.state, 
                d.postal_code, 
                d.is_primary, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.user_address_id, 
                s.user_id, 
                s.address_type, 
                s.country_id, 
                s.addr1, 
                s.addr2, 
                s.addr3, 
                s.addr4, 
                s.addr5, 
                s.addr6, 
                s.city, 
                s.state, 
                s.postal_code, 
                s.is_primary, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE user_phone';
  v_tbl_name := 'user_phone';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_phone d
       USING (SELECT user_phone_id, 
                     user_id, 
                     phone_type, 
                     phone_nbr, 
                     phone_ext, 
                     country_phone_code, 
                     is_primary, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM user_phone_g4
              ) s
          ON (d.user_phone_id = s.user_phone_id)
        WHEN MATCHED THEN UPDATE 
         SET d.user_id            = s.user_id,
             d.phone_type         = s.phone_type,
             d.phone_nbr          = s.phone_nbr,
             d.phone_ext          = s.phone_ext,
             d.country_phone_code = s.country_phone_code,
             d.is_primary         = s.is_primary,
             d.created_by         = s.created_by,
             d.date_created       = s.date_created,
             d.modified_by        = s.modified_by,
             d.date_modified      = s.date_modified,
             d.version            = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.user_phone_id, 
                d.user_id, 
                d.phone_type, 
                d.phone_nbr, 
                d.phone_ext, 
                d.country_phone_code, 
                d.is_primary, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.user_phone_id, 
                s.user_id, 
                s.phone_type, 
                s.phone_nbr, 
                s.phone_ext, 
                s.country_phone_code, 
                s.is_primary, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE user_characteristic';
  v_tbl_name := 'user_characteristic';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_characteristic d
       USING (SELECT user_characteristic_id, 
                     user_id, 
                     characteristic_id, 
                     characteristic_value, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM user_characteristic_g4
              ) s
          ON (d.user_characteristic_id = s.user_characteristic_id)
        WHEN MATCHED THEN UPDATE 
         SET d.user_id                = s.user_id,
             d.characteristic_id      = s.characteristic_id,
             d.characteristic_value   = s.characteristic_value,
             d.created_by             = s.created_by,
             d.date_created           = s.date_created,
             d.modified_by            = s.modified_by,
             d.date_modified          = s.date_modified,
             d.version                = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.user_characteristic_id, 
                d.user_id, 
                d.characteristic_id, 
                d.characteristic_value, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.user_characteristic_id, 
                s.user_id, 
                s.characteristic_id, 
                s.characteristic_value, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE user_node';
  v_tbl_name := 'user_node';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_node d
       USING (SELECT
       user_node_id, 
                     user_id, 
                     node_id, 
                     status, 
                     role, 
                     DECODE(rec_rank,1,1,0) is_primary, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
  FROM (
         select RANK() OVER (PARTITION BY pe.user_id ORDER BY pe.user_node_id ASC) as rec_rank,
                pe.*
           FROM user_node_g4 pe
       ) r
              ) s
          ON (d.user_node_id = s.user_node_id)
        WHEN MATCHED THEN UPDATE 
         SET d.user_id       = s.user_id,
             d.node_id       = s.node_id,
             d.status        = s.status,
             d.role          = s.role,
             d.is_primary    = s.is_primary,
             d.created_by    = s.created_by,
             d.date_created  = s.date_created,
             d.modified_by   = s.modified_by,
             d.date_modified = s.date_modified,
             d.version       = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.user_node_id, 
                d.user_id, 
                d.node_id, 
                d.status, 
                d.role, 
                d.is_primary, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.user_node_id, 
                s.user_id, 
                s.node_id, 
                s.status, 
                s.role, 
                s.is_primary, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE user_node_history';
  v_tbl_name := 'user_node_history';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_node_history d
       USING (SELECT user_node_history_id, 
                     user_id, 
                     node_id, 
                     status, 
                     role, 
                     created_by, 
                     date_created
                FROM user_node_history_g4
              ) s
          ON (d.user_node_history_id = s.user_node_history_id)
        WHEN MATCHED THEN UPDATE 
         SET d.user_id              = s.user_id,
             d.node_id              = s.node_id,
             d.status               = s.status,
             d.role                 = s.role,
             d.created_by           = s.created_by,
             d.date_created         = s.date_created
        WHEN NOT MATCHED THEN INSERT
             (  d.user_node_history_id, 
                d.user_id, 
                d.node_id, 
                d.status, 
                d.role, 
                d.created_by, 
                d.date_created
              )
      VALUES (  s.user_node_history_id, 
                s.user_id, 
                s.node_id, 
                s.status, 
                s.role, 
                s.created_by, 
                s.date_created
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE user_acl';                 --**
  v_tbl_name := 'user_acl';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_acl d
       USING (SELECT user_id, 
                     acl_id, 
                     guid, 
                     target, 
                     permission, 
                     created_by, 
                     date_created
                FROM user_acl_g4
              ) s
          ON (d.user_id    = s.user_id    AND  
              d.acl_id     = s.acl_id     AND 
              d.target     = s.target     AND 
              d.permission = s.permission
              )
        WHEN NOT MATCHED THEN INSERT
             (d.user_id, 
              d.acl_id, 
              d.guid, 
              d.target, 
              d.permission, 
              d.created_by, 
              d.date_created
              )
      VALUES (s.user_id, 
              s.acl_id, 
              s.guid, 
              s.target, 
              s.permission, 
              s.created_by, 
              s.date_created
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE user_role';
  v_tbl_name := 'user_role';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_role d
       USING (SELECT * FROM (                --01/31/2014
                SELECT user_id,(select role.role_id from role,role_g4
                WHERE role.code=role_g4.code AND role_g4.role_id= user_role_g4.role_id )
                     role_id,
                     created_by, 
                     date_created
                FROM user_role_g4) 
                      WHERE role_id IS NOT NULL
              ) s
          ON (d.user_id = s.user_id AND
              d.role_id = s.role_id )
        WHEN MATCHED THEN UPDATE 
         SET d.created_by   = s.created_by,
             d.date_created = s.date_created
        WHEN NOT MATCHED THEN INSERT
             (  d.user_id, 
                d.role_id, 
                d.created_by, 
                d.date_created
              )
      VALUES (  s.user_id, 
                s.role_id, 
                s.created_by, 
                s.date_created
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE login_activity';          --**
  v_tbl_name := 'login_activity';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO login_activity d
       USING (SELECT user_id,
                     login_date_time
                FROM login_activity_g4
              ) s
          ON (d.user_id         = s.user_id AND
              d.login_date_time = s.login_date_time)
        WHEN NOT MATCHED THEN INSERT
             (d.user_id,
              d.login_date_time
              )
      VALUES (s.user_id,
              s.login_date_time
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE participant_comm_preference';
  v_tbl_name := 'participant_comm_preference';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO participant_comm_preference d
       USING (SELECT participant_comm_preference_id, 
                     user_id, 
                     communication_code, 
                     sms_group_type, 
                     created_by, 
                     date_created
                FROM participant_comm_preference_g4
              ) s
          ON (d.participant_comm_preference_id = s.participant_comm_preference_id)
        WHEN MATCHED THEN UPDATE 
         SET d.user_id                        = s.user_id,
             d.communication_code             = s.communication_code,
             d.sms_group_type                 = s.sms_group_type,
             d.created_by                     = s.created_by,
             d.date_created                   = s.date_created
        WHEN NOT MATCHED THEN INSERT
             (  d.participant_comm_preference_id, 
                d.user_id, 
                d.communication_code, 
                d.sms_group_type, 
                d.created_by, 
                d.date_created
              )
      VALUES (  s.participant_comm_preference_id, 
                s.user_id, 
                s.communication_code, 
                s.sms_group_type, 
                s.created_by, 
                s.date_created
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE participant_address_book';
  v_tbl_name := 'participant_address_book';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO participant_address_book d
       USING (SELECT pax_address_id, 
                     user_id, 
                     guid, 
                     first_name, 
                     last_name, 
                     addr1, 
                     addr2, 
                     city, 
                     state, 
                     postal_code, 
                     country_id, 
                     email_addr, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM participant_address_book_g4
              ) s
          ON (d.pax_address_id = s.pax_address_id)
        WHEN MATCHED THEN UPDATE 
         SET d.user_id        = s.user_id,
             d.guid           = s.guid,
             d.first_name     = s.first_name,
             d.last_name      = s.last_name,
             d.addr1          = s.addr1,
             d.addr2          = s.addr2,
             d.city           = s.city,
             d.state          = s.state,
             d.postal_code    = s.postal_code,
             d.country_id     = s.country_id,
             d.email_addr     = s.email_addr,
             d.created_by     = s.created_by,
             d.date_created   = s.date_created,
             d.modified_by    = s.modified_by,
             d.date_modified  = s.date_modified,
             d.version        = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.pax_address_id, 
                d.user_id, 
                d.guid, 
                d.first_name, 
                d.last_name, 
                d.addr1, 
                d.addr2, 
                d.city, 
                d.state, 
                d.postal_code, 
                d.country_id, 
                d.email_addr, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.pax_address_id, 
                s.user_id, 
                s.guid, 
                s.first_name, 
                s.last_name, 
                s.addr1, 
                s.addr2, 
                s.city, 
                s.state, 
                s.postal_code, 
                s.country_id, 
                s.email_addr, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;


  v_msg      := 'MERGE user_twitter';
  v_tbl_name := 'user_twitter';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_twitter d
       USING (SELECT twitter_id, 
                     request_token, 
                     request_token_secret, 
                     access_token, 
                     access_token_secret, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM user_twitter_g4
              ) s
          ON (d.twitter_id = s.twitter_id)
        WHEN MATCHED THEN UPDATE 
         SET d.request_token        = s.request_token,
             d.request_token_secret = s.request_token_secret,
             d.access_token         = s.access_token,
             d.access_token_secret  = s.access_token_secret,
             d.created_by           = s.created_by,
             d.date_created         = s.date_created,
             d.modified_by          = s.modified_by,
             d.date_modified        = s.date_modified,
             d.version              = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.twitter_id, 
                d.request_token, 
                d.request_token_secret, 
                d.access_token, 
                d.access_token_secret, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.twitter_id, 
                s.request_token, 
                s.request_token_secret, 
                s.access_token, 
                s.access_token_secret, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;
  
  
  v_msg      := 'MERGE user_facebook';
  v_tbl_name := 'user_facebook';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_facebook d
       USING (SELECT facebook_id, 
                     user_id, 
                     access_token, 
                     created_by, 
                     date_created, 
                     modified_by, 
                     date_modified, 
                     version
                FROM user_facebook_g4
              ) s
          ON (d.facebook_id = s.facebook_id)
        WHEN MATCHED THEN UPDATE 
         SET d.user_id       = s.user_id,
             d.access_token  = s.access_token,
             d.created_by    = s.created_by,
             d.date_created  = s.date_created,
             d.modified_by   = s.modified_by,
             d.date_modified = s.date_modified,
             d.version       = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.facebook_id, 
                d.user_id, 
                d.access_token, 
                d.created_by, 
                d.date_created, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.facebook_id, 
                s.user_id, 
                s.access_token, 
                s.created_by, 
                s.date_created, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;
  
  
  v_msg      := 'MERGE user_country_changes';
  v_tbl_name := 'user_country_changes';
  IF fnc_check_g5_tbl(v_tbl_name) <> 'X' AND fnc_check_g4_tbl(v_g4_schema, v_tbl_name) <> 'X' THEN

       MERGE INTO user_country_changes d
       USING (SELECT user_country_changes_id, 
                     import_file_id, 
                     user_id, 
                     old_country_id, 
                     old_campaign_nbr, 
                     old_awardbanq_nbr, 
                     old_centrax_id, 
                     new_country_id, 
                     new_campaign_nbr, 
                     new_awardbanq_nbr, 
                     new_centrax_id, 
                     balance_to_move, 
                     is_processed, 
                     message, 
                     date_created, 
                     created_by, 
                     modified_by, 
                     date_modified, 
                     version
                FROM user_country_changes_g4
              ) s
          ON (d.user_country_changes_id = s.user_country_changes_id)
        WHEN MATCHED THEN UPDATE 
         SET d.import_file_id          = s.import_file_id,
             d.user_id                 = s.user_id,
             d.old_country_id          = s.old_country_id,
             d.old_campaign_nbr        = s.old_campaign_nbr,
             d.old_awardbanq_nbr       = s.old_awardbanq_nbr,
             d.old_centrax_id          = s.old_centrax_id,
             d.new_country_id          = s.new_country_id,
             d.new_campaign_nbr        = s.new_campaign_nbr,
             d.new_awardbanq_nbr       = s.new_awardbanq_nbr,
             d.new_centrax_id          = s.new_centrax_id,
             d.balance_to_move         = s.balance_to_move,
             d.is_processed            = s.is_processed,
             d.message                 = s.message,
             d.date_created            = s.date_created,
             d.created_by              = s.created_by,
             d.modified_by             = s.modified_by,
             d.date_modified           = s.date_modified,
             d.version                 = s.version
        WHEN NOT MATCHED THEN INSERT
             (  d.user_country_changes_id, 
                d.import_file_id, 
                d.user_id, 
                d.old_country_id, 
                d.old_campaign_nbr, 
                d.old_awardbanq_nbr, 
                d.old_centrax_id, 
                d.new_country_id, 
                d.new_campaign_nbr, 
                d.new_awardbanq_nbr, 
                d.new_centrax_id, 
                d.balance_to_move, 
                d.is_processed, 
                d.message, 
                d.date_created, 
                d.created_by, 
                d.modified_by, 
                d.date_modified, 
                d.version
              )
      VALUES (  s.user_country_changes_id, 
                s.import_file_id, 
                s.user_id, 
                s.old_country_id, 
                s.old_campaign_nbr, 
                s.old_awardbanq_nbr, 
                s.old_centrax_id, 
                s.new_country_id, 
                s.new_campaign_nbr, 
                s.new_awardbanq_nbr, 
                s.new_centrax_id, 
                s.balance_to_move, 
                s.is_processed, 
                s.message, 
                s.date_created, 
                s.created_by, 
                s.modified_by, 
                s.date_modified, 
                s.version
              );
    show_msg_count(v_msg, SQL%ROWCOUNT);
    
  ELSE
    dbms_output.put_line('Table not found in G4 or G5: '||v_tbl_name);
  END IF;
--5. Participant related tables End

  v_exe_log_msg := 'Procedure completed.';
  prc_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_exe_log_msg,
                          NULL);
  COMMIT;
  po_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    po_return_code := 99;
    v_exe_log_msg := 'Error at stage:'||v_msg||' message:'||SQLERRM;
    prc_execution_log_entry(C_process_name,C_release_level,C_severity_e,
                            v_exe_log_msg,
                            NULL);
    COMMIT;
    
END;
/

DECLARE

po_return_code  NUMBER;

BEGIN


prc_tier_1_data_convert
 ('SourcePrefix',
  po_return_code);

END;
/