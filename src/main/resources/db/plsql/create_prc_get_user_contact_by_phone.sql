CREATE OR REPLACE PROCEDURE prc_get_user_contact_by_phone
(p_in_phone_number IN VARCHAR2,
 p_in_unique_contact IN NUMBER,
 p_out_return_code OUT NUMBER,
 p_out_result_set OUT SYS_REFCURSOR)
 
 IS
 /*******************************************************************************
   -- Purpose: To provide contact information of users for forgot username process...by input phone number.
   --
   -- Person                   Date                  Comments
   -- -----------               --------          -----------------------------------------------------
   -- Ravi Dhanekula           09/26/2017   Initial version   
   *******************************************************************************/
 
 v_process_name          execution_log.process_name%type  := 'prc_get_user_contact_by_phone' ;
 v_release_level         execution_log.release_level%type := '1';
 
 BEGIN
 
 IF p_in_unique_contact = 1 THEN
 OPEN p_out_result_set FOR 
SELECT contact_id,contact_type,contact_value,user_id,1 is_contact_unique,COUNT(ROWNUM) OVER() AS total_records FROM (
SELECT user_phone_id contact_id,'phone' contact_type,up.phone_nbr contact_value,up.user_id FROM
 user_phone up,country c WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','')
 AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec')
 AND c.is_sms_capable = 1
 AND (NOT EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up2.phone_nbr,'[^0-9]','') AND up.user_id <> up2.user_id))
 UNION
 SELECT user_phone_id contact_id,'phone' contact_type,up.phone_nbr contact_value,up.user_id FROM
 user_phone up,country c WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(phone_country_code||up.phone_nbr,'[^0-9]','')
 AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec')
 AND c.is_sms_capable = 1
 AND (NOT EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up2.phone_nbr,'[^0-9]','') AND up.user_id <> up2.user_id))
 UNION
 SELECT email_address_id contact_id,'email' contact_type,uea.email_addr contact_value,uea.user_id FROM
 user_phone up,user_email_address uea WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','')
  AND uea.user_id = up.user_id
  AND (NOT EXISTS (SELECT 1 FROM user_email_address ue where lower(ue.email_addr)  = lower(uea.email_addr) AND ue.user_id <> uea.user_id))
  UNION
  SELECT email_address_id contact_id,'email' contact_type,uea.email_addr contact_value,uea.user_id FROM
 user_phone up,user_email_address uea,country c WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(c.phone_country_code||up.phone_nbr,'[^0-9]','')
  AND uea.user_id = up.user_id
  AND up.country_phone_code = c.country_code
  AND (NOT EXISTS (SELECT 1 FROM user_email_address ue where lower(ue.email_addr)  = lower(uea.email_addr) AND ue.user_id <> uea.user_id))
  
  );
 
ELSE

