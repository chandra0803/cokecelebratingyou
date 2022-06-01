CREATE OR REPLACE
PACKAGE pkg_query_login_activity
IS
  /******************************************************************************
  NAME:       pkg_query_login_activity
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  07/22/2014           VINAY            1) Updated p_in_department to p_in_departments and p_in_countryId to p_in_countryIds
                                        2) Updated the p_in_rowNumStart,p_in_rowNumEnd  data type from VARCHAR to NUMBER
  ******************************************************************************/
PROCEDURE prc_getMonthlyLoginBarResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultSet OUT SYS_REFCURSOR);
PROCEDURE prc_getTopPaxLoginsByName(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultSet OUT SYS_REFCURSOR);
PROCEDURE prc_getPercentageBarResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultSet OUT SYS_REFCURSOR);
PROCEDURE prc_getOrganizationBarResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultSet OUT SYS_REFCURSOR);
PROCEDURE prc_getLoginStatusChartResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultSet OUT SYS_REFCURSOR);
PROCEDURE prc_getPaxResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultsCursor OUT SYS_REFCURSOR,
    p_out_size_data          OUT    NUMBER);
PROCEDURE prc_getListOfPaxResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultsCursor OUT SYS_REFCURSOR,
    p_out_size_data          OUT    NUMBER,
    p_out_total_data OUT NUMBER);
PROCEDURE prc_getOrgTopLevelResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultsCursor OUT SYS_REFCURSOR,
    p_out_size_data          OUT    NUMBER,
    p_out_totalsCursor OUT SYS_REFCURSOR);
END pkg_query_login_activity;
/
CREATE OR REPLACE PACKAGE BODY pkg_query_login_activity
IS
PROCEDURE prc_getMonthlyLoginBarResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultSet OUT SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getMonthlyLoginBarResults' ;
  v_stage        VARCHAR2 (500);
BEGIN
  OPEN p_out_resultSet FOR SELECT month_trans.cms_name || '-' || ss.YEAR
AS
  OUT_MONTH, total_count
