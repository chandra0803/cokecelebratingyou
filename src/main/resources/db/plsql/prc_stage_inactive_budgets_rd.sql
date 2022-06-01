CREATE OR REPLACE PROCEDURE prc_stage_inactive_budgets_rd
  (
   p_file_name               IN     VARCHAR2,
   p_out_returncode          OUT NUMBER
   ) IS

/*-----------------------------------------------------------------------------
       Purpose:  Provide the ability to generate an on-demand BIW Admin extract that will list budgets with no active givers 
       associated with them (inactive budgets) and to be able redistribute the budget

       Person           Date           Comments
       -----------      ----------     -------------------------------------------
       Suresh J         03/02/2016     Bug 65895 - Staging of  file should not error out when staging from a 
                                                  deleted Org Unit Budget to Active Org Unit Budget 
       Ravi Dhanekula  03/02/2016      Made changes to load org_unit_id (new column added).
       Suresh J        03/03/2016      Bug 66030 - Budget Redistribution-Org_unit file is getting failed in verified status
                                       Bug 66039 - Budget Redistribution File Load - File load allows active Org unit 
                                                   budget transfer amount with negative value       
     -----------------------------------------------------------------------------*/
   
    --MISCELLANIOUS
   c_delimiter        CONSTANT VARCHAR2 (10) := '|';
   v_count                     NUMBER := 0;                 --for commit count
   v_stage                     VARCHAR2 (500);
   v_return_code               NUMBER (5);          -- return from insert call
   v_out_error_message         VARCHAR2 (500);
   v_header_rec                BOOLEAN := FALSE;
   v_first_rec                 BOOLEAN := TRUE;
   --UTL_FILE
   v_in_file_handler           UTL_FILE.file_type;

   v_text                      VARCHAR2 (2000);
   v_file_name                 VARCHAR2 (500);
   v_directory_path            VARCHAR2 (4000);
   v_directory_name            VARCHAR2 (30);

   -- RECORD TYPE DECLARATION
   rec_inactive_budget_rd        stage_inactive_budget_rd%ROWTYPE;
   rec_import_record_error     import_record_error%ROWTYPE;

   v_import_record_id               stage_inactive_budget_rd.import_record_id%TYPE;
   v_import_file_id                 stage_inactive_budget_rd.import_file_id%TYPE;
   v_created_by                     stage_inactive_budget_rd.created_by%TYPE
                                    := 0;
   v_timestamp                      stage_inactive_budget_rd.date_created%TYPE
                                    := SYSDATE;
   v_budget_master_name             stage_inactive_budget_rd.budget_master_name%TYPE; 
   v_budget_time_period             stage_inactive_budget_rd.budget_time_period%TYPE; 
   v_budget_master_time_period      stage_inactive_budget_rd.budget_master_time_period%TYPE;    
   v_org_unit_id                    stage_inactive_budget_rd.org_unit_id%TYPE; 
   v_budget_owner_id                stage_inactive_budget_rd.budget_owner_login_id%TYPE; 
   v_budget_owner_name              stage_inactive_budget_rd.budget_owner_name%TYPE; 
   v_budget_amount                  stage_inactive_budget_rd.budget_amount%TYPE;
   v_transfer_to_owner1             stage_inactive_budget_rd.transfer_to_owner1%TYPE; 
   v_amount_owner1                  stage_inactive_budget_rd.amount_owner1%TYPE;
   v_transfer_to_owner2             stage_inactive_budget_rd.transfer_to_owner2%TYPE; 
   v_amount_owner2                  stage_inactive_budget_rd.amount_owner2%TYPE;
   v_transfer_to_owner3             stage_inactive_budget_rd.transfer_to_owner3%TYPE;
   v_amount_owner3                  stage_inactive_budget_rd.amount_owner3%TYPE;
   v_budget_id                      budget.budget_id%TYPE; 
   v_budget_current_value           budget.current_value%TYPE;


   v_valid_budget_amount            NUMBER (18, 4);
   v_valid_amount_owner1            NUMBER (18, 4);
   v_valid_amount_owner2            NUMBER (18, 4);
   v_valid_amount_owner3            NUMBER (18, 4);      

   v_trf_current_value             budget.current_value%TYPE;
   v_trf_original_value            budget.original_value%TYPE;

   v_valid_budget_master_name       stage_inactive_budget_rd.budget_master_name%TYPE;
   v_budget_master_id               BUDGET_MASTER.budget_master_id%TYPE;
   v_budget_type                    BUDGET_MASTER.budget_type%TYPE;
   v_user_id                        budget.user_id%TYPE;
   v_node_id                        budget.node_id%TYPE;   
   v_valid_pax_id                   budget.user_id%TYPE;
   v_valid_node_id                  budget.node_id%TYPE;      
   v_budget_segment_id              budget_segment.budget_segment_id%TYPE; 
   -- EXECUTION LOG VARIABLES
   v_process_name              execution_log.process_name%TYPE
                                  := 'prc_stage_inactive_budgets_rd';
   v_release_level             execution_log.release_level%TYPE := '1';
   v_execution_log_msg         execution_log.text_line%TYPE;

   -- COUNTS
   v_recs_in_file_cnt          INTEGER := 0;
   v_field_cnt                 INTEGER := 0;
   v_recs_loaded_cnt           INTEGER := 0;
   v_error_count               PLS_INTEGER := 0;
   v_error_tbl_count           PLS_INTEGER := 0;
   v_act_amt_length               NUMBER := 0;

   -- EXCEPTIONS
   exit_program_exception      EXCEPTION;
   
   
   
   BEGIN
   
      v_stage := 'Write start to execution_log table';
   p_out_returncode := 0;
   prc_execution_log_entry (v_process_name,
                            v_release_level,
                            'INFO',
                            'Procedure Started for file ' || p_file_name,
                            NULL);
   COMMIT;

   v_stage := 'Get Directory path and File name from p_file_name';
   prc_get_directory_file_name (p_file_name, v_directory_path, v_file_name);

   BEGIN
      v_stage := 'Insert into import_file table';

      SELECT IMPORT_FILE_PK_SQ.NEXTVAL INTO v_import_file_id FROM DUAL;

      INSERT INTO import_file (import_file_id,
                               file_name,
                               file_type,
                               import_record_count,
                               import_record_error_count,
                               status,
                               staged_by,
                               version,
                               date_staged)
              VALUES (
                        v_import_file_id,
                           SUBSTR (v_file_name,
                                   1,
                                   INSTR (v_file_name, '.') - 1)
                        || '_'
                        || v_import_file_id,
                        'bud_dist',
                        0,
                        0,
                        'stg_in_process',
                        v_created_by,
                        0,
                        SYSDATE);

      COMMIT;
      rec_import_record_error.import_file_id := v_import_file_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         prc_execution_log_entry (v_process_name,
                                  v_release_level,
                                  'ERROR',
                                  'Stage: ' || v_stage || ' -- ' || SQLERRM,
                                  NULL);
   END;

   BEGIN
      v_stage := 'Get Directory name';

      SELECT directory_name
        INTO v_directory_name
        FROM all_directories
       WHERE directory_path = v_directory_path AND ROWNUM = 1;
   EXCEPTION
      WHEN OTHERS
      THEN
         v_execution_log_msg := SQLERRM;
         RAISE exit_program_exception;
   END;

   v_stage := 'Open file for read: ' || v_file_name;
   v_in_file_handler := UTL_FILE.fopen (v_directory_name, v_file_name, 'r');

   LOOP
      --*** move records from text file into the table stage_pax_import_record.**
        v_stage := 'Reset variables';
        v_field_cnt := 0;
        v_budget_master_name      := NULL;
        v_budget_time_period      := NULL;
        v_org_unit_id := NULL;
        v_budget_owner_id   := NULL;
        v_budget_owner_name       := NULL;
        v_budget_amount           := NULL;
        v_transfer_to_owner1      := NULL;
        v_amount_owner1           := NULL;
        v_transfer_to_owner2      := NULL;
        v_amount_owner2           := NULL;
        v_transfer_to_owner3      := NULL;
        v_amount_owner3           := NULL;
        v_budget_master_id        := NULL;   
        v_budget_type             := NULL;
      
        v_budget_current_value       := NULL;
        v_valid_budget_amount        := NULL;   
        v_valid_amount_owner1        := NULL; 
        v_valid_amount_owner2        := NULL;
        v_valid_amount_owner3        := NULL;
        v_valid_budget_master_name   := NULL;
        v_user_id                    := NULL;
        v_node_id                    := NULL;
        v_valid_pax_id               := NULL;
        v_valid_node_id              := NULL;
        v_budget_master_id           := NULL;
        v_budget_segment_id          := NULL;
        v_budget_id                  := NULL;    
      
      BEGIN
         v_stage := 'Get record';
         UTL_FILE.get_line (v_in_file_handler, v_text);

         IF v_header_rec
         THEN
            v_header_rec := FALSE;
         ELSE
            SELECT import_record_pk_sq.NEXTVAL
              INTO v_import_record_id
              FROM DUAL;

            rec_import_record_error.import_record_id := v_import_record_id;

            v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
            v_stage := 'Parsing record number: ' || v_recs_in_file_cnt;

            v_field_cnt := v_field_cnt + 1;
            v_budget_master_name :=
               REPLACE (TRIM (fnc_pipe_parse (v_text, 1, c_delimiter)),
                        CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_budget_time_period :=
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 2, c_delimiter)),
                           CHR (13));
             
            v_field_cnt := v_field_cnt + 1;
            v_org_unit_id :=
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 3, c_delimiter)),
                           CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_budget_owner_name :=
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 4, c_delimiter)),
                           CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_budget_owner_id :=
               UPPER (
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 5, c_delimiter)),
                           CHR (13)));

            v_field_cnt := v_field_cnt + 1;
            v_budget_amount :=
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 6, c_delimiter)),
                           CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_transfer_to_owner1 :=
               UPPER (
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 7, c_delimiter)),
                           CHR (13)));

            v_field_cnt := v_field_cnt + 1;
            v_amount_owner1 :=
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 8, c_delimiter)),
                           CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_transfer_to_owner2 :=
               UPPER(
               REPLACE (TRIM (fnc_pipe_parse (v_text, 9, c_delimiter)),
                        CHR (13)));

            v_field_cnt := v_field_cnt + 1;
            v_amount_owner2 :=
               REPLACE (TRIM (fnc_pipe_parse (v_text, 10, c_delimiter)),
                        CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_transfer_to_owner3 :=
               UPPER( 
               REPLACE (TRIM (fnc_pipe_parse (v_text, 11, c_delimiter)),
                        CHR (13)));

            v_field_cnt := v_field_cnt + 1;
            v_amount_owner3 :=
               REPLACE (TRIM (fnc_pipe_parse (v_text, 12, c_delimiter)),
                        CHR (13));

            v_stage := 'Budget Master Validations';
                       
            
            IF v_first_rec
            THEN
               v_first_rec := FALSE;

               -- Budget Master Name is Required
               IF LTRIM (RTRIM (v_budget_master_name)) IS NULL
               THEN
                  v_execution_log_msg := 'Budget Master Name cannot be NULL';
                   rec_import_record_error.item_key :=
                      'admin.fileload.errors.MISSING_PROPERTY';
                   rec_import_record_error.param1 := 'Budget Master Name';
                   rec_import_record_error.created_by := v_created_by;
                   prc_insert_import_record_error (rec_import_record_error);
                  --EXIT;
               END IF;

           END IF;


           -- Budget Master Name should be valid and live
           IF (v_budget_master_name IS NOT NULL)
           THEN
              BEGIN
                 SELECT cms.cms_value,budget_master_id,budget_type
                   INTO v_valid_budget_master_name,v_budget_master_id,v_budget_type
                   FROM budget_master bm,
                        vw_cms_asset_value cms
                  WHERE cms.cms_value = v_budget_master_name AND
                        cms.asset_code = bm.cm_asset_code AND
                        cms.key = 'BUDGET_NAME' AND
                        cms.locale = 'en_US';
              EXCEPTION
                 WHEN NO_DATA_FOUND
                 THEN
                      v_execution_log_msg := 'Budget Master Name should be Valid';
                       rec_import_record_error.item_key :=
                          'admin.fileload.errors.MISSING_PROPERTY';
                       rec_import_record_error.param1 := 'Budget Master Name:'||v_budget_master_name;
                       rec_import_record_error.created_by := v_created_by;
                       prc_insert_import_record_error (rec_import_record_error);
                       --v_budget_master_name := NULL;
                       v_budget_type := NULL;
                    --EXIT;
              END;
           END IF;

            IF LTRIM (RTRIM (v_budget_time_period)) IS NULL
            THEN
               rec_import_record_error.item_key :=
                  'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'Budget Time Period';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
            END IF;


           IF v_budget_time_period IS NOT NULL
           THEN
              BEGIN
               
                 SELECT bs.budget_segment_id 
                        INTO v_budget_segment_id 
                 FROM budget_segment bs,
                      budget_master bm
                 WHERE bs.budget_master_id = bm.budget_master_id and
                       bm.budget_master_id = v_budget_master_id and
                       TRIM(to_char(bs.start_date,'mm/dd/yyyy')||' - '||to_char(bs.end_date,'mm/dd/yyyy')) = v_budget_time_period;      
                       
              EXCEPTION
                 WHEN OTHERS
                 THEN                     
                    rec_import_record_error.item_key :=
                       'system.errors.DATE';
                    rec_import_record_error.param1 := v_budget_time_period;
                    rec_import_record_error.param2 :=
                       SUBSTR (SQLERRM, 1, 250);
                    rec_import_record_error.created_by := v_created_by;
                    prc_insert_import_record_error (
                       rec_import_record_error);
                    --v_budget_time_period := NULL;
              END;
           END IF;

           
           rec_inactive_budget_rd.budget_master_id            := v_budget_master_id; 
           rec_inactive_budget_rd.budget_type                 := v_budget_type;
           rec_inactive_budget_rd.budget_segment_id           := v_budget_segment_id;
 
          IF v_budget_type = 'pax' THEN   
 
            -- Budget Owner Login ID is Required
            IF LTRIM (RTRIM (v_budget_owner_id)) IS NULL
            THEN
               rec_import_record_error.item_key :=
                  'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'User Name';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
            END IF;

            -- Login id for participant (not non-pax) is valid and participant is active status.
            IF (v_budget_owner_id IS NOT NULL)
            THEN
               BEGIN
                  SELECT user_id
                    INTO v_user_id
                    FROM application_user
                   WHERE UPPER (user_name) = UPPER (v_budget_owner_id);
                   
                   
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'User Name';
                     rec_import_record_error.param2 := v_budget_owner_id;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_user_id := NULL;
               END;                                   --  check if user exists
            END IF;



            IF (v_user_id IS NOT NULL)
            THEN
               BEGIN
                  SELECT au.user_id
                    INTO v_valid_pax_id
                    FROM application_user au
                   WHERE     au.user_id = v_user_id
                         AND EXISTS
                                (SELECT b.user_id
                                   FROM budget b
                                  WHERE     b.budget_segment_id = v_budget_segment_id
                                        AND b.user_id = au.user_id);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'User ID';
                     rec_import_record_error.param2 := v_user_id;

                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END;                                --  check if user is Active
            END IF;
          
            rec_inactive_budget_rd.user_id            := v_valid_pax_id;
           
           ---------------------------------------------------------------------------------------------------------------------------------
            v_user_id := NULL; 
           --Validating v_transfer_to_owner1 column
            IF LTRIM (RTRIM (v_transfer_to_owner1)) IS NULL
            THEN
               rec_import_record_error.item_key :=
                  'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'Transfer To User Name1';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
            END IF;

          ELSIF   v_budget_type = 'node' THEN
          
                      -- Budget Owner Name is Required
            IF LTRIM (RTRIM (v_budget_owner_name)) IS NULL
            THEN
               rec_import_record_error.item_key :=
                  'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'Node Name';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
            END IF;

            -- Node is active status.
            IF (v_budget_owner_name IS NOT NULL)
            THEN
               BEGIN

                  SELECT node_id
                    INTO v_node_id
                    FROM node
                   WHERE node_id = v_org_unit_id;--03/02/2016
--                    (
--                    SELECT node_id,name as node_name
--                        FROM node
--                       WHERE node_id = v_org_unit_id
--                    UNION ALL     --03/02/2016
--                    --Deleted node name should be truncated to remove the timestamp      --03/02/2016 
--                    --since the incoming file contains only truncated name for deleted nodes
--                    SELECT node_id,SUBSTR(n.name,1,INSTR(n.name,'-',-1,3)-1) as node_name     --03/02/2016
--                        FROM node n
--                       WHERE UPPER (SUBSTR(n.name,1,INSTR(n.name,'-',-1,3)-1)) = UPPER (v_budget_owner_name) AND
--                             n.is_deleted = 1
--                             ) nv
--                   WHERE UPPER (nv.node_name) = UPPER (v_budget_owner_name);

               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Node Name';
                     rec_import_record_error.param2 :=  v_budget_owner_name;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_user_id := NULL;
               END;                                   --  check if user exists
            END IF;



            IF (v_node_id IS NOT NULL)
            THEN
               BEGIN
                  SELECT n.node_id
                    INTO v_valid_node_id
                    FROM node n
                   WHERE     n.node_id = v_node_id
                         AND EXISTS
                                (SELECT b.node_id
                                   FROM budget b
                                  WHERE     b.budget_segment_id = v_budget_segment_id
                                        AND b.node_id = n.node_id);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Node ID ';
                     rec_import_record_error.param2 := v_node_id;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_node_id := NULL;
               END;                                --  check if Node is Active
            END IF;

            rec_inactive_budget_rd.node_id            := v_valid_node_id;
           ---------------------------------------------------------------------------------------------------------------------------------
           --Validating v_transfer_to_owner1 column
            IF LTRIM (RTRIM (v_transfer_to_owner1)) IS NULL
            THEN
               rec_import_record_error.item_key :=
                  'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'Transfer To Node Name1';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
            END IF;

          END IF;  --IF v_header_rec
          
          END IF; --IF v_budget_type = 'pax' THEN  
            

           IF LTRIM (RTRIM (v_budget_amount)) IS NULL
           THEN
               rec_import_record_error.item_key :=
                  'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'Budget Amount';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
           END IF;


           IF v_budget_amount IS NOT NULL  THEN
           
               BEGIN    
               v_valid_budget_amount := TO_NUMBER(LTRIM (RTRIM (v_budget_amount)));

                IF v_valid_budget_amount <= 0 THEN     --03/03/2016
                      rec_import_record_error.item_key :=
                         'admin.fileload.errors.INVALID_PROPERTY';
                      rec_import_record_error.param1 := 'Budget Amount';
                      rec_import_record_error.param2 := v_valid_budget_amount;                      
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_budget_amount := NULL;
                      v_budget_id := NULL;     
                END IF;

               SELECT b.budget_id
                 INTO v_budget_id   
                    FROM budget b 
                     WHERE b.budget_segment_id = v_budget_segment_id AND
                           ((v_budget_type = 'pax' AND b.user_id = v_valid_pax_id) OR 
                             (v_budget_type = 'node' AND b.node_id = v_node_id));  
               EXCEPTION
                   WHEN VALUE_ERROR
                   THEN
                      rec_import_record_error.item_key :=
                         'user.characteristic.errors.INTEGER';
                      rec_import_record_error.param1 := 'Budget Amount';
                      rec_import_record_error.param2 := v_budget_amount;                      
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_budget_amount := NULL;
                      v_budget_id := NULL;
                      
                   WHEN OTHERS
                   THEN
                      rec_import_record_error.item_key :=
                         'admin.fileload.errors.INVALID_PROPERTY';
                      rec_import_record_error.param1 := 'Budget Amount';
                      rec_import_record_error.param2 := v_budget_amount;                      
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_budget_amount := NULL;
                      v_budget_id := NULL;     
                END;
                       rec_inactive_budget_rd.budget_id            := v_budget_id;
                   
           END IF; 
            

           IF v_amount_owner1 IS NOT NULL  THEN
           
               BEGIN    
               v_valid_amount_owner1 := TO_NUMBER(LTRIM (RTRIM (v_amount_owner1)));


                IF v_valid_amount_owner1 <= 0 THEN     --03/03/2016
                      rec_import_record_error.item_key :=
                         'admin.fileload.errors.INVALID_PROPERTY';
                      rec_import_record_error.param1 := 'Amount Owner 1';
                      rec_import_record_error.param2 := v_valid_amount_owner1;                      
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_amount_owner1 := NULL;
                END IF;


               EXCEPTION
                   WHEN VALUE_ERROR
                   THEN
                      rec_import_record_error.item_key :=
                         'user.characteristic.errors.INTEGER';
                      rec_import_record_error.param1 := 'Amount Owner 1';
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_amount_owner1 := NULL;
                END;
                   
           END IF; 


           IF v_amount_owner2 IS NOT NULL  THEN
           
               BEGIN    
               v_valid_amount_owner2 := TO_NUMBER(LTRIM (RTRIM (v_amount_owner2)));

                IF v_valid_amount_owner2 <= 0 THEN     --03/03/2016
                      rec_import_record_error.item_key :=
                         'admin.fileload.errors.INVALID_PROPERTY';
                      rec_import_record_error.param1 := 'Amount Owner 2';
                      rec_import_record_error.param2 := v_valid_amount_owner2;                      
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_amount_owner2 := NULL;
                END IF;


               EXCEPTION
                   WHEN VALUE_ERROR
                   THEN
                      rec_import_record_error.item_key :=
                         'user.characteristic.errors.INTEGER';
                      rec_import_record_error.param1 := 'Amount Owner 2';
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_amount_owner2 := NULL;
                END;
                   
           END IF; 

           IF v_amount_owner3 IS NOT NULL  THEN
           
               BEGIN    
               v_valid_amount_owner3 := TO_NUMBER(LTRIM (RTRIM (v_amount_owner3)));


                IF v_valid_amount_owner3 <= 0 THEN     --03/03/2016
                      rec_import_record_error.item_key :=
                         'admin.fileload.errors.INVALID_PROPERTY';
                      rec_import_record_error.param1 := 'Amount Owner 3';
                      rec_import_record_error.param2 := v_valid_amount_owner3;                      
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_amount_owner3 := NULL;
                END IF;


               EXCEPTION
                   WHEN VALUE_ERROR
                   THEN
                      rec_import_record_error.item_key :=
                         'user.characteristic.errors.INTEGER';
                      rec_import_record_error.param1 := 'Amount Owner 3';
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_amount_owner3 := NULL;
                END;
                   
           END IF; 

            -- Initialize the import record error values
            rec_import_record_error.item_key := NULL;
            rec_import_record_error.param1 := NULL;
            rec_import_record_error.param2 := NULL;

            v_stage := 'Initialize the record type';
            rec_inactive_budget_rd.import_record_id         := v_import_record_id;
            rec_inactive_budget_rd.import_file_id           := v_import_file_id;
            rec_inactive_budget_rd.budget_master_name       := v_budget_master_name;
            rec_inactive_budget_rd.budget_time_period       := v_budget_time_period;
            rec_inactive_budget_rd.org_unit_id              := v_org_unit_id;             --03/03/2016
            rec_inactive_budget_rd.budget_owner_name        := v_budget_owner_name;
            rec_inactive_budget_rd.budget_owner_login_id    := v_budget_owner_id;
            rec_inactive_budget_rd.budget_amount            := v_budget_amount;
            rec_inactive_budget_rd.transfer_to_owner1       := v_transfer_to_owner1;
            rec_inactive_budget_rd.amount_owner1            := v_amount_owner1;
            rec_inactive_budget_rd.transfer_to_owner2       := v_transfer_to_owner2;
            rec_inactive_budget_rd.amount_owner2            := v_amount_owner2;
            rec_inactive_budget_rd.transfer_to_owner3       := v_transfer_to_owner3;
            rec_inactive_budget_rd.amount_owner3            := v_amount_owner3;
            rec_inactive_budget_rd.date_modified := NULL;
            rec_inactive_budget_rd.modified_by := NULL;
            rec_inactive_budget_rd.created_by := v_created_by;
            rec_inactive_budget_rd.date_created := SYSDATE;
            rec_inactive_budget_rd.version := 1;
 
            --Insert into stage_inactive_budget_rd            
            IF v_budget_amount IS NOT NULL THEN                          
            v_stage := 'Insert into stage_inactive_budget_rd table';
            prc_stg_inactive_budget_rd_ins (rec_inactive_budget_rd,
                                          v_return_code);
            IF v_return_code = 99
            THEN
               v_error_count := v_error_count + 1; -- need to count any insert errors
            ELSE
               v_count := v_count + 1;
               v_recs_loaded_cnt := v_recs_loaded_cnt + 1;
            END IF;
            
            END IF;

            IF MOD (v_count, 10) = 0
            THEN
               COMMIT;
            END IF;

      EXCEPTION
         WHEN NO_DATA_FOUND
         THEN
            -- end of file
            --Program continues on to compare rec cnt.  Then final
            -- file_load_audit entry is done.
            UTL_FILE.fclose (v_in_file_handler);
            EXIT;
         WHEN OTHERS
         THEN
            IF v_stage LIKE 'Parsing%'
            THEN
               v_stage := v_stage || ' field number: ' || v_field_cnt;

               rec_import_record_error.item_key :=
                  'admin.fileload.errors.PARSING';
               rec_import_record_error.param1 := v_recs_in_file_cnt;
               rec_import_record_error.param2 := v_field_cnt;
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
            ELSE
               prc_execution_log_entry (
                  v_process_name,
                  v_release_level,
                  'ERROR',
                  'Stage: ' || v_stage || ' --> ' || SQLERRM,
                  NULL);
            END IF;

            COMMIT;

            --v_error_count := v_error_count + 1; -- need to count any parsing errors 
      END;
   END LOOP; -- *** end of loop to move data to stage_inactive_budget_rd table. ***
   
    v_stage := 'Count distinct records that have an error';

    SELECT COUNT (DISTINCT (import_record_id))
    INTO v_error_tbl_count
    FROM import_record_error
    WHERE import_file_id = v_import_file_id;

    v_stage := 'Total error count';
    v_error_count := v_error_count + v_error_tbl_count;

       IF v_error_tbl_count > 0 THEN
           UPDATE import_file
           SET status = 'stg_fail',
           import_record_count = v_recs_loaded_cnt,
           import_record_error_count = v_error_count
           WHERE import_file_id = v_import_file_id;

           COMMIT;

       ELSIF v_error_tbl_count = 0  THEN
       
           UPDATE import_file
           SET status = 'stg',
           import_record_count = v_recs_loaded_cnt,
           import_record_error_count = v_error_count
           WHERE import_file_id = v_import_file_id;

           COMMIT;
       END IF;
             
      EXCEPTION
     WHEN OTHERS
     THEN
      p_out_returncode := 99;
      v_stage := 'Count distinct records that have an error';

       SELECT COUNT (DISTINCT (import_record_id))
       INTO v_error_tbl_count
       FROM import_record_error
       WHERE import_file_id = v_import_file_id;

       v_stage := 'Total error count';
       v_error_count := v_error_count + v_error_tbl_count;
         
       UPDATE import_file
       SET status = 'stg_fail',
       import_record_count = v_recs_loaded_cnt,
       import_record_error_count = v_error_count
       WHERE import_file_id = v_import_file_id;
         
               prc_execution_log_entry (
                  v_process_name,
                  v_release_level,
                  'ERROR',
                  'Stage: ' || v_stage || ' --> ' || SQLERRM,
                  NULL);
  
   END;
/
