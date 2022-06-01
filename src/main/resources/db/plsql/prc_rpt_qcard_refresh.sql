CREATE OR REPLACE procedure prc_rpt_qcard_refresh (p_user_id      IN  NUMBER,
                                                   p_start_date             IN  DATE,
                                                   p_end_date               IN  DATE,
                                                   p_return_code           OUT NUMBER,
                                                   p_out_error_message         OUT VARCHAR2)
  IS
/*******************************************************************************

   Purpose:  Populate the prc_rpt_qcard_refresh reporting table

   Person                      Date       Comments
   -----------                ---------- -----
   ????????                  ???????           Initial
-- Suresh J                01/29/2015   Bug #57300 - Fixed the issue of Node Name Changes that are not reflected in reports 
-- nagarajs                02/01/2016   Bug 65458 - Awards Received extract fetching duplicate record for On The Spot
-- chidamba                08/22/2017   G6-2886 - The OTS deposited award is not displayed for the PAX who not having Employer details 
-- chidamba                03/01/2018   G6-3432	- Changes to the Report due to OTS cards Enhancement 
*******************************************************************************/
  
CURSOR cur_qcard_detail IS  
 SELECT a.transaction_date AS trans_date ,
        a.user_id,
        b.first_name AS pax_first_name,
        b.last_name AS pax_last_name,
        fnc_format_user_name(b.last_name, b.first_name, b.middle_name, b.suffix) AS participant_name,
        a.journal_id ,
        ua.country_id,
        d.NAME AS node_name,
        d.node_id,
        SUBSTR(a.transaction_description,34,LENGTH(a.transaction_description)) AS cert_num,
        SUBSTR(a.transaction_description,34,8) AS first_cert,
        SUBSTR(a.transaction_description,42,LENGTH(a.transaction_description)) AS sec_cert,
        a.transaction_amt AS trans_amount,
        fnc_get_badge_count_by_user(a.user_id,null,trunc(a.transaction_date)) badges_earned,
        pe.position_type,
        pe.department_type department,
        NULL batch_description, --03/01/2018
        NULL batch_number,   --03/01/2018
        NULL program_number  --03/01/2018
   FROM journal A,
        application_user b,
        user_node C,
        user_address ua,
        vw_curr_pax_employer pe,
        node d
  WHERE a.user_id = b.user_id
    AND a.user_id  = ua.user_id
    AND a.user_id = pe.user_id(+)   --08/22/2017  G6-2886
    AND b.user_id = c.user_id
    AND c.is_primary =1 
    AND ua.is_primary = 1       --02/01/2016
    AND c.node_id = d.node_id
    AND SUBSTR(a.transaction_description,0,33) = 'CERTIFICATE CONVERSION for Cert: '
    AND (p_start_date < A.date_created  AND A.date_created <= p_end_date)
 --03/01/2018 Start OTS enhancement--
 UNION ALL 
 SELECT a.transaction_date AS trans_date ,
        a.user_id,
        b.first_name AS pax_first_name,
        b.last_name AS pax_last_name,
        fnc_format_user_name(b.last_name, b.first_name, b.middle_name, b.suffix) AS participant_name,
        a.journal_id ,
        ua.country_id,
        d.NAME AS node_name,
        d.node_id,
        card_reference_number AS cert_num,
        NULL AS first_cert,
        NULL AS sec_cert,
        a.transaction_amt AS trans_amount,
        fnc_get_badge_count_by_user(a.user_id,null,trunc(a.transaction_date)) badges_earned,
        pe.position_type,
        pe.department_type department,
        NVL(bh.batch_description,a.batch_description) batch_description,
        batch_number,
        program_number
   FROM (SELECT j.*,SUBSTR(SUBSTR(transaction_description,1,
                INSTR(transaction_description,'-',-1,2)-1)
                ,INSTR(SUBSTR(transaction_description,1,
                INSTR(transaction_description,'-',-1,2)-1)
                ,'[',-1)+1) program_number,               
                SUBSTR(SUBSTR(transaction_description,1,
                INSTR(transaction_description,']',-1)-1),
                INSTR(SUBSTR(transaction_description,1,
                INSTR(transaction_description,']',-1)-1),'-',-1,1)+1)  batch_number,
                SUBSTR(transaction_description,
                INSTR(transaction_description,'-',-1,1)+1) card_reference_number,
                TRIM(REPLACE(SUBSTR(transaction_description,1,
                  INSTR(transaction_description,'[',-1,1)-1),'CERTIFICATE CONVERSION:'))||'#'  batch_description 
           FROM journal j
          WHERE SUBSTR(transaction_description,0,23) = 'CERTIFICATE CONVERSION:') A,
         (SELECT ob.ots_cms_asset_code batch_description,
                 ob.ots_batch_nbr,
                 op.ots_program_nbr 
            FROM ots_program op, ots_batch ob 
           WHERE op.ots_program_id = ob.ots_program_id) bh,
        application_user b,
        user_node C,
        user_address ua,
        vw_curr_pax_employer pe,
        node d
  WHERE a.user_id = b.user_id
    AND a.user_id  = ua.user_id
    AND a.user_id = pe.user_id(+) 
    AND b.user_id = c.user_id
    AND c.is_primary =1 
    AND ua.is_primary = 1      
    AND c.node_id = d.node_id
    AND (a.program_number = bh.ots_program_nbr(+) AND a.batch_number = bh.ots_batch_nbr(+))
    AND (p_start_date < A.date_created  AND A.date_created <= p_end_date);
    --03/01/2018 END OTS enhancement--;

            
  c_process_name        CONSTANT execution_log.process_name%TYPE := UPPER('prc_rpt_qcard_refresh');
  c_release_level       CONSTANT execution_log.release_level%TYPE := 1.0;
  
  v_stage               execution_log.text_line%TYPE;
  v_rec_cnt             INTEGER;
  v_rpt_qcard_dtl_id    rpt_qcard_detail.QCARD_DETAIL_ID%TYPE;
  
  v_tab_node_id         dbms_sql.number_table;   --01/29/2015  
  v_tab_node_name       dbms_sql.varchar2_table;  --01/29/2015  
  
  v_tab_batch_id         dbms_sql.number_table;   --03/01/2018   
  v_tab_batch_name       dbms_sql.varchar2_table; --03/01/2018  
  
    --Cursor to pick modified node name   
  CURSOR cur_node_changed IS          --01/29/2015
    SELECT node_id,
           NAME
      FROM node
     WHERE date_modified BETWEEN p_start_date AND p_end_date;  
   
   CURSOR cur_batch_changed IS  --03/01/2018 new cursor OTS Enhancement   
   SELECT ots_batch_nbr,
          ots_cms_asset_code
     FROM ots_batch o , 
          rpt_qcard_detail r
    WHERE r.batch_number = o.ots_batch_nbr
     AND TRUNC(o.date_created) BETWEEN p_start_date AND p_end_date;  
    
