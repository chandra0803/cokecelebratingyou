CREATE OR REPLACE PROCEDURE PRC_POPULATE_REMINDER_EMAIL    
   (p_promotion_id      IN  NUMBER  ,
    p_environment_name  IN  VARCHAR2,
    p_return_code       OUT NUMBER  ,
    p_error_message     OUT VARCHAR2)
  IS
/*******************************************************************************
-- Purpose: Used to populate data to the table tmp_nonredemption_reminder.
--          The table data is used to send regular reminder emails for 
--          participants who have not redeemed their award.   

-- MODIFICATION HISTORY
-- Person      Date        Comments
-- ---------   ----------  -----------------------------------------------------
-- Arun S      06/02/2009  Initial creation      
-- Arun S      06/18/2009  Bug # 26950 fix, Removed TRUNCATE and COMMIT in code 
--                         due to the error "Commit not allowed in subordinate session". 
-- Arun S      06/19/2009  Bug # 27022 fix, Altered the Gift code number as expected 
-- Arun S      07/09/2009  Added address and characteristics details
-- Arun S      09/09/2009  Added login id details
-- Arun S      05/11/2010  Bug # 32555 Fix, International address changes 
                           Removed Address4, Address5, Address6 populate to temp table                                                                                 
*******************************************************************************/  
  
  CURSOR cur_pax IS 
    SELECT au.user_name login_id,
           uea.email_addr,
           au.first_name,    
           au.middle_name,    
           au.last_name,    
           fnc_java_decrypt(mo.gift_code)||fnc_java_decrypt(mo.gift_code_key) gift_code,          
           p.promotion_name promotion_name,    
           fnc_cms_asset_code_value (pmcl.cm_asset_key) om_level_name,
           ua.addr1,
           ua.addr2,
           ua.addr3,
           --ua.addr4,      --05/11/2010
           --ua.addr5,
           --ua.addr6,
           ua.city,
           ua.state,
           (SELECT country_code FROM country WHERE country_id = ua.country_id) country,
           ua.postal_code,
           rc.characteristic_charvalue1,
           rc.characteristic_charvalue2,
           rc.characteristic_charvalue3,
           rc.characteristic_charvalue4,
           rc.characteristic_charvalue5             
      FROM merch_order mo,    
           claim       c,    
           promo_merch_country       pmc,    
           promo_merch_program_level pmcl,    
           application_user          au,
           (SELECT * FROM user_email_address
             WHERE is_primary = 1)   uea,              
           promotion                 p,
           (SELECT * FROM user_address
             WHERE is_primary = 1)   ua,           
           (SELECT * FROM rpt_characteristic
             WHERE UPPER(characteristic_type) = 'USER') rc     
     WHERE mo.claim_id                     = c.claim_id(+)    
       AND mo.participant_id               = au.user_id    
       AND pmc.promotion_id                = NVL(c.promotion_id, pmc.promotion_id)    
       AND pmc.promo_merch_country_id      = pmcl.promo_merch_country_id    
       AND mo.promo_merch_program_level_id = pmcl.promo_merch_program_level_id  
       AND uea.user_id(+)                  = au.user_id   
       AND c.promotion_id                  = p.promotion_id 
       AND au.user_id                      = ua.user_id(+) 
       AND au.user_id                      = rc.user_nt_id(+)   
       AND mo.is_redeemed                  = 0    
       AND p.promotion_id                  = p_promotion_id
       AND mo.order_status is null		   --Bug # 30346 Fix
       ;    

  --Exception
  e_invalid_parm            EXCEPTION;
     
  --Execution Log Variables
  v_process_name            execution_log.process_name%type  := 'prc_populate_reminder_email';
  v_release_level           execution_log.release_level%type := '1';  
  
  --Constants
  C_link_string             CONSTANT VARCHAR2(200) := '/merchLevelShopping.do?method=displayThanqOnline'||'&'||'giftCode=';
  C_entity_string           CONSTANT os_propertyset.entity_name%TYPE := 'site.url.';
  
  --Procedure Variables
  v_site_url                os_propertyset.string_val%TYPE;
  v_entity_name             os_propertyset.entity_name%TYPE;
  v_promotion_id            promotion.promotion_id%TYPE;
  v_linq_to_redeem          VARCHAR2(2000);
  v_stage                   VARCHAR2(10);
  
