CREATE OR REPLACE FORCE VIEW SM_CP_GOAL_NOT_SELECTED_VW
(
   MAILING_ID,
   USER_ID,
   LOCALE,
   BATCH_ID,
   EMAIL_ADDR,
   FIRST_NAME,
   LAST_NAME,
   PROMOTION_NAME,
   SITE_LINK,
   SITE_URL,
   CONTACT_US_URL,
   CP_SELECTION_END_DATE
)
AS
     SELECT r.mailing_id,
            r.user_id,
            r.locale,
            m.batch_id,             
            ue.email_addr,            
            MAX (DECODE (d.data_key, 'firstName', d.data_value, NULL)) firstName,
            MAX (DECODE (d.data_key, 'lastName', d.data_value, NULL)) lastName ,
            MAX (DECODE (d.data_key, 'promotionName', d.data_value, NULL)) promotionName ,
            MAX (DECODE (d.data_key, 'siteLink', d.data_value, NULL)) siteLink,
            MAX (DECODE (d.data_key, 'siteURL', d.data_value, NULL)) siteUrl,
            MAX (DECODE (d.data_key, 'contactUsUrl', d.data_value, NULL)) contactUsUrl,
            MAX (DECODE (d.data_key, 'challengepointSelectionEndDate', d.data_value, NULL)) cpSelectionEndDate 
       FROM mailing_recipient r, mailing_recipient_data d, mailing m, user_email_address ue
      WHERE m.batch_id is not null 
        AND r.MAILING_RECIPIENT_ID = d.MAILING_RECIPIENT_ID
        AND m.mailing_id = r.mailing_id       
        AND d.data_key IN (  'firstName', 'lastName', 'promotionName' , 'siteLink' , 'siteURL', 'contactUsUrl', 'challengepointSelectionEndDate' )
        AND ue.user_id = r.user_id
        AND ue.is_primary = 1        
   GROUP BY R.USER_ID, R.MAILING_ID, R.LOCALE , M.BATCH_ID, UE.EMAIL_ADDR
/

   
   