CREATE OR REPLACE FUNCTION fnc_cms_picklist_code
  ( p_in_pl_name IN VARCHAR2,
    p_in_pl_itemname IN VARCHAR2,
    p_in_locale IN VARCHAR2)
  RETURN  VARCHAR2 IS
--
-- To modify this template, edit file FUNC.TXT in TEMPLATE 
-- directory of SQL Navigator
--
-- Purpose: Briefly explain the functionality of the function
--
-- MODIFICATION HISTORY
-- Person      Date    Comments
-- ---------   ------  -------------------------------------------       
   v_content_id       NUMBER(18) ;
   v_pl_item_code     VARCHAR2(40) ;
   -- Declare program variables as shown above
BEGIN 
     SELECT cd.content_id
       INTO v_content_id
       FROM cms_content_data cd
      WHERE cd.content_id IN
           (SELECT c.id
              FROM cms_content c
             WHERE locale = p_in_locale AND c.content_key_id IN
                  (SELECT ck.id
                     FROM cms_content_key ck
                    WHERE  ck.asset_id IN
                        (SELECT a.id
                           FROM cms_asset a
                          WHERE a.code = p_in_pl_name) --'picklist.emailtype.items'
                   )
               AND c.content_status = 'Live'
            )
        AND cd.KEY = ('NAME')
        AND UPPER(DBMS_LOB.substr(cd.value,300,1)) = UPPER(p_in_pl_itemname) ;
   
   
     SELECT DBMS_LOB.substr(cd.value,40,1)
       INTO v_pl_item_code
       FROM cms_content_data cd
      WHERE cd.content_id = v_content_id
        AND cd.KEY = ('CODE')
        AND cd.content_id = v_content_id ;
    RETURN v_pl_item_code ;
EXCEPTION
   WHEN others THEN
      prc_execution_log_entry('FNC_CMS_PICKLIST_CODE',1,'ERROR',p_in_pl_name||SQLERRM,null);
END;
/