CREATE OR REPLACE FUNCTION FNC_GET_SYSTEM_VAR_BOOLEAN
(p_entity_name  IN VARCHAR2,
 p_entity_key   IN VARCHAR2)
RETURN NUMBER IS
/****************************************************************
--  Purpose: Get the value for the variables that could vary by the client 
--           installation.
--  
--  Person                  Date                 Comments
--  ---------              ---------           -------------------------------------------
--  Ravi Dhanekula     04/23/2013    Initial
*****************************************************************/

--Procedure variables
v_value_num                os_propertyset.boolean_val%type;

BEGIN

  --Retrieve value_text from system_variable table for passed var_name
  SELECT boolean_val
    INTO v_value_num
    FROM os_propertyset
   WHERE upper(entity_name)  = upper(p_entity_name)
     and upper(entity_key) = upper(p_entity_key);


  RETURN v_value_num;

EXCEPTION
  WHEN others THEN
    --RETURN error code for failed retrieval of system_variable
    v_value_num := '99';
    RETURN v_value_num;

END; -- Function FNC_GET_SYSTEM_VAR_BOOLEAN
/