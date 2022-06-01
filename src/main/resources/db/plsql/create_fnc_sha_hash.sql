CREATE OR REPLACE FUNCTION fnc_sha_hash (p_password varchar2)
return VARCHAR is
 shakey  raw(256); 
  v_boolean number(1); 
   p_val raw(2000);  
  
BEGIN

SELECT BOOLEAN_VAL INTO v_boolean FROM OS_PROPERTYSET where entity_name='password.should.use.regex';

IF v_boolean =1 THEN
   p_val := utl_raw.cast_to_raw( p_password ) ;
ELSE 
   p_val := utl_raw.cast_to_raw( UPPER(p_password) );
                    END IF;

 if substr(p_password,1,4) != '{V2}' then
   shakey := dbms_crypto.hash(p_val,4);
   return '{V2}'||rawtohex(shakey) ;
 else
   return p_password ;
 end if ;      
END  ;
/
