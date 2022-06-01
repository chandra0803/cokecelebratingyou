CREATE OR REPLACE FUNCTION fnc_cms_language_validation
  (p_in_assetcode IN VARCHAR2,
   p_in_language  IN VARCHAR2)
  RETURN VARCHAR2 IS

/*******************************************************************************
-- Purpose: Function that validates language with CM
  
-- MODIFICATION HISTORY
-- Person       Date         Comments
-- ---------    ----------   --------------------------------------------------
-- Arun S       12/09/2011   Initial Creation
    
*******************************************************************************/

  v_content_data_value VARCHAR2(300);

BEGIN

  SELECT DBMS_LOB.substr(ccd.value,300, 1)
    INTO v_content_data_value
    FROM cms_asset ca,
         cms_content_key cck,
         cms_content cc,
         cms_content_data ccd
   WHERE ca.id   = cck.asset_id
     AND cck.id  = cc.content_key_id
     AND cc.id   = ccd.content_id
     AND ccd.key = 'CODE'
     AND cc.content_status = 'Live'
     AND ca.code = p_in_assetcode   --'default.locale.items'
     AND DBMS_LOB.substr(ccd.value,300, 1) = p_in_language  --'en'
     AND ROWNUM = 1;  

  RETURN v_content_data_value;

EXCEPTION
  WHEN OTHERS THEN
    v_content_data_value := 'X';
    RETURN v_content_data_value;

END;
/