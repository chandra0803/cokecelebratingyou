CREATE OR REPLACE PACKAGE pkg_populate_promo_elig  IS

   PROCEDURE p_populate_PaxPromoEligibility
   (p_in_promotion_type IN promotion.promotion_type%TYPE,
    p_in_bulk_limit     IN INTEGER    DEFAULT 100000,p_out_return_code OUT NUMBER);
    
    PROCEDURE p_populate_PaxPromoElig_Quiz
   (p_in_bulk_limit     IN INTEGER    DEFAULT 100000,p_out_return_code OUT NUMBER);

   PROCEDURE p_pop_PaxPromoCritAudReceiver (p_in_promotion_type IN promotion.promotion_type%TYPE);
   
   PROCEDURE p_rpt_pax_elig_allaud_node  (p_in_requested_user_id      IN  NUMBER,
                                      p_out_return_code           OUT NUMBER,
                                      p_out_error_message         OUT VARCHAR2);
   PROCEDURE p_rpt_pax_elig_allaud_team  (p_in_requested_user_id      IN  NUMBER,
                                      p_out_return_code           OUT NUMBER,
                                      p_out_error_message         OUT VARCHAR2);
    PROCEDURE p_rpt_pax_elig_speaud_team  (p_in_requested_user_id      IN  NUMBER,
                                      p_out_return_code           OUT NUMBER,
                                      p_out_error_message         OUT VARCHAR2);
   PROCEDURE p_rpt_pax_elig_speaud_node  (p_in_requested_user_id      IN  NUMBER,
                                      p_out_return_code           OUT NUMBER,
                                      p_out_error_message         OUT VARCHAR2);
END pkg_populate_promo_elig; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_populate_promo_elig
IS
/*******************************************************************************
--
-- Purpose: Populate rpt_pax_promo_eligibility table.
--
-- MODIFICATION HISTORY
-- Person      Date         Comments
-- ---------   ----------   ------------------------------------------
-- ---         --           Initial Version
-- S. Majumder 08/11/2008   Added Sections for Challengepoint Promotions
-- Arun S      09/14/2010   Bug 34295 Fix, Modified c_promo_list Sql in
--                          p_populate_paxpromoeligibility and p_pop_paxpromocritaudreceiver
-- Arun S      12/27/2010   Bug 34296 Fix as per Bug description comment,add distinct to c_giver_nodes
--                          in p_clone_giver_node to prevent duplicate entries in rpt_pax_promo_eligibility table
-- J Flees     08/23/2011   Modified processes to insert records in bulk rather than one at a time.
--                          Removed extraneous processing code.
-- Chidamba    21/02/2012   Bug # 39774 - Changes to include live and expired promotion for all promotion_type
--                          (but just not deleted promotion)
-- J Flees       05/02/2013 Altered process to remove massive inserts/deletes report table through use of a report "staging" table.
--Ravi Dhanekula 04/09/2014 Added the case condition to generate quiz data with 'receiver' type as primary. This is helpful for award reports. bug # 52098
                 05/13/2014 Added condition for diy_quiz. Bug # 52681
-- murphyc       07/22/2014 Remove allactivepaxaudience recs from rpt_pax_promo_eligibility
--                          being taken care of in ALLAUD_NODE/TEAM tables
--Ravi Dhanekula 08/06/2014 Changed the giver_recvr_type for quiz and diy_quiz promotions to 'giver' instead of 'taker'. And changed product_claim primary audience data from submitter to 'receiver' to count in award reports.
--               12/11/2014 Bug # 58623 Changed the UNION ALL to UNION as we are getting duplicate entrees for product-claim promotion in case if a user is listed in both primary and secondary audience.
-Suresh J        03/16/2015 Bug 60433 - Report Refresh error: Added SSI Promotion Type for the "Giver/Receiver" column  
--Ravi Dhanekula 04/03/2015 Bug # 61090 Added a fix for the issue with eligible count on recognitions given by org report.
--Ravi Dhanekula 07/20/2016 Replaced Obfuscation toolkit with dbms_crypto md5
--Chidamba       03/02/2018 Bug 72971 - populate GTT_PROMO_LIST instead of using "WITH promo_list AS" (copy tunning change from acqp)
*******************************************************************************/

