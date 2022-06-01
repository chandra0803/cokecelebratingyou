CREATE OR REPLACE FUNCTION fnc_get_osp_bool
( i_entity_name   IN os_propertyset.entity_name%TYPE
) RETURN os_propertyset.boolean_val%TYPE
IS
/*------------------------------------------------------------------------------
   Purpose: Returns the boolean OS property set value associated with the input entity name.
            Returns NULL when no record found.

   MODIFICATION HISTORY
   Person      Date        Comments
   ---------   ------      -------------------------------------------
   J.Flees     11/18/2011  Initial version.

------------------------------------------------------------------------------*/
   c_process_name       CONSTANT execution_log.process_name%TYPE := UPPER('fnc_get_osp_bool');
   c_release_level      CONSTANT execution_log.release_level%TYPE := 1.0;

   v_msg                execution_log.text_line%TYPE;
   v_boolean_val        os_propertyset.boolean_val%TYPE;

   CURSOR cur_propertyset_boolean
   ( cv_entity_name  os_propertyset.entity_name%TYPE
   ) IS
   SELECT boolean_val AS terms_used
     FROM os_propertyset
    WHERE entity_name = cv_entity_name
      ;
BEGIN
   v_msg := 'OPEN cur_propertyset_boolean: i_entity_name >' || i_entity_name || '<';
   OPEN cur_propertyset_boolean(i_entity_name);
   v_msg := 'FETCH cur_propertyset_boolean: i_entity_name >' || i_entity_name || '<';
   FETCH cur_propertyset_boolean INTO v_boolean_val;
   CLOSE cur_propertyset_boolean;

   RETURN v_boolean_val;

EXCEPTION   
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg || ', ' || SQLERRM, NULL);
      RAISE;
END fnc_get_osp_bool;
/