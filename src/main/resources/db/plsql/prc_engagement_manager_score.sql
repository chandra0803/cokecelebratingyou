CREATE OR REPLACE PROCEDURE prc_engagement_manager_score(
p_in_user_id        IN    NUMBER,
p_in_mode           IN   VARCHAR2,
p_in_time_frame    IN VARCHAR2,
p_in_node_id        IN    NUMBER,
p_in_locale         IN    VARCHAR2,
p_in_end_month     IN    NUMBER,
p_in_end_year       IN    NUMBER,
p_in_rownum_start   IN    NUMBER,
p_in_rownum_end     IN    NUMBER,
p_in_sort_mem_col  IN   VARCHAR2,
p_in_sort_mem_order IN VARCHAR2,
p_in_sort_team_col IN VARCHAR2,
p_in_sort_team_order IN VARCHAR2,
p_in_start_date     IN    VARCHAR2,
p_in_end_date       IN    VARCHAR2,
p_out_result_set1   OUT sys_refcursor,
p_out_result_set2   OUT sys_refcursor,
p_out_result_set3   OUT sys_refcursor,
p_out_result_set4   OUT sys_refcursor,
p_out_result_set5   OUT sys_refcursor,
p_out_result_set6   OUT sys_refcursor,
p_out_result_set7   OUT sys_refcursor
)
AS
v_user_node_list    VARCHAR2(100);
v_child_node_list    VARCHAR2(1000);

p_start_date DATE;
p_end_date DATE;
v_as_of_date DATE;
v_company_goal NUMBER;
v_is_score_active NUMBER;

v_rec_sent_company_avg NUMBER;
v_rec_recv_company_avg NUMBER;
v_conn_to_company_avg NUMBER;
v_conn_from_company_avg NUMBER;
v_login_company_avg NUMBER;
v_score_company_avg NUMBER;


