CREATE OR REPLACE PACKAGE pkg_participant_verify_import  IS
/***********************************************************************************
   Purpose:  To verify or load STAGE_PAX_IMPORT_RECORD into original paricipant/user tables.
             This is a high-volume version of pkg_participant_verify_import

   Person             Date           Comments
   -----------        ----------     --------------------------------------------
   Majumder, Sandip   10/08/2007     Initial Version
   Majumder, Sandip   11/06/2007     Removed Hierarchy ID as an input parameter
                                     from p_participant_verify_load
   Arun S             04/23/2010     Added in parameter p_hierarchy_id                                            
************************************************************************************/

  PROCEDURE p_participant_verify_load(p_import_file_id  IN NUMBER,
                                      p_load_type       IN VARCHAR2,
                                      p_user_id         IN NUMBER,
                                      p_hierarchy_id    IN NUMBER,
                                      p_total_error_rec OUT NUMBER,
                                      p_out_returncode  OUT NUMBER,
                                      p_out_user_data   OUT SYS_REFCURSOR) ;
END pkg_participant_verify_import;
/
CREATE OR REPLACE PACKAGE BODY pkg_participant_verify_import
IS
/***********************************************************************************
   Purpose:  To verify or load STAGE_PAX_IMPORT_RECORD into original paricipant/user tables.
             This is a high-volume version of pkg_participant_verify_import

   Person             Date           Comments
   -----------        ----------     --------------------------------------------
   Majumder, Sandip   10/08/2007     Initial Version
   Majumder, Sandip   11/06/2007     Tuned content manager lookup queries
   Majumder, Sandip   11/06/2007     Removed Hierarchy ID as an input parameter
                                     from p_participant_verify_load
   Majumder, Sandip   11/12/2007     Added code to see if same Node Name, Role Description 
                                     or Characteristic Name is repeated for the same Pax Record. 
   Majumder, Sandip   11/13/2007     Modified the procedure p_val_pax_employer to update the 
                                     user_node status to 0 when a participant is terminated  
   Majumder, Sandip   11/19/2007     A new email address in a pax file, regardless of the type, 
                                     should be populated as the Primary. The original emaiil address should be kept.
                                     Similar functionality will be applied for User address
   Majumder, Sandip   02/14/2008     Bug # 19363: Update f_upd_validate function to change the datatype of 
                                     v_awardbanq_number VARCHAR2(100); Also as Award Banq Number is encrypted then 
                                     it should be decrypted before checking if it is NULL                                                        
                                     Content Manager look up and insertion was allowing only 30 characters for 
                                     Job Position which can be 100 characters long
   Majumder, Sandip   02/27/2008     Bug # 19582: Canadian Postal Code can have alphanumeric value. 
                                     Program is allowing only numeric value for Canadian Zip Code
   Majumder, Sandip   03/07/2008     Bug # 19620 Insert Default Audience Record for Content Manager Record   
   M Lindvig          03/25/2008     Bug # 19915 - Pax Load Node Owner Updates    
   Majumder, Sandip   04/07/2008     Bug # 20108 Insert Removed User Node information into USER_NODE_HISTORY table
   Majumder, Sandip   04/25/2008     Bug # 20345 Modified procedure p_participant_verify_load to find out any 
                                     USER_NODE record that was inactivated for this user. Move them to USER_NODE_HISTORY table and then DELETE them from USER_NODE table
   Lindvig, Matt      06/06/2008     Bug #20674  Modified the new participant insert statement to have the sysdate be the enrollment_date
   Lindvig, Matt      06/12/2008     Bug #20398 - Modified Language ID to be automatically set to english
   Nagarjuna, Reddy   07/03/2008     Bug#19539 - Modified to allow department type v_department VARCHAR2(100) to hold 100 characters length,rather than 30 chars.
   Lindvig, Matt      07/25/2008     Bug #20701 - Modified so the system can recongize the different between Canada's NL state and Mexico's.
   Lindvig, Matt      11/07/2008     BUG #21679 - Keep inactive nodes in the user_node table for reporting
   Arun S             01/16/2009     Bug #20193 - participant.terms_acceptance not popluated in participant load
   Arun S             02/06/2009     Bug #21379 - Participant Load File Won't Stage when Translations are added 
   Arun S             02/12/2009     Bug #13536 Fix,Changes to Inactivate the budget assigned to pax when pax is terminated    
   Arun S             02/17/2009     Bug #21187 Fix,Added two in parm p_department_type, p_position_type in f_get_pax_employer for validating
                                     department and position then made Update participant_employer only when match is found on participant, employer,
                                     department and position.
   Arun S             05/06/2009     Bug 21074 fix Duplicate owner error
   Arun S             06/05/2009     Bug 23076 fix modified to delete User role based on system variable
   Arun S             08/03/2009     Bug 27387 Updating employment records
   Arun S             02/17/2009     Bug 31183 fix, Removed text_message_id from Participant_comm_preference for G3 Redo,
                                     hence removed populating values to that column and in param p_text_message_id from p_val_ins_comm_pref
   Arun S             04/23/2010     Bug 18054 Fix Participant file load for multiple hierarchies
                                     i.Added new in parameter p_hierarchy_id to p_participant_verify_load
                                     ii.Added new procedure p_upd_stg_pax_import_node_id which will updade node id in satage table based
                                     on the Hierarchy selected from application
                                     iii.Updated status of user node to inactive in all hierarchy when a pax is terminated
                                     iv.Unused node removal and node status inactivation for a pax changed with respect to hierarchy
   Arun S             05/11/2010     Bug # 32555 Fix, International address changes
                                     Remove addr4,5 and 6
   Arun S             01/18/2011     Bug 35512 Fix Participant_employer record update
   Arun S             01/18/2011     Bug 35514 Fix Generate muliple primary phones
   Arun S             04/29/2011     Bug 36789 - Fixed code to add the validation of the status of picklist data.
   J Flees            11/03/2011     Refactored p_participant_verify_load from single record/field loop processing to bulk processing.
   J Flees            01/16/2012     Corrected load processing from an all or nothing import to loading all records without errors.
   J Flees            02/16/2012     Application passing in hierarchy ID only on verify, not on load.
                                     Removed hierarchy_id check from user_node delete.
                                     Hierarchy_id already validated when staged node_id populated during verify.
  Ravi Dhanekula       07/23/2013     1). Allow for removal of participant characteristic, by sending char value= ?DELETE?
                                         i,e. When value=?DELETE? the characteristic value will be removed from the participant.           
                                     2). Populate country_phone_code in user_phone                
                                     3). Globalization Changes : If pax's new country has different campaign
                                     from the current country, write record to user_country_changes table for Java processing.       
                      07/24/2013       Changed p_imp_participant_employer process, not to update the hire_date anytime.If we have                       
  Chidamba            08/03/2013     Adding IS_PRIMARY column to user_node table. and set the Node_Name1 as primary node
  Ravi Dhanekula      09/23/2013     Fixed the defect # 4415.
  Chidamba            09/24/2013     Fixed defect# 4424.rewitten merge statement to accept old primary address to replace 
                                     with new primary address, and also include general address update.  
  nagarajs            09/26/2013     Fixed the defect #4422 - multi select characteristic not showing on site after loaded
  Ravi Dhanekula      12/17/2013     Fixed the bug # 50404 - Phone numbers added earlier should be removed in case if the current file doesn't have a number.
                      01/08/2014     Fixed the bug # 50814
                      01/08/2014     Fixed the bug # 49816
                      01/09/2014     Fixed the bug # 50932
                      01/22/2014     Fixed the bug # 51131.
                      01/28/2014     Fixed the bug # 51285
                      02/17/2014     Fixed the issue with erroring out the pax record that has role as 'member' but the node is having more than one owner.
                      02/18/2014     Fixed the issue where the 'NODE_NAME1' is displayed in the error. It is changed to show the user_name.
                      02/26/2014     Fixed the Bug # 51792
                      03/26/2014     Fixed the Bug # 51995
                      03/27/2014     Fixed the Bugs # 52457,52223 AND 52052
  Swati               04/04/2014     Fixed the Bug # 52579   
  Swati               04/07/2014     Fixed the Bug # 51556
  Swati               04/10/2014     Fixed the Bug # 52515
  Swati               04/29/2014     Fixed the Bug # 52980
  Ravi Dhanekula      04/29/2014     Fixed the Bug # 53009 
  Swati               05/09/2014     Fixed the Bug # 53140  Change in pax load to log an error in import_record_error before erroring out for missing email id
  nagarajs            07/28/2014     Fixed the Bug # 55217
  nagarajs            08/07/2014     Fixed the Bug # 55521
  Suresh J            08/28/2014     Fixed the Bug #55571  Commented Country Validation Check when changing country code
  Swati                  10/24/2014     Fixed the Bug 57537 - In pax file load, node name validation will be case sensitive. 
                                     If node name in file and node name in database differs in case then pax file load mark that record as error  
  Swati                  11/20/2014     Fixed Bug 58207 - Pax File load - Allowing Multiple owners of same node
  Ravi Dhanekula      02/20/2015     Bug # 51792(59819) - Added a fix for the issue with mismatch in status for application_user and participant for inactive users.                                     
  Ramkumar            03/03/2015     Bug # 60016 - Corrected the comparison of d.sso_id and s.sso_id
  Swati               05/22/2015     Bug 61961 - Mobile phone number is deleted on pax file load
  Ravi Dhanekula      06/24/2015     Bug 62760 - Added a fix for Owner swap between Org units.
  nagarajs            08/26/2015    Bug 63803 - Fix done for below issues
                                   1. Don't allow two owners for an Org Unit 
                                  2. Allow Owner swap between Org units.
  nagarajs           10/06/2015   Bug 64217 - Made changes in p_imp_cms to create CM content for default 'en_US' locale only
  nagarajs           10/30/2015   JIRA- IBC-2 - Add 5 more characteristics to the participant file load
  nagarajs           12/10/2015   Bug 64939 - Fixed the PAX File Load Allowing to load two owners for an Org Unit issue 
  nagarajs           12/16/2015   Bug 64953 - Participant file load showed error message twice for wrong records in "Verified" stage
  nagarajs           02/03/2016  Bug 65483 - Duplicate record is getting created by default in participant_employer table while update the termination date through file load
  nagarajs          04/21/2016   Bug 65946 - Participant file load throwing NODE_ALREADY_HAS_OWNER has owner
  Sherif            06/08/2016   Bug 67002 - Participant (signed up for text messaging) cell Phone Number should not get updated by file load
  Ravi Dhanekula    07/07/2016   G6 changes - Added a ref cursor to provide list of users that have been changed during each load.
  nagarajs          08/10/2016   JIRA G6-198 -  allow hire_date field update on pax load
  nagarajs          08/23/2016   JIRA G6-120 - Criteria Based Audience updated
  Ravi Dhanekula    03/23/2017   Added new column termination_date in participant. This column will have data only when a pax is inactive (unlike termination_date in participant_employer).
  Suresh J          03/30/2017   G6-2079 - added an additional check of pax status based upon the newly added column is_opt_out_of_program 
  Ravi Dhanekula    05/05/2017   G6-2326 - termination of pax when not using employment information.
  Gorantla          08/08/2017  JIRA #G6-2796 - Problem: pax file does not allow new org unit owner to be added, when original org unit owner is termed in same pax file. 
                                Solution: pax file should allow the new org unit owner to be assigned, even if the termed record is in the same file. 
                                          It will check all of the data in the file before throwing an error, and failing to load that record.
  Chidamba          08/24/2017  G6-2870 When a participant becomes inactive, their pending purl should also expire Added new internal procedure p_inactive_pax_process. 
  Chidamba          09/27/2017  G6-2810 Part of tunning replace vw_cms_asset_value with Query.
  Chidamba          11/28/2017  G6-3424 Issue with participant loads and default language being assigned/used.
  Gorantla          01/25/2019  Bug 78846 - PAX LOAD participant_employer merge error when multiple changes
  DeepakrajS        04/12/2019  Bug 76745 - Pax swap with different roles to different nodes is casuing error
  Loganathan        07/05/2019  Bug 79083 - User_node status is updating as active(1) when the pax loaded as inactive(0) in DB.
  murphyc           10/11/2019  Bug 75587 - p_imp_participant_employer - use cm picklists for dept and position so case-sensitivity is standard
  Gorantla          11/28/2019  Git#2611  - Add new columns and modify the oracle packages to insert new UUID
************************************************************************************/

-- global package constants
c_cms_locale               CONSTANT vw_cms_code_value.locale%TYPE := 'en_US';
c_cms_status               CONSTANT vw_cms_code_value.cms_status%TYPE := 'true';
c_cms_content_status_live  CONSTANT cms_content.content_status%TYPE := 'Live';

c_action_type_add          CONSTANT stage_pax_import_record.action_type%TYPE := 'add';
c_action_type_upd          CONSTANT stage_pax_import_record.action_type%TYPE := 'upd';

c_pax_status_name_active   CONSTANT vw_cms_code_value.cms_name%TYPE := 'active';--04/29/2014 Bug # 52980--01/08/2014 Bug # 50814,51792
c_pax_status_name_inactive CONSTANT vw_cms_code_value.cms_name%TYPE := 'inactive'; --08/08/2017 - G6-2796 changing it to lower  -- Ignore these--01/08/2014  Bug # 50814,51792  

c_node_role_name_owner     CONSTANT vw_cms_code_value.cms_name%TYPE := 'owner';

c_email_type_name_sms      CONSTANT vw_cms_code_value.cms_name%TYPE := 'SMS';

c_phone_type_name_hom      CONSTANT vw_cms_code_value.cms_name%TYPE := 'Home';
c_phone_type_name_bus      CONSTANT vw_cms_code_value.cms_name%TYPE := 'Business';
c_phone_type_name_mob      CONSTANT vw_cms_code_value.cms_name%TYPE := 'Mobile';
c_phone_type_name_oth      CONSTANT vw_cms_code_value.cms_name%TYPE := 'Other';
c_phone_type_name_fax      CONSTANT vw_cms_code_value.cms_name%TYPE := 'Fax';

c_pax_comm_pref_name_estmt CONSTANT vw_cms_code_value.cms_name%TYPE := 'eStatements';

-- global package variables
g_created_by               import_record_error.created_by%TYPE := 0;
g_timestamp                import_record_error.date_created%TYPE := SYSDATE;

v_returncode              NUMBER:= 0;      --11/14/2012 added
v_default_language_id     VARCHAR2(10):= fnc_get_system_variable('default.language', 'Default Language');  --11/28/2017

---------------------
---------------------
-- private functions
---------------------
-- get the count of records with errors
FUNCTION f_get_error_record_count
( p_import_file_id  IN  NUMBER
) RETURN INTEGER
IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('f_get_error_record_count');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_error_record_count INTEGER;

   CURSOR cur_import_error_rec_count
   ( cv_import_file_id  import_record_error.import_file_id%TYPE
   ) IS
   SELECT COUNT(DISTINCT ire.import_record_id) AS error_cnt
     FROM import_record_error ire
    WHERE ire.import_file_id = cv_import_file_id
      ;

BEGIN
   -- get count of any existing errors
   v_msg := 'OPEN cur_import_error_rec_count';
   OPEN cur_import_error_rec_count(p_import_file_id);
   v_msg := 'FETCH cur_import_error_rec_count';
   FETCH cur_import_error_rec_count INTO v_error_record_count;
   CLOSE cur_import_error_rec_count;

   RETURN v_error_record_count;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END f_get_error_record_count;

---------------------
-- Bug 19620 start
-- gets the Beacon default audience ID
FUNCTION f_beacon_default_aud_id
RETURN cms_audience.id%TYPE
IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('f_beacon_default_aud_id');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_default_aud_id     cms_audience.id%TYPE;

   CURSOR cur_beacon_default_aud_id IS
   SELECT au.id
     FROM cms_application ca,
          cms_audience au
    WHERE UPPER(ca.code) = 'BEACON' 
      AND ca.id = au.application_id
      AND UPPER(au.code) = 'DEFAULT'
      ;

BEGIN
   v_msg := 'OPEN cur_beacon_default_aud_id';
   OPEN cur_beacon_default_aud_id;
   v_msg := 'FETCH cur_beacon_default_aud_id';
   FETCH cur_beacon_default_aud_id INTO v_default_aud_id;
   CLOSE cur_beacon_default_aud_id;

   RETURN v_default_aud_id;
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END f_beacon_default_aud_id;
-- Bug 19620 end

