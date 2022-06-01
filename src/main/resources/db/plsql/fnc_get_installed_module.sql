CREATE OR REPLACE 
FUNCTION fnc_get_installed_module
  ( p_in_module IN VARCHAR2)
  RETURN NUMBER IS

-- Purpose: Returns 1 if module is installed, else 0
--
-- MODIFICATION HISTORY
-- Person      Date    Comments
-- ---------   ------  -------------------------------------------       
-- Percy M.   03/07/06 Initial Creation

v_return_value         NUMBER := NULL;

BEGIN 

    SELECT boolean_val
           INTO v_return_value
    FROM os_propertyset
    WHERE lower(entity_name) = lower(p_in_module);

    RETURN v_return_value ;
    
EXCEPTION
   WHEN others THEN
       RETURN v_return_value;
END;
/
