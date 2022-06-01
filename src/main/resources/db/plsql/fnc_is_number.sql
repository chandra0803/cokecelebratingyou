CREATE OR REPLACE FUNCTION fnc_is_number
( i_number_text   IN VARCHAR2,
  i_int_chk       IN INTEGER  DEFAULT 0
) RETURN INTEGER
IS
/*------------------------------------------------------------------------------
Purpose: Returns a value (0/1) indicating whether the input numeric string is a valid number.
         Note: A null value passes numeric validation.

         The i_int_chk input parameter indicates whether the function should validate the input text is also an integer.
--------------------------------------------------------------------------------
J.Flees     11/08/2011  Initial version.

------------------------------------------------------------------------------*/
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('fnc_is_number');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;

   v_number             NUMBER;
   v_is_number          INTEGER := 1;

BEGIN
   -- convert string to a number
   v_number := TO_NUMBER(i_number_text);

   IF (   (i_int_chk = 1)
      AND (MOD(v_number, 1) != 0)
      ) THEN
      -- not an integer
      v_is_number := 0;
   END IF;

   RETURN v_is_number;

EXCEPTION   
   WHEN OTHERS THEN
      -- conversion failure, value is not numeric
      RETURN 0;

END fnc_is_number;
/