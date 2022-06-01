CREATE OR REPLACE PROCEDURE prc_welcome_email_prep (p_out_return_code OUT NUMBER)
IS PRAGMA AUTONOMOUS_TRANSACTION;
/*----------------------------------------------------------------------------

  Purpose: Create passwords for the participants. This is used by strong mail process.

  Person        Date         Comments
  -----------   -----------  ---------------------------------------------------
  Ravi Dhanekula 04/23/2013  Initial
                 09/11/2013  Added process to include more data in strongmail_user table.
-- Chidamba      10/04/2013  Fixed defect# 4832
--Ravi Dhanekula 05/27/2014 Fixed the bugs 53715 and 53555
                           12/12/2014 Fixed the bug # 58582. Concatenated v_website_url with v_contact_url
--Ravi Dhanekula 03/28/2016 MD5 elimination change. Replaced MD5 function with sha256 function.
--Gorantla       10/16/2017 JIRA# G6-3076 Adding new column and sequence to strong_mai_ user table
  -----------------------------------------------------------------------------*/
--Exception
  e_exit_pgm                EXCEPTION;

  --Execution log variables
  C_process_name            execution_log.process_name%TYPE  := 'prc_welcome_email_prep';
  C_release_level           execution_log.release_level%TYPE := '1';
  
  --Procedure variables
v_password VARCHAR2(100);
v_stage   VARCHAR2(100);
v_password_pattern VARCHAR2(30);
v_company VARCHAR2(250);
v_website_url VARCHAR2(300);
v_contact_url VARCHAR2(300);
v_count NUMBER:=0;
v_boolean number(1); 

cursor cur_user is
SELECT au.user_id,
           au.first_name,
           au.last_name, 
           au.user_name,
           em.email_addr,
           language_id 
      FROM application_user au,
           user_email_address em,
           participant p
     WHERE au.user_id          = em.user_id
       AND au.is_active        = 
          DECODE ( (SELECT boolean_val --05/27/2014 Bug # 53715.
                      FROM os_propertyset
                     WHERE entity_name = 'termsAndConditions.used'),
                  1, 0,
                  0, 1)
       AND au.is_welcome_email_sent = 0
       AND au.last_reset_date  IS NULL
       AND em.is_primary       = 1
        AND au.user_id = p.user_id
        AND p.terms_acceptance = 'notaccepted';
      
BEGIN

  v_stage := 'Write start to execution_log table';
  prc_execution_log_entry(C_process_name, 1, 'INFO',
                          'Procedure Started',
                          NULL);
                          
EXECUTE IMMEDIATE ('TRUNCATE TABLE strongmail_user'); --05/27/2014 Bug # 53555.

v_password :=NULL;

v_company:= fnc_get_system_variable ('client.name','Client Name');
v_website_url:= fnc_get_system_variable ('site.url.prod','Prod Site URL');
v_contact_url:= fnc_get_system_variable ('client.contact.url','Client Contact Us URL');
SELECT boolean_val INTO v_boolean FROM os_propertyset where entity_name='password.should.use.regex';


 IF fnc_get_system_var_boolean('password.use.initial','Use Initial Password') =1 THEN
    
     v_password := fnc_get_system_variable ('password.initial','Initial Password');
      IF v_password is NOT NULL THEN
        FOR rec_user IN cur_user LOOP

         v_count :=v_count+1;
         INSERT INTO STRONGMAIL_USER(strongmail_user_id,first_name,last_name,user_name,email_addr,language_id,company,website_url,contact_url,password) -- 10/16/2017
            VALUES (strongmail_user_pk_sq.nextval,rec_user.first_name,rec_user.last_name,rec_user.user_name,rec_user.email_addr,NVL(rec_user.language_id,'en_US'),v_company,v_website_url,v_website_url||v_contact_url,v_password);--12/12/2014 --10/16/2017

          IF mod(v_count,10000) =0 THEN
           COMMIT;
          END IF;
        END LOOP;
      ELSE
        v_stage := 'Could not find a value for system variable password.initial';
        RAISE e_exit_pgm;
      END IF;

 ELSE

      v_password_pattern:= fnc_get_system_variable ('password.pattern','Pattern to generate default password');
 
   IF v_password_pattern IS NULL THEN
 
     FOR rec_user IN cur_user LOOP
        v_count :=v_count+1;
        INSERT INTO STRONGMAIL_USER(strongmail_user_id,first_name,last_name,user_name,email_addr,language_id,company,website_url,contact_url,password) --10/16/2017
        VALUES (strongmail_user_pk_sq.nextval,rec_user.first_name,rec_user.last_name,rec_user.user_name,rec_user.email_addr,NVL(rec_user.language_id,'en_US'),v_company,v_website_url,v_website_url||v_contact_url,DBMS_RANDOM.STRING('X',10));--12/12/2014 --10/16/2017

       IF mod(v_count,10000) =0 THEN
        COMMIT;
       END IF;
     END LOOP;

   ELSE

     FOR rec_user IN cur_user LOOP
         v_count :=v_count+1;
         INSERT INTO STRONGMAIL_USER(strongmail_user_id,first_name,last_name,user_name,email_addr,language_id,company,website_url,contact_url,password) --10/16/2017
         VALUES (strongmail_user_pk_sq.nextval,rec_user.first_name,rec_user.last_name,rec_user.user_name,rec_user.email_addr,NVL(rec_user.language_id,'en_US'),v_company,v_website_url,v_website_url||v_contact_url,fnc_random_password(v_password_pattern));--12/12/2014 --10/16/2017

       IF mod(v_count,10000) =0 THEN
         COMMIT;
       END IF;
     END LOOP;
   END IF;

 END IF;
 v_count := 0;
 --Adding update to reset password in application table with force_password and welcome email set to true
 FOR rec_user IN cur_user LOOP          --10/04/2013 Started
   SELECT CASE WHEN v_boolean = 0 THEN UPPER(password) ELSE  password END --03/28/2016
    INTO v_password
    FROM STRONGMAIL_USER
   WHERE user_name = rec_user.user_name;
  
  
  UPDATE application_user a 
     SET ---password = fnc_sha_hash(v_password),--03/28/2016  -- 10/16/2017
         is_welcome_email_sent = 1,
         force_password_change = 1,
         last_reset_date       = SYSDATE,
         version               = version + 1,
         date_modified         = SYSDATE,
         modified_by           = 0 
   WHERE user_name = rec_user.user_name; 
  
   v_count := v_count + 1;   
 END LOOP;                      --10/04/2013 End
  
 prc_execution_log_entry(C_process_name,1, 
                         'INFO',
                         'Update Record count :'||v_count,
                          NULL);
                          
 p_out_return_code := 0;
 v_stage := 'Write completion to execution_log table';
 prc_execution_log_entry(C_process_name,1, 
                         'INFO',
                         'Procedure Completed',
                          NULL);
COMMIT;

EXCEPTION
WHEN e_exit_pgm THEN
    p_out_return_code := 99;
    prc_execution_log_entry(C_process_name, 1, 'ERROR',
                            'Error at stage: '||v_stage||SQLERRM,                          
                            NULL);

  WHEN OTHERS THEN
    p_out_return_code := 99;
    prc_execution_log_entry(C_process_name, 1, 'ERROR',
                            'Error at stage: '||v_stage||
                            ' Message: '||SQLERRM,
                            NULL);
ROLLBACK;
END prc_welcome_email_prep;
/
