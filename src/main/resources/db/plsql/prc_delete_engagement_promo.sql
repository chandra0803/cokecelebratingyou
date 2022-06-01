CREATE OR REPLACE PROCEDURE      prc_delete_engagement_promo (p_in_promotion_id IN promotion.promotion_id%type,
                                                              p_out_return_code  OUT VARCHAR2)
 IS
   /****************************************************************************************
   -- Purpose: To delete engagement(RPM) related promotion data for the input promotion_id   
   --
   -- Person                   Date                  Comments
   -- -----------              --------          ----------------------------------
   -- Suresh J                 01/19/2016          Initial version
   ******************************************************************************************/
    v_process_name          execution_log.process_name%type  := 'PRC_DELETE_ENGAGEMENT_PROMO';
    v_release_level         execution_log.release_level%type := '1';
    v_msg                   execution_log.text_line%TYPE;
    v_live_promo_count      NUMBER := 0;
      
    e_error                 EXCEPTION;
    BEGIN

     BEGIN

       SELECT COUNT(*) 
              INTO v_live_promo_count 
       FROM PROMOTION WHERE promotion_id = p_in_promotion_id AND
                            promotion_status = 'live';
                            
       IF v_live_promo_count > 0 THEN
          v_msg := 'ERROR Cannot delete a live promotion...';
          RAISE e_error;
        END IF;         
                             
       EXCEPTION
         WHEN OTHERS THEN
          v_msg := 'ERROR while validating promotion ID for deletion->'||SQLERRM;
          RAISE e_error;
       
       END;
    
     BEGIN
    
      DELETE 
      FROM PROMO_NOTIFICATION 
      WHERE promotion_id = p_in_promotion_id;

      EXCEPTION
        WHEN OTHERS THEN
          v_msg := 'ERROR while deleting from PROMO_NOTIFICATION table->'||SQLERRM;
          RAISE e_error;
     END;
 
     BEGIN
    
      DELETE 
      FROM PROMO_ENGAGEMENT_PROMOTIONS 
      WHERE promotion_id = p_in_promotion_id;

      EXCEPTION
        WHEN OTHERS THEN
          v_msg := 'ERROR while deleting from PROMO_ENGAGEMENT_PROMOTIONS table->'||SQLERRM;
          RAISE e_error;
     END;


     BEGIN
    
      DELETE 
      FROM PROMO_ENG_RULES_AUDIENCE 
      WHERE rules_id IN (SELECT ID 
                            FROM PROMO_ENGAGEMENT_RULES 
                            WHERE promotion_id = p_in_promotion_id);

      EXCEPTION
        WHEN OTHERS THEN
          v_msg := 'ERROR while deleting from PROMO_ENG_RULES_AUDIENCE table->'||SQLERRM;
          RAISE e_error;
     END;


     BEGIN
    
      DELETE 
      FROM PROMO_ENGAGEMENT_RULES 
      WHERE promotion_id = p_in_promotion_id;

      EXCEPTION
        WHEN OTHERS THEN
          v_msg := 'ERROR while deleting from PROMO_ENGAGEMENT_RULES table->'||SQLERRM;
          RAISE e_error;
     END;


     BEGIN
    
      DELETE 
      FROM PROMO_ENGAGEMENT 
      WHERE promotion_id = p_in_promotion_id;

      EXCEPTION
        WHEN OTHERS THEN
          v_msg := 'ERROR while deleting from PROMO_ENGAGEMENT table->'||SQLERRM;
          RAISE e_error;
     END;


     BEGIN
    
      DELETE 
      FROM PROMOTION 
      WHERE promotion_id = p_in_promotion_id;

      EXCEPTION
        WHEN OTHERS THEN
          v_msg := 'ERROR while deleting from PROMOTION table->'||SQLERRM;
          RAISE e_error;
     END;
          
    p_out_return_code := '00';
    EXCEPTION
       WHEN e_error THEN
       p_out_return_code := v_msg;
         prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                                 'p_in_promotion_id: '||p_in_promotion_id||                            
                                 ' --> '||p_out_return_code,null);
 
       WHEN OTHERS THEN
         p_out_return_code := SQLERRM;
         prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                                 'p_in_promotion_id: '||p_in_promotion_id||                            
                                 ' --> '||SQLERRM,null);
         COMMIT;
    END  prc_delete_engagement_promo ;
/
