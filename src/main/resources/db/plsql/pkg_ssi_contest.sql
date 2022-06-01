CREATE OR REPLACE PACKAGE PKG_SSI_CONTEST
IS
   /***********************************************************************************
      Purpose:  Package to hold all the procedures created for SSI contest module.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
     Ravi Dhanekula   12/09/2014     Initial Version
   ************************************************************************************/

   PROCEDURE prc_ssi_contest_managers (
      p_in_ssi_contest_id     IN     NUMBER,
      p_in_locale             IN     VARCHAR2,
      p_in_sortColName        IN     VARCHAR2,
      p_in_sortedBy           IN     VARCHAR2,
      p_out_return_code          OUT NUMBER,
      p_out_count_mgr_owner      OUT NUMBER,
      p_out_ref_cursor           OUT SYS_REFCURSOR);


   PROCEDURE prc_ssi_contest_totals (
      p_in_ssi_contest_id            IN     NUMBER,
      p_in_issuance_number        IN     NUMBER,
      p_out_return_code                 OUT NUMBER,
      total_objective_amount            OUT NUMBER,
      total_objective_payout            OUT NUMBER,
      total_objective_bonus_payout      OUT NUMBER,
      total_objective_bonus_cap         OUT NUMBER);

   PROCEDURE prc_ssi_contest_copy (
      p_in_ssi_contest_id             IN     NUMBER,
      p_in_destination_contest_name   IN     VARCHAR2,
      p_in_locale                     IN     VARCHAR2 DEFAULT 'en_US',
      p_in_user_id                    IN     NUMBER,
      p_out_return_code                  OUT NUMBER,
      p_out_destination_contest_id       OUT NUMBER);

   PROCEDURE prc_ssi_contest_uniqueness (
      p_in_ssi_contest_id             IN     NUMBER,
      p_in_issuance_number        IN     NUMBER,
      p_out_return_code                  OUT NUMBER,
      p_out_boolean_activity_desc        OUT NUMBER,
      p_out_boolean_obj_amount           OUT NUMBER,
      p_out_boolean_obj_payout           OUT NUMBER,
      p_out_boolean_obj_payout_desc      OUT NUMBER,
      p_out_boolean_obj_bonus            OUT NUMBER,
      p_out_boolean_obj_bonus_cap        OUT NUMBER,
      p_out_activity_description         OUT VARCHAR2,
      p_out_obj_amount                   OUT NUMBER,
      p_out_obj_payout                   OUT NUMBER,
      p_out_obj_payout_desc              OUT VARCHAR2,
      p_out_obj_bonus_increment          OUT NUMBER,
      p_out_obj_bonus_payout             OUT NUMBER,
      p_out_obj_bonus_cap                OUT NUMBER,
      p_out_total_obj_amount             OUT NUMBER,
      p_out_total_obj_payout             OUT NUMBER,
      p_out_total_obj_bonus_cap          OUT NUMBER);

   PROCEDURE prc_ssi_remove_activity (
      p_in_ssi_contest_id            IN     NUMBER,
      p_in_ssi_contest_activity_id   IN     NUMBER,
      p_in_user_id                   IN     NUMBER,
      p_out_return_code                 OUT NUMBER);

   PROCEDURE prc_ssi_contest_download (
      p_in_ssi_contest_id   IN     NUMBER,
      p_out_return_code        OUT NUMBER,
      p_out_result_set         OUT SYS_REFCURSOR);

   PROCEDURE prc_upd_ssi_contest_goal_perc (
      p_in_ssi_contest_id   IN     NUMBER,
      p_out_return_code        OUT NUMBER,
      p_out_error_message      OUT VARCHAR2);

   PROCEDURE prc_upd_ssi_contest_stackrank (
      p_in_ssi_contest_id   IN     NUMBER,
      p_out_return_code        OUT NUMBER,
      p_out_error_message      OUT VARCHAR2);

   PROCEDURE prc_ssi_remove_level (
      p_in_ssi_contest_id            IN     NUMBER,
      p_in_ssi_contest_level_id   IN     NUMBER,
      p_in_user_id                   IN     NUMBER,
      p_out_return_code                 OUT NUMBER);

  PROCEDURE prc_ssi_contest_pax_payout (
      p_in_ssi_contest_id         IN NUMBER,
      p_in_user_id                IN NUMBER,
      p_in_award_issuance_number  IN NUMBER,
      p_in_csv_user_ids           IN VARCHAR2,
      p_in_csv_payout_amounts     IN VARCHAR2,
      p_out_return_code          OUT NUMBER
      );
      PROCEDURE prc_ssi_contest_user_managers (
      p_in_ssi_contest_id     IN     NUMBER,
      p_in_user_id        IN     NUMBER,
      p_out_return_code          OUT NUMBER,
      p_out_count_mgr_owner      OUT NUMBER,
      p_out_ref_cursor           OUT SYS_REFCURSOR);
      PROCEDURE prc_ssi_contest_sr_payouts (            --05/28/2015
      p_in_ssi_contest_id             IN     NUMBER,
      p_out_return_code               OUT NUMBER,
      p_out_sr_payout_ref_cursor      OUT SYS_REFCURSOR);

      PROCEDURE PRC_SSI_CONTEST_CLAIMS_UPDATE(
      p_in_ssi_contest_id      IN NUMBER,
      p_out_count_claims       OUT NUMBER,
      p_out_error_message      OUT VARCHAR2,
      p_out_return_code        OUT NUMBER);
      


END;                                                           -- Package spec
/
CREATE OR REPLACE PACKAGE BODY PKG_SSI_CONTEST
IS
   PROCEDURE prc_ssi_contest_managers (
      p_in_ssi_contest_id     IN     NUMBER,
      p_in_locale             IN     VARCHAR2,
      p_in_sortColName        IN     VARCHAR2,
      p_in_sortedBy           IN     VARCHAR2,
      p_out_return_code          OUT NUMBER,
      p_out_count_mgr_owner      OUT NUMBER,
      p_out_ref_cursor           OUT SYS_REFCURSOR)
   IS
      /*******************************************************************************
      -- Purpose:
      -- This procedure returns the result set  of the managers and owners for SSI contest page.
      -- Person          Date        Comments
      -- ---------       ----------  -----------------------------------------------------
      -- Ravi Dhanekula 11/13/2014  Initial Version
      -- Swati             03/26/2015    Bug 60562 - Create contest-Objective-Able to add inactive member as Approver/Participant/Manager
      -- Swati          04/13/2015  Bug 61311 - Selecting pax and managers step - Contest creator cannot appear as a contest manager in the same contest
      --Ravi Arumugam   05/26/2016 Bug#66902- Manager Selection during contest setup is inconsistent, based on how Participants were selected      
      *******************************************************************************/
      v_sortCol   VARCHAR2 (200);
      l_query     VARCHAR2 (32767);
   BEGIN
      DELETE temp_table_session;

      INSERT INTO temp_table_session
         SELECT asset_code, cms_name, cms_code
           FROM vw_cms_code_value E
          WHERE     asset_code IN ('picklist.department.type.items',
                                   'picklist.positiontype.items')
                AND e.locale = p_in_locale
         UNION ALL
         SELECT asset_code, cms_value, key
           FROM vw_cms_asset_value
          WHERE key IN ('NODE_TYPE_NAME','COUNTRY_NAME') AND locale = p_in_locale;


      l_query := 'SELECT * FROM (';

      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;

      l_query :=
            l_query
         || '  '
         || '
SELECT  first_name,last_name,user_id,name,(SELECT cms_name FROM temp_table_session WHERE cms_code=''NODE_TYPE_NAME'' AND asset_code=ssi.cm_asset_code) as node_type,
                                                   (SELECT cms_name from temp_table_session where asset_code=''picklist.department.type.items'' AND cms_code=ssi.department_type) as department,
                                                   (SELECT cms_name from temp_table_session where asset_code=''picklist.positiontype.items'' AND cms_code=ssi.position_type)  AS job_position,
                                                   (SELECT cms_name FROM temp_table_session WHERE cms_code=''COUNTRY_NAME'' AND asset_code=ssi.country_name) as country_name,
                                                   country_code
           FROM (
     SELECT node_id,ssip_node_id,last_name,first_name,name,user_id,cm_asset_code,country_name,country_code,position_type,department_type FROM (
SELECT node_id,ssip_node_id,ROWNUM RN,last_name,first_name,name,user_id,cm_asset_code,country_name,country_code,position_type,department_type FROM (
    SELECT n.node_id,ssip_node_id,au.last_name,au.first_name,au.user_id,c.country_code,c.cm_asset_code country_name,n.name,nt.cm_asset_code ,emp.position_type,emp.department_type FROM (
SELECT DISTINCT 2 as set_no, user_id,node_id FROM (SELECT
                      h.node_id AS detail_node_id
                 FROM ( -- associate each node to all its hierarchy nodes
                        SELECT np.node_id,
                               p.column_value AS path_node_id
                          FROM ( -- get node hierarchy path
                                 SELECT h.node_id,
                                        level AS hier_level,
                                        sys_connect_by_path(node_id, ''/'') || ''/'' AS node_path
                                   FROM node h
                                  START WITH h.parent_node_id IS NULL
                                CONNECT BY PRIOR h.node_id = h.parent_node_id
                               ) np,
                               -- parse node path into individual nodes
                               -- pivoting the node path into separate records
                               TABLE( CAST( MULTISET(
                                  SELECT TO_NUMBER(
                                            SUBSTR(np.node_path,
                                                   INSTR(np.node_path, ''/'', 1, LEVEL)+1,
                                                   INSTR(np.node_path, ''/'', 1, LEVEL+1) - INSTR(np.node_path, ''/'', 1, LEVEL)-1
                                            )
                                         )
                                    FROM dual
                                 CONNECT BY LEVEL <= np.hier_level
                               ) AS sys.odcinumberlist ) ) p
                      ) npn,
                       (SELECT node_id,node_type_id,
         NAME as node_name,
         parent_node_id
    FROM node n
    START WITH parent_node_id IS NULL
  CONNECT BY PRIOR node_id = parent_node_id) h,
                      (SELECT  node_id FROM ssi_contest_participant scpt,user_node un
                                                  WHERE scpt.user_id = un.user_id
                                                  AND scpt.ssi_contest_id = '''
         || p_in_ssi_contest_id
         || '''
                                                  AND      un.is_primary = 1) rt
                WHERE rt.node_id = npn.node_id
                  AND npn.path_node_id = h.node_id) dds, user_node un
             WHERE dds.detail_node_id = un.node_id
             AND un.role in (''mgr'',''own'')) A,
             vw_curr_pax_employer emp,node n,application_user au,
             ssi_contest ssic,--04/13/2015 Bug 61311
             node_type nt,user_address ua,country c,
             (select t2.node_id as ssip_node_id,1 as set_no from ssi_contest_participant t1,
                                                     user_node t2
                                                     where t1.user_id = t2.user_id and
                                                     t1.ssi_contest_id = '''
         || p_in_ssi_contest_id
         || ''') ssicp
             WHERE ssic.ssi_contest_id = '''|| p_in_ssi_contest_id|| ''' --04/13/2015 Bug 61311
             AND ssic.contest_owner_id <> au.user_id --04/13/2015 Bug 61311 --05/21/2015
             AND a.user_id =emp.user_id(+)
             AND  a.node_id = n.node_id
             AND  a.node_id = ssicp.ssip_node_id (+)
             AND a.user_id = au.user_id
             AND au.is_active = 1 --03/26/2015 Bug 60562
             AND au.user_id = ua.user_id
             AND ua.is_primary = 1
             AND ua.country_id = c.country_id
             AND n.node_type_id = nt.node_type_id ORDER BY NVL(ssicp.set_no,a.set_no),last_name ASC)) ) ssi) ORDER BY ' --remove where RN <=300 --Bug 66902 05/26/2016            
         || v_sortCol;


      OPEN p_out_ref_cursor FOR l_query;

      SELECT COUNT (1)
        INTO p_out_count_mgr_owner
        FROM (SELECT manager_name,
                     name,
                     cm_asset_code,
                     position_type,
                     department_type
                FROM (SELECT ROWNUM RN,
                             manager_name,
                             name,
                             cm_asset_code,
                             position_type,
                             department_type
                        FROM (  SELECT au.last_name || ' , ' || au.first_name
                                          AS manager_name,
                                       n.name,
                                       nt.cm_asset_code,
                                       emp.position_type,
                                       emp.department_type
                                  FROM (SELECT DISTINCT user_id, node_id
                                          FROM (SELECT           -- key fields
                                                      h.node_id
                                                          AS detail_node_id
                                                  FROM ( -- associate each node to all its hierarchy nodes
                                                        SELECT np.node_id,
                                                               p.COLUMN_VALUE
                                                                  AS path_node_id
                                                          FROM ( -- get node hierarchy path
                                                                SELECT     h.node_id,
                                                                           LEVEL
                                                                              AS hier_level,
                                                                              SYS_CONNECT_BY_PATH (
                                                                                 node_id,
                                                                                 '/')
                                                                           || '/'
                                                                              AS node_path
                                                                      FROM node h
                                                                START WITH h.parent_node_id
                                                                              IS NULL
                                                                CONNECT BY PRIOR h.node_id =
                                                                              h.parent_node_id)
                                                               np,
                                                               -- parse node path into individual nodes
                                                               -- pivoting the node path into separate records
                                                               TABLE (
                                                                  CAST (
                                                                     MULTISET (
                                                                            SELECT TO_NUMBER (
                                                                                      SUBSTR (
                                                                                         np.node_path,
                                                                                           INSTR (
                                                                                              np.node_path,
                                                                                              '/',
                                                                                              1,
                                                                                              LEVEL)
                                                                                         + 1,
                                                                                           INSTR (
                                                                                              np.node_path,
                                                                                              '/',
                                                                                              1,
                                                                                                LEVEL
                                                                                              + 1)
                                                                                         - INSTR (
                                                                                              np.node_path,
                                                                                              '/',
                                                                                              1,
                                                                                              LEVEL)
                                                                                         - 1))
                                                                              FROM DUAL
                                                                        CONNECT BY LEVEL <=
                                                                                      np.hier_level) AS SYS.odcinumberlist)) p)
                                                       npn,
                                                       (    SELECT node_id,
                                                                   node_type_id,
                                                                   NAME
                                                                      AS node_name,
                                                                   parent_node_id
                                                              FROM node n
                                                        START WITH parent_node_id
                                                                      IS NULL
                                                        CONNECT BY PRIOR node_id =
                                                                      parent_node_id)
                                                       h,
                                                       (SELECT node_id
                                                          FROM ssi_contest_participant scpt,
                                                               user_node un
                                                         WHERE     scpt.user_id =
                                                                      un.user_id
                                                               AND scpt.ssi_contest_id =
                                                                      p_in_ssi_contest_id
                                                               AND un.is_primary =
                                                                      1) rt
                                                 WHERE     rt.node_id =
                                                              npn.node_id
                                                       AND npn.path_node_id =
                                                              h.node_id) dds,
                                               user_node un --,vw_curr_pax_employer emp,node n,application_user au,node_type nt
                                         WHERE     dds.detail_node_id =
                                                      un.node_id
                                               AND un.role IN ('mgr', 'own')) A,
                                       vw_curr_pax_employer emp,
                                       node n,
                                       application_user au,
                                       ssi_contest ssic, --04/13/2015 Bug 61311
                                       node_type nt
                                 WHERE ssic.ssi_contest_id =  p_in_ssi_contest_id --04/13/2015 Bug 61311
                                       AND a.user_id = emp.user_id(+)
                                       AND a.node_id = n.node_id
                                       AND a.user_id = au.user_id
                                       AND ssic.contest_owner_id <> au.user_id --04/13/2015 Bug 61311 --05/21/2015
                                       AND n.node_type_id = nt.node_type_id
                              ORDER BY last_name ASC))) ssi;


      p_out_return_code := 0;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := 99;

         prc_execution_log_entry (
            'PRC_SSI_CONTEST_MANAGERS',
            1,
            'ERROR',
               'Process failed forssi_contest_id:  '
            || p_in_ssi_contest_id
            || SQLERRM,
            NULL);
   END prc_ssi_contest_managers;

   PROCEDURE prc_ssi_contest_totals (
      p_in_ssi_contest_id            IN     NUMBER,
      p_in_issuance_number        IN     NUMBER,
      p_out_return_code                 OUT NUMBER,
      total_objective_amount            OUT NUMBER,
      total_objective_payout            OUT NUMBER,
      total_objective_bonus_payout      OUT NUMBER,
      total_objective_bonus_cap         OUT NUMBER)
   IS
      /*******************************************************************************
      -- Purpose:
      -- This procedure to calculate the totals for all participants in a contest
      -- Person      Date        Comments
      -- ---------   ----------  -----------------------------------------------------
      Suresh J      12/09/2014   Initial creation
                   12/16/2014   Added conditions based on include_stack_rank and include_bonus column values
      KrishnaDeepika 05/12/2015 Replaced include_stack_rank with is_same_obj_description
      *******************************************************************************/
      v_payout_type           VARCHAR2 (20);
      v_column_value_cnt      NUMBER := NULL;
      v_ssi_contest_pax_cnt   NUMBER := NULL;
   BEGIN
      SELECT payout_type
        INTO v_payout_type
        FROM SSI_CONTEST
       WHERE ssi_contest_id = p_in_ssi_contest_id;

      SELECT NVL (COUNT (*), 0)
        INTO v_ssi_contest_pax_cnt
        FROM SSI_CONTEST_PARTICIPANT
       WHERE ssi_contest_id = p_in_ssi_contest_id
       AND NVL(award_issuance_number,0) = NVL(p_in_issuance_number,NVL(award_issuance_number,0));

      IF v_payout_type = 'points'
      THEN
         SELECT NVL (COUNT (*), 0)
           INTO v_column_value_cnt
           FROM SSI_CONTEST_PARTICIPANT ssicp, SSI_CONTEST ssic
          WHERE     ssicp.ssi_contest_id = ssic.ssi_contest_id
                AND NVL(ssicp.award_issuance_number,0) = NVL(p_in_issuance_number,NVL(ssicp.award_issuance_number,0))
                AND ssicp.ssi_contest_id = p_in_ssi_contest_id
