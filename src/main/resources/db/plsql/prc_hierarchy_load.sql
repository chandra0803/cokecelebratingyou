CREATE OR REPLACE PROCEDURE prc_hierarchy_load (p_file_name IN VARCHAR2,p_out_returncode OUT NUMBER)
IS
   /*******************************************************************************
   -- Purpose: To process the Hierachy records from the file and populate the
   --          STAGE_HIERARCHY_IMPORT_RECORD with transactional data. Basically resolve
   --          or validate the relational dependencies and mark the records either
   --          for Insert or Update
   --
   -- Person        Date       Comments
   -- -----------   --------   -----------------------------------------------------
   -- Raju N        09/23/2005 Creation
   -- Raju N        11/16/2005 Modifications for the new layout to accommodate
                               for the deletes and updates.
   -- Raju N        11/16/2005 Modifications for the new layout to accommodate
                               for old_parent_node_name
   -- Raju N        01/01/2006 Exectuion log entry changes
   -- Rachel R.        01/17/2006 Changed all references of execution_log_entry to
   --                                       prc_execution_log_entry
   -- Rachel R.        01/17/2006 Changed all references of p_insert_import_record_error
   --                                       to prc_insert_import_record_error
   -- Rachel R.        01/17/2006 Changed all references of p_insert_stg_hierarchy
   --                                       to prc_insert_stg_hierarchy
   -- Rachel R.        01/17/2006 Changed all references of unix_call
   --                                       to prc_unix_call
   -- Arun S        01/20/2006 Added the output parameter so that the procedure can be called from Script generator
   -- Percy M       01/24/2006 Changed from csv to pipe delimited
   -- Percy M       01/31/2006 Commented out call to movefile script
   -- D Murray      05/01/2006 Updated import_file table to stg_fail if file load fails (Bug 12020).
   --                          Removed all move file code - 9i can use prc_move_load_file_9i procedure.
   --                          Moved recs_in_file_cnt before parsing, changed call to prc_insert_stg_hierarchy
   --                          to have a return code, only counting recs_loaded_cnt if insert successful,
   --                          added v_error_count to count any records that fail during parsing or during
   --                          insert to stage table, changed dbms_outputs to execution_log entries. (Bug 12148)
   --                          Reset all variables that are not being reset with each new record (Bug 11885 and 11924).
   --                          Changed v_stage to have better messaging and added v_field_cnt for parsing errors.
   --
   -- Raju N       07/07/2006 Bug#12865 - PARM 2 for errors is not being populated in Import_error_record
   --                         fixed the code to make sure the parm2 value is assigned properly.                          
   -- Raju N       08/01/2006 Bug#12864 - restricted to the node_type_name check
   --                         to the 40 char limit.
   -- Arun S       01/07/2009 Changes to replace UC4
   -- Chidamba     11/28/2012 Change Request G5:
                                •    Improve error outputs  - have more descriptive errors to allow for remediation, always allow errors be seen from the file processing page (implement)
                                •    Remove need to specify insert and update. ‘D’ is required for delete only.  (implement)                                
                                •    Org unit type – make field optional (implement)
                                •    Allow additional characteristics – five additional characteristics (implement)
   -- Chidamba     12/07/2012 Change Request to create Default Node Type for type Not-Applicable.  
   -- Chidamba     12/31/2012 Change to get CMS value from FNC_CMS_ASSET_CODE_VAL_EXTR as hierarchy.hierarchy assert 
                              code return mulitple value along with DEFAULT. No unique Assert code availabe for DEFAULT node Type
  -- Chidamba       01/16/2012 Defect ID : 1508 - Fileload Participant-Update error message admin.fileload.errors.CHARACTERISTIC_NOT_VALID
  -- Ravi Dhanekula 06/07/2013  Defect Id : 3366 Updated error message for invalid node type.
                                Removed the unwanted UPPER function.
  -- Ravi Dhanekula  07/13/2015 07/13/2015 Bug # 60801 Change made to ignore the value given in old_parent_node_name.       
  --Suresh J         04/29/2019  Issue #1945 - Update action type is not working in Hierarchy load                            
  *******************************************************************************/
   --MISCELLANIOUS

   c_delimiter                  CONSTANT  VARCHAR2(10):='|';
   v_count                      NUMBER := 0; --for commit count
   v_stage                      VARCHAR2(500);
   v_return_code                NUMBER(5);  -- return from insert call
   v_header_rec                 BOOLEAN := FALSE ;

   --UTL_FILE
   v_in_file_handler            UTL_FILE.file_type;
   --v_path_name                  os_propertyset.string_val%TYPE :=
   --                                  fnc_get_system_variable ('import.file.utl_path', 'db_utl_path');
   v_text                       VARCHAR2(4000);
   v_file_name                  VARCHAR2(500);
   v_directory_path             VARCHAR2(4000);
   v_directory_name             VARCHAR2(30);

   -- RECORD TYPE DECLARATION
   rec_stg_hierarchy            stage_hierarchy_import_record%ROWTYPE;
   rec_import_file              import_file%ROWTYPE;
   rec_import_record_error      import_record_error%ROWTYPE;

   --HIERARCHY LOAD
   v_import_record_id           stage_hierarchy_import_record.import_record_id%TYPE;
   v_import_file_id             stage_hierarchy_import_record.import_file_id%TYPE;
   v_action_type                stage_hierarchy_import_record.action_type%TYPE;
   v_import_record_error_id     import_record_error.import_record_error_id%TYPE;
   v_node_name                  stage_hierarchy_import_record.NAME%TYPE;
   v_old_node_name              stage_hierarchy_import_record.old_name%TYPE;
   v_move_to_node_name          stage_hierarchy_import_record.move_to_node_name%TYPE;
   v_description                stage_hierarchy_import_record.description%TYPE;
   v_node_type_id               stage_hierarchy_import_record.node_type_id%TYPE;
   v_node_type_name             stage_hierarchy_import_record.node_type_name%TYPE;
   v_parent_node_name           stage_hierarchy_import_record.old_parent_node_name%TYPE;
   v_old_parent_node_name       stage_hierarchy_import_record.parent_node_name%TYPE;
   v_characteristic_id1         stage_hierarchy_import_record.characteristic_id1%TYPE;
   v_characteristic_id2         stage_hierarchy_import_record.characteristic_id2%TYPE;
   v_characteristic_id3         stage_hierarchy_import_record.characteristic_id3%TYPE;
   v_characteristic_id4         stage_hierarchy_import_record.characteristic_id4%TYPE;
   v_characteristic_id5         stage_hierarchy_import_record.characteristic_id5%TYPE;
   v_characteristic_id6         stage_hierarchy_import_record.characteristic_id6%TYPE;
   v_characteristic_id7         stage_hierarchy_import_record.characteristic_id7%TYPE;
   v_characteristic_id8         stage_hierarchy_import_record.characteristic_id8%TYPE;
   v_characteristic_id9         stage_hierarchy_import_record.characteristic_id9%TYPE;
   v_characteristic_id10         stage_hierarchy_import_record.characteristic_id10%TYPE;
   v_characteristic_name1       stage_hierarchy_import_record.characteristic_name1%TYPE;
   v_rt_characteristic_name1    stage_hierarchy_import_record.characteristic_name1%TYPE;
   v_characteristic_value1      stage_hierarchy_import_record.characteristic_value1%TYPE;
   v_characteristic_name2       stage_hierarchy_import_record.characteristic_name2%TYPE;
   v_rt_characteristic_name2    stage_hierarchy_import_record.characteristic_name2%TYPE;
   v_characteristic_value2      stage_hierarchy_import_record.characteristic_value2%TYPE;
   v_characteristic_name3       stage_hierarchy_import_record.characteristic_name3%TYPE;
   v_rt_characteristic_name3    stage_hierarchy_import_record.characteristic_name3%TYPE;
   v_characteristic_value3      stage_hierarchy_import_record.characteristic_value3%TYPE;
   v_characteristic_name4       stage_hierarchy_import_record.characteristic_name4%TYPE;
   v_rt_characteristic_name4    stage_hierarchy_import_record.characteristic_name4%TYPE;
   v_characteristic_value4      stage_hierarchy_import_record.characteristic_value4%TYPE;
   v_characteristic_name5       stage_hierarchy_import_record.characteristic_name5%TYPE;
   v_rt_characteristic_name5    stage_hierarchy_import_record.characteristic_name5%TYPE;
   v_characteristic_value5      stage_hierarchy_import_record.characteristic_value5%TYPE;
   v_characteristic_name6       stage_hierarchy_import_record.characteristic_name6%TYPE;
   v_rt_characteristic_name6    stage_hierarchy_import_record.characteristic_name6%TYPE;
   v_characteristic_value6      stage_hierarchy_import_record.characteristic_value6%TYPE;
   v_characteristic_name7       stage_hierarchy_import_record.characteristic_name7%TYPE;
   v_rt_characteristic_name7    stage_hierarchy_import_record.characteristic_name7%TYPE;
   v_characteristic_value7      stage_hierarchy_import_record.characteristic_value7%TYPE;
   v_characteristic_name8       stage_hierarchy_import_record.characteristic_name8%TYPE;
   v_rt_characteristic_name8    stage_hierarchy_import_record.characteristic_name8%TYPE;
   v_characteristic_value8      stage_hierarchy_import_record.characteristic_value8%TYPE;
   v_characteristic_name9       stage_hierarchy_import_record.characteristic_name9%TYPE;
   v_rt_characteristic_name9    stage_hierarchy_import_record.characteristic_name9%TYPE;
   v_characteristic_value9      stage_hierarchy_import_record.characteristic_value9%TYPE;
   v_characteristic_name10      stage_hierarchy_import_record.characteristic_name10%TYPE;
   v_rt_characteristic_name10   stage_hierarchy_import_record.characteristic_name10%TYPE;
   v_characteristic_value10     stage_hierarchy_import_record.characteristic_value10%TYPE;
      
   v_created_by                 stage_hierarchy_import_record.created_by%TYPE := 0;
   v_date_created               stage_hierarchy_import_record.date_created%TYPE;

   v_parent_node_cnt            NUMBER (12) := 0;

   -- EXECUTION LOG VARIABLES
   v_process_name          execution_log.process_name%type  := 'PRC_HIERARCHY_LOAD' ;
   v_release_level         execution_log.release_level%type := '1';
   v_execution_log_msg     execution_log.text_line%TYPE;
   v_node_type_flg              BOOLEAN;
   
   -- COUNTS
   v_recs_in_file_cnt           INTEGER := 0;
   v_field_cnt                  INTEGER := 0;
   v_recs_loaded_cnt            INTEGER := 0;
   v_error_count                PLS_INTEGER := 0;
   v_error_tbl_count            PLS_INTEGER := 0;
        
  -- EXCEPTIONS
  exit_program_exception    EXCEPTION;


