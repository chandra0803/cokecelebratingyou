INSERT INTO audience
  (AUDIENCE_ID, NAME, LIST_TYPE, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
 (100, 'Davis Last Name', 'pax', 0, SYSDATE, NULL, NULL, 0)
/

INSERT INTO audience
 (AUDIENCE_ID, NAME, LIST_TYPE, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
 (101, 'Reagan Last Name', 'pax', 0, SYSDATE, NULL, NULL, 0)
/

INSERT INTO audience
 (AUDIENCE_ID, NAME, LIST_TYPE, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
 (102, 'Org Managers and Owners', 'criteria', 0, SYSDATE, NULL, NULL, 0)
/

INSERT INTO audience
 (AUDIENCE_ID, NAME, LIST_TYPE, CREATED_BY, DATE_CREATED, MODIFIED_BY, DATE_MODIFIED, VERSION)
VALUES
 (103, 'Org Members Role', 'criteria', 0, SYSDATE, NULL, NULL, 0)
/

INSERT INTO participant_audience
SELECT DISTINCT a.user_id, 100, rownum-1, 5662, SYSDATE
     FROM application_user a
     WHERE last_name like 'Davis'
/

INSERT INTO participant_audience
SELECT DISTINCT a.user_id, 101,rownum-1, 5662, SYSDATE
     FROM application_user a
     WHERE last_name like 'Reagan'
/

INSERT INTO participant_audience
SELECT DISTINCT a.user_id, 102, rownum-1, 5662, SYSDATE
     FROM application_user a, user_node n
    WHERE A.USER_ID = n.user_id AND n.role IN ('mgr', 'own')
/
    
INSERT INTO audience_criteria(audience_criteria_id, audience_id, user_node_role, include_child_nodes, exclude_include_child_nodes, created_by, date_created, version)
VALUES ( AUDIENCE_CRITERIA_PK_SQ.nextval, 102, 'mgr', 0, 0, 5662, sysdate, 0 )
/

INSERT INTO audience_criteria(audience_criteria_id, audience_id, user_node_role, include_child_nodes, exclude_include_child_nodes, created_by, date_created, version)
VALUES ( AUDIENCE_CRITERIA_PK_SQ.nextval, 102, 'own', 0, 0, 5662, sysdate, 0 )
/

INSERT INTO participant_audience
SELECT DISTINCT a.user_id, 103, rownum-1, 5662, SYSDATE
    FROM application_user a, user_node n
    WHERE A.USER_ID = n.user_id AND n.role IN ('mbr')
    AND n.role not IN ('mgr', 'own')
/

INSERT INTO audience_criteria(audience_criteria_id, audience_id, user_node_role, include_child_nodes, exclude_include_child_nodes, created_by, date_created, version)
VALUES ( AUDIENCE_CRITERIA_PK_SQ.nextval, 103, 'mbr', 0, 0, 5662, sysdate, 0 )
/
