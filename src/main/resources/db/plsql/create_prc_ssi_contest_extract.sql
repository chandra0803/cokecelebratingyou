CREATE OR REPLACE PROCEDURE prc_ssi_contest_extract
 (
      p_in_ssi_contest_id     IN     NUMBER,
      p_out_return_code       OUT    NUMBER,
      p_out_ref_cursor        OUT    SYS_REFCURSOR
  )

   /***********************************************************************************
      Purpose:  Procedure for SSI Extracts

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
    Suresh J            11/17/2016       Initial Version
  ************************************************************************************/

      IS

      -- constants
      c_delimiter       constant  varchar2(1) := ',' ;
      c2_delimiter      constant  varchar2(1) := '"' ;

      --Contest Types
      c_award_them_now   constant number := 1;
      c_do_this_get_that constant number := 2;
      c_objectives       constant number := 4;
      c_stack_rank       constant number := 8;
      c_step_it_up       constant number := 16;

      v_out_contest_type          number(18);
      v_activity_measure_type     varchar2(100);
      v_include_stack_rank        number(1);
      v_include_bonus             number(1);
      v_status                    varchar2(100);
      

      BEGIN

      SELECT contest_type,
             activity_measure_type,
             include_stack_rank,
             include_bonus,
             status
      INTO   v_out_contest_type,
             v_activity_measure_type,
             v_include_stack_rank,
             v_include_bonus,
             v_status
      FROM ssi_contest 
      WHERE ssi_contest_id = p_in_ssi_contest_id;


      IF v_out_contest_type = c_objectives THEN
             OPEN p_out_ref_cursor FOR
      SELECT textline FROM (
      SELECT 1,c2_delimiter||'User Name'          ||c2_delimiter||c_delimiter|| 
               c2_delimiter||'First Name'         ||c2_delimiter||c_delimiter||
               c2_delimiter||'Last Name'          ||c2_delimiter||c_delimiter||
               c2_delimiter||'Role'               ||c2_delimiter||c_delimiter||
               c2_delimiter||'Objective Description'   ||c2_delimiter||c_delimiter||
               c2_delimiter||'Objective Amount'   ||c2_delimiter||c_delimiter||
               c2_delimiter||'Objective Payout'   ||c2_delimiter||c_delimiter||
               c2_delimiter||'Other Payout Description' ||c2_delimiter||c_delimiter||
               c2_delimiter||'Other Value' ||c2_delimiter||c_delimiter||
               c2_delimiter||'Bonus For Every'       ||c2_delimiter||c_delimiter||
               c2_delimiter||'Bonus Payout'          ||c2_delimiter||c_delimiter||
               c2_delimiter||'Bonus Cap'    ||c2_delimiter                                                                                            
               AS Textline
      FROM dual
      UNION  ALL
      SELECT
        (ROWNUM+1),
        c2_delimiter||user_name             ||c2_delimiter||c_delimiter||
        c2_delimiter||first_name            ||c2_delimiter||c_delimiter||
        c2_delimiter||last_name             ||c2_delimiter||c_delimiter||        
        c2_delimiter||role                  ||c2_delimiter||c_delimiter||
        c2_delimiter||activity_description  ||c2_delimiter||c_delimiter||
        c2_delimiter||objective_amount      ||c2_delimiter||c_delimiter||
        c2_delimiter||objective_payout      ||c2_delimiter||c_delimiter||
        c2_delimiter||payout_description    ||c2_delimiter||c_delimiter||
        c2_delimiter||payout_value          ||c2_delimiter||c_delimiter||
        c2_delimiter||bonus_for_every          ||c2_delimiter||c_delimiter||
        c2_delimiter||bonus_payout   ||c2_delimiter||c_delimiter||
        c2_delimiter||objective_bonus_cap       ||c2_delimiter       
        AS Textline
        FROM 
        (SELECT first_name,
                last_name,
                user_name,
                activity_description,
                CASE WHEN v_activity_measure_type = 'currency' 
                     THEN trim(to_char(objective_amount,'999999999D99')) 
                ELSE trim(to_char(objective_amount,'999999999D9999')) 
                END objective_amount, 
                CASE WHEN v_activity_measure_type = 'currency' 
                     THEN trim(to_char(activity_amt,'999999999D99')) 
                ELSE trim(to_char(activity_amt,'999999999D9999')) 
                END activity_amt,  
                payout_description,
                Payout_Value,
                objective_bonus_cap,
                objective_payout,
                bonus_payout,
                bonus_for_every,
                CASE WHEN bonus_payout IS NOT NULL
                     THEN objective_payout + bonus_payout
                ELSE objective_payout
                END potential_payout,
                role
        FROM 
        (
         SELECT  au.first_name,
                 au.last_name,
                 au.user_name,
                 NVL(sc.activity_description,scp.activity_description) as activity_description,  
                 scp.objective_amount,
                 scpp.activity_amt,
                 scp.objective_payout_description as payout_description,
                 scp.objective_bonus_cap,
                 scp.objective_bonus_increment as bonus_for_every,
                 CASE WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                        ELSE
                        NULL
                 END Payout_Value,
                 CASE WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                   ELSE NULL
                 END objective_payout,
                  CASE WHEN
                    CASE
                       WHEN scpp.activity_amt > scp.objective_amount
                       THEN
                              FLOOR ((scpp.activity_amt - scp.objective_amount)
                            / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                       ELSE
                          NULL
                    END  > objective_bonus_cap then objective_bonus_cap
                  ELSE CASE
                       WHEN scpp.activity_amt > scp.objective_amount
                       THEN
                               FLOOR ((scpp.activity_amt - scp.objective_amount)
                            / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                       ELSE
                          NULL
                END END AS bonus_payout,
                'participant' AS role
        FROM
                     ssi_contest sc,
                     ssi_contest_participant scp,
                     ssi_contest_pax_progress scpp,
                     application_user au
        WHERE        sc.ssi_contest_id      = p_in_ssi_contest_id
                     and scp.user_id        = au.user_id
                     and au.is_active       = 1
                     and sc.ssi_contest_id  = scp.ssi_contest_id
                     and scp.ssi_contest_id = scpp.ssi_contest_id (+)
                     and scp.user_id        = scpp.user_id (+)
        UNION ALL  --Manager
         SELECT  au.first_name,
                 au.last_name,
                 au.user_name,
                 NVL(sc.activity_description,scp.activity_description) as activity_description,  
                 scp.objective_amount,
                 scpp.activity_amt,
                 scp.objective_payout_description as payout_description,
                 scp.objective_bonus_cap,
                 scp.objective_bonus_increment as bonus_for_every,
                 CASE WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                        ELSE
                        NULL
                 END Payout_Value,
                 CASE WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                   ELSE NULL
                 END objective_payout,
                  CASE WHEN
                    CASE
                       WHEN scpp.activity_amt > scp.objective_amount
                       THEN
                              FLOOR ((scpp.activity_amt - scp.objective_amount)
                            / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                       ELSE
                          NULL
                    END  > objective_bonus_cap then objective_bonus_cap
                  ELSE CASE
                       WHEN scpp.activity_amt > scp.objective_amount
                       THEN
                               FLOOR ((scpp.activity_amt - scp.objective_amount)
                            / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                       ELSE
                          NULL
                END END AS bonus_payout,
                'manager' AS role
        FROM
                     ssi_contest sc,
                     ssi_contest_participant scp,
                     ssi_contest_pax_progress scpp,
                     ssi_contest_manager sm,
                     application_user au
        WHERE        sc.ssi_contest_id      = p_in_ssi_contest_id
                     and sc.ssi_contest_id  = sm.ssi_contest_id
                     and sm.user_id         = au.user_id
                     and au.is_active       = 1
                     and sm.ssi_contest_id  = scp.ssi_contest_id (+)
                     and sm.user_id         = scp.user_id (+)
                     and sm.ssi_contest_id  = scpp.ssi_contest_id (+)
                     and sm.user_id         = scpp.user_id (+)
        UNION ALL   --Superviewer
         SELECT  au.first_name,
                 au.last_name,
                 au.user_name,
                 NVL(sc.activity_description,scp.activity_description) as activity_description,  
                 scp.objective_amount,
                 scpp.activity_amt,
                 scp.objective_payout_description as payout_description,
                 scp.objective_bonus_cap,
                 scp.objective_bonus_increment as bonus_for_every,
                 CASE WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                        ELSE
                        NULL
                 END Payout_Value,
                 CASE WHEN scpp.activity_amt >= scp.objective_amount THEN objective_payout
                   ELSE NULL
                 END objective_payout,
                  CASE WHEN
                    CASE
                       WHEN scpp.activity_amt > scp.objective_amount
                       THEN
                              FLOOR ((scpp.activity_amt - scp.objective_amount)
                            / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                       ELSE
                          NULL
                    END  > objective_bonus_cap then objective_bonus_cap
                  ELSE CASE
                       WHEN scpp.activity_amt > scp.objective_amount
                       THEN
                               FLOOR ((scpp.activity_amt - scp.objective_amount)
                            / scp.objective_bonus_increment ) * scp.objective_bonus_payout
                       ELSE
                          NULL
                END END AS bonus_payout,
                'superviewer' AS role 
        FROM
                     ssi_contest sc,
                     ssi_contest_participant scp,
                     ssi_contest_pax_progress scpp,
                     ssi_contest_superviewer sm,
                     application_user au
        WHERE        sc.ssi_contest_id      = p_in_ssi_contest_id
                     and sc.ssi_contest_id  = sm.ssi_contest_id
                     and sm.user_id         = au.user_id
                     and au.is_active       = 1
                     and sm.ssi_contest_id  = scp.ssi_contest_id (+)
                     and sm.user_id         = scp.user_id (+)
                     and sm.ssi_contest_id  = scpp.ssi_contest_id (+)
                     and sm.user_id         = scpp.user_id (+)
        ORDER BY last_name)
        ));

      ELSIF v_out_contest_type = c_do_this_get_that THEN
              OPEN p_out_ref_cursor  FOR
              SELECT textline FROM (
              SELECT 1,c2_delimiter||'User Name'           ||c2_delimiter||c_delimiter||
                       c2_delimiter||'First Name'          ||c2_delimiter||c_delimiter||
                       c2_delimiter||'Last Name'           ||c2_delimiter||c_delimiter||
                       c2_delimiter||'Role'                ||c2_delimiter                                                                                            
                       AS Textline
              FROM dual
              UNION  ALL
              SELECT
                (ROWNUM+1),
                c2_delimiter||user_name             ||c2_delimiter||c_delimiter||
                c2_delimiter||first_name            ||c2_delimiter||c_delimiter||
                c2_delimiter||last_name             ||c2_delimiter||c_delimiter||
                c2_delimiter||role                  ||c2_delimiter        
                AS Textline
                FROM
                (select au.first_name,
                        au.last_name,
                        au.user_name,
                        role
                FROM  (
                SELECT     sc.ssi_contest_id,                           
                           scp.user_id,                         
                           'participant' role
                      FROM ssi_contest sc,
                           ssi_contest_participant scp
                     WHERE
                           sc.ssi_contest_id        = p_in_ssi_contest_id and
                           sc.ssi_contest_id        = scp.ssi_contest_id                           
                UNION ALL --Manager
                SELECT     sc.ssi_contest_id,                           
                           sm.user_id,
                           'manager' role
                      FROM ssi_contest sc, 
                           ssi_contest_manager sm                          
                     WHERE
                           sc.ssi_contest_id        = p_in_ssi_contest_id and
                           sc.ssi_contest_id        = sm.ssi_contest_id
                UNION ALL  --Superviewer
                SELECT     sc.ssi_contest_id,
                           sm.user_id,
                           'superviewer' role
                      FROM ssi_contest sc, 
                           ssi_contest_superviewer sm                          
                     WHERE
                           sc.ssi_contest_id        = p_in_ssi_contest_id and
                           sc.ssi_contest_id        = sm.ssi_contest_id
                          ) sc,
                             application_user au
                      where   sc.user_id = au.user_id
                             and au.is_active = 1
                          ORDER BY au.last_name
                             ));

      ELSIF v_out_contest_type = c_step_it_up THEN
            OPEN p_out_ref_cursor FOR
              SELECT textline FROM (
              SELECT 1,c2_delimiter||'User Name'           ||c2_delimiter||c_delimiter|| 
                       c2_delimiter||'First Name'          ||c2_delimiter||c_delimiter||
                       c2_delimiter||'Last Name'           ||c2_delimiter||c_delimiter||                       
                       c2_delimiter||'Role'                ||c2_delimiter||c_delimiter||
                       c2_delimiter||'Baseline Amount'     ||c2_delimiter    
                       AS Textline
             FROM dual
             UNION  ALL
             SELECT
                (ROWNUM+1),
                c2_delimiter||user_name             ||c2_delimiter||c_delimiter||
                c2_delimiter||first_name            ||c2_delimiter||c_delimiter||
                c2_delimiter||last_name             ||c2_delimiter||c_delimiter||
                c2_delimiter||role                  ||c2_delimiter||c_delimiter||                
                c2_delimiter||siu_baseline_amount   ||c2_delimiter
                AS Textline
                FROM
                (     SELECT    au.first_name,
                                au.last_name,
                                au.user_name,
                                siu_baseline_amount,
                                role
                                FROM (
                        SELECT scp.user_id,
                               scp.siu_baseline_amount,
                               'participant' role 
                              FROM ssi_contest_participant scp
                             WHERE     scp.ssi_contest_id  = p_in_ssi_contest_id
                        UNION ALL  --Manager
                        SELECT sm.user_id,
                               NULL siu_baseline_amount,
                               'manager' role
                              FROM ssi_contest_manager sm
                             WHERE     sm.ssi_contest_id = p_in_ssi_contest_id                                 
                        UNION ALL   --Superviewer
                        SELECT sm.user_id,
                               NULL siu_baseline_amount,
                               'superviewer' role  
                              FROM ssi_contest sc,
                                   ssi_contest_superviewer sm
                             WHERE     sm.ssi_contest_id = p_in_ssi_contest_id
                               ) sc, 
                               application_user au
                               WHERE sc.user_id = au.user_id
                               AND au.is_active = 1
                    ORDER BY au.last_name
                               ));

      ELSIF v_out_contest_type = c_stack_rank THEN    
            OPEN p_out_ref_cursor FOR
              SELECT textline FROM (
              SELECT 1,c2_delimiter||'User Name'           ||c2_delimiter||c_delimiter|| 
                       c2_delimiter||'First Name'          ||c2_delimiter||c_delimiter||
                       c2_delimiter||'Last Name'           ||c2_delimiter||c_delimiter||                       
                       c2_delimiter||'Role'                ||c2_delimiter    
                       AS Textline
             FROM dual
             UNION  ALL
             SELECT
                (ROWNUM+1),
                c2_delimiter||user_name             ||c2_delimiter||c_delimiter||  
                c2_delimiter||first_name            ||c2_delimiter||c_delimiter||
                c2_delimiter||last_name             ||c2_delimiter||c_delimiter||
                c2_delimiter||role                  ||c2_delimiter
                AS Textline
                FROM
                (
                  SELECT    au.user_id,
                               au.last_name,
                               au.first_name,
                               au.user_name,
                               'participant' AS role
                FROM ssi_contest_participant sp,
                     application_user au                                                 
                WHERE
                     sp.ssi_contest_id = p_in_ssi_contest_id
                  AND sp.user_id = au.user_id
                  AND au.is_active = 1  
                 UNION ALL  --Manager
                  SELECT    au.user_id,
                               au.last_name,
                               au.first_name,
                               au.user_name,
                       'manager' AS role
                FROM ssi_contest_manager sm,
                     application_user au                              
                WHERE
                     sm.ssi_contest_id = p_in_ssi_contest_id
                 AND sm.user_id = au.user_id
                 AND au.is_active = 1     
                 UNION ALL   --Superviewer
                  SELECT    au.user_id,
                               au.last_name,
                               au.first_name,
                               au.user_name,                                
                       'superviewer' AS role
                FROM application_user au,
                     ssi_contest_superviewer sm                              
                WHERE
                     sm.ssi_contest_id = p_in_ssi_contest_id
                 AND sm.user_id        = au.user_id
                 AND au.is_active      = 1      
                 ));

      ELSIF v_out_contest_type = c_award_them_now THEN    
            OPEN p_out_ref_cursor FOR
              SELECT textline FROM (
              SELECT 1,c2_delimiter||'User Name'           ||c2_delimiter||c_delimiter||
                       c2_delimiter||'First Name'          ||c2_delimiter||c_delimiter||
                       c2_delimiter||'Last Name'           ||c2_delimiter||c_delimiter||                        
                       c2_delimiter||'Role'                ||c2_delimiter||c_delimiter||
                       c2_delimiter||'Activity Description'||c2_delimiter||c_delimiter||
                       c2_delimiter||'Activity Amount'     ||c2_delimiter||c_delimiter||
                       c2_delimiter||'Payout Amount'       ||c2_delimiter||c_delimiter||     
                       c2_delimiter||'Payout Description'       ||c2_delimiter||c_delimiter||                       
                       c2_delimiter||'Value'  ||c2_delimiter   
                       AS Textline
             FROM dual
             UNION  ALL
             SELECT
                (ROWNUM+1),
                c2_delimiter||user_name             ||c2_delimiter||c_delimiter|| 
                c2_delimiter||first_name            ||c2_delimiter||c_delimiter||
                c2_delimiter||last_name             ||c2_delimiter||c_delimiter||  
                c2_delimiter||role                  ||c2_delimiter||c_delimiter|| 
                c2_delimiter||activity_description  ||c2_delimiter||c_delimiter||                            
                c2_delimiter||activity_amt          ||c2_delimiter||c_delimiter||  
                c2_delimiter||total_payout          ||c2_delimiter||c_delimiter||
                c2_delimiter||payout_description          ||c2_delimiter||c_delimiter||                
                c2_delimiter||NULL    ||c2_delimiter
                AS Textline
                FROM
                (
                  SELECT    au.user_id,
                            au.last_name,
                            au.first_name,
                            au.user_name,
                            CASE 
                                WHEN v_activity_measure_type = 'currency' 
                                        THEN trim(to_char(scpp.activity_amt,'999999999D99')) 
                                ELSE trim(to_char(scpp.activity_amt,'999999999D9999')) 
                            END activity_amt,                  
                            CASE WHEN v_status = 'finalize_results'
                              THEN NVL(ssip.payout_amount,0)   
                                   ELSE scsrp.payout_amount 
                            END  as total_payout,   
                            CASE WHEN v_status = 'finalize_results'
                              THEN CASE WHEN NVL(ssip.payout_amount,0) = 0 THEN NULL 
                                        ELSE scsrp.payout_desc 
                                   END
                             ELSE scsrp.payout_desc 
                            END  AS payout_description,  
                            sp.activity_description,
                            'participant' AS role
                FROM ssi_contest_participant sp,
                     ssi_contest_pax_progress scpp,
                     ssi_contest_sr_payout scsrp,
                     application_user au,
                     ssi_contest sc,
                     ssi_contest_pax_payout ssip                              
                WHERE
                     sc.ssi_contest_id = p_in_ssi_contest_id
                 AND sc.ssi_contest_id = sp.ssi_contest_id                  
                 AND sc.ssi_contest_id = scsrp.ssi_contest_id
                 AND sp.user_id        = au.user_id
                 AND au.is_active      = 1
                 AND sp.ssi_contest_id = scpp.ssi_contest_id (+)    
                 AND sp.user_id        = scpp.user_id (+)    
                 AND sp.ssi_contest_id = ssip.ssi_contest_id (+)   
                 AND sp.user_id        = ssip.user_id (+)       
                 UNION ALL  --Manager
                 SELECT     au.user_id,
                            au.last_name,
                            au.first_name,
                            au.user_name,
                            NULL activity_amt,                  
                            NULL total_payout,   
                            NULL payout_description,
                       NULL activity_description,
                       'manager' AS role
                FROM application_user au,                     
                     ssi_contest_manager sm                              
                WHERE
                     sm.ssi_contest_id = p_in_ssi_contest_id
                 AND sm.user_id        = au.user_id
                 AND au.is_active      = 1      
                 UNION ALL   --Superviewer
                 SELECT    au.user_id,
                           au.last_name,
                           au.first_name,
                           au.user_name,
                           NULL activity_amt,                  
                            NULL total_payout,   
                            NULL payout_description,
                       NULL activity_description,
                           'superviewer' AS role                       
                FROM application_user au,
                     ssi_contest_superviewer sm                              
                WHERE
                     sm.ssi_contest_id = p_in_ssi_contest_id
                 AND sm.user_id        = au.user_id
                 AND au.is_active      = 1      
                 ));

        END IF;  -- End of Contest Type Extract SQL Condition

    p_out_return_code :=00 ;

     EXCEPTION WHEN OTHERS THEN
       p_out_return_code :=99;
       prc_execution_log_entry ('prc_ssi_contest_extract',1,'ERROR','Process failed for ssi_contest_id:  '|| p_in_ssi_contest_id|| ' '|| SQLERRM,NULL);
      END prc_ssi_contest_extract;
/
