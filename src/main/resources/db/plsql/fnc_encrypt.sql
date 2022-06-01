CREATE OR REPLACE 
FUNCTION fnc_encrypt 
     (p_in_string IN VARCHAR2, 
      p_key_string IN VARCHAR2) RETURN RAW IS 
   v_raw_input RAW(128) := utl_raw.cast_to_raw(p_in_string); 
   v_encrypted_raw	RAW(128); v_string_length		
   NUMBER(10) := 0; 
   v_go						VARCHAR2(2) := 'Y'; 
   v_string					VARCHAR2(4000); 
   v_strength              PLS_INTEGER := 0; 
BEGIN 
   v_string_length := nvl(length(p_in_string), 0); 
   WHILE v_go = 'Y' LOOP 
     IF MOD(v_string_length, 8) = 0 THEN 
        v_go := 'N'; 
     ELSE v_string_length := v_string_length + 1; 
     END IF; 
   END LOOP; 
   v_string := lpad(p_in_string, v_string_length, '|'); 
   v_encrypted_raw := fnc_3des_encrypt(v_string,p_key_string,v_strength); 
   RETURN v_encrypted_raw; 
EXCEPTION 
   WHEN Others Then RAISE_APPLICATION_ERROR (-20011,'Error in Encrypting Value : ' || v_raw_input || ' - ' || SQLERRM); 
END;
/

