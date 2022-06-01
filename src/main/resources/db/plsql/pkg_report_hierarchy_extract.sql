CREATE OR REPLACE PACKAGE pkg_rpt_hierarchy_extract IS

/******************************************************************************
  NAME:       pkg_rpt_hierarchy_extract
  PURPOSE:    Extracts the Heirarchy of an Organization

  Date        Author           Description
  ----------  ---------------  ------------------------------------------------
  12/13/2011  Arun S           Initial Creation
  12/29/2011  chidamba         Bug # 35717 - Added new p_in_locale get locale                 
******************************************************************************/

  FUNCTION fnc_get_characteristic
  (pi_node_id              IN NUMBER)
  RETURN VARCHAR2;

  PROCEDURE prc_hierarchy_extract
  (p_in_locale                IN  VARCHAR2,   -- 12/29/2011 added for Bug # 35717
   p_out_error_message        OUT VARCHAR2,
   p_out_return_code          OUT VARCHAR2,   
   p_out_result_set           OUT sys_refcursor);
 
END;
/
CREATE OR REPLACE PACKAGE BODY pkg_rpt_hierarchy_extract IS

/******************************************************************************
  NAME:       pkg_rpt_hierarchy_extract
  PURPOSE:    Extracts the Heirarchy of an Organization

  Date        Author           Description
  ----------  ---------------  ------------------------------------------------
  12/13/2011  Arun S           Initial Creation

******************************************************************************/

FUNCTION fnc_get_characteristic
(pi_node_id              IN NUMBER)
 RETURN VARCHAR2 IS
  v_count        NUMBER(10) := 1;
  v_char         VARCHAR2(2000);
  v_char_val     VARCHAR2(2000);
  c_delimiter CONSTANT VARCHAR2(1) := '|' ;

  CURSOR cur_char IS
    SELECT SUBSTR(fnc_cms_asset_code_value(c.cm_asset_code),1,120)||c_delimiter||nc.characteristic_value||c_delimiter characteristic_val
      FROM node_characteristic nc,
           characteristic c
     WHERE nc.characteristic_id = c.characteristic_id
       AND nc.node_id = pi_node_id
     ORDER BY node_characteristic_id;

  rec_char cur_char%ROWTYPE;

BEGIN

  FOR rec_char IN cur_char LOOP
    v_char := rec_char.characteristic_val;

    v_char_val := v_char_val||v_char;
    
    IF v_count >= 5 THEN
      EXIT;
    END IF;   
    v_count:= v_count + 1;   
  END LOOP;

  RETURN v_char_val;

EXCEPTION
  WHEN OTHERS THEN
    v_char_val := NULL;
    RETURN v_char_val;  
END; 
  
PROCEDURE prc_hierarchy_extract
(p_in_locale                IN  VARCHAR2,   -- 12/29/2011 added for Bug # 35717
 p_out_error_message        OUT VARCHAR2,
 p_out_return_code          OUT VARCHAR2,
 p_out_result_set           OUT sys_refcursor) IS

/******************************************************************************
  NAME:       prc_hierarchy_extract
  PURPOSE:    Extracts the Heirarchy of an Organization

  Date        Author           Description
  ----------  ---------------  ------------------------------------------------
  12/13/2011  Arun S           Created this procedure.
  12/29/2011  chidamba         Bug # 35717 - Add fnc_cms_asset_code_val_extr to translation report headers content in extract 
                               Added new p_in_locale get locale  e.g- en,fr
  03/08/2012  chidamba         Bug # 39795 - Remove references to 'node' in report extracts and replace as 'Org Unit'                 
******************************************************************************/

 c_delimiter CONSTANT VARCHAR2(1) := '|' ;
 v_release_level NUMBER;

BEGIN
  
  v_release_level := 0;               
  /*Start fix'NODE' as 'ORG_UNIT' */
  OPEN p_out_result_set FOR
    SELECT textline 
      FROM (SELECT 1,fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','RECORD_TYPE', p_in_locale)||c_delimiter||                --'Record type'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_NAME', p_in_locale)||c_delimiter||        --'Node name'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','OLD_ORG_UNIT_NAME', p_in_locale)||c_delimiter||          --'Old Node name'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','MOVE_ORG_UNIT_NAME', p_in_locale)||c_delimiter||         --'Move to Node name'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','DESCRIPTION', p_in_locale)||c_delimiter||             --'Description'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_TYPE', p_in_locale)||c_delimiter||              --'Node type'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','PARENT_ORG_UNIT_NAME', p_in_locale)||c_delimiter||        --'Parent Node name'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','OLD_PARENT_ORG_UNIT_NAME', p_in_locale)||c_delimiter||   --'Old Parent Node name'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_CHAR1_NAME', p_in_locale)||c_delimiter||        --'Node characteristic1 name'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_CHAR1_VALUE', p_in_locale)||c_delimiter||        --'Node characteristic1 value'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_CHAR2_NAME', p_in_locale)||c_delimiter||         --'Node characteristic2 name'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_CHAR2_VALUE', p_in_locale)||c_delimiter||        --'Node characteristic2 value'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_CHAR3_NAME', p_in_locale)||c_delimiter||         --'Node characteristic3 name'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_CHAR3_VALUE', p_in_locale)||c_delimiter||        --'Node characteristic3 value'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_CHAR4_NAME', p_in_locale)||c_delimiter||         --'Node characteristic4 name'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_CHAR4_VALUE', p_in_locale)||c_delimiter||        --'Node characteristic4 value'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_CHAR5_NAME', p_in_locale)||c_delimiter||         --'Node characteristic5 name'||c_delimiter||
                   fnc_cms_asset_code_val_extr('extract.extractprchierachy.detail','ORG_UNIT_CHAR5_VALUE', p_in_locale)                       --'Node characteristic5 value'
                   textline
              FROM dual
  /*End fix'NODE' as 'ORG_UNIT' */
             UNION  
             SELECT (rownum+1),
                    'U'||c_delimiter||
                    nc.name||c_delimiter||
                    NULL||c_delimiter||
                    NULL||c_delimiter||
                    nc.description||c_delimiter||
                    SUBSTR(fnc_cms_asset_code_value(nt.cm_asset_code),1,120)||c_delimiter||
                    np.name||c_delimiter||
                    NULL||c_delimiter||
                    fnc_get_characteristic(nc.node_id)
               FROM node nc,
                    node_type nt,
                    (SELECT node_id, name 
                       FROM node) np
              WHERE nc.node_type_id   = nt.node_type_id
                AND nc.parent_node_id = np.node_id (+)
                AND nc.is_deleted     = 0
            CONNECT BY PRIOR nc.node_id = nc.parent_node_id
              START WITH parent_node_id IS NULL);

   p_out_return_code := '00';

EXCEPTION
    WHEN OTHERS  THEN
       p_out_return_code := '99';
       p_out_error_message := SQLERRM;
       prc_execution_log_entry('prc_hierarchy_extract', v_release_level, 'ERROR', SQLERRM, NULL);
       OPEN p_out_result_set FOR SELECT NULL FROM dual;
END;

END;
/
