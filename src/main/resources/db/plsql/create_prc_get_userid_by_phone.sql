CREATE OR REPLACE PROCEDURE prc_get_userid_by_phone
(p_in_phone_number IN VARCHAR2,
 p_out_return_code OUT NUMBER,
 p_out_result_set OUT SYS_REFCURSOR)
 
 IS
 /*******************************************************************************
   -- Purpose: To provide list of user IDs matching the phone number passed in.
   --
   -- Person                   Date                  Comments
   -- -----------               --------          -----------------------------------------------------
   -- Ravi Dhanekula           09/22/2017   Initial version
   *******************************************************************************/
 
 v_process_name          execution_log.process_name%type  := 'prc_get_userid_by_phone' ;
 v_release_level         execution_log.release_level%type := '1';
 
 BEGIN
 
 OPEN p_out_result_set FOR 
 SELECT DISTINCT user_id FROM (
 SELECT user_id FROM user_phone WHERE regexp_replace(phone_nbr,'[^0-9]','') = regexp_replace(p_in_phone_number,'[^0-9]','')
 UNION ALL
 SELECT up.user_id FROM user_phone up,country c
 WHERE up.country_phone_code = c.country_code
 AND regexp_replace(c.phone_country_code||up.phone_nbr,'[^0-9]','') = regexp_replace(p_in_phone_number,'[^0-9]',''));
 p_out_return_code := 0;
 EXCEPTION WHEN OTHERS THEN
 
 p_out_return_code := 99;
 prc_execution_log_entry(v_process_name,v_release_level,'INFO',SQLERRM,null); 
 
 END;
/