AS
  TOTAL_COUNT FROM
  (WITH DATA AS
  (SELECT TO_CHAR ( ADD_MONTHS ( TRUNC ( TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM - 1), 'Mon') AS month_list,
    TO_CHAR ( ADD_MONTHS ( TRUNC ( TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM       - 1), 'yy')  AS YEAR,
    ADD_MONTHS ( TRUNC (TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM                  - 1)         AS month_sort
  FROM all_objects
  WHERE ROWNUM <= MONTHS_BETWEEN ( TRUNC (TO_DATE(p_in_toDate,p_in_localeDatePattern), 'mm'), TRUNC ( TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm')) + 1
  )
SELECT DATA.month_list,
  DATA.YEAR AS YEAR,
  NVL (la.COUNT, 0) total_count
FROM
  (SELECT TO_CHAR (login_date_time, 'Mon') AS MONTH,
    TO_CHAR (login_date_time, 'yy')        AS YEAR,
    --sql.append( distinctAddOn ); Add the above distinctAddOn query here based on the condition
    CASE
      WHEN p_in_uniqueValues = 'true'
      THEN COUNT( DISTINCT la.user_id )
      ELSE COUNT( la.user_id )
    END AS COUNT
  FROM application_user app_user,
    participant P,
    rpt_participant_employer pe,
    login_activity la,
    node n,
    user_node un,
    user_address ua
    --sql.append( conditionalStatement );  Add the above conditionalStatement query here based on the condition
  WHERE
    --nodeAndBelow conditional(Sorry about the formatting)
    ((p_in_nodeAndBelow=1
  AND un.NODE_ID      IN
    (
--    SELECT node_id --Commented out as a part of performance fix.
--    FROM NODE N2
--      CONNECT BY PRIOR node_id = parent_node_id
--      START WITH node_id      IN
--      ( SELECT * FROM TABLE( get_array_varchar( parentNodeId))
--      )
      SELECT child_node_id
    FROM rpt_hierarchy_rollup N2
      WHERE node_id      IN
      ( SELECT * FROM TABLE( get_array_varchar( parentNodeId))
      )
    ))
  OR (p_in_nodeAndBelow=0
  AND n.node_id       IN
    (SELECT * FROM TABLE(get_array_varchar( parentNodeId ))
    ) ))
  AND app_user.user_id = la.user_id
  AND TRUNC (login_date_time) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
  AND un.user_id           = app_user.user_id
  AND un.is_primary        = 1
  AND ua.is_primary        = 1
  AND un.node_id           = n.node_id
  AND P.user_id            = app_user.user_id
  AND ua.user_id           = app_user.user_id
  AND app_user.user_id     = pe.user_id(+)
  AND P.status             = NVL (p_in_participantStatus, P.status)
  AND un.role              = NVL (p_in_role, un.role)
  AND ( (p_in_departments IS NULL)
  OR pe.department_type   IN
    (SELECT * FROM TABLE (get_array_varchar (p_in_departments))
    ) )
  AND ( (p_in_countryIds IS NULL)
  OR ua.country_id       IN
    (SELECT * FROM TABLE (get_array_varchar (p_in_countryIds))
    ) )
  GROUP BY TO_CHAR (login_date_time, 'Mon'),
    TO_CHAR (login_date_time, 'yy')
  ) LA,
  DATA
WHERE DATA.month_list = la.MONTH(+)
AND DATA.YEAR         = la.YEAR(+)
ORDER BY DATA.month_sort
  ) ss,
  (SELECT cms_code,
    cms_name
  FROM vw_cms_code_value vw
  WHERE asset_code = 'picklist.monthname.type.items'
  AND locale       =
    (SELECT string_val
    FROM os_propertyset
    WHERE entity_name = 'default.language'
    )
  AND NOT EXISTS
    (SELECT *
    FROM vw_cms_code_value
    WHERE asset_code = 'picklist.monthname.type.items'
    AND locale       = p_in_languageCode
    )
  UNION ALL
  SELECT cms_code,
    cms_name
  FROM vw_cms_code_value
  WHERE asset_code                               = 'picklist.monthname.type.items'
  AND locale                                     = p_in_languageCode
  ) month_trans WHERE UPPER(month_trans.cms_code)=UPPER(ss.month_list);
  p_out_return_code                             := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_resultSet FOR SELECT NULL FROM DUAL;
END prc_getMonthlyLoginBarResults;
/* getTopPaxLoginsByName */
  /******************************************************************************
  NAME:      prc_getTopPaxLoginsByName
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  08/06/2014           Suresh J        1) Bug Fix #55224 - Added User ID filter condition
  ******************************************************************************/

PROCEDURE prc_getTopPaxLoginsByName(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultSet OUT SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getTopPaxLoginsByName' ;
  v_stage        VARCHAR2 (500);
BEGIN
  OPEN p_out_resultSet FOR SELECT * FROM
  (SELECT NAME,
    total_visits
  FROM
    (SELECT last_name
      || ', '
      || first_name NAME,
      COUNT (la.user_id) total_visits
    FROM login_activity la,
      application_user au,
      user_node un,
      node n,
      rpt_participant_employer pe,
      participant P,
      user_address ua
    WHERE la.user_id(+) = au.user_id
    AND un.user_id      = au.user_id
    AND un.is_primary   = 1
    AND n.node_id       = un.node_id
    AND au.user_id      = pe.user_id(+)
    AND au.user_id      = P.user_id
    AND au.user_id      = ua.user_id
    AND ua.is_primary   = 1
    AND TRUNC (login_date_time) BETWEEN TO_DATE(p_in_fromDate, p_in_localeDatePattern) AND TO_DATE(p_in_toDate, p_in_localeDatePattern)
    AND la.user_id           = NVL (p_in_userId, la.user_id)  --08/06/2014
    AND P.status             = NVL (p_in_participantStatus, P.status)
    AND un.role              = NVL (p_in_role, un.role)
    AND ( (p_in_departments IS NULL)
    OR pe.department_type   IN
      (SELECT * FROM TABLE ( get_array_varchar (p_in_departments))
      ))
    AND ( (p_in_countryIds IS NULL)
    OR ua.country_id       IN
      (SELECT * FROM TABLE ( get_array_varchar (p_in_countryIds))
      ))
    AND un.NODE_ID IN
      (
--    SELECT node_id --Commented out as a part of performance fix.
--    FROM NODE N2
--      CONNECT BY PRIOR node_id = parent_node_id
--      START WITH node_id      IN
--      ( SELECT * FROM TABLE( get_array_varchar( parentNodeId))
--      )
      SELECT child_node_id
    FROM rpt_hierarchy_rollup N2
      WHERE node_id      IN
      ( SELECT * FROM TABLE( get_array_varchar( parentNodeId))
      )
      )
    GROUP BY n.NAME,
      au.user_id,
      last_name
      || ', '
      || first_name,
      pe.department_type,
      pe.position_type
    ORDER BY total_visits DESC,
      NAME ASC
    ) -- end SELECT
  )   -- end SELECT
  WHERE ROWNUM      <= 20;
  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_resultSet FOR SELECT NULL FROM DUAL;
END prc_getTopPaxLoginsByName;
/* getLoginPercentageBarResults */
PROCEDURE prc_getPercentageBarResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultSet OUT SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getPercentageBarResults' ;
  v_stage        VARCHAR2 (500);
BEGIN
  CASE
  WHEN p_in_nodeAndBelow = 1 THEN
    OPEN p_out_resultSet FOR SELECT * FROM
    (SELECT SRC.NAME                                                                                                                                    AS NODE_NAME,
      ROUND ( ( (SRC.CNT_HAVE_LOGGED_IN       / DECODE(SRC.ELIGIABLE_APRTICIPENTS,0,1,SRC.ELIGIABLE_APRTICIPENTS)) * 100), 2)                           AS PERCENT_LOGIN,
      ROUND ( ( ( (SRC.ELIGIABLE_APRTICIPENTS - SRC.CNT_HAVE_LOGGED_IN) / DECODE(SRC.ELIGIABLE_APRTICIPENTS,0,1,SRC.ELIGIABLE_APRTICIPENTS)) * 100), 2) AS PERCENT_NOT_LOGIN
    FROM
      (SELECT NAME,
        NVL(ELIGIABLE_APRTICIPENTS,0) ELIGIABLE_APRTICIPENTS,
        NVL(CNT_HAVE_LOGGED_IN,0) CNT_HAVE_LOGGED_IN,
        NVL(CNT_VISITS,0) CNT_VISITS
      FROM node n,
        (WITH result AS
        (SELECT N.NODE_ID,
          COUNT (DISTINCT UN.USER_ID) AS ELIGIABLE_APRTICIPENTS,
          COUNT (DISTINCT LA.USER_ID) AS CNT_HAVE_LOGGED_IN,
          COUNT (LA.USER_ID)          AS CNT_VISITS
        FROM NODE N,
          USER_NODE UN,
          PARTICIPANT P,
          (SELECT *
          FROM LOGIN_ACTIVITY
          WHERE TRUNC (NVL (login_date_time, SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern )
          ) LA,
          vw_curr_pax_employer PE,
          user_address ua
        WHERE un.NODE_ID        = n.node_id
        AND la.user_id(+)       = un.user_id
        AND un.is_primary       = 1
        AND un.user_id          = P.user_id
        AND P.user_id           = pe.user_id (+)
        AND P.user_id           = ua.user_id
        AND P.status            = NVL (p_in_participantStatus, P.status)
        AND un.role             = NVL (p_in_role, un.role)
        AND ((p_in_departments IS NULL)
        OR pe.department_type  IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_departments))
          ))
        AND ((p_in_countryIds IS NULL)
        OR ua.country_id      IN
          (SELECT * FROM TABLE(get_array_varchar(p_in_countryIds))
          ))
        GROUP BY n.node_id
        )
      SELECT nl.node_id,
        SUM(ELIGIABLE_APRTICIPENTS)ELIGIABLE_APRTICIPENTS,
        SUM(CNT_HAVE_LOGGED_IN) CNT_HAVE_LOGGED_IN,
        SUM(CNT_VISITS) CNT_VISITS
      FROM
        (select * from rpt_hierarchy_rollup
--        Commented out for performance fix.
--        SELECT pv.COLUMN_VALUE AS node_id,
--          np.node_id            AS child_node_id
--        FROM
--          (SELECT h.node_id,
--            LEVEL hier_level,
--            SYS_CONNECT_BY_PATH(node_id, '/')
--            || '/' AS node_path
--          FROM node h
--            START WITH h.parent_node_id IS NULL
--            CONNECT BY PRIOR h.node_id   = h.parent_node_id
--          ) np,
--          TABLE( CAST( MULTISET
--          (SELECT TO_NUMBER( SUBSTR(np.node_path, INSTR(np.node_path, '/', 1, LEVEL)+1, INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 ) )
--          FROM dual
--            CONNECT BY LEVEL <= np.hier_level
--          ) AS sys.odcinumberlist ) ) pv
        ) nl,
        result r
      WHERE nl.child_node_id = r.node_id
      GROUP BY nl.node_id
        ) nhr
      WHERE nhr.node_id(+)  = n.node_id
      AND n.parent_node_id IN
        (SELECT * FROM TABLE(get_array_varchar(parentNodeId))
        )
      ) SRC
    UNION ALL
    SELECT SRC.NAME
      || ' Team'                                                                                                                                    AS NODE_NAME,
      ROUND ( ( (SRC.logged_in_cnt           / DECODE (SRC.eligible_participants, 0, 1,SRC.eligible_participants)) * 100), 2)                       AS PERCENT_LOGIN,
      ROUND ( ( ( (SRC.eligible_participants - SRC.logged_in_cnt) / DECODE (SRC.eligible_participants, 0, 1, SRC.eligible_participants)) * 100), 2) AS PERCENT_NOT_LOGIN
    FROM
      (SELECT NODE.NODE_ID AS NODE_ID,
        NODE.NAME,
        COUNT (DISTINCT UN.USER_ID) AS eligible_participants,
        COUNT (DISTINCT LA.USER_ID) AS logged_in_cnt,
        COUNT (LA.USER_ID)          AS CNT_VISITS
      FROM NODE,
        USER_NODE UN,
        (SELECT USER_ID
        FROM LOGIN_ACTIVITY
        WHERE TRUNC (login_date_time) BETWEEN TO_DATE ( p_in_fromDate, p_in_localeDatePattern) AND TO_DATE ( p_in_toDate, p_in_localeDatePattern)
        ) LA,
        PARTICIPANT P,
        rpt_participant_employer PE
      WHERE node.NODE_ID IN
        (SELECT           *
        FROM TABLE ( get_array_varchar ( parentNodeId))
        )
      AND un.user_id           = la.user_id(+)
      AND un.is_primary        = 1
      AND un.node_id           = node.node_id(+)
      AND un.user_id           = P.user_id
      AND P.user_id            = pe.user_id(+)
      AND P.status             = NVL (p_in_participantStatus, P.status)
      AND un.role              = NVL (p_in_role, un.role)
      AND ( (p_in_departments IS NULL)
      OR pe.department_type   IN
        (SELECT * FROM TABLE ( get_array_varchar ( p_in_departments))
        ))
      GROUP BY NODE.NAME,
        NODE.NODE_ID
      ) SRC
    ) --Closes the first select that was wrapped in parens
    ORDER BY PERCENT_LOGIN DESC;
    --END WHEN CLAUSE
  ELSE
    OPEN p_out_resultSet FOR SELECT SRC.NAME || ' Team'
  AS
    NODE_NAME, ROUND ( ( (SRC.logged_in_cnt / DECODE (SRC.eligible_participants, 0, 1,SRC.eligible_participants)) * 100), 2)
  AS
    PERCENT_LOGIN, ROUND ( ( ( (SRC.eligible_participants - SRC.logged_in_cnt) / DECODE (SRC.eligible_participants, 0, 1, SRC.eligible_participants)) * 100), 2)
  AS
    PERCENT_NOT_LOGIN FROM
    (SELECT NODE.NODE_ID AS NODE_ID,
      NODE.NAME,
      COUNT (DISTINCT UN.USER_ID) AS eligible_participants,
      COUNT (DISTINCT LA.USER_ID) AS logged_in_cnt,
      COUNT (LA.USER_ID)          AS CNT_VISITS
    FROM NODE,
      USER_NODE UN,
      (SELECT USER_ID
      FROM LOGIN_ACTIVITY
      WHERE TRUNC (login_date_time) BETWEEN TO_DATE ( p_in_fromDate, p_in_localeDatePattern) AND TO_DATE ( p_in_toDate, p_in_localeDatePattern)
      ) LA,
      PARTICIPANT P,
      rpt_participant_employer PE
    WHERE node.NODE_ID IN
      (SELECT           *
      FROM TABLE ( get_array_varchar ( parentNodeId))
      )
    AND un.user_id           = la.user_id(+)
    AND un.is_primary        = 1
    AND un.node_id           = node.node_id(+)
    AND un.user_id           = P.user_id
    AND P.user_id            = pe.user_id(+)
    AND P.status             = NVL (p_in_participantStatus, P.status)
    AND un.role              = NVL (p_in_role, un.role)
    AND ( (p_in_departments IS NULL)
    OR pe.department_type   IN
      (SELECT * FROM TABLE ( get_array_varchar ( p_in_departments))
      ))
    GROUP BY NODE.NAME,
      NODE.NODE_ID
    ) SRC ORDER BY PERCENT_LOGIN DESC;
  END CASE;--case statement
  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_resultSet FOR SELECT NULL FROM DUAL;
END prc_getPercentageBarResults;
/* getOrganizationBarResults */
PROCEDURE prc_getOrganizationBarResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultSet OUT SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getOrganizationBarResults' ;
  v_stage        VARCHAR2 (500);
BEGIN
  CASE
  WHEN p_in_nodeAndBelow = 1 THEN
    --nodeAndBelow
    OPEN p_out_resultSet FOR SELECT R.* FROM
    (SELECT RS.NAME AS NAME,
      RS.CNT_VISITS AS TOTAL_COUNT
    FROM
      (SELECT NODE.NODE_ID AS NODE_ID,
        NODE.NAME
        || ' Team' NAME,
        COUNT (LA.USER_ID) AS CNT_VISITS
      FROM NODE,
        USER_NODE UN,
        (SELECT USER_ID
        FROM LOGIN_ACTIVITY
        WHERE TRUNC (login_date_time) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        ) LA,
        PARTICIPANT P,
        rpt_participant_employer PE
      WHERE node.NODE_ID IN
        (SELECT           *
        FROM TABLE ( get_array_varchar (parentNodeId))
        )
      AND un.user_id           = la.user_id(+)
      AND un.is_primary        = 1
      AND un.node_id           = node.node_id(+)
      AND un.user_id           = P.user_id
      AND P.user_id            = pe.user_id(+)
      AND P.status             = NVL (p_in_participantStatus, P.status)
      AND un.role              = NVL (p_in_role, un.role)
      AND ( (p_in_departments IS NULL)
      OR pe.department_type   IN
        (SELECT * FROM TABLE ( get_array_varchar ( p_in_departments))
        ))
      GROUP BY NODE.NAME,
        NODE.NODE_ID
      UNION ALL
      SELECT dtl.node_id,
        dtl.node_name AS NAME,
        cnt_visits
      FROM
        (SELECT NODE_ID,
          PARENT_NODE_ID
        FROM node
        WHERE parent_node_id IN
          (SELECT * FROM TABLE ( get_array_varchar (parentNodeId))
          )
        ) raD,
        (SELECT node_id,
          NAME node_name,
          cnt_visits
        FROM node rh,
          (SELECT npn.path_node_id,
            NVL(SUM (cnt_visits),0) cnt_visits
          FROM
            (
--            SELECT np.node_id, ************Commented out for performance fix.
--              P.COLUMN_VALUE AS path_node_id
--            FROM
--              (SELECT
--                /*+ FIRST_ROWS */
--                h.node_id,
--                LEVEL hier_level,
--                SYS_CONNECT_BY_PATH ( node_id, '/')
--                || '/' AS node_path
--              FROM node h
--                START WITH h.parent_node_id IS NULL
--                CONNECT BY PRIOR h.node_id   = h.parent_node_id
--              ORDER BY H.NODE_ID
--              ) np,
--              TABLE ( CAST ( MULTISET
--              (SELECT TO_NUMBER ( SUBSTR ( np.node_path, INSTR ( np.node_path, '/', 1, LEVEL) + 1, INSTR ( np.node_path, '/', 1, LEVEL + 1) - INSTR ( np.node_path, '/', 1, LEVEL) - 1))
--              FROM DUAL
--                CONNECT BY LEVEL <= np.hier_level
--              ) AS SYS.odcinumberlist)) P
--            
              select node_id path_node_id,child_node_id node_id from rpt_hierarchy_rollup
            ) npn,
            (SELECT node_id,
              NVL(COUNT (p2.user_id),0) elg_pax
            FROM rpt_participant_employer vue,
              participant p2,
              user_node un
            WHERE p2.user_id         = vue.user_id(+)
            AND p2.status            = NVL ( p_in_participantStatus, p2.status)
            AND un.role              = NVL (p_in_role, un.role)
            AND p2.user_id           = un.user_id
            AND un.is_primary        = 1
            AND ( (p_in_departments IS NULL)
            OR (vue.department_type IN
              (SELECT * FROM TABLE ( get_array_varchar ( p_in_departments))
              )))
            GROUP BY node_id
            ) elg,
            (SELECT un.node_id,
              NVL(COUNT(la.user_id),0) cnt_visits
            FROM login_activity la,
              user_node un,
              participant P,
              rpt_participant_employer pe
            WHERE la.user_id         = un.user_id
            AND un.is_primary        = 1
            AND un.user_id           = P.user_id
            AND P.user_id            = pe.user_id(+)
            AND P.status             = NVL (p_in_participantStatus, P.status)
            AND un.role              = NVL (p_in_role, un.role)
            AND (pe.department_type IN
              (SELECT * FROM TABLE(get_array_varchar(p_in_departments))
              )
            OR (p_in_departments IS NULL))
            AND TRUNC(la.login_date_time) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            GROUP BY un.node_id
            ) v
          WHERE elg.node_id(+) = npn.node_id
          AND v.node_id(+)     = npn.node_id
          GROUP BY npn.path_node_id
          )
        WHERE rh.node_id = path_node_id
        ) dtl
      WHERE raD.node_id                = dtl.node_id
      AND NVL (raD.PARENT_NODE_ID, 0) IN
        (SELECT * FROM TABLE ( get_array_varchar (NVL (parentNodeId, 0)))
        )
      ) RS
    ) R ORDER BY TOTAL_COUNT DESC;
  ELSE
    OPEN p_out_resultSet FOR SELECT R.* FROM
    (SELECT RS.NAME AS NAME,
      RS.CNT_VISITS AS TOTAL_COUNT
    FROM
      (SELECT NODE.NODE_ID AS NODE_ID,
        NODE.NAME
        || ' Team' NAME,
        COUNT (LA.USER_ID) AS CNT_VISITS
      FROM NODE,
        USER_NODE UN,
        (SELECT USER_ID
        FROM LOGIN_ACTIVITY
        WHERE TRUNC (login_date_time) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        ) LA,
        PARTICIPANT P,
        rpt_participant_employer PE
      WHERE node.NODE_ID IN
        (SELECT           *
        FROM TABLE ( get_array_varchar (parentNodeId))
        )
      AND un.user_id           = la.user_id(+)
      AND un.is_primary        = 1
      AND un.node_id           = node.node_id(+)
      AND un.user_id           = P.user_id
      AND P.user_id            = pe.user_id(+)
      AND P.status             = NVL (p_in_participantStatus, P.status)
      AND un.role              = NVL (p_in_role, un.role)
      AND ( (p_in_departments IS NULL)
      OR pe.department_type   IN
        (SELECT * FROM TABLE ( get_array_varchar ( p_in_departments))
        ))
      GROUP BY NODE.NAME,
        NODE.NODE_ID
      ) RS
    ) R ORDER BY TOTAL_COUNT DESC;
  END CASE;
  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_resultSet FOR SELECT NULL FROM DUAL;
END prc_getOrganizationBarResults;
/* getLoginStatusChartResults */
PROCEDURE prc_getLoginStatusChartResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultSet OUT SYS_REFCURSOR)
IS
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getLoginStatusChartResults' ;
  v_stage        VARCHAR2 (500);
  l_query        VARCHAR2 (32767);
BEGIN
l_query := 'SELECT ';

l_query :=
            l_query
         || '  '
         || ' LOGGED_IN, (app_user.TOTAL_COUNT - la.LOGGED_IN) NOT_LOGGED_IN FROM
  (SELECT COUNT (                                                  *) TOTAL_COUNT
  FROM application_user au,
    user_node un,
    participant p,
    rpt_participant_employer pe,
    user_address ua
  WHERE au.user_id         = un.user_id
  AND un.is_primary        = 1
  AND au.user_id           = p.user_id
  AND p.user_id            = pe.user_id(+)
  AND ua.user_id           = au.user_id
  AND ua.is_primary        = 1
  AND p.status             = NVL ('''||p_in_participantStatus||''', p.status)
  AND un.role              = NVL ('''||p_in_role||''', un.role)
  AND ( ('''||p_in_departments||''' IS NULL)
  OR pe.department_type   IN
    (SELECT * FROM TABLE (get_array_varchar ('''||p_in_departments||'''))
    ))
  AND ( ('''||p_in_countryIds||''' IS NULL)
  OR ua.country_id       IN
    (SELECT               *
    FROM TABLE (get_array_varchar ('''||p_in_countryIds||'''))
    ))
      AND ( (('''||p_in_nodeAndBelow||''' = 1 )
  AND (un.NODE_ID          IN
    (
      SELECT child_node_id
    FROM rpt_hierarchy_rollup N2
      WHERE node_id      IN
      ( SELECT * FROM TABLE( get_array_varchar( '''||parentNodeId||'''))
      )   
    )))
  OR (('''||p_in_nodeAndBelow||''' = 0 )
  AND un.node_id        IN
    (SELECT * FROM TABLE(get_array_varchar( '''||parentNodeId||''' ) )
    )) )
  ) app_user,
  (SELECT COUNT (DISTINCT p.user_id) logged_in
  FROM application_user au,
    user_node un,
    participant p,
    rpt_participant_employer pe,
    user_address ua
  WHERE au.user_id         = un.user_id
  AND un.is_primary        = 1
  AND au.user_id           = p.user_id
  AND p.user_id            = pe.user_id(+)
  AND ua.user_id           = au.user_id
  AND ua.is_primary        = 1
  AND p.status             = NVL ('''||p_in_participantStatus||''', p.status)
  AND un.role              = NVL ('''||p_in_role||''', un.role)
  AND ( ('''||p_in_departments||''' IS NULL)
  OR pe.department_type   IN
    (SELECT * FROM TABLE (get_array_varchar ('''||p_in_departments||'''))
    ))
  AND ( ('''||p_in_countryIds||''' IS NULL)
  OR ua.country_id       IN
    (SELECT               *
    FROM TABLE (get_array_varchar ('''||p_in_countryIds||'''))
    ))
      AND ( (('''||p_in_nodeAndBelow||''' = 1 )
  AND (un.NODE_ID          IN
    (
      SELECT child_node_id
    FROM rpt_hierarchy_rollup N2
      WHERE node_id      IN
      ( SELECT * FROM TABLE( get_array_varchar( '''||parentNodeId||'''))
      )
    )))
  OR (('''||p_in_nodeAndBelow||''' = 0 )
  AND un.node_id        IN
    (SELECT * FROM TABLE(get_array_varchar( '''||parentNodeId||''' ) )
    )) )
  AND EXISTS
    (SELECT *
    FROM login_activity
    WHERE TRUNC (login_date_time) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''', '''||p_in_localeDatePattern||''')
    AND user_id = au.user_id
    )
  ) la';
  
  OPEN p_out_resultset FOR l_query;
  
  DBMS_OUTPUT.PUT_LINE (l_query);
  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_resultSet FOR SELECT NULL FROM DUAL;
END prc_getLoginStatusChartResults;
/* Formatted on 7/7/2014 3:41:00 PM (QP5 v5.252.13127.32847) */
/* getParticipantLogonActivityResults */
PROCEDURE prc_getPaxResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultsCursor OUT SYS_REFCURSOR,
    p_out_size_data          OUT    NUMBER)
IS
      /******************************************************************************
  NAME:       prc_get_byorgsummaryResults
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  ******************************************************************************/

  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getPaxResults' ;
  v_stage        VARCHAR2 (500);
   v_sortCol             VARCHAR2(200);
     l_query VARCHAR2(32767);
BEGIN

 l_query  := 'SELECT * FROM ( ';

    v_sortCol := ' '|| p_in_sortedOn || ' ' || p_in_sortedBy;

    l_query := l_query ||'  '||'SELECT ROWNUM                AS RN,
    user_info.participant_name  AS pax_name,
    user_info.department        AS pax_department,
    user_info.position          AS pax_position,
    user_info.pax_country       AS pax_country,
    user_info.organization_name AS organization_name,
    TRUNC (la.login_date_time)  AS login_date
  FROM login_activity la,
    (SELECT au.user_id,
      au.last_name
      || '', ''
      || au.first_name                                                                                                       AS PARTICIPANT_NAME,
      fnc_cms_picklist_value (''picklist.department.type.items'', pe.department_type, '''||p_in_languageCode||''')                       AS department,
      fnc_cms_picklist_value (''picklist.positiontype.items'', pe.position_type, '''||p_in_languageCode||''')                            AS position,
      DECODE ( c.cm_asset_code, NULL, NULL, fnc_cms_asset_code_val_extr (c.cm_asset_code, c.name_cm_key, '''||p_in_languageCode||''')) AS PAX_COUNTRY,
      n.name                                                                                                                 as organization_name
    FROM application_user au,
      user_node un,
      node n,
      user_address ua,
      country c,
      rpt_participant_employer pe
    WHERE un.user_id  = au.user_id
    AND un.node_id    = n.node_id
    AND un.is_primary = 1
    AND au.user_id    = ua.user_id
    AND ua.is_primary = 1
    AND au.user_id    = pe.user_id(+)
    AND ua.country_id = c.country_id
    AND au.user_id    = '''||p_in_userId||'''
    ) user_info
  WHERE la.user_id= user_info.user_id
  AND la.user_id  = '''||p_in_userId||'''
  AND TRUNC (la.login_date_time) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
  ORDER BY '|| v_sortCol ||'  )  WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd;
 prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || l_query, NULL);
  --RESULTS
  OPEN p_out_resultsCursor FOR
   l_query;
  --SIZE
  SELECT COUNT (1) INTO p_out_size_data FROM
  (SELECT au.last_name
    ||', '
    || au.first_name          AS PARTICIPANT_NAME,
    pe.department_type        AS department,
    pe.position_type          AS position,
    c.cm_asset_code           AS PAX_COUNTRY,
    N.NAME                    AS ORGANIZATION_NAME,
    TRUNC(LA.login_date_time) AS LOGIN_DATE
  FROM APPLICATION_USER au,
    LOGIN_ACTIVITY la,
    USER_NODE un,
    NODE n,
    USER_ADDRESS ua,
    COUNTRY c,
    rpt_participant_employer pe
  WHERE la.user_id  = au.user_id
  AND un.user_id    = au.user_id
  AND un.is_primary = 1
  AND un.node_id    = n.node_id
  AND au.user_id    = ua.user_id
  AND ua.is_primary = 1
  AND au.user_id    = pe.user_id (+)
  AND ua.country_id = c.country_id
  AND au.user_id    = p_in_userId
  AND TRUNC(login_date_time) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
  AND ((p_in_departments IS NULL)
  OR pe.department_type  IN
    (SELECT * FROM TABLE(get_array_varchar(p_in_departments))
    ))
  AND ((p_in_countryIds IS NULL)
  OR ua.country_id      IN
    (SELECT * FROM TABLE(get_array_varchar(p_in_countryIds))
    ))
  ORDER BY la.login_date_time
  );
  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_resultsCursor FOR SELECT NULL FROM DUAL;
  p_out_size_data := NULL;
END prc_getPaxResults;
/* Formatted on 7/8/2014 11:10:02 AM (QP5 v5.252.13127.32847) */
/* getLoginListOfParticipantsResults */
PROCEDURE prc_getListOfPaxResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultsCursor OUT SYS_REFCURSOR,
    p_out_size_data          OUT    NUMBER,
    p_out_total_data OUT NUMBER)
IS
   /******************************************************************************
  NAME:       prc_get_byorgsummaryResults
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  ******************************************************************************/

  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getListOfPaxResults' ;
  v_stage        VARCHAR2 (500);
  v_sortCol             VARCHAR2(200);
     l_query VARCHAR2(32767);
BEGIN

l_query  := 'SELECT * FROM ( ';

    v_sortCol := ' '|| p_in_sortedOn || ' ' || p_in_sortedBy;

    l_query := l_query ||'  '||'SELECT org    AS org,
    userId       AS userId,
    name         AS pax_name,
    department   AS department,
    position     AS pax_position,
    last_login   AS last_login,
    total_visits AS total_visits,
    ROWNUM rn
  FROM
    (SELECT au.user_id userId,
      n.name org,
      last_name
      || '', ''
      || first_name name,
      dpt.cms_name department,
      j.cms_name position,
      MAX (TRUNC (la.login_date_time)) last_login,
      COUNT (la.user_id) total_visits
    FROM login_activity la,
      application_user au,
      user_node un,
      node n,
      rpt_participant_employer pe,
      participant p,
      user_address ua,
      (SELECT cms_code,
        cms_name
      FROM vw_cms_code_value vw
      WHERE asset_code = ''picklist.department.type.items''
      AND locale       =
        (SELECT string_val
        FROM os_propertyset
        WHERE entity_name = ''default.language''
        )
      AND NOT EXISTS
        (SELECT *
        FROM vw_cms_code_value
        WHERE asset_code = ''picklist.department.type.items''
        AND locale       = '''||p_in_languageCode||'''
        )
      UNION ALL
      SELECT cms_code,
        cms_name
      FROM vw_cms_code_value
      WHERE asset_code = ''picklist.department.type.items''
      AND locale       ='''||p_in_languageCode||'''
      ) dpt,
      (SELECT cms_code,
        cms_name
      FROM vw_cms_code_value vw
      WHERE asset_code = ''picklist.positiontype.items''
      AND locale       =
        (SELECT string_val
        FROM os_propertyset
        WHERE entity_name = ''default.language''
        )
      AND NOT EXISTS
        (SELECT *
        FROM vw_cms_code_value
        WHERE asset_code = ''picklist.positiontype.items''
        AND locale       = '''||p_in_languageCode||'''
        )
      UNION ALL
      SELECT cms_code,
        cms_name
      FROM vw_cms_code_value
      WHERE asset_code = ''picklist.positiontype.items''
      AND locale       = '''||p_in_languageCode||'''
      ) j
    WHERE la.user_id  = au.user_id
    AND un.user_id    = au.user_id
    AND un.is_primary = 1
    AND n.node_id     = un.node_id
    AND au.user_id    = pe.user_id(+)
    AND au.user_id    = p.user_id
    AND au.user_id    = ua.user_id
    AND ua.is_primary = 1
    AND TRUNC (login_date_time) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
    AND p.status             = NVL ('''||p_in_participantStatus||''', p.status)
    AND un.role              = NVL ('''||p_in_role||''', un.role)
    AND ( ('''||p_in_departments||''' IS NULL)
    OR pe.department_type   IN
      (SELECT * FROM TABLE ( get_array_varchar ('''||p_in_departments||'''))
      ))
    AND ( ('''||p_in_countryIds||''' IS NULL)
    OR ua.country_id       IN
      (SELECT               *
      FROM TABLE ( get_array_varchar ('''||p_in_countryIds||'''))
      ))
      /* sql.append( conditionalStatement ); Replace the above conditional statement query based on condition */
    AND( ('''||p_in_nodeAndBelow||''' = 1
    AND un.NODE_ID         IN
      ( 
      SELECT child_node_id
    FROM rpt_hierarchy_rollup N2
      WHERE node_id      IN
      ( SELECT * FROM TABLE( get_array_varchar( '''||parentNodeId||'''))
      )        
      ))
    OR ('''||p_in_nodeAndBelow||''' = 0
    AND n.node_id        IN
      (SELECT * FROM TABLE(get_array_varchar( '''||parentNodeId||''' ) )
      )))
    AND dpt.cms_code(+) = pe.department_type
    AND j.cms_code(+)   = pe.position_type
    GROUP BY n.name,
      au.user_id,
      last_name
      || '', ''
      || first_name,
      dpt.cms_name,
      j.cms_name
    ORDER BY  '|| v_sortCol ||'
    )
  ) WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd;
  --RESULTS
  OPEN p_out_resultsCursor FOR
   l_query;

  --SIZE
  SELECT COUNT (1) INTO p_out_size_data FROM
  (SELECT au.user_name,
    last_name
    || ', '
    || first_name NAME,
    pe.department_type department,
    pe.position_type position
  FROM login_activity la,
    application_user au,
    user_node un,
    rpt_participant_employer pe,
    participant P,
    user_address ua
  WHERE la.user_id  = au.user_id
  AND un.user_id    = au.user_id
  AND un.is_primary = 1
  AND au.user_id    = pe.user_id (+)
  AND au.user_id    = P.user_id
  AND au.user_id    = ua.user_id
  AND ua.is_primary = 1
  AND TRUNC(login_date_time) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
  AND P.status            = NVL(p_in_participantStatus, P.status)
  AND un.role             = NVL(p_in_role, un.role)
  AND ((p_in_departments IS NULL)
  OR pe.department_type  IN
    (SELECT * FROM TABLE(get_array_varchar(p_in_departments))
    ))
  AND ((p_in_countryIds IS NULL)
  OR ua.country_id      IN
    (SELECT              *
    FROM TABLE(get_array_varchar(p_in_countryIds))
    ))
  AND( (p_in_nodeAndBelow = 1
  AND un.NODE_ID         IN
    (
    --    SELECT node_id --Commented out as a part of performance fix.
--    FROM NODE N2
--      CONNECT BY PRIOR node_id = parent_node_id
--      START WITH node_id      IN
--      ( SELECT * FROM TABLE( get_array_varchar( parentNodeId))
--      )
      SELECT child_node_id
    FROM rpt_hierarchy_rollup N2
      WHERE node_id      IN
      ( SELECT * FROM TABLE( get_array_varchar( parentNodeId))
      )
    ))
  OR (p_in_nodeAndBelow = 0
  AND un.node_id       IN
    (SELECT * FROM TABLE(get_array_varchar( parentNodeId ) )
    )))
    /* sql.append( conditionalStatement ); Replace the above conditional statement query based on condition */
  GROUP BY au.user_name,
    last_name
    || ', '
    || first_name,
    pe.department_type,
    pe.position_type
  );
  --TOTALS
  SELECT COUNT(la.user_id) INTO p_out_total_data FROM login_activity la, application_user au, user_node un, node n, rpt_participant_employer pe, participant P, user_address ua WHERE la.user_id = au.user_id AND un.user_id = au.user_id AND un.is_primary = 1 AND n.node_id = un.node_id AND au.user_id = pe.user_id (+) AND au.user_id = P.user_id AND au.user_id = ua.user_id AND ua.is_primary = 1 AND TRUNC(login_date_time) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern) AND P.status = NVL(p_in_participantStatus, P.status) AND un.role = NVL(p_in_role, un.role) AND ((p_in_departments IS NULL) OR pe.department_type IN
    (SELECT * FROM TABLE(get_array_varchar(p_in_departments))
    )) AND ((p_in_countryIds IS NULL) OR ua.country_id IN
    (SELECT                                             *
    FROM TABLE(get_array_varchar(p_in_countryIds))
    )) AND( (p_in_nodeAndBelow = 1 AND un.NODE_ID IN
      (
      --    SELECT node_id --Commented out as a part of performance fix.
--    FROM NODE N2
--      CONNECT BY PRIOR node_id = parent_node_id
--      START WITH node_id      IN
--      ( SELECT * FROM TABLE( get_array_varchar( parentNodeId))
--      )
      SELECT child_node_id
    FROM rpt_hierarchy_rollup N2
      WHERE node_id      IN
      ( SELECT * FROM TABLE( get_array_varchar( parentNodeId))
      )
      )) OR (p_in_nodeAndBelow = 0 AND n.node_id IN
      (SELECT * FROM TABLE(get_array_varchar( parentNodeId ) )
      )));
  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_resultsCursor FOR SELECT NULL FROM DUAL;
  p_out_size_data := NULL;
  p_out_total_data := NULL;
  END prc_getListOfPaxResults;
/* getOrgLoginActivityTopLevelResults */
PROCEDURE prc_getOrgTopLevelResults(
    parentNodeId           IN VARCHAR,
    p_in_uniqueValues      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_role              IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_languageCode      IN VARCHAR,
    p_in_countryIds        IN VARCHAR,
    p_in_userId            IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_resultsCursor OUT SYS_REFCURSOR,
    p_out_size_data          OUT    NUMBER,
    p_out_totalsCursor OUT SYS_REFCURSOR)
IS
   /******************************************************************************
  NAME:       prc_get_byorgsummaryResults
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Ravi Dhanekula       11/02/2016           Bug # 69893.
  ******************************************************************************/
       c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getOrgTopLevelResults' ;
  v_stage        VARCHAR2 (500);
  v_sortCol             VARCHAR2(200);
  l_query VARCHAR2(32767);
BEGIN

 l_query  := 'SELECT * FROM
  (SELECT ROWNUM RN,
    R.*
  FROM
    ( ';

    v_sortCol := ' '|| p_in_sortedOn || ' ' || p_in_sortedBy;

    l_query := l_query ||'  '||'SELECT rs.node_id         as node_id,
      rs.name                  as name,
      RS.eligible_participants AS eligible_pax,
      RS.logged_in_cnt         AS logged_in_cnt,
      CASE
        WHEN (logged_in_cnt      = 0
        OR eligible_participants = 0)
        THEN 0
        ELSE ROUND ( (logged_in_cnt / eligible_participants) * 100, 2)
      END percent_login,
      (eligible_participants - logged_in_cnt) not_logged_in_cnt,
      CASE
        WHEN eligible_participants = 0
        THEN 0
        ELSE ROUND ( ( (eligible_participants - logged_in_cnt) * 100 / eligible_participants), 2)
      END percent_not_login,
      RS.CNT_VISITS AS TOTAL_VISITS
    FROM
      (SELECT NODE.NODE_ID AS NODE_ID,
        NODE.NAME
        || '' Team'' NAME,
        COUNT (DISTINCT UN.USER_ID) AS eligible_participants,
        COUNT (DISTINCT LA.USER_ID) AS logged_in_cnt,
        COUNT (LA.USER_ID)          AS CNT_VISITS
      FROM NODE,
        USER_NODE UN,
        (SELECT USER_ID
        FROM LOGIN_ACTIVITY
        WHERE TRUNC (login_date_time) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
        ) LA,
        participant p,
        rpt_participant_employer pe
      WHERE node.node_id IN
        (SELECT           *
        FROM TABLE ( get_array_varchar ('''||parentNodeId||''' ))
        )
      AND un.user_id           = la.user_id(+)
      AND un.is_primary        = 1
      AND un.node_id           = node.node_id(+)
      AND un.user_id           = p.user_id
      AND p.user_id            = pe.user_id(+)
      AND p.status             = NVL ('''||p_in_participantStatus||''', p.status)
      AND un.role              = NVL ('''||p_in_role||''', un.role)
      AND ( ('''||p_in_departments||''' IS NULL)
      OR pe.department_type   IN
        (SELECT * FROM TABLE ( get_array_varchar ('''||p_in_departments||''' ))
        ))
      GROUP BY NODE.NAME,
        NODE.NODE_ID
      UNION ALL
      SELECT dtl.node_id,
        dtl.node_name AS NAME,
        eligible_participants,
        logged_in_cnt,
        cnt_visits
      FROM
        (SELECT NODE_ID,
          PARENT_NODE_ID
        FROM node
        WHERE parent_node_id IN
          (SELECT * FROM TABLE ( get_array_varchar ('''||parentNodeId||''' ))
          )
        ) raD,
        (SELECT node_id,
          name node_name,
          have_logged_in logged_in_cnt,
          elg_pax eligible_participants,
          cnt_visits
        FROM node rh,
          (SELECT npn.path_node_id,
            NVL(SUM (have_logged_in),0) have_logged_in,
            NVL(SUM (elg_pax),0) elg_pax,
            NVL(SUM (cnt_visits),0) cnt_visits
          FROM
            (
            select node_id path_node_id,child_node_id node_id from rpt_hierarchy_rollup
            ) npn,
            (SELECT un.node_id,
              NVL(COUNT(DISTINCT un.user_id),0) have_logged_in
            FROM user_node un,
              participant p,
              rpt_participant_employer pe
            WHERE un.is_primary      = 1
            AND un.user_id           = p.user_id
            AND p.user_id            = pe.user_id(+)
            AND p.status             = NVL ('''||p_in_participantStatus||''', p.status)
            AND un.role              = NVL ('''||p_in_role||''', un.role)
            AND (pe.department_type IN
              (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||'''))
              )
            OR ('''||p_in_departments||''' IS NULL))
            AND EXISTS
              (SELECT *
              FROM login_activity la
              WHERE user_id = un.user_id
              AND TRUNC(la.login_date_time) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
              )
            GROUP BY un.node_id
            ) dtl,
            (SELECT node_id,
              NVL(COUNT (p2.user_id),0) elg_pax
            FROM rpt_participant_employer vue,
              participant p2,
              user_node un
            WHERE p2.user_id         = vue.user_id(+)
            AND p2.status            = NVL ('''||p_in_participantStatus||''' , p2.status)
            AND un.role              = NVL ('''||p_in_role||''', un.role)
            AND p2.user_id           = un.user_id
            AND un.is_primary        = 1
            AND ( ('''||p_in_departments||''' IS NULL)
            OR (vue.department_type IN
              (SELECT * FROM TABLE ( get_array_varchar ( '''||p_in_departments||'''))
              )))
            GROUP BY node_id
            ) elg,
            (SELECT un.node_id,
              NVL(COUNT(la.user_id),0) cnt_visits
            FROM login_activity la,
              user_node un,
              participant p,
              rpt_participant_employer pe
            WHERE la.user_id         = un.user_id
            AND un.is_primary        = 1
            AND un.user_id           = p.user_id
            AND p.user_id            = pe.user_id(+)
            AND p.status             = NVL ('''||p_in_participantStatus||''', p.status)
            AND un.role              = NVL ('''||p_in_role||''', un.role)
            AND (pe.department_type IN
              (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||'''))
              )
            OR ('''||p_in_departments||''' IS NULL))
            AND TRUNC(la.login_date_time) BETWEEN TO_DATE('''||p_in_fromDate||''','''||p_in_localeDatePattern||''') AND TO_DATE('''||p_in_toDate||''','''||p_in_localeDatePattern||''')
            GROUP BY un.node_id
            ) v
          WHERE elg.node_id(+) = npn.node_id
          AND dtl.node_id(+)   = npn.node_id
          AND v.node_id(+)     = npn.node_id
          GROUP BY npn.path_node_id
          )
        WHERE rh.node_id = path_node_id
        ) dtl
      WHERE rad.node_id                = dtl.node_id
      AND NVL (rad.parent_node_id, 0) IN
        (SELECT * FROM TABLE ( get_array_varchar ( NVL ('''||parentNodeId||''', 0)))
        )
      ) RS
    ) R
  ORDER BY '|| v_sortCol ||'  )  WHERE RN > ' ||p_in_rowNumStart||' AND RN   < '|| p_in_rowNumEnd;
  OPEN p_out_resultsCursor FOR
  l_query;
  /* getOrgLoginActivityTopLevelResultsSize */
  SELECT COUNT (1) INTO p_out_size_data FROM
  (SELECT NODE.NODE_ID,
    NODE.NAME
    ||' Team'
  FROM NODE
  WHERE node.NODE_ID IN
    (SELECT * FROM TABLE(get_array_varchar( parentNodeId ) )
    )
  GROUP BY NODE.NAME,
    NODE.NODE_ID
  UNION
  SELECT NODE.NODE_ID,
    NODE.NAME
  FROM NODE
  WHERE NODE.NODE_ID IN
    (SELECT node_id
    FROM NODE N2
      CONNECT BY PRIOR node_id = parent_node_id
      START WITH node_id       = NODE.NODE_ID
    )
  AND NODE.PARENT_NODE_ID IN
    (SELECT * FROM TABLE(get_array_varchar( parentNodeId ) )
    )
  GROUP BY NODE.NAME,
    NODE.NODE_ID
  );
  /* getOrgLoginActivityTopLevelResultsTotals */
  OPEN p_out_totalsCursor FOR SELECT SUM(ELIGIBLE_PARTICIPANTS) ELIGIBLE_PARTICIPANTS, SUM(logged_in_cnt) LOGGED_IN_CNT, DECODE( SUM(eligible_participants), 0, 0, ROUND ( ( (SUM (logged_in_cnt) / SUM (eligible_participants)) * 100),2) ) PERCENT_LOGIN, SUM(ELIGIBLE_PARTICIPANTS) - SUM(logged_in_cnt) NOT_LOGGED_IN_CNT, DECODE( SUM(eligible_participants), 0, 0, ROUND ( ( (SUM (not_logged_in_cnt) / SUM (eligible_participants)) * 100),2) ) PERCENT_NOT_LOGIN, SUM(total_visits) TOTAL_VISITS FROM
  (SELECT --RS.NODE_ID                                                                                     AS NODE_ID,--11/02/2016
    RS.ELIGIABLE_APRTICIPENTS                                                                            AS ELIGIBLE_PARTICIPANTS,
    RS.CNT_HAVE_LOGGED_IN                                                                                AS LOGGED_IN_CNT,
    ROUND ( ( (RS.CNT_HAVE_LOGGED_IN     / RS.ELIGIABLE_APRTICIPENTS) * 100), 2)                         AS PERCENT_LOGIN,
    (RS.ELIGIABLE_APRTICIPENTS           - RS.CNT_HAVE_LOGGED_IN)                                        AS NOT_LOGGED_IN_CNT,
    ROUND ( ( (RS.ELIGIABLE_APRTICIPENTS - RS.CNT_HAVE_LOGGED_IN) * 100 / RS.ELIGIABLE_APRTICIPENTS), 2) AS PERCENT_NOT_LOGIN,
    RS.CNT_VISITS                                                                                        AS TOTAL_VISITS
  FROM
    (SELECT 
--    NODE.NODE_ID AS NODE_ID,--11/02/2016
--      NODE.NAME
--      || ' Team' NAME,
      COUNT (DISTINCT UN.USER_ID) AS ELIGIABLE_APRTICIPENTS,
      COUNT (DISTINCT LA.USER_ID) AS CNT_HAVE_LOGGED_IN,
      COUNT (LA.USER_ID)          AS CNT_VISITS
    FROM NODE,
      USER_NODE UN,
      (SELECT *
      FROM LOGIN_ACTIVITY
      WHERE TRUNC (login_date_time) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
      ) LA,
      PARTICIPANT P,
      rpt_participant_employer PE
    WHERE node.NODE_ID IN
      (
      --    SELECT node_id --Commented out as a part of performance fix.
--    FROM NODE N2
--      CONNECT BY PRIOR node_id = parent_node_id
--      START WITH node_id      IN
--      ( SELECT * FROM TABLE( get_array_varchar( parentNodeId))
--      )
      SELECT child_node_id
    FROM rpt_hierarchy_rollup N2
      WHERE node_id      IN
      ( SELECT * FROM TABLE( get_array_varchar( parentNodeId))
      )
      )
    AND un.user_id          = la.user_id(+)
    AND un.node_id          = node.node_id(+)
    AND un.user_id          = P.user_id
    AND un.is_primary       = 1
    AND P.user_id           = pe.user_id (+)
    AND P.status            = NVL (p_in_participantStatus, P.status)
    AND un.role             = NVL (p_in_role, un.role)
    AND ((p_in_departments IS NULL)
    OR pe.department_type  IN
      (SELECT * FROM TABLE(get_array_varchar(p_in_departments))
      ))
    ) RS
  );
  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_resultsCursor FOR SELECT NULL FROM DUAL;
  p_out_size_data := NULL;
  OPEN p_out_totalsCursor FOR SELECT NULL FROM DUAL;
END prc_getOrgTopLevelResults;
END pkg_query_login_activity;
/
