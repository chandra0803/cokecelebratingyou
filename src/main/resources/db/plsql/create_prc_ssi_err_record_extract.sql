CREATE OR REPLACE PROCEDURE prc_ssi_err_record_extract
 (
      p_in_import_file_id     IN     NUMBER,
      p_out_return_code       OUT    NUMBER,
      p_out_ref_cursor        OUT    SYS_REFCURSOR
  )

   /***********************************************************************************
      Purpose:  Extracts SSI Error records 

      Person             Date           Comments
      -----------        ----------     --------------------------------------------
    Suresh J            01/23/2017       Initial Version
  ************************************************************************************/

      IS

      -- constants
      c_delimiter        constant  varchar2(1)  := ',' ;
      c2_delimiter       constant  varchar2(1)  := '"' ;
      c_process_name     constant  varchar2(30) := 'prc_ssi_err_record_extract';
      
      --Contest Types
      c_ssicontestatn    constant  varchar2(30) := 'ssicontestatn';
      c_ssicontestdtgt   constant  varchar2(30) := 'ssicontestdtgt';
      c_ssicontestobj    constant  varchar2(30) := 'ssicontestobj';
      c_ssicontestsr     constant  varchar2(30) := 'ssicontestsr';
      c_ssicontestsiu    constant  varchar2(30) := 'ssicontestsiu';

      v_file_name        varchar2(100);
      v_status           varchar2(100);

      BEGIN

      SELECT substr(file_name,1,instr(file_name,'_',1)-1)
      INTO   v_file_name
      FROM import_file 
      WHERE import_file_id = p_in_import_file_id;


      IF v_file_name in (c_ssicontestatn,c_ssicontestdtgt,c_ssicontestobj,c_ssicontestsr,c_ssicontestsiu ) THEN
      OPEN p_out_ref_cursor FOR
      SELECT textline FROM (
      SELECT 1,
               c2_delimiter||'Import Record ID'         ||c2_delimiter||c_delimiter||
               c2_delimiter||'Import File ID'           ||c2_delimiter||c_delimiter||        
               c2_delimiter||'Error Message'            ||c2_delimiter||c_delimiter||
               c2_delimiter||'Field Name'               ||c2_delimiter||c_delimiter||        
               c2_delimiter||'Field Value'              ||c2_delimiter||c_delimiter||
               c2_delimiter||'Error Message 2'          ||c2_delimiter||c_delimiter||        
               c2_delimiter||'Error Message 3'          ||c2_delimiter||c_delimiter||
               c2_delimiter||'User Name'                ||c2_delimiter||c_delimiter|| 
               c2_delimiter||'First Name'               ||c2_delimiter||c_delimiter||
               c2_delimiter||'Last Name'                ||c2_delimiter||c_delimiter||
               c2_delimiter||'Role'                     ||c2_delimiter||c_delimiter||
               CASE WHEN v_file_name = c_ssicontestobj THEN
               c2_delimiter||'Objective Description'    ||c2_delimiter||c_delimiter||
               c2_delimiter||'Objective Amount'         ||c2_delimiter||c_delimiter||
               c2_delimiter||'Objective Payout'         ||c2_delimiter||c_delimiter||
               c2_delimiter||'Other Payout Description' ||c2_delimiter||c_delimiter||
               c2_delimiter||'Other Value'              ||c2_delimiter||c_delimiter||
               c2_delimiter||'Bonus For Every'          ||c2_delimiter||c_delimiter||
               c2_delimiter||'Bonus Payout'             ||c2_delimiter||c_delimiter||
               c2_delimiter||'Bonus Cap'                ||c2_delimiter                                                                                            
                WHEN v_file_name = c_ssicontestsiu THEN
               c2_delimiter||'Baseline'                 ||c2_delimiter||c_delimiter        
               WHEN v_file_name = c_ssicontestatn THEN
               c2_delimiter||'Activity Description'     ||c2_delimiter||c_delimiter||
               c2_delimiter||'Activity Amount'          ||c2_delimiter||c_delimiter||
               c2_delimiter||'Payout Amount'            ||c2_delimiter||c_delimiter||
               c2_delimiter||'Payout Description'       ||c2_delimiter||c_delimiter||
               c2_delimiter||'Award Issue Value'        ||c2_delimiter||c_delimiter
                ELSE NULL 
               END 
               AS Textline
      FROM dual
      UNION  ALL
      SELECT
        (ROWNUM+1),
            c2_delimiter||import_record_id      ||c2_delimiter||c_delimiter||
            c2_delimiter||import_file_id        ||c2_delimiter||c_delimiter||        
            c2_delimiter||error_message         ||c2_delimiter||c_delimiter||
            c2_delimiter||field_name            ||c2_delimiter||c_delimiter||        
            c2_delimiter||field_value           ||c2_delimiter||c_delimiter||
            c2_delimiter||error_message_2       ||c2_delimiter||c_delimiter||        
            c2_delimiter||error_message_3       ||c2_delimiter||c_delimiter||
            c2_delimiter||user_name             ||c2_delimiter||c_delimiter||
            c2_delimiter||first_name            ||c2_delimiter||c_delimiter||
            c2_delimiter||last_name             ||c2_delimiter||c_delimiter||        
            c2_delimiter||role                  ||c2_delimiter||c_delimiter||
        CASE WHEN v_file_name = c_ssicontestobj THEN
            c2_delimiter||objective_description ||c2_delimiter||c_delimiter||
            c2_delimiter||objective_amount      ||c2_delimiter||c_delimiter||
            c2_delimiter||objective_payout      ||c2_delimiter||c_delimiter||
            c2_delimiter||payout_description    ||c2_delimiter||c_delimiter||
            c2_delimiter||payout_value          ||c2_delimiter||c_delimiter||
            c2_delimiter||bonus_for_every       ||c2_delimiter||c_delimiter||
            c2_delimiter||bonus_payout          ||c2_delimiter||c_delimiter||
            c2_delimiter||bonus_payout_cap      ||c2_delimiter       
            WHEN v_file_name = c_ssicontestsiu THEN
            c2_delimiter||baseline              ||c2_delimiter        
            WHEN v_file_name = c_ssicontestatn THEN
            c2_delimiter||activity_description  ||c2_delimiter||c_delimiter||
            c2_delimiter||activity_amount       ||c2_delimiter||c_delimiter||
            c2_delimiter||payout_amount         ||c2_delimiter||c_delimiter||
            c2_delimiter||payout_description    ||c2_delimiter||c_delimiter||
            c2_delimiter||award_issue_value     ||c2_delimiter||c_delimiter
            ELSE NULL 
        END 
        AS Textline
        FROM 
        (SELECT 
                CASE    WHEN s_obj.import_file_id  IS NOT NULL THEN s_obj.import_record_id
                        WHEN s_dtgt.import_file_id IS NOT NULL THEN s_dtgt.import_record_id
                        WHEN s_sr.import_file_id   IS NOT NULL THEN s_sr.import_record_id
                        WHEN s_siu.import_file_id  IS NOT NULL THEN s_siu.import_record_id
                        WHEN s_atn.import_file_id  IS NOT NULL THEN s_atn.import_record_id
                        ELSE NULL
                END import_record_id,        
                p_in_import_file_id as import_file_id,
                ie.cm_error_desc    as error_message,
                ie.param1 as field_name,
                ie.param2 as field_value,
                ie.param3 as error_message_2,
                ie.param4 as error_message_3,
                CASE    WHEN s_obj.import_file_id  IS NOT NULL THEN s_obj.user_name
                        WHEN s_dtgt.import_file_id IS NOT NULL THEN s_dtgt.user_name
                        WHEN s_sr.import_file_id   IS NOT NULL THEN s_sr.user_name
                        WHEN s_siu.import_file_id  IS NOT NULL THEN s_siu.user_name
                        WHEN s_atn.import_file_id  IS NOT NULL THEN s_atn.user_name
                        ELSE NULL
                END user_name,        
                CASE    WHEN s_obj.import_file_id  IS NOT NULL THEN s_obj.first_name
                        WHEN s_dtgt.import_file_id IS NOT NULL THEN s_dtgt.first_name
                        WHEN s_sr.import_file_id   IS NOT NULL THEN s_sr.first_name
                        WHEN s_siu.import_file_id  IS NOT NULL THEN s_siu.first_name
                        WHEN s_atn.import_file_id  IS NOT NULL THEN s_atn.first_name
                        ELSE NULL
                END first_name,        
                CASE    WHEN s_obj.import_file_id  IS NOT NULL THEN s_obj.last_name
                        WHEN s_dtgt.import_file_id IS NOT NULL THEN s_dtgt.last_name
                        WHEN s_sr.import_file_id   IS NOT NULL THEN s_sr.last_name
                        WHEN s_siu.import_file_id  IS NOT NULL THEN s_siu.last_name
                        WHEN s_atn.import_file_id  IS NOT NULL THEN s_atn.last_name
                        ELSE NULL
                END last_name,        
                CASE    WHEN s_obj.import_file_id  IS NOT NULL THEN s_obj.role
                        WHEN s_dtgt.import_file_id IS NOT NULL THEN s_dtgt.role
                        WHEN s_sr.import_file_id   IS NOT NULL THEN s_sr.role
                        WHEN s_siu.import_file_id  IS NOT NULL THEN s_siu.role
                        WHEN s_atn.import_file_id  IS NOT NULL THEN s_atn.role
                        ELSE NULL
                END role,        
                s_obj.objective_description,
                s_obj.objective_amount,
                s_obj.objective_payout,
                CASE    WHEN s_obj.import_file_id  IS NOT NULL THEN s_obj.other_payout_description
                        WHEN s_atn.import_file_id  IS NOT NULL THEN s_atn.payout_description
                        ELSE NULL
                END payout_description,        
                s_obj.other_value as payout_value,
                s_obj.bonus_for_every,
                s_obj.bonus_payout,
                s_obj.bonus_payout_cap,
                s_siu.baseline,
                s_atn.activity_description,
                s_atn.activity_amount,
                s_atn.payout_amount,
                s_atn.award_issue_value
         FROM stage_ssi_objective_import s_obj,
              stage_ssi_dtgt_import s_dtgt,
              stage_ssi_stack_rank_import s_sr,
              stage_ssi_step_it_up_import s_siu,
              stage_ssi_atn_import s_atn,
              (select ie.*,
                      replace(replace(cm.cms_value,'"{1}"','*'||ie.param2||'*'),'"{0}"','*'||ie.param1||'*') as cm_error_desc 
                from (select c.*,
                             c.asset_code||'.'||c.KEY as item_key
                        from vw_cms_asset_value c 
                        where c.locale = 'en_US') cm,
                     import_record_error ie
                where cm.item_key = ie.item_key 
                      and ie.import_file_id = p_in_import_file_id ) ie   
         WHERE ie.import_file_id = p_in_import_file_id        AND
               ie.import_file_id = s_obj.import_file_id      (+)  AND
               ie.import_file_id = s_dtgt.import_file_id     (+)  AND
               ie.import_file_id = s_sr.import_file_id       (+)  AND
               ie.import_file_id = s_siu.import_file_id      (+)  AND                                           
               ie.import_file_id = s_atn.import_file_id      (+)  AND               
               ie.import_record_id = s_obj.import_record_id  (+)  AND
               ie.import_record_id = s_dtgt.import_record_id (+)  AND
               ie.import_record_id = s_sr.import_record_id   (+)  AND
               ie.import_record_id = s_siu.import_record_id  (+)  AND                                           
               ie.import_record_id = s_atn.import_record_id  (+)
         ORDER BY 1               
               )) s ;


      ELSE
      
       p_out_return_code :=99;
       OPEN p_out_ref_cursor FOR SELECT NULL FROM DUAL;
       prc_execution_log_entry (c_process_name,1,'ERROR','Invalid SSI file or Invalid file name -  p_in_import_file_id:'|| p_in_import_file_id|| ' '|| SQLERRM,NULL);
        

      END IF;  -- End of Contest Type Extract SQL Condition

    p_out_return_code :=00 ;

     EXCEPTION WHEN OTHERS THEN
       p_out_return_code :=99;
       OPEN p_out_ref_cursor FOR SELECT NULL FROM DUAL;
       prc_execution_log_entry (c_process_name,1,'ERROR','Process failed for import_file_id:  '|| p_in_import_file_id|| ' '|| SQLERRM,NULL);
      END prc_ssi_err_record_extract;
/
