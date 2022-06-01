CREATE OR REPLACE PROCEDURE prc_refresh_t_and_c_audience
 (pi_promotion_id IN  audience.audience_id%TYPE,
  po_returncode  OUT NUMBER)
IS 

/*----------------------------------------------------------------------------

  Purpose: Assign audience to Participant based on audience table.This process
          will add/remove records to a GTT table used for budget file loads. This is called only when the 
          system variable termsandconditions.used =1

  Person        Date         Comments
  -----------   -----------  ---------------------------------------------------
  Ravi Dhanekula  04/28/2017    Initial creation   
  ---------------------------------------------------------------------------*/

     
 CURSOR cur_audience (vi_promotion_id audience.audience_id%TYPE) IS
     SELECT A.audience_id
       FROM promo_audience pa,audience a
      WHERE a.list_type   = 'criteria'
         AND a.audience_id = pa.audience_id
         AND pa.promotion_id = vi_promotion_id
         AND pa.promo_audience_type = 'PRIMARY'
      ORDER BY audience_id;

  --Audience criteria cursor
  CURSOR cur_cri_aud (vi_audience_id audience_criteria.audience_id%TYPE) IS
    SELECT 
     audience_criteria_id,
  audience_id,
  first_name,
  last_name,
  employer_id,
  position_type,
  department_type,
  language_id,
  node_id,
  replace(node_name, '''', '''''')node_name,
  node_type_id,
  user_node_role,
  include_child_nodes,
  exclude_country_id,
  exclude_node_id,
  exclude_node_name,
  exclude_node_type_id,
  exclude_user_node_role,
  exclude_include_child_nodes,
  exclude_position_type,
  exclude_department_type,
  created_by,
  date_created,
  version,
  country_id
      FROM audience_criteria
     WHERE audience_id = vi_audience_id
     ORDER BY audience_criteria_id;


  --Audience node/user characteristic cursor
  CURSOR cur_char (vi_characteristic_type  characteristic.characteristic_type%TYPE,
                   vi_audience_criteria_id audience_criteria_char.audience_criteria_id%TYPE,
                   vi_search_type          audience_criteria_char.search_type%TYPE ) IS
    SELECT ac.characteristic_id,
           lower(REPLACE(REPLACE(criteria_characteristic_value,'(') ,')')) criteria_characteristic_value
      FROM audience_criteria_char ac,
           characteristic c
     WHERE ac.characteristic_id    = c.characteristic_id
       AND c.characteristic_type   = vi_characteristic_type 
       AND ac.audience_criteria_id = vi_audience_criteria_id
       AND ac.search_type          = vi_search_type;

  --Exception
  e_exit_pgm                EXCEPTION;
  e_exit_pgm_lck            EXCEPTION;

  --Execution log variables
  C_process_name            execution_log.process_name%TYPE  := 'prc_refresh_criteria_audience';
  C_release_level           execution_log.release_level%TYPE := '1';
  C_severity_i              execution_log.severity%TYPE      := 'INFO';
  C_severity_e              execution_log.severity%TYPE      := 'ERROR';
  v_log_msg                 execution_log.text_line%TYPE;
  
  --Procedure variables
  v_stage                   VARCHAR2(500);
  v_audience_id             audience.audience_id%TYPE;

  v_sql                     CLOB;
  v_sql_emp                 VARCHAR2(5000);
  v_sql_lang                VARCHAR2(5000);
  v_sql_node                VARCHAR2(5000);
  v_sql_nc                  VARCHAR2(5000);
  v_sql_nc_str              VARCHAR2(5000);
  v_sql_cntry               VARCHAR2(5000);
  v_sql_char                VARCHAR2(5000);
  v_sql_char_str            VARCHAR2(5000);  
  v_rec_cnt                 NUMBER := 0;
  v_sql_all                 VARCHAR2(5000);
  
  v_employer_id             VARCHAR2(40);
  v_position_type           participant_employer.position_type%TYPE; 
  v_dep_type                participant_employer.department_type%TYPE;
  v_node_id                 VARCHAR2(40);        
  v_node_name               node.NAME%TYPE;
  v_node_type_id            VARCHAR2(40);   
  v_user_node_role          VARCHAR2(40);
  v_node_char_cnt           NUMBER := 0;
  v_usr_char_cnt            NUMBER := 0;
  v_cri_char_cnt            NUMBER := 0;
  v_append_intersect        BOOLEAN := FALSE;
  
  --exclude variables
  v_e_sql                   CLOB;
  v_e_sql_cntry             VARCHAR2(5000);
  v_e_sql_emp               VARCHAR2(5000);
  v_e_sql_node              VARCHAR2(5000);
  v_e_sql_nc                VARCHAR2(5000);
  v_e_sql_nc_str            VARCHAR2(5000);    
  v_e_sql_char              VARCHAR2(5000);
  v_e_sql_char_str          VARCHAR2(5000);
  
  v_e_position_type         participant_employer.position_type%TYPE;
  v_e_dep_type              participant_employer.department_type%TYPE;
  v_e_node_id               VARCHAR2(40);        
  v_e_node_name             node.NAME%TYPE;
  v_e_node_type_id          VARCHAR2(40);   
  v_e_user_node_role        VARCHAR2(40);
  v_e_cri_char_cnt          NUMBER; 
  v_pax_audience_index      participant_audience.participant_audience_index%TYPE;
  v_retun_lck               NUMBER;
 
  TYPE user_id_type IS TABLE OF participant_audience.user_id%TYPE;
  
  tbl_user_id      user_id_type; 
  tbl_ex_user_id   user_id_type;
  
BEGIN

  v_stage := 'Write start to execution_log table';
  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i,
                          'Procedure Started for Parameter: '||pi_promotion_id,
                          NULL);
 DELETE FROM tmp_audience_user_id;
 FOR rec_audience IN cur_audience (pi_promotion_id) LOOP
    
    v_audience_id := rec_audience.audience_id;
    v_rec_cnt     := 0;
    
   v_stage := 'Delete global temporary table gtt_pax_audience';
   DELETE FROM gtt_pax_audience;
   
   DELETE FROM gtt_ex_pax_audience;
    
    <<audience_criteria_loop>>
    FOR rec_cri_aud IN cur_cri_aud (v_audience_id) LOOP

        v_sql        := NULL;
        v_sql_emp    := NULL;
        v_sql_lang   := NULL;
        v_sql_node   := NULL;
        v_sql_nc_str := NULL;
        v_sql_nc     := NULL;
        v_sql_cntry  := NULL;
        v_sql_char   := NULL;
        v_sql_char_str := NULL;
        v_sql_all      := NULL;
        v_e_sql          := NULL;
        v_e_sql_cntry    := NULL;
        v_e_sql_emp      := NULL;
        v_e_sql_node     := NULL;
        v_e_sql_nc       := NULL;
        v_e_sql_nc_str   := NULL;
        v_e_sql_char     := NULL;
        v_e_sql_char_str := NULL;
        v_cri_char_cnt     := 0;
        v_append_intersect := FALSE;
        v_e_cri_char_cnt   :=0;
        
      SELECT COUNT (1)
        INTO v_cri_char_cnt
        FROM audience_criteria_char
       WHERE audience_criteria_id = rec_cri_aud.audience_criteria_id;

      --All criteria (default) and no criteria char found
      IF rec_cri_aud.first_name                  IS NULL AND
         rec_cri_aud.last_name                   IS NULL AND
         rec_cri_aud.employer_id                 IS NULL AND
         rec_cri_aud.position_type               IS NULL AND
         rec_cri_aud.department_type             IS NULL AND
         rec_cri_aud.language_id                 IS NULL AND
         rec_cri_aud.node_id                     IS NULL AND
         rec_cri_aud.node_name                   IS NULL AND
         rec_cri_aud.node_type_id                IS NULL AND
         rec_cri_aud.user_node_role              IS NULL AND
         rec_cri_aud.include_child_nodes         = 0     AND
         rec_cri_aud.exclude_country_id          IS NULL AND
         rec_cri_aud.exclude_node_id             IS NULL AND
         rec_cri_aud.exclude_node_name           IS NULL AND
         rec_cri_aud.exclude_node_type_id        IS NULL AND
         rec_cri_aud.exclude_user_node_role      IS NULL AND
         rec_cri_aud.exclude_include_child_nodes = 0     AND
         rec_cri_aud.exclude_position_type       IS NULL AND
         rec_cri_aud.exclude_department_type     IS NULL AND
         rec_cri_aud.country_id                  IS NULL AND
         v_cri_char_cnt                          = 0     THEN
        
          v_sql_all := ' SELECT a.user_id
                           FROM application_user a, 
                                participant b 
                          WHERE a.user_id = b.user_id
                            AND ((a.is_active = 1
                            AND b.status = ''active'') OR (terms_acceptance = ''notaccepted'' AND termination_date IS NULL))';

          v_sql := v_sql_all;
        
      ELSE  

       IF v_e_cri_char_cnt = 1 THEN
       v_sql_all := ' SELECT a.user_id
                           FROM application_user a, 
                                participant b 
                          WHERE a.user_id = b.user_id
                            AND ((a.is_active = 1
                            AND b.status = ''active'') OR (terms_acceptance = ''notaccepted'' AND termination_date IS NULL))';
       ELSE 
        v_sql_all := ' SELECT a.user_id
                         FROM application_user a, 
                              participant b 
                        WHERE a.user_id   = b.user_id
                          AND ((a.is_active = 1
                            AND b.status = ''active'') OR (terms_acceptance = ''notaccepted'' AND termination_date IS NULL))
                          AND a.user_id   IN ( ';
       END IF;
  
        v_sql := v_sql_all; 
        
        --employer, position, department
        IF rec_cri_aud.employer_id     IS NOT NULL OR
           rec_cri_aud.position_type   IS NOT NULL OR
           rec_cri_aud.department_type IS NOT NULL THEN

          IF rec_cri_aud.employer_id IS NOT NULL THEN
            v_employer_id := rec_cri_aud.employer_id; 
          ELSE
            v_employer_id := 'NULL';
          END IF;
          
          IF rec_cri_aud.position_type IS NOT NULL THEN
            v_position_type := ''''||rec_cri_aud.position_type||''''; 
          ELSE
            v_position_type := 'NULL'; 
          END IF;
          
          IF rec_cri_aud.department_type IS NOT NULL THEN
            v_dep_type := ''''||rec_cri_aud.department_type||''''; 
          ELSE
            v_dep_type := 'NULL';
          END IF;   

          v_stage := 'Get users with employer, position, department for audience criteria id: '||rec_cri_aud.audience_criteria_id;
          v_sql_emp := 'SELECT user_id
                          FROM vw_curr_pax_employer
                         WHERE employer_id                 = NVL('||v_employer_id||', employer_id)
                           AND NVL(position_type, ''X'')   = NVL('||v_position_type||', NVL(position_type, ''X''))
                           AND NVL(department_type, ''X'') = NVL('||v_dep_type||', NVL(department_type, ''X'')) 
                           AND (termination_date IS NULL OR termination_date >= SYSDATE ) ';

          v_sql := v_sql||v_sql_emp;
          v_append_intersect := TRUE;  
        END IF;


        --Language
        IF rec_cri_aud.language_id IS NOT NULL THEN
          
          v_stage := 'Get users with language for audience criteria id: '||rec_cri_aud.audience_criteria_id;
          v_sql_lang :=  ' SELECT user_id
                             FROM application_user 
                            WHERE LOWER(language_id) = LOWER('''||rec_cri_aud.language_id||''') ';

          IF v_append_intersect THEN
            v_sql := v_sql||' INTERSECT '||v_sql_lang;
          ELSE
            v_sql := v_sql||v_sql_lang;
            v_append_intersect := TRUE;
          END IF;
          
        END IF;


        --node_id,node_name,node_type_id,user_node_role,include_child_nodes
        IF rec_cri_aud.node_id        IS NOT NULL OR
           rec_cri_aud.node_name      IS NOT NULL OR
           rec_cri_aud.node_type_id   IS NOT NULL OR
           rec_cri_aud.user_node_role IS NOT NULL THEN

          IF rec_cri_aud.node_id IS NOT NULL THEN
            v_node_id := rec_cri_aud.node_id;
          ELSE
            v_node_id := 'NULL';  
          END IF;

          IF rec_cri_aud.node_name IS NOT NULL THEN                 
            v_node_name := ''''||rec_cri_aud.node_name||'''';
          ELSE
            v_node_name := 'NULL';
          END IF;
        
          IF rec_cri_aud.node_type_id IS NOT NULL THEN      
            v_node_type_id := rec_cri_aud.node_type_id;  
          ELSE
            v_node_type_id := 'NULL';
          END IF;

          IF rec_cri_aud.user_node_role IS NOT NULL THEN
            v_user_node_role := ''''||rec_cri_aud.user_node_role||'''';
          ELSE
            v_user_node_role := 'NULL';
          END IF;

          IF rec_cri_aud.include_child_nodes = 0 THEN

            v_stage := 'Get users with node and include_child_nodes =0 for audience criteria id: '||rec_cri_aud.audience_criteria_id;
            v_sql_node := ' SELECT user_id
                             FROM user_node 
                            WHERE role    = NVL('||v_user_node_role||', role)
                              AND node_id IN (SELECT node_id 
                                                FROM node 
                                               WHERE node_id      = NVL('||v_node_id||', node_id)
                                                 AND LOWER(name)  = LOWER(NVL('||v_node_name||', name))
                                                 AND node_type_id = NVL('||v_node_type_id||', node_type_id) ';

            SELECT COUNT (1)
              INTO v_node_char_cnt
              FROM audience_criteria_char ac,
                   characteristic c
             WHERE ac.characteristic_id    = c.characteristic_id
               AND c.characteristic_type   = 'NT' 
               AND ac.audience_criteria_id = rec_cri_aud.audience_criteria_id
               AND ac.search_type          = 'include';

            IF v_node_char_cnt = 0 THEN
              v_sql_node := v_sql_node||')';
            ELSE
              v_stage := 'Get users with node,include_child_nodes =0,node characteristics for audience criteria id: '||
              rec_cri_aud.audience_criteria_id;

              FOR rec_node_char IN cur_char ('NT',rec_cri_aud.audience_criteria_id,'include') LOOP

                v_sql_nc_str := ' SELECT node_id
                                    FROM (SELECT characteristic_id,node_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                            FROM node_characteristic)
                                   WHERE characteristic_id = '||rec_node_char.characteristic_id||'
                                     AND lower(characteristic_value) = '''||rec_node_char.criteria_characteristic_value||''''; --12/16/2013
              
                IF v_sql_nc IS NULL THEN
                  v_sql_nc := v_sql_nc_str;
                ELSE
                  v_sql_nc := v_sql_nc||' INTERSECT '||v_sql_nc_str;
                END IF;
              
              END LOOP;          

              v_sql_node := v_sql_node||' INTERSECT '||v_sql_nc||')';
              
            END IF;
            
          ELSE

            v_stage := 'Get users with node,node_type,user_node_role,include_child_nodes =1 for audience criteria id: '||
            rec_cri_aud.audience_criteria_id;
            v_sql_node := ' SELECT user_id
                              FROM user_node 
                             WHERE role    = NVL('||v_user_node_role||', role) 
                               AND node_id IN (
                                               SELECT node_id 
                                                 FROM node
                                                WHERE node_type_id = NVL('||v_node_type_id||', node_type_id)       
                                                START WITH node_id IN (
                                                                       SELECT node_id 
                                                                         FROM node 
                                                                        WHERE node_id      = NVL('||v_node_id||', node_id)
                                                                          AND LOWER(name)  = LOWER(NVL('||v_node_name||', name))
                                                                       )
                                              CONNECT BY PRIOR node_id = parent_node_id ';

            SELECT COUNT (1)
              INTO v_node_char_cnt
              FROM audience_criteria_char ac,
                   characteristic c
             WHERE ac.characteristic_id    = c.characteristic_id
               AND c.characteristic_type   = 'NT' 
               AND ac.audience_criteria_id = rec_cri_aud.audience_criteria_id
               AND ac.search_type          = 'include';

            IF v_node_char_cnt = 0 THEN
              v_sql_node := v_sql_node||')';
            ELSE
              v_stage := 'Check node,include_child_nodes =1,node characteristics for audience criteria id: '||
              rec_cri_aud.audience_criteria_id;
              FOR rec_node_char IN cur_char ('NT',rec_cri_aud.audience_criteria_id,'include') LOOP

                v_sql_nc_str := ' SELECT node_id
                                    FROM (SELECT characteristic_id,node_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                            FROM node_characteristic)
                                   WHERE characteristic_id = '||rec_node_char.characteristic_id||'
                                     AND lower(characteristic_value) = '''||rec_node_char.criteria_characteristic_value||'''';                                
            
                IF v_sql_nc IS NULL THEN
                  v_sql_nc := v_sql_nc_str;
                ELSE
                  v_sql_nc := v_sql_nc||' INTERSECT '||v_sql_nc_str;
                END IF;
              
              END LOOP;          

              v_sql_node := v_sql_node||' INTERSECT '||v_sql_nc||')';
            
            END IF;           
            
          END IF;

          IF v_append_intersect THEN
            v_sql := v_sql||' INTERSECT '||v_sql_node;
          ELSE
            v_sql := v_sql||v_sql_node;
            v_append_intersect := TRUE;
          END IF;

        END IF;


        --country_id
        IF rec_cri_aud.country_id IS NOT NULL THEN
   
         v_stage := 'Get User with Country for audience criteria id: '||
                    rec_cri_aud.audience_criteria_id;
         v_sql_cntry := ' SELECT user_id
                            FROM user_address 
                           WHERE country_id = '||rec_cri_aud.country_id;

          IF v_append_intersect THEN
            v_sql := v_sql||' INTERSECT '||v_sql_cntry;
          ELSE
            v_sql := v_sql||v_sql_cntry;
            v_append_intersect := TRUE;
          END IF;

        END IF;


        --User characteristic
        SELECT COUNT (1)
          INTO v_usr_char_cnt
          FROM audience_criteria_char ac,
               characteristic c
         WHERE ac.characteristic_id    = c.characteristic_id
           AND c.characteristic_type   = 'USER' 
           AND ac.audience_criteria_id = rec_cri_aud.audience_criteria_id
           AND ac.search_type          = 'include';
      
        IF v_usr_char_cnt > 0 THEN
          v_stage := 'Get users with Characteristic for audience criteria id: '||
                     rec_cri_aud.audience_criteria_id;

          FOR rec_user_char IN cur_char ('USER', rec_cri_aud.audience_criteria_id, 'include') LOOP

            v_sql_char_str := ' SELECT user_id
                                  FROM (SELECT characteristic_id,user_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                           FROM user_characteristic)
                                 WHERE characteristic_id = '||rec_user_char.characteristic_id||'
                                   AND REGEXP_LIKE (lower(characteristic_value),''(^|,)''||REPLACE(LOWER('''||rec_user_char.criteria_characteristic_value||'''),'','',''|'')||''(,|$)'') '; --16/12/2013 --05/24/2016
            
            IF v_sql_char IS NULL THEN
              v_sql_char := v_sql_char_str;
            ELSE
              v_sql_char := v_sql_char||' INTERSECT '||v_sql_char_str;
            END IF;

          END LOOP;

          IF v_append_intersect THEN
            v_sql := v_sql||' INTERSECT '||v_sql_char;
          ELSE
            v_sql := v_sql||v_sql_char;
          END IF;
        
        END IF;

        --Excludes criteria starts
        --exclude_country_id
        IF rec_cri_aud.exclude_country_id IS NOT NULL THEN

         v_stage := 'Get users exclude country for audience criteria id: '||
                     rec_cri_aud.audience_criteria_id;
         v_e_sql_cntry :=  ' SELECT user_id
                               FROM user_address 
                              WHERE country_id = '||rec_cri_aud.exclude_country_id;

          v_e_sql := v_e_sql_cntry;
                
        END IF;


        --exclude_position_type, exclude_department_type
        IF rec_cri_aud.exclude_position_type IS NOT NULL OR 
           rec_cri_aud.exclude_department_type IS NOT NULL THEN

          IF rec_cri_aud.exclude_position_type IS NOT NULL THEN
            v_e_position_type := ''''||rec_cri_aud.exclude_position_type||''''; 
          ELSE
            v_e_position_type := 'NULL'; 
          END IF;
          
          IF rec_cri_aud.exclude_department_type IS NOT NULL THEN
            v_e_dep_type := ''''||rec_cri_aud.exclude_department_type||''''; 
          ELSE
            v_e_dep_type := 'NULL';
          END IF;   
    

          v_stage := 'Get users exclude position, department for audience criteria id: '||
                     rec_cri_aud.audience_criteria_id;
          v_e_sql_emp :=   ' SELECT user_id
                               FROM vw_curr_pax_employer
                              WHERE NVL(position_type, ''X'')   = NVL('||v_e_position_type||', NVL(position_type, ''X''))
                                AND NVL(department_type, ''X'') = NVL('||v_e_dep_type||', NVL(department_type, ''X'')) ';

          IF v_e_sql IS NULL THEN
            v_e_sql := v_e_sql_emp;
          ELSE
            v_e_sql := v_e_sql||' INTERSECT '||v_e_sql_emp;
          END IF;
         
        END IF;

        --exclude_node_id, exclude_node_name, exclude_node_type_id, exclude_user_node_role, exclude_include_child_nodes 
        IF rec_cri_aud.exclude_node_id        IS NOT NULL OR
           rec_cri_aud.exclude_node_name      IS NOT NULL OR
           rec_cri_aud.exclude_node_type_id   IS NOT NULL OR
           rec_cri_aud.exclude_user_node_role IS NOT NULL THEN

          IF rec_cri_aud.exclude_node_id IS NOT NULL THEN
            v_e_node_id := rec_cri_aud.exclude_node_id;
          ELSE
            v_e_node_id := 'NULL';  
          END IF;

          IF rec_cri_aud.exclude_node_name IS NOT NULL THEN                 
            v_e_node_name := ''''||rec_cri_aud.exclude_node_name||'''';
          ELSE
            v_e_node_name := 'NULL';
          END IF;
        
          IF rec_cri_aud.exclude_node_type_id IS NOT NULL THEN      
            v_e_node_type_id := rec_cri_aud.exclude_node_type_id;  
          ELSE
            v_e_node_type_id := 'NULL';
          END IF;

          IF rec_cri_aud.exclude_user_node_role IS NOT NULL THEN
            v_e_user_node_role := ''''||rec_cri_aud.exclude_user_node_role||'''';
          ELSE
            v_e_user_node_role := 'NULL';
          END IF;

          IF rec_cri_aud.exclude_include_child_nodes = 0 THEN

            v_stage := 'Get users exclude node,node_type,user_node_role,include_child_nodes =0 for audience criteria id: '||
                        rec_cri_aud.audience_criteria_id;
            v_e_sql_node :=' SELECT user_id
                               FROM user_node 
                              WHERE role    = NVL('||v_e_user_node_role||', role) 
                                AND node_id IN (SELECT node_id 
                                                  FROM node 
                                                 WHERE node_id      = NVL('||v_e_node_id||', node_id)
                                                   AND LOWER(name)  = LOWER(NVL('||v_e_node_name||', name))
                                                   AND node_type_id = NVL('||v_e_node_type_id||', node_type_id) ';

            SELECT COUNT (1)
              INTO v_node_char_cnt
              FROM audience_criteria_char ac,
                   characteristic c
             WHERE ac.characteristic_id  = c.characteristic_id
               AND c.characteristic_type = 'NT' 
               AND ac.audience_criteria_id = rec_cri_aud.audience_criteria_id
               AND ac.search_type          = 'exclude';

            IF v_node_char_cnt = 0 THEN
              v_e_sql_node := v_e_sql_node||')';
            ELSE

              v_stage := 'Get users exclude node,node_type,user_node_role,include_child_nodes =0,node characteristics for audience criteria id: '||
                          rec_cri_aud.audience_criteria_id;
              FOR rec_node_char IN cur_char ('NT', rec_cri_aud.audience_criteria_id,'exclude') LOOP

                v_e_sql_nc_str := ' SELECT node_id
                                      FROM (SELECT characteristic_id,node_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                              FROM node_characteristic)
                                     WHERE characteristic_id = '||rec_node_char.characteristic_id||'
                                       AND lower(characteristic_value) = '''||rec_node_char.criteria_characteristic_value||''''; --16/12/2013
            
                IF v_e_sql_nc IS NULL THEN
                  v_e_sql_nc := v_e_sql_nc_str;
                ELSE
                  v_e_sql_nc := v_e_sql_nc||' INTERSECT '||v_e_sql_nc_str;
                END IF;
              
              END LOOP;      

              v_e_sql_node := v_e_sql_node||' INTERSECT '||v_e_sql_nc||')';
              
            END IF;
                      
          ELSE

            v_stage := 'Get users node,node_type,user_node_role,include_child_nodes =1 for audience criteria id: '||
                        rec_cri_aud.audience_criteria_id;

            v_e_sql_node :=' SELECT user_id
                               FROM user_node 
                              WHERE role    = NVL('||v_e_user_node_role||', role) 
                                AND node_id IN (
                                                SELECT node_id 
                                                  FROM node
                                                 WHERE node_type_id = NVL('||v_e_node_type_id||', node_type_id)       
                                                 START WITH node_id IN (
                                                                        SELECT node_id 
                                                                          FROM node 
                                                                         WHERE node_id      = NVL('||v_e_node_id||', node_id)
                                                                           AND LOWER(name)  = LOWER(NVL('||v_e_node_name||', name))
                                                                        )
                                               CONNECT BY PRIOR node_id = parent_node_id ';

            SELECT COUNT (1)
              INTO v_node_char_cnt
              FROM audience_criteria_char ac,
                   characteristic c
             WHERE ac.characteristic_id  = c.characteristic_id
               AND c.characteristic_type = 'NT' 
               AND ac.audience_criteria_id = rec_cri_aud.audience_criteria_id
               AND ac.search_type          = 'exclude';

            IF v_node_char_cnt = 0 THEN
              v_e_sql_node := v_e_sql_node||')';
            ELSE  

              v_stage := 'Get users node,node_type,user_node_role,include_child_nodes =1,node characteristics for audience criteria id: '||
                          rec_cri_aud.audience_criteria_id;
              FOR rec_node_char IN cur_char ('NT', rec_cri_aud.audience_criteria_id,'exclude') LOOP

                v_e_sql_nc_str := ' SELECT node_id
                                      FROM (SELECT characteristic_id,node_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                              FROM node_characteristic)
                                     WHERE characteristic_id = '||rec_node_char.characteristic_id||'
                                       AND lower(characteristic_value) = '''||rec_node_char.criteria_characteristic_value||'''';
            
                IF v_e_sql_nc IS NULL THEN
                  v_e_sql_nc := v_e_sql_nc_str;
                ELSE
                  v_e_sql_nc := v_e_sql_nc||' INTERSECT '||v_e_sql_nc_str;
                END IF;
              
              END LOOP;      

              v_e_sql_node := v_e_sql_node||' INTERSECT '||v_e_sql_nc||')';
            
            END IF;          
          
          END IF;

          IF v_e_sql IS NULL THEN
            v_e_sql := v_e_sql_node;
          ELSE
            v_e_sql := v_e_sql||' INTERSECT '||v_e_sql_node;
          END IF;


        END IF;

        --User characteristic
        SELECT COUNT (1)
          INTO v_usr_char_cnt
          FROM audience_criteria_char ac,
               characteristic c
         WHERE ac.characteristic_id    = c.characteristic_id
           AND c.characteristic_type   = 'USER' 
           AND ac.audience_criteria_id = rec_cri_aud.audience_criteria_id
           AND ac.search_type          = 'exclude';

        IF v_usr_char_cnt > 0 THEN

          v_stage := 'Get user exclude characteristic for for audience criteria id: '||
                      rec_cri_aud.audience_criteria_id;
          FOR rec_user_char IN cur_char ('USER', rec_cri_aud.audience_criteria_id, 'exclude') LOOP

            v_e_sql_char_str :=' SELECT user_id
                                   FROM (SELECT characteristic_id,user_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                           FROM user_characteristic)
                                  WHERE characteristic_id = '||rec_user_char.characteristic_id||'
                                    AND REGEXP_LIKE (lower(characteristic_value),''(^|,)''||REPLACE(LOWER('''||rec_user_char.criteria_characteristic_value||'''),'','',''|'')||''(,|$)'') '; --16/12/2013 --05/24/2016
            
            IF v_e_sql_char IS NULL THEN
              v_e_sql_char := v_e_sql_char_str;
            ELSE
              v_e_sql_char := v_e_sql_char||' INTERSECT '||v_e_sql_char_str;
            END IF;

          END LOOP;

          IF v_e_sql IS NULL THEN
            v_e_sql := v_e_sql_char;
          ELSE
            v_e_sql := v_e_sql||' INTERSECT '||v_e_sql_char;
          END IF;

        END IF;
        
        --Excludes criteria ends

         IF v_e_cri_char_cnt =0  then 
            v_sql := v_sql||')';
         END IF;

      END IF; --ALL
              
      IF v_sql LIKE '%IN ( )%' THEN
          v_sql := REPLACE(v_sql,'IN ( )','IN ( NULL )'); -- no pax qualified
        END IF;
        
      BEGIN
        v_stage := 'Execute immediate'; 
        EXECUTE IMMEDIATE v_sql BULK COLLECT INTO tbl_user_id;

        IF tbl_user_id.COUNT > 0 THEN
          FORALL i IN tbl_user_id.FIRST .. tbl_user_id.LAST
           INSERT INTO gtt_pax_audience VALUES (tbl_user_id(i), v_audience_id);
        END IF;        
  
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          NULL;
        WHEN OTHERS THEN
          po_returncode := 99;
          prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'Error at stage: '||v_stage||
                            ' Message: '||SQLERRM,
                            NULL);
      END;   
    
    --Insert Users of Exclude Criteria
     IF v_e_sql IS NOT NULL THEN
      BEGIN
        v_stage := 'Execute immediate'; 
        EXECUTE IMMEDIATE v_e_sql BULK COLLECT INTO tbl_ex_user_id;

        IF tbl_ex_user_id.COUNT > 0 THEN
          FORALL i IN tbl_ex_user_id.FIRST .. tbl_ex_user_id.LAST
           INSERT INTO gtt_ex_pax_audience VALUES (tbl_ex_user_id(i), v_audience_id);
        END IF;        
  
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          NULL;
        WHEN OTHERS THEN 
          po_returncode := 99;
          prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'Error at stage: '||v_stage||
                            ' Message: '||SQLERRM,
                            NULL);
      END;     
    
     END IF;

    END LOOP;
    
    DELETE FROM gtt_pax_audience
        WHERE user_id IN (SELECT user_id FROM gtt_ex_pax_audience);

    SELECT COUNT(1)
      INTO v_rec_cnt 
      FROM gtt_pax_audience
     WHERE audience_id = v_audience_id;

    IF v_rec_cnt > 0 THEN
      v_stage := 'Deleting earlier assigned Users but not meets criteria after audience_criteria modification';
      DELETE FROM participant_audience pa
       WHERE pa.audience_id = v_audience_id
         AND NOT EXISTS (SELECT 'X'
                           FROM gtt_pax_audience gpa
                          WHERE gpa.user_id     = pa.user_id
                            AND gpa.audience_id = pa.audience_id
                            AND gpa.audience_id = v_audience_id);
     
      v_retun_lck := fnc_lock_control_audience(rec_audience.audience_id);      
      IF v_retun_lck = 99 THEN
       v_log_msg := 'Resource Busy';
       RAISE e_exit_pgm_lck;
      END IF;       

--      BEGIN
--        v_stage := 'Find Max audience index for audience id: '||v_audience_id;
--        SELECT participant_audience_index + 1
--          INTO v_pax_audience_index
--          FROM (SELECT RANK () OVER (PARTITION BY audience_id ORDER BY participant_audience_index DESC) AS rec_rank, 
--                       participant_audience_index 
--                  FROM participant_audience
--                 WHERE audience_id = v_audience_id)
--         WHERE rec_rank = 1;
--      EXCEPTION
--        WHEN NO_DATA_FOUND THEN
--          v_pax_audience_index := 0;
--      END;
    v_pax_audience_index :=0;

      v_stage := 'Insert participant_audience for audience id: '||v_audience_id;    
      INSERT INTO tmp_audience_user_id
                  (user_id, audience_id)                       
           SELECT pax_aud.user_id, pax_aud.audience_id
             FROM (SELECT DISTINCT user_id, audience_id
                     FROM gtt_pax_audience gpa
                    WHERE gpa.audience_id = v_audience_id
                   ) pax_aud;  
    END IF;
    
  END LOOP;
    
  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i,'Procedure Completed',
                          NULL);
  po_returncode := 0;

EXCEPTION  
  WHEN e_exit_pgm_lck THEN
    po_returncode := 98;
    prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'Error at stage: '||v_stage||
                            ' Message: '||v_log_msg,
                            NULL);
  WHEN e_exit_pgm THEN
    po_returncode := 99;
    prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'Error at stage: '||v_stage||
                            ' Message: '||v_log_msg,
                            NULL);

  WHEN OTHERS THEN
    po_returncode := 99;
    prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'Error at stage: '||v_stage||
                            ' Message: '||SQLERRM,
                            NULL);

END prc_refresh_t_and_c_audience;
/
