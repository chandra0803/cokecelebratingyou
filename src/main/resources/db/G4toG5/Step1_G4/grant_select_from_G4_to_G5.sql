CREATE OR REPLACE PROCEDURE prc_grant_select_g4_to_g5 
 (pi_new_schema   IN  VARCHAR2,
  po_return_code  OUT NUMBER)
IS

/*-----------------------------------------------------------------------------
 
  Process Name : PRC_GRANT_SELECT_G4_TO_G5
  Purpose      : Grant select from G4 schema to G5 schema
  
  Modification History
  Person          Date         Comments
  -------------  ----------  -------------------------------------------------
  Arun S         02/19/2012  Initial creation
  
-----------------------------------------------------------------------------*/

  CURSOR cur_tbl IS
    SELECT object_name table_name
      FROM user_objects
     WHERE object_name IN ('ACL',
                          'CHARACTERISTIC',
                          'OS_PROPERTYSET',
                          'ENCRYPTION_CONVERSION',
                          'MESSAGE',
                          'SUPPLIER', 
                          'COUNTRY',
                          'COUNTRY_SUPPLIERS',
                          'EMPLOYER',
                          'HIERARCHY', 
                          'HIERARCHY_NODE',
                          'NODE_TYPE',
                          'HIERARCHY_NODE_TYPE',
                          'NODE',
                          'NODE_CHARACTERISTIC',
                          'ROLE',
                          'USER_TYPE_ROLE',
                          'APPLICATION_USER',
                          'PARTICIPANT',
                          'PARTICIPANT_EMPLOYER',
                          --'PARTICIPANT_ABOUTME',        --tbl not present in g4
                          'PARTICIPANT_CONTACT_METHOD',
                          'USER_EMAIL_ADDRESS',
                          'USER_ADDRESS',
                          'USER_PHONE',
                          'USER_CHARACTERISTIC',
                          'USER_NODE',
                          'USER_NODE_HISTORY',
                          'USER_ACL',
                          'USER_ROLE',
                          'LOGIN_ACTIVITY',
                          'PARTICIPANT_COMM_PREFERENCE',
                          'PARTICIPANT_ADDRESS_BOOK',
                          --'PARTICIPANT_FOLLOWERS',      --tbl not present in g4
                          'USER_TWITTER',
                          'USER_FACEBOOK', 
                          'USER_COUNTRY_CHANGES',
                          'VW_CMS_ASSET_VALUE',
                          'CMS_CONTENT_DATA',
                          'CMS_CONTENT',
                          'CMS_CONTENT_KEY',
                          'CMS_ASSET',
                          'CMS_SECTION',
                          'CMS_CONTENT_KEY_AUDIENCE_LNK',
                          'CMS_AUDIENCE',
                          'CMS_ASSET_TYPE_ITEM',
                          'CMS_ASSET_TYPE',
                          'CMS_APPLICATION',
                          'AUDIENCE',
                          'AUDIENCE_CRITERIA',
                          'AUDIENCE_CRITERIA_CHAR',
                          'PARTICIPANT_AUDIENCE',
                          'BUDGET_MASTER',
                          'BUDGET',
                          'BUDGET_HISTORY',
                          'ODD_CAST_CATEGORY',
                          'CARD',
                          'ECARD',
                          'ECARD_LOCALE',
                          'ODD_CAST_CARD',
                          'QUIZ',
                          'QUIZ_QUESTION',
                          'QUIZ_QUESTION_ANSWER',
                          'CALCULATOR',
                          'CLAIM_FORM',                  
                          'CLAIM_FORM_STEP',             
                          'CLAIM_FORM_STEP_EMAIL',       
                          'CLAIM_FORM_STEP_ELEMENT',     
                          'PRODUCT_CATEGORY',            
                          'PRODUCT',
                          'PRODUCT_CHARACTERISTIC',
                          'PROCESS',
                          'PROCESS_ROLE',
                          'PROMOTION',
                          'PROMO_APPROVAL_OPTION',
                          'PROMO_APPROVAL_PARTICIPANT',
                          'PROMO_AUDIENCE',
                          'PROMO_BEHAVIOR',
                          'PROMO_CARD',
                          'PROMO_CERTIFICATE',
                          'PROMO_CFSE_VALIDATION',
                          --'PROMO_GOALQUEST',             
                          'PROMO_MERCH_COUNTRY',
                          'PROMO_MERCH_PROGRAM_LEVEL',
                          'PROMO_PRODUCT_CLAIM',         
                          'PROMO_NOMINATION',
                          'PROMO_NOTIFICATION',
                          'PROMO_QUIZ',
                          'PROMO_RECOGNITION',
                          'PROMO_HOME_PAGE_ITEM',
                          'PROMO_WELLNESS',
                          'PROMO_SWEEPSTAKE_DRAWING',
                          'PROMO_SWEEPSTAKE_WINNERS',
                          'PROXY',
                          'PROXY_MODULE',
                          'PROXY_MODULE_PROMOTION',
                          'STACK_RANK',
                          'STACK_RANK_NODE',
                          'STACK_RANK_PARTICIPANT',         
                          'PARTICIPANT_PARTNER',         
                          'CLAIM',
                          'CLAIM_APPROVER_SNAPSHOT',
                          'CLAIM_CFSE',
                          'CLAIM_GROUP',
                          'CLAIM_ITEM',
                          'CLAIM_ITEM_APPROVER',
                          'CLAIM_PARTICIPANT',
                          'CLAIM_RECIPIENT',
                          'CLAIM_PRODUCT',
                          'CLAIM_PRODUCT_CHARACTERISTIC',
                          'NOMINATION_CLAIM',
                          'PRODUCT_CLAIM',
                          'QUIZ_CLAIM',
                          'QUIZ_RESPONSE',
                          'RECOGNITION_CLAIM',
                          'CALCULATOR_CRITERION',
                          'CALCULATOR_CRITERION_RATING',
                          'CALCULATOR_RESPONSE',
                          'CALCULATOR_PAYOUT',
                          'SCHEDULED_RECOGNITION',
                          'PURL_RECIPIENT',
                          'PURL_RECIPIENT_CFSE',
                          'PURL_CONTRIBUTOR',
                          'PURL_CONTRIBUTOR_COMMENT',
                          'PURL_CONTRIBUTOR_MEDIA',
                          'PROMO_PAYOUT_GROUP',
                          'PROMO_PAYOUT',
                          'PROMO_TEAM_POSITION',
                          'MIN_QUALIFIER_STATUS',
                          'STACK_RANK_PAYOUT_GROUP',
                          'STACK_RANK_PAYOUT',                          
                          'MERCH_ORDER',
                          'ACTIVITY',
                          'ACTIVITY_MERCH_ORDER',
                          'BUDGET',
                          'JOURNAL',
                          'ACTIVITY_JOURNAL',
                          'PAYOUT_CALCULATION_AUDIT',       
                          'MOCK_ACCOUNT_TRANSACTION',         
                          'PROMO_APPROVAL_OPTION_REASON',        
                          'WELCOME_MESSAGE',         
                          'WELCOME_MESSAGE_AUDIENCE',
                          'PREP_ENCRYPT_PAX',
                          'PREP_ENCRYPT_JOURNAL',
                          'PREP_ENCRYPT_MERCH'
                          );
  
  --Procedure variables
  C_process_name          execution_log.process_name%TYPE  := 'prc_grant_select_g4_to_g5';
  C_release_level         execution_log.release_level%TYPE := '1';
  C_severity_i            execution_log.severity%TYPE      := 'INFO';
  C_severity_e            execution_log.severity%TYPE      := 'ERROR';
  
  v_exe_log_msg           execution_log.text_line%TYPE;
  v_stage                 VARCHAR2(100);

BEGIN

  prc_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          'Procedure Started for new prefix : '||pi_new_schema,
                          NULL);
  COMMIT;
  
  FOR rec_tbl IN cur_tbl LOOP
    v_stage := 'Grant select to the table: '||rec_tbl.table_name;
    EXECUTE IMMEDIATE 'GRANT SELECT ON '||rec_tbl.table_name||' TO '|| pi_new_schema;
  END LOOP;

  v_exe_log_msg := 'Procedure completed.';
  prc_execution_log_entry(C_process_name,C_release_level,C_severity_i,
                          v_exe_log_msg,
                          NULL);
  COMMIT;
  po_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    po_return_code := 99;
    v_exe_log_msg := 'Error at stage:'||v_stage||' message:'||SQLERRM;
    prc_execution_log_entry(C_process_name,C_release_level,C_severity_e,
                            v_exe_log_msg,
                            NULL);
    COMMIT;
    
END;
/

DECLARE
po_return_code   NUMBER;
BEGIN

prc_grant_select_g4_to_g5('TargetPrefix',po_return_code);--Need to change the prefix name to the desired G5 schema prefix.
end;
/