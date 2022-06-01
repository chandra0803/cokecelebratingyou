CREATE OR REPLACE FORCE VIEW SM_CP_INTERIM_VW
(
   MAILING_ID,
   USER_ID,
   LOCALE,
   BATCH_ID,
   EMAIL_ADDR,   
   firstName,
   lastName,
   programName,
   siteLink,
   registrationStartDate,
   progressSubmissionDate,
   interimAmount,
   primaryAwardIncrement,
   primaryAwardThreshold,
   promotionEndDate,
   challengepointLevel,
   challengepointLevelAmount,
   totalChallengepointValue,
   contactUsUrl,
   isThreshold,
   points
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
            MAX (DECODE (d.data_key, 'siteLink', d.data_value, NULL)) siteLink ,
            MAX (DECODE (d.data_key, 'registrationStartDate', d.data_value, NULL)) registrationStartDate ,
            MAX (DECODE (d.data_key, 'progressSubmissionDate', d.data_value, NULL)) progressSubmissionDate ,
            MAX (DECODE (d.data_key, 'interimAmount', d.data_value, NULL)) interimAmount ,
            MAX (DECODE (d.data_key, 'primaryAwardIncrement', d.data_value, NULL)) primaryAwardIncrement ,
            MAX (DECODE (d.data_key, 'primaryAwardThreshold', d.data_value, NULL)) primaryAwardThreshold,
            MAX (DECODE (d.data_key, 'promotionEndDate', d.data_value, NULL)) promotionEndDate,
            MAX (DECODE (d.data_key, 'challengepointLevel', d.data_value, NULL)) challengepointLevel,
            MAX (DECODE (d.data_key, 'challengepointLevelAmount', d.data_value, NULL)) challengepointLevelAmount,
            MAX (DECODE (d.data_key, 'totalChallengepointValue', d.data_value, NULL)) totalChallengepointValue,
            MAX (DECODE (d.data_key, 'contactUsUrl', d.data_value, NULL)) contactUsUrl,
            MAX (DECODE (d.data_key, 'isThreshold', d.data_value, NULL)) isThreshold,
            MAX (DECODE (d.data_key, 'points', d.data_value, NULL)) points
       FROM mailing_recipient r, mailing_recipient_data d, mailing m, user_email_address ue
      WHERE m.batch_id is not null 
        AND r.MAILING_RECIPIENT_ID = d.MAILING_RECIPIENT_ID
        AND m.mailing_id = r.mailing_id       
        AND d.data_key IN ('firstName','lastName', 'programName', 'siteLink', 'registrationStartDate' , 'progressSubmissionDate' , 'interimAmount' , 'primaryAwardIncrement' , 'primaryAwardThreshold','promotionEndDate','challengepointLevel','challengepointLevelAmount','totalChallengepointValue','contactUsUrl','isThreshold','points')
        AND ue.user_id = r.user_id
        AND ue.is_primary = 1        
   GROUP BY R.USER_ID, R.MAILING_ID, R.LOCALE , M.BATCH_ID , UE.EMAIL_ADDR
/