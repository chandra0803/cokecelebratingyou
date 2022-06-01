CREATE OR REPLACE PROCEDURE prc_stg_pax_record_insert
   ( p_in_stg_rec stage_pax_import_record%ROWTYPE,
     p_out_return_code  OUT NUMBER)
 IS
   /*******************************************************************************
   -- Purpose: To insert records into STAGE_PAX_IMPORT_RECORD table
   --
   -- Person        Date       Comments
   -- -----------   --------   -----------------------------------------------------
   -- D Murray      04/28/2006 Changed to have a return code so that calling procedure
   --                          will know if insert was successful or not, changed
   --                          dbms_outputs to execution_log entries. (Bug 12148)
   -- Arun S        05/12/2011 CR Add Language to PAX file
   -- Chidamba      11/20/2012 G5-Change Request - Add 10 more characteristic(6-15)
   -- Chidamba      03/08/2013 Enhancement: Add (Single Sign on)SSO ID to Pax file load 
                               which is unique for each pax   
   *******************************************************************************/
  -- EXECUTION LOG VARIABLES
  v_process_name          execution_log.process_name%type  := 'PRC_STG_PAX_RECORD_INSERT';
  v_release_level         execution_log.release_level%type := '1';

BEGIN 

  INSERT INTO  stage_pax_import_record   (import_record_id, import_file_id, action_type, user_name,
       first_name, middle_name, last_name, suffix, ssn,
       birth_date, gender, active, email_address,
       email_address_type, text_message_address, address_1,
       address_2, address_3, address_4, address_5, address_6,
       city, state, country_id, country_code, postal_code,
       address_type, personal_phone_number, business_phone_number,
       cell_phone_number, employer_name, job_position,
       department, hire_date, termination_date, node_id1,
       node_id2, node_id3, node_id4, node_id5, node_name1,
       node_name2, node_name3, node_name4, node_name5,
       node_relationship1, node_relationship2, node_relationship3,
       node_relationship4, node_relationship5, characteristic_id1,
       characteristic_id2, characteristic_id3, characteristic_id4,
       characteristic_id5, characteristic_id6, characteristic_id7,
       characteristic_id8, characteristic_id9, characteristic_id10,
       characteristic_id11, characteristic_id12, characteristic_id13,
       characteristic_id14, characteristic_id15, characteristic_name1,
       characteristic_name2, characteristic_name3, characteristic_name4, 
       characteristic_name5, characteristic_name6, characteristic_name7,
       characteristic_name8, characteristic_name9, characteristic_name10, 
       characteristic_name11, characteristic_name12, characteristic_name13,
       characteristic_name14, characteristic_name15, characteristic_value1, 
       characteristic_value2, characteristic_value3, characteristic_value4,
       characteristic_value5, characteristic_value6, characteristic_value7,
       characteristic_value8, characteristic_value9, characteristic_value10,
       characteristic_value11, characteristic_value12, characteristic_value13,
       characteristic_value14, characteristic_value15, role_id1, 
       role_id2, role_id3, role_id4, 
       role_id5, role_description1, role_description2,
       role_description3, role_description4, role_description5,
       created_by, date_created, employer_id, user_id,status,
       language_id,      --05/12/2011
       sso_id            --03/08/2013
       )
         VALUES
      (p_in_stg_rec.import_record_id,
       p_in_stg_rec.import_file_id, 
       p_in_stg_rec.action_type, 
       p_in_stg_rec.user_name,
       p_in_stg_rec.first_name, 
       p_in_stg_rec.middle_name, 
       p_in_stg_rec.last_name, 
       p_in_stg_rec.suffix, 
       p_in_stg_rec.ssn,
       p_in_stg_rec.birth_date, 
       p_in_stg_rec.gender, 
       p_in_stg_rec.active, 
       p_in_stg_rec.email_address,
       p_in_stg_rec.email_address_type, 
       p_in_stg_rec.text_message_address, 
       p_in_stg_rec.address_1,
       p_in_stg_rec.address_2, 
       p_in_stg_rec.address_3, 
       p_in_stg_rec.address_4, 
       p_in_stg_rec.address_5, 
       p_in_stg_rec.address_6,
       p_in_stg_rec.city, 
       p_in_stg_rec.state, 
       p_in_stg_rec.country_id, 
       p_in_stg_rec.country_code, 
       p_in_stg_rec.postal_code,
       p_in_stg_rec.address_type, 
       p_in_stg_rec.personal_phone_number, 
       p_in_stg_rec.business_phone_number,
       p_in_stg_rec.cell_phone_number, 
       p_in_stg_rec.employer_name, 
       p_in_stg_rec.job_position,
       p_in_stg_rec.department, 
       p_in_stg_rec.hire_date, 
       p_in_stg_rec.termination_date, 
       p_in_stg_rec.node_id1,
       p_in_stg_rec.node_id2, 
       p_in_stg_rec.node_id3, 
       p_in_stg_rec.node_id4, 
       p_in_stg_rec.node_id5, 
       p_in_stg_rec.node_name1,
       p_in_stg_rec.node_name2, 
       p_in_stg_rec.node_name3, 
       p_in_stg_rec.node_name4, 
       p_in_stg_rec.node_name5,
       p_in_stg_rec.node_relationship1, 
       p_in_stg_rec.node_relationship2, 
       p_in_stg_rec.node_relationship3,
       p_in_stg_rec.node_relationship4, 
       p_in_stg_rec.node_relationship5, 
       p_in_stg_rec.characteristic_id1,
       p_in_stg_rec.characteristic_id2, 
       p_in_stg_rec.characteristic_id3, 
       p_in_stg_rec.characteristic_id4,
       p_in_stg_rec.characteristic_id5, 
       p_in_stg_rec.characteristic_id6,
       p_in_stg_rec.characteristic_id7,
       p_in_stg_rec.characteristic_id8,
       p_in_stg_rec.characteristic_id9,
       p_in_stg_rec.characteristic_id10,
       p_in_stg_rec.characteristic_id11,
       p_in_stg_rec.characteristic_id12,
       p_in_stg_rec.characteristic_id13,
       p_in_stg_rec.characteristic_id14,
       p_in_stg_rec.characteristic_id15,
       p_in_stg_rec.characteristic_name1,
       p_in_stg_rec.characteristic_name2, 
       p_in_stg_rec.characteristic_name3,
       p_in_stg_rec.characteristic_name4, 
       p_in_stg_rec.characteristic_name5,
       p_in_stg_rec.characteristic_name6,
       p_in_stg_rec.characteristic_name7,
       p_in_stg_rec.characteristic_name8,
       p_in_stg_rec.characteristic_name9,
       p_in_stg_rec.characteristic_name10,
       p_in_stg_rec.characteristic_name11,
       p_in_stg_rec.characteristic_name12,
       p_in_stg_rec.characteristic_name13,
       p_in_stg_rec.characteristic_name14,
       p_in_stg_rec.characteristic_name15,
       p_in_stg_rec.characteristic_value1, 
       p_in_stg_rec.characteristic_value2,
       p_in_stg_rec.characteristic_value3, 
       p_in_stg_rec.characteristic_value4,
       p_in_stg_rec.characteristic_value5, 
       p_in_stg_rec.characteristic_value6,
       p_in_stg_rec.characteristic_value7,
       p_in_stg_rec.characteristic_value8,
       p_in_stg_rec.characteristic_value9,
       p_in_stg_rec.characteristic_value10,
       p_in_stg_rec.characteristic_value11,
       p_in_stg_rec.characteristic_value12,
       p_in_stg_rec.characteristic_value13,
       p_in_stg_rec.characteristic_value14,
       p_in_stg_rec.characteristic_value15,
       p_in_stg_rec.role_id1, 
       p_in_stg_rec.role_id2, 
       p_in_stg_rec.role_id3,
       p_in_stg_rec.role_id4, 
       p_in_stg_rec.role_id5, 
       p_in_stg_rec.role_description1, 
       p_in_stg_rec.role_description2,
       p_in_stg_rec.role_description3, 
       p_in_stg_rec.role_description4, 
       p_in_stg_rec.role_description5,
       p_in_stg_rec.created_by, 
       p_in_stg_rec.date_created, 
       p_in_stg_rec.employer_id, 
       p_in_stg_rec.user_id,
       p_in_stg_rec.status,
       p_in_stg_rec.language_id,     --05/12/2011
       p_in_stg_rec.sso_id          --03/08/2013
       ) ;

  p_out_return_code := 0;
EXCEPTION
  WHEN OTHERS THEN
     p_out_return_code := 99;
     prc_execution_log_entry(v_process_name,v_release_level,'ERROR',
                             'Import_file_id: '||p_in_stg_rec.import_file_id||
                             'Import_record_id: '||p_in_stg_rec.import_record_id||
                             ' --> '||SQLERRM,null);
     COMMIT;
END;
/