/*******************************************************************************
-- Procedure removes all report records for the specified promotion type,
-- then inserts all report records for the specified promotion type
-- based upon the promotion's primary and secondary audience types.
*******************************************************************************/
PROCEDURE p_populate_PaxPromoEligibility
( p_in_promotion_type IN promotion.promotion_type%TYPE,
  p_in_bulk_limit     IN INTEGER    DEFAULT 100000,
  p_out_return_code OUT NUMBER
) AS
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name          CONSTANT execution_log.process_name%TYPE := UPPER('p_populate_PaxPromoEligibility');
   c_release_level         CONSTANT execution_log.release_level%TYPE := 3.0;

   v_msg                   execution_log.text_line%TYPE;
   v_rec_cnt               INTEGER;
   v_stg_rec_cnt           INTEGER;
   v_done                  BOOLEAN;

   v_tab_promotion_id      dbms_sql.NUMBER_table;
   v_tab_participant_id    dbms_sql.NUMBER_table;
   v_tab_node_id           dbms_sql.NUMBER_table;
   v_tab_audience_id       dbms_sql.NUMBER_table;
   v_tab_giver_recvr_type  dbms_sql.VARCHAR2_table;

   -- build insert records
   CURSOR cur_pax_promo_elig IS
   SELECT i.promotion_id,
          i.participant_id,
          i.node_id,
          i.audience_id,
          i.giver_recvr_type
     FROM (
            WITH 
--            promo_list AS -- 03/02/2018
--            (  -- get driver list of promotions
--               SELECT p.promotion_id,
--                      p.promotion_type,
--                      LOWER(p.primary_audience_type) AS primary_audience_type,
--                      LOWER(p.secondary_audience_type) AS secondary_audience_type
--                 FROM promotion p
--               WHERE p.promotion_type = p_in_promotion_type
--                  AND promotion_status IN ('live','expired')
--                  AND is_deleted = 0
--                 /*AND (  (   p.promotion_status = 'live')           --21/02/2012
--                       OR (   p.promotion_type = 'goalquest'
--                          AND p.promotion_status IN ('live','expired')
--                          ))*/
--            ), 
            primary_list AS
            (  -- get primary audience records
               -- Primary Audience is AllActivePaxAudience --07/22/2014
               /*SELECT pl.*,
                      p.user_id AS participant_id,
                      un.node_id,
                      0 AS audience_id, -- no audience record for this audience type
                      CASE WHEN pl.promotion_type = 'quiz' THEN  --04/09/2014
                      'receiver'
                               WHEN pl.promotion_type = 'diy_quiz' THEN
                      'receiver'
                      ELSE 'giver'
                      END AS giver_recvr_type
                 FROM promo_list pl,
                      participant p,
                      user_node un
                WHERE pl.primary_audience_type = 'allactivepaxaudience'
                   -- cartesian join active participants to promotions
                  AND p.status = 'active'
                  AND p.user_id = un.user_id
                UNION ALL */
               -- Primary Audience is SpecifyAudience
               SELECT pl.*,
                      pa.user_id AS participant_id,
                      un.node_id,
                      a.audience_id,
                      DECODE( pl.promotion_type,
                         'product_claim',  'receiver', --Changed this from submitter to receiver to count product claims also in award reports.
                         'recognition',    'giver',
                         'nomination',     'giver',
                         'quiz',           'giver', --08/06/2014
                         'diy_quiz',           'giver', --05/13/2014 --08/06/2014
                         'goalquest',      'pax',
                         'challengepoint', 'pax',
                         'survey', 'pax'
                         ,'self_serv_incentives','pax'            --03/16/2015
                      ) AS giver_recvr_type
                 FROM --promo_list pl, -- 03/02/2018
                      GTT_PROMO_LIST PL, -- 03/02/2018
                      promo_audience pra,
                      audience a,
                      participant_audience pa,
                      user_node un
                WHERE pl.primary_audience_type = 'specifyaudience'
                  AND pl.promotion_id = pra.promotion_id
                  AND pra.promo_audience_type = 'PRIMARY'
                  AND pra.audience_id = a.audience_id
                  --AND a.list_type = 'pax'
                  AND a.audience_id = pa.audience_id
                  AND pa.user_id = un.user_id
            ), promo_root_node AS
            ( -- get root node level(s) for entire promotion based on secondary audience and giver type
            SELECT pnl.promotion_id,
                   pnl.node_id AS root_node
                   , pnl.promotion_type
                   , pnl.primary_audience_type
                   , pnl.secondary_audience_type
              FROM (
                     -- get promotions by node level
                     SELECT DISTINCT
                            pri.promotion_id,
                            pri.node_id,
                            nl.node_level,
                            MIN(nl.node_level) OVER (PARTITION BY pri.promotion_id) AS min_node_level
                            , pri.promotion_type
                            , pri.primary_audience_type
                            , pri.secondary_audience_type
                       from primary_list pri,
                            ( -- get all active nodes by level
                              SELECT node_id,
                                     level AS node_level
                                FROM node
                               WHERE is_deleted = 0
                             CONNECT BY PRIOR node_id = parent_node_id
                               START WITH parent_node_id IS NULL
                            ) nl
                      WHERE pri.node_id = nl.node_id
                        AND pri.secondary_audience_type = 'activepaxfromprimarynodebelowaudience'
                        AND pri.giver_recvr_type = 'giver'
                   ) pnl
             WHERE pnl.node_level = min_node_level
            )
            -- primary audience records
            SELECT p.promotion_id,
                   p.participant_id,
                   p.node_id,
                   p.audience_id,
                   p.giver_recvr_type
                   , p.promotion_type
                   , p.primary_audience_type
                   , p.secondary_audience_type
              FROM ( -- get records with pax sequence
                     SELECT pri.promotion_id,
                            pri.participant_id,
                            pri.node_id,
                            pri.audience_id,
                            pri.giver_recvr_type
                            -- sequence records
                            , ROW_NUMBER() OVER (PARTITION BY pri.giver_recvr_type, pri.promotion_id, pri.participant_id
                                                 ORDER BY pri.node_id
                                                ) AS pax_seq
                            , pri.promotion_type
                            , pri.primary_audience_type
                            , pri.secondary_audience_type
                       FROM primary_list pri
                   ) p
             WHERE (  p.giver_recvr_type != 'pax'
                   OR (   -- only 1 pax record per promotion/participant
                          p.giver_recvr_type = 'pax'
                      AND pax_seq = 1
                      )
                   )
             UNION --12/11/2014
            -- secondary audience AllActivePaxAudience records
            -- get all active participants based on promotion list
            /*SELECT pl.promotion_id,  --07/22/2014
                   p.user_id AS participant_id,
                   un.node_id,
                   0 AS audience_id, -- no audience record for this audience type
                   'receiver' AS giver_recvr_type
                   , pl.promotion_type
                   , pl.primary_audience_type
                   , pl.secondary_audience_type
              FROM promo_list pl,
                   participant p,
                   user_node un
             WHERE pl.secondary_audience_type = 'allactivepaxaudience'
                -- cartesian join active participants to promotions
               AND p.status = 'active'
               AND p.user_id = un.user_id
             UNION ALL */
            -- secondary audience SpecifyAudience records
            -- get all secondary pax audience participants based on promotion list
            SELECT pl.promotion_id,
                   pa.user_id AS participant_id,
                   un.node_id,
                   a.audience_id,
                   DECODE( pl.promotion_type,
                      'product_claim',  'receiver',
                      'recognition',    'receiver',
                      'nomination',     'receiver',
                      'quiz',           'receiver',
                      'goalquest',      'pax',
                      'challengepoint', 'pax',
                      'survey', 'pax'
                       ,'self_serv_incentives','pax'            --03/16/2015                      
                   ) AS giver_recvr_type
                   , pl.promotion_type
                   , pl.primary_audience_type
                   , pl.secondary_audience_type
              FROM --promo_list pl, -- 03/02/2018
                   GTT_PROMO_LIST PL, -- 03/02/2018
                   promo_audience pra,
                   audience a,
                   participant_audience pa,
                   user_node un
             WHERE pl.secondary_audience_type = 'specifyaudience'
               AND pl.promotion_id = pra.promotion_id
               AND pra.promo_audience_type = 'SECONDARY'
               AND pra.audience_id = a.audience_id
               --AND a.list_type = 'pax'
               AND a.audience_id = pa.audience_id
               AND pa.user_id = un.user_id
             UNION ALL
            -- secondary audience SameAsPrimaryAudience records
            -- get reciever participants based on primary list giver
            SELECT pri.promotion_id,
                   pri.participant_id,
                   pri.node_id,
                   pri.audience_id,
                   'receiver' AS giver_recvr_type
                   , pri.promotion_type
                   , pri.primary_audience_type
                   , pri.secondary_audience_type
              FROM primary_list pri
             WHERE pri.secondary_audience_type = 'sameasprimaryaudience'
               AND pri.giver_recvr_type = 'giver'
             UNION ALL
            -- secondary audience ActivePaxFromPrimaryNodeAudience records
            -- get reciever participants based on primary list giver node
            SELECT DISTINCT
                   pri.promotion_id,
                   p.user_id AS participant_id,
                   un.node_id,
                   0 AS audience_id,
                   'receiver' AS giver_recvr_type
                   , pri.promotion_type
                   , pri.primary_audience_type
                   , pri.secondary_audience_type
              FROM primary_list pri,
                   user_node un,
                   participant p
             WHERE pri.secondary_audience_type = 'activepaxfromprimarynodeaudience'
               AND pri.giver_recvr_type = 'giver'
               AND pri.node_id = un.node_id
               AND un.user_id = p.user_id
               AND p.status = 'active'
             UNION ALL
            -- secondary audience ActivePaxFromPrimaryNodeBelowAudience records
            -- get reciever participants based on primary list giver root node
            SELECT DISTINCT
                   prn.promotion_id,
                   p.user_id AS participant_id,
                   un.node_id,
                   0 AS audience_id,
                   'receiver' AS giver_recvr_type
                   , prn.promotion_type
                   , prn.primary_audience_type
                   , prn.secondary_audience_type
              FROM ( -- get nodes and below for all promo root nodes
                     SELECT node_id,
                            sys_connect_by_path(node_id, '/') as node_path
                       FROM node
                      WHERE is_deleted = 0
                    CONNECT BY PRIOR node_id = parent_node_id
                      START WITH node_id IN
                            (SELECT root_node
                               FROM promo_root_node
                            )
                   ) np,
                   participant p,
                   user_node un,
                   promo_root_node prn
             WHERE un.user_id = p.user_id
               and p.status = 'active'
               AND un.node_id = np.node_id
                -- match promo root node to node path root
               AND prn.root_node = NVL(TO_NUMBER(SUBSTR( np.node_path, 2, (INSTR( np.node_path, '/', 2)-2))), np.node_id)
          ) i;
   -- end cur_pax_promo_elig

BEGIN
   v_msg := 'Start'
      || ': p_in_promotion_type >' || p_in_promotion_type
      || '<, p_in_bulk_limit >' || p_in_bulk_limit
      || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   -- remove all existing records for specified promotion type
   v_msg := 'TRUNCATE rpt_pax_promo_elig_stage FOR p_in_promotion_type >' || p_in_promotion_type || '<';
   EXECUTE IMMEDIATE 'TRUNCATE TABLE rpt_pax_promo_elig_stage';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);
   
  DELETE FROM gtt_promo_list; -- 03/02/2018
  INSERT INTO gtt_promo_list
	SELECT p.promotion_id,  
	  		 p.promotion_type,  
				 LOWER(p.primary_audience_type) AS primary_audience_type,  
				 LOWER(p.secondary_audience_type) AS secondary_audience_type  
  FROM promotion P  
  WHERE p.promotion_type = p_in_promotion_type--'badge'  
  AND promotion_status IN ('live','expired')  
  AND is_deleted = 0  
  AND (p.primary_audience_type IS NOT NULL OR p.secondary_audience_type IS NOT NULL);

   -- stage all records for specified promotion type
   v_msg := 'OPEN cur_pax_promo_elig';
   OPEN cur_pax_promo_elig;

   v_done := FALSE;
   v_stg_rec_cnt := 0;
   WHILE (NOT v_done) LOOP
      v_msg := 'FETCH cur_pax_promo_elig';
      FETCH cur_pax_promo_elig
       BULK COLLECT
       INTO v_tab_promotion_id,
            v_tab_participant_id,
            v_tab_node_id,
            v_tab_audience_id,
            v_tab_giver_recvr_type
      LIMIT p_in_bulk_limit;

      v_done := cur_pax_promo_elig%NOTFOUND;

      v_rec_cnt := v_tab_promotion_id.COUNT;
      v_stg_rec_cnt := v_stg_rec_cnt + v_rec_cnt;