---------------------
---------------------
-- private procecures
---------------------
-- Populates the node ID based upon the staged node name
PROCEDURE p_set_stg_pax_import_node_id
( p_import_file_id  IN  stage_pax_import_record.import_file_id%TYPE,
  p_hierarchy_id    IN  NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_set_stg_pax_import_node_id');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- set missing node IDs
   v_msg := 'MERGE stage_pax_import_record'
      || ': p_import_file_id >' || p_import_file_id
      || '<, p_hierarchy_id >'  || p_hierarchy_id
      || '<';

   MERGE INTO stage_pax_import_record d
   USING (  -- get node ID based on node name
            SELECT s.ROWID AS s_rowid,
                   n1.node_id AS node_id1,
                   n2.node_id AS node_id2,
                   n3.node_id AS node_id3,
                   n4.node_id AS node_id4,
                   n5.node_id AS node_id5,
                   s.node_name1,
                   s.node_name2,
                   s.node_name3,
                   s.node_name4,
                   s.node_name5
              FROM stage_pax_import_record s,
                   node n1,
                   node n2,
                   node n3,
                   node n4,
                   node n5
             WHERE s.import_file_id = p_import_file_id
                -- restrict to records with missing node ID
               AND (  (s.node_id1 IS NULL AND s.node_name1 IS NOT NULL)
                   OR (s.node_id2 IS NULL AND s.node_name2 IS NOT NULL)
                   OR (s.node_id3 IS NULL AND s.node_name3 IS NOT NULL)
                   OR (s.node_id4 IS NULL AND s.node_name4 IS NOT NULL)
                   OR (s.node_id5 IS NULL AND s.node_name5 IS NOT NULL)
                   )
                -- outer join on node name in case staged node name invalid
               AND lower(s.node_name1)   = lower(n1.name (+)) --10/24/2014 Bug 57537
               AND p_hierarchy_id = n1.hierarchy_id (+)
               AND lower(s.node_name2)   = lower(n2.name (+)) --10/24/2014 Bug 57537
               AND p_hierarchy_id = n2.hierarchy_id (+)
               AND lower(s.node_name3)   = lower(n3.name (+)) --10/24/2014 Bug 57537
               AND p_hierarchy_id = n3.hierarchy_id (+)
               AND lower(s.node_name4)   = lower(n4.name (+)) --10/24/2014 Bug 57537
               AND p_hierarchy_id = n4.hierarchy_id (+)
               AND lower(s.node_name5)   = lower(n5.name (+)) --10/24/2014 Bug 57537
               AND p_hierarchy_id = n5.hierarchy_id (+)
         ) s
      ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
      UPDATE SET
         d.node_id1 = s.node_id1,
         d.node_id2 = s.node_id2,
         d.node_id3 = s.node_id3,
         d.node_id4 = s.node_id4,
         d.node_id5 = s.node_id5
      ;

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_set_stg_pax_import_node_id;


-- Reports duplicate user errors
PROCEDURE p_rpt_dup_user_err
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_dup_user_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- duplicate user validation
   v_msg := 'INSERT import_record_error';
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
             e.param1,
             NULL AS param2,
             NULL AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_user AS
               (  -- get user data from stage records
                  SELECT s.import_file_id,
                         s.import_record_id,
                         s.action_type,
                         s.user_name,
                         DECODE( s.ssn,
                           NULL, NULL,
                           fnc_java_encrypt(s.ssn)
                         ) AS ssn,
                         COUNT(s.user_name) OVER (PARTITION BY s.action_type, s.user_name) AS user_name_cnt,
                         COUNT(s.ssn)       OVER (PARTITION BY s.action_type, s.ssn)       AS ssn_cnt,
                         s.sso_id,
                         s.user_id
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              --AND e.date_created < g_timestamp --04/10/2014
                         )
               )
               -- duplicate user name add in import file
               SELECT su.import_file_id,
                      su.import_record_id,
                      'participant.errors.DUPLICATE_USER_NAME' AS item_key,
                      su.user_name,
                      su.user_name AS param1
                 FROM stg_user su
                WHERE su.action_type = c_action_type_add
                  AND su.user_name_cnt > 1
                UNION ALL
               -- user name already assigned to an existing user
               SELECT su.import_file_id,
                      su.import_record_id,
                      'participant.errors.DUPLICATE_USER_NAME' AS item_key,
                      su.user_name,
                      au.user_name AS param1
                 FROM stg_user su,
                      application_user au
                WHERE su.action_type = c_action_type_add
                  AND su.user_name = au.user_name
                UNION ALL
               -- duplicate SSN add in import file
               SELECT su.import_file_id,
                      su.import_record_id,
                      'participant.errors.DUPLICATE_SSN' AS item_key,
                      su.user_name,
                      'other users' AS param1
                 FROM stg_user su
                WHERE su.action_type = c_action_type_add
                  AND su.ssn_cnt > 1
                UNION ALL
               -- SSN already assigned to an existing user
               SELECT su.import_file_id,
                      su.import_record_id,
                      'participant.errors.DUPLICATE_SSN' AS item_key,
                      su.user_name,
                      au.user_name AS param1
                 FROM stg_user su,
                      application_user au
                WHERE su.action_type = c_action_type_add
                  AND su.ssn IS NOT NULL
                  AND su.ssn = au.ssn
                UNION ALL
               -- updating SSN already assigned to another user
               SELECT su.import_file_id,
                      su.import_record_id,
                      'participant.errors.DUPLICATE_SSN' AS item_key,
                      su.user_name,
                      au.user_name AS param1
                 FROM stg_user su,
                      application_user au
                WHERE su.action_type = c_action_type_upd
                  AND su.user_name != au.user_name
                  AND su.ssn IS NOT NULL
                  AND su.ssn = au.ssn
                   UNION ALL
               -- updating SSO ID already assigned to another user
               SELECT su.import_file_id,
                      su.import_record_id,
                      'participant.errors.DUPLICATE_SSO_ID' AS item_key,
                      su.sso_id,
                      p.sso_id AS param1
                 FROM stg_user su,
                      participant p
                WHERE su.action_type = c_action_type_upd
                  AND su.user_id != p.user_id
                  AND su.sso_id is not null
                  AND su.sso_id = p.sso_id
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_dup_user_err;

-- Reports invalid property errors
PROCEDURE p_rpt_invalid_property_err
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_invalid_property_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';
   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- file load property validation
   v_msg := 'INSERT import_record_error';
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
             'admin.fileload.errors.INVALID_PROPERTY' AS item_key,
             e.field_name AS param1,
             e.field_value AS param2,
             NULL AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- find stage records with node name but no associated node ID
                  SELECT s.*
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              --AND e.date_created < g_timestamp --04/10/2014
                         )
               )
               -- active value must be 0 or 1
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'ACTIVE' AS field_name,
                      TO_CHAR(sr.active) AS field_value
                 FROM stg_rec sr
                WHERE sr.active IS NOT NULL
                  AND sr.active != 0
                  AND sr.active != 1
                UNION ALL
               -- node ID required when node name present
               SELECT pv.import_file_id,
                      pv.import_record_id,
                      pv.field_name AS field_name,
                      pv.node_name  AS field_value
                 FROM ( -- pivot node columns into rows
                        SELECT sr.import_file_id,
                               sr.import_record_id,
                               'Org Unit Name' || p.column_value AS field_name,
                               DECODE(p.column_value,
                                 1, sr.node_id1,
                                 2, sr.node_id2,
                                 3, sr.node_id3,
                                 4, sr.node_id4,
                                 5, sr.node_id5
                               ) AS node_id,
                               DECODE(p.column_value,
                                 1, sr.node_name1,
                                 2, sr.node_name2,
                                 3, sr.node_name3,
                                 4, sr.node_name4,
                                 5, sr.node_name5
                               ) AS node_name,
                               DECODE(p.column_value,
                                 1, sr.node_relationship1,
                                 2, sr.node_relationship2,
                                 3, sr.node_relationship3,
                                 4, sr.node_relationship4,
                                 5, sr.node_relationship5
                               ) AS node_relationship
                          FROM stg_rec sr,
                               ( -- select a row for each node column
                                  SELECT LEVEL AS column_value
                                    FROM dual
                                 CONNECT BY LEVEL <= 5
                               ) p
                      ) pv
                WHERE pv.node_id IS NULL
                  AND pv.node_name IS NOT NULL
                UNION ALL
               -- user ID required when updating
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'USER NAME'  AS field_name,
                      sr.user_name AS field_value
                 FROM stg_rec sr
                WHERE sr.action_type = c_action_type_upd
                  AND sr.user_id IS NULL
                UNION ALL
               -- SSN must be 9 characters
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'SSN'  AS field_name,
                      sr.ssn AS field_value
                 FROM stg_rec sr
                WHERE sr.ssn IS NOT NULL
                  AND LENGTH(sr.ssn) != 9
                UNION ALL
               -- country ID required when country code present
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'COUNTRY CODE'  AS field_name,
                      sr.country_code AS field_value
                 FROM stg_rec sr
                WHERE sr.country_code IS NOT NULL
                  AND sr.country_id IS NULL
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_invalid_property_err;

-- Reports missing required fields
PROCEDURE p_rpt_required_field_err
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_required_field_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
   v_sso_id_flag           os_propertyset.string_val%TYPE;
   
BEGIN

      SELECT string_val
      INTO v_sso_id_flag 
      FROM os_propertyset 
     WHERE lower(ENTITY_NAME) = 'sso.unique.id';
   -- required field validation
   v_msg := 'INSERT import_record_error';
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
             'system.errors.REQUIRED' AS item_key,
             e.field_name AS param1,
             NULL AS param2,
             NULL AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              --AND e.date_created < g_timestamp --04/10/2014
                         )
               )
               -- user name required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'USER_NAME' AS field_name
                 FROM stg_rec sr
                WHERE sr.user_name IS NULL
                UNION ALL
               -- first name required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'FIRST_NAME' AS field_name
                 FROM stg_rec sr
                WHERE sr.first_name IS NULL
                UNION ALL
               -- last name required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'LAST_NAME' AS field_name
                 FROM stg_rec sr
                WHERE sr.last_name IS NULL
                UNION ALL
               -- active required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'ACTIVE' AS field_name
                 FROM stg_rec sr
                WHERE sr.active IS NULL
                UNION ALL
               -- address 1 required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'ADDRESS_1' AS field_name
                 FROM stg_rec sr
                WHERE sr.address_1 IS NULL
                UNION ALL
               -- address type required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'ADDRESS_TYPE' AS field_name
                 FROM stg_rec sr
                WHERE sr.address_type IS NULL
                UNION ALL
               -- country code required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'COUNTRY_CODE' AS field_name
                 FROM stg_rec sr
                WHERE sr.country_code IS NULL
                UNION ALL
               -- city code required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'CITY' AS field_name
                 FROM stg_rec sr
                WHERE sr.city IS NULL
                UNION ALL
               -- postal code required dependent on country
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'POSTAL_CODE' AS field_name
                 FROM stg_rec sr,
                      country c
                WHERE sr.postal_code IS NULL
                  AND LOWER(sr.country_code) = c.country_code
                  AND c.require_postalcode = 1
--                UNION ALL
-- email address required -- Added for 05/09/2014 For Bug # 53140 --Email address is not mandatory Bug # 57479
--               SELECT sr.import_file_id,
--                      sr.import_record_id,
--                      'Email_ID' AS field_name
--                 FROM stg_rec sr
--                WHERE sr.email_address IS NULL    
                UNION ALL
               -- state code required when CMS state values exist for country
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'STATE' AS field_name
                 FROM stg_rec sr,
                      ( -- get list of country codes that have CMS state pick lists
--                        SELECT DISTINCT
--                               SUBSTR(ccv.cms_code, 1, INSTR(ccv.cms_code,'_')-1) AS country_code
--                          FROM vw_cms_code_value ccv
--                         WHERE ccv.asset_code = 'picklist.statetype.items'
--                           AND ccv.locale     = c_cms_locale
--                           AND ccv.cms_status = c_cms_status
                         SELECT DISTINCT  --Changed the code for performance improvement as the view vw_cms_asset_value ismuch faster than vw_cms_code_value
                              SUBSTR(ccv.cms_value, 1, INSTR(ccv.cms_value,'_')-1) AS country_code
                          FROM vw_cms_asset_value ccv
                         WHERE ccv.asset_code = 'picklist.statetype.items'
                           AND ccv.locale     = c_cms_locale
                          AND ccv.key = 'CODE'
                      ) cc
                WHERE sr.country_code IS NOT NULL
                  AND LOWER(sr.country_code) = cc.country_code
                  AND LOWER(sr.country_code) IN ('us','ca','mx') --defect # 48394
                  AND sr.state IS NULL
                UNION ALL
               -- job position or department required when employer name present
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'JOB_POSITION or DEPARTMENT with EMPLOYER_NAME' AS field_name
                 FROM stg_rec sr
                WHERE sr.employer_name IS NOT NULL
                  AND sr.job_position IS NULL
                  AND sr.department IS NULL
                   UNION ALL
               -- job position or department required when employer name present
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'SSO ID' AS field_name
                 FROM stg_rec sr
                WHERE sr.sso_id IS NULL
                  AND UPPER(v_sso_id_flag)='SSO ID'
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_required_field_err;

-- Reports postal code errors
PROCEDURE p_rpt_postal_code_err
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_postal_code_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- postal code validation
   v_msg := 'INSERT import_record_error';
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
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.import_file_id,
                         s.import_record_id,
                         s.country_code,
                         s.postal_code,
                         -- remov any dashes and spaces from field before checking validation
                         UPPER(REPLACE(REPLACE(s.postal_code, '-', NULL),' ',NULL)) AS chk_postal_code --Defect # 3927
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              --AND e.date_created < g_timestamp --04/10/2014
                         )
               )
               -- US postal codes must be numeric
               SELECT DISTINCT
                      pc.import_file_id,
                      pc.import_record_id,
                      'participant.address.errors.US_POSTAL_CODE_INVALID' AS item_key,
                      'POSTAL_CODE' AS field_name,
                      pc.postal_code AS field_value
                 FROM ( -- get US postal codes for validations
                        SELECT sr.*
                          FROM stg_rec sr
                         WHERE LOWER(sr.country_code) = 'us'
                      ) pc,
                      -- parse postal code into individual ascii values
                      -- pivoting the ascii values into separate records
                      TABLE( CAST( MULTISET(
                         SELECT ASCII(SUBSTR(pc.chk_postal_code, LEVEL, 1))
                           FROM dual
                        CONNECT BY LEVEL <= LENGTH(pc.chk_postal_code)
                      ) AS sys.odcinumberlist ) ) p
                   -- find non-numeric characters
                WHERE NOT (p.column_value BETWEEN 48 AND 57)
                UNION ALL
               -- US postal codes must be at least 5 characters long
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'participant.address.errors.US_POSTAL_CODE_INVALID_LENGTH' AS item_key,
                      'POSTAL_CODE' AS field_name,
                      sr.postal_code AS field_value
                 FROM stg_rec sr
                WHERE LOWER(sr.country_code) = 'us'
                  AND LENGTH(sr.chk_postal_code) < 5
                UNION ALL
               -- Canadian postal codes must be alphanumeric
               SELECT DISTINCT
                      pc.import_file_id,
                      pc.import_record_id,
                      'participant.address.errors.CA_POSTAL_CODE_INVALID' AS item_key,
                      'POSTAL_CODE' AS field_name,
                      pc.postal_code AS field_value
                 FROM ( -- get Canadian postal codes for validations
                        SELECT sr.*
                          FROM stg_rec sr
                         WHERE LOWER(sr.country_code) = 'ca'
                      ) pc,
                      -- parse postal code into individual ascii values
                      -- pivoting the ascii values into separate records
                      TABLE( CAST( MULTISET(
                         SELECT ASCII(SUBSTR(pc.chk_postal_code, LEVEL, 1))
                           FROM dual
                        CONNECT BY LEVEL <= LENGTH(pc.chk_postal_code)
                      ) AS sys.odcinumberlist ) ) p
                   -- find non-alphanumeric characters
                WHERE NOT (   p.column_value BETWEEN 48 AND 57 -- numeric
                          OR  p.column_value BETWEEN 65 AND 90 -- alpha (upper case)
                          )
                UNION ALL
               -- Canadian postal codes must be at least 6 characters long
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'participant.address.errors.CA_POSTAL_CODE_INVALID_LENGTH' AS item_key,
                      'POSTAL_CODE' AS field_name,
                      sr.postal_code AS field_value
                 FROM stg_rec sr
                WHERE LOWER(sr.country_code) = 'ca'
                  AND LENGTH(sr.chk_postal_code) < 6
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_postal_code_err;

-- Reports CMS validation errors
PROCEDURE p_rpt_cms_err
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_cms_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- CMS validation
   v_msg := 'INSERT import_record_error';
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
             'admin.fileload.errors.INVALID_PROPERTY' AS item_key,
             e.field_name AS param1,
             e.field_value AS param2,
             NULL AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM ( 
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              --AND e.date_created < g_timestamp --04/10/2014
                         )
               )
               SELECT ccv.content_id,
                      sv.*
                 FROM (SELECT asset_code, cms_value, content_id, key, locale
                         FROM vw_cms_asset_value
                        WHERE locale = c_cms_locale
                          AND key = 'CODE'
                          AND (asset_code IN ('picklist.hierarchyrole.type.items',
                                              'picklist.addresstype.items', 
                                              'picklist.participantstatus.items',
                                              'picklist.gendertype.items')
                           OR (asset_code = 'picklist.statetype.items' AND SUBSTR(cms_value,1,2) IN ('us', 'ca', 'mx')))) ccv ,--changed the view from vw_cms_code_value to vw_cms_asset_value  --09/27/2017 G6-2810 Part of tunning Replace vw_cms_asset_value with query
                      ( -- get stage validation fields
                        SELECT sr.import_file_id,
                               sr.import_record_id,
                               'picklist.gendertype.items' AS asset_code,
                               sr.gender AS cms_code,
                               'GENDER' AS field_name,
                               sr.gender AS field_value
                          FROM stg_rec sr
                         WHERE sr.gender IS NOT NULL
                         UNION ALL
                        SELECT sr.import_file_id,
                               sr.import_record_id,
                               'picklist.participantstatus.items' AS asset_code,
                               sr.status AS cms_code,
                               'STATUS' AS field_name,
                               sr.status AS field_value
                          FROM stg_rec sr
                         WHERE sr.status IS NOT NULL
                         UNION ALL
                        SELECT sr.import_file_id,
                               sr.import_record_id,
                               'picklist.addresstype.items' AS asset_code,
                               sr.address_type AS cms_code,
                               'ADDRESS TYPE' AS field_name,
                               sr.address_type AS field_value
                          FROM stg_rec sr
                         WHERE sr.address_type IS NOT NULL
                         UNION ALL
                        SELECT sr.import_file_id,
                               sr.import_record_id,
                               'picklist.statetype.items' AS asset_code,
                        -- format state code 
                     DECODE(sr.state,NULL,sr.state,LOWER(sr.country_code || '_' || sr.state))  AS cms_code,
                               'STATE' AS field_name,
                               sr.state AS field_value
                          FROM stg_rec sr
                         WHERE sr.state IS NOT NULL
                           AND LOWER(sr.country_code) IN ('us', 'ca', 'mx')
                         UNION ALL
                        SELECT sr.import_file_id,
                               sr.import_record_id,
                               'picklist.hierarchyrole.type.items' AS asset_code,
                               DECODE(p.column_value,
                                 1, sr.node_relationship1,
                                 2, sr.node_relationship2,
                                 3, sr.node_relationship3,
                                 4, sr.node_relationship4,
                                 5, sr.node_relationship5
                               ) AS cms_code,
                               'NODE_RELATIONSHIP' || p.column_value AS field_name,
                               DECODE(p.column_value,
                                 1, sr.node_relationship1,
                                 2, sr.node_relationship2,
                                 3, sr.node_relationship3,
                                 4, sr.node_relationship4,
                                 5, sr.node_relationship5
                               ) AS field_value
                           -- pivot node relationship columns into rows
                          FROM stg_rec sr,
                               ( -- select a row for each node column
                                  SELECT LEVEL AS column_value
                                    FROM dual
                                 CONNECT BY LEVEL <= 5
                               ) p
                         WHERE DECODE(p.column_value,
                                 1, sr.node_relationship1,
                                 2, sr.node_relationship2,
                                 3, sr.node_relationship3,
                                 4, sr.node_relationship4,
                                 5, sr.node_relationship5
                               ) IS NOT NULL
                      ) sv
                   -- outer join import fields to CMS table
                WHERE ccv.asset_code (+) = sv.asset_code
                  AND ccv.locale     (+) = c_cms_locale
                  AND ccv.key (+) = 'CODE'
                  AND LOWER(ccv.cms_value (+)) = LOWER(sv.cms_code)
             ) e
          -- import field value does not have a matching CMS value
       WHERE e.content_id IS NULL
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_cms_err;

-- Reports country update errors
PROCEDURE p_rpt_country_upd_err
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_country_upd_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- the participant award banq number is tied to the campaign number
   -- cannot switch the address country when user has an AwardBanq number and the country campaign numbers differ
   v_msg := 'INSERT import_record_error';
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
             s.import_file_id,
             s.import_record_id,
             'participant.errors.INVAL_COUNTRY_UPDATE' AS item_key,
             s.user_id AS param1,
             c_ua.country_code AS param2,
             c_s.country_code AS param3,
             NULL AS param4,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM stage_pax_import_record s,
             participant pax,
             user_address ua,
             country c_s,
             country c_ua
       WHERE s.import_file_id = p_import_file_id
          -- skip records previously marked as erred
         AND s.import_record_id NOT IN
             ( SELECT e.import_record_id
                 FROM import_record_error e
                WHERE e.import_file_id = p_import_file_id
                  --AND e.date_created < g_timestamp --04/10/2014
             )
          -- updating an existing user
         AND s.action_type = 'upd'
         AND s.user_id IS NOT NULL
         AND s.country_id IS NOT NULL
         ANd s.country_id = c_s.country_id
          -- PAX user has an award banq number
         AND s.user_id = pax.user_id
         AND pax.awardbanq_nbr IS NOT NULL
         AND fnc_java_decrypt(pax.awardbanq_nbr) IS NOT NULL
          -- user has an existing address type
         AND s.user_id = ua.user_id
         AND LOWER(s.address_type) = ua.address_type
         AND ua.country_id = c_ua.country_id
          -- changing address country and campaign numbers differ
         AND c_s.country_id != c_ua.country_id
         AND DECODE( c_s.campaign_nbr, c_ua.campaign_nbr, 1, 0) = 0 -- decode handles NULL values
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_country_upd_err;

