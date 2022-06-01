DECLARE   

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
                          'CONVERSION_PREP_ENCRYPT',
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
                          'MERCH_ORDER',
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
                          'ACTIVITY',
                          'ACTIVITY_MERCH_ORDER',
                          'BUDGET',
                          'JOURNAL',
                          'ACTIVITY_JOURNAL',
                          'PAYOUT_CALCULATION_AUDIT',       
                          'MOCK_ACCOUNT_TRANSACTION',         
                          'PROMO_APPROVAL_OPTION_REASON',        
                          'WELCOME_MESSAGE',         
                          'WELCOME_MESSAGE_AUDIENCE'
                          )UNION
                          SELECT 'PREP_ENCRYPT_PAX' FROM DUAL
                          UNION
                          SELECT 'PREP_ENCRYPT_JOURNAL' FROM DUAL
                          UNION
                          SELECT 'PREP_ENCRYPT_MERCH' FROM DUAL;   
  
  e_table_exists   EXCEPTION;
  PRAGMA           EXCEPTION_INIT(e_table_exists,-00955);

  v_old_schema     VARCHAR2(100);
  v_sql            VARCHAR2(500);
   
BEGIN

  v_old_schema := 'SourcePrefix';--This needs to be updated with the old schema name.

  FOR rec_tbl IN cur_tbl LOOP
  
    BEGIN
      --added substr sothat synonym name should not exceed more than 30 char after appending  _G4 those 
      --synonym name taken care in tier 2 convert script 
      v_sql := 'CREATE SYNONYM '||SUBSTR(rec_tbl.table_name, 1, 27)||'_G4 FOR '||v_old_schema||'.'||rec_tbl.table_name;
      --dbms_output.put_line ('SQL: '||v_sql);
      EXECUTE IMMEDIATE v_sql;
    
    EXCEPTION
      WHEN e_table_exists THEN 
        --dbms_output.put_line ('Synonym exists: '||rec_tbl.table_name||'_G4');
        NULL;
    END;  
  
  END LOOP;
  
  dbms_output.put_line ('Synonynm creation completed');
  
EXCEPTION
  WHEN OTHERS THEN
    dbms_output.put_line ('SQL: '||v_sql||' When Others: '||sqlerrm);
END;
/