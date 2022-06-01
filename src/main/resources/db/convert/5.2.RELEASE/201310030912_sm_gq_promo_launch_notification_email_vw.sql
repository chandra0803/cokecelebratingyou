CREATE OR REPLACE FORCE VIEW SM_GQ_PROMO_LAUNCH_NOTI_VW
(
   MAILING_ID,
   USER_ID,
   LOCALE,
   BATCH_ID,
   EMAIL_ADDR,   
   FIRST_NAME,
   PROGRAM_NAME,
   GOAL_LEVEL1,
   GOAL_VALUE1,
   GOAL_REWARD1,   
   GOAL_LEVEL2,
   GOAL_VALUE2,
   GOAL_REWARD2,   
   GOAL_LEVEL3,
   GOAL_VALUE3,
   GOAL_REWARD3,   
   GOAL_LEVEL4,
   GOAL_VALUE4,
   GOAL_REWARD4,   
   GOAL_LEVEL5,
   GOAL_VALUE5,
   GOAL_REWARD5,   
   BASE,
   SITE_LINK,
   CONTACT_URL,
   REGISTRATION_START_DATE, 
   REGISTRATION_END_DATE,
   PROMOTION_START_DATE,
   PROMOTION_END_DATE ,
   AWARD_PERQS
)
AS
     SELECT r.mailing_id,
            r.user_id,
            r.locale,
            m.batch_id,
            ue.email_addr,            
            MAX (DECODE (d.data_key, 'firstName', d.data_value, NULL)) firstName,
            MAX (DECODE (d.data_key, 'programName', d.data_value, NULL)) programName,
            MAX (DECODE (d.data_key, 'goalLevel1', d.data_value, NULL)) goalLevel1,
            MAX (DECODE (d.data_key, 'goalValue1', d.data_value, NULL)) goalValue1,
            MAX (DECODE (d.data_key, 'goalReward1', d.data_value, NULL)) goalReward1,            
            MAX (DECODE (d.data_key, 'goalLevel2', d.data_value, NULL)) goalLevel2,
            MAX (DECODE (d.data_key, 'goalValue2', d.data_value, NULL)) goalValue2,
            MAX (DECODE (d.data_key, 'goalReward1', d.data_value, NULL)) goalReward2,            
            MAX (DECODE (d.data_key, 'goalLevel3', d.data_value, NULL)) goalLevel3,
            MAX (DECODE (d.data_key, 'goalValue3', d.data_value, NULL)) goalValue3,
            MAX (DECODE (d.data_key, 'goalReward1', d.data_value, NULL)) goalReward3,            
            MAX (DECODE (d.data_key, 'goalLevel4', d.data_value, NULL)) goalLevel4,
            MAX (DECODE (d.data_key, 'goalValue4', d.data_value, NULL)) goalValue4,
            MAX (DECODE (d.data_key, 'goalReward1', d.data_value, NULL)) goalReward4,            
            MAX (DECODE (d.data_key, 'goalLevel5', d.data_value, NULL)) goalLevel5,
            MAX (DECODE (d.data_key, 'goalValue5', d.data_value, NULL)) goalValue5,
            MAX (DECODE (d.data_key, 'goalReward1', d.data_value, NULL)) goalReward5,            
            MAX (DECODE (d.data_key, 'base', d.data_value, NULL)) base,
            MAX (DECODE (d.data_key, 'siteLink', d.data_value, NULL)) siteLink,
            MAX (DECODE (d.data_key, 'contactUsUrl', d.data_value, NULL)) contactUsUrl,
            MAX (DECODE (d.data_key, 'registrationStartDate', d.data_value, NULL)) registrationStartDate,
            MAX (DECODE (d.data_key, 'registrationEndDate', d.data_value, NULL)) registrationEndDate,
            MAX (DECODE (d.data_key, 'promotionStartDate', d.data_value, NULL)) promotionStartDate,
            MAX (DECODE (d.data_key, 'promotionEndDate', d.data_value, NULL)) promotionEndDate,
            MAX (DECODE (d.data_key, 'points', d.data_value, NULL)) points
       FROM mailing_recipient r, mailing_recipient_data d, mailing m, user_email_address ue
      WHERE m.batch_id is not null 
        AND r.MAILING_RECIPIENT_ID = d.MAILING_RECIPIENT_ID
        AND m.mailing_id = r.mailing_id       
        AND d.data_key IN ( 'firstName', 'programName' , 'goalLevel1' ,  'goalValue1', 'goalReward1' , 'goalLevel2' ,  'goalValue2' , 'goalReward2' ,'goalLevel3' ,  'goalValue3' ,'goalReward3' , 'goalLevel4' ,  'goalValue4' , 'goalReward4' ,'goalLevel5' ,  'goalValue5' , 'goalReward5' ,'base','siteLink' , 'contactUsUrl' ,'registrationStartDate','registrationEndDate', 'promotionStartDate' , 'promotionEndDate' , 'points')
        AND ue.user_id = r.user_id
        AND ue.is_primary = 1        
   GROUP BY R.USER_ID, R.MAILING_ID, R.LOCALE , M.BATCH_ID, UE.EMAIL_ADDR
/