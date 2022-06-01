CREATE OR REPLACE FUNCTION fnc_get_pk_nextval(pi_sequence_name IN VARCHAR2)
    RETURN NUMBER IS
v_sql VARCHAR(100);
v_pk  NUMBER;
TYPE EligCurTyp  IS REF CURSOR;
r_select_statement        EligCurTyp;
BEGIN

v_sql := 'SELECT ' || pi_sequence_name || '.NEXTVAL FROM DUAL';

OPEN r_select_statement FOR v_sql;

FETCH r_select_statement INTO v_pk;

RETURN v_pk;
END;
/