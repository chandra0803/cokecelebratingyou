CREATE OR REPLACE PROCEDURE prc_get_user_contact_info
(p_in_email_address IN VARCHAR2,
p_in_user_id       IN NUMBER,
p_in_unique_contact IN NUMBER,
p_out_return_code OUT NUMBER,
p_out_result_set OUT SYS_REFCURSOR)

 IS
/*******************************************************************************
   -- Purpose: To provide contact information of users for forgot username process.
   --
   -- Person                   Date                  Comments
   -- -----------               --------          -----------------------------------------------------
   -- Ravi Dhanekula           07/21/2017   Initial version
   -- Ravi Dhanekula           09/25/2017   G6-3049 Consider inactive users also.
   -- Gorantla                 11/03/2017   G6-G6-3342 Account Activation - Initiating the activation flow 
                                           with Shared email gives lesser contacts in MOC page under a particular scenario
   -- Ravi Dhanekula           11/09/2017  Removed duplicates in the result set.
   *******************************************************************************/

 v_process_name          execution_log.process_name%type  := 'prc_get_user_contact_info' ;
v_release_level         execution_log.release_level%type := '1';

 BEGIN

 IF p_in_unique_contact = 1 THEN

  OPEN p_out_result_set FOR  
  SELECT contact_id,contact_type,contact_value,user_id,1 is_contact_unique,COUNT(ROWNUM) OVER() AS total_records FROM (
SELECT contact_id,contact_type,contact_value,user_id,RANK() OVER ( PARTITION BY user_id,contact_value
                                                   ORDER BY ROWNUM ASC
                                                 ) AS rec_rank FROM (
--SELECT contact_id,contact_type,contact_value,user_id,1 is_contact_unique,COUNT(ROWNUM) OVER() AS total_records FROM (
--Email contacts from email address/user_id
SELECT uea2.email_address_id contact_id,'email' contact_type,uea2.email_addr contact_value,uea.user_id FROM
user_email_address uea,user_email_address uea2 WHERE lower(uea.email_addr) = lower(NVL(p_in_email_address,uea.email_addr))
AND uea.user_id = NVL(p_in_user_id,uea.user_id) AND uea.user_id = uea2.user_id
AND NOT EXISTS (SELECT 1 FROM user_email_address ue where lower(ue.email_addr)  = lower(uea2.email_addr) AND ue.user_id <> uea2.user_id)
UNION
--Phone contacts from email address
SELECT user_phone_id contact_id,'phone' contact_type,phone_nbr contact_value,up.user_id FROM
user_phone up,user_email_address uea,country c WHERE lower(uea.email_addr) = lower(NVL(p_in_email_address,uea.email_addr))
  AND uea.user_id = NVL(p_in_user_id,uea.user_id)
  AND p_in_email_address IS NOT NULL
  AND uea.user_id = up.user_id
  AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec')
  AND c.is_sms_capable = 1
  AND NOT EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(up2.phone_nbr,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','') AND up2.user_id <> up.user_id)--09/20/2017
  UNION
  --Phone contacts from user_id
  SELECT user_phone_id contact_id,'phone' contact_type,phone_nbr contact_value,up.user_id FROM
user_phone up,country c WHERE
  up.user_id = p_in_user_id
  AND p_in_user_id IS NOT NULL
  AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec')
  AND c.is_sms_capable = 1
  AND NOT EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(up2.phone_nbr,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','') AND up2.user_id <> up.user_id))) WHERE rec_rank = 1;--09/20/2017

ELSE --p_in_unique_contact = 0

 OPEN p_out_result_set FOR 
 SELECT contact_id,contact_type,contact_value,user_id,is_contact_unique,COUNT(ROWNUM) OVER() AS total_records,input_contact FROM (
SELECT contact_id,contact_type,contact_value,user_id,is_contact_unique,input_contact,RANK() OVER ( PARTITION BY contact_type,contact_value
                                                   ORDER BY ROWNUM ASC
                                                 ) AS rec_rank FROM (
SELECT uea2.email_address_id contact_id,'email' contact_type,uea2.email_addr contact_value,uea.user_id,1 is_contact_unique,CASE WHEN lower(uea2.email_addr) = lower(p_in_email_address) THEN 1 ELSE 0 END input_contact FROM
user_email_address uea,user_email_address uea2 WHERE lower(uea.email_addr) = lower(NVL(p_in_email_address,uea.email_addr))
AND uea.user_id = NVL(p_in_user_id,uea.user_id) AND uea.user_id = uea2.user_id
--AND lower(uea.email_addr) <> lower(uea2.email_addr)--Do not show the inut email address in the outpur result if the p_in_unique_contact=0
AND NOT EXISTS (SELECT 1 FROM user_email_address ue where lower(ue.email_addr)  = lower(uea2.email_addr) AND ue.user_id <> uea2.user_id)
UNION ALL
SELECT user_phone_id contact_id,'phone' contact_type,phone_nbr contact_value,up.user_id,1 is_contact_unique,0 input_contact FROM
user_phone up,user_email_address uea,country c WHERE lower(uea.email_addr) = lower(NVL(p_in_email_address,uea.email_addr))
  AND uea.user_id = NVL(p_in_user_id,uea.user_id)
  AND p_in_email_address IS NOT NULL
  AND uea.user_id = up.user_id
  AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec')
  AND c.is_sms_capable = 1
  AND NOT EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(up2.phone_nbr,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','') AND up2.user_id <> up.user_id)--09/20/2017
  UNION ALL
  SELECT user_phone_id contact_id,'phone' contact_type,phone_nbr contact_value,up.user_id,1 is_contact_unique,0 input_contact FROM
user_phone up,country c WHERE
  up.user_id = p_in_user_id
  AND p_in_user_id IS NOT NULL
  AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec')
  AND c.is_sms_capable = 1
  AND NOT EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(up2.phone_nbr,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','') AND up2.user_id <> up.user_id)
  UNION ALL
  SELECT uea2.email_address_id contact_id,'email' contact_type,uea2.email_addr contact_value,uea.user_id,0 is_contact_unique,CASE WHEN lower(uea2.email_addr) = lower(p_in_email_address) THEN 1 ELSE 0 END input_contact FROM
user_email_address uea,user_email_address uea2 WHERE lower(uea.email_addr) = lower(NVL(p_in_email_address,uea.email_addr))
AND uea.user_id = NVL(p_in_user_id,uea.user_id) AND uea.user_id = uea2.user_id
-- AND lower(uea.email_addr) <> lower(uea2.email_addr)--Do not show the inut email address in the outpur result if the p_in_unique_contact=0 -- 11/03/2017 Commented out
AND EXISTS (SELECT 1 FROM user_email_address ue where lower(ue.email_addr)  = lower(uea2.email_addr) AND ue.user_id <> uea2.user_id)
UNION ALL
SELECT user_phone_id contact_id,'phone' contact_type,phone_nbr contact_value,up.user_id,0 is_contact_unique,0 input_contact FROM
user_phone up,user_email_address uea,country c WHERE lower(uea.email_addr) = lower(NVL(p_in_email_address,uea.email_addr))
  AND uea.user_id = NVL(p_in_user_id,uea.user_id)
  AND p_in_email_address IS NOT NULL
  AND uea.user_id = up.user_id
  AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec')
  AND c.is_sms_capable = 1
  AND EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(up2.phone_nbr,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','') AND up2.user_id <> up.user_id)--09/20/2017
  UNION ALL
  SELECT user_phone_id contact_id,'phone' contact_type,phone_nbr contact_value,up.user_id,0 is_contact_unique,0 input_contact FROM
user_phone up,country c WHERE
  up.user_id = p_in_user_id
  AND p_in_user_id IS NOT NULL
  AND up.country_phone_code = c.country_code AND up.phone_type IN ('mob','rec')
  AND c.is_sms_capable = 1
  AND EXISTS (SELECT 1 FROM user_phone up2 where regexp_replace(up2.phone_nbr,'[^0-9]','')  = regexp_replace(up.phone_nbr,'[^0-9]','') AND up2.user_id <> up.user_id)  
  )) WHERE rec_rank = 1;
  
END IF;
p_out_return_code := 0;
EXCEPTION WHEN OTHERS THEN

 p_out_return_code := 99;
prc_execution_log_entry(v_process_name,v_release_level,'INFO',SQLERRM,null); 
 
 END;
/
