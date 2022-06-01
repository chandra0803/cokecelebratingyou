CREATE OR REPLACE 
FUNCTION fnc_report_chart_period_nbr
  ( p_in_start_date IN DATE,
    p_in_end_date IN DATE,
    p_in_timeframe_period IN VARCHAR2)
  RETURN  NUMBER IS
--
--
-- Purpose: This function will be called by the report trend charts to determine
--          the number of periods needed according to the start and end dates and
--          the timeframe_period report parameter (weekly, monthly, quarterly, or
--          annually).
--
-- MODIFICATION HISTORY
-- Person      Date    Comments
-- ---------   ------  -------------------------------------------       
-- D Murray    12/09/2005  Initial Creation
-- Raju N      12/14/2005  Modified to look at the calendar table to give us
--                         more flexibility to accomodate for various client 
--                         specific fiscal caledar needs.

v_period_nbrs      NUMBER;

BEGIN

  IF p_in_timeframe_period = 'weekly' THEN
   BEGIN
    SELECT COUNT(DISTINCT TO_CHAR(TIME_KEY,'RRRR')||week_ind)
      INTO v_period_nbrs
      FROM CALENDAR
     WHERE TIME_KEY BETWEEN p_in_start_date AND p_in_end_date ;
   EXCEPTION
     WHEN OTHERS THEN
      v_period_nbrs := 0 ;
   END  ;
  ELSIF p_in_timeframe_period = 'monthly' THEN
   BEGIN
    SELECT COUNT(DISTINCT TO_CHAR(TIME_KEY,'RRRR')||month_ind)
      INTO v_period_nbrs
      FROM CALENDAR
     WHERE TIME_KEY BETWEEN p_in_start_date AND p_in_end_date ;
   EXCEPTION
     WHEN OTHERS THEN
      v_period_nbrs := 0 ;
   END  ;
  ELSIF p_in_timeframe_period = 'quarterly' THEN
   BEGIN
    SELECT COUNT(DISTINCT TO_CHAR(TIME_KEY,'RRRR')||quarter_ind)
      INTO v_period_nbrs
      FROM CALENDAR
     WHERE TIME_KEY BETWEEN p_in_start_date AND p_in_end_date ;
   EXCEPTION
     WHEN OTHERS THEN
      v_period_nbrs := 0 ;
   END  ;
  ELSIF p_in_timeframe_period = 'annually' THEN
   BEGIN
    SELECT COUNT(DISTINCT TO_CHAR(TIME_KEY,'RRRR'))
      INTO v_period_nbrs
      FROM CALENDAR
     WHERE TIME_KEY BETWEEN p_in_start_date AND p_in_end_date ;
   EXCEPTION
     WHEN OTHERS THEN
      v_period_nbrs := 0 ;
   END  ;
  END IF;

    RETURN v_period_nbrs;
EXCEPTION
   WHEN others THEN
       v_period_nbrs := 0;
       RETURN v_period_nbrs;
END;
/

