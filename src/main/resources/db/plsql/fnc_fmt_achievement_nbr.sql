CREATE OR REPLACE FUNCTION fnc_fmt_achievement_nbr
( p_promotion_id    IN  promotion.promotion_id%TYPE,    --promo_goalquest 
  p_achievement_nbr IN  NUMBER,
  p_suffix          IN  VARCHAR2  DEFAULT NULL
) RETURN VARCHAR2
IS
/*******************************************************************************
-- Purpose: Formats the input achievement number based upon the promotion achievement precision. 
--          Suffix appended when achievement number is not null.
--
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
--  J. Flees   04/14/2011  Bug Fix: 34777, Format achievement numbers
--                         based on the promotion achievement precision value.
--  J. Flees   08/30/2011  Converted process to dynamically query table based on installed module.
--  Arun S     11/05/2012  Modified p_promotion_id in param data type declaration
                           to use promotion table instead of promo_goalquest
*******************************************************************************/  
   c_process_name          CONSTANT execution_log.process_name%TYPE  := 'fnc_fmt_achievement_nbr';
   c_release_level         CONSTANT execution_log.release_level%TYPE := '1';

   e_null_input            EXCEPTION;

   v_fmt_achievement       VARCHAR2(30);
   v_gq_installed          os_propertyset.boolean_val%TYPE;
   v_cp_installed          os_propertyset.boolean_val%TYPE;
   v_cur_fmt_achievement   SYS_REFCURSOR;
   v_sql_stmt              VARCHAR2(1000);

BEGIN
   IF (p_achievement_nbr IS NULL) THEN
      -- skip processing when input value is null
      RAISE e_null_input;
   END IF;

   -- initialize variables
   v_gq_installed := fnc_get_installed_module('install.goalquest');
   v_cp_installed := fnc_get_installed_module('install.challengepoint');

   v_sql_stmt := -- use WITH clause to input parameters once independent of number of sub-selects
                  'WITH i AS '
               || '(SELECT TO_NUMBER(:p_promotion_id) AS promotion_id,'
               ||        ' TO_NUMBER(:p_achievement_nbr) AS achievement_nbr'
               ||   ' FROM DUAL'
               || ')'
                  -- format number based on promotion precision
               || 'SELECT DECODE(f.achievement_precision,'
               ||         ' ''zero'', TRIM(TO_CHAR(i.achievement_nbr, ''9999999999999990'')),'
               ||         ' ''one'',  TRIM(TO_CHAR(i.achievement_nbr, ''9999999999999990.9'')),'
               ||         ' ''two'',  TRIM(TO_CHAR(i.achievement_nbr, ''9999999999999990.99'')),'
               ||         ' TRIM(TO_CHAR(i.achievement_nbr))'
               ||       ' ) AS fmt_achievement'
               ||  ' FROM i,'
                           -- pass through when promo record not found
               ||       ' ( SELECT 99 AS seq_nbr,'
               ||                ' CAST(NULL AS VARCHAR2(30)) AS achievement_precision'
               ||           ' FROM dual';

   -- add goal quest table to dynamic SQL statement
   IF (v_gq_installed = 1) THEN
      v_sql_stmt := v_sql_stmt || ' UNION ALL SELECT 1, pg.achievement_precision FROM promo_goalquest pg, i WHERE pg.promotion_id = i.promotion_id';
   END IF;

   IF (v_cp_installed = 1) THEN
      v_sql_stmt := v_sql_stmt || ' UNION ALL SELECT 1, pc.achievement_precision FROM promo_challengepoint pc, i WHERE pc.promotion_id = i.promotion_id';
   END IF;

   -- conclude SQL statement
   v_sql_stmt := v_sql_stmt || ') f ORDER BY f.seq_nbr';

   -- format achievement based on input parameters and dynamic SQL
    OPEN v_cur_fmt_achievement
     FOR v_sql_stmt
   USING p_promotion_id, 
         p_achievement_nbr;
   FETCH v_cur_fmt_achievement INTO v_fmt_achievement;
   CLOSE v_cur_fmt_achievement;

   IF (v_fmt_achievement IS NOT NULL) THEN
      v_fmt_achievement := v_fmt_achievement || p_suffix;
   END IF;

   RETURN v_fmt_achievement;
EXCEPTION
   WHEN e_null_input THEN
      RETURN NULL;

   WHEN OTHERS THEN
      prc_execution_log_entry
      ( c_process_name,
        c_release_level,
        'ERROR',
        'p_promotion_id >' || p_promotion_id
           || '<, p_achievement_nbr >' || p_achievement_nbr
           || '<, p_suffix >' || p_suffix
           || '< SQLERRM> ' || SQLERRM
           || '<, v_sql_stmt >' || v_sql_stmt
           || '<',
        NULL
      );
      RETURN NULL;

END fnc_fmt_achievement_nbr;
/