BEGIN
   v_stage := 'Write start to execution_log table';
   p_out_returncode := 0;
   prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure Started for file '
                           ||p_file_name,null);
   COMMIT;

   v_stage := 'Get Directory path and File name from p_file_name';              --01/07/2009 Changes to replace UC4 
   prc_get_directory_file_name ( p_file_name, v_directory_path, v_file_name );

   BEGIN
      v_stage := 'Insert into import_file table';
      SELECT import_file_pk_sq.NEXTVAL
        INTO v_import_file_id
        FROM DUAL;

      INSERT INTO import_file
                  (import_file_id,
                   file_name,
                   file_type,
                   import_record_count,
                   import_record_error_count,
                   status,
                   date_staged,
                   staged_by,
                   version
                  )
           VALUES (v_import_file_id,
                   SUBSTR (v_file_name, 1, INSTR (v_file_name, '.') - 1)|| '_'|| v_import_file_id,
                   'hier',
                   0,
                   0,
                   'stg_in_process',
                   SYSDATE,
                   v_created_by,
                   1
                  );
   EXCEPTION
      WHEN OTHERS THEN
       prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                               'Stage: '||v_stage||' -- '||SQLERRM,null);
   END;

   BEGIN
     v_stage := 'Get Directory name';                                           --01/07/2009 Changes to replace UC4
     SELECT directory_name
       INTO v_directory_name
       FROM all_directories
      WHERE directory_path = v_directory_path
        AND ROWNUM = 1;   
   EXCEPTION
     WHEN OTHERS THEN
       v_execution_log_msg := SQLERRM; 
       RAISE exit_program_exception;  
   END;

   v_stage := 'Open file for read: '||v_file_name;
   v_in_file_handler := UTL_FILE.fopen (v_directory_name, v_file_name, 'r');

   LOOP
      --*** move records from text file into the table stage_import_record.**
      v_stage := 'Reset variables';
      v_field_cnt := 0;
      v_characteristic_id1 := NULL;
      v_characteristic_id2 := NULL;
      v_characteristic_id3 := NULL;
      v_characteristic_id4 := NULL;
      v_characteristic_id5 := NULL;
      v_characteristic_id6 := NULL;
      v_characteristic_id7 := NULL;
      v_characteristic_id8 := NULL;
      v_characteristic_id9 := NULL;
      v_characteristic_id10 := NULL;     
      v_node_type_id        := NULL;  --11/28/2012
      v_node_type_flg       := FALSE;  --12/07/2012

      BEGIN
         v_stage := 'Get record';
         UTL_FILE.get_line (v_in_file_handler, v_text);

         IF v_header_rec THEN
            v_header_rec := FALSE;
         ELSE
            v_stage := 'Validations';           --10/08/2015 Start
            SELECT import_record_pk_sq.NEXTVAL
              INTO v_import_record_id
              FROM DUAL;

            v_stage := 'Check for required columns';
            rec_import_record_error.date_created := SYSDATE;
            rec_import_record_error.created_by := 0;
            rec_import_record_error.import_record_id := v_import_record_id;
            rec_import_record_error.import_file_id := v_import_file_id; --10/08/2015 End
            
            v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
            v_stage := 'Parsing record number: '||v_recs_in_file_cnt;

            v_field_cnt := v_field_cnt + 1;
            v_action_type :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 1, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_node_name :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 2, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_old_node_name :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 3, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_move_to_node_name :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 4, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_description :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 5, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_node_type_name :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 6, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_parent_node_name :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 7, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_old_parent_node_name := NULL;--07/13/2015 Bug # 60801
