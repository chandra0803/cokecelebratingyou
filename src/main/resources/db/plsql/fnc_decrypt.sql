CREATE OR REPLACE 
FUNCTION fnc_decrypt(p_in_raw IN RAW, p_key_string IN VARCHAR2) RETURN VARCHAR2  IS   
   v_decrypted_str  VARCHAR2(4000); 
   v_strength PLS_INTEGER := 0; 
BEGIN 
   v_decrypted_str := fnc_3des_decrypt(p_in_raw, p_key_string,v_strength); 
   v_decrypted_str := ltrim(v_decrypted_str, '|'); 
   RETURN v_decrypted_str; 
EXCEPTION 
  WHEN Others Then 
     RAISE_APPLICATION_ERROR (-20012,'Error in Decrypting Value : ' || rawtohex (p_in_raw) || ' - ' || SQLERRM); 
END;
/

