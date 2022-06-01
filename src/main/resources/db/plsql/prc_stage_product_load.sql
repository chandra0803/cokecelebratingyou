CREATE OR REPLACE PROCEDURE prc_stage_product_load (p_file_name IN VARCHAR2,p_out_returncode OUT NUMBER)
IS
   /*******************************************************************************
   -- Purpose: To process the Product records from the file and populate the
   --          STAGE_PRODUCT_IMPORT_RECORD with transactional data. Basically resolve
   --          or validate the relational dependencies and mark the records
   --          for Insert
   --
   -- Person        Date       Comments
   -- -----------   --------   -----------------------------------------------------
   -- Arun S        01/31/2006 Initial Creation
   -- Arun S        03/10/2006 Changes to handle product characteristics
   -- D Murray      04/28/2006 Updated import_file table to stg_fail if file load fails (Bug 12020).
   --                          Removed all move file code - 9i can use prc_move_load_file_9i procedure.
   --                          Moved recs_in_file_cnt before parsing, changed call to prc_insert_stg_product
   --                          to have a return code, only counting recs_loaded_cnt if insert successful,
   --                          added v_error_count to count any records that fail during parsing or during
   --                          insert to stage table, changed dbms_outputs to execution_log entries. (Bug 12148)
   --                          Removed null constraints and added error if the following fields are null -
   --                          product_name, product_desc, category_name, category_desc (Bug 12148).
   --                          Changed v_stage to have better messaging and added v_field_cnt for parsing errors.
   --R K Goyal      05/15/2007 File name having path also, modified code to remove
   -- Arun S        01/19/2009 Changes to replace UC4   
   -- Chidamba      01/16/2012 Defect ID : 1508 - Fileload Participant-Update error message
    --Ravi Dhanekula 03/25/2014  Bug # 52297 fnc_getsubcategory_id created to get sub_category_id.
    --nagarajs       07/16/2014  Bug # 54831 - Fixed error count issue
   *******************************************************************************/
   --MISCELLANIOUS
   c_delimiter                  CONSTANT  VARCHAR2(10):='|';
   v_count                      NUMBER := 0; --for commit count
   v_stage                      VARCHAR2(500);
   v_return_code                NUMBER(5); -- return from insert call
   v_header_rec                 BOOLEAN := FALSE;

   --UTL_FILE
   v_in_file_handler            UTL_FILE.file_type;
   --v_path_name                  os_propertyset.string_val%TYPE :=
   --                                  fnc_get_system_variable ('import.file.utl_path', 'db_utl_path');
   v_text                       VARCHAR2 (500);
   v_file_name                  VARCHAR2(500); 
   v_directory_path             VARCHAR2(4000);
   v_directory_name             VARCHAR2(30);
   
   -- RECORD TYPE DECLARATION
   rec_stg_product              stage_product_import_record%ROWTYPE;
   rec_import_file              import_file%ROWTYPE;
   rec_import_record_error      import_record_error%ROWTYPE;

   --PRODUCT LOAD
   v_import_record_id           stage_product_import_record.import_record_id%TYPE;
   v_import_file_id             stage_product_import_record.import_file_id%TYPE;
   v_action_type                stage_product_import_record.action_type%TYPE;
   v_import_record_error_id     import_record_error.import_record_error_id%TYPE;
   v_product_id                 stage_product_import_record.product_id%TYPE;
   v_product_name               stage_product_import_record.product_name%TYPE;
   v_product_desc               stage_product_import_record.product_desc%TYPE;
   v_sku_code                   stage_product_import_record.sku_code%TYPE;
   v_category_id                stage_product_import_record.category_id%TYPE;
   v_category_name              stage_product_import_record.category_name%TYPE;
   v_category_desc              stage_product_import_record.category_desc%TYPE;
   v_subcategory_id             stage_product_import_record.subcategory_id%TYPE;
   v_subcategory_name           stage_product_import_record.subcategory_name%TYPE;
   v_subcategory_desc           stage_product_import_record.subcategory_desc%TYPE;

   v_characteristic_id1         stage_product_import_record.characteristic_id1%TYPE;
   v_characteristic_id2         stage_product_import_record.characteristic_id2%TYPE;
   v_characteristic_id3         stage_product_import_record.characteristic_id3%TYPE;
   v_characteristic_id4         stage_product_import_record.characteristic_id4%TYPE;
   v_characteristic_id5         stage_product_import_record.characteristic_id5%TYPE;
   v_characteristic_name1       stage_product_import_record.characteristic_name1%TYPE;
   v_rt_characteristic_name1    stage_product_import_record.characteristic_name1%TYPE;
   v_characteristic_name2       stage_product_import_record.characteristic_name2%TYPE;
   v_rt_characteristic_name2    stage_product_import_record.characteristic_name2%TYPE;
   v_characteristic_name3       stage_product_import_record.characteristic_name3%TYPE;
   v_rt_characteristic_name3    stage_product_import_record.characteristic_name3%TYPE;
   v_characteristic_name4       stage_product_import_record.characteristic_name4%TYPE;
   v_rt_characteristic_name4    stage_product_import_record.characteristic_name4%TYPE;
   v_characteristic_name5       stage_product_import_record.characteristic_name5%TYPE;
   v_rt_characteristic_name5    stage_product_import_record.characteristic_name5%TYPE;

   v_created_by                 stage_product_import_record.created_by%TYPE := 0;
   v_date_created               stage_product_import_record.date_created%TYPE;

   v_parent_node_cnt            NUMBER (12) := 0;

   -- EXECUTION LOG VARIABLES
   v_process_name               execution_log.process_name%type  := 'PRC_STAGE_PRODUCT_LOAD' ;
   v_release_level              execution_log.release_level%type := '1';
   v_execution_log_msg          execution_log.text_line%TYPE;
  
   -- COUNTS
   v_recs_in_file_cnt           INTEGER := 0;
   v_field_cnt                  INTEGER := 0;
   v_recs_loaded_cnt            INTEGER := 0;
   v_error_count                PLS_INTEGER := 0;
   v_error_tbl_count            PLS_INTEGER := 0;

   -- EXCEPTIONS
   exit_program_exception       EXCEPTION;