--                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 8, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name1 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 9, c_delimiter))),CHR(13)); -- Removed Upper function 11/25/2013

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_value1 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 10, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name2 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 11, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_value2 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 12, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name3 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 13, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_value3 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 14, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name4 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 15, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_value4 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 16, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name5 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 17, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_value5 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 18, c_delimiter))),CHR(13));
                 
            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name6 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 19, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_value6 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 20, c_delimiter))),CHR(13));
                 
            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name7 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 21, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_value7 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 22, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name8 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 23, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_value8 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 24, c_delimiter))),CHR(13));
            
            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name9 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 25, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_value9 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 26, c_delimiter))),CHR(13));
                 
            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name10 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 27, c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_value10 :=
                 REPLACE(LTRIM(RTRIM(fnc_pipe_parse (v_text, 28, c_delimiter))),CHR(13));
                 

            /*v_stage := 'Validations'; --10/08/2015 Commented
            SELECT import_record_pk_sq.NEXTVAL
              INTO v_import_record_id
              FROM DUAL;

            v_stage := 'Check for required columns';
            rec_import_record_error.date_created := SYSDATE;
            rec_import_record_error.created_by := 0;
            rec_import_record_error.import_record_id := v_import_record_id;
            rec_import_record_error.import_file_id := v_import_file_id;*/

            IF LTRIM (RTRIM (v_node_name)) IS NULL THEN
               IF v_parent_node_cnt = 0 THEN
                  v_parent_node_cnt := 1;
               ELSE
                  rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
                  rec_import_record_error.param1 := 'Node Name';
                  rec_import_record_error.param2 := NVL(v_node_name,'**NULL**');
                  rec_import_record_error.created_by := v_created_by;
                  prc_insert_import_record_error (rec_import_record_error);
               END IF;
               
             /*ELSIF LTRIM (RTRIM (v_node_type_name)) IS NULL THEN              --11/28/2012
               rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'Node Type Name';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
               v_node_type_name := ' ';*/
                              
            END IF;  -- v_node_name is null


            v_stage := 'Check relational keys';
           BEGIN
               v_stage := 'Validate node type name';
               IF LTRIM (RTRIM (v_node_type_name)) IS NOT NULL THEN
                  DECLARE
                     v_nt_id   VARCHAR2 (70);
                   BEGIN
                     SELECT node_type_id
                       INTO v_nt_id
                       FROM node_type_name
                      WHERE cm_name = substr( UPPER(LTRIM (RTRIM (v_node_type_name))),1,40);

                     IF v_nt_id = NULL THEN
                        rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_NODETYPE';--06/07/2013
                        rec_import_record_error.param1 := 'Node Type Name';                        
                        rec_import_record_error.created_by := v_created_by;
                        prc_insert_import_record_error (rec_import_record_error);
                     ELSIF v_nt_id = 'X' THEN
                        rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_NODETYPE';--06/07/2013
                        rec_import_record_error.param1 := 'Node Type Name';                        
                        rec_import_record_error.created_by := v_created_by;
                        prc_insert_import_record_error (rec_import_record_error);
                     ELSE
                        v_node_type_id := v_nt_id;
                     END IF;  -- v_nt_id = null
                  EXCEPTION
                     WHEN NO_DATA_FOUND THEN
                      rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_NODETYPE';--06/07/2013
                        rec_import_record_error.param1 := 'Node Type Name';                        
                        rec_import_record_error.created_by := v_created_by;
                        prc_insert_import_record_error (rec_import_record_error); 
                     WHEN OTHERS THEN
                        v_nt_id := NULL;
                        rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                        rec_import_record_error.param1 := 'Node Type Name ';
                        rec_import_record_error.param2 := v_node_type_name||'. - at '||v_stage||SUBSTR (SQLERRM, 1,250);
                        rec_import_record_error.created_by := v_created_by;
                        prc_insert_import_record_error (rec_import_record_error);
                  END;
                  
               ELSE
                  
                  BEGIN             --12/07/2012 Validate to get default Node_type  
                    /*SELECT node_type_id           --12/31/2012 Change 
                      INTO v_node_type_id
                      FROM node_type_name
                      WHERE cm_name = 'DEFAULT';         --node type "Not Applicable"*/
                      SELECT node_type_id
                        INTO v_node_type_id 
                        FROM node_type
                       WHERE UPPER(FNC_CMS_ASSET_CODE_VAL_EXTR(cm_asset_code,name_cm_key,'en_US')) = 'DEFAULT'; 

                     v_node_type_flg := TRUE;
                                          
                    EXCEPTION
                     WHEN OTHERS THEN                        
                        rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                        rec_import_record_error.param1 := 'Node Type Name';
                        rec_import_record_error.param2 := 'Default node type not avalilabe. - at '||v_stage||SUBSTR (SQLERRM, 1,250);
                        rec_import_record_error.created_by := v_created_by;
                        prc_insert_import_record_error (rec_import_record_error);
                  END;   
                      
               END IF;  -- v_node_type_name is null
             
                
             --IF LTRIM (RTRIM (v_node_type_name)) IS NOT NULL THEN
               -- characteristicID
               v_stage := 'Validate characteristic';
               IF v_characteristic_name1 IS NOT NULL THEN
                  v_rt_characteristic_name1 :=
                            fnc_getcharacteristic_id (v_characteristic_name1,v_node_type_id);

                  IF v_rt_characteristic_name1 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name1';
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name1 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID'; --01/16/2012 added for Characteristic 1 to 10
                     rec_import_record_error.param1 := 'Characteristic Name1 - '||v_characteristic_name1;                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);    
                  ELSIF v_rt_characteristic_name1 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name1 ';
                     rec_import_record_error.param2 := v_characteristic_name1||'. - at '||v_stage;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id1 := v_rt_characteristic_name1;
                  END IF; -- v_rt_characteristic_name1 is null
               ELSE
                  v_characteristic_id1 := v_characteristic_name1;
               END IF;  -- v_characteristic_name1 is not null

               IF v_characteristic_name2 IS NOT NULL THEN
                  v_rt_characteristic_name2 :=
                            fnc_getcharacteristic_id (v_characteristic_name2,v_node_type_id);

                  IF v_rt_characteristic_name2 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name2';                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name2 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';
                     rec_import_record_error.param1 := 'Characteristic Name2 - '||v_characteristic_name2;                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);     
                  ELSIF v_rt_characteristic_name2 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name2 ';                                                       
                     rec_import_record_error.param2 := v_characteristic_name2||'. - at '||v_stage;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id2 := v_rt_characteristic_name2;
                  END IF; -- v_rt_characteristic_name2 is null
               ELSE
                  v_characteristic_id2 := v_characteristic_name2;
               END IF; -- v_characteristic_name2 is not null

               IF v_characteristic_name3 IS NOT NULL THEN
                  v_rt_characteristic_name3 :=
                            fnc_getcharacteristic_id (v_characteristic_name3,v_node_type_id);

                  IF v_rt_characteristic_name3 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name3';                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name3 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';
                     rec_import_record_error.param1 := 'Characteristic Name3 - '||v_characteristic_name3;                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error); 
                  ELSIF v_rt_characteristic_name3 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name3 ';
                     rec_import_record_error.param2 := v_characteristic_name3||'. - at '||v_stage;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id3 := v_rt_characteristic_name3;
                  END IF; -- v_rt_characteristic_name3 is null
               ELSE
                  v_characteristic_id3 := v_characteristic_name3;
               END IF; -- v_characteristic_name3 is not null

               IF v_characteristic_name4 IS NOT NULL THEN
                  v_rt_characteristic_name4 :=
                            fnc_getcharacteristic_id (v_characteristic_name4,v_node_type_id);

                  IF v_rt_characteristic_name4 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name4';                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name4 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';
                     rec_import_record_error.param1 := 'Characteristic Name4 - '||v_characteristic_name4;                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error); 
                  ELSIF v_rt_characteristic_name4 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name4 ';
                     rec_import_record_error.param2 := v_characteristic_name4||'. - at '||v_stage;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id4 := v_rt_characteristic_name4;
                  END IF; -- v_rt_characteristic_name4 is null
               ELSE
                  v_characteristic_id4 := v_characteristic_name4;
               END IF; -- v_characteristic_name4 is not null

               IF v_characteristic_name5 IS NOT NULL THEN
                  v_rt_characteristic_name5 :=
                            fnc_getcharacteristic_id (v_characteristic_name5,v_node_type_id);

                  IF v_rt_characteristic_name5 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name5';                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name5 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';
                     rec_import_record_error.param1 := 'Characteristic Name5 - '||v_characteristic_name5;                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);    
                  ELSIF v_rt_characteristic_name5 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name5 ';
                     rec_import_record_error.param2 := v_characteristic_name5||'. - at '||v_stage;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id5 := v_rt_characteristic_name5;
                  END IF; -- v_rt_characteristic_name5 is null
               ELSE
                  v_characteristic_id5 := v_characteristic_name5;
               END IF; -- v_characteristic_name5 is not null
               
               IF v_characteristic_name6 IS NOT NULL THEN
                  v_rt_characteristic_name6 :=
                            fnc_getcharacteristic_id (v_characteristic_name6,v_node_type_id);

                  IF v_rt_characteristic_name6 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name6';                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name6 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';
                     rec_import_record_error.param1 := 'Characteristic Name6 - '||v_characteristic_name6;                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error); 
                  ELSIF v_rt_characteristic_name6 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name6 ';
                     rec_import_record_error.param2 := v_characteristic_name6||'. - at '||v_stage;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id6 := v_rt_characteristic_name6;
                  END IF; -- v_rt_characteristic_name6 is null
               ELSE
                  v_characteristic_id6 := v_characteristic_name6;
               END IF;  -- v_characteristic_name6 is not null

               IF v_characteristic_name7 IS NOT NULL THEN
                  v_rt_characteristic_name7 :=
                            fnc_getcharacteristic_id (v_characteristic_name7,v_node_type_id);

                  IF v_rt_characteristic_name7 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name7';                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name7 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';
                     rec_import_record_error.param1 := 'Characteristic Name7 - '||v_characteristic_name7;                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error); 
                  ELSIF v_rt_characteristic_name7 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name7 ';
                     rec_import_record_error.param2 := v_characteristic_name7||'. - at '||v_stage;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id7 := v_rt_characteristic_name7;
                  END IF; -- v_rt_characteristic_name7 is null
                  
               ELSE
                  v_characteristic_id7 := v_characteristic_name7;
               END IF;  -- v_characteristic_name7 is not null
               
               IF v_characteristic_name8 IS NOT NULL THEN
                  v_rt_characteristic_name8 :=
                            fnc_getcharacteristic_id (v_characteristic_name8,v_node_type_id);

                  IF v_rt_characteristic_name8 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name8';                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name8 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';
                     rec_import_record_error.param1 := 'Characteristic Name8 - '||v_characteristic_name8;                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error); 
                  ELSIF v_rt_characteristic_name8 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name8  ';
                     rec_import_record_error.param2 := v_characteristic_name8||'. - at '||v_stage;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id8 := v_rt_characteristic_name8;
                  END IF; -- v_rt_characteristic_name8 is null
                  
               ELSE
                  v_characteristic_id8 := v_characteristic_name8;
               END IF;  -- v_characteristic_name8 is not null
               
               IF v_characteristic_name9 IS NOT NULL THEN
                  v_rt_characteristic_name9 :=
                            fnc_getcharacteristic_id (v_characteristic_name9,v_node_type_id);

                  IF v_rt_characteristic_name9 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name9';                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name9 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';
                     rec_import_record_error.param1 := 'Characteristic Name9 - '||v_characteristic_name9;                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);    
                  ELSIF v_rt_characteristic_name9 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name9 ';
                     rec_import_record_error.param2 := v_characteristic_name9||'. - at '||v_stage;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id9 := v_rt_characteristic_name9;
                  END IF; -- v_rt_characteristic_name9 is null
                  
               ELSE
                  v_characteristic_id9 := v_characteristic_name9;
               END IF;  -- v_characteristic_name9 is not null
               
               IF v_characteristic_name10 IS NOT NULL THEN
                  v_rt_characteristic_name10 :=
                            fnc_getcharacteristic_id (v_characteristic_name10,v_node_type_id);

                  IF v_rt_characteristic_name10 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name10';                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name10 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';
                     rec_import_record_error.param1 := 'Characteristic Name10 - '||v_characteristic_name10;                     
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error); 
                  ELSIF v_rt_characteristic_name10 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name10 ';
                     rec_import_record_error.param2 := v_characteristic_name10||'. - at '||v_stage;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id10 := v_rt_characteristic_name10;
                  END IF; -- v_rt_characteristic_name10 is null
                  
               ELSE
                  v_characteristic_id10 := v_characteristic_name10;
               END IF;  -- v_characteristic_name10 is not null
              
            -- END IF;
                              
           END; -- chk relational keys
            
            
            /*IF LTRIM (RTRIM (v_action_type)) IS NULL THEN
               rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
               rec_import_record_error.param1 := 'Action Type';
               rec_import_record_error.created_by := v_created_by;
               prc_insert_import_record_error (rec_import_record_error);
               v_action_type := ' ';
            ELSE
               IF UPPER (v_action_type) = 'I' THEN
                  v_action_type := 'add';
               ELSIF UPPER (v_action_type) = 'U' THEN
                  v_action_type := 'upd';
               ELSIF UPPER (v_action_type) = 'D' THEN
                  v_action_type := 'del';
               ELSE
                  rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                  rec_import_record_error.param1 := 'Action Type';
                  rec_import_record_error.param2 := v_action_type;
                  rec_import_record_error.created_by := v_created_by;
                  prc_insert_import_record_error (rec_import_record_error);
                  v_action_type := ' ';
               END IF;
            END IF; -- v_action_type is null*/
            IF LTRIM (RTRIM (v_action_type)) IS NULL THEN                       --11/28/2012
               NULL;  --- will process insert and update in Verification stage
              /*BEGIN
                    
                IF v_node_name IS NULL 
                 SELECT name
                   INTO v_node_name 
                   FROM node
                  WHERE name = lower(v_node_name);
                END IF;
                v_action_type := 'upd';                
              EXCEPTION
               WHEN NO_DATA_FOUND THEN
                 v_action_type := 'add';
              END;  */
            ELSE
               IF UPPER (v_action_type) = 'D' THEN
                  v_action_type := 'del';
                --               ELSE                           --04/29/2019 NOTE: pkg_hierarchy_verify_import.p_upd_action_type_stg handles update and insert scenarios
                --                  rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                --                  rec_import_record_error.param1   := 'Action Type';
                --                  rec_import_record_error.param2   := v_action_type;    
                --                  rec_import_record_error.created_by := v_created_by;
                --                  prc_insert_import_record_error (rec_import_record_error);
                --                  v_action_type := ' ';
               END IF;
            END IF; -- v_action_type is null

            IF v_node_type_flg THEN                 --12/07/2012
              v_node_type_id := NULL;
            END IF;
            
            v_stage := 'Initialize the record type';
            rec_stg_hierarchy.import_record_id := v_import_record_id;
            rec_stg_hierarchy.import_file_id := v_import_file_id;
            rec_stg_hierarchy.action_type := v_action_type;
            rec_stg_hierarchy.NAME := v_node_name;
            rec_stg_hierarchy.old_name := v_old_node_name;
            rec_stg_hierarchy.move_to_node_name := v_move_to_node_name;
            rec_stg_hierarchy.description := v_description;
            rec_stg_hierarchy.node_type_id := v_node_type_id;
            rec_stg_hierarchy.node_type_name := v_node_type_name;
            rec_stg_hierarchy.parent_node_name := v_parent_node_name;
            rec_stg_hierarchy.old_parent_node_name := v_old_parent_node_name;
            rec_stg_hierarchy.characteristic_id1 := v_characteristic_id1;
            rec_stg_hierarchy.characteristic_id2 := v_characteristic_id2;
            rec_stg_hierarchy.characteristic_id3 := v_characteristic_id3;
            rec_stg_hierarchy.characteristic_id4 := v_characteristic_id4;
            rec_stg_hierarchy.characteristic_id5 := v_characteristic_id5;
            rec_stg_hierarchy.characteristic_id6 := v_characteristic_id6;
            rec_stg_hierarchy.characteristic_id7 := v_characteristic_id7;
            rec_stg_hierarchy.characteristic_id8 := v_characteristic_id8;
            rec_stg_hierarchy.characteristic_id9 := v_characteristic_id9;
            rec_stg_hierarchy.characteristic_id10 := v_characteristic_id10;            
            rec_stg_hierarchy.characteristic_name1 := v_characteristic_name1;
            rec_stg_hierarchy.characteristic_name2 := v_characteristic_name2;
            rec_stg_hierarchy.characteristic_name3 := v_characteristic_name3;
            rec_stg_hierarchy.characteristic_name4 := v_characteristic_name4;
            rec_stg_hierarchy.characteristic_name5 := v_characteristic_name5;
            rec_stg_hierarchy.characteristic_name6 := v_characteristic_name6;
            rec_stg_hierarchy.characteristic_name7 := v_characteristic_name7;
            rec_stg_hierarchy.characteristic_name8 := v_characteristic_name8;
            rec_stg_hierarchy.characteristic_name9 := v_characteristic_name9;
            rec_stg_hierarchy.characteristic_name10 := v_characteristic_name10;
            rec_stg_hierarchy.characteristic_value1 := v_characteristic_value1;
            rec_stg_hierarchy.characteristic_value2 := v_characteristic_value2;
            rec_stg_hierarchy.characteristic_value3 := v_characteristic_value3;
            rec_stg_hierarchy.characteristic_value4 := v_characteristic_value4;
            rec_stg_hierarchy.characteristic_value5 := v_characteristic_value5;
            rec_stg_hierarchy.characteristic_value6 := v_characteristic_value6;
            rec_stg_hierarchy.characteristic_value7 := v_characteristic_value7;
            rec_stg_hierarchy.characteristic_value8 := v_characteristic_value8;
            rec_stg_hierarchy.characteristic_value9 := v_characteristic_value9;
            rec_stg_hierarchy.characteristic_value10 := v_characteristic_value10;            
            rec_stg_hierarchy.created_by := v_created_by;
            rec_stg_hierarchy.date_created := SYSDATE;


            --Insert into stage_hierarchy_import_record
            v_stage := 'Insert into stage_hierarchy_import_record table';
            prc_insert_stg_hierarchy (rec_stg_hierarchy, v_return_code);
            IF v_return_code = 99 THEN
              v_error_count := v_error_count + 1;  -- need to count any insert errors
            ELSE
              v_count := v_count + 1;
              v_recs_loaded_cnt := v_recs_loaded_cnt + 1;
            END IF; -- v_return_code

            IF MOD (v_count, 10) = 0 THEN
               COMMIT;
            END IF;
         END IF;  -- v_header_rec
      EXCEPTION
         WHEN NO_DATA_FOUND THEN
            -- end of file
            --Program continues on to compare rec cnt.  Then final
            -- file_load_audit entry is done.
            UTL_FILE.fclose (v_in_file_handler);
            EXIT;
         WHEN OTHERS THEN
            IF v_stage LIKE 'Parsing%' THEN
              rec_import_record_error.item_key := 'admin.fileload.errors.PARSING_ERROR'; --10/08/2015 Start
              rec_import_record_error.param1   := v_recs_in_file_cnt;
              rec_import_record_error.param2   := v_field_cnt;
              rec_import_record_error.created_by   := v_created_by;
              prc_insert_import_record_error(rec_import_record_error);
            ELSE                                                                    --10/08/2015 End
              v_stage := v_stage||' field number: '||v_field_cnt;
              prc_execution_log_entry(v_process_name,v_release_level,'ERROR',       --10/08/2015
                                    'Stage: '||v_stage||' --> '||SQLERRM,null);
              v_error_count := v_error_count + 1;                                   --10/08/2015
            END IF;
            /*prc_execution_log_entry(v_process_name,v_release_level,'ERROR',       --10/08/2015
                                    'Stage: '||v_stage||' --> '||SQLERRM,null);

            v_error_count := v_error_count + 1;*/  -- need to count any parsing errors
      END;  -- Get Record
   END LOOP; -- *** end of loop to move data to stage_import_record table. ***
   COMMIT;
   
   v_stage := 'Count distinct records that have an error';
   SELECT COUNT(DISTINCT(import_record_id))
     INTO v_error_tbl_count
     FROM import_record_error
    WHERE import_file_id = v_import_file_id;

   v_stage := 'Total error count';
   v_error_count := v_error_count + v_error_tbl_count;

   v_stage := 'Update import_file table with record counts';
   UPDATE import_file f
      SET VERSION = 1,
          status = 'stg',
          import_record_count = v_recs_in_file_cnt,
          import_record_error_count = v_error_count
    WHERE import_file_id = v_import_file_id;

   UTL_FILE.fclose (v_in_file_handler);
   
   v_stage := 'Write counts and ending to execution_log table';
   prc_execution_log_entry(v_process_name,v_release_level,'INFO','Count of records on file: '||v_recs_in_file_cnt||
                           '  Count of records in error: '||v_error_count,null);
   prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure completed for file '
                           ||p_file_name,null);
   COMMIT;
EXCEPTION
   WHEN exit_program_exception THEN
      p_out_returncode := 99;

      UPDATE import_file 
         SET version        = 1,
             status         = 'stg_fail'
       WHERE import_file_id = v_import_file_id;

      prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                              'File: '||p_file_name||' failed at Stage: '||v_stage||
                              ' --> '||v_execution_log_msg,null);
      COMMIT;

   WHEN OTHERS THEN
      UTL_FILE.fclose (v_in_file_handler);

      SELECT COUNT(DISTINCT(import_record_id))
        INTO v_error_tbl_count
        FROM import_record_error
       WHERE import_file_id = v_import_file_id;

      v_error_count := v_error_count + v_error_tbl_count;
      
      UPDATE import_file f
         SET version                   = 1,
             status                    = 'stg_fail',
             import_record_count       = v_recs_in_file_cnt,
             import_record_error_count = v_error_count
       WHERE import_file_id = v_import_file_id;

      prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                              'File: '||p_file_name||' failed at Stage: '||v_stage||
                              ' --> '||SQLERRM,null);
      COMMIT;
      p_out_returncode := 99;
          
END prc_hierarchy_load;
/
