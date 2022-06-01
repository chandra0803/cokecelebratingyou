CREATE OR REPLACE FORCE VIEW SM_CP_PAX_NOT_ACHIEVED_VW
(
   MAILING_ID,
   USER_ID,
   LOCALE,
   BATCH_ID,
   EMAIL_ADDR,   
   FIRST_NAME,
   LAST_NAME,   
   PROGRAM_NAME,
   challengepointLevel,
   actual_results , 
   AWARD_PERQS,
   actualResults,
   primaryEarnings,
   interimAmount,
   remainingPoints,
   calculationapprovedate,
   notAchieved,
   hasAward,
   threshold,
   SITE_LINK,
   CONTACT_US_URL
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
            MAX (DECODE (d.data_key, 'challengepointLevel', d.data_value, NULL)) challengepointLevel,
            MAX (DECODE (d.data_key, 'actual_results', d.data_value, NULL)) actual_results,
            MAX (DECODE (d.data_key, 'points', d.data_value, NULL)) points,
            MAX (DECODE (d.data_key, 'actualResults', d.data_value, NULL)) actualResults,
            MAX (DECODE (d.data_key, 'primaryEarnings', d.data_value, NULL)) primaryEarnings,
            MAX (DECODE (d.data_key, 'interimAmount', d.data_value, NULL)) interimAmount,
            MAX (DECODE (d.data_key, 'remainingPoints', d.data_value, NULL)) remainingPoints,
            MAX (DECODE (d.data_key, 'calculationapprovedate', d.data_value, NULL)) calculationapprovedate,   
            MAX (DECODE (d.data_key, 'notAchieved', d.data_value, NULL)) notAchieved,
            MAX (DECODE (d.data_key, 'hasAward', d.data_value, NULL)) hasAward,
            MAX (DECODE (d.data_key, 'threshold', d.data_value, NULL)) threshold,
            MAX (DECODE (d.data_key, 'siteLink', d.data_value, NULL)) siteLink,
            MAX (DECODE (d.data_key, 'contactUsUrl', d.data_value, NULL)) contactUsUrl
       FROM mailing_recipient r, mailing_recipient_data d, mailing m, user_email_address ue
      WHERE m.batch_id is not null 
        AND r.MAILING_RECIPIENT_ID = d.MAILING_RECIPIENT_ID
        AND m.mailing_id = r.mailing_id       
        AND d.data_key IN ( 'firstName','lastName' , 'programName' , 'challengepointLevel', 'actual_results' , 'points',  
		   'actualResults',
		   'primaryEarnings',
		   'interimAmount',
		   'remainingPoints',
		   'calculationapprovedate',
		   'notAchieved',
		   'hasAward',
		   'threshold',
		   'siteLink',
		   'contactUsUrl')
        AND ue.user_id = r.user_id
        AND ue.is_primary = 1        
   GROUP BY R.USER_ID, R.MAILING_ID, R.LOCALE , M.BATCH_ID, UE.EMAIL_ADDR
/