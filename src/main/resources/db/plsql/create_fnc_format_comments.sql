CREATE OR REPLACE FUNCTION fnc_format_comments(p_in_string  IN  VARCHAR2)
RETURN VARCHAR2
--***************************************************************************
--  Purpose : to replace common html character strings
--
-- NOTE: intentially replacing double-quotes with single-quotes because of .csv issues
--
--  Author                Date        Comments
--  --------------------------------------------------------------------------
-- chidamba             02/08/2018    Initial Creation
--***************************************************************************
IS
    p_out_string  VARCHAR2(5000);
BEGIN

	p_out_string := REGEXP_REPLACE(p_in_string,'<[^>]*>','');
	p_out_string := REPLACE(p_out_string,'&ndash;','-');
	p_out_string := REPLACE(p_out_string,'&mdash;','-');
	p_out_string := REPLACE(p_out_string,'&ldquo;','''');
	p_out_string := REPLACE(p_out_string,'&rdquo;','''');
	p_out_string := REPLACE(p_out_string,'&lsquo','''');
	p_out_string := REPLACE(p_out_string,'&rsquo;','''');
  p_out_string := REPLACE(p_out_string,'&#39;','''');
	p_out_string := REPLACE(p_out_string,'&nbsp;',' ');
	p_out_string := REPLACE(p_out_string,'&middot;','');
	p_out_string := REPLACE(p_out_string,'"','''');
	p_out_string := REPLACE(p_out_string,';','');
	p_out_string := REPLACE(REPLACE(p_out_string,CHR(10),''),CHR(13),''); 

	RETURN p_out_string;
  
EXCEPTION
  WHEN OTHERS THEN
    RETURN p_in_string ;
END;
/