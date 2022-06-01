CREATE OR REPLACE PACKAGE pkg_build_audience IS

/*----------------------------------------------------------------------------

  Purpose: Assign audience to Participant based on audience criteria.This process
          will add/remove records to participant_audience

  Person        Date         Comments
  -----------   -----------  ---------------------------------------------------
  Arun S        09/24/2012   Initial creation
-----------------------------------------------------------------------------*/

PROCEDURE prc_sync_pax_criteria_audience
 (pi_user_id        IN  application_user.user_id%TYPE,
  pi_import_file_id IN  stage_pax_import_record.import_file_id%TYPE,
  po_returncode     OUT NUMBER);

PROCEDURE prc_sync_audience_hierarchy
 (pi_target_node_id      IN  node.node_id%TYPE,
  pi_destination_node_id IN  node.node_id%TYPE,
  pi_child_dest_node_id  IN  node.node_id%TYPE,
  po_returncode          OUT NUMBER,
  p_out_user_data OUT SYS_REFCURSOR);

PROCEDURE prc_refresh_criteria_audience
 (pi_audience_id IN  audience.audience_id%TYPE,
  po_returncode  OUT NUMBER,
  p_out_user_data OUT SYS_REFCURSOR);
   
END;
/
CREATE OR REPLACE PACKAGE BODY pkg_build_audience IS
/*----------------------------------------------------------------------------

  Purpose: Assign audience to Participant based on audience criteria.This process
          will add/remove records to participant_audience

  Person        Date         Comments
  -----------   -----------  ---------------------------------------------------
  Arun S        09/24/2012   Initial creation   
  Chidamba      11/08/2013   Fix - Bug 49776# Making characteristic_value check 
                             as case insensitive by adding LOWER()
-----------------------------------------------------------------------------*/

PROCEDURE prc_sync_pax_criteria_audience
 (pi_user_id        IN  application_user.user_id%TYPE,
  pi_import_file_id IN  stage_pax_import_record.import_file_id%TYPE,
  po_returncode     OUT NUMBER)

IS