-- Reports user characteristic errors
PROCEDURE p_rpt_user_characteristic_err
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_user_characteristic_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- validate user characteristic field requirements
   v_msg := 'INSERT import_record_error (field requirements)';
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
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              --AND e.date_created < g_timestamp --04/10/2014
                         )
               ), stg_rec_pivot AS
               (  -- pivot the characteristic IDs columns with its associated import file/record ID
                  SELECT sr.import_file_id,
                         sr.import_record_id,
                         p.column_value AS field_position,
                         DECODE(p.column_value,
                           1, sr.characteristic_id1,
                           2, sr.characteristic_id2,
                           3, sr.characteristic_id3,
                           4, sr.characteristic_id4,
                           5, sr.characteristic_id5,
                           6, sr.characteristic_id6,
                           7, sr.characteristic_id7,
                           8, sr.characteristic_id8,
                           9, sr.characteristic_id9,
                           10, sr.characteristic_id10,
                           11, sr.characteristic_id11,
                           12, sr.characteristic_id12,
                           13, sr.characteristic_id13,
                           14, sr.characteristic_id14,
                           15, sr.characteristic_id15,
                           16, sr.characteristic_id16,  --10/30/2015 Start
                           17, sr.characteristic_id17,
                           18, sr.characteristic_id18,
                           19, sr.characteristic_id19,
                           20, sr.characteristic_id20   --10/30/2015 End
                         ) AS characteristic_id,
                         DECODE(p.column_value,
                           1, sr.characteristic_name1,
                           2, sr.characteristic_name2,
                           3, sr.characteristic_name3,
                           4, sr.characteristic_name4,
                           5, sr.characteristic_name5,
                           6, sr.characteristic_name6,
                           7, sr.characteristic_name7,
                           8, sr.characteristic_name8,
                           9, sr.characteristic_name9,
                           10, sr.characteristic_name10,
                           11, sr.characteristic_name11,
                           12, sr.characteristic_name12,
                           13, sr.characteristic_name13,
                           14, sr.characteristic_name14,
                           15, sr.characteristic_name15,
                           16, sr.characteristic_name16,    --10/30/2015 Start
                           17, sr.characteristic_name17,
                           18, sr.characteristic_name18,
                           19, sr.characteristic_name19,
                           20, sr.characteristic_name20     --10/30/2015 End
                         ) AS characteristic_name,
                         DECODE(p.column_value,
                           1, sr.characteristic_value1,
                           2, sr.characteristic_value2,
                           3, sr.characteristic_value3,
                           4, sr.characteristic_value4,
                           5, sr.characteristic_value5,
                           6, sr.characteristic_value6,
                           7, sr.characteristic_value7,
                           8, sr.characteristic_value8,
                           9, sr.characteristic_value9,
                           10, sr.characteristic_value10,
                           11, sr.characteristic_value11,
                           12, sr.characteristic_value12,
                           13, sr.characteristic_value13,
                           14, sr.characteristic_value14,
                           15, sr.characteristic_value15,   --10/30/2015 Start
                           16, sr.characteristic_value16,
                           17, sr.characteristic_value17,
                           18, sr.characteristic_value18,
                           19, sr.characteristic_value19,
                           20, sr.characteristic_value20    --10/30/2015 End
                         ) AS characteristic_value
                    FROM stg_rec sr,
                         ( -- select a row for each characteristic column
                            SELECT LEVEL AS column_value
                              FROM dual
                           CONNECT BY LEVEL <= 20 --15 --10/30/2015
                         ) p
               )
               -- check whether any characteristic_id is repeated within the record
               SELECT DISTINCT
                      cc.import_file_id,
                      cc.import_record_id,
                      'system.errors.REPEATED' AS item_key,
                      'Characteristic Name' AS field_name,
                      cc.characteristic_name AS field_value
                 FROM ( -- get characteristic ID counts across the record columns
                        SELECT srp.import_file_id,
                               srp.import_record_id,
                               srp.characteristic_id,
                               srp.characteristic_name,
                               COUNT(srp.characteristic_id)
                                 OVER (PARTITION BY srp.import_file_id, srp.import_record_id, srp.characteristic_id)
                                 AS characteristic_cnt
                          FROM stg_rec_pivot srp
                      ) cc
                WHERE cc.characteristic_cnt > 1
                UNION ALL
               -- ensure record contains all required user characteristics
               SELECT DISTINCT
                      sc.import_file_id,
                      sc.import_record_id,
                      'system.errors.MISSING_REQUIRED_CHARACTERISTICS' AS item_key,
                      NULL AS field_name,
                      NULL AS field_value
                 FROM ( -- get count of required user characteristics
                        SELECT COUNT(c.characteristic_id) AS req_cnt
                          FROM characteristic c
                         WHERE c.characteristic_type = 'USER'
                           AND c.is_required = 1
                           AND c.is_active   = 1
                      ) rc,
                      ( -- get count of required user characteristics in stage record
                        SELECT srp.import_file_id,
                               srp.import_record_id,
                               COUNT(DISTINCT c.characteristic_id) AS stg_req_cnt
                          FROM stg_rec_pivot srp,
                               characteristic c
                            -- outer join since no characteristic may be required
                         WHERE c.characteristic_type (+) = 'USER'
                           AND c.is_required (+) = 1
                           AND c.is_active   (+) = 1
                           AND c.characteristic_id (+) = srp.characteristic_id
                         GROUP BY srp.import_file_id,
                               srp.import_record_id
                      ) sc
                WHERE sc.stg_req_cnt < rc.req_cnt
                UNION ALL
               -- all characteristic fields required when any characteristic field contains a value
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'system.errors.DEPENDENCY_REQUIRED' AS item_key,
                      'Characteristic Name '  || srp.field_position AS field_name,
                      'Characteristic Value ' || srp.field_position AS field_value
                 FROM stg_rec_pivot srp
                WHERE (  srp.characteristic_id    IS NOT NULL
                      OR srp.characteristic_name  IS NOT NULL
                      OR srp.characteristic_value IS NOT NULL
                      )
                  AND (  srp.characteristic_id    IS NULL
                      OR srp.characteristic_name  IS NULL
                      OR srp.characteristic_value IS NULL
                      )
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

   -- validate user characteristic field values based on characteristic data type
   v_msg := 'INSERT import_record_error (field values)';
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
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                             -- AND e.date_created < g_timestamp --04/10/2014
                         )
               ), stg_rec_pivot AS
               (  -- get associated characteristic data
                  SELECT pv.*,
                         -- indicate whether value is the specified type
                         DECODE( LOWER(c.characteristic_data_type),
                           'int', fnc_is_number(pv.characteristic_value, 1),
                           'dec', fnc_is_number(pv.characteristic_value),
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
                           SELECT sr.import_file_id,
                                  sr.import_record_id,
                                  p.column_value AS field_position,
                                  DECODE(p.column_value,
                                    1, sr.characteristic_id1,
                                    2, sr.characteristic_id2,
                                    3, sr.characteristic_id3,
                                    4, sr.characteristic_id4,
                                    5, sr.characteristic_id5,
                                    6, sr.characteristic_id6,
                                    7, sr.characteristic_id7,
                                    8, sr.characteristic_id8,
                                    9, sr.characteristic_id9,
                                    10, sr.characteristic_id10,
                                    11, sr.characteristic_id11,
                                    12, sr.characteristic_id12,
                                    13, sr.characteristic_id13,
                                    14, sr.characteristic_id14,
                                    15, sr.characteristic_id15,
                                    16, sr.characteristic_id16, --10/30/2015 Start
                                    17, sr.characteristic_id17,
                                    18, sr.characteristic_id18,
                                    19, sr.characteristic_id19,
                                    20, sr.characteristic_id20  --10/30/2015 End
                                  ) AS characteristic_id,
                                  DECODE(p.column_value,
                                    1, sr.characteristic_name1,
                                    2, sr.characteristic_name2,
                                    3, sr.characteristic_name3,
                                    4, sr.characteristic_name4,
                                    5, sr.characteristic_name5,
                                    6, sr.characteristic_name6,
                                    7, sr.characteristic_name7,
                                    8, sr.characteristic_name8,
                                    9, sr.characteristic_name9,
                                    10, sr.characteristic_name10,
                                    11, sr.characteristic_name11,
                                    12, sr.characteristic_name12,
                                    13, sr.characteristic_name13,
                                    14, sr.characteristic_name14,
                                    15, sr.characteristic_name15,
                                    16, sr.characteristic_name16,    --10/30/2015 Start
                                    17, sr.characteristic_name17,
                                    18, sr.characteristic_name18,
                                    19, sr.characteristic_name19,
                                    20, sr.characteristic_name20     --10/30/2015 End
                                  ) AS characteristic_name,
                                  DECODE(p.column_value,
                                    1, sr.characteristic_value1,
                                    2, sr.characteristic_value2,
                                    3, sr.characteristic_value3,
                                    4, sr.characteristic_value4,
                                    5, sr.characteristic_value5,
                                    6, sr.characteristic_value6,
                                    7, sr.characteristic_value7,
                                    8, sr.characteristic_value8,
                                    9, sr.characteristic_value9,
                                    10, sr.characteristic_value10,
                                    11, sr.characteristic_value11,
                                    12, sr.characteristic_value12,
                                    13, sr.characteristic_value13,
                                    14, sr.characteristic_value14,
                                    15, sr.characteristic_value15,
                                    16, sr.characteristic_value16,  --10/30/2015 Start
                                    17, sr.characteristic_value17,
                                    18, sr.characteristic_value18,
                                    19, sr.characteristic_value19,
                                    20, sr.characteristic_value20   --10/30/2015 End
                                  ) AS characteristic_value
                             FROM stg_rec sr,
                                  ( -- select a row for each characteristic column
                                     SELECT LEVEL AS column_value
                                       FROM dual
                                    CONNECT BY LEVEL <= 20 --15 --10/30/2015
                                  ) p
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
                      'Value for Characteristic ' || srp.characteristic_name AS field_name,
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
                      'Value for Characteristic ' || srp.characteristic_name AS field_name,
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
                      'Value for Characteristic ' || srp.characteristic_name AS field_name,
                      srp.characteristic_value AS field_value1,
                      NULL AS field_value2
                 FROM stg_rec_pivot srp
                WHERE srp.characteristic_data_type = 'dec'
                  AND srp.is_type = 0
                UNION ALL
               -- validate integer/decimal characteristic within range
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'user.characteristic.errors.RANGE' AS item_key,
                      'Value for Characteristic ' || srp.characteristic_name AS field_name,
                      TO_CHAR(srp.min_value) AS field_value1,
                      TO_CHAR(srp.max_value) AS field_value2
                 FROM stg_rec_pivot srp
                WHERE srp.characteristic_data_type IN ('int','dec')
                  AND srp.is_type = 1
                   -- find values outside of characteristic range
                  AND NOT (TO_NUMBER(srp.characteristic_value) BETWEEN srp.min_value AND srp.max_value)
                UNION ALL
               -- validate text characteristic length
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'user.characteristic.errors.SIZE' AS item_key,
                      'Value for Characteristic ' || srp.characteristic_name AS field_name,
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
                      'Value for Characteristic ' || srp.characteristic_name AS field_name,
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
                      'Value for Characteristic ' || srp.characteristic_name AS field_name,
                      TO_CHAR(srp.date_start, 'MM/DD/YYYY') AS field_value1,
                      TO_CHAR(srp.date_end,   'MM/DD/YYYY') AS field_value2
                 FROM stg_rec_pivot srp
                WHERE srp.characteristic_data_type = 'date'
                  AND srp.is_type = 1
                   -- find values outside of characteristic range
                  AND NOT (TO_DATE(srp.characteristic_value, 'MM/DD/YYYY') BETWEEN srp.date_start AND srp.date_end)
                UNION ALL
               -- validate single characteristic in pick list
               SELECT cms.import_file_id,
                      cms.import_record_id,
                      'admin.fileload.errors.INVALID_PROPERTY' AS item_key,
                      'CHARACTERISTIC VALUE' AS field_name,
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
                      'CHARACTERISTIC VALUE' AS field_name,
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
                               vw_cms_asset_value ccv
                            -- outer join characteristic fields to CMS table
                         WHERE ccv.asset_code (+) = pl.pl_name
                           AND ccv.locale     (+) = c_cms_locale
                           AND ccv.key (+) = 'CODE'
                           AND LOWER(ccv.cms_value (+)) = LOWER(pl.picklist_item)
                      ) cms
                   -- find values not in the pick list
                WHERE cms.content_id IS NULL
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_user_characteristic_err;

-- Reports node errors
PROCEDURE p_rpt_node_err
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_node_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- validate node field requirements
   v_msg := 'INSERT import_record_error (field requirements)';
  /* INSERT INTO import_record_error -- 04/10/2014 commented as a part of Bug # 52515
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
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              AND e.date_created < g_timestamp
                         )
               ), stg_rec_pivot AS
               (  -- pivot the node IDs columns with its associated import file/record ID
                  SELECT sr.import_file_id,
                         sr.import_record_id,
                         p.column_value AS field_position,
                         DECODE(p.column_value,
                           1, sr.node_id1,
                           2, sr.node_id2,
                           3, sr.node_id3,
                           4, sr.node_id4,
                           5, sr.node_id5
                         ) AS node_id,
                         DECODE(p.column_value,
                           1, sr.node_name1,
                           2, sr.node_name2,
                           3, sr.node_name3,
                           4, sr.node_name4,
                           5, sr.node_name5
                         ) AS node_name,
                         DECODE(p.column_value,
                           1, sr.node_relationship1,
                           2, sr.node_relationship2,
                           3, sr.node_relationship3,
                           4, sr.node_relationship4,
                           5, sr.node_relationship5
                         ) AS node_relationship
                    FROM stg_rec sr,
                         ( -- select a row for each node column
                            SELECT LEVEL AS column_value
                              FROM dual
                           CONNECT BY LEVEL <= 5
                         ) p
               )
               -- check whether any node ID is repeated within the record
               SELECT DISTINCT
                      nc.import_file_id,
                      nc.import_record_id,
                      'system.errors.REPEATED' AS item_key,
                      'Node Name' AS field_name,
                      nc.node_name AS field_value
                 FROM ( -- get node ID counts across the record columns
                        SELECT srp.import_file_id,
                               srp.import_record_id,
                               srp.node_id,
                               srp.node_name,
                               COUNT(srp.node_id)
                                 OVER (PARTITION BY srp.import_file_id, srp.import_record_id, srp.node_id)
                                 AS node_cnt
                          FROM stg_rec_pivot srp
                      ) nc
                WHERE nc.node_cnt > 1
                UNION ALL
               -- at least one node field required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.REQUIRED' AS item_key,
                      'At Least one NODE_ID'  AS field_name,
                      NULL AS field_value
                 FROM stg_rec sr
                WHERE sr.node_id1 IS NULL
                  AND sr.node_id2 IS NULL
                  AND sr.node_id3 IS NULL
                  AND sr.node_id4 IS NULL
                  AND sr.node_id5 IS NULL
                UNION ALL
               -- all node fields required when any node field contains a value
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'system.errors.DEPENDENCY_REQUIRED' AS item_key,
                      'Node Name '         || srp.field_position AS field_name,
                      'Node Relationship ' || srp.field_position AS field_value
                 FROM stg_rec_pivot srp
                WHERE (  srp.node_id           IS NOT NULL
                      OR srp.node_name         IS NOT NULL
                      OR srp.node_relationship IS NOT NULL
                      )
                  AND (  srp.node_id           IS NULL
                      OR srp.node_name         IS NULL
                      OR srp.node_relationship IS NULL
                      )
             ) e
   );*/
   -- 04/10/2014 split above query into two as a part of Bug # 52515
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
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              --AND e.date_created < g_timestamp --04/10/2014
                         )
               )              
               -- at least one node field required
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.REQUIRED' AS item_key,
                      'At Least one NODE_ID'  AS field_name,
                      NULL AS field_value
                 FROM stg_rec sr
                WHERE sr.node_id1 IS NULL
                  AND sr.node_id2 IS NULL
                  AND sr.node_id3 IS NULL
                  AND sr.node_id4 IS NULL
                  AND sr.node_id5 IS NULL               
             ) e
   );
   
   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);
   
   
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
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              --AND e.date_created < g_timestamp --04/10/2014
                         )
               ), stg_rec_pivot AS
               (  -- pivot the node IDs columns with its associated import file/record ID
                  SELECT sr.import_file_id,
                         sr.import_record_id,
                         p.column_value AS field_position,
                         DECODE(p.column_value,
                           1, sr.node_id1,
                           2, sr.node_id2,
                           3, sr.node_id3,
                           4, sr.node_id4,
                           5, sr.node_id5
                         ) AS node_id,
                         DECODE(p.column_value,
                           1, sr.node_name1,
                           2, sr.node_name2,
                           3, sr.node_name3,
                           4, sr.node_name4,
                           5, sr.node_name5
                         ) AS node_name,
                         DECODE(p.column_value,
                           1, sr.node_relationship1,
                           2, sr.node_relationship2,
                           3, sr.node_relationship3,
                           4, sr.node_relationship4,
                           5, sr.node_relationship5
                         ) AS node_relationship
                    FROM stg_rec sr,
                         ( -- select a row for each node column
                            SELECT LEVEL AS column_value
                              FROM dual
                           CONNECT BY LEVEL <= 5
                         ) p
               )    
                -- check whether any node ID is repeated within the record
               SELECT DISTINCT
                      nc.import_file_id,
                      nc.import_record_id,
                      'system.errors.REPEATED' AS item_key,
                      'Org Unit Name' AS field_name,
                      nc.node_name AS field_value
                 FROM ( -- get node ID counts across the record columns
                        SELECT srp.import_file_id,
                               srp.import_record_id,
                               srp.node_id,
                               srp.node_name,
                               COUNT(srp.node_id)
                                 OVER (PARTITION BY srp.import_file_id, srp.import_record_id, srp.node_id)
                                 AS node_cnt
                          FROM stg_rec_pivot srp
                      ) nc
                WHERE nc.node_cnt > 1
                UNION ALL               
               -- all node fields required when any node field contains a value
               SELECT srp.import_file_id,
                      srp.import_record_id,
                      'system.errors.DEPENDENCY_REQUIRED' AS item_key,
                      'Org Unit Name '         || srp.field_position AS field_name,
                      'Org Unit Relationship ' || srp.field_position AS field_value
                 FROM stg_rec_pivot srp
                WHERE (  srp.node_id           IS NOT NULL
                      OR srp.node_name         IS NOT NULL
                      OR srp.node_relationship IS NOT NULL
                      )
                  AND (  srp.node_id           IS NULL
                      OR srp.node_name         IS NULL
                      OR srp.node_relationship IS NULL
                      )
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

   -- validate node field values
   v_msg := 'INSERT import_record_error (field values)';
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
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              --AND e.date_created < g_timestamp --04/10/2014
                         )
               ), stg_node_pivot AS
               (  -- ensure pivoted node data has valid values
                  SELECT LOWER(ccv_ps.cms_name) AS pax_status_name,
                         LOWER(ccv_h.cms_name) AS stage_node_role,
                         -- count of active node owners in import file
                         COUNT(DISTINCT
                               CASE
                                 WHEN (   LOWER(ccv_h.cms_name) = c_node_role_name_owner
                                      AND LOWER(ccv_ps.cms_name) = c_pax_status_name_active
                                      ) THEN pv.user_name
                                 ELSE NULL
                               END) OVER (PARTITION BY pv.node_id) AS import_active_owner_cnt,
                         pv.*
                    FROM ( -- pivot the node IDs columns with its associated import file/record ID
                           SELECT sr.import_file_id,
                                  sr.import_record_id,
                                  sr.user_id,
                                  sr.user_name,
                                  sr.status,
                                  p.column_value AS field_position,
                                  DECODE(p.column_value,
                                    1, sr.node_id1,
                                    2, sr.node_id2,
                                    3, sr.node_id3,
                                    4, sr.node_id4,
                                    5, sr.node_id5
                                  ) AS node_id,
                                  DECODE(p.column_value,
                                    1, sr.node_name1,
                                    2, sr.node_name2,
                                    3, sr.node_name3,
                                    4, sr.node_name4,
                                    5, sr.node_name5
                                  ) AS node_name,
                                  DECODE(p.column_value,
                                    1, sr.node_relationship1,
                                    2, sr.node_relationship2,
                                    3, sr.node_relationship3,
                                    4, sr.node_relationship4,
                                    5, sr.node_relationship5
                                  ) AS node_relationship
                             FROM stg_rec sr,
                                  ( -- select a row for each node column
                                     SELECT LEVEL AS column_value
                                       FROM dual
                                    CONNECT BY LEVEL <= 5
                                  ) p
                         ) pv,
                         vw_cms_code_value ccv_h,  -- hierarchy role
                         vw_cms_code_value ccv_ps  -- participant status
                      -- ensure all import node fields have values
                   WHERE pv.node_id           IS NOT NULL
                     AND pv.node_name         IS NOT NULL
                     --AND pv.node_relationship ='own' --02/17/2014 --04/12/2019
                     AND pv.user_name IS NOT NULL                     
                      -- get CMS node owner code
                     AND ccv_h.asset_code = 'picklist.hierarchyrole.type.items'
                     AND ccv_h.locale     = c_cms_locale
                     AND ccv_h.cms_status = c_cms_status
                      -- get CMS participant status codes
                     AND ccv_ps.asset_code = 'picklist.participantstatus.items'
                     AND ccv_ps.locale     = c_cms_locale
                     AND ccv_ps.cms_status = c_cms_status
                      -- esnsure participant has a valid status and node relationship
                     AND LOWER(ccv_h.cms_code) = LOWER(pv.node_relationship)
                     AND LOWER(ccv_ps.cms_code) = LOWER(pv.status)
               ), curr_node_owner AS
               (  -- derive current node owners by finding all database node owners
                  -- and removing all staged users who are inactive or switched role/node
                  SELECT DISTINCT node_id,--06/24/2015 Added DISTINCT to avoid duplicate rows created.
                         user_id,
                         user_name
                    FROM ( -- join database node owners to all staged node owners
                           SELECT un.node_id,
                                  un.user_id,
                                  au.user_name,
                                  snp.user_id AS snp_user_id,
                                  snp.node_id AS snp_node_id,
                                  snp.pax_status_name,
                                  snp.stage_node_role
                             FROM user_node un,
                                  application_user au,
                                  stg_node_pivot snp
                            WHERE un.role = 'own'
                              AND un.status = 1 -- active
                              AND un.user_id = au.user_id
                              AND un.node_id = snp.node_id --11/20/2014 Bug 58207
                               -- outer join to staged owner
                             -- AND un.user_id = snp.user_id --(+) --07/28/2014 Removed outer join --11/20/2014 Bug 58207
                         )
                      -- only include nodes with active staged owners or not matched in stage
                   WHERE (  snp_node_id IS NULL
                         OR (   node_id = snp_node_id
                            AND pax_status_name = c_pax_status_name_active
                            AND stage_node_role = c_node_role_name_owner
                            )
                         )
               )
               -- find stage records with multiple active owners for the same node
               SELECT snp.import_file_id,
                      snp.import_record_id,
                      'participant.errors.NODE_ALREADY_HAS_OWNER' AS item_key,
