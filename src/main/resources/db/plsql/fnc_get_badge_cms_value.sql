CREATE OR REPLACE FUNCTION fnc_get_badge_cms_value
( badge_rule_id     IN NUMBER,
  locale    IN VARCHAR2  
) RETURN VARCHAR2 IS
/*******************************************************************************
 Purpose:  Places the user name into a standard format

 Person         Date        Comments
 -----------    ----------  -----------------------------------------------------
 KrishnaDeepika 05/14/2015  Initial version.
*******************************************************************************/
  c_process_name         CONSTANT execution_log.process_name%type  := 'fnc_get_badge_cms_value';
  c_release_level        CONSTANT execution_log.release_level%type := '1';
  v_cms_value            VARCHAR2 (4000 Char);

BEGIN

SELECT cms_value
  INTO v_cms_value
  FROM vw_cms_asset_value v, badge_rule br
 WHERE v.asset_code = br.badge_name 
   AND locale = locale 
   AND br.badge_rule_id = badge_rule_id;
 
 RETURN v_cms_value;

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR',
             'v_cms_value :' || v_cms_value || SQLCODE || ', ' || SQLERRM,
         NULL);

     RETURN NULL;

END fnc_get_badge_cms_value;
/
