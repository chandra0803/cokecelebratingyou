CREATE OR REPLACE 
FUNCTION fnc_3des_decrypt(p_in_raw  IN  RAW,
                          p_in_key  IN  VARCHAR2,
                          p_in_strength IN PLS_INTEGER)
RETURN VARCHAR2
--***************************************************************************
--  Purpose : To decrypt an encrypted string using 3DES (128/192) bit strength
--
--  p_in_strength: 0/1
--  0=128 bit encryption, needs a key of atleast 16 characters
--  1=192 bit encryption, needs a key of atleast 24 characters
--
--  Author                Date        Comments
--  --------------------------------------------------------------------------
--  Raju N             05/04/2005   Cloned from encryptDB
--***************************************************************************
IS
  v_raw_key        RAW(128) := UTL_RAW.CAST_TO_RAW(p_in_key);
  v_decrypted_raw  RAW(200);
BEGIN
	dbms_obfuscation_toolkit.DES3Decrypt(input          =>p_in_raw,
                                       key            =>v_raw_key,
                                       decrypted_data =>v_decrypted_raw,
                                       which          =>p_in_strength);

  RETURN UTL_RAW.CAST_TO_VARCHAR2(v_decrypted_raw);
EXCEPTION
  WHEN OTHERS THEN
    DBMS_OUTPUT.put_line(SQLERRM);
    RETURN NULL ;
END;
/

