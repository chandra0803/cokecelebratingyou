CREATE OR REPLACE FUNCTION fnc_get_osp_int
( i_entity_name   IN os_propertyset.entity_name%TYPE
) RETURN os_propertyset.int_val%TYPE
  RESULT_CACHE
  RELIES_ON(os_propertyset)
IS
/*------------------------------------------------------------------------------
   Purpose: Returns the integer OS property set value associated with the input entity name.
            Returns NULL when no record found.

   MODIFICATION HISTORY
   Person      Date        Comments
   ---------   ------      -------------------------------------------
   J.Flees     10/17/2016  Initial version.
------------------------------------------------------------------------------*/
   gc_error             CONSTANT execution_log.severity%TYPE := pkg_const.gc_error;

   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('fnc_get_osp_int');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;

   v_msg                execution_log.text_line%TYPE;
   v_int_val            os_propertyset.int_val%TYPE;

   CURSOR cur_propertyset_int
   ( cv_entity_name  os_propertyset.entity_name%TYPE
   ) IS
   SELECT ops.int_val
     FROM os_propertyset ops 
    WHERE ops.entity_name = cv_entity_name
      ;
BEGIN
   v_msg := 'OPEN cur_propertyset_int: i_entity_name >' || i_entity_name || '<';
   OPEN cur_propertyset_int(i_entity_name);
   v_msg := 'FETCH cur_propertyset_int: i_entity_name >' || i_entity_name || '<';
   FETCH cur_propertyset_int INTO v_int_val;
   CLOSE cur_propertyset_int;

   RETURN v_int_val;

EXCEPTION   
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, gc_error, v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END fnc_get_osp_int;
/
