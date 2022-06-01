CREATE OR REPLACE PACKAGE pkg_report_behavior
  IS
/*******************************************************************************
--
-- Purpose: Proocedures to populate behavior report tables
--
-- MODIFICATION HISTORY
-- Person      Date          Comments
-- ---------   ------        ------------------------------------------
-- nagarajs    04/26/2016   Initial  Creation
*******************************************************************************/
 PROCEDURE prc_behavior_detail
 (p_in_requested_user_id    IN  NUMBER,
  p_in_start_date           IN  DATE,
  p_in_end_date             IN  DATE,
  p_out_return_code         OUT NUMBER,
  p_out_error_message       OUT VARCHAR2);

 PROCEDURE prc_behavior_summary
 (p_in_requested_user_id    IN  NUMBER,
  p_out_return_code         OUT NUMBER,
  p_out_error_message       OUT VARCHAR2);
--  
END; -- Package spec
/
CREATE OR REPLACE PACKAGE BODY pkg_report_behavior
IS
/*******************************************************************************
--
-- Purpose: Proocedures to populate behavior report tables
--
-- MODIFICATION HISTORY
-- Person      Date          Comments
-- ---------   ------        ------------------------------------------
-- nagarajs    04/26/2016   Initial  Creation
*******************************************************************************/


 PROCEDURE prc_behavior_detail
 (p_in_requested_user_id    IN  NUMBER,
  p_in_start_date           IN  DATE,
  p_in_end_date             IN  DATE,
  p_out_return_code         OUT NUMBER,
  p_out_error_message       OUT VARCHAR2) IS
/*******************************************************************************
--
-- Purpose: Proocedures to populate behavior detail report
--
-- MODIFICATION HISTORY
-- Person      Date          Comments
-- ---------   ------        ------------------------------------------
-- nagarajs    04/26/2016   Initial  Creation
-- nagarajs    06/08/2016   Bug 67032 - Behavior report is getting data without approve the nomination for behaviors
-- nagarajs    01/11/2017   G5.6.3.3 report changes
*******************************************************************************/
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'PRC_BEHAVIOR_DETAIL';
  c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by         CONSTANT rpt_behavior_detail.created_by%TYPE:= p_in_requested_user_id;

  v_stage              execution_log.text_line%TYPE;
  v_rec_cnt            INTEGER;
  v_rpt_cnt            INTEGER := 0;
  v_tab_node_id        dbms_sql.number_table;
  v_tab_node_name      dbms_sql.varchar2_table;
  
  --Cursor to pick modified node name
  CURSOR cur_node_changed IS  
    SELECT node_id,
           name
      FROM node
     WHERE date_modified BETWEEN p_in_start_date AND p_in_end_date;  