BEGIN
    prc_execution_log_entry('prc_engagement_manager_score',1,'INFO','START',null);  
    
      p_start_date := TO_DATE(P_IN_START_DATE,'MM/DD/YYYY');
      p_end_date := TO_DATE(P_IN_END_DATE,'MM/DD/YYYY');
      
      BEGIN
      SELECT prev_process_date,company_goal,is_score_active INTO v_as_of_date,v_company_goal,v_is_score_active FROM promo_engagement pe WHERE EXISTS 
      (SELECT * FROM promotion WHERE promotion_status = 'live' AND promotion_id = pe.promotion_id  );
      END;
      
                  
          SELECT received_count/total_participant_count,
                sent_count/total_participant_count,
                connected_from_count/total_participant_count,
                connected_to_count/total_participant_count,
                login_activity_count/total_participant_count,score  INTO v_rec_recv_company_avg,v_rec_sent_company_avg,v_conn_from_company_avg,v_conn_to_company_avg,v_login_company_avg,v_score_company_avg  from engagement_score_summary d
    WHERE parent_node_id IS NULL
           AND d.time_frame = p_in_time_frame AND d.record_type ='nodesum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;
     
    IF upper(p_in_mode) = upper('team') THEN 
      
    --get first child node list        
        SELECT LISTAGG(node_id,',') WITHIN GROUP(ORDER BY node_id)
            INTO v_child_node_list
        FROM
        (SELECT n.node_id FROM node n,user_node un WHERE
        un.user_id = p_in_user_id
         AND un.role IN ('own','mgr')
         AND parent_node_id = un.node_id ); 
         
    IF p_in_node_id IS NULL THEN
    
        SELECT LISTAGG(node_id,',') WITHIN GROUP(ORDER BY node_id) 
            INTO v_user_node_list
        FROM user_node
        WHERE user_id = p_in_user_id
            AND role IN ('own','mgr');    
        
             OPEN p_out_result_set1 FOR
        SELECT v_as_of_date as_of_date,v_company_goal company_goal,v_is_score_active is_score_active,d.score ,d.received_count,d.sent_count,d.connected_to_count,
                                 d.connected_from_count,d.login_activity_count,
                                 d.received_target,d.sent_target,d.connected_target,
                                 d.connected_from_target,d.login_activity_target,
                                 d.score_achieved_count,d.sent_achieved_count,
                                 d.recv_achieved_count,d.conn_to_achieved_count,
                                 d.conn_from_achieved_count,d.login_achieved_count,
                                 d.total_participant_count
                                  FROM (
        SELECT  SUM (d.score * d.total_participant_count) / SUM(d.total_participant_count) score ,sum(d.received_count) received_count,sum(d.sent_count) sent_count,sum(d.connected_to_count) connected_to_count,
                                 sum(d.connected_from_count) connected_from_count,sum(d.login_activity_count) login_activity_count,
                                 sum(d.received_target) received_target,sum(d.sent_target) sent_target,sum(d.connected_target) connected_target,
                                 sum(d.connected_from_target) connected_from_target,sum(d.login_activity_target) login_activity_target,
                                 sum(d.score_achieved_count) score_achieved_count,
                                 sum(d.sent_achieved_count) sent_achieved_count,
                                 sum(d.recv_achieved_count) recv_achieved_count,
                                 sum(d.conn_to_achieved_count) conn_to_achieved_count,
                                 sum(d.conn_from_achieved_count) conn_from_achieved_count,
                                 sum(d.login_achieved_count) login_achieved_count,
                                 sum(d.total_participant_count) total_participant_count
                          FROM engagement_score_summary d 
                          WHERE d.time_frame = p_in_time_frame AND d.record_type ='nodesum' AND d.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))
                                AND trans_month = p_in_end_month
                               AND trans_year = p_in_end_year)d;
                               
           OPEN p_out_result_set6 FOR
        SELECT node_id,name node_name FROM node n
                          WHERE n.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)));
            
        OPEN p_out_result_set2 FOR
        SELECT * FROM (             
        SELECT v_company_goal company_goal,un.node_id,n.name node_name,a.user_id,a.first_name,a.last_name,ecd.score,
        received_target,sent_target,connected_target,connected_from_target,login_activity_target,
        received_count,sent_count,connected_to_count,connected_from_count,login_activity_count        
        FROM user_node un,application_user a,engagement_score_detail ecd,node n
        WHERE un.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))
            AND un.user_id = a.user_id
            AND a.user_id = ecd.user_id
            AND un.node_id = n.node_id
            AND ecd.time_frame = p_in_time_frame
            AND trans_month = p_in_end_month
            AND trans_year = p_in_end_year
            ORDER BY p_in_sort_mem_col||' '||p_in_sort_mem_order) WHERE ROWNUM BETWEEN p_in_rownum_start AND p_in_rownum_end
            ;
        
        OPEN p_out_result_set3 FOR
        SELECT * FROM (
        SELECT v_company_goal company_goal,s.node_id,rh.name node_name,rh.parent_node_id,score,
        received_target,sent_target,connected_target,connected_from_target,login_activity_target,
        received_count,sent_count,connected_to_count,connected_from_count,login_activity_count,au.last_name mgr_last_name,au.first_name mgr_first_name 
        FROM engagement_score_summary s,node rh, user_node un, application_user au
        WHERE s.record_type ='nodesum' 
            AND time_frame = p_in_time_frame
            AND s.node_id IN (SELECT * FROM TABLE(get_array(v_child_node_list)))
            AND s.node_id = rh.node_id
            AND trans_month = p_in_end_month
            AND trans_year = p_in_end_year
            AND rh.node_id = un.node_id
            AND un.role = 'mgr'
            AND un.user_id = au.user_id
            ORDER BY p_in_sort_team_col||' '||p_in_sort_team_order) WHERE ROWNUM BETWEEN p_in_rownum_start AND p_in_rownum_end; 
            
            OPEN p_out_result_set4 FOR
        SELECT v_company_goal company_goal,d.node_id,n.name node_name, d.score ,d.received_count,d.sent_count,d.connected_to_count,
                                 d.connected_from_count,d.login_activity_count,
                                 d.received_target,d.sent_target,d.connected_target,
                                 d.connected_from_target,d.login_activity_target
                          FROM engagement_score_summary d, node n 
                          WHERE d.time_frame = p_in_time_frame AND d.record_type ='teamsum' AND d.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))
                                AND trans_month = p_in_end_month
                                AND trans_year = p_in_end_year 
                                AND d.node_id = n.node_id;
      
    OPEN p_out_result_set5 FOR
    SELECT cms.cms_name behavior,sent_count,received_count FROM (    
    SELECT behavior,SUM (sent_count) sent_count,
         SUM (received_count) received_count FROM (
      SELECT behavior,
         sent_count sent_count,
         received_count received_count
    FROM engagement_behavior_summary d
   WHERE d.header_node_id IN (SELECT *
                                FROM TABLE (get_array (v_user_node_list)))
AND Last_day(TO_DATE(trans_month||'/01'||'/'||trans_year,'MM/DD/YYYY')) BETWEEN p_start_date AND p_end_date
UNION ALL
SELECT behavior_type behavior, 0 sent_count, 0 received_count
  FROM promo_behavior
 WHERE promotion_id IN (SELECT eligible_promotion_id
                          FROM promo_engagement_promotions))
GROUP BY behavior                          
                          ) d,
(SELECT cms_name,cms_code
     FROM vw_cms_code_value E
    WHERE asset_code in ( 'picklist.promo.recognition.behavior.items') and e.locale=p_in_locale) cms
