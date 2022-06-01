declare

 v_variable    VARCHAR2(4000 CHAR);
 v_recog_ind   NUMBER(1);

 cursor c_enc IS
 SELECT * from ENCRYPTION_CONVERSION
 WHERE NEEDS_ENCRYPTION = 'Y';

begin
   BEGIN
      SELECT boolean_val  
        INTO v_recog_ind
        FROM os_propertyset
       WHERE upper(entity_name)  = upper('install.recognition')
         AND upper(entity_key) = upper('Recognition Installed');
   EXCEPTION
      WHEN OTHERS THEN   
      v_recog_ind := 0;
   END;              
   FOR r_enc IN c_enc LOOP
      IF (TRIM(UPPER(r_enc.TABLE_NAME)) = 'RPT_AWARD_EARNING') AND (v_recog_ind = 0) THEN
         NULL;
      ELSE
         v_variable := 'UPDATE '||r_enc.TABLE_NAME||' SET '||r_enc.COLUMN_NAME||' = fnc_java_encrypt(fnc_java_decrypt_old('||r_enc.COLUMN_NAME||')) WHERE '||r_enc.COLUMN_NAME||' LIKE ''{3DES}%''';
         --dbms_output.put_line(v_variable);
         execute immediate v_variable;
         UPDATE ENCRYPTION_CONVERSION
            SET NEEDS_ENCRYPTION = 'N'
          WHERE TABLE_NAME = r_enc.TABLE_NAME
            AND COLUMN_NAME = r_enc.COLUMN_NAME;
      END IF;            
   END LOOP;
end;
/