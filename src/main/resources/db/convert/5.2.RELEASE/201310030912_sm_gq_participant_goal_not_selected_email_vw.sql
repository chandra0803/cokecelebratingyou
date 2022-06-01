CREATE OR REPLACE FORCE VIEW SM_GQ_PAX_GOAL_NOT_SELECTED_VW
(
   MAILING_ID,
   USER_ID,
   LOCALE,
   BATCH_ID,
   EMAIL_ADDR,
   FIRST_NAME,
   LAST_NAME,
   PROGRAM_NAME,
   SITE_LINK,
   GOAL_SELECTION_END_DATE
)
AS
     SELECT r.mailing_id,
            r.user_id,
            r.locale,
            m.batch_id,             
            ue.email_addr,            
            MAX (DECODE (d.data_key, 'firstName', d.data_value, NULL)) firstName,
            MAX (DECODE (d.data_key, 'lastName', d.data_value, NULL)) lastName ,
            MAX (DECODE (d.data_key, 'programName', d.data_value, NULL)) programName ,
            MAX (DECODE (d.data_key, 'siteLink', d.data_value, NULL)) siteLink,
            MAX (DECODE (d.data_key, 'goalSelectionEndDate', d.data_value, NULL)) goalSelectionEndDate 
       FROM mailing_recipient r, mailing_recipient_data d, mailing m, user_email_address ue
      WHERE m.batch_id is not null 
        AND r.MAILING_RECIPIENT_ID = d.MAILING_RECIPIENT_ID
        AND m.mailing_id = r.mailing_id       
        AND d.data_key IN (  'firstName', 'lastName', 'programName' , 'siteLink' , 'goalSelectionEndDate' )
        AND ue.user_id = r.user_id
        AND ue.is_primary = 1        
   GROUP BY R.USER_ID, R.MAILING_ID, R.LOCALE , M.BATCH_ID, UE.EMAIL_ADDR
/

   
   