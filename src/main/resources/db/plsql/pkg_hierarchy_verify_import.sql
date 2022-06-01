CREATE OR REPLACE PACKAGE PKG_HIERARCHY_VERIFY_IMPORT IS

/***********************************************************************************
   Purpose:  To verify or load stage_hierarchy_import_record into original hierarchy tables.

   Person             Date           Comments
   -----------        ----------     --------------------------------------------
   Majumder, Sandip   09/17/2007     Initial Version
************************************************************************************/

  PROCEDURE p_hierarchy_verify_load(p_import_file_id  IN NUMBER, 
                                    p_load_type       IN VARCHAR2,
                                    p_hierarchy_id    IN NUMBER,
                                    p_user_id         IN NUMBER,
                                    p_total_error_rec OUT NUMBER,
                                    p_out_returncode  OUT NUMBER,
                                    p_out_user_data   OUT SYS_REFCURSOR) ;
END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY PKG_HIERARCHY_VERIFY_IMPORT
IS
 
/***********************************************************************************
   Purpose:  To verify or load stage_hierarchy_import_record into original hierarchy tables.
 
   Person             Date           Comments
   -----------        ----------     --------------------------------------------
   Majumder, Sandip   09/17/2007     Initial Version
   Majumder, Sandip   02/28/2008     Bug 18715: Modified the p_delete_node() function so that node 
                                     is not physically deleted from the database, instead 
                                     it would update the node name to have date time stamp
                                     as a suffix
   Majumder, Sandip   04/11/2008     Bug 20163: Modified the p_delete_node() function so that node  
   Majumder, Sandip   04/25/2008     Bug # 20344: Modified the p_verify_delete() procedure to check is_deleted = 0
   Arun S             05/06/2009     Bug # 13536, Budget Maintenance - is not handling deleted nodes 
   Arun S             12/07/2009     Bug # 30001, 
                                     Changes in p_validate_parent_node_name to check Parent node in file 
                                     and database and added in param p_hierarchy_id 
                                     Changes in p_val_old_parent_node_name to check Old parent in file
                                     and database and added in param p_old_node_name.        
                                     Changes in p_hierarchy_verify_load to call 
                                     p_val_old_parent_node_name before p_validate_parent_node_name
   chidamba           10/13/2011     Bug # 38184 - Hierarchy file load not updating the node.description of NULL value 
   Arun S             11/14/2012     G5 changes - Pre-calculating Criteria based Audience
                                     1.Added to populate user_node_history in p_update_user_nodes when 
                                       user_node.node_id is updated
                                     2.Call pkg_build_audience.prc_sync_audience_hierarchy for the input 
                                       import file id when load_type is 'L' and no error while loading
   Chidamba            11/28/2012  Change Request G5:
                                    - Improve error outputs  - have more descriptive errors to allow for remediation, always allow errors be seen from the file processing page (implement)
                                    - Remove need to specify insert and update. -D- is required for delete only.  (implement)                                
                                    - Org unit type - make field optional (implement)
                                    - Allow additional characteristics - five additional characteristics (implement)
                                    - Change Request to create Default Node Type for type Not-Applicable.
   Chidamba            12/26/2012  Change to get node_type_id from node_name table as we reutilizing Old DEFAULT value from CMS.
                                   earlier node_type_name view is used to get node_type_id of DEFAULT.
   Chidamba            01/25/2013  Defect #2301  New Node owner should be Updated when existing Node owner is Inactivated prior to File Load. 
   Ravi Dhanekula      04/05/2013  Fixed the issue with root node validation.
   nagarajs            01/15/2014  Bug 51076 Fix
   Ravi Dhanekula  01/15/2014  Bug 50051 Fix
                            03/31/2014 Commented out the end_date column update while inactivating budget. As end_date is removed during budget changes of 5.4 release.  
  Suresh J         12/03/2014  - Bug 51124 Fix - Delete Node Characteristics if p_node_type_chg_flg = 1    
  Suresh J        12/10/2014 - Fixed Bug 51124 - Value of v_node_id was NULL before Delete command. Fixed by assigning it to the value of f_get_node_id                                
  Ravi Dhanekula  06/24/2015 - Fixed the bug # 62110.Re-write to fix performance issues.
  Suresh J        07/16/2015 - Bug 63258 - Description not getting updated       
 Suresh J         07/20/2015  Bug 63243 - Deleting a Node which has child node-Error message is not triggered     
 Ravi Dhanekula 07/21/2015 Bug # 63237 - Admin-Hierarchy file load-File has all the required fields with missing parent org unit-Error message didn't triggered   
 Ravi Dhanekula 07/21/2015 Bug # 63155 - Hierarchy file load NOT properly handling a new root org being inserted into existing hierarchy
Suresh J        07/23/2015  Bug # 63245 - Node Characteristics are not getting reflected after a successful load of an existing hierarchy      
Ravi Dhanekula 08/16/2015 Bug # 63891..WIP # 6067 
Ravi Dhanekula 08/20/2015 BUG # 63768 -Fix for the issue sys_connect_by_path ORA-01489: result of string concatenation is too long 
nagarajs       10/07/2015 Bug 63504 - Admin-Hierarchy file load-Decimal Org unit type accepts the value beyond the valid decimal range
nagarajs       10/08/2015 Bug 63504 - Admin-Hierarchy file load-Decimal Org unit type accepts the value beyond the valid decimal range
nagarajs       11/20/2015 Bug 64731 - HIERARCHY load allowing duplicate name
nagarajs       12/21/2015 Bug 64996- Deleting a node via file load no longer makes node.name unique with date/time stamp
nagarajs      12/21/2015 Bug 64997 - Case insensitive comparison seems to be gone from Hierarchy File Load
nagarajs      01/04/2016 Bug 65098 - Report refresh is failing when a node moves up in the hierarchy 
nagarajs      03/18/2016 Bug 66157 - Loading the same Hierarchy file twice throws all records in error at Verify step
Ravi Dhanekula  07/13/2016   G6 changes - Added a ref cursor to provide list of users that have been changed during each load.
 nagarajs       08/23/2016   JIRA G6-120 - Criteria Based Audience updated
Ravi Dhanekula  05/04/2017   FIxed issue with hierarchy load where its allowing more than one root node to exist.
Chidamba    08/18/2017  G6-2862- Hierarchy file load allowing cyclic/broken hierarchy 
                            (action type becomes 'upd' in new hierarchy when new node name exists in more than one hierarchy)
Chidamba    29/12/2017  G6-3645- Reordering the process call, by this way users will be 
                        moved to specified node before node gets deleted
Gorantla        05/22/2018   GitLab#1097/Bug 76352 - Hierarchy File Load Error for Case or Capitalization Update   
Gorantla        03/05/2019   add custom process prc_budget_move_ins
                             remove call to p_close_node_budget - replaced by a custom budget process
                             add call to prc_aefo_budget_move_transfer   
                03/15/2019   move call to prc_budget_move_transfer after COMMIT to 
                             ensure all changes have been applied before doing the budget moves
Gorantla        11/28/2019   Git#2611  - Add new columns and modify the oracle packages to insert new UUID                                                 
************************************************************************************/
 
g_hierarchy_id            HIERARCHY.HIERARCHY_ID%TYPE := 0;
g_initial_load            NUMBER(1) := 1; -- 1 : Initial Load, 0: Subsequent Loads
g_master_node_id          NODE.NODE_ID%TYPE;
g_master_node_name        NODE.NAME%TYPE;
g_total_error_rec         NUMBER(10) := 0;
g_import_file_id          NUMBER(18) := 0;
g_import_record_id        NUMBER(18) := 0;
g_name_change_flg         NUMBER(1) := 0;
g_description_chg_flg     NUMBER(1) := 0;
g_parent_name_change_flg  NUMBER(1) := 0;
g_del_with_move_to_node   NUMBER(1) := 0;
g_move_to_node_in_file    NUMBER(1) := 0;
g_parent_node_in_file     NUMBER(1) := 0;
g_node_type_chg_flg       NUMBER(1) := 0;
g_chr_chg_Flg1            NUMBER(1) := 0;
g_chr_chg_Flg2            NUMBER(1) := 0;
g_chr_chg_Flg3            NUMBER(1) := 0;
g_chr_chg_Flg4            NUMBER(1) := 0;
g_chr_chg_Flg5            NUMBER(1) := 0;
g_chr_chg_Flg6            NUMBER(1) := 0;
g_chr_chg_Flg7            NUMBER(1) := 0;
g_chr_chg_Flg8            NUMBER(1) := 0;
g_chr_chg_Flg9            NUMBER(1) := 0;
g_chr_chg_Flg10           NUMBER(1) := 0;
g_tot_rec_null_parent     NUMBER(10) := 0;
g_node_null_parent     NODE.NAME%TYPE := NULL; --04/05/2013
g_created_by              NODE.CREATED_BY%TYPE := 0;
g_modified_by             NODE.MODIFIED_BY%TYPE := 0;
g_file_name               IMPORT_FILE.FILE_NAME%TYPE;
g_import_record_count     IMPORT_FILE.IMPORT_RECORD_COUNT%TYPE := 0;
g_is_node_type_req        NUMBER(1);
g_node_type_id            NUMBER(10);

c_action_type_add          CONSTANT stage_pax_import_record.action_type%TYPE := 'add';
c_action_type_upd          CONSTANT stage_pax_import_record.action_type%TYPE := 'upd';
c_action_type_del          CONSTANT stage_pax_import_record.action_type%TYPE := 'del';
c_created_by               CONSTANT import_record_error.created_by%TYPE := 0;
g_timestamp                import_record_error.date_created%TYPE := SYSDATE;
c_cms_status               CONSTANT vw_cms_code_value.cms_status%TYPE := 'true';
c_cms_locale               CONSTANT vw_cms_code_value.locale%TYPE := 'en_US';
 
PROCEDURE p_execution_log_entry (
                                 i_process_name  execution_log.process_name%TYPE,
                                 i_release_level execution_log.release_level%TYPE,
                                 i_severity  IN  execution_log.severity%TYPE,
                                 i_text_line IN  execution_log.text_line%TYPE,
                                 i_dbms_job_nbr IN execution_log.dbms_job_nbr%TYPE) IS
  PRAGMA AUTONOMOUS_TRANSACTION;
-- --------------------------------------------------------------
-- This procedure is used to create an entry into execution_log table to faciltate
-- the audit trail of any process thats executed on the DB.
-- --------------------------------------------------------------
BEGIN
  --    i_severity should be one of the following:
  --    info   = informational message
 
  --    warn   = warning message
  --    error  = error message that should be checked now
  --    fatal  = fatal error message that should be checked now
  --    detail = detailed message for debugging
 
    INSERT INTO execution_log
                             (execution_log_id,
                              session_id,
                              process_name,
                              severity,
                              text_line,
                              release_level,
                              dbms_job_nbr,
                              created_by,
                              date_created)
                       VALUES
                             (execution_log_pk_sq.NEXTVAL,
                              USERENV('sessionid'),
                              i_process_name,
                              i_severity,
                              i_text_line,
                              i_release_level,
                              i_dbms_job_nbr,
                              0,
                              SYSDATE);
   COMMIT;
END;
 

PROCEDURE p_insert_import_record_error(p_import_record import_record_error%ROWTYPE) IS
  PRAGMA AUTONOMOUS_TRANSACTION;
  v_import_record_error_id import_record_error.import_record_error_id%TYPE;
BEGIN
  SELECT IMPORT_record_error_pk_SQ.NEXTVAL
    INTO v_import_record_error_id
    FROM dual;
  INSERT INTO import_record_error A
    (import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created)
  VALUES
    (v_import_record_error_id,
     p_import_record.import_file_id,
     p_import_record.import_record_id,
     p_import_record.item_key,
     p_import_record.param1,
     p_import_record.param2,
     p_import_record.param3,
     p_import_record.param4,
     g_created_by,
     SYSDATE);
  COMMIT;
END;
 
FUNCTION f_str_parse(p_instring IN VARCHAR2, p_field_no IN NUMBER, p_delimiter IN VARCHAR2 DEFAULT '~')
   RETURN VARCHAR2
IS
   v_start                       NUMBER;
   v_no_of_chars                 NUMBER;
   v_instring                    VARCHAR2(32767);
BEGIN
   v_instring := p_instring || UPPER(p_delimiter);

   IF p_field_no = 1
   THEN
      RETURN(SUBSTR(v_instring
                   ,1
                   , INSTR(UPPER(v_instring), UPPER(p_delimiter) ) - 1
                   ) );
   ELSE
      v_start := INSTR(UPPER(v_instring)
                      ,UPPER(p_delimiter)
                      ,1
                      , p_field_no - 1
                      ) + LENGTH(p_delimiter);
      v_no_of_chars := INSTR(UPPER(v_instring)
                            ,UPPER(p_delimiter)
                            ,1
                            ,p_field_no
                            ) - v_start;
      RETURN(SUBSTR(v_instring
                   ,v_start
                   ,v_no_of_chars
                   ) );
   END IF;
