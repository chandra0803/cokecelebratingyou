CREATE OR REPLACE 
FUNCTION fnc_java_encrypt_old  (pi_string varchar2) return varchar2 is
    v_encrypted_raw         RAW(128);
    v_pi_string_encrypt     varchar2(2000) ;
begin  
   if substr(pi_string,1,6) = '{3DES}' then
       v_pi_string_encrypt := pi_string ;
       return v_pi_string_encrypt ;
   else   
       v_encrypted_raw := fnc_encrypt(pi_string,'AAPX_PASSWORDKEY') ;
       return '{3DES}'||rawtohex(v_encrypted_raw) ; 
   end if ; 
end  ;
/

