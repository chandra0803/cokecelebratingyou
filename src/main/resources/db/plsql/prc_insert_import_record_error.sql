CREATE OR REPLACE 
PROCEDURE prc_insert_import_record_error(p_import_record import_record_error%rowtype) is
  v_import_record_error_id import_record_error.import_record_error_id%type;
begin
  SELECT IMPORT_record_error_pk_SQ.NEXTVAL
    INTO v_import_record_error_id
    FROM dual;
  INSERT INTO import_record_error a
    (import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created)
  VALUES
    (v_import_record_error_id,
     p_import_record.import_file_id,
     p_import_record.import_record_id,
     p_import_record.item_key,
     p_import_record.param1,
     p_import_record.param2,
     p_import_record.param3,
     p_import_record.param4,
     p_import_record.created_by,
     SYSDATE);
exception
   when others then
    prc_execution_log_entry('prc_insert_import_record_error',
        '1',
        'ERROR',
        'Import_Record_Error: '||v_import_record_error_id||'. '||SQLERRM,
         null);
    COMMIT;
end prc_insert_import_record_error;
/

