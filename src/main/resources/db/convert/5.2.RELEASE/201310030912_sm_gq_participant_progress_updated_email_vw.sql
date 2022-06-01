CREATE OR REPLACE FORCE VIEW SM_GQ_PAX_PROG_UPD_VW
(
   MAILING_ID,
   USER_ID,
   LOCALE,
   BATCH_ID,   
   EMAIL_ADDR, 
   FIRST_NAME,
   LAST_NAME,
   PROGRAM_NAME,
   GOAL_LEVEL,
   GOAL_LEVEL_AMOUNT,
   TOTAL_GOAL_VALUE,
   ACTUAL_RESULTS,
   PERCENT_TO_GOAL,
   SITE_LINK,
   DATE_1
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
            MAX (DECODE (d.data_key, 'goalLevel', d.data_value, NULL)) goalLevel ,
            MAX (DECODE (d.data_key, 'goalLevelAmount', d.data_value, NULL)) goalLevelAmount ,
            MAX (DECODE (d.data_key, 'totalGoalValue', d.data_value, NULL)) totalGoalValue ,
            MAX (DECODE (d.data_key, 'actualResults', d.data_value, NULL)) actualResults ,
            MAX (DECODE (d.data_key, 'percentToGoal', d.data_value, NULL)) percentToGoal ,
            MAX (DECODE (d.data_key, 'siteLink', d.data_value, NULL)) siteLink,
            MAX (DECODE (d.data_key, 'date', d.data_value, NULL)) date1
      FROM mailing_recipient r, mailing_recipient_data d , mailing m , user_email_address ue
      WHERE m.batch_id is not null 
        AND r.MAILING_RECIPIENT_ID = d.MAILING_RECIPIENT_ID
        AND m.mailing_id = r.mailing_id      
        AND d.data_key IN ( 'firstName', 'lastName', 'programName' , 'goalLevel' , 'goalLevelAmount' , 'totalGoalValue' , 'actualResults','percentToGoal','siteLink','date' )
        AND ue.user_id = r.user_id
        AND ue.is_primary = 1
   GROUP BY R.USER_ID, R.MAILING_ID , R.LOCALE , M.BATCH_ID , UE.EMAIL_ADDR
/
