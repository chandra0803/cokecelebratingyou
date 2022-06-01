CREATE OR REPLACE 
FUNCTION fnc_3des_encrypt(p_in_string   IN  VARCHAR2,
                          p_in_key      IN  VARCHAR2,
                          p_in_strength IN  PLS_INTEGER)
RETURN RAW
--***************************************************************************
--  Purpose : To encrypt a string using 3DES (128/192) bit strength
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
  v_raw_input      RAW(128) := UTL_RAW.CAST_TO_RAW(p_in_string);
  v_raw_key        RAW(128) := UTL_RAW.CAST_TO_RAW(p_in_key);
  v_encrypted_str  RAW(128) ;
BEGIN
  dbms_obfuscation_toolkit.DES3Encrypt(input          =>v_raw_input,
                                       key            =>v_raw_key,
                                       encrypted_data =>v_encrypted_str,
                                       which          =>p_in_strength);
  RETURN v_encrypted_str;
EXCEPTION
  WHEN OTHERS THEN
    RETURN NULL;
END;
/