--      prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ' (' || v_rec_cnt || ' records )', NULL);

      IF (v_rec_cnt > 0) THEN
         v_msg := 'INSERT rpt_pax_promo_elig_stage';
         FORALL indx IN 1 .. v_tab_promotion_id.COUNT
            INSERT /*+ append */ INTO rpt_pax_promo_elig_stage
            (  key_field_hash,
               promotion_id,
               participant_id,
               node_id,
               audience_id,
               giver_recvr_type
            )
            VALUES
            (  dbms_crypto.hash( utl_raw.cast_to_raw(--07/20/2016
                            v_tab_promotion_id(indx)
                  || '/' || v_tab_participant_id(indx)
                  || '/' || v_tab_node_id(indx)
                  || '/' || v_tab_audience_id(indx)
                  || '/' || v_tab_giver_recvr_type(indx)
               ),2),
               v_tab_promotion_id(indx),
               v_tab_participant_id(indx),
               v_tab_node_id(indx),
               v_tab_audience_id(indx),
               v_tab_giver_recvr_type(indx)
            );

         v_rec_cnt := SQL%ROWCOUNT;
--         prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ' (' || v_rec_cnt || ' records )', NULL);
      END IF;

      v_msg := 'Commit staged records';
      COMMIT;
   END LOOP; -- while not done

   CLOSE cur_pax_promo_elig;

   v_msg := 'Insert rpt_pax_promo_elig_stage';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ' (' || v_stg_rec_cnt || ' records)', NULL);

   -- remove obsolete records
   v_msg := 'DELETE rpt_pax_promo_eligibility';
   DELETE rpt_pax_promo_eligibility
    WHERE ROWID IN
          (  -- report record not matched with stage table
            SELECT ppe.ROWID
              FROM promotion p,
                   rpt_pax_promo_eligibility ppe,
                   rpt_pax_promo_elig_stage stg
             WHERE p.promotion_type = p_in_promotion_type
               AND p.promotion_id = ppe.promotion_id
               AND ppe.key_field_hash = stg.key_field_hash (+)
               AND stg.ROWID IS NULL
          );
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ' (' || v_rec_cnt || ' records)', NULL);

   -- add missing staged records
   v_msg := 'INSERT rpt_pax_promo_eligibility';
   INSERT INTO rpt_pax_promo_eligibility
   (  key_field_hash,
      promotion_id,
      participant_id,
      node_id,
      audience_id,
      giver_recvr_type,
      created_by,
      date_created
    )
    ( -- stage record not found in report table
      SELECT stg.key_field_hash,
             stg.promotion_id,
             stg.participant_id,
             stg.node_id,
             stg.audience_id,
             stg.giver_recvr_type,
             0 AS created_by,
             SYSDATE AS date_created
        FROM rpt_pax_promo_elig_stage stg,
             rpt_pax_promo_eligibility ppe
       WHERE stg.key_field_hash = ppe.key_field_hash (+)
         AND ppe.ROWID IS NULL
    );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ' (' || v_rec_cnt || ' records)', NULL);

   v_msg := 'Success: p_in_promotion_type >' || p_in_promotion_type || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   COMMIT;
      p_out_return_code := 0;

EXCEPTION
   WHEN OTHERS THEN
    ROLLBACK;
      p_out_return_code   := 99;
         prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
 ROLLBACK;
END p_populate_PaxPromoEligibility;


PROCEDURE p_populate_PaxPromoElig_quiz
( p_in_bulk_limit     IN INTEGER    DEFAULT 100000,
  p_out_return_code OUT NUMBER
) AS
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name          CONSTANT execution_log.process_name%TYPE := UPPER('p_populate_PaxPromoElig_quiz');
   c_release_level         CONSTANT execution_log.release_level%TYPE := 3.0;

   v_msg                   execution_log.text_line%TYPE;
   v_rec_cnt               INTEGER;
   v_stg_rec_cnt           INTEGER;
   v_done                  BOOLEAN;

   v_tab_promotion_id      dbms_sql.NUMBER_table;
   v_tab_participant_id    dbms_sql.NUMBER_table;
   v_tab_node_id           dbms_sql.NUMBER_table;
   v_tab_audience_id       dbms_sql.NUMBER_table;
   v_tab_giver_recvr_type  dbms_sql.VARCHAR2_table;

   -- build insert records
   CURSOR cur_pax_promo_elig IS
   SELECT i.promotion_id,
          i.participant_id,
          i.node_id,
          i.audience_id,
          i.giver_recvr_type
     FROM (
            WITH 
--            promo_list AS -- 03/02/2018
--            (  -- get driver list of promotions
--               SELECT p.promotion_id,
--                      p.promotion_type,
--                      LOWER(p.primary_audience_type) AS primary_audience_type,
--                      LOWER(p.secondary_audience_type) AS secondary_audience_type
--                 FROM promotion p
--               WHERE p.promotion_type IN ('quiz','diy_quiz')
--                  AND promotion_status IN ('live','expired')
--                  AND is_deleted = 0                
--            ), 
            primary_list AS
            (  -- get primary audience records               
               SELECT pl.*,
                      p.user_id AS participant_id,
                      un.node_id,
                      0 AS audience_id, -- no audience record for this audience type
                     'giver' AS giver_recvr_type
                 FROM --promo_list pl, -- 03/02/2018
                      GTT_PROMO_LIST pl, -- 03/02/2018
                      participant p,
                      user_node un
                WHERE pl.primary_audience_type = 'allactivepaxaudience'
                   -- cartesian join active participants to promotions
                  AND p.status = 'active'
                  AND p.user_id = un.user_id
                UNION ALL
               -- Primary Audience is SpecifyAudience
               SELECT pl.*,
                      pa.user_id AS participant_id,
                      un.node_id,
                      a.audience_id,
                      DECODE( pl.promotion_type,                   
                         'quiz',           'giver', --08/06/2014
                         'diy_quiz',           'giver'--05/13/2014 --08/06/2014                        
                      ) AS giver_recvr_type
                 FROM --promo_list pl, -- 03/02/2018
                      GTT_PROMO_LIST pl, -- 03/02/2018
                      promo_audience pra,
                      audience a,
                      participant_audience pa,
                      user_node un
                WHERE pl.primary_audience_type = 'specifyaudience'
                  AND pl.promotion_id = pra.promotion_id
                  AND pra.promo_audience_type = 'PRIMARY'
                  AND pra.audience_id = a.audience_id
                  --AND a.list_type = 'pax'
                  AND a.audience_id = pa.audience_id
                  AND pa.user_id = un.user_id
            ), promo_root_node AS
            ( -- get root node level(s) for entire promotion based on secondary audience and giver type
            SELECT pnl.promotion_id,
                   pnl.node_id AS root_node
                   , pnl.promotion_type
                   , pnl.primary_audience_type
                   , pnl.secondary_audience_type
              FROM (
                     -- get promotions by node level
                     SELECT DISTINCT
                            pri.promotion_id,
                            pri.node_id,
                            nl.node_level,
                            MIN(nl.node_level) OVER (PARTITION BY pri.promotion_id) AS min_node_level
                            , pri.promotion_type
                            , pri.primary_audience_type
                            , pri.secondary_audience_type
                       from primary_list pri,
                            ( -- get all active nodes by level
                              SELECT node_id,
                                     level AS node_level
                                FROM node
                               WHERE is_deleted = 0
                             CONNECT BY PRIOR node_id = parent_node_id
                               START WITH parent_node_id IS NULL
                            ) nl
                      WHERE pri.node_id = nl.node_id
                        AND pri.secondary_audience_type = 'activepaxfromprimarynodebelowaudience'
                        AND pri.giver_recvr_type = 'giver'
                   ) pnl
             WHERE pnl.node_level = min_node_level
            )
            -- primary audience records
            SELECT p.promotion_id,
                   p.participant_id,
                   p.node_id,
                   p.audience_id,
                   p.giver_recvr_type
                   , p.promotion_type
                   , p.primary_audience_type
                   , p.secondary_audience_type
              FROM ( -- get records with pax sequence
                     SELECT pri.promotion_id,
                            pri.participant_id,
                            pri.node_id,
                            pri.audience_id,
                            pri.giver_recvr_type
                            -- sequence records
                            , ROW_NUMBER() OVER (PARTITION BY pri.giver_recvr_type, pri.promotion_id, pri.participant_id
                                                 ORDER BY pri.node_id
                                                ) AS pax_seq
                            , pri.promotion_type
                            , pri.primary_audience_type
                            , pri.secondary_audience_type
                       FROM primary_list pri
                   ) p
             WHERE (  p.giver_recvr_type != 'pax'
                   OR (   -- only 1 pax record per promotion/participant
                          p.giver_recvr_type = 'pax'
                      AND pax_seq = 1
                      )
                   )
             UNION ALL
            -- secondary audience AllActivePaxAudience records
            -- get all active participants based on promotion list
            /*SELECT pl.promotion_id,  --07/22/2014
                   p.user_id AS participant_id,
                   un.node_id,
                   0 AS audience_id, -- no audience record for this audience type
                   'receiver' AS giver_recvr_type
                   , pl.promotion_type
                   , pl.primary_audience_type
                   , pl.secondary_audience_type
              FROM promo_list pl,
                   participant p,
                   user_node un
             WHERE pl.secondary_audience_type = 'allactivepaxaudience'
                -- cartesian join active participants to promotions
               AND p.status = 'active'
               AND p.user_id = un.user_id
             UNION ALL */
            -- secondary audience SpecifyAudience records
            -- get all secondary pax audience participants based on promotion list
            SELECT pl.promotion_id,
                   pa.user_id AS participant_id,
                   un.node_id,
                   a.audience_id,
                   DECODE( pl.promotion_type,
                      'product_claim',  'receiver',
                      'recognition',    'receiver',
                      'nomination',     'receiver',
                      'quiz',           'receiver',
                      'goalquest',      'pax',
                      'challengepoint', 'pax',
                      'survey', 'pax'
                     ,'self_serv_incentives','pax'            --03/16/2015                      
                   ) AS giver_recvr_type
                   , pl.promotion_type
                   , pl.primary_audience_type
                   , pl.secondary_audience_type
              FROM --promo_list pl,-- 03/02/2018
		           GTT_PROMO_LIST pl, -- 03/02/2018
                   promo_audience pra,
                   audience a,
                   participant_audience pa,
                   user_node un
             WHERE pl.secondary_audience_type = 'specifyaudience'
               AND pl.promotion_id = pra.promotion_id
               AND pra.promo_audience_type = 'SECONDARY'
               AND pra.audience_id = a.audience_id
               --AND a.list_type = 'pax'
               AND a.audience_id = pa.audience_id
               AND pa.user_id = un.user_id
             UNION ALL
            -- secondary audience SameAsPrimaryAudience records
            -- get reciever participants based on primary list giver
            SELECT pri.promotion_id,
                   pri.participant_id,
                   pri.node_id,
                   pri.audience_id,
                   'giver' AS giver_recvr_type
                   , pri.promotion_type
                   , pri.primary_audience_type
                   , pri.secondary_audience_type
              FROM primary_list pri
             WHERE pri.secondary_audience_type = 'sameasprimaryaudience'
               AND pri.giver_recvr_type = 'giver'
             UNION ALL
            -- secondary audience ActivePaxFromPrimaryNodeAudience records
            -- get reciever participants based on primary list giver node
            SELECT DISTINCT
                   pri.promotion_id,
                   p.user_id AS participant_id,
                   un.node_id,
                   0 AS audience_id,
                   'giver' AS giver_recvr_type
                   , pri.promotion_type
                   , pri.primary_audience_type
                   , pri.secondary_audience_type
              FROM primary_list pri,
                   user_node un,
                   participant p
             WHERE pri.secondary_audience_type = 'activepaxfromprimarynodeaudience'
               AND pri.giver_recvr_type = 'giver'
               AND pri.node_id = un.node_id
               AND un.user_id = p.user_id
               AND p.status = 'active'
             UNION ALL
            -- secondary audience ActivePaxFromPrimaryNodeBelowAudience records
            -- get reciever participants based on primary list giver root node
            SELECT DISTINCT
                   prn.promotion_id,
                   p.user_id AS participant_id,
                   un.node_id,
                   0 AS audience_id,
                   'receiver' AS giver_recvr_type
                   , prn.promotion_type
                   , prn.primary_audience_type
                   , prn.secondary_audience_type
              FROM ( -- get nodes and below for all promo root nodes
                     SELECT node_id,
                            sys_connect_by_path(node_id, '/') as node_path
                       FROM node
                      WHERE is_deleted = 0
                    CONNECT BY PRIOR node_id = parent_node_id
                      START WITH node_id IN
                            (SELECT root_node
                               FROM promo_root_node
                            )
                   ) np,
                   participant p,
                   user_node un,
                   promo_root_node prn
             WHERE un.user_id = p.user_id
               and p.status = 'active'
               AND un.node_id = np.node_id
                -- match promo root node to node path root
               AND prn.root_node = NVL(TO_NUMBER(SUBSTR( np.node_path, 2, (INSTR( np.node_path, '/', 2)-2))), np.node_id)
          ) i;
   -- end cur_pax_promo_elig

