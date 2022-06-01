CREATE OR REPLACE PROCEDURE prc_conv_script_pax_term_date (p_out_return_code OUT   NUMBER)
IS
/*******************************************************************************
   -- Purpose: Procedure to update the newly added termination_date in participant table based upon
   --          participant_employer table 

   --
   -- Person        Date       Comments
   -- -----------   --------   -----------------------------------------------------
   -- Suresh J      04/21/2017  Initial Creation.
   *******************************************************************************/

   c_process_name       CONSTANT execution_log.process_name%type := UPPER('prc_conv_script_pax_term_date');
   c_release_level      CONSTANT execution_log.release_level%type := '1.0';

   v_msg                execution_log.text_line%TYPE;
   v_cms_seq_id     NUMBER;    
   v_content_seq_id     NUMBER;     
  
   CURSOR cur_pax_term_date  is
   SELECT pe.user_id,
          pe.termination_date,
          pe.date_created
   FROM participant_employer pe,
        participant p
   WHERE pe.user_id = p.user_id AND
         p.status = 'inactive'  AND
         pe.date_created = (SELECT MAX(pe2.date_created)
                                FROM participant_employer pe2
                                WHERE pe2.user_id = pe.user_id ) ;
   
   BEGIN
        
        FOR cur_rec IN cur_pax_term_date LOOP
        
        UPDATE participant
        SET termination_date = CASE WHEN cur_rec.termination_date IS NOT NULL THEN cur_rec.termination_date
                                    ELSE NULL
                               END  
        WHERE user_id = cur_rec.user_id ;
        
        END LOOP;
    
   p_out_return_code :=0;
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_msg, NULL);

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_msg||SQLERRM, NULL);
      p_out_return_code :=99;

END prc_conv_script_pax_term_date;
/
