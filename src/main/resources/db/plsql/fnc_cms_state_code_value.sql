CREATE OR REPLACE FUNCTION fnc_cms_state_code_value
( p_in_cms_contenttype  IN VARCHAR2,
  p_in_statecode        IN VARCHAR2,
  p_in_content_data_key IN VARCHAR2    DEFAULT 'NAME'
) RETURN VARCHAR2 IS
/*******************************************************************************
  -- Purpose: Abstract function that returns actual value for the give
  --          CM state code.
  -- MODIFICATION HISTORY
  -- Person      Date       Comments
  -- ---------   ------     -------------------------------------------
  -- Arun S      12/22/2008 Initial Creation
  -- J Flees     08/22/2011 Added content data key input parameter allowing
  --                        function to return the state name or abbreviation.
*******************************************************************************/
  v_statecode_value VARCHAR2(300);

BEGIN
 SELECT value
   INTO v_statecode_value   
   FROM cms_content_data 
  WHERE content_id = (SELECT cd.content_id                  
                        FROM cms_content_data cd
                       WHERE cd.content_id IN (SELECT c.id				                          
				                                 FROM cms_content c
				                                WHERE c.content_key_id IN (SELECT ck.id					 
					                                                         FROM cms_content_key ck
					                                                        WHERE ck.asset_id IN (SELECT a.id						 
						                                                                            FROM cms_asset a
						                                                                           WHERE a.code = p_in_cms_contenttype))
						                       AND c.content_status = 'Live')                           
   AND cd.key = 'CODE' 
   AND DBMS_LOB.substr(cd.value,300, 1) = p_in_statecode)
   AND key = p_in_content_data_key;
                       
  RETURN v_statecode_value;
EXCEPTION
  WHEN no_data_found then
    v_statecode_value  := null;
    RETURN v_statecode_value;
  WHEN others THEN
    v_statecode_value := 'X';
    RETURN v_statecode_value;
END;
/