CREATE OR REPLACE FUNCTION fnc_format_comments_clob(p_in_string  IN  CLOB)
RETURN CLOB 
--***************************************************************************
--  Purpose : to replace common html character strings
--
-- NOTE: intentially replacing double-quotes with single-quotes because of .csv issues
--
--  Author                Date        Comments
--  --------------------------------------------------------------------------
-- DeepakrajS           04/08/2019    Bug 72708 : Created with CLOB I/O Parameters
--***************************************************************************
IS
    --p_out_string  VARCHAR2(5000); --04/08/2019
      p_out_string CLOB; --04/08/2019
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