END;

FUNCTION f_get_node_id ( p_in_nodename IN VARCHAR2, p_hierarchy_id NUMBER)
  RETURN  VARCHAR2 IS
  v_node_id             VARCHAR2(30);
 
BEGIN
    SELECT node_id
      INTO v_node_id
      FROM node
     WHERE LOWER(NAME) = LOWER(p_in_nodename)
       AND hierarchy_id = p_hierarchy_id;
 
    RETURN v_node_id ;
 
EXCEPTION
   WHEN NO_DATA_FOUND THEN
       v_node_id := 'X' ;
       RETURN v_node_id ;
   WHEN OTHERS THEN
       v_node_id := 'X' ;
       RETURN v_node_id ;
END;
 
FUNCTION f_get_node_path ( p_in_node_id  NUMBER, p_hierarchy_id NUMBER)
  RETURN  VARCHAR2  IS
 
  v_path    node.PATH%TYPE ;
BEGIN 
  SELECT PATH
    INTO v_path
    FROM node
   WHERE node_id = p_in_node_id
     AND hierarchy_id = p_hierarchy_id;
   RETURN v_path ;
EXCEPTION
   WHEN OTHERS THEN
       v_path := 'X' ;
       RETURN v_path ;
END;
 
FUNCTION f_get_node_type_id ( p_in_nodename  VARCHAR2, p_hierarchy_id NUMBER)
  RETURN  VARCHAR2  IS
 
  v_node_type_id    node.PATH%TYPE ;
BEGIN 
    SELECT node_type_id
      INTO v_node_type_id
      FROM node
     WHERE LOWER(NAME) = LOWER(p_in_nodename)
       AND hierarchy_id = p_hierarchy_id;   
    RETURN v_node_type_id ;
EXCEPTION
   WHEN OTHERS THEN
       v_node_type_id := 'X' ;
       RETURN v_node_type_id ;
END;
 
FUNCTION f_get_characteristic_data_type ( p_in_characteristic_id  NUMBER)
  RETURN  VARCHAR2  IS
 
  v_characteristic_data_type    CHARACTERISTIC.CHARACTERISTIC_DATA_TYPE%TYPE ;
BEGIN 
 
   SELECT LOWER(CHARACTERISTIC_DATA_TYPE)
     INTO v_characteristic_data_type
     FROM CHARACTERISTIC
    WHERE CHARACTERISTIC_ID = p_in_characteristic_id;
 
    RETURN v_characteristic_data_type ;
EXCEPTION
   WHEN OTHERS THEN
       v_characteristic_data_type := 'X' ;
       RETURN v_characteristic_data_type ;
END;
 

FUNCTION f_isinteger(P IN VARCHAR2)
   RETURN BOOLEAN
IS
   TEST   NUMBER;
BEGIN
   TEST := TO_NUMBER(P);

   IF MOD(TEST, 1) <> 0
   THEN
      RETURN FALSE;
   ELSE
      RETURN TRUE;
   END IF;
EXCEPTION
   WHEN VALUE_ERROR
   THEN
      RETURN FALSE;
END;
 
FUNCTION f_isdecimal(P IN VARCHAR2)
   RETURN BOOLEAN
IS
   TEST   NUMBER;
BEGIN
   TEST := TO_NUMBER(P);


EXCEPTION
   WHEN VALUE_ERROR
   THEN
      RETURN FALSE;
END;

PROCEDURE p_is_within_min_max(
   p_characteristic_id      IN       characteristic.characteristic_id%TYPE
  ,p_characteristic_value   IN       VARCHAR2
  ,p_characteristic_min     OUT      VARCHAR2
  ,p_characteristic_max     OUT      VARCHAR2
  ,p_return                 OUT      VARCHAR2
)
IS
BEGIN
   p_return := 'FALSE';
   p_characteristic_min := 0;
   p_characteristic_max := 0;

   BEGIN
      SELECT min_value
            ,max_value
        INTO p_characteristic_min
            ,p_characteristic_max
        FROM characteristic
       WHERE LOWER(characteristic_data_type) = 'int' 
         AND characteristic_id = p_characteristic_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         NULL;
   END;

   IF TO_NUMBER(p_characteristic_value) BETWEEN TO_NUMBER(p_characteristic_min) AND TO_NUMBER(p_characteristic_max)
   THEN
      p_return := 'TRUE';
   END IF;
END;

PROCEDURE p_is_txt_within_max(
   p_characteristic_id      IN       characteristic.characteristic_id%TYPE
  ,p_characteristic_value   IN       VARCHAR2
  ,p_max_txt_size           OUT      VARCHAR2
  ,p_result                 OUT      VARCHAR2
)
IS
BEGIN
   p_result := 'FALSE';
   p_max_txt_size := 0;

   BEGIN
      SELECT max_size
        INTO p_max_txt_size
        FROM characteristic
       WHERE LOWER(characteristic_data_type) = 'txt' 
         AND characteristic_id = p_characteristic_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         NULL;
   END;
   -- Start modification for Bug 20163
   IF LENGTH(p_characteristic_value) <= p_max_txt_size
   -- End Modification for Bug 20163   
   THEN
      p_result := 'TRUE';
   END IF;
END;

PROCEDURE p_is_date_within_range(p_characteristic_id     characteristic.characteristic_id%TYPE
                                ,p_characteristic_value  VARCHAR2
                                ,p_date_start            OUT      DATE
                                ,p_date_end              OUT      DATE
                                ,p_return                OUT      VARCHAR2)
IS

/****************************
Ravi Dhanekula 02/13/2013  Defect # 2693.

****************************/
v_language_code   VARCHAR2(10):='en_US';
BEGIN
   p_return := 'FALSE';

   BEGIN
      SELECT date_start
            ,date_end
        INTO p_date_start
            ,p_date_end
        FROM characteristic
       WHERE LOWER(characteristic_data_type) = 'date' 
         AND characteristic_id = p_characteristic_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         NULL;
   END;
   IF p_date_start IS NULL AND p_date_end IS NULL THEN --02/13/2013
   p_return := 'TRUE';
   ELSIF p_date_end IS NULL AND fnc_locale_to_date_dt(p_characteristic_value, v_language_code) >= p_date_start THEN
    p_return := 'TRUE';
    ELSIF p_date_start IS NULL AND fnc_locale_to_date_dt(p_characteristic_value, v_language_code) <= p_date_end THEN
    p_return := 'TRUE';
    ELSIF fnc_locale_to_date_dt(p_characteristic_value, v_language_code) BETWEEN p_date_start AND p_date_end
   THEN
      p_return := 'TRUE';
   END IF;
END;



FUNCTION f_isDate(P IN VARCHAR2)
  RETURN BOOLEAN
IS
  TEST VARCHAR2(30);
  v_language_code   VARCHAR2(10):='en_US';
BEGIN
  TEST := fnc_locale_to_date_dt(P, v_language_code);
  RETURN TRUE;
EXCEPTION
  WHEN OTHERS THEN RETURN FALSE;
END;
 
FUNCTION f_isvalid_picklist_item(p_characteristic_id NUMBER, p_characteristic_value VARCHAR2)
  RETURN BOOLEAN
IS
  v_pl_name                 VUE_CHARACTERISTIC_NAME.PL_NAME%TYPE;
  v_cms_asset_code_value    VARCHAR2(300);
BEGIN
   SELECT PL_NAME
     INTO v_pl_name
     FROM vue_characteristic_name
    WHERE characteristic_id = p_characteristic_id;   

  SELECT UPPER(DBMS_LOB.SUBSTR(cd.VALUE,300,1))
    INTO v_cms_asset_code_value
    FROM cms_content_data cd
   WHERE cd.content_id IN
         (SELECT c.ID
            FROM cms_content c
           WHERE locale='en_US' AND c.content_key_id IN
                 (SELECT ck.ID
                    FROM cms_content_key ck
                   WHERE ck.asset_id IN
                         (SELECT A.ID
                            FROM cms_asset A
                           WHERE A.code = v_pl_name))
                           AND content_status = 'Live')
     AND cd.KEY =  'CODE'
     AND UPPER(DBMS_LOB.SUBSTR(cd.VALUE,300,1)) = UPPER(p_characteristic_value);

   RETURN TRUE;
EXCEPTION
  WHEN OTHERS THEN RETURN FALSE;
END;

PROCEDURE p_upd_child_paths(
   p_base_node_id   IN   node.node_id%TYPE
  ,p_old_path       IN   node.PATH%TYPE
  ,p_new_path       IN   node.PATH%TYPE
)
IS
BEGIN
   UPDATE node
      SET PATH = REPLACE(PATH
                        ,p_old_path
                        ,p_new_path
                        ),
          modified_by = g_modified_by,
          date_modified = SYSDATE,
          VERSION = VERSION + 1
    WHERE node_id IN(SELECT node_id
                       FROM node
                      WHERE hierarchy_id = g_hierarchy_id
                     START WITH node_id = p_base_node_id
                     CONNECT BY PRIOR node_id = parent_node_id) 
      AND parent_node_id IS NOT NULL
      AND node_id <> p_base_node_id;
EXCEPTION
   WHEN OTHERS
   THEN
      NULL;
END;

FUNCTION f_get_file_name (p_import_file_id IN NUMBER)
  RETURN VARCHAR2 IS
 
  v_file_name VARCHAR2(300);
 
BEGIN
   SELECT file_name
     INTO v_file_name
     FROM import_file
    WHERE import_file_id = p_import_file_id
      AND file_type = 'hier';
   RETURN v_file_name;
EXCEPTION
  WHEN OTHERS THEN
    v_file_name := 'X';
    RETURN v_file_name;
END;
 

FUNCTION f_get_import_record_count (p_import_file_id IN NUMBER)
  RETURN NUMBER IS
 
  v_import_record_count VARCHAR2(300);
 
BEGIN
   SELECT import_record_count
     INTO v_import_record_count
     FROM import_file
    WHERE import_file_id = p_import_file_id
      AND file_type = 'hier';
   RETURN v_import_record_count;
EXCEPTION
  WHEN OTHERS THEN
    v_import_record_count := 0;
    RETURN v_import_record_count;
END;
 

FUNCTION f_get_cms_asset_code_value(p_in_assetcode IN VARCHAR2, p_content_data_key IN VARCHAR2)
  RETURN CLOB IS
 
  v_cms_asset_code_value CLOB;
 
BEGIN
  SELECT cd.VALUE
    INTO v_cms_asset_code_value
    FROM cms_content_data cd
   WHERE cd.content_id IN
         (SELECT c.ID
            FROM cms_content c
           WHERE c.content_key_id IN
                 (SELECT ck.ID
                    FROM cms_content_key ck
                   WHERE ck.asset_id IN
                         (SELECT A.ID
                            FROM cms_asset A
                           WHERE A.code = p_in_assetcode))
                           AND content_status = 'Live')
     AND cd.KEY =  p_content_data_key;
  RETURN v_cms_asset_code_value;
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    v_cms_asset_code_value := NULL;
    RETURN v_cms_asset_code_value;
  WHEN OTHERS THEN
    v_cms_asset_code_value := 'X';
    RETURN v_cms_asset_code_value;
END;
 
 
 
FUNCTION f_get_email_from
  RETURN VARCHAR2 IS
 
  v_email_from VARCHAR2(300);
 
BEGIN
   v_email_from := fnc_get_system_variable('email.address.incentive.personaldisplay', 'Incentive Personal Name Display') 
                   ||' <'||fnc_get_system_variable('email.address.system.incentive', 'Incentive System E-mail Address')||'>';
   RETURN v_email_from;
EXCEPTION
  WHEN OTHERS THEN
    v_email_from := 'X';
    RETURN v_email_from;
END;
 
FUNCTION f_get_email_to (p_user_id IN NUMBER)
  RETURN VARCHAR2 IS
 
  v_email_to VARCHAR2(300);
 
BEGIN
   SELECT email_addr
     INTO v_email_to
     FROM USER_EMAIL_ADDRESS
    WHERE user_id = p_user_id
      AND email_type = 'bus';
   RETURN v_email_to;
EXCEPTION
  WHEN OTHERS THEN
    v_email_to := 'X';
    RETURN v_email_to;
END;
 

FUNCTION f_get_email_subject (p_load_type IN VARCHAR2)
  RETURN VARCHAR2 IS
 
  v_email_email_subject VARCHAR2(300);
 
BEGIN
   IF p_load_type = 'V' THEN
      v_email_email_subject := fnc_get_system_variable('email.subject.prefix', 'Subject Prefix')||TO_CHAR(f_get_cms_asset_code_value('message_data.message.17240', 'SUBJECT'));
   ELSIF p_load_type = 'L' THEN
      v_email_email_subject := fnc_get_system_variable('email.subject.prefix', 'Subject Prefix')||TO_CHAR(f_get_cms_asset_code_value('message_data.message.101451', 'SUBJECT'));
   ELSE
      v_email_email_subject := 'X'; 
   END IF;     
   RETURN v_email_email_subject;