BEGIN

-- remove file path from file name if present --
  /*SELECT substr(p_file_name ,instr(p_file_name,'/',-1,1) + 1)
  INTO v_file_name
  FROM DUAL;
  */
   v_stage := 'Write start to execution_log table';
   p_out_returncode := 0;
   prc_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure Started for file '
                           ||p_file_name,null);
   COMMIT;

   --Changes to replace UC4, added on 01/19/2009
   v_stage := 'Get Directory path and File name from p_file_name'; 
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
                   SUBSTR (v_file_name, 1, INSTR(v_file_name, '.') - 1)||'_'||v_import_file_id,
                   'prod',
                    0,
                    0,
                   'stg_in_process',
                   SYSDATE,
                   v_created_by,
                   1
                  );
      COMMIT;
   EXCEPTION
     WHEN OTHERS THEN
       prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                               'Stage: '||v_stage||' -- '||SQLERRM,null);
   END;
   
   --Changes to replace UC4, added on 01/19/2009
   BEGIN
     v_stage := 'Get Directory name';                                 
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
      v_category_name := NULL;
      v_category_desc := NULL;
      v_subcategory_name := NULL;
      v_subcategory_desc := NULL;
      v_product_name := NULL;
      v_product_desc := NULL;
      v_sku_code := NULL;
      v_characteristic_name1 := NULL;
      v_characteristic_name2 := NULL;
      v_characteristic_name3 := NULL;
      v_characteristic_name4 := NULL;
      v_characteristic_name5 := NULL;
      v_characteristic_id1 := NULL;
      v_characteristic_id2 := NULL;
      v_characteristic_id3 := NULL;
      v_characteristic_id4 := NULL;
      v_characteristic_id5 := NULL;
      v_category_id := NULL;
      v_subcategory_id := NULL;

      BEGIN
         v_stage := 'Get record';
         UTL_FILE.get_line (v_in_file_handler, v_text);

         IF v_header_rec THEN
            v_header_rec := FALSE;
         ELSE
            v_stage := 'Validations';                               --07/16/2014 Start
            SELECT import_record_pk_sq.NEXTVAL
              INTO v_import_record_id
              FROM DUAL;

            v_stage := 'Check for required columns';
            rec_import_record_error.date_created := SYSDATE;
            rec_import_record_error.created_by := v_created_by;
            rec_import_record_error.import_record_id := v_import_record_id;
            rec_import_record_error.import_file_id := v_import_file_id; --07/16/2014 End
            
            v_recs_in_file_cnt := v_recs_in_file_cnt + 1;
            v_stage := 'Parsing record number: '||v_recs_in_file_cnt;

            v_action_type :='I';

            v_field_cnt := v_field_cnt + 1;
            v_category_name := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text,1,c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_category_desc := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text,2,c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_subcategory_name := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text,3,c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_subcategory_desc := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text,4,c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_product_name := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text,5,c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_product_desc := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text,6,c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_sku_code := REPLACE(LTRIM(RTRIM(fnc_pipe_parse(v_text,7,c_delimiter))),CHR(13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name1 :=
               REPLACE (LTRIM (RTRIM (fnc_pipe_parse (v_text, 8, c_delimiter))),CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name2 :=
               REPLACE (LTRIM (RTRIM (fnc_pipe_parse (v_text, 9, c_delimiter))),CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name3 :=
               REPLACE (LTRIM (RTRIM (fnc_pipe_parse (v_text, 10, c_delimiter))),CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name4 :=
               REPLACE (LTRIM (RTRIM (fnc_pipe_parse (v_text, 11, c_delimiter))),CHR (13));

            v_field_cnt := v_field_cnt + 1;
            v_characteristic_name5 :=
               REPLACE (LTRIM (RTRIM (fnc_pipe_parse (v_text, 12, c_delimiter))),CHR (13));


            /*v_stage := 'Validations'; --07/16/2014 Commented
            SELECT import_record_pk_sq.NEXTVAL
              INTO v_import_record_id
              FROM DUAL;


            v_stage := 'Check for required columns';
            rec_import_record_error.date_created := SYSDATE;
            rec_import_record_error.created_by := v_created_by;
            rec_import_record_error.import_record_id := v_import_record_id;
            rec_import_record_error.import_file_id := v_import_file_id;*/ 

            v_stage := 'Validations';
            IF v_category_name IS NULL THEN
              rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
              rec_import_record_error.param1 := 'Category name';
              rec_import_record_error.param2 := v_category_name;
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error (rec_import_record_error);
            END IF;  -- v_category_name
            
            IF v_category_desc IS NULL THEN
              rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
              rec_import_record_error.param1 := 'Category desc';
              rec_import_record_error.param2 := v_category_desc;
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error (rec_import_record_error);
            END IF; -- v_category_desc
            
            IF v_product_name IS NULL THEN
              rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
              rec_import_record_error.param1 := 'Product name';
              rec_import_record_error.param2 := v_product_name;
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error (rec_import_record_error);
            END IF; -- v_product_name
            
            IF v_product_desc IS NULL THEN
              rec_import_record_error.item_key := 'admin.fileload.errors.MISSING_PROPERTY';
              rec_import_record_error.param1 := 'Product desc';
              rec_import_record_error.param2 := v_product_desc;
              rec_import_record_error.created_by := v_created_by;
              prc_insert_import_record_error (rec_import_record_error);
            END IF; -- v_product_desc
            

            v_stage := 'Check relational keys';
            BEGIN
              IF v_category_name IS NOT NULL THEN
                v_category_id := fnc_getcategory_id(v_category_name);
/*
                IF v_category_id IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_CATEGORY';
                     rec_import_record_error.param1 := 'Category name';
                     rec_import_record_error.param2 := v_category_name;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  END IF;
*/
              END IF; -- v_category_name is not null
              IF v_subcategory_name IS NOT NULL THEN
                v_subcategory_id := fnc_getsubcategory_id(v_subcategory_name,v_category_name); --03/25/2014 Bug # 52297
/*
                IF v_subcategory_id IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_SUBCATEGORY';
                     rec_import_record_error.param1 := 'SubCategory name';
                     rec_import_record_error.param2 := v_subcategory_name;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  END IF;
*/
              END IF; -- v_subcategory_name is not null
              

               -- characteristicID
               IF v_characteristic_name1 IS NOT NULL THEN
                  v_rt_characteristic_name1 :=
                            fnc_getcharacteristic_id (v_characteristic_name1,null,'PRODUCT');

                  IF v_rt_characteristic_name1 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name1';
                     rec_import_record_error.param2 := v_characteristic_name1;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name1 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';
                     rec_import_record_error.param1 := 'Characteristic Name1 - '||v_characteristic_name1;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);   
                  ELSIF v_rt_characteristic_name1 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name1';
                     rec_import_record_error.param2 := v_characteristic_name1;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id1 := v_rt_characteristic_name1;
                  END IF; -- v_rt_characteristic_name1 is null
               ELSE
                  v_characteristic_id1 := v_characteristic_name1;
               END IF; -- v_characteristic_name1 is not null

               IF v_characteristic_name2 IS NOT NULL THEN
                  v_rt_characteristic_name2 :=
                            fnc_getcharacteristic_id (v_characteristic_name2,null,'PRODUCT');

                  IF v_rt_characteristic_name2 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name2';
                     rec_import_record_error.param2 := v_characteristic_name2;
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
                     rec_import_record_error.param2 := v_characteristic_name2;
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
                            fnc_getcharacteristic_id (v_characteristic_name3,null,'PRODUCT');

                  IF v_rt_characteristic_name3 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name3';
                     rec_import_record_error.param2 := v_characteristic_name3;
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
                     rec_import_record_error.param2 := v_characteristic_name3;
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
                            fnc_getcharacteristic_id (v_characteristic_name4,null,'PRODUCT');

                  IF v_rt_characteristic_name4 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name4';
                     rec_import_record_error.param2 := v_characteristic_name4;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSIF v_rt_characteristic_name4 = 'N' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.CHARACTERISTIC_NOT_VALID';
                     rec_import_record_error.param1 := 'Characteristic Name4 - '||v_characteristic_name4;                  
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);   
                  ELSIF v_rt_characteristic_name4 = 'X' THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.OTHER_ERRORS';
                     rec_import_record_error.param1 := 'Characteristic Name4';
                     rec_import_record_error.param2 := v_characteristic_name4;
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
                            fnc_getcharacteristic_id (v_characteristic_name5,null,'PRODUCT');

                  IF v_rt_characteristic_name5 IS NULL THEN
                     rec_import_record_error.item_key := 'admin.fileload.errors.INVALID_PROPERTY';
                     rec_import_record_error.param1 := 'Characteristic Name5';
                     rec_import_record_error.param2 := v_characteristic_name5;
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
                     rec_import_record_error.param2 := v_characteristic_name5;
                     rec_import_record_error.param3 := SQLCODE ;
                     rec_import_record_error.created_by := v_created_by;
                     prc_insert_import_record_error (rec_import_record_error);
                  ELSE
                     v_characteristic_id5 := v_rt_characteristic_name5;
                  END IF; -- v_rt_characteristic_name5 is null
               ELSE
                  v_characteristic_id5 := v_characteristic_name5;
               END IF; -- v_characteristic_name5 is not null
            END; -- Check relational keys


            IF LTRIM (RTRIM (v_action_type)) IS NULL THEN
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
            END IF; -- v_action_type is null


            v_stage := 'Initialize the record type';
            rec_stg_product.import_record_id := v_import_record_id;
            rec_stg_product.import_file_id := v_import_file_id;
            rec_stg_product.action_type := v_action_type;
            rec_stg_product.category_id := v_category_id;
            rec_stg_product.category_name := v_category_name;
            rec_stg_product.category_desc := v_category_desc;
            rec_stg_product.subcategory_id := v_subcategory_id;
            rec_stg_product.subcategory_name := v_subcategory_name;
            rec_stg_product.subcategory_desc := v_subcategory_desc;
            rec_stg_product.product_id := NULL; -- product_id will be null as we are doing only Inserts
            rec_stg_product.product_name := v_product_name;
            rec_stg_product.product_desc := v_product_desc;
            rec_stg_product.sku_code := v_sku_code;
            rec_stg_product.characteristic_id1 := v_characteristic_id1;
            rec_stg_product.characteristic_id2 := v_characteristic_id2;
            rec_stg_product.characteristic_id3 := v_characteristic_id3;
            rec_stg_product.characteristic_id4 := v_characteristic_id4;
            rec_stg_product.characteristic_id5 := v_characteristic_id5;
            rec_stg_product.characteristic_name1 := v_characteristic_name1;
            rec_stg_product.characteristic_name2 := v_characteristic_name2;
            rec_stg_product.characteristic_name3 := v_characteristic_name3;
            rec_stg_product.characteristic_name4 := v_characteristic_name4;
            rec_stg_product.characteristic_name5 := v_characteristic_name5;
            rec_stg_product.created_by := v_created_by;
            rec_stg_product.date_created := SYSDATE;


            --Insert into stage_product_import_record
            v_stage := 'Insert into stage_product_import_record table';
            prc_insert_stg_product (rec_stg_product, v_return_code);
            IF v_return_code = 99 THEN
              v_error_count := v_error_count + 1;  -- need to count any insert errors
            ELSE
              v_count := v_count + 1;
              v_recs_loaded_cnt := v_recs_loaded_cnt + 1;
            END IF;

            IF MOD (v_count, 10) = 0 THEN
               COMMIT;
            END IF;
         END IF; -- v_header_rec
      EXCEPTION
         WHEN NO_DATA_FOUND THEN
            -- end of file
            --Program continues on to compare rec cnt.  Then final
            -- file_load_audit entry is done.

            UTL_FILE.fclose (v_in_file_handler);
            EXIT;
         WHEN OTHERS THEN
           IF v_stage LIKE 'Parsing%' THEN
              rec_import_record_error.item_key := 'admin.fileload.errors.PARSING_ERROR'; --07/16/2014 Start
              rec_import_record_error.param1   := v_recs_in_file_cnt;
              rec_import_record_error.param2   := v_field_cnt;
              rec_import_record_error.created_by   := v_created_by;
              prc_insert_import_record_error(rec_import_record_error);
           ELSE                                                                    --07/16/2014 End
            v_stage := v_stage||' field number: '||v_field_cnt;
            prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                                    'Stage: '||v_stage||' --> '||SQLERRM,null);          
            v_error_count := v_error_count + 1;  -- need to count any parsing errors --07/16/2014 
           END IF;
            
           COMMIT;
           
           --v_error_count := v_error_count + 1;  -- need to count any parsing errors --07/16/2014 

      END; -- Get record
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
END prc_stage_product_load;
/