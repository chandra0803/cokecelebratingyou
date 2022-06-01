CREATE OR REPLACE FUNCTION fnc_get_instance
  RETURN VARCHAR2 IS
  
/*------------------------------------------------------------------------------
-- Purpose: get DB instance - DEV, QA, PRE or PRD
--
-- Called by: 
--
--------------------------------------------------------------------------------
-- Gorantla    03/14/2019  initial
--
------------------------------------------------------------------------------*/

  v_return        VARCHAR2(200);
  v_global_name   VARCHAR2(200);

BEGIN 

  SELECT  GLOBAL_NAME
  INTO    v_global_name
  FROM    GLOBAL_NAME;
  
  IF v_global_name LIKE '%PPRD%' OR v_global_name LIKE '%PRE%' THEN
    v_return := 'preprod';
  ELSIF v_global_name LIKE '%QA%' THEN
    v_return := 'qa';
  ELSE
    v_return := 'prod';
  END IF;

  RETURN v_return;
    
EXCEPTION
  WHEN OTHERS THEN
    RETURN 'dev';
END;  -- FUNCTION fnc_get_instance
/