WHERE cms.cms_code = d.behavior;

OPEN p_out_result_set7 FOR--For team avg and Company average
    SELECT SUM(received_count)/SUM(total_participant_count) AS v_rec_recv_team_avg,
                SUM(sent_count)/SUM(total_participant_count) AS v_rec_sent_team_avg,
                SUM(connected_from_count)/SUM(total_participant_count) AS v_conn_from_team_avg,
                SUM(connected_to_count)/SUM(total_participant_count) AS v_conn_to_team_avg,
                SUM(login_activity_count)/SUM(total_participant_count) AS v_login_team_avg,
                v_rec_sent_company_avg as v_rec_sent_company_avg,v_rec_recv_company_avg as v_rec_recv_company_avg,v_conn_to_company_avg as v_conn_to_company_avg,
                v_conn_from_company_avg as v_conn_from_company_avg,v_login_company_avg as v_login_company_avg,v_score_company_avg as v_score_company_avg FROM engagement_score_summary d
    WHERE d.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)))        
                   AND d.time_frame = p_in_time_frame AND d.record_type ='teamsum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;    
    ELSE
        OPEN p_out_result_set1 FOR
        SELECT  v_as_of_date as_of_date,v_company_goal company_goal,v_is_score_active is_score_active,d.node_id,n.name node_name, d.score ,d.received_count,d.sent_count,d.connected_to_count,
                                 d.connected_from_count,d.login_activity_count,
                                 d.received_target,d.sent_target,d.connected_target,
                                 d.connected_from_target,d.login_activity_target,
                                 d.score_achieved_count,d.sent_achieved_count,
                                 d.recv_achieved_count,d.conn_to_achieved_count,
                                 d.conn_from_achieved_count,d.login_achieved_count,
                                 d.total_participant_count
                          FROM engagement_score_summary d, node n 
                          WHERE time_frame = p_in_time_frame AND d.record_type ='nodesum' AND d.node_id = p_in_node_id
                                AND trans_month = p_in_end_month
                                AND trans_year = p_in_end_year
                                AND d.node_id = n.node_id;
                         
        OPEN p_out_result_set2 FOR
        SELECT * FROM (            
        SELECT v_company_goal company_goal,un.node_id,n.name node_name,a.user_id,a.first_name,a.last_name,ecd.score,
        received_target,sent_target,connected_target,connected_from_target,login_activity_target,
        received_count,sent_count,connected_to_count,connected_from_count,login_activity_count      
        FROM user_node un,application_user a,engagement_score_detail ecd, node n
        WHERE un.node_id = p_in_node_id
            AND un.user_id = a.user_id
            AND a.user_id = ecd.user_id
            AND un.node_id = n.node_id
            AND time_frame = p_in_time_frame 
            AND trans_month = p_in_end_month
            AND trans_year = p_in_end_year
            ORDER BY p_in_sort_mem_col||' '||p_in_sort_mem_order) WHERE ROWNUM BETWEEN p_in_rownum_start AND p_in_rownum_end;            
       
        OPEN p_out_result_set3 FOR
        SELECT * FROM (
        SELECT v_company_goal company_goal, s.node_id, rh.name node_name, rh.parent_node_id parent_node_id, s.score,
        received_target,sent_target,connected_target,connected_from_target,login_activity_target,
        received_count,sent_count,connected_to_count,connected_from_count,login_activity_count,au.last_name mgr_last_name,au.first_name mgr_first_name 
        FROM engagement_score_summary s,node rh, user_node un, application_user au
        WHERE s.record_type ='nodesum' 
            AND s.node_id IN (SELECT * FROM TABLE(get_array(v_child_node_list)))
            AND s.node_id = rh.node_id
            AND time_frame = p_in_time_frame
            AND trans_month = p_in_end_month
            AND trans_year = p_in_end_year
            AND rh.node_id = un.node_id
            AND un.role = 'mgr'
            AND un.user_id = au.user_id
            ORDER BY p_in_sort_team_col||' '||p_in_sort_team_order) WHERE ROWNUM BETWEEN p_in_rownum_start AND p_in_rownum_end; 
        
        OPEN p_out_result_set4 FOR
        SELECT v_company_goal company_goal,d.node_id,n.name node_name, d.score ,d.received_count,d.sent_count,d.connected_to_count,
                                 d.connected_from_count,d.login_activity_count,
                                 d.received_target,d.sent_target,d.connected_target,
                                 d.connected_from_target,d.login_activity_target
                          FROM engagement_score_summary d, node n 
                          WHERE d.time_frame = p_in_time_frame AND d.record_type ='teamsum' AND d.node_id = p_in_node_id
                                AND trans_month = p_in_end_month
                                AND trans_year = p_in_end_year 
                                AND d.node_id = n.node_id;
                                
     OPEN p_out_result_set5 FOR
    SELECT cms.cms_name behavior,sent_count,received_count FROM (    
    SELECT behavior,SUM (sent_count) sent_count,
         SUM (received_count) received_count FROM (
      SELECT behavior,
         sent_count sent_count,
         received_count received_count
    FROM engagement_behavior_summary d
   WHERE d.header_node_id =p_in_node_id
AND Last_day(TO_DATE(trans_month||'/01'||'/'||trans_year,'MM/DD/YYYY')) BETWEEN p_start_date AND p_end_date
UNION ALL
SELECT behavior_type behavior, 0 sent_count, 0 received_count
  FROM promo_behavior
 WHERE promotion_id IN (SELECT eligible_promotion_id
                          FROM promo_engagement_promotions))
GROUP BY behavior                          
                          ) d,
