CREATE OR REPLACE FUNCTION fnc_cms_asset_code_value(p_in_assetcode IN VARCHAR2)
  RETURN VARCHAR2 IS
/*******************************************************************************
  -- Purpose: Abstract function that returns actual value for the give
  --          CM asset code.
  -- MODIFICATION HISTORY
  -- Person      Date       Comments
  -- ---------   ------     -------------------------------------------
  -- Raju N      08/01/2006 inccreased the length of the retuned value from 
  --                        30 to 300
  -- Arun S      03/16/2009 Bug 19713 Fix,Populated 'Characteristic Not Applicable' instead of null 
  --                        to Remove empty columns from Report extract  
  --nagarajs    01/05/2016 Bug 65026 - Admin View/Budget Balance Report : There is a mismatch in values 
  --                        between the summary table and the extracted sheet.
*******************************************************************************/
  v_assetcode_value VARCHAR2(300);

BEGIN
  SELECT DBMS_LOB.substr(cd.value, 300, 1)
    INTO v_assetcode_value
    FROM cms_content_data cd
   WHERE cd.content_id IN
         (SELECT c.id
            FROM cms_content c
           WHERE c.content_key_id IN
                 (SELECT ck.id
                    FROM cms_content_key ck
                   WHERE ck.asset_id IN
                         (SELECT a.id
                            FROM cms_asset a
                           WHERE a.code = p_in_assetcode))
                           AND content_status = 'Live'
                           AND locale = 'en_US');   --01/05/2016
  RETURN v_assetcode_value;
EXCEPTION
  WHEN no_data_found then
    v_assetcode_value := null;
    RETURN NVL(v_assetcode_value, 'Characteristic Not Applicable');
  WHEN others THEN
    v_assetcode_value := 'X';
    RETURN v_assetcode_value;
END;
/
