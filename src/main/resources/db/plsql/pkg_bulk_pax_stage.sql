CREATE OR REPLACE PACKAGE pkg_bulk_pax_stage AS
/*****************************************************************
 Purpose: Loads the participant file into the stage table by processing the file records in bulk.

 MODIFICATION HISTORY
 Person                       Date        Comments
 ---------                   ------     -------------------------------------------
 J Flees                    12/20/2011  Initial Creation
 Ravi Dhanekula             07/17/2013  Changed the process to be useful for G5.
 nagarajs                   10/30/2015  JIRA- IBC-2 - Add 5 more characteristics to the participant file load
*****************************************************************/
   -- package types
   TYPE rec_file_stage IS RECORD
   (  -- stage table fields
      import_record_id        stage_pax_import_record.import_record_id     %TYPE,
      import_file_id          stage_pax_import_record.import_file_id       %TYPE,
      action_type             stage_pax_import_record.action_type          %TYPE,
      user_id                 stage_pax_import_record.user_id              %TYPE,
      user_name               stage_pax_import_record.user_name            %TYPE,
      first_name              stage_pax_import_record.first_name           %TYPE,
      middle_name             stage_pax_import_record.middle_name          %TYPE,
      last_name               stage_pax_import_record.last_name            %TYPE,
      suffix                  stage_pax_import_record.suffix               %TYPE,
      ssn                     stage_pax_import_record.ssn                  %TYPE,
      birth_date              stage_pax_import_record.birth_date           %TYPE,
      gender                  stage_pax_import_record.gender               %TYPE,
      active                  stage_pax_import_record.active               %TYPE,
      status                  stage_pax_import_record.status               %TYPE,      
      terms_acceptance        stage_pax_import_record.terms_acceptance     %TYPE,
      user_id_accepted        stage_pax_import_record.user_id_accepted     %TYPE,
      date_terms_accepted     stage_pax_import_record.date_terms_accepted  %TYPE,
      email_address           stage_pax_import_record.email_address        %TYPE,
      email_address_type      stage_pax_import_record.email_address_type   %TYPE,
      text_message_address    stage_pax_import_record.text_message_address %TYPE,    
      address_1               stage_pax_import_record.address_1            %TYPE,
      address_2               stage_pax_import_record.address_2            %TYPE,
      address_3               stage_pax_import_record.address_3            %TYPE,
      address_4               stage_pax_import_record.address_4            %TYPE,
      address_5               stage_pax_import_record.address_5            %TYPE,
      address_6               stage_pax_import_record.address_6            %TYPE,
      city                    stage_pax_import_record.city                 %TYPE,
      state                   stage_pax_import_record.state                %TYPE,
      country_id              stage_pax_import_record.country_id           %TYPE,
      country_code            stage_pax_import_record.country_code         %TYPE,
      postal_code             stage_pax_import_record.postal_code          %TYPE,
      address_type            stage_pax_import_record.address_type         %TYPE,
      personal_phone_number   stage_pax_import_record.personal_phone_number%TYPE,
      business_phone_number   stage_pax_import_record.business_phone_number%TYPE,
      cell_phone_number       stage_pax_import_record.cell_phone_number    %TYPE,
      employer_id             stage_pax_import_record.employer_id          %TYPE,
      employer_name           stage_pax_import_record.employer_name        %TYPE,
      job_position            stage_pax_import_record.job_position         %TYPE,
      department              stage_pax_import_record.department           %TYPE,
      hire_date               stage_pax_import_record.hire_date            %TYPE,
      termination_date        stage_pax_import_record.termination_date     %TYPE,
      node_id1                stage_pax_import_record.node_id1             %TYPE,
      node_id2                stage_pax_import_record.node_id2             %TYPE,
      node_id3                stage_pax_import_record.node_id3             %TYPE,
      node_id4                stage_pax_import_record.node_id4             %TYPE,
      node_id5                stage_pax_import_record.node_id5             %TYPE,
      node_name1              stage_pax_import_record.node_name1           %TYPE,
      node_name2              stage_pax_import_record.node_name2           %TYPE,
      node_name3              stage_pax_import_record.node_name3           %TYPE,
      node_name4              stage_pax_import_record.node_name4           %TYPE,
      node_name5              stage_pax_import_record.node_name5           %TYPE,
      node_relationship1      stage_pax_import_record.node_relationship1   %TYPE,
      node_relationship2      stage_pax_import_record.node_relationship2   %TYPE,
      node_relationship3      stage_pax_import_record.node_relationship3   %TYPE,
      node_relationship4      stage_pax_import_record.node_relationship4   %TYPE,
      node_relationship5      stage_pax_import_record.node_relationship5   %TYPE,
      characteristic_id1      stage_pax_import_record.characteristic_id1   %TYPE,
      characteristic_id2      stage_pax_import_record.characteristic_id2   %TYPE,
      characteristic_id3      stage_pax_import_record.characteristic_id3   %TYPE,
      characteristic_id4      stage_pax_import_record.characteristic_id4   %TYPE,
      characteristic_id5      stage_pax_import_record.characteristic_id5   %TYPE,
      characteristic_id6      stage_pax_import_record.characteristic_id6   %TYPE,
      characteristic_id7      stage_pax_import_record.characteristic_id7   %TYPE,
      characteristic_id8      stage_pax_import_record.characteristic_id8   %TYPE,
      characteristic_id9      stage_pax_import_record.characteristic_id9   %TYPE,
      characteristic_id10      stage_pax_import_record.characteristic_id10   %TYPE,
      characteristic_id11      stage_pax_import_record.characteristic_id11   %TYPE,
      characteristic_id12      stage_pax_import_record.characteristic_id12   %TYPE,
      characteristic_id13      stage_pax_import_record.characteristic_id13   %TYPE,
      characteristic_id14      stage_pax_import_record.characteristic_id14   %TYPE,
      characteristic_id15      stage_pax_import_record.characteristic_id15   %TYPE,
      characteristic_id16      stage_pax_import_record.characteristic_id16   %TYPE, --10/30/2015 Start
      characteristic_id17      stage_pax_import_record.characteristic_id17   %TYPE,
      characteristic_id18      stage_pax_import_record.characteristic_id18   %TYPE,
      characteristic_id19      stage_pax_import_record.characteristic_id19   %TYPE,
      characteristic_id20      stage_pax_import_record.characteristic_id20   %TYPE, --10/30/2015 End
      characteristic_name1    stage_pax_import_record.characteristic_name1 %TYPE,
      characteristic_name2    stage_pax_import_record.characteristic_name2 %TYPE,
      characteristic_name3    stage_pax_import_record.characteristic_name3 %TYPE,
      characteristic_name4    stage_pax_import_record.characteristic_name4 %TYPE,
      characteristic_name5    stage_pax_import_record.characteristic_name5 %TYPE,
      characteristic_name6    stage_pax_import_record.characteristic_name6 %TYPE,
      characteristic_name7    stage_pax_import_record.characteristic_name7 %TYPE,
      characteristic_name8    stage_pax_import_record.characteristic_name8 %TYPE,
      characteristic_name9    stage_pax_import_record.characteristic_name9 %TYPE,
      characteristic_name10    stage_pax_import_record.characteristic_name10 %TYPE,
      characteristic_name11    stage_pax_import_record.characteristic_name11 %TYPE,
      characteristic_name12    stage_pax_import_record.characteristic_name12 %TYPE,
      characteristic_name13    stage_pax_import_record.characteristic_name13 %TYPE,
      characteristic_name14    stage_pax_import_record.characteristic_name14 %TYPE,
      characteristic_name15    stage_pax_import_record.characteristic_name15 %TYPE,
      characteristic_name16    stage_pax_import_record.characteristic_name16 %TYPE, --10/30/2015 Start
      characteristic_name17    stage_pax_import_record.characteristic_name17 %TYPE,
      characteristic_name18    stage_pax_import_record.characteristic_name18 %TYPE,
      characteristic_name19    stage_pax_import_record.characteristic_name19 %TYPE,
      characteristic_name20   stage_pax_import_record.characteristic_name20%TYPE,   --10/30/2015 Start
      characteristic_value1   stage_pax_import_record.characteristic_value1%TYPE,
      characteristic_value2   stage_pax_import_record.characteristic_value2%TYPE,
      characteristic_value3   stage_pax_import_record.characteristic_value3%TYPE,
      characteristic_value4   stage_pax_import_record.characteristic_value4%TYPE,
      characteristic_value5   stage_pax_import_record.characteristic_value5%TYPE,
      characteristic_value6   stage_pax_import_record.characteristic_value6%TYPE,
      characteristic_value7   stage_pax_import_record.characteristic_value7%TYPE,
      characteristic_value8   stage_pax_import_record.characteristic_value8%TYPE,
      characteristic_value9   stage_pax_import_record.characteristic_value9%TYPE,
      characteristic_value10   stage_pax_import_record.characteristic_value10%TYPE,
      characteristic_value11   stage_pax_import_record.characteristic_value11%TYPE,
      characteristic_value12   stage_pax_import_record.characteristic_value12%TYPE,
      characteristic_value13   stage_pax_import_record.characteristic_value13%TYPE,
      characteristic_value14   stage_pax_import_record.characteristic_value14%TYPE,
      characteristic_value15   stage_pax_import_record.characteristic_value15%TYPE,
      characteristic_value16   stage_pax_import_record.characteristic_value16%TYPE, --10/30/2015 Start
      characteristic_value17   stage_pax_import_record.characteristic_value17%TYPE,
      characteristic_value18   stage_pax_import_record.characteristic_value18%TYPE,
      characteristic_value19   stage_pax_import_record.characteristic_value19%TYPE,
      characteristic_value20   stage_pax_import_record.characteristic_value20%TYPE, --10/30/2015 End
      role_id1                stage_pax_import_record.role_id1             %TYPE,
      role_id2                stage_pax_import_record.role_id2             %TYPE,
      role_id3                stage_pax_import_record.role_id3             %TYPE,
      role_id4                stage_pax_import_record.role_id4             %TYPE,
      role_id5                stage_pax_import_record.role_id5             %TYPE,
      role_description1       stage_pax_import_record.role_description1    %TYPE,
      role_description2       stage_pax_import_record.role_description2    %TYPE,
      role_description3       stage_pax_import_record.role_description3    %TYPE,
      role_description4       stage_pax_import_record.role_description4    %TYPE,
      role_description5       stage_pax_import_record.role_description5    %TYPE,
      language_id               stage_pax_import_record.language_id           %TYPE,
      sso_id               stage_pax_import_record.sso_id           %TYPE,
     -- emp_status_code         stage_pax_import_record.emp_status_code      %TYPE,
   --   job_key                 stage_pax_import_record.job_key              %TYPE,
   --   emp_classification      stage_pax_import_record.emp_classification   %TYPE,
     -- responsibility_code     stage_pax_import_record.responsibility_code  %TYPE,
  --    dominant_jfc            stage_pax_import_record.dominant_jfc         %TYPE,
   --   bus_unit_description    stage_pax_import_record.bus_unit_description %TYPE,
   --   company_name            stage_pax_import_record.company_name         %TYPE,
   --   perner_id               stage_pax_import_record.perner_id            %TYPE,
  --    empl_cont_ind           stage_pax_import_record.empl_cont_ind        %TYPE,

      -- processing fields
      rec_indx                INTEGER,
      ssn_length              INTEGER,
      birth_date_str          VARCHAR2(30),
      hire_date_str           VARCHAR2(30),
      termination_date_str    VARCHAR2(30),    
      country                 stage_pax_import_record.country_code         %TYPE,
      email_address_type_name stage_pax_import_record.email_address_type   %TYPE,
      address_type_name       stage_pax_import_record.address_type         %TYPE,
      node_relationship1_name stage_pax_import_record.node_relationship1   %TYPE,
      node_relationship2_name stage_pax_import_record.node_relationship2   %TYPE,
      node_relationship3_name stage_pax_import_record.node_relationship3   %TYPE,
      node_relationship4_name stage_pax_import_record.node_relationship4   %TYPE,
      node_relationship5_name stage_pax_import_record.node_relationship5   %TYPE,
      language_id_str         stage_pax_import_record.language_id          %TYPE,
      suffix_name             stage_pax_import_record.suffix               %TYPE  --09/23/2013 Defect #4432
   );

   TYPE tab_file_stage IS TABLE OF rec_file_stage;

   -- public functions
   FUNCTION fnc_pipe_file_records
   RETURN tab_file_stage PIPELINED;

   -- public procedures
   PROCEDURE prc_stage_pax_file
   ( p_file_name        IN  VARCHAR2,
     p_out_returncode   OUT NUMBER
   );