BEGIN

  v_stage := 'INSERT rpt_behavior_detail';   
  INSERT INTO rpt_behavior_detail
         (rpt_behavior_detail_id,
          giver_user_id,
          giver_login_id,
          giver_node_id,
          giver_node_name,
          giver_last_name,
          giver_first_name,
          giver_middle_name,
          giver_pax_status,
          giver_country_id,
          giver_department,
          giver_job_position,
          recvr_user_id,
          recvr_login_id, 
          recvr_node_id,
          recvr_node_name,
          recvr_last_name,
          recvr_first_name,
          recvr_middle_name,
          recvr_pax_status,
          recvr_country_id,
          recvr_department,
          recvr_job_position, 
          claim_id,
          promotion_id,
          promotion_name,
          behavior,
          date_submitted,
          date_approved,
          proxy_first_name, --01/11/2017
          proxy_last_name, --01/11/2017
          created_by,
          date_created,
          version)  
    SELECT rpt_behavior_detail_id_pk_sq.NEXTVAL,
           giver_user_id,
           giver_login_id,
           giver_node_id,
           giver_node_name,
           giver_last_name,
           giver_first_name,
           giver_middle_name,
           giver_pax_status,
           giver_country_id,
           giver_department,
           giver_job_position,
           recvr_user_id,
           recvr_login_id,
           recvr_node_id,
           recvr_node_name,
           recvr_last_name,
           recvr_first_name,
           recvr_middle_name,
           recvr_pax_status,
           recvr_country_id,
           recvr_department,
           recvr_job_position,
           claim_id,
           promotion_id,
           promotion_name,
           behavior,
           date_submitted,
           date_approved,
           proxy_first_name, --01/11/2017
           proxy_last_name, --01/11/2017
           c_created_by,
           SYSDATE,
           0
      FROM (SELECT c.submitter_id               AS giver_user_id,
                   c.node_id                    AS giver_node_id,
                   ua_gu.country_id             AS giver_country_id,
                   n_g.name                     AS giver_node_name,
                   INITCAP (au_gu.last_name)    AS giver_last_name,
                   INITCAP (au_gu.first_name)   AS giver_first_name,
                   TRIM (au_gu.middle_name)     AS giver_middle_name,
                   DECODE (au_gu.is_active, 1, 'active', 'inactive') AS giver_pax_status,
                   cpe_g.department_type        AS giver_department,
                   cpe_g.position_type          AS giver_job_position,
                   au_gu.user_name              AS giver_login_id,
                   cr.participant_id            AS recvr_user_id,
                   cr.node_id                   AS recvr_node_id,
                   ua_ru.country_id             AS recvr_country_id,
                   n_r.name                     AS recvr_node_name,
                   INITCAP (au_ru.last_name)    AS recvr_last_name,
                   INITCAP (au_ru.first_name)   AS recvr_first_name,
                   TRIM (au_ru.middle_name)     AS recvr_middle_name,
                   DECODE (au_ru.is_active, 1, 'active', 'inactive') AS recvr_pax_status,
                   cpe_r.department_type        AS recvr_department,
                   cpe_r.position_type          AS recvr_job_position,                 
                   au_ru.user_name              AS recvr_login_id,
                   c.claim_id,
                   c.promotion_id,
                   p.promotion_name,
                   rc.behavior,
                   c.submission_date AS date_submitted,
                   ci_r.date_approved AS date_approved,
                   au_pu.last_name AS proxy_last_name, --01/11/2017
                   au_pu.first_name AS proxy_first_name --01/11/2017
              FROM claim c,
                   promotion p,
                   recognition_claim rc,
                   claim_item ci_r,
                   claim_recipient cr,
                   node n_g,                   -- giver node
                   node n_r,                   -- receiver node
                   application_user au_gu,     -- giver user
                   user_address ua_gu,         -- giver address
                   application_user au_ru,     -- receiver user
                   user_address ua_ru,         -- receiver address
                   vw_curr_pax_employer cpe_g, -- giver  employer
                   vw_curr_pax_employer cpe_r,  -- receiver employer
                   application_user au_pu        --proxy user --01/11/2017
             WHERE c.is_open = 0 --06/08/2016
               AND c.promotion_id = p.promotion_id
               AND p.promotion_status IN ('live', 'expired')
               AND p.is_deleted = 0
               AND c.claim_id = rc.claim_id
               AND rc.behavior IS NOT NULL
               AND rc.behavior <> 'none'
               AND c.claim_id = ci_r.claim_id
               AND ci_r.claim_item_id = cr.claim_item_id
               AND c.node_id = n_g.node_id(+)
               AND cr.node_id = n_r.node_id(+)
               AND c.submitter_id = au_gu.user_id(+)
               AND au_gu.user_id = ua_gu.user_id (+)
               AND ua_gu.is_primary (+)= 1                                   
               AND c.submitter_id = cpe_g.user_id(+)
               AND cr.participant_id = au_ru.user_id(+)
               AND au_ru.user_id = ua_ru.user_id(+)
               AND ua_ru.is_primary (+)= 1                                   
               AND cr.participant_id = cpe_r.user_id(+)
               AND c.proxy_user_id = au_pu.useR_id (+) --01/11/2017
               AND (c.date_created > p_in_start_date AND c.date_created <= p_in_end_date)
             UNION ALL
            SELECT c.submitter_id               AS giver_user_id,
                   c.node_id                    AS giver_node_id,
                   ua_gu.country_id             AS giver_country_id,
                   n_g.name                     AS giver_node_name,
                   INITCAP (au_gu.last_name)    AS giver_last_name,
                   INITCAP (au_gu.first_name)   AS giver_first_name,
                   TRIM (au_gu.middle_name)     AS giver_middle_name,
                   DECODE (au_gu.is_active, 1, 'active', 'inactive') AS giver_pax_status,
                   cpe_g.department_type        AS giver_department,
                   cpe_g.position_type          AS giver_job_position,
                   au_gu.user_name              AS giver_login_id,
                   cr.participant_id            AS recvr_user_id,
                   cr.node_id                   AS recvr_node_id,
                   ua_ru.country_id             AS recvr_country_id,
                   n_r.name                     AS recvr_node_name,
                   INITCAP (au_ru.last_name)    AS recvr_last_name,
                   INITCAP (au_ru.first_name)   AS recvr_first_name,
                   TRIM (au_ru.middle_name)     AS recvr_middle_name,
                   DECODE (au_ru.is_active, 1, 'active', 'inactive') AS recvr_pax_status,
                   cpe_r.department_type        AS recvr_department,
                   cpe_r.position_type          AS recvr_job_position,                 
                   au_ru.user_name              AS recvr_login_id,
                   c.claim_id,
                   c.promotion_id,
                   p.promotion_name,
                   ncb.behavior,
                   c.submission_date AS date_submitted,
                   ci_r.date_approved AS date_approved,
                   au_pu.last_name AS proxy_last_name, --01/11/2017
                   au_pu.first_name AS proxy_first_name --01/11/2017
              FROM claim c,
                   promotion p,
                   nomination_claim_behaviors ncb,
                   claim_item ci_r,
                   claim_recipient cr,
                   node n_g,                   -- giver node
                   node n_r,                   -- receiver node
                   application_user au_gu,     -- giver user
                   user_address ua_gu,         -- giver address
                   application_user au_ru,     -- receiver user
                   user_address ua_ru,         -- receiver address
                   vw_curr_pax_employer cpe_g, -- giver  employer
                   vw_curr_pax_employer cpe_r,  -- receiver employer
                   application_user au_pu        --proxy user name --01/11/2017
             WHERE c.is_open = 0 --06/08/2016
               AND c.promotion_id = p.promotion_id
               AND p.promotion_status IN ('live', 'expired')
               AND p.is_deleted = 0
               AND c.claim_id = ncb.claim_id
               AND c.claim_id = ci_r.claim_id
               AND ci_r.claim_item_id = cr.claim_item_id
               AND c.node_id = n_g.node_id(+)
               AND cr.node_id = n_r.node_id(+)
               AND c.submitter_id = au_gu.user_id(+)
               AND au_gu.user_id = ua_gu.user_id (+)
               AND ua_gu.is_primary (+)= 1                                   
               AND c.submitter_id = cpe_g.user_id(+)
               AND cr.participant_id = au_ru.user_id(+)
               AND au_ru.user_id = ua_ru.user_id(+)
               AND ua_ru.is_primary (+)= 1                                   
               AND cr.participant_id = cpe_r.user_id(+)
               AND c.proxy_user_id = au_pu.user_id (+) --01/11/2017
               AND (c.date_created > p_in_start_date AND c.date_created <= p_in_end_date)
            );     
   
   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

  SELECT COUNT(1)
    INTO v_rpt_cnt
    FROM rpt_behavior_detail;
   
  IF v_rpt_cnt > 0 THEN   --Dont need to run this part if it's full report refresh in between
      v_stage := 'Giver rpt_behavior_detail changed';               
      MERGE INTO rpt_behavior_detail dtl                
      USING ( 
        SELECT rpt.ROWID row_id,
               au.last_name,
               au.first_name,
               au.middle_name,
               DECODE(au.is_active,1,'active','inactive') pax_status,
               vcpe.department_type,
               vcpe.position_type
          FROM application_user au,
               rpt_behavior_detail rpt,
               vw_curr_pax_employer vcpe
         WHERE au.user_id   = rpt.giver_user_id
           AND rpt.giver_user_id= vcpe.user_id(+)
           AND (p_in_start_date < au.date_created AND au.date_created <= p_in_end_date
                OR p_in_start_date < au.date_modified  AND  au.date_modified <= p_in_end_date
                OR p_in_start_date < vcpe.date_created AND vcpe.date_created <= p_in_end_date
                OR p_in_start_date < vcpe.date_modified AND vcpe.date_modified <= p_in_end_date)
           AND (au.last_name            <> rpt.giver_last_name
                OR au.first_name        <> rpt.giver_first_name
                OR NVL(au.middle_name,'X')       <> NVL(rpt.giver_middle_name,'X')
                OR DECODE(au.is_active,1,'active','inactive') <> rpt.giver_pax_status
                OR NVL(vcpe.department_type,'X') <> NVL(rpt.giver_department,'X')      
                OR NVL(vcpe.position_type,'X')   <> NVL(rpt.giver_job_position,'X'))) e 
        ON (dtl.rowid = e.row_id)
       WHEN MATCHED THEN 
         UPDATE SET 
             dtl.giver_last_name    = e.last_name
            ,dtl.giver_first_name   = e.first_name
            ,dtl.giver_middle_name  = e.middle_name
            ,dtl.giver_pax_status   = e.pax_status
            ,dtl.giver_department   = e.department_type
            ,dtl.giver_job_position = e.position_type
            ,dtl.date_modified      = SYSDATE
            ,dtl.modified_by        = c_created_by;
       
       v_rec_cnt := SQL%ROWCOUNT;
       prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);
      
      v_stage := 'Receiver rpt_behavior_detail changed';       
      MERGE INTO rpt_behavior_detail dtl           
      USING ( 
        SELECT rpt.ROWID row_id,
               au.last_name,
               au.first_name,
               au.middle_name,
               DECODE(au.is_active,1,'active','inactive') pax_status,
               vcpe.department_type,
               vcpe.position_type
          FROM application_user au,
               rpt_behavior_detail rpt,
               vw_curr_pax_employer vcpe
         WHERE au.user_id        = rpt.recvr_user_id
           AND rpt.recvr_user_id = vcpe.user_id(+)
           AND (p_in_start_date < au.date_created       AND au.date_created <= p_in_end_date
                OR p_in_start_date < au.date_modified   AND au.date_modified <= p_in_end_date
                OR p_in_start_date < vcpe.date_created  AND vcpe.date_created <= p_in_end_date
                OR p_in_start_date < vcpe.date_modified AND vcpe.date_modified <= p_in_end_date)
           AND (au.last_name         <> rpt.recvr_last_name
             OR au.first_name        <> rpt.recvr_first_name
             OR NVL(au.middle_name,'X')       <> NVL(rpt.recvr_middle_name,'X')
             OR DECODE(au.is_active,1,'active','inactive') <> rpt.recvr_pax_status
             OR NVL(vcpe.department_type,'X') <> NVL(rpt.recvr_department,'X')  
             OR NVL(vcpe.position_type,'X')   <> NVL(rpt.recvr_job_position,'X'))) e 
        ON (dtl.rowid = e.row_id)
       WHEN MATCHED THEN 
         UPDATE SET 
             dtl.recvr_last_name    = e.last_name
            ,dtl.recvr_first_name   = e.first_name
            ,dtl.recvr_middle_name  = e.middle_name 
            ,dtl.recvr_pax_status   = e.pax_status
            ,dtl.recvr_department   = e.department_type
            ,dtl.recvr_job_position = e.position_type
            ,dtl.date_modified      = SYSDATE
            ,dtl.modified_by        = c_created_by;
            
       v_rec_cnt := SQL%ROWCOUNT;
       prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

       
       v_stage := 'Open and Fetch cursor to pick modified node name';
       OPEN cur_node_changed;                        
        FETCH cur_node_changed BULK COLLECT         
         INTO v_tab_node_id,
              v_tab_node_name;
        CLOSE cur_node_changed;   

      v_stage := 'Update modified node name for giver and receiver in rpt table';
      FORALL indx IN v_tab_node_id.FIRST .. v_tab_node_id.LAST      
        UPDATE rpt_behavior_detail
           SET giver_node_name = DECODE (giver_node_id, v_tab_node_id(indx), v_tab_node_name(indx), giver_node_name),
               recvr_node_name = DECODE (recvr_node_id, v_tab_node_id(indx), v_tab_node_name(indx), recvr_node_name),
               date_modified   = SYSDATE,
               modified_by     = c_created_by,
               version         = version + 1
         WHERE (   (    giver_node_id    = v_tab_node_id(indx)
                    AND giver_node_name != v_tab_node_name(indx)
                    ) 
                OR (  recvr_node_id    = v_tab_node_id(indx)
                    AND recvr_node_name != v_tab_node_name(indx)
                    )
                );  
  END IF;
   v_stage := 'Success';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
   p_out_return_code := 00;
   
EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code    := 99; 
    p_out_error_message  := v_stage||' '||SQLERRM; 
    prc_execution_log_entry(c_process_name, c_release_level,'ERROR',v_stage||' '||SQLERRM,null);
END prc_behavior_detail;

PROCEDURE prc_behavior_summary
 (p_in_requested_user_id    IN  NUMBER,
  p_out_return_code         OUT NUMBER,
  p_out_error_message       OUT VARCHAR2) IS

/*******************************************************************************
--
-- Purpose: Proocedures to populate behavior detail report
--
-- MODIFICATION HISTORY
-- Person      Date          Comments
-- ---------   ------        ------------------------------------------
-- nagarajs    04/26/2016   Initial  Creation
--Ravi Dhanekula 07/20/2016 Replaced obfuscation toolkit with dbms_crypto md5.
*******************************************************************************/
  v_stage       VARCHAR2(250);
  v_rec_cnt  INTEGER;

  c_process_name            CONSTANT execution_log.process_name%TYPE  := 'PRC_BEHAVIOR_SUMMARY';
  c_release_level           CONSTANT execution_log.release_level%TYPE := 1.0;
  c_created_by              CONSTANT rpt_behavior_detail.created_by%TYPE:= p_in_requested_user_id;
  c_default_key_field_hash  CONSTANT rpt_behavior_summary.key_field_hash%TYPE :=
                               dbms_crypto.hash( utl_raw.cast_to_raw(--07/20/2016
                                 0              -- promotion_id
                                 || '/' || ' '  -- behavior
                                 || '/' || ' '  -- pax_status
                                 || '/' || ' '  -- job_position
                                 || '/' || ' '  -- department
                                 ),2);