BEGIN
   v_msg := 'Start'     
      || '<, p_in_bulk_limit >' || p_in_bulk_limit
      || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   -- remove all existing records for specified promotion type
   v_msg := 'TRUNCATE rpt_pax_promo_elig_stage FOR ';
   EXECUTE IMMEDIATE 'TRUNCATE TABLE rpt_pax_promo_elig_stage';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);
   
   DELETE FROM GTT_PROMO_LIST; -- 03/02/2018
   INSERT INTO GTT_PROMO_LIST  -- 03/02/2018
   SELECT p.promotion_id,
					p.promotion_type,
					LOWER(p.primary_audience_type) AS primary_audience_type,
					LOWER(p.secondary_audience_type) AS secondary_audience_type
		 FROM promotion P
	 WHERE p.promotion_type IN ('quiz','diy_quiz')
			AND promotion_status IN ('live','expired')
			AND is_deleted = 0 
      AND (p.primary_audience_type IS NOT NULL OR p.secondary_audience_type IS NOT NULL);

   -- stage all records for specified promotion type
   v_msg := 'OPEN cur_pax_promo_elig';
   OPEN cur_pax_promo_elig;

   v_done := FALSE;
   v_stg_rec_cnt := 0;
   WHILE (NOT v_done) LOOP
      v_msg := 'FETCH cur_pax_promo_elig';
      FETCH cur_pax_promo_elig
       BULK COLLECT
       INTO v_tab_promotion_id,
            v_tab_participant_id,
            v_tab_node_id,
            v_tab_audience_id,
            v_tab_giver_recvr_type
      LIMIT p_in_bulk_limit;

      v_done := cur_pax_promo_elig%NOTFOUND;

      v_rec_cnt := v_tab_promotion_id.COUNT;
      v_stg_rec_cnt := v_stg_rec_cnt + v_rec_cnt;
--      prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ' (' || v_rec_cnt || ' records )', NULL);

      IF (v_rec_cnt > 0) THEN
         v_msg := 'INSERT rpt_pax_promo_elig_stage';
         FORALL indx IN 1 .. v_tab_promotion_id.COUNT
            INSERT /*+ append */ INTO rpt_pax_promo_elig_stage
            (  key_field_hash,
               promotion_id,
               participant_id,
               node_id,
               audience_id,
               giver_recvr_type
            )
            VALUES
            (  dbms_crypto.hash( utl_raw.cast_to_raw(--07/20/2016
                            v_tab_promotion_id(indx)
                  || '/' || v_tab_participant_id(indx)
                  || '/' || v_tab_node_id(indx)
                  || '/' || v_tab_audience_id(indx)
                  || '/' || v_tab_giver_recvr_type(indx)
               ),2),
               v_tab_promotion_id(indx),
               v_tab_participant_id(indx),
               v_tab_node_id(indx),
              v_tab_audience_id(indx),
               v_tab_giver_recvr_type(indx)
            );

         v_rec_cnt := SQL%ROWCOUNT;