--                AND (   (    NVL (ssic.include_stack_rank, 0) = 1  --05/12/2015
                AND (   (    NVL (ssic.is_same_obj_description, 0) = 1  --05/12/2015
                         AND ssic.activity_description IS NOT NULL)
--                     OR (    NVL (ssic.include_stack_rank, 0) = 0  --05/12/2015
                     OR (    NVL (ssic.is_same_obj_description, 0) = 0  --05/12/2015
                         AND ssicp.activity_description IS NOT NULL))
                AND ssicp.objective_amount IS NOT NULL
                AND ssicp.objective_payout IS NOT NULL
                AND (   NVL (ssic.include_bonus, 0) = 0
                     OR (    NVL (ssic.include_bonus, 0) = 1
                         AND (    ssicp.objective_bonus_payout IS NOT NULL
                              AND ssicp.objective_bonus_cap IS NOT NULL)));

         IF v_column_value_cnt = v_ssi_contest_pax_cnt
         THEN
            SELECT NVL (SUM (objective_amount), 0),
                   NVL (SUM (objective_payout), 0),
                   NVL (SUM (objective_bonus_payout), 0),
                   NVL (SUM (objective_bonus_cap), 0)
              INTO total_objective_amount,
                   total_objective_payout,
                   total_objective_bonus_payout,
                   total_objective_bonus_cap
              FROM SSI_CONTEST_PARTICIPANT
             WHERE ssi_contest_id = p_in_ssi_contest_id
             AND NVL(award_issuance_number,0) = NVL(p_in_issuance_number,NVL(award_issuance_number,0));

            p_out_return_code := 0;
         ELSE
            p_out_return_code := 99;
            prc_execution_log_entry (
               'PRC_SSI_CONTEST_TOTALS',
               1,
               'ERROR',
                  'At least one of the required columns (activity_description,objective_amount,objective_payout,objective_bonus_payout,objective_bonus_cap) contains NULL for ssi_contest_id:  '
               || p_in_ssi_contest_id
               || SQLERRM,
               NULL);
         END IF;
      ELSIF v_payout_type = 'other'
      THEN
         SELECT COUNT (*)
           INTO v_column_value_cnt
           FROM SSI_CONTEST_PARTICIPANT ssicp, SSI_CONTEST ssic
          WHERE     ssicp.ssi_contest_id = ssic.ssi_contest_id
                AND NVL(ssicp.award_issuance_number,0) = NVL(p_in_issuance_number,NVL(ssicp.award_issuance_number,0))
                AND ssicp.ssi_contest_id = p_in_ssi_contest_id
--                AND (   (    NVL (ssic.include_stack_rank, 0) = 1  --05/12/2015
                AND (   (    NVL (ssic.is_same_obj_description, 0) = 1  --05/12/2015
                         AND ssic.activity_description IS NOT NULL)
--                     OR (    NVL (ssic.include_stack_rank, 0) = 0  --05/12/2015
                     OR (    NVL (ssic.is_same_obj_description, 0) = 0  --05/12/2015
                         AND ssicp.activity_description IS NOT NULL))
                AND ssicp.objective_amount IS NOT NULL
                AND ssicp.objective_payout IS NOT NULL
                AND objective_payout_description IS NOT NULL;

         IF v_column_value_cnt = v_ssi_contest_pax_cnt
         THEN
            SELECT NVL (SUM (objective_amount), 0),
                   NVL (SUM (objective_payout), 0),
                   0,
                   0
              INTO total_objective_amount,
                   total_objective_payout,
                   total_objective_bonus_payout,
                   total_objective_bonus_cap
              FROM SSI_CONTEST_PARTICIPANT
             WHERE ssi_contest_id = p_in_ssi_contest_id
             AND NVL(award_issuance_number,0) = NVL(p_in_issuance_number,NVL(award_issuance_number,0));

            p_out_return_code := 0;
         ELSE
            p_out_return_code := 99;
            prc_execution_log_entry (
               'PRC_SSI_CONTEST_TOTALS',
               1,
               'ERROR',
                  'At least one of the required columns (activity_description,objective_amount,objective_payout,objective_payout_description) contains NULL for ssi_contest_id:  '
               || p_in_ssi_contest_id
               || SQLERRM,
               NULL);
         END IF;
      ELSE
         SELECT NVL (COUNT (*), 0)
           INTO v_column_value_cnt
           FROM SSI_CONTEST_PARTICIPANT ssicp, SSI_CONTEST ssic
          WHERE     ssicp.ssi_contest_id = ssic.ssi_contest_id
                AND NVL(ssicp.award_issuance_number,0) = NVL(p_in_issuance_number,NVL(ssicp.award_issuance_number,0))
                AND ssicp.ssi_contest_id = p_in_ssi_contest_id
--                AND (   (    NVL (ssic.include_stack_rank, 0) = 1  --05/12/2015
                AND (   (    NVL (ssic.is_same_obj_description, 0) = 1  --05/12/2015
                         AND ssic.activity_description IS NOT NULL)
--                     OR (    NVL (ssic.include_stack_rank, 0) = 0  --05/12/2015
                     OR (    NVL (ssic.is_same_obj_description, 0) = 0  --05/12/2015
                         AND ssicp.activity_description IS NOT NULL))
                AND ssicp.objective_amount IS NOT NULL
                AND ssicp.objective_payout IS NOT NULL
                AND (   NVL (ssic.include_bonus, 0) = 0
                     OR (    NVL (ssic.include_bonus, 0) = 1
                         AND (    ssicp.objective_bonus_payout IS NOT NULL
                              AND ssicp.objective_bonus_cap IS NOT NULL)));

         IF v_column_value_cnt = v_ssi_contest_pax_cnt
         THEN
            SELECT NVL (SUM (objective_amount), 0),
                   NVL (SUM (objective_payout), 0),
                   NVL (SUM (objective_bonus_payout), 0),
                   NVL (SUM (objective_bonus_cap), 0)
              INTO total_objective_amount,
                   total_objective_payout,
                   total_objective_bonus_payout,
                   total_objective_bonus_cap
              FROM SSI_CONTEST_PARTICIPANT
             WHERE ssi_contest_id = p_in_ssi_contest_id
             AND NVL(award_issuance_number,0) = NVL(p_in_issuance_number,NVL(award_issuance_number,0));

            p_out_return_code := 0;
         ELSE
            p_out_return_code := 99;
            prc_execution_log_entry (
               'PRC_SSI_CONTEST_TOTALS',
               1,
               'ERROR',
                  'At least one of the required columns (activity_description,objective_amount,objective_payout) contains NULL for ssi_contest_id:  '
               || p_in_ssi_contest_id,
               NULL);
         END IF;
      END IF;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := 99;
         prc_execution_log_entry (
            'PRC_SSI_CONTEST_TOTALS',
            1,
            'ERROR',
               'Process failed for ssi_contest_id:  '
            || p_in_ssi_contest_id
            || ' '
            || SQLERRM,
            NULL);
   END prc_ssi_contest_totals;

   PROCEDURE prc_ssi_contest_copy (
      p_in_ssi_contest_id             IN     NUMBER,
      p_in_destination_contest_name   IN     VARCHAR2,
      p_in_locale                     IN     VARCHAR2 DEFAULT 'en_US',
      p_in_user_id                    IN     NUMBER,
      p_out_return_code                  OUT NUMBER,
      p_out_destination_contest_id       OUT NUMBER)
   IS
      /*******************************************************************************
      -- Purpose:
      -- This procedure is to copy an SSI Contest
      -- Person      Date        Comments
      -- ---------   ----------  -----------------------------------------------------
      Suresh J            12/16/2014   Initial creation
      Ravi Dhanekula 12/22/2014   Changed  bill_payout_code to the new columns  bill_payout_code_1 AND  bill_payout_code_2.
                     12/30/2014   Removed referrence to the table PROMO_SSI_BILL_CODE.
      Swati          04/30/2015   Copy contest - Participants and Managers Screen - Non Eligible participants and managers are able to add in to the contest
     Suresh J        06/09/2015  Bug 62641,62642 Added is_same_obj_description and Data Collection Tab details  
     nagarajs       01/19/2017   Changes to use new ssi bill code table
      *******************************************************************************/

      --Variables for SSI Master Promo Table - PROMO_SSI
      --v_selected_contests           NUMBER (12);
      v_require_contest_approval    NUMBER (1);
      v_contest_approval_levels     NUMBER (1);
      v_approval_lvl1_aud_type      VARCHAR2 (40);
      v_approval_lvl2_aud_type      VARCHAR2 (40);
      v_primary_audience_type       VARCHAR2 (40);
      v_secondary_audience_type     VARCHAR2 (40);
      v_allow_award_points          NUMBER (1);
      v_allow_award_other           NUMBER (1);

      --Original Contest
      v_ssi_contest_id              ssi_contest.ssi_contest_id%TYPE;
      c_created_by                  ssi_contest.created_by%TYPE
                                       := p_in_user_id;
      c_contest_owner_id                  ssi_contest.contest_owner_id%TYPE
                                       := p_in_user_id; --05/21/2015
      v_promotion_id                NUMBER (12);
      v_contest_type                VARCHAR2 (40);
      /*v_bill_payout_code            VARCHAR2 (50); --01/19/2017
      v_bill_payout_code_type       ssi_contest.bill_payout_code_type%TYPE;*/
      v_badge_rule_id               NUMBER (18);
      v_payout_type                 VARCHAR2 (40) := NULL;

      v_cm_asset_code               cms_asset.code%TYPE;
      v_destination_cm_asset_code   cms_asset.code%TYPE;
      v_data_collection_type        ssi_contest.data_collection_type%TYPE;  --06/09/2015 

      v_stage                       VARCHAR2 (100);
   BEGIN
      v_stage := 'Getting original contest column values for SSI_CONTEST_ID: ';

      SELECT promotion_id,
             contest_type,
             /*bill_payout_code_1, --01/19/2017
             bill_payout_code_type,*/
             badge_rule_id,
             payout_type                                          --12/22/2014
             ,data_collection_type  --06/09/2015
        INTO v_promotion_id,
             v_contest_type,
             /*v_bill_payout_code,  --01/19/2017
             v_bill_payout_code_type,*/
             v_badge_rule_id,
             v_payout_type
             ,v_data_collection_type  --06/09/2015
        FROM ssi_contest
       WHERE ssi_contest_id = p_in_ssi_contest_id;

      v_stage := 'Getting promo audience types for SSI_CONTEST_ID: ';

      SELECT primary_audience_type, secondary_audience_type
        INTO v_primary_audience_type, v_secondary_audience_type
        FROM promotion
       WHERE promotion_id = v_promotion_id;

      v_stage :=
         'Getting SSI Master Promo and Bill Code column values for SSI_CONTEST_ID: ';

      SELECT                                     --NVL (selected_contests, 0),
            require_contest_approval,
             contest_approval_levels,
             contest_approval_lvl1_aud_type,
             contest_approval_lvl2_aud_type,
             allow_award_points,
             allow_award_other
        INTO                                            --v_selected_contests,
            v_require_contest_approval,
             v_contest_approval_levels,
             v_approval_lvl1_aud_type,
             v_approval_lvl2_aud_type,
             v_allow_award_points,
             v_allow_award_other
        FROM promo_ssi
       WHERE promotion_id = v_promotion_id;

      --      SELECT COUNT (*)
      --        INTO v_bill_code_cnt
      --        FROM PROMO_SSI_BILL_CODE
      --       WHERE promotion_id = v_promotion_id;

      v_stage :=
         'Inserting records into SSI_CONTEST table for SSI_CONTEST_ID: ';

      SELECT ssi_contest_pk_sq.NEXTVAL INTO v_ssi_contest_id FROM DUAL;

      INSERT INTO ssi_contest (ssi_contest_id,
                               promotion_id,
                               contest_type,
                               status,
                               contest_start_date,
                               contest_end_date,
                               display_start_date,
                               cm_asset_code,
                               include_personal_message,
                               /*bill_payout_code_type,  --01/19/2017
                               bill_payout_code_1,                --12/22/2014
                               bill_payout_code_2, */               --12/22/2014
                               activity_measure_type,
                               activity_measure_cur_code,
                               sit_indv_baseline_type,
                               SIT_BONUS_CAP, SIT_BONUS_INCREMENT, SIT_BONUS_PAYOUT,  --04/20/2015 Bug 61558
                               payout_type,
                               include_stack_rank,
                               include_bonus,
                               payout_other_cur_code,
                               activity_description,
                               badge_rule_id,
                               activity_submission_type,
                               stack_rank_order,
                               contest_goal,
                               include_stack_rank_qualifier,
                               stack_rank_qualifier_amount,
                               created_by,
                               date_created,
                               modified_by,
                               date_modified,
                               version,
                               contest_owner_id
                               ,is_same_obj_description  --06/09/2015
                               ,is_claim_approval_needed  --06/09/2015
                               ,data_collection_type   --06/09/2015   
                               ,claim_submission_last_date --06/09/2015                         
                               )  --05/21/2015
         SELECT v_ssi_contest_id,
                promotion_id,
                contest_type,
                --                CASE
                --                   WHEN TO_NUMBER (v_contest_type) = v_selected_contests
                --                   THEN --Workaround.. Need to modify when actual data available
                --                      contest_type
                --                   ELSE
                --                      NULL
                --                END,
                'draft',
                contest_start_date,
                contest_end_date,
                display_start_date,
                   'ssi_contest_data.contest_details.'
                || cms_unique_name_sq.NEXTVAL,
                include_personal_message,
                /*bill_payout_code_type, --01/19/2017
                --   bill_payout_code,
                DECODE (v_bill_payout_code_type,
                        'other', bill_payout_code_1,
                        NULL),                        --12/22/2014--12/20/2014
                DECODE (v_bill_payout_code_type,
                        'other', bill_payout_code_2,
                        NULL),*/                      --12/22/2014--12/20/2014
                activity_measure_type,
                activity_measure_cur_code,
                sit_indv_baseline_type,
                SIT_BONUS_CAP, SIT_BONUS_INCREMENT, SIT_BONUS_PAYOUT,  --04/20/2015 Bug 61588
                --   payout_type,
                CASE
                   WHEN payout_type = 'other' AND v_allow_award_other = 0
                   THEN
                      NULL
                   ELSE
                      payout_type
                END,
                include_stack_rank,
                include_bonus,
                payout_other_cur_code,
                activity_description,
                badge_rule_id,
                activity_submission_type,
                stack_rank_order,
                contest_goal,
                include_stack_rank_qualifier,
                stack_rank_qualifier_amount,
                c_created_by,
                SYSDATE,
                NULL,
                NULL,
                0,
                c_contest_owner_id  --05/21/2015
                ,is_same_obj_description  --06/09/2015    
                ,is_claim_approval_needed  --06/09/2015
                ,data_collection_type   --06/09/2015       
                ,claim_submission_last_date --06/09/2015                                     
           FROM ssi_contest 
          WHERE ssi_contest_id = p_in_ssi_contest_id;
          
        INSERT INTO SSI_CONTEST_BILL_CODE --01/19/2017
                 (ssi_contest_bill_code_id, 
                  ssi_contest_id, 
                  track_bills_by, 
                  bill_code, 
                  custom_value,
                  sort_order, 
                  created_by,
                  date_created,
                  modified_by, 
                  date_modified,
                  version)
           SELECT ssi_contest_bill_code_pk_sq.NEXTVAL,
                  v_ssi_contest_id,
                  track_bills_by,
                  bill_code,
                  custom_value,
                  sort_order,
                  c_created_by,
                  SYSDATE,
                  NULL,
                  NULL,
                  0
             FROM SSI_CONTEST_BILL_CODE
            WHERE ssi_contest_id = p_in_ssi_contest_id;
           
       IF v_data_collection_type = 'claimSubmission' THEN  --06/09/2015
           
          INSERT INTO SSI_CONTEST_CLAIM_FIELD      --06/09/2015
          ( ssi_contest_claim_field_id, 
            ssi_contest_id, 
            ssi_claim_form_step_element_id, 
            is_required, 
            sequence_number, 
            created_by, 
            date_created, 
            modified_by, 
            date_modified, 
            version)
          SELECT  ssi_contest_claim_field_pk_sq.NEXTVAL,
                   v_ssi_contest_id,
                   ssi_claim_form_step_element_id,
                   is_required,
                   sequence_number,
                   c_created_by,
                   SYSDATE,
                   NULL,
                   NULL,
                   0 
           FROM ssi_contest_claim_field
          WHERE ssi_contest_id = p_in_ssi_contest_id;
           
       END IF; 

      v_stage := 'Find the asset code for new ssi_contest_id ';

      SELECT cm_asset_code
        INTO v_cm_asset_code
        FROM ssi_contest
       WHERE ssi_contest_id = p_in_ssi_contest_id;

      SELECT cm_asset_code
        INTO v_destination_cm_asset_code
        FROM ssi_contest
       WHERE ssi_contest_id = v_ssi_contest_id;

      v_stage := 'Copy the contents to destination contest asset code ';
      prc_copy_cms_asset (v_cm_asset_code,
                          v_destination_cm_asset_code,
                          p_out_return_code);

      v_stage :=
         'Update the name of the new contest created with name given thru screen';

      UPDATE cms_content_data
         SET VALUE = p_in_destination_contest_name
       WHERE     key = 'CONTEST_NAME'
             AND content_id IN (SELECT id
                                  FROM cms_content
                                 WHERE content_key_id IN (SELECT id
                                                            FROM cms_content_key
                                                           WHERE asset_id IN (SELECT id
                                                                                FROM cms_asset
                                                                               WHERE code IN (SELECT cm_asset_code
                                                                                                FROM ssi_contest
                                                                                               WHERE ssi_contest_id =
                                                                                                        v_ssi_contest_id))));

      v_stage :=
         'Inserting records into SSI_CONTEST_ACTIVITY table for SSI_CONTEST_ID: ';

      INSERT INTO ssi_contest_activity (ssi_contest_activity_id,
                                        ssi_contest_id,
                                        description,
                                        increment_amount,
                                        payout_amount,
                                        min_qualifier,
                                        payout_cap_amount,
                                        goal_amount,
                                        sequence_number,
                                        payout_description,
                                        created_by,
                                        date_created,
                                        modified_by,
                                        date_modified,
                                        version)
         SELECT ssi_contest_activity_pk_sq.NEXTVAL,
                v_ssi_contest_id,
                description,
                increment_amount,
                --        payout_amount,
                CASE
                   WHEN v_payout_type = 'other' AND v_allow_award_other = 0
                   THEN
                      NULL
                   ELSE
                      payout_amount
                END,
                min_qualifier,
                --        payout_cap_amount,
                CASE
                   WHEN v_payout_type = 'other' AND v_allow_award_other = 0
                   THEN
                      NULL
                   ELSE
                      payout_cap_amount
                END,
                goal_amount,
                sequence_number,
                --        payout_description,
                CASE
                   WHEN v_payout_type = 'other' AND v_allow_award_other = 0
                   THEN
                      NULL
                   ELSE
                      payout_description
                END,
                c_created_by,
                SYSDATE,
                NULL,
                NULL,
                version
           FROM ssi_contest_activity
          WHERE ssi_contest_id = p_in_ssi_contest_id;

      v_stage :=
         'Inserting records into SSI_CONTEST_LEVEL table for SSI_CONTEST_ID: ';

      INSERT INTO ssi_contest_level (ssi_contest_level_id,
                                     ssi_contest_id,
                                     goal_amount,
                                     payout_amount,
                                     payout_description,
                                     badge_rule_id,
                                     sequence_number,
                                     created_by,
                                     date_created,
                                     modified_by,
                                     date_modified,
                                     version)
         SELECT ssi_contest_level_pk_sq.NEXTVAL,
                v_ssi_contest_id,
                goal_amount,
                --        payout_amount,
                CASE
                   WHEN v_payout_type = 'other' AND v_allow_award_other = 0
                   THEN
                      NULL
                   ELSE
                      payout_amount
                END,
                --        payout_description,
                CASE
                   WHEN v_payout_type = 'other' AND v_allow_award_other = 0
                   THEN
                      NULL
                   ELSE
                      payout_description
                END,
                badge_rule_id,
                sequence_number,
                c_created_by,
                SYSDATE,
                NULL,
                NULL,
                version
           FROM ssi_contest_level
          WHERE ssi_contest_id = p_in_ssi_contest_id;

           v_stage :=
'Inserting records into SSI_CONTEST_SR_PAYOUT table for SSI_CONTEST_ID: ';

      INSERT INTO ssi_contest_sr_payout (ssi_contest_sr_payout_id,
                                         ssi_contest_id,
                                         rank_position,
                                         payout_amount,
                                         payout_desc,
                                         badge_rule_id,
                                         created_by,
                                         date_created,
                                         modified_by,
                                         date_modified,
                                         version)
         SELECT SSI_CONTEST_SR_PAYOUT_PK_SQ.NEXTVAL,
                v_ssi_contest_id,
                rank_position,
                payout_amount,
                payout_desc,
                badge_rule_id,
                c_created_by,
                SYSDATE,
                NULL,
                NULL,
                version
           FROM ssi_contest_sr_payout
          WHERE ssi_contest_id = p_in_ssi_contest_id;

      v_stage :=
         'Inserting records into SSI_CONTEST_APPROVER table for SSI_CONTEST_ID: ';

      IF NVL (v_require_contest_approval, 0) = 1
      THEN
         --    select ssi_contest_approver_pk_sq.nextval into v_ssi_contest_approver_id from dual;
         INSERT INTO ssi_contest_approver (ssi_contest_approver_id,
                                           ssi_contest_id,
                                           ssi_approver_type,
                                           user_id,
                                           created_by,
                                           date_created,
                                           version)
            SELECT ssi_contest_approver_pk_sq.NEXTVAL,
                   v_ssi_contest_id,
                   ssica.ssi_approver_type,
                   ssica.user_id,
                   c_created_by,
                   SYSDATE,
                   0
              FROM ssi_contest_approver ssica,
                   application_user au --04/30/2015 Bug 61643
             WHERE     ssica.ssi_contest_id = p_in_ssi_contest_id
                   AND (   (    v_contest_approval_levels = 1
                            AND EXISTS
                                   (SELECT pax_aud.user_id
                                      FROM promo_audience pa,
                                           participant_audience pax_aud
                                     WHERE     pa.promotion_id =
                                                  v_promotion_id
                                           AND pa.audience_id =
                                                  pax_aud.audience_id
                                           AND pa.promo_audience_type IN ('SSI_CONTEST_APPROVAL_LEVEL1')
                                           AND pax_aud.user_id =
                                                  ssica.user_id))
                        OR (    v_contest_approval_levels = 2
                            AND EXISTS
                                   (SELECT pax_aud.user_id
                                      FROM promo_audience pa,
                                           participant_audience pax_aud
                                     WHERE     pa.promotion_id =
                                                  v_promotion_id
                                           AND pa.audience_id =
                                                  pax_aud.audience_id
                                           AND pa.promo_audience_type IN ('SSI_CONTEST_APPROVAL_LEVEL1',
                                                                          'SSI_CONTEST_APPROVAL_LEVEL2')
                                           AND pax_aud.user_id =
                                                  ssica.user_id)))
                   AND ssica.user_id = au.user_id --04/30/2015 Bug 61643
                   AND au.is_active = 1; --04/30/2015 Bug 61643
      END IF;

      v_stage :=
         'Inserting records into SSI_CONTEST_DOCUMENT table for SSI_CONTEST_ID: ';

      --select ssi_contest_document_pk_sq.nextval into v_ssi_contest_document_id from dual;
      INSERT INTO ssi_contest_document (ssi_contest_document_id,
                                        ssi_contest_id,
                                        attachment_name,
                                        attachment_display_name,
                                        locale,
                                        created_by,
                                        date_created,
                                        modified_by,
                                        date_modified,
                                        version)
         SELECT ssi_contest_document_pk_sq.NEXTVAL,
                v_ssi_contest_id,
                attachment_name,
                attachment_display_name,
                locale,
                c_created_by,
                SYSDATE,
                NULL,
                NULL,
                0
           FROM ssi_contest_document
          WHERE ssi_contest_id = p_in_ssi_contest_id;

      v_stage :=
         'Inserting records into SSI_CONTEST_MANAGER table for SSI_CONTEST_ID: ';

      --select ssi_contest_manager_pk_sq.nextval into v_ssi_contest_manager_id from dual;
      INSERT INTO ssi_contest_manager (ssi_contest_manager_id,
                                       ssi_contest_id,
                                       user_id,
                                       created_by,
                                       date_created,
                                       version)
         SELECT ssi_contest_manager_pk_sq.NEXTVAL,
                v_ssi_contest_id,
                ssicm.user_id,
                c_created_by,
                SYSDATE,
                0
           FROM ssi_contest_manager ssicm, application_user au
          WHERE     ssicm.ssi_contest_id = p_in_ssi_contest_id
                AND ssicm.user_id = au.user_id
                AND au.is_active = 1
                AND (SELECT COUNT (un.user_id)
                       FROM user_node un
                      WHERE     un.role = 'mbr'
                            AND EXISTS
                                   (SELECT un2.node_id
                                      FROM user_node un2
                                     WHERE     un2.user_id = ssicm.user_id
                                           AND un2.node_id = un.node_id)) > 0;

      v_stage :=
         'Inserting records into SSI_CONTEST_PARTICIPANT table for SSI_CONTEST_ID: ';

      --select ssi_contest_participant_pk_sq.nextval into v_ssi_contest_participant_id from dual;
      INSERT INTO ssi_contest_participant (ssi_contest_participant_id,
                                           ssi_contest_id,
                                           user_id,
                                           activity_description,
                                           objective_amount,
                                           objective_payout,
                                           --                                           objective_payout_level,
                                           objective_payout_description,
                                           objective_bonus_increment,
                                           objective_bonus_payout,
                                           objective_bonus_cap,
                                           created_by,
                                           date_created,
                                           modified_by,
                                           date_modified,
                                           version)
         SELECT ssi_contest_participant_pk_sq.NEXTVAL,
                v_ssi_contest_id,
                ssicp.user_id,
                activity_description,
                objective_amount,
                --objective_payout,
                CASE
                   WHEN v_payout_type = 'other' AND v_allow_award_other = 0
                   THEN
                      NULL
                   ELSE
                      objective_payout
                END,
                --objective_payout_level,
                --                CASE
                --                   WHEN v_payout_type = 'other' AND v_allow_award_other = 0
                --                   THEN
                --                      NULL
                --                   ELSE
                --                      objective_payout_level
                --                END,
                --objective_payout_description,
                CASE
                   WHEN v_payout_type = 'other' AND v_allow_award_other = 0
                   THEN
                      NULL
                   ELSE
                      objective_payout_description
                END,
                objective_bonus_increment,
                objective_bonus_payout,
                objective_bonus_cap,
                c_created_by,
                SYSDATE,
                NULL,
                NULL,
                0
           FROM ssi_contest_participant ssicp, application_user au
          WHERE     ssi_contest_id = p_in_ssi_contest_id
                AND au.user_id = ssicp.user_id
                AND (   (    au.is_active = 1
                         AND (   v_secondary_audience_type =
                                    'allactivepaxaudience'))
                     OR (    au.is_active = 1
                         AND (   v_secondary_audience_type =
                                    'specifyaudience')
                         AND EXISTS
                                (SELECT pax_aud.user_id
                                   FROM promo_audience pa,
                                        participant_audience pax_aud
                                  WHERE     pa.promotion_id = v_promotion_id
                                        AND pa.audience_id =
                                               pax_aud.audience_id
                                        AND pa.promo_audience_type IN ('SECONDARY')
                                        AND pax_aud.user_id = ssicp.user_id)));
    
      v_stage :=
         'Inserting records into SSI_CONTEST_SUPERVIEWER table for SSI_CONTEST_ID: ';

      --select ssi_contest_superviewer_pk_sq.nextval into v_ssi_contest_superviewer_id from dual; --Added superviewer copy with G6.
      INSERT INTO ssi_contest_superviewer (ssi_contest_superviewer_id,
                                           ssi_contest_id,
                                           user_id,
                                           created_by,
                                           date_created,
                                           version)
         SELECT ssi_contest_superviewer_pk_sq.NEXTVAL,
                v_ssi_contest_id,
                ssicp.user_id,
                c_created_by,
                SYSDATE,
                0
           FROM ssi_contest_superviewer ssicp, application_user au
          WHERE     ssi_contest_id = p_in_ssi_contest_id
                AND au.user_id = ssicp.user_id
                AND (   (    au.is_active = 1
                         AND (   v_secondary_audience_type =
                                    'allactivepaxaudience'))
                     OR (    au.is_active = 1
                         AND (   v_secondary_audience_type =
                                    'specifyaudience')
                         AND EXISTS
                                (SELECT pax_aud.user_id
                                   FROM promo_audience pa,
                                        participant_audience pax_aud
                                  WHERE     pa.promotion_id = v_promotion_id
                                        AND pa.audience_id =
                                               pax_aud.audience_id
                                        AND pa.promo_audience_type IN ('SECONDARY')
                                        AND pax_aud.user_id = ssicp.user_id)));

      p_out_return_code := 0;
      p_out_destination_contest_id := v_ssi_contest_id;
   EXCEPTION
      WHEN OTHERS
      THEN
         BEGIN
            p_out_return_code := 99;
            prc_execution_log_entry (
               'PRC_SSI_CONTEST_COPY',
               1,
               'ERROR',
               v_stage || p_in_ssi_contest_id || ' ' || SQLERRM,
               NULL);
         END;
   END prc_ssi_contest_copy;

   PROCEDURE prc_ssi_contest_uniqueness (
      p_in_ssi_contest_id             IN     NUMBER,
      p_in_issuance_number        IN     NUMBER,
      p_out_return_code                  OUT NUMBER,
      p_out_boolean_activity_desc        OUT NUMBER,
      p_out_boolean_obj_amount           OUT NUMBER,
      p_out_boolean_obj_payout           OUT NUMBER,
      p_out_boolean_obj_payout_desc      OUT NUMBER,
      p_out_boolean_obj_bonus            OUT NUMBER,
      p_out_boolean_obj_bonus_cap        OUT NUMBER,
      p_out_activity_description         OUT VARCHAR2,
      p_out_obj_amount                   OUT NUMBER,
      p_out_obj_payout                   OUT NUMBER,
      p_out_obj_payout_desc              OUT VARCHAR2,
      p_out_obj_bonus_increment          OUT NUMBER,
      p_out_obj_bonus_payout             OUT NUMBER,
      p_out_obj_bonus_cap                OUT NUMBER,
      p_out_total_obj_amount             OUT NUMBER,
      p_out_total_obj_payout             OUT NUMBER,
      p_out_total_obj_bonus_cap          OUT NUMBER)
   IS
   /*******************************************************************************
-- Purpose:
--
-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
Ravi Dhanekula             Initial creation
Suresh J      01/07/2015  Added 3 output fields - total_obj_amount,total_obj_payout,total_obj_bonus_cap
DeepikaM      11/02/2017  [G6-3725] Based on IS_SAME_OBJ_DESCRIPTION column, activity values should be picked up
*******************************************************************************/
   BEGIN
      WITH ssi_count
           AS (SELECT COUNT (
                        -- DISTINCT NVL (CASE WHEN INCLUDE_STACK_RANK =1 THEN sc.activity_description ELSE scp.activity_description END, --Bug # 61265 
                        DISTINCT NVL (CASE WHEN IS_SAME_OBJ_DESCRIPTION =1 THEN sc.activity_description ELSE scp.activity_description END, --11/02/2017 G6-3725
                                       DBMS_RANDOM.random))
                         AS ssi_count_activity_description,
                      COUNT (
                         DISTINCT NVL (scp.objective_amount,
                                       DBMS_RANDOM.random))
                         AS ssi_count_objective_amount,
                      COUNT (
                         DISTINCT NVL (scp.objective_payout,
                                       DBMS_RANDOM.random))
                         AS ssi_count_objective_payout,
                      COUNT (
                         DISTINCT NVL (scp.objective_payout_description,
                                       DBMS_RANDOM.random))
                         AS ssi_count_obj_payout_desc,
                      COUNT (
                         DISTINCT    NVL (scp.objective_bonus_increment,
                                          DBMS_RANDOM.random)
                                  || scp.objective_bonus_payout)
                         AS ssi_count_objective_bonus,
                      COUNT (
                         DISTINCT NVL (scp.objective_bonus_cap,
                                       DBMS_RANDOM.random))
                         AS ssi_count_objective_bonus_cap,
                      SUM (NVL (scp.objective_amount, 0)) AS total_obj_amount, --01/07/2015
                      SUM (NVL (scp.objective_payout, 0)) AS total_obj_payout, --01/07/2015
                      SUM (NVL (scp.objective_bonus_cap, 0))
                         AS total_obj_bonus_cap                   --01/07/2015
                 FROM ssi_contest_participant scp, ssi_contest sc
                WHERE     scp.ssi_contest_id = p_in_ssi_contest_id
                      AND NVL(scp.award_issuance_number,0) = NVL(p_in_issuance_number,NVL(scp.award_issuance_number,0))
                      AND scp.ssi_contest_id = sc.ssi_contest_id)
      SELECT CASE
                WHEN ssi_count.ssi_count_activity_description = 1 THEN 1
                ELSE 0
             END
                AS boolean_activity_desc,
             CASE
                WHEN ssi_count.ssi_count_objective_amount = 1 THEN 1
                ELSE 0
             END
                AS boolean_obj_amount,
             CASE
                WHEN ssi_count.ssi_count_objective_payout = 1 THEN 1
                ELSE 0
             END
                AS boolean_obj_payout,
             CASE
                WHEN ssi_count.ssi_count_obj_payout_desc = 1 THEN 1
                ELSE 0
             END
                AS Boolean_obj_payout_desc,
             CASE
                WHEN ssi_count.ssi_count_objective_bonus = 1 THEN 1
                ELSE 0
             END
                AS Boolean_objective_bonus,
             CASE
                WHEN ssi_count.ssi_count_objective_bonus_cap = 1 THEN 1
                ELSE 0
             END
                AS Boolean_objective_bonus_cap,
             CASE
                WHEN ssi_count.ssi_count_activity_description = 1
                THEN
                   activity_description
                ELSE
                   NULL
             END
                AS activity_description,
             CASE
                WHEN ssi_count.ssi_count_objective_amount = 1
                THEN
                   objective_amount
                ELSE
                   NULL
             END
                AS objective_amount,
             CASE
                WHEN ssi_count.ssi_count_objective_payout = 1
                THEN
                   objective_payout
                ELSE
                   NULL
             END
                AS objective_payout,
             CASE
                WHEN ssi_count.ssi_count_obj_payout_desc = 1
                THEN
                   objective_payout_description
                ELSE
                   NULL
             END
                AS objective_payout_description,
             CASE
                WHEN ssi_count.ssi_count_objective_bonus = 1
                THEN
                   objective_bonus_increment
                ELSE
                   NULL
             END
                AS objective_bonus_increment,
             CASE
                WHEN ssi_count.ssi_count_objective_bonus = 1
                THEN
                   objective_bonus_payout
                ELSE
                   NULL
             END
                AS objective_bonus_payout,
             CASE
                WHEN ssi_count.ssi_count_objective_bonus_cap = 1
                THEN
                   objective_bonus_cap
                ELSE
                   NULL
             END
                AS objective_bonus_cap,
             ssi_count.total_obj_amount,                          --01/07/2015
             ssi_count.total_obj_payout,                          --01/07/2015
             ssi_count.total_obj_bonus_cap                        --01/07/2015
        INTO p_out_boolean_activity_desc,
             p_out_boolean_obj_amount,
             p_out_boolean_obj_payout,
             p_out_boolean_obj_payout_desc,
             p_out_boolean_obj_bonus,
             p_out_boolean_obj_bonus_cap,
             p_out_activity_description,
             p_out_obj_amount,
             p_out_obj_payout,
             p_out_obj_payout_desc,
             p_out_obj_bonus_increment,
             p_out_obj_bonus_payout,
             p_out_obj_bonus_cap,
             p_out_total_obj_amount,                              --01/07/2015
             p_out_total_obj_payout,                              --01/07/2015
             p_out_total_obj_bonus_cap                            --01/07/2015
        FROM ssi_contest_participant SSI, SSI_COUNT
       WHERE ssi_contest_id = p_in_ssi_contest_id
       AND NVL(ssi.award_issuance_number,0) = NVL(p_in_issuance_number,NVL(ssi.award_issuance_number,0))
       AND ROWNUM < 2;

      p_out_return_code := 0;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := 99;
         prc_execution_log_entry (
            'PRC_SSI_CONTEST_UNIQUENESS',
            1,
            'ERROR',
               'Process failed for ssi_contest_id:  '
            || p_in_ssi_contest_id
            || ' '
            || SQLERRM,
            NULL);
   END;                                                        -- Package spec

   PROCEDURE prc_ssi_remove_activity (
      p_in_ssi_contest_id            IN     NUMBER,
      p_in_ssi_contest_activity_id   IN     NUMBER,
      p_in_user_id                   IN     NUMBER,
      p_out_return_code                 OUT NUMBER)
   IS
      /*******************************************************************************
   -- Purpose:
   -- This procedure is to remove a contest activity and re-sequence the remaining activities on a given contest.
   -- Person      Date        Comments
   -- ---------   ----------  -----------------------------------------------------
   Ravi Dhanekula 12/30/2014   Initial creation
   *******************************************************************************/
      v_sequence_number   SSI_CONTEST_ACTIVITY.SEQUENCE_NUMBER%TYPE;

      v_stage             VARCHAR2 (100);
   BEGIN
      v_stage := 'Get the sequence number of the activity to be deleted';

      SELECT sequence_number
        INTO v_sequence_number
        FROM ssi_contest_activity
       WHERE ssi_contest_activity_id = p_in_ssi_contest_activity_id;

      v_stage := 'Delete the Activity';

      DELETE FROM ssi_contest_activity
            WHERE ssi_contest_activity_id = p_in_ssi_contest_activity_id;

      v_stage := 'Re-order the sequence on the given contest';

      UPDATE ssi_contest_activity
         SET sequence_number = sequence_number - 1,
             version = version + 1,
             date_modified = SYSDATE,
             modified_by = p_in_user_id
       WHERE     ssi_contest_id = p_in_ssi_contest_id
             AND sequence_number > v_sequence_number;

      p_out_return_code := 0;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := 99;
         prc_execution_log_entry (
            'PRC_SSI_REMOVE_ACTIVITY',
            1,
            'ERROR',
               'Error at stage: '
            || v_stage
            || ' '
            || 'Process failed for ssi_contest_activity_id:  '
            || p_in_ssi_contest_activity_id
            || ' '
            || SQLERRM,
            NULL);
   END prc_ssi_remove_activity;

   PROCEDURE prc_ssi_contest_download (
      p_in_ssi_contest_id   IN     NUMBER,
      p_out_return_code        OUT NUMBER,
      p_out_result_set         OUT SYS_REFCURSOR)
   IS
   /*******************************************************************************
  -- Purpose:
  -- This procedure to fetch the data for the SSI Contest Download
  -- Person      Date        Comments
  -- ---------   ----------  -----------------------------------------------------
  Suresh J       01/14/2015  Initial creation
  Ravi Dhanekula 02/06/2015  Added ssi_contest_id and blank activity date as needed for progress file load.
  Swati          04/16/2015  Bug 61499 - Data collection spreadsheet is missing some participants
  Ravi Dhanekula 06/16/2015  Bug # 62884 - Progress download spreadsheet - Activity description is NULL
  *******************************************************************************/

   BEGIN
      OPEN p_out_result_set FOR
           SELECT sc.ssi_contest_id,--02/06/2015
                  au.user_name AS Login_ID,
                  au.first_name AS First_Name,
                  au.last_name AS Last_Name,
                  n.name AS org_unit,
                  NVL(uea.email_addr,' ') AS email_address,   --Bug 61814 04/23/2015
                  CASE
                     WHEN sc.contest_type = 2
                     THEN
                        sca.description
                     WHEN sc.contest_type = 4 AND sc.is_same_obj_description = 1 --06/16/2015
                     THEN
                        sc.activity_description
                     WHEN sc.contest_type = 4 AND sc.is_same_obj_description = 0 --06/16/2015
                     THEN
                        scp.activity_description
                     ELSE
                        sc.activity_description
                  END
                     AS activity_description,
                  NULL AS progress_total,
                  NULL AS activity_date --02/06/2015
             FROM ssi_contest sc,
                  ssi_contest_participant scp,
                  ssi_contest_activity sca,
                  application_user au,
                  user_email_address uea,
                  user_node un,
                  node n
            WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                  AND sc.ssi_contest_id = scp.ssi_contest_id
                  AND sc.ssi_contest_id = sca.ssi_contest_id(+)
                  AND scp.user_id = au.user_id
                  AND au.user_id = uea.user_id(+)--04/16/2015 Bug 61499
                  AND scp.user_id = un.user_id
                  AND un.node_id = n.node_id
                  AND sc.contest_type != 1
                  AND un.is_primary = 1
                  AND uea.is_primary(+) = 1--04/16/2015 Bug 61499
                  AND au.is_active = 1
         ORDER BY last_name ASC;

      p_out_return_code := 0;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := 99;
         prc_execution_log_entry (
            'PRC_SSI_CONTEST_DOWNLOAD',
            1,
            'ERROR',
               'Process failed for ssi_contest_id:  '
            || p_in_ssi_contest_id
            || ' '
            || SQLERRM,
            NULL);
   END;

   PROCEDURE prc_upd_ssi_contest_goal_perc (
      p_in_ssi_contest_id   IN     NUMBER,
      p_out_return_code        OUT NUMBER,
      p_out_error_message      OUT VARCHAR2)
   /*******************************************************************************
 -- Purpose:
 -- This procedure updates the goal_percentage column used for calculating goal amount for manager view page..
 -- Person      Date        Comments
 -- ---------   ----------  -----------------------------------------------------
 -- Ravi Dhanekula 2/2/2015  Initial Version
 -- Suresh J      2/10/2015  Modified goal percentage calculation for StepItUp
 --Ravi Dhanekula 03/27/2015 Bug # 60821.
 *******************************************************************************/
   IS
      v_contest_type   NUMBER (18);

      CURSOR cur_dtgt IS
       SELECT sca.ssi_contest_activity_id , ROUND (  goal_amount
              / ( ( min_qualifier --Bug # 60821
                 +   payout_cap_amount/ payout_amount
                   * increment_amount )
                   * (SELECT COUNT (1)
                        FROM ssi_contest_participant
                       WHERE ssi_contest_id = p_in_ssi_contest_id)),
                 6)
              * 100 goal_perc
  FROM ssi_contest_activity sca
 WHERE sca.ssi_contest_id = p_in_ssi_contest_id;

   BEGIN
      SELECT contest_type
        INTO v_contest_type
        FROM ssi_contest
       WHERE ssi_contest_id = p_in_ssi_contest_id;

      IF v_contest_type = 4
      THEN
         UPDATE ssi_contest
            SET goal_percentage =
                   (  SELECT   ROUND (sc.contest_goal / SUM (objective_amount),
                                      6)
                             * 100
                        FROM ssi_contest sc, ssi_contest_participant scp
                       WHERE sc.ssi_contest_id = scp.ssi_contest_id
                       AND sc.ssi_contest_id = p_in_ssi_contest_id
                    GROUP BY sc.contest_goal)
          WHERE ssi_contest_id = p_in_ssi_contest_id;
      ELSIF v_contest_type = 2
      THEN

      FOR rec_dtgt IN cur_dtgt LOOP
         UPDATE ssi_contest_activity
            SET goal_percentage = rec_dtgt.goal_perc
          WHERE ssi_contest_activity_id = rec_dtgt.ssi_contest_activity_id;

      END LOOP;
      ELSIF v_contest_type = 16
      THEN
UPDATE ssi_contest
SET goal_percentage = (
SELECT ROUND (sc.contest_goal/
SUM (CASE WHEN sc.sit_indv_baseline_type = 'no' THEN sc.goal_amount
          WHEN sc.sit_indv_baseline_type = 'percent' THEN (scp.siu_baseline_amount*(100+goal_amount)/100)
          WHEN sc.sit_indv_baseline_type = 'currency' THEN (scp.siu_baseline_amount+goal_amount) END ),6) * 100 AS goal_percentage
 FROM (
SELECT ssi_contest_id,sit_indv_baseline_type,contest_goal,
(SELECT goal_amount FROM (SELECT goal_amount,RANK() OVER (PARTITION BY ssi_contest_id ORDER BY  goal_amount DESC ) as rec_rank FROM ssi_contest_level scl WHERE ssi_contest_id = p_in_ssi_contest_id)
WHERE rec_rank =1) goal_amount
FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id ) sc,
ssi_contest_participant scp
WHERE scp.ssi_contest_id = sc.ssi_contest_id
GROUP BY sc.contest_goal )
WHERE ssi_contest_id = p_in_ssi_contest_id;
      END IF;


      p_out_return_code := 0;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := 99;
         p_out_error_message := SQLERRM;
   END;

   PROCEDURE prc_upd_ssi_contest_stackrank (
      p_in_ssi_contest_id   IN     NUMBER,
      p_out_return_code        OUT NUMBER,
      p_out_error_message      OUT VARCHAR2)
   /*******************************************************************************
 -- Purpose:
 -- This procedure updates the stack ranks for all for a given ssi_contest_id.
 -- Person          Date        Comments
 -- ---------       ----------  -----------------------------------------------------
 -- Ravi Dhanekula     02/3/2015      Initial Version
 -- Ravi Dhanekula     03/30/2015     Bug # 60845. Added lookup to stack_rank_order.
 -- Swati            03/31/2015    Bug 60827 - Creator view-DTGT-After Progress load-Extract report-'Total Potential Payout Value' is incorrect in extract report
 -- Swati            04/01/2015  Bug 60169 - Creator/Manager View-Objective contest-Export link-The extract displays wrong data
 --Ravi Dhanekula   04/08/2015  Bug # 61215 - Contest-Rank is getting skipped for the participant when the participant is inactive
 *******************************************************************************/
   IS
   BEGIN
   --Delete Inactive pax from stack rank.
DELETE FROM ssi_contest_pax_stack_rank s --Bug # 61215
WHERE ssi_contest_id = p_in_ssi_contest_id
AND EXISTS (SELECT * FROM application_user WHERE s.user_id = user_id AND is_active = 0);
   --Merge stack rank
      MERGE INTO ssi_contest_pax_stack_rank stack
           USING (
           SELECT sc.ssi_contest_id,
                         scp.user_id,
                         sca.ssi_contest_activity_id,
                         activity_amt,
                         CASE WHEN NVL(sc.stack_rank_order,'desc') = 'desc' THEN --04/01/2015  Bug 60169
                         RANK ()
                         OVER (
                            PARTITION BY scp.ssi_contest_id,
                            sca.ssi_contest_activity_id
                            ORDER BY NVL(activity_amt,-999999999999999999999999) desc)-- Bug # 61207
                          WHEN sc.stack_rank_order = 'asc' THEN
                         RANK ()
                         OVER (
                            PARTITION BY scp.ssi_contest_id,
                            sca.ssi_contest_activity_id
                            ORDER BY NVL(activity_amt,999999999999999999999999) asc)-- Bug # 61207
                         END AS RANK
                    FROM ssi_contest sc,
                         ssi_contest_pax_progress scpp,
                         ssi_contest_participant scp,
                         ssi_contest_activity sca,
                         application_user au --Bug # 61215
                   WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                         AND sc.contest_type <> 2 -- 03/31/2015 Bug 60827 This Query takes care of SR for Contest Other Than DTGT
                         AND scp.ssi_contest_id = scpp.ssi_contest_id(+)
                         AND scp.ssi_contest_id = sc.ssi_contest_id
                         AND scp.user_id = scpp.user_id(+)
                         AND scpp.ssi_contest_id = sca.ssi_contest_id(+)
                         AND sca.ssi_contest_activity_id(+) = scpp.ssi_contest_activity_id
                         AND scp.user_id = au.user_id
                         AND sc.status NOT IN ('pending','draft')
                         AND au.is_active = 1
           UNION -- 03/31/2015 Bug 60827 Below Query is only for DTGT
           SELECT t.ssi_contest_id,
                         t.user_id,
                         t.ssi_contest_activity_id,
                         scpp.activity_amt,
                         CASE WHEN NVL(t.stack_rank_order,'desc') = 'desc' THEN --04/01/2015  Bug 60169
                         RANK ()
                         OVER (
                            PARTITION BY t.ssi_contest_id,
                            t.ssi_contest_activity_id
                            ORDER BY NVL(activity_amt,-999999999999999999999999) desc)-- Bug # 61207
                         WHEN t.stack_rank_order = 'asc' THEN
                         RANK ()
                         OVER (
                            PARTITION BY t.ssi_contest_id,
                            t.ssi_contest_activity_id
                            ORDER BY NVL(activity_amt,999999999999999999999999) asc)-- Bug # 61207
                         END AS RANK
               FROM (SELECT  sc.ssi_contest_id,
                             scp.user_id,
                             sca.ssi_contest_activity_id,
                             sc.stack_rank_order
                        FROM ssi_contest sc,
                             ssi_contest_participant scp,
                             ssi_contest_activity sca,
                             application_user au--Bug # 61215
                       WHERE     sc.ssi_contest_id = p_in_ssi_contest_id
                             AND sc.contest_type = 2
                             AND scp.ssi_contest_id = sc.ssi_contest_id
                             AND sc.ssi_contest_id = sca.ssi_contest_id
                             AND sc.status NOT IN ('pending','draft')
                             AND scp.user_id = au.user_id
                             AND au.is_active = 1) t,
                    ssi_contest_pax_progress scpp
              WHERE     t.ssi_contest_id = scpp.ssi_contest_id(+)
                    AND t.user_id = scpp.user_id(+)
                    AND t.ssi_contest_activity_id = scpp.ssi_contest_activity_id(+)) s
              ON (    stack.ssi_contest_id = s.ssi_contest_id
                  AND NVL (stack.ssi_contest_activity_id, 0) =
                         NVL (s.ssi_contest_activity_id, 0)
                  AND stack.user_id = s.user_id)
      WHEN MATCHED
      THEN
         UPDATE SET
            stack_rank_position = s.RANK,
            date_modified = SYSDATE,
            modified_by = 5662,
            version = version + 1
                 WHERE NOT (DECODE (stack.stack_rank_position, s.RANK, 1, 0) =
                               1)
      WHEN NOT MATCHED
      THEN
         INSERT     (ssi_contest_pax_stack_rank_id,
                     ssi_contest_id,
                     user_id,
                     stack_rank_position,
                     ssi_contest_activity_id,
                     created_by,
                     date_created,
                     version)
             VALUES (ssi_contest_pax_stackrankpk_sq.NEXTVAL,
                     s.ssi_contest_id,
                     s.user_id,
                     s.RANK,
                     s.ssi_contest_activity_id,
                     5662,
                     SYSDATE,
                     0);

      p_out_return_code := 0;

      p_out_error_message := SQLERRM;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := 99;
         p_out_error_message := SQLERRM;
         prc_execution_log_entry (
            'prc_upd_ssi_contest_stackrank',
            1,
            'ERROR',
               'Process failed for ssi_contest_id:  '
            || p_in_ssi_contest_id
            || SQLERRM,
            NULL);
   END;

   PROCEDURE prc_ssi_remove_level (
      p_in_ssi_contest_id            IN     NUMBER,
      p_in_ssi_contest_level_id   IN     NUMBER,
      p_in_user_id                   IN     NUMBER,
      p_out_return_code                 OUT NUMBER)
   IS
      /*******************************************************************************
   -- Purpose:
   -- This procedure is to remove a contest level and re-sequence the remaining levels on a given contest.
   -- Person      Date        Comments
   -- ---------   ----------  -----------------------------------------------------
   Ravi Dhanekula 2/23/2015   Initial creation
   *******************************************************************************/
      v_sequence_number   SSI_CONTEST_LEVEL.SEQUENCE_NUMBER%TYPE;

      v_stage             VARCHAR2 (100);
   BEGIN
      v_stage := 'Get the sequence number of the level to be deleted';

      SELECT sequence_number
        INTO v_sequence_number
        FROM ssi_contest_level
       WHERE ssi_contest_level_id = p_in_ssi_contest_level_id;

      v_stage := 'Delete the Level';

      DELETE FROM ssi_contest_level
            WHERE ssi_contest_level_id = p_in_ssi_contest_level_id;

      v_stage := 'Re-order the sequence on the given contest';

      UPDATE ssi_contest_level
         SET sequence_number = sequence_number - 1,
             version = version + 1,
             date_modified = SYSDATE,
             modified_by = p_in_user_id
       WHERE     ssi_contest_id = p_in_ssi_contest_id
             AND sequence_number > v_sequence_number;

      p_out_return_code := 0;
   EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := 99;
         prc_execution_log_entry (
            'PRC_SSI_REMOVE_LEVEL',
            1,
            'ERROR',
               'Error at stage: '
            || v_stage
            || ' '
            || 'Process failed for ssi_contest_level_id:  '
            || p_in_ssi_contest_level_id
            || ' '
            || SQLERRM,
            NULL);
   END prc_ssi_remove_level;
   
PROCEDURE PRC_SSI_CONTEST_PAX_PAYOUT
(     p_in_ssi_contest_id         IN NUMBER,
      p_in_user_id                IN NUMBER,
      p_in_award_issuance_number  IN NUMBER,
      p_in_csv_user_ids           IN VARCHAR2,
      p_in_csv_payout_amounts     IN VARCHAR2,
      p_out_return_code          OUT NUMBER
      )
IS
   /*******************************************************************************
   -- Purpose: To issue Contest payouts. This procedure would insert records for the payout and journals.
   --
   -- Person                    Date         Comments
   -- -----------                --------    -----------------------------------------------------
   -- Suresh J                  03/03/2015   Initlal Creation.
   -- Ravi Dhanekula            03/05/2015   Added code for inserting records to participant_badge
   -- Suresh J                  04/08/2015   Bug 61174 - Error while issuing payout
   -- Suresh J                  04/14/2015   Bug #61357 - Error while issuing payout in SR block
   -- Ravi Dhanekula            05/04/2015   Bug # 62027 - In objective contest deposit amount is displaying wrong in the statement page
   -- Swati                     05/06/2015   SSI Phase2 CR Award Activity
   -- Swati                     05/12/2015   Bug 62117 - Participant - Step it up - Payout is deposited wrong points
   -- KrishnaDeepika            06/03/2015   Added filter to cur_stack_rank
   --Suresh J                   06/12/2015   Stack Rank Payout Approvals and Issue Payout changes   
   --Ravi Dhanekula            07/22/2015   Bug # 63336 - Changed the promotion_id populated in journal table to the ssi promotion live at payout time.
                               08/05/2015  Bug # 63584 Step it up - Bonus is payed out even though participant did not reach the highest level.
   --Suresh J                  08/10/2015  Bug 63613 - SSI Contest Payout Deposit process throws an exception error                                       
   --nagarajs                  05/26/2016  G5.6.3 changes
   --Ravi Dhanekula            11/22/2016 Changed journal description to include contest owner name too. Also added 10 bill codes.
   --nagarajs                  03/29/2017  G6.2 - Populte 0 value to journal.transaction_amt if participant.is_opt_out_awards=1
   --Loganathan 			   07/01/2019  Bug 79078 - SSI Bonus Point Incorrect Payout Amount
 *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_SSI_CONTEST_PAX_PAYOUT';
  v_release_level         execution_log.release_level%type := '1';
  v_execution_log_msg     execution_log.text_line%TYPE;

  v_contest_type      ssi_contest.contest_type%type;
  v_journal_id        journal.journal_id%type;
  v_contest_name      VARCHAR2(100);
  v_payout_type       ssi_contest.payout_type%type;
  v_bill_payout_code_type1  ssi_contest_bill_code.track_bills_by%type;
  v_bill_payout_code_type2  ssi_contest_bill_code.track_bills_by%type;
  v_bill_payout_code_type3  ssi_contest_bill_code.track_bills_by%type;
  v_bill_payout_code_type4  ssi_contest_bill_code.track_bills_by%type;
  v_bill_payout_code_type5  ssi_contest_bill_code.track_bills_by%type;
  v_bill_payout_code_type6  ssi_contest_bill_code.track_bills_by%type;
  v_bill_payout_code_type7  ssi_contest_bill_code.track_bills_by%type;
  v_bill_payout_code_type8  ssi_contest_bill_code.track_bills_by%type;
  v_bill_payout_code_type9  ssi_contest_bill_code.track_bills_by%type;
  v_bill_payout_code_type10  ssi_contest_bill_code.track_bills_by%type;
  v_bill_payout_code_1  ssi_contest_bill_code.bill_code%type;
  v_bill_payout_code_2  ssi_contest_bill_code.bill_code%type;
  v_bill_payout_code_3  ssi_contest_bill_code.bill_code%type;
  v_bill_payout_code_4  ssi_contest_bill_code.bill_code%type;
  v_bill_payout_code_5  ssi_contest_bill_code.bill_code%type;
  v_bill_payout_code_6  ssi_contest_bill_code.bill_code%type;
  v_bill_payout_code_7  ssi_contest_bill_code.bill_code%type;
  v_bill_payout_code_8  ssi_contest_bill_code.bill_code%type;
  v_bill_payout_code_9  ssi_contest_bill_code.bill_code%type;
  v_bill_payout_code_10  ssi_contest_bill_code.bill_code%type;
  v_creator_user_id     ssi_contest.created_by%type;
  v_contest_owner_id     ssi_contest.contest_owner_id%type;  --05/21/2015
  v_contest_owner_name   VARCHAR2(250);
  v_creator_node_name   node.name%type;
  v_promotion_id        promotion.promotion_id%type; --SSI Phase2
  v_has_tie            NUMBER;   --06/12/2015
  v_creator_user_name             application_user.user_name%type;
  v_creator_country_code          country.country_code%type;
  v_creator_department            participant_employer.department_type%type;
  v_creator_custom_value_list     VARCHAR2(500);  
    v_characteristic_value1             user_characteristic.characteristic_value%type;
    v_characteristic_value2             user_characteristic.characteristic_value%type;
    v_characteristic_value3             user_characteristic.characteristic_value%type;
    v_characteristic_value4             user_characteristic.characteristic_value%type;
    v_characteristic_value5             user_characteristic.characteristic_value%type;
    v_characteristic_value6             user_characteristic.characteristic_value%type;
    v_characteristic_value7             user_characteristic.characteristic_value%type;
    v_characteristic_value8             user_characteristic.characteristic_value%type;
    v_characteristic_value9             user_characteristic.characteristic_value%type;
    v_characteristic_value10            user_characteristic.characteristic_value%type;
  
  --Contest Types
  c_award_them_now   constant number := 1;
  c_stack_rank       constant number := 8;
  c_do_this_get_that constant number := 2;
  c_objectives       constant number := 4;
  c_step_it_up       constant number := 16;

  --Bill Codes
  c_bill_code_orgUnitName    CONSTANT VARCHAR2(50)  := 'orgUnitName';
  c_bill_code_department     CONSTANT VARCHAR2(50)  := 'department';
  c_bill_code_countryCode    CONSTANT VARCHAR2(50)  := 'countryCode';
  c_bill_code_userName       CONSTANT VARCHAR2(50)  := 'userName';
  c_bill_code_customValue    CONSTANT VARCHAR2(50)  := 'customValue';  
  c_bill_code_characteristic CONSTANT VARCHAR2(50)  := 'characteristic';

  c_track_bill_type_participant  CONSTANT VARCHAR2(30)  := 'participant';
  c_track_bill_type_creator      CONSTANT VARCHAR2(30)  := 'creator';
  c_track_bill_type_other        CONSTANT VARCHAR2(30)  := 'other';   

  -- EXCEPTIONS
  exit_program_exception      EXCEPTION;

  CURSOR cur_objectives (v_ssi_contest_id ssi_contest.ssi_contest_id%TYPE) IS
       SELECT  sc.ssi_contest_id,
               scpp.user_id,
               n.name pax_node_name,
                CASE WHEN p.is_opt_out_of_awards = 1 THEN 0  --03/29/2017
                     WHEN CASE
                                   WHEN scpp.activity_amt >= scp.objective_amount
                                   THEN
                                           NVL(FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment) * scp.objective_bonus_payout,0)
                                   ELSE
                                      0
                                END  > OBJECTIVE_BONUS_CAP THEN scp.objective_payout + OBJECTIVE_BONUS_CAP
                                ELSE CASE
                                   WHEN scpp.activity_amt >= scp.objective_amount
                                   THEN
                                           scp.objective_payout + NVL(FLOOR((scpp.activity_amt - scp.objective_amount)/ scp.objective_bonus_increment)* scp.objective_bonus_payout,0)
                                   ELSE
                                      0
                                END END AS total_payout,--05/04/2015
                sc.badge_rule_id,br.promotion_id,
                pb.user_name,
                pb.country_code,
                pb.department,
                pb.custom_value_list,
               characteristic1_value, 
               characteristic2_value, 
               characteristic3_value, 
               characteristic4_value, 
               characteristic5_value, 
               characteristic6_value, 
               characteristic7_value, 
               characteristic8_value, 
               characteristic9_value, 
               characteristic10_value  
          FROM
            ssi_contest sc,
                 ssi_contest_pax_progress scpp,
                 ssi_contest_participant scp
                 , participant p   --03/29/2017
                 ,user_node un
                 ,node n,
                 badge_rule br,
                 (SELECT au.user_id,
                           au.user_name,
                           c.country_code,
                           emp.department_type as department,
                           cb.custom_value_list,
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
                    FROM application_user au,
                         user_address ua,
                         country c,
                         (SELECT * FROM ( -- rank records by termination date and employer index in reverse order
                               SELECT RANK ()
                                      OVER (
                                         PARTITION BY pe.user_id
                                         ORDER BY
                                            pe.termination_date DESC,
                                            pe.participant_employer_index DESC)
                                         AS rec_rank,
                                      pe.*
                                 FROM participant_employer pe) r
                        -- the current employment record has the lowest ranking
                        WHERE r.rec_rank = 1) emp,
                        ssi_contest sc,
                        (SELECT ssi_contest_id,
                           bill_code,
                           track_bills_by,
                           LISTAGG(custom_value , ', ') WITHIN GROUP (ORDER BY custom_value) AS custom_value_list
                        FROM ssi_contest_bill_code 
                        WHERE bill_code = c_bill_code_customValue  
                        AND ssi_contest_id = v_ssi_contest_id
                        AND track_bills_by = c_track_bill_type_participant
                        GROUP BY ssi_contest_id,
                                 bill_code,
                                 track_bills_by,
                                 custom_value
                         ) cb
                 ,( SELECT ssi_contest_id, 
                           user_id, 
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
                      FROM (
                      SELECT sbc.ssi_contest_id,
                                   uc.characteristic_value characteristic_values,
                                   sbc.sort_order+1 char_num,
                                   user_id
                              FROM user_characteristic uc,
                                   ssi_contest_bill_code sbc
                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
                                      sbc.bill_code IS NOT NULL                   AND
                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)                      
--                      SELECT sbc.ssi_contest_id,
--                                   uc.characteristic_value characteristic_values,
--                                   DENSE_RANK ()
--                                   OVER (PARTITION BY user_id ORDER BY uc.user_characteristic_id) char_num,
--                                   user_id
--                              FROM user_characteristic uc,
--                                   ssi_contest_bill_code sbc
--                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
--                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
--                                      sbc.bill_code IS NOT NULL                   AND
--                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)
                               ) PIVOT ( MIN (UPPER(characteristic_values)) VALUE
                                                                               FOR (char_num)
                                                                                IN (1 AS characteristic1,
                                                                                    2 AS characteristic2,
                                                                                    3 AS characteristic3,
                                                                                    4 AS characteristic4,
                                                                                    5 AS characteristic5,
                                                                                    6 AS characteristic6,
                                                                                    7 AS characteristic7,
                                                                                    8 AS characteristic8,
                                                                                    9 AS characteristic9,
                                                                                    10 AS characteristic10 ))  ) uc
            WHERE au.user_id        = ua.user_id            AND
                  ua.country_id     = c.country_id          AND
                  ua.is_primary     = 1                     AND
                  ua.user_id        = emp.user_id           AND
                  sc.ssi_contest_id = v_ssi_contest_id  AND
                  ua.user_id        = uc.user_id        (+) AND     
                  sc.ssi_contest_id = cb.ssi_contest_id (+)) pb
           WHERE     sc.ssi_contest_id = v_ssi_contest_id
                 AND sc.ssi_contest_id = scp.ssi_contest_id
                 AND scp.ssi_contest_id = scpp.ssi_contest_id
                 AND scp.user_id = scpp.user_id
                 AND scpp.user_id = p.user_id --03/29/2017
                 AND scpp.activity_amt >= scp.objective_amount
                 AND un.node_id = n.node_id
                 AND un.status = 1
                 AND un.is_primary = 1
                 AND un.user_id = scpp.user_id
                 AND sc.badge_rule_id = br.badge_rule_id(+)
                 AND scp.user_id = pb.user_id (+);

  CURSOR cur_dtgt (v_ssi_contest_id ssi_contest.ssi_contest_id%TYPE) IS
  SELECT ssi_contest_id,ssi_contest_activity_id,activity_name,user_id, CASE WHEN is_opt_out_of_awards = 1 THEN 0  --03/29/2017
                                                                        ELSE payout_value
                                                                       END AS payout_value
         ,pax_node_name,badge_rule_id,promotion_id  --08/10/2015
         ,user_name,
         country_code,
         department,
         custom_value_list,
           characteristic1_value, 
           characteristic2_value, 
           characteristic3_value, 
           characteristic4_value, 
           characteristic5_value, 
           characteristic6_value, 
           characteristic7_value, 
           characteristic8_value, 
           characteristic9_value, 
           characteristic10_value  
  FROM
       (SELECT
             ssi_contest_id,
             ssi_contest_activity_id,
             activity_name,
             user_id,
             is_opt_out_of_awards, --03/29/2017
            SUM(FLOOR((CASE WHEN activity_amt <= maximum_payout_activity THEN activity_amt
                       ELSE  maximum_payout_activity END- min_qualifier)/for_every)* increment_payout) AS payout_value
            ,pax_node_name,badge_rule_id,promotion_id,
            user_name,
            country_code,
            department,
            custom_value_list,
           characteristic1_value, 
           characteristic2_value, 
           characteristic3_value, 
           characteristic4_value, 
           characteristic5_value, 
           characteristic6_value, 
           characteristic7_value, 
           characteristic8_value, 
           characteristic9_value, 
           characteristic10_value  

        FROM (
            SELECT sc.ssi_contest_id,
                   sca.ssi_contest_activity_id,
                   sca.description as activity_name,
                   scp.user_id,
                   p.is_opt_out_of_awards, --03/29/2017
                   n.name pax_node_name,
                   scpp.activity_amt,
                     sca.increment_amount for_every,
                     sca.payout_amount increment_payout,
                     sca.min_qualifier,
                     (scpp.activity_amt - sca.min_qualifier) qualified_activity,
                    sca.min_qualifier + sca.payout_cap_amount/payout_amount*increment_amount AS maximum_payout_activity,
                    sc.badge_rule_id,br.promotion_id,
                   pb.user_name,
                   pb.country_code,
                   pb.department,
                   pb.custom_value_list,
                   characteristic1_value, 
                   characteristic2_value, 
                   characteristic3_value, 
                   characteristic4_value, 
                   characteristic5_value, 
                   characteristic6_value, 
                   characteristic7_value, 
                   characteristic8_value, 
                   characteristic9_value, 
                   characteristic10_value  
              FROM ssi_contest sc, ssi_contest_activity sca, ssi_contest_participant scp,
              participant p,   --03/29/2017
              ssi_contest_pax_progress scpp
              ,user_node un
              ,node n, badge_rule br
              ,(SELECT au.user_id,
                           au.user_name,
                           c.country_code,
                           emp.department_type as department,
                           cb.custom_value_list,
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
                    FROM application_user au,
                         user_address ua,
                         country c,
                         (SELECT * FROM ( -- rank records by termination date and employer index in reverse order
                               SELECT RANK ()
                                      OVER (
                                         PARTITION BY pe.user_id
                                         ORDER BY
                                            pe.termination_date DESC,
                                            pe.participant_employer_index DESC)
                                         AS rec_rank,
                                      pe.*
                                 FROM participant_employer pe) r
                        -- the current employment record has the lowest ranking
                        WHERE r.rec_rank = 1) emp,
                        ssi_contest sc,
                        (SELECT ssi_contest_id,
                           bill_code,
                           track_bills_by,
                           LISTAGG(custom_value , ', ') WITHIN GROUP (ORDER BY custom_value) AS custom_value_list
                        FROM ssi_contest_bill_code 
                        WHERE bill_code = c_bill_code_customValue  
                        AND ssi_contest_id = v_ssi_contest_id
                        AND track_bills_by = c_track_bill_type_participant
                        GROUP BY ssi_contest_id,
                                 bill_code,
                                 track_bills_by,
                                 custom_value
                         ) cb
                 ,( SELECT ssi_contest_id, 
                           user_id, 
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
                      FROM (SELECT sbc.ssi_contest_id,
                                   uc.characteristic_value characteristic_values,
                                   sbc.sort_order+1 char_num,
                                   user_id
                              FROM user_characteristic uc,
                                   ssi_contest_bill_code sbc
                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
                                      sbc.bill_code IS NOT NULL                   AND
                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)
--                                      SELECT sbc.ssi_contest_id,
--                                   uc.characteristic_value characteristic_values,
--                                   DENSE_RANK ()
--                                   OVER (PARTITION BY user_id ORDER BY uc.user_characteristic_id) char_num,
--                                   user_id
--                              FROM user_characteristic uc,
--                                   ssi_contest_bill_code sbc
--                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
--                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
--                                      sbc.bill_code IS NOT NULL                   AND
--                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)
                               ) PIVOT ( MIN (UPPER(characteristic_values)) VALUE
                                                                               FOR (char_num)
                                                                                IN (1 AS characteristic1,
                                                                                    2 AS characteristic2,
                                                                                    3 AS characteristic3,
                                                                                    4 AS characteristic4,
                                                                                    5 AS characteristic5,
                                                                                    6 AS characteristic6,
                                                                                    7 AS characteristic7,
                                                                                    8 AS characteristic8,
                                                                                    9 AS characteristic9,
                                                                                    10 AS characteristic10 ))  ) uc
            WHERE au.user_id        = ua.user_id            AND
                  ua.country_id     = c.country_id          AND
                  ua.is_primary     = 1                     AND
                  ua.user_id        = emp.user_id           AND
                  sc.ssi_contest_id = v_ssi_contest_id   AND
                  ua.user_id        = uc.user_id        (+) AND     
                  sc.ssi_contest_id = cb.ssi_contest_id (+)) pb
             WHERE     sc.ssi_contest_id = v_ssi_contest_id
                   AND sc.ssi_contest_id = sca.ssi_contest_id
                   AND sc.ssi_contest_id = scp.ssi_contest_id
                   AND scp.user_id = scpp.user_id
                   AND scp.user_id = p.user_id --03/29/2017
                   AND sca.ssi_contest_activity_id = scpp.ssi_contest_activity_id
                   AND scpp.activity_amt > sca.min_qualifier
                   and un.node_id = n.node_id
                   and un.status = 1
                   and un.is_primary = 1
                   and un.user_id = scpp.user_id
                   AND sc.badge_rule_id = br.badge_rule_id(+)
                   AND scp.user_id      = pb.user_id      (+)
                   )
             GROUP BY
             ssi_contest_id,
             ssi_contest_activity_id,
             activity_name,
             user_id
             ,is_opt_out_of_awards --03/29/2017
             ,pax_node_name,badge_rule_id,promotion_id,
               user_name,
               country_code,
               department,
               custom_value_list,
               characteristic1_value, 
               characteristic2_value, 
               characteristic3_value, 
               characteristic4_value, 
               characteristic5_value, 
               characteristic6_value, 
               characteristic7_value, 
               characteristic8_value, 
               characteristic9_value, 
               characteristic10_value  
             ) t where t.payout_value <> 0 ;  --08/10/2015

  CURSOR cur_step_it_up (v_ssi_contest_id ssi_contest.ssi_contest_id%TYPE) IS
  SELECT ssi_contest_id,
           user_id,
           pax_node_name,
            CASE WHEN is_opt_out_of_awards = 1 THEN 0  --03/29/2017
            ELSE SUM(level_payout+ CASE WHEN level_completed = max_level THEN bonus_payout ELSE 0 END) END AS total_payout,--Bug # 63584 08/05/2015
           activity_measure_type,
           badge_rule_id,
           promotion_id,
           user_name,
           country_code,
           department,
           custom_value_list,
           characteristic1_value, 
           characteristic2_value, 
           characteristic3_value, 
           characteristic4_value, 
           characteristic5_value, 
           characteristic6_value, 
           characteristic7_value, 
           characteristic8_value, 
           characteristic9_value, 
           characteristic10_value  
           FROM (           
           SELECT          sc.ssi_contest_id,
                           scp.user_id,
                           p.is_opt_out_of_awards, --03/29/2017
                           n.name pax_node_name,
                           scpp.activity_amt,
                           sc.activity_measure_type,
                           RANK() OVER (PARTITION BY scp.user_id ORDER BY
                           CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                                WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                                WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END  DESC) as rec_rank,
                           CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.payout_amount
                                WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.payout_amount
                                WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.payout_amount
                           ELSE 0 END level_payout,
                           CASE WHEN
                           (CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap * sit_bonus_payout
                                WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment) * sit_bonus_payout--(scpp.activity_amt - scl.goal_amount)* sit_bonus_payout/sit_bonus_increment --07/01/2019#Bug 79078
                                WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap * sit_bonus_payout
                                WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment THEN (scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))* sit_bonus_payout/sit_bonus_increment
                                WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap * sit_bonus_payout
                                WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN (scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))* sit_bonus_payout/sit_bonus_increment
                           ELSE 0
                           END) > sc.sit_bonus_cap THEN sc.sit_bonus_cap
                           ELSE
                           (CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt > scl.goal_amount+ sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap * sit_bonus_payout
                                WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount AND scpp.activity_amt <= scl.goal_amount+ sit_bonus_cap*sit_bonus_increment THEN FLOOR((scpp.activity_amt - scl.goal_amount)/sit_bonus_increment) * sit_bonus_payout --(scpp.activity_amt - scl.goal_amount)* sit_bonus_payout/sit_bonus_increment  --07/01/2019#Bug 79078
                                WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt > (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap * sit_bonus_payout
                                WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) AND scpp.activity_amt <= (scp.siu_baseline_amount*(100+goal_amount)/100) + sit_bonus_cap*sit_bonus_increment THEN (scpp.activity_amt - (scp.siu_baseline_amount*(100+goal_amount)/100))* sit_bonus_payout/sit_bonus_increment
                                WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt > (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN sit_bonus_cap * sit_bonus_payout
                                WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) AND scpp.activity_amt <= (scp.siu_baseline_amount+goal_amount) + sit_bonus_cap*sit_bonus_increment THEN (scpp.activity_amt - (scp.siu_baseline_amount+goal_amount))* sit_bonus_payout/sit_bonus_increment
                           ELSE 0
                           END)
                           END bonus_payout, --05/12/2015 Bug 62117
                            (SELECT MAX(sequence_number) FROM ssi_contest_level WHERE ssi_contest_id = v_ssi_contest_id) max_level,
                            CASE WHEN sc.sit_indv_baseline_type = 'no' AND scpp.activity_amt >= scl.goal_amount THEN scl.sequence_number
                      WHEN sc.sit_indv_baseline_type = 'percent' AND scpp.activity_amt >= (scp.siu_baseline_amount*(100+goal_amount)/100) THEN scl.sequence_number
                      WHEN sc.sit_indv_baseline_type = 'currency' AND scpp.activity_amt >= (scp.siu_baseline_amount+goal_amount) THEN scl.sequence_number ELSE 0 END level_completed,
--                           sc.badge_rule_id,    --04/08/2015
                           scl.badge_rule_id,     --04/08/2015
                           br.promotion_id,
                           pb.user_name,
                           pb.country_code,
                           pb.department,
                           pb.custom_value_list,
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
          FROM ssi_contest sc,
               ssi_contest_level scl,
               ssi_contest_participant scp,
               participant p,   --03/29/2017
               ssi_contest_pax_progress scpp
              ,user_node un
              ,node n, badge_rule br
              ,(SELECT au.user_id,
                           au.user_name,
                           c.country_code,
                           emp.department_type as department,
                           cb.custom_value_list,
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
                    FROM application_user au,
                         user_address ua,
                         country c,
                         (SELECT * FROM ( -- rank records by termination date and employer index in reverse order
                               SELECT RANK ()
                                      OVER (
                                         PARTITION BY pe.user_id
                                         ORDER BY
                                            pe.termination_date DESC,
                                            pe.participant_employer_index DESC)
                                         AS rec_rank,
                                      pe.*
                                 FROM participant_employer pe) r
                        -- the current employment record has the lowest ranking
                        WHERE r.rec_rank = 1) emp,
                        ssi_contest sc,
                        (SELECT ssi_contest_id,
                           bill_code,
                           track_bills_by,
                           LISTAGG(custom_value , ', ') WITHIN GROUP (ORDER BY custom_value) AS custom_value_list
                        FROM ssi_contest_bill_code 
                        WHERE bill_code = c_bill_code_customValue  
                        AND ssi_contest_id = v_ssi_contest_id
                        AND track_bills_by = c_track_bill_type_participant
                        GROUP BY ssi_contest_id,
                                 bill_code,
                                 track_bills_by,
                                 custom_value
                         ) cb
                 ,( SELECT ssi_contest_id, 
                           user_id, 
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
                      FROM (
                      SELECT sbc.ssi_contest_id,
                                   uc.characteristic_value characteristic_values,
                                   sbc.sort_order+1 char_num,
                                   user_id
                              FROM user_characteristic uc,
                                   ssi_contest_bill_code sbc
                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
                                      sbc.bill_code IS NOT NULL                   AND
                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)
--                      SELECT sbc.ssi_contest_id,
--                                   uc.characteristic_value characteristic_values,
--                                   DENSE_RANK ()
--                                   OVER (PARTITION BY user_id ORDER BY uc.user_characteristic_id) char_num,
--                                   user_id
--                              FROM user_characteristic uc,
--                                   ssi_contest_bill_code sbc
--                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
--                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
--                                      sbc.bill_code IS NOT NULL                   AND
--                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)
                               ) PIVOT ( MIN (UPPER(characteristic_values)) VALUE
                                                                               FOR (char_num)
                                                                                IN (1 AS characteristic1,
                                                                                    2 AS characteristic2,
                                                                                    3 AS characteristic3,
                                                                                    4 AS characteristic4,
                                                                                    5 AS characteristic5,
                                                                                    6 AS characteristic6,
                                                                                    7 AS characteristic7,
                                                                                    8 AS characteristic8,
                                                                                    9 AS characteristic9,
                                                                                    10 AS characteristic10 ))  ) uc
            WHERE au.user_id        = ua.user_id            AND
                  ua.country_id     = c.country_id          AND
                  ua.is_primary     = 1                     AND
                  ua.user_id        = emp.user_id           AND
                  sc.ssi_contest_id = v_ssi_contest_id  AND
                  ua.user_id        = uc.user_id        (+) AND     
                  sc.ssi_contest_id = cb.ssi_contest_id (+)) pb
          WHERE    sc.ssi_contest_id = v_ssi_contest_id
               AND sc.ssi_contest_id = scl.ssi_contest_id
               AND sc.ssi_contest_id = scp.ssi_contest_id
               AND scp.user_id = scpp.user_id
               AND scp.user_id = p.user_id --03/29/2017
               AND scp.ssi_contest_id = scpp.ssi_contest_id
               AND scpp.activity_amt >= scl.goal_amount
               and un.node_id = n.node_id
               and un.status = 1
               and un.is_primary = 1
               and un.user_id = scpp.user_id
               AND scl.badge_rule_id = br.badge_rule_id(+)
               AND scp.user_id       = pb.user_id      (+)                
               ) WHERE rec_rank = 1 GROUP BY ssi_contest_id,user_id,activity_measure_type,pax_node_name,badge_rule_id,promotion_id,
                                             user_name,country_code,department,custom_value_list,
                                               is_opt_out_of_awards, --03/29/2017
                                               characteristic1_value, 
                                               characteristic2_value, 
                                               characteristic3_value, 
                                               characteristic4_value, 
                                               characteristic5_value, 
                                               characteristic6_value, 
                                               characteristic7_value, 
                                               characteristic8_value, 
                                               characteristic9_value, 
                                               characteristic10_value ; 

    
  CURSOR cur_stack_rank (v_ssi_contest_id ssi_contest.ssi_contest_id%TYPE) IS
      SELECT  sc.ssi_contest_id ,
                           scpp.user_id,
                            CASE WHEN p.is_opt_out_of_awards = 1 THEN 0  --03/29/2017
                            ELSE scsrp.payout_amount  END as payout_amt,
                           scsrp.payout_desc as payout_description,
                           scpsr.stack_rank_position,
                           scpp.activity_amt,
                           n.name as pax_node_name,
                           scsrp.badge_rule_id,
                           br.promotion_id,
                           pb.user_name,
                           pb.country_code,
                           pb.department,
                           pb.custom_value_list,
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
                    FROM ssi_contest_pax_stack_rank scpsr,
                         ssi_contest_pax_progress scpp,
                         participant p,   --03/29/2017
                         ssi_contest_sr_payout scsrp,
                         ssi_contest sc,
                         badge_rule br,
                         user_node un,
                         node n
                        ,(SELECT au.user_id,
                                       au.user_name,
                                       c.country_code,
                                       emp.department_type as department,
                                       cb.custom_value_list,
                                       characteristic1_value, 
                                       characteristic2_value, 
                                       characteristic3_value, 
                                       characteristic4_value, 
                                       characteristic5_value, 
                                       characteristic6_value, 
                                       characteristic7_value, 
                                       characteristic8_value, 
                                       characteristic9_value, 
                                       characteristic10_value  
                                FROM application_user au,
                                     user_address ua,
                                     country c,
                                     (SELECT * FROM ( -- rank records by termination date and employer index in reverse order
                                           SELECT RANK ()
                                                  OVER (
                                                     PARTITION BY pe.user_id
                                                     ORDER BY
                                                        pe.termination_date DESC,
                                                        pe.participant_employer_index DESC)
                                                     AS rec_rank,
                                                  pe.*
                                             FROM participant_employer pe) r
                                    -- the current employment record has the lowest ranking
                                    WHERE r.rec_rank = 1) emp,
                                    ssi_contest sc,
                                    (SELECT ssi_contest_id,
                                       bill_code,
                                       track_bills_by,
                                       LISTAGG(custom_value , ', ') WITHIN GROUP (ORDER BY custom_value) AS custom_value_list
                                    FROM ssi_contest_bill_code 
                                    WHERE bill_code = c_bill_code_customValue  
                                    AND ssi_contest_id = v_ssi_contest_id
                                    AND track_bills_by = c_track_bill_type_participant
                                    GROUP BY ssi_contest_id,
                                             bill_code,
                                             track_bills_by,
                                             custom_value
                                     ) cb
                 ,( SELECT ssi_contest_id, 
                           user_id, 
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
                      FROM (
                      SELECT sbc.ssi_contest_id,
                                   uc.characteristic_value characteristic_values,
                                   sbc.sort_order+1 char_num,
                                   user_id
                              FROM user_characteristic uc,
                                   ssi_contest_bill_code sbc
                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
                                      sbc.bill_code IS NOT NULL                   AND
                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)                                        
--                                      SELECT sbc.ssi_contest_id,
--                                   uc.characteristic_value characteristic_values,
--                                   DENSE_RANK ()
--                                   OVER (PARTITION BY user_id ORDER BY uc.user_characteristic_id) char_num,
--                                   user_id
--                              FROM user_characteristic uc,
--                                   ssi_contest_bill_code sbc
--                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
--                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
--                                      sbc.bill_code IS NOT NULL                   AND
--                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)
                               ) PIVOT ( MIN (UPPER(characteristic_values)) VALUE
                                                                               FOR (char_num)
                                                                                IN (1 AS characteristic1,
                                                                                    2 AS characteristic2,
                                                                                    3 AS characteristic3,
                                                                                    4 AS characteristic4,
                                                                                    5 AS characteristic5,
                                                                                    6 AS characteristic6,
                                                                                    7 AS characteristic7,
                                                                                    8 AS characteristic8,
                                                                                    9 AS characteristic9,
                                                                                    10 AS characteristic10 ))  ) uc
                WHERE au.user_id        = ua.user_id            AND
                      ua.country_id     = c.country_id          AND
                      ua.is_primary     = 1                     AND
                      ua.user_id        = emp.user_id           AND
                      sc.ssi_contest_id = v_ssi_contest_id  AND
                      ua.user_id        = uc.user_id        (+) AND     
                      sc.ssi_contest_id = cb.ssi_contest_id (+)) pb
                   WHERE     scpsr.ssi_contest_id = v_ssi_contest_id
                         AND scpsr.ssi_contest_id = sc.ssi_contest_id  --06/03/2015 
                         AND scpsr.user_id = scpp.user_id
                         AND scpp.user_id = p.user_id --03/29/2017
                         AND sc.ssi_contest_id = scpp.ssi_contest_id
                         AND scpsr.user_id = un.user_id
                         AND un.node_id = n.node_id
                         AND un.status = 1
                         AND un.is_primary = 1
                         AND sc.ssi_contest_id = scsrp.ssi_contest_id
                         AND scsrp.rank_position = scpsr.stack_rank_position
                         AND scsrp.badge_rule_id = br.badge_rule_id  (+)
                         AND scpsr.user_id       = pb.user_id        (+)
                        AND scpp.activity_amt >= NVL(sc.stack_rank_qualifier_amount,0);                        
                       /* UNION ALL   06/12/2015 Commented out this part of the union as this will never be executed, because we have seperate cusror for the Tie secario.
                        SELECT scsrp.ssi_contest_id,
       un.user_id AS user_id,
       TO_NUMBER(payout_amt_csv.payout_amt) AS payout_amt,
       n.name AS pax_node_name,
       scsrp.badge_rule_id,
       br.promotion_id
  FROM (SELECT ROWNUM row_num, COLUMN_VALUE AS user_id
          FROM TABLE (get_array_varchar (v_csv_user_id))) user_id_csv,
       (SELECT ROWNUM row_num, COLUMN_VALUE AS payout_amt
          FROM TABLE (get_array_varchar (v_csv_payout_amt))) payout_amt_csv,
       user_node un,
       node n,
       ssi_contest sc,
       badge_rule br,
       ssi_contest_sr_payout scsrp,
       ssi_contest_pax_stack_rank scpsr
 WHERE v_csv_payout_amt IS NOT NULL
       AND user_id_csv.row_num = payout_amt_csv.row_num
       AND user_id_csv.user_id = un.user_id
       AND un.node_id = n.node_id
       AND un.status = 1
       AND un.is_primary = 1
       AND sc.ssi_contest_id = v_ssi_contest_id
       AND sc.ssi_contest_id = scsrp.ssi_contest_id
       AND scsrp.rank_position = scpsr.stack_rank_position
       AND scpsr.user_id = un.user_id
       AND scpsr.ssi_contest_id = scsrp.ssi_contest_id
       AND scsrp.badge_rule_id = br.badge_rule_id(+) */

--To 'Issue Payouts? for a Stack Rank contest with a ?tie?, when this procedure is invoked, --06/12/2015
--It should use the records in the SSI_CONTEST_PAX_PAYOUT table for creating the JOURNAL records.
  CURSOR cur_stack_rank_with_tie (v_ssi_contest_id ssi_contest.ssi_contest_id%TYPE) IS  --06/12/2015
                   SELECT  sc.ssi_contest_id ,
                           scpp.user_id,
                            CASE WHEN p.is_opt_out_of_awards = 1 THEN 0  --03/29/2017
                            ELSE scpp.payout_amount END as payout_amt,
                           n.name as pax_node_name,
                           scsrp.badge_rule_id,
                           br.promotion_id,
                           pb.user_name,
                           pb.country_code,
                           pb.department,
                           pb.custom_value_list,
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
                    FROM ssi_contest_pax_stack_rank scpsr,
                         ssi_contest_pax_payout scpp,  --06/12/2015
                         participant p,   --03/29/2017
                         ssi_contest_sr_payout scsrp,
                         ssi_contest sc,
                         badge_rule br,
                         user_node un,
                         node n,
                         (SELECT au.user_id,
                                       au.user_name,
                                       c.country_code,
                                       emp.department_type as department,
                                       cb.custom_value_list,
                                       characteristic1_value, 
                                       characteristic2_value, 
                                       characteristic3_value, 
                                       characteristic4_value, 
                                       characteristic5_value, 
                                       characteristic6_value, 
                                       characteristic7_value, 
                                       characteristic8_value, 
                                       characteristic9_value, 
                                       characteristic10_value  
                                FROM application_user au,
                                     user_address ua,
                                     country c,
                                     (SELECT * FROM ( -- rank records by termination date and employer index in reverse order
                                           SELECT RANK ()
                                                  OVER (
                                                     PARTITION BY pe.user_id
                                                     ORDER BY
                                                        pe.termination_date DESC,
                                                        pe.participant_employer_index DESC)
                                                     AS rec_rank,
                                                  pe.*
                                             FROM participant_employer pe) r
                                    -- the current employment record has the lowest ranking
                                    WHERE r.rec_rank = 1) emp,
                                    ssi_contest sc,
                                    (SELECT ssi_contest_id,
                                       bill_code,
                                       track_bills_by,
                                       LISTAGG(custom_value , ', ') WITHIN GROUP (ORDER BY custom_value) AS custom_value_list
                                    FROM ssi_contest_bill_code 
                                    WHERE bill_code = c_bill_code_customValue  
                                    AND ssi_contest_id = v_ssi_contest_id
                                    AND track_bills_by = c_track_bill_type_participant
                                    GROUP BY ssi_contest_id,
                                             bill_code,
                                             track_bills_by,
                                             custom_value
                                     ) cb
                 ,( SELECT ssi_contest_id, 
                           user_id, 
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
                      FROM (
                      SELECT sbc.ssi_contest_id,
                                   uc.characteristic_value characteristic_values,
                                   sbc.sort_order+1 char_num,
                                   user_id
                              FROM user_characteristic uc,
                                   ssi_contest_bill_code sbc
                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
                                      sbc.bill_code IS NOT NULL                   AND
                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)  
--                      SELECT sbc.ssi_contest_id,
--                                   uc.characteristic_value characteristic_values,
--                                   DENSE_RANK ()
--                                   OVER (PARTITION BY user_id ORDER BY uc.user_characteristic_id) char_num,
--                                   user_id
--                              FROM user_characteristic uc,
--                                   ssi_contest_bill_code sbc
--                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
--                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
--                                      sbc.bill_code IS NOT NULL                   AND
--                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)
                               ) PIVOT ( MIN (UPPER(characteristic_values)) VALUE
                                                                               FOR (char_num)
                                                                                IN (1 AS characteristic1,
                                                                                    2 AS characteristic2,
                                                                                    3 AS characteristic3,
                                                                                    4 AS characteristic4,
                                                                                    5 AS characteristic5,
                                                                                    6 AS characteristic6,
                                                                                    7 AS characteristic7,
                                                                                    8 AS characteristic8,
                                                                                    9 AS characteristic9,
                                                                                    10 AS characteristic10 ))  ) uc
                WHERE au.user_id        = ua.user_id            AND
                      ua.country_id     = c.country_id          AND
                      ua.is_primary     = 1                     AND
                      ua.user_id        = emp.user_id           AND
                      sc.ssi_contest_id = v_ssi_contest_id  AND
                      ua.user_id        = uc.user_id        (+) AND     
                      sc.ssi_contest_id = cb.ssi_contest_id (+)) pb
                   WHERE     scpsr.ssi_contest_id = v_ssi_contest_id
                         AND scpsr.ssi_contest_id = sc.ssi_contest_id  --06/03/2015 
                         AND scpsr.user_id = scpp.user_id
                         AND scpp.user_id = p.user_id --03/29/2017
                         AND sc.ssi_contest_id = scpp.ssi_contest_id
                         AND scpsr.user_id = un.user_id
                         AND un.node_id = n.node_id
                         AND un.status = 1
                         AND un.is_primary = 1
                         AND sc.ssi_contest_id = scsrp.ssi_contest_id
                         AND scsrp.rank_position = scpsr.stack_rank_position
                         AND scsrp.badge_rule_id = br.badge_rule_id  (+)
                         AND scpsr.user_id       = pb.user_id        (+);


      CURSOR cur_atn (v_ssi_contest_id ssi_contest.ssi_contest_id%TYPE,v_in_award_issuance_number NUMBER) IS
       SELECT
             sc.ssi_contest_id,
             scp.user_id,
            CASE WHEN p.is_opt_out_of_awards = 1 THEN 0  --03/29/2017
            ELSE scp.objective_payout END AS payout_amt,
           sc.badge_rule_id,br.promotion_id,
           n.name pax_node_name,
           pb.user_name,
           pb.country_code,
           pb.department,
           pb.custom_value_list,
           characteristic1_value, 
           characteristic2_value, 
           characteristic3_value, 
           characteristic4_value, 
           characteristic5_value, 
           characteristic6_value, 
           characteristic7_value, 
           characteristic8_value, 
           characteristic9_value, 
           characteristic10_value  
          FROM
            ssi_contest sc,
                 ssi_contest_participant scp,
                 participant p,   --03/29/2017
                 badge_rule br,
                 user_node un,
                 node n
                 ,(SELECT au.user_id,
                                       au.user_name,
                                       c.country_code,
                                       emp.department_type as department,
                                       cb.custom_value_list,
                                       characteristic1_value, 
                                       characteristic2_value, 
                                       characteristic3_value, 
                                       characteristic4_value, 
                                       characteristic5_value, 
                                       characteristic6_value, 
                                       characteristic7_value, 
                                       characteristic8_value, 
                                       characteristic9_value, 
                                       characteristic10_value  
                                FROM application_user au,
                                     user_address ua,
                                     country c,
                                     (SELECT * FROM ( -- rank records by termination date and employer index in reverse order
                                           SELECT RANK ()
                                                  OVER (
                                                     PARTITION BY pe.user_id
                                                     ORDER BY
                                                        pe.termination_date DESC,
                                                        pe.participant_employer_index DESC)
                                                     AS rec_rank,
                                                  pe.*
                                             FROM participant_employer pe) r
                                    -- the current employment record has the lowest ranking
                                    WHERE r.rec_rank = 1) emp,
                                    ssi_contest sc,
                                    (SELECT ssi_contest_id,
                                       bill_code,
                                       track_bills_by,
                                       LISTAGG(custom_value , ', ') WITHIN GROUP (ORDER BY custom_value) AS custom_value_list
                                    FROM ssi_contest_bill_code 
                                    WHERE bill_code = c_bill_code_customValue  
                                    AND ssi_contest_id = v_ssi_contest_id
                                    AND track_bills_by = c_track_bill_type_participant
                                    GROUP BY ssi_contest_id,
                                             bill_code,
                                             track_bills_by,
                                             custom_value
                                     ) cb
                 ,( SELECT ssi_contest_id, 
                           user_id, 
                           characteristic1_value, 
                           characteristic2_value, 
                           characteristic3_value, 
                           characteristic4_value, 
                           characteristic5_value, 
                           characteristic6_value, 
                           characteristic7_value, 
                           characteristic8_value, 
                           characteristic9_value, 
                           characteristic10_value  
                      FROM (
                      SELECT sbc.ssi_contest_id,
                                   uc.characteristic_value characteristic_values,
                                   sbc.sort_order+1 char_num,
                                   user_id
                              FROM user_characteristic uc,
                                   ssi_contest_bill_code sbc
                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
                                      sbc.bill_code IS NOT NULL                   AND
                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)  
--                                      SELECT sbc.ssi_contest_id,
--                                   uc.characteristic_value characteristic_values,
--                                   DENSE_RANK ()
--                                   OVER (PARTITION BY user_id ORDER BY uc.user_characteristic_id) char_num,
--                                   user_id
--                              FROM user_characteristic uc,
--                                   ssi_contest_bill_code sbc
--                              WHERE sbc.ssi_contest_id = v_ssi_contest_id   AND
--                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
--                                      sbc.bill_code IS NOT NULL                   AND
--                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)
                               ) PIVOT ( MIN (UPPER(characteristic_values)) VALUE
                                                                               FOR (char_num)
                                                                                IN (1 AS characteristic1,
                                                                                    2 AS characteristic2,
                                                                                    3 AS characteristic3,
                                                                                    4 AS characteristic4,
                                                                                    5 AS characteristic5,
                                                                                    6 AS characteristic6,
                                                                                    7 AS characteristic7,
                                                                                    8 AS characteristic8,
                                                                                    9 AS characteristic9,
                                                                                    10 AS characteristic10 ))  ) uc
                    WHERE au.user_id        = ua.user_id            AND
                          ua.country_id     = c.country_id          AND
                          ua.is_primary     = 1                     AND
                          ua.user_id        = emp.user_id           AND
                          sc.ssi_contest_id = v_ssi_contest_id  AND
                          ua.user_id        = uc.user_id        (+) AND     
                          sc.ssi_contest_id = cb.ssi_contest_id (+)) pb
           WHERE     sc.ssi_contest_id = v_ssi_contest_id
                 AND sc.ssi_contest_id = scp.ssi_contest_id
                 AND scp.award_issuance_number = v_in_award_issuance_number
                 AND scp.user_id = un.user_id
                 AND scp.user_id = p.user_id --03/29/2017
                 AND un.is_primary = 1
                 AND un.status = 1
                 AND un.node_id = n.node_id
                 AND sc.badge_rule_id = br.badge_rule_id(+)
                 AND scp.user_id      = pb.user_id      (+) ;

BEGIN

 prc_execution_log_entry(v_process_name, v_release_level, 'INFO', 
                           'Process Started. Parameters - '||
--                           ' v_contest_type :'||v_contest_type||
--                           ' v_payout_type :'||v_payout_type||                           
                           ' p_in_ssi_contest_id :' || p_in_ssi_contest_id||
                           ' p_in_user_id :'||p_in_user_id||
                           ' p_in_award_issuance_number :'||p_in_award_issuance_number||
                           ' p_in_csv_user_ids :'||p_in_csv_user_ids||
                           ' p_in_csv_payout_amounts :'||p_in_csv_payout_amounts
                           , NULL);

   BEGIN
        --       SELECT contest_type,fnc_cms_asset_code_val_extr(cm_asset_code,'CONTEST_NAME','en_US'),payout_type,bill_payout_code_type,
        --       bill_payout_code_1,bill_payout_code_2,bill_payout_code_3,bill_payout_code_4,bill_payout_code_5,bill_payout_code_6,bill_payout_code_7,bill_payout_code_8,
        --       bill_payout_code_9,bill_payout_code_10,contest_owner_id,au.first_name||' '||au.last_name  --05/21/2015,11/22/2016
        --             INTO v_contest_type, v_contest_name,v_payout_type,v_bill_payout_code_type,v_bill_payout_code_1,v_bill_payout_code_2,v_bill_payout_code_3,v_bill_payout_code_4,v_bill_payout_code_5,v_bill_payout_code_6,
        --             v_bill_payout_code_7,v_bill_payout_code_8,v_bill_payout_code_9,v_bill_payout_code_10,v_contest_owner_id,v_contest_owner_name --SSI Phase2
        --       FROM ssi_contest s, application_user au WHERE ssi_contest_id = p_in_ssi_contest_id AND s.status <> 'finalize_results' AND s.contest_owner_id = au.user_id;
       SELECT contest_type,
           fnc_cms_asset_code_val_extr(cm_asset_code,'CONTEST_NAME','en_US'),payout_type,
           (SELECT track_bills_by bill_payout_code_type FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 0) bill_payout_code_type1,
           (SELECT track_bills_by bill_payout_code_type FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 1) bill_payout_code_type2,
           (SELECT track_bills_by bill_payout_code_type FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 2) bill_payout_code_type3,
           (SELECT track_bills_by bill_payout_code_type FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 3) bill_payout_code_type4,
           (SELECT track_bills_by bill_payout_code_type FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 4) bill_payout_code_type5,
           (SELECT track_bills_by bill_payout_code_type FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 5) bill_payout_code_type6,
           (SELECT track_bills_by bill_payout_code_type FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 6) bill_payout_code_type7,
           (SELECT track_bills_by bill_payout_code_type FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 7) bill_payout_code_type8,
           (SELECT track_bills_by bill_payout_code_type FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 8) bill_payout_code_type9,
           (SELECT track_bills_by bill_payout_code_type FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 9) bill_payout_code_type10,  
           (SELECT DECODE (REGEXP_COUNT(bill_code,'[a-zA-Z]'),0,c_bill_code_characteristic,bill_code ) as bill_code FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 0) bill_payout_code1,
           (SELECT DECODE (REGEXP_COUNT(bill_code,'[a-zA-Z]'),0,c_bill_code_characteristic,bill_code ) as bill_code FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 1) bill_payout_code2,
           (SELECT DECODE (REGEXP_COUNT(bill_code,'[a-zA-Z]'),0,c_bill_code_characteristic,bill_code ) as bill_code FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 2) bill_payout_code3,
           (SELECT DECODE (REGEXP_COUNT(bill_code,'[a-zA-Z]'),0,c_bill_code_characteristic,bill_code ) as bill_code FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 3) bill_payout_code4,
           (SELECT DECODE (REGEXP_COUNT(bill_code,'[a-zA-Z]'),0,c_bill_code_characteristic,bill_code ) as bill_code FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 4) bill_payout_code5,
           (SELECT DECODE (REGEXP_COUNT(bill_code,'[a-zA-Z]'),0,c_bill_code_characteristic,bill_code ) as bill_code FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 5) bill_payout_code6,
           (SELECT DECODE (REGEXP_COUNT(bill_code,'[a-zA-Z]'),0,c_bill_code_characteristic,bill_code ) as bill_code FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 6) bill_payout_code7,
           (SELECT DECODE (REGEXP_COUNT(bill_code,'[a-zA-Z]'),0,c_bill_code_characteristic,bill_code ) as bill_code FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 7) bill_payout_code8,
           (SELECT DECODE (REGEXP_COUNT(bill_code,'[a-zA-Z]'),0,c_bill_code_characteristic,bill_code ) as bill_code FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 8) bill_payout_code9,
           (SELECT DECODE (REGEXP_COUNT(bill_code,'[a-zA-Z]'),0,c_bill_code_characteristic,bill_code ) as bill_code FROM ssi_contest_bill_code WHERE ssi_contest_id = s.ssi_contest_id AND sort_order = 9) bill_payout_code10,
           contest_owner_id,
           au.first_name||' '||au.last_name  --05/21/2015,11/22/2016
           INTO       v_contest_type, v_contest_name,v_payout_type,v_bill_payout_code_type1,v_bill_payout_code_type2,v_bill_payout_code_type3,v_bill_payout_code_type4,v_bill_payout_code_type5,
                      v_bill_payout_code_type6,v_bill_payout_code_type7,v_bill_payout_code_type8,v_bill_payout_code_type9,v_bill_payout_code_type10,
                      v_bill_payout_code_1,v_bill_payout_code_2,v_bill_payout_code_3,v_bill_payout_code_4,v_bill_payout_code_5,v_bill_payout_code_6,
                      v_bill_payout_code_7,v_bill_payout_code_8,v_bill_payout_code_9,v_bill_payout_code_10,v_contest_owner_id,v_contest_owner_name --SSI Phase2
       FROM ssi_contest s, 
            application_user au 
       WHERE ssi_contest_id = p_in_ssi_contest_id AND 
             s.status <> 'finalize_results' AND 
             s.contest_owner_id = au.user_id;
       EXCEPTION
       WHEN OTHERS THEN
         p_out_return_code := 99;
         v_execution_log_msg := SQLERRM;
         RAISE exit_program_exception;
   END;
   
   BEGIN--Bug # 63336
   SELECT promotion_id INTO v_promotion_id FROM promotion WHERE promotion_type = 'self_serv_incentives'
   AND promotion_status = 'live';
    EXCEPTION
       WHEN OTHERS THEN
         p_out_return_code := 99;
         v_execution_log_msg := SQLERRM;
         RAISE exit_program_exception;
    END;

   IF v_contest_type IS NULL THEN
   RAISE exit_program_exception;
   END IF;

SELECT n.name,
          pb.user_name,
          pb.country_code,
          pb.department,  
          pb.custom_value_list,
           pb.characteristic1_value, 
           pb.characteristic2_value, 
           pb.characteristic3_value, 
           pb.characteristic4_value, 
           pb.characteristic5_value, 
           pb.characteristic6_value, 
           pb.characteristic7_value, 
           pb.characteristic8_value, 
           pb.characteristic9_value, 
           pb.characteristic10_value  
   into v_creator_node_name,
        v_creator_user_name,
        v_creator_country_code,
        v_creator_department,
        v_creator_custom_value_list,
        v_characteristic_value1,
        v_characteristic_value2,
        v_characteristic_value3,
        v_characteristic_value4,
        v_characteristic_value5,
        v_characteristic_value6,
        v_characteristic_value7,
        v_characteristic_value8,
        v_characteristic_value9,
        v_characteristic_value10
   FROM user_node un, 
   node n,
   (SELECT au.user_id,
           au.user_name,
           c.country_code,
           emp.department_type as department,
           cb.custom_value_list,
           uc.characteristic1_value, 
           uc.characteristic2_value, 
           uc.characteristic3_value, 
           uc.characteristic4_value, 
           uc.characteristic5_value, 
           uc.characteristic6_value, 
           uc.characteristic7_value, 
           uc.characteristic8_value, 
           uc.characteristic9_value, 
           uc.characteristic10_value  
    FROM application_user au,
         user_address ua,
         country c,
         (SELECT * FROM ( -- rank records by termination date and employer index in reverse order
               SELECT RANK ()
                      OVER (
                         PARTITION BY pe.user_id
                         ORDER BY
                            pe.termination_date DESC,
                            pe.participant_employer_index DESC)
                         AS rec_rank,
                      pe.*
                 FROM participant_employer pe) r
        -- the current employment record has the lowest ranking
        WHERE r.rec_rank = 1) emp,
        ssi_contest sc,
        (SELECT ssi_contest_id,
           bill_code,
           track_bills_by,
           LISTAGG(custom_value , ', ') WITHIN GROUP (ORDER BY custom_value) AS custom_value_list
        FROM ssi_contest_bill_code 
        WHERE bill_code = 'customValue'  
        AND ssi_contest_id = p_in_ssi_contest_id
        AND track_bills_by = 'creator'
        GROUP BY ssi_contest_id,
                 bill_code,
                 track_bills_by,
                 custom_value
         ) cb
     ,( SELECT ssi_contest_id, 
               user_id, 
               characteristic1_value, 
               characteristic2_value, 
               characteristic3_value, 
               characteristic4_value, 
               characteristic5_value, 
               characteristic6_value, 
               characteristic7_value, 
               characteristic8_value, 
               characteristic9_value, 
               characteristic10_value  
          FROM (SELECT sbc.ssi_contest_id,
                                   uc.characteristic_value characteristic_values,
                                   sbc.sort_order+1 char_num,
                                   user_id
                              FROM user_characteristic uc,
                                   ssi_contest_bill_code sbc
                              WHERE sbc.ssi_contest_id = p_in_ssi_contest_id   AND
                                      REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
                                      sbc.bill_code IS NOT NULL                   AND
                                      sbc.bill_code = TO_CHAR(uc.characteristic_id)  
--                                      SELECT sbc.ssi_contest_id,
--                       uc.characteristic_value characteristic_values,
--                       DENSE_RANK ()
--                       OVER (PARTITION BY user_id ORDER BY uc.user_characteristic_id) char_num,
--                       user_id
--                  FROM user_characteristic uc,
--                       ssi_contest_bill_code sbc
--                  WHERE sbc.ssi_contest_id = p_in_ssi_contest_id   AND
--                          REGEXP_COUNT(sbc.bill_code,'[a-zA-Z]') = 0  AND
--                          sbc.bill_code IS NOT NULL                   AND
--                          sbc.bill_code = TO_CHAR(uc.characteristic_id)
                   ) PIVOT ( MIN (UPPER(characteristic_values)) VALUE
                                                                   FOR (char_num)
                                                                    IN (1 AS characteristic1,
                                                                        2 AS characteristic2,
                                                                        3 AS characteristic3,
                                                                        4 AS characteristic4,
                                                                        5 AS characteristic5,
                                                                        6 AS characteristic6,
                                                                        7 AS characteristic7,
                                                                        8 AS characteristic8,
                                                                        9 AS characteristic9,
                                                                        10 AS characteristic10 ))  ) uc
    WHERE au.user_id        = ua.user_id            AND
          ua.country_id     = c.country_id          AND
          ua.is_primary     = 1                     AND
          ua.user_id        = emp.user_id           AND
          sc.ssi_contest_id = p_in_ssi_contest_id  AND
          ua.user_id        = uc.user_id        (+) AND     
          sc.ssi_contest_id = cb.ssi_contest_id (+)) pb
   WHERE un.user_id = v_contest_owner_id and 
         un.node_id = n.node_id          and 
         un.is_primary = 1               and 
         un.status = 1                   and
         un.user_id = pb.user_id (+);


   prc_execution_log_entry(v_process_name, v_release_level, 'INFO', 
                           'Process Started. Parameters - '||
                           ' v_contest_type :'||v_contest_type||
                           ' v_payout_type :'||v_payout_type||                           
                           ' p_in_ssi_contest_id :' || p_in_ssi_contest_id||
                           ' p_in_user_id :'||p_in_user_id||
                           ' p_in_award_issuance_number :'||p_in_award_issuance_number||
                           ' p_in_csv_user_ids :'||p_in_csv_user_ids||
                           ' p_in_csv_payout_amounts :'||p_in_csv_payout_amounts
                           , NULL);
   IF  v_contest_type = c_objectives THEN

    FOR rec_cur_objectives IN cur_objectives (p_in_ssi_contest_id) LOOP

          v_journal_id := NULL;
          IF v_payout_type  <> c_track_bill_type_other THEN
                SELECT journal_pk_sq.NEXTVAL INTO v_journal_id FROM DUAL;
                INSERT INTO journal (
                   journal_id,
                   user_id,
                   promotion_id,--SSI Phase2
                   transaction_date,
                   transaction_type,
                   transaction_amt,
                   transaction_description,
                   journal_type,
                   status_type,
                   award_type,
                   /*primary_billing_code, --05/26/2016
                   secondary_billing_code,*/
                   guid,
                   created_by,
                   date_created,
                   version
                             )     
           VALUES  (
                    v_journal_id,
                    rec_cur_objectives.user_id, 
                    v_promotion_id,--SSI Phase2
                    SYSDATE,
                    'payout',
                    rec_cur_objectives.total_payout,
                    v_contest_name||' - '||v_contest_owner_name,--11/22/2016
                    'Award',
                    'approve',
                    'points',
                    /*CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25) --05/26/2016
                         WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25)
                         WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                    END,  --primary_billing_code 
                    CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25)
                         WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25)
                         WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                    END,*/--secondary_billing_code
                    SYS_GUID(),
                    p_in_user_id,
                    SYSDATE,
                    0
                   );
                   
                  INSERT INTO JOURNAL_BILL_CODE ( --05/26/2016
                           journal_bill_code_id, 
                           journal_id, 
                           billing_code1, 
                           billing_code2, 
                           billing_code3, 
                           billing_code4, 
                           billing_code5, 
                           billing_code6, 
                           billing_code7, 
                           billing_code8, 
                           billing_code9, 
                           billing_code10, 
                           created_by, 
                           date_created, 
                           modified_by, 
                           date_modified, 
                           VERSION) 
                    VALUES ( journal_bill_pk_sq.NEXTVAL,
                     v_journal_id,
                     CASE WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_orgUnitName     THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_userName        THEN SUBSTR(rec_cur_objectives.user_name,1,25)  
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_countryCode     THEN SUBSTR(rec_cur_objectives.country_code,1,25)                          
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_department      THEN SUBSTR(rec_cur_objectives.department,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_customValue     THEN SUBSTR(rec_cur_objectives.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_characteristic  THEN SUBSTR(rec_cur_objectives.characteristic1_value,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value1,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                     END,
                     CASE WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_userName    THEN SUBSTR(rec_cur_objectives.user_name,1,25)  
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_countryCode THEN SUBSTR(rec_cur_objectives.country_code,1,25)                          
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_department  THEN SUBSTR(rec_cur_objectives.department,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_customValue THEN SUBSTR(rec_cur_objectives.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_characteristic THEN SUBSTR(rec_cur_objectives.characteristic2_value,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value2,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                        END,
                     CASE WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_userName    THEN SUBSTR(rec_cur_objectives.user_name,1,25)  
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_countryCode THEN SUBSTR(rec_cur_objectives.country_code,1,25)                          
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_department  THEN SUBSTR(rec_cur_objectives.department,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_customValue THEN SUBSTR(rec_cur_objectives.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_characteristic THEN SUBSTR(rec_cur_objectives.characteristic3_value,1,25)                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value3,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_other AND v_bill_payout_code_3 IS NOT NULL THEN v_bill_payout_code_3 
                        END,
                     CASE WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_userName    THEN SUBSTR(rec_cur_objectives.user_name,1,25)  
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_countryCode THEN SUBSTR(rec_cur_objectives.country_code,1,25)                          
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_department  THEN SUBSTR(rec_cur_objectives.department,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_customValue THEN SUBSTR(rec_cur_objectives.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_characteristic THEN SUBSTR(rec_cur_objectives.characteristic4_value,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value4,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_other AND v_bill_payout_code_4 IS NOT NULL THEN v_bill_payout_code_4 
                        END,
                     CASE WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_userName    THEN SUBSTR(rec_cur_objectives.user_name,1,25)  
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_countryCode THEN SUBSTR(rec_cur_objectives.country_code,1,25)                          
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_department  THEN SUBSTR(rec_cur_objectives.department,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_customValue THEN SUBSTR(rec_cur_objectives.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_characteristic THEN SUBSTR(rec_cur_objectives.characteristic5_value,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value5,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_other AND v_bill_payout_code_5 IS NOT NULL THEN v_bill_payout_code_5 
                     END,
                     CASE WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_userName    THEN SUBSTR(rec_cur_objectives.user_name,1,25)  
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_countryCode THEN SUBSTR(rec_cur_objectives.country_code,1,25)                          
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_department  THEN SUBSTR(rec_cur_objectives.department,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_customValue THEN SUBSTR(rec_cur_objectives.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_characteristic THEN SUBSTR(rec_cur_objectives.characteristic6_value,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value6,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_other AND v_bill_payout_code_6 IS NOT NULL THEN v_bill_payout_code_6 
                        END,
                     CASE WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_userName    THEN SUBSTR(rec_cur_objectives.user_name,1,25)  
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_countryCode THEN SUBSTR(rec_cur_objectives.country_code,1,25)                          
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_department  THEN SUBSTR(rec_cur_objectives.department,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_customValue THEN SUBSTR(rec_cur_objectives.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_characteristic THEN SUBSTR(rec_cur_objectives.characteristic7_value,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value7,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_other AND v_bill_payout_code_7 IS NOT NULL THEN v_bill_payout_code_7 
                     END,
                     CASE WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_userName    THEN SUBSTR(rec_cur_objectives.user_name,1,25)  
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_countryCode THEN SUBSTR(rec_cur_objectives.country_code,1,25)                          
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_department  THEN SUBSTR(rec_cur_objectives.department,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_customValue THEN SUBSTR(rec_cur_objectives.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_characteristic THEN SUBSTR(rec_cur_objectives.characteristic8_value,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value8,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_other AND v_bill_payout_code_8 IS NOT NULL THEN v_bill_payout_code_8 
                        END,
                     CASE WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_userName    THEN SUBSTR(rec_cur_objectives.user_name,1,25)  
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_countryCode THEN SUBSTR(rec_cur_objectives.country_code,1,25)                          
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_department  THEN SUBSTR(rec_cur_objectives.department,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_customValue THEN SUBSTR(rec_cur_objectives.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_characteristic THEN SUBSTR(rec_cur_objectives.characteristic9_value,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value9,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_other AND v_bill_payout_code_9 IS NOT NULL THEN v_bill_payout_code_9 
                     END,
                     CASE WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_objectives.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_userName    THEN SUBSTR(rec_cur_objectives.user_name,1,25)  
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_countryCode THEN SUBSTR(rec_cur_objectives.country_code,1,25)                          
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_department  THEN SUBSTR(rec_cur_objectives.department,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_customValue THEN SUBSTR(rec_cur_objectives.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_characteristic THEN SUBSTR(rec_cur_objectives.characteristic10_value,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value10,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_other AND v_bill_payout_code_10 IS NOT NULL THEN v_bill_payout_code_10 
                        END,
                     p_in_user_id,/* CREATED_BY */
                     SYSDATE,/* DATE_CREATED */
                     NULL,/* MODIFIED_BY */
                     NULL,/* DATE_MODIFIED */
                     0/* VERSION */ 
                     );
           END IF;   --IF v_payout_type <> other     


            INSERT INTO ssi_contest_pax_payout (
                   ssi_contest_pax_payout_id, 
                   ssi_contest_id, 
                   ssi_contest_activity_id, 
                   user_id, 
                   payout_amount, 
                   journal_id, 
                   created_by, 
                   date_created, 
                   modified_by, 
                   date_modified, 
                   version )       
        VALUES (
                   ssi_contest_pax_payout_pk_sq.NEXTVAL,
                   p_in_ssi_contest_id,
                   NULL, 
                   rec_cur_objectives.user_id,
                   rec_cur_objectives.total_payout,
                   v_journal_id,
                   p_in_user_id,
                   SYSDATE,
                   NULL,
                   NULL,
                   0
                );    
    IF rec_cur_objectives.promotion_id IS NOT NULL THEN            
       INSERT INTO participant_badge (
                   participant_badge_id, 
                   promotion_id, 
                   participant_id, 
                   is_earned, 
                   earned_date, 
                   sent_count, 
                   received_count, 
                   badge_rule_id, 
                   status,
                   is_earned_all_behavior_points,
                   is_earned_badge_points,
                   ssi_contest_id,
                   created_by,
                   date_created,                   
                   modified_by, 
                   date_modified, 
                   version )       
        VALUES (
                   participant_badge_pk_sq.NEXTVAL,
                   rec_cur_objectives.promotion_id, 
                   rec_cur_objectives.user_id, 
                   1, 
                   TRUNC(SYSDATE), 
                   0, 
                   0, 
                   rec_cur_objectives.badge_rule_id, 
                   'A',
                   0,
                   0,                   
                   p_in_ssi_contest_id,
                   p_in_user_id,
                   SYSDATE,
                   NULL,
                   NULL,
                   0
                );            
                
       END IF;         
                
    END LOOP;  --LOOP cur_objective
   
   ELSIF v_contest_type = c_do_this_get_that THEN 
   
    FOR rec_cur_dtgt IN cur_dtgt (p_in_ssi_contest_id) LOOP

       IF v_payout_type  <> c_track_bill_type_other THEN
           SELECT journal_pk_sq.NEXTVAL INTO v_journal_id FROM DUAL;       
           INSERT INTO journal (
                   journal_id,
                   user_id,
                   promotion_id,--SSI Phase2
                   transaction_date,
                   transaction_type,
                   transaction_amt,                   
                   transaction_description,
                   journal_type,
                   status_type,
                   /*primary_billing_code, --05/26/2016
                   secondary_billing_code,*/
                   guid,
                   created_by,
                   date_created,
                   version
                             )     
           VALUES  (
                    v_journal_id,
                    rec_cur_dtgt.user_id,
                    v_promotion_id,--SSI Phase2
                    SYSDATE,
                    'payout',
                    rec_cur_dtgt.payout_value,                    
                    v_contest_name||'-'||rec_cur_dtgt.activity_name,
                    'Award',
                    'approve',
                    /*CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25) --05/26/2016
                         WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25)
                         WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                    END,  --primary_billing_code 
                    CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25)
                         WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25)
                         WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                    END, */ --secondary_billing_code
                    SYS_GUID(),
                    p_in_user_id,
                    SYSDATE,
                    0
                   );
                   
                INSERT INTO JOURNAL_BILL_CODE ( --05/26/2016
                           journal_bill_code_id, 
                           journal_id, 
                           billing_code1, 
                           billing_code2, 
                           billing_code3, 
                           billing_code4, 
                           billing_code5, 
                           billing_code6, 
                           billing_code7, 
                           billing_code8, 
                           billing_code9, 
                           billing_code10, 
                           created_by, 
                           date_created, 
                           modified_by, 
                           date_modified, 
                           VERSION) 
                    VALUES ( journal_bill_pk_sq.NEXTVAL,
                     v_journal_id,
                     CASE WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_orgUnitName     THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_userName        THEN SUBSTR(rec_cur_dtgt.user_name,1,25)  
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_countryCode     THEN SUBSTR(rec_cur_dtgt.country_code,1,25)                          
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_department      THEN SUBSTR(rec_cur_dtgt.department,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_customValue     THEN SUBSTR(rec_cur_dtgt.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_characteristic  THEN SUBSTR(rec_cur_dtgt.characteristic1_value,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value1,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                     END,
                     CASE WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_userName    THEN SUBSTR(rec_cur_dtgt.user_name,1,25)  
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_countryCode THEN SUBSTR(rec_cur_dtgt.country_code,1,25)                          
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_department  THEN SUBSTR(rec_cur_dtgt.department,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_customValue THEN SUBSTR(rec_cur_dtgt.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_characteristic THEN SUBSTR(rec_cur_dtgt.characteristic2_value,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value2,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                        END,
                     CASE WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_userName    THEN SUBSTR(rec_cur_dtgt.user_name,1,25)  
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_countryCode THEN SUBSTR(rec_cur_dtgt.country_code,1,25)                          
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_department  THEN SUBSTR(rec_cur_dtgt.department,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_customValue THEN SUBSTR(rec_cur_dtgt.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_characteristic THEN SUBSTR(rec_cur_dtgt.characteristic3_value,1,25)                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value3,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_other AND v_bill_payout_code_3 IS NOT NULL THEN v_bill_payout_code_3 
                        END,
                     CASE WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_userName    THEN SUBSTR(rec_cur_dtgt.user_name,1,25)  
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_countryCode THEN SUBSTR(rec_cur_dtgt.country_code,1,25)                          
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_department  THEN SUBSTR(rec_cur_dtgt.department,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_customValue THEN SUBSTR(rec_cur_dtgt.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_characteristic THEN SUBSTR(rec_cur_dtgt.characteristic4_value,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value4,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_other AND v_bill_payout_code_4 IS NOT NULL THEN v_bill_payout_code_4 
                        END,
                     CASE WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_userName    THEN SUBSTR(rec_cur_dtgt.user_name,1,25)  
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_countryCode THEN SUBSTR(rec_cur_dtgt.country_code,1,25)                          
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_department  THEN SUBSTR(rec_cur_dtgt.department,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_customValue THEN SUBSTR(rec_cur_dtgt.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_characteristic THEN SUBSTR(rec_cur_dtgt.characteristic5_value,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value5,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_other AND v_bill_payout_code_5 IS NOT NULL THEN v_bill_payout_code_5 
                     END,
                     CASE WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_userName    THEN SUBSTR(rec_cur_dtgt.user_name,1,25)  
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_countryCode THEN SUBSTR(rec_cur_dtgt.country_code,1,25)                          
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_department  THEN SUBSTR(rec_cur_dtgt.department,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_customValue THEN SUBSTR(rec_cur_dtgt.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_characteristic THEN SUBSTR(rec_cur_dtgt.characteristic6_value,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value6,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_other AND v_bill_payout_code_6 IS NOT NULL THEN v_bill_payout_code_6 
                        END,
                     CASE WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_userName    THEN SUBSTR(rec_cur_dtgt.user_name,1,25)  
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_countryCode THEN SUBSTR(rec_cur_dtgt.country_code,1,25)                          
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_department  THEN SUBSTR(rec_cur_dtgt.department,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_customValue THEN SUBSTR(rec_cur_dtgt.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_characteristic THEN SUBSTR(rec_cur_dtgt.characteristic7_value,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value7,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_other AND v_bill_payout_code_7 IS NOT NULL THEN v_bill_payout_code_7 
                     END,
                     CASE WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_userName    THEN SUBSTR(rec_cur_dtgt.user_name,1,25)  
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_countryCode THEN SUBSTR(rec_cur_dtgt.country_code,1,25)                          
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_department  THEN SUBSTR(rec_cur_dtgt.department,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_customValue THEN SUBSTR(rec_cur_dtgt.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_characteristic THEN SUBSTR(rec_cur_dtgt.characteristic8_value,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value8,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_other AND v_bill_payout_code_8 IS NOT NULL THEN v_bill_payout_code_8 
                        END,
                     CASE WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_userName    THEN SUBSTR(rec_cur_dtgt.user_name,1,25)  
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_countryCode THEN SUBSTR(rec_cur_dtgt.country_code,1,25)                          
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_department  THEN SUBSTR(rec_cur_dtgt.department,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_customValue THEN SUBSTR(rec_cur_dtgt.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_characteristic THEN SUBSTR(rec_cur_dtgt.characteristic9_value,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value9,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_other AND v_bill_payout_code_9 IS NOT NULL THEN v_bill_payout_code_9 
                     END,
                     CASE WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_dtgt.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_userName    THEN SUBSTR(rec_cur_dtgt.user_name,1,25)  
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_countryCode THEN SUBSTR(rec_cur_dtgt.country_code,1,25)                          
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_department  THEN SUBSTR(rec_cur_dtgt.department,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_customValue THEN SUBSTR(rec_cur_dtgt.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_characteristic THEN SUBSTR(rec_cur_dtgt.characteristic10_value,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value10,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_other AND v_bill_payout_code_10 IS NOT NULL THEN v_bill_payout_code_10 
                        END,
                     p_in_user_id,/* CREATED_BY */
                     SYSDATE,/* DATE_CREATED */
                     NULL,/* MODIFIED_BY */
                     NULL,/* DATE_MODIFIED */
                     0/* VERSION */ 
                     );
        END IF;  --IF v_payout_type <> other           

            INSERT INTO ssi_contest_pax_payout (
                   ssi_contest_pax_payout_id, 
                   ssi_contest_id, 
                   ssi_contest_activity_id, 
                   user_id, 
                   payout_amount, 
                   journal_id, 
                   created_by, 
                   date_created, 
                   modified_by, 
                   date_modified, 
                   version )       
        VALUES (
                   ssi_contest_pax_payout_pk_sq.NEXTVAL,
                   p_in_ssi_contest_id,
                   rec_cur_dtgt.ssi_contest_activity_id, 
                   rec_cur_dtgt.user_id,
                   rec_cur_dtgt.payout_value,
                   v_journal_id,
                   p_in_user_id,
                   SYSDATE,
                   NULL,
                   NULL,
                   0
                );            

IF rec_cur_dtgt.promotion_id IS NOT NULL THEN        
       INSERT INTO participant_badge (
                   participant_badge_id, 
                   promotion_id, 
                   participant_id, 
                   is_earned, 
                   earned_date, 
                   sent_count, 
                   received_count, 
                   badge_rule_id, 
                   status,
                   is_earned_all_behavior_points,
                   is_earned_badge_points,
                   ssi_contest_id,
                   created_by,
                   date_created,                   
                   modified_by, 
                   date_modified, 
                   version )       
        VALUES (
                   participant_badge_pk_sq.NEXTVAL,
                   rec_cur_dtgt.promotion_id, 
                   rec_cur_dtgt.user_id, 
                   1, 
                   TRUNC(SYSDATE), 
                   0, 
                   0, 
                   rec_cur_dtgt.badge_rule_id, 
                   'A',
                   0,
                   0,                   
                   p_in_ssi_contest_id,
                   p_in_user_id,
                   SYSDATE,
                   NULL,
                   NULL,
                   0
                );            
     
     END IF;           
    END LOOP;   --LOOP cur_dtgt
   
   ELSIF v_contest_type = c_step_it_up THEN
       FOR rec_cur_step_it_up IN cur_step_it_up (p_in_ssi_contest_id) LOOP

          IF v_payout_type  <> c_track_bill_type_other THEN 
               SELECT journal_pk_sq.NEXTVAL INTO v_journal_id FROM DUAL;
               INSERT INTO journal (
                   journal_id,
                   user_id,
                   promotion_id,--SSI Phase2
                   transaction_date,
                   transaction_type,
                   transaction_amt,
                   transaction_description,
                   journal_type,
                   status_type,
                   /*primary_billing_code,--05/26/2016
                   secondary_billing_code,*/
                   guid,
                   created_by,
                   date_created,
                   version
                             )     
           VALUES  (
                    v_journal_id,
                    rec_cur_step_it_up.user_id, 
                    v_promotion_id,--SSI Phase2
                    SYSDATE,
                    'payout',
                    rec_cur_step_it_up.total_payout,
                    v_contest_name,
                    'Award',
                    'approve',
                    /*CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25) --05/26/2016
                         WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25) 
                         WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                    END,  --primary_billing_code 
                    CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25)
                         WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25)
                         WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                    END, */ --secondary_billing_code
                    SYS_GUID(),
                    p_in_user_id,
                    SYSDATE,
                    0
                   );
                 
           INSERT INTO JOURNAL_BILL_CODE ( --05/26/2016
                           journal_bill_code_id, 
                           journal_id, 
                           billing_code1, 
                           billing_code2, 
                           billing_code3, 
                           billing_code4, 
                           billing_code5, 
                           billing_code6, 
                           billing_code7, 
                           billing_code8, 
                           billing_code9, 
                           billing_code10, 
                           created_by, 
                           date_created, 
                           modified_by, 
                           date_modified, 
                           VERSION) 
                    VALUES ( journal_bill_pk_sq.NEXTVAL,
                     v_journal_id,
                     CASE WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_orgUnitName     THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_userName        THEN SUBSTR(rec_cur_step_it_up.user_name,1,25)  
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_countryCode     THEN SUBSTR(rec_cur_step_it_up.country_code,1,25)                          
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_department      THEN SUBSTR(rec_cur_step_it_up.department,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_customValue     THEN SUBSTR(rec_cur_step_it_up.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_characteristic  THEN SUBSTR(rec_cur_step_it_up.characteristic1_value,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value1,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                     END,
                     CASE WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_userName    THEN SUBSTR(rec_cur_step_it_up.user_name,1,25)  
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_countryCode THEN SUBSTR(rec_cur_step_it_up.country_code,1,25)                          
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_department  THEN SUBSTR(rec_cur_step_it_up.department,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_customValue THEN SUBSTR(rec_cur_step_it_up.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_characteristic THEN SUBSTR(rec_cur_step_it_up.characteristic2_value,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value2,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                        END,
                     CASE WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_userName    THEN SUBSTR(rec_cur_step_it_up.user_name,1,25)  
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_countryCode THEN SUBSTR(rec_cur_step_it_up.country_code,1,25)                          
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_department  THEN SUBSTR(rec_cur_step_it_up.department,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_customValue THEN SUBSTR(rec_cur_step_it_up.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_characteristic THEN SUBSTR(rec_cur_step_it_up.characteristic3_value,1,25)                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value3,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_other AND v_bill_payout_code_3 IS NOT NULL THEN v_bill_payout_code_3 
                        END,
                     CASE WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_userName    THEN SUBSTR(rec_cur_step_it_up.user_name,1,25)  
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_countryCode THEN SUBSTR(rec_cur_step_it_up.country_code,1,25)                          
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_department  THEN SUBSTR(rec_cur_step_it_up.department,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_customValue THEN SUBSTR(rec_cur_step_it_up.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_characteristic THEN SUBSTR(rec_cur_step_it_up.characteristic4_value,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value4,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_other AND v_bill_payout_code_4 IS NOT NULL THEN v_bill_payout_code_4 
                        END,
                     CASE WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_userName    THEN SUBSTR(rec_cur_step_it_up.user_name,1,25)  
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_countryCode THEN SUBSTR(rec_cur_step_it_up.country_code,1,25)                          
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_department  THEN SUBSTR(rec_cur_step_it_up.department,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_customValue THEN SUBSTR(rec_cur_step_it_up.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_characteristic THEN SUBSTR(rec_cur_step_it_up.characteristic5_value,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value5,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_other AND v_bill_payout_code_5 IS NOT NULL THEN v_bill_payout_code_5 
                     END,
                     CASE WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_userName    THEN SUBSTR(rec_cur_step_it_up.user_name,1,25)  
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_countryCode THEN SUBSTR(rec_cur_step_it_up.country_code,1,25)                          
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_department  THEN SUBSTR(rec_cur_step_it_up.department,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_customValue THEN SUBSTR(rec_cur_step_it_up.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_characteristic THEN SUBSTR(rec_cur_step_it_up.characteristic6_value,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value6,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_other AND v_bill_payout_code_6 IS NOT NULL THEN v_bill_payout_code_6 
                        END,
                     CASE WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_userName    THEN SUBSTR(rec_cur_step_it_up.user_name,1,25)  
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_countryCode THEN SUBSTR(rec_cur_step_it_up.country_code,1,25)                          
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_department  THEN SUBSTR(rec_cur_step_it_up.department,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_customValue THEN SUBSTR(rec_cur_step_it_up.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_characteristic THEN SUBSTR(rec_cur_step_it_up.characteristic7_value,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value7,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_other AND v_bill_payout_code_7 IS NOT NULL THEN v_bill_payout_code_7 
                     END,
                     CASE WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_userName    THEN SUBSTR(rec_cur_step_it_up.user_name,1,25)  
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_countryCode THEN SUBSTR(rec_cur_step_it_up.country_code,1,25)                          
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_department  THEN SUBSTR(rec_cur_step_it_up.department,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_customValue THEN SUBSTR(rec_cur_step_it_up.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_characteristic THEN SUBSTR(rec_cur_step_it_up.characteristic8_value,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value8,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_other AND v_bill_payout_code_8 IS NOT NULL THEN v_bill_payout_code_8 
                        END,
                     CASE WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_userName    THEN SUBSTR(rec_cur_step_it_up.user_name,1,25)  
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_countryCode THEN SUBSTR(rec_cur_step_it_up.country_code,1,25)                          
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_department  THEN SUBSTR(rec_cur_step_it_up.department,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_customValue THEN SUBSTR(rec_cur_step_it_up.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_characteristic THEN SUBSTR(rec_cur_step_it_up.characteristic9_value,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value9,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_other AND v_bill_payout_code_9 IS NOT NULL THEN v_bill_payout_code_9 
                     END,
                     CASE WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_step_it_up.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_userName    THEN SUBSTR(rec_cur_step_it_up.user_name,1,25)  
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_countryCode THEN SUBSTR(rec_cur_step_it_up.country_code,1,25)                          
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_department  THEN SUBSTR(rec_cur_step_it_up.department,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_customValue THEN SUBSTR(rec_cur_step_it_up.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_characteristic THEN SUBSTR(rec_cur_step_it_up.characteristic10_value,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value10,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_other AND v_bill_payout_code_10 IS NOT NULL THEN v_bill_payout_code_10 
                        END,
                     p_in_user_id,/* CREATED_BY */
                     SYSDATE,/* DATE_CREATED */
                     NULL,/* MODIFIED_BY */
                     NULL,/* DATE_MODIFIED */
                     0/* VERSION */ 
                     );
           END IF;  --IF v_payout_type <> other      

           INSERT INTO ssi_contest_pax_payout (
                   ssi_contest_pax_payout_id, 
                   ssi_contest_id, 
                   ssi_contest_activity_id, 
                   user_id, 
                   payout_amount, 
                   journal_id, 
                   created_by, 
                   date_created, 
                   modified_by, 
                   date_modified, 
                   version )       
        VALUES (
                   ssi_contest_pax_payout_pk_sq.NEXTVAL,
                   p_in_ssi_contest_id,
                   NULL, 
                   rec_cur_step_it_up.user_id,
                   rec_cur_step_it_up.total_payout,
                   v_journal_id,
                   p_in_user_id,
                   SYSDATE,
                   NULL,
                   NULL,
                   0
                );
   
   IF rec_cur_step_it_up.promotion_id IS NOT NULL THEN
        
      INSERT INTO participant_badge (
                   participant_badge_id, 
                   promotion_id, 
                   participant_id, 
                   is_earned, 
                   earned_date, 
                   sent_count, 
                   received_count, 
                   badge_rule_id, 
                   status,
                   is_earned_all_behavior_points,
                   is_earned_badge_points,
                   ssi_contest_id,
                   created_by,
                   date_created,                   
                   modified_by, 
                   date_modified,
                   version )       
        VALUES (
                   participant_badge_pk_sq.NEXTVAL,
                   rec_cur_step_it_up.promotion_id, 
                   rec_cur_step_it_up.user_id, 
                   1, 
                   TRUNC(SYSDATE), 
                   0, 
                   0, 
                   rec_cur_step_it_up.badge_rule_id, 
                   'A',
                   0,
                   0,                   
                   p_in_ssi_contest_id,
                   p_in_user_id,
                   SYSDATE,
                   NULL,
                   NULL,
                   0
                );      
                
    END IF;            
    END LOOP; --LOOP cur_step_it_up

   ELSIF v_contest_type = c_award_them_now THEN
       FOR rec_cur_atn IN cur_atn (p_in_ssi_contest_id,p_in_award_issuance_number) LOOP

          IF v_payout_type  <> c_track_bill_type_other THEN 
                SELECT journal_pk_sq.NEXTVAL INTO v_journal_id FROM DUAL;
                INSERT INTO journal (
                   journal_id,
                   user_id,
                   promotion_id,--SSI Phase2
                   transaction_date,
                   transaction_type,
                   transaction_amt,
                   transaction_description,
                   journal_type,
                   status_type,
                   /*primary_billing_code, --05/26/2016
                   secondary_billing_code,*/
                   guid,
                   created_by,
                   date_created,
                   version
                             )     
           VALUES  (
                    v_journal_id,
                    rec_cur_atn.user_id, 
                    v_promotion_id,--SSI Phase2
                    SYSDATE,
                    'payout',
                    rec_cur_atn.payout_amt,
                    v_contest_name,
                    'Award',
                    'approve',
                    /*CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_atn.pax_node_name,1,25) --05/26/2016
                         WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25)
                         WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                    END,  --primary_billing_code 
                    CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_atn.pax_node_name,1,25)
                         WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25)
                         WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                    END, */ --secondary_billing_code
                    SYS_GUID(),
                    p_in_user_id,
                    SYSDATE,
                    0
                   );
                   
              INSERT INTO JOURNAL_BILL_CODE ( --05/26/2016
                           journal_bill_code_id, 
                           journal_id, 
                           billing_code1, 
                           billing_code2, 
                           billing_code3, 
                           billing_code4, 
                           billing_code5, 
                           billing_code6, 
                           billing_code7, 
                           billing_code8, 
                           billing_code9, 
                           billing_code10, 
                           created_by, 
                           date_created, 
                           modified_by, 
                           date_modified, 
                           VERSION) 
                    VALUES ( journal_bill_pk_sq.NEXTVAL,
                     v_journal_id,
                     CASE WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_orgUnitName     THEN SUBSTR(rec_cur_atn.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_userName        THEN SUBSTR(rec_cur_atn.user_name,1,25)  
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_countryCode     THEN SUBSTR(rec_cur_atn.country_code,1,25)                          
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_department      THEN SUBSTR(rec_cur_atn.department,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_customValue     THEN SUBSTR(rec_cur_atn.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_characteristic  THEN SUBSTR(rec_cur_atn.characteristic1_value,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value1,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                     END,
                     CASE WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_atn.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_userName    THEN SUBSTR(rec_cur_atn.user_name,1,25)  
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_countryCode THEN SUBSTR(rec_cur_atn.country_code,1,25)                          
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_department  THEN SUBSTR(rec_cur_atn.department,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_customValue THEN SUBSTR(rec_cur_atn.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_characteristic THEN SUBSTR(rec_cur_atn.characteristic2_value,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value2,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                        END,
                     CASE WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_atn.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_userName    THEN SUBSTR(rec_cur_atn.user_name,1,25)  
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_countryCode THEN SUBSTR(rec_cur_atn.country_code,1,25)                          
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_department  THEN SUBSTR(rec_cur_atn.department,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_customValue THEN SUBSTR(rec_cur_atn.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_characteristic THEN SUBSTR(rec_cur_atn.characteristic3_value,1,25)                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value3,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_other AND v_bill_payout_code_3 IS NOT NULL THEN v_bill_payout_code_3 
                        END,
                     CASE WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_atn.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_userName    THEN SUBSTR(rec_cur_atn.user_name,1,25)  
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_countryCode THEN SUBSTR(rec_cur_atn.country_code,1,25)                          
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_department  THEN SUBSTR(rec_cur_atn.department,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_customValue THEN SUBSTR(rec_cur_atn.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_characteristic THEN SUBSTR(rec_cur_atn.characteristic4_value,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value4,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_other AND v_bill_payout_code_4 IS NOT NULL THEN v_bill_payout_code_4 
                        END,
                     CASE WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_atn.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_userName    THEN SUBSTR(rec_cur_atn.user_name,1,25)  
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_countryCode THEN SUBSTR(rec_cur_atn.country_code,1,25)                          
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_department  THEN SUBSTR(rec_cur_atn.department,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_customValue THEN SUBSTR(rec_cur_atn.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_characteristic THEN SUBSTR(rec_cur_atn.characteristic5_value,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value5,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_other AND v_bill_payout_code_5 IS NOT NULL THEN v_bill_payout_code_5 
                     END,
                     CASE WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_atn.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_userName    THEN SUBSTR(rec_cur_atn.user_name,1,25)  
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_countryCode THEN SUBSTR(rec_cur_atn.country_code,1,25)                          
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_department  THEN SUBSTR(rec_cur_atn.department,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_customValue THEN SUBSTR(rec_cur_atn.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_characteristic THEN SUBSTR(rec_cur_atn.characteristic6_value,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value6,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_other AND v_bill_payout_code_6 IS NOT NULL THEN v_bill_payout_code_6 
                        END,
                     CASE WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_atn.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_userName    THEN SUBSTR(rec_cur_atn.user_name,1,25)  
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_countryCode THEN SUBSTR(rec_cur_atn.country_code,1,25)                          
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_department  THEN SUBSTR(rec_cur_atn.department,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_customValue THEN SUBSTR(rec_cur_atn.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_characteristic THEN SUBSTR(rec_cur_atn.characteristic7_value,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value7,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_other AND v_bill_payout_code_7 IS NOT NULL THEN v_bill_payout_code_7 
                     END,
                     CASE WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_atn.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_userName    THEN SUBSTR(rec_cur_atn.user_name,1,25)  
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_countryCode THEN SUBSTR(rec_cur_atn.country_code,1,25)                          
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_department  THEN SUBSTR(rec_cur_atn.department,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_customValue THEN SUBSTR(rec_cur_atn.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_characteristic THEN SUBSTR(rec_cur_atn.characteristic8_value,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value8,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_other AND v_bill_payout_code_8 IS NOT NULL THEN v_bill_payout_code_8 
                        END,
                     CASE WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_atn.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_userName    THEN SUBSTR(rec_cur_atn.user_name,1,25)  
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_countryCode THEN SUBSTR(rec_cur_atn.country_code,1,25)                          
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_department  THEN SUBSTR(rec_cur_atn.department,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_customValue THEN SUBSTR(rec_cur_atn.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_characteristic THEN SUBSTR(rec_cur_atn.characteristic9_value,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value9,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_other AND v_bill_payout_code_9 IS NOT NULL THEN v_bill_payout_code_9 
                     END,
                     CASE WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_atn.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_userName    THEN SUBSTR(rec_cur_atn.user_name,1,25)  
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_countryCode THEN SUBSTR(rec_cur_atn.country_code,1,25)                          
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_department  THEN SUBSTR(rec_cur_atn.department,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_customValue THEN SUBSTR(rec_cur_atn.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_characteristic THEN SUBSTR(rec_cur_atn.characteristic10_value,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value10,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_other AND v_bill_payout_code_10 IS NOT NULL THEN v_bill_payout_code_10 
                        END,
                     p_in_user_id,/* CREATED_BY */
                     SYSDATE,/* DATE_CREATED */
                     NULL,/* MODIFIED_BY */
                     NULL,/* DATE_MODIFIED */
                     0/* VERSION */ 
                     );
           END IF;  --IF v_payout_type <> other      

          INSERT INTO ssi_contest_pax_payout (
                   ssi_contest_pax_payout_id, 
                   ssi_contest_id,                    
                   award_issuance_number,
                   user_id, 
                   payout_amount, 
                   journal_id, 
                   created_by, 
                   date_created, 
                   modified_by, 
                   date_modified, 
                   version )       
        VALUES (
                   ssi_contest_pax_payout_pk_sq.NEXTVAL,
                   p_in_ssi_contest_id,
                   p_in_award_issuance_number, 
                   rec_cur_atn.user_id,
                   rec_cur_atn.payout_amt,
                   v_journal_id,
                   p_in_user_id,
                   SYSDATE,
                   NULL,
                   NULL,
                   0
                );            
     
     IF rec_cur_atn.promotion_id IS NOT NULL THEN
                
                INSERT INTO participant_badge (
                   participant_badge_id, 
                   promotion_id, 
                   participant_id, 
                   is_earned, 
                   earned_date, 
                   sent_count, 
                   received_count, 
                   badge_rule_id, 
                   status,
                   is_earned_all_behavior_points,
                   is_earned_badge_points,
                   ssi_contest_id,
                   created_by,
                   date_created,                   
                   modified_by, 
                   date_modified,
                   version )       
        VALUES (
                   participant_badge_pk_sq.NEXTVAL,
                   rec_cur_atn.promotion_id, 
                   rec_cur_atn.user_id, 
                   1, 
                   TRUNC(SYSDATE), 
                   0, 
                   0, 
                   rec_cur_atn.badge_rule_id, 
                   'A',
                   0,
                   0,                   
                   p_in_ssi_contest_id,
                   p_in_user_id,
                   SYSDATE,
                   NULL,
                   NULL,
                   0
                );   
       END IF;
     END LOOP; --LOOP cur_atn
   ELSIF v_contest_type = c_stack_rank THEN
   
    SELECT tie_indicator.tie_rank_cnt into v_has_tie
      FROM
        (SELECT NVL(sum(tie_cnt),0) tie_rank_cnt
         FROM (SELECT COUNT(stack_rank_position) tie_cnt
              FROM ssi_contest_pax_stack_rank scpsr,
                        ssi_contest_sr_payout scsp
              WHERE scpsr.ssi_contest_id = p_in_ssi_contest_id
                   AND scpsr.ssi_contest_id = scsp.ssi_contest_id
                   AND scsp.rank_position = scpsr.stack_rank_position
              GROUP BY stack_rank_position
                            HAVING COUNT(stack_rank_position) > 1)) tie_indicator;
       IF v_has_tie > 0 THEN

        FOR rec_cur_stack_rank IN cur_stack_rank_with_tie (p_in_ssi_contest_id) LOOP  --06/12/2015      
 
              IF v_payout_type  <> c_track_bill_type_other THEN 
                    SELECT journal_pk_sq.NEXTVAL INTO v_journal_id FROM DUAL;
                    INSERT INTO journal (
                       journal_id,
                       user_id,
                       promotion_id,--SSI Phase2
                       transaction_date,
                       transaction_type,
                       transaction_amt,
                       transaction_description,
                       journal_type,
                       status_type,
                       /*primary_billing_code, --05/26/2016
                       secondary_billing_code,*/
                       guid,
                       created_by,
                       date_created,
                       version
                                 )     
               VALUES  (
                        v_journal_id,
                        rec_cur_stack_rank.user_id, 
                        v_promotion_id,--SSI Phase2
                        SYSDATE,
                        'payout',
                        rec_cur_stack_rank.payout_amt,
                        v_contest_name,
                        'Award',
                        'approve',
                        /*CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25) --05/26/2016
                             WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                        END,  --primary_billing_code 
                        CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                             WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                        END,*/  --secondary_billing_code
                        SYS_GUID(),
                        p_in_user_id,
                        SYSDATE,
                        0
                       );
                       
               INSERT INTO JOURNAL_BILL_CODE ( --05/26/2016
                           journal_bill_code_id, 
                           journal_id, 
                           billing_code1, 
                           billing_code2, 
                           billing_code3, 
                           billing_code4, 
                           billing_code5, 
                           billing_code6, 
                           billing_code7, 
                           billing_code8, 
                           billing_code9, 
                           billing_code10, 
                           created_by, 
                           date_created, 
                           modified_by, 
                           date_modified, 
                           VERSION) 
                    VALUES ( journal_bill_pk_sq.NEXTVAL,
                     v_journal_id,
                     CASE WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_orgUnitName     THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_userName        THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_countryCode     THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_department      THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_customValue     THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_characteristic  THEN SUBSTR(rec_cur_stack_rank.characteristic1_value,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value1,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                     END,
                     CASE WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic2_value,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value2,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                        END,
                     CASE WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic3_value,1,25)                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value3,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_other AND v_bill_payout_code_3 IS NOT NULL THEN v_bill_payout_code_3 
                        END,
                     CASE WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic4_value,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value4,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_other AND v_bill_payout_code_4 IS NOT NULL THEN v_bill_payout_code_4 
                        END,
                     CASE WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic5_value,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value5,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_other AND v_bill_payout_code_5 IS NOT NULL THEN v_bill_payout_code_5 
                     END,
                     CASE WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic6_value,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value6,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_other AND v_bill_payout_code_6 IS NOT NULL THEN v_bill_payout_code_6 
                        END,
                     CASE WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic7_value,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value7,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_other AND v_bill_payout_code_7 IS NOT NULL THEN v_bill_payout_code_7 
                     END,
                     CASE WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic8_value,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value8,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_other AND v_bill_payout_code_8 IS NOT NULL THEN v_bill_payout_code_8 
                        END,
                     CASE WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic9_value,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value9,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_other AND v_bill_payout_code_9 IS NOT NULL THEN v_bill_payout_code_9 
                     END,
                     CASE WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic10_value,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value10,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_other AND v_bill_payout_code_10 IS NOT NULL THEN v_bill_payout_code_10 
                        END,
                     p_in_user_id,/* CREATED_BY */
                     SYSDATE,/* DATE_CREATED */
                     NULL,/* MODIFIED_BY */
                     NULL,/* DATE_MODIFIED */
                     0/* VERSION */ 
                     );
               END IF;  --IF v_payout_type <> other      

              UPDATE ssi_contest_pax_payout      --06/12/2015
              SET    journal_id = v_journal_id,
                     modified_by = p_in_user_id,
                     date_modified = SYSDATE,
                     version = version+1
              WHERE  ssi_contest_id = p_in_ssi_contest_id AND
                     user_id = rec_cur_stack_rank.user_id;     

            IF rec_cur_stack_rank.promotion_id IS NOT NULL THEN  --04/14/2015
                    INSERT INTO participant_badge (
                       participant_badge_id, 
                       promotion_id, 
                       participant_id, 
                       is_earned, 
                       earned_date, 
                       sent_count, 
                       received_count, 
                       badge_rule_id, 
                       status,
                       is_earned_all_behavior_points,
                       is_earned_badge_points,
                       ssi_contest_id,
                       created_by,
                       date_created,                   
                       modified_by, 
                       date_modified,
                       version )       
            VALUES (
                       participant_badge_pk_sq.NEXTVAL,
                       rec_cur_stack_rank.promotion_id, 
                       rec_cur_stack_rank.user_id, 
                       1, 
                       TRUNC(SYSDATE), 
                       0, 
                       0, 
                       rec_cur_stack_rank.badge_rule_id, 
                       'A',
                       0,
                       0,                   
                       p_in_ssi_contest_id,
                       p_in_user_id,
                       SYSDATE,
                       NULL,
                       NULL,
                       0
                    );   
          END IF; --04/14/2015
        END LOOP; --LOOP cur_stack_rank
       ELSE   --v_has_tie > 0
       
           FOR rec_cur_stack_rank IN cur_stack_rank (p_in_ssi_contest_id) LOOP

              IF v_payout_type  <> c_track_bill_type_other THEN 
                    SELECT journal_pk_sq.NEXTVAL INTO v_journal_id FROM DUAL;
                    INSERT INTO journal (
                       journal_id,
                       user_id,
                       promotion_id,--SSI Phase2
                       transaction_date,
                       transaction_type,
                       transaction_amt,
                       transaction_description,
                       journal_type,
                       status_type,
                       /*primary_billing_code, --05/26/2016
                       secondary_billing_code,*/
                       guid,
                       created_by,
                       date_created,
                       version
                                 )     
               VALUES  (
                        v_journal_id,
                        rec_cur_stack_rank.user_id, 
                        v_promotion_id,--SSI Phase2
                        SYSDATE,
                        'payout',
                        rec_cur_stack_rank.payout_amt,
                        v_contest_name,
                        'Award',
                        'approve',
                        /*CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25) --05/26/2016
                             WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                        END,  --primary_billing_code 
                        CASE WHEN v_bill_payout_code_type = c_track_bill_type_participant THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                             WHEN v_bill_payout_code_type = c_track_bill_type_creator THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                        END,*/  --secondary_billing_code
                        SYS_GUID(),
                        p_in_user_id,
                        SYSDATE,
                        0
                       );
                       
                INSERT INTO JOURNAL_BILL_CODE ( --05/26/2016
                           journal_bill_code_id, 
                           journal_id, 
                           billing_code1, 
                           billing_code2, 
                           billing_code3, 
                           billing_code4, 
                           billing_code5, 
                           billing_code6, 
                           billing_code7, 
                           billing_code8, 
                           billing_code9, 
                           billing_code10, 
                           created_by, 
                           date_created, 
                           modified_by, 
                           date_modified, 
                           VERSION) 
                    VALUES ( journal_bill_pk_sq.NEXTVAL,
                     v_journal_id,
                     CASE WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_orgUnitName     THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_userName        THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_countryCode     THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_department      THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_customValue     THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type1 = c_track_bill_type_participant  AND v_bill_payout_code_1 = c_bill_code_characteristic  THEN SUBSTR(rec_cur_stack_rank.characteristic1_value,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_creator AND v_bill_payout_code_1 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value1,1,25)                             
                             WHEN v_bill_payout_code_type1 = c_track_bill_type_other AND v_bill_payout_code_1 IS NOT NULL THEN v_bill_payout_code_1 
                     END,
                     CASE WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type2 = c_track_bill_type_participant  AND v_bill_payout_code_2 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic2_value,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_creator AND v_bill_payout_code_2 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value2,1,25)
                             WHEN v_bill_payout_code_type2 = c_track_bill_type_other AND v_bill_payout_code_2 IS NOT NULL THEN v_bill_payout_code_2 
                        END,
                     CASE WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type3 = c_track_bill_type_participant  AND v_bill_payout_code_3 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic3_value,1,25)                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_creator AND v_bill_payout_code_3 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value3,1,25)
                             WHEN v_bill_payout_code_type3 = c_track_bill_type_other AND v_bill_payout_code_3 IS NOT NULL THEN v_bill_payout_code_3 
                        END,
                     CASE WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type4 = c_track_bill_type_participant  AND v_bill_payout_code_4 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic4_value,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_creator AND v_bill_payout_code_4 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value4,1,25)
                             WHEN v_bill_payout_code_type4 = c_track_bill_type_other AND v_bill_payout_code_4 IS NOT NULL THEN v_bill_payout_code_4 
                        END,
                     CASE WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type5 = c_track_bill_type_participant  AND v_bill_payout_code_5 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic5_value,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_creator AND v_bill_payout_code_5 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value5,1,25)
                             WHEN v_bill_payout_code_type5 = c_track_bill_type_other AND v_bill_payout_code_5 IS NOT NULL THEN v_bill_payout_code_5 
                     END,
                     CASE WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type6 = c_track_bill_type_participant  AND v_bill_payout_code_6 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic6_value,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_creator AND v_bill_payout_code_6 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value6,1,25)
                             WHEN v_bill_payout_code_type6 = c_track_bill_type_other AND v_bill_payout_code_6 IS NOT NULL THEN v_bill_payout_code_6 
                        END,
                     CASE WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type7 = c_track_bill_type_participant  AND v_bill_payout_code_7 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic7_value,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_creator AND v_bill_payout_code_7 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value7,1,25)
                             WHEN v_bill_payout_code_type7 = c_track_bill_type_other AND v_bill_payout_code_7 IS NOT NULL THEN v_bill_payout_code_7 
                     END,
                     CASE WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type8 = c_track_bill_type_participant  AND v_bill_payout_code_8 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic8_value,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_creator AND v_bill_payout_code_8 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value8,1,25)
                             WHEN v_bill_payout_code_type8 = c_track_bill_type_other AND v_bill_payout_code_8 IS NOT NULL THEN v_bill_payout_code_8 
                        END,
                     CASE WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type9 = c_track_bill_type_participant  AND v_bill_payout_code_9 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic9_value,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_creator AND v_bill_payout_code_9 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value9,1,25)
                             WHEN v_bill_payout_code_type9 = c_track_bill_type_other AND v_bill_payout_code_9 IS NOT NULL THEN v_bill_payout_code_9 
                     END,
                     CASE WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_orgUnitName THEN SUBSTR(rec_cur_stack_rank.pax_node_name,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_userName    THEN SUBSTR(rec_cur_stack_rank.user_name,1,25)  
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_countryCode THEN SUBSTR(rec_cur_stack_rank.country_code,1,25)                          
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_department  THEN SUBSTR(rec_cur_stack_rank.department,1,25)
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_customValue THEN SUBSTR(rec_cur_stack_rank.custom_value_list,1,25)                                                    
                          WHEN v_bill_payout_code_type10 = c_track_bill_type_participant  AND v_bill_payout_code_10 = c_bill_code_characteristic THEN SUBSTR(rec_cur_stack_rank.characteristic10_value,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_orgUnitName   THEN SUBSTR(v_creator_node_name,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_userName      THEN SUBSTR(v_creator_user_name,1,25)                             
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_countryCode   THEN SUBSTR(v_creator_country_code,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_department    THEN SUBSTR(v_creator_department,1,25)                                                          
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_customValue   THEN SUBSTR(v_creator_custom_value_list,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_creator AND v_bill_payout_code_10 = c_bill_code_characteristic   THEN SUBSTR(v_characteristic_value10,1,25)
                             WHEN v_bill_payout_code_type10 = c_track_bill_type_other AND v_bill_payout_code_10 IS NOT NULL THEN v_bill_payout_code_10 
                        END,
                     p_in_user_id,/* CREATED_BY */
                     SYSDATE,/* DATE_CREATED */
                     NULL,/* MODIFIED_BY */
                     NULL,/* DATE_MODIFIED */
                     0/* VERSION */ 
                     );
               END IF;  --IF v_payout_type <> other      

              INSERT INTO ssi_contest_pax_payout (
                       ssi_contest_pax_payout_id, 
                       ssi_contest_id, 
                       ssi_contest_activity_id, 
                       user_id, 
                       payout_amount, 
                       payout_description,
                       stack_rank_position,
                       activity_amount,
                       badge_rule_id,
                       journal_id, 
                       created_by, 
                       date_created, 
                       modified_by, 
                       date_modified, 
                       version )       
            VALUES (
                       ssi_contest_pax_payout_pk_sq.NEXTVAL,
                       p_in_ssi_contest_id,
                       NULL, 
                       rec_cur_stack_rank.user_id,
                       rec_cur_stack_rank.payout_amt,
                       rec_cur_stack_rank.payout_description,
                       rec_cur_stack_rank.stack_rank_position,
                       rec_cur_stack_rank.activity_amt,
                       rec_cur_stack_rank.badge_rule_id,
                       v_journal_id,
                       p_in_user_id,
                       SYSDATE,
                       NULL,
                       NULL,
                       0
                    );            
                    
            IF rec_cur_stack_rank.promotion_id IS NOT NULL THEN  --04/14/2015
                    INSERT INTO participant_badge (
                       participant_badge_id, 
                       promotion_id, 
                       participant_id, 
                       is_earned, 
                       earned_date, 
                       sent_count, 
                       received_count, 
                       badge_rule_id, 
                       status,
                       is_earned_all_behavior_points,
                       is_earned_badge_points,
                       ssi_contest_id,
                       created_by,
                       date_created,                   
                       modified_by, 
                       date_modified,
                       version )       
            VALUES (
                       participant_badge_pk_sq.NEXTVAL,
                       rec_cur_stack_rank.promotion_id, 
                       rec_cur_stack_rank.user_id, 
                       1, 
                       TRUNC(SYSDATE), 
                       0, 
                       0, 
                       rec_cur_stack_rank.badge_rule_id, 
                       'A',
                       0,
                       0,                   
                       p_in_ssi_contest_id,
                       p_in_user_id,
                       SYSDATE,
                       NULL,
                       NULL,
                       0
                    );   
          END IF; --04/14/2015
        END LOOP; --LOOP cur_stack_rank
       
       END IF;--v_has_tie > 0
   
   
   END IF; --IF v_contest_type
   
   IF v_contest_type <> c_award_them_now THEN
   update ssi_contest 
   set status = 'finalize_results',
       payout_issue_date = TRUNC(SYSDATE),
       version = version + 1 
   where ssi_contest_id =  p_in_ssi_contest_id;
   
   ELSE
   
   update ssi_contest_atn
   set issuance_status = 'finalize_results',
       payout_issue_date = TRUNC(SYSDATE),
       version = version + 1 
   where ssi_contest_id =  p_in_ssi_contest_id
   and  issuance_number = p_in_award_issuance_number;
   
   END IF; --Update status of ssi_contest_atn table in case of ATN contest.
   
  p_out_return_code := 0;
   prc_execution_log_entry(v_process_name, v_release_level, 'INFO', 
                           'Process Completed'
                           , NULL);

  EXCEPTION 
   WHEN exit_program_exception THEN
     p_out_return_code := 99;
     v_execution_log_msg := SQLERRM;
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',SQLERRM,null);
   WHEN OTHERS THEN
     p_out_return_code := 99;
     v_execution_log_msg := SQLERRM;
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',SQLERRM,null);

END  PRC_SSI_CONTEST_PAX_PAYOUT ;

PROCEDURE prc_ssi_contest_user_managers (
      p_in_ssi_contest_id     IN     NUMBER,      
      p_in_user_id        IN     NUMBER,
      p_out_return_code          OUT NUMBER,
      p_out_count_mgr_owner      OUT NUMBER,
      p_out_ref_cursor           OUT SYS_REFCURSOR) IS
      
      /*******************************************************************************
   -- Purpose: To provide list of eligible managers for a user with in a contest.
   --
   -- Person                    Date         Comments
   -- -----------                --------    -----------------------------------------------------
   -- Ravi Dhanekula      05/01/2015    Initlal Creation.     
   *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_SSI_CONTEST_USER_MANAGERS';
  v_release_level         execution_log.release_level%type := '1';
  v_execution_log_msg     execution_log.text_line%TYPE;
      BEGIN
      
      OPEN p_out_ref_cursor FOR
      SELECT  DISTINCT un2.user_id,au_mgr.first_name,au_mgr.last_name
                                        FROM                        (SELECT np.node_id,
                                                                       p.column_value AS path_node_id
                                                                  FROM ( -- get node hierarchy path
                                                                         SELECT h.node_id,
                                                                                level AS hier_level,
                                                                                sys_connect_by_path(node_id, '/') || '/' AS node_path
                                                                           FROM node h
                                                                          START WITH h.parent_node_id IS NULL
                                                                        CONNECT BY PRIOR h.node_id = h.parent_node_id
                                                                       ) np,
                                                                       -- parse node path into individual nodes
                                                                       -- pivoting the node path into separate records
                                                                       TABLE( CAST( MULTISET(
                                                                          SELECT TO_NUMBER(
                                                                                    SUBSTR(np.node_path,
                                                                                           INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                                                                           INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                                                                    )
                                                                                 )
                                                                            FROM dual
                                                                         CONNECT BY LEVEL <= np.hier_level 
                                                                       ) AS sys.odcinumberlist ) ) p) npn,
                                                                       (SELECT node_id,NAME as node_name,parent_node_id,path      
                                                                        FROM node n
                                                                        START WITH parent_node_id IS NULL
                                                                        CONNECT BY PRIOR node_id = parent_node_id) ip
                                                                        ,user_node un
                                                                        ,application_user au
                                                                        ,ssi_contest_manager scm
                                                                        ,user_node un2
                                                                        ,application_user au_mgr
                                        where ip.node_id = npn.path_node_id
                                              AND npn.node_id = un.node_id
                                              AND un.user_id = au.user_id
                                              AND ip.node_id = un2.node_id
                                              AND un2.role in ('own','mgr')
                                              AND un2.user_id = scm.user_id
                                              AND scm.ssi_contest_id = p_in_ssi_contest_id
                                              AND un.user_id = p_in_user_id
                                              AND au_mgr.user_id = un2.user_id
                                              AND un2.status = 1
                                              AND au.is_active = 1
                                              AND au.user_type = 'pax'
                                              AND un.status = 1;
                                              
                                              SELECT  COUNT(DISTINCT un2.user_id) INTO p_out_count_mgr_owner
                                        FROM                        (SELECT np.node_id,
                                                                       p.column_value AS path_node_id
                                                                  FROM ( -- get node hierarchy path
                                                                         SELECT h.node_id,
                                                                                level AS hier_level,
                                                                                sys_connect_by_path(node_id, '/') || '/' AS node_path
                                                                           FROM node h
                                                                          START WITH h.parent_node_id IS NULL
                                                                        CONNECT BY PRIOR h.node_id = h.parent_node_id
                                                                       ) np,
                                                                       -- parse node path into individual nodes
                                                                       -- pivoting the node path into separate records
                                                                       TABLE( CAST( MULTISET(
                                                                          SELECT TO_NUMBER(
                                                                                    SUBSTR(np.node_path,
                                                                                           INSTR(np.node_path, '/', 1, LEVEL)+1, 
                                                                                           INSTR(np.node_path, '/', 1, LEVEL+1) - INSTR(np.node_path, '/', 1, LEVEL)-1 
                                                                                    )
                                                                                 )
                                                                            FROM dual
                                                                         CONNECT BY LEVEL <= np.hier_level 
                                                                       ) AS sys.odcinumberlist ) ) p) npn,
                                                                       (SELECT node_id,NAME as node_name,parent_node_id,path      
                                                                        FROM node n
                                                                        START WITH parent_node_id IS NULL
                                                                        CONNECT BY PRIOR node_id = parent_node_id) ip
                                                                        ,user_node un
                                                                        ,application_user au
                                                                        ,ssi_contest_manager scm
                                                                        ,user_node un2
                                        where ip.node_id = npn.path_node_id
                                              AND npn.node_id = un.node_id
                                              AND un.user_id = au.user_id
                                              AND ip.node_id = un2.node_id
                                              AND un2.role in ('own','mgr')
                                              AND un2.user_id = scm.user_id
                                              AND scm.ssi_contest_id = p_in_ssi_contest_id
                                             AND un.user_id = p_in_user_id
                                              AND un2.status = 1
                                              AND au.is_active = 1
                                              AND au.user_type = 'pax'
                                              AND un.status = 1;
                                              
                                              p_out_return_code := 0;
            EXCEPTION    
   WHEN OTHERS THEN
     p_out_return_code := 99;
     v_execution_log_msg := SQLERRM;
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR','p_in_ssi_contest_id : '||p_in_ssi_contest_id||' p_in_user_id : '||p_in_user_id||' '|| SQLERRM,null);                                  
                                           
      END prc_ssi_contest_user_managers;
      
PROCEDURE prc_ssi_contest_sr_payouts (
   p_in_ssi_contest_id             IN     NUMBER,
   p_out_return_code               OUT NUMBER,
   p_out_sr_payout_ref_cursor      OUT SYS_REFCURSOR)
IS
/***********************************************************************************
      Purpose:  Procedure to provide the detail pax progress for a contest.

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
      KrishnaDeepika     05/28/2015     Creating a procedure to get the list of Stack Rank Payout details on a SR contest
   ************************************************************************************/
   v_out_contest_type    NUMBER;
   v_creator_user_id     NUMBER;
   v_locale              VARCHAR2 (10);
BEGIN

       DELETE FROM temp_table_session;

 INSERT INTO temp_table_session
                SELECT cav.asset_code,
                          MAX (DECODE (cav.key, 'NAME', cav.cms_value, NULL)) AS cms_name,
                         MAX (DECODE (cav.key, 'EARNED_IMAGE_SMALL', cav.cms_value, NULL)) AS cms_code
                       FROM vw_cms_asset_value cav
                       WHERE asset_code='promotion.badge'
                       AND locale = v_locale
                   GROUP BY cav.asset_code,
                            cav.locale,
                            cav.asset_id,
                            cav.content_key_id,
                            cav.content_id;
                            
      SELECT contest_type,contest_owner_id --created_by --05/21/2015
          INTO v_out_contest_type,v_creator_user_id
      FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;

      IF v_out_contest_type = 8 THEN
      SELECT NVL(language_id,(SELECT STRING_VAL FROM os_propertyset WHERE entity_name = 'default.language')) INTO v_locale FROM application_user WHERE user_id = v_creator_user_id;
      END IF;

   prc_execution_log_entry ('prc_ssi_contest_sr_payouts',1,'INFO','Process Started. Parameters - '||' v_creator_user_id :'|| v_creator_user_id|| ' p_in_ssi_contest_id :'|| p_in_ssi_contest_id, NULL);                                                      --05/14/2015

   IF v_out_contest_type = 8
   THEN  
      OPEN p_out_sr_payout_ref_cursor FOR
         SELECT rank_position,
                payout_amount,
                payout_desc,
                br.badge_rule_id,
                NVL ( (fnc_get_badge_cms_value (br.badge_rule_id, v_locale)),
                     (fnc_get_badge_cms_value (br.badge_rule_id, 'en_US'))) --05/14/2015
                   badge_name,
                cms_badge.image_small_URL badge_image
           FROM ssi_contest_sr_payout sc,
                badge_rule br,
                (SELECT asset_code, cms_name, cms_code image_small_URL
                   FROM temp_table_session) cms_badge
          WHERE     ssi_contest_id = p_in_ssi_contest_id
                AND sc.badge_rule_id = br.badge_rule_id(+)
                AND br.cm_asset_key = cms_badge.cms_name(+)
                order by rank_position;

      prc_execution_log_entry ('prc_ssi_contest_sr_payouts',1,'INFO',p_in_ssi_contest_id || ' ' || SQLERRM,NULL);

   ELSE
      OPEN p_out_sr_payout_ref_cursor FOR
       SELECT * FROM DUAL WHERE 1 = 2;
   END IF;

   p_out_return_code := 0;
   
EXCEPTION WHEN OTHERS THEN
       p_out_return_code :=99;
       prc_execution_log_entry ('prc_ssi_contest_sr_payouts',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);
END prc_ssi_contest_sr_payouts;

PROCEDURE PRC_SSI_CONTEST_CLAIMS_UPDATE
(     p_in_ssi_contest_id      IN NUMBER,
      p_out_count_claims       OUT NUMBER,
      p_out_error_message      OUT VARCHAR2,
      p_out_return_code        OUT NUMBER
      )
IS
   /*******************************************************************************
--      Purpose: Proc to update SSI_CONTEST_PAX_CLAIM and SSI_CONTEST_PAX_PROGRESS for Approved Claims.
  
   -- Person                    Date         Comments
   -- -----------                --------    ----------------
   -- Suresh J                  03/03/2015   Initlal Creation.
   --Ravi Dhanekula        06/15/2015    Bug # 62708 - Approved Claim and click on update button - The activity did not get updated in the details page
   --Ravi Dhanekula        06/15/2015    Bug # 62822 -- Participant-Submit Claim enabled-Participant's activity is not getting added instead getting updated by last progress value
   *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_SSI_CONTEST_CLAIMS_UPDATE';
  v_release_level         execution_log.release_level%type := '1';

  v_contest_owner_id     ssi_contest.contest_owner_id%type;  --05/21/2015
  v_date DATE := SYSDATE;

  --Contest Types
  c_award_them_now constant number := 1;
  c_stack_rank constant number := 8;
  c_do_this_get_that constant number := 2;
  c_objectives constant number := 4;
  c_step_it_up constant number := 16;
  v_contest_type NUMBER;--06/15/2015

BEGIN

   prc_execution_log_entry(v_process_name, v_release_level, 'INFO', 
                           'Process Started. Parameters - '||
                           ' p_in_ssi_contest_id :' || p_in_ssi_contest_id
                           , NULL);

SELECT NVL(contest_owner_id,created_by),contest_type INTO v_contest_owner_id,v_contest_type FROM ssi_contest WHERE ssi_contest_id = p_in_ssi_contest_id;

IF v_contest_type <> 2 THEN --06/15/2015--v_contest_type <> 2 
MERGE INTO ssi_contest_pax_progress d USING
(
   SELECT spc.ssi_contest_id, 
          spc.submitter_id, 
          SUM(spc.claim_amount_quantity) as total_claim          
   FROM  ssi_contest_pax_claim spc
   WHERE spc.ssi_contest_id = p_in_ssi_contest_id AND
         spc.date_results_posted IS NULL AND
         spc.status = 'approved'    
   GROUP BY spc.ssi_contest_id,submitter_id
  ) s  
    ON (d.ssi_contest_id = s.ssi_contest_id AND d.user_id = s.submitter_id)
WHEN MATCHED THEN 
        UPDATE SET activity_amt  = activity_amt + s.total_claim,--62822 --06/15/2015
                   activity_date = v_date,
                   modified_by   = v_contest_owner_id,
                   date_modified = SYSDATE,
                   version = version+1
WHEN NOT MATCHED THEN
INSERT
( 
    ssi_contest_pax_progress_id, 
    ssi_contest_id, 
    user_id, 
    activity_amt, 
    activity_date, 
    created_by, 
    date_created, 
    version
)
VALUES
(
    SSI_CONTEST_PAX_PROGRESS_PK_SQ.NEXTVAL, 
    s.ssi_contest_id, 
    s.submitter_id,
    s.total_claim,
    v_date,
    v_contest_owner_id,
    SYSDATE,
    0
  );
  
ELSE --v_contest_type <> 2 

MERGE INTO ssi_contest_pax_progress d USING --06/15/2015
(
SELECT sca.ssi_contest_id,a.user_id,ssi_contest_activity_id,SUM(a.activity_amount) activity_amount FROM (      
      SELECT ssi_contest_id,submitter_id user_id,text activity_amount,RANK () 
                                                                     OVER (
                                                                        PARTITION BY rec_rank
                                                                        ORDER BY
                                                                           rn)
                                                                        AS rec_rank2 FROM (
     SELECT RANK () 
                                                                     OVER (
                                                                        PARTITION BY ssi_contest_id
                                                                        ORDER BY
                                                                           ssi_contest_pax_claim_id)
                                                                        AS rec_rank,
                                                                        ssi_contest_id,submitter_id,trim(regexp_substr(REPLACE(activities_amount_quantity,',',' , '), '[^,]+', 1, column_value)) text,rownum rn
    FROM ssi_contest_pax_claim,
     TABLE (CAST (MULTISET
     (SELECT LEVEL FROM dual CONNECT BY  instr(REPLACE(activities_amount_quantity,',',' , '), ',', 1, LEVEL - 1) > 0)
                   AS sys.odciNumberList
                 )
        )                                                                    
                                                                 WHERE ssi_contest_id=p_in_ssi_contest_id 
                                                                 AND date_results_posted IS NULL 
                                                                 AND status = 'approved')) a,
                                                                 ssi_contest_activity sca
                                                                 WHERE a.ssi_contest_id = sca.ssi_contest_id
                                                                 AND a.rec_rank2 = sca.sequence_number
                                                                 GROUP BY sca.ssi_contest_id,ssi_contest_activity_id,user_id
) s  
    ON (d.ssi_contest_id = s.ssi_contest_id AND d.user_id = s.user_id AND d.ssi_contest_activity_id = s.ssi_contest_activity_id)
WHEN MATCHED THEN 
        UPDATE SET activity_amt  = activity_amt + s.activity_amount,--62822 --06/15/2015
                   activity_date = v_date,
                   modified_by   = v_contest_owner_id,
                   date_modified = SYSDATE,
                   version = version+1
WHEN NOT MATCHED THEN
INSERT
( 
    ssi_contest_pax_progress_id, 
    ssi_contest_id, 
    ssi_contest_activity_id,
    user_id, 
    activity_amt, 
    activity_date, 
    created_by, 
    date_created, 
    version
)
VALUES
(
    SSI_CONTEST_PAX_PROGRESS_PK_SQ.NEXTVAL, 
    s.ssi_contest_id, 
    s.ssi_contest_activity_id,
    s.user_id,
    s.activity_amount,
    v_date,
    v_contest_owner_id,
    SYSDATE,
    0
  );

END IF;--v_contest_type <> 2 

  UPDATE  ssi_contest_pax_claim 
  SET  date_results_posted = v_date,
       modified_by = v_contest_owner_id,
       date_modified = v_date, 
       version = version + 1        
  WHERE ssi_contest_id = p_in_ssi_contest_id AND
        date_results_posted IS NULL AND
        status = 'approved';    
  p_out_count_claims := SQL%ROWCOUNT;
  
  UPDATE  ssi_contest --To update last_progress_update_date
  SET  last_progress_update_date = v_date,
       update_in_progress = 0,--Bug # 72320.
       modified_by = v_contest_owner_id,
       date_modified = v_date, 
       version = version + 1        
  WHERE ssi_contest_id = p_in_ssi_contest_id;  

  p_out_return_code := 0;
   prc_execution_log_entry(v_process_name, v_release_level, 'INFO', 
                           'Process Completed'
                           , NULL);
  EXCEPTION 
   WHEN OTHERS THEN
     p_out_return_code := 99;
     p_out_error_message := SQLERRM;
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',SQLERRM,null);
     rollback;

END  prc_ssi_contest_claims_update;
 
END PKG_SSI_CONTEST;
/
