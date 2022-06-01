 CREATE OR REPLACE FUNCTION fnc_random_password (IN_TEMPLATE IN VARCHAR2)  
 RETURN VARCHAR2 IS  
 
/*----------------------------------------------------------------------------

  Purpose: Create random passwords

  Person        Date         Comments
  -----------   -----------  ---------------------------------------------------
  Ravi Dhanekula 4/23/2013  Initial
  -----------------------------------------------------------------------------*/
 v_criteria VARCHAR2(1);  
 v_password VARCHAR2(500);  
 v_pattern VARCHAR2(500);  
 v_indx NUMBER;  
 BEGIN  

   v_criteria := '';  
   v_password := '';  
   FOR I IN 1.. LENGTH(IN_TEMPLATE) LOOP  
     v_criteria := SUBSTR(IN_TEMPLATE,I,1);  
     IF UPPER(v_criteria ) = '#' THEN   
       v_pattern := q'[0123456789]';  
     ELSIF ASCII(UPPER(v_criteria )) = ASCII(LOWER(v_criteria )) THEN   
        v_pattern := q'[0123456789]';    
     ELSIF ASCII(UPPER(v_criteria )) <> ASCII(LOWER(v_criteria )) THEN 
        v_pattern := q'[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]'; 
     ELSE
        v_pattern := q'[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789]';  
     END IF;  
     v_indx := TRUNC( LENGTH(v_pattern) * DBMS_RANDOM.VALUE) + 1;  
     v_password := v_password || SUBSTR(v_pattern,v_indx,1);  
   END LOOP;  
   RETURN v_password;  
END fnc_random_password;
/