EXCEPTION
  WHEN OTHERS THEN
    v_email_email_subject := 'X';
    RETURN v_email_email_subject;
END;
 

FUNCTION f_get_ver_email_message (p_file_name IN VARCHAR2,
                                  p_num_valid_rec IN NUMBER,
                                  p_num_error_rec IN NUMBER )
  RETURN CLOB IS
 
  v_ver_email_message CLOB;
 
BEGIN
   v_ver_email_message := f_get_cms_asset_code_value('message_data.message.17240', 'PLAIN_TEXT_MSG');
   v_ver_email_message := REPLACE (v_ver_email_message, '${fileType}', 'Hierarchy'); 
   v_ver_email_message := REPLACE (v_ver_email_message, '${fileName}', p_file_name); 
   v_ver_email_message := REPLACE (v_ver_email_message, '${numValidRecords}', p_num_valid_rec);
   v_ver_email_message := REPLACE (v_ver_email_message, '${numErrorRecords}', p_num_error_rec);
 
   RETURN v_ver_email_message;
EXCEPTION
  WHEN OTHERS THEN
    v_ver_email_message := 'X';
    RETURN v_ver_email_message;
END;
 
FUNCTION f_get_imp_email_message (p_file_name IN VARCHAR2,
                                  p_success   IN VARCHAR2 DEFAULT NULL)
  RETURN VARCHAR2 IS
 
  v_imp_email_message VARCHAR2(300);
 
BEGIN
   v_imp_email_message := f_get_cms_asset_code_value('message_data.message.101451', 'PLAIN_TEXT_MSG');
   v_imp_email_message := REPLACE (v_imp_email_message, '${fileName}', p_file_name); 
   v_imp_email_message := REPLACE (v_imp_email_message, '${success}', p_success);
 
   RETURN v_imp_email_message;
EXCEPTION
  WHEN OTHERS THEN
    v_imp_email_message := 'X';
    RETURN v_imp_email_message;
END;
 
PROCEDURE p_insert_node(p_node node%ROWTYPE, p_node_id OUT NUMBER) IS
  v_node_id node.node_id%TYPE;
BEGIN
  SELECT NODE_pk_SQ.NEXTVAL
    INTO v_node_id
    FROM dual;
  p_node_id := v_node_id;
  INSERT INTO node
    (NODE_ID,
     NODE_TYPE_ID,
     NAME,
     PATH,
     DESCRIPTION,
     PARENT_NODE_ID,
     HIERARCHY_ID,
     IS_DELETED,
     CREATED_BY,
     DATE_CREATED,
     MODIFIED_BY,
     DATE_MODIFIED,
     VERSION,
     roster_node_id  -- 11/28/2019
     )
  VALUES
    (v_node_id,
     p_node.NODE_TYPE_ID,
     p_node.NAME,
     p_node.PATH,
     p_node.DESCRIPTION,
     p_node.PARENT_NODE_ID,
     p_node.HIERARCHY_ID,
     p_node.IS_DELETED,
     g_CREATED_BY,
     SYSDATE,
     NULL,
     NULL,
     p_node.VERSION,
     fnc_randomuuid -- 11/28/2019
     );
END;
 
PROCEDURE p_insert_node_characteristic(p_node_characteristic node_characteristic%ROWTYPE ) IS
  v_node_characteristic_id node_characteristic.node_characteristic_id%TYPE;
  v_chracteristic_value               node_characteristic.CHARACTERISTIC_VALUE%TYPE;
BEGIN
  SELECT NODE_CHARACTERISTIC_PK_SQ.NEXTVAL
    INTO v_node_characteristic_id
    FROM dual;
  IF f_get_characteristic_data_type(p_node_characteristic.CHARACTERISTIC_ID) = 'multi_select' THEN
     v_chracteristic_value := REPLACE ( p_node_characteristic.CHARACTERISTIC_VALUE, '~', ',' );
  ELSE
     v_chracteristic_value :=  p_node_characteristic.CHARACTERISTIC_VALUE;
  END IF;
  
  INSERT INTO node_characteristic
    (NODE_CHARACTERISTIC_ID,        
     NODE_ID,
     CHARACTERISTIC_ID,
     CHARACTERISTIC_VALUE,
     CREATED_BY,
     DATE_CREATED,
     MODIFIED_BY,
     DATE_MODIFIED,
     VERSION,
     roster_node_char_id  -- 11/28/2019
     )
  VALUES
    (v_node_characteristic_id,
     p_node_characteristic.NODE_ID,
     p_node_characteristic.CHARACTERISTIC_ID,
     v_chracteristic_value,
     g_created_by,
     SYSDATE,
     NULL,
     NULL,
     0,
     fnc_randomuuid  -- 11/28/2019
     );
     
END;
 
PROCEDURE p_update_node_characteristic ( p_node_characteristic node_characteristic%ROWTYPE )
IS
   v_node_characteristic_id      node_characteristic.node_characteristic_id%TYPE;
   v_rows_cnt                    NUMBER(10) := 0;
   v_chracteristic_value         node_characteristic.CHARACTERISTIC_VALUE%TYPE;
BEGIN
  
 
   IF f_get_characteristic_data_type(p_node_characteristic.CHARACTERISTIC_ID) = 'multi_select' THEN
      v_chracteristic_value := REPLACE ( p_node_characteristic.CHARACTERISTIC_VALUE, '~', ',' );
   ELSE
      v_chracteristic_value :=  p_node_characteristic.CHARACTERISTIC_VALUE;
   END IF;

   UPDATE node_characteristic
      SET characteristic_value = v_chracteristic_value,
          modified_by = g_modified_by,
          date_modified = SYSDATE,
          VERSION = VERSION + 1
    WHERE node_id = p_node_characteristic.node_id 
      AND characteristic_id = p_node_characteristic.characteristic_id;
 
   v_rows_cnt := SQL%ROWCOUNT;
  
   IF v_rows_cnt = 0 THEN
      p_insert_node_characteristic(p_node_characteristic);
 
   END IF;
 
END;
/* Procedure to check one and only one parent node in the NODE table */
PROCEDURE p_check_OAOI_parent ( p_out_returncode     OUT NUMBER) IS
 
  v_parent_node_cnt   NUMBER(10) := 0;
  rec_import_record_error   import_record_error%ROWTYPE;

BEGIN
   p_out_returncode := 0;
   SELECT COUNT(1)
     INTO v_parent_node_cnt
     FROM node
    WHERE parent_node_id IS NULL
      AND hierarchy_id = g_hierarchy_id
      AND is_deleted = 0;
   --p_debug_message('v_parent_node_cnt :'||v_parent_node_cnt);
   IF v_parent_node_cnt <> 1 THEN
      rec_import_record_error.import_file_id := g_import_file_id;
      rec_import_record_error.import_record_id := 0;
      rec_import_record_error.item_key := 'system.errors.ONE_PARENT_NODE';
      rec_import_record_error.created_by := g_created_by;
      p_insert_import_record_error (rec_import_record_error);
      p_out_returncode := 1;
   END IF;

EXCEPTION WHEN OTHERS THEN
   p_execution_log_entry('p_check_OAOI_parent', '1', 'ERROR',
        'Parent Node Count: '||v_parent_node_cnt||'. '||SQLERRM, NULL);
   p_out_returncode := 99;
END;
PROCEDURE p_upd_action_type_stg(p_import_file_id NUMBER,                      --11/28/2012 Created                               
                                p_hierarchy_id NUMBER,
                                p_out_returncode OUT NUMBER) IS
 v_node_name   NODE.NAME%TYPE;
-- v_action_type VARCHAR2(5); 
 v_old_node_id VARCHAR2(10); 
 rec_import_record_error   import_record_error%ROWTYPE;
 
 v_import_record_id        DBMS_SQL.NUMBER_TABLE;
 v_action_type           DBMS_SQL.VARCHAR2_TABLE;
 
 CURSOR C_ACTION_TYPE IS
  SELECT s.import_record_id,CASE WHEN n.node_id IS NOT NULL THEN 'upd'
                                                                                                                      WHEN n.node_id IS NULL AND s.old_name IS NOT NULL AND n_old.node_id IS NULL THEN NULL
                                                                                                                      WHEN n.node_id IS NULL AND s.old_name IS NOT NULL AND n_old.node_id IS NOT NULL THEN 'upd'
                                                                                                                      WHEN n.node_id IS NULL AND s.old_name IS NULL THEN 'add' END action_type                                                                                                                                                   
  FROM stage_hierarchy_import_record s ,node n, node n_old WHERE s.import_file_id = p_import_file_id
AND LOWER(s.NAME) = LOWER(n.NAME(+))
AND LOWER(s.old_name) = LOWER(n_old.NAME(+))
AND n.hierarchy_id(+) = p_hierarchy_id          --08/18/2017  G6-2862 
AND n_old.hierarchy_id(+) = p_hierarchy_id      --08/18/2017  G6-2862
AND NVL(s.action_type,'X') <> 'del';
BEGIN                                
 
--   v_old_node_id := f_get_node_id (p_node_name, g_hierarchy_id);
--    
--   IF v_old_node_id = 'X' THEN
--
--     IF p_old_node_name IS NOT NULL THEN
--
--       v_old_node_id := f_get_node_id (p_old_node_name, g_hierarchy_id);
--        IF v_old_node_id = 'X' THEN
--              rec_import_record_error.import_file_id := g_import_file_id;
--              rec_import_record_error.import_record_id := g_import_record_id;
--              rec_import_record_error.item_key := 'system.errors.UNKNOWN_NODE';
--              rec_import_record_error.param1 := 'Old Node Name';
--              rec_import_record_error.param2 := p_old_node_name;
--              rec_import_record_error.created_by := g_created_by;
--              p_insert_import_record_error (rec_import_record_error);
--              p_out_returncode := 1;
--              v_action_type    := NULL;
--        ELSE
--          v_action_type := 'upd';
--        END IF; 
--     ELSE 
--       v_action_type := 'add';
--      
--     END IF;
--
--   ELSE 
--     v_action_type := 'upd';
--   END IF;

OPEN C_ACTION_TYPE;                        --03/19/2015
    FETCH C_ACTION_TYPE BULK COLLECT          
     INTO   v_import_record_id,
            v_action_type;
    CLOSE C_ACTION_TYPE;
        
        IF v_import_record_id.COUNT > 0 THEN    
    FORALL indx IN v_import_record_id.FIRST .. v_import_record_id.LAST
    UPDATE stage_hierarchy_import_record A
    SET  action_type = v_action_type(indx)
    WHERE A.import_record_id = v_import_record_id(indx);          
    END IF; --if v_import_record_id.COUNT      
        
 p_out_returncode := 00;
EXCEPTION
WHEN OTHERS THEN
   p_execution_log_entry('p_upd_action_type_stg', '1', 'ERROR',
        'Action Type update Error : '||p_import_file_id||'. '||SQLERRM, NULL);
   p_out_returncode := 99;

