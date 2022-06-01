CREATE OR REPLACE PROCEDURE PRC_RA_REMINDER (p_in_user_id        IN  application_user.user_id%TYPE, 
                                              p_in_exclude_upcoming      IN  NUMBER,
                                              p_in_no_pend        IN NUMBER,
                                              p_in_emp_filter     IN VARCHAR2,
                                              p_in_rownum_start   IN  NUMBER,
                                              p_in_rownum_end     IN  NUMBER,
                                              p_in_sortColName    IN  VARCHAR2,
                                              p_in_sortedBy       IN  VARCHAR2,
                                              p_out_return_code   OUT NUMBER,
                                              p_out_user_data     OUT SYS_REFCURSOR) 
 IS
  PRAGMA AUTONOMOUS_TRANSACTION;
    /*******************************************************************************
   -- Purpose: To generate result for RA, which will include new hires, overdue and upcoming pax
   --
   -- Person        Date         Comments
   -- -----------   --------     -----------------------------------------------------
   -- Gorantla      01/06/2018   Initial creation    
   -- Loganathan    03/29/2019   Bug 78991 - Upcoming page - Application is displaying pending for auto-approval claim too    
   -- Loganathan    05/03/2019   Bug 79050 - move "WHERE (by_me_promotion_id IS NULL" inside R alias
                                 when used after R, it's out of scope and query is invalid (won't compile) 
   -- Loganathan    08/06/2019	 Bug 79087 - RA overdue notifications shows large overdue days when pax moved from One 
								 Manager to Other Manager	                                          		
   *******************************************************************************/
  c_process_name          CONSTANT execution_log.process_name%TYPE:='PRC_RA_REMINDER';

  v_user_node             user_node.node_id%TYPE;
  v_live_cnt_promotion    NUMBER(10);
  v_cnt_all_active_promo  NUMBER(10);
  v_stage                 VARCHAR2(400);
  v_query_all_active      VARCHAR2(32767);
  v_query_spec_active     VARCHAR2(32767);
  v_query_active          VARCHAR2(32767);
  v_query_with            VARCHAR2(32767);
  v_spec_query_with       VARCHAR2(32767);
  v_sortCol               VARCHAR2(400);
  v_emp_filter            VARCHAR2(30);
  v_level                 PLS_INTEGER;
  v_orphan_nodes          PLS_INTEGER;
  v_fetch_count           PLS_INTEGER;
  v_return_code           PLS_INTEGER;
  v_no_pend               PLS_INTEGER;
   
  
  -- colour variables
  v_color_reminder        PLS_INTEGER;
  v_color_nh              PLS_INTEGER;
  v_color_n2              PLS_INTEGER;
  v_color_r2              PLS_INTEGER;
    
  --System variables
  v_days_emp_reminder     os_propertyset.int_val%TYPE;
  v_days_new_to_regemp    os_propertyset.int_val%TYPE;
  v_no_of_levels          os_propertyset.int_val%TYPE;
  
  no_user_id              EXCEPTION;
  
