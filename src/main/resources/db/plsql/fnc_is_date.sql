CREATE OR REPLACE FUNCTION fnc_is_date
( i_date_text     IN VARCHAR2,
  i_date_format   IN VARCHAR2    DEFAULT 'MM/DD/YYYY'
) RETURN INTEGER
IS
/*------------------------------------------------------------------------------
Purpose: Returns a value (0/1) indicating whether the input string is a valid date for the specified date format.
         Note: A null value passes date validation.

--------------------------------------------------------------------------------
J.Flees     11/08/2011  Initial version.

------------------------------------------------------------------------------*/
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('fnc_is_date');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;

   v_date               DATE;
BEGIN
   -- convert string to a date
   v_date := TO_DATE(i_date_text, i_date_format);

   RETURN 1;

EXCEPTION   
   WHEN OTHERS THEN
      -- conversion failure
      RETURN 0;

END fnc_is_date;
/