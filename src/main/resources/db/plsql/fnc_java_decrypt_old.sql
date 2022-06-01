CREATE OR REPLACE 
FUNCTION fnc_java_decrypt_old  (pi_string VARCHAR2) return VARCHAR2 is
    v_decrypted_str         VARCHAR2(4000);
    v_pi_string_new         raw(2000) ;
begin  
  if substr(pi_string,1,6) = '{3DES}' then
      v_pi_string_new := hextoraw(substr(pi_string,7)) ;
  else    
      v_pi_string_new := hextoraw(pi_string) ; 
  end if ; 
  v_decrypted_str := fnc_decrypt(v_pi_string_new,'AAPX_PASSWORDKEY') ; 
  return v_decrypted_str ; 
end  ;
/