END;
PROCEDURE p_rpt_node_name_err
( p_import_file_id  IN     stage_hierarchy_import_record.import_file_id%TYPE,
  p_hierarchy_id    IN     node.hierarchy_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_node_name_err');
   c_release_level      CONSTANT execution_log.release_level%TYPE := '2.0';

   v_stage                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- node name validation
   v_stage := 'INSERT import_record_error';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   (  SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
             E.import_file_id,
             E.import_record_id,
             E.item_key,
             E.field_name AS param1,
             E.field_value AS param2,
             NULL AS param3,
             NULL AS param4,
             c_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*,
                         n_cur.NAME AS db_cur_name, --12/21/2015 Removed UPPER function Bug 64997
                         n_old.NAME AS db_old_name  --12/21/2015 Removed UPPER function Bug 64997
                    FROM stage_hierarchy_import_record s,
                         node n_cur,
                         node n_old
                   WHERE s.import_file_id = p_import_file_id
                     -- outer join by node name to find current/old database values
                     AND UPPER(n_cur.NAME (+))  = UPPER(s.NAME)
                     AND n_cur.hierarchy_id (+) = p_hierarchy_id
                     AND UPPER(n_old.NAME (+))  = UPPER(s.old_name)
                     AND n_old.hierarchy_id (+) = p_hierarchy_id
               )
               -- node name required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.MISSING_PROPERTY' AS item_key,
                      'Node Name' AS field_name,
                      sr.NAME AS field_value
                 FROM stg_rec sr
                WHERE sr.NAME IS NULL
                UNION ALL
               -- adding a duplicate node name
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.NODE_NAME_NOT_UNIQUE' AS item_key,
                      'Node Name' AS field_name,
                      sr.NAME AS field_value
                 FROM stg_rec sr
                WHERE sr.NAME IS NOT NULL
                  AND sr.action_type = c_action_type_add
                  AND UPPER(sr.NAME) = UPPER(sr.db_cur_name) --12/21/2015 Added UPPER function to db_cur_name Bug 64997
                  AND sr.NAME <> sr.db_cur_name              --03/18/2016 added
                  --AND NOT EXISTS(SELECT 1 FROM dual WHERE REGEXP_LIKE (sr.NAME, sr.db_cur_name, 'c')) --12/21/2015 Added Bug 64997 --03/18/2016 commented
               /* UNION ALL                                                                             --12/21/2015 Added Bug 64997 -- 05/22/2018 commented out
                 SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.NODE_NAME_NOT_UNIQUE' AS item_key,
                      'Node Name' AS field_name,
                      sr.NAME AS field_value
                 FROM stg_rec sr
                WHERE sr.NAME IS NOT NULL
                  AND sr.action_type = c_action_type_upd
                  AND UPPER(sr.NAME)= UPPER(sr.db_cur_name)
                  AND sr.old_name IS NULL      
                  AND sr.NAME <> sr.db_cur_name */    --03/18/2016 added
                  --AND NOT EXISTS(SELECT 1 FROM dual WHERE REGEXP_LIKE (sr.NAME, sr.db_cur_name, 'c')) --03/18/2016 commented
               -- updating to a duplicate node name
               UNION ALL
               SELECT sr.import_file_id,          --12/21/2015 Bug 64997 --03/18/2016 uncommented
                      sr.import_record_id,
                      'system.errors.NODE_NAME_NOT_UNIQUE' AS item_key,
                      'Node Name' AS field_name,
                      sr.NAME AS field_value
                 FROM stg_rec sr
                WHERE sr.NAME IS NOT NULL
                  AND sr.old_name IS NOT NULL
                  AND sr.action_type = c_action_type_upd
                  AND sr.NAME = sr.db_cur_name  --03/18/2016 Remove UPPER
                UNION ALL
                -- check for duplicate node name within source file
               SELECT sr.import_file_id,         -- 11/20/2015
                      sr.import_record_id,
                      'system.errors.NODE_NAME_NOT_UNIQUE' AS item_key,
                      'Node Name' AS field_name,
                      sr.NAME AS field_value
                 FROM stg_rec sr
                WHERE sr.NAME IS NOT NULL
                  AND sr.NAME IN
                         (SELECT NAME
                          FROM stg_rec 
                          GROUP BY NAME HAVING COUNT(1)>1)
                UNION ALL
               -- updating from an unknown node name
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.UNKNOWN_NODE' AS item_key,
                      'Old Node Name' AS field_name,
                      sr.old_name AS field_value
                 FROM stg_rec sr
                WHERE sr.NAME IS NOT NULL
                  AND sr.old_name IS NOT NULL
                  AND sr.action_type = c_action_type_upd
                  AND sr.db_old_name IS NULL
                UNION ALL
               -- updating an unknown node name
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.UNKNOWN_NODE' AS item_key,
                      'Node Name' AS field_name,
                      sr.NAME AS field_value
                 FROM stg_rec sr
                WHERE sr.NAME IS NOT NULL
                  AND sr.old_name IS NULL
                  AND sr.action_type = c_action_type_upd
                  AND sr.db_cur_name IS NULL
                UNION ALL
               -- deleting an unknown node name
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.UNKNOWN_NODE' AS item_key,
                      'Node Name' AS field_name,
                      sr.NAME AS field_value
                 FROM stg_rec sr
                WHERE sr.NAME IS NOT NULL
                  AND sr.action_type = c_action_type_del
                  AND sr.db_cur_name IS NULL
             ) E
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_node_name_err;

