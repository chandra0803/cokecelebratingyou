CREATE OR REPLACE FUNCTION fnc_lock_control_audience(v_audience_id IN NUMBER)
 RETURN NUMBER IS
  CURSOR cur_lck IS 
   SELECT audience_id 
     FROM audience 
    WHERE audience_id = v_audience_id
      FOR UPDATE WAIT 300;
 /*--------------------------------------------------------------------------
  purpose : create a delay/lock the prc_sync_pax_criteria_audience
            process to restrict duplicate enrollment at same time.
  --  Chidamba  07/11/2014 Initial Creation
 --------------------------------------------------------------------------*/        
      r_value NUMBER;
BEGIN
      OPEN cur_lck;
     FETCH cur_lck INTO r_value;
     CLOSE cur_lck;
    RETURN 0;  --successful lock
EXCEPTION
 WHEN OTHERS THEN
   RETURN 99; 
END;
/