CREATE OR REPLACE PROCEDURE PRC_RA_WELCOME_EMAIL(p_out_return_code   OUT NUMBER,
                                                 p_out_user_data     OUT SYS_REFCURSOR)
IS                                                 
 PRAGMA AUTONOMOUS_TRANSACTION;
    /*******************************************************************************
   -- Purpose: To generate result for RA welcome email
   --
   -- Person        Date         Comments
   -- -----------   --------     -----------------------------------------------------
   -- Gorantla      04/03/2018   Initial creation                                                  
   *******************************************************************************/
  c_process_name          CONSTANT execution_log.process_name%TYPE:='PRC_RA_WELCOME_EMAIL';

  v_stage                 VARCHAR2(400);
  v_primary_aud           VARCHAR2(400);
  
  
BEGIN
  prc_execution_log_entry(c_process_name,1,'INFO','Process Started ',null);
                                               
 v_stage := 'Get eligible RA promotions';

WITH promo_aud as
    (SELECT p.*
      FROM promotion p,
           promo_recognition pr
     WHERE p.promotion_id = pr.promotion_id
       AND pr.is_include_purl = 0
       AND pr.include_celebrations = 0
       AND promotion_type = 'recognition'
       AND award_type != 'merchandise'
       AND promotion_status = 'live'
       AND is_fileload_entry = 0)
 SELECT CASE WHEN audience_type LIKE '%allactivepaxaudience%' THEN 'all_active' 
             WHEN audience_type LIKE '%specifyaudience%' THEN 'specific'
         ELSE 'no_aud' END primary_aud
   INTO v_primary_aud
   FROM (SELECT listagg( primary_audience_type, ',') within group (order by primary_audience_type) audience_type
           FROM (SELECT distinct primary_audience_type
                   FROM promo_aud
                  )
         );    

IF v_primary_aud = 'all_active' THEN 
 
 v_stage := 'Get list of all active managers';     
OPEN p_out_user_data FOR       
WITH mgr_node AS
-- Get nodes which are mgr/own and should have atleat one pax
(SELECT node_id
   FROM user_node un
  WHERE role <> 'mbr'
    AND status = 1
    AND EXISTS (SELECT 1
                  FROM user_node 
                 WHERE node_id = un.node_id
                   AND status = 1
                   AND role = 'mbr')
UNION
-- check in levels above to find nodes if current node doesnt have mgr/own
    SELECT  node_id
      FROM
    (SELECT CASE WHEN cur_node_ownr_avlbl>0 
              THEN node_id 
             ELSE parent_node_id END
                node_id
      FROM (
            SELECT level lvl,
                   (SELECT count(*) FROM user_node c WHERE c.node_id = a.node_id AND c.role IN ('own','mgr')) cur_node_ownr_avlbl,
                   (SELECT count(*) FROM user_node d WHERE d.node_id = a.parent_node_id AND d.role IN ('own','mgr'))  par_node_ownr_avlbl,a.*
             FROM node a
             START WITH parent_node_id is null
             CONNECT BY PRIOR node_id = a.parent_node_id)
     WHERE cur_node_ownr_avlbl > 0 OR par_node_ownr_avlbl > 0) un
    WHERE node_id IS NOT NULL
      AND EXISTS (SELECT 1
                  FROM user_node 
                 WHERE node_id = un.node_id
                   AND status = 1
                   AND role = 'mbr'))
    SELECT au.first_name,
           au.last_name,
           au.user_id,
           uea.email_addr
      FROM mgr_node mn,
           user_node un, 
           application_user au,
           participant p,
           user_email_address uea
     WHERE mn.node_id = un.node_id
       AND un.user_id = au.user_id
       AND au.user_id = p.user_id
       AND p.user_id = uea.user_id
       AND uea.is_primary = 1
       AND au.is_active = 1
       AND p.is_opt_out_of_program = 0 
       AND au.is_ra_welcome_email_sent = 0
       AND p.status = 'active'  
       AND un.role <> 'mbr' ;
       
ELSIF v_primary_aud = 'specific' THEN 
 
v_stage := 'Get list of specific managers';
    OPEN p_out_user_data FOR
  WITH mgr_node AS
-- Get nodes which are mgr/own and should have atleat one pax
(SELECT node_id
   FROM user_node un
  WHERE role <> 'mbr'
    AND status = 1
    AND EXISTS (SELECT 1
                  FROM user_node 
                 WHERE node_id = un.node_id
                   AND status = 1
                   AND role = 'mbr')
UNION
-- check in levels above to find nodes if current node doesnt have mgr/own
    SELECT  node_id
      FROM
    (SELECT CASE WHEN cur_node_ownr_avlbl>0 
              THEN node_id 
             ELSE parent_node_id END
                node_id
      FROM (
            SELECT level lvl,
                   (SELECT count(*) FROM user_node c WHERE c.node_id = a.node_id AND c.role IN ('own','mgr')) cur_node_ownr_avlbl,
                   (SELECT count(*) FROM user_node d WHERE d.node_id = a.parent_node_id AND d.role IN ('own','mgr'))  par_node_ownr_avlbl,a.*
             FROM node a
             START WITH parent_node_id is null
             CONNECT BY PRIOR node_id = a.parent_node_id)
     WHERE cur_node_ownr_avlbl > 0 OR par_node_ownr_avlbl > 0) un
    WHERE node_id IS NOT NULL
      AND EXISTS (SELECT 1
                  FROM user_node 
                 WHERE node_id = un.node_id
                   AND status = 1
                   AND role = 'mbr'))
    SELECT au.first_name,
           au.last_name,
           au.user_id,
           uea.email_addr
      FROM mgr_node mn,
           user_node un, 
           application_user au,
           participant p,
           user_email_address uea
     WHERE mn.node_id = un.node_id
       AND un.user_id = au.user_id
       AND au.user_id = p.user_id
       AND p.user_id = uea.user_id
       AND uea.is_primary = 1
       AND au.is_active = 1
       AND p.is_opt_out_of_program = 0 
       AND au.is_ra_welcome_email_sent = 0
       AND p.status = 'active'  
       AND un.role <> 'mbr'
       AND EXISTS (SELECT *
                     FROM PROMO_AUDIENCE pa,
                          promotion p,
                          PARTICIPANT_AUDIENCE pax
                    WHERE pa.promotion_id = p.promotion_id
                      AND pa.audience_id = pax.audience_id
                      AND p.primary_audience_type = 'specifyaudience'
                      AND pax.user_id = au.user_id); 
ELSE 
  OPEN p_out_user_data FOR 
   SELECT NULL first_name,
          NULL last_name,
          NULL user_id,
          NULL email_addr
     FROM dual  ;
                        
END IF;                       
  
prc_execution_log_entry(c_process_name,1,'INFO','Process Completed ',null);
p_out_return_code:=0;
  
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    p_out_return_code :=  99 ;
    prc_execution_log_entry(c_process_name,1,'ERROR',v_stage||' - '||SQLERRM,null);
    OPEN p_out_user_data FOR SELECT NULL FROM dual  ;
END;
/
