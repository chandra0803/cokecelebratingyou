create or replace PROCEDURE PRC_PAX_ACC_AUTH_SCRIPT(p_out_return_code OUT NUMBER) IS
/*----------------------------------------------------------------------------

  Purpose: Create a DB script for easy data creation process.
           Save these data scripts so that it can be used whenever needed.

  Person        Date         Comments
  -----------   -----------  ---------------------------------------------------
  Gorantla      10/12/2017  Initial JIRA# G6-3105 Pax Account Authorization DB data script
  -----------------------------------------------------------------------------*/
  v_stage            VARCHAR2(500);
  C_process_name     execution_log.process_name%TYPE  := 'PRC_PAX_ACC_AUTH_SCRIPT';
  
  BEGIN        
    /*         
    Users: Test 101 – Test 120 
    a>    Unique Email Address as Primary email + Unique Mobile Phone Numbers 
    b>    Unique Recovery Email Address ;
    */         
    v_stage := 'Set No:1';
    FOR rec_data IN ( SELECT user_id,
                             user_name
                       FROM application_user
                      WHERE user_name IN ('TEST-101','TEST-102',
                                          'TEST-103','TEST-104',
                                           'TEST-105','TEST-106',
                                           'TEST-107','TEST-108',
                                           'TEST-109','TEST-110',
                                           'TEST-111','TEST-112',
                                           'TEST-113','TEST-114',
                                           'TEST-115','TEST-116',
                                           'TEST-117','TEST-118',
                                           'TEST-119','TEST-120'))
    LOOP    
      BEGIN
      -- Unique Email Address as Primary email                    
        INSERT INTO user_email_Address
               (email_address_id,
                user_id,
                email_type,
                email_addr,
                created_by,
                is_primary,
                date_created,
                version)
         VALUES(user_email_address_pk_sq.nextval,
                rec_data.user_id,
                'bus',
                'abcworld.'||lower(replace(rec_data.user_name,'-'))||'@biworldwide.com',
                1035,
                1,
                sysdate,
                0);
                
      EXCEPTION 
        WHEN OTHERS THEN
          UPDATE user_email_Address
             SET email_addr = 'abcworld.'||lower(replace(rec_data.user_name,'-'))||'@biworldwide.com',
                 date_modified = sysdate,
                 modified_by = 1035,
                 version = version +1
           WHERE user_id = rec_data.user_id
             AND is_primary = 1
             AND email_type = 'bus';
      END;                
           
      -- Unique Mobile Phone Numbers          
      INSERT INTO user_phone
                (user_phone_id,
                 user_id,
                 phone_type,
                 phone_nbr,
                 country_phone_code,
                 is_primary,
                 created_by,
                 date_created,
                 version)
          VALUES(user_phone_pk_sq.nextval,
                 rec_Data.user_id,
                 'mob',
                 '612-000-0'||replace(rec_data.user_name,'TEST-'),
                 'us',
                  0,
                  1035,
                  sysdate,
                  0); 
                  
      DELETE FROM user_email_Address
       WHERE email_type = 'rec'
        AND user_id = rec_data.user_id;                            

      -- Unique Recovery Email Address           
      INSERT INTO user_email_Address
               (email_address_id,
                user_id,
                email_type,
                email_addr,
                created_by,
                is_primary,
                date_created,
                version)
        VALUES(user_email_address_pk_sq.nextval,
               rec_data.user_id,
               'rec',
               'abcrecov.'||lower(replace(rec_data.user_name,'-'))||'@biworldwide.com',
               1035,
               0,
               sysdate,
               0);                                
    END LOOP;

    /*
    Users: Test121- Test140 
    a>    Unique Email Address as Primary email + Shared Phone Number (Business) 
    b>    Unique Recovery for the first 10 users and shared for the next 10 
    */
    v_stage := 'Set No:2';
    FOR rec_data IN ( SELECT rownum row_num,
                             user_id,
                             user_name
                        FROM application_user
                       WHERE user_name IN ('TEST-121','TEST-122',
                                           'TEST-123','TEST-124',
                                           'TEST-125','TEST-126',
                                           'TEST-127','TEST-128',
                                           'TEST-129','TEST-130',
                                           'TEST-131','TEST-132',
                                           'TEST-133','TEST-134',
                                           'TEST-135','TEST-136',
                                           'TEST-137','TEST-138',
                                           'TEST-139','TEST-140')) 
    LOOP    
      BEGIN
        -- Unique Email Address as Primary email                  
        INSERT INTO user_email_Address
               (email_address_id,
                user_id,
                email_type,
                email_addr,
                created_by,
                is_primary,
                date_created,
                version)
        VALUES(user_email_address_pk_sq.nextval,
               rec_data.user_id,
               'bus',
               'abcworld.'||lower(replace(rec_data.user_name,'-'))||'@biworldwide.com',
               1035,
               1,
               sysdate,
               0);
               
      EXCEPTION WHEN OTHERS THEN
        UPDATE user_email_Address
           SET email_addr = 'abcworld.'||lower(replace(rec_data.user_name,'-'))||'@biworldwide.com',
               date_modified = sysdate,
               modified_by = 1035,
               version = version +1
         WHERE user_id = rec_data.user_id
           AND is_primary = 1
           AND email_type = 'bus';
      END;                
               
      -- Shared Phone Number (Business)      
      INSERT INTO user_phone
                (user_phone_id,
                 user_id,
                 phone_type,
                 phone_nbr,
                 country_phone_code,
                 is_primary,
                 created_by,
                 date_created,
                 version)
         VALUES (user_phone_pk_sq.nextval,
                 rec_data.user_id,
                 'mob',
                 '555-111-2233',
                 'us',
                  0,
                  1035,
                  sysdate,
                  0);     

      DELETE FROM user_email_Address
       WHERE email_type = 'rec'
        AND user_id = rec_data.user_id;                           

      -- Unique Recovery for the first 10 users 
      IF rec_data.row_num <= 10 THEN         
        INSERT INTO user_email_Address
               (email_address_id,
                user_id,
                email_type,
                email_addr,
                created_by,
                is_primary,
                date_created,
                version)
        VALUES(user_email_address_pk_sq.nextval,
               rec_data.user_id,
               'rec',
               'abcrecov.'||lower(replace(rec_data.user_name,'-'))||'@biworldwide.com',
               1035,
               0,
               sysdate,
               0); 
      ELSE             
        --and shared for the next 10                
        INSERT INTO user_email_Address
               (email_address_id,
                user_id,
                email_type,
                email_addr,
                created_by,
                is_primary,
                date_created,
                version)
        VALUES(user_email_address_pk_sq.nextval,
               rec_data.user_id,
               'rec',
               'bicore_pax@biworldwide.com',
               1035,
               0,
               sysdate,
               0);   
      END IF;                                                         
    END LOOP;

    /*
    Users: Test 141 – Test160 
    a>    Shared Email Address as Primary (bicore_pax@biworlwide.com) + Unique Mobile Phone Numbers 
    b>    Shared Recovery email address 
    */
    v_stage := 'Set No:3';
    FOR rec_data IN ( SELECT user_id,
                             user_name
                        FROM application_user
                       WHERE user_name IN ('TEST-141','TEST-142',
                                           'TEST-143','TEST-144',
                                           'TEST-145','TEST-146',
                                           'TEST-147','TEST-148',
                                           'TEST-149','TEST-150',
                                           'TEST-151','TEST-152',
                                           'TEST-153','TEST-154',
                                           'TEST-155','TEST-156',
                                           'TEST-157','TEST-158',
                                           'TEST-159','TEST-160'))
    LOOP    
      BEGIN     
        -- Shared Email Address as Primary (bicore_pax@biworlwide.com)                  
        INSERT INTO user_email_Address
               (email_address_id,
                user_id,
                email_type,
                email_addr,
                created_by,
                is_primary,
                date_created,
                version)
        VALUES(user_email_address_pk_sq.nextval,
               rec_data.user_id,
               'bus',
               'bicore_pax@biworldwide.com',
               1035,
               1,
               sysdate,
               0);
               
      EXCEPTION WHEN OTHERS THEN
        UPDATE user_email_Address
           SET email_addr = 'bicore_pax@biworldwide.com',
               date_modified = sysdate,
               modified_by = 1035,
               version = version +1
         WHERE user_id = rec_data.user_id
           AND is_primary = 1
           AND email_type = 'bus';
      END;                 
               
      -- Unique Mobile Phone Numbers         
      INSERT INTO user_phone
                (user_phone_id,
                 user_id,
                 phone_type,
                 phone_nbr,
                 country_phone_code,
                 is_primary,
                 created_by,
                 date_created,
                 version)
         VALUES (user_phone_pk_sq.nextval,
                 rec_Data.user_id,
                 'mob',
                 '612-000-0'||replace(rec_data.user_name,'TEST-'),
                 'us',
                  0,
                  1035,
                  sysdate,
                  0);           

      DELETE FROM user_email_Address
       WHERE email_type = 'rec'
         AND user_id = rec_data.user_id;   
        
      --  Shared Recovery email address            
      INSERT INTO user_email_Address
               (email_address_id,
                user_id,
                email_type,
                email_addr,
                created_by,
                is_primary,
                date_created,
                version)
        VALUES(user_email_address_pk_sq.nextval,
               rec_data.user_id,
               'rec',
               'bicore_pax@biworldwide.com',
               1035,
               0,
               sysdate,
               0);                                
    END LOOP;

    /*
    Users: Test 161 – Test 180 
    a>    Shared Email as Primary + Shared mobile phone numbers 
    b>    Unique Recovery Address 
    */
    v_stage := 'Set No:4';
    FOR rec_data IN ( SELECT user_id,
                             user_name
                        FROM application_user
                       WHERE user_name IN ('TEST-161','TEST-162',
                                           'TEST-163','TEST-164',
                                           'TEST-165','TEST-166',
                                           'TEST-167','TEST-168',
                                           'TEST-169','TEST-170',
                                           'TEST-171','TEST-172',
                                           'TEST-173','TEST-174',
                                           'TEST-175','TEST-176',
                                           'TEST-177','TEST-178',
                                           'TEST-179','TEST-180'))
    LOOP    
      BEGIN    
        -- Shared Email Address as Primary (bicore_pax@biworlwide.com)                     
        INSERT INTO user_email_Address
               (email_address_id,
                user_id,
                email_type,
                email_addr,
                created_by,
                is_primary,
                date_created,
                version)
        VALUES(user_email_address_pk_sq.nextval,
               rec_data.user_id,
               'bus',
               'bicore_pax@biworldwide.com',
               1035,
               1,
               sysdate,
               0);
               
      EXCEPTION WHEN OTHERS THEN
        UPDATE user_email_Address
           SET email_addr = 'bicore_pax@biworldwide.com',
               date_modified = sysdate,
               modified_by = 1035,
               version = version +1
         WHERE user_id = rec_data.user_id
           AND is_primary = 1
           AND email_type = 'bus';
      END;                  
                     
      -- Shared mobile phone numbers         
      INSERT INTO user_phone
                (user_phone_id,
                 user_id,
                 phone_type,
                 phone_nbr,
                 country_phone_code,
                 is_primary,
                 created_by,
                 date_created,
                 version)
         VALUES (user_phone_pk_sq.nextval,
                 rec_Data.user_id,
                 'mob',
                 '555-111-2233',
                 'us',
                  0,
                  1035,
                  sysdate,
                  0);  
                  
      DELETE FROM user_email_Address
       WHERE email_type = 'rec'
        AND user_id = rec_data.user_id;                              

      --   Unique Recovery Address             
      INSERT INTO user_email_Address
               (email_address_id,
                user_id,
                email_type,
                email_addr,
                created_by,
                is_primary,
                date_created,
                version)
        VALUES(user_email_address_pk_sq.nextval,
               rec_data.user_id,
               'rec',
               'abcrecov.'||lower(replace(rec_data.user_name,'-'))||'@biworldwide.com',
               1035,
               0,
               sysdate,
               0);                                
    END LOOP;
   COMMIT;
   p_out_return_code := 00;
   
EXCEPTION   
  WHEN OTHERS THEN
    p_out_return_code := 99;
    prc_execution_log_entry(C_process_name, 1, 'ERROR',
                            'Error at stage: '||v_stage||
                            ' Message: '||SQLERRM,
                            NULL); 
    ROLLBACK;  
END PRC_PAX_ACC_AUTH_SCRIPT;