BEGIN

  v_stage := 'Start';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

   -- remove obsolete node summaries (node not found in rpt_hierarchy)
   v_stage := 'DELETE obsolete node summary records';
   DELETE
     FROM rpt_behavior_summary s
    WHERE s.detail_node_id NOT IN
          ( -- get node ID currently in the report hierarchy
            SELECT h.node_id
              FROM rpt_hierarchy h
          );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);

  -- merge derived summary records
  v_stage := 'MERGE detail derived summary records';
  MERGE INTO rpt_behavior_summary d
   USING (
            WITH rpt_teamsum AS
            (  -- build team summary records
               SELECT -- key fields
                      h.node_id                     AS detail_node_id,
                      'giver'                       AS giver_recvr_type,
                      d.promotion_id                AS promotion_id,
                      d.behavior                    AS behavior,
                      NVL(d.giver_pax_status,' ')   AS pax_status,
                      NVL(d.giver_job_position,' ') AS job_position,
                      NVL(d.giver_department,' ')   AS department,
                      -- used date_submitted instead of date_approved since considering open claims 
                      TRUNC(d.date_submitted)       AS date_submitted, 
                      -- reference fields
                      h.parent_node_id              AS header_node_id,
                      h.hier_level,
                      -- count fields                    
                      COUNT(DISTINCT d.claim_id)    AS behavior_cnt                     
                 FROM rpt_behavior_detail d,
                      rpt_hierarchy h
                WHERE h.node_id = d.giver_node_id
                GROUP BY h.node_id,
                      d.promotion_id,
                      d.behavior, 
                      NVL(d.giver_pax_status,' '),
                      NVL(d.giver_job_position,' '),
                      NVL(d.giver_department,' '),
                      TRUNC(d.date_submitted),
                      h.parent_node_id,
                      h.hier_level
                UNION ALL
               -- get receiver node hierachy details
               SELECT -- key fields
                      h.node_id                     AS detail_node_id,
                      'receiver'                    AS giver_recvr_type,
                      d.promotion_id                AS promotion_id,
                      d.behavior                    AS behavior,
                      NVL(d.recvr_pax_status,' ')   AS pax_status,
                      NVL(d.recvr_job_position,' ') AS job_position,
                      NVL(d.recvr_department,' ')   AS department,
                      TRUNC(d.date_submitted)        AS date_submitted,
                      -- reference fields
                      h.parent_node_id              AS header_node_id,
                      h.hier_level,
                      -- count fields                     
                      COUNT(DISTINCT d.claim_id)    AS behavior_cnt                     
                 FROM rpt_behavior_detail d,
                      rpt_hierarchy h
                WHERE h.node_id = d.recvr_node_id
                GROUP BY h.node_id,
                      d.promotion_id,
                      d.behavior,                      
                      NVL(d.recvr_pax_status,' '),
                      NVL(d.recvr_job_position,' '),
                      NVL(d.recvr_department,' '),
                      TRUNC(d.date_submitted),
                      h.parent_node_id,
                      h.hier_level               
            ), detail_derived_summary AS
            (  -- derive summaries based on detail data
               SELECT -- key fields
                      rt.detail_node_id,
                      'teamsum' AS sum_type,
                      rt.giver_recvr_type,
                      rt.promotion_id,
                      rt.behavior,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.date_submitted,
                      -- reference fields
                      dbms_crypto.hash( utl_raw.cast_to_raw(--07/20/2016
                                  rt.promotion_id
                        || '/' || rt.behavior
                        || '/' || rt.pax_status
                        || '/' || rt.job_position
                        || '/' || rt.department
                      ),2) as key_field_hash,
                      rt.header_node_id,
                      rt.hier_level,
                      1 AS is_leaf,  -- team summary always a leaf node
                      -- count fields                     
                      rt.behavior_cnt                    
                 FROM rpt_teamsum rt
                UNION ALL
               SELECT -- key fields
                      h.node_id AS detail_node_id,
                      'nodesum' AS sum_type,
                      rt.giver_recvr_type,
                      rt.promotion_id,
                      rt.behavior,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.date_submitted,
                      -- reference fields
                      dbms_crypto.hash( utl_raw.cast_to_raw(--07/20/2016
                                  rt.promotion_id
                        || '/' || rt.behavior
                        || '/' || rt.pax_status
                        || '/' || rt.job_position
                        || '/' || rt.department
                      ),2) as key_field_hash,
                      h.parent_node_id AS header_node_id,
                      h.hier_level,
                      NVL(h.is_leaf, 1) AS is_leaf,
                      -- count fields                     
                      SUM(rt.behavior_cnt)          AS behavior_cnt                                         
                 FROM ( -- associate each node to all its hierarchy nodes
                        SELECT np.node_id,
                               p.column_value AS path_node_id
                          FROM ( -- get node hierarchy path
                                 SELECT h.node_id,
                                        h.hier_level,
                                        sys_connect_by_path(node_id, '/') || '/' AS node_path
                                   FROM rpt_hierarchy h
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
                               ) AS sys.odcinumberlist ) ) p
                      ) npn,
                      rpt_hierarchy h,
                      rpt_teamsum rt
                WHERE rt.behavior_cnt != 0
                   -- join detail record to all node path nodes allowing detail grouping across all hierarchy nodes
                  AND rt.detail_node_id = npn.node_id (+)
                  AND npn.path_node_id = h.node_id (+)
                GROUP BY h.node_id,
                      rt.giver_recvr_type,
                      rt.promotion_id,
                      rt.behavior,
                      rt.pax_status,
                      rt.job_position,
                      rt.department,
                      rt.date_submitted,
                      h.parent_node_id,
                      h.hier_level,
                      h.is_leaf
            ) -- end detail_derived_summary
            -- compare existing summary records with detail derived summaries
            SELECT es.s_rowid,
                   NVL(dds.hier_level, 0) || '-' || dds.sum_type AS record_type,
                   dds.*
              FROM (  -- get existing summaries based on derived details
                     SELECT s2.ROWID AS s_rowid,
                            s2.detail_node_id,
                            s2.sum_type,
                            s2.giver_recvr_type,
                            s2.date_submitted,
                            s2.key_field_hash
                       FROM rpt_behavior_summary s2
                   ) es
                   -- full outer join so unmatched existing summaries can be deleted
                   FULL OUTER JOIN detail_derived_summary dds
                   ON (   es.detail_node_id = dds.detail_node_id
                      AND es.sum_type         = dds.sum_type
                      AND es.giver_recvr_type = dds.giver_recvr_type
                      AND NVL(es.date_submitted,SYSDATE)    = NVL(dds.date_submitted,SYSDATE) 
                      AND es.key_field_hash   = dds.key_field_hash
                      )
         ) s
      ON (d.ROWID = s.s_rowid)
    WHEN MATCHED THEN
      UPDATE SET
         d.header_node_id           = s.header_node_id,
         d.hier_level               = s.hier_level,
         d.is_leaf                  = s.is_leaf,        
         d.behavior_cnt             = s.behavior_cnt,        
         d.modified_by              = c_created_by,
         d.date_modified            = SYSDATE
       WHERE ( -- only update records with different values
                DECODE(d.header_node_id,           s.header_node_id,           1, 0) = 0
             OR DECODE(d.hier_level,               s.hier_level,               1, 0) = 0
             OR DECODE(d.is_leaf,                  s.is_leaf,                  1, 0) = 0             
             OR DECODE(d.behavior_cnt,             s.behavior_cnt,          1, 0) = 0             
             )
      -- remove existing summaries that no longer have details
      DELETE
       WHERE s.detail_node_id IS NULL
    WHEN NOT MATCHED THEN
      INSERT
      (  rpt_behavior_summary_id,
         -- key fields
         detail_node_id,
         sum_type,         
         giver_recvr_type,
         promotion_id,
         behavior,
         pax_status,
         job_position,
         department,
         date_submitted,
         -- reference fields
         key_field_hash,
         header_node_id,
         hier_level,
         is_leaf,
         record_type,
         -- count fields        
         behavior_cnt,       
         -- audit fields
         created_by,
         date_created,
         modified_by,
         date_modified
      )
      VALUES
      (  rpt_behavior_summary_id_pk_sq.nextval,
         -- key fields
         s.detail_node_id,
         s.sum_type,        
         s.giver_recvr_type,
         s.promotion_id,
         s.behavior,
         s.pax_status,
         s.job_position,
         s.department,
         s.date_submitted,
         -- reference fields
         s.key_field_hash,
         s.header_node_id,
         s.hier_level,
         s.is_leaf,
          s.record_type,
         -- count fields        
         s.behavior_cnt,       
         -- audit fields
         c_created_by,
         SYSDATE,
         NULL,
         NULL
      );

   v_rec_cnt := SQL%ROWCOUNT;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || ' (' || v_rec_cnt || ' records processed)', NULL);  

   v_stage := 'Success';
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
   
   p_out_return_code := 00;

EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code :=99;
    prc_execution_log_entry(c_process_name,c_release_level,'ERROR','stage '||v_stage||SQLERRM,NULL);
END;

END;
/