(SELECT cms_name,cms_code
     FROM vw_cms_code_value E
    WHERE asset_code in ( 'picklist.promo.recognition.behavior.items') and e.locale=p_in_locale) cms
WHERE cms.cms_code = d.behavior;
    END IF;    
   
OPEN p_out_result_set6 FOR
        SELECT node_id,name node_name FROM node n
                          WHERE n.node_id IN (SELECT * FROM TABLE(get_array(v_user_node_list)));
                          
                          OPEN p_out_result_set7 FOR--For team avg and Company average
    SELECT received_count/total_participant_count AS v_rec_recv_team_avg,
                sent_count/total_participant_count AS v_rec_sent_team_avg,
                connected_from_count/total_participant_count AS v_conn_from_team_avg,
                connected_to_count/total_participant_count AS v_conn_to_team_avg,
                login_activity_count/total_participant_count AS v_login_team_avg,
                v_rec_sent_company_avg,v_rec_recv_company_avg,v_conn_to_company_avg,
                v_conn_from_company_avg,v_login_company_avg,v_score_company_avg FROM engagement_score_summary d
    WHERE d.node_id  = p_in_node_id               
                   AND d.time_frame = p_in_time_frame AND d.record_type ='teamsum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;    
    ELSE  
       
    OPEN p_out_result_set7 FOR--For team avg and Company average
    SELECT received_count/total_participant_count AS v_rec_recv_team_avg,
                sent_count/total_participant_count AS v_rec_sent_team_avg,
                connected_from_count/total_participant_count AS v_conn_from_team_avg,
                connected_to_count/total_participant_count AS v_conn_to_team_avg,
                login_activity_count/total_participant_count AS v_login_team_avg,
                v_rec_sent_company_avg as v_rec_sent_company_avg,v_rec_recv_company_avg as v_rec_recv_company_avg,v_conn_to_company_avg as v_conn_to_company_avg,
                v_conn_from_company_avg as v_conn_from_company_avg,v_login_company_avg as v_login_company_avg,v_score_company_avg as v_score_company_avg FROM engagement_score_summary d,user_node un
    WHERE d.node_id  = un.node_id
                AND un.user_id = p_in_user_id
                AND un.is_primary = 1
           AND d.time_frame = p_in_time_frame AND d.record_type ='teamsum'
                   AND trans_month = p_in_end_month
                   AND trans_year = p_in_end_year;        
                          
    OPEN p_out_result_set1 FOR
                                SELECT  v_as_of_date as_of_date,v_company_goal company_goal,v_is_score_active is_score_active,d.score,                                
                                  d.received_count,d.sent_count,d.connected_to_count,d.connected_from_count,d.login_activity_count,d.received_target,d.sent_target,d.connected_target,d.connected_from_target,
                                  d.login_activity_target                              
                          FROM engagement_score_detail d 
                          WHERE time_frame = p_in_time_frame AND d.user_id = p_in_user_id                                
                               AND trans_month = p_in_end_month
                               AND trans_year = p_in_end_year;
    OPEN p_out_result_set2 FOR
    SELECT NULL FROM DUAL;
    
    OPEN p_out_result_set3 FOR
    SELECT NULL FROM DUAL;
    
     OPEN p_out_result_set4 FOR
    SELECT NULL FROM DUAL;
    
     OPEN p_out_result_set5 FOR
    SELECT NULL FROM DUAL;
    
    OPEN p_out_result_set6 FOR
    SELECT NULL FROM DUAL;
    END IF;
    prc_execution_log_entry('prc_engagement_manager_score',1,'INFO','END',null); 
EXCEPTION
WHEN OTHERS THEN 
    prc_execution_log_entry('prc_engagement_manager_score',1,'INFO',SQLERRM,null);  

END prc_engagement_manager_score;
/