--         prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ' (' || v_rec_cnt || ' records )', NULL);
      END IF;

      v_msg := 'Commit staged records';
      COMMIT;
   END LOOP; -- while not done

   CLOSE cur_pax_promo_elig;

   v_msg := 'Insert rpt_pax_promo_elig_stage';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ' (' || v_stg_rec_cnt || ' records)', NULL);

   -- remove obsolete records
   v_msg := 'DELETE rpt_pax_promo_elig_quiz';
   DELETE rpt_pax_promo_elig_quiz
    WHERE ROWID IN
          (  -- report record not matched with stage table
            SELECT ppe.ROWID
              FROM promotion p,
                   rpt_pax_promo_elig_quiz ppe,
                   rpt_pax_promo_elig_stage stg
             WHERE p.promotion_type IN ('quiz','diy_quiz')
               AND p.promotion_id = ppe.promotion_id
               AND ppe.key_field_hash = stg.key_field_hash (+)
               AND stg.ROWID IS NULL
          );
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ' (' || v_rec_cnt || ' records)', NULL);

   -- add missing staged records
   v_msg := 'INSERT rpt_pax_promo_eligibility';
   INSERT INTO rpt_pax_promo_elig_quiz
   (  key_field_hash,
      promotion_id,
      participant_id,
      node_id,      
      giver_recvr_type,
      created_by,
      date_created
    )
    ( -- stage record not found in report table
      SELECT stg.key_field_hash,
             stg.promotion_id,
             stg.participant_id,
             stg.node_id,             
             stg.giver_recvr_type,
             0 AS created_by,
             SYSDATE AS date_created
        FROM rpt_pax_promo_elig_stage stg,
             rpt_pax_promo_elig_quiz ppe
       WHERE stg.key_field_hash = ppe.key_field_hash (+)
         AND ppe.ROWID IS NULL
    );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg || ' (' || v_rec_cnt || ' records)', NULL);

   v_msg := 'Success:';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   COMMIT;
      p_out_return_code := 0;

EXCEPTION
   WHEN OTHERS THEN
    ROLLBACK;
      p_out_return_code   := 99;
         prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
 ROLLBACK;
END p_populate_PaxPromoElig_quiz;

/*******************************************************************************
-- Procedure inserts a receiver based upon the existing giver node and promotion secondary audience
*******************************************************************************/
PROCEDURE p_pop_PaxPromoCritAudReceiver
( p_in_promotion_type IN promotion.promotion_type%TYPE
)  AS
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_pop_PaxPromoCritAudReceiver');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 2.0;

   v_msg                execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;

BEGIN
   v_msg := 'Start: p_in_promotion_type >' || p_in_promotion_type || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

   -- insert all records for specified promotion type
   -- process has no commit, so insert records in one statement
   v_msg := 'INSERT rpt_pax_promo_eligibility FOR p_in_promotion_type >' || p_in_promotion_type || '<';
   INSERT INTO rpt_pax_promo_eligibility
   (  promotion_id,
      participant_id,
      node_id,
      audience_id,
      giver_recvr_type,
      created_by,
      date_created
    )
    ( -- build insert records
      SELECT i.promotion_id,
             i.participant_id,
             i.node_id,
             i.audience_id,
             i.giver_recvr_type,
             0 AS created_by,
             SYSDATE AS date_created
        FROM (
               WITH giver_node_list AS
               (  -- get driver list of promotion giver nodes
                  SELECT DISTINCT
                         r.promotion_id,
                         r.node_id,
                         LOWER(p.secondary_audience_type) AS secondary_audience_type
                    FROM promotion p,
                         rpt_pax_promo_eligibility r
                   WHERE p.promotion_type = p_in_promotion_type
                     AND promotion_status IN ('live','expired')
                     AND is_deleted = 0
                    /*AND (  (   p.promotion_status = 'live')           --21/02/2012
                          OR (   p.promotion_type = 'goalquest'
                             AND p.promotion_status IN ('live','expired')
                             ))*/
                     AND LOWER(p.secondary_audience_type) IN
                         ('activepaxfromprimarynodeaudience', 'activepaxfromprimarynodebelowaudience')
                     AND p.promotion_id = r.promotion_id
                     AND r.giver_recvr_type = 'giver'

               ), promo_root_node AS
               ( -- get root node level(s) for entire promotion based on secondary audience and giver type
               SELECT pnl.promotion_id,
                      pnl.node_id AS root_node
                 FROM (
                        -- get promotions by node level
                        SELECT DISTINCT
                               gnl.promotion_id,
                               gnl.node_id,
                               nl.node_level,
                               MIN(nl.node_level) OVER (PARTITION BY gnl.promotion_id) AS min_node_level
                          from giver_node_list gnl,
                               ( -- get all active nodes by level
                                 SELECT node_id,
                                        level AS node_level
                                   FROM node
                                  WHERE is_deleted = 0
                                CONNECT BY PRIOR node_id = parent_node_id
                                  START WITH parent_node_id IS NULL
                               ) nl
                         WHERE gnl.node_id = nl.node_id
                           AND gnl.secondary_audience_type = 'activepaxfromprimarynodebelowaudience'
                      ) pnl
                WHERE pnl.node_level = min_node_level
               )
               --select * from promo_root_node;
               -- secondary audience ActivePaxFromPrimaryNodeAudience records
               -- get reciever participants based on giver node
               SELECT DISTINCT
                      gnl.promotion_id,
                      p.user_id AS participant_id,
                      un.node_id,
                      0 AS audience_id,
                      'receiver' AS giver_recvr_type
                 FROM giver_node_list gnl,
                      user_node un,
                      participant p
                WHERE gnl.secondary_audience_type = 'activepaxfromprimarynodeaudience'
                  AND gnl.node_id = un.node_id
                  AND un.user_id = p.user_id
                  AND p.status = 'active'
                UNION ALL
               -- secondary audience ActivePaxFromPrimaryNodeBelowAudience records
               -- get reciever participants based on primary list giver root node
               SELECT DISTINCT
                      prn.promotion_id,
                      p.user_id AS participant_id,
                      un.node_id,
                      0 AS audience_id,
                      'receiver' AS giver_recvr_type
                 FROM ( -- get nodes and below for all promo root nodes
                        SELECT node_id,
                               sys_connect_by_path(node_id, '/') as node_path
                          FROM node
                         WHERE is_deleted = 0
                       CONNECT BY PRIOR node_id = parent_node_id
                         START WITH node_id IN
                               (SELECT root_node
                                  FROM promo_root_node
                               )
                      ) np,
                      participant p,
                      user_node un,
                      promo_root_node prn
                WHERE un.user_id = p.user_id
                  and p.status = 'active'
                  AND un.node_id = np.node_id
                   -- match promo root node to node path root
                  AND prn.root_node = NVL(TO_NUMBER(SUBSTR( np.node_path, 2, (INSTR( np.node_path, '/', 2)-2))), np.node_id)
             ) i
   );
   -- end INSERT INTO rpt_pax_promo_eligibility

   v_rec_cnt := SQL%ROWCOUNT;
   v_msg := 'Success: p_in_promotion_type >' || p_in_promotion_type || '<, Rows inserted >' || v_rec_cnt || '<';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
END p_pop_PaxPromoCritAudReceiver;
-----------------

 PROCEDURE p_rpt_pax_elig_allaud_node  (p_in_requested_user_id      IN  NUMBER,
                                      p_out_return_code           OUT NUMBER,
                                      p_out_error_message         OUT VARCHAR2)
IS
/*******************************************************************************
--
-- Purpose: Populate rpt_pax_promo_elig_allaud_node table.
--
-- MODIFICATION HISTORY
-- Person                    Date                              Comments
-- ---------                ----------             ------------------------------------------
-- Ravi Dhanekula 07/21/2014         Initial Version
--Ravi Dhanekula  04/23/2015         Bugs # 61322,61090 Recognition Given - Participation by Organization report displaying wrong eligible count
--Suresh J        05/22/2015        Bug #61322,62303,62383 - Recognition Given - Participation by Organization shows wrong elig count 
--nagarajs        04/21/2017        rpt_recognition_summary removed as part of recognition performance changes
*******************************************************************************/
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_pax_elig_allaud_node');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  -- c_created_by         CONSTANT rpt_recognition_summary.created_by%TYPE:= p_in_requested_user_id; --04/21/2017
  c_created_by         CONSTANT rpt_pax_promo_elig_allaud_node.created_by%TYPE:= p_in_requested_user_id; --04/21/2017
   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