PROCEDURE p_rpt_parent_node_name_err
( p_import_file_id  IN     stage_hierarchy_import_record.import_file_id%TYPE,
  p_hierarchy_id    IN     node.hierarchy_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_parent_node_name_err');
   c_release_level      CONSTANT execution_log.release_level%TYPE := '2.0';

   v_stage                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- node name validation
   v_stage := 'INSERT import_record_error';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   (  SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
             E.import_file_id,
             E.import_record_id,
             E.item_key,
             E.field_name AS param1,
             E.field_value AS param2,
             NULL AS param3,
             NULL AS param4,
             c_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*,
                         UPPER(np.name)     AS db_cur_parent_name,
                         UPPER(np_old.name) AS db_old_parent_name,
                         UPPER(np_new.name) AS db_new_parent_name,
                         UPPER(sp.name)     AS stg_new_parent_name
                    FROM stage_hierarchy_import_record s,
                         node n,
                         node np,   -- node parent
                         node np_old,
                         node np_new,
                         stage_hierarchy_import_record sp   -- staged node parent
                   WHERE s.import_file_id = p_import_file_id
                      -- outer join by node name to find database values
                     AND UPPER(n.name (+))  = UPPER(NVL(s.old_name, s.name))
                     AND n.hierarchy_id (+) = p_hierarchy_id
                     AND n.parent_node_id = np.node_id (+)
                      -- outer join to find old parent node
                     AND UPPER(np_old.name (+))  = UPPER(s.old_parent_node_name)
                     AND np_old.hierarchy_id (+) = p_hierarchy_id
                      -- outer join to find new parent node in database
                     AND UPPER(np_new.name (+))  = UPPER(s.parent_node_name)
                     AND np_new.hierarchy_id (+) = p_hierarchy_id
                      -- outer join to find new parent node in stage
                     AND UPPER(sp.name (+))    = UPPER(s.parent_node_name)
                     AND sp.import_file_id (+) = p_import_file_id
               ), 
               -- 08/18/2017  G6-2862 the existing hierarchy
               existing_hierarchy AS
               (
               SELECT UPPER(n.name) name,UPPER(par.name) parent_name
               FROM node n,
                    node par
              WHERE n.parent_node_id = par.node_id(+)
                AND n.hierarchy_id = p_hierarchy_id
                AND par.hierarchy_id(+) = p_hierarchy_id
                    AND n.is_deleted = 0 
               ),               
               -- 08/18/2017  G6-2862 the new hierarchy that will be post this new addition and update
            new_hier_set_up AS
            (SELECT n.name new_name,
                    NVL (UPPER (stg.parent_node_name), n.parent_name) new_par,
                    stg.*
               FROM existing_hierarchy n,
                    (SELECT s.*
                       FROM stg_rec s
                      WHERE s.action_type = c_action_type_upd) stg
              WHERE n.name = UPPER (stg.name(+))
             UNION ALL
             SELECT UPPER (s.name) new_name,
                    UPPER (parent_node_name) new_par,
                    s.*
               FROM stg_rec s
              WHERE s.action_type = c_action_type_add 
              ),           
            get_cycling_nodes AS
          ( SELECT 'upd',c.*,
               SUM (is_cycling) OVER (PARTITION BY error_node) act_cycling
              FROM (SELECT CONNECT_BY_ISCYCLE is_cycling,
                           LEVEL lvl,
                           CONNECT_BY_ROOT UPPER(sr.name) error_node,
                           sr.*
                      FROM new_hier_set_up sr
                     START WITH new_name IN (SELECT UPPER(s.name)
                                               FROM stg_rec s
                                              WHERE s.action_type = c_action_type_upd)
                   CONNECT BY NOCYCLE  sr.new_name = PRIOR sr.new_par) c
           UNION ALL           
           SELECT 'add',c.*,
                   SUM (is_cycling) OVER (PARTITION BY error_node) act_cycling
             FROM (SELECT CONNECT_BY_ISCYCLE is_cycling,
                           LEVEL lvl,
                           CONNECT_BY_ROOT UPPER(sr.name) error_node,
                           sr.*
                      FROM new_hier_set_up sr
                     START WITH new_name IN (SELECT UPPER(s.name)
                                               FROM stg_rec s
                                              WHERE s.action_type = c_action_type_add)
                   CONNECT BY NOCYCLE  sr.new_name = PRIOR sr.new_par) c
           )   
              -- updating from an unknown parent node name
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.UNKNOWN_NODE' AS item_key,
                      'Old Parent Node Name' AS field_name,
                      sr.old_parent_node_name AS field_value
                 FROM stg_rec sr
                WHERE sr.name IS NOT NULL
                  AND sr.old_parent_node_name IS NOT NULL
                  AND sr.action_type = c_action_type_upd
                  AND sr.db_old_parent_name IS NULL
                UNION ALL
               -- updating from a different old parent node name
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.DIFF_OLD_PARENT' AS item_key,
                      'Current Parent Node Name' AS field_name,
                      sr.db_cur_parent_name AS field_value
                 FROM stg_rec sr
                WHERE sr.name IS NOT NULL
                  AND sr.old_parent_node_name IS NOT NULL
                  AND sr.action_type = c_action_type_upd
                  AND sr.old_parent_node_name != db_cur_parent_name
                UNION ALL
               -- unknown parent node name
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.UNKNOWN_PARENT_NODE' AS item_key,
                      'Parent Node Name' AS field_name,
                      sr.parent_node_name AS field_value
                 FROM stg_rec sr
                WHERE sr.name IS NOT NULL
                  AND sr.parent_node_name IS NOT NULL
                  AND sr.db_new_parent_name IS NULL
                  AND sr.stg_new_parent_name IS NULL
                UNION ALL
               -- node same as parent node
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.PARENT_SAME_AS_NODE' AS item_key,
                      'Parent Node Name' AS field_name,
                      sr.parent_node_name AS field_value
                 FROM stg_rec sr
                WHERE sr.name IS NOT NULL
                  AND sr.parent_node_name IS NOT NULL
                  AND UPPER(sr.name) = UPPER(sr.parent_node_name)
--                UNION ALL -- Bug # 60801 --Commented this as we are ignoring old_parent_node_name column from the file.
--               -- updating parent without specifying old parent
--               SELECT sr.import_file_id,
--                      sr.import_record_id,
--                      'system.errors.DIFF_PARENT_NO_OLD_PARENT' AS item_key,
--                      'Current Parent Node Name' AS field_name,
--                      sr.db_cur_parent_name AS field_value
--                 FROM stg_rec sr
--                WHERE sr.name IS NOT NULL
--                  AND sr.parent_node_name IS NOT NULL
--                  AND sr.action_type = c_action_type_upd
--                  AND sr.old_parent_node_name IS NULL
--                  AND UPPER(sr.parent_node_name) != sr.db_cur_parent_name
    --08/18/2017  G6-2862 get the nodes that leads to cycling in the hierarchy
    --08/18/2017  G6-2862 NOT EXISTS is added that in case the node,parent node combination already exists that cannot be a invalid record to correct
               UNION ALL
               SELECT import_file_id,
                      import_record_id,
                      'system.errors.CYCLIC_PARENT_NODE' AS item_key,
                      'Node Name' AS field_name,error_node AS field_value
                 FROM get_cycling_nodes a
                WHERE import_record_id IS NOT NULL
                  AND act_cycling > 0
                  AND lvl=1 
                  AND EXISTS (select '1' from get_cycling_nodes b where a.error_node = b.error_node AND a.error_node = b.new_par)
                  AND NOT EXISTS (SELECT '1' FROM existing_hierarchy ch where ch.name = a.error_node AND ch.parent_name = a.new_par)                   
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_parent_node_name_err;

-- Reports node deletion errors
PROCEDURE p_rpt_delete_node_err
( p_import_file_id  IN     stage_hierarchy_import_record.import_file_id%TYPE,
  p_hierarchy_id    IN     node.hierarchy_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_delete_node_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_stage                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN

   -- node deletion validation
   v_stage := 'INSERT import_record_error';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   (  SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
             e.import_file_id,
             e.import_record_id,
             e.item_key,
             e.field_name AS param1,
             e.field_value AS param2,
             NULL AS param3,
             NULL AS param4,
             c_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.import_file_id,
                         s.import_record_id,
                         s.name,
                         s.move_to_node_name,
                         n.node_id,
                         p_hierarchy_id AS hierarchy_id
                    FROM stage_hierarchy_import_record s,
                         node n
                   WHERE s.import_file_id = p_import_file_id
                      -- deleting record
                     AND s.action_type = c_action_type_del
                      -- outer join by node name to find database values
--                     AND UPPER(s.name)  = n.name (+)      --07/20/2015
                     AND s.name  = n.name (+)          --07/20/2015             
                     AND p_hierarchy_id = n.hierarchy_id (+)
               )
               -- updating to an unknown node name
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.UNKNOWN_MOVE_TO_NODE' AS item_key,
                      'Move To Node Name' AS field_name,
                      sr.move_to_node_name AS field_value
                 FROM stg_rec sr,
                      node nmt,  -- move to node
                      stage_hierarchy_import_record s2
                WHERE sr.name IS NOT NULL
                  AND sr.move_to_node_name IS NOT NULL
                   -- outer join by "move to" node name to find database values
                  AND UPPER(sr.move_to_node_name) = UPPER(nmt.name (+))
                  AND sr.hierarchy_id             = nmt.hierarchy_id (+)
                   -- outer join by "move to" node name to find staged values
                  AND UPPER(sr.move_to_node_name) = UPPER(s2.name (+))
                  AND sr.import_file_id           = s2.import_file_id (+)
                   -- no move to records found
                  AND nmt.ROWID IS NULL
                  AND s2.ROWID  IS NULL
                UNION ALL
               -- deleting node with users and no move to node
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.DELETE_NODE_WITH_USERS' AS item_key,
                      'Node Name' AS field_name,
                      sr.name AS field_value
                 FROM stg_rec sr
                WHERE sr.node_id IS NOT NULL
                  AND sr.move_to_node_name IS NULL
                  AND EXISTS
                      ( SELECT 1
                          FROM user_node un
                         WHERE un.node_id = sr.node_id
                           AND un.status = 1 -- active
                      )
                UNION ALL
               -- deleting node with active child nodes
               SELECT DISTINCT
                      sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.DELETE_NODE_WITH_CHILDREN' AS item_key,
                      'Node Name' AS field_name,
                      sr.name AS field_value
                 FROM stg_rec sr,
                      node nc -- child node
                WHERE sr.node_id IS NOT NULL
                  AND sr.node_id = nc.parent_node_id
                  AND nc.is_deleted = 0
                   -- parent not changing and child not deleting in the staged file
                  AND NOT EXISTS
                      ( -- child updating to another parent
                        SELECT 1
                          FROM stage_hierarchy_import_record s2
                         WHERE s2.import_file_id = sr.import_file_id
                           AND UPPER(s2.name) = UPPER(nc.name)
                           AND s2.action_type = c_action_type_upd
                           AND UPPER(s2.parent_node_name) != UPPER(sr.name)
                         UNION
                        -- child record deleting   05/14/2012 fix
                        SELECT 1
                          FROM stage_hierarchy_import_record s2
                         WHERE s2.import_file_id = sr.import_file_id
                           AND UPPER(s2.name) = UPPER(nc.name)
                           AND s2.action_type = c_action_type_del
                      )
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_delete_node_err;

-- Reports node characteristic errors
PROCEDURE p_rpt_node_characteristic_err
( p_import_file_id  IN     stage_hierarchy_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_node_characteristic_t');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_stage                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- validate user characteristic field requirements
   v_stage := 'INSERT import_record_error (field requirements)';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   (  SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
             e.import_file_id,
             e.import_record_id,
             e.item_key,
             e.field_name AS param1,
             e.field_value AS param2,
             NULL AS param3,
             NULL AS param4,
             c_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec_pivot AS
               (  -- pivot the characteristic IDs columns with its associated import file/record ID
                  SELECT s.import_file_id,
                         s.import_record_id,
                         s.name,
                         s.node_type_id,
                         p.column_value AS field_position,
                         DECODE(p.column_value,
                           1, s.characteristic_id1,
                           2, s.characteristic_id2,
                           3, s.characteristic_id3,
                           4, s.characteristic_id4,
                           5, s.characteristic_id5,
                           6, s.characteristic_id6,     --10/08/2015
                           7, s.characteristic_id7,
                           8, s.characteristic_id8,
                           9, s.characteristic_id9,
                           10, s.characteristic_id10    --10/08/2015
                         ) AS characteristic_id,
                         DECODE(p.column_value,
                           1, s.characteristic_name1,
                           2, s.characteristic_name2,
                           3, s.characteristic_name3,
                           4, s.characteristic_name4,
                           5, s.characteristic_name5,
                           6, s.characteristic_name6,     --10/08/2015
                           7, s.characteristic_name7,
                           8, s.characteristic_name8,
                           9, s.characteristic_name9,
                           10, s.characteristic_name10    --10/08/2015
                         ) AS characteristic_name,
                         DECODE(p.column_value,
                           1, s.characteristic_value1,
                           2, s.characteristic_value2,
                           3, s.characteristic_value3,
                           4, s.characteristic_value4,
                           5, s.characteristic_value5,
                           6, s.characteristic_value6,     --10/08/2015
                           7, s.characteristic_value7,
                           8, s.characteristic_value8,
                           9, s.characteristic_value9,
                           10, s.characteristic_value10    --10/08/2015
                         ) AS characteristic_value
                    FROM stage_hierarchy_import_record s,
                         ( -- select a row for each characteristic column
                            SELECT LEVEL AS column_value
                              FROM dual
                           CONNECT BY LEVEL <= 10 --5 --10/08/2015
                         ) p
                   WHERE s.import_file_id = p_import_file_id
               )
               -- all characteristic fields required when any characteristic field contains a value
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'system.errors.DEPENDENCY_REQUIRED' AS item_key,
                      'Characteristic Value '  || srp.field_position AS field_name,
                      srp.characteristic_value AS field_value
                 FROM stg_rec_pivot srp
                WHERE (  srp.characteristic_id    IS NOT NULL
                      OR srp.characteristic_name  IS NOT NULL
                      OR srp.characteristic_value IS NOT NULL
                      )
                  AND (  srp.characteristic_id    IS NULL
                      OR srp.characteristic_name  IS NULL
                      OR srp.characteristic_value IS NULL
                      )
                UNION ALL
               -- re-evaluate characteristic ID matches name/domain
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'system.errors.DEPENDENCY_REQUIRED' AS item_key,
                      'Characteristic Name '  || srp.field_position AS field_name,
                      'Characteristic Value ' || srp.field_position AS field_value
                 FROM stg_rec_pivot srp,
                      vue_characteristic_name vcm
                WHERE srp.characteristic_id    IS NOT NULL
                  AND srp.characteristic_name  IS NOT NULL
                   -- join characteristic name to view
                  AND vcm.cm_name = UPPER(srp.characteristic_name)
                  AND vcm.domain_id = srp.node_type_id
                  AND vcm.characteristic_type = 'NT'
                   -- IDs do not match
                  AND vcm.characteristic_id != srp.characteristic_id
                UNION ALL
               -- ensure record contains all required node characteristics
               SELECT DISTINCT
                      sc.import_file_id,
                      sc.import_record_id,
                      'system.errors.MISSING_REQUIRED_CHARACTERISTICS' AS item_key,
                      'Node Name' AS field_name,
                      sc.name AS field_value
                 FROM ( -- get count of required node characteristics by domain ID
                        SELECT c.domain_id,
                               COUNT(c.characteristic_id) AS req_cnt
                          FROM characteristic c
                         WHERE c.characteristic_type = 'NT'
                           AND c.is_required = 1
                           AND c.is_active   = 1
                         GROUP BY c.domain_id
                      ) rc,
                      ( -- get count of required user characteristics in stage record
                        SELECT srp.import_file_id,
                               srp.import_record_id,
                               srp.node_type_id,
                               srp.name,
                               COUNT(DISTINCT c.characteristic_id) AS stg_req_cnt
                          FROM stg_rec_pivot srp,
                               characteristic c
                            -- outer join since no characteristic may be required
                         WHERE c.characteristic_type (+) = 'NT'
                           AND c.is_required (+) = 1
                           AND c.is_active   (+) = 1
                           AND c.characteristic_id (+) = srp.characteristic_id
                         GROUP BY srp.import_file_id,
                               srp.import_record_id,
                               srp.node_type_id,
                               srp.name
                      ) sc
                WHERE sc.node_type_id = rc.domain_id
                  AND sc.stg_req_cnt < rc.req_cnt
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ', ' || v_rec_cnt || ' records', NULL);


   -- validate node characteristic field values based on characteristic data type
   v_stage := 'INSERT import_record_error (field values)';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   (  SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
             e.import_file_id,
             e.import_record_id,
             e.item_key,
             e.field_name AS param1,
             e.field_value1 AS param2,
             e.field_value2 AS param3,
             NULL AS param4,
             c_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec_pivot AS
               (  -- get associated characteristic data
                  SELECT pv.*,
                         -- indicate whether value is the specified type
                         DECODE( LOWER(c.characteristic_data_type),
                           'int', fnc_is_number(pv.characteristic_value, 1),
                           'decimal', fnc_is_number(pv.characteristic_value), --10/07/2015 replaced 'dec' with 'decimal'
                           'date', fnc_is_date(pv.characteristic_value),
                           NULL
                         ) AS is_type,
                         LOWER(c.characteristic_data_type) AS characteristic_data_type,
                         c.min_value,
                         c.max_value,
                         c.max_size,
                         c.date_start,
                         c.date_end,
                         c.pl_name
                    FROM ( -- pivot characteristic columns into rows
                           SELECT s.import_file_id,
                                  s.import_record_id,
                                  p.column_value AS field_position,
                                  DECODE(p.column_value,
                                    1, s.characteristic_id1,
                                    2, s.characteristic_id2,
                                    3, s.characteristic_id3,
                                    4, s.characteristic_id4,
                                    5, s.characteristic_id5,
                                    6, s.characteristic_id6,     --10/08/2015
                                    7, s.characteristic_id7,
                                    8, s.characteristic_id8,
                                    9, s.characteristic_id9,
                                    10, s.characteristic_id10    --10/08/2015
                                  ) AS characteristic_id,
                                  DECODE(p.column_value,
                                    1, s.characteristic_name1,
                                    2, s.characteristic_name2,
                                    3, s.characteristic_name3,
                                    4, s.characteristic_name4,
                                    5, s.characteristic_name5,
                                    6, s.characteristic_name6,     --10/08/2015
                                    7, s.characteristic_name7,
                                    8, s.characteristic_name8,
                                    9, s.characteristic_name9,
                                    10, s.characteristic_name10    --10/08/2015
                                  ) AS characteristic_name,
                                  DECODE(p.column_value,
                                    1, s.characteristic_value1,
                                    2, s.characteristic_value2,
                                    3, s.characteristic_value3,
                                    4, s.characteristic_value4,
                                    5, s.characteristic_value5,
                                    6, s.characteristic_value6,     --10/08/2015
                                    7, s.characteristic_value7,
                                    8, s.characteristic_value8,
                                    9, s.characteristic_value9,
                                    10, s.characteristic_value10    --10/08/2015
                                  ) AS characteristic_value
                             FROM stage_hierarchy_import_record s,
                                  ( -- select a row for each characteristic column
                                     SELECT LEVEL AS column_value
                                       FROM dual
                                    CONNECT BY LEVEL <= 10 --5 --10/08/2015
                                  ) p
                            WHERE s.import_file_id = p_import_file_id
                         ) pv,
                         characteristic c
                      -- ensure all import characteristic fields have values
                   WHERE pv.characteristic_id    IS NOT NULL
                     AND pv.characteristic_name  IS NOT NULL
                     AND pv.characteristic_value IS NOT NULL
                     AND pv.characteristic_id = c.characteristic_id
               )
               -- validate boolean characteristic
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'user.characteristic.errors.BOOLEAN' AS item_key,
                      'CHARACTERISTIC_VALUE' || srp.field_position AS field_name,
                      srp.characteristic_value AS field_value1,
                      NULL AS field_value2
                 FROM stg_rec_pivot srp
                WHERE srp.characteristic_data_type = 'boolean'
                   -- find non-bolean values
                  AND NOT (  LOWER(srp.characteristic_value) = 'true'
                          OR LOWER(srp.characteristic_value) = 'false'
                          )
                UNION ALL
               -- validate integer characteristic is an integer
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'user.characteristic.errors.INTEGER' AS item_key,
                      'CHARACTERISTIC_VALUE' || srp.field_position AS field_name,
                      srp.characteristic_value AS field_value1,
                      NULL AS field_value2
                 FROM stg_rec_pivot srp
                WHERE srp.characteristic_data_type = 'int'
                  AND srp.is_type = 0
                UNION ALL
               -- validate decimal characteristic is a decimal
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'user.characteristic.errors.DECIMAL' AS item_key,
                      'CHARACTERISTIC_VALUE' || srp.field_position AS field_name,
                      srp.characteristic_value AS field_value1,
                      NULL AS field_value2
                 FROM stg_rec_pivot srp
                WHERE srp.characteristic_data_type = 'decimal' --10/07/2015 replaced 'dec' with 'decimal'
                  AND srp.is_type = 0
                UNION ALL
               -- validate integer/decimal characteristic within range
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'user.characteristic.errors.RANGE' AS item_key,
                      'hierarchy.hierarchylabel.CHARACTERISTIC_VALUE' AS field_name,
                      TO_CHAR(srp.min_value) AS field_value1,
                      TO_CHAR(srp.max_value) AS field_value2
                 FROM stg_rec_pivot srp
                WHERE srp.characteristic_data_type IN ('int','decimal') --10/07/2015 replaced 'dec' with 'decimal'
                  AND srp.is_type = 1
                   -- find values outside of characteristic range
                  AND NOT (TO_NUMBER(srp.characteristic_value)
                           BETWEEN NVL(srp.min_value, TO_NUMBER(srp.characteristic_value))
                               AND NVL(srp.max_value, TO_NUMBER(srp.characteristic_value))
                          )
                UNION ALL
               -- validate text characteristic length
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'user.characteristic.errors.SIZE' AS item_key,
                      'hierarchy.hierarchylabel.CHARACTERISTIC_VALUE' AS field_name,
                      TO_CHAR(srp.max_size) AS field_value1,
                      NULL AS field_value2
                 FROM stg_rec_pivot srp
                WHERE srp.characteristic_data_type = 'txt'
                   -- find values that exceed the max length
                  AND LENGTH(srp.characteristic_value) > srp.max_size
                UNION ALL
               -- validate date characteristic is a date
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'user.characteristic.errors.DATE_INVALID' AS item_key,
                      'CHARACTERISTIC_VALUE' || srp.field_position AS field_name,
                      srp.characteristic_value AS field_value1,
                      NULL AS field_value2
                 FROM stg_rec_pivot srp
                WHERE srp.characteristic_data_type = 'date'
                  AND srp.is_type = 0
                UNION ALL
               -- validate date characteristic with range
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'user.characteristic.errors.DATE_RANGE' AS item_key,
                      'CHARACTERISTIC_VALUE' || srp.field_position AS field_name,
                      TO_CHAR(srp.date_start, 'DD-MON-YYYY') AS field_value1,
                      TO_CHAR(srp.date_end,   'DD-MON-YYYY') AS field_value2
                 FROM stg_rec_pivot srp
                WHERE srp.characteristic_data_type = 'date'
                  AND srp.is_type = 1
                   -- find values outside of characteristic range
                  AND NOT (TO_DATE(srp.characteristic_value, 'MM/DD/YYYY')
                           BETWEEN NVL(srp.date_start, TO_DATE(srp.characteristic_value, 'MM/DD/YYYY'))
                               AND NVL(srp.date_end,   TO_DATE(srp.characteristic_value, 'MM/DD/YYYY'))
                          )
                UNION ALL
               -- validate single characteristic in pick list
               SELECT cms.import_file_id,
                      cms.import_record_id,
                      'admin.fileload.errors.INVALID_PROPERTY' AS item_key,
                      'CHARACTERISTIC_VALUE' || cms.field_position AS field_name,
                      cms.characteristic_value AS field_value1,
                      NULL AS field_value2
                 FROM ( -- get CMS record associated with pick list value
                        SELECT ccv.content_id,
                               srp.*
                          FROM stg_rec_pivot srp,
                               vw_cms_code_value ccv
                         WHERE srp.characteristic_data_type = 'single_select'
                            -- outer join characteristic fields to CMS table
                           AND ccv.asset_code (+) = srp.pl_name
                           AND ccv.locale     (+) = c_cms_locale
                           AND ccv.cms_status (+) = c_cms_status
                           AND LOWER(ccv.cms_code (+)) = LOWER(srp.characteristic_value)
                      ) cms
                   -- find values not in the pick list
                WHERE cms.content_id IS NULL
                UNION ALL
               -- validate multi characteristic in pick list
               SELECT cms.import_file_id,
                      cms.import_record_id,
                      'admin.fileload.errors.INVALID_PROPERTY' AS item_key,
                      'CHARACTERISTIC_VALUE' || cms.field_position AS field_name,
                      cms.picklist_item AS field_value1,
                      NULL AS field_value2
                 FROM ( -- get CMS record associated with individual pick list value
                        SELECT ccv.content_id,
                               pl.*
                          FROM ( -- parse pick list items from multi-select characteristic
                                 SELECT p.column_value AS picklist_item,
                                        ms.*
                                   FROM ( -- get multi-select characteristics
                                          SELECT srp.*,
                                                 '~' AS delimiter,
                                                 '~' || srp.characteristic_value || '~' AS picklist,
                                                 LENGTH(srp.characteristic_value) - LENGTH(REPLACE(srp.characteristic_value,'~',NULL)) +1 AS field_cnt
                                            FROM stg_rec_pivot srp
                                           WHERE srp.characteristic_data_type = 'multi_select'
                                        ) ms,
                                        -- parse characteristic into individual pick list codes
                                        -- pivoting pick list codes into separate records
                                        TABLE( CAST( MULTISET(
                                           SELECT SUBSTR(ms.picklist,
                                                   INSTR(ms.picklist, ms.delimiter, 1, LEVEL)+1, 
                                                   INSTR(ms.picklist, ms.delimiter, 1, LEVEL+1) - INSTR(ms.picklist, ms.delimiter, 1, LEVEL)-1 
                                                  )
                                             FROM dual
                                          CONNECT BY LEVEL <= ms.field_cnt
                                        ) AS sys.odcivarchar2list ) ) p
                               ) pl,
                               vw_cms_code_value ccv
                            -- outer join characteristic fields to CMS table
                         WHERE ccv.asset_code (+) = pl.pl_name
                           AND ccv.locale     (+) = c_cms_locale
                           AND ccv.cms_status (+) = c_cms_status
                           AND LOWER(ccv.cms_code (+)) = LOWER(pl.picklist_item)
                      ) cms
                   -- find values not in the pick list
                WHERE cms.content_id IS NULL
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ', ' || v_rec_cnt || ' records', NULL);


EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_node_characteristic_err;

PROCEDURE p_rpt_misc_err
( p_import_file_id  IN     stage_hierarchy_import_record.import_file_id%TYPE,
  p_hierarchy_id    IN     node.hierarchy_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_misc_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_stage                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
   v_root_node_exists NUMBER:=0;
BEGIN

-- Check if root node already exists for the hierarchy

 SELECT COUNT(1) INTO v_root_node_exists
 FROM node n
 WHERE parent_node_id IS NULL
 AND is_deleted = 0
 AND hierarchy_id = p_hierarchy_id
 AND NOT EXISTS
 (SELECT * FROM stage_hierarchy_import_record WHERE import_file_id = p_import_file_id AND name = n.name AND parent_node_name IS NULL);-- Bug # 63155
   -- validate miscellaneous requirements
   v_stage := 'INSERT import_record_error';
   INSERT INTO import_record_error
   ( import_record_error_id,
     import_file_id,
     import_record_id,
     item_key,
     param1,
     param2,
     param3,
     param4,
     created_by,
     date_created
   )
   (  SELECT import_record_error_pk_sq.NEXTVAL AS import_record_error_id,
             e.import_file_id,
             e.import_record_id,
             e.item_key,
             e.field_name AS param1,
             e.field_value AS param2,
             NULL AS param3,
             NULL AS param4,
             c_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*
                    FROM stage_hierarchy_import_record s
                   WHERE s.import_file_id = p_import_file_id
               )
               -- action type must be known
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.UNKNOWN_ACTION_TYPE' AS item_key,
                      'Action Type' AS field_name,
                      sr.action_type AS field_value
                 FROM stg_rec sr
                   -- action type not in known value
                WHERE sr.action_type NOT IN
                      ( c_action_type_add,
                        c_action_type_upd,
                        c_action_type_del
                      )
                UNION ALL
               -- node type ID required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.NODE_TYPE_MUST_EXIST' AS item_key,
                      'Node Type Id' AS field_name,
                      TO_CHAR(sr.node_type_id) AS field_value
                 FROM stg_rec sr
                WHERE sr.node_type_id IS NULL
                AND g_is_node_type_req <> 0--08/16/2015
                UNION ALL
               -- node type ID not found in database
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.NODE_TYPE_MUST_EXIST' AS item_key,
                      'Node Type Id' AS field_name,
                      TO_CHAR(sr.node_type_id) AS field_value
                 FROM stg_rec sr,
                      node_type nt
                WHERE sr.node_type_id IS NOT NULL
                   -- outer join to node type table
                  AND sr.node_type_id = nt.node_type_id (+)
                   -- node type not found in database
                  AND nt.ROWID IS NULL
                  UNION ALL                  --Bug # 63237 --07/21/2015
                  SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.ROOT_NODE_ALREADY_EXISTS' AS item_key,
                      'Root Node' AS field_name,
                      sr.name AS field_value
                 FROM stg_rec sr
                WHERE sr.action_type = 'add'
                AND sr.parent_node_name IS NULL
                AND v_root_node_exists > 0     
                UNION ALL
                SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.ROOT_NODE_ALREADY_EXISTS' AS item_key,
                      'Root Node' AS field_name,
                      sr.name AS field_value
                 FROM stg_rec sr
                WHERE sr.action_type = 'upd'
                AND sr.old_name IS NULL
                AND sr.parent_node_name IS NULL
                AND v_root_node_exists > 0
                UNION ALL--05/04/2017
                SELECT sr.import_file_id,
                       sr.import_record_id,
                       'system.errors.ONE_PARENT_NODE' AS item_key,
                       'Root Node'                     AS field_name,
                       sr.name                         AS field_value
                 FROM stg_rec SR
                WHERE     (SELECT COUNT (1)
              FROM stg_rec sr
               WHERE sr.parent_node_name IS NULL) > 1
                AND sr.parent_node_name IS NULL
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   
   SELECT COUNT(1) INTO p_total_err_count -- Added this code to get the distinct products with the errors to show on file loads admin page. Bug # 63268
FROM STAGE_HIERARCHY_IMPORT_RECORD h
WHERE h.import_file_id = p_import_file_id
AND EXISTS
  (SELECT import_record_id
  FROM IMPORT_RECORD_ERROR ire
  WHERE ire.import_record_id = h.import_record_id
  AND ire.import_file_id     = h.import_file_id
  );

--   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_misc_err;

PROCEDURE p_imp_node
( p_import_file_id  IN     stage_hierarchy_import_record.import_file_id%TYPE,
  p_hierarchy_id    IN     node.hierarchy_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_node');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- import staged records so all staged records have an associated node record
   v_msg := 'MERGE node (initial)';
   MERGE INTO node d
   USING (  -- get stage records
   SELECT n.node_id,
                   p_hierarchy_id AS hierarchy_id,
                   s.action_type,   --12/21/2015 Bug 64996
                   s.name AS name,--07/07/2015 Removed Upper function usage.Bug # 63197 
                   CASE WHEN g_is_node_type_req = 0 THEN NVL(s.node_type_id,g_node_type_id) ELSE s.node_type_id END node_type_id,--08/16/2015
                   'tmp' AS path,         -- temp value
                   CASE WHEN parent_node_name IS NOT NULL THEN -1 ELSE NULL END AS parent_node_id,  -- temp value
                   s.description,  --07/16/2015
                   DECODE(s.action_type,
                     'del', 1,
                     0
                   ) AS is_deleted
              FROM stage_hierarchy_import_record s,
                   node n
             WHERE s.import_file_id = p_import_file_id
                -- outer join to existing node records
               AND upper(NVL(s.old_name, s.name)) = upper(n.name (+))   --07/16/2015 Removed UPPER function usage  -- 05/22/2018 added upper function
               AND p_hierarchy_id = n.hierarchy_id (+)
         ) s
      ON (d.node_id = NVL(s.node_id, -1))
    WHEN MATCHED THEN
      UPDATE
         SET d.name                      = DECODE(s.action_type,'del',s.name||'-'||to_char(SYSDATE, 'YYYY-MM-DD-HH:MI:SS'), s.name), --12/21/2015 added decode Bug 64996
             d.is_deleted                = s.is_deleted,
             -- update only with not null stage values
             d.node_type_id              = nvl(s.node_type_id, d.node_type_id),             
             -- track update
             d.modified_by   = c_created_by,
             d.date_modified = g_timestamp,
             d.version       = d.version + 1,
             d.description=    s.description   --07/16/2015
          -- only update if data differs (decode handles null values)
       WHERE NOT (   d.name       = s.name
                 AND d.is_deleted = s.is_deleted
                 AND DECODE(d.description, s.description,1,0) = 1   --07/16/2015
                 AND DECODE(d.node_type_id,              NVL(s.node_type_id, d.node_type_id),                           1, 0) = 1                 
                 )
    WHEN NOT MATCHED THEN
      INSERT
      (  node_id,
         node_type_id,
         name,
         description,
         path,
         parent_node_id,  
         hierarchy_id,
         is_deleted,       
         created_by,
         date_created,
         modified_by,
         date_modified,
         version,
         roster_node_id  -- 11/28/2019
      )
      VALUES
      (  node_pk_sq.NEXTVAL,
         s.node_type_id,
         s.name,
         s.description,
         s.path,
         s.parent_node_id,       
         s.hierarchy_id,
         s.is_deleted,        
         c_created_by,  -- created_by,
         g_timestamp,   -- date_created,
         NULL,          -- modified_by,
         NULL,          -- date_modified,
         0,              -- version
         fnc_randomuuid -- 11/28/2019
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

   -- with all records initially imported, update parent node values
   v_msg := 'MERGE node (parent)';
   MERGE INTO node d
   USING (  -- get stage records
            SELECT n.node_id,
                   p.node_id AS parent_node_id,
                   s.name,
                   s.parent_node_name
                   ,s.description  --07/16/2015
              FROM stage_hierarchy_import_record s,
                   node n,
                   node p
             WHERE s.import_file_id = p_import_file_id
                -- current node
               AND s.name         = n.name
               AND p_hierarchy_id = n.hierarchy_id
                -- parent node
               AND LOWER(s.parent_node_name) = LOWER(p.name) --08/01/2016
               AND p_hierarchy_id     = p.hierarchy_id
         ) s
      ON (d.node_id = s.node_id)
    WHEN MATCHED THEN
      UPDATE
         SET d.parent_node_id = s.parent_node_id,
             -- track update (if not already time stamped by this process)
             d.modified_by   = DECODE(NVL(d.date_modified, d.date_created), g_timestamp, d.modified_by,   c_created_by),
             d.date_modified = DECODE(NVL(d.date_modified, d.date_created), g_timestamp, d.date_modified, g_timestamp),
             d.version       = DECODE(NVL(d.date_modified, d.date_created), g_timestamp, d.version,       d.version + 1),
             d.description  =  s.description   --07/16/2015

          -- only update if data differs
       WHERE d.parent_node_id != s.parent_node_id;

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

   -- with all parent values set, derive node paths
   v_msg := 'MERGE node (path)';
   MERGE INTO node d
   USING (  -- get node paths
   SELECT node_id,REPLACE (path,'~~','/') path FROM (--08/20/2015 BUG # 63768
SELECT n.node_id,
                   -- build node path
                   REPLACE(SYS_CONNECT_BY_PATH(name, '~~'), CONNECT_BY_ROOT n.name, NULL) AS path--08/20/2015 BUG # 63768 Fix for the issue sys_connect_by_path ORA-01489: result of string concatenation is too long 
              FROM node n
            -- WHERE n.is_deleted = 0 --01/04/2016
                -- build top down hierarchy
                START WITH (   n.hierarchy_id = p_hierarchy_id
                        AND n.parent_node_id IS NULL
                        )
           CONNECT BY PRIOR n.node_id = n.parent_node_id)
--            SELECT n.node_id,
--                   -- build node path
--                   REPLACE(SYS_CONNECT_BY_PATH(n.name, '/'), CONNECT_BY_ROOT n.name, NULL) AS path
--              FROM node n
--             WHERE n.is_deleted = 0
--                -- build top down hierarchy
--             START WITH (   n.hierarchy_id = p_hierarchy_id
--                        AND n.parent_node_id IS NULL
--                        )
--           CONNECT BY PRIOR n.node_id = n.parent_node_id
         ) s
      ON (d.node_id = s.node_id)
    WHEN MATCHED THEN
      UPDATE
         SET d.path = s.path,
             -- track update (if not already time stamped by this process)
             d.modified_by   = DECODE(NVL(d.date_modified, d.date_created), g_timestamp, d.modified_by,   c_created_by),
             d.date_modified = DECODE(NVL(d.date_modified, d.date_created), g_timestamp, d.date_modified, g_timestamp),
             d.version       = DECODE(NVL(d.date_modified, d.date_created), g_timestamp, d.version,       d.version + 1)
          -- only update if data differs
       WHERE d.path != s.path;

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_node;

PROCEDURE p_imp_node_characteristic
( p_import_file_id  IN     stage_hierarchy_import_record.import_file_id%TYPE,
  p_hierarchy_id    IN     node.hierarchy_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_node_characteristic');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- import staged records
   v_msg := 'MERGE node_characteristic';
   MERGE INTO node_characteristic d
   USING (  -- get staged data pivoting characteristic columns into rows
            SELECT pv.import_file_id,
                   pv.import_record_id,
                   pv.node_id,
                   pv.field_position,
                   pv.characteristic_id,
                   pv.characteristic_name,
                   DECODE(LOWER(c.characteristic_data_type),
                     -- replace tilde with a comma in multi select characteristics
                     'multi_select', REPLACE(pv.characteristic_value, '~', ','),
                     pv.characteristic_value
                   ) AS characteristic_value,
                   LOWER(c.characteristic_data_type) AS characteristic_data_type
              FROM ( -- pivot characteristic columns into rows
                     SELECT s.import_file_id,
                            s.import_record_id,
                            s.name,
                            n.node_id,
                            p.column_value AS field_position,
                            DECODE(p.column_value,
                              1, s.characteristic_id1,
                              2, s.characteristic_id2,
                              3, s.characteristic_id3,
                              4, s.characteristic_id4,
                              5, s.characteristic_id5,
                              6, s.characteristic_id6,
                              7, s.characteristic_id7,
                              8, s.characteristic_id8,
                              9, s.characteristic_id9,
                              10, s.characteristic_id10
                            ) AS characteristic_id,
                            DECODE(p.column_value,
                              1, s.characteristic_name1,
                              2, s.characteristic_name2,
                              3, s.characteristic_name3,
                              4, s.characteristic_name4,
                              5, s.characteristic_name5,
                              6, s.characteristic_name6,
                              7, s.characteristic_name7,
                              8, s.characteristic_name8,
                              9, s.characteristic_name9,
                              10, s.characteristic_name10
                            ) AS characteristic_name,
                            DECODE(p.column_value,
                              1, s.characteristic_value1,
                              2, s.characteristic_value2,
                              3, s.characteristic_value3,
                              4, s.characteristic_value4,
                              5, s.characteristic_value5,
                              6, s.characteristic_value6,
                              7, s.characteristic_value7,
                              8, s.characteristic_value8,
                              9, s.characteristic_value9,
                              10, s.characteristic_value10
                            ) AS characteristic_value
                       FROM stage_hierarchy_import_record s,
                            node n,
                            ( -- select a row for each characteristic column
                               SELECT LEVEL AS column_value
                                 FROM dual
                              CONNECT BY LEVEL <= 10
                            ) p
                      WHERE s.import_file_id = p_import_file_id
--                        AND UPPER(s.name)  = n.name       --07/23/2015
                        AND s.name  = n.name                --07/23/2015        
                        AND p_hierarchy_id = n.hierarchy_id
                        AND n.is_deleted = 0
                   ) pv,
                   characteristic c
             WHERE pv.characteristic_id = c.characteristic_id
                -- ensure all import characteristic fields have values
               AND pv.characteristic_id    IS NOT NULL
               AND pv.characteristic_name  IS NOT NULL
               AND pv.characteristic_value IS NOT NULL
         ) s
      ON (   d.node_id = s.node_id
         AND d.characteristic_id = s.characteristic_id
         )
    WHEN MATCHED THEN
      UPDATE
         SET d.characteristic_value = s.characteristic_value,
             -- track update
             d.modified_by    = c_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
             
          -- only update if data differs
     WHERE NOT (d.characteristic_value = s.characteristic_value)
    
    WHEN NOT MATCHED THEN
      INSERT
      (  node_characteristic_id,
         node_id,
         characteristic_id,
         characteristic_value,
         created_by,
         date_created,
         modified_by,
         date_modified,
         version,
         roster_node_char_id  -- 11/28/2019
      )
      VALUES
      (  node_characteristic_pk_sq.NEXTVAL, -- node_characteristic_id,
         s.node_id,
         s.characteristic_id,
         s.characteristic_value,
         c_created_by,  -- created_by,
         g_timestamp,   -- date_created,
         NULL,          -- modified_by,
         NULL,          -- date_modified,
         0,              -- version
         fnc_randomuuid  -- 11/28/2019
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

   -- remove node characteristic not in the staged file
   v_msg := 'DELETE node_characteristic';
   DELETE node_characteristic
    WHERE node_characteristic_id IN
          ( -- get characteristics without a matching staged characteristic
            SELECT nc.node_characteristic_id
              FROM stage_hierarchy_import_record s,
                   node n,
                   node_characteristic nc
             WHERE s.import_file_id = p_import_file_id
               AND s.action_type    = c_action_type_upd
--               AND UPPER(s.name)  = n.name      --07/23/2015
               AND s.name = n.name    --07/23/2015
               AND p_hierarchy_id = n.hierarchy_id
               AND n.is_deleted = 0
               AND n.node_id = nc.node_id
               AND nc.characteristic_id NOT IN
                   ( -- exclude characteristics in staged file
                      NVL(s.characteristic_id1,0),
                      NVL(s.characteristic_id2,0),
                      NVL(s.characteristic_id3,0),
                      NVL(s.characteristic_id4,0),
                      NVL(s.characteristic_id5,0),
                      NVL(s.characteristic_id6,0),
                      NVL(s.characteristic_id7,0),
                      NVL(s.characteristic_id8,0),
                      NVL(s.characteristic_id9,0),
                      NVL(s.characteristic_id10,0)
                   )
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_node_characteristic;

-- associates active node user members to the "move to" node
PROCEDURE p_move_user_node
( p_import_file_id  IN     stage_hierarchy_import_record.import_file_id%TYPE,
  p_hierarchy_id    IN     node.hierarchy_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_move_user_node');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- import staged records
   v_msg := 'MERGE user_node';
   MERGE INTO user_node d
   USING (  -- get users moving to node
   SELECT move_to_node_id,user_node_id,CASE WHEN role='own' AND own_count > 0 THEN 'mgr' ELSE role END role FROM (
            SELECT nmt.node_id AS move_to_node_id,
                   un.user_node_id,
                   (SELECT COUNT(1) FROM user_node WHERE node_id= nmt.node_id AND status=1 AND role='own' ) own_count,
                   un.role
              FROM stage_hierarchy_import_record s,
                   node n,
                   node nmt,     -- node move to
                   user_node un
             WHERE s.import_file_id = p_import_file_id
               AND s.action_type    = c_action_type_del
               AND s.move_to_node_name IS NOT NULL
               AND UPPER(s.name)  = UPPER(n.name)
               AND p_hierarchy_id = n.hierarchy_id
               AND UPPER(s.move_to_node_name) = UPPER(nmt.name)
               AND p_hierarchy_id             = nmt.hierarchy_id
               AND nmt.is_deleted = 0
               AND n.node_id = un.node_id
               AND un.status = 1
                -- ensure user not already associated with move to node
               AND NOT EXISTS
                   ( SELECT 1
                       FROM user_node unmt
                      WHERE unmt.node_id = nmt.node_id
                        AND unmt.user_id = un.user_id
                   ))
         ) s
      ON (d.user_node_id = s.user_node_id)
    WHEN MATCHED THEN
      UPDATE
         SET d.node_id = s.move_to_node_id,
               d.role = s.role,
             -- track update
             d.modified_by    = c_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
             
          -- only update if data differs
       WHERE NOT (d.node_id = s.move_to_node_id)
         ;

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_move_user_node;

-- closes the budget for "deleted" nodes
PROCEDURE p_close_node_budget
( p_import_file_id  IN     stage_hierarchy_import_record.import_file_id%TYPE,
  p_hierarchy_id    IN     node.hierarchy_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_close_node_budget');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- Bug #13536 Fix, Changes to Inactivate the budget that is associated with deleted node   
   v_msg := 'MERGE budget';
   MERGE INTO budget d
   USING (  -- get budgets for deleted nodes
            SELECT b.budget_id,
                   'closed' AS status
                   , s.name
              FROM stage_hierarchy_import_record s,
                   node n,
                   budget b
             WHERE s.import_file_id = p_import_file_id
               AND s.action_type    = c_action_type_del
               AND UPPER(s.name)  = n.name
               AND p_hierarchy_id = n.hierarchy_id
               AND n.node_id = b.node_id
         ) s
      ON (d.budget_id = s.budget_id)
    WHEN MATCHED THEN
      UPDATE
         SET d.status = s.status,
             -- track update
             d.modified_by    = c_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
          -- only update if data differs (decode handles null values)
       WHERE NOT (DECODE(d.status, s.status, 1, 0) = 1)
         ;

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_close_node_budget;

--03/05/2019 Added new procedure 
PROCEDURE prc_budget_move_ins
( p_import_file_id  IN NUMBER, 
  p_out_returncode  OUT NUMBER) 
IS
/*------------------------------------------------------------------------------
Purpose:  maintain BUDGET_MOVE table

Called by: Hierarchy "L"oad

Person             Date           Comments
--------------------------------------------------------------------------------
-- Gorantla        03/10/2018     initial
--                 03/15/2019     use old_name, if available (because rename hasn't happened yet)
-- 
------------------------------------------------------------------------------*/

  c_process_name           CONSTANT execution_log.process_name%TYPE:= 'PRC_BUDGET_MOVE_INS';
  v_msg                    execution_log.text_line%TYPE;
  v_stage                  VARCHAR2(500) := 'Start';
  v_insert_count           INT;

BEGIN

  p_out_returncode := 0;
  v_msg := 'Start for file_id: '||p_import_file_id;
  prc_execution_log_entry(c_process_name,1,'INFO',v_msg,NULL);
	
  v_stage := 'write existing recs to budget_move_history';
  INSERT INTO budget_move_history
  SELECT budget_move_id, 
         import_file_id, 
         import_record_id, 
         record_status, 
         node_id, 
         node_name, 
         old_parent_node_id, 
         new_parent_node_id, 
         node_own_user_id, 
         old_parent_own_user_id, 
         budget_id_list, 
         is_budget_move_needed, 
         date_processed, 
         date_created, 
         created_by, 
         sysdate date_added_to_hist
	FROM budget_move bm;
	
	v_stage := 'truncate BUDGET_MOVE';
	EXECUTE IMMEDIATE 'TRUNCATE TABLE BUDGET_MOVE';
  
    v_stage := 'INSERT INTO BUDGET_MOVE';
    INSERT INTO BUDGET_MOVE
              (budget_move_id, 
               import_file_id, 
               import_record_id, 
               record_status, 
               node_id, 
               node_name, 
               old_parent_node_id, 
               new_parent_node_id,
               budget_id_list, 
               date_processed, 
               is_budget_move_needed,
               date_created, 
               created_by)
        SELECT budget_move_pk_sq.nextval,
               stg.import_file_id,
               stg.import_record_id,
               stg.action_type,
               NULL, -- node_id
         NVL(stg.old_name,stg.name), -- 03/15/2019
               NULL, -- old_parent_node_id
               NULL, -- new_parent_node_id
               NULL, -- budget_id_list
               NULL, -- date_move_processed
               0,    -- is_budget_move_needed
               SYSDATE, -- date_created
               stg.created_by
          FROM stage_hierarchy_import_record stg
         WHERE stg.import_file_id = p_import_file_id
           AND stg.action_type IN ('del','upd')
           AND NOT EXISTS (SELECT 'x'
                             FROM import_record_error
                            WHERE import_record_id = stg.import_record_id);
									
    v_insert_count := SQL%ROWCOUNT;
	
--------------------------------------------------------------------------------
									
    v_stage := 'update budget_move.node_id, based on budget_move.node_name';
	UPDATE budget_move bm
	   SET node_id = (SELECT node_id
	                    FROM node
	                   WHERE UPPER(name) = UPPER(bm.node_name))
	 WHERE import_file_id = p_import_file_id;
									
  v_stage := 'update budget_move.old_parent_node_id, based on budget_move.node_id';
	UPDATE budget_move bm
	   SET old_parent_node_id = (SELECT parent_node_id
                                   FROM node
                                  WHERE node_id = bm.node_id)
	 WHERE import_file_id = p_import_file_id;
	
	COMMIT;
	
--------------------------------------------------------------------------------
  
  v_msg := 'Success for file_id: '||p_import_file_id||CHR(10)||
           'Recs inserted into BUDGET_MOVE: '||v_insert_count;
	prc_execution_log_entry(c_process_name,1,'INFO',v_msg,NULL);

EXCEPTION
	WHEN OTHERS THEN	
    p_out_returncode := 99;
		prc_execution_log_entry(c_process_name,1,'ERROR',v_stage||', '||SQLERRM,NULL);
END prc_budget_move_ins;

PROCEDURE p_hierarchy_verify_load (p_import_file_id  IN NUMBER, 
                                   p_load_type       IN VARCHAR2,
                                   p_hierarchy_id    IN NUMBER,                                   
                                   p_user_id         IN NUMBER,
                                   p_total_error_rec OUT NUMBER,
                                   p_out_returncode  OUT NUMBER,
                                   p_out_user_data   OUT SYS_REFCURSOR) IS
   PRAGMA AUTONOMOUS_TRANSACTION;
--  v_created_by              STAGE_HIERARCHY_IMPORT_RECORD.created_by%TYPE := 0;
  v_field_name              varchar2(70) ;
  v_stage                   VARCHAR2(500);
  v_rows_cnt                NUMBER(10) := 0;
  v_error_cur_rec           NUMBER(10) := 0; -- Number of errors in a particular record.
  v_pc_node_id              node.node_id%TYPE; --11/14/2012 added
  v_pc_move_to_node_id      node.node_id%TYPE; --11/14/2012 added 
  v_returncode              NUMBER:= 0;        --11/14/2012 added
  v_out_user_data      SYS_REFCURSOR;
  v_auto_transfer_enabled   os_propertyset.boolean_val%TYPE;  --04/25/2019
  
  -- RECORD TYPE DECLARATION
  rec_import_record_error   import_record_error%ROWTYPE;
 
  -- EXECUTION LOG VARIABLES
  v_process_name            execution_log.process_name%type:= 'p_hierarchy_verify_load';
  v_release_level           execution_log.release_level%type := '1';
  v_node_name               node.name%TYPE;

BEGIN
  p_out_returncode := 0;
  p_total_error_rec := 0; -- Set total Number of error to zero.
--  g_total_error_rec := 0; -- Initialize Total number of error.
  
  p_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure Started for file ID '
                          ||p_import_file_id||' load_type - '||p_load_type,null);
 
  v_stage := 'To check on Budget Auto Transfer';
    BEGIN
      SELECT boolean_val                            --04/25/2019
        INTO v_auto_transfer_enabled
        FROM os_propertyset
       WHERE entity_name = 'budget.auto.transfer';
     EXCEPTION
         WHEN OTHERS THEN
         v_auto_transfer_enabled := 0;
     END;

 -- Initializing all the Global Variables
  g_hierarchy_id := p_hierarchy_id;
  g_import_file_id          := p_import_file_id;
  g_created_by              := p_user_id;
  g_modified_by             := p_user_id;
  g_file_name               := f_get_file_name(p_import_file_id);
  g_import_record_count     := f_get_import_record_count(p_import_file_id);
  
  SELECT 1 - COUNT(node_id)
    INTO g_initial_load
    FROM node
   WHERE hierarchy_id = g_hierarchy_id
        AND is_deleted = 0
     AND rownum = 1;
 
  IF g_initial_load = 0 THEN
     SELECT node_id, name
       INTO g_master_node_id, g_master_node_name
       FROM node
      WHERE hierarchy_id = g_hierarchy_id
        AND parent_node_id is NULL
        AND is_deleted = 0;
  END IF;
  
         v_stage := 'Call p_upd_action_type_stg';
        p_upd_action_type_stg(p_import_file_id,p_hierarchy_id,p_out_returncode);
          
        IF p_out_returncode = 99 THEN
           ROLLBACK;
           p_total_error_rec := g_import_record_count;
           p_execution_log_entry(v_process_name,v_release_level,'ERROR',
                                   'File: '||p_import_file_id||
                                   ' --> '||SQLERRM,null);
           RETURN;
        END IF;                      
    
  v_stage:='check node type for hierarchy'||g_hierarchy_id;
  SELECT is_node_type_req                                   --11/29/2012  get flage if node type is required or not for file node hierarchy                          
    INTO g_is_node_type_req 
    FROM hierarchy 
   WHERE hierarchy_id = g_hierarchy_id;
   
  IF  g_is_node_type_req = 0 THEN
    SELECT node_type_id 
      INTO g_node_type_id 
      FROM node_type 
     WHERE UPPER(FNC_CMS_ASSET_CODE_VAL_EXTR(cm_asset_code,name_cm_key,'en_US')) = 'DEFAULT';      --chidamba 12/26/2012
  END IF; 
 
   -- report validation errors
   v_stage := 'Call p_rpt_node_name_err';
   p_rpt_node_name_err(p_import_file_id, p_hierarchy_id, p_total_error_rec);
   
   v_stage := 'Call p_rpt_parent_node_name_err';
   p_rpt_parent_node_name_err(p_import_file_id, p_hierarchy_id, p_total_error_rec);

   v_stage := 'Call p_rpt_delete_node_err';
   p_rpt_delete_node_err(p_import_file_id, p_hierarchy_id, p_total_error_rec);

   v_stage := 'Call p_rpt_node_characteristic_err';
   p_rpt_node_characteristic_err(p_import_file_id, p_total_error_rec);
   
   v_stage := 'Call p_rpt_misc_err';
   p_rpt_misc_err(p_import_file_id,p_hierarchy_id, p_total_error_rec);
   
   COMMIT;
     v_stage := 'Verified'
      || ': p_import_file_id >'   || p_import_file_id     
      || '<, p_total_error_rec >' || p_total_error_rec
      || '<';
   prc_execution_log_entry(v_process_name, v_release_level, 'INFO', v_stage, NULL);

   -- load file when no errors reported
   IF (p_total_error_rec = 0 AND p_load_type = 'L') THEN
      v_stage := 'Import file records';
      
      IF v_auto_transfer_enabled = 1 THEN       --04/25/2019
        prc_budget_move_ins(p_import_file_id,p_out_returncode); -- 03/05/2019
      END IF;
      p_move_user_node(p_import_file_id, p_hierarchy_id); --29/12/2017 G6-3645 **
      p_imp_node(p_import_file_id, p_hierarchy_id);
      p_imp_node_characteristic(p_import_file_id, p_hierarchy_id);
      --p_move_user_node(p_import_file_id, p_hierarchy_id); --29/12/2017 commenting - [G6-3645] moving before p_imp_node call**
      --p_close_node_budget(p_import_file_id, p_hierarchy_id);  -- 03/05/2019
       -- After all the update is done check the NODE table for one and only one (OAOI) NODE with no parent
      p_check_OAOI_parent(p_out_returncode);

      -- loaded without error
      p_out_returncode := 0;
   END IF; -- load file 

   v_stage := 'Success'
      || ': p_import_file_id >'   || p_import_file_id
      || '<, p_out_returncode >'  || p_out_returncode
      || '<';
   prc_execution_log_entry(v_process_name, v_release_level, 'INFO', v_stage, NULL);

  DELETE FROM tmp_audience_user_id; --08/23/2016
    
   INSERT INTO tmp_audience_user_id  --08/23/2016
    (audience_id, user_id)
    SELECT audience_id, user_id 
      FROM participant_audience;
      
  -- Commit only if no error encountered and Load Type is 'L'
  IF ((p_total_error_rec = 0) AND (p_load_type = 'L')) THEN
     COMMIT;
     pkg_build_audience.prc_refresh_criteria_audience (NULL, v_returncode,v_out_user_data); --12/02/2013
     v_returncode := 0;
      IF v_returncode <> 0 THEN
      ROLLBACK;  --01/15/2014
      prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                            'Error in Pkg_build_audience.prc_refresh_criteria_audience while invoking for file ID '
                            ||p_import_file_id,null);
      
    ELSE         --01/15/2014
      COMMIT;    --01/15/2014
    END IF;
              IF v_auto_transfer_enabled = 1 THEN       --04/25/2019
                prc_budget_move_transfer(p_import_file_id,p_out_returncode); -- 01/22/2019 -- 03/15/2019
              END IF;
  ELSE
     ROLLBACK;
  END IF; 
  
     IF (p_load_type = 'L') THEN
            
      OPEN p_out_user_data FOR --07/07/2016
      SELECT user_id FROM
      user_node
      WHERE NVL(date_modified,date_created) BETWEEN g_timestamp and SYSDATE
      UNION
      SELECT user_id FROM   --08/23/2016
      participant_audience pa
      WHERE NOT EXISTS (SELECT 1 FROM tmp_audience_user_id tp 
                         WHERE tp.audience_id = pa.audience_id AND tp.user_id = pa.user_id)
      UNION
      SELECT user_id FROM   --08/23/2016
      tmp_audience_user_id tp  
      WHERE NOT EXISTS (SELECT 1 FROM participant_audience pa 
                         WHERE pa.audience_id = tp.audience_id AND pa.user_id = tp.user_id);
      -- loaded without error
   ELSE
     OPEN p_out_user_data FOR --07/07/2016
       SELECT * FROM DUAL WHERE 1=2;
   END IF; -- load file
 
--  --p_debug_message('ROLL/COMMIT'); --Bug # 63095
--  IF g_total_error_rec > 0 THEN
--     p_out_returncode := 1;
--     p_total_error_rec := g_total_error_rec;
--  ELSE
--     p_out_returncode := 0;
--     p_total_error_rec := g_total_error_rec;
--  END IF;

  p_execution_log_entry(v_process_name,v_release_level,'INFO','Procedure completed for file ID '
                          ||p_import_file_id,null);

EXCEPTION
  WHEN OTHERS THEN
     ROLLBACK;
     p_total_error_rec := g_import_record_count; 
     p_execution_log_entry(v_process_name,v_release_level,'ERROR',
                             'others File: '||p_import_file_id||v_stage||
                             ' --> '||SQLERRM,null);
     p_out_returncode := 99;
END; 
 
END; -- End of Package
/