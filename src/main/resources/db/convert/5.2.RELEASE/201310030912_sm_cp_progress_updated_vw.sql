CREATE OR REPLACE FORCE VIEW SM_CP_PROGRESS_UPDATED_VW
(
   MAILING_ID,
   USER_ID,
   LOCALE,
   BATCH_ID,
   EMAIL_ADDR,   
   FIRST_NAME,
   LAST_NAME,   
   PROGRAM_NAME,
   challengepointLevelDescription,
   challengepointLevel,
   challengepointLevelAward,   
   BASE,
   PROMOTION_END_DATE ,
   primaryAwardIncrement ,
   primaryAward ,
   totalChallengepointValue ,
   actual_results , 
   percentToChallengepoint,
   date_1
)
AS
     SELECT r.mailing_id,
            r.user_id,
            r.locale,
            m.batch_id,
            ue.email_addr,            
            MAX (DECODE (d.data_key, 'firstName', d.data_value, NULL)) firstName,
            MAX (DECODE (d.data_key, 'lastName', d.data_value, NULL)) lastName,            
            MAX (DECODE (d.data_key, 'programName', d.data_value, NULL)) programName,
            MAX (DECODE (d.data_key, 'challengepointLevelDescription', d.data_value, NULL)) challengepointLevelDescription,
            MAX (DECODE (d.data_key, 'challengepointLevel', d.data_value, NULL)) challengepointLevel,
            MAX (DECODE (d.data_key, 'challengepointLevelAward', d.data_value, NULL)) challengepointLevelAward,            
            MAX (DECODE (d.data_key, 'base', d.data_value, NULL)) base,
            MAX (DECODE (d.data_key, 'promotionEndDate', d.data_value, NULL)) promotionEndDate,
            MAX (DECODE (d.data_key, 'primaryAwardIncrement', d.data_value, NULL)) primaryAwardIncrement,
            MAX (DECODE (d.data_key, 'primaryAward', d.data_value, NULL)) primaryAward,
            MAX (DECODE (d.data_key, 'totalChallengepointValue', d.data_value, NULL)) totalChallengepointValue,
            MAX (DECODE (d.data_key, 'actual_results', d.data_value, NULL)) actual_results,
            MAX (DECODE (d.data_key, 'percentToChallengepoint', d.data_value, NULL)) percentToChallengepoint,
            MAX (DECODE (d.data_key, 'date', d.data_value, NULL)) date_1            
       FROM mailing_recipient r, mailing_recipient_data d, mailing m, user_email_address ue
      WHERE m.batch_id is not null 
        AND r.MAILING_RECIPIENT_ID = d.MAILING_RECIPIENT_ID
        AND m.mailing_id = r.mailing_id       
        AND d.data_key IN ( 'firstName','lastName' , 'programName' ,'challengepointLevelDescription',  'challengepointLevel' ,  'challengepointLevelAward','base' , 'promotionEndDate' , 'primaryAwardIncrement' , 'primaryAward' , 'totalChallengepointValue' , 'actual_results' , 'percentToChallengepoint','date')
        AND ue.user_id = r.user_id
        AND ue.is_primary = 1        
   GROUP BY R.USER_ID, R.MAILING_ID, R.LOCALE , M.BATCH_ID, UE.EMAIL_ADDR
/