BEGIN
  p_return_code := 1;
  v_stage := '1';
  --Validating Promotion Id 
  BEGIN
    SELECT promotion_id
      INTO v_promotion_id
      FROM promotion
     WHERE promotion_id = p_promotion_id;
  EXCEPTION        
    WHEN NO_DATA_FOUND THEN
      p_error_message := 'Error While Validating Promotion Id:'||p_promotion_id||' '||SUBSTR(SQLERRM,1,100);
      RAISE e_invalid_parm;
  END;

  v_stage := '2';
  --Retrieve site URL
  v_entity_name := C_entity_string||p_environment_name;
  BEGIN
    SELECT string_val
      INTO v_site_url
      FROM os_propertyset
     WHERE UPPER(entity_name) = UPPER(v_entity_name); 
  EXCEPTION
    WHEN OTHERS THEN
      p_error_message := 'Error While Retrieving site URL for the entity: '||v_entity_name||' '||SUBSTR(SQLERRM,1,100);
      RAISE e_invalid_parm;
  END;  
  
  v_stage := '3';
  --Delete records from tmp_nonredemption_reminder before insert 
  DELETE tmp_nonredemption_reminder;
  
  FOR rec_pax IN cur_pax LOOP
    v_stage := '4';
    v_linq_to_redeem := v_site_url||C_link_string||rec_pax.gift_code;
    
    --Insert tmp_nonredemption_reminder
    INSERT INTO tmp_nonredemption_reminder 
                (nonredemption_id,
                 login_id,
                 email_addr, 
                 first_name, 
                 last_name,
                 gift_code, 
                 promotion_name, 
                 level_name, 
                 link_to_merchlinq_to_redeem,
                 is_valid,
                 addr1,
                 addr2,
                 addr3,
                 --addr4,       --05/11/2010    
                 --addr5,
                 --addr6,
                 city,
                 state,
                 country,
                 postal_code,
                 characteristic_charvalue1,
                 characteristic_charvalue2,
                 characteristic_charvalue3,
                 characteristic_charvalue4,
                 characteristic_charvalue5                 
                 ) 
        VALUES  (NONREDEMPTION_ID_SEQ.NEXTVAL, 
                 rec_pax.login_id,
                 rec_pax.email_addr,   
                 rec_pax.first_name, 
                 rec_pax.last_name,
                 rec_pax.gift_code, 
                 rec_pax.promotion_name, 
                 rec_pax.om_level_name, 
                 v_linq_to_redeem,
                 NULL,
                 rec_pax.addr1,
                 rec_pax.addr2,
                 rec_pax.addr3,
                 --rec_pax.addr4,
                 --rec_pax.addr5,
                 --rec_pax.addr6,
                 rec_pax.city,
                 rec_pax.state,
                 rec_pax.country,
                 rec_pax.postal_code,
                 rec_pax.characteristic_charvalue1,
                 rec_pax.characteristic_charvalue2,
                 rec_pax.characteristic_charvalue3,
                 rec_pax.characteristic_charvalue4,
                 rec_pax.characteristic_charvalue5
                 );
  END LOOP;
  
EXCEPTION
  WHEN e_invalid_parm THEN
    p_return_code   := 0;
    prc_execution_log_entry(v_process_name,v_release_level,'ERROR',p_error_message,null);
    
  WHEN OTHERS THEN
    p_return_code   := 0;
    p_error_message := 'When Others.Stage:'||v_stage||'.'||SUBSTR(SQLERRM,1,100);
    prc_execution_log_entry(v_process_name,v_release_level,'ERROR',p_error_message,null);
END;    --PRC_POPULATE_REMINDER_EMAIL
/