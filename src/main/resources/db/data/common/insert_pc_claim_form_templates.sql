INSERT INTO claim_form
(CLAIM_FORM_ID,CM_ASSET_CODE,FORM_NAME,DESCRIPTION,MODULE_CODE,STATUS_CODE,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(100,'claim_form_data.claimform.100952','General Product Claim Form','Customer Name, Inv # and Date required','prd','tmpl',0,SYSDATE,null,null,0)
/
INSERT INTO claim_form
(CLAIM_FORM_ID,CM_ASSET_CODE,FORM_NAME,DESCRIPTION,MODULE_CODE,STATUS_CODE,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(101,'claim_form_data.claimform.100957','Comprehensive Product Claim Form','Multiple CIB fields, Inv # and Date required','prd','tmpl',0,SYSDATE,null,null,0)
/
INSERT INTO claim_form_step
(CLAIM_FORM_STEP_ID,CLAIM_FORM_ID,CM_KEY_FRAGMENT,SEQUENCE_NUM,IS_SALES_REQUIRED,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(100,100,'100953',0,0,0,SYSDATE,null,null,0)
/
INSERT INTO claim_form_step
(CLAIM_FORM_STEP_ID,CLAIM_FORM_ID,CM_KEY_FRAGMENT,SEQUENCE_NUM,IS_SALES_REQUIRED,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION)
VALUES
(101,101,'100958',0,0,0,SYSDATE,null,null,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(100,100,'100954','Company Name',1,'text',NULL,NULL,0,1,NULL,0,100,'text',NULL,NULL,NULL,0,NULL,0,SYSDATE,null,null,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(101,100,'100955','Invoice Number',1,'text',NULL,NULL,0,NULL,'NONE',0,25,'alpha_num',NULL,NULL,NULL,1,NULL,0,SYSDATE,NULL,NULL,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(102,100,'100956','Invoice Date',1,'date',NULL,NULL,0,NULL,'NONE',0,NULL,NULL,NULL,NULL,NULL,2,NULL,0,SYSDATE,null,null,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(103,101,'100959','Company Name',1,'text',NULL,NULL,0,1,NULL,0,100,'text',NULL,NULL,NULL,0,NULL,0,SYSDATE,null,null,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(104,101,'100960','Contact First Name',1,'text',NULL,NULL,0,2,NULL,0,100,'text',NULL,NULL,NULL,1,NULL,0,SYSDATE,null,null,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(105,101,'100961','Contact Last Name',1,'text',NULL,NULL,0,3,NULL,0,100,'text',NULL,NULL,NULL,2,NULL,0,SYSDATE,null,null,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(106,101,'100962','Main Telephone 1',0,'text',NULL,NULL,0,6,NULL,0,100,'phone',NULL,NULL,NULL,3,NULL,0,SYSDATE,null,null,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(107,101,'100963','Main Email 1',1,'text',NULL,NULL,0,8,NULL,0,100,'email',NULL,NULL,NULL,4,NULL,0,SYSDATE,null,null,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(108,101,'100964','Notes',0,'text',NULL,NULL,0,10,NULL,0,100,'text',NULL,NULL,NULL,5,NULL,0,SYSDATE,null,null,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(109,101,'100965','Address Book DropDown',0,'addr_book_sel',NULL,NULL,0,11,NULL,0,100,NULL,NULL,NULL,NULL,6,NULL,0,SYSDATE,null,null,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(110,101,'100966','Save To Address Book Option',0,'bool_checkbox',NULL,NULL,0,12,NULL,0,100,NULL,NULL,NULL,NULL,7,NULL,0,SYSDATE,null,null,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(111,101,'100967','Invoice Number',1,'text',NULL,NULL,0,NULL,'NONE',0,25,'alpha_num',NULL,NULL,NULL,8,NULL,0,SYSDATE,null,null,0,0)
/
INSERT INTO claim_form_step_element
(CLAIM_FORM_STEP_ELEMENT_ID,CLAIM_FORM_STEP_ID,CM_KEY_FRAGMENT,DESCRIPTION,IS_REQUIRED,CLAIM_FORM_ELEMENT_TYPE_CODE,NUMBER_OF_DECIMALS,NBR_INPUT_FORMAT_TYPE_CODE,MASKED_ON_ENTRY,CUSTOMER_INFORMATION_BLOCK_ID,UNIQUENESS,SHOULD_ENCRYPT,MAX_SIZE_TEXT_FIELD,TEXT_INPUT_FORMAT_TYPE_CODE,LINK_URL,DATE_START,DATE_END,SEQUENCE_NUM,SELECTION_PICK_LIST_NAME,CREATED_BY,DATE_CREATED,MODIFIED_BY,DATE_MODIFIED,VERSION,WHY_FIELD)
VALUES
(112,101,'100968','Invoice Date',1,'date',NULL,NULL,0,NULL,'NONE',0,NULL,NULL,NULL,NULL,NULL,9,NULL,0,SYSDATE,NULL,NULL,0,0)
/
INSERT INTO claim_form_step_email
(CLAIM_FORM_STEP_EMAIL_ID,CLAIM_FORM_STEP_ID,NOTIFICATION_TYPE)
VALUES
(100,100,'submitted')
/
INSERT INTO claim_form_step_email
(CLAIM_FORM_STEP_EMAIL_ID,CLAIM_FORM_STEP_ID,NOTIFICATION_TYPE)
VALUES
(101,100,'approved')
/
INSERT INTO claim_form_step_email
(CLAIM_FORM_STEP_EMAIL_ID,CLAIM_FORM_STEP_ID,NOTIFICATION_TYPE)
VALUES
(102,100,'denied')
/
INSERT INTO claim_form_step_email
(CLAIM_FORM_STEP_EMAIL_ID,CLAIM_FORM_STEP_ID,NOTIFICATION_TYPE)
VALUES
(103,101,'submitted')
/
INSERT INTO claim_form_step_email
(CLAIM_FORM_STEP_EMAIL_ID,CLAIM_FORM_STEP_ID,NOTIFICATION_TYPE)
VALUES
(104,101,'approved')
/
INSERT INTO claim_form_step_email
(CLAIM_FORM_STEP_EMAIL_ID,CLAIM_FORM_STEP_ID,NOTIFICATION_TYPE)
VALUES
(105,101,'denied')
/