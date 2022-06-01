CREATE OR REPLACE PACKAGE pkg_conversion_api AS

  /*******************************************************************************
   -- Purpose: To generate UUID
   --
   -- Person        Date         Comments
   -- -----------   --------     -----------------------------------------------------
   -- Gorantla      01/20/2019   Initial creation                                                  
   *******************************************************************************/

FUNCTION fnc_to_base(p_dec   IN  NUMBER,
                 p_base  IN  NUMBER) RETURN VARCHAR2;

FUNCTION fnc_to_dec (p_str        IN  VARCHAR2,
                 p_from_base  IN  NUMBER DEFAULT 16) RETURN NUMBER;

FUNCTION fnc_to_hex(p_dec  IN  NUMBER) RETURN VARCHAR2;

FUNCTION fnc_to_bin(p_dec  IN  NUMBER) RETURN VARCHAR2;

FUNCTION fnc_to_oct(p_dec  IN  NUMBER) RETURN VARCHAR2;

END pkg_conversion_api;

CREATE OR REPLACE PACKAGE BODY pkg_conversion_api AS
  /*******************************************************************************
   -- Purpose: To generate result for RAf or new hires
   --
   -- Person        Date         Comments
   -- -----------   --------     -----------------------------------------------------
   -- Gorantla      01/06/2018   Initial creation                                                  
   *******************************************************************************/
   
FUNCTION fnc_to_base(p_dec   IN  NUMBER,
                 p_base  IN  NUMBER) RETURN VARCHAR2 IS
                 
	v_str	VARCHAR2(255) DEFAULT NULL;
	v_num	NUMBER	      DEFAULT p_dec;
	v_hex	VARCHAR2(16)  DEFAULT '0123456789abcdef';
BEGIN
	IF (TRUNC(p_dec) <> p_dec OR p_dec < 0) THEN
		RAISE PROGRAM_ERROR;
	END IF;
	LOOP
		v_str := SUBSTR(v_hex, MOD(v_num,p_base)+1, 1) || v_str;
		v_num := TRUNC(v_num/p_base);
		EXIT WHEN (v_num = 0);
	END LOOP;
	RETURN v_str;
END fnc_to_base;

FUNCTION fnc_to_dec (p_str        IN  VARCHAR2,
                 p_from_base  IN  NUMBER DEFAULT 16) RETURN NUMBER IS
                 
	v_num   NUMBER       DEFAULT 0;
	v_hex   VARCHAR2(16) DEFAULT '0123456789abcdef';
BEGIN
	FOR i IN 1 .. LENGTH(p_str) LOOP
		v_num := v_num * p_from_base + INSTR(v_hex,UPPER(SUBSTR(p_str,i,1)))-1;
	END LOOP;
	RETURN v_num;
END fnc_to_dec;

FUNCTION fnc_to_hex(p_dec  IN  NUMBER) RETURN VARCHAR2 IS

BEGIN
	RETURN fnc_to_base(p_dec, 16);
END fnc_to_hex;

FUNCTION fnc_to_bin(p_dec  IN  NUMBER) RETURN VARCHAR2 IS

BEGIN
	RETURN fnc_to_base(p_dec, 2);
END fnc_to_bin;

FUNCTION fnc_to_oct(p_dec  IN  NUMBER) RETURN VARCHAR2 IS
BEGIN
	RETURN fnc_to_base(p_dec, 8);
END fnc_to_oct;

END pkg_conversion_api;

CREATE OR REPLACE FUNCTION fnc_new_uuid RETURN VARCHAR2 AS
  /*******************************************************************************
   -- Purpose: To generate UUID
   --
   -- Person        Date         Comments
   -- -----------   --------     -----------------------------------------------------
   -- Gorantla      01/20/2019   Initial creation                                                  
   *******************************************************************************/
  v_seed        BINARY_INTEGER;
  v_random_num  NUMBER(5);

  v_date        VARCHAR2(25);
  v_random      VARCHAR2(4);
  v_ip_address  VARCHAR2(12);
BEGIN
  v_seed := TO_NUMBER(TO_CHAR(SYSDATE,'YYYYDDMMSS'));
  DBMS_RANDOM.initialize (val => v_seed);
  v_random_num := TRUNC(DBMS_RANDOM.value(low => 1, high => 65535));
  DBMS_RANDOM.terminate;
  
  v_date       := pkg_conversion_api.fnc_to_hex(TO_NUMBER(TO_CHAR(SYSTIMESTAMP,'FFSSMIHH24DDMMYYYY')));
  v_random     := RPAD(pkg_conversion_api.fnc_to_hex(v_random_num), 4, '0');
  v_ip_address := pkg_conversion_api.fnc_to_hex(TO_NUMBER(REPLACE(NVL(SYS_CONTEXT('USERENV','IP_ADDRESS'), '123.123.123.123'), '.', '')));

  RETURN SUBSTR(v_date, 1, 8)                     || '-' ||
         SUBSTR(v_date, 9, 4)                     || '-' ||
         SUBSTR(v_date, 13, 4)                    || '-' ||
         RPAD(SUBSTR(v_date, 17), 4, '0')         || '-' ||
         RPAD(v_random || v_ip_address, 12, '0');
END;
