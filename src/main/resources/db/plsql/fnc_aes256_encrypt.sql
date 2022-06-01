CREATE OR REPLACE FUNCTION fnc_aes256_encrypt 
  ( pi_string IN VARCHAR2,
    pi_key    IN VARCHAR2 )
  RETURN VARCHAR2 IS
  /*
  The following does PL/SQL block encrypting of
  pre-defined 'input_string' using 256-bit AES algorithm with
  Cipher Block Chaining and PKCS#5 compliant padding.
 
  CHAIN_CBC Cipher Block Chaining. Plaintext is XORed with the previous ciphertext block before it is encrypted.
  PAD_PKCS5 Provides padding which complies with the PKCS #5: Password-Based Cryptography Standard
 
  The VARCHAR2 datatype is not directly supported by DBMS_CRYPTO.
  Before you can perform cryptographic operations on data of the
  type VARCHAR2, you must convert it to the uniform database
  character set AL32UTF8, and then convert it to the RAW datatype.
  After performing these conversions, you can then encrypt it
  with the DBMS_CRYPTO package.
  */
 
   encrypted_raw      RAW (2000);             -- stores encrypted binary text
   key_bytes_raw      RAW (32);               -- stores 256-bit encryption key
   encryption_type    PLS_INTEGER :=          -- total encryption type
                             dbms_crypto.ENCRYPT_AES256
                             + dbms_crypto.CHAIN_CBC
                             + dbms_crypto.PAD_PKCS5 ;
                             
/*                               GETCRYPTOVAR.GET_ENCRYPT_AES256@CRYPTO
                             + GETCRYPTOVAR.GET_CHAIN_CBC@CRYPTO
                             + GETCRYPTOVAR.GET_PAD_PKCS5@CRYPTO ;  */
/*                            DBMS_CRYPTO.ENCRYPT_AES256@crypto
                          + DBMS_CRYPTO.CHAIN_CBC
                          + DBMS_CRYPTO.PAD_PKCS5; */
   v_return VARCHAR2(1000);
   
BEGIN
 
   key_bytes_raw := UTL_I18N.STRING_TO_RAW  --@crypto
   (pi_key,  'AL32UTF8');
   
   encrypted_raw := DBMS_CRYPTO.ENCRYPT   --@crypto
      (
         src => UTL_I18N.STRING_TO_RAW    --@crypto
         (pi_string,  'AL32UTF8'),
         typ => encryption_type,
         key => key_bytes_raw
      );
 
    /*If you want to store encrypted data of the RAW datatype in a VARCHAR2
    DATABASE column, then use RAWTOHEX or UTL_ENCODE.BASE64_ENCODE to make it
    suitable for VARCHAR2 storage*/
    v_return := RAWTOHEX(encrypted_raw);
 
    RETURN v_return;
    
EXCEPTION
   WHEN others THEN
     RAISE_APPLICATION_ERROR (-20011,'Error in Encrypting Value : ' || pi_string || ' - ' || SQLERRM);
END;


 