BEGIN

  v_stage := 'Start';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

   -- remove obsolete node summaries (node not found in rpt_hierarchy)
   v_stage := 'DELETE obsolete node records';
   DELETE
     FROM rpt_pax_promo_elig_allaud_node s
    WHERE s.node_id NOT IN
          ( -- get node ID currently in the report hierarchy
            SELECT h.node_id
              FROM rpt_hierarchy h
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

--   -- remove inactive pax (inactive pax have elig_count = 0) --04/23/2015
--   v_stage := 'DELETE inactive pax records';
--   DELETE
--     FROM rpt_pax_promo_elig_allaud_node s
--    WHERE EXISTS (SELECT 'x'
--                  FROM   rpt_participant_employer rad, participant pax, user_node un
--                  WHERE  pax.user_id = un.user_id
--                  AND    pax.user_id = rad.user_id(+)
--                  AND    un.node_id = s.node_id
--                  AND    pax.status = 'inactive'
--                  AND    NVL(rad.position_type,' ') = s.position_type
--                  AND    NVL(rad.department_type,' ') = s.department_type);

    v_stage := 'DELETE invalid node,job and dept combinations';    --05/22/2015
    DELETE   --05/22/2015
    from rpt_pax_promo_elig_allaud_node allaud
    where not exists (
    select un.node_id, rad.position_type, rad.department_type
    from rpt_participant_employer rad,
         user_node un
    where rad.user_id = un.user_id 
          and un.node_id = allaud.node_id
          and nvl(rad.position_type,'x')  = nvl(allaud.position_type,'x')
          and nvl(rad.department_type,'x') = nvl(allaud.department_type,'x'));  

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   ------------
 v_stage := 'MERGE rpt_pax_promo_elig_allaud_node';

MERGE INTO rpt_pax_promo_elig_allaud_node d
USING (
SELECT SUM(elig_count) elig_count, npn.path_node_id node_id, elg.giver_recvr_type ,
    elg.position_type, elg.department_type, elg.country_id
  FROM   
  ( 
  SELECT un.node_id,c.VALUE giver_recvr_type,
      NVL(rad.position_type,' ') position_type, NVL(rad.department_type,' ') department_type, 
      addr.country_id,
      COUNT( pax.user_id) elig_count
      FROM rpt_participant_employer rad, participant pax,user_node un,
      (SELECT user_id, country_id FROM user_address WHERE is_primary = 1) addr,
      (SELECT VALUE
            FROM ((SELECT 'giver' v1, 'receiver' v2 FROM DUAL) 
            UNPIVOT (VALUE FOR value_type IN  (v1,v2) ))) c
      WHERE pax.user_id = rad.user_id(+)
      AND un.user_id = pax.user_id
--      AND pax.status = 'active' --04/23/2015
      AND pax.user_id = addr.user_id
      GROUP BY un.node_id, c.VALUE, rad.position_type, rad.department_type, addr.country_id,c.VALUE) elg, 
  (SELECT child_node_id node_id,node_id path_node_id 
  FROM rpt_hierarchy_rollup 
  --  WHERE NODE_ID IN (SELECT node_id FROM rpt_hierarchy) --04/03/2015
  ) npn 
  WHERE elg.node_id = npn.node_id
  GROUP BY npn.path_node_id, elg.giver_recvr_type, elg.position_type, elg.department_type, elg.country_id
    ) s    
       ON (s.node_id = d.node_id AND 
           s.giver_recvr_type = d.giver_recvr_type AND
           s.position_type = d.position_type AND
           s.department_type = d.department_type AND
           s.country_id = d.country_id)   
        WHEN MATCHED THEN UPDATE 
         SET d.elig_count = s.elig_count,
              modified_by = c_created_by,
              date_modified = SYSDATE
        WHERE NOT (   DECODE(d.elig_count,  s.elig_count,                    1, 0) = 1              
                 )            
    WHEN NOT MATCHED THEN
      INSERT
      ( node_id,
        giver_recvr_type,
        position_type,
        department_type,
        country_id,
        elig_count,
        date_created,
        created_by
      )
      VALUES
      ( s.node_id,
        s.giver_recvr_type,
        s.position_type,
        s.department_type,
        s.country_id,
        s.elig_count,
        SYSDATE,
        c_created_by
      );
      
      MERGE INTO rpt_pax_promo_elig_allaud_node d --04/23/2015 Added this negate the inactive pax related rows.
USING (
SELECT SUM(elig_count) elig_count, npn.path_node_id node_id, elg.giver_recvr_type ,
    elg.position_type, elg.department_type, elg.country_id
  FROM   
  ( 
  SELECT un.node_id,c.VALUE giver_recvr_type,
      NVL(rad.position_type,' ') position_type, NVL(rad.department_type,' ') department_type, 
      addr.country_id,
      COUNT( pax.user_id) elig_count
      FROM rpt_participant_employer rad, participant pax,user_node un,
      (SELECT user_id, country_id FROM user_address WHERE is_primary = 1) addr,
      (SELECT VALUE
            FROM ((SELECT 'giver' v1, 'receiver' v2 FROM DUAL) 
            UNPIVOT (VALUE FOR value_type IN  (v1,v2) ))) c
      WHERE pax.user_id = rad.user_id(+)
      AND un.user_id = pax.user_id
      AND pax.status = 'inactive'
      AND pax.user_id = addr.user_id
      GROUP BY un.node_id, c.VALUE, rad.position_type, rad.department_type, addr.country_id,c.VALUE) elg, 
  (SELECT child_node_id node_id,node_id path_node_id 
  FROM rpt_hierarchy_rollup 
  --  WHERE NODE_ID IN (SELECT node_id FROM rpt_hierarchy) --04/03/2015
  ) npn 
  WHERE elg.node_id = npn.node_id
  GROUP BY npn.path_node_id, elg.giver_recvr_type, elg.position_type, elg.department_type, elg.country_id
    ) s    
       ON (s.node_id = d.node_id AND 
           s.giver_recvr_type = d.giver_recvr_type AND
           s.position_type = d.position_type AND
           s.department_type = d.department_type AND
           s.country_id = d.country_id)   
        WHEN MATCHED THEN UPDATE 
         SET d.elig_count = d.elig_count - s.elig_count,
              modified_by = c_created_by,
              date_modified = SYSDATE;      

END;
-----------------

   PROCEDURE p_rpt_pax_elig_allaud_team  (p_in_requested_user_id      IN  NUMBER,
                                      p_out_return_code           OUT NUMBER,
                                      p_out_error_message         OUT VARCHAR2)
IS
/*******************************************************************************
--
-- Purpose: Populate rpt_pax_promo_elig_allaud_team table.
--
-- MODIFICATION HISTORY
-- Person                    Date                              Comments
-- ---------                ----------             ------------------------------------------
-- Ravi Dhanekula 07/21/2014         Initial Version
--Suresh J        05/22/2015        Bug #61322,62303,62383 - Recognition Given - Participation by Organization shows wrong elig count
--nagarajs        04/21/2017        rpt_recognition_summary removed as part of recognition performance changes
*******************************************************************************/
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_pax_elig_allaud_team');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
   --c_created_by         CONSTANT rpt_recognition_summary.created_by%TYPE:= p_in_requested_user_id; --04/21/2017
   c_created_by         CONSTANT rpt_pax_promo_elig_allaud_node.created_by%TYPE:= p_in_requested_user_id; --04/21/2017
   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
BEGIN

  v_stage := 'Start';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

   -- remove obsolete node summaries (node not found in rpt_hierarchy)
   v_stage := 'DELETE obsolete node records';
   DELETE
     FROM RPT_PAX_PROMO_ELIG_ALLAUD_TEAM s
    WHERE s.node_id NOT IN
          ( -- get node ID currently in the report hierarchy
            SELECT h.node_id
              FROM rpt_hierarchy h
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- remove inactive pax (inactive pax have elig_count = 0)
   v_stage := 'DELETE inactive pax records';
   DELETE
     FROM rpt_pax_promo_elig_allaud_team s
    WHERE EXISTS (SELECT 'x'
                  FROM   rpt_participant_employer rad, participant pax, user_node un
                  WHERE  pax.user_id = un.user_id
                  AND    pax.user_id = rad.user_id(+)
                  AND    un.node_id = s.node_id
                  AND    pax.status = 'inactive'
                  AND    NVL(rad.position_type,' ') = s.position_type
                  AND    NVL(rad.department_type,' ') = s.department_type);

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

    v_stage := 'DELETE invalid node,job and dept combinations';    --05/22/2015
    DELETE   --05/22/2015
    from rpt_pax_promo_elig_allaud_team allaud
    where not exists (
    select un.node_id, rad.position_type, rad.department_type
    from rpt_participant_employer rad,
         user_node un
    where rad.user_id = un.user_id 
          and un.node_id = allaud.node_id
          and nvl(rad.position_type,'x')  = nvl(allaud.position_type,'x')
          and nvl(rad.department_type,'x') = nvl(allaud.department_type,'x'));  

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   ------------
 v_stage := 'MERGE RPT_PAX_PROMO_ELIG_ALLAUD_TEAM';

MERGE INTO RPT_PAX_PROMO_ELIG_ALLAUD_TEAM d
USING (
SELECT un.node_id, c.VALUE giver_recvr_type,
NVL(rad.position_type,' ') position_type, NVL(rad.department_type,' ') department_type, addr.country_id, 
COUNT( pax.user_id) elig_count
FROM rpt_participant_employer rad, participant pax,user_node un,
(SELECT user_id, country_id FROM user_address WHERE is_primary = 1) addr,
(SELECT VALUE
        FROM ((SELECT 'giver' v1, 'receiver' v2 FROM DUAL) 
        UNPIVOT (VALUE FOR value_type IN  (v1, v2) ))) c
WHERE pax.user_id = rad.user_id(+)
AND un.user_id = pax.user_id
AND pax.status = 'active'
AND pax.user_id = addr.user_id
GROUP BY un.node_id, c.VALUE, rad.position_type, rad.department_type, addr.country_id) s    
       ON (s.node_id = d.node_id AND 
           s.giver_recvr_type = d.giver_recvr_type AND
           s.position_type = d.position_type AND
           s.department_type = d.department_type AND
           s.country_id = d.country_id)
        WHEN MATCHED THEN UPDATE 
         SET d.elig_count = s.elig_count,
              modified_by = c_created_by,
              date_modified = SYSDATE
        WHERE NOT (   DECODE(d.elig_count,  s.elig_count,                    1, 0) = 1              
                 )            
    WHEN NOT MATCHED THEN
      INSERT
      ( node_id,
        giver_recvr_type,
        position_type,
        department_type,
        country_id, 
        elig_count,
        date_created,
        created_by
      )
      VALUES
      ( s.node_id,
        s.giver_recvr_type,
        s.position_type,
        s.department_type,
        s.country_id,
        s.elig_count,
        SYSDATE,
        c_created_by
      );

END;
-----------------

    PROCEDURE p_rpt_pax_elig_speaud_team  (p_in_requested_user_id      IN  NUMBER,
                                      p_out_return_code           OUT NUMBER,
                                      p_out_error_message         OUT VARCHAR2)
IS
/*******************************************************************************
--
-- Purpose: Populate rpt_pax_promo_elig_speaud_team table.
--
-- MODIFICATION HISTORY
-- Person                    Date                              Comments
-- ---------                ----------             ------------------------------------------
-- Ravi Dhanekula 07/21/2014         Initial Version
-- murphyc        12/01/2014         bug 58402
--nagarajs        04/21/2017        rpt_recognition_summary removed as part of recognition performance changes
*******************************************************************************/
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_pax_elig_speaud_team');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
   --c_created_by         CONSTANT rpt_recognition_summary.created_by%TYPE:= p_in_requested_user_id; --04/21/2017
   c_created_by         CONSTANT rpt_pax_promo_elig_allaud_node.created_by%TYPE:= p_in_requested_user_id; --04/21/2017
   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
BEGIN

  v_stage := 'Start';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

/*
   -- remove obsolete node summaries (node not found in rpt_hierarchy)
   v_stage := 'DELETE obsolete node records';
   DELETE
     FROM rpt_pax_promo_elig_speaud_team s
    WHERE s.node_id NOT IN
          ( -- get node ID currently in the report hierarchy
            SELECT h.node_id
              FROM rpt_hierarchy h
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- remove expired promotions (expired promos have elig_count = 0)
   v_stage := 'DELETE expired promotion records';
   DELETE
     FROM rpt_pax_promo_elig_speaud_team s
    WHERE s.promotion_id IN
          ( SELECT promotion_id
              FROM promotion
             WHERE promotion_status = 'expired'
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- remove inactive pax (inactive pax have elig_count = 0)
   v_stage := 'DELETE inactive pax records';
   DELETE
     FROM rpt_pax_promo_elig_speaud_team s
    WHERE EXISTS (SELECT 'x'
                  FROM   user_node pe, participant emp, rpt_participant_employer rad
                  WHERE  pe.user_id = emp.user_id
                  AND    pe.user_id = rad.user_id(+)
                  AND    pe.node_id = s.node_id
                  AND    emp.status = 'inactive'
                  AND    NVL(rad.position_type,' ') = s.position_type
                  AND    NVL(rad.department_type,' ') = s.department_type);

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
*/
   v_stage := 'DELETE existing team records';
   DELETE rpt_pax_promo_elig_speaud_team s; -- 12/01/2014
   
   ------------
 v_stage := 'MERGE rpt_pax_promo_elig_speaud_team';
MERGE INTO rpt_pax_promo_elig_speaud_team d
USING (
SELECT pe.node_id, pe.promotion_id, emp.status pax_status , 
      pr.promotion_status, pr.promotion_type, 
      NVL(rad.position_type,' ') position_type, NVL(rad.department_type,' ') department_type,country_id,giver_recvr_type,
--    COUNT(1) elig_count   --05/22/2015
      COUNT(DISTINCT pe.participant_id) elig_count  --05/22/2015    
      FROM rpt_pax_promo_eligibility pe, participant emp, promotion pr, rpt_participant_employer rad,
        (SELECT user_id, country_id FROM user_address WHERE is_primary = 1) addr
      WHERE pe.participant_id = emp.user_id 
      AND emp.status = 'active'
      AND pe.promotion_id = pr.promotion_id
      AND pr.promotion_status = 'live'
      AND pe.participant_id = rad.user_id(+)
      AND emp.user_id = addr.user_id
      GROUP BY pe.promotion_id, pe.node_id, emp.status,addr.country_id, pr.promotion_status, pr.promotion_type, 
      rad.position_type, rad.department_type,giver_recvr_type 
      UNION ALL -- promo_id -1 = ALL (for promotion types other than Award report)      
      SELECT pe.node_id, -1 promotion_id, emp.status pax_status , 
      pr.promotion_status, pr.promotion_type, 
      NVL(rad.position_type,' ') position_type, NVL(rad.department_type,' ') department_type,country_id,giver_recvr_type,
      COUNT(DISTINCT pe.participant_id) elig_count
      FROM rpt_pax_promo_eligibility pe, participant emp, promotion pr, rpt_participant_employer rad,
       (SELECT user_id, country_id FROM user_address WHERE is_primary = 1) addr
      WHERE pe.participant_id = emp.user_id 
      AND emp.status = 'active'
      AND pe.promotion_id = pr.promotion_id
      AND pr.promotion_status = 'live'
      AND pe.participant_id = rad.user_id(+)
      AND emp.user_id = addr.user_id
      GROUP BY pe.node_id, addr.country_id,emp.status, pr.promotion_status, pr.promotion_type, 
      rad.position_type, rad.department_type,giver_recvr_type
      UNION ALL  -- promo_id -2 = ALL (for promotion type--> Award report)      
      SELECT pe.node_id, -2 promotion_id, emp.status pax_status , 
      pr.promotion_status,'award' promotion_type,    
      NVL(rad.position_type,' ') position_type, NVL(rad.department_type,' ') department_type,country_id,giver_recvr_type,
      COUNT(DISTINCT pe.participant_id) elig_count
      FROM rpt_pax_promo_eligibility pe, participant emp, promotion pr, rpt_participant_employer rad,
       (SELECT user_id, country_id FROM user_address WHERE is_primary = 1) addr
      WHERE pe.participant_id = emp.user_id 
      AND emp.status = 'active'
      AND pe.promotion_id = pr.promotion_id
      AND pr.promotion_status = 'live'
      AND pe.participant_id = rad.user_id(+)
      AND emp.user_id = addr.user_id
      GROUP BY pe.node_id, addr.country_id,emp.status, pr.promotion_status,
      rad.position_type, rad.department_type,giver_recvr_type
       ) s    
       ON (s.node_id = d.node_id AND 
           s.promotion_id = d.promotion_id AND
           s.pax_status = d.pax_status AND 
           s.promotion_status = d.promotion_status AND
           s.promotion_type = d.promotion_type AND
           s.giver_recvr_type = d.giver_recvr_type AND
           s.position_type = d.position_type AND
           s.department_type = d.department_type AND
           s.country_id = d.country_id)   
        WHEN MATCHED THEN UPDATE 
         SET d.elig_count = s.elig_count,
              modified_by = c_created_by,
              date_modified = SYSDATE
        WHERE NOT (   DECODE(d.elig_count,  s.elig_count,                    1, 0) = 1              
                 )            
    WHEN NOT MATCHED THEN
      INSERT
      ( node_id,
        giver_recvr_type,
        promotion_id,
        pax_status , 
        promotion_status,
        promotion_type,
        position_type,
        department_type,
        country_id,
        elig_count,
        date_created,
        created_by
      )
      VALUES
      ( s.node_id,
        s.giver_recvr_type,
        s.promotion_id,
        s.pax_status , 
        s.promotion_status,
        s.promotion_type,
        s.position_type,
        s.department_type,
        s.country_id, 
        s.elig_count,
        SYSDATE,
        c_created_by
      );

END;
-----------------

   PROCEDURE p_rpt_pax_elig_speaud_node  (p_in_requested_user_id      IN  NUMBER,
                                      p_out_return_code           OUT NUMBER,
                                      p_out_error_message         OUT VARCHAR2)
IS
/*******************************************************************************
--
-- Purpose: Populate rpt_pax_promo_elig_speaud_node table.
--
-- MODIFICATION HISTORY
-- Person                    Date                              Comments
-- ---------                ----------             ------------------------------------------
-- Ravi Dhanekula 07/21/2014         Initial Version
-- murphyc        12/01/2014         bug 58402
--nagarajs        04/21/2017        rpt_recognition_summary removed as part of recognition performance changes
*******************************************************************************/
c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('p_rpt_pax_elig_speaud_node');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
   --c_created_by         CONSTANT rpt_recognition_summary.created_by%TYPE:= p_in_requested_user_id; --04/21/2017
   c_created_by         CONSTANT rpt_pax_promo_elig_allaud_node.created_by%TYPE:= p_in_requested_user_id; --04/21/2017
   v_stage              execution_log.text_line%TYPE;
   v_rec_cnt            INTEGER;
BEGIN

  v_stage := 'Start';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
/*
   -- remove obsolete node summaries (node not found in rpt_hierarchy)
   v_stage := 'DELETE obsolete node records';
   DELETE
     FROM rpt_pax_promo_elig_speaud_node s
    WHERE s.node_id NOT IN
          ( -- get node ID currently in the report hierarchy
            SELECT h.node_id
              FROM rpt_hierarchy h
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- remove expired promotions (expired promos have elig_count = 0)
   v_stage := 'DELETE expired promotion records';
   DELETE
     FROM rpt_pax_promo_elig_speaud_node s
    WHERE s.promotion_id IN
          ( SELECT promotion_id
              FROM promotion
             WHERE promotion_status = 'expired'
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

   -- remove inactive pax (inactive pax have elig_count = 0)
   v_stage := 'DELETE inactive pax records';
   DELETE
     FROM rpt_pax_promo_elig_speaud_node s
    WHERE EXISTS (SELECT 'x'
                  FROM   user_node pe, participant emp, rpt_participant_employer rad
                  WHERE  pe.user_id = emp.user_id
                  AND    pe.user_id = rad.user_id(+)
                  AND    pe.node_id = s.node_id
                  AND    emp.status = 'inactive'
                  AND    NVL(rad.position_type,' ') = s.position_type
                  AND    NVL(rad.department_type,' ') = s.department_type);

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
*/

   v_stage := 'DELETE existing node records';
   DELETE rpt_pax_promo_elig_speaud_node; -- 12/01/2014
   ------------
 v_stage := 'MERGE rpt_pax_promo_elig_speaud_node';
MERGE INTO rpt_pax_promo_elig_speaud_node d
USING (
SELECT SUM(elig_count) elig_count,promotion_id,elg.pax_status,elg.promotion_status,elg.promotion_type,npn.path_node_id node_id, elg.giver_recvr_type ,
          elg.position_type, elg.department_type, elg.country_id
        FROM   
        ( SELECT pe.node_id, pe.promotion_id, emp.status pax_status , 
      pr.promotion_status, pr.promotion_type, 
      NVL(rad.position_type,' ') position_type, NVL(rad.department_type,' ') department_type,country_id,giver_recvr_type,
--    COUNT(1) elig_count   --05/22/2015
      COUNT(DISTINCT pe.participant_id) elig_count  --05/22/2015    
      FROM rpt_pax_promo_eligibility pe, participant emp, promotion pr, rpt_participant_employer rad,
        (SELECT user_id, country_id FROM user_address WHERE is_primary = 1) addr
      WHERE pe.participant_id = emp.user_id 
      AND emp.status = 'active'
      AND pe.promotion_id = pr.promotion_id
      AND pr.promotion_status = 'live'
      AND pe.participant_id = rad.user_id(+)
      AND emp.user_id = addr.user_id
      GROUP BY pe.promotion_id, pe.node_id, emp.status,addr.country_id, pr.promotion_status, pr.promotion_type, 
      rad.position_type, rad.department_type,giver_recvr_type
      UNION ALL -- promo_id -1 = ALL
      SELECT pe.node_id, -1 promotion_id, emp.status pax_status , 
      pr.promotion_status, pr.promotion_type, 
      NVL(rad.position_type,' ') position_type, NVL(rad.department_type,' ') department_type,country_id,giver_recvr_type,
      COUNT(DISTINCT pe.participant_id) elig_count
      FROM rpt_pax_promo_eligibility pe, participant emp, promotion pr, rpt_participant_employer rad,
       (SELECT user_id, country_id FROM user_address WHERE is_primary = 1) addr
      WHERE pe.participant_id = emp.user_id 
      AND emp.status = 'active'
      AND pe.promotion_id = pr.promotion_id
      AND pr.promotion_status = 'live'
      AND pe.participant_id = rad.user_id(+)
      AND emp.user_id = addr.user_id
      GROUP BY pe.node_id, addr.country_id,emp.status, pr.promotion_status, pr.promotion_type, rad.position_type, rad.department_type,giver_recvr_type
      UNION ALL  -- promo_id -2 = ALL (for promotion type--> Award report)      
      SELECT pe.node_id, -2 promotion_id, emp.status pax_status , 
      pr.promotion_status,'award' promotion_type,    
      NVL(rad.position_type,' ') position_type, NVL(rad.department_type,' ') department_type,country_id,giver_recvr_type,
      COUNT(DISTINCT pe.participant_id) elig_count
      FROM rpt_pax_promo_eligibility pe, participant emp, promotion pr, rpt_participant_employer rad,
       (SELECT user_id, country_id FROM user_address WHERE is_primary = 1) addr
      WHERE pe.participant_id = emp.user_id 
      AND emp.status = 'active'
      AND pe.promotion_id = pr.promotion_id
      AND pr.promotion_status = 'live'
      AND pe.participant_id = rad.user_id(+)
      AND emp.user_id = addr.user_id
      GROUP BY pe.node_id, addr.country_id,emp.status, pr.promotion_status,
      rad.position_type, rad.department_type,giver_recvr_type) elg, 
        (SELECT child_node_id node_id,node_id path_node_id 
        FROM rpt_hierarchy_rollup 
        --  WHERE NODE_ID IN (SELECT node_id FROM rpt_hierarchy) --04/03/2015
        ) npn 
        WHERE elg.node_id = npn.node_id
        GROUP BY npn.path_node_id, elg.giver_recvr_type,promotion_id,elg.pax_status,elg.promotion_status,
        elg.promotion_type,elg.position_type, elg.department_type, elg.country_id
       ) s    
       ON (s.node_id = d.node_id AND 
           s.promotion_id = d.promotion_id AND
           s.pax_status = d.pax_status AND 
           s.promotion_status = d.promotion_status AND
           s.promotion_type = d.promotion_type AND
           s.giver_recvr_type = d.giver_recvr_type AND
           s.position_type = d.position_type AND
           s.department_type = d.department_type AND
           s.country_id = d.country_id)   
        WHEN MATCHED THEN UPDATE 
         SET d.elig_count = s.elig_count,
              modified_by = c_created_by,
              date_modified = SYSDATE
        WHERE NOT (   DECODE(d.elig_count,  s.elig_count,                    1, 0) = 1              
                 )            
    WHEN NOT MATCHED THEN
      INSERT
      ( node_id,
        giver_recvr_type,
        promotion_id,
        pax_status , 
        promotion_status,
        promotion_type,
        position_type,
        department_type,
        country_id, 
        elig_count,
        date_created,
        created_by
      )
      VALUES
      ( s.node_id,
        s.giver_recvr_type,
        s.promotion_id,
        s.pax_status , 
        s.promotion_status,
        s.promotion_type,
        s.position_type,
        s.department_type,
        s.country_id, 
        s.elig_count,
        SYSDATE,
        c_created_by
      );

END;
-----------------
END pkg_populate_promo_elig;
/
