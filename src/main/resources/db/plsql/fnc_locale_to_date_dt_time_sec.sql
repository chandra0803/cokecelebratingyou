CREATE OR REPLACE FUNCTION fnc_locale_to_date_dt_time_sec (p_in_date IN VARCHAR2, p_in_locale IN VARCHAR2)
  RETURN DATE IS

/*******************************************************************************

-- Purpose: Function that returns date,time and second with 
            loacle specific format for a input varchar and locale 
            (locale example: 'en_US' or 'en_GB' or 'de_DE' or 'fr_CA' etc)

-- Modification history

-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- Arun S      08/23/2011  Initial Creation
  
*******************************************************************************/

  v_to_date_dt_time_sec  DATE;

BEGIN

  v_to_date_dt_time_sec := TO_DATE(p_in_date,fnc_java_get_dt_time_sec_ptrn(p_in_locale));
  RETURN v_to_date_dt_time_sec;

EXCEPTION
  WHEN OTHERS THEN
    v_to_date_dt_time_sec := NULL;
    RETURN v_to_date_dt_time_sec;  
END;
/