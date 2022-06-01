CREATE OR REPLACE FUNCTION fnc_cms_picklist_value
  ( p_in_pl_name IN VARCHAR2,
    p_in_pl_itemname IN VARCHAR2,
    p_in_locale IN VARCHAR2)
  RETURN  VARCHAR2 IS
--
-- Purpose: Fetch the value for any pick list item
--
-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ------      -------------------------------------------      
-- Raju N      01/27/2006  creation
-- Matt L      10/18/2007  Changed v_pl_item_code to VARCHAR2(40) from VARCHAR2(30) to fix bug 16193.
-- Ravi Dhanekula 09/05/2012  Added Locale into the inputs
-- Chidamba       10/05/2013  Modify to use default local, if input local doen't exists in the system  
--Ravi Dhanekula 01/07/2014 Fixed the issue with the previous change.
   v_content_id       NUMBER(18) ;
   v_pl_item_code     VARCHAR2(40) ;
   v_default_local    VARCHAR2(40) := FNC_GET_SYSTEM_VARIABLE('default.language','Default Language');
   
   -- Declare program variables as shown above
BEGIN 
     
     BEGIN
     SELECT cd.content_id
       INTO v_content_id
       FROM cms_content_data cd
      WHERE cd.content_id IN
           (SELECT c.id
              FROM cms_content c
              --WHERE locale = NVL(substr(p_in_locale,1,INSTR(p_in_locale,'_',1,1)-1),p_in_locale)--09/05/2012
              --WHERE locale = p_in_locale--01/08/2013
                --WHERE locale = DECODE(p_in_locale,locale,p_in_locale,v_default_local) --10/05/2013--01/07/2014
              WHERE locale = p_in_locale--01/07/2014
             AND c.content_key_id IN
                  (SELECT ck.id
                     FROM cms_content_key ck
                    WHERE  ck.asset_id IN
                        (SELECT a.id
                           FROM cms_asset a
                          WHERE a.code = p_in_pl_name) --'picklist.emailtype.items'
                   )
               AND c.content_status = 'Live'
            )
        AND cd.KEY = ('CODE')
        AND UPPER(DBMS_LOB.substr(cd.value,300,1)) = UPPER(p_in_pl_itemname) ;
    EXCEPTION WHEN NO_DATA_FOUND THEN
    SELECT cd.content_id
       INTO v_content_id
       FROM cms_content_data cd
      WHERE cd.content_id IN
           (SELECT c.id
              FROM cms_content c           
              WHERE locale = v_default_local
             AND c.content_key_id IN
                  (SELECT ck.id
                     FROM cms_content_key ck
                    WHERE  ck.asset_id IN
                        (SELECT a.id
                           FROM cms_asset a
                          WHERE a.code = p_in_pl_name) --'picklist.emailtype.items'
                   )
               AND c.content_status = 'Live'
            )
        AND cd.KEY = ('CODE')
        AND UPPER(DBMS_LOB.substr(cd.value,300,1)) = UPPER(p_in_pl_itemname) ;
        END;
   
     SELECT DBMS_LOB.substr(cd.value,40,1)
       INTO v_pl_item_code
       FROM cms_content_data cd
      WHERE cd.content_id = v_content_id
        AND cd.KEY = ('NAME')
        AND cd.content_id = v_content_id ;
    RETURN v_pl_item_code ;
EXCEPTION
   WHEN others THEN
   RETURN '' ;
END;
/