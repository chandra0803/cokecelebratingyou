--liquibase formatted sql

--changeset cornelius:1
--comment Add have / have not filter to report extracts - Recognition given by org
INSERT INTO REPORT_PARAMETER (REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION, COLLECTION_NAME, DEFAULT_VALUE, HIDE_SHOW_ALL_OPTION) 
SELECT REPORT_PARAMETER_PK_SQ.nextVal, (SELECT report_id from report WHERE report_code='recGivenByOrg'), 'givenType','GIVEN_TYPE_FIELD','givenTypeList','selectPicklist','picklist.given.type','EXPORT_SELECTION', (SELECT max(sequence_num)+1 from report_parameter WHERE report_id =(SELECT report_id from report WHERE report_code='recGivenByOrg')), 5661,to_date('21-JUN-17','DD-MON-RR'),5661,to_date('21-JUN-17','DD-MON-RR'),0,'givenTypeList','have', 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM REPORT_PARAMETER WHERE report_id = (SELECT report_id from report WHERE report_code='recGivenByOrg') AND PARAMETER_NAME='givenType')
--rollback DELETE report_parameter WHERE report_id = (SELECT report_id from report WHERE report_code='recGivenByOrg') AND parameter_name = 'givenType';

--changeset cornelius:2
--comment Add have / have not filter to report extracts - Recognition given by pax
INSERT INTO REPORT_PARAMETER (REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION, COLLECTION_NAME, DEFAULT_VALUE, HIDE_SHOW_ALL_OPTION) 
SELECT REPORT_PARAMETER_PK_SQ.nextVal, (SELECT report_id from report WHERE report_code='recGivenByPax'), 'givenType','GIVEN_TYPE_FIELD','givenTypeList','selectPicklist','picklist.given.type','EXPORT_SELECTION', (SELECT max(sequence_num)+1 from report_parameter WHERE report_id =(SELECT report_id from report WHERE report_code='recGivenByPax')), 5661,to_date('21-JUN-17','DD-MON-RR'),5661,to_date('21-JUN-17','DD-MON-RR'),0,'givenTypeList','have', 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM REPORT_PARAMETER WHERE report_id = (SELECT report_id from report WHERE report_code='recGivenByPax') AND PARAMETER_NAME='givenType')
--rollback DELETE report_parameter WHERE report_id = (SELECT report_id from report WHERE report_code='recGivenByPax') AND parameter_name = 'givenType';

--changeset cornelius:3
--comment Add have / have not filter to report extracts - Goalquest selection
INSERT INTO REPORT_PARAMETER (REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION, COLLECTION_NAME, DEFAULT_VALUE, HIDE_SHOW_ALL_OPTION) 
SELECT REPORT_PARAMETER_PK_SQ.nextVal, (SELECT report_id from report WHERE report_code='gcSelection'), 'hasSelected','SELECTED_TYPE_FIELD','selectedTypeList','selectPicklist','picklist.selected.type','EXPORT_SELECTION', (SELECT max(sequence_num)+1 from report_parameter WHERE report_id =(SELECT report_id from report WHERE report_code='gcSelection')), 5661,to_date('21-JUN-17','DD-MON-RR'),5661,to_date('21-JUN-17','DD-MON-RR'),0,'selectedTypeList','select', 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM REPORT_PARAMETER WHERE report_id = (SELECT report_id from report WHERE report_code='gcSelection') AND PARAMETER_NAME='hasSelected')
--rollback DELETE report_parameter WHERE report_id = (SELECT report_id from report WHERE report_code='gcSelection') AND parameter_name = 'hasSelected';

--changeset cornelius:4
--comment Add have / have not filter to report extracts - Challengepoint selection
INSERT INTO REPORT_PARAMETER (REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION, COLLECTION_NAME, DEFAULT_VALUE, HIDE_SHOW_ALL_OPTION) 
SELECT REPORT_PARAMETER_PK_SQ.nextVal, (SELECT report_id from report WHERE report_code='cpSelection'), 'hasSelected','SELECTED_TYPE_FIELD','selectedTypeList','selectPicklist','picklist.selected.type','EXPORT_SELECTION', (SELECT max(sequence_num)+1 from report_parameter WHERE report_id =(SELECT report_id from report WHERE report_code='cpSelection')), 5661,to_date('21-JUN-17','DD-MON-RR'),5661,to_date('21-JUN-17','DD-MON-RR'),0,'selectedTypeList','select', 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM REPORT_PARAMETER WHERE report_id = (SELECT report_id from report WHERE report_code='cpSelection') AND PARAMETER_NAME='hasSelected')
--rollback DELETE report_parameter WHERE report_id = (SELECT report_id from report WHERE report_code='cpSelection') AND parameter_name = 'hasSelected';

--changeset kancherla:5
--comment Add have / have not filter to report extracts - List of Nominators
INSERT INTO REPORT_PARAMETER (REPORT_PARAMETER_ID, REPORT_ID, PARAMETER_NAME, PARAMETER_CM_KEY, DATABASE_COLUMN, PARAMETER_TYPE, LIST_DEFINITION, PARAMETER_GROUP, SEQUENCE_NUM, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION, COLLECTION_NAME, DEFAULT_VALUE, HIDE_SHOW_ALL_OPTION) 
SELECT REPORT_PARAMETER_PK_SQ.nextVal, (SELECT report_id from report WHERE report_code='nomGiverSummary'), 'givenType','GIVEN_TYPE_FIELD','givenTypeList','selectPicklist','picklist.given.type','EXPORT_SELECTION', (SELECT max(sequence_num)+1 from report_parameter WHERE report_id =(SELECT report_id from report WHERE report_code='nomGiverSummary')), 5661,to_date('21-JUN-17','DD-MON-RR'),5661,to_date('21-JUN-17','DD-MON-RR'),0,'givenTypeList','have', 1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM REPORT_PARAMETER WHERE report_id = (SELECT report_id from report WHERE report_code='nomGiverSummary') AND PARAMETER_NAME='givenType')
--rollback DELETE report_parameter WHERE report_id = (SELECT report_id from report WHERE report_code='nomGiverSummary') AND parameter_name = 'givenType';