/*----------------------------------------------------------------------------

  Purpose: Assign audience to Participant based on audience criteria.This process
          will add/remove records to participant_audience

  Person        Date         Comments
  -----------   -----------  ---------------------------------------------------
  Arun S        09/24/2012   Initial creation   
  Ravi Dhanekula 01/23/2013  Fixed the performance issue with cur_user.
  Ravi Dhanekula 4/20/2013   Changed the declaration of the variables to fetch from the tables instead of hard coding the length.
  Arun S         04/26/2013  Changes to below issue fix
                             There is an issue when defining criteria audience only with default search criteria 
                             (Define audience in Add audience page with default selected search criteria alone - issue in
                             submitting page)  
  Ravi Dhanekula 02/28/2014  Fixed the bug # 51835. That was caused with single quotes in node names.
                 04/01/2014  Fixed the bug # 52544. 
  Chidamba       07/11/2014  Bug 53134 - prc_sync_pax_criteria_audience DUPE error
                             fix to overcome Concurrency issue during self-enrolli
 Suresh J       02/18/2015   Bug 59725 - Fixed the problem related to index when pax got deleted from participant_audience table                              
 nagarajs       10/01/2015   Bug 64054  - When node or user characteristic value has braces it's not populating the eligible users into participant_audience table                   
 nagarajs       01/07/2016   Bug 65155 - PKG_BUILD_AUDIENCE.PRC_SYNC_PAX_CRITERIA_AUDIENCE error - for include and exclude user characteristic criteria
 Ravi Dhanekula 02/03/2016   Added a performance fix.
Suresh J        03/25/2016   Bug 66214 - PKG_BUILD_AUDIENCE.sync_pax_criteria_audience procedure is taking more time 
Gorantla        03/14/2018   Bug 75683/G6-3940 - fix single-quote issue in node_name
-----------------------------------------------------------------------------*/


  --Pax cursor
  CURSOR cur_user (vi_usr_id      application_user.user_id%TYPE,
                   vi_imp_file_id stage_pax_import_record.import_file_id%TYPE) IS
     SELECT au.user_id, 
           au.is_active 
      FROM application_user au,
           participant pax
     WHERE au.user_id   = pax.user_id
     and au.user_id = vi_usr_id
     UNION         
    SELECT au.user_id, 
           au.is_active 
      FROM application_user au,
           participant pax
     WHERE au.user_id   = pax.user_id
       AND  au.user_name IN (SELECT user_name
                                  FROM stage_pax_import_record
                                 WHERE import_file_id = vi_imp_file_id
                                   AND import_record_id NOT IN (SELECT import_record_id
                                                                  FROM import_record_error
                                                                 WHERE import_file_id = vi_imp_file_id
                                                                )                                
            );

  --Audience cursor
  CURSOR cur_audience IS
    SELECT audience_id,
           name
      FROM audience
     WHERE list_type = 'criteria'
     ORDER BY audience_id;

  --Audience criteria cursor
  CURSOR cur_cri_aud (p_in_audience_id audience_criteria.audience_id%TYPE) IS
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
  --replace(node_name, '''', '"')node_name, --02/28/2014
  replace(node_name, '''', '''''')node_name, --03/14/2018
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
     WHERE audience_id = p_in_audience_id
     ORDER BY audience_criteria_id;

  --Audience node characteristic cursor
  CURSOR cur_node_char (vi_audience_criteria_id audience_criteria_char.audience_criteria_id%TYPE,
                        vi_search_type audience_criteria_char.search_type%TYPE ) IS
    SELECT ac.characteristic_id,
           lower(REPLACE(REPLACE(ac.criteria_characteristic_value,'(') ,')')) criteria_characteristic_value --10/01/2015
           --lower(ac.criteria_characteristic_value) criteria_characteristic_value --11/08/2013 --10/01/2015
      FROM audience_criteria_char ac,
           characteristic c
     WHERE ac.characteristic_id    = c.characteristic_id
       AND c.characteristic_type   = 'NT' 
       AND ac.audience_criteria_id = vi_audience_criteria_id
       AND ac.search_type          = vi_search_type;

  --Audience user characteristic cursor
  CURSOR cur_user_char (vi_aud_cri_id audience_criteria_char.audience_criteria_id%TYPE,
                        vi_search_typ audience_criteria_char.search_type%TYPE ) IS
    SELECT ac.characteristic_id, 
           lower(REPLACE(REPLACE(ac.criteria_characteristic_value,'(') ,')')) criteria_characteristic_value --10/01/2015
           --lower(ac.criteria_characteristic_value) criteria_characteristic_value --11/08/2013 --10/01/2015
      FROM audience_criteria_char ac,
           characteristic c
     WHERE ac.characteristic_id    = c.characteristic_id
       AND c.characteristic_type   = 'USER'
       AND ac.audience_criteria_id = vi_aud_cri_id
       AND ac.search_type          = vi_search_typ;

  CURSOR cur_rebuild_index is    --03/25/2016 Bug 66214
      SELECT pa.audience_id,
             pa.user_id,
             pa.participant_audience_index,
             rank () over (partition by pa.audience_id order by rownum asc) - 1
                as new_index_value
        FROM participant_audience pa
       WHERE pa.audience_id in                   
                               (  select pa2.audience_id
                                    from participant_audience pa2
                                group by pa2.audience_id
                                  having count (distinct pa2.user_id) <>
                                            (  max (pa2.participant_audience_index)
                                             + 1)
                               )
    order by pa.audience_id, pa.participant_audience_index;       

--CURSOR cur_rebuild_index IS  --02/18/2015 --02/03/2016
--SELECT PA.AUDIENCE_ID, 
--       PA.USER_ID, 
--       PA.PARTICIPANT_AUDIENCE_INDEX, 
--       RANK() OVER (PARTITION BY PA.AUDIENCE_ID 
--                    ORDER BY ROWNUM ASC) - 1 AS NEW_INDEX_VALUE 
--  FROM PARTICIPANT_AUDIENCE PA 
-- WHERE EXISTS (SELECT PA2.AUDIENCE_ID 
--                 FROM PARTICIPANT_AUDIENCE PA2 
--                WHERE PA2.AUDIENCE_ID = PA.AUDIENCE_ID 
--                GROUP BY PA2.AUDIENCE_ID 
--               HAVING COUNT(DISTINCT PA2.USER_ID) <> MAX(PA2.PARTICIPANT_AUDIENCE_INDEX) + 1) 
-- ORDER BY PA.AUDIENCE_ID, PA.PARTICIPANT_AUDIENCE_INDEX;
--
--SELECT
--       pa.audience_id,
--       pa.user_id,
--       pa.participant_audience_index,
--       rank() over (partition by pa.audience_id order by rownum asc  )-1 as new_index_value
--from
--participant_audience pa
--WHERE exists
--(select pa2.audience_id
--from participant_audience pa2
--group by pa2.audience_id
--having count(distinct pa2.user_id) <> (max (pa2.participant_audience_index)+1)
--       and pa2.audience_id = pa.audience_id 
--)
--order by pa.audience_id,pa.participant_audience_index;

  --Exception
  e_exit_pgm                EXCEPTION;

  --Execution log variables
  C_process_name            execution_log.process_name%TYPE  := 'prc_sync_pax_criteria_audience';
  C_release_level           execution_log.release_level%TYPE := '1';
  C_severity_i              execution_log.severity%TYPE      := 'INFO';
  C_severity_e              execution_log.severity%TYPE      := 'ERROR';
  v_log_msg                 execution_log.text_line%TYPE;

  --Procedure variables
  v_stage                   VARCHAR2(500);                   
  vi_user_id                participant_audience.user_id%TYPE;
  vi_import_file_id         import_file.import_file_id%TYPE;
  v_sql                     CLOB;
  v_sql_emp                 VARCHAR2(5000);
  v_sql_lang                VARCHAR2(5000);
  v_sql_node                VARCHAR2(5000);
  v_sql_nc                  VARCHAR2(5000);
  v_sql_nc_str              VARCHAR2(5000);
  v_sql_cntry               VARCHAR2(5000);
  v_sql_char                VARCHAR2(5000);
  v_sql_char_str            VARCHAR2(5000);  
  v_sql_all                 VARCHAR2(5000);
  
  v_employer_id             VARCHAR2(40);
  v_position_type           participant_employer.position_type%type; --4/20/2013
  v_dep_type                participant_employer.department_type%type; --4/20/2013
  v_lang_id                 VARCHAR2(40);
  v_node_id                 VARCHAR2(40);        
  v_node_name               node.name%type; --4/20/2013
  v_node_type_id            VARCHAR2(40);   
  v_user_node_role          VARCHAR2(40);
  v_node_char_cnt           NUMBER := 0;
  v_usr_char_cnt            NUMBER := 0;
  v_cri_char_cnt            NUMBER := 0;
  v_append_intersect        BOOLEAN := FALSE;
  v_retun_lck               NUMBER;
  v_aud_cri_char_cnt        NUMBER := 0;        --01/07/2016
  v_exclude_cnt             NUMBER := 0;        --01/07/2016
  
  --exclude variables
  v_e_sql                   CLOB;
  v_e_sql_cntry             VARCHAR2(5000);
  v_e_sql_emp               VARCHAR2(5000);
  v_e_sql_node              VARCHAR2(5000);
  v_e_sql_nc                VARCHAR2(5000);
  v_e_sql_nc_str            VARCHAR2(5000);    
  v_e_sql_char              VARCHAR2(5000);
  v_e_sql_char_str          VARCHAR2(5000);
  
  v_e_position_type         participant_employer.position_type%type; --4/20/2013
  v_e_dep_type              participant_employer.department_type%type; --4/20/2013
  v_e_node_id               VARCHAR2(40);        
  v_e_node_name             node.name%type; --4/20/2013
  v_e_node_type_id          VARCHAR2(40);   
  v_e_user_node_role        VARCHAR2(40);
  
    
  v_user_id                 participant_audience.user_id%TYPE;
  v_set_delete              BOOLEAN := FALSE;
  v_pax_audience_index      participant_audience.participant_audience_index%TYPE;
  
  e_exit_pgm_lck            EXCEPTION;

BEGIN  

  v_stage := 'Write start to execution_log table';
  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i,
                          'Procedure Started. Parameters pi_user_id: '||pi_user_id||
                          ' pi_import_file_id: '||pi_import_file_id,
                          NULL);

  v_stage := 'Validate Inparam';
  IF pi_user_id IS NOT NULL AND pi_import_file_id IS NOT NULL THEN
    v_log_msg := 'Both Inparameter User id and Import file id passed with value. Either one should be Null';
    RAISE e_exit_pgm;
  END IF;

  <<user_loop>>
  FOR rec_user IN cur_user (pi_user_id, pi_import_file_id) LOOP

    vi_user_id := rec_user.user_id;

    IF rec_user.is_active = 0 THEN
      v_stage := 'Delete participant_audience for User Id: '||vi_user_id;
      DELETE FROM participant_audience
       WHERE user_id = vi_user_id
         AND audience_id IN (SELECT audience_id
                               FROM audience
                              WHERE list_type = 'criteria');

      CONTINUE user_loop; 
    END IF;
  
    <<audience_loop>>
    FOR rec_audience IN cur_audience LOOP
      
      v_set_delete := FALSE;
      
      SELECT COUNT (1)      --01/07/2016
        INTO v_aud_cri_char_cnt
        FROM audience_criteria_char
       WHERE audience_criteria_id IN (SELECT audience_criteria_id  
                                        FROM audience_criteria
                                       WHERE audience_id = rec_audience.audience_id);

      <<audience_criteria_loop>>
      FOR rec_cri_aud IN cur_cri_aud (rec_audience.audience_id) LOOP

        v_user_id    := NULL;
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
        
        SELECT COUNT (1), 
               COUNT(DECODE(SEARCH_TYPE,'exclude',1,null))      --01/07/2016
          INTO v_cri_char_cnt, 
               v_exclude_cnt                                    --01/07/2016
          FROM audience_criteria_char
         WHERE audience_criteria_id = rec_cri_aud.audience_criteria_id;

        --All criteria (default) and no criteria char found     --04/26/2013
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
                          WHERE a.user_id   = b.user_id
                            AND a.is_active = 1
                            AND b.status    = ''active''
                            AND a.user_id     = '||vi_user_id;

          v_sql := v_sql_all;

        ELSE
          
         IF rec_cri_aud.first_name                  IS NULL AND     --01/07/2016 Added IF
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
           v_exclude_cnt                           <> 0    AND
           v_cri_char_cnt                          = 1     AND
           v_aud_cri_char_cnt                      > 1     THEN

          v_sql_all := ' SELECT a.user_id
                           FROM application_user a, 
                                participant b 
                          WHERE a.user_id   = b.user_id
                            AND a.is_active = 1
                            AND b.status    = ''active''
                            AND a.user_id     = '||vi_user_id||'
                            AND a.user_id   NOT IN';

          v_sql := v_sql_all;
          
         ELSE

          v_sql_all := ' SELECT a.user_id
                           FROM application_user a, 
                                participant b 
                          WHERE a.user_id   = b.user_id
                            AND a.is_active = 1
                            AND b.status    = ''active''
                            AND a.user_id   IN ( ';
  
          v_sql := v_sql_all; 
         END IF;
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

            v_stage := 'Check employer, position, department for pax: '||vi_user_id;
            v_sql_emp := 'SELECT user_id
                            FROM vw_curr_pax_employer
                           WHERE employer_id                 = NVL('||v_employer_id||', employer_id)
                             AND NVL(position_type, ''X'')   = NVL('||v_position_type||', NVL(position_type, ''X''))
                             AND NVL(department_type, ''X'') = NVL('||v_dep_type||', NVL(department_type, ''X''))
                             AND (termination_date IS NULL OR termination_date >= SYSDATE ) 
                             AND user_id                     = '||vi_user_id;

            v_sql := v_sql||v_sql_emp;
            v_append_intersect := TRUE;
          END IF;

        
          --Language
          IF rec_cri_aud.language_id IS NOT NULL THEN
          
            v_stage := 'Check language for pax: '||vi_user_id;
            v_sql_lang :=  ' SELECT user_id
                               FROM application_user 
                              WHERE LOWER(language_id) = LOWER('''||rec_cri_aud.language_id||''')
                                AND user_id     = '||vi_user_id;

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

              v_stage := 'Check node,node_type,user_node_role,include_child_nodes =0 : '||vi_user_id;
              v_sql_node := ' SELECT user_id
                               FROM user_node 
                              WHERE ROWNUM  < 2
                                AND role    = NVL('||v_user_node_role||', role) 
                                AND user_id = '||vi_user_id ||'
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
                v_stage := 'Check node,node_type,user_node_role,include_child_nodes =0,node characteristics : '||vi_user_id;

                FOR rec_node_char IN cur_node_char (rec_cri_aud.audience_criteria_id,'include') LOOP

                  v_sql_nc_str := ' SELECT node_id
                                      FROM (SELECT characteristic_id,node_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                            FROM node_characteristic)
                                     WHERE characteristic_id = '||rec_node_char.characteristic_id||'
                                       AND lower(characteristic_value) = '''||rec_node_char.criteria_characteristic_value||''''; --11/08/2013
              
                  IF v_sql_nc IS NULL THEN
                    v_sql_nc := v_sql_nc_str;
                  ELSE
                    v_sql_nc := v_sql_nc||' INTERSECT '||v_sql_nc_str;
                  END IF;
              
                END LOOP;          

                v_sql_node := v_sql_node||' INTERSECT '||v_sql_nc||')';
              
              END IF;
            
            ELSE

              v_stage := 'Check node,node_type,user_node_role,include_child_nodes =1 : '||vi_user_id;
              v_sql_node := ' SELECT user_id
                                FROM user_node 
                               WHERE ROWNUM  < 2
                                 AND role    = NVL('||v_user_node_role||', role) 
                                 AND user_id = '||vi_user_id ||'
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
                v_stage := 'Check node,node_type,user_node_role,include_child_nodes =1,node characteristics : '||vi_user_id;
                FOR rec_node_char IN cur_node_char (rec_cri_aud.audience_criteria_id,'include') LOOP

                  v_sql_nc_str := ' SELECT node_id
                                      FROM (SELECT characteristic_id,node_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                            FROM node_characteristic)
                                     WHERE characteristic_id = '||rec_node_char.characteristic_id||'
                                       AND lower(characteristic_value) = '''||rec_node_char.criteria_characteristic_value||'''';  --11/08/2013
            
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
   
            v_stage := 'Check Country for pax: '||vi_user_id;
            v_sql_cntry := ' SELECT user_id
                               FROM user_address 
                              WHERE country_id = '||rec_cri_aud.country_id||'
                                AND user_id    = '||vi_user_id||'
                                AND rownum < 2';

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
            v_stage := 'Check Characteristic for pax: '||vi_user_id;

            FOR rec_user_char IN cur_user_char (rec_cri_aud.audience_criteria_id, 'include') LOOP

              v_sql_char_str := ' SELECT user_id
                                    FROM (SELECT characteristic_id,user_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                           FROM user_characteristic)
                                   WHERE characteristic_id = '||rec_user_char.characteristic_id||'
                                     AND REGEXP_LIKE (lower(characteristic_value), '''||rec_user_char.criteria_characteristic_value||''') --11/08/2013
                                     AND user_id = '||vi_user_id;
            
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

          v_stage := 'Check exclude country for pax: '||vi_user_id;
          v_e_sql_cntry :=  ' SELECT user_id
                                FROM user_address 
                              -- WHERE country_id = '||rec_cri_aud.exclude_country_id||'
                              WHERE country_id  in (select EXCLUDE_COUNTRY_ID  from AUDIENCE_CRITERIA -- Bug#78749
                                                                    where audience_id='|| rec_cri_aud.audience_id||') -- Bug#78749
                                 AND user_id    = '||vi_user_id||'
                                 AND rownum     < 2';
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
    

            v_stage := 'Check exclude position, department for pax: '||vi_user_id;
            v_e_sql_emp :=   ' SELECT user_id
                                 FROM vw_curr_pax_employer
                                WHERE NVL(position_type, ''X'')   = NVL('||v_e_position_type||', NVL(position_type, ''X''))
                                  AND NVL(department_type, ''X'') = NVL('||v_e_dep_type||', NVL(department_type, ''X''))
                                  AND user_id                     = '||vi_user_id;

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
  
              v_stage := 'Check exclude node,node_type,user_node_role,include_child_nodes =0 : '||vi_user_id;
              v_e_sql_node :=' SELECT user_id
                                 FROM user_node 
                                WHERE ROWNUM  < 2
                                  AND role    = NVL('||v_e_user_node_role||', role) 
                                  AND user_id = '||vi_user_id ||'
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

                v_stage := 'Check exclude node,node_type,user_node_role,include_child_nodes =0,node characteristics : '||vi_user_id;
                FOR rec_node_char IN cur_node_char (rec_cri_aud.audience_criteria_id,'exclude') LOOP

                  v_e_sql_nc_str := ' SELECT node_id
                                        FROM (SELECT characteristic_id,node_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                                FROM node_characteristic)
                                       WHERE characteristic_id = '||rec_node_char.characteristic_id||'
                                         AND lower(characteristic_value) = '''||rec_node_char.criteria_characteristic_value||''''; --11/08/2013
            
                  IF v_e_sql_nc IS NULL THEN
                    v_e_sql_nc := v_e_sql_nc_str;
                  ELSE
                    v_e_sql_nc := v_e_sql_nc||' INTERSECT '||v_e_sql_nc_str;
                  END IF;
              
                END LOOP;      

                v_e_sql_node := v_e_sql_node||' INTERSECT '||v_e_sql_nc||')';
              
              END IF;
                      
            ELSE

              v_stage := 'Check node,node_type,user_node_role,include_child_nodes =1 : '||vi_user_id;

              v_e_sql_node :=' SELECT user_id
                                 FROM user_node 
                                WHERE ROWNUM  < 2
                                  AND role    = NVL('||v_e_user_node_role||', role) 
                                  AND user_id = '||vi_user_id ||'
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

                v_stage := 'Check node,node_type,user_node_role,include_child_nodes =1,node characteristics : '||vi_user_id;
                FOR rec_node_char IN cur_node_char (rec_cri_aud.audience_criteria_id,'exclude') LOOP

                  v_e_sql_nc_str := ' SELECT node_id
                                        FROM (SELECT characteristic_id,node_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                                FROM node_characteristic)
                                       WHERE characteristic_id = '||rec_node_char.characteristic_id||'
                                         AND lower(characteristic_value = '''||rec_node_char.criteria_characteristic_value||''''; --11/08/2013
            
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

            v_stage := 'Check exclude characteristic for pax: '||vi_user_id;
            FOR rec_user_char IN cur_user_char (rec_cri_aud.audience_criteria_id, 'exclude') LOOP

              v_e_sql_char_str :=' SELECT user_id
                                     FROM (SELECT characteristic_id,user_id, REPLACE(REPLACE(characteristic_value,''('') ,'')'') characteristic_value --10/01/2015
                                             FROM user_characteristic)
                                    WHERE characteristic_id = '||rec_user_char.characteristic_id||'
                                      AND REGEXP_LIKE (lower(characteristic_value), '''||rec_user_char.criteria_characteristic_value||''') --11/08/2013
                                      AND user_id = '||vi_user_id;
            
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

          IF rec_cri_aud.first_name                  IS NULL AND    --01/07/2016 Added IF
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
           v_exclude_cnt                           <> 0    AND
           v_cri_char_cnt                          = 1     AND
           v_aud_cri_char_cnt                      > 1     THEN
           v_sql := v_sql||'('||v_e_sql||')';
          ELSE
            
              IF v_e_sql IS NOT NULL THEN
                v_sql := v_sql||' MINUS ('||v_e_sql||')';
              END IF;
              --Excludes criteria ends

              v_sql := v_sql||')';
              
          END IF;

        END IF;
        
           IF v_sql LIKE '%IN ( )%' THEN -- 04/01/2014 Bug # 52544
          v_sql := REPLACE(v_sql,'IN ( )','IN ( NULL )'); -- no pax qualified
        END IF;
        
        --dbms_output.put_line ('v_sql:'||rec_audience.audience_id||' '||v_sql);              
        BEGIN
          v_stage := 'Execute immediate';
          EXECUTE IMMEDIATE v_sql INTO v_user_id;

        EXCEPTION
          WHEN NO_DATA_FOUND THEN
            v_user_id := NULL;

        END;    
        
        IF v_user_id IS NOT NULL THEN

          BEGIN

            v_stage := 'Check pax_audience for pax: '||v_user_id;
            SELECT user_id
              INTO v_user_id
              FROM participant_audience
             WHERE user_id     = v_user_id
               AND audience_id = rec_audience.audience_id;        

          EXCEPTION
            WHEN NO_DATA_FOUND THEN
              -- 07/11/2014 Concurrency issue during self-enrolling 
              v_retun_lck := fnc_lock_control_audience(rec_audience.audience_id);      
              IF v_retun_lck = 99 THEN
                v_log_msg := 'Resource Busy';
                RAISE e_exit_pgm_lck;
              END IF;       
              -- 07/11/2014  end;
                  
              BEGIN
                v_stage := 'Find Max audience index for pax: '||v_user_id;
                SELECT participant_audience_index + 1
                  INTO v_pax_audience_index
                  FROM (SELECT RANK () OVER (PARTITION BY audience_id ORDER BY participant_audience_index DESC) AS rec_rank, 
                               participant_audience_index 
                          FROM participant_audience
                         WHERE audience_id = rec_audience.audience_id)
                 WHERE rec_rank = 1;
              EXCEPTION
                WHEN NO_DATA_FOUND THEN
                  v_pax_audience_index := 0;
              END;

              v_stage := 'Insert participant_audience for pax: '||vi_user_id;
              INSERT INTO participant_audience 
                          (user_id, 
                           audience_id, 
                           participant_audience_index, 
                           created_by, 
                           date_created) 
                   VALUES (v_user_id,
                           rec_audience.audience_id,
                           v_pax_audience_index,
                           0,
                           SYSDATE);
          
          END;

          CONTINUE audience_loop;
        
        ELSE
          v_set_delete := TRUE;
        END IF;  

      END LOOP; --<<audience_criteria_loop>>

      IF v_set_delete THEN

        BEGIN
          v_stage := 'Check pax_audience for pax: '||vi_user_id;
          SELECT user_id
            INTO v_user_id
            FROM participant_audience
           WHERE user_id     = vi_user_id
             AND audience_id = rec_audience.audience_id;

          v_stage := 'Delete criteria';           
          DELETE FROM participant_audience
           WHERE user_id     = vi_user_id
             AND audience_id = rec_audience.audience_id;
           
        EXCEPTION
          WHEN OTHERS THEN
            NULL;
        END;
    
      END IF;

    END LOOP;  --<<audience_loop>>

  END LOOP;     --<<user_loop>>

  v_stage := 'Rebuilding indexes for audience';      --02/18/2015
  FOR rec_index in cur_rebuild_index      --02/18/2015
  LOOP
        UPDATE participant_audience  
        SET   participant_audience_index = rec_index.new_index_value
        WHERE audience_id = rec_index.audience_id and
              user_id = rec_index.user_id and
              participant_audience_index = rec_index.participant_audience_index;
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

END prc_sync_pax_criteria_audience;


PROCEDURE prc_sync_audience_hierarchy
  (pi_target_node_id      IN  node.node_id%TYPE,
   pi_destination_node_id IN  node.node_id%TYPE,
   pi_child_dest_node_id  IN  node.node_id%TYPE,
   po_returncode          OUT NUMBER,
  p_out_user_data OUT SYS_REFCURSOR)
IS

/*----------------------------------------------------------------------------

  Purpose: Assign audience to Participant based on Hierarchy modification.This process
          will add/remove records to participant_audience

  Person        Date         Comments
  -----------   -----------  ---------------------------------------------------
  Arun S        11/07/2012   Initial creation   

-----------------------------------------------------------------------------*/

  CURSOR cur_user (vi_node_id user_node.node_id%TYPE) IS
    SELECT user_id
      FROM user_node
     WHERE node_id = vi_node_id;  

  CURSOR cur_user_reassign (vi_node_id     user_node.node_id%TYPE,
                            vi_des_node_id user_node.node_id%TYPE) IS
    SELECT DISTINCT user_id
      FROM user_node_history
     WHERE node_id = vi_node_id
       AND user_id IN (SELECT user_id
                         FROM user_node
                        WHERE node_id = vi_des_node_id);

  CURSOR cur_user_reassign_ch (vi_node_id     user_node.node_id%TYPE,
                               vi_node_id_des user_node.node_id%TYPE,
                               vi_node_des_ch user_node.node_id%TYPE) IS
    SELECT DISTINCT user_id
      FROM user_node_history
     WHERE node_id = vi_node_id
       AND (user_id IN (SELECT user_id
                          FROM user_node
                         WHERE node_id = vi_node_id_des)
            OR
            user_id IN (SELECT user_id
                          FROM user_node
                         WHERE node_id = vi_node_des_ch)
            );

  --Exception
  e_exit_pgm                EXCEPTION;

  --Execution log variables
  C_process_name            execution_log.process_name%TYPE  := 'prc_sync_audience_hierarchy';
  C_release_level           execution_log.release_level%TYPE := '1';
  C_severity_i              execution_log.severity%TYPE      := 'INFO';
  C_severity_e              execution_log.severity%TYPE      := 'ERROR';
  v_log_msg                 execution_log.text_line%TYPE;
  
  --Procedure variables
  v_stage                   VARCHAR2(500);
  v_node_id                 node.node_id%TYPE;
       
BEGIN

  v_stage := 'Write start to execution_log table';
  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i,
                          'Procedure Started for Parameters pi_target_node_id:'||pi_target_node_id||
                          ' pi_destination_node_id:'||pi_destination_node_id||
                          ' pi_child_dest_node_id'||pi_child_dest_node_id,
                          NULL);

  v_stage := 'Validate input node: '||pi_target_node_id;
  BEGIN
    SELECT node_id
      INTO v_node_id
      FROM node
     WHERE node_id = pi_target_node_id;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      v_log_msg := 'Input node id: '||pi_target_node_id||' not present in node table';
      RAISE e_exit_pgm;
  END;
  
 OPEN p_out_user_data FOR --G5-1511
 SELECT user_id FROM user_node where node_id = pi_target_node_id;

  IF pi_target_node_id IS NOT NULL AND pi_destination_node_id IS NULL AND pi_child_dest_node_id IS NULL THEN

    FOR rec_user IN cur_user (pi_target_node_id) LOOP
    
      v_stage := 'Call prc_sync_pax_criteria_audience for Pax Id: '||rec_user.user_id;
      prc_sync_pax_criteria_audience ( rec_user.user_id, NULL, po_returncode );
      
      IF po_returncode <> 0 THEN
        prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                                'Error in calling prc_sync_pax_criteria_audience for Pax Id: '||rec_user.user_id,
                                NULL);
      END IF;
    
    END LOOP;

  END IF;


  IF pi_target_node_id IS NOT NULL AND pi_destination_node_id IS NOT NULL AND pi_child_dest_node_id IS NULL THEN

    FOR rec_user_reassign IN cur_user_reassign (pi_target_node_id, pi_destination_node_id) LOOP

      v_stage := 'Call prc_sync_pax_criteria_audience for Pax Id: '||rec_user_reassign.user_id;
      prc_sync_pax_criteria_audience ( rec_user_reassign.user_id, NULL, po_returncode );
      
      IF po_returncode <> 0 THEN
        prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                                'Error in calling prc_sync_pax_criteria_audience for Pax Id: '||rec_user_reassign.user_id,
                                NULL);
      END IF;
    
    END LOOP;

  END IF;


  IF pi_target_node_id IS NOT NULL AND pi_destination_node_id IS NOT NULL AND pi_child_dest_node_id IS NOT NULL THEN

    FOR rec_user_reassign_ch IN cur_user_reassign_ch (pi_target_node_id, pi_destination_node_id, pi_child_dest_node_id) LOOP

      v_stage := 'Call prc_sync_pax_criteria_audience for Pax Id: '||rec_user_reassign_ch.user_id;
      prc_sync_pax_criteria_audience ( rec_user_reassign_ch.user_id, NULL, po_returncode );
      
      IF po_returncode <> 0 THEN
        prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                                'Error in calling prc_sync_pax_criteria_audience for Pax Id: '||rec_user_reassign_ch.user_id,
                                NULL);
      END IF;
    
    END LOOP;

  END IF;
    
  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i,'Procedure Completed',
                          NULL);

  po_returncode := 0;

EXCEPTION
  WHEN e_exit_pgm THEN
    po_returncode := 99;
    prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'Error at stage: '||v_stage||
                            ' Message: '||v_log_msg,
                            NULL);
    OPEN p_out_user_data FOR SELECT * FROM dual WHERE 1=2;

  WHEN OTHERS THEN
    po_returncode := 99;
    prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'Error at stage: '||v_stage||
                            ' Message: '||SQLERRM,
                            NULL);
    OPEN p_out_user_data FOR SELECT * FROM dual WHERE 1=2;

END prc_sync_audience_hierarchy;


PROCEDURE prc_refresh_criteria_audience
 (pi_audience_id IN  audience.audience_id%TYPE,
  po_returncode  OUT NUMBER,
  p_out_user_data   OUT SYS_REFCURSOR)
IS 

/*----------------------------------------------------------------------------

  Purpose: Assign audience to Participant based on audience table.This process
          will add/remove records to participant_audience

  Person        Date         Comments
  -----------   -----------  ---------------------------------------------------
  Arun S        11/07/2012    Initial creation   
  Ravi Dhanekula 4/20/2013    Changed the declaration of the variables to fetch from the tables instead of hard coding the length.
  Arun S        04/26/2013    Changes to below issue fix
                              There is an issue when defining criteria audience only with default search criteria 
                              (Define audience in Add audience page with default selected search criteria alone - issue in
                              submitting page)  
  Ravi Dhanekula  12/02/2013  run process for one audience id or  all audience id
  Chidamba        16/12/2013  Fix - Bug 49776# Making characteristic_value check 
                              as case insensitive by adding LOWER()  
  Ravi Dhanekula  03/27/2014  Fixed the bug # 52493              
  Chidamba        07/11/2014  Bug 53134 - prc_sync_pax_criteria_audience DUPE error
                              fix to overcome Concurrency issue during self-enrolling   
  Swati           09/18/2014  Bug 56636 - pkg_build_audience.prc_refresh_criteria_audience is failing when search_type='exclude'         
  Ravi Dhanekula  12/31/2014  Bug # 58868 - Fixed issue introduced with the bug fix for 56636.    
  Swati           03/13/2015  Bug 60050 - PKG_BUILD_AUDIENCE not using EXCLUDE values correctly
  Swati           06/09/2015  Bug 62548 - PKG_BUILD_AUDIENCE.prc_refresh_criteria_audience issues
  nagarajs        10/01/2015  Bug 64054  - When node or user characteristic value has braces it's not populating the eligible users into participant_audience table               
  nagarajs        12/11/2015  Bug 64884 - Refresh Audience Process Failing
  nagarajs        12/16/2015  Bug 64991 - Recreate Criteria Audience Participant process keep on running for long time
  nagarajs       05/24/2016   Bug 66797 - System is excluding user characteristics restriction while creating criteria audience   
  chidamba       03/14/2018   Bug 75683/G6-3940 - fix single-quote issue in node_name
  Loganathan 	 04/24/2019   Bug 72697 - Application is not considering the audience whose ‘Search Criteria’ values contain special char (+) 
-----------------------------------------------------------------------------*/

     
 CURSOR cur_audience (vi_aud_id audience.audience_id%TYPE) IS
     SELECT audience_id
       FROM audience
      WHERE list_type   = 'criteria'
        AND audience_id = NVL(vi_aud_id, audience_id)
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
  --replace(node_name, '''', '"')node_name, --02/28/2014
  replace(node_name, '''', '''''')node_name, --06/09/2015 Bug 62548
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
           lower(REPLACE(REPLACE(criteria_characteristic_value,'(') ,')')) criteria_characteristic_value --10/01/2015
           --lower(ac.criteria_characteristic_value)  criteria_characteristic_value --16/12/2013 --10/01/2015
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
  v_position_type           participant_employer.position_type%TYPE; --4/20/2013
  v_dep_type                participant_employer.department_type%TYPE; --4/20/2013
  v_lang_id                 VARCHAR2(40);
  v_node_id                 VARCHAR2(40);        
  v_node_name               node.NAME%TYPE; --4/20/2013
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
  
  v_e_position_type         participant_employer.position_type%TYPE; --4/20/2013
  v_e_dep_type              participant_employer.department_type%TYPE; --4/20/2013
  v_e_node_id               VARCHAR2(40);        
  v_e_node_name             node.NAME%TYPE; --4/20/2013
  v_e_node_type_id          VARCHAR2(40);   
  v_e_user_node_role        VARCHAR2(40);
  v_e_cri_char_cnt          NUMBER; --09/18/2014 
--  v_e_max_count             NUMBER; --09/18/2014 --12/31/2014
    
  v_user_id                 participant_audience.user_id%TYPE;
  v_pax_audience_index      participant_audience.participant_audience_index%TYPE;
  v_insert_rec              VARCHAR2(1);
  v_retun_lck               NUMBER;
 
  TYPE user_id_type IS TABLE OF participant_audience.user_id%TYPE;
  
  tbl_user_id      user_id_type; 
  tbl_ex_user_id   user_id_type; --03/13/2015 Bug 60050  
  
BEGIN

  v_stage := 'Write start to execution_log table';
  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i,
                          'Procedure Started for Parameter: '||pi_audience_id,
                          NULL);
                          
 DELETE FROM tmp_audience_user_id; --08/23/2016
    
   INSERT INTO tmp_audience_user_id  --08/23/2016
    (audience_id, user_id)
    SELECT audience_id, user_id 
      FROM participant_audience WHERE pi_audience_id IS NOT NULL AND audience_id = pi_audience_id;

--   BEGIN --12/2/2013
--     SELECT audience_id
--       INTO v_audience_id
--       FROM audience
--      WHERE list_type   = 'criteria'
--        AND audience_id = pi_audience_id;
--   EXCEPTION
--     WHEN NO_DATA_FOUND THEN
--       v_log_msg := 'Criteria id: '||pi_audience_id||' not present in audience table';
--       RAISE e_exit_pgm;
--   END;
 FOR rec_audience IN cur_audience (pi_audience_id) LOOP    --12/2/2013
    
    v_audience_id := rec_audience.audience_id;
    v_rec_cnt     := 0;
    
   v_stage := 'Delete global temporary table gtt_pax_audience';
   DELETE FROM gtt_pax_audience;
   
   DELETE FROM gtt_ex_pax_audience; --03/13/2015 Bug 60050  
    
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
        v_e_cri_char_cnt   :=0; --09/18/2014
        
      SELECT COUNT (1)
        INTO v_cri_char_cnt
        FROM audience_criteria_char
       WHERE audience_criteria_id = rec_cri_aud.audience_criteria_id;

      --All criteria (default) and no criteria char found       --04/26/2013
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
                            AND a.is_active = 1
                            AND b.status = ''active''';

          v_sql := v_sql_all;
        
      ELSE  

       IF v_e_cri_char_cnt = 1 THEN  --09/18/2014
       v_sql_all := ' SELECT a.user_id
                           FROM application_user a, 
                                participant b 
                          WHERE a.user_id = b.user_id
                            AND a.is_active = 1
                            AND b.status = ''active''';
       ELSE --09/18/2014  
        v_sql_all := ' SELECT a.user_id
                         FROM application_user a, 
                              participant b 
                        WHERE a.user_id   = b.user_id
                          AND a.is_active = 1
                          AND b.status    = ''active''
                          AND a.user_id   IN ( ';
       END IF;--09/18/2014
  
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
          v_node_name := ''''||REPLACE(rec_cri_aud.node_name,'','''')||''''; -- 03/14/2018
          --v_node_name := ''''||rec_cri_aud.node_name||''''; -- 03/14/2018
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
                                     AND lower(characteristic_value) = '''||rec_node_char.criteria_characteristic_value||''''; --16/12/2013                                     
            
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
                                   AND REGEXP_LIKE (lower(characteristic_value),''(^|,)''||REGEXP_REPLACE('''||rec_user_char.criteria_characteristic_value||''', ''([-~`!@#$%^&*\(\)\\{}_+=|''''";:,./?])'', ''[\1]'')||''(,|$)'') '; --16/12/2013 --05/24/2016 --04/24/2019
            
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
           v_e_node_name := ''''||REPLACE(rec_cri_aud.exclude_node_name,'','''')||''''; -- 03/14/2018
           -- v_e_node_name := ''''||rec_cri_aud.exclude_node_name||''''; -- 03/14/2018
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
                                       AND lower(characteristic_value) = '''||rec_node_char.criteria_characteristic_value||''''; --12/16/2013
            
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
        
        /*IF v_e_sql IS NOT NULL THEN
          v_sql := v_sql||' MINUS ('||v_e_sql||')';
        END IF; */ --03/13/2015 Bug 60050
        
        --Excludes criteria ends

         IF v_e_cri_char_cnt =0  then --09/18/2014  
            v_sql := v_sql||')';
         END IF;

      END IF; --ALL
              
      IF v_sql LIKE '%IN ( )%' THEN -- 03/27/2014 Bug # 52493
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
        WHEN OTHERS THEN --03/13/2015 Bug 60050  
          po_returncode := 99;
          prc_execution_log_entry(C_process_name, C_release_level, C_severity_e,
                            'Error at stage: '||v_stage||
                            ' Message: '||SQLERRM,
                            NULL);
      END;   
   
    --03/13/2015 Bug 60050 Starts
    --Insert Users of Exclude Criteria
     IF v_e_sql IS NOT NULL THEN        --12/11/2015 
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
    
    --03/13/2015 Bug 60050 Ends
     END IF;

    END LOOP;
    
    DELETE FROM gtt_pax_audience
        WHERE user_id IN (SELECT user_id FROM gtt_ex_pax_audience); --03/13/2015 Bug 60050 
--    
--    SELECT MAX (count_nm) max_nm --09/18/2014 --12/31/2014
--      INTO v_e_max_count
--      FROM (  SELECT user_id, COUNT (1) count_nm
--                FROM gtt_pax_audience
--               WHERE audience_id = v_audience_id
--            GROUP BY user_id);


--   -- DBMS_OUTPUT.put_line('v_e_max_count:'||v_e_max_count); --12/31/2014
--    DELETE FROM gtt_pax_audience gpa --09/18/2014  
--      WHERE     audience_id = v_audience_id
--            AND user_id IN (  SELECT user_id
--                                FROM gtt_pax_audience
--                               WHERE audience_id = v_audience_id
--                            GROUP BY user_id, audience_id
--                              HAVING COUNT (1) < v_e_max_count); 
                              
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
     
      -- 07/11/2014 Concurrency issue during self-enrolling 
      v_retun_lck := fnc_lock_control_audience(rec_audience.audience_id);      
      IF v_retun_lck = 99 THEN
       v_log_msg := 'Resource Busy';
       RAISE e_exit_pgm_lck;
      END IF;       
      -- 07/11/2014  end;

      BEGIN
        v_stage := 'Find Max audience index for audience id: '||v_audience_id;
        SELECT participant_audience_index + 1
          INTO v_pax_audience_index
          FROM (SELECT RANK () OVER (PARTITION BY audience_id ORDER BY participant_audience_index DESC) AS rec_rank, 
                       participant_audience_index 
                  FROM participant_audience
                 WHERE audience_id = v_audience_id)
         WHERE rec_rank = 1;
      EXCEPTION
        WHEN NO_DATA_FOUND THEN
          v_pax_audience_index := 0;
      END;

      v_stage := 'Insert participant_audience for audience id: '||v_audience_id;    
      INSERT INTO participant_audience
                  (user_id, audience_id, participant_audience_index, created_by, date_created)                       
           SELECT pax_aud.user_id, pax_aud.audience_id, (v_pax_audience_index + ROWNUM - 1), 0, SYSDATE
             FROM (SELECT DISTINCT user_id, audience_id
                     FROM gtt_pax_audience gpa
                    WHERE gpa.audience_id = v_audience_id
                      AND NOT EXISTS (SELECT 'X'
                                        FROM participant_audience pa
                                       WHERE pa.user_id     = gpa.user_id
                                         AND pa.audience_id = gpa.audience_id
                                         AND pa.audience_id = v_audience_id)
                   ) pax_aud;  
                   
       v_stage := 'Update correct participant audience_index';
      /*UPDATE participant_audience pa  --06/09/2015 Bug 62548 --12/16/2015
          SET  participant_audience_index = (SELECT participant_audience_index
                                               FROM (SELECT user_id,RANK () OVER (PARTITION BY audience_id ORDER BY user_id) - 1 participant_audience_index
                                                       FROM participant_audience ppa
                                                      WHERE ppa.audience_id = v_audience_id) ppa
                                              WHERE ppa.user_id = pa.user_id)
         WHERE pa.audience_id = v_audience_id;*/
       
      MERGE INTO participant_audience PA  --12/16/2015
      USING (SELECT user_id,
                    audience_id,
                    RANK () OVER (PARTITION BY audience_id ORDER BY user_id) - 1 participant_audience_index 
               FROM participant_audience ppa 
              WHERE ppa.audience_id = v_audience_id) ppa
         ON (pa.user_id = ppa.user_id
           AND pa.audience_id = ppa.audience_id )
      WHEN MATCHED THEN 
      UPDATE SET participant_audience_index = ppa.participant_audience_index;                  
    END IF;
    
  END LOOP; --12/2/2013
    
  prc_execution_log_entry(C_process_name, C_release_level, C_severity_i,'Procedure Completed',
                          NULL);
 
   IF (pi_audience_id IS NOT NULL) THEN
            
      OPEN p_out_user_data FOR --07/07/2016
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

END prc_refresh_criteria_audience;

END;
/