END pkg_bulk_pax_stage;
/
CREATE OR REPLACE PACKAGE BODY pkg_bulk_pax_stage AS
/*****************************************************************
 Purpose: Loads the participant file into the stage table by processing the file records in bulk.

MODIFICATION HISTORY
 Person      Date          Comments
 ---------   ------       -------------------------------------------
 J Flees     12/20/2011   Initial Creation.
                          Package logic originally based on prc_stage_participant_load.
 Ravi Dhanekula  07/17/2013  Changed the process to be useful for G5.
                 07/23/2013  Added country status validation.
                 09/10/2013  Fixed defect # 4198 
                 09/18/2013  Fixed the defect # 4415 and defect # 4422
 Nagarajs        09/23/2013  Fixed Defect #4432 - Add Suffix validation
                             Fixed Defect #4593 - SSN validation
Ravi Dhanekula   11/18/2013  Fixed bug # 49700
                 12/03/2013  Fixed bug # 50186 Removed LOWER command for all characteristic values.
Suresh J         01/27/2014  Fixed bug # 50929 Added UPPER function to email address type fiel       
Ravi Dhanekula   03/27/2014  Fixed the bug # 52223  
Swati            10/14/2014  Bug 57149 - Pax load Insert terminated Pax
Suresh J         12/24/2014  Bug 57048 - Quotes allowed in Name fields  
Ramkumar         03/03/2015  Bug 59780 - Error when email_addr has apostrophe(') before @
Swati            03/11/2015  Bug 59953 - File load not validating phone number format - causing deposits to fail
Swati            04/13/2015  Bug 60602 - Participant file load does not accept accent marks/special characters in email address field - errors out
nagarajs         10/30/2015  JIRA- IBC-2 - Add 5 more characteristics to the participant file load
nagarajs         11/06/2015  Bug 64312 - Added fix to not allow more than 30 charcters for city field
nagarajs        12/23/2015  Bug 65043 - move dupe user_name check from p_rpt_file_record_err to new PRC p_rpt_file_record_err_stg
nagarajs        04/11/2016  Bug 66374 - Able to load PAX for an inactive country
Ravi Dhanekula  05/05/2017  G6-2324  - Inactivation without employer information failing.
Loganathan      04/15/2019  Bug 79028 - tuning change needed for PKG_BULK_PAX_STAGE
*****************************************************************/
-- package constants
gc_pkg_name          CONSTANT execution_log.process_name%type := UPPER('pkg_bulk_pax_stage');
gc_release_level     CONSTANT execution_log.release_level%type := '1.0';

gc_error             CONSTANT execution_log.severity%TYPE := 'ERROR';
gc_debug             CONSTANT execution_log.severity%TYPE := 'DEBUG';
gc_warn              CONSTANT execution_log.severity%TYPE := 'WARN';
gc_info              CONSTANT execution_log.severity%TYPE := 'INFO';

gc_created_by                    CONSTANT stage_pax_import_record.created_by%TYPE := 0;

gc_delimiter                     CONSTANT VARCHAR2(10):='|';

gc_admin_err_invalid_property    CONSTANT import_record_error.item_key%TYPE := 'admin.fileload.errors.INVALID_PROPERTY';
gc_sys_err_date                  CONSTANT import_record_error.item_key%TYPE := 'system.errors.DATE';
gc_sys_err_not_required          CONSTANT import_record_error.item_key%TYPE := 'system.errors.NOT_REQUIRED';
gc_sys_char_not_valid          CONSTANT import_record_error.item_key%TYPE := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';--07/17/2013
gc_sys_parsing_err          CONSTANT import_record_error.item_key%TYPE := 'admin.fileload.errors.PARSING'; --07/17/2013
gc_sys_err_required       CONSTANT import_record_error.item_key%TYPE := 'system.errors.REQUIRED'; --09/10/2013
gs_dup_user_error         CONSTANT import_record_error.item_key%TYPE := 'participant.errors.DUPLICATE_USER_NAME'; --11/18/2013
gs_city_length_error      CONSTANT import_record_error.item_key%TYPE := 'participant.errors.VALUE_LENGTH_EXCEEDS_30'; --11/06/2015




-- global package variables
gv_timestamp                     import_record_error.date_created%TYPE := SYSDATE;

gv_tab_field_parse               tab_file_stage;
gv_tab_file_rec                  dbms_sql.VARCHAR2_table;
gv_tab_import_record_id          dbms_sql.NUMBER_table;

---------------------
---------------------
-- private procedures
---------------------
-- Reads records from the data file
PROCEDURE p_read_file
( p_file_handler     IN utl_file.file_type,
  p_read_max_ctr     IN INTEGER,
  p_end_of_file      IN OUT BOOLEAN,
  p_read_rec_count      OUT INTEGER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_read_file');

   v_stage              execution_log.text_line%TYPE;
   v_text               VARCHAR2(4000);

   -- get the next import record ID for the specified number of records
   CURSOR cur_next_import_record_id
   ( cv_rec_cnt   INTEGER
   ) IS
   SELECT import_record_pk_sq.NEXTVAL AS import_record_id
     FROM dual
  CONNECT BY LEVEL <= cv_rec_cnt
      ;

BEGIN
   -- reset table variable
   v_stage := 'Reset table variables';
   p_read_rec_count := 0;
   gv_tab_file_rec.DELETE;
   gv_tab_import_record_id.DELETE;

   v_stage := 'Read records';
   BEGIN -- read file block

      WHILE (p_read_rec_count < p_read_max_ctr) LOOP
         utl_file.get_line(p_file_handler, v_text);

         IF (UPPER(SUBSTR(v_text, 1, 8)) = 'LOGIN_ID') THEN
            -- skip header record
            NULL;
         ELSE
            -- process data record
            p_read_rec_count := p_read_rec_count + 1;
            gv_tab_file_rec(p_read_rec_count) := v_text;
         END IF;

      END LOOP; -- file read

   EXCEPTION
      WHEN no_data_found THEN
         -- end of file
         p_end_of_file := TRUE;
   END;  -- read file block

   -- associate each record read with an import record ID
   IF (p_read_rec_count > 0) THEN

      v_stage := 'OPEN cur_next_import_record_id(' || p_read_rec_count || ')';
      OPEN cur_next_import_record_id(p_read_rec_count);
      v_stage := 'FETCH cur_next_import_record_id(' || p_read_rec_count || ')';
      FETCH cur_next_import_record_id
       BULK COLLECT
       INTO gv_tab_import_record_id;
      CLOSE cur_next_import_record_id;

   END IF;  -- process read records

   v_stage := 'Records read: ' || p_read_rec_count;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ': ' || v_stage, NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ': ' || v_stage, NULL);
      RAISE;
END p_read_file;

