CREATE OR REPLACE 
FUNCTION fnc_get_system_variable
(p_entity_name  IN VARCHAR2,
 p_entity_key   IN VARCHAR2)
RETURN VARCHAR2 IS
/****************************************************************
--  Purpose: Get the value for the variables that could vary by the client 
--           installation.
--  
--  Person      	Date       Comments
--  ---------   ---------     -------------------------------------------
--  Raju N      09/19/2005    Creation
*****************************************************************/

--Procedure variables
v_value_text                os_propertyset.string_val%type;

BEGIN

  --Retrieve value_text from system_variable table for passed var_name
  SELECT string_val
    INTO v_value_text

    FROM os_propertyset
   WHERE upper(entity_name)  = upper(p_entity_name)
     and upper(entity_key) = upper(p_entity_key);

  --RETURN value_text for var_name of system_variable entered
  RETURN v_value_text;

EXCEPTION
  WHEN others THEN
    --RETURN error code for failed retrieval of system_variable
    v_value_text := 'ERROR';
    RETURN v_value_text;

END; -- Function FNC_GET_SYSTEM_VARIABLE
/

