CREATE OR REPLACE FUNCTION fnc_java_md5_hash (p_password varchar2)
return VARCHAR is
 md5key  raw(16); 
  v_boolean number(1); 
   p_val raw(2000);  
  
BEGIN

SELECT BOOLEAN_VAL INTO v_boolean FROM OS_PROPERTYSET where entity_name='password.should.use.regex';

IF v_boolean =1 THEN
   p_val := utl_raw.cast_to_raw( p_password ) ;
ELSE 
   p_val := utl_raw.cast_to_raw( UPPER(p_password) );
                    END IF;

 if substr(p_password,1,5) != '{MD5}' then
   md5key := DBMS_OBFUSCATION_TOOLKIT.MD5(input => p_val);
   return '{MD5}'||rawtohex(md5key) ;
 else
   return p_password ;
 end if ;      
END  ;
/