---------------------
-- Parses field data from file records
PROCEDURE p_parse_file_records
( p_import_file_id   IN stage_pax_import_record.import_file_id%TYPE,
  p_recs_in_file_cnt IN INTEGER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_parse_file_records');

   v_stage              execution_log.text_line%TYPE;
   v_stage_prefix       execution_log.text_line%TYPE;
   v_stage_suffix       execution_log.text_line%TYPE;
   v_ssn                stage_pax_import_record.ssn%TYPE;
   v_file_record_nbr    INTEGER;


   -- match file record CMS name values to CMS codes
   CURSOR cur_cms_code IS
   
   WITH cms_email_type AS
   (  -- get CMS email type codes
   --01/27/2014
      SELECT UPPER(ccv.cms_name) cms_name,     
             ccv.cms_code
        FROM vw_cms_code_value ccv
       WHERE ccv.asset_code = 'picklist.emailtype.items'
         AND ccv.locale = 'en_US'
         --AND ccv.status = 'true'
   ), cms_addr_type AS
   (  -- get CMS address type codes
      SELECT ccv.cms_name,
             ccv.cms_code
        FROM vw_cms_code_value ccv
       WHERE ccv.asset_code = 'picklist.addresstype.items'
         AND ccv.locale = 'en_US'
       --  AND ccv.status = 'true'
   ), cms_node_role AS
   (  -- get CMS node role codes
      SELECT UPPER(ccv.cms_name) cms_name,
             ccv.cms_code
        FROM vw_cms_code_value ccv
       WHERE ccv.asset_code = 'picklist.hierarchyrole.type.items'
         AND ccv.locale = 'en_US'
    --     AND ccv.status = 'true'
   )
--   cms_suffix AS             --09/23/2013 --03/27/2014   Bug # 52223 Removed the check as the load porcess will add the suffixes to picklist if they are not present.
--   (  -- get CMS node role codes
--      SELECT UPPER(ccv.cms_name) cms_name,
--             UPPER(ccv.cms_code) cms_code 
--        FROM vw_cms_code_value ccv
--       WHERE ccv.asset_code = 'picklist.suffixtype.items'
--         AND ccv.locale = 'en_US'
--    --     AND ccv.status = 'true'
--   )
   SELECT fr.rec_indx,
          cet.cms_code AS email_address_type,
          cat.cms_code AS address_type,
          cnr1.cms_code AS node_relationship1,
          cnr2.cms_code AS node_relationship2,
          cnr3.cms_code AS node_relationship3,
          cnr4.cms_code AS node_relationship4,
          cnr5.cms_code AS node_relationship5
--          cs.cms_code   AS suffix    --09/23/2013 --03/27/2014 
     FROM TABLE(fnc_pipe_file_records) fr,
          cms_email_type cet,   -- email type
          cms_addr_type  cat,   -- address type
          cms_node_role  cnr1,  -- node role 1
          cms_node_role  cnr2,  -- node role 2
          cms_node_role  cnr3,  -- node role 3
          cms_node_role  cnr4,  -- node role 4
          cms_node_role  cnr5 -- node role 5
          --cms_suffix     cs     -- suffix   --09/23/2013
       -- outer join because file value may be NULL or invalid
    WHERE UPPER(fr.email_address_type_name) = cet.cms_name (+)      --01/27/2014
      AND fr.address_type_name       = cat.cms_name (+)
      AND UPPER(fr.node_relationship1_name) = cnr1.cms_name (+)
      AND UPPER(fr.node_relationship2_name) = cnr2.cms_name (+)
      AND UPPER(fr.node_relationship3_name) = cnr3.cms_name (+)
      AND UPPER(fr.node_relationship4_name) = cnr4.cms_name (+)
      AND UPPER(fr.node_relationship5_name) = cnr5.cms_name (+)
     -- AND UPPER(fr.suffix_name)             = cs.cms_name (+)   --09/23/2013 --03/27/2014 
      ;

   -- ID lookup for file record characteristic name
   CURSOR cur_lookup_user_char_id IS
   WITH user_characteristic AS
   (  -- get USER characteristics
      SELECT cn.characteristic_id,
             cn.cm_name
        FROM vue_characteristic_name cn
       WHERE cn.characteristic_type = 'USER'
         AND cn.domain_id IS NULL
   )
   SELECT fr.rec_indx,
          uc1.characteristic_id AS characteristic_id1,
          uc2.characteristic_id AS characteristic_id2,
          uc3.characteristic_id AS characteristic_id3,
          uc4.characteristic_id AS characteristic_id4,
          uc5.characteristic_id AS characteristic_id5,
          uc6.characteristic_id AS characteristic_id6,
          uc7.characteristic_id AS characteristic_id7,
          uc8.characteristic_id AS characteristic_id8,
          uc9.characteristic_id AS characteristic_id9,
          uc10.characteristic_id AS characteristic_id10,
          uc11.characteristic_id AS characteristic_id11,
          uc12.characteristic_id AS characteristic_id12,
          uc13.characteristic_id AS characteristic_id13,
          uc14.characteristic_id AS characteristic_id14,
          uc15.characteristic_id AS characteristic_id15,
          uc16.characteristic_id AS characteristic_id16,    --10/30/2015 Start
          uc17.characteristic_id AS characteristic_id17,
          uc18.characteristic_id AS characteristic_id18,
          uc19.characteristic_id AS characteristic_id19,
          uc20.characteristic_id AS characteristic_id20     --10/30/2015 End
     FROM TABLE(fnc_pipe_file_records) fr,
          user_characteristic uc1,  -- characteristic 1
          user_characteristic uc2,  -- characteristic 2
          user_characteristic uc3,  -- characteristic 3
          user_characteristic uc4,  -- characteristic 4
          user_characteristic uc5,   -- characteristic 5
           user_characteristic uc6,  -- characteristic 6
          user_characteristic uc7,  -- characteristic 7
          user_characteristic uc8,  -- characteristic 8
          user_characteristic uc9,  -- characteristic 9
          user_characteristic uc10,   -- characteristic 10
           user_characteristic uc11,  -- characteristic 11
          user_characteristic uc12,  -- characteristic 12
          user_characteristic uc13,  -- characteristic 13
          user_characteristic uc14,  -- characteristic 14
          user_characteristic uc15,   -- characteristic 15
          user_characteristic uc16,  -- characteristic 16   --10/30/2015 Start
          user_characteristic uc17,  -- characteristic 17
          user_characteristic uc18,  -- characteristic 18
          user_characteristic uc19,  -- characteristic 19
          user_characteristic uc20   -- characteristic 20    --10/30/2015 End
    WHERE NOT (-- skip records with no characteristic names
                  fr.characteristic_name1 IS NULL
              AND fr.characteristic_name2 IS NULL
              AND fr.characteristic_name3 IS NULL
              AND fr.characteristic_name4 IS NULL
              AND fr.characteristic_name5 IS NULL
              AND fr.characteristic_name6 IS NULL
              AND fr.characteristic_name7 IS NULL
              AND fr.characteristic_name8 IS NULL
              AND fr.characteristic_name9 IS NULL
              AND fr.characteristic_name10 IS NULL
              AND fr.characteristic_name11 IS NULL
              AND fr.characteristic_name12 IS NULL
              AND fr.characteristic_name13 IS NULL
              AND fr.characteristic_name14 IS NULL
              AND fr.characteristic_name15 IS NULL
              AND fr.characteristic_name16 IS NULL  --10/30/2015 Start
              AND fr.characteristic_name17 IS NULL
              AND fr.characteristic_name18 IS NULL
              AND fr.characteristic_name19 IS NULL
              AND fr.characteristic_name20 IS NULL  --10/30/2015 End
              )
       -- outer join because file value may be NULL or invalid
      AND UPPER(fr.characteristic_name1) = uc1.cm_name (+)
      AND UPPER(fr.characteristic_name2) = uc2.cm_name (+)
      AND UPPER(fr.characteristic_name3) = uc3.cm_name (+)
      AND UPPER(fr.characteristic_name4) = uc4.cm_name (+)
      AND UPPER(fr.characteristic_name5) = uc5.cm_name (+)
      AND UPPER(fr.characteristic_name6) = uc6.cm_name (+)
      AND UPPER(fr.characteristic_name7) = uc7.cm_name (+)
      AND UPPER(fr.characteristic_name8) = uc8.cm_name (+)
      AND UPPER(fr.characteristic_name9) = uc9.cm_name (+)
      AND UPPER(fr.characteristic_name10) = uc10.cm_name (+)
       AND UPPER(fr.characteristic_name11) = uc11.cm_name (+)
      AND UPPER(fr.characteristic_name12) = uc12.cm_name (+)
      AND UPPER(fr.characteristic_name13) = uc13.cm_name (+)
      AND UPPER(fr.characteristic_name14) = uc14.cm_name (+)
      AND UPPER(fr.characteristic_name15) = uc15.cm_name (+)
      AND UPPER(fr.characteristic_name16) = uc16.cm_name (+)    --10/30/2015 Start
      AND UPPER(fr.characteristic_name17) = uc17.cm_name (+)
      AND UPPER(fr.characteristic_name18) = uc18.cm_name (+)
      AND UPPER(fr.characteristic_name19) = uc19.cm_name (+)
      AND UPPER(fr.characteristic_name20) = uc20.cm_name (+)    --10/30/2015 End
      ;

   -- ID lookup for file record role description
   CURSOR cur_lookup_role_id IS
   SELECT fr.rec_indx,
          r1.role_id AS role_id1,
          r2.role_id AS role_id2,
          r3.role_id AS role_id3,
          r4.role_id AS role_id4,
          r5.role_id AS role_id5
     FROM TABLE(fnc_pipe_file_records) fr,
          role r1,  -- role 1
          role r2,  -- role 2
          role r3,  -- role 3
          role r4,  -- role 4
          role r5   -- role 5
    WHERE NOT (-- skip records with no values
                  fr.role_description1 IS NULL
              AND fr.role_description2 IS NULL
              AND fr.role_description3 IS NULL
              AND fr.role_description4 IS NULL
              AND fr.role_description5 IS NULL
              )
       -- outer join because file value may be NULL or invalid
      AND UPPER(fr.role_description1) = UPPER(r1.name (+))
      AND UPPER(fr.role_description2) = UPPER(r2.name (+))
      AND UPPER(fr.role_description3) = UPPER(r3.name (+))
      AND UPPER(fr.role_description4) = UPPER(r4.name (+))
      AND UPPER(fr.role_description5) = UPPER(r5.name (+))
      ;

    -- ID lookup for file record country
   CURSOR cur_lookup_country_id IS
   SELECT fr.rec_indx,
          NVL(ac.country_id,   aca.country_id)   AS country_id,
          NVL(ac.country_code, aca.country_code) AS country_code          
     FROM TABLE(fnc_pipe_file_records) fr,
          country ac,   -- address country
          -- join by country abbreviation in case country code join fails
          country aca   -- address country abbrev
       -- outer join because file value may be NULL or invalid
    WHERE LOWER(fr.country)      = ac.country_code (+)    
      AND UPPER(fr.country)      = aca.awardbanq_country_abbrev (+)
      ;
      
--Lookup for the language Id(Locale)
CURSOR cur_language_id_validation IS
SELECT fr.rec_indx,locale.cms_code
FROM
TABLE(fnc_pipe_file_records) fr,
 (SELECT distinct cms_code
              FROM vw_cms_code_value    
        WHERE asset_code      = 'default.locale.items'
                    AND cms_name is not null) locale
WHERE LOWER(fr.language_id_str) = LOWER(locale.cms_code);


BEGIN
   -- reset table variables
   v_stage := 'Reset table variables';
   gv_tab_field_parse.DELETE;
   v_file_record_nbr := p_recs_in_file_cnt - gv_tab_file_rec.COUNT;




   -- loop through file records parsing each field
   FOR indx IN gv_tab_file_rec.FIRST .. gv_tab_file_rec.LAST LOOP
      gv_tab_field_parse.EXTEND;
      v_stage_prefix := 'Parse record: ' || (v_file_record_nbr + indx) || ', field: ';

      -- assign key fields
      v_stage := v_stage_prefix || 'import_record_id';
      gv_tab_field_parse(indx).import_record_id := gv_tab_import_record_id(indx);

      v_stage := v_stage_prefix || 'import_file_id';
      gv_tab_field_parse(indx).import_file_id := p_import_file_id;

      -- parse record fields
      v_stage := v_stage_prefix || 'user_name';
      gv_tab_field_parse(indx).user_name               :=   UPPER(TRIM(fnc_pipe_parse(gv_tab_file_rec(indx),  1, gc_delimiter)));

      v_stage := v_stage_prefix || 'first_name';
      gv_tab_field_parse(indx).first_name              :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx),  2, gc_delimiter));

      v_stage := v_stage_prefix || 'middle_name';
      gv_tab_field_parse(indx).middle_name             :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx),  3, gc_delimiter));

      v_stage := v_stage_prefix || 'last_name';
      gv_tab_field_parse(indx).last_name               :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx),  4, gc_delimiter));

      v_stage := v_stage_prefix || 'suffix';
      gv_tab_field_parse(indx).suffix_name             :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx),  5, gc_delimiter));  --09/23/2013

      v_stage := v_stage_prefix || 'ssn';
      gv_tab_field_parse(indx).ssn                     :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx),  6, gc_delimiter));

      v_stage := v_stage_prefix || 'birth_date_str';
      gv_tab_field_parse(indx).birth_date_str          :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx),  7, gc_delimiter)); --Need to validate the date format ***********

      v_stage := v_stage_prefix || 'gender';
      gv_tab_field_parse(indx).gender                  :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx),  8, gc_delimiter));

      v_stage := v_stage_prefix || 'status';
      gv_tab_field_parse(indx).status                  :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx),  9, gc_delimiter));

      v_stage := v_stage_prefix || 'email_address';
      gv_tab_field_parse(indx).email_address           :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 10, gc_delimiter));

      v_stage := v_stage_prefix || 'email_address_type_name';
      gv_tab_field_parse(indx).email_address_type_name := REPLACE(TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 11, gc_delimiter)),CHR(13));

      v_stage := v_stage_prefix || 'text_message_address';
      gv_tab_field_parse(indx).text_message_address    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 12, gc_delimiter));

      v_stage := v_stage_prefix || 'address_type_name';
      gv_tab_field_parse(indx).address_type_name       :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 13, gc_delimiter));

      v_stage := v_stage_prefix || 'country';
      gv_tab_field_parse(indx).country                 :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 14, gc_delimiter));

      v_stage := v_stage_prefix || 'address_1';
      gv_tab_field_parse(indx).address_1               :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 15, gc_delimiter));

      v_stage := v_stage_prefix || 'address_2';
      gv_tab_field_parse(indx).address_2               :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 16, gc_delimiter));

      v_stage := v_stage_prefix || 'address_3';
      gv_tab_field_parse(indx).address_3               :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 17, gc_delimiter));

      v_stage := v_stage_prefix || 'address_4';
      gv_tab_field_parse(indx).address_4               :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 18, gc_delimiter));

      v_stage := v_stage_prefix || 'address_5';
      gv_tab_field_parse(indx).address_5               :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 19, gc_delimiter));

      v_stage := v_stage_prefix || 'address_6';
      gv_tab_field_parse(indx).address_6               :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 20, gc_delimiter));

      v_stage := v_stage_prefix || 'city';
      gv_tab_field_parse(indx).city                    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 21, gc_delimiter));

      v_stage := v_stage_prefix || 'state';
      gv_tab_field_parse(indx).state                   :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 22, gc_delimiter));

      v_stage := v_stage_prefix || 'postal_code';
      gv_tab_field_parse(indx).postal_code             :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 23, gc_delimiter));

      v_stage := v_stage_prefix || 'personal_phone_number';
      gv_tab_field_parse(indx).personal_phone_number   :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 24, gc_delimiter));

      v_stage := v_stage_prefix || 'business_phone_number';
      gv_tab_field_parse(indx).business_phone_number   :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 25, gc_delimiter));

      v_stage := v_stage_prefix || 'cell_phone_number';
      gv_tab_field_parse(indx).cell_phone_number       :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 26, gc_delimiter));

      v_stage := v_stage_prefix || 'employer_name';
      gv_tab_field_parse(indx).employer_name           :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 27, gc_delimiter));

      v_stage := v_stage_prefix || 'job_position';
      gv_tab_field_parse(indx).job_position            :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 28, gc_delimiter));

      v_stage := v_stage_prefix || 'department';
      gv_tab_field_parse(indx).department              :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 29, gc_delimiter));

      v_stage := v_stage_prefix || 'hire_date_str';
      gv_tab_field_parse(indx).hire_date_str           :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 30, gc_delimiter));

      v_stage := v_stage_prefix || 'termination_date_str';
      gv_tab_field_parse(indx).termination_date_str    := REPLACE(TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 31, gc_delimiter)),CHR(13));

      v_stage := v_stage_prefix || 'node_name1';
      gv_tab_field_parse(indx).node_name1              :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 32, gc_delimiter));

      v_stage := v_stage_prefix || 'node_relationship1_name';
      gv_tab_field_parse(indx).node_relationship1_name := REPLACE(TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 33, gc_delimiter)),CHR(13));

      v_stage := v_stage_prefix || 'node_name2';
      gv_tab_field_parse(indx).node_name2              :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 34, gc_delimiter));

      v_stage := v_stage_prefix || 'node_relationship2_name';
      gv_tab_field_parse(indx).node_relationship2_name := REPLACE(TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 35, gc_delimiter)),CHR(13));

      v_stage := v_stage_prefix || 'node_name3';
      gv_tab_field_parse(indx).node_name3              :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 36, gc_delimiter));

      v_stage := v_stage_prefix || 'node_relationship3_name';
      gv_tab_field_parse(indx).node_relationship3_name := REPLACE(TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 37, gc_delimiter)), CHR(13));

      v_stage := v_stage_prefix || 'node_name4';
      gv_tab_field_parse(indx).node_name4              :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 38, gc_delimiter));

      v_stage := v_stage_prefix || 'node_relationship4_name';
      gv_tab_field_parse(indx).node_relationship4_name := REPLACE(TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 39, gc_delimiter)),CHR(13));

      v_stage := v_stage_prefix || 'node_name5';
      gv_tab_field_parse(indx).node_name5              :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 40, gc_delimiter));

      v_stage := v_stage_prefix || 'node_relationship5_name';
      gv_tab_field_parse(indx).node_relationship5_name := REPLACE(TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 41, gc_delimiter)),CHR(13));     

      v_stage := v_stage_prefix || 'characteristic_name1';
      gv_tab_field_parse(indx).characteristic_name1    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 42, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value1';
      gv_tab_field_parse(indx).characteristic_value1   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 43, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name2';
      gv_tab_field_parse(indx).characteristic_name2    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 44, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value2';
      gv_tab_field_parse(indx).characteristic_value2   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 45, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name3';
      gv_tab_field_parse(indx).characteristic_name3    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 46, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value3';
      gv_tab_field_parse(indx).characteristic_value3   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 47, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name4';
      gv_tab_field_parse(indx).characteristic_name4    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 48, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value4';
      gv_tab_field_parse(indx).characteristic_value4   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 49, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name5';
      gv_tab_field_parse(indx).characteristic_name5    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 50, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value5';
      gv_tab_field_parse(indx).characteristic_value5   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 51, gc_delimiter));
      
      
       v_stage := v_stage_prefix || 'characteristic_name6';
      gv_tab_field_parse(indx).characteristic_name6    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 52, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value6';
      gv_tab_field_parse(indx).characteristic_value6   :=  TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 53, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name7';
      gv_tab_field_parse(indx).characteristic_name7    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 54, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value7';
      gv_tab_field_parse(indx).characteristic_value7   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 55, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name8';
      gv_tab_field_parse(indx).characteristic_name8    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 56, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value8';
      gv_tab_field_parse(indx).characteristic_value8   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 57, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name9';
      gv_tab_field_parse(indx).characteristic_name9    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 58, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value9';
      gv_tab_field_parse(indx).characteristic_value9   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 59, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name10';
      gv_tab_field_parse(indx).characteristic_name10    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 60, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value10';
      gv_tab_field_parse(indx).characteristic_value10   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 61, gc_delimiter));
      
      
       v_stage := v_stage_prefix || 'characteristic_name11';
      gv_tab_field_parse(indx).characteristic_name11    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 62, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value11';
      gv_tab_field_parse(indx).characteristic_value11   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 63, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name12';
      gv_tab_field_parse(indx).characteristic_name12    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 64, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value12';
      gv_tab_field_parse(indx).characteristic_value12   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 65, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name13';
      gv_tab_field_parse(indx).characteristic_name13    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 66, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value13';
      gv_tab_field_parse(indx).characteristic_value13   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 67, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name14';
      gv_tab_field_parse(indx).characteristic_name14    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 68, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value14';
      gv_tab_field_parse(indx).characteristic_value14   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 69, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_name15';
      gv_tab_field_parse(indx).characteristic_name15    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 70, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value15';
      gv_tab_field_parse(indx).characteristic_value15   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 71, gc_delimiter));
      
      v_stage := v_stage_prefix || 'characteristic_name16';                                                                         --10/30/2015 Start
      gv_tab_field_parse(indx).characteristic_name16    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 72, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value16';
      gv_tab_field_parse(indx).characteristic_value16   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 73, gc_delimiter));
      
      v_stage := v_stage_prefix || 'characteristic_name17';
      gv_tab_field_parse(indx).characteristic_name17    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 74, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value17';
      gv_tab_field_parse(indx).characteristic_value17   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 75, gc_delimiter));
      
      v_stage := v_stage_prefix || 'characteristic_name18';
      gv_tab_field_parse(indx).characteristic_name18    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 76, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value18';
      gv_tab_field_parse(indx).characteristic_value18   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 77, gc_delimiter));
      
      v_stage := v_stage_prefix || 'characteristic_name19';
      gv_tab_field_parse(indx).characteristic_name19    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 78, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value19';
      gv_tab_field_parse(indx).characteristic_value19   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 79, gc_delimiter));
      
      v_stage := v_stage_prefix || 'characteristic_name20';
      gv_tab_field_parse(indx).characteristic_name20    :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 80, gc_delimiter));

      v_stage := v_stage_prefix || 'characteristic_value20';
      gv_tab_field_parse(indx).characteristic_value20   :=   TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 81, gc_delimiter));             --10/30/2015 End
      
     v_stage := v_stage_prefix || 'role_description1';
      gv_tab_field_parse(indx).role_description1       := REPLACE(TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 82, gc_delimiter)),CHR(13));

      v_stage := v_stage_prefix || 'role_description2';
      gv_tab_field_parse(indx).role_description2       :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 83, gc_delimiter));

      v_stage := v_stage_prefix || 'role_description3';
      gv_tab_field_parse(indx).role_description3       :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 84, gc_delimiter));

      v_stage := v_stage_prefix || 'role_description4';
      gv_tab_field_parse(indx).role_description4       :=         TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 85, gc_delimiter));

      v_stage := v_stage_prefix || 'role_description5';
      gv_tab_field_parse(indx).role_description5       := REPLACE(TRIM(fnc_pipe_parse(gv_tab_file_rec(indx), 86, gc_delimiter)),CHR(13));
      
      v_stage := v_stage_prefix || 'language_id';
      gv_tab_field_parse(indx).language_id_str       :=         LOWER(fnc_pipe_parse(gv_tab_file_rec(indx), 87, gc_delimiter));

      v_stage := v_stage_prefix || 'sso_id';
      gv_tab_field_parse(indx).sso_id       := LOWER(fnc_pipe_parse(gv_tab_file_rec(indx), 88, gc_delimiter));

      -- Set derived fields
      v_stage := v_stage_prefix || 'derived fields';
      gv_tab_field_parse(indx).rec_indx := indx;
      gv_tab_field_parse(indx).ssn_length  := 0;
      IF (gv_tab_field_parse(indx).termination_date_str IS NULL) THEN
         gv_tab_field_parse(indx).active := 1;
         gv_tab_field_parse(indx).status := 'active';
      ELSIF (gv_tab_field_parse(indx).termination_date_str IS NOT NULL) AND ----05/05/2017..Start
            (gv_tab_field_parse(indx).job_position IS NULL) AND (gv_tab_field_parse(indx).department IS NULL) THEN
         gv_tab_field_parse(indx).active := 0;
         gv_tab_field_parse(indx).status := 'inactive';
         gv_tab_field_parse(indx).employer_name := NULL;
      ELSE
         gv_tab_field_parse(indx).active := 0;
         gv_tab_field_parse(indx).status := 'inactive';----05/05/2017..End
      END IF;



      -- convert date strings to dates
      BEGIN
         gv_tab_field_parse(indx).birth_date := TO_DATE(gv_tab_field_parse(indx).birth_date_str, 'MM/DD/YYYY');


      EXCEPTION
         WHEN OTHERS THEN
            NULL;
      END;













      BEGIN 
         gv_tab_field_parse(indx).hire_date := TO_DATE(gv_tab_field_parse(indx).hire_date_str, 'MM/DD/YYYY');
      EXCEPTION
         WHEN OTHERS THEN
            NULL;
      END;

      BEGIN        
         gv_tab_field_parse(indx).termination_date := TO_DATE(gv_tab_field_parse(indx).termination_date_str, 'MM/DD/YYYY');
      EXCEPTION
         WHEN OTHERS THEN
            NULL;
      END;

      -- convert from old encryption to the current encryption
      v_stage := v_stage_prefix || 'SSN convert';
      IF (gv_tab_field_parse(indx).ssn IS NOT NULL) THEN
         -- decrypt from old encryption
         --v_ssn := FNC_JAVA_DECRYPT_OLD(gv_tab_field_parse(indx).ssn); --09/23/2013
         gv_tab_field_parse(indx).ssn_length := LENGTH(gv_tab_field_parse(indx).ssn);--LENGTH(v_ssn); --09/23/2013

         -- encrypt with current encryption
         --gv_tab_field_parse(indx).ssn := FNC_JAVA_ENCRYPT(v_ssn);  --09/23/2013
      END IF; -- SSN NOT NULL

   END LOOP; -- FOR gv_tab_file_rec

   v_stage_suffix := 
         ' for import_record_id ' || gv_tab_field_parse(gv_tab_field_parse.FIRST).import_record_id
      || ' to '                   || gv_tab_field_parse(gv_tab_field_parse.LAST).import_record_id;

   -- get CMS code values
   v_stage := 'FOR cur_cms_code' || v_stage_suffix;
   FOR rec IN cur_cms_code LOOP
      gv_tab_field_parse(rec.rec_indx).email_address_type := rec.email_address_type;
      gv_tab_field_parse(rec.rec_indx).address_type       := rec.address_type;
      gv_tab_field_parse(rec.rec_indx).node_relationship1 := rec.node_relationship1;
      gv_tab_field_parse(rec.rec_indx).node_relationship2 := rec.node_relationship2;
      gv_tab_field_parse(rec.rec_indx).node_relationship3 := rec.node_relationship3;
      gv_tab_field_parse(rec.rec_indx).node_relationship4 := rec.node_relationship4;
      gv_tab_field_parse(rec.rec_indx).node_relationship5 := rec.node_relationship5;
