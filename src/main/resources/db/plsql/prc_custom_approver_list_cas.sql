CREATE OR REPLACE PROCEDURE prc_custom_approver_list_cas
         ( p_in_promotion_id        IN  NUMBER,
           p_in_level_number        IN  NUMBER,
           p_in_recipient_user_id   IN  VARCHAR2,
           p_in_behavior            IN  VARCHAR2,
           p_in_award_amount        IN  VARCHAR2,
           p_in_nominator_id        IN  NUMBER,
           p_in_is_team             IN  NUMBER,
           p_out_returncode         OUT NUMBER,         
           p_out_approver_list      OUT SYS_REFCURSOR)
AS
 /*******************************************************************************
  -- Purpose: To return the custom  approvers to create entry in claim_approver_snapshot
  --
  -- Person             Date         Comments
  -- -----------        --------    -----------------------------------------------------
  -- nagarajs          05/18/2016    Initial Creation      
  --Sherif Basha       02/08/2107    Bug 71184 - Modify Characteristic Approval Routing - Current only routes via Nominee
  --Sherif Basha      02/13/2017     Bug 71422 - Approve Nomination - 5 level nomination with characteristic at all 5 levels with 'payout at each level
                                     didn't go to the 5th level approver; the characteristic is 'by nominee' route  (characteristics date value was not compared as date comparison:separating date and decimal char types) 
  *******************************************************************************/
   c_process_name           CONSTANT execution_log.process_name%type :='PRC_CUSTOM_APPROVER_LIST_CAS';
   c_release_level          CONSTANT execution_log.release_level%type := '1.0';
   
