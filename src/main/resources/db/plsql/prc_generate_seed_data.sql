CREATE OR REPLACE PROCEDURE prc_generate_seed_data(p_new_replace VARCHAR2, p_system_variable_ready VARCHAR2 DEFAULT NULL) IS

   v_in_file_handler            UTL_FILE.file_type;
   v_path_name                  VARCHAR2(255);
   v_file_name                  VARCHAR2(255);
   v_text                       VARCHAR2 (1000);
   v_count                      NUMBER(10) := 1;
   v_user                       varchar2(100);
   v_ext_tab_counter            NUMBER(10);
   v_variable                   VARCHAR2(4000 CHAR);

   cursor         c_enc IS
   SELECT * from ENCRYPTION_CONVERSION
   WHERE NEEDS_ENCRYPTION = 'Y';

BEGIN

   IF p_new_replace IN ('N', 'R') THEN
      select user
        into v_user
        from dual;

      select ext_tab_sq.NEXTVAL
        into v_ext_tab_counter
        from dual;
     
      v_path_name := v_user||'_EXTERNAL';
      v_file_name := 'external_file_'||v_ext_tab_counter||'.txt';
      v_in_file_handler := UTL_FILE.fopen (v_path_name, v_file_name, 'w');

      while v_count < 100 loop
        select v_count||'|'||UPPER(dbms_random.string('X', 99)) random_number 
          into v_text
          from dual;  
        UTL_FILE.put_line (v_in_file_handler, v_text);
        v_count:= v_count + 1;
      END LOOP;
      UTL_FILE.fclose (v_in_file_handler);
  
      execute immediate 'ALTER TABLE EXT_TAB_TEMP LOCATION ('''||v_file_name||''')';
  
      IF p_system_variable_ready IS NULL THEN
         UPDATE os_propertyset
            SET string_val = v_file_name
          WHERE entity_name = 'seeddatafile.temp';
      END IF;
   END IF; 

   pkg_set_runtime_variable.prc_set_runtime_variable;

   IF p_new_replace = 'R' THEN    

      FOR r_enc IN c_enc LOOP
         v_variable := 'UPDATE '||r_enc.TABLE_NAME||' SET '||r_enc.COLUMN_NAME||' = fnc_java_encrypt_temp(fnc_java_decrypt('||r_enc.COLUMN_NAME||')) WHERE '||r_enc.COLUMN_NAME||' IS NOT NULL';
         execute immediate v_variable;
         dbms_output.put_line(v_variable);
         UPDATE ENCRYPTION_CONVERSION
            SET NEEDS_ENCRYPTION = 'N'
          WHERE TABLE_NAME = r_enc.TABLE_NAME
            AND COLUMN_NAME = r_enc.COLUMN_NAME;
      END LOOP;

   END IF;

   IF p_system_variable_ready IS NULL THEN
      SELECT string_val 
        INTO v_file_name
        FROM os_propertyset
       WHERE entity_name = 'seeddatafile.temp';

      UPDATE os_propertyset
         SET string_val = v_file_name
       WHERE entity_name = 'seeddatafile.orig';
   END IF;
   execute immediate 'ALTER TABLE EXT_TAB LOCATION ('''||v_file_name||''')';  
EXCEPTION
   WHEN OTHERS THEN
    prc_execution_log_entry('prc_generate_seed_data',1,'ERROR', SQLERRM,null);
    ROLLBACK;
END;
/