--      gv_tab_field_parse(rec.rec_indx).suffix             := rec.suffix;   --09/23/2013 --03/27/2014
   END LOOP;

   -- lookup user characteristic IDs
   v_stage := 'FOR cur_lookup_user_char_id' || v_stage_suffix;
   FOR rec IN cur_lookup_user_char_id LOOP
      gv_tab_field_parse(rec.rec_indx).characteristic_id1 := rec.characteristic_id1;
      gv_tab_field_parse(rec.rec_indx).characteristic_id2 := rec.characteristic_id2;
      gv_tab_field_parse(rec.rec_indx).characteristic_id3 := rec.characteristic_id3;
      gv_tab_field_parse(rec.rec_indx).characteristic_id4 := rec.characteristic_id4;
      gv_tab_field_parse(rec.rec_indx).characteristic_id5 := rec.characteristic_id5;
       gv_tab_field_parse(rec.rec_indx).characteristic_id6 := rec.characteristic_id6;
      gv_tab_field_parse(rec.rec_indx).characteristic_id7 := rec.characteristic_id7;
      gv_tab_field_parse(rec.rec_indx).characteristic_id8 := rec.characteristic_id8;
      gv_tab_field_parse(rec.rec_indx).characteristic_id9 := rec.characteristic_id9;
      gv_tab_field_parse(rec.rec_indx).characteristic_id10 := rec.characteristic_id10;
       gv_tab_field_parse(rec.rec_indx).characteristic_id11 := rec.characteristic_id11;
      gv_tab_field_parse(rec.rec_indx).characteristic_id12 := rec.characteristic_id12;
      gv_tab_field_parse(rec.rec_indx).characteristic_id13 := rec.characteristic_id13;
      gv_tab_field_parse(rec.rec_indx).characteristic_id14 := rec.characteristic_id14;
      gv_tab_field_parse(rec.rec_indx).characteristic_id15 := rec.characteristic_id15;
      gv_tab_field_parse(rec.rec_indx).characteristic_id16 := rec.characteristic_id16;  --10/30/2015 Start
      gv_tab_field_parse(rec.rec_indx).characteristic_id17 := rec.characteristic_id17;
      gv_tab_field_parse(rec.rec_indx).characteristic_id18 := rec.characteristic_id18;
      gv_tab_field_parse(rec.rec_indx).characteristic_id19 := rec.characteristic_id19;
      gv_tab_field_parse(rec.rec_indx).characteristic_id20 := rec.characteristic_id20;  --10/30/2015 End
   END LOOP;

   -- lookup role description IDs
   v_stage := 'FOR cur_lookup_role_id' || v_stage_suffix;
   FOR rec IN cur_lookup_role_id LOOP
      gv_tab_field_parse(rec.rec_indx).role_id1 := rec.role_id1;
      gv_tab_field_parse(rec.rec_indx).role_id2 := rec.role_id2;
      gv_tab_field_parse(rec.rec_indx).role_id3 := rec.role_id3;
      gv_tab_field_parse(rec.rec_indx).role_id4 := rec.role_id4;
      gv_tab_field_parse(rec.rec_indx).role_id5 := rec.role_id5;
   END LOOP;

   -- lookup country IDs
   v_stage := 'FOR cur_lookup_country_id' || v_stage_suffix;
   FOR rec IN cur_lookup_country_id LOOP
      gv_tab_field_parse(rec.rec_indx).country_id        := rec.country_id;
      gv_tab_field_parse(rec.rec_indx).country_code      := rec.country_code;
   END LOOP;

   -- lookup Language ID
   v_stage := 'FOR cur_language_id_validation' || v_stage_suffix;
   FOR rec IN cur_language_id_validation LOOP
      gv_tab_field_parse(rec.rec_indx).language_id        := rec.cms_code;
   END LOOP;
