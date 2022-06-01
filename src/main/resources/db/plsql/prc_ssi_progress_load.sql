CREATE OR REPLACE PROCEDURE prc_ssi_progress_load (
   p_file_name            IN     VARCHAR2,
   p_out_ssi_contest_id      OUT NUMBER,
   p_out_import_file_id      OUT NUMBER,
   p_file_records_count      OUT NUMBER,
   p_processed_records_count OUT NUMBER,
   p_out_returncode          OUT NUMBER)
IS
   /*******************************************************************************
   -- Purpose: To stage and load list of users and their SSI progress scores

   --
   -- Person        Date       Comments
   -- -----------   --------   -----------------------------------------------------
   -- Suresh J      02/06/2015  Initial Creation.
   --Ravi Dhanekula 02/12/2015  Added IF loop to insert data to stage table only in case of progress data NOT NULL.
   --Suresh J       02/24/2015  Not to check digits after decimal if progress amount is a whole number  
   --Ravi Dhanekula 03/04/2015  Added extra out parameters with total records and processed records counts.
   --Suresh J       03/25/2015  Bug Fix 60776 Spreadsheet upload doesn't reflect in the DB when no pax exist in progress table for a contest ID  
   --Ravi Dhanekula 03/26/2015  Bug # 60805 Progress load (from excel upload) is not loading correctly into ssi_contest_progress table.    
   --Ravi Dhanekula 05/05/2015  Bug # 61749 - Creator Notification-Step it up-Contest Progress Update is complete notification-The progress updated for number of participants is incorrect
   --Swati          05/13/2015  Bug 61657 - Creator view-DTGT-Update Results-Upload Spreadsheet-Able to upload activity value as '10000000000' 
   --Swati          05/20/2015  Bug # 61749 - Creator Notification-Step it up-Contest Progress Update is complete notification-The progress updated for number of participants is incorrect
   --nagarajs       06/20/2016  Bug 66021 - Global File Load defined for SSI 
   --Suresh J       07/01/2016  Bug 67403 - SSI Progress Load Process Loads Null values
   --Sherif Basha   09/16/2016  Bug 66021,Bug 67161  Label SSIProgress is changed as ssiprog
   --Suresh J       11/30/2016  Bug 70286 - SSI File is getting loaded without Progress Values and blank page is displayed on Record details page for valid records
   --Chidamba       01/16/2018  G6-3651 -- reset upload_in_progress column value to '0' at the end of process completion.
   --Gorantla       02/21/2019  GitLab 3868/Bug 76936 - As of date is different than the activity date on objective contest progress mail
   *******************************************************************************/

   --MISCELLANIOUS
   c_delimiter        CONSTANT VARCHAR2 (10) := '|';
   v_count                     NUMBER := 0;                 --for commit count
   v_stage                     VARCHAR2 (500);
   v_return_code               NUMBER (5);          -- return from insert call
   v_out_error_message         VARCHAR2 (500);
   v_header_rec                BOOLEAN := FALSE;
   v_first_rec                 BOOLEAN := TRUE;
   v_ssi_contest_exist         BOOLEAN := TRUE;
   --UTL_FILE
   v_in_file_handler           UTL_FILE.file_type;

   v_text                      VARCHAR2 (2000);
   v_file_name                 VARCHAR2 (500);
   v_directory_path            VARCHAR2 (4000);
   v_directory_name            VARCHAR2 (30);

   -- RECORD TYPE DECLARATION
   rec_ssi_pax_progress        stage_ssi_pax_progress_load%ROWTYPE;
   rec_import_record_error     import_record_error%ROWTYPE;

   v_import_record_id          stage_ssi_pax_progress_load.import_record_id%TYPE;
   v_import_file_id            stage_ssi_pax_progress_load.import_file_id%TYPE;
   v_user_name                 stage_ssi_pax_progress_load.user_name%TYPE;
   v_ssi_contest_id            stage_ssi_pax_progress_load.ssi_contest_id%TYPE;
   v_valid_ssi_contest_id      stage_ssi_pax_progress_load.ssi_contest_id%TYPE;
   v_contest_type              ssi_contest.contest_type%TYPE;
   v_str_activity_date         VARCHAR2 (10);
   v_user_id                   application_user.user_id%TYPE;
   v_valid_pax_id              ssi_contest_participant.user_id%TYPE;
   v_activity_date             stage_ssi_pax_progress_load.activity_date%TYPE;
   v_first_name                stage_ssi_pax_progress_load.first_name%TYPE;
   v_last_name                 stage_ssi_pax_progress_load.last_name%TYPE;
   v_node_name                 stage_ssi_pax_progress_load.node_name%TYPE;
   v_email_address             stage_ssi_pax_progress_load.email_address%TYPE;
   v_activity_description      stage_ssi_pax_progress_load.activity_description%TYPE;
   v_ssi_contest_activity_id   ssi_contest_activity.ssi_contest_activity_id%TYPE;
   v_progress_total            NUMBER (18, 4);
   v_valid_progress_total      NUMBER (18, 4);
   v_created_by                stage_ssi_pax_progress_load.created_by%TYPE
                                  := 0;
   v_timestamp                 stage_ssi_pax_progress_load.date_created%TYPE
                                  := SYSDATE;
   v_activity_measure_type     ssi_contest.activity_measure_type%TYPE;

   -- EXECUTION LOG VARIABLES
   v_process_name              execution_log.process_name%TYPE
                                  := 'prc_ssi_progress_load';
   v_release_level             execution_log.release_level%TYPE := '1';
   v_execution_log_msg         execution_log.text_line%TYPE;

   -- COUNTS
   v_recs_in_file_cnt          INTEGER := 0;
   v_field_cnt                 INTEGER := 0;
   v_recs_loaded_cnt           INTEGER := 0;
   v_error_count               PLS_INTEGER := 0;
   v_error_tbl_count           PLS_INTEGER := 0;
   v_act_amt_length			   NUMBER := 0;

   -- EXCEPTIONS
   exit_program_exception      EXCEPTION;

   PROCEDURE prc_stg_ssi_pax_progress_ins (
      p_import_record         stage_ssi_pax_progress_load%ROWTYPE,
      p_out_return_code   OUT NUMBER)
   IS
      -- EXECUTION LOG VARIABLES
      v_process_name    execution_log.process_name%TYPE
                           := 'prc_stg_ssi_pax_progress_ins';
      v_release_level   execution_log.release_level%TYPE := '1';
   BEGIN
      INSERT INTO stage_ssi_pax_progress_load (import_record_id,
                                               import_file_id,
                                               user_name,
                                               user_id,
                                               first_name,
                                               last_name,
                                               node_name,
                                               progress,
                                               email_address,
                                               activity_description,
                                               ssi_contest_id,
                                               activity_date,
                                               ssi_contest_activity_id,
                                               date_created,
                                               created_by,
                                               date_modified,
                                               modified_by,
                                               version)
           VALUES (p_import_record.import_record_id,
                   p_import_record.import_file_id,
                   p_import_record.user_name,
                   p_import_record.user_id,
                   p_import_record.first_name,
                   p_import_record.last_name,
                   p_import_record.node_name,
                   p_import_record.progress,
                   p_import_record.email_address,
                   p_import_record.activity_description,
                   p_import_record.ssi_contest_id,
                   p_import_record.activity_date,
                   p_import_record.ssi_contest_activity_id,
                   p_import_record.date_created,
                   p_import_record.created_by,
                   p_import_record.date_modified,
                   p_import_record.modified_by,
                   p_import_record.version);

      p_out_return_code := 0;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := 99;
         prc_execution_log_entry (
            v_process_name,
            v_release_level,
            'ERROR',
               'Import_file_id: '
            || p_import_record.import_file_id
            || 'Import_record_id: '
            || p_import_record.import_record_id
            || ' --> '
            || SQLERRM,
            NULL);
         COMMIT;
   END prc_stg_ssi_pax_progress_ins;
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
                               date_staged)
              VALUES (
                        v_import_file_id,
                           SUBSTR (v_file_name,
                                   1,
                                   INSTR (v_file_name, '.') - 1)
                        || '_'
                        || v_import_file_id,
                        'ssiprog',      -- 09/16/2016
                        0,
                        0,
                        'stg_in_process',
                        v_created_by,
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
      v_user_name := NULL;
      v_progress_total := NULL;
      v_activity_description := NULL;

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
            v_ssi_contest_id :=
               REPLACE (TRIM (fnc_pipe_parse (v_text, 1, c_delimiter)),
                        CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_user_name :=
               UPPER (
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 2, c_delimiter)),
                           CHR (13)));

            v_field_cnt := v_field_cnt + 1;
            v_first_name :=
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 3, c_delimiter)),      --07/01/2016
                           CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_last_name :=
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 4, c_delimiter)),      --07/01/2016
                           CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_node_name :=                                                  
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 5, c_delimiter)),      --07/01/2016
                           CHR (13));   

            v_field_cnt := v_field_cnt + 1;
            v_email_address :=
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 6, c_delimiter)),      --07/01/2016
                           CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_activity_description :=                                           
                  REPLACE (TRIM (fnc_pipe_parse (v_text, 7, c_delimiter)),      --07/01/2016
                           CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_progress_total :=
               REPLACE (TRIM (fnc_pipe_parse (v_text, 8, c_delimiter)),
                        CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_str_activity_date :=
               REPLACE (TRIM (fnc_pipe_parse (v_text, 9, c_delimiter)),
                        CHR (13));

            v_stage := 'Participant Validations';
                       
            --Contest ID and Activity Date will be same for all rows
            IF v_first_rec
            THEN
               v_first_rec := FALSE;

               -- SSI Contest ID is Required
               IF LTRIM (RTRIM (v_ssi_contest_id)) IS NULL
               THEN
                  v_execution_log_msg := 'SSI Contest ID cannot be NULL';
                  v_ssi_contest_exist := FALSE;
                  EXIT;
               END IF;

               -- SSI Contest ID should be valid and live
               IF (v_ssi_contest_id IS NOT NULL)
               THEN
                  BEGIN
                     SELECT v_ssi_contest_id, activity_measure_type, contest_type
                       INTO v_valid_ssi_contest_id, v_activity_measure_type, v_contest_type
                       FROM ssi_contest
                      WHERE     ssi_contest_id = v_ssi_contest_id
                            AND status = 'live';
                  EXCEPTION
                     WHEN NO_DATA_FOUND
                     THEN
                        v_execution_log_msg :=
                           'SSI Contest ID should be Valid and Live';
                        v_ssi_contest_exist := FALSE;
                        EXIT;
                  END;
               END IF;
           END IF;
               p_out_ssi_contest_id := v_valid_ssi_contest_id;

               -- for populated column activity_date
               IF v_str_activity_date IS NOT NULL
               THEN
                  BEGIN
                     v_activity_date :=
                        TO_DATE (v_str_activity_date, 'MM/DD/YYYY');
                       
                  EXCEPTION
                     WHEN OTHERS
                     THEN                     
                        rec_import_record_error.item_key :=
                           'system.errors.DATE';
                        rec_import_record_error.param1 := v_str_activity_date;
                        rec_import_record_error.param2 :=
                           SUBSTR (SQLERRM, 1, 250);
                        rec_import_record_error.created_by := v_created_by;
                        prc_insert_import_record_error (
                           rec_import_record_error);
                        v_activity_date := NULL;
                  END;
               END IF;
 


            -- User_name and Score are Required
            IF LTRIM (RTRIM (v_user_name)) IS NULL
            THEN
               rec_import_record_error.item_key :=
                  'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'User Name';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
            END IF;

            IF LTRIM (RTRIM (v_str_activity_date)) IS NULL
            THEN
               rec_import_record_error.item_key :=
                  'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'Activity Date';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
            END IF;

            -- Login id for participant (not non-pax) is valid and participant is active status.
            IF (v_user_name IS NOT NULL)
            THEN
               BEGIN
                  SELECT user_id
                    INTO v_user_id
                    FROM application_user
                   WHERE UPPER (user_name) = UPPER (v_user_name);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 :=
                        'User Name - ' || v_user_name;
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
                         AND is_active = 1
                         AND EXISTS
                                (SELECT scp.user_id
                                   FROM ssi_contest_participant scp
                                  WHERE     scp.ssi_contest_id =
                                               v_valid_ssi_contest_id
                                        AND scp.user_id = au.user_id);
               EXCEPTION
                  WHEN NO_DATA_FOUND
                  THEN
                     rec_import_record_error.item_key :=
                        'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 :=
                        'User ID - ' || v_user_id;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                     v_valid_pax_id := NULL;
               END;                                --  check if user is Active
            END IF;
                IF v_progress_total IS NOT NULL THEN --We don't need to review the records without any activity.
                -- Score is numeric
                BEGIN
                   v_valid_progress_total := TO_NUMBER (v_progress_total);
                   --05/13/2015 Bug 61657
    			   
                   SELECT DECODE(INSTR(TO_CHAR(v_valid_progress_total),'.'),0,LENGTH(TO_CHAR(v_valid_progress_total)),
                                INSTR(TO_CHAR(v_valid_progress_total),'.')-1) INTO v_act_amt_length
                   FROM DUAL;
    			   
                   IF  v_act_amt_length > 9
                   THEN
                     rec_import_record_error.item_key :=
                         'admin.fileload.errors.INVALID_PROPERTY';
                      rec_import_record_error.param1 := 'Progress Total';
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_progress_total := NULL;
                   END IF;
    			   
                   IF     v_activity_measure_type IS NOT NULL
                      AND v_activity_measure_type = 'currency'
                      AND INSTR (TO_CHAR (v_valid_progress_total), '.') > 0   --02/24/2015
                      AND REGEXP_COUNT (
                             TO_CHAR (v_valid_progress_total),
                             '[0-9]',
                             INSTR (TO_CHAR (v_valid_progress_total), '.') + 1) >
                             2				  
                   THEN
                      rec_import_record_error.item_key :=
                         'admin.fileload.errors.INVALID_PROPERTY';
                      rec_import_record_error.param1 := 'Progress Total';
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_progress_total := NULL;
                   ELSIF     v_activity_measure_type IS NOT NULL
                         AND v_activity_measure_type = 'units'
                         AND INSTR (TO_CHAR (v_valid_progress_total), '.') > 0    --02/24/2015
                         AND REGEXP_COUNT (
                                TO_CHAR (v_valid_progress_total),
                                '[0-9]',
                                INSTR (TO_CHAR (v_valid_progress_total), '.') + 1) >
                                4					 
                   THEN
                      rec_import_record_error.item_key :=
                         'admin.fileload.errors.INVALID_PROPERTY';
                      rec_import_record_error.param1 := 'Progress Total';
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_progress_total := NULL;
                   END IF;
                EXCEPTION
                   WHEN VALUE_ERROR
                   THEN
                      rec_import_record_error.item_key :=
                         'user.characteristic.errors.INTEGER';
                      rec_import_record_error.param1 := 'Progress Total';
                      rec_import_record_error.created_by := v_created_by;
                      prc_insert_import_record_error (rec_import_record_error);
                      v_valid_progress_total := NULL;
                END;

                    -- Activity Description validation check.
                    IF (v_activity_description IS NOT NULL AND v_contest_type =2 )--Do this check only if it is a DTGT contest
                    THEN
                       BEGIN
                          SELECT ssi_contest_activity_id
                            INTO v_ssi_contest_activity_id
                            FROM ssi_contest_activity
                           WHERE ssi_contest_id = v_ssi_contest_id-- Bug # 60825
                                 AND UPPER (description) =
                                    UPPER (TRIM (v_activity_description));
                       EXCEPTION
                          WHEN NO_DATA_FOUND
                          THEN
                             rec_import_record_error.item_key :=
                                'admin.fileload.errors.ACTIVITY_DESCRIPTION_NOT_VALID';
                             rec_import_record_error.param1 :=
                                'Activity Description - ' || v_activity_description;
                             rec_import_record_error.created_by := v_created_by;
                             prc_insert_import_record_error (rec_import_record_error);
                             v_ssi_contest_activity_id := NULL;
                       END;
                    END IF;

                -- Initialize the import record error values
                rec_import_record_error.item_key := NULL;
                rec_import_record_error.param1 := NULL;
                rec_import_record_error.param2 := NULL;

                v_stage := 'Initialize the record type';
                rec_ssi_pax_progress.import_record_id       := v_import_record_id;
                rec_ssi_pax_progress.import_file_id         := v_import_file_id;
                rec_ssi_pax_progress.user_name              := v_user_name;
                rec_ssi_pax_progress.user_id                := v_valid_pax_id;
                rec_ssi_pax_progress.progress               := v_valid_progress_total;
                rec_ssi_pax_progress.activity_description   := v_activity_description;
                rec_ssi_pax_progress.ssi_contest_activity_id:= v_ssi_contest_activity_id;
                rec_ssi_pax_progress.ssi_contest_id         := v_valid_ssi_contest_id;
                rec_ssi_pax_progress.activity_date          := v_activity_date;
                rec_ssi_pax_progress.node_name              := v_node_name;   --07/01/2016
                rec_ssi_pax_progress.first_name             := v_first_name;  --07/01/2016
                rec_ssi_pax_progress.last_name              := v_last_name;   --07/01/2016
                rec_ssi_pax_progress.email_address          := v_email_address;  --07/01/2016
                rec_ssi_pax_progress.date_modified          := NULL;
                rec_ssi_pax_progress.modified_by            := NULL;
                rec_ssi_pax_progress.created_by             := v_created_by;
                rec_ssi_pax_progress.date_created           := SYSDATE;
                rec_ssi_pax_progress.version                := 1;
     
                    --Insert into stage_ssi_pax_progress_load            
                    IF v_valid_progress_total IS NOT NULL THEN                          --02/12/2015
                            v_stage := 'Insert into stage_ssi_pax_progress_load table';
                            prc_stg_ssi_pax_progress_ins (rec_ssi_pax_progress,
                                                          v_return_code);
                            --END IF;
                            --      END IF; --Bug # 60805 03/26/2015--Bug # 61749
                            IF v_return_code = 99
                            THEN
                               v_error_count := v_error_count + 1; -- need to count any insert errors
                            ELSE
                               v_count := v_count + 1;
                               v_recs_loaded_cnt := v_recs_loaded_cnt + 1;
                            END IF;
                            
                    END IF;

                    ELSE    --11/30/2016

                    v_stage := 'Initialize the record type';                                --11/30/2016
                    rec_ssi_pax_progress.import_record_id       := v_import_record_id;
                    rec_ssi_pax_progress.import_file_id         := v_import_file_id;
                    rec_ssi_pax_progress.user_name              := v_user_name;
                    rec_ssi_pax_progress.user_id                := v_valid_pax_id;
                    rec_ssi_pax_progress.progress               := v_progress_total;
                    rec_ssi_pax_progress.activity_description   := v_activity_description;
                    rec_ssi_pax_progress.ssi_contest_activity_id:= v_ssi_contest_activity_id;
                    rec_ssi_pax_progress.ssi_contest_id         := v_valid_ssi_contest_id;
                    rec_ssi_pax_progress.activity_date          := v_activity_date;
                    rec_ssi_pax_progress.node_name              := v_node_name;   
                    rec_ssi_pax_progress.first_name             := v_first_name;  
                    rec_ssi_pax_progress.last_name              := v_last_name;   
                    rec_ssi_pax_progress.email_address          := v_email_address;  
                    rec_ssi_pax_progress.date_modified          := NULL;
                    rec_ssi_pax_progress.modified_by            := NULL;
                    rec_ssi_pax_progress.created_by             := v_created_by;
                    rec_ssi_pax_progress.date_created           := SYSDATE;
                    rec_ssi_pax_progress.version                := 1;

                    v_stage := 'Insert into stage_ssi_pax_progress_load table';
                    prc_stg_ssi_pax_progress_ins (rec_ssi_pax_progress,
                                                          v_return_code);
                    
                    rec_import_record_error.item_key      := 'admin.fileload.errors.MISSING_PROPERTY';
                    rec_import_record_error.param1        := 'Progress Total';
                    rec_import_record_error.created_by    := v_created_by;

                    v_stage := 'Insert into import_record_error table';
                    prc_insert_import_record_error (rec_import_record_error);
               
                END IF; --Bug # 60805 03/26/2015--Bug # 61749

            IF MOD (v_count, 10) = 0
            THEN
               COMMIT;
            END IF;
         END IF;                                               -- v_header_rec
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

            --v_error_count := v_error_count + 1; -- need to count any parsing errors --05/20/2015 Bug 61749
      END;
   END LOOP; -- *** end of loop to move data to stage_ssi_pax_progress_load table. ***

   IF v_ssi_contest_exist
   THEN
      -- import staged records
      v_stage := 'MERGE ssi_contest_pax_progress';

      MERGE INTO ssi_contest_pax_progress d
           USING (                                          -- get staged data
                  SELECT s.user_id,
                         s.user_name,
                         s.first_name,
                         s.last_name,
                         s.progress,
                         s.ssi_contest_activity_id,
                         s.ssi_contest_id,
                         s.activity_date
                    FROM stage_ssi_pax_progress_load s
                   WHERE     s.import_file_id = v_import_file_id
                         -- skip records marked as erred
                         AND s.import_record_id NOT IN (SELECT e.import_record_id
                                                          FROM import_record_error e
                                                         WHERE e.import_file_id =
                                                                  v_import_file_id))
                 s
              ON (    d.user_id = s.user_id
                  AND d.ssi_contest_id = s.ssi_contest_id AND NVL(d.ssi_contest_activity_id,-1) = NVL(s.ssi_contest_activity_id,-1))
      WHEN MATCHED
      THEN
         UPDATE -- staged data over lays database values
         SET
            d.activity_amt = s.progress,
            d.activity_date = s.activity_date,
--            d.ssi_contest_activity_id = s.ssi_contest_activity_id,
            -- track update
            d.modified_by = v_created_by,
            d.date_modified = v_timestamp,
            d.version = d.version + 1
                 -- only update if data differs (decode handles null values)
                 WHERE NOT (    DECODE (d.activity_amt, s.progress, 1, 0) = 1
                            AND DECODE (d.activity_date,
                                        s.activity_date, 1,
                                        0) = 1
--                            AND DECODE (d.ssi_contest_activity_id,
--                                        s.ssi_contest_activity_id, 1,
--                                        0) = 1
                                        )
      WHEN NOT MATCHED THEN   --03/25/2015 Bug 60776
      INSERT
      (ssi_contest_pax_progress_id, ssi_contest_id, user_id, activity_amt, activity_date, ssi_contest_activity_id, created_by, date_created, modified_by, date_modified, version
      )
      VALUES 
      (
      ssi_contest_pax_progress_pk_sq.nextval, s.ssi_contest_id, s.user_id, s.progress, s.activity_date, s.ssi_contest_activity_id, v_created_by, SYSDATE, NULL, NULL, 0
      );
      COMMIT;
   ELSE
      RAISE exit_program_exception;
   END IF;

   v_stage := 'Count distinct records that have an error';

   SELECT COUNT (DISTINCT (import_record_id))
     INTO v_error_tbl_count
     FROM import_record_error
    WHERE import_file_id = v_import_file_id;

   v_stage := 'Total error count';
   v_error_count := v_error_count + v_error_tbl_count;

   v_stage := 'Update import_file table with record counts';

   UPDATE import_file f
      SET version = 1,
          status = 'imp',
          date_imported = SYSDATE, --06/20/2016
          import_record_count = v_recs_in_file_cnt,
          import_record_error_count = v_error_count
    WHERE import_file_id = v_import_file_id;

   UTL_FILE.fclose (v_in_file_handler);
   
   p_processed_records_count := v_recs_loaded_cnt;                         --03/04/2015
   p_file_records_count := v_error_count + v_recs_loaded_cnt ;
   
   v_stage := 'Write counts and ending to execution_log table';
   prc_execution_log_entry (
      v_process_name,
      v_release_level,
      'INFO',
         'Count of records on file: '
      || v_recs_in_file_cnt
      || '  Count of records in error: '
      || v_error_count,
      NULL);

 p_out_import_file_id := v_import_file_id; 
 
    COMMIT;
 
 v_stage := 'Call Stack rank update procedure';
 
     prc_execution_log_entry (v_process_name,
                            v_release_level,
                            'INFO',
                            v_stage||' for file ' || p_file_name,
                            NULL);
 pkg_ssi_contest.prc_upd_ssi_contest_stackrank (v_ssi_contest_id,v_return_code, v_out_error_message);
    
  v_stage := 'Update upload_in_progress on a successful completion'; --01/16/2018 G6-3651
  UPDATE SSI_CONTEST
     SET upload_in_progress = 0,
         last_progress_update_date = v_activity_date --02/21/2019
     WHERE ssi_contest_id = v_ssi_contest_id;     --01/16/2018 G6-3651 update statment. 
     
  prc_execution_log_entry (v_process_name,
                            v_release_level,
                            'INFO',
                            'Procedure completed for file ' || p_file_name,
                            NULL);
   COMMIT;
EXCEPTION
   WHEN exit_program_exception
   THEN
      p_out_returncode := 99;

      IF UTL_FILE.is_open (v_in_file_handler)
      THEN
         UTL_FILE.fclose (v_in_file_handler);
      END IF;

      UPDATE import_file
         SET version = 1, status = 'stg_fail'
       WHERE import_file_id = v_import_file_id;
       
   p_processed_records_count := 0;                         --03/04/2015
   p_file_records_count := v_error_count ;

p_out_import_file_id := v_import_file_id;
      prc_execution_log_entry (
         v_process_name,
         v_release_level,
         'ERROR',
            'File: '
         || p_file_name
         || ' failed at Stage: '
         || v_stage
         || ' --> '
         || v_execution_log_msg,
         NULL);
      COMMIT;
   WHEN OTHERS
   THEN
      UTL_FILE.Fclose (v_in_file_handler);

      SELECT COUNT (DISTINCT (import_record_id))
        INTO v_error_tbl_count
        FROM import_record_error
       WHERE import_file_id = v_import_file_id;

      v_error_count := v_error_count + v_error_tbl_count;

      UPDATE import_file f
         SET version = 1,
             status = 'stg_fail',
             import_record_count = v_recs_in_file_cnt,
             import_record_error_count = v_error_count
       WHERE import_file_id = v_import_file_id;
       
   p_processed_records_count := 0;                         --03/04/2015
   p_file_records_count := v_error_count ;

      prc_execution_log_entry (
         v_process_name,
         v_release_level,
         'ERROR',
            'File: '
         || p_file_name
         || ' failed at Stage: '
         || v_stage
         || ' --> '
         || SQLERRM,
         NULL);
      COMMIT;
      p_out_returncode := 99;
      
      p_out_import_file_id := v_import_file_id;
END;                                    -- Procedure prc_ssi_pax_progress_load
/