BEGIN
  prc_execution_log_entry(c_process_name,1,'INFO','Process Started '||p_in_user_id||' -'||
                                                  p_in_exclude_upcoming||' -'||
                                                  p_in_emp_filter||' -'||
                                                  p_in_rownum_start||' -'||
                                                  p_in_rownum_end||' -'||
                                                  p_in_sortColName||' -'||
                                                  p_in_sortedBy,null);
  
  v_stage := 'UserID validation';
  IF p_in_user_id IS NULL THEN
    RAISE no_user_id;
  END IF;
  
  IF p_in_emp_filter IS NOT NULL THEN
  v_emp_filter := p_in_emp_filter;
  ELSE
    v_emp_filter := 'NULL';
  END IF;  
  
  IF p_in_no_pend IS NOT NULL THEN
      v_no_pend := p_in_no_pend;
  ELSE
    v_no_pend := 0;
  END IF; 

  v_stage := 'Days after which a recogn reminder is shown for regular emp'; 
  SELECT int_val
    INTO v_days_emp_reminder
    FROM os_propertyset
   WHERE entity_name = 'ra.numberofdays.employee.reminder';
   
  v_color_reminder := floor(v_days_emp_reminder/3);
  v_color_r2       := v_days_emp_reminder + v_color_reminder;
 
  v_stage := 'Days after which a newhire is changed to regular emp status';
  SELECT int_val
    INTO v_days_new_to_regemp
    FROM os_propertyset
   WHERE entity_name = 'ra.numberofdays.newhiretoregularemployee';
   
  v_color_nh  := floor(v_days_new_to_regemp/3);
  v_color_n2  := v_color_nh*2;

  v_stage := 'Populate promotions and redeivers info into gtt tables '; 
  prc_ra_promo_level(p_in_user_id,v_return_code);

  v_stage := 'Check for promotions with givers as all active pax '; 
  SELECT count(1)
    INTO v_cnt_all_active_promo
    FROM gtt_ra_promotion
   WHERE promotion_status = 'live'
     AND secondary_audience_type = 'allactivepaxaudience';
     
  SELECT count(1)
    INTO v_live_cnt_promotion
    FROM gtt_ra_promotion
   WHERE promotion_status = 'live';     
     
  IF p_in_sortColName = 'NO_VALUE' THEN   
     v_sortCol := ' is_new_hire asc,
                    by_me_since_last_recog desc,
                    upper(first_name),
                    upper(last_name),
                    user_id';
  ELSIF p_in_sortColName = 'is_new_hire' THEN
    IF (p_in_sortedBy IS NULL OR p_in_sortedBy = 'asc') THEN
        v_sortCol := p_in_sortColName|| ' asc, by_me_since_last_recog desc,upper(first_name)';
    ELSE
        v_sortCol := p_in_sortColName|| ' desc, by_me_since_last_recog asc,upper(first_name) desc';
    END IF;
  ELSIF p_in_sortColName IN ('first_name', 'last_name') THEN
    v_sortCol := 'upper('||p_in_sortColName||')' || ' ' || NVL(p_in_sortedBy, ' ') ;
  ELSE
    IF (p_in_sortedBy IS NULL OR p_in_sortedBy = 'asc') THEN
        v_sortCol := 'trunc('||p_in_sortColName||') asc, is_new_hire asc,by_me_since_last_recog desc,upper(first_name)';
    ELSE
        v_sortCol := 'trunc('||p_in_sortColName||') desc, is_new_hire desc, by_me_since_last_recog asc,upper(first_name) desc';
    END IF;
  END IF;  

  v_query_with := 
     'WITH rec_user as 
       (SELECT au.user_id,
               (SELECT listagg(node_id, '','') within group (order by node_id)     
                  FROM user_node
                 WHERE user_id = p.user_id
                   AND status = 1) node_id,
               ua.country_id,
               c.awardbanq_country_abbrev country_code,
               fnc_cms_asset_code_value(c.cm_asset_code) country_name,
               au.first_name,
               au.last_name,
               pe.position_type,
               p.avatar_small,
               CASE 
		 	     WHEN trunc(sysdate) - NVL(trunc(rct_date),NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created)))) >  '''||v_days_emp_reminder||''' THEN 2  -- overdue --08/06/2019 Bug 79087
                 --WHEN trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) >  '''||v_days_emp_reminder||''' THEN 2  -- overdue       --08/06/2019 Bug 79087	Commented
                 WHEN trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) >  '''||v_days_new_to_regemp||''' AND 
                  --trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) <= '''||v_days_emp_reminder||''' THEN 3  -- Upcoming  --08/06/2019 Bug 79087	Commented
		   	   	 trunc(sysdate) - NVL(trunc(rct_date),NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created)))) <= '''||v_days_emp_reminder||''' THEN 3  -- Upcoming  --08/06/2019 Bug 79087	
                 WHEN trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) <= '''||v_days_new_to_regemp||''' THEN 1 -- NewHire
               END is_new_hire,
               pe.hire_Date,
               au.enrollment_date,
               au.date_created,                     
	       	   gr.rct_date             --08/06/2019 Bug 79087
          FROM application_user au,
               participant p,
               vw_curr_pax_employer pe,
               --gtt_ra_receiver gr,	 --08/06/2019 Bug 79087
               user_address ua,
               country c,
	       	   gtt_ra_receiver_info gr --08/06/2019 Bug 79087
         WHERE au.user_id = p.user_id
           AND au.user_id = gr.user_id
           AND au.user_id = pe.user_id(+)
           AND au.user_id = ua.user_id
           AND ua.country_id = c.country_id
           AND ua.is_primary = 1
           AND au.is_active = 1
           AND p.is_opt_out_of_program = 0 
           AND p.status = ''active''),
  -- Get all rec claim records                      
     rec_pax as
       (SELECT cr.participant_id,
               ci.approval_status_type,
               ci.date_created,
               cr.award_qty,
               c.claim_id
          FROM claim_recipient cr,
               claim_item ci,
               recognition_claim rc,
               claim c,
               rec_user ru
         WHERE ci.claim_item_id = cr.claim_item_id 
           AND ci.claim_id = rc.claim_id
           AND rc.claim_id = c.claim_id
           AND c.submitter_id = '''||p_in_user_id||'''
           AND cr.participant_id = user_id
        ),
  -- Get only pending records    
     rec_pend as 
       (SELECT *
          FROM (SELECT pr.*,
                       rank() over(partition by PR.participant_id order by pr.date_created desc) ranks
                  FROM rec_pax pr
                 WHERE approval_status_type = ''pend''
                   AND NOT EXISTS (SELECT participant_id
                                     FROM rec_pax
                                    WHERE participant_id = pr.participant_id
                                      AND approval_status_type <> ''pend'')
                )
         WHERE ranks = 1) ' ; 
     
  v_spec_query_with := 
      'WITH rec_user as 
        (SELECT au.user_id,
                (SELECT listagg(node_id, '','') within group (order by node_id)     
                   FROM user_node
                  WHERE user_id = p.user_id
                    AND status = 1) node_id,
                ua.country_id,
                c.awardbanq_country_abbrev country_code,
                fnc_cms_asset_code_value(c.cm_asset_code) country_name,
                au.first_name,
                au.last_name,
                pe.position_type,
                p.avatar_small,
                CASE 
                  WHEN trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) >  '''||v_days_emp_reminder||''' THEN 2  -- Overdue
                  WHEN trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) >  '''||v_days_new_to_regemp||''' AND 
                       trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) <= '''||v_days_emp_reminder||''' THEN 3  -- Upcoming
                  WHEN trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) <= '''||v_days_new_to_regemp||''' THEN 1 -- NewHire
                END is_new_hire,
                pe.hire_Date,
                au.enrollment_date,
                au.date_created
           FROM application_user au,
                participant p,
                vw_curr_pax_employer pe,
                gtt_ra_receiver gr,
                user_address ua,
                country c
          WHERE au.user_id = p.user_id
            AND au.user_id = gr.user_id
            AND au.user_id = pe.user_id(+)
            AND au.user_id = ua.user_id
            AND ua.country_id = c.country_id
            AND ua.is_primary = 1 
            AND EXISTS (SELECT user_id
                         FROM audience a,
                              promo_audience pa,
                              participant_audience par,
                              gtt_ra_promotion gm
                        WHERE a.audience_id = pa.audience_id
                          AND par.audience_id = pa.audience_id
                          AND pa.promotion_id = gm.promotion_id
                          AND par.user_id = gr.user_id
                          AND ((orgl_secondary_audience_type = ''specifyaudience'' 
                                AND pa.promo_audience_type = ''SECONDARY'')
                            OR (orgl_secondary_audience_type <> ''specifyaudience'' 
                                AND pa.promo_audience_type = ''PRIMARY'')
                               )
                        UNION
                        SELECT distinct un.user_id
                          FROM user_node un,
                               (SELECT node_id,
                                       level AS node_level
                                  FROM node
                                 WHERE is_deleted = 0
                               CONNECT BY PRIOR node_id = parent_node_id
                                 START WITH node_id IN (SELECT node_id
                                                          FROM user_node
                                                         WHERE user_id = '''||p_in_user_id||''')
                                ) n
                         WHERE un.node_id = n.node_id
                           AND un.user_id = gr.user_id 
                           AND 1 = ANY (SELECT 1
                                          FROM gtt_ra_promotion
                                         WHERE secondary_audience_type = ''activepaxfromprimarynodebelowaudience'')
                        )
               AND au.is_active = 1
               AND p.is_opt_out_of_program = 0 
               AND p.status = ''active''
        ),
      -- Get all rec claim records                      
      rec_pax as
        (SELECT cr.participant_id,
                ci.approval_status_type,
                ci.date_created,
                cr.award_qty,
                c.claim_id
           FROM claim_recipient cr,
                claim_item ci,
                recognition_claim rc,
                claim c,
                rec_user ru
          WHERE ci.claim_item_id = cr.claim_item_id 
            AND ci.claim_id = rc.claim_id
            AND rc.claim_id = c.claim_id
            AND c.submitter_id = '''||p_in_user_id||'''
            AND cr.participant_id = user_id
         ),
      -- Get only pending records    
      rec_pend as 
        (SELECT *
           FROM (SELECT pr.*,
                        rank() over(partition by PR.participant_id order by pr.date_created desc) ranks
                   FROM rec_pax pr
                  WHERE approval_status_type = ''pend''
                    AND NOT EXISTS (SELECT participant_id
                                      FROM rec_pax
                                     WHERE participant_id = pr.participant_id
                                       AND approval_status_type <> ''pend'')
                 )
          WHERE ranks = 1) ';                                                            
     
  v_query_active := 
     'SELECT decode('''||p_in_exclude_upcoming||''', 1,nh_od_cnt,total_records) total_records,
             CASE WHEN nh_cnt > 0 THEN 1 ELSE 0 END is_newhire,
             CASE WHEN od_cnt > 0 THEN 1 ELSE 0 END is_overdue,
             CASE WHEN uc_cnt > 0 THEN 1 ELSE 0 END is_upcoming,
             nh_cnt newhire_count,
             od_cnt overdue_count,
             user_id,
             first_name,
             last_name,
             node_id,
             country_id,
             country_code,
             country_name,
             position_type,
             avatar_small,
             is_new_hire,
             by_oth_date_sent,
             by_oth_since_last_recog,
             by_oth_quantity,
             by_oth_claim_id,
             by_me_date_sent,
             by_me_since_last_recog,
             by_me_quantity, 
             by_me_claim_id,
             approval_status_type,
             CASE WHEN IS_NEW_HIRE = 1 AND by_me_since_last_recog >= 0 AND by_me_since_last_recog < '''||v_color_nh||''' THEN ''gray''
                  WHEN IS_NEW_HIRE = 1 AND by_me_since_last_recog >= '''||v_color_nh||''' AND by_me_since_last_recog < '''||v_color_n2||''' THEN ''orange''
                  WHEN IS_NEW_HIRE = 1 AND by_me_since_last_recog >= '''||v_color_n2||''' THEN ''red''
                  WHEN IS_NEW_HIRE = 2 AND by_me_since_last_recog >= '''||v_days_emp_reminder||''' AND by_me_since_last_recog < '''||v_color_r2||''' THEN ''orange''
                  WHEN IS_NEW_HIRE = 2 AND by_me_since_last_recog >= '''||v_color_r2||''' THEN ''red''
                  WHEN IS_NEW_HIRE = 3 THEN ''gray''
             END colour_grade,
             CASE WHEN by_oth_since_last_recog >= 0 AND by_oth_since_last_recog < '''||v_color_nh||''' THEN ''gray''
                  WHEN by_oth_since_last_recog >= '''||v_color_nh||''' AND by_oth_since_last_recog < '''||v_color_n2||''' THEN ''orange''
                  WHEN by_oth_since_last_recog >= '''||v_color_n2||''' THEN ''red''
                  WHEN by_oth_since_last_recog >= '''||v_days_emp_reminder||''' AND by_oth_since_last_recog < '''||v_color_r2||''' THEN ''orange''
                  WHEN by_oth_since_last_recog >= '''||v_color_r2||''' THEN ''red''
             END colour_grade_other 
       FROM ( ';
        
 IF v_live_cnt_promotion >= 1 THEN
   IF v_cnt_all_active_promo >= 1 THEN  -- START v_cnt_all_active_promo
   
    v_stage := 'Get result for all active promotions';    
    v_query_all_active :=  v_query_with||'  '|| v_query_active||'  '||'
      SELECT ROWNUM RN,RS.* FROM ( 
          SELECT COUNT(user_id) OVER() AS total_records,R.*
           FROM ( 
         SELECT SUM(CASE WHEN is_new_hire IN (1,2) THEN 1 END) over() nh_od_cnt,  -- for nh and od count
             SUM(CASE WHEN is_new_hire = 1 THEN 1 END) over() nh_cnt, 
             SUM(CASE WHEN is_new_hire = 2 THEN 1 END) over() od_cnt, 
             SUM(CASE WHEN is_new_hire = 3 THEN 1 END) over() uc_cnt,
             user_id,
             first_name,
             last_name,
             node_id,
             country_id,
             country_code,
             country_name,
             position_type,
             avatar_small,
             is_new_hire,
             by_oth_date_sent,
             by_oth_since_last_recog,
             by_oth_quantity,
             by_oth_claim_id,
             by_me_date_sent,
             --NVL(by_me_since_last_recog,(trunc(sysdate) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(date_created))))) by_me_since_last_recog,  --08/06/2019 Bug 79087
	         NVL(by_me_since_last_recog,(trunc(sysdate) - NVL(TRUNC(rct_date),NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(date_created)))))) by_me_since_last_recog,  --08/06/2019 Bug 79087
             by_me_quantity,
             by_me_claim_id,
             approval_status_type
        FROM (
           WITH recg_pax as 
           (SELECT user_id,
                    first_name,
                    last_name,
                    node_id,
                    country_id,
                    country_code,
                    country_name,
                    position_type,
                    avatar_small,
                    hire_Date,
                    enrollment_date,
                    rec.date_created,
                    decode(by_me_is_new_hire, null, by_oth_is_new_hire, by_me_is_new_hire) is_new_hire,
                    by_oth_date_sent,
                    by_oth_since_last_recog,
                    by_oth_quantity,
                    by_oth_claim_id,
                    NVL(by_me_date_sent,NVL(p.date_created,rp.date_created)) by_me_date_sent,
                    by_me_since_last_recog,
                    DECODE(NVL(p.approval_status_type,rp.approval_status_type),''pend'',NVL(p.award_qty,rp.award_qty),by_me_quantity) by_me_quantity,
                    DECODE(NVL(p.approval_status_type,rp.approval_status_type),''pend'',NVL(p.claim_id,rp.claim_id),by_me_claim_id) by_me_claim_id,
                    NVL(p.approval_status_type,rp.approval_status_type) approval_status_type,
		    rct_date --08/06/2019 Bug 79087	
               FROM (
                  (SELECT user_id,
                            first_name,
                            last_name,
                            node_id,
                            country_id,
                            country_code,
                            country_name,
                            position_type,
                            avatar_small,
                            hire_Date,
                            enrollment_date,
                            date_created,
                            is_new_hire,
                            date_sent,
                            since_last_recog,
                            quantity,
                            claim_id,
                            by_others,
			    			rct_date    --08/06/2019 Bug 79087
                       FROM (SELECT *  
                               FROM (SELECT gr.user_id,
                                            gr.node_id,
                                            gr.country_id,
                                            gr.country_code,
                                            gr.country_name,
                                            gr.first_name,
                                            gr.last_name,
                                            gr.position_type,
                                            gr.avatar_small,
                                            gr.hire_Date,
                                            gr.enrollment_date,
                                            gr.date_created,
                                            ci.date_approved date_sent,
                                            --trunc(sysdate) - trunc(ci.date_approved) since_last_recog, --08/06/2019 Bug 79087 Commented
					    					trunc(sysdate) - greatest(trunc(rct_date),trunc(ci.date_approved)) since_last_recog, --08/06/2019 Bug 79087
                                            is_new_hire,
                                            a.quantity,
                                            rc.claim_id,
                                            1 by_others,
                                            rank() over(partition by gr.user_id order by ci.date_approved desc,ci.claim_item_id desc) ranks,
					    					rct_date   --08/06/2019 Bug 79087
                                       FROM claim c,
                                            recognition_claim rc,  --to restrict recognisation promotions
                                            claim_item ci,
                                            activity a,
                                            rec_user gr,
                                            promotion p  -- Added to get other than merchandise promotions
                                      WHERE c.claim_id = ci.claim_id
                                        AND c.claim_id = rc.claim_id
                                        AND c.claim_id = a.claim_id
                                        AND c.promotion_id = p.promotion_id
                                        AND p.award_type != ''merchandise''
                                        AND a.user_id = gr.user_id
                                        AND a.is_submitter = 0
                                        AND ci.approval_status_type = ''approv''
                                        AND c.submitter_id <> '''||p_in_user_id||''')
                              WHERE ranks = 1   
                            UNION    
                            SELECT *
                              FROM (SELECT gr.user_id,
                                           gr.node_id,
                                           gr.country_id,
                                           gr.country_code,
                                           gr.country_name,
                                           gr.first_name,
                                           gr.last_name,
                                           gr.position_type,
                                           gr.avatar_small,
                                           gr.hire_Date,
                                           gr.enrollment_date,
                                           gr.date_created,
                                           c.date_created date_sent,
                                           --trunc(sysdate) - trunc(ci.date_approved) since_last_recog,--08/06/2019 Bug 79087 commented
					   					   trunc(sysdate) -greatest(trunc(rct_date),trunc(ci.date_approved)) since_last_recog,--08/06/2019 Bug 79087
                                           --CASE WHEN (trunc(sysdate+1) - trunc(ci.date_approved)) > '''||v_days_emp_reminder||''' THEN 2 -- Overdue				--08/06/2019 Bug 79087 Commented
					   					   CASE WHEN (trunc(sysdate) - greatest(trunc(rct_date),trunc(ci.date_approved))) > '''||v_days_emp_reminder||''' THEN 2 -- Overdue --08/06/2019 Bug 79087
                                           ELSE 3 -- team memmber
                                           END is_new_hire,
                                           a.quantity,
                                           rc.claim_id, 
                                           0 by_others,
                                           rank() over(partition by gr.user_id order by ci.date_approved desc,ci.claim_item_id desc) ranks,
					   rct_date
                                      FROM claim c,
                                           recognition_claim rc,
                                           claim_item ci,
                                           activity a,
                                           rec_user gr,
                                           gtt_ra_promotion gm
                                     WHERE c.claim_id = ci.claim_id
                                       AND c.claim_id = rc.claim_id
                                       AND c.claim_id = a.claim_id
                                       AND c.promotion_id = gm.promotion_id
                                       AND a.user_id = gr.user_id
                                       AND a.is_submitter = 0
                                       AND ci.approval_status_type = ''approv''
                                       AND c.submitter_id = '''||p_in_user_id||''')
                             WHERE ranks = 1))
                    PIVOT
                      (
                       max(date_sent) as date_sent,
                       max(since_last_recog) as since_last_recog,
                       max(quantity)as  quantity,
                       max(claim_id) as claim_id,
                       max(is_new_hire) as is_new_hire
                       FOR by_others IN (1 as  by_oth,0 as by_me)
                       )
                       ) rec,
                     ( SELECT *
                         FROM
                       (SELECT PR.*,
                             RANK () OVER (PARTITION BY PR.participant_id ORDER BY pr.date_created DESC) ranks
                        FROM rec_pax pr
                       --WHERE approval_status_type = ''pend''  --03/29/2019 Bug 78991
                         WHERE participant_id NOT IN (SELECT participant_id
                                                      FROM rec_pend))
                      WHERE ranks = 1 
					  AND approval_status_type = ''pend'' --03/29/2019 Bug 78991
                      ) p,
                      rec_pend rp
                WHERE rec.user_id = p.participant_id(+)
                  AND rec.user_id = rp.participant_id(+))
                SELECT *
                  FROM recg_pax
                   UNION
             SELECT user_id,
                    first_name,
                    last_name,
                    node_id,
                    country_id,
                    country_code,
                    country_name,
                    position_type,
                    avatar_small,
                    hire_Date,
                    enrollment_date,
                    rec.date_created,
                    is_new_hire,
                    null by_oth_date_sent,
                    null by_oth_since_last_recog,
                    null by_oth_quantity,
                    null by_oth_claim_id,
                    p.date_created by_me_date_sent,
                    null by_me_since_last_recog,
                    p.award_qty by_me_quantity,
                    p.claim_id by_me_claim_id,
                    p.approval_status_type,
		    		rct_date 			 --08/06/2019 Bug 79087	
               FROM 
               (SELECT *
                 FROM rec_user 
                WHERE user_id NOT IN (SELECT user_id  --Find pax which are not recog by only recognition type promotions
                                        FROM recg_pax)
                                      ) rec,
                rec_pend p
                WHERE rec.user_id = p.participant_id(+)
          )) R WHERE is_new_hire = NVL('||v_emp_filter||', is_new_hire)
              AND (('||v_no_pend||' = 1 AND approval_status_type IS NULL)
                   OR '||v_no_pend||' = 0)
    ORDER BY '||v_sortCol||' )  RS ) WHERE RN >= ' ||p_in_rownum_start||' AND RN   <= '|| p_in_rownum_end;

   OPEN p_out_user_data FOR 
     v_query_all_active;                           
                      
  ELSE 

    v_stage := 'Get result for specific promotions'; 
    v_query_spec_active :=  v_spec_query_with||'  '|| v_query_active||'  '||'
      SELECT ROWNUM RN,RS.* FROM ( 
         SELECT COUNT(user_id) OVER() AS total_records,R.*
           FROM ( 
         SELECT SUM(CASE WHEN is_new_hire IN (1,2) THEN 1 END) over() nh_od_cnt,  -- for nh and od count
                SUM(CASE WHEN is_new_hire = 1 THEN 1 END) over() nh_cnt, 
                SUM(CASE WHEN is_new_hire = 2 THEN 1 END) over() od_cnt, 
                SUM(CASE WHEN is_new_hire = 3 THEN 1 END) over() uc_cnt,
                user_id,
                first_name,
                last_name,
                node_id,
                country_id,
                country_code,
                country_name,
                position_type,
                avatar_small,
                is_new_hire,
                by_oth_date_sent,
                by_oth_since_last_recog,
                by_oth_quantity,
                by_oth_claim_id,
                by_me_date_sent,
                NVL(by_me_since_last_recog,(trunc(sysdate) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(date_created))))) by_me_since_last_recog,
                by_me_quantity,
                by_me_claim_id,
                approval_status_type,
                by_me_promotion_id
           FROM (
           WITH recg_pax as 
           (SELECT user_id,
                        first_name,
                        last_name,
                        node_id,
                        country_id,
                        country_code,
                        country_name,
                        position_type,
                        avatar_small,
                        hire_Date,
                        enrollment_date,
                        rec.date_created,
                        is_new_hire,
                        by_oth_date_sent,
                        by_oth_since_last_recog,
                        by_oth_quantity,
                        by_oth_claim_id,
                        NVL(by_me_date_sent,NVL(p.date_created,rp.date_created)) by_me_date_sent,
                        by_me_since_last_recog,
                        DECODE(NVL(p.approval_status_type,rp.approval_status_type),''pend'',NVL(p.award_qty,rp.award_qty),by_me_quantity) by_me_quantity,
                        DECODE(NVL(p.approval_status_type,rp.approval_status_type),''pend'',NVL(p.claim_id,rp.claim_id),by_me_claim_id) by_me_claim_id,
                        by_me_promotion_id,  
                        by_oth_promotion_id,
                        NVL(p.approval_status_type,rp.approval_status_type) approval_status_type
                   FROM 
                     ( SELECT user_id,
                            first_name,
                            last_name,
                            node_id,
                            country_id,
                            country_code,
                            country_name,
                            position_type,
                            avatar_small,
                            hire_Date,
                            enrollment_date,
                            date_created,
                            decode(by_me_is_new_hire, null, by_oth_is_new_hire, by_me_is_new_hire) is_new_hire,
                            by_oth_date_sent,
                            by_oth_since_last_recog,
                            by_oth_quantity,
                            by_oth_claim_id,
                            by_me_date_sent,
                            by_me_since_last_recog,
                            by_me_quantity,
                            by_me_claim_id,
                            by_me_promotion_id,
                            by_oth_promotion_id
                       FROM (SELECT user_id,
                                    first_name,
                                    last_name,
                                    node_id,
                                    country_id,
                                    country_code,
                                    country_name,
                                    position_type,
                                    avatar_small,
                                    hire_Date,
                                    enrollment_date,
                                    date_created,
                                    is_new_hire,
                                    date_sent,
                                    since_last_recog,
                                    quantity,
                                    claim_id,
                                    promotion_id,
                                    by_others
                               FROM (SELECT * 
                                       FROM (SELECT gr.user_id,
                                                    gr.node_id,
                                                    gr.country_id,
                                                    gr.country_code,
                                                    gr.country_name,
                                                    gr.first_name,
                                                    gr.last_name,
                                                    gr.position_type,
                                                    gr.avatar_small,
                                                    gr.hire_Date,
                                                    gr.enrollment_date,
                                                    gr.date_created,
                                                    ci.date_approved date_sent,
                                                    trunc(sysdate) - trunc(ci.date_approved) since_last_recog,
                                                    is_new_hire,
                                                    a.quantity,
                                                    rc.claim_id,
                                                    c.promotion_id,
                                                    1 by_others,
                                                    rank() over(partition by gr.user_id order by ci.date_approved desc,ci.claim_item_id desc) ranks
                                               FROM claim c,
                                                    recognition_claim rc,
                                                    claim_item ci,
                                                    activity a,
                                                    rec_user gr
                                             WHERE c.claim_id = ci.claim_id
                                               AND c.claim_id = rc.claim_id
                                               AND c.claim_id = a.claim_id
                                               AND a.user_id = gr.user_id
                                               AND a.is_submitter = 0
                                               AND ci.approval_status_type = ''approv''
                                               AND c.submitter_id <> '''||p_in_user_id||''')
                                      WHERE ranks = 1   
                                    UNION    
                                    SELECT *
                                      FROM (SELECT gr.user_id,
                                                   gr.node_id,
                                                   gr.country_id,
                                                   gr.country_code,
                                                   gr.country_name,
                                                   gr.first_name,
                                                   gr.last_name,
                                                   gr.position_type,
                                                   gr.avatar_small,
                                                   gr.hire_Date,
                                                   gr.enrollment_date,
                                                   gr.date_created,
                                                   c.date_created date_sent,
                                                   trunc(sysdate) - trunc(ci.date_approved) since_last_recog,
                                                   CASE WHEN (trunc(sysdate+1) - trunc(ci.date_approved)) > '''||v_days_emp_reminder||''' THEN 2 -- Overdue
                                                   ELSE 3 -- team memmber
                                                   END is_new_hire,
                                                   a.quantity,
                                                   rc.claim_id, 
                                                   c.promotion_id,
                                                   0 by_others,
                                                   rank() over(partition by gr.user_id order by ci.date_approved desc,ci.claim_item_id desc) ranks
                                              FROM claim c,
                                                   recognition_claim rc,
                                                   claim_item ci,
                                                   activity a,
                                                   rec_user gr,
                                                   gtt_ra_promotion gm
                                             WHERE c.claim_id = ci.claim_id
                                               AND c.claim_id = rc.claim_id
                                               AND c.claim_id = a.claim_id
                                               AND c.promotion_id = gm.promotion_id
                                               AND a.user_id = gr.user_id
                                               AND a.is_submitter = 0
                                               AND ci.approval_status_type = ''approv''
                                               AND c.submitter_id = '''||p_in_user_id||''')
                                     WHERE ranks = 1
                                    )
                            )
                            PIVOT
                             (
                              max(date_sent) as date_sent,
                              max(since_last_recog) as since_last_recog,
                              max(promotion_id) as promotion_id,
                              max(quantity)as  quantity,
                              max(claim_id) as claim_id,
                              max(is_new_hire) as is_new_hire
                              FOR by_others IN (1 as  by_oth,0 as by_me)
                              )
                           ) rec,
                      (SELECT *
                         FROM
                       (SELECT PR.*,
                             RANK () OVER (PARTITION BY PR.participant_id ORDER BY pr.date_created DESC) ranks
                        FROM rec_pax pr
                       WHERE approval_status_type = ''pend''
                         AND participant_id NOT IN (SELECT participant_id
                                                      FROM rec_pend))
                      WHERE ranks = 1 
                      ) p,
                      rec_pend rp
                WHERE rec.user_id = p.participant_id(+)
                  AND rec.user_id = rp.participant_id(+))
                  SELECT *
                    FROM recg_pax
                     UNION
                     SELECT user_id,
                            first_name,
                            last_name,
                            node_id,
                            country_id,
                            country_code,
                            country_name,
                            position_type,
                            avatar_small,
                            hire_Date,
                            enrollment_date,
                            rec.date_created,
                            is_new_hire,
                            null by_oth_date_sent,
                            null by_oth_since_last_recog,
                            null by_oth_quantity,
                            null by_oth_claim_id,
                            p.date_created by_me_date_sent,
                            null by_me_since_last_recog,
                            p.award_qty by_me_quantity,
                            p.claim_id by_me_claim_id,
                            null by_me_promotion_id,
                            null by_oth_promotion_id,
                            approval_status_type
                       FROM 
                         (SELECT *
                           FROM (SELECT au.user_id,
                                    (SELECT listagg(node_id, '','') within group (order by node_id)     
                                       FROM user_node
                                      WHERE user_id = p.user_id
                                        AND status = 1) node_id,
                                    ua.country_id,
                                    c.awardbanq_country_abbrev country_code,
                                    fnc_cms_asset_code_value(c.cm_asset_code) country_name,
                                    au.first_name,
                                    au.last_name,
                                    pe.position_type,
                                    p.avatar_small,
                                    CASE 
                                      WHEN trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) >  '''||v_days_emp_reminder||''' THEN 2  -- Overdue
                                      WHEN trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) >  '''||v_days_new_to_regemp||''' AND 
                                           trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) <= '''||v_days_emp_reminder||''' THEN 3  -- Upcoming
                                      WHEN trunc(sysdate+1) - NVL(trunc(hire_Date),NVL(trunc(enrollment_date),trunc(au.date_created))) <= '''||v_days_new_to_regemp||''' THEN 1 -- NewHire
                                    END is_new_hire,
                                    pe.hire_Date,
                                    au.enrollment_date,
                                    au.date_created
                               FROM application_user au,
                                    participant p,
                                    vw_curr_pax_employer pe,
                                    gtt_ra_receiver gr,
                                    user_address ua,
                                    country c
                              WHERE au.user_id = p.user_id
                                AND au.user_id = gr.user_id
                                AND au.user_id = pe.user_id(+)
                                AND au.user_id = ua.user_id
                                AND ua.country_id = c.country_id
                                AND ua.is_primary = 1
                                AND EXISTS (SELECT user_id
                                              FROM audience a,
                                                   promo_audience pa,
                                                   participant_audience par,
                                                   gtt_ra_promotion gm
                                             WHERE a.audience_id = pa.audience_id
                                               AND par.audience_id = pa.audience_id
                                               AND pa.promotion_id = gm.promotion_id
                                               AND par.user_id = gr.user_id
                                               AND gm.promotion_status = ''live''
                                               AND ((orgl_secondary_audience_type = ''specifyaudience'' 
                                                       AND pa.promo_audience_type = ''SECONDARY'')
                                                    OR (orgl_secondary_audience_type <> ''specifyaudience'' 
                                                        AND pa.promo_audience_type = ''PRIMARY''))
                                            UNION
                                           SELECT distinct un.user_id
                                             FROM user_node un,
                                                  (SELECT node_id,
                                                          level AS node_level
                                                     FROM node
                                                    WHERE is_deleted = 0
                                               CONNECT BY PRIOR node_id = parent_node_id
                                                 START WITH node_id IN (SELECT node_id
                                                                          FROM user_node
                                                                         WHERE user_id = '''||p_in_user_id||''')) n
                                            WHERE un.node_id = n.node_id
                                              AND un.user_id = gr.user_id 
                                              AND 1 = ANY (SELECT 1
                                                             FROM gtt_ra_promotion
                                                            WHERE secondary_audience_type = ''activepaxfromprimarynodebelowaudience'')
                                               ) --if pax blongs to exp promo n never recog 
                                 AND au.is_active = 1
                                 AND p.is_opt_out_of_program = 0 
                                 AND p.status = ''active'') rc
                    WHERE user_id NOT IN (SELECT user_id  --Find pax which are not recog by only recognition type promotions
                                            FROM recg_pax)
                                           ) rec,
                          rec_pend p
                    WHERE rec.user_id = p.participant_id(+)
                        ) -- ))R -- Bug 79050 05/03/2019 
          WHERE (by_me_promotion_id IS NULL   
              OR by_me_promotion_id IN (SELECT promotion_id
                                          FROM gtt_ra_promotion
                                         WHERE promotion_status = ''live''))
																) R  -- Bug 79050 05/03/2019
           Where  is_new_hire = NVL('||v_emp_filter||', is_new_hire) -- 05/03/2019 -- change AND to WHERE
           AND (('||v_no_pend||' = 1 AND approval_status_type IS NULL)
                   OR '||v_no_pend||' = 0)                                      
ORDER BY '||v_sortCol||' )  RS ) WHERE RN >= ' ||p_in_rownum_start||' AND RN   <= '|| p_in_rownum_end;                       

     OPEN p_out_user_data FOR 
       v_query_spec_active;      
    
  END IF;  -- END v_cnt_all_active_promo
  
  ELSE 
  OPEN p_out_user_data FOR 
 SELECT NULL total_records,
       NULL is_newhire,
       NULL is_overdue,
       NULL is_upcoming, 
       NULL user_id,
       NULL first_name,
       NULL last_name,
       NULL node_id,
       NULL country_id,
       NULL country_code,
       NULL country_name,
       NULL position_type,
       NULL avatar_small,
       NULL is_new_hire,
       NULL by_oth_date_sent,
       NULL by_oth_since_last_recog,
       NULL by_oth_quantity,
       NULL by_oth_claim_id,
       NULL by_me_date_sent,
       NULL by_me_since_last_recog,
       NULL by_me_quantity, 
       NULL by_me_claim_id,
       NULL approval_status_type,
       NULL colour_grade,
       NULL colour_grade_other 
 FROM dual;
  
  END IF;   --END v_live_cnt_promotion  

  COMMIT;
  prc_execution_log_entry(c_process_name,1,'INFO','Process Completed '||v_fetch_count,null);
  p_out_return_code:=0;
  
EXCEPTION
  WHEN no_user_id THEN
    p_out_return_code :=  99 ;
    prc_execution_log_entry(c_process_name,1,'ERROR',v_stage||' - '||SQLERRM,null);
    OPEN p_out_user_data FOR SELECT NULL FROM dual  ;
    
  WHEN OTHERS THEN
    ROLLBACK;
    p_out_return_code :=  99 ;
    prc_execution_log_entry(c_process_name,1,'ERROR',v_stage||' - '||SQLERRM,null);
    OPEN p_out_user_data FOR SELECT NULL FROM dual  ;
END;
/