--     v_stage := 'FOR cur_email_validation' || v_stage_suffix;
--   FOR rec IN cur_email_validation LOOP
--      gv_tab_field_parse(rec.rec_indx).email_address := NVL(rec.email_address,' ');   --Added space to make sure the error is logged in case if the email_address format is wrong.  
--   END LOOP;
   
   v_stage := 'Parsed records' || v_stage_suffix;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ': ' || v_stage, NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ': ' || v_stage, NULL);
      RAISE;
END p_parse_file_records;

---------------------
-- Reports miscellaneous errors
PROCEDURE p_rpt_file_record_err
IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_file_record_err');

   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
BEGIN
   -- validate miscellaneous requirements
   v_stage := 'INSERT import_record_error'
      || ' for import_record_id ' || gv_tab_field_parse(gv_tab_field_parse.FIRST).import_record_id
      || ' to '                   || gv_tab_field_parse(gv_tab_field_parse.LAST).import_record_id;
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
             e.field_name  AS param1,
             e.field_value AS param2,
             NULL          AS param3,
             NULL          AS param4,
             gc_created_by AS created_by,
             gv_timestamp  AS date_created
        FROM ( -- get errant records
               WITH file_rec AS
               (  -- get file records
                  SELECT *
                    FROM TABLE(fnc_pipe_file_records)
               )
               -- SSN incorrect length
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property          AS item_key,
                      'SSN'                                  AS field_name,
                      'Decrypted length: ' ||  fr.ssn_length AS field_value
                 FROM file_rec fr
                WHERE fr.ssn IS NOT NULL
                  AND fr.ssn_length != 9
                UNION ALL
               -- birth date string did not convert to a valid date
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_err_date   AS item_key,
                      'Birth Date'      AS field_name,
                      fr.birth_date_str AS field_value
                 FROM file_rec fr
                WHERE fr.birth_date_str IS NOT NULL
                  AND fr.birth_date     IS NULL
                UNION ALL
               -- hire date string did not convert to a valid date
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_err_date  AS item_key,
                      'Hire Date'      AS field_name,
                      fr.hire_date_str AS field_value
                 FROM file_rec fr
                WHERE fr.hire_date_str IS NOT NULL
                  AND fr.hire_date     IS NULL
                UNION ALL
               -- termination date string did not convert to a valid date
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_err_date         AS item_key,
                      'Termination Date'      AS field_name,
                      fr.termination_date_str AS field_value
                 FROM file_rec fr
                WHERE fr.termination_date_str IS NOT NULL
                  AND fr.termination_date     IS NULL
                UNION ALL
               -- CMS email type name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Email Address type'          AS field_name,
                      fr.email_address_type_name    AS field_value
                 FROM file_rec fr
                WHERE fr.email_address_type_name IS NOT NULL
                  AND fr.email_address_type      IS NULL
                UNION ALL
               -- CMS address type name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Address type'                AS field_name,
                      fr.address_type_name          AS field_value
                 FROM file_rec fr
                WHERE fr.address_type_name IS NOT NULL
                  AND fr.address_type      IS NULL
                UNION ALL
               -- CMS node role name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Primary Org Unit Role'                  AS field_name,
                      fr.node_relationship1_name    AS field_value
                 FROM file_rec fr
                WHERE fr.node_relationship1_name IS NOT NULL
                  AND fr.node_relationship1      IS NULL
                UNION ALL
               -- CMS node role name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Org Unit Role2'                  AS field_name,
                      fr.node_relationship2_name    AS field_value
                 FROM file_rec fr
                WHERE fr.node_relationship2_name IS NOT NULL
                  AND fr.node_relationship2      IS NULL
                UNION ALL
               -- CMS node role name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Org Unit Role3'                  AS field_name,
                      fr.node_relationship3_name    AS field_value
                 FROM file_rec fr
                WHERE fr.node_relationship3_name IS NOT NULL
                  AND fr.node_relationship3      IS NULL
                UNION ALL
               -- CMS node role name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Org Unit Role4'                  AS field_name,
                      fr.node_relationship4_name    AS field_value
                 FROM file_rec fr
                WHERE fr.node_relationship4_name IS NOT NULL
                  AND fr.node_relationship4      IS NULL
                UNION ALL
               -- CMS node role name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Org Unit Role5'                  AS field_name,
                      fr.node_relationship5_name    AS field_value
                 FROM file_rec fr
                WHERE fr.node_relationship5_name IS NOT NULL
                  AND fr.node_relationship5      IS NULL
                UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Characteristic Name1'        AS field_name,
                      fr.characteristic_name1       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name1 IS NOT NULL
                  AND fr.characteristic_id1      IS NULL
                UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name2'        AS field_name,
                      fr.characteristic_name2       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name2 IS NOT NULL
                  AND fr.characteristic_id2      IS NULL
                UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name3'        AS field_name,
                      fr.characteristic_name3       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name3 IS NOT NULL
                  AND fr.characteristic_id3      IS NULL
                UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name4'        AS field_name,
                      fr.characteristic_name4       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name4 IS NOT NULL
                  AND fr.characteristic_id4      IS NULL
                UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name5'        AS field_name,
                      fr.characteristic_name5       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name5 IS NOT NULL
                  AND fr.characteristic_id5      IS NULL
                  UNION ALL
                   SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Characteristic Name6'        AS field_name,
                      fr.characteristic_name6       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name6 IS NOT NULL
                  AND fr.characteristic_id6      IS NULL
                UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name7'        AS field_name,
                      fr.characteristic_name7       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name7 IS NOT NULL
                  AND fr.characteristic_id7      IS NULL
                UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name8'        AS field_name,
                      fr.characteristic_name8       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name8 IS NOT NULL
                  AND fr.characteristic_id8      IS NULL
                UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name9'        AS field_name,
                      fr.characteristic_name9       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name9 IS NOT NULL
                  AND fr.characteristic_id9      IS NULL
                UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name10'        AS field_name,
                      fr.characteristic_name10       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name10 IS NOT NULL
                  AND fr.characteristic_id10      IS NULL
                  UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name11'        AS field_name,
                      fr.characteristic_name11       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name11 IS NOT NULL
                  AND fr.characteristic_id11      IS NULL
                  UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name12'        AS field_name,
                      fr.characteristic_name12       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name12 IS NOT NULL
                  AND fr.characteristic_id12      IS NULL
                  UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name13'        AS field_name,
                      fr.characteristic_name13       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name13 IS NOT NULL
                  AND fr.characteristic_id13      IS NULL
                  UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name14'        AS field_name,
                      fr.characteristic_name14       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name14 IS NOT NULL
                  AND fr.characteristic_id14      IS NULL
                  UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name15'        AS field_name,
                      fr.characteristic_name15       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name15 IS NOT NULL
                  AND fr.characteristic_id15      IS NULL
                UNION ALL                                                   --10/30/2015 Start
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name16'        AS field_name,
                      fr.characteristic_name16       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name16 IS NOT NULL
                  AND fr.characteristic_id16      IS NULL
                  UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name17'        AS field_name,
                      fr.characteristic_name17       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name17 IS NOT NULL
                  AND fr.characteristic_id17      IS NULL
                  UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name18'        AS field_name,
                      fr.characteristic_name18       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name18 IS NOT NULL
                  AND fr.characteristic_id18      IS NULL
                  UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name19'        AS field_name,
                      fr.characteristic_name19       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name19 IS NOT NULL
                  AND fr.characteristic_id19      IS NULL
                  UNION ALL
               -- characteristic name not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_char_not_valid AS item_key,
                      'Characteristic Name20'        AS field_name,
                      fr.characteristic_name20       AS field_value
                 FROM file_rec fr
                WHERE fr.characteristic_name20 IS NOT NULL
                  AND fr.characteristic_id20      IS NULL                   --10/30/2015 End
                UNION ALL
               -- role description not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Role description1'           AS field_name,
                      fr.role_description1          AS field_value
                 FROM file_rec fr
                WHERE fr.role_description1 IS NOT NULL
                  AND fr.role_id1          IS NULL
                UNION ALL
               -- role description not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Role description2'           AS field_name,
                      fr.role_description2          AS field_value
                 FROM file_rec fr
                WHERE fr.role_description2 IS NOT NULL
                  AND fr.role_id2          IS NULL
                UNION ALL
               -- role description not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Role description3'           AS field_name,
                      fr.role_description3          AS field_value
                 FROM file_rec fr
                WHERE fr.role_description3 IS NOT NULL
                  AND fr.role_id3          IS NULL
                UNION ALL
               -- role description not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Role description4'           AS field_name,
                      fr.role_description4          AS field_value
                 FROM file_rec fr
                WHERE fr.role_description4 IS NOT NULL
                  AND fr.role_id4          IS NULL
                UNION ALL
               -- role description not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Role description5'           AS field_name,
                      fr.role_description5          AS field_value
                 FROM file_rec fr
                WHERE fr.role_description5 IS NOT NULL
                  AND fr.role_id5          IS NULL
                UNION ALL
               -- country not found
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Country'                     AS field_name,
                      fr.country                    AS field_value
                 FROM file_rec fr
                WHERE fr.country    IS NOT NULL
                  AND fr.country_id IS NULL             
                UNION ALL
                  -- country inactive  07/23/2013
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      'admin.fileload.errors.COUNTRY_INACTIVE' AS item_key,
                      'Country'                     AS field_name,
                      fr.country                    AS field_value
                 FROM country c,file_rec fr
                WHERE LOWER(fr.country) =  C.country_code  
                  AND c.status = 'inactive'
                UNION ALL
                -- country inactive FOR ABBREVATION 04/11/2016
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      'admin.fileload.errors.COUNTRY_INACTIVE' AS item_key,
                      'Country'                     AS field_name,
                      fr.country                    AS field_value
                 FROM country c,file_rec fr
                WHERE LOWER(fr.country) =  LOWER(c.awardbanq_country_abbrev) 
                  AND c.status = 'inactive'
                UNION ALL
               -- Bug #32555 Fix, International address changes
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_err_not_required AS item_key,
                      'Address4'              AS field_name,
                      NULL                    AS field_value
                 FROM file_rec fr
                WHERE fr.address_4 IS NOT NULL
                UNION ALL
               -- Bug #32555 Fix, International address changes
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_err_not_required AS item_key,
                      'Address5'              AS field_name,
                      NULL                    AS field_value
                 FROM file_rec fr
                WHERE fr.address_5 IS NOT NULL
                UNION ALL
               -- Bug #32555 Fix, International address changes
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_err_not_required AS item_key,
                      'Address6'              AS field_name,
                      NULL                    AS field_value
                 FROM file_rec fr
                WHERE fr.address_6 IS NOT NULL
              UNION ALL
                SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Email Address'        AS field_name,
                      fr.email_address       AS field_value
                 FROM file_rec fr
                WHERE fr.email_address IS NOT NULL
                  --AND NOT REGEXP_LIKE ( fr.email_address, '[a-zA-Z0-9._%-'']+@[a-zA-Z0-9._%-]+\.[a-zA-Z]{2,4}')   --03/03/2015 Added '' before ]           
                  AND NOT REGEXP_LIKE(substr(fr.email_address,0,instr( fr.email_address,'@')-1),'[a-zA-Z0-9._%-'']') --04/13/2015 Bug 60602
                  OR  NOT REGEXP_LIKE(substr(fr.email_address,instr(fr.email_address,'@'),length(fr.email_address)),'@[a-zA-Z0-9._%-]+\.[a-zA-Z]{2,4}')--04/13/2015 Bug 60602
               UNION ALL
                SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Locale'        AS field_name,
                      fr.language_id_str       AS field_value
                 FROM file_rec fr
                WHERE fr.language_id_str IS NOT NULL
                  AND fr.language_id     IS NULL
                  UNION ALL --Defect # 4198
                  SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_sys_err_required AS item_key,
                      'Primary Org Unit Name'              AS field_name,
                      NULL                    AS field_value
                 FROM file_rec fr
                WHERE fr.node_name1 IS NULL