BEGIN

  v_stage := 'Start';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);
  

  FOR rec_qcard_detail IN cur_qcard_detail LOOP

         v_stage := 'INSERT rpt_qcard_detail';
      INSERT INTO rpt_qcard_detail
                  (qcard_detail_id,                   
                   node_id,
                   node_name,       
                   trans_date,           
                   user_id,                   
                   user_first_name,                   
                   user_last_name,       
                   user_full_name,         
                   transaction_amount,
                   date_created,
                   created_by,                  
                   journal_id,
                   country_id,  
                   first_cert,
                   sec_cert,
                   cert_num,                  
                   badges_earned,
                   position_type,
                   department,
                   version       ,
                   batch_description, --03/01/2018 
                   batch_number,      --03/01/2018
                   program_number     --03/01/2018
                   )
           VALUES (qcard_detail_id_seq.nextval,                  
                   rec_qcard_detail.node_id,
                   rec_qcard_detail.node_name,
                   rec_qcard_detail.trans_date,                  
                   rec_qcard_detail.user_id,                  
                   rec_qcard_detail.pax_first_name,                   
                   rec_qcard_detail.pax_last_name,
                   rec_qcard_detail.participant_name  ,                 
                   rec_qcard_detail.trans_amount,
                   SYSDATE,
                   p_user_id,                  
                   rec_qcard_detail.journal_id,
                   rec_qcard_detail.country_id,      
                   rec_qcard_detail.first_cert,
                   rec_qcard_detail.sec_cert,
                   rec_qcard_detail.cert_num,                  
                   rec_qcard_detail.badges_earned, 
                   rec_qcard_detail.position_type,
                   rec_qcard_detail.department,
                   1                   ,
                   rec_qcard_detail.batch_description,--03/01/2018 new columns
                   rec_qcard_detail.batch_number,     --03/01/2018 new columns
                   rec_qcard_detail.program_number);  --03/01/2018 new columns  
   
  END LOOP;
  v_stage := 'Open and Fetch cursor to pick modified node name';      --01/29/2015
  OPEN cur_node_changed;   --01/29/2015
  FETCH cur_node_changed BULK COLLECT
   INTO v_tab_node_id,
        v_tab_node_name;
  CLOSE cur_node_changed;   

  v_stage := 'Update modified node name in rpt table';    --01/29/2015
  FORALL indx IN v_tab_node_id.FIRST .. v_tab_node_id.LAST    --01/29/2015
    UPDATE rpt_qcard_detail
       SET node_name     = DECODE (node_id, v_tab_node_id(indx), v_tab_node_name(indx), node_name),
           date_modified = SYSDATE,
           modified_by   = p_user_id,
           VERSION       = VERSION + 1
     WHERE (node_id        = v_tab_node_id(indx)
            AND node_name != v_tab_node_name(indx)
            );
  /************************************--03/01/2018 Start OTS Enhancement *************************************/
  v_stage := 'Open and Fetch cursor to pick modified node name';      
  OPEN cur_batch_changed;   
  FETCH cur_batch_changed BULK COLLECT
   INTO v_tab_batch_id,
        v_tab_batch_name;
  CLOSE cur_batch_changed;            
  
  v_stage := 'Update modified batch name in rpt table';   
  FORALL indx IN v_tab_batch_id.FIRST .. v_tab_batch_id.LAST   
    UPDATE rpt_qcard_detail
       SET batch_description = v_tab_batch_name(indx),
           date_modified = SYSDATE,
           modified_by   = p_user_id,
           VERSION       = VERSION + 1
     WHERE  (batch_number   = v_tab_batch_id(indx)
            AND NVL(batch_description,'x') != v_tab_batch_name(indx));       
  /************************************--03/01/2018 End OTS Enhancement *************************************/          

  v_stage := 'Success';
  prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage, NULL);

  p_return_code := 0;

EXCEPTION
  WHEN OTHERS THEN
    p_return_code := 99;
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', v_stage || ': ' || SQLCODE || ', ' || SQLERRM, NULL);
END prc_rpt_qcard_refresh;
/