--                      'NODE_NAME' || snp.field_position AS field_name,
                      snp.user_name AS field_name, --02/18/2014
                      snp.node_name AS field_value
                 FROM stg_node_pivot snp
                WHERE snp.import_active_owner_cnt > 1
                AND snp.stage_node_role = c_node_role_name_owner --04/12/2019
                UNION ALL
               -- find staged node owners who are not the current database node owners
               SELECT snp.import_file_id,
                      snp.import_record_id,
                      'participant.errors.NODE_ALREADY_HAS_OWNER' AS item_key,
--                      'NODE_NAME' || snp.field_position AS field_name,
                      snp.user_name AS field_name,--02/18/2014
                      snp.node_name AS field_value
                 FROM stg_node_pivot snp,
                      curr_node_owner cno
                WHERE snp.node_id = cno.node_id
                   -- ensure staged node does not have duplicates
                               AND snp.import_active_owner_cnt <= 1   --06/24/2015 Changed this as a fix for allowing onwers swap between org units. --08/26/2015 Un Commented
                  --AND snp.import_active_owner_cnt > 1 --06/24/2015 --Newline added --08/26/2015 Commented. achieved the swap logic in below NOT Exists
                   -- validate only staged active owners
                  AND snp.pax_status_name = c_pax_status_name_active
                  AND snp.stage_node_role = c_node_role_name_owner
                   -- staged node owner not the current node owner
                  AND snp.user_name != cno.user_name
                  AND NOT EXISTS (SELECT 1   --08/26/2015 added the fix for allowing onwers swap between org units
                                    FROM stg_node_pivot stn
                                   WHERE stn.user_name = cno.user_name
                                     --AND stn.pax_status_name = c_pax_status_name_active --04/12/2019
                                    -- AND stn.stage_node_role = c_node_role_name_owner --04/12/2019
                                     AND stn.node_id <> cno.node_id
                                   UNION
                                  SELECT 1    ----08/08/2017 - G6-2796
                                    FROM stg_node_pivot stn
                                   WHERE stn.user_name = cno.user_name
                                     --AND stn.pax_status_name = c_pax_status_name_inactive --04/12/2019
                                     --AND stn.stage_node_role = c_node_role_name_owner --04/12/2019
                                     AND stn.stage_node_role != c_node_role_name_owner
                                     AND stn.node_id = cno.node_id)                                     
                  UNION     --12/10/2015  --12/16/2015 Removed UNION ALL and added UNION 
               -- find staged node owners who are not the current database node owners
               SELECT stnp.import_file_id,      --04/21/2016
                      stnp.import_record_id,
                      'participant.errors.NODE_ALREADY_HAS_OWNER' AS item_key,
                      stnp.user_name AS field_name,
                      stnp.node_name AS field_value
                 FROM stg_node_pivot stnp,
                      (SELECT snp.user_name,
                              cnoo.node_id 
                         FROM stg_node_pivot snp,
                              curr_node_owner cno,
                              curr_node_owner cnoo
                        WHERE snp.node_id = cno.node_id
                           -- ensure staged node does not have duplicates
                          AND snp.import_active_owner_cnt <= 1
                           -- validate only staged active owners
                          AND snp.pax_status_name = c_pax_status_name_active
                          AND snp.stage_node_role = c_node_role_name_owner
                           -- staged node owner not the current node owner
                          AND snp.user_name != cno.user_name
                          AND snp.user_name  = cnoo.user_name
                          AND NOT EXISTS ( SELECT 1   
                                            FROM stg_node_pivot stn,
                                                 stg_node_pivot st,
                                                 curr_node_owner cn
                                           WHERE stn.user_name = cn.user_name
                                             AND stn.pax_status_name = c_pax_status_name_active
                                             AND stn.stage_node_role = c_node_role_name_owner
                                             AND stn.node_id <> cno.node_id --04/21/2016
                                             AND st.pax_status_name = c_pax_status_name_active
                                             AND st.stage_node_role = c_node_role_name_owner
                                             AND st.node_id = cn.node_id)
                                            ) ss
                WHERE stnp.node_id = ss.node_id
                  AND stnp.pax_status_name = c_pax_status_name_active
                  AND stnp.stage_node_role = c_node_role_name_owner
                   -- staged node owner not the current node owner
                  AND stnp.user_name != ss.user_name
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_node_err;

-- Reports miscellaneous errors
PROCEDURE p_rpt_misc_err
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_total_err_count IN OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_rpt_misc_err');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- validate miscellaneous requirements
   v_msg := 'INSERT import_record_error';
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
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM (
               WITH stg_rec AS
               (  -- get stage records
                  SELECT s.*
                    FROM stage_pax_import_record s
                   WHERE s.import_file_id = p_import_file_id
                      -- skip records previously marked as erred
                     AND s.import_record_id NOT IN
                         ( SELECT e.import_record_id
                             FROM import_record_error e
                            WHERE e.import_file_id = p_import_file_id
                              --AND e.date_created < g_timestamp --04/10/2014
                         )
               )
               -- employee termination date must be after hire date
               SELECT sr.import_file_id,
                      sr.import_record_id,
                      'participant.errors.TERMINATION_AFTER_HIRE' AS item_key,
                      'Termination Date' AS field_name,
                      TO_CHAR(sr.termination_date, 'MM/DD/YYYY') AS field_value
                 FROM stg_rec sr,
                      vw_curr_pax_employer cpe
                WHERE sr.termination_date IS NOT NULL
                   -- outer join to current employer
                  AND sr.user_id = cpe.user_id (+)
                   -- termination date must be after import hire date, current hire date, or system date
--                  AND sr.termination_date <= NVL(NVL(sr.hire_date, cpe.hire_date), TRUNC(SYSDATE))
                    AND sr.termination_date < NVL(NVL(sr.hire_date, cpe.hire_date), TRUNC(sr.termination_date))--05/05/2017-G6-2326
                  UNION ALL -- Added as a fix for Bug # 51995 --03/26/2014
                SELECT sr.import_file_id,
                      sr.import_record_id,
                      'system.errors.DEPENDENCY_REQUIRED' AS item_key,
                      'Email Address' AS field_name,
                      'Email Address Type' AS field_value
                 FROM stg_rec sr
                WHERE 
                ((email_address IS NOT NULL AND email_address_type IS NULL)
                OR (email_address IS NULL AND email_address_type IS NOT NULL))               
                UNION ALL
               -- validate duplicate role values within a record
               SELECT DISTINCT
                      rc.import_file_id,
                      rc.import_record_id,
                      'system.errors.REPEATED' AS item_key,
                      'Role Description' AS field_name,
                      rc.role_description AS field_value
                 FROM ( -- get count of role ID values in the import record
                        SELECT COUNT(pv.role_id)
                                 OVER (PARTITION BY pv.import_file_id, pv.import_record_id, pv.role_id)
                                 AS role_cnt,
                               pv.*
                          FROM ( -- pivot the role columns with its associated import file/record ID
                                 SELECT sr.import_file_id,
                                        sr.import_record_id,
                                        p.column_value AS field_position,
                                        DECODE(p.column_value,
                                          1, sr.role_id1,
                                          2, sr.role_id2,
                                          3, sr.role_id3,
                                          4, sr.role_id4,
                                          5, sr.role_id5
                                        ) AS role_id,
                                        DECODE(p.column_value,
                                          1, sr.role_description1,
                                          2, sr.role_description2,
                                          3, sr.role_description3,
                                          4, sr.role_description4,
                                          5, sr.role_description5
                                        ) AS role_description
                                   FROM stg_rec sr,
                                        ( -- select a row for each role column
                                           SELECT LEVEL AS column_value
                                             FROM dual
                                          CONNECT BY LEVEL <= 5
                                        ) p
                               ) pv
                      ) rc
                WHERE rc.role_cnt > 1
             ) e
   );

   v_rec_cnt := SQL%ROWCOUNT;
   p_total_err_count := p_total_err_count + v_rec_cnt;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_rpt_misc_err;

-- imports application user from the stage table
PROCEDURE p_imp_application_user
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_application_user');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
   v_tc_accepted     NUMBER; ---1/08/2014 Bug # 49816
   v_force_passwd  NUMBER(1);
BEGIN

   -- Find status of Terms and Conditions variable --01/08/2014 Bug # 49816
   
SELECT boolean_val 
      INTO v_tc_accepted 
      FROM os_propertyset 
     WHERE lower(ENTITY_NAME) = 'termsandconditions.used';
     
      -- Find status of  Force password reset variable --01/28/2014 Bug # 51285
SELECT boolean_val  
      INTO v_force_passwd
      FROM os_propertyset
      WHERE entity_name='password.force.reset';
     
   -- import staged records
   v_msg := 'MERGE application_user';
   MERGE INTO application_user d
   USING (  -- get staged data
            SELECT s.user_id,
                   s.user_name,
                   'pax' AS user_type,
                   s.first_name,
                   s.middle_name,
                   s.last_name,
                   s.suffix,
                   fnc_java_encrypt(s.ssn) AS ssn,
                   s.birth_date,
                   LOWER(s.gender) AS gender,
                   s.language_id, --09/23/2013
                    --                   DECODE(s.termination_date,     --03/30/2017
                    --                     NULL, s.active,
                    --                     0  -- inactivate user when termination date not null
                    --                   ) AS is_active,
                    CASE WHEN s.termination_date IS NOT NULL THEN 0      --03/30/2017
                         WHEN s.termination_date IS NULL AND NVL(pax.is_opt_out_of_program,0) = 1 THEN 0      
                         WHEN s.termination_date IS NULL AND NVL(pax.is_opt_out_of_program,0) = 0 THEN s.active
                         WHEN NVL(pax.is_opt_out_of_program,0) = 1 THEN 0
                    END AS is_active,     
                   s.termination_date --02/20/2015-- Bug # 57192,59819
                   ,NVL(pax.is_opt_out_of_program,0) AS is_opt_out_of_program  --03/30/2017
              FROM stage_pax_import_record s,
                   participant pax 
             WHERE s.import_file_id = p_import_file_id   AND
                   s.user_id = pax.user_id (+)
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
         ) s
      ON (d.user_name = s.user_name)
    WHEN MATCHED THEN
      UPDATE
             -- staged data over lays database values
         SET d.first_name     = s.first_name,
             d.middle_name    = s.middle_name,
             d.last_name      = s.last_name,
             d.suffix         = s.suffix,
             -- update only with not null stage values
             d.user_type      = NVL(s.user_type, d.user_type),
             d.ssn            = NVL(s.ssn, d.ssn),
             d.birth_date     = NVL(s.birth_date, d.birth_date),
             d.gender         = NVL(s.gender, d.gender),