--                UNION ALL --09/23/2013   Defect # 4432  --03/27/2014
--                 -- CMS Suffix name not found
--               SELECT fr.import_file_id,
--                      fr.import_record_id,
--                      gc_admin_err_invalid_property AS item_key,
--                      'Suffix'     AS field_name,
--                      fr.suffix_name    AS field_value
--                 FROM file_rec fr
--                WHERE fr.suffix_name IS NOT NULL
--                  AND fr.suffix IS NULL
/*  -- this check moved to P_RPT_FILE_RECORD_ERR_STG  -- 12/23/2015
                  UNION ALL                  
                    -- Duplicate user_name withinfile Bug#49700 --11/18/2013
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gs_dup_user_error AS item_key,
                      'USER_NAME'     AS field_name,
                      fr.user_name    AS field_value
                 FROM file_rec fr
                 WHERE user_name IN
                 (select user_name
                 FROM file_rec fr
                 GROUP BY user_name HAVING COUNT(*)>1)*/
                 UNION ALL -- 10/14/2014 Bug 57149
                 -- cannot Insert pax with a term_date
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Term Date'     AS field_name,
                      'Cannot ADD pax with term_date'    AS field_value
                 FROM file_rec fr
                WHERE fr.termination_date IS NOT NULL
                  AND NOT EXISTS (SELECT 'x'
                                  FROM   application_user au
                                  WHERE  au.user_name = fr.user_name)
               UNION ALL   --12/24/2014 Bug 57048
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Last Name'     AS field_name,
                      'Quotes not allowed in Last Name'    AS field_value
                 FROM file_rec fr
                WHERE fr.last_name IS NOT NULL
                      AND REGEXP_COUNT(fr.last_name,'"',1) > 0     
               UNION ALL   --12/24/2014 Bug 57048
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'First Name'     AS field_name,
                      'Quotes not allowed in First Name'    AS field_value
                 FROM file_rec fr
                WHERE fr.first_name IS NOT NULL
                      AND REGEXP_COUNT(fr.first_name,'"',1) > 0     
               UNION ALL   --12/24/2014 Bug 57048
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Middle Name'     AS field_name,
                      'Quotes not allowed in Middle Name'    AS field_value
                 FROM file_rec fr
                WHERE fr.middle_name IS NOT NULL
                      AND REGEXP_COUNT(fr.middle_name,'"',1) > 0 
               UNION ALL --03/11/2015 Bug 59953
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Personal Phone Number'      AS field_name,
                      fr.personal_phone_number    AS field_value
                      FROM file_rec fr
               WHERE length(fr.personal_phone_number) > 24
               UNION ALL
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Business Phone Number'      AS field_name,
                      fr.business_phone_number    AS field_value
                      FROM file_rec fr
               WHERE length(fr.business_phone_number) > 24
               UNION ALL
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gc_admin_err_invalid_property AS item_key,
                      'Cell Phone Number'      AS field_name,
                      fr.cell_phone_number    AS field_value
                      FROM file_rec fr
               WHERE length(fr.cell_phone_number) > 24 
               UNION ALL
               SELECT fr.import_file_id,        --11/06/2015
                      fr.import_record_id,
                      gs_city_length_error AS item_key,
                      'City'      AS field_name,
                      fr.city    AS field_value
                      FROM file_rec fr
               WHERE length(fr.city) > 30               
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ': ' || v_stage || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ': ' || v_stage, NULL);
      RAISE;
