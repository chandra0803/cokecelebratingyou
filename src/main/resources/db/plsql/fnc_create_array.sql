CREATE OR REPLACE TYPE array_num AS TABLE OF NUMBER;
/
    CREATE or replace FUNCTION get_array(num_list IN VARCHAR2)
RETURN array_num PIPELINED
IS
    countAppearance NUMBER := 0;
    delimiter_position NUMBER := -1;
    start_position NUMBER := 1;
    num VARCHAR2(20); 
 
BEGIN
    WHILE delimiter_position != 0 LOOP
        countAppearance := countAppearance + 1;
        delimiter_position := InStr(num_list, ',', 1,countAppearance);
        IF delimiter_position = 0 THEN
            num := Trim(SubStr(num_list,start_position));
        ELSE
            num := Trim(SubStr(num_list,start_position,(delimiter_position - start_position)));
            start_position := delimiter_position + 1;
        END IF; 
 
        PIPE ROW(TO_NUMBER(num)); 
 
    END LOOP;
    RETURN;
END get_array;
/
CREATE OR REPLACE TYPE array_varchar AS TABLE OF VARCHAR2(1000);
/
          
          CREATE or replace FUNCTION get_array_varchar(varchar_list IN VARCHAR2)
RETURN array_varchar PIPELINED
IS
    countAppearance NUMBER := 0;
    delimiter_position NUMBER := -1;
    start_position NUMBER := 1;
    num VARCHAR2(100); 
 
BEGIN
    WHILE delimiter_position != 0 LOOP
        countAppearance := countAppearance + 1;
        delimiter_position := InStr(varchar_list, ',', 1,countAppearance);
        IF delimiter_position = 0 THEN
            num := Trim(SubStr(varchar_list,start_position));
        ELSE
            num := Trim(SubStr(varchar_list,start_position,(delimiter_position - start_position)));
            start_position := delimiter_position + 1;
        END IF; 
 
        PIPE ROW(num); 
 
    END LOOP;
    RETURN;
END get_array_varchar;
/