BEGIN

   
  p_out_returncode := 0;
  
  OPEN p_out_approver_list FOR 
    WITH approv AS
          ( SELECT a.user_id, un.node_id,ac.approver_value, ac.min_val, ac.max_val, 
                   ao.approver_type, ao.characteristic_id,ao.approval_routing_type,pml.award_amount_type_fixed,
                   c.characteristic_data_type
              FROM approver a,
                   approver_criteria ac,
                   approver_option ao,
                   user_node un,
                   promo_nomination pn,
                   promo_nomination_level pml,
                   characteristic c
             WHERE a.approver_criteria_id = ac.approver_criteria_id
               AND ac.approver_option_id = ao.approver_option_id
               AND ao.approval_round = p_in_level_number
               AND ao.promotion_id = p_in_promotion_id
               AND ao.promotion_id = pml.promotion_id (+)
               AND ao.promotion_id = pn.promotion_id
               AND ((ao.approval_round = pml.level_index AND pn.payout_level_type = 'eachLevel')
                   OR (pn.payout_level_type = 'finalLevel' ))
               AND a.user_id = un.user_id (+)
               AND un.is_primary (+)= 1
               AND ao.characteristic_id = c.characteristic_id(+)
          )
   SELECT user_id,
          node_id
     FROM approv
    WHERE approver_type = 'behavior'
      AND approver_value IN (SELECT * FROM TABLE (get_array_varchar (p_in_behavior)))
    UNION
    SELECT approv.user_id,
          node_id
     FROM approv,
          (SELECT user_id_csv.user_id AS user_id,
                  TO_NUMBER(payout_amt_csv.payout_amt) AS payout_amt
            FROM (SELECT ROWNUM row_num, COLUMN_VALUE AS user_id FROM TABLE (get_array_varchar (p_in_recipient_user_id))) user_id_csv,
                 (SELECT ROWNUM row_num, COLUMN_VALUE AS payout_amt FROM TABLE (get_array_varchar (p_in_award_amount))) payout_amt_csv
           WHERE user_id_csv.row_num = payout_amt_csv.row_num
          ) inputs
    WHERE approver_type = 'award'
      AND ((award_amount_type_fixed =  0 AND  inputs.payout_amt  BETWEEN min_val AND max_val))
           OR (award_amount_type_fixed =  1 AND  (inputs.payout_amt  = min_val))
    UNION
   SELECT ap.user_id,
          ap.node_id
     FROM approv ap,
          user_characteristic uc
    WHERE ap.approver_type = 'characteristic'
      AND ap.characteristic_data_type IN ('decimal')  --02/13/2017     Bug 71422 separated date type from here and handled
      AND ap.characteristic_id = uc.characteristic_id
      AND uc.user_id IN (SELECT * FROM TABLE (get_array (DECODE(p_in_is_team,1,p_in_nominator_id,
                                                                 DECODE(ap.approval_routing_type,'by_nominator',p_in_nominator_id,p_in_recipient_user_id))))) -- 02/08/2107    Bug 71184
      AND uc.characteristic_value BETWEEN SUBSTR(ap.approver_value,1,INSTR(ap.approver_value,'-')-1) 
      AND SUBSTR(ap.approver_value,INSTR(ap.approver_value,'-')+1) 
      UNION
   --02/13/2017  Bug 71422 date char type handled here  
   SELECT ap.user_id,
          ap.node_id
     FROM approv ap,
          user_characteristic uc
    WHERE ap.approver_type = 'characteristic'
      AND ap.characteristic_data_type IN ('date')
      AND ap.characteristic_id = uc.characteristic_id
      AND uc.user_id IN (SELECT * FROM TABLE (get_array (DECODE(p_in_is_team,1,p_in_nominator_id,
                                                                 DECODE(ap.approval_routing_type,'by_nominator',p_in_nominator_id,p_in_recipient_user_id))))) -- 02/08/2107    Bug 71184
      AND to_date(uc.characteristic_value,'MM/DD/YYYY') BETWEEN to_date(SUBSTR(ap.approver_value,1,INSTR(ap.approver_value,'-')-1),'MM/DD/YYYY')
      AND to_date(SUBSTR(ap.approver_value,INSTR(ap.approver_value,'-')+1),'MM/DD/YYYY')
      UNION
      SELECT ap.user_id,
          ap.node_id
     FROM approv ap,
          user_characteristic uc
    WHERE ap.approver_type = 'characteristic'
      AND ap.characteristic_data_type IN ('int')
      AND ap.characteristic_id = uc.characteristic_id
      AND uc.user_id IN (SELECT * FROM TABLE (get_array (DECODE(p_in_is_team,1,p_in_nominator_id,
                                                                 DECODE(ap.approval_routing_type,'by_nominator',p_in_nominator_id,p_in_recipient_user_id)))))   -- 02/08/2107    Bug 71184
      AND uc.characteristic_value BETWEEN TO_NUMBER(SUBSTR(ap.approver_value,1,INSTR(ap.approver_value,'-')-1))
      AND TO_NUMBER(SUBSTR(ap.approver_value,INSTR(ap.approver_value,'-')+1))  
    UNION
   SELECT ap.user_id,
          ap.node_id
     FROM approv ap,
          user_characteristic uc
    WHERE ap.approver_type = 'characteristic'
      AND ap.characteristic_data_type IN ('boolean','single_select','txt')
      AND ap.characteristic_id = uc.characteristic_id
      AND uc.user_id IN (SELECT * FROM TABLE (get_array (DECODE(p_in_is_team,1,p_in_nominator_id,
                                                                 DECODE(ap.approval_routing_type,'by_nominator',p_in_nominator_id,p_in_recipient_user_id)))))    -- 02/08/2107    Bug 71184
      AND LOWER(uc.characteristic_value) = LOWER(ap.approver_value)
    UNION
   SELECT ap.user_id,
          ap.node_id
     FROM approv ap,
          user_characteristic uc
    WHERE ap.approver_type = 'characteristic'
      AND ap.characteristic_data_type = 'multi_select'
      AND ap.characteristic_id = uc.characteristic_id
      AND uc.user_id IN (SELECT * FROM TABLE (get_array (DECODE(p_in_is_team,1,p_in_nominator_id,
                                                                 DECODE(ap.approval_routing_type,'by_nominator',p_in_nominator_id,p_in_recipient_user_id)))))   -- 02/08/2107    Bug 71184
      AND REGEXP_LIKE(LOWER(ap.approver_value) ,'(^|,)'||REPLACE(LOWER(uc.characteristic_value),',','|')||'(,|$)');
                                               
EXCEPTION 
  WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, c_release_level, 'ERROR', SQLERRM, NULL);
      p_out_returncode := 99;

END;
/