END p_rpt_file_record_err;

---------------------
-- Inserts the file records into the stage table.
PROCEDURE p_stage_file_record
IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_stage_file_record');

   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
BEGIN
   -- validate miscellaneous requirements
   v_stage := 'INSERT stage_pax_import_record'
      || ' for import_record_id ' || gv_tab_field_parse(gv_tab_field_parse.FIRST).import_record_id
      || ' to '                   || gv_tab_field_parse(gv_tab_field_parse.LAST).import_record_id;
   INSERT INTO stage_pax_import_record
   (  import_record_id,
      import_file_id,
      action_type,
      user_id,
      user_name,
      first_name,
      middle_name,
      last_name,
      suffix,
      ssn,
      birth_date,
      gender,
      active,
      status,
      terms_acceptance,
      user_id_accepted,
      date_terms_accepted,
      email_address,
      email_address_type,
      text_message_address,      
      address_1,
      address_2,
      address_3,
      address_4,
      address_5,
      address_6,
      city,
      state,
      country_id,
      country_code,
      postal_code,
      address_type,
      personal_phone_number,
      business_phone_number,
      cell_phone_number,
      employer_id,
      employer_name,
      job_position,
      department,
      hire_date,
      termination_date,
      node_id1,
      node_id2,
      node_id3,
      node_id4,
      node_id5,
      node_name1,
      node_name2,
      node_name3,
      node_name4,
      node_name5,
      node_relationship1,
      node_relationship2,
      node_relationship3,
      node_relationship4,
      node_relationship5,
      characteristic_id1,
      characteristic_id2,
      characteristic_id3,
      characteristic_id4,
      characteristic_id5,
      characteristic_id6,
      characteristic_id7,
      characteristic_id8,
      characteristic_id9,
      characteristic_id10,
      characteristic_id11,
      characteristic_id12,
      characteristic_id13,
      characteristic_id14,
      characteristic_id15,
      characteristic_id16,  --10/30/2015 Start
      characteristic_id17,
      characteristic_id18,
      characteristic_id19,
      characteristic_id20,  --10/30/2015 End
      characteristic_name1,
      characteristic_name2,
      characteristic_name3,
      characteristic_name4,
      characteristic_name5,
      characteristic_name6,
      characteristic_name7,
      characteristic_name8,
      characteristic_name9,
      characteristic_name10,
      characteristic_name11,
      characteristic_name12,
      characteristic_name13,
      characteristic_name14,
      characteristic_name15,
      characteristic_name16,    --10/30/2015 Start
      characteristic_name17,
      characteristic_name18,
      characteristic_name19,
      characteristic_name20,    --10/30/2015 End
      characteristic_value1,
      characteristic_value2,
      characteristic_value3,
      characteristic_value4,
      characteristic_value5,
      characteristic_value6,
      characteristic_value7,
      characteristic_value8,
      characteristic_value9,
      characteristic_value10,
      characteristic_value11,
      characteristic_value12,
      characteristic_value13,
      characteristic_value14,
      characteristic_value15,
      characteristic_value16,   --10/30/2015 Start
      characteristic_value17,
      characteristic_value18,
      characteristic_value19,
      characteristic_value20,   --10/30/2015 End
      role_id1,
      role_id2,
      role_id3,
      role_id4,
      role_id5,
      role_description1,
      role_description2,
      role_description3,
      role_description4,
      role_description5,     
      language_id,
      sso_id,
      created_by,
      date_created
   )
   (  -- join file records to existing application users
      SELECT fr.import_record_id,
             fr.import_file_id,
             DECODE( au.user_id,
               NULL, 'add',
               'upd'
             ) AS action_type,
             au.user_id,
             fr.user_name,
             fr.first_name,
             fr.middle_name,
             fr.last_name,
             fr.suffix_name, --03/27/2014
             fr.ssn,
             fr.birth_date,
             fr.gender,
             fr.active,
             fr.status,          
             fr.terms_acceptance,
             fr.user_id_accepted,
             fr.date_terms_accepted,
             fr.email_address,
             fr.email_address_type,
             fr.text_message_address,             
             fr.address_1,
             fr.address_2,
             fr.address_3,
             fr.address_4,
             fr.address_5,
             fr.address_6,
             fr.city,
             fr.state,
             fr.country_id,
             fr.country_code,
             fr.postal_code,
             fr.address_type,
             fr.personal_phone_number,
             fr.business_phone_number,
             fr.cell_phone_number,
             fr.employer_id,
             fr.employer_name,
             fr.job_position,
             fr.department,
             fr.hire_date,
             fr.termination_date,
             fr.node_id1,
             fr.node_id2,
             fr.node_id3,
             fr.node_id4,
             fr.node_id5,
             fr.node_name1,
             fr.node_name2,
             fr.node_name3,
             fr.node_name4,
             fr.node_name5,
             fr.node_relationship1,
             fr.node_relationship2,
             fr.node_relationship3,
             fr.node_relationship4,
             fr.node_relationship5,
             fr.characteristic_id1,
             fr.characteristic_id2,
             fr.characteristic_id3,
             fr.characteristic_id4,
             fr.characteristic_id5,
             fr.characteristic_id6,
             fr.characteristic_id7,
             fr.characteristic_id8,
             fr.characteristic_id9,
             fr.characteristic_id10,
             fr.characteristic_id11,
             fr.characteristic_id12,
             fr.characteristic_id13,
             fr.characteristic_id14,
             fr.characteristic_id15,
             fr.characteristic_id16,    --10/30/2015 Start
             fr.characteristic_id17,
             fr.characteristic_id18,
             fr.characteristic_id19,
             fr.characteristic_id20,    --10/30/2015 End
             fr.characteristic_name1,
             fr.characteristic_name2,
             fr.characteristic_name3,
             fr.characteristic_name4,
             fr.characteristic_name5,
             fr.characteristic_name6,
             fr.characteristic_name7,
             fr.characteristic_name8,
             fr.characteristic_name9,
             fr.characteristic_name10,
             fr.characteristic_name11,
             fr.characteristic_name12,
             fr.characteristic_name13,
             fr.characteristic_name14,
             fr.characteristic_name15,
             fr.characteristic_name16,  --10/30/2015 Start
             fr.characteristic_name17,
             fr.characteristic_name18,
             fr.characteristic_name19,
             fr.characteristic_name20,  --10/30/2015 End
             fr.characteristic_value1,
             fr.characteristic_value2,
             fr.characteristic_value3,
             fr.characteristic_value4,
             fr.characteristic_value5,
             fr.characteristic_value6,
             fr.characteristic_value7,
             fr.characteristic_value8,
             fr.characteristic_value9,
             fr.characteristic_value10,
             fr.characteristic_value11,
             fr.characteristic_value12,
             fr.characteristic_value13,
             fr.characteristic_value14,
             fr.characteristic_value15,
             fr.characteristic_value16, --10/30/2015 Start
             fr.characteristic_value17,
             fr.characteristic_value18,
             fr.characteristic_value19,
             fr.characteristic_value20, --10/30/2015 End
             fr.role_id1,
             fr.role_id2,
             fr.role_id3,
             fr.role_id4,
             fr.role_id5,
             fr.role_description1,
             fr.role_description2,
             fr.role_description3,
             fr.role_description4,
             fr.role_description5,
             fr.language_id,
             fr.sso_id,            
             gc_created_by AS created_by,
             gv_timestamp  AS date_created
        FROM TABLE(fnc_pipe_file_records) fr,
             application_user au
          -- outer join
       WHERE UPPER(fr.user_name) = UPPER(au.user_name (+))
   );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ': ' || v_stage || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ': ' || v_stage, NULL);
      RAISE;
