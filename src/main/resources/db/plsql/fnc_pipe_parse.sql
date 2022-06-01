CREATE OR REPLACE 
FUNCTION fnc_pipe_parse
 (p_instring in varchar2,
  p_field_no in number,
  p_delimiter in varchar2 default '|')
  RETURN varchar2 IS
-- -------------------------------------------------------------------------------
-- PRIME VERSION 3.0
--
-- Purpose: To parse a string based on a delimiter, and field number.
--
-- USAGE: First paramater is the input string itself.
--	  Second parameter is the field number which is required to be extracted from the string.
--	  Third paramater is the delimiter, which is optional and defaults to '~'.

--	  RETURNS the required field from the string.
--
-- Arunkumar Subramanian 12/16/2003 Increased the length of the v_instring variable to 32767
-- --------------------------------------------------------------------------------
	v_start number;
	v_no_of_chars number;
	v_instring varchar2(32767);
BEGIN
v_instring:=p_instring||p_delimiter;
  IF p_field_no = 1 THEN
  	return(substr(v_instring,1,instr(v_instring,p_delimiter)-1));
  ELSE
  	v_start := instr(v_instring,p_delimiter,1,p_field_no-1)+1;

  	v_no_of_chars := instr(v_instring,p_delimiter,1,p_field_no) - v_start;
	return(substr(v_instring,v_start,v_no_of_chars));
  END IF;
END;
/

