CREATE OR REPLACE FORCE VIEW SM_CP_WELCOME_LOGIN_PWD_VW
(
   MAILING_ID,
   USER_ID,
   LOCALE,
   BATCH_ID,
   EMAIL_ADDR,   
   FIRST_NAME,
   LAST_NAME,
   PROGRAM_NAME,
   challengepoint1,
   challengepointLevel1,
   challengepointValue1,   
   challengepointReward1,
   challengepoint2,
   challengepointLevel2,
   challengepointValue2,   
   challengepointReward2,
   challengepoint3,
   challengepointLevel3,
   challengepointValue3,   
   challengepointReward3,
   BASE,
   SITE_LINK,
   CONTACT_URL,
   REGISTRATION_START_DATE, 
   REGISTRATION_END_DATE,
   PROMOTION_START_DATE,
   PROMOTION_END_DATE ,
   AWARD_PERQS,
   USER_NM,
   PASSWORD
)
AS
   SELECT   r.mailing_id,
            r.user_id,
            r.locale,
            m.batch_id,
            ue.email_addr,            
            MAX (DECODE (d.data_key, 'firstName', d.data_value, NULL)) firstName,
            MAX (DECODE (d.data_key, 'lastName', d.data_value, NULL)) lastName,              
            MAX (DECODE (d.data_key, 'programName', d.data_value, NULL)) programName,
            MAX (DECODE (d.data_key, 'challengepoint1', d.data_value, NULL)) challengepoint1,
            MAX (DECODE (d.data_key, 'challengepointLevel1', d.data_value, NULL)) challengepointLevel1,
            MAX (DECODE (d.data_key, 'challengepointValue1', d.data_value, NULL)) challengepointValue1,            
            MAX (DECODE (d.data_key, 'challengepointReward1', d.data_value, NULL)) challengepointReward1,
            MAX (DECODE (d.data_key, 'challengepoint2', d.data_value, NULL)) challengepoint2,
            MAX (DECODE (d.data_key, 'challengepointLevel2', d.data_value, NULL)) challengepointLevel2,
            MAX (DECODE (d.data_key, 'challengepointValue2', d.data_value, NULL)) challengepointValue2,            
            MAX (DECODE (d.data_key, 'challengepointReward2', d.data_value, NULL)) challengepointReward2,
            MAX (DECODE (d.data_key, 'challengepoint3', d.data_value, NULL)) challengepoint3,
            MAX (DECODE (d.data_key, 'challengepointLevel3', d.data_value, NULL)) challengepointLevel3,
            MAX (DECODE (d.data_key, 'challengepointValue3', d.data_value, NULL)) challengepointValue3,            
            MAX (DECODE (d.data_key, 'challengepointReward3', d.data_value, NULL)) challengepointReward3,
            MAX (DECODE (d.data_key, 'base', d.data_value, NULL)) base,
            MAX (DECODE (d.data_key, 'siteLink', d.data_value, NULL)) siteLink,
            MAX (DECODE (d.data_key, 'contactUsUrl', d.data_value, NULL)) contactUsUrl,
            MAX (DECODE (d.data_key, 'registrationStartDate', d.data_value, NULL)) registrationStartDate,
            MAX (DECODE (d.data_key, 'registrationEndDate', d.data_value, NULL)) registrationEndDate,
            MAX (DECODE (d.data_key, 'promotionStartDate', d.data_value, NULL)) promotionStartDate,
            MAX (DECODE (d.data_key, 'promotionEndDate', d.data_value, NULL)) promotionEndDate,
            MAX (DECODE (d.data_key, 'points', d.data_value, NULL)) points,
            MAX (DECODE (d.data_key, 'user', d.data_value, NULL)) user_nm,
            MAX (DECODE (d.data_key, 'password', d.data_value, NULL)) password
       FROM mailing_recipient r, mailing_recipient_data d, mailing m, user_email_address ue
      WHERE m.batch_id is not null 
        AND r.MAILING_RECIPIENT_ID = d.MAILING_RECIPIENT_ID
        AND m.mailing_id = r.mailing_id       
        AND d.data_key IN ( 'firstName', 'lastName', 'programName' ,'challengepoint1',  'challengepointLevel1' ,  'challengepointValue1', 'challengepointReward1' ,'challengepoint2', 'challengepointLevel2' ,  'challengepointValue2' , 'challengepointReward2' ,'challengepoint3','challengepointLevel3' ,  'challengepointValue3' ,'challengepointReward3' ,'base','siteLink' , 'contactUsUrl' ,'registrationStartDate','registrationEndDate', 'promotionStartDate' , 'promotionEndDate' , 'points' , 'user' ,'password' )
        AND ue.user_id = r.user_id
        AND ue.is_primary = 1        
   GROUP BY R.USER_ID, R.MAILING_ID, R.LOCALE , M.BATCH_ID, UE.EMAIL_ADDR
/