END p_stage_file_record;

---------------------
---------------------
-- public functions

---------------------
-- Pipelines the current block of file records
FUNCTION fnc_pipe_file_records
RETURN tab_file_stage PIPELINED
IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('fnc_pipe_file_records');

   v_stage              execution_log.text_line%TYPE;
BEGIN
   -- loop through file records
   FOR indx IN gv_tab_field_parse.FIRST .. gv_tab_field_parse.LAST LOOP
      PIPE ROW(gv_tab_field_parse(indx));
   END LOOP;

   RETURN;
END fnc_pipe_file_records;

---------------------
-- Reports miscellaneous errors
PROCEDURE p_rpt_file_record_err_stg    --Bug#49700 --11/18/2013
(p_import_file_id IN import_file.import_file_id%TYPE)
IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_file_record_err_stg');

   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER :=0;  --04/15/2019 Bug 79028
BEGIN
   -- validate miscellaneous requirements
   v_stage := 'INSERT import_record_error'
      || ' for import_file_id ' || p_import_file_id;
      FOR main_rec IN (SELECT user_name  --04/15/2019  add cursor Bug 79028
									 FROM stage_pax_import_record
									 WHERE import_file_id = p_import_file_id
									 HAVING COUNT(1) > 1
									 GROUP BY user_name) LOOP
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
             E.import_file_id,
             E.import_record_id,
             E.item_key,
             E.field_name  AS param1,
             E.field_value AS param2,
             NULL          AS param3,
             NULL          AS param4,
             gc_created_by AS created_by,
             gv_timestamp  AS date_created
        FROM ( -- get errant records
               WITH file_rec AS
               (  -- get file records
                  SELECT *
                    FROM stage_pax_import_record
                   WHERE import_file_id = p_import_file_id
                   AND   user_name      = main_rec.user_name --04/15/2019 Bug 79028
               )             
                    -- Duplicate user_name withinfile
               SELECT fr.import_file_id,
                      fr.import_record_id,
                      gs_dup_user_error AS item_key,
                      'USER_NAME'     AS field_name,
                      fr.user_name    AS field_value
                 FROM file_rec fr
                 /*  WHERE user_name IN                --04/15/2019 comment out Bug 79028
                 (SELECT user_name
                    FROM file_rec fr
                   GROUP BY user_name HAVING COUNT(*)>1)*/
             ) E
   );

   v_rec_cnt := v_rec_cnt + 1; --04/15/2019  Bug 79028 
   
   END LOOP;   -- FOR main_rec   --04/15/2019  Bug 79028
   --v_rec_cnt := SQL%ROWCOUNT;  --04/15/2019 comment out Bug 79028
   prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_info, c_process_name || ': ' || v_stage || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(gc_pkg_name, gc_release_level, gc_error, c_process_name || ': ' || v_stage, NULL);
      RAISE;
END p_rpt_file_record_err_stg;

---------------------
---------------------
-- public procedures
---------------------
PROCEDURE prc_stage_pax_file
( p_file_name IN VARCHAR2,
  p_out_returncode OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('prc_stage_pax_file');

   v_stage              execution_log.text_line%TYPE;
   v_file_name          VARCHAR2(500);
   v_directory_path     VARCHAR2(4000);
   v_directory_name     all_directories.directory_name%TYPE;
   v_in_file_handler    utl_file.file_type;
   v_import_file_id     stage_pax_import_record.import_file_id%TYPE;


   v_read_max_ctr       INTEGER;
   v_read_rec_count     INTEGER;
   v_end_of_file        BOOLEAN;
   v_recs_in_file_cnt   INTEGER := 0;
   v_error_count        INTEGER := 0;

BEGIN
   v_stage := 'Start'
      || ': file >' || p_file_name
      || '<';
   prc_execution_log_entry(c_process_name, gc_release_level, gc_info, v_stage, NULL);

   -- initialize variables
   gv_timestamp := SYSDATE;
   v_read_max_ctr := 25000;
   p_out_returncode := 0;
   gv_tab_field_parse := tab_file_stage();

   v_stage := 'Call prc_get_directory_file_name (' || p_file_name || ')';
   prc_get_directory_file_name ( p_file_name, v_directory_path, v_file_name );
   v_stage := 'Call fnc_get_dir_name (' || v_directory_path || ')';
   v_directory_name := fnc_get_dir_name(v_directory_path);

   v_stage := 'SELECT NEXT import_file_id';
   SELECT IMPORT_FILE_PK_SQ.NEXTVAL
     INTO v_import_file_id
     FROM dual;

   v_stage := 'Parms'
      || ': v_import_file_id >' || v_import_file_id
      || '<, v_read_max_ctr >'   || v_read_max_ctr
      || '<';
   prc_execution_log_entry(c_process_name, gc_release_level, gc_info, v_stage, NULL);

   -- add parent record
   v_stage := 'INSERT import_file: v_import_file_id >' || v_import_file_id || '<';
   INSERT INTO import_file
   (  import_file_id,
      file_name,
      file_type,
      import_record_count,
      import_record_error_count,
      status,
      staged_by,
      date_staged
   )
   VALUES
   (  v_import_file_id,
      SUBSTR(v_file_name, 1, INSTR(v_file_name, '.') - 1) || '_' || v_import_file_id,
      'par',
      0,
      0,
      'stg_in_process',
      gc_created_by,
      gv_timestamp
   );

   COMMIT;

   -- process file
   v_stage := 'Open file for read: directory >' || v_directory_path || '<, file >' || v_file_name || '<';
   v_in_file_handler := utl_file.fopen(v_directory_name, v_file_name, 'r');

   -- loop through file records until end-of-file reached
   v_end_of_file := FALSE;
   WHILE (NOT v_end_of_file) LOOP
      -- read file records
      v_stage := 'Call p_read_file: v_recs_in_file_cnt >' || v_recs_in_file_cnt || '<';
      p_read_file(v_in_file_handler, v_read_max_ctr, v_end_of_file, v_read_rec_count);

      -- increment file counter
      v_recs_in_file_cnt := v_recs_in_file_cnt + v_read_rec_count;

      -- process records read
      v_stage := 'Processing records >' || v_recs_in_file_cnt || '<';
      IF (v_read_rec_count > 0 ) THEN

         v_stage := 'Call p_parse_file_records: v_recs_in_file_cnt >' || v_recs_in_file_cnt || '<';
         p_parse_file_records(v_import_file_id, v_recs_in_file_cnt);

         v_stage := 'Call p_rpt_file_record_err: v_recs_in_file_cnt >' || v_recs_in_file_cnt || '<';
         p_rpt_file_record_err;

         v_stage := 'Call p_stage_file_record: v_recs_in_file_cnt >' || v_recs_in_file_cnt || '<';
         p_stage_file_record;

      END IF;  -- records read

   END LOOP; -- while not EOF

   utl_file.fclose(v_in_file_handler);
   
   COMMIT; --04/15/2019 Bug 79028
   
   -- 12/23/2015 add call to p_rpt_file_record_err_stg
   v_stage := 'Call p_rpt_file_record_err_stg: v_import_file_id >' || v_import_file_id || '<';
   p_rpt_file_record_err_stg(v_import_file_id);

   -- update load results
   v_stage := 'SELECT error count';
   SELECT COUNT(DISTINCT(import_record_id))
     INTO v_error_count
     FROM import_record_error
    WHERE import_file_id = v_import_file_id;

   v_stage := 'Update import_file table with record counts';
   UPDATE import_file f
      SET version                   = 1,
          status                    = 'stg',
          import_record_count       = v_recs_in_file_cnt,
          import_record_error_count = v_error_count
    WHERE import_file_id = v_import_file_id;

   COMMIT;

   v_stage := 'Success'
      || ': p_file_name >'          || p_file_name
      || '<, v_recs_in_file_cnt >'  || v_recs_in_file_cnt
      || '<, v_error_count >'       || v_error_count
      || '<';
   prc_execution_log_entry(c_process_name, gc_release_level, gc_info, v_stage, NULL);

EXCEPTION
   WHEN OTHERS THEN
      p_out_returncode := 99;
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, 
         'File ' || p_file_name || ' failed at Stage: ' || v_stage 
         || ' --> ' || SQLERRM,
         NULL);

      IF (UTL_FILE.is_open(v_in_file_handler)) THEN
         utl_file.fclose(v_in_file_handler);
      END IF;

      SELECT COUNT(DISTINCT(import_record_id))
        INTO v_error_count
        FROM import_record_error
       WHERE import_file_id = v_import_file_id;

      UPDATE import_file f
         SET version                   = 1,
             status                    = 'stg_fail',
             import_record_count       = v_recs_in_file_cnt,
             import_record_error_count = v_error_count
       WHERE import_file_id = v_import_file_id;

      COMMIT;

END prc_stage_pax_file;

END pkg_bulk_pax_stage;
/