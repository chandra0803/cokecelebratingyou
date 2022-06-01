CREATE OR REPLACE PROCEDURE prc_get_user_autocomp_by_email
(p_in_email_address IN VARCHAR2,
 p_in_search_string IN VARCHAR2,
 p_out_return_code OUT NUMBER,
 p_out_result_set OUT SYS_REFCURSOR)
 
 IS
 /*******************************************************************************
   -- Purpose: To provide contact information of users for forgot username process...Used for auto complete...
   Serach with in the subset of results by input email address
   --
   -- Person                   Date                  Comments
   -- -----------               --------          -----------------------------------------------------
   -- Ravi Dhanekula           10/09/2017   Initial version   
   *******************************************************************************/
 
 v_process_name          execution_log.process_name%type  := 'prc_get_user_autocomp_by_email' ;
 v_release_level         execution_log.release_level%type := '1';
 v_in_phone  VARCHAR2(50);
 v_in_email  VARCHAR2(50);
 
 BEGIN
 
 v_in_phone := REGEXP_REPLACE(p_in_search_string, '[^a-zA-Z0-9]+', '')||'%';
 v_in_email := REPLACE(lower(p_in_search_string),'_','!_')||'%';
 
 
 prc_execution_log_entry(v_process_name,v_release_level,'INFO','p_in_search_string :'||p_in_search_string||' , '||'v_in_email : '||v_in_email,null); 
 
 OPEN p_out_result_set FOR
 SELECT contact_id,contact_type,contact_value,user_id,is_contact_unique,COUNT(ROWNUM) OVER() AS total_records FROM (
SELECT contact_id,contact_type,contact_value,user_id,is_contact_unique,RANK() OVER ( PARTITION BY contact_type,contact_value
                                                   ORDER BY ROWNUM ASC
                                                 ) AS rec_rank FROM (
--Email contacts from email address/user_id
SELECT uea2.email_address_id contact_id,'email' contact_type,uea2.email_addr contact_value,uea.user_id,1 is_contact_unique FROM
 user_email_address uea,user_email_address uea2 WHERE lower(uea.email_addr) = lower(NVL(p_in_email_address,uea.email_addr))
 AND uea.user_id = uea2.user_id        --Added uea2 to find all the contacts of the users that has p_in_email_address as one of their contact 
 AND lower(uea2.email_addr) LIKE v_in_email ESCAPE '!'   --Search for p_in_search_string with in the results of the contacts resulted by p_in_email_address
 AND NOT EXISTS (SELECT 1 FROM user_email_address ue where lower(ue.email_addr)  = lower(uea2.email_addr) AND ue.user_id <> uea2.user_id)
 UNION ALL
 SELECT uea2.email_address_id contact_id,'email' contact_type,uea2.email_addr contact_value,uea.user_id,0 is_contact_unique FROM
 user_email_address uea,user_email_address uea2 WHERE lower(uea.email_addr) = lower(NVL(p_in_email_address,uea.email_addr))
 AND uea.user_id = uea2.user_id        --Added uea2 to find all the contacts of the users that has p_in_email_address as one of their contact 
 AND lower(uea2.email_addr) LIKE v_in_email ESCAPE '!'   --Search for p_in_search_string with in the results of the contacts resulted by p_in_email_address--Added ESCAPE to treat '_' as a string instead of a wildcard
 AND EXISTS (SELECT 1 FROM user_email_address ue where lower(ue.email_addr)  = lower(uea2.email_addr) AND ue.user_id <> uea2.user_id)
 UNION ALL
 --Phone contacts from email address
 SELECT user_phone_id contact_id,'phone' contact_type,phone_nbr contact_value,up.user_id,1 is_contact_unique FROM
 user_phone up,user_email_address uea,country c WHERE lower(uea.email_addr) = lower(NVL(p_in_email_address,uea.email_addr))
  AND p_in_email_address IS NOT NULL
  AND uea.user_id = up.user_id
  AND regexp_replace(up.phone_nbr,'[^0-9]','') LIKE v_in_phone
  AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec')
  AND c.is_sms_capable = 1
  AND NOT EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(up2.phone_nbr,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','') AND up2.user_id <> up.user_id)
  UNION ALL
  SELECT user_phone_id contact_id,'phone' contact_type,phone_nbr contact_value,up.user_id,0 is_contact_unique FROM
 user_phone up,user_email_address uea,country c WHERE lower(uea.email_addr) = lower(NVL(p_in_email_address,uea.email_addr))
  AND p_in_email_address IS NOT NULL
  AND uea.user_id = up.user_id
  AND regexp_replace(up.phone_nbr,'[^0-9]','') LIKE v_in_phone
  AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec')
  AND c.is_sms_capable = 1
  AND EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(up2.phone_nbr,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','') AND up2.user_id <> up.user_id)))
  WHERE rec_rank = 1;

 p_out_return_code := 0;
 EXCEPTION WHEN OTHERS THEN
 
 p_out_return_code := 99;
 prc_execution_log_entry(v_process_name,v_release_level,'INFO',SQLERRM,null); 
 
 END;
/
