-- script finds the maximum sequence value in the table
-- and ensures the sequence generator exceeds the max table value
DECLARE
   v_last_seq_id     NUMBER;
   v_sql_stmt        VARCHAR2(1000);
   v_tab_seq_id      dbms_sql.NUMBER_table;

   TYPE rc IS REF CURSOR;
   v_cursor RC;

   CURSOR cur_max_seq IS      
   WITH max_seq AS
   (  -- get max sequence values
                SELECT 'ACL_PK_SQ' AS sequence_name,
                                                     CAST(MAX(acl_id) AS NUMBER) AS max_seq_id  FROM acl
      UNION ALL SELECT 'CHARACTERISTIC_PK_SQ',        CAST(MAX(characteristic_id) AS NUMBER)            FROM characteristic
      UNION ALL SELECT 'ENTITY_ID_PK_SQ',            CAST(MAX(entity_id)         AS NUMBER)            FROM os_propertyset
      UNION ALL SELECT 'MESSAGE_PK_SQ', CAST(MAX(message_id)   AS NUMBER)            FROM message
      UNION ALL SELECT 'SUPPLIER_PK_SQ',                CAST(MAX(supplier_id)         AS NUMBER)            FROM supplier
      UNION ALL SELECT 'COUNTRY_PK_SQ',               CAST(MAX(country_id)        AS NUMBER)            FROM country
      UNION ALL SELECT 'USER_ADDRESS_PK_SQ',          CAST(MAX(user_address_id)   AS NUMBER)            FROM user_address
      UNION ALL SELECT 'EMPLOYER_PK_SQ',       CAST(MAX(employer_id)         AS NUMBER)            FROM employer      
      UNION ALL SELECT 'HIERARCHY_PK_SQ', CAST(MAX(hierarchy_id)   AS NUMBER)            FROM hierarchy
      UNION ALL SELECT 'NODE_TYPE_PK_SQ',                CAST(MAX(node_type_id)         AS NUMBER)            FROM node_type
      UNION ALL SELECT 'NODE_PK_SQ',               CAST(MAX(node_id)        AS NUMBER)            FROM node
      UNION ALL SELECT 'NODE_CHARACTERISTIC_PK_SQ',          CAST(MAX(node_characteristic_id)   AS NUMBER)            FROM node_characteristic
      UNION ALL SELECT 'ROLE_PK_SQ',       CAST(MAX(role_id)         AS NUMBER)            FROM role      
      UNION ALL SELECT 'USER_PK_SQ', CAST(MAX(user_id)   AS NUMBER)            FROM application_user
      UNION ALL SELECT 'USER_PHONE_PK_SQ',                CAST(MAX(user_phone_id)         AS NUMBER)            FROM user_phone
      UNION ALL SELECT 'USER_CHARACTERISTIC_PK_SQ',               CAST(MAX(user_characteristic_id)        AS NUMBER)            FROM user_characteristic
      UNION ALL SELECT 'USER_NODE_PK_SQ',          CAST(MAX(user_node_id)   AS NUMBER)            FROM user_node
      UNION ALL SELECT 'USER_NODE_HISTORY_PK_SQ',       CAST(MAX(user_node_history_id)         AS NUMBER)            FROM user_node_history            
      UNION ALL SELECT 'PAX_COMM_PREFERENCE_PK_SQ', CAST(MAX(participant_comm_preference_id)   AS NUMBER)            FROM participant_comm_preference
      UNION ALL SELECT 'USER_TWITTER_PK_SQ',                CAST(MAX(twitter_id)         AS NUMBER)            FROM user_twitter
      UNION ALL SELECT 'USER_FACEBOOK_PK_SQ',               CAST(MAX(facebook_id)        AS NUMBER)            FROM user_facebook
      UNION ALL SELECT 'USER_COUNTRY_CHANGES_PK_SQ',          CAST(MAX(user_country_changes_id)   AS NUMBER)            FROM user_country_changes
      UNION ALL SELECT 'USER_EMAIL_ADDRESS_PK_SQ',       CAST(MAX(email_address_id)         AS NUMBER)            FROM user_email_address
      UNION ALL SELECT 'AUDIENCE_PK_SQ', CAST(MAX(audience_id) AS NUMBER) FROM audience
      UNION ALL SELECT 'AUDIENCE_CRITERIA_PK_SQ', CAST(MAX(audience_criteria_id) AS NUMBER) FROM audience_criteria
      UNION ALL SELECT 'AUDIENCE_CRITERIA_CHAR_PK_SQ', CAST(MAX(audience_criteria_char_id) AS NUMBER) FROM audience_criteria_char
      UNION ALL SELECT 'BUDGET_MASTER_PK_SQ', CAST(MAX(budget_master_id) AS NUMBER) FROM budget_master
      UNION ALL SELECT 'BUDGET_PK_SQ', CAST(MAX(budget_id) AS NUMBER) FROM budget
      UNION ALL SELECT 'BUDGET_TX_HISTORY_PK_SQ', CAST(MAX(budget_history_id) AS NUMBER) FROM budget_history
      UNION ALL SELECT 'ODD_CAST_CATEGORY_PK_SQ', CAST(MAX(category_id) AS NUMBER) FROM odd_cast_category
      UNION ALL SELECT 'CARD_PK_SQ', CAST(MAX(card_id) AS NUMBER) FROM card
      UNION ALL SELECT 'QUIZ_PK_SQ', CAST(MAX(quiz_id) AS NUMBER) FROM quiz
      UNION ALL SELECT 'QUIZ_QUESTION_PK_SQ', CAST(MAX(quiz_question_id) AS NUMBER) FROM quiz_question
      UNION ALL SELECT 'QUIZ_QUESTION_ANSWER_PK_SQ', CAST(MAX(quiz_question_answer_id) AS NUMBER) FROM quiz_question_answer
      UNION ALL SELECT 'CALCULATOR_PK_SQ', CAST(MAX(calculator_id) AS NUMBER) FROM calculator
      UNION ALL SELECT 'CLAIM_FORM_PK_SQ', CAST(MAX(claim_form_id) AS NUMBER) FROM claim_form
      UNION ALL SELECT 'CLAIM_FORM_STEP_PK_SQ', CAST(MAX(claim_form_step_id) AS NUMBER) FROM claim_form_step
      UNION ALL SELECT 'CLAIM_FORM_STEP_EMAIL_PK_SQ', CAST(MAX(claim_form_step_email_id) AS NUMBER) FROM claim_form_step_email
      UNION ALL SELECT 'CLAIM_FORM_STEP_ELEMENT_PK_SQ', CAST(MAX(claim_form_step_element_id) AS NUMBER) FROM claim_form_step_element
      UNION ALL SELECT 'PRODUCT_CATEGORY_PK_SQ', CAST(MAX(product_category_id) AS NUMBER) FROM product_category
      UNION ALL SELECT 'PRODUCT_PK_SQ', CAST(MAX(product_id) AS NUMBER) FROM product
      UNION ALL SELECT 'PROCESS_PK_SQ', CAST(MAX(process_id) AS NUMBER) FROM process
      UNION ALL SELECT 'PROMOTION_PK_SQ', CAST(MAX(promotion_id) AS NUMBER) FROM promotion
      UNION ALL SELECT 'PROMO_APPROVAL_OPTION_PK_SQ', CAST(MAX(promo_approval_option_id) AS NUMBER) FROM promo_approval_option
      UNION ALL SELECT 'PROMO_APPROVAL_PAX_PK_SQ', CAST(MAX(promo_approval_participant_id) AS NUMBER) FROM promo_approval_participant
      UNION ALL SELECT 'PROMO_AUDIENCE_PK_SQ', CAST(MAX(promo_audience_id) AS NUMBER) FROM promo_audience
      UNION ALL SELECT 'PROMO_CARD_PK_SQ', CAST(MAX(promo_card_id) AS NUMBER) FROM promo_card
      UNION ALL SELECT 'PROMO_CERTIFICATE_PK_SQ', CAST(MAX(promo_certificate_id) AS NUMBER) FROM promo_certificate
      UNION ALL SELECT 'PROMO_CFSE_VALIDATION_PK_SQ', CAST(MAX(promo_cfse_validation_id) AS NUMBER) FROM promo_cfse_validation      
      UNION ALL SELECT 'PROMO_MERCH_COUNTRY_PK_SQ', CAST(MAX(promo_merch_country_id) AS NUMBER) FROM promo_merch_country
      UNION ALL SELECT 'PROMO_MERCH_PROGRAM_LVL_PK_SQ', CAST(MAX(promo_merch_program_level_id) AS NUMBER) FROM promo_merch_program_level
      UNION ALL SELECT 'PROMO_SWEEPSTAKE_WINNER_PK_SQ', CAST(MAX(sweepstake_drawing_id) AS NUMBER) FROM promo_sweepstake_winners
      UNION ALL SELECT 'PROMO_HOME_PAGE_ITEM_PK_SQ', CAST(MAX(promo_home_page_item_id) AS NUMBER) FROM promo_home_page_item
      UNION ALL SELECT 'PROMO_NOTIFICATION_PK_SQ', CAST(MAX(promo_notification_id) AS NUMBER) FROM promo_notification
      UNION ALL SELECT 'PROXY_PK_SQ', CAST(MAX(proxy_id) AS NUMBER) FROM proxy
      UNION ALL SELECT 'PROXY_MODULE_PK_SQ', CAST(MAX(proxy_module_id) AS NUMBER) FROM proxy_module
      UNION ALL SELECT 'PROXY_MODULE_PROMOTION_PK_SQ', CAST(MAX(proxy_module_promotion_id) AS NUMBER) FROM proxy_module_promotion
      UNION ALL SELECT 'STACK_RANK_PK_SQ', CAST(MAX(stack_rank_id) AS NUMBER) FROM stack_rank
      UNION ALL SELECT 'STACK_RANK_NODE_PK_SQ', CAST(MAX(stack_rank_node_id) AS NUMBER) FROM stack_rank_node
      UNION ALL SELECT 'STACK_RANK_PARTICIPANT_PK_SQ', CAST(MAX(stack_rank_participant_id) AS NUMBER) FROM stack_rank_participant
      UNION ALL SELECT 'CLAIM_PK_SQ', CAST(MAX(claim_id) AS NUMBER) FROM claim      
      UNION ALL SELECT 'CLAIM_APPROVER_SNAPSHOT_PK_SQ', CAST(MAX(claim_approver_snapshot_id) AS NUMBER) FROM claim_approver_snapshot
      UNION ALL SELECT 'CLAIM_CFSE_PK_SQ', CAST(MAX(claim_cfse_id) AS NUMBER) FROM claim_cfse
      UNION ALL SELECT 'CLAIM_GROUP_PK_SQ', CAST(MAX(claim_group_id) AS NUMBER) FROM claim_group
      UNION ALL SELECT 'CLAIM_ITEM_PK_SQ', CAST(MAX(claim_item_id) AS NUMBER) FROM claim_item
      UNION ALL SELECT 'CLAIM_ITEM_APPROVER_PK_SQ', CAST(MAX(claim_item_approver_id) AS NUMBER) FROM claim_item_approver
      UNION ALL SELECT 'CLAIM_PARTICIPANT_PK_SQ', CAST(MAX(claim_participant_id) AS NUMBER) FROM claim_participant
      UNION ALL SELECT 'CLAIM_PROD_CHARACTERISTIC_SQ', CAST(MAX(claim_prod_characteristic_id) AS NUMBER) FROM claim_product_characteristic
      UNION ALL SELECT 'MERCH_ORDER_PK_SQ', CAST(MAX(merch_order_id) AS NUMBER) FROM merch_order
      UNION ALL SELECT 'SWEEPSTAKES_PARTICIPANT_PK_SQ', CAST(MAX(sweepstakes_participant_id) AS NUMBER) FROM sweepstakes_participant      
       UNION ALL SELECT 'CALCULATOR_CRITERION_PK_SQ', CAST(MAX(calculator_criterion_id) AS NUMBER) FROM calculator_criterion
      UNION ALL SELECT 'CALC_CRITERION_RATING_PK_SQ', CAST(MAX(calculator_criterion_rating_id) AS NUMBER) FROM calculator_criterion_rating
      UNION ALL SELECT 'CALCULATOR_RESPONSE_PK_SQ', CAST(MAX(calculator_response_id) AS NUMBER) FROM calculator_response
      UNION ALL SELECT 'CALCULATOR_PAYOUT_PK_SQ', CAST(MAX(calculator_payout_id) AS NUMBER) FROM calculator_payout
      UNION ALL SELECT 'SCHEDULED_RECOG_PK_SQ', CAST(MAX(scheduled_recog_id) AS NUMBER) FROM scheduled_recognition      
      UNION ALL SELECT 'PURL_RECIPIENT_PK_SQ', CAST(MAX(purl_recipient_id) AS NUMBER) FROM purl_recipient
      UNION ALL SELECT 'PURL_RECIPIENT_CFSE_PK_SQ', CAST(MAX(purl_recipient_cfse_id) AS NUMBER) FROM purl_recipient_cfse
      UNION ALL SELECT 'PURL_CONTRIBUTOR_PK_SQ', CAST(MAX(purl_contributor_id) AS NUMBER) FROM purl_contributor
      UNION ALL SELECT 'PURL_CONTRIBUTOR_COMMENT_PK_SQ', CAST(MAX(purl_contributor_comment_id) AS NUMBER) FROM purl_contributor_comment
      UNION ALL SELECT 'PURL_CONTRIBUTOR_MEDIA_PK_SQ', CAST(MAX(purl_contributor_media_id) AS NUMBER) FROM purl_contributor_media  
      UNION ALL SELECT 'PROMO_PAYOUT_GROUP_PK_SQ', CAST(MAX(promo_payout_group_id) AS NUMBER) FROM promo_payout_group
      UNION ALL SELECT 'PROMO_PAYOUT_PK_SQ', CAST(MAX(promo_payout_id) AS NUMBER) FROM promo_payout
      UNION ALL SELECT 'PROMO_TEAM_POSITION_PK_SQ', CAST(MAX(promo_team_position_id) AS NUMBER) FROM promo_team_position
      UNION ALL SELECT 'MIN_QUALIFIER_STATUS_PK_SQ', CAST(MAX(min_qualifier_status_id) AS NUMBER) FROM min_qualifier_status
      UNION ALL SELECT 'STACK_RANK_PAYOUT_GROUP_PK_SQ', CAST(MAX(stack_rank_payout_group_id) AS NUMBER) FROM stack_rank_payout_group      
      UNION ALL SELECT 'STACK_RANK_PAYOUT_PK_SQ', CAST(MAX(stack_rank_payout_id) AS NUMBER) FROM stack_rank_payout
      UNION ALL SELECT 'ACTIVITY_PK_SQ', CAST(MAX(activity_id) AS NUMBER) FROM activity
      UNION ALL SELECT 'JOURNAL_PK_SQ', CAST(MAX(journal_id) AS NUMBER) FROM journal
      UNION ALL SELECT 'PAYOUT_CALC_AUDIT_PK_SQ', CAST(MAX(payout_calculation_audit_id) AS NUMBER) FROM payout_calculation_audit       
      UNION ALL SELECT 'MOCK_ACCT_XACT_PK_SQ', CAST(MAX(account_transaction_id) AS NUMBER) FROM mock_account_transaction
      UNION ALL SELECT 'PROMO_APPRVL_OPT_REASON_PK_SQ', CAST(MAX(promo_apprvl_option_reason_id) AS NUMBER) FROM promo_approval_option_reason
      UNION ALL SELECT 'WELCOME_MESSAGE_PK_SQ', CAST(MAX(welcome_message_id) AS NUMBER) FROM welcome_message
   )
   SELECT max_seq.sequence_name,
          max_seq.max_seq_id,
          s.last_number
     FROM max_seq,
          user_sequences s
    WHERE max_seq.sequence_name = s.sequence_name
      AND max_seq.max_seq_id > s.last_number
      ;
l_val NUMBER;
l_val2 NUMBER;
BEGIN
   FOR rec IN cur_max_seq LOOP
      dbms_output.put_line('Seq: ' || rec.sequence_name);

     execute immediate
    'select ' || rec.sequence_name || '.nextval from dual' INTO l_val;

    execute immediate
    'alter sequence ' || rec.sequence_name || ' increment by -' || l_val || 
                                                          ' minvalue 0';

    execute immediate
    'alter sequence ' || rec.sequence_name || ' increment by '||rec.max_seq_id||' minvalue 0';

     execute immediate
    'select ' || rec.sequence_name || '.nextval from dual' INTO l_val;

     execute immediate
    'alter sequence ' || rec.sequence_name || ' increment by 1'||' minvalue 0';


   END LOOP; -- FOR cur_max_seq
EXCEPTION
   WHEN OTHERS THEN
     -- dbms_output.put_line('FAILURE: ' || v_sql_stmt);
      dbms_output.put_line(SQLCODE || ', ' || SQLERRM);
      RAISE;
END;
/