CREATE OR REPLACE FUNCTION fnc_locale_to_char_dt_time (p_in_date IN DATE, p_in_locale IN VARCHAR2)
  RETURN VARCHAR2 IS

/*******************************************************************************

-- Purpose: Function that returns date and time in varchar with loacle specific format 
            for a input date and locale 
            (locale example: 'en_US' or 'en_GB' or 'de_DE' or 'fr_CA' etc)

-- Modification history

-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- Arun S      08/23/2011  Initial Creation
  
*******************************************************************************/

  v_to_char_dt_time  VARCHAR2(20);

BEGIN

  v_to_char_dt_time := TO_CHAR(p_in_date,fnc_java_get_date_time_pattern(p_in_locale));
  RETURN v_to_char_dt_time;

EXCEPTION
  WHEN OTHERS THEN
    v_to_char_dt_time := NULL;
    RETURN v_to_char_dt_time;  
END;
/