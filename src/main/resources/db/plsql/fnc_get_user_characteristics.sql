CREATE OR REPLACE 
FUNCTION FNC_GET_USER_CHARACTERISTICS
  ( p_in_user_id IN number )
  RETURN varchar2 IS
--
-- Purpose: return a comma delimited string of all characterisitc of the user.
--
-- MODIFICATION HISTORY
-- Person      Date    Comments
-- ---------   ------  -------------------------------------------       
-- Percy      11/06/06  Initial Creation

CURSOR cur_char IS
    select characteristic_value
    from user_characteristic
    where user_id = p_in_user_id;

v_char_string varchar2(2000) := null ;

BEGIN 
    FOR v_char in cur_char LOOP
        v_char_string := v_char_string || v_char.characteristic_value ||',';
    END LOOP;
    
    RETURN v_char_string ;
    
EXCEPTION
   WHEN others THEN
    RETURN v_char_string ;
END;
/