--             d.is_active      = NVL(s.is_active, d.is_active),
             d.is_active      = --DECODE(v_tc_accepted,1,DECODE(d.is_cms_terms_accepted,1,NVL(s.is_active, d.is_active),0,d.is_active),0,NVL(s.is_active, d.is_active)),--01/08/2014  Bug # 49816   --02/20/2015-- Bug # 57192,59819   --03/30/2017
             CASE 
                --                    WHEN v_tc_accepted = 1 AND d.is_cms_terms_accepted = 1 AND s.termination_date IS NULL ---02/20/2015 -- Bug # 57192, 59819
                --                                                        THEN NVL(s.is_active, d.is_active)
                --                                                        WHEN v_tc_accepted = 1 AND d.is_cms_terms_accepted = 0 AND s.termination_date IS NULL
                --                                                        THEN d.is_active
                --                                                        WHEN v_tc_accepted = 0 AND s.termination_date IS NULL
                --                                                        THEN NVL(s.is_active, d.is_active)
                --                                                        WHEN s.termination_date IS NOT NULL
                --                                                        THEN s.is_active
                --                                                        END,      
                        WHEN v_tc_accepted = 1 AND d.is_cms_terms_accepted = 1 AND s.termination_date IS NULL AND s.is_opt_out_of_program = 0 ---02/20/2015 -- Bug # 57192, 59819   --03/30/2017
                        THEN NVL(s.is_active, d.is_active)
                        WHEN v_tc_accepted = 1 AND d.is_cms_terms_accepted = 0 AND s.termination_date IS NULL  AND s.is_opt_out_of_program = 0
                        THEN d.is_active
                        WHEN v_tc_accepted = 0 AND s.termination_date IS NULL  AND s.is_opt_out_of_program = 0
                        THEN NVL(s.is_active, d.is_active)
                        WHEN s.termination_date IS NOT NULL
                        THEN s.is_active
                        WHEN s.is_opt_out_of_program = 1
                        THEN s.is_active
             END,      
             d.language_id = NVL(s.language_id, NVL(d.language_id,v_default_language_id)),--09/23/2013 --11/28/2017
             -- track update
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
          -- only update if data differs (decode handles null values)
       WHERE NOT (   DECODE(d.first_name,  s.first_name,                    1, 0) = 1
                 AND DECODE(d.middle_name, s.middle_name,                   1, 0) = 1
                 AND DECODE(d.last_name,   s.last_name,                     1, 0) = 1
                 AND DECODE(d.suffix,      s.suffix,                        1, 0) = 1
                 AND DECODE(d.user_type,   NVL(s.user_type, d.user_type),   1, 0) = 1
                 AND DECODE(d.ssn,         NVL(s.ssn, d.ssn),               1, 0) = 1
                 AND DECODE(d.birth_date,  NVL(s.birth_date, d.birth_date), 1, 0) = 1
                 AND DECODE(d.gender,      NVL(s.gender, d.gender),         1, 0) = 1
                 AND DECODE(d.is_active,   NVL(s.is_active, d.is_active),   1, 0) = 1
                 AND DECODE(d.language_id,   NVL(s.language_id,NVL(d.language_id,v_default_language_id)),   1, 0) = 1--09/23/2013 --11/28/2017
                 )
    WHEN NOT MATCHED THEN
      INSERT
      (  user_id,
         user_name,
         user_type,
         first_name,
         middle_name,
         last_name,
         suffix,
         ssn,
         birth_date,
         gender,
         is_active,
         -- default values
         force_password_change,
         title,
         password,
         date_modified,
         is_welcome_email_sent,
         is_cms_terms_accepted,
         login_failures_count,
         last_reset_date,
         secret_question,
         secret_answer,
         master_user_id,
         language_id,
         enrollment_source,
         enrollment_date,
         created_by,
         date_created,
         modified_by,
         version,
         is_text_messages_terms_accept,
         is_profile_setup_done,
         roster_user_id  -- 11/28/2019
      )
      VALUES
      (  user_pk_sq.NEXTVAL,  -- user_id,
         s.user_name,
         s.user_type,
         s.first_name,
         s.middle_name,
         s.last_name,
         s.suffix,
         s.ssn,
         s.birth_date,
         s.gender,
         DECODE(v_tc_accepted,1,0,s.is_active), --01/08/2014 Bug # 49816
         -- default values
         v_force_passwd,             -- force_password_change, --01/28/2014 Bug # 51285
         NULL,          -- title,
         NULL,          -- password,
         NULL,          -- date_modified,
         0,             -- is_welcome_email_sent,
         0,             -- is_cms_terms_accepted,
         0,             -- login_failures_count,
         NULL,          -- last_reset_date,
         NULL,          -- secret_question,
         NULL,          -- secret_answer,
         NULL,          -- master_user_id,
         NVL(s.language_id,v_default_language_id),  -- language_id, --09/23/2013 --11/28/2017
         NULL,          -- enrollment_source,
         g_timestamp,   -- enrollment_date,
         g_created_by,  -- created_by,
         g_timestamp,   -- date_created,
         NULL,          -- modified_by,
         0,             -- version,
         0,              -- is_text_messages_terms_accept
         0,               --is_profile_setup_done
         fnc_randomuuid  -- 11/28/2019
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_application_user;

-- imports application user from the stage table
PROCEDURE p_imp_participant
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_participant');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
   v_inactive_status    vw_cms_code_value.cms_code%TYPE;
   -- Bug #20193 - participant.terms_acceptance not popluated in participant load
   v_terms_used         os_propertyset.boolean_val%TYPE := fnc_get_osp_bool('termsAndConditions.used');
--   v_sso_id_flag           os_propertyset.string_val%TYPE;
   

   CURSOR cur_inactive_status IS
   SELECT ccv.cms_code AS inactive_status
     FROM vw_cms_code_value ccv
    WHERE ccv.asset_code = 'picklist.participantstatus.items'
      AND ccv.locale     = c_cms_locale
      AND ccv.cms_status = c_cms_status
      AND LOWER(ccv.cms_name)   = c_pax_status_name_inactive  --08/08/2017 G6-2796
      ;

BEGIN
   
--   --Check if SSO-ID is required 
--   SELECT string_val
--      INTO v_sso_id_flag 
--      FROM os_propertyset 
--     WHERE lower(ENTITY_NAME) = 'sso.unique.id';
   
   -- initialize variables
   
   
   v_msg := 'OPEN cur_inactive_status';
   OPEN cur_inactive_status;
   v_msg := 'OPEN cur_inactive_status';
   FETCH cur_inactive_status INTO v_inactive_status;
   CLOSE cur_inactive_status;

   -- import staged records
   v_msg := 'MERGE participant';
   MERGE INTO participant d
   USING (  -- get staged data
            SELECT au.user_id,
                   s.user_name,
--                   DECODE(s.termination_date,         --03/30/2017
--                     NULL, LOWER(s.status),
--                     v_inactive_status  --   user when termination date not null
--                   ) AS status,
                    CASE WHEN s.termination_date IS NOT NULL THEN v_inactive_status     --03/30/2017
                         WHEN s.termination_date IS NULL AND NVL(pax.is_opt_out_of_program,0) = 1 THEN v_inactive_status      
                         WHEN s.termination_date IS NULL AND NVL(pax.is_opt_out_of_program,0) = 0 THEN LOWER(s.status)
                         WHEN NVL(pax.is_opt_out_of_program,0) = 1 THEN v_inactive_status
                    END AS status,     
                   s.terms_acceptance,
                   s.user_id_accepted,
                   s.date_terms_accepted,
                   s.sso_id,
                   s.termination_date
              FROM stage_pax_import_record s,
                   application_user au,
                   participant pax
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
               AND au.user_id = pax.user_id (+)
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
         ) s
      ON (d.user_id = s.user_id)
    WHEN MATCHED THEN
      UPDATE
          -- update only with not null stage values
         SET 
                --             d.status              = CASE WHEN v_terms_used=1 AND d.terms_acceptance='accepted' AND s.termination_date IS NULL--Bug # 49816--Bug # 51792 --02/26/2014  --03/30/2017
                --                                                        THEN NVL(s.status, d.status)
                --                                                        WHEN v_terms_used=1 AND d.terms_acceptance='notaccepted' AND s.termination_date IS NULL
                --                                                        THEN d.status
                --                                                        WHEN v_terms_used=0 AND s.termination_date IS NULL
                --                                                        THEN NVL(s.status, d.status)
                --                                                        WHEN s.termination_date IS NOT NULL
                --                                                        THEN s.status
                --                                                        END,                                                        
             d.status              = CASE WHEN v_terms_used=1 AND d.terms_acceptance='accepted' AND s.termination_date IS NULL AND NVL(d.is_opt_out_of_program,0) = 0  --Bug # 49816--Bug # 51792 --02/26/2014 --03/30/2017
                                                        THEN NVL(s.status, d.status)
                                                        WHEN v_terms_used=1 AND d.terms_acceptance='notaccepted' AND s.termination_date IS NULL AND NVL(d.is_opt_out_of_program,0) = 0
                                                        THEN d.status
                                                        WHEN v_terms_used=0 AND s.termination_date IS NULL AND NVL(d.is_opt_out_of_program,0) = 0
                                                        THEN NVL(s.status, d.status)
                                                        WHEN s.termination_date IS NOT NULL
                                                        THEN s.status
                                                        WHEN NVL(d.is_opt_out_of_program,0) = 1
                                                        THEN s.status
                                                        END,                                                        
             d.date_status_change  = CASE
                                       WHEN (s.status is NOT NULL AND LOWER(d.status) != s.status) THEN g_timestamp
                                       ELSE d.date_status_change
                                     END,
              d.terms_acceptance    = CASE WHEN v_terms_used=1 AND s.termination_date IS NOT NULL THEN 'accepted' --Bug # 51131
                                                   ELSE NVL(s.terms_acceptance, d.terms_acceptance) END,
             d.user_id_accepted    = CASE WHEN v_terms_used=1 AND s.termination_date IS NOT NULL THEN '5662'
                                                   ELSE NVL(s.user_id_accepted, d.user_id_accepted) END,
             d.date_terms_accepted = CASE WHEN v_terms_used=1 AND s.termination_date IS NOT NULL THEN SYSDATE
                                                   ELSE NVL(s.date_terms_accepted, d.date_terms_accepted) END,
              d.termination_date = s.termination_date,--03/23/2017
              d.sso_id  = NVL(s.sso_id,d.sso_id), -- 03/03/2015 corrected NVL(d.sso_id,s.sso_id)
              d.date_modified = SYSDATE--07/07/2016
          -- only update if data differs (decode handles null values)
       WHERE NOT (   DECODE(d.status,              NVL(s.status, d.status),                           1, 0) = 1
                 AND DECODE(d.date_status_change,  CASE
                                                      WHEN (s.status is NOT NULL AND LOWER(d.status) != s.status) THEN g_timestamp
                                                      ELSE d.date_status_change
                                                   END,                                               1, 0) = 1
                 AND DECODE(d.terms_acceptance,    NVL(s.terms_acceptance, d.terms_acceptance),       1, 0) = 1
                 AND DECODE(d.user_id_accepted,    NVL(s.user_id_accepted, d.user_id_accepted),       1, 0) = 1
                 AND DECODE(d.date_terms_accepted, NVL(s.date_terms_accepted, d.date_terms_accepted), 1, 0) = 1
                 AND DECODE(d.sso_id,    NVL(s.sso_id, d.sso_id),       1, 0) = 1  -- 03/03/2015 corrected NVL(d.sso_id,s.sso_id)
                 AND DECODE(d.termination_date,    NVL(s.termination_date, d.termination_date),       1, 0) = 1--03/23/2017
                 )
    WHEN NOT MATCHED THEN
      INSERT
      (  user_id,
         awardbanq_nbr,
         date_awardbanq_extract,
         centrax_id,
         relationship_type,
         status,
         date_status_change,
         suspension_status,
         terms_acceptance,
         user_id_accepted,
         date_terms_accepted,
         sso_id,
         termination_date,
         date_created
      )
      VALUES
      (  s.user_id,
         NULL, -- awardbanq_nbr,
         NULL, -- date_awardbanq_extract,
         NULL, -- centrax_id,
         NULL, -- relationship_type,
         CASE WHEN v_terms_used=1 THEN 'inactive'
         ELSE s.status
         END,
         NULL, -- date_status_change,
         NULL, -- suspension_status,
--         CASE -- Bug #20193 fix
--            WHEN (s.terms_acceptance IS NOT NULL) THEN s.terms_acceptance
--            WHEN (v_terms_used = 1 AND s.status = 'active') THEN 'notaccepted'
--            ELSE NULL
--         END,  -- terms_acceptance,
          CASE WHEN s.termination_date IS NOT NULL AND v_terms_used = 1
         THEN 'accepted'
         ELSE 'notaccepted'
         END, -- terms_acceptance,
         s.user_id_accepted,
         s.date_terms_accepted,
         s.sso_id,
         s.termination_date,
         SYSDATE--07/07/2016
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_participant;

-- imports user address from the stage table
PROCEDURE p_imp_user_address
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE,
  p_out_returncode  OUT NUMBER
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_user_address');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
   v_country_change_rec_cnt NUMBER:=0;   

BEGIN
             
   -- import staged records
   
    v_msg := 'FIND country changes for primary addresses'; --07/23/2013
   
    INSERT INTO user_country_changes 
                               (user_country_changes_id, 
                                import_file_id, 
                                user_id, 
                                old_country_id,
                                old_campaign_nbr,
                                old_awardbanq_nbr,
                                old_centrax_id,
                                new_country_id,
                                new_campaign_nbr,
                                new_awardbanq_nbr,
                                is_processed,
                                date_created,
                                created_by,
                                VERSION )
                        (SELECT 
                                user_country_changes_pk_sq.NEXTVAL,                                              
                                p_import_file_id,
                                ua.user_id,
                                c_old.country_id, 
                                c_old.campaign_nbr, 
                                --fnc_java_decrypt(p.awardbanq_nbr),
                                --fnc_java_decrypt(P.centrax_id),
                                p.awardbanq_nbr,--04/29/2014
                                P.centrax_id,--04/29/2014
                                c_new.country_id, 
                                c_new.campaign_nbr,
                                NULL, -- new_awardbanq_nbr
                                0,    -- is_processed initial value
                                SYSDATE,
                                g_created_by,
                                0 
                           FROM user_address ua,stage_pax_import_record s,country c_old,country c_new,participant p
       WHERE s.import_file_id  = p_import_file_id
              AND ua.user_id = s.user_id               
              AND ua.is_primary = 1
              AND  NOT (   DECODE(ua.country_id,  s.country_id,  1, 0) = 1 )
              AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
              AND ua.country_id = c_old.country_id
              AND s.country_id = c_new.country_id
              AND ua.user_id  =  p.user_id);

 v_country_change_rec_cnt := SQL%ROWCOUNT;

   -- Remove awardbanq data from participants with country change
      v_msg := 'Remove awardbanq data from participants with country change';
   
   UPDATE participant  --Bug # 52457 03/27/2014
   SET awardbanq_nbr = NULL,
          date_awardbanq_extract = NULL,
          centrax_id = NULL,
          date_modified = SYSDATE --07/07/2016
   WHERE user_id IN (
   SELECT user_id FROM
   user_country_changes WHERE
   import_file_id = p_import_file_id);
 
   -- import staged records
   v_msg := 'MERGE user_address';
      MERGE INTO user_address d                       --09/24/2013 Re-written merge for Fix#4424
    USING (  -- get staged data
        SELECT au.user_id,
               s.user_name,
               LOWER(s.address_type) AS address_type,
               s.country_id,
               s.address_1 AS addr1,
               s.address_2 AS addr2,
               s.address_3 AS addr3,
               s.city,
               CASE
                 -- format state code 
                 WHEN (s.state IS NOT NULL) THEN LOWER(s.country_code || '_' || s.state) --07/23/2013
                 ELSE s.state
               END AS state,
               s.postal_code,
               1 is_primary
          FROM stage_pax_import_record s,
               application_user au
         WHERE s.import_file_id = p_import_file_id
           AND s.user_name = au.user_name
            -- skip records marked as erred
           AND s.import_record_id NOT IN
               ( SELECT e.import_record_id
                   FROM import_record_error e
                  WHERE e.import_file_id = p_import_file_id
               )
         UNION   
              SELECT au.user_id,
               au.user_name,
               LOWER(ua.address_type) AS address_type,
               ua.country_id,
               ua.addr1 AS addr1,
               ua.addr2 AS addr2,
               ua.addr3 AS addr3,
               ua.city,
               ua.state,
               ua.postal_code,
               0 is_primary
              FROM user_address ua,
                   application_user au,
                   stage_pax_import_record s
             WHERE ua.user_id = s.user_id
               AND s.user_id = au.user_id
               AND s.import_file_id = p_import_file_id
               AND NOT EXISTS (SELECT 1
                              FROM stage_pax_import_record sa
                              WHERE sa.import_file_id = p_import_file_id
                                 AND  sa.import_record_id NOT IN
                                       ( SELECT e.import_record_id
                                           FROM import_record_error e
                                          WHERE e.import_file_id = p_import_file_id
                                       )
                                  AND sa.user_id = ua.user_id
                                  AND sa.address_type = ua.address_type
                              )
               AND  s.import_record_id NOT IN
                       ( SELECT e.import_record_id
                           FROM import_record_error e
                          WHERE e.import_file_id = p_import_file_id
                       )             
         ) s
       ON (   d.user_id = s.user_id
         AND d.address_type = s.address_type
         )
    WHEN MATCHED THEN
      UPDATE
          -- staged data over lays database values
         SET d.country_id     = s.country_id,
             d.addr1          = s.addr1,
             d.addr2          = s.addr2,
             d.addr3          = s.addr3,
             d.city           = s.city,
             d.state          = s.state,
             d.postal_code    = s.postal_code,
             d.is_primary     = s.is_primary,  
             -- track update
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
          -- only update if data differs (decode handles null values)
       WHERE NOT (   DECODE(d.country_id,  s.country_id,  1, 0) = 1
                 AND DECODE(d.addr1,       s.addr1,       1, 0) = 1
                 AND DECODE(d.addr2,       s.addr2,       1, 0) = 1
                 AND DECODE(d.addr3,       s.addr3,       1, 0) = 1
                 AND DECODE(d.city,        s.city,        1, 0) = 1
                 AND DECODE(d.state,       s.state,       1, 0) = 1
                 AND DECODE(d.postal_code, s.postal_code, 1, 0) = 1
                 AND DECODE(d.is_primary, s.is_primary, 1, 0) = 1
                 )
    WHEN NOT MATCHED THEN
      INSERT
      (  user_address_id,
         user_id,
         address_type,
         country_id,
         addr1,
         addr2,
         addr3,
         city,
         state,
         postal_code,
         is_primary,
         created_by,
         date_created,
         modified_by,
         date_modified,
         version,
         roster_address_id  -- 11/28/2019
      )
      VALUES
      (  user_address_pk_sq.NEXTVAL,   -- user_address_id,
         s.user_id,
         s.address_type,
         s.country_id,
         s.addr1,
         s.addr2,
         s.addr3,
         s.city,
         s.state,
         s.postal_code,
         s.is_primary,    
         g_created_by,  -- created_by,
         g_timestamp,   -- date_created,
         NULL,          -- modified_by,
         NULL,          -- date_modified,
         0,              -- version
         fnc_randomuuid  -- 11/28/2019
      );           --09/24/2013 end Re-written merge for Fix#4424

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

IF v_country_change_rec_cnt > 0 THEN -- Bug # 52457
p_out_returncode :=2;
ELSE
p_out_returncode:=0;
END IF;

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_user_address;

-- imports user email address from the stage table
PROCEDURE p_imp_user_email_address
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_user_email_address');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
   v_email_type_sms     vw_cms_code_value.cms_code%TYPE;

   CURSOR cur_get_email_type_sms IS
   SELECT ccv.cms_code
     FROM vw_cms_code_value ccv
    WHERE ccv.asset_code = 'picklist.emailtype.items'
      AND ccv.locale     = c_cms_locale
      AND ccv.cms_status = c_cms_status
      AND ccv.cms_name   = c_email_type_name_sms
      ;

BEGIN
   -- initialize variables
   v_msg := 'OPEN cur_get_email_type_sms';
   OPEN cur_get_email_type_sms;
   v_msg := 'FETCH cur_get_email_type_sms';
   FETCH cur_get_email_type_sms INTO v_email_type_sms;
   CLOSE cur_get_email_type_sms;

   -- import staged records
   v_msg := 'MERGE user_email_address';
   MERGE INTO user_email_address d
   USING (  -- get staged data
            SELECT au.user_id,
                   s.user_name,
                   LOWER(s.email_address_type) AS email_type,
                   s.email_address AS email_addr,
                   ( -- get count of existing primary user email address
                     SELECT COUNT(uea.user_id)
                       FROM user_email_address uea
                      WHERE uea.user_id = au.user_id
                        AND uea.is_primary = 1
                   ) AS primary_addr_cnt 
              FROM stage_pax_import_record s,
                   application_user au
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
               AND s.email_address_type IS NOT NULL
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
         ) s
      ON (   d.user_id = s.user_id
         AND d.email_type = s.email_type
         )
    WHEN MATCHED THEN
      UPDATE
          -- staged data over lays database values
         SET d.email_addr     = s.email_addr,
             -- track update
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
          -- only update if data differs
       WHERE NOT (d.email_addr = s.email_addr)
    WHEN NOT MATCHED THEN
      INSERT
      (  email_address_id,
         user_id,
         email_type,
         email_addr,
         email_status,
         is_primary,
         created_by,
         date_created,
         modified_by,
         date_modified,
         version,
         roster_email_id  -- 11/28/2019
      )
      VALUES
      (  user_email_address_pk_sq.NEXTVAL, -- email_address_id,
         s.user_id,
         s.email_type,
         s.email_addr,
         NULL,          -- email_status,
         CASE
            -- text message address is never the primary
            WHEN (s.email_type = v_email_type_sms) THEN 0
            -- when no primary address exists, this record is primary
            WHEN (s.primary_addr_cnt = 0) THEN 1
            ELSE 0
         END,           -- is_primary,
         g_created_by,  -- created_by,
         g_timestamp,   -- date_created,
         NULL,          -- modified_by,
         NULL,          -- date_modified,
         0,              -- version
         fnc_randomuuid  -- 11/28/2019
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);


   v_msg := 'MERGE user_email_address (text msg addr)';
   MERGE INTO user_email_address d
   USING (  -- get staged data
            SELECT au.user_id,
                   s.user_name,
                   v_email_type_sms AS email_type,
                   s.text_message_address AS email_addr,
                   -- text message is never the primary
                   0 AS is_primary
              FROM stage_pax_import_record s,
                   application_user au
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
               AND s.text_message_address IS NOT NULL
               AND v_email_type_sms IS NOT NULL
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
         ) s
      ON (   d.user_id = s.user_id
         AND d.email_type = s.email_type
         )
    WHEN MATCHED THEN
      UPDATE
          -- staged data over lays database values
         SET d.email_addr     = s.email_addr,
             d.is_primary     = s.is_primary,
             -- track update
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
          -- only update if data differs
       WHERE NOT (   d.email_addr = s.email_addr
                 AND d.is_primary = s.is_primary
                 )
    WHEN NOT MATCHED THEN
      INSERT
      (  email_address_id,
         user_id,
         email_type,
         email_addr,
         email_status,
         is_primary,
         created_by,
         date_created,
         modified_by,
         date_modified,
         version,
         roster_email_id  -- 11/28/2019
      )
      VALUES
      (  user_email_address_pk_sq.NEXTVAL, -- email_address_id,
         s.user_id,
         s.email_type,
         s.email_addr,
         NULL,          -- email_status,
         s.is_primary,
         g_created_by,  -- created_by,
         g_timestamp,   -- date_created,
         NULL,          -- modified_by,
         NULL,          -- date_modified,
         0,              -- version
         fnc_randomuuid  -- 11/28/2019
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_user_email_address;

-- imports user phone from the stage table
PROCEDURE p_imp_user_phone
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_user_phone');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- import staged records
      v_msg := 'MERGE user_phone for phone numbers to be removed';
      
      MERGE INTO user_phone d  --12/17/2013 
   USING (  -- get staged data joined to any existing primary records so is_primary can be properly set     
          SELECT stg_p.user_id,
                   stg_p.user_name,
                   stg_p.country_code,
                   stg_p.phone_type,
                   stg_p.phone_nbr AS phone_nbr,
                   -- rank phone numbers by primary sequence, rank 1 is the primary
                   DECODE( RANK() OVER (PARTITION BY stg_p.user_id
                                            ORDER BY DECODE((stg_p.phone_nbr),
                                                        NULL, NULL,
                                                        stg_p.is_primary_seq)
                                       ),
                     1, 1,  -- rank 1 is primary
                     0      -- all other ranks not primary
                   ) AS is_primary
              FROM ( -- pivot phone number columns into rows
                     SELECT au.user_id,
                            s.user_name,
                            s.country_code,
                            DECODE(p.phone_name,
                              'Home', s.personal_phone_number,
                              'Business', s.business_phone_number,
                              'Mobile', s.cell_phone_number,
                              NULL
                            ) AS phone_nbr,
                            p.phone_type,
                            p.is_primary_seq
                       FROM stage_pax_import_record s,
                            application_user au,
                            ( -- get phone type codes
                              SELECT ccv.cms_code AS phone_type,
                                     ccv.cms_name AS phone_name,
                                     -- specify order in which primary phone number is determined
                                     RANK() OVER (ORDER BY
                                                   DECODE(ccv.cms_name,
                                                      'Home', '001',
                                                      'Business', '002',
                                                      'Mobile', '003',
                                                      'Other', '004',
                                                      'Fax', '005',
                                                      ccv.cms_name
                                                   )
                                                 ) AS is_primary_seq
                                FROM vw_cms_code_value ccv
                               WHERE ccv.asset_code = 'picklist.phonetype.items'
                                 AND ccv.locale     = c_cms_locale
                                 AND ccv.cms_status = c_cms_status
                            ) p
                      WHERE s.import_file_id = p_import_file_id
                        AND s.user_name = au.user_name
                         -- skip records marked as erred
                        AND s.import_record_id NOT IN
                            ( SELECT e.import_record_id
                                FROM import_record_error e
                               WHERE e.import_file_id = p_import_file_id
                            )
                        AND (personal_phone_number IS NOT NULL -- 05/22/2015 Bug 61961
                            OR business_phone_number IS NOT NULL
                            OR cell_phone_number IS NOT NULL)
                   ) stg_p,
                   user_phone up
                -- outer join stage records to any existing primary records
             WHERE stg_p.user_id = up.user_id(+)
               AND stg_p.phone_type = up.phone_type(+)
               AND 1 = up.is_primary(+)
                -- exclude records with no associated phone numbers
               AND NVL(stg_p.phone_nbr, up.phone_nbr) IS NOT NULL
               AND stg_p.phone_nbr IS NULL        
         ) s
      ON (   d.user_id = s.user_id
         AND d.phone_type = s.phone_type
         )
    WHEN MATCHED THEN
     UPDATE
       SET DATE_MODIFIED=SYSDATE
       WHERE s.phone_nbr IS NULL
               DELETE WHERE s.phone_nbr IS NULL;

v_msg := 'MERGE user_phone';
 
   MERGE INTO user_phone d
   USING (  -- get staged data joined to any existing primary records so is_primary can be properly set
            SELECT stg_p.user_id,
                   stg_p.user_name,
                   stg_p.country_code,
                   stg_p.phone_type,
                   stg_p.phone_nbr AS phone_nbr,
                   -- rank phone numbers by primary sequence, rank 1 is the primary
                   DECODE( RANK() OVER (PARTITION BY stg_p.user_id
                                            ORDER BY DECODE(NVL(stg_p.phone_nbr, up.phone_nbr),
                                                        NULL, NULL,
                                                        stg_p.is_primary_seq)
                                       ),
                     1, 1,  -- rank 1 is primary
                     0      -- all other ranks not primary
                   ) AS is_primary,
                   --06/08/2016 Bug 67002 fix added is_txtmsg_avlbl flag
                   ( SELECT COUNT(*) 
                       FROM participant_comm_preference pcp,
                            user_phone up1
                      WHERE stg_p.user_id = pcp.user_id 
                        AND pcp.user_id = up1.user_id 
                        AND up1.phone_type = stg_p.phone_type
                        AND up1.phone_type = 'mob'
                        AND pcp.communication_code = 'textmessages' 
                        AND sms_group_type IS NOT NULL
                   )     AS is_txtmsg_avlbl                 
              FROM ( -- pivot phone number columns into rows
                     SELECT au.user_id,
                            s.user_name,
                            s.country_code,
                            DECODE(p.phone_name,
                              c_phone_type_name_hom, s.personal_phone_number,
                              c_phone_type_name_bus, s.business_phone_number,
                              c_phone_type_name_mob, s.cell_phone_number,
                              NULL
                            ) AS phone_nbr,
                            p.phone_type,
                            p.is_primary_seq
                       FROM stage_pax_import_record s,
                            application_user au,
                            ( -- get phone type codes
                              SELECT ccv.cms_code AS phone_type,
                                     ccv.cms_name AS phone_name,
                                     -- specify order in which primary phone number is determined
                                     RANK() OVER (ORDER BY
                                                   DECODE(ccv.cms_name,
                                                      c_phone_type_name_hom, '001',
                                                      c_phone_type_name_bus, '002',
                                                      c_phone_type_name_mob, '003',
                                                      c_phone_type_name_oth, '004',
                                                      c_phone_type_name_fax, '005',
                                                      ccv.cms_name
                                                   )
                                                 ) AS is_primary_seq
                                FROM vw_cms_code_value ccv
                               WHERE ccv.asset_code = 'picklist.phonetype.items'
                                 AND ccv.locale     = c_cms_locale
                                 AND ccv.cms_status = c_cms_status
                            ) p
                      WHERE s.import_file_id = p_import_file_id
                        AND s.user_name = au.user_name
                         -- skip records marked as erred
                        AND s.import_record_id NOT IN
                            ( SELECT e.import_record_id
                                FROM import_record_error e
                               WHERE e.import_file_id = p_import_file_id
                            )
                   ) stg_p,
                   user_phone up
                -- outer join stage records to any existing primary records
             WHERE stg_p.user_id = up.user_id (+)
               AND stg_p.phone_type = up.phone_type (+)
               AND 1 = up.is_primary (+)
                -- exclude records with no associated phone numbers
               AND NVL(stg_p.phone_nbr, up.phone_nbr) IS NOT NULL
         ) s
      ON (   d.user_id = s.user_id
         AND d.phone_type = s.phone_type
         )
    WHEN MATCHED THEN
      UPDATE
         SET d.phone_nbr      = CASE WHEN is_txtmsg_avlbl > 0       -- 06/08/2016 Case statement added for Bug 67002 fix
                                THEN d.phone_nbr
                                ELSE s.phone_nbr
                                END,
             d.is_primary     = s.is_primary,
             d.country_phone_code     = LOWER(s.country_code), --07/23/2013 
             -- track update
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
          -- only update if data differs
       WHERE s.phone_nbr IS NOT NULL AND NOT (   d.phone_nbr  = s.phone_nbr
                 AND d.is_primary = s.is_primary
                 )               
    WHEN NOT MATCHED THEN
      INSERT
      (  user_phone_id,
         user_id,
         phone_type,
         phone_nbr,
         phone_ext,
         is_primary,
         country_phone_code,
         created_by,
         date_created,
         modified_by,
         date_modified,
         version,
         roster_phone_id  -- 11/28/2019
      )
      VALUES
      (  user_phone_pk_sq.NEXTVAL, -- user_phone_id,
         s.user_id,
         s.phone_type,
         s.phone_nbr,
         NULL,          -- phone_ext,
         s.is_primary,
         LOWER(s.country_code), --07/23/2013
         g_created_by,  -- created_by,
         g_timestamp,   -- date_created,
         NULL,          -- modified_by,
         NULL,          -- date_modified,
         0,              -- version
         fnc_randomuuid  -- 11/28/2019
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_user_phone;

-- imports user node from the stage table
PROCEDURE p_imp_user_node
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_user_node');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- import staged records
   v_msg := 'MERGE user_node';
   MERGE INTO user_node d
   USING (  -- get staged data pivoting node columns into rows
            SELECT au.user_id,
                   s.user_name,
                   DECODE(s.termination_date,
                     NULL, decode(au.is_active,1,s.active,0),--s.active,  --07/05/2019 Bug#79083
                     0  -- inactive user when termination date not null
                   ) AS status,
                   DECODE(p.column_value,1, 1, 0 ) AS is_primary,  --08/03/2013
                   DECODE(p.column_value,
                     1, s.node_id1,
                     2, s.node_id2,
                     3, s.node_id3,
                     4, s.node_id4,
                     5, s.node_id5
                   ) AS node_id,
                   DECODE(p.column_value,
                     1, s.node_relationship1,
                     2, s.node_relationship2,
                     3, s.node_relationship3,
                     4, s.node_relationship4,
                     5, s.node_relationship5
                   ) AS role
              FROM stage_pax_import_record s,
                   application_user au,
                   ( -- select a row for each node column
                      SELECT LEVEL AS column_value
                        FROM dual
                     CONNECT BY LEVEL <= 5
                   ) p
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
                -- ensure ID/relationship fields populated
               AND DECODE(p.column_value,
                     1, s.node_id1,
                     2, s.node_id2,
                     3, s.node_id3,
                     4, s.node_id4,
                     5, s.node_id5
                   ) IS NOT NULL
               AND DECODE(p.column_value,
                     1, s.node_relationship1,
                     2, s.node_relationship2,
                     3, s.node_relationship3,
                     4, s.node_relationship4,
                     5, s.node_relationship5
                   ) IS NOT NULL
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
        /*    UNION -- Bug # 51556 commented to avoid more than one returned by single query
            -- inactive all existing nodes when user terminated
            SELECT au.user_id,
                   s.user_name,
                   0 AS status,  -- inactive
                   un.is_primary,                   --08/03/2013
                   un.node_id,
                   un.role
              FROM stage_pax_import_record s,
                   application_user au,
                   user_node un
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
                -- user is terminated
               AND s.termination_date IS NOT NULL
               AND au.user_id = un.user_id
                -- node is currently active
               AND un.status = 1
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )*/
         ) s
      ON (   d.user_id = s.user_id
         AND d.node_id = s.node_id
         )
    WHEN MATCHED THEN
      UPDATE
         SET d.status         = s.status,
             d.role           = s.role,
             -- track update
             d.is_primary     = s.is_primary,  --08/03/2013
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
          -- only update if data differs
       WHERE --NOT (   d.status = s.status --08/07/2014
                 --AND d.role   = s.role --08/07/2014
                (   DECODE(d.status,         s.status,       1, 0) = 0 --08/07/2014
                 OR DECODE(d.role,           s.role,         1, 0) = 0 --08/07/2014
                 OR DECODE(d.is_primary,     s.is_primary,   1, 0) = 0 --08/07/2014
                 )
    WHEN NOT MATCHED THEN
      INSERT
      (  user_node_id,
         user_id,
         node_id,
         status,
         role,
         is_primary,               --08/03/2013
         created_by,
         date_created,
         modified_by,
         date_modified,
         version
      )
      VALUES
      (  user_node_pk_sq.NEXTVAL, -- user_node_id,
         s.user_id,
         s.node_id,
         s.status,
         s.role,
         s.is_primary,             --08/03/2013
         g_created_by,  -- created_by,
         g_timestamp,   -- date_created,
         NULL,          -- modified_by,
         NULL,          -- date_modified,
         0              -- version
      );
--Bug # 51556 Added termination logic in a separate query
    MERGE INTO user_node d
    USING ( -- inactive all existing nodes when user terminated
            SELECT au.user_id,
                   s.user_name,
                   0 AS status,  -- inactive
                   un.is_primary,                   --08/03/2013
                   un.node_id,
                   un.role
              FROM stage_pax_import_record s,
                   application_user au,
                   user_node un
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
                -- user is terminated
               AND s.termination_date IS NOT NULL
               AND au.user_id = un.user_id
                -- node is currently active
               AND un.status = 1
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
         ) s
      ON (   d.user_id = s.user_id
         AND d.node_id = s.node_id
         )
    WHEN MATCHED THEN
      UPDATE
         SET d.status         = s.status,
             d.role           = s.role,
             -- track update
             d.is_primary     = s.is_primary,  --08/03/2013
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
          -- only update if data differs
       WHERE NOT (   d.status = s.status
                 AND d.role   = s.role
                 )
    WHEN NOT MATCHED THEN
      INSERT
      (  user_node_id,
         user_id,
         node_id,
         status,
         role,
         is_primary,               --08/03/2013
         created_by,
         date_created,
         modified_by,
         date_modified,
         version
      )
      VALUES
      (  user_node_pk_sq.NEXTVAL, -- user_node_id,
         s.user_id,
         s.node_id,
         s.status,
         s.role,
         s.is_primary,             --08/03/2013
         g_created_by,  -- created_by,
         g_timestamp,   -- date_created,
         NULL,          -- modified_by,
         NULL,          -- date_modified,
         0              -- version
      );
      
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_user_node;

-- imports user characteristic from the stage table
PROCEDURE p_imp_user_characteristic
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_user_characteristic');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- import staged records
   v_msg := 'MERGE user_characteristic';
   MERGE INTO user_characteristic d
   USING (  -- get staged data pivoting characteristic columns into rows
       SELECT s.user_id,  --Added Select 09/26/2013 
              s.user_name,
              s.field_position,
              s.characteristic_id,
              DECODE(c.characteristic_data_type,'multi_select',REPLACE(s.characteristic_value,'~',','),s.characteristic_value) characteristic_value
         FROM (
            SELECT au.user_id,
                   s.user_name,
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
                     10, s.characteristic_id10,
                     11, s.characteristic_id11,
                     12, s.characteristic_id12,
                     13, s.characteristic_id13,
                     14, s.characteristic_id14,
                     15, s.characteristic_id15,
                     16, s.characteristic_id16, --10/30/2015 Start
                     17, s.characteristic_id17,
                     18, s.characteristic_id18,
                     19, s.characteristic_id19,
                     20, s.characteristic_id20  --10/30/2015 End
                   ) AS characteristic_id,
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
                     10, s.characteristic_value10,
                     11, s.characteristic_value11,
                     12, s.characteristic_value12,
                     13, s.characteristic_value13,
                     14, s.characteristic_value14,
                     15, s.characteristic_value15,
                     16, s.characteristic_value16,  --10/30/2015 Start
                     17, s.characteristic_value17,
                     18, s.characteristic_value18,
                     19, s.characteristic_value19,
                     20, s.characteristic_value20   --10/30/2015 End
                   ) AS characteristic_value
              FROM stage_pax_import_record s,
                   application_user au,
                   ( -- select a row for each characteristic column
                      SELECT LEVEL AS column_value
                        FROM dual
                     CONNECT BY LEVEL <= 20 --15 --10/30/2015
                   ) p
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
                -- ensure ID/value fields populated
               AND DECODE(p.column_value,
                     1, s.characteristic_id1,
                     2, s.characteristic_id2,
                     3, s.characteristic_id3,
                     4, s.characteristic_id4,
                     5, s.characteristic_id5,
                     6, s.characteristic_id6,
                     7, s.characteristic_id7,
                     8, s.characteristic_id8,
                     9, s.characteristic_id9,
                     10, s.characteristic_id10,
                     11, s.characteristic_id11,
                     12, s.characteristic_id12,
                     13, s.characteristic_id13,
                     14, s.characteristic_id14,
                     15, s.characteristic_id15, --10/30/2015 Start
                     16, s.characteristic_id16,
                     17, s.characteristic_id17,
                     18, s.characteristic_id18,
                     19, s.characteristic_id19,
                     20, s.characteristic_id20  --10/30/2015 End
                   ) IS NOT NULL
               AND DECODE(p.column_value,
                     1, s.characteristic_value1,
                     2, s.characteristic_value2,
                     3, s.characteristic_value3,
                     4, s.characteristic_value4,
                     5, s.characteristic_value5,
                     6, s.characteristic_value6,
                     7, s.characteristic_value7,
                     8, s.characteristic_value8,
                     9, s.characteristic_value9,
                     10, s.characteristic_value10,
                     11, s.characteristic_value11,
                     12, s.characteristic_value12,
                     13, s.characteristic_value13,
                     14, s.characteristic_value14,
                     15, s.characteristic_value15, --10/30/2015 Start
                     16, s.characteristic_value16,
                     17, s.characteristic_value17,
                     18, s.characteristic_value18,
                     19, s.characteristic_value19,
                     20, s.characteristic_value20   --10/30/2015 End
                   ) IS NOT NULL
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
            ) s,
            characteristic c --09/26/2013
      WHERE s.characteristic_id = c.characteristic_id (+) --09/26/2013
      ORDER BY field_position ASC   --03/27/2014 Bug # 52052
         ) s
      ON (   d.user_id = s.user_id
         AND d.characteristic_id = s.characteristic_id
         )
    WHEN MATCHED THEN
      UPDATE
         SET d.characteristic_value = s.characteristic_value,
             -- track update
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
          -- only update if data differs
       WHERE NOT (d.characteristic_value = s.characteristic_value)
    WHEN NOT MATCHED THEN
      INSERT
      (  user_characteristic_id,
         user_id,
         characteristic_id,
         characteristic_value,
         created_by,
         date_created,
         modified_by,
         date_modified,
         version,
         roster_user_char_id -- 11/28/2019
      )
      VALUES
      (  user_characteristic_pk_sq.NEXTVAL, -- user_characteristic_id,
         s.user_id,
         s.characteristic_id,
         s.characteristic_value,
         g_created_by,  -- created_by,
         g_timestamp,   -- date_created,
         NULL,          -- modified_by,
         NULL,          -- date_modified,
         0,              -- version
         fnc_randomuuid -- 11/28/2019
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

   DELETE FROM user_characteristic
   where characteristic_value ='DELETE'; --07/23/2013

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_user_characteristic;

-- imports user role from the stage table
PROCEDURE p_imp_user_role
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_user_role');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- import staged records
   v_msg := 'MERGE user_role';
   MERGE INTO user_role d
   USING (  -- get staged data pivoting role columns into rows
            SELECT au.user_id,
                   s.user_name,
                   s.active AS status,
                   p.column_value AS field_position,
                   DECODE(p.column_value,
                     1, s.role_id1,
                     2, s.role_id2,
                     3, s.role_id3,
                     4, s.role_id4,
                     5, s.role_id5
                   ) AS role_id
              FROM stage_pax_import_record s,
                   application_user au,
                   ( -- select a row for each role column
                      SELECT LEVEL AS column_value
                        FROM dual
                     CONNECT BY LEVEL <= 5
                   ) p
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
                -- ensure ID fields populated
               AND DECODE(p.column_value,
                     1, s.role_id1,
                     2, s.role_id2,
                     3, s.role_id3,
                     4, s.role_id4,
                     5, s.role_id5
                   ) IS NOT NULL
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
         ) s
      ON (   d.user_id = s.user_id
         AND d.role_id = s.role_id
         )
    WHEN NOT MATCHED THEN
      INSERT
      (  user_id,
         role_id,
         created_by,
         date_created
      )
      VALUES
      (  s.user_id,
         s.role_id,
         g_created_by,  -- created_by,
         g_timestamp    -- date_created
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_user_role;

-- imports new CMS data from the stage table
PROCEDURE p_imp_cms
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_cms');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   -- Bug 19620, value place in a constant so it can be used in the FORALL statement
   c_default_aud_id     cms_audience.id%TYPE := f_beacon_default_aud_id;

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
   v_default_lang       os_propertyset.string_val%TYPE := fnc_get_system_variable('default.language','Default Language');

   v_tab_asset_id       dbms_sql.NUMBER_table;
   v_tab_content_key_id dbms_sql.NUMBER_table;
   v_tab_content_id     dbms_sql.NUMBER_table;
   v_tab_sort_order     dbms_sql.NUMBER_table;
   v_tab_cms_code       dbms_sql.VARCHAR2_table;

   CURSOR cur_new_cms
   ( cv_import_file_id  stage_pax_import_record.import_file_id%TYPE
   ) IS
   WITH stg_rec AS
   (  -- get stage records
      SELECT s.*
        FROM stage_pax_import_record s
       WHERE s.import_file_id = cv_import_file_id
          -- skip records marked as erred
         AND s.import_record_id NOT IN
             ( SELECT e.import_record_id
                 FROM import_record_error e
                WHERE e.import_file_id = cv_import_file_id
             )
   ), cms_parent_key AS
   (  -- get key fields from parent CMS tables
      SELECT a.id AS asset_id,
             a.code AS asset_code,
             MAX(ck.sort_order) AS max_sort_order
        FROM cms_asset a,
             cms_content_key ck
       WHERE a.code IN ('picklist.positiontype.items',
                        'picklist.department.type.items','picklist.suffixtype.items' --03/27/2014 bug # 52223
                       )
         AND a.id = ck.asset_id
       GROUP BY a.id,
             a.code
   )
   SELECT stg_cms.asset_id,
          -- calculate new sort order starting from the current max sort order
          ( stg_cms.max_sort_order
          + ROW_NUMBER() OVER (PARTITION BY stg_cms.asset_id ORDER BY stg_cms.cms_code)
          ) AS sort_order,
          stg_cms.cms_code
     FROM ( -- associate staged CMS fields to database CMS values
            SELECT cpk.asset_id,
                   cpk.max_sort_order,
                   ccv.content_id,
                   sc.*
              FROM cms_parent_key cpk,
                   ( -- get stage CMS fields
                     SELECT DISTINCT
                            'picklist.positiontype.items' AS asset_code,
                            sr.job_position AS cms_code
                       FROM stg_rec sr
                      WHERE sr.job_position IS NOT NULL
                      UNION ALL
                     SELECT DISTINCT
                            'picklist.department.type.items' AS asset_code,
                            sr.department AS cms_code
                       FROM stg_rec sr
                      WHERE sr.department IS NOT NULL
                      UNION ALL
                     SELECT DISTINCT
                            'picklist.suffixtype.items' AS asset_code, --03/27/2014 bug # 52223
                            sr.suffix AS cms_code
                       FROM stg_rec sr
                      WHERE sr.suffix IS NOT NULL
                   ) sc,
                   vw_cms_asset_value ccv
                -- inner join import fields to CMS parent key
             WHERE cpk.asset_code = sc.asset_code
                -- outer join import fields to CMS code values
               AND ccv.asset_code (+) = sc.asset_code
               AND ccv.locale     (+) = c_cms_locale
               AND ccv.key (+) = 'CODE'
               AND LOWER(ccv.cms_value (+)) = LOWER(sc.cms_code)
          ) stg_cms
       -- staged data not found in CMS content
    WHERE stg_cms.content_id IS NULL
      ;

   -- cursor returns the specified number of CMS primary key IDs
   CURSOR cur_next_cms_id
   ( cv_rec_cnt   INTEGER
   ) IS
   SELECT cms_generic_pk_sq.NEXTVAL
     FROM dual
  CONNECT BY LEVEL <= cv_rec_cnt
      ;
      
      --cursor 
   /*CURSOR c1 is --10/06/2015 
   SELECT  to_char(substr(cd.VALUE, 1, 4000)) AS locale  FROM cms_content_data cd,
       cms_content cc,
       cms_content_key ck,
       cms_asset_type caa,
       cms_asset ca, cms_section s, CMS_APPLICATION aa,cms_content_data ccd
WHERE 
    cd.content_id = cc.ID
   AND cc.content_key_id = ck.ID
   AND cc.content_status = 'Live'
   AND ck.asset_id = ca.ID
   AND ca.asset_type_id = caa.ID
   AND cc.locale ='en_US'
   AND cd.key like  '%CODE%'
   AND CD.CONTENT_ID = CCD.CONTENT_ID
   AND ccd.key like  '%STATUS%'
  AND to_char(substr(ccd.VALUE, 1, 4000)) ='true'
  AND ca.code like 'default.locale.items'
  AND CA.SECTION_ID = S.ID
  AND S.APPLICATION_ID = aa.id
  and aa.code = 'beacon'
  order by ck.id;*/
  
BEGIN
   -- get any import values not currently stored in the CMS tables
   v_msg := 'OPEN cur_new_cms';
   OPEN cur_new_cms(p_import_file_id);
   v_msg := 'FETCH cur_new_cms';
   FETCH cur_new_cms
    BULK COLLECT
    INTO v_tab_asset_id,
         v_tab_sort_order,
         v_tab_cms_code;
   CLOSE cur_new_cms;

   v_rec_cnt := v_tab_asset_id.COUNT;

   -- ensure new CMS data found
   IF (v_rec_cnt > 0) THEN
      -- get CMS content key IDs
      OPEN cur_next_cms_id(v_rec_cnt);
      FETCH cur_next_cms_id
       BULK COLLECT
       INTO v_tab_content_key_id;
      CLOSE cur_next_cms_id;

--      -- get CMS content IDs
--      OPEN cur_next_cms_id(v_rec_cnt);
--      FETCH cur_next_cms_id
--       BULK COLLECT
--       INTO v_tab_content_id;
--      CLOSE cur_next_cms_id;

      -- add content key records
      v_msg := 'INSERT cms_content_key';
      FORALL indx IN v_tab_asset_id.FIRST .. v_tab_asset_id.LAST
         INSERT INTO cms_content_key
         (  id,
            entity_version,
            sort_order,
            guid,
            asset_id,
            created_by,
            date_created
         )
         VALUES
         (  v_tab_content_key_id(indx),
            0,             -- entity_version
            v_tab_sort_order(indx),
            SYS_GUID(),    -- guid
            v_tab_asset_id(indx),
            g_created_by,  -- created_by,
            g_timestamp    -- date_created
         );
         
      --FOR C1_R IN C1 LOOP -- This is for inserting department and job_position rows for all the installed languages. --10/06/2015
             
          -- get CMS content IDs
      OPEN cur_next_cms_id(v_rec_cnt);
      FETCH cur_next_cms_id
       BULK COLLECT
       INTO v_tab_content_id;
      CLOSE cur_next_cms_id;

      -- add content records
      v_msg := 'INSERT cms_content';
      FORALL indx IN v_tab_asset_id.FIRST .. v_tab_asset_id.LAST
         INSERT INTO cms_content
         (  id,
            entity_version,
            locale,
            version,
            content_status,
            added_by,
            added_timestamp,
            guid,
            content_version,
            content_key_id,
            created_by,
            date_created
         )
         VALUES
         (  v_tab_content_id(indx),
            0,             -- entity_version
            DECODE(v_default_lang,'ERROR','en_US',v_default_lang),--c1_r.locale, --10/06/2015 added default locale 
            1,             -- version
            c_cms_content_status_live,
            g_created_by,  -- added_by,
            g_timestamp,   -- added_timestamp,
            SYS_GUID(),    -- guid
            1,
            v_tab_content_key_id(indx),
            g_created_by,  -- created_by,
            g_timestamp    -- date_created
         );

      -- add content data records
      v_msg := 'INSERT cms_content_data';
      FORALL indx IN v_tab_asset_id.FIRST .. v_tab_asset_id.LAST
         INSERT INTO cms_content_data
         (  content_id,
            key,
            value
         )
         (  -- select a row for each key field value
            SELECT v_tab_content_id(indx),
                   DECODE(LEVEL,
                     1, 'NAME',
                     2, 'DESC',
                     3, 'CODE',
                     4, 'STATUS',
                     5, 'ABBR'
                   ) AS key,
                   DECODE(LEVEL,
                     1, v_tab_cms_code(indx),
                     2, v_tab_cms_code(indx),
                     3, v_tab_cms_code(indx),
                     4, c_cms_status,
                     5, NULL
                   ) AS value
              FROM dual
           CONNECT BY LEVEL <= 5
         );
        --END LOOP; --10/06/2015
      -- Bug 19620 Insert Default Audience Record
      v_msg := 'INSERT cms_content_key_audience_lnk';
      FORALL indx IN v_tab_asset_id.FIRST .. v_tab_asset_id.LAST
         INSERT INTO cms_content_key_audience_lnk
         (  content_key_id,
            audience_id
         )
         VALUES
         (  v_tab_content_key_id(indx),
            c_default_aud_id
         );
      
   END IF;

   v_msg := 'INSERT new CMS data';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_cms;

-- imports employer from the stage table
PROCEDURE p_imp_employer
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_employer');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- import staged records
   v_msg := 'MERGE employer';
   MERGE INTO employer d
   USING (  -- get stage records
            SELECT DISTINCT
                   s.employer_name AS name
              FROM stage_pax_import_record s
             WHERE s.import_file_id = p_import_file_id
               AND s.employer_name IS NOT NULL
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
         ) s
      ON (UPPER(d.name) = UPPER(s.name))
    WHEN NOT MATCHED THEN
      INSERT
      (  employer_id,
         name,
         is_active,
         status_reason,
         federal_tax_id,
         state_tax_id,
         country_id,
         addr1,
         addr2,
         addr3,
         addr4,
         addr5,
         addr6,
         city,
         state,
         postal_code,
         phone_nbr,
         created_by,
         date_created,
         version
      )
      VALUES
      (  employer_pk_sq.NEXTVAL, -- employer_id,
         s.name,
         1,             -- is_active,
         NULL,          -- status_reason,
         NULL,          -- federal_tax_id,
         NULL,          -- state_tax_id,
         NULL,          -- country_id,
         NULL,          -- addr1,
         NULL,          -- addr2,
         NULL,          -- addr3,
         NULL,          -- addr4,
         NULL,          -- addr5,
         NULL,          -- addr6,
         NULL,          -- city,
         NULL,          -- state,
         NULL,          -- postal_code,
         NULL,          -- phone_nbr,
         g_created_by,  -- created_by,
         g_timestamp,   -- date_created
         0              -- version
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_employer;

-- imports participant employer from the stage table
PROCEDURE p_imp_participant_employer
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_imp_participant_employer');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN

  -- 10/11/2019 start
  v_msg := 'populate temp_table_session for department and position';
    DELETE temp_table_session;
    INSERT INTO temp_table_session
    SELECT asset_code, cms_name, cms_code
        FROM vw_cms_code_value E
     WHERE asset_code IN ('picklist.department.type.items',
                                                'picklist.positiontype.items')
         AND e.locale = c_cms_locale;
  -- 10/11/2019 end
           
   -- import staged records
   v_msg := 'MERGE participant_employer';
   MERGE INTO participant_employer d
   USING (  -- get stage records
            WITH stg_rec AS
            (  -- get stage records
               SELECT au.user_id,
                      ( -- increment current max value for an insert record
                        SELECT NVL(MAX(pe.participant_employer_index)+1, 0)
                          FROM participant_employer pe
                         WHERE s.user_id = pe.user_id
                      ) AS next_pax_emp_index,
                      s.user_name,
                      s.employer_name,
                      e.employer_id,
--                      s.job_position AS position_type, -- 10/11/2019
                      pl_job.position_type,              -- 10/11/2019 case-sensitive CM code
--                      s.department   AS department_type, -- 10/11/2019
                      pl_dept.department_type,             -- 10/11/2019 case-sensitive CM code
                      NVL(s.hire_date,(SELECT pe.hire_date           --08/10/2016
                          FROM vw_curr_pax_employer pe
                         WHERE s.user_id = pe.user_id))  hire_date,                        
                       /*NVL((SELECT pe.hire_date           --08/10/2016
                          FROM vw_curr_pax_employer pe
                         WHERE s.user_id = pe.user_id),s.hire_date) as hire_date,*/
                      s.termination_date
                 FROM stage_pax_import_record s,
                      application_user au,
                      employer E,
                      (SELECT cms_code position_type -- 10/11/2019
                       FROM   temp_table_session
                       WHERE  asset_code = 'picklist.positiontype.items') pl_job,
                      (SELECT cms_code department_type -- 10/11/2019
                       FROM   temp_table_session
                       WHERE  asset_code = 'picklist.department.type.items') pl_dept
                WHERE s.import_file_id = p_import_file_id
                  AND s.user_name = au.user_name
                  AND UPPER(s.employer_name) = UPPER(e.name)
                  AND UPPER(s.job_position) = UPPER(pl_job.position_type) -- 10/11/2019 case-insensitive compare
                  AND UPPER(s.department) = UPPER(pl_dept.department_type) -- 10/11/2019 case-insensitive compare
                   -- skip records marked as erred
                  AND s.import_record_id NOT IN
                      ( SELECT e.import_record_id
                          FROM import_record_error e
                         WHERE e.import_file_id = p_import_file_id
                      ))
SELECT sr.user_id,
                   NVL(pe.participant_employer_index, sr.next_pax_emp_index) AS participant_employer_index,
                   sr.employer_id,
                   sr.position_type,
                   sr.department_type,
                   sr.hire_date hire_date,     --08/10/2016
                   --NVL(pe.hire_date,sr.hire_date) hire_date,   --08/10/2016
                   sr.termination_date
              FROM stg_rec sr,
                   participant_employer pe
                -- outer join so staged data can be inserted
             WHERE sr.user_id = pe.user_id (+)
               AND sr.employer_id = pe.employer_id (+)
                -- decode handles null comparison
              AND DECODE(sr.position_type,    pe.position_type (+),    1, 0) = 1
              AND DECODE(sr.department_type,  pe.department_type (+),  1, 0) = 1
              AND DECODE(sr.termination_date, NVL(sr.termination_date,pe.termination_date (+)), 1, 0) = 1    --added NVL 02/03/2016  
              AND DECODE(sr.hire_date,        pe.hire_date (+),        1, 0) = 1  --08/10/2016
             UNION
            -- teminate current active employment record when does not match staged data
            SELECT sr.user_id,
                   cpe.participant_employer_index,
                   cpe.employer_id,
                   cpe.position_type,
                   cpe.department_type,
                   cpe.hire_date,
                   NVL(sr.termination_date,TRUNC(SYSDATE)) AS termination_date --added NVL 02/03/2016
              FROM stg_rec sr,
                   vw_curr_pax_employer cpe
             WHERE sr.user_id = cpe.user_id
               AND cpe.termination_date IS NULL
                -- current employment not the staged value
               AND NOT (   sr.employer_id = cpe.employer_id
                        -- decode handles null comparison
                       AND DECODE(sr.position_type,   cpe.position_type,    1, 0) = 1
                       AND DECODE(sr.department_type, cpe.department_type,  1, 0) = 1
                       AND DECODE(sr.termination_date, cpe.termination_date,  1, 0) = 1
                       AND DECODE(sr.hire_date,       cpe.hire_date,        1, 0) = 1  --08/10/2016
                       )
             UNION
            -- teminate employment records when changing employers
            SELECT sr.user_id,
                   pe.participant_employer_index,
                   pe.employer_id,
                   pe.position_type,
                   pe.department_type,
                   pe.hire_date,
                   --TRUNC(SYSDATE) AS termination_date -- 01/25/2019
                   NVL(sr.termination_date,TRUNC(SYSDATE)) AS termination_date -- 01/25/2019
              FROM stg_rec sr,
                   participant_employer pe
             WHERE sr.user_id = pe.user_id
               AND pe.termination_date IS NULL
                -- employer not the staged value
               AND sr.employer_id != pe.employer_id
         ) s
      ON (   d.user_id = s.user_id
         AND d.participant_employer_index = s.participant_employer_index
         )
    WHEN MATCHED THEN
      UPDATE
         SET --d.hire_date        = NVL(d.hire_date, NVL(s.hire_date, TRUNC(SYSDATE))), --08/10/2016
             d.termination_date = s.termination_date,
             -- track update
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp
          -- only update if data differs
       WHERE NOT (   --DECODE(d.hire_date, NVL(s.hire_date, NVL(d.hire_date, TRUNC(SYSDATE))), 1, 0) = 1
                  DECODE(d.termination_date, s.termination_date, 1, 0) = 1
                 )
    WHEN NOT MATCHED THEN
      INSERT
      (  user_id,
         participant_employer_index,
         employer_id,
         position_type,
         department_type,
         hire_date,
         termination_date,
         created_by,
         date_created,
         modified_by,
         date_modified
      )
      VALUES
      (  s.user_id,
         s.participant_employer_index,
         s.employer_id,
         s.position_type,
         s.department_type,
         s.hire_date,
         s.termination_date,
         g_created_by,  -- created_by,
         g_timestamp,   -- date_created,
         NULL,          -- modified_by,
         NULL           -- date_modified,
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_participant_employer;

-- imports participant employer from the stage table
PROCEDURE p_imp_pax_comm_pref
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name          CONSTANT execution_log.process_name%type := UPPER('p_imp_pax_comm_pref');
   c_release_level         CONSTANT execution_log.release_level%type := '2.0';
   c_pax_allow_estatements CONSTANT os_propertyset.boolean_val%TYPE := fnc_get_osp_bool('participant.allow.estatements');

   v_msg                   execution_log.text_line%TYPE;
   v_rec_cnt               NUMBER := 0;
   v_comm_code_estatement  vw_cms_code_value.cms_code%TYPE;

   -- gets the communication code used for eStatements
   CURSOR cur_pax_comm_pref_estatement IS
   SELECT ccv.cms_code AS comm_code_estatement
     FROM vw_cms_code_value ccv
    WHERE ccv.asset_code = 'picklist.participant.preference.communications.items'
      AND ccv.locale     = c_cms_locale
      AND ccv.cms_status = c_cms_status
      AND ccv.cms_name   = c_pax_comm_pref_name_estmt
      ;

BEGIN
   v_msg := 'Allow eStatements >' || c_pax_allow_estatements || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   -- only process records when eStatements allowed
   IF (c_pax_allow_estatements = 1) THEN

      -- initialize variables
      v_msg := 'OPEN cur_pax_comm_pref_estatement';
      OPEN cur_pax_comm_pref_estatement;
      v_msg := 'OPEN cur_pax_comm_pref_estatement';
      FETCH cur_pax_comm_pref_estatement INTO v_comm_code_estatement;
      CLOSE cur_pax_comm_pref_estatement;

      -- import staged records
      v_msg := 'MERGE participant_comm_preference (comm code >' || v_comm_code_estatement || '<)';
      MERGE INTO participant_comm_preference d
      USING (  -- get stage records
               SELECT au.user_id,
                      s.user_name,
                      v_comm_code_estatement AS communication_code,
                      s.country_code,
                      c.supplier_id,
                      sup.supplier_name
                 FROM stage_pax_import_record s,
                      application_user au,
                      country c,
                      country_suppliers cs, -- 04/03/2014 Bug # 52579 
                      supplier sup
                WHERE s.import_file_id = p_import_file_id
                  AND s.user_name = au.user_name
                  AND LOWER(s.country_code) = c.country_code
                  AND c.country_id = cs.country_id -- 04/03/2014 Bug # 52579 
                  AND cs.supplier_id = sup.supplier_id -- 04/03/2014 Bug # 52579 
                  --AND c.supplier_id = sup.supplier_id
                   -- estatements only used when BI Bank is the country supplier
                  AND UPPER(sup.supplier_name) = 'BI BANK'
                   -- skip records marked as erred
                  AND s.import_record_id NOT IN
                      ( SELECT e.import_record_id
                          FROM import_record_error e
                         WHERE e.import_file_id = p_import_file_id
                      )
            ) s
         ON (   d.user_id = s.user_id
            AND d.communication_code = s.communication_code
            )
       WHEN NOT MATCHED THEN
         INSERT
         (  participant_comm_preference_id,
            user_id,
            communication_code,
            created_by,
            date_created)
         VALUES
         (  pax_comm_preference_pk_sq.NEXTVAL,  -- participant_comm_preference_id,
            s.user_id,
            s.communication_code,
            g_created_by,  -- created_by,
            g_timestamp    -- date_created,
         );

      v_rec_cnt := SQL%ROWCOUNT;
      prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);
   END IF; -- allow eStatements

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_imp_pax_comm_pref;

-- remove records with no associated stage data
PROCEDURE p_remove_obsolete
( p_import_file_id  IN  stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_remove_obsolete');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';
   c_user_role_delete   CONSTANT os_propertyset.boolean_val%TYPE := fnc_get_osp_bool('fileload.userrole.delete');

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
--   -- remove obsolete characteristics  --We dont need this in G5 as the characteristics to be removed will come with a value of 'DELETE'
--   v_msg := 'DELETE user_characteristic';
--   DELETE user_characteristic
--    WHERE ROWID IN
--          ( -- find database records not associated with the staged data
--            SELECT uc.ROWID
--              FROM stage_pax_import_record s,
--                   application_user au,
--                   user_characteristic uc
--             WHERE s.import_file_id = p_import_file_id
--               AND s.user_name = au.user_name
--               AND au.user_id = uc.user_id
--                -- find database records not associated with the staged data
--               AND DECODE(uc.characteristic_id, s.characteristic_id1, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id2, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id3, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id4, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id5, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id6, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id7, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id8, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id9, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id10, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id11, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id12, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id13, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id14, 1, 0) = 0
--               AND DECODE(uc.characteristic_id, s.characteristic_id15, 1, 0) = 0
--                -- skip records marked as erred
--               AND s.import_record_id NOT IN
--                   ( SELECT e.import_record_id
--                       FROM import_record_error e
--                      WHERE e.import_file_id = p_import_file_id
--                   )
--          );
--
--   v_rec_cnt := SQL%ROWCOUNT;
--   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

   -- archive obsolete nodes
   v_msg := 'INSERT user_node_history';
   INSERT INTO user_node_history
   (  user_node_history_id,
      user_id,
      node_id,
      role,
      status,
      created_by,
      date_created
   )
   (  -- get obsolete records
      SELECT user_node_history_pk_sq.NEXTVAL AS user_node_history_id,
             obs.user_id,
             obs.node_id,
             obs.role,
             -- mark status as inactive
             0 AS status,
             g_created_by AS created_by,
             g_timestamp AS date_created
        FROM ( -- find database records not associated with the staged data
               SELECT un.user_id,
                      un.node_id,
                      un.role
                 FROM stage_pax_import_record s,
                      application_user au,
                      user_node un
                WHERE s.import_file_id = p_import_file_id
                  AND s.user_name = au.user_name
                  AND au.user_id = un.user_id
                  AND DECODE(un.node_id, s.node_id1, 1, 0) = 0
                  AND DECODE(un.node_id, s.node_id2, 1, 0) = 0
                  AND DECODE(un.node_id, s.node_id3, 1, 0) = 0
                  AND DECODE(un.node_id, s.node_id4, 1, 0) = 0
                  AND DECODE(un.node_id, s.node_id5, 1, 0) = 0
                   -- skip records marked as erred
                  AND s.import_record_id NOT IN
                      ( SELECT e.import_record_id
                          FROM import_record_error e
                         WHERE e.import_file_id = p_import_file_id
                      )
                UNION
               -- find nodes inactivated by the import process
               SELECT un.user_id,
                      un.node_id,
                      un.role
                 FROM stage_pax_import_record s,
                      application_user au,
                      user_node un
                WHERE s.import_file_id = p_import_file_id
                  AND s.user_name = au.user_name
                  AND au.user_id = un.user_id
                  AND un.status = 0 -- inactive
                  AND (  un.created_by  = g_created_by
                      OR un.modified_by = g_created_by
                      )
                  AND (  un.date_created  = g_timestamp
                      OR un.date_modified = g_timestamp
                      )
                   -- skip records marked as erred
                  AND s.import_record_id NOT IN
                      ( SELECT e.import_record_id
                          FROM import_record_error e
                         WHERE e.import_file_id = p_import_file_id
                      )
             ) obs
   );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

   -- remove obsolete nodes
   v_msg := 'DELETE user_node';
   DELETE user_node
    WHERE ROWID IN
          ( -- find database records not associated with the staged data
            SELECT un.ROWID
              FROM stage_pax_import_record s,
                   application_user au,
                   user_node un
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
               AND au.user_id = un.user_id
               AND DECODE(un.node_id, s.node_id1, 1, 0) = 0
               AND DECODE(un.node_id, s.node_id2, 1, 0) = 0
               AND DECODE(un.node_id, s.node_id3, 1, 0) = 0
               AND DECODE(un.node_id, s.node_id4, 1, 0) = 0
               AND DECODE(un.node_id, s.node_id5, 1, 0) = 0
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
            -- no inactivated file nodes
            -- Bug #21679, Keep inactive nodes in the user_node table for reporting
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

   IF (c_user_role_delete = 1) THEN
      -- remove obsolete user roles
      v_msg := 'DELETE user_role';
      DELETE user_role
       WHERE ROWID IN
             ( -- find database records not associated with the staged data
               SELECT ur.ROWID
                 FROM stage_pax_import_record s,
                      application_user au,
                      user_role ur
                WHERE s.import_file_id = p_import_file_id
                  AND s.user_name = au.user_name
                  AND au.user_id = ur.user_id
                  AND DECODE(ur.role_id, s.role_id1, 1, 0) = 0
                  AND DECODE(ur.role_id, s.role_id2, 1, 0) = 0
                  AND DECODE(ur.role_id, s.role_id3, 1, 0) = 0
                  AND DECODE(ur.role_id, s.role_id4, 1, 0) = 0
                  AND DECODE(ur.role_id, s.role_id5, 1, 0) = 0
                   -- skip records marked as erred
                  AND s.import_record_id NOT IN
                      ( SELECT e.import_record_id
                          FROM import_record_error e
                         WHERE e.import_file_id = p_import_file_id
                      )
             );

      v_rec_cnt := SQL%ROWCOUNT;
      prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);
   END IF; -- user role delete

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_remove_obsolete;

-- marks a budget as closed when a user is terminated
PROCEDURE p_close_budget
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_close_budget');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN
   -- close terminated budgets
   v_msg := 'MERGE budget';
   MERGE INTO budget d
   USING (  -- get stage records
            SELECT b.budget_id
              FROM stage_pax_import_record s,
                   application_user au,
                   budget b
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
                -- user is terminated
               AND s.termination_date IS NOT NULL
               AND au.user_id = b.user_id
                -- budget is currently active
               AND b.status  = 'active'
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
         ) s
      ON (d.budget_id = s.budget_id)
    WHEN MATCHED THEN
      UPDATE
         SET d.status         = 'closed',
             -- track update
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1
      ;

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_close_budget;
PROCEDURE p_inactive_pax_process  --08/24/2017 G6-2870
( p_import_file_id  IN     stage_pax_import_record.import_file_id%TYPE
) IS
   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_inactive_pax_process');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            NUMBER;
BEGIN

   v_msg := 'Update PURL contributor state to expired for inactive recipient';
   
         MERGE INTO purl_contributor d
      USING (
            SELECT pc.*
              FROM stage_pax_import_record s,
                   application_user au,
                   purl_recipient pr,
                   purl_contributor pc
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
                -- user is terminated
               AND s.termination_date IS NOT NULL
               AND au.user_id = pr.user_id
               AND pr.purl_recipient_id = pc.purl_recipient_id
               AND pr.state IN ('invitation','contribution')
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
               ) s
               ON (d.purl_contributor_id = s.purl_contributor_id)
            WHEN MATCHED THEN   
             UPDATE SET
             d.state = 'expired',
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1;
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);
   
      v_msg := 'Update PURL recipient state to expired';
   
   MERGE INTO purl_recipient d
       USING (
            SELECT pr.*
              FROM stage_pax_import_record s,
                   application_user au,
                   purl_recipient pr
             WHERE s.import_file_id = p_import_file_id
               AND s.user_name = au.user_name
                -- user is terminated
               AND s.termination_date IS NOT NULL
               AND au.user_id = pr.user_id
               AND pr.state IN ('invitation','contribution')
                -- skip records marked as erred
               AND s.import_record_id NOT IN
                   ( SELECT e.import_record_id
                       FROM import_record_error e
                      WHERE e.import_file_id = p_import_file_id
                   )
               ) s
               ON (d.purl_recipient_id = s.purl_recipient_id)
            WHEN MATCHED THEN   
             UPDATE SET
             d.state = 'expired',
             d.modified_by    = g_created_by,
             d.date_modified  = g_timestamp,
             d.version        = d.version + 1;
             
       v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ', ' || v_rec_cnt || ' records', NULL);             

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END p_inactive_pax_process;  ----08/24/2017 G6-2870
---------------------
---------------------
-- public procecures
PROCEDURE p_participant_verify_load
( p_import_file_id  IN  NUMBER,
  p_load_type       IN  VARCHAR2,
  p_user_id         IN  NUMBER,
  p_hierarchy_id    IN  NUMBER,
  p_total_error_rec OUT NUMBER,  -- count of records with errors
  p_out_returncode  OUT NUMBER,
  p_out_user_data   OUT SYS_REFCURSOR
) IS
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name       CONSTANT execution_log.process_name%type := UPPER('p_participant_verify_load');
   c_release_level      CONSTANT execution_log.release_level%type := '2.0';

   v_msg                execution_log.text_line%TYPE;
   v_total_err_count    INTEGER; -- count of errors
   v_import_record_count   import_file.import_record_count%TYPE;
   v_country_change   NUMBER; --Bug # 50932
   v_out_returncode  NUMBER :=0; --Bug # 52457
   v_out_user_data SYS_REFCURSOR;

   CURSOR cur_import_file
   ( cv_import_file_id  import_record_error.import_file_id%TYPE
   ) IS
   SELECT f.import_record_count
     FROM import_file f
    WHERE f.import_file_id = cv_import_file_id
      ;

BEGIN
   v_msg := 'Start'
      || ': p_import_file_id >' || p_import_file_id
      || '<, p_load_type >'     || p_load_type
      || '<, p_hierarchy_id >'  || p_hierarchy_id
      || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   -- initialize variables
   g_created_by := p_user_id;
   g_timestamp := SYSDATE;
   p_out_returncode := 0;
   v_total_err_count := 0;
   
    v_msg := 'Find status of variable run.campaign.transfer.process'; --01/09/2014 Bug # 50932';
    BEGIN
    SELECT boolean_val 
      INTO v_country_change 
      FROM os_propertyset 
     WHERE lower(ENTITY_NAME) = 'run.campaign.transfer.process';
     EXCEPTION WHEN OTHERS THEN
     v_country_change :=0;    
    END;

   v_msg := 'OPEN cur_import_file';
   OPEN cur_import_file(p_import_file_id);
   v_msg := 'FETCH cur_import_file';
   FETCH cur_import_file INTO v_import_record_count;
   CLOSE cur_import_file;

   -- get count of any existing errors
   p_total_error_rec := f_get_error_record_count(p_import_file_id);
   v_msg := 'Previous error count >' || p_total_error_rec || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   IF (p_load_type = 'V') THEN     --added on 04/23/2010 
      -- populate node ID based on node name
      v_msg := 'Call p_set_stg_pax_import_node_id';
      p_set_stg_pax_import_node_id( p_import_file_id, p_hierarchy_id);
   END IF;

   -- report validation errors
   v_msg := 'Call p_rpt_dup_user_err';
   p_rpt_dup_user_err(p_import_file_id, v_total_err_count);

   v_msg := 'Call p_rpt_invalid_property_err';
   p_rpt_invalid_property_err(p_import_file_id, v_total_err_count);

   v_msg := 'Call p_rpt_required_field_err';
   p_rpt_required_field_err(p_import_file_id, v_total_err_count);

   v_msg := 'Call p_rpt_postal_code_err';
   p_rpt_postal_code_err(p_import_file_id, v_total_err_count);

   v_msg := 'Call p_rpt_cms_err';
   p_rpt_cms_err(p_import_file_id, v_total_err_count);
   
--   IF v_country_change=0 THEN --01/09/2014    Bug # 50932
--   v_msg := 'Call p_rpt_country_upd_err';                          Bug 55571
--   p_rpt_country_upd_err(p_import_file_id, v_total_err_count);
--   END IF;
   
   v_msg := 'Call p_rpt_user_characteristic_err';
   p_rpt_user_characteristic_err(p_import_file_id, v_total_err_count);

   v_msg := 'Call p_rpt_node_err';
   p_rpt_node_err(p_import_file_id, v_total_err_count);

   v_msg := 'Call p_rpt_misc_err';
   p_rpt_misc_err(p_import_file_id, v_total_err_count);

   -- get count of any existing errors
   p_total_error_rec := f_get_error_record_count(p_import_file_id);

   IF( p_total_error_rec > 0) THEN
      -- import file has errors
      p_out_returncode := 1;
   END IF;

   -- load file records without errors
   IF (p_load_type = 'L') THEN
      p_imp_application_user(p_import_file_id);
      p_imp_participant(p_import_file_id);
      p_imp_user_address(p_import_file_id,v_out_returncode); --Bug # 52457 03/26/2014
      p_imp_user_email_address(p_import_file_id);
      p_imp_user_phone(p_import_file_id);
      p_imp_user_node(p_import_file_id);
      p_imp_user_characteristic(p_import_file_id);
      p_imp_user_role(p_import_file_id);
      p_imp_cms(p_import_file_id);
      p_imp_employer(p_import_file_id);
      p_imp_participant_employer(p_import_file_id);
      p_imp_pax_comm_pref(p_import_file_id);
      COMMIT; --05/15/2014
      p_remove_obsolete(p_import_file_id);
      p_close_budget(p_import_file_id);
      p_inactive_pax_process(p_import_file_id); --08/24/2017 G6-2870
      -- loaded without error
      p_out_returncode := NVL(v_out_returncode,0);
   END IF; -- load file
   
   DELETE FROM tmp_audience_user_id; --08/23/2016
    
   INSERT INTO tmp_audience_user_id  --08/23/2016
    (audience_id, user_id)
    SELECT audience_id, user_id 
      FROM participant_audience;
   
   IF (p_out_returncode != 99 AND p_load_type = 'L') THEN 
     pkg_build_audience.prc_refresh_criteria_audience (NULL, v_returncode,v_out_user_data);
    
     IF v_returncode <> 0 THEN

      prc_execution_log_entry(c_process_name,c_release_level,'ERROR',
                            'Error in Pkg_build_audience.prc_refresh_criteria_audience while invoking for file ID '
                            ||p_import_file_id,null);
      
     END IF;
    
   END IF;
   
   IF (p_load_type = 'L') THEN
            
      OPEN p_out_user_data FOR --07/07/2016
      SELECT user_id FROM
      application_user
      WHERE NVL(date_modified,date_created) BETWEEN g_timestamp and SYSDATE
      UNION
      SELECT user_id FROM
      participant
      WHERE NVL(date_modified,date_created) BETWEEN g_timestamp and SYSDATE
      UNION
      SELECT user_id FROM
      participant_employer
      WHERE NVL(date_modified,date_created) BETWEEN g_timestamp and SYSDATE
      UNION
      SELECT user_id FROM
      user_address
      WHERE NVL(date_modified,date_created) BETWEEN g_timestamp and SYSDATE
      UNION
      SELECT user_id FROM
      user_node
      WHERE NVL(date_modified,date_created) BETWEEN g_timestamp and SYSDATE
      UNION
      SELECT user_id FROM
      user_email_address
      WHERE NVL(date_modified,date_created) BETWEEN g_timestamp and SYSDATE
      UNION
      SELECT user_id FROM
      user_phone
      WHERE NVL(date_modified,date_created) BETWEEN g_timestamp and SYSDATE
      UNION
      SELECT user_id FROM
      user_characteristic
      WHERE NVL(date_modified,date_created) BETWEEN g_timestamp and SYSDATE
      UNION
      SELECT user_id FROM
      user_role
      WHERE date_created BETWEEN g_timestamp and SYSDATE
      UNION
      SELECT user_id FROM
      participant_comm_preference
      WHERE date_created BETWEEN g_timestamp and SYSDATE
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

   v_msg := 'Success'
      || ': p_import_file_id >'   || p_import_file_id
      || '<, p_out_returncode >'  || p_out_returncode
      || '<, v_total_err_count >' || v_total_err_count
      || '<, p_total_error_rec >' || p_total_error_rec
      || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   COMMIT;

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      ROLLBACK;
      p_total_error_rec := v_import_record_count;
      p_out_returncode := 99;
END p_participant_verify_load;

END pkg_participant_verify_import;
/