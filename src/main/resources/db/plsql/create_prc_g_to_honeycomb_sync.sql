CREATE OR REPLACE PROCEDURE prc_g_to_honeycomb_sync ( p_in_user_id_list           IN  VARCHAR2,
                                                            p_out_return_code           OUT NUMBER,
                                                            p_out_application_user      OUT SYS_REFCURSOR,
                                                            p_out_participant           OUT SYS_REFCURSOR,
                                                            p_out_participant_address   OUT SYS_REFCURSOR,
                                                            p_out_participant_email     OUT SYS_REFCURSOR,
                                                            p_out_participant_phone     OUT SYS_REFCURSOR,
                                                            p_out_org_unit_participant  OUT SYS_REFCURSOR,
                                                            p_out_participant_employer  OUT SYS_REFCURSOR,
                                                            p_out_participant_char      OUT SYS_REFCURSOR
                                                            )
AS
 /*******************************************************************************
  -- Purpose: Return the pax details to sync into honeycomb (GQ) database
  --
  -- Person             Date         Comments
  -- -----------        --------    -----------------------------------------------------
  -- nagarajs          07/10/2017    Initial Creation
  --Ravi Dhanekula     07/12/2017    Restricted application_user data ref cursor to the user list provided.
  *******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_G_TO_HONEYCOMB_SYNC';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   
   gc_ref_text_user_id      CONSTANT gtt_id_list.ref_text_1%TYPE := 'user_id'; 
   
   v_stage                  VARCHAR2(500); 
BEGIN
   
  p_out_return_code := 0;
  
  v_stage := 'stage input lists';
  DELETE gtt_id_list; 
  pkg_report_common.p_stage_search_criteria(p_in_user_id_list,  gc_ref_text_user_id, 1);  
  
  v_stage := 'Open p_out_application_user';
  OPEN p_out_application_user FOR
    SELECT user_id,
           user_name,
           first_name, 
           last_name, 
           middle_name, 
           suffix, 
           user_type 
      FROM application_user a,
           gtt_id_list gil_u
      WHERE a.user_id = gil_u.id;
      
  v_stage := 'Open p_out_participant';
  OPEN p_out_participant FOR
    SELECT user_name,
           TO_CHAR(birth_date, 'MM/DD') AS birth_date,
           0                            AS first_time_setup_done,
           is_active                    AS is_enabled,
           1                            AS account_non_expired,
           1                            AS credentials_non_expired,
           1                            AS account_non_locked,
           fnc_java_decrypt( p.awardbanq_nbr) AS bank_account_number,
           avatar_original              AS avatar_url,
           allow_public_birth_date,
           allow_public_information     AS allow_public_phone,
           allow_public_information     AS allow_public_email,
           hire_date,
           0                            AS login_fail_count,
           un.role                      AS highest_role_level,
           suspension_status,
           language_id                  AS locale,
           sso_id,
           fnc_java_decrypt( centrax_id) AS om_participant_id
      FROM application_user au,
           participant p,
           (SELECT user_id, role FROM user_node un WHERE is_primary = 1) un,
           vw_curr_pax_employer vw,
           gtt_id_list gil_u
     WHERE au.user_id = p.user_id
       AND au.user_id = un.user_id (+)
       AND au.user_id = vw.user_id (+)
       AND au.user_id = gil_u.id;
       
  v_stage := 'Open p_out_participant_address';
  OPEN p_out_participant_address FOR
    SELECT au.user_name,
           country_code, 
           address_type, 
           addr1 AS address1, 
           addr2 AS address2, 
           city, 
           SUBSTR(state,INSTR(state,'_') +1) AS state_code, 
           postal_code, 
           is_primary
      FROM application_user au,
           user_address ua,
           country c,
           gtt_id_list gil_u
     WHERE au.user_id = ua.user_id
       AND ua.country_id = c.country_id
       AND au.user_id = gil_u.id;
        
  v_stage := 'Open p_out_participant_email';
  OPEN p_out_participant_email FOR        
    SELECT au.user_name,
           is_primary, 
           email_type, 
           email_addr AS email_address, 
           NVL(email_status, 1) AS email_status
      FROM application_user au,
           user_email_address uea,
           gtt_id_list gil_u
     WHERE au.user_id = uea.user_id
       AND au.user_id = gil_u.id;
  
  v_stage := 'Open p_out_participant_phone';
  OPEN p_out_participant_phone FOR  
    SELECT au.user_name,
           up.phone_type, 
           up.phone_nbr AS phone_number, 
           up.phone_ext, 
           c.awardbanq_country_abbrev AS country_phone_code, 
           is_primary
      FROM application_user au,
           user_phone up,
           country c,
           gtt_id_list gil_u
     WHERE au.user_id = up.user_id
       AND up.country_phone_code = c.country_code (+)
       AND au.user_id = gil_u.id;
        
  v_stage := 'Open p_out_org_unit_participant';
  OPEN p_out_org_unit_participant FOR        
    SELECT au.user_name,
           n.name AS org_unit_name, 
           un.role, 
           un.status, 
           un.is_primary
      FROM application_user au,
           user_node un,
           node n,
           gtt_id_list gil_u
     WHERE au.user_id = un.user_id
       AND un.node_id = n.node_id
       AND au.user_id = gil_u.id;
        
  v_stage := 'Open p_out_participant_employer';
  OPEN p_out_participant_employer FOR
    SELECT au.user_id,au.user_name,
           e.name AS employer_name, 
           vw_po.cms_name AS position_type, 
           vw_dt.cms_name AS department_type, 
           0 AS participant_employer_index, 
           termination_date AS termination_date
      FROM application_user au,
           vw_curr_pax_employer vw,
           employer e,
           vw_cms_code_value vw_po,
           vw_cms_code_value vw_dt,
           gtt_id_list gil_u
     WHERE au.user_id = vw.user_id
       AND vw.employer_id = e.employer_id
       AND vw_po.asset_code (+) = 'picklist.positiontype.items'
       AND vw.position_type = vw_po.cms_code (+)
       AND vw_po.locale (+)= 'en_US'
       AND vw_po.cms_status (+) = 'true'
       AND vw_dt.asset_code (+) = 'picklist.department.type.items'
       AND vw.department_type = vw_dt.cms_code (+)
       AND vw_dt.locale (+)= 'en_US'
       AND vw_po.cms_status (+) = 'true'
       AND au.user_id = gil_u.id;
              
  v_stage := 'Open p_out_participant_char';
  OPEN p_out_participant_char FOR  
    SELECT au.user_name,
           vw.cms_value AS characteristic_name,
           uc.characteristic_value
      FROM application_user au,
           user_characteristic uc,
           characteristic c,
           vw_cms_asset_value vw,
           gtt_id_list gil_u
     WHERE au.user_id = uc.user_id
       AND uc.characteristic_id = c.characteristic_id
       AND c.cm_asset_code = vw.asset_code
       AND vw.locale = 'en_US'
       AND au.user_id = gil_u.id;
       
   prc_execution_log_entry(c_process_name, c_release_level, 'INFO', 'Success', NULL);
   
EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := 99;
    prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', 'Error at stgae '||v_stage||'-->'||SQLERRM, NULL);
    OPEN p_out_application_user FOR SELECT * FROM DUAL;
    OPEN p_out_participant  FOR SELECT * FROM DUAL;
    OPEN p_out_participant_address  FOR SELECT * FROM DUAL;
    OPEN p_out_participant_email  FOR SELECT * FROM DUAL;
    OPEN p_out_participant_phone  FOR SELECT * FROM DUAL;
    OPEN p_out_org_unit_participant  FOR SELECT * FROM DUAL;
    OPEN p_out_participant_employer  FOR SELECT * FROM DUAL;
    OPEN p_out_participant_char  FOR SELECT * FROM DUAL; 
END prc_g_to_honeycomb_sync;
/