OPEN p_out_result_set FOR
SELECT contact_id,contact_type,contact_value,user_id,is_contact_unique,COUNT(ROWNUM) OVER() AS total_records,input_contact FROM (
SELECT contact_id,contact_type,contact_value,user_id,is_contact_unique,input_contact,RANK() OVER ( PARTITION BY contact_type,contact_value
                                                   ORDER BY ROWNUM ASC
                                                 ) AS rec_rank FROM (
SELECT up3.user_phone_id contact_id,'phone' contact_type,up3.phone_nbr contact_value,up.user_id,1 is_contact_unique,CASE WHEN regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up3.phone_nbr,'[^0-9]','') THEN 1 ELSE 0 END input_contact FROM
 user_phone up3,user_phone up,country c WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','')
 AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec') AND up.user_id = up3.user_id
 AND up3.phone_type IN ('mob','rec')
 AND c.is_sms_capable = 1
 AND (NOT EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up2.phone_nbr,'[^0-9]','') AND up.user_id <> up2.user_id))
 UNION ALL
 SELECT up3.user_phone_id contact_id,'phone' contact_type,up3.phone_nbr contact_value,up.user_id,1 is_contact_unique,CASE WHEN regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(phone_country_code||up3.phone_nbr,'[^0-9]','') THEN 1 ELSE 0 END input_contact FROM
 user_phone up3,user_phone up,country c WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(phone_country_code||up.phone_nbr,'[^0-9]','')
 AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec') AND up.user_id = up3.user_id
 AND up3.phone_type IN ('mob','rec')
 AND c.is_sms_capable = 1
 AND (NOT EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up2.phone_nbr,'[^0-9]','') AND up.user_id <> up2.user_id))
 UNION ALL
 SELECT email_address_id contact_id,'email' contact_type,uea.email_addr contact_value,uea.user_id,1 is_contact_unique,0 input_contact FROM
 user_phone up,user_email_address uea WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','')
  AND uea.user_id = up.user_id
  AND (NOT EXISTS (SELECT 1 FROM user_email_address ue where lower(ue.email_addr)  = lower(uea.email_addr) AND ue.user_id <> uea.user_id))
  UNION ALL
  SELECT email_address_id contact_id,'email' contact_type,uea.email_addr contact_value,uea.user_id,1 is_contact_unique,0 input_contact FROM
 user_phone up,user_email_address uea,country c WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(phone_country_code||up.phone_nbr,'[^0-9]','')
  AND uea.user_id = up.user_id
  AND up.country_phone_code = c.country_code
  AND NOT EXISTS (SELECT 1 FROM user_email_address ue where lower(ue.email_addr)  = lower(uea.email_addr) AND ue.user_id <> uea.user_id)
  UNION ALL
SELECT up3.user_phone_id contact_id,'phone' contact_type,up3.phone_nbr contact_value,up.user_id,0 is_contact_unique,CASE WHEN regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up3.phone_nbr,'[^0-9]','') THEN 1 ELSE 0 END input_contact FROM
 user_phone up3,user_phone up,country c WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','')
 AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec') AND up.user_id = up3.user_id
 AND up3.phone_type IN ('mob','rec')
 AND c.is_sms_capable = 1
 AND EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up2.phone_nbr,'[^0-9]','') AND up.user_id <> up2.user_id)
 UNION ALL
 SELECT up3.user_phone_id contact_id,'phone' contact_type,up.phone_nbr contact_value,up.user_id,0 is_contact_unique,CASE WHEN regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(phone_country_code||up3.phone_nbr,'[^0-9]','') THEN 1 ELSE 0 END input_contact FROM
 user_phone up3,user_phone up,country c WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(phone_country_code||up.phone_nbr,'[^0-9]','')
 AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec') AND up.user_id = up3.user_id
 AND up3.phone_type IN ('mob','rec')
 AND c.is_sms_capable = 1
 AND EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up2.phone_nbr,'[^0-9]','') AND up.user_id <> up2.user_id)
 UNION ALL
 SELECT email_address_id contact_id,'email' contact_type,uea.email_addr contact_value,uea.user_id,0 is_contact_unique,0 input_contact FROM
 user_phone up,user_email_address uea WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','')
  AND uea.user_id = up.user_id
  AND EXISTS (SELECT 1 FROM user_email_address ue where lower(ue.email_addr)  = lower(uea.email_addr) AND ue.user_id <> uea.user_id)
  UNION ALL
  SELECT email_address_id contact_id,'email' contact_type,uea.email_addr contact_value,uea.user_id,0 is_contact_unique,0 input_contact FROM
 user_phone up,user_email_address uea,country c WHERE regexp_replace(p_in_phone_number,'[^0-9]','')  = regexp_replace(phone_country_code||up.phone_nbr,'[^0-9]','')
  AND uea.user_id = up.user_id
  AND up.country_phone_code = c.country_code
  AND  EXISTS (SELECT 1 FROM user_email_address ue where lower(ue.email_addr)  = lower(uea.email_addr) AND ue.user_id <> uea.user_id)
  )) WHERE rec_rank = 1;
 

END IF;
 p_out_return_code := 0;
 EXCEPTION WHEN OTHERS THEN
 
 p_out_return_code := 99;
 prc_execution_log_entry(v_process_name,v_release_level,'INFO',SQLERRM,null); 
 
 END;
/
