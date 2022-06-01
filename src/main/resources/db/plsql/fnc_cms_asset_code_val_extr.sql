CREATE OR REPLACE FUNCTION fnc_cms_asset_code_val_extr(p_in_assetcode IN VARCHAR2,p_in_key IN VARCHAR2,p_in_local IN VARCHAR2)
  RETURN VARCHAR2 IS
/*******************************************************************************
-- Purpose: Abstract function that returns actual value for the give in parameters
-- MODIFICATION HISTORY
-- Person      Date       Comments
-- ---------   ------     -------------------------------------------
-- chidamba   12/15/2011  Initial creation
-- chidamba   01/04/2012  Change in return value for exception NO_DATA_FOUND  
-- Chidamba   01/27/2012  Changes to check p_in_local hierarchy ('de_DE','de') and get cms data based on the hierachy
              if no content available retruns 'en' as default content value
*******************************************************************************/
  v_assetcode_value VARCHAR2(300);
  v_default_locale  VARCHAR2(25) := 'en_US';
  
BEGIN


  BEGIN

    SELECT cms_value
      INTO v_assetcode_value
      FROM vw_cms_asset_value
     WHERE asset_code = p_in_assetcode
       AND key = UPPER(p_in_key)
       AND locale = p_in_local;

  EXCEPTION
    WHEN no_data_found THEN
      
      BEGIN

        SELECT cms_value
          INTO v_assetcode_value
          FROM vw_cms_asset_value
         WHERE asset_code = p_in_assetcode
           AND key = UPPER(p_in_key)
           AND locale = p_in_local;--substr(p_in_local,1,INSTR(p_in_local,'_',1,1)-1);
      
      EXCEPTION
        WHEN no_data_found THEN
          
          BEGIN

            SELECT cms_value
              INTO v_assetcode_value
              FROM vw_cms_asset_value
             WHERE asset_code = p_in_assetcode
               AND key = UPPER(p_in_key)
               AND locale = v_default_locale;
          
          END;
      
      END;             
      
  END;
     
  RETURN v_assetcode_value;
EXCEPTION
  WHEN NO_DATA_FOUND then
    v_assetcode_value := null;
--    RETURN NVL(v_assetcode_value, 'Characteristic Not Applicable');           --01/04/2012
      RETURN NVL(v_assetcode_value, p_in_assetcode||'+'||p_in_key||'+'||p_in_local);
  WHEN OTHERS THEN
    v_assetcode_value := 'X';
    RETURN v_assetcode_value;
END;
/