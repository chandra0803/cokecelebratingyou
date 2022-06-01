CREATE OR REPLACE 
FUNCTION fnc_data_has_delimiter (p_in_string  VARCHAR2 ,
                                 p_in_delimiter VARCHAR2 )
RETURN VARCHAR2 IS
/*******************************************************************************
-- Purpose: Checks the string being passed in for the delimiter. If the string 
--          has delimiter then encloses the value in double quotes.
-- MODIFICATION HISTORY
-- Person      Date    Comments
-- ---------   ------  -------------------------------------------
-- Raju N.    01/15/06  Creation
*******************************************************************************/
   v_string varchar2(700) := p_in_string ;
BEGIN
 IF INSTR (p_in_string,p_in_delimiter ) > 0 THEN
   v_string := '"'||p_in_string||'"' ;
 END IF ;
  RETURN v_string ;
EXCEPTION
   WHEN OTHERS THEN
     v_string := p_in_string ;
     RETURN v_string ;
END  ;
/
