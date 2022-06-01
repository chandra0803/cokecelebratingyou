CREATE OR REPLACE PROCEDURE prc_delete_audience(p_in_audience_id IN audience.audience_id%type,
                                                        p_out_return_code  OUT VARCHAR2)
 IS
   /*******************************************************************************
   -- Purpose: To verify and delete an audience
   --
   -- Person                   Date                  Comments
   -- -----------               --------          -----------------------------------------------------
   -- Ravi Dhanekula   10/28/2013   Initial version
   -- nagarajs         07/18/2014   Fixed Bug 54728
   *******************************************************************************/
  v_process_name          execution_log.process_name%type  := 'prc_delete_audience';
  v_release_level            execution_log.release_level%type := '1';
  v_msg                     execution_log.text_line%TYPE;
  
  Valid_audience            EXCEPTION;
  e_error                        EXCEPTION;
  v_aud_exists               NUMBER:=0;

BEGIN

--Check if there are any promotions linked to the Audience.
BEGIN
SELECT count(1) INTO v_aud_exists FROM promo_audience WHERE audience_id = p_in_audience_id;
  
  EXCEPTION
    WHEN OTHERS THEN
      v_msg := 'ERROR during audience validation: '||SQLERRM;
      RAISE e_error;
  END;
  
  IF v_aud_exists > 0 THEN
  v_msg := 'There are Promotion(s) linked to the Audience';
  RAISE Valid_audience;
  END IF;
  --Check if there are any CM data linked to the Audience.
  BEGIN
SELECT count(1) INTO v_aud_exists FROM cms_content_key_audience_lnk caulink,cms_application ca,cms_audience cau,audience au  WHERE 
 ca.code = 'beacon' 
 AND ca.id = cau.application_id
 AND LOWER(cau.name) = LOWER(au.name)
 AND cau.id = caulink.audience_id
 AND au.audience_id = p_in_audience_id;
  
  EXCEPTION
    WHEN OTHERS THEN
      v_msg := 'ERROR during audience validation: '||SQLERRM;
      RAISE e_error;
  END;
  
      IF v_aud_exists > 0 THEN
  v_msg := 'There is CM data linked to the Audience';
  RAISE Valid_audience;
  END IF;
  -- Check if audience exists
   BEGIN
SELECT count(1) INTO v_aud_exists FROM audience  WHERE audience_id = p_in_audience_id;
  
  EXCEPTION
    WHEN OTHERS THEN
      v_msg := 'ERROR during audience validation: '||SQLERRM;
      RAISE e_error;
  END;
  
      IF v_aud_exists = 0 THEN
  v_msg := 'Invalid Audience ID '||p_in_audience_id;
  RAISE Valid_audience;
  END IF;
  
 --Delete Audience

BEGIN
DELETE audience_criteria_char
 WHERE audience_criteria_id IN (SELECT audience_criteria_id
                                  FROM audience_criteria
                                 WHERE audience_id = p_in_audience_id);

DELETE audience_criteria
 WHERE audience_id = p_in_audience_id;

DELETE participant_audience
WHERE audience_id = p_in_audience_id;

DELETE cms_audience
WHERE id IN (select cau.id from cms_audience cau,cms_application ca,audience au where 
ca.code = 'beacon' 
 AND ca.id = cau.application_id
 AND LOWER(cau.name) = LOWER(au.name) 
 AND au.audience_id = p_in_audience_id);
 
 DELETE audience_role  --07/18/2014
 WHERE audience_id = p_in_audience_id;

DELETE audience
 WHERE audience_id = p_in_audience_id;

END;
  
  p_out_return_code := '00';
EXCEPTION
   WHEN Valid_audience THEN
   p_out_return_code := v_msg;
   WHEN OTHERS THEN
     p_out_return_code := SQLERRM;
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                             'Audience_id: '||p_in_audience_id||                            
                             ' --> '||SQLERRM,null);
     COMMIT;
END  